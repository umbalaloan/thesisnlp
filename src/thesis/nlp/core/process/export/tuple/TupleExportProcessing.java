/**
 * 
 */
package thesis.nlp.core.process.export.tuple;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import thesis.nlp.models.Clause;
import thesis.nlp.models.Complement;
import thesis.nlp.models.DirectObject;
import thesis.nlp.models.IndirectObject;
import thesis.nlp.models.Predicate;
import thesis.nlp.models.PrepositionalPhrase;
import thesis.nlp.models.Subject;
import thesis.nlp.models.Tuple;
import thesis.nlp.models.VerbPhrase;
import thesis.nlp.util.StringProcessUtil;

/**
 * @author lohuynh
 *
 */
public class TupleExportProcessing {

//	private static TupleExportProcessing instance = null;
	private Set<Tuple> tuplesOfPrep = new LinkedHashSet<Tuple>();
	
	public Set<Tuple> exportTuplesOfClause(Clause clause)
	{
		
		Set<Tuple> tuples = new LinkedHashSet<Tuple>();
		tuples.addAll(exportGeneralTuples(clause.getSubject(), clause.getPredicate()));
		return tuples;
	}
	
	private Set<String> objStr (DirectObject dobj, IndirectObject iobj, Complement comp)
	{
		Set<String> objstrs = new LinkedHashSet<String>();
		DobjStringConvert dobjcv = new DobjStringConvert();
		IobjStringConvert iobjcv = new IobjStringConvert();
		ComplementStringConvert compcv = new ComplementStringConvert();
		objstrs.addAll(dobjcv.listDobjStr(dobj));
		objstrs.addAll(iobjcv.listIobjStr(iobj));
		objstrs.addAll(compcv.listCompStr(comp));
		return objstrs;
	}
	/**
	 * get List of objects for VerbPhrase
	 * @param vp
	 * @return
	 */
	public Set<String> objStrForVP (VerbPhrase vp)
	{
		if (vp == null)
			return new LinkedHashSet<String>();
		Set<String> objstr = new LinkedHashSet<String>();
		Set<String> objStrs = objStr(vp.getDirectObj(), vp.getIndirectObj(), vp.getComplement());
		Iterator<String> itr = objStrs.iterator();
		while (itr.hasNext())
		{
			
			String obj = itr.next();
//			System.out.println("OBJ: " + obj);
			if (!obj.trim().equals("")){
				objstr.add(obj);
				if (vp.getListPrepPhrase().size() != 0)
				{
					for (PrepositionalPhrase pp : vp.getListPrepPhrase())
					{
						String prep = StringProcessUtil.valueOf(pp);
						if (!StringProcessUtil.checkStringContainSubString(obj.toString().trim(), prep.trim()))
						{
							objstr.add(prep.trim());
							objstr.add(obj + " " + prep);
//							System.out.println("OBJSTRVP: " + objstr.toString());
						}		
					}
				}
		}
				
		}
		if (objstr.size() == 0)
		{
			if (vp.getListPrepPhrase().size() != 0)
			{
				for (PrepositionalPhrase pp : vp.getListPrepPhrase())
				{
					String prep = StringProcessUtil.valueOf(pp);
					objstr.add(prep.trim());
				}
			}
			else
				objstr.add("");
		}
		
		return objstr;
	}
	
	public Set<Tuple> exportGeneralTuples(Subject subj, Predicate predicate)
	{
		Set<Tuple> tuples = new LinkedHashSet<Tuple>();
		String subject = "";
		String relationship = "";
		String advPhrase = TuplesOfAdverbPhraseExport.getInstance().advPhraseOfTuple(predicate);
		String voice = "";
		String object = "";
		Iterator<String> itr = SubjStringConvert.listSubjStr(subj).iterator();
		if (predicate != null)
		{
			while (itr.hasNext())
			{
				subject = itr.next().trim();
				if (predicate.getVpList().size() > 0)
				{
					for (VerbPhrase vp : predicate.getVpList())
					{
						
						voice = vp.getVerb().getVerbVoice();
						relationship = vp.toString().trim();
						Iterator<String> itrvp = objStrForVP(vp).iterator();
						while(itrvp.hasNext())
						{
							object = itrvp.next().trim();
							Tuple tuple = new Tuple(subject, relationship, object, voice, advPhrase);
							tuples.add(tuple);
//							tuples.addAll(this.tuplesOfPrep);
						}
						Iterator<PrepositionalPhrase> itrprep = vp.getListPrepPhrase().iterator();
						while (itrprep.hasNext())
						{
							tuples.addAll(TupleOfPrepPhraseExport.getInstance().exportTuplesOfPrepPhrase(itrprep.next()));
						}
						tuples.addAll(TupleOfObjectExport.getInstance().exportTuplesOfDOBJ(vp.getDirectObj()));
						tuples.addAll(TupleOfObjectExport.getInstance().exportTuplesOfCOMP(vp.getComplement()));
						tuples.addAll(TupleOfObjectExport.getInstance().exportTuplesOfIOBJ(vp.getIndirectObj()));
					}
				}
			}
			tuples.addAll(TuplesOfAdverbPhraseExport.getInstance().exportTuplesOfAdverbPhrase(predicate.getAdvPhrase()));
		}
		tuples.addAll(TupleOfSubjExport.getInstance().exportTuplesOfSubject(subj));
		
		return tuples;
	}

	public Set<Tuple> getTuplesOfPrep() {
		return tuplesOfPrep;
	}

	public void setTuplesOfPrep(Set<Tuple> tuplesOfPrep) {
		this.tuplesOfPrep = tuplesOfPrep;
	}
	
}
