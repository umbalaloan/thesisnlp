/**
 * 
 */
package thesis.nlp.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import thesis.nlp.util.StringProcessUtil;

/**
 * @author lohuynh
 *
 */
@JsonInclude(Include.NON_NULL)
public class AdverbPhrase {

	private SBAR sbarAdvPhrase = null;
	private List<PrepositionalPhrase> listPrepPhrase = new ArrayList<PrepositionalPhrase>();
	private NounPhrase nounPhrase = null;
	private String adv = "";

	/**
	 * Default Constructor
	 */
	public AdverbPhrase() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param sbarAdvPhrase
	 * @param listPrepPhrase
	 * @param nounPhrase
	 * @param adv
	 */
	public AdverbPhrase(SBAR sbarAdvPhrase, List<PrepositionalPhrase> listPrepPhrase, NounPhrase nounPhrase, String adv) {
		super();
		this.sbarAdvPhrase = sbarAdvPhrase;
		this.listPrepPhrase = listPrepPhrase;
		this.nounPhrase = nounPhrase;
		this.adv = adv;
	}

	/**
	 * @return the sbarAdvPhrase
	 */
	public SBAR getSbarAdvPhrase() {
		return this.sbarAdvPhrase;
	}

	/**
	 * @param sbarAdvPhrase the sbarAdvPhrase to set
	 */
	public void setSbarAdvPhrase(SBAR sbarAdvPhrase) {
		this.sbarAdvPhrase = sbarAdvPhrase;
	}

	/**
	 * @return the listPrepPhrase
	 */
	public List<PrepositionalPhrase> getListPrepPhrase() {
		return this.listPrepPhrase;
	}

	/**
	 * @param listPrepPhrase the listPrepPhrase to set
	 */
	public void setListPrepPhrase(List<PrepositionalPhrase> listPrepPhrase) {
		this.listPrepPhrase = listPrepPhrase;
	}

	/**
	 * @return the nounPhrase
	 */
	public NounPhrase getNounPhrase() {
		return this.nounPhrase;
	}

	/**
	 * @param nounPhrase the nounPhrase to set
	 */
	public void setNounPhrase(NounPhrase nounPhrase) {
		this.nounPhrase = nounPhrase;
	}

	/**
	 * @return the adv
	 */
	public String getAdv() {
		return this.adv;
	}

	/**
	 * @param adv the adv to set
	 */
	public void setAdv(String adv) {
		this.adv = adv;
	}
	
	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
//		strBuilder.append(this.nounPhrase.toString());
		strBuilder.append(this.adv);
		strBuilder.append(" ");
		strBuilder.append(StringProcessUtil.valueOf(this.nounPhrase));
		if (this.listPrepPhrase.size() != 0)
		{
			for (PrepositionalPhrase pp : this.listPrepPhrase)
			{
				strBuilder.append(" ");
				strBuilder.append(StringProcessUtil.valueOf(pp));
			}
		}
//		if (this.sbarAdvPhrase != null)
//		{
//			strBuilder.append(" ");
//			strBuilder.append(StringProcessUtil.valueOf(this.sbarAdvPhrase));
//		}
		
		
		return strBuilder.toString();
	}
	
	
}
