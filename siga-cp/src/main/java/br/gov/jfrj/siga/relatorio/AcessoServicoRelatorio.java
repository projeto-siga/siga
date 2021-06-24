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
package br.gov.jfrj.siga.relatorio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpConfiguracaoCache;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;

/**
 * Relatório indicando todas as pessoas que tem acesso aos sistemas/módulos e
 * quais são os niveis de acesso
 * 
 * @author aym
 * 
 */
public class AcessoServicoRelatorio extends RelatorioTemplate {
	private CpServico cpServico;
	private ArrayList<CpSituacaoDeConfiguracaoEnum> cpSituacoesConfiguracao;
	private ArrayList<CpOrgaoUsuario> cpOrgaosUsuario;

	@SuppressWarnings("unchecked")
	public AcessoServicoRelatorio(Map parametros) throws DJBuilderException {
		super(parametros);
		if (parametros.get("idServico") == null) {
			throw new DJBuilderException("Parâmetro idServico não informado!");
		}
		if (parametros.get("situacoesSelecionadas") == null) {
			throw new DJBuilderException(
					"Parâmetro situacoesSelecionadas não informado!");
		}
		if (parametros.get("idOrgaoUsuario") == null) {
			throw new DJBuilderException(
					"Parâmetro idOrgaoUsuario não informado!");
		}

		try {
			Long t_lngIdServico = Long.parseLong((String) parametros
					.get("idServico"));
			setCpServico(dao()
					.consultar(t_lngIdServico, CpServico.class, false));
		} catch (Exception e) {
			throw new DJBuilderException("Parâmetro idServico inválido!");
		}
		try {
			ArrayList<CpSituacaoDeConfiguracaoEnum> cpSituacoes = new ArrayList<>();
			String strSits = (String) parametros.get("situacoesSelecionadas");
			for (String strIdSit : strSits.split(",")) {
				Integer idSit = Integer.parseInt(strIdSit);
				CpSituacaoDeConfiguracaoEnum sit = CpSituacaoDeConfiguracaoEnum.getById(idSit);
				cpSituacoes.add(sit);
			}
			setCpSituacoesConfiguracao(cpSituacoes);
		} catch (Exception e) {
			throw new DJBuilderException("Situações inválidas ! erro:"
					+ e.getMessage());
		}
		try {
			if (parametros.get("idOrgaoUsuario").equals("-1")) {
				setCpOrgaosUsuario((ArrayList<CpOrgaoUsuario>) dao()
						.listarOrgaosUsuarios());
			} else {
				Long idOrg = Long.parseLong((String) parametros
						.get("idOrgaoUsuario"));
				ArrayList<CpOrgaoUsuario> cpOrgs = new ArrayList<CpOrgaoUsuario>();
				cpOrgs.add(dao().consultar(idOrg, CpOrgaoUsuario.class, false));
				setCpOrgaosUsuario(cpOrgs);
			}
		} catch (Exception e) {
			throw new DJBuilderException("Orgao Usuario inválido ! erro:"
					+ e.getMessage());
		}
		parametros.put("titulo","SIGA");
		parametros.put("subtitulo","Sistema de Gestão Administrativa");
		parametros.put("secaoUsuario", "");
		if ( Prop.get("/siga.relat.brasao") == null ) {
			parametros.put("brasao","brasao.png");
		} else {
			parametros.put("brasao",Prop.get("/siga.relat.brasao") );
		}
		//System.out.println("Brasao: " + parametros.get("brasao"));
	}

	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException {
		//this.setTemplateFile(null);
		this.setTitle("Acesso - " + getDescricaoTipoConfiguracao() + " - "
				+ "(" + cpServico.getSiglaServico() + ") "
				+ cpServico.getDscServico());
		this.addColuna("Situação", 0, RelatorioRapido.ESQUERDA, true, false);
		this.addColuna("Pessoa", 60, RelatorioRapido.ESQUERDA, false, false);
		this.addColuna("Lotação", 20, RelatorioRapido.ESQUERDA, false, false);
		this.addColuna("Desde", 12, RelatorioRapido.ESQUERDA, false, false);
		this.addColuna("Origem", 20, RelatorioRapido.ESQUERDA, false, false);
		this.addColuna("Cadastrante", 18, RelatorioRapido.ESQUERDA, false,
				false);
		return this;
	}

	/**
	 * Processa as configurações
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection processarDados() {
		CpTipoDeConfiguracao tipo = CpTipoDeConfiguracao.UTILIZAR_SERVICO;
		ArrayList<AlteracaoDireitosItem> aldis = new ArrayList<AlteracaoDireitosItem>();
		ArrayList<String> dados = new ArrayList<String>();
		try {
			for (CpOrgaoUsuario cpou : getCpOrgaosUsuario()) {
				for (DpPessoa pes : obterPessoas(cpou)) {
					AlteracaoDireitosItem it = gerar(tipo, null, pes, null,
							cpou, getCpServico(), null);
					if (getCpSituacoesConfiguracao().contains(it.getSituacao())) {
						aldis.add(it);
					}
				}
			}
			ordenarConfiguracoes(aldis);
			for (AlteracaoDireitosItem it : aldis) {
				processarItem(it, dados);
			}
		} catch (Exception e) {
			dados = new ArrayList<String>();
		}
		return dados;
	}

	private AlteracaoDireitosItem gerar(CpTipoDeConfiguracao tipo,
			CpPerfil perfil, DpPessoa pessoa, DpLotacao lotacao,
			CpOrgaoUsuario orgao, CpServico servico, Date dtEvn)
			throws Exception {
		CpConfiguracao cfgFiltro = new CpConfiguracao();
		cfgFiltro.setCpGrupo(perfil);
		cfgFiltro.setDpPessoa(pessoa);
		cfgFiltro.setLotacao(lotacao);
		cfgFiltro.setOrgaoUsuario(orgao);
		cfgFiltro.setCpServico(servico);
		cfgFiltro.setCpTipoConfiguracao(tipo);
		CpConfiguracaoCache cache = Cp.getInstance().getConf().buscaConfiguracao(
				cfgFiltro, new int[0], dtEvn);
		CpConfiguracao cfg = CpDao.getInstance().consultar(cache.idConfiguracao, CpConfiguracao.class, false);
		AlteracaoDireitosItem itm = new AlteracaoDireitosItem();
		itm.setServico(servico);
		itm.setPessoa(pessoa);
		if (cfg != null) {
			itm.setCfg(cfg);
			itm.setSituacao(cfg.getCpSituacaoConfiguracao());
		} else {
			itm.setSituacao(servico.getCpTipoServico().getSituacaoDefault());
		}
		return itm;
	}

	private ArrayList<DpPessoa> obterPessoas(CpOrgaoUsuario cpou) {
		return (ArrayList<DpPessoa>) dao().listarAtivos(DpPessoa.class,
				"dataFimPessoa", cpou.getIdOrgaoUsu());
	}

	/**
	 * Utilizado apenas para teste subtituindo obterPessoas
	 * 
	 * @param cpou
	 * @return
	 */
	@SuppressWarnings("unused")
	private ArrayList<DpPessoa> obterPessoasTeste(CpOrgaoUsuario cpou) {
		ArrayList<DpPessoa> psas = new ArrayList<DpPessoa>();
		psas.add(dao().consultar(131078L, DpPessoa.class, true));
		psas.add(dao().consultar(132232L, DpPessoa.class, true));
		psas.add(dao().consultar(129903L, DpPessoa.class, true));
		psas.add(dao().consultar(131002L, DpPessoa.class, true));
		psas.add(dao().consultar(129929L, DpPessoa.class, true));
		return psas;
	}

	/**
	 * Ordena o ArraList de ConfiguracaoAcesso
	 * 
	 * @param arl
	 *            ArrayList com as configurações a rodenar
	 */
	@SuppressWarnings("unchecked")
	private void ordenarConfiguracoes(ArrayList<AlteracaoDireitosItem> arl) {
		Collections.sort(arl, new Comparator() {
			public int compare(Object o1, Object o2) {
				AlteracaoDireitosItem c1 = (AlteracaoDireitosItem) o1;
				AlteracaoDireitosItem c2 = (AlteracaoDireitosItem) o2;
				String dscSit1;
				if (c1.getSituacao() == null) {
					dscSit1 = new String();
				} else {
					dscSit1 = c1.getSituacao().getDescr();
				}
				String dscSit2;
				if (c2.getSituacao() == null) {
					dscSit2 = new String();
				} else {
					dscSit2 = c2.getSituacao().getDescr();
				}
				if (dscSit1.equals(dscSit2)) {
					String nome1;
					if (c1.getPessoa() == null) {
						nome1 = new String();
					} else {
						nome1 = c1.getPessoa().getNomePessoa();
					}
					String nome2;
					if (c2.getPessoa() == null) {
						nome2 = new String();
					} else {
						nome2 = c2.getPessoa().getNomePessoa();
					}
					return nome1.compareToIgnoreCase(nome2);
				} else {
					return dscSit1.compareToIgnoreCase(dscSit2);
				}
			}
		});
	}

	/**
	 * Preenche os dados com as informações da configuração já formatados
	 * 
	 * @param cfga
	 *            - Configuração acesso
	 * @param dados
	 *            - coleção de linhas do relatório
	 */
	private void processarItem(AlteracaoDireitosItem cfga, List<String> dados) {
		try {
			dados.add(cfga.getSituacao().getDescr());
		} catch (Exception e) {
			dados.add("");
		}
		try {
			dados.add(cfga.getPessoa().getNomePessoa());
		} catch (Exception e) {
			dados.add("");
		}
		try {
			dados.add(cfga.getPessoa().getLotacao().getSigla());
		} catch (Exception e) {
			dados.add("");
		}
		try {
			dados.add(printDate(cfga.getInicio()));
		} catch (Exception e) {
			dados.add("");
		}
		try {
			dados.add(printOrigem(cfga));
		} catch (Exception e) {
			dados.add("");
		}
		try {
			dados.add(String.valueOf(cfga.getCadastrante().getSesbPessoa()
					+ cfga.getCadastrante().getMatricula()));
		} catch (Exception e) {
			dados.add("");
		}
	}

	private String printDate(Date dte) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return df.format(dte);
	}

	/**
	 * @param configuração
	 *            acesso
	 * @return Uma String representativa da origem
	 */
	private String printOrigem(AlteracaoDireitosItem cfga) {
		return cfga.printOrigemCurta();
	}

	private Integer getIdTipoConfiguracao() {
		return CpTipoDeConfiguracao.UTILIZAR_SERVICO.getId();
	}

	private CpTipoDeConfiguracao getTipoConfiguracao() {
		return dao().consultar(getIdTipoConfiguracao(),
				CpTipoDeConfiguracao.class, false);
	}

	private String getDescricaoTipoConfiguracao() {
		return getTipoConfiguracao().getDescr();
	}

	private CpDao dao() {
		return CpDao.getInstance();
	}

	public static void main(String args[]) throws Exception {
	}

	/**
	 * @return the cpServico
	 */
	public CpServico getCpServico() {
		return cpServico;
	}

	/**
	 * @param cpServico
	 *            the cpServico to set
	 */
	public void setCpServico(CpServico cpServico) {
		this.cpServico = cpServico;
	}

	/**
	 * @return the cpSituacoesConfiguracao
	 */
	public ArrayList<CpSituacaoDeConfiguracaoEnum> getCpSituacoesConfiguracao() {
		return cpSituacoesConfiguracao;
	}

	/**
	 * @param cpSituacoesConfiguracao
	 *            the cpSituacoesConfiguracao to set
	 */
	public void setCpSituacoesConfiguracao(
			ArrayList<CpSituacaoDeConfiguracaoEnum> cpSituacoesConfiguracao) {
		this.cpSituacoesConfiguracao = cpSituacoesConfiguracao;
	}

	/**
	 * @return the cpOrgaosUsuario
	 */
	public ArrayList<CpOrgaoUsuario> getCpOrgaosUsuario() {
		return cpOrgaosUsuario;
	}

	/**
	 * @param cpOrgaosUsuario
	 *            the cpOrgaosUsuario to set
	 */
	public void setCpOrgaosUsuario(ArrayList<CpOrgaoUsuario> cpOrgaosUsuario) {
		this.cpOrgaosUsuario = cpOrgaosUsuario;
	}

}
