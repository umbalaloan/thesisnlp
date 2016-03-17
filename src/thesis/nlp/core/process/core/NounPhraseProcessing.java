/**
 * 
 */
package thesis.nlp.core.process.core;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import thesis.nlp.models.NounPhrase;
import thesis.nlp.models.PrepositionalPhrase;
import thesis.nlp.models.SBAR;
import thesis.nlp.models.SingleNP;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author lohuynh
 *
 */
public class NounPhraseProcessing {
	private static NounPhraseProcessing instance = null;

	/**
	 * 
	 */
	private NounPhraseProcessing() {
	}

	public static NounPhraseProcessing getInstance() {
		if (instance == null)
			return instance = new NounPhraseProcessing();
		else
			return instance;
	}

	/**
	 * This function process NounPhrase based on TypedDependency if you can define the list of last Noun using typedDependency
	 * 
	 * @param treeNP
	 * @param rawWords
	 * @param tdeps
	 * @param taggedWords
	 * @param lastNounInComps
	 *            . The headNoun of Compound Noun Phrase can define via typedDependency like dobj
	 * @return NounPhrase
	 */
	public NounPhrase processNounPhraseByTDep(List<TypedDependency> tdls, List<CoreLabel> taggedWords, List<CoreLabel> lastNounInComps) {
		if (lastNounInComps.size() == 0) {
			return null;
		}
		NounPhrase np = null;
		String conjunction = "";
		Set<SingleNP> singleNPs = listSingleNPsFromTDep(tdls, taggedWords, lastNounInComps);
		Set<PrepositionalPhrase> prepNPs = new LinkedHashSet<PrepositionalPhrase>();
		for (SingleNP singleNP : singleNPs) {
			prepNPs = PrepositionalPhraseProcessing.getInstance().processPPsOfNP(tdls, taggedWords, singleNP);
//			if (prepNP != null)
//				break;
		}
		SBAR sbarNP = null;
//		for (CoreLabel lastNoun : lastNounInComps) {
//			sbarNP = SbarProcessing.getInstance().processSbarOfNPByTdep(tdls, taggedWords, lastNoun);
//			if (sbarNP != null)
//				break;
//		}
		for (SingleNP singleNP : singleNPs){
			sbarNP = SbarProcessing.getInstance().processSbarOfNPByTdep(tdls, taggedWords, singleNP);
			if (sbarNP != null)
			{
				break;
			}
		}
		if (singleNPs.size() > 0 || sbarNP != null)
			np = new NounPhrase(singleNPs, conjunction, sbarNP, prepNPs);
		return np;
	}

	/**
	 * This function return the list of Single Noun Phrase from TypedDependency
	 * 
	 * @param tdeps
	 *            . The typed Dependency of a sentence
	 * @param lastNounInComps
	 *            . The list CoreLabel of last word in compound Noun
	 * @return . The Set<SingleNP>
	 */
	public Set<SingleNP> listSingleNPsFromTDep(List<TypedDependency> tdls, List<CoreLabel> taggedWords, List<CoreLabel> lastNounInComps) {
		Set<SingleNP> singleNPs = new LinkedHashSet<SingleNP>();
		for (CoreLabel lastNounInComp : lastNounInComps) {
			SingleNP singleNP = SingleNPProcessing.getInstance().processSingleNPByTdep(tdls, taggedWords, lastNounInComp);
			singleNPs.add(singleNP);
		}
		return singleNPs;
	}

}
