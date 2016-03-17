/**
 * 
 */
package thesis.nlp.core.process.export.tuple;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import thesis.nlp.models.NounPhrase;
import thesis.nlp.models.PrepositionalPhrase;
import thesis.nlp.models.SBAR;
import thesis.nlp.models.Tuple;
import thesis.nlp.models.VerbPhrase;
import thesis.nlp.util.StringProcessUtil;
import thesis.nlp.util.TupleUtil;

/**
 * @author lohuynh
 *
 */
public class TuplesOfSbarExport {
	private TupleExportProcessing tuplesExport = new TupleExportProcessing();
	private static TuplesOfSbarExport instance = null;
	public TuplesOfSbarExport() {
		// Exists only to defeat instantiation.
	}

	public static TuplesOfSbarExport getInstance() {
		if (instance == null) {
			instance = new TuplesOfSbarExport();
		}
		return instance;
	}
	
	public Set<Tuple> exportTuplesOfSbar(SBAR sbar)
	{
		if (sbar == null)
			return new HashSet<Tuple>();
		Set<Tuple> tuples = new HashSet<Tuple>();
		if (sbar.getSubj() == null)
			tuples.addAll(exportTuplesOfSbarWithouSubjInSbar(sbar));
		else
		{
			tuples.addAll(exportTuplesOfSbarWithSubjInSbar(sbar));
			if (sbar.getRefSubj().equals(""))
				tuples.addAll(exportTuplesOfSbarWithSubjWithoutReferSubj(sbar));
		}
		return tuples;
	}
	/**
	 * List tuples of sbar such as: The girl who has beautiful eyes.
	 * Tuple of Sbar = "Reference Subject + Verb + Object"
	 * @param sbar
	 * @return
	 */
	private Set<Tuple> exportTuplesOfSbarWithouSubjInSbar(SBAR sbar)
	{
		Set<Tuple> tuples = new HashSet<Tuple>();
		if (sbar != null && sbar.getPredicateSbar() !=null)
		{
			String subject = sbar.getRefSubj().trim();
			String relationship = "";
			String voice = "";
			String advPhrase = TuplesOfAdverbPhraseExport.getInstance().advPhraseOfTuple(sbar.getPredicateSbar());
			for (VerbPhrase vp : sbar.getPredicateSbar().getVpList())
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
				Iterator<PrepositionalPhrase> itrpp = vp.getListPrepPhrase().iterator();
				while(itrpp.hasNext())
				{
					PrepositionalPhrase pp = itrpp.next();
					tuples.addAll(TupleOfPrepPhraseExport.getInstance().exportTuplesOfPrepPhrase(pp));
				}
//				tuples.addAll(tuplesExport.getTuplesOfPrep());
				tuples.addAll(TupleOfObjectExport.getInstance().exportTuplesOfDOBJ(vp.getDirectObj()));
				tuples.addAll(TupleOfObjectExport.getInstance().exportTuplesOfCOMP(vp.getComplement()));
				tuples.addAll(TupleOfObjectExport.getInstance().exportTuplesOfIOBJ(vp.getIndirectObj()));
			}
			tuples.addAll(TuplesOfAdverbPhraseExport.getInstance().exportTuplesOfAdverbPhrase(sbar.getPredicateSbar().getAdvPhrase()));
		}
		return tuples;
	}
	
	/**
	 * Exp: The girl who you know
	 * Tuples : SubjInSbar + Verb + ReferenceSubj
	 * @param sbar
	 * @return
	 */
	public Set<Tuple> exportTuplesOfSbarWithSubjInSbar(SBAR sbar)
	{
		Set<Tuple> tuples = new HashSet<Tuple>();
		if (sbar != null && sbar.getPredicateSbar()!=null)
		{
			String subject = StringProcessUtil.valueOf(sbar.getSubj()).trim();
			boolean subjIsTHAT = TupleUtil.checkSubjIsTHAT_WH(subject);
			if ( subjIsTHAT && (!sbar.getRefSubj().equals(""))) // replace "that"by refer Subj if has
				subject = sbar.getRefSubj().trim();
//			String subject = SubjStringConvert.subjInSbar(sbar);
			String relationship = "";
			String voice = "";
			String advPhrase = TuplesOfAdverbPhraseExport.getInstance().advPhraseOfTuple(sbar.getPredicateSbar());
			Iterator<VerbPhrase> itrvps = sbar.getPredicateSbar().getVpList().iterator();
			while (itrvps.hasNext())
			{
				VerbPhrase vp = itrvps.next();
				StringBuilder object = new StringBuilder();
				voice = StringProcessUtil.valueOf(vp.getVerb().getVerbVoice());
				relationship = vp.toString().trim();
				if (!subjIsTHAT)
					object.append(sbar.getRefSubj().trim()); // No add RefSubj in THAT clause
				Iterator<String> itrvp = tuplesExport.objStrForVP(vp).iterator();
				String strobj = sbar.getRefSubj().trim();
				while(itrvp.hasNext())
				{
					strobj = itrvp.next().trim();
					Tuple tuple = new Tuple(subject, relationship, strobj.trim(), voice, advPhrase);
					tuples.add(tuple);
//					tuples.addAll(this.tuplesOfPrep);
				}
				if (!object.toString().trim().equals(""))
				{
					Tuple tuple = new Tuple(subject, relationship, object.toString(), voice, advPhrase);
					tuples.add(tuple);
				}
				if (vp.getListPrepPhrase().size() != 0)
				{
					for (PrepositionalPhrase pp : vp.getListPrepPhrase())
					{
						object.append(" ");
						object.append(StringProcessUtil.valueOf(pp).trim());
						Tuple tuple = new Tuple(subject, relationship, object.toString().trim(), voice, advPhrase);
						tuples.add(tuple);
						tuples.addAll(TupleOfPrepPhraseExport.getInstance().exportTuplesOfPrepPhrase(pp));
					}
				}
				
				tuples.addAll(TupleOfObjectExport.getInstance().exportTuplesOfDOBJ(vp.getDirectObj()));
				tuples.addAll(TupleOfObjectExport.getInstance().exportTuplesOfCOMP(vp.getComplement()));
				tuples.addAll(TupleOfObjectExport.getInstance().exportTuplesOfIOBJ(vp.getIndirectObj()));
			}
			for(NounPhrase np : sbar.getSubj().getNplist())
			{
				// export tuples of NP in Subj Of Sbar
				tuples.addAll(TuplesOfNPExport.getInstance().exportTuplesOfNounPhrase(np));
			}
			tuples.addAll(TupleOfSubjExport.getInstance().exportTuplesOfSubject(sbar.getSubj()));
			tuples.addAll(TuplesOfAdverbPhraseExport.getInstance().exportTuplesOfAdverbPhrase(sbar.getPredicateSbar().getAdvPhrase()));
		}
		return tuples;
	}
	
	/**
	 * Tuples Of Sbar without ReferenceSubject
	 * Exp: He is smart and fat but he does not want to play football.
	 * @param sbar
	 * @return
	 */
	public Set<Tuple> exportTuplesOfSbarWithSubjWithoutReferSubj(SBAR sbar)
	{
		Set<Tuple> tuples = new HashSet<Tuple>();
		if (sbar == null)
			return new LinkedHashSet<Tuple>();
		tuples.addAll(tuplesExport.exportGeneralTuples(sbar.getSubj(), sbar.getPredicateSbar()));
		for(NounPhrase np : sbar.getSubj().getNplist())
		{
			// export tuples of NP in Subj Of Sbar
			tuples.addAll(TuplesOfNPExport.getInstance().exportTuplesOfNounPhrase(np));
		}
		
		return tuples;
	}
}
