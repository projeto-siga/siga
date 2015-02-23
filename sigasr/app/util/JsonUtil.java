package util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public abstract class JsonUtil {

	public static final Gson gson = new Gson();

	public static JsonElement toJson(Object element) {
		return gson.toJsonTree(element);
	}
}