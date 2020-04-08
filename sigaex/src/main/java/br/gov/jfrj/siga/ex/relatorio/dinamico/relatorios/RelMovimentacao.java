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
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import net.sf.jasperreports.engine.JRException;

public class RelMovimentacao extends RelatorioTemplate {

	public RelMovimentacao(Map parametros) throws DJBuilderException {
		super(parametros);
		if (parametros.get("secaoUsuario") == null) {
			throw new DJBuilderException(
					"Parâmetro secaoUsuario não informado!");
		}
		if (parametros.get("lotacaoTitular") == null) {
			throw new DJBuilderException("Parâmetro lotação não informado!");
		}
		if (parametros.get("dataInicial") == null) {
			throw new DJBuilderException("Parâmetro dataInicial não informado!");
		}
		if (parametros.get("dataFinal") == null) {
			throw new DJBuilderException("Parâmetro dataFinal não informado!");
		}
		if (parametros.get("link_siga") == null) {
			throw new DJBuilderException("Parâmetro link_siga não informado!");
		}
	}

	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException, JRException {
		this.addAutoText("Período: " + parametros.get("dataInicial").toString() + " a " + parametros.get("dataFinal").toString(), AutoText.POSITION_HEADER,(byte) RelatorioRapido.ESQUERDA,200);
		this.setTitle("Relatório de Movimentações");
		this.addColuna("Documento", 25, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Data de Movimento", 15, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Descrição", 15, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Responsável", 15, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Cadastrante", 15, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Subscritor", 15, RelatorioRapido.ESQUERDA, false);
		return this;

	}

	@Override
	public Collection processarDados() throws Exception {

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		List<String> d = new ArrayList<String>(); // new LinkedList<String>();

		Query query = ContextoPersistencia.em().createQuery ( "select  mov, mob, doc " +
		 "from ExMovimentacao mov " + "inner join mov.exMobil mob " +
		 "inner join mob.exDocumento doc " +
		 "where mob.idMobil=mov.exMobil.idMobil " +
		 "and (mov.lotaResp.idLotacao = :id or mov.lotaCadastrante.idLotacao = :id or mov.lotaSubscritor.idLotacao = :id) "
		 + "and mov.dtIniMov >= :dtini " + "and mov.dtIniMov <= :dtfim " +
		 "order by mov.dtIniMov, mov.exMobil.idMobil");
		 
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
			//ExMobil mob = (ExMobil) obj[1];
			//ExDocumento doc = (ExDocumento) obj[2];
			if (mov.getExMobil().getSigla()  != null) {
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
			if (mov.getLotaCadastrante().getSigla() != null) {
				d.add(mov.getLotaCadastrante().getSigla());
			} else {
				d.add("");
			}
			if (mov.getLotaResp().getSigla() != null) {
				d.add(mov.getLotaResp().getSigla());
			} else {
				d.add("");
			}
			if (mov.getLotaSubscritor().getSigla() != null) {
				d.add(mov.getLotaSubscritor().getSigla());
			} else {
				d.add("");
			}
		}
		return d;	}
}

