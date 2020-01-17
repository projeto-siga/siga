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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.SigaCalendar;
import br.gov.jfrj.siga.wf.bl.Wf;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Classe que representa o relatório estatístico de procedimento.
 * 
 * @author kpf
 * 
 */
public class RelEstatisticaProcedimento extends RelatorioTemplate {

	private Double percentualMediaTruncada = 5.0;

	/**
	 * Construtor que define os parâmetros que são obrigatórios para a construção do
	 * relatório.
	 * 
	 * @param parametros - Mapa contendo os parâmetros necessários para a construção
	 *                   do relatório. As Keys do Map são: secaoUsuario - Informa a
	 *                   seção judiciária a ser impressa no cabeçalho do relatório;
	 *                   nomeProcedimento - Nome do procedimento ao qual se refere o
	 *                   relatório; dataInicial e dataFinal - datas que definem o
	 *                   período em que os procedimentos foram encerrados.
	 * @throws DJBuilderException
	 */
	public RelEstatisticaProcedimento(Map parametros) throws DJBuilderException {
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
		if (parametros.get("percentualMediaTruncada") != null) {
			Double minMediaTruncada = null;
			Double maxMediaTruncada = null;
			try {
				percentualMediaTruncada = Double
						.valueOf(((String) parametros.get("percentualMediaTruncada")).replace(",", "."));

				minMediaTruncada = Wf.getInstance().getProp().getRelEstatGeraisMinMediaTrunc();
				maxMediaTruncada = Wf.getInstance().getProp().getRelEstatGeraisMaxMediaTrunc();

			} catch (Exception e) {
				throw new AplicacaoException("Não foi possível determinar a média truncada!");
			}

			if (percentualMediaTruncada < minMediaTruncada || percentualMediaTruncada > maxMediaTruncada) {
				throw new AplicacaoException(
						"A média truncada deve ser entre " + minMediaTruncada + " e " + maxMediaTruncada);
			}

		} else {
			throw new AplicacaoException("Informe o percentual da média truncada!");
		}

	}

	/**
	 * Configura o layout do relatório.
	 */
	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio() throws DJBuilderException, JRException {

		this.setTitle(parametros.get("nomeProcedimento") + " [iniciado(s) de " + parametros.get("dataInicialDe")
				+ " até " + parametros.get("dataInicialAte") + ", finalizado(s) de " + parametros.get("dataFinalDe")
				+ " até " + parametros.get("dataFinalAte") + "]");
		this.addColuna("Procedimento/Tarefa", 35, RelatorioRapido.CENTRO, false);
		this.addColuna("Concluídos", 15, RelatorioRapido.CENTRO, false);
		this.addColuna("Mín", 15, RelatorioRapido.CENTRO, false);
		this.addColuna("Max", 15, RelatorioRapido.CENTRO, false);
		this.addColuna("Méd", 15, RelatorioRapido.CENTRO, false);
		this.addColuna("Méd Truncada " + percentualMediaTruncada.toString().replace(".", ",") + "%", 15,
				RelatorioRapido.CENTRO, false);

		return this;
	}

	/**
	 * Pesquisa os dados no banco de dados, realiza os cálculos estatísticos e monta
	 * uma collection com os dados que serão apresentados no relatório.
	 */
	@Override
	public Collection processarDados() {

		// inicialização das variáveis
		String procedimento = (String) parametros.get("nomeProcedimento");

		Date dataInicialDe = getDataDe("dataInicialDe");
		Date dataInicialAte = getDataAte("dataInicialAte");
		Date dataFinalDe = getDataDe("dataFinalDe");
		Date dataFinalAte = getDataAte("dataFinalAte");

		GregorianCalendar calendario = new GregorianCalendar();
		List<String> dados = new ArrayList<String>();

		Map<String, Long> mapaMin = new HashMap<String, Long>();
		Map<String, Long> mapaMax = new HashMap<String, Long>();
		Map<String, Long> mapaMedia = new HashMap<String, Long>();
		Map<String, ArrayList<Long>> mapaAmostra = new HashMap<String, ArrayList<Long>>();
		Map<String, Long> mapaMedTrunc = new HashMap<String, Long>();
		Long mediaPI = 0L;
		Long medTruncPI = 0L;
		Long minPI = 0L;
		Long maxPI = 0L;

		Map<Long, Long> mapaAmostraPI = new HashMap<Long, Long>();
//
//		List<ProcessDefinition> listaPD = getProcessDefinitions(procedimento);
//
//		for (ProcessDefinition pd : listaPD) {
//
//			List<ProcessInstance> instanciasProcesso = getProcessInstances(pd);
//
//			for (ProcessInstance pi : instanciasProcesso) {
//				Collection<TaskInstance> instanciasTarefas = pi.getTaskMgmtInstance().getTaskInstances();
//
//				// se processo encerrado e dentro das datas informadas
//				if (pi.hasEnded() && iniciadoEntre(pi, dataInicialDe, dataInicialAte)
//						&& finalizadoEntre(pi, dataFinalDe, dataFinalAte)) {
//
//					Long duracaoPI = getDuracaoProcesso(pi);
//
//					// minPI
//					if (minPI == 0 || duracaoPI < minPI) {
//						minPI = duracaoPI;
//					}
//
//					// maxPI
//					if (maxPI == 0 || duracaoPI > maxPI) {
//						maxPI = duracaoPI;
//					}
//
//					// amostrasPI
//					mapaAmostraPI.put(pi.getId(), duracaoPI);
//
//					for (TaskInstance ti : instanciasTarefas) {
//
//						Long duracaoTarefa = getDuracaoTarefa(ti);
//
//						// min Tarefa
//						Long min = mapaMin.get(ti.getName());
//						if (min != null) {
//							if (duracaoTarefa < min) {
//								mapaMin.put(ti.getName(), duracaoTarefa);
//							}
//						} else {
//							mapaMin.put(ti.getName(), duracaoTarefa);
//						}
//
//						// max Tarefa
//						Long max = mapaMax.get(ti.getName());
//						if (max != null) {
//							if (duracaoTarefa > max) {
//								mapaMax.put(ti.getName(), duracaoTarefa);
//							}
//						} else {
//							mapaMax.put(ti.getName(), duracaoTarefa);
//						}
//
//						// amostras Tarefas
//						ArrayList<Long> listaAmostral = mapaAmostra.get(ti.getName());
//						if (listaAmostral != null) {
//							listaAmostral.add(duracaoTarefa);
//						} else {
//							listaAmostral = new ArrayList<Long>();
//							listaAmostral.add(duracaoTarefa);
//							mapaAmostra.put(ti.getName(), listaAmostral);
//						}
//					}
//				}
//
//			}
//
//		}

		// Estatísticas Processos
		Estatistica e = new Estatistica();
		ArrayList<Long> duracoesPI = new ArrayList<Long>(mapaAmostraPI.values());
		e.setArray(duracoesPI);

		Double media = e.getMediaAritmetica();
		mediaPI = media.longValue();

		Double medTrunc = e.getMediaAritmeticaTruncada(percentualMediaTruncada);
		medTruncPI = medTrunc.longValue();

		// Estatísticas tarefas
		for (String tarefa : mapaAmostra.keySet()) {
			ArrayList<Long> lista = mapaAmostra.get(tarefa);
			e.setArray(lista);
			Double mediaTarefa = e.getMediaAritmetica();
			mapaMedia.put(tarefa, mediaTarefa.longValue());

			Double medTruncTarefa = e.getMediaAritmeticaTruncada(percentualMediaTruncada);
			mapaMedTrunc.put(tarefa, medTruncTarefa.longValue());

		}

		// informações das tarefas
		String[] tarefasOrdenadas = getTarefasOrdenadas(mapaMedia);

		// insere dados do processo
		dados.add("Procedimento: " + procedimento + " [TOTAL]");
		dados.add(new Integer(mapaAmostraPI.size()).toString());
		dados.add(SigaCalendar.formatDHM(minPI));
		dados.add(SigaCalendar.formatDHM(maxPI));
		dados.add(SigaCalendar.formatDHM(mediaPI));

		dados.add(SigaCalendar.formatDHM(medTruncPI));

		// insere dados das tarefas
		for (int i = 0; i < tarefasOrdenadas.length; i++) {
			dados.add(tarefasOrdenadas[i]);
			dados.add(new Integer(mapaAmostra.get(tarefasOrdenadas[i]).size()).toString());
			dados.add(SigaCalendar.formatDHM(mapaMin.get(tarefasOrdenadas[i])));
			dados.add(SigaCalendar.formatDHM(mapaMax.get(tarefasOrdenadas[i])));
			dados.add(SigaCalendar.formatDHM(mapaMedia.get(tarefasOrdenadas[i])));

			dados.add(SigaCalendar.formatDHM(mapaMedTrunc.get(tarefasOrdenadas[i]).longValue()));

		}

		return dados;

	}

//	private boolean iniciadoEntre(ProcessInstance pi, Date dataInicialDe, Date dataInicialAte) {
//		return pi.getStart().after(dataInicialDe) && pi.getStart().before(dataInicialAte);
//	}
//
//	private boolean finalizadoEntre(ProcessInstance pi, Date dataFinalDe, Date dataFinalAte) {
//		return pi.getEnd().after(dataFinalDe) && pi.getEnd().before(dataFinalAte);
//	}
//
//	private List getProcessInstances(ProcessDefinition pd) {
//		return WfContextBuilder.getJbpmContext().getGraphSession().findProcessInstances(pd.getId());
//	}
//
//	private List getProcessDefinitions(String procedimento) {
//		return WfContextBuilder.getJbpmContext().getJbpmContext().getGraphSession()
//				.findAllProcessDefinitionVersions(procedimento);
//	}

	/**
	 * Retorna a lista de tarefas por ordem de média.
	 * 
	 * @param mapaMedia
	 * @return
	 */
	private String[] getTarefasOrdenadas(Map<String, Long> mapaMedia) {
		List<String> tarefasOrdenadas = new ArrayList<String>();
		Object[] valores = mapaMedia.values().toArray();
		Object[] nomes = mapaMedia.keySet().toArray();

		Long auxValores = null;
		String auxNomes = null;
		for (int i = 0; i < valores.length - 1; i++) {
			Long atual = (Long) valores[i];
			Long proximo = (Long) valores[i + 1];
			if (proximo > atual) {
				auxValores = (Long) valores[i + 1];
				valores[i + 1] = valores[i];
				valores[i] = auxValores;

				String nomeAtual = (String) nomes[i];
				String nomeProximo = (String) nomes[i + 1];
				auxNomes = (String) nomes[i + 1];
				nomes[i + 1] = nomes[i];
				nomes[i] = auxNomes;

				i = -1;
			}
		}

		String[] resultado = new String[nomes.length];
		int i = 0;
		for (Object o : nomes) {
			resultado[i] = (String) o;
			i++;
		}
		return resultado;
	}

	/**
	 * Retorna um objeto Date com a data passada como parêmetro. ATENÇÃO: Este
	 * método pode ser reescrito com um DateFormat em um refactoring posterior. A
	 * data é definida como hh:mm:ss = 00:00:00
	 * 
	 * @param data
	 * @return
	 */
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

	/**
	 * Retorna quanto tempo uma tarefa demou para ser concluída. O resultado é em
	 * milissegundos.
	 * 
	 * @param ti
	 * @return
	 */
//	private long getDuracaoTarefa(TaskInstance ti) {
//
//		SigaCalendar inicio = new SigaCalendar();
//		SigaCalendar fim = new SigaCalendar();
//
//		inicio.setTime(ti.getCreate());
//		fim.setTime(ti.getEnd());
//
//		long tInicio = inicio.getTimeInMillis();
//		long tFim = fim.getTimeInMillis();
//
//		return tFim - tInicio;
//	}

	/**
	 * Retorna quanto tempo um processo demorou para ser concluído. O resultado é em
	 * milissegundos.
	 * 
	 * @param pi
	 * @return
	 */
//	private long getDuracaoProcesso(ProcessInstance pi) {
//
//		SigaCalendar inicio = new SigaCalendar();
//		SigaCalendar fim = new SigaCalendar();
//
//		inicio.setTime(pi.getStart());
//		fim.setTime(pi.getEnd());
//
//		long tInicio = inicio.getTimeInMillis();
//		long tFim = fim.getTimeInMillis();
//
//		return tFim - tInicio;
//	}

	/**
	 * Utilizado para realizar testes no relatório. ATENÇÃO; Uma classe de teste
	 * (JUnit) deve ser criada para substituir este método.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Map<String, String> parametros = new HashMap<String, String>();
		parametros.put("dataInicial", "01/11/2009");
		parametros.put("dataFinal", "20/11/2009");
		parametros.put("secaoUsuario", "SJRJ");

		try {
			RelEstatisticaProcedimento rep = new RelEstatisticaProcedimento(parametros);
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

}
