/**
 * 
 */
package thesis.nlp.core.process.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import thesis.nlp.core.process.StanfordTreeTypedDependConsts;
import thesis.nlp.core.process.core.verb.CopularVerbProcess;
import thesis.nlp.core.process.core.verb.DiTransitiveVerbPhraseProcessing;
import thesis.nlp.core.process.core.verb.GerundVerbPhraseProcessing;
import thesis.nlp.core.process.core.verb.IntransitiveVerbPhraseProcessing;
import thesis.nlp.core.process.core.verb.OtherVerbPhraseProcessing;
import thesis.nlp.core.process.core.verb.ToInfinitiveVerbProcessing;
import thesis.nlp.core.process.core.verb.TransitiveVerbPhraseProcessing;
import thesis.nlp.core.process.core.verb.VerbPhraseProcessing;
import thesis.nlp.models.AdverbPhrase;
import thesis.nlp.models.NounPhrase;
import thesis.nlp.models.Predicate;
import thesis.nlp.models.PrepositionalPhrase;
import thesis.nlp.models.SBAR;
import thesis.nlp.models.Subject;
import thesis.nlp.models.Verb;
import thesis.nlp.models.VerbPhrase;
import thesis.nlp.util.TypedDependencyCheckUtil;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author lohuynh
 *
 */
public class PredicateProcessing {

	/**
 * 
 */
	public PredicateProcessing() {
	}

//	public static PredicateProcessing getInstance() {
//		return instance;
//
//	}
	

	public Predicate processPredicate(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Subject subj) {
		List<VerbPhrase> vps = listVerbPhrases(tdls, taggedWords, subj);
//		List<PrepositionalPhrase> pps = listPrepPhrasesInPredicate(tdls, taggedWords, vps);
//		List<PrepositionalPhrase> pps = new ArrayList<PrepositionalPhrase>();
//		String adv = getAdverbInPredicateFromTree(tdls, verb)
		AdverbPhrase advphrase = getAdvPhraseOfPredicate(tdls, taggedWords, vps);
		return new Predicate(vps, advphrase);
	}

	public List<VerbPhrase> listVerbPhrases(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Subject subj) {
		List<VerbPhrase> listVPs = new ArrayList<VerbPhrase>();
		Set<Verb> verbs = VerbPhraseProcessing.getInstance().findListMainVerbStr(taggedWords, tdls, subj);
		for (Verb verb : verbs) {
			VerbPhrase vp = processVPforAVerb(tdls, taggedWords, verb);
			if (vp != null) {
				listVPs.add(vp);
			}
		}
		return listVPs;
	}

	/**
	 * 
	 * @param tdls
	 * @param taggedWords
	 * @param verbs
	 *            . Set of Verb
	 * @return
	 */
	public static List<VerbPhrase> listVerbPhrases(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Set<Verb> verbs) {
		List<VerbPhrase> listVPs = new ArrayList<VerbPhrase>();
		for (Verb verb : verbs) {
			VerbPhrase vp = processVPforAVerb(tdls, taggedWords, verb);
			if (vp != null) {
				listVPs.add(vp);
			}
		}
		return listVPs;
	}

	public static VerbPhrase processVPforAVerb(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb) {
		VerbPhrase vp = null;
		try {
			BufferedReader buff = new BufferedReader(new FileReader("data\\FinalVerbList.txt"));
			String currentLine;
			eachVerb: while ((currentLine = buff.readLine()) != null) {
				String[] strs = currentLine.split(",");
				String firstWord = strs[2].toString();
				int patternType = Integer.parseInt(strs[3].toString());
				String particle = strs[4].toString();
				VerbPhrase tempVPResult = null;

				switch (patternType) {
				case 14:
					if (verb.getStemmedVerb().equals(firstWord)) {
						tempVPResult = CopularVerbProcess.processVerbType14(tdls, taggedWords, verb, patternType);
						if (tempVPResult == null)
							continue;
						else {

							vp = tempVPResult;
							break eachVerb;

						}
					}
				case 13:
					if (verb.getStemmedVerb().equals(firstWord)) {
						// OtherVerbPhraseProcessing ovpp = new OtherVerbPhraseProcessing();
						tempVPResult = OtherVerbPhraseProcessing.processVerbType13(tdls, taggedWords, verb, patternType);
						if (tempVPResult == null)
							continue;
						else {

							vp = tempVPResult;
							break eachVerb;

						}
					}
				case 12:
					// very rarely
					if (verb.getStemmedVerb().equals(firstWord)) {
						// GerundVerbPhraseProcessing gvpp = new GerundVerbPhraseProcessing();
						tempVPResult = GerundVerbPhraseProcessing.processVerbType12(tdls, taggedWords, verb, patternType, particle);
						if (tempVPResult == null)
							continue;
						else {

							vp = tempVPResult;
							break eachVerb;

						}
					}
				case 11:
					if (verb.getStemmedVerb().equals(firstWord)) {
						// GerundVerbPhraseProcessing gvpp = new GerundVerbPhraseProcessing();
						tempVPResult = GerundVerbPhraseProcessing.processVerbType11(tdls, taggedWords, verb, patternType, particle);
						if (tempVPResult == null)
							continue;
						else {

							vp = tempVPResult;
							break eachVerb;

						}
					}
				case 10:
					if (verb.getStemmedVerb().equals(firstWord)) {
						// GerundVerbPhraseProcessing gvpp = new GerundVerbPhraseProcessing();
						tempVPResult = GerundVerbPhraseProcessing.processVerbType10(tdls, taggedWords, verb, patternType);
						if (tempVPResult == null)
							continue;
						else {

							vp = tempVPResult;
							break eachVerb;

						}
					}
				case 9:
					if (verb.getStemmedVerb().equals(firstWord)) {
						// ToInfinitiveVerbProcessing toinfp = new ToInfinitiveVerbProcessing();
						tempVPResult = ToInfinitiveVerbProcessing.processVerbType9(tdls, taggedWords, verb, patternType);
						if (tempVPResult == null)
							continue;
						else {
							vp = tempVPResult;
							break eachVerb;

						}
					}
				case 8:
					if (verb.getStemmedVerb().equals(firstWord)) {
						// ToInfinitiveVerbProcessing toinfp = new ToInfinitiveVerbProcessing();
						tempVPResult = ToInfinitiveVerbProcessing.processVerbType8(tdls, taggedWords, verb, patternType);
						if (tempVPResult == null)
							continue;
						else {
							vp = tempVPResult;
							break eachVerb;

						}
					}
				case 7:
					if (verb.getStemmedVerb().equals(firstWord)) {
						// DiTransitiveVerbPhraseProcessing divp = new DiTransitiveVerbPhraseProcessing();
						tempVPResult = DiTransitiveVerbPhraseProcessing.processVerbType7(tdls, taggedWords, verb, patternType);
						if (tempVPResult == null)
							continue;
						else {

							vp = tempVPResult;
							break eachVerb;

						}
					}
				case 6:
					if (verb.getStemmedVerb().equals(firstWord)) {
						// TransitiveVerbPhraseProcessing tvp = new TransitiveVerbPhraseProcessing();
						tempVPResult = TransitiveVerbPhraseProcessing.processVerbType6(tdls, taggedWords, verb, patternType, particle);
						if (tempVPResult == null)
							continue;
						else {

							vp = tempVPResult;
							break eachVerb;

						}
					}
				case 5:
					if (verb.getStemmedVerb().equals(firstWord)) {
						// TransitiveVerbPhraseProcessing tvp = new TransitiveVerbPhraseProcessing();
						tempVPResult = TransitiveVerbPhraseProcessing.processVerbType5(tdls, taggedWords, verb, patternType, particle);
						if (tempVPResult == null)
							continue;
						else {

							vp = tempVPResult;
							break eachVerb;

						}
					}
				case 4:
					if (verb.getStemmedVerb().equals(firstWord)) {
						// TransitiveVerbPhraseProcessing tvp = new TransitiveVerbPhraseProcessing();
						tempVPResult = TransitiveVerbPhraseProcessing.processVerbType4(tdls, taggedWords, verb, patternType, particle);
						if (tempVPResult == null)
							continue;
						else {

							vp = tempVPResult;
							break eachVerb;

						}
					}
				case 3:
					if (verb.getStemmedVerb().equals(firstWord)) {
						// TransitiveVerbPhraseProcessing tvp = new TransitiveVerbPhraseProcessing();
						tempVPResult = TransitiveVerbPhraseProcessing.processVerbType3(tdls, taggedWords, verb, patternType);
						if (tempVPResult == null)
							continue;
						else {

							vp = tempVPResult;
							break eachVerb;

						}
					}
				case 2:
					if (verb.getStemmedVerb().equals(firstWord)) {

						// IntransitiveVerbPhraseProcessing ivp = new IntransitiveVerbPhraseProcessing();
						tempVPResult = IntransitiveVerbPhraseProcessing.processVerbType2(tdls, taggedWords, verb, patternType, particle);
					}
					if (tempVPResult == null)
						continue;
					else {

						vp = tempVPResult;
						break eachVerb;
					}
				case 1:

					if (verb.getStemmedVerb().equals(firstWord)) {
						// IntransitiveVerbPhraseProcessing ivp = new IntransitiveVerbPhraseProcessing();
						tempVPResult = IntransitiveVerbPhraseProcessing.processVerbType1(tdls, taggedWords, verb, patternType);
					}
					if (tempVPResult == null)
						continue;
					else {

						vp = tempVPResult;
						break eachVerb;
					}
				default:
					vp = null;
					break;

				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vp;
	}

//	/**
//	 * return preposition phrase in Predicate
//	 * 
//	 * @param predicateTree
//	 * @param rawWords
//	 * @return
//	 */
//	public List<PrepositionalPhrase> listPrepPhrasesInPredicate(List<TypedDependency> tdls, List<CoreLabel> taggedWords, List<VerbPhrase> verbphrases) {
//		List<PrepositionalPhrase> pps = new ArrayList<PrepositionalPhrase>();
//		if (verbphrases.size() < 0) {
//			return pps;
//		}
//		for (VerbPhrase vp : verbphrases) {
//			pps.addAll(PrepositionalPhraseProcessing.getInstance().processPPOfVerb(tdls, taggedWords, vp));
//		}
//		// for other special
//		// "this year the Defense Department sought $950 million in assistance from Congress for Ankara's huge military machine, which includes the biggest standing army in NATO outside the United States"
//		// prep has relation with ROOT
//		return pps;
//	}

	/**
	 * return Adverb string in predicate
	 * 
	 * @param predicateTree
	 * @return
	 */
	public static String getAdverbInPredicateFromTree(List<TypedDependency> tdls, Verb verb) {

		String adv = "";
		// NOT IMPLEMENT
		return adv;
	}

	public static AdverbPhrase getAdvPhraseOfPredicate(List<TypedDependency> tdls, List<CoreLabel> taggedWords, List<VerbPhrase> verbphrases) {
		if (verbphrases.size() < 0) {
			return null;
		}
		AdverbPhrase advPhrase = null;
		for (VerbPhrase vp : verbphrases) {
			if (vp.getComplement() == null || vp.getComplement().getCompAdvPhrase() == null)
				advPhrase = AdverbPhraseProcessing.getInstance().processAdvPhrase(tdls, taggedWords, vp.getVerb());
			if (advPhrase != null)
				break;
		}
		if (advPhrase == null)
		{
			advPhrase = adverbPhraseOfPredicateAtBeginOfSentence(tdls, taggedWords, verbphrases);
		}
		return advPhrase;
	}
	
	private static AdverbPhrase adverbPhraseOfPredicateAtBeginOfSentence(List<TypedDependency> tdls, List<CoreLabel> taggedWords, List<VerbPhrase> vps)
	{
		if (vps.size() == 0)
			return null;
		AdverbPhrase advPhrase = null;
		SBAR sbarAdvPhrase = null;
		NounPhrase nounPhrase = null;
		String adv = "";
		List<PrepositionalPhrase> pps = new ArrayList<PrepositionalPhrase>();
		for (TypedDependency tdl : tdls)
		{
			// Compare govIdx with the first Verb in VPs
			if (TypedDependencyCheckUtil.checkRel(tdl, StanfordTreeTypedDependConsts.ADVMOD) && tdl.gov().index() < vps.get(0).getVerb().getVerbIdx())
			{
				
				/*
				 * Exp: More widely, in late August, 2007 the group was accused in 'The Telegraph'
				 */
				adv = tdl.dep().word() + " " + tdl.gov().word();
				pps = AdverbPhraseProcessing.getInstance().processPPinAdvPhraseByTDep(tdls, taggedWords, tdl.gov().index());
				advPhrase = new AdverbPhrase(sbarAdvPhrase, pps, nounPhrase, adv);
				break;
			}
		}
		return advPhrase;
	}
	
}
