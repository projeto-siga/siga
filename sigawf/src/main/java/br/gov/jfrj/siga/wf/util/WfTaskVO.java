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

import com.crivano.jlogic.Expression;

import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.base.VO;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.service.ExService;
import br.gov.jfrj.siga.parser.PessoaLotacaoParser;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.logic.PodeSim;
import br.gov.jfrj.siga.wf.logic.WfPodePegar;
import br.gov.jfrj.siga.wf.model.WfConhecimento;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeDesvio;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeTarefa;
import br.gov.jfrj.siga.wf.model.WfDefinicaoDeVariavel;
import br.gov.jfrj.siga.wf.model.WfProcedimento;
import br.gov.jfrj.siga.wf.model.enm.WfPrioridade;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDePrincipal;

/**
 * Classe que repesenta um View Object (Objeto Visão, ou seja, objeto que será
 * usado na exibição da interface do usuário) de uma tarefa.
 * 
 */
public class WfTaskVO extends VO {
	public static final String DISABLE_DOC_FORWARD = "disable_doc_forward";

	protected WfTarefa ti;

	private String descricao = null;

	private boolean conhecimentoEditavel = false;

	private String conhecimento = null;

	private String msgAviso = null;

	private boolean desabilitarForm;

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

			if (!Utils.empty(ti.getInstanciaDeProcedimento().getPrincipal())
					&& ti.getInstanciaDeProcedimento().getTipoDePrincipal() == WfTipoDePrincipal.DOC) {
				ExService service = Service.getExService();
				// if (!service.isAtendente(value, siglaTitular)) {
				String respEX = service.getAtendente(ti.getInstanciaDeProcedimento().getPrincipal(), siglaTitular);
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
				boolean podeMovimentar = service.podeMovimentar(ti.getInstanciaDeProcedimento().getPrincipal(),
						siglaTitular);
				boolean estaComTarefa = titular.equivale(new PessoaLotacaoParser(respWF).getPessoa());
				respEX = service.getAtendente(ti.getInstanciaDeProcedimento().getPrincipal(), siglaTitular);
				lotEX = new PessoaLotacaoParser(respEX).getLotacaoOuLotacaoPrincipalDaPessoa();

				if (podeMovimentar && !estaComTarefa) {
					if (lotWF == null || !lotWF.equivale(lotEX)) {
						setMsgAviso("Esta tarefa só poderá prosseguir quando o documento "
								+ ti.getInstanciaDeProcedimento().getPrincipal() + " for transferido para "
								+ (lotWF == null ? "Nula" : lotWF.getSigla()) + ".");
					}
				}
				if (!podeMovimentar && estaComTarefa) {
					setMsgAviso("Esta tarefa só poderá prosseguir quando o documento "
							+ ti.getInstanciaDeProcedimento().getPrincipal() + ", que está com " + lotEX.getSigla()
							+ ", for devolvido.");
				}
				if (!podeMovimentar && !estaComTarefa) {
					if (lotWF != null && !lotWF.equivale(lotEX)) {
						setMsgAviso("Esta tarefa só poderá prosseguir quando o documento "
								+ ti.getInstanciaDeProcedimento().getPrincipal() + ", que está com " + lotEX.getSigla()
								+ ", for transferido para " + lotWF.getSigla() + ".");
					}
				}
			}

		}

		if (!titular.equivale(ti.getInstanciaDeProcedimento().getPessoa())
				&& !lotaTitular.equivale(ti.getInstanciaDeProcedimento().getLotacao())) {
			setDesabilitarForm(true);
			if (ti.getInstanciaDeProcedimento().getPessoa() != null
					&& ti.getInstanciaDeProcedimento().getLotacao() != null)
				setMsgAviso("Esta tarefa será desempenhada por " + titular.getSigla() + " na lotação "
						+ ti.getInstanciaDeProcedimento().getLotacao().getSigla());
			if (ti.getInstanciaDeProcedimento().getPessoa() != null)
				setMsgAviso(
						"Esta tarefa será desempenhada por " + ti.getInstanciaDeProcedimento().getPessoa().getSigla());
			if (ti.getInstanciaDeProcedimento().getLotacao() != null)
				setMsgAviso("Esta tarefa será desempenhada pela lotação "
						+ ti.getInstanciaDeProcedimento().getLotacao().getSigla());
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
	public void addAcao(String icone, String nome, String nameSpace, String action, boolean pode, String tooltip,
			String msgConfirmacao, String parametros, String pre, String pos, String classe, String modal) {
		if (parametros == null)
			parametros = "id=" + this.ti.getInstanciaDeProcedimento().getId();
		else
			parametros += "&id=" + this.ti.getInstanciaDeProcedimento().getId();
		super.addAcao(icone, nome, nameSpace, action, pode, tooltip, msgConfirmacao, parametros, pre, pos, classe,
				modal);
	}

	public void addAcao(String icone, String nome, String nameSpace, String action, Expression pode,
			String msgConfirmacao, String parametros, String pre, String pos, String classe, String modal) {
		if (parametros == null)
			parametros = "id=" + this.ti.getInstanciaDeProcedimento().getId();
		else
			parametros += "&id=" + this.ti.getInstanciaDeProcedimento().getId();
		boolean f = pode.eval();

		super.addAcao(icone, nome, nameSpace, action, f, pode.explain(f), msgConfirmacao, parametros, pre, pos, classe,
				modal);
	}

	private void addAcoes(WfTarefa ti, DpPessoa titular, DpLotacao lotaTitular) {
		addAcao(WfAcaoVO.builder().nome("_Anotar").icone("note_add").acao("/app/anotar").modal("anotarModal")
				.exp(new PodeSim()).build());

		addAcao(WfAcaoVO.builder().nome("_Pegar").icone("add")
				.acao("/app/procedimento/" + ti.getInstanciaDeProcedimento().getSiglaCompacta() + "/pegar")
				.exp(new WfPodePegar(ti.getInstanciaDeProcedimento(), titular, lotaTitular)).post(true).build());
	}

	public boolean isDesabilitarForm() {
		return desabilitarForm;
	}

	public void setDesabilitarForm(boolean desabilitarForm) {
		this.desabilitarForm = desabilitarForm;
	}

	public List<WfDefinicaoDeVariavel> getDefinicaoDeVariavel() {
		return ti.getDefinicaoDeTarefa().getDefinicaoDeVariavel();
	}

	public Object obterValorDeVariavel(WfDefinicaoDeVariavel vd) {
		return ti.getInstanciaDeProcedimento().getVariavelMap().get(vd.getIdentificador());
	}

	public List<WfDefinicaoDeDesvio> getDefinicaoDeDesvio() {
		return ti.getDefinicaoDeTarefa().getDefinicaoDeDesvio();
	}

	public String obterResponsavelDeDesvio(WfDefinicaoDeDesvio dd) {
		WfResp resp = ti.getInstanciaDeProcedimento()
				.localizarResponsavelAtual(ti.getInstanciaDeProcedimento().getCurrentTaskDefinition());
		return resp.getInitials();
	}

	public WfProcedimento getInstanciaDeProcedimento() {
		return ti.getInstanciaDeProcedimento();
	}
}
