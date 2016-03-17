/**
 * 
 */
package thesis.nlp.core.process.core.object;

import java.util.ArrayList;
import java.util.List;

import thesis.nlp.core.process.StanfordTreeTypedDependConsts;
import thesis.nlp.core.process.core.GeneralProcessing;
import thesis.nlp.core.process.core.NounPhraseProcessing;
import thesis.nlp.models.IndirectObject;
import thesis.nlp.models.NounPhrase;
import thesis.nlp.models.SBAR;
import thesis.nlp.models.Verb;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author lohuynh
 *
 */
public class IndirectObjectProcessing {
	private static IndirectObjectProcessing instance = null;

	private IndirectObjectProcessing() {
	}

	public static IndirectObjectProcessing getInstance() {
		if (instance == null)
			return new IndirectObjectProcessing();
		return instance;

	}

	public IndirectObject processIOBJ(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb, String particle) {
		IndirectObject iobj = null;
		List<NounPhrase> npIobjs = new ArrayList<NounPhrase>();
		String pronounIO = "";
		SBAR sbarIobj = getSbarForIOBJ(tdls, taggedWords, verb);
		if (!particle.equals("")) {
			npIobjs = getNPInIOBJWithoutIOBJtdep(tdls, taggedWords, verb, particle);
		} else {
			npIobjs = getNPsInIOBJ(tdls, taggedWords, verb, particle);
		}
		if (sbarIobj != null || npIobjs.size() > 0) {
			iobj = new IndirectObject(npIobjs, sbarIobj, pronounIO);
		}

		return iobj;
	}

	private List<NounPhrase> getNPsInIOBJ(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb, String particle) {
		List<NounPhrase> nps = new ArrayList<NounPhrase>();
		List<CoreLabel> listIobjHead = listLastNounOfIOBJs(tdls, verb);
		if (listIobjHead.size() == 0) {
			return nps;
		}
		for (CoreLabel cl : listIobjHead) {
			// for each Noun Phrase of Direct Objects
			List<CoreLabel> iobjHead = new ArrayList<CoreLabel>();
			iobjHead.add(cl);
			NounPhrase np = NounPhraseProcessing.getInstance().processNounPhraseByTDep(tdls, taggedWords, iobjHead);
			nps.add(np);
		}
		return nps;
	}

	private List<CoreLabel> listLastNounOfIOBJs(List<TypedDependency> tdls, Verb verb) {
		if (tdls.size() == 0) {
			return new ArrayList<CoreLabel>();
		}

		TypedDependency iobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.IOBJ, "", verb.getVerbIdx());

		return GeneralProcessing.listHeadNounWordsOfTdepByDepIdx(iobjTdep, tdls);
	}

	private List<NounPhrase> getNPInIOBJWithoutIOBJtdep(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb, String particle) {
		List<NounPhrase> nps = new ArrayList<NounPhrase>();
		List<CoreLabel> listIobjHead = listLastNounOfIOBJsWithoutIOBJtdep(tdls, verb, particle);
		if (listIobjHead == null) {
			return nps;
		}
		for (CoreLabel cl : listIobjHead) {
			// for each Noun Phrase of Direct Objects
			List<CoreLabel> iobjHead = new ArrayList<CoreLabel>();
			iobjHead.add(cl);
			NounPhrase np = NounPhraseProcessing.getInstance().processNounPhraseByTDep(tdls, taggedWords, iobjHead);
			nps.add(np);
		}
		return nps;
	}

	private List<CoreLabel> listLastNounOfIOBJsWithoutIOBJtdep(List<TypedDependency> tdls, Verb verb, String particle) {
		if (tdls.size() == 0 || verb == null) {
			return new ArrayList<CoreLabel>();
		}
		TypedDependency prepTdep = GeneralProcessing.returnOneRelationByDepIdx(tdls, StanfordTreeTypedDependConsts.PREP, particle, verb.getVerbIdx());

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
	private static SBAR getSbarForIOBJ(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb) {
		// NOT IMPLEMENT
		return null;
	}
}
