/**
 * 
 */
package thesis.nlp.core.process.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import thesis.nlp.core.process.SentenceConstants;
import thesis.nlp.core.process.StanfordTreeTypedDependConsts;
import thesis.nlp.core.process.core.verb.VerbProcessing;
import thesis.nlp.models.NounPhrase;
import thesis.nlp.models.Predicate;
import thesis.nlp.models.PrepositionalPhrase;
import thesis.nlp.models.SBAR;
import thesis.nlp.models.SingleNP;
import thesis.nlp.models.Subject;
import thesis.nlp.models.Verb;
import thesis.nlp.util.NounPhraseUtil;
import thesis.nlp.util.TypedDependencyCheckUtil;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author lohuynh
 * @History
 * 18/10/2015 : change parameter Verb into VerbPhrase in function processPPOfVerb
 *
 */
public class PrepositionalPhraseProcessing {

	private static PrepositionalPhraseProcessing instance = null;

	private PrepositionalPhraseProcessing() {
	}

	public static PrepositionalPhraseProcessing getInstance() {
		if (instance == null)
			return new PrepositionalPhraseProcessing();
		return instance;
	}
/*
	public List<PrepositionalPhrase> processPPOfVerb(List<TypedDependency> tdls, List<CoreLabel> taggedWords, VerbPhrase vp) {
		if (vp == null) {
			return new ArrayList<PrepositionalPhrase>();
		}
		List<PrepositionalPhrase> pps = new ArrayList<PrepositionalPhrase>();
		String particle = vp.getPartical();
		for (TypedDependency tdl : tdls) {
			if ((tdl.gov().index() == vp.getVerb().getVerbIdx()) && (tdl.reln().getShortName().toUpperCase().equals(StanfordTreeTypedDependConsts.PREP.toUpperCase()))) {
				if (particle.equals("") || ((particle != null) && (!tdl.reln().getSpecific().toUpperCase().equals(particle.toUpperCase())))) {
					List<CoreLabel> listLastNounInPP = GeneralProcessing.listHeadWordsOfTdepByDepIdx(tdl, tdls);
					NounPhrase np = NounPhraseProcessing.getInstance().processNounPhraseByTDep(tdls, taggedWords, listLastNounInPP);
					SBAR sbarInPP = null;
//					for (CoreLabel lastNounInPP : listLastNounInPP) {
//						sbarInPP = SbarProcessing.getInstance().processSbarOfNPByTdep(tdls, taggedWords, lastNounInPP);
//						if (sbarInPP != null)
//							break;
//					}
					for (SingleNP singleNP : np.getSingleNPList())
					{
						sbarInPP = SbarProcessing.getInstance().processSbarOfNPByTdep(tdls, taggedWords, singleNP);
						if (sbarInPP != null)
						{
							break;
						}
					}
					PrepositionalPhrase pp = new PrepositionalPhrase();
					pp.setprep(tdl.reln().getSpecific());
					pp.setNounPhrase(np);
					pp.setSbarPrepPhrase(sbarInPP);
					pps.add(pp);
				}
			}
		}
		return pps;
	}*/
	
	public List<PrepositionalPhrase> processPPOfVerb(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb, String particle) {
		if (verb == null) {
			return new ArrayList<PrepositionalPhrase>();
		}
		int prepGovIdx = verb.getVerbIdx();
		if (verb.getVerbType() == SentenceConstants.COPULAR_VERB)
		{
			TypedDependency copTdep = GeneralProcessing.returnOneRelationByDepIdx(tdls, StanfordTreeTypedDependConsts.COP, "", prepGovIdx);
			if (copTdep != null)
				prepGovIdx = copTdep.gov().index();
		}
		List<PrepositionalPhrase> pps = new ArrayList<PrepositionalPhrase>();
		
		for (TypedDependency tdl : tdls) {
			if ((tdl.gov().index() == prepGovIdx) && (tdl.reln().getShortName().toUpperCase().equals(StanfordTreeTypedDependConsts.PREP.toUpperCase()))) {
				if (TypedDependencyCheckUtil.checkParticleInVerb(particle, tdl)) {
					List<CoreLabel> listLastNounInPP = GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(tdl, tdls);
					List<NounPhrase> nps = new ArrayList<NounPhrase>();
					for (CoreLabel cl : listLastNounInPP)
					{
						List<CoreLabel> cls = new ArrayList<CoreLabel>();
						cls.add(cl);
						NounPhrase np = NounPhraseProcessing.getInstance().processNounPhraseByTDep(tdls, taggedWords, cls);
						nps.add(np);
					}
					SBAR sbarInPP = null;
//					if (nps.size() >0)
//					{
//						Iterator<NounPhrase> np
//						for (SingleNP singleNP : np.getSingleNPList())
//						{
//							sbarInPP = SbarProcessing.getInstance().processSbarOfNPByTdep(tdls, taggedWords, singleNP);
//							if (sbarInPP != null)
//							{
//								break;
//							}
//						}
//					}
					PrepositionalPhrase pp = new PrepositionalPhrase();
					pp.setprep(tdl.reln().getSpecific());
					pp.setNounPhraseList(nps);
//					pp.setSbarPrepPhrase(sbarInPP);
					pps.add(pp);
				}
			}
		}
		return pps;
	}

	/**
	 * 
	 * @param tdls
	 * @param taggedWords
	 * @param headNounIdx
	 * @return
	 */
	public Set<PrepositionalPhrase> processPPsOfNP(List<TypedDependency> tdls, List<CoreLabel> taggedWords, SingleNP singleNP) {
		/*
		 * Exp : The girl with black eyes and brown hair is my friend
		 */
		int headNounIdx = singleNP.getHeadNounIdx();
		List<TypedDependency> prepTdeps = NounPhraseUtil.listPrepTdepsSupportForNPByGovIdx(tdls, StanfordTreeTypedDependConsts.PREP, "", headNounIdx);
		if (prepTdeps.size() == 0) {
			// Exp : "Crick stressed the importance of finding new methods to probe human brain function."
			prepTdeps = GeneralProcessing.listTypeDepRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PREPC, "", headNounIdx);
		}
		if (prepTdeps.size() == 0)
		{
			return new LinkedHashSet<PrepositionalPhrase>();
		}
		Set<PrepositionalPhrase> pps = new LinkedHashSet<PrepositionalPhrase>();
		for (TypedDependency prepTdep : prepTdeps)
		{
			List<CoreLabel> listlastHeadNounOfPP = GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(prepTdep, tdls);
			NounPhrase npOfPP = NounPhraseProcessing.getInstance().processNounPhraseByTDep(tdls, taggedWords, listlastHeadNounOfPP);
			List<NounPhrase> nps = new ArrayList<NounPhrase>();
			nps.add(npOfPP);
			// FOR SBAR in PREPPHRASE : not implement
			
			SBAR sbarOfPP = processSbarOfPrepPhrase(tdls, taggedWords, prepTdep, singleNP);
//			SBAR sbarOfPP = null;
			PrepositionalPhrase pp = new PrepositionalPhrase(prepTdep.reln().getSpecific(), nps, sbarOfPP); // NOT store SBAR OF PREP in this case
			pps.add(pp);
		}
		return pps;
	}

	private SBAR processSbarOfPrepPhrase(List<TypedDependency> tdls, List<CoreLabel> taggedWords, TypedDependency prepTdep, SingleNP singleNP)
	{
		if (!TypedDependencyCheckUtil.checkATagIsAVerb(prepTdep.dep().tag()))
			return null;
		int verbSbarIdx = prepTdep.dep().index();
		Verb verbOfSbar = new Verb(prepTdep.dep().originalText(), verbSbarIdx, VerbProcessing.stemmingWord(prepTdep.dep().originalText()));
		SBAR sbar = null;
		String refSubj = singleNP.toString().trim();
		Subject subjSbar = SbarUtil.processSubjInSbarByVerb(tdls, taggedWords, verbSbarIdx);
		Predicate predicateSbar = SbarUtil.processPredicateInSbarByVerb(tdls, taggedWords, verbOfSbar);
		if (subjSbar != null || predicateSbar != null)
			sbar = new SBAR(subjSbar, predicateSbar, "", refSubj);
		return sbar;
	}
	/**
	 * 
	 * @param tdls
	 * @param taggedWords
	 * @param adjIdxOrAdvIdx
	 * @return
	 */
	public PrepositionalPhrase processPPOfAdjPhraseOrAdvPhrase(List<TypedDependency> tdls, List<CoreLabel> taggedWords, int adjIdxOrAdvIdx) {
		/*
		 * Exp : The girl with black eyes and brown hair is my friend
		 */
		TypedDependency prepTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PREP, "", adjIdxOrAdvIdx);
		if (prepTdep == null) {
			return null;
		}
		List<CoreLabel> listlastHeadNounOfPP = GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(prepTdep, tdls);
		NounPhrase npOfPP = NounPhraseProcessing.getInstance().processNounPhraseByTDep(tdls, taggedWords, listlastHeadNounOfPP);
		List<NounPhrase> nps = new ArrayList<NounPhrase>();
		nps.add(npOfPP);
		PrepositionalPhrase pp = new PrepositionalPhrase(prepTdep.reln().getSpecific(), nps); // NOT store SBAR OF PREP in this case
		return pp;
	}
	
	/**
	 * ToInf Clause as a PrepPositional Phrase
	 * <p>Exp: It is too good to be true</p>
	 * @param tdls
	 * @param taggedWords
	 * @param adjIdx
	 * @return
	 */
	public PrepositionalPhrase processToInfClauseAsPrepPhrase (List<TypedDependency> tdls, List<CoreLabel> taggedWords, int adjIdx)
	{
		/*
		 * This method consider To-Inf Clause as a prepositionPharse in Adj
		 * Exp: It is too good to be true
		 */
		
		TypedDependency xcomp = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.XCOMP, "", adjIdx);
		if (xcomp == null)
		{
			return null;
		}
		List<NounPhrase> nps = new ArrayList<NounPhrase>();
		int prepIdx = -1;
		TypedDependency aux = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.AUX, "", xcomp.dep().index());
		String prep = "";
		if (aux != null)
		{
			prep = aux.dep().originalText();
			prepIdx = aux.dep().index();
		}
		
		Subject subjSbar = null;
		String tagOfXcomp = xcomp.dep().tag();
		Verb verbOfSbar = new Verb();
		if ((tagOfXcomp.equals(StanfordTreeTypedDependConsts.VB)))
		{
			verbOfSbar.setVerbName(xcomp.dep().originalText());
			verbOfSbar.setVerbIdx(xcomp.dep().index());
			verbOfSbar.setStemmedVerb(VerbProcessing.stemmingWord(xcomp.dep().originalText()));
			verbOfSbar.setVerbForm(VerbProcessing.verbForm(xcomp.dep().tag()));
		}
		else
		{
			// for case: It is too good to be true
			/*
			 * aux(true-6, to-4) ; 
				cop(true-6, be-5) ; 
				xcomp(good-3, true-6) ;
			 */
			TypedDependency copTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.COP, "", xcomp.dep().index());
			if (copTdep == null)
			{
				return null;
			}
			verbOfSbar.setVerbName(copTdep.dep().originalText());
			verbOfSbar.setVerbIdx(copTdep.dep().index());
			verbOfSbar.setStemmedVerb(VerbProcessing.stemmingWord(copTdep.dep().originalText()));
			verbOfSbar.setVerbForm(VerbProcessing.verbForm(copTdep.dep().tag()));
		}
		Predicate predicateSbar = SbarUtil.processPredicateInSbarByVerb(tdls, taggedWords, verbOfSbar);
		SBAR sbarOfPP = new SBAR(subjSbar, predicateSbar, "");
		return new PrepositionalPhrase(prep,nps,sbarOfPP, prepIdx);
	}
}
