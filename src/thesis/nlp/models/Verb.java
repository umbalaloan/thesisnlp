/**
 * 
 */
package thesis.nlp.models;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import thesis.nlp.core.process.SentenceConstants;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author lohuynh
 *
 */
@JsonIgnoreProperties({"verbForm", "verbTense", "verbIdx"})
public class Verb {

	/**
	 * 
	 */
	private String verbName = "";
	private int verbForm = -1;
	private String verbVoice = SentenceConstants.VERBVOICE_ACTIVE;
	private String verbTense = "";
	private int verbIdx = -1;
	private int verbType = -1;
	private String stemmedVerb = "";

	public Verb() {
		// TODO Auto-generated constructor stub
	}

	

	/**
	 * @param verbName
	 * @param verbIdx
	 * @param stemmedVerb
	 */
	public Verb(String verbName, int verbIdx, String stemmedVerb) {
		super();
		this.verbName = verbName;
		this.verbIdx = verbIdx;
		this.stemmedVerb = stemmedVerb;
	}



	/**
	 * @param verbName
	 * @param verbIdx
	 * @param verbType
	 * @param stemmedVerb
	 */
	public Verb(String verbName, int verbIdx, int verbType, String stemmedVerb) {
		super();
		this.verbName = verbName;
		this.verbIdx = verbIdx;
		this.verbType = verbType;
		this.stemmedVerb = stemmedVerb;
	}



	/**
	 * @param verbName
	 * @param verbVoice
	 * @param verbIdx
	 * @param stemmedVerb
	 */
	public Verb(String verbName, String verbVoice, int verbIdx, String stemmedVerb) {
		super();
		this.verbName = verbName;
		this.verbVoice = verbVoice;
		this.verbIdx = verbIdx;
		this.stemmedVerb = stemmedVerb;
	}



	/**
	 * @param verbName
	 * @param verbForm
	 * @param verbVoice
	 * @param verbTense
	 * @param verbIdx
	 * @param verbType
	 * @param stemmedVerb
	 */
	public Verb(String verbName, int verbForm, String verbVoice, String verbTense, int verbIdx, int verbType, String stemmedVerb) {
		super();
		this.verbName = verbName;
		this.verbForm = verbForm;
		this.verbVoice = verbVoice;
		this.verbTense = verbTense;
		this.verbIdx = verbIdx;
		this.verbType = verbType;
		this.stemmedVerb = stemmedVerb;
	}



	/**
	 * @return the verbName
	 */
	public String getVerbName() {
		return this.verbName;
	}



	/**
	 * @param verbName the verbName to set
	 */
	public void setVerbName(String verbName) {
		this.verbName = verbName;
	}



	/**
	 * @return the verbForm
	 */
	public int getVerbForm() {
		return this.verbForm;
	}



	/**
	 * @param verbForm the verbForm to set
	 */
	public void setVerbForm(int verbForm) {
		this.verbForm = verbForm;
	}



	/**
	 * @return the verbVoice
	 */
	public String getVerbVoice() {
		return this.verbVoice;
	}



	/**
	 * @param verbVoice the verbVoice to set
	 */
	public void setVerbVoice(String verbVoice) {
		this.verbVoice = verbVoice;
	}



	/**
	 * @return the verbTense
	 */
	public String getVerbTense() {
		return this.verbTense;
	}



	/**
	 * @param verbTense the verbTense to set
	 */
	public void setVerbTense(String verbTense) {
		this.verbTense = verbTense;
	}



	/**
	 * @return the verbIdx
	 */
	public int getVerbIdx() {
		return this.verbIdx;
	}



	/**
	 * @param verbIdx the verbIdx to set
	 */
	public void setVerbIdx(int verbIdx) {
		this.verbIdx = verbIdx;
	}



	/**
	 * @return the verbType
	 */
	public int getVerbType() {
		return this.verbType;
	}



	/**
	 * @param verbType the verbType to set
	 */
	public void setVerbType(int verbType) {
		this.verbType = verbType;
	}



	/**
	 * @return the stemmedVerb
	 */
	public String getStemmedVerb() {
		return this.stemmedVerb;
	}



	/**
	 * @param stemmedVerb the stemmedVerb to set
	 */
	public void setStemmedVerb(String stemmedVerb) {
		this.stemmedVerb = stemmedVerb;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.verbIdx;
		result = prime * result + ((this.verbName == null) ? 0 : this.verbName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Verb other = (Verb) obj;
		if (this.verbIdx != other.verbIdx)
			return false;
		if (this.verbName == null) {
			if (other.verbName != null)
				return false;
		} else if (!this.verbName.equals(other.verbName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return new StringBuilder().append(stemmedVerb).toString();
	}

}
