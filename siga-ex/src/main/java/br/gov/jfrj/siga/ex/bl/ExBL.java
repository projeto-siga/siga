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
package br.gov.jfrj.siga.ex.bl;

import static br.gov.jfrj.siga.ex.ExMobil.isMovimentacaoComOrigemPeloBotaoDeRestricaoDeAcesso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.proxy.HibernateProxy;
import org.jboss.logging.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import com.auth0.jwt.JWTSigner;
import com.crivano.swaggerservlet.ISwaggerRequest;
import com.crivano.swaggerservlet.ISwaggerResponse;
import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import br.gov.jfrj.itextpdf.ConversorHtml;
import br.gov.jfrj.itextpdf.Documento;
import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Correio;
import br.gov.jfrj.siga.base.CurrentRequest;
import br.gov.jfrj.siga.base.Data;
import br.gov.jfrj.siga.base.GeraMessageDigest;
import br.gov.jfrj.siga.base.HtmlToPlainText;
import br.gov.jfrj.siga.base.HttpRequestUtils;
import br.gov.jfrj.siga.base.Par;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.base.RequestInfo;
import br.gov.jfrj.siga.base.SigaHTTP;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.base.UsuarioDeSistemaEnum;
import br.gov.jfrj.siga.base.util.SetUtils;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.bluc.service.BlucService;
import br.gov.jfrj.siga.bluc.service.EnvelopeRequest;
import br.gov.jfrj.siga.bluc.service.EnvelopeResponse;
import br.gov.jfrj.siga.bluc.service.ValidateRequest;
import br.gov.jfrj.siga.bluc.service.ValidateResponse;
import br.gov.jfrj.siga.cp.CpArquivo;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpConfiguracaoCache;
import br.gov.jfrj.siga.cp.CpGrupo;
import br.gov.jfrj.siga.cp.CpGrupoDeEmail;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpToken;
import br.gov.jfrj.siga.cp.TipoConteudo;
import br.gov.jfrj.siga.cp.arquivo.Armazenamento;
import br.gov.jfrj.siga.cp.arquivo.ArmazenamentoFabrica;
import br.gov.jfrj.siga.cp.auth.ValidadorDeSenhaFabrica;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.CpBL;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoBL;
import br.gov.jfrj.siga.cp.grupo.ConfiguracaoGrupo;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorFinalidadeEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorFinalidadeGrupoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeMovimentacao;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpResponsavel;
import br.gov.jfrj.siga.dp.DpSubstituicao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExArquivo;
import br.gov.jfrj.siga.ex.ExArquivoNumerado;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExConfiguracao;
import br.gov.jfrj.siga.ex.ExConfiguracaoCache;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExEditalEliminacao;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.ExProtocolo;
import br.gov.jfrj.siga.ex.ExRef;
import br.gov.jfrj.siga.ex.ExTemporalidade;
import br.gov.jfrj.siga.ex.ExTermoEliminacao;
import br.gov.jfrj.siga.ex.ExTipoDespacho;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.ExTipoMobil;
import br.gov.jfrj.siga.ex.ExTipoSequencia;
import br.gov.jfrj.siga.ex.ExVia;
import br.gov.jfrj.siga.ex.bl.ExTramiteBL.Pendencias;
import br.gov.jfrj.siga.ex.bl.BIE.BoletimInternoBL;
import br.gov.jfrj.siga.ex.ext.AbstractConversorHTMLFactory;
import br.gov.jfrj.siga.ex.logic.ExECossignatario;
import br.gov.jfrj.siga.ex.logic.ExPodeAcessarDocumento;
import br.gov.jfrj.siga.ex.logic.ExPodeApensar;
import br.gov.jfrj.siga.ex.logic.ExPodeArquivarCorrente;
import br.gov.jfrj.siga.ex.logic.ExPodeAssinarComSenha;
import br.gov.jfrj.siga.ex.logic.ExPodeAssinarMovimentacaoComSenha;
import br.gov.jfrj.siga.ex.logic.ExPodeAssinarPorPorConfiguracao;
import br.gov.jfrj.siga.ex.logic.ExPodeAtenderPedidoPublicacaoNoDiario;
import br.gov.jfrj.siga.ex.logic.ExPodeAutenticarComCertificadoDigital;
import br.gov.jfrj.siga.ex.logic.ExPodeAutenticarMovimentacaoComSenha;
import br.gov.jfrj.siga.ex.logic.ExPodeCancelar;
import br.gov.jfrj.siga.ex.logic.ExPodeCancelarAnexo;
import br.gov.jfrj.siga.ex.logic.ExPodeCancelarDespacho;
import br.gov.jfrj.siga.ex.logic.ExPodeCancelarMarcacao;
import br.gov.jfrj.siga.ex.logic.ExPodeCancelarMovimentacao;
import br.gov.jfrj.siga.ex.logic.ExPodeCancelarOuAlterarPrazoDeAssinatura;
import br.gov.jfrj.siga.ex.logic.ExPodeCancelarRestringirAcesso;
import br.gov.jfrj.siga.ex.logic.ExPodeCancelarVia;
import br.gov.jfrj.siga.ex.logic.ExPodeCancelarVinculacao;
import br.gov.jfrj.siga.ex.logic.ExPodeCancelarVinculacaoPapel;
import br.gov.jfrj.siga.ex.logic.ExPodeCriarSubprocesso;
import br.gov.jfrj.siga.ex.logic.ExPodeDefinirPrazoAssinatura;
import br.gov.jfrj.siga.ex.logic.ExPodeDespachar;
import br.gov.jfrj.siga.ex.logic.ExPodeDisponibilizarNoAcompanhamentoDoProtocolo;
import br.gov.jfrj.siga.ex.logic.ExPodeEditarDescricao;
import br.gov.jfrj.siga.ex.logic.ExPodeExcluir;
import br.gov.jfrj.siga.ex.logic.ExPodeExibirQuemTemAcessoAoDocumento;
import br.gov.jfrj.siga.ex.logic.ExPodeFazerCiencia;
import br.gov.jfrj.siga.ex.logic.ExPodeJuntar;
import br.gov.jfrj.siga.ex.logic.ExPodeMarcar;
import br.gov.jfrj.siga.ex.logic.ExPodeMovimentar;
import br.gov.jfrj.siga.ex.logic.ExPodeNotificar;
import br.gov.jfrj.siga.ex.logic.ExPodePorConfiguracao;
import br.gov.jfrj.siga.ex.logic.ExPodePublicarPortalDaTransparencia;
import br.gov.jfrj.siga.ex.logic.ExPodeReceber;
import br.gov.jfrj.siga.ex.logic.ExPodeReceberDocumentoSemAssinatura;
import br.gov.jfrj.siga.ex.logic.ExPodeRedefinirNivelDeAcesso;
import br.gov.jfrj.siga.ex.logic.ExPodeReiniciarNumeracao;
import br.gov.jfrj.siga.ex.logic.ExPodeRestringirAcesso;
import br.gov.jfrj.siga.ex.logic.ExPodeRestringirCossignatarioSubscritor;
import br.gov.jfrj.siga.ex.logic.ExPodeSerJuntado;
import br.gov.jfrj.siga.ex.logic.ExPodeSerSubscritor;
import br.gov.jfrj.siga.ex.logic.ExPodeSerTransferido;
import br.gov.jfrj.siga.ex.logic.ExPodeTornarDocumentoSemEfeito;
import br.gov.jfrj.siga.ex.logic.ExPodeTransferir;
import br.gov.jfrj.siga.ex.model.enm.ConversaoDoeEnum;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDePrincipal;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeVinculo;
import br.gov.jfrj.siga.ex.service.ExService;
import br.gov.jfrj.siga.ex.util.DatasPublicacaoDJE;
import br.gov.jfrj.siga.ex.util.ExMovimentacaoRecebimentoComparator;
import br.gov.jfrj.siga.ex.util.ExXjusRecordServiceEnum;
import br.gov.jfrj.siga.ex.util.FuncoesEL;
import br.gov.jfrj.siga.ex.util.GeradorRTF;
import br.gov.jfrj.siga.ex.util.MascaraUtil;
import br.gov.jfrj.siga.ex.util.ProcessadorHtml;
import br.gov.jfrj.siga.ex.util.ProcessadorModelo;
import br.gov.jfrj.siga.ex.util.ProcessadorModeloFreemarker;
import br.gov.jfrj.siga.ex.util.PublicacaoDJEBL;
import br.gov.jfrj.siga.ex.util.BIE.ManipuladorEntrevista;
import br.gov.jfrj.siga.ex.util.notificador.especifico.ExEmail;
import br.gov.jfrj.siga.ex.util.notificador.especifico.ExNotificar;
import br.gov.jfrj.siga.ex.util.notificador.geral.Notificador;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.CancelaMaterialDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.EnviaPublicacaoDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.MontaReciboPublicacaoCancelamentoDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.MontaReciboPublicacaoDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.PermissaoPublicanteDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.ProximoSequencialDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.dto.TokenDto;
import br.gov.jfrj.siga.integracao.ws.pubnet.mapping.AuthHeader;
import br.gov.jfrj.siga.integracao.ws.pubnet.service.PubnetCancelamentoService;
import br.gov.jfrj.siga.integracao.ws.pubnet.service.PubnetConsultaService;
import br.gov.jfrj.siga.integracao.ws.pubnet.service.PubnetEnvioService;
import br.gov.jfrj.siga.integracao.ws.siafem.ServicoSiafemWs;
import br.gov.jfrj.siga.integracao.ws.siafem.SiafDoc;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.Objeto;
import br.gov.jfrj.siga.model.ObjetoBase;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.model.ContextoPersistencia.AfterCommit;
import br.gov.jfrj.siga.model.enm.CpExtensoesDeArquivoEnum;
import br.gov.jfrj.siga.parser.PessoaLotacaoParser;
import br.gov.jfrj.siga.parser.SiglaParser;
import br.gov.jfrj.siga.sinc.lib.Desconsiderar;
import br.gov.jfrj.siga.sinc.lib.Item;
import br.gov.jfrj.siga.sinc.lib.Sincronizador;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;
import br.gov.jfrj.siga.wf.service.WfProcedimentoWSTO;
import br.gov.jfrj.siga.wf.service.WfService;

public class ExBL extends CpBL {
	private static final String ERRO_EXCLUIR_ARQUIVO = "Erro ao excluir o arquivo";
	private static final String ERRO_GRAVAR_ARQUIVO = "Erro ao gravar o arquivo";
	private static final String MODELO_DESPACHO_AUTOMATICO = "Despacho Automático";
	private static final String MODELO_FOLHA_DE_ROSTO_EXPEDIENTE_INTERNO = "Folha de Rosto - Expediente Interno";
	private static final String MODELO_FOLHA_DE_ROSTO_PROCESSO_ADMINISTRATIVO_INTERNO = "Folha de Rosto - Processo Administrativo Interno";
	private static final String SHA1 = "1.3.14.3.2.26";
	private static final String MIME_TYPE_PKCS7 = "application/pkcs7-signature";
	private static final String STRING_TRUE = "1";
	
	private final ThreadLocal<Set<String>> docsParaAtualizacaoDeWorkflow = new ThreadLocal<Set<String>>();
	private final ThreadLocal<Boolean> suprimirAtualizacaoDeWorkflow = new ThreadLocal<>();

	private ProcessadorModelo processadorModeloJsp;
	private ProcessadorModelo processadorModeloFreemarker = new ProcessadorModeloFreemarker();

	private final static Logger log = Logger.getLogger(ExBL.class);

	ExNotificar notificar;
	
	public ExCompetenciaBL getComp() {
		return (ExCompetenciaBL) super.getComp();
	}

	public ExConfiguracaoBL getConf() {
		return (ExConfiguracaoBL) super.getComp().getConfiguracaoBL();
	}

	public ProcessadorModelo getProcessadorModeloJsp() {
		return processadorModeloJsp;
	}

	public void setProcessadorModeloJsp(ProcessadorModelo processadorModelo) {
		this.processadorModeloJsp = processadorModelo;
	}

	public String fixTableCols(String html) throws Exception {
		StringReader st = new StringReader(html);
		SAXBuilder sb = new SAXBuilder();
		sb.setIgnoringElementContentWhitespace(true);
		Document doc = sb.build(st);
		Element raiz = doc.getRootElement();
		fixTableCols(raiz);
		return (new XMLOutputter()).outputString(doc);
	}
	
	private ExVisualizacaoTempDocCompl getExConsTempDocCompleto() {
		return ExVisualizacaoTempDocCompl.getInstance();
	}
	
	private void fixTableCols(Element raiz) {
		List<Element> filhos = raiz.getChildren("table");
		for (Element table : filhos) {

			if (table.getAttribute("width") == null)
				table.setAttribute("width", "50%");

			if (table.getAttribute("border") == null && !(table.getAttribute("style") != null
					&& table.getAttribute("style").getValue().contains("border")))
				table.setAttribute("border", "1");

			Element tr = null;
			Element tableBody = table.getChild("tbody");
			if (tableBody == null)
				tableBody = table.getChild("thead");
			if (tableBody == null)
				tr = (Element) table.getChildren("tr").get(0);
			else
				tr = (Element) tableBody.getChildren("tr").get(0);

			List<Element> tds = tr.getChildren("td");
			int cols = 0;
			for (Element td : tds) {
				Attribute colspan = td.getAttribute("colspan");
				if (colspan != null)
					cols += (new Integer(colspan.getValue()));
				else
					cols++;
				fixTableCols(td);
			}

			Element colgroup = new Element("colgroup");
			for (int i = 0; i < cols; i++) {
				Element col = new Element("col");
				col.setAttribute("width", 100 / cols + "%");
				colgroup.addContent(col);
			}
			table.addContent(0, colgroup);
		}
	}

	// Executa algoritmo de comparação entre dois sets e
	// preenche as listas: inserir, excluir e atualizar.
	// \
	private void encaixar(SortedSet<ExMarca> setA, SortedSet<ExMarca> setB, Set<ExMarca> incluir, Set<ExMarca> excluir,
			Set<Par<ExMarca, ExMarca>> atualizar) {
		Iterator<ExMarca> ia = setA.iterator();
		Iterator<ExMarca> ib = setB.iterator();

		ExMarca a = null;
		ExMarca b = null;

		if (ia.hasNext())
			a = ia.next();
		if (ib.hasNext())
			b = ib.next();
		while (a != null || b != null) {
			if ((a == null) || (b != null && a.compareTo(b) > 0)) {
				// Existe em setB, mas nao existe em setA
				incluir.add(b);
				if (ib.hasNext())
					b = ib.next();
				else
					b = null;
			} else if (b == null || (a != null && b.compareTo(a) > 0)) {
				// Existe em setA, mas nao existe em setB
				excluir.add(a);
				if (ia.hasNext())
					a = ia.next();
				else
					a = null;
			} else {
				// O registro existe nos dois
				atualizar.add(new Par<ExMarca, ExMarca>(a, b));
				if (ib.hasNext())
					b = ib.next();
				else
					b = null;
				if (ia.hasNext())
					a = ia.next();
				else
					a = null;
			}
		}
		ib = null;
		ia = null;
	}

	public void atualizarMarcas(ExMobil mob) {
		atualizarMarcas(false, mob);
	}

	public void atualizarMarcas(ExDocumento doc) {
		if (doc != null) {
			for (ExMobil mob : doc.getExMobilSet())
				atualizarMarcas(false, mob);
		}
	}

	public void atualizarMarcasTemporalidade(ExMobil mob) {
		atualizarMarcas(true, mob);
	}

	private void atualizarMarcas(boolean apenasTemporalidade, ExMobil mob) {
		SortedSet<ExMarca> setA = null, setB = new TreeSet<ExMarca>();

		if (apenasTemporalidade) {
			setA = mob.getExMarcaSetTemporalidade();
		} else {
			setA = mob.getExMarcaSet();
		}

		new ExMarcadorBL(setB, mob).calcular(apenasTemporalidade);

		if (setA == null)
			setA = new TreeSet<ExMarca>();

		Set<ExMarca> incluir = new TreeSet<ExMarca>();
		Set<ExMarca> excluir = new TreeSet<ExMarca>();
		Set<Par<ExMarca, ExMarca>> atualizar = new TreeSet<Par<ExMarca, ExMarca>>();
		encaixar(setA, setB, incluir, excluir, atualizar);
		for (ExMarca i : incluir) {
			if (i.getExMobil().getExMarcaSet() == null) {
				i.getExMobil().setExMarcaSet(new TreeSet<ExMarca>());
			}
			i.getExMobil().getExMarcaSet().add(i);
			dao().gravar(i);
		}
		for (ExMarca e : excluir) {
			if (e.getExMobil().getExMarcaSet() == null) {
				e.getExMobil().setExMarcaSet(new TreeSet<ExMarca>());
			}
			e.getExMobil().getExMarcaSet().remove(e);
			dao().excluir(e);
		}
		for (Entry<ExMarca, ExMarca> pair : atualizar) {
			ExMarca a = pair.getKey();
			ExMarca b = pair.getValue();
			if (a.getExMobil().getExMarcaSet() == null) {
				a.getExMobil().setExMarcaSet(new TreeSet<ExMarca>());
			}
			a.setDpLotacaoIni(b.getDpLotacaoIni());
			a.setDpPessoaIni(b.getDpPessoaIni());
			a.setDtFimMarca(b.getDtFimMarca());
			a.setDtIniMarca(b.getDtIniMarca());
			dao().gravar(a);
		}
	}

	public void corrigirArquivamentosEmVolume(int primeiro, int ultimo, boolean efetivar) {
		
		if (true)
			throw new AplicacaoException("Funcionalidade desativada por conta do trâmite paralelo");
		Long ini = System.currentTimeMillis();
		List<ExDocumento> list = new ArrayList<ExDocumento>();

		Query query = dao().em()
				.createQuery("select distinct(doc.idDoc) from ExMarca mar " + "inner join mar.exMobil mob "
						+ "inner join mob.exDocumento doc " + "where mar.cpMarcador.idMarcador = 6"
						+ "and mob.exTipoMobil.idTipoMobil = 4 " + (primeiro > 0 ? "and doc.idDoc > " + primeiro : "")
						+ (ultimo > 0 ? "and doc.idDoc < " + ultimo : "") + " order by doc.idDoc");

		int index = 0;

		List<Long> ids = dao().arquivamentosEmVolumes(primeiro, ultimo);


		for (Long id : ids) {
			index++;
			try {
				ExDocumento doc = dao().consultar(id, ExDocumento.class, false);
				ExMovimentacao mov = doc.getUltimoVolume().getUltimaMovimentacaoNaoCancelada();
				DpPessoa pess = mov.getResp();
				DpLotacao lota = mov.getLotaResp();

				System.out.println();
				System.out.println(doc.getCodigo() + " (" + doc.getIdDoc() + ")");

				if (!Ex.getInstance().getComp().pode(ExPodeArquivarCorrente.class, pess, lota, doc.getMobilGeral()))
					System.out.println("NAO PODE");
				else if (efetivar)
					Ex.getInstance().getBL().arquivarCorrente(pess, lota, doc.getMobilGeral(), mov.getDtIniMov(), null,
							pess, false);
				dao().em().clear();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		System.out.println(index + " itens; " + (System.currentTimeMillis() - ini) + " ms");
	}

	public void marcar(ExDocumento doc) {
		ExDao.iniciarTransacao();
		atualizarMarcas(doc);
		// Nato: será que precisamos contar o número de páginas cada vez que
		// trocamos as marcas? Essa é uma operação custosa! Desabilitei para ver
		// se temos alguma reclamação.
		// for (ExMovimentacao m : doc.getExMovimentacaoSet()) {
		// m.setNumPaginas(m.getContarNumeroDePaginas());
		// dao().gravar(m);
		// }
		ExDao.commitTransacao();
	}

	/**
	 * Método criado para contar o número de páginas de um documento que foi criado
	 * antes da função que grava um documento com o total de páginas.
	 * 
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public Integer ContarNumeroDePaginas(ExDocumento doc) {
		ExDao.iniciarTransacao();
		Integer numeroDePaginas = doc.getContarNumeroDePaginas();
		doc.setNumPaginas(numeroDePaginas);
		dao().gravar(doc);
		try {
			ExDao.commitTransacao();
		} catch (Throwable e) {
			System.out.println("Erro ao contar o número de páginas do documento." + doc);
			e.printStackTrace();
		}

		return numeroDePaginas;
	}

	/**
	 * Método criado para contar o némero de páginas de uma movimentacao que foi
	 * criada antes da função que grava uma movimentacao com o total de páginas.
	 * 
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public Integer ContarNumeroDePaginas(ExMovimentacao mov) {
		ExDao.iniciarTransacao();
		Integer numeroDePaginas = mov.getContarNumeroDePaginas();
		mov.setNumPaginas(numeroDePaginas);
		dao().gravar(mov);
		try {
			ExDao.commitTransacao();
		} catch (Throwable e) {
			System.out.println("Erro ao contar o número de páginas da movimentação." + mov);
			e.printStackTrace();
		}

		return numeroDePaginas;
	}

	@SuppressWarnings("unchecked")
	public void numerarTudo(int aPartirDe) {
		List<ExDocumento> list = new ArrayList<ExDocumento>();
		int index = 0;

		do {
			long inicio = System.currentTimeMillis();
			// System.gc();
			// iniciarAlteracao();
			Query q = dao().em().createQuery("select d from ExDocumento d where d.idDoc : idDoc order by d.idDoc asc");
			dao().em().getTransaction().begin();
			list =  q.setFirstResult(index).setMaxResults(60).getResultList();
			q.setParameter("idDoc", new Long(aPartirDe));
			for (ExDocumento doc : list) {
				index++;
				try {
					for (ExMovimentacao m : doc.getExMovimentacaoSet()) {
						m.setNumPaginas(m.getContarNumeroDePaginas());
						dao().gravar(m);
					}
				} catch (Throwable e) {
					System.out.println("Erro ao marcar o doc " + doc);
					e.printStackTrace();
				}
				if (index % 50 == 0) {
					// System.gc();
				}
				System.out.print(doc.getIdDoc() + " ok - ");
			}
			dao().em().getTransaction().commit();
			dao().em().clear();
			long duracao = System.currentTimeMillis() - inicio;
			System.out.println();
			System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " " + String.valueOf(index)
					+ " numerados");
		} while (list.size() > 0);

		// System.gc();
	}

	public void marcarTudo() {
		marcarTudo(0, 0, true, false, new PrintWriter(System.out));
	}

	public void marcarTudoTemporalidade(int aPartirDe) throws Exception {
		marcarTudo(aPartirDe, 0, true, true, new PrintWriter(System.out));
	}

	@SuppressWarnings("unchecked")
	public void marcarTudo(int primeiro, int ultimo, boolean efetivar, boolean apenasTemporalidade, PrintWriter out) {
		Query query = dao().em()
				.createQuery("select d from ExDocumento d "
						+ "where d.idDoc >= :primeiro and  d.idDoc <= :ultimo  order by d.idDoc asc");
		query.setParameter("primeiro", new Long(primeiro));
		query.setParameter("ultimo", new Long(ultimo));

		out.println("-----------------------------------------------");
		out.print(new SimpleDateFormat("HH:mm:ss").format(new Date()));
		out.print(" - Remarcando documentos.");
		out.print(" Primeiro: ");
		out.println(primeiro);
		if (ultimo != 0) {
			out.print(" Ultimo: ");
			out.println(ultimo);
		}
		if (efetivar)
			out.println("***EFETIVAR!!***");
		out.println("-----------------------------------------------");

		int index = 0;

		List<ExDocumento> list;
		do {
			long inicio = System.currentTimeMillis();
			if (efetivar)
				dao().em().getTransaction().begin();

			list = query.setFirstResult(index).setMaxResults(5).getResultList();
			for (ExDocumento doc : list) {
				index++;
				StringBuilder msg = new StringBuilder();
				try {

					StringBuilder marcasAnteriores = new StringBuilder();
					for (ExMobil mob : doc.getExMobilSet()) {
						marcasAnteriores.append(mob.isGeral() ? "0" : mob.getNumSequencia());
						marcasAnteriores.append(" - ");
						marcasAnteriores.append(mob.getMarcadoresDescrCompleta(apenasTemporalidade));
						atualizarMarcasTemporalidade(mob);
					}
					StringBuilder marcasPosteriores = new StringBuilder();
					for (ExMobil mob : doc.getExMobilSet()) {
						marcasPosteriores.append(mob.isGeral() ? "0" : mob.getNumSequencia());
						marcasPosteriores.append(" - ");
						marcasPosteriores.append(mob.getMarcadoresDescrCompleta(apenasTemporalidade));
					}

					if (!marcasAnteriores.toString().equals(marcasPosteriores.toString())) {
						msg.append("Marcas:");
						msg.append("\n\tAntes: ");
						msg.append(marcasAnteriores);
						msg.append("\n\tDepois: ");
						msg.append(marcasPosteriores);
					}

				} catch (Throwable e) {
					msg.append("ERRO: ");
					msg.append(e.getMessage());
					e.printStackTrace(out);
				}

				if (msg.length() > 0) {
					msg.insert(0, "\n");
					msg.insert(0, doc.getCodigo());
					msg.insert(0, " - ");
					msg.insert(0, new SimpleDateFormat("HH:mm:ss").format(new Date()));
					msg.insert(0, "\n");
					out.println(msg);
				}

			}
			if (efetivar) {
				dao().em().getTransaction().commit();
				// System.gc();
			}
			dao().em().clear();
		} while (list.size() > 0);

		out.println("\n-----------------------------------------------");
		out.print(new SimpleDateFormat("HH:mm:ss").format(new Date()));
		out.println(" - Fim");
		out.println("-----------------------------------------------");

		// System.gc();
	}

	public void agendarPublicacaoBoletim(final DpPessoa cadastrante, final DpLotacao lotaCadastrante,
			final ExDocumento doc) throws AplicacaoException {

		try {
			if (!anexosAlternativosEstaoAssinados(doc))
				throw new AplicacaoException("O documento " + doc.getCodigo()
						+ " possui documentos filhos do tipo anexo que não estão assinados. ");

			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoDeMovimentacao.AGENDAMENTO_DE_PUBLICACAO_BOLETIM, cadastrante,
					lotaCadastrante, doc.getMobilGeral(), null, null, null, null, null, null);

			gravarMovimentacao(mov);

			new BoletimInternoBL().deixarDocDisponivelParaInclusaoEmBoletim(mov.getExDocumento());

			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao agendar publicação no boletim.", e);
		}
	}

	public void publicarBoletim(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExDocumento doc,
			final Date dtPubl) throws AplicacaoException {

		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.PUBLICACAO_BOLETIM,
					cadastrante, lotaCadastrante, doc.getMobilGeral(), dtPubl, null, null, null, null, null);

			mov.setDescrMov("Publicado em " + mov.getDtMovDDMMYY());

			gravarMovimentacao(mov);
			concluirAlteracao(mov);

			for (ExDocumento ex : new ManipuladorEntrevista(doc).obterDocsMarcados())
				notificarPublicacao(cadastrante, lotaCadastrante, ex, mov.getDtMov(), doc);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao atestar publicação no boletim.", e);
		}
	}

	public void notificarPublicacao(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExDocumento doc,
			final Date dtPubl, final ExDocumento boletim) throws AplicacaoException {

		try {
			iniciarAlteracao();
			final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.NOTIFICACAO_PUBL_BI,
					cadastrante, lotaCadastrante, doc.getMobilGeral(), dtPubl, null, null, null, null, null);

			mov.setExMobilRef(boletim.getMobilGeral());

			gravarMovimentacao(mov);
			concluirAlteracao(mov);

			try {
				String mensagemTeste = mensagemDeTeste();

				StringBuffer sb = new StringBuffer("Documento publicado no Boletim Interno " + doc.getCodigo());

				if (mensagemTeste != null)
					sb.append("\n " + mensagemTeste + "\n");

				StringBuffer sbHtml = new StringBuffer("<html><body><p>Publicado no Boletim Interno: "
						+ boletim.getCodigo() + " em " + FuncoesEL.getDataDDMMYYYY(dtPubl) + "</p> ");

				sbHtml.append("<p>Documento: " + doc.getCodigo() + "</p> ");
				sbHtml.append("<p>Descrição: " + doc.getDescrDocumento() + "</p> ");

				if (mensagemTeste != null)
					sbHtml.append("<p><b>" + mensagemTeste + "</b></p>");

				sbHtml.append("</body></html>");

				String emailsAtendentes[] = null;
				String sDest = Prop.get("bie.lista.destinatario.publicacao");

				if (sDest != null && !sDest.isEmpty())
					emailsAtendentes = sDest.split(",");

				if (emailsAtendentes != null && emailsAtendentes.length > 0) {
					Correio.enviar(null, emailsAtendentes,
							"Documento disponibilizado no Boletim Interno " + doc.getCodigo(), sb.toString(),
							sbHtml.toString());
				}
			} catch (Exception e) {

			}
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao notificar publicação.", e);
		}
	}

	public String mensagemDeTeste() {
		String mensagemTeste = null;
		if (!"prod".equals(Prop.get("/siga.ambiente")))
			mensagemTeste = Prop.get("email.mensagem.teste");
		return mensagemTeste;
	}

	// Edson: os métodos abaixo apenas repassam a chamada aos correspondentes
	// na BoletimInternoBL, e estão aqui para facilitar a referência por
	// nome do método (por exemplo, [@entrevista acaoGravar="gravarBIE"])

	public void gravarBIE(ExDocumento docBIE) throws Exception {
		new BoletimInternoBL().gravarBIE(docBIE);
	}

	public void excluirBIE(ExDocumento docBIE) throws Exception {
		new BoletimInternoBL().excluirBIE(docBIE);
	}

	public void finalizarBIE(ExDocumento docBIE) throws Exception {
		new BoletimInternoBL().finalizarBIE(docBIE);
	}

	public void pedirPublicacao(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor, final DpPessoa titular, final DpLotacao lotaTitular,
			final Date dtDispPublicacao, final String tipoMateria, final String lotPublicacao,
			final String descrPublicacao) throws AplicacaoException {

		try {
			// Verifica se o documento possui anexos alterantivos e se estes
			// anexos estão assinado
			if (!anexosAlternativosEstaoAssinados(mob.getExDocumento()))
				throw new AplicacaoException("O documento " + mob.getExDocumento().getCodigo()
						+ " possui documentos filhos do tipo anexo que não estão assinados. ");

			iniciarAlteracao();

			DatasPublicacaoDJE DJE = new DatasPublicacaoDJE(dtDispPublicacao);
			// Esse teste é feito para validar a data de disponibilização e
			// pegar a data de publicação correta.
			// Pois se a data de disponibilização for sexta o sistema coloca a
			// data de publicação para segunda.
			String sMensagem = DJE.validarDataDeDisponibilizacao(true);
			if (sMensagem != null)
				throw new AplicacaoException(sMensagem);

			if (DJE.sao17Horas())
				throw new AplicacaoException(
						"Excedido Horário de Solicitação. Tente novamente amanhã ou defina a disponibilização para um dia depois do escolhido");

			final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.PEDIDO_PUBLICACAO,
					cadastrante, lotaCadastrante, mob, dtMov, subscritor, null, titular, lotaTitular, null);

			mov.setResp(cadastrante);
			mov.setLotaResp(lotaCadastrante);

			mov.setDtDispPublicacao(dtDispPublicacao);
			
			mov.setDescrMov(
					"Disponibilização prevista para " + new SimpleDateFormat("dd/MM/yy").format(dtDispPublicacao));
			mov.setCadernoPublicacaoDje(tipoMateria);
		    gerarIdDeMovimentacao(mov);
			mov.setConteudoBlobXML("boletimadm",
					PublicacaoDJEBL.gerarXMLPublicacao(mov, tipoMateria, lotPublicacao, descrPublicacao));

			gravarMovimentacao(mov);

			// Verifica se está na base de teste
			String mensagemTeste = mensagemDeTeste();
			StringBuffer sb = new StringBuffer("Foi feita uma solicitação de remessa do documento "
					+ mob.getExDocumento().getCodigo() + " para publicação no DJE.\n ");

			if (mensagemTeste != null)
				sb.append("\n " + mensagemTeste + "\n");

			StringBuffer sbHtml = new StringBuffer("<html><body><p>Foi feita uma solicitação de remessa do documento "
					+ mob.getExDocumento().getCodigo() + " para publicação no DJE.</p> ");

			if (mensagemTeste != null)
				sbHtml.append("<p><b>" + mensagemTeste + "</b></p>");

			sbHtml.append("</body></html>");

			TreeSet<CpConfiguracaoCache> atendentes = getConf()
					.getListaPorTipo(ExTipoDeConfiguracao.ATENDER_PEDIDO_PUBLICACAO);

			ArrayList<String> emailsAtendentes = new ArrayList<String>();
			Date hoje = new Date();
			for (CpConfiguracaoCache cpConf : atendentes) {
				if (!cpConf.ativaNaData(hoje))
					continue;
				if (!(cpConf instanceof ExConfiguracaoCache))
					continue;
				ExConfiguracaoCache conf = (ExConfiguracaoCache) cpConf;
				if (conf.situacao == CpSituacaoDeConfiguracaoEnum.PODE) {
					if (conf.dpPessoa != 0) {
						DpPessoa pes = dao().consultar(conf.dpPessoa, DpPessoa.class, false);
						if (!emailsAtendentes.contains(pes.getEmailPessoaAtual())) {
							emailsAtendentes.add(pes.getEmailPessoaAtual());
						}
					} else if (conf.lotacao != 0) {
						List<DpPessoa> pessoasLotacao = CpDao.getInstance()
								.pessoasPorLotacao(conf.lotacao, false, true);
						for (DpPessoa pessoa : pessoasLotacao) {
							if (!emailsAtendentes.contains(pessoa.getEmailPessoaAtual()))
								emailsAtendentes.add(pessoa.getEmailPessoaAtual());
						}
					}
				}
			}

			Correio.enviar(null,
					emailsAtendentes.toArray(new String[emailsAtendentes.size()]),
					"Nova solicitação de publicação DJE (" + mov.getLotaCadastrante().getSiglaLotacao() + ") ",
					sb.toString(), sbHtml.toString());

			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao agendar publicação.", e);
		}
	}

	public void registrarDisponibilizacaoPublicacao(final ExMobil mob, final Date dtMov, final String pagPublicacao)
			throws AplicacaoException {

		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.DISPONIBILIZACAO,
					null, null, mob, dtMov, null, null, null, null, null);

			mov.setPagPublicacao(pagPublicacao);

			mov.setDescrMov("Documento disponibilizado no Diário em " + FuncoesEL.getDataDDMMYYYY(dtMov));

			gravarMovimentacao(mov);
			concluirAlteracao(mov);

			String mensagemTeste = mensagemDeTeste();
			StringBuffer sb = new StringBuffer(
					"Documento disponibilizado no Diário " + mob.getExDocumento().getCodigo());

			if (mensagemTeste != null)
				sb.append("\n " + mensagemTeste + "\n");

			StringBuffer sbHtml = new StringBuffer("<html><body><p>Documento disponibilizado no Diário.</p> ");

			sbHtml.append("<p>Documento: " + mob.getExDocumento().getCodigo() + "</p> ");
			sbHtml.append("<p>Descrição: " + mob.getExDocumento().getDescrDocumento() + "</p> ");
			sbHtml.append("<p>Data: " + FuncoesEL.getDataDDMMYYYY(dtMov) + "</p> ");
			sbHtml.append("<p>Página: " + pagPublicacao + "</p> ");

			if (mensagemTeste != null)
				sbHtml.append("<p><b>" + mensagemTeste + "</b></p>");

			sbHtml.append("</body></html>");

			String emailsAtendentes[] = null;
			String sDest = Prop.get("dje.lista.destinatario.publicacao");

			if (sDest != null && !sDest.isEmpty())
				emailsAtendentes = sDest.split(",");

			if (emailsAtendentes != null && emailsAtendentes.length > 0) {
				Correio.enviar(null, emailsAtendentes,
						"Documento disponibilizado no Diário " + mob.getExDocumento().getCodigo(), sb.toString(),
						sbHtml.toString());
			}

		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao registrar disponibilização.", e);
		}
	}

	public void remeterParaPublicacao(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor, final DpPessoa titular, final DpLotacao lotaTitular,
			final Date dtDispPublicacao, final String tipoMateria, final String lotPublicacao,
			final String descrPublicacao) throws Exception {

		try {

			// Verifica se o documento possui anexos alterantivos e se estes
			// anexos estão assinados
			if (!anexosAlternativosEstaoAssinados(mob.getExDocumento()))
				throw new AplicacaoException("O documento " + mob.getExDocumento().getCodigo()
						+ " possui documentos filhos do tipo anexo que não estão assinados. ");

			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoDeMovimentacao.AGENDAMENTO_DE_PUBLICACAO, cadastrante, lotaCadastrante, mob,
					dtMov, subscritor, null, titular, lotaTitular, null);

		    gerarIdDeMovimentacao(mov);
			
			mov.setResp(cadastrante);
			mov.setLotaResp(lotaCadastrante);
	

			mov.setDtDispPublicacao(dtDispPublicacao);
			mov.setCadernoPublicacaoDje(tipoMateria);

			// Calendar cal = new GregorianCalendar();
			// cal.setTime(dtDispPublicacao);
			// cal.add(Calendar.DAY_OF_MONTH, 1);
			// Date dtPublicacao = cal.getTime();

			// String nomeArq = "I-" + doc.getIdDoc();
			// Gera o arquivo zip composto de RTF e XML
			GeradorRTF gerador = new GeradorRTF();
			mov.setConteudoBlobRTF("boletim", gerador.geraRTFFOP(mob.getExDocumento()));
			mov.setConteudoBlobXML("boletimadm",
					PublicacaoDJEBL.gerarXMLPublicacao(mov, tipoMateria, lotPublicacao, descrPublicacao));

			if (tipoMateria.equals("A"))
				mov.setNmArqMov("ADM.zip");
			else
				mov.setNmArqMov("JUD.zip");

			try {
				PublicacaoDJEBL.primeiroEnvio(mov);
			} catch (Throwable t) {
				throw new Exception(t.getMessage());
			}

			// mov.setNumTRFPublicacao(numTRF);
			DatasPublicacaoDJE DJE = new DatasPublicacaoDJE(dtDispPublicacao);
			DJE.validarDataDeDisponibilizacao(true);
			mov.setDescrMov(
					"Disponibilização prevista para " + new SimpleDateFormat("dd/MM/yy").format(dtDispPublicacao));

			gravarMovimentacao(mov);
			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw e;
		}
	}
	
	public void gravarPublicacaoDOE(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor, final DpPessoa titular, final DpLotacao lotaTitular,
			final Date dtDispPublicacao, final String lotPublicacao, final String descrPublicacao, 
			final String descrMov, final String nomeArqDoc, final Long id, final Long idComprovanteEnvioDoe, ExTipoDeMovimentacao exTipoDeMov) throws Exception {

		if(id != null) {
			ExMovimentacao exMov = ExDao.getInstance().consultar(id,
					ExMovimentacao.class, false);
			
			this.cancelar(titular, lotaTitular, mob, exMov,
				dao().dt(), subscritor, titular, "");
		}

		
		try {
			final ExMovimentacao mov = criarNovaMovimentacao(
					exTipoDeMov, cadastrante, lotaCadastrante, mob,
					dtMov, subscritor, null, titular, lotaTitular, null);
			
			mov.setDtDispPublicacao(dtDispPublicacao);
			mov.setNmArqMov(nomeArqDoc);
			mov.setConteudoTpMov("text/plain");
			mov.setDescrMov(descrMov);
			mov.setNumTRFPublicacao(idComprovanteEnvioDoe);
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {			
				baos.write(descrPublicacao.getBytes());
//				baos.toByteArray();
		        gerarIdDeMovimentacao(mov);
				mov.setConteudoBlobMov2(baos.toByteArray());
			}
			
			gravarMovimentacao(mov);
			concluirAlteracao(mov);
			
		} catch (final Exception e) {
			cancelarAlteracao();
			throw e;
		}
	}
	
	public String gerarTextoPublicacaoDOE(ExDocumento doc, ExMovimentacao mov) throws IOException {
		String html = new String();
	
		if(mov == null) {
			String nomeModelo = "";
			nomeModelo = doc.getExModelo().getNmMod();
			if(nomeModelo.contains(":")) {
				nomeModelo = StringEscapeUtils.escapeHtml4(nomeModelo.substring(nomeModelo.indexOf(":")+1, nomeModelo.length()).trim());
			}
			
			html = doc.getConteudoBlobHtmlString();
			html = html.substring(html.toLowerCase().indexOf(nomeModelo.toLowerCase())+nomeModelo.length(), html.indexOf("<!-- INICIO ASSINATURA -->"));
			html = html.replace(" <strong>Assunto:</strong> ", "");
			
			Integer posicao = -1;
			Integer posicaoFinal = -1;
			ConversaoDoeEnum palavraFinal = null;
			if(doc.getExModelo().getNmMod().contains("Resolução")) {
				List<ConversaoDoeEnum> listaPalavas = Arrays.asList(ConversaoDoeEnum.values());
				for (ConversaoDoeEnum palavra : listaPalavas) {
					posicao = html.toLowerCase().indexOf(palavra.getImperativo());
					if((posicaoFinal > posicao && posicao != -1) || (posicaoFinal == -1 && posicao != -1)) {
						posicaoFinal = posicao;
						palavraFinal = palavra;
					}
				}
			}
			if(posicaoFinal != -1)
				html = palavraFinal.getGerundio() + ":\n"+ HtmlToPlainText.getText(html.substring(posicaoFinal + palavraFinal.getImperativo().length()));
			else {
				
				html = new HtmlToPlainText().getPlainText(Jsoup.parse(html), 0);

				html = Texto.removerQuebraDeLinhasExtras(html);
				html = html.substring(0, html.lastIndexOf("\n"));
			}
		} else {
			ExMovimentacao exMov = ExDao.getInstance().consultar(mov.getIdDoc(), ExMovimentacao.class, false);
			html = new String(exMov.getConteudoBlobMov());
		}
		return html;
	}

	public ExMovimentacao anexarArquivo(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor, final String nmArqMov, final DpPessoa titular,
			final DpLotacao lotaTitular, final byte[] conteudo, final String tipoConteudo, String motivo,
			Set<ExMovimentacao> pendenciasResolvidas) throws AplicacaoException {

		final ExMovimentacao mov;

		try {
			iniciarAlteracao();

			mov = criarNovaMovimentacao(ExTipoDeMovimentacao.ANEXACAO, cadastrante, lotaCadastrante,
					mob, dtMov, subscritor, null, titular, lotaTitular, null);

            gerarIdDeMovimentacao(mov);
			mov.setNmArqMov(nmArqMov);
			mov.setConteudoTpMov(tipoConteudo);
			mov.setConteudoBlobMov2(conteudo);
			mov.setDescrMov(motivo);

			gravarMovimentacao(mov);

			// Cancelar pendencias de anexação
			if (pendenciasResolvidas != null) {
				for (ExMovimentacao m : pendenciasResolvidas) {
					m.setExMovimentacaoCanceladora(mov);
					gravarMovimentacao(m);
				}
			}

			encerrarVolumeAutomatico(cadastrante, lotaCadastrante, mob, dtMov);

			concluirAlteracao(mov);
			
			return mov;
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao anexar documento.", e);
		}
	}
	
	public List<PermissaoPublicanteDto> listarPermissoesDOE(AuthHeader user) throws Exception {
		PubnetConsultaService consultaService = new PubnetConsultaService();
		List<PermissaoPublicanteDto> lista = consultaService.consultarPermissaoPublicante(user);
		return lista;
	}
	
	public MontaReciboPublicacaoDto montarReciboPublicacaoDOE(AuthHeader user, String anuncianteIdentificador,
			String cadernoIdentificador, String retrancaCodigo, String tipomaterialIdentificador, String textoPublicacao) throws Exception {
		
		PubnetEnvioService envioService = new PubnetEnvioService();
		
		ProximoSequencialDto sequencialDto = envioService.obterProximoSequencialPublicacao(user, retrancaCodigo);
		String sequencial = sequencialDto.getSequenciaPublicacao();
		MontaReciboPublicacaoDto reciboPublicacaoDto = envioService.montarReciboPublicacao(user, anuncianteIdentificador, 
									cadernoIdentificador, retrancaCodigo, tipomaterialIdentificador, sequencial, textoPublicacao);
		reciboPublicacaoDto.setProximoSequencial(sequencial);
		return reciboPublicacaoDto;
	}
	
	public EnviaPublicacaoDto enviarPublicacaoDOE(AuthHeader user, String anuncianteIdentificador,
			String cadernoIdentificador, String retrancaCodigo, String tipomaterialIdentificador, String sequencial,
			String textoPublicacao, String recibo, String reciboHash) throws Exception {

		PubnetEnvioService envioService = new PubnetEnvioService();
		EnviaPublicacaoDto reciboPublicacaoDto = envioService.enviarPublicacao(
					user, anuncianteIdentificador, cadernoIdentificador, retrancaCodigo, tipomaterialIdentificador, 
					sequencial, textoPublicacao, recibo, reciboHash);
		return reciboPublicacaoDto;
	}
	
	public MontaReciboPublicacaoCancelamentoDto montarReciboCancelPublicacaoDOE(AuthHeader user, String nomeArq) throws Exception {
		PubnetCancelamentoService cancelService = new PubnetCancelamentoService();
		MontaReciboPublicacaoCancelamentoDto reciboCancel = cancelService.montarReciboPublicacaoCancelamento(user, nomeArq);
		return reciboCancel;
	}
	
	public CancelaMaterialDto cancelarPublicacaoArquivoDOE(AuthHeader user, String nomeArquivo,
			String justificativaId, String recibo, String reciboHash) throws Exception {
		PubnetCancelamentoService cancelService = new PubnetCancelamentoService();
		CancelaMaterialDto cancelMaterialDto = cancelService.cancelarMaterial(
										user, nomeArquivo, justificativaId, recibo, reciboHash);
		return cancelMaterialDto;
	}
	
	public TokenDto gerarTokenDOE(String userName, String cpfUser, String email) throws Exception {
		PubnetConsultaService consultaService = new PubnetConsultaService();
		TokenDto token = consultaService.gerarToken(userName, cpfUser, email);
		return token;
	}


	public void anexarArquivoAuxiliar(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor, String nmArqMov, final DpPessoa titular,
			final DpLotacao lotaTitular, final byte[] conteudo, final String tipoConteudo) throws AplicacaoException {

		final ExMovimentacao mov;

		if (nmArqMov == null)
			throw new AplicacaoException("Nome do arquivo precisa ser informado");
		nmArqMov = nmArqMov.replace("_", "-");

		Set<ExMovimentacao> cancelar = new HashSet<>();
		for (ExMovimentacao m : mob.getExMovimentacaoSet()) {
			if (m.getExTipoMovimentacao() == ExTipoDeMovimentacao.ANEXACAO_DE_ARQUIVO_AUXILIAR && !m.isCancelada()
					&& m.getNmArqMov() != null && m.getNmArqMov().equals(nmArqMov))
				cancelar.add(m);
		}

		try {
			iniciarAlteracao();

			mov = criarNovaMovimentacao(ExTipoDeMovimentacao.ANEXACAO_DE_ARQUIVO_AUXILIAR, cadastrante,
					lotaCadastrante, mob, dtMov, subscritor, null, titular, lotaTitular, null);

            gerarIdDeMovimentacao(mov);
			mov.setNmArqMov(nmArqMov);
			mov.setConteudoTpMov(tipoConteudo);
			mov.setConteudoBlobMov2(conteudo);

			gravarMovimentacao(mov);
			for (ExMovimentacao m : cancelar) {
				m.setExMovimentacaoCanceladora(mov);
				dao().gravar(m);
			}

			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao anexar arquivo auxiliar.", e);
		}
	}

	private void permitirOuNaoMovimentarDestinacao(DpPessoa cadastrante, final DpLotacao lotaCadastrante, ExMobil mob) {

		Set<ExMobil> mobsVerif = new HashSet<ExMobil>();

		if (mob.isGeralDeProcesso()) {
			if (mob.doc().isEletronico())
				mobsVerif.add(mob.doc().getUltimoVolume());
			else
				mobsVerif.addAll(mob.doc().getVolumes());
		} else
			mobsVerif.add(mob);

		String sobrestados = "", emTransito = "", foraDaLota = "", apensados = "", msgFinal = "", comApensos = "";

		for (ExMobil mobVerif : mobsVerif) {

			if (mobVerif.isApensadoAVolumeDoMesmoProcesso())
				continue;

			if (lotaCadastrante != null && !mobVerif.isApensadoAVolumeDoMesmoProcesso() && !mobVerif.isAtendente(cadastrante, lotaCadastrante))
				foraDaLota += (foraDaLota.length() < 2
						? " Os seguintes volumes ou vias encontram-se em lotação diferente de " + lotaCadastrante.getSigla()
								+ ": "
						: ", ") + mobVerif.getSigla();

			if (mobVerif.isSobrestado())
				sobrestados += (sobrestados.length() < 2 ? " Os seguintes volumes ou vias encontram-se sobrestados: "
						: ", ") + mobVerif.getSigla();

			if (mobVerif.isEmTransito(cadastrante, lotaCadastrante))
				emTransito += (emTransito.length() < 2 ? " Os seguintes volumes ou vias encontram-se em trânsito: "
						: ", ") + mobVerif.getSigla();

			if (mobVerif.isApensado())
				apensados += (apensados.length() < 2
						? " Os seguintes volumes ou vias encontram-se apensados a outro documento: "
						: ", ") + mobVerif.getSigla();

			for (ExMobil apenso : mobVerif.getApensosExcetoVolumeApensadoAoProximo()) {
				comApensos += (comApensos.length() < 2
						? " Os seguintes volumes ou vias possuem outros documentos apensados: "
						: ", ") + apenso.getSigla() + " apensado a " + mobVerif.getSigla();
			}
		}

		msgFinal += foraDaLota + sobrestados + emTransito + apensados + comApensos;
		if (msgFinal.length() > 2)
			throw new AplicacaoException("não foi possível movimentar o processo." + msgFinal);
	}

	public void arquivarCorrenteAutomatico(DpPessoa cadastrante, final DpLotacao lotaCadastrante, ExMobil mob)
			throws Exception {

		arquivarCorrente(cadastrante, lotaCadastrante, mob, null, null, null, true);
		try {
			ExDao.iniciarTransacao();
			for (ExMarca marc : mob.getExMarcaSet()) {
				if (!marc.getCpMarcador().getIdMarcador().equals(CpMarcadorEnum.ARQUIVADO_CORRENTE.getId())) {
					dao().excluir(marc);
				}
			}
			ExDao.commitTransacao();
		} catch (final AplicacaoException e) {
			ExDao.rollbackTransacao();
			throw e;
		} catch (final Exception e) {
			ExDao.rollbackTransacao();
			throw new RuntimeException("Ocorreu um Erro durante a Operação", e);
		}

	}
	
	public void arquivarCorrente(DpPessoa cadastrante, final DpLotacao lotaCadastrante, ExMobil mob, Date dtMov,
			Date dtMovIni, DpPessoa subscritor, boolean automatico) {

		permitirOuNaoMovimentarDestinacao(cadastrante, lotaCadastrante, mob);

		Date dt = dtMovIni != null ? dtMovIni : dao().dt();
		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.ARQUIVAMENTO_CORRENTE,
					cadastrante, lotaCadastrante, mob, dtMov, subscritor, null, null, null, dt);
			mov.setLotaResp(lotaCadastrante);
			mov.setResp(cadastrante);

			if (automatico)
				mov.setDescrMov("Arquivamento automático.");

			gravarMovimentacao(mov);
			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao arquivar documento.", e);
		}
	}

	public void arquivarIntermediario(DpPessoa cadastrante, final DpLotacao lotaCadastrante, ExMobil mob, Date dtMov,
			DpPessoa subscritor, String descrMov) throws AplicacaoException {

		permitirOuNaoMovimentarDestinacao(cadastrante, lotaCadastrante, mob);

		Date dt = dao().dt();
		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoDeMovimentacao.ARQUIVAMENTO_INTERMEDIARIO, cadastrante, lotaCadastrante, mob,
					dtMov, subscritor, null, null, null, dt);
			mov.setDescrMov(descrMov);
			gravarMovimentacao(mov);

			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao arquivar um documento.", e);
		}
	}

	public void arquivarPermanente(DpPessoa cadastrante, final DpLotacao lotaCadastrante, ExMobil mob, Date dtMov,
			DpPessoa subscritor) throws AplicacaoException {

		permitirOuNaoMovimentarDestinacao(cadastrante, lotaCadastrante, mob);

		Date dt = dao().dt();
		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoDeMovimentacao.ARQUIVAMENTO_PERMANENTE, cadastrante, lotaCadastrante, mob,
					dtMov, subscritor, null, null, null, dt);
			gravarMovimentacao(mov);
			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao arquivar permanentemente um documento.", e);
		}
	}

	public void eliminar(DpPessoa cadastrante, final DpLotacao lotaCadastrante, ExMobil mob, Date dtMov,
			DpPessoa subscritor, ExMobil termo) throws AplicacaoException {

		permitirOuNaoMovimentarDestinacao(cadastrante, lotaCadastrante, mob);

		Date dt = dao().dt();
		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.ELIMINACAO,
					cadastrante, lotaCadastrante, mob, dtMov, subscritor, null, null, null, dt);
			mov.setExMobilRef(termo);
			gravarMovimentacao(mov);
			concluirAlteracaoParcialComRecalculoAcesso(mob);
			concluirAlteracao();
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao eliminar o documento.", e);
		}
	}

	public void sobrestar(DpPessoa cadastrante, final DpLotacao lotaCadastrante, ExMobil mob, Date dtMov, Date dtMovIni,
			DpPessoa subscritor) throws AplicacaoException {

		SortedSet<ExMobil> set = mob.getMobilEApensosExcetoVolumeApensadoAoProximo();
		for (ExMobil m : set) {
			if (!m.getExDocumento().isFinalizado())
				throw new AplicacaoException("não é possível sobrestar um documento não finalizado");
		}

		Date dt = dtMovIni != null ? dtMovIni : dao().dt();
		try {
			iniciarAlteracao();

			for (ExMobil m : set) {
				final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.SOBRESTAR,
						cadastrante, lotaCadastrante, m, dtMov, subscritor, null, null, null, dt);
				gravarMovimentacao(mov);
				concluirAlteracaoParcial(m);
			}
			concluirAlteracao();
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao sobrestar documento.", e);
		}
	}

	public void avaliarReclassificar(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExMobil mob,
									 final Date dtMov, final DpPessoa subscritor, final ExClassificacao novaClassif, 
									 final String motivo, boolean fAvaliacao) {
		
		avaliarReclassificar(cadastrante, lotaCadastrante, mob, dtMov, subscritor, null, novaClassif, motivo, fAvaliacao);
	}
	public void avaliarReclassificar(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor, final DpPessoa titular, final ExClassificacao novaClassif, 
									 final String motivo, boolean fAvaliacao) throws AplicacaoException {

		boolean fReclassif = (novaClassif != null);

		if (!fAvaliacao && !fReclassif)
			throw new AplicacaoException("não foram informados dados para a reclassificação ou avaliação");

		if (fReclassif && (motivo == null || motivo.trim().equals("")))
			throw new AplicacaoException("É necessário informar o motivo da reclassificação");

		if (fReclassif)
			for (ExMobil m : mob.getArvoreMobilesParaAnaliseDestinacao())
				if (m.isEmEditalEliminacao())
					throw new AplicacaoException("não é possível reclassificar porque " + m.getSigla()
							+ " encontra-se em edital de eliminação. Após a eliminação, esta operação estará novamente disponível.");

		Date dt = dtMov != null ? dtMov : dao().dt();

		ITipoDeMovimentacao tpMov;

		try {
			iniciarAlteracao();

			if (fAvaliacao)
				if (fReclassif)
					tpMov = ExTipoDeMovimentacao.AVALIACAO_COM_RECLASSIFICACAO;
				else
					tpMov = ExTipoDeMovimentacao.AVALIACAO;
			else
				tpMov = ExTipoDeMovimentacao.RECLASSIFICACAO;
			
			DpLotacao lotaSubscritor = Objects.nonNull(subscritor) ? subscritor.getLotacao() : null;
			DpLotacao lotaTitular = Objects.nonNull(titular) ? titular.getLotacao() : null;
			
			final ExMovimentacao mov = criarNovaMovimentacao(tpMov, cadastrante, lotaCadastrante, mob, dtMov,
					subscritor, lotaSubscritor, titular, lotaTitular, dtMov);

			if (fReclassif) {
				mov.setExClassificacao(novaClassif);
				mov.setDescrMov("Classificação documental alterada para " + novaClassif.getDescricaoSimples()
						+ " | Motivo: " + motivo);
			} else
				mov.setDescrMov(motivo);

			gravarMovimentacao(mov);

			for (ExMobil m : mob.doc().getExMobilSet()) {
				ExMobil mobPai = m.getMobilPrincipal();
				if (!mobPai.doc().equals(mob.doc()))
					atualizarMarcas(mobPai);
			}
			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao reclassificar.", e);
		}

	}

	public void simularAssinaturaDocumento(final DpPessoa cadastrante, final DpLotacao lotaCadastrante,
			final ExDocumento doc) {

		if (doc.isCancelado())
			throw new AplicacaoException("não é possível assinar um documento cancelado.");

		boolean fPreviamenteAssinado = !doc.isPendenteDeAssinatura();

		if (!fPreviamenteAssinado) {
			try {
				processarComandosEmTag(doc, "pre_assinatura");
			} catch (AplicacaoException e) {
				throw e;
			} catch (Exception e) {
				throw new Error(e);
			}
		}

		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoDeMovimentacao.ASSINATURA_DIGITAL_DOCUMENTO, cadastrante, lotaCadastrante,
					doc.getMobilGeral(), new Date(), doc.getSubscritor(), null, null, null, null);

			mov.setDescrMov(doc.getSubscritor().getNomePessoa());

			gravarMovimentacao(mov);
			concluirAlteracaoDoc(mov);

			// Verifica se o documento possui documento pai e faz a juntada
			// automática. Caso o pai seja um volume de um processo, primeiro
			// verifica se o volume está encerrado, se estiver procura o último
			// volume para juntar.

			if (doc.getExMobilPai() != null) {
				if (doc.getExMobilPai().getDoc().isProcesso() && doc.getExMobilPai().isVolumeEncerrado()) {
					doc.setExMobilPai(doc.getExMobilPai().doc().getUltimoVolume());
					gravar(cadastrante, cadastrante, lotaCadastrante, doc);
				}
				juntarAoDocumentoPai(cadastrante, lotaCadastrante, doc, mov.getDtMov(), cadastrante, cadastrante, mov);
			}

			if (doc.getExMobilAutuado() != null) {
				juntarAoDocumentoAutuado(cadastrante, lotaCadastrante, doc, mov.getDtMov(), cadastrante);
			}

			if (!fPreviamenteAssinado && !doc.isPendenteDeAssinatura()) {
				processarComandosEmTag(doc, "assinatura");
			}

		} catch (final Exception e) {
			cancelarAlteracao();

			if (e.getMessage().contains("junta"))
				throw new RuntimeException(
						"Não foi possível juntar este documento ao documento pai. O erro da juntada foi - "
								+ e.getMessage(),
						e);

			throw new RuntimeException("Erro ao assinar documento: " + e.getLocalizedMessage(), e);
		}

	}

	public void simularAssinaturaMovimentacao(DpPessoa cadastrante, DpLotacao lotaCadastrante, ExMovimentacao movAlvo,
			final Date dtMov, ITipoDeMovimentacao tpMovAssinatura) throws AplicacaoException {

		if (movAlvo == null) {
			throw new AplicacaoException("não é possível assinar uma movimentação cancelada.");
		}

		if (movAlvo.isCancelada()) {
			throw new AplicacaoException("não é possível assinar uma movimentação cancelada.");
		}

		try {
			final ExMovimentacao mov = criarNovaMovimentacao(tpMovAssinatura, cadastrante, lotaCadastrante,
					movAlvo.getExMobil(), null, null, null, null, null, null);

			mov.setExMovimentacaoRef(movAlvo);

			mov.setSubscritor(movAlvo.getSubscritor() != null ? movAlvo.getSubscritor() : movAlvo.getCadastrante());
			mov.setDescrMov(
					movAlvo.getSubscritor() != null ? movAlvo.getSubscritor().getNomePessoa() : movAlvo.getDescrMov());

			gravarMovimentacao(mov);
			concluirAlteracao(mov);
		} catch (final AplicacaoException e) {
			cancelarAlteracao();
			throw e;
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao assinar movimentação.", e);
		}

	}

	public String criarDocTeste() throws Exception {
		// Método utilizado para testar criação de documentos por webService
		/*
		 * ExService client = Service.getExService(); String s;
		 * 
		 * s = client.criarDocumento("RJ13989", "RJ13989", "CEF",
		 * "Agência Av. Rio Branco, 326", "Interno Produzido", "Formulário",
		 * "Formulário de Solicitação de Deslocamento", null, null, true,
		 * "Limitado entre pessoas",
		 * "alteracao=N%E3o&solicitacaoDentroDoPrazo=Sim&razoesDoDeslocamento=Cursos%2C+Semin%E1rios%2C+Simp%F3sios%2C+Debates%2C+F%F3runs+e+afins&internacional=N%E3o&uf=AC&cidadeCacheAC=%253C%253Fxml%2520version%253D%25221.0%2522%2520encoding%253D%2522utf-8%2522%253F%253E%253Csoap%253AEnvelope%2520xmlns%253Asoap%253D%2522http%253A%252F%252Fschemas.xmlsoap.org%252Fsoap%252Fenvelope%252F%2522%2520xmlns%253Axsi%253D%2522http%253A%252F%252Fwww.w3.org%252F2001%252FXMLSchema-instance%2522%2520xmlns%253Axsd%253D%2522http%253A%252F%252Fwww.w3.org%252F2001%252FXMLSchema%2522%253E%253Csoap%253ABody%253E%253CRETORNA_CIDADES_ESTADOResponse%2520xmlns%253D%2522http%253A%252F%252Ftempuri.org%252F%2522%253E%253CRETORNA_CIDADES_ESTADOResult%253E%253Cxs%253Aschema%2520id%253D%2522NewDataSet%2522%2520xmlns%253D%2522%2522%2520xmlns%253Axs%253D%2522http%253A%252F%252Fwww.w3.org%252F2001%252FXMLSchema%2522%2520xmlns%253Amsdata%253D%2522urn%253Aschemas-microsoft-com%253Axml-msdata%2522%253E%253Cxs%253Aelement%2520name%253D%2522NewDataSet%2522%2520msdata%253AIsDataSet%253D%2522true%2522%2520msdata%253AUseCurrentLocale%253D%2522true%2522%253E%253Cxs%253AcomplexType%253E%253Cxs%253Achoice%2520minOccurs%253D%25220%2522%2520maxOccurs%253D%2522unbounded%2522%253E%253Cxs%253Aelement%2520name%253D%2522CIDADES%2522%253E%253Cxs%253AcomplexType%253E%253Cxs%253Asequence%253E%253Cxs%253Aelement%2520name%253D%2522CODCID%2522%2520type%253D%2522xs%253Aint%2522%2520minOccurs%253D%25220%2522%2520%252F%253E%253Cxs%253Aelement%2520name%253D%2522NOMCID%2522%2520type%253D%2522xs%253Astring%2522%2520minOccurs%253D%25220%2522%2520%252F%253E%253Cxs%253Aelement%2520name%253D%2522ESTCID%2522%2520type%253D%2522xs%253Astring%2522%2520minOccurs%253D%25220%2522%2520%252F%253E%253Cxs%253Aelement%2520name%253D%2522DDDCID%2522%2520type%253D%2522xs%253Astring%2522%2520minOccurs%253D%25220%2522%2520%252F%253E%253Cxs%253Aelement%2520name%253D%2522CEPCID%2522%2520type%253D%2522xs%253Astring%2522%2520minOccurs%253D%25220%2522%2520%252F%253E%253C%252Fxs%253Asequence%253E%253C%252Fxs%253AcomplexType%253E%253C%252Fxs%253Aelement%253E%253C%252Fxs%253Achoice%253E%253C%252Fxs%253AcomplexType%253E%253C%252Fxs%253Aelement%253E%253C%252Fxs%253Aschema%253E%253Cdiffgr%253Adiffgram%2520xmlns%253Amsdata%253D%2522urn%253Aschemas-microsoft-com%253Axml-msdata%2522%2520xmlns%253Adiffgr%253D%2522urn%253Aschemas-microsoft-com%253Axml-diffgram-v1%2522%253E%253CNewDataSet%2520xmlns%253D%2522%2522%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES1%2522%2520msdata%253ArowOrder%253D%25220%2522%253E%253CCODCID%253E43%253C%252FCODCID%253E%253CNOMCID%253EACRELANDIA%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69945000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES2%2522%2520msdata%253ArowOrder%253D%25221%2522%253E%253CCODCID%253E711%253C%252FCODCID%253E%253CNOMCID%253EASSIS%2520BRASIL%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69935000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES3%2522%2520msdata%253ArowOrder%253D%25222%2522%253E%253CCODCID%253E1397%253C%252FCODCID%253E%253CNOMCID%253EBRASILEIA%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69932000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES4%2522%2520msdata%253ArowOrder%253D%25223%2522%253E%253CCODCID%253E1463%253C%252FCODCID%253E%253CNOMCID%253EBUJARI%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69923000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES5%2522%2520msdata%253ArowOrder%253D%25224%2522%253E%253CCODCID%253E1970%253C%252FCODCID%253E%253CNOMCID%253ECAPIXABA%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69922000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES6%2522%2520msdata%253ArowOrder%253D%25225%2522%253E%253CCODCID%253E2714%253C%252FCODCID%253E%253CNOMCID%253ECRUZEIRO%2520DO%2520SUL%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69980000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES7%2522%2520msdata%253ArowOrder%253D%25226%2522%253E%253CCODCID%253E3062%253C%252FCODCID%253E%253CNOMCID%253EEPITACIOLANDIA%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69934000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES8%2522%2520msdata%253ArowOrder%253D%25227%2522%253E%253CCODCID%253E3223%253C%252FCODCID%253E%253CNOMCID%253EFEIJO%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69960000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES9%2522%2520msdata%253ArowOrder%253D%25228%2522%253E%253CCODCID%253E4659%253C%252FCODCID%253E%253CNOMCID%253EJORDAO%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69975000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES10%2522%2520msdata%253ArowOrder%253D%25229%2522%253E%253CCODCID%253E5157%253C%252FCODCID%253E%253CNOMCID%253EMANCIO%2520LIMA%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69990000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES11%2522%2520msdata%253ArowOrder%253D%252210%2522%253E%253CCODCID%253E5188%253C%252FCODCID%253E%253CNOMCID%253EMANOEL%2520URBANO%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69950000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES12%2522%2520msdata%253ArowOrder%253D%252211%2522%253E%253CCODCID%253E5263%253C%252FCODCID%253E%253CNOMCID%253EMARECHAL%2520THAUMATURGO%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69983000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES13%2522%2520msdata%253ArowOrder%253D%252212%2522%253E%253CCODCID%253E6870%253C%252FCODCID%253E%253CNOMCID%253EPLACIDO%2520DE%2520CASTRO%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69928000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES14%2522%2520msdata%253ArowOrder%253D%252213%2522%253E%253CCODCID%253E7006%253C%252FCODCID%253E%253CNOMCID%253EPORTO%2520ACRE%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69921000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES15%2522%2520msdata%253ArowOrder%253D%252214%2522%253E%253CCODCID%253E7065%253C%252FCODCID%253E%253CNOMCID%253EPORTO%2520WALTER%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69982000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES16%2522%2520msdata%253ArowOrder%253D%252215%2522%253E%253CCODCID%253E7435%253C%252FCODCID%253E%253CNOMCID%253ERIO%2520BRANCO%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%2520%252F%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES17%2522%2520msdata%253ArowOrder%253D%252216%2522%253E%253CCODCID%253E7562%253C%252FCODCID%253E%253CNOMCID%253ERODRIGUES%2520ALVES%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69985000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES18%2522%2520msdata%253ArowOrder%253D%252217%2522%253E%253CCODCID%253E7913%253C%252FCODCID%253E%253CNOMCID%253ESANTA%2520ROSA%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69955000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES19%2522%2520msdata%253ArowOrder%253D%252218%2522%253E%253CCODCID%253E8871%253C%252FCODCID%253E%253CNOMCID%253ESENA%2520MADUREIRA%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69940000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES20%2522%2520msdata%253ArowOrder%253D%252219%2522%253E%253CCODCID%253E8880%253C%252FCODCID%253E%253CNOMCID%253ESENADOR%2520GUIOMARD%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69925000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES21%2522%2520msdata%253ArowOrder%253D%252220%2522%253E%253CCODCID%253E9263%253C%252FCODCID%253E%253CNOMCID%253ETARAUACA%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CDDDCID%253E68%253C%252FDDDCID%253E%253CCEPCID%253E69970000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253CCIDADES%2520diffgr%253Aid%253D%2522CIDADES22%2522%2520msdata%253ArowOrder%253D%252221%2522%253E%253CCODCID%253E9960%253C%252FCODCID%253E%253CNOMCID%253EXAPURI%253C%252FNOMCID%253E%253CESTCID%253EAC%253C%252FESTCID%253E%253CCEPCID%253E69930000%253C%252FCEPCID%253E%253C%252FCIDADES%253E%253C%252FNewDataSet%253E%253C%252Fdiffgr%253Adiffgram%253E%253C%252FRETORNA_CIDADES_ESTADOResult%253E%253C%252FRETORNA_CIDADES_ESTADOResponse%253E%253C%252Fsoap%253ABody%253E%253C%252Fsoap%253AEnvelope%253E&cidade=ACRELANDIA&diaUnico=N%E3o&numDePeriodos=1&dataIniPeriodo1=&dataFimPeriodo1=&proposto=CJF&numDePropostos=2&propostos1_pessoaSel.id=262253&propostos1_pessoaSel.sigla=RJ14054&propostos1_pessoaSel.descricao=ADRIANA+GRASSI+MARTINS+RIBEIRO+DE+ALMEIDA&ramal1=&solicitacaoComo1=Escolha&precisaAutorizacao1=Nao&propostos2_pessoaSel.id=279603&propostos2_pessoaSel.sigla=RJ13989&propostos2_pessoaSel.descricao=ANDRE+LUIS+SOUSA+DA+SILVA&ramal2=&solicitacaoComo2=Escolha&precisaAutorizacao2=Nao&contadorDePropostos=2&nomeDoEvento=&entidadePromotora=CEJ&cargaHoraria=1h&areaDeConhecimento=Escolha&abrangencia=Sim&foraDaRegiaoMetropolitana=Sim&idaDiaAnterior1=Nao&voltaDiaSubsequente1=Nao&diarias=Sim&agecccache14054=%253C%253Fxml%2520version%253D%25221.0%2522%2520encoding%253D%2522utf-8%2522%253F%253E%253Csoap%253AEnvelope%2520xmlns%253Asoap%253D%2522http%253A%252F%252Fschemas.xmlsoap.org%252Fsoap%252Fenvelope%252F%2522%2520xmlns%253Axsi%253D%2522http%253A%252F%252Fwww.w3.org%252F2001%252FXMLSchema-instance%2522%2520xmlns%253Axsd%253D%2522http%253A%252F%252Fwww.w3.org%252F2001%252FXMLSchema%2522%253E%253Csoap%253ABody%253E%253CConsultaDadosBancariosResponse%2520xmlns%253D%2522http%253A%252F%252Ftempuri.org%252F%2522%253E%253CConsultaDadosBancariosResult%253E%253CMatricula%253E14054%253C%252FMatricula%253E%253CNome%253EADRIANA%2520GRASSI%2520MARTINS%2520RIBEIRO%2520DE%2520ALMEIDA%253C%252FNome%253E%253CEmpresa%253E2%253C%252FEmpresa%253E%253CBanco%253E1%253C%252FBanco%253E%253CAgencia%253E24767%253C%252FAgencia%253E%253CContaCorrente%253E351725%253C%252FContaCorrente%253E%253C%252FConsultaDadosBancariosResult%253E%253C%252FConsultaDadosBancariosResponse%253E%253C%252Fsoap%253ABody%253E%253C%252Fsoap%253AEnvelope%253E&banco1=1&agencia1=24767&contaCorrente1=175994&agecccache13989=%253C%253Fxml%2520version%253D%25221.0%2522%2520encoding%253D%2522utf-8%2522%253F%253E%253Csoap%253AEnvelope%2520xmlns%253Asoap%253D%2522http%253A%252F%252Fschemas.xmlsoap.org%252Fsoap%252Fenvelope%252F%2522%2520xmlns%253Axsi%253D%2522http%253A%252F%252Fwww.w3.org%252F2001%252FXMLSchema-instance%2522%2520xmlns%253Axsd%253D%2522http%253A%252F%252Fwww.w3.org%252F2001%252FXMLSchema%2522%253E%253Csoap%253ABody%253E%253CConsultaDadosBancariosResponse%2520xmlns%253D%2522http%253A%252F%252Ftempuri.org%252F%2522%253E%253CConsultaDadosBancariosResult%253E%253CMatricula%253E13989%253C%252FMatricula%253E%253CNome%253EANDRE%2520LUIS%2520SOUSA%2520DA%2520SILVA%253C%252FNome%253E%253CEmpresa%253E2%253C%252FEmpresa%253E%253CBanco%253E1%253C%252FBanco%253E%253CAgencia%253E24767%253C%252FAgencia%253E%253CContaCorrente%253E175994%253C%252FContaCorrente%253E%253C%252FConsultaDadosBancariosResult%253E%253C%252FConsultaDadosBancariosResponse%253E%253C%252Fsoap%253ABody%253E%253C%252Fsoap%253AEnvelope%253E&banco2=1&agencia2=24767&contaCorrente2=175994&passagemAerea=Nao&usoDeCarroOficial=Nao"
		 * , null, false);
		 * 
		 * return s;
		 */

		return null;
	}

	public boolean deveTramitarAutomaticamente(DpPessoa titular, DpLotacao lotaTitular, ExDocumento doc) {
		final CpSituacaoDeConfiguracaoEnum idSit = Ex.getInstance().getConf().buscaSituacao(doc.getExModelo(), doc.getExTipoDocumento(),
				titular, lotaTitular, ExTipoDeConfiguracao.TRAMITE_AUTOMATICO);
		return idSit != null && idSit.isAutomaticoOuObrigatoria();
	}

	public boolean deveJuntarAutomaticamente(DpPessoa titular, DpLotacao lotaTitular, ExDocumento doc) {
		final CpSituacaoDeConfiguracaoEnum idSit = Ex.getInstance().getConf().buscaSituacao(doc.getExModelo(), doc.getExTipoDocumento(),
				titular, lotaTitular, ExTipoDeConfiguracao.JUNTADA_AUTOMATICA);
		return idSit != null && idSit.isAutomaticoOuObrigatoria();
	}

	public String assinarDocumento(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExDocumento doc,
			final Date dtMov, final byte[] pkcs7, final byte[] certificado, ITipoDeMovimentacao tpMovAssinatura, Boolean juntar,
			Boolean tramitar, Boolean exibirNoProtocolo, DpPessoa titular) throws AplicacaoException, SQLException {
		DpPessoa cosignatario = null;
		boolean fSubstituindoSubscritor = false;
		boolean fSubstituindoCosignatario = false;
		String sNome;
		Long lCPF = null;

		if (doc.isCancelado())
			throw new AplicacaoException("não é possível assinar um documento cancelado.");

		if (Utils.empty(doc.getDescrDocumento()))
			throw new AplicacaoException(
					"Não é possível assinar o documento pois a descrição está vazia. Edite-o e informe uma descrição.");

		if (!doc.isFinalizado())
			throw new AplicacaoException(
					"Não é possível assinar o documento pois não está finalizado.");

        boolean fPreviamenteAssinado = !doc.isPendenteDeAssinatura();
        boolean fPreviamenteAutenticado = doc.isAutenticadoENaoTemSubscritor();

		if (!fPreviamenteAssinado) {
			try {
				processarComandosEmTag(doc, "pre_assinatura");
			} catch (AplicacaoException e) {
				throw e;
			} catch (Exception e) {
				throw new Error(e);
			}
		}

		final byte[] cms;
		try {
			final byte[] data = doc.getConteudoBlobPdf();

			String s;

			BlucService bluc = Service.getBlucService();
			if (certificado != null) {
				// Chamar o BluC para criar o pacote assinavel
				//
				EnvelopeRequest envelopereq = new EnvelopeRequest();
				envelopereq.setCertificate(bluc.bytearray2b64(certificado));
				envelopereq.setCrl("true");
				envelopereq.setPolicy("AD-RB");
				envelopereq.setSignature(bluc.bytearray2b64(pkcs7));
				envelopereq.setSha1(bluc.bytearray2b64(bluc.calcSha1(data)));
				envelopereq.setSha256(bluc.bytearray2b64(bluc.calcSha256(data)));
				envelopereq.setTime(dtMov != null ? dtMov : dao().consultarDataEHoraDoServidor());
				EnvelopeResponse enveloperesp = bluc.envelope(envelopereq);
				if (enveloperesp.getErrormsg() != null)
					throw new Exception("BluC não conseguiu produzir o envelope AD-RB. " + enveloperesp.getErrormsg());
				cms = bluc.b642bytearray(enveloperesp.getEnvelope());
			} else {
				cms = pkcs7;
			}

			// Chamar o BluC para validar a assinatura
			//
			ValidateRequest validatereq = new ValidateRequest();
			validatereq.setEnvelope(bluc.bytearray2b64(cms));
			validatereq.setSha1(bluc.bytearray2b64(bluc.calcSha1(data)));
			validatereq.setSha256(bluc.bytearray2b64(bluc.calcSha256(data)));
			validatereq.setTime(dtMov);
			validatereq.setCrl("true");
			ValidateResponse validateresp = assertValid(bluc, validatereq);

			sNome = validateresp.getCn();

			Service.throwExceptionIfError(sNome);

			String sCPF = validateresp.getCertdetails().get("cpf0");
			Service.throwExceptionIfError(sCPF);

			lCPF = Long.valueOf(sCPF);
		} catch (final Exception e) {
			throw new RuntimeException(
					"Erro na assinatura de um documento: " + e.getMessage() == null ? "" : e.getMessage(), e);
		}

		boolean fValido = false;
		Long lMatricula = null;

		DpPessoa usuarioDoToken = null;

		// Obtem a matricula do assinante
		try {
			if (sNome == null)
				throw new AplicacaoException("não foi possível acessar o nome do assinante");
			String[] split = sNome.split(":");
			if (split.length > 1) {
				String sMatricula = split[1];
				lMatricula = Long.valueOf(sMatricula.replace("-", ""));
			}
		} catch (final Exception e) {
			throw new RuntimeException("não foi possível obter a matrícula do assinante", e);
		}

		// Verifica se a matrícula confere com o subscritor titular ou com um
		// cossignatario
		try {
			if (lMatricula != null) {
				if (doc.getSubscritor() != null && lMatricula.equals(doc.getSubscritor().getMatricula())) {
					fValido = true;
					usuarioDoToken = doc.getSubscritor();
				}
				if (!fValido && doc.getCadastrante() != null) {
					fValido = (lMatricula.equals(doc.getCadastrante().getMatricula())) && (doc.getExTipoDocumento()
							.getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_FOLHA_DE_ROSTO);
				}
				if (!fValido) {
					for (ExMovimentacao m : doc.getMobilGeral().getExMovimentacaoSet()) {
						if (m.getExTipoMovimentacao()
								 == ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO
								&& m.getExMovimentacaoCanceladora() == null
								&& lMatricula.equals(m.getSubscritor().getMatricula())) {
							fValido = true;
							usuarioDoToken = m.getSubscritor();
							continue;
						}
					}
				}
				
				if (!fValido && tpMovAssinatura == ExTipoDeMovimentacao.CONFERENCIA_COPIA_DOCUMENTO
						&& Ex.getInstance().getComp().pode(ExPodeAutenticarComCertificadoDigital.class, cadastrante, lotaCadastrante, doc)) {
					fValido = true;
				}
			}

			if (!fValido && lCPF != null) {
				if (doc.getSubscritor() != null && lCPF.equals(doc.getSubscritor().getCpfPessoa())) {
					fValido = true;
					usuarioDoToken = doc.getSubscritor();
				}
				if (!fValido && doc.getCadastrante() != null) {
					fValido = (lCPF.equals(doc.getCadastrante().getCpfPessoa())) && (doc.getExTipoDocumento()
							.getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_FOLHA_DE_ROSTO);
				}
				if (!fValido)
					for (ExMovimentacao m : doc.getMobilGeral().getExMovimentacaoSet()) {
						if (m.getExTipoMovimentacao()
								== ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO
								&& m.getExMovimentacaoCanceladora() == null
								&& lCPF.equals(m.getSubscritor().getCpfPessoa())) {
							fValido = true;
							usuarioDoToken = m.getSubscritor();
							continue;
						}
					}

				if (!fValido && tpMovAssinatura == ExTipoDeMovimentacao.CONFERENCIA_COPIA_DOCUMENTO
						&& Ex.getInstance().getComp().pode(ExPodeAutenticarComCertificadoDigital.class, cadastrante, lotaCadastrante, doc)) {
					fValido = true;
				}
				
				if ((!fValido || (fValido && doc.isAssinadoPelaPessoaComTokenOuSenha(cadastrante))) && cadastrante != titular) { 
					// Verificar se é substituto do subscritor do documento						
					if(doc.getSubscritor().equivale(titular)) {	
						fSubstituindoSubscritor = estaSubstituindoSubscritorOuCosignatario(cadastrante, lotaCadastrante, doc.getSubscritor(),
								cadastrante);
						fValido = fSubstituindoSubscritor;
					}
					
					if(!fSubstituindoSubscritor) {
						for (ExMovimentacao m : doc.getMobilGeral().getExMovimentacaoSet()) { // Verifica se é substituto de cossignatário
							if (m.getExTipoMovimentacao()
									== ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO
									&& m.getExMovimentacaoCanceladora() == null &&  titular.equivale(m.getSubscritor()) ) {
								// Verificar se é substituto do cosignatario do documento
								fSubstituindoCosignatario = estaSubstituindoSubscritorOuCosignatario(cadastrante, lotaCadastrante, m.getSubscritor(),
										cadastrante);
								if (fSubstituindoCosignatario) {
									cosignatario = titular;
									fValido = true;
									break;								
								}
							}
						}
					}
				}
			}

			if (lMatricula == null && lCPF == null)
				throw new AplicacaoException("não foi possível recuperar nem a matrícula nem o CPF do assinante");
			if (!lCPF.equals(cadastrante.getCpfPessoa()))
				throw new AplicacaoException("Usuário não permitido a utilizar o certificado digital de " + sNome);
			if (fValido == false)
				throw new AplicacaoException("Assinante não é subscritor nem cossignatario");
		} catch (final AplicacaoException e) {
			throw e;
		} catch (final Exception e) {
			throw new RuntimeException(
					"Só é permitida a assinatura digital do subscritor e dos cossignatários do documento", e);
		}

		if (usuarioDoToken != null) {
			if (doc.isAssinadoPelaPessoaComTokenOuSenha(usuarioDoToken))
				throw new AplicacaoException("Documento já assinado pelo(a) subscritor(a) ou cossignatário(a).");
		}

		String s = null;
		DpPessoa assinante = calculaAssinanteCriaMovAssinadoPor(cadastrante, lotaCadastrante, doc, dtMov, titular,
				cadastrante, cosignatario, fSubstituindoSubscritor, fSubstituindoCosignatario);
		
		try {
			// Nato: desabilita a atualização do workflow enquanto faz várias operações. Depois o workflow será
			// atualizado apenas no final.
			suprimirAtualizacaoDeWorkflow.set(true);
		
			final ExMovimentacao mov;
			try {
				if (usuarioDoToken != null && usuarioDoToken.equivale(cadastrante))
					usuarioDoToken = cadastrante;
	
				mov = criarNovaMovimentacao(tpMovAssinatura, cadastrante, lotaCadastrante, doc.getMobilGeral(), dtMov, 
						assinante, null, null, null, null);
	
				gerarIdDeMovimentacao(mov);
				
				// if (BUSCAR_CARIMBO_DE_TEMPO) {
				// mov.setConteudoTpMov(CdService.MIME_TYPE_CMS);
				mov.setConteudoTpMov(MIME_TYPE_PKCS7);
				mov.setConteudoBlobMov2(cms);
				// } else {
				// mov.setConteudoBlobMov2(pkcs7);
				// }
	
				mov.setDescrMov(assinante.getNomePessoa() + ":" + assinante.getSigla() + " [Digital]");
				
				notificar = new ExNotificar();
				notificar.cossignatario(doc, cadastrante);
				
				gravarMovimentacao(mov);
	
				concluirAlteracaoDocComRecalculoAcesso(mov);
			} catch (final Exception e) {
				throw new RuntimeException("Erro ao assinar documento: " + e.getLocalizedMessage(), e);
			}
	
			depoisDeAssinar(cadastrante, lotaCadastrante, doc, mov, dtMov, juntar, tramitar, fPreviamenteAssinado, fPreviamenteAutenticado,
					usuarioDoToken);
		
		} catch (final Exception e) {
			throw new RuntimeException("Erro ao assinar documento: " + e.getLocalizedMessage(), e);
		} finally {
			suprimirAtualizacaoDeWorkflow.remove();
			atualizarWorkflow(doc, null, null);
		}

		if (exibirNoProtocolo != null && exibirNoProtocolo) {
			exibirNoAcompanhamentoDoProtocolo(cadastrante, lotaCadastrante,
								doc.getVia(1), cadastrante);
		}
		
		return s;
	}

	public void gerarIdDeMovimentacao(ExMovimentacao mov) {
	    // Acrescentei essa gravação para que a ID da movimentação pudesse ser incluída no
        // caminho do armazenamento.
	    if (mov.getIdMov() == null)
	        dao().gravar(mov);
    }

    private void depoisDeAssinar(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExDocumento doc,
			final ExMovimentacao mov, final Date dtMov, Boolean juntar, Boolean tramitar, boolean fPreviamenteAssinado, boolean fPreviamenteAutenticado,
			DpPessoa usuarioDoToken) {
		try {
			// Verifica se o documento possui documento pai e faz a juntada
			// automática. Caso o pai seja um volume de um processo, primeiro
			// verifica se o volume está encerrado, se estiver procura o último
			// volume para juntar.

			if (juntar == null)
				juntar = deveJuntarAutomaticamente(cadastrante, lotaCadastrante, doc);

			if (doc.getExMobilPai() != null && juntar) {
				if (doc.getExMobilPai().getDoc().isProcesso() && doc.getExMobilPai().isVolumeEncerrado()) {
					doc.setExMobilPai(doc.getExMobilPai().doc().getUltimoVolume());
					gravar(cadastrante, cadastrante, lotaCadastrante, doc);
				}
				// Receber o móbil pai caso ele tenha sido tramitado para o cadastrante ou sua lotação
				if (Ex.getInstance().getComp().pode(ExPodeReceber.class, cadastrante, lotaCadastrante, doc.getExMobilPai())) 
					receber(cadastrante, cadastrante, lotaCadastrante, doc.getExMobilPai(), null);
				juntarAoDocumentoPai(cadastrante, lotaCadastrante, doc, dtMov, cadastrante, cadastrante, mov);
			}

			if (doc.getExMobilAutuado() != null) {
				juntarAoDocumentoAutuado(cadastrante, lotaCadastrante, doc, dtMov, cadastrante);
			}
		} catch (final Exception e) {
			throw new RuntimeException(
					"Não foi possível juntar este documento ao documento pai. O erro da juntada foi - "
							+ e.getMessage(),
					e);
		}

		try {
			if (tramitar == null)
				tramitar = deveTramitarAutomaticamente(cadastrante, lotaCadastrante, doc);
			if (tramitar)
				trasferirAutomaticamente(cadastrante, lotaCadastrante, usuarioDoToken, doc, fPreviamenteAssinado);
		} catch (final Exception e) {
			throw new RuntimeException("Erro ao tramitar automaticamente: " + e.getLocalizedMessage(), e);
		}

		try {
			if (doc.isAssinadoPorTodosOsSignatariosComTokenOuSenha())
				removerPapel(doc, ExPapel.PAPEL_REVISOR);
			
			if (getExConsTempDocCompleto().podeVisualizarTempDocComplCossigsSubscritor(cadastrante, lotaCadastrante)
					&& doc.isFinalizado() && doc.isAssinadoDigitalmente() && getExConsTempDocCompleto().possuiAssinaturaCossigsSubscritorHoje(doc)) {
				getExConsTempDocCompleto().removerCossigsSubscritorVisTempDocsComplFluxoDepoisAssinar(cadastrante, lotaCadastrante, usuarioDoToken, doc);
			}
		} catch (final Exception e) {
			throw new RuntimeException("Erro ao remover revisores: " + e.getLocalizedMessage(), e);
		}

		// É importante que esta seja a última operação, pois estávamos vendo um erro muito estranho
		// do Hibernate quando ele tentava deletar pela segunda vez a mesma CpMarca do banco. O erro
		// era Caused by: org.hibernate.StaleStateException: Batch update returned unexpected row count 
		// from update [0]; actual row count: 0; expected: 1. Acho que a explicação é que de alguma
		// forma, a criação de um novo procedimento, que por sua vez cria um documento filho, estava
		// alterando o documento corrente. Aí, a sessão do hibernate não refletia mais os dados gravados
		// no banco, e a tentativa de flush esbarrava em inconsistências.
		try {
			if ((!fPreviamenteAssinado && doc.isAssinadoPorTodosOsSignatariosComTokenOuSenha()) || (!fPreviamenteAutenticado && doc.isAutenticadoENaoTemSubscritor())) {
				processarComandosEmTag(doc, "assinatura");
			}
		} catch (final Exception e) {
			throw new RuntimeException("Erro ao executar procedimento pós-assinatura: " + e.getLocalizedMessage(), e);
		}
	}

	public ValidateResponse assertValid(BlucService bluc, ValidateRequest validatereq) throws Exception {
		ValidateResponse validateresp = bluc.validate(validatereq);
		if (validateresp.getErrormsg() != null) {
			if (Prop.isGovSP()) { 
				throw new AplicacaoException("BluC não conseguiu validar a assinatura digital.");
			} else {
				throw new Exception("BluC não conseguiu validar a assinatura digital. " + validateresp.getErrormsg());
			}
			
		}
			
		if (!"GOOD".equals(validateresp.getStatus()) && !"UNKNOWN".equals(validateresp.getStatus()))
			throw new Exception("BluC não validou a assinatura digital. " + validateresp.getStatus());
		return validateresp;
	}

	private void trasferirAutomaticamente(final DpPessoa cadastrante, final DpLotacao lotaCadastrante,
			DpPessoa assinante, final ExDocumento doc, boolean fPreviamenteAssinado) {
		if (doc.getLotaDestinatario() == null && doc.getDestinatario() == null)
			return;
		// Transferir automaticamente os documentos quando forem plenamente assinados
		if (!fPreviamenteAssinado && !doc.isPendenteDeAssinatura()
				&& !doc.getPrimeiroMobil().getMobilPrincipal().doc().isPendenteDeAssinatura()) {
			transferir(doc.getOrgaoExternoDestinatario(), doc.getObsOrgao(), cadastrante, lotaCadastrante,
					doc.getPrimeiroMobil().getMobilPrincipal(), null, null, null, doc.getLotaDestinatario(),
					doc.getDestinatario(), null, null, null, assinante, assinante, null, false, null, null, null, false,
					false, ExTipoDeMovimentacao.TRANSFERENCIA);
		}
	}

	public String assinarDocumentoComSenha(final DpPessoa cadastrante, final DpLotacao lotaCadastrante,
			final ExDocumento doc, final Date dtMov, final String matriculaSubscritor, final String senhaSubscritor, final boolean senhaIsPIN,
			final boolean validarSenha, final DpPessoa titular, final boolean autenticando, Boolean juntar,
			Boolean tramitar, final Boolean exibirNoProtocolo) throws Exception {

		DpPessoa subscritor = null;
		DpPessoa cosignatario = null;
		boolean fValido = false;
		boolean fSubstituindoSubscritor = false;
		boolean fSubstituindoCosignatario = false;
		final String formaAssinaturaSenha = senhaIsPIN ? "PIN" : "Senha";
		final String concordanciaAssinaturaSenha = senhaIsPIN ? "o" : "a";
		
		if (matriculaSubscritor == null || matriculaSubscritor.isEmpty())
			throw new AplicacaoException("Matrícula do Subscritor não foi informada.");

		final CpIdentidade id = dao().consultaIdentidadeCadastrante(matriculaSubscritor, true);
		// se o usuário não existir
		if (id == null)
			throw new AplicacaoException("O usuário não está cadastrado.");

		subscritor = id.getDpPessoa().getPessoaAtual();

		if (validarSenha) {
			
			if (senhaSubscritor == null || senhaSubscritor.isEmpty()) {
				throw new AplicacaoException(String.format("%s do subscritor não foi informad%s.",formaAssinaturaSenha,concordanciaAssinaturaSenha));
			}
				
			String hashAtual = null;
			boolean senhaValida = false;
			if (senhaIsPIN) { 
				if (id.getPinIdentidade() == null) {
					throw new AplicacaoException("Não há um PIN cadastrado para registrar assinatura. Utilize outra forma ou cadastre um PIN se disponível clicando <a href='/siga/app/pin/cadastro'>aqui</a>.");
				}
				
				senhaValida = Cp.getInstance().getBL().validaPinIdentidade(senhaSubscritor, id);
			} else {
			    senhaValida = ValidadorDeSenhaFabrica.getInstance().validarSenha(id, senhaSubscritor);
			}

			if (!senhaValida) {
				throw new AplicacaoException(String.format("%s do subscritor inválid%s.",formaAssinaturaSenha,concordanciaAssinaturaSenha));
			}
		}

		String s = null;
		try {
			// Nato: desabilita a atualização do workflow enquanto faz várias operações. Depois o workflow será
			// atualizado apenas no final.
			suprimirAtualizacaoDeWorkflow.set(true);
		
			if (!doc.isFinalizado()) {
				ExTipoSequencia tipoSequencia = obterTipoSequenciaPorNomeModelo(doc.getExModelo().getNmMod());
				if (!Utils.empty(tipoSequencia)) {
					throw new AplicacaoException("Documentos com numerações automáticas necessitam de ser finalizados antes de assinar. Favor finalizar o documento e depois assinar.");
				}
				finalizar(cadastrante, lotaCadastrante, null, null, doc);
			}
			
            boolean fPreviamenteAssinado = doc.isAssinadoPorTodosOsSignatariosComTokenOuSenha();
            boolean fPreviamenteAutenticado = doc.isAutenticadoENaoTemSubscritor();
	
			if (!doc.isFinalizado())
				throw new AplicacaoException("não é possível registrar assinatura de um documento não finalizado");
	
			if (doc.isCancelado())
				throw new AplicacaoException("não é possível assinar um documento cancelado.");
	
			getComp().afirmar("Usuário não tem permissão de assinar documento com senha.", ExPodeAssinarComSenha.class, subscritor, subscritor.getLotacao(), doc.getMobilGeral());
	
			// Verifica se a matrícula confere com o subscritor, titular ou com um
			// cossignatario
			if (!autenticando) {
				try {
					if (subscritor != null) {
						if (doc.getSubscritor() != null && subscritor.equivale(doc.getSubscritor())) {
							fValido = true;
						}
						if (!fValido && doc.getCadastrante() != null) {
							fValido = (subscritor.equivale(doc.getCadastrante())) && (doc.getExTipoDocumento()
									.getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_FOLHA_DE_ROSTO);
						}
						if (!fValido)
							for (ExMovimentacao m : doc.getMobilGeral().getExMovimentacaoSet()) {
								if (m.getExTipoMovimentacao() == ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO
										&& m.getExMovimentacaoCanceladora() == null
										&& subscritor.equivale(m.getSubscritor())) {
									fValido = true;
									continue;
								} 							
							}
					
						if ((!fValido || (fValido && doc.isAssinadoPelaPessoaComTokenOuSenha(subscritor))) && cadastrante != titular) { 
							
							// Verificar se é substituto do subscritor do documento						
							if(doc.getSubscritor().equivale(titular)) {	
								fSubstituindoSubscritor = estaSubstituindoSubscritorOuCosignatario(cadastrante, lotaCadastrante, doc.getSubscritor(),
										subscritor);
								fValido = fSubstituindoSubscritor;
							}
							
							if(!fSubstituindoSubscritor) {
								for (ExMovimentacao m : doc.getMobilGeral().getExMovimentacaoSet()) { // Verifica se é substituto de cossignatário
									if (m.getExTipoMovimentacao()
											== ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO
											&& m.getExMovimentacaoCanceladora() == null &&  titular.equivale(m.getSubscritor()) ) {
										// Verificar se é substituto do cosignatario do documento
										fSubstituindoCosignatario = estaSubstituindoSubscritorOuCosignatario(cadastrante, lotaCadastrante, m.getSubscritor(),
												subscritor);
										if (fSubstituindoCosignatario) {
											cosignatario = titular;
											fValido = true;
											break;								
										}
									}
								}
							}					
						}	
					}
	
					if (fValido == false)
						throw new AplicacaoException("Assinante não é subscritor nem cossignatario");
				} catch (final Exception e) {
					throw new RuntimeException(
							"Só é permitida a assinatura digital do subscritor e dos cossignatários do documento", e);
				}
			}
	
			DpPessoa assinante = calculaAssinanteCriaMovAssinadoPor(cadastrante, lotaCadastrante, doc, dtMov, titular,
					subscritor, cosignatario, fSubstituindoSubscritor, fSubstituindoCosignatario);

			final ExMovimentacao mov;
			try {
				iniciarAlteracao();
				
				//Atualiza data da primeira assinatura antes de prosseguir com o Hash de Auditoria
				atualizaDataPrimeiraAssinatura(doc,cadastrante,titular);
				
				// Hash de auditoria
				//
				final byte[] pdf = doc.getConteudoBlobPdf();
				byte[] sha256 = BlucService.calcSha256(pdf);
	
				mov = criarNovaMovimentacao(
						autenticando ? ExTipoDeMovimentacao.CONFERENCIA_COPIA_COM_SENHA
								: ExTipoDeMovimentacao.ASSINATURA_COM_SENHA,
						cadastrante, lotaCadastrante, doc.getMobilGeral(), dtMov, assinante, null, null, null, null);
				mov.setDescrMov(assinante.getNomePessoa() + ":" + assinante.getSigla() + " ["+formaAssinaturaSenha+"]");
				String cpf = Long.toString(assinante.getCpfPessoa());
				acrescentarHashDeAuditoria(mov, sha256, autenticando, assinante.getNomePessoa(), cpf, null);
				
				notificar = new ExNotificar();
				notificar.cossignatario(doc, cadastrante);
				
				gravarMovimentacao(mov);
	
				concluirAlteracaoDocComRecalculoAcesso(mov);
			} catch (final Exception e) {
				cancelarAlteracao();
				throw new RuntimeException("Erro ao registrar assinatura: " + getRootCauseMessage(e), e);
			}
			
			depoisDeAssinar(cadastrante, lotaCadastrante, doc, mov, dtMov, juntar, tramitar, fPreviamenteAssinado, fPreviamenteAutenticado,
					subscritor);

		} catch (final Exception e) {
			throw new RuntimeException("Erro ao assinar documento: " + e.getLocalizedMessage(), e);
		} finally {
			suprimirAtualizacaoDeWorkflow.remove();
			atualizarWorkflow(doc, null, null);
		}


		if (exibirNoProtocolo != null && exibirNoProtocolo) {
			exibirNoAcompanhamentoDoProtocolo(cadastrante, lotaCadastrante,
								doc.getVia(1), cadastrante);
		}

		return s;
	}

	private DpPessoa calculaAssinanteCriaMovAssinadoPor(final DpPessoa cadastrante, final DpLotacao lotaCadastrante,
			final ExDocumento doc, final Date dtMov, final DpPessoa titular, DpPessoa subscritor, DpPessoa cosignatario,
			boolean fSubstituindoSubscritor, boolean fSubstituindoCosignatario) throws SQLException {
		DpPessoa assinante;
		if (!fSubstituindoSubscritor && !fSubstituindoCosignatario ) {
			assinante = subscritor;
			if (subscritor != null) {
				if (doc.isAssinadoPelaPessoaComTokenOuSenha(subscritor))
					throw new AplicacaoException("Documento já assinado pelo(a) subscritor(a) ou cossignatário(a).");
			}
		} else {
			if (fSubstituindoSubscritor) { 
				assinante = titular.getPessoaAtual();
			} else {
				assinante = cosignatario;	
			}
			assinante = dao().consultarPorSigla(assinante);
			if (assinante != null) {
				if (doc.isAssinadoPelaPessoaComTokenOuSenha(assinante))
					throw new AplicacaoException("Documento já assinado pelo(a) subscritor(a) ou cossignatário(a).");
			}

			//Cria movimentação de Assinatura POR
			criarMovimentacaoAssinadorPor(cadastrante, lotaCadastrante, doc, dtMov, subscritor, assinante);
		}
		return assinante;
	}
	
	private String getRootCauseMessage(Exception ex) {
		Throwable cause = ex;
		String message = ex.getMessage();
		while (cause.getCause() != null && cause != cause.getCause()) {
			cause = cause.getCause();
			if (cause.getMessage() != null)
				message = cause.getMessage();
		}
		return message;
	}

	private void criarMovimentacaoAssinadorPor(final DpPessoa cadastrante, final DpLotacao lotaCadastrante,
			final ExDocumento doc, final Date dtMov, DpPessoa subscritor, DpPessoa assinante) throws SQLException {
		final ExMovimentacao movsub;
		movsub = criarNovaMovimentacao(ExTipoDeMovimentacao.ASSINATURA_POR,
				cadastrante, lotaCadastrante, doc.getMobilGeral(), dtMov, subscritor, null, null, null, null);
		movsub.setDescrMov(subscritor.getNomePessoa() + ":" + subscritor.getSigla() + " em substituição a "
				+ assinante.getNomePessoa() + ":" + assinante.getSigla());
		gravarMovimentacao(movsub);
	}

	private boolean estaSubstituindoSubscritorOuCosignatario(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final DpPessoa subscritorOuCosignatarioDoDocumento, DpPessoa subscritor)
			throws SQLException {
		Boolean fSubstituindo = false;
		if (subscritor.getId() != subscritorOuCosignatarioDoDocumento.getId()) {
			if (Ex.getInstance().getComp().pode(ExPodeAssinarPorPorConfiguracao.class, cadastrante, lotaCadastrante)) {
				DpSubstituicao dpSubstituicao = new DpSubstituicao();
				dpSubstituicao.setSubstituto(subscritor);
				dpSubstituicao.setLotaSubstituto(subscritor.getLotacao());
				List<DpSubstituicao> itens = dao().consultarSubstituicoesPermitidas(dpSubstituicao);

				// Comparar Titular com doc subscritor
				for (DpSubstituicao tit : itens) {
					if (tit.getTitular() != null && tit.getTitular().equivale(subscritorOuCosignatarioDoDocumento)) {
						fSubstituindo = true;
					}
				}
			}
		}
		return fSubstituindo;
	}

	public void assinarMovimentacaoComSenha(DpPessoa cadastrante, DpLotacao lotaCadastrante, ExMovimentacao movAlvo,
			final Date dtMov, final String matriculaSubscritor, final String senhaSubscritor, final boolean senhaIsPIN,
			final boolean validarSenha, ITipoDeMovimentacao tpMovAssinatura) throws Exception {

		DpPessoa subscritor = null;
		boolean fValido = false;
		final String formaAssinaturaSenha = senhaIsPIN ? "PIN" : "Senha";
		final String concordanciaAssinaturaSenha = senhaIsPIN ? "o" : "a";

		if (matriculaSubscritor == null || matriculaSubscritor.isEmpty())
			throw new AplicacaoException("Matrícula do Subscritor não foi informada.");

		final CpIdentidade id = dao().consultaIdentidadeCadastrante(matriculaSubscritor, true);
		// se o usuário não existir
		if (id == null)
			throw new AplicacaoException("O usuário não está cadastrado.");

		subscritor = id.getDpPessoa().getPessoaAtual();
		
		if (validarSenha) {
			
			if (senhaSubscritor == null || senhaSubscritor.isEmpty()) {
				throw new AplicacaoException(String.format("%s do subscritor não foi informad%s.",formaAssinaturaSenha,concordanciaAssinaturaSenha));
			}
				
			String hashAtual = null;
			boolean senhaValida = false;
			if (senhaIsPIN) { 
				if (id.getPinIdentidade() == null) {
					throw new AplicacaoException("Não há um PIN cadastrado para registrar assinatura. Utilize outra forma ou cadastre um PIN se disponível clicando <a href='/siga/app/pin/cadastro'>aqui</a>.");
				}
				
				hashAtual = GeraMessageDigest.calcSha256(senhaSubscritor);	
				senhaValida = id.getPinIdentidade().equals(hashAtual);
			} else {
			    senhaValida = ValidadorDeSenhaFabrica.getInstance().validarSenha(id, senhaSubscritor);
			}

			if (!senhaValida) {
				throw new AplicacaoException(String.format("%s do subscritor inválid%s.",formaAssinaturaSenha,concordanciaAssinaturaSenha));
			}
		}

		if (movAlvo != null) {
			log.info("Assinando movimentacao: " + movAlvo.toString() + " Id da movimentação: " + movAlvo.getIdMov());
		} else {
			log.warn("A movimentacao a ser assinada nao pode ser nula");
			throw new AplicacaoException("não é possível assinar uma movimentação cancelada.");
		}

		if (movAlvo.isCancelada()) {
			log.warn("A movimentacao a ser assinada está cancelada");
			throw new AplicacaoException("não é possível assinar uma movimentação cancelada.");
		}

		if (tpMovAssinatura == ExTipoDeMovimentacao.CONFERENCIA_COPIA_COM_SENHA)
			Ex.getInstance().getComp().afirmar("Usuário não tem permissão de autenticar documento com senha.", ExPodeAutenticarMovimentacaoComSenha.class, cadastrante, lotaCadastrante, movAlvo);

		if (tpMovAssinatura == ExTipoDeMovimentacao.ASSINATURA_MOVIMENTACAO_COM_SENHA)
			Ex.getInstance().getComp().afirmar("Usuário não tem permissão de assinar com senha.", ExPodeAssinarMovimentacaoComSenha.class, cadastrante, lotaCadastrante, movAlvo);

		// Verifica se a matrícula confere com o subscritor do Despacho ou
		// do
		// desentranhamento
		try {

			if (movAlvo.getExTipoMovimentacao() == ExTipoDeMovimentacao.DESPACHO
					|| movAlvo.getExTipoMovimentacao() == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA
					|| movAlvo.getExTipoMovimentacao() == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA_EXTERNA
					|| movAlvo.getExTipoMovimentacao() == ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA) {

				fValido = movAlvo.getSubscritor() != null && subscritor.equivale(movAlvo.getSubscritor());

				if (fValido == false
						&& movAlvo.getExTipoMovimentacao() == ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA) {
					log.warn("Assinante não é subscritor do desentranhamento");
					throw new AplicacaoException("Assinante não é subscritor do desentranhamento");
				}

				if (fValido == false) {
					log.warn("Assinante não é subscritor do despacho");
					throw new AplicacaoException("Assinante não é subscritor do despacho");
				}
			}

			log.info("Iniciando alteração da movimentação " + movAlvo.toString() + " Id da movimentação: "
					+ movAlvo.getIdMov());

			iniciarAlteracao();

			// Nato: isso esta errado. Deveriamos estar recebendo o cadastrante
			// e sua lotacao.
			final ExMovimentacao mov = criarNovaMovimentacao(tpMovAssinatura, cadastrante, lotaCadastrante,
					movAlvo.getExMobil(), null, null, null, null, null, null);

			mov.setDescrMov(subscritor.getNomePessoa() + ":" + subscritor.getSigla() + " ["+formaAssinaturaSenha+"]");

			mov.setExMovimentacaoRef(movAlvo);

			// Hash de auditoria
			//
			final byte[] pdf = movAlvo.getConteudoBlobpdf();
			byte[] sha256 = BlucService.calcSha256(pdf);
			String cpf = Long.toString(subscritor.getCpfPessoa());
			acrescentarHashDeAuditoria(mov, sha256,
					tpMovAssinatura == ExTipoDeMovimentacao.CONFERENCIA_COPIA_COM_SENHA,
					subscritor.getNomePessoa(), cpf, null);

			gravarMovimentacao(mov);
			concluirAlteracao(mov);

		} catch (final AplicacaoException e) {
			cancelarAlteracao();
			throw e;
		} catch (final Exception e) {
			log.error("Erro ao assinar movimentação.", e);
			cancelarAlteracao();
			throw new RuntimeException("Erro ao assinar movimentação.", e);
		}
	}

	/**
	 * @param doc
	 * @param tag
	 * @throws Exception
	 */
	public String processarComandosEmTag(final ExDocumento doc, String tag) throws Exception {
		String s = processarModelo(doc, null, tag, null, null);
		if (s != null && s.contains("Erro executando template FreeMarker"))
			throw new Exception(s);
		return extraiTag(s, tag);
	}

	private String extraiTag(String s, String tag) {
		if (s == null)
			return null;

		// retorna a resposta produzida pelo processamento da instrucao de
		// assinatura
		int start = s.indexOf("<!-- " + tag + " -->");
		int end = s.indexOf("<!-- /" + tag + " -->");
		if (start != -1 && end != -1) {
			s = s.substring(start, end);
			if (s.contains("{") && s.contains("}")) {
				s = s.substring(s.lastIndexOf("{") + 1, s.indexOf("}"));
			} else
				s = null;
		} else
			s = null;

		return s;
	}

	public ExPartes processarDadosDasPartes(final ExDocumento doc) throws Exception {
		String s = processarModelo(doc, null, "partes", null, null);
		List<ExParte> l = new ArrayList<>();

		int start = 0, end = 0;
		String ps = "";
		for (;;) {
			start = s.indexOf("<parte", end);
			end = s.indexOf("</parte>", start);
			if (start == -1 || end == -1)
				break;
			String p = s.substring(start, end + 8);
			ps += p;
			// System.out.println(p);
		}
		ExPartes partes = ExPartes.unmarshall("<partes>" + ps + "</partes>");
		return partes;
	}

	public void atualizarMovimentacoesDePartes(final ExDocumento doc, DpPessoa cadastrante, DpLotacao lotaCadastrante,
			Date dt) throws Exception {
		ExPartes partes = processarDadosDasPartes(doc);
		partes.calcular();
		ExMobil mob = doc.getMobilGeral();

		// Inicializar as listas de movimentações de controle de colaboração
		List<ExMovimentacao> movs = new ArrayList<>();
		if (mob.getExMovimentacaoSet() == null)
			mob.setExMovimentacaoSet(new TreeSet<ExMovimentacao>());
		for (ExMovimentacao mov : mob.getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			if (mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.CONTROLE_DE_COLABORACAO)
				movs.add(mov);
		}

		for (ExParte parte : partes.getPartes()) {
			String atual = parte.getDescricaoMov();
			// Localiza a parte
			ExMovimentacao encontrada = null;
			for (ExMovimentacao mov : movs) {
				if (!parte.isMesmaId(mov.getDescrMov()))
					continue;
				encontrada = mov;
				break;
			}
			if (encontrada != null && atual.equals(encontrada.getDescrMov())) {
				movs.remove(encontrada);
			} else {
				ExMovimentacao m = criarNovaMovimentacao(ExTipoDeMovimentacao.CONTROLE_DE_COLABORACAO,
						cadastrante, lotaCadastrante, mob, null, null, null, null, null, dt);
				m.setDescrMov(parte.getDescricaoMov());

				if (encontrada != null) {
					// mob.getExMovimentacaoSet().remove(encontrada);
					gravarMovimentacaoCancelamento(m, encontrada);
					// dao().excluir(encontrada);
				} else {
					gravarMovimentacao(m);
				}
			}
		}

		// Remove movimentações referentes a partes inativas
		// for (ExMovimentacao mov : movs) {
		// mob.getExMovimentacaoSet().remove(mov);
		// dao().excluir(mov);
		// }
	}

	public void assinarMovimentacao(DpPessoa cadastrante, DpLotacao lotaCadastrante, ExMovimentacao movAlvo,
			final Date dtMov, final byte[] pkcs7, final byte[] certificado, ITipoDeMovimentacao tpMovAssinatura)
			throws AplicacaoException {

		if (movAlvo != null) {
			log.info("Assinando movimentacao: " + movAlvo.toString() + " Id da movimentação: " + movAlvo.getIdMov());
		} else {
			log.warn("A movimentacao a ser assinada nao pode ser nula");
			throw new AplicacaoException("não é possível assinar uma movimentação cancelada.");
		}

		if (movAlvo.isCancelada()) {
			log.warn("A movimentacao a ser assinada está cancelada");
			throw new AplicacaoException("não é possível assinar uma movimentação cancelada.");
		}

		String sNome;
		Long lCPF = null;

		final byte[] cms;
		try {
			final byte[] data = movAlvo.getConteudoBlobpdf();

			String s;

			BlucService bluc = Service.getBlucService();
			if (certificado != null) {
				// Chamar o BluC para criar o pacote assinavel
				//
				EnvelopeRequest envelopereq = new EnvelopeRequest();
				envelopereq.setCertificate(bluc.bytearray2b64(certificado));
				envelopereq.setCrl("true");
				envelopereq.setPolicy("AD-RB");
				envelopereq.setSignature(bluc.bytearray2b64(pkcs7));
				envelopereq.setSha1(bluc.bytearray2b64(bluc.calcSha1(data)));
				envelopereq.setSha256(bluc.bytearray2b64(bluc.calcSha256(data)));
				envelopereq.setTime((dtMov != null) ? dtMov : dao().consultarDataEHoraDoServidor());
				EnvelopeResponse enveloperesp = bluc.envelope(envelopereq);
				if (enveloperesp.getErrormsg() != null)
					throw new Exception("BluC não conseguiu produzir o envelope AD-RB. " + enveloperesp.getErrormsg());
				cms = bluc.b642bytearray(enveloperesp.getEnvelope());
			} else {
				cms = pkcs7;
			}

			if (cms == null)
				throw new Exception("Assinatura inválida!");

			if (data == null)
				throw new Exception("Conteúdo inválido na validação da assinatura!");

			// Chamar o BluC para validar a assinatura
			//
			ValidateRequest validatereq = new ValidateRequest();
			validatereq.setEnvelope(bluc.bytearray2b64(cms));
			validatereq.setSha1(bluc.bytearray2b64(bluc.calcSha1(data)));
			validatereq.setSha256(bluc.bytearray2b64(bluc.calcSha256(data)));
			validatereq.setTime(dao().dt());
			validatereq.setCrl("true");
			ValidateResponse validateresp = assertValid(bluc, validatereq);

			sNome = validateresp.getCn();
			Service.throwExceptionIfError(sNome);

			String sCPF = validateresp.getCertdetails().get("cpf0");
			Service.throwExceptionIfError(sCPF);

			lCPF = Long.valueOf(sCPF);

			boolean fValido = false;
			Long lMatricula = null;

			// Obtem a matricula do assinante
			try {
				if (sNome == null)
					throw new AplicacaoException("não foi possível acessar o nome do assinante");
				String[] split = sNome.split(":");
				if (split.length > 1) {
					String sMatricula = split[1];
					lMatricula = Long.valueOf(sMatricula.replace("-", ""));
				}
			} catch (final Exception e) {
				throw new RuntimeException("não foi possível obter a matrícula do assinante", e);
			}

			// Verifica se a matrícula confere com o subscritor do Despacho ou
			// do
			// desentranhamento no caso de assinatura de despacho
			if (tpMovAssinatura == ExTipoDeMovimentacao.ASSINATURA_DIGITAL_MOVIMENTACAO
					&& (movAlvo.getExTipoMovimentacao() == ExTipoDeMovimentacao.DESPACHO
							|| movAlvo.getExTipoMovimentacao() == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA
							|| movAlvo
									.getExTipoMovimentacao() == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA_EXTERNA
							|| movAlvo.getExTipoMovimentacao() == ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA)) {

				try {
					if (lMatricula != null) {
						fValido = movAlvo.getSubscritor() != null
								&& lMatricula.equals(movAlvo.getSubscritor().getMatricula());
					}

					if (!fValido && lCPF != null) {
						fValido = movAlvo.getSubscritor() != null
								&& lCPF.equals(movAlvo.getSubscritor().getCpfPessoa());
					}

					if (lMatricula == null && lCPF == null) {
						log.warn("não foi possível recuperar nem a matrícula nem o CPF do assinante");
						throw new AplicacaoException(
								"não foi possível recuperar nem a matrícula nem o CPF do assinante");
					}

					if (fValido == false
							&& movAlvo.getExTipoMovimentacao() == ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA) {
						log.warn("Assinante não é subscritor do desentranhamento");
						throw new AplicacaoException("Assinante não é subscritor do desentranhamento");
					}

					if (fValido == false) {
						log.warn("Assinante não é subscritor do despacho");
						throw new AplicacaoException("Assinante não é subscritor do despacho");
					}

				} catch (final Exception e) {
					if (fValido == false
							&& movAlvo.getExTipoMovimentacao() == ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA) {
						throw new RuntimeException(
								"Só é permitida a assinatura digital do subscritor do desentranhamento", e);
					}
					throw new RuntimeException("Só é permitida a assinatura digital do subscritor do despacho", e);
				}
			}

			log.info("Iniciando alteração da movimentação " + movAlvo.toString() + " Id da movimentação: "
					+ movAlvo.getIdMov());
			iniciarAlteracao();

			// Nato: isso esta errado. Deveriamos estar recebendo o cadastrante
			// e sua lotacao.
			final ExMovimentacao mov = criarNovaMovimentacao(tpMovAssinatura, cadastrante, lotaCadastrante,
					movAlvo.getExMobil(), null, null, null, null, null, null);

			mov.setExMovimentacaoRef(movAlvo);

			// if (BUSCAR_CARIMBO_DE_TEMPO) {
			// mov.setConteudoTpMov(CdService.MIME_TYPE_CMS);
	        Ex.getInstance().getBL().gerarIdDeMovimentacao(mov);
	        mov.setConteudoTpMov(MIME_TYPE_PKCS7);
			mov.setConteudoBlobMov2(cms);
			// } else {
			// mov.setConteudoBlobMov2(pkcs7);
			// }

			mov.setDescrMov(sNome);

			gravarMovimentacao(mov);
			concluirAlteracao(mov);
		} catch (final AplicacaoException e) {
			cancelarAlteracao();
			throw e;
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao assinar movimentação. " + e.getMessage(), e);
		}

	}

	public void atualizarPublicacao(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExDocumento doc,
			final Date dtMov, final String pagPublicacao, final Date dtDispPublicacao) throws AplicacaoException {

		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.DISPONIBILIZACAO,
					cadastrante, lotaCadastrante, doc.getMobilGeral(), dtMov, null, null, null, null, null);

			mov.setPagPublicacao(pagPublicacao);
			mov.setDtDispPublicacao(dtDispPublicacao);

			mov.setDescrMov("Disponibilizado na internet em "
					+ new SimpleDateFormat("dd/MM/yy").format(dtDispPublicacao) + ", na página " + pagPublicacao);

			gravarMovimentacao(mov);
			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao cancelar juntada.", e);
		}

	}

	public void cancelarDocumento(DpPessoa cadastrante, final DpLotacao lotaCadastrante, ExDocumento doc) 
			throws Exception {
		
		cancelarDocumento(cadastrante, lotaCadastrante, doc, null);
	}
	
	public void cancelarDocumento(DpPessoa cadastrante, final DpLotacao lotaCadastrante, ExDocumento doc, String motivo)
			throws Exception {
		if (Prop.isGovSP() && doc.getMobilDefaultParaReceberJuntada().temDocumentosJuntados()) {
			throw new RegraNegocioException("Não é possível efetuar o cancelamento, pois o documento possui documento(s) juntado(s)");
		}
		try {
			iniciarAlteracao();
			if (getExConsTempDocCompleto().podeVisualizarTempDocComplCossigsSubscritor(cadastrante, lotaCadastrante)) {
				getExConsTempDocCompleto().removerCossigsSubscritorVisTempDocsComplFluxosRefazerCancelarExcluirDoc(cadastrante, lotaCadastrante, doc);
			}
			cancelarMovimentacoes(cadastrante, lotaCadastrante, doc, motivo);
			cancelarMovimentacoesReferencia(cadastrante, lotaCadastrante, doc);
			concluirAlteracaoDocComRecalculoAcesso(doc);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao cancelar o documento.", e);
		}
	}

	private void cancelarMovimentacoes(DpPessoa cadastrante, final DpLotacao lotaCadastrante, ExDocumento doc) 
			throws Exception {

		cancelarMovimentacoes(cadastrante, lotaCadastrante, doc, null);
	}

	private void cancelarMovimentacoes(DpPessoa cadastrante, final DpLotacao lotaCadastrante, ExDocumento doc,
									   String motivo) throws Exception {
		// Cancelar todas as criações
		//
		for (ExMobil mob : doc.getExMobilSet()) {
			while (true) {
				ExMovimentacao mov = mob.getUltimaMovimentacaoNaoCancelada();
				if (mov == null)
					break;
				cancelarMovimentacao(cadastrante, lotaCadastrante, mob, motivo);
			}
		}
	}

	private void cancelarMovimentacoesReferencia(DpPessoa cadastrante, final DpLotacao lotaCadastrante, ExDocumento doc)
			throws Exception {
		for (ExMobil mob : doc.getExMobilSet()) {
			Set<ExMovimentacao> set = mob.getExMovimentacaoReferenciaSet();
			if (set.size() > 0) {
				final Object[] aMovimentacao = set.toArray();
				for (int i = 0; i < set.size(); i++) {
					final ExMovimentacao movimentacao = (ExMovimentacao) aMovimentacao[i];
					if (!movimentacao.isCancelada()
						&& movimentacao.getExTipoMovimentacao() != ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA
						&& movimentacao.getExTipoMovimentacao() != ExTipoDeMovimentacao.CANCELAMENTO_DE_MOVIMENTACAO
						&& !(movimentacao.getExTipoMovimentacao() == ExTipoDeMovimentacao.JUNTADA 
							&& movimentacao.getExMobil().sofreuMov(ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA))) {
						Ex.getInstance().getBL().cancelar(cadastrante, lotaCadastrante, movimentacao.getExMobil(),
								movimentacao, null, cadastrante, cadastrante, "");
					}
				}
			}
		}

	}

	public void cancelarJuntada(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor, final DpPessoa titular, String textoMotivo) throws Exception {

		try {
			
			iniciarAlteracao();
			
			final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA,
					cadastrante, lotaCadastrante, mob, dtMov, subscritor, null, titular, null, null);
			
            gerarIdDeMovimentacao(mov);

			final ExMovimentacao ultMov = mob.getUltimaMovimentacao();

			mov.setDescrMov(textoMotivo);

			if (!mob.sofreuMov(ExTipoDeMovimentacao.JUNTADA_EXTERNO,
					ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA)) {
				mov.setExMovimentacaoRef(
						mov.getExMobil().getUltimaMovimentacao(ExTipoDeMovimentacao.JUNTADA));
				final ExMobil mobPai = mov.getExMovimentacaoRef().getExMobilRef();

				if (mobPai.isArquivado())
					throw new RegraNegocioException("Não é possível fazer o desentranhamento porque o documento ao qual este está juntado encontra-se arquivado.");

				ExMobil mobGrandePai = mobPai;
				if (mobPai.isApensado()) 
					mobGrandePai = (mobPai.getGrandeMestre());

				mov.setExMobilRef(mobPai);

				for (PessoaLotacaoParser resp : mobGrandePai.getAtendente()) {
					mov.setResp(resp.getPessoa());
					mov.setLotaResp(resp.getLotacao());
					break;
				}

				if (mobPai.getMobilPrincipal().isNumeracaoUnicaAutomatica() || SigaMessages.isSigaSP()) {
					List<ExArquivoNumerado> ans = mov.getExMobil().filtrarArquivosNumerados(null, true);
					armazenarCertidaoDeDesentranhamento(mov, mobPai.getMobilPrincipal(), ans, mov.obterDescrMovComPontoFinal());
				}
			} else {
				mov.setExMovimentacaoRef(
						mov.getExMobil().getUltimaMovimentacao(ExTipoDeMovimentacao.JUNTADA_EXTERNO));
				mov.setResp(mob.getExDocumento().getTitular());
				mov.setLotaResp(mob.getExDocumento().getLotaTitular());
			}
			
			if (getExConsTempDocCompleto().podeVisualizarTempDocComplCossigsSubscritor(cadastrante, lotaCadastrante)) {
				getExConsTempDocCompleto().tratarFluxoDesentrDesfJuntadaVisTempDocsCompl(mob, cadastrante, lotaCadastrante);
			}
			
			gravarMovimentacao(mov);
			
			concluirAlteracaoComRecalculoAcesso(mov);
		} catch (RegraNegocioException e) {
			cancelarAlteracao();
			throw new RegraNegocioException(e.getMessage());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao cancelar juntada.", e);
		}
	}

	/**
	 * Calcula o número original de páginas do documento que está sendo
	 * desentranhado, com base em um vetor de arquivos numerados. Depois, inclui
	 * essa informação no contexto do processador html para gerar a certidão. Também
	 * armazena o número original de páginas em campo específico da movimentação de
	 * cancelamento ou desentranhamento.
	 * 
	 * @param mov
	 * @param mob
	 * @param ans
	 * @param textoMotivo
	 * @throws AplicacaoException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	private void armazenarCertidaoDeDesentranhamento(final ExMovimentacao mov, final ExMobil mob,
			List<ExArquivoNumerado> ans, String textoMotivo)
			throws AplicacaoException, IOException, UnsupportedEncodingException, Exception {
		if (ans == null || ans.size() == 0)
			throw new AplicacaoException(
					"não foi possível obter a numeração única automática das páginas da movimentação a ser cancelada.");

		Integer paginaInicial = ans.get(0).getPaginaInicial();
		Integer paginaFinal = ans.get(ans.size() - 1).getPaginaFinal();

		mov.setNumPaginasOri(paginaFinal - paginaInicial + 1);
		criarCertidaoDeDesentranhamento(mov, mob, paginaInicial, paginaFinal, textoMotivo);
	}

	/**
	 * @param movCanceladora
	 * @param textoMotivo
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	private void criarCertidaoDeDesentranhamento(final ExMovimentacao movCanceladora, ExMobil mob, int folhaInicial,
			int folhaFinal, String textoMotivo) throws IOException, UnsupportedEncodingException, Exception {
		Map<String, String> form = new TreeMap<String, String>();
		form.put("folhaInicial", Integer.toString(folhaInicial));
		form.put("folhaFinal", Integer.toString(folhaFinal));
		form.put("textoMotivo", textoMotivo);
		
		gerarIdDeMovimentacao(movCanceladora);
		movCanceladora.setConteudoBlobForm(urlEncodedFormFromMap(form));

		// Gravar o Html //Nato
		final String strHtml = processarModelo(movCanceladora, "processar_modelo", null, null);
		movCanceladora.setConteudoBlobHtmlString(strHtml);

		// Gravar o Pdf
		final byte pdf[] = Documento.generatePdf(strHtml);
		movCanceladora.setConteudoBlobPdf(pdf);
		movCanceladora.setConteudoTpMov("application/zip");
	}

	/**
	 * Retorna o conteúdo de um Map<String,String> na forma de um array de bytes
	 * formatado de acordo com os padrões de Url Encoded Form e utilizando
	 * iso-8859-1 como charset.
	 * 
	 * @param map
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] urlEncodedFormFromMap(Map<String, String> map)
			throws IOException, UnsupportedEncodingException {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			for (String sKey : map.keySet()) {
				if (baos.size() != 0)
					baos.write("&".getBytes("iso-8859-1"));
				baos.write(sKey.getBytes("iso-8859-1"));
				baos.write('=');
				String s = map.get(sKey);
				baos.write(URLEncoder.encode(s == null ? "" : s, "iso-8859-1").getBytes());
			}
			byte[] baForm = baos.toByteArray();
			return baForm;
		}
	}
	
	public void validarCancelamentoDeUltimaMovimentacao(final DpPessoa titular, final DpLotacao lotaTitular, final ExMobil mob) {
		final ExMovimentacao exUltMovNaoCanc = mob
				.getUltimaMovimentacaoNaoCancelada();
		final ExMovimentacao exUltMov = mob.getUltimaMovimentacao();

		if (exUltMovNaoCanc.getExTipoMovimentacao() == ExTipoDeMovimentacao.CRIACAO
				&& exUltMovNaoCanc.getIdMov() == exUltMov.getIdMov()) {
			if (!Ex.getInstance().getComp().pode(ExPodeCancelarVia.class, titular, lotaTitular, mob)) {
				throw new AplicacaoException("Não é possível cancelar via");
			}
		} else {
			if (!Ex.getInstance()
					.getComp().pode(ExPodeCancelarMovimentacao.class, titular, lotaTitular,
							mob)) {
				throw new AplicacaoException(
						"Não é possível cancelar movimentação");
			}
		}
	}

	public void cancelarMovimentacao(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExMobil mob) {
		cancelarMovimentacao(cadastrante, lotaCadastrante, mob, null);
	}
	
	public void cancelarMovimentacao(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExMobil mob,
									 final String motivo) {
		try {
			boolean indexar = false;
			SortedSet<ExMobil> set = null;
			ExMovimentacao movACancelar = mob.getUltimaMovimentacaoNaoCancelada();
			switch ((ExTipoDeMovimentacao) movACancelar.getExTipoMovimentacao()) {
			case SOBRESTAR:
			case DESOBRESTAR:
				set = mob.getMobilEApensosExcetoVolumeApensadoAoProximo();
				break;
			case TRANSFERENCIA:
			case TRANSFERENCIA_EXTERNA:
			case RECEBIMENTO:
				set = mob.getMobilEApensosExcetoVolumeApensadoAoProximo();
				break;
			case DESPACHO:
			case DESPACHO_TRANSFERENCIA:
			case DESPACHO_TRANSFERENCIA_EXTERNA:
			case REDEFINICAO_NIVEL_ACESSO:
				indexar = true;
				set = new TreeSet<ExMobil>();
				set.add(mob);
				break;
			default:
				set = new TreeSet<ExMobil>();
				set.add(mob);
			}

			iniciarAlteracao();
			for (ExMobil m : set) {
				if (m.getUltimaMovimentacao().getExTipoMovimentacao()
						== ExTipoDeMovimentacao.AGENDAMENTO_DE_PUBLICACAO_BOLETIM) {
					new BoletimInternoBL().deixarDocIndisponivelParaInclusaoEmBoletim(m.doc());
				}

				final ExMovimentacao ultMov = m.getUltimaMovimentacao();
				final ExMovimentacao ultMovNaoCancelada = m.getUltimaMovimentacaoNaoCancelada(movACancelar);

				/*
				 * O correto seria a variível abaixo guardar a movimentação anterior é
				 * movimentação acima. não necessariamente será a penúltima.
				 */
				final ExMovimentacao penultMovNaoCancelada = m.getPenultimaMovimentacaoNaoCancelada();

				final ExMovimentacao mov = criarNovaMovimentacao(
						ExTipoDeMovimentacao.CANCELAMENTO_DE_MOVIMENTACAO, cadastrante, lotaCadastrante,
						m, null, null, null, null, null, null);

				mov.setDescrMov(motivo);
				gravarMovimentacao(mov);

				mov.setExMovimentacaoRef(ultMovNaoCancelada);
				if ( (ultMovNaoCancelada.getExTipoMovimentacao() == ExTipoDeMovimentacao.REDEFINICAO_NIVEL_ACESSO) 
						|| (ultMovNaoCancelada.getExTipoMovimentacao() == ExTipoDeMovimentacao.RESTRINGIR_ACESSO) ) {
					if (m.getPenultimaMovimentacaoAlteracaoNivelAcessoNaoCancelada() != null)
						mov.setExNivelAcesso(m.getPenultimaMovimentacaoAlteracaoNivelAcessoNaoCancelada().getExNivelAcesso());
					else
						mov.setExNivelAcesso(m.doc().getExNivelAcesso());
				}

				if (ultMovNaoCancelada.getExTipoMovimentacao()
						== ExTipoDeMovimentacao.AGENDAMENTO_DE_PUBLICACAO)
					PublicacaoDJEBL.cancelarRemessaPublicacao(mov);

				if (ultMovNaoCancelada.getExTipoMovimentacao()
						== ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA
						&& ultMovNaoCancelada.getExMovimentacaoRef() != null) {
					mov.setExMobilRef(ultMovNaoCancelada.getExMovimentacaoRef().getExMobilRef());
				}

				if (ultMovNaoCancelada.getExTipoMovimentacao()
						== ExTipoDeMovimentacao.CRIACAO) {
					// Exclui documento da tabela de Boletim Interno
					final String funcao = mob.getExDocumento().getForm().get("acaoExcluir");
					if (funcao != null) {
						obterMetodoPorString(funcao, mob.getExDocumento());
					}
				}

				if (penultMovNaoCancelada != null) {
					mov.setExClassificacao(penultMovNaoCancelada.getExClassificacao());
				} else {
					mov.setExClassificacao(null);
				}
				
				if (ExTipoDeMovimentacao.JUNTADA.equals(ultMovNaoCancelada.getExTipoMovimentacao())) {
					if (getExConsTempDocCompleto().podeVisualizarTempDocComplCossigsSubscritor(cadastrante, lotaCadastrante)) 
						getExConsTempDocCompleto().tratarFluxoDesentrDesfJuntadaVisTempDocsCompl(mob, cadastrante, lotaCadastrante);
				}
				
				gravarMovimentacaoCancelamento(mov, ultMovNaoCancelada);
				
				if (ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA.equals(ultMovNaoCancelada.getExTipoMovimentacao())) {
					if (getExConsTempDocCompleto().podeVisualizarTempDocComplCossigsSubscritor(cadastrante, lotaCadastrante)) 
						getExConsTempDocCompleto().tratarFluxoJuntarVisTempDocsCompl(mob, cadastrante, lotaCadastrante);
				}

				if (ultMovNaoCancelada.getExTipoMovimentacao()
						== ExTipoDeMovimentacao.ANOTACAO) {
					atualizarDnmAnotacao(m);
				}

				if (ultMovNaoCancelada.getExTipoMovimentacao()
						== ExTipoDeMovimentacao.RECLASSIFICACAO
						|| ultMovNaoCancelada.getExTipoMovimentacao()
								== ExTipoDeMovimentacao.AVALIACAO_COM_RECLASSIFICACAO)
					for (ExMobil mobRemarcar : mob.doc().getExMobilSet()) {
						ExMobil mobPai = mobRemarcar.getMobilPrincipal();
						if (!mobPai.doc().equals(mob.doc()))
							atualizarMarcas(mobPai);
					}
				
				concluirAlteracaoParcialComRecalculoAcesso(m);
			}

			concluirAlteracao();
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao cancelar movimentação.", e);
			// throw e;
		}
	}

	public void removerPendenciaDeDevolucao(Set<ExMovimentacao> movs, ExMobil mob) {
		if (movs.isEmpty())
			return;
		try {
			iniciarAlteracao();

			for (ExMovimentacao m : movs) {
				if (m.getDtFimMov() != null) {
					m.setDtFimMov(null);
				}
				gravarMovimentacao(m);
			}

			concluirAlteracao(mob);

		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao remover data de devolução.", e);
			// throw e;
		}
	}
	
	public void cancelar(final DpPessoa titular, final DpLotacao lotaTitular, final ExMobil mob,
			final ExMovimentacao movCancelar, final Date dtMovForm, final DpPessoa subscritorForm,
			final DpPessoa titularForm, String textoMotivo) throws Exception {
		cancelar(titular, lotaTitular, mob, movCancelar, dtMovForm, subscritorForm, titularForm, textoMotivo,false);
	}
	
	public void cancelar(final DpPessoa titular, final DpLotacao lotaTitular, final ExMobil mob,
			final ExMovimentacao movCancelar, final Date dtMovForm, final DpPessoa subscritorForm,
			final DpPessoa titularForm, String textoMotivo, boolean forcar) throws Exception {

		if (movCancelar.mob() != mob) {
			throw new AplicacaoException("movimentação não é relativa ao mobil informado");
		} else if (movCancelar.getExTipoMovimentacao() == ExTipoDeMovimentacao.ANEXACAO) {
			getComp().afirmar("não é possível cancelar anexo", ExPodeCancelarAnexo.class, titular, lotaTitular, mob, movCancelar);
		} else if (movCancelar.getExTipoMovimentacao() == ExTipoDeMovimentacao.PEDIDO_PUBLICACAO) {
			Ex.getInstance().getComp()
			.afirmar("Usuário não tem permissão de cancelar pedido de publicação no DJE.", ExPodeAtenderPedidoPublicacaoNoDiario.class, titular, lotaTitular, mob);
		} else if (ExTipoDeMovimentacao.hasDespacho(movCancelar.getExTipoMovimentacao())) {
			getComp().afirmar("não é possível cancelar anexo", ExPodeCancelarDespacho.class, titular, lotaTitular, mob, movCancelar);
		} else if (movCancelar.getExTipoMovimentacao() == ExTipoDeMovimentacao.VINCULACAO_PAPEL) {
			getComp().afirmar("não é possível cancelar definição de perfil", ExPodeCancelarVinculacaoPapel.class, titular, lotaTitular, movCancelar);
		} else if (movCancelar.getExTipoMovimentacao() == ExTipoDeMovimentacao.MARCACAO) {
			ExPodeCancelarMarcacao.afirmar(movCancelar, titular, lotaTitular);
		} else if (movCancelar.getExTipoMovimentacao() == ExTipoDeMovimentacao.REFERENCIA) {
			getComp().afirmar("não é possível cancelar vinculação de documento", ExPodeCancelarVinculacao.class, titular, lotaTitular, movCancelar);
		} else if (movCancelar.getExTipoMovimentacao() == ExTipoDeMovimentacao.RESTRINGIR_ACESSO) {
			getComp().afirmar("não é possível cancelar Restrição de Acesso de documento", ExPodeCancelarRestringirAcesso.class, titular, lotaTitular, movCancelar);
		} else if (!forcar && movCancelar.getExTipoMovimentacao() != ExTipoDeMovimentacao.AGENDAMENTO_DE_PUBLICACAO_BOLETIM
				&& movCancelar.getExTipoMovimentacao() != ExTipoDeMovimentacao.INCLUSAO_EM_EDITAL_DE_ELIMINACAO
				&& movCancelar.getExTipoMovimentacao() != ExTipoDeMovimentacao.SOLICITACAO_DE_ASSINATURA
				&& movCancelar.getExTipoMovimentacao() != ExTipoDeMovimentacao.CIENCIA) {
			getComp().afirmar("não é permitido cancelar esta movimentação.", ExPodeCancelar.class, titular, lotaTitular, mob, movCancelar);
		}
		
		if (movCancelar.getExTipoMovimentacao() == ExTipoDeMovimentacao.RESTRINGIR_ACESSO && mob.getDoc().isPendenteDeAssinatura()) {
			
			boolean subscritorOuCossignatario = mob.getDoc().getSubscritorECosignatarios()
													.stream()
													.anyMatch(subscritor -> movCancelar.getSubscritor().equivale(subscritor));
			
			
			if (subscritorOuCossignatario
					&& !mob.getDoc().isAssinadoPelaPessoaComTokenOuSenha(movCancelar.getSubscritor())) {
				throw new AplicacaoException("Não é permitida a exclusão do Subscritor/Cossignatário da Restrição de Acesso enquanto estiver com a assinatura pendente.");
			}
		}

		try {
			iniciarAlteracao();
			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoDeMovimentacao.CANCELAMENTO_DE_MOVIMENTACAO, titular, lotaTitular, mob,
					dtMovForm, subscritorForm, null, titularForm, null, null);

			// gravarMovimentacao(mov);
			mov.setExMovimentacaoRef(movCancelar);
			mov.setDescrMov(textoMotivo);

			if ((ExTipoDeMovimentacao.hasDocumento(movCancelar.getExTipoMovimentacao()))
					&& movCancelar.getExMobil().getMobilPrincipal().isNumeracaoUnicaAutomatica()) {
				List<ExArquivoNumerado> ans = mob.filtrarArquivosNumerados(mov.getExMovimentacaoRef(), false);
				armazenarCertidaoDeDesentranhamento(mov, mob.getMobilPrincipal(), ans, textoMotivo);
				// if (ans.size() != 1)
				// throw new AplicacaoException(
				// "não foi possível obter a numeração única automática das
				// páginas da movimentação a ser cancelada.");
				//
				// criarCertidaoDeDesentranhamento(mov, mob, ans.get(0)
				// .getPaginaInicial(), ans.get(0).getPaginaFinal());
			}

			if (movCancelar.getExTipoMovimentacao() == ExTipoDeMovimentacao.AGENDAMENTO_DE_PUBLICACAO_BOLETIM) {
				new BoletimInternoBL().deixarDocIndisponivelParaInclusaoEmBoletim(mob.doc());
			}

			gravarMovimentacaoCancelamento(mov, movCancelar);
			if (movCancelar.getExTipoMovimentacao() == ExTipoDeMovimentacao.VINCULACAO_PAPEL 
					|| movCancelar.getExTipoMovimentacao() == ExTipoDeMovimentacao.RESTRINGIR_ACESSO)
				concluirAlteracaoComRecalculoAcesso(mov);
			else {
				// concluindo só com o documento para forçar o recálculo das marcas de todos os mobiles
				concluirAlteracao(mov.mob().doc(), null, null, false);
			}
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao cancelar movimentação.", e);
			// throw e;
		}
	}

	public void cancelarMovimentacoesReplicadas(Set<ExMovimentacao> movs) throws Exception {
		try {
			iniciarAlteracao();
			for (ExMovimentacao mov : movs) {
				mov.setExMovimentacaoRef(mov);
				gravarMovimentacaoCancelamento(mov, mov);
			}

		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao cancelar movimentaçôes replicadas.", e);
		}
	}

	public void criarVia(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, 
			final DpPessoa titular, final DpLotacao lotaTitular, final ExDocumento doc) {
		criarVia(cadastrante, lotaCadastrante, titular, lotaTitular, doc, null);
		return;
	}

	public void criarVia(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, 
			final DpPessoa titular, final DpLotacao lotaTitular, final ExDocumento doc,
			Integer numVia) {
		try {
			int numSequencia = numVia == null ? (int) dao().obterProximoNumeroVia(doc) : numVia;
			if (numSequencia > 21)
				throw new AplicacaoException("Não é permitido criar mais de 21 vias, a última via permitida é a 'Z'.");

			iniciarAlteracao();

			ExMobil mob = new ExMobil();
			mob.setExTipoMobil(dao().consultar(ExTipoMobil.TIPO_MOBIL_VIA, ExTipoMobil.class, false));
			mob.setNumSequencia(numSequencia);
			mob.setExDocumento(doc);
			mob.setDnmSigla(mob.getSigla());
			if (doc.getMobilGeral().getDnmSigla() != doc.getSigla())
				doc.getMobilGeral().setDnmSigla(doc.getSigla());			
			doc.getExMobilSet().add(mob);
			mob = dao().gravar(mob);

			final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.CRIACAO, cadastrante,
					lotaCadastrante, mob, null, null, null, titular, lotaTitular, null);

			gravarMovimentacao(mov);

			concluirAlteracao(mov);
			
			
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao criar nova via.", e);
			// throw e;
		}
	}

	private ExDao dao() {
		return ExDao.getInstance();
	}

	public void desarquivarCorrente(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor) throws AplicacaoException {
		if (!mob.isArquivado())
			throw new AplicacaoException("não é possível desarquivar um documento não arquivado");

		Date dt = dao().dt();
		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoDeMovimentacao.DESARQUIVAMENTO_CORRENTE, cadastrante, lotaCadastrante, mob,
					dtMov, subscritor, null, null, null, dt);
			gravarMovimentacao(mov);
			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao reabrir.", e);
		}
	}

	public void desarquivarIntermediario(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor) throws AplicacaoException {

		if (!mob.isArquivadoIntermediario())
			throw new AplicacaoException(
					"não é possível retirar do arquivo intermediário um documento que não esteja arquivado");

		Date dt = dao().dt();
		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoDeMovimentacao.DESARQUIVAMENTO_INTERMEDIARIO, cadastrante, lotaCadastrante,
					mob, dtMov, subscritor, null, null, null, dt);
			gravarMovimentacao(mov);
			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao desarquivar intermediário.", e);
		}
	}

	public void desobrestar(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor) throws AplicacaoException {
		SortedSet<ExMobil> set = mob.getMobilEApensosExcetoVolumeApensadoAoProximo();
		for (ExMobil m : set) {
			if (!m.isSobrestado())
				throw new AplicacaoException("não é possível desobrestar um documento que não esteja sobrestado");
		}

		Date dt = dao().dt();
		try {
			iniciarAlteracao();

			for (ExMobil m : set) {
				final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.DESOBRESTAR,
						cadastrante, lotaCadastrante, m, dtMov, subscritor, null, null, null, dt);
				gravarMovimentacao(mov);
				concluirAlteracaoParcial(m);
			}
			concluirAlteracao();
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao desobrestar.", e);
		}
	}

	public void excluirMovimentacao(DpPessoa cadastrante, final DpLotacao lotaCadastrante, ExMobil mob, Long idMov) {
		try {
			iniciarAlteracao();

			final ExMovimentacao mov = dao().consultar(idMov, ExMovimentacao.class, false);
			
			if (mov != null && !ExTipoDeMovimentacao.listaTipoMovimentacoesExcluiveisFisicamente().contains(mov.getExTipoMovimentacao())) {
				throw new AplicacaoException("Não é permitido excluir a movimentação!");
			}
			
			// ExTipoDeMovimentacao.ANEXACAO
			// movDao.excluir(mov);
			ExMobil mobil = mov.getExMobil();
			excluirMovimentacao(mov);
			ExMovimentacao ultMov = mobil
					.getUltimaMovimentacao(new ITipoDeMovimentacao[] {}, 
							new ITipoDeMovimentacao[] {}, mobil, false, null, false);
			mobil.setUltimaMovimentacaoNaoCancelada(ultMov);
			mobil.setDnmDataUltimaMovimentacaoNaoCancelada(ultMov != null ? ultMov.getDtIniMov() : null);

			if (mob.doc().isPendenteDeAssinatura()
					&& ((mob.doc().isFisico() && !mob.doc().isFinalizado()) || (mob.doc().isEletronico()
							&& mob.doc().getAssinaturasEAutenticacoesComTokenOuSenhaERegistros().isEmpty()))) {
				processar(mob.getExDocumento(), true, false);
				// mob.getExDocumento().armazenar(); 
				getExConsTempDocCompleto().removerCossigsVisTempDocsComplFluxoTelaCossignatarios(cadastrante, lotaCadastrante, Arrays.asList(mov), mob.doc());
			}
			
			if(mov.getExTipoMovimentacao().equals(ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO)) {
				this.excluirRegistroDaOrdenacaoAssinatura(mob, mov, cadastrante, lotaCadastrante);
				this.excluirRestricaoAcessoPessoa(mob, mov.getSubscritor(), cadastrante, lotaCadastrante);
			}
			
			concluirAlteracao(mov);

		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao excluir movimentação.", e);
		}
	}

	@SuppressWarnings("unchecked")
	public String finalizar(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, 
				DpPessoa titular, DpLotacao lotaTitular, ExDocumento doc)
			throws AplicacaoException {
		
		verificaDocumento(titular, lotaTitular, doc);

		if (doc.isFisico() && Utils.empty(doc.getDescrDocumento()))
			throw new AplicacaoException(
					"Não é possível finalizar o documento pois a descrição está vazia. Edite-o e informe uma descrição.");

		if (doc.isFinalizado())
			throw new AplicacaoException("Documento já está finalizado.");

		ExClassificacao classificacaoValidaModelo = doc.getExModelo().getExClassificacao() == null ? null
				: doc.getExModelo().getExClassificacao().getAtual();

		if (doc.getExClassificacao() == null)
			throw new AplicacaoException(
					"Não é possível finalizar documento sem que seja informada a classificação documental.");

		if (classificacaoValidaModelo != null && classificacaoValidaModelo.isAtivo()
				&& !doc.getExClassificacao().equivale(classificacaoValidaModelo))
			throw new AplicacaoException(
					"Classificação documental do modelo foi alterada. Edite e grave o documento para atualizá-lo.");

		if (doc.getExClassificacao().getAtual() == null)
			throw new AplicacaoException("Classificação documental encerrada. Edite o documento para escolher outra.");

		if (doc.getExModelo() != null && doc.getExModelo().isFechado()) {
			throw new AplicacaoException("Este modelo não está mais em uso. Selecione outro na tela de edição");
		}

		if (doc.getExModelo() != null && !doc.getExModelo().isAtivo()) {
			throw new AplicacaoException("Este modelo foi alterado. Edite-o para atualizá-lo");
		}

		if (!doc.isEletronico() && doc.isProcesso() && doc.getExMobilPai() != null
				&& doc.getExMobilPai().getExDocumento().isProcesso()
				&& doc.getExMobilPai().getExDocumento().isEletronico())
			throw new AplicacaoException("Não é possível criar Subprocesso físico de processo eletrônico.");

		
		getComp().afirmar("O usuário não pode ser subscritor do documento", ExPodeSerSubscritor.class, cadastrante, lotaCadastrante, doc);
		
		if (doc.isProcesso() && doc.getMobilGeral().temAnexos())
			throw new AplicacaoException(
					"Processos não podem possuir anexos antes da finalização. Exclua todos os anexos para poder finalizar. Os anexos poderão ser incluídos no primeiro volume após a finalização.");

		Date dt = dao().dt();
		Calendar c = Calendar.getInstance();
		c.setTime(dt);

		if (doc.getDtDoc() != null) {
			Calendar dtDocCalendar = Calendar.getInstance();
			dtDocCalendar.setTime(doc.getDtDoc());

			if (c.before(dtDocCalendar))
				throw new AplicacaoException("não é permitido criar documento com data futura");
		}
		
		// Acrescenta o TMP na lista de notificações do WF, para que seja atualizado o evento com o código que será recebido depois da finalização
		atualizarWorkFlowAdicionarCodigoDeDocumento(doc.getCodigo());

		try {
			// atualizando a classificacao do documento
			doc.setExClassificacao(doc.getExClassificacao().getAtual() != null ? doc.getExClassificacao().getAtual()
					: doc.getExClassificacao());

			// Pega a data sem horas, minutos e segundos...
			if (doc.getDtDoc() == null) {
				final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				doc.setDtDoc(sdf.parse(sdf.format(c.getTime())));
			}

			if (doc.getOrgaoUsuario() == null)
				doc.setOrgaoUsuario(doc.getLotaCadastrante().getOrgaoUsuario());

			/*
			 * Desabilita se configuracao ativa numeração realizada pelo Select Max. Numeração
			 * controlada pela table EX_DOCUMENTO_NUMERACAO
			 */
			if (Prop.getBool("controlar.numeracao.expediente") || SigaMessages.isSigaSP()) {
				doc.setAnoEmissao((long) c.get(Calendar.YEAR));
				doc.setNumExpediente(obterNumeroDocumento(doc));
			} else {
				if (doc.getNumExpediente() == null)
					doc.setNumExpediente(obterProximoNumero(doc));
			}
			
			//Esse metodo deve estar acima do doc.setDtFinalizacao(dt), devido a flag de doc.isFinalizado()
			gerarTipoSequenciaGenerica(doc);

			doc.setDtFinalizacao(dt);

			if (doc.getExMobilPai() != null) {
				if (doc.getExMobilPai().doc().isProcesso() && doc.isProcesso()) {
					getComp().afirmar("Documento filho não pode ser criado nessas condições.", ExPodeCriarSubprocesso.class, cadastrante, doc.getLotaCadastrante(), doc.getExMobilPai());
					int n = dao().obterProximoNumeroSubdocumento(doc.getExMobilPai());
					doc.setNumSequencia(n);
				}
			}

			Set<ExVia> setVias = doc.getSetVias();

			processar(doc, false, false);
			// doc.armazenar();

			doc.setNumPaginas(doc.getContarNumeroDePaginas());
			
			dao().gravar(doc);
			
			if (doc.getSubscritor() != null) {
				if ((doc.getCadastrante() == null || !doc.getCadastrante().equivale(doc.getSubscritor())) && usuarioExternoTemQueAssinar(doc, doc.getSubscritor())) {
					enviarEmailParaUsuarioExternoAssinarDocumento(doc, doc.getSubscritor());
				}
			}	
			
			if (doc.getExFormaDocumento().getExTipoFormaDoc().isExpediente()) {
				for (final ExVia via : setVias) {
					Integer numVia = null;
					if (via.getCodVia() != null)
						numVia = Integer.parseInt(via.getCodVia());
					if (numVia == null) {
						numVia = 1;
					}
					criarVia(cadastrante, lotaCadastrante, titular, lotaTitular, doc, numVia);
				}
			} else {
				criarVolume(cadastrante, lotaCadastrante, titular, lotaTitular, doc);
			}

			concluirAlteracaoDocComRecalculoAcesso(doc);
			if (getExConsTempDocCompleto().podeVisualizarTempDocComplCossigsSubscritor(cadastrante, lotaCadastrante) 
							&& doc.isFinalizado() && getExConsTempDocCompleto().possuiInclusaoCossigsSubscritor(doc)) {
				//Incluir Mov Papel todos Cossignatarios e subscritor 
				getExConsTempDocCompleto().incluirCossigsSubscrVisTempDocsComplFluxoFinalizar(cadastrante, lotaCadastrante, doc);
				//Remover todas movs Papel todos Cossignatarios e subscritor 
				getExConsTempDocCompleto().removerCossigsSubscrVisTempDocsComplFluxoFinalizar(cadastrante, lotaCadastrante, doc);
			}

			if (setVias == null || setVias.size() == 0)
				criarVia(cadastrante, lotaCadastrante, titular, lotaTitular, doc, null);

			String s = processarComandosEmTag(doc, "finalizacao");
			
			ExMobil mob = doc.getMobilDefaultParaReceberJuntada();
			if (doc.getExMobilAutuado() != null)
				juntarAoDocumentoAutuado(cadastrante, lotaCadastrante, doc, null, cadastrante);
			
			notificar = new ExNotificar();
			notificar.responsavelPelaAssinatura(doc, cadastrante);
			
			return s;
		} catch (final Exception e) {
			throw new RuntimeException("Erro ao finalizar o documento: " + e.getMessage(), e);
		}
	}
	
	public void verificaDocumento(final DpPessoa titular, final DpLotacao lotaTitular, final ExDocumento doc) {
		if ((doc.getSubscritor() == null)
				&& !doc.isExternoCapturado()
				&& !doc.isExterno()
				&& ((doc.isProcesso() && doc.isEletronico()) || !doc
						.isProcesso())) {
			throw new AplicacaoException(
					"É necessário definir um subscritor para o documento.");
		}

		if (doc.getDestinatario() == null
				&& doc.getLotaDestinatario() == null
				&& (doc.getNmDestinatario() == null || doc.getNmDestinatario()
						.trim().equals(""))
				&& doc.getOrgaoExternoDestinatario() == null
				&& (doc.getNmOrgaoExterno() == null || doc.getNmOrgaoExterno()
						.trim().equals(""))
				&& titular != null && lotaTitular != null) {
			final CpSituacaoDeConfiguracaoEnum idSit = Ex
					.getInstance()
					.getConf()
					.buscaSituacao(doc.getExModelo(), titular,
							lotaTitular,
							ExTipoDeConfiguracao.DESTINATARIO);
			if (idSit == CpSituacaoDeConfiguracaoEnum.OBRIGATORIO) {
				throw new AplicacaoException("Para documentos do modelo "
						+ doc.getExModelo().getNmMod()
						+ ", é necessário definir um destinatário");
			}
		}

		if (doc.getExClassificacao() == null) {
			throw new AplicacaoException(
					"É necessário informar a classificação documental.");
		}

	}

	public Long obterProximoNumero(ExDocumento doc) throws Exception {
		doc.setAnoEmissao(Long.valueOf(new Date().getYear()) + 1900);

		Long num = dao().obterProximoNumero(doc, doc.getAnoEmissao());

		if (num == null) {
			// Verifica se reiniciar a numeração ou continua com a numeração
			// anterior
			if (getComp().pode(ExPodeReiniciarNumeracao.class, doc)) {
				num = 1L;
			} else {
				// Obtém o próximo número considerando os anos anteriores até
				// 2006
				Long anoEmissao = doc.getAnoEmissao();
				while (num == null && anoEmissao > 2005) {
					anoEmissao = anoEmissao - 1;
					num = dao().obterProximoNumero(doc, anoEmissao);
				}
				// Se continuar null é porque nunca foi criado documento para
				// este formato.
				if (num == null)
					num = 1L;
			}
		}

		return num;
	}

	public Long obterNumeroDocumento(ExDocumento doc) throws Exception {
		// Obtém Número definitivo de documento de acordo com órgão/forma/ano via WS
		// para desacoplamento da transação
		ExService exService = Service.getExService();
		String numeroDocumento = null;

		numeroDocumento = exService.obterNumeracaoExpediente(doc.getOrgaoUsuario().getIdOrgaoUsuIni(),
				doc.getExFormaDocumento().getIdFormaDoc(), doc.getAnoEmissao());

		return Long.parseLong(numeroDocumento);
	}
	
	public Long obterSequencia(Long ano, Integer tipo, String zerarInicioAno) throws Exception {
		ExService exService = Service.getExService();
		String sequencia = null;
		
		sequencia = exService.obterSequencia(tipo, ano, zerarInicioAno);
		 
		return Long.parseLong(sequencia);
	}
	
	private String obterSequenciaAno(Long ano, ExTipoSequencia tipoSequencia) throws Exception {
		
		String sequencia = obterSequencia(ano, 
				tipoSequencia.getidTipoSequencia().intValue(), 
				tipoSequencia.getZerarInicioAno()).toString();
		
		return  sequencia + "/" + ano;
	}
	
	private void gerarTipoSequenciaGenerica(ExDocumento doc) throws Exception {
		if (doc != null) {
			ExTipoSequencia tipoSequencia = obterTipoSequenciaPorNomeModelo(doc.getExModelo().getNmMod());
			
			if (!Utils.empty(tipoSequencia) && !doc.isFinalizado()) {
				doc.setNumeroSequenciaGenerica(obterSequenciaAno(doc.getAnoEmissao(), tipoSequencia));
				doc.setCodigoUnico(formatarCodigoUnico(doc.getNumeroSequenciaGenerica()));
				doc.setDigitoVerificadorCodigoUnico(calcularDigitoVerificador(doc.getCodigoUnico()));
			}
		}
	}
	
	private ExTipoSequencia obterTipoSequenciaPorNomeModelo(String nomeModelo) {
		return dao().obterTipoSequencia(nomeModelo);
	}
	
	public void criarVolume(DpPessoa cadastrante, DpLotacao lotaCadastrante,
			DpPessoa titular, DpLotacao lotaTitular,
			ExDocumento doc) throws AplicacaoException {
		try {
			iniciarAlteracao();

			ExMobil mob = new ExMobil();
			mob.setExTipoMobil(dao().consultar(ExTipoMobil.TIPO_MOBIL_VOLUME, ExTipoMobil.class, false));
			mob.setNumSequencia((int) dao().obterProximoNumeroVolume(doc));
			mob.setExDocumento(doc);
			mob.setDnmSigla(mob.getSigla());
			if (doc.getMobilGeral().getDnmSigla() != doc.getSigla())
				doc.getMobilGeral().setDnmSigla(doc.getSigla());			
			doc.getExMobilSet().add(mob);
			mob = dao().gravar(mob);

			final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.CRIACAO, cadastrante,
					lotaCadastrante, mob, null, null, null, titular, lotaTitular, null);

			gravarMovimentacao(mov);
			concluirAlteracao(mov);

			if (mob.getNumSequencia() > 1) {
				ExMobil mobApenso = mob.doc().getVolume(mob.getNumSequencia() - 1);
				for (ExMobil apensoAoApenso : mobApenso.getApensosExcetoVolumeApensadoAoProximo()) {
					desapensarDocumento(cadastrante, lotaCadastrante, apensoAoApenso, null, null, null);
					apensarDocumento(cadastrante, cadastrante, lotaCadastrante, apensoAoApenso, mob, null, null, null);
				}
				if (mobApenso.isApensado()) {
					ExMobil outroMestreDoApenso = mobApenso.getMestre();
					desapensarDocumento(cadastrante, lotaCadastrante, mobApenso, null, null, null);
					apensarDocumento(cadastrante, cadastrante, lotaCadastrante, mob, outroMestreDoApenso, null, null,
							null);
				}
				apensarDocumento(cadastrante, cadastrante, lotaCadastrante, mobApenso, mob, null, null, null);
			}
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao criar novo volume.", e);
		}
	}

	/**
	 * @param cadastrante
	 * @param titular
	 * @param lotaTitular
	 * @param doc
	 * @param nomeProcesso
	 * @throws Exception
	 */
	public void criarWorkflow(final DpPessoa cadastrante, DpPessoa titular, final DpLotacao lotaTitular,
			ExDocumento doc, String nomeProcesso) throws Exception {
		{ // Utiliza o serviço WebService do SigaWf
			WfService client = Service.getWfService();
			ArrayList<String> keys = new ArrayList<String>();
			ArrayList<String> values = new ArrayList<String>();
			{ // Adiciona no contexto a via Geral
				keys.add("doc_document");
				if (doc.isExpediente())
					values.add(doc.getMobilDefaultParaReceberJuntada().getSigla());
				else
					values.add(doc.getCodigo());
			}
//			for (ExMobil mob : doc.getExMobilSet()) {
//				if (mob.isGeral())
//					continue;
//				if (mob.isVia() || mob.isVolume()) {
//					keys.add("doc_" + (char) ('a' + mob.getNumSequencia()));
//					values.add((String) mob.getSigla());
//				}
//			}

//			for (int n = 0; n < doc.getSetVias().size(); n++) { // Adiciona no
//				// contexto a
//				// via 'n'
//				keys.add("doc_" + (char) ('a' + n));
//				values.add(doc.getCodigo() + "-" + (char) ('A' + n));
//			}
			List<ExPapel> papeis = dao().listarExPapeis();
			for (ExPapel papel : papeis) {
				List<DpResponsavel> responsaveis = doc.getResponsaveisPorPapel(papel);
				if (responsaveis != null && responsaveis.size() > 0) {
					keys.add("_doc_perfil_" + papel.getComoNomeDeVariavel());
					values.add(responsaveis.get(0).getSiglaCompleta());
				}
			}
			{
				// Nato: Esse flush é necessário porque o workflow precisará dos dados atualizados do documento para prosseguir
				ContextoPersistencia.flushTransaction();
				client.criarInstanciaDeProcesso(nomeProcesso,
						SiglaParser.makeSigla(cadastrante, cadastrante.getLotacao()),
						SiglaParser.makeSigla(titular, lotaTitular), keys, values, "DOCUMENTO", 
						(doc.isExpediente() && doc.isFinalizado()) ? doc.getPrimeiraVia().getSigla() : doc.getCodigo());
			}
		}
		// atualizarWorkFlow(doc);
	}

	public static String descricaoSePuderAcessar(ExDocumento doc,
			DpPessoa titular, DpLotacao lotaTitular) {
	//	if (mostraDescricaoConfidencial(doc, titular, lotaTitular) || (getComp().ehPublicoExterno(titular) && !(getComp().podeAcessarPublicoExterno()))  
				if (mostraDescricaoConfidencial(doc, titular, lotaTitular))
				return "CONFIDENCIAL";
		else
			return doc.getDescrDocumento();
	}

	public static String descricaoConfidencial(ExDocumento doc, DpLotacao lotaTitular) {
		if (mostraDescricaoConfidencial(doc, lotaTitular))
			return "CONFIDENCIAL";
		else
			return doc.getDescrDocumento();
	}

	public String descricaoConfidencialDoDocumento(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {

		try {
			if (!getComp().pode(ExPodeAcessarDocumento.class, titular, lotaTitular, mob))
				return "CONFIDENCIAL";
			else
				return mob.getExDocumento().getDescrDocumento();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "ERRO!";
		}
	}

	public static String selDescricaoConfidencial(Selecionavel sel, DpLotacao lotaTitular, DpPessoa titular) {
		if (sel instanceof ExDocumento) {
			ExDocumento doc = (ExDocumento) sel;
			if (mostraDescricaoConfidencial(doc, titular, lotaTitular))
				return "CONFIDENCIAL";
			else
				return doc.getDescrDocumento();
		} else if (sel instanceof ExMobil) {
			ExDocumento doc = ((ExMobil) sel).getExDocumento();
			if (mostraDescricaoConfidencial(doc, titular, lotaTitular))
				return "CONFIDENCIAL";
			else
				return doc.getDescrDocumento();
		} else if (sel instanceof DpLotacao) {
			DpLotacao lot = (DpLotacao) sel;
			return lot.getDescricaoAmpliada();
		}
		return sel.getDescricao();
	}

	public static boolean mostraDescricaoConfidencial(ExDocumento doc, DpLotacao lotaTitular) {
		if (doc == null)
			return false;
		if (doc.getExNivelAcessoAtual() == null)
			return false;
		if (doc.getExNivelAcessoAtual().getGrauNivelAcesso() > ExNivelAcesso.NIVEL_ACESSO_ENTRE_ORGAOS)
			return true;
		if (doc.getOrgaoUsuario() == null)
			return false;
		if (lotaTitular == null)
			return false;
		if (lotaTitular.getOrgaoUsuario() == null)
			return false;
		if (doc.getExNivelAcessoAtual().getGrauNivelAcesso() == ExNivelAcesso.NIVEL_ACESSO_ENTRE_ORGAOS && doc
						.getOrgaoUsuario().getIdOrgaoUsu() != lotaTitular
						.getOrgaoUsuario().getIdOrgaoUsu())
			return true;
		return false;
	}

	public static boolean mostraDescricaoConfidencial(ExDocumento doc, DpPessoa titular, DpLotacao lotaTitular) {
	    if (doc.getExModelo().isDescricaoAutomatica())
	        return false;
		try {
			return !Ex.getInstance().getComp().pode(ExPodeAcessarDocumento.class, titular, lotaTitular, doc.getMobilGeral());
		} catch (Exception e) {
			return true;
		}
	}
	
	public static boolean exibirQuemTemAcessoDocumentosLimitados(ExDocumento doc, DpPessoa titular, DpLotacao lotaTitular) {
		try {
			if (Ex.getInstance().getComp().pode(ExPodeAcessarDocumento.class, titular, lotaTitular, doc.getMobilGeral())) { return true; }
			return Ex.getInstance().getComp().pode(ExPodeExibirQuemTemAcessoAoDocumento.class, titular, lotaTitular, doc.getExModelo());
		} catch (Exception e) {
			return true;
		}
	}

	public boolean mostraDescricaoConfidencialDoDocumento(ExDocumento doc, DpLotacao lotaTitular) {
		if (doc == null)
			return false;
		if (doc.getExNivelAcesso() == null)
			return false;
		if (doc.getExNivelAcesso().getGrauNivelAcesso() > ExNivelAcesso.NIVEL_ACESSO_ENTRE_ORGAOS
				|| (doc.getExNivelAcesso().getGrauNivelAcesso() == ExNivelAcesso.NIVEL_ACESSO_ENTRE_ORGAOS
						&& doc.getOrgaoUsuario().getIdOrgaoUsu() != lotaTitular.getOrgaoUsuario().getIdOrgaoUsu()))
			return true;
		return false;
	}

	public static String anotacaoConfidencial(ExMobil mob, DpPessoa titular, DpLotacao lotaTitular) {
		if (mob.isGeral())
			return "";
		
		if (mostraDescricaoConfidencial(mob.doc(), titular, lotaTitular))
			return "CONFIDENCIAL";
				
		String s = mob.getDnmUltimaAnotacao();
		if (s != null)
			return s;
		
		//Trata mobiles sem última anotação registrada. Passivo anterior a 24/07/2014
		if (Prop.getBool("atualiza.anotacao.pesquisa"))
			s = atualizarDnmAnotacao(mob);
		
		return s;
	}

	private static String atualizarDnmAnotacao(ExMobil mob) {
		String s;
		s = "";
		
		for (ExMovimentacao mov : mob.getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			if (mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.ANOTACAO)
				s = mov.getDescrMov();
		}
		// Nato: precisei gravar um espaco pois estava desconsiderando a string
		// vazia e gravando nulo.
		if (s == null || s.length() == 0)
			s = " ";
		mob.setDnmUltimaAnotacao(s);
		ExDao.getInstance().gravar(mob);
		return s;
	}
	
	public void enviarEmailParaUsuarioExternoAssinarDocumento(ExDocumento documento, DpPessoa pessoaSubscritorOuCossignatario) {
		if (usuarioExternoTemQueAssinar(documento, pessoaSubscritorOuCossignatario)) {
			
			String uri = obterURIDoDocumento(documento.getSigla());
			String conteudoHTML = Correio.obterHTMLEmailParaUsuarioExternoAssinarDocumento(uri, documento.getSigla(), pessoaSubscritorOuCossignatario.getSigla());			
			String destinatario[] = { pessoaSubscritorOuCossignatario.getEmailPessoaAtual() };						
			String assunto = "Acesso ao documento nº " + documento;
			
			try {
				Correio.enviar(null, destinatario, assunto, assunto, conteudoHTML);
			} catch (Exception e) {
				System.out.println("Problemas ao enviar e-mail para usuário externo assinar documento. Erro: " + e.getMessage());
			}
		}				
	}
	
	public boolean usuarioExternoTemQueAssinar(ExDocumento documento, DpPessoa pessoaSubscritorOuCossignatario) {
		return SigaMessages.isSigaSP() && 
				documento.isPendenteDeAssinatura() && 
				pessoaSubscritorOuCossignatario != null &&
				pessoaSubscritorOuCossignatario.isUsuarioExterno();
	}
	
	public String obterURIDoDocumento(String siglaDocumento) {
		HttpServletRequest request = CurrentRequest.get().getRequest();
		
		String uri = request.getRequestURL().toString().replace(request.getRequestURI(), "");
		uri +=  request.getContextPath() + "/app/expediente/doc/exibir?sigla=" + siglaDocumento;
		
		return uri;
	}
	
    public ExDocumento gravar(final DpPessoa cadastrante, final DpPessoa titular, final DpLotacao lotaTitular,
            ExDocumento doc) throws Exception {
        return gravar(cadastrante, titular, lotaTitular, doc, false);
    }
    
	public ExDocumento gravar(final DpPessoa cadastrante, final DpPessoa titular, final DpLotacao lotaTitular,
			ExDocumento doc, boolean fManterSolicitacaoDeAssinatura) throws Exception {
	    doc.atrasarAtualizacaoDoArquivo();
		// Verifica se o documento possui documento pai e se o usuário possui
		// permissões de criar documento filho
		/*
		 * if (doc.getExMobilPai() != null &&
		 * doc.getExMobilPai().getExDocumento().getIdDoc() != null) if
		 * (doc.getExMobilPai().getExDocumento().isProcesso() && doc.isProcesso()) { if
		 * (!ExCompetenciaBL.podeCriarSubprocesso(cadastrante, lotaCadastrante,
		 * doc.getExMobilPai())) throw new AplicacaoException(
		 * "não foi possível criar Subprocesso do documento selecionado."); } else { if
		 * (!ExCompetenciaBL.podeCriarDocFilho(cadastrante, lotaCadastrante,
		 * doc.getExMobilPai())) throw new AplicacaoException(
		 * "não é permitido criar documento filho do documento pai selecionado, pois este está inacessível ao usuário."
		 * ); }
		 */					

		if (!fManterSolicitacaoDeAssinatura && doc.isAssinaturaSolicitada()) {
			ExMovimentacao m = doc.getMovSolicitacaoDeAssinatura();
			cancelar(titular, lotaTitular, m.getExMobil(), m, null, null, null,
					"Edição após solicitação de assinatura");
		}

		try {
			//Nato: Importante restaurar o flush mode para evitar um erro de Can not set java.lang.Long field br.gov.jfrj.siga.cp.CpArquivoBlob.idArqBlob to org.hibernate.action.internal.DelayedPostInsertIdentifier
			// dao().em().setFlushMode(FlushModeType.AUTO);
			
			Date dt = dao().dt();

			// System.out.println(System.currentTimeMillis() + " - INI gravar");
			iniciarAlteracao();

			if (doc.getCadastrante() == null)
				doc.setCadastrante(cadastrante);
			if (doc.getLotaCadastrante() == null) {
				doc.setLotaCadastrante(lotaTitular);
				if (doc.getLotaCadastrante() == null && doc.getCadastrante() != null)
					doc.setLotaCadastrante(doc.getCadastrante().getLotacao());
			}
			if (doc.getDtRegDoc() == null) {
				doc.setDtRegDoc(dt);
			}

			// Nato: para obter o numero do TMP na primeira gravação
			boolean primeiraGravacao = false;
			if (doc.getIdDoc() == null) {
				doc = salvarDocSemSalvarArq(doc);
				primeiraGravacao = true;
			}

			// Verifica se a data está entre o ano 2000 e o ano 2100
			if (doc.getDtDoc() != null && !Data.dataDentroSeculo21(doc.getDtDoc())) {
				throw new AplicacaoException("Data inválida, deve estar entre o ano 2000 e ano 2100");
			}

			if (doc.getDtDocOriginal() != null && !Data.dataDentroSeculo21(doc.getDtDocOriginal())) {
				throw new AplicacaoException("Data original inválida, deve estar entre o ano 2000 e ano 2100");
			}
			gravaDescrDocumento(titular, lotaTitular, doc);

			if (doc.getSubscritor() == null && !doc.getCosignatarios().isEmpty())
				throw new AplicacaoException(
						"É necessário informar um subscritor, pois o documento possui cossignatários");

			long tempoIni = System.currentTimeMillis();

			// Remover eventuais pendencias de anexos que foram inseridar por
			// modelos anteriores e que não são necessárias no motelo atual.
			if (!doc.isFinalizado()) {
				while (true) {
					ExMovimentacao mov = doc.getMobilGeral().anexoPendente(null, false);
					if (mov == null)
						break;
					doc.getMobilGeral().getExMovimentacaoSet().remove(mov);
					dao().excluir(mov);
				}
				if (doc.getMobilGeral().getDnmSigla() == null)
					doc.getMobilGeral().setDnmSigla(doc.getSigla());				
			}

			// a estrutura try catch abaixo foi colocada de modo a impedir que
			// os erros na formatação impeçam a gravação do documento
			//try {
				processar(doc, false, false);
			//} catch (Throwable t) {
			//	System.out.println("gravação doc " + doc.getCodigo() + ", "
			//			+ new Date().toString() + " - erro na formatação - "
			//			+ t.getMessage());
			//	t.printStackTrace();
			//}

			// System.out.println("monitorando gravacao IDDoc " + doc.getIdDoc()
			// + ", PESSOA " + doc.getCadastrante().getIdPessoa()
			// + ". Terminou processar: "
			// + (System.currentTimeMillis() - tempoIni));
			tempoIni = System.currentTimeMillis();

			if (doc.getConteudoBlobDoc() != null)
				doc.setConteudoTpDoc(TipoConteudo.ZIP.getMimeType());

			processarResumo(doc);

			doc.setNumPaginas(doc.getContarNumeroDePaginas());
			
			doc = ExDao.getInstance().gravar(doc);
			for (ExMobil mob : doc.getExMobilSet()) {
				if (mob.getIdMobil() == null)
					mob = ExDao.getInstance().gravar(mob);
			}

			if (doc.getMobilGeral() == null) {
				throw new AplicacaoException("Documento precisa de mobil geral.");
			}

			String funcao = doc.getForm().get("acaoGravar");
			if (funcao != null) {
				obterMetodoPorString(funcao, doc);
			}

			atualizarMovimentacoesDePartes(doc, cadastrante, lotaTitular, dt);

			String s = processarComandosEmTag(doc, "gravacao");

			// Incluir movimentações de definição automática de perfil.
			if (!doc.isFinalizado())
				atualizarDefinicaoAutomaticaDePapel(cadastrante, lotaTitular, doc);
			
			doc.atualizarArquivo();
			
			concluirAlteracaoDocComRecalculoAcesso(doc);
			
			// Finaliza o documento automaticamente se ele for coloborativo
			if (!primeiraGravacao && doc.isColaborativo() && !doc.isFisico() && !doc.isFinalizado()) {
				finalizar(cadastrante, cadastrante.getLotacao(), titular, lotaTitular, doc);
			}
			
			// doc.armazenar();

			/*
			 * alteracao para adicionar a movimentacao de insercao de substituto
			 */
			/*
			if(exDocumentoDTO.isSubstituicao() && exDocumentoDTO.getDoc().getTitular() != exDocumentoDTO.getDoc().getSubscritor()) {
				final ExMovimentacao mov_substituto = criarNovaMovimentacao(
						ExTipoDeMovimentacao.SUBSTITUICAO_RESPONSAVEL, cadastrante,
						cadastrante.getLotacao(), doc.getMobilGeral(), null, cadastrante, null, null, null, null);
				mov_substituto.setDescrMov("Responsável pela assinatura: " + doc.getSubscritor().getNomePessoa() + " - "
						+ doc.getSubscritor().getMatricula() + " em substituição de " + cadastrante.getNomePessoa()
						+ " - " + cadastrante.getMatricula());
				gravarMovimentacao(mov_substituto);
			}
			*/
			/*
			 * fim da alteracao
			 */

			// System.out.println("monitorando gravacao IDDoc " + doc.getIdDoc()
			// + ", PESSOA " + doc.getCadastrante().getIdPessoa()
			// + ". Terminou commit gravacao: "
			// + (System.currentTimeMillis() - tempoIni));
			tempoIni = System.currentTimeMillis();
		} catch (final Exception e) {
			cancelarAlteracao();
			Throwable t = e.getCause();
			if (t != null && t instanceof InvocationTargetException)
				t = t.getCause();
			if (t != null && t instanceof AplicacaoException)
				throw (AplicacaoException) t;
			else
				throw new RuntimeException("Erro na gravação", e);
		}
		// System.out.println(System.currentTimeMillis() + " - FIM gravar");
		return doc;
	}

	private ExDocumento salvarDocSemSalvarArq(ExDocumento doc) {
		CpArquivo arqTemp = null;
		// Nato: remover o cpArquivo para que ele não seja salvo automaticamente pelo
		// JPA, pois isso acarreta em gravação desnecessária na tabela CpArquivo.
		if (doc.getCpArquivo() != null && doc.getCpArquivo().getIdArq() == null) {
			arqTemp = doc.getCpArquivo();
			doc.setCpArquivo(null);
		}
		doc = ExDao.getInstance().gravar(doc);
		if (arqTemp != null) 
			doc.setCpArquivo(arqTemp);
		return doc;
	}
	
	public void geraMovimentacaoSubstituicao(ExDocumento doc, DpPessoa cadastrante) throws AplicacaoException, SQLException {
		final ExMovimentacao mov_substituto = criarNovaMovimentacao(
				ExTipoDeMovimentacao.SUBSTITUICAO_RESPONSAVEL, cadastrante,
				cadastrante.getLotacao(), doc.getMobilGeral(), null, cadastrante, null, null, null, null);
		mov_substituto.setDescrMov("Responsável pela assinatura: " + doc.getSubscritor().getNomePessoa() + " - "
				+ doc.getSubscritor().getSiglaCompleta() + " em substituição de " + doc.getTitular().getNomePessoa()
				+ " - " + doc.getTitular().getSiglaCompleta());
		gravarMovimentacao(mov_substituto);
	}

	private class MovimentacaoSincronizavel extends SincronizavelSuporte
			implements Sincronizavel, Comparable<MovimentacaoSincronizavel> {
		@Desconsiderar
		ExPapel papel;
		@Desconsiderar
		DpPessoa pessoa;
		@Desconsiderar
		DpLotacao lotacao;
		@Desconsiderar
		ExMovimentacao mov;

		MovimentacaoSincronizavel(ExPapel papel, DpPessoa pessoaIni, DpLotacao lotacaoIni, ExMovimentacao mov) {
			this.papel = papel;
			this.pessoa = pessoaIni;
			this.lotacao = lotacaoIni;
			this.mov = mov;
			this.setIdExterna(papel + "|" + pessoaIni + "|" + lotacaoIni);
		}

		@Override
		public int compareTo(MovimentacaoSincronizavel o) {
			return getIdExterna().compareTo(o.getIdExterna());
		}

	}

	private void atualizarDefinicaoAutomaticaDePapel(DpPessoa cadastrante, DpLotacao lotaCadastrante, ExDocumento doc)
			throws AplicacaoException, SQLException {
		if (doc == null || doc.getTitular() == null || doc.getMobilGeral() == null)
			return;

		SortedSet<MovimentacaoSincronizavel> setAntes = new TreeSet<>();
		SortedSet<MovimentacaoSincronizavel> setDepois = new TreeSet<>();

		// Inclui em setAntes os papeis que já estão atribuídos de acordo com as
		// movimentações de vínculo de papel
		List<ExMovimentacao> movs = doc.getMobilGeral()
				.getMovimentacoesPorTipo(ExTipoDeMovimentacao.VINCULACAO_PAPEL, false);
		for (ExMovimentacao mov : movs) {
			if (mov.isCancelada() || mov.getCadastrante() != null)
				continue;
			setAntes.add(new MovimentacaoSincronizavel(mov.getExPapel(),
					mov.getSubscritor() != null ? mov.getSubscritor().getPessoaAtual() : null,
					(mov.getSubscritor() == null && mov.getLotaSubscritor() != null)
							? mov.getLotaSubscritor().getLotacaoAtual()
							: null,
					mov));
		}

		// Inclui em setDepois os papeis que devem estar atribuídos ao documento
		//
		Date dt = dao().consultarDataEHoraDoServidor();
		TreeSet<ExConfiguracaoCache> lista = null;
		ExConfiguracaoBL confBL = Ex.getInstance().getConf();
		lista = (TreeSet) confBL.getListaPorTipo(ExTipoDeConfiguracao.DEFINICAO_AUTOMATICA_DE_PAPEL);
		if (lista != null) {
			ExConfiguracao confFiltro = new ExConfiguracao();
			confFiltro.setDpPessoa(doc.getTitular());
			confFiltro.setLotacao(doc.getLotaTitular());
			confFiltro.setExFormaDocumento(doc.getExFormaDocumento());
			confFiltro.setExModelo(doc.getExModelo());
			confBL.deduzFiltro(confFiltro);
			Set<Integer> atributosDesconsiderados = new HashSet<>();
			atributosDesconsiderados.add(CpConfiguracaoBL.PESSOA_OBJETO);
			atributosDesconsiderados.add(CpConfiguracaoBL.LOTACAO_OBJETO);
			atributosDesconsiderados.add(ExConfiguracaoBL.PAPEL);
			
			CpConfiguracaoCache filtroConfiguracaoCache = confFiltro.converterParaCache();
			for (ExConfiguracaoCache conf : lista) {
				if (// (!conf.ativaNaData(dt)) ||
				conf.exPapel == 0 || (conf.pessoaObjeto == 0 && conf.lotacaoObjeto == 0)
						|| !confBL.atendeExigencias(filtroConfiguracaoCache, atributosDesconsiderados, conf, null))
					continue;
				DpPessoa po = null;
				DpLotacao lo = null;
				if (conf.pessoaObjeto != 0)
					po = dao().consultar(conf.pessoaObjeto, DpPessoa.class, false);
				if (conf.lotacaoObjeto != 0)
					lo = dao().consultar(conf.lotacaoObjeto, DpLotacao.class, false);
				ExPapel p = dao().consultar(conf.exPapel, ExPapel.class, false);
				setDepois.add(new MovimentacaoSincronizavel(p, po, lo, null));
			}
		}

		// O uso da classe Sincronizador nessa rotina acabou se tornando desnecessário
		// pois não estamos tratando
		// a exclusão nem a alteração. Convém simplificar isso depois.
		Sincronizador sinc = new Sincronizador();
		sinc.setSetNovo((SortedSet<Sincronizavel>) (SortedSet) setDepois);
		sinc.setSetAntigo((SortedSet<Sincronizavel>) (SortedSet) setAntes);
		List<Item> list = sinc.getEncaixe();

		for (Item i : list) {
			switch (i.getOperacao()) {
			case alterar:
				throw new RuntimeException("Não deveria haver uma operação de alteração na lista.");
			case incluir:
				MovimentacaoSincronizavel novo = (MovimentacaoSincronizavel) i.getNovo();
				final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.VINCULACAO_PAPEL,
						null, null, doc.getMobilGeral(), dt, novo.pessoa != null ? novo.pessoa.getPessoaAtual() : null,
						novo.lotacao != null ? novo.lotacao.getLotacaoAtual() : null, null, null, dt);
				mov.setExPapel(novo.papel);
				gravarMovimentacao(mov);
				break;
			case excluir:
				final ExMovimentacao movCancelamento = criarNovaMovimentacao(
						ExTipoDeMovimentacao.CANCELAMENTO_DE_MOVIMENTACAO, null, null,
						doc.getMobilGeral(), dt, null, null, null, null, null);
				movCancelamento.setExMovimentacaoRef(((MovimentacaoSincronizavel) i.getAntigo()).mov);
				gravarMovimentacaoCancelamento(movCancelamento, ((MovimentacaoSincronizavel) i.getAntigo()).mov);
				break;
			}
		}
	}
	
	private void removerPapel(ExDocumento doc, long idPapel) throws Exception {
		List<ExMovimentacao> movs = doc.getMobilGeral()
				.getMovimentacoesPorTipo(ExTipoDeMovimentacao.VINCULACAO_PAPEL, false);
		removerPapel(doc, movs, idPapel, null, null);
	}
	
	public void removerPapel(ExDocumento doc, List<ExMovimentacao> movs, long idPapel, DpPessoa cadastrante, String descrMov) throws Exception {
		ExMovimentacao movCancelamento = null;
		boolean removido = false;
		for (ExMovimentacao mov : movs) {
			if (mov.isCancelada() || !mov.getExPapel().getIdPapel().equals(idPapel))
				continue;
			if (movCancelamento == null) {
				Date dt = dao().consultarDataEHoraDoServidor();
				movCancelamento = criarNovaMovimentacao(
						ExTipoDeMovimentacao.CANCELAMENTO_DE_MOVIMENTACAO, cadastrante, null,
						doc.getMobilGeral(), dt, null, null, null, null, null);
				movCancelamento.setDescrMov(descrMov);
				movCancelamento.setExMovimentacaoRef(mov);
			}
			gravarMovimentacaoCancelamento(movCancelamento, mov);
			removido = true;
		}
		if (removido)
			concluirAlteracaoDocComRecalculoAcesso(doc);
	}
	
	private void processarResumo(ExDocumento doc) throws Exception, UnsupportedEncodingException {
		String r = processarModelo(doc, null, "resumo", null, null);
		String resumo = null;
		if (r.contains("<!-- resumo -->") && r.contains("<!-- /resumo -->")) {
			if (r.contains("<!-- topico -->") && r.contains("<!-- /topico -->")) {
				r = r.substring(r.lastIndexOf("<!-- resumo -->") + 15, r.indexOf("<!-- /resumo -->")).trim();
				// r = r.replaceAll("\r", "");
				r = r.replaceAll("\n", "");
				r = r.replaceAll("\t", "");
				r = r.replaceAll("<!-- /topico -->", "");
				String topicos[] = r.split("<!-- topico -->");
				String topico = null;
				for (int i = 0; i < topicos.length; i++) {
					if (!topicos[i].equals("")) {
						String descr = topicos[i].substring(topicos[i].indexOf("name=\"") + "name=\"".length(),
								topicos[i].indexOf("\" value="));
						String valor = topicos[i].substring(topicos[i].indexOf(" value=\"") + " value=\"".length(),
								topicos[i].indexOf("\"/>"));
						topico = URLEncoder.encode(descr, "iso-8859-1") + "=" + URLEncoder.encode(valor, "iso-8859-1")
								+ "&";
						if (resumo == null) {
							resumo = topico;
						} else {
							resumo = resumo + topico;
						}
					}
				}

				resumo = resumo.substring(0, resumo.length() - 1);

			}
		} else {
			resumo = null;
		}

		if (resumo != null) {
			doc.setConteudoBlobResumo(resumo.getBytes());
		}
	}

	public ExNivelAcesso atualizarDnmNivelAcesso(ExDocumento doc) {
		log.debug("[getExNivelAcesso] - Obtendo nível de acesso atual do documento...");
		ExNivelAcesso nivel = null;
		if (doc.getMobilGeral() != null && doc.getMobilGeral().getUltimaMovimentacaoAlteracaoNivelAcessoNaoCancelada() != null)
			nivel = doc.getMobilGeral().getUltimaMovimentacaoAlteracaoNivelAcessoNaoCancelada().getExNivelAcesso();
		if (nivel == null)
			nivel = doc.getExNivelAcesso();
		doc.setDnmExNivelAcesso(nivel);
		doc = salvarDocSemSalvarArq(doc);
		return nivel;
	}

	public void atualizarDnmAcesso(ExDocumento doc) {
		atualizarDnmAcesso(doc, null, null);
	}
	public void atualizarDnmAcesso(ExDocumento doc, Object incluirAcesso, Object excluirAcesso) {
		Date dt = ExDao.getInstance().dt();
		String acessoRecalculado = new ExAcesso().getAcessosString(doc, dt, incluirAcesso, excluirAcesso);

		if (doc.getDnmAcesso() == null || !doc.getDnmAcesso().equals(acessoRecalculado)) {
			doc.setDnmAcesso(acessoRecalculado);
			doc.setDnmDtAcesso(dt);
			ExDao.getInstance().gravar(doc);
		}
	}
	
	public void bCorrigirDataFimMov(final ExMovimentacao mov) throws Exception {
		try {
			iniciarAlteracao();

			dao().gravar(mov);

			mov.getExMobil().getExMovimentacaoSet().add(mov);

			concluirAlteracao(mov);

		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro na gravação", e);

		}

	}

	public void bCorrigirCriacaoDupla(final ExMovimentacao mov) throws Exception {
		try {
			iniciarAlteracao();

			dao().excluir(mov);

			concluirAlteracao(mov);

		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro na gravação", e);

		}

	}

	private void gravarMovimentacao(final ExMovimentacao mov) throws AplicacaoException, SQLException {
		// if (mov.getNumVia() != null && mov.getNumVia() == 0)
		// mov.setNumVia(null);
		//
		// if (ultMov == null)
		// ultMov = mov.getExDocumento()
		// .getUltimaMovimentacao(mov.getNumVia());
		//
		// if (ultMov != null) {
		// ultMov.setDtFimMov(new Date());
		// ExDao.getInstance().gravar(ultMov);
		// }
		
		if (SigaMessages.isSigaSP()) {
			mov.setNumPaginas(mov.getContarNumeroDePaginas()); //Sempre conta a página para SP
		} else if (mov.getExTipoMovimentacao() != ExTipoDeMovimentacao.ANEXACAO_DE_ARQUIVO_AUXILIAR) {
			mov.setNumPaginas(mov.getContarNumeroDePaginas()); 
		}
		
		dao().gravar(mov);

		/*
		 * if (mov.getConteudoBlobMov() != null) movDao.gravarConteudoBlob(mov);
		 */

		if (mov.getExMobil().getExMovimentacaoSet() == null)
			mov.getExMobil().setExMovimentacaoSet(new TreeSet<ExMovimentacao>());

		mov.getExMobil().getExMovimentacaoSet().add(mov);
		
		if (mov.getExTipoMovimentacao() != ExTipoDeMovimentacao.CANCELAMENTO_DE_MOVIMENTACAO) {
			mov.getExMobil().setUltimaMovimentacaoNaoCancelada(mov);
			mov.getExMobil().setDnmDataUltimaMovimentacaoNaoCancelada(mov.getDtIniMov());
			
			dao().gravar(mov.getExMobil());
		} else {
			ExMovimentacao movUlt = mov.getExMobil()
					.getUltimaMovimentacao(new ITipoDeMovimentacao[] {}, 
							new ITipoDeMovimentacao[] {}, mov.getExMobil(), false, null, false);
			mov.getExMobil().setUltimaMovimentacaoNaoCancelada(movUlt);
			mov.getExMobil().setDnmDataUltimaMovimentacaoNaoCancelada(movUlt != null ? movUlt.getDtIniMov() : null);
			dao().gravar(mov.getExMobil());
		}
		
		if (mov.getExTipoMovimentacao() != ExTipoDeMovimentacao.CANCELAMENTO_DE_MOVIMENTACAO) { 
			Notificador.notificarDestinariosEmail(mov, Notificador.TIPO_NOTIFICACAO_GRAVACAO);
		} 
		
		if (SigaMessages.isSigaSP()) {
			if (mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.SOLICITACAO_DE_ASSINATURA &&
					usuarioExternoTemQueAssinar(mov.getExDocumento(), mov.getSubscritor())) {
				enviarEmailParaUsuarioExternoAssinarDocumento(mov.getExDocumento(), mov.getSubscritor());
				
			} else if (mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.ASSINATURA_COM_SENHA ||
					mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.ASSINATURA_DIGITAL_DOCUMENTO ||
					mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.CONFERENCIA_COPIA_COM_SENHA ||
					mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.CONFERENCIA_COPIA_DOCUMENTO) {	
				
				if (!mov.getExDocumento().getCosignatarios().isEmpty() && 
						!mov.getExDocumento().isCossignatario(mov.getSubscritor())) {
					for (DpPessoa cossignatario : mov.getExDocumento().getCosignatarios()) {
						if (usuarioExternoTemQueAssinar(mov.getExDocumento(), cossignatario)) {
							enviarEmailParaUsuarioExternoAssinarDocumento(mov.getExDocumento(), cossignatario);
						}
					}
				}
				
			}
		}		
	}
	
	public void getExTipoMovSubstituicaoAssinante(final ExMovimentacao mov) {
			final ExMovimentacao mov_substituto = criarNovaMovimentacao(
					ExTipoDeMovimentacao.SUBSTITUICAO_RESPONSAVEL,
					mov.getExMobil().getDoc().getCadastrante(), mov.getExMobil().getDoc().getLotacao(),
					mov.getExMobil().getDoc().getMobilGeral(), null, mov.getExMobil().getDoc().getCadastrante(), null,
					null, null, null);
			mov_substituto.setDescrMov(
					"Responsável pela assinatura: " + mov.getExMobil().getDoc().getSubscritor().getNomePessoa() + " - "
							+ mov.getExMobil().getDoc().getSubscritor().getOrgaoUsuario().getSiglaOrgaoUsu()
							+ mov.getExMobil().getDoc().getSubscritor().getMatricula() + " em substituição de "
							+ mov.getExMobil().getDoc().getCadastrante().getNomePessoa() + " - "
							+ mov.getExMobil().getDoc().getCadastrante().getOrgaoUsuario().getSiglaOrgaoUsu()
						+ mov.getExMobil().getDoc().getCadastrante().getMatricula());
		dao().gravar(mov_substituto);
	}

	private void gravarMovimentacaoCancelamento(final ExMovimentacao mov, ExMovimentacao movCancelada)
			throws AplicacaoException, SQLException {
		// if (ultMov == null)
		// ultMov = mov.getExMobil().getUltimaMovimentacao();

		gravarMovimentacao(mov);

		if (movCancelada == null)
			movCancelada = mov.getExMobil().getUltimaMovimentacaoNaoCancelada();

		if (movCancelada != null) {
			movCancelada.setExMovimentacaoCanceladora(mov);
			dao().gravar(movCancelada);
		}

		Notificador.notificarDestinariosEmail(mov, mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.TRANSFERENCIA ? Notificador.TIPO_NOTIFICACAO_GRAVACAO : Notificador.TIPO_NOTIFICACAO_CANCELAMENTO);
	}

	public void excluirDocumentoAutomatico(final ExDocumento doc, DpPessoa titular, DpLotacao lotaTitular)
			throws Exception {
		excluirDocumento(doc, titular, lotaTitular, true);
	}

	public void excluirDocumento(final ExDocumento doc, DpPessoa titular, DpLotacao lotaTitular,
			boolean automatico) /*
								 * somente documentos temporários
								 */
			throws Exception {
		try {
			ExDao.iniciarTransacao();

			try {
				final Date d = doc.getDtRegDoc();
			} catch (final ObjectNotFoundException e) {
				throw new AplicacaoException("Documento já foi excluído anteriormente", 1, e);
			}
			
			if (getExConsTempDocCompleto().podeVisualizarTempDocComplCossigsSubscritor(titular, lotaTitular)) {
				getExConsTempDocCompleto().removerCossigsSubscritorVisTempDocsComplFluxosRefazerCancelarExcluirDoc(titular, lotaTitular, doc);
			}

			if (doc.isFinalizado())
				throw new AplicacaoException("Documento já foi finalizado e não pode ser excluído", 2);
			for (ExMobil m : doc.getExMobilSet()) {
				Set set = m.getExMovimentacaoSet();

				if (!automatico && !Ex.getInstance().getComp().pode(ExPodeExcluir.class, titular, lotaTitular, m))
					throw new AplicacaoException("não é possível excluir");

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
						if (!movimentacao.isCancelada())
							Ex.getInstance().getBL().excluirMovimentacao(titular, lotaTitular, movimentacao.getExMobil(),
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
			throw new RuntimeException("Ocorreu um Erro durante a Operação", e);
		}

	}

	public void excluirMovimentacao(final ExMovimentacao mov) throws AplicacaoException, SQLException {

		// Date dtFim = mov.getDtFimMov();
		// Date dtIni = mov.getDtIniMov();
		//
		// mov.getExMobil().getExMovimentacaoSet().remove(mov);
		// for (ExMovimentacao m : mov.getExMobil().getExMovimentacaoSet()) {
		// if (m.getDtFimMov() != null && m.getDtFimMov().equals(dtIni)) {
		// ExMovimentacao ultMov = m;
		// ultMov.setDtFimMov(dtFim);
		// dao().gravar(ultMov);
		// break;
		// }
		// }
		dao().excluir(mov);
		mov.getExMobil().getExMovimentacaoSet().remove(mov);
		for (ExMovimentacao m : mov.getExMobil().getExMovimentacaoSet()) {
			if (mov.equals(m.getExMovimentacaoCanceladora())) {
				m.setExMovimentacaoCanceladora(null);
			}
		}

		if (mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.ANOTACAO) {
			atualizarDnmAnotacao(mov.getExMobil());
		}

		Notificador.notificarDestinariosEmail(mov, Notificador.TIPO_NOTIFICACAO_EXCLUSAO);
	}

	public void excluirAnexosNaoAssinados(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, ExMobil mob) {

		for (ExMovimentacao movNaoAss : mob.getAnexosNaoAssinados()) {
			if (movNaoAss != null) {
				Ex.getInstance().getBL().excluirMovimentacao(cadastrante, lotaCadastrante, mob, movNaoAss.getIdMov());
			}
		}
		for (ExMovimentacao movNaoAss : mob.doc().getMobilGeral().getAnexosNaoAssinados()) {
			if (movNaoAss != null) {
				Ex.getInstance().getBL().excluirMovimentacao(cadastrante, lotaCadastrante, mob, movNaoAss.getIdMov());
			}
		}

	}
	
	public void incluirCosignatario(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExDocumento doc,
			final Date dtMov, final DpPessoa subscritor, final String funcaoCosignatario) throws AplicacaoException {
		incluirCosignatario(cadastrante, lotaCadastrante, doc, dtMov, subscritor, funcaoCosignatario, Boolean.FALSE);
	}

	public void incluirCosignatario(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExDocumento doc,
			final Date dtMov, final DpPessoa subscritor, final String funcaoCosignatario, final boolean podeIncluirCossigArvoreDocs) throws AplicacaoException {
			
		if (subscritor == null) {
			throw new RegraNegocioException("Cossignatário não foi informado");
		}
		
		if(Ex.getInstance().getComp().exp(ExECossignatario.class, subscritor, doc).eval()) {
			throw new RegraNegocioException("Não é possivel incluir o cossignatário, pois ele já foi incluído!");
		}
		
		if (!new ExPodeRestringirCossignatarioSubscritor(cadastrante, lotaCadastrante, subscritor, subscritor.getLotacao(),
				subscritor.getCargo(),
				subscritor.getFuncaoConfianca(),
				subscritor.getOrgaoUsuario()).eval()) {
			throw new RegraNegocioException("Esse usuário não está disponível para inclusão de Cossignatário / " + SigaMessages.getMessage("documento.subscritor"));
		}

		try {
			
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO, cadastrante, lotaCadastrante,
					doc.getMobilGeral(), dtMov, subscritor, null, null, null, null);

			mov.setDescrMov(funcaoCosignatario);
			mov.setNmFuncaoSubscritor(funcaoCosignatario);

			gravarMovimentacao(mov);
			processar(doc, true, false);
			// doc.armazenar();
			concluirAlteracaoDocComRecalculoAcesso(mov);
			if (podeIncluirCossigArvoreDocs)
				getExConsTempDocCompleto().incluirCossigsVisTempDocsCompl(cadastrante, lotaCadastrante, doc, podeIncluirCossigArvoreDocs, Boolean.TRUE);
			
			ExMovimentacao movOrdemAss = doc.getMobilGeral().getUltimaMovimentacaoNaoCancelada(ExTipoDeMovimentacao.ORDEM_ASSINATURA);
			if(movOrdemAss != null) {
				reordenarAss(doc.getMobilGeral(), cadastrante, lotaCadastrante, movOrdemAss.getDescrMov() + ";" + subscritor.getSigla());
			}
		} catch (RegraNegocioException e) {
			throw e;
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao incluir Cossignatário.", e);
		}
	}
	
	public ExMovimentacao reordenarAss(ExMobil mob, DpPessoa cadastrante, DpLotacao lotacao, String descricao) {
		ExMovimentacao mov = gravarNovaMovimentacao(ExTipoDeMovimentacao.ORDEM_ASSINATURA, cadastrante, lotacao, mob, null, cadastrante, lotacao, cadastrante, lotacao, null, descricao);
		processar(mob.doc(), true, false);
		return mov;
	}

	public void excluirRegistroDaOrdenacaoAssinatura(ExMobil mob, ExMovimentacao mov, DpPessoa cadastrante, DpLotacao lotacao) {
		ExMovimentacao movOrdem = mob.getDoc().getMobilGeral().getUltimaMovimentacaoNaoCancelada(ExTipoDeMovimentacao.ORDEM_ASSINATURA);
		if(movOrdem != null) {
			List<String> listaMatricula = new ArrayList<String>();
			listaMatricula.addAll(Arrays.asList(movOrdem.getDescrMov().split(";")));
			listaMatricula.remove(mov.getSubscritor().getSigla());
			String desc = "";
			for (String string : listaMatricula) {
				desc += string + ";";
			}
			desc = desc.substring(0, desc.length()-1);
			reordenarAss(mob, cadastrante, lotacao, desc);
		}
	}
	

	
	/**
	 *Se Documento com Acesso Restrito, verifica se há restrição para cossignatário na exclusão de cossignatário e remove restrição
	 */
	public void excluirRestricaoAcessoPessoa(ExMobil mob, DpPessoa pessoa, DpPessoa cadastrante, DpLotacao lotaCadastrante) {
		if (mob.isAcessoRestrito()) {
			mob.getMovimentacoesPorTipo(ExTipoDeMovimentacao.RESTRINGIR_ACESSO, true)
				.stream()
				.filter(movRestricao -> pessoa.equivale(movRestricao.getSubscritor()))
				.forEach(movRestricaoAcessoParaPessoa-> {
					try {
						cancelar(cadastrante, lotaCadastrante, mob, movRestricaoAcessoParaPessoa, null, null, null, "Restrição: " + movRestricaoAcessoParaPessoa.getDescrMov());
					} catch (Exception e) {
						throw new RuntimeException(String.format("Erro ao excluir a Restrição de Acesso para %s. ", pessoa.getSigla() + "-" + pessoa.getNomePessoa()) , e);
					}
				});
		}
	}

	public void juntarDocumento(final DpPessoa cadastrante, final DpPessoa docTitular, final DpLotacao lotaCadastrante,
			final String idDocExterno, final ExMobil mob, ExMobil mobPai, final Date dtMov, final DpPessoa subscritor,
			final DpPessoa titular, final String idDocEscolha) {

		if (idDocEscolha.equals("1")) {

			if (mobPai == null)
				throw new RegraNegocioException("Não foi selecionado um documento para a juntada");

			if (mob.getExDocumento().getIdDoc().equals(mobPai.getExDocumento().getIdDoc())
					&& mob.getNumSequencia().equals(mobPai.getNumSequencia()))
				throw new RegraNegocioException("Não é possível juntar um documento a ele mesmo");					
			
			if (!mobPai.getExDocumento().isFinalizado())
				throw new RegraNegocioException("Não é possível juntar a um documento não finalizado");

			if (mobPai.isGeral())
				mobPai = mobPai.doc().getMobilDefaultParaReceberJuntada();

			if (mobPai.isGeral()) {
				throw new RegraNegocioException("É necessário informar a via é qual será feita a juntada");
			}

			if (mob.doc().isEletronico()) {
				if (mob.temAnexosNaoAssinados() || mob.temDespachosNaoAssinados())
					throw new RegraNegocioException(
							"Não é possível juntar documento com anexo/despacho pendente de assinatura ou conferência");
			}

			if (!mob.getDoc().isEletronico() && mobPai.getDoc().isEletronico())
				throw new RegraNegocioException("Não é possível juntar um documento físico a um documento eletrônico.");

			if (mobPai.isSobrestado())
				throw new RegraNegocioException("Não é possível juntar um documento a um volume sobrestado.");

			// Verifica se o documeto pai já estáapensado a este documento
			for (ExMobil apenso : mob.getApensos()) {
				if (apenso.getId() == mobPai.getId())
					throw new RegraNegocioException(
							"Não é possível juntar um documento a um documento que está apensado a ele.");
			}

			if (mobPai.isSobrestado())
				throw new RegraNegocioException("Não é possível juntar um documento a um volume sobrestado.");

			if (mobPai.isCancelada())
				throw new RegraNegocioException("A via não pode ser juntada ao documento porque ele está cancelado.");

			if (mobPai.isVolumeEncerrado())
				throw new RegraNegocioException("A via não pode ser juntada ao documento porque o volume está encerrado.");

//			if (mobPai.doc().isPendenteDeAssinatura())
//				throw new RegraNegocioException(
//						"A via não pode ser juntada ao documento porque ele está pendente de assinatura.");

			if (mobPai.isJuntado())
				throw new RegraNegocioException("A via não pode ser juntada ao documento porque ele está juntado.");

			if (mobPai.isEmTransito(cadastrante, lotaCadastrante))
				throw new RegraNegocioException("A via não pode ser juntada ao documento porque ele está em trânsito.");

			if (mobPai.isArquivado())
				throw new RegraNegocioException("A via não pode ser juntada ao documento porque ele está arquivado");

			if (!getComp().pode(ExPodeSerJuntado.class, docTitular, lotaCadastrante, mob.doc(), mobPai) 
					&& !getComp().pode(ExPodeMovimentar.class, docTitular, lotaCadastrante, mobPai))			
				throw new RegraNegocioException("A via não pode ser juntada ao documento porque ele não pode ser movimentado.");			
			
			if(mob.getDoc().isComposto() && !mobPai.getDoc().isComposto())
				throw new RegraNegocioException("Não é permitido realizar a juntada de documento composto em documento avulso.");
			
		}

		final ExMovimentacao mov;
		
		try {
			iniciarAlteracao();

			ITipoDeMovimentacao idTpMov;
			if (idDocEscolha.equals("1")) {
				idTpMov = ExTipoDeMovimentacao.JUNTADA;
			} else if (idDocEscolha.equals("2")) {
				idTpMov = ExTipoDeMovimentacao.JUNTADA_EXTERNO;
			} else
				throw new AplicacaoException("Opção inválida.");

			mov = criarNovaMovimentacao(idTpMov, cadastrante, lotaCadastrante, mob, dtMov, subscritor, null, titular,
					null, null);

			mov.setExMobilRef(mobPai);


			if (idDocEscolha.equals("1")) {
				mov.setDescrMov("Juntado ao documento " + mov.getExMobilRef().getCodigo().toString());
			} else if (idDocEscolha.equals("2")) {
				mov.setDescrMov(idDocExterno);
			} else
				throw new AplicacaoException("Opção inválida.");

			if (idDocEscolha.equals("1") || idDocEscolha.equals("2")) {
			} else
				throw new AplicacaoException("Opção inválida.");

			gravarMovimentacao(mov);
			
			if (getExConsTempDocCompleto().podeVisualizarTempDocComplCossigsSubscritor(cadastrante, lotaCadastrante)) {
				getExConsTempDocCompleto().tratarFluxoJuntarVisTempDocsCompl(mob, cadastrante, lotaCadastrante);
			}

			atualizarMarcas(false, mob);

			if (idDocEscolha.equals("1")) {
				encerrarVolumeAutomatico(cadastrante, lotaCadastrante, mov.getExMobilRef(), dtMov);
			}

			Set<ExMovimentacao> movs = mob.getTransferenciasPendentesDeDevolucao(mob);
			if (!movs.isEmpty())
				removerPendenciaDeDevolucao(movs, mob);
			
			if (idTpMov == ExTipoDeMovimentacao.JUNTADA) {			
				if (mobPai.isAcessoRestrito() && Ex.getInstance().getComp().pode(ExPodeRestringirAcesso.class, cadastrante, lotaCadastrante, mobPai.getDoc().getMobilGeral())) {
					herdaRestricaoAcessoDocumentoPai(mob.getDoc().getMobilGeral(), mobPai.getDoc().getMobilGeral(), cadastrante, titular, dtMov);
				}
			}
			
			concluirAlteracaoComRecalculoAcesso(mov);

		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao juntar documento.", e);
		}

	}

	public void alterarPrincipal(final DpPessoa cadastrante, final DpPessoa docTitular, final DpLotacao lotaCadastrante,
			final String idDocExterno, final ExMobil mob, ExMobil mobPai, final Date dtMov, final DpPessoa subscritor,
			final DpPessoa titular, final String idDocEscolha) {

		if (idDocEscolha.equals("1")) {
			if (mobPai == null)
				throw new RegraNegocioException("Não foi selecionado um documento");
		}
	}
	
	public void herdaRestricaoAcessoDocumentoPai(ExMobil mobFilho, ExMobil mobPai, DpPessoa cadastrante, DpPessoa titular, Date dtMov)
			throws AplicacaoException, SQLException {

		if (!mobPai.getDoc().getMobilGeral().isAcessoRestrito()) {
			if (mobFilho.getDoc().getMobilGeral().isAcessoRestrito()) {
				//Se Filho tem acesso restrito e Pai não tem, cancela movimentações de restrição e herda nível de acesso
				desfazerRestringirAcesso(cadastrante, cadastrante.getLotacao(), mobFilho.getDoc(), null);
			}
		} else {
			//Se tem Restrição de Acesso no Pai, herda movimentações e nível de acesso do Pai
			restringirAcesso(cadastrante, titular.getLotacao(), mobFilho.getDoc(), null, null, null, 
					mobPai.getSubscritoresMovimentacoesPorTipo(ExTipoDeMovimentacao.RESTRINGIR_ACESSO, false), 
					titular, null, mobPai.getDoc().getExNivelAcessoAtual());

		}

	}

	public ExDocumento refazer(DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, ExDocumento doc) {

		// As alterações devem ser feitas em cancelardocumento.
		try {
			iniciarAlteracao();
			
			if (getExConsTempDocCompleto().podeVisualizarTempDocComplCossigsSubscritor(cadastrante, lotaCadastrante)) {
				getExConsTempDocCompleto().removerCossigsSubscritorVisTempDocsComplFluxosRefazerCancelarExcluirDoc(cadastrante, lotaCadastrante, doc);
			}

			cancelarMovimentacoesReferencia(cadastrante, lotaCadastrante, doc);

			ExDocumento novoDoc = duplicarDocumento(cadastrante, lotaCadastrante, doc, true);

			cancelarMovimentacoes(cadastrante, lotaCadastrante, doc);

			String funcao = doc.getForm().get("acaoGravar");
			if (funcao != null) {
				obterMetodoPorString(funcao, doc);
			}
			
			if (Prop.isGovSP()) {
			//Gerar movimentação REFAZER para Mobil Pai
				if (doc.getExMobilPai() != null) {
					final ExMovimentacao mov = criarNovaMovimentacao(
							ExTipoDeMovimentacao.REFAZER,
							cadastrante, lotaCadastrante, doc.getExMobilPai(), null, null, null,
							null, null, null);
					mov.setDescrMov("Documento refeito. <br /> Documento Cancelado: " + doc.getSigla() + ".<br /> Novo Documento:  " + novoDoc.getSigla());
				
					gravarMovimentacao(mov);
				}
			}	
			
			concluirAlteracaoDocComRecalculoAcesso(novoDoc);
			// atualizarWorkflow(doc, null);
			atualizarMarcas(doc);
			return novoDoc;
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao refazer o documento.", e);
		}
	}

	// Nato: removi: HttpServletRequest request
	public ExDocumento duplicar(DpPessoa cadastrante, final DpLotacao lotaCadastrante, ExDocumento doc) {

		try {
			iniciarAlteracao();

			ExDocumento novoDoc = duplicarDocumento(cadastrante, lotaCadastrante, doc, false);

			concluirAlteracaoDocComRecalculoAcesso(novoDoc);
			return novoDoc;

		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao duplicar o documento.", e);
		}
	}

	private ExDocumento duplicarDocumento(DpPessoa cadastrante, final DpLotacao lotaCadastrante, ExDocumento doc,
			final boolean refazendo) throws Exception {
		ExDocumento novoDoc = new ExDocumento();

		novoDoc.setOrgaoUsuario(cadastrante.getOrgaoUsuario());
		novoDoc.setConteudoBlobDoc(doc.getConteudoBlobDoc());
		novoDoc.setConteudoTpDoc(doc.getConteudoTpDoc());
		novoDoc.setDescrDocumento(doc.getDescrDocumento());

		if (doc.getDestinatario() != null && !doc.getDestinatario().isFechada())
			novoDoc.setDestinatario(doc.getDestinatario().getPessoaAtual());

		final CpSituacaoDeConfiguracaoEnum idSit = Ex.getInstance().getConf().buscaSituacao(doc.getExModelo(), doc.getExTipoDocumento(),
				cadastrante, lotaCadastrante, ExTipoDeConfiguracao.ELETRONICO);

		if (idSit == CpSituacaoDeConfiguracaoEnum.OBRIGATORIO) {
			novoDoc.setFgEletronico("S");
		} else if (idSit == CpSituacaoDeConfiguracaoEnum.PROIBIDO) {
			novoDoc.setFgEletronico("N");
		} else {
			novoDoc.setFgEletronico(doc.getFgEletronico());
		}

		novoDoc.setExNivelAcesso(doc.getExNivelAcessoAtual());

		ExClassificacao classAtual = doc.getExClassificacaoAtual();
		if (classAtual != null && !classAtual.isFechada())
			if (classAtual.getAtual() != null)
				novoDoc.setExClassificacao(classAtual.getAtual());
			else
				novoDoc.setExClassificacao(classAtual);
		novoDoc.setDescrClassifNovo(doc.getDescrClassifNovo());
		novoDoc.setExFormaDocumento(doc.getExFormaDocumento());

		if (!doc.getExModelo().isFechado())
			novoDoc.setExModelo(doc.getExModelo().getModeloAtual());
		else
			throw new AplicacaoException("não foi possível duplicar o documento pois este modelo não estámais em uso.");

		novoDoc.setExTipoDocumento(doc.getExTipoDocumento());

		if (doc.getLotaDestinatario() != null && !doc.getLotaDestinatario().isFechada())
			novoDoc.setLotaDestinatario(doc.getLotaDestinatario().getLotacaoAtual());

		if (doc.getSubscritor() != null && !doc.getSubscritor().isFechada()) {
			novoDoc.setSubscritor(doc.getSubscritor().getPessoaAtual());
			novoDoc.setLotaSubscritor(doc.getSubscritor().getPessoaAtual().getLotacao());
		}

		novoDoc.setNmArqDoc(doc.getNmArqDoc());
		novoDoc.setNmDestinatario(doc.getNmDestinatario());
		novoDoc.setNmFuncaoSubscritor(doc.getNmFuncaoSubscritor());
		novoDoc.setNmOrgaoExterno(doc.getNmOrgaoExterno());
		novoDoc.setNmSubscritorExt(doc.getNmSubscritorExt());
		novoDoc.setNumExtDoc(doc.getNumExtDoc());
		novoDoc.setObsOrgao(doc.getObsOrgao());
		novoDoc.setOrgaoExterno(doc.getOrgaoExterno());
		novoDoc.setOrgaoExternoDestinatario(doc.getOrgaoExternoDestinatario());

		if (refazendo)
			novoDoc.setExMobilPai(doc.getExMobilPai());
		else
			novoDoc.setExMobilPai(null);

		if (doc.getTitular() != null && !doc.getTitular().isFechada())
			novoDoc.setTitular(doc.getTitular().getPessoaAtual());

		if (doc.getLotaTitular() != null && !doc.getLotaTitular().isFechada())
			novoDoc.setLotaTitular(doc.getLotaTitular().getLotacaoAtual());

		novoDoc.setNumAntigoDoc(doc.getNumAntigoDoc());
		novoDoc.setIdDocAnterior(doc.getIdDoc());
		// novoDoc.setNumPaginas(novoDoc.getContarNumeroDePaginas());

		ExMobil mob = new ExMobil();
		mob.setExTipoMobil(dao().consultar(ExTipoMobil.TIPO_MOBIL_GERAL, ExTipoMobil.class, false));
		mob.setNumSequencia(1);
		mob.setExDocumento(novoDoc);
		mob.setExMovimentacaoSet(new TreeSet<ExMovimentacao>());
		novoDoc.setExMobilSet(new TreeSet<ExMobil>());
		novoDoc.getExMobilSet().add(mob);

		novoDoc = gravar(cadastrante, cadastrante, lotaCadastrante, novoDoc);
		mob.setDnmSigla(mob.getSigla());
	
		// mob = dao().gravar(mob);

		for (ExMovimentacao mov : doc.getMobilGeral().getExMovimentacaoSet()) {
			if (doc.isFinalizado() && mov.getDtIniMov().after(doc.getDtFinalizacao()))
				break;
			if (mov.isCancelada())
				continue;
			switch ((ExTipoDeMovimentacao) mov.getExTipoMovimentacao()) {
			case REDEFINICAO_NIVEL_ACESSO:
			case INCLUSAO_DE_COSIGNATARIO:
			case ANEXACAO:
			case ANOTACAO:
				ExMovimentacao novaMov = duplicarMovimentacao(cadastrante, lotaCadastrante, mov, novoDoc);
	

				try {
					iniciarAlteracao();
					gravarMovimentacao(novaMov);
					concluirAlteracaoDoc(novaMov);
				} catch (final Exception e) {
					cancelarAlteracao();
					throw new RuntimeException("Erro ao gravar movimentacao.", e);
				}
			}
		}

		// é necessário gravar novamente pois uma movimentação de inclusão
		// de cossignatário pode ter sido introduzida, gerando a necessidade
		// de refazer o HTML e o PDF. Quando o documento é colaborativo, não
		// deve ser gravado novamente pois isso faria com que ele fosse
		// automaticamente finalizado, o que não é desejavel na duplicação.
		if (!doc.isColaborativo())
			novoDoc = gravar(cadastrante, cadastrante, lotaCadastrante, novoDoc);

		return novoDoc;
	}

	private ExMovimentacao duplicarMovimentacao(DpPessoa cadastrante, DpLotacao lotaCadastrante, ExMovimentacao mov, ExDocumento novoDoc)
			throws AplicacaoException {
		ExMovimentacao novaMov = new ExMovimentacao();
		novaMov.setCadastrante(cadastrante);
		novaMov.setDescrMov(mov.getDescrMov());
		novaMov.setDtIniMov(dao().dt());
		novaMov.setDtMov(mov.getDtMov());
		novaMov.setExNivelAcesso(mov.getExNivelAcesso());
		novaMov.setExTipoMovimentacao(mov.getExTipoMovimentacao());
		novaMov.setLotaCadastrante(lotaCadastrante);
		novaMov.setLotaResp(mov.getLotaResp());
		novaMov.setLotaSubscritor(mov.getLotaSubscritor());
		novaMov.setLotaTitular(mov.getLotaTitular());
		novaMov.setNmArqMov(mov.getNmArqMov());
		novaMov.setNmFuncaoSubscritor(mov.getNmFuncaoSubscritor());
		novaMov.setNumPaginas(mov.getNumPaginas());
		novaMov.setNumPaginasOri(mov.getNumPaginasOri());
		novaMov.setResp(mov.getResp());
		novaMov.setSubscritor(mov.getSubscritor());
		novaMov.setTitular(mov.getTitular());
		novaMov.setExPapel(mov.getExPapel());
		novaMov.setExMobil(novoDoc.getMobilGeral());
		acrescentarCamposDeAuditoria(novaMov);
		if (mov.getConteudoTpMov() != null && mov.getConteudoBlobMov() != null) {
			
			gerarIdDeMovimentacao(novaMov);
			novaMov.setConteudoBlobMov(mov.getConteudoBlobMov());
			novaMov.setConteudoTpMov(mov.getConteudoTpMov());
		}
		return novaMov;
	}
	
	public int cancelarTramitesPendentes(final ExMobil mob, final String motivo) {
		int c = 0;
		Pendencias p = mob.calcularTramitesPendentes();
		for (ExMovimentacao mov : p.tramitesPendentes) {
			try {
				cancelar(null, null, mob, mov, null, null, null, motivo, true);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			c++;
		}
		return c;
	}

	// Nato: removi , final DpPessoa subscritor, final DpPessoa responsavel,
	// pois nao eram utilizados
	public void receber(final DpPessoa cadastrante, final DpPessoa titular, final DpLotacao lotaTitular, final ExMobil mob,
			final Date dtMov) throws AplicacaoException {

		SortedSet<ExMobil> set = mob.getMobilEApensosExcetoVolumeApensadoAoProximo();

		try {
			iniciarAlteracao();;

			for (ExMobil m : set) {
				final ExMobil geral = mob.doc().getMobilGeral();
				
				Pendencias p = m.calcularTramitesPendentes();
				
				// Concluir trâmites ou recebimentos de notificação pendentes quando já é atendente.
				// Seleciona o que será mantido
				ExMovimentacao selecionado = null;
				ExMovimentacao tramiteASerRecebido = null;
				{
					Set<ExMovimentacao> tramitesERecebimentosPendentes = new TreeSet<>(new ExMovimentacaoRecebimentoComparator());
					for (ExMovimentacao r : p.tramitesPendentes)
						if (r.isRespExato(titular, lotaTitular)) 
							tramitesERecebimentosPendentes.add(r);
					for (ExMovimentacao r : p.recebimentosPendentes)
						if (r.isRespExato(titular, lotaTitular))
							tramitesERecebimentosPendentes.add(r);

					// Tem mais de um trâmite ou recebimento pendente para o usuário ou a lotação
					if (tramitesERecebimentosPendentes.size() > 1) {
						// Tenta selecionar um recebimento da lotação, que não seja de notificação, que será mantido
						for (ExMovimentacao r : tramitesERecebimentosPendentes)
							if (r.getExTipoMovimentacao() == ExTipoDeMovimentacao.RECEBIMENTO && !p.recebimentosDeNotificacoesPendentes.contains(r)) {
							    tramiteASerRecebido = r;
                                break;
                            }
						// Seleciona o que será mantido
                        for (ExMovimentacao r : tramitesERecebimentosPendentes)
                            if (r.getExTipoMovimentacao() != ExTipoDeMovimentacao.NOTIFICACAO
                                    && r.getExTipoMovimentacao() != ExTipoDeMovimentacao.TRAMITE_PARALELO
                                    && r.getExTipoMovimentacao() != ExTipoDeMovimentacao.RECEBIMENTO) {
                                tramiteASerRecebido = r;
                                break;
                            }
                        if (tramiteASerRecebido == null)
                            for (ExMovimentacao r : tramitesERecebimentosPendentes)
                                if (r.getExTipoMovimentacao() != ExTipoDeMovimentacao.NOTIFICACAO
                                        && r.getExTipoMovimentacao() != ExTipoDeMovimentacao.RECEBIMENTO) {
                                    tramiteASerRecebido = r;
                                    break;
                                }
						if (tramiteASerRecebido == null)
							for (ExMovimentacao r : tramitesERecebimentosPendentes)
								if (r.getExTipoMovimentacao() != ExTipoDeMovimentacao.NOTIFICACAO) {
									tramiteASerRecebido = r;
									break;
								}
						if (tramiteASerRecebido == null)
							for (ExMovimentacao r : tramitesERecebimentosPendentes) {
								tramiteASerRecebido = r;
								break;
							}
						// Seleciona o primeiro item, pois o set já está ordenado
						if (selecionado == null)
							selecionado = tramitesERecebimentosPendentes.stream().findFirst().get();
						// Conclui demais tramites e recebimentos pendentes
						for (ExMovimentacao pend : tramitesERecebimentosPendentes) {
							if (tramiteASerRecebido == pend)
								continue;
							final ExMovimentacao mov = criarNovaMovimentacao(
									ExTipoDeMovimentacao.CONCLUSAO, cadastrante, lotaTitular, m, dtMov,
									titular, null, null, null, null);
							mov.setResp(titular);
							mov.setLotaResp(lotaTitular);
							mov.setDestinoFinal(pend.getDestinoFinal());
							mov.setLotaDestinoFinal(pend.getLotaDestinoFinal());
							mov.setExMovimentacaoRef(pend);
							
							// Localiza a última movimentação de marcação de lotação, para cancelar ela com a conclusao
							ExMovimentacao movAnterior = localizaMarcacaoDePasta(mob, pend);
							if (movAnterior != null)
								gravarMovimentacaoCancelamento(mov, movAnterior);
							else
								gravarMovimentacao(mov);
						}
						p = m.calcularTramitesPendentes();
					}
				}

				// Se existe algum tramite pendente, então a movimentação selecionada na etapa anterior não será um recebimento
				if (tramiteASerRecebido == null || tramiteASerRecebido.getExTipoMovimentacao() != ExTipoDeMovimentacao.RECEBIMENTO) {
    				// Se o móbil ainda não foi movimentado e o titular e a lotaTitular forem as mesmas do móbil, é sinal de que houve uma notificação 
    				// ou um trâmite paralelo para o próprio cadastrante. Neste caso, em vez de receber deve concluir direto
    				boolean fConcluirDireto = p.fIncluirCadastrante && Utils.equivale(mob.getTitular(), titular)
    						&& Utils.equivale(mob.getLotaTitular(), lotaTitular) && !mob.isEmTransitoExterno();
    				
    				final ExMovimentacao mov = criarNovaMovimentacao(fConcluirDireto ? ExTipoDeMovimentacao.CONCLUSAO : ExTipoDeMovimentacao.RECEBIMENTO,
    						cadastrante, lotaTitular, m, dtMov, titular, null, null, null, null);
    
    				// Localiza o tramite que será recebido
    				if (tramiteASerRecebido == null)
        				for (ExMovimentacao t : p.tramitesPendentes) {
        					if (t.isResp(titular, lotaTitular)) {
        					    tramiteASerRecebido = t;
        						break;
        					}
        				}
    				
    				if (tramiteASerRecebido == null && !mob.isEmTransitoExterno())
    					throw new AplicacaoException("Não foi encontrado nenhum trâmite pendente para o usuário corrente ou sua lotação");
    				
    				mov.setResp(titular);
    				mov.setLotaResp(lotaTitular);
    				
    				if (tramiteASerRecebido != null) {
    					mov.setDestinoFinal(tramiteASerRecebido.getDestinoFinal());
    					mov.setLotaDestinoFinal(tramiteASerRecebido.getLotaDestinoFinal());
    					mov.setExMovimentacaoRef(tramiteASerRecebido);
    				}
    
    				// Localiza a última movimentação de marcação de lotação, para cancelar ela com o recebimento
    				ExMovimentacao movAnterior = localizaMarcacaoDePasta(m, tramiteASerRecebido);
    
    				if (movAnterior != null) {
    					gravarMovimentacaoCancelamento(mov, movAnterior);
    				} else
    					gravarMovimentacao(mov);
    				
    				// Se houver configuração para restringir acesso somente para quem recebeu,
    				// remove a lotação das permissões de acesso e inclui o recebedor
    				if (Ex.getInstance().getConf().podePorConfiguracao(mov.getResp(), mov.getLotaResp(), 
    						null, mob.doc().getExModelo().getExFormaDocumento(), mob.doc().getExModelo(), 
    						ExTipoDeConfiguracao.RESTRINGIR_ACESSO_APOS_RECEBER)) {
    					concluirAlteracaoParcial(m, true, mov.getResp(), mov.getLotaResp());
    				} else {
    					concluirAlteracaoParcial(m);
    				}
				} else {
                    concluirAlteracaoParcial(m);
				}
			}
			concluirAlteracao();
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao receber documento.", e);
		}
	}

	// Localiza a última movimentação de marcação de lotação, para cancelar ela com o recebimento
	private ExMovimentacao localizaMarcacaoDePasta(ExMobil mob, ExMovimentacao tramiteOuRecebimento) {
		ExMovimentacao movMarcacaoDePasta = null;
		List<ExMovimentacao> movs = mob.getMovimentacoesPorTipo(ExTipoDeMovimentacao.MARCACAO, true);
		if (!mob.isGeral())
			movs.addAll(mob.doc().getMobilGeral().getMovimentacoesPorTipo(ExTipoDeMovimentacao.MARCACAO, true));
		for (ExMovimentacao mv : movs) {
			boolean isRespPorTramiteOuRecebimento = tramiteOuRecebimento != null && (Utils.equivale(mv.getMarcador().getDpLotacaoIni(), tramiteOuRecebimento.getLotaCadastrante())
					|| Utils.equivale(mv.getMarcador().getDpLotacaoIni(), tramiteOuRecebimento.getLotaTitular()));
			boolean isCadastrante = tramiteOuRecebimento == null && (Utils.equivale(mv.getMarcador().getDpLotacaoIni(), mob.doc().getLotaCadastrante())
					|| Utils.equivale(mv.getMarcador().getDpLotacaoIni(), mob.doc().getLotaTitular()));
			if (mv.getMarcador() != null && (mv.getMarcador().getIdFinalidade() == CpMarcadorFinalidadeEnum.PASTA 
					 || mv.getMarcador().getIdFinalidade() == CpMarcadorFinalidadeEnum.PASTA_PADRAO)
					&& (isRespPorTramiteOuRecebimento || isCadastrante)) {
				movMarcacaoDePasta = mv;
				break;
			}
		}

		// Marcação deve ser removida só se a lotação estiver sendo alterada
		if (movMarcacaoDePasta != null && tramiteOuRecebimento != null) {
			if (movMarcacaoDePasta.getMarcador().getDpLotacaoIni() != null	&& movMarcacaoDePasta.getMarcador().getDpLotacaoIni().equivale(tramiteOuRecebimento.getLotaResp())) {
				movMarcacaoDePasta = null;
			}
		}
		return movMarcacaoDePasta;
	}
	
	public void concluir(DpPessoa cadastrante, final DpLotacao lotaCadastrante, DpPessoa titular, final DpLotacao lotaTitular, ExMobil mob, Date dtMov,
			Date dtMovIni, DpPessoa subscritor) {

		try {
			iniciarAlteracao();

			if (mob.isGeralDeProcesso()) {
				mob = mob.doc().getUltimoVolume();
			}
			
			Pendencias p = mob.calcularTramitesPendentes();
			
			ExMovimentacao recebimento = null;
			// Localiza o recebimento que será concluído
			
			// Primeiro busca entre as notificações
			for (ExMovimentacao r : p.recebimentosDeNotificacoesPendentes) {
				if (r.isRespPreferencialmentePelaLotacao(titular, lotaTitular)) {
					recebimento = r;
					break;
				}
			}
			// Tenta um outro recebimento qualquer
			if (recebimento == null) {
				for (ExMovimentacao r : p.recebimentosPendentes) {
					if (r.isRespPreferencialmentePelaLotacao(titular, lotaTitular)) {
						recebimento = r;
					}
				}
				
                if (recebimento == null)
                    if (p.fIncluirCadastrante && (Utils.equivale(mob.getLotaTitular(), lotaTitular)
                            || (mob.getLotaTitular() == null && Utils.equivale(mob.getTitular(), titular)))) {
                        // Ok, pois se trata da conclusão do "trâmite" em posse do cadastrante.
                    } else {
                            throw new AplicacaoException("Não foi encontrado nenhum recebimento pendente para o usuário corrente ou sua lotação");
                    }

				// Se houver apenas um recebimento, produzir um erro, pois o 
				int c = p.fIncluirCadastrante ? 1 : 0;
				for (ExMovimentacao r : p.recebimentosPendentes) {
					if (!p.recebimentosDeNotificacoesPendentes.contains(r)) {
						c++;
					}
				}
				if (c <= 1)
					throw new AplicacaoException("Não é permitido concluir o último recebimento, em vez disso, deve ser realizado o arquivamento");
			}
	
			
			final ExMovimentacao mov = gerarMovimentacaoDeConclusao(cadastrante, lotaCadastrante, titular, lotaTitular, mob, dtMov,
					dtMovIni, subscritor, recebimento);

			// Localiza a última movimentação de marcação de lotação, para cancelar ela com a conclusao
			ExMovimentacao movAnterior = localizaMarcacaoDePasta(mob, recebimento);

			if (movAnterior != null) {
				gravarMovimentacaoCancelamento(mov, movAnterior);
			} else
				gravarMovimentacao(mov);
			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao concluir documento.", e);
		}
	}

	public ExMovimentacao gerarMovimentacaoDeConclusao(DpPessoa cadastrante, final DpLotacao lotaCadastrante, DpPessoa titular,
			final DpLotacao lotaTitular, ExMobil mob, Date dtMov, Date dtMovIni, DpPessoa subscritor,
			ExMovimentacao recebimento) {
		Date dt = dtMovIni != null ? dtMovIni : dao().dt();
		final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.CONCLUSAO,
				cadastrante, lotaCadastrante, mob, dtMov, subscritor, null, null, null, dt);
		
		mov.setResp(titular);
		mov.setLotaResp(lotaTitular);
		if (recebimento != null) {
			mov.setDestinoFinal(recebimento.getDestinoFinal());
			mov.setLotaDestinoFinal(recebimento.getLotaDestinoFinal());
			mov.setExMovimentacaoRef(recebimento);
		}
		return mov;
	}

	public void receberEletronico(ExMovimentacao mov) throws AplicacaoException {

		// try {
		// iniciarAlteracao();
		// mov.setExEstadoDoc(dao().consultar(
		// ExEstadoDoc.ESTADO_DOC_EM_ANDAMENTO, ExEstadoDoc.class,
		// false));
		// dao().gravar(mov);
		// concluirAlteracao(mov);
		// } catch (final Exception e) {
		// cancelarAlteracao();
		// throw new AplicacaoException("Erro ao receber documento.", 0, e);
		//
		// }

		// Nato: alterei para chamar a receber, pois temos que criar uma
		// movimentacao para influenciar no calculo dos marcadores
		receber(null, null, null, mov.getExMobil(), null);

	}

	public void indicarPermanente(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor, final DpPessoa titular, final String descrMov)
			throws AplicacaoException {

		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoDeMovimentacao.INDICACAO_GUARDA_PERMANENTE, cadastrante, lotaCadastrante, mob,
					dtMov, subscritor, null, titular, null, null);

			mov.setDescrMov(descrMov);

			gravarMovimentacao(mov);
			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao indicar para guarda permenente.", e);
		}
	}

	public void reverterIndicacaoPermanente(final DpPessoa cadastrante, final DpLotacao lotaCadastrante,
			final ExMobil mob, final Date dtMov, final DpPessoa subscritor, final DpPessoa titular, String descrMov)
			throws AplicacaoException {

		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoDeMovimentacao.REVERSAO_INDICACAO_GUARDA_PERMANENTE, cadastrante,
					lotaCadastrante, mob, dtMov, subscritor, null, titular, null, null);

			mov.setDescrMov(descrMov);

			gravarMovimentacao(mov);
			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao reverter indicação para guarda permenente.", e);
		}
	}

	public void referenciarDocumento(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, ExMobil mob,
			ExMobil mobRef, final ExTipoDeVinculo tipo, final Date dtMov, final DpPessoa subscritor, final DpPessoa titular)
			throws AplicacaoException {
		
		mob = mob.doc().getMobilGeral();

		if (mobRef == null)
			throw new AplicacaoException("não foi selecionado um documento para o vinculo");

		mobRef = mobRef.doc().getMobilGeral();

		if (mob.getExDocumento().getIdDoc().equals(mobRef.getExDocumento().getIdDoc())
				&& mob.getNumSequencia().equals(mobRef.getNumSequencia())
				&& mob.getExTipoMobil().getIdTipoMobil().equals(mobRef.getExTipoMobil().getIdTipoMobil()))
			throw new AplicacaoException("não é possível vincular um documento a ele mesmo");

		if (!mobRef.getExDocumento().isFinalizado())
			throw new AplicacaoException("não é possível vincular-se a um documento não finalizado");

		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.REFERENCIA,
					cadastrante, lotaCadastrante, mob, dtMov, subscritor, null, titular, null, null);

			mov.setExMobilRef(mobRef);
			mov.setTipoDeVinculo(tipo);
			mov.setDescrMov(tipo.getAcao() + ": " + mov.getExMobilRef().getCodigo().toString());

			gravarMovimentacao(mov);
			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao vincular documento.", e);
		}
	}

	public void copiar(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExMobil mob,
			final ExMobil mobRef, final Date dtMov, final DpPessoa subscritor, final DpPessoa titular)
			throws AplicacaoException {

		final ExMobil mobRefGeral = mobRef.doc().getMobilGeral();

		if (mobRefGeral == null)
			throw new AplicacaoException("não foi selecionado um documento para a inclusão de cópia");

		if (mob.getExDocumento().getIdDoc().equals(mobRefGeral.getExDocumento().getIdDoc())
				&& mob.getNumSequencia().equals(mobRefGeral.getNumSequencia())
				&& mob.getExTipoMobil().getIdTipoMobil().equals(mobRefGeral.getExTipoMobil().getIdTipoMobil()))
			throw new AplicacaoException("não é possível incluir uma cópia de um documento nele mesmo");

		if (!mobRefGeral.getExDocumento().isFinalizado())
			throw new AplicacaoException("não é possível incluir uma cópia de um documento não finalizado");

		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.COPIA, cadastrante,
					lotaCadastrante, mob, dtMov, subscritor, null, titular, null, null);

			mov.setExMobilRef(mobRefGeral);
			mov.setDescrMov("Inclusão de Cópia: documento " + mov.getExMobilRef().getCodigo().toString());

			gravarMovimentacao(mov);
			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao copiar documento.", e);
		}
	}

	public String RegistrarAssinatura(final DpPessoa cadastrante, final DpLotacao lotaCadastrante,
			final ExDocumento doc, final Date dtMov, final DpPessoa subscritor, final DpPessoa titular)
			throws AplicacaoException {
		boolean fPreviamenteAssinado = !doc.isPendenteDeAssinatura();

		if (!doc.isFinalizado())
			throw new AplicacaoException("não é possível registrar assinatura de um documento não finalizado");

		String s = null;
		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoDeMovimentacao.REGISTRO_ASSINATURA_DOCUMENTO, cadastrante, lotaCadastrante,
					doc.getMobilGeral(), dtMov, subscritor, null, null, null, null);

			gravarMovimentacao(mov);
			concluirAlteracaoDocComRecalculoAcesso(mov);

			// Verifica se o documento possui documento pai e faz a juntada
			// automática.
			if (doc.getExMobilPai() != null) {
				juntarAoDocumentoPai(cadastrante, lotaCadastrante, doc, dtMov, subscritor, titular, mov);
			}

			if (!fPreviamenteAssinado && !doc.isPendenteDeAssinatura())
				s = processarComandosEmTag(doc, "assinatura");
		} catch (final Exception e) {
			cancelarAlteracao();
			log.error(e.getMessage(), e);
			e.printStackTrace();
			throw new RuntimeException("Erro ao registrar assinatura." + getRootCauseMessage(e));
		}
		return s;
	}

	public void transferirAutomatico(DpPessoa cadastrante, final DpLotacao lotaCadastrante, DpPessoa resp,
			DpLotacao lotaResp, ExMobil mob) throws Exception {

		transferir(null, null, cadastrante, lotaCadastrante, mob, null, null, null, lotaResp, resp, null, null, null, null,
				null, null, false, null, null, null, false, true, ExTipoDeMovimentacao.TRANSFERENCIA);
	}

	/**
	 * Transfere um documento de um local para outro. Esse método trata tanto da
	 * movimentação "Despacho com transferência" como "Transferência".
	 * 
	 * @param orgaoExterno
	 * @param obsOrgao
	 * @param cadastrante
	 * @param lotaCadastrante
	 * @param mob
	 * @param dtMov
	 * @param dtMovIni
	 * @param dtFimMov
	 * @param lotaResponsavel
	 * @param responsavel
	 * @param lotaDestinoFinal
	 * @param destinoFinal
	 * @param subscritor
	 * @param titular
	 * @param tpDespacho
	 * @param fInterno
	 * @param descrMov
	 * @param conteudo
	 * @param nmFuncaoSubscritor
	 * @throws AplicacaoException
	 * @throws Exception
	 */
	// Nato: retirei: final HttpServletRequest request,

	public void transferir(final CpOrgao orgaoExterno, final String obsOrgao, final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob, final Date dtMov, final Date dtMovIni,
			final Date dtFimMov, DpLotacao lotaResponsavel, final DpPessoa responsavel, final CpGrupoDeEmail grupo,
			final DpLotacao lotaDestinoFinal, final DpPessoa destinoFinal, final DpPessoa subscritor,
			final DpPessoa titular, final ExTipoDespacho tpDespacho, final boolean fInterno, final String descrMov,
			final String conteudo, String nmFuncaoSubscritor, boolean forcarTransferencia, boolean automatico, final ITipoDeMovimentacao tipoTramite) {
		boolean fDespacho = tpDespacho != null || descrMov != null || conteudo != null;

		boolean fTranferencia = lotaResponsavel != null || responsavel != null;
		
		final DpPessoa titularFinal = titular != null? titular : cadastrante;

		SortedSet<ExMobil> set = mob.getMobilEApensosExcetoVolumeApensadoAoProximo();

		Date dtUltReceb = null;

		// Edson: apagar isto? A verificação já é feita no for abaixo...
		if (fDespacho && mob.isApensadoAVolumeDoMesmoProcesso())
			throw new AplicacaoException("não é possível fazer despacho em um documento que faça parte de um apenso");

		if (!automatico) {

			if (fTranferencia && mob.doc().isEletronico()) {
				if (mob.doc().getMobilGeral().temAnexosNaoAssinados()
						|| mob.doc().getMobilGeral().temDespachosNaoAssinados())
					throw new AplicacaoException(
							"não é permitido tramitar documento com anexo/despacho pendente de assinatura ou conferência");

			}

			if (fDespacho && mob.isVolumeEncerrado())
				if (!mob.isApensadoAVolumeDoMesmoProcesso())
					throw new AplicacaoException(
							"não é permitido fazer despacho em volume que esta encerrado ID_MOBIL:" + mob.getId());

			for (ExMobil m : set) {

				if (!m.equals(mob) && fDespacho && fTranferencia) {
					throw new AplicacaoException(
							"não é permitido fazer despacho com trâmite em um documento que faça parte de um apenso. faça primeiro o despacho e depois tramite o documento.");
				}

				if (fDespacho)
					getComp().afirmar("não é permitido fazer despacho. Verifique se a via ou processo não está arquivado(a) e se não possui despachos pendentes de assinatura.",
							ExPodeDespachar.class, cadastrante, lotaCadastrante, m);
					
				if (fTranferencia) {

					if (!m.isApensadoAVolumeDoMesmoProcesso()) {

						if (lotaResponsavel != null && lotaResponsavel.isFechada())
							throw new AplicacaoException("não é permitido tramitar documento para lotação fechada");

						if (forcarTransferencia) {
							if (!new ExPodeSerTransferido(mob).eval())
								throw new AplicacaoException("Trâmite não pode ser realizado (" + m.getSigla()
										+ " ID_MOBIL: " + m.getId() + ")");
						} else {
							if (tipoTramite == ExTipoDeMovimentacao.NOTIFICACAO) {
								if (!Ex.getInstance().getComp().pode(ExPodeNotificar.class, cadastrante, lotaCadastrante, m)) 
									throw new AplicacaoException("Não é possível notificar");			
							} else 
								getComp().afirmar("Trâmite não permitido (" + m.getSigla() + " ID_MOBIL: " + m.getId() + ")", 
										ExPodeTransferir.class, cadastrante, lotaCadastrante, m);
						}
					
						if (m.getExDocumento().isPendenteDeAssinatura()
								&& !lotaResponsavel.equivale(m.getExDocumento().getLotaTitular()))
							getComp().afirmar("não é permitido tramitar documento que ainda não foi assinado", ExPodeReceberDocumentoSemAssinatura.class, responsavel, lotaResponsavel, m);

						if (m.doc().isEletronico()) {
							if (m.temAnexosNaoAssinados() || m.temDespachosNaoAssinados())
								throw new AplicacaoException(
										"não é permitido tramitar documento com anexo/despacho pendente de assinatura ou conferência");
						}

						if (m.getExDocumento().isEletronico() && m.getExDocumento().isPendenteDeAssinatura())
							throw new AplicacaoException(
									"não é permitido tramitar documento que ainda não foi assinado por todos os subscritores.");
					}

				}

				if (!fDespacho) {
					if (responsavel == null && lotaResponsavel == null && grupo == null)
						if (orgaoExterno == null && obsOrgao == null)
							throw new AplicacaoException("não foram informados dados para o trâmite");
				}
			}
		}

		Date dt = dtMovIni != null ? dtMovIni : dao().dt();

		try {
			iniciarAlteracao();

			for (ExMobil m : set) {

				ITipoDeMovimentacao idTpMov;
				if (!fDespacho) {
					if (responsavel == null && lotaResponsavel == null && grupo == null)
						idTpMov = ExTipoDeMovimentacao.TRANSFERENCIA_EXTERNA;
					else
						idTpMov = ExTipoDeMovimentacao.TRANSFERENCIA;
				} else if (lotaResponsavel == null && responsavel == null && orgaoExterno == null) {
					if (fInterno)
						idTpMov = ExTipoDeMovimentacao.DESPACHO_INTERNO;
					else
						idTpMov = ExTipoDeMovimentacao.DESPACHO;
				} else if (orgaoExterno != null) {
					if (fInterno)
						idTpMov = ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA_EXTERNA;
					else
						idTpMov = ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA_EXTERNA;
				} else {
					if (fInterno)
						idTpMov = ExTipoDeMovimentacao.DESPACHO_INTERNO_TRANSFERENCIA;
					else
						idTpMov = ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA;
				}
				
				// Aplica o tipo correto de trâmite
				if (idTpMov == ExTipoDeMovimentacao.TRANSFERENCIA 
						&& (tipoTramite == ExTipoDeMovimentacao.TRAMITE_PARALELO 
						|| tipoTramite == ExTipoDeMovimentacao.NOTIFICACAO)) 
					idTpMov = tipoTramite;

				// se não for apensado, pode.
				// se for apenas tranferência, pode.
				if (m.equals(mob) || idTpMov == ExTipoDeMovimentacao.TRANSFERENCIA_EXTERNA
						|| idTpMov == ExTipoDeMovimentacao.TRANSFERENCIA 
						|| idTpMov == ExTipoDeMovimentacao.TRAMITE_PARALELO
						|| idTpMov == ExTipoDeMovimentacao.NOTIFICACAO) {
					
					Set<PessoaLotacaoParser> destinatarios = new HashSet<>();
					if (grupo != null) {
						ArrayList<ConfiguracaoGrupo> configuracoesGrupo = Cp.getInstance().getConf()
								.obterCfgGrupo(dao().consultar(grupo.getHisIdIni(),CpGrupo.class,false));
						for (ConfiguracaoGrupo cfgGrp : configuracoesGrupo) {
							CpConfiguracao cfg = cfgGrp.getCpConfiguracao();
							switch (cfgGrp.getTipo()) {
							case PESSOA:
						        if (cfg.getDpPessoa() != null) {
                                    DpPessoa dpPessoa = cfg.getDpPessoa().getPessoaAtual();
                                    destinatarios.add(new PessoaLotacaoParser(dpPessoa, dpPessoa.getLotacao()));
						        }
								break;
							case LOTACAO:
						        if (cfg.getLotacao() != null) {
                                    DpLotacao lotacao = cfg.getLotacao().getLotacaoAtual();
                                    destinatarios.add(new PessoaLotacaoParser(null, lotacao));
						        }
								break;
							}
						}
					} else {
						destinatarios.add(new PessoaLotacaoParser(responsavel, lotaResponsavel));
					}
					
					for (PessoaLotacaoParser destinatario : destinatarios) {
						ExMovimentacao mov = criarNovaMovimentacaoTransferencia(idTpMov, cadastrante,
								lotaCadastrante, m, dtMov, dtFimMov,
								(subscritor == null && fDespacho) ? cadastrante : subscritor, null, titular, null, dt);
	
						if (dt != null)
							mov.setDtIniMov(dt);
	
						if (dtFimMov != null)//
							mov.setDtFimMov(dtFimMov);//
	
						if (orgaoExterno != null || obsOrgao != null) {
							mov.setOrgaoExterno(orgaoExterno);
							mov.setObsOrgao(obsOrgao);
						}
	
						mov.setLotaResp(destinatario.getLotacao());
						mov.setResp(destinatario.getPessoa());
	
						mov.setLotaTitular(mov.getLotaSubscritor());
						mov.setDestinoFinal(destinoFinal);
						mov.setLotaDestinoFinal(lotaDestinoFinal);
	
						mov.setNmFuncaoSubscritor(nmFuncaoSubscritor);
	
						mov.setExTipoDespacho(tpDespacho);
						mov.setDescrMov(descrMov);
	
						if (tpDespacho != null || descrMov != null || conteudo != null) {
							// Gravar o form
							String cont = null;
							if (conteudo != null) {
								cont = conteudo;
							} else if (descrMov != null) {
								cont = descrMov;
							} else {
								cont = tpDespacho.getDescTpDespacho();
							}
							try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
								baos.write("conteudo".getBytes("iso-8859-1"));
								baos.write('=');
								baos.write(URLEncoder.encode(cont, "iso-8859-1").getBytes());
								mov.setConteudoBlobForm(baos.toByteArray());
							}
	
							// Gravar o Html //Nato
							final String strHtml = processarModelo(mov, "processar_modelo", null,
									mov.getTitular().getOrgaoUsuario());
							mov.setConteudoBlobHtmlString(strHtml);
	
							// Gravar o Pdf
							final byte pdf[] = Documento.generatePdf(strHtml);
							mov.setConteudoBlobPdf(pdf);
							mov.setConteudoTpMov("application/zip");
						}
						if (automatico)
							mov.setDescrMov("Transferência automática.");
						
						Pendencias p = m.calcularTramitesPendentes();
						
						// Localiza o tramite que será recebido
						for (ExMovimentacao t : p.recebimentosPendentes) {
							if (forcarTransferencia || (titularFinal == null && lotaCadastrante == null) || t.isResp(titularFinal, lotaCadastrante)) {
								mov.setExMovimentacaoRef(t);
								break;
							}
						}
						
						// Titular é a origem e deve sempre ser preenchido
						if (mov.getExMovimentacaoRef() == null && p.fIncluirCadastrante) {
							mov.setTitular(mov.mob().getTitular());
							mov.setLotaTitular(mov.mob().getLotaTitular());
						}
						
						// Cancelar trâmite pendente quando é para forçar para outro destino
						Set<ExMovimentacao> movsTramitePendente = m.calcularTramitesPendentes().tramitesPendentes;
						if (forcarTransferencia && movsTramitePendente.size() > 0) {
							for (ExMovimentacao tp : movsTramitePendente)
								gravarMovimentacaoCancelamento(mov, tp);
						} else {
							gravarMovimentacao(mov);
						}
	
						concluirAlteracaoParcialComRecalculoAcesso(m);
						
						List<ExMovimentacao> listaMovimentacao = new ArrayList<ExMovimentacao>();
						listaMovimentacao.addAll(m.doc().getMobilGeral()
								.getMovsNaoCanceladas(ExTipoDeMovimentacao.RESTRINGIR_ACESSO));
						
						if(orgaoExterno == null) {
							notificar = new ExNotificar();
							notificar.usuarioDiretamenteOuPelaUnidade(mov);
						}
						
						if (!listaMovimentacao.isEmpty()) {
							List<ExDocumento> listaDocumentos = new ArrayList<ExDocumento>();
							listaDocumentos.addAll(mob.getDoc().getExDocumentoFilhoSet());
	
							for (ExDocumento exDocumento : listaDocumentos) {
								concluirAlteracaoParcialComRecalculoAcesso(exDocumento.getMobilGeral());
							}
						}
					}
				}
			}

			if (fDespacho)
				encerrarVolumeAutomatico(cadastrante, lotaCadastrante, mob, dtMovIni);

			concluirAlteracao();

		} catch (final AplicacaoException e) {
			cancelarAlteracao();
			throw e;
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao transferir documento.", e);
		}
	}

	public void registrarAcessoReservado(DpPessoa cadastrante, DpLotacao lotaCadastrante, ExMobil mob)
			throws AplicacaoException {

		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoDeMovimentacao.REGISTRO_ACESSO_INDEVIDO, cadastrante, lotaCadastrante, mob,
					null, null, null, null, null, null);

			mov.setDescrMov(
					"Visualizado por " + cadastrante.getNomePessoa() + " (" + lotaCadastrante.getNomeLotacao() + ")");

			gravarMovimentacao(mov);
			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao registrar acesso reservado.", e);
		}
	}

	// Nato: removi: final HttpServletRequest request,
	public void anotar(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExMobil mob, final Date dtMov,
			DpLotacao lotaResponsavel, final DpPessoa responsavel, final DpPessoa subscritor, final DpPessoa titular,
			final String descrMov, String nmFuncaoSubscritor) throws AplicacaoException {
		if (descrMov == null) {
			if (responsavel == null && lotaResponsavel == null)
				if (dtMov == null)
					throw new RegraNegocioException("Não foram informados dados para a anotação");
		}
		
		if (descrMov.length() > 500) {
			throw new RegraNegocioException("Descrição com mais de 500 caracteres");
		}

		try {						
			// criarWorkflow(cadastrante, lotaCadastrante, doc, "Exoneracao");
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.ANOTACAO, cadastrante,
					lotaCadastrante, mob, dtMov, subscritor, null, titular, null, dtMov);

			mov.setNmFuncaoSubscritor(nmFuncaoSubscritor);
			mov.setDescrMov(descrMov);

			gravarMovimentacao(mov);

			atualizarDnmAnotacao(mov.getExMobil());

			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao fazer anotação.", e);
		}
	}
	
	public void vincularPapel(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, DpLotacao lotaResponsavel, final DpPessoa responsavel, final DpPessoa subscritor,
			final DpPessoa titular, final String descrMov, String nmFuncaoSubscritor, ExPapel papel)
			throws AplicacaoException {
		List<ExMovimentacao> movs = mob.getMovimentacoesPorTipo(ExTipoDeMovimentacao.VINCULACAO_PAPEL, true);
		StringBuilder msg = new StringBuilder();
		movs.forEach(m -> {
			if(papel !=null && m.getExPapel() !=null) {
				if(responsavel != null && responsavel.equals(m.getSubscritor()) && papel.getIdPapel().equals(m.getExPapel().getIdPapel())) {
					msg.append("Usuário ").append(m.getSubscritor().getNomePessoa()).append(" já foi definido");
				} else {
					if(responsavel == null && m.getSubscritor() == null && lotaResponsavel.equals(m.getLotaSubscritor()) && papel.getIdPapel().equals(m.getExPapel().getIdPapel())) {
						msg.append("Unidade ").append(m.getLotaSubscritor().getNomeLotacao()).append(" já foi definida");
					}
				}
				if (msg.length() > 0 ) {
					msg.append(" como ").append(papel.getDescPapel()).append(" no acompanhamento do documento ").append(mob.getDnmSigla());
					throw new AplicacaoException(msg.toString());
				}
			}
		});
		vincularPapel(cadastrante, lotaCadastrante, mob, dtMov, lotaResponsavel, 
				responsavel, subscritor, titular, descrMov, nmFuncaoSubscritor, papel, null);
	}

	// Nato: removi: final HttpServletRequest request,
	public void vincularPapel(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, DpLotacao lotaResponsavel, final DpPessoa responsavel, final DpPessoa subscritor,
			final DpPessoa titular, final String descrMov, String nmFuncaoSubscritor, ExPapel papel, ExMobil exMobRef)
			throws AplicacaoException {

		if (descrMov == null) {
			if (responsavel == null && lotaResponsavel == null)
				if (dtMov == null)
					throw new AplicacaoException("não foram informados dados para a vinculação de papel");
		}
		if (papel == null)
			throw new AplicacaoException("não foi informado o papel para a vinculação de papel");

		try {
			// criarWorkflow(cadastrante, lotaCadastrante, doc, "Exoneracao");
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.VINCULACAO_PAPEL,
					cadastrante, lotaCadastrante, mob, dtMov, responsavel, lotaResponsavel, titular, null, dtMov);

			mov.setNmFuncaoSubscritor(nmFuncaoSubscritor);
			mov.setDescrMov(descrMov);
			mov.setExPapel(papel);
			mov.setExMobilRef(exMobRef);
			gravarMovimentacao(mov);
			concluirAlteracaoComRecalculoAcesso(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao fazer vinculação de papel.", e);
		}

	}

	public void marcar(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, 
			final DpPessoa titular, final DpLotacao lotaTitular, 
			final ExMobil mob, final Date dtMov,  
			DpPessoa subscritor, DpLotacao lotaSubscritor,
			final String observacoes, CpMarcador marcador,
			Date dataPlanejada, Date dataLimite, boolean fConcluirAlteracao) throws Exception {

		ExPodeMarcar.afirmar(mob, titular, lotaTitular);
		
		if (marcador == null)
			throw new AplicacaoException("não foi informado o marcador");
		
		final ExMobil geral = mob.doc().getMobilGeral();
		
//		if (marcador.getDpLotacaoIni() != null) {
//			subscritor = null;
//			lotaSubscritor = marcador.getDpLotacaoIni().getLotacaoAtual();
//		}
		
		// Localiza a última movimentação de marcação de lotação, para tratar o caso do mutuamente exclusivo
		ExMovimentacao movAnterior = null;
		if (marcador.getIdFinalidade().isXor()) {
			List<ExMovimentacao> movs = mob.getMovimentacoesPorTipo(ExTipoDeMovimentacao.MARCACAO, true);
			if (!mob.isGeral())
				movs.addAll(geral.getMovimentacoesPorTipo(ExTipoDeMovimentacao.MARCACAO, true));
			for (ExMovimentacao mov : movs) {
				if (mov.getMarcador() != null && mov.getMarcador().getIdFinalidade() == marcador.getIdFinalidade()
						|| (mov.getMarcador().getIdFinalidade().getGrupo() == CpMarcadorFinalidadeGrupoEnum.PASTA
								&& marcador.getIdFinalidade().getGrupo() == CpMarcadorFinalidadeGrupoEnum.PASTA)) {
					movAnterior = mov;
					break;
				}
			}
		}

		try {
			if (fConcluirAlteracao)
				iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.MARCACAO,
					cadastrante, lotaCadastrante, 
					mob.isVia() && marcador.isAplicacaoGeralOuViaEspecificaOuUltimoVolume() 
					 ? mob : geral, 
					dtMov, null, null, titular, lotaTitular, dtMov);

			mov.setDescrMov(observacoes);
			mov.setMarcador(marcador);
			mov.setDtParam1(dataPlanejada);
			mov.setDtParam2(dataLimite);
			mov.setSubscritor(subscritor);
			mov.setLotaSubscritor(lotaSubscritor);

			if (movAnterior != null) {
				mov.setExMovimentacaoRef(movAnterior);
				gravarMovimentacaoCancelamento(mov, movAnterior);
			} else
				gravarMovimentacao(mov);
			// concluindo só com o documento para forçar o recálculo das marcas de todos os mobiles
			if (fConcluirAlteracao)
				concluirAlteracao(mov.mob().doc(), null, null, false);
		} catch (final Exception e) {
			if (fConcluirAlteracao)
				cancelarAlteracao();
			throw new RuntimeException("Erro ao fazer marcação.", e);
		}
	}

	public void redefinirNivelAcesso(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExDocumento doc,
			final Date dtMov, DpLotacao lotaResponsavel, final DpPessoa responsavel, final DpPessoa subscritor,
			final DpPessoa titular, String nmFuncaoSubscritor, ExNivelAcesso nivelAcesso) throws AplicacaoException {

		if (nivelAcesso == null) {
			throw new AplicacaoException("não foram informados dados para a redefinição do nível de acesso");
		}

		try {
			//Não previsto no requisito, mas
			//Deveria ter uma verificação aqui se o documento tem restrição de acesso para desfazer a restrição se for diferente de Limitado entre Pessoas 
			// ou impedir a redefinição
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoDeMovimentacao.REDEFINICAO_NIVEL_ACESSO, cadastrante, lotaCadastrante,
					doc.getMobilGeral(), dtMov, subscritor, null, titular, null, dtMov);

			mov.setNmFuncaoSubscritor(nmFuncaoSubscritor);
			mov.setDescrMov("Nível de acesso do documento alterado de " + doc.getExNivelAcessoAtual().getNmNivelAcesso()
					+ " para " + nivelAcesso.getNmNivelAcesso());

			mov.setExNivelAcesso(nivelAcesso);
			// A variável doc.exNivelAcesso deve conter o acesso
			// da criação do documento e não pode ser alterada
			// mais a frente. Para isso existe a dnmNivelAcesso.
			//
			// doc.setExNivelAcesso(nivelAcesso);

			gravarMovimentacao(mov);
			concluirAlteracaoComRecalculoAcesso(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao tentar redefinir nível de acesso", e);
		}
	}
	
	public void restringirAcesso(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExDocumento doc,
			final Date dtMov, DpLotacao lotaResponsavel, final DpPessoa responsavel,
			final List<DpPessoa> listaPessoasRestricaoAcesso, final DpPessoa titular, String nmFuncaoSubscritor,
			ExNivelAcesso nivelAcesso) throws AplicacaoException {

		if (nivelAcesso == null) {
			throw new AplicacaoException("não foram informados dados para a redefinição do nível de acesso");
		}

		try {
			iniciarAlteracao();

			List<ExDocumento> documentos = new ArrayList<>();
			List<ExMovimentacao> listaMov = new ArrayList<ExMovimentacao>();
			List<DpPessoa> listaPessoasSubscritoresRestricaoAcesso = new ArrayList<DpPessoa>();
			
			
			documentos.add(doc);
			documentos.addAll(doc.getExDocumentoFilhoSet());
			for (ExDocumento exDocumento : documentos) {
				
				listaPessoasSubscritoresRestricaoAcesso.clear();
				listaPessoasSubscritoresRestricaoAcesso.addAll(listaPessoasRestricaoAcesso);
				
				//mantém Subscritores e Cossignatários caso esteja pendente de assinatura
				if (exDocumento.isPendenteDeAssinatura()) {
					listaPessoasSubscritoresRestricaoAcesso.addAll(exDocumento.getSubscritorECosignatarios());
				}
				
				boolean pessoaEstaRestricaoAcesso = false;
				for (DpPessoa subscritor : listaPessoasSubscritoresRestricaoAcesso) {		
					
					pessoaEstaRestricaoAcesso = doc.getMobilGeral().getSubscritoresMovimentacoesPorTipo(ExTipoDeMovimentacao.RESTRINGIR_ACESSO, true)
													.stream()
													.anyMatch(pessoaRestrita -> subscritor.equivale(pessoaRestrita));
					
					
					//Se já estiver na lista, não adiciona
					if (!pessoaEstaRestricaoAcesso) {
						final ExMovimentacao mov = criarNovaMovimentacao(
								ExTipoDeMovimentacao.RESTRINGIR_ACESSO, cadastrante, lotaCadastrante,
								exDocumento.getMobilGeral(), dtMov, subscritor, null, titular, null, dtMov);
	
						mov.setNmFuncaoSubscritor(nmFuncaoSubscritor);
						mov.setDescrMov("Nível de acesso do documento alterado de "
								+ exDocumento.getExNivelAcessoAtual().getNmNivelAcesso() + " para "
								+ nivelAcesso.getNmNivelAcesso() + ". Restrição de acesso adicionada para "
								+ subscritor.getDescricaoIniciaisMaiusculas());
	
						mov.setExNivelAcesso(nivelAcesso);
						
						if (isMovimentacaoComOrigemPeloBotaoDeRestricaoDeAcesso()) {
							listaMov.add(mov);
							mov.getExMobil().doc().setListaMovimentacaoPorRestricaoAcesso(listaMov);
						}
							
						gravarMovimentacao(mov);
						concluirAlteracaoComRecalculoAcesso(mov);
					}
				}
			}

		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao tentar redefinir nível de acesso", e);
		}
	}

	public void desfazerRestringirAcesso(final DpPessoa cadastrante, final DpLotacao lotaCadastrante,
			final ExDocumento doc, final Date dtMov) throws AplicacaoException {
		try {
			iniciarAlteracao();
			List<ExMovimentacao> listaMov = new ArrayList<ExMovimentacao>();
			List<ExDocumento> documentos = new ArrayList<>();
			
			ExNivelAcesso nivelAcesso = null;
			documentos.add(doc);
			documentos.addAll(doc.getExDocumentoFilhoSet());
			ExMovimentacao movPrincipal = null;
			for (ExDocumento exDocumento : documentos) {
				listaMov = new ArrayList<>();
				listaMov.addAll(exDocumento.getMobilGeral()
						.getMovsNaoCanceladas(ExTipoDeMovimentacao.RESTRINGIR_ACESSO));
				for (ExMovimentacao movimentacaoRestricao : listaMov) {
					final ExMovimentacao movimentacaoCanceladora = criarNovaMovimentacao(
							ExTipoDeMovimentacao.CANCELAMENTO_DE_MOVIMENTACAO, cadastrante,
							lotaCadastrante, exDocumento.getMobilGeral(), dtMov, movimentacaoRestricao.getSubscritor(), null, null,
							null, dtMov);
					
					//Ao desfazer, set na Movimentacao o Nível de Acesso Anterior ao da movimentação em questão
					//Nível retorna até o nível definido na inclusão do documento
					nivelAcesso = doc.getExNivelAcessoAnterior();

					movimentacaoCanceladora.setDescrMov("Nível de acesso do documento alterado de "
							+ exDocumento.getExNivelAcessoAtual().getNmNivelAcesso() + " para "
							+ nivelAcesso.getNmNivelAcesso() + ". Restrição de acesso retirada para "
							+ movimentacaoRestricao.getSubscritor().getDescricaoIniciaisMaiusculas());

					movimentacaoRestricao.setExMovimentacaoCanceladora(movimentacaoCanceladora);
					movimentacaoCanceladora.setExNivelAcesso(nivelAcesso);

					gravarMovimentacao(movimentacaoCanceladora);
					gravarMovimentacao(movimentacaoRestricao);
					
					if (doc == exDocumento)
						movPrincipal = movimentacaoCanceladora;

				}

				concluirAlteracaoComRecalculoAcesso(movPrincipal);
			}
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao tentar desfazer restrigir acesso", e);
		}
	}
	public void exigirAnexo(final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob,
			final String descrMov) throws AplicacaoException {

		if (mob.doc().getIdDoc() == null)
			return;

		if (descrMov == null) {
			throw new AplicacaoException("não foram informados dados para a exigência de anexação");
		}

		boolean fGravarMov = false;
		try {
			String as[] = descrMov.split(";");
			for (String s : as) {
				if (mob.anexoPendente(s, true) != null)
					continue;

				if (!fGravarMov) {
					iniciarAlteracao();
					fGravarMov = true;
				}

				final ExMovimentacao mov = criarNovaMovimentacao(
						ExTipoDeMovimentacao.PENDENCIA_DE_ANEXACAO, cadastrante, lotaCadastrante, mob,
						null, null, null, null, null, null);
				mov.setDescrMov(s);
				gravarMovimentacao(mov);
			}
			if (fGravarMov)
				concluirAlteracao(mob);
		} catch (final Exception e) {
			if (fGravarMov)
				cancelarAlteracao();
			throw new RuntimeException("Erro ao exigir anexação.", e);
		}
	}

	/* Daqui para frente não são movimentacoes, mas sim funcoes auxiliares */

	public String canonicalizarHtml(String s, boolean fRemoverEspacos, boolean fRemoverTagsDesconhecidos,
			boolean fIso8859, boolean fBodyOnly) {
		String sRet;
		try {
			sRet = (new ProcessadorHtml().canonicalizarHtml(s, fRemoverEspacos, fRemoverTagsDesconhecidos, fIso8859,
					fBodyOnly, false));
			return sRet;
		} catch (Exception e) {
			return s;
		}
	}

	public void processar(final ExDocumento doc, final boolean gravar, final boolean transacao) {
		// Não existe processamento de modelo para documento capturado
		if (doc.isCapturado() && doc.getExModelo().getExtensoesArquivo() == null)
			return;

		try {
			if (doc != null && (!doc.isPendenteDeAssinatura() || doc.isAssinadoDigitalmente()))
				throw new AplicacaoException("O documento não pode ser reprocessado, pois já está assinado");

			if ((doc.getExModelo() != null && ("template/freemarker".equals(doc
					.getExModelo().getConteudoTpBlob()) || doc.getExModelo()
					.getNmArqMod() != null))
					|| doc.getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_FOLHA_DE_ROSTO) {
				if (doc.getConteudoBlobForm() != null) {
					if (!Utils.empty(doc.getNumeroSequenciaGenerica())) {
						Map<String, String> form = new TreeMap<String, String>();
						//Get Form atual
						Utils.mapFromUrlEncodedForm(form, doc.getConteudoBlobForm());
						//gera e adiciona a entrevista o número da sequencia generica
						form.put("numeroSequenciaGenerica", doc.getNumeroSequenciaGenerica());
						form.put("codigoUnico", doc.getCodigoUnico());
						form.put("digitoVerificadorCodigoUnico", doc.getDigitoVerificadorCodigoUnico());
						//Atualiza Form 
						doc.setConteudoBlobForm(urlEncodedFormFromMap(form));
						//Reprocessa Descrição para adição de sequencia se implementado no modelo
						gravaDescrDocumento(doc.getTitular(), doc.getLotaTitular(), doc);
					}
					
				}
				if (gravar && transacao) {
					iniciarAlteracao();
				}

				// Internos antigos devem usar sempre o modelo 778L
				Long backupID = null;
				if (doc.getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_FOLHA_DE_ROSTO) {
					if (doc.getExModelo() != null)
						backupID = doc.getExModelo().getIdMod();

					Long idMod;
					if (doc.isProcesso()) {
						ExModelo modPA = dao().consultarExModelo(null,
								MODELO_FOLHA_DE_ROSTO_PROCESSO_ADMINISTRATIVO_INTERNO);
						idMod = modPA != null ? modPA.getId() : Prop.getInt("modelo.processo.administrativo");
					} else {
						ExModelo modInterno = dao().consultarExModelo(null, MODELO_FOLHA_DE_ROSTO_EXPEDIENTE_INTERNO);
						idMod = modInterno != null ? modInterno.getId() : Prop.getInt("modelo.interno.importado");
					}
					doc.setExModelo(dao().consultar(idMod, ExModelo.class, false));
				}

				final String strHtml;
				try {
					strHtml = processarModelo(doc, null, "processar_modelo", null, null);
				} catch (Exception e) {
					throw new RuntimeException("Erro no processamento do modelo HTML.", e);
				}

				// Restaurar o modelo do "Interno Antigo"
				if (doc.getExTipoDocumento().getIdTpDoc() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_FOLHA_DE_ROSTO) {
					if (backupID != null) {
						doc.setExModelo(dao().consultar(backupID, ExModelo.class, false));
					} else {
						doc.setExModelo(null);
					}
				}

				doc.setConteudoBlobHtmlString(strHtml);

				if (strHtml != null && strHtml.trim().length() > 0) {
					final byte pdf[];
					AbstractConversorHTMLFactory fabricaConvHtml = AbstractConversorHTMLFactory.getInstance();
					ConversorHtml conversor = fabricaConvHtml.getConversor(getConf(), doc, strHtml);

					try {
						pdf = Documento.generatePdf(strHtml, conversor);
					} catch (Exception e) {
						throw new RuntimeException(
								"Erro na geração do PDF. Por favor, verifique se existem recursos de formatação não suportados. Para eliminar toda a formatação do texto clique em voltar e depois, no editor, clique no botõo de 'Selecionar Tudo' e depois no botão de 'Remover Formatação'.",
								e);
					}
					doc.setConteudoBlobPdf(pdf);
				}

				if (gravar) {
					doc.setNumPaginas(doc.getContarNumeroDePaginas());
					dao().gravar(doc);
					if (transacao) {
						concluirAlteracao(doc, null, null, false);
					}
				}
			}
		} catch (final AplicacaoException e) {
			if (gravar && transacao) {
				cancelarAlteracao();
			}
			throw e;
		} catch (final Exception e) {
			if (gravar && transacao) {
				cancelarAlteracao();
			}
			throw new RuntimeException("Erro na gravação", e);
		}
	}

	public String processarModelo(final ExDocumento doc, String acao, Map<String, String> formParams,
			CpOrgaoUsuario orgaoUsu) throws Exception {
		return processarModelo(doc, null, acao, formParams, orgaoUsu);
	}

	public String processarModelo(final ExMovimentacao mov, final String acao, Map<String, String> formParams,
			CpOrgaoUsuario orgaoUsu) throws Exception {
		return processarModelo(mov.getExDocumento(), mov, acao, formParams, orgaoUsu);
	}

	private String processarModelo(ExDocumento doc, ExMovimentacao mov, String acao, Map<String, String> formParams,
			CpOrgaoUsuario orgaoUsu) throws Exception {
		Map<String, Object> attrs = new TreeMap<String, Object>();
		Map<String, Object> params = new TreeMap<String, Object>();
		ProcessadorModelo p = getProcessadorModeloJsp();

		// System.out.println(System.currentTimeMillis() + " - INI
		// processarModelo");

		// Parsear o registro de dados da entrevista que esta urlencoded
		// e gravar cada um dos valores no mapa de parametros
		if (formParams != null) {
			attrs.putAll(formParams);
		} else {
			if (doc.getExModelo() != null) {
				final byte[] form;
				if (mov == null)
					form = doc.getConteudoBlobForm();
				else
					form = mov.getConteudoBlobForm();
				Utils.mapFromUrlEncodedForm(attrs, form);
			}
		}
		if (acao != null)
			attrs.put(acao, "1");
		attrs.put("doc", doc);
		if (mov != null && mov.getExMobil() != null)
			attrs.put("ref", new ExRef(doc, mov.getExMobil()));
		else
			attrs.put("ref", doc.getRef());
		
		// Incluir um atributo chamado "wf" que contém os dados do procedimento vinculado
		String principal = null;
		if (doc.getTipoDePrincipal() != null && doc.getTipoDePrincipal() == ExTipoDePrincipal.PROCEDIMENTO && doc.getPrincipal() != null) 
			principal = doc.getPrincipal();
		else if (doc.getExMobilPai() != null && doc.getExMobilPai().doc().getTipoDePrincipal() != null && doc.getExMobilPai().doc().getTipoDePrincipal() == ExTipoDePrincipal.PROCEDIMENTO && doc.getExMobilPai().doc().getPrincipal() != null) 
			principal = doc.getExMobilPai().doc().getPrincipal();
		else if (doc.getExMobilAutuado() != null && doc.getExMobilAutuado().doc().getTipoDePrincipal() != null && doc.getExMobilAutuado().doc().getTipoDePrincipal() == ExTipoDePrincipal.PROCEDIMENTO && doc.getExMobilAutuado().doc().getPrincipal() != null) 
			principal = doc.getExMobilAutuado().doc().getPrincipal();
		if (principal != null) {
			WfProcedimentoWSTO wf = Service.getWfService().consultarProcedimento(principal);
			Map<String, Object> vars = wf.getVar();
			
			// Converter boolean em Sim/Não
			if (vars != null) 
				for (String key : vars.keySet()) {
					Object val = vars.get(key);
					if (val instanceof Boolean)
						vars.put(key, ((Boolean)val) ? "Sim" : "Não");
				}
			
			attrs.put("wf", wf);
		}
		
		// rw.setAttribute("modelo", doc.getExModelo());
		if (mov == null) {
			if (doc.getExModelo() != null) {
				attrs.put("nmArqMod", doc.getExModelo().getNmArqMod());
				if ("template/freemarker".equals(doc.getExModelo().getConteudoTpBlob())) {
					attrs.put("nmMod", doc.getExModelo().getNmMod());
					byte[] fm = doc.getExModelo().getConteudoBlobMod2();
					if (fm != null)
						attrs.put("template", new String(fm, "utf-8"));
					p = processadorModeloFreemarker;
				}
			}
		} else {
			attrs.put("mov", mov);
			attrs.put("mob", mov.getExMobil());
			if (mov.getExTipoMovimentacao() != null && mov.getExTipoMovimentacao()
					 == ExTipoDeMovimentacao.ENCERRAMENTO_DE_VOLUME) {
				if (SigaMessages.isSigaSP()) {
					attrs.put("nmArqMod", "certidaoEncerramentoVolumeGOVSP.jsp");
				} else {
					attrs.put("nmArqMod", "certidaoEncerramentoVolume.jsp");
				}
				
			} else if (mov.getExTipoMovimentacao() != null
					&& (mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA
							|| (mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.CANCELAMENTO_DE_MOVIMENTACAO)
								&& mov.getExMovimentacaoRef() != null && ExTipoDeMovimentacao.hasDocumento(mov.getExMovimentacaoRef().getExTipoMovimentacao()))) {
				if (SigaMessages.isSigaSP()) {
					attrs.put("nmArqMod", "certidaoDesentranhamentoGOVSP.jsp");
				} else {
					attrs.put("nmArqMod", "certidaoDesentranhamento.jsp");
				}

			} else if (mov.getExTipoMovimentacao() != null
					&& (mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.CIENCIA)) {
				attrs.put("nmArqMod", "ciencia.jsp");
			} else {
				if (mov.getExTipoDespacho() != null) {
					attrs.put("despachoTexto", mov.getExTipoDespacho().getDescTpDespacho());
				} else if (mov.getDescrMov() != null) {
					attrs.put("despachoTexto", mov.getDescrMov());
				} else if (attrs.get("conteudo") != null) {
					attrs.put("despachoTexto", attrs.get("conteudo"));
				}
				if (mov.getConteudoBlobHtmlString() != null) {
					attrs.put("despachoHtml", mov.getConteudoBlobHtmlString());
				}
				// attrs.put("nmArqMod", "despacho_mov.jsp");
				ExModelo m = dao().consultarExModelo(null, MODELO_DESPACHO_AUTOMATICO);
				attrs.put("nmMod", m.getNmMod());
				attrs.put("template", new String(m.getConteudoBlobMod2(), "utf-8"));

				p = processadorModeloFreemarker;
			}
		}

		// Nato: alterei essas linhas para que os modelos possam conhecer o
		// cadastrante e o titular
		// attrs.put("lotaTitular", doc.getLotaTitular());
		attrs.put("cadastrante", doc.getCadastrante());
		attrs.put("lotaCadastrante", doc.getLotaCadastrante());
		attrs.put("titular", doc.getTitular());
		attrs.put("lotaTitular", doc.getLotaTitular());

		attrs.put("urlbase", Prop.get("/siga.base.url"));

		params.put("processar_modelo", "1");
		params.put("finalizacao", "1");
		if (doc != null && doc.getIdDoc() != null)
			params.put("idDoc", doc.getIdDoc().toString());
		if (mov != null && mov.getIdMov() != null) {
			params.put("id", mov.getIdMov().toString());
		}
		CpOrgaoUsuario ou = orgaoUsu;
		if (doc != null)
			ou = doc.getOrgaoUsuario();
		if (mov != null && mov.getResp() != null && mov.getResp().getOrgaoUsuario() != null)
			ou = mov.getResp().getOrgaoUsuario();
		String s = p.processarModelo(ou, attrs, params);

		// System.out.println(System.currentTimeMillis() + " - FIM
		// processarModelo");
		return s;
	}

	private void juntarAoDocumentoPai(final DpPessoa cadastrante, final DpLotacao lotaCadastrante,
			final ExDocumento doc, final Date dtMov, final DpPessoa subscritor, final DpPessoa titular,
			final ExMovimentacao mov) throws Exception, AplicacaoException {

		// for (int numVia = 1; numVia <= doc.getNumUltimaViaNaoCancelada();
		// numVia++)
		for (final ExMobil mob : doc.getExMobilSet()) {

			if (getComp().pode(ExPodeJuntar.class, titular, lotaCadastrante, mob) && getComp()
					.pode(ExPodeSerJuntado.class, titular, lotaCadastrante, mob.doc(), doc.getExMobilPai())) {
				juntarDocumento(cadastrante, titular, lotaCadastrante, null, mob,
						doc.getExMobilPai(), dtMov, null, titular, "1");
				break;
			}
		}
	}

	private void juntarAoDocumentoAutuado(final DpPessoa cadastrante, final DpLotacao lotaCadastrante,
			final ExDocumento doc, final Date dtMov, final DpPessoa titular) throws Exception, AplicacaoException {

		// for (int numVia = 1; numVia <= doc.getNumUltimaViaNaoCancelada();
		// numVia++)
		for (final ExMobil mob : doc.getExMobilSet()) {
			if (getComp().pode(ExPodeJuntar.class, titular, lotaCadastrante, mob.doc(), doc.getExMobilAutuado())
					& getComp().pode(ExPodeSerJuntado.class, titular, lotaCadastrante, doc.getExMobilAutuado().doc(), mob)) {
				juntarDocumento(cadastrante, titular, lotaCadastrante, null,
						doc.getExMobilAutuado(), mob, dtMov, null, titular, "1");
				break;
			}
		}
	}

	private ExMovimentacao criarNovaMovimentacao(final ITipoDeMovimentacao tpmov, final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob, final Date dtMov, final DpPessoa subscritor,
			final DpLotacao lotaSubscritor, final DpPessoa titular, final DpLotacao lotaTitular, final Date dtOrNull)
			throws AplicacaoException {
		final ExMovimentacao mov;
		mov = new ExMovimentacao();

		final Date dt = dtOrNull == null ? dao().dt() : dtOrNull;

		mov.setCadastrante(cadastrante);
		mov.setLotaCadastrante(lotaCadastrante);
		if (lotaCadastrante == null && mov.getCadastrante() != null)
			mov.setLotaCadastrante(mov.getCadastrante().getLotacao());
		if (subscritor != null) {
			mov.setSubscritor(subscritor);
			mov.setLotaSubscritor(mov.getSubscritor().getLotacao());
		} else {
			if (tpmov == ExTipoDeMovimentacao.VINCULACAO_PAPEL) {
				mov.setSubscritor(null); /* o perfil(responsível) é uma lotação */
				mov.setLotaSubscritor(lotaSubscritor);
			} else {
				mov.setSubscritor(cadastrante);
				mov.setLotaSubscritor(mov.getLotaCadastrante());
			}
		}

		if (titular != null)
			mov.setTitular(titular);
		else
			mov.setTitular(subscritor);
		if (lotaTitular != null)
			mov.setLotaTitular(lotaTitular);
		else
			mov.setLotaTitular(mov.getLotaSubscritor());

		if (dtMov != null)
			mov.setDtMov(dtMov);
		else
			mov.setDtMov(dt);

		// A data de início da movimentação sempre será a data do servidor, não
		// a data que o usuário digitou
		mov.setDtIniMov(dao().dt());
		mov.setExMobil(mob);
		mov.setExTipoMovimentacao(tpmov);

		final ExMovimentacao ultMov = mob.getUltimaMovimentacao();
		
		if (ultMov != null) {
			// if (mov.getExMobilPai() == null)
			// mov.setExMobilPai(ultMov.getExMobilPai());
			if (mov.getExClassificacao() == null)
				mov.setExClassificacao(ultMov.getExClassificacao());
			if (mov.getLotaResp() == null)
				mov.setLotaResp(ultMov.getLotaResp());
			if (mov.getResp() == null)
				mov.setResp(ultMov.getResp());
			if (mov.getLotaDestinoFinal() == null)
				mov.setLotaDestinoFinal(ultMov.getLotaDestinoFinal());
			if (mov.getDestinoFinal() == null)
				mov.setDestinoFinal(ultMov.getDestinoFinal());
		}
		if (ultMov == null || tpmov == ExTipoDeMovimentacao.RECEBIMENTO) {
			mov.setLotaResp(lotaCadastrante);
			mov.setResp(cadastrante);
		}
		acrescentarCamposDeAuditoria(mov);
		return mov;
	}

	public ExMovimentacao gravarNovaMovimentacao(final ITipoDeMovimentacao tpmov,
									   final DpPessoa cadastrante, final DpLotacao lotaCadastrante,
									   final ExMobil mob, final Date dtMov, final DpPessoa subscritor,
									   final DpLotacao lotaSubscritor, final DpPessoa titular,
									   final DpLotacao lotaTitular, final Date dtOrNull,
									   final String descrMov) throws AplicacaoException {

		try {
			final ExMovimentacao mov = criarNovaMovimentacao(tpmov, cadastrante, lotaCadastrante,
					mob, dtMov, subscritor, lotaSubscritor, titular, lotaTitular, dtOrNull);

			mov.setDescrMov(descrMov);
			gravarMovimentacao(mov);
			
			return mov;
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao gravar " + descrMov, tpmov.getId(), e);
		}

	}

	private void acrescentarCamposDeAuditoria(ExMovimentacao mov) {
		String principal = ContextoPersistencia.getUserPrincipal();
		if (principal != null) {
			CpIdentidade identidade = dao().consultaIdentidadeCadastrante(principal, true);
			mov.setAuditIdentidade(identidade);
		}
		RequestInfo ri = CurrentRequest.get();
		if (ri != null) {
			mov.setAuditIP(HttpRequestUtils.getIpAudit(ri.getRequest()));
		}
	}

	public void registrarCiencia(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, 
			final DpPessoa titular, final DpLotacao lotaTitular, final ExMobil mob,
			final Date dtMov, DpLotacao lotaResponsavel, final DpPessoa responsavel, final DpPessoa subscritor,
			final String descrMov) throws AplicacaoException {
		
		if(!SigaMessages.isSigaSP()) {
			throw new RegraNegocioException("Não é possível fazer ciência do documento neste ambiente.");
		}

		if (!Ex.getInstance().getComp().pode(ExPodeFazerCiencia.class, responsavel, lotaResponsavel, mob)) {
			throw new RegraNegocioException("Não é possível fazer ciência do documento."			
					+ " Isso pode ocorrer se o documento não estiver apto a receber ciência ou devido a alguma regra para não permitir esta operação");
		}

		try {
			iniciarAlteracao();
			final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.CIENCIA, cadastrante,
					lotaCadastrante, mob, dtMov, cadastrante, null, titular, lotaTitular, null);

			mov.setDescrMov(descrMov);

			gravarMovimentacao(mov);
			
			mov.setNumPaginas(1);
			
			Map<String, String> form = new TreeMap<String, String>();
			form.put("textoMotivo", descrMov);
			mov.setConteudoBlobForm(urlEncodedFormFromMap(form));

			// Gravar o Html
			final String strHtml = processarModelo(mov, "processar_modelo", null, null);
			mov.setConteudoBlobHtmlString(strHtml);

			// Gravar o Pdf
			final byte pdf[] = Documento.generatePdf(strHtml);
			mov.setConteudoBlobPdf(pdf);
			mov.setConteudoTpMov("application/zip");
			
			final ExMovimentacao movAssMov = criarNovaMovimentacao(ExTipoDeMovimentacao.ASSINATURA_MOVIMENTACAO_COM_SENHA, cadastrante, lotaCadastrante,
					mov.getExMobil(), null, null, null, null, null, null);

			movAssMov.setDescrMov(cadastrante.getDescricao());

			movAssMov.setExMovimentacaoRef(mov);
			
			gravarMovimentacao(movAssMov);
			
			concluirAlteracao(mov);
			
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao fazer ciência.", e);
		}
	}

	private final int HASH_TIMEOUT_MILLISECONDS = 5000;

	private static class TimestampPostRequest implements ISwaggerRequest {
		String system;
		byte[] sha256;
		String tipo;
		String nome;
		String cpf;
		String json;
	}

	private static class TimestampPostResponse implements ISwaggerResponse {
		String jwt;
		String id;
		Date time;
		String url;
		String host;
	}

	private void acrescentarHashDeAuditoria(ExMovimentacao mov, byte[] sha256, boolean autenticar, String nome,
			String cpf, String json) {
		try {
			String timestampUrl = Prop.get("carimbo.url");
			log.warn("URL_TIMESTAMP " + timestampUrl);
			if (timestampUrl == null)
				return;
			TimestampPostRequest req = new TimestampPostRequest();
			req.system = Prop.get("carimbo.sistema");
			req.sha256 = sha256;
			req.tipo = autenticar ? "auth" : "sign";
			req.nome = nome;
			req.cpf = cpf;
			req.json = json;
			SwaggerAsyncResponse<TimestampPostResponse> resp = SwaggerCall.callAsync("obter timestamp", null, "POST",
					timestampUrl + "/timestamp", req, TimestampPostResponse.class)
					.get(HASH_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS);
			if (resp != null && resp.getException() != null)
				throw new RuntimeException("Exceção obtendo carimbo de tempo para a assinatura com senha",
						resp.getException());
			if (resp == null || resp.getResp() == null || resp.getResp() == null)
				throw new RuntimeException("Carimbo de tempo para a assinatura com senha indisponível");
			mov.setAuditHash(resp.getResp().jwt);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException("Erro obtendo o carimbo de tempo para a assinatura com senha", e);
		}
	}

	private ExMovimentacao criarNovaMovimentacaoTransferencia(final ITipoDeMovimentacao tpmov, final DpPessoa cadastrante,
			final DpLotacao lotaCadastrante, final ExMobil mob, final Date dtMov, final Date dtFimMov,
			final DpPessoa subscritor, final DpLotacao lotaSubscritor, final DpPessoa titular,
			final DpLotacao lotaTitular, final Date dtOrNull) throws AplicacaoException {
		final ExMovimentacao mov;
		mov = new ExMovimentacao();

		final Date dt = dtOrNull == null ? dao().dt() : dtOrNull;

		mov.setCadastrante(cadastrante);
		mov.setLotaCadastrante(lotaCadastrante);
		if (lotaCadastrante == null && mov.getCadastrante() != null)
			mov.setLotaCadastrante(mov.getCadastrante().getLotacao());
		if (subscritor != null) {
			mov.setSubscritor(subscritor);
			mov.setLotaSubscritor(mov.getSubscritor().getLotacao());
		} else {
			if (tpmov == ExTipoDeMovimentacao.VINCULACAO_PAPEL) {
				mov.setSubscritor(null); /* o perfil(responsível) é uma lotação */
				mov.setLotaSubscritor(lotaSubscritor);
			} else {
				mov.setSubscritor(cadastrante);
				mov.setLotaSubscritor(mov.getLotaCadastrante());
			}
		}

		if (titular != null)
			mov.setTitular(titular);
		else
			mov.setTitular(subscritor);
		if (lotaTitular != null)
			mov.setLotaTitular(lotaTitular);
		else
			mov.setLotaTitular(mov.getLotaSubscritor());

		if (dtMov != null)
			mov.setDtMov(dtMov);
		else
			mov.setDtMov(dt);

		if (dtFimMov != null)
			mov.setDtFimMov(dtFimMov);
		else
			mov.setDtFimMov(dtFimMov);

		// A data de início da movimentação sempre será a data do servidor, não
		// a data que o usuário digitou
		mov.setDtIniMov(dao().dt());
		mov.setExMobil(mob);
		mov.setExTipoMovimentacao(tpmov);

		final ExMovimentacao ultMov = mob.getUltimaMovimentacao();
		if (ultMov != null) {
			// if (mov.getExMobilPai() == null)
			// mov.setExMobilPai(ultMov.getExMobilPai());
			if (mov.getExClassificacao() == null)
				mov.setExClassificacao(ultMov.getExClassificacao());
			if (mov.getLotaResp() == null)
				mov.setLotaResp(ultMov.getLotaResp());
			if (mov.getResp() == null)
				mov.setResp(ultMov.getResp());
			if (mov.getLotaDestinoFinal() == null)
				mov.setLotaDestinoFinal(ultMov.getLotaDestinoFinal());
			if (mov.getDestinoFinal() == null)
				mov.setDestinoFinal(ultMov.getDestinoFinal());
		}
		if (ultMov == null || tpmov == ExTipoDeMovimentacao.RECEBIMENTO) {
			if (mov.getLotaResp() == null)
				mov.setLotaResp(lotaCadastrante);
			if (mov.getResp() == null)
				mov.setResp(cadastrante);
		}
		acrescentarCamposDeAuditoria(mov);
		return mov;
	}

	private void iniciarAlteracao() throws AplicacaoException {
		ExDao.iniciarTransacao();
	}

	private void concluirAlteracaoParcial(ExMobil mob) {
		concluirAlteracaoParcial(mob, false);
	}

	private void concluirAlteracaoParcialComRecalculoAcesso(ExMobil mob) {
		concluirAlteracaoParcial(mob, true);
	}

	private void concluirAlteracaoParcial(ExMobil mob, boolean recalcularAcesso) {
		concluirAlteracaoParcial(mob, recalcularAcesso, null, null);
	}
	private void concluirAlteracaoParcial(ExMobil mob, boolean recalcularAcesso, 
			Object incluirAcesso, Object excluirAcesso) {
		Set<String> set = docsParaAtualizacaoDeWorkflow.get();
		if (set == null) {
			docsParaAtualizacaoDeWorkflow.set(new HashSet<>());
			set = docsParaAtualizacaoDeWorkflow.get();
		}
		if (mob != null && mob.doc() != null) {
			if (recalcularAcesso)
				atualizarVariaveisDenormalizadas(mob.doc(), incluirAcesso, excluirAcesso);
			if (mob.isGeral())
				atualizarMarcas(mob.doc());
			else {
				atualizarMarcas(mob);
				if(mob.isVolume()) {
					for(ExMobil m : mob.getDoc().getExMobilSet()) {
						if(!m.isGeralDeProcesso() && !mob.equals(m)) {
							atualizarMarcas(m);
						}			 
					}	
				}
			}
		}
		set.add(mob.doc().getCodigo());
	}

	private void atualizarVariaveisDenormalizadas(ExDocumento doc) {
		atualizarVariaveisDenormalizadas(doc, null, null);
	}

	private void atualizarVariaveisDenormalizadas(ExDocumento doc, Object incluirAcesso, Object excluirAcesso) {
		atualizarDnmNivelAcesso(doc);
		atualizarDnmAcesso(doc, incluirAcesso, excluirAcesso);
	}

	private void concluirAlteracao() throws Exception {
		concluirAlteracao(null, null, null, false);
	}
		
	private void concluirAlteracao(ExMovimentacao mov) throws Exception {
		concluirAlteracao(mov.getExDocumento(), mov.getExMobil(), mov, false);
	}
	
	private void concluirAlteracao(ExMobil mob) throws Exception {
		concluirAlteracao(null, mob, null, false);
	}

	private void concluirAlteracaoDoc(ExMovimentacao mov) throws Exception {
		concluirAlteracao(mov.getExDocumento(), null, null, false);
	}
	
	private void concluirAlteracaoComRecalculoAcesso(ExMovimentacao mov) throws Exception {
		concluirAlteracao(null, mov.getExMobil(), mov, true);
	}

	private void concluirAlteracaoDocComRecalculoAcesso(ExDocumento doc) throws Exception {
		concluirAlteracao(doc, null, null, true);
	}
	
	private void concluirAlteracaoDocComRecalculoAcesso(ExMovimentacao mov) throws Exception {
		concluirAlteracao(mov.getExDocumento(), null, null, true);
 	}

	private void concluirAlteracao(ExDocumento doc, ExMobil mob, ExMovimentacao mov, boolean recalcularAcesso) throws Exception {
		if (mob != null) {
			if (recalcularAcesso)
				atualizarVariaveisDenormalizadas(mob.doc(), null, null);
			if (mob.isGeral())
				atualizarMarcas(mob.doc());
			else
				atualizarMarcas(mob);
		} else if (doc != null) {
			if (recalcularAcesso)
				atualizarVariaveisDenormalizadas(doc, null, null);
			atualizarMarcas(doc);
		}
		atualizarWorkflow(doc, mob, mov);
		atualizarXjus(doc, mob, mov);
 	}

	private void atualizarWorkflow(ExDocumento doc, ExMobil mob, ExMovimentacao mov) {
		Set<String> set = null;
		if (mov != null) {
			set = atualizarWorkFlowAdicionarCodigoDeDocumento(mov.mob().doc().getCodigo());
			if (mov.getExMobilRef() != null)
				set = atualizarWorkFlowAdicionarCodigoDeDocumento(mov.getExMobilRef().doc().getCodigo());
		} else if (mob != null) 
			set = atualizarWorkFlowAdicionarCodigoDeDocumento(mob.doc().getCodigo());
		else if (doc != null)
			set = atualizarWorkFlowAdicionarCodigoDeDocumento(doc.getCodigo());
		
		// Nato: criei uma threadLocal para suprimir a atualização do WF. Isso é especialmente
		// necessário para métodos que realizam várias operações, como por exemplo a assinatura,
		// que faz a finalização, a assinatura, o trâmite, etc. Nesses casos, só queremos que o
		// WF seja sinalizado no final.
		if (suprimirAtualizacaoDeWorkflow.get() != null && suprimirAtualizacaoDeWorkflow.get())
			return;
		
		// Nato: meio confuso esse código de commitar a transação e depois atualizar o workflow, mas
		// quis manter assim mesmo para não correr o risco de mudar alguma lógica e provocar algum erro
		// inesperado.
		if (set != null && Prop.getBool("/sigawf.ativo") && (ContextoPersistencia.getUsuarioDeSistema() == null || ContextoPersistencia.getUsuarioDeSistema() != UsuarioDeSistemaEnum.SIGA_WF)) {
			ContextoPersistencia.flushTransaction();
 			for (String d : set) {
 				try {
					Service.getWfService().atualizarWorkflowsDeDocumento(d);
 				} catch (Exception ex) {
 					throw new RuntimeException("Erro ao tentar atualizar estado do workflow: " + ex.getLocalizedMessage(), ex);
 				}
 			}
		}
		if (set != null)
			set.clear();
		docsParaAtualizacaoDeWorkflow.remove();
	}
	
   private void atualizarXjus(ExDocumento doc, ExMobil mob, ExMovimentacao mov) {
        if (mov != null) {
            if (mov.getCpArquivo() != null) {
                atualizarXjus(ExXjusRecordServiceEnum.formatIdAndService(mov.getIdMov(), ExXjusRecordServiceEnum.MOV));
            }
            doc = mob.doc();
        } else if (mob != null)
            doc = mob.doc();
        
        if (doc != null) {
            atualizarXjus(ExXjusRecordServiceEnum.formatIdAndService(doc.getIdDoc(), ExXjusRecordServiceEnum.DOC));
        }
   }

   private void atualizarXjus(final String id) {
       if (!Prop.getBool("/xjus.reindex"))
           return;
       ContextoPersistencia.addAfterCommit(new AfterCommit() {
           @Override
           public void run() {
               final SigaHTTP http = new SigaHTTP();
               String url = Prop.get("/xjus.url");
               if (url.endsWith("/query"))
                   url = url.substring(0, url.length() - 5);
               url += "record/" + id + "/reindex";
               HashMap<String, String> headers = new HashMap<>();
               try {
                String response = IOUtils.toString(http.fetch(url, headers, 60000, null, "POST"), "UTF-8");
                } catch (Exception e) {
                    log.info("Erro reindexando " + id + " no X-Jus");
                }
           }
       });
   }

	private void cancelarAlteracao() throws AplicacaoException {
		ExDao.rollbackTransacao();
		Set<String> set = docsParaAtualizacaoDeWorkflow.get();
		if (set != null) {
			set.clear();
			docsParaAtualizacaoDeWorkflow.remove();
		}
	}

	public Set<String> atualizarWorkFlowAdicionarCodigoDeDocumento(String codigoDoDocumento) throws AplicacaoException {
		Set<String> set = docsParaAtualizacaoDeWorkflow.get();
		if (set == null) {
			docsParaAtualizacaoDeWorkflow.set(new HashSet<>());
			set = docsParaAtualizacaoDeWorkflow.get();
		}
		set.add(codigoDoDocumento);
		return set;
	}

	/**
	 * Obtem a lista de formas de documentos a partir dos modelos selecionados e das
	 * restrições de tipo (interno, externo) e de tipo da forma (expediente,
	 * processo)
	 * 
	 * @param modelos
	 * @param tipoDoc
	 * @param tipoForma
	 * @return
	 * @throws Exception
	 */
	public SortedSet<ExFormaDocumento> obterFormasDocumento(List<ExModelo> modelos, ExTipoDocumento tipoDoc,
			ExTipoFormaDoc tipoForma) {
		SortedSet<ExFormaDocumento> formasSet = new TreeSet<ExFormaDocumento>();
		SortedSet<ExFormaDocumento> formasFinal = new TreeSet<ExFormaDocumento>();
		// Por enquanto, os parâmetros tipoForma e tipoDoc não podem ser
		// preenchidos simultaneamente. Melhorar isso.
		if (tipoDoc != null && tipoForma != null) {
			formasSet.addAll(SetUtils.intersection(tipoDoc.getExFormaDocumentoSet(), tipoForma.getExFormaDocSet()));
		} else if (tipoDoc != null)
			formasSet.addAll(tipoDoc.getExFormaDocumentoSet());
		else if (tipoForma != null)
			formasSet.addAll(tipoForma.getExFormaDocSet());
		else
			formasSet = null;

		for (ExModelo mod : modelos) {
			if (mod.getExFormaDocumento() == null)
				continue;
			if (formasSet == null || formasSet.contains(mod.getExFormaDocumento()))
				formasFinal.add(mod.getExFormaDocumento());
		}

		return formasFinal;
	}

	// Nato: esse método está muito lento, precisamos melhorar isso.
	public List<ExModelo> obterListaModelos(ExTipoDocumento tipo, ExFormaDocumento forma, boolean despachando,
			boolean criandoSubprocesso, ExMobil mobPai, String headerValue, boolean protegido, DpPessoa titular,
			DpLotacao lotaTitular, boolean autuando) {
		ArrayList<ExModelo> modeloSetFinal = new ArrayList<ExModelo>();
		ArrayList<ExModelo> provSet;
		boolean isComposto;
		if (forma != null)
			modeloSetFinal = new ArrayList<ExModelo>(forma.getExModeloSet());
		else
			modeloSetFinal = (ArrayList) dao().listarTodosModelosOrdenarPorNome(tipo, null);
		if (criandoSubprocesso && mobPai != null) {
			ExFormaDocumento especie = mobPai.doc().getExModelo().getExFormaDocumento();
			provSet = new ArrayList<ExModelo>();
			for (ExModelo mod : modeloSetFinal)
				if (especie.equals(mod.getExFormaDocumento()))
					provSet.add(mod);
			modeloSetFinal = provSet;
		}

		if (despachando) {
			isComposto = mobPai.doc().isComposto();
			provSet = new ArrayList<ExModelo>();
			for (ExModelo mod : modeloSetFinal) {
				if (getConf().podePorConfiguracao(titular, lotaTitular, mod,
						ExTipoDeConfiguracao.DESPACHAVEL)) {
					if (!isComposto) {
						if (getConf().podePorConfiguracao(titular, lotaTitular, null, mod.getExFormaDocumento(), mod,
								ExTipoDeConfiguracao.INCLUIR_EM_AVULSO)) {
							provSet.add(mod);
						}
					} else {
						provSet.add(mod);
					}
				}
			}
			modeloSetFinal = provSet;
		} else {
			if (protegido) {
				provSet = new ArrayList<ExModelo>();
				for (ExModelo mod : modeloSetFinal)
					if (getConf().podePorConfiguracao(titular, lotaTitular, mod,
							ExTipoDeConfiguracao.CRIAR_COMO_NOVO))
						provSet.add(mod);
				modeloSetFinal = provSet;
			}
		}

		if (autuando) {
			provSet = new ArrayList<ExModelo>();
			for (ExModelo mod : modeloSetFinal)
				if (getConf().podePorConfiguracao(titular, lotaTitular, mod, ExTipoDeConfiguracao.AUTUAVEL))
					provSet.add(mod);
			modeloSetFinal = provSet;
		}
		if (protegido) {
			provSet = new ArrayList<ExModelo>();
			for (ExModelo mod : modeloSetFinal) {
				if (getConf().podePorConfiguracao(titular, lotaTitular, mod, ExTipoDeConfiguracao.CRIAR))
					provSet.add(mod);
			}
			modeloSetFinal = provSet;
		}
		if (headerValue != null && modeloSetFinal.size() > 1) {
			ExModelo mod = new ExModelo();
			mod.setIdMod(0L);
			mod.setHisIdIni(0L);
			mod.setNmMod("[" + headerValue + "]");
			modeloSetFinal.add(0, mod);
		}
		return modeloSetFinal;
	}

	private boolean anexosAlternativosEstaoAssinados(ExDocumento doc) {
		// Verifica se o documento possui anexos alterantivos e se estes anexos
		// estão assinado
		for (ExMobil mob : doc.getExMobilSet()) {
			for (ExDocumento docFilho : mob.getExDocumentoFilhoSet()) {
				// Verifica se docFilho é do tipo anexo
				if (docFilho.getExFormaDocumento().getIdFormaDoc() == 60) {
					if (!docFilho.isCancelado() && docFilho.isPendenteDeAssinatura())
						return false;
				}
			}
		}
		return true;
	}

	public void gravarTermoEliminacao(ExDocumento doc) throws Exception {
		new ExTermoEliminacao(doc).gravar();
	}

	public void gravarEditalEliminacao(ExDocumento doc) throws Exception {
		new ExEditalEliminacao(doc).gravar();
	}

	public void incluirEmEditalEliminacao(ExDocumento edital, ExMobil mob) throws AplicacaoException {

		try {

			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoDeMovimentacao.INCLUSAO_EM_EDITAL_DE_ELIMINACAO, edital.getCadastrante(),
					edital.getLotaCadastrante(), mob, null, edital.getSubscritor(), edital.getLotaSubscritor(),
					edital.getTitular(), edital.getLotaTitular(), null);

			mov.setExMobilRef(edital.getMobilGeral());

			gravarMovimentacao(mov);

			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao incluir em edital de eliminação.", e);
		}

	}

	public void retirarDeEditalEliminacao(ExMobil mob, DpPessoa cadastrante, DpLotacao lotaCadastrante,
			DpPessoa subscritor, DpLotacao lotaSubscritor, DpPessoa titular, DpLotacao lotaTitular, String descrMov)
			throws AplicacaoException {

		try {

			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoDeMovimentacao.RETIRADA_DE_EDITAL_DE_ELIMINACAO, cadastrante, lotaCadastrante,
					mob, null, subscritor, lotaSubscritor, titular, lotaTitular, null);

			mov.setDescrMov(descrMov);

			gravarMovimentacao(mov);

			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao retirar do edital de eliminação.", e);
		}

	}

	public void obterMetodoPorString(String metodo, ExDocumento doc) throws Exception {
		if (metodo != null) {
			final Class[] classes = new Class[] { ExDocumento.class };

			ExBL exBl = Ex.getInstance().getBL();
			final Method method = ExBL.class.getDeclaredMethod(metodo, classes);
			method.invoke(exBl, new Object[] { doc });
		}
	}
	
	public byte[] obterPdfPorNumeroAssinatura(String num) throws Exception {
		return obterPdfPorNumeroAssinatura(num,false);
	}

	public byte[] obterPdfPorNumeroAssinatura(String num, boolean tamanhoOriginal) throws Exception {

		ExArquivo arq = buscarPorNumeroAssinatura(num);

		Documento documento = new Documento();

		if (arq instanceof ExDocumento)
			return documento.getDocumento(((ExDocumento) arq).getMobilGeral(), null, tamanhoOriginal);
		if (arq instanceof ExMovimentacao) {
			ExMovimentacao mov = (ExMovimentacao) arq;
			return documento.getDocumento(mov.getExMobil(), mov, tamanhoOriginal);
		}

		return null;

	}

	public ExArquivo buscarPorNumeroAssinatura(String num) throws Exception {
		Pattern p = Pattern.compile("([0-9]{1,10})(.[0-9]{1,10})?-([0-9]{1,4})");
		Matcher m = p.matcher(num);

		if (!m.matches())
			throw new AplicacaoException("Número inválido");

		Long idDoc = Long.parseLong(m.group(1));

		ExDocumento doc = ExDao.getInstance().consultar(idDoc, ExDocumento.class, false);

		if (doc == null)
			throw new AplicacaoException("Documento não encontrado");

		// Testa se o documento existe na base
		try {
			doc.getDescrCurta();
		} catch (ObjectNotFoundException e) {
			throw new RuntimeException("Documento não encontrado na base", e);
		}

		/*
		 * if (doc.getExNivelAcesso().getGrauNivelAcesso() > 20) throw new
		 * Exception("Documento sigiloso");
		 */

		int hash = Integer.parseInt(m.group(3));

		ExMovimentacao move = null;

		if (Math.abs(doc.getDescrCurta().hashCode() % 10000) == hash) {
			return doc;
		} else {
			for (ExMovimentacao mov : doc.getExMovimentacaoSet())
				if (Math.abs((doc.getDescrCurta() + mov.getIdMov()).hashCode() % 10000) == hash || Math
						.abs((doc.getDescrCurta() + mov.getIdMov() + "AssinaturaExterna").hashCode() % 10000) == hash)
					move = mov;
			if (move == null)
				throw new AplicacaoException("Número inválido");

			return move;
		}

	}

	public void apensarDocumento(final DpPessoa cadastrante, final DpPessoa docTitular, final DpLotacao lotaCadastrante,
			final ExMobil mob, final ExMobil mobMestre, final Date dtMov, final DpPessoa subscritor,
			final DpPessoa titular) {

		if (mobMestre == null)
			throw new AplicacaoException("não foi selecionado um documento para a apensação");

		if (mob.getExDocumento().getIdDoc().equals(mobMestre.getExDocumento().getIdDoc())
				&& mob.getNumSequencia().equals(mobMestre.getNumSequencia())
				&& mob.getExTipoMobil().getIdTipoMobil().equals(mobMestre.getExTipoMobil().getIdTipoMobil()))
			throw new AplicacaoException("não é possível apensar um documento a ele mesmo");

		if (!mobMestre.getExDocumento().isFinalizado())
			throw new AplicacaoException("não é possível apensar a um documento não finalizado");

		if (mobMestre.doc().isPendenteDeAssinatura())
			throw new AplicacaoException("não é possível apensar a um documento não finalizado");

		if (mobMestre.isGeral())
			throw new AplicacaoException("[E necessário definir a via ou volume do documento ao qual se quer apensar");

		if (mobMestre.doc().isPendenteDeAssinatura())
			throw new AplicacaoException("não é possível apensar a um documento não finalizado");

		if (mobMestre.isArquivado())
			throw new AplicacaoException("não é possível apensar a um documento arquivado");

		if (!mob.isVolumeEncerrado() && mobMestre.isVolumeEncerrado())
			throw new AplicacaoException("não é possível apensar um volume aberto a um volume encerrado");

		if (mobMestre.isSobrestado())
			throw new AplicacaoException("não é possível apensar a um documento Sobrestado");

		if (mobMestre.isJuntado())
			throw new AplicacaoException("não é possível apensar a um documento juntado");

		if (mobMestre.isEmTransito(cadastrante, lotaCadastrante))
			throw new AplicacaoException("não é possível apensar a um documento em trânsito");

		if (mobMestre.isCancelada())
			throw new AplicacaoException("não é possível apensar a um documento cancelado");

		for (ExMobil apenso : mobMestre.getMobilEApensosExcetoVolumeApensadoAoProximo()) {
			if (apenso.getIdMobil() == mob.getIdMobil()) {
				throw new AplicacaoException("não é possível apensar ao documento " + mobMestre.getSigla()
						+ ", pois este já está apensado ao documento " + mob.getSigla());
			}
		}

		Ex.getInstance().getComp().afirmar("Não é possível apensar", ExPodeApensar.class, docTitular, lotaCadastrante, mob);

		Ex.getInstance().getComp().afirmar("Não é possível apensar pois não é possível movimentar o mestre", ExPodeMovimentar.class, docTitular, lotaCadastrante, mobMestre);

		Ex.getInstance().getComp().afirmar("Não é possível apensar pois não é possível movimentar", ExPodeMovimentar.class, docTitular, lotaCadastrante, mob);

		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.APENSACAO,
					cadastrante, lotaCadastrante, mob, dtMov, subscritor, null, titular, null, null);

			mov.setExMobilRef(mobMestre);

			mov.setDescrMov("Apensado ao documento " + mov.getExMobilRef().getCodigo().toString());

			gravarMovimentacao(mov);
			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao apensar documento.", e);
		}
	}

	/**
	 * Após desapensar o mobil, copiamos do grande mestre o responsível e sua
	 * lotação para o mobil em questão, de modo que nem todas as movimentações do
	 * grande mestre tenham que ser copiadas para todos os mobiles. Em especial, a
	 * transferência e o recebimento só será copiado quando não se tratar de volume
	 * processual apensado ao próximo.
	 * 
	 * @param cadastrante
	 * @param lotaCadastrante
	 * @param mob
	 * @param dtMov
	 * @param subscritor
	 * @param titular
	 * @throws AplicacaoException
	 */
	public void desapensarDocumento(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor, final DpPessoa titular) throws AplicacaoException {

		if (!mob.isApensado())
			throw new AplicacaoException("Impossível desapensar documento que não está apensado.");

		try {
			iniciarAlteracao();

			ExMobil mobMestre = mob.getGrandeMestre();

			final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.DESAPENSACAO,
					cadastrante, lotaCadastrante, mob, dtMov, subscritor, null, titular, null, null);

			ExMovimentacao movRef = mov.getExMobil()
					.getUltimaMovimentacao(ExTipoDeMovimentacao.APENSACAO);
			mov.setExMovimentacaoRef(movRef);
			mov.setExMobilRef(movRef.getExMobilRef());
			mov.setDescrMov("Desapensado do documento " + mov.getExMobilRef().getCodigo().toString());

			for (PessoaLotacaoParser pl : mobMestre.getAtendente()) {
				mov.setLotaResp(pl.getLotacao());
				mov.setResp(pl.getPessoa());
			}
			gravarMovimentacao(mov);
			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao desapensar.", e);
		}

	}

	public void encerrarVolumeAutomatico(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov) throws AplicacaoException, Exception {

		if (mob.doc().isEletronico()) {
			//Nato: o refresh quando aplicado a uma entidade detached produz um erro e faz com que a
			// session fique num estado de RollbackOnly. Antes de usar o refresh é necessário verificar se "contains".
			// Mesmo assim, não sei por que existe esse "refresh" aqui. Mas imagino que, se alguém incluíu, seja necessário.
			// Fiz mais alguns testes e o "contains" é "true" para uma entidade que ainda não foi "flushed" para o banco.
			// Ou seja, está muito complicado isso aqui. Simplesmente desabilitei o refresh.
			// if (dao().em().contains(mob))
			//	   dao().em().refresh(mob);
			// Verifica se é Processo e conta o número de páginas para verificar
			// se tem que encerrar o volume
			if (mob.doc().isProcesso()) {
				if (mob.getTotalDePaginasSemAnexosDoMobilGeral() >= Prop.getInt("volume.max.paginas")) {
					encerrarVolume(cadastrante, lotaCadastrante, mob, dtMov, null, null, null, true);
				}
			}
		}
	}

	/**
	 * Encerra um volume e insere uma certidão de encerramento de volume, que deve
	 * ser produzida, tanto em html quanto em pdf.
	 * 
	 * @param cadastrante
	 * @param lotaCadastrante
	 * @param mob
	 * @param dtMov
	 * @param subscritor
	 * @param titular
	 * @param nmFuncaoSubscritor
	 * @throws AplicacaoException
	 * @throws Exception
	 */
	public void encerrarVolume(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, final ExMobil mob,
			final Date dtMov, final DpPessoa subscritor, final DpPessoa titular, String nmFuncaoSubscritor,
			boolean automatico) {

		if (mob.isVolumeEncerrado())
			throw new AplicacaoException("não é permitido encerrar um volume já encerrado");

		if (!mob.isVolume())
			throw new AplicacaoException("Esta operação somente pode ser realizada em volumes.");

		// if (responsavel == null && lotaResponsavel == null)
		// throw new AplicacaoException(
		// "não foram informados dados para o despacho/transferência");

		try {
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoDeMovimentacao.ENCERRAMENTO_DE_VOLUME, cadastrante, lotaCadastrante, mob,
					dtMov, subscritor, null, titular, null, null);

			gerarIdDeMovimentacao(mov);
			mov.setNmFuncaoSubscritor(nmFuncaoSubscritor);

			// Gravar o Html
			final String strHtml = processarModelo(mov, "processar_modelo", null, null);
			mov.setConteudoBlobHtmlString(strHtml);

			// Gravar o Pdf
			final byte pdf[] = Documento.generatePdf(strHtml);
			mov.setConteudoBlobPdf(pdf);
			mov.setConteudoTpMov("application/zip");

			if (automatico)
				mov.setDescrMov("Volume encerrado automaticamente.");

			gravarMovimentacao(mov);
			concluirAlteracao(mov);
		} catch (final AplicacaoException e) {
			cancelarAlteracao();
			throw e;
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao encerrar volume.", e);
		}
	}

	public void gravarModelo(ExModelo modNovo, ExModelo modAntigo, Date dt, CpIdentidade identidadeCadastrante)
			throws AplicacaoException {
		if ("template-file/jsp".equals(modNovo.getConteudoTpBlob())) {
			modNovo.setCpArquivo(null);
		}
		if (modNovo.getExFormaDocumento() == null)
			throw new AplicacaoException("não é possível salvar um modelo sem informar a forma do documento.");
		if (modNovo.getNmMod() == null || modNovo.getNmMod().trim().length() == 0)
			throw new AplicacaoException("não é possível salvar um modelo sem informar o nome.");
		if ((modNovo.getDescMod() == null || modNovo.getDescMod().trim().length() == 0)
				&& (!Prop.isGovSP()))
			throw new AplicacaoException("não é possível salvar um modelo sem informar a descrição.");
		
		if (modNovo.getDescMod() != null && modNovo.getDescMod().trim().length() > 256 )
			throw new AplicacaoException("A Descrição deve conter no máximo 256 caracteres");
		
		if (modNovo.getExtensoesArquivo() != null && !CpExtensoesDeArquivoEnum
				.validaLista(modNovo.getExtensoesArquivo()))
			throw new AplicacaoException ("Uma das extensões de arquivos informada é inválida.");
		
		try {
			ExDao.iniciarTransacao();
			dao().gravarComHistorico(modNovo, modAntigo, dt, identidadeCadastrante);
			ExDao.commitTransacao();
		} catch (Exception e) {
			ExDao.rollbackTransacao();
			throw new RuntimeException("Erro ao salvar um modelo.", e);
		}
	}

	public void gravarForma(ExFormaDocumento forma, ExFormaDocumento formaCadastrada) throws AplicacaoException {
		try {	
			
			if (forma.isEditando()) {
				boolean temDocumentoVinculado = dao().isExFormaComDocumentoVinculado(forma.getId());
				
				if (temDocumentoVinculado) {
					if (!forma.getSigla().equals(formaCadastrada.getSigla())) {													
						throw new RegraNegocioException("Não é possível alterar a sigla. Existem documentos que dependem desta informação.");									
					}
					
					if (!forma.getExTipoFormaDoc().getId().equals(formaCadastrada.getExTipoFormaDoc().getId())) {													
						throw new RegraNegocioException("Não é possível alterar o Tipo para <b>" + forma.getExTipoFormaDoc().getDescTipoFormaDoc() + "</b>"
								+ ". Existem documentos que dependem desta informação.");									
					}					
					
					if ((forma.getIsComposto() == null && formaCadastrada.getIsComposto() != null) ||
							(forma.getIsComposto() != null && formaCadastrada.getIsComposto() == null)) {													
						throw new RegraNegocioException("Não é possível alterar o indicativo de Documento Composto. Existem documentos que dependem desta informação.");									
					}
					
					String descricaoOrigens = "";					
					for (ExTipoDocumento origemCadastrada : formaCadastrada.getExTipoDocumentoSet()) {
						ExTipoDocumento origemEncontrada = forma.getExTipoDocumentoSet().stream()
								.filter(o -> o.getIdTpDoc().equals(origemCadastrada.getIdTpDoc()))
								.findAny()
								.orElse(null);
						
						if (origemEncontrada == null) {
							if (descricaoOrigens.length() > 0) descricaoOrigens += "<br/>";
							descricaoOrigens += origemCadastrada.getDescricaoSimples();						
						}
					}
					
					if (!descricaoOrigens.isEmpty()) {				
						String mensagem;
						
						if (descricaoOrigens.contains("<br/>")) {
							mensagem = "Não é possível retirar as Origens: <br/><b>" + descricaoOrigens + "</b><br/> Existem documentos que dependem destas informações.";
						} else {
							mensagem = "Não é possível retirar a Origem: <br/><b>" + descricaoOrigens + "</b><br/> Existem documentos que dependem desta informação.";
						}
												
						throw new RegraNegocioException(mensagem);
					}
				}			
			}				
							
			if (forma.getDescrFormaDoc() == null || forma.getDescrFormaDoc().isEmpty())
				throw new RegraNegocioException("Não é possível salvar um tipo sem informar a descrição.");
			if (forma.getExTipoFormaDoc() == null)
				throw new RegraNegocioException("Não é possível salvar um tipo sem informar se é processo ou expediente.");
			if (!forma.isSiglaValida())
				throw new RegraNegocioException("Sigla inválida. A sigla deve ser formada por 3 letras.");
	
			if (Prop.isGovSP()
					&& forma.getExTipoDocumentoSet().isEmpty())
				throw new RegraNegocioException("Selecione uma origem.");
	
			ExFormaDocumento formaConsulta = dao().consultarPorSigla(forma);
			if ((forma.getIdFormaDoc() == null && formaConsulta != null) || (forma.getIdFormaDoc() != null
					&& formaConsulta != null && !formaConsulta.getIdFormaDoc().equals(forma.getIdFormaDoc())))
				throw new RegraNegocioException("Esta sigla já está sendo utilizada.");
		
			ExDao.iniciarTransacao();
			dao().gravar(forma);
			ExDao.commitTransacao();
		} catch (RegraNegocioException e) {
			dao().em().getTransaction().rollback();
			throw new RegraNegocioException(e.getMessage());
		} catch (Exception e) {
			ExDao.rollbackTransacao();
			throw new RuntimeException("Erro ao salvar um tipo.", e);
		}
	}

	public void DesfazerCancelamentoDocumento(DpPessoa cadastrante, final DpLotacao lotaCadastrante, ExDocumento doc) {

		try {
			SortedSet<ExMobil> set = doc.getExMobilSet();

			iniciarAlteracao();

			for (ExMobil mob : set) {

				List<ExMovimentacao> movsCanceladas = mob.getMovimentacoesCanceladas();

				for (ExMovimentacao movARecuperar : movsCanceladas) {
					
					if (movARecuperar.isCanceladora())
						continue;
					
					ExMovimentacao movCanceladora = movARecuperar.getExMovimentacaoCanceladora();

					movARecuperar.setExMovimentacaoCanceladora(null);

					gravarMovimentacao(movARecuperar);

					final ExMovimentacao novaMov = criarNovaMovimentacao(
							ExTipoDeMovimentacao.CANCELAMENTO_DE_MOVIMENTACAO, cadastrante,
							lotaCadastrante, mob, null, null, null, null, null, null);

					novaMov.setExMovimentacaoRef(movCanceladora);

					gravarMovimentacaoCancelamento(novaMov, movCanceladora);
				}
				concluirAlteracaoParcial(mob);
			}

			concluirAlteracao();

		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao anular cancelamento do documento.", e);
		}
	}

	public void tornarDocumentoSemEfeito(DpPessoa cadastrante, final DpLotacao lotaCadastrante, ExDocumento doc,
			String motivo) throws Exception {	
		
		Ex.getInstance().getComp().afirmar(
				SigaMessages.getMessage("excecao.cancelamento.naopodetornardocumentosemefeito"),
				ExPodeTornarDocumentoSemEfeito.class, cadastrante, lotaCadastrante, doc.getMobilGeral());
		
		// Verifica se o subscritor pode movimentar todos os mobils
		// E Também se algum documento diferente está apensado ou juntado a este
		// documento

		for (ExMobil m : doc.getExMobilSet()) {
			if(!m.isGeral() && !m.isCancelada()) { //Retirada as vias que foram canceladas					
				
				getComp().afirmar(SigaMessages.getMessage("excecao.cancelamento.naopodemovimentar"), ExPodeMovimentar.class, cadastrante, lotaCadastrante, m);
				
				if (m.isJuntado()) {
					throw new RegraNegocioException("Não é possível efetuar o cancelamento, pois o documento está juntado");
				}
				
				if (m.temDocumentosJuntados()) {
					throw new RegraNegocioException("Não é possível efetuar o cancelamento, pois o documento possui documento(s) juntado(s)");
				}
				
				if (m.isApensado()) {
					throw new RegraNegocioException("Não é possível efetuar o cancelamento, pois o documento está apensado");
				}
				
				if (m.temApensos()) {
					throw new RegraNegocioException("Não é possível efetuar o cancelamento, pois o documento possui documento(s) apensado(s)");
				}							
			}
		}

		try {
			iniciarAlteracao();
			final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.TORNAR_SEM_EFEITO,
					cadastrante, lotaCadastrante, doc.getMobilGeral(), null, null, null, null, null, null);

			mov.setDescrMov(motivo);
			gravarMovimentacao(mov);

			// A gravação deve ser chamada apenas para atualizar a timestamp
			dao().gravar(doc);

			String funcao = doc.getForm().get("acaoExcluir");
			if (funcao != null) {
				obterMetodoPorString(funcao, doc);
			}

			if (SigaMessages.isSigaSP() && doc.isCapturado()) 
				concluirAlteracaoDoc(mov);
			else 
				concluirAlteracaoDocComRecalculoAcesso(mov);						
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao tornar o documento sem efeito.", e);
		}
	}

	public void alterarExClassificacao(ExClassificacao exClassNovo, ExClassificacao exClassAntigo, Date dt,
			CpIdentidade identidadeCadastrante) throws AplicacaoException {
		try {
			dao().gravarComHistorico(exClassNovo, exClassAntigo, dt, identidadeCadastrante);
			copiarReferencias(exClassNovo, exClassAntigo, dt, identidadeCadastrante);

		} catch (Exception e) {
			throw new RuntimeException("Erro ao copiar as propriedades do modelo anterior. ", e);
		}

	}

	public void copiarReferencias(ExClassificacao exClassNova, ExClassificacao exClassAntiga, Date dt,
			CpIdentidade identidadeCadastrante) throws AplicacaoException {
		copiarVias(exClassNova, exClassAntiga, dt, identidadeCadastrante);
		copiarModelos(exClassNova, exClassAntiga, dt, identidadeCadastrante);
	}

	private void copiarModelos(ExClassificacao exClassNovo, ExClassificacao exClassAntigo, Date dt,
			CpIdentidade identidadeCadastrante) throws AplicacaoException {
		try {
			// classificacao geral
			for (ExModelo modAntigo : exClassAntigo.getExModeloSet()) {
				ExModelo modNovo = new ExModelo();

				PropertyUtils.copyProperties(modNovo, modAntigo);
				modNovo.setIdMod(null);
				modNovo.setExClassificacao(exClassNovo);

				dao().gravarComHistorico(modNovo, modAntigo, dt, identidadeCadastrante);
				if (exClassNovo.getExModeloSet() == null) {
					exClassNovo.setExModeloSet(new HashSet<ExModelo>());
				}
				exClassNovo.getExModeloSet().add(modNovo);

			}

			// classificacao criacao via
			for (ExModelo modAntigo : exClassAntigo.getExModeloCriacaoViaSet()) {
				ExModelo modNovo = new ExModelo();

				PropertyUtils.copyProperties(modNovo, modAntigo);
				modNovo.setIdMod(null);
				modNovo.setExClassCriacaoVia(exClassNovo);

				dao().gravarComHistorico(modNovo, modAntigo, dt, identidadeCadastrante);
				if (exClassNovo.getExModeloCriacaoViaSet() == null) {
					exClassNovo.setExModeloCriacaoViaSet(new HashSet<ExModelo>());
				}
				exClassNovo.getExModeloCriacaoViaSet().add(modNovo);

			}

		} catch (Exception e) {
			throw new RuntimeException("não foi possível fazer cópia dos modelos!", e);
		}
	}

	private void copiarVias(ExClassificacao exClassNovo, ExClassificacao exClassAntigo, Date dt,
			CpIdentidade identidadeCadastrante) throws AplicacaoException {
		try {
			for (ExVia viaAntiga : exClassAntigo.getExViaSet()) {
				if (viaAntiga.isAtivo()) {
					ExVia viaNova = new ExVia();

					PropertyUtils.copyProperties(viaNova, viaAntiga);
					viaNova.setId(null);
					viaNova.setExClassificacao(exClassNovo);

					dao().gravarComHistorico(viaNova, viaAntiga, dt, identidadeCadastrante);
					if (exClassNovo.getExViaSet() == null) {
						exClassNovo.setExViaSet(new HashSet<ExVia>());
					}
					exClassNovo.getExViaSet().add(viaNova);
				}

			}
		} catch (Exception e) {
			throw new RuntimeException("não foi possível fazer cópia das vias!", e);
		}
	}

	public void moverClassificacao(ExClassificacao exClassDest, ExClassificacao exClassOrigem,
			CpIdentidade identidadeCadastrante) throws AplicacaoException {

		Date dt = dao().consultarDataEHoraDoServidor();
		MascaraUtil m = MascaraUtil.getInstance();
		ExDao dao = ExDao.getInstance();

		List<ExClassificacao> filhos = dao.consultarFilhos(exClassOrigem, true);

		if (filhos.size() > 0
				&& m.calcularNivel(exClassDest.getCodificacao()) > m.calcularNivel(exClassOrigem.getCodificacao())) {
			throw new AplicacaoException(
					"O nível do destino é maior do que o nível da origem! Os filhos não caberão na hierarquia da classificação documental!");
		}

		ExClassificacao classExistente = dao().consultarPorSigla(exClassDest);
		if (classExistente != null) {
			throw new AplicacaoException(
					"A classificação destino já existe! <br/><br/>" + classExistente.getCodificacao());
		}

		String mascaraDestino = m.getMscFilho(exClassDest.getCodificacao(), true);
		for (ExClassificacao f : filhos) {
			String novaCodificacao = m.substituir(f.getCodificacao(), mascaraDestino);
			ExClassificacao fNovo = getCopia(f);
			fNovo.setCodificacao(novaCodificacao);
			Ex.getInstance().getBL().alterarExClassificacao(fNovo, f, dt, identidadeCadastrante);
		}

		exClassDest.setHisIdIni(exClassOrigem.getHisIdIni());
		Ex.getInstance().getBL().alterarExClassificacao(exClassDest, exClassOrigem, dt, identidadeCadastrante);
	}

	public ExClassificacao getCopia(ExClassificacao exClassOrigem) throws AplicacaoException {
		ExClassificacao exClassCopia = new ExClassificacao();
		try {

			PropertyUtils.copyProperties(exClassCopia, exClassOrigem);

			// novo id
			exClassCopia.setId(null);
			exClassCopia.setHisDtFim(null);
			exClassCopia.setHisDtIni(null);
			exClassCopia.updateAtivo();

			// objeto collection deve ser diferente (mas com mesmos elementos),
			// senão ocorre exception
			// HibernateException:Found shared references to a collection
			Set<ExVia> setExVia = new HashSet<ExVia>();
			exClassCopia.setExViaSet(setExVia);

			Set<ExModelo> setExModelo = new HashSet<ExModelo>();
			exClassCopia.setExModeloSet(setExModelo);

			Set<ExModelo> setExModeloCriacaoVia = new HashSet<ExModelo>();
			exClassCopia.setExModeloCriacaoViaSet(setExModeloCriacaoVia);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao copiar as propriedades do modelo anterior.", e);
		}

		return exClassCopia;
	}

	public void incluirExClassificacao(ExClassificacao exClass, CpIdentidade identidadeCadastrante)
			throws AplicacaoException {
		dao().gravarComHistorico(exClass, null, dao().consultarDataEHoraDoServidor(), identidadeCadastrante);

	}

	private void verificarDuplicacaoTermoCompleto(ExClassificacao exClassNovo, ExClassificacao exClassAntigo)
			throws AplicacaoException {

		MascaraUtil m = MascaraUtil.getInstance();
		String mascara = m.getMscTodosDoNivel(m.calcularNivel(exClassNovo.getCodificacao()));
		List<ExClassificacao> exClassMesmoNivel = dao().consultarExClassificacao(mascara,
				exClassNovo.getDescrClassificacao());
		if (exClassMesmoNivel.size() > 0) {
			for (ExClassificacao exClassConflito : exClassMesmoNivel) {

				if (exClassNovo.getDescricao().equalsIgnoreCase(exClassConflito.getDescricao())) {
					// se conflito não causado por movimentacao onde a própria
					// classificacao é o conflito
					if (exClassAntigo == null
							|| !exClassConflito.getCodificacao().equals(exClassAntigo.getCodificacao())) {
						throw new AplicacaoException("Termo da classificação em conflito! <br/><br/>"
								+ exClassConflito.getDescricaoCompleta());
					}

				}

			}
		}

	}

	public void incluirExTemporalidade(ExTemporalidade exTemporalidade, CpIdentidade identidadeCadastrante)
			throws AplicacaoException {
		ExDao.getInstance().gravarComHistorico(exTemporalidade, null, null, identidadeCadastrante);
	}

	public ExTemporalidade getCopia(ExTemporalidade exTempOrigem) throws AplicacaoException {
		ExTemporalidade exTempCopia = new ExTemporalidade();
		try {

			PropertyUtils.copyProperties(exTempCopia, exTempOrigem);

			// novo id
			exTempCopia.setId(null);
			exTempCopia.setHisDtFim(null);
			exTempCopia.setHisDtIni(null);
			exTempCopia.updateAtivo();
			// objeto collection deve ser diferente (mas com mesmos elementos),
			// senão ocorre exception
			// HibernateException:Found shared references to a collection
			Set<ExVia> setExViaArqCorr = new HashSet<ExVia>();
			Set<ExVia> setExViaArqInterm = new HashSet<ExVia>();
			exTempCopia.setExViaArqCorrenteSet(setExViaArqCorr);
			exTempCopia.setExViaArqIntermediarioSet(setExViaArqInterm);

		} catch (Exception e) {
			throw new RuntimeException("Erro ao copiar as propriedades do objeto ExTemporalidade.", e);
		}

		return exTempCopia;

	}

	public void alterarExTemporalidade(ExTemporalidade exTempNovo, ExTemporalidade exTempAntiga, Date dt,
			CpIdentidade identidadeCadastrante) throws AplicacaoException {

		dao().gravarComHistorico(exTempNovo, exTempAntiga, dt, identidadeCadastrante);

		// copiar Referências arq corrente
		try {
			for (ExVia viaAntiga : exTempAntiga.getExViaArqCorrenteSet()) {
				ExVia viaNova = new ExVia();

				PropertyUtils.copyProperties(viaNova, viaAntiga);
				viaNova.setIdVia(null);
				viaNova.setTemporalidadeCorrente(exTempNovo);

				dao().gravarComHistorico(viaNova, viaAntiga, dt, identidadeCadastrante);
				if (exTempNovo.getExViaArqCorrenteSet() == null) {
					exTempNovo.setExViaArqCorrenteSet(new HashSet<ExVia>());
				}
				exTempNovo.getExViaArqCorrenteSet().add(viaNova);

			}
		} catch (Exception e) {
			throw new RuntimeException("não foi possível fazer cópia das vias em arquivo corrente!", e);
		}

		// copiar Referências arq intermediário
		try {
			for (ExVia viaAntiga : exTempAntiga.getExViaArqIntermediarioSet()) {
				ExVia viaNova = new ExVia();

				PropertyUtils.copyProperties(viaNova, viaAntiga);
				viaNova.setIdVia(null);
				viaNova.setTemporalidadeIntermediario(exTempNovo);

				dao().gravarComHistorico(viaNova, viaAntiga, dt, identidadeCadastrante);
				if (exTempNovo.getExViaArqIntermediarioSet() == null) {
					exTempNovo.setExViaArqIntermediarioSet(new HashSet<ExVia>());
				}
				exTempNovo.getExViaArqIntermediarioSet().add(viaNova);

			}
		} catch (Exception e) {
			throw new RuntimeException("não foi possível fazer cópia das vias em arquivo intermediário!", e);
		}

	}

	public ExModelo getCopia(ExModelo exModOrigem) throws AplicacaoException {
		ExModelo exModCopia = new ExModelo();
		try {

			PropertyUtils.copyProperties(exModCopia, exModOrigem);

			// novo id
			exModCopia.setId(null);
			exModCopia.setHisDtFim(null);
			exModCopia.setHisDtIni(null);
			exModCopia.updateAtivo();

		} catch (Exception e) {
			throw new RuntimeException("Erro ao copiar as propriedades do modelo anterior.", e);
		}

		return exModCopia;
	}

	public void excluirExClassificacao(ExClassificacao exClass, CpIdentidade idCadastrante) {
		Date dt = dao().consultarDataEHoraDoServidor();
		if (exClass.getExModeloSet().size() > 0 || exClass.getExModeloCriacaoViaSet().size() > 0) {
			StringBuffer sb = new StringBuffer();
			for (ExModelo m : exClass.getExModeloSet()) {
				sb.append("(");
				sb.append(m.getId());
				sb.append(") ");
				sb.append(m.getNmMod());
				sb.append("<br/>");
			}
			for (ExModelo m : exClass.getExModeloCriacaoViaSet()) {
				sb.append("(");
				sb.append(m.getId());
				sb.append(") ");
				sb.append(m.getNmMod());
				sb.append(" (Criação de via)");
				sb.append("<br/>");
			}

			throw new AplicacaoException(
					"não é possível excluir a classificação documental, pois estáassociada ao(s) seguinte(s) modelo(s):<br/><br/>"
							+ sb.toString());
		}

		/*
		 * AVISO:
		 * 
		 * 
		 * O código abaixo foi comentado para permitir a atualização da tabela de
		 * classificação documental enquanto a funcionalidade de reclassificação de
		 * documentos não estádisponível.
		 */

		/*
		 * List<ExDocumento> docs = ExDao.getInstance()
		 * .consultarExDocumentoPorClassificacao(null,
		 * MascaraUtil.getInstance().getMscTodosDoMaiorNivel(),
		 * idCadastrante.getPessoaAtual().getOrgaoUsuario()); if (docs.size() > 0) {
		 * StringBuffer sb = new StringBuffer();
		 * 
		 * throw new AplicacaoException(
		 * "Náo é possível excluir a classificação documental, pois já foi associada a documento(s)."
		 * + sb.toString()); }
		 */

		for (ExVia exVia : exClass.getExViaSet()) {
			dao().excluirComHistorico(exVia, dt, idCadastrante);
		}
		dao().excluirComHistorico(exClass, dt, idCadastrante);
	}

	public void incluirCosignatariosAutomaticamente(final DpPessoa cadastrante, final DpLotacao lotaCadastrante,
			final ExDocumento doc) throws Exception {

		final List<DpPessoa> cosignatariosDaEntrevista = obterCosignatariosDaEntrevista(doc.getForm());

		if (cosignatariosDaEntrevista != null && !cosignatariosDaEntrevista.isEmpty()) {

			if (doc.getCosignatarios() != null && !doc.getCosignatarios().isEmpty())
				excluirCosignatariosAutomaticamente(cadastrante, lotaCadastrante, doc);

			for (DpPessoa cosignatario : cosignatariosDaEntrevista) {

				incluirCosignatario(cadastrante, lotaCadastrante, doc, null, cosignatario, null);
			}
		}
	}

	public void excluirCosignatariosAutomaticamente(final DpPessoa cadastrante, final DpLotacao lotaCadastrante,
			final ExDocumento doc) throws Exception {

		List<Long> idExcl = new ArrayList<Long>();

		for (ExMovimentacao m : doc.getMobilGeral().getExMovimentacaoSet()) {
			if (m.getExTipoMovimentacao() == ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO
					&& m.getExMovimentacaoCanceladora() == null) {
				idExcl.add(m.getIdMov());
			}
		}
		for (Long id : idExcl) {
			excluirMovimentacao(cadastrante, lotaCadastrante, doc.getMobilGeral(), id);
		}
	}

	public List<DpPessoa> obterCosignatariosDaEntrevista(Map<String, String> docForm) {
		List<DpPessoa> list = new ArrayList<DpPessoa>();
		ExDao exDao = ExDao.getInstance();

		for (String chave : docForm.keySet()) {

			DpPessoa pessoa;

			if (chave.contains("cosignatarioSel.sigla")) {
				String valor = docForm.get(chave);

				if (valor != null) {

					pessoa = exDao.getPessoaFromSigla(valor);

					if (pessoa != null)
						list.add(pessoa);
				}
			}
		}
		return list;
	}

	private static class ByteArraySerializer implements
			JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
		public JsonElement serialize(byte[] src, Type srcType,
				JsonSerializationContext context) {
			String s = null;
			if (src != null)
				s = BlucService.bytearray2b64(src);
			return new JsonPrimitive(s);
		}

		public byte[] deserialize(JsonElement json, Type type,
				JsonDeserializationContext context) throws JsonParseException {
			String s = json.getAsString();
			if (s != null) {
				byte ab[] = BlucService.b642bytearray(s);
				return ab;
			}
			return null;
		}
	}

	private static class HibernateProxySerializer
			implements JsonSerializer<HibernateProxy>, JsonDeserializer<HibernateProxy> {
		private Gson gson;

		public JsonElement serialize(HibernateProxy src, Type srcType, JsonSerializationContext context) {
			Object obj = ((HibernateProxy) src).getHibernateLazyInitializer().getImplementation();

			return gson.toJsonTree(obj);
		}

		public void setGson(Gson gson) {
			this.gson = gson;
		}

		public HibernateProxy deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
				throws JsonParseException {
			// TODO Auto-generated method stub
			return null;
		}
	}

	private static class ObjetoBaseSerializer implements JsonSerializer<ObjetoBase>, JsonDeserializer<ObjetoBase> {
		private Gson gson;

		public JsonElement serialize(ObjetoBase src, Type srcType, JsonSerializationContext context) {

			return gson.toJsonTree(src);
		}

		public void setGson(Gson gson) {
			this.gson = gson;
		}

		public ObjetoBase deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
				throws JsonParseException {
			// TODO Auto-generated method stub
			return null;
		}
	}

	public static Object getImplementationDeep(Object obj) throws Exception {
		if (obj instanceof HibernateProxy) {
			obj = ((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
		}
		prune((Objeto) obj);
		getImplementationDeep(obj, obj.getClass(), new HashSet<Objeto>());
		return obj;
	}

	public static Object getImplementationDeep(Object o, Class clazz, HashSet<Objeto> set)
			throws Exception, IllegalAccessException {
		Field f[] = clazz.getDeclaredFields();
		AccessibleObject.setAccessible(f, true);

		for (int i = 0; i < f.length; i++) {
			final Object object = f[i].get(o);
			if (object instanceof ObjetoBase) {
				Objeto objeto = (Objeto) object;

				if (objeto instanceof HibernateProxy) {
					objeto = (Objeto) (((HibernateProxy) objeto).getHibernateLazyInitializer().getImplementation());
				}

				prune(objeto);

				if (!set.contains(objeto)) {
					set.add(objeto);
					objeto = (Objeto) getImplementationDeep(objeto, objeto.getClass(), set);
				}
				f[i].set(o, objeto);
			}
		}
		if (clazz.getSuperclass().getSuperclass() != null) {
			// System.out.println("*** Classe: " + clazz.getName() + " - "
			// + clazz.getSuperclass().getName());
			getImplementationDeep(o, clazz.getSuperclass(), set);
		}
		return o;
	}

	private static void prune(Objeto objeto) {
		if (objeto instanceof ExTipoDocumento) {
			((ExTipoDocumento) objeto).setExFormaDocumentoSet(null);
		}

		if (objeto instanceof DpLotacao) {
			((DpLotacao) objeto).setDpLotacaoSubordinadosSet(null);
			((DpLotacao) objeto).setLotacaoInicial(null);
			((DpLotacao) objeto).setLotacaoPai(null);
			((DpLotacao) objeto).setLotacoesPosteriores(null);
		}

		if (objeto instanceof DpPessoa) {
			((DpPessoa) objeto).setPessoaInicial(null);
			((DpPessoa) objeto).setPessoasPosteriores(null);
		}

		if (objeto instanceof ExMobil) {
			((ExMobil) objeto).setExMarcaSet(null);
			((ExMobil) objeto).setExDocumentoFilhoSet(null);
			((ExMobil) objeto).setExMovimentacaoReferenciaSet(null);
			((ExMobil) objeto).setExMovimentacaoSet(null);
		}
	}

	public String toJSON(ExMobil mobil) throws Exception {
		// Prune
		ExMobil mob = (ExMobil) getImplementationDeep(mobil);

		// mob.setExDocumento((ExDocumento)
		// Objeto.getImplementation(mob.doc()));
		mob.setExDocumentoFilhoSet(null);
		// mob.setExMarcaSet(null);
		mob.setExMovimentacaoReferenciaSet(null);
		mob.setExMovimentacaoSet(null);
		mob.setExTipoMobil(null);

		ExDocumento doc = mob.doc();
		// doc.setCadastrante(null);
		// doc.setDestinatario(null);
		doc.setExBoletimDocSet(null);
		doc.setExClassificacao(null);
		doc.setIdDocAnterior(null);
		doc.setExFormaDocumento(null);
		doc.setExMobilAutuado(null);
		doc.setExMobilPai(null);
		doc.setExMobilSet(null);
		doc.setExModelo(null);
		doc.setExNivelAcesso(null);
		// doc.setExTipoDocumento(null);
		// doc.getExTipoDocumento().setExFormaDocumentoSet(null);
		doc.setLotaCadastrante(null);
		doc.setLotaDestinatario(null);
		// doc.setLotaSubscritor((DpLotacao)
		// Objeto.getImplementation(doc.getLotaSubscritor()));
		// doc.setLotaSubscritor(null);
		// doc.setLotaTitular(null);
		// doc.setCadastrante(null);
		// doc.setDestinatario(null);
		// doc.setSubscritor(null);
		// doc.setTitular(null);
		// doc.setOrgaoExterno(null);
		// doc.setOrgaoExternoDestinatario(null);
		// doc.setOrgaoUsuario(null);

		ObjetoBaseSerializer hps = new ObjetoBaseSerializer();

		Gson gson = new GsonBuilder()
				.registerTypeAdapter(java.sql.Blob.class, new ByteArraySerializer())
				// .registerTypeAdapter(ObjetoBase.class, hps)
				.setPrettyPrinting().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
		hps.setGson(gson);

		String jsonOutput = gson.toJson(mob);

		// Importante para que as alterações do "prune" não sejam salvas no BD.
		dao().em().clear();

		return jsonOutput;
	}

	public List<ExAssinavelDoc> obterAssinaveis(DpPessoa titular, DpLotacao lotaTitular,
			boolean apenasComSolicitacaoDeAssinatura) {
		List<ExAssinavelDoc> assinaveis = new ArrayList<ExAssinavelDoc>();
		Map<Long, ExAssinavelDoc> map = new HashMap<>();

		// Acrescenta documentos
		//
		for (final ExDocumento doc : dao().listarDocPendenteAssinatura(titular, apenasComSolicitacaoDeAssinatura)) {
			if (!doc.isFinalizado() || !doc.isEletronico())
				continue;
			ExAssinavelDoc ass = acrescentarDocAssinavel(assinaveis, map, titular, lotaTitular, doc);
			ass.setPodeAssinar(true);
			ass.setPodeSenha(ass.isPodeAssinar()
					&& Ex.getInstance().getComp().pode(ExPodeAssinarComSenha.class, titular, lotaTitular, doc.getMobilGeral()));
		}

		// Acrescenta despachos
		//
		for (final ExMovimentacao mov : dao().listarDespachoPendenteAssinatura(titular)) {
			if (mov.isAssinada() || mov.isCancelada())
				continue;
			acrescentarMovAssinavel(assinaveis, map, titular, lotaTitular, false, mov);
		}

		// Acrescenta anexos
		//
		for (final ExMovimentacao mov : dao().listarAnexoPendenteAssinatura(titular)) {
			if (mov.isAssinada() || mov.isCancelada())
				continue;
			acrescentarMovAssinavel(assinaveis, map, titular, lotaTitular, true, mov);
		}

		// Acrescenta anexos que não estão destinados ao subscritor em questão
		//
		for (final ExAssinavelDoc assdoc : assinaveis) {
			for (ExMobil mob : assdoc.getDoc().getExMobilSet()) {
				if (mob.getExMovimentacaoSet() == null)
					continue;
				for (ExMovimentacao mov : mob.getExMovimentacaoSet()) {
					if (mov.isAssinada() || mov.isCancelada() || mov.getExTipoMovimentacao() != ExTipoDeMovimentacao.ANEXACAO)
						continue;

					acrescentarMovAssinavel(assinaveis, map, titular, lotaTitular, true, mov);
				}
			}
		}

		Collections.sort(assinaveis, new ExAssinavelComparador());
		return assinaveis;
	}

	protected ExAssinavelDoc acrescentarDocAssinavel(List<ExAssinavelDoc> assinaveis, Map<Long, ExAssinavelDoc> map,
			DpPessoa titular, DpLotacao lotaTitular, final ExDocumento doc) {
		ExAssinavelDoc ass = map.get(doc.getIdDoc());
		if (ass == null) {
			ass = new ExAssinavelDoc();
			ass.setDoc(doc);
			map.put(doc.getIdDoc(), ass);
			ass.setPodeAssinar(doc.isFinalizado() && doc.isPendenteDeAssinatura()
					&& !doc.isAssinadoPelaPessoaComTokenOuSenha(titular));
			ass.setPodeSenha(ass.isPodeAssinar()
					&& Ex.getInstance().getComp().pode(ExPodeAssinarComSenha.class, titular, lotaTitular, doc.getMobilGeral()));
			assinaveis.add(ass);
		}
		return ass;
	}

	private void acrescentarMovAssinavel(List<ExAssinavelDoc> assinaveis, Map<Long, ExAssinavelDoc> map,
			DpPessoa titular, DpLotacao lotaTitular, boolean podeAutenticar, final ExMovimentacao mov) {
		ExDocumento doc = mov.getExDocumento();
		ExAssinavelDoc ass = acrescentarDocAssinavel(assinaveis, map, titular, lotaTitular, doc);
		ExAssinavelMov assmov = new ExAssinavelMov();
		assmov.setMov(mov);
		assmov.setPodeSenha(Ex.getInstance().getComp().pode(ExPodeAssinarMovimentacaoComSenha.class, titular, lotaTitular, mov));
		assmov.setPodeAutenticar(podeAutenticar);
		ass.getMovs().add(assmov);
	}

	public void solicitarAssinatura(DpPessoa cadastrante, DpLotacao lotaTitular, ExDocumento doc) {
		try {
			iniciarAlteracao();
			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoDeMovimentacao.SOLICITACAO_DE_ASSINATURA, cadastrante, lotaTitular,
					doc.getMobilGeral(), null, cadastrante, null, null, null, null);					

			gravarMovimentacao(mov);
			concluirAlteracao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao revisar documento.", e);
		}
	}
	
	public ExProtocolo gerarProtocolo(ExDocumento doc, DpPessoa cadastrante, DpLotacao lotacao) {
		try {
			iniciarAlteracao();

			Date dt = dao().dt();
			Calendar c = Calendar.getInstance();
			c.setTime(dt);

			ExProtocolo prot = new ExProtocolo();
			/*
			 *prot.setNumero(
			 *     obterSequencia(Long.valueOf(c.get(Calendar.YEAR)), ExSequenciaEnum.PROTOCOLO.getValor(), "1"));
			*/
			prot.setCodigo(dao().gerarCodigoProtocolo());
			prot.setExDocumento(doc);
			prot.setData(c.getTime());

			dao().gravar(prot);
			ContextoPersistencia.flushTransaction();
			final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.GERAR_PROTOCOLO,
					cadastrante, lotacao, doc.getMobilGeral(), null, cadastrante, null, null, null, null);

			gravarMovimentacao(mov);
			concluirAlteracao(doc.getMobilGeral());
			return prot;
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Ocorreu um erro ao gerar protocolo.", e);
		}
	}
	
	public ExProtocolo obterProtocolo(ExDocumento doc) {
		try {
			return dao().obterProtocoloPorDocumento(doc);
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao obter protocolo.", e);
		}
	}
	
	
	
	
	/*
	 * Adicionado 23/01/2020
	 */

	public ExArquivo buscarPorProtocolo(String num) throws Exception {
		/*
		 * Busca por numero e ano
		 * 
		 * String temp[] = num.split("/");
		 * Long numero = Long.parseLong(temp[0]);
		 * Integer ano = Integer.parseInt(temp[1]);
		 * ExProtocolo protocolo = (ExProtocolo) dao().obterProtocoloPorCodigo(numero,ano);
		 */
		
		ExProtocolo protocolo = dao().obterProtocoloPorCodigo(num);
		
		if(protocolo == null)
			throw new AplicacaoException("Protocolo não encontrado");
		
		if (protocolo.getExDocumento() == null)
			throw new AplicacaoException("Protocolo não encontrado");

		try {
			protocolo.getExDocumento().getDescrCurta();
		} catch (ObjectNotFoundException e) {
			throw new AplicacaoException("Protocolo não encontrado", 0, e);
		}

		return protocolo.getExDocumento();
	}

	public byte[] obterPdfPorProtocolo(String num) throws Exception {
		ExDocumento doc = dao().obterProtocoloPorCodigo(num).getExDocumento();
		Pattern p = Pattern.compile("([0-9]{1,10})(.[0-9]{1,10})?-([0-9]{1,4})");
		Matcher m = p.matcher(doc.getSiglaAssinatura());
		
		if (doc == null)
			throw new AplicacaoException("Documento não encontrado");

		// Testa se o documento existe na base
		try {
			doc.getDescrCurta();
		} catch (ObjectNotFoundException e) {
			throw new AplicacaoException("Documento não encontrado", 0, e);
		}

		/*
		 * if (doc.getExNivelAcesso().getGrauNivelAcesso() > 20) throw new
		 * Exception("Documento sigiloso");
		 */

		String temp[] = doc.getSiglaAssinatura().split("-");
		int hash = Integer.parseInt(temp[1]);
		ExArquivo arq;
		ExMovimentacao move = null;

		if (Math.abs(doc.getDescrCurta().hashCode() % 10000) == hash) {
			arq = doc;
		} else {
			for (ExMovimentacao mov : doc.getExMovimentacaoSet())
				if (Math.abs((doc.getDescrCurta() + mov.getIdMov()).hashCode() % 10000) == hash || Math
						.abs((doc.getDescrCurta() + mov.getIdMov() + "AssinaturaExterna").hashCode() % 10000) == hash)
					move = mov;
			if (move == null)
				throw new AplicacaoException("Número inválido");

			arq = move;
		}

		Documento documento = new Documento();
		
		if (arq instanceof ExDocumento)
			return documento.getDocumento(((ExDocumento) arq).getMobilGeral(), null);
		if (arq instanceof ExMovimentacao) {
			ExMovimentacao mov = (ExMovimentacao) arq;
			return documento.getDocumento(mov.getExMobil(), mov);
		}
		return null;

	}

	/*
	 * Fim da adicao
	 */
	
	public void reordenarDocumentos(ExDocumento doc, DpPessoa cadastrante, DpLotacao lotacao, boolean isOrdemOriginal) {				
		try {
			iniciarAlteracao();
			
			ITipoDeMovimentacao idTpMov = isOrdemOriginal ? 
					ExTipoDeMovimentacao.ORDENACAO_ORIGINAL_DOCUMENTO : 
					ExTipoDeMovimentacao.REORDENACAO_DOCUMENTO;
					
			ExMovimentacao mov = criarNovaMovimentacao(idTpMov, cadastrante, lotacao, doc.getMobilGeral(), null, cadastrante, null, null, null, null);						

			gravarMovimentacao(mov);
			concluirAlteracao(doc.getMobilGeral());			
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Ocorreu um erro ao reordenar documentos.", e);
		}
	}
	
	public ExDocumento buscarDocumentoPorLinkPermanente(CpToken cpToken) {
		
		ExDocumento doc = ExDao.getInstance().consultar(cpToken.getIdRef(), ExDocumento.class, false);
		return doc;
		
	}

	public String publicarTransparencia(ExMobil mob, DpPessoa cadastrante, DpLotacao lotaCadastrante, boolean viaWS) {

		/* Verificação de autorização - Via WS é feito bypass*/
		if (!viaWS && !Ex.getInstance().getComp().pode(ExPodePublicarPortalDaTransparencia.class, cadastrante, lotaCadastrante, mob)) {
			throw new AplicacaoException(
					"Não é possível " + SigaMessages.getMessage("documento.publicar.portaltransparencia"));
		}

		/* 1- Redefinição para Público - Via WS é feito bypass*/
		if (!viaWS && !Ex.getInstance().getComp().pode(ExPodeRedefinirNivelDeAcesso.class, cadastrante, lotaCadastrante, mob)) {
			throw new AplicacaoException(
					"Não é possível redefinir o nível de acesso");
		}

		ExNivelAcesso exTipoSig = null;
		exTipoSig = dao().consultar(ExNivelAcesso.ID_PUBLICO, ExNivelAcesso.class, false);

		redefinirNivelAcesso(cadastrante, lotaCadastrante, mob.getDoc(), null, lotaCadastrante, cadastrante, cadastrante, cadastrante, null, exTipoSig);
		/* END Redefinição para Público */

		CpToken cpToken;
		String url;
		try {
			/*3- Gerar URL Permanente */
			cpToken = Cp.getInstance().getBL().gerarUrlPermanente(mob.getDoc().getIdDoc());
			url = Cp.getInstance().getBL().obterURLPermanente(cpToken.getIdTpToken().toString(), cpToken.getToken());
			
			/*4- Gerar Movimentação de Publicação */
			gravarNovaMovimentacao(ExTipoDeMovimentacao.PUBLICACAO_PORTAL_TRANSPARENCIA,
					cadastrante, lotaCadastrante, mob, null,null, null, 
					null, null, null, "Publicado no Portal da Transparência");

		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro na publicação do documento em Portal da Transparência", e);
		}
		
		return url;
		
	}
	
	public String documentoToJSON(ExDocumento doc) throws Exception {
		
	    Gson gson = new Gson();
	    
	    HashMap<String, Object> documentoJson = new HashMap<String, Object>();
	    HashMap<String, Object> orgaoJson = new HashMap<String, Object>();
	    HashMap<String, Object> modeloJson = new HashMap<String, Object>();
	    HashMap<String, Object> nivelAcessoJson = new HashMap<String, Object>();
	    HashMap<String, Object> classificacaoDocumentalJson = new HashMap<String, Object>();
	    HashMap<String, Object> formaJson = new HashMap<String, Object>();
	    HashMap<String, Object> marcadoresJson = new HashMap<String, Object>();
	    
	    documentoJson.put("idDoc", doc.getIdDoc());
	    documentoJson.put("siglaDocumento", doc.getSigla());
	    documentoJson.put("numExpediente", doc.getNumExpediente());
	    documentoJson.put("anoEmissao", doc.getAnoEmissao());
	    documentoJson.put("descricaoDocumento", doc.getDescrDocumento());
	    documentoJson.put("dataFinalizacao", doc.getDtFinalizacao());
	    
	    
	    CpToken cpToken = CpDao.getInstance().obterCpTokenPorTipoIdRef(CpToken.TOKEN_URLPERMANENTE,doc.getIdDoc());
	    String urlPermanente = "";
	    
	    if (cpToken != null) { //Obter Link Permanente
	    	urlPermanente = obterURLPermanente(cpToken.getIdTpToken().toString(), cpToken.getToken());
	    } else {
	    	urlPermanente = "Endereço público não disponível.";
	    }
	    
	    documentoJson.put("urlPermanente", urlPermanente);   

	    /* orgao */
	    orgaoJson.put("orgaoSigla", doc.getOrgaoUsuario().getSiglaOrgaoUsu());
	    orgaoJson.put("orgaoDescricao", doc.getOrgaoUsuario().getDescricao());
	    
	    documentoJson.put("orgao", orgaoJson);
	    
	    /* modelo */
	    modeloJson.put("nomeModelo", doc.getExModelo().getNmMod());
	    modeloJson.put("descModelo", doc.getExModelo().getDescMod());
	    
	    documentoJson.put("modelo", modeloJson);
	    
	    /* Nivel Acesso */
	    nivelAcessoJson.put("idNivelAcesso", doc.getExNivelAcessoAtual().getIdNivelAcesso());
	    nivelAcessoJson.put("nomeNivelAcesso", doc.getExNivelAcessoAtual().getNmNivelAcesso());
	    
	    documentoJson.put("nivelAcesso", nivelAcessoJson);
	    
	    /* Classificacao */
	    classificacaoDocumentalJson.put("codificacaoClassificacao", doc.getExClassificacaoAtual().getCodificacao());
	    classificacaoDocumentalJson.put("descClassificacao", doc.getExClassificacaoAtual().getDescrClassificacao());
	    
	    documentoJson.put("classificacao", classificacaoDocumentalJson);
	    
	    /* Forma Documental */
	    formaJson.put("especieSigla", doc.getExFormaDocumento().getSigla());
	    formaJson.put("especieDescricacao", doc.getExFormaDocumento().getDescricao());
	    
	    formaJson.put("especie", classificacaoDocumentalJson);
	    
	    /* Marcadores */
	    marcadoresJson.put("marcadorGeral", doc.getMobilGeral().getMarcadores());
	    marcadoresJson.put("marcadorMobil", doc.getPrimeiroMobil().getMarcadores());
	    
	    documentoJson.put("marcadores", marcadoresJson);

	    // converte objetos Java para JSON e retorna JSON como String
	    String json = gson.toJson(documentoJson);

		return json;
		
	}
	
	public String marcadoresGeraisTaxonomiaAdministradaToJSON() throws Exception {
		Gson gson = new Gson();

		ArrayList<HashMap<String, Object>> objectJson = new ArrayList<>();
		try {
			List<CpMarcador> listaMarcadores = dao().listarCpMarcadoresGerais(true);
			
			for (CpMarcador marcador : listaMarcadores) {
				HashMap<String, Object> marcadorJson = new HashMap<String, Object>();
				HashMap<String, Object> tipoMarcadorJson = new HashMap<String, Object>();
				
				marcadorJson.put("id", marcador.getIdMarcador());
				marcadorJson.put("descMarcador", marcador.getDescrMarcador());
				
				tipoMarcadorJson.put("idTipoMarcador", marcador.getIdFinalidade().getIdTpMarcador().getId());
				tipoMarcadorJson.put("descTipoMarcador", marcador.getIdFinalidade().getIdTpMarcador().getDescricao());
				marcadorJson.put("tipoMarcador", tipoMarcadorJson);
				
				objectJson.add(marcadorJson);
			}
			
			// converte objetos Java para JSON e retorna JSON como String
		    String json = gson.toJson(objectJson);    
		    return json;
		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro na conversão dos marcadores para JSON.", e);
		} finally {
			objectJson = null;
		}
	}
	
	public void corrigeDocSemMobil(ExDocumento doc)
			throws Exception {
		Set<ExVia> setVias = doc.getSetVias();
		List<Integer> mobs = new ArrayList<Integer>(); 
		if (doc.getExMobilSet().isEmpty())
			doc.setExMobilSet(new TreeSet<ExMobil>());

		for (ExMobil m : doc.getExMobilSet()) {
			if (!m.isGeral())
				mobs.add(m.getNumSequencia());
		}

		if (doc.getMobilGeral() == null) {
			ExMobil mob = new ExMobil();
			mob.setExTipoMobil(dao().consultar(ExTipoMobil.TIPO_MOBIL_GERAL, ExTipoMobil.class, false));
			mob.setNumSequencia(1);
			mob.setExDocumento(doc);
			mob.setDnmSigla(mob.getSigla());
			doc.getExMobilSet().add(mob);
			mob = dao().gravar(mob);
		}
	
		if (doc.getExFormaDocumento().getExTipoFormaDoc().isExpediente()) {
			for (final ExVia via : setVias) {
				Integer numVia = null;
				if (via.getCodVia() != null)
					numVia = Integer.parseInt(via.getCodVia());
				if (numVia == null) {
					numVia = 1;
				}
				if (!mobs.contains(numVia))
					criarVia(doc.getCadastrante(), doc.getLotaCadastrante(), 
							doc.getTitular(), doc.getLotaTitular(), doc, numVia);
			}
		} else {
			criarVolume(doc.getCadastrante(), doc.getLotaCadastrante(), 
					doc.getTitular(), doc.getLotaTitular(), doc);
		}
	
		concluirAlteracaoDocComRecalculoAcesso(doc);
	
		ContextoPersistencia.flushTransaction();
	
		if (setVias == null || setVias.size() == 0)
			criarVia(doc.getCadastrante(), doc.getLotaCadastrante(), 
					doc.getTitular(), doc.getLotaTitular(), doc, null);
		return;
	}
	
	public void corrigeDocSemDescricao(ExDocumento doc)
			throws Exception {
		if (doc.getDescrDocumento() != null)
			throw new AplicacaoException("Documento já contém a descrição.");
		gravaDescrDocumento(doc.getTitular(), doc.getLotaTitular(), doc);
	
		concluirAlteracaoDocComRecalculoAcesso(doc);
	
		ContextoPersistencia.flushTransaction();
	
		return;
	}

	private void gravaDescrDocumento(DpPessoa titular, DpLotacao lotaTitular, ExDocumento doc) throws Exception {
		// Obtem a descricao pela macro @descricao
		if (doc.getExModelo().isDescricaoAutomatica()) {
			doc.setDescrDocumento(processarComandosEmTag(doc, "descricao"));

			// Obter a descricao pela macro @entrevista
		} else if (!Ex.getInstance().getComp().pode(ExPodeEditarDescricao.class, titular, lotaTitular, doc.getExModelo())) {
			String s = processarModelo(doc, null, "entrevista", null, null);
			String descr = extraiTag(s, "descricaoentrevista");
			doc.setDescrDocumento(descr);
		}
		if (doc.getDescrDocumento() == null || doc.getDescrDocumento().isEmpty())
			doc.setDescrDocumento(processarComandosEmTag(doc, "descricaodefault"));

		if (doc.getDescrDocumento() == null || doc.getDescrDocumento().isEmpty())
			doc.setDescrDocumento(doc.getExModelo().getNmMod()
					+ (doc.getSubscritorString() != null ? " de " + doc.getSubscritorString() : ""));
	}

	public void exibirNoAcompanhamentoDoProtocolo(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, 
			final ExMobil mob, final DpPessoa titular) throws AplicacaoException {
		if (mob == null)
			throw new AplicacaoException("Não existe via para a disponibilização no acompanhamento do protocolo.");
		
		if (!Ex.getInstance().getComp().pode(ExPodeDisponibilizarNoAcompanhamentoDoProtocolo.class, cadastrante, lotaCadastrante, mob.getDoc()))
			throw new AplicacaoException("Disponibilização no acompanhamento do protocolo só é permitida para despachos.");
		
		Set<ExMovimentacao> movs = mob.getMovsNaoCanceladas(ExTipoDeMovimentacao
				.EXIBIR_NO_ACOMPANHAMENTO_DO_PROTOCOLO);
		if (!movs.isEmpty())
			throw new AplicacaoException("Disponibilização no acompanhamento do protocolo já foi solicitada anteriormente.");
		
		try {						
			iniciarAlteracao();

			final ExMovimentacao mov = criarNovaMovimentacao(
					ExTipoDeMovimentacao.EXIBIR_NO_ACOMPANHAMENTO_DO_PROTOCOLO, 
					cadastrante, lotaCadastrante, mob, dao().dt(), null, null, titular, null, dao().dt());

			gravarMovimentacao(mov);

			concluirAlteracao(mov.getExMobil());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao permitir a disponibilização do documento no acompanhamento do protocolo.", 0, e);
		}
	}
	
	public String extraiPersonalizacaoAssinatura(final ExMovimentacao movimentacao, boolean exibeFuncaoLotacaoSemPersonalizacao) {
		/*
		 *getNmFuncaoSubscritor = [0] - personalizarFuncao [1] - personalizarUnidade [2] - personalizarLocalidade [3] - personalizarNome
		 */
		
		if (movimentacao.getExTipoMovimentacao() != ExTipoDeMovimentacao.ASSINATURA_COM_SENHA 
				&& movimentacao.getExTipoMovimentacao() != ExTipoDeMovimentacao.ASSINATURA_DIGITAL_DOCUMENTO
				&& movimentacao.getExTipoMovimentacao() != ExTipoDeMovimentacao.CONFERENCIA_COPIA_COM_SENHA
				&& movimentacao.getExTipoMovimentacao() != ExTipoDeMovimentacao.CONFERENCIA_COPIA_DOCUMENTO) {
			throw new RuntimeException("Não é possível extrair personalização de movimentações que não são de assinatura ou autenticação.");
		}
		
		SortedSet<ExMovimentacao> listaMovimentacoes = movimentacao.mob().getExMovimentacaoSet();
		ExMovimentacao movimentacaoOrigem = null; 
		String[] personalizacaoAssinatura = new String[4];	
		
		if (movimentacao.getExDocumento().getSubscritor().equivale(movimentacao.getSubscritor())) {
			if (movimentacao.getExDocumento().getNmFuncaoSubscritor() != null ) {
				personalizacaoAssinatura = movimentacao.getExDocumento().getNmFuncaoSubscritor().split(";");
			} else if (!exibeFuncaoLotacaoSemPersonalizacao) {
				return "";
			}
		} else {
		
			for (ExMovimentacao mov : listaMovimentacoes) {
				if (!mov.equals(movimentacao)) {
					if (mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO) {
						if (mov.getExMovimentacaoCanceladora() == null) {
							if (mov.getSubscritor().equivale(movimentacao.getSubscritor())) {
								movimentacaoOrigem = mov;
								break;
							}
						}
					}
				}
			}
			if (movimentacaoOrigem != null && movimentacaoOrigem.getNmFuncaoSubscritor() != null ) {
				personalizacaoAssinatura = movimentacaoOrigem.getNmFuncaoSubscritor().split(";");	
			} else if (!exibeFuncaoLotacaoSemPersonalizacao) {
				return "";
			}
		}
		

		StringBuilder funcaoCargoPersonalizadoAssinatura = new StringBuilder();
		
		funcaoCargoPersonalizadoAssinatura.append(" - ");
		if (personalizacaoAssinatura[0] != null && !"".equals(personalizacaoAssinatura[0])) {
			funcaoCargoPersonalizadoAssinatura.append(personalizacaoAssinatura[0]);
		} else {
			funcaoCargoPersonalizadoAssinatura.append(movimentacao.getTitular() != null ? movimentacao.getTitular().getFuncaoString() : movimentacao.getCadastrante().getFuncaoString());
		}
		funcaoCargoPersonalizadoAssinatura.append(" / ");
		if (personalizacaoAssinatura.length > 1) {
			if (personalizacaoAssinatura[1] != null && !"".equals(personalizacaoAssinatura[1])) {
			 funcaoCargoPersonalizadoAssinatura.append(personalizacaoAssinatura[1]);
			} else {
				funcaoCargoPersonalizadoAssinatura.append(movimentacao.getTitular() != null ? movimentacao.getTitular().getLotacao().getSigla() : movimentacao.getCadastrante().getLotacao().getSigla());
			}
		} else {
			funcaoCargoPersonalizadoAssinatura.append(movimentacao.getTitular() != null ? movimentacao.getTitular().getLotacao().getSigla() : movimentacao.getCadastrante().getLotacao().getSigla());
		}
		return funcaoCargoPersonalizadoAssinatura.toString();

	}

	public void definirPrazoAssinatura(final DpPessoa cadastrante, final DpLotacao lotaCadastrante, 
			final ExDocumento doc, final DpPessoa titular, Date dtPrazo) throws AplicacaoException {
		if (doc == null)
			throw new AplicacaoException("Documento não existente.");
		ExMobil mob = doc.getMobilGeral();
		
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern( "dd/MM/yyyy HH:mm" );
		LocalDateTime localDate = dtPrazo.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		String dtPrazoStr = localDate.format(fmt);
		try {
			if (localDate.isBefore(LocalDateTime.now())) 
				throw new AplicacaoException(
						"Data e hora de devolução não pode ser anterior à agora: " + dtPrazoStr);
		} catch (DateTimeParseException e) {
			throw new AplicacaoException("Data ou hora do prazo inválida: " + dtPrazoStr);
		}
		
		if (!Ex.getInstance().getComp()
				.pode(ExPodeDefinirPrazoAssinatura.class, cadastrante, lotaCadastrante, mob))
			throw new AplicacaoException("Definição de prazo para assinatura não permitida.");
		
		ExMovimentacao mov = doc.getMovPrazoDeAssinatura();
		if (mov != null 
				&& !Ex.getInstance().getComp().pode(ExPodeCancelarOuAlterarPrazoDeAssinatura.class, cadastrante, lotaCadastrante, mov))
			throw new AplicacaoException("Usuário não permitido a alterar o prazo de assinatura. Se o documento "
					+ "estiver assinado, deve ser o subscritor; senão deve ser quem cadastrou o prazo.");
		
		try {						
			iniciarAlteracao();

			if (mov == null) {
				mov = criarNovaMovimentacao(
						ExTipoDeMovimentacao.PRAZO_ASSINATURA, 
						cadastrante, lotaCadastrante, mob, dao().dt(), null, null, titular, null, dao().dt());
			} 
			mov.setDtParam1(dtPrazo);
			
			mov.setDescrMov("O prazo de assinatura " + dtPrazoStr + " foi definido para o documento " + mob.doc().getSigla());

			gravarMovimentacao(mov);

			concluirAlteracao(mov.getExMobil());
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro na definição de prazo para assinatura do documento.", 0, e);
		}
	}

	public void atualizarPrincipal(ExDocumento doc, ExTipoDePrincipal tipo, String siglaPrincipal) {
		doc.setTipoDePrincipal(tipo);
		doc.setPrincipal(siglaPrincipal);
		ExDao.getInstance().gravar(doc);
	}

	public void gravarSiafem(String usuarioSiafem, String senhaSiafem, ExDocumento exDoc, DpPessoa cadastrante, DpLotacao lotacaoTitular) {
		try {
			enviarSiafem(usuarioSiafem, senhaSiafem, exDoc);
			gravarMovimentacaoSiafem(exDoc, cadastrante, lotacaoTitular);
		} catch (final AplicacaoException e) {
			if(e.getCodigoErro() == ExTipoDeMovimentacao.ENVIO_SIAFEM.getId())
				cancelarAlteracao();
			throw e;
		}
	}

	private void enviarSiafem(String usuarioSiafem, String senhaSiafem, ExDocumento exDoc) throws AplicacaoException {
		ExDocumento formulario = obterFormularioSiafem(exDoc);
		
		if(formulario == null)
			throw new AplicacaoException("Favor preencher o \"" + Prop.get("ws.siafem.nome.modelo") + ".");
		
		Map<String, String> form = new TreeMap<String, String>();
		Utils.mapFromUrlEncodedForm(form, formulario.getConteudoBlobForm());
		SiafDoc siafDoc = new SiafDoc(form);
		
		siafDoc.setCodSemPapel(exDoc.getExMobilPai().doc().getSigla().replaceAll("[-/]", ""));
		
		ServicoSiafemWs.enviarDocumento(usuarioSiafem, senhaSiafem, siafDoc);
	}

	private void gravarMovimentacaoSiafem(ExDocumento exDoc, DpPessoa cadastrante, DpLotacao lotacaoTitular) throws AplicacaoException {
		try {
			ExMovimentacao mov = new ExMovimentacao();
			Date dt = dao().dt();
			//final ExTipoDeMovimentacao tpmov = dao().consultar(ExTipoDeMovimentacao.ENVIO_SIAFEM, ExTipoDeMovimentacao.class, false);

			mov.setCadastrante(cadastrante);
			mov.setDtIniMov(dt);
			mov.setDtFimMov(dt);
			mov.setDtMov(dt);
			mov.setExMobil(exDoc.getPrimeiraVia());
			mov.setExTipoMovimentacao(ExTipoDeMovimentacao.ENVIO_SIAFEM);
			mov.setLotaCadastrante(lotacaoTitular);
			mov.setLotaResp(lotacaoTitular);
			mov.setLotaSubscritor(lotacaoTitular);
			mov.setLotaTitular(lotacaoTitular);
			mov.setResp(cadastrante);
			mov.setSubscritor(cadastrante);
			mov.setTitular(cadastrante);
			mov.setDescrMov("Documento enviado ao SIAFEM: " + exDoc.getSigla()
					+ " " + exDoc.getPai().getSigla());

			acrescentarCamposDeAuditoria(mov);

			gravarMovimentacao(mov);
		} catch (final AplicacaoException | SQLException e) {
			throw new AplicacaoException(e.getMessage(), ExTipoDeMovimentacao.ENVIO_SIAFEM.getId());
		}
	}

	public ExDocumento obterFormularioSiafem(ExDocumento doc) {
		String modeloSiafem = Prop.get("ws.siafem.nome.modelo");//"Formulario Integracao Siafem";
		
		if(modeloSiafem == null)
			return null;
			
		if(doc.getNmMod().equals(modeloSiafem)) 
			return doc;
		
		ExMobil mDefault = doc.getMobilDefaultParaReceberJuntada();

		if(mDefault == null)
			return null;
		
		Set<ExMobil> mobilsJuntados = mDefault.getJuntados();
		
		for (ExMobil exMobil : mobilsJuntados) {
			if(!exMobil.isCancelada() && modeloSiafem.contains(exMobil.getDoc().getNmMod())) {
				return exMobil.getDoc();
			}
		}
		
		return null;
	}
	
	private String formatarCodigoUnico(String codigo){
		String numero = codigo.trim().replaceAll("[^\\d]", "");
		
		String ano = numero.substring(numero.length() - 4, numero.length());
		String sequencia = ("000000" + numero.substring(0, numero.length() - 4));
		sequencia = sequencia.substring(sequencia.length() - 6, sequencia.length());
		
		return ano + sequencia;
	}
	
	public String obterCodigoUnico(ExDocumento doc, boolean comDigitoVerificador) {
		ExDocumento doctSiafem = obterFormularioSiafem(doc);
		
		if(doctSiafem == null)
			return null;
				
		Map<String, String> form = new TreeMap<String, String>();
		Utils.mapFromUrlEncodedForm(form, doctSiafem.getConteudoBlobForm());
		
		String codigoUnico = form.get("codigoUnico") == null ? "" : form.get("codigoUnico").trim();
		String digitoCodigoUnico = form.get("digitoVerificadorCodigoUnico") == null ? "" : form.get("digitoVerificadorCodigoUnico").trim();
		
		if(codigoUnico.isEmpty() || digitoCodigoUnico.isEmpty())
			throw new AplicacaoException("O código único não foi gerado corretamente");
				
		if(!comDigitoVerificador)
			return codigoUnico;
		
		return codigoUnico + "-" + digitoCodigoUnico;
	}
	
	public String calcularDigitoVerificador(String numero) {
		int soma = 0;
		for(int i = 0, j = numero.length(); i < numero.length(); i++, j--) {
			soma += Integer.valueOf(numero.charAt(i) + "")*j;
		}
		
		int resto = soma%11;
		int digito = 11 - resto;
		
		if(digito > 9 || digito == 0)
			return "1";
		
		return digito + "";
	}


	public String obterNumeracaoExpediente(Long idOrgaoUsuario, Long idFormaDocumento, Long anoEmissao) throws Exception {
		return Service.getExService().obterNumeracaoExpediente(idOrgaoUsuario, idFormaDocumento, anoEmissao);
	}
	
	public Map<String, String> obterEntrevista(ExDocumento doc, boolean incluirOculto) {
		Map<String, String> entrevista = new HashMap<>();
		
		Utils.mapFromUrlEncodedForm(entrevista, doc.getConteudoBlobForm());
		
		ArrayList<String> variaveisOcultas = obterVariaveisOcultas(doc.getExModelo());
		
		for(Iterator<String> it = entrevista.keySet().iterator(); it.hasNext(); ) 
			if(!incluirOculto && variaveisOcultas.contains(it.next()))
				it.remove();
		
		return entrevista;
	}

	public ArrayList<String> obterVariaveisOcultas(ExModelo mod) {
		ArrayList<String> variaveisOcultas = new ArrayList<>();

		byte[] blobMod = mod.getConteudoBlobMod();
		
		if (blobMod != null) {
			final String blobStr = new String(blobMod);
			int fromIndex = 0;

			do {
				fromIndex = blobStr.indexOf("[@oculto", fromIndex);
				if (fromIndex != -1) {
					fromIndex += 8;
					int start = blobStr.indexOf("var", fromIndex) + 3;
					start = blobStr.indexOf("\"", start) + 1;
					int end = blobStr.indexOf("\"", start + 1);

					if (start > 0 && end > start) 
						variaveisOcultas.add(blobStr.substring(start, end));
				}
			} while (fromIndex != -1);
		} 

		return variaveisOcultas;
	}

	public List<Long> pesquisarXjus(
			String filter, 
			String acronimoOrgaoUsu, 
			String descEspecie,
			String descModelo,
			String dataInicial, 
			String dataFinal,
			String anoEmissao,
			String numeroExpediente,
			String lotacaoSubscritor,
			String acl, 
			int page, 
			int perpage) throws Exception {
		
		final SigaHTTP http = new SigaHTTP();
		String url = Prop.get("/xjus.url");

		String facets = ( acronimoOrgaoUsu == null ? "" : ( "facet_orgao:" + acronimoOrgaoUsu ) ) +
				( descEspecie == null ? "" : ( ",facet_especie:" + descEspecie ) ) +
				( descModelo == null ? "" : ( ",facet_modelo:" + descModelo ) ) +
				( anoEmissao == null ? "" : ( ",facet_ano:" + anoEmissao ) ) +
				( lotacaoSubscritor == null ? "" : ( ",facet_subscritor_lotacao:" + lotacaoSubscritor ) );
		
		url += "?filter=" + URLEncoder.encode(filter, "UTF-8") + 
			   "&facets=" + URLEncoder.encode(facets, "UTF-8") +
			   "&page=" + page + 
			   "&perpage=" + perpage;

		if (numeroExpediente != null && !numeroExpediente.isEmpty())
			url += "&code=" + numeroExpediente;
				
		if (dataInicial != null || dataFinal != null)
			url += "&fromDate=" + ( dataInicial == null ? "" : dataInicial ) +
					"&toDate=" + ( dataFinal == null ? "" : dataFinal );

		final JWTSigner signer = new JWTSigner(Prop.get("/xjus.jwt.secret"));
		final HashMap<String, Object> claims = new HashMap<String, Object>();

		final long iat = System.currentTimeMillis() / 1000L; // issued at claim
		final long exp = iat + 60 * 60L; // token expires in 1h
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("acl", acl);
		String token = signer.sign(claims);

		HashMap<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + token);
		String response = http.getNaWeb(url, headers, 60000, null);

		JSONObject obj = new JSONObject(response);
		JSONArray arr = obj.getJSONArray("results");

		List<Long> ret =  new ArrayList<Long>();
		for (int i = 0; i < arr.length(); i++){
			String id = arr.getJSONObject(i).getString("id");
			ret.add(Long.parseLong(id));
		}

		return ret;
	}


	public void gravarMovimentacaoLinkPublico(final DpPessoa cadastrante, final DpPessoa titular, final DpLotacao lotaTitular, final ExMobil mob) {
		
		try {
            final ExMovimentacao mov = criarNovaMovimentacao(ExTipoDeMovimentacao.GERAR_LINK_PUBLICO_PROCESSO, cadastrante,
					lotaTitular, mob, null, cadastrante, null, titular, lotaTitular, null);

			mov.setDescrMov("Gerado link público do documento " + mob.getSigla());
			gravarMovimentacao(mov);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new AplicacaoException("Erro ao gravar link público do documento", ExTipoDeMovimentacao.GERAR_LINK_PUBLICO_PROCESSO.getId(), e);
		}

	}

	public ExMovimentacao enviarParaVisualizacaoExterna(final String nmPessoa, final String email, ExDocumento doc,
											  final DpPessoa cadastrante, final DpLotacao lotaCadastrante,
											  final String cod, final String url) throws AplicacaoException {


		final Date dt = ExDao.getInstance().dt();
		final String dest = "Destinatário: " + nmPessoa + ". " + "e-mail: " + email;
		final String descrMov = doc.getSigla() + " enviado para visualização externa.\n" + dest;
		final String numeroReferencia = doc.getSiglaAssinatura();

		try {
			final ExEmail exEmail = new ExEmail();
			exEmail.enviarAoDestinatarioExterno(nmPessoa, email, doc.getSigla(), 
					numeroReferencia, cod, url);

			ExMovimentacao mov = gravarNovaMovimentacao(
					ExTipoDeMovimentacao.ENVIO_PARA_VISUALIZACAO_EXTERNA,
					cadastrante, lotaCadastrante, doc.getMobilGeral(), dt, null, null,
					null, null, null, descrMov);

			Cp.getInstance().getBL().gravarNovoToken(
					CpToken.TOKEN_COD_ACESSO_EXTERNO_AO_DOCUMENTO,
					mov.getIdMov(),
					Calendar.MONTH,
					1,
					cod
			);

			return mov;
			
		} catch (final Exception e) {
			throw new AplicacaoException("Erro ao enviar para visualização externa",
					ExTipoDeMovimentacao.ENVIO_PARA_VISUALIZACAO_EXTERNA.getId(), e);
		}
	}
	
	/*
	 * 
	 * Caso não tenha registro de Assinatura, processa a data da primeira assinatura com re-processamento do documento
	   antes de tirar o Hash do documento para Assinatura Digital do Hash
	 * 
	 * Data será sempre atualizada caso não tenha registros de assinatura
	 * Documento será reprocessado caso a data de Finalização seja diferente de hoje
	 * 
	 * */

	public void atualizaDataPrimeiraAssinatura(ExDocumento doc, DpPessoa cadastrante, DpPessoa titular) throws Exception {

		Date dataPrimeiraAssinatura = doc.getDtPrimeiraAssinatura();
		if (dataPrimeiraAssinatura == null || doc. getAssinaturasDigitais().isEmpty()) {
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date dataAtualSemTempo = sdf.parse(sdf.format(CpDao.getInstance().dt()));

			if (!dataAtualSemTempo.equals(dataPrimeiraAssinatura) || !dataAtualSemTempo.equals(doc.getDtDoc())) {
				doc.setDtPrimeiraAssinatura(dataAtualSemTempo);  
				
				boolean podePorConfiguracao = new ExPodePorConfiguracao(titular, titular != null ? titular.getLotacao() : null)
				        .withExMod(doc.getExModelo())
				        .withExFormaDoc(doc.getExFormaDocumento())
				        .withIdTpConf(ExTipoDeConfiguracao.ATUALIZAR_DATA_AO_ASSINAR).eval();
				
				if ((Prop.isGovSP() || podePorConfiguracao) && doc.getDtDoc() != null && !dataAtualSemTempo.equals(doc.getDtDoc())) {
				    doc.setDtDoc(dataAtualSemTempo);
					gravar(cadastrante, titular, titular != null ? titular.getLotacao() : null, doc, true);
				}
			}
		}
	}
	
	/*
	 * 
	 * Transfere Documentos Arquivados (Corrente, Permanente e Intermediário) entre Arquivos sem mudar
	 * sua data de temporalidade.
	 * 
	 * @param mob
	 * @param cadastrante
	 * @param lotaCadastrante
	 * @param lotaDestinoFinal
	 * @param descrMov
	 * @throws AplicacaoException
	 * @throws Exception
	 * */
	public void transferirEntreArquivos(ExMobil mob, DpPessoa cadastrante, final DpLotacao lotaCadastrante,
			final DpLotacao lotaDestinoFinal, String descrMov) {
		
		try {
			iniciarAlteracao();
			ExMovimentacao movArquivamentoNova = null;
			ExMovimentacao movArquivadaACancelar = mob.getUltimaMovimentacaoNaoCancelada(ExTipoDeMovimentacao.ARQUIVAMENTO_PERMANENTE);
			if (movArquivadaACancelar == null)
				movArquivadaACancelar = mob.getUltimaMovimentacaoNaoCancelada(ExTipoDeMovimentacao.ARQUIVAMENTO_INTERMEDIARIO);
			
			if (movArquivadaACancelar == null)
				movArquivadaACancelar = mob.getUltimaMovimentacaoNaoCancelada(ExTipoDeMovimentacao.ARQUIVAMENTO_CORRENTE);

			if (movArquivadaACancelar != null) {
				movArquivadaACancelar.setDescrMov(descrMov);
				
				movArquivamentoNova = criarNovaMovimentacao(movArquivadaACancelar.getExTipoMovimentacao(), cadastrante,
						lotaCadastrante, mob, null, null, lotaDestinoFinal, null, null, null);
				
				movArquivamentoNova.setExMovimentacaoRef(movArquivadaACancelar);
				movArquivamentoNova.setLotaResp(lotaDestinoFinal);
				movArquivamentoNova.setResp(null);
				movArquivamentoNova.setSubscritor(cadastrante);
				movArquivamentoNova.setLotaSubscritor(lotaCadastrante);
				
				//movArquivamentoNova.setDtIniMov(movArquivadaACancelar.getDtIniMov());
				movArquivamentoNova.setLotaDestinoFinal(lotaDestinoFinal);
				movArquivamentoNova.setLotaTitular(lotaDestinoFinal);
				
				gravarMovimentacaoCancelamento(movArquivamentoNova, movArquivadaACancelar);
						
				concluirAlteracaoParcial(movArquivamentoNova.getExMobil(), true, movArquivamentoNova.getLotaResp(), movArquivadaACancelar.getLotaResp());
				concluirAlteracao();
			} else {
				throw new AplicacaoException("NÃO foi encontrado nenhuma movimentação do tipo (" + 
						ExTipoDeMovimentacao.ARQUIVAMENTO_PERMANENTE.getDescr() + ", " +
						ExTipoDeMovimentacao.ARQUIVAMENTO_INTERMEDIARIO.getDescr() + " ou " +
						ExTipoDeMovimentacao.ARQUIVAMENTO_CORRENTE.getDescr() + ") para o documento " + mob.doc().getSigla() + "."
				);
			}
		} catch (final AplicacaoException e) {
			throw new AplicacaoException(e.getMessage(), 400, e);
		} catch (final Exception e) {
			cancelarAlteracao();
			throw new RuntimeException("Erro ao arquivar documento em DESTINO.", e);
		}
		
	}
	
	
	public void restringirAcessoAntes(final ExDocumento documento , final DpPessoa pessoaASerAdicionada, DpPessoa cadastrante, final DpLotacao lotaCadastrante) {
		Ex.getInstance().getComp().afirmar("Não é possível restringir acesso", ExPodeRestringirAcesso.class, cadastrante, lotaCadastrante, documento.getMobilGeral());

		List<DpPessoa> listaPessoasRestricaoAcesso = new ArrayList<DpPessoa>();
		listaPessoasRestricaoAcesso.add(pessoaASerAdicionada);
		
		ExNivelAcesso nivelAcesso = dao().consultar(ExNivelAcesso.ID_LIMITADO_ENTRE_PESSOAS, ExNivelAcesso.class, false);

		Ex.getInstance()
				.getBL()
				.restringirAcesso(cadastrante, lotaCadastrante, documento, null, null, null, listaPessoasRestricaoAcesso, null, null, nivelAcesso);
	}
}

