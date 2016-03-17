/**
 * 
 */
package thesis.nlp.core.process;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

/**
 * @author lohuynh
 *
 */
public final class SentenceConstants {
	public static final String FILE_STEMMEDWORD = "data\\StemmingVerb.txt";
	public static final String PARSER_MODEL = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
	public static final LexicalizedParser lp = LexicalizedParser.loadModel(PARSER_MODEL, "-makeCopulaHead");
//	public static final String[] SUBORDINATORS_LIST = { "after", "although", "as",
//			"as if", "as long as", "as soon as", "as though", "because",
//			"even if", "even though", "if", "in case", "since", "so that",
//			"that", "though", "till", "until", "unless", "whatever", "when",
//			"whenever", "where", "wherever", "whether", "which", "while",
//			"who", "whom", "whoever", "whomever", "why" };
	public static final String[] SUBORDINATORS_LIST_2= { "after", "although", "as",
		"as if", "as long as", "as soon as", "as though",
		"even if", "even though", "if", "in case", "since", "so that",
		"that", "till", "until", "unless", "whatever", "when",
		"whenever", "whereas", "wherever", "while"};
	public static final String[] SUBORDINATORS_LIST= {"although",
		"as if", "as long as", "as soon as", "as though",
		"even if", "even though", "if", "in case", "so that",
		"whatever", "when",
		"whenever", "whereas", "wherever", "after"};
	public static final int COMPLEX_SENTENCE_TYPE2 = 2;
	public static final int COMPLEX_SENTENCE_DEFAULTTYPE = 1;
	public static final int COMPLEX_SENTENCE_TYPE1 = 1;
	public static final String VERB_BASE_FORM = "base";
	public static final String VERB_PAST_TENSE = "past";
	public static final String VERB_PRESENT_TENSE = "present";
	public static final String VERB_PP_TENSE = "past paticiple";
	public static final String VERB_GERUND = "gerund";
	public static final String[] COPULAR_VERBS = {"be", "seem", "stay", "feel", "look", "smell","taste"}; // https://en.wikipedia.org/wiki/List_of_English_copulae
	public static final String VERBVOICE_PASSIVE = "passive";
	public static final String VERBVOICE_ACTIVE = "active";
	public static final int SINGULAR_FORM = 0;
	public static final int PLURAL_FORM = 1;
	public static final int TRANSITIVE_VERB = 0;
	public static final int INTRANSITIVE_VERB = 1;
	public static final int DITRANSITIVE_VERB = 2;
	public static final int COPULAR_VERB = 20; // copular Verb
	public static final int SUBJVOICE_PASSIVE = 1;
	public static final int SUBJVOICE_ACTIVE = 0;
	public static final int VERB_STATE_POS = 0;
	public static final int VERB_STATE_NEG = 1;
	public static final int TYPE_SBAR_WH = 0;
	public static final int TYPE_SBAR_TOINF = 1;
	public static final int VERB_TYPE_10 = 10;
	public static final int VERB_TYPE_09 = 9;
	public static final int VERB_TYPE_08 = 8;
	public static final int VERB_TYPE_07 = 7;
	public static final int VERB_TYPE_06 = 6;
	public static final int VERB_TYPE_05 = 5;
	public static final int VERB_TYPE_04 = 4;
	public static final int VERB_TYPE_03 = 3;
	public static final int VERB_TYPE_02 = 2;
	public static final int VERB_TYPE_01 = 1;
	public static final int VERB_TYPE_11 = 11;
	public static final int VERB_TYPE_12 = 12; 
	public static final int VERB_TYPE_13 = 13; 
	public static final int VERB_TYPE_14 = 14;
	public static final int VERB_TYPE_15 = 15;

}
