/**
 * 
 */
package thesis.nlp.core.process.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import thesis.nlp.core.process.SentenceConstants;
import thesis.nlp.core.process.StanfordTreeTypedDependConsts;
import thesis.nlp.models.SingleNP;
import thesis.nlp.models.Tuple;
import thesis.nlp.util.RelationOfTupleProcessUtil;
import thesis.nlp.util.TypedDependencyCheckUtil;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author lohuynh
 *
 */
public class SingleNPProcessing {
	private static SingleNPProcessing instance = null;
	private int firstIndexOfHeadNoun = -1;
	private Set<Tuple> tuplesOfSingleNP ;
	private String headNoun = "";
	private SingleNPProcessing(){}
	public static SingleNPProcessing getInstance() {
		if (instance == null)
			return new SingleNPProcessing();
		return instance;
		
	}

	public SingleNP processSingleNPByTdep(List<TypedDependency> tdls, List<CoreLabel> taggedWords, CoreLabel lastNoun) {
		tuplesOfSingleNP  = new HashSet<Tuple>();
		int headNounIdx = lastNoun.index(); // default -1 ; headNounIdx get the index of last
		// word in HeadNoun
		String premodifier = "";
		String determier = determinter(tdls, headNounIdx);

		List<TypedDependency> compoundList = GeneralProcessing.listTypeDepRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NN, "",
				lastNoun.index());
		this.firstIndexOfHeadNoun = lastNoun.index(); // headNounIndex
		this.headNoun = returnCompoundNoun(tdls,compoundList) + " " + headNoun(tdls, taggedWords, lastNoun);
		this.firstIndexOfHeadNoun = returnTheFirstIndexOfHeadNoun(compoundList);
		String dep = dependentBeforeHeadNoun(tdls, taggedWords, lastNoun);
		if (!dep.equals(""))
		{
			tuplesOfSingleNP.add(tuplesOfDependentBeforeHeadNoun(headNoun, dep));
			this.headNoun = dep + " " + this.headNoun;
		}
		if (!(dep = dependentAfterHeadNoun(tdls, taggedWords, lastNoun)).equals(""))
		{
			// exp : Moncayo (231 met)
			tuplesOfSingleNP.add(tuplesOfDependentAfterHeadNoun(headNoun, dep));
			this.headNoun = this.headNoun + " " + dep.trim();
		}
			
		// get Premodifier of SingleNP
		premodifier = premodifier(tdls, taggedWords, this.firstIndexOfHeadNoun, lastNoun.index());

		if (!this.headNoun.trim().equals(""))
		{
			SingleNP singleNP = new SingleNP(this.headNoun, premodifier, determier, headNounIdx);
			singleNP.setTuples(tuplesOfSingleNP);
			return singleNP;
		}
		else
			return null;
	}
	
	/**
	 * Exp: Washington, DC, in United State => Washington belongs to United State
	 * @param headNoun
	 * @param dep
	 * @return
	 */
	private Tuple tuplesOfDependentBeforeHeadNoun(String headNoun, String dep)
	{
		String subj = dep.trim();
		String obj = headNoun.trim();
		String relation = "belong to";
		return new Tuple(subj, relation, obj, SentenceConstants.VERBVOICE_ACTIVE, "");
		
	}
	/**
	 * Moncoya (2310 m) => Moncoya is 2310 m
	 * @param headNoun
	 * @param dep
	 * @return
	 */
	private Tuple tuplesOfDependentAfterHeadNoun(String headNoun, String dep)
	{
		String subj = dep.trim();
		String obj = headNoun.trim();
		String relation = "be";
		return new Tuple(subj, relation, obj, SentenceConstants.VERBVOICE_ACTIVE, "");
		
	}
	private String dependentBeforeHeadNoun(List<TypedDependency> tdls, List<CoreLabel> taggedWords, CoreLabel lastNoun)
	{
		String dep = "";
		TypedDependency depTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DEP, "", lastNoun.index());
		if (depTdep != null && depTdep.dep().index() < lastNoun.index())
		{
			List<TypedDependency> compoundList = GeneralProcessing.listTypeDepRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NN, "",
					depTdep.dep().index());
			if (depTdep.dep().index() > returnTheFirstIndexOfHeadNoun(compoundList))
				dep = premodifierOfDependent(tdls, taggedWords, depTdep.dep().index()) + " " +returnCompoundNoun(tdls, compoundList) + " " + depTdep.dep().originalText();
		}
		return dep.trim();
	}
	
	private String dependentAfterHeadNoun(List<TypedDependency> tdls, List<CoreLabel> taggedWords, CoreLabel lastNoun)
	{
		String dep = "";
		TypedDependency depTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DEP, "", lastNoun.index());
		if (depTdep != null && depTdep.dep().index() > lastNoun.index() && (!TypedDependencyCheckUtil.checkATagIsAVerb(depTdep.dep().tag())))
		{
			List<TypedDependency> compoundList = GeneralProcessing.listTypeDepRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NN, "",
					depTdep.dep().index());
			dep = determinter(tdls, depTdep.dep().index()) + " " + premodifierOfDependent(tdls, taggedWords, depTdep.dep().index()) + " " +returnCompoundNoun(tdls, compoundList) + " " + depTdep.dep().originalText();
		}
		return dep.trim();
	}
	
	private String premodifierOfDependent(List<TypedDependency> tdls, List<CoreLabel> taggedWords, int headNounIndex)
	{
		// Exp: The highest mountain in this range is the Moncayo (2,313 m) 
		StringBuilder predep = new StringBuilder();
//		TypedDependency depTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DEP, "", headNounIndex.index());
		String pre = "";
		for (TypedDependency tdl : tdls)
		{
			if (tdl.gov().index() == headNounIndex)
			{
				pre = tdl.dep().originalText();
				if (TypedDependencyCheckUtil.checkRel(tdl, StanfordTreeTypedDependConsts.AMOD))
				{
					predep.append(pre);
					predep.append(" ");
				}
				if (TypedDependencyCheckUtil.checkRel(tdl, StanfordTreeTypedDependConsts.NUM) || TypedDependencyCheckUtil.checkRel(tdl, StanfordTreeTypedDependConsts.NUMBER))
				{
					predep.append(pre);
					predep.append(" ");
				}
				if (TypedDependencyCheckUtil.checkRel(tdl, StanfordTreeTypedDependConsts.POSS))
				{
					List<TypedDependency> compoundTdeps = GeneralProcessing.listTypeDepRelationByDepIdx(tdls, StanfordTreeTypedDependConsts.NN, "", tdl.dep().index());
					pre = returnCompoundNoun(tdls, compoundTdeps) + " " + pre;
					predep.append(pre.trim());
					predep.append(" ");
				}
				if (TypedDependencyCheckUtil.checkRel(tdl, StanfordTreeTypedDependConsts.PRECONJ))
				{
					predep.append(pre);
					predep.append(" ");
				}
				if (TypedDependencyCheckUtil.checkRel(tdl, StanfordTreeTypedDependConsts.ADVMOD))
				{
					predep.append(pre);
					predep.append(" ");
				}
			}
		}
		return predep.toString().trim();
	}
	private String headNoun(List<TypedDependency> tdls, List<CoreLabel> taggedWords, CoreLabel lastNoun)
	{
		String headNoun = lastNoun.word();
		for (TypedDependency tdl : tdls)
		{		
			if (tdl.gov().index() == lastNoun.index()){
				if (tdl.reln().getShortName().equals(StanfordTreeTypedDependConsts.NUM)
						|| (tdl.reln().getShortName().equals(StanfordTreeTypedDependConsts.NUMBER)))
				{
					if (lastNoun.index() < tdl.dep().index())
					{
						// Exp : number(million-18, 950-17)
						//	num($-16, million-18)
						// "Taken for granted it sometimes may be, but this year the Defense Department sought $950 million in assistance from Congress (and secured half that amount) for Ankara's huge military machine, which includes the biggest standing army in NATO outside the United States."
//						for (int i = lastNoun.index()+ 1; i<= tdl.dep().index(); i++)
//						{
//							headNoun = headNoun + " " + taggedWords.get(i).word();
//							System.out.println("HEADNOUN: " + headNoun);
//						}
						TypedDependency numberTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NUMBER, "", tdl.dep().index());
						if (numberTdep != null)
							headNoun = headNoun + " " + numberTdep.dep().originalText();
						headNoun = headNoun + " " + tdl.dep().word();
					}
				}
				if (tdl.reln().getShortName().equals(StanfordTreeTypedDependConsts.APPOS))
				{
					if (lastNoun.index() > tdl.dep().index())
						headNoun = tdl.dep().originalText() + " " + headNoun; 
				}
			}
			if (tdl.reln().getShortName().equals(StanfordTreeTypedDependConsts.TMOD))
			{
				// exp: Robert Barnard (born 23 November 1936) is an English crime writer, critic and lecturer.
				if (lastNoun.index() + 1 == tdl.dep().index())
				{
					headNoun = headNoun + " " + tdl.dep().originalText();
					TypedDependency numTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NUM, "", tdl.dep().index());
						if (numTdep != null)
							headNoun = headNoun + " " + numTdep.dep().originalText();
				}
			}
		}
		return headNoun;
	}

	/**
	 * return the String of Compound Noun which includes many single NN
	 * 
	 * @param compTDeps
	 *            . The list of compound TypedDependency
	 * @return
	 */
	private String returnCompoundNoun(List<TypedDependency> tdls, List<TypedDependency> compTDeps) {
		String compound = "";
		// System.out.println("COMPTDEPS : " + compTDeps.size());
		// if no compound Noun in typedDepend
		if (compTDeps.size() == 0) {
			return "";
		}
		
		for (TypedDependency compTDep : compTDeps) {
			compound = compound + " " + compTDep.dep().originalText();
			for (TypedDependency tdl: tdls)
			{
				// exp: John & Paul Co. sold stocks.
				if ( (tdl.gov().index() == (compTDep.dep().index()))&&(tdl.reln().getShortName().equals(StanfordTreeTypedDependConsts.CONJ)) && (tdl.reln().getSpecific().equals(StanfordTreeTypedDependConsts.AND)))
				{
					compound = compound + " & " + tdl.dep().originalText();

				}
				if ( (tdl.gov().index() == (compTDep.dep().index()))&&(tdl.reln().getShortName().equals(StanfordTreeTypedDependConsts.CONJ)) && (tdl.reln().getSpecific().equals(StanfordTreeTypedDependConsts.OR)))
				{
					// exp : You can choose black, yellow or while apple
					compound = compound + " (or) " + tdl.dep().originalText();

				}
					
			}
			
		}
		// compound = compound + " " +compTDeps.get(0).gov().originalText();
		
		return compound;
	}
	
	
	private int returnTheFirstIndexOfHeadNoun (List<TypedDependency> compTdeps)
	{
		if (compTdeps.size() == 0)
			return this.firstIndexOfHeadNoun;
		for (TypedDependency compTdep : compTdeps)
		{
			if (this.firstIndexOfHeadNoun > compTdep.dep().index())
			{
				this.firstIndexOfHeadNoun = compTdep.dep().index();
			}
		}
		return this.firstIndexOfHeadNoun;
	}
	/**
	 * Get Premodifier of SingleNP
	 * @param tdls
	 * @param taggedWords
	 * @param firstIdxOfHeadNoun
	 * @param lastIdxOfHeadNoun
	 * @return
	 */
	private String premodifier(List<TypedDependency> tdls, List<CoreLabel> taggedWords ,int firstIdxOfHeadNoun, int lastIdxOfHeadNoun)
	{
		String subjOfTuple = "";
		String relnOfTuple = "";
		String premodifier = "";
		int firstIndexOfPre = firstIdxOfHeadNoun;
		for (TypedDependency tdl : tdls)
		{
			subjOfTuple = "";
			String objOfTuple = this.headNoun;;
			if (tdl.gov().index() == lastIdxOfHeadNoun)
			{
				if (TypedDependencyCheckUtil.checkRel(tdl, StanfordTreeTypedDependConsts.AMOD))
				{
					relnOfTuple = RelationOfTupleProcessUtil.relationOfPremodifierAndNoun(tdl);
					subjOfTuple = tdl.dep().originalText();
					Tuple tuple = new Tuple(subjOfTuple.trim(), relnOfTuple.trim(), objOfTuple.trim(), "", "");
					tuplesOfSingleNP.add(tuple);
					if (firstIndexOfPre > tdl.dep().index())
						firstIndexOfPre = tdl.dep().index();
				}
				if (TypedDependencyCheckUtil.checkRel(tdl, StanfordTreeTypedDependConsts.NUM) || TypedDependencyCheckUtil.checkRel(tdl, StanfordTreeTypedDependConsts.NUMBER))
				{
					TypedDependency numberTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NUMBER, "", tdl.dep().index());
					if (numberTdep != null && (firstIndexOfPre > numberTdep.dep().index()))
						firstIndexOfPre = numberTdep.dep().index();
					else if (firstIndexOfPre > tdl.dep().index())
						firstIndexOfPre = tdl.dep().index();
				}
				if (TypedDependencyCheckUtil.checkRel(tdl, StanfordTreeTypedDependConsts.POSS))
				{
					if (firstIndexOfPre > tdl.dep().index())
					{
						subjOfTuple = RelationOfTupleProcessUtil.proNoun(tdl.dep().word());
						firstIndexOfPre = tdl.dep().index();
						List<TypedDependency> compoundList = GeneralProcessing.listTypeDepRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NN, "",
								firstIndexOfPre);
						TypedDependency amodTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.AMOD, "", tdl.dep().index());
						if (amodTdep != null)
						{
							if (firstIndexOfPre > amodTdep.dep().index())
							{
								firstIndexOfPre = amodTdep.dep().index();
								subjOfTuple = returnAmodString(tdls, amodTdep) + " " + subjOfTuple;
							}
						}
						if (compoundList.size() >0)
						{
							subjOfTuple = returnCompoundNoun(tdls, compoundList) + " " + subjOfTuple;
							firstIndexOfPre = returnTheFirstIndexOfHeadNoun(compoundList);
						}
//						else
//							subjOfTuple = RelationOfTupleProcessUtil.proNoun(tdl.dep().word());

						relnOfTuple = RelationOfTupleProcessUtil.relationOfPremodifierAndNoun(tdl);
						objOfTuple = objectTupleOfSingleNP(taggedWords, tdl.dep().index()+1, lastIdxOfHeadNoun);
						Tuple tuple = new Tuple(subjOfTuple.trim(), relnOfTuple.trim(), objOfTuple.trim(), "", "");
						tuplesOfSingleNP.add(tuple);
					}
				}
				if (TypedDependencyCheckUtil.checkRel(tdl, StanfordTreeTypedDependConsts.PRECONJ))
				{
					if (firstIndexOfPre > tdl.dep().index())
						firstIndexOfPre = tdl.dep().index();
				}
				if (TypedDependencyCheckUtil.checkRel(tdl, StanfordTreeTypedDependConsts.ADVMOD))
				{
					if (firstIndexOfPre > tdl.dep().index())
						firstIndexOfPre = tdl.dep().index();
				}
			}
		}
		if (firstIdxOfHeadNoun > 0 && firstIndexOfPre != firstIdxOfHeadNoun)
		{
			for (int i = firstIndexOfPre; i < firstIdxOfHeadNoun; i ++)
			{
				premodifier = premodifier + " " +taggedWords.get(i).word();
			}
		}
		return premodifier;
	}
	
//	private Set<Tuple> listTuplesOfSingleNP()
//	{
//		
//	}
	private  String determinter(List<TypedDependency> tdls, int headNounIdx)
	{
		String det ="";
		TypedDependency detTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DET, "", headNounIdx);
		if (detTdep == null) {
			return det;
		} else {
			det= detTdep.dep().originalText().trim().toLowerCase();
			if (det.equals("a") || det.equals("an") || det.equals("the"))
			{
				det = "";
			}
		}
		return det;
	}
	
	private String objectTupleOfSingleNP(List<CoreLabel> taggedWords, int firstIndex, int lastIndex)
	{
		String obj = "";
		for (int i = firstIndex; i <= lastIndex ; i++)
		{
			obj =  obj + " " + taggedWords.get(i).word();
		}
		obj = obj.replace("'s", "").replace("'", "").replace("\"", "").trim();
		return obj;
	}
	
	/**
	 * return the String of Amod which includes many JJ, JJN
	 * 
	 * @param amodTDep
	 *            . The list of amod typedDependency
	 * @return String
	 */
	private static String returnAmodString(List<TypedDependency> tdls, TypedDependency amodTDep) {
		String amodstr = "";
		if (amodTDep == null) {
			return "";
		}
		amodstr = amodTDep.dep().originalText();
		// conjAnd with multiple adj (JJ or JJN)
//		List<TypedDependency> conjAndTdeps = GeneralProcessing.listTypeDepRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.CONJ,
//				StanfordTreeTypedDependConsts.AND, amodTDep.dep().index());
//		if (conjAndTdeps.size() > 0) {
//			for (TypedDependency conjAndTdep : conjAndTdeps) {
//				amodstr = amodstr + " (and) " + conjAndTdep.dep().originalText();
//			}
//		}
		return amodstr;
	}
}
