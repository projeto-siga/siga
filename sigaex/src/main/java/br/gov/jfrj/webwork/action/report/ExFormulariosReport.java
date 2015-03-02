package br.gov.jfrj.webwork.action.report;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.webwork.action.ExActionSupport;

public class ExFormulariosReport extends ExActionSupport {

	private Collection dataSource;

	private String secaoUsuario;

	private class ListItem implements Comparable {
		public String idForma;

		public String descricaoForma;

		public String idModelo;

		public String nomeModelo;

		public String siglaClassificacao;

		public String siglaClassCriacaoVia;

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
	}

	@Override
	public String execute() throws Exception {
		// parameters.put("secaoUsuario", param("secaoUsuario"));
		secaoUsuario = getCadastrante().getOrgaoUsuario()
				.getDescricaoMaiusculas();

		List<ExModelo> l = dao().listarExModelos();

		SortedSet<ListItem> ll = new TreeSet<ListItem>();
		for (ExModelo o : l) {
			try {
				if (Ex.getInstance().getConf().podePorConfiguracao(
						getTitular(), getLotaTitular(), o,
						CpTipoConfiguracao.TIPO_CONFIG_CRIAR))
					ll.add(new ListItem(o));
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		setDataSource(ll);
		return gerarRelatorio("formularios");
	}

	public String gerarRelatorio(String arquivo) throws JRException, Exception {

		final String cam = getRequest().getRealPath(
				"/paginas/expediente/relatorios/");

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
		return SUCCESS;
	}

	public Collection getDataSource() {
		return dataSource;
	}

	public void setDataSource(Collection dataSource) {
		this.dataSource = dataSource;
	}

	public String getSecaoUsuario() {
		return secaoUsuario;
	}

	public void setSecaoUsuario(String secaoUsuario) {
		this.secaoUsuario = secaoUsuario;
	}

}
