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
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.base.VO;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.service.ExService;
import br.gov.jfrj.siga.parser.PessoaLotacaoParser;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.model.WfConhecimento;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeDesvio;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeVariavel;
import br.gov.jfrj.siga.wf.model.enm.WfPrioridade;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeTarefa;

/**
 * Classe que repesenta um View Object (Objeto Visão, ou seja, objeto que será
 * usado na exibição da interface do usuário) de uma tarefa.
 * 
 */
public class WfTaskVO extends VO {
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

		if (ti.getDefinicaoDeTarefa() != null) {
			WfConhecimento c = WfDao.getInstance().consultarConhecimento(ti.getDefinicaoDeTarefa().getId());
			if (c != null) {
				this.setDescricao(WfWikiParser.renderXHTML(c.getDescricao()));
				this.setConhecimento(c.getDescricao());
			}

			String siglaTitular = titular.getSigla() + "@" + lotaTitular.getSiglaCompleta();

			String respWF = null;
			if (ti.getInstanciaDeProcedimento().getPessoa() != null)
				respWF = ti.getInstanciaDeProcedimento().getPessoa().getSigla();
			if (respWF == null && ti.getInstanciaDeProcedimento().getLotacao() != null)
				respWF = "@" + ti.getInstanciaDeProcedimento().getLotacao().getSiglaCompleta();

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
					this.variableList.add(wfva);
					wfva.setValor(
							(String) ti.getInstanciaDeProcedimento().getVariavelMap().get(wfva.getIdentificador()));

					if (wfva.getIdentificador().startsWith("doc_")) {
						String documento = (String) ti.getInstanciaDeProcedimento().getVariable()
								.get(wfva.getIdentificador());
						if (documento != null) {
							if (siglaDoc == null || !documento.startsWith(siglaDoc)) {
								this.variableList.add(wfva);
							} else {
								ExService service = Service.getExService();
								if (wfva != null)
									this.variableList.add(wfva);
								setMsgAviso("");
								// if (!service.isAtendente(value, siglaTitular)) {
								String respEX = service.getAtendente(documento, siglaTitular);
								DpLotacao lotEX = new PessoaLotacaoParser(respEX)
										.getLotacaoOuLotacaoPrincipalDaPessoa();
								// if (lotEX != null &&
								// !lotaTitular.equivale(lotEX))
								// wfva.setRespEX(lotEX.getSigla());

								DpLotacao lotWF = new PessoaLotacaoParser(respWF)
										.getLotacaoOuLotacaoPrincipalDaPessoa();
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
					boolean ultimo = dd.isUltimo();
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
						else if (tdProxima.isUltimo() || (desvio != null && desvio.isUltimo())
								|| ti.getInstanciaDeProcedimento().getCurrentIndex() == ti.getInstanciaDeProcedimento()
										.getProcessDefinition().getTaskDefinition().size() - 1) {
							ultimo = true;
							tdProxima = null;
							break;
						}

						// Ou vai para a tarefa seguinte
						else
							tdProxima = (WfDefinicaoDeTarefa) ti.getInstanciaDeProcedimento().getProcessDefinition()
									.getTaskDefinition().get(ti.getInstanciaDeProcedimento().getCurrentIndex() + 1);
					}

					if (ultimo)
						set.add("FIM");
					else {
						if (tdProxima != null) {
							WfResp r = (WfResp) ti.getInstanciaDeProcedimento().calcResponsible(tdProxima);
							if (r != null)
								set.add(r.getInitials());
						}
					}

					// Será que já temos em "set" uma lista das pessoas responsáveis
					// pelo próximo passo?
					// nextNode deve pular para o próximo se não for um tasknode e
					// se só tiver uma saida

					this.transitions.add(new WfTransitionVO(dd, set));
				}

			}
		}

		tags = new ArrayList<String>();
		if (ti.getInstanciaDeProcedimento().getProcessDefinition() != null)
			tags.add("@" + Texto.slugify(ti.getInstanciaDeProcedimento().getProcessDefinition().getNome(), true, true));
		if (ti.getDefinicaoDeTarefa() != null && ti.getDefinicaoDeTarefa().getNome() != null)
			tags.add("@" + Texto.slugify(ti.getDefinicaoDeTarefa().getNome(), true, true));

		if (ti.getInstanciaDeProcedimento().getProcessDefinition().getNome() != null
				&& ti.getDefinicaoDeTarefa() != null && ti.getDefinicaoDeTarefa().getNome() != null)
			ancora = "^wf:" + Texto.slugify(ti.getInstanciaDeProcedimento().getProcessDefinition().getNome() + "-"
					+ ti.getDefinicaoDeTarefa().getNome(), true, true);

		addAcoes(ti, titular, lotaTitular);
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
		return (String) this.ti.getInstanciaDeProcedimento().getVariable().get("wf_cadastrante");
	}

	public String getLotaCadastrante() {
		return (String) this.ti.getInstanciaDeProcedimento().getVariable().get("wf_lota_cadastrante");
	}

	public String getTitular() {
		return (String) this.ti.getInstanciaDeProcedimento().getVariable().get("wf_titular");
	}

	public String getLotaTitular() {
		return (String) this.ti.getInstanciaDeProcedimento().getVariable().get("wf_lota_titular");
	}

	public Long getId() {
		return this.ti.getInstanciaDeProcedimento().getId();
	}

	public String getSigla() {
		return this.ti.getInstanciaDeProcedimento().getSigla();
	}

	public String getNomeDoProcedimento() {
		return this.ti.getInstanciaDeProcedimento().getDefinicaoDeProcedimento().getNome();
	}

	public String getNomeDaTarefa() {
		if (this.ti.getDefinicaoDeTarefa() == null)
			return null;
		return this.ti.getDefinicaoDeTarefa().getNome();
	}

	public boolean isPodePegarTarefa() {
		// (titular.sigla eq taskInstance.actorId) or (wf:podePegarTarefa(cadastrante,
		// titular,lotaCadastrante,lotaTitular,taskInstance))
		return false;
	}

	public WfPrioridade getPrioridade() {
		return this.ti.getInstanciaDeProcedimento().getPrioridade();
	}

	public Date getInicio() {
		return this.ti.getInstanciaDeProcedimento().getDtEvento();
	}

	public WfDefinicaoDeTarefa getDefinicaoDeTarefa() {
		return this.ti.getDefinicaoDeTarefa();
	}

	@Override
	public void addAcao(String icone, String nome, String nameSpace, String action, boolean pode, String msgConfirmacao,
			String parametros, String pre, String pos, String classe, String modal) {
		if (parametros == null)
			parametros = "id=" + this.ti.getInstanciaDeProcedimento().getId();
		else
			parametros += "&id=" + this.ti.getInstanciaDeProcedimento().getId();
		super.addAcao(icone, nome, nameSpace, action, pode, msgConfirmacao, parametros, pre, pos, classe, modal);
	}

	private void addAcoes(WfTarefa ti, DpPessoa titular, DpLotacao lotaTitular) {
		addAcao("note_add", "_Anotar", "/app", "anotar",
				// Ex.getInstance().getComp().podeFazerAnotacao(titular, lotaTitular, ti)
				true, null, null, null, null, null, "anotarModal");
	}
}
