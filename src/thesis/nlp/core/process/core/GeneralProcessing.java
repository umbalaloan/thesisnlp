/**
 * 
 */
package thesis.nlp.core.process.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thesis.nlp.core.process.StanfordTreeTypedDependConsts;
import thesis.nlp.util.StringProcessUtil;
import thesis.nlp.util.TypedDependencyCheckUtil;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author lohuynh This class contains common functions which are used for all classes
 */
public class GeneralProcessing {
	/**
	 * Find Index of a Word in a sentence.
	 * 
	 * @param rawWords
	 * @param word
	 * @return
	 */
	public static int findWordIdxInListRawWordOrTaggedWord(List<CoreLabel> rawWords, Word word) {
		int index = -1;
		for (CoreLabel rawWord : rawWords) {
			if ((rawWord.beginPosition() == word.beginPosition()) && (rawWord.endPosition() == word.endPosition())) {
				index = rawWord.index();
			}
		}
		return index;
	}

	public static int findWordIdxInListRawWordOrTaggedWord(List<CoreLabel> rawWords, String word) {
		int index = -1;
		for (CoreLabel rawWord : rawWords) {
			if (rawWord.word().equals(word)) {
				index = rawWord.index();
			}
		}
		return index;
	}

	public static TypedDependency searchROOTTdep(List<TypedDependency> tdls) {
		TypedDependency rootTdep = null;
		for (TypedDependency tdl : tdls) {
			if (tdl.reln().getShortName().equals(StanfordTreeTypedDependConsts.ROOT)) {
				rootTdep = tdl;
				break;
			}
		}
		return rootTdep;
	}

	/**
	 * parse Tree into Sentence
	 * 
	 * @param tree
	 * @return
	 */
	public static String converseTreeToSentence(Tree tree) {
		List<Word> words = tree.yieldWords();
		String str = Sentence.listToString(words);
		return str;
	}

	/**
	 * 
	 * @param strNP
	 * @return
	 */
	public static List<String> spitStringIntoSubStrByCommaAndOr(String strNP) {
		strNP = strNP.replaceAll("and | or ", ",");
		List<String> strs = Arrays.asList(strNP.split("\\s*,\\s*"));
		return strs;
	}

	/**
	 * 
	 * @param tdls
	 * @param govIdx
	 * @return
	 */
	public static List<TypedDependency> listRelation(List<TypedDependency> tdls, int govIdx) {
		List<TypedDependency> listTypeDep = new ArrayList<TypedDependency>();
		for (TypedDependency tdl : tdls) {
			if (tdl.gov().index() == govIdx) {
				listTypeDep.add(tdl);
			}
		}
		return listTypeDep;
	}

	public static List<TypedDependency> listTdepsByRelation(List<TypedDependency> tdls, String relShortName, String specific) {
		List<TypedDependency> tdeps = new ArrayList<TypedDependency>();
		for (TypedDependency tdl : tdls) {
			if (tdl.reln().getShortName().equals(relShortName)) {
				if (specific.equals("")) {
					tdeps.add(tdl);
				} else if (specific != null && tdl.reln().getSpecific().equals(specific)) {
					tdeps.add(tdl);
				}

			}
		}
		return tdeps;
	}

	public static TypedDependency searchTDepByGovAndDep(List<TypedDependency> tdls, String relShortName, String specific, int govIdx, String depword) {
		TypedDependency tdep = null;
		for (TypedDependency tdl : tdls) {
			if ((tdl.gov().index() == govIdx) && (tdl.reln().getShortName().equals(relShortName))) {
				if (specific.equals("") && tdl.dep().originalText().equals(depword)) {
					tdep = tdl;
					break;
				} else if (specific != null && tdl.reln().getSpecific().equals(specific) && tdl.dep().word().equals(depword)) {
					tdep = tdl;
					break;
				}

			}
		}
		return tdep;
	}

	/**
	 * This function search NMOD dependency which show relation between verb and object. In which verb is followed by particle. This is an error from
	 * Stanford API
	 * 
	 * @Exp : Sentence: I asked for your help. nmod (asked, for) => nmod(asked, help) , specific is "for"
	 * @param tdls
	 * @param relShortName
	 * @param specific
	 * @param govIdx
	 * @param depword
	 * @return
	 */
	public static TypedDependency searchNmodTdep(List<TypedDependency> tdls, String relShortName, String specific, int govIdx) {
		TypedDependency tdep = null;
		for (TypedDependency tdl : tdls) {
			if ((tdl.gov().index() == govIdx) && (tdl.reln().getShortName().equals(relShortName))) {
				if (specific.equals("")) {
					tdep = tdl;
					break;
				} else if (specific != null && tdl.reln().getSpecific().equals(specific)) {
					tdep = tdl;
					break;
				}

			}
		}
		return tdep;
	}

	/**
	 * This function is used for typedDependency which only return one value in sentence based on GOV such as ROOT, DET, AUX, REF
	 * 
	 * @SpecialCase NMOD dependency. For example, sentence: "I asked for your help ". Output of Stanford is "nmod (asked, for)" => nmod(asked, help) ,
	 *              specific is "for"
	 * @param tdls
	 * @param relShortName
	 * @param specific
	 * @param govIdx
	 *            . The index of Governor in TypedDependency
	 * @return
	 */
	public static TypedDependency returnOneRelationByGovIdx(List<TypedDependency> tdls, String relShortName, String specific, int govIdx) {
		TypedDependency tdep = null;
		for (TypedDependency tdl : tdls) {
			if ((tdl.gov().index() == govIdx) && (tdl.reln().getShortName().equals(relShortName))) {
				if (specific.equals("")) {
					tdep = tdl;
					break;
				} else if (tdl.reln().getSpecific()!= null && tdl.reln().getSpecific().equals(specific)) {
					tdep = tdl;
					break;
				}

			}
		}
		return tdep;
	}

	/**
	 * This function search Case typedDependence
	 * 
	 * @Exp: "Tom ask Bill for his help" => case(help-6, for-4)
	 * @param tdls
	 * @param relShortName
	 * @param specific
	 * @param gov
	 *            . String
	 * @return
	 */
	public static TypedDependency searchCaseTDepByGovStr(List<TypedDependency> tdls, String relShortName, String specific, String gov) {
		TypedDependency tdep = null;
		for (TypedDependency tdl : tdls) {
			if ((tdl.gov().originalText().equals(gov)) && (tdl.reln().getShortName().equals(relShortName))) {
				if (specific.equals("")) {
					tdep = tdl;
					break;
				} else if (specific != null && tdl.reln().getSpecific().equals(specific)) {
					tdep = tdl;
					break;
				}

			}
		}
		return tdep;
	}

	public static TypedDependency searchCaseTDepByGovIdx(List<TypedDependency> tdls, String relShortName, String specific, int govIdx) {
		TypedDependency tdep = null;
		for (TypedDependency tdl : tdls) {
			if ((tdl.gov().index() == govIdx) && (tdl.reln().getShortName().equals(relShortName))) {
				if (specific.equals("")) {
					tdep = tdl;
					break;
				} else if (specific != null && tdl.reln().getSpecific().equals(specific)) {
					tdep = tdl;
					break;
				}

			}
		}
		return tdep;
	}

	/**
	 * This function search Case typedDependence By Dep String
	 * 
	 * @Exp: "Tom ask Bill for his help" => case(help-6, for-4)
	 * @param tdls
	 * @param relShortName
	 * @param specific
	 * @param dep
	 * @return
	 */
	public static TypedDependency searchCaseTDepByDepStr(List<TypedDependency> tdls, String relShortName, String specific, String dep) {
		TypedDependency tdep = null;
		for (TypedDependency tdl : tdls) {
			if ((tdl.dep().originalText().equals(dep)) && (tdl.reln().getShortName().equals(relShortName))) {
				if (specific.equals("")) {
					tdep = tdl;
					break;
				} else if (specific != null && tdl.reln().getSpecific().equals(specific)) {
					tdep = tdl;
					break;
				}

			}
		}
		return tdep;
	}

	/**
	 * This function search Case typedDependence by Dep Index
	 * 
	 * @Exp: "Tom ask Bill for his help" => case(help-6, for-4)
	 * @param tdls
	 * @param relShortName
	 * @param specific
	 * @param depIdx
	 * @return
	 */
	public static TypedDependency searchCaseTDepByDepIdx(List<TypedDependency> tdls, String relShortName, String specific, int depIdx) {
		TypedDependency tdep = null;
		for (TypedDependency tdl : tdls) {
			if ((tdl.dep().index() == depIdx) && (tdl.reln().getShortName().equals(relShortName))) {
				if (specific.equals("")) {
					tdep = tdl;
					break;
				} else if (specific != null && tdl.reln().getSpecific().equals(specific)) {
					tdep = tdl;
					break;
				}

			}
		}
		return tdep;
	}

	/**
	 * The function return the list of TypeDepency which has the same govIdx. For exp: COMPOUND, AMOD, DOBJ, IOBJ
	 * 
	 * @param tdls
	 * @param relShortName
	 * @param specific
	 * @param govIdx
	 * @return
	 */
	public static List<TypedDependency> listTypeDepRelationByGovIdx(List<TypedDependency> tdls, String relShortName, String specific, int govIdx) {
		List<TypedDependency> tdeps = new ArrayList<TypedDependency>();
		for (TypedDependency tdl : tdls) {
			if ((tdl.gov().index() == govIdx) && (tdl.reln().getShortName().toUpperCase().equals(relShortName.toUpperCase()))) {
				if (specific.equals("") || (specific != null && tdl.reln().getSpecific().toUpperCase().equals(specific.toUpperCase()))) {
					tdeps.add(tdl);
				}
			}
		}
		return tdeps;
	}

	public static List<TypedDependency> listTypeDepRelationByDepIdx(List<TypedDependency> tdls, String relShortName, String specific, int depIdx) {
		List<TypedDependency> tdeps = new ArrayList<TypedDependency>();
		for (TypedDependency tdl : tdls) {
			if ((tdl.dep().index() == depIdx) && (tdl.reln().getShortName().toUpperCase().equals(relShortName.toUpperCase()))) {
				if (specific.equals("") || (specific != null && tdl.reln().getSpecific().toUpperCase().equals(specific.toUpperCase()))) {
					tdeps.add(tdl);
				}
			}
		}
		return tdeps;
	}

	/**
	 * This function is used for typedDependency which only return one value in sentence based on DEP such as ROOT, DET, AUX, REF
	 * 
	 * @param tdls
	 * @param relShortName
	 * @param specific
	 * @param depIdx
	 * @return
	 */
	public static TypedDependency returnOneRelationByDepIdx(List<TypedDependency> tdls, String relShortName, String specific, int depIdx) {
		TypedDependency tdep = null;
		for (TypedDependency tdl : tdls) {
			if ((tdl.dep().index() == depIdx) && (tdl.reln().getShortName().toLowerCase().equals(relShortName.toLowerCase()))) {
				if (specific.equals("")) {
					tdep = tdl;
					break;
				}
				if (specific != null && tdl.reln().getSpecific().toLowerCase().equals(specific.toLowerCase())) {
					tdep = tdl;
					break;
				}

			}
		}
		return tdep;
	}

	// public static TypedDependency returnRelationByDepAndGovIdx(List<TypedDependency> tdls, String relShortName, String specific, )

	public static List<TypedDependency> listTypeDepRelationByGovStr(List<TypedDependency> tdls, String relShortName, String specific, String gov) {
		List<TypedDependency> tdeps = new ArrayList<TypedDependency>();
		for (TypedDependency tdl : tdls) {
			// System.out.println("TDL: " + tdl.reln().getShortName() + " ; " + tdl.reln().getSpecific() + " ; gov : " + tdl.gov().word() +
			// " ; dep: "+ tdl.dep().word());
			if ((tdl.reln().getShortName().equals(relShortName)) && (tdl.gov().word().toLowerCase().equals(gov.toLowerCase()))) {
				if (specific.equals("") || (specific != null && tdl.reln().getSpecific().equals(specific))) {
					tdeps.add(tdl);
				}
			}
		}
		return tdeps;
	}

	public static List<TypedDependency> listTypeDepRelationByDepStr(List<TypedDependency> tdls, String relShortName, String specific, String dep) {
		List<TypedDependency> tdeps = new ArrayList<TypedDependency>();
		for (TypedDependency tdl : tdls) {
			System.out.println("TDL: " + tdl.reln().getShortName() + " ; " + tdl.reln().getSpecific() + " ; gov : " + tdl.gov().word() + " ; dep: "
					+ tdl.dep().word());
			if ((tdl.reln().getShortName().equals(relShortName)) && (tdl.gov().word().toLowerCase().equals(dep.toLowerCase()))) {
				if (specific.equals("") || (specific != null && tdl.reln().getSpecific().equals(specific))) {
					tdeps.add(tdl);
				}
			}
		}
		return tdeps;
	}

	public static int convertWordIdxFromTDepToListCoreLabel(int wordIdxInTDep) {
		if (wordIdxInTDep == 0)
		{
			return 0;
		}
		return wordIdxInTDep - 1;
	}

	public static int convertWordIdxFromListCoreLabelToTDep(int wordIdxInList) {
		return wordIdxInList + 1;
	}

	/**
	 * Because the list of rawWords doesnot store index of word and list of TaggedWords does not store positions of word. This function should be run
	 * first before processing sentence;
	 * 
	 * @param rawWords
	 * @param taggedWords
	 */
	public final static void processRawWordsAndTaggedWords(List<CoreLabel> rawWords, List<CoreLabel> taggedWords) {
		for (int i = 0; i < rawWords.size(); i++) {
			rawWords.get(i).setIndex(taggedWords.get(i).index());
			taggedWords.get(i).setBeginPosition(rawWords.get(i).beginPosition());
			taggedWords.get(i).setEndPosition(rawWords.get(i).endPosition());
		}
	}

	public static List<CoreLabel> listOfHeadNounByDepIdx(List<TypedDependency> tdeps) {
		List<CoreLabel> cls = new ArrayList<CoreLabel>();
		if (tdeps.size() == 0) {
			return cls;
		}
		for (TypedDependency tdep : tdeps) {
			CoreLabel cl = new CoreLabel();
			cl.setWord(tdep.dep().originalText());
			cl.setIndex(tdep.dep().index());
			cl.setTag(tdep.dep().tag());
			cls.add(cl);
		}
		return cls;

	}

	public static List<CoreLabel> listOfHeadNounByGovIdx(List<TypedDependency> tdeps) {
		List<CoreLabel> cls = new ArrayList<CoreLabel>();
		if (tdeps.size() == 0) {
			return cls;
		}
		for (TypedDependency tdep : tdeps) {
			CoreLabel cl = new CoreLabel();
			cl.setWord(tdep.gov().originalText());
			cl.setIndex(tdep.gov().index());
			cl.setTag(tdep.gov().tag());
			cls.add(cl);
		}
		return cls;

	}

	/**
	 * This function return the list of headNoun in NPs or the list of adjective which are connected by 'And' & 'OR' conjunction
	 * 
	 * @param specificTdep
	 *            . Exp: "nsubj", "prep", "dobj", "iobj"
	 * @param tdls
	 * @return
	 */
	public static List<CoreLabel> listHeadNounWordsOfTdepByDepIdx(TypedDependency specificTdep, List<TypedDependency> tdls) {
		List<CoreLabel> listLastNoun = new ArrayList<CoreLabel>();
		int govIdxOfConjAndOfNPs = 0;
		CoreLabel noun = null;
		if (specificTdep != null && (!specificTdep.dep().tag().equals(StanfordTreeTypedDependConsts.IN))) {
			govIdxOfConjAndOfNPs = specificTdep.dep().index();
			noun = new CoreLabel();
			noun.setWord(specificTdep.dep().word());
			noun.setIndex(specificTdep.dep().index());
			noun.setTag(specificTdep.dep().tag());
			listLastNoun.add(noun);
			for (TypedDependency tdl : tdls) {
				String rel = tdl.reln().getShortName();
				String specific = StringProcessUtil.valueOf(tdl.reln().getSpecific());
				int govIdx = tdl.gov().index();
				if (rel.equals(StanfordTreeTypedDependConsts.CONJ) && specific.equals(StanfordTreeTypedDependConsts.SPECIFIC_AND)
						&& govIdx == govIdxOfConjAndOfNPs) {
					noun = new CoreLabel();
					noun.setWord(tdl.dep().word());
					noun.setIndex(tdl.dep().index());
					noun.setTag(tdl.dep().tag());
					listLastNoun.add(noun);
				}
				if (rel.equals(StanfordTreeTypedDependConsts.CONJ) && specific.equals(StanfordTreeTypedDependConsts.SPECIFIC_OR)
						&& govIdx == govIdxOfConjAndOfNPs) {
					noun = new CoreLabel();
					noun.setWord(tdl.dep().word());
					noun.setIndex(tdl.dep().index());
					noun.setTag(tdl.dep().tag());
					listLastNoun.add(noun);
				}
			}
		}
		return listLastNoun;
	}

	/**
	 * This function to return the list of HeadWord which has a relationship with gov() of specificTdep
	 * <p>
	 * <br>
	 * Exp: "Java is concurrent, class-based, object-oriented, and specifically designed to have as few implementation dependencies as possible"</br>
	 * <br>
	 * auxpass(concurrent-3, is-2) ; passive auxiliary ; auxpass * null ; aux </br> <br>
	 * root(ROOT-0, concurrent-3) ; root ; root * null ; null </br> <br>
	 * conj_and(concurrent-3, class-based-5) ; conj_collapsed ; conj * and ; conj </br> <br>
	 * dep(concurrent-3, object-oriented-7) ; dependent ; dep * null ; null </br>
	 * </p>
	 * <p>
	 * It can be used to get NN of COmplement in Copular Verb
	 * </p>
	 * 
	 * @param specificTdep
	 * @param tdls
	 * @return
	 */

	public static List<CoreLabel> listHeadWordsOfTdepByGovIdx(TypedDependency specificTdep, List<TypedDependency> tdls) {
		List<CoreLabel> listLastNoun = new ArrayList<CoreLabel>();
		int govIdxOfConjAndOfNPs = 0;
		CoreLabel noun = null;
		if (specificTdep != null) {
			govIdxOfConjAndOfNPs = specificTdep.gov().index();
			noun = new CoreLabel();
			noun.setWord(specificTdep.gov().word());
			noun.setIndex(specificTdep.gov().index());
			noun.setTag(specificTdep.gov().tag());
			listLastNoun.add(noun);
			for (TypedDependency tdl : tdls) {
				String rel = tdl.reln().getShortName();
				String specific = tdl.reln().getSpecific();
				int govIdx = tdl.gov().index();
				if (rel.equals(StanfordTreeTypedDependConsts.CONJ) && specific.equals(StanfordTreeTypedDependConsts.SPECIFIC_AND)
						&& govIdx == govIdxOfConjAndOfNPs) {
					if (!TypedDependencyCheckUtil.checkATagIsAVerb(tdl.dep().tag())){
						noun = new CoreLabel();
						noun.setWord(tdl.dep().word());
						noun.setIndex(tdl.dep().index());
						noun.setTag(tdl.dep().tag());
						listLastNoun.add(noun);
					}
				}
				if (rel.equals(StanfordTreeTypedDependConsts.CONJ) && specific.equals(StanfordTreeTypedDependConsts.SPECIFIC_OR)
						&& govIdx == govIdxOfConjAndOfNPs) {
					if (!TypedDependencyCheckUtil.checkATagIsAVerb(tdl.dep().tag())){
						noun = new CoreLabel();
						noun.setWord(tdl.dep().word());
						noun.setIndex(tdl.dep().index());
						noun.setTag(tdl.dep().tag());
						listLastNoun.add(noun);
					}
				}
			}
		}
		return listLastNoun;
	}
}
