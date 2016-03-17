/**
 * 
 */
package thesis.nlp.core.process.export.tuple;

import java.util.LinkedHashSet;
import java.util.Set;

import thesis.nlp.models.AdverbPhrase;
import thesis.nlp.models.PrepositionalPhrase;
import thesis.nlp.util.StringProcessUtil;

/**
 * @author lohuynh
 *
 */
public class AdvPhraseStringConvert {
	private static AdvPhraseStringConvert instance = null;
	public static AdvPhraseStringConvert  getInstance() {
		if (instance == null) {
			instance = new AdvPhraseStringConvert ();
		}
		return instance;
	}
	 public Set<String> advphraseStr (AdverbPhrase advphrase)
	 {
		 if (advphrase == null)
			 return new LinkedHashSet<String>();
	 	Set<String> listOfAdvPhrase = new LinkedHashSet<String>();
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(advphrase.getAdv());
		strBuilder.append(" ");
		strBuilder.append(StringProcessUtil.valueOf(advphrase.getNounPhrase()));
		listOfAdvPhrase.add(strBuilder.toString().trim());
		if (advphrase.getListPrepPhrase().size() != 0)
		{
			String prepbuilder = strBuilder.toString();
			for (PrepositionalPhrase pp : advphrase.getListPrepPhrase())
			{
				strBuilder.append(" ");
				String prep = prepbuilder + " " + StringProcessUtil.valueOf(pp).trim();
				strBuilder.append(StringProcessUtil.valueOf(pp).trim());
				listOfAdvPhrase.add(prep.trim());
			}
			
		}	
		if (advphrase.getSbarAdvPhrase() != null)
		{
			String sbarAdvPhrase = StringProcessUtil.valueOf(advphrase.getSbarAdvPhrase());
			strBuilder.append(" ");
			strBuilder.append(sbarAdvPhrase);
//			listOfAdvPhrase.add(strBuilder.toString());
			listOfAdvPhrase.add(sbarAdvPhrase);
		}
	 
		return listOfAdvPhrase;
	}
}

