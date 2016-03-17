/**
 * 
 */
package thesis.nlp.util;

import java.util.Arrays;
import java.util.List;

import thesis.nlp.core.process.StanfordTreeTypedDependConsts;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author lohuynh
 *
 */
public class RelationOfTupleProcessUtil {
	private final static String[] possProNoun = new String[] {"my", "his", "her", "their", "our", "your", "its"};
	private final static String possReln = "have";
	public static String relationOfPremodifierAndNoun(TypedDependency tdl)
	{
		String reln = tdl.reln().getShortName();
		switch (reln) {
		case StanfordTreeTypedDependConsts.AMOD:
			return "is property of"; 
//			break;
		case StanfordTreeTypedDependConsts.POSS:
			return "have";
		default:
			return "";
		}
	}
	
	public static  boolean isPossRelOfSingleNP(String relation)
	{
		if (relation.trim().toUpperCase().equals(possReln.trim().toUpperCase()))
		{
			return true;
		}
		else
			return false;
	}
	public static String proNoun(String word)
	{
		List<String> possProNounlist = Arrays.asList(possProNoun);
		if (possProNounlist.contains(word))
		{
			switch (word.toUpperCase()) {
			case "MY":
				return "I";
			case "HER":
				return "she";
			case "HIS":
				return "he";
			case "THEIR":
				return "they";
			case "OUR":
				return "we";
			case "YOUR":
				return "you";
			case "ITS":
				return "it";
			default:
				return word;
			}
		}
		else
			return word;
	}
	
	
}
