/**
 * 
 */
package thesis.nlp.core.process.core.verb;

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
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author lohuynh
 *
 */
public class IntransitiveVerbPhraseProcessing {
	private IntransitiveVerbPhraseProcessing(){}

	/**
	 * This function process intransitive Verb (Active Voice): Subj + Verb No Passive Voice
	 * 
	 * @param verb
	 * @param tdls
	 * @return
	 */
	public static VerbPhrase processVerbType1(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb originalVerb, int patternType) {
		VerbPhrase vp = null;
		String aux = VerbPhraseProcessing.findAuxOfVerb(originalVerb, tdls);
		int posNeg = VerbPhraseProcessing.getStatePosNeg(originalVerb, tdls);
		int govIdx = originalVerb.getVerbIdx();
		String verbVoice = VerbProcessing.voiceOfVerb(tdls, govIdx);
		if (verbVoice == SentenceConstants.VERBVOICE_ACTIVE) {
			TypedDependency dobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DOBJ, "", govIdx);
			TypedDependency iobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.IOBJ, "", govIdx);
			TypedDependency prtTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.COMPOUND_PRT, "", govIdx);
			Verb verb = new Verb();
			if ((dobjTdep == null) && (iobjTdep == null) && (prtTdep == null)) {
				String tagWord = taggedWords.get(govIdx).tag();
				verb.setVerbName(originalVerb.getVerbName());
				verb.setVerbIdx(govIdx);
				verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
				verb.setVerbVoice(verbVoice);
				verb.setVerbType(SentenceConstants.VERB_TYPE_01);
				verb.setStemmedVerb(originalVerb.getStemmedVerb());
				DirectObject dobj = null;
				IndirectObject iobj = null;
				Complement comp = ComplementProcessing.getInstance().processComplement(tdls, taggedWords, verb);
				List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, "");
				vp = new VerbPhrase(aux, posNeg, "", patternType, verb, iobj, dobj, comp, pps);
			}
		}
		// no passive for this case
		return vp;
	}

	/***
	 * Type 2 : Intransitive Verb + particles
	 * 
	 * @param tdls
	 * @param predicateTree
	 * @param taggedWords
	 * @param originalVerb
	 * @param patternType
	 * @return
	 */
	public static VerbPhrase processVerbType2(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb originalVerb, int patternType,
			String particle) {
		VerbPhrase vp = null;
		String aux = VerbPhraseProcessing.findAuxOfVerb(originalVerb, tdls);
		int posNeg = VerbPhraseProcessing.getStatePosNeg(originalVerb, tdls);
		String verbVoice = VerbProcessing.voiceOfVerb(tdls, originalVerb.getVerbIdx());
		if (verbVoice == SentenceConstants.VERBVOICE_ACTIVE) {
			// active sentence
			TypedDependency dobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DOBJ, "",
					originalVerb.getVerbIdx());
			TypedDependency iobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.IOBJ, "",
					originalVerb.getVerbIdx());
			TypedDependency prtTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PRT, "",
					originalVerb.getVerbIdx());
			Verb verb = new Verb();
			if ((dobjTdep == null) && (iobjTdep == null)) {
				if ((prtTdep != null) && (prtTdep.dep().originalText().equals(particle))) {
					String tagWord = prtTdep.gov().tag();
					verb.setVerbName(originalVerb.getVerbName());
					verb.setVerbIdx(originalVerb.getVerbIdx());
					verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
					verb.setVerbVoice(verbVoice);
					verb.setVerbType(SentenceConstants.VERB_TYPE_02);
					verb.setStemmedVerb(originalVerb.getStemmedVerb());
					DirectObject dobj = null;
					IndirectObject iobj = null;
					Complement comp = ComplementProcessing.getInstance().processComplement(tdls, taggedWords, verb);
					List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, particle);
					vp = new VerbPhrase(aux, posNeg, particle, patternType, verb, iobj, dobj, comp, pps);
				}
				else
				{
					TypedDependency prep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PREP, "",
							originalVerb.getVerbIdx());
					if (prep != null && (prep.dep().originalText().equals(particle)))
					{
						// Exp: It goes it all that this country stands for
						String tagWord = prep .gov().tag();
						verb.setVerbName(originalVerb.getVerbName());
						verb.setVerbIdx(originalVerb.getVerbIdx());
						verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
						verb.setVerbVoice(verbVoice);
						verb.setVerbType(SentenceConstants.VERB_TYPE_02);
						verb.setStemmedVerb(originalVerb.getStemmedVerb());
						DirectObject dobj = null;
						IndirectObject iobj = null;
						Complement comp = ComplementProcessing.getInstance().processComplement(tdls, taggedWords, verb);
						List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, particle);
						vp = new VerbPhrase(aux, posNeg, particle, patternType, verb, iobj, dobj, comp, pps);
					}
						
				}

			}
		}
		// no passive for this case
		return vp;
	}

}
