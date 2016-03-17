/**
 * 
 */
package thesis.nlp.core.process.sentence;

import java.util.ArrayList;
import java.util.List;

import thesis.nlp.models.AdverbialClause;
import thesis.nlp.models.Clause;
import thesis.nlp.models.Sentence;
import thesis.nlp.models.Tuple;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * @author lohuynh
 *
 */
public class SentenceProcessing extends ClauseProcessing {

	public List<Tuple> tuplesInSentence = new ArrayList<Tuple>();
	private SentenceWithMultipleClauses conjsen = new SentenceWithMultipleClauses();
	
	public Sentence processSentence(String sentence) {
		List<Clause> clauses = new ArrayList<Clause>();
		AdverbialClause advClause = null;
		List<String> conjunctionList = new ArrayList<String>();
//		sentence = sentence.replace("\"", "'");
		clauses = conjsen.processSentence(sentence);
		return new Sentence(advClause, clauses, conjunctionList);
	}


	public boolean checkCoordinator(String sentence) {
		boolean check = true;
		List<CoreLabel> cor = conjsen.findCoordinatorList(sentence);
		if (cor.size() == 0) {
			check = false;
		}
		return check;
	}

	public static boolean checkSemicolon(String sentence) {
		boolean check = true;
		if (!sentence.contains(",")) {
			check = false;
		}
		return check;
	}
}
