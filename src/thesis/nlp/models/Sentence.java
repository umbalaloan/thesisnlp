/**
 * 
 */
package thesis.nlp.models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lohuynh
 *
 */
public class Sentence {
	private AdverbialClause advClause;
	private List<Clause> clauses = new ArrayList<Clause>();
	private List<String> conjunctionList = new ArrayList<String>();

	/**
	 * 
	 */
	public Sentence() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param advClause
	 * @param clauses
	 * @param conjunctionList
	 */
	public Sentence(AdverbialClause advClause, List<Clause> clauses,
			List<String> conjunctionList) {
		super();
		this.advClause = advClause;
		this.clauses = clauses;
		this.conjunctionList = conjunctionList;
	}

	/**
	 * @return the advClause
	 */
	public AdverbialClause getAdvClause() {
		return this.advClause;
	}

	/**
	 * @param advClause
	 *            the advClause to set
	 */
	public void setAdvClause(AdverbialClause advClause) {
		this.advClause = advClause;
	}

	/**
	 * @return the clauses
	 */
	public List<Clause> getClauses() {
		return this.clauses;
	}

	/**
	 * @param clauses
	 *            the clauses to set
	 */
	public void setClauses(List<Clause> clauses) {
		this.clauses = clauses;
	}

	/**
	 * @return the conjunctionList
	 */
	public List<String> getConjunctionList() {
		return this.conjunctionList;
	}

	/**
	 * @param conjunctionList
	 *            the conjunctionList to set
	 */
	public void setConjunctionList(List<String> conjunctionList) {
		this.conjunctionList = conjunctionList;
	}

}
