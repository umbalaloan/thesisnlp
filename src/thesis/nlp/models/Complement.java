/**
 * 
 */
package thesis.nlp.models;

import java.util.ArrayList;
import java.util.List;

import thesis.nlp.util.StringProcessUtil;

/**
 * @author lohuynh
 *
 */
public class Complement {

	private List<NounPhrase> compNN = new ArrayList<NounPhrase>();
	private List<AdjectivePhrase> compAdjPhrase = new ArrayList<AdjectivePhrase>();
	private AdverbPhrase compAdvPhrase;
	private PrepositionalPhrase prepPhrase; // this is used to process AGENT for passive sentence. It means "prep_by"
	private SBAR compSbar;
	

	public Complement(List<NounPhrase> compNN,
			List<AdjectivePhrase> compAdjPhrase, AdverbPhrase compAdvPhrase,
			PrepositionalPhrase prepPhrase, SBAR compSbar) {
		super();
		this.compNN = compNN;
		this.compAdjPhrase = compAdjPhrase;
		this.compAdvPhrase = compAdvPhrase;
		this.prepPhrase = prepPhrase;
		this.compSbar = compSbar;
	}



	public List<NounPhrase> getCompNN() {
		return compNN;
	}



	public void setCompNN(List<NounPhrase> compNN) {
		this.compNN = compNN;
	}



	public List<AdjectivePhrase> getCompAdjPhrase() {
		return compAdjPhrase;
	}



	public void setCompAdjPhrase(List<AdjectivePhrase> compAdjPhrase) {
		this.compAdjPhrase = compAdjPhrase;
	}



	public AdverbPhrase getCompAdvPhrase() {
		return compAdvPhrase;
	}



	public void setCompAdvPhrase(AdverbPhrase compAdvPhrase) {
		this.compAdvPhrase = compAdvPhrase;
	}



	public PrepositionalPhrase getPrepPhrase() {
		return prepPhrase;
	}



	public void setPrepPhrase(PrepositionalPhrase prepPhrase) {
		this.prepPhrase = prepPhrase;
	}



	public SBAR getCompSbar() {
		return compSbar;
	}



	public void setCompSbar(SBAR compSbar) {
		this.compSbar = compSbar;
	}



	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("");
		if (this.compNN.size() != 0)
		{
			int i = 0;
			for (NounPhrase np : this.compNN)
			{
				i++;
				if (i>=2)
					strBuilder.append(",");
				if (np!= null)
					strBuilder.append(np.toStringDetail());
				strBuilder.append(" ");
			}
		}
		if (this.compAdjPhrase.size() > 0)
		{
			int i = 0;
			for (AdjectivePhrase adjp : this.compAdjPhrase)
			{
				i++;
				if (i>=2)
					strBuilder.append(",");
				strBuilder.append(StringProcessUtil.valueOf(adjp));
				strBuilder.append(" ");
			}
		}
		strBuilder.append(StringProcessUtil.valueOf(this.compAdvPhrase));
		
		return strBuilder.toString().trim();
	}
}
