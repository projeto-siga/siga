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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.download.Download;
import br.com.caelum.vraptor.observer.download.InputStreamDownload;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Data;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.base.SigaModal;
import br.gov.jfrj.siga.base.TipoResponsavelEnum;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.CpOrgaoSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExFormaDocumento;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExNivelAcesso;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.ExTipoFormaDoc;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;
import br.gov.jfrj.siga.ex.util.FuncoesEL;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.GenericoSelecao;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.vraptor.builder.ExMobilBuilder;

@Controller
public class ExMobilController extends
		ExSelecionavelController<ExMobil, ExMobilDaoFiltro> {
	private static final String SIGA_DOC_FE_LD = "FE:Ferramentas;LD:Listar Documentos";
	private static final String SIGA_DOC_PESQ_PESQDESCR = "SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;PESQ:Pesquisar;PESQDESCR:Pesquisar descrição";
	private static final String SIGA_DOC_PESQ_PESQDESCR_LIMITADA = "SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;PESQ:Pesquisar;PESQDESCR:Pesquisar descrição;LIMITADA:Pesquisar descrição só se informar outros filtros";
	private static final String SIGA_DOC_PESQ_DTLIMITADA = "SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;PESQ:Pesquisar;DTLIMITADA:Pesquisar somente com data limitada";
	final static public Long MAXIMO_DIAS_PESQUISA = 30L;	
	/**
	 * @deprecated CDI eyes only
	 */
	public ExMobilController() {
		super();
	}

	@Inject
	public ExMobilController(HttpServletRequest request, Result result,
			SigaObjects so, EntityManager em) {
		super(request, result, ExDao.getInstance(), so, em);
		setItemPagina(50);
	}

	@Transacional
	@Get("app/expediente/doc/marcar")
	public void aMarcar() {
		assertAcesso("");
		
		String sigla = param("sigla");
		String[] siglasSparadas = sigla.split(";");
		for (String s : siglasSparadas) {
			final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
			filter.setSigla(s);
			ExMobil mob = (ExMobil) dao().consultarPorSigla(filter);
			ExDocumento doque = mob.getExDocumento();
			Ex.getInstance().getBL().marcar(doque);
		}
		result.redirectTo("/app/expediente/doc/finalizou_rotina");
	}

	@Get("app/expediente/doc/finalizou_rotina")
	public void aFinalizouRotina() {
//		System.out.println("Finalizou rotina");
	}

	@Get("app/expediente/buscar")
	public void aBuscar(final String sigla, final String popup, final String primeiraVez, final String propriedade, final Integer postback,
			final int apenasRefresh, final Long ultMovIdEstadoDoc, final int ordem, final int visualizacao, final Integer ultMovTipoResp,
			final DpPessoaSelecao ultMovRespSel, final DpLotacaoSelecao ultMovLotaRespSel, final Long orgaoUsu, final Long idTpDoc, final String dtDocString,
			final String dtDocFinalString, final Long idTipoFormaDoc, final Long idFormaDoc, final Long idMod, final String anoEmissaoString,
			final String numExpediente, final String numExtDoc, final CpOrgaoSelecao cpOrgaoSel, final String numAntigoDoc,
			final DpPessoaSelecao subscritorSel, String nmSubscritorExt, final Integer tipoCadastrante, final DpPessoaSelecao cadastranteSel,
			final DpLotacaoSelecao lotaCadastranteSel, final Integer tipoDestinatario, final DpPessoaSelecao destinatarioSel,
			final DpLotacaoSelecao lotacaoDestinatarioSel, final CpOrgaoSelecao orgaoExternoDestinatarioSel, final String nmDestinatario,
			final ExClassificacaoSelecao classificacaoSel, final String descrDocumento, final String fullText, final Long ultMovEstadoDoc,
			final Integer offset) {
		assertAcesso("");
		
		getP().setOffset(offset);
		this.setSigla(sigla);
		this.setPostback(postback);

		final ExMobilBuilder builder = ExMobilBuilder.novaInstancia();

		builder.setPostback(postback).setUltMovTipoResp(ultMovTipoResp)
				.setUltMovRespSel(ultMovRespSel)
				.setUltMovLotaRespSel(ultMovLotaRespSel).setOrgaoUsu(orgaoUsu)
				.setIdTpDoc(idTpDoc).setCpOrgaoSel(cpOrgaoSel)
				.setSubscritorSel(subscritorSel)
				.setTipoCadastrante(tipoCadastrante)
				.setCadastranteSel(cadastranteSel)
				.setLotaCadastranteSel(lotaCadastranteSel)
				.setTipoDestinatario(tipoDestinatario)
				.setDestinatarioSel(destinatarioSel)
				.setLotacaoDestinatarioSel(lotacaoDestinatarioSel)
				.setOrgaoExternoDestinatarioSel(orgaoExternoDestinatarioSel)
				.setClassificacaoSel(classificacaoSel).setOffset(offset);

		builder.processar(getLotaTitular());

		String dtDoc = dtDocString;

		if ((SigaMessages.isSigaSP() && offset != null) || (!SigaMessages.isSigaSP() && (primeiraVez == null || !primeiraVez.equals("sim")))) {
			dtDoc = listarItensPesquisa(dtDocString, dtDocFinalString, builder, dtDoc);				
		} else {
			if( Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getTitular(), getLotaTitular(),
					SIGA_DOC_PESQ_DTLIMITADA )) {
				dtDoc = FuncoesEL.calculaDiasAPartirDeHoje(-MAXIMO_DIAS_PESQUISA);
        		result.include("msgCabecClass", "alert-warning");
        		result.include("mensagemCabec", "ATENÇÃO: A pesquisa deve ser limitada com uma range de datas de no máximo "
        				+ MAXIMO_DIAS_PESQUISA.toString() + " dias. Será assumida uma data inicial 30 dias anterior à hoje no campo Data Inicial.");
			}
		}

		result.include("primeiraVez", primeiraVez);
		result.include("popup", true);
		result.include("apenasRefresh", apenasRefresh);
		result.include("estados", this.getEstados(ultMovIdEstadoDoc));
		result.include("listaOrdem", this.getListaOrdem());
		result.include("ordem", ordem);
		result.include("listaVisualizacao", this.getListaVisualizacao());
		result.include("visualizacao", visualizacao);
		result.include("listaTipoResp", this.getListaTipoResp());
		result.include("ultMovTipoResp", builder.getUltMovTipoResp());
		result.include("ultMovRespSel", builder.getUltMovRespSel());
		result.include("orgaoUsu", builder.getOrgaoUsu());
		result.include("orgaosUsu", this.getOrgaosUsu());
		result.include("tiposDocumento", this.getTiposDocumentoParaConsulta());
		result.include("idTpDoc", builder.getIdTpDoc());
		result.include("dtDocString", dtDoc);
		result.include("dtDocFinalString", dtDocFinalString);
		result.include("tiposFormaDoc", this.getTiposFormaDoc());
		result.include("anoEmissaoString", anoEmissaoString);
		result.include("listaAnos", this.getListaAnos());
		result.include("numExpediente", numExpediente);
		result.include("numExtDoc", numExtDoc);
		result.include("cpOrgaoSel", builder.getCpOrgaoSel());
		result.include("numAntigoDoc", numAntigoDoc);
		result.include("nmSubscritorExt", nmSubscritorExt);
		result.include("subscritorSel", builder.getSubscritorSel());
		result.include("listaTipoResp", this.getListaTipoResp());
		result.include("tipoCadastrante", builder.getTipoCadastrante());
		result.include("cadastranteSel", builder.getCadastranteSel());
		result.include("lotaCadastranteSel", builder.getLotaCadastranteSel());
		result.include("orgaoExternoDestinatarioSel",
				builder.getOrgaoExternoDestinatarioSel());
		result.include("listaTipoDest", this.getListaTipoDest());
		result.include("tipoDestinatario", builder.getTipoDestinatario());
		result.include("destinatarioSel", builder.getDestinatarioSel());
		result.include("lotacaoDestinatarioSel",
				builder.getLotacaoDestinatarioSel());
		result.include("nmDestinatario", nmDestinatario);
		result.include("descrDocumento", descrDocumento);
		result.include("visualizacao", visualizacao);
		result.include("itemPagina", this.getItemPagina());
		result.include("tamanho", this.getTamanho());
		result.include("itens", this.getItens());
		result.include("classificacaoSel", builder.getClassificacaoSel());
		result.include("ultMovLotaRespSel", builder.getUltMovLotaRespSel());
		result.include("ultMovEstadoDoc", ultMovIdEstadoDoc);
		result.include("paramoffset", builder.getOffset());
		result.include("sigla", this.getSigla());
		result.include("propriedade", propriedade);
		result.include("currentPageNumber", calculaPaginaAtual(offset));
		result.include("idTipoFormaDoc", idTipoFormaDoc);
		result.include("idFormaDoc", idFormaDoc);
		result.include("idMod", idMod);		
	}

	private List<ExTipoDocumento> getTiposDocumentoParaConsulta() {
		List<ExTipoDocumento> l = dao().listarExTiposDocumento();
		if ("inativa".equals(Prop.get("folha.de.rosto"))) {
			List<ExTipoDocumento> l2 = new ArrayList<>();
			for (ExTipoDocumento i : l) {
				if (i.getId() == ExTipoDocumento.TIPO_DOCUMENTO_INTERNO_FOLHA_DE_ROSTO || i.getId() == ExTipoDocumento.TIPO_DOCUMENTO_EXTERNO_FOLHA_DE_ROSTO)
					continue;
				l2.add(i);
			}
			l = l2;
		}
		return l;
	}
	
	@Post
	@Path("app/expediente/doc/exportarCsv")
	public Download exportarCsv(final String popup, final String primeiraVez, final String propriedade, final Integer postback, final int apenasRefresh,
			final Long ultMovIdEstadoDoc, final int ordem, final int visualizacao, final Integer ultMovTipoResp, final DpPessoaSelecao ultMovRespSel,
			final DpLotacaoSelecao ultMovLotaRespSel, final Long orgaoUsu, final Long idTpDoc, final String dtDocString, final String dtDocFinalString,
			final Long idTipoFormaDoc, final Long idFormaDoc, final Long idMod, final String anoEmissaoString, final String numExpediente,
			final String numExtDoc, final CpOrgaoSelecao cpOrgaoSel, final String numAntigoDoc, final DpPessoaSelecao subscritorSel, String nmSubscritorExt,
			final Integer tipoCadastrante, final DpPessoaSelecao cadastranteSel, final DpLotacaoSelecao lotaCadastranteSel, final Integer tipoDestinatario,
			final DpPessoaSelecao destinatarioSel, final DpLotacaoSelecao lotacaoDestinatarioSel, final CpOrgaoSelecao orgaoExternoDestinatarioSel,
			final String nmDestinatario, final ExClassificacaoSelecao classificacaoSel, final String descrDocumento, final String fullText,
			final Long ultMovEstadoDoc, final Integer paramoffset) throws UnsupportedEncodingException {
			
		
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		try {
			final ExMobilDaoFiltro flt = createDaoFiltro();
			//long tempoIni = System.currentTimeMillis();
			setTamanho(dao().consultarQuantidadePorFiltroOtimizado(flt,getTitular(), getLotaTitular()));
			
			
			LocalDate dtIni = null;
			LocalDate dtFinal = null;
			LocalDate dataAtual = LocalDate.now();
			
			if (getTamanho() > 50000) {		
				if (!"".equals(param("dtDocString"))) {
					if (Data.validaDDMMYYYY(param("dtDocString"))) {
						dtIni = LocalDate.parse(param("dtDocString"), formatter);
					} else {
						throw new RegraNegocioException("Data Inicial inválida.");
					}
				} else {
					throw new RegraNegocioException("Data Inicial não informada. Para grandes volumes, período para exportação não deve ser superior à 90 dias.");
				}
				
				if (!"".equals(param("dtDocFinalString"))) {
					if (Data.validaDDMMYYYY(param("dtDocFinalString"))) {
						dtFinal = LocalDate.parse(param("dtDocFinalString"), formatter);
						if (ChronoUnit.DAYS.between(dtIni, dtFinal) > 90) {
							throw new RegraNegocioException("Para grandes volumes, período para exportação não deve ser superior à 90 dias.");
						}	
					} else {
						throw new RegraNegocioException("Data Final inválida.");
					}
				} else {
					if (ChronoUnit.DAYS.between(dtIni, dataAtual) > 90) {
						throw new RegraNegocioException("Para grandes volumes, período para exportação não deve ser superior à 90 dias. Informe a Data Final.");
					}	
				}
			
			}
		
			getP().setOffset(paramoffset);
			this.setPostback(postback);
	
			final ExMobilBuilder builder = ExMobilBuilder.novaInstancia();
	
			builder.setPostback(postback).setUltMovTipoResp(ultMovTipoResp)
					.setUltMovRespSel(ultMovRespSel)
					.setUltMovLotaRespSel(ultMovLotaRespSel).setOrgaoUsu(orgaoUsu)
					.setIdTpDoc(idTpDoc).setCpOrgaoSel(cpOrgaoSel)
					.setSubscritorSel(subscritorSel)
					.setTipoCadastrante(tipoCadastrante)
					.setCadastranteSel(cadastranteSel)
					.setLotaCadastranteSel(lotaCadastranteSel)
					.setTipoDestinatario(tipoDestinatario)
					.setDestinatarioSel(destinatarioSel)
					.setLotacaoDestinatarioSel(lotacaoDestinatarioSel)
					.setOrgaoExternoDestinatarioSel(orgaoExternoDestinatarioSel)
					.setClassificacaoSel(classificacaoSel).setOffset(paramoffset);
	
			builder.processar(getLotaTitular());
			
			if (getTamanho() > 200000) {
				throw new RegraNegocioException("Numero máximo de registros para exportação excedido. Use os filtros para restringir resultado.");
			}
			
			List lista = dao().consultarPorFiltroOtimizado(flt,
					builder.getOffset(), -1, getTitular(),
					getLotaTitular());
	
			InputStream inputStream = null;
			StringBuffer texto = new StringBuffer();
			texto.append(";Responsável pela Assinatura;;;Responsável pela situação atual" + System.lineSeparator());
			texto.append("Número;Unidade;Usuário;Data;Unidade;Usuário;Data;Situação;Documento;Descrição" + System.lineSeparator());
			
			
			ExDocumento e = new ExDocumento();
			ExMobil m = new ExMobil();
			ExMarca ma = new ExMarca();
			String descricao = "";
			String marcadorFormatado = "";
			
			for (Object object : lista) {
				e = (ExDocumento)(((Object[])object)[0]);
				m = (ExMobil)(((Object[])object)[1]);
				ma = (ExMarca)(((Object[])object)[2]);
				
						
				texto.append(m.getCodigo()+";");
				if(e.getLotaSubscritor() != null && e.getLotaSubscritor().getSigla() != null) {
					texto.append(e.getLotaSubscritor().getSigla().replaceAll(";",","));
				}
				texto.append(";");
				
				if(e.getSubscritor() != null && e.getSubscritor().getIniciais() != null) {
					texto.append(e.getSubscritor().getIniciais().replaceAll(";",","));
				}
				texto.append(";");
				
				if(e.getDtDocDDMMYY() != null) {
					texto.append(e.getDtDocDDMMYY());
				}
				texto.append(";");
				
				/*if(ma.getDpLotacaoIni() != null && ma.getDpLotacaoIni().getLotacaoAtual() != null && ma.getDpLotacaoIni().getLotacaoAtual().getSigla() != null) {
					texto.append(ma.getDpLotacaoIni().getLotacaoAtual().getSigla().replaceAll(";",","));
				}*/
				
						
				if(ma.getDpLotacaoIni() != null && ma.getDpLotacaoIni().getSigla() != null) {
					texto.append(ma.getDpLotacaoIni().getSigla().replaceAll(";",","));
				}
				texto.append(";");
				
				if(ma.getDpPessoaIni() != null && ma.getDpPessoaIni().getIniciais() != null) {
					texto.append(ma.getDpPessoaIni().getIniciais().replaceAll(";",","));
				}
				texto.append(";");
				
				if(ma.getDtIniMarcaDDMMYYYY() != null) {
					texto.append(ma.getDtIniMarcaDDMMYYYY());
				}
				texto.append(";");

				if(ma != null && ma.getCpMarcador() != null && ma.getCpMarcador().getDescrMarcador() != null) {
					marcadorFormatado = ma.getDescricaoMarcadorFormatadoComData().replaceAll(";",",");
					texto.append(marcadorFormatado);
				}
				texto.append(";");
				
				if(e.getNmMod() != null) {
					texto.append(e.getNmMod().replaceAll(";",","));
				}
				texto.append(";");
				
				if(Prop.isGovSP()) {
					descricao = e.getDescrDocumento();
				} else {
					Ex.getInstance().getBL();
					descricao = ExBL.descricaoSePuderAcessar(e, getTitular(), getTitular().getLotacao());
				}
				if(descricao != null) {
					texto.append(descricao.replaceAll("\n", "").replaceAll("\t", "").replaceAll("\r","").replaceAll(";",","));
				}
				
				texto.append(";");
				texto.append(System.lineSeparator());
			}
			inputStream = new ByteArrayInputStream(texto.toString().getBytes("ISO-8859-1"));
			
			return new InputStreamDownload(inputStream, "text/csv", "documentos.csv");	

		} catch (final RegraNegocioException e) {
			result.include(SigaModal.ALERTA, SigaModal.mensagem(e.getMessage()));
			result.forwardTo(this).aListar(popup, primeiraVez, propriedade, postback, apenasRefresh, ultMovIdEstadoDoc, ordem, visualizacao, ultMovTipoResp, ultMovRespSel, ultMovLotaRespSel, orgaoUsu, idTpDoc, dtDocString, dtDocFinalString, idTipoFormaDoc, idFormaDoc, idMod, anoEmissaoString, numExpediente, numExtDoc, cpOrgaoSel, numAntigoDoc, subscritorSel, nmSubscritorExt, tipoCadastrante, cadastranteSel, lotaCadastranteSel, tipoDestinatario, destinatarioSel, lotacaoDestinatarioSel, orgaoExternoDestinatarioSel, nmDestinatario, classificacaoSel, descrDocumento, fullText, ultMovEstadoDoc, paramoffset);
		}
		return null;	
	}

	@Get("app/expediente/doc/listar")
	public void aListar(final String popup,  final String primeiraVez, final String propriedade, final Integer postback, final int apenasRefresh,
			final Long ultMovIdEstadoDoc, final int ordem, final int visualizacao, final Integer ultMovTipoResp, final DpPessoaSelecao ultMovRespSel,
			final DpLotacaoSelecao ultMovLotaRespSel, final Long orgaoUsu, final Long idTpDoc, final String dtDocString, final String dtDocFinalString,
			final Long idTipoFormaDoc, final Long idFormaDoc, final Long idMod, final String anoEmissaoString, final String numExpediente,
			final String numExtDoc, final CpOrgaoSelecao cpOrgaoSel, final String numAntigoDoc, final DpPessoaSelecao subscritorSel, String nmSubscritorExt,
			final Integer tipoCadastrante, final DpPessoaSelecao cadastranteSel, final DpLotacaoSelecao lotaCadastranteSel, final Integer tipoDestinatario,
			final DpPessoaSelecao destinatarioSel, final DpLotacaoSelecao lotacaoDestinatarioSel, final CpOrgaoSelecao orgaoExternoDestinatarioSel,
			final String nmDestinatario, final ExClassificacaoSelecao classificacaoSel, final String descrDocumento, final String fullText,
			final Long ultMovEstadoDoc, final Integer paramoffset) {
		assertAcesso("");

		getP().setOffset(paramoffset);
		this.setPostback(postback);

		final ExMobilBuilder builder = ExMobilBuilder.novaInstancia();

		builder.setPostback(postback).setUltMovTipoResp(ultMovTipoResp)
				.setUltMovRespSel(ultMovRespSel)
				.setUltMovLotaRespSel(ultMovLotaRespSel).setOrgaoUsu(orgaoUsu)
				.setIdTpDoc(idTpDoc).setCpOrgaoSel(cpOrgaoSel)
				.setSubscritorSel(subscritorSel)
				.setTipoCadastrante(tipoCadastrante)
				.setCadastranteSel(cadastranteSel)
				.setLotaCadastranteSel(lotaCadastranteSel)
				.setTipoDestinatario(tipoDestinatario)
				.setDestinatarioSel(destinatarioSel)
				.setLotacaoDestinatarioSel(lotacaoDestinatarioSel)
				.setOrgaoExternoDestinatarioSel(orgaoExternoDestinatarioSel)
				.setClassificacaoSel(classificacaoSel).setOffset(paramoffset);

		builder.processar(getLotaTitular());

		String dtDoc = dtDocString;
	
		if (primeiraVez == null || !primeiraVez.equals("sim")) {
				dtDoc = listarItensPesquisa(dtDocString, dtDocFinalString, builder, dtDoc);				
		} else {
			if( Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getTitular(), getLotaTitular(),
					SIGA_DOC_PESQ_DTLIMITADA )) {
				dtDoc = FuncoesEL.calculaDiasAPartirDeHoje(-MAXIMO_DIAS_PESQUISA);
        		result.include("msgCabecClass", "alert-warning");
        		result.include("mensagemCabec", "ATENÇÃO: A pesquisa deve ser limitada com uma range de datas de no máximo "
        				+ MAXIMO_DIAS_PESQUISA.toString() + " dias. Será assumida uma data inicial 30 dias anterior à hoje no campo Data Inicial.");
			}
		}

		result.include("primeiraVez", primeiraVez);
		result.include("popup", popup);
		result.include("apenasRefresh", apenasRefresh);
		result.include("estados", this.getEstados(ultMovIdEstadoDoc));
		result.include("listaOrdem", this.getListaOrdem());
		result.include("ordem", ordem);
		result.include("listaVisualizacao", this.getListaVisualizacao());
		result.include("visualizacao", visualizacao);
		result.include("listaTipoResp", this.getListaTipoResp());
		result.include("ultMovTipoResp", builder.getUltMovTipoResp());
		result.include("ultMovRespSel", builder.getUltMovRespSel());
		result.include("orgaoUsu", builder.getOrgaoUsu());
		result.include("orgaosUsu", this.getOrgaosUsu());
		result.include("tiposDocumento", this.getTiposDocumentoParaConsulta());
		result.include("idTpDoc", builder.getIdTpDoc());
		result.include("dtDocString", dtDoc);
		result.include("dtDocFinalString", dtDocFinalString);
		result.include("tiposFormaDoc", this.getTiposFormaDoc());
		result.include("anoEmissaoString", anoEmissaoString);
		result.include("listaAnos", this.getListaAnos());
		result.include("numExpediente", numExpediente);
		result.include("numExtDoc", numExtDoc);
		result.include("cpOrgaoSel", builder.getCpOrgaoSel());
		result.include("numAntigoDoc", numAntigoDoc);
		result.include("nmSubscritorExt", nmSubscritorExt);
		result.include("subscritorSel", builder.getSubscritorSel());
		result.include("listaTipoResp", this.getListaTipoResp());
		result.include("tipoCadastrante", builder.getTipoCadastrante());
		result.include("cadastranteSel", builder.getCadastranteSel());
		result.include("lotaCadastranteSel", builder.getLotaCadastranteSel());
		result.include("orgaoExternoDestinatarioSel",
				builder.getOrgaoExternoDestinatarioSel());
		result.include("listaTipoDest", this.getListaTipoDest());
		result.include("tipoDestinatario", builder.getTipoDestinatario());
		result.include("destinatarioSel", builder.getDestinatarioSel());
		result.include("lotacaoDestinatarioSel",
				builder.getLotacaoDestinatarioSel());
		result.include("nmDestinatario", nmDestinatario);
		result.include("descrDocumento", descrDocumento);
		result.include("visualizacao", visualizacao);
		result.include("itemPagina", this.getItemPagina());
		result.include("tamanho", this.getTamanho());
		result.include("itens", this.getItens());
		result.include("classificacaoSel", builder.getClassificacaoSel());
		result.include("ultMovLotaRespSel", builder.getUltMovLotaRespSel());
		result.include("ultMovIdEstadoDoc", ultMovIdEstadoDoc);
		result.include("fullText", fullText);
		result.include("currentPageNumber", calculaPaginaAtual(paramoffset));
		result.include("idTipoFormaDoc", idTipoFormaDoc);
		result.include("idFormaDoc", idFormaDoc);
		result.include("idMod", idMod);		

		if (visualizacao == 3 || visualizacao == 4) {
			TreeMap campos = new TreeMap<String, String>();
			for (Object[] oa : (List<Object[]>) this.getItens()) {
				for (String s : ((ExDocumento) oa[0]).getForm().keySet()) {
					Object nomeCampo = preprocessarNomeCampo(s);
					if (nomeCampo != null)
						campos.put(s, nomeCampo);
				}
			}
			result.include("campos", campos);
		}

	}

	private String listarItensPesquisa(final String dtDocString, final String dtDocFinalString,
			final ExMobilBuilder builder, String dtDoc) {
		final ExMobilDaoFiltro flt = createDaoFiltro();
		if (Prop.isGovSP() && flt.getDescrDocumento() != null 
				&& !"".equals(flt.getDescrDocumento())) {
			if (!(Cp.getInstance().getConf() 
				.podeUtilizarServicoPorConfiguracao(getTitular(), getLotaTitular(), SIGA_DOC_PESQ_PESQDESCR))) {
				result.include(SigaModal.ALERTA, SigaModal.mensagem("Usuário não autorizado a pesquisar pela descrição do documento."));
				return dtDocString;
			}
			if ((Cp.getInstance().getConf() 
					.podeUtilizarServicoPorConfiguracao(getTitular(), getLotaTitular(), SIGA_DOC_PESQ_PESQDESCR_LIMITADA))
					&& !(flt.getIdOrgaoUsu() != null && flt.getIdOrgaoUsu() != 0
					&& flt.getAnoEmissao() != null && flt.getAnoEmissao() != 0
					&& flt.getIdFormaDoc() != null && flt.getIdFormaDoc() != 0)) {
					result.include(SigaModal.ALERTA, SigaModal.mensagem("Usuário autorizado a pesquisar pela Descrição somente após o preenchimento dos campos Órgão, Espécie, Documento e Ano de Emissão."));
					return dtDocString;
				}
		}
		
		LocalDate dt = null;
		if ((param("dtDocString") == null || "".equals(param("dtDocString"))) 
				&& Cp.getInstance().getConf()
					.podeUtilizarServicoPorConfiguracao(getTitular(), getLotaTitular(), SIGA_DOC_PESQ_DTLIMITADA )) {
			result.include("msgCabecClass", "alert-warning");
			result.include("mensagemCabec", "ATENÇÃO: A pesquisa deve ser limitada com uma range de datas de no máximo "
					+ MAXIMO_DIAS_PESQUISA.toString() + " dias. Foi assumida uma data inicial 30 dias anterior à hoje.");
			dt = LocalDate.now().plusDays(-MAXIMO_DIAS_PESQUISA);
			flt.setDtDoc(Date.from(dt.atStartOfDay(ZoneId.systemDefault()).toInstant()));
			dtDoc = FuncoesEL.calculaDiasAPartirDeHoje(-MAXIMO_DIAS_PESQUISA);
		}

		try {
			if(Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getTitular(), 
					getLotaTitular(), SIGA_DOC_PESQ_DTLIMITADA ) && dt == null) 
				validarLimiteDeDatas(dtDocString, dtDocFinalString);
//					long tempoIni = System.currentTimeMillis();
//					System.out.println("Consulta dos por filtro: "
//							+ (System.currentTimeMillis() - tempoIni));
			setItens(dao().consultarPorFiltroOtimizado(flt,
					builder.getOffset(), getItemPagina() + (Prop.isGovSP() ? 1 : 0), getTitular(),
					getLotaTitular()));
			if(Prop.isGovSP()) {
				setTamanho(getItens().size());
			} else {
				setTamanho(dao().consultarQuantidadePorFiltroOtimizado(flt,
						getTitular(), getLotaTitular()));
			}
		} catch (RegraNegocioException e) {
			result.include("msgCabecClass", "alert-danger");
			result.include("mensagemCabec", e.getMessage());
		}
		return dtDoc;
	}

	private void validarLimiteDeDatas(final String dtDocString, final String dtDocFinalString) {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate dtIni = null;
		LocalDate dtFinal = null;
		LocalDate dataAtual = LocalDate.now();
		
		if (dtDocString != null && !"".equals(dtDocString)) {
			if (Data.validaDDMMYYYY(dtDocString)) {
				dtIni = LocalDate.parse(dtDocString, formatter);
			} else {
				throw new RegraNegocioException("Data Inicial inválida.");
			}
		} else {
			throw new RegraNegocioException("Data Inicial não informada. Para grandes volumes, período para pesquisa não deve ser superior à "
					+ MAXIMO_DIAS_PESQUISA.toString() + " dias.");
		}
		
		if (dtDocFinalString != null && !"".equals(dtDocFinalString)) {
			if (Data.validaDDMMYYYY(dtDocFinalString)) {
				dtFinal = LocalDate.parse(dtDocFinalString, formatter);
				if (ChronoUnit.DAYS.between(dtIni, dtFinal) > MAXIMO_DIAS_PESQUISA) {
					throw new RegraNegocioException("Para grandes volumes, período para pesquisa não deve ser superior a "
							+ MAXIMO_DIAS_PESQUISA.toString() + " dias. Informe a Data Inicial e/ou Final.");
				}	
			} else {
				throw new RegraNegocioException("Data Final inválida.");
			}
		} else {
			if (ChronoUnit.DAYS.between(dtIni, dataAtual) > MAXIMO_DIAS_PESQUISA) {
				throw new RegraNegocioException("Para grandes volumes, período para exportação não deve ser superior a "
						+ MAXIMO_DIAS_PESQUISA.toString() + " dias. Informe a Data Inicial e/ou Final.");
			}	
		}
	}

	final static Pattern preprocessadorPatternSelecao = Pattern
			.compile("^([\\w_]+)(_[a-z]+Sel.)([\\w_]+)$");

	private Object preprocessarNomeCampo(String s) {
		Matcher m = preprocessadorPatternSelecao.matcher(s);
		if (m.find())
			s = m.replaceFirst("$1 $3");
		if (s.endsWith(" id"))
			return null;
		return s;
	}
	
	protected ExClassificacao daoClassificacao(long id) {
		return dao().consultar(id, ExClassificacao.class, false);
	}

	@Override
	protected ExMobilDaoFiltro createDaoFiltro() {
		final ExMobilDaoFiltro flt = new ExMobilDaoFiltro();

		if (flt.getIdOrgaoUsu() == null || flt.getIdOrgaoUsu() == 0) {
			if (param("matricula") != null) {
				final DpPessoa pes = daoPes(param("matricula"));
				flt.setIdOrgaoUsu(pes.getOrgaoUsuario().getId());
			}
		}

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			String dt = param("dtDocString");
			if (dt != null && !dt.equals("")) {
				dt = dt + " 00:00:00";
			}

			flt.setDtDoc(df.parse(dt));
		} catch (final ParseException e) {
			flt.setDtDoc(null);
		} catch (final NullPointerException e) {
			flt.setDtDoc(null);
		}

		try {
			String dt = param("dtDocFinalString");
			if (dt != null && !dt.equals("")) {
				dt = dt + " 23:59:59";
			}

			flt.setDtDocFinal(df.parse(dt));
		} catch (final ParseException e) {
			flt.setDtDocFinal(null);
		} catch (final NullPointerException e) {
			flt.setDtDocFinal(null);
		}

		flt.setAnoEmissao(paramLong("anoEmissaoString"));
		flt.setClassificacaoSelId(paramLong("classificacaoSel.id"));
		if (flt.getClassificacaoSelId() != null)
			flt.setClassificacaoSelId((daoClassificacao(flt.getClassificacaoSelId()))
					.getIdInicial());
		flt.setDescrDocumento(Texto
				.removeAcentoMaiusculas(param("descrDocumento")));
		String paramFullText = param("fullText");
		if (paramFullText != null) {
			paramFullText = paramFullText.trim();
			paramFullText = paramFullText.replace("\"", "");
		}
		flt.setFullText(paramFullText);
		flt.setDestinatarioSelId(paramLong("destinatarioSel.id"));
		if (flt.getDestinatarioSelId() != null) {
			flt.setDestinatarioSelId((daoPes(flt.getDestinatarioSelId()))
					.getIdInicial());
		}
		flt.setIdFormaDoc(paramLong("idFormaDoc"));
		flt.setIdTipoFormaDoc(paramLong("idTipoFormaDoc"));
		flt.setIdTpDoc(paramInteger("idTpDoc"));
		flt.setLotacaoDestinatarioSelId(paramLong("lotacaoDestinatarioSel.id"));
		if (flt.getLotacaoDestinatarioSelId() != null) {
			flt.setLotacaoDestinatarioSelId((daoLot(flt
					.getLotacaoDestinatarioSelId())).getIdInicial());
		}
		if (param("nmDestinatario") != null) {
			flt.setNmDestinatario(param("nmDestinatario").toUpperCase());
		}
		flt.setNmSubscritorExt(param("nmSubscritorExt"));
		flt.setNumExpediente(paramLong("numExpediente"));
		flt.setNumExtDoc(param("numExtDoc"));
		flt.setNumAntigoDoc(param("numAntigoDoc"));
		flt.setOrgaoExternoSelId(paramLong("cpOrgaoSel.id"));
		flt.setOrgaoExternoDestinatarioSelId(paramLong("orgaoExternoDestinatarioSel.id"));
		flt.setSubscritorSelId(paramLong("subscritorSel.id"));
		if (flt.getSubscritorSelId() != null) {
			flt.setSubscritorSelId((daoPes(flt.getSubscritorSelId()))
					.getIdInicial());
		}

		flt.setLotaCadastranteSelId(paramLong("lotaCadastranteSel.id"));
		if (flt.getLotaCadastranteSelId() != null) {
			flt.setLotaCadastranteSelId((daoLot(flt.getLotaCadastranteSelId()))
					.getIdInicial());
		}
		flt.setCadastranteSelId(paramLong("cadastranteSel.id"));
		if (flt.getCadastranteSelId() != null) {
			flt.setCadastranteSelId((daoPes(flt.getCadastranteSelId()))
					.getIdInicial());
		}

		flt.setUltMovIdEstadoDoc(paramLong("ultMovIdEstadoDoc"));
		flt.setUltMovLotaRespSelId(paramLong("ultMovLotaRespSel.id"));
		if (flt.getUltMovLotaRespSelId() != null)
			flt.setUltMovLotaRespSelId((daoLot(flt.getUltMovLotaRespSelId()))
					.getIdInicial());
		flt.setUltMovRespSelId(paramLong("ultMovRespSel.id"));
		if (flt.getUltMovRespSelId() != null) {
			flt.setUltMovRespSelId((daoPes(flt.getUltMovRespSelId()))
					.getIdInicial());
		}

		flt.setNumSequencia(paramInteger("numVia"));
		if (getCadastrante() != null) {
			flt.setLotacaoCadastranteAtualId(getCadastrante().getLotacao()
					.getIdInicial());
		}

		if (paramLong("orgaoUsu") != null) {
			flt.setIdOrgaoUsu(paramLong("orgaoUsu"));
		}
		if (flt.getIdOrgaoUsu() == null && getLotaTitular() != null) {
			flt.setIdOrgaoUsu(getLotaTitular().getOrgaoUsuario()
					.getIdOrgaoUsu());
		}
		flt.setIdMod(paramLong("idMod"));
		flt.setOrdem(paramInteger("ordem"));

		return flt;
	}

	@Get("app/expediente/doc/carregar_lista_formas")
	public void aCarregarListaFormas(Long tipoForma, Long idFormaDoc) {
		result.include("todasFormasDocPorTipoForma",
				this.getTodasFormasDocPorTipoForma(tipoForma));
		result.include("idFormaDoc", idFormaDoc);
	}

	@Get("app/expediente/doc/carregar_lista_modelos")
	public void aCarregarListaModelos(final Long forma, final Long idMod) {
		result.include("modelos", this.getModelos(forma));
		result.include("idMod", idMod);
	}

	private List<String> getListaAnos() {
		final ArrayList<String> lst = new ArrayList<String>();
		final Calendar cal = Calendar.getInstance();
		
		Integer anoAux = 1990;
		if(Prop.isGovSP()) {
			anoAux = 2018;
		}
		
		for (Integer ano = cal.get(Calendar.YEAR); ano >= anoAux; ano--) {
			lst.add(ano.toString());
		}
		return lst;
	}

	private Map<Integer, String> getListaOrdem() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(0, "Data do documento");
		map.put(1, "Data da situação");
		map.put(2, "Ano e número");
		map.put(3, "Data de finalização");
		map.put(4, "Data de criação do temporário");
		return map;
	}

	private Map<Integer, String> getListaVisualizacao() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(0, "Normal");
		map.put(1, "Última anotação");
		map.put(2, "Tabela dinâmica");
		map.put(3, "Normal com entrevista");
		map.put(4, "Tabela dinâmica com entrevista");
		return map;
	}

	private Map<Integer, String> getListaTipoDest() {
		return TipoResponsavelEnum.getLista();
	}

	private List<ExFormaDocumento> getTodasFormasDocPorTipoForma(
			final Long idTipoFormaDoc) {
		final ExBL bl = Ex.getInstance().getBL();
		ExTipoFormaDoc tipoForma = null;
		if (idTipoFormaDoc != null && !idTipoFormaDoc.equals(0L)) {
			tipoForma = dao().consultar(idTipoFormaDoc, ExTipoFormaDoc.class,
					false);
		}

		final List<ExFormaDocumento> formasDoc = new ArrayList<ExFormaDocumento>();
		if (Prop.get("/siga.pesquisa.confere.modelos").equals("true")) {
			formasDoc.addAll(bl.obterFormasDocumento(bl.obterListaModelos(null, null,
					false, false, null, null, false, getTitular(), getLotaTitular(), false),
					null, tipoForma));
		} else {
			formasDoc.addAll(dao().listarPorEspecieOrdenarPorSigla(tipoForma));
		}
		return formasDoc;
	}

	private List<ExTipoFormaDoc> getTiposFormaDoc() {
		return dao().listarExTiposFormaDoc();
	}

	private List<ExModelo> getModelos(final Long idFormaDoc) {
		ExFormaDocumento forma = null;
		if (idFormaDoc != null && idFormaDoc != 0) {
			forma = dao().consultar(idFormaDoc, ExFormaDocumento.class, false);
		}

		return Ex
				.getInstance()
				.getBL()
				.obterListaModelos(null, forma, false, false, null, "Todos", false, getTitular(),
						getLotaTitular(), false);
	}

	@Get({"public/app/expediente/selecionar","app/expediente/selecionar","/expediente/selecionar.action"})
	public void selecionar(final String sigla, final String matricula) throws Exception {
		String resultado = super.aSelecionar(sigla);
		if (getSel() != null && matricula != null) {
			GenericoSelecao sel = new GenericoSelecao();
			sel.setId(getSel().getId());
			sel.setSigla(getSel().getSigla());
			sel.setDescricao("/sigaex/app/expediente/doc/exibir?sigla="
					+ sel.getSigla());
			setSel(sel);
		}
		if (resultado == "ajax_retorno") {
			result.include("sel", getSel());
			result.use(Results.page()).forwardTo(
					"/WEB-INF/jsp/ajax_retorno.jsp");
		} else {
			result.use(Results.page()).forwardTo("/WEB-INF/jsp/ajax_vazio.jsp");
		}
	}

	@Override
	public Selecionavel selecionarVerificar(Selecionavel sel)
			throws AplicacaoException {

		ExMobil mob = (ExMobil) sel;

		if (mob.doc() == null)
			return null;

		//Edson: Se a via, volume ou documento inteiro tiver sido eliminado(a), não retorna nada.
		if (mob.isEliminado())
			return null;

		// Se for uma via ou volume, retornar
		if (mob.isVia() || mob.isVolume())
			return mob;

		if (mob.isGeral() && mob.doc().isExpediente()) {
			// Se o numero da via nao foi especificado, tentar encontrar a via
			// de numero mais baixo que esteja com o titular.
			//
			for (ExMobil m : mob.doc().getExMobilSet()) {
				if (m.isGeral() || m.isEliminado())
					continue;
				if (m.isAtendente(super.getTitular(), null) || m.isNotificado(super.getTitular(), null))
					return m;
			}

			// Se nao encontrar, tentar encontrar uma na lotacao do titular
			//
			for (ExMobil m : mob.doc().getExMobilSet()) {
				if (m.isGeral() || m.isEliminado())
					continue;
				if (m.isAtendente(null, super.getLotaTitular()) || m.isNotificado(null, super.getLotaTitular()))
					return m;
			}
		}

		if (mob.isGeral() && mob.doc().isProcesso()) {
			// Se o ultimo volume estiver na lotação do titular ou com ele,
			// retornar o ultimo volume
			//
			ExMobil m = mob.doc().getUltimoVolume();
			if (m != null && (m.isAtendente(super.getTitular(), super.getLotaTitular()) || m.isNotificado(super.getTitular(), super.getLotaTitular()))) {
				return m;
			}
		}

		return sel;
	}
	
	@Get("app/ferramentas/doc/listar")
	public void aFerramentasListarDocumentos() {
		assertAcesso(SIGA_DOC_FE_LD);

		final ExMobilDaoFiltro flt = createDaoFiltro();
		List<Object[]> list = dao().consultarPorFiltroOtimizado(flt, 0, 0, getTitular(), getLotaTitular());
		List<ExDocumento> docs = new ArrayList<>();
		Set<ExDocumento> set = new HashSet<>();
		
		for (Object[] aobj : list) {
			ExDocumento doc = (ExDocumento) aobj[0];
			if (!set.contains(doc)) {
				set.add(doc);
				docs.add(doc);
			}
		}
		
		result.include("lista", docs);
		List<ExNivelAcesso> listaNivelAcesso = ExDao.getInstance().listarOrdemNivel();
		result.include("listaNivelAcesso", listaNivelAcesso);
	}

}