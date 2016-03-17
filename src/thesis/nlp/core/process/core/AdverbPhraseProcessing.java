/**
 * 
 */
package thesis.nlp.core.process.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import thesis.nlp.core.process.SentenceConstants;
import thesis.nlp.core.process.StanfordTreeTypedDependConsts;
import thesis.nlp.core.process.core.verb.VerbProcessing;
import thesis.nlp.models.AdverbPhrase;
import thesis.nlp.models.NounPhrase;
import thesis.nlp.models.Predicate;
import thesis.nlp.models.PrepositionalPhrase;
import thesis.nlp.models.SBAR;
import thesis.nlp.models.Subject;
import thesis.nlp.models.Verb;
import thesis.nlp.util.TypedDependencyCheckUtil;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author lohuynh
 *
 */
public class AdverbPhraseProcessing {
	private String adv = "";
	private int advIdx = -1;
	private static AdverbPhraseProcessing instance = null;

	private AdverbPhraseProcessing() {
	}

	public static AdverbPhraseProcessing getInstance() {
		if (instance == null)
			return new AdverbPhraseProcessing();
		return instance;
	}

	public AdverbPhrase processAdvPhrase(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb) {
		// if (sbarTree == null) {
		// return null;
		// }
		AdverbPhrase advPhrase = null;
		SBAR sbarAdvPhrase = processSbarInAdvPhrase(tdls, taggedWords, verb);
		NounPhrase nounPhrase = processNPInAdvPhrase(tdls, taggedWords, verb);
		List<PrepositionalPhrase> pps = new ArrayList<PrepositionalPhrase>();
		pps = processPPinAdvPhraseByTDep(tdls, taggedWords, this.advIdx);

		if (sbarAdvPhrase != null || nounPhrase != null || !this.adv.equals(""))
		{
			advPhrase = new AdverbPhrase(sbarAdvPhrase, pps, nounPhrase, this.adv);
		}
		return advPhrase ;
	}
	
	public List<PrepositionalPhrase> processPPinAdvPhraseByTDep(List<TypedDependency> tdls, List<CoreLabel> taggedWords, int advIdx) {
	/*
	 * Exp : He speaks in the low and deep voice => prepPhrase is " in the low and deep voice";
	 */
		
	if (advIdx == -1){
		return new ArrayList<PrepositionalPhrase>();
	}
	List<TypedDependency> prepTdeps = new ArrayList<TypedDependency>();
	prepTdeps = GeneralProcessing.listTypeDepRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PREP, "", advIdx);
	List<PrepositionalPhrase> pps = new ArrayList<PrepositionalPhrase>();
	if (prepTdeps.size() > 0) {
		PrepositionalPhrase pp = null;
		NounPhraseProcessing npp = NounPhraseProcessing.getInstance();
		for (TypedDependency prepTdep : prepTdeps) {
			int prepIdx = -1;
			List<CoreLabel> listHeadNounInPrep = new ArrayList<CoreLabel>();
			CoreLabel cl = new CoreLabel();
			List<NounPhrase> nps = new ArrayList<NounPhrase>();
			String prep = prepTdep.reln().getSpecific();
			if (TypedDependencyCheckUtil.checkATagIsAnNoun(prepTdep.gov().tag())) {
				// prep_in(re-election-10, 2012-12)
				cl.setWord(prepTdep.gov().originalText());
				cl.setIndex(prepTdep.gov().index());
				listHeadNounInPrep.add(cl);
				NounPhrase np = npp.processNounPhraseByTDep(tdls, taggedWords, listHeadNounInPrep);
				nps.add(np);
				pp = new PrepositionalPhrase(prep, nps, prepIdx);
			} else {

				// prep_to(won-2, presidency-6)
				prepIdx = prepTdep.gov().index() + 1; // prep_to(won-2, presidency-6)
				cl.setWord(prepTdep.dep().originalText());
				cl.setIndex(prepTdep.dep().index());
				cl.setTag(prepTdep.dep().tag());
				listHeadNounInPrep.add(cl);
				NounPhrase np = npp.processNounPhraseByTDep(tdls, taggedWords, listHeadNounInPrep);
				nps.add(np);
				prep = prepTdep.reln().getSpecific();
				pp = new PrepositionalPhrase(prep, nps, prepIdx);
			}

			pps.add(pp);
		}
	}
	return pps;
}

	/**
	 * This function return subordinator as Adverb for a Sbar. 
	 * <p>For exp: even though, as long as, as soon as</p>
	 * @param tdls
	 * @param verb
	 * @return
	 */
	private String adverb(List<TypedDependency> tdls, Verb verb) {
		String str = "";
		Set<String> subs = new HashSet<String>(Arrays.asList(new String[] { "if", "though", "case" })); // even if, even though, in case, as long as,
																										// as soon as
		for (TypedDependency tdl : tdls) {
			if (tdl.reln().getShortName().equals(StanfordTreeTypedDependConsts.ADVMOD))
			{
				if (tdl.gov().index() == verb.getVerbIdx())
				{
					str = tdl.dep().originalText();
				}
			}
			if (tdl.reln().getShortName().equals(StanfordTreeTypedDependConsts.MARK))
			{
				if (tdl.gov().index() == verb.getVerbIdx())
				{
					// advmod(came-9, even-6)
//					// mark(came-9, if-7)
					String dep = tdl.dep().originalText();
					if (subs.contains(dep)) {
						str = str + " " + dep;
					}
//					else
//					{
//						str = tdl.dep().originalText();
//					}
				}
			}
		}
		return str;
	}
	
	
	private NounPhrase processNPInAdvPhrase(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb)
	{
		// exp: By agreement among the Allied nations, in March 1942 the acific theatre was divided into the South West Pacific Area, under General Douglas MacArthur, and the Pacific Ocean Areas, under Admiral Chester W. Nimitz.
		if (verb == null)
			return null;
		TypedDependency advmodTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.ADVMOD, "", verb.getVerbIdx());
		NounPhrase np = null;
		if (advmodTdep != null)
		{
			this.adv = advString(tdls,advmodTdep);
			this.advIdx = advmodTdep.dep().index();
			TypedDependency pobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.POBJ, "", this.advIdx);
			if (pobjTdep != null)
			{
				List<CoreLabel> lastNounList = GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(pobjTdep, tdls); // list of Head Nouns for NP
				np = NounPhraseProcessing.getInstance().processNounPhraseByTDep(tdls, taggedWords, lastNounList);
			}
		}
		else
		{
			TypedDependency npadvmodTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NPADVMOD, "", verb.getVerbIdx());
			if (npadvmodTdep != null)
			{
//				this.adv = advStringForNPADVMOD(tdls, npadvmodTdep);
				List<CoreLabel> lastNounList = GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(npadvmodTdep, tdls); // list of Head Nouns for NP
				np = NounPhraseProcessing.getInstance().processNounPhraseByTDep(tdls, taggedWords, lastNounList);
			}
		}
		return np;
	}
	
	private String advStringForNPADVMOD(List<TypedDependency> tdls, TypedDependency npadvmodTdep)
	{
		StringBuilder adv = new StringBuilder();
		TypedDependency numTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NUM, "", npadvmodTdep.dep().index());
		if (numTdep != null)
		{
			if (numTdep.dep().index() <numTdep.dep().index())
			{
				adv.append(numTdep.dep().originalText());
				adv.append(" ");
			}
		}
		adv.append(npadvmodTdep.dep().originalText());
		adv.append(" ");
		adv.append(npadvmodTdep.gov().originalText());
		return adv.toString();
	}
	private String advString(List<TypedDependency> tdls, TypedDependency advmodTdep)
	{
		StringBuilder adv = new StringBuilder();
		adv.append(advmodTdep.dep().originalText());
		List<TypedDependency> advTdeps = GeneralProcessing.listTypeDepRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.ADVMOD, "", advmodTdep.dep().index());
		Iterator<TypedDependency> itr = advTdeps.iterator();
		while (itr.hasNext())
		{
			TypedDependency advTdep = itr.next();
			adv.append(" ");
			adv.append(advTdep.dep().originalText());
		}
		return adv.toString();
	}
	private SBAR processSbarInAdvPhrase(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb){
		SBAR sbar = null;
		if ((sbar = processSbarInAdvPhraseForADVCL(tdls, taggedWords, verb))!= null)
			return sbar;
		else if ((sbar = processSbarInAdvPhraseForBUTconj(tdls, taggedWords, verb))!= null)
			return sbar;
		return sbar;
	}

	private SBAR processSbarInAdvPhraseForADVCL(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb) {
		TypedDependency advclTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.ADVCL, "", verb.getVerbIdx());
		if (advclTdep == null) {
			return processSbarInAdvPhraseForVMod(tdls, taggedWords, verb);
		}
		if (!TypedDependencyCheckUtil.checkATagIsAVerb(advclTdep.dep().tag()))
			return null;
		SBAR sbar = null;
		int verbSbarIdx = advclTdep.dep().index();
		String verbSbarName = advclTdep.dep().originalText();
		Verb verbOfSbar = new Verb(verbSbarName, verbSbarIdx, VerbProcessing.stemmingWord(verbSbarName));
		Predicate predicateSbar = SbarUtil.processPredicateInSbarByVerb(tdls, taggedWords, verbOfSbar);
		Subject subjSbar = SbarUtil.processSubjInSbarByVerb(tdls, taggedWords, verbSbarIdx);
		String subordinator = adverb(tdls, verbOfSbar);
		if (subjSbar != null || predicateSbar != null)
			sbar = new SBAR(subjSbar, predicateSbar, subordinator);
		return sbar;
	}
	
	private SBAR processSbarInAdvPhraseForBUTconj(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb){
		TypedDependency conj_butTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.CONJ, StanfordTreeTypedDependConsts.SPECIFIC_BUT, verb.getVerbIdx());
		if (conj_butTdep == null)
			return null;
		if (!TypedDependencyCheckUtil.checkATagIsAVerb(conj_butTdep.dep().tag()))
			return null;
		SBAR sbar = null;
		int verbSbarIdx = conj_butTdep.dep().index();
		String verbSbarName = conj_butTdep.dep().originalText();
		Subject subjSbar = SbarUtil.processSubjInSbarByVerb(tdls, taggedWords, verbSbarIdx);
		Verb verbOfSbar = new Verb(verbSbarName, verbSbarIdx, VerbProcessing.stemmingWord(verbSbarName));
		Predicate predicateSbar = SbarUtil.processPredicateInSbarByVerb(tdls, taggedWords, verbOfSbar);
		if (subjSbar != null || predicateSbar != null)
			sbar = new SBAR(subjSbar, predicateSbar, "");
		return sbar;
	}
	

	/**
	 * In case Sbar in AdvPhrase is a Vmod
	 *<p> Exp: Crying the whole day, he went to the party </p>
	 * @param tdls
	 * @param taggedWords
	 * @param verb
	 * @param subj
	 * @return
	 */
	
	private SBAR processSbarInAdvPhraseForVMod(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb)
	{
		TypedDependency vmodTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.VMOD, "", verb.getVerbIdx()); 
		if (vmodTdep == null)
		{
			return null;
		}
		Subject subjSbar = null;
		Predicate predicateSbar = null;
		int verbSbarIdx = vmodTdep.dep().index();
		String verbSbarName = vmodTdep.dep().originalText();
		Verb verbOfSbar = new Verb(verbSbarName, verbSbarIdx, VerbProcessing.stemmingWord(verbSbarName));
		predicateSbar = SbarUtil.processPredicateInSbarByVerb(tdls, taggedWords, verbOfSbar);
		// No Subordinator
		
		if (subjSbar != null || predicateSbar != null)	
			return new SBAR(subjSbar, predicateSbar, ""); 
		else
			return null;
	}
	
	
	/**
	 * 
	 * @param tdls.	A list of TypedDependency
	 * @param taggedWords.	A list<CoreLabel>
	 * @param adjIndex.	Integer <p> "adjIndex" parameter is the input of adjective word which adverb Phrase support for it</p>
	 * @return
	 */
	
	public SBAR processSbarInAdvPhraseForAdj(List<TypedDependency> tdls, List<CoreLabel> taggedWords, int adjIndex) {
		TypedDependency advclTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.ADVCL, "", adjIndex);
		if (advclTdep == null) {
			return null;
		}
		SBAR sbar = null;
		Subject subjSbar = null;
		Predicate predicateSbar = null;
		String depTagOfAdcvl = advclTdep.dep().tag();
		String subordinator = "";
		if (TypedDependencyCheckUtil.checkATagIsAVerb(depTagOfAdcvl)) {
			int verbSbarIdx = advclTdep.dep().index();

			String verbSbarName = advclTdep.dep().originalText();
			Verb verbOfSbar = new Verb(verbSbarName, verbSbarIdx, VerbProcessing.stemmingWord(verbSbarName));
			predicateSbar = SbarUtil.processPredicateInSbarByVerb(tdls, taggedWords, verbOfSbar);
			subjSbar = SbarUtil.processSubjInSbarByVerb(tdls, taggedWords, verbSbarIdx);
			subordinator = adverb(tdls, verbOfSbar);
			
		}
		else
		{
			TypedDependency copTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.COP, "", advclTdep.dep().index());
			if (copTdep == null)
			{
				return null;
			}
			String verbSbarName =copTdep.dep().originalText();
			Verb verbOfSbar = new Verb(verbSbarName , copTdep.dep().index(), VerbProcessing.stemmingWord(verbSbarName));
			verbOfSbar.setVerbForm(VerbProcessing.verbForm(copTdep.dep().tag()));
			subjSbar = SbarUtil.processSubjInSbarByVerb(tdls, taggedWords, advclTdep.dep().index());
			predicateSbar = SbarUtil.processPredicateInSbarByVerb(tdls, taggedWords, verbOfSbar);
		}
		if (subjSbar != null || predicateSbar != null)	
			sbar = new SBAR(subjSbar, predicateSbar, subordinator);
		return sbar;
	}
}
