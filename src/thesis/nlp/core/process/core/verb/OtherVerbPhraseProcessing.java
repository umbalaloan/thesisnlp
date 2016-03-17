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
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author lohuynh
 *
 */
public class OtherVerbPhraseProcessing {
	private OtherVerbPhraseProcessing() {
	}

	/**
	 * Type 13: Reporting Verb + that + Clause
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
	public static VerbPhrase processVerbType13(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb originalVerb, int patternType) {
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
			TypedDependency ccompTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.CCOMP, "",
					originalVerb.getVerbIdx());

			if ((dobjTdep == null) && (iobjTdep == null) && (ccompTdep != null)) {
				String tagWord = ccompTdep.gov().tag();
				verb.setVerbName(originalVerb.getVerbName());
				verb.setVerbIdx(govIdx);
				verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
				verb.setVerbVoice(verbVoice);
				verb.setVerbType(SentenceConstants.COPULAR_VERB);
				verb.setStemmedVerb(originalVerb.getStemmedVerb() + " that ");
				DirectObject dobj = DirectObjectProcessing.getInstance().processDOBJ(tdls, taggedWords, verb, "");
				IndirectObject iobj = null;
				Complement comp = null;
				List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, "");
				vp = new VerbPhrase(aux, posNeg, "", patternType, verb, iobj, dobj, comp, pps);
			}
			else {
				TypedDependency advclTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.ADVCL, "",
						originalVerb.getVerbIdx());
				if ((dobjTdep == null) && (iobjTdep == null) && (advclTdep != null)){
					String tagWord = advclTdep.gov().tag();
					verb.setVerbName(originalVerb.getVerbName());
					verb.setVerbIdx(govIdx);
					verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
					verb.setVerbVoice(verbVoice);
					verb.setVerbType(SentenceConstants.COPULAR_VERB);
					verb.setStemmedVerb(originalVerb.getStemmedVerb() + " that ");
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
