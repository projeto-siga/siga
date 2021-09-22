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
package br.gov.jfrj.siga.ex.vo;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.cp.CpTipoMarcadorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExCompetenciaBL;
import br.gov.jfrj.siga.ex.util.ExGraphColaboracao;
import br.gov.jfrj.siga.ex.util.ExGraphRelacaoDocs;
import br.gov.jfrj.siga.ex.util.ExGraphTramitacao;
import br.gov.jfrj.siga.ex.util.ProcessadorModeloFreemarker;
import br.gov.jfrj.siga.hibernate.ExDao;

public class ExDocumentoVO extends ExVO {
	transient DpPessoa titular;
	transient DpLotacao lotaTitular;
	transient ExDocumento doc;
	transient ExMobil mob;
	transient Map<ExMobil, Set<ExMarca>> marcasPorMobil = new LinkedHashMap<ExMobil, Set<ExMarca>>();
	transient Map<ExMobil, Set<ExMarca>> marcasDeSistemaPorMobil = new LinkedHashMap<ExMobil, Set<ExMarca>>();
	transient Set<ExMarca> marcasDoMobil = new TreeSet<ExMarca>(ExMarca.MARCADOR_DO_MOBIL_COMPARATOR);
	transient List<Object> listaDeAcessos;
	
	List<ExMobilVO> mobs = new ArrayList<ExMobilVO>();
	transient List<ExDocumentoVO> documentosPublicados = new ArrayList<ExDocumentoVO>();
	transient ExDocumentoVO boletim;
	String classe;
	String outrosMobsLabel;
	String nomeCompleto;
	String dtDocDDMMYY;
	String dataPrimeiraAssinatura;
	String subscritorString;
	String subscritorSigla;
	String subscritorNome;
	String subscritorTipo;
	String originalNumero;
	String originalData;
	String originalOrgao;
	String classificacaoDescricaoCompleta;
	String classificacaoSigla;
	String classificacaoNome;
	List<String> tags;
	String destinatarioString;
	String destinatarioSigla;
	String destinatarioNome;
	String destinatarioTipo;
	String descrDocumento;
	String nmNivelAcesso;
	String paiSigla;
	String tipoDocumento;

	String dtFinalizacao;
	String nmArqMod;
	String conteudoBlobHtmlString;
	String conteudoBlobFormString;
	String sigla;
	String fisicoOuEletronico;
	boolean fDigital;
	Map<ExMovimentacaoVO, Boolean> cossignatarios = new HashMap<ExMovimentacaoVO, Boolean>();
	String dadosComplementares;
	String forma;
	String modelo;
	String idModelo;
	String tipoFormaDocumento;
	String cadastranteString;
	String lotaCadastranteString;
	transient ExGraphTramitacao dotTramitacao;
	transient ExGraphRelacaoDocs dotRelacaoDocs;
	transient ExGraphColaboracao dotColaboracao;
	String vizTramitacao;
	String vizRelacaoDocs;
	String vizColaboracao;
	boolean podeAssinar;
	boolean podeCapturarPDF;
	String exTipoDocumentoDescricao;
	boolean podeAnexarArquivoAuxiliar;
	String dtLimiteDemandaJudicial;
	String dtPrazoDeAssinatura;

	public ExDocumentoVO(ExDocumento doc, ExMobil mob, DpPessoa cadastrante, DpPessoa titular,
			DpLotacao lotaTitular, boolean completo, boolean exibirAntigo, boolean serializavel) {
		this.titular = titular;
		this.lotaTitular = lotaTitular;
		this.doc = doc;
		this.mob = mob;
		this.sigla = doc.getSigla();
		this.descrDocumento = doc.getDescrDocumento();

		this.exTipoDocumentoDescricao = doc.getExTipoDocumento().getDescricao();

		if (!completo)
			return;

		this.nomeCompleto = doc.getNomeCompleto();
		this.dtDocDDMMYY = doc.getDtDocDDMMYY();
		
		/*
		 * 16/01/2020 - Data da ultima assinatura
		 */
		this.dataPrimeiraAssinatura = doc.getDtPrimeiraAssinaturaDDMMYY();
		
		
		/*this.dataPrimeiraAssinatura = this.obterDataPrimeiraAssinatura(doc);*/
		this.subscritorString = doc.getSubscritorString();
		if (doc.getSubscritor() != null) {
			this.subscritorSigla = doc.getSubscritor().getSigla();
			this.subscritorNome = doc.getSubscritor().getNomePessoa();
			this.subscritorTipo = "PESSOA";
		}
		this.cadastranteString = doc.getCadastranteString();
		if (doc.getLotaCadastrante() != null)
			this.lotaCadastranteString = "("
					+ doc.getLotaCadastrante().getSigla() + ")";
		else
			this.lotaCadastranteString = "";

		if (doc.getExClassificacaoAtual() != null) {
			if (doc.getExClassificacaoAtual().getAtual() != null)
				this.classificacaoDescricaoCompleta = doc.getExClassificacaoAtual()
						.getAtual().getDescricaoCompleta();
			else
				this.classificacaoDescricaoCompleta = doc.getExClassificacaoAtual()
				.getDescricaoCompleta();
		}
		this.destinatarioString = doc.getDestinatarioString();
		if (doc.getDestinatario() != null) {
			this.destinatarioSigla = doc.getDestinatario().getSigla();
			this.destinatarioNome = doc.getDestinatario().getNomePessoa();
			this.destinatarioTipo = "PESSOA";
		} else if (doc.getLotaDestinatario() != null) {
			this.destinatarioSigla = doc.getLotaDestinatario().getSigla();
			this.destinatarioNome = doc.getLotaDestinatario().getNomeLotacao();
			this.destinatarioTipo = "LOTACAO";
		}

		ExClassificacao classif = doc.getExClassificacaoAtual();
		if (classif != null) {
			if (classif.getAtual() != null)
				   classif = classif.getAtual();
			this.classificacaoDescricaoCompleta = classif.getDescricaoCompleta();
			this.classificacaoSigla = classif.getSigla();
			this.classificacaoNome = classif.getNome();
		}

		if (doc.getExNivelAcessoAtual() != null)
			this.nmNivelAcesso = doc.getExNivelAcessoAtual().getNmNivelAcesso();
		// Desabilitado temporariamente
		this.listaDeAcessos = doc.getListaDeAcessos();
		if (doc.getExMobilPai() != null)
			this.paiSigla = doc.getExMobilPai().getSigla();
		if (doc.getExTipoDocumento() != null)
			switch (doc.getExTipoDocumento().getId().intValue()) {
			case 1:
				this.tipoDocumento = "interno";
				break;
			case 2:
				this.tipoDocumento = "interno_importado";
				break;
			case 3:
				this.tipoDocumento = "externo";
				break;
			}
		if (doc.getExFormaDocumento().getExTipoFormaDoc() != null)
			switch (doc.getExFormaDocumento().getExTipoFormaDoc().getId()
					.intValue()) {
			case 1:
				this.tipoFormaDocumento = "expediente";
				break;
			case 2:
				this.tipoFormaDocumento = "processo_administrativo";
				break;
			}
		
		this.dtFinalizacao = doc.getDtFinalizacaoDDMMYY();
		if (doc.getExModelo() != null)
			this.nmArqMod = doc.getExModelo().getNmArqMod();

		this.conteudoBlobHtmlString = doc
				.getConteudoBlobHtmlStringComReferencias();

		byte[] form = doc.getConteudoBlobForm();
		if (form != null)
			this.conteudoBlobFormString = new String(form, StandardCharsets.ISO_8859_1);
		
		if (doc.isEletronico()) {
			this.classe = "header_eletronico";
			this.fisicoOuEletronico = "Documento Eletrônico";
			this.fDigital = true;
		} else {
			this.classe = "header";
			this.fisicoOuEletronico = "Documento Físico";
			this.fDigital = false;
		}

		ExCompetenciaBL comp = Ex.getInstance().getComp();
		for (ExMovimentacao movCossig : doc.getMovsCosignatario()) {
			ExMobilVO mobilVO = new ExMobilVO(doc.getMobilGeral(), titular, titular, lotaTitular,
					exibirAntigo, serializavel);
			ExMovimentacaoVO movVO = new ExMovimentacaoVO(mobilVO, movCossig, cadastrante, titular,
					lotaTitular, serializavel);
			cossignatarios.put(movVO,
					comp.podeExcluirCosignatario(titular, lotaTitular, doc.getMobilGeral(), movCossig));
		}

		this.forma = doc.getExFormaDocumento() != null ? doc
				.getExFormaDocumento().getDescricao() : "";
		this.modelo = doc.getExModelo() != null ? doc.getExModelo().getNmMod()
				: "";
		this.idModelo = doc.getExModelo() != null && doc.getExModelo().getId() != null
				? doc.getExModelo().getId().toString()
				: null;

		if (mob != null) {
			SortedSet<ExMobil> mobsDoc;
			if (doc.isProcesso())
				mobsDoc = doc.getExMobilSetInvertido();
			else
				mobsDoc = doc.getExMobilSet();

			for (ExMobil m : mobsDoc) {
				if (mob.isGeral() || m.isGeral()
						|| mob.getId().equals(m.getId()))
					mobs.add(new ExMobilVO(m, cadastrante, titular, lotaTitular, completo, serializavel));
			}

			addAcoes(doc, titular, lotaTitular, exibirAntigo);

			this.podeAssinar = comp.podeAssinar(titular, lotaTitular, mob)
					&& comp.podeAssinarComSenha(titular, lotaTitular, mob);
			this.podeCapturarPDF = comp.podeCapturarPDF(titular, lotaTitular, mob);

			ExGraphTramitacao exGraphTramitacao = new ExGraphTramitacao(mob);
			if (exGraphTramitacao.getNumNodos() > 1) {
				this.dotTramitacao = exGraphTramitacao;
				this.vizTramitacao = this.dotTramitacao.toString();
			}
			this.dotRelacaoDocs = new ExGraphRelacaoDocs(mob, titular);
			this.vizRelacaoDocs = this.dotRelacaoDocs.toString();
			this.dotColaboracao = new ExGraphColaboracao(doc);
			this.vizColaboracao = this.dotColaboracao.toString();
		}

		if (!serializavel)
			addDadosComplementares();

		tags = new ArrayList<String>();
		if (doc.getExClassificacao() != null) {
			String classificacao = doc.getExClassificacao().getDescricao();
			if (classificacao != null && classificacao.length() != 0) {
				String a[] = classificacao.split(": ");
				for (String s : a) {
					String ss = "@" + Texto.slugify(s, true, true);
					if (!tags.contains(ss)) {
						tags.add(ss);
					}
				}
			}
		}
		if (doc.getExModelo() != null) {
			String ss = "@"
					+ Texto.slugify(doc.getExModelo().getNmMod(), true, true);
			if (!tags.contains(ss)) {
				tags.add(ss);
			}
		}

		List<ExDocumento> documentosPublicadosNoBoletim = doc.getDocumentosPublicadosNoBoletim();
		if (documentosPublicadosNoBoletim != null) {
			for (ExDocumento documentoPublicado : documentosPublicadosNoBoletim) {
				documentosPublicados.add(new ExDocumentoVO(documentoPublicado));
			}
		}

		ExDocumento bol = doc.getBoletimEmQueDocFoiPublicado();
		if (bol != null)
			boletim = new ExDocumentoVO(bol);
		
		this.originalNumero = doc.getNumExtDoc();
		this.originalData = doc.getDtDocOriginalDDMMYYYY();
		this.originalOrgao = doc.getOrgaoExterno() != null ? doc.getOrgaoExterno().getDescricao() : null;
		
		this.podeAnexarArquivoAuxiliar = Ex.getInstance().getComp().podeAnexarArquivoAuxiliar(titular, lotaTitular, mob);

		this.dtLimiteDemandaJudicial = doc.getMobilGeral().getExMovimentacaoSet().stream() //
				.filter(mov -> mov.getExTipoMovimentacao().getId()
						.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_MARCACAO))
				.filter(mov -> !mov.isCancelada()) //
				.filter(mov -> mov.getMarcador().isDemandaJudicial()) //
				.map(ExMovimentacao::getDtFimMovDDMMYY) //
				.findFirst().orElse(null);
		
		this.dtPrazoDeAssinatura = doc.getDtPrazoDeAssinaturaDDMMYYYYHHMM();
		if (serializavel) {
			this.titular = null;
			this.lotaTitular = null;
			this.doc = null;
			this.mob = null;
			this.marcasPorMobil = null;
			this.marcasDeSistemaPorMobil = null;
			this.marcasDoMobil = null;
			this.dotTramitacao = null;
			this.dotColaboracao = null;
			this.dotRelacaoDocs = null;
		}
	}
	
	/*
	 * Adicionado 14/02/2020
	 */
	
	protected  ExDao dao() {
		return ExDao.getInstance();
	}

	public List<Object> getListaDeAcessos() {
		return listaDeAcessos;
	}

	public ExDocumentoVO(ExDocumento doc) {
		this.doc = doc;
		this.sigla = doc.getSigla();
		this.nomeCompleto = doc.getNomeCompleto();
		this.dtDocDDMMYY = doc.getDtDocDDMMYY();
		this.descrDocumento = doc.getDescrDocumento();
		if (doc.isEletronico()) {
			this.classe = "header_eletronico";
			this.fisicoOuEletronico = "Documento Eletrônico";
			this.fDigital = true;
		} else {
			this.classe = "header";
			this.fisicoOuEletronico = "Documento Físico";
			this.fDigital = false;
		}
		
		this.podeAnexarArquivoAuxiliar = Ex.getInstance().getComp().podeAnexarArquivoAuxiliar(titular, lotaTitular, mob);
	}

	public void exibe() {
		List<Long> movimentacoesPermitidas = new ArrayList<Long>();

		movimentacoesPermitidas
				.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA);
		movimentacoesPermitidas
				.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA_A_DOCUMENTO_EXTERNO);
		movimentacoesPermitidas
				.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA_EXTERNO);
		movimentacoesPermitidas
				.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA);
		movimentacoesPermitidas
				.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO);
		movimentacoesPermitidas
				.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO_DE_ARQUIVO_AUXILIAR);
		movimentacoesPermitidas
				.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANOTACAO);
		movimentacoesPermitidas
				.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO);
		movimentacoesPermitidas
				.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_INTERNO);
		movimentacoesPermitidas
				.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_INTERNO_TRANSFERENCIA);
		movimentacoesPermitidas
				.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA);
		movimentacoesPermitidas
				.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA);
		movimentacoesPermitidas
				.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_DISPONIBILIZACAO);
		movimentacoesPermitidas
				.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO_BOLETIM);
		movimentacoesPermitidas
				.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_PUBLICACAO_BOLETIM);
		movimentacoesPermitidas
				.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_PUBLICACAO);
		movimentacoesPermitidas
				.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ENCERRAMENTO_DE_VOLUME);
		movimentacoesPermitidas
				.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_COPIA);
		movimentacoesPermitidas
				.add(ExTipoMovimentacao.TIPO_MOVIMENTACAO_CIENCIA);		

		List<Long> marcasGeralPermitidas = new ArrayList<Long>();
		marcasGeralPermitidas.add(CpMarcadorEnum.A_ELIMINAR.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.ARQUIVADO_CORRENTE.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.ARQUIVADO_INTERMEDIARIO.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.ARQUIVADO_PERMANENTE.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.EM_EDITAL_DE_ELIMINACAO.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.PUBLICACAO_SOLICITADA.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.PUBLICADO.getId());
		marcasGeralPermitidas
				.add(CpMarcadorEnum.RECOLHER_PARA_ARQUIVO_PERMANENTE.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.REMETIDO_PARA_PUBLICACAO.getId());
		marcasGeralPermitidas
				.add(CpMarcadorEnum.TRANSFERIR_PARA_ARQUIVO_INTERMEDIARIO.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.PENDENTE_DE_ASSINATURA.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.COMO_SUBSCRITOR.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.REVISAR.getId());
		marcasGeralPermitidas
				.add(CpMarcadorEnum.ANEXO_PENDENTE_DE_ASSINATURA.getId());
		marcasGeralPermitidas
				.add(CpMarcadorEnum.TRANSFERIR_PARA_ARQUIVO_INTERMEDIARIO.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.PENDENTE_DE_ANEXACAO.getId());
		marcasGeralPermitidas.add(CpMarcadorEnum.PORTAL_TRANSPARENCIA.getId());

		for (ExMobilVO mobVO : mobs) {

			// Limpa as Movimentações
			List<ExMovimentacaoVO> movimentacoesFinais = new ArrayList<ExMovimentacaoVO>();
			List<ExMovimentacao> juntadasRevertidas = new ArrayList<ExMovimentacao>();

			for (ExMovimentacaoVO exMovVO : mobVO.getMovs()) {
				if (!exMovVO.isCancelada() && movimentacoesPermitidas.contains(exMovVO.getIdTpMov())) {
					if (exMovVO.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA) {
						juntadasRevertidas.add(exMovVO.getMov()
								.getExMovimentacaoRef());
						// Edson: se não gerou peça, nem mostra o
						// desentranhamento
						if (exMovVO.getMov().getConteudoBlobMov() == null)
							continue;
					}
					if (!juntadasRevertidas.contains(exMovVO.getMov()))
						movimentacoesFinais.add(exMovVO);
				}

			}

			mobVO.setMovs(movimentacoesFinais);
		}

		ExMobilVO mobilGeral = null;
		ExMobilVO mobilEspecifico = null;

		for (ExMobilVO mobilVO : mobs) {
			if (mobilVO.isGeral)
				mobilGeral = mobilVO;
			else
				mobilEspecifico = mobilVO;
		}

		calculaSetsDeMarcas();

		if (mobilEspecifico != null && mobilGeral != null) {
			mobilEspecifico.getAcoes().addAll(mobilGeral.getAcoes());
			mobilEspecifico.getMovs().addAll(mobilGeral.getMovs());
			mobilEspecifico.anexosNaoAssinados
					.addAll(mobilGeral.anexosNaoAssinados);
			for (ExMarca m : mobilGeral.getMarcasAtivas())
				if (marcasGeralPermitidas.contains(m.getCpMarcador()
						.getIdMarcador()))
					mobilEspecifico.getMarcasAtivas().add(m);
//			for (ExMarca m : mobilGeral.getMob().getExMarcaSet())
//				if (marcasGeralPermitidas.contains(m.getCpMarcador()
//						.getIdMarcador()))
//					for (ExMobil cadaMobil : marcasPorMobil.keySet())
//						marcasPorMobil.get(cadaMobil).add(m);
			mobs.remove(mobilGeral);
		}

		this.dotTramitacao = new ExGraphTramitacao(mob);
		this.dotRelacaoDocs = new ExGraphRelacaoDocs(mob, titular);
		this.dotColaboracao = new ExGraphColaboracao(doc);

	}

	public void calculaSetsDeMarcas() {
		Date now = dao().consultarDataEHoraDoServidor(); 
	
		for (ExMobil cadaMobil : doc.getExMobilSet()) {
			SortedSet<ExMarca> setSistema = new TreeSet<>();
			SortedSet<ExMarca> set = cadaMobil.getExMarcaSet();
			for (ExMarca m : set) {
				if ((m.getDtIniMarca() == null || !m.getDtIniMarca().after(now))
						&& (m.getDtFimMarca() == null || m.getDtFimMarca().after(now))) {
					if (m.getCpMarcador().getIdFinalidade().getIdTpMarcador() == CpTipoMarcadorEnum.TIPO_MARCADOR_SISTEMA)
						setSistema.add(m);
					if (m.getCpMarcador().getIdFinalidade().getIdTpMarcador() != CpTipoMarcadorEnum.TIPO_MARCADOR_SISTEMA && (cadaMobil == mob || cadaMobil.isGeral())) 
						getMarcasDoMobil().add(m);
				}
			}
			getMarcasDeSistemaPorMobil().put(cadaMobil, setSistema);
			marcasPorMobil.put(cadaMobil, set);
		}
		// Edson: mostra lista de vias/volumes só se número de
		// vias/volumes além do geral for > que 1 ou se o móbil
		// tiver informações que não aparecem no topo da tela
		//if (doc.getExMobilSet().size() > 2 || mob.temMarcaNaoAtiva())
		outrosMobsLabel = doc.isProcesso() ? "Volumes" : "Vias";
	}
	
	public void addAcoesVisualizar(ExDocumentoVO docVO, Long idVisualizacao) {
		ExVO vo = new ExVO();
		
		int numUltMobil = doc.getNumUltimoMobil();
		vo.addAcao(
				SigaMessages.getMessage("icon.ver.mais"),
				SigaMessages.getMessage("documento.ver.mais"),
				"/app/expediente/doc",
				SigaMessages.getMessage("documento.acao.exibirAntigo")+ "?sigla="+doc+"&idVisualizacao="+idVisualizacao,
				Boolean.TRUE, 
						numUltMobil < 20 ? "" : "Exibir todos os " + numUltMobil + " volumes do processo simultaneamente pode exigir um tempo maior de processamento. Deseja exibi-los?", 
						null, null, null, null);
		
		vo.addAcao(
				SigaMessages.getMessage("icon.ver.impressao"),
				SigaMessages.getMessage("documento.ver.impressao"),
				"/app/arquivo",
				"exibir",
				Boolean.TRUE,
				null, "&popup=true&arquivo=" + doc.getReferenciaPDF()+"&idVisualizacao="+idVisualizacao, null,
				null, null);
		
		vo.addAcao(
				"folder_magnify",
				SigaMessages.getMessage("documento.ver.dossie"),
				"/app/expediente/doc",
				"exibirProcesso?sigla="+doc+"&idVisualizacao="+idVisualizacao,
				Boolean.TRUE);
		
			
		docVO.getMobs().get(0).setAcoes(vo.getAcoes());
		
		
	}
	
	public String obterDataPrimeiraAssinatura(ExDocumento doc) {
		String retorno = "";
		
		List<ExMovimentacao> lista = new ArrayList<ExMovimentacao>();
		lista.addAll(doc.getMobilGeral().getMovsNaoCanceladas(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA));
		lista.addAll(doc.getMobilGeral().getMovsNaoCanceladas(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO));
		lista.addAll(doc.getMobilGeral().getMovsNaoCanceladas(ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA));
		
		if(lista.isEmpty()) {
			return retorno;
		} else if(lista.size() == 1) {
			retorno = lista.get(0).getDtMovDDMMYYYY();
		} else {
			Long i = Long.valueOf(0);
			for (ExMovimentacao exMovimentacao : lista) {
				if(exMovimentacao.getIdMov() < i || i.equals(Long.valueOf(0))) {
					i = exMovimentacao.getIdMov();
					retorno = exMovimentacao.getDtMovDDMMYYYY();
				}
			}
		}
		return retorno;
	}
	
	/*
	 * Adicionado 14/02/2020
	 */
	private boolean mostrarGerarProtocolo(ExDocumento doc) {				
		if(doc.getDtAssinatura() == null)
			return false;
		
		List<ExMobil>mobs = dao().consultarMobilPorDocumento(doc);
		List<ExMovimentacao>movs = new ArrayList<ExMovimentacao>();
		
		for(ExMobil mob:mobs)
			movs.addAll(dao().consultarMovimentoPorMobil(mob));
		
		Collections.sort(movs, new Comparator<ExMovimentacao>() {
			public int compare(ExMovimentacao m1, ExMovimentacao m2) {
				return m2.getData().compareTo(m1.getData());
			}
		});
		
		for(ExMovimentacao mov:movs) {
			if(mov.getDescrTipoMovimentacao().equalsIgnoreCase("Desarquivamento")||
					mov.getDescrTipoMovimentacao().equalsIgnoreCase("Desentranhamento"))
				return true;
			
			
			if(mov.getDescrTipoMovimentacao().equalsIgnoreCase("Juntada")||
					mov.getDescrTipoMovimentacao().equalsIgnoreCase("Cancelamento")||
					mov.getDescrTipoMovimentacao().equalsIgnoreCase("Arquivamento Intermediário")||
					mov.getDescrTipoMovimentacao().equalsIgnoreCase("Arquivamento Corrente"))
				return false;
		}
		return true;
	}
	
	/**
	 * @param doc
	 * @param titular
	 * @param lotaTitular
	 * @throws Exception
	 */
	private void addAcoes(ExDocumento doc, DpPessoa titular,
			DpLotacao lotaTitular, boolean exibirAntigo) {
		
		ExVO vo = this;
		for (ExMobilVO mobvo : mobs) {
			if (mobvo.isGeral)
				vo = mobvo;
		}

		ExMobil mob = doc.getMobilGeral();
		
		vo.addAcao(
				"folder_magnify",
				SigaMessages.getMessage("documento.ver.dossie"),
				"/app/expediente/doc",
				"exibirProcesso",
				Ex.getInstance().getComp()
						.podeVisualizarImpressao(titular, lotaTitular, mob));

		vo.addAcao(
				SigaMessages.getMessage("icon.ver.impressao"),
				SigaMessages.getMessage("documento.ver.impressao"),
				"/app/arquivo",
				"exibir",
				Ex.getInstance().getComp()
						.podeVisualizarImpressao(titular, lotaTitular, mob),
				null, "&popup=true&arquivo=" + doc.getReferenciaPDF(), null,
				null, null);
			
		vo.addAcao(
				"lock",
				"Fina_lizar",
				"/app/expediente/doc",

				"finalizar",
				Ex.getInstance().getComp()
						.podeFinalizar(titular, lotaTitular, mob),
				null, null, null, null,
				"once");

		// addAcao("Finalizar e Assinar", "/expediente/mov",
		// "finalizar_assinar",
		// podeFinalizarAssinar(titular, lotaTitular, mob),
		// "Confirma a finalização do documento?", null, null, null);

		vo.addAcao("pencil", "Edita_r", "/app/expediente/doc", "editar", Ex
				.getInstance().getComp().podeEditar(titular, lotaTitular, mob));

		vo.addAcao(
				"delete",
				"Excluir",
				"/app/expediente/doc",
				"excluir",
				Ex.getInstance().getComp()
						.podeExcluir(titular, lotaTitular, mob),
				"Confirma a exclusão do documento?", null, null, null, "once");

		vo.addAcao("user_add", "Incluir Cossignatário", "/app/expediente/mov",
				"incluir_cosignatario", Ex.getInstance().getComp()
						.podeIncluirCosignatario(titular, lotaTitular, mob),
				null, "sigla=" + doc.getSigla(), null, null, null);

		vo.addAcao(
				"attach",
				"Ane_xar",
				"/app/expediente/mov",
				"anexar",
				Ex.getInstance().getComp()
						.podeAnexarArquivo(titular, lotaTitular, mob));
		vo.addAcao(
				"note_add",
				"_Anotar",
				"/app/expediente/mov",
				"anotar",
				Ex.getInstance().getComp()
						.podeFazerAnotacao(titular, lotaTitular, mob));

		vo.addAcao("folder_user", SigaMessages.getMessage("documento.definir.perfil"), "/app/expediente/mov",
				"vincularPapel", Ex.getInstance().getComp()
						.podeFazerVinculacaoPapel(titular, lotaTitular, mob));

//		vo.addAcao("folder_star", "Definir Marcador", "/app/expediente/mov",
//				"marcar", Ex.getInstance().getComp()
//						.podeMarcar(titular, lotaTitular, mob));

		vo.addAcao(
				"cd",
				"Download do Conteúdo",
				"/expediente/doc",
				"anexo",
				Ex.getInstance().getComp()
						.podeDownloadConteudo(titular, lotaTitular, mob));

		vo.addAcao("add", "Criar Via", "/app/expediente/doc", "criar_via", Ex
				.getInstance().getComp()
				.podeCriarVia(titular, lotaTitular, mob), null, null, null,
				null, "once");

		vo.addAcao(
				"add",
				"Abrir Novo Volume",
				"/app/expediente/doc",
				"criar_volume",
				Ex.getInstance().getComp()
						.podeCriarVolume(titular, lotaTitular, mob),
				"Confirma a abertura de um novo volume?", null, null, null,
				"once");

		vo.addAcao(
				"link_add",
				"Criar Subprocesso",
				"/app/expediente/doc",
				"editar",
				Ex.getInstance().getComp()
						.podeCriarSubprocesso(titular, lotaTitular, mob), null,
				"mobilPaiSel.sigla=" + getSigla() + "&idForma="
						+ mob.doc().getExFormaDocumento().getIdFormaDoc() + "&criandoSubprocesso=true",
				null, null, null);

		vo.addAcao(
				"script_edit",
				"Registrar A_ssinatura Manual",
				"/app/expediente/mov",
				"registrar_assinatura",
				Ex.getInstance().getComp()
						.podeRegistrarAssinatura(titular, lotaTitular, mob));

		vo.addAcao(
				"script_key",
				"A_ssinar",
				"/app/expediente/mov",
				"assinar",
				Ex.getInstance().getComp()
						.podeAssinar(titular, lotaTitular, mob),
				null, null, null, null,
				"once");
		
		vo.addAcao(
				"script_key",
				"A_utenticar",
				"/app/expediente/mov",
				"autenticar_documento",
				Ex.getInstance()
						.getComp()
						.podeAutenticarDocumento(titular, lotaTitular,
								mob.getExDocumento()));

		vo.addAcao(
				"page_go	",
				"Solicitar Assinatura",
				"/app/expediente/mov",
				"solicitar_assinatura",
				Ex.getInstance()
						.getComp()
						.podeSolicitarAssinatura(titular, lotaTitular,
								mob.doc()), "Ao clicar em prosseguir, você estará sinalizando que revisou este documento e que ele deve ser incluído na lista para ser assinado em lote. Prosseguir?", 
								null, null, null, "once");

		if (doc.isFinalizado() && doc.getNumExpediente() != null) {
			// documentos finalizados
			if (mob.temAnexos()) {
				vo.addAcao("script_key", "Assinar Anexos Gerais",
						"/app/expediente/mov", "assinarAnexos", true, null,
						"assinandoAnexosGeral=true&sigla=" + getSigla(), null,
						null, null);
			}
		}

		vo.addAcao(
				"shield",
				"Redefinir Acesso",
				"/app/expediente/mov",
				"redefinir_nivel_acesso",
				Ex.getInstance().getComp()
						.podeRedefinirNivelAcesso(titular, lotaTitular, mob));

		vo.addAcao(
				"group_link",
				"Restrição de Acesso",
				"/app/expediente/mov",
				"restringir_acesso",
				Ex.getInstance().getComp().podeRestrigirAcesso(titular, lotaTitular, mob));
		
		vo.addAcao(
				"arrow_undo",
				"Redefinir Acesso Padrão",
				"/app/expediente/mov",
				"desfazer_restricao_acesso",
				Ex.getInstance()
						.getComp()
						.podeDesfazerRestricaoAcesso(titular, lotaTitular, mob),
				"Esta operação anulará as Restrições de Acesso. Prosseguir?",
				null, null, null, "once");
		
		vo.addAcao(
				"book_add",
				"Solicitar Publicação no Boletim",
				"/app/expediente/mov",
				"boletim_agendar",
				Ex.getInstance()
						.getComp()
						.podeBotaoAgendarPublicacaoBoletim(titular,
								lotaTitular, mob));

		vo.addAcao(
				"book_link",
				"Registrar Publicação do Boletim",
				"/app/expediente/mov",
				"boletim_publicar",
				Ex.getInstance().getComp()
				.podePublicar(titular, lotaTitular, mob), null, null, null, null,
				"once");

		vo.addAcao(
				"error_go",
				"Refazer",
				"/app/expediente/doc",
				"refazer",
				Ex.getInstance().getComp()
						.podeRefazer(titular, lotaTitular, mob),
						SigaMessages.getMessage("mensagem.cancela.documento"),
				null, null, null, "once siga-btn-refazer");

		vo.addAcao(
				"arrow_divide",
				"Duplicar",
				"/app/expediente/doc",
				"duplicar",
				Ex.getInstance().getComp()
						.podeDuplicar(titular, lotaTitular, mob),
				SigaMessages.getMessage("documento.confirma.duplica"),
				null, null, null, "once");

		// test="${exibirCompleto != true}" />
		int numUltMobil = doc.getNumUltimoMobil();
		vo.addAcao(
				SigaMessages.getMessage("icon.ver.mais"),
				SigaMessages.getMessage("documento.ver.mais"),
				"/app/expediente/doc",
				SigaMessages.getMessage("documento.acao.exibirAntigo"),
				Ex.getInstance()
						.getComp()
						.podeExibirInformacoesCompletas(titular, lotaTitular,
								mob)
						&& !exibirAntigo, 
						numUltMobil < 20 ? "" : "Exibir todos os " + numUltMobil + " volumes do processo simultaneamente pode exigir um tempo maior de processamento. Deseja exibi-los?", 
						null, null, null, null);

		vo.addAcao(
				"magnifier",
				"Auditar",
				"/app/expediente/doc",
				"exibirAntigo",
				Ex.getInstance()
						.getComp()
						.podeExibirInformacoesCompletas(titular, lotaTitular,
								mob)
						&& exibirAntigo, null, "&exibirCompleto=true", null,
				null, null);

		vo.addAcao(
				"report_link",
				"Agendar Publicação no Diário",
				"/app/expediente/mov",
				"agendar_publicacao",
				Ex.getInstance().getComp()
						.podeAgendarPublicacao(titular, lotaTitular, mob));

		vo.addAcao(
				"report_add",
				"Solicitar Publicação no Diário",
				"/app/expediente/mov",
				"pedirPublicacao",
				Ex.getInstance().getComp()
						.podePedirPublicacao(titular, lotaTitular, mob));

		// <ww:param name="idFormaDoc">60</ww:param>
		vo.addAcao(
				"arrow_undo",
				"Desfazer Cancelamento",
				"/app/expediente/doc",
				"desfazerCancelamentoDocumento",
				Ex.getInstance()
						.getComp()
						.podeDesfazerCancelamentoDocumento(titular,
								lotaTitular, mob),
				"Esta operação anulará o cancelamento do documento e tornará o documento novamente editável. Prosseguir?",
				null, null, null, "once");

		vo.addAcao(
				"delete",
				"Cancelar",
				"/app/expediente/doc",
				"tornarDocumentoSemEfeito",
				Ex.getInstance()
						.getComp()
						.podeTornarDocumentoSemEfeito(titular, lotaTitular, mob),
				(SigaMessages.getMessage("mensagem.semEfeito.documento")),
				null, null, null, "once  siga-btn-tornar-documento-sem-efeito");
		
		vo.addAcao(
				"cancel",
				"Cancelar",
				"/app/expediente/doc",
				"cancelarDocumento",
				Ex.getInstance()
						.getComp()
						.podeCancelarDocumento(titular, lotaTitular, mob),
				"Esta operação cancelará o documento pendente de assinatura. Prosseguir?",
				null, null, null, "once");
		
		vo.addAcao(
				"report_link",
				SigaMessages.getMessage("documento.publicar.portaltransparencia"),
				"/app/expediente/mov",
				"publicacao_transparencia",
				Ex.getInstance().getComp()
						.podePublicarPortalTransparencia(titular, lotaTitular, mob));

		
		if(mostrarGerarProtocolo(doc)) {
			vo.addAcao(
					"printer",
					"Gerar Protocolo",
					"/app/expediente/doc",
					"gerarProtocolo",
					Ex.getInstance()
							.getComp()
							.podeGerarProtocolo(titular, lotaTitular, mob),
					null,
					"&popup=true", null, null, null);
		}
	}

	public void addDadosComplementares() {
		ProcessadorModeloFreemarker p = new ProcessadorModeloFreemarker();
		Map attrs = new HashMap();
		attrs.put("nmMod", "macro dadosComplementares");
		attrs.put("template", "[@dadosComplementares/]");
		attrs.put("doc", this.getDoc());
		dadosComplementares = p.processarModelo(doc.getOrgaoUsuario(), attrs,
				null);
	}

	public String getClasse() {
		return classe;
	}

	public String getDataPrimeiraAssinatura() {
		return dataPrimeiraAssinatura;
	}

	public void setDataPrimeiraAssinatura(String dataPrimeiraAssinatura) {
		this.dataPrimeiraAssinatura = dataPrimeiraAssinatura;
	}

	public String getClassificacaoDescricaoCompleta() {
		return classificacaoDescricaoCompleta;
	}

	public String getConteudoBlobHtmlString() {
		return conteudoBlobHtmlString;
	}

	public String getDescrDocumento() {
		return descrDocumento;
	}

	public String getDestinatarioString() {
		return destinatarioString;
	}

	public ExDocumento getDoc() {
		return doc;
	}

	public ExMobil getMob() {
		return mob;
	}

	public String getDtDocDDMMYY() {
		return dtDocDDMMYY;
	}

	public String getDtFinalizacao() {

		return dtFinalizacao;
	}

	public List<ExMobilVO> getMobs() {
		return mobs;
	}

	public String getNmArqMod() {
		return nmArqMod;
	}

	public String getNmNivelAcesso() {
		return nmNivelAcesso;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public String getPaiSigla() {
		return paiSigla;
	}

	public String getSigla() {
		return sigla;
	}

	public String getSiglaCurtaSubProcesso() {
		if (doc.isProcesso() && doc.getExMobilPai() != null) {
			try {
				return sigla.substring(sigla.length() - 3, sigla.length());
			} catch (Exception e) {
				return sigla;
			}
		}

		return "";

	}

	public String getSubscritorString() {
		return subscritorString;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public String getFisicoOuEletronico() {
		return fisicoOuEletronico;
	}

	public boolean isDigital() {
		return fDigital;
	}

	@Override
	public String toString() {
		String s = getSigla() + "[" + getAcoes() + "]";
		for (ExMobilVO m : getMobs()) {
			s += "\n" + m.toString();
		}
		return s;
	}

	public String getDadosComplementares() {
		return dadosComplementares;
	}

	public String getForma() {
		return forma;
	}

	public void setForma(String forma) {
		this.forma = forma;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getTipoFormaDocumento() {
		return tipoFormaDocumento;
	}

	public String getCadastranteString() {
		return cadastranteString;
	}

	public String getLotaCadastranteString() {
		return lotaCadastranteString;
	}

	public String getOutrosMobsLabel() {
		return outrosMobsLabel;
	}

	public ExGraphTramitacao getDotTramitacao() {
		return dotTramitacao;
	}

	public ExGraphRelacaoDocs getDotRelacaoDocs() {
		return dotRelacaoDocs;
	}

	public ExGraphColaboracao getDotColaboracao() {
		return dotColaboracao;
	}

	public List<ExDocumentoVO> getDocumentosPublicados() {
		return documentosPublicados;
	}

	public ExDocumentoVO getBoletim() {
		return boletim;
	}

	public Map<ExMobil, Set<ExMarca>> getMarcasPorMobil() {
		return marcasPorMobil;
	}

	public void setMarcasPorMobil(Map<ExMobil, Set<ExMarca>> marcasPorMobil) {
		this.marcasPorMobil = marcasPorMobil;
	}

	public Map<ExMovimentacaoVO, Boolean> getCossignatarios() {
		return cossignatarios;
	}

	public void setCossignatarios(Map<ExMovimentacaoVO, Boolean> cossignatarios) {
		this.cossignatarios = cossignatarios;
	}
	
	public String getOriginalNumero() {
		return originalNumero;
	}

	public String getOriginalData() {
		return originalData;
	}

	public String getOriginalOrgao() {
		return originalOrgao;
	}

	public boolean getPodeAnexarArquivoAuxiliar() {
		return podeAnexarArquivoAuxiliar;
	}
	
	public void setPodeAnexarArquivoAuxiliar(boolean podeAnexar) {
		this.podeAnexarArquivoAuxiliar = podeAnexar;
	}
	
	public String getDtLimiteDemandaJudicial() {
		return dtLimiteDemandaJudicial;
	}

	public Set<ExMarca> getMarcasDoMobil() {
		return marcasDoMobil;
	}

	public void setMarcasDoMobil(Set<ExMarca> marcasDoMobil) {
		this.marcasDoMobil = marcasDoMobil;
	}

	public Map<ExMobil, Set<ExMarca>> getMarcasDeSistemaPorMobil() {
		return marcasDeSistemaPorMobil;
	}

	public void setMarcasDeSistemaPorMobil(Map<ExMobil, Set<ExMarca>> marcasDeSistemaPorMobil) {
		this.marcasDeSistemaPorMobil = marcasDeSistemaPorMobil;
	}

	public String getDtPrazoDeAssinatura() {
		return dtPrazoDeAssinatura;
	}

}