/**
 * 
 */
package thesis.nlp.core.process.core.object;

import java.util.ArrayList;
import java.util.List;

import thesis.nlp.core.process.StanfordTreeTypedDependConsts;
import thesis.nlp.core.process.core.GeneralProcessing;
import thesis.nlp.core.process.core.NounPhraseProcessing;
import thesis.nlp.core.process.core.SbarProcessing;
import thesis.nlp.core.process.core.subject.SubjectProcessing;
import thesis.nlp.models.DirectObject;
import thesis.nlp.models.NounPhrase;
import thesis.nlp.models.SBAR;
import thesis.nlp.models.Verb;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author lohuynh
 * @history
 * 18/10/2015 : add function "listDOBJfollowedbyParticle" and add one "Particle" parameter in processDOBJ
 */
public class DirectObjectProcessing {
	private List<NounPhrase> npdobjs = new ArrayList<NounPhrase>();
	private SBAR sbarDobj = null;
	private String pronounDO = "";
	private static DirectObjectProcessing instance = null;

	private DirectObjectProcessing() {
	}

	public static DirectObjectProcessing getInstance() {
		if (instance == null)
			return new DirectObjectProcessing();
		return instance;
	}

	/**
	 * The function return the Direct Object of sentence which is the list of SingleNP or SBAR clause or PronounDO
	 * 
	 * @param tdls
	 *            . List TypedDependency of sentence
	 * @param taggedWords
	 *            . List<CoreLabel> taggedWord of sentence
	 * @param listverbs
	 *            . List the main verb in sentence
	 * @return . The DirectObject of sentence
	 * @ExpofPREDICATETREE: (VP (MD can) (VP (VB see) (NP (NP (DT the) (NN girl)) (CC and) (NP (NP (DT the) (NN boy)) (SBAR (WHNP (WP who)) (S (NP
	 *                      (PRP you)) (VP (VBP know))))))))
	 * @InputofObjTree1: (VP (VB see) (NP (NP (DT the) (NN girl)) (CC and) (NP (NP (DT the) (NN boy)) (SBAR (WHNP (WP who)) (S (NP (PRP you)) (VP (VBP
	 *                   know))))))).
	 * @OR Input: (NP (NP (DT the) (NN girl)) (CC and) (NP (NP (DT the) (NN boy)) (SBAR (WHNP (WP who)) (S (NP (PRP you)) (VP (VBP know))))))
	 */

	public DirectObject processDOBJ(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb, String particle) {
		DirectObject dobj = null;
		// if (verb.getVerbVoice() == SentenceConstants.VERBVOICE_ACTIVE || verb.getVerbType() == SentenceConstants.COPULAR_VERB) {
		sbarDobj = getSbarForDOBJ(tdls, taggedWords, verb);
		npdobjs = getNPsInDOBJ(tdls, taggedWords, verb, particle);
		if ((npdobjs.size()>0) || (sbarDobj != null)) {
			dobj = new DirectObject(npdobjs, sbarDobj, pronounDO);
		}
	
		return dobj;
	}

	/**
	 * Process the list of NPs in Direct Object. Each NP is a SingleNP with PrepPhrase
	 * 
	 * @param treeNP
	 * @param tdls
	 * @param taggedWords
	 * @param verb
	 * @return
	 */
	private List<NounPhrase> getNPsInDOBJ(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb, String particle) {
		List<NounPhrase> nps = new ArrayList<NounPhrase>();
		List<CoreLabel> listDobjHead = listLastNounOfDOBJs(tdls, verb, particle);
		if (listDobjHead == null) {
			return nps;
		}
		for (CoreLabel cl : listDobjHead) {
			// for each Noun Phrase of Direct Objects
			List<CoreLabel> dobjHead = new ArrayList<CoreLabel>();
			dobjHead.add(cl);
			NounPhrase np = NounPhraseProcessing.getInstance().processNounPhraseByTDep(tdls, taggedWords, dobjHead);
			nps.add(np);
		}
		return nps;
	}

	/**
	 * This function get headNOun of DOBJ based on TypedDEpendency and verb
	 * 
	 * @Exp: dobj(see-3, girl-5) => Noun is girl
	 * @param tdls
	 * @param listverbs
	 * @return CoreLabel . This includes word name, index, tag word
	 */

	private List<CoreLabel> listLastNounOfDOBJs(List<TypedDependency> tdls, Verb verb, String particle) {

		if (tdls.size() == 0 || verb == null) {
			return new ArrayList<CoreLabel>();
		}
		TypedDependency dobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DOBJ, "", verb.getVerbIdx());
		// for multiple DOBJ
		List<CoreLabel> listLastNoun = GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(dobjTdep, tdls);
		listLastNoun.addAll(listDOBJfollowedbyParticle(tdls, verb, particle));
		return listLastNoun;
	}

	private List<CoreLabel> listDOBJfollowedbyParticle(List<TypedDependency> tdls, Verb verb, String particle) {
		// I talk about Bill Gate
		if (particle.equals("")) {
			return new ArrayList<CoreLabel>();
		}
		TypedDependency prepTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PREP, particle, verb.getVerbIdx());
		return GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(prepTdep, tdls);
	}

	/**
	 * If DirectObj is a SBAR clause, the function return the SBAR model for DOBJ
	 * 
	 * @param refSubj
	 *            . It is the subject which SBAR supports
	 * @param sbarTree
	 *            .
	 * @param rawWords
	 *            . List<Corelabel> rawWord of sentence
	 * @param tdls
	 *            . List TypedDependency of sentence
	 * @param taggedWords
	 *            . List<CoreLabel> taggedWords of sentence
	 * @return . Return SBAR clause
	 */
	private SBAR getSbarForDOBJ(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb) {
		// TypedDependency dobjTdep
		return SbarProcessing.getInstance().processSbarOfVerbByTdep(tdls, taggedWords, verb);
	}

	private DirectObject processDOBJInPassiveSentence(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb) {
		// THIS FUNCTION only process NounPhrase as Subject. In case Subject is
		// a SBAR , it has been not implemented yet

		List<CoreLabel> listLastNounOfDOBJ = listlastNounOfDOBJInPassiveSentence(tdls, taggedWords, verb);
		// List<NounPhrase> nps = new ArrayList<NounPhrase>();
		for (CoreLabel cl : listLastNounOfDOBJ) {
			// Each NP in Subject
			List<CoreLabel> lastNounOfNP = new ArrayList<CoreLabel>();
			lastNounOfNP.add(cl);
			SubjectProcessing subj = new SubjectProcessing();
			NounPhrase np = subj.getNPInSubj(tdls, taggedWords, listLastNounOfDOBJ);
			npdobjs.add(np);
		}

//		for (CoreLabel cl : listLastNounOfDOBJ) {
//			sbarDobj = SbarProcessing.getInstance().processSbarOfNPByTdep(tdls, taggedWords, cl);
//			if (sbarDobj != null)
//				break;
//		}
		return new DirectObject(npdobjs, sbarDobj, pronounDO);
	}

	private List<CoreLabel> listlastNounOfDOBJInPassiveSentence(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb) {
		List<CoreLabel> listlastNounOfDOBJ = new ArrayList<CoreLabel>();
		TypedDependency nsubjpassTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NSUBJPASS, "",
				verb.getVerbIdx());
		listlastNounOfDOBJ = GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(nsubjpassTdep, tdls);
		return listlastNounOfDOBJ;
	}
}
