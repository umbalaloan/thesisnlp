/**
 * 
 */
package thesis.nlp.core.process.export.tuple;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import thesis.nlp.models.AdjectivePhrase;
import thesis.nlp.models.Complement;
import thesis.nlp.models.NounPhrase;
import thesis.nlp.util.StringProcessUtil;

/**
 * @author lohuynh
 *
 */
public class ComplementStringConvert {

	public Set<String> listCompStr(Complement comp)
	{
		Set<String> listComp = new LinkedHashSet<String>();
		if (comp == null)
			return listComp;
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("");
		if (comp.getCompNN().size() != 0)
		{
			int i = 0;
			for (NounPhrase np : comp.getCompNN())
			{
				i++;
				if (i>=2)
					strBuilder.append(",");
				String npstr = StringProcessUtil.valueOf(np);
				strBuilder.append(npstr);
				strBuilder.append(" ");
			}
			listComp.add(strBuilder.toString().trim());
		}
		if (comp.getCompAdjPhrase().size() > 0)
		{
			int i = 0;
			for (AdjectivePhrase adjp : comp.getCompAdjPhrase())
			{
				i++;
				if (i>=2)
					strBuilder.append("(and) ");
				String adjstr = StringProcessUtil.valueOf(adjp).trim();
				strBuilder.append(adjstr);
				strBuilder.append(" ");
				listComp.add(adjstr);
			}
			listComp.add(strBuilder.toString().trim());
		}
//		String adv = StringProcessUtil.valueOf(comp.getCompAdvPhrase()).trim();
//		listComp.add(strBuilder.append(adv).toString());
		Iterator<String> advitr = AdvPhraseStringConvert.getInstance().advphraseStr(comp.getCompAdvPhrase()).iterator();
		while (advitr.hasNext())
		{
			String advstr = advitr.next();
			strBuilder.append(advstr);
			
			listComp.add(advstr);
		}
		if (comp.getCompSbar()!= null)
		{
			String compSbarStr = StringProcessUtil.valueOf(comp.getCompSbar());
			listComp.add(compSbarStr.trim());
		}
		String prep = StringProcessUtil.valueOf(comp.getPrepPhrase()).trim();
		strBuilder.append(prep.toString());
		listComp.add(prep.toString().trim());
		return listComp;
	}
}
