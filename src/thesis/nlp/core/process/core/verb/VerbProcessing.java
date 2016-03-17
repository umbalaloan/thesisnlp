/**
 * 
 */
package thesis.nlp.core.process.core.verb;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import thesis.nlp.core.process.SentenceConstants;
import thesis.nlp.core.process.StanfordTreeTypedDependConsts;
import thesis.nlp.util.TypedDependencyCheckUtil;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author lohuynh
 *
 */
public class VerbProcessing {

	/**
	 * 
	 */
//	private static String stemmedVerb = "";
	private static BufferedReader buff;

	public static String stemmingWord(String verb) {
		// TODO Auto-generated method stub
		String stemmedVerb = "";
		try {
			buff = new BufferedReader(new FileReader(SentenceConstants.FILE_STEMMEDWORD));
			String currentLine;
			String[] strparts;
			while ((currentLine = buff.readLine()) != null) {
				strparts = currentLine.split(",");

				if (strparts[0].toLowerCase().equals(verb.toLowerCase())) {
					stemmedVerb = strparts[1];
					break;
				} else if (strparts[1].toLowerCase().equals(verb.toLowerCase())) {
					stemmedVerb = verb;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stemmedVerb;
	}

	public static String stemmedVerb(String verb, FileReader stemmedWordList) {
		BufferedReader buff = new BufferedReader(stemmedWordList);
		String stemmedVerb = "";
		String[] str;
		try {
			while (buff.readLine() != null) {
				str = buff.readLine().split(",");
				if (str[0].toString().equals(verb)) {
					stemmedVerb = str[1].toString().trim();
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return stemmedVerb;
	}

	/**
	 * return verbVoice
	 * 
	 * @param verb
	 * @param tdl
	 * @return
	 */
	public static String voiceOfVerb(String verb, List<TypedDependency> tdls) {
		String voice = SentenceConstants.VERBVOICE_ACTIVE;
		for (TypedDependency tdl : tdls) {
			if (tdl.reln().getShortName().equals(StanfordTreeTypedDependConsts.AUXPASS) && tdl.gov().originalText().equals(verb)) {
				voice = SentenceConstants.VERBVOICE_PASSIVE;
				break;
			}
		}
		return voice;
	}

	public static String voiceOfVerb(List<TypedDependency> tdl, int govIdx) {
		String voice = SentenceConstants.VERBVOICE_ACTIVE;
		// check whether or not Grammar Structure contain Auxpass for verb
		for (TypedDependency td : tdl) {
			if (td.reln().getShortName().equals(StanfordTreeTypedDependConsts.AUXPASS) && td.gov().index() == govIdx) {
				voice = SentenceConstants.VERBVOICE_PASSIVE;
				break;
			}
		}
		return voice;
	}

	public static String getVerbInPPTenseOrPassive(TypedDependency tdl) {
		String verb = "";
		if (tdl.reln().getShortName().equals(StanfordTreeTypedDependConsts.AUX)
				|| tdl.reln().getShortName().equals(StanfordTreeTypedDependConsts.AUXPASS)) {
			verb = tdl.gov().originalText();
		}
		return verb;
	}

	/**
	 * return verbTense
	 * 
	 * @param tagVerb
	 * @return
	 */
	public static String tenseVerb(String tagVerb) {
		String tense = "";
		switch (tagVerb) {
		case StanfordTreeTypedDependConsts.VBD:
			tense = SentenceConstants.VERB_PAST_TENSE;
			break;
		case StanfordTreeTypedDependConsts.VBN:
			tense = SentenceConstants.VERB_PP_TENSE;
			break;
		case StanfordTreeTypedDependConsts.VBP:
			tense = SentenceConstants.VERB_PRESENT_TENSE;
			break;
		case StanfordTreeTypedDependConsts.VBZ:
			tense = SentenceConstants.VERB_PRESENT_TENSE;
			break;
		default:
			tense = SentenceConstants.VERB_PRESENT_TENSE;
			break;
		}
		return tense;
	}

	/**
	 * return form of Verb : Single or PLURAL
	 * 
	 * @param tagVerb
	 * @return
	 */
	public static int verbForm(String tagVerb) {
		int form = -1;
		if (tagVerb.equals(StanfordTreeTypedDependConsts.VBZ)) {
			form = SentenceConstants.SINGULAR_FORM;
		}
		if (tagVerb.equals(StanfordTreeTypedDependConsts.VBP)) {
			form = SentenceConstants.PLURAL_FORM;
		}
		return form;
	}

	/**
	 * return VerbType : Intransitive / Transitive /Copular Verb
	 * 
	 * @param verb
	 * @return
	 */
	public static int verbType(String verb) {
		int verbType = -1;
		// not implement yet
		return verbType;
	}
	
	public static TypedDependency copularVerb(List<TypedDependency> tdls)
	{
		TypedDependency tdep = null;
		for (TypedDependency tdl : tdls)
		{
			if (TypedDependencyCheckUtil.checkRel(tdl, StanfordTreeTypedDependConsts.COP))
			{
				tdep = tdl;
				break;
			}
		}
		return tdep;
	}

}
