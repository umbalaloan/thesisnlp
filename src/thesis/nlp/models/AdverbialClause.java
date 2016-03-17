/**
 * 
 */
package thesis.nlp.models;

/**
 * @author lohuynh
 *
 */
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
