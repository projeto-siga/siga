package br.gov.jfrj.siga.ex.api.v1;

import java.io.FileOutputStream;
import java.util.concurrent.Callable;

import com.crivano.swaggerservlet.SwaggerUtils;

import br.gov.jfrj.itextpdf.Documento;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.log.RequestExceptionLogger;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class DownloadAssincrono implements Callable<String> {
	private String uuid;
	private String contenttype;
	private String sigla;
	private boolean estampar;
	private boolean volumes;
	private String contextpath;
	String servernameport;
	private boolean exibirReordenacao;

	public DownloadAssincrono(String uuid, String contenttype, String sigla, boolean estampar, boolean volumes,
			String contextpath, String servernameport, boolean exibirReordenacao) {
		super();
		this.uuid = uuid;
		this.contenttype = contenttype;
		this.sigla = sigla;
		this.estampar = estampar;
		this.volumes = volumes;
		this.contextpath = contextpath;
		this.servernameport = servernameport;
		this.exibirReordenacao = exibirReordenacao;
	}

	@Override
	public String call() throws Exception {
		String bufName = null;
		try (ExApiV1Context ctx = new ExApiV1Context()) {
			ctx.init(null);
			ExDao dao = ExDao.getInstance();

			final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
			filter.setSigla(sigla);
			ExMobil mob = (ExMobil) dao.consultarPorSigla(filter);
			mob.getMobilPrincipal().indicarSeDeveExibirDocumentoCompletoReordenado(exibirReordenacao);

			// Consulta o processo para saber quais são os documentos a serem
			// concatenados

			// Cria um documento em diretório temporário para agregar os
			// diversos PDFs
			bufName = getBufName(uuid, contenttype, sigla);
			FileOutputStream buf = new FileOutputStream(bufName);
			if ("text/html".equals(this.contenttype))
				Documento.getDocumentoHTML(buf, this.uuid, mob, null, true, volumes, this.contextpath,
						this.servernameport);
			else
				Documento.getDocumento(buf, this.uuid, mob, null, true, estampar, volumes, null, null);
		} catch (Exception ex) {
			SwaggerUtils.log(DownloadAssincrono.class).error(RequestExceptionLogger.simplificarStackTrace(ex));
		}
		return bufName;
	}

	public static String getBufName(String uuid, String contenttype, String sigla) {
		String dirTemp = Prop.get("upload.dir.temp");
		return dirTemp + "/" + Texto.slugify(sigla, true, false) + "-completo-" + uuid
				+ ("text/html".equals(contenttype) ? ".html" : ".pdf");
	}

}