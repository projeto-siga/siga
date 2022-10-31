package br.gov.jfrj.siga.model.enm;

import java.util.ArrayList;
import java.util.List;

public enum CpExtensoesDeArquivoEnum {
	AVI("avi", "video/avi"),
	//
	AVIF("avif", "image/avif"),
	//
	BMP("bmp", "image/bmp"),
	//
	DWG("dwg", "image/vnd.dwg"),
	//
	JPEG("jpeg", "image/jpeg"),
	//
	MOV("mov", "video/quicktime"),
	//
	MP4("mp4", "video/mp4"),
	//
	MPG("mpg", "video/mpeg"),
	//
	MPEG4("mpeg4", "video/mpeg4-generic"),
	//
	MP3("mp3", "audio/mpeg"),
	//
	PDF("pdf", "application/pdf"),
	//
	PNG("png", "image/png"),
	//
	PSD("psd", "image/vnd.adobe.photoshop"),
	//
	TIFF("tiff", "image/tiff"),
	//
	WAV("wav", "audio/wav"),
	//
	WMV("wmv", "video/x-ms-asf");

	private final String extensao;
	private final String tipoConteudo;

	private CpExtensoesDeArquivoEnum(String extensao, String tipoConteudo) {
		this.extensao = extensao;
		this.tipoConteudo = tipoConteudo;
	}

	public String getExtensao() {
		return extensao;
	}

	public static String getTipoConteudo(String parmExtensao) {
		for (CpExtensoesDeArquivoEnum item : values()) {
			if (item.extensao.equals(parmExtensao))
				return(item.tipoConteudo);
		}
		return null;
	}

	public static List<String> getList() {
		List<String> list = new ArrayList<String>();
		for (CpExtensoesDeArquivoEnum item : values()) {
			list.add(item.extensao);
		}
		return list;
	}

	public static boolean validaLista(String extensoes) {
		for (String extensao : extensoes.split(",")) {
			if (valida(extensao))
				return false;
		}
		return true;
	}

	public static boolean valida(String extensao) {
		for (CpExtensoesDeArquivoEnum item : values()) {
			if (item.equals(extensao))
				return true;
		}
		return false;
	}

}