package br.gov.jfrj.siga.vraptor.builder;

import java.text.MessageFormat;

import br.gov.jfrj.siga.ex.ExMovimentacao;

public class ExDownloadZip extends ExInputStreamDownload {

	private ExMovimentacao mov;
	private String hash;

	public ExDownloadZip(ExMovimentacao mov, String hash) {
		super(MEDIA_TYPE_OCTET_STREAM, mov.getConteudoBlobMov2());
		this.mov = mov;
		this.hash = hash;
	}

	public ExDownloadZip(ExMovimentacao mov, String hash, String contentType) {
		super(contentType, mov.getConteudoBlobMov2());
		this.mov = mov;
		this.hash = hash;
	}

	@Override
	protected String getFileName() {
		String filename = mov.getNmArqMov();
		return hash != null ? MessageFormat.format("{0}.{1}", filename, hash.toLowerCase()) : filename;
	}
}
