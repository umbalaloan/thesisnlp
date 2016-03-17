/**
 * 
 */
package thesis.nlp.core.process.core;

import java.util.List;

import thesis.nlp.core.process.sentence.ClauseProcessing;
import thesis.nlp.models.AdverbPhrase;
import thesis.nlp.models.AdverbialClause;
import thesis.nlp.models.Verb;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * @author lohuynh
 *
 */
public class AdverbClauseProcessing extends ClauseProcessing {
	/**
	 * 
	 */
	private static AdverbClauseProcessing instance = null;

	private AdverbClauseProcessing() {
		// TODO Auto-generated constructor stub
	}

	public static AdverbClauseProcessing getInstance() {
		if (instance == null)
			return new AdverbClauseProcessing();
		return instance;
	}

	public AdverbialClause processAdvClause(List<TypedDependency> tdls, List<CoreLabel> taggedWords, Verb verb) {
		AdverbPhrase advPhrase = AdverbPhraseProcessing.getInstance().processAdvPhrase(tdls, taggedWords, verb);
		if (advPhrase == null) {
			return null;
		} else
			return new AdverbialClause(advPhrase);
	}
}
