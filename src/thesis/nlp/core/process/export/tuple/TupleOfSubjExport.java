/**
 * 
 */
package thesis.nlp.core.process.export.tuple;

import java.util.HashSet;
import java.util.Set;

import thesis.nlp.models.NounPhrase;
import thesis.nlp.models.Subject;
import thesis.nlp.models.Tuple;

/**
 * @author lohuynh
 *
 */
public class TupleOfSubjExport {
	private static TupleOfSubjExport instance = null;
	public static TupleOfSubjExport  getInstance() {
		if (instance == null) {
			instance = new TupleOfSubjExport ();
		}
		return instance;
	}
	
	/**
	 * Tuples of NP in Subject and Tuples of SBAR
	 * @param subject
	 * @return
	 */
	public Set<Tuple> exportTuplesOfSubject(Subject subject)
	{
		if (subject == null)
			return new HashSet<Tuple>();
		Set<Tuple> tuples = new HashSet<Tuple>();
		for (NounPhrase np : subject.getNplist())
		{
			tuples.addAll(TuplesOfNPExport.getInstance().exportTuplesOfNounPhrase(np));
		}
		tuples.addAll(TuplesOfSbarExport.getInstance().exportTuplesOfSbar(subject.getSbarSubj()));
		return tuples;
	}

}
