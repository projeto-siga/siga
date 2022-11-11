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
package br.gov.jfrj.siga.ldap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.mvel2.MVEL;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpGrupo;
import br.gov.jfrj.siga.cp.CpGrupoDeEmail;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpPapel;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.CpPerfilJEE;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.grupo.ConfiguracaoGrupo;
import br.gov.jfrj.siga.cp.grupo.TipoConfiguracaoGrupoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ldap.sinc.SincProperties;
import br.gov.jfrj.siga.ldap.sinc.resolvedores.ResolvedorRegrasCaixaPostal;
import sun.misc.BASE64Encoder;

public class AdModelo {

	// TODO: Eliminar quando o tipo de agrupamento estiver disponivel na lotacao
	private static final long CP_TIPO_PAPEL_PRINCIPAL = 1;

	private static AdModelo instance;

	private Logger log = Logger.getLogger(AdModelo.class.getName());
	private SincProperties conf;

	private final AdUnidadeOrganizacional uoGI;
	private final AdUnidadeOrganizacional uoGrpDistr;
	private final AdUnidadeOrganizacional uoUsuarios;
	private final AdUnidadeOrganizacional uoUsuariosInativos;
	private final AdUnidadeOrganizacional uoGrpDistrInativos;
	private final AdUnidadeOrganizacional uoContatos;

	private final AdUnidadeOrganizacional uoGrpSeguranca;
	private final AdUnidadeOrganizacional uoGrpSegInativos;

	private ResolvedorRegrasCaixaPostal rCxPostal;

	private List<DpPessoa> listaPessoas;

	private Map<String, ServicoSincronizavel> mapSvcSinc = new HashMap<String, ServicoSincronizavel>();

	ListaObjetos al = new ListaObjetos();

	// TODO: Eliminar quando o tipo de agrupamento estiver disponivel na lotacao
	private List<DpLotacao> lotacoesNaoPrincipais = new ArrayList<DpLotacao>();

	private AdModelo(SincProperties conf) {
		this.conf = conf;

		uoGI = new AdUnidadeOrganizacional(conf.getNomeContainerGI(), conf.getNomeContainerGI(), conf.getDnDominio());
		uoGrpDistr = new AdUnidadeOrganizacional(conf.getNomeContainerGrpDistr(), conf.getNomeContainerGrpDistr(), uoGI,
				conf.getDnDominio());
		uoUsuarios = new AdUnidadeOrganizacional(conf.getNomeContainerUsuarios(), conf.getNomeContainerUsuarios(), uoGI,
				conf.getDnDominio());
		uoUsuariosInativos = new AdUnidadeOrganizacional(conf.getNomeContainerUsuariosInativos(),
				conf.getNomeContainerUsuariosInativos(), uoGI, conf.getDnDominio());
		uoGrpDistrInativos = new AdUnidadeOrganizacional(conf.getNomeContainerGrpDistrInativos(),
				conf.getNomeContainerGrpDistrInativos(), uoGI, conf.getDnDominio());
		uoContatos = new AdUnidadeOrganizacional(conf.getNomeContainerContatos(), conf.getNomeContainerContatos(), uoGI,
				conf.getDnDominio());
		uoGrpSeguranca = new AdUnidadeOrganizacional(conf.getNomeContainerGrpSeg(), conf.getNomeContainerGrpSeg(), uoGI,
				conf.getDnDominio());

		uoGrpSegInativos = new AdUnidadeOrganizacional(conf.getNomeContainerGrpSegInativos(),
				conf.getNomeContainerGrpSegInativos(), uoGI, conf.getDnDominio());

		for (String s : conf.getListaServicosSincronizaveis()) {
			ServicoSincronizavel ss = new ServicoSincronizavel(s);
			mapSvcSinc.put(ss.get("svc"), ss);
		}

	}

	/**
	 * Retorna uma instância única (singleton)
	 * 
	 * @return
	 * @throws IOException
	 */
	public static AdModelo getInstance(SincProperties conf) {
		if (instance == null) {
			instance = new AdModelo(conf);
		}
		return instance;
	}

//	/**
//	 * Retorna uma instância única (singleton)
//	 * 
//	 * @return
//	 * @throws IOException
//	 */
//	public static AdModelo getInstance() {
//		if (instance == null) {
//			instance = new AdModelo(SincProperties.getInstancia());
//		}
//		return instance;
//	}

	public List<AdObjeto> gerarModelo(List<DpPessoa> pessoas, List<DpLotacao> lotacoes, boolean sincSenhas)
			throws AplicacaoException {
		registrarLotacoesNaoPrincipais();
		listaPessoas = pessoas;
		try {
			rCxPostal = ResolvedorRegrasCaixaPostal.getInstancia(conf);
		} catch (AplicacaoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		CpDao dao = CpDao.getInstance();
		// List<AdObjeto> al = new ArrayList<AdObjeto>();

		al.put(uoGI.getNome(), uoGI);
		al.put(uoGrpDistr.getNome(), uoGrpDistr);

		al.put(uoUsuarios.getNome(), uoUsuarios);
		al.put(uoContatos.getNome(), uoContatos);

		if (conf.isModoExclusaoUsuarioAtivo()) {
			al.put(uoUsuariosInativos.getNome(), uoUsuariosInativos);
		}

		if (conf.isModoExclusaoGrupoAtivo()) {
			al.put(uoGrpDistrInativos.getNome(), uoGrpDistrInativos);
			al.put(uoGrpSegInativos.getNome(), uoGrpSegInativos);
		}

		if (conf.getSincronizarGruposSeguranca()) {
			al.put(uoGrpSeguranca.getNome(), uoGrpSeguranca);
		}

		// boolean interromperSincronismo = false;
		int totalPessoas = getListaPessoas().size();
		int p = 0;
		System.out.println("Processando " + totalPessoas + " pessoas...");
		for (DpPessoa pes : getListaPessoas()) {
			processarPessoa(sincSenhas, pes);
			p++;
			if (p % 50 == 0)
				System.out.println(p + " de " + totalPessoas);

		}

		// processarLotacoes (mesmo sem pessoas)
		if (lotacoes != null) {
			int totalLotacoes = getListaPessoas().size();
			int l = 0;
			System.out.println("Processando " + totalLotacoes + " lotacoes...");

			for (DpLotacao lotacao : lotacoes) {
				if (lotacaoPrincipal(lotacao)) {
					if (!lotacao.getSigla().contains("(extinta)")) {
						AdGrupoDeDistribuicao gdLot = processarGrupoDistribuicao(lotacao, uoGrpDistr);

						if (conf.getSincronizarGruposSeguranca()) {
							processarGrupoSegurancaAuto(null, lotacao, uoGrpSeguranca);
						}
					}
				}
			}
			if (l % 10 == 0)
				System.out.println(l + " de " + totalLotacoes);
		}

		processarGruposManuais();

		List<AdObjeto> modelo = new ArrayList<AdObjeto>();
		modelo.addAll(al.values());
		return modelo;
	}

	private void processarPessoa(boolean sincSenhas, DpPessoa pes) {
		String nomePessoa = conf.getPrefixoMatricula() + pes.getMatricula().toString();

		AdUsuario usu = null;

		usu = new AdUsuario(nomePessoa, nomePessoa, uoUsuarios, conf.getDnDominio());

		usu.setSigla(pes.getSiglaPessoa().toLowerCase());
		// usu.setEmail(pes.getEmailPessoaAtual().toLowerCase());

		DpPessoa pesAtiva = CpDao.getInstance().consultarPorIdInicial(pes.getIdInicial());
		if (pesAtiva == null) {
			usu.addEmail(pes.getPessoaAtual().getEmailPessoaAtual().toLowerCase());
		} else {
			usu.addEmail(pesAtiva.getEmailPessoa());
		}

		usu.setCriarEmail(deveCriarEmail(pes));

		usu.setNomeResolucaoEmail(pes.getNomePessoa());

		usu.setNomeExibicao(pes.getNomeExibicao());

		if (pes.getCpTipoPessoa() != null) {
			usu.setTipoPessoa(pes.getCpTipoPessoa().getDscTpPessoa());
		}

		try {
			if (rCxPostal.getRegra(pes) != null) {
				usu.setHomeMDB(rCxPostal.getRegra(pes).getHomeMDB());
				usu.setTemplateLink(rCxPostal.getRegra(pes).getTemplateLink());
			}
		} catch (AplicacaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BASE64Encoder enc64 = new BASE64Encoder();
		CpDao dao = CpDao.getInstance();
		for (CpIdentidade cpId : dao.consultaIdentidades(pes)) {
			if (cpId.getCpTipoIdentidade().getIdCpTpIdentidade().equals(1)) {
				if (sincSenhas && cpId.getDscSenhaIdentidadeCriptoSinc() != null) {
					if ((dao.consultarDataEHoraDoServidor().getTime() - conf.getTempoLimiteSincSenha()) >= cpId
							.getDtCriacaoIdentidade().getTime()) {
						throw new AplicacaoException("A senha do usuário " + pes.getSigla()
								+ " excedeu o tempo limite de sincronismo (" + conf.getTempoLimiteSincSenha() + "). \n"
								+ "Verifique se a conta do usuário já existe no LDAP e defina como NULL o campo SENHA_IDENTIDADE_CRIPTO_SINC e SENHA_IDENTIDADE_CRIPTO antes de executar o sincronismo novamente");
					} else {
						usu.setChaveCripto(enc64.encode(pes.getIdInicial().toString().getBytes()));
						usu.setSenhaCripto(cpId.getDscSenhaIdentidadeCriptoSinc());
					}

				} else {
					usu.setChaveCripto(null);
					usu.setSenhaCripto(null);
				}
			}
		}

		al.put(usu.getNome(), usu);
		DpLotacao lotacao = pes.getLotacao();

		// processa lotacao da pessoa
		if (lotacaoPrincipal(lotacao)) {

			if (!lotacao.getSigla().contains("(extinta)")) {
				AdGrupoDeDistribuicao gdLot = processarGrupoDistribuicao(lotacao, uoGrpDistr);

				gdLot.acrescentarMembro(usu);

				if (conf.getSincronizarGruposSeguranca()) {
					processarGrupoSegurancaAuto(pes, pes.getLotacao(), uoGrpSeguranca);
//					gdSeg.acrescentarMembro(usu);
					processarPermissoesGruposSeguranca(pes, usu);
				}
			}
		} else {
			processarPermissoesGruposSeguranca(pes, usu);
		}
	}

	// TODO: Eliminar quando o tipo de agrupamento estiver disponivel na lotacao
	private boolean lotacaoPrincipal(DpLotacao lotacao) {
		for (DpLotacao lotNaoPrinc : lotacoesNaoPrincipais) {
			if (lotNaoPrinc.equivale(lotacao)) {
				return false;
			}
		}
		return true;
	}

	// TODO: Eliminar quando o tipo de agrupamento estiver disponivel na lotacao
	private void registrarLotacoesNaoPrincipais() {

		List<CpPapel> listaPapeis = CpDao.getInstance().listarAtivos(CpPapel.class, "hisDtFim",
				Long.valueOf(conf.getIdLocalidade()));

		for (CpPapel cpPapel : listaPapeis) {
			if (cpPapel.getCpTipoPapel().getIdCpTpPapel().longValue() != CP_TIPO_PAPEL_PRINCIPAL) {
				lotacoesNaoPrincipais.add(cpPapel.getDpLotacao());
			}
		}

	}

	/**
	 * Verifica se deve criar email ao criar a conta do usuario
	 * 
	 * @param pes
	 * @return
	 */
	private boolean deveCriarEmail(DpPessoa pes) {
		try {
			Map<String, DpPessoa> params = new HashMap<String, DpPessoa>();
			params.put("pessoa", pes);
			return (Boolean) MVEL.eval(conf.getExpressaoUsuariosCriarEmail(), params);

		} catch (Exception e) {
			return true;
		}
	}

	private void processarGruposManuais() {
		CpDao dao = CpDao.getInstance();

		List<CpGrupo> listaGrupos = new ArrayList<>();
		listaGrupos.addAll(dao.listarAtivos(CpGrupoDeEmail.class, null));

		if (conf.getSincronizarGruposSeguranca()) {
			listaGrupos.addAll(dao.listarAtivos(CpPerfil.class, null));
			listaGrupos.addAll(dao.listarAtivos(CpPerfilJEE.class, null));
		}

		int totalGrupos = listaGrupos.size();
		int g = 0;
		System.out.println("Processando " + totalGrupos + " grupos...");
		for (CpGrupo grp : listaGrupos) {
			g++;
			if (g % 10 == 0)
				System.out.println(g + " de " + totalGrupos);
			AdGrupo adGrupo = null;
			try {
				List<ConfiguracaoGrupo> listaCfgGrupo = Cp.getInstance().getConf().obterCfgGrupo(grp);
				String nomeGrupo = null;
				if (grp instanceof CpGrupoDeEmail) {
					nomeGrupo = conf.getPfxGrpDistrManualEmail() + grp.getSigla().toLowerCase()
							+ conf.getSfxGrpDistrManualEmail();
					adGrupo = new AdGrupoDeDistribuicao(nomeGrupo, nomeGrupo, uoGrpDistr, conf.getDnDominio());
					verificarSobreposicaoGrupo(adGrupo);
				}

				if (conf.getSincronizarGruposSeguranca()) {
					if (grp instanceof CpPerfil) {
						nomeGrupo = conf.getPfxGrpSegManualPerfil() + grp.getSigla().toLowerCase()
								+ conf.getSfxGrpSegManualPerfil();
						adGrupo = new AdGrupoDeSeguranca(nomeGrupo, nomeGrupo, uoGrpSeguranca, conf.getDnDominio());
					}

					if (grp instanceof CpPerfilJEE) {
						nomeGrupo = conf.getPfxGrpSegManualPerfilJEE() + grp.getSigla().toLowerCase()
								+ conf.getSfxGrpSegManualPerfilJEE();
						adGrupo = new AdGrupoDeSeguranca(nomeGrupo, nomeGrupo, uoGrpSeguranca, conf.getDnDominio());
					}
				}

				Map<String, DpPessoa> pessoaMap = new HashMap<String, DpPessoa>();
				AdObjeto adMembro;

				for (ConfiguracaoGrupo cfgGrupo : listaCfgGrupo) {

					adMembro = null;

					if (cfgGrupo.getTipo().equals(TipoConfiguracaoGrupoEnum.EMAIL)) {
						String emailContato = cfgGrupo.getConteudoConfiguracao();
						adMembro = new AdContato(emailContato, emailContato, uoContatos, conf.getDnDominio());
						al.put(adMembro.getNome(), adMembro);
						adGrupo.acrescentarMembro(adMembro);
					}

					if (cfgGrupo.getTipo().equals(TipoConfiguracaoGrupoEnum.FORMULA)) {
						for (DpPessoa p : getListaPessoas()) {
							adMembro = null;
							pessoaMap.put("pessoa", p);
							if ((Boolean) MVEL.eval(cfgGrupo.getCpConfiguracao().getDscFormula(), pessoaMap))
								adMembro = recuperarPorNome(p.getSesbPessoa() + p.getMatricula());

							if (adMembro != null) {
								adGrupo.acrescentarMembro(adMembro);
							}
						}
					}
					if (cfgGrupo.getTipo().equals(TipoConfiguracaoGrupoEnum.PESSOA)) {
						DpPessoa p = cfgGrupo.getCpConfiguracao().getDpPessoa();
						adMembro = recuperarPorNome(p.getSesbPessoa() + p.getMatricula());
						if (adMembro != null) {
							adGrupo.acrescentarMembro(adMembro);
						} else {
							tratarPessoaComoContato(adGrupo, p);
						}
					}

					if (cfgGrupo.getTipo().equals(TipoConfiguracaoGrupoEnum.LOTACAO)) {
						String sufixo = null;
						String prefixo = null;
						if (adGrupo instanceof AdGrupoDeDistribuicao) {
							prefixo = conf.getPfxGrpDistrAuto();
							sufixo = conf.getSfxGrpDistrAuto();
						}
						if (adGrupo instanceof AdGrupoDeSeguranca) {
							prefixo = mapSvcSinc.get("FS-RAIZ").get("pfx");
							sufixo = mapSvcSinc.get("FS-RAIZ").get("sfx");
						}
						DpLotacao l = dao.consultar(Long.valueOf(cfgGrupo.getConteudoConfiguracao()), DpLotacao.class,
								false);
						adMembro = recuperarPorNome(prefixo + l.getSigla() + sufixo);
						if (adMembro == null) {
							prefixo = conf.getPfxGrpDistrAuto();
							sufixo = conf.getSfxGrpDistrManualEmail();
							adMembro = recuperarPorNome(prefixo + l.getSigla() + sufixo);
						}
						if (adMembro != null) {
							adGrupo.acrescentarMembro(adMembro);
						}
					}

					if (cfgGrupo.getTipo().equals(TipoConfiguracaoGrupoEnum.FUNCAOCONFIANCA)) {
						List<DpPessoa> listaPessoas = dao
								.consultarPessoasComFuncaoConfianca(Long.valueOf(cfgGrupo.getConteudoConfiguracao()));

						for (DpPessoa p : listaPessoas) {
							adMembro = null;
							adMembro = recuperarPorNome(p.getSesbPessoa() + p.getMatricula());

							if (adMembro != null) {
								adGrupo.acrescentarMembro(adMembro);
							}
						}
					}

					if (cfgGrupo.getTipo().equals(TipoConfiguracaoGrupoEnum.CARGO)) {
						List<DpPessoa> listaPessoas = dao
								.consultarPessoasComCargo(Long.valueOf(cfgGrupo.getConteudoConfiguracao()));

						for (DpPessoa p : listaPessoas) {
							adMembro = null;
							adMembro = recuperarPorNome(p.getSesbPessoa() + p.getMatricula());
							if (adMembro != null) {
								adGrupo.acrescentarMembro(adMembro);
							}
						}
					}

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			al.put(adGrupo.getNome(), adGrupo);

		}
	}

	/**
	 * Se a pessoa não estiver na localidade do sincronismo, o sistema tenta
	 * cadastrá-la como objeto externo.
	 * 
	 * @param al
	 * @param adGrupo
	 * @param p
	 */
	private void tratarPessoaComoContato(AdGrupo adGrupo, DpPessoa p) {
		if (adGrupo instanceof AdGrupoDeDistribuicao) {
			AdObjeto adMembro;
			if (p.getOrgaoUsuario().getId().longValue() != Long.valueOf(conf.getIdLocalidade())) {

				// a pesquisa do e-mail da pessoa eh mais rápida dessa forma do que pesquisar
				// direto com
				// o método getEmailPessoaAtual()
				DpPessoa pesAtiva = CpDao.getInstance().consultarPorIdInicial(p.getIdInicial());
				String email = null;

				if (pesAtiva == null) {
					email = p.getPessoaAtual().getEmailPessoaAtual().toLowerCase();
				} else {
					email = pesAtiva.getEmailPessoa();
				}

				adMembro = recuperarPorNome(email);

				if (adMembro == null) {
					adMembro = new AdContato(email, email, uoContatos, conf.getDnDominio());
				}
				al.put(adMembro.getNome(), adMembro);
				adGrupo.acrescentarMembro(adMembro);

			}
		}
	}

	/**
	 * Se um grupo manual estiver com o mesmo prefixo de um grupo automático, o
	 * grupo automático será desconsiderado e prevalecerá o grupo manual
	 * 
	 * @param grpNovo
	 * @param al
	 */
	private void verificarSobreposicaoGrupo(AdGrupo grpNovo) {
		AdObjeto o = al.get(grpNovo.getNome().replace(conf.getSfxGrpDistrManualEmail(), conf.getSfxGrpDistrAuto()));
		if (o != null && o instanceof AdGrupoDeDistribuicao) {
			AdGrupoDeDistribuicao grpAntigo = (AdGrupoDeDistribuicao) o;

			for (AdGrupo gp : grpAntigo.getGruposPertencentes()) {
				gp.removerMembro(grpAntigo);
				gp.acrescentarMembro(grpNovo);
			}
			al.remove(grpAntigo.getNome());
		}
	}

	/**
	 * Verifica se a pessoa pode fazer parte do grupo e, caso possa, inclui a pessoa
	 * no grupo.
	 * 
	 * @param idSvc     - serviço que será verificado se a pessoa tem permissão para
	 *                  fazer parte
	 * @param pes       - pessoa a ser verificada
	 * @param lot       - lotação que o usuário deve ter permissão
	 * @param idTpSvc   - tipo do serviço a ser verificado
	 * @param prxGrpSvc - prefixo do grupo que será utilizado quando escrito no AD
	 * @param sfxGrpSvc - sufixo do grupo que será utilizado quando escrito no AD
	 * @param al        - lista de objetos já processados e fará parte do
	 *                  sincronismo
	 * @param usu       - objeto do AD que será inserido como membro, caso a pessoa
	 *                  tenha permissão
	 */
	private void processarPermissao(CpServico svc, DpPessoa pes, DpLotacao lot, CpTipoDeConfiguracao idTpSvc,
			String cnGrupo, AdUsuario usu) {

		try {
			boolean podeSvc = Cp.getInstance().getConf().podePorConfiguracao(pes, lot, svc, idTpSvc);

			if (podeSvc) {
				criarAdGrpSegOutraLotacao(pes, lot, cnGrupo);
				addMembroGrp(cnGrupo, lot.getSigla(), usu);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Cria grupo de segurança de outra lotação caso ainda não esteja na lista geral
	 * de objetos do modelo. Esse método foi inserir após perceber que as permissões
	 * para outras lotações só funcionam se já tiver processado uma pessoa da
	 * lotação destino
	 * 
	 * @param pes
	 * @param cnGrupo
	 * @param al
	 */
	private void criarAdGrpSegOutraLotacao(DpPessoa pes, DpLotacao outraLot, String cnGrupo) {
		AdGrupoDeSeguranca grp = ((AdGrupoDeSeguranca) recuperarPorNome(cnGrupo));
		if (grp == null) {
			processarGrupoSegurancaAuto(pes, outraLot, uoGrpSeguranca);
		}
	}

	/**
	 * Coloca os usuários dentro dos grupos de segurança definidos no self service
	 * de serviços (GAB,SEC,JUIZ, PUB e RAIZ).
	 * 
	 * @param al
	 * @param pes
	 * @param usu
	 */
	private void processarPermissoesGruposSeguranca(final DpPessoa pes, final AdUsuario usu) {

		// processar servicos sincronizaveis
		for (ServicoSincronizavel ss : mapSvcSinc.values()) {
			String cnGrpSvc = null;
			if (Boolean.valueOf(ss.get("condensado"))) {
				cnGrpSvc = resolverNomeGrpCondensado(ss);
			} else {
				cnGrpSvc = resolverNomeGrpNaoCondensado(pes.getLotacao(), ss);
			}
			processarPermissao(ss.getCpServico(), pes, pes.getLotacao(), CpTipoDeConfiguracao.UTILIZAR_SERVICO,
					cnGrpSvc, usu);
		}

		// processar servicos de outra lotacao
		Set<DpLotacao> lotacoesGSManualPes = Cp.getInstance().getConf().getLotacoesGrupoSegManual(pes);
		for (DpLotacao outraLot : lotacoesGSManualPes) {
			// servicos sincronizaveis (outras lotacoes)
			for (ServicoSincronizavel ss : mapSvcSinc.values()) {
				String cnGrpSvc = null;
				if (Boolean.valueOf(ss.get("condensado"))) {
					cnGrpSvc = resolverNomeGrpCondensado(ss);
				} else {
					cnGrpSvc = resolverNomeGrpNaoCondensado(outraLot, ss);
				}
				processarPermissao(ss.getCpServico(), pes, outraLot,
						CpTipoDeConfiguracao.UTILIZAR_SERVICO_OUTRA_LOTACAO, cnGrpSvc, usu);
			}

		}
	}

	private String resolverNomeGrpNaoCondensado(final DpLotacao lot, ServicoSincronizavel ss) {

		String servico = ss.get("svc");
		String prefixo = mapSvcSinc.get(servico).get("pfx");
		String sufixo = mapSvcSinc.get(servico).get("sfx");
		String siglaLotacao = lot.getSigla().toLowerCase();

		if (servico.startsWith("FS-") && !sufixo.equals("_svc_gs")) {
			return prefixo + siglaLotacao + sufixo;
		}
		return prefixo + siglaLotacao + "_" + ss.getCpServico().getSiglaServico().toLowerCase() + sufixo;
	}

	private String resolverNomeGrpCondensado(ServicoSincronizavel ss) {
		return mapSvcSinc.get(ss.get("svc")).get("pfx") + ss.getCpServico().getSiglaServico().toLowerCase()
				+ mapSvcSinc.get(ss.get("svc")).get("sfx");
	}

	/**
	 * Adiciona um usuário como membro de um grupo seguraça de uma lotação
	 * 
	 * @param pfxGrp - prefixo utilizado pelo grupo
	 * @param sigla  - sigla da lotação ao qual o usuário será adicionado como
	 *               membro
	 * @param sfxGrp - sufixo utilizado pelo grupo
	 * @param al     - lista com todos os objetos do modelo. Usado para reaproveitar
	 *               o objeto existente
	 * @param usu    - o usuaário que será adicionado como membro
	 */
	private void addMembroGrp(String cnGrupo, String siglaLotacao, AdUsuario usu) {

		AdGrupoDeSeguranca grp = ((AdGrupoDeSeguranca) recuperarPorNome(cnGrupo));
		if (grp != null) {
			grp.acrescentarMembro(usu);
		}

	}

	/**
	 * Cria os grupos da lotação.
	 * 
	 * @param pes     - a pessoa que terá as permissões verificadas
	 * @param lotacao - lotação a ser utiizada como base
	 * @param al      - lista de objetos que serão utilizados na sincronização
	 * @param uo      - unidade organizacional onde os grupos de segurança serão
	 *                criados no AD
	 * @return - grupo de segurança criado
	 */
	private void processarGrupoSegurancaAuto(DpPessoa pes, DpLotacao lotacao, AdUnidadeOrganizacional uo) {
		
//		System.out.println("processarGrupoSegurancaAuto: " + lotacao.getSigla() + " - " + uo.getNomeCompleto());

		// cria os grupos de servicos
		AdGrupoDeSeguranca gsSvc = null;
		for (ServicoSincronizavel ss : mapSvcSinc.values()) {
			if (Boolean.valueOf(ss.get("condensado"))) {
				String nomeGrpSvc = resolverNomeGrpCondensado(ss);
				gsSvc = (AdGrupoDeSeguranca) recuperarPorNome(nomeGrpSvc);
				if (gsSvc == null) {
					gsSvc = new AdGrupoDeSeguranca(nomeGrpSvc, nomeGrpSvc, uo, conf.getDnDominio());
					al.put(gsSvc.getNome(), gsSvc);
				}
			} else {
				boolean podeGerarGrupo = false;
				try {
					podeGerarGrupo = Cp.getInstance().getConf().podePorConfiguracao(pes, lotacao, ss.getCpServico(),
							CpTipoDeConfiguracao.HABILITAR_SERVICO_DE_DIRETORIO)
							|| Cp.getInstance().getConf().podePorConfiguracao(pes, lotacao, ss.getCpServico(),
									CpTipoDeConfiguracao.HABILITAR_SERVICO)
							|| Cp.getInstance().getConf().podePorConfiguracao(pes, lotacao, ss.getCpServico(),
									CpTipoDeConfiguracao.UTILIZAR_SERVICO);
				} catch (Exception e) {
					throw new RuntimeException("Erro verificando se pode gerar grupo", e);
				}

				if (podeGerarGrupo) {
					String lotSvc = resolverNomeGrpNaoCondensado(lotacao, ss);
					gsSvc = (AdGrupoDeSeguranca) recuperarPorNome(lotSvc);
					if (gsSvc == null) {
						gsSvc = new AdGrupoDeSeguranca(lotSvc, lotSvc, uo, conf.getDnDominio());
						al.put(gsSvc.getNome(), gsSvc);
					}
				}
			}

		}

	}

	private AdGrupoDeDistribuicao processarGrupoDistribuicao(DpLotacao lotacao, AdUnidadeOrganizacional uo) {
		// return processarGrupo(AdGrupoDeDistribuicao.class,lotacao, al, uo,
		// "GrupoDeEmail:", "ts", "");
		String lot = conf.getPfxGrpDistrAuto() + lotacao.getSigla().toLowerCase() + conf.getSfxGrpDistrAuto();

		AdGrupoDeDistribuicao gdLot = (AdGrupoDeDistribuicao) recuperarPorNome(lot);
		if (gdLot == null) {
			// AdGrupoDeDistribuicao gdd = new AdGrupoDeDistribuicao(lot,
			// "GrupoDeEmail:" + lot, uo);
			AdGrupoDeDistribuicao gdd = new AdGrupoDeDistribuicao(lot, lot, uo, conf.getDnDominio());

			al.put(gdd.getNome(), gdd);
			gdLot = gdd;
			while (lotacao.getLotacaoPai() != null && !lotacao.getLotacaoPai().getSigla().contains("(extinta)")) {
				String lotPai = conf.getPfxGrpDistrAuto() + lotacao.getLotacaoPai().getSigla().toLowerCase()
						+ conf.getSfxGrpDistrAuto();
				AdGrupoDeDistribuicao gdLotPai = (AdGrupoDeDistribuicao) recuperarPorNome(lotPai);
				if (gdLotPai == null) {
					// AdGrupoDeDistribuicao gdPai = new AdGrupoDeDistribuicao(
					// lotPai, "GrupoDeEmail:" + lotPai, uo);
					AdGrupoDeDistribuicao gdPai = new AdGrupoDeDistribuicao(lotPai, lotPai, uo, conf.getDnDominio());

					al.put(gdPai.getNome(), gdPai);
					gdLotPai = gdPai;
				}
				gdLotPai.acrescentarMembro(gdd);
				gdd = gdLotPai;
				lotacao = lotacao.getLotacaoPai();
			}

		}

		return gdLot;
	}

	private AdObjeto recuperarPorNome(String nomeObj) {
		return al.get(nomeObj);
	}

	public List<DpPessoa> getListaPessoas() {
		return listaPessoas;
	}

	class ListaObjetos extends HashMap<String, AdObjeto> {

		@Override
		public AdObjeto put(String key, AdObjeto value) {
			// TODO Auto-generated method stub
			return super.put(key.toLowerCase(), value);
		}

		@Override
		public AdObjeto get(Object key) {
			return super.get(key.toString().toLowerCase());
		}
	}

}
