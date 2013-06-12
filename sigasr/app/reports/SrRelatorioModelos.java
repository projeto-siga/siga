package reports;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

public class SrRelatorioModelos extends RelatorioTemplate {

	public SrRelatorioModelos(Map parametros) throws DJBuilderException {
		super(parametros);
	}

	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException {
		this.setTitle("Relatório de Modelos");
		this.addColuna("Forma", 27, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Modelo", 50, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Class Documental", 15, RelatorioRapido.CENTRO, false);
		this.addColuna("Class Criação", 15, RelatorioRapido.CENTRO, false);
		return this;
	}

	@Override
	public Collection processarDados() {

		List<String> d = new LinkedList<String>();
		Query q = HibernateUtil.getSessao().createQuery(
				"from ExModelo order by exFormaDocumento.descrFormaDoc");

		/*
		for (ExModelo modelo : (List<ExModelo>) q.list()) {
			if (modelo.getExFormaDocumento() != null) {
				d.add(modelo.getExFormaDocumento().getDescrFormaDoc());
			} else {
				d.add("");
			}
			if (modelo.getNmMod() != null) {
				d.add(modelo.getNmMod());
			} else {
				d.add("");
			}

			if (modelo.getExClassificacao() != null) {
				d.add(modelo.getExClassificacao().getSigla());
			} else {
				d.add("");
			}
			if (modelo.getExClassCriacaoVia() != null) {
				d.add(modelo.getExClassCriacaoVia().getSigla());
			} else {
				d.add("");
			}
		}
		*/
		return d;
	}

}
