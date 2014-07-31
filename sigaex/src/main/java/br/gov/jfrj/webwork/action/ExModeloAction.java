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
/*
 * Criado em  13/09/2005
 *
 */
package br.gov.jfrj.webwork.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletResponse;

import org.kxml2.io.KXmlSerializer;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpModelo;
import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.libs.webwork.DpCargoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpFuncaoConfiancaSelecao;
import br.gov.jfrj.siga.libs.webwork.DpLotacaoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpPessoaSelecao;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;

public class ExModeloAction extends ExSelecionavelActionSupport {

	public Long id;
	public String nome;
	public String descricao;
	public Integer forma;
	public Long nivel;
	public String tipoModelo;
	public String conteudo;
	public String arquivo;
	public String script;

	public ExModelo mod;

	public Integer postback;

	private ExClassificacaoSelecao classificacaoSel;
	private ExClassificacaoSelecao classificacaoCriacaoViasSel;

	private DpPessoaSelecao pessoaSel;
	private DpLotacaoSelecao lotacaoSel;
	private DpCargoSelecao cargoSel;

	private DpFuncaoConfiancaSelecao funcaoSel;	
	
	public ExModeloAction() {
		classificacaoSel = new ExClassificacaoSelecao();
		classificacaoCriacaoViasSel = new ExClassificacaoSelecao();
		pessoaSel = new DpPessoaSelecao();
		lotacaoSel = new DpLotacaoSelecao();
		cargoSel = new DpCargoSelecao();
		funcaoSel = new DpFuncaoConfiancaSelecao();
	}

	public List<ExNivelAcesso> getListaNivelAcesso() throws Exception {
		return dao().listarOrdemNivel();
	}

	public List<ExFormaDocumento> getListaForma() throws Exception {
		return dao().listarExFormasDocumento();
	}

	// TODO: Criar EX_TIPO_MODELO
	public Map<String, String> getListaTipoModelo() throws Exception {
		TreeMap<String, String> map = new TreeMap<String, String>();
		map.put("template/freemarker", "FreeMarker");
		map.put("template-file/jsp", "JSP");
		return map;
	}

	public String aExportar() throws Exception {
		String MODELOS = "siga-doc-modelos";
		String MODELO_GERAL = "modelo-geral";
		String MODELO = "modelo";

		assertAcesso("MOD:Gerenciar modelos");

		KXmlSerializer serializer = new KXmlSerializer();
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		serializer.setOutput(os, "utf-8");
		serializer.startDocument(null, null);

		serializer.startTag(null, MODELOS);

		serializer.flush();
		os.write('\n');
		os.write('\n');

		List<CpModelo> lCp = dao().listarModelosOrdenarPorNome(null);

		for (CpModelo m : lCp) {
			serializer.startTag(null, MODELO_GERAL);
			if (m.getCpOrgaoUsuario() != null)
				serializer.attribute(null, "orgao", m.getCpOrgaoUsuario()
						.getSigla());
			String template = m.getConteudoBlobString();
			if (template != null) {
				serializer.flush();
				os.write('\n');
				serializer.cdsect(template);
				serializer.flush();
				os.write('\n');
			}
			serializer.endTag(null, MODELO_GERAL);

			serializer.flush();
			os.write('\n');
			os.write('\n');
		}


		serializer.flush();
		os.write('\n');
		os.write('\n');

		List<ExModelo> l = dao().listarTodosModelosOrdenarPorNome(null);
		
		for (ExModelo m : l) {
			serializer.startTag(null, MODELO);
			if (m.getExFormaDocumento() != null)
				serializer.attribute(null, "forma", m.getExFormaDocumento()
						.getDescricao());
			if (m.getNmMod() != null)
				serializer.attribute(null, "nome", m.getNmMod());
			if (m.getDescMod() != null)
				serializer.attribute(null, "descricao", m.getDescMod());
			if (m.getExClassificacao() != null)
				serializer.attribute(null, "classificacao", m
						.getExClassificacao().getSigla());
			if (m.getExClassCriacaoVia() != null)
				serializer.attribute(null, "classCriacaoVia", m
						.getExClassCriacaoVia().getSigla());
			if (m.getExNivelAcesso() != null)
				serializer.attribute(null, "nivel", m.getExNivelAcesso()
						.getNmNivelAcesso());
			if (m.getNmArqMod() != null)
				serializer.attribute(null, "arquivo", m.getNmArqMod());
			if (m.getConteudoTpBlob() != null)
				serializer.attribute(null, "tipo", m.getConteudoTpBlob());
			byte[] template = m.getConteudoBlobMod2();
			if (template != null) {
				serializer.flush();
				os.write('\n');
				serializer.cdsect(new String(template, "utf-8"));
				serializer.flush();
				os.write('\n');
			}
			serializer.endTag(null, MODELO);

			serializer.flush();
			os.write('\n');
			os.write('\n');
		}

		serializer.endTag(null, MODELOS);

		serializer.endDocument();
 
		serializer.flush();

		HttpServletResponse res = ServletActionContext.getResponse();
		OutputStream out = res.getOutputStream();

		res.setContentType("text/xml");
		res.setContentLength(os.size());

		ByteArrayInputStream bais = new ByteArrayInputStream(os.toByteArray());

		int i;
		while ((i = bais.read()) != -1) {
			out.write(i);
		}
		bais.close();
		out.close();
		return Action.SUCCESS;
	}

	public String aListar() throws Exception {
		assertAcesso("MOD:Gerenciar modelos");
		setItens(dao().listarTodosModelosOrdenarPorNome(script));
		return Action.SUCCESS;
	}

	public String aEditar() throws Exception {
		assertAcesso("MOD:Gerenciar modelos");
		if (getPostback() == null) {
			lerForm();
			escreverForm();
		}
		return Action.SUCCESS;
	}

	private void lerForm() throws AplicacaoException,
			UnsupportedEncodingException {
		if (id == null) {
			mod = new ExModelo();
		} else {
			mod = Ex.getInstance().getBL().getCopia(dao().consultar(getId(), ExModelo.class, false));

			// if (!ExCompetenciaBL
			// .podeEditar(getTitular(), getLotaTitular(), mob))
			// throw new AplicacaoException(
			// "Não é permitido editar documento fechado");

		}

		if (getPostback() != null) {
			mod.setNmMod(nome);
			mod.setExClassificacao(classificacaoSel.buscarObjeto());
			mod.setExClassCriacaoVia(classificacaoCriacaoViasSel.buscarObjeto());
			mod.setConteudoTpBlob(tipoModelo);
			if (conteudo != null && conteudo.trim().length() > 0)
				mod.setConteudoBlobMod2(conteudo.getBytes("utf-8"));
			mod.setDescMod(descricao);
			if (forma != null && forma != 0)
				mod.setExFormaDocumento(dao().consultar(forma,
						ExFormaDocumento.class, false));
			if (nivel != null && nivel != 0)
				mod.setExNivelAcesso(dao().consultar(nivel,
						ExNivelAcesso.class, false));
			mod.setNmArqMod(arquivo);
		}
	}

	private void escreverForm() throws AplicacaoException,
			UnsupportedEncodingException {
		nome = mod.getNmMod();
		classificacaoSel.buscarPorObjeto(mod.getExClassificacao());
		classificacaoCriacaoViasSel.buscarPorObjeto(mod.getExClassCriacaoVia());
		tipoModelo = mod.getConteudoTpBlob();
		if (tipoModelo == null || tipoModelo.trim().length() == 0)
			tipoModelo = "template-file/jsp";
		conteudo = mod.getConteudoBlobMod() != null ? new String(
				mod.getConteudoBlobMod2(), "utf-8") : null;
		descricao = mod.getDescMod();
		forma = mod.getExFormaDocumento() != null ? mod.getExFormaDocumento()
				.getIdFormaDoc() : null;
		nivel = mod.getExNivelAcesso() != null ? mod.getExNivelAcesso()
				.getIdNivelAcesso() : null;
		arquivo = mod.getNmArqMod();
	}

	public String aEditarGravar() throws Exception {
		assertAcesso("MOD:Gerenciar modelos");
		lerForm();
		ExModelo modAntigo = null;
		if (mod.getIdInicial() != null){
			modAntigo = dao().consultar(mod.getIdInicial(), ExModelo.class, false).getModeloAtual();
		}
		Ex.getInstance().getBL().gravarModelo(mod,modAntigo,null,getIdentidadeCadastrante());
		setId(mod.getId());
		Map<String, String[]> l = getPar();
		if ("Aplicar".equals(param("submit")))
			return "aplicar";
		return Action.SUCCESS;
	}
	
	public String aDesativar() throws Exception {
		dao().iniciarTransacao();
		assertAcesso("MOD:Gerenciar modelos");
		if (getId() == null) 
			throw new Exception("ID não informada");
		mod = dao().consultar(getId(), ExModelo.class, false);
		dao().excluirComHistorico(mod, dao().consultarDataEHoraDoServidor(),
				getIdentidadeCadastrante());
		dao().commitTransacao();
		return Action.SUCCESS;
	}
	
	public Set<CpSituacaoConfiguracao> getListaSituacao() throws Exception {
		TreeSet<CpSituacaoConfiguracao> s = new TreeSet<CpSituacaoConfiguracao>(
				new Comparator() {

					public int compare(Object o1, Object o2) {
						return ((CpSituacaoConfiguracao) o1)
								.getDscSitConfiguracao().compareTo(
										((CpSituacaoConfiguracao) o2)
												.getDscSitConfiguracao());
					}

				});

		s.addAll(dao().listarSituacoesConfiguracao());

		return s;
	}

	@Override
	public DaoFiltroSelecionavel createDaoFiltro() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getForma() {
		return forma;
	}

	public void setForma(Integer forma) {
		this.forma = forma;
	}

	public Long getNivel() {
		return nivel;
	}

	public void setNivel(Long nivel) {
		this.nivel = nivel;
	}

	public String getTipoModelo() {
		return tipoModelo;
	}

	public void setTipoModelo(String tipoModelo) {
		this.tipoModelo = tipoModelo;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public String getArquivo() {
		return arquivo;
	}

	public void setArquivo(String arquivo) {
		this.arquivo = arquivo;
	}

	public ExModelo getMod() {
		return mod;
	}

	public void setMod(ExModelo mod) {
		this.mod = mod;
	}

	public Integer getPostback() {
		return postback;
	}

	public void setPostback(Integer postback) {
		this.postback = postback;
	}

	public ExClassificacaoSelecao getClassificacaoSel() {
		return classificacaoSel;
	}

	public void setClassificacaoSel(ExClassificacaoSelecao classificacaoSel) {
		this.classificacaoSel = classificacaoSel;
	}

	public ExClassificacaoSelecao getClassificacaoCriacaoViasSel() {
		return classificacaoCriacaoViasSel;
	}

	public void setClassificacaoCriacaoViasSel(
			ExClassificacaoSelecao classificacaoCriacaoViasSel) {
		this.classificacaoCriacaoViasSel = classificacaoCriacaoViasSel;
	}

	public DpPessoaSelecao getPessoaSel() {
		return pessoaSel;
	}

	public void setPessoaSel(DpPessoaSelecao pessoaSel) {
		this.pessoaSel = pessoaSel;
	}

	public DpLotacaoSelecao getLotacaoSel() {
		return lotacaoSel;
	}

	public void setLotacaoSel(DpLotacaoSelecao lotacaoSel) {
		this.lotacaoSel = lotacaoSel;
	}

	public DpCargoSelecao getCargoSel() {
		return cargoSel;
	}

	public void setCargoSel(DpCargoSelecao cargoSel) {
		this.cargoSel = cargoSel;
	}

	public DpFuncaoConfiancaSelecao getFuncaoSel() {
		return funcaoSel;
	}

	public void setFuncaoSel(DpFuncaoConfiancaSelecao funcaoSel) {
		this.funcaoSel = funcaoSel;
	}
}