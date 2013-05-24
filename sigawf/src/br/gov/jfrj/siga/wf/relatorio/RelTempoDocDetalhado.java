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

public class RelTempoDocDetalhado extends RelatorioTemplate {

	public RelTempoDocDetalhado(Map parametros) throws DJBuilderException {
		super(parametros);
		if (parametros.get("secaoUsuario") == null) {
			throw new DJBuilderException(
					"Parâmetro secaoUsuario não informado!");
		}
		if (parametros.get("nomeProcedimento") == null) {
			throw new DJBuilderException(
					"Parâmetro nomeProcedimento não informado!");
		}

		if (parametros.get("dataInicialDe") == null) {
			throw new DJBuilderException(
					"Parâmetro dataInicialDe não informado!");
		}
		if (parametros.get("dataInicialAte") == null) {
			throw new DJBuilderException(
					"Parâmetro dataInicialAte não informado!");
		}
		if (parametros.get("dataFinalDe") == null) {
			throw new DJBuilderException("Parâmetro dataFinalDe não informado!");
		}
		if (parametros.get("dataFinalAte") == null) {
			throw new DJBuilderException(
					"Parâmetro dataFinalAte não informado!");
		}
	}

	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException, JRException {

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
		Date dataInicialDe = getDataDe("dataInicialDe");
		Date dataInicialAte = getDataAte("dataInicialAte");
		Date dataFinalDe = getDataDe("dataFinalDe");
		Date dataFinalAte = getDataAte("dataFinalAte");

		Set<Tarefa> tarefas = consultarTarefas(procedimento, dataInicialDe,
				dataInicialAte, dataFinalDe, dataFinalAte);
		List<String> dados = new ArrayList<String>();
		for (Tarefa t : tarefas) {
			dados.add(t.getNumeroDocumento() + " ("
					+ t.getDuracaoProcedimento() + ")");
			dados.add(t.getNome());
			dados.add(DateFormat.getInstance().format(
					t.getDataInicio().getTime()));
			dados
					.add(DateFormat.getInstance().format(
							t.getDataFim().getTime()));
			dados.add(t.getDuracao().toString());

		}

		return dados;

	}

	private Set<Tarefa> consultarTarefas(String nomeProcedimento,
			Date dataInicialDe, Date dataInicialAte, Date dataFinalDe,
			Date dataFinalAte) {
		// ArrayList<Tarefa> tarefas = new ArrayList<Tarefa>();
		// Tarefa t1 = new Tarefa();
		// Tarefa t2 = new Tarefa();
		//
		// t1.setNome("t1");
		// t1.setDataFim("01/01/2000");
		// t1.setDataInicio("01/01/2000");
		// t1.setDuracao("5 seg");
		//
		// t2.setNome("t2");
		// t2.setDataFim("01/01/1999");
		// t2.setDataInicio("01/01/1888");
		// t2.setDuracao("5 seg");
		//
		// tarefas.add(t1);
		// tarefas.add(t2);
		// return tarefas;

		// String sql =
		// "SELECT PI.START_,PI.END_,VI.STRINGVALUE_,PI.ID_ FROM JBPM_PROCESSINSTANCE PI, (SELECT DISTINCT PROCESSINSTANCE_, STRINGVALUE_ FROM JBPM_VARIABLEINSTANCE WHERE NAME_ LIKE 'doc_%' AND STRINGVALUE_ LIKE '%-_' AND STRINGVALUE_ IS NOT NULL) VI, (SELECT ID_ FROM JBPM_PROCESSDEFINITION WHERE NAME_ = :nomeProcedimento) PD WHERE PI.PROCESSDEFINITION_=PD.ID_ AND PI.END_ IS NOT NULL AND PI.ID_ = VI.PROCESSINSTANCE_ AND  (PI.START_ >= :dataInicialDe and PI.START_ <= :dataInicialAte) AND (PI.END_ >= :dataFinalDe and PI.END_ <= :dataFinalAte)";

		String sql = "select pd.name_ pd_name,stringvalue_,p.start_ p_start,p.end_ p_end,t.name_ t_name,t.create_ t_create,t.end_ t_end "
				+ "from (select id_, stringvalue_, processinstance_ from sigawf.jbpm_variableinstance "
				+ "where stringvalue_ like '%RJ-SEC%' or stringvalue_ like '%RJ-EOF%' and name_ like 'doc_document')v ,(select id_,start_,end_, processdefinition_ "
				+ "from sigawf.jbpm_processinstance where end_ is not null )p, "
				+ "(select name_,procinst_,create_, end_ from sigawf.jbpm_taskinstance)t, "
				+ "(select name_ , id_ from sigawf.jbpm_processdefinition)pd "
				+ "where p.id_=v.processinstance_ and p.id_=t.procinst_ and p.processdefinition_ = pd.id_ "
				+ "and (p.start_ >= :dataInicialDe and p.start_ <= :dataInicialAte) and (p.end_ >= :dataFinalDe and p.end_ <= :dataFinalAte) "
				+ "and pd.name_ = :nomeProcedimento "
				+ "order by stringvalue_ ,create_";

		SQLQuery query = WfDao.getInstance().getSessao().createSQLQuery(sql);

		query.addScalar("stringvalue_", new StringType());
		query.addScalar("pd_name", new StringType());
		query.addScalar("p_start", new CalendarType());
		query.addScalar("p_end", new CalendarType());
		query.addScalar("t_name", new StringType());
		query.addScalar("t_create", new CalendarType());
		query.addScalar("t_end", new CalendarType());

		query.setString("nomeProcedimento", nomeProcedimento);
		query.setDate("dataInicialDe", dataInicialDe);
		query.setDate("dataInicialAte", dataInicialAte);
		query.setDate("dataFinalDe", dataFinalDe);
		query.setDate("dataFinalAte", dataFinalAte);

		List<Object[]> resultado = query.list();
		Set<Tarefa> tarefas = new TreeSet<Tarefa>(new TarefaComparator());
		for (Object[] o : resultado) {
			Tarefa t = new Tarefa();
			t.setDataInicioProcedimento((Calendar) o[2]);
			t.setDataFimProcedimento((Calendar) o[3]);
			t.setNumeroDocumento((String) o[0]);
			t.setNome((String) o[4]);
			t.setDataInicio((Calendar) o[5]);
			t.setDataFim((Calendar) o[6]);

			tarefas.add(t);

		}
		// Set<Doc> secs = new TreeSet<Doc>(new DocComparator());
		// for (Object[] o : resultado) {
		// Doc s = new Doc();
		// Calendar inicio = (Calendar) o[0];
		// Calendar fim = (Calendar) o[1];
		// s.setNumeroDoc(o[2].toString());
		// s.setInicio(inicio);
		// s.setFim(fim);
		// s.setProcessInstanceID((Long) (o[3]));
		// secs.add(s);
		// }
		//
		return tarefas;

	}

	private Date getDataDe(String data) {
		String pDT = (String) parametros.get(data);
		GregorianCalendar dt = new GregorianCalendar();

		dt.set(new Integer(pDT.substring(6)),
				new Integer(pDT.substring(3, 5)) - 1, new Integer(pDT
						.substring(0, 2)), 0, 0, 0);
		return dt.getTime();

	}

	/**
	 * Retorna um objeto Date com a data passada como parêmetro. ATENÇÃO: Este
	 * método pode ser reescrito com um DateFormat em um refactoring posterior.
	 * A data é definida como hh:mm:ss = 23:59:59
	 * 
	 * @param data
	 * @return
	 */
	private Date getDataAte(String data) {
		String pDT = (String) parametros.get(data);
		GregorianCalendar dt = new GregorianCalendar();

		dt.set(new Integer(pDT.substring(6)),
				new Integer(pDT.substring(3, 5)) - 1, new Integer(pDT
						.substring(0, 2)), 23, 59, 59);
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
			return getDataFim().getTimeInMillis()
					- getDataInicio().getTimeInMillis();
		}

		public long getDuracaoProcedimentoEmMili() {
			return getDataFimProcedimento().getTimeInMillis()
					- getDataInicioProcedimento().getTimeInMillis();
		}

		public String getDuracaoProcedimento() {
			return SigaCalendar.formatDHM(this.getDuracaoProcedimentoEmMili());
		}
	}

	/**
	 * Compara duas tarefas. Se as tarefas se referem a documentos diferentes, a
	 * ordem é do documento mais demorado para o mais rápido. Se as tarefas
	 * forem referentes ao mesmo documento, a ordem é pela tarefa que foi criada
	 * primeiro.
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
				if (t1.getDuracaoProcedimentoEmMili() > t2
						.getDuracaoProcedimentoEmMili()) {
					return -1;
				} else {
					return 1;
				}
			}

		}

	}
}
