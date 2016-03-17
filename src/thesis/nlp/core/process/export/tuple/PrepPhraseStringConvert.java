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
public class PrepPhraseStringConvert {
	private static PrepPhraseStringConvert instance = null;
	public static PrepPhraseStringConvert  getInstance() {
		if (instance == null) {
			instance = new PrepPhraseStringConvert ();
		}
		return instance;
	}
//	 public Set<String> prepPhraseStr (PrepositionalPhrase prepphrase)
//	 {
//		 if (prepphrase == null)
//			 return new LinkedHashSet<String>();
//	 	Set<String> listOfPrepPhrase = new LinkedHashSet<String>();
//		StringBuilder strBuilder = new StringBuilder();
//		strBuilder.append(advphrase.getAdv());
//		strBuilder.append(" ");
//		strBuilder.append(StringProcessUtil.valueOf(advphrase.getNounPhrase()));
//		listOfPrepPhrase.add(strBuilder.toString());
//		if (advphrase.getListPrepPhrase().size() != 0)
//		{
//			String prepbuilder = strBuilder.toString();
//			for (PrepositionalPhrase pp : advphrase.getListPrepPhrase())
//			{
//				strBuilder.append(" ");
//				String prep = prepbuilder + " " + StringProcessUtil.valueOf(pp).trim();
//				strBuilder.append(StringProcessUtil.valueOf(pp).trim());
//				listOfPrepPhrase.add(prep.trim());
//			}
//			
//		}
//		
//		return listOfPrepPhrase;
//	}
}

