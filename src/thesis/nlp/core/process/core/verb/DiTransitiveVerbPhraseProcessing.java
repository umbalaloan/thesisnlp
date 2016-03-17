/**
 * 
 */
package thesis.nlp.core.process.core.verb;

import java.util.List;

import thesis.nlp.core.process.SentenceConstants;
import thesis.nlp.core.process.StanfordTreeTypedDependConsts;
import thesis.nlp.core.process.core.GeneralProcessing;
import thesis.nlp.core.process.core.object.ComplementProcessing;
import thesis.nlp.core.process.core.object.DirectObjectProcessing;
import thesis.nlp.core.process.core.object.IndirectObjectProcessing;
import thesis.nlp.models.Complement;
import thesis.nlp.models.DirectObject;
import thesis.nlp.models.IndirectObject;
import thesis.nlp.models.PrepositionalPhrase;
import thesis.nlp.models.Verb;
import thesis.nlp.models.VerbPhrase;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author lohuynh
 *
 */
public class DiTransitiveVerbPhraseProcessing {
	private DiTransitiveVerbPhraseProcessing(){}
	/**
	 * 
	 * Type 7: Di-Transitive Verb 2 objects - May have supplements List of verb: http://www.cse.unsw.edu.au/~billw/ditransitive.html Subject -- Verb
	 * -- Indirect Object -- Direct Object
	 * 
	 * @e.g. "I give him a book." "I give a book to him."
	 * 
	 * @param tdls
	 * @param subjTree
	 * @param predicateTree
	 * @param objOrCompTree
	 * @param rawWords
	 * @param taggedWords
	 * @param originalVerb
	 * @param patternType
	 * @param particle
	 * @return
	 */
	public static VerbPhrase processVerbType7(List<TypedDependency> tdls, 
			List<CoreLabel> taggedWords, Verb originalVerb, int patternType) {
		VerbPhrase vp = null;
		String aux = VerbPhraseProcessing.findAuxOfVerb(originalVerb, tdls);
		int posNeg = VerbPhraseProcessing.getStatePosNeg(originalVerb, tdls);
		int govIdx = originalVerb.getVerbIdx();
		String verbVoice = originalVerb.getVerbVoice();
		if (verbVoice == SentenceConstants.VERBVOICE_ACTIVE)
			verbVoice = VerbProcessing.voiceOfVerb(tdls, originalVerb.getVerbIdx());
		Verb verb = new Verb();
		if (verbVoice == SentenceConstants.VERBVOICE_ACTIVE) {
			TypedDependency dobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DOBJ, "", govIdx);
			TypedDependency iobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.IOBJ, "", govIdx);
			if ((dobjTdep != null) && (iobjTdep != null)) {
				String tagWord = taggedWords.get(govIdx).tag();
				verb.setVerbName(originalVerb.getVerbName());
				verb.setVerbIdx(govIdx);
				verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
				verb.setVerbVoice(verbVoice);
				verb.setVerbType(SentenceConstants.VERB_TYPE_07);
				verb.setStemmedVerb(originalVerb.getStemmedVerb());
				DirectObject dobj = DirectObjectProcessing.getInstance().processDOBJ( tdls, taggedWords, verb, "");
				IndirectObject iobj = IndirectObjectProcessing.getInstance().processIOBJ(tdls, taggedWords, verb, "");
				Complement comp = null;
				List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, "");
				vp = new VerbPhrase(aux, posNeg, "", patternType, verb, iobj, dobj, comp, pps);
			}
		} 
		else {
			TypedDependency dobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DOBJ, "", govIdx);
			TypedDependency iobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.IOBJ, "", govIdx);
			if ((dobjTdep != null) && (iobjTdep == null)) {
				// Passive Voice
				// e.g. "He is given a book."
				// get DObject, but no IObject
				String tagWord = taggedWords.get(GeneralProcessing.convertWordIdxFromTDepToListCoreLabel(govIdx)).tag();
				verb.setVerbName(originalVerb.getVerbName());
				verb.setVerbIdx(govIdx);
				verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
				verb.setVerbVoice(verbVoice);
				verb.setVerbType(SentenceConstants.VERB_TYPE_07);
				verb.setStemmedVerb(originalVerb.getStemmedVerb());
				DirectObject dobj = DirectObjectProcessing.getInstance().processDOBJ(tdls, taggedWords, verb, "");
				IndirectObject iobj = IndirectObjectProcessing.getInstance().processIOBJ(tdls, taggedWords, verb, "");
				Complement comp = ComplementProcessing.getInstance().processComplement(tdls, taggedWords, verb);
				List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, "");
				vp = new VerbPhrase(aux, posNeg, "", patternType, verb, iobj, dobj, comp, pps);
			}
		}
		return vp;
	}
}
