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
public class ToInfinitiveVerbProcessing {
	private ToInfinitiveVerbProcessing(){}
	/**
	 * Type 8: Verb + to-infinitive Verb
	 * 
	 * @Exp: She decided to set out at once.
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
	public static  VerbPhrase processVerbType8(List<TypedDependency> tdls,
			List<CoreLabel> taggedWords, Verb originalVerb, int patternType) {
		VerbPhrase vp = null;
		String aux = VerbPhraseProcessing.findAuxOfVerb(originalVerb, tdls);
		int posNeg = VerbPhraseProcessing.getStatePosNeg(originalVerb, tdls);
		int govIdx = originalVerb.getVerbIdx();

		String verbVoice = VerbProcessing.voiceOfVerb(tdls, originalVerb.getVerbIdx());
		Verb verb = new Verb();
		if (verbVoice == SentenceConstants.VERBVOICE_ACTIVE) {
			/*
			 * exp : I want to go
			 */
//			TypedDependency dobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DOBJ, "", govIdx);
//			TypedDependency iobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.IOBJ, "", govIdx);
			TypedDependency xcompTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.XCOMP, "", govIdx);
			TypedDependency auxTdep = null;
			boolean isToInfinitive = false;
			if (xcompTdep != null) {
				// mark(go-4, to-3)
				// xcomp(want-2, go-4)
				int infVerbIdx = xcompTdep.dep().index();
//				TypedDependency auxTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.AUX, "", infVerbIdx);
				auxTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.AUX, "", infVerbIdx);
				if ((auxTdep != null) && (auxTdep.dep().originalText().toUpperCase().equals("TO"))) {
					isToInfinitive = true;
				}
			}
			if (isToInfinitive)
			{
				Verb toinfVerb = new Verb();
				toinfVerb.setVerbName(auxTdep.gov().originalText());
				toinfVerb.setVerbIdx(auxTdep.gov().index());
				toinfVerb.setVerbTense(SentenceConstants.VERB_PRESENT_TENSE);
				toinfVerb.setStemmedVerb(auxTdep.dep().originalText()); // bare Verb
				TypedDependency dobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DOBJ, "", toinfVerb.getVerbIdx());
				TypedDependency iobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.IOBJ, "", toinfVerb.getVerbIdx());
				if ((dobjTdep == null) && (iobjTdep == null)) {
					String tagWord = taggedWords.get(GeneralProcessing.convertWordIdxFromTDepToListCoreLabel(govIdx)).tag();
					verb.setVerbName(originalVerb.getVerbName());
					verb.setVerbIdx(govIdx);
					verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
					verb.setVerbVoice(verbVoice);
					// verb.setVerbType(SentenceConstants.TRANSITIVE_VERB);
	//				verb.setStemmedVerb(originalVerb.getStemmedVerb() + " + to do sth");
					verb.setStemmedVerb(originalVerb.getVerbName() + " to " + toinfVerb.getVerbName()); // verb + to inf
					
					DirectObject dobj = null;
					IndirectObject iobj = null;
	//				Complement comp = null;
					Complement comp = ComplementProcessing.getInstance().processComplement(tdls, taggedWords, toinfVerb);
					List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, "");
					vp = new VerbPhrase(aux, posNeg, "", patternType, verb, iobj, dobj, comp, pps);
					
				} else if ((dobjTdep != null) && (iobjTdep == null)) {
					String tagWord = taggedWords.get(GeneralProcessing.convertWordIdxFromTDepToListCoreLabel(govIdx)).tag();
					verb.setVerbName(originalVerb.getVerbName());
					verb.setVerbIdx(govIdx);
					verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
					verb.setVerbVoice(verbVoice);
					verb.setStemmedVerb(originalVerb.getVerbName() + " to " + toinfVerb.getVerbName()); // verb + to inf
					DirectObject dobj = DirectObjectProcessing.getInstance().processDOBJ(tdls, taggedWords, toinfVerb, "");
					IndirectObject iobj = null;
					Complement comp = null;
					List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, "");
					vp = new VerbPhrase(aux, posNeg, "", patternType, verb, iobj, dobj, comp, pps);
				}
			}
		}
		return vp;
	}

	/**
	 * Type 9: Verb + Dobj + to-infinitive Verb
	 * 
	 * @Exp: She helps me to do exercise
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
	public static  VerbPhrase processVerbType9(List<TypedDependency> tdls, 
			List<CoreLabel> taggedWords, Verb originalVerb, int patternType) {
		/*
		 * Example: The teacher instructed the students to line up in pairs. I've invited Jill to come to dinner on Saturday.
		 */
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
			TypedDependency xcompTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.XCOMP, "", govIdx);
			TypedDependency vmodTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.VMOD, "", govIdx);
			boolean isToInfinitive = false;
			int infVerbIdx = -1;
			if (xcompTdep != null) {
				// aux(line-7, to-6)
				// vmod(instructed-3, line-7)
				infVerbIdx = xcompTdep.dep().index();
			} else if (vmodTdep != null) {
				infVerbIdx = vmodTdep.dep().index();
			}

			if ((xcompTdep != null) || (vmodTdep != null)) {
				TypedDependency auxTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.AUX, "", infVerbIdx);
				if ((auxTdep != null) && (auxTdep.dep().originalText().toUpperCase().equals("TO"))) {
					isToInfinitive = true;
				}
			}
			if ((dobjTdep != null) && (iobjTdep == null) && (isToInfinitive)) {
				String tagWord = taggedWords.get(GeneralProcessing.convertWordIdxFromTDepToListCoreLabel(govIdx)).tag();
				verb.setVerbName(originalVerb.getVerbName());
				verb.setVerbIdx(govIdx);
				verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
				verb.setVerbVoice(verbVoice);
				verb.setVerbType(SentenceConstants.VERB_TYPE_09);
				verb.setStemmedVerb(originalVerb.getStemmedVerb() + " + s.o to do sth");
				DirectObject dobj = DirectObjectProcessing.getInstance().processDOBJ(tdls, taggedWords, verb, "");
				IndirectObject iobj = null;
				Complement comp = null;
				List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, "");
				vp = new VerbPhrase(aux, posNeg, "", patternType, verb, iobj, dobj, comp, pps);
			}
		} else {
			// for passive
			TypedDependency dobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DOBJ, "", govIdx);
			TypedDependency iobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.IOBJ, "", govIdx);
			TypedDependency xcompTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.XCOMP, "", govIdx);
			if ((dobjTdep == null) && (iobjTdep == null) && (xcompTdep != null)) {
				String tagWord = taggedWords.get(GeneralProcessing.convertWordIdxFromTDepToListCoreLabel(govIdx)).tag();
				verb.setVerbName(originalVerb.getVerbName());
				verb.setVerbIdx(govIdx);
				verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
				verb.setVerbVoice(verbVoice);
				// verb.setVerbType(SentenceConstants.TRANSITIVE_VERB);
				verb.setStemmedVerb(originalVerb.getStemmedVerb() + " + s.o to do sth");
				DirectObject dobj = null;
				IndirectObject iobj = null;
				Complement comp = ComplementProcessing.getInstance().processComplement(tdls, taggedWords, verb);
				List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, "");
				vp = new VerbPhrase(aux, posNeg, "", patternType, verb, iobj, dobj, comp, pps);
			}
			else
			{
				TypedDependency agentTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.AGENT, "", govIdx);
				if (agentTdep != null)
				{
					String tagWord = taggedWords.get(GeneralProcessing.convertWordIdxFromTDepToListCoreLabel(govIdx)).tag();
					verb.setVerbName(originalVerb.getVerbName());
					verb.setVerbIdx(govIdx);
					verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
					verb.setVerbVoice(verbVoice);
					// verb.setVerbType(SentenceConstants.TRANSITIVE_VERB);
					verb.setStemmedVerb(originalVerb.getStemmedVerb() + " + s.o to do sth");
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
