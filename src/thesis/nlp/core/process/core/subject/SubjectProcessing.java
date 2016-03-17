/**
 * 
 */
package thesis.nlp.core.process.core.subject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import thesis.nlp.core.process.SentenceConstants;
import thesis.nlp.core.process.StanfordTreeTypedDependConsts;
import thesis.nlp.core.process.core.GeneralProcessing;
import thesis.nlp.core.process.core.NounPhraseProcessing;
import thesis.nlp.models.NounPhrase;
import thesis.nlp.models.SBAR;
import thesis.nlp.models.SingleNP;
import thesis.nlp.models.Subject;
import thesis.nlp.util.TypedDependencyCheckUtil;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author lohuynh
 *
 */
public class SubjectProcessing {
	public int subjVoice = SentenceConstants.SUBJVOICE_ACTIVE;
//	private NounPhraseProcessing npp = NounPhraseProcessing.getInstance(); ;
//	private SbarProcessing sbarp  = SbarProcessing.getInstance();;
//	private static SubjectProcessing instance = new SubjectProcessing();
	public SubjectProcessing()
	{
	}
//	public static SubjectProcessing getInstance() {
//		return instance;
//
//	}

	/**
	 * 
	 * @param subjTree
	 * @param tdls
	 * @param taggedWords
	 * @return
	 */
	public Subject processSubjByTDep(List<TypedDependency> tdls, List<CoreLabel> taggedWords) {
		// THIS FUNCTION only process NounPhrase as Subject. In case Subject is
		// a SBAR , it has been not implemented yet

		List<CoreLabel> listLastNounOfSubj = listLastNounOfSubj(tdls);
		List<NounPhrase> nps = new ArrayList<NounPhrase>();
		for (CoreLabel cl : listLastNounOfSubj) {
			// Each NP in Subject
			List<CoreLabel> lastNounOfNP = new ArrayList<CoreLabel>();
			lastNounOfNP.add(cl);
			NounPhrase np = getNPInSubj(tdls, taggedWords, lastNounOfNP);
			nps.add(np);
		}
		SBAR sbarSubj = null;
		if (nps.size() > 0 || sbarSubj != null) {
			return new Subject(nps, sbarSubj, subjVoice);
		} else {
			return null;
		}

	}

	/**
	 * Return NounPhrase in Subject. This function is used to get NounPhrase from TypedDependency
	 * 
	 * @param subjTree
	 * @param rawWords
	 * @param tdls
	 * @param taggedWords
	 * @param lastNounInSubj
	 * @return
	 */
	public NounPhrase getNPInSubj(List<TypedDependency> tdls, List<CoreLabel> taggedWords, List<CoreLabel> lastNounInSubj) {
		NounPhrase np = NounPhraseProcessing.getInstance().processNounPhraseByTDep(tdls, taggedWords, lastNounInSubj);
		return np;
	}

	/**
	 * return Voice of Subject
	 * 
	 * @param np
	 * @param tdls
	 * @return
	 */
	private int voiceOfSubj(List<NounPhrase> nps, List<TypedDependency> tdls) {
		int voice = SentenceConstants.SUBJVOICE_ACTIVE;
		searchEachNP: for (NounPhrase np : nps) {
			Set<SingleNP> singleNPs = np.getSingleNPList();
			for (SingleNP singleNP : singleNPs) {
				int headNounIdx = singleNP.getHeadNounIdx();
				TypedDependency nsubjpass = GeneralProcessing.returnOneRelationByDepIdx(tdls, StanfordTreeTypedDependConsts.NSUBJPASS, "",
						headNounIdx);

				if ((nsubjpass != null) && (nsubjpass.dep().index() == headNounIdx)) {
					voice = SentenceConstants.SUBJVOICE_PASSIVE;
					break searchEachNP;
				}
			}
		}
		return voice;
	}

	/**
	 * If Sentence is passive => Subject follow by "BY"
	 * 
	 * @param tdls
	 * @param taggedWords
	 * @return
	 */
	private List<NounPhrase> processNPofSubjInPassive(List<TypedDependency> tdls, List<CoreLabel> taggedWords) {
		TypedDependency rootDep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.ROOT, "", 0);
		int verbIdx = rootDep.dep().index();
		List<TypedDependency> depAgentTdeps = GeneralProcessing.listTypeDepRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.AGENT, "", verbIdx);
		CoreLabel subjHeadNoun = null;
		List<NounPhrase> nps = new ArrayList<NounPhrase>();
		for (TypedDependency dep : depAgentTdeps) {// check whether or not in
													// relation
													// dep(by-6, Gate-8), follow
													// "BY" is
													// a Noun
			String tag = dep.dep().tag();
			if (TypedDependencyCheckUtil.checkATagIsAnNoun(tag)) {
				subjHeadNoun = new CoreLabel();
				subjHeadNoun.setWord(dep.dep().word());
				subjHeadNoun.setTag(tag);
				subjHeadNoun.setIndex(dep.dep().index());
				// System.out.println("DEP index: " + dep.dep().index());
				break;
			}
		}
		if (subjHeadNoun != null) {
			List<CoreLabel> lastNounInComps = new ArrayList<CoreLabel>();
			lastNounInComps.add(subjHeadNoun);

			// Set<SingleNP> singleNPs = NounPhraseProcessing.listSingleNPsFromTDep(tdls, lastNounInComps);
			NounPhrase np = NounPhraseProcessing.getInstance().processNounPhraseByTDep(tdls, taggedWords, lastNounInComps);
			nps.add(np);
		}
		return nps;
	}

//	private List<CoreLabel> listLastNounOfSubj(List<TypedDependency> tdls) {
//		// exp: subj: Information Technology and Data Mining => return
//		// List<CoreLabel> = {'Technology', 'Mining'}
//		List<CoreLabel> listLastNoun = new ArrayList<CoreLabel>();
//		for (TypedDependency tdl : tdls) {
//			if (tdl.reln().getShortName().equals(StanfordTreeTypedDependConsts.NSUBJ)) {
//				listLastNoun = GeneralProcessing.listHeadWordsOfTdepByDepIdx(tdl, tdls);
//				break;
//			}
//			if (tdl.reln().getShortName().equals(StanfordTreeTypedDependConsts.NSUBJPASS)) {
//				listLastNoun = GeneralProcessing.listHeadWordsOfTdepByDepIdx(tdl, tdls);
//				subjVoice = SentenceConstants.SUBJVOICE_PASSIVE;
//				break;
//			}
//			if (tdl.reln().getShortName().equals(StanfordTreeTypedDependConsts.ROOT)){
//				// get subject for case: 
//				// "Fighting continued in and around Baidoa, particularly at Iidale and Dinsoor; approximately 60 and 120 kilometres south of Baidoa respectively."
//				if (TypedDependencyCheckUtil.checkATagIsAnNoun(tdl.dep().tag()))
//				{
//					listLastNoun = GeneralProcessing.listHeadWordsOfTdepByDepIdx(tdl, tdls);
//					break;
//				}
//			}
//		}
//		return listLastNoun;
//	}
	
	private List<CoreLabel> listLastNounOfSubj(List<TypedDependency> tdls) {
		List<CoreLabel> listLastNoun = new ArrayList<CoreLabel>();
		TypedDependency rootTdep = GeneralProcessing.searchROOTTdep(tdls);
		List<TypedDependency> subjTdeps = listTypeDepRelationByGovIdxForSubjectBeforeVerb(tdls, rootTdep.dep().index());
		if (subjTdeps.size() == 0)
		{
			// get subject for case: 
			// "Fighting continued in and around Baidoa, particularly at Iidale and Dinsoor; approximately 60 and 120 kilometres south of Baidoa respectively."
			if (TypedDependencyCheckUtil.checkATagIsAnNoun(rootTdep.dep().tag()))
				listLastNoun = GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(rootTdep, tdls);
			else
			{
				// for case : "The foundations of the University of the Aegean date back to October 1918 when Greece had expanded its geographic borders to the wider Smyrna area after the World War I."
				TypedDependency depTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DEP, "", rootTdep.dep().index());
				if (depTdep != null){
					if (TypedDependencyCheckUtil.checkATagIsAnNoun(depTdep.dep().tag()))
						listLastNoun = GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(depTdep, tdls);
					else
					{
						// in case : dep (VB, VB)
						subjTdeps = listTypeDepRelationByGovIdxForSubjectBeforeVerb(tdls, depTdep.dep().index());
					}
				}
				else
				{
					subjTdeps = listTypeDepRelationByGovIdxForSubjectAfterVerb(tdls, rootTdep.dep().index());
				}
			}
		}
		if(subjTdeps.size() >0)
		{
			Iterator<TypedDependency> itr = subjTdeps.iterator();
			while (itr.hasNext())
			{
				TypedDependency nsubj = itr.next();
				listLastNoun.addAll(GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(nsubj, tdls));
			}
		}
		
		if (listLastNoun.size() == 0)
		{
			
			// for case: "McGaughey said that Personal Ensign might race again in New York this year and definitely would race next year and be pointed for the Breeders' Cup."
			for (TypedDependency tdl : tdls) {
				if (tdl.reln().getShortName().equals(StanfordTreeTypedDependConsts.NSUBJ)) {
					listLastNoun = GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(tdl, tdls);
					break;
				}
				if (tdl.reln().getShortName().equals(StanfordTreeTypedDependConsts.NSUBJPASS)) {
					listLastNoun = GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(tdl, tdls);
//					subjVoice = SentenceConstants.SUBJVOICE_PASSIVE;
					break;
				}
			}
		}
		return listLastNoun;
	}
	
	
	/**
	 * The function return the list of TypeDepency which has the same govIdx. For exp: COMPOUND, AMOD, DOBJ, IOBJ
	 * 
	 * @param tdls
	 * @param relShortName
	 * @param specific
	 * @param govIdx
	 * @return
	 */
	public static List<TypedDependency> listTypeDepRelationByGovIdxForSubjectBeforeVerb(List<TypedDependency> tdls, int govIdx) {
		List<TypedDependency> oldsubjTdeps = new LinkedList<TypedDependency>();
		List<TypedDependency> newsubjTdeps = new ArrayList<TypedDependency>();
		List<TypedDependency> nsubjTdeps = GeneralProcessing.listTypeDepRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NSUBJ, "", govIdx);
		oldsubjTdeps = nsubjTdeps;
		if (nsubjTdeps.size() == 0)
		{
			oldsubjTdeps = GeneralProcessing.listTypeDepRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NSUBJPASS, "", govIdx);
		}
		
		Iterator<TypedDependency> itr = oldsubjTdeps.iterator();
		while (itr.hasNext())
		{
			TypedDependency tdl = itr.next();
			if (tdl.dep().index() < govIdx)
				newsubjTdeps.add(tdl);
				
		}
//		if (newsubjTdeps.size() ==0)
//			newsubjTdeps = oldsubjTdeps;
		return newsubjTdeps;
	}
	
	public static List<TypedDependency> listTypeDepRelationByGovIdxForSubjectAfterVerb(List<TypedDependency> tdls, int govIdx) {
		List<TypedDependency> oldsubjTdeps = new LinkedList<TypedDependency>();
		List<TypedDependency> nsubjTdeps = GeneralProcessing.listTypeDepRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NSUBJ, "", govIdx);
		oldsubjTdeps = nsubjTdeps;
		if (nsubjTdeps.size() == 0)
		{
			oldsubjTdeps = GeneralProcessing.listTypeDepRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NSUBJPASS, "", govIdx);
		}
		return oldsubjTdeps;
	}
	
}
