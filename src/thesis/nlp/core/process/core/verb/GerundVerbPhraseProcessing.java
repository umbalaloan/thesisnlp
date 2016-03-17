/**
 * 
 */
package thesis.nlp.core.process.core.verb;

import java.awt.GraphicsEnvironment;
import java.util.List;

import thesis.nlp.core.process.SentenceConstants;
import thesis.nlp.core.process.StanfordTreeTypedDependConsts;
import thesis.nlp.core.process.core.GeneralProcessing;
import thesis.nlp.core.process.core.object.ComplementProcessing;
import thesis.nlp.models.Complement;
import thesis.nlp.models.DirectObject;
import thesis.nlp.models.IndirectObject;
import thesis.nlp.models.PrepositionalPhrase;
import thesis.nlp.models.Verb;
import thesis.nlp.models.VerbPhrase;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author lohuynh
 *
 */
public class GerundVerbPhraseProcessing {
	private GerundVerbPhraseProcessing(){}
	/**
	 * Type 10: Verb + gerund
	 * 
	 * @param tdls
	 * @param subjTree
	 * @param predicateTree
	 * @param objOrCompTree
	 * @param rawWords
	 * @param taggedWords
	 * @param originalVerb
	 * @param patternType
	 * @return
	 */
	public static VerbPhrase processVerbType10(List<TypedDependency> tdls,
			 List<CoreLabel> taggedWords, Verb originalVerb, int patternType) {
		/*
		 * Exp : "You mentioned going to Italy last summer" =>  nsubj(mentioned-2, You-1) root(ROOT-0, mentioned-2) xcomp(mentioned-2, going-3)
		 * "I really miss taking photos with an analogue camera"
		 */
		VerbPhrase vp = null;
		String aux = VerbPhraseProcessing.findAuxOfVerb(originalVerb, tdls);
		int posNeg = VerbPhraseProcessing.getStatePosNeg(originalVerb, tdls);
		int govIdx = originalVerb.getVerbIdx();

		String verbVoice = VerbProcessing.voiceOfVerb(tdls, originalVerb.getVerbIdx());
		Verb verb = new Verb();
		if (verbVoice == SentenceConstants.VERBVOICE_ACTIVE) {
			TypedDependency dobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DOBJ, "", govIdx);
			TypedDependency iobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.IOBJ, "", govIdx);
			TypedDependency xcompTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.XCOMP, "", govIdx);
			boolean isToInfinitive = false;
			if (xcompTdep != null) {
				// mark(go-4, to-3)
				// xcomp(want-2, go-4)
				int infVerbIdx = xcompTdep.dep().index();
//				TypedDependency markTdep = GeneralProcessing.returnOneRelationByDepIdx(tdls, StanfordTreeTypedDependConsts.MARK, "", infVerbIdx);
//				if (markTdep != null)
//				{
//					isToInfinitive = true;
//				}
				if (!xcompTdep.dep().tag().equals(StanfordTreeTypedDependConsts.VBG))
				{
					isToInfinitive = true;
				}
			}
			if ((dobjTdep == null) && (iobjTdep == null) && (!isToInfinitive)) {
				String tagWord = taggedWords.get(govIdx).tag();
				verb.setVerbName(originalVerb.getVerbName());
				verb.setVerbIdx(govIdx);
				verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
				verb.setVerbVoice(verbVoice);
				verb.setVerbType(SentenceConstants.VERB_TYPE_10);
				verb.setStemmedVerb(originalVerb.getStemmedVerb() + " + doing sth");
				DirectObject dobj = null;
				IndirectObject iobj = null;
				Complement comp = ComplementProcessing.getInstance().processComplement(tdls, taggedWords, verb);
				List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, "");
				vp = new VerbPhrase(aux, posNeg, "", patternType, verb, iobj, dobj, comp, pps);
			}
		}
		return vp;
	}
	
	/**
	 * Type 11: Verb + PP + gerund
     * link: http://www.grammaring.com/verb-preposition-gerund
     * @Exp: "We laughed about having to do such silly things"
	 * @param tdls
	 * @param subjTree
	 * @param predicateTree
	 * @param objOrCompTree
	 * @param rawWords
	 * @param taggedWords
	 * @param originalVerb
	 * @param patternType
	 * @return
	 */
	public static VerbPhrase processVerbType11(List<TypedDependency> tdls,
			 List<CoreLabel> taggedWords, Verb originalVerb, int patternType, String particle) {
		/*
		 * Exp : "We laughed about having to do such silly things"
		 */
		VerbPhrase vp = null;
		String aux = VerbPhraseProcessing.findAuxOfVerb(originalVerb, tdls);
		int posNeg = VerbPhraseProcessing.getStatePosNeg(originalVerb, tdls);
		int govIdx = originalVerb.getVerbIdx();

		String verbVoice = VerbProcessing.voiceOfVerb(tdls, originalVerb.getVerbIdx());
		Verb verb = new Verb();
		if (verbVoice == SentenceConstants.VERBVOICE_ACTIVE) {
			TypedDependency dobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DOBJ, "", govIdx);
			TypedDependency iobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.IOBJ, "", govIdx);
			TypedDependency xcompTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.XCOMP, "", govIdx);
			TypedDependency prepTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PREPC, particle, govIdx);// prepc_about(laughed-2, having-4)
			boolean isToInfinitive = false;
			if (xcompTdep != null) {
				int infVerbIdx = xcompTdep.dep().index();
				TypedDependency auxTdep = GeneralProcessing.returnOneRelationByDepIdx(tdls, StanfordTreeTypedDependConsts.AUX, "", infVerbIdx);
				if (auxTdep != null && auxTdep.dep().originalText().toUpperCase().equals("TO"))
				{
					isToInfinitive = true;
				}
			}
			else if (prepTdep != null)
			{
				if (!(prepTdep.dep().tag().equals(StanfordTreeTypedDependConsts.VBG) || (prepTdep.dep().tag().equals(StanfordTreeTypedDependConsts.VBN))) )
				{
					isToInfinitive = true;
				}
			}
			if ((dobjTdep == null) && (iobjTdep == null) && (!isToInfinitive) && (prepTdep != null)) {
				String tagWord = taggedWords.get(govIdx).tag();
				verb.setVerbName(originalVerb.getVerbName());
				verb.setVerbIdx(govIdx);
				verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
				verb.setVerbVoice(verbVoice);
				// verb.setVerbType(SentenceConstants.TRANSITIVE_VERB);
				verb.setStemmedVerb(originalVerb.getStemmedVerb() + " + doing sth");
				DirectObject dobj = null;
				IndirectObject iobj = null;
				Complement comp = null;
				List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, particle);
				vp = new VerbPhrase(aux, posNeg, particle, patternType, verb, iobj, dobj, comp, pps);
			}
		}
		return vp;
	}
	
	/**
	 * NOT IMPLEMENT YET
	 * @param tdls
	 * @param subjTree
	 * @param predicateTree
	 * @param objOrCompTree
	 * @param taggedWords
	 * @param originalVerb
	 * @param patternType
	 * @param particle
	 * @return
	 */
	public  static VerbPhrase processVerbType12(List<TypedDependency> tdls, 
			 List<CoreLabel> taggedWords, Verb originalVerb, int patternType, String particle) {
		/*
         * Type 12: Verb + PP + NP + gerund             (very rare)
        // Ex.: I objected to him calling me a liar.
        */
		VerbPhrase vp = null;
		String aux = VerbPhraseProcessing.findAuxOfVerb(originalVerb, tdls);
		int posNeg = VerbPhraseProcessing.getStatePosNeg(originalVerb, tdls);
		int govIdx = originalVerb.getVerbIdx();

		String verbVoice = VerbProcessing.voiceOfVerb(tdls, originalVerb.getVerbIdx());
		Verb verb = new Verb();
		if (verbVoice == SentenceConstants.VERBVOICE_ACTIVE) {
			TypedDependency dobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DOBJ, "", govIdx);
			TypedDependency iobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.IOBJ, "", govIdx);
//			TypedDependency xcompTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.XCOMP, "", govIdx);
			TypedDependency prepTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PREPC, particle, govIdx);// prepc_about(laughed-2, having-4)
		
			if ((dobjTdep == null) && (iobjTdep == null) && (prepTdep!= null)) {
				String tagWord = taggedWords.get(govIdx).tag();
				verb.setVerbName(originalVerb.getVerbName());
				verb.setVerbIdx(govIdx);
				verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
				verb.setVerbVoice(verbVoice);
				// verb.setVerbType(SentenceConstants.TRANSITIVE_VERB);
				verb.setStemmedVerb(originalVerb.getStemmedVerb() + " + s.o doing sth");
				DirectObject dobj = null;
				IndirectObject iobj = null;
				Complement comp = null;
				List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, particle);
				vp = new VerbPhrase(aux, posNeg, particle, patternType, verb, iobj, dobj, comp, pps);
			}
		}
		else
		{
			// NOT IMPLEMENT YET
		}
		return vp;
	}
}
