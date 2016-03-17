/**
 * 
 */
package thesis.nlp.core.process.export.tuple;

import java.util.Set;

import thesis.nlp.models.NounPhrase;
import thesis.nlp.models.PrepositionalPhrase;
import thesis.nlp.models.SingleNP;
import thesis.nlp.util.StringProcessUtil;

/**
 * @author lohuynh
 *
 */
public class NounPhraseStringConvert {
	
	protected static String strOflistSingleNPInNP (NounPhrase np)
	{
		StringBuilder strBuilder = new StringBuilder();
		if (np.getSingleNPList().size() == 0)
		{
			return "";
		}
		int i = 0;
		for (SingleNP singleNP : np.getSingleNPList())
		{
			i++;
			if (i>=2)
				strBuilder.append(",");
			strBuilder.append(singleNP.toString());
			strBuilder.append(" ");
		}
		return strBuilder.toString().trim();
	}

}
