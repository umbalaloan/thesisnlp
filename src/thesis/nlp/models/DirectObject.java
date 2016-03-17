/**
 * 
 */
package thesis.nlp.models;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import thesis.nlp.util.StringProcessUtil;

/**
 * @author lohuynh
 *
 */
public class DirectObject {

	private List<NounPhrase> nounphraseDOlist; // because NounPhrase include a list of
										// SingleNP
	private SBAR vsbarDO;
	private String pronounDO = "";

	public DirectObject()
	{
		
	}
	/**
	 * @return the nounphraseDO
	 */
	public List<NounPhrase> getNounphraseDO() {
		return this.nounphraseDOlist;
	}

	/**
	 * @param nounphraseDOlist
	 *            the nounphraseDO to set
	 */
	public void setNounphraseDO(List<NounPhrase> nounphraseDOlist) {
		this.nounphraseDOlist = nounphraseDOlist;
	}

	/**
	 * @return the vsbarDO
	 */
	public SBAR getVsbarDO() {
		return this.vsbarDO;
	}

	/**
	 * @param vsbarDO
	 *            the vsbarDO to set
	 */
	public void setVsbarDO(SBAR vsbarDO) {
		this.vsbarDO = vsbarDO;
	}

	/**
	 * @return the pronounDO
	 */
	public String getPronounDO() {
		return this.pronounDO;
	}

	/**
	 * @param pronounDO
	 *            the pronounDO to set
	 */
	public void setPronounDO(String pronounDO) {
		this.pronounDO = pronounDO;
	}

	/**
	 * @param nounphraseDO
	 * @param vsbarDO
	 * @param pronounDO
	 */
	public DirectObject(List<NounPhrase> nounphraseDOlist, SBAR vsbarDO, String pronounDO) {
		super();
		this.nounphraseDOlist = nounphraseDOlist;
		this.vsbarDO = vsbarDO;
		this.pronounDO = pronounDO;
	}
	
	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		if (this.nounphraseDOlist.size() == 0)
		{
			return "";
		}
		int i = 0;
		for (NounPhrase np : this.nounphraseDOlist)
		{
			i++;
			if (i>=2)
				strBuilder.append(", ");
//			strBuilder.append(StringProcessUtil.valueOf(np));
			if (np!= null)
				strBuilder.append(np.toStringDetail());
		}
		if (this.vsbarDO != null)
		{
			strBuilder.append(" ");
			String vsbar = StringProcessUtil.valueOf(this.vsbarDO).trim();
			if (!StringProcessUtil.checkStringContainSubString(strBuilder.toString(), vsbar))
				strBuilder.append(StringProcessUtil.valueOf(this.vsbarDO).trim());
		}
		return strBuilder.toString().trim();
	}

}
