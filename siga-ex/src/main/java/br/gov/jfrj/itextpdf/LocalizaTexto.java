package br.gov.jfrj.itextpdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import br.gov.jfrj.siga.base.AplicacaoException;

public class LocalizaTexto extends PDFTextStripper {
	public String[] seekA;
	private byte[] pdf;
	private List<LocalizaTextoResultado> l = new ArrayList<>();

	public LocalizaTexto(byte[] pdf, String[] seekA) throws IOException {
		super.setSortByPosition(true);
		this.pdf = pdf;
		this.seekA = seekA;
	}

	public static List<LocalizaTextoResultado> localizar(byte[] pdf, String[] seekA) {
		try {
			return new LocalizaTexto(pdf, seekA).localizar();
		} catch (Exception e) {
			return null;
		}
	}

	public List<LocalizaTextoResultado> localizar() throws Exception {
		l.clear();
		PDDocument document = null;
		try {
			document = PDDocument.load(this.pdf);
			if (document.isEncrypted())
				throw new AplicacaoException("Documento está encriptado com uma senha");

			writeText(document, new OutputStreamWriter(new ByteArrayOutputStream()));
//			y = document.getPage(page).getMediaBox().getHeight() - y;

		} finally {
			if (document != null) {
				document.close();
			}
		}
		return l;
	}

	@Override
	protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
		for (String s : seekA) {
			if (string.contains(s)) {
				LocalizaTextoResultado r = new LocalizaTextoResultado();
				TextPosition text = textPositions.get(0);
				r.text = string;
				r.seek = s;
				r.x = text.getXDirAdj();
				r.y = text.getYDirAdj();
				l.add(r);
			}
		}
	}

	public static class LocalizaTextoResultado {
		public String text;
		String seek;
		int page;
		float x;
		float y;
	}
}
