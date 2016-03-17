/**
 * 
 */
package thesis.nlp.models;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import thesis.nlp.core.process.SentenceConstants;
import thesis.nlp.util.StringProcessUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author lohuynh
 * @version 0.1
 */
public class NounPhrase {
	private Set<SingleNP> singleNPList = new LinkedHashSet<SingleNP>();

	private String conjunction = "";
	private SBAR sbarNP; // subordinate clause support for a SingleNP. Exp: "The girl who you know"
	private Set<PrepositionalPhrase> prepPhraseList = new LinkedHashSet<PrepositionalPhrase>() ;
	@JsonIgnore
//	private Set<Tuple> tuples = new LinkedHashSet<Tuple>();
	/**
	 * 
	 */
	public NounPhrase() {
		// TODO Auto-generated constructor stub
		
	}

	

	public NounPhrase(Set<SingleNP> singleNPList, String conjunction,
			SBAR sbarNP, Set<PrepositionalPhrase> prepPhraseLiist) {
		super();
		this.singleNPList = singleNPList;
		this.conjunction = conjunction;
		this.sbarNP = sbarNP;
		this.prepPhraseList = prepPhraseLiist;
	}



	/**
	 * @param singleNpInSBAR
	 */
	public NounPhrase(Set<SingleNP> singleNpInSBAR) {
		// TODO Auto-generated constructor stub
		this.singleNPList = singleNpInSBAR;
	}



	public Set<SingleNP> getSingleNPList() {
		return singleNPList;
	}



	public void setSingleNPList(Set<SingleNP> singleNPList) {
		this.singleNPList = singleNPList;
	}



	public String getConjunction() {
		return conjunction;
	}



	public void setConjunction(String conjunction) {
		this.conjunction = conjunction;
	}



	public SBAR getSbarNP() {
		return sbarNP;
	}



	public void setSbarNP(SBAR sbarNP) {
		this.sbarNP = sbarNP;
	}



	public Set<PrepositionalPhrase> getPrepPhraseLiist() {
		return prepPhraseList;
	}



	public void setPrepPhraseLiist(Set<PrepositionalPhrase> prepPhraseLiist) {
		this.prepPhraseList = prepPhraseLiist;
	}



	



//	public void setTuples(Set<Tuple> tuples) {
//		this.tuples = tuples;
//	}



	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		if (this.singleNPList.size() == 0)
		{
			return "";
		}
		int i = 0;
		for (SingleNP singleNP : this.singleNPList)
		{
			i++;
			if (i>=2)
				strBuilder.append(",");
			strBuilder.append(singleNP.toString().trim());
			strBuilder.append(" ");
			for (PrepositionalPhrase prepPhrase : this.prepPhraseList)
			{
				strBuilder.append(StringProcessUtil.valueOf(prepPhrase));
				strBuilder.append(" ");
			}
		}
		return strBuilder.toString().trim();
	}
	
	public String toStringDetail() {
		StringBuilder strBuilder = new StringBuilder();
		if (this.singleNPList.size() == 0)
		{
			return "";
		}
		int i = 0;
		for (SingleNP singleNP : this.singleNPList)
		{
			i++;
			if (i>=2)
				strBuilder.append(",");
			strBuilder.append(singleNP.toString().trim());
			strBuilder.append(" ");
			for (PrepositionalPhrase prepPhrase : this.prepPhraseList)
			{
				strBuilder.append(StringProcessUtil.valueOf(prepPhrase));
				strBuilder.append(" ");
			}
		}
		if (this.sbarNP != null)
		{
			strBuilder.append(StringProcessUtil.valueOf(this.sbarNP));
		}
		return strBuilder.toString().trim();
	}
	
}
