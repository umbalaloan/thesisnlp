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
public class Tuple {

	private String subject = "";
	private String relationship = ""; // verb
	private String object = "";
	private String voice = "";
//	private String advPhrase = "";
	private String prepPhrase = "";

	/**
	 * @param subject
	 * @param relationship
	 * @param object
	 * @param voice
	 * @param prepPhrase
	 */
	public Tuple(String subject, String relationship, String object, String voice, String prepPhrase) {
		super();
		this.subject = subject;
		this.relationship = relationship;
		this.object = object;
		this.voice = voice;
		this.prepPhrase = prepPhrase;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return this.subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject.trim();
	}

	/**
	 * @return the relationship
	 */
	public String getRelationship() {
		return this.relationship;
	}

	/**
	 * @param relationship
	 *            the relationship to set
	 */
	public void setRelationship(String relationship) {
		this.relationship = relationship.trim();
	}

	/**
	 * @return the object
	 */
	public String getObject() {
		return this.object;
	}

	/**
	 * @param object
	 *            the object to set
	 */
	public void setObject(String object) {
		this.object = object.trim();
	}

	/**
	 * @return the voice
	 */
	public String getVoice() {
		return this.voice;
	}

	/**
	 * @param voice
	 *            the voice to set
	 */
	public void setVoice(String voice) {
		this.voice = voice;
	}

	/**
	 * @return the prepPhrase
	 */
	public String getPrepPhrase() {
		return this.prepPhrase;
	}

	/**
	 * @param prepPhrase
	 *            the prepPhrase to set
	 */
	public void setPrepPhrase(String prepPhrase) {
		this.prepPhrase = prepPhrase.trim();
	}

	public String toString() {
		return new StringBuilder().append("{").append(this.subject).append(" ; ").append(this.relationship).append(" ; ").append(this.object)
				.append(" ; ").append(this.prepPhrase).append(" ; ").append(this.voice).append("}").toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		result = prime * result
				+ ((prepPhrase == null) ? 0 : prepPhrase.hashCode());
		result = prime * result
				+ ((relationship == null) ? 0 : relationship.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
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
		Tuple other = (Tuple) obj;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.trim().equals(other.object.trim()))
			return false;
		if (prepPhrase == null) {
			if (other.prepPhrase != null)
				return false;
		} else if (!prepPhrase.trim().equals(other.prepPhrase.trim()))
			return false;
		if (relationship == null) {
			if (other.relationship != null)
				return false;
		} else if (!relationship.trim().equals(other.relationship.trim()))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.trim().equals(other.subject.trim()))
			return false;
		return true;
	}

	
	
	
}
