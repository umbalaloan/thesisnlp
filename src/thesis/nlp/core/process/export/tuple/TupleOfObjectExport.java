/**
 * 
 */
package thesis.nlp.core.process.export.tuple;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import thesis.nlp.models.AdjectivePhrase;
import thesis.nlp.models.Complement;
import thesis.nlp.models.DirectObject;
import thesis.nlp.models.IndirectObject;
import thesis.nlp.models.NounPhrase;
import thesis.nlp.models.Tuple;

/**
 * @author lohuynh
 *
 */
public class TupleOfObjectExport {
	private static TupleOfObjectExport instance = null;
	public static TupleOfObjectExport getInstance() {
		if (instance == null) {
			instance = new TupleOfObjectExport();
		}
		return instance;
	}
	public Set<Tuple> exportTuplesOfCOMP(Complement comp)
	{
		if (comp == null)
			return new HashSet<Tuple>();
		Set<Tuple> tuples = new HashSet<Tuple>();
		for (NounPhrase np : comp.getCompNN())
		{
			tuples.addAll(TuplesOfNPExport.getInstance().exportTuplesOfNounPhrase(np));
			tuples.addAll(TuplesOfSbarExport.getInstance().exportTuplesOfSbar(np.getSbarNP()));
		}
		tuples.addAll(TuplesOfAdverbPhraseExport.getInstance().exportTuplesOfAdverbPhrase(comp.getCompAdvPhrase()));
		if (comp.getCompAdjPhrase().size() >0)
		{
			for (AdjectivePhrase adjPhrase :comp.getCompAdjPhrase())
			{
				tuples.addAll(TupleOfPrepPhraseExport.getInstance().exportTuplesOfPrepPhrase(adjPhrase.getPrepPhrase()));
				tuples.addAll(TuplesOfAdverbPhraseExport.getInstance().exportTuplesOfAdverbPhrase(adjPhrase.getAdvPhrase()));
			}
		}
		tuples.addAll(TupleOfPrepPhraseExport.getInstance().exportTuplesOfPrepPhrase(comp.getPrepPhrase()));
		tuples.addAll(TuplesOfSbarExport.getInstance().exportTuplesOfSbar(comp.getCompSbar()));
		return tuples;
	}
	public Set<Tuple> exportTuplesOfIOBJ(IndirectObject iobj)
	{
		if (iobj == null)
			return new HashSet<Tuple>();
		Set<Tuple> tuples = new HashSet<Tuple>();
		for (NounPhrase np : iobj.getNounphraseIOlist())
		{
			tuples.addAll(TuplesOfNPExport.getInstance().exportTuplesOfNounPhrase(np));
		}
		tuples.addAll(TuplesOfSbarExport.getInstance().exportTuplesOfSbar(iobj.getVsbarIO()));
		return tuples;
	}
	
	public Set<Tuple> exportTuplesOfDOBJ(DirectObject dobj)
	{
		if (dobj == null)
			return new LinkedHashSet<Tuple>();
		Set<Tuple> tuples =  new LinkedHashSet<Tuple>();
		for (NounPhrase np : dobj.getNounphraseDO())
		{
			tuples.addAll(TuplesOfNPExport.getInstance().exportTuplesOfNounPhrase(np));
		}
		tuples.addAll(TuplesOfSbarExport.getInstance().exportTuplesOfSbar(dobj.getVsbarDO()));
		return tuples;
	}

}
