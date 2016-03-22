/**
 * 
 */
package thesis.nlp.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import thesis.nlp.util.StringProcessUtil;

/**
 * @author lohuynh
 *
 */
@JsonInclude(Include.NON_NULL)
public class AdjectivePhrase {
	
	private String adj = "";
	private PrepositionalPhrase prepPhrase;
	private AdverbPhrase advPhrase;
//	private SBAR toInfSbar;
	
	public String getAdj() {
		return adj;
	}
	public AdjectivePhrase(String adj, PrepositionalPhrase prepPhrase,
		AdverbPhrase advPhrase) {
	super();
	this.adj = adj;
	this.prepPhrase = prepPhrase;
	this.advPhrase = advPhrase;
}
	public void setAdj(String adj) {
		this.adj = adj;
	}
	public PrepositionalPhrase getPrepPhrase() {
		return prepPhrase;
	}
	public void setPrepPhrase(PrepositionalPhrase prepPhrase) {
		this.prepPhrase = prepPhrase;
	}
	public AdverbPhrase getAdvPhrase() {
		return advPhrase;
	}
	public void setAdvPhrase(AdverbPhrase advPhrase) {
		this.advPhrase = advPhrase;
	}
	
	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(this.adj);
		strBuilder.append(" ");
		strBuilder.append(StringProcessUtil.valueOf(this.prepPhrase));
		strBuilder.append(" ");
		strBuilder.append(StringProcessUtil.valueOf(this.advPhrase));
//		return new StringBuilder().append(this.prepPhrase).append(" ").append(this.advPhrase).toString();
		return strBuilder.toString();
	}

	
}
