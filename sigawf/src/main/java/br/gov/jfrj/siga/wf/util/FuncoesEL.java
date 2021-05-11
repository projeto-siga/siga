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
package br.gov.jfrj.siga.wf.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeDesvio;

/**
 * Classe que contém diversas funcões úteis que podem ser utilizadas nos
 * arquivos jsp.
 * 
 * @author kpf
 * 
 */
public class FuncoesEL {

	static WfTarefaComparator tic = new WfTarefaComparator();

	/**
	 * Retorna as transições disponíveis em ordem alfabética.
	 * 
	 * @param taskInstance
	 * @return
	 */
	public static List<WfDefinicaoDeDesvio> ordenarTransicoes(WfTarefa taskInstance) {

		WfDefinicaoDeDesvio[] lista = new WfDefinicaoDeDesvio[taskInstance.getDefinicaoDeTarefa().getDefinicaoDeDesvio()
				.size()];
		taskInstance.getDefinicaoDeTarefa().getDefinicaoDeDesvio().toArray(lista);

		Arrays.sort(lista, new Comparator<WfDefinicaoDeDesvio>() {

			public int compare(WfDefinicaoDeDesvio o1, WfDefinicaoDeDesvio o2) {
				if (o1.getNome() == null) {
					return 1;
				}
				if (o2.getNome() == null) {
					return -1;
				}

				return o1.getNome().compareToIgnoreCase(o2.getNome());
			}

		});

		return Arrays.asList(lista);
	}

	/**
	 * Retorna a lista de tarefas em ordem decrescente de criação.
	 * 
	 * @return
	 */
//	public static List<TaskInstance> ordenarTarefas(TaskInstance taskInstance) {
//		TaskInstance[] lista = new TaskInstance[taskInstance.getTaskMgmtInstance().getTaskInstances().size()];
//		taskInstance.getTaskMgmtInstance().getTaskInstances().toArray(lista);
//
//		Arrays.sort(lista, new Comparator<TaskInstance>() {
//
//			public int compare(TaskInstance o1, TaskInstance o2) {
//				if (o1.getCreate().getTime() < o2.getCreate().getTime()) {
//					return 1;
//				}
//				if (o1.getCreate().getTime() > o2.getCreate().getTime()) {
//					return -1;
//				}
//				return 0;
//			}
//
//		});
//
//		return Arrays.asList(lista);
//	}

	/**
	 * Retorna a lista de tarefas em ordem decrescente de criação.
	 * 
	 * @return
	 */
//	public static List<Comment> ordenarComentarios(TaskInstance taskInstance) {
//		if (taskInstance.getComments() == null)
//			return new ArrayList(0);
//		Comment[] lista = new Comment[taskInstance.getComments().size()];
//		taskInstance.getComments().toArray(lista);
//
//		Arrays.sort(lista, new Comparator<Comment>() {
//
//			public int compare(Comment o1, Comment o2) {
//				if (o1.getTime().getTime() < o2.getTime().getTime()) {
//					return 1;
//				}
//				if (o1.getTime().getTime() > o2.getTime().getTime()) {
//					return -1;
//				}
//				return 0;
//			}
//
//		});
//
//		return Arrays.asList(lista);
//	}

	public static List<String> listarOpcoes(String nomeVariavel) {
		String op = nomeVariavel.substring(nomeVariavel.indexOf("(") + 1, nomeVariavel.length() - 1);
		return Arrays.asList(op.split(";"));
	}

	public static Boolean podePegarTarefa(DpPessoa cadastrante, DpPessoa titular, DpLotacao lotaCadastrante,
			DpLotacao lotaTitular, WfTarefa ti) {
		return Wf.getInstance().getBL().podePegarTarefa(cadastrante, titular, lotaCadastrante, lotaTitular, ti);
	}
}
