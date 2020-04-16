/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.siga.wf.relatorio;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sf.jasperreports.engine.JRException;

import org.hibernate.SQLQuery;
import org.hibernate.type.CalendarType;
import org.hibernate.type.StringType;

import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.siga.base.SigaCalendar;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeProcedimento;
import br.gov.jfrj.siga.wf.model.WfMov;
import br.gov.jfrj.siga.wf.model.WfMovTransicao;
import br.gov.jfrj.siga.wf.model.WfProcedimento;

public class RelTempoDocDetalhado extends RelatorioTemplate {

	public RelTempoDocDetalhado(Map parametros) throws DJBuilderException {
		super(parametros);
		if (parametros.get("secaoUsuario") == null) {
			throw new DJBuilderException("Parâmetro secaoUsuario não informado!");
		}
		if (parametros.get("nomeProcedimento") == null) {
			throw new DJBuilderException("Parâmetro nomeProcedimento não informado!");
		}

		if (parametros.get("dataInicialDe") == null) {
			throw new DJBuilderException("Parâmetro dataInicialDe não informado!");
		}
		if (parametros.get("dataInicialAte") == null) {
			throw new DJBuilderException("Parâmetro dataInicialAte não informado!");
		}
		if (parametros.get("dataFinalDe") == null) {
			throw new DJBuilderException("Parâmetro dataFinalDe não informado!");
		}
		if (parametros.get("dataFinalAte") == null) {
			throw new DJBuilderException("Parâmetro dataFinalAte não informado!");
		}
	}

	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio() throws DJBuilderException, JRException {

		this.setTitle("Tarefas por DOC");
		this.addColuna("Número do DOC", 20, RelatorioRapido.ESQUERDA, true);
		this.addColuna("Tarefa", 55, RelatorioRapido.ESQUERDA, false);
		this.addColuna("Início", 25, RelatorioRapido.CENTRO, false);
		this.addColuna("Fim", 25, RelatorioRapido.CENTRO, false);
		this.addColuna("Duração", 30, RelatorioRapido.CENTRO, false);

		return this;
	}

	@Override
	public Collection processarDados() {

		String procedimento = (String) parametros.get("nomeProcedimento");
		Long pdId = Long.parseLong((String) parametros.get("pdId"));
		Date dataInicialDe = getDataDe("dataInicialDe");
		Date dataInicialAte = getDataAte("dataInicialAte");
		Date dataFinalDe = getDataDe("dataFinalDe");
		Date dataFinalAte = getDataAte("dataFinalAte");
		Boolean incluirAbertos = parametros.get("incluirAbertos") == null ? false
				: Boolean.valueOf(parametros.get("incluirAbertos").toString().equals("on"));
		String grupoIni = parametros.get("inicioGrupo") == null ? "-1" : (String) parametros.get("inicioGrupo");
		String grupoFim = parametros.get("fimGrupo") == null ? "-1" : (String) parametros.get("fimGrupo");

		EstatisticaGrupoRel estatisticaGrp = new EstatisticaGrupoRel();
		WfDefinicaoDeProcedimento pd = WfDefinicaoDeProcedimento.AR.findById(pdId);
		Set<Tarefa> tarefas = consultarTarefas(pd, dataInicialDe, dataInicialAte, dataFinalDe, dataFinalAte,
				incluirAbertos);
		List<String> dados = new ArrayList<String>();

		String ultimoDoc = null;
		Tarefa ultimaTarefa = null;
		DetectorGrupoRel detectGrupo = null;
		List<Tarefa> grupoAtual = new ArrayList<RelTempoDocDetalhado.Tarefa>();
		int linhaInicioGrupo = -1;
		for (Tarefa t : tarefas) {

			if (ultimoDoc != null && !t.getNumeroDocumento().equals(ultimoDoc)) {
				addLinhaSumarioGrupo(estatisticaGrp, ultimaTarefa, dados);
			}

			// processamento inicial de grupo
			if (ultimoDoc == null || !t.getNumeroDocumento().equals(ultimoDoc)) {
				detectGrupo = new DetectorGrupoRel(grupoIni, grupoFim);
				grupoAtual.clear();
			}

			if (detectGrupo.fazParteDoGrupo(t.getNome()) && detectGrupo.isInicio()) {
				linhaInicioGrupo = dados.size();
				detectGrupo.continuar();
			}

			if (detectGrupo.fazParteDoGrupo(t.getNome())) {
				grupoAtual.add(t);
			}

			addLinha(dados, t);

			// processamento final de grupo
			if (detectGrupo.fazParteDoGrupo(t.getNome()) && detectGrupo.isFim()) {
				addLinhaEmBranco(t, dados, linhaInicioGrupo);
				addLinhaTotalGrupo(grupoAtual, dados);
				addLinhaEmBranco(t, dados);
				estatisticaGrp.addGrupo(grupoAtual);
				detectGrupo.reiniciarAvaliacao();
				grupoAtual.clear();
			}

			ultimoDoc = t.getNumeroDocumento();
			ultimaTarefa = t;
		}

		addLinhaFinal(estatisticaGrp, dados, ultimaTarefa);

		return dados;

	}

	private void addLinhaFinal(EstatisticaGrupoRel est, List<String> dados, Tarefa ultimaTarefa) {

		dados.add("Informações Gerais");

		dados.add("MEDIA GERAL [Grupos]");
		dados.add("");
		dados.add("");
		dados.add(est.getMediaGeralGrupos());

	}

	private void addLinhaSumarioGrupo(EstatisticaGrupoRel est, Tarefa t, List<String> dados) {
		String mediaGrupo = est.getMediaGrupo(t.getNumeroDocumento());
		if (!mediaGrupo.equals("0")) {
			addLinhaEmBranco(t, dados);

			dados.add(t.getNumeroDocumento() + " (" + t.getDuracaoProcedimento() + ")");

			dados.add("MEDIA [Grupos]");
			dados.add("");
			dados.add("");
			dados.add(est.getMediaGrupo(t.getNumeroDocumento()));
			addLinhaEmBranco(t, dados);

		}
	}

	private void addLinha(List<String> dados, Tarefa t) {
		dados.add(t.getNumeroDocumento() + " (" + t.getDuracaoProcedimento() + ")");
		dados.add(t.getNome());
		dados.add(DateFormat.getInstance().format(t.getDataInicio().getTime()));
		dados.add(t.getDataFim() == null ? "Em Andamento" : DateFormat.getInstance().format(t.getDataFim().getTime()));
		dados.add(t.getDuracao().toString());
	}

	private void addLinhaTotalGrupo(List<Tarefa> grupoAtual, List<String> dados) {
		long duracao = calcularDuracao(grupoAtual);

		dados.add(grupoAtual.get(0).getNumeroDocumento() + " (" + grupoAtual.get(0).getDuracaoProcedimento() + ")");
		dados.add("TOTAL [Grupo]");
		dados.add("");
		dados.add("");
		dados.add(SigaCalendar.formatDHM(duracao));
	}

	private long calcularDuracao(List<Tarefa> tarefas) {
		long duracao = 0;
		for (Tarefa t : tarefas) {
			duracao += t.getDuracaoEmMili();
		}
		return duracao;
	}

	private void addLinhaEmBranco(Tarefa t, List<String> dados, int posicao) {
		if (posicao <= -1) {
			posicao = dados.size();
		}
		dados.add(posicao, "");
		dados.add(posicao, "");
		dados.add(posicao, "");
		dados.add(posicao, "");
		dados.add(posicao, t.getNumeroDocumento() + " (" + t.getDuracaoProcedimento() + ")");

	}

	private void addLinhaEmBranco(Tarefa t, List<String> dados) {
		addLinhaEmBranco(t, dados, -1);
	}

	private Set<Tarefa> consultarTarefas(WfDefinicaoDeProcedimento pd, Date dataInicialDe, Date dataInicialAte,
			Date dataFinalDe, Date dataFinalAte, Boolean incluirAbertos) {

		List<WfProcedimento> l;
		if (incluirAbertos)
			l = WfDao.getInstance().consultarProcedimentosParaEstatisticasPorPrincipalInclusiveNaoFinalizados(pd,
					dataInicialDe, dataInicialAte, dataFinalDe, dataFinalAte);
		else
			l = WfDao.getInstance().consultarProcedimentosParaEstatisticasPorPrincipal(pd, dataInicialDe,
					dataInicialAte, dataFinalDe, dataFinalAte);

		Set<Tarefa> tarefas = new TreeSet<Tarefa>(new TarefaComparator());
		for (WfProcedimento pi : l) {
			Date dtLast = null;
			for (WfMov mov : pi.getMovimentacoes()) {
				if (!(mov instanceof WfMovTransicao))
					continue;
				WfMovTransicao t = (WfMovTransicao) mov;
				if (dtLast == null)
					dtLast = t.getHisDtIni();
				if (t.getDefinicaoDeTarefaDe() == null)
					continue;

				Tarefa tsk = new Tarefa();
				tsk.setDataInicioProcedimento(buildCalendar(pi.getHisDtIni()));
				tsk.setDataFimProcedimento(buildCalendar(pi.getHisDtFim()));
				tsk.setNumeroDocumento(pi.getPrincipal());
				tsk.setNome(t.getDefinicaoDeTarefaDe().getNome());
				tsk.setDataInicio(buildCalendar(t.getHisDtIni()));
				tsk.setDataFim(buildCalendar(dtLast));
				tarefas.add(tsk);

				dtLast = t.getHisDtIni();
			}
		}
		return tarefas;
	}

	private Calendar buildCalendar(Date data) {
		if (data == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		return c;
	}

	private Date getDataDe(String data) {
		String pDT = (String) parametros.get(data);
		GregorianCalendar dt = new GregorianCalendar();

		dt.set(new Integer(pDT.substring(6)), new Integer(pDT.substring(3, 5)) - 1, new Integer(pDT.substring(0, 2)), 0,
				0, 0);
		return dt.getTime();

	}

	/**
	 * Retorna um objeto Date com a data passada como parêmetro. ATENÇÃO: Este
	 * método pode ser reescrito com um DateFormat em um refactoring posterior. A
	 * data é definida como hh:mm:ss = 23:59:59
	 * 
	 * @param data
	 * @return
	 */
	private Date getDataAte(String data) {
		String pDT = (String) parametros.get(data);
		GregorianCalendar dt = new GregorianCalendar();

		dt.set(new Integer(pDT.substring(6)), new Integer(pDT.substring(3, 5)) - 1, new Integer(pDT.substring(0, 2)),
				23, 59, 59);
		return dt.getTime();

	}

	class Tarefa {
		Calendar dataInicio, dataFim;
		Calendar dataInicioProcedimento, dataFimProcedimento;
		String nome;
		String numeroDocumento;

		public String getNome() {
			return nome;
		}

		public Calendar getDataInicioProcedimento() {
			return dataInicioProcedimento;
		}

		public void setDataInicioProcedimento(Calendar dataInicioProcedimento) {
			this.dataInicioProcedimento = dataInicioProcedimento;
		}

		public Calendar getDataFimProcedimento() {
			return dataFimProcedimento;
		}

		public void setDataFimProcedimento(Calendar dataFimProcedimento) {
			this.dataFimProcedimento = dataFimProcedimento;
		}

		public String getNumeroDocumento() {
			return numeroDocumento;
		}

		public void setNumeroDocumento(String numeroDocumento) {
			this.numeroDocumento = numeroDocumento;
		}

		public String getDuracao() {
			return SigaCalendar.formatDHM(this.getDuracaoEmMili());
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		public Calendar getDataInicio() {
			return dataInicio;
		}

		public void setDataInicio(Calendar dataInicio) {
			this.dataInicio = dataInicio;
		}

		public Calendar getDataFim() {
			return dataFim;
		}

		public void setDataFim(Calendar dataFim) {
			this.dataFim = dataFim;
		}

		public long getDuracaoEmMili() {
			if (getDataFim() == null || getDataInicio() == null) {
				return Calendar.getInstance().getTimeInMillis() - getDataInicio().getTimeInMillis();
			}
			return getDataFim().getTimeInMillis() - getDataInicio().getTimeInMillis();
		}

		public long getDuracaoProcedimentoEmMili() {
			if (getDataFimProcedimento() == null || getDataInicioProcedimento() == null) {
				return Calendar.getInstance().getTimeInMillis() - getDataInicioProcedimento().getTimeInMillis();
			}

			return getDataFimProcedimento().getTimeInMillis() - getDataInicioProcedimento().getTimeInMillis();
		}

		public String getDuracaoProcedimento() {
			return SigaCalendar.formatDHM(this.getDuracaoProcedimentoEmMili());
		}

		@Override
		public String toString() {
			return getNumeroDocumento() + "," + getNome();
		}
	}

	/**
	 * Compara duas tarefas. Se as tarefas se referem a documentos diferentes, a
	 * ordem é do documento mais demorado para o mais rápido. Se as tarefas forem
	 * referentes ao mesmo documento, a ordem é pela tarefa que foi criada primeiro.
	 * 
	 * @author kpf
	 * 
	 */
	class TarefaComparator implements Comparator<Tarefa> {

		public int compare(Tarefa t1, Tarefa t2) {

			if (t1.getNumeroDocumento().equals(t2.getNumeroDocumento())) {

				if (t1.getDataInicio().before(t2.getDataInicio())) {
					return -1;
				} else {
					return 1;
				}
			} else {
				if (t1.getDuracaoProcedimentoEmMili() > t2.getDuracaoProcedimentoEmMili()) {
					return -1;
				} else {
					return 1;
				}
			}

		}

	}
}
