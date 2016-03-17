/**
 * 
 */
package thesis.nlp.core.process.export.tuple;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import thesis.nlp.models.NounPhrase;
import thesis.nlp.models.PrepositionalPhrase;
import thesis.nlp.models.Tuple;

/**
 * @author lohuynh
 *
 */
public class TupleOfPrepPhraseExport {
	private static TupleOfPrepPhraseExport  instance = null;
	public static TupleOfPrepPhraseExport  getInstance() {
		if (instance == null) {
			instance = new TupleOfPrepPhraseExport ();
		}
		return instance;
	}
	public Set<Tuple> exportTuplesOfPrepPhrase(PrepositionalPhrase pp)
	{
		
		if (pp == null)
			return new HashSet<Tuple>();
		Set<Tuple> tuples = new HashSet<Tuple>();
		Iterator<NounPhrase> nps = pp.getNounPhraseList().iterator();
		while (nps.hasNext())
		{
			tuples.addAll(TuplesOfNPExport.getInstance().exportTuplesOfNounPhrase(nps.next()));
		}
		
		tuples.addAll(TuplesOfSbarExport.getInstance().exportTuplesOfSbar(pp.getSbarPrepPhrase()));
		return tuples;
	}
}
