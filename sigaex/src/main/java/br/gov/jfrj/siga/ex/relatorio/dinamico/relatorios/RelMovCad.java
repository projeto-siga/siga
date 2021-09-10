package br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import net.sf.jasperreports.engine.JRException;

public class RelMovCad extends RelatorioTemplate {

	private DpLotacao lotacao;
	
	public RelMovCad(Map<String, String> parametros) throws Exception {
 		super(parametros);
		if (Utils.empty(parametros.get("secaoUsuario"))) {
			throw new AplicacaoException("Parâmetro secaoUsuario não informado!");
 		}
		if (Utils.empty(parametros.get("lotacao"))) {
			throw new AplicacaoException("Parâmetro lotação não informado!");
 		}
		if (Utils.empty(parametros.get("dataInicial"))) {
			throw new AplicacaoException("Parâmetro dataInicial não informado!");
 		}
		if (Utils.empty(parametros.get("dataFinal"))) {
			throw new AplicacaoException("Parâmetro dataFinal não informado!");
 		}
 		if (parametros.get("link_siga") == null) {
			throw new AplicacaoException("Parâmetro link_siga não informado!");
 		}
		this.lotacao = buscarLotacaoPor(Long.valueOf(parametros.get("lotacao")));
	}
	
	private DpLotacao buscarLotacaoPor(Long id) {
		CpDao dao = CpDao.getInstance();
		DpLotacao lotacao = dao.consultar(id, DpLotacao.class, false);
		return lotacao;
 	}
		
	public AbstractRelatorioBaseBuilder configurarRelatorio() throws DJBuilderException, JRException {
		//this.addAutoText(
		//		"Período: " + parametros.get("dataInicial").toString() + " a " + //parametros.get("dataFinal").toString(),
		//		AutoText.POSITION_HEADER, (byte) RelatorioRapido.ESQUERDA, 200);
		//this.setTitle("Relatório de Movimentações por Cadastrante");
		String titulo = "Relatório de Movimentações por Cadastrante de " + parametros.get("dataInicial").toString() + " a " + parametros.get("dataFinal").toString();
		String subtitulo = "Do(a) " + lotacao.getNomeLotacao();
		
		this.setTitle(titulo);
		this.setSubtitle(subtitulo);
		this.addColuna("Cadastrante", 100, RelatorioRapido.ESQUERDA, true);
		this.addColuna("Documento", 40, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Data da Movimentação", 50, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Descrição", 50, RelatorioRapido.ESQUERDA, false);
		return this;

	}

	@Override
	public Collection processarDados() throws Exception {

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		Query qryLotacaoTitular = ContextoPersistencia.em().createQuery(
				"from DpLotacao lot " + "where lot.dataFimLotacao is null "
						+ "and lot.orgaoUsuario = "
						+ parametros.get("orgaoUsuario")
						+ " and lot.siglaLotacao = '"
						+ parametros.get("lotacaoTitular") + "'");
		DpLotacao lotaTitular = (DpLotacao) qryLotacaoTitular.getSingleResult();

		DpPessoa titular = ExDao.getInstance().consultar(
				new Long((String) parametros.get("idTit")), DpPessoa.class,
				false);

		List<String> d = new ArrayList<String>(); // new LinkedList<String>();

		Query query = ContextoPersistencia.em()
				.createQuery(
						"select  mov, mob, doc "
								+ "from ExMovimentacao mov "
								+ "inner join mov.exMobil mob "
								+ "inner join mob.exDocumento doc "
								+ "where mob.idMobil=mov.exMobil.idMobil "
								+ "and mov.lotaCadastrante.idLotacao = :id "
								+ "and mov.dtIniMov >= :dtini "
								+ "and mov.dtIniMov <= :dtfim "
								+ "order by mov.cadastrante.idPessoaIni, mob.idMobil, mov.dtIniMov");

		query.setParameter("id",
				Long.valueOf((String) parametros.get("lotacao")));
		try {
			query.setParameter("dtini",
					formatter.parse((String) parametros.get("dataInicial")));
		} catch (ParseException e) {
			throw new AplicacaoException("Data Inicial inválida.", 0, e);
		}
		try {
			query.setParameter("dtfim",
					formatter.parse((String) parametros.get("dataFinal")));
		} catch (ParseException e) {
			throw new AplicacaoException("Data Final inválida.", 0, e);
		}

		Iterator it = query.getResultList().iterator();
		while (it.hasNext()) {
			Object[] obj = (Object[]) it.next();
			ExMovimentacao mov = (ExMovimentacao) obj[0];
			ExMobil mob = (ExMobil) obj[1];
			ExDocumento doc = (ExDocumento) obj[2];
			if (Ex.getInstance().getBL().exibirQuemTemAcessoDocumentosLimitados(
					doc, titular, 
							lotaTitular)) {
				if (mov.getCadastrante().getIdPessoa() != null) {
					d.add(mov.getCadastrante().getNomePessoa().toString());
				} else {
					d.add("");
				}
				if (mov.getExMobil().getSigla() != null) {
					d.add(mov.getExMobil().getSigla());
				} else {
					d.add("");
				}
				if (mov.getDtRegMovDDMMYYHHMMSS().toString() != null) {
					d.add(mov.getDtRegMovDDMMYYHHMMSS().toString());
				} else {
					d.add("");
				}
				if (mov.getDescrTipoMovimentacao() != null) {
					d.add(mov.getDescrTipoMovimentacao());
				} else {
					d.add("");
				}
			}
		}
		return d;
	}
}
