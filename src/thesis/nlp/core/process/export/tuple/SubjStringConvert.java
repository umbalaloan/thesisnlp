/**
 * 
 */
package thesis.nlp.core.process.export.tuple;

import java.util.LinkedHashSet;
import java.util.Set;

import thesis.nlp.models.NounPhrase;
import thesis.nlp.models.SBAR;
import thesis.nlp.models.Subject;
import thesis.nlp.util.StringProcessUtil;
import thesis.nlp.util.TupleUtil;

/**
 * @author lohuynh
 *
 */
public class SubjStringConvert {
	
	public static Set<String> listSubjStr (Subject subj)
	{
		Set<String> listsubj = new LinkedHashSet<String>();
		if (subj == null)
		{
			listsubj.add("");
			return listsubj;
		}
		StringBuilder strBuilder = new StringBuilder();
		if (subj.getNplist().size() == 0)
		{
			return listsubj;
		}
		int i = 0;
		for (NounPhrase np : subj.getNplist())
		{
			i++;
			if (i>=2)
				strBuilder.append(",");
			String npstr = StringProcessUtil.valueOf(np).trim();
			strBuilder.append(npstr);
			listsubj.add(npstr);
		}
		listsubj.add(strBuilder.toString().trim());
		return listsubj;
	}
	
	public static String subjInSbar(SBAR sbar)
	{
		Subject subjSbar = sbar.getSubj();
		String subject = StringProcessUtil.valueOf(subjSbar).trim();
		boolean subjIsTHAT = TupleUtil.checkSubjIsTHAT_WH(subject);
		if ( subjIsTHAT || (!sbar.getRefSubj().equals(""))) // replace "that"by refer Subj if has
			subject = sbar.getRefSubj().trim();
		return subject;
	}

}
