/**
 * 
 */
package thesis.nlp.models;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author lohuynh
 *
 */
@JsonIgnoreProperties({ "determier" })
public class SingleNP {

	private String headNoun = "";
	private String premodifier = "";
	private String determier = "";
	private int headNounIdx = -1;
//	private String relation = "";

	private Set<Tuple> tuples = new HashSet<Tuple>();

	/**
	 * 
	 */
	public SingleNP() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param headNoun
	 * @param premodifier
	 * @param determier
	 * @param headNounIdx
	 */
	public SingleNP(String headNoun, String premodifier, String determier, int headNounIdx) {
		super();
		this.headNoun = headNoun;
		this.premodifier = premodifier;
		this.determier = determier;
		this.headNounIdx = headNounIdx;
	}

	/**
	 * @return the headNoun
	 */
	public String getHeadNoun() {
		return this.headNoun;
	}

	/**
	 * @param headNoun
	 *            the headNoun to set
	 */
	public void setHeadNoun(String headNoun) {
		this.headNoun = headNoun;
	}

	/**
	 * @return the premodifier
	 */
	public String getPremodifier() {
		return this.premodifier;
	}

	/**
	 * @param premodifier
	 *            the premodifier to set
	 */
	public void setPremodifier(String premodifier) {
		this.premodifier = premodifier;
	}

	/**
	 * @return the determier
	 */
	// @JsonIgnore
	public String getDetermier() {
		return this.determier;
	}

	/**
	 * @param determier
	 *            the determier to set
	 */
	public void setDetermier(String determier) {
		this.determier = determier;
	}

	/**
	 * @return the headNounIdx
	 */
	// @JsonIgnore
	public int getHeadNounIdx() {
		return this.headNounIdx;
	}

	/**
	 * @param headNounIdx
	 *            the headNounIdx to set
	 */
	public void setHeadNounIdx(int headNounIdx) {
		this.headNounIdx = headNounIdx;
	}
	

	public Set<Tuple> getTuples() {
		return tuples;
	}

	public void setTuples(Set<Tuple> tuples) {
		this.tuples = tuples;
	}



	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((determier == null) ? 0 : determier.hashCode());
		result = prime * result
				+ ((headNoun == null) ? 0 : headNoun.hashCode());
		result = prime * result + headNounIdx;
		result = prime * result
				+ ((premodifier == null) ? 0 : premodifier.hashCode());
		result = prime * result + ((tuples == null) ? 0 : tuples.hashCode());
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
		SingleNP other = (SingleNP) obj;
		if (determier == null) {
			if (other.determier != null)
				return false;
		} else if (!determier.equals(other.determier))
			return false;
		if (headNoun == null) {
			if (other.headNoun != null)
				return false;
		} else if (!headNoun.equals(other.headNoun))
			return false;
		if (headNounIdx != other.headNounIdx)
			return false;
		if (premodifier == null) {
			if (other.premodifier != null)
				return false;
		} else if (!premodifier.equals(other.premodifier))
			return false;
		if (tuples == null) {
			if (other.tuples != null)
				return false;
		} else if (!tuples.equals(other.tuples))
			return false;
		return true;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
//		String[] det = {"a", "an", "the"};
		String det = determier.trim().toLowerCase();
		if (!det.equals("a") || !det.equals("an") || !det.equals("the"))
		{
			str.append(det);
			str.append(" ");
		}
		str.append(premodifier.trim()).append(" ").append(headNoun.trim());
		return  new StringBuffer().append(determier.trim()).append(" ").append(premodifier.trim()).append(" ").append(headNoun.trim()).toString().trim();
	}
	
}
