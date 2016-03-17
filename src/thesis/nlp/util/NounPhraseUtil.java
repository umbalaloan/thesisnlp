/**
 * 
 */
package thesis.nlp.util;

import java.util.ArrayList;
import java.util.List;

import thesis.nlp.core.process.StanfordTreeTypedDependConsts;
import thesis.nlp.core.process.core.GeneralProcessing;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author lohuynh
 *
 */
public class NounPhraseUtil {
	/**
	 * The function return the list of TypeDepency which has the same govIdx. For exp: COMPOUND, AMOD, DOBJ, IOBJ
	 * 
	 * @param tdls
	 * @param relShortName
	 * @param specific
	 * @param govIdx
	 * @return
	 */
	public static List<TypedDependency> listPrepTdepsSupportForNPByGovIdx(List<TypedDependency> tdls, String relShortName, String specific, int govIdx) {
		List<TypedDependency> preptdeps = new ArrayList<TypedDependency>();
		for (TypedDependency tdl : tdls) {
			if ((tdl.gov().index() == govIdx) && (tdl.reln().getShortName().toUpperCase().equals(relShortName.toUpperCase())) && (tdl.dep().index() > govIdx)) {
				if (specific.equals("") || (specific != null && tdl.reln().getSpecific().toUpperCase().equals(specific.toUpperCase()))) {
					preptdeps.add(tdl);
				}
			}
		}
		return preptdeps;
	}
}
