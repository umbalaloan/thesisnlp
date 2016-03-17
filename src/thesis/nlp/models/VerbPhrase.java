/**
 * 
 */
package thesis.nlp.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.xalan.xsltc.compiler.util.StringType;

import thesis.nlp.core.process.SentenceConstants;
import thesis.nlp.util.StringProcessUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author lohuynh
 *
 */
public class VerbPhrase {
	
	private String aux = "";
	private int posNeg = -1;
	private String partical = "";
	private int patternType = -1;
	private Verb verb = null;
	private IndirectObject indirectObj = null;
	private DirectObject directObj = null;
	private Complement complement = null;
	private List<PrepositionalPhrase> listPrepPhrase = new ArrayList<PrepositionalPhrase>();
	
	

public VerbPhrase(String aux, int posNeg, String partical, int patternType,
			Verb verb, IndirectObject indirectObj, DirectObject directObj,
			Complement complement, List<PrepositionalPhrase> listPrepPhrase) {
		super();
		this.aux = aux;
		this.posNeg = posNeg;
		this.partical = partical;
		this.patternType = patternType;
		this.verb = verb;
		this.indirectObj = indirectObj;
		this.directObj = directObj;
		this.complement = complement;
		this.listPrepPhrase = listPrepPhrase;
	}



//	/**
//	 * @param aux
//	 * @param posNeg
//	 * @param partical
//	 * @param patternType
//	 * @param verb
//	 * @param indirectObjList
//	 * @param directObjList
//	 * @param complement
//	 */
//	public VerbPhrase(String aux, int posNeg, String partical, int patternType,
//			Verb verb, IndirectObject indirectObjList,
//			DirectObject directObjList, Complement complement) {
//		super();
//		this.aux = aux;
//		this.posNeg = posNeg;
//		this.partical = partical;
//		this.patternType = patternType;
//		this.verb = verb;
//		this.indirectObj = indirectObjList;
//		this.directObj = directObjList;
//		this.complement = complement;
//	}
//
//	/**
//	 * @return the aux
//	 */
//	public String getAux() {
//		return this.aux;
//	}
//
//	/**
//	 * @param aux
//	 *            the aux to set
//	 */
//	public void setAux(String aux) {
//		this.aux = aux;
//	}
//
//	/**
//	 * @return the posNeg
//	 */
//	public int getPosNeg() {
//		return this.posNeg;
//	}
//
//	/**
//	 * @param posNeg
//	 *            the posNeg to set
//	 */
//	public void setPosNeg(int posNeg) {
//		this.posNeg = posNeg;
//	}
//
//	/**
//	 * @return the partical
//	 */
//	public String getPartical() {
//		return this.partical;
//	}
//
//	/**
//	 * @param partical
//	 *            the partical to set
//	 */
//	public void setPartical(String partical) {
//		this.partical = partical;
//	}
//
//	/**
//	 * @return the patternType
//	 */
//	public int getPatternType() {
//		return this.patternType;
//	}
//
//	/**
//	 * @param patternType
//	 *            the patternType to set
//	 */
//	public void setPatternType(int patternType) {
//		this.patternType = patternType;
//	}
//
//	/**
//	 * @return the verb
//	 */
//	public Verb getVerb() {
//		return this.verb;
//	}
//
//	/**
//	 * @param verb
//	 *            the verb to set
//	 */
//	public void setVerb(Verb verb) {
//		this.verb = verb;
//	}
//
//	/**
//	 * @return the indirectObjList
//	 */
//	public IndirectObject getIndirectObj() {
//		return this.indirectObj;
//	}
//
//	/**
//	 * @param indirectObjList
//	 *            the indirectObjList to set
//	 */
//	public void setIndirectObj(IndirectObject indirectObjList) {
//		this.indirectObj = indirectObjList;
//	}
//
//	/**
//	 * @return the directObjList
//	 */
//	public DirectObject getDirectObj() {
//		return this.directObj;
//	}
//
//	/**
//	 * @param directObjList
//	 *            the directObjList to set
//	 */
//	public void setDirectObj(DirectObject directObjList) {
//		this.directObj = directObjList;
//	}
//
//	/**
//	 * @return the complement
//	 */
//	public Complement getComplement() {
//		return this.complement;
//	}
//
//	/**
//	 * @param complement
//	 *            the complement to set
//	 */
//	public void setComplement(Complement complement) {
//		this.complement = complement;
//	}

	
	public String getAux() {
		return aux;
	}



	public void setAux(String aux) {
		this.aux = aux;
	}



	public int getPosNeg() {
		return posNeg;
	}



	public void setPosNeg(int posNeg) {
		this.posNeg = posNeg;
	}



	public String getPartical() {
		return partical;
	}



	public void setPartical(String partical) {
		this.partical = partical;
	}



	public int getPatternType() {
		return patternType;
	}



	public void setPatternType(int patternType) {
		this.patternType = patternType;
	}



	public Verb getVerb() {
		return verb;
	}



	public void setVerb(Verb verb) {
		this.verb = verb;
	}



	public IndirectObject getIndirectObj() {
		return indirectObj;
	}



	public void setIndirectObj(IndirectObject indirectObj) {
		this.indirectObj = indirectObj;
	}



	public DirectObject getDirectObj() {
		return directObj;
	}



	public void setDirectObj(DirectObject directObj) {
		this.directObj = directObj;
	}



	public Complement getComplement() {
		return complement;
	}



	public void setComplement(Complement complement) {
		this.complement = complement;
	}



	public List<PrepositionalPhrase> getListPrepPhrase() {
		return listPrepPhrase;
	}



	public void setListPrepPhrase(List<PrepositionalPhrase> listPrepPhrase) {
		this.listPrepPhrase = listPrepPhrase;
	}



	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		if (posNeg == SentenceConstants.VERB_STATE_NEG)
		{
			strBuilder.append("not");
			strBuilder.append(" ");
		}
		strBuilder.append(verb.toString());
		strBuilder.append(" ");
		strBuilder.append(partical);
		return strBuilder.toString();
	}
	
	public String toStringForDetail()
	{
		StringBuilder strBuilder = new StringBuilder();
		if (posNeg == SentenceConstants.VERB_STATE_NEG)
		{
			strBuilder.append("not");
			strBuilder.append(" ");
		}
		strBuilder.append(StringProcessUtil.valueOf(aux));
		strBuilder.append(" ");
		strBuilder.append(verb.toString());
		strBuilder.append(" ");
		strBuilder.append(partical);
		strBuilder.append(" ");
		strBuilder.append(StringProcessUtil.valueOf(directObj));
		strBuilder.append(StringProcessUtil.valueOf(indirectObj));
		strBuilder.append(StringProcessUtil.valueOf(complement));
		strBuilder.append(" ");
		Iterator<PrepositionalPhrase> itrPP = listPrepPhrase.iterator();
		while(itrPP.hasNext())
		{
			PrepositionalPhrase pp = itrPP.next();
			strBuilder.append(StringProcessUtil.valueOf(pp));
			strBuilder.append(" ");
		}
		return strBuilder.toString();
	}
}
