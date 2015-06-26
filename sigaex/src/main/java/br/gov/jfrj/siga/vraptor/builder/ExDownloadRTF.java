package br.gov.jfrj.siga.vraptor.builder;

import java.text.MessageFormat;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.util.GeradorRTF;

public class ExDownloadRTF extends ExInputStreamDownload {

	private ExMobil mob;
	private String hash;
	
	public ExDownloadRTF(ExMobil mob, String hash) throws Exception {
		super(MEDIA_TYPE_OCTET_STREAM, toBytes(mob));
		this.mob = mob;
		this.hash = hash;
	}
	
	public ExDownloadRTF(ExMobil mob, String hash, String contentType) throws Exception {
		super(contentType, toBytes(mob));
		this.mob = mob;
		this.hash = hash;
	}

	private static byte[] toBytes(ExMobil mob) throws Exception {
		return new GeradorRTF().geraRTFFOP(mob.getDoc());
	}
	
	@Override
	protected String getFileName() {
		String filename = mob.doc().getCodigo() + ".rtf";
		return hash != null ? MessageFormat.format("{0}.{1}", filename, hash.toLowerCase()) : filename;
	}
}