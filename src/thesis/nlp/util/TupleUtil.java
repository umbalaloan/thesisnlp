/**
 * 
 */
package thesis.nlp.util;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import thesis.nlp.models.NounPhrase;
import thesis.nlp.models.SingleNP;

/**
 * @author lohuynh
 *
 */
public class TupleUtil {

	/**
	 * 
	 * @param nps
	 * @return the total of singleNPs in list of NPs
	 */
	public static Set<SingleNP> listOfSingleNPInListOfNP(List<NounPhrase> nps)
	{
		if(nps == null || nps.size() == 0)
			return new LinkedHashSet<SingleNP>();
		Iterator<NounPhrase> itr = nps.iterator();
		Set<SingleNP> singleNPs = new LinkedHashSet<SingleNP>();
		while (itr.hasNext())
		{
			singleNPs.addAll(itr.next().getSingleNPList());
		}
		return singleNPs;
	}
	
	/**
	 * check Subject is "THAT" , "WHICH", "WHERE", "WHOSE
	 * @param subj
	 * @return
	 */
	public static boolean checkSubjIsTHAT_WH(String subj)
	{
		switch (subj.toLowerCase().trim()) {
		case "that":
			return true;
		case "which":
			return true;
		case "where":
			return true;
		case "whose":
			return true;
		case "who":
			return true;

		default:
			return false;
		}
	}
	
	
//	public static boolean checkSubjBeTHAT(String subj)
//	{
//		if (subj.trim().toLowerCase().equals("that"))
//			return true;
//		else
//			return false;
//	}
}
