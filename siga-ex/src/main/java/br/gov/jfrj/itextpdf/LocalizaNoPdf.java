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

public class LocalizaNoPdf extends PDFTextStripper {
	public String[] seekA;
	private byte[] pdf;
	private List<LocalizaResultado> l = new ArrayList<>();

	public LocalizaNoPdf(byte[] pdf, String[] seekA) throws IOException {
		super.setSortByPosition(true);
		this.pdf = pdf;
		this.seekA = seekA;
	}

	public static List<LocalizaResultado> localizar(byte[] pdf, String[] seekA) throws Exception {
		return new LocalizaNoPdf(pdf, seekA).localizar();
	}

	public List<LocalizaResultado> localizar() throws Exception {
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
				LocalizaResultado r = new LocalizaResultado();
				TextPosition text = textPositions.get(0);
				r.text = string;
				r.seek = s;
				r.x = text.getXDirAdj();
				r.y = text.getYDirAdj();
			}
		}
	}

	public static class LocalizaResultado {
		public String text;
		String seek;
		int page;
		float x;
		float y;
	}
}
