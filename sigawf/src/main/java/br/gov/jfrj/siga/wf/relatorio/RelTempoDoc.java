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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.view.JasperViewer;

import org.hibernate.SQLQuery;
import org.hibernate.cfg.Configuration;
import org.hibernate.type.CalendarType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.base.SigaCalendar;
import br.gov.jfrj.siga.cp.bl.CpAmbienteEnumBL;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.dao.WfDao;

/**
 * Classe que representa o relatório de docs que demoraram
 * 
 * @author kpf
 * 
 */
public class RelTempoDoc extends RelatorioTemplate {

	/**
	 * Construtor que define os parâmetros que são obrigatórios para a
	 * construção do relatório.
	 * 
	 * @param parametros
	 *            - Mapa contendo os parâmetros necessários para a construção do
	 *            relatório. As Keys do Map são: secaoUsuario - Informa a seção
	 *            judiciária a ser impressa no cabeçalho do relatório;
	 *            nomeProcedimento - Nome do procedimento ao qual se refere o
	 *            relatório; dataInicial e dataFinal - datas que definem o
	 *            período em que os procedimentos foram encerrados.
	 * @throws DJBuilderException
	 */
	public RelTempoDoc(Map parametros) throws DJBuilderException {
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

	/**
	 * Configura o layout do relatório.
	 */
	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException, JRException {

		this.setTitle(parametros.get("nomeProcedimento") + " [iniciado(s) de "
				+ parametros.get("dataInicialDe") + " até "
				+ parametros.get("dataInicialAte") + ", finalizado(s) de "
				+ parametros.get("dataFinalDe") + " até "
				+ parametros.get("dataFinalAte") + "]");
		this.addColuna("Documento", 25, RelatorioRapido.CENTRO, false);
		this.addColuna("Início", 25, RelatorioRapido.CENTRO, false);
		this.addColuna("Fim", 25, RelatorioRapido.CENTRO, false);
		this.addColuna("Tempo", 25, RelatorioRapido.CENTRO, false);

		return this;
	}

	/**
	 * Pesquisa os dados no banco de dados, realiza os cálculos e monta uma
	 * collection com os dados que serão apresentados no relatório.
	 */
	@Override
	public Collection processarDados() {

		// inicialização das variáveis
		String procedimento = (String) parametros.get("nomeProcedimento");
		Date dataInicialDe = getDataDe("dataInicialDe");
		Date dataInicialAte = getDataAte("dataInicialAte");
		Date dataFinalDe = getDataDe("dataFinalDe");
		Date dataFinalAte = getDataAte("dataFinalAte");
		Boolean incluirAbertos = parametros.get("incluirAbertos")==null?false:Boolean.valueOf(parametros.get("incluirAbertos").toString().equals("on"));

		Set<Doc> docs = consultarDocs(procedimento, dataInicialDe,
				dataInicialAte, dataFinalDe, dataFinalAte,incluirAbertos);
		List<String> dados = new ArrayList<String>();
		for (Doc s : docs) {
			dados.add(s.getNumeroDoc());
			dados.add(DateFormat.getInstance().format(s.getInicio().getTime()));
			dados.add(s.getFim()==null?"Em Andamento":DateFormat.getInstance().format(s.getFim().getTime()));
			dados.add(s.getDuracao());
		}
		return dados;

	}

	/**
	 * Retorna os docs no período indicado, ordenadas pelo tempo de
	 * demora, podendo estar ou não finalizados.
	 * 
	 * Exemplo da query:
	 * 
	 * SELECT PI.START_,PI.END_,VI.STRINGVALUE_,PI.ID_ FROM
	 * SIGAWF.JBPM_PROCESSINSTANCE PI, (SELECT DISTINCT PROCESSINSTANCE_,
	 * STRINGVALUE_ FROM SIGAWF.JBPM_VARIABLEINSTANCE WHERE NAME_ LIKE 'doc_%'
	 * AND STRINGVALUE_ LIKE '%-_' AND STRINGVALUE_ IS NOT NULL) VI, (SELECT *
	 * FROM SIGAWF.JBPM_PROCESSDEFINITION WHERE NAME_ = 'Contratação: fase de
	 * análise') PD WHERE PI.PROCESSDEFINITION_=PD.ID_ AND PI.END_ IS NOT NULL
	 * AND PI.ID_ = VI.PROCESSINSTANCE_ AND (PI.START_ >= To_Date('01/03/2011')
	 * and PI.START_ <= To_Date('31/03/2011')) AND (PI.END_ >=
	 * To_Date('01/03/2011') and PI.END_ <= To_Date('31/05/2011')) ;
	 * 
	 * 
	 * @param dataInicial
	 * @param dataFinal
	 * @param dataFinalAte
	 * @param dataFinalDe
	 * @param incluirAbertos 
	 * @return
	 */
	private Set<Doc> consultarDocs(String nomeProcedimento, Date dataInicialDe,
			Date dataInicialAte, Date dataFinalDe, Date dataFinalAte, Boolean incluirAbertos) {
		SQLQuery query = null;
		if (incluirAbertos){
			query = (SQLQuery) WfDao.getInstance().getSessao().createSQLQuery(getSQLConsultarDocumentosFinalizadosEAbertosNoPeriodo());
		}else{
			query = (SQLQuery) WfDao.getInstance().getSessao().createSQLQuery(getSQLConsultarDocumentosFinalizadosNoPeriodo());
		}

		query.addScalar("START_", new CalendarType());
		query.addScalar("END_", new CalendarType());
		query.addScalar("STRINGVALUE_", new StringType());
		query.addScalar("ID_", new LongType());

		dataInicialAte = inclusiveData(dataInicialAte);
		dataFinalAte = inclusiveData(dataFinalAte);
		
		query.setString("nomeProcedimento", nomeProcedimento);
		query.setDate("dataInicialDe", dataInicialDe);
		query.setDate("dataInicialAte", dataInicialAte);
		query.setDate("dataFinalDe", dataFinalDe);
		query.setDate("dataFinalAte", dataFinalAte);
		List<Object[]> resultado = query.list();
		Set<Doc> docs = new TreeSet<Doc>(new DocComparator());
		for (Object[] o : resultado) {
			Doc s = new Doc();
			Calendar inicio = (Calendar) o[0];
			Calendar fim = (Calendar) o[1];
			s.setNumeroDoc(o[2].toString());
			s.setInicio(inicio);
			s.setFim(fim);
			s.setProcessInstanceID((Long) (o[3]));
			docs.add(s);
		}

		return docs;
	}

	private String getSQLConsultarDocumentosFinalizadosEAbertosNoPeriodo() {
		return
		"SELECT " + 
		"PI.START_,PI.END_,VI.STRINGVALUE_,PI.ID_ " + 
	"FROM  " +
		"JBPM_PROCESSINSTANCE PI, " +
		"(SELECT DISTINCT PROCESSINSTANCE_, STRINGVALUE_ FROM JBPM_VARIABLEINSTANCE WHERE NAME_ LIKE 'doc_%' AND STRINGVALUE_ LIKE '%-_' AND STRINGVALUE_ IS NOT NULL) VI, " +
		"(SELECT ID_ FROM JBPM_PROCESSDEFINITION WHERE NAME_ = :nomeProcedimento) PD  " +
	"WHERE " +
		"PI.PROCESSDEFINITION_=PD.ID_ AND " + 
		"PI.ID_ = VI.PROCESSINSTANCE_ AND   " +
		"(PI.START_ >= :dataInicialDe and PI.START_ <= :dataInicialAte) AND " + 
		"(PI.END_ >= :dataFinalDe and PI.END_ <= :dataFinalAte OR PI.END_ IS NULL)";
	}

	private String getSQLConsultarDocumentosFinalizadosNoPeriodo() {
		return 
	"SELECT PI.START_,PI.END_,VI.STRINGVALUE_,PI.ID_ " +  
	"FROM " +
		"JBPM_PROCESSINSTANCE PI, " +
		"(SELECT DISTINCT PROCESSINSTANCE_, STRINGVALUE_ FROM JBPM_VARIABLEINSTANCE WHERE NAME_ LIKE 'doc_%' AND STRINGVALUE_ LIKE '%-_' AND STRINGVALUE_ IS NOT NULL) VI, " +
		"(SELECT ID_ FROM JBPM_PROCESSDEFINITION WHERE NAME_ = :nomeProcedimento) PD " + 
	"WHERE " +
		"PI.PROCESSDEFINITION_=PD.ID_ AND " + 
		"PI.END_ IS NOT NULL AND  " +
		"PI.ID_ = VI.PROCESSINSTANCE_ AND " +  
		"(PI.START_ >= :dataInicialDe and PI.START_ <= :dataInicialAte) AND " + 
		"(PI.END_ >= :dataFinalDe and PI.END_ <= :dataFinalAte) " ;
	}

	/**
	 * Soma um dia à data para que esta possa estar incluída na pesquisa.
	 * @param dataInicialAte
	 * @return
	 */
	private Date inclusiveData(Date data) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}

	/**
	 * Retorna um objeto Date com a data passada como parêmetro. ATENÇÃO: Este
	 * método pode ser reescrito com um DateFormat em um refactoring posterior.
	 * A data é definida como hh:mm:ss = 00:00:00
	 * 
	 * @param data
	 * @return
	 */
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

	/**
	 * Utilizado para realizar testes no relatório. ATENÇÃO; Uma classe de teste
	 * (JUnit) deve ser criada para substituir este método.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Map<String, String> parametros = new HashMap<String, String>();
		parametros.put("dataInicial", "01/01/2010");
		parametros.put("dataFinal", "01/01/2011");
		parametros.put("secaoUsuario", "SJRJ");

		try {
			CpAmbienteEnumBL ambiente = CpAmbienteEnumBL.DESENVOLVIMENTO;
			Wf.getInstance().getProp().setPrefixo(ambiente.getSigla());
			Configuration cfg = WfDao.criarHibernateCfg(ambiente);
//			CpDao.configurarHibernateParaDebug(cfg);
			HibernateUtil.configurarHibernate(cfg);

			RelTempoDoc rep = new RelTempoDoc(parametros);
			rep.gerar();
			JasperViewer.viewReport(rep.getRelatorioJasperPrint());
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DJBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class Doc {
		private String numeroDoc;
		private Calendar inicio;
		private Calendar fim;
		private long processInstanceID;

		public void setNumeroDoc(String numeroDoc) {
			this.numeroDoc = numeroDoc;
		}

		public String getNumeroDoc() {
			return numeroDoc;
		}

		public String getDuracao() {
			return SigaCalendar.formatDHM(this.getDuracaoEmMili());
		}

		public long getDuracaoEmMili() {
			if (getFim()==null || getInicio()==null){
				return Calendar.getInstance().getTimeInMillis() - getInicio().getTimeInMillis();
			}
			return getFim().getTimeInMillis() - getInicio().getTimeInMillis();
		}

		public void setProcessInstanceID(long processInstanceID) {
			this.processInstanceID = processInstanceID;
		}

		public long getProcessInstanceID() {
			return processInstanceID;
		}

		@Override
		public String toString() {
			return getNumeroDoc() + " (" + getDuracao() + ")";
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Doc)) {
				return false;
			} else {
				Doc s = (Doc) obj;
				return (this.getProcessInstanceID() == s.getProcessInstanceID())
						&& this.getNumeroDoc().equals(s.getNumeroDoc());
			}
		}

		public void setInicio(Calendar inicio) {
			this.inicio = inicio;
		}

		public Calendar getInicio() {
			return inicio;
		}

		public void setFim(Calendar fim) {
			this.fim = fim;
		}

		public Calendar getFim() {
			return fim;
		}

	}

	class DocComparator implements Comparator<Doc> {

		public int compare(Doc s1, Doc s2) {
			if (s1.getDuracaoEmMili() < s2.getDuracaoEmMili()) {
				return 1;
			}
			if (s1.getDuracaoEmMili() > s2.getDuracaoEmMili()) {
				return -1;
			}
			return 0;
		}

	}

}
