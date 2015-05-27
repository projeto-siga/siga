package br.gov.jfrj.siga.vraptor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.bl.Ex;

@Resource
public class ExFormulariosReportController extends ExController {
	
	private static final String APPLICATION_PDF = "application/pdf";
	private static final String REPORT_NAME = "formularios";

	public ExFormulariosReportController(HttpServletRequest request,
			HttpServletResponse response, ServletContext context,
			Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, response, context, result, dao, so, em);
		
		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();
	}
	
	@Get("app/expediente/rel/formularios")
	public Download execute() throws Exception {
		List<ExModelo> l = dao().listarExModelos();
		
		SortedSet<ListItem> ll = new TreeSet<ListItem>();
		for (ExModelo o : l) {
			try {
				if (Ex.getInstance().getConf().podePorConfiguracao(
						getTitular(), getLotaTitular(), o,
						CpTipoConfiguracao.TIPO_CONFIG_CRIAR))
					ll.add(new ListItem(o));
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
		
		final InputStream inputStream = new ByteArrayInputStream(gerarRelatorio(REPORT_NAME, ll));
		return new InputStreamDownload(inputStream, APPLICATION_PDF, REPORT_NAME);
	}

	public byte[] gerarRelatorio(String arquivo, SortedSet<ListItem> ll) throws JRException, Exception {
		final Map<String, String> parametros = new HashMap<String, String>();
		final String cam = getContext().getRealPath("/WEB-INF/page/exRelatorio/");

		/*
		 * Here we compile our xml jasper template to a jasper file. Note: this
		 * isn't exactly considered 'good practice'. You should either use
		 * precompiled jasper files (.jasper) or provide some kind of check to
		 * make sure you're not compiling the file on every request. If you
		 * don't have to compile the report, you just setup your data source
		 * (eg. a List)
		 */
		final String sJRXml = cam + "/" + arquivo + ".xml";
		final String sJasper = cam + "/" + arquivo + ".jasper";
		
		try {
			JasperCompileManager.compileReportToFile(sJRXml, sJasper);
		} catch (Exception e) {
			throw new Exception("Erro ao criar um relatório", e);
		}
		
		parametros.put("secaoUsuario", getCadastrante().getOrgaoUsuario().getDescricaoMaiusculas());
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(ll);
		JasperPrint jp = JasperFillManager.fillReport(sJasper, parametros, beanColDataSource);
		
		return JasperExportManager.exportReportToPdf(jp);
	}
	
	
	public class ListItem implements Comparable<Object> {
		private String idForma;
		private String descricaoForma;
		private String idModelo;
		private String nomeModelo;
		private String siglaClassificacao;
		private String siglaClassCriacaoVia;

		ListItem(ExModelo o) {
			if (null != o.getExFormaDocumento()) {
				this.idForma = o.getExFormaDocumento().getId().toString();
				this.descricaoForma = o.getExFormaDocumento().getDescricao();
			}
			
			this.idModelo = o.getIdMod().toString();
			this.nomeModelo = o.getNmMod();
			this.siglaClassificacao = "";
			if (null != o.getExClassificacao())
				this.siglaClassificacao = o.getExClassificacao().getSigla();
			this.siglaClassCriacaoVia = "";
			if (null != o.getExClassCriacaoVia())
				this.siglaClassCriacaoVia = o.getExClassCriacaoVia().getSigla();
		}

		public int compareTo(Object o) {
			int i = 0;
			ListItem other = (ListItem) o;
			try {
				i = this.descricaoForma.compareTo(other.descricaoForma);
			} catch (RuntimeException e) {
			}
			if (i != 0)
				return i;
			try {
				i = this.nomeModelo.compareTo(other.nomeModelo);
			} catch (RuntimeException e) {
			}
			if (i != 0)
				return i;
			return i;
		}

		public String getIdForma() {
			return idForma;
		}

		public void setIdForma(String idForma) {
			this.idForma = idForma;
		}

		public String getDescricaoForma() {
			return descricaoForma;
		}

		public void setDescricaoForma(String descricaoForma) {
			this.descricaoForma = descricaoForma;
		}

		public String getIdModelo() {
			return idModelo;
		}

		public void setIdModelo(String idModelo) {
			this.idModelo = idModelo;
		}

		public String getNomeModelo() {
			return nomeModelo;
		}

		public void setNomeModelo(String nomeModelo) {
			this.nomeModelo = nomeModelo;
		}

		public String getSiglaClassCriacaoVia() {
			return siglaClassCriacaoVia;
		}

		public void setSiglaClassCriacaoVia(String siglaClassCriacaoVia) {
			this.siglaClassCriacaoVia = siglaClassCriacaoVia;
		}

		public String getSiglaClassificacao() {
			return siglaClassificacao;
		}

		public void setSiglaClassificacao(String siglaClassificacao) {
			this.siglaClassificacao = siglaClassificacao;
		}
	}
	
}
