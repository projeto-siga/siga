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
package br.gov.jfrj.siga.vraptor;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.QueryStatistics;
import org.hibernate.stat.SessionStatistics;
import org.hibernate.stat.Statistics;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExPreenchimento;
import br.gov.jfrj.siga.ex.ExSituacaoConfiguracao;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoMobil;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.ExTpDocPublicacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;
import br.gov.jfrj.siga.ex.bl.BIE.HierarquizadorBoletimInterno;
import br.gov.jfrj.siga.ex.util.FuncoesEL;
import br.gov.jfrj.siga.ex.util.GeradorRTF;
import br.gov.jfrj.siga.ex.util.PublicacaoDJEBL;
import br.gov.jfrj.siga.ex.vo.ExDocumentoVO;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.libs.webwork.CpOrgaoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpLotacaoSelecao;
import br.gov.jfrj.siga.libs.webwork.DpPessoaSelecao;
import br.gov.jfrj.siga.libs.webwork.Selecao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

import com.opensymphony.xwork.Action;

@Resource
public class ExDocumentoController extends ExController {

	public ExDocumentoController(HttpServletRequest request, Result result, SigaObjects so) {
		super(request, result, CpDao.getInstance(), so);
	}



	public String aAlterarPreenchimento(ExDocumentoAction exDocumentoAct) throws Exception {

		ExPreenchimento exPreenchimento = new ExPreenchimento();

		dao().iniciarTransacao();
		exPreenchimento.setIdPreenchimento(exDocumentoAct.getPreenchimento());
		exPreenchimento = dao().consultar(exDocumentoAct.getPreenchimento(), ExPreenchimento.class,
				false);

		exPreenchimento.setPreenchimentoBA(getByteArrayFormPreenchimento());
		dao().gravar(exPreenchimento);
		dao().commitTransacao();

		exDocumentoAct.setPreenchimento(exPreenchimento.getIdPreenchimento());

		String url = getUrlEncodedParameters();
		if (url.indexOf("preenchimento") >= 0) {
			String parte1 = url.substring(0, url.indexOf("preenchimento"));
			String parte2 = url.substring(url.indexOf("&",
					url.indexOf("&preenchimento") + 1) + 1);
			parte2 = parte2 + "&preenchimento=" + exDocumentoAct.getPreenchimento();
			exDocumentoAct.setPreenchRedirect(parte1 + parte2);
		} else
			exDocumentoAct.setPreenchRedirect(getUrlEncodedParameters());

		return edita();
	}

	public String aAnexo(ExDocumentoAction exDocumentoAct) throws Exception {
		buscarDocumento(true, exDocumentoAct);
		return Action.SUCCESS;
	}

	public String aCancelarDocumento(ExDocumentoAction exDocumentoAct) throws Exception {
		buscarDocumento(true, exDocumentoAct);

		try {
			Ex.getInstance().getBL()
					.cancelarDocumento(getCadastrante(), getLotaTitular(), exDocumentoAct.getDoc());
		} catch (final Exception e) {
			throw e;
		}
		return Action.SUCCESS;

	}

	// Converte encode de url de iso-8859-1 para utf-8
	private String ConverteEncodeDeUriDeIsoParaUtf(String sUri) {
		// return sUri;
		StringBuilder sb = new StringBuilder();
		String aParametros[] = sUri.split("&");
		for (int i = 0; i < aParametros.length; i++) {
			String aTupla[] = aParametros[i].split("=");
			try {
				sb.append(URLEncoder.encode(
						URLDecoder.decode(aTupla[0], "iso-8859-1"), "utf-8"));
				sb.append("=");
				sb.append(URLEncoder.encode(
						URLDecoder.decode(aTupla[1], "iso-8859-1"), "utf-8"));
			} catch (UnsupportedEncodingException e) {
			}
			if (i < aParametros.length - 1)
				sb.append("&");
		}
		return sb.toString();
	}

	public boolean validar() {
		if (getPar().get("obrigatorios") != null)
			for (String valor : getPar().get("obrigatorios"))
				if (getPar().get(valor) == null
						|| getPar().get(valor)[0].trim().equals("")
						|| getPar().get(valor)[0].trim().equals("Não")
						|| getPar().get(valor)[0].trim().equals("Nao"))
					return false;
		return true;
	}

	public String aCarregarPreenchimento(ExDocumentoAction exDocumentoAct) throws Exception {
		ExPreenchimento exPreenchimento = new ExPreenchimento();

		// Obtém arrStrBanco[], com os parâmetros vindos do banco
		exPreenchimento = dao().consultar(exDocumentoAct.getPreenchimento(), ExPreenchimento.class,
				false);
		String strBanco = new String(exPreenchimento.getPreenchimentoBA());
		String arrStrBanco[] = strBanco.split("&");
		String strBancoLimpa = new String();

		// seta os atributos da action com base nos valores do banco, fazendo o
		// decoding da string
		for (String elem : arrStrBanco) {
			String[] paramNameAndValue = ((String) elem).split("=");
			String paramName = paramNameAndValue[0];
			String paramValue = paramNameAndValue[1];
			String paramValueDecoded = URLDecoder.decode(paramValue,
					"ISO-8859-1");
			String paramValueEncodedUTF8 = URLEncoder.encode(paramValueDecoded,
					"UTF-8");
			try {
				if (!paramName.contains("Sel.id")) {
					final String mName = "set"
							+ paramName.substring(0, 1).toUpperCase()
							+ paramName.substring(1);
					if (getPar().get(paramName) != null
							|| (paramName.contains("nmOrgaoExterno"))
							|| (paramName.contains("nmDestinatario"))) {
						Class paramType = this.getClass()
								.getDeclaredField(paramName).getType();
						Constructor paramTypeContructor = paramType
								.getConstructor(new Class[] { String.class });
						final Method method = this.getClass().getMethod(mName,
								new Class[] { paramType });
						method.invoke(
								this,
								new Object[] { paramTypeContructor
										.newInstance(new Object[] { (paramValueDecoded) }) });
					}
				} else {
					final String mName = "get"
							+ paramName.substring(0, 1).toUpperCase()
							+ paramName.substring(1, paramName.indexOf(".id"));
					if (getPar().get(paramName) != null
							|| (paramName.contains("estinatarioSel.id"))) {
						final Method method = this.getClass().getMethod(mName);
						Selecao sel = (Selecao) method.invoke(this);
						sel.setId(Long.parseLong(paramValue));
						sel.buscarPorId();
					}
				}

			} catch (NoSuchMethodException nSME) {
			} catch (NoSuchFieldException nSFE) {
			} catch (NumberFormatException nfe) {
				paramValue = "";
			} catch (InvocationTargetException nfe) {
				paramValue = "";
			} finally {
				strBancoLimpa += "&" + paramName + "=" + paramValueEncodedUTF8;
			}
		}

		// Obtém arrStrURL[], com os parâmetros atuais da edita.jsp
		String strURL = getUrlEncodedParameters();
		String arrStrURL[] = strURL.split("&");
		String strURLLimpa = "";

		// limpa a url vinda do browser, tirando o que já consta na string do
		// banco, tirando também os .sigla e .descricao
		if (arrStrURL.length > 0) {
			for (String s : arrStrURL) {
				String arrStrURL2[] = s.split("=");
				if (arrStrURL2.length > 1 && !arrStrURL2[0].contains(".sigla")
						&& !arrStrURL2[0].contains(".descricao")
						&& !strBanco.contains(arrStrURL2[0] + "="))
					strURLLimpa = strURLLimpa + s + "&";
			}
		}

		exDocumentoAct.setPreenchRedirect(strURLLimpa + strBancoLimpa);

		return edita();
	}

	public String aCriarVia(ExDocumentoAction exDocumentoAct) throws Exception {
		buscarDocumento(true, exDocumentoAct);

		if (!Ex.getInstance().getComp()
				.podeCriarVia(getTitular(), getLotaTitular(), exDocumentoAct.getMob()))
			throw new AplicacaoException(
					"Não é possível criar vias neste documento");
		try {
			Ex.getInstance().getBL()
					.criarVia(getCadastrante(), getLotaTitular(), exDocumentoAct.getDoc());
		} catch (final Exception e) {
			throw e;
		}

		return Action.SUCCESS;
	}

	public String aCriarVolume(ExDocumentoAction exDocumentoAct) throws Exception {
		buscarDocumento(true, exDocumentoAct);

		if (!Ex.getInstance().getComp()
				.podeCriarVolume(getTitular(), getLotaTitular(), exDocumentoAct.getMob()))
			throw new AplicacaoException(
					"Não é possível criar volumes neste documento");
		try {
			Ex.getInstance().getBL()
					.criarVolume(getCadastrante(), getLotaTitular(), exDocumentoAct.getDoc());
		} catch (final Exception e) {
			throw e;
		}

		return Action.SUCCESS;
	}

	@Get("app/expediente/doc/editar")
	public String edita() throws Exception {
		
		ExDocumentoAction exDocumentoAct = new ExDocumentoAction();

		buscarDocumentoOuNovo(true, exDocumentoAct);

		if ((getPostback() == null) || (param("docFilho") != null)) {
			exDocumentoAct.setTipoDestinatario(2);
			exDocumentoAct.setIdFormaDoc(2);
			exDocumentoAct.setIdTpDoc(1L);

			ExNivelAcesso nivelDefault = exDocumentoAct.getNivelAcessoDefault();
			if (nivelDefault != null) {
				exDocumentoAct.setNivelAcesso(nivelDefault.getIdNivelAcesso());
			} else
				exDocumentoAct.setNivelAcesso(1L);

			exDocumentoAct.setIdMod(((ExModelo) dao().consultarAtivoPorIdInicial(
					ExModelo.class, 26L)).getIdMod());
		}

		if (exDocumentoAct.isCriandoAnexo() && exDocumentoAct.getId() == null && getPostback() == null) {
			exDocumentoAct.setIdFormaDoc(60);
			exDocumentoAct.setIdMod(((ExModelo) dao().consultarAtivoPorIdInicial(
					ExModelo.class, 507L)).getIdMod());
		}

		if (exDocumentoAct.getDespachando() && exDocumentoAct.getId() == null
				&& (getPostback() == null || getPostback() == 0)) {

			exDocumentoAct.setIdFormaDoc(8);

		}

		if (exDocumentoAct.getId() == null && exDocumentoAct.getDoc() != null)
			exDocumentoAct.setId(exDocumentoAct.getDoc().getIdDoc());

		if (exDocumentoAct.getId() == null) {
			if (getLotaTitular().isFechada())
				throw new AplicacaoException(
						"A lotação "
								+ getLotaTitular().getSiglaLotacao()
								+ " foi extinta em "
								+ new SimpleDateFormat("dd/MM/yyyy")
										.format(getLotaTitular()
												.getDataFimLotacao())
								+ ". Não é possível gerar expedientes em lotação extinta.");
			exDocumentoAct.setDoc(new ExDocumento());
			exDocumentoAct.getDoc().setOrgaoUsuario(getTitular().getOrgaoUsuario());
		} else {
			exDocumentoAct.setDoc(daoDoc(exDocumentoAct.getId()));

			if (!Ex.getInstance().getComp()
					.podeEditar(getTitular(), getLotaTitular(), exDocumentoAct.getMob()))
				throw new AplicacaoException(
						"Não é permitido editar documento fechado");

			if (getPostback() == null) {
				escreverForm(exDocumentoAct);
				lerEntrevista(exDocumentoAct);
			}
		}

		if (exDocumentoAct.getTipoDocumento() != null && exDocumentoAct.getTipoDocumento().equals("externo")) {
			exDocumentoAct.setIdMod(((ExModelo) dao().consultarAtivoPorIdInicial(
					ExModelo.class, 28L)).getIdMod());
		}
		carregarBeans(exDocumentoAct);

		Long idSit = Ex
				.getInstance()
				.getConf()
				.buscaSituacao(exDocumentoAct.getModelo(), exDocumentoAct.getDoc().getExTipoDocumento(),
						getTitular(), getLotaTitular(),
						CpTipoConfiguracao.TIPO_CONFIG_ELETRONICO)
				.getIdSitConfiguracao();

		if (idSit == ExSituacaoConfiguracao.SITUACAO_OBRIGATORIO) {
			exDocumentoAct.setEletronico(1);
			exDocumentoAct.setEletronicoFixo(true);
		} else if (idSit == ExSituacaoConfiguracao.SITUACAO_PROIBIDO) {
			exDocumentoAct.setEletronico(2);
			exDocumentoAct.setEletronicoFixo(true);
		} else if (idSit == ExSituacaoConfiguracao.SITUACAO_DEFAULT
				&& (exDocumentoAct.getEletronico() == null || exDocumentoAct.getEletronico() == 0)) {
			exDocumentoAct.setEletronico(1);
		} else if (exDocumentoAct.isAlterouModelo()) {
			if (idSit == ExSituacaoConfiguracao.SITUACAO_DEFAULT) {
				exDocumentoAct.setEletronico(1);
			} else {
				exDocumentoAct.setEletronicoFixo(false);
				exDocumentoAct.setEletronico(0);
			}
		}

		lerForm(exDocumentoAct);

		// O (&& classif.getCodAssunto() != null) foi adicionado para permitir
		// que as classificações antigas, ainda não linkadas por equivalência,
		// possam ser usadas
		ExClassificacao classif = exDocumentoAct.getClassificacaoSel().buscarObjeto();
		if (classif != null && classif.getHisDtFim() != null
				&& classif.getHisDtIni() != null
				&& classif.getCodAssunto() != null) {
			classif = ExDao.getInstance().consultarAtual(classif);
			if (classif != null)
				exDocumentoAct.getClassificacaoSel().setId(classif.getIdClassificacao());
			else
				exDocumentoAct.getClassificacaoSel().setId(null);
		}

		exDocumentoAct.getSubscritorSel().buscar();
		exDocumentoAct.getDestinatarioSel().buscar();
		exDocumentoAct.getLotacaoDestinatarioSel().buscar();
		exDocumentoAct.getOrgaoSel().buscar();
		exDocumentoAct.getOrgaoExternoDestinatarioSel().buscar();
		exDocumentoAct.getClassificacaoSel().buscar();
		exDocumentoAct.getMobilPaiSel().buscar();

		if (getRequest().getSession().getAttribute("preenchRedirect") != null) {
			exDocumentoAct.setPreenchRedirect((String) getRequest().getSession().getAttribute(
					"preenchRedirect"));
			getRequest().getSession().removeAttribute("preenchRedirect");
		}

		registraErroExtEditor();

		// Usado pela extensão editor...
		getPar().put(
				"serverAndPort",
				new String[] { getRequest().getServerName()
						+ (getRequest().getServerPort() > 0 ? ":"
								+ getRequest().getServerPort() : "") });

		// ...inclusive nas operações com preenchimento automático
		if (exDocumentoAct.getPreenchRedirect() != null && exDocumentoAct.getPreenchRedirect().length() > 2) {
			exDocumentoAct.setPreenchRedirect(exDocumentoAct.getPreenchRedirect() + "&serverAndPort="+ getPar().get("serverAndPort")[0]);
		}
		
		result.include("tamanhoMaximoDescricao", exDocumentoAct.getTamanhoMaximoDescricao());
		result.include("doc", exDocumentoAct.getDoc());
		result.include("despachando", exDocumentoAct.getDespachando());
		result.include("autuando", exDocumentoAct.getAutuando());
		result.include("criandoAnexo", exDocumentoAct.isCriandoAnexo());
		result.include("idMobilAutuado", exDocumentoAct.getIdMobilAutuado());
		result.include("tiposDocumento", getTiposDocumento());
		result.include("idTpDoc", exDocumentoAct.getIdTpDoc());
		result.include("listaNivelAcesso", exDocumentoAct.getListaNivelAcesso());
		result.include("idNivelAcesso", null);
		result.include("eletronicoFixo", exDocumentoAct.isEletronicoFixo());
		result.include("eletronico", exDocumentoAct.getEletronico());
		result.include("eletronicoString", exDocumentoAct.getEletronicoString());
		result.include("tipoDocumento", exDocumentoAct.getTipoDocumento());
		result.include("dtDocString", exDocumentoAct.getDtDocString());
		result.include("numExtDoc", exDocumentoAct.getNumExtDoc());
		result.include("numAntigoDoc", exDocumentoAct.getNumAntigoDoc());
		result.include("dtDocOriginalString", exDocumentoAct.getDtDocOriginalString());
		result.include("obsOrgao", exDocumentoAct.getObsOrgao());
		result.include("nmSubscritorExt", exDocumentoAct.getNmSubscritorExt());
		result.include("nmFuncaoSubscritor", exDocumentoAct.getNmFuncaoSubscritor());
		result.include("nmOrgaoExterno", exDocumentoAct.getNmOrgaoExterno());
		result.include("nmDestinatario", exDocumentoAct.getNmDestinatario());
		result.include("desativarDocPai", exDocumentoAct.getDesativarDocPai());
		result.include("substituicao", exDocumentoAct.isSubstituicao());
		result.include("listaTipoDest", exDocumentoAct.getListaTipoDest());
		result.include("tipoDest", exDocumentoAct.getTipoDestinatario());
		result.include("formasDocPorTipo", exDocumentoAct.getFormasDocPorTipo());
		result.include("idFormaDoc", exDocumentoAct.getIdFormaDoc());
		result.include("possuiMaisQueUmModelo", (exDocumentoAct.getModelos().size() >1));
		result.include("modelos", exDocumentoAct.getModelos());
		result.include("idMod", exDocumentoAct.getIdMod());
		result.include("preenchimentos", exDocumentoAct.getPreenchimentos());
		result.include("idPreenchimento", null);
		result.include("modelo", exDocumentoAct.getModelo());
		result.include("classificacaoSel", exDocumentoAct.getClassificacaoSel());
		result.include("classificacaoIntermediaria",exDocumentoAct.isClassificacaoIntermediaria());
		result.include("descrClassifNovo",exDocumentoAct.getDescrClassifNovo());
		result.include("par",getPar());
		result.include("preenchRedirect",exDocumentoAct.getPreenchRedirect());
		return "edita";
	}

	public String aExcluir(ExDocumentoAction exDocumentoAct) throws Exception {
		ExDocumento documento = null;
		final String sId = getRequest().getParameter("id");

		try {
			ExDao.iniciarTransacao();
			documento = daoDoc(Long.valueOf(sId));

			verificaNivelAcesso(exDocumentoAct.getDoc().getMobilGeral());

			// Testa se existe algum valor preenchido em documento.
			// Se não houver gera ObjectNotFoundException
			final Date d = documento.getDtRegDoc();

			if (documento.isFinalizado())

				throw new AplicacaoException(
						"Documento já foi finalizado e não pode ser excluído");
			if (!Ex.getInstance().getComp()
					.podeExcluir(getTitular(), getLotaTitular(), exDocumentoAct.getMob()))
				throw new AplicacaoException("Não é possível excluir");

			// Exclui documento da tabela de Boletim Interno
			String funcao = exDocumentoAct.getDoc().getForm().get("acaoExcluir");
			if (funcao != null) {
				obterMetodoPorString(funcao, exDocumentoAct.getDoc());
			}

			for (ExMovimentacao movRef : exDocumentoAct.getMob().getExMovimentacaoReferenciaSet())
				Ex.getInstance().getBL().excluirMovimentacao(movRef);

			dao().excluir(documento);
			ExDao.commitTransacao();

		} catch (final ObjectNotFoundException e) {
			throw new AplicacaoException(
					"Documento já foi excluído anteriormente");
		} catch (final AplicacaoException e) {
			ExDao.rollbackTransacao();
			throw e;
		} catch (final Exception e) {
			ExDao.rollbackTransacao();
			throw new AplicacaoException("Ocorreu um Erro durante a Operação",
					0, e);
		}

		return Action.SUCCESS;
	}

	private void registraErroExtEditor() {

		try {
			if (param("desconsiderarExtensao") != null
					&& param("desconsiderarExtensao").equals("true")) {

				String nomeArquivo = getRequest().getRemoteHost();
				nomeArquivo = nomeArquivo.replaceAll(":", "_");

				BufferedWriter out = new BufferedWriter(new FileWriter(
						"./siga-ex-ext-editor-erro/" + nomeArquivo));
				out.close();
			}
		} catch (IOException e) {
			int a = 0;
		}
	}

	private void obterMetodoPorString(String metodo, ExDocumento doc)
			throws Exception {
		if (metodo != null) {
			final Class[] classes = new Class[] { ExDocumento.class };

			Method method;
			try {
				method = Ex.getInstance().getBL().getClass()
						.getDeclaredMethod(metodo, classes);
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
				return;
			}
			method.invoke(Ex.getInstance().getBL(), new Object[] { doc });
		}
	}

	public String aExcluirDocMovimentacoes(ExDocumentoAction exDocumentoAct) throws Exception {
		ExDocumento doc = exDocumentoAct.getDoc();
		buscarDocumento(true, exDocumentoAct);
		try {
			ExDao.iniciarTransacao();

			try {
				final Date d = doc.getDtRegDoc();
			} catch (final ObjectNotFoundException e) {
				throw new AplicacaoException(
						"Documento já foi excluído anteriormente", 1, e);
			}

			if (doc.isFinalizado())

				throw new AplicacaoException(
						"Documento já foi finalizado e não pode ser excluído",
						2);
			for (ExMobil m : doc.getExMobilSet()) {
				Set set = m.getExMovimentacaoSet();

				if (!Ex.getInstance().getComp()
						.podeExcluir(getTitular(), getLotaTitular(), m))
					throw new AplicacaoException("Não é possível excluir");

				if (set.size() > 0) {
					final Object[] aMovimentacao = set.toArray();
					for (int i = 0; i < set.size(); i++) {
						final ExMovimentacao movimentacao = (ExMovimentacao) aMovimentacao[i];
						dao().excluir(movimentacao);
					}
				}

				for (ExMarca marc : m.getExMarcaSet())
					dao().excluir(marc);

				set = m.getExMovimentacaoReferenciaSet();
				if (set.size() > 0) {
					final Object[] aMovimentacao = set.toArray();
					for (int i = 0; i < set.size(); i++) {
						final ExMovimentacao movimentacao = (ExMovimentacao) aMovimentacao[i];
						Ex.getInstance()
								.getBL()
								.excluirMovimentacao(getCadastrante(),
										getLotaTitular(),
										movimentacao.getExMobil(),
										movimentacao.getIdMov());
					}
				}

				dao().excluir(m);
			}

			// Exclui documento da tabela de Boletim Interno
			String funcao = doc.getForm().get("acaoExcluir");
			if (funcao != null) {
				obterMetodoPorString(funcao, doc);
			}

			dao().excluir(doc);
			ExDao.commitTransacao();
		} catch (final AplicacaoException e) {
			ExDao.rollbackTransacao();
			throw e;
		} catch (final Exception e) {
			ExDao.rollbackTransacao();
			throw new AplicacaoException("Ocorreu um Erro durante a Operação",
					0, e);
		}

		return Action.SUCCESS;
	}

	public String aExcluirPreenchimento(ExDocumentoAction exDocumentoAct) throws Exception {
		dao().iniciarTransacao();
		ExPreenchimento exemplo = dao().consultar(exDocumentoAct.getPreenchimento(),
				ExPreenchimento.class, false);
		dao().excluir(exemplo);
		// preenchDao.excluirPorId(preenchimento);
		dao().commitTransacao();
		exDocumentoAct.setPreenchimento(0L);
		return edita();
	}

	public String aTestarConexao() {
		return Action.SUCCESS;
	}

	public String aAcessar(ExDocumentoAction exDocumentoAct) throws Exception {
		buscarDocumento(false, exDocumentoAct);

		assertAcesso(exDocumentoAct);

		return Action.SUCCESS;
	}

	private void assertAcesso(ExDocumentoAction exDocumentoAct) throws Exception {			
		String msgDestinoDoc = "";
		DpPessoa dest;
		if(!Ex.getInstance().getComp()
				.podeAcessarDocumento(getTitular(), getLotaTitular(), exDocumentoAct.getMob())) {			
			
			String s = "";
			try { 
				s += exDocumentoAct.getMob().doc().getListaDeAcessosString();
				s = "(" + s + ")";
				s = " " + exDocumentoAct.getMob().doc().getExNivelAcesso().getNmNivelAcesso() + " " + s;
			} catch (Exception e) {
			}
			throw new AplicacaoException("Documento " + exDocumentoAct.getMob().getSigla()
					+ " inacessível ao usuário " + getTitular().getSigla()
					+ "/" + getLotaTitular().getSiglaCompleta() + "." + s + " " + msgDestinoDoc);
		}
	}

	public String aExibirAntigo(ExDocumentoAction exDocumentoAct) throws Exception {
		buscarDocumento(false, exDocumentoAct);
		
		assertAcesso(exDocumentoAct);

		if (Ex.getInstance().getComp()
				.podeReceberEletronico(getTitular(), getLotaTitular(), exDocumentoAct.getMob()))
			Ex.getInstance()
					.getBL()
					.receber(getCadastrante(), getLotaTitular(), exDocumentoAct.getMob(),
							new Date());

		ExDocumentoVO docVO = new ExDocumentoVO(exDocumentoAct.getDoc(), exDocumentoAct.getMob(), getTitular(),
				getLotaTitular(), true,true);
		super.getRequest().setAttribute("docVO", docVO);

		// logStatistics();

		if (exDocumentoAct.getMob().isEliminado())
			throw new AplicacaoException("Documento "
					+ exDocumentoAct.getMob().getSigla()
					+ " eliminado, conforme o termo "
					+ exDocumentoAct.getMob().getUltimaMovimentacaoNaoCancelada(
							ExTipoMovimentacao.TIPO_MOVIMENTACAO_ELIMINACAO)
							.getExMobilRef());

		return Action.SUCCESS;
	}
	
	@Get("/app/expediente/doc/exibir")
	public void aExibir(ExDocumentoAction exDocumentoAct) throws Exception {
		ExDocumento doc = exDocumentoAct.getDoc();
		ExMobil mob = exDocumentoAct.getMob();
		
		buscarDocumento(false, exDocumentoAct);

		assertAcesso(exDocumentoAct);

		if (Ex.getInstance().getComp()
				.podeReceberEletronico(getTitular(), getLotaTitular(), mob))
			Ex.getInstance()
					.getBL()
					.receber(getCadastrante(), getLotaTitular(), mob,
							new Date());
		
		if(mob == null || mob.isGeral()) {
			if(mob.getDoc().isFinalizado()) {
				if(doc.isProcesso())
					mob = doc.getUltimoVolume();
				else
					mob = doc.getPrimeiraVia();
			}
		}

		ExDocumentoVO docVO = new ExDocumentoVO(doc, mob, getTitular(),
				getLotaTitular(), true,false);
		
		
		docVO.exibe();
		
		//super.getRequest().setAttribute("docVO", docVO);
		result.include("docVO", docVO);
	}

	public String aCorrigirPDF(ExDocumentoAction exDocumentoAct) throws Exception {
		if (exDocumentoAct.getSigla() != null) {
			final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
			filter.setSigla(exDocumentoAct.getSigla());
			exDocumentoAct.setMob((ExMobil) dao().consultarPorSigla(filter));

			Ex.getInstance().getBL().processar(exDocumentoAct.getMob().getExDocumento(), true, false, null);
		}
		return Action.SUCCESS;
	}

	private void logStatistics() {
		Statistics stats = ExDao.getInstance().getSessao().getSessionFactory()
				.getStatistics();
		SessionStatistics statsSession = ExDao.getInstance().getSessao()
				.getStatistics();

		double queryCacheHitCount = stats.getQueryCacheHitCount();
		double queryCacheMissCount = stats.getQueryCacheMissCount();
		double queryCacheHitRatio = queryCacheHitCount
				/ (queryCacheHitCount + queryCacheMissCount);

		System.out.println("Query Hit ratio:" + queryCacheHitRatio);

		System.out.println(stats.getQueryExecutionMaxTimeQueryString()
				+ " [time (ms): " + stats.getQueryExecutionMaxTime() + "]");

		System.out
				.println("\n\n\n\n\n\n*****************************************\n\n\n\n\n\n\n");

		for (String query : stats.getQueries()) {
			QueryStatistics qs = stats.getQueryStatistics(query);
			System.out.println(query + " [time (ms): "
					+ qs.getExecutionAvgTime() + ", count: "
					+ qs.getExecutionCount() + "]");
		}

		for (String ent : stats.getEntityNames()) {
			EntityStatistics es = stats.getEntityStatistics(ent);
			System.out.println(ent + " [count: " + es.getFetchCount() + "]");
		}

		System.out
				.println("\n\n\n\n\n\n*****************************************\n\n\n\n\n\n\n");

	}

	private void verificaDocumento(ExDocumento doc) throws AplicacaoException, Exception {
		if ((doc.getSubscritor() == null && doc.getNmSubscritor() == null && doc
				.getNmSubscritorExt() == null)
				&& ((doc.getExFormaDocumento().getExTipoFormaDoc()
						.getIdTipoFormaDoc() == 2 && doc.isEletronico()) || doc
						.getExFormaDocumento().getExTipoFormaDoc()
						.getIdTipoFormaDoc() != 2))
			throw new AplicacaoException(
					"É necessário definir um subscritor para o documento.");

		if (doc.getDestinatario() == null
				&& doc.getLotaDestinatario() == null
				&& (doc.getNmDestinatario() == null || doc.getNmDestinatario()
						.trim().equals(""))
				&& doc.getOrgaoExternoDestinatario() == null
				&& (doc.getNmOrgaoExterno() == null || doc.getNmOrgaoExterno()
						.trim().equals(""))) {
			Long idSit = Ex
					.getInstance()
					.getConf()
					.buscaSituacao(doc.getExModelo(), getTitular(),
							getLotaTitular(),
							CpTipoConfiguracao.TIPO_CONFIG_DESTINATARIO)
					.getIdSitConfiguracao();
			if (idSit == ExSituacaoConfiguracao.SITUACAO_OBRIGATORIO)
				throw new AplicacaoException("Para documentos do modelo "
						+ doc.getExModelo().getNmMod()
						+ ", é necessário definir um destinatário");
		}

		if (doc.getExClassificacao() == null)
			throw new AplicacaoException(
					"É necessário informar a classificação documental.");

	}

	private void buscarDocumentoOuNovo(boolean fVerificarAcesso, ExDocumentoAction exDocumentoAct)throws Exception {
		ExDocumento doc = exDocumentoAct.getDoc();
		ExMobil mob = exDocumentoAct.getMob();
		buscarDocumento(fVerificarAcesso, true, exDocumentoAct);
		if (doc == null) {
			doc = new ExDocumento();
			doc.setExTipoDocumento(dao().consultar(
					ExTipoDocumento.TIPO_DOCUMENTO_INTERNO,
					ExTipoDocumento.class, false));
			mob = new ExMobil();
			mob.setExTipoMobil(dao().consultar(ExTipoMobil.TIPO_MOBIL_GERAL,
					ExTipoMobil.class, false));
			mob.setNumSequencia(1);
			mob.setExDocumento(doc);

			doc.setExMobilSet(new TreeSet<ExMobil>());
			doc.getExMobilSet().add(mob);
		}
	}

	private void buscarDocumento(boolean fVerificarAcesso, ExDocumentoAction exDocumentoAct) throws Exception {
		buscarDocumento(fVerificarAcesso, false, exDocumentoAct);
	}

	private void buscarDocumento(boolean fVerificarAcesso, boolean fPodeNaoExistir, ExDocumentoAction exDocumentoAct) throws Exception {
		ExDocumento doc = exDocumentoAct.getDoc();
		ExMobil mob = exDocumentoAct.getMob();
		String sigla = exDocumentoAct.getSigla();
		Long idMob = exDocumentoAct.getIdMob();
		if (mob == null && sigla != null && sigla.length() != 0) {
			final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
			filter.setSigla(sigla);
			mob = (ExMobil) dao().consultarPorSigla(filter);
			// bruno.lacerda@avantiprima.com.br
			if (mob != null) {
				doc = mob.getExDocumento();
				exDocumentoAct.setIdMob(mob.getId());
			}
		} else if (mob == null && exDocumentoAct.getDocumentoViaSel().getId() != null) {
			exDocumentoAct.setIdMob(exDocumentoAct.getDocumentoViaSel().getId());
			mob = dao().consultar(idMob, ExMobil.class, false);
		} else if (mob == null && idMob != null && idMob != 0) {
			mob = dao().consultar(idMob, ExMobil.class, false);
		}
		if (mob != null)
			doc = mob.doc();
		if (doc == null) {
			String id = param("id");
			if (id != null && id.length() != 0) {
				doc = daoDoc(Long.parseLong(id));
			}
		}
		if (doc != null && mob == null)
			mob = doc.getMobilGeral();

		if (!fPodeNaoExistir && doc == null)
			throw new AplicacaoException("Documento não informado");
		if (fVerificarAcesso && mob != null && mob.getIdMobil() != null)
			verificaNivelAcesso(mob);
	}

	public String aFinalizar(ExDocumentoAction exDocumentoAct) throws Exception {
		ExDocumento doc = exDocumentoAct.getDoc();
		ExMobil mob = exDocumentoAct.getMob();
		buscarDocumento(true, exDocumentoAct);

		verificaDocumento(doc);

		if (!Ex.getInstance().getComp()
				.podeFinalizar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível Finalizar");

		try {

			exDocumentoAct.setMsg(Ex.getInstance().getBL()
					.finalizar(getCadastrante(), getLotaTitular(), doc, null));

			if (doc.getForm() != null) {
				String funcao = doc.getForm().get("acaoFinalizar");
				if (funcao != null && funcao.trim().length() > 0) {
					obterMetodoPorString(funcao, doc);
				}
			}

		} catch (final Throwable t) {
			throw new AplicacaoException("Erro ao finalizar documento", 0, t);
		}

		return Action.SUCCESS;
	}

	public String aFinalizarAssinar(ExDocumentoAction exDocumentoAct) throws Exception {

		aFinalizar(exDocumentoAct);

		buscarDocumento(true, exDocumentoAct);

		return Action.SUCCESS;
	}

	public String aGravar(ExDocumentoAction exDocumentoAct) throws Exception {
		ExDocumento doc = exDocumentoAct.getDoc();

		try {
			buscarDocumentoOuNovo(true, exDocumentoAct);
			if (doc == null)
				doc = new ExDocumento();

			long tempoIni = System.currentTimeMillis();

			if (!validar()) {
		         edita();
		         getPar().put("alerta", new String[]{"Sim"});
		         exDocumentoAct.setAlerta("Sim");
		         return "form_incompleto";
			}

			ByteArrayOutputStream baos = null;

			lerForm(exDocumentoAct);

			if (!Ex.getInstance()
					.getConf()
					.podePorConfiguracao(getTitular(), getLotaTitular(),
							doc.getExTipoDocumento(),
							doc.getExFormaDocumento(), doc.getExModelo(),

							doc.getExClassificacaoAtual(),
							doc.getExNivelAcesso(),
							CpTipoConfiguracao.TIPO_CONFIG_CRIAR)) {

				if (!Ex.getInstance()
						.getConf()
						.podePorConfiguracao(getTitular(), getLotaTitular(),
								null, null, null, doc.getExClassificacao(),
								null, CpTipoConfiguracao.TIPO_CONFIG_CRIAR))
					throw new AplicacaoException(
							"Usuário não possui permissão de criar documento da classificação "
									+ doc.getExClassificacao().getCodificacao());

				throw new AplicacaoException("Operação não permitida");
			}

			System.out.println("monitorando gravacao IDDoc " + doc.getIdDoc()
					+ ", PESSOA " + doc.getCadastrante().getIdPessoa()
					+ ". Terminou verificacao de config PodeCriarModelo: "
					+ (System.currentTimeMillis() - tempoIni));
			tempoIni = System.currentTimeMillis();

			doc.setOrgaoUsuario(getLotaTitular().getOrgaoUsuario());

			if (exDocumentoAct.isClassificacaoIntermediaria()
					&& (exDocumentoAct.getDescrClassifNovo() == null || exDocumentoAct.getDescrClassifNovo().trim()
							.length() == 0))
				throw new AplicacaoException(
						"Quando a classificação selecionada não traz informação para criação de vias, o sistema exige que, antes de gravar o documento, seja informada uma sugestão de classificação para ser incluída na próxima revisão da tabela de classificações.");

			if (doc.getDescrDocumento().length() > exDocumentoAct.getTamanhoMaximoDescricao())
				throw new AplicacaoException(
						"O campo descrição possui mais do que "
								+ exDocumentoAct.getTamanhoMaximoDescricao() + " caracteres.");

			if (doc.isFinalizado()) {

				Date dt = dao().dt();
				Calendar c = Calendar.getInstance();
				c.setTime(dt);

				Calendar dtDocCalendar = Calendar.getInstance();

				if (doc.getDtDoc() == null)
					throw new Exception(
							"A data do documento deve ser informada.");

				dtDocCalendar.setTime(doc.getDtDoc());

				if (c.before(dtDocCalendar))
					throw new Exception(
							"Não é permitido criar documento com data futura");

				verificaDocumento(doc);
			}

			ExMobil mobilAutuado = null;
			if (exDocumentoAct.getIdMobilAutuado() != null) {

				mobilAutuado = dao().consultar(exDocumentoAct.getIdMobilAutuado(),
						ExMobil.class, false);

				doc.setExMobilAutuado(mobilAutuado);
			}

			Ex.getInstance().getBL()
					.gravar(getCadastrante(), getLotaTitular(), doc, null);

			lerEntrevista(exDocumentoAct);

			if (exDocumentoAct.getDesativarDocPai().equals("sim"))
				exDocumentoAct.setDesativ("&desativarDocPai=sim");

			try {

				Ex.getInstance()
						.getBL()
						.incluirCosignatariosAutomaticamente(getCadastrante(),
								getLotaTitular(), doc);

			} catch (Exception e) {

				throw new AplicacaoException(
						"Erro ao tentar incluir os cosignatários deste documento",
						0, e);

			}

		} catch (final Exception e) {
			throw new AplicacaoException("Erro na gravação", 0, e);
		} finally {
		}

		if (param("ajax") != null && param("ajax").equals("true"))
			return "ajax";
		else
			return Action.SUCCESS;
	}

	public String aGravarPreenchimento(ExDocumentoAction exDocumentoAct) throws Exception {
		dao().iniciarTransacao();
		ExPreenchimento exPreenchimento = new ExPreenchimento();

		DpLotacao provLota = new DpLotacao();
		provLota = getLotaTitular();
		ExModelo provMod = new ExModelo();
		provMod.setIdMod(exDocumentoAct.getIdMod());

		exPreenchimento.setDpLotacao(provLota);
		exPreenchimento.setExModelo(provMod);
		exPreenchimento.setNomePreenchimento(exDocumentoAct.getNomePreenchimento());

		exPreenchimento.setPreenchimentoBA(getByteArrayFormPreenchimento());
		dao().gravar(exPreenchimento);
		dao().commitTransacao();

		exDocumentoAct.setPreenchimento(exPreenchimento.getIdPreenchimento());

		return edita();

	}

	public String aPrever(ExDocumentoAction exDocumentoAct) throws Exception {
		buscarDocumentoOuNovo(true, exDocumentoAct);
		if (exDocumentoAct.getDoc() != null) {
			if (getPostback() == null) {
				escreverForm(exDocumentoAct);
			} else {

				lerForm(exDocumentoAct);
			}
		} else {
			exDocumentoAct.setDoc(new ExDocumento());
			lerForm(exDocumentoAct);
		}

		carregarBeans(exDocumentoAct);

		if (param("idMod") != null) {
			exDocumentoAct.setModelo(dao().consultar(paramLong("idMod"), ExModelo.class, false));
		}

		if (param("processar_modelo") != null)
			return "processa_modelo";
		return Action.SUCCESS;
	}

	public String aPreverPdf(ExDocumentoAction exDocumentoAct) throws Exception {
		buscarDocumentoOuNovo(true, exDocumentoAct);
		if (exDocumentoAct.getDoc() != null) {
			if (getPostback() == null) {
				escreverForm(exDocumentoAct);
			} else {
				lerForm(exDocumentoAct);
			}
		} else {
			exDocumentoAct.setDoc(new ExDocumento());
			lerForm(exDocumentoAct);
		}

		carregarBeans(exDocumentoAct);

		if (param("idMod") != null) {
			exDocumentoAct.setModelo(dao()
					.consultar(paramLong("idMod"), ExModelo.class, false));
		}

		Ex.getInstance().getBL().processar(exDocumentoAct.getDoc(), false, false, null);

		exDocumentoAct.setPdfStreamResult(new ByteArrayInputStream(exDocumentoAct.getDoc().getConteudoBlobPdf()));

		return Action.SUCCESS;
	}

	public String aRefazer(ExDocumentoAction exDocumentoAct) throws Exception {
		buscarDocumento(true, exDocumentoAct);

		if (!Ex.getInstance().getComp().podeRefazer(getTitular(), getLotaTitular(), exDocumentoAct.getMob()))
			throw new AplicacaoException("Não é possível refazer o documento");
		try {
			exDocumentoAct.setDoc(Ex.getInstance().getBL().refazer(getCadastrante(), getLotaTitular(), exDocumentoAct.getDoc()));
		} catch (final Exception e) {
			throw e;
		}
		return Action.SUCCESS;
	}

	public String aAtualizarMarcasDoc(ExDocumentoAction exDocumentoAct) throws Exception {

		buscarDocumento(false, exDocumentoAct);
		Ex.getInstance().getBL().atualizarMarcas(exDocumentoAct.getDoc());

		return Action.SUCCESS;
	}

	public String aTestarPdf() throws Exception {
		return Action.SUCCESS;
	}

	public String aTesteEnvioDJE() throws Exception {

		try {
			ExMovimentacao fakeMov = ExDao.getInstance().consultar(39468L,
					ExMovimentacao.class, false);
			ExDocumento doque = fakeMov.getExDocumento();
			GeradorRTF gerador = new GeradorRTF();
			String nomeArq = doque.getIdDoc().toString();
			fakeMov.setConteudoBlobRTF(nomeArq, gerador.geraRTF(doque));
			fakeMov.setConteudoBlobXML(nomeArq, PublicacaoDJEBL
					.gerarXMLPublicacao(fakeMov, "A", "SESIA",
							"Teste descrição"));
			fakeMov.setNmArqMov(nomeArq + ".zip");

			PublicacaoDJEBL.primeiroEnvio(fakeMov);
			return Action.SUCCESS;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String aCorrigirArquivamentosVolume() throws Exception {
		int idPrimeiroDoc, idUltimoDoc;
		boolean efetivar;
		try {
			idPrimeiroDoc = Integer.valueOf(param("de"));
		} catch (Exception e) {
			idPrimeiroDoc = 1;
		}
		try {
			idUltimoDoc = Integer.valueOf(param("ate"));
		} catch (Exception e) {
			idUltimoDoc = 999999999;
		}
		try {
			efetivar = Boolean.parseBoolean(param("efetivar"));
		} catch (Exception e) {
			efetivar = false;
		}
		Ex.getInstance()
				.getBL()
				.corrigirArquivamentosEmVolume(idPrimeiroDoc, idUltimoDoc,
						efetivar);
		return Action.SUCCESS;
	}

	public String aTesteExclusaoDJE() throws Exception {
		ExMovimentacao fakeMov = ExDao.getInstance().consultar(37644L,
				ExMovimentacao.class, false);
		PublicacaoDJEBL.cancelarRemessaPublicacao(fakeMov);
		return null;
	}

	public String aMassaTesteDJE2() throws Exception {
		DpPessoa edson = new DpPessoa();
		edson.setSigla("RJ13285");
		edson = CpDao.getInstance().consultar(edson, null).get(0);

		ExDao exDao = ExDao.getInstance();
		StringBuffer appender = new StringBuffer(
				"from ExDocumento doc where (doc.exModelo.hisIdIni in (73, 76) and doc.exModelo.hisAtivo = 1) and doc.dtFechamento between :start and :end");
		Query query = exDao.getSessao().createQuery(appender.toString());
		Calendar cal = new GregorianCalendar();
		cal.set(2008, 07, 14);
		query.setDate("start", cal.getTime());
		cal.set(2008, 07, 18);
		query.setDate("end", cal.getTime());

		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, 2);

		List<ExDocumento> doques = query.list();
		for (ExDocumento ex : doques) { /*
										 * ExDocumentoBL.remeterParaPublicacao(edson
										 * , edson.getLotacao(), ex, null, new
										 * Date(), edson, edson,
										 * edson.getLotacao(), cal .getTime());
										 */
			/*
			 * ExMovimentacao fakeMov = new ExMovimentacao(); GeradorRTF gerador
			 * = new GeradorRTF(); String nomeArq = "I-" + doc.getIdDoc();
			 * fakeMov.setConteudoBlobRTF(nomeArq, gerador.geraRTF(doc));
			 * fakeMov.setConteudoBlobXML(nomeArq, PublicacaoDJEBL
			 * .gerarXMLPublicacao(fakeMov)); fakeMov.setNmArqMov(nomeArq +
			 * ".zip");
			 * 
			 * Long numTRF = PublicacaoDJEBL
			 * .verificaPrimeiroRetornoPublicacao(fakeMov); /* final Compactador
			 * zip = new Compactador(); final byte[] arqZip =
			 * getConteudoBlobMov2(); byte[] conteudoZip = null; conteudoZip =
			 * zip.adicionarStream(nome, conteudo, arqZip);
			 * setConteudoBlobMov2(conteudoZip);
			 */

		}
		return Action.SUCCESS;
	}

	public String aDuplicar(ExDocumentoAction exDocumentoAct) throws Exception {
		buscarDocumento(false, exDocumentoAct);
		if (!Ex.getInstance().getComp()
				.podeDuplicar(getTitular(), getLotaTitular(), exDocumentoAct.getMob()))
			throw new AplicacaoException("Não é possível duplicar o documento");
		try {
			exDocumentoAct.setDoc(Ex.getInstance().getBL().duplicar(getCadastrante(), getLotaTitular(), exDocumentoAct.getDoc()));
		} catch (final Exception e) {
			throw e;
		}
		return Action.SUCCESS;
	}

	private void atualizaLotacoesPreenchimentos(DpLotacao lotaTitular)
			throws AplicacaoException {
		if (lotaTitular.getIdLotacao().longValue() != lotaTitular
				.getIdLotacaoIni().longValue()) {
			ExPreenchimento exp = new ExPreenchimento();
			exp.setDpLotacao(daoLot(lotaTitular.getIdLotacaoIni()));
			for (ExPreenchimento exp2 : dao().consultar(exp)) {
				dao().iniciarTransacao();
				exp2.setDpLotacao(lotaTitular);
				dao().gravar(exp2);
				dao().commitTransacao();
			}
		}
	}

	public String aDesfazerCancelamentoDocumento(ExDocumentoAction exDocumentoAct) throws Exception {
		buscarDocumento(true, exDocumentoAct);
		if (!Ex.getInstance()
				.getComp()
				.podeDesfazerCancelamentoDocumento(getTitular(),
						getLotaTitular(), exDocumentoAct.getMob()))
			throw new AplicacaoException(
					"Não é possível desfazer o cancelamento deste documento");
		try {
			Ex.getInstance()
					.getBL()
					.DesfazerCancelamentoDocumento(getCadastrante(),
							getLotaTitular(), exDocumentoAct.getDoc());
		} catch (final Exception e) {
			throw e;
		}
		return Action.SUCCESS;
	}

	public String aTornarDocumentoSemEfeito(ExDocumentoAction exDocumentoAct) throws Exception {
		buscarDocumento(true, exDocumentoAct);
		return Action.SUCCESS;
	}

	public String aTornarDocumentoSemEfeitoGravar(ExDocumentoAction exDocumentoAct) throws Exception {
		if (exDocumentoAct.getDescrMov() == null || exDocumentoAct.getDescrMov().trim().length() == 0) {
			throw new AplicacaoException(
					"O preenchimento do campo MOTIVO é obrigatório!");
		}
		buscarDocumento(true, exDocumentoAct);
		if (!Ex.getInstance()
				.getComp()
				.podeTornarDocumentoSemEfeito(getTitular(), getLotaTitular(),exDocumentoAct.getMob()))
			throw new AplicacaoException(
					"Não é possível tornar documento sem efeito.");
		try {
			Ex.getInstance()
					.getBL()
					.TornarDocumentoSemEfeito(getCadastrante(),
							getLotaTitular(), exDocumentoAct.getDoc(), exDocumentoAct.getDescrMov());
		} catch (final Exception e) {
			throw e;
		}
		return Action.SUCCESS;
	}
	
	public String acriarDocTest() throws Exception {
		try {
			setMensagem(Ex.getInstance()
					.getBL()
					.criarDocTeste());
		} catch (final Exception e) {
			throw e;
		}
		
		return Action.SUCCESS;
	}

	private void carregarBeans(ExDocumentoAction exDocumentoAct) throws Exception {
		ExMobil mobPai = null;

		exDocumentoAct.getDoc().setExTipoDocumento(dao().consultar(exDocumentoAct.getIdTpDoc(), ExTipoDocumento.class,
				false));

		// Questões referentes a doc pai-----------------------------

		if (exDocumentoAct.getDoc().getIdDoc() == null) {
			String req = "nao";
			if (getPar().get("reqdocumentoRefSel") != null)
				req = getPar().get("reqmobilPaiSel")[0].toString();

			if (param("mobilPaiSel.sigla") != null)
				exDocumentoAct.getMobilPaiSel().setSigla(param("mobilPaiSel.sigla"));
			exDocumentoAct.getMobilPaiSel().buscar();
			if ((exDocumentoAct.getMobilPaiSel().getId() != null) || (req.equals("sim"))) {

				if (exDocumentoAct.getMobilPaiSel().getId() != null) {
					// Documento Pai
					mobPai = daoMob(exDocumentoAct.getMobilPaiSel().getId());

					Integer idForma = paramInteger("idForma");
					if (idForma != null)
						exDocumentoAct.setIdFormaDoc(idForma);

					if (exDocumentoAct.getClassificacaoSel() != null
							&& exDocumentoAct.getClassificacaoSel().getId() == null)
						exDocumentoAct.getClassificacaoSel().setId(
								mobPai.doc().getExClassificacaoAtual().getId());

					exDocumentoAct.setDescrDocumento(mobPai.doc().getDescrDocumento());

					exDocumentoAct.setDesativarDocPai("sim");
				}

			}

			if (exDocumentoAct.getAutuando() && exDocumentoAct.getIdMobilAutuado() != null) {
				ExMobil mobilAutuado = daoMob(exDocumentoAct.getIdMobilAutuado());

				exDocumentoAct.getDoc().setExMobilAutuado(mobilAutuado);

				exDocumentoAct.getClassificacaoSel().setId(mobilAutuado.getDoc().getExClassificacao().getId());
				exDocumentoAct.setDescrDocumento(mobilAutuado.getDoc().getDescrDocumento());
			}
		}

		// Fim das questões referentes a doc pai--------------------

		Integer idFormaDoc = exDocumentoAct.getIdFormaDoc();
		if (idFormaDoc != null) {
			if (idFormaDoc == 0) {
				exDocumentoAct.setIdMod(0L);
			} else {

				// Mudou origem? Escolhe um tipo automaticamente--------
				// Vê se usuário alterou campo Origem. Caso sim, seleciona um
				// tipo
				// automaticamente, dentro daquela origem

				final List<ExFormaDocumento> formasDoc = exDocumentoAct.getFormasDocPorTipo();

				ExFormaDocumento forma = dao().consultar(exDocumentoAct.getIdFormaDoc(),
						ExFormaDocumento.class, false);

				if (!formasDoc.contains(forma)) {
					exDocumentoAct.setIdFormaDoc(exDocumentoAct.getFormaDocPorTipo().getIdFormaDoc());
					forma = dao().consultar(exDocumentoAct.getIdFormaDoc(),
							ExFormaDocumento.class, false);
				}

				// Fim -- Mudou origem? Escolhe um tipo automaticamente--------

				if (forma.getExModeloSet().size() == 0) {
					exDocumentoAct.setIdMod(0L);
				}
			}
		}

		ExModelo mod = null;
		if (exDocumentoAct.getIdMod() != null && exDocumentoAct.getIdMod() != 0) {
			mod = dao().consultar(exDocumentoAct.getIdMod(), ExModelo.class, false);
		}
		if (mod != null) {
			mod = mod.getModeloAtual();
		}

		List<ExModelo> modelos = exDocumentoAct.getModelos();
		if (mod == null || !modelos.contains(mod)) {
			mod = (ExModelo) (modelos.toArray())[0];

			for (ExModelo modeloAtual : modelos) {
				if (modeloAtual.getIdMod() != null
						&& modeloAtual.getIdMod() != 0
						&& modeloAtual.getNmMod().equals(
								modeloAtual.getExFormaDocumento()
										.getDescricao())) {
					mod = modeloAtual;
					break;
				}
			}

			exDocumentoAct.setIdMod(mod.getIdMod());
			if ((exDocumentoAct.getIdMod() != 0) && (exDocumentoAct.getMobilPaiSel().getId() == null)
					&& (exDocumentoAct.getIdMobilAutuado() == null))
				exDocumentoAct.getClassificacaoSel().apagar();
		}

		if (exDocumentoAct.getPreenchimentos().size() <= 1) {
			exDocumentoAct.setPreenchimento(0L);
		}

		if (exDocumentoAct.isAlterouModelo() && exDocumentoAct.getMobilPaiSel().getId() == null
				&& exDocumentoAct.getIdMobilAutuado() == null)
			exDocumentoAct.getClassificacaoSel().apagar();

		boolean naLista = false;
		final Set<ExPreenchimento> preenchimentos = exDocumentoAct.getPreenchimentos();
		if (preenchimentos != null && preenchimentos.size() > 0) {
			for (ExPreenchimento exp : preenchimentos) {
				if (exp.getIdPreenchimento().equals(exDocumentoAct.getPreenchimento())) {
					naLista = true;
					break;
				}
			}
			if (!naLista)
				exDocumentoAct.setPreenchimento(((ExPreenchimento) (preenchimentos.toArray())[0])
						.getIdPreenchimento());
		}

		exDocumentoAct.setModelo(mod);
		if (mod.getExClassificacao() != null
				&& mod.getExClassificacao().getId() != exDocumentoAct.getClassificacaoSel()
						.getId()) {
			exDocumentoAct.getClassificacaoSel().buscarPorObjeto(mod.getExClassificacao());
		}

	}

	private void escreverForm(ExDocumentoAction exDocumentoAct) throws IllegalAccessException,
			NoSuchMethodException, AplicacaoException,
			InvocationTargetException {
		ExDocumento doc = exDocumentoAct.getDoc();
		
		// Destino , Origem
		DpLotacao backupLotaTitular = getLotaTitular();
		DpPessoa backupTitular = getTitular();
		DpPessoa backupCadastrante = getCadastrante();

		BeanUtils.copyProperties(this, doc);

		setTitular(backupTitular);
		setLotaTitular(backupLotaTitular);
		// Orlando: Inclusão da linha, abaixo, para preservar o cadastrante do
		// ambiente.
		setCadastrante(backupCadastrante);

		if (doc.getConteudoBlob("doc.htm") != null)
			exDocumentoAct.setConteudo(new String(doc.getConteudoBlob("doc.htm")));

		exDocumentoAct.setIdTpDoc(doc.getExTipoDocumento().getIdTpDoc());
		exDocumentoAct.setNivelAcesso(doc.getIdExNivelAcesso());

		if (doc.getExFormaDocumento() != null) {
			exDocumentoAct.setIdFormaDoc(doc.getExFormaDocumento().getIdFormaDoc());
		}

		ExClassificacao classif = doc.getExClassificacaoAtual();
		if (classif != null)
			exDocumentoAct.getClassificacaoSel().buscarPorObjeto(classif.getAtual());
		exDocumentoAct.getSubscritorSel().buscarPorObjeto(doc.getSubscritor());
		// form.getDestinatarioSel().buscarPorObjeto(doc.getDestinatario());
		if (doc.getExModelo() != null) {
			ExModelo modeloAtual = doc.getExModelo().getModeloAtual();
			if (modeloAtual != null) {
				exDocumentoAct.setIdMod(modeloAtual.getIdMod());	
			}	
		}
			
		if (doc.getDestinatario() != null) {
			exDocumentoAct.getDestinatarioSel().buscarPorObjeto(doc.getDestinatario());
			exDocumentoAct.setTipoDestinatario(1);
		}
		if (doc.getLotaDestinatario() != null) {
			exDocumentoAct.getLotacaoDestinatarioSel().buscarPorObjeto(
					doc.getLotaDestinatario());
			if (doc.getDestinatario() == null)
				exDocumentoAct.setTipoDestinatario(2);
		}

		if (doc.getExMobilPai() != null) {
			exDocumentoAct.getMobilPaiSel().buscarPorObjeto(doc.getExMobilPai());
		}

		if (doc.getTitular() != null
				&& doc.getSubscritor() != null
				&& !doc.getTitular().getIdPessoa()
						.equals(doc.getSubscritor().getIdPessoa())) {
			exDocumentoAct.getTitularSel().buscarPorObjeto(doc.getTitular());
			exDocumentoAct.setSubstituicao(true);
		}

		// TODO Verificar se ha realmente a necessidade de setar novamente o
		// nível de acesso do documento
		// tendo em vista que o nível de acesso já foi setado anteriormente
		// neste mesmo método sem que o documento fosse alterado
		exDocumentoAct.setNivelAcesso(doc.getIdExNivelAcesso());

		if (doc.getOrgaoExternoDestinatario() != null) {
			exDocumentoAct.getOrgaoExternoDestinatarioSel().buscarPorObjeto(
					doc.getOrgaoExternoDestinatario());
			exDocumentoAct.setTipoDestinatario(3);
		}
		if (doc.getNmOrgaoExterno() != null
				&& !doc.getNmOrgaoExterno().equals("")) {
			exDocumentoAct.setTipoDestinatario(3);
		}
		if (doc.getNmDestinatario() != null) {
			exDocumentoAct.setNmDestinatario(doc.getNmDestinatario());
			exDocumentoAct.setTipoDestinatario(4);
		}

		if (doc.getOrgaoExterno() != null) {
			exDocumentoAct.getCpOrgaoSel().buscarPorObjeto(doc.getOrgaoExterno());
			exDocumentoAct.setIdTpDoc(3L);
		}

		if (doc.getObsOrgao() != null) {
			exDocumentoAct.setObsOrgao(doc.getObsOrgao());
		}

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			exDocumentoAct.setDtDocString(df.format(doc.getDtDoc()));
		} catch (final Exception e) {
		}

		try {
			exDocumentoAct.setDtDocOriginalString(df.format(doc.getDtDocOriginal()));
		} catch (final Exception e) {
		}

		if (doc.getAnoEmissao() != null)
			exDocumentoAct.setAnoEmissaoString(doc.getAnoEmissao().toString());

		exDocumentoAct.setEletronico(doc.isEletronico() ? 1 : 2);

	}


	private byte[] getByteArrayFormPreenchimento() throws Exception {
		ByteArrayOutputStream baos = null;
		String[] aVars = getPar().get("vars");
		String[] aCampos = getPar().get("campos");
		ArrayList<String> aFinal = new ArrayList<String>();
		if (aVars != null && aVars.length > 0)
			for (String str : aVars) {
				aFinal.add(str);
			}
		if (aCampos != null && aCampos.length > 0)
			for (String str : aCampos) {
				aFinal.add(str);
			}
		if (aFinal != null && aFinal.size() > 0) {
			baos = new ByteArrayOutputStream();
			for (final String s : aFinal) {
				if (param(s) != null && !param(s).trim().equals("")
						&& !s.trim().equals("preenchimento")
						&& !param(s).matches("[0-9]{2}/[0-9]{2}/[0-9]{4}")/*
																		 * s.trim
																		 * (
																		 * ).equals
																		 * (
																		 * "dtDocString"
																		 * )
																		 */) {
					if (baos.size() > 0)
						baos.write('&');
					baos.write(s.getBytes());
					baos.write('=');

					// Deveria estar gravando como UTF-8
					baos.write(URLEncoder.encode(param(s), "iso-8859-1")
							.getBytes());
				}
			}
		}
		return baos.toByteArray();
	}


	private void lerEntrevista(ExDocumentoAction exDocumentoAct) {
		final ExDocumento doc = exDocumentoAct.getDoc();
		if (doc.getExModelo() != null) {
			final byte[] form = doc.getConteudoBlob("doc.form");
			if (form != null) {
				final String as[] = new String(form).split("&");
				for (final String s : as) {
					final String param[] = s.split("=");
					try {
						if (param.length == 2)
							exDocumentoAct.getParamsEntrevista().put(param[0],URLDecoder.decode(param[1], "iso-8859-1"));
						// setParam(param[0], URLDecoder.decode(param[1],
						// "iso-8859-1"));
					} catch (final UnsupportedEncodingException e) {
					}
				}
			}
		}
	}

	private void lerForm(ExDocumentoAction exDocumentoAct) throws IllegalAccessException,
			NoSuchMethodException, AplicacaoException {

		ExDocumento doc = exDocumentoAct.getDoc();
		if (exDocumentoAct.getAnexar()) {
			doc.setConteudoTpDoc(exDocumentoAct.getConteudoTpDoc());
			doc.setNmArqDoc(exDocumentoAct.getNmArqDoc());
		}

		// BeanUtils.copyProperties(doc, form);
		// fabrica = DaoFactory.getDAOFactory();
		doc.setDescrDocumento(exDocumentoAct.getDescrDocumento());
		doc.setNmSubscritorExt(exDocumentoAct.getNmSubscritorExt());
		doc.setNmFuncaoSubscritor(exDocumentoAct.getNmFuncaoSubscritor());
		doc.setNumExtDoc(exDocumentoAct.getNumExtDoc());
		doc.setNumAntigoDoc(exDocumentoAct.getNumAntigoDoc());
		doc.setObsOrgao(exDocumentoAct.getObsOrgao());
		doc.setEletronico(exDocumentoAct.getEletronico() == 1 ? true : false);
		doc.setNmOrgaoExterno(exDocumentoAct.getNmOrgaoExterno());
		doc.setDescrClassifNovo(exDocumentoAct.getDescrClassifNovo());

		doc.setExNivelAcesso(dao().consultar(exDocumentoAct.getNivelAcesso(),
				ExNivelAcesso.class, false));

		doc.setExTipoDocumento(dao().consultar(exDocumentoAct.getIdTpDoc(),
				ExTipoDocumento.class, false));

		if (!doc.isFinalizado())
			doc.setExFormaDocumento(dao().consultar(exDocumentoAct.getIdFormaDoc(),
					ExFormaDocumento.class, false));
		doc.setNmDestinatario(exDocumentoAct.getNmDestinatario());
		
		doc.setExModelo(null);
		if (exDocumentoAct.getIdMod() != 0) {
			ExModelo modelo = dao().consultar(exDocumentoAct.getIdMod(), ExModelo.class, false);
			if (modelo != null) 								
				doc.setExModelo(modelo.getModeloAtual());			
		}

		if (exDocumentoAct.getClassificacaoSel().getId() != null
				&& exDocumentoAct.getClassificacaoSel().getId() != 0) {

			ExClassificacao classificacao = dao().consultar(exDocumentoAct.getClassificacaoSel().getId(), ExClassificacao.class,false);

			if (classificacao != null && !classificacao.isFechada())
				doc.setExClassificacao(classificacao);
			else {
				doc.setExClassificacao(null);
				exDocumentoAct.getClassificacaoSel().apagar();
			}

		} else
			doc.setExClassificacao(null);
		if (exDocumentoAct.getCpOrgaoSel().getId() != null) {
			doc.setOrgaoExterno(dao().consultar(exDocumentoAct.getCpOrgaoSel().getId(), CpOrgao.class, false));
		} else
			doc.setOrgaoExterno(null);

		// Orlando: Alterei o IF abaixo incluindo a instrução
		// "doc.setLotaCadastrante(getLotaTitular());".
		// Esta linha estava "solta",após o IF, e era executada sempre.
		// Fiz esta modificação porque esta linha alterava a lotação do
		// cadastrante, não permitindo que este,
		// ao preencher o campo subscritor com a matrícula de outro usuário,
		// tivesse acesso ao documento.

		if (doc.getCadastrante() == null) {
			doc.setCadastrante(getCadastrante());
			doc.setLotaCadastrante(getLotaTitular());
		}

		if (doc.getLotaCadastrante() == null)
			doc.setLotaCadastrante(doc.getCadastrante().getLotacao());
		if (exDocumentoAct.getSubscritorSel().getId() != null) {
			doc.setSubscritor(daoPes(exDocumentoAct.getSubscritorSel().getId()));
			doc.setLotaSubscritor(doc.getSubscritor().getLotacao());
		} else {
			doc.setSubscritor(null);
		}

		if (exDocumentoAct.isSubstituicao()) {
			if (exDocumentoAct.getTitularSel().getId() != null) {
				doc.setTitular(daoPes(exDocumentoAct.getTitularSel().getId()));
				doc.setLotaTitular(doc.getTitular().getLotacao());
			} else {
				doc.setTitular(doc.getSubscritor());
				doc.setLotaTitular(doc.getLotaSubscritor());
			}
		} else {
			doc.setTitular(doc.getSubscritor());
			doc.setLotaTitular(doc.getLotaSubscritor());
		}

		if (exDocumentoAct.getDestinatarioSel().getId() != null) {
			doc.setDestinatario(daoPes(exDocumentoAct.getDestinatarioSel().getId()));
			doc.setLotaDestinatario(daoPes(exDocumentoAct.getDestinatarioSel().getId()).getLotacao());
			doc.setOrgaoExternoDestinatario(null);
		} else {
			doc.setDestinatario(null);
			if (exDocumentoAct.getLotacaoDestinatarioSel().getId() != null) {
				doc.setLotaDestinatario(daoLot(exDocumentoAct.getLotacaoDestinatarioSel()
						.getId()));
				doc.setOrgaoExternoDestinatario(null);
			} else {
				doc.setLotaDestinatario(null);
				if (exDocumentoAct.getOrgaoExternoDestinatarioSel().getId() != null) {
					doc.setOrgaoExternoDestinatario(dao().consultar(
							exDocumentoAct.getOrgaoExternoDestinatarioSel().getId(),
							CpOrgao.class, false));
				} else {
					doc.setOrgaoExternoDestinatario(null);
				}
			}
		}

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			doc.setDtDoc(df.parse(exDocumentoAct.getDtDocString()));
		} catch (final ParseException e) {
			doc.setDtDoc(null);
		} catch (final NullPointerException e) {
			doc.setDtDoc(null);
		}
		if (doc.getDtRegDoc() == null)
			doc.setDtRegDoc(dao().dt());

		try {
			doc.setDtDocOriginal(df.parse(exDocumentoAct.getDtDocOriginalString()));
		} catch (final ParseException e) {
			doc.setDtDocOriginal(null);
		} catch (final NullPointerException e) {
			doc.setDtDocOriginal(null);
		}

		if (exDocumentoAct.getNumExpediente() != null) {
			doc.setNumExpediente(new Long(exDocumentoAct.getNumExpediente()));
			doc.setAnoEmissao(new Long(exDocumentoAct.getAnoEmissaoString()));
		}

		if (exDocumentoAct.getMobilPaiSel().getId() != null) {
			doc.setExMobilPai(dao().consultar(exDocumentoAct.getMobilPaiSel().getId(), ExMobil.class, false));
		} else {
			doc.setExMobilPai(null);
		}

		try {
			ByteArrayOutputStream baos;

			final String marcacoes[] = { "<!-- INICIO NUMERO -->",
					"<!-- FIM NUMERO -->", "<!-- INICIO NUMERO",
					"FIM NUMERO -->", "<!-- INICIO TITULO", "FIM TITULO -->",
					"<!-- INICIO MIOLO -->", "<!-- FIM MIOLO -->",
					"<!-- INICIO CORPO -->", "<!-- FIM CORPO -->",
					"<!-- INICIO CORPO", "FIM CORPO -->",
					"<!-- INICIO ASSINATURA -->", "<!-- FIM ASSINATURA -->",
					"<!-- INICIO ABERTURA -->", "<!-- FIM ABERTURA -->",
					"<!-- INICIO ABERTURA", "FIM ABERTURA -->",
					"<!-- INICIO FECHO -->", "<!-- FIM FECHO -->" };

			final String as[] = getPar().get("vars");
			if (as != null) {
				baos = new ByteArrayOutputStream();
				for (final String s : as) {
					if (baos.size() > 0)
						baos.write('&');
					baos.write(s.getBytes());
					baos.write('=');
					if (param(s) != null) {
						String parametro = param(s);
						for (final String m : marcacoes) {
							if (parametro.contains(m))
								parametro = parametro.replaceAll(m, "");
						}
						if (!FuncoesEL.contemTagHTML(parametro)) {
							if (parametro.contains("\"")) {
								parametro = parametro.replace("\"", "&quot;");
								setParam(s, parametro);
							}
						}

						baos.write(URLEncoder.encode(parametro, "iso-8859-1")
								.getBytes());
					}
				}
				doc.setConteudoTpDoc("application/zip");
				doc.setConteudoBlobForm(baos.toByteArray());

			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Retorna o conteúdo do arquivo em um array de Byte
	public byte[] toByteArray(final File file) throws IOException {

		final InputStream is = new FileInputStream(file);

		// Get the size of the file
		final long tamanho = file.length();

		// Não podemos criar um array usando o tipo long.
		// é necessário usar o tipo int.
		if (tamanho > Integer.MAX_VALUE)
			throw new IOException("Arquivo muito grande");

		// Create the byte array to hold the data
		final byte[] meuByteArray = new byte[(int) tamanho];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < meuByteArray.length
				&& (numRead = is.read(meuByteArray, offset, meuByteArray.length
						- offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < meuByteArray.length)
			throw new IOException(
					"Não foi possível ler o arquivo completamente "
							+ file.getName());

		// Close the input stream and return bytes
		is.close();
		return meuByteArray;
	}

}
