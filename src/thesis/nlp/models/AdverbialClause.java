/**
 * 
 */
package thesis.nlp.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author lohuynh
 *
 */
@JsonInclude(Include.NON_NULL)
public class AdverbialClause {
	private AdverbPhrase advPhrase = null;

	/**
	 * 
	 */
	public AdverbialClause() {
		// TODO Auto-generated constructor stub
	}
	

	/**
	 * @param advPhrase
	 */
	public AdverbialClause(AdverbPhrase advPhrase) {
		super();
		this.advPhrase = advPhrase;
	}


	/**
	 * @return the advPhrase
	 */
	public AdverbPhrase getAdvPhrase() {
		return this.advPhrase;
	}

	/**
	 * @param advPhrase
	 *            the advPhrase to set
	 */
	public void setAdvPhrase(AdverbPhrase advPhrase) {
		this.advPhrase = advPhrase;
	}

}
