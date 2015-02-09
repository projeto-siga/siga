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
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.QueryStatistics;
import org.hibernate.stat.SessionStatistics;
import org.hibernate.stat.Statistics;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.http.VRaptorResponse;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.com.caelum.vraptor.view.Results;
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
import br.gov.jfrj.siga.ex.bl.CurrentRequest;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;
import br.gov.jfrj.siga.ex.bl.RequestInfo;
import br.gov.jfrj.siga.ex.bl.BIE.HierarquizadorBoletimInterno;
import br.gov.jfrj.siga.ex.util.FuncoesEL;
import br.gov.jfrj.siga.ex.util.GeradorRTF;
import br.gov.jfrj.siga.ex.util.PublicacaoDJEBL;
import br.gov.jfrj.siga.ex.vo.ExDocumentoVO;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.libs.webwork.Selecao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

@Resource
public class ExDocumentoController extends ExController {
	
	private static final String URL_EXIBIR = "/app/expediente/doc/exibir?sigla={0}";
	
	
	public ExDocumentoController(HttpServletRequest request, HttpServletResponse response, ServletContext context, Result result, SigaObjects so, EntityManager em) {
		super(request, response, context, result, CpDao.getInstance(), so, em);
		CurrentRequest.set(new RequestInfo(context, request, response));
		
		result.on(AplicacaoException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();	
	}

	@Post("app/expediente/doc/alterarpreench")
	public void aAlterarPreenchimento(ExDocumentoDTO exDocumentoDTO, String[] vars, String[] campos) throws Exception {
		ExPreenchimento exPreenchimento = new ExPreenchimento();
		
		dao().iniciarTransacao();
		exPreenchimento.setIdPreenchimento(exDocumentoDTO.getPreenchimento());
		exPreenchimento = dao().consultar(exDocumentoDTO.getPreenchimento(), ExPreenchimento.class, false);
		
		exPreenchimento.setPreenchimentoBA(getByteArrayFormPreenchimento(vars, campos));
		dao().gravar(exPreenchimento);
		dao().commitTransacao();
		
		exDocumentoDTO.setPreenchimento(exPreenchimento.getIdPreenchimento());
		
		String url = getUrlEncodedParameters();
		if (url.indexOf("preenchimento") >= 0) {
			String parte1 = url.substring(0, url.indexOf("preenchimento"));
			String parte2 = url.substring(url.indexOf("&", url.indexOf("&preenchimento") + 1) + 1);
			parte2 = parte2 + "&preenchimento=" + exDocumentoDTO.getPreenchimento();
			exDocumentoDTO.setPreenchRedirect(parte1 + parte2);
		} else
			exDocumentoDTO.setPreenchRedirect(getUrlEncodedParameters());
		
		result.forwardTo(this).aCarregarPreenchimento(exDocumentoDTO, vars);
	}
	
	public String aAnexo(ExDocumentoDTO exDocumentoDTO) throws Exception {
		buscarDocumento(true, exDocumentoDTO);
		return "success";
	}
	
	public String aCancelarDocumento(ExDocumentoDTO exDocumentoDTO, String sigla) throws Exception {
		if (sigla != null){
			exDocumentoDTO.setSigla(sigla);
		}
		buscarDocumento(true, exDocumentoDTO);
		
		try {
			Ex.getInstance().getBL().cancelarDocumento(getCadastrante(), getLotaTitular(), exDocumentoDTO.getDoc());
		} catch (final Exception e) {
			throw e;
		}
		return "success";
		
	}
	
	// Converte encode de url de iso-8859-1 para utf-8
	private String ConverteEncodeDeUriDeIsoParaUtf(String sUri) {
		// return sUri;
		StringBuilder sb = new StringBuilder();
		String aParametros[] = sUri.split("&");
		for (int i = 0; i < aParametros.length; i++) {
			String aTupla[] = aParametros[i].split("=");
			try {
				sb.append(URLEncoder.encode(URLDecoder.decode(aTupla[0], "iso-8859-1"), "utf-8"));
				sb.append("=");
				sb.append(URLEncoder.encode(URLDecoder.decode(aTupla[1], "iso-8859-1"), "utf-8"));
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
				if (getPar().get(valor) == null || getPar().get(valor)[0].trim().equals("")
						|| getPar().get(valor)[0].trim().equals("Não") || getPar().get(valor)[0].trim().equals("Nao"))
					return false;
		return true;
	}
	
	@Post("app/expediente/doc/carregarpreench")
	public void aCarregarPreenchimento(ExDocumentoDTO exDocumentoDTO, String[] vars) throws Exception {
		ExPreenchimento exPreenchimento = new ExPreenchimento();
		setPar(getRequest().getParameterMap());
		
		// Obtém arrStrBanco[], com os parâmetros vindos do banco
		exPreenchimento = dao().consultar(exDocumentoDTO.getPreenchimento(), ExPreenchimento.class, false);
		String strBanco = new String(exPreenchimento.getPreenchimentoBA());
		String arrStrBanco[] = strBanco.split("&");
		String strBancoLimpa = new String();
		
		ExDocumentoDTO exDocumentoDTOPreench = exDocumentoDTO;
		
		// seta os atributos da action com base nos valores do banco, fazendo o
		// decoding da string
		for (String elem : arrStrBanco) {
			String[] paramNameAndValue = ((String) elem).split("=");
			String paramName = paramNameAndValue[0];
			String paramValue = paramNameAndValue[1];
			String paramValueDecoded = URLDecoder.decode(paramValue, "ISO-8859-1");
			String paramValueEncodedUTF8 = URLEncoder.encode(paramValueDecoded, "UTF-8");
			String dtoParamName = "exDocumentoDTO.".concat(paramName);
			
			try {
				if (!paramName.contains("Sel.id")) {
					final String mName = "set" + paramName.substring(0, 1).toUpperCase() + paramName.substring(1);
					if (getPar().get(dtoParamName) != null || (dtoParamName.contains("nmOrgaoExterno"))|| (dtoParamName.contains("nmDestinatario"))) {
						Class paramType = exDocumentoDTOPreench.getClass().getDeclaredField(paramName).getType();
						Constructor paramTypeContructor = paramType.getConstructor(new Class[] { String.class });
						final Method method = exDocumentoDTOPreench.getClass().getMethod(mName, new Class[] { paramType });
						method.invoke(exDocumentoDTOPreench, new Object[] { paramTypeContructor.newInstance(new Object[] { (paramValueDecoded) }) });
					}
				} else {
					final String mName = "get" + paramName.substring(0, 1).toUpperCase()
							+ paramName.substring(1, paramName.indexOf(".id"));
					if (getPar().get(dtoParamName) != null || (dtoParamName.contains("estinatarioSel.id"))) {
						final Method method = exDocumentoDTOPreench.getClass().getMethod(mName);
						Selecao sel = (Selecao) method.invoke(exDocumentoDTOPreench);
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
		exDocumentoDTOPreench.setSigla("");
		if (arrStrURL.length > 0) {
			for (String s : arrStrURL) {
				String arrStrURL2[] = s.split("=");
				if (arrStrURL2.length > 1 && !arrStrURL2[0].contains(".sigla") && !arrStrURL2[0].contains(".descricao")
						&& !strBanco.contains(arrStrURL2[0] + "="))
					strURLLimpa = strURLLimpa + s + "&";
			}
		}
		
		exDocumentoDTOPreench.setPreenchRedirect(strURLLimpa + strBancoLimpa);
		
		result.forwardTo(this).edita(exDocumentoDTOPreench, null, vars, exDocumentoDTO.getMobilPaiSel(), exDocumentoDTO.isCriandoAnexo());
		
	}
	
	@Get("app/expediente/doc/criar_via")
	public void criarVia(String sigla) throws Exception {
		ExDocumentoDTO exDocumentoDTO = new ExDocumentoDTO(sigla);
		buscarDocumento(true, exDocumentoDTO);
		
		if (!Ex.getInstance().getComp().podeCriarVia(getTitular(), getLotaTitular(), exDocumentoDTO.getMob()))
			throw new AplicacaoException("Não é possível criar vias neste documento");
		try {
			Ex.getInstance().getBL().criarVia(getCadastrante(), getLotaTitular(), exDocumentoDTO.getDoc());
		} catch (final Exception e) {
			throw e;
		}
		ExDocumentoController.redirecionarParaExibir(result, sigla);
	}
	
	@Get("app/expediente/doc/criar_volume")
	public void criarVolume(String sigla) throws Exception {
		ExDocumentoDTO exDocumentoDTO = new ExDocumentoDTO(sigla);
		buscarDocumento(true, exDocumentoDTO);
		
		if (!Ex.getInstance().getComp().podeCriarVolume(getTitular(), getLotaTitular(), exDocumentoDTO.getMob()))
			throw new AplicacaoException("Não é possível criar volumes neste documento");
		
		try {
			Ex.getInstance().getBL().criarVolume(getCadastrante(), getLotaTitular(), exDocumentoDTO.getDoc());
		} catch (final Exception e) {
			throw e;
		}
		ExDocumentoController.redirecionarParaExibir(result, exDocumentoDTO.getSigla());
	}
	
	@Post("app/expediente/doc/recarregar")
	public ExDocumentoDTO recarregar(ExDocumentoDTO exDocumentoDTO, String[] vars) throws Exception {
		exDocumentoDTO.getClassificacaoSel().apagar();
		result.forwardTo(this).edita(exDocumentoDTO, null, vars, exDocumentoDTO.getMobilPaiSel(),exDocumentoDTO.isCriandoAnexo());		
		return exDocumentoDTO;
	}
	
	@Get("app/expediente/doc/editar")
	public ExDocumentoDTO edita(ExDocumentoDTO exDocumentoDTO, String sigla, String[] vars, ExMobilSelecao mobilPaiSel, Boolean criandoAnexo) throws Exception {
		boolean isDocNovo = (exDocumentoDTO == null);
		if (isDocNovo) {
			exDocumentoDTO = new ExDocumentoDTO();
			exDocumentoDTO.setCriandoAnexo(criandoAnexo==null?false:criandoAnexo);
		    
			if (mobilPaiSel != null){
				exDocumentoDTO.setMobilPaiSel(mobilPaiSel);
			}
		}
		
		if ((sigla != null) && (sigla != "")){
			exDocumentoDTO.setSigla(sigla);
		}
		
		buscarDocumentoOuNovo(true, exDocumentoDTO);
		
		if ((isDocNovo) || (param("docFilho") != null)) {
			exDocumentoDTO.setTipoDestinatario(2);
			exDocumentoDTO.setIdFormaDoc(2);
			exDocumentoDTO.setIdTpDoc(1L);
			
			ExNivelAcesso nivelDefault = getNivelAcessoDefault(exDocumentoDTO);
			if (nivelDefault != null) {
				exDocumentoDTO.setNivelAcesso(nivelDefault.getIdNivelAcesso());
			} else
				exDocumentoDTO.setNivelAcesso(1L);
			
			exDocumentoDTO.setIdMod(((ExModelo) dao().consultarAtivoPorIdInicial(ExModelo.class, 26L)).getIdMod());
		}
		
		if (exDocumentoDTO.isCriandoAnexo() && exDocumentoDTO.getId() == null && isDocNovo) {
			exDocumentoDTO.setIdFormaDoc(60);
			exDocumentoDTO.setIdMod(((ExModelo) dao().consultarAtivoPorIdInicial(ExModelo.class, 507L)).getIdMod());
		}
		
		if (exDocumentoDTO.getDespachando() && exDocumentoDTO.getId() == null && (isDocNovo)) {
			
			exDocumentoDTO.setIdFormaDoc(8);
			
		}
		
		if (exDocumentoDTO.getId() == null && exDocumentoDTO.getDoc() != null)
			exDocumentoDTO.setId(exDocumentoDTO.getDoc().getIdDoc());
		
		if (exDocumentoDTO.getId() == null) {
			if (getLotaTitular().isFechada())
				throw new AplicacaoException("A lotação " + getLotaTitular().getSiglaLotacao() + " foi extinta em "
						+ new SimpleDateFormat("dd/MM/yyyy").format(getLotaTitular().getDataFimLotacao())
						+ ". Não é possível gerar expedientes em lotação extinta.");
			exDocumentoDTO.setDoc(new ExDocumento());
			exDocumentoDTO.getDoc().setOrgaoUsuario(getTitular().getOrgaoUsuario());
		} else {
			exDocumentoDTO.setDoc(daoDoc(exDocumentoDTO.getId()));
			
			if (!Ex.getInstance().getComp().podeEditar(getTitular(), getLotaTitular(), exDocumentoDTO.getMob()))
				throw new AplicacaoException("Não é permitido editar documento fechado");
			
			if (isDocNovo) {
				escreverForm(exDocumentoDTO);
				lerEntrevista(exDocumentoDTO);
			}
		}
		
		if (exDocumentoDTO.getTipoDocumento() != null && exDocumentoDTO.getTipoDocumento().equals("externo")) {
			exDocumentoDTO.setIdMod(((ExModelo) dao().consultarAtivoPorIdInicial(ExModelo.class, 28L)).getIdMod());
		}
		carregarBeans(exDocumentoDTO, mobilPaiSel);
		
		Long idSit = Ex
				.getInstance()
				.getConf()
				.buscaSituacao(exDocumentoDTO.getModelo(), exDocumentoDTO.getDoc().getExTipoDocumento(), getTitular(),
						getLotaTitular(), CpTipoConfiguracao.TIPO_CONFIG_ELETRONICO).getIdSitConfiguracao();
		
		if (idSit == ExSituacaoConfiguracao.SITUACAO_OBRIGATORIO) {
			exDocumentoDTO.setEletronico(1);
			exDocumentoDTO.setEletronicoFixo(true);
		} else if (idSit == ExSituacaoConfiguracao.SITUACAO_PROIBIDO) {
			exDocumentoDTO.setEletronico(2);
			exDocumentoDTO.setEletronicoFixo(true);
		} else if (idSit == ExSituacaoConfiguracao.SITUACAO_DEFAULT
				&& (exDocumentoDTO.getEletronico() == null || exDocumentoDTO.getEletronico() == 0)) {
			exDocumentoDTO.setEletronico(1);
		} else if (exDocumentoDTO.isAlterouModelo()) {
			if (idSit == ExSituacaoConfiguracao.SITUACAO_DEFAULT) {
				exDocumentoDTO.setEletronico(1);
			} else {
				exDocumentoDTO.setEletronicoFixo(false);
				exDocumentoDTO.setEletronico(0);
			}
		}
		
		lerForm(exDocumentoDTO, vars);
		
		// O (&& classif.getCodAssunto() != null) foi adicionado para permitir
		// que as classificações antigas, ainda não linkadas por equivalência,
		// possam ser usadas

		ExClassificacao classif = exDocumentoDTO.getClassificacaoSel().buscarObjeto();
		if (classif != null && classif.getHisDtFim() != null && classif.getHisDtIni() != null
				&& classif.getCodAssunto() != null) {

			classif = ExDao.getInstance().consultarAtual(classif);
			if (classif != null)
				exDocumentoDTO.getClassificacaoSel().setId(classif.getIdClassificacao());
			else
				exDocumentoDTO.getClassificacaoSel().setId(null);
		}
		
		exDocumentoDTO.getSubscritorSel().buscar();
		exDocumentoDTO.getDestinatarioSel().buscar();
		exDocumentoDTO.getLotacaoDestinatarioSel().buscar();
		exDocumentoDTO.getOrgaoSel().buscar();
		exDocumentoDTO.getOrgaoExternoDestinatarioSel().buscar();
		exDocumentoDTO.getClassificacaoSel().buscar();
		exDocumentoDTO.getMobilPaiSel().buscar();
		
		if (getRequest().getSession().getAttribute("preenchRedirect") != null) {
			exDocumentoDTO.setPreenchRedirect((String) getRequest().getSession().getAttribute("preenchRedirect"));
			getRequest().getSession().removeAttribute("preenchRedirect");
		}
		
		registraErroExtEditor();
		
		// Usado pela extensão editor...
		getPar().put("serverAndPort", new String[] { getRequest().getServerName()+ (getRequest().getServerPort() > 0 ? ":" + getRequest().getServerPort() : "") });
		
		// ...inclusive nas operações com preenchimento automático
		if (exDocumentoDTO.getPreenchRedirect() != null && exDocumentoDTO.getPreenchRedirect().length() > 2) {
			exDocumentoDTO.setPreenchRedirect(exDocumentoDTO.getPreenchRedirect() + "&serverAndPort="+ getPar().get("serverAndPort")[0]);
		}
		
		exDocumentoDTO.setTiposDocumento(getTiposDocumento());
		exDocumentoDTO.setListaNivelAcesso(getListaNivelAcesso(exDocumentoDTO));
		exDocumentoDTO.setFormasDoc(getFormasDocPorTipo(exDocumentoDTO));
		exDocumentoDTO.setModelos(getModelos(exDocumentoDTO));
		getPreenchimentos(exDocumentoDTO);
		
		Map<String, String[]> parFreeMarker = new HashMap<>();
		setPar(getRequest().getParameterMap());
		if (getPar() != null) {
			for (final String key : getPar().keySet()) {
				final String as[] = getPar().get(key);
				final String chave = key.replace("exDocumentoDTO.", "");
				parFreeMarker.put(chave, as);				
			}
		}
		
		result.include("possuiMaisQueUmModelo", (getModelos(exDocumentoDTO).size() > 1));
		result.include("par", parFreeMarker);
		result.include("cpOrgaoSel", exDocumentoDTO.getCpOrgaoSel());
		result.include("mobilPaiSel", exDocumentoDTO.getMobilPaiSel());
		result.include("subscritorSel", exDocumentoDTO.getSubscritorSel());
		result.include("titularSel", exDocumentoDTO.getTitularSel());
		result.include("destinatarioSel", exDocumentoDTO.getDestinatarioSel());
		result.include("lotacaoDestinatarioSel", exDocumentoDTO.getLotacaoDestinatarioSel());
		result.include("orgaoExternoDestinatarioSel", exDocumentoDTO.getOrgaoExternoDestinatarioSel());
		result.include("classificacaoSel", exDocumentoDTO.getClassificacaoSel());
		result.include("tipoDestinatario", exDocumentoDTO.getTipoDestinatario());
		
		return exDocumentoDTO;
	}
	
	public void aExcluir(ExDocumentoDTO exDocumentoDTO, Long id) throws Exception {
		ExDocumento documento = null;
		if (exDocumentoDTO == null) {
			exDocumentoDTO = new ExDocumentoDTO();
			exDocumentoDTO.setId(id);
		}		
		
		try {
			ExDao.iniciarTransacao();
			documento = daoDoc(id);
			
			verificaNivelAcesso(exDocumentoDTO.getDoc().getMobilGeral());
			
			// Testa se existe algum valor preenchido em documento.
			// Se não houver gera ObjectNotFoundException
			final Date d = documento.getDtRegDoc();
			
			if (documento.isFinalizado())
				
				throw new AplicacaoException("Documento já foi finalizado e não pode ser excluído");
			if (!Ex.getInstance().getComp().podeExcluir(getTitular(), getLotaTitular(), exDocumentoDTO.getMob()))
				throw new AplicacaoException("Não é possível excluir");
			
			// Exclui documento da tabela de Boletim Interno
			String funcao = exDocumentoDTO.getDoc().getForm().get("acaoExcluir");
			if (funcao != null) {
				obterMetodoPorString(funcao, exDocumentoDTO.getDoc());
			}
			
			for (ExMovimentacao movRef : exDocumentoDTO.getMob().getExMovimentacaoReferenciaSet())
				Ex.getInstance().getBL().excluirMovimentacao(movRef);
			
			dao().excluir(documento);
			ExDao.commitTransacao();
			
		} catch (final ObjectNotFoundException e) {
			throw new AplicacaoException("Documento já foi excluído anteriormente");
		} catch (final AplicacaoException e) {
			ExDao.rollbackTransacao();
			throw e;
		} catch (final Exception e) {
			ExDao.rollbackTransacao();
			throw new AplicacaoException("Ocorreu um Erro durante a Operação", 0, e);
		}
	}
	
	private void registraErroExtEditor() {
		
		try {
			if (param("desconsiderarExtensao") != null && param("desconsiderarExtensao").equals("true")) {
				
				String nomeArquivo = getRequest().getRemoteHost();
				nomeArquivo = nomeArquivo.replaceAll(":", "_");
				
				BufferedWriter out = new BufferedWriter(new FileWriter("./siga-ex-ext-editor-erro/" + nomeArquivo));
				out.close();
			}
		} catch (IOException e) {
			int a = 0;
		}
	}
	
	private void obterMetodoPorString(String metodo, ExDocumento doc) throws Exception {
		if (metodo != null) {
			final Class[] classes = new Class[] { ExDocumento.class };
			
			Method method;
			try {
				method = Ex.getInstance().getBL().getClass().getDeclaredMethod(metodo, classes);
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
				return;
			}
			method.invoke(Ex.getInstance().getBL(), new Object[] { doc });
		}
	}
	
	@Get("/app/expediente/doc/excluir")
	public void aExcluirDocMovimentacoes(String sigla) throws Exception {
		ExDocumentoDTO exDocumentoDTO = new ExDocumentoDTO();
		exDocumentoDTO.setSigla(sigla);		
		buscarDocumento(true, exDocumentoDTO);
		ExDocumento doc = exDocumentoDTO.getDoc();
		try {
			ExDao.iniciarTransacao();
			
			try {
				final Date d = doc.getDtRegDoc();
			} catch (final ObjectNotFoundException e) {
				throw new AplicacaoException("Documento já foi excluído anteriormente", 1, e);
			}
			
			if (doc.isFinalizado())
				
				throw new AplicacaoException("Documento já foi finalizado e não pode ser excluído", 2);
			for (ExMobil m : doc.getExMobilSet()) {
				Set set = m.getExMovimentacaoSet();
				
				if (!Ex.getInstance().getComp().podeExcluir(getTitular(), getLotaTitular(), m))
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
								.excluirMovimentacao(getCadastrante(), getLotaTitular(), movimentacao.getExMobil(),
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
			result.include("sigla", sigla);
			result.redirectTo("/app/expediente/doc/listar");
		} catch (final AplicacaoException e) {
			ExDao.rollbackTransacao();
			throw e;
		} catch (final Exception e) {
			ExDao.rollbackTransacao();
			throw new AplicacaoException("Ocorreu um Erro durante a Operação", 0, e);
		}
	}
	
	@Post("app/expediente/doc/excluirpreench")
	public void aExcluirPreenchimento(ExDocumentoDTO exDocumentoDTO, String[] vars) throws Exception {
		dao().iniciarTransacao();
		ExPreenchimento exemplo = dao().consultar(exDocumentoDTO.getPreenchimento(), ExPreenchimento.class, false);
		dao().excluir(exemplo);
		// preenchDao.excluirPorId(preenchimento);
		dao().commitTransacao();
		exDocumentoDTO.setPreenchimento(0L);
		result.forwardTo(this).edita(exDocumentoDTO, null, vars, exDocumentoDTO.getMobilPaiSel(), exDocumentoDTO.isCriandoAnexo());
	}
	
	public String aTestarConexao() {
		return "success";
	}
	
	public String aAcessar(ExDocumentoDTO exDocumentoDTO) throws Exception {
		buscarDocumento(false, exDocumentoDTO);
		
		assertAcesso(exDocumentoDTO);
		
		return "success";
	}
	
	private void assertAcesso(ExDocumentoDTO exDocumentoDTO) throws Exception {
		String msgDestinoDoc = "";
		DpPessoa dest;
		if (!Ex.getInstance().getComp().podeAcessarDocumento(getTitular(), getLotaTitular(), exDocumentoDTO.getMob())) {
			
			String s = "";
			try {
				s += exDocumentoDTO.getMob().doc().getListaDeAcessosString();
				s = "(" + s + ")";
				s = " " + exDocumentoDTO.getMob().doc().getExNivelAcesso().getNmNivelAcesso() + " " + s;
			} catch (Exception e) {
			}

			throw new AplicacaoException("Documento " + exDocumentoDTO.getMob().getSigla() + " inacessível ao usuário "
					+ getTitular().getSigla() + "/" + getLotaTitular().getSiglaCompleta() + "." + s + " "
					+ msgDestinoDoc);
		}
	}
	
	@Get("app/expediente/doc/exibirAntigo")
	public void aExibirAntigo(String sigla, boolean popup) throws Exception {
		ExDocumentoDTO exDocumentoDTO  = new ExDocumentoDTO();
		
		exDocumentoDTO.setSigla(sigla);
		buscarDocumento(false, exDocumentoDTO);
		
		assertAcesso(exDocumentoDTO);
		
		if (Ex.getInstance().getComp().podeReceberEletronico(getTitular(), getLotaTitular(), exDocumentoDTO.getMob()))
			Ex.getInstance().getBL().receber(getCadastrante(), getLotaTitular(), exDocumentoDTO.getMob(), new Date());
		
		ExDocumentoVO docVO = new ExDocumentoVO(exDocumentoDTO.getDoc(), exDocumentoDTO.getMob(), getTitular(),
				getLotaTitular(), true, true);

		if (exDocumentoDTO.getMob().isEliminado())
			throw new AplicacaoException("Documento "
					+ exDocumentoDTO.getMob().getSigla()
					+ " eliminado, conforme o termo "
					+ exDocumentoDTO.getMob().getUltimaMovimentacaoNaoCancelada(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ELIMINACAO)
							.getExMobilRef());
		result.include("msg", exDocumentoDTO.getMsg());
		result.include("docVO", docVO);
		result.include("mob", exDocumentoDTO.getMob());
		result.include("exibirCompleto", exDocumentoDTO.isExibirCompleto());
		result.include("currentTimeMillis", System.currentTimeMillis());
		result.include("popup", popup);
	}
	
	@Get("/app/expediente/doc/exibir")
	public void exibe(boolean conviteEletronico, String sigla, ExDocumentoDTO exDocumentoDTO) throws Exception {
		ExDocumentoDTO exDocumentoDto;
		if (exDocumentoDTO == null){
			exDocumentoDto = new ExDocumentoDTO();
		}else{
			exDocumentoDto = exDocumentoDTO;
		}
		
		exDocumentoDto.setSigla(sigla);
		buscarDocumento(false, exDocumentoDto);
		
		assertAcesso(exDocumentoDto);
		
		if (Ex.getInstance().getComp().podeReceberEletronico(getTitular(), getLotaTitular(), exDocumentoDto.getMob()))
			Ex.getInstance().getBL().receber(getCadastrante(), getLotaTitular(), exDocumentoDto.getMob(), new Date());
		
		if (exDocumentoDto.getMob() == null || exDocumentoDto.getMob().isGeral()) {
			if (exDocumentoDto.getMob().getDoc().isFinalizado()) {
				if (exDocumentoDto.getDoc().isProcesso())
					exDocumentoDto.setMob(exDocumentoDto.getDoc().getUltimoVolume());
				else
					exDocumentoDto.setMob(exDocumentoDto.getDoc().getPrimeiraVia());
			}
		}
		
		ExDocumentoVO docVO = new ExDocumentoVO(exDocumentoDto.getDoc(), exDocumentoDto.getMob(), getTitular(), getLotaTitular(),
				true, false);
		
		docVO.exibe();
		
		result.include("docVO", docVO);
		result.include("sigla", exDocumentoDto.getSigla().replace("/", ""));
		result.include("id", exDocumentoDto.getId());
		result.include("mob", exDocumentoDto.getMob());
		result.include("lota", this.getLotaTitular());
		result.include("param", exDocumentoDto.getParamsEntrevista());
	}
	
	@Get("app/expediente/doc/exibirProcesso")
	public void exibeProcesso(String sigla, boolean podeExibir) throws Exception {
		exibe(false, sigla, null);
	}
	
	@Get("/app/expediente/doc/exibirResumoProcesso")
	public void exibeResumoProcesso(String sigla, boolean podeExibir) throws Exception {
		exibe(false, sigla, null);
	}
	
	public String aCorrigirPDF(ExDocumentoDTO exDocumentoDTO) throws Exception {
		if (exDocumentoDTO.getSigla() != null) {
			final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
			filter.setSigla(exDocumentoDTO.getSigla());
			exDocumentoDTO.setMob((ExMobil) dao().consultarPorSigla(filter));
			
			Ex.getInstance().getBL().processar(exDocumentoDTO.getMob().getExDocumento(), true, false, null);
		}
		return "success";
	}
	
	private void logStatistics() {
		Statistics stats = ExDao.getInstance().getSessao().getSessionFactory().getStatistics();
		SessionStatistics statsSession = ExDao.getInstance().getSessao().getStatistics();
		
		double queryCacheHitCount = stats.getQueryCacheHitCount();
		double queryCacheMissCount = stats.getQueryCacheMissCount();
		double queryCacheHitRatio = queryCacheHitCount / (queryCacheHitCount + queryCacheMissCount);
		
		System.out.println("Query Hit ratio:" + queryCacheHitRatio);
		
		System.out
				.println(stats.getQueryExecutionMaxTimeQueryString() + " [time (ms): " + stats.getQueryExecutionMaxTime() + "]");
		
		System.out.println("\n\n\n\n\n\n*****************************************\n\n\n\n\n\n\n");
		
		for (String query : stats.getQueries()) {
			QueryStatistics qs = stats.getQueryStatistics(query);
			System.out.println(query + " [time (ms): " + qs.getExecutionAvgTime() + ", count: " + qs.getExecutionCount() + "]");
		}
		
		for (String ent : stats.getEntityNames()) {
			EntityStatistics es = stats.getEntityStatistics(ent);
			System.out.println(ent + " [count: " + es.getFetchCount() + "]");
		}
		
		System.out.println("\n\n\n\n\n\n*****************************************\n\n\n\n\n\n\n");
		
	}
	
	private void verificaDocumento(ExDocumento doc) throws AplicacaoException, Exception {
		if ((doc.getSubscritor() == null && doc.getNmSubscritor() == null && doc.getNmSubscritorExt() == null)
				&& ((doc.getExFormaDocumento().getExTipoFormaDoc().getIdTipoFormaDoc() == 2 && doc.isEletronico()) || doc
						.getExFormaDocumento().getExTipoFormaDoc().getIdTipoFormaDoc() != 2))
			throw new AplicacaoException("É necessário definir um subscritor para o documento.");
		
		if (doc.getDestinatario() == null && doc.getLotaDestinatario() == null
				&& (doc.getNmDestinatario() == null || doc.getNmDestinatario().trim().equals(""))
				&& doc.getOrgaoExternoDestinatario() == null
				&& (doc.getNmOrgaoExterno() == null || doc.getNmOrgaoExterno().trim().equals(""))) {
			Long idSit = Ex
					.getInstance()
					.getConf()
					.buscaSituacao(doc.getExModelo(), getTitular(), getLotaTitular(), CpTipoConfiguracao.TIPO_CONFIG_DESTINATARIO)
					.getIdSitConfiguracao();
			if (idSit == ExSituacaoConfiguracao.SITUACAO_OBRIGATORIO)
				throw new AplicacaoException("Para documentos do modelo " + doc.getExModelo().getNmMod()
						+ ", é necessário definir um destinatário");
		}
		
		if (doc.getExClassificacao() == null)
			throw new AplicacaoException("É necessário informar a classificação documental.");
		
	}
	
	private void buscarDocumentoOuNovo(boolean fVerificarAcesso, ExDocumentoDTO exDocumentoDTO) throws Exception {
		buscarDocumento(fVerificarAcesso, true, exDocumentoDTO);
		if (exDocumentoDTO.getDoc() == null) {
			exDocumentoDTO.setDoc(new ExDocumento());
			exDocumentoDTO.getDoc().setExTipoDocumento(dao().consultar(ExTipoDocumento.TIPO_DOCUMENTO_INTERNO, ExTipoDocumento.class, false));
			exDocumentoDTO.setMob(new ExMobil());
			exDocumentoDTO.getMob().setExTipoMobil(dao().consultar(ExTipoMobil.TIPO_MOBIL_GERAL, ExTipoMobil.class, false));
			exDocumentoDTO.getMob().setNumSequencia(1);
			exDocumentoDTO.getMob().setExDocumento(exDocumentoDTO.getDoc());
			
			exDocumentoDTO.getDoc().setExMobilSet(new TreeSet<ExMobil>());
			exDocumentoDTO.getDoc().getExMobilSet().add(exDocumentoDTO.getMob());
		}
	}
	
	private void buscarDocumento(boolean fVerificarAcesso, ExDocumentoDTO exDocumentoDTO) throws Exception {
		buscarDocumento(fVerificarAcesso, false, exDocumentoDTO);
	}
	
	private void buscarDocumento(boolean fVerificarAcesso, boolean fPodeNaoExistir, ExDocumentoDTO exDocumentoDto)
			throws Exception {
		if (exDocumentoDto.getMob() == null && exDocumentoDto.getSigla() != null && exDocumentoDto.getSigla().length() != 0) {
			final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
			filter.setSigla(exDocumentoDto.getSigla());
			exDocumentoDto.setMob((ExMobil) dao().consultarPorSigla(filter));
			if (exDocumentoDto.getMob() != null) {
				exDocumentoDto.setDoc(exDocumentoDto.getMob().getExDocumento());
			}
		} else if (exDocumentoDto.getMob() == null && exDocumentoDto.getDocumentoViaSel().getId() != null) {
			exDocumentoDto.setIdMob(exDocumentoDto.getDocumentoViaSel().getId());
			exDocumentoDto.setMob(dao().consultar(exDocumentoDto.getIdMob(), ExMobil.class, false));
		} else if (exDocumentoDto.getMob() == null && exDocumentoDto.getIdMob() != null && exDocumentoDto.getIdMob() != 0) {
			exDocumentoDto.setMob(dao().consultar(exDocumentoDto.getIdMob(), ExMobil.class, false));

		}
		if (exDocumentoDto.getMob() != null)
			exDocumentoDto.setDoc(exDocumentoDto.getMob().doc());
		if (exDocumentoDto.getDoc() == null) {
			String id = param("exDocumentoDto.id");
			if (id != null && id.length() != 0) {
				exDocumentoDto.setDoc(daoDoc(Long.parseLong(id)));
			}
		}
		if (exDocumentoDto.getDoc() != null && exDocumentoDto.getMob() == null)
			exDocumentoDto.setMob(exDocumentoDto.getDoc().getMobilGeral());
		
		if (!fPodeNaoExistir && exDocumentoDto.getDoc() == null)
			throw new AplicacaoException("Documento não informado");
		if (fVerificarAcesso && exDocumentoDto.getMob() != null && exDocumentoDto.getMob().getIdMobil() != null)
			verificaNivelAcesso(exDocumentoDto.getMob());
	}
	
	@Get("/app/expediente/doc/finalizar")
	public void aFinalizar(String sigla) throws Exception {
		
		ExDocumentoDTO exDocumentoDto = new ExDocumentoDTO();
		exDocumentoDto.setSigla(sigla);
		
		buscarDocumento(true, exDocumentoDto);
		
		verificaDocumento(exDocumentoDto.getDoc());
		
		if (!Ex.getInstance().getComp().podeFinalizar(getTitular(), getLotaTitular(), exDocumentoDto.getMob()))
			throw new AplicacaoException("Não é possível Finalizar");
		
		try {
			exDocumentoDto.setMsg(Ex.getInstance().getBL()
					.finalizar(getCadastrante(), getLotaTitular(), exDocumentoDto.getDoc(), null));
			

			if (exDocumentoDto.getDoc().getForm() != null) {
				if (exDocumentoDto.getDoc().getForm().get("acaoFinalizar") != null
						&& exDocumentoDto.getDoc().getForm().get("acaoFinalizar").trim().length() > 0) {
					obterMetodoPorString(exDocumentoDto.getDoc().getForm().get("acaoFinalizar"), exDocumentoDto.getDoc());

				}
			}
			
		} catch (final Throwable t) {
			throw new AplicacaoException("Erro ao finalizar documento", 0, t);
		}
		
		result.redirectTo("exibir?sigla=" + exDocumentoDto.getDoc().getCodigo());
		
	}
	
	public void aFinalizarAssinar(String sigla) throws Exception {
		
		aFinalizar(sigla);
	}
	
	@Post("/app/expediente/doc/gravar")
	public void gravar(ExDocumentoDTO exDocumentoDTO, String[] vars, String[] campos) throws Exception {
		Ex ex = Ex.getInstance();
		ExBL exBL = ex.getBL();
		
		try {
			buscarDocumentoOuNovo(true, exDocumentoDTO);
			if (exDocumentoDTO.getDoc() == null){
				exDocumentoDTO.setDoc(new ExDocumento());	
			}
			
			long tempoIni = System.currentTimeMillis();
			
			if (!validar()) {
				edita(exDocumentoDTO, null, vars, exDocumentoDTO.getMobilPaiSel(), exDocumentoDTO.isCriandoAnexo());
				getPar().put("alerta", new String[] { "Sim" });
				exDocumentoDTO.setAlerta("Sim");
				
				String url = null;
				if(exDocumentoDTO.getMobilPaiSel().getSigla() != null) {
					url = MessageFormat.format("editar?mobilPaiSel.sigla={0}&criandoAnexo={1}", exDocumentoDTO.getMobilPaiSel().getSigla(), exDocumentoDTO.isCriandoAnexo());
				} else {
					url = MessageFormat.format("editar?sigla={0}&criandoAnexo={1}", exDocumentoDTO.getSigla(), exDocumentoDTO.isCriandoAnexo());
				}
				
				result.redirectTo(url);
				return;
			}
			
			lerForm(exDocumentoDTO, vars);
			
			if (!ex
					.getConf()
					.podePorConfiguracao(getTitular(), getLotaTitular(), exDocumentoDTO.getDoc().getExTipoDocumento(), exDocumentoDTO.getDoc().getExFormaDocumento() 
							,exDocumentoDTO.getDoc().getExModelo(), exDocumentoDTO.getDoc().getExClassificacaoAtual(), exDocumentoDTO.getDoc().getExNivelAcesso()
							,CpTipoConfiguracao.TIPO_CONFIG_CRIAR)) {
				
				if (!ex.getConf().podePorConfiguracao(getTitular(), getLotaTitular(), null, null, null,
						exDocumentoDTO.getDoc().getExClassificacao(), null, CpTipoConfiguracao.TIPO_CONFIG_CRIAR))
					throw new AplicacaoException("Usuário não possui permissão de criar documento da classificação "
							+ exDocumentoDTO.getDoc().getExClassificacao().getCodificacao());
				
				throw new AplicacaoException("Operação não permitida");
			}
			

			System.out.println("monitorando gravacao IDDoc " + exDocumentoDTO.getDoc().getIdDoc() + ", PESSOA "+ exDocumentoDTO.getDoc().getCadastrante().getIdPessoa() 
							  +". Terminou verificacao de config PodeCriarModelo: "+ (System.currentTimeMillis() - tempoIni));
			
			tempoIni = System.currentTimeMillis();
			
			exDocumentoDTO.getDoc().setOrgaoUsuario(getLotaTitular().getOrgaoUsuario());
			

			if (exDocumentoDTO.isClassificacaoIntermediaria()
					&& (exDocumentoDTO.getDescrClassifNovo() == null || exDocumentoDTO.getDescrClassifNovo().trim()
							.length() == 0))
				throw new AplicacaoException("Quando a classificação selecionada não traz informação para criação de vias, o "
							                +"sistema exige que, antes de gravar o documento, seja informada uma sugestão de "
						                    +"classificação para ser incluída na próxima revisão da tabela de classificações.");
			
			if (exDocumentoDTO.getDoc().getDescrDocumento().length() > exDocumentoDTO.getTamanhoMaximoDescricao())
				throw new AplicacaoException("O campo descrição possui mais do que "+ exDocumentoDTO.getTamanhoMaximoDescricao() 
						                    +" caracteres.");
			
			if (exDocumentoDTO.getDoc().isFinalizado()) {
				
				Date dt = dao().dt();
				Calendar c = Calendar.getInstance();
				c.setTime(dt);
				
				Calendar dtDocCalendar = Calendar.getInstance();
				
				if (exDocumentoDTO.getDoc().getDtDoc() == null)
					throw new Exception("A data do documento deve ser informada.");
				
				dtDocCalendar.setTime(exDocumentoDTO.getDoc().getDtDoc());
				
				if (c.before(dtDocCalendar))
					throw new Exception("Não é permitido criar documento com data futura");
				
				verificaDocumento(exDocumentoDTO.getDoc());
			}
			
			ExMobil mobilAutuado = null;
			if (exDocumentoDTO.getIdMobilAutuado() != null) {
				
				mobilAutuado = dao().consultar(exDocumentoDTO.getIdMobilAutuado(), ExMobil.class, false);
				
				exDocumentoDTO.getDoc().setExMobilAutuado(mobilAutuado);
			}
			
			String realPath = getContext().getRealPath("");
			exBL.gravar(getCadastrante(), getLotaTitular(), exDocumentoDTO.getDoc(), realPath);
			
			lerEntrevista(exDocumentoDTO);
			
			if (exDocumentoDTO.getDesativarDocPai().equals("sim"))
				exDocumentoDTO.setDesativ("&exDocumentoDTO.desativarDocPai=sim");
			
			try {				
				exBL.incluirCosignatariosAutomaticamente(getCadastrante(), getLotaTitular(), exDocumentoDTO.getDoc());				
			} catch (Exception e) {				
				throw new AplicacaoException("Erro ao tentar incluir os cosignatários deste documento", 0, e);				
			}
			
		} catch (final Exception e) {
			throw new AplicacaoException("Erro na gravação", 0, e);
		}
		
		if (param("ajax") != null && param("ajax").equals("true")) {
			String body = MessageFormat.format("OK_{0}_{1}", exDocumentoDTO.getDoc().getSigla(), exDocumentoDTO.getDoc().getDtRegDocDDMMYY());
			result
				.use(Results.http())
				.body(body);
		} else {
			String url = MessageFormat.format("exibir?sigla={0}{1}", exDocumentoDTO.getDoc().getSigla(), exDocumentoDTO.getDesativ());
			result.redirectTo(url);
		}
	}
	
	
	@Post("app/expediente/doc/gravarpreench")
	public void aGravarPreenchimento(ExDocumentoDTO exDocumentoDTO, String[] vars, String[] campos) throws Exception {
		dao().iniciarTransacao();
		ExPreenchimento exPreenchimento = new ExPreenchimento();
		
		DpLotacao provLota = new DpLotacao();
		provLota = getLotaTitular();
		ExModelo provMod = new ExModelo();
		provMod.setIdMod(exDocumentoDTO.getIdMod());
		
		exPreenchimento.setDpLotacao(provLota);
		exPreenchimento.setExModelo(provMod);
		exPreenchimento.setNomePreenchimento(exDocumentoDTO.getNomePreenchimento());
		
		exPreenchimento.setPreenchimentoBA(getByteArrayFormPreenchimento(vars, campos));
		dao().gravar(exPreenchimento);
		dao().commitTransacao();
		
		exDocumentoDTO.setPreenchimento(exPreenchimento.getIdPreenchimento());
		
		result.forwardTo(this).aCarregarPreenchimento(exDocumentoDTO, vars);
		
	}
	
	@Post("app/expediente/doc/prever")
	public void preve(ExDocumentoDTO exDocumentoDTO, String[] vars) throws Exception {
		boolean isDocNovo = (exDocumentoDTO == null);
		buscarDocumentoOuNovo(true, exDocumentoDTO);
		if (exDocumentoDTO.getDoc() != null) {
			if (isDocNovo) {
				escreverForm(exDocumentoDTO);
			} else {				
				lerForm(exDocumentoDTO, vars);
			}
		} else {
			exDocumentoDTO.setDoc(new ExDocumento());
			lerForm(exDocumentoDTO, vars);
		}
		
		carregarBeans(exDocumentoDTO, exDocumentoDTO.getMobilPaiSel());
		
		if (exDocumentoDTO.getIdMob() != null) {
			exDocumentoDTO.setModelo(dao().consultar(exDocumentoDTO.getIdMob(), ExModelo.class, false));
		}
		
		if (param("processar_modelo") != null)
			result.forwardTo(this).processa_modelo(exDocumentoDTO);
		else{
			result.include("par", getRequest().getParameterMap());
			result.include("modelo", exDocumentoDTO.getModelo());
			result.include("nmArqMod", exDocumentoDTO.getModelo().getNmArqMod());
			result.include("doc", exDocumentoDTO.getDoc());
		}
		
	}
	
	
	public void processa_modelo(ExDocumentoDTO exDocumentoDTO) throws Exception {
		result.include("par", getRequest().getParameterMap());
		result.include("modelo", exDocumentoDTO.getModelo());
		result.include("nmArqMod", exDocumentoDTO.getModelo().getNmArqMod());
		result.include("doc", exDocumentoDTO.getDoc());
	}
	
	@Post("app/expediente/doc/preverPdf")
	public Download aPreverPdf(ExDocumentoDTO exDocumentoDTO, String[] vars) throws Exception {
		boolean isDocNovo = (exDocumentoDTO == null);
		buscarDocumentoOuNovo(true, exDocumentoDTO);
		if (exDocumentoDTO.getDoc() != null) {
			if (isDocNovo) {
				escreverForm(exDocumentoDTO);
			} else {
				lerForm(exDocumentoDTO, vars);
			}
		} else {
			exDocumentoDTO.setDoc(new ExDocumento());
			lerForm(exDocumentoDTO, vars);
		}
		
		carregarBeans(exDocumentoDTO, exDocumentoDTO.getMobilPaiSel());
		
		if (exDocumentoDTO.getIdMob() != null) {
			exDocumentoDTO.setModelo(dao().consultar(exDocumentoDTO.getIdMob(), ExModelo.class, false));
		}
		
		String realPath = getContext().getRealPath("");
		Ex.getInstance().getBL().processar(exDocumentoDTO.getDoc(), false, false, realPath);
		
		exDocumentoDTO.setPdfStreamResult(new ByteArrayInputStream(exDocumentoDTO.getDoc().getConteudoBlobPdf()));
		
		return new InputStreamDownload(exDocumentoDTO.getPdfStreamResult(), "application/pdf", "document.pdf");
	}
	
	@Get("app/expediente/doc/refazer")
	public void refazer(String sigla) throws Exception {
		ExDocumentoDTO exDocumentoDTO = new ExDocumentoDTO(sigla);
		this.buscarDocumento(true, exDocumentoDTO);
		
		if (!Ex.getInstance().getComp().podeRefazer(getTitular(), getLotaTitular(), exDocumentoDTO.getMob()))
			throw new AplicacaoException("Não é possível refazer o documento");
		
		try {
			exDocumentoDTO.setDoc(Ex.getInstance().getBL().refazer(getCadastrante(), getLotaTitular(), exDocumentoDTO.getDoc()));
		} catch (final Exception e) {
			throw e;
		}
		ExDocumentoController.redirecionarParaExibir(result, exDocumentoDTO.getDoc().getSigla());
	}
	
	public String aAtualizarMarcasDoc(ExDocumentoDTO exDocumentoDTO) throws Exception {
		
		buscarDocumento(false, exDocumentoDTO);
		Ex.getInstance().getBL().atualizarMarcas(exDocumentoDTO.getDoc());
		
		return "success";
	}
	
	public String aTestarPdf() throws Exception {
		return "success";
	}
	
	public String aTesteEnvioDJE() throws Exception {
		
		try {
			ExMovimentacao fakeMov = ExDao.getInstance().consultar(39468L, ExMovimentacao.class, false);
			ExDocumento doque = fakeMov.getExDocumento();
			GeradorRTF gerador = new GeradorRTF();
			String nomeArq = doque.getIdDoc().toString();
			fakeMov.setConteudoBlobRTF(nomeArq, gerador.geraRTF(doque));
			fakeMov.setConteudoBlobXML(nomeArq, PublicacaoDJEBL.gerarXMLPublicacao(fakeMov, "A", "SESIA", "Teste descrição"));
			fakeMov.setNmArqMov(nomeArq + ".zip");
			
			PublicacaoDJEBL.primeiroEnvio(fakeMov);
			return "success";
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
		Ex.getInstance().getBL().corrigirArquivamentosEmVolume(idPrimeiroDoc, idUltimoDoc, efetivar);
		return "success";
	}
	
	public String aTesteExclusaoDJE() throws Exception {
		ExMovimentacao fakeMov = ExDao.getInstance().consultar(37644L, ExMovimentacao.class, false);
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
										 * ExDocumentoBL.remeterParaPublicacao(edson , edson.getLotacao(), ex, null, new Date(),
										 * edson, edson, edson.getLotacao(), cal .getTime());
										 */
			/*
			 * ExMovimentacao fakeMov = new ExMovimentacao(); GeradorRTF gerador = new GeradorRTF(); String nomeArq = "I-" +
			 * doc.getIdDoc(); fakeMov.setConteudoBlobRTF(nomeArq, gerador.geraRTF(doc)); fakeMov.setConteudoBlobXML(nomeArq,
			 * PublicacaoDJEBL .gerarXMLPublicacao(fakeMov)); fakeMov.setNmArqMov(nomeArq + ".zip"); Long numTRF = PublicacaoDJEBL
			 * .verificaPrimeiroRetornoPublicacao(fakeMov); /* final Compactador zip = new Compactador(); final byte[] arqZip =
			 * getConteudoBlobMov2(); byte[] conteudoZip = null; conteudoZip = zip.adicionarStream(nome, conteudo, arqZip);
			 * setConteudoBlobMov2(conteudoZip);
			 */
			
		}
		return "success";
	}
	
	@Get("app/expediente/doc/duplicar")
	public void aDuplicar(boolean conviteEletronico, String sigla) throws Exception {
		
		ExDocumentoDTO exDocumentoDto = new ExDocumentoDTO();
		exDocumentoDto.setSigla(sigla);
		buscarDocumento(false, exDocumentoDto);
		if (!Ex.getInstance().getComp().podeDuplicar(getTitular(), getLotaTitular(), exDocumentoDto.getMob()))
			throw new AplicacaoException("Não é possível duplicar o documento");
		try {
			exDocumentoDto.setDoc(Ex.getInstance().getBL().duplicar(getCadastrante(), getLotaTitular(), exDocumentoDto.getDoc()));

		} catch (final Exception e) {
			throw e;
		}
		result.redirectTo("exibir?sigla=" + exDocumentoDto.getDoc().getCodigo());
	}
	
	private void atualizaLotacoesPreenchimentos(DpLotacao lotaTitular) throws AplicacaoException {
		if (lotaTitular.getIdLotacao().longValue() != lotaTitular.getIdLotacaoIni().longValue()) {
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
	
	
	public String aDesfazerCancelamentoDocumento(ExDocumentoDTO exDocumentoDTO, String sigla) throws Exception {
		if (sigla != null){
			exDocumentoDTO.setSigla(sigla);
		}
		buscarDocumento(true, exDocumentoDTO);
		if (!Ex.getInstance().getComp()
				.podeDesfazerCancelamentoDocumento(getTitular(), getLotaTitular(), exDocumentoDTO.getMob()))
			throw new AplicacaoException("Não é possível desfazer o cancelamento deste documento");
		try {

			Ex.getInstance().getBL().DesfazerCancelamentoDocumento(getCadastrante(), getLotaTitular(), exDocumentoDTO.getDoc());

		} catch (final Exception e) {
			throw e;
		}
		
		return "success";
	}
	
	public String aTornarDocumentoSemEfeito(ExDocumentoDTO exDocumentoDTO) throws Exception {
		buscarDocumento(true, exDocumentoDTO);
		return "success";
	}
	
	public String aTornarDocumentoSemEfeitoGravar(ExDocumentoDTO exDocumentoDTO) throws Exception {
		if (exDocumentoDTO.getDescrMov() == null || exDocumentoDTO.getDescrMov().trim().length() == 0) {
			throw new AplicacaoException("O preenchimento do campo MOTIVO é obrigatório!");
		}

		buscarDocumento(true, exDocumentoDTO);
		if (!Ex.getInstance().getComp()
				.podeTornarDocumentoSemEfeito(getTitular(), getLotaTitular(), exDocumentoDTO.getMob()))

			throw new AplicacaoException("Não é possível tornar documento sem efeito.");
		try {
			Ex.getInstance()
					.getBL()
					.TornarDocumentoSemEfeito(getCadastrante(), getLotaTitular(), exDocumentoDTO.getDoc(),
							exDocumentoDTO.getDescrMov());
		} catch (final Exception e) {
			throw e;
		}
		return "success";
	}
	
	public String acriarDocTest() throws Exception {
		try {
			setMensagem(Ex.getInstance().getBL().criarDocTeste());
		} catch (final Exception e) {
			throw e;
		}
		
		return "success";
	}
	
	private void carregarBeans(ExDocumentoDTO exDocumentoDTO, ExMobilSelecao mobilPaiSel) throws Exception {
		setPar(getRequest().getParameterMap());
		ExMobil mobPai = null;
		

		exDocumentoDTO.getDoc().setExTipoDocumento(dao().consultar(exDocumentoDTO.getIdTpDoc(), ExTipoDocumento.class, false));
		
		// Questões referentes a doc pai-----------------------------
		
		if (exDocumentoDTO.getDoc().getIdDoc() == null) {
			String req = "nao";
			if (getPar().get("reqexDocumentoDTO.documentoRefSel") != null)
				req = getPar().get("reqexDocumentoDTO.mobilPaiSel")[0].toString();
			
			if (getPar().get("reqdocumentoRefSel") != null)
				req = getPar().get("reqmobilPaiSel")[0].toString();			
			
			if ((mobilPaiSel != null) && (mobilPaiSel.getSigla() != null))
				exDocumentoDTO.getMobilPaiSel().setSigla(mobilPaiSel.getSigla());
			
			
			exDocumentoDTO.getMobilPaiSel().buscar();
			if ((exDocumentoDTO.getMobilPaiSel().getId() != null) || (req.equals("sim"))) {
				
				if (exDocumentoDTO.getMobilPaiSel().getId() != null) {
					// Documento Pai
					mobPai = daoMob(exDocumentoDTO.getMobilPaiSel().getId());
					
					Integer idForma = paramInteger("exDocumentoDTO.idFormaDoc");
					if (idForma != null)
						exDocumentoDTO.setIdFormaDoc(idForma);
					
					if (exDocumentoDTO.getClassificacaoSel() != null
							&& exDocumentoDTO.getClassificacaoSel().getId() == null)
						exDocumentoDTO.getClassificacaoSel().setId(mobPai.doc().getExClassificacaoAtual().getId());
					
					exDocumentoDTO.setDescrDocumento(mobPai.doc().getDescrDocumento());
					
					exDocumentoDTO.setDesativarDocPai("sim");
				}
				
			}
			
			if (exDocumentoDTO.getAutuando() && exDocumentoDTO.getIdMobilAutuado() != null) {
				ExMobil mobilAutuado = daoMob(exDocumentoDTO.getIdMobilAutuado());
				
				exDocumentoDTO.getDoc().setExMobilAutuado(mobilAutuado);
				
				exDocumentoDTO.getClassificacaoSel().setId(mobilAutuado.getDoc().getExClassificacao().getId());
				exDocumentoDTO.setDescrDocumento(mobilAutuado.getDoc().getDescrDocumento());
			}
		}
		
		// Fim das questões referentes a doc pai--------------------
		
		Integer idFormaDoc = exDocumentoDTO.getIdFormaDoc();
		if (idFormaDoc != null) {
			if (idFormaDoc == 0) {
				exDocumentoDTO.setIdMod(0L);
			} else {
				
				// Mudou origem? Escolhe um tipo automaticamente--------
				// Vê se usuário alterou campo Origem. Caso sim, seleciona um
				// tipo
				// automaticamente, dentro daquela origem
				
				final List<ExFormaDocumento> formasDoc = getFormasDocPorTipo(exDocumentoDTO);
				
				ExFormaDocumento forma = dao().consultar(exDocumentoDTO.getIdFormaDoc(), ExFormaDocumento.class, false);
				
				if (!formasDoc.contains(forma)) {
					exDocumentoDTO.setIdFormaDoc(exDocumentoDTO.getFormaDocPorTipo().getIdFormaDoc());
					forma = dao().consultar(exDocumentoDTO.getIdFormaDoc(), ExFormaDocumento.class, false);
				}
				
				// Fim -- Mudou origem? Escolhe um tipo automaticamente--------
				
				if (forma.getExModeloSet().size() == 0) {
					exDocumentoDTO.setIdMod(0L);
				}
			}
		}
		
		ExModelo mod = null;
		if (exDocumentoDTO.getIdMod() != null && exDocumentoDTO.getIdMod() != 0) {
			mod = dao().consultar(exDocumentoDTO.getIdMod(), ExModelo.class, false);
		}
		if (mod != null) {
			mod = mod.getModeloAtual();
		}
		
		List<ExModelo> modelos = getModelos(exDocumentoDTO);
		if (mod == null || !modelos.contains(mod)) {
			mod = (ExModelo) (modelos.toArray())[0];
			
			for (ExModelo modeloAtual : modelos) {
				if (modeloAtual.getIdMod() != null && modeloAtual.getIdMod() != 0
						&& modeloAtual.getNmMod().equals(modeloAtual.getExFormaDocumento().getDescricao())) {
					mod = modeloAtual;
					break;
				}
			}
			
			exDocumentoDTO.setIdMod(mod.getIdMod());
			if ((exDocumentoDTO.getIdMod() != 0) && (exDocumentoDTO.getMobilPaiSel().getId() == null)
					&& (exDocumentoDTO.getIdMobilAutuado() == null))
				exDocumentoDTO.getClassificacaoSel().apagar();
		}
		
		if (getPreenchimentos(exDocumentoDTO).size() <= 1) {
			exDocumentoDTO.setPreenchimento(0L);
		}
		
		if (exDocumentoDTO.isAlterouModelo() && exDocumentoDTO.getMobilPaiSel().getId() == null
				&& exDocumentoDTO.getIdMobilAutuado() == null)
			exDocumentoDTO.getClassificacaoSel().apagar();
		
		boolean naLista = false;
		final Set<ExPreenchimento> preenchimentos = getPreenchimentos(exDocumentoDTO);
		if (preenchimentos != null && preenchimentos.size() > 0) {
			for (ExPreenchimento exp : preenchimentos) {
				if (exp.getIdPreenchimento().equals(exDocumentoDTO.getPreenchimento())) {
					naLista = true;
					break;
				}
			}
			if (!naLista)
				exDocumentoDTO.setPreenchimento(((ExPreenchimento) (preenchimentos.toArray())[0]).getIdPreenchimento());
		}
		
		exDocumentoDTO.setModelo(mod);
		if (mod.getExClassificacao() != null
				&& mod.getExClassificacao().getId() != exDocumentoDTO.getClassificacaoSel().getId()) {
			exDocumentoDTO.getClassificacaoSel().buscarPorObjeto(mod.getExClassificacao());

		}
		
	}
	
	private void escreverForm(ExDocumentoDTO exDocumentoDTO) throws IllegalAccessException, NoSuchMethodException,
			AplicacaoException, InvocationTargetException {
		ExDocumento doc = exDocumentoDTO.getDoc();
		
		// Destino , Origem
		DpLotacao backupLotaTitular = getLotaTitular();
		DpPessoa backupTitular = getTitular();
		DpPessoa backupCadastrante = getCadastrante();
		
		BeanUtils.copyProperties(exDocumentoDTO, doc);
		
		setTitular(backupTitular);
		setLotaTitular(backupLotaTitular);
		// Orlando: Inclusão da linha, abaixo, para preservar o cadastrante do
		// ambiente.
		setCadastrante(backupCadastrante);
		
		if (doc.getConteudoBlob("doc.htm") != null)
			exDocumentoDTO.setConteudo(new String(doc.getConteudoBlob("doc.htm")));
		
		exDocumentoDTO.setIdTpDoc(doc.getExTipoDocumento().getIdTpDoc());
		exDocumentoDTO.setNivelAcesso(doc.getIdExNivelAcesso());
		
		if (doc.getExFormaDocumento() != null) {
			exDocumentoDTO.setIdFormaDoc(doc.getExFormaDocumento().getIdFormaDoc());
		}
		
		ExClassificacao classif = doc.getExClassificacaoAtual();
		if (classif != null)
			exDocumentoDTO.getClassificacaoSel().buscarPorObjeto(classif.getAtual());
		exDocumentoDTO.getSubscritorSel().buscarPorObjeto(doc.getSubscritor());
		// form.getDestinatarioSel().buscarPorObjeto(doc.getDestinatario());
		if (doc.getExModelo() != null) {
			ExModelo modeloAtual = doc.getExModelo().getModeloAtual();
			if (modeloAtual != null) {
				exDocumentoDTO.setIdMod(modeloAtual.getIdMod());
			}
		}
		
		if (doc.getDestinatario() != null) {
			exDocumentoDTO.getDestinatarioSel().buscarPorObjeto(doc.getDestinatario());
			exDocumentoDTO.setTipoDestinatario(1);
		}
		if (doc.getLotaDestinatario() != null) {
			exDocumentoDTO.getLotacaoDestinatarioSel().buscarPorObjeto(doc.getLotaDestinatario());
			if (doc.getDestinatario() == null)
				exDocumentoDTO.setTipoDestinatario(2);
		}
		
		if (doc.getExMobilPai() != null) {
			exDocumentoDTO.getMobilPaiSel().buscarPorObjeto(doc.getExMobilPai());
		}
		
		if (doc.getTitular() != null && doc.getSubscritor() != null
				&& !doc.getTitular().getIdPessoa().equals(doc.getSubscritor().getIdPessoa())) {
			exDocumentoDTO.getTitularSel().buscarPorObjeto(doc.getTitular());
			exDocumentoDTO.setSubstituicao(true);
		}
		
		// TODO Verificar se ha realmente a necessidade de setar novamente o
		// nível de acesso do documento
		// tendo em vista que o nível de acesso já foi setado anteriormente
		// neste mesmo método sem que o documento fosse alterado
		exDocumentoDTO.setNivelAcesso(doc.getIdExNivelAcesso());
		
		if (doc.getOrgaoExternoDestinatario() != null) {
			exDocumentoDTO.getOrgaoExternoDestinatarioSel().buscarPorObjeto(doc.getOrgaoExternoDestinatario());
			exDocumentoDTO.setTipoDestinatario(3);
		}
		if (doc.getNmOrgaoExterno() != null && !doc.getNmOrgaoExterno().equals("")) {
			exDocumentoDTO.setTipoDestinatario(3);
		}
		if (doc.getNmDestinatario() != null) {
			exDocumentoDTO.setNmDestinatario(doc.getNmDestinatario());
			exDocumentoDTO.setTipoDestinatario(4);
		}
		
		if (doc.getOrgaoExterno() != null) {
			exDocumentoDTO.getCpOrgaoSel().buscarPorObjeto(doc.getOrgaoExterno());
			exDocumentoDTO.setIdTpDoc(3L);
		}
		
		if (doc.getObsOrgao() != null) {
			exDocumentoDTO.setObsOrgao(doc.getObsOrgao());
		}
		
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			exDocumentoDTO.setDtDocString(df.format(doc.getDtDoc()));
		} catch (final Exception e) {
		}
		
		try {
			exDocumentoDTO.setDtDocOriginalString(df.format(doc.getDtDocOriginal()));
		} catch (final Exception e) {
		}
		
		if (doc.getAnoEmissao() != null)
			exDocumentoDTO.setAnoEmissaoString(doc.getAnoEmissao().toString());
		
		exDocumentoDTO.setEletronico(doc.isEletronico() ? 1 : 2);
		
	}
	
	private byte[] getByteArrayFormPreenchimento(String[] vars, String[] campos) throws Exception {
		ByteArrayOutputStream baos = null;
		String[] aVars = vars;
		String[] aCampos = campos;
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
			for (final String par : aFinal) {
				String s = "exDocumentoDTO.".concat(par);
				if (param(s) == null){
					s = par;
				}
				if (param(s) != null && !param(s).trim().equals("") && !s.trim().equals("exDocumentoDTO.preenchimento")
						&& !param(s).matches("[0-9]{2}/[0-9]{2}/[0-9]{4}")/*
																		   * s.trim ( ). equals ( "dtDocString" )
																		   */) {
					if (baos.size() > 0)
						baos.write('&');
					baos.write(par.getBytes());
					baos.write('=');
					
					// Deveria estar gravando como UTF-8
					baos.write(URLEncoder.encode(param(s), "iso-8859-1").getBytes());
				}
			}
		}
		return baos.toByteArray();
	}
	
	private void lerEntrevista(ExDocumentoDTO exDocumentoDTO) {
		if (exDocumentoDTO.getDoc().getExModelo() != null) {
			final byte[] form = exDocumentoDTO.getDoc().getConteudoBlob("doc.form");
			if (form != null) {
				final String as[] = new String(form).split("&");
				for (final String s : as) {
					final String param[] = s.split("=");
					try {
						if (param.length == 2)
							exDocumentoDTO.getParamsEntrevista().put(param[0], URLDecoder.decode(param[1], "iso-8859-1"));

						// setParam(param[0], URLDecoder.decode(param[1],
						// "iso-8859-1"));
					} catch (final UnsupportedEncodingException e) {
					}
				}
			}
		}
	}
	

	private void lerForm(ExDocumentoDTO exDocumentoDTO, String[] vars) throws IllegalAccessException, NoSuchMethodException, AplicacaoException {
		
		if (exDocumentoDTO.getAnexar()) {
			exDocumentoDTO.getDoc().setConteudoTpDoc(exDocumentoDTO.getConteudoTpDoc());
			exDocumentoDTO.getDoc().setNmArqDoc(exDocumentoDTO.getNmArqDoc());
		}
		
		// BeanUtils.copyProperties(doc, form);
		// fabrica = DaoFactory.getDAOFactory();
		exDocumentoDTO.getDoc().setDescrDocumento(exDocumentoDTO.getDescrDocumento());
		exDocumentoDTO.getDoc().setNmSubscritorExt(exDocumentoDTO.getNmSubscritorExt());
		exDocumentoDTO.getDoc().setNmFuncaoSubscritor(exDocumentoDTO.getNmFuncaoSubscritor());
		exDocumentoDTO.getDoc().setNumExtDoc(exDocumentoDTO.getNumExtDoc());
		exDocumentoDTO.getDoc().setNumAntigoDoc(exDocumentoDTO.getNumAntigoDoc());
		exDocumentoDTO.getDoc().setObsOrgao(exDocumentoDTO.getObsOrgao());
		exDocumentoDTO.getDoc().setEletronico(exDocumentoDTO.getEletronico() == 1 ? true : false);
		exDocumentoDTO.getDoc().setNmOrgaoExterno(exDocumentoDTO.getNmOrgaoExterno());
		exDocumentoDTO.getDoc().setDescrClassifNovo(exDocumentoDTO.getDescrClassifNovo());		
		exDocumentoDTO.getDoc().setExNivelAcesso(dao().consultar(exDocumentoDTO.getNivelAcesso(), ExNivelAcesso.class, false));		
		exDocumentoDTO.getDoc().setExTipoDocumento(dao().consultar(exDocumentoDTO.getIdTpDoc(), ExTipoDocumento.class, false));
		
		if (!exDocumentoDTO.getDoc().isFinalizado())
			exDocumentoDTO.getDoc().setExFormaDocumento(dao().consultar(exDocumentoDTO.getIdFormaDoc(), ExFormaDocumento.class, false));
		exDocumentoDTO.getDoc().setNmDestinatario(exDocumentoDTO.getNmDestinatario());
		
		exDocumentoDTO.getDoc().setExModelo(null);
		if (exDocumentoDTO.getIdMod() != 0) {
			ExModelo modelo = dao().consultar(exDocumentoDTO.getIdMod(), ExModelo.class, false);
			if (modelo != null)
				exDocumentoDTO.getDoc().setExModelo(modelo.getModeloAtual());
		}
		
		if (exDocumentoDTO.getClassificacaoSel().getId() != null && exDocumentoDTO.getClassificacaoSel().getId() != 0) {
			
			ExClassificacao classificacao = dao().consultar(exDocumentoDTO.getClassificacaoSel().getId(), ExClassificacao.class, 
					false);

			
			if (classificacao != null && !classificacao.isFechada())
				exDocumentoDTO.getDoc().setExClassificacao(classificacao);
			else {
				exDocumentoDTO.getDoc().setExClassificacao(null);
				exDocumentoDTO.getClassificacaoSel().apagar();
			}
			
		} else
			exDocumentoDTO.getDoc().setExClassificacao(null);
		if (exDocumentoDTO.getCpOrgaoSel().getId() != null) {
			exDocumentoDTO.getDoc().setOrgaoExterno(dao().consultar(exDocumentoDTO.getCpOrgaoSel().getId(), CpOrgao.class, false));
		} else
			exDocumentoDTO.getDoc().setOrgaoExterno(null);
		
		// Orlando: Alterei o IF abaixo incluindo a instrução
		// "doc.setLotaCadastrante(getLotaTitular());".
		// Esta linha estava "solta",após o IF, e era executada sempre.
		// Fiz esta modificação porque esta linha alterava a lotação do
		// cadastrante, não permitindo que este,
		// ao preencher o campo subscritor com a matrícula de outro usuário,
		// tivesse acesso ao documento.
		
		if (exDocumentoDTO.getDoc().getCadastrante() == null) {
			exDocumentoDTO.getDoc().setCadastrante(getCadastrante());
			exDocumentoDTO.getDoc().setLotaCadastrante(getLotaTitular());
		}
		
		if (exDocumentoDTO.getDoc().getLotaCadastrante() == null)
			exDocumentoDTO.getDoc().setLotaCadastrante(exDocumentoDTO.getDoc().getCadastrante().getLotacao());
		if (exDocumentoDTO.getSubscritorSel().getId() != null) {
			exDocumentoDTO.getDoc().setSubscritor(daoPes(exDocumentoDTO.getSubscritorSel().getId()));
			exDocumentoDTO.getDoc().setLotaSubscritor(exDocumentoDTO.getDoc().getSubscritor().getLotacao());
		} else {
			exDocumentoDTO.getDoc().setSubscritor(null);
		}
		
		if (exDocumentoDTO.isSubstituicao()) {
			if (exDocumentoDTO.getTitularSel().getId() != null) {
				exDocumentoDTO.getDoc().setTitular(daoPes(exDocumentoDTO.getTitularSel().getId()));
				exDocumentoDTO.getDoc().setLotaTitular(exDocumentoDTO.getDoc().getTitular().getLotacao());
			} else {
				exDocumentoDTO.getDoc().setTitular(exDocumentoDTO.getDoc().getSubscritor());
				exDocumentoDTO.getDoc().setLotaTitular(exDocumentoDTO.getDoc().getLotaSubscritor());
			}
		} else {
			exDocumentoDTO.getDoc().setTitular(exDocumentoDTO.getDoc().getSubscritor());
			exDocumentoDTO.getDoc().setLotaTitular(exDocumentoDTO.getDoc().getLotaSubscritor());
		}
		
		if (exDocumentoDTO.getDestinatarioSel().getId() != null) {
			exDocumentoDTO.getDoc().setDestinatario(daoPes(exDocumentoDTO.getDestinatarioSel().getId()));
			exDocumentoDTO.getDoc().setLotaDestinatario(daoPes(exDocumentoDTO.getDestinatarioSel().getId()).getLotacao());
			exDocumentoDTO.getDoc().setOrgaoExternoDestinatario(null);
		} else {
			exDocumentoDTO.getDoc().setDestinatario(null);
			if (exDocumentoDTO.getLotacaoDestinatarioSel().getId() != null) {
				exDocumentoDTO.getDoc().setLotaDestinatario(daoLot(exDocumentoDTO.getLotacaoDestinatarioSel().getId()));
				exDocumentoDTO.getDoc().setOrgaoExternoDestinatario(null);
			} else {
				exDocumentoDTO.getDoc().setLotaDestinatario(null);

				if (exDocumentoDTO.getOrgaoExternoDestinatarioSel().getId() != null) {
					exDocumentoDTO.getDoc().setOrgaoExternoDestinatario(dao().consultar(exDocumentoDTO.getOrgaoExternoDestinatarioSel().getId(), 
							CpOrgao.class, false));

				} else {
					exDocumentoDTO.getDoc().setOrgaoExternoDestinatario(null);
				}
			}
		}
		
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			exDocumentoDTO.getDoc().setDtDoc(df.parse(exDocumentoDTO.getDtDocString()));
		} catch (final ParseException e) {
			exDocumentoDTO.getDoc().setDtDoc(null);
		} catch (final NullPointerException e) {
			exDocumentoDTO.getDoc().setDtDoc(null);
		}
		if (exDocumentoDTO.getDoc().getDtRegDoc() == null)
			exDocumentoDTO.getDoc().setDtRegDoc(dao().dt());
		
		try {
			exDocumentoDTO.getDoc().setDtDocOriginal(df.parse(exDocumentoDTO.getDtDocOriginalString()));
		} catch (final ParseException e) {
			exDocumentoDTO.getDoc().setDtDocOriginal(null);
		} catch (final NullPointerException e) {
			exDocumentoDTO.getDoc().setDtDocOriginal(null);
		}
		
		if (exDocumentoDTO.getNumExpediente() != null) {
			exDocumentoDTO.getDoc().setNumExpediente(new Long(exDocumentoDTO.getNumExpediente()));
			exDocumentoDTO.getDoc().setAnoEmissao(new Long(exDocumentoDTO.getAnoEmissaoString()));
		}
		
		if (exDocumentoDTO.getMobilPaiSel().getId() != null) {
			exDocumentoDTO.getDoc().setExMobilPai(dao().consultar(exDocumentoDTO.getMobilPaiSel().getId(), ExMobil.class, false));
		} else {
			exDocumentoDTO.getDoc().setExMobilPai(null);
		}
		
		try {
			ByteArrayOutputStream baos;
			
			final String marcacoes[] = { "<!-- INICIO NUMERO -->", "<!-- FIM NUMERO -->", "<!-- INICIO NUMERO", "FIM NUMERO -->",
					"<!-- INICIO TITULO", "FIM TITULO -->", "<!-- INICIO MIOLO -->", "<!-- FIM MIOLO -->",
					"<!-- INICIO CORPO -->", "<!-- FIM CORPO -->", "<!-- INICIO CORPO", "FIM CORPO -->",
					"<!-- INICIO ASSINATURA -->", "<!-- FIM ASSINATURA -->", "<!-- INICIO ABERTURA -->", "<!-- FIM ABERTURA -->",
					"<!-- INICIO ABERTURA", "FIM ABERTURA -->", "<!-- INICIO FECHO -->", "<!-- FIM FECHO -->" };
			
			final String as[] = vars;
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
						
						baos.write(URLEncoder.encode(parametro, "iso-8859-1").getBytes());
					}
				}
				exDocumentoDTO.getDoc().setConteudoTpDoc("application/zip");
				exDocumentoDTO.getDoc().setConteudoBlobForm(baos.toByteArray());
				
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
		while (offset < meuByteArray.length && (numRead = is.read(meuByteArray, offset, meuByteArray.length - offset)) >= 0) {
			offset += numRead;
		}
		
		// Ensure all the bytes have been read in
		if (offset < meuByteArray.length)
			throw new IOException("Não foi possível ler o arquivo completamente " + file.getName());
		
		// Close the input stream and return bytes
		is.close();
		return meuByteArray;
	}
	
	public List<ExModelo> getModelos(ExDocumentoDTO exDocumentoDTO) throws Exception {
		if (exDocumentoDTO.getModelos() != null)
			return exDocumentoDTO.getModelos();
		
		ExFormaDocumento forma = null;
		if (exDocumentoDTO.getIdFormaDoc() != null && exDocumentoDTO.getIdFormaDoc() != 0)
			forma = dao().consultar(exDocumentoDTO.getIdFormaDoc(), ExFormaDocumento.class, false);
		
		String headerValue = null;
		if (exDocumentoDTO.getTipoDocumento() != null && exDocumentoDTO.getTipoDocumento().equals("antigo"))
			headerValue = "Não Informado";
		
		exDocumentoDTO.setModelos(Ex
				.getInstance()
				.getBL()
				.obterListaModelos(forma, exDocumentoDTO.getDespachando(), headerValue, true, getTitular(), getLotaTitular(), 
						exDocumentoDTO.getAutuando()));
		return exDocumentoDTO.getModelos();

		
	}
	
	public List<ExFormaDocumento> getFormasDocPorTipo(ExDocumentoDTO exDocumentoDTO) throws Exception {
		if (exDocumentoDTO.getFormasDoc() == null) {
			exDocumentoDTO.setFormasDoc(new ArrayList<ExFormaDocumento>());
			ExBL bl = Ex.getInstance().getBL();

			exDocumentoDTO.getFormasDoc().addAll(
					bl.obterFormasDocumento(bl.obterListaModelos(null, exDocumentoDTO.getDespachando(), null, true, getTitular(), 
							getLotaTitular(), exDocumentoDTO.getAutuando()), exDocumentoDTO.getDoc().getExTipoDocumento(), null));

		}
		
		return exDocumentoDTO.getFormasDoc();
	}
	
	public Set<ExPreenchimento> getPreenchimentos(ExDocumentoDTO exDocumentoDTO) throws AplicacaoException {
		if (exDocumentoDTO.getPreenchSet() != null)
			return exDocumentoDTO.getPreenchSet();
		
		exDocumentoDTO.setPreenchSet(new LinkedHashSet<ExPreenchimento>());
		if (exDocumentoDTO.getIdFormaDoc() == null || exDocumentoDTO.getIdFormaDoc() == 0)
			return exDocumentoDTO.getPreenchSet();
		
		ExPreenchimento preench = new ExPreenchimento();
		if (exDocumentoDTO.getIdMod() != null && exDocumentoDTO.getIdMod() != 0L)
			preench.setExModelo(dao().consultar(exDocumentoDTO.getIdMod(), ExModelo.class, false));
		
		DpLotacao lota = new DpLotacao();
		lota.setIdLotacaoIni(getLotaTitular().getIdLotacaoIni());
		List<DpLotacao> lotacaoSet = dao().consultar(lota, null);
		
		exDocumentoDTO.getPreenchSet().add(new ExPreenchimento(0, null, " [Em branco] ", null));
		
		if (exDocumentoDTO.getIdMod() != null && exDocumentoDTO.getIdMod() != 0) {
			for (DpLotacao lotacao : lotacaoSet) {
				preench.setDpLotacao(lotacao);
				exDocumentoDTO.getPreenchSet().addAll(dao().consultar(preench));
			}
		}
		
		return exDocumentoDTO.getPreenchSet();
	}
	
	public List<ExNivelAcesso> getListaNivelAcesso(ExDocumentoDTO exDocumentoDTO) throws Exception {
		ExFormaDocumento exForma = new ExFormaDocumento();
		ExClassificacao exClassif = new ExClassificacao();
		ExTipoDocumento exTipo = new ExTipoDocumento();
		ExModelo exMod = new ExModelo();
		
		if (exDocumentoDTO.getIdTpDoc() != null) {
			exTipo = dao().consultar(exDocumentoDTO.getIdTpDoc(), ExTipoDocumento.class, false);
		}
		
		if (exDocumentoDTO.getIdFormaDoc() != null) {
			exForma = dao().consultar(exDocumentoDTO.getIdFormaDoc(), ExFormaDocumento.class, false);
		}
		
		if (exDocumentoDTO.getIdMod() != null && exDocumentoDTO.getIdMod() != 0) {
			exMod = dao().consultar(exDocumentoDTO.getIdMod(), ExModelo.class, false);
		}
		
		if (exDocumentoDTO.getClassificacaoSel().getId() != null) {
			exClassif = dao().consultar(exDocumentoDTO.getClassificacaoSel().getId(), ExClassificacao.class, false);
		}
		
		return getListaNivelAcesso(exTipo, exForma, exMod, exClassif);
	}
	
	public ExNivelAcesso getNivelAcessoDefault(ExDocumentoDTO exDocumentoDTO) throws Exception {
		ExFormaDocumento exForma = new ExFormaDocumento();
		ExClassificacao exClassif = new ExClassificacao();
		ExTipoDocumento exTipo = new ExTipoDocumento();
		ExModelo exMod = new ExModelo();
		
		if (exDocumentoDTO.getIdTpDoc() != null) {
			exTipo = dao().consultar(exDocumentoDTO.getIdTpDoc(), ExTipoDocumento.class, false);
		}
		
		if (exDocumentoDTO.getIdFormaDoc() != null) {
			exForma = dao().consultar(exDocumentoDTO.getIdFormaDoc(), ExFormaDocumento.class, false);
		}
		
		if (exDocumentoDTO.getIdMod() != null) {
			exMod = dao().consultar(exDocumentoDTO.getIdMod(), ExModelo.class, false);
		}
		
		if (exDocumentoDTO.getClassificacaoSel().getId() != null) {
			exClassif = dao().consultar(exDocumentoDTO.getClassificacaoSel().getId(), ExClassificacao.class, false);
		}
		
		return getNivelAcessoDefault(exTipo, exForma, exMod, exClassif);
	}
	
	public Map<Integer, String> getListaVias() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		for (byte k = 1; k <= 20; k++) {
			final byte[] k2 = { (byte) (k + 64) };
			map.put(new Integer(k), new String(k2));
		}
		
		return map;
	}
	
	public List<ExFormaDocumento> getFormasDocumento() throws Exception {
		List<ExFormaDocumento> formasSet = dao().listarExFormasDocumento();
		ArrayList<ExFormaDocumento> formasFinal = new ArrayList<ExFormaDocumento>();
		for (ExFormaDocumento forma : formasSet) {
			if (Ex.getInstance().getConf()
					.podePorConfiguracao(getTitular(), getLotaTitular(), forma, CpTipoConfiguracao.TIPO_CONFIG_CRIAR))
				formasFinal.add(forma);
		}
		return formasFinal;
		
	}
	
	public List<ExDocumento> getListaDocsAPublicarBoletim() {
		return FuncoesEL.listaDocsAPublicarBoletim(getLotaTitular().getOrgaoUsuario());
	}
	
	public HierarquizadorBoletimInterno getHierarquizadorBIE() {
		return new HierarquizadorBoletimInterno(getLotaTitular().getOrgaoUsuario());
	}	

	public static void redirecionarParaExibir(Result result, String sigla) {
		result.redirectTo(MessageFormat.format(URL_EXIBIR, sigla));
	}
}
