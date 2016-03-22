package thesis.nlp.keywords;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonElement;

public class KeywordsExtraction {
	private static Set<String> listKeywords;
	public Set<String> extractKeyWords(String key, JsonElement jsonElement)
	{
		listKeywords = new HashSet<String>();
		check(key, jsonElement);
		return listKeywords;
	}
	private static void check(String key, JsonElement jsonElement) {

		if (jsonElement.isJsonArray()) {
			for (JsonElement jsonElement1 : jsonElement.getAsJsonArray()) {
				check(key, jsonElement1);
				
			}
		} else {
			if (jsonElement.isJsonObject()) {
				Set<Map.Entry<String, JsonElement>> entrySet = jsonElement
						.getAsJsonObject().entrySet();
				for (Map.Entry<String, JsonElement> entry : entrySet) {
					String key1 = entry.getKey();
					if (key1.equals(key)) {
						listKeywords.add(entry.getValue().toString());
					}
					check(key, entry.getValue());
				}
			} else {
				if (jsonElement.toString().equals(key)) {
					listKeywords.add(jsonElement.toString());
				}
			}
		}
	}
	public static Set<String> getList() {
		return listKeywords;
	}
//	public static void setList(List<String> list) {
//		KeywordsExtraction.list = list;
//	}

}
