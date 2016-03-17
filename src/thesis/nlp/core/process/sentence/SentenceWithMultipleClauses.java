/**
 * 
 */
package thesis.nlp.core.process.sentence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import thesis.nlp.core.preprocess.SentencePreProcessing;
import thesis.nlp.core.process.SentenceConstants;
import thesis.nlp.models.Clause;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;

/**
 * @author lohuynh
 * @version 1.0
 * @Template : MAIN CLAUSE + ", " + Coordinating Conj + MAIN CLAUSE
 * @Ref: http://dictionary.cambridge.org/grammar/british-grammar/conjunctions
 * 
 */
public class SentenceWithMultipleClauses extends ClauseProcessing
		 {
	private List<CoreLabel> coordinatorList;
	private List<String> listOfClauses;
	private int compType = -1;
	private String subordinator = "";

	/**
	 * 
	 */
	public SentenceWithMultipleClauses() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public List<Clause> processSentence(String sentence)
	{
		List<Clause> clauses = new ArrayList<Clause>();
		List<String> multisentence = splitIntoMultipleSimpleSentenceBySemicolon(sentence);
		List<String> coor = new ArrayList<String>();
		for (String asentence : multisentence){
			Collections.addAll(coor, SentenceConstants.SUBORDINATORS_LIST);
			List<String> multiClauses = splitIntoMultipleSimpleSentence(asentence, coor);
			if (multiClauses.size() == 1)
			{
				String clause = multiClauses.get(0);
				clauses.add(processAClause(clause));
			}
			else
			{
				String subClauseStr = multiClauses.get(0);
				String mainClause = multiClauses.get(1);
				Clause subClause = processAClause(subClauseStr);
				if (subClause == null)
					clauses.add(processAClause(asentence));
				else
					clauses.add(subClause);
				clauses.add(processAClause(mainClause));
			}
		}
		return clauses;
	}
	private Clause processAClause(String str)
	{
		str = SentencePreProcessing.removeCommaAtTheEnd(str);
		str = SentencePreProcessing.removeCommaAtTheBegin(str);
		str= SentencePreProcessing.removeDotAtTheEnd(str);
		Clause aclause = processClause(str);
		return aclause;
	}
	private String removeSubordinator(String sentence)
	{
		List<String> tokens = convertStringIntoTokens(sentence);
		tokens.remove(0);
		String str = Sentence.listToString(tokens);
		return str;
	}
	public String getAdverbClause(String sentence)
	{
		String advcl = "";
		
		return advcl;
	}

	/**
	 * This function return the subordinator
	 * 
	 * @param sentence
	 * @return String
	 */

	public String findSubordinator(String sentence) {
		// TODO Auto-generated method stub
		for (int i = 0; i < SentenceConstants.SUBORDINATORS_LIST.length; i++) {
			if (sentence.toUpperCase().matches(".*\\b" + SentenceConstants.SUBORDINATORS_LIST[i].toUpperCase() + "\\b.*")) {
				subordinator = SentenceConstants.SUBORDINATORS_LIST[i].toString();
				// check whether subordinator at the begin of sentence
				if (sentence.toUpperCase().indexOf(subordinator.toUpperCase()) != 0) {
					if (sentence.toUpperCase().contains(", " + subordinator.toUpperCase())) {
						compType = SentenceConstants.COMPLEX_SENTENCE_TYPE2;
						break;
					}

				} else {
					compType = SentenceConstants.COMPLEX_SENTENCE_DEFAULTTYPE;
					break;
				}
			}

		}
		return subordinator;
	}

	/**
	 * This function return the beginIndex of subordinator position in sentence
	 * 
	 * @param sentence
	 * @return int
	 */
	public int beginIndexOfSubordinatorPosition(String sentence, String subordinator) {
		int beginIndex = 0;
		if (sentence.toUpperCase().contains(", " + subordinator.toUpperCase())) {
			beginIndex = sentence.toUpperCase().indexOf(subordinator.toUpperCase());
		}
		return beginIndex;
	}

	/**
	 * This function return the endIndex of subordinator position in sentence
	 * 
	 * @param sentence
	 * @return int
	 */
	public int endIndexOfSubordinatorPosition(String sentence, String subordinator) {
		int endIndex = subordinator.length();
		if (sentence.toUpperCase().contains(", " + subordinator.toUpperCase())) {
			endIndex = sentence.toUpperCase().indexOf(subordinator.toUpperCase()) + subordinator.length();
		}
		return endIndex;
	}

	/**
	 * This function return the list of coordinator with position
	 * 
	 * @author lohuynh
	 * @param sentence
	 * @return List<CoreLabel>
	 */
	public List<CoreLabel> findCoordinatorList(String sentence) {
		// TODO Auto-generated method stub
//		parseTree(sentence);
		this.coordinatorList = new ArrayList<CoreLabel>();
		CoreLabel cl;
		String aTaggedWordTag;
		for (int i = 0; i < taggedWords.size(); i++) {
			if ((aTaggedWordTag = taggedWords.get(i).tag()).equals("CC") && (taggedWords.get(i).word().toUpperCase().equals("BUT"))) {
				if (taggedWords.get(i - 1).word().equals(",")) {
					cl = new CoreLabel();
					cl.setWord(taggedWords.get(i).word());
					cl.setTag(aTaggedWordTag);
					cl.setIndex(taggedWords.get(i).index());
					cl.setBeginPosition(taggedWords.get(i).beginPosition());
					cl.setEndPosition(taggedWords.get(i).endPosition());
					coordinatorList.add(cl);
					break;
				}
			}
		}
		return this.coordinatorList;
	}
//	
	private List<String> splitIntoMultipleSimpleSentence(String sentence, List<String> coordinator)
	{
		List<String> clauses = new ArrayList<String>();
		List<String> tokens = convertStringIntoTokens(sentence);
		if (coordinator.contains(tokens.get(0).trim().toLowerCase()))
		{
			String[] strs = sentence.split(",");
			clauses.add(removeSubordinator(strs[0].trim())); // remove subordinator from the first SUbordinator Clause;
			String second = "";
			if (strs.length > 1)
			{
				for (int i = 1; i <strs.length; i++)
				{
					second = second.concat(strs[i]);
				}
				clauses.add(second.trim());
			}
		}
		else
			clauses.add(sentence);
		return clauses;
		
	}

	private static List<String> convertStringIntoTokens(String str)
	{
		List<String> tokens = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(str);
		 while (st.hasMoreTokens()) {
             tokens.add(st.nextToken());
         }
		 return tokens;
	}


	/**
	 * This function split ConjunctionSentence into multiple SimpleSentence
	 * based on coordinator
	 * 
	 * @author lohuynh
	 * @param coordinatorList
	 * @param sentence
	 * @return List<String>
	 */
	public List<String> splitIntoMultipleSimpleSentence(
			List<CoreLabel> coordinatorList, String sentence) {
		// TODO Auto-generated method stub
		listOfClauses = new ArrayList<String>();
		int beginIndex = 0;
		int endIndex = 0;
		System.out.println(coordinatorList.size());
		for (int i = 0; i <= coordinatorList.size(); i++) {
			if (i == 0) {
				endIndex = coordinatorList.get(i).beginPosition();
				beginIndex = 0;
			} else if (i == coordinatorList.size()) {
				endIndex = sentence.length();
				beginIndex = coordinatorList.get(i - 1).endPosition();
			} else {
				endIndex = coordinatorList.get(i).beginPosition();
				beginIndex = coordinatorList.get(i - 1).endPosition();
			}
			listOfClauses.add(sentence.substring(beginIndex, endIndex).trim());
		}
		return listOfClauses;
	}

	public List<String> splitIntoMultipleSimpleSentenceBySemicolon(
			String sentence) {
		listOfClauses = new ArrayList<String>();
		String[] strs = sentence.split(";"); // if all sentences are seperated by semicolon
		for (int i = 0; i < strs.length; i++) {
			listOfClauses.add(strs[i].toString().trim());
		}
		return listOfClauses;
	}
	
	public List<String> splitIntoMultipleSimpleSentenceByComma(
			String sentence) {
		listOfClauses = new ArrayList<String>();
		String[] strs = sentence.split(","); // if all sentences are seperated by semicolon
		for (int i = 0; i < strs.length; i++) {
			listOfClauses.add(strs[i].toString().trim());
		}
		return listOfClauses;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String sentence = "Bill refuses to eat peas, but she eats banana";
		SentenceWithMultipleClauses conjs = new SentenceWithMultipleClauses();
//		List<CoreLabel> list = conjs.findCoordinatorList(sentence);
		List<String> tokens = convertStringIntoTokens(sentence);
		for (String str : tokens)
		{
			System.out.println(str);
		}
		System.out.println("@@@@@@@@@");
		List myList = new ArrayList(); 
		Collections.addAll(myList, SentenceConstants.SUBORDINATORS_LIST); 
		List<String> strs = conjs.splitIntoMultipleSimpleSentence(sentence, myList);
		for(String str: strs)
		{
			System.out.println(str);
		}
	}
}
