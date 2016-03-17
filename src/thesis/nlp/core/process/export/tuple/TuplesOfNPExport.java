/**
 * 
 */
package thesis.nlp.core.process.export.tuple;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import thesis.nlp.core.process.SentenceConstants;
import thesis.nlp.models.NounPhrase;
import thesis.nlp.models.Predicate;
import thesis.nlp.models.PrepositionalPhrase;
import thesis.nlp.models.SBAR;
import thesis.nlp.models.SingleNP;
import thesis.nlp.models.Tuple;
import thesis.nlp.models.VerbPhrase;
import thesis.nlp.util.StringProcessUtil;

/**
 * @author lohuynh
 *
 */
public class TuplesOfNPExport {
	
//	private TupleExportProcessing tuplesExport = new TupleExportProcessing();
	private static TuplesOfNPExport instance = null;
	public static TuplesOfNPExport getInstance() {
		if (instance == null) {
			instance = new TuplesOfNPExport();
		}
		return instance;
	}

	
	/**
	 * Export Sbar - Tuples Of NounPhrase 
	 * @param np
	 * @return
	 */
	public Set<Tuple> exportTuplesOfNounPhraseForSbar(NounPhrase np)
	{
		if (np == null)
			return new HashSet<Tuple>();
		Set<Tuple> tuples = new HashSet<Tuple>();
		SBAR sbar = np.getSbarNP();
		if (sbar !=null)
		{
			if ( sbar.getSubj() != null)
			{
				if (sbar.getPredicateSbar() != null)
				{
					tuples.addAll(TuplesOfSbarExport.getInstance().exportTuplesOfSbar(sbar));
//					tuples.addAll(exportTuplesOfNPWithSbarNP(np));
				}
				else
					tuples.addAll(exportTuplesOfNounPhraseOnlySubjInSbar(np));
			}
			else
				tuples.addAll(exportTupleOfNounPhraseOnlyPredicateInSbar(np));
		}
		return tuples;
	}
//	public Set<Tuple> exportTuplesOfNounPhraseForSbar(NounPhrase np)
//	{
//		if (np == null)
//			return new HashSet<Tuple>();
//		Set<Tuple> tuples = new HashSet<Tuple>();
//		SBAR sbar = np.getSbarNP();
//		if (sbar !=null)
//		{
//			if ( sbar.getSubj() != null)
//			{
//				if (sbar.getPredicateSbar() != null)
//					tuples.addAll(TuplesOfSbarExport.getInstance().exportTuplesOfSbar(sbar));
//				else
//					tuples.addAll(exportTuplesOfNounPhraseOnlySubjInSbar(np));
//			}
//			else
//				tuples.addAll(exportTupleOfNounPhraseOnlyPredicateInSbar(np));
//		}
//		return tuples;
//	}
	
	/**
	 * Exp: The girl, Anas, has blue eyes => The girl is Anas
	 * @param np
	 * @return
	 */
//	public Tuple exportTupleOfNounPhraseOnlySubjInSbar(NounPhrase np)
//	{
//		if (np == null)
//			return null;
//		Set<Tuple> tuples = new LinkedHashSet<Tuple>();
//		String subj = np.toString().trim();
//		String relation = " be ";
//		String object = np.getSbarNP().getSubj().toString().trim();
//		String adv = "";
//		Tuple tuple = new Tuple(subj, relation, object, SentenceConstants.VERBVOICE_ACTIVE, adv);
//		tuples.add(tuple);
////		tuples.addAll(exportTuplesOfSbar(np.getSbarNP())); // Add Tuples of NP in Subject of Sbar
//		return tuple;
//	}
	public Set<Tuple> exportTuplesOfNounPhraseOnlySubjInSbar(NounPhrase np)
	{
		Set<Tuple> tuples = new LinkedHashSet<Tuple>();
		if (np == null)
			return tuples;
		String subj = np.toString().trim();
		String relation = " be ";
		String object = np.getSbarNP().getSubj().toString().trim();
		String adv = "";
		if (!object.equals(""))
		{
			Tuple tuple = new Tuple(subj, relation, object, SentenceConstants.VERBVOICE_ACTIVE, adv);
			tuples.add(tuple);
		}
		for (NounPhrase anp : np.getSbarNP().getSubj().getNplist())
			tuples.addAll(exportTuplesOfNounPhrase(anp)); // Add Tuples of NP in Subject of Sbar
		return tuples;
	}
	/**
	 * Exp: the girl made up last night is beautiful
	 * @param np
	 * @return
	 */
	public Set<Tuple> exportTupleOfNounPhraseOnlyPredicateInSbar(NounPhrase np)
	{
		if (np == null)
			return new LinkedHashSet<Tuple>();
		TupleExportProcessing tupleexport = new TupleExportProcessing();
		Set<Tuple> tuples = new LinkedHashSet<Tuple>();
		String subj = np.toString().trim();
		String relationship = "";
		String object = "";
		String voice = "";
		String advPhrase = TuplesOfAdverbPhraseExport.getInstance().advPhraseOfTuple(np.getSbarNP().getPredicateSbar());
		for (VerbPhrase vp : np.getSbarNP().getPredicateSbar().getVpList())
		{
			voice = vp.getVerb().getVerbVoice();
			relationship = vp.toString().trim();
			Iterator<String> itrvp = tupleexport.objStrForVP(vp).iterator();
			while(itrvp.hasNext())
			{
				object = itrvp.next().trim();
				Tuple tuple = new Tuple(subj, relationship, object, voice, advPhrase);
				tuples.add(tuple);
			}
			// extract tuples of prep
			Iterator<PrepositionalPhrase> itrprep = vp.getListPrepPhrase().iterator();
			while (itrprep.hasNext())
			{
				tuples.addAll(TupleOfPrepPhraseExport.getInstance().exportTuplesOfPrepPhrase(itrprep.next()));
			}
//			tuples.addAll(tupleexport.getTuplesOfPrep());
			Tuple tuple = new Tuple(subj, relationship, object.toString().trim(), voice, advPhrase);
			tuples.add(tuple);
		}
		return tuples;
	}
	
	/**
	 * Export Sbar - Tuples Of NounPhrase 
	 * @param np
	 * @return
	 */
	public Set<Tuple> exportTuplesOfNounPhrase(NounPhrase np)
	{
		if (np == null)
			return new LinkedHashSet<Tuple>();
		Set<Tuple> tuples = new LinkedHashSet<Tuple>();
		tuples.addAll(exportTuplesOfNounPhraseForSbar(np));
		Iterator<PrepositionalPhrase> itrprep = np.getPrepPhraseLiist().iterator();
		while (itrprep.hasNext())
		{
			tuples.addAll(TupleOfPrepPhraseExport.getInstance().exportTuplesOfPrepPhrase(itrprep.next()));
		}
		tuples.addAll(exportTuplesOfSingleNP(np));
		tuples.addAll(getTuplesSingleNPAndPrepPhrase(np));
//		tuples.addAll(exportTuplesOfNPWithSbarNP(np));
		return tuples;
	}
	
	private Set<Tuple> exportTuplesOfNPWithSbarNP(NounPhrase np){
		if (np == null)
			return new LinkedHashSet<Tuple>();
		if (np.getSbarNP() == null || np.getSbarNP().getPredicateSbar() == null)
			return new LinkedHashSet<Tuple>();
		Predicate predicateOfSbar = np.getSbarNP().getPredicateSbar();
		String subject = NounPhraseStringConvert.strOflistSingleNPInNP(np);
		String relationship = "";
		String voice = "";
		String advPhrase = "";
		Set<Tuple> tuples = new LinkedHashSet<Tuple>();
		TupleExportProcessing tuplesExport = new TupleExportProcessing();
		for (VerbPhrase vp : predicateOfSbar.getVpList())
		{
			String object = "";
			voice = vp.getVerb().getVerbVoice();
			relationship = vp.toString().trim();
			Iterator<String> itrvp = tuplesExport.objStrForVP(vp).iterator();
			if (!subject.equals(""))
			{
				while(itrvp.hasNext())
				{
					object = itrvp.next().trim();
					Tuple tuple = new Tuple(subject, relationship, object, voice, advPhrase);
					tuples.add(tuple);
				}
			}
		}
		return tuples;
	}
	
//	private Tuple exportTuplesRelationshipbetweenNPandSbarNP(NounPhrase np)
//	{
//		if (np == null)
//			return null;
//		
//	}
	
	
	private Set<Tuple> exportTuplesOfSingleNP(NounPhrase np)
	{
//		System.out.println("@@@@@@@@@@@@exportTuplesOfSingleNP: " + np.getSingleNPList().toString());
		if (np.getSingleNPList().size() == 0)
			return new LinkedHashSet<Tuple>();
		Iterator<SingleNP> itrsingle = np.getSingleNPList().iterator();
		Set<Tuple> tuples = new HashSet<Tuple>();
		while (itrsingle.hasNext())
		{
			SingleNP singleNP = itrsingle.next();
			tuples.addAll(singleNP.getTuples());
		}
		return tuples;
	}
	
	/**
	 * This funtion return tuples which show relationship between SingleNP and PrepPhrase
	 * @param np
	 * @return
	 */
	private Set<Tuple> getTuplesSingleNPAndPrepPhrase(NounPhrase np) {
		StringBuilder strBuilder = new StringBuilder();
		if (np.getSingleNPList().size() == 0 || np.getPrepPhraseLiist().size() == 0)
		{
			return new LinkedHashSet<Tuple>();
		}
		Set<Tuple> tuples = new LinkedHashSet<Tuple>();
		int i = 0;
		for (SingleNP singleNP : np.getSingleNPList())
		{
			i++;
			if (i>=2)
				strBuilder.append(",");
			strBuilder.append(singleNP.toString());
			strBuilder.append(" ");
		}
		String subj = strBuilder.toString().trim();
//		System.out.println("@@@@@@@@@@@@@@@@@getTuplesSingleNPAndPrepPhrase : " + subj);
		String relation = " be (complemented by)";
		StringBuilder objbuilder = new StringBuilder();
		for (PrepositionalPhrase prepPhrase : np.getPrepPhraseLiist())
		{
			String str = StringProcessUtil.valueOf(prepPhrase);
			Tuple tuple = new Tuple(subj, relation, str, SentenceConstants.VERBVOICE_ACTIVE, "");
			tuples.add(tuple);
			objbuilder.append(str.trim());
			objbuilder.append(" ");
		}
		if ( np.getPrepPhraseLiist().size() > 1){
			Tuple tuple = new Tuple(subj, relation, objbuilder.toString().trim(), SentenceConstants.VERBVOICE_ACTIVE, "");
			tuples.add(tuple);
		}
		return tuples;
	}
	
//	private Set<Tuple> getTupleOfSingleNP (SingleNP singleNP)
//	{
//		
//	}
}
