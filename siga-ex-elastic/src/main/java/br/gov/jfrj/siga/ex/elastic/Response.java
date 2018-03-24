package br.gov.jfrj.siga.ex.elastic;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

public class Response {
	boolean notFound;
	byte[] content;
	String contentType;
	String url;
	Date lastModified;
	Map<String, String> map = new HashMap<>();

	public void addMetadata(String title, String value) {
		map.put(title, value);
	}

	public void respondNotFound() {
		this.notFound = true;
	}

	public void setLastModified(Date dt) {
		this.lastModified = dt;
	}

	public void setDisplayUrl(URI uri) {
		this.url = uri.toString();
	}

	public void setContentType(String string) {
		this.contentType = string;
	}

	public void setContent(byte[] bytes) {
		this.content = bytes;
	}

	public String toJson() {
		JsonObject json = new JsonObject();
		json.addProperty("permalink", this.url);
		if (this.lastModified != null)
			json.addProperty("modificado", this.lastModified.toGMTString());
		for (String key : map.keySet())
			json.addProperty(key, map.get(key));
		json.addProperty("conteudo", this.getText());
		return json.toString();
	}

	private String getText() {
		if ("text/html".equals(this.contentType))
			return HtmlUtils.getText(this.content);
		if ("application/pdf".equals(this.contentType))
			return PdfUtils.getText(this.content);
		return null;
	}
}
