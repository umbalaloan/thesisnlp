/**
 * 
 */
package thesis.nlp.core.process.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import thesis.nlp.core.process.SentenceConstants;
import thesis.nlp.core.process.StanfordTreeTypedDependConsts;
import thesis.nlp.core.process.core.verb.VerbPhraseProcessing;
import thesis.nlp.core.process.core.verb.VerbProcessing;
import thesis.nlp.models.NounPhrase;
import thesis.nlp.models.Predicate;
import thesis.nlp.models.PrepositionalPhrase;
import thesis.nlp.models.SBAR;
import thesis.nlp.models.SingleNP;
import thesis.nlp.models.Subject;
import thesis.nlp.models.Verb;
import thesis.nlp.models.VerbPhrase;
import thesis.nlp.util.TypedDependencyCheckUtil;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author lohuynh
 * @history: 
 */
public class SbarProcessing {
	private static SbarProcessing instance = null;
	private PredicateProcessing predicate = new PredicateProcessing();
	public static SbarProcessing getInstance() {
		if (instance == null)
			return new SbarProcessing();
		return instance;
	}
	
	/**
	 * 
	 * @param tdls
	 * @param taggedWords
	 * @param singleNP
	 * @return	Sbar
	 */
	public SBAR processSbarOfNPByTdep(List<TypedDependency> tdls, List<CoreLabel> taggedWords, SingleNP singleNP)
	{
		SBAR sbar = null;
		if ( (sbar = processSbarOfNPForRelativeClause(tdls, taggedWords, singleNP)) != null)
		{
			return sbar;
		}
		else if ((sbar = processSbarOfNPForToInfinitiveClause(tdls, taggedWords, singleNP)) != null)
		{
			return sbar;
		}
		else if ((sbar = processSbarOfNPForPassiveFormOfClause(tdls, taggedWords, singleNP)) != null)
		{
			return sbar;
		}
		else if ((sbar = processSbarOfNPForApposCase(tdls, taggedWords, singleNP)) != null)
		{
			return sbar;
		}
		else if ((sbar = processSbarOfNPForCComp(tdls, taggedWords, singleNP)) != null)
		{
			return sbar;
		}
		else if ((sbar = processSbarOfNPInBracket(tdls, taggedWords, singleNP)) != null)
		{
			return sbar;
		}
		else
			return sbar;
	}
	
	/**
	 * THis function process appositional modifier of NP
	 * <p>Exp: Same, my brother, arrived</p>
	 * @param tdls
	 * @param taggedWords
	 * @param singleNP
	 * @return sbar
	 */
	private SBAR processSbarOfNPForApposCase(List<TypedDependency> tdls, List<CoreLabel> taggedWords, SingleNP singleNP)
	{
		/*
		 * Exp: "My dog, John said, is great"
		 * poss(dog-2, My-1)
			nsubj(great-8, dog-2)
			appos(dog-2, John-4)
			vmod(John-4, said-5)
		 */
//		TypedDependency apposTdeps = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.APPOS, "", singleNP.getHeadNounIdx());
//		if (apposTdeps == null)
//		{
//			return null;
//		}
		List<TypedDependency> apposTdeps = GeneralProcessing.listTypeDepRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.APPOS, "",singleNP.getHeadNounIdx());
		if (apposTdeps.size() == 0)
			return null;
		Subject subjSbar = SbarUtil.processSubjInSbarOfAppos(tdls, taggedWords, apposTdeps);
		Predicate predicateSbar = SbarUtil.processPredicateInSbarOfAppos(tdls, taggedWords, apposTdeps);
		return new SBAR(subjSbar, predicateSbar, "") ;// NO Subordinator
		
	}
	
	
	/**
	 * Process for short form of clause
	 * <p> Exp: Truffles picked during the spring are tasty
	 * @param tdls
	 * @param taggedWords
	 * @param singleNP
	 * @return sbar
	 */
	private SBAR processSbarOfNPForPassiveFormOfClause(List<TypedDependency> tdls, List<CoreLabel> taggedWords, SingleNP singleNP)
	{
		/*
		 * nsubj(tasty-7, Truffles-1)
			vmod(Truffles-1, picked-2)
		 */
		int verbSbarIdx = singleNP.getHeadNounIdx() + 1; // Exp : Truffles picked during the spring
		TypedDependency vmodTdep = GeneralProcessing.returnOneRelationByDepIdx(tdls,StanfordTreeTypedDependConsts.VMOD, "", verbSbarIdx);
		if (vmodTdep == null)
		{
			return null;
		}
		SBAR sbar = null;
		if (TypedDependencyCheckUtil.checkATagIsAVerb(vmodTdep.dep().tag())){
			Subject subjSbar = null;
	//		Predicate predicateSbar = null;
			String refSubj = singleNP.toString();
			Verb verbInSbar = new Verb(vmodTdep.dep().originalText(), SentenceConstants.VERBVOICE_PASSIVE, verbSbarIdx, VerbProcessing.stemmingWord(vmodTdep.dep().originalText()));
			VerbPhrase vpSbar = PredicateProcessing.processVPforAVerb(tdls, taggedWords, verbInSbar);
			if (vpSbar !=null){
				List<VerbPhrase> vpsSbar = new ArrayList<VerbPhrase>();
				vpsSbar.add(vpSbar);
		//		List<PrepositionalPhrase> ppsOfPredicateInSBAR = PredicateProcessing.getInstance().listPrepPhrasesInPredicate(tdls, taggedWords, vpsSbar);
				Predicate predicateSbar = new Predicate(vpsSbar, null); // AdverbClause is default NULL (not implement)
				sbar = new SBAR(subjSbar, predicateSbar,"" ,refSubj);
			}
		}
		return sbar;
	}

	/**
	 * Process toinf Clause
	 * <p> Exp: Teacher instructs students to line up
	 * <p> Exp2: Points to establish ...
	 * @return
	 */
	private SBAR processSbarOfNPForToInfinitiveClause(List<TypedDependency> tdls, List<CoreLabel> taggedWords, SingleNP singleNP)
	{
		/*
		 * dobj(instructed-3, students-5)
			aux(line-7, to-6)
			vmod(instructed-3, line-7)
		 */
		int verbSbarIdx = singleNP.getHeadNounIdx() + 2; // Exp : student to line up
		TypedDependency vmodTdep = GeneralProcessing.returnOneRelationByDepIdx(tdls,	StanfordTreeTypedDependConsts.VMOD, "", verbSbarIdx);
		if (vmodTdep == null)
		{
			return null;
		}
		SBAR sbar = null;
		if (TypedDependencyCheckUtil.checkATagIsAVerb(vmodTdep.dep().tag()))
		{
			Subject subjSbar = null;
			Predicate predicateSbar = null;
			String refSubj = singleNP.toString();
			Verb verbInSbar = new Verb(vmodTdep.dep().originalText(), verbSbarIdx, VerbProcessing.stemmingWord(vmodTdep.dep().originalText()));
			VerbPhrase vpSbar = PredicateProcessing.processVPforAVerb(tdls, taggedWords, verbInSbar);
			if (vpSbar != null)
			{
				List<VerbPhrase> vpsSbar = new ArrayList<VerbPhrase>();
				vpsSbar.add(vpSbar);
		//		List<PrepositionalPhrase> ppsOfPredicateInSBAR = PredicateProcessing.getInstance().listPrepPhrasesInPredicate(tdls, taggedWords, vpsSbar);
				predicateSbar = new Predicate(vpsSbar,  null); // AdverbClause is default NULL (not implement)
				sbar = new SBAR(subjSbar, predicateSbar, "",refSubj);
			}
		}
		return sbar;
	}
	
	/**
	 * Process Verb-ing clause
	 * <br> exp: Crying the whole day, he went to the paris
	 * @param tdls
	 * @param taggedWords
	 * @param singleNP
	 * @return
	 */
	private SBAR processSbarOfGerundVerbClause(List<TypedDependency> tdls, List<CoreLabel> taggedWords, SingleNP singleNP)
	{
		// NOT IMPLEMENT YET
		return new SBAR();
	}
	/**
	 * Process Relative Clause
	 * 
	 * @param tdls
	 * @param taggedWords
	 * @param lastHeadNoun
	 *            . The last word of Noun Phrase which is followed by "subordinator" such as which, who, whom...
	 * @return
	 */
	private SBAR processSbarOfNPForRelativeClause(List<TypedDependency> tdls, List<CoreLabel> taggedWords, SingleNP singleNP) {
		TypedDependency rcmodTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.RCMOD, "", singleNP.getHeadNounIdx());
		if (rcmodTdep == null) {
			return null;
		}
		SBAR sbar = null;
		Subject subjSbar = null;
		Predicate predicateSbar = null;
		String refSubj = singleNP.toString();
		int depIdxInRcmod = rcmodTdep.dep().index();
		String depTagInRcmod = rcmodTdep.dep().tag();
//		int subjVoice = SentenceConstants.SUBJVOICE_ACTIVE;
		TypedDependency tdepforVerb = null;
		if (TypedDependencyCheckUtil.checkATagIsAVerb(depTagInRcmod)) {
			tdepforVerb = rcmodTdep;
			// in case verb follow subordinator - Verb is not a Copular Verb

		} else if (TypedDependencyCheckUtil.checkATagIsAnAdj(depTagInRcmod)|| TypedDependencyCheckUtil.checkATagIsAnNoun(depTagInRcmod)) {
			// for copular Verb
			// Exp: Java is a general-purpose computer programming language that is concurrent, class-based, object-oriented, and specifically
			// designed to have as few implementation dependencies
			// Exp 2: Paul, who is the bravest man, is beautiful
			TypedDependency copTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.COP, "", depIdxInRcmod);
			if (copTdep != null) {
				tdepforVerb = copTdep;
			} else {
				TypedDependency auxpassTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.AUXPASS, "",
						depIdxInRcmod);
				if (auxpassTdep != null)
					// GrammaticalRelation rel = new GrammaticalRelation(Language.English, StanfordTreeTypedDependConsts.COP, "", auxpassTdep.)
					tdepforVerb = auxpassTdep;
			}

		}
		if (tdepforVerb != null) {
			int verbIdx = tdepforVerb.dep().index();
			subjSbar = SbarUtil.processSubjInSbarByVerb(tdls, taggedWords, verbIdx);
			String verbInSbar = tdepforVerb.dep().originalText();
			Verb verb = new Verb(verbInSbar, verbIdx, VerbProcessing.stemmingWord(verbInSbar));
			VerbPhrase vpSbar = PredicateProcessing.processVPforAVerb(tdls, taggedWords, verb);
			List<VerbPhrase> vpsSbar = new ArrayList<VerbPhrase>();
			if (vpSbar != null)
			{
				vpsSbar.add(vpSbar);
	//			List<PrepositionalPhrase> ppsOfPredicateInSBAR = PredicateProcessing.getInstance().listPrepPhrasesInPredicate(tdls, taggedWords, vpsSbar);
				predicateSbar = new Predicate(vpsSbar, null); // AdverbClause is default NULL (not implement)
				sbar = new SBAR(subjSbar, predicateSbar, "" ,refSubj);
			}
		}
		return sbar;
	}
	
	/**
	 * This function process SBAR for NP in case : "Robert Barnard (born 23 November 1936) is an English crime writer, critic and lecturer."
	 * @param tdls
	 * @param taggedWords
	 * @param singleNP
	 * @return
	 */
	private SBAR processSbarOfNPInBracket(List<TypedDependency> tdls, List<CoreLabel> taggedWords, SingleNP singleNP)
	{
		/*
		 * 	dep(Barnard-2, born-4)
			dobj(born-4, 23-5)
			tmod(born-4, November-6)
		 */
		TypedDependency depTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DEP, "", singleNP.getHeadNounIdx());
		if (depTdep != null)
		{
			
		String depTag = depTdep.dep().tag();
			if (TypedDependencyCheckUtil.checkATagIsAVerb(depTag))
			{
				int depIdxOfccomp = depTdep.dep().index(); // verbIdx of dependent Clause
				Subject subjSbar = SbarUtil.processSubjInSbarByVerb(tdls, taggedWords, depIdxOfccomp);
				Verb verbInSbar = new Verb();
				verbInSbar.setVerbName(depTdep.dep().word());
				verbInSbar.setVerbIdx(depIdxOfccomp);
				verbInSbar.setStemmedVerb(VerbProcessing.stemmingWord(depTdep.dep().word()));
				Predicate predicateSbar = SbarUtil.processPredicateInSbarByVerb(tdls, taggedWords, verbInSbar);
				String subordinator = "";
				return new SBAR(subjSbar, predicateSbar, subordinator); // Not implement
			}
			else
				return null;
		}
		else
		{
			return null;
		}
		
	}
	
	/**
	 * This function process CCOMP followed by SingleNP
	 * <p>Exp: He is smart but he does not want to play football</p>
	 * @param tdls
	 * @param taggedWords
	 * @param singleNP
	 * @return
	 */
	private SBAR processSbarOfNPForCComp(List<TypedDependency> tdls, List<CoreLabel> taggedWords, SingleNP singleNP)
	{
		TypedDependency ccompTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.CCOMP, "", singleNP.getHeadNounIdx());
		if (ccompTdep != null) {

			int depIdxOfccomp = ccompTdep.dep().index(); // verbIdx of dependent Clause
			Subject subjSbar = SbarUtil.processSubjInSbarByVerb(tdls, taggedWords, depIdxOfccomp);
			Verb verbInSbar = new Verb();
			verbInSbar.setVerbName(ccompTdep.dep().word());
			verbInSbar.setVerbIdx(depIdxOfccomp);
			verbInSbar.setStemmedVerb(VerbProcessing.stemmingWord(ccompTdep.dep().word()));
			Predicate predicateSbar = SbarUtil.processPredicateInSbarByVerb(tdls, taggedWords, verbInSbar);
			String subordinator = "";
			return new SBAR(subjSbar, predicateSbar, subordinator); // Not implement
		} else {
			return null;
		}
	}

	public SBAR processSbarOfVerbByTdep(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb) {
		SBAR sbar = null;
		if ((sbar = processSbarOfVerbForCCOMP(tdls, taggedWords, verb))!= null)
			return sbar;
		if ((sbar = processSbarOfVerbForVMOD(tdls, taggedWords, verb))!= null)
			return sbar;
		if ((sbar = processSbarOfVerbForXCOMP(tdls, taggedWords, verb))!= null)
			return sbar;
		return sbar;
	}
	
	/**
	 * This function process Complement followed Verb
	 * 
	 * @param tdls
	 * @param taggedWords
	 * @param verb
	 * @return
	 */
	private SBAR processSbarOfVerbForCCOMP(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb) {
		TypedDependency ccompTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.CCOMP, "", verb.getVerbIdx());
		SBAR sbar = null;
		if (ccompTdep != null) {

			int verbSbarIdx = ccompTdep.dep().index(); // verbIdx of dependent Clause
			Verb verbInSbar = null;
			Subject subjSbar = null;
			if (TypedDependencyCheckUtil.checkATagIsAVerb(ccompTdep.dep().tag()))
			{
				verbInSbar = new Verb();
				verbInSbar.setVerbName(ccompTdep.dep().originalText());
				verbInSbar.setVerbIdx(verbSbarIdx);
				verbInSbar.setStemmedVerb(VerbProcessing.stemmingWord(ccompTdep.dep().word()));
				subjSbar = SbarUtil.processSubjInSbarByVerb(tdls, taggedWords, verbSbarIdx);
			}
			else
			{
				// for copular Verb
				TypedDependency copTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.COP, "", ccompTdep.dep().index());
				if (copTdep != null)
				{
					verbInSbar = new Verb();
					verbSbarIdx = copTdep.dep().index();
					verbInSbar.setVerbName(copTdep.dep().word());
					verbInSbar.setVerbIdx(verbSbarIdx);
					verbInSbar.setStemmedVerb(VerbProcessing.stemmingWord(copTdep.dep().word()));
					subjSbar = SbarUtil.processSubjInSbarByVerb(tdls, taggedWords, copTdep.gov().index());
				}
			}
			if (verbInSbar != null)
			{
				Predicate predicateSbar = SbarUtil.processPredicateInSbarByVerb(tdls, taggedWords, verbInSbar);
				String subordinator = "";
				sbar = new SBAR(subjSbar, predicateSbar, subordinator);
			}
		}
		return sbar;
		
	}
	private SBAR processSbarOfVerbForVMOD(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb) {
		//
		TypedDependency vmodTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.VMOD, "", verb.getVerbIdx());
		if (vmodTdep == null)
			return null;
		SBAR sbar = null;
		if (TypedDependencyCheckUtil.checkATagIsAVerb(vmodTdep.dep().tag()))
		{
			Subject subjSbar = null;
			Predicate predicateSbar = null;
			Verb verbInSbar = new Verb(vmodTdep.dep().originalText(), vmodTdep.dep().index(), VerbProcessing.stemmingWord(vmodTdep.dep().originalText()));
			VerbPhrase vpSbar = PredicateProcessing.processVPforAVerb(tdls, taggedWords, verbInSbar);
			if (vpSbar != null)
			{
				List<VerbPhrase> vpsSbar = new ArrayList<VerbPhrase>();
				vpsSbar.add(vpSbar);
				predicateSbar = new Predicate(vpsSbar,  null); // AdverbClause is default NULL (not implement)
				sbar = new SBAR(subjSbar, predicateSbar, "","");
			}
		}
		return sbar;
	}
	
	private SBAR processSbarOfVerbForXCOMP(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb) {
		// "Terrorist attacks by E.T.A. have declined in recent years and the number of its hardcore militants is thought to have fallen from the hundreds of 15 years ago to several score."
		TypedDependency xcompTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.XCOMP, "", verb.getVerbIdx());
		if (xcompTdep == null)
			return null;
		SBAR sbar = null;
		if (TypedDependencyCheckUtil.checkATagIsAVerb(xcompTdep.dep().tag()))
		{
			int verbInSbarIdx = xcompTdep.dep().index();
			Subject subjSbar = SbarUtil.processSubjInSbarByVerb(tdls, taggedWords, verbInSbarIdx);
			if (subjSbar == null)
				subjSbar = SbarUtil.processSubjInSbarByVerb(tdls, taggedWords, verbInSbarIdx);
			Predicate predicateSbar = null;
			Verb verbInSbar = new Verb(xcompTdep.dep().originalText(), verbInSbarIdx, VerbProcessing.stemmingWord(xcompTdep.dep().originalText()));
			VerbPhrase vpSbar = PredicateProcessing.processVPforAVerb(tdls, taggedWords, verbInSbar);
			if (vpSbar != null)
			{
				List<VerbPhrase> vpsSbar = new ArrayList<VerbPhrase>();
				vpsSbar.add(vpSbar);
				predicateSbar = new Predicate(vpsSbar,  null); // AdverbClause is default NULL (not implement)
				sbar = new SBAR(subjSbar, predicateSbar, "","");
			}
		}
		return sbar;
	}
	
	private SBAR processSbarOfVerbForTHATClause(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb) {
		TypedDependency markTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.MARK, "", verb.getVerbIdx());
		if (markTdep == null)
			return null;
		String subordinator = markTdep.dep().word();
		SBAR sbar = null;
		
		//mark(hurting-14, that-3)
		if (TypedDependencyCheckUtil.checkATagIsAVerb(markTdep.gov().tag()))
		{
			int verbSbarIdx = markTdep.gov().index();
			String verbName = markTdep.gov().originalText();
			Subject subjSbar = SbarUtil.processSubjInSbarByVerb(tdls, taggedWords, verbSbarIdx);;
			Predicate predicateSbar = null;
			Verb verbInSbar = new Verb(verbName, verbSbarIdx, VerbProcessing.stemmingWord(verbName));
			VerbPhrase vpSbar = PredicateProcessing.processVPforAVerb(tdls, taggedWords, verbInSbar);
			if (vpSbar != null)
			{
				List<VerbPhrase> vpsSbar = new ArrayList<VerbPhrase>();
				vpsSbar.add(vpSbar);
				predicateSbar = new Predicate(vpsSbar,  null); // AdverbClause is default NULL (not implement)
				sbar = new SBAR(subjSbar, predicateSbar, "",subordinator);
			}
		}
		return sbar;
	}

	
	/**
	 * process Subject of SBAR followed by Noun Phrase
	 * <p>Exp: Paul, who is the bravest man in the word, does not want to fight</p>
	 * @param tdls
	 * @param taggedWords
	 * @param verbIdxOfSbar
	 * @return
	 */
//	public Subject processSubjInSbarByNoun(List<TypedDependency> tdls, List<CoreLabel> taggedWords, TypedDependency nsubjTdep) {
//		List<CoreLabel> listHeadNounsofSubjInSbar = new ArrayList<CoreLabel>();
//		TypedDependency nsubjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NSUBJ, "", verbIdxOfSbar);
//		int subjVoice = SentenceConstants.SUBJVOICE_ACTIVE;
//		if (nsubjTdep != null) {
//			listHeadNounsofSubjInSbar = GeneralProcessing.listHeadWordsOfTdepByDepIdx(nsubjTdep, tdls);
//		} else {
//			TypedDependency nsubjpassTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NSUBJPASS, "",
//					verbIdxOfSbar);
//			if (nsubjpassTdep != null) {
//				listHeadNounsofSubjInSbar = GeneralProcessing.listHeadWordsOfTdepByDepIdx(nsubjpassTdep, tdls);
//				subjVoice = SentenceConstants.SUBJVOICE_PASSIVE;
//			}
//		}
//		List<NounPhrase> nplist = new ArrayList<NounPhrase>();
//		if (listHeadNounsofSubjInSbar.size() > 0) {
//			for (CoreLabel cl : listHeadNounsofSubjInSbar) {
//				// Each NP in Subject
//				List<CoreLabel> lastNounOfNP = new ArrayList<CoreLabel>();
//				lastNounOfNP.add(cl);
//				NounPhrase np = NounPhraseProcessing.getInstance().processNounPhraseByTDep(tdls, taggedWords, listHeadNounsofSubjInSbar);
//				nplist.add(np);
//			}
//		}
//		SBAR subSbarOfSubj = null; // NOT IMPLEMENT
//		return new Subject(nplist, subSbarOfSubj, subjVoice);
//	}
//	
//	public Subject 
	
//	private SBAR processSbarOfAPForToInfClause(List<TypedDependency> tdls, List<CoreLabel> taggedWords, int adjIndex)
//	{
//		TypedDependency xcompTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.XCOMP, "", adjIndex);
//		if (xcompTdep == null)
//		{
//			return null;
//		}
//		
//		Subject subjSbar = null; // No subject for to inf clause
//		
//	}

}
