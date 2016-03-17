/**
 * 
 */
package thesis.nlp.core.process.sentence;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import thesis.nlp.core.process.SentenceConstants;
import thesis.nlp.core.process.core.GeneralProcessing;
import thesis.nlp.core.process.core.PredicateProcessing;
import thesis.nlp.core.process.core.subject.SubjectProcessing;
import thesis.nlp.models.Clause;
import thesis.nlp.models.Predicate;
import thesis.nlp.models.Subject;
import thesis.nlp.models.Tuple;
import thesis.nlp.models.VerbPhrase;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;

/**
 * @author lohuynh
 *
 */
public class ClauseProcessing {
	public TokenizerFactory<CoreLabel> tokenfactory;
	public Tokenizer<CoreLabel> tok;
	public Tree parse = null;
	public static List<CoreLabel> rawWords;
	public List<CoreLabel> taggedWords;
	public List<TypedDependency> tdls;
	
	public List<Tuple> tuples = new ArrayList<Tuple>();
	public Clause processClause(String sentence) {
		parseTree(sentence);
		SubjectProcessing subjp = new SubjectProcessing();
		Subject subject = subjp.processSubjByTDep(tdls, taggedWords);
		PredicateProcessing predicatep = new PredicateProcessing();
		Predicate predicate = predicatep.processPredicate(tdls, taggedWords, subject);
		if( subject != null || predicate != null)
			return new Clause(subject, predicate);
		else
			return null;
	}

	/**
	 * This function parse a sentence into PennTree and GrammaticalStructure
	 * 
	 * @param sentence
	 */
	public void parseTree(String sentence) {
		tokenfactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
		tok = tokenfactory.getTokenizer(new StringReader(sentence));
		rawWords = tok.tokenize();
		// parse = SentenceConstants.lp.parseTree(rawWords);
		parse = SentenceConstants.lp.parseTree(rawWords);
		taggedWords = parse.taggedLabeledYield();
		TreebankLanguagePack tlp = SentenceConstants.lp.treebankLanguagePack(); // PennTreebankLanguagePack

		// for English
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
		// tdl = gs.typedDependenciesCCprocessed();
		tdls = (List<TypedDependency>) gs.typedDependenciesCollapsed();
//		TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
//		tp.printTree(parse);
		GeneralProcessing.processRawWordsAndTaggedWords(rawWords, taggedWords);
//		for (TypedDependency tdl : tdls) {
//			System.out.println(tdl.toString() + " ; " + tdl.reln().getLongName() + " ; " + tdl.reln().getShortName() + " * "
//					+ tdl.reln().getSpecific() + " ; " + tdl.reln().getParent());
//		}
		// Test output of Semantic Graph:
//		printSemanticGraph(sentence);
	}

	public void printSemanticGraph(String text) {
		// create an empty Annotation just with desired text
		Annotation document = new Annotation(text);

		// run all Annotators on this text
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		pipeline.annotate(document);
		// for each sentence you can get a list of collapsed dependencies as follows
		// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types   
		 List < CoreMap > sentences = document.get(SentencesAnnotation.class);
		 for (CoreMap sentence: sentences) {
		 	// traversing the words in the current sentence       // a CoreLabel is a CoreMap with additional token-specific methods     
//		 	for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
//		 		// this is the text of the token       
//		 		String word = token.get(TextAnnotation.class); // this is the POS tag of the token       
//		 		String pos = token.get(PartOfSpeechAnnotation.class); // this is the NER label of the token        
//		 		String ne = token.get(NamedEntityTagAnnotation.class);
//		 	} // this is the parse tree of the current sentence     
//		 	Tree tree = sentence.get(TreeAnnotation.class); // this is the Stanford dependency graph of the current sentence    
		 	SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
		 	int egde = dependencies.edgeCount();
		 	System.out.println("EDGE: " +egde);
		 	Iterator<SemanticGraphEdge> itr = dependencies.edgeIterable().iterator();
		 	while(itr.hasNext())
		 	{
		 		System.out.println("SematicGraphEdge: " + itr.next());
		 	}
		 	
		 	System.out.println("Sematic Graph:  " + dependencies.toFormattedString());
		 } // This is the coreference link graph     // Each chain stores a set of mentions that link to each other,     // along with a method for getting the most representative mention     // Both sentence and token offsets start at 1! 
	 }
	


}
