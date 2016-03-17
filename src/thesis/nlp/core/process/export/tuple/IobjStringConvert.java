/**
 * 
 */
package thesis.nlp.core.process.export.tuple;

import java.util.LinkedHashSet;
import java.util.Set;

import thesis.nlp.models.IndirectObject;
import thesis.nlp.models.NounPhrase;
import thesis.nlp.util.StringProcessUtil;

/**
 * @author lohuynh
 *
 */
public class IobjStringConvert {
	public Set<String> listIobjStr (IndirectObject iobj)
	{
		if (iobj == null)
			return new LinkedHashSet<String>();
		Set<String> listiobj = new LinkedHashSet<String>();
		StringBuilder strBuilder = new StringBuilder();
		if (iobj.getNounphraseIOlist().size() == 0)
		{
			return listiobj;
		}
		int i = 0;
		for (NounPhrase np : iobj.getNounphraseIOlist())
		{
			i++;
			if (i>=2)
				strBuilder.append(",");
			String npstr = StringProcessUtil.valueOf(np);
			strBuilder.append(npstr);
			listiobj.add(npstr.trim());
		}
		if (iobj.getVsbarIO() != null)
		{
			strBuilder.append(" ");
			String sbarStr = StringProcessUtil.valueOf(iobj.getVsbarIO());
			strBuilder.append(sbarStr);
			listiobj.add(sbarStr);
		}
		listiobj.add(strBuilder.toString().trim());
		return listiobj;
	}
}
