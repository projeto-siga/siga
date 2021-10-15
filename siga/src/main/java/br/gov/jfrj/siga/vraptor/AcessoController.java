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
 * Criado em  23/11/2005
 *
 */
package br.gov.jfrj.siga.vraptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.acesso.ConfiguracaoAcesso;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.CpPerfilSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;

@Controller
public class AcessoController extends GiControllerSupport {

	private Long idOrgaoUsuSel;
	private String nomeOrgaoUsuSel;
	private DpPessoaSelecao pessoaSel;
	private DpLotacaoSelecao lotacaoSel;
	private CpPerfilSelecao perfilSel;

	private Long idServico;
	private Long idSituacao;
	private int idAbrangencia;
	private List<ConfiguracaoAcesso> itens;
	private String itensHTML;
	private String itemHTML;
	

	/**
	 * @deprecated CDI eyes only
	 */
	public AcessoController() {
		super();
	}

	@Inject
	public AcessoController(HttpServletRequest request, Result result,SigaObjects so, EntityManager em) {
		
		super(request, result, CpDao.getInstance(), so, em);
		pessoaSel = new DpPessoaSelecao();
		lotacaoSel = new DpLotacaoSelecao();
		perfilSel = new CpPerfilSelecao();
	}

	@Get("/app/gi/acesso/listar")
	public void acessoconf(int idAbrangencia
						  ,DpPessoaSelecao pessoaSel
						  ,DpLotacaoSelecao lotacaoSel
						  ,CpPerfilSelecao perfilSel
						  ,Long idOrgaoUsuSel, String servicoPai)throws Exception {
		
		this.pessoaSel = pessoaSel != null ? pessoaSel: this.pessoaSel;
		this.lotacaoSel = lotacaoSel != null ? lotacaoSel: this.lotacaoSel;
		this.perfilSel = perfilSel != null ? perfilSel: this.perfilSel;		
		
		listar(idAbrangencia, pessoaSel, lotacaoSel, perfilSel, idOrgaoUsuSel, servicoPai);
		
		result.include("idAbrangencia", this.idAbrangencia);
		
		result.include("perfilSel", this.perfilSel);
		result.include("pessoaSel", this.pessoaSel);
		result.include("lotacaoSel", this.lotacaoSel);

		result.include("idOrgaoUsuSel", this.idOrgaoUsuSel);
		result.include("nomeOrgaoUsuSel", this.nomeOrgaoUsuSel);
		result.include("orgaosUsu", getOrgaosUsu());			
		
		result.include("itemHTML", this.itemHTML);
		result.include("itensHTML", this.itensHTML);
		
		
		result.include("idServico", this.idServico);
		result.include("idSituacao", this.idSituacao);		

		result.include("servicoPai", servicoPai);
}
	
	private void listar(int idAbrangencia
					  ,DpPessoaSelecao pessoaSel
					  ,DpLotacaoSelecao lotacaoSel
					  ,CpPerfilSelecao perfilSel
					  ,Long idOrgaoUsuSel, String servicoPai) throws Exception {
		
				
		CpServico cpServicoPai=null;
		if (servicoPai == null) {
			assertAcesso("PERMISSAO:Gerenciar permissões");
		} else {
			cpServicoPai = dao().consultarCpServicoPorChave(servicoPai);
			if (cpServicoPai != null) {	
			   assertAcesso("PERMISSAONARVORE-"+servicoPai+":Gerenciar permissões do serviço "+servicoPai);
			}
		}
		if (idAbrangencia == 0) {
			this.idAbrangencia = 1;
			this.idOrgaoUsuSel = getLotaTitular().getOrgaoUsuario().getId();
		}else{
			this.idAbrangencia = idAbrangencia;
			this.idOrgaoUsuSel = idOrgaoUsuSel; 
		}

		CpPerfil perfil = this.idAbrangencia == 4 ? perfilSel.buscarObjeto(): null;
		
		DpPessoa pessoa = this.idAbrangencia == 3 ? pessoaSel.buscarObjeto(): null;
		
		DpLotacao lotacao = this.idAbrangencia == 2 ? lotacaoSel.buscarObjeto(): null;
		
		CpOrgaoUsuario orgao = this.idAbrangencia == 1 ? (this.idOrgaoUsuSel != null ? dao().consultar(this.idOrgaoUsuSel, CpOrgaoUsuario.class, false): null): null;
				
		if (orgao != null)
			this.nomeOrgaoUsuSel = orgao.getDescricao();				
				
		if (perfil != null || pessoa != null || lotacao != null || orgao != null) {
			List<CpServico> l = null;
			
			if (servicoPai == null) {
				l = dao().listarServicos();
			} else {
				l = new ArrayList<CpServico>(); 
			//	CpServico cpServicoPai = dao().consultarCpServicoPorChave(servicoPai);

				if (cpServicoPai != null) {	
					l = listarServicosPorPai(cpServicoPai);
				}
			}

			HashMap<String, ConfiguracaoAcesso> achm = new HashMap<String, ConfiguracaoAcesso>();
			for (CpServico srv : l) {
				ConfiguracaoAcesso ac = ConfiguracaoAcesso.gerar(perfil, pessoa, lotacao, orgao, srv, null);

				System.out.print(ac.getSituacao().getDescr());
				achm.put(ac.getServico().getSigla(), ac);
				
			}

			SortedSet<ConfiguracaoAcesso> acs = new TreeSet<ConfiguracaoAcesso>();
		
			Collection<ConfiguracaoAcesso> colecaoDeConfiguracoes = achm.values();
			for (ConfiguracaoAcesso ac : colecaoDeConfiguracoes) {
				if (ac.getServico().getCpServicoPai() == null) {
					acs.add(ac);
				}  else if (acs.size() == 0 && servicoPai != null && ac.getServico().getSiglaServico().toString() == servicoPai.toString())  {  
					acs.add(ac);
				}
				else 
				{
					if (achm.get(ac.getServico().getCpServicoPai().getSigla()) != null) {
						achm.get(ac.getServico().getCpServicoPai().getSigla()).getSubitens().add(ac);
					}
					else {
						acs.add(ac);
					}
				}
			}

			this.itens = (new ArrayList<ConfiguracaoAcesso>(acs));
			this.itensHTML = arvoreHTML();
			
		}
	}
	
	private List<CpServico> listarServicosPorPai(CpServico cpServicoPai) {
		List<CpServico> lista = new ArrayList<CpServico>(); 
		lista = dao().listarServicosPorPai(cpServicoPai);
		int contador = 0;
		lista.add(cpServicoPai);
		
		while (contador < lista.size()) {
		    CpServico item = lista.get(contador);
		    
			if (item != cpServicoPai) {
				List<CpServico> subitens = dao().listarServicosPorPai(item);
				if (subitens.size() > 0) {
					lista.addAll(subitens);
				}
			}
			
			contador++;
		}
		
		return lista;
	}

	@Transacional
	@Get("/app/gi/acesso/gravar")
	public void gravar(Long idServico
			          ,Integer idSituacao
			          ,DpPessoaSelecao pessoaSel
					  ,DpLotacaoSelecao lotacaoSel
					  ,CpPerfilSelecao perfilSel
					  ,Long idOrgaoUsuSel
					  ,String servicoPai) throws Exception {
		
		assertAcesso("PERMISSAO:Gerenciar permissões");
		CpPerfil perfil = null;
		DpPessoa pessoa = null;
		DpLotacao lotacao = null;
		CpOrgaoUsuario orgao = null;

		if (pessoaSel != null && pessoaSel.getId() != null) {
			pessoa = daoPes(pessoaSel.getId()).getPessoaInicial();
		} else if (lotacaoSel != null && lotacaoSel.getId() != null) {
			lotacao = daoLot(lotacaoSel.getId()).getLotacaoInicial();
		} else if (perfilSel != null && perfilSel.getId() != null) {
			perfil = dao().consultar(perfilSel.getId(), CpPerfil.class,false);
		} else if (idOrgaoUsuSel != null && idOrgaoUsuSel != null) {
			orgao = dao().consultar(idOrgaoUsuSel,CpOrgaoUsuario.class, false);
		} else {
			throw new AplicacaoException(
					"Não foi informada pessoa, lotação ou órgão usuário.");
		}

		CpServico servico = dao().consultar(idServico, CpServico.class, false);
		
		CpSituacaoDeConfiguracaoEnum situacao = CpSituacaoDeConfiguracaoEnum.getById(idSituacao);
		
		CpTipoDeConfiguracao tpConf = CpTipoDeConfiguracao.UTILIZAR_SERVICO;
		
		Cp.getInstance().getBL().configurarAcesso(perfil, orgao, lotacao,pessoa, servico, situacao,tpConf, getIdentidadeCadastrante());
		
		ConfiguracaoAcesso ac = ConfiguracaoAcesso.gerar(perfil, pessoa,lotacao, orgao, servico,null);
		
		StringBuilder sb = new StringBuilder();
		acessoHTML(sb, ac);
		this.itemHTML = sb.toString();
		result.include("servicoPai", servicoPai);
		result.use(Results.http()).body(this.itemHTML);
	}	

	private String arvoreHTML() {
		StringBuilder sb = new StringBuilder();
		acrescentarHTML(this.itens, sb);
		return sb.toString();
	}

	private void acrescentarHTML(Collection<ConfiguracaoAcesso> l,
			StringBuilder sb) {
		sb.append("<ul style=\"list-style-type:disc;\">");
		for (ConfiguracaoAcesso ac : l) {
			sb.append("<span id=\"SPAN-" + ac.getServico().getId() + "\">");
			acessoHTML(sb, ac);
			sb.append("</span>");
			if (ac.getSubitens() != null && ac.getSubitens().size() > 0)
				acrescentarHTML(ac.getSubitens(), sb);
		}
		sb.append("</ul>");
	}

	private void acessoHTML(StringBuilder sb, ConfiguracaoAcesso ac) {
		boolean fPode = ac.getSituacao() == CpSituacaoDeConfiguracaoEnum.PODE;
		boolean fNaoPode = ac.getSituacao() == CpSituacaoDeConfiguracaoEnum.NAO_PODE;
		
		sb.append("<li style=\"color:"+ (fPode ? "green" : (fNaoPode ? "red" : "black")) + ";\">");
		
		if (ac.getServico().getCpServicoPai() != null
				&& ac.getServico().getSigla().startsWith(
						ac.getServico().getCpServicoPai().getSigla() + "-")) {
			sb.append(ac.getServico().getSigla().substring(
					ac.getServico().getCpServicoPai().getSigla().length() + 1));
		} else {
			sb.append(ac.getServico().getSigla());
		}
		
		sb.append(": <span style=\"\">");
		sb.append(ac.getServico().getDescricao());
		
		if (!(fPode || fNaoPode)) {
			sb.append(" (");
			sb.append(ac.getSituacao().getDescr());
			sb.append(")");
		}
		
		sb.append("</span>");
		
		if (fNaoPode)
			sb.append(" - <a style=\"color:gray;\" href=\"javascript:enviar("+ ac.getServico().getId() + ",1)\">Permitir</a>");
		
		if (fPode)
			sb.append(" - <a  style=\"color:gray;\" href=\"javascript:enviar("+ ac.getServico().getId() + ",2)\">Proibir</a>");
		
		if (!ac.isDefault())
			sb.append(" - <a  style=\"color:gray;\" href=\"javascript:enviar("+ ac.getServico().getId() + ",9)\">Default</a>");
		
		if (ac.getOrgao() != null)
			sb.append(" - origem: <a  style=\"color:gray;\" href=\"?idAbrangencia=1&idOrgaoUsu="
							+ ac.getOrgao().getId()
							+ "\">"
							+ ac.getOrgao().getSigla() + "</a>");
		
		if (ac.getPerfil() != null)
			sb.append(" - origem: <a  style=\"color:gray;\" href=\"?idAbrangencia=4&perfilSel.id="
							+ ac.getPerfil().getId()
							+ "&perfilSel.descricao="
							+ ac.getPerfil().getDescricao()
							+ "&perfilSel.sigla="
							+ ac.getPerfil().getSigla()
							+ "\">" + ac.getPerfil().getSigla() + "</a>");
		
		if (ac.getLotacao() != null)
			sb.append(" - origem: <a  style=\"color:gray;\" href=\"?idAbrangencia=2&lotacaoSel.id="
							+ ac.getLotacao().getId()
							+ "&lotacaoSel.descricao="
							+ ac.getLotacao().getDescricao()
							+ "&lotacaoSel.sigla="
							+ ac.getLotacao().getSigla()
							+ "\">" + ac.getLotacao().getSigla() + "</a>");
		
		if (ac.getPessoa() != null)
			sb.append(" - origem: <a  style=\"color:gray;\" href=\"?idAbrangencia=3&pessoaSel.id="
							+ ac.getPessoa().getId()
							+ "&pessoaSel.descricao="
							+ ac.getPessoa().getDescricao()
							+ "&pessoaSel.sigla="
							+ ac.getPessoa().getSigla()
							+ "\">" + ac.getPessoa().getSigla() + "</a>");
		
		sb.append("</li>");
	}

	private void acrescentarHTMLOld(Collection<ConfiguracaoAcesso> l,StringBuilder sb) {
		sb.append("<ul style=\"list-style-type:disc;\">");
		for (ConfiguracaoAcesso ac : l) {
			sb.append("<span id=\"SPAN-" + ac.getServico().getId() + "\">");
			acessoHTML(sb, ac);
			sb.append("</span>");
			if (ac.getSubitens() != null && ac.getSubitens().size() > 0)
				acrescentarHTML(ac.getSubitens(), sb);
		}
		sb.append("</ul>");
	}

	private void acessoHTMLOld(StringBuilder sb, ConfiguracaoAcesso ac) {
		boolean fPode = ac.getSituacao().getDescr().equals("Pode");
		boolean fNaoPode = ac.getSituacao().getDescr().equals(
				"Não Pode");
		sb.append("<li style=\"color:"
				+ (fPode ? "green" : (fNaoPode ? "red" : "black")) + ";\">");
		if (ac.getServico().getCpServicoPai() != null
				&& ac.getServico().getSigla().startsWith(
						ac.getServico().getCpServicoPai().getSigla() + "-")) {
			sb.append(ac.getServico().getSigla().substring(
					ac.getServico().getCpServicoPai().getSigla().length() + 1));
		} else {
			sb.append(ac.getServico().getSigla());
		}
		
		sb.append(": <span style=\"\">");
		sb.append(ac.getServico().getDescricao());
		
		if (!(fPode || fNaoPode)) {
			sb.append(" (");
			sb.append(ac.getSituacao().getDescr());
			sb.append(")");
		}
		
		sb.append("</span>");
		
		if (fNaoPode)
			sb.append(" - <a style=\"color:gray;\" href=\"javascript:enviar("+ ac.getServico().getId() + ",1)\">Permitir</a>");
		
		if (fPode)
			sb.append(" - <a  style=\"color:gray;\" href=\"javascript:enviar("+ ac.getServico().getId() + ",2)\">Proibir</a>");
		
		if (!ac.isDefault())
			sb.append(" - <a  style=\"color:gray;\" href=\"javascript:enviar("+ ac.getServico().getId() + ",9)\">Default</a>");
		
		sb.append("</li>");
	}

	protected List<CpOrgaoUsuario> getOrgaosUsu() throws AplicacaoException {
		return this.dao.listarOrgaosUsuarios();
	}	
	
}
