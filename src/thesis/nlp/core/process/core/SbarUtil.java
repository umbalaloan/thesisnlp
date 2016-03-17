/**
 * 
 */
package thesis.nlp.core.process.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import thesis.nlp.core.process.SentenceConstants;
import thesis.nlp.core.process.StanfordTreeTypedDependConsts;
import thesis.nlp.core.process.core.verb.VerbPhraseProcessing;
import thesis.nlp.core.process.core.verb.VerbProcessing;
import thesis.nlp.models.NounPhrase;
import thesis.nlp.models.Predicate;
import thesis.nlp.models.PrepositionalPhrase;
import thesis.nlp.models.SBAR;
import thesis.nlp.models.Subject;
import thesis.nlp.models.Verb;
import thesis.nlp.models.VerbPhrase;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author lohuynh
 *
 */
public class SbarUtil {

	/**
	 * This function process 
	 * @param tdls
	 * @param taggedWords
	 * @param verbIdxOfSbar . <p>The index of verb. If the verbOfSbar is a copular Verb, it will the index of DEP of CCOMP Tdep 
	 * 	<br> For exp: "He is smart and fat but he is lazy"</br>
	 * </p>
	 * @return
	 */
	public static Subject processSubjInSbarByVerb(List<TypedDependency> tdls, List<CoreLabel> taggedWords, int verbIdxOfSbar) {
		List<CoreLabel> listHeadNounsofSubjInSbar = new ArrayList<CoreLabel>();
		TypedDependency nsubjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NSUBJ, "", verbIdxOfSbar);
		int subjVoice = SentenceConstants.SUBJVOICE_ACTIVE;
		if (nsubjTdep != null) {
			listHeadNounsofSubjInSbar = GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(nsubjTdep, tdls);
		} else {
			TypedDependency nsubjpassTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NSUBJPASS, "",
					verbIdxOfSbar);
			if (nsubjpassTdep != null) {
				listHeadNounsofSubjInSbar = GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(nsubjpassTdep, tdls);
				subjVoice = SentenceConstants.SUBJVOICE_PASSIVE;
			}
		}
		List<NounPhrase> nplist = new ArrayList<NounPhrase>();
		if (listHeadNounsofSubjInSbar.size() > 0) {
			for (CoreLabel cl : listHeadNounsofSubjInSbar) {
				// Each NP in Subject
				List<CoreLabel> lastNounOfNP = new ArrayList<CoreLabel>();
				lastNounOfNP.add(cl);
				NounPhrase np = NounPhraseProcessing.getInstance().processNounPhraseByTDep(tdls, taggedWords, listHeadNounsofSubjInSbar);
				nplist.add(np);
			}
		}
		SBAR subSbarOfSubj = null; // NOT IMPLEMENT
		if (nplist.size() > 0 || subSbarOfSubj != null)
			return new Subject(nplist, subSbarOfSubj, subjVoice);
		else
			return null;
	}

	/**
	 * 
	 * @param tdls
	 * @param taggedWords
	 * @param verbOfSbar
	 * @return
	 */
	public static Predicate processPredicateInSbarByVerb(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verbOfSbar) {
		Set<Verb> verbsInSbar = VerbPhraseProcessing.getInstance().findListVerbsRelatedToMainVerb(taggedWords, tdls, verbOfSbar);
		List<VerbPhrase> vps = PredicateProcessing.listVerbPhrases(tdls, taggedWords, verbsInSbar);
//		List<PrepositionalPhrase> pps = PredicateProcessing.getInstance().listPrepPhrasesInPredicate(tdls, taggedWords, vps);
		if (vps.size() >0)
			return new Predicate(vps,  null); // AdverbClause is default NULL (not implement)
		else
			return null;
	}
	
//	/**
//	 * Get Subject of Appositional Modifier
//	 * <p>Exp: My dog, Join said, is beautiful</p>
//	 * @param tdls
//	 * @param taggedWords
//	 * @param apposTdep
//	 * @return
//	 */
//	public static Subject processSubjInSbarOfAppos(List<TypedDependency> tdls, List<CoreLabel> taggedWords, TypedDependency apposTdep) {
//		List<CoreLabel> listHeadNounsofSubjInSbar = new ArrayList<CoreLabel>();
//		int subjVoice = SentenceConstants.SUBJVOICE_ACTIVE; // default active
//		CoreLabel cl = new CoreLabel();
//		cl.setIndex(apposTdep.dep().index());
//		cl.setOriginalText(apposTdep.dep().originalText());
//		cl.setTag(apposTdep.dep().tag());
//		cl.setWord(apposTdep.dep().word());
//		listHeadNounsofSubjInSbar.add(cl);
//		List<NounPhrase> nplist = new ArrayList<NounPhrase>();
//		NounPhrase np = NounPhraseProcessing.getInstance().processNounPhraseByTDep(tdls, taggedWords, listHeadNounsofSubjInSbar);
//		nplist.add(np);
//		SBAR subSbarOfSubj = null; // NOT IMPLEMENT
//		if (nplist.size() > 0 || subSbarOfSubj != null)
//			return new Subject(nplist, subSbarOfSubj, subjVoice);
//		else
//			return null;
////		return new Subject(nplist, subSbarOfSubj, subjVoice);
//	}
	
	/**
	 * Get Subject of Appositional Modifier
	 * <p>Exp: My dog, Join said, is beautiful</p>
	 * @param tdls
	 * @param taggedWords
	 * @param apposTdep
	 * @return
	 */
	public static Subject processSubjInSbarOfAppos(List<TypedDependency> tdls, List<CoreLabel> taggedWords, List<TypedDependency> apposTdeps) {
		List<CoreLabel> listHeadNounsofSubjInSbar = new ArrayList<CoreLabel>();
		int subjVoice = SentenceConstants.SUBJVOICE_ACTIVE; // default active
		for (TypedDependency apposTdep : apposTdeps){
			CoreLabel cl = new CoreLabel();
			cl.setIndex(apposTdep.dep().index());
			cl.setOriginalText(apposTdep.dep().originalText());
			cl.setTag(apposTdep.dep().tag());
			cl.setWord(apposTdep.dep().word());
			listHeadNounsofSubjInSbar.add(cl);
		}
		List<NounPhrase> nplist = new ArrayList<NounPhrase>();
		NounPhrase np = NounPhraseProcessing.getInstance().processNounPhraseByTDep(tdls, taggedWords, listHeadNounsofSubjInSbar);
		nplist.add(np);
		SBAR subSbarOfSubj = null; // NOT IMPLEMENT
		if (nplist.size() > 0 || subSbarOfSubj != null)
			return new Subject(nplist, subSbarOfSubj, subjVoice);
		else
			return null;
//		return new Subject(nplist, subSbarOfSubj, subjVoice);
	}
	
	private static List<CoreLabel> listHeadNounOfApposCase(List<TypedDependency> tdls, List<CoreLabel> taggedWords, TypedDependency apposTdep)
	{
		if (apposTdep == null)
			return new ArrayList<CoreLabel>();
		List<CoreLabel> listApposCoreLabel = new ArrayList<CoreLabel>();
		int govIdx = apposTdep.gov().index();
		List<TypedDependency> apposTdeps = GeneralProcessing.listTypeDepRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.APPOS, "", govIdx);
		Iterator<TypedDependency> itrTdeps = apposTdeps.iterator();
		while (itrTdeps.hasNext())
		{
			CoreLabel cl = new CoreLabel();
			cl.setIndex(apposTdep.dep().index());
			cl.setOriginalText(apposTdep.dep().originalText());
			cl.setTag(apposTdep.dep().tag());
			cl.setWord(apposTdep.dep().word());
			listApposCoreLabel.add(cl);
		}
		return listApposCoreLabel;
	}

//	/**
//	 * Get Predicate of Appositional Modifier
//	 * <p>Exp: My dog, Join said, is beautiful</p>
//	 * @param tdls
//	 * @param taggedWords
//	 * @param apposTdep
//	 * @return
//	 */
//	public static Predicate processPredicateInSbarOfAppos(List<TypedDependency> tdls, List<CoreLabel> taggedWords, TypedDependency apposTdep) {
//		TypedDependency vmodTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.VMOD, "", apposTdep.dep().index());
//		if (vmodTdep == null)
//		{
//			return null;
//		}
//		Verb verb = new Verb();
//		verb.setVerbName(vmodTdep.dep().originalText());
//		verb.setVerbIdx(vmodTdep.dep().index());
//		verb.setStemmedVerb(VerbProcessing.stemmingWord(vmodTdep.dep().originalText()));
//		Set<Verb> verbsInSbar = new HashSet<Verb>();
//		verbsInSbar.add(verb);
//		List<VerbPhrase> vps = PredicateProcessing.listVerbPhrases(tdls, taggedWords, verbsInSbar);
////		List<PrepositionalPhrase> pps = PredicateProcessing.getInstance().listPrepPhrasesInPredicate(tdls, taggedWords, vps);
//		if (vps.size() >0)
//			return new Predicate(vps, null); // AdverbClause is default NULL (not implement)
//		else
//			return null;
//
//	}
	
	public static Predicate processPredicateInSbarOfAppos(List<TypedDependency> tdls, List<CoreLabel> taggedWords, List<TypedDependency> apposTdeps) {
		Set<Verb> verbsInSbar = new HashSet<Verb>();
		for (TypedDependency apposTdep : apposTdeps)
		{
			TypedDependency vmodTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.VMOD, "", apposTdep.dep().index());
			if (vmodTdep == null)
			{
				continue;
			}
			Verb verb = new Verb();
			verb.setVerbName(vmodTdep.dep().originalText());
			verb.setVerbIdx(vmodTdep.dep().index());
			verb.setStemmedVerb(VerbProcessing.stemmingWord(vmodTdep.dep().originalText()));
	//		Set<Verb> verbsInSbar = new HashSet<Verb>();
			verbsInSbar.add(verb);
		}
		List<VerbPhrase> vps = PredicateProcessing.listVerbPhrases(tdls, taggedWords, verbsInSbar);
	//	List<PrepositionalPhrase> pps = PredicateProcessing.getInstance().listPrepPhrasesInPredicate(tdls, taggedWords, vps);
		if (vps.size() >0)
			return new Predicate(vps, null); // AdverbClause is default NULL (not implement)
		else
			return null;
	}
	
	/**
	 * this function return subject of sbar if its typedDependency is "NN" , not NSUBJ or NSUBJPASS
	 * @param tdls
	 * @param taggedWords
	 * @param verbIdxOfSbar
	 * @return
	 * <p> Exp: "The region is still far from rebuilt, despite Government promises to have the work completed within two years, and there have been accusations of corruption and misuse of relief funds. 
	 * 	<br>nn(promises-11, Government-10)</br>
		<br>prep_despite(far-5, promises-11)</br>
	 * </p
	 */
	public static Subject processSubjAsNNInSbarByVerb(List<TypedDependency> tdls, List<CoreLabel> taggedWords, int verbIdxOfSbar) {
		List<CoreLabel> listHeadNounsofSubjInSbar = new ArrayList<CoreLabel>();
		TypedDependency nnTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NN, "", verbIdxOfSbar);
		int subjVoice = SentenceConstants.SUBJVOICE_ACTIVE;
		if (nnTdep == null)
			return null;
		listHeadNounsofSubjInSbar = GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(nnTdep, tdls);
		
		List<NounPhrase> nplist = new ArrayList<NounPhrase>();
		if (listHeadNounsofSubjInSbar.size() > 0) {
			for (CoreLabel cl : listHeadNounsofSubjInSbar) {
				// Each NP in Subject
				List<CoreLabel> lastNounOfNP = new ArrayList<CoreLabel>();
				lastNounOfNP.add(cl);
				NounPhrase np = NounPhraseProcessing.getInstance().processNounPhraseByTDep(tdls, taggedWords, listHeadNounsofSubjInSbar);
				nplist.add(np);
			}
		}
		SBAR subSbarOfSubj = null; // NOT IMPLEMENT
		if (nplist.size() > 0 || subSbarOfSubj != null)
			return new Subject(nplist, subSbarOfSubj, subjVoice);
		else
			return null;
	}


}
