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
package br.gov.jfrj.siga.acesso;

import java.security.cert.CertificateParsingException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpTipoIdentidade;
import br.gov.jfrj.siga.dp.CpPersonalizacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpSubstituicao;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class UsuarioAutenticado {
	@SuppressWarnings("static-access")
	public static void carregarUsuarioAutenticadoClientCert(String principal,ConheceUsuario ioc) throws Exception {

		List<CpIdentidade> ids = dao().consultaIdentidadesCadastrante(principal, true);
		CpIdentidade idCertEncontrada = null;
		for (CpIdentidade idCert : ids) {
			if (idCert.getCpTipoIdentidade().isTipoCertificado()) {
				idCertEncontrada = idCert;
				break;
			}
		}

		if (idCertEncontrada == null) {
			CpIdentidade idCertNova;
			try {
				// cria uma nova identidade para o caso de não existir para o
				// certificado.
				DpPessoa pessoa = CpDao.getInstance().getPessoaPorPrincipal(
						principal);
				if (pessoa == null)
					throw new AplicacaoException("Pessoa não identificada para a matrícula '"+ principal + "'.");
				CpTipoIdentidade tpId = CpDao.getInstance().consultar(
						CpTipoIdentidade.CERTIFICADO, CpTipoIdentidade.class, false);
				if (tpId == null)
					throw new AplicacaoException("Tipo de identidade não encontrado para o id '"+ CpTipoIdentidade.CERTIFICADO + "'.");
				idCertNova = new CpIdentidade();
				idCertNova.setDpPessoa(pessoa);
				idCertNova.setCpTipoIdentidade(tpId);
				idCertNova.setCpOrgaoUsuario(pessoa.getOrgaoUsuario());
				idCertNova.setNmLoginIdentidade(principal);
				idCertNova.updateAtivo();
				idCertNova.setIdIdentidade(null);
				Date dt = dao().consultarDataEHoraDoServidor();
				idCertNova.setDtCriacaoIdentidade(dt);
				idCertNova.setDscSenhaIdentidade(null);
				idCertNova.setDscSenhaIdentidadeCripto(null);
				idCertNova.setDscSenhaIdentidadeCriptoSinc(null);
				// TODO: verificar o porquï¿½ da nï¿½o gravaï¿½ï¿½o da identidade
				dao().iniciarTransacao();
				// dao().gravar(idCertNova);
				dao().gravarComHistorico(idCertNova, null, dt, null);
				dao().commitTransacao();
			} catch (Exception e) {
				throw new AplicacaoException(
						"Não foi possível criar uma identidade para o certificado.");
			}
			carregarUsuario(idCertNova, ioc);
		} else {
			carregarUsuario(idCertEncontrada, ioc);
		}
	}
	

	@SuppressWarnings("static-access")
	public static void carregarUsuario(CpIdentidade id, ConheceUsuario ioc)
			throws AplicacaoException, SQLException {
		Date dt = dao().consultarDataEHoraDoServidor();
		if (!id.ativaNaData(dt)) {
			CpIdentidade idAtual = dao().obterIdentidadeAtual(id);
			if (!id.getId().equals(idAtual.getId())) { 
				dao().invalidarCache(id);
				id = idAtual;
			}
			if (!id.ativaNaData(dt)) 
				throw new AplicacaoException("O acesso não será permitido porque identidade está inativa desde '"+ id.getDtExpiracaoDDMMYYYY() + "'.");
		}
		if (id.isBloqueada()) {
			throw new AplicacaoException("O acesso não será permitido porque esta identidade está bloqueada.");
		}

		ioc.setIdentidadeCadastrante(id);
		ioc.setCadastrante(id.getPessoaAtual());

		CpPersonalizacao per = dao().consultarPersonalizacao(ioc.getCadastrante());

		if ((per != null)
				&& ((per.getPesSubstituindo() != null) || (per
						.getLotaSubstituindo() != null))) {

			DpSubstituicao dpSubstituicao = new DpSubstituicao();
			dpSubstituicao.setSubstituto(ioc.getCadastrante());
			dpSubstituicao.setLotaSubstituto(ioc.getCadastrante().getLotacao());

			ioc.setTitular(per.getPesSubstituindo());
			ioc.setLotaTitular(per.getLotaSubstituindo());

		}

		if (ioc.getLotaTitular() == null && ioc.getTitular() != null)
			ioc.setLotaTitular(ioc.getTitular().getLotacao());
		if (ioc.getTitular() == null)
			ioc.setTitular(ioc.getCadastrante());
		if (ioc.getLotaTitular() == null)
			ioc.setLotaTitular(ioc.getTitular().getLotacao());
	}

	/**
	 * @param principal
	 * @throws SQLException
	 */
	public static void carregarUsuarioAutenticado(String principal,	ConheceUsuario ioc) throws Exception {

		CpIdentidade id = dao().consultaIdentidadeCadastrante(principal, true);
		carregarUsuario(id, ioc);
		/*
		 * if (id.isBloqueada()) { throw new AplicacaoException(
		 * "O acesso nï¿½o serï¿½ permitido porque esta identidade estï¿½ bloqueada."
		 * ); }
		 * 
		 * ioc.setIdentidadeCadastrante(id);
		 * ioc.setCadastrante(id.getPessoaAtual());
		 * 
		 * CpPersonalizacao per = dao().consultarPersonalizacao(
		 * ioc.getCadastrante());
		 * 
		 * // // Verifica se o usuï¿½rio estï¿½ simulando alguï¿½m. // if (per != null
		 * && per.getUsuarioSimulando() != null) { // principal =
		 * per.getUsuarioSimulando().getNmUsuario(); // usu =
		 * dao().consultaUsuarioCadastranteAtivo(principal); //
		 * ioc.setCadastrante(usu.getPessoa()); // // per =
		 * dao().consultarPersonalizacao(ioc.getCadastrante()); // }
		 * 
		 * if ((per != null) && ((per.getPesSubstituindo() != null) || (per
		 * .getLotaSubstituindo() != null))) {
		 * 
		 * DpSubstituicao dpSubstituicao = new DpSubstituicao();
		 * dpSubstituicao.setSubstituto(ioc.getCadastrante());
		 * dpSubstituicao.setLotaSubstituto(ioc.getCadastrante().getLotacao());
		 * 
		 * List<DpSubstituicao> substituicoesPermitidas = dao()
		 * .consultarSubstituicoesPermitidas(dpSubstituicao);
		 * 
		 * for (final DpSubstituicao substituicao : substituicoesPermitidas) {
		 * if (per.getPesSubstituindo() != null) if
		 * (per.getPesSubstituindo().equivale( substituicao.getTitular())) {
		 * ioc.setTitular(per.getPesSubstituindo()); break; }
		 * 
		 * if (per.getLotaSubstituindo() != null) if
		 * (per.getLotaSubstituindo().equivale( substituicao.getLotaTitular()))
		 * { ioc.setLotaTitular(per.getLotaSubstituindo()); break; }
		 * 
		 * }
		 * 
		 * if (ioc.getTitular() == null && ioc.getLotaTitular() == null) {
		 * per.setPesSubstituindo(null); per.setLotaSubstituindo(null);
		 * dao().iniciarTransacao(); dao().gravar(per); dao().commitTransacao();
		 * } }
		 * 
		 * if (ioc.getLotaTitular() == null && ioc.getTitular() != null)
		 * ioc.setLotaTitular(ioc.getTitular().getLotacao()); if
		 * (ioc.getTitular() == null) ioc.setTitular(ioc.getCadastrante()); if
		 * (ioc.getLotaTitular() == null)
		 * ioc.setLotaTitular(ioc.getTitular().getLotacao());
		 */
	}

	/**
	 * Carrega usuário autenticado a partir do request
	 * 
	 * @param principal
	 * @param ioc
	 * @throws SQLException
	 *             ,NullPointerException
	 */
	public static void carregarUsuarioAutenticadoRequest(HttpServletRequest request, ConheceUsuario ioc) throws SQLException, NullPointerException,	CertificateParsingException, Exception {
		// login por formulario
		carregarUsuarioAutenticado(request.getUserPrincipal().getName(),ioc);
	}

	private static CpDao dao() {
		return CpDao.getInstance();
	}

}
