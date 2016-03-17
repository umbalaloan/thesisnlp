/**
 * 
 */
package thesis.nlp.core.process.export.tuple;

import java.util.HashSet;
import java.util.Set;

import thesis.nlp.models.AdverbPhrase;
import thesis.nlp.models.Predicate;
import thesis.nlp.models.SBAR;
import thesis.nlp.models.Tuple;
import thesis.nlp.util.StringProcessUtil;

/**
 * @author lohuynh
 *
 */
public class TuplesOfAdverbPhraseExport {
	private static TuplesOfAdverbPhraseExport  instance = null;
	public static TuplesOfAdverbPhraseExport  getInstance() {
		if (instance == null) {
			instance = new TuplesOfAdverbPhraseExport ();
		}
		return instance;
	}
	/**
	 * Export tuples Of Sbar in AdverbPhrase
	 * @param advPhrase
	 * @return
	 */
	public Set<Tuple> exportTuplesOfAdverbPhrase(AdverbPhrase advPhrase)
	{
		if (advPhrase == null)
			return new HashSet<Tuple>();
		Set<Tuple> tuples = new HashSet<Tuple>();
		SBAR sbar = advPhrase.getSbarAdvPhrase();
		tuples.addAll(TuplesOfSbarExport.getInstance().exportTuplesOfSbar(sbar));
		return tuples;
	}
	public String advPhraseOfTuple (Predicate predicate)
	{
		if (predicate == null)
			return "";
		return StringProcessUtil.valueOf(predicate.getAdvPhrase());
	}
}
