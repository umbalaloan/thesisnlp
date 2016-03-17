/**
 * 
 */
package thesis.nlp.models;

import java.util.ArrayList;
import java.util.List;

import thesis.nlp.util.StringProcessUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author lohuynh
 *
 */
public class PrepositionalPhrase {
	private String prep = "";
	private List<NounPhrase> nounPhraseList = new ArrayList<NounPhrase>();
//	private NounPhrase nounPhrase;
	private SBAR sbarPrepPhrase;
	private int prepIdx = -1;

	/**
	 * 
	 */
	public PrepositionalPhrase() {
		// TODO Auto-generated constructor stub
	}

	
	public PrepositionalPhrase(String prep, List<NounPhrase> nounPhraseList) {
		super();
		this.prep = prep;
		this.nounPhraseList = nounPhraseList;
	}


	public PrepositionalPhrase(String prep, List<NounPhrase> nounPhraseList,
			SBAR sbarPrepPhrase, int prepIdx) {
		super();
		this.prep = prep;
		this.nounPhraseList = nounPhraseList;
		this.sbarPrepPhrase = sbarPrepPhrase;
		this.prepIdx = prepIdx;
	}


	public PrepositionalPhrase(String prep, List<NounPhrase> nounPhraseList,
			SBAR sbarPrepPhrase) {
		super();
		this.prep = prep;
		this.nounPhraseList = nounPhraseList;
		this.sbarPrepPhrase = sbarPrepPhrase;
	}

	public PrepositionalPhrase(String prep, List<NounPhrase> nounPhraseList,
			int prepIdx) {
		super();
		this.prep = prep;
		this.nounPhraseList = nounPhraseList;
		this.prepIdx = prepIdx;
	}


	/**
	 * @return the prep
	 */
	public String getprep() {
		return this.prep;
	}

	/**
	 * @param prep
	 *            the prep to set
	 */
	public void setprep(String prep) {
		this.prep = prep;
	}

	

	public List<NounPhrase> getNounPhraseList() {
		return nounPhraseList;
	}

	public void setNounPhraseList(List<NounPhrase> nounPhraseList) {
		this.nounPhraseList = nounPhraseList;
	}

	/**
	 * @return the sbarPrepPhrase
	 */
	public SBAR getSbarPrepPhrase() {
		return this.sbarPrepPhrase;
	}

	/**
	 * @param sbarPrepPhrase
	 *            the sbarPrepPhrase to set
	 */
	public void setSbarPrepPhrase(SBAR sbarPrepPhrase) {
		this.sbarPrepPhrase = sbarPrepPhrase;
	}

	/**
	 * @return the prepIdx
	 */
	@JsonIgnore
	public int getPrepIdx() {
		return this.prepIdx;
	}

	/**
	 * @param prepIdx
	 *            the prepIdx to set
	 */
	public void setPrepIdx(int prepIdx) {
		this.prepIdx = prepIdx;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((nounPhraseList == null) ? 0 : nounPhraseList.hashCode());
		result = prime * result + ((prep == null) ? 0 : prep.hashCode());
		result = prime * result + prepIdx;
		result = prime * result
				+ ((sbarPrepPhrase == null) ? 0 : sbarPrepPhrase.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrepositionalPhrase other = (PrepositionalPhrase) obj;
		if (nounPhraseList == null) {
			if (other.nounPhraseList != null)
				return false;
		} else if (!nounPhraseList.equals(other.nounPhraseList))
			return false;
		if (prep == null) {
			if (other.prep != null)
				return false;
		} else if (!prep.equals(other.prep))
			return false;
		if (prepIdx != other.prepIdx)
			return false;
		if (sbarPrepPhrase == null) {
			if (other.sbarPrepPhrase != null)
				return false;
		} else if (!sbarPrepPhrase.equals(other.sbarPrepPhrase))
			return false;
		return true;
	}


	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(StringProcessUtil.valueOf(this.prep).trim());
		strBuilder.append(" ");
		if (this.nounPhraseList.size() > 0)
		{
			int i = 0;
			for (NounPhrase nounphrase : this.nounPhraseList)
			{
				i++;
				if (i>=2)
					strBuilder.append(", ");
				strBuilder.append(StringProcessUtil.valueOf(nounphrase).trim());
			}
		}
		return strBuilder.toString().trim();
	}
}
