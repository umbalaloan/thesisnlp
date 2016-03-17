/**
 * 
 */
package thesis.nlp.core.process.core.verb;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import thesis.nlp.core.process.SentenceConstants;
import thesis.nlp.core.process.StanfordTreeTypedDependConsts;
import thesis.nlp.core.process.core.GeneralProcessing;
import thesis.nlp.core.process.core.PrepositionalPhraseProcessing;
import thesis.nlp.models.NounPhrase;
import thesis.nlp.models.PrepositionalPhrase;
import thesis.nlp.models.SingleNP;
import thesis.nlp.models.Subject;
import thesis.nlp.models.Verb;
import thesis.nlp.util.TupleUtil;
import thesis.nlp.util.TypedDependencyCheckUtil;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author lohuynh
 *
 */
public class VerbPhraseProcessing {
	private static int verbType = SentenceConstants.DITRANSITIVE_VERB;
	private static VerbPhraseProcessing instance = new VerbPhraseProcessing();

	private VerbPhraseProcessing() {
	}

	public static VerbPhraseProcessing getInstance() {
		return instance;

	}

	public Set<Verb> findListMainVerbStr(List<CoreLabel> taggedWords, List<TypedDependency> tdls, Subject subj) {
		// Tree[] childTrees = predicateTree.children();
		Set<Verb> verbs = new LinkedHashSet<Verb>();
		// this condition for the Case that DEP in ROOT is not a verb
		Verb mainVerb = getFirstMainVerbWithSubj(tdls, taggedWords, subj);
		if (mainVerb == null)
			mainVerb = getFirstMainVerbWithoutSubj(tdls, taggedWords);
		if (mainVerb != null)
			verbs.add(mainVerb);
		Set<Verb> conjVerbs = listConjVerbs(tdls, mainVerb); // runs and goes
		if (conjVerbs.size() > 0) {
			for (Verb conjAndVerb : conjVerbs) {
				verbs.add(conjAndVerb);
				verbs.addAll(listConjVerbs(tdls, conjAndVerb));
			}
		}
		Set<Verb> toInfVerbs = listInifinitiveVerbAndGerundVerb(tdls, mainVerb);
		if (toInfVerbs.size() > 0) {
			for (Verb toinfVerb : toInfVerbs) {
				verbs.add(toinfVerb);
			}
		}
		return verbs;
	}
	
	

	public Set<Verb> findListVerbsRelatedToMainVerb(List<CoreLabel> taggedWords, List<TypedDependency> tdls, Verb mainVerb) {
		// Tree[] childTrees = predicateTree.children();
		Set<Verb> verbs = new LinkedHashSet<Verb>();
		// this condition for the Case that DEP in ROOT is not a verb
		verbs.add(mainVerb);
		Set<Verb> conjVerbs = listConjVerbs(tdls, mainVerb); // runs and goes
		if (conjVerbs.size() > 0) {
			for (Verb conjAndVerb : conjVerbs) {
				verbs.add(conjAndVerb);
			}
		}
		Set<Verb> toInfVerbs = listInifinitiveVerbAndGerundVerb(tdls, mainVerb);
		if (toInfVerbs.size() > 0) {
			for (Verb toinfVerb : toInfVerbs) {
				verbs.add(toinfVerb);
			}
		}
		return verbs;
	}

	public Verb getFirstMainVerbWithSubj(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Subject subj)
	{
		if (subj == null)
			return null;
		if (subj.getNplist().size() == 0)
			return null;
		String verbName = "";
		int verbIdx = -1;
		Verb verb = null;
		Set<SingleNP> singleNPsInlistofNPs = TupleUtil.listOfSingleNPInListOfNP(subj.getNplist());
		searchSingleNP:
			for (SingleNP singleNP : singleNPsInlistofNPs)
			{
				int headNounIdx = singleNP.getHeadNounIdx();
				for (TypedDependency tdl : tdls)
				{
					if (tdl.dep().index() == headNounIdx)
					{
						if (TypedDependencyCheckUtil.checkRel(tdl, StanfordTreeTypedDependConsts.NSUBJ) && TypedDependencyCheckUtil.checkATagIsAVerb(tdl.gov().tag()))
						{
							verbName = tdl.gov().originalText();
							verbIdx = tdl.gov().index();
							verb = new Verb(verbName, verbIdx, verbType, VerbProcessing.stemmingWord(verbName));
							break searchSingleNP;
						}
						if (TypedDependencyCheckUtil.checkRel(tdl, StanfordTreeTypedDependConsts.NSUBJPASS) && TypedDependencyCheckUtil.checkATagIsAVerb(tdl.gov().tag()))
						{
							verbName = tdl.gov().originalText();
							verbIdx = tdl.gov().index();
							verb = new Verb(verbName, verbIdx, verbType, VerbProcessing.stemmingWord(verbName));
							break searchSingleNP;
						}
//						if (TypedDependencyCheckUtil.checkRel(tdl, StanfordTreeTypedDependConsts.COP) && TypedDependencyCheckUtil.checkATagIsAVerb(tdl.gov().tag()))
//						{
//							verbName = tdl.dep().originalText();
//							verbIdx = tdl.dep().index();
//							verbType = SentenceConstants.COPULAR_VERB;
//							verb = new Verb(verbName, verbIdx, verbType, VerbProcessing.stemmingWord(verbName));
//							break searchSingleNP;
//						}
					}
				}
			}
		return verb;
	}
	/**
	 * searching main verb without index of NounPhrase in subject
	 * @param tdls
	 * @param taggedWords
	 * @return
	 */
	public Verb getFirstMainVerbWithoutSubj(List<TypedDependency> tdls, List<CoreLabel> taggedWords) {
		/*
		 * 
		 * (NP (PRP He)) (VP (VBZ knows) (SBAR (WHADVP (WRB where)) (S (NP (PRP you)) (VP (VBP go))))))) => get verb "know"
		 */
		String verbName = "";
		int verbIdx = -1;
		Verb verb = null;
		TypedDependency rootTdep = GeneralProcessing.searchROOTTdep(tdls);
		String tagDepOfRoot = rootTdep.dep().tag();
		int indexDepOfRoot = rootTdep.dep().index();
		if (TypedDependencyCheckUtil.checkATagIsAVerb(tagDepOfRoot)) {
			verbName = rootTdep.dep().originalText(); // verb in ROOT tdep
			// depTag = tDep.dep().tag();
			verbIdx = indexDepOfRoot;
			verb = new Verb(verbName, verbIdx, verbType, VerbProcessing.stemmingWord(verbName));
		} else {
			TypedDependency copTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.COP, "", indexDepOfRoot);
			if (copTdep != null) {
				verbName = copTdep.dep().originalText();
				verbIdx = copTdep.dep().index();
				verbType = SentenceConstants.COPULAR_VERB;
				verb = new Verb(verbName, verbIdx, verbType, VerbProcessing.stemmingWord(verbName));
			} else {
				// Other case. Exp: "They get dirty around the edges"
				TypedDependency depTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DEP, "", indexDepOfRoot);
				if (depTdep != null) {
					String tagDepOfDep = depTdep.dep().tag();
					if (TypedDependencyCheckUtil.checkATagIsAVerb(tagDepOfDep)) {
						verbName = depTdep.dep().originalText();
						verbIdx = depTdep.dep().index();
						verb = new Verb(verbName, verbIdx, verbType, VerbProcessing.stemmingWord(verbName));
					} else if (TypedDependencyCheckUtil.checkATagIsAnAdj(tagDepOfDep)) {
						//
						/*
						 * fix error in UnDefined Parse Sentence // Exp :
						 * "Java is concurrent, class-based, object-oriented, and specifically designed to have as few implementation dependencies as possible"
						 */

						TypedDependency auxpassTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.AUXPASS, "",
								indexDepOfRoot);
						if (auxpassTdep != null) {
							verbName = auxpassTdep.dep().originalText();
							verbIdx = auxpassTdep.dep().index();
							verb = new Verb(verbName, verbIdx, verbType, VerbProcessing.stemmingWord(verbName));
						}
					}
					else
					{
						copTdep = VerbProcessing.copularVerb(tdls);
						if (copTdep != null) {
							/*
							 * for Copular Verb
							 * Exp : "The highest mountain in this range is the Moncayo (2,313 m) and, despite getting less snow than in the Pyrenees, it has several ski resorts."
							 * cop(concurrent-3, is-2)
							 */
							verbName = copTdep.dep().originalText();
							verbIdx = copTdep.dep().index();
							verbType = SentenceConstants.COPULAR_VERB;
							verb = new Verb(verbName, verbIdx, verbType, VerbProcessing.stemmingWord(verbName));
						}
					}
				} 
				
			}
			
		}
		return verb;

	}

	/**
	 * 
	 * @param tdls
	 * @param mainVerb
	 * @return
	 */
	private Set<Verb> listConjVerbs(List<TypedDependency> tdls, Verb mainVerb) {
		/*
		 * Exp : "She goes and runs" nsubj(runs-4, she-1) root(ROOT-0, goes-2) conj_and(goes-2, runs-4) => return "runs"
		 */
		if (mainVerb == null) {
			return new LinkedHashSet<Verb>();
		}
		Set<Verb> verbs = new LinkedHashSet<Verb>();
		List<TypedDependency> conj_andTdeps = GeneralProcessing.listTypeDepRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.CONJ,
				StanfordTreeTypedDependConsts.SPECIFIC_AND, mainVerb.getVerbIdx());
		for (TypedDependency conjTdep : conj_andTdeps) {
			String depTag = conjTdep.dep().tag();
			if (TypedDependencyCheckUtil.checkATagIsAVerb(depTag)) {
				Verb verb = new Verb(conjTdep.dep().originalText(), conjTdep.dep().index(), verbType, VerbProcessing.stemmingWord(conjTdep.dep()
						.originalText()));
				verbs.add(verb);
			}
		}
		return verbs;
	}

	/**
	 * This function to list Infinitive Verb:
	 * 
	 * @Exp: I have to work and sleep => output is "work" and "sleep"
	 * @param predicateTree
	 * @param rawWords
	 * @param tdl
	 * @param mainVerb
	 * @return
	 */
	private static Set<Verb> listInifinitiveVerbAndGerundVerb(List<TypedDependency> tdls, Verb mainVerb) {
		Set<Verb> verbs = new LinkedHashSet<Verb>();
		if (mainVerb == null) {
			return new LinkedHashSet<Verb>();
		}
		for (TypedDependency tDep : tdls) {
			String shortNameTdep = tDep.reln().getShortName();
			if(tDep.gov().index() == mainVerb.getVerbIdx())
			{
				if (shortNameTdep.equals(StanfordTreeTypedDependConsts.XCOMP)) {
//					if (tDep.gov().index() == mainVerb.getVerbIdx()) {
						// Exp: He attempt to break s.th => xcomp(attempt-3, break-5)
						// Exp : I miss taking the photo => xcomp(miss-2, taking-3)
						if (tDep.dep().tag().equals(StanfordTreeTypedDependConsts.VB)) {
							String verbName = tDep.dep().originalText();
							int verbIdx = tDep.dep().index();
							Verb verb = new Verb(verbName, verbIdx, verbType, verbName); // stemmedVerb = verbName for this case
							verbs.add(verb);
						}
						if (tDep.dep().tag().equals(StanfordTreeTypedDependConsts.VBG)) {
							// Gerund Verb
							String verbName = tDep.dep().originalText();
							int verbIdx = tDep.dep().index();
							Verb verb = new Verb(verbName, verbIdx, verbType, VerbProcessing.stemmingWord(verbName)); // stemmedVerb = verbName for this
																														// case
							verbs.add(verb);
						}
//					}
				}
				else if (shortNameTdep.equals(StanfordTreeTypedDependConsts.PREPC)) {
					/*
					 * Exp : We laughed about having to do such silly things => prepc_about(laughed-2, having-4) aux(do-6, to-5) xcomp(having-4, do-6)
					 */
					if (tDep.dep().tag().equals(StanfordTreeTypedDependConsts.VBG) || tDep.dep().tag().equals(StanfordTreeTypedDependConsts.VBN)) {
						String verbName = tDep.dep().originalText();
						int verbIdx = tDep.dep().index();
						Verb verb = new Verb(verbName, verbIdx, verbType, VerbProcessing.stemmingWord(verbName)); // stemmedVerb = verbName for this case
						verbs.add(verb);
					}
				} 
				}
		}
		return verbs;
	}

	/**
	 * 
	 * @param originalVerb
	 *            . The main Verb
	 * @param tdls
	 * @return
	 */
	public static int getStatePosNeg(Verb originalVerb, List<TypedDependency> tdls) {
		for (TypedDependency tdl : tdls) {
			if ((tdl.reln().getShortName().equals(StanfordTreeTypedDependConsts.NEG)) && (tdl.gov().index() == originalVerb.getVerbIdx())) {
				return SentenceConstants.VERB_STATE_NEG;
			}
		}
		return SentenceConstants.VERB_STATE_POS;
	}

	/**
	 * 
	 * @param originalVerb
	 * @param tdls
	 * @return
	 */
	public static String findAuxOfVerb(Verb originalVerb, List<TypedDependency> tdls) {
		for (TypedDependency tdl : tdls) {
			if ((tdl.reln().getShortName().equals(StanfordTreeTypedDependConsts.AUX)) && (tdl.gov().index() == originalVerb.getVerbIdx())) {
				return tdl.dep().originalText();
			}
		}
		return null;

	}
	
	/**
	 * return preposition phrase in VerbPhrase
	 * 
	 * @param predicateTree
	 * @param rawWords
	 * @return
	 */
	public List<PrepositionalPhrase> listPrepPhrasesInVerbPhrase(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb, String particle) {
		List<PrepositionalPhrase> pps = PrepositionalPhraseProcessing.getInstance().processPPOfVerb(tdls, taggedWords, verb, particle);
		// for other special
		// "this year the Defense Department sought $950 million in assistance from Congress for Ankara's huge military machine, which includes the biggest standing army in NATO outside the United States"
		// prep has relation with ROOT
		return pps;
	}
}
