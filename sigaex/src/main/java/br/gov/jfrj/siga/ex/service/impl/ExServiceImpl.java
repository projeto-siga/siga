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
package br.gov.jfrj.siga.ex.service.impl;

import java.lang.reflect.InvocationTargetException;

import javax.ws.rs.Path;

import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpResponsavel;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExCompetenciaBL;
import br.gov.jfrj.siga.ex.service.ExService;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.parser.PessoaLotacaoParser;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.webwork.action.ExMobilSelecao;

//@WebService(endpointInterface = "br.gov.jfrj.siga.ex.service.ExService")
@Path("/servicos")
public class ExServiceImpl implements ExService {

	private boolean hideStackTrace = false;

	public boolean isHideStackTrace() {
		return hideStackTrace;
	}

	public void setHideStackTrace(boolean hideStackTrace) {
		this.hideStackTrace = hideStackTrace;
	}

	private ExDao dao() {
		return ExDao.getInstance();
	}

	public Boolean transferir(String codigoDocumentoVia, String siglaDestino,
			String siglaCadastrante) throws Exception {
		if (codigoDocumentoVia == null)
			return false;
		try {
			ExMobil mob = buscarMobil(codigoDocumentoVia);
			if (mob.isGeral() && mob.doc().isProcesso())
				mob = mob.doc().getUltimoVolume();
			PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(
					siglaCadastrante);
			PessoaLotacaoParser destinoParser = new PessoaLotacaoParser(
					siglaDestino);
			if (destinoParser.getLotacao() == null
					&& destinoParser.getPessoa() == null)
				return false;
			if (destinoParser.getLotacao() == null)
				destinoParser
						.setLotacao(destinoParser.getPessoa().getLotacao());
			if (mob.getUltimaMovimentacaoNaoCancelada() != null
					&& ((destinoParser.getLotacao() == null || !destinoParser
							.getLotacao().equivale(
									mob.getUltimaMovimentacaoNaoCancelada()
											.getLotaResp())) || (destinoParser
							.getPessoa() == null || !destinoParser.getPessoa()
							.equivale(
									mob.getUltimaMovimentacaoNaoCancelada()
											.getResp())))) {
				Ex.getInstance()
						.getBL()
						.transferir(null, null, cadastranteParser.getPessoa(),
								cadastranteParser.getLotacao(), mob, null,
								null, destinoParser.getLotacao(),
								destinoParser.getPessoa(), null, null, null,
								null, null, false, null, null, null);
			}
			return true;
		} catch (Exception e) {
			if (!isHideStackTrace())
				e.printStackTrace(System.out);
			return false;
		}
	}

	private ExMobil buscarMobil(String codigoDocumentoVia) throws Exception,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		try {
			ExMobil mob = null;
			{
				final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
				filter.setSigla(codigoDocumentoVia);
				mob = (ExMobil) dao().consultarPorSigla(filter);
			}
			return mob;
		} catch (Exception e) {
			if (!isHideStackTrace())
				e.printStackTrace(System.out);
			throw e;
		}
	}

	public Boolean arquivarCorrente(String codigoDocumentoVia,
			String siglaDestino, String siglaCadastrante) throws Exception {
		try {
			ExMobil mob = buscarMobil(codigoDocumentoVia);

			PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(
					siglaCadastrante);
			PessoaLotacaoParser destinoParser = new PessoaLotacaoParser(
					siglaDestino);
			Ex.getInstance()
					.getBL()
					.arquivarCorrente(cadastranteParser.getPessoa(),
							cadastranteParser.getLotacao(), mob, null, null,
							destinoParser.getPessoa());
			return true;
		} catch (Exception e) {
			if (!isHideStackTrace())
				e.printStackTrace(System.out);
			throw e;
		}
	}

	public Boolean juntar(String codigoDocumentoViaFilho,
			String codigoDocumentoViaPai, String siglaDestino,
			String siglaCadastrante) throws Exception {
		try {
			ExMobil mobFilho = buscarMobil(codigoDocumentoViaFilho);
			ExMobil mobPai = buscarMobil(codigoDocumentoViaPai);

			PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(
					siglaCadastrante);
			PessoaLotacaoParser destinoParser = new PessoaLotacaoParser(
					siglaDestino);

			Ex.getInstance()
					.getBL()
					.juntarDocumento(cadastranteParser.getPessoa(),
							cadastranteParser.getPessoa(),
							cadastranteParser.getLotacao(), null, mobFilho,
							mobPai, null, destinoParser.getPessoa(),
							destinoParser.getPessoa(), "1");
			return true;
		} catch (Exception e) {
			if (!isHideStackTrace())
				e.printStackTrace(System.out);
			throw e;
		}
	}

	public Boolean isAssinado(String codigoDocumento, String siglaCadastrante)
			throws Exception {
		try {
			ExMobil mob = buscarMobil(codigoDocumento);
			return mob.getExDocumento().isAssinado();
		} catch (Exception e) {
			if (!isHideStackTrace())
				e.printStackTrace(System.out);
			throw e;
		}
	}

	public Boolean isSemEfeito(String codigoDocumento) throws Exception {
		try {
			ExMobil mob = buscarMobil(codigoDocumento);
			return mob.getExDocumento().isSemEfeito();
		} catch (Exception e) {
			if (!isHideStackTrace())
				e.printStackTrace(System.out);
			throw e;
		}
	}

	public Boolean podeMovimentar(String codigoDocumento,
			String siglaCadastrante) throws Exception {
		try {
			PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(
					siglaCadastrante);
			ExMobil mob = buscarMobil(codigoDocumento);
			return Ex
					.getInstance()
					.getComp()
					.podeMovimentar(
							cadastranteParser.getPessoa(),
							cadastranteParser.getLotacao() == null ? cadastranteParser
									.getPessoa().getLotacao()
									: cadastranteParser.getLotacao(), mob);
		} catch (Exception e) {
			if (!isHideStackTrace())
				e.printStackTrace(System.out);
			throw e;
		}
	}

	public Boolean podeTransferir(String codigoDocumento,
			String siglaCadastrante) throws Exception {
		try {
			PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(
					siglaCadastrante);
			ExMobil mob = buscarMobil(codigoDocumento);
			if (mob.isGeral() && mob.doc().isProcesso())
				mob = mob.doc().getUltimoVolume();
			return Ex
					.getInstance()
					.getComp()
					.podeTransferir(cadastranteParser.getPessoa(),
							cadastranteParser.getLotacao(), mob);
		} catch (Exception e) {
			if (!isHideStackTrace())
				e.printStackTrace(System.out);
			throw e;
		}
	}

	public Boolean isAtendente(String codigoDocumento, String siglaTitular)
			throws Exception {
		try {
			PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(
					siglaTitular);
			ExMobil mob = buscarMobil(codigoDocumento);
			return ExCompetenciaBL.isAtendente(cadastranteParser.getPessoa(),
					cadastranteParser.getLotacao(), mob);
		} catch (Exception e) {
			if (!isHideStackTrace())
				e.printStackTrace(System.out);
			throw e;
		}
	}

	public String getAtendente(String codigoDocumento, String siglaTitular)
			throws Exception {
		try {
			PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(
					siglaTitular);
			ExMobil mob = buscarMobil(codigoDocumento);
			DpResponsavel resp = ExCompetenciaBL.getAtendente(mob);
			if (resp == null)
				return null;

			if (resp instanceof DpPessoa) {
				return resp.getSiglaCompleta() + "@"
						+ ((DpPessoa) resp).getLotacao().getSiglaCompleta();
			} else {
				return "@" + resp.getSiglaCompleta();
			}
		} catch (Exception e) {
			if (!isHideStackTrace())
				e.printStackTrace(System.out);
			throw e;
		}
	}

	public byte[] obterPdfPorNumeroAssinatura(String num) throws Exception {
		return Ex.getInstance().getBL().obterPdfPorNumeroAssinatura(num);
	}

	public String buscarPorCodigo(String codigo) throws Exception {
		ExMobilSelecao sel = new ExMobilSelecao();
		sel.setSigla(codigo);
		sel.buscarPorSigla();
		String s = sel.getSigla();
		if (s != null && s.length() > 0)
			return s;
		return null;
	}

	public String criarVia(String codigoDocumento, String siglaCadastrante)
			throws Exception {
		if (codigoDocumento == null)
			return null;
		try {
			ExMobil mob = buscarMobil(codigoDocumento);
			PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(
					siglaCadastrante);
			if (cadastranteParser.getLotacao() == null
					&& cadastranteParser.getPessoa() == null)
				return null;
			Ex.getInstance()
					.getBL()
					.criarVia(
							cadastranteParser.getPessoa(),
							cadastranteParser
									.getLotacaoOuLotacaoPrincipalDaPessoa(),
							mob.doc());
			return mob.doc().getUltimaVia().getSigla();
		} catch (Exception e) {
			if (!isHideStackTrace())
				e.printStackTrace(System.out);
			return null;
		}
	}

	public String form(String codigoDocumento, String variavel)
			throws Exception {
		if (codigoDocumento == null)
			return null;
		try {
			ExMobil mob = buscarMobil(codigoDocumento);
			return mob.doc().getForm().get(variavel);
		} catch (Exception e) {
			if (!isHideStackTrace())
				e.printStackTrace(System.out);
			return null;
		}
	}

	public String toJSON(String codigo) throws Exception {
		try {
			ExMobil mob = null;
			{
				final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
				filter.setSigla(codigo);
				mob = (ExMobil) dao().consultarPorSigla(filter);
				//return Ex.getInstance().getBL().toJSON(mob);
				return "";
			}
		} catch (Exception e) {
			if (!isHideStackTrace())
				e.printStackTrace(System.out);
			throw e;
		}
	}

}
