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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.service.ExService;
import br.gov.jfrj.siga.parser.PessoaLotacaoParser;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.model.WfConhecimento;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeDesvio;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeVariavel;
import br.gov.jfrj.siga.wf.model.WfTarefa;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeTarefa;

/**
 * Classe que repesenta um View Object (Objeto Visão, ou seja, objeto que será
 * usado na exibição da interface do usuário) de uma tarefa.
 * 
 */
public class WfTaskVO {
	public static final String DISABLE_DOC_FORWARD = "disable_doc_forward";

	protected WfTarefa ti;

	protected List<WfVariableVO> variableList = new ArrayList<WfVariableVO>();

	protected Set<WfTransitionVO> transitions = new TreeSet<WfTransitionVO>();

	private String descricao = null;

	private boolean conhecimentoEditavel = false;

	private String conhecimento = null;

	private String msgAviso = "";

	private List<String> tags;

	private String ancora;

	/**
	 * Construtor privado, impedindo a instanciação sem informar a tarefa.
	 */
	private WfTaskVO() {
	};

	/**
	 * Contrutor padrão (default)
	 * 
	 * @param ti
	 */
	public WfTaskVO(WfTarefa ti) {
		this.ti = ti;
	}

	public WfTaskVO(WfTarefa ti, DpPessoa titular, DpLotacao lotaTitular)
			throws IllegalAccessException, InvocationTargetException, Exception, AplicacaoException {
		this(ti, null, titular, lotaTitular);
	}

	public WfTaskVO(WfTarefa ti, String siglaDoc, DpPessoa titular, DpLotacao lotaTitular)
			throws IllegalAccessException, InvocationTargetException, Exception, AplicacaoException {
		this.ti = ti;

		WfConhecimento c = WfDao.getInstance().consultarConhecimento(ti.getDefinicaoDeTarefa().getId());
		if (c != null) {
			this.setDescricao(WfWikiParser.renderXHTML(c.getDescricao()));
			this.setConhecimento(c.getDescricao());
		}

		ExService service = Service.getExService();

		String siglaTitular = titular.getSigla() + "@" + lotaTitular.getSiglaCompleta();

		String respWF = null;
		if (ti.getInstanciaDeProcesso().getPessoa() != null)
			respWF = ti.getInstanciaDeProcesso().getPessoa().getSigla();
		if (respWF == null && ti.getInstanciaDeProcesso().getLotacao() != null)
			respWF = "@" + ti.getInstanciaDeProcesso().getLotacao().getSiglaCompleta();

		// O ideal é que seja desconsiderada apenas a variavel cujo valor seja
		// igual a siglaDoc, e não todas que começam com doc_
		if (ti.getDefinicaoDeTarefa().getVariable() != null && ti.getDefinicaoDeTarefa().getVariable().size() > 0) {
			List<WfVariableVO> variableAccesses = new ArrayList<>();

			for (WfDefinicaoDeVariavel vd : ti.getDefinicaoDeTarefa().getVariable()) {
//				if (va.getAccess() != null) {
//					String access = va.getAccess().toString();
//					if (access.contains("value=")) {
//						Pattern pattern = Pattern.compile("value\\=(\\{[^}]+\\})");
//						Matcher matcher = pattern.matcher(access);
//						if (matcher.find()) {
//							String expression = matcher.group(1);
//							Object resultado = WfHandler.eval(ti.getInstanciaDeProcesso(), expression);
//							ti.getInstanciaDeProcesso().getVariable().put(va.getMappedName(), resultado);
//							// TODO: Salvar o PI?
//						}
//					}

				WfVariableVO wfva = new WfVariableVO(vd);
				if (wfva.getMappedName().startsWith("doc_")) {
					String documento = (String) ti.getInstanciaDeProcesso().getVariable().get(wfva.getMappedName());
					if (documento != null) {
						if (siglaDoc == null || !documento.startsWith(siglaDoc)) {
							this.variableList.add(wfva);
						} else {
							if (wfva != null)
								this.variableList.add(wfva);
							setMsgAviso("");
							// if (!service.isAtendente(value, siglaTitular)) {
							String respEX = service.getAtendente(documento, siglaTitular);
							DpLotacao lotEX = new PessoaLotacaoParser(respEX).getLotacaoOuLotacaoPrincipalDaPessoa();
							// if (lotEX != null &&
							// !lotaTitular.equivale(lotEX))
							// wfva.setRespEX(lotEX.getSigla());

							DpLotacao lotWF = new PessoaLotacaoParser(respWF).getLotacaoOuLotacaoPrincipalDaPessoa();
							// if (lotWF != null &&
							// !lotaTitular.equivale(lotWF))
							// wfva.setRespWF(lotWF.getSigla());

							// if (wfva.isAviso())
							// }

							boolean podeMovimentar = service.podeMovimentar(documento, siglaTitular);
							boolean estaComTarefa = titular.equivale(new PessoaLotacaoParser(respWF).getPessoa());
							respEX = service.getAtendente(documento, siglaTitular);
							lotEX = new PessoaLotacaoParser(respEX).getLotacaoOuLotacaoPrincipalDaPessoa();

							if (podeMovimentar && !estaComTarefa) {
								if (lotWF == null || !lotWF.equivale(lotEX)) {
									setMsgAviso("Esta tarefa só poderá prosseguir quando o documento " + documento
											+ " for transferido para " + (lotWF == null ? "Nula" : lotWF.getSigla())
											+ ".");
								}
							}
							if (!podeMovimentar && estaComTarefa) {
								setMsgAviso("Esta tarefa só poderá prosseguir quando o documento " + documento
										+ ", que está com " + lotEX.getSigla() + ", for devolvido.");
							}
							if (!podeMovimentar && !estaComTarefa) {
								if (lotWF != null && !lotWF.equivale(lotEX)) {
									setMsgAviso("Esta tarefa só poderá prosseguir quando o documento " + documento
											+ ", que está com " + lotEX.getSigla() + ", for transferido para "
											+ lotWF.getSigla() + ".");
								}
							}

						}
					}
				}
			}
		}
		if (ti.getDefinicaoDeTarefa().getDetour() != null && ti.getDefinicaoDeTarefa().getDetour().size() > 0) {
			Set<String> set = new TreeSet<String>();

			for (WfDefinicaoDeDesvio dd : ti.getDefinicaoDeTarefa().getDetour()) {
				set.clear();
				WfDefinicaoDeTarefa tdProxima = dd.getSeguinte();
				boolean ultimo = false;
				WfDefinicaoDeDesvio desvio = null;
				while (tdProxima != null) {
					if (tdProxima.getTipoDeTarefa() == WfTipoDeTarefa.FORMULARIO)
						break;
					desvio = null;
					if (tdProxima.getDefinicaoDeDesvio() != null && tdProxima.getDefinicaoDeDesvio().size() == 1)
						desvio = tdProxima.getDefinicaoDeDesvio().get(0);

					// A proxima tarefa está indicada
					if (tdProxima.getSeguinte() != null)
						tdProxima = tdProxima.getSeguinte();
					else if (desvio != null && desvio.getSeguinte() != null)
						tdProxima = desvio.getSeguinte();

					// Depois dessa tarefa vai terminar
					else if (tdProxima.isUltimo() || (desvio != null && desvio.getUltimo())
							|| ti.getInstanciaDeProcesso().getCurrentIndex() == ti.getInstanciaDeProcesso()
									.getProcessDefinition().getTaskDefinition().size() - 1) {
						ultimo = true;
						tdProxima = null;
						break;
					}

					// Ou vai para a tarefa seguinte
					else
						tdProxima = (WfDefinicaoDeTarefa) ti.getInstanciaDeProcesso().getProcessDefinition()
								.getTaskDefinition().get(ti.getInstanciaDeProcesso().getCurrentIndex() + 1);
				}

				if (ultimo)
					set.add("FIM");
				else {
					WfResp r = (WfResp) ti.getInstanciaDeProcesso().calcResponsible(tdProxima);
					if (r != null)
						set.add(r.getInitials());
				}

				// Será que já temos em "set" uma lista das pessoas responsáveis
				// pelo próximo passo?
				// nextNode deve pular para o próximo se não for um tasknode e
				// se só tiver uma saida

				this.transitions.add(new WfTransitionVO(dd, set));
			}

		}

		tags = new ArrayList<String>();
		if (ti.getInstanciaDeProcesso().getProcessDefinition() != null)
			tags.add("@" + Texto.slugify(ti.getInstanciaDeProcesso().getProcessDefinition().getNome(), true, true));
		if (ti.getDefinicaoDeTarefa().getNome() != null)
			tags.add("@" + Texto.slugify(ti.getDefinicaoDeTarefa().getNome(), true, true));

		if (ti.getInstanciaDeProcesso().getProcessDefinition().getNome() != null
				&& ti.getDefinicaoDeTarefa().getNome() != null)
			ancora = "^wf:" + Texto.slugify(ti.getInstanciaDeProcesso().getProcessDefinition().getNome() + "-"
					+ ti.getDefinicaoDeTarefa().getNome(), true, true);

	}

	/**
	 * Retorna o objeto WfInstanciaDeTarefa.
	 * 
	 * @return
	 */
	public WfTarefa getWfInstanciaDeTarefa() {
		return ti;
	}

	/**
	 * Define o objeto WfInstanciaDeTarefa.
	 * 
	 * @param ti
	 */
	public void setWfInstanciaDeTarefa(WfTarefa ti) {
		this.ti = ti;
	}

	/**
	 * Retorna a lista de variáveis.
	 * 
	 * @return
	 */
	public List<WfVariableVO> getVariableList() {
		return variableList;
	}

	/**
	 * Define a lista de variáveis.
	 * 
	 * @param variables
	 */
	public void setVariableList(List<WfVariableVO> variables) {
		this.variableList = variables;
	}

	/**
	 * Retorna as transições.
	 * 
	 * @return
	 */
	public Set<WfTransitionVO> getTransitions() {
		return transitions;
	}

	/**
	 * Define as transições.
	 * 
	 * @param transitions
	 */
	public void setTransitions(Set<WfTransitionVO> transitions) {
		this.transitions = transitions;
	}

	/**
	 * Retorna a descrição da tarefa.
	 * 
	 * @return
	 */
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isConhecimentoEditavel() {
		return this.conhecimentoEditavel;
	}

	public void setConhecimentoEditavel(boolean conhecimentoEditavel) {
		this.conhecimentoEditavel = conhecimentoEditavel;
	}

	public String getConhecimento() {
		return conhecimento;
	}

	public void setConhecimento(String conhecimento) {
		this.conhecimento = conhecimento;
	}

	public void setMsgAviso(String msgAviso) {
		this.msgAviso = msgAviso;
	}

	public String getMsgAviso() {
		return msgAviso;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getAncora() {
		return ancora;
	}

	public void setAncora(String ancora) {
		this.ancora = ancora;
	}

	public String getCadastrante() {
		return (String) getWfInstanciaDeTarefa().getInstanciaDeProcesso().getVariable().get("wf_cadastrante");
	}

	public String getLotaCadastrante() {
		return (String) getWfInstanciaDeTarefa().getInstanciaDeProcesso().getVariable().get("wf_lota_cadastrante");
	}

	public String getTitular() {
		return (String) getWfInstanciaDeTarefa().getInstanciaDeProcesso().getVariable().get("wf_titular");
	}

	public String getLotaTitular() {
		return (String) getWfInstanciaDeTarefa().getInstanciaDeProcesso().getVariable().get("wf_lota_titular");
	}
}
