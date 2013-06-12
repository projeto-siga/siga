package reports;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import models.SrSolicitacao;

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
		List<SrSolicitacao> list = SrSolicitacao.findAll();

		// Query q = HibernateUtil.getSessao().createQuery(
		// "from SrSolicitacao");

		for (SrSolicitacao sol : list) {
			d.add("Renato");
			d.add("Renato");
			d.add("Renato");
			d.add("Renato");
			
//			d.add(sol.getSigla());
//			d.add(sol.descrSolicitacao);
//
//			d.add(sol.itemConfiguracao.descrItemConfiguracao);
//			d.add(sol.servico.descrServico);
		}
		return d;
	}

}
