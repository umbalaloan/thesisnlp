/**
 * 
 */
package thesis.nlp.core.process.export.tuple;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Iterator;

import thesis.nlp.models.DirectObject;
import thesis.nlp.models.NounPhrase;
import thesis.nlp.util.StringProcessUtil;

/**
 * @author lohuynh
 *
 */
public class DobjStringConvert {
	
	public Set<String> listDobjStr (DirectObject dobj)
	{
		if (dobj == null)
			return new LinkedHashSet<String>();
		Set<String> listdobj = new LinkedHashSet<String>();
		StringBuilder strBuilder = new StringBuilder();
		int i = 0;
		Iterator<NounPhrase> itrNP = dobj.getNounphraseDO().iterator();
		while (itrNP.hasNext())
		{
			NounPhrase np = itrNP.next();
			i++;
			if (i>=2)
				strBuilder.append(",");
			String npstr = StringProcessUtil.valueOf(np);
			strBuilder.append(npstr);
			listdobj.add(npstr.trim());
		}
		if (dobj.getVsbarDO() != null)
		{
			strBuilder.append(" ");
			String sbarStr = StringProcessUtil.valueOf(dobj.getVsbarDO());
//			System.out.println("SBARSTR " + sbarStr);
			strBuilder.append(sbarStr);
			listdobj.add(sbarStr);
		}
		if (!dobj.getPronounDO().equals(""))
			listdobj.add(dobj.getPronounDO());
		listdobj.add(strBuilder.toString().trim());
		return listdobj;
	}

}
