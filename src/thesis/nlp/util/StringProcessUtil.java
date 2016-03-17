/**
 * 
 */
package thesis.nlp.util;

/**
 * @author lohuynh
 *
 */
public class StringProcessUtil {
	public static String valueOf(Object obj)
	{
		return obj == null ? "" : obj.toString().trim();
	}
	
	public static boolean checkStringContainSubString(String str, String subString)
	{
		if (str.toLowerCase().trim().contains(subString.toLowerCase().trim()))
			return true;
		else
			return false;
	}
	
	public static void main(String[] args) {
		String str = "Socatoa";
		String subStr = "Soca";
		System.out.println(checkStringContainSubString(str, subStr));
	}
}
