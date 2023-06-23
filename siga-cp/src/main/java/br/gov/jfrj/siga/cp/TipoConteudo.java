package br.gov.jfrj.siga.cp;

import java.util.HashMap;
import java.util.Map;

public enum TipoConteudo {
	
    ZIP("application/zip", "zip"),
    P7S("application/pkcs7-signature", "p7s"),
	JPG("image/jpeg", "jpg"),
	PNG("image/png", "png"),
	TXT("application/txt", "txt"),
	PLAIN("text/plain", "txt"),
	XML("text/xml", "xml"),
	FREEMARKER("template/freemarker", "txt"),
	JSP("template-file/jsp", "txt"),
	PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx"),
	XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx"),
	DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx"),
	OCTET("application/octet-stream", "octet-stream"),
	PDF("application/pdf", "pdf"),
    AVI("video/avi", "avi"),
    AVIF("image/avif", "avif"),
    BMP("image/bmp", "bmp"),
    DWG("image/vnd.dwg", "dwg"),
    MOV("video/quicktime", "mov"),
    MP4("video/mp4", "mp4"),
    MPEG("video/mpeg", "mpeg"),
    MPEG4("video/mpeg4-generic", "mpeg4"),
    MP3("audio/mpeg", "mp3"),
    PSD("image/vnd.adobe.photoshop", "psd"),
    TIFF("image/tiff", "tiff"),
    WAV("audio/wav", "wav"),
	X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded", "txt");
    
    
	
	private TipoConteudo(String mimeType ,String extensao) {
		this.mimeType = mimeType;
		this.extensao = extensao;
	}
	
	private String mimeType;
	private String extensao;
	
	private static Map<String, TipoConteudo> map = new HashMap<String, TipoConteudo>();
	
	static {
		for(TipoConteudo t : TipoConteudo.values())
			map.put(t.getMimeType(), t);
	}
	
	public static TipoConteudo getByMimeType(String mime) {
		return map.get(mime);
	}
	
	public String getMimeType() {
		return mimeType;
	}
	
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	public String getExtensao() {
		return extensao;
	}
	
	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}
}
