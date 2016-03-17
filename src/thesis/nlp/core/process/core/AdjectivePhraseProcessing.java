/**
 * 
 */
package thesis.nlp.core.process.core;

import java.util.ArrayList;
import java.util.List;

import thesis.nlp.core.process.StanfordTreeTypedDependConsts;
import thesis.nlp.core.process.core.verb.VerbProcessing;
import thesis.nlp.models.AdjectivePhrase;
import thesis.nlp.models.AdverbPhrase;
import thesis.nlp.models.NounPhrase;
import thesis.nlp.models.Predicate;
import thesis.nlp.models.PrepositionalPhrase;
import thesis.nlp.models.SBAR;
import thesis.nlp.models.Subject;
import thesis.nlp.models.Verb;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author lohuynh
 *
 * http://examples.yourdictionary.com/example-adjective-clauses.html
 */
public class AdjectivePhraseProcessing {

	private static AdjectivePhraseProcessing instance = null;

	private AdjectivePhraseProcessing() {}

	public static AdjectivePhraseProcessing getInstance() {
		if (instance == null)
			return new AdjectivePhraseProcessing();
		return instance;
	}
	/**
	 * This function return AdjectivePhrase in adjective Tree
	 * 
	 * @param adjTree
	 * @param taggedWords
	 * @param tdls
	 * @param taggedWords
	 * @return
	 */
	public AdjectivePhrase processAdjectPhrase( List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb) {
		
//		String adj = getValueOfAdjective(tdls, taggedWords, verb);
		CoreLabel cl = getAdjColabel(tdls, taggedWords, verb);
		String adj = "";
		int adjIndex = -1;
		if (cl != null)
		{
			adj = cl.originalText();
			adjIndex = cl.index();
		}
		PrepositionalPhrase pp = processPrepPhraseofAdjPhrase(tdls, taggedWords, adjIndex);
		AdverbPhrase advPhrase = processAdvPhrase(tdls, taggedWords, adjIndex);
		AdjectivePhrase adjPhrase = new AdjectivePhrase(adj, pp, advPhrase);
		return adjPhrase;
	}
	
	public AdjectivePhrase processAdjectPhrase( List<TypedDependency> tdls, List<CoreLabel> taggedWords, CoreLabel headAdj) {
		
//		String adj = getValueOfAdjective(tdls, taggedWords, verb);
		
		String adj = getValueOfAdjective(tdls, taggedWords, headAdj);
		int adjIndex = headAdj.index();
		PrepositionalPhrase pp = processPrepPhraseofAdjPhrase(tdls, taggedWords, adjIndex);
		AdverbPhrase advPhrase = processAdvPhrase(tdls, taggedWords, adjIndex);
		AdjectivePhrase adjPhrase = new AdjectivePhrase(adj, pp, advPhrase);
		return adjPhrase;
	}
	
	private PrepositionalPhrase processPrepPhraseofAdjPhrase(List<TypedDependency> tdls, List<CoreLabel> taggedWords, int adjIndex)
	{
		if (adjIndex == -1)
		{
			return null;
		}
		PrepositionalPhrase pp = null;
		if ((pp = PrepositionalPhraseProcessing.getInstance().processPPOfAdjPhraseOrAdvPhrase(tdls, taggedWords, adjIndex)) != null)
		{
			return pp;
		}
		else if ( (pp = PrepositionalPhraseProcessing.getInstance().processToInfClauseAsPrepPhrase(tdls, taggedWords, adjIndex)) != null)
		{
			return pp;
		}
		else if ((pp = processAClauseAsPrepPhrase(tdls, taggedWords, adjIndex))!= null)
		{
			return pp;
		}
		else
			return null;
	}



	/**
	 * This function extract Adverb Phrase for Adjective . NOT COMPLEMENT IMPLEMENT
	 * @param tdeps
	 * @param taggedWords
	 * @param adjIndex
	 * @return
	 */
	private AdverbPhrase processAdvPhrase( List<TypedDependency> tdls, List<CoreLabel> taggedWords, int adjIndex) {
		if (adjIndex == -1)
		{
			return null;
		}
		NounPhrase np = null; // no noun phrase of AdverbPhrase
		String adv = "" ; // not implement
		SBAR sbarOfAdv = AdverbPhraseProcessing.getInstance().processSbarInAdvPhraseForAdj(tdls, taggedWords, adjIndex);
		List<PrepositionalPhrase> pps = new ArrayList<PrepositionalPhrase>(); // not implement
		if (sbarOfAdv == null)
			return null;
		else 
			return new AdverbPhrase(sbarOfAdv, pps, np, adv) ;
	}
	
	private String getValueOfAdjective (List<TypedDependency> tdls, List<CoreLabel> taggedWords, CoreLabel headAdj)
	{
		//  constitute of adj is : "Adverb + adject). Exp : too good, extremely good
		String adj = "";
		adj = headAdj.word();
		TypedDependency advmodTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.ADVMOD, "", headAdj.index());
		if (advmodTdep != null)
		{
			adj = advmodTdep.dep().originalText() + " " + adj;
		}
		else
		{
			TypedDependency npadvmodTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NPADVMOD, "", headAdj.index());
			if (npadvmodTdep != null)
			{
				adj = npadvmodTdep.dep().originalText() + " " + adj;
				TypedDependency numTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NUM, "", npadvmodTdep.dep().index());
				if (numTdep != null)
					adj = numTdep.dep().originalText() + " " + adj;
			}
			
		}
		
		return adj;
	}
	
	private  CoreLabel getAdjColabel (List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb)
	{
	//  constitute of adj is : "Adverb + adject). Exp : too good, extremely good
			CoreLabel cl = null;
			TypedDependency copTdep = GeneralProcessing.returnOneRelationByDepIdx(tdls, StanfordTreeTypedDependConsts.COP, "", verb.getVerbIdx());
			if (copTdep != null)
			{
				if ((copTdep.gov().tag().equals(StanfordTreeTypedDependConsts.JJ)) || (copTdep.gov().tag().equals(StanfordTreeTypedDependConsts.JJR)))
				{
					cl = new CoreLabel();
					String adj = copTdep.gov().originalText();
					TypedDependency advmodTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.ADVMOD, "", copTdep.gov().index());
					if (advmodTdep != null)
					{
						adj = advmodTdep.dep().originalText() + " " + adj;
					}
					cl.setOriginalText(adj);
					cl.setWord(adj);
					cl.setIndex(copTdep.gov().index());
					cl.setTag(copTdep.gov().tag());
				}
			}
			else
			{
				// OTher case
			}
			return cl;
	}
	
	/**
	 * This function process a clause support for an adjective
	 * <p>For exp: "He is smart and fat but he does not want to play football."</p>
	 * @param tdls
	 * @param taggedWords
	 * @param adjIdx
	 * @return
	 */
	private PrepositionalPhrase processAClauseAsPrepPhrase (List<TypedDependency> tdls, List<CoreLabel> taggedWords, int adjIdx)
	{
		/*
		 * nsubj(want-10, he-7)
			nsubj(play-12, he-7)
			aux(want-10, does-8)
			neg(want-10, not-9)
			ccomp(smart-3, want-10)
			aux(play-12, to-11)
		 */
		
		TypedDependency ccomp = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.CCOMP, "", adjIdx);
		if (ccomp == null)
		{
			return null;
		}
		List<NounPhrase> nps = new ArrayList<NounPhrase>();
		String prep = "";
		int prepIdx = -1;
		
		String tagOfCcomp = ccomp.dep().tag();
		Verb verbOfSbar = new Verb();
		if ((tagOfCcomp.equals(StanfordTreeTypedDependConsts.VB)) || (tagOfCcomp.equals(StanfordTreeTypedDependConsts.VBD)) ||
				(tagOfCcomp.equals(StanfordTreeTypedDependConsts.VBG)) ||
				(tagOfCcomp.equals(StanfordTreeTypedDependConsts.VBN)) ||
				(tagOfCcomp.equals(StanfordTreeTypedDependConsts.VBP)) ||
				(tagOfCcomp.equals(StanfordTreeTypedDependConsts.VBZ)))
		{
			verbOfSbar.setVerbName(ccomp.dep().originalText());
			verbOfSbar.setVerbIdx(ccomp.dep().index());
			verbOfSbar.setStemmedVerb(VerbProcessing.stemmingWord(ccomp.dep().originalText()));
			verbOfSbar.setVerbForm(VerbProcessing.verbForm(ccomp.dep().tag()));
		}
		else
		{
			// for case: It is too good to be true
			/*
			 * aux(true-6, to-4) ; 
				cop(true-6, be-5) ; 
				xcomp(good-3, true-6) ;
			 */
			TypedDependency copTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.COP, "", ccomp.dep().index());
			if (copTdep == null)
			{
				return null;
			}
			verbOfSbar.setVerbName(copTdep.dep().originalText());
			verbOfSbar.setVerbIdx(copTdep.dep().index());
			verbOfSbar.setStemmedVerb(VerbProcessing.stemmingWord(copTdep.dep().originalText()));
			verbOfSbar.setVerbForm(VerbProcessing.verbForm(copTdep.dep().tag()));
		}
		Subject subjSbar = SbarUtil.processSubjInSbarByVerb(tdls, taggedWords, ccomp.dep().index());
		Predicate predicateSbar = SbarUtil.processPredicateInSbarByVerb(tdls, taggedWords, verbOfSbar);
		SBAR sbarOfPP = new SBAR(subjSbar, predicateSbar, "");
		return new PrepositionalPhrase(prep,nps,sbarOfPP, prepIdx);
	}

//	/**
//	 * 
//	 * @param tdls.	A list of TypedDependency
//	 * @param taggedWords.	A list<CoreLabel>
//	 * @param adjIndex.	Integer <p> "adjIndex" parameter is the input of adjective word which adverb Phrase support for it</p>
//	 * @return
//	 */
//	private SBAR processSbarInAdvPhrase(List<TypedDependency> tdls, List<CoreLabel> taggedWords, int adjIndex) {
//		TypedDependency advclTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.ADVCL, "", adjIndex);
//		if (advclTdep == null) {
//			return null;
//		}
//		SBAR sbar = null;
//		Subject subjSbar = SbarProcessing.getInstance().processSubjInSbarByVerb(tdls, taggedWords, adjIndex);
//		Predicate predicateSbar = null;
//		String depTagOfAdcvl = advclTdep.dep().tag();
//		String subordinator = "";
//		if (depTagOfAdcvl.equals(StanfordTreeTypedDependConsts.VB) || (depTagOfAdcvl.equals(StanfordTreeTypedDependConsts.VBD))
//				|| (depTagOfAdcvl.equals(StanfordTreeTypedDependConsts.VBN)) || (depTagOfAdcvl.equals(StanfordTreeTypedDependConsts.VBZ))
//				|| (depTagOfAdcvl.equals(StanfordTreeTypedDependConsts.VBP))) {
//			int verbSbarIdx = advclTdep.dep().index();
//
//			String verbSbarName = advclTdep.dep().originalText();
//			Verb verbOfSbar = new Verb(verbSbarName, verbSbarIdx, VerbProcessing.stemmingWord(verbSbarName));
//			predicateSbar = SbarProcessing.getInstance().processPredicateInSbarByVerb(tdls, taggedWords, verbOfSbar);
//			subjSbar = SbarProcessing.getInstance().processSubjInSbarByVerb(tdls, taggedWords, verbSbarIdx);
//			subordinator = processAdv(tdls, verbOfSbar);
//			sbar = new SBAR(subjSbar, predicateSbar, subordinator);
//		}
//		return sbar;
//	}
	
}
