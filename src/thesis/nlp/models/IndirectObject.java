/**
 * 
 */
package thesis.nlp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import thesis.nlp.util.StringProcessUtil;

/**
 * @author lohuynh
 *
 */
public class IndirectObject {

	private List<NounPhrase> nounphraseIOlist = new ArrayList<NounPhrase>();
	private SBAR vsbarIO;
	private String pronounIO = "";

	/**
	 * Default Constructor
	 */
	public IndirectObject() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param nounphraseIOlist
	 * @param vsbarIO
	 * @param pronounIO
	 */
	public IndirectObject(List<NounPhrase> nounphraseIOlist, SBAR vsbarIO, String pronounIO) {
		super();
		this.nounphraseIOlist = nounphraseIOlist;
		this.vsbarIO = vsbarIO;
		this.pronounIO = pronounIO;
	}

	/**
	 * @return the nounphraseIOlist
	 */
	public List<NounPhrase> getNounphraseIOlist() {
		return this.nounphraseIOlist;
	}

	/**
	 * @param nounphraseIOlist
	 *            the nounphraseIOlist to set
	 */
	public void setNounphraseIOlist(List<NounPhrase> nounphraseIOlist) {
		this.nounphraseIOlist = nounphraseIOlist;
	}

	/**
	 * @return the vsbarIO
	 */
	public SBAR getVsbarIO() {
		return this.vsbarIO;
	}

	/**
	 * @param vsbarIO
	 *            the vsbarIO to set
	 */
	public void setVsbarIO(SBAR vsbarIO) {
		this.vsbarIO = vsbarIO;
	}

	/**
	 * @return the pronounIO
	 */
	public String getPronounIO() {
		return this.pronounIO;
	}

	/**
	 * @param pronounIO
	 *            the pronounIO to set
	 */
	public void setPronounIO(String pronounIO) {
		this.pronounIO = pronounIO;
	}
	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		if (this.nounphraseIOlist.size() == 0)
		{
			return "";
		}
		int i = 0;
		for (NounPhrase np : this.nounphraseIOlist)
		{
			i++;
			if (i>=2)
				strBuilder.append(",");
			if (np!= null)
				strBuilder.append(np.toStringDetail());
			/*strBuilder.append(", ");
			strBuilder.append(prepPhrase.toString());*/
		}
		if (this.vsbarIO != null)
		{
			strBuilder.append(" ");
			String vsbar = StringProcessUtil.valueOf(this.vsbarIO).trim();
			if (!StringProcessUtil.checkStringContainSubString(strBuilder.toString(), vsbar))
				strBuilder.append(StringProcessUtil.valueOf(this.vsbarIO).trim());
		}
		return strBuilder.toString().trim();
	}
}
