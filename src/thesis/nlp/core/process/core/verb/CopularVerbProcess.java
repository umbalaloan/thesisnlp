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
public class CopularVerbProcess {
	private static CopularVerbProcess instance = new CopularVerbProcess();

	/**
	 * 
	 */
	private CopularVerbProcess() {
	}

	public static CopularVerbProcess getInstance() {
		return instance;
	}

	public static boolean isCopularVerb(String verb) {
		boolean isCopularVerb = false;
		for (int i = 0; i < SentenceConstants.COPULAR_VERBS.length; i++) {
			if (SentenceConstants.COPULAR_VERBS[i].toUpperCase().equals(verb.toUpperCase())) {

				isCopularVerb = true;

			}
		}
		return isCopularVerb;

	}

	public static VerbPhrase processVerbType3Copula(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb originalVerb, int patternType) {
		VerbPhrase vp = null;
		String aux = VerbPhraseProcessing.findAuxOfVerb(originalVerb, tdls);
		int posNeg = VerbPhraseProcessing.getStatePosNeg(originalVerb, tdls);
		if (posNeg == SentenceConstants.VERB_STATE_POS)
		{
			TypedDependency copTdep = GeneralProcessing.returnOneRelationByDepIdx(tdls, StanfordTreeTypedDependConsts.COP, "", originalVerb.getVerbIdx());
			if (copTdep!= null)
			{
				TypedDependency negTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NEG, "", copTdep.gov().index());
				if (negTdep != null)
					posNeg = SentenceConstants.VERB_STATE_NEG;
			}
		}
		int govIdx = originalVerb.getVerbIdx();
		TypedDependency dobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DOBJ, "",
				originalVerb.getVerbIdx());
		TypedDependency iobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.IOBJ, "",
				originalVerb.getVerbIdx());
		TypedDependency prtTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PRT, "", originalVerb.getVerbIdx());

		Verb verb = new Verb();
		if ((dobjTdep == null) && (iobjTdep == null) && (prtTdep == null)) {
			String tagWord = taggedWords.get(originalVerb.getVerbIdx() - 1).tag();
			verb.setVerbName(originalVerb.getVerbName());
			verb.setVerbIdx(govIdx);
			verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
			verb.setVerbVoice(VerbProcessing.voiceOfVerb(tdls, govIdx));
			verb.setVerbType(SentenceConstants.COPULAR_VERB);
			verb.setStemmedVerb(originalVerb.getStemmedVerb());
			DirectObject dobj = null;
			IndirectObject iobj = null;
			Complement comp = ComplementProcessing.getInstance().processComplement(tdls, taggedWords, verb);
			List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, "");
			vp = new VerbPhrase(aux, posNeg, "", patternType, verb, iobj, dobj, comp, pps);
		} else if ((dobjTdep != null) && (iobjTdep == null)) {
			// the case: I know who you are - dobj(are-5, who-3)
			// TypedDependency ccompTdep = GeneralProcessing.returnOneRelationByDepIdx(tdls, StanfordTreeTypedDependConsts.CCOMP, "", govIdx);
			String tagWord = dobjTdep.gov().tag();
			verb.setVerbName(originalVerb.getVerbName());
			verb.setVerbIdx(govIdx);
			verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
			verb.setVerbVoice(VerbProcessing.voiceOfVerb(tdls, govIdx));
			verb.setVerbType(SentenceConstants.COPULAR_VERB);
			verb.setStemmedVerb(originalVerb.getStemmedVerb());
			DirectObject dobj = DirectObjectProcessing.getInstance().processDOBJ(tdls, taggedWords, verb, "");
			IndirectObject iobj = null;
			Complement comp = null;
			List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, "");
			vp = new VerbPhrase(aux, posNeg,"", patternType, verb, iobj, dobj, comp, pps);
		}
		return vp;
	}

	/**
	 * 
	 * @param tdls
	 * @param subjTree
	 * @param predicateTree
	 * @param objOrCompTree
	 * @param taggedWords
	 * @param originalVerb
	 * @param patternType
	 * @return
	 */
	public static VerbPhrase processVerbType14(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb originalVerb, int patternType) {
		VerbPhrase vp = null;
		String aux = VerbPhraseProcessing.findAuxOfVerb(originalVerb, tdls);
		int posNeg = VerbPhraseProcessing.getStatePosNeg(originalVerb, tdls);
		int govIdx = originalVerb.getVerbIdx();
		String verbVoice = VerbProcessing.voiceOfVerb(tdls, govIdx);
		Verb verb = new Verb();
		if (verbVoice == SentenceConstants.VERBVOICE_ACTIVE) {
			TypedDependency dobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DOBJ, "",
					originalVerb.getVerbIdx());
			TypedDependency iobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.IOBJ, "",
					originalVerb.getVerbIdx());
			TypedDependency acompTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.ACOMP, "",
					originalVerb.getVerbIdx());
			TypedDependency prtTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PRT, "",
					originalVerb.getVerbIdx());

			if ((dobjTdep == null) && (iobjTdep == null) && (acompTdep != null) && (prtTdep == null)) {
				String tagWord = taggedWords.get(originalVerb.getVerbIdx() - 1).tag();
				verb.setVerbName(originalVerb.getVerbName());
				verb.setVerbIdx(govIdx);
				verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
				verb.setVerbVoice(verbVoice);
				verb.setVerbType(SentenceConstants.COPULAR_VERB);
				verb.setStemmedVerb(originalVerb.getStemmedVerb());
				DirectObject dobj = null;
				IndirectObject iobj = null;
				Complement comp = ComplementProcessing.getInstance().processComplement(tdls, taggedWords, verb);
				List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, "");
				vp = new VerbPhrase(aux, posNeg, "", patternType, verb, iobj, dobj, comp, pps);
			} else {
				TypedDependency prepTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PREP, "",
						originalVerb.getVerbIdx());
				if (prepTdep != null) {
					String tagWord = prepTdep.gov().tag();
					verb.setVerbName(originalVerb.getVerbName());
					verb.setVerbIdx(govIdx);
					verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
					verb.setVerbVoice(verbVoice);
					verb.setVerbType(SentenceConstants.COPULAR_VERB);
					verb.setStemmedVerb(originalVerb.getStemmedVerb());
					DirectObject dobj = null;
					IndirectObject iobj = null;
					Complement comp = ComplementProcessing.getInstance().processComplement(tdls, taggedWords, verb);
					List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, "");
					vp = new VerbPhrase(aux, posNeg, "", patternType, verb, iobj, dobj, comp, pps);
				}
			}
		}
		return vp;
	}
}
