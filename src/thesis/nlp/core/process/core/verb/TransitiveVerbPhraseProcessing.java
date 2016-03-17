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
public class TransitiveVerbPhraseProcessing {
	private TransitiveVerbPhraseProcessing() {
	}

	/**
	 * VP type 3 : Subj + verb + dobj
	 * 
	 * @param tdls
	 * @param predicateTree
	 * @param taggedWords
	 * @param originalVerb
	 * @param patternType
	 * @return
	 */
	public static VerbPhrase processVerbType3(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb originalVerb, int patternType) {
		VerbPhrase vp = null;
		if (CopularVerbProcess.isCopularVerb(originalVerb.getStemmedVerb())) {
			return CopularVerbProcess.processVerbType3Copula(tdls, taggedWords, originalVerb, patternType);

		}
		String aux = VerbPhraseProcessing.findAuxOfVerb(originalVerb, tdls);
		int posNeg = VerbPhraseProcessing.getStatePosNeg(originalVerb, tdls);
		int govIdx = originalVerb.getVerbIdx();
		String verbVoice = originalVerb.getVerbVoice();
		if (verbVoice == SentenceConstants.VERBVOICE_ACTIVE)
			verbVoice = VerbProcessing.voiceOfVerb(tdls, originalVerb.getVerbIdx());
		if (originalVerb.getVerbVoice() == SentenceConstants.VERBVOICE_PASSIVE)
		{
			/* for case: voice of sentence is identified before
			 *  (used in Subordinate Clause - 'processSbarOfNPForPassiveFormOfClause')
			 */
			verbVoice = originalVerb.getVerbVoice();
		}
		Verb verb = new Verb();
		if (verbVoice == SentenceConstants.VERBVOICE_ACTIVE) {
			TypedDependency dobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DOBJ, "", govIdx);
			TypedDependency iobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.IOBJ, "", govIdx);
			TypedDependency prtTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PRT, "", govIdx);
			if ((dobjTdep != null) && (iobjTdep == null) && (prtTdep == null)) {
				String tagWord = dobjTdep.gov().tag();
				verb.setVerbName(originalVerb.getVerbName());
				verb.setVerbIdx(govIdx);
				verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
				verb.setVerbVoice(verbVoice);
				verb.setVerbType(SentenceConstants.VERB_TYPE_03);
				verb.setStemmedVerb(originalVerb.getStemmedVerb());
				DirectObject dobj = DirectObjectProcessing.getInstance().processDOBJ(tdls, taggedWords, verb, "");
				IndirectObject iobj = null;
				Complement comp = null;
				List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, "");
				vp = new VerbPhrase(aux, posNeg, "", patternType, verb, iobj, dobj, comp, pps);
			}
			else {
				TypedDependency ccompTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.CCOMP, "", govIdx);
				if (ccompTdep == null)
				{
					ccompTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.XCOMP, "", govIdx);
					if (ccompTdep == null)
						ccompTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.ADVMOD, "", govIdx);
				}
				if ((dobjTdep == null) && (iobjTdep == null) && (ccompTdep != null)) {
					String tagWord = ccompTdep.gov().tag();
					verb.setVerbName(originalVerb.getVerbName());
					verb.setVerbIdx(originalVerb.getVerbIdx());
					verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
					verb.setVerbVoice(verbVoice);
					verb.setVerbType(SentenceConstants.VERB_TYPE_03);
					verb.setStemmedVerb(originalVerb.getStemmedVerb());
//					DirectObject dobj = DirectObjectProcessing.getInstance().processDOBJ(tdls, taggedWords, verb, "");
					DirectObject dobj = null;
					IndirectObject iobj = null;
					Complement comp = ComplementProcessing.getInstance().processComplement(tdls, taggedWords, verb);
					List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, "");
					vp = new VerbPhrase(aux, posNeg, "", patternType, verb, iobj, dobj, comp, pps);
				}
				else
				{
					/*
					 * Fix error the case: Crying the whole day, he went to Paris 
					 */
					
					TypedDependency tmodTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.TMOD, "", govIdx);
					if (tmodTdep != null)
					{
						String tagWord = tmodTdep.gov().tag();
						verb.setVerbName(originalVerb.getVerbName());
						verb.setVerbIdx(govIdx);
						verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
						verb.setVerbVoice(verbVoice);
						verb.setVerbType(SentenceConstants.VERB_TYPE_03);
						verb.setStemmedVerb(originalVerb.getStemmedVerb());
						DirectObject dobj = null;
						IndirectObject iobj = null;
						Complement comp = ComplementProcessing.getInstance().processComplement(tdls, taggedWords, verb);
						List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, "");
						vp = new VerbPhrase(aux, posNeg, "", patternType, verb, iobj, dobj, comp, pps);
					}
					
					/*
					 * Fix error the case: It has as few implementation as possible
					 * Exp: Java is concurrent, class-based, object-oriented, and specifically designed to have as few implementation dependencies as possible
					 */
					TypedDependency prep_AsTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PREP, StanfordTreeTypedDependConsts.SPECIFIC_AS, govIdx);
					if (prep_AsTdep != null)
					{
						String tagWord = prep_AsTdep.gov().tag();
						verb.setVerbName(originalVerb.getVerbName());
						verb.setVerbIdx(govIdx);
						verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
						verb.setVerbVoice(verbVoice);
						verb.setVerbType(SentenceConstants.VERB_TYPE_03);
						verb.setStemmedVerb(originalVerb.getStemmedVerb());
						DirectObject dobj = null;
						IndirectObject iobj = null;
						Complement comp = ComplementProcessing.getInstance().processComplement(tdls, taggedWords, verb);
						List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, "");
						vp = new VerbPhrase(aux, posNeg, "", patternType, verb, iobj, dobj, comp, pps);
					}
					
				}
					
				
			}
		} else {
			// for passive case : It is made by An
			TypedDependency dobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DOBJ, "", govIdx);
			TypedDependency iobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.IOBJ, "", govIdx);
			TypedDependency prtTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PRT, "", govIdx);
			if ((dobjTdep == null) && (iobjTdep == null) && (prtTdep == null)) {
//				 TypedDependency agentTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.AGENT,
//				 "", govIdx); // => nmod:agent(made-3,An-5)
				// if (agentTdep != null) {
				// TypedDependency caseTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.CASE, "", agentTdep
				// .dep().index()); // case(An-5, by-4) ;
				// if (caseTdep.dep().originalText().equals("by")) {
					String tagWord = taggedWords.get(originalVerb.getVerbIdx()).tag();
					verb.setVerbName(originalVerb.getVerbName());
					verb.setVerbIdx(originalVerb.getVerbIdx());
					verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
					verb.setVerbVoice(verbVoice);
					verb.setVerbType(SentenceConstants.VERB_TYPE_03);
					verb.setStemmedVerb(originalVerb.getStemmedVerb());
					DirectObject dobj = null;
					IndirectObject iobj = null;
					Complement comp = ComplementProcessing.getInstance().processComplement(tdls, taggedWords, verb);
					List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, "");
					vp = new VerbPhrase(aux, posNeg, "", patternType, verb, iobj, dobj, comp, pps);
				 
			}
			else if ((dobjTdep != null) && (iobjTdep == null) && (prtTdep == null)){
				// Exp: "Lancaster was nominated four times for Academy Awards and won once - for his work in "Elmer Gantry" in 1960."
				String tagWord = dobjTdep.gov().tag();
				verb.setVerbName(originalVerb.getVerbName());
				verb.setVerbIdx(govIdx);
				verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
				verb.setVerbVoice(verbVoice);
				verb.setVerbType(SentenceConstants.VERB_TYPE_03);
				verb.setStemmedVerb(originalVerb.getStemmedVerb());
				DirectObject dobj = DirectObjectProcessing.getInstance().processDOBJ(tdls, taggedWords, verb, "");
				IndirectObject iobj = null;
				Complement comp = null;
				List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, "");
				vp = new VerbPhrase(aux, posNeg, "", patternType, verb, iobj, dobj, comp, pps);
			}
		}
		return vp;
	}

	/**
	 * VP Type 4 = Subj + verb + particle + dobj
	 * 
	 * @param tdls
	 * @param predicateTree
	 * @param taggedWords
	 * @param originalVerb
	 * @param patternType
	 * @param particle
	 * @return
	 */
	public static VerbPhrase processVerbType4(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb originalVerb, int patternType,
			String particle) {
		/*
		 * Exp : This team is asked for support by him
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
			TypedDependency prtTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PRT, "", govIdx); // phrasal
																																		// verb
																																		// particle
			
			

			if ((dobjTdep != null) && (iobjTdep == null) && (prtTdep != null) && (prtTdep.dep().originalText().equals(particle))) {
				// Exp: "Jack picks up Ann"
				String tagWord = taggedWords.get(GeneralProcessing.convertWordIdxFromTDepToListCoreLabel(govIdx)).tag();
				verb.setVerbName(originalVerb.getVerbName());
				verb.setVerbIdx(govIdx);
				verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
				verb.setVerbVoice(verbVoice);
				verb.setVerbType(SentenceConstants.VERB_TYPE_04);
				verb.setStemmedVerb(originalVerb.getStemmedVerb());
				DirectObject dobj = DirectObjectProcessing.getInstance().processDOBJ(tdls, taggedWords, verb, particle);
				IndirectObject iobj = null;
				Complement comp = null;
				List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, particle);
				vp = new VerbPhrase(aux, posNeg, particle, patternType, verb, iobj, dobj, comp, pps);
				
			} else
				{
					TypedDependency prepTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PREP, particle, govIdx);
					if ((dobjTdep == null) && (iobjTdep == null) && (prepTdep != null)) {
					// for Sentence without direct object
	
					String tagWord = taggedWords.get(GeneralProcessing.convertWordIdxFromTDepToListCoreLabel(govIdx)).tag();
					// if (verbVoice == SentenceConstants.VERBVOICE_ACTIVE) {
					// Exp : I talked about science
					verb.setVerbName(originalVerb.getVerbName());
					verb.setVerbIdx(govIdx);
					verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
					verb.setVerbVoice(verbVoice);
					verb.setVerbType(SentenceConstants.VERB_TYPE_04);
					verb.setStemmedVerb(originalVerb.getStemmedVerb());
					DirectObject dobj = DirectObjectProcessing.getInstance().processDOBJ(tdls, taggedWords, verb, particle);
					IndirectObject iobj = null;
					Complement comp = null;
					List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, particle);
					vp = new VerbPhrase(aux, posNeg, particle, patternType, verb, iobj, dobj, comp, pps);
				// }
				}
			}
		} else {
			TypedDependency dobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DOBJ, "", govIdx);
			TypedDependency iobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.IOBJ, "", govIdx);
			TypedDependency prtTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PRT, "", govIdx);
			// for passive
			// Exp : An is picked up by Jack
			if ((dobjTdep == null) && (iobjTdep == null)){
				if ((prtTdep != null) && (prtTdep.dep().originalText().equals(particle))) {
			
					String tagWord = prtTdep.gov().tag();
					verb.setVerbName(originalVerb.getVerbName());
					verb.setVerbIdx(originalVerb.getVerbIdx());
					verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
					verb.setVerbVoice(verbVoice);
					verb.setVerbType(SentenceConstants.VERB_TYPE_04);
					verb.setStemmedVerb(originalVerb.getStemmedVerb());
					DirectObject dobj = null;
					IndirectObject iobj = null;
					Complement comp = ComplementProcessing.getInstance().processComplement(tdls, taggedWords, verb);
					List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, particle);
					vp = new VerbPhrase(aux, posNeg, particle, patternType, verb, iobj, dobj, comp, pps);
				}
				else
				{
					TypedDependency prepcTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PREPC, particle, govIdx);
					if ((prepcTdep != null) )
					{
						String tagWord = prepcTdep.gov().tag();
						verb.setVerbName(originalVerb.getVerbName());
						verb.setVerbIdx(originalVerb.getVerbIdx());
						verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
						verb.setVerbVoice(verbVoice);
						verb.setVerbType(SentenceConstants.VERB_TYPE_04);
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
		return vp;

	}

	/**
	 * 
	 * Type 5: Transitive Verb + dobj + term + e.g. "Mary asks Tom along to the party."
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
	public static VerbPhrase processVerbType5(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb originalVerb, int patternType,
			String particle) {
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
			TypedDependency prtTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PRT, "", govIdx);

			if ((iobjTdep == null) && (prtTdep != null) && (prtTdep.dep().originalText().equals(particle))) {
				if ((dobjTdep != null)) {
					// "Mary asks Tom along to the party"
					String tagWord = taggedWords.get(GeneralProcessing.convertWordIdxFromTDepToListCoreLabel(govIdx)).tag();
					verb.setVerbName(originalVerb.getVerbName());
					verb.setVerbIdx(govIdx);
					verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
					verb.setVerbVoice(verbVoice);
					verb.setVerbType(SentenceConstants.VERB_TYPE_05);
					verb.setStemmedVerb(originalVerb.getStemmedVerb() );
					DirectObject dobj = DirectObjectProcessing.getInstance().processDOBJ(tdls, taggedWords, verb, particle);
					IndirectObject iobj = null;
					Complement comp = null;
					List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, particle);
					vp = new VerbPhrase(aux, posNeg, particle, patternType, verb, iobj, dobj, comp, pps);
				}
			}
		} else {
			// for passive case
			// "Tom is asked along by Mary to the party."
			TypedDependency dobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DOBJ, "", govIdx);
			TypedDependency iobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.IOBJ, "", govIdx);
			TypedDependency prtTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PRT, "", govIdx);
			if (prtTdep != null)
			{
				if ((dobjTdep == null) && (iobjTdep == null) && (prtTdep.dep().originalText().equals(particle))) {
					// TypedDependency agentTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.NMOD,
					// StanfordTreeTypedDependConsts.AGENT, govIdx); // => nmod:agent(made-3,An-5)
					// if (agentTdep != null) {
					// TypedDependency caseTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.CASE, "", agentTdep
					// .dep().index()); // case(An-5, by-4) ;
					// // if (caseTdep.dep().originalText().equals("by")) {
					String tagWord = prtTdep.gov().tag();
					verb.setVerbName(originalVerb.getVerbName());
					verb.setVerbIdx(originalVerb.getVerbIdx());
					verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
					verb.setVerbVoice(VerbProcessing.voiceOfVerb(tdls, originalVerb.getVerbIdx()));
					verb.setVerbType(SentenceConstants.VERB_TYPE_05);
					verb.setStemmedVerb(originalVerb.getStemmedVerb());
					DirectObject dobj = DirectObjectProcessing.getInstance().processDOBJ(tdls, taggedWords, verb, particle);
					IndirectObject iobj = null;
					Complement comp = ComplementProcessing.getInstance().processComplement(tdls, taggedWords, verb);
					List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, particle);
					vp = new VerbPhrase(aux, posNeg, particle, patternType, verb, iobj, dobj, comp, pps);
					// }
					// }
				}
				else if ((iobjTdep == null) && (prtTdep.dep().originalText().equals(particle)))
				{
					String tagWord = prtTdep.gov().tag();
					verb.setVerbName(originalVerb.getVerbName());
					verb.setVerbIdx(originalVerb.getVerbIdx());
					verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
					verb.setVerbVoice(VerbProcessing.voiceOfVerb(tdls, originalVerb.getVerbIdx()));
					verb.setVerbType(SentenceConstants.VERB_TYPE_05);
					verb.setStemmedVerb(originalVerb.getStemmedVerb());
					DirectObject dobj = DirectObjectProcessing.getInstance().processDOBJ(tdls, taggedWords, verb, particle);
					IndirectObject iobj = null;
					Complement comp = ComplementProcessing.getInstance().processComplement(tdls, taggedWords, verb);
					List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, particle);
					vp = new VerbPhrase(aux, posNeg, particle, patternType, verb, iobj, dobj, comp, pps);
				}
			}
			else
			{
				TypedDependency prepcTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PREPC, particle, govIdx);
				if ((prepcTdep != null) && (dobjTdep == null)&& (iobjTdep == null))
				{
					String tagWord = prepcTdep.gov().tag();
					verb.setVerbName(originalVerb.getVerbName());
					verb.setVerbIdx(originalVerb.getVerbIdx());
					verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
					verb.setVerbVoice(VerbProcessing.voiceOfVerb(tdls, originalVerb.getVerbIdx()));
					verb.setVerbType(SentenceConstants.VERB_TYPE_05);
					verb.setStemmedVerb(originalVerb.getStemmedVerb());
					DirectObject dobj = DirectObjectProcessing.getInstance().processDOBJ(tdls, taggedWords, verb, particle);
					IndirectObject iobj = null;
					Complement comp = ComplementProcessing.getInstance().processComplement(tdls, taggedWords, verb);
					List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, particle);
					vp = new VerbPhrase(aux, posNeg, particle, patternType, verb, iobj, dobj, comp, pps);
				}
			}
		}

		return vp;
	}

	/**
	 * Type 6: Transitive Verb + dobj + term + iobj e.g. "Tom ask Bill for his help." "Bill is asked for his help by Tom."
	 * "Bill is asked by Tom for his help."
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
	public static VerbPhrase processVerbType6(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb originalVerb, int patternType,
			String particle) {
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

			if (dobjTdep != null) {
				if ((iobjTdep == null)) {
					// Exp: "Tom ask Bill for his help"
					int dobjDep = dobjTdep.dep().index();/* dobj(ask-2, Bill-3) poss(help-6, his-5) prep_for(Bill-3, help-6) */

					TypedDependency prepTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PREP, particle,
							dobjDep);
					if (prepTdep != null) {
						String tagWord = taggedWords.get(GeneralProcessing.convertWordIdxFromTDepToListCoreLabel(govIdx)).tag();
						verb.setVerbName(originalVerb.getVerbName());
						verb.setVerbIdx(govIdx);
						verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
						verb.setVerbVoice(verbVoice);
						verb.setVerbType(SentenceConstants.VERB_TYPE_06);
						verb.setStemmedVerb(originalVerb.getStemmedVerb());
						DirectObject dobj = DirectObjectProcessing.getInstance().processDOBJ(tdls, taggedWords, verb, particle);
						IndirectObject iobj = IndirectObjectProcessing.getInstance().processIOBJ(tdls, taggedWords, verb, particle);
						Complement comp = null;
						List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, particle);
						vp = new VerbPhrase(aux, posNeg, particle, patternType, verb, iobj, dobj, comp, pps);
					}
				} 
			}
		} else {
			// passive. "Bill is asked for his help by Tom"
			TypedDependency dobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.DOBJ, "", govIdx);
			TypedDependency iobjTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.IOBJ, "", govIdx);
			if ((dobjTdep == null) && (iobjTdep == null)) {
				TypedDependency agentTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.AGENT, "", govIdx); // =>
																																				// agent(asked-3,
																																				// Tom-8)
																																				// TypedDependency
																																				// prepTdep
																																				// =
																																				// null;
				// if (agentTdep != null) {
				// prepTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.PREP, particle, agentTdep.dep()
				// .index()); // => prep_for(Tom-5, help-8)
				// }
				// if (prepTdep != null) {
				if (agentTdep != null) {
//					TypedDependency caseTdep = GeneralProcessing.returnOneRelationByGovIdx(tdls, StanfordTreeTypedDependConsts.CASE, "", agentTdep
//							.dep().index()); // case(An-5, by-4) ;
					// if (caseTdep.dep().originalText().equals("by")) {
					String tagWord = taggedWords.get(GeneralProcessing.convertWordIdxFromTDepToListCoreLabel(govIdx)).tag();
					verb.setVerbName(originalVerb.getVerbName());
					verb.setVerbIdx(originalVerb.getVerbIdx());
					verb.setVerbTense(VerbProcessing.tenseVerb(tagWord));
					verb.setVerbVoice(verbVoice);
					verb.setVerbType(SentenceConstants.VERB_TYPE_06);
					verb.setStemmedVerb(originalVerb.getStemmedVerb());
					DirectObject dobj = DirectObjectProcessing.getInstance().processDOBJ(tdls, taggedWords, verb, particle);
					IndirectObject iobj = IndirectObjectProcessing.getInstance().processIOBJ(tdls, taggedWords, verb, particle);
					Complement comp = ComplementProcessing.getInstance().processComplement(tdls, taggedWords, verb);
					List<PrepositionalPhrase> pps = VerbPhraseProcessing.getInstance().listPrepPhrasesInVerbPhrase(tdls, taggedWords, verb, particle);
					vp = new VerbPhrase(aux, posNeg, particle, patternType, verb, iobj, dobj, comp, pps);
					// }
				}
				// }
			}
		}

		return vp;
	}
}
