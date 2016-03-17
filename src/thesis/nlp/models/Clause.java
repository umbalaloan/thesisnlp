/**
 * 
 */
package thesis.nlp.models;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author lohuynh
 *
 */
public class Clause {
	private Subject subject;
	private Predicate predicate;

	/**
	 * 
	 */
	public Clause() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param subject
	 * @param predicate
	 */
	public Clause(Subject subject, Predicate predicate) {
		super();
		this.subject = subject;
		this.predicate = predicate;
	}

	/**
	 * @return the subject
	 */
	public Subject getSubject() {
		return this.subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	/**
	 * @return the predicate
	 */
	public Predicate getPredicate() {
		return this.predicate;
	}

	/**
	 * @param predicate
	 *            the predicate to set
	 */
	public void setPredicate(Predicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public String toString() {
		 return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("subject: ", subject.getNplist().toString()).toString();
//		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
