/**
 * 
 */
package thesis.nlp.models;

import thesis.nlp.util.StringProcessUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author lohuynh
 *
 */
@JsonInclude(Include.NON_NULL)
public class SBAR {
	private Subject subj;
	private Predicate predicateSbar;
	private String subordinator = "";
	private int typeSbar = -1;
	private String refSubj = "";

	public SBAR() {

	}

	/**
	 * @param subj
	 * @param predicateSbar
	 * @param subordinator
	 * @param typeSbar
	 */
	public SBAR(Subject subj, Predicate predicateSbar, String subordinator) {
		super();
		this.subj = subj;
		this.predicateSbar = predicateSbar;
		this.subordinator = subordinator;
	}

	/**
	 * @param subj
	 * @param predicateSbar
	 * @param subordinator
	 * @param typeSbar
	 * @param refSubj
	 */
	public SBAR(Subject subj, Predicate predicateSbar, String subordinator,
			String refSubj) {
		super();
		this.subj = subj;
		this.predicateSbar = predicateSbar;
		this.subordinator = subordinator;
		this.refSubj = refSubj;
	}

	/**
	 * @return the subj
	 */
	public Subject getSubj() {
		return this.subj;
	}

	/**
	 * @param subj
	 *            the subj to set
	 */
	public void setSubj(Subject subj) {
		this.subj = subj;
	}

	/**
	 * @return the predicateSbar
	 */
	public Predicate getPredicateSbar() {
		return this.predicateSbar;
	}

	/**
	 * @param predicateSbar
	 *            the predicateSbar to set
	 */
	public void setPredicateSbar(Predicate predicateSbar) {
		this.predicateSbar = predicateSbar;
	}

	/**
	 * @return the subordinator
	 */
	public String getSubordinator() {
		return this.subordinator;
	}

	/**
	 * @param subordinator
	 *            the subordinator to set
	 */
	public void setSubordinator(String subordinator) {
		this.subordinator = subordinator;
	}

	/**
	 * @return the typeSbar
	 */
	@JsonIgnore
	public int getTypeSbar() {
		return this.typeSbar;
	}

	/**
	 * @param typeSbar
	 *            the typeSbar to set
	 */
	public void setTypeSbar(int typeSbar) {
		this.typeSbar = typeSbar;
	}

	/**
	 * @return the refSubj
	 */
	public String getRefSubj() {
		return this.refSubj;
	}

	/**
	 * @param refSubj
	 *            the refSubj to set
	 */
	public void setRefSubj(String refSubj) {
		this.refSubj = refSubj;
	}
	
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(subordinator);
		
		return new StringBuilder().append(this.subordinator.trim()).append(" ").
				append(StringProcessUtil.valueOf(this.subj).trim()).append(" ").
				append(StringProcessUtil.valueOf(this.predicateSbar).trim()).toString().trim();
	}

}
