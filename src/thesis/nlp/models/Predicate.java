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
public class Predicate {
	private List<VerbPhrase> vpList = new ArrayList<VerbPhrase>();
//	private List<PrepositionalPhrase> listPrepPhrase = new ArrayList<PrepositionalPhrase>();
	private AdverbPhrase advPhrase = null;
//	private String refSubj = " ";
//	private String adv = " ";
	
	public List<VerbPhrase> getVpList() {
		return vpList;
	}
	public Predicate(List<VerbPhrase> vpList, AdverbPhrase advPhrase) {
	super();
	this.vpList = vpList;
	this.advPhrase = advPhrase;
}
	public void setVpList(List<VerbPhrase> vpList) {
		this.vpList = vpList;
	}
	public AdverbPhrase getAdvPhrase() {
		return advPhrase;
	}
	public void setAdvPhrase(AdverbPhrase advPhrase) {
		this.advPhrase = advPhrase;
	}
	
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		int i = 0;
		for (VerbPhrase vp : vpList)
		{
			i++;
			if (i>=2)
				builder.append(", ");
//			builder.append(StringProcessUtil.valueOf(vp));
			String vpStr = vp.toStringForDetail();
//			System.out.println("PREDICATE VPSTR: " + vpStr);
			if (!StringProcessUtil.checkStringContainSubString(builder.toString().trim(), vpStr.trim()))
				builder.append(vp.toStringForDetail());
			
		}
		if (advPhrase != null)
		{
			builder.append(" ");
			String adv = StringProcessUtil.valueOf(advPhrase);
			if (!StringProcessUtil.checkStringContainSubString(builder.toString(), adv))
				builder.append(adv);
			
		}
		return builder.toString().trim();
	}
	

	
	

//	/**
//	 * @param vpList
//	 * @param listPrepPhrase
//	 * @param advClause
//	 */
//	public Predicate(List<VerbPhrase> vpList, List<PrepositionalPhrase> listPrepPhrase, AdverbialClause advClause) {
//		super();
//		this.vpList = vpList;
//		this.listPrepPhrase = listPrepPhrase;
//		this.advClause = advClause;
//	}
//
//	/**
//	 * @param vpList
//	 *            the vpList to set
//	 */
//	public void setVpList(List<VerbPhrase> vpList) {
//		this.vpList = vpList;
//	}
//
//	/**
//	 * @return the vpList
//	 */
//	public List<VerbPhrase> getVpList() {
//		return this.vpList;
//	}
//
//	/**
//	 * @return the listPrepPhrase
//	 */
//	public List<PrepositionalPhrase> getListPrepPhrase() {
//		return this.listPrepPhrase;
//	}
//
//	/**
//	 * @param listPrepPhrase
//	 *            the listPrepPhrase to set
//	 */
//	public void setListPrepPhrase(List<PrepositionalPhrase> listPrepPhrase) {
//		this.listPrepPhrase = listPrepPhrase;
//	}
//
//	/**
//	 * @return the refSubj
//	 */
//	@JsonIgnore
//	public String getRefSubj() {
//		return this.refSubj;
//	}
//
//	/**
//	 * @param refSubj
//	 *            the refSubj to set
//	 */
//	public void setRefSubj(String refSubj) {
//		this.refSubj = refSubj;
//	}
//
//	/**
//	 * @return the advClause
//	 */
//	public AdverbialClause getAdvClause() {
//		return this.advClause;
//	}
//
//	/**
//	 * @param advClause the advClause to set
//	 */
//	public void setAdvClause(AdverbialClause advClause) {
//		this.advClause = advClause;
//	}
	
}

