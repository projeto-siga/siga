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
package br.gov.jfrj.ldap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import sun.misc.BASE64Encoder;
import br.gov.jfrj.ldap.sinc.SincProperties;
import br.gov.jfrj.ldap.sinc.resolvedores.ResolvedorRegrasCaixaPostal;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpGrupo;
import br.gov.jfrj.siga.cp.CpGrupoDeEmail;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpPerfil;
import br.gov.jfrj.siga.cp.CpPerfilJEE;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.grupo.ConfiguracaoGrupo;
import br.gov.jfrj.siga.cp.grupo.TipoConfiguracaoGrupoEnum;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import bsh.Interpreter;

public class AdModelo {

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

	private AdModelo(SincProperties conf) {
		this.conf = conf;

		uoGI = new AdUnidadeOrganizacional(conf.getNomeContainerGI(), conf
				.getNomeContainerGI(), conf.getDnDominio());
		uoGrpDistr = new AdUnidadeOrganizacional(conf
				.getNomeContainerGrpDistr(), conf.getNomeContainerGrpDistr(),
				uoGI, conf.getDnDominio());
		uoUsuarios = new AdUnidadeOrganizacional(conf
				.getNomeContainerUsuarios(), conf.getNomeContainerUsuarios(),
				uoGI, conf.getDnDominio());
		uoUsuariosInativos = new AdUnidadeOrganizacional(conf
				.getNomeContainerUsuariosInativos(), conf
				.getNomeContainerUsuariosInativos(), uoGI, conf.getDnDominio());
		uoGrpDistrInativos = new AdUnidadeOrganizacional(conf
				.getNomeContainerGrpDistrInativos(), conf
				.getNomeContainerGrpDistrInativos(), uoGI, conf.getDnDominio());
		uoContatos = new AdUnidadeOrganizacional(conf
				.getNomeContainerContatos(), conf.getNomeContainerContatos(),
				uoGI, conf.getDnDominio());
		uoGrpSeguranca = new AdUnidadeOrganizacional(conf
				.getNomeContainerGrpSeg(), conf.getNomeContainerGrpSeg(), uoGI,
				conf.getDnDominio());

		uoGrpSegInativos = new AdUnidadeOrganizacional(conf
				.getNomeContainerGrpSegInativos(), conf
				.getNomeContainerGrpSegInativos(), uoGI, conf.getDnDominio());
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

	/**
	 * Retorna uma instância única (singleton)
	 * 
	 * @return
	 * @throws IOException
	 */
	public static AdModelo getInstance() {
		if (instance == null) {
			instance = new AdModelo(SincProperties.getInstancia());
		}
		return instance;
	}

	public List<AdObjeto> gerarModelo() {
		try {
			rCxPostal = ResolvedorRegrasCaixaPostal.getInstancia(conf);
		} catch (AplicacaoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		CpDao dao = CpDao.getInstance();
		// List<AdObjeto> al = new ArrayList<AdObjeto>();
		Map<String, AdObjeto> al = new HashMap<String, AdObjeto>();

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

		List<DpPessoa> list = consultarPessoas();

		BASE64Encoder enc64 = new BASE64Encoder();

		// boolean interromperSincronismo = false;
		for (DpPessoa pes : list) {

			String nomePessoa = conf.getPrefixoMatricula()
					+ pes.getMatricula().toString();

			AdUsuario usu = null;

			usu = new AdUsuario(nomePessoa, nomePessoa, uoUsuarios, conf
					.getDnDominio());

			usu.setSigla(pes.getSiglaPessoa().toLowerCase());
			// usu.setEmail(pes.getEmailPessoa().toLowerCase());
			usu.addEmail(pes.getEmailPessoa().toLowerCase());
			usu.setNomeResolucaoEmail(pes.getNomePessoa());

			usu.setNomeExibicao(pes.getNomeExibicao() != null ? pes
					.getNomeExibicao() : pes.getNomePessoa());
			try {
				usu.setHomeMDB(rCxPostal.getRegra(pes).getHomeMDB());
				usu.setTemplateLink(rCxPostal.getRegra(pes).getTemplateLink());
			} catch (AplicacaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (CpIdentidade cpId : dao.consultaIdentidades(pes)) {
				if (cpId.getCpTipoIdentidade().getIdCpTpIdentidade().equals(1)) {
					usu.setChaveCripto(enc64.encode(pes.getIdInicial()
							.toString().getBytes()));
					usu.setSenhaCripto(cpId.getDscSenhaIdentidadeCriptoSinc());
				}
			}

			al.put(usu.getNome(), usu);
			DpLotacao lotacao = pes.getLotacao();

			if (!lotacao.getSigla().contains("(extinta)")) {
				AdGrupoDeDistribuicao gdLot = processarGrupoDistribuicao(
						lotacao, al, uoGrpDistr);

				gdLot.acrescentarMembro(usu);

				if (conf.getSincronizarGruposSeguranca()) {
					AdGrupoDeSeguranca gdSeg = processarGrupoSeguranca(lotacao,
							al, uoGrpSeguranca);
					gdSeg.acrescentarMembro(usu);
					processarPermissoesGruposSeguranca(al, pes, usu);
				}
			}

		}

		processarGruposManuais(al);

		List<AdObjeto> modelo = new ArrayList<AdObjeto>();
		modelo.addAll(al.values());
		return modelo;
	}

	private void processarGruposManuais(Map<String, AdObjeto> al) {
		Interpreter i = new Interpreter();
		CpDao dao = CpDao.getInstance();

		CpGrupo cpGrpEmail = new CpGrupoDeEmail();
		cpGrpEmail.setHisAtivo(1);
		List<CpGrupo> listaGrupos = dao.consultar(cpGrpEmail, null);

		if (conf.getSincronizarGruposSeguranca()) {
			CpGrupo cpPerfil = new CpPerfil();
			cpPerfil.setHisAtivo(1);
			listaGrupos.addAll(dao.consultar(cpPerfil, null));

			CpGrupo cpPerfilJEE = new CpPerfilJEE();
			cpPerfilJEE.setHisAtivo(1);
			listaGrupos.addAll(dao.consultar(cpPerfilJEE, null));
		}
		
		for (CpGrupo grp : listaGrupos) {

			AdGrupo adGrupo = null;
			try {
				List<ConfiguracaoGrupo> listaCfgGrupo = Cp.getInstance()
						.getConf().obterCfgGrupo(grp);
				String nomeGrupo = null;
				if (grp instanceof CpGrupoDeEmail) {
					nomeGrupo = conf.getPfxGrpDistrManualEmail()
							+ grp.getSigla().toLowerCase()
							+ conf.getSfxGrpDistrManualEmail();
					adGrupo = new AdGrupoDeDistribuicao(nomeGrupo, nomeGrupo,
							uoGrpDistr, conf.getDnDominio());
					verificarSobreposicaoGrupo(adGrupo, al);
				}

				if (conf.getSincronizarGruposSeguranca()) {
					if (grp instanceof CpPerfil) {
						nomeGrupo = conf.getPfxGrpSegManualPerfil()
								+ grp.getSigla().toLowerCase()
								+ conf.getSfxGrpSegManualPerfil();
						adGrupo = new AdGrupoDeSeguranca(nomeGrupo, nomeGrupo,
								uoGrpSeguranca, conf.getDnDominio());
					}

					if (grp instanceof CpPerfilJEE) {
						nomeGrupo = conf.getPfxGrpSegManualPerfilJEE()
								+ grp.getSigla().toLowerCase()
								+ conf.getSfxGrpSegManualPerfilJEE();
						adGrupo = new AdGrupoDeSeguranca(nomeGrupo, nomeGrupo,
								uoGrpSeguranca, conf.getDnDominio());
					}
				}
				AdObjeto adMembro;
				for (ConfiguracaoGrupo cfgGrupo : listaCfgGrupo) {

					adMembro = null;

					if (cfgGrupo.getTipo().equals(
							TipoConfiguracaoGrupoEnum.EMAIL)) {
						String emailContato = cfgGrupo
								.getConteudoConfiguracao();
						adMembro = new AdContato(emailContato, emailContato,
								uoContatos, conf.getDnDominio());
						al.put(adMembro.getNome(), adMembro);
						adGrupo.acrescentarMembro(adMembro);
					}

					if (cfgGrupo.getTipo().equals(
							TipoConfiguracaoGrupoEnum.FORMULA)) {
						for (DpPessoa p : consultarPessoas()) {
							adMembro = null;
							i.set("pessoa", p); // Set variables
							if ((Boolean) i.eval(cfgGrupo.getCpConfiguracao()
									.getDscFormula()))
								adMembro = recuperarPorNome(conf
										.getPrefixoMatricula()
										+ p.getMatricula(), al);

							if (adMembro != null) {
								adGrupo.acrescentarMembro(adMembro);
							}
						}
					}
					if (cfgGrupo.getTipo().equals(
							TipoConfiguracaoGrupoEnum.PESSOA)) {
						DpPessoa p = dao.consultar(Long.valueOf(cfgGrupo
								.getConteudoConfiguracao()), DpPessoa.class,
								false);
						adMembro = recuperarPorNome(conf.getPrefixoMatricula()
								+ p.getMatricula(), al);
						if (adMembro != null) {
							adGrupo.acrescentarMembro(adMembro);
						}
					}

					if (cfgGrupo.getTipo().equals(
							TipoConfiguracaoGrupoEnum.LOTACAO)) {
						String sufixo = null;
						String prefixo = null;
						if (adGrupo instanceof AdGrupoDeDistribuicao) {
							prefixo = conf.getPfxGrpDistrAuto();
							sufixo = conf.getSfxGrpDistrAuto();
						}
						if (adGrupo instanceof AdGrupoDeSeguranca) {
							prefixo = conf.getPfxGrpSegAuto();
							sufixo = conf.getSfxGrpSegAuto();
						}
						DpLotacao l = dao.consultar(Long.valueOf(cfgGrupo
								.getConteudoConfiguracao()), DpLotacao.class,
								false);
						adMembro = recuperarPorNome(prefixo + l.getSigla()
								+ sufixo, al);
						if (adMembro != null) {
							adGrupo.acrescentarMembro(adMembro);
						}
					}

					if (cfgGrupo.getTipo().equals(
							TipoConfiguracaoGrupoEnum.FUNCAOCONFIANCA)) {
						List<DpPessoa> listaPessoas = dao
								.consultarPessoasComFuncaoConfianca(Long
										.valueOf(cfgGrupo
												.getConteudoConfiguracao()));

						for (DpPessoa p : listaPessoas) {
							adMembro = null;
							adMembro = recuperarPorNome(conf
									.getPrefixoMatricula()
									+ p.getMatricula(), al);

							if (adMembro != null) {
								adGrupo.acrescentarMembro(adMembro);
							}
						}
					}

					if (cfgGrupo.getTipo().equals(
							TipoConfiguracaoGrupoEnum.CARGO)) {
						List<DpPessoa> listaPessoas = dao
								.consultarPessoasComCargo(Long.valueOf(cfgGrupo
										.getConteudoConfiguracao()));

						for (DpPessoa p : listaPessoas) {
							adMembro = null;
							adMembro = recuperarPorNome(conf
									.getPrefixoMatricula()
									+ p.getMatricula(), al);
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
	 * Se um grupo manual estiver com o mesmo prefixo de um grupo automático, o
	 * grupo automático será desconsiderado e prevalecerá o grupo manual
	 * 
	 * @param grpNovo
	 * @param al
	 */
	private void verificarSobreposicaoGrupo(AdGrupo grpNovo,
			Map<String, AdObjeto> al) {
		AdObjeto o = al.get(grpNovo.getNome().replace(
				conf.getSfxGrpDistrManualEmail(), conf.getSfxGrpDistrAuto()));
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
	 * Coloca os usuários dentro dos grupos de segurança definidos no self
	 * service de serviços (GAB,SEC,JUIZ, PUB e RAIZ).
	 * 
	 * @param al
	 * @param pes
	 * @param usu
	 */
	private void processarPermissoesGruposSeguranca(
			final Map<String, AdObjeto> al, final DpPessoa pes,
			final AdUsuario usu) {
		CpDao dao = CpDao.getInstance();
		try {
			long idSvcFS_SEC = conf.getIdSvcFilesystemSecretaria();
			long idSvcFS_GAB = conf.getIdSvcFilesystemGabinete();
			long idSvcFS_JUIZ = conf.getIdSvcFilesystemJuiz();
			long idSvcFS_PUB = conf.getIdSvcFilesystemPub();
			long idSvcFS_Raiz = conf.getIdSvcFilesystemRaiz();

			CpServico svcFsSec = dao.consultar(idSvcFS_SEC, CpServico.class,
					false);
			CpServico svcFsGab = dao.consultar(idSvcFS_GAB, CpServico.class,
					false);
			CpServico svcFsJuiz = dao.consultar(idSvcFS_JUIZ, CpServico.class,
					false);
			CpServico svcFsPub = dao.consultar(idSvcFS_PUB, CpServico.class,
					false);
			CpServico svcFsRaiz = dao.consultar(idSvcFS_Raiz, CpServico.class,
					false);

			boolean podeFsSec = Cp.getInstance().getConf().podePorConfiguracao(
					pes, pes.getLotacao(), svcFsSec,
					CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO);
			boolean podeFsGab = Cp.getInstance().getConf().podePorConfiguracao(
					pes, pes.getLotacao(), svcFsGab,
					CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO);
			boolean podeFsJuiz = Cp.getInstance().getConf()
					.podePorConfiguracao(pes, pes.getLotacao(), svcFsJuiz,
							CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO);
			boolean podeFsPub = Cp.getInstance().getConf().podePorConfiguracao(
					pes, pes.getLotacao(), svcFsPub,
					CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO);
			boolean podeFsRaiz = Cp.getInstance().getConf()
			.podePorConfiguracao(pes, pes.getLotacao(), svcFsRaiz,
					CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO);

			if (podeFsSec) {
				String lot = conf.getPfxGrpSegAutoSec()
						+ pes.getLotacao().getSigla().toLowerCase()
						+ conf.getSfxGrpSegAutoSec();
				AdGrupoDeSeguranca grp = ((AdGrupoDeSeguranca) recuperarPorNome(
						lot, al));
				if (grp != null) {
					grp.acrescentarMembro(usu);
				}
			}

			if (podeFsGab) {
				String lot = conf.getPfxGrpSegAutoGab()
						+ pes.getLotacao().getSigla().toLowerCase()
						+ conf.getSfxGrpSegAutoGab();
				AdGrupoDeSeguranca grp = ((AdGrupoDeSeguranca) recuperarPorNome(
						lot, al));
				if (grp != null) {
					grp.acrescentarMembro(usu);
				}
			}

			if (podeFsJuiz) {
				String lot = conf.getPfxGrpSegAutoJuiz()
						+ pes.getLotacao().getSigla().toLowerCase()
						+ conf.getSfxGrpSegAutoJuiz();
				AdGrupoDeSeguranca grp = ((AdGrupoDeSeguranca) recuperarPorNome(
						lot, al));
				if (grp != null) {
					grp.acrescentarMembro(usu);
				}
			}

			if (podeFsPub) {
				String lot = conf.getPfxGrpSegAutoPub()
						+ pes.getLotacao().getSigla().toLowerCase()
						+ conf.getSfxGrpSegAutoPub();
				AdGrupoDeSeguranca grp = ((AdGrupoDeSeguranca) recuperarPorNome(
						lot, al));
				if (grp != null) {
					grp.acrescentarMembro(usu);
				}
			}

			if (podeFsRaiz) {
				String lot = conf.getPfxGrpSegAutoRaiz()
						+ pes.getLotacao().getSigla().toLowerCase()
						+ conf.getSfxGrpSegAutoRaiz();
				AdGrupoDeSeguranca grp = ((AdGrupoDeSeguranca) recuperarPorNome(
						lot, al));
				if (grp != null) {
					grp.acrescentarMembro(usu);
				}
			}

			Set<DpLotacao> lotacoesGSManualPes = Cp.getInstance().getConf().getLotacoesGrupoSegManual(pes);
			for (DpLotacao outraLot: lotacoesGSManualPes) {
				
				boolean podeFSSecOutraLot = Cp.getInstance().getConf().podePorConfiguracao(pes, outraLot,svcFsSec,CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO_OUTRA_LOTACAO);
				boolean podeFSGabOutraLot = Cp.getInstance().getConf().podePorConfiguracao(pes, outraLot,svcFsGab,CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO_OUTRA_LOTACAO);
				boolean podeFSJuizOutraLot = Cp.getInstance().getConf().podePorConfiguracao(pes, outraLot,svcFsJuiz,CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO_OUTRA_LOTACAO);
				boolean podeFSPubOutraLot = Cp.getInstance().getConf().podePorConfiguracao(pes, outraLot,svcFsPub,CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO_OUTRA_LOTACAO);
				boolean podeFSRaizOutraLot = Cp.getInstance().getConf().podePorConfiguracao(pes, outraLot,svcFsRaiz,CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO_OUTRA_LOTACAO);
				
				if (podeFSSecOutraLot){
					String lot = conf.getPfxGrpSegAutoSec()
					+ outraLot.getSigla().toLowerCase()
					+ conf.getSfxGrpSegAutoSec();
					AdGrupoDeSeguranca grp = ((AdGrupoDeSeguranca) recuperarPorNome(
							lot, al));
					if (grp != null) {
						grp.acrescentarMembro(usu);
					}
				}
				if (podeFSGabOutraLot){
					String lot = conf.getPfxGrpSegAutoGab()
					+ outraLot.getSigla().toLowerCase()
					+ conf.getSfxGrpSegAutoGab();
					AdGrupoDeSeguranca grp = ((AdGrupoDeSeguranca) recuperarPorNome(
							lot, al));
					if (grp != null) {
						grp.acrescentarMembro(usu);
					}
				}
				if (podeFSJuizOutraLot){
					String lot = conf.getPfxGrpSegAutoJuiz()
					+ outraLot.getSigla().toLowerCase()
					+ conf.getSfxGrpSegAutoJuiz();
					AdGrupoDeSeguranca grp = ((AdGrupoDeSeguranca) recuperarPorNome(
							lot, al));
					if (grp != null) {
						grp.acrescentarMembro(usu);
					}
					
				}
				if (podeFSPubOutraLot){
					String lot = conf.getPfxGrpSegAutoPub()
					+ outraLot.getSigla().toLowerCase()
					+ conf.getSfxGrpSegAutoPub();
					AdGrupoDeSeguranca grp = ((AdGrupoDeSeguranca) recuperarPorNome(
							lot, al));
					if (grp != null) {
						grp.acrescentarMembro(usu);
					}
				}

				if (podeFSRaizOutraLot){
					String lot = conf.getPfxGrpSegAutoRaiz()
					+ outraLot.getSigla().toLowerCase()
					+ conf.getSfxGrpSegAutoRaiz();
					AdGrupoDeSeguranca grp = ((AdGrupoDeSeguranca) recuperarPorNome(
							lot, al));
					if (grp != null) {
						grp.acrescentarMembro(usu);
					}
					
				}


			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private AdGrupoDeSeguranca processarGrupoSeguranca(DpLotacao lotacao,
			Map<String, AdObjeto> al, AdUnidadeOrganizacional uo) {

		String lot = conf.getPfxGrpSegAuto() + lotacao.getSigla().toLowerCase()
				+ conf.getSfxGrpSegAuto();

		AdGrupoDeSeguranca gsLot = (AdGrupoDeSeguranca) recuperarPorNome(lot,
				al);
		if (gsLot == null) {
			AdGrupoDeSeguranca gs = new AdGrupoDeSeguranca(lot, lot, uo, conf
					.getDnDominio());

			al.put(gs.getNome(), gs);

			if (lotacao.getCpTipoLotacao().getIdTpLotacao().equals(100L)) {
				String lotGab = conf.getPfxGrpSegAutoGab()
						+ lotacao.getSigla().toLowerCase()
						+ conf.getSfxGrpSegAutoGab();
				String lotSec = conf.getPfxGrpSegAutoSec()
						+ lotacao.getSigla().toLowerCase()
						+ conf.getSfxGrpSegAutoSec();
				String lotJuiz = conf.getPfxGrpSegAutoJuiz()
						+ lotacao.getSigla().toLowerCase()
						+ conf.getSfxGrpSegAutoJuiz();
				String lotPub = conf.getPfxGrpSegAutoPub()
						+ lotacao.getSigla().toLowerCase()
						+ conf.getSfxGrpSegAutoPub();

				AdGrupoDeSeguranca gsGab = new AdGrupoDeSeguranca(lotGab,
						lotGab, uo, conf.getDnDominio());
				AdGrupoDeSeguranca gsSec = new AdGrupoDeSeguranca(lotSec,
						lotSec, uo, conf.getDnDominio());
				AdGrupoDeSeguranca gsJuiz = new AdGrupoDeSeguranca(lotJuiz,
						lotJuiz, uo, conf.getDnDominio());
				AdGrupoDeSeguranca gsPub = new AdGrupoDeSeguranca(lotPub,
						lotPub, uo, conf.getDnDominio());

				al.put(gsGab.getNome(), gsGab);
				al.put(gsSec.getNome(), gsSec);
				al.put(gsJuiz.getNome(), gsJuiz);
				al.put(gsPub.getNome(), gsPub);
			}

			gsLot = gs;
			while (lotacao.getLotacaoPai() != null) {
				String lotPai = conf.getPfxGrpSegAuto()
						+ lotacao.getLotacaoPai().getSigla().toLowerCase()
						+ conf.getSfxGrpSegAuto();
				AdGrupoDeSeguranca gdLotPai = (AdGrupoDeSeguranca) recuperarPorNome(
						lotPai, al);
				if (gdLotPai == null) {
					// AdGrupoDeSeguranca gdPai = new AdGrupoDeSeguranca(
					// lotPai, "GrupoDeSeguranca:" + lotPai, uo);
					AdGrupoDeSeguranca gdPai = new AdGrupoDeSeguranca(lotPai,
							lotPai, uo, conf.getDnDominio());

					al.put(gdPai.getNome(), gdPai);
					gdLotPai = gdPai;
				}
				gdLotPai.acrescentarMembro(gs);
				gs = gdLotPai;
				lotacao = lotacao.getLotacaoPai();
			}

		}

		return gsLot;
	}

	private AdGrupoDeDistribuicao processarGrupoDistribuicao(DpLotacao lotacao,
			Map<String, AdObjeto> al, AdUnidadeOrganizacional uo) {
		// return processarGrupo(AdGrupoDeDistribuicao.class,lotacao, al, uo,
		// "GrupoDeEmail:", "ts", "");
		String lot = conf.getPfxGrpDistrAuto()
				+ lotacao.getSigla().toLowerCase() + conf.getSfxGrpDistrAuto();

		AdGrupoDeDistribuicao gdLot = (AdGrupoDeDistribuicao) recuperarPorNome(
				lot, al);
		if (gdLot == null) {
			// AdGrupoDeDistribuicao gdd = new AdGrupoDeDistribuicao(lot,
			// "GrupoDeEmail:" + lot, uo);
			AdGrupoDeDistribuicao gdd = new AdGrupoDeDistribuicao(lot, lot, uo,
					conf.getDnDominio());

			al.put(gdd.getNome(), gdd);
			gdLot = gdd;
			while (lotacao.getLotacaoPai() != null) {
				String lotPai = conf.getPfxGrpDistrAuto()
						+ lotacao.getLotacaoPai().getSigla().toLowerCase()
						+ conf.getSfxGrpDistrAuto();
				AdGrupoDeDistribuicao gdLotPai = (AdGrupoDeDistribuicao) recuperarPorNome(
						lotPai, al);
				if (gdLotPai == null) {
					// AdGrupoDeDistribuicao gdPai = new AdGrupoDeDistribuicao(
					// lotPai, "GrupoDeEmail:" + lotPai, uo);
					AdGrupoDeDistribuicao gdPai = new AdGrupoDeDistribuicao(
							lotPai, lotPai, uo, conf.getDnDominio());

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

	private List<DpPessoa> consultarPessoas() {
		CpDao dao = CpDao.getInstance();
		DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
		flt.setNome("");
		flt.setSigla("");
		flt.setLotacao(null);
		flt.setSituacaoFuncionalPessoa(conf.getIdSituacaoFuncionalAtivo()
				.toString());
		flt.setBuscarFechadas(false);
		flt.setIdOrgaoUsu(Long.valueOf(conf.getIdLocalidade()));

		List<DpPessoa> list = dao.consultarPorFiltro(flt);
		return list;
	}

	private AdObjeto recuperarPorNome(String nomeObj, Map<String, AdObjeto> al) {
		AdObjeto adObj = null;
		for (AdObjeto o : al.values()) {
			if (o.getNome().equalsIgnoreCase(nomeObj)) {
				adObj = o;
				break;
			}
		}
		return adObj;
	}

}
