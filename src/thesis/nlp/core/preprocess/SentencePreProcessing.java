/**
 * 
 */
package thesis.nlp.core.preprocess;


/**
 * @author lohuynh
 *
 */
public final class SentencePreProcessing{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * thesis.sentence.preprocess.SentencePreProcess#removeDotAtTheEnd(java.
	 * lang.String)
	 */
	public static String removeDotAtTheEnd(String sentence) {
		// TODO Auto-generated method stub
		return  sentence.replaceAll("\\.$", "").trim();
	}
	
	/* (non-Javadoc)
	 * @see preprocess.SentencePreProcess#removeCommaAtTheBeginAndEnd(java.lang.String)
	 */
	public static String removeCommaAtTheBegin(String sentence) {
		// TODO Auto-generated method stub
		return sentence.replaceAll("^\\,", "").trim();
	}
	
	public static String removeCommaAtTheEnd(String sentence){
		return sentence.replaceAll(",$", "").trim();
	}

	public static void main(String[] args) {
//		String str = ",If trim is used and it is not needed, removing version 2.0 it will make a program faster.";
		String str = "It was during his campaign for the United States Senate,";
		SentencePreProcessing spp = new SentencePreProcessing();
		System.out.println(spp.removeCommaAtTheEnd(str));
	}

	
}
