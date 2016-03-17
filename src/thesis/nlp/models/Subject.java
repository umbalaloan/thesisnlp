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
public class Subject {
	private List<NounPhrase> nplist = new ArrayList<NounPhrase>();
	private SBAR sbarSubj = new SBAR();
	private int subjVoice = -1;

	public Subject() {

	}

	/**
	 * 
	 * @param nplist
	 * @param sbarSubj
	 * @param subjVoice
	 */
	public Subject(List<NounPhrase> nplist, SBAR sbarSubj, int subjVoice) {
		super();
		this.nplist = nplist;
		this.sbarSubj = sbarSubj;
		this.subjVoice = subjVoice;
	}

	/**
	 * @return the np
	 */
	public List<NounPhrase> getNplist() {
		return this.nplist;
	}

	/**
	 * @param np
	 *            the np to set
	 */
	public void setNp(List<NounPhrase> nplist) {
		this.nplist = nplist;
	}

	/**
	 * @return the sbarSubj
	 */
	public SBAR getSbarSubj() {
		return this.sbarSubj;
	}

	/**
	 * @param sbarSubj
	 *            the sbarSubj to set
	 */
	public void setSbarSubj(SBAR sbarSubj) {
		this.sbarSubj = sbarSubj;
	}

	/**
	 * @return the subjVoice
	 */
	public int getSubjVoice() {
		return this.subjVoice;
	}

	/**
	 * @param subjVoice
	 *            the subjVoice to set
	 */
	public void setSubjVoice(int subjVoice) {
		this.subjVoice = subjVoice;
	}

	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		if (this.nplist.size() == 0)
		{
			return "";
		}
		int i = 0;
		for (NounPhrase np : this.nplist)
		{
			i++;
			strBuilder.append(StringProcessUtil.valueOf(np));
			if (i >=2)
				strBuilder.append(", ");
		}
		return strBuilder.toString();
	}
}
