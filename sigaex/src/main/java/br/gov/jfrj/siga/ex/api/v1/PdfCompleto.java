package br.gov.jfrj.siga.ex.api.v1;

import java.io.FileOutputStream;
import java.util.concurrent.Callable;

import com.crivano.swaggerservlet.SwaggerServlet;
import com.crivano.swaggerservlet.SwaggerUtils;

import br.gov.jfrj.itextpdf.Documento;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.base.log.RequestExceptionLogger;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

public class PdfCompleto implements Callable<String> {
	private String uuid;
	private String sigla;
	private boolean estampar;

	public PdfCompleto(String uuid, String sigla, boolean estampar) {
		super();
		this.uuid = uuid;
		this.sigla = sigla;
		this.estampar = estampar;
	}

	@Override
	public String call() throws Exception {
		String bufName = null;
		try (ApiContext ctx = new ApiContext(false)) {
			ExDao dao = ExDao.getInstance();

			final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
			filter.setSigla(sigla);
			ExMobil mob = (ExMobil) dao.consultarPorSigla(filter);

			// Consulta o processo para saber quais são os documentos a serem
			// concatenados

			// Cria um documento em diretório temporário para agregar os
			// diversos PDFs
			bufName = getBufName(uuid, sigla);
			FileOutputStream buf = new FileOutputStream(bufName);
			Documento.getDocumento(buf, this.uuid, mob, null, true, estampar, null, null);
		} catch (Exception ex) {
			SwaggerUtils.log(PdfCompleto.class).error(RequestExceptionLogger.simplificarStackTrace(ex));
		}
		return bufName;
	}

	public static String getBufName(String uuid, String sigla) {
		String dirTemp = SwaggerServlet.getProperty("upload.dir.temp");
		return dirTemp + "/" + Texto.slugify(sigla, true, false) + "-completo-" + uuid + ".pdf";
	}

}
