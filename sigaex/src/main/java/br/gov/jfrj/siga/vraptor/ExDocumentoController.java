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
import javax.servlet.http.HttpServletResponse;

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
	
	public String aAlterarPreenchimento(ExDocumentoDTO exDocumentoDto) throws Exception {
		
		ExPreenchimento exPreenchimento = new ExPreenchimento();
		
		dao().iniciarTransacao();
		exPreenchimento.setIdPreenchimento(exDocumentoDto.getPreenchimento());
		exPreenchimento = dao().consultar(exDocumentoDto.getPreenchimento(), ExPreenchimento.class, false);
		
		exPreenchimento.setPreenchimentoBA(getByteArrayFormPreenchimento());
		dao().gravar(exPreenchimento);
		dao().commitTransacao();
		
		exDocumentoDto.setPreenchimento(exPreenchimento.getIdPreenchimento());
		
		String url = getUrlEncodedParameters();
		if (url.indexOf("preenchimento") >= 0) {
			String parte1 = url.substring(0, url.indexOf("preenchimento"));
			String parte2 = url.substring(url.indexOf("&", url.indexOf("&preenchimento") + 1) + 1);
			parte2 = parte2 + "&preenchimento=" + exDocumentoDto.getPreenchimento();
			exDocumentoDto.setPreenchRedirect(parte1 + parte2);
		} else
			exDocumentoDto.setPreenchRedirect(getUrlEncodedParameters());
		
		return edita(exDocumentoDto).toString();
	}
	
	public String aAnexo(ExDocumentoDTO exDocumentoDto) throws Exception {
		buscarDocumento(true, exDocumentoDto);
		return Action.SUCCESS;
	}
	
	public String aCancelarDocumento(ExDocumentoDTO exDocumentoDto) throws Exception {
		buscarDocumento(true, exDocumentoDto);
		
		try {
			Ex.getInstance().getBL().cancelarDocumento(getCadastrante(), getLotaTitular(), exDocumentoDto.getDoc());
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
	
	public String aCarregarPreenchimento(ExDocumentoDTO exDocumentoDto) throws Exception {
		ExPreenchimento exPreenchimento = new ExPreenchimento();
		
		// Obtém arrStrBanco[], com os parâmetros vindos do banco
		exPreenchimento = dao().consultar(exDocumentoDto.getPreenchimento(), ExPreenchimento.class, false);
		String strBanco = new String(exPreenchimento.getPreenchimentoBA());
		String arrStrBanco[] = strBanco.split("&");
		String strBancoLimpa = new String();
		
		// seta os atributos da action com base nos valores do banco, fazendo o
		// decoding da string
		for (String elem : arrStrBanco) {
			String[] paramNameAndValue = ((String) elem).split("=");
			String paramName = paramNameAndValue[0];
			String paramValue = paramNameAndValue[1];
			String paramValueDecoded = URLDecoder.decode(paramValue, "ISO-8859-1");
			String paramValueEncodedUTF8 = URLEncoder.encode(paramValueDecoded, "UTF-8");
			try {
				if (!paramName.contains("Sel.id")) {
					final String mName = "set" + paramName.substring(0, 1).toUpperCase() + paramName.substring(1);
					if (getPar().get(paramName) != null || (paramName.contains("nmOrgaoExterno"))
							|| (paramName.contains("nmDestinatario"))) {
						Class paramType = this.getClass().getDeclaredField(paramName).getType();
						Constructor paramTypeContructor = paramType.getConstructor(new Class[] { String.class });
						final Method method = this.getClass().getMethod(mName, new Class[] { paramType });
						method.invoke(this,
								new Object[] { paramTypeContructor.newInstance(new Object[] { (paramValueDecoded) }) });
					}
				} else {
					final String mName = "get" + paramName.substring(0, 1).toUpperCase()
							+ paramName.substring(1, paramName.indexOf(".id"));
					if (getPar().get(paramName) != null || (paramName.contains("estinatarioSel.id"))) {
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
				if (arrStrURL2.length > 1 && !arrStrURL2[0].contains(".sigla") && !arrStrURL2[0].contains(".descricao")
						&& !strBanco.contains(arrStrURL2[0] + "="))
					strURLLimpa = strURLLimpa + s + "&";
			}
		}
		
		exDocumentoDto.setPreenchRedirect(strURLLimpa + strBancoLimpa);
		
		return edita(exDocumentoDto).toString();
	}
	
	public String aCriarVia(ExDocumentoDTO exDocumentoDto) throws Exception {
		buscarDocumento(true, exDocumentoDto);
		
		if (!Ex.getInstance().getComp().podeCriarVia(getTitular(), getLotaTitular(), exDocumentoDto.getMob()))
			throw new AplicacaoException("Não é possível criar vias neste documento");
		try {
			Ex.getInstance().getBL().criarVia(getCadastrante(), getLotaTitular(), exDocumentoDto.getDoc());
		} catch (final Exception e) {
			throw e;
		}
		
		return Action.SUCCESS;
	}
	
	public String aCriarVolume(ExDocumentoDTO exDocumentoDto) throws Exception {
		buscarDocumento(true, exDocumentoDto);
		
		if (!Ex.getInstance().getComp().podeCriarVolume(getTitular(), getLotaTitular(), exDocumentoDto.getMob()))
			throw new AplicacaoException("Não é possível criar volumes neste documento");
		try {
			Ex.getInstance().getBL().criarVolume(getCadastrante(), getLotaTitular(), exDocumentoDto.getDoc());
		} catch (final Exception e) {
			throw e;
		}
		
		return Action.SUCCESS;
	}
	
	@Get("app/expediente/doc/editar")
	public ExDocumentoDTO edita(ExDocumentoDTO exDocumentoDto) throws Exception {
		
		if (exDocumentoDto == null) {
			exDocumentoDto = new ExDocumentoDTO();
		}
		
		buscarDocumentoOuNovo(true, exDocumentoDto);
		
		if ((getPostback() == null) || (param("docFilho") != null)) {
			exDocumentoDto.setTipoDestinatario(2);
			exDocumentoDto.setIdFormaDoc(2);
			exDocumentoDto.setIdTpDoc(1L);
			
			ExNivelAcesso nivelDefault = getNivelAcessoDefault(exDocumentoDto);
			if (nivelDefault != null) {
				exDocumentoDto.setNivelAcesso(nivelDefault.getIdNivelAcesso());
			} else
				exDocumentoDto.setNivelAcesso(1L);
			
			exDocumentoDto.setIdMod(((ExModelo) dao().consultarAtivoPorIdInicial(ExModelo.class, 26L)).getIdMod());
		}
		
		if (exDocumentoDto.isCriandoAnexo() && exDocumentoDto.getId() == null && getPostback() == null) {
			exDocumentoDto.setIdFormaDoc(60);
			exDocumentoDto.setIdMod(((ExModelo) dao().consultarAtivoPorIdInicial(ExModelo.class, 507L)).getIdMod());
		}
		
		if (exDocumentoDto.getDespachando() && exDocumentoDto.getId() == null
				&& (getPostback() == null || getPostback() == 0)) {
			
			exDocumentoDto.setIdFormaDoc(8);
			
		}
		
		if (exDocumentoDto.getId() == null && exDocumentoDto.getDoc() != null)
			exDocumentoDto.setId(exDocumentoDto.getDoc().getIdDoc());
		
		if (exDocumentoDto.getId() == null) {
			if (getLotaTitular().isFechada())
				throw new AplicacaoException("A lotação " + getLotaTitular().getSiglaLotacao() + " foi extinta em "
						+ new SimpleDateFormat("dd/MM/yyyy").format(getLotaTitular().getDataFimLotacao())
						+ ". Não é possível gerar expedientes em lotação extinta.");
			exDocumentoDto.setDoc(new ExDocumento());
			exDocumentoDto.getDoc().setOrgaoUsuario(getTitular().getOrgaoUsuario());
		} else {
			exDocumentoDto.setDoc(daoDoc(exDocumentoDto.getId()));
			
			if (!Ex.getInstance().getComp().podeEditar(getTitular(), getLotaTitular(), exDocumentoDto.getMob()))
				throw new AplicacaoException("Não é permitido editar documento fechado");
			
			if (getPostback() == null) {
				escreverForm(exDocumentoDto);
				lerEntrevista(exDocumentoDto);
			}
		}
		
		if (exDocumentoDto.getTipoDocumento() != null && exDocumentoDto.getTipoDocumento().equals("externo")) {
			exDocumentoDto.setIdMod(((ExModelo) dao().consultarAtivoPorIdInicial(ExModelo.class, 28L)).getIdMod());
		}
		carregarBeans(exDocumentoDto);
		
		Long idSit = Ex
				.getInstance()
				.getConf()
				.buscaSituacao(exDocumentoDto.getModelo(), exDocumentoDto.getDoc().getExTipoDocumento(), getTitular(),
						getLotaTitular(), CpTipoConfiguracao.TIPO_CONFIG_ELETRONICO).getIdSitConfiguracao();
		
		if (idSit == ExSituacaoConfiguracao.SITUACAO_OBRIGATORIO) {
			exDocumentoDto.setEletronico(1);
			exDocumentoDto.setEletronicoFixo(true);
		} else if (idSit == ExSituacaoConfiguracao.SITUACAO_PROIBIDO) {
			exDocumentoDto.setEletronico(2);
			exDocumentoDto.setEletronicoFixo(true);
		} else if (idSit == ExSituacaoConfiguracao.SITUACAO_DEFAULT
				&& (exDocumentoDto.getEletronico() == null || exDocumentoDto.getEletronico() == 0)) {
			exDocumentoDto.setEletronico(1);
		} else if (exDocumentoDto.isAlterouModelo()) {
			if (idSit == ExSituacaoConfiguracao.SITUACAO_DEFAULT) {
				exDocumentoDto.setEletronico(1);
			} else {
				exDocumentoDto.setEletronicoFixo(false);
				exDocumentoDto.setEletronico(0);
			}
		}
		
		lerForm(exDocumentoDto);
		
		// O (&& classif.getCodAssunto() != null) foi adicionado para permitir
		// que as classificações antigas, ainda não linkadas por equivalência,
		// possam ser usadas
		ExClassificacao classif = exDocumentoDto.getClassificacaoSel().buscarObjeto();
		if (classif != null && classif.getHisDtFim() != null && classif.getHisDtIni() != null
				&& classif.getCodAssunto() != null) {
			classif = ExDao.getInstance().consultarAtual(classif);
			if (classif != null)
				exDocumentoDto.getClassificacaoSel().setId(classif.getIdClassificacao());
			else
				exDocumentoDto.getClassificacaoSel().setId(null);
		}
		
		exDocumentoDto.getSubscritorSel().buscar();
		exDocumentoDto.getDestinatarioSel().buscar();
		exDocumentoDto.getLotacaoDestinatarioSel().buscar();
		exDocumentoDto.getOrgaoSel().buscar();
		exDocumentoDto.getOrgaoExternoDestinatarioSel().buscar();
		exDocumentoDto.getClassificacaoSel().buscar();
		exDocumentoDto.getMobilPaiSel().buscar();
		
		if (getRequest().getSession().getAttribute("preenchRedirect") != null) {
			exDocumentoDto.setPreenchRedirect((String) getRequest().getSession().getAttribute("preenchRedirect"));
			getRequest().getSession().removeAttribute("preenchRedirect");
		}
		
		registraErroExtEditor();
		
		// Usado pela extensão editor...
		getPar().put(
				"serverAndPort",
				new String[] { getRequest().getServerName()
						+ (getRequest().getServerPort() > 0 ? ":" + getRequest().getServerPort() : "") });
		
		// ...inclusive nas operações com preenchimento automático
		if (exDocumentoDto.getPreenchRedirect() != null && exDocumentoDto.getPreenchRedirect().length() > 2) {
			exDocumentoDto.setPreenchRedirect(exDocumentoDto.getPreenchRedirect() + "&serverAndPort="
					+ getPar().get("serverAndPort")[0]);
		}
		
		exDocumentoDto.setTiposDocumento(getTiposDocumento());
		exDocumentoDto.setListaNivelAcesso(getListaNivelAcesso(exDocumentoDto));
		exDocumentoDto.setFormasDoc(getFormasDocPorTipo(exDocumentoDto));
		exDocumentoDto.setModelos(getModelos(exDocumentoDto));
		getPreenchimentos(exDocumentoDto);
		
		result.include("possuiMaisQueUmModelo", (getModelos(exDocumentoDto).size() > 1));
		result.include("par", getPar());
		return exDocumentoDto;
	}
	
	public String aExcluir(ExDocumentoDTO exDocumentoDto) throws Exception {
		ExDocumento documento = null;
		final String sId = getRequest().getParameter("id");
		
		try {
			ExDao.iniciarTransacao();
			documento = daoDoc(Long.valueOf(sId));
			
			verificaNivelAcesso(exDocumentoDto.getDoc().getMobilGeral());
			
			// Testa se existe algum valor preenchido em documento.
			// Se não houver gera ObjectNotFoundException
			final Date d = documento.getDtRegDoc();
			
			if (documento.isFinalizado())
				
				throw new AplicacaoException("Documento já foi finalizado e não pode ser excluído");
			if (!Ex.getInstance().getComp().podeExcluir(getTitular(), getLotaTitular(), exDocumentoDto.getMob()))
				throw new AplicacaoException("Não é possível excluir");
			
			// Exclui documento da tabela de Boletim Interno
			String funcao = exDocumentoDto.getDoc().getForm().get("acaoExcluir");
			if (funcao != null) {
				obterMetodoPorString(funcao, exDocumentoDto.getDoc());
			}
			
			for (ExMovimentacao movRef : exDocumentoDto.getMob().getExMovimentacaoReferenciaSet())
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
		
		return Action.SUCCESS;
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
	
	public String aExcluirDocMovimentacoes(ExDocumentoDTO exDocumentoDto) throws Exception {
		ExDocumento doc = exDocumentoDto.getDoc();
		buscarDocumento(true, exDocumentoDto);
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
		} catch (final AplicacaoException e) {
			ExDao.rollbackTransacao();
			throw e;
		} catch (final Exception e) {
			ExDao.rollbackTransacao();
			throw new AplicacaoException("Ocorreu um Erro durante a Operação", 0, e);
		}
		
		return Action.SUCCESS;
	}
	
	public String aExcluirPreenchimento(ExDocumentoDTO exDocumentoDto) throws Exception {
		dao().iniciarTransacao();
		ExPreenchimento exemplo = dao().consultar(exDocumentoDto.getPreenchimento(), ExPreenchimento.class, false);
		dao().excluir(exemplo);
		// preenchDao.excluirPorId(preenchimento);
		dao().commitTransacao();
		exDocumentoDto.setPreenchimento(0L);
		return edita(exDocumentoDto).toString();
	}
	
	public String aTestarConexao() {
		return Action.SUCCESS;
	}
	
	public String aAcessar(ExDocumentoDTO exDocumentoDto) throws Exception {
		buscarDocumento(false, exDocumentoDto);
		
		assertAcesso(exDocumentoDto);
		
		return Action.SUCCESS;
	}
	
	private void assertAcesso(ExDocumentoDTO exDocumentoDto) throws Exception {
		String msgDestinoDoc = "";
		DpPessoa dest;
		if (!Ex.getInstance().getComp().podeAcessarDocumento(getTitular(), getLotaTitular(), exDocumentoDto.getMob())) {
			
			String s = "";
			try {
				s += exDocumentoDto.getMob().doc().getListaDeAcessosString();
				s = "(" + s + ")";
				s = " " + exDocumentoDto.getMob().doc().getExNivelAcesso().getNmNivelAcesso() + " " + s;
			} catch (Exception e) {
			}
			throw new AplicacaoException("Documento " + exDocumentoDto.getMob().getSigla() + " inacessível ao usuário "
					+ getTitular().getSigla() + "/" + getLotaTitular().getSiglaCompleta() + "." + s + " "
					+ msgDestinoDoc);
		}
	}
	
	public String aExibirAntigo(ExDocumentoDTO exDocumentoDto) throws Exception {
		buscarDocumento(false, exDocumentoDto);
		
		assertAcesso(exDocumentoDto);
		
		if (Ex.getInstance().getComp().podeReceberEletronico(getTitular(), getLotaTitular(), exDocumentoDto.getMob()))
			Ex.getInstance().getBL().receber(getCadastrante(), getLotaTitular(), exDocumentoDto.getMob(), new Date());
		
		ExDocumentoVO docVO = new ExDocumentoVO(exDocumentoDto.getDoc(), exDocumentoDto.getMob(), getTitular(),
				getLotaTitular(), true, true);
		super.getRequest().setAttribute("docVO", docVO);
		
		// logStatistics();
		
		if (exDocumentoDto.getMob().isEliminado())
			throw new AplicacaoException("Documento "
					+ exDocumentoDto.getMob().getSigla()
					+ " eliminado, conforme o termo "
					+ exDocumentoDto.getMob()
							.getUltimaMovimentacaoNaoCancelada(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ELIMINACAO)
							.getExMobilRef());
		
		return Action.SUCCESS;
	}
	
	@Get("/app/expediente/doc/exibir")
	public void aExibir(ExDocumentoDTO exDocumentoDto) throws Exception {
		ExDocumento doc = exDocumentoDto.getDoc();
		ExMobil mob = exDocumentoDto.getMob();
		
		buscarDocumento(false, exDocumentoDto);
		
		assertAcesso(exDocumentoDto);
		
		if (Ex.getInstance().getComp().podeReceberEletronico(getTitular(), getLotaTitular(), mob))
			Ex.getInstance().getBL().receber(getCadastrante(), getLotaTitular(), mob, new Date());
		
		if (mob == null || mob.isGeral()) {
			if (mob.getDoc().isFinalizado()) {
				if (doc.isProcesso())
					mob = doc.getUltimoVolume();
				else
					mob = doc.getPrimeiraVia();
			}
		}
		
		ExDocumentoVO docVO = new ExDocumentoVO(doc, mob, getTitular(), getLotaTitular(), true, false);
		
		docVO.exibe();
		
		// super.getRequest().setAttribute("docVO", docVO);
		result.include("docVO", docVO);
	}
	
	public String aCorrigirPDF(ExDocumentoDTO exDocumentoDto) throws Exception {
		if (exDocumentoDto.getSigla() != null) {
			final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
			filter.setSigla(exDocumentoDto.getSigla());
			exDocumentoDto.setMob((ExMobil) dao().consultarPorSigla(filter));
			
			Ex.getInstance().getBL().processar(exDocumentoDto.getMob().getExDocumento(), true, false, null);
		}
		return Action.SUCCESS;
	}
	
	private void logStatistics() {
		Statistics stats = ExDao.getInstance().getSessao().getSessionFactory().getStatistics();
		SessionStatistics statsSession = ExDao.getInstance().getSessao().getStatistics();
		
		double queryCacheHitCount = stats.getQueryCacheHitCount();
		double queryCacheMissCount = stats.getQueryCacheMissCount();
		double queryCacheHitRatio = queryCacheHitCount / (queryCacheHitCount + queryCacheMissCount);
		
		System.out.println("Query Hit ratio:" + queryCacheHitRatio);
		
		System.out.println(stats.getQueryExecutionMaxTimeQueryString() + " [time (ms): "
				+ stats.getQueryExecutionMaxTime() + "]");
		
		System.out.println("\n\n\n\n\n\n*****************************************\n\n\n\n\n\n\n");
		
		for (String query : stats.getQueries()) {
			QueryStatistics qs = stats.getQueryStatistics(query);
			System.out.println(query + " [time (ms): " + qs.getExecutionAvgTime() + ", count: "
					+ qs.getExecutionCount() + "]");
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
					.buscaSituacao(doc.getExModelo(), getTitular(), getLotaTitular(),
							CpTipoConfiguracao.TIPO_CONFIG_DESTINATARIO).getIdSitConfiguracao();
			if (idSit == ExSituacaoConfiguracao.SITUACAO_OBRIGATORIO)
				throw new AplicacaoException("Para documentos do modelo " + doc.getExModelo().getNmMod()
						+ ", é necessário definir um destinatário");
		}
		
		if (doc.getExClassificacao() == null)
			throw new AplicacaoException("É necessário informar a classificação documental.");
		
	}
	
	private void buscarDocumentoOuNovo(boolean fVerificarAcesso, ExDocumentoDTO exDocumentoDto) throws Exception {
		buscarDocumento(fVerificarAcesso, true, exDocumentoDto);
		ExDocumento doc = exDocumentoDto.getDoc();
		ExMobil mob = exDocumentoDto.getMob();
		if (doc == null) {
			doc = new ExDocumento();
			doc.setExTipoDocumento(dao()
					.consultar(ExTipoDocumento.TIPO_DOCUMENTO_INTERNO, ExTipoDocumento.class, false));
			mob = new ExMobil();
			mob.setExTipoMobil(dao().consultar(ExTipoMobil.TIPO_MOBIL_GERAL, ExTipoMobil.class, false));
			mob.setNumSequencia(1);
			mob.setExDocumento(doc);
			
			doc.setExMobilSet(new TreeSet<ExMobil>());
			doc.getExMobilSet().add(mob);
		}
	}
	
	private void buscarDocumento(boolean fVerificarAcesso, ExDocumentoDTO exDocumentoDto) throws Exception {
		buscarDocumento(fVerificarAcesso, false, exDocumentoDto);
	}
	
	private void buscarDocumento(boolean fVerificarAcesso, boolean fPodeNaoExistir, ExDocumentoDTO exDocumentoDto)
			throws Exception {
		ExDocumento doc = exDocumentoDto.getDoc();
		ExMobil mob = exDocumentoDto.getMob();
		String sigla = exDocumentoDto.getSigla();
		Long idMob = exDocumentoDto.getIdMob();
		if (mob == null && sigla != null && sigla.length() != 0) {
			final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
			filter.setSigla(sigla);
			mob = (ExMobil) dao().consultarPorSigla(filter);
			// bruno.lacerda@avantiprima.com.br
			if (mob != null) {
				doc = mob.getExDocumento();
				exDocumentoDto.setIdMob(mob.getId());
			}
		} else if (mob == null && exDocumentoDto.getDocumentoViaSel().getId() != null) {
			exDocumentoDto.setIdMob(exDocumentoDto.getDocumentoViaSel().getId());
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
	
	public String aFinalizar(ExDocumentoDTO exDocumentoDto) throws Exception {
		ExDocumento doc = exDocumentoDto.getDoc();
		ExMobil mob = exDocumentoDto.getMob();
		buscarDocumento(true, exDocumentoDto);
		
		verificaDocumento(doc);
		
		if (!Ex.getInstance().getComp().podeFinalizar(getTitular(), getLotaTitular(), mob))
			throw new AplicacaoException("Não é possível Finalizar");
		
		try {
			
			exDocumentoDto.setMsg(Ex.getInstance().getBL().finalizar(getCadastrante(), getLotaTitular(), doc, null));
			
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
	
	public String aFinalizarAssinar(ExDocumentoDTO exDocumentoDto) throws Exception {
		
		aFinalizar(exDocumentoDto);
		
		buscarDocumento(true, exDocumentoDto);
		
		return Action.SUCCESS;
	}
	
	public String aGravar(ExDocumentoDTO exDocumentoDto) throws Exception {
		ExDocumento doc = exDocumentoDto.getDoc();
		
		try {
			buscarDocumentoOuNovo(true, exDocumentoDto);
			if (doc == null)
				doc = new ExDocumento();
			
			long tempoIni = System.currentTimeMillis();
			
			if (!validar()) {
				edita(exDocumentoDto);
				getPar().put("alerta", new String[] { "Sim" });
				exDocumentoDto.setAlerta("Sim");
				return "form_incompleto";
			}
			
			ByteArrayOutputStream baos = null;
			
			lerForm(exDocumentoDto);
			
			if (!Ex.getInstance()
					.getConf()
					.podePorConfiguracao(getTitular(), getLotaTitular(), doc.getExTipoDocumento(),
							doc.getExFormaDocumento(), doc.getExModelo(),
							
							doc.getExClassificacaoAtual(), doc.getExNivelAcesso(), CpTipoConfiguracao.TIPO_CONFIG_CRIAR)) {
				
				if (!Ex.getInstance()
						.getConf()
						.podePorConfiguracao(getTitular(), getLotaTitular(), null, null, null,
								doc.getExClassificacao(), null, CpTipoConfiguracao.TIPO_CONFIG_CRIAR))
					throw new AplicacaoException("Usuário não possui permissão de criar documento da classificação "
							+ doc.getExClassificacao().getCodificacao());
				
				throw new AplicacaoException("Operação não permitida");
			}
			
			System.out.println("monitorando gravacao IDDoc " + doc.getIdDoc() + ", PESSOA "
					+ doc.getCadastrante().getIdPessoa() + ". Terminou verificacao de config PodeCriarModelo: "
					+ (System.currentTimeMillis() - tempoIni));
			tempoIni = System.currentTimeMillis();
			
			doc.setOrgaoUsuario(getLotaTitular().getOrgaoUsuario());
			
			if (exDocumentoDto.isClassificacaoIntermediaria()
					&& (exDocumentoDto.getDescrClassifNovo() == null || exDocumentoDto.getDescrClassifNovo().trim()
							.length() == 0))
				throw new AplicacaoException(
						"Quando a classificação selecionada não traz informação para criação de vias, o sistema exige que, antes de gravar o documento, seja informada uma sugestão de classificação para ser incluída na próxima revisão da tabela de classificações.");
			
			if (doc.getDescrDocumento().length() > exDocumentoDto.getTamanhoMaximoDescricao())
				throw new AplicacaoException("O campo descrição possui mais do que "
						+ exDocumentoDto.getTamanhoMaximoDescricao() + " caracteres.");
			
			if (doc.isFinalizado()) {
				
				Date dt = dao().dt();
				Calendar c = Calendar.getInstance();
				c.setTime(dt);
				
				Calendar dtDocCalendar = Calendar.getInstance();
				
				if (doc.getDtDoc() == null)
					throw new Exception("A data do documento deve ser informada.");
				
				dtDocCalendar.setTime(doc.getDtDoc());
				
				if (c.before(dtDocCalendar))
					throw new Exception("Não é permitido criar documento com data futura");
				
				verificaDocumento(doc);
			}
			
			ExMobil mobilAutuado = null;
			if (exDocumentoDto.getIdMobilAutuado() != null) {
				
				mobilAutuado = dao().consultar(exDocumentoDto.getIdMobilAutuado(), ExMobil.class, false);
				
				doc.setExMobilAutuado(mobilAutuado);
			}
			
			Ex.getInstance().getBL().gravar(getCadastrante(), getLotaTitular(), doc, null);
			
			lerEntrevista(exDocumentoDto);
			
			if (exDocumentoDto.getDesativarDocPai().equals("sim"))
				exDocumentoDto.setDesativ("&desativarDocPai=sim");
			
			try {
				
				Ex.getInstance().getBL().incluirCosignatariosAutomaticamente(getCadastrante(), getLotaTitular(), doc);
				
			} catch (Exception e) {
				
				throw new AplicacaoException("Erro ao tentar incluir os cosignatários deste documento", 0, e);
				
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
	
	public String aGravarPreenchimento(ExDocumentoDTO exDocumentoDto) throws Exception {
		dao().iniciarTransacao();
		ExPreenchimento exPreenchimento = new ExPreenchimento();
		
		DpLotacao provLota = new DpLotacao();
		provLota = getLotaTitular();
		ExModelo provMod = new ExModelo();
		provMod.setIdMod(exDocumentoDto.getIdMod());
		
		exPreenchimento.setDpLotacao(provLota);
		exPreenchimento.setExModelo(provMod);
		exPreenchimento.setNomePreenchimento(exDocumentoDto.getNomePreenchimento());
		
		exPreenchimento.setPreenchimentoBA(getByteArrayFormPreenchimento());
		dao().gravar(exPreenchimento);
		dao().commitTransacao();
		
		exDocumentoDto.setPreenchimento(exPreenchimento.getIdPreenchimento());
		
		return edita(exDocumentoDto).toString();
		
	}
	
	public String aPrever(ExDocumentoDTO exDocumentoDto) throws Exception {
		buscarDocumentoOuNovo(true, exDocumentoDto);
		if (exDocumentoDto.getDoc() != null) {
			if (getPostback() == null) {
				escreverForm(exDocumentoDto);
			} else {
				
				lerForm(exDocumentoDto);
			}
		} else {
			exDocumentoDto.setDoc(new ExDocumento());
			lerForm(exDocumentoDto);
		}
		
		carregarBeans(exDocumentoDto);
		
		if (param("idMod") != null) {
			exDocumentoDto.setModelo(dao().consultar(paramLong("idMod"), ExModelo.class, false));
		}
		
		if (param("processar_modelo") != null)
			return "processa_modelo";
		return Action.SUCCESS;
	}
	
	public String aPreverPdf(ExDocumentoDTO exDocumentoDto) throws Exception {
		buscarDocumentoOuNovo(true, exDocumentoDto);
		if (exDocumentoDto.getDoc() != null) {
			if (getPostback() == null) {
				escreverForm(exDocumentoDto);
			} else {
				lerForm(exDocumentoDto);
			}
		} else {
			exDocumentoDto.setDoc(new ExDocumento());
			lerForm(exDocumentoDto);
		}
		
		carregarBeans(exDocumentoDto);
		
		if (param("idMod") != null) {
			exDocumentoDto.setModelo(dao().consultar(paramLong("idMod"), ExModelo.class, false));
		}
		
		Ex.getInstance().getBL().processar(exDocumentoDto.getDoc(), false, false, null);
		
		exDocumentoDto.setPdfStreamResult(new ByteArrayInputStream(exDocumentoDto.getDoc().getConteudoBlobPdf()));
		
		return Action.SUCCESS;
	}
	
	public String aRefazer(ExDocumentoDTO exDocumentoDto) throws Exception {
		buscarDocumento(true, exDocumentoDto);
		
		if (!Ex.getInstance().getComp().podeRefazer(getTitular(), getLotaTitular(), exDocumentoDto.getMob()))
			throw new AplicacaoException("Não é possível refazer o documento");
		try {
			exDocumentoDto.setDoc(Ex.getInstance().getBL()
					.refazer(getCadastrante(), getLotaTitular(), exDocumentoDto.getDoc()));
		} catch (final Exception e) {
			throw e;
		}
		return Action.SUCCESS;
	}
	
	public String aAtualizarMarcasDoc(ExDocumentoDTO exDocumentoDto) throws Exception {
		
		buscarDocumento(false, exDocumentoDto);
		Ex.getInstance().getBL().atualizarMarcas(exDocumentoDto.getDoc());
		
		return Action.SUCCESS;
	}
	
	public String aTestarPdf() throws Exception {
		return Action.SUCCESS;
	}
	
	public String aTesteEnvioDJE() throws Exception {
		
		try {
			ExMovimentacao fakeMov = ExDao.getInstance().consultar(39468L, ExMovimentacao.class, false);
			ExDocumento doque = fakeMov.getExDocumento();
			GeradorRTF gerador = new GeradorRTF();
			String nomeArq = doque.getIdDoc().toString();
			fakeMov.setConteudoBlobRTF(nomeArq, gerador.geraRTF(doque));
			fakeMov.setConteudoBlobXML(nomeArq,
					PublicacaoDJEBL.gerarXMLPublicacao(fakeMov, "A", "SESIA", "Teste descrição"));
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
		Ex.getInstance().getBL().corrigirArquivamentosEmVolume(idPrimeiroDoc, idUltimoDoc, efetivar);
		return Action.SUCCESS;
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
			 * ".zip"); Long numTRF = PublicacaoDJEBL
			 * .verificaPrimeiroRetornoPublicacao(fakeMov); /* final Compactador
			 * zip = new Compactador(); final byte[] arqZip =
			 * getConteudoBlobMov2(); byte[] conteudoZip = null; conteudoZip =
			 * zip.adicionarStream(nome, conteudo, arqZip);
			 * setConteudoBlobMov2(conteudoZip);
			 */
			
		}
		return Action.SUCCESS;
	}
	
	public String aDuplicar(ExDocumentoDTO exDocumentoDto) throws Exception {
		buscarDocumento(false, exDocumentoDto);
		if (!Ex.getInstance().getComp().podeDuplicar(getTitular(), getLotaTitular(), exDocumentoDto.getMob()))
			throw new AplicacaoException("Não é possível duplicar o documento");
		try {
			exDocumentoDto.setDoc(Ex.getInstance().getBL()
					.duplicar(getCadastrante(), getLotaTitular(), exDocumentoDto.getDoc()));
		} catch (final Exception e) {
			throw e;
		}
		return Action.SUCCESS;
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
	
	public String aDesfazerCancelamentoDocumento(ExDocumentoDTO exDocumentoDto) throws Exception {
		buscarDocumento(true, exDocumentoDto);
		if (!Ex.getInstance().getComp()
				.podeDesfazerCancelamentoDocumento(getTitular(), getLotaTitular(), exDocumentoDto.getMob()))
			throw new AplicacaoException("Não é possível desfazer o cancelamento deste documento");
		try {
			Ex.getInstance().getBL()
					.DesfazerCancelamentoDocumento(getCadastrante(), getLotaTitular(), exDocumentoDto.getDoc());
		} catch (final Exception e) {
			throw e;
		}
		return Action.SUCCESS;
	}
	
	public String aTornarDocumentoSemEfeito(ExDocumentoDTO exDocumentoDto) throws Exception {
		buscarDocumento(true, exDocumentoDto);
		return Action.SUCCESS;
	}
	
	public String aTornarDocumentoSemEfeitoGravar(ExDocumentoDTO exDocumentoDto) throws Exception {
		if (exDocumentoDto.getDescrMov() == null || exDocumentoDto.getDescrMov().trim().length() == 0) {
			throw new AplicacaoException("O preenchimento do campo MOTIVO é obrigatório!");
		}
		buscarDocumento(true, exDocumentoDto);
		if (!Ex.getInstance().getComp()
				.podeTornarDocumentoSemEfeito(getTitular(), getLotaTitular(), exDocumentoDto.getMob()))
			throw new AplicacaoException("Não é possível tornar documento sem efeito.");
		try {
			Ex.getInstance()
					.getBL()
					.TornarDocumentoSemEfeito(getCadastrante(), getLotaTitular(), exDocumentoDto.getDoc(),
							exDocumentoDto.getDescrMov());
		} catch (final Exception e) {
			throw e;
		}
		return Action.SUCCESS;
	}
	
	public String acriarDocTest() throws Exception {
		try {
			setMensagem(Ex.getInstance().getBL().criarDocTeste());
		} catch (final Exception e) {
			throw e;
		}
		
		return Action.SUCCESS;
	}
	
	private void carregarBeans(ExDocumentoDTO exDocumentoDto) throws Exception {
		ExMobil mobPai = null;
		
		exDocumentoDto.getDoc().setExTipoDocumento(
				dao().consultar(exDocumentoDto.getIdTpDoc(), ExTipoDocumento.class, false));
		
		// Questões referentes a doc pai-----------------------------
		
		if (exDocumentoDto.getDoc().getIdDoc() == null) {
			String req = "nao";
			if (getPar().get("reqdocumentoRefSel") != null)
				req = getPar().get("reqmobilPaiSel")[0].toString();
			
			if (param("mobilPaiSel.sigla") != null)
				exDocumentoDto.getMobilPaiSel().setSigla(param("mobilPaiSel.sigla"));
			exDocumentoDto.getMobilPaiSel().buscar();
			if ((exDocumentoDto.getMobilPaiSel().getId() != null) || (req.equals("sim"))) {
				
				if (exDocumentoDto.getMobilPaiSel().getId() != null) {
					// Documento Pai
					mobPai = daoMob(exDocumentoDto.getMobilPaiSel().getId());
					
					Integer idForma = paramInteger("idForma");
					if (idForma != null)
						exDocumentoDto.setIdFormaDoc(idForma);
					
					if (exDocumentoDto.getClassificacaoSel() != null
							&& exDocumentoDto.getClassificacaoSel().getId() == null)
						exDocumentoDto.getClassificacaoSel().setId(mobPai.doc().getExClassificacaoAtual().getId());
					
					exDocumentoDto.setDescrDocumento(mobPai.doc().getDescrDocumento());
					
					exDocumentoDto.setDesativarDocPai("sim");
				}
				
			}
			
			if (exDocumentoDto.getAutuando() && exDocumentoDto.getIdMobilAutuado() != null) {
				ExMobil mobilAutuado = daoMob(exDocumentoDto.getIdMobilAutuado());
				
				exDocumentoDto.getDoc().setExMobilAutuado(mobilAutuado);
				
				exDocumentoDto.getClassificacaoSel().setId(mobilAutuado.getDoc().getExClassificacao().getId());
				exDocumentoDto.setDescrDocumento(mobilAutuado.getDoc().getDescrDocumento());
			}
		}
		
		// Fim das questões referentes a doc pai--------------------
		
		Integer idFormaDoc = exDocumentoDto.getIdFormaDoc();
		if (idFormaDoc != null) {
			if (idFormaDoc == 0) {
				exDocumentoDto.setIdMod(0L);
			} else {
				
				// Mudou origem? Escolhe um tipo automaticamente--------
				// Vê se usuário alterou campo Origem. Caso sim, seleciona um
				// tipo
				// automaticamente, dentro daquela origem
				
				final List<ExFormaDocumento> formasDoc = getFormasDocPorTipo(exDocumentoDto);
				
				ExFormaDocumento forma = dao().consultar(exDocumentoDto.getIdFormaDoc(), ExFormaDocumento.class, false);
				
				if (!formasDoc.contains(forma)) {
					exDocumentoDto.setIdFormaDoc(exDocumentoDto.getFormaDocPorTipo().getIdFormaDoc());
					forma = dao().consultar(exDocumentoDto.getIdFormaDoc(), ExFormaDocumento.class, false);
				}
				
				// Fim -- Mudou origem? Escolhe um tipo automaticamente--------
				
				if (forma.getExModeloSet().size() == 0) {
					exDocumentoDto.setIdMod(0L);
				}
			}
		}
		
		ExModelo mod = null;
		if (exDocumentoDto.getIdMod() != null && exDocumentoDto.getIdMod() != 0) {
			mod = dao().consultar(exDocumentoDto.getIdMod(), ExModelo.class, false);
		}
		if (mod != null) {
			mod = mod.getModeloAtual();
		}
		
		List<ExModelo> modelos = getModelos(exDocumentoDto);
		if (mod == null || !modelos.contains(mod)) {
			mod = (ExModelo) (modelos.toArray())[0];
			
			for (ExModelo modeloAtual : modelos) {
				if (modeloAtual.getIdMod() != null && modeloAtual.getIdMod() != 0
						&& modeloAtual.getNmMod().equals(modeloAtual.getExFormaDocumento().getDescricao())) {
					mod = modeloAtual;
					break;
				}
			}
			
			exDocumentoDto.setIdMod(mod.getIdMod());
			if ((exDocumentoDto.getIdMod() != 0) && (exDocumentoDto.getMobilPaiSel().getId() == null)
					&& (exDocumentoDto.getIdMobilAutuado() == null))
				exDocumentoDto.getClassificacaoSel().apagar();
		}
		
		if (getPreenchimentos(exDocumentoDto).size() <= 1) {
			exDocumentoDto.setPreenchimento(0L);
		}
		
		if (exDocumentoDto.isAlterouModelo() && exDocumentoDto.getMobilPaiSel().getId() == null
				&& exDocumentoDto.getIdMobilAutuado() == null)
			exDocumentoDto.getClassificacaoSel().apagar();
		
		boolean naLista = false;
		final Set<ExPreenchimento> preenchimentos = getPreenchimentos(exDocumentoDto);
		if (preenchimentos != null && preenchimentos.size() > 0) {
			for (ExPreenchimento exp : preenchimentos) {
				if (exp.getIdPreenchimento().equals(exDocumentoDto.getPreenchimento())) {
					naLista = true;
					break;
				}
			}
			if (!naLista)
				exDocumentoDto.setPreenchimento(((ExPreenchimento) (preenchimentos.toArray())[0]).getIdPreenchimento());
		}
		
		exDocumentoDto.setModelo(mod);
		if (mod.getExClassificacao() != null
				&& mod.getExClassificacao().getId() != exDocumentoDto.getClassificacaoSel().getId()) {
			exDocumentoDto.getClassificacaoSel().buscarPorObjeto(mod.getExClassificacao());
		}
		
	}
	
	private void escreverForm(ExDocumentoDTO exDocumentoDto) throws IllegalAccessException, NoSuchMethodException,
			AplicacaoException, InvocationTargetException {
		ExDocumento doc = exDocumentoDto.getDoc();
		
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
			exDocumentoDto.setConteudo(new String(doc.getConteudoBlob("doc.htm")));
		
		exDocumentoDto.setIdTpDoc(doc.getExTipoDocumento().getIdTpDoc());
		exDocumentoDto.setNivelAcesso(doc.getIdExNivelAcesso());
		
		if (doc.getExFormaDocumento() != null) {
			exDocumentoDto.setIdFormaDoc(doc.getExFormaDocumento().getIdFormaDoc());
		}
		
		ExClassificacao classif = doc.getExClassificacaoAtual();
		if (classif != null)
			exDocumentoDto.getClassificacaoSel().buscarPorObjeto(classif.getAtual());
		exDocumentoDto.getSubscritorSel().buscarPorObjeto(doc.getSubscritor());
		// form.getDestinatarioSel().buscarPorObjeto(doc.getDestinatario());
		if (doc.getExModelo() != null) {
			ExModelo modeloAtual = doc.getExModelo().getModeloAtual();
			if (modeloAtual != null) {
				exDocumentoDto.setIdMod(modeloAtual.getIdMod());
			}
		}
		
		if (doc.getDestinatario() != null) {
			exDocumentoDto.getDestinatarioSel().buscarPorObjeto(doc.getDestinatario());
			exDocumentoDto.setTipoDestinatario(1);
		}
		if (doc.getLotaDestinatario() != null) {
			exDocumentoDto.getLotacaoDestinatarioSel().buscarPorObjeto(doc.getLotaDestinatario());
			if (doc.getDestinatario() == null)
				exDocumentoDto.setTipoDestinatario(2);
		}
		
		if (doc.getExMobilPai() != null) {
			exDocumentoDto.getMobilPaiSel().buscarPorObjeto(doc.getExMobilPai());
		}
		
		if (doc.getTitular() != null && doc.getSubscritor() != null
				&& !doc.getTitular().getIdPessoa().equals(doc.getSubscritor().getIdPessoa())) {
			exDocumentoDto.getTitularSel().buscarPorObjeto(doc.getTitular());
			exDocumentoDto.setSubstituicao(true);
		}
		
		// TODO Verificar se ha realmente a necessidade de setar novamente o
		// nível de acesso do documento
		// tendo em vista que o nível de acesso já foi setado anteriormente
		// neste mesmo método sem que o documento fosse alterado
		exDocumentoDto.setNivelAcesso(doc.getIdExNivelAcesso());
		
		if (doc.getOrgaoExternoDestinatario() != null) {
			exDocumentoDto.getOrgaoExternoDestinatarioSel().buscarPorObjeto(doc.getOrgaoExternoDestinatario());
			exDocumentoDto.setTipoDestinatario(3);
		}
		if (doc.getNmOrgaoExterno() != null && !doc.getNmOrgaoExterno().equals("")) {
			exDocumentoDto.setTipoDestinatario(3);
		}
		if (doc.getNmDestinatario() != null) {
			exDocumentoDto.setNmDestinatario(doc.getNmDestinatario());
			exDocumentoDto.setTipoDestinatario(4);
		}
		
		if (doc.getOrgaoExterno() != null) {
			exDocumentoDto.getCpOrgaoSel().buscarPorObjeto(doc.getOrgaoExterno());
			exDocumentoDto.setIdTpDoc(3L);
		}
		
		if (doc.getObsOrgao() != null) {
			exDocumentoDto.setObsOrgao(doc.getObsOrgao());
		}
		
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			exDocumentoDto.setDtDocString(df.format(doc.getDtDoc()));
		} catch (final Exception e) {
		}
		
		try {
			exDocumentoDto.setDtDocOriginalString(df.format(doc.getDtDocOriginal()));
		} catch (final Exception e) {
		}
		
		if (doc.getAnoEmissao() != null)
			exDocumentoDto.setAnoEmissaoString(doc.getAnoEmissao().toString());
		
		exDocumentoDto.setEletronico(doc.isEletronico() ? 1 : 2);
		
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
				if (param(s) != null && !param(s).trim().equals("") && !s.trim().equals("preenchimento")
						&& !param(s).matches("[0-9]{2}/[0-9]{2}/[0-9]{4}")/*
																		   * s.trim
																		   * (
																		   * ).
																		   * equals
																		   * (
																		   * "dtDocString"
																		   * )
																		   */) {
					if (baos.size() > 0)
						baos.write('&');
					baos.write(s.getBytes());
					baos.write('=');
					
					// Deveria estar gravando como UTF-8
					baos.write(URLEncoder.encode(param(s), "iso-8859-1").getBytes());
				}
			}
		}
		return baos.toByteArray();
	}
	
	private void lerEntrevista(ExDocumentoDTO exDocumentoDto) {
		final ExDocumento doc = exDocumentoDto.getDoc();
		if (doc.getExModelo() != null) {
			final byte[] form = doc.getConteudoBlob("doc.form");
			if (form != null) {
				final String as[] = new String(form).split("&");
				for (final String s : as) {
					final String param[] = s.split("=");
					try {
						if (param.length == 2)
							exDocumentoDto.getParamsEntrevista().put(param[0],
									URLDecoder.decode(param[1], "iso-8859-1"));
						// setParam(param[0], URLDecoder.decode(param[1],
						// "iso-8859-1"));
					} catch (final UnsupportedEncodingException e) {
					}
				}
			}
		}
	}
	
	private void lerForm(ExDocumentoDTO exDocumentoDto) throws IllegalAccessException, NoSuchMethodException,
			AplicacaoException {
		
		ExDocumento doc = exDocumentoDto.getDoc();
		if (exDocumentoDto.getAnexar()) {
			doc.setConteudoTpDoc(exDocumentoDto.getConteudoTpDoc());
			doc.setNmArqDoc(exDocumentoDto.getNmArqDoc());
		}
		
		// BeanUtils.copyProperties(doc, form);
		// fabrica = DaoFactory.getDAOFactory();
		doc.setDescrDocumento(exDocumentoDto.getDescrDocumento());
		doc.setNmSubscritorExt(exDocumentoDto.getNmSubscritorExt());
		doc.setNmFuncaoSubscritor(exDocumentoDto.getNmFuncaoSubscritor());
		doc.setNumExtDoc(exDocumentoDto.getNumExtDoc());
		doc.setNumAntigoDoc(exDocumentoDto.getNumAntigoDoc());
		doc.setObsOrgao(exDocumentoDto.getObsOrgao());
		doc.setEletronico(exDocumentoDto.getEletronico() == 1 ? true : false);
		doc.setNmOrgaoExterno(exDocumentoDto.getNmOrgaoExterno());
		doc.setDescrClassifNovo(exDocumentoDto.getDescrClassifNovo());
		
		doc.setExNivelAcesso(dao().consultar(exDocumentoDto.getNivelAcesso(), ExNivelAcesso.class, false));
		
		doc.setExTipoDocumento(dao().consultar(exDocumentoDto.getIdTpDoc(), ExTipoDocumento.class, false));
		
		if (!doc.isFinalizado())
			doc.setExFormaDocumento(dao().consultar(exDocumentoDto.getIdFormaDoc(), ExFormaDocumento.class, false));
		doc.setNmDestinatario(exDocumentoDto.getNmDestinatario());
		
		doc.setExModelo(null);
		if (exDocumentoDto.getIdMod() != 0) {
			ExModelo modelo = dao().consultar(exDocumentoDto.getIdMod(), ExModelo.class, false);
			if (modelo != null)
				doc.setExModelo(modelo.getModeloAtual());
		}
		
		if (exDocumentoDto.getClassificacaoSel().getId() != null && exDocumentoDto.getClassificacaoSel().getId() != 0) {
			
			ExClassificacao classificacao = dao().consultar(exDocumentoDto.getClassificacaoSel().getId(),
					ExClassificacao.class, false);
			
			if (classificacao != null && !classificacao.isFechada())
				doc.setExClassificacao(classificacao);
			else {
				doc.setExClassificacao(null);
				exDocumentoDto.getClassificacaoSel().apagar();
			}
			
		} else
			doc.setExClassificacao(null);
		if (exDocumentoDto.getCpOrgaoSel().getId() != null) {
			doc.setOrgaoExterno(dao().consultar(exDocumentoDto.getCpOrgaoSel().getId(), CpOrgao.class, false));
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
		if (exDocumentoDto.getSubscritorSel().getId() != null) {
			doc.setSubscritor(daoPes(exDocumentoDto.getSubscritorSel().getId()));
			doc.setLotaSubscritor(doc.getSubscritor().getLotacao());
		} else {
			doc.setSubscritor(null);
		}
		
		if (exDocumentoDto.isSubstituicao()) {
			if (exDocumentoDto.getTitularSel().getId() != null) {
				doc.setTitular(daoPes(exDocumentoDto.getTitularSel().getId()));
				doc.setLotaTitular(doc.getTitular().getLotacao());
			} else {
				doc.setTitular(doc.getSubscritor());
				doc.setLotaTitular(doc.getLotaSubscritor());
			}
		} else {
			doc.setTitular(doc.getSubscritor());
			doc.setLotaTitular(doc.getLotaSubscritor());
		}
		
		if (exDocumentoDto.getDestinatarioSel().getId() != null) {
			doc.setDestinatario(daoPes(exDocumentoDto.getDestinatarioSel().getId()));
			doc.setLotaDestinatario(daoPes(exDocumentoDto.getDestinatarioSel().getId()).getLotacao());
			doc.setOrgaoExternoDestinatario(null);
		} else {
			doc.setDestinatario(null);
			if (exDocumentoDto.getLotacaoDestinatarioSel().getId() != null) {
				doc.setLotaDestinatario(daoLot(exDocumentoDto.getLotacaoDestinatarioSel().getId()));
				doc.setOrgaoExternoDestinatario(null);
			} else {
				doc.setLotaDestinatario(null);
				if (exDocumentoDto.getOrgaoExternoDestinatarioSel().getId() != null) {
					doc.setOrgaoExternoDestinatario(dao().consultar(
							exDocumentoDto.getOrgaoExternoDestinatarioSel().getId(), CpOrgao.class, false));
				} else {
					doc.setOrgaoExternoDestinatario(null);
				}
			}
		}
		
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			doc.setDtDoc(df.parse(exDocumentoDto.getDtDocString()));
		} catch (final ParseException e) {
			doc.setDtDoc(null);
		} catch (final NullPointerException e) {
			doc.setDtDoc(null);
		}
		if (doc.getDtRegDoc() == null)
			doc.setDtRegDoc(dao().dt());
		
		try {
			doc.setDtDocOriginal(df.parse(exDocumentoDto.getDtDocOriginalString()));
		} catch (final ParseException e) {
			doc.setDtDocOriginal(null);
		} catch (final NullPointerException e) {
			doc.setDtDocOriginal(null);
		}
		
		if (exDocumentoDto.getNumExpediente() != null) {
			doc.setNumExpediente(new Long(exDocumentoDto.getNumExpediente()));
			doc.setAnoEmissao(new Long(exDocumentoDto.getAnoEmissaoString()));
		}
		
		if (exDocumentoDto.getMobilPaiSel().getId() != null) {
			doc.setExMobilPai(dao().consultar(exDocumentoDto.getMobilPaiSel().getId(), ExMobil.class, false));
		} else {
			doc.setExMobilPai(null);
		}
		
		try {
			ByteArrayOutputStream baos;
			
			final String marcacoes[] = { "<!-- INICIO NUMERO -->", "<!-- FIM NUMERO -->", "<!-- INICIO NUMERO",
					"FIM NUMERO -->", "<!-- INICIO TITULO", "FIM TITULO -->", "<!-- INICIO MIOLO -->",
					"<!-- FIM MIOLO -->", "<!-- INICIO CORPO -->", "<!-- FIM CORPO -->", "<!-- INICIO CORPO",
					"FIM CORPO -->", "<!-- INICIO ASSINATURA -->", "<!-- FIM ASSINATURA -->",
					"<!-- INICIO ABERTURA -->", "<!-- FIM ABERTURA -->", "<!-- INICIO ABERTURA", "FIM ABERTURA -->",
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
						
						baos.write(URLEncoder.encode(parametro, "iso-8859-1").getBytes());
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
				&& (numRead = is.read(meuByteArray, offset, meuByteArray.length - offset)) >= 0) {
			offset += numRead;
		}
		
		// Ensure all the bytes have been read in
		if (offset < meuByteArray.length)
			throw new IOException("Não foi possível ler o arquivo completamente " + file.getName());
		
		// Close the input stream and return bytes
		is.close();
		return meuByteArray;
	}
	
	public List<ExModelo> getModelos(ExDocumentoDTO exDocumentoDto) throws Exception {
		if (exDocumentoDto.getModelos() != null)
			return exDocumentoDto.getModelos();
		
		ExFormaDocumento forma = null;
		if (exDocumentoDto.getIdFormaDoc() != null && exDocumentoDto.getIdFormaDoc() != 0)
			forma = dao().consultar(exDocumentoDto.getIdFormaDoc(), ExFormaDocumento.class, false);
		
		String headerValue = null;
		if (exDocumentoDto.getTipoDocumento() != null && exDocumentoDto.getTipoDocumento().equals("antigo"))
			headerValue = "Não Informado";
		
		exDocumentoDto.setModelos(Ex
				.getInstance()
				.getBL()
				.obterListaModelos(forma, exDocumentoDto.getDespachando(), headerValue, true, getTitular(),
						getLotaTitular(), exDocumentoDto.getAutuando()));
		return exDocumentoDto.getModelos();
		
	}
	
	public List<ExFormaDocumento> getFormasDocPorTipo(ExDocumentoDTO exDocumentoDto) throws Exception {
		if (exDocumentoDto.getFormasDoc() == null) {
			exDocumentoDto.setFormasDoc(new ArrayList<ExFormaDocumento>());
			ExBL bl = Ex.getInstance().getBL();
			exDocumentoDto.getFormasDoc().addAll(
					bl.obterFormasDocumento(bl.obterListaModelos(null, exDocumentoDto.getDespachando(), null, true,
							getTitular(), getLotaTitular(), exDocumentoDto.getAutuando()), exDocumentoDto.getDoc()
							.getExTipoDocumento(), null));
		}
		
		return exDocumentoDto.getFormasDoc();
	}
	
	public Set<ExPreenchimento> getPreenchimentos(ExDocumentoDTO exDocumentoDto) throws AplicacaoException {
		if (exDocumentoDto.getPreenchSet() != null)
			return exDocumentoDto.getPreenchSet();
		
		exDocumentoDto.setPreenchSet(new LinkedHashSet<ExPreenchimento>());
		if (exDocumentoDto.getIdFormaDoc() == null || exDocumentoDto.getIdFormaDoc() == 0)
			return exDocumentoDto.getPreenchSet();
		
		ExPreenchimento preench = new ExPreenchimento();
		if (exDocumentoDto.getIdMod() != null && exDocumentoDto.getIdMod() != 0L)
			preench.setExModelo(dao().consultar(exDocumentoDto.getIdMod(), ExModelo.class, false));
		
		DpLotacao lota = new DpLotacao();
		lota.setIdLotacaoIni(getLotaTitular().getIdLotacaoIni());
		List<DpLotacao> lotacaoSet = dao().consultar(lota, null);
		
		exDocumentoDto.getPreenchSet().add(new ExPreenchimento(0, null, " [Em branco] ", null));
		
		if (exDocumentoDto.getIdMod() != null && exDocumentoDto.getIdMod() != 0) {
			for (DpLotacao lotacao : lotacaoSet) {
				preench.setDpLotacao(lotacao);
				exDocumentoDto.getPreenchSet().addAll(dao().consultar(preench));
			}
		}
		
		return exDocumentoDto.getPreenchSet();
	}
	
	public List<ExNivelAcesso> getListaNivelAcesso(ExDocumentoDTO exDocumentoDto) throws Exception {
		ExFormaDocumento exForma = new ExFormaDocumento();
		ExClassificacao exClassif = new ExClassificacao();
		ExTipoDocumento exTipo = new ExTipoDocumento();
		ExModelo exMod = new ExModelo();
		
		if (exDocumentoDto.getIdTpDoc() != null) {
			exTipo = dao().consultar(exDocumentoDto.getIdTpDoc(), ExTipoDocumento.class, false);
		}
		
		if (exDocumentoDto.getIdFormaDoc() != null) {
			exForma = dao().consultar(exDocumentoDto.getIdFormaDoc(), ExFormaDocumento.class, false);
		}
		
		if (exDocumentoDto.getIdMod() != null && exDocumentoDto.getIdMod() != 0) {
			exMod = dao().consultar(exDocumentoDto.getIdMod(), ExModelo.class, false);
		}
		
		if (exDocumentoDto.getClassificacaoSel().getId() != null) {
			exClassif = dao().consultar(exDocumentoDto.getClassificacaoSel().getId(), ExClassificacao.class, false);
		}
		
		return getListaNivelAcesso(exTipo, exForma, exMod, exClassif);
	}
	
	public ExNivelAcesso getNivelAcessoDefault(ExDocumentoDTO exDocumentoDto) throws Exception {
		ExFormaDocumento exForma = new ExFormaDocumento();
		ExClassificacao exClassif = new ExClassificacao();
		ExTipoDocumento exTipo = new ExTipoDocumento();
		ExModelo exMod = new ExModelo();
		
		if (exDocumentoDto.getIdTpDoc() != null) {
			exTipo = dao().consultar(exDocumentoDto.getIdTpDoc(), ExTipoDocumento.class, false);
		}
		
		if (exDocumentoDto.getIdFormaDoc() != null) {
			exForma = dao().consultar(exDocumentoDto.getIdFormaDoc(), ExFormaDocumento.class, false);
		}
		
		if (exDocumentoDto.getIdMod() != null) {
			exMod = dao().consultar(exDocumentoDto.getIdMod(), ExModelo.class, false);
		}
		
		if (exDocumentoDto.getClassificacaoSel().getId() != null) {
			exClassif = dao().consultar(exDocumentoDto.getClassificacaoSel().getId(), ExClassificacao.class, false);
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
	
}
