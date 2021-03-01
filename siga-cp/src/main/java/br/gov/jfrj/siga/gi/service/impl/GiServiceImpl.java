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
package br.gov.jfrj.siga.gi.service.impl;

import java.util.List;
import java.util.regex.Pattern;

import javax.jws.WebService;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import br.gov.jfrj.siga.acesso.ConfiguracaoAcesso;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.GeraMessageDigest;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.util.SigaUtil;
import br.gov.jfrj.siga.cp.util.TokenException;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpLotacaoDaoFiltro;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import br.gov.jfrj.siga.gi.integracao.IntegracaoLdapViaWebService;
import br.gov.jfrj.siga.gi.service.GiService;

/**
 * Esta classe implementa os métodos de gestão de identidade O acesso à esta
 * classe é realizado via web-services, com interfaces definidas no módulo
 * siga-ws, conforme o padrão adotados para o SIGA.
 * 
 * @author tah
 * 
 */
@WebService(serviceName = "GiService", endpointInterface = "br.gov.jfrj.siga.gi.service.GiService", targetNamespace = "http://impl.service.gi.siga.jfrj.gov.br/")
public class GiServiceImpl implements GiService {
	
    private boolean autenticaViaBanco(CpIdentidade identidade, String senha) {
    	// caso o campo senha esteja vazio ou nulo, retorna false. 
    	// Não autentica usuários com senha em branco.
    	if(identidade.getDscSenhaIdentidade() == null || identidade.getDscSenhaIdentidade().equals("")) 
    		return false;
    	
    	try {
    		final String hashAtual = GeraMessageDigest.executaHash(senha.getBytes(), "MD5");
    		if (identidade != null && identidade.getDscSenhaIdentidade().equals(hashAtual)) return true;
		} catch (Exception e) {
			return false;
		}
    	return false;
    }
    
    private boolean autenticaViaLdap(String matricula, String senha) {
    	try {
			return IntegracaoLdapViaWebService.getInstancia().autenticarUsuario(matricula, senha);
		} catch (Exception e) {
			return false;
		}
    }
    
    public String buscarModoAutenticacao(String login) {
    	CpIdentidade id = null;
		CpDao dao = CpDao.getInstance();
		id = dao.consultaIdentidadeCadastrante(login, true);
		return buscarModoAutenticacao(id);
    }

    private String buscarModoAutenticacao(CpIdentidade id) {
    	String orgao = id.getCpOrgaoUsuario().getSiglaOrgaoUsu();
    	String retorno = _MODO_AUTENTICACAO_DEFAULT;
		String modo = Cp.getInstance().getBL().buscarModoAutenticacao(orgao);
		if(modo != null) 
			retorno = modo;
    	return retorno;
    }
    
    @Override
    public String login(String matricula, String senha) {
		String resultado = "";

		CpIdentidade id = null;
		CpDao dao = CpDao.getInstance();
		id = dao.consultaIdentidadeCadastrante(matricula, true);
		String modoAut = buscarModoAutenticacao(id);

		try {
			if(modoAut.equals(_MODO_AUTENTICACAO_BANCO)) {
				if (autenticaViaBanco(id, senha)) {
					resultado = parseLoginResult(id);
				}
			} else if(modoAut.equals(_MODO_AUTENTICACAO_LDAP)) {
				if(autenticaViaLdap(matricula, senha)) {
					resultado = parseLoginResult(id);
				}
			}

		} catch (AplicacaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultado;
	}

	@Override
	public String dadosUsuario(String matricula) {
		String resultado = "";
		try {
			CpDao dao = CpDao.getInstance();

			CpIdentidade id = null;
			id = dao.consultaIdentidadeCadastrante(matricula, true);
			if (id != null) {
				resultado = parseLoginResult(id);
			}
		} catch (AplicacaoException e) {
			e.printStackTrace();
		}
		return resultado;
	}

	@Override
	public String perfilAcessoPorCpf(String cpf, String token) {
		
		String resultado = "";
		try {
			//if(Prop.getBool("/siga.ws.seguranca.token.jwt"))
				//SigaUtil.getInstance().validarToken(token);
				
			if (Pattern.matches("\\d+", cpf) && cpf.length() == 11) {
				List<CpIdentidade> lista = new CpDao().consultaIdentidadesCadastrante(cpf, Boolean.TRUE);
				if (!lista.isEmpty()) {
					resultado = parseAcessosResult(lista);
				} else {
					resultado = "Não foi possível buscar acessos. Acessos não localizados.";
				}
			} else {
				resultado = "Não foi possível buscar acessos. CPF inválido.";
			}

		} catch (AplicacaoException e) {
			e.printStackTrace();
		} 
		return resultado;
	}

	private String parseAcessosResult(List<CpIdentidade> lista) {
		JSONArray acessos = new JSONArray();

		try {
			if (!lista.isEmpty()) {
				for (CpIdentidade identidade : lista) {
					JSONObject pessoa = new JSONObject();
					JSONObject orgao = new JSONObject();
					JSONObject lotacao = new JSONObject();
					JSONObject cargo = new JSONObject();
					JSONObject funcao = new JSONObject();

					// Pessoa
					DpPessoa p = identidade.getPessoaAtual();
					pessoa.put("siglaPessoa", p.getSiglaCompleta());
					pessoa.put("nomePessoa", p.getNomePessoa());
					pessoa.put("isExternaPessoa", p.isUsuarioExterno());
					
					// Orgao Pessoa
					CpOrgaoUsuario o = p.getOrgaoUsuario();
					orgao.put("idOrgao", o.getId());
					orgao.put("nomeOrgao", o.getNmOrgaoAI());
					orgao.put("siglaOrgao", o.getSigla());

					// Lotacao Pessoa
					DpLotacao l = p.getLotacao();
					lotacao.put("idLotacao", l.getId());
					lotacao.put("nomeLotacao", l.getNomeLotacao());
					lotacao.put("siglaLotacao", l.getSigla());

					// Cargo Pessoa
					DpCargo c = p.getCargo();
					if (c != null) {
						cargo.put("idCargo", c.getId());
						cargo.put("nomeCargo", c.getNomeCargo());
					}
					// Função Pessoa
					DpFuncaoConfianca f = p.getFuncaoConfianca();
					if (f != null) {
						funcao.put("idFuncaoConfianca", f.getId());
						funcao.put("nomeFuncaoConfianca", f.getNomeFuncao());
					}

					pessoa.put("orgao", orgao);
					pessoa.put("lotacao", lotacao);
					pessoa.put("cargo", cargo);
					pessoa.put("funcaoConfianca", funcao);

					acessos.put(pessoa);

				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			return acessos.toString();
		} catch (Exception e) {
			return "";
		}

	}

	private String parseLoginResult(CpIdentidade id) {
		JSONObject pessoa = new JSONObject();
		JSONObject lotacao = new JSONObject();
		JSONObject cargo = new JSONObject();
		JSONObject funcao = new JSONObject();
		JSONObject identidade = new JSONObject();

		try {
			DpPessoa p = id.getPessoaAtual();
			pessoa.put("idPessoa", p.getId());
			pessoa.put("idExternaPessoa", p.getIdExterna());
			pessoa.put("isExternaPessoa", p.isUsuarioExterno());
			pessoa.put("matriculaPessoa", p.getMatricula());
			pessoa.put("cpf", p.getCpfPessoa());
			pessoa.put("siglaPessoa", p.getSiglaCompleta());
			pessoa.put("nomePessoa", p.getNomePessoa());
			pessoa.put("emailPessoa", p.getEmailPessoaAtual());
			pessoa.put("siglaPessoaWEmul", p.getSiglaPessoa());
			pessoa.put("tipoServidor", p.getCpTipoPessoa() != null ? p
					.getCpTipoPessoa().getIdTpPessoa() : "null");

			DpLotacao l = p.getLotacao();
			lotacao.put("idLotacao", l.getId());
			lotacao.put("idExternaLotacao", l.getIdExterna());
			lotacao.put("nomeLotacao", l.getNomeLotacao());
			lotacao.put("siglaLotacao", l.getSigla());
			lotacao.put("idLotacaoPai", l.getIdLotacaoPai());
			lotacao.put("idTpLotacao", l.getCpTipoLotacao() != null ? l
					.getCpTipoLotacao().getIdTpLotacao() : "null");
			lotacao.put("siglaTpLotacao", l.getCpTipoLotacao() != null ? l
					.getCpTipoLotacao().getSiglaTpLotacao() : "null");

			DpCargo c = p.getCargo();
			if (c != null) {
				cargo.put("idCargo", c.getId());
				cargo.put("idExternaCargo", c.getIdExterna());
				cargo.put("nomeCargo", c.getNomeCargo());
				cargo.put("siglaCargo", c.getSigla());
			}

			DpFuncaoConfianca f = p.getFuncaoConfianca();
			if (f != null) {
				funcao.put("idFuncaoConfianca", f.getId());
				funcao.put("idExternaFuncaoConfianca", f.getIdeFuncao());
				funcao.put("nomeFuncaoConfianca", f.getNomeFuncao());
				funcao.put("siglaFuncaoConfianca", f.getSigla());
				funcao.put("idPaiFuncaoConfianca", f.getIdFuncaoPai());
			}
			
			identidade.put("isSenhaUsuarioExpirada", id.isSenhaUsuarioExpirada());

			pessoa.put("lotacao", lotacao);
			pessoa.put("cargo", cargo);
			pessoa.put("funcaoConfianca", funcao);
			pessoa.put("identidade", identidade);
			

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			return pessoa.toString(2);
		} catch (JSONException e) {
			return "";
		}
	}

	@SuppressWarnings("unused")
	public String acesso(String matricula, String lotacao, String servico) {
		JSONObject servicos = new JSONObject();
		String resultado = "";
		try {
			CpDao dao = CpDao.getInstance();

			DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
			flt.setSigla(matricula);
			DpPessoa p = (DpPessoa) dao.consultarPorSigla(flt);

			DpLotacao lot = null;
			if (lotacao != null) {
				DpLotacaoDaoFiltro fltLot = new DpLotacaoDaoFiltro();
				fltLot.setSiglaCompleta(lotacao);
				lot = (DpLotacao) dao.consultarPorSigla(fltLot);
			}

			boolean pode = Cp.getInstance().getConf()
					.podeUtilizarServicoPorConfiguracao(p, lot, servico);

			CpServico srv = dao.consultarCpServicoPorChave(servico);

			if (p != null) {
				ConfiguracaoAcesso ac;
				ac = ConfiguracaoAcesso.gerar(null, p, lot, null, srv, null);
				if (ac != null)
					servicos.put(ac.getServico().getSigla(), ac.getSituacao()
							.getDscSitConfiguracao());
			}
			resultado = servicos.toString(2);
		} catch (AplicacaoException e) {
			return "";
		} catch (JSONException e) {
			return "";
		} catch (Exception e) {
			return "";
		}
		return resultado;
	}

	public String acessos(String matricula, String lotacao) {
		JSONObject servicos = new JSONObject();
		String resultado = "";
		try {
			CpDao dao = CpDao.getInstance();

			DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
			flt.setSigla(matricula);
			DpPessoa p = (DpPessoa) dao.consultarPorSigla(flt);

			DpLotacao lot = null;
			if (lotacao != null) {
				DpLotacaoDaoFiltro fltLot = new DpLotacaoDaoFiltro();
				fltLot.setSiglaCompleta(lotacao);
				lot = (DpLotacao) dao.consultarPorSigla(fltLot);
			}

			if (p != null) {
				List<CpServico> l = dao.listarServicos();
				for (CpServico srv : l) {
					ConfiguracaoAcesso ac;
					try {
						ac = ConfiguracaoAcesso.gerar(null, p, lot, null, srv,
								null);
						if (ac != null)
							servicos.put(ac.getServico().getSigla(), ac
									.getSituacao().getDscSitConfiguracao());
					} catch (Exception e) {
					}
				}
			}
			resultado = servicos.toString(2);
		} catch (AplicacaoException e) {
			return "";
		} catch (JSONException e) {
			return "";
		}
		return resultado;
	}

	@Override
	public String esqueciSenha(String cpf, String email) {
		String resultado = "";
		try {
			resultado = Cp.getInstance().getBL().alterarSenha(cpf, email, null);
		} catch (Exception e) {
			return "";
		}
		return resultado;
	}

	@Override
	public String criarUsuario(String cadastranteStr, String orgaoUsu,
			String lotacao, String cargo, String funcao, String nmPessoa,
			String dtNascimento, String cpf, String email) {

		String resultado = "";
		try {

			if (orgaoUsu == null || "".equals(orgaoUsu.trim()))
				throw new AplicacaoException("Órgão não informado");

			if (cargo == null || "".equals(cargo.trim()))
				throw new AplicacaoException("Cargo não informado");

			if (lotacao == null || "".equals(lotacao.trim()))
				throw new AplicacaoException("Unidade não informada");

			if (nmPessoa == null || "".equals(nmPessoa.trim()))
				throw new AplicacaoException("Nome não informado");

			if (cpf == null || "".equals(cpf.trim()))
				throw new AplicacaoException("CPF não informado");

			if (email == null || "".equals(email.trim()))
				throw new AplicacaoException("E-mail não informado");

			if (nmPessoa != null
					&& !nmPessoa.matches("[a-zA-ZáâãéêíóôõúçÁÂÃÉÊÍÓÔÕÚÇ'' ]+"))
				throw new AplicacaoException(
						"Nome com caracteres não permitidos");

			Long idOrgaoUsu = null;
			Long idCargoUsu = null;
			Long idLotacaoUsu = null;
			Long idFuncaoUsu = null;

			// Obtem identidade do cadastrante
			CpIdentidade identidadeCadastrante = CpDao.getInstance()
					.consultaIdentidadeCadastrante(cadastranteStr, true);
			// Obtém Id Órgão
			CpOrgaoUsuario orgaoUsuario = new CpOrgaoUsuario();
			orgaoUsuario.setNmOrgaoUsu(Texto.removeAcento(orgaoUsu));
			orgaoUsuario = CpDao.getInstance().consultarPorNome(orgaoUsuario);
			if (orgaoUsuario == null) {
				throw new AplicacaoException("Órgão não localizado");
			} else {
				idOrgaoUsu = orgaoUsuario.getIdOrgaoUsu();
			}

			// Obtém Cargo
			DpCargo cargoUsuario = new DpCargo();
			cargoUsuario.setNomeCargo(Texto.removeAcento(cargo));
			cargoUsuario.setOrgaoUsuario(orgaoUsuario);
			cargoUsuario = CpDao.getInstance().consultarPorNomeOrgao(
					cargoUsuario);
			if (cargoUsuario == null) {
				throw new AplicacaoException("Cargo não localizado");
			} else {
				idCargoUsu = cargoUsuario.getId();
			}

			// Obtém Unidade
			DpLotacao lotacaoUsuario = new DpLotacao();
			lotacaoUsuario.setNomeLotacao(Texto.removeAcento(lotacao));
			lotacaoUsuario.setOrgaoUsuario(orgaoUsuario);
			lotacaoUsuario = CpDao.getInstance().consultarPorNomeOrgao(
					lotacaoUsuario);
			if (lotacaoUsuario == null) {
				throw new AplicacaoException("Unidade não localizada");
			} else {
				idLotacaoUsu = lotacaoUsuario.getId();
			}

			// Obtém Função
			if (funcao != null && !"".equals(funcao.trim())) {
				DpFuncaoConfianca funcaoConfianca = new DpFuncaoConfianca();
				funcaoConfianca.setNomeFuncao(Texto.removeAcento(funcao));
				funcaoConfianca.setOrgaoUsuario(orgaoUsuario);
				funcaoConfianca = CpDao.getInstance().consultarPorNomeOrgao(
						funcaoConfianca);
				if (funcaoConfianca == null) {
					throw new AplicacaoException("Função não localizada");
				} else {
					idFuncaoUsu = funcaoConfianca.getId();
				}
			}

			DpPessoa pes = Cp
					.getInstance()
					.getBL()
					.criarUsuario(null, identidadeCadastrante, idOrgaoUsu, idCargoUsu,
							idFuncaoUsu, idLotacaoUsu, nmPessoa, dtNascimento,
							cpf, email, null, null, null, null, null, "true");

			resultado = "Usuário cadastrado com sucesso: " + pes.getSesbPessoa() + pes.getMatricula();
		} catch (Exception e) {
			return e.getMessage();
		}
		return resultado;
	}

	@Override
	public String inativarUsuario(final Long idUsuario) {
		return Cp
				.getInstance()
				.getBL()
				.inativarUsuario(idUsuario);
	}

	/**
	 * 1 - Autenticar: logar no Sistema
	 * 2 - Autorizar: verificar na tabela CP_Configuração se o usuário tem autorização
	 * 3 - Gerar token
	 */
	@Override
	public  String gerarToken(String matricula, String senha) throws Exception{
		String token = "";

		CpIdentidade id = null;
		CpDao dao = CpDao.getInstance();
		id = dao.consultaIdentidadeCadastrante(matricula, true);
		String modoAut = buscarModoAutenticacao(id);

		/* Autenticação */
		if(modoAut.equals(_MODO_AUTENTICACAO_BANCO)) {
			if (!autenticaViaBanco(id, senha)) {
				throw new AplicacaoException("Usuário ou Senha inválidos.");
			}
		} else if(modoAut.equals(_MODO_AUTENTICACAO_LDAP)) {
			if(!autenticaViaLdap(matricula, senha)) {
				throw new AplicacaoException("Usuário ou Senha inválidos.");
			}
		}
		
		/* Autorização */
		Boolean permissaoWS =  SigaUtil.getInstance().verificaSePessoTemPermissaoWS(id.getDpPessoa());
		if(!permissaoWS)
			throw new TokenException("Usuário sem permissão de acesso ao Web Service.");
		
		/* Gera Token JWT para consumo dos WS SOAP*/
		token = SigaUtil.getInstance().gerarToken(matricula);
		if("".equals(token))			
			throw new TokenException("Erro ao gerar TOKEN.");
						
		return token;
	}
	
}