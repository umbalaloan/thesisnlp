/**
 * 
 */
package thesis.nlp.core.process;

/**
 * @author lohuynh
 *
 */
public final class StanfordTreeTypedDependConsts {
	public static final String APPOS = "appos";
	public static final String TMOD = "tmod"; // Temporal modifier
	public static final String COMPOUND = "compound";
	public static final String COMPOUND_PRT = "compound:prt";
	public static final String NSUBJ = "nsubj";
	public static final String NSUBJPASS = "nsubjpass";
	public static final String DET = "det";
	public static final String AMOD = "amod";
	public static final String ADVMOD = "advmod";
	public static final String COP = "cop";
	public static final String NMOD = "nmod";
	public static final String DEP = "dep"; // ~ with DEP in typedDependency
	public static final String CASE = "case"; // nmod(asked-2, for-3) = > case(asked-2, for-3)
	public static final String CC = "cc";
	public static final String CC_TREE = "CC";
	public static final String CONJ = "conj";
	public static final String SPECIFIC_AND = "and";
	public static final String SPECIFIC_OR = "or";
	public static final String SPECIFIC_AS = "as";
	public static final String SPECIFIC_BUT = "but";
	public static final String NNP = "NNP";
	public static final String NN = "NN";
	public static final String VP = "VP";
	public static final String VBP = "VBP";
	public static final String VBZ = "VBZ";
	public static final String NP = "NP";
	public static final String VB = "VB";
	public static final String VBG = "VBG";
	public static final String VBD = "VBD";
	public static final String VBN = "VBN";
	public static final String AUXPASS = "auxpass";
	public static final String AUX = "aux";
	public static final String MD = "MD";
	public static final String DOBJ = "dobj";
	public static final String IOBJ = "iobj";
	public static final String DT = "DT";
	public static final String JJR = "JJR";
	public static final String JJ = "JJ";
	public static final String NNS = "NNS";
	public static final String NNPS = "NNPS";
	public static final String PP = "PP";
	public static final String IN = "IN";
	public static final String WHNP = "WHNP"; // who, that, which
	public static final String WP = "WP"; // who
	public static final String WDT = "WDT"; // that, which
	public static final String WHADVP = "WHADVP"; // where
	public static final String WRB = "WRB"; // where
	public static final String SBAR = "SBAR";
	public static final String S = "S";
	public static final String ROOT = "root";
	public static final String PRT = "prt";
	public static final String NEG = "neg";
//	public static final String AMOD = "amod";
	public static final String ADVP = "ADVP";
	public static final String PRP = "PRP";
	public static final String ADJP = "ADJP";
	public static final String REF = "ref";
	public static final String AGENT = "agent";
	public static final String XCOMP = "xcomp";// xclausal complement
	public static final String UCP = "UCP" ;// unlike coordination Phrase
	public static final String TO = "TO";
	public static final String CD = "CD"; //  (NP (DT the) (CD 1,500) (NNS metres)))))))))))
	public static final String NUMMOD = "nummod"; // (NP (DT the) (CD 1,500) (NNS metres))))))))))) => nummod(metres-12, 1,500-11)
	public static final String ADVCL = "advcl"; // advcl(have-4, say-7) ; adverbial clause modifier
	public static final String MARK = "mark";
	public static final String VMOD = "vmod"; //vmod(unit-9, composed-10) ; verb modifier ; vmod ; null
	public static final String PREP = "prep";
	public static final String PREPC = "prepc";
	public static final String ACOMP = "acomp";
	public static final String CCOMP = "ccomp";
	public static final String POSS = "poss";
	public static final String RCMOD = "rcmod" ;// relative clause modifier
	public static final String AND = "and";
	public static final String OR = "or";
	public static final String NUM = "num";
	public static final String NUMBER = "number";
	public static final String PRECONJ = "preconj";
	public static final String NOUN_PHRASE = "nn";
	public static final String POBJ = "pobj";
	public static final String NPADVMOD = "npadvmod";
	public static final String DESPITE = "despite";
	
}
