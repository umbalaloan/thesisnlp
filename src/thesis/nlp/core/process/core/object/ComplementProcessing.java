/**
 * 
 */
package thesis.nlp.core.process.core.object;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import thesis.nlp.core.process.SentenceConstants;
import thesis.nlp.core.process.StanfordTreeTypedDependConsts;
import thesis.nlp.core.process.core.AdjectivePhraseProcessing;
import thesis.nlp.core.process.core.AdverbPhraseProcessing;
import thesis.nlp.core.process.core.GeneralProcessing;
import thesis.nlp.core.process.core.NounPhraseProcessing;
import thesis.nlp.core.process.core.SbarProcessing;
import thesis.nlp.models.AdjectivePhrase;
import thesis.nlp.models.AdverbPhrase;
import thesis.nlp.models.Complement;
import thesis.nlp.models.NounPhrase;
import thesis.nlp.models.PrepositionalPhrase;
import thesis.nlp.models.SBAR;
import thesis.nlp.models.Verb;
import thesis.nlp.util.TypedDependencyCheckUtil;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author lohuynh
 *
 */
public class ComplementProcessing {
	private static ComplementProcessing instance = null;

	private ComplementProcessing() {}

	public static ComplementProcessing getInstance() {
		if (instance == null)
			return new ComplementProcessing();
		return instance;
	}

	public Complement processComplement(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb) {
		List<AdjectivePhrase> adjPhrases = new ArrayList<AdjectivePhrase>();
		AdverbPhrase advPhrase = null;
		List<NounPhrase> nps = new ArrayList<NounPhrase>();
		PrepositionalPhrase prepby = processPPInCompForAgent(tdls, taggedWords, verb);
		if (verb.getVerbType() == SentenceConstants.COPULAR_VERB) {
			nps = processNPsinCompForCopularVerb(tdls, taggedWords, verb);
			if (nps.size() == 0) {
				adjPhrases = processAdjComplementForCopularVerb(tdls, taggedWords, verb);
				if (adjPhrases.size() == 0) {
					advPhrase = processAdvPhraseInComp(tdls, taggedWords, verb);
				}
			}
			advPhrase = AdverbPhraseProcessing.getInstance().processAdvPhrase(tdls, taggedWords, verb);
		} else {
			nps = processNPsinCompForOtherVerbs(tdls, taggedWords, verb);
			adjPhrases = processAdjComplementForCopularVerb(tdls, taggedWords, verb);
			advPhrase = AdverbPhraseProcessing.getInstance().processAdvPhrase(tdls, taggedWords, verb);
		}
		SBAR compSbar = processCompOfVerb(tdls, taggedWords, verb);
		if (nps.size() >0 || adjPhrases.size() > 0 || advPhrase != null || compSbar != null)
			return new Complement(nps, adjPhrases, advPhrase, prepby, compSbar);
		else
			return null;
	}

	/**
	 * This function process prepPhrase for agent in passive sentence. It means "prep_by"
	 * @param tdls
	 * @param taggedWords
	 * @param verb
	 * @return
	 */
	private PrepositionalPhrase processPPInCompForAgent(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb)
	{
		if (verb.getVerbVoice() == SentenceConstants.VERBVOICE_ACTIVE)
			return null;
		TypedDependency agentTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.AGENT, "", verb.getVerbIdx());
		PrepositionalPhrase prepBy = null;
		if (agentTdep != null)
		{
			String prep = "by";
			int prepIdx = agentTdep.gov().index() + 1; // agent(intended-20, Hill-22)
			List<CoreLabel> listHeadNoun = GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(agentTdep, tdls);
			NounPhrase np = NounPhraseProcessing.getInstance().processNounPhraseByTDep(tdls, taggedWords, listHeadNoun);
			List<NounPhrase> nps = new ArrayList<NounPhrase>();
			nps.add(np);
			prepBy = new PrepositionalPhrase(prep, nps, prepIdx);
		}
		return prepBy;
	}
	private List<NounPhrase> processNPsinCompForCopularVerb(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb) {

		TypedDependency copTdeps = GeneralProcessing.returnOneRelationByDepIdx(tdls, StanfordTreeTypedDependConsts.COP, "", verb.getVerbIdx());
		List<CoreLabel> lastNounInComp = new ArrayList<CoreLabel>();
		List<NounPhrase> nps = new ArrayList<NounPhrase>();
		if (copTdeps != null) {
			// copular Verbs
			String govTag = copTdeps.gov().tag();
			if (TypedDependencyCheckUtil.checkATagIsAnNoun(govTag))
			{
				lastNounInComp = GeneralProcessing.listHeadWordsOfTdepByGovIdx(copTdeps, tdls);
			}

		} else {
			
				List<TypedDependency> vmodTdeps = GeneralProcessing.listTypeDepRelationByDepIdx(tdls, StanfordTreeTypedDependConsts.VMOD, "",
						verb.getVerbIdx());
				if (vmodTdeps.size() > 0) {
					/*
					 * verb in vmod relationship
					 */
					TypedDependency prepTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PREP, "",
							verb.getVerbIdx()); // get prepPhrase after verb. Exp : "composed of a unit
					lastNounInComp = GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(prepTdep, tdls);
	
			}
		}
		Iterator<CoreLabel> itrcl = lastNounInComp.iterator();
		while (itrcl.hasNext()) {
			List<CoreLabel> cls = new ArrayList<CoreLabel>();
			cls.add(itrcl.next());
			NounPhrase np = NounPhraseProcessing.getInstance().processNounPhraseByTDep(tdls, taggedWords, cls);
			nps.add(np);
		}
		return nps;
	}
	
	private List<NounPhrase> processNPsinCompForOtherVerbs(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb){
		List<NounPhrase> nps = processNPsinCompForCCompCases(tdls, taggedWords, verb);
		if (nps.size()==0)
			nps = processNPsinCompForAGENTCase(tdls, taggedWords, verb);
		if (nps.size() == 0)
			nps = processNPsinCompForSpecialCases(tdls, taggedWords, verb);
		return nps;
	}
	
	private List<NounPhrase> processNPsinCompForAGENTCase(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb)
	{
		TypedDependency agentTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.AGENT, "", verb.getVerbIdx());
		if (agentTdep == null)
			return new ArrayList<NounPhrase>();
		List<CoreLabel> lastNounInComp = new ArrayList<CoreLabel>();
		List<NounPhrase> nps = new ArrayList<NounPhrase>();
		lastNounInComp = GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(agentTdep, tdls);
		Iterator<CoreLabel> itrcl = lastNounInComp.iterator();
		while (itrcl.hasNext()) {
			List<CoreLabel> cls = new ArrayList<CoreLabel>();
			cls.add(itrcl.next());
			NounPhrase np = NounPhraseProcessing.getInstance().processNounPhraseByTDep(tdls, taggedWords, cls);
			nps.add(np);
		}
		return nps;
	}
	/**
	 * This function fixed special issue for temporal modifier
	 * <p>Exp: Crying the whole day, he went to Paris</p>
	 * @param tdls
	 * @param taggedWords
	 * @param verb
	 * @return
	 */
	private List<NounPhrase> processNPsinCompForSpecialCases(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb)
	{
		TypedDependency tmodTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.TMOD, "", verb.getVerbIdx());
		List<CoreLabel> lastNounInComp = new ArrayList<CoreLabel>();
		List<NounPhrase> nps = new ArrayList<NounPhrase>();
		if (tmodTdep != null)
		{
			lastNounInComp = GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(tmodTdep, tdls);
		}
		Iterator<CoreLabel> itrcl = lastNounInComp.iterator();
		while (itrcl.hasNext()) {
			List<CoreLabel> cls = new ArrayList<CoreLabel>();
			cls.add(itrcl.next());
			NounPhrase np = NounPhraseProcessing.getInstance().processNounPhraseByTDep(tdls, taggedWords, cls);
			nps.add(np);
		}
		return nps;
	}

	private List<NounPhrase> processNPsinCompForCCompCases(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb)
	{
		TypedDependency ccompTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.CCOMP, "", verb.getVerbIdx());
		if (ccompTdep == null || TypedDependencyCheckUtil.checkATagIsAVerb(ccompTdep.dep().tag()))
			ccompTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.XCOMP, "", verb.getVerbIdx());
		if (ccompTdep == null || TypedDependencyCheckUtil.checkATagIsAVerb(ccompTdep.dep().tag()))
			return new ArrayList<NounPhrase>();
		List<CoreLabel> lastNounInComp = new ArrayList<CoreLabel>();
		List<NounPhrase> nps = new ArrayList<NounPhrase>();
		lastNounInComp = GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(ccompTdep, tdls);
		Iterator<CoreLabel> itrcl = lastNounInComp.iterator();
		while (itrcl.hasNext()) {
			List<CoreLabel> cls = new ArrayList<CoreLabel>();
			cls.add(itrcl.next());
			NounPhrase np = NounPhraseProcessing.getInstance().processNounPhraseByTDep(tdls, taggedWords, cls);
			nps.add(np);
		}
		return nps;
	}
	private AdverbPhrase processAdvPhraseInComp(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb) {
		AdverbPhrase advPhrase = AdverbPhraseProcessing.getInstance().processAdvPhrase(tdls, taggedWords, verb);
		// NOT IMPLEMENT
		return advPhrase;
	}

//	private AdjectivePhrase processAdjComplementForCopularVerb(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb) {
//
//		List<String> adjs = new ArrayList<String>();
//		PrepositionalPhrase ppOfAdj = null;
//		TypedDependency tdepForVerb = null;
//		List<CoreLabel> listAdj = new ArrayList<CoreLabel>();
//		TypedDependency acompTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.ACOMP, "", verb.getVerbIdx());
//		if (acompTdep != null) {
//			tdepForVerb = acompTdep;
//			listAdj = GeneralProcessing.listHeadWordsOfTdepByDepIdx(tdepForVerb, tdls);
//		} 
//		else {
//			/*
//			 * fix error in sentence
//			 * "Java is a general-purpose computer programming language that is concurrent, class-based, object-oriented, and specifically designed to have as few implementation dependencies as possible"
//			 */
//			TypedDependency auxpassTdep = GeneralProcessing.returnOneRelationByDepIdx(tdls, StanfordTreeTypedDependConsts.AUXPASS, "",
//					verb.getVerbIdx());
//			if (auxpassTdep != null
//					&& (auxpassTdep.gov().tag().equals(StanfordTreeTypedDependConsts.JJ) || auxpassTdep.gov().tag()
//							.equals(StanfordTreeTypedDependConsts.JJR))) {
//				tdepForVerb = auxpassTdep;
//				listAdj = GeneralProcessing.listHeadWordsOfTdepByGovIdx(tdepForVerb, tdls);
//				
//			}
//		}
//		if (tdepForVerb == null) {
//			return null;
//		}
//
//		for (CoreLabel cl : listAdj) {
//			ppOfAdj = PrepositionalPhraseProcessing.getInstance().processPPOfAdjPhraseOrAdvPhrase(tdls, taggedWords, cl.index());
//			adjs.add(cl.word());
//			if (ppOfAdj != null)
//				break;
//		}
//
//		return new AdjectivePhrase(adjs, ppOfAdj, null); // NOT IMPLEMENT ADVERB PHRASE
//	}
	
	private List<AdjectivePhrase> processAdjComplementForCopularVerb(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb){
		List<AdjectivePhrase> listAP = new ArrayList<AdjectivePhrase>();
//		List<String> adjs = new ArrayList<String>();
//		PrepositionalPhrase ppOfAdj = null;
		TypedDependency tdepForVerb = null;
		List<CoreLabel> listAdj = new ArrayList<CoreLabel>();
		TypedDependency acompTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.ACOMP, "", verb.getVerbIdx());
		TypedDependency copTdeps = GeneralProcessing.returnOneRelationByDepIdx(tdls, StanfordTreeTypedDependConsts.COP, "", verb.getVerbIdx());
		if (acompTdep != null) {
			tdepForVerb = acompTdep;
			listAdj = GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(tdepForVerb, tdls);
		} 
		else if (copTdeps != null)
		{
			tdepForVerb = copTdeps;
			listAdj = GeneralProcessing.listHeadWordsOfTdepByGovIdx(tdepForVerb, tdls);
		}
		else {
			
			
			/*
			 * fix error in sentence
			 * "Java is a general-purpose computer programming language that is concurrent, class-based, object-oriented, and specifically designed to have as few implementation dependencies as possible"
			 */
			TypedDependency auxpassTdep = GeneralProcessing.returnOneRelationByDepIdx(tdls, StanfordTreeTypedDependConsts.AUXPASS, "",
					verb.getVerbIdx());
			if (auxpassTdep != null
					&& (auxpassTdep.gov().tag().equals(StanfordTreeTypedDependConsts.JJ) || auxpassTdep.gov().tag()
							.equals(StanfordTreeTypedDependConsts.JJR))) {
				tdepForVerb = auxpassTdep;
				listAdj = GeneralProcessing.listHeadWordsOfTdepByGovIdx(tdepForVerb, tdls);
				
			}
		}
		if (tdepForVerb == null) {
			return new ArrayList<AdjectivePhrase>();
		}

		for (CoreLabel cl : listAdj) {
//			ppOfAdj = PrepositionalPhraseProcessing.getInstance().processPPOfAdjPhraseOrAdvPhrase(tdls, taggedWords, cl.index());
//			adjs.add(cl.word());
//			if (ppOfAdj != null)
//				break;
//			String adj = AdjectivePhraseProcessing.getInstance().
//			ppOfAdj = AdjectivePhraseProcessing.getInstance().processPrepPhraseByAdjIndex(tdls, taggedWords, cl.index());
			AdjectivePhrase adjphrase = AdjectivePhraseProcessing.getInstance().processAdjectPhrase(tdls, taggedWords, cl);
			listAP.add(adjphrase);
		}

		return listAP;
	}

	/**
	 * 
	 * @param tdls
	 * @param taggedWords
	 * @param verb
	 *            . Main Verb which is followed by Clausal Complement
	 * @param ccompTdep
	 * @return
	 */
	private SBAR processCompOfVerb(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb) {
		SBAR sbarcomp = SbarProcessing.getInstance().processSbarOfVerbByTdep(tdls, taggedWords, verb);
		return sbarcomp;
	}
}
