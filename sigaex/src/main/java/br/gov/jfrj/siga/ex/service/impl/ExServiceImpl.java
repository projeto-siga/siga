
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.xml.ws.WebServiceContext;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.cp.CpToken;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.util.SigaUtil;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpResponsavel;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExConfiguracao;
import br.gov.jfrj.siga.ex.ExConfiguracaoCache;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExDocumentoNumeracao;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.ExPreenchimento;
import br.gov.jfrj.siga.ex.ExSequencia;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoMobil;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExConfiguracaoBL;
import br.gov.jfrj.siga.ex.logic.ExPodeMovimentar;
import br.gov.jfrj.siga.ex.logic.ExPodePublicarPortalDaTransparencia;
import br.gov.jfrj.siga.ex.logic.ExPodeReiniciarNumeracao;
import br.gov.jfrj.siga.ex.logic.ExPodeSerTransferido;
import br.gov.jfrj.siga.ex.logic.ExPodeTransferir;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDePrincipal;
import br.gov.jfrj.siga.ex.service.ExService;
import br.gov.jfrj.siga.ex.util.NivelDeAcessoUtil;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.hibernate.ExStarter;
import br.gov.jfrj.siga.jee.SoapContext;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.parser.PessoaLotacaoParser;
import br.gov.jfrj.siga.parser.SiglaParser;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.vraptor.ExMobilSelecao;

@WebService(serviceName = "ExService", endpointInterface = "br.gov.jfrj.siga.ex.service.ExService", targetNamespace = "http://impl.service.ex.siga.jfrj.gov.br/")
public class ExServiceImpl implements ExService {
	private final static Logger log = Logger.getLogger(ExService.class);

	private class ExSoapContext extends SoapContext {
		EntityManager em;
		boolean transacional;
		long inicio = System.currentTimeMillis();

		public ExSoapContext(boolean transacional) {
			super(context, ExStarter.emf, transacional);
		}

		@Override
		public void initDao() {
			ExDao.getInstance();
			try {
				Ex.getInstance().getConf().limparCacheSeNecessario();
			} catch (Exception e1) {
				throw new RuntimeException("Não foi possível atualizar o cache de configurações", e1);
			}
		}
	}

	@Resource
	private WebServiceContext context;

	private ExDao dao() {
		return ExDao.getInstance();
	}

	public Boolean transferir(String codigoDocumentoVia, String siglaDestino, String siglaCadastrante,
			Boolean forcarTransferencia) throws Exception {
		// System.out.println("*** transferir: " + codigoDocumentoVia + " - " +
		// siglaDestino + " - " + siglaCadastrante + " - " + forcarTransferencia);
		try (ExSoapContext ctx = new ExSoapContext(true)) {
			try {
				if (codigoDocumentoVia == null)
					return false;
				ExMobil mob = buscarMobil(codigoDocumentoVia);
				if (mob.doc().isProcesso()) {
					mob = mob.doc().getUltimoVolume();
				} else if (contemApenasUmaVia(mob)) {
					mob = mob.doc().getPrimeiraVia();
				}
				PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(siglaCadastrante);
				PessoaLotacaoParser destinoParser = new PessoaLotacaoParser(siglaDestino);
				if (destinoParser.getLotacao() == null && destinoParser.getPessoa() == null)
					return false;
				if (destinoParser.getLotacao() == null)
					destinoParser.setLotacao(destinoParser.getPessoa().getLotacao());
				if (!mob.isAtendente(destinoParser.getPessoa(), destinoParser.getLotacao())) {
					Ex.getInstance().getBL().transferir(null, null, cadastranteParser.getPessoa(),
							cadastranteParser.getLotacao(), mob, null, null, null, destinoParser.getLotacao(),
							destinoParser.getPessoa(), null, null, null, null, null, null, false, null, null, null,
							forcarTransferencia, false, ExTipoDeMovimentacao.TRANSFERENCIA);
				}
				return true;
			} catch (Exception ex) {
				Exception e = ctx.exceptionWithMessageFileAndLine(ex);
				ctx.rollback(e);
				throw e;
			}
		}
	}

	private ExMobil buscarMobil(String codigoDocumentoVia)
			throws Exception, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		ExMobil mob = null;
		final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
		filter.setSigla(codigoDocumentoVia);
		mob = (ExMobil) dao().consultarPorSigla(filter);
		return mob;
	}

	public Boolean arquivarCorrente(String codigoDocumentoVia, String siglaDestino, String siglaCadastrante)
			throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(true)) {
			try {
				ExMobil mob = buscarMobil(codigoDocumentoVia);
				if (mob.doc().isProcesso()) {
					mob = mob.doc().getUltimoVolume();
				} else if (contemApenasUmaVia(mob)) {
					mob = mob.doc().getPrimeiraVia();
				}
				PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(siglaCadastrante);
				PessoaLotacaoParser destinoParser = new PessoaLotacaoParser(siglaDestino);
				Ex.getInstance().getBL().arquivarCorrente(cadastranteParser.getPessoa(), cadastranteParser.getLotacao(),
						mob, null, null, destinoParser.getPessoa(), false);
				return true;
			} catch (Exception ex) {
				Exception e = ctx.exceptionWithMessageFileAndLine(ex);
				ctx.rollback(e);
				throw e;
			}
		}
	}

	public Boolean juntar(String codigoDocumentoViaFilho, String codigoDocumentoViaPai, String siglaDestino,
			String siglaCadastrante) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(true)) {
			try {
				ExMobil mobFilho = buscarMobil(codigoDocumentoViaFilho);
				ExMobil mobPai = buscarMobil(codigoDocumentoViaPai);

				PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(siglaCadastrante);
				PessoaLotacaoParser destinoParser = new PessoaLotacaoParser(siglaDestino);

				Ex.getInstance().getBL().juntarDocumento(cadastranteParser.getPessoa(), cadastranteParser.getPessoa(),
						cadastranteParser.getLotacao(), null, mobFilho, mobPai, null, destinoParser.getPessoa(),
						destinoParser.getPessoa(), "1");
				return true;
			} catch (Exception ex) {
				Exception e = ctx.exceptionWithMessageFileAndLine(ex);
				ctx.rollback(e);
				throw e;
			}
		}
	}

	public Boolean isAssinado(String codigoDocumento, String siglaCadastrante) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(false)) {
			try {
				ExMobil mob = buscarMobil(codigoDocumento);
				return !mob.getExDocumento().isPendenteDeAssinatura();
			} catch (Exception ex) {
				Exception e = ctx.exceptionWithMessageFileAndLine(ex);
				ctx.rollback(e);
				throw e;
			}
		}
	}

	public Boolean isSemEfeito(String codigoDocumento) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(false)) {
			try {
				ExMobil mob = buscarMobil(codigoDocumento);
				if (mob == null)
					return null;
				return mob.getExDocumento().isSemEfeito();
			} catch (Exception ex) {
				Exception e = ctx.exceptionWithMessageFileAndLine(ex);
				ctx.rollback(e);
				throw e;
			}
		}
	}

	public Boolean podeMovimentar(String codigoDocumento, String siglaCadastrante) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(false)) {
			try {
				PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(siglaCadastrante);
				ExMobil mob = buscarMobil(codigoDocumento);
				return Ex.getInstance().getComp().pode(ExPodeMovimentar.class, cadastranteParser.getPessoa(),
						cadastranteParser.getLotacao() == null ? cadastranteParser.getPessoa().getLotacao()
								: cadastranteParser.getLotacao(),
						mob);
			} catch (Exception ex) {
				Exception e = ctx.exceptionWithMessageFileAndLine(ex);
				ctx.rollback(e);
				throw e;
			}
		}
	}

	public Boolean podeTransferir(String codigoDocumento, String siglaCadastrante, Boolean forcarTransferencia)
			throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(false)) {
			PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(siglaCadastrante);
			ExMobil mob = buscarMobil(codigoDocumento);
			if (mob.doc().isProcesso()) {
				mob = mob.doc().getUltimoVolume();
			} else if (contemApenasUmaVia(mob)) {
				mob = mob.doc().getPrimeiraVia();
			}
			if (forcarTransferencia)
				return new ExPodeSerTransferido(mob).eval();
			else
				return Ex.getInstance().getComp().pode(ExPodeTransferir.class, cadastranteParser.getPessoa(),
						cadastranteParser.getLotacao(), mob);
		}
	}

	/**
	 * Verifica se o móbil contém 1 e apenas 1 via. Se houver mais de uma via não há
	 * como determinar qual via deve ser transferida.
	 * 
	 * @param mob
	 * @return
	 */
	private boolean contemApenasUmaVia(ExMobil mob) {
		return mob.doc().getPrimeiraVia() != null && mob.doc().getSetVias().size() == 1;
	}

	private static boolean isAtendente(final DpPessoa titular,
			final DpLotacao lotaTitular, final ExMobil mob) throws Exception {
		if (mob.isGeral()) {
			for (ExMobil m : mob.doc().getExMobilSet()) {
				if (!m.isGeral() && m.isAtendente(titular, lotaTitular))
					return true;
			}
			return false;
		}
		if (!mob.isVia() && !mob.isVolume())
			return false;

		return mob.isAtendente(titular, lotaTitular);
	}
	
	public Boolean isAtendente(String codigoDocumento, String siglaTitular) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(false)) {
			try {
				PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(siglaTitular);
				ExMobil mob = buscarMobil(codigoDocumento);
				return isAtendente(cadastranteParser.getPessoa(), cadastranteParser.getLotacao(), mob);
			} catch (Exception ex) {
				Exception e = ctx.exceptionWithMessageFileAndLine(ex);
				ctx.rollback(e);
				throw e;
			}
		}
	}

	public String getAtendente(String codigoDocumento, String siglaTitular) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(false)) {
			try {
				PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(siglaTitular); 
				ExMobil mob = buscarMobil(codigoDocumento);

				if (mob.isGeralDeExpediente() && mob.doc().isFinalizado())
					mob = mob.doc().getPrimeiraVia();
				else if (mob.getDoc().isProcesso())
					mob = mob.getDoc().getUltimoVolume();

				Set<PessoaLotacaoParser> l = mob.getAtendente();
				for (PessoaLotacaoParser pl : l) {
					if (pl.getPessoa() != null) {
						return pl.getPessoa().getSiglaCompleta() + "@" + pl.getPessoa().getLotacao().getSiglaCompleta();
					} else if (pl.getLotacao() != null) {
						return "@" + pl.getLotacao().getSiglaCompleta();
					}
				}
				return null;
			} catch (Exception ex) {
				Exception e = ctx.exceptionWithMessageFileAndLine(ex);
				ctx.rollback(e);
				throw e;
			}
		}
	}

	public byte[] obterPdfPorNumeroAssinatura(String num) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(false)) {
			try {
				return Ex.getInstance().getBL().obterPdfPorNumeroAssinatura(num);
			} catch (Exception ex) {
				Exception e = ctx.exceptionWithMessageFileAndLine(ex);
				ctx.rollback(e);
				throw e;
			}
		}
	}

	public String buscarPorCodigo(String codigo) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(false)) {
			try {
				ExMobilSelecao sel = new ExMobilSelecao();
				sel.setSigla(codigo);
				sel.buscarPorSigla();
				String s = sel.getSigla();
				if (s != null && s.length() > 0)
					return s;
				return null;
			} catch (Exception ex) {
				Exception e = ctx.exceptionWithMessageFileAndLine(ex);
				ctx.rollback(e);
				throw e;
			}
		}
	}

	public String criarVia(String codigoDocumento, String siglaCadastrante) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(true)) {
			try {
				if (codigoDocumento == null)
					return null;
				ExMobil mob = buscarMobil(codigoDocumento);
				PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(siglaCadastrante);
				if (cadastranteParser.getLotacao() == null && cadastranteParser.getPessoa() == null)
					return null;
				Ex.getInstance().getBL().criarVia(cadastranteParser.getPessoa(),
						cadastranteParser.getLotacaoOuLotacaoPrincipalDaPessoa(), mob.doc());
				return mob.doc().getUltimaVia().getSigla();
			} catch (Exception ex) {
				Exception e = ctx.exceptionWithMessageFileAndLine(ex);
				ctx.rollback(e);
				throw e;
			}
		}
	}

	public String form(String codigoDocumento, String variavel) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(false)) {
			try {
				if (codigoDocumento == null)
					return null;
				ExMobil mob = buscarMobil(codigoDocumento);
				return mob.doc().getForm().get(variavel);
			} catch (Exception ex) {
				Exception e = ctx.exceptionWithMessageFileAndLine(ex);
				ctx.rollback(e);
				throw e;
			}
		}
	}

	@Override
	public Boolean exigirAnexo(String codigoDocumentoVia, String siglaCadastrante, String descricaoDoAnexo)
			throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(true)) {
			try {
				ExMobil mob = buscarMobil(codigoDocumentoVia);

				PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(siglaCadastrante);
				Ex.getInstance().getBL().exigirAnexo(cadastranteParser.getPessoa(), cadastranteParser.getLotacao(), mob,
						descricaoDoAnexo);
				return true;
			} catch (Exception ex) {
				Exception e = ctx.exceptionWithMessageFileAndLine(ex);
				ctx.rollback(e);
				throw e;
			}
		}
	}

	public String toJSON(String codigo) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(false)) {
			try {
				ExMobil mob = null;
				{
					final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
					filter.setSigla(codigo);
					mob = (ExMobil) dao().consultarPorSigla(filter);
					// return Ex.getInstance().getBL().toJSON(mob);
					return "";
				}
			} catch (Exception ex) {
				Exception e = ctx.exceptionWithMessageFileAndLine(ex);
				ctx.rollback(e);
				throw e;
			}
		}
	}
	
	@Override
	public String obterSiglaAtual(String codigoDocumentoVia)
			throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(false)) {
			ExMobil mob = buscarMobil(codigoDocumentoVia);
			return mob.getSigla();
		}
	}

	@Override
	public Boolean atualizarPrincipal(String codigoDocumento, String tipoPrincipal,
			String siglaPrincipal)
			throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(true)) {
			try {
				ExMobil mob = buscarMobil(codigoDocumento);
				ExTipoDePrincipal tipo = null;
				if (tipoPrincipal != null) 
					tipo = ExTipoDePrincipal.valueOf(tipoPrincipal);
				Ex.getInstance().getBL().atualizarPrincipal(mob.doc(), tipo, siglaPrincipal);
				return true;
			} catch (Exception ex) {
				Exception e = ctx.exceptionWithMessageFileAndLine(ex);
				ctx.rollback(e);
				throw e;
			}
		}
	}

	public String criarDocumento(String cadastranteStr, String subscritorStr, String destinatarioStr,
			String destinatarioCampoExtraStr, String descricaoTipoDeDocumento, String nomeForma, String nomeModelo,
			String nomePreenchimento, String classificacaoStr, String descricaoStr, Boolean eletronico,
			String nomeNivelDeAcesso, String conteudo, String siglaMobilPai, String siglaMobilFilho, String tipoPrincipal,
			String siglaPrincipal, Boolean finalizar) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(true)) {
			try {
				DpPessoa cadastrante = null;
				DpLotacao lotaCadastrante = null;
				DpPessoa subscritor = null;
				ExModelo modelo = null;
				ExFormaDocumento forma = null;
				ExTipoDocumento tipoDocumento = null;
				ExPreenchimento preenchimento = null;
				ExClassificacao classificacao = null;
				ExNivelAcesso nivelDeAcesso = null;
				DpLotacao destinatarioLotacao = null;
				DpPessoa destinatarioPessoa = null;
				CpOrgao destinatarioOrgaoExterno = null;

				ExDocumento doc = new ExDocumento();

				if (tipoPrincipal != null) {
					doc.setTipoDePrincipal(ExTipoDePrincipal.valueOf(tipoPrincipal));
					doc.setPrincipal(siglaPrincipal);
				}
				
				if (siglaMobilPai != null && !siglaMobilPai.isEmpty()) {
					final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
					filter.setSigla(siglaMobilPai);
					ExMobil mobPai = (ExMobil) dao().consultarPorSigla(filter);
					if (mobPai != null) {
						ExDocumento docPai = mobPai.getExDocumento();

						if (docPai.getExMobilPai() != null)
							throw new AplicacaoException("Não foi possível criar o documento pois o documento pai ("
									+ docPai.getSigla() + ") já é documento filho.");

						doc.setExMobilPai(mobPai);
					}
				}

				if (siglaMobilFilho != null && !siglaMobilFilho.isEmpty()) {
					final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
					filter.setSigla(siglaMobilFilho);
					ExMobil mobFilho = (ExMobil) dao().consultarPorSigla(filter);
					if (mobFilho != null) {
						ExDocumento docFilho = mobFilho.getExDocumento();

						if (docFilho.getExMobilAutuado() != null)
							throw new AplicacaoException("Não foi possível criar o documento pois o documento filho ("
									+ docFilho.getSigla() + ") já é autuado.");

						if (docFilho.isPendenteDeAssinatura())
							throw new AplicacaoException("Não foi possível criar o documento pois o documento filho ("
									+ docFilho.getSigla() + ") ainda não foi assinado.");

						doc.setExMobilAutuado(mobFilho);
					}
				}



				if (nomePreenchimento != null) {
					if (nomePreenchimento.matches("^\\d+$")) {
						preenchimento = dao().consultar(Long.parseLong(nomePreenchimento), ExPreenchimento.class,
								false);
					} else {
						ExPreenchimento filtro = new ExPreenchimento();
						filtro.setNomePreenchimento(nomePreenchimento);
						filtro.setExModelo(modelo);
						filtro.setDpLotacao(lotaCadastrante);
						List<ExPreenchimento> lista = dao().consultar(filtro);
						if (lista.size() == 1)
							preenchimento = lista.get(0);
					}
					if (preenchimento == null)
						throw new AplicacaoException(
								"Não foi possível encontrar um preenchimento com os dados informados.");

					final String strBanco = new String(preenchimento.getPreenchimentoBlob());
					final String arrStrBanco[] = strBanco.split("&");
					for (final String elem : arrStrBanco) {
						final String[] paramNameAndValue = ((String) elem).split("=");
						final String paramName = paramNameAndValue[0];
						String paramValueEncoded = paramNameAndValue[1];
						String paramValue = URLDecoder.decode(paramValueEncoded, "ISO-8859-1");
						switch (paramName) {
						case "subscritorSel.id":
							subscritorStr = dao().consultar(Long.parseLong(paramValue), DpPessoa.class, false)
									.getSigla();
							break;
						case "destinatarioSel.id":
							destinatarioStr = dao().consultar(Long.parseLong(paramValue), DpPessoa.class, false)
									.getSigla();
							break;
						case "lotacaoDestinatarioSel.id":
							destinatarioStr = dao().consultar(Long.parseLong(paramValue), DpLotacao.class, false)
									.getSigla();
							break;
						case "classificacaoSel.id":
							classificacaoStr = dao().consultar(Long.parseLong(paramValue), ExClassificacao.class, false)
									.getSigla();
							break;
						case "descricao":
							descricaoStr = paramValue;
							break;
						case "nivelDeAcesso":
							nomeNivelDeAcesso = paramValue;
							break;
						default:
							if (conteudo == null)
								conteudo = "";
							else
								conteudo += "&";
							conteudo += paramName + "=" + paramValueEncoded;
						}
					}
				}

				if (subscritorStr != null) {
					subscritor = dao().getPessoaFromSigla(subscritorStr);
					if (subscritor == null)
						throw new AplicacaoException(
								"Não foi possível encontrar um subscritor com a matrícula informada.");
					if (subscritor.isFechada())
						throw new AplicacaoException("O subscritor não está mais ativo.");
				}

				if (cadastranteStr == null || cadastranteStr.isEmpty())
					throw new AplicacaoException("A matrícula do cadastrante não foi informada.");

				PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(cadastranteStr);

				cadastrante = cadastranteParser.getPessoa();
				lotaCadastrante = cadastranteParser.getLotacaoOuLotacaoPrincipalDaPessoa();

				if (cadastrante == null && lotaCadastrante != null) {
					if (subscritor != null)
						cadastrante = subscritor;
					else {
						List<DpPessoa> pessoas = dao().pessoasPorLotacao(lotaCadastrante.getId(), false, false);
						if (pessoas == null || pessoas.size() == 0)
							throw new AplicacaoException(
									"Não foi possível eleger um cadastrante para a lotação informada.");
						cadastrante = pessoas.get(0);
					}
				}

				if (cadastrante == null || lotaCadastrante == null)
					throw new AplicacaoException(
							"Não foi possível encontrar um cadastrante ou uma lotação cadastrante com a matrícula informada.");

				if (cadastrante != null && cadastrante.isFechada())
					throw new AplicacaoException("O cadastrante não está mais ativo.");

				if (descricaoTipoDeDocumento == null)
					tipoDocumento = (dao().consultar(ExTipoDocumento.TIPO_DOCUMENTO_INTERNO, ExTipoDocumento.class,
							false));
				else
					tipoDocumento = dao().consultarExTipoDocumento(descricaoTipoDeDocumento);

				if (tipoDocumento == null)
					throw new AplicacaoException(
							"Não foi possível encontrar o Tipo de Documento. Os Tipos de Documentos aceitos são: 1-Interno Produzido, 2-Interno Importado, 3-Externo");

				if (nomeModelo == null)
					throw new AplicacaoException("O modelo não foi informado.");

				// Aceita também o ID do modelo
				if (nomeModelo.matches("^\\d+$")) {
					modelo = dao().consultar(Long.parseLong(nomeModelo), ExModelo.class, false);
				} else {
					if (nomeForma == null)
						throw new AplicacaoException("O Tipo não foi informado.");
					modelo = dao().consultarExModelo(nomeForma, nomeModelo);
				}

				if (modelo == null)
					throw new AplicacaoException("Não foi possível encontrar um modelo com os dados informados.");
				else
					modelo = modelo.getModeloAtual();

				forma = modelo.getExFormaDocumento();

				if (!forma.podeSerDoTipo(tipoDocumento))
					throw new AplicacaoException("O documento do tipo " + forma.getDescricao() + " não pode ser "
							+ tipoDocumento.getDescricao());

				if ((classificacaoStr == null || classificacaoStr.isEmpty()) && !modelo.isClassificacaoAutomatica()) {
					if (doc.getExMobilPai() != null && doc.getExMobilPai().doc().getExClassificacao() != null)
						classificacao = doc.getExMobilPai().doc().getExClassificacao();
					else if (doc.getExMobilAutuado() != null && doc.getExMobilAutuado().doc().getExClassificacao() != null)
						classificacao = doc.getExMobilAutuado().doc().getExClassificacao();
					else
						throw new AplicacaoException("A Classificação não foi informada.");
				} else {
					if (modelo.isClassificacaoAutomatica())
						classificacao = modelo.getExClassificacao();
					else
						classificacao = dao().consultarExClassificacao(classificacaoStr);
					if (classificacao == null)
						throw new AplicacaoException(
								"Não foi possível encontrar uma classificação com o código informado.");
				}
				classificacao = classificacao.getClassificacaoAtual();

				if (eletronico == null)
					eletronico = true;

				CpSituacaoDeConfiguracaoEnum idSit = Ex.getInstance().getConf().buscaSituacao(modelo, tipoDocumento,
						cadastrante, lotaCadastrante, ExTipoDeConfiguracao.ELETRONICO);

				if (idSit == CpSituacaoDeConfiguracaoEnum.OBRIGATORIO) {
					eletronico = true;
				} else if (idSit == CpSituacaoDeConfiguracaoEnum.PROIBIDO) {
					eletronico = false;
				}

				if (nomeNivelDeAcesso == null) {
					Date dt = ExDao.getInstance().consultarDataEHoraDoServidor();

					ExConfiguracao config = new ExConfiguracao();
					config.setDpPessoa(cadastrante);
					config.setLotacao(lotaCadastrante);
					config.setExTipoDocumento(tipoDocumento);
					config.setExFormaDocumento(forma);
					config.setExModelo(modelo);
					config.setExClassificacao(classificacao);
					config.setCpTipoConfiguracao(ExTipoDeConfiguracao.NIVEL_DE_ACESSO);
					config.setCpSituacaoConfiguracao(CpSituacaoDeConfiguracaoEnum.DEFAULT);

					ExConfiguracaoCache exConfig = ((ExConfiguracaoCache) Ex.getInstance().getConf()
							.buscaConfiguracao(config, new int[] { ExConfiguracaoBL.NIVEL_ACESSO }, dt));

					if (exConfig != null)
						nivelDeAcesso = dao().consultar(exConfig.exNivelAcesso, ExNivelAcesso.class, false);
				} else {
					nivelDeAcesso = dao().consultarExNidelAcesso(nomeNivelDeAcesso);
				}

				if (nivelDeAcesso == null)
					nivelDeAcesso = dao().consultar(6L, ExNivelAcesso.class, false);

				List<ExNivelAcesso> niveisFinal = NivelDeAcessoUtil.getListaNivelAcesso(tipoDocumento, forma, modelo,
						classificacao, cadastrante, lotaCadastrante);

				if (niveisFinal != null && !niveisFinal.isEmpty() & !niveisFinal.contains(nivelDeAcesso))
					nivelDeAcesso = niveisFinal.get(0);

				doc.setCadastrante(cadastrante);
				doc.setLotaCadastrante(lotaCadastrante);
				if (subscritor != null && doc.getTitular() == null) {
					doc.setTitular(subscritor);
					doc.setLotaTitular(subscritor.getLotacao());
				}

				if (destinatarioStr != null) {
					try {
						destinatarioLotacao = dao().getLotacaoFromSigla(destinatarioStr);

						if (destinatarioLotacao != null)
							doc.setLotaDestinatario(destinatarioLotacao);
					} catch (Exception e) {
					}
				}

				if (destinatarioStr != null && destinatarioLotacao == null) {
					try {
						destinatarioPessoa = dao().getPessoaFromSigla(destinatarioStr);

						if (destinatarioPessoa != null)
							doc.setDestinatario(destinatarioPessoa);
					} catch (Exception e) {
					}
				}

				if (destinatarioStr != null && destinatarioLotacao == null && destinatarioPessoa == null) {
					try {
						destinatarioOrgaoExterno = dao().getOrgaoFromSiglaExata(destinatarioStr);

						if (destinatarioOrgaoExterno != null) {
							doc.setOrgaoExternoDestinatario(destinatarioOrgaoExterno);
							doc.setNmOrgaoExterno(destinatarioCampoExtraStr);
						}
					} catch (Exception e) {
					}
				}

				if (destinatarioStr != null && destinatarioLotacao == null && destinatarioPessoa == null
						&& destinatarioOrgaoExterno == null) {
					doc.setNmDestinatario(destinatarioStr);
				}

				doc.setSubscritor(subscritor);
				if (subscritor != null) {
					doc.setLotaSubscritor(subscritor.getLotacao());
					doc.setOrgaoUsuario(subscritor.getOrgaoUsuario());
				}
				doc.setExTipoDocumento(tipoDocumento);
				doc.setExFormaDocumento(forma);
				doc.setExModelo(modelo);

				if (!modelo.isDescricaoAutomatica()) {
					if (descricaoStr != null && !descricaoStr.isEmpty())
						doc.setDescrDocumento(descricaoStr);
					else if (doc.getExMobilPai() != null && doc.getExMobilPai().doc().getDescrDocumento() != null && !doc.getExMobilPai().doc().getDescrDocumento().isEmpty())
						doc.setDescrDocumento(doc.getExMobilPai().doc().getDescrDocumento());
					else if (doc.getExMobilAutuado() != null && doc.getExMobilAutuado().doc().getDescrDocumento() != null && !doc.getExMobilAutuado().doc().getDescrDocumento().isEmpty())
						doc.setDescrDocumento(doc.getExMobilAutuado().doc().getDescrDocumento());
				}

				doc.setExClassificacao(classificacao);
				if (eletronico)
					doc.setFgEletronico("S");
				else
					doc.setFgEletronico("N");

				doc.setExNivelAcesso(nivelDeAcesso);

				ExMobil mob = new ExMobil();
				mob.setExTipoMobil(dao().consultar(ExTipoMobil.TIPO_MOBIL_GERAL, ExTipoMobil.class, false));
				mob.setNumSequencia(1);
				mob.setExMovimentacaoSet(new TreeSet<ExMovimentacao>());
				mob.setExDocumento(doc);

				doc.setExMobilSet(new TreeSet<ExMobil>());
				doc.getExMobilSet().add(mob);

				if (conteudo == null)
					conteudo = "";
				try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
					baos.write(conteudo.getBytes());

					doc.setConteudoTpDoc("application/zip");
					doc.setConteudoBlobForm(baos.toByteArray());
				}

				doc = Ex.getInstance().getBL().gravar(cadastrante, cadastrante, lotaCadastrante, doc);

				if (finalizar)
					Ex.getInstance().getBL().finalizar(cadastrante, lotaCadastrante, doc);

				return doc.getSigla();
			} catch (Exception ex) {
				Exception e = ctx.exceptionWithMessageFileAndLine(ex);
				ctx.rollback(e);
				throw e;
			}
		}
	}

	public String cadastrante(String codigoDocumentoVia) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(false)) {
			ExMobil mob = buscarMobil(codigoDocumentoVia);
			return SiglaParser.makeSigla(mob.doc().getCadastrante(), mob.doc().getLotaCadastrante());
		}
	}

	public String titular(String codigoDocumentoVia) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(false)) {
			ExMobil mob = buscarMobil(codigoDocumentoVia);
			return SiglaParser.makeSigla(mob.doc().getTitular(), mob.doc().getLotaTitular());
		}
	}

	public String subscritor(String codigoDocumentoVia) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(false)) {
			ExMobil mob = buscarMobil(codigoDocumentoVia);
			return SiglaParser.makeSigla(mob.doc().getSubscritor(), mob.doc().getLotaSubscritor());
		}
	}

	public String destinatario(String codigoDocumentoVia) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(false)) {
			ExMobil mob = buscarMobil(codigoDocumentoVia);
			return SiglaParser.makeSigla(mob.doc().getDestinatario(), mob.doc().getLotaDestinatario());
		}
	}

	public String gestor(String codigoDocumentoVia) throws Exception {
		return obterPrimeiroResponsavelPorIdPapel(codigoDocumentoVia, ExPapel.PAPEL_GESTOR);
	}

	public String fiscalTecnico(String codigoDocumentoVia) throws Exception {
		return obterPrimeiroResponsavelPorIdPapel(codigoDocumentoVia, ExPapel.PAPEL_FISCAL_TECNICO);
	}

	public String fiscalAdministrativo(String codigoDocumentoVia) throws Exception {
		return obterPrimeiroResponsavelPorIdPapel(codigoDocumentoVia, ExPapel.PAPEL_FISCAL_ADMINISTRATIVO);
	}

	public String interessado(String codigoDocumentoVia) throws Exception {
		return obterPrimeiroResponsavelPorIdPapel(codigoDocumentoVia, ExPapel.PAPEL_INTERESSADO);
	}

	public String autorizador(String codigoDocumentoVia) throws Exception {
		return obterPrimeiroResponsavelPorIdPapel(codigoDocumentoVia, ExPapel.PAPEL_AUTORIZADOR);
	}

	public String revisor(String codigoDocumentoVia) throws Exception {
		return obterPrimeiroResponsavelPorIdPapel(codigoDocumentoVia, ExPapel.PAPEL_REVISOR);
	}

	public String liquidante(String codigoDocumentoVia) throws Exception {
		return obterPrimeiroResponsavelPorIdPapel(codigoDocumentoVia, ExPapel.PAPEL_LIQUIDANTE);
	}

	private String obterPrimeiroResponsavelPorIdPapel(String codigoDocumentoVia, long papel)
			throws Exception, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
		try (ExSoapContext ctx = new ExSoapContext(false)) {
			ExMobil mob = buscarMobil(codigoDocumentoVia);
			List<DpResponsavel> l = mob.doc().getResponsaveisPorPapel(dao().consultar(papel, ExPapel.class, false));
			if (l == null || l.size() == 0)
				return null;
			DpResponsavel r = l.get(0);
			return r.getSiglaDePessoaEOuLotacao();
		}
	}

	public Boolean isModeloIncluso(String codigoDocumento, Long idModelo, Date depoisDaData) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(false)) {
			try {
				ExMobil mob = buscarMobil(codigoDocumento);
				if (mob.isGeral())
					mob = mob.getDoc().getPrimeiroMobil();
				return mob.isModeloIncluso(idModelo, depoisDaData);
			} catch (Exception ex) {
				Exception e = ctx.exceptionWithMessageFileAndLine(ex);
				ctx.rollback(e);
				throw e;			}
		}
	}
	
	public Boolean isAuxiliarIncluso(String codigoDocumento, Date depoisDaData) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(false)) {
			try {
				ExMobil mob = buscarMobil(codigoDocumento).getDoc().getMobilGeral();

				return mob.isAuxiliarIncluso(depoisDaData);
			} catch (Exception ex) {
				ctx.rollback(ex);
				throw ex;
			}
		}
	}

	public String obterNumeracaoExpediente(Long idOrgaoUsu, Long idFormaDoc, Long anoEmissao) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(true)) {
			try {
				Long idDocNumeracao = null;
				Long nrDocumento = 0L;
				ContextoPersistencia.flushTransaction();

				// Verifica se Range atual existe
				ExDocumentoNumeracao docNumeracao = dao().obterNumeroDocumento(idOrgaoUsu, idFormaDoc, anoEmissao,
						false);

				if (docNumeracao == null) {
					CpOrgaoUsuario orgaoUsuario = new CpOrgaoUsuario();
					ExFormaDocumento formaDocumento = new ExFormaDocumento();
					orgaoUsuario.setIdOrgaoUsu(idOrgaoUsu);
					formaDocumento.setIdFormaDoc(idFormaDoc);

					orgaoUsuario = dao().consultarPorId(orgaoUsuario);
					formaDocumento = dao().consultarExFormaPorId(idFormaDoc);

					idDocNumeracao = dao().existeRangeNumeroDocumento(idOrgaoUsu, idFormaDoc);

					if ((idDocNumeracao != null)
							&& !new ExPodeReiniciarNumeracao(orgaoUsuario, formaDocumento).eval()) { // Existe
																													// Range
																													// Anterior
																													// e
																													// Não
																													// pode
																													// Resetar
																													// numeracao
						dao().updateMantemRangeNumeroDocumento(idDocNumeracao);

					} else { // Não existe ou deve resetar numeração
						ExDocumentoNumeracao documentoNumeracao = new ExDocumentoNumeracao();

						documentoNumeracao.setIdOrgaoUsu(idOrgaoUsu);
						documentoNumeracao.setIdFormaDoc(idFormaDoc);
						documentoNumeracao.setFlAtivo("1");
						documentoNumeracao.setAnoEmissao(anoEmissao);

						nrDocumento = 1L;
						documentoNumeracao.setNrDocumento(nrDocumento);
						documentoNumeracao.setNrInicial(nrDocumento);

						dao().gravar(documentoNumeracao);

						documentoNumeracao = null;
					}

					orgaoUsuario = null;
					formaDocumento = null;

				} else { // Range vigente. Só incrementa
					idDocNumeracao = docNumeracao.getIdDocumentoNumeracao();
					dao().incrementNumeroDocumento(idDocNumeracao);
				}

				if (nrDocumento != 1L) { // Obtém Número Gerado antes de liberar registro
					nrDocumento = dao().obterNumeroGerado(idOrgaoUsu, idFormaDoc, anoEmissao);
				}

				ContextoPersistencia.flushTransaction();

				// Retorno em String para WS
				return nrDocumento.toString();
			} catch (Exception ex) {
				ctx.rollback(ex);
				throw new Exception(
						"Ocorreu um problema na obtenção do número do documento definitivo: " + ex.getMessage(), ex);
			}
		}
	}

	public String obterSequencia(Integer tipoSequencia, Long anoEmissao, String zerarInicioAno) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(true)) {
			try {
				Long idSeq = null;
				Long numero = 0L;
				ContextoPersistencia.flushTransaction();

				// Verifica se Range atual existe
				ExSequencia sequencia = dao().obterSequencia(tipoSequencia, anoEmissao, true);

				if (sequencia == null) {

					sequencia = dao().existeRangeSequencia(tipoSequencia);

					if (sequencia != null && "0".equals(sequencia.getZerarInicioAno())) { // Existe Range Anterior e Não
																							// pode Resetar numeracao
						dao().updateMantemRangeSequencia(sequencia.getIdSequencia());

					} else { // Não existe ou deve resetar numeração
						ExSequencia exSequencia = new ExSequencia();

						exSequencia.setTipoSequencia(tipoSequencia);
						exSequencia.setFlAtivo("1");
						exSequencia.setAnoEmissao(anoEmissao);
						if (zerarInicioAno != null && !"".equals(zerarInicioAno)) {
							exSequencia.setZerarInicioAno(zerarInicioAno);
						} else {
							exSequencia.setZerarInicioAno("1");
						}

						numero = 1L;
						exSequencia.setNumero(numero);
						exSequencia.setNrInicial(numero);

						dao().gravar(exSequencia);

						exSequencia = null;
					}

				} else { // Range vigente. Só incrementa
					idSeq = sequencia.getIdSequencia();
					dao().incrementNumero(idSeq);
				}

				if (numero != 1L) { // Obtém Número Gerado antes de liberar registro
					numero = dao().obterNumeroGerado(tipoSequencia, anoEmissao);
				}

				ContextoPersistencia.flushTransaction();

				// Retorno em String para WS
				return numero.toString();
			} catch (Exception ex) {
				ctx.rollback(ex);
				throw new Exception("Ocorreu um problema na obtenção do número: " + ex.getMessage(), ex);
			}
		}
	}

	public String obterSiglaMobilPorIdDoc(Long idDoc) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(false)) {
			ExDocumento doc = ExDao.getInstance().consultar(idDoc, ExDocumento.class, false);
			if (doc != null) {
				return doc.getPrimeiroMobil().getSigla();
			} else
				return "";
		}
	}

	public String obterMetadadosDocumento(String siglaDocumento, String token) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(false)) {
			if (Prop.getBool("/siga.ws.seguranca.token.jwt"))
				SigaUtil.getInstance().validarToken(token);

			final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
			filter.setSigla(siglaDocumento);

			ExMobil mob = dao().consultarPorSigla(filter);
			if (mob != null) {
				return Ex.getInstance().getBL().documentoToJSON(mob.getDoc());
			}
		} catch (Exception e) {
			throw new Exception("Ocorreu um problema na obtenção dos Metadados do Documento: " + e.getMessage(), e);
		}
		return "Ocorreu um problema na obtenção dos Metadados do Documento";
	}

	public String obterMarcadores(String token) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(false)) {
			if (Prop.getBool("/siga.ws.seguranca.token.jwt"))
				SigaUtil.getInstance().validarToken(token);

			return Ex.getInstance().getBL().marcadoresGeraisTaxonomiaAdministradaToJSON();

		} catch (Exception e) {
			throw new Exception("Ocorreu um problema na obtenção da lista de Marcadores: " + e.getMessage(), e);
		}
	}

	public String publicarDocumentoPortal(String siglaDocumento, String cadastranteStr, String marcadoresStr,
			String token) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(true)) {
			try {
				if (Prop.getBool("/siga.ws.seguranca.token.jwt"))
					SigaUtil.getInstance().validarToken(token);

				DpPessoa cadastrante = null;

				cadastrante = dao().getPessoaFromSigla(cadastranteStr);

				if (cadastrante == null)
					throw new AplicacaoException(
							"Não foi possível encontrar um cadastrante com a matrícula informada.");

				if (cadastrante.isFechada())
					throw new AplicacaoException("O cadastrante não está mais ativo.");

				final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
				filter.setSigla(siglaDocumento);

				ExMobil mob = dao().consultarPorSigla(filter);

				if (mob != null) {

					/* Valida se usuário WS pode movimentar */
					DpPessoa cadastranteWS = null;
					cadastranteWS = dao()
							.getPessoaFromSigla(SigaUtil.getInstance().parseTokenJwt(token).get("sub").toString());
					Ex.getInstance().getComp().afirmar("Não é possível " + SigaMessages.getMessage("documento.publicar.portaltransparencia"),
							ExPodePublicarPortalDaTransparencia.class, cadastranteWS, cadastranteWS.getLotacao(), mob);
					
					/* Fim da Validação */
					String[] listaMarcadores = null;
					if (!"".equals(marcadoresStr)) {
						listaMarcadores = marcadoresStr.split(",");
					}

					CpToken sigaUrlPermanente = new CpToken();
					sigaUrlPermanente = Ex.getInstance().getBL().publicarTransparencia(mob, cadastrante,
							cadastrante.getLotacao(), listaMarcadores, true);
					String url = System.getProperty("siga.ex.enderecoAutenticidadeDocs")
							.replace("/sigaex/public/app/autenticar", "");
					String caminho = url + "/siga/public/app/sigalink/1/" + sigaUrlPermanente.getToken();

					return "Documento " + siglaDocumento
							+ " enviado para publicação. Gerado para acesso externo ao documento: " + caminho;
				}
			} catch (Exception ex) {
				ctx.rollback(ex);
				throw new Exception("Ocorreu um problema na publicação de documento em Portal: " + ex.getMessage(), ex);
			}
			return "Ocorreu um problema na publicação de documento em Portal.";
		}
	}

	@Override
	public void incluirCopiaDeDocumento(String siglaCadastrante, String siglaMobilPai, String siglaMobilFilho)
			throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(true)) {
			try {
				if (siglaMobilPai == null)
					throw new Exception("Informe a sigla do móbil pai");
				ExMobil mobPai = buscarMobil(siglaMobilPai);
				if (mobPai == null)
					throw new Exception("Móbil pai inexistente");
				if (mobPai.isGeral())
					mobPai = mobPai.doc().getMobilDefaultParaReceberJuntada();
				if (siglaMobilFilho == null)
					throw new Exception("Informe a sigla do móbil filho");
				ExMobil mobFilho = buscarMobil(siglaMobilFilho);
				if (mobFilho == null)
					throw new Exception("Móbil filho inexistente");

				PessoaLotacaoParser cadastranteParser = new PessoaLotacaoParser(siglaCadastrante);
//				if (cadastranteParser.getLotacao() == null && cadastranteParser.getPessoa() == null)
//					throw new Exception("Cadastrante inválido");

				Ex.getInstance().getBL().copiar(cadastranteParser.getPessoa(),
						cadastranteParser.getLotacaoOuLotacaoPrincipalDaPessoa(), mobPai, mobFilho, null, null, null);
				return;
			} catch (Exception ex) {
				Exception e = ctx.exceptionWithMessageFileAndLine(ex);
				ctx.rollback(e);
				throw e;
			}
		}
	}

	@Override
	public boolean isJuntado(String siglaMobilFilho, String siglaMobilPai) throws Exception {
		try (ExSoapContext ctx = new ExSoapContext(false)) {
			if (siglaMobilFilho == null)
				throw new Exception("Informe a sigla do móbil filho");
			ExMobil mobFilho = buscarMobil(siglaMobilFilho);

			if (mobFilho.isGeralDeExpediente())
				mobFilho = mobFilho.doc().getPrimeiraVia();
			else if (mobFilho.isGeralDeProcesso())
				mobFilho = mobFilho.doc().getUltimoVolume();
			
			if (mobFilho == null)
				return false;

			if (!mobFilho.isJuntado())
				return false;

			if (siglaMobilPai == null)
				return mobFilho.isJuntado();
			ExMobil mobPai = buscarMobil(siglaMobilPai);

			if (mobPai.isGeralDeExpediente())
				mobPai = mobPai.doc().getPrimeiraVia();
			else if (mobPai.isGeralDeProcesso())
				mobPai = mobPai.doc().getUltimoVolume();
			
			return mobFilho.getMobilPrincipal().equals(mobPai)
					|| mobFilho.getMobilPrincipal().equals(mobPai.doc().getMobilGeral());
		}
	}

}
