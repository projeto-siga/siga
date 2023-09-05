/*******************************************************************************
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

import com.crivano.jlogic.And;

import br.gov.jfrj.siga.base.AcaoVO;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.cp.CpTipoMarcadorEnum;
import br.gov.jfrj.siga.cp.logic.CpPodeBoolean;
import br.gov.jfrj.siga.cp.logic.CpPodeSempre;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeMovimentacao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExCompetenciaBL;
import br.gov.jfrj.siga.ex.logic.ExEstaFinalizado;
import br.gov.jfrj.siga.ex.logic.ExEstaOrdenadoAssinatura;
import br.gov.jfrj.siga.ex.logic.ExPodeAgendarPublicacao;
import br.gov.jfrj.siga.ex.logic.ExPodeAgendarPublicacaoDOE;
import br.gov.jfrj.siga.ex.logic.ExPodeAgendarPublicacaoNoBoletim;
import br.gov.jfrj.siga.ex.logic.ExPodeAnexarArquivo;
import br.gov.jfrj.siga.ex.logic.ExPodeAnexarArquivoAuxiliar;
import br.gov.jfrj.siga.ex.logic.ExPodeAssinar;
import br.gov.jfrj.siga.ex.logic.ExPodeAssinarComSenha;
import br.gov.jfrj.siga.ex.logic.ExPodeAutenticarComSenha;
import br.gov.jfrj.siga.ex.logic.ExPodeAutenticarDocumento;
import br.gov.jfrj.siga.ex.logic.ExPodeCancelarDocumento;
import br.gov.jfrj.siga.ex.logic.ExPodeCapturarPDF;
import br.gov.jfrj.siga.ex.logic.ExPodeCriarSubprocesso;
import br.gov.jfrj.siga.ex.logic.ExPodeCriarVia;
import br.gov.jfrj.siga.ex.logic.ExPodeCriarVolume;
import br.gov.jfrj.siga.ex.logic.ExPodeDesfazerConcelamentoDeDocumento;
import br.gov.jfrj.siga.ex.logic.ExPodeDesfazerRestricaoDeAcesso;
import br.gov.jfrj.siga.ex.logic.ExPodeDuplicar;
import br.gov.jfrj.siga.ex.logic.ExPodeEditar;
import br.gov.jfrj.siga.ex.logic.ExPodeEnviarParaVisualizacaoExterna;
import br.gov.jfrj.siga.ex.logic.ExPodeEnviarSiafem;
import br.gov.jfrj.siga.ex.logic.ExPodeExcluir;
import br.gov.jfrj.siga.ex.logic.ExPodeExcluirCossignatario;
import br.gov.jfrj.siga.ex.logic.ExPodeFazerAnotacao;
import br.gov.jfrj.siga.ex.logic.ExPodeFazerDownloadFormatoLivre;
import br.gov.jfrj.siga.ex.logic.ExPodeFazerVinculacaoDePapel;
import br.gov.jfrj.siga.ex.logic.ExPodeFinalizar;
import br.gov.jfrj.siga.ex.logic.ExPodeGerarProtocolo;
import br.gov.jfrj.siga.ex.logic.ExPodeIncluirCossignatario;
import br.gov.jfrj.siga.ex.logic.ExPodeOrdemAssinatura;
import br.gov.jfrj.siga.ex.logic.ExPodePedirPublicacao;
import br.gov.jfrj.siga.ex.logic.ExPodePublicar;
import br.gov.jfrj.siga.ex.logic.ExPodePublicarPortalDaTransparencia;
import br.gov.jfrj.siga.ex.logic.ExPodeRedefinirNivelDeAcesso;
import br.gov.jfrj.siga.ex.logic.ExPodeRefazer;
import br.gov.jfrj.siga.ex.logic.ExPodeReferenciar;
import br.gov.jfrj.siga.ex.logic.ExPodeRegistrarAssinatura;
import br.gov.jfrj.siga.ex.logic.ExPodeRestringirAcesso;
import br.gov.jfrj.siga.ex.logic.ExPodeSolicitarAssinatura;
import br.gov.jfrj.siga.ex.logic.ExPodeTornarDocumentoSemEfeito;
import br.gov.jfrj.siga.ex.logic.ExPodeVisualizarImpressao;
import br.gov.jfrj.siga.ex.logic.ExTemAnexos;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
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
	transient boolean serializavel;
	
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
	String tipoDePrincipal;
	String principal;
	String principalCompacto;
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
	Map<String, String> form;
	String sigla;
	String codigoUnico;
	String fisicoOuEletronico;
	boolean fDigital;
	Map<ExMovimentacaoVO, Boolean> cossignatarios = new HashMap<ExMovimentacaoVO, Boolean>();
	String dadosComplementares;
	LinkedHashMap<DpPessoa, Long> listaOrdenadaCossigSub = new LinkedHashMap<DpPessoa, Long>();
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
	private ArrayList<ExMarcaVO> marcas;
	String dtPrazoDeAssinatura;
	Long idDoc;

	public ExDocumentoVO(ExDocumento doc, ExMobil mob, DpPessoa cadastrante, DpPessoa titular,
			DpLotacao lotaTitular, boolean completo, boolean exibirAntigo, boolean serializavel, boolean exibe) {
		this.serializavel = serializavel;
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
		
		if (doc.getTipoDePrincipal() != null && doc.getPrincipal() != null) {
			this.setPrincipal(doc.getPrincipal());
			this.setPrincipalCompacto(this.getPrincipal().replace("-", "").replace("/", ""));
			this.setTipoDePrincipal(doc.getTipoDePrincipal().getDescr());
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

		byte[] conteudoBlobForm = doc.getConteudoBlobForm();
		if (conteudoBlobForm != null) {
			Map<String, String> map = new HashMap<>();
			this.conteudoBlobFormString = new String(conteudoBlobForm, StandardCharsets.ISO_8859_1);
			Utils.mapFromUrlEncodedForm(map, conteudoBlobForm);
			this.form = map;
		}
		
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
					comp.pode(ExPodeExcluirCossignatario.class, titular, lotaTitular, movCossig));
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

			this.podeAssinar = comp.pode(ExPodeAssinar.class, titular, lotaTitular, mob)
					&& comp.pode(ExPodeAssinarComSenha.class, titular, lotaTitular, mob);
			this.podeCapturarPDF = comp.pode(ExPodeCapturarPDF.class, titular, lotaTitular, mob);

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
				documentosPublicados.add(new ExDocumentoVO(documentoPublicado, documentoPublicado.getMobilGeral(), cadastrante, titular,
						lotaTitular, false, exibirAntigo, serializavel, exibe));
			}
		}

		ExDocumento bol = doc.getBoletimEmQueDocFoiPublicado();
		if (bol != null)
			boletim = new ExDocumentoVO(bol, bol.getMobilGeral(), cadastrante, titular,
					lotaTitular, false, exibirAntigo, serializavel, exibe);
					
		this.originalNumero = doc.getNumExtDoc();
		this.originalData = doc.getDtDocOriginalDDMMYYYY();
		this.originalOrgao = doc.getOrgaoExterno() != null ? doc.getOrgaoExterno().getDescricao() : null;
		
		this.podeAnexarArquivoAuxiliar = Ex.getInstance().getComp().pode(ExPodeAnexarArquivoAuxiliar.class, titular, lotaTitular, mob);

		
		this.dtLimiteDemandaJudicial = doc.getMobilGeral().getExMovimentacaoSet().stream() //
				.filter(mov -> mov.getExTipoMovimentacao() == ExTipoDeMovimentacao.MARCACAO)
				.filter(mov -> !mov.isCancelada()) //
				.filter(mov -> mov.getMarcador().isDemandaJudicial()) //
				.map(ExMovimentacao::getDtFimMovDDMMYY) //
				.findFirst().orElse(null);
		
		this.dtPrazoDeAssinatura = doc.getDtPrazoDeAssinaturaDDMMYYYYHHMM();
		
		if (exibe)
			exibe();
		
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
	
	public LinkedHashMap<DpPessoa, Long> getListaOrdenadaCossigSub() {
		List<AssinanteVO> listaAsssinantes = mob.getDoc().getListaAssinantesOrdenados();

		for (AssinanteVO assinanteVO : listaAsssinantes) {
			for (Map.Entry<ExMovimentacaoVO, Boolean> cossig : this.cossignatarios.entrySet()) {
				if(assinanteVO.getSubscritor().getSigla().equals(cossig.getKey().getMov().getSubscritor().getSigla())) {
					this.listaOrdenadaCossigSub.put(cossig.getKey().getMov().getSubscritor(), cossig.getValue() ? cossig.getKey().getMov().getIdMov() : 0L);
				}
			}
			if((Ex.getInstance().getComp().pode(ExPodeOrdemAssinatura.class, this.titular, this.lotaTitular, this.doc) || 
					new ExEstaOrdenadoAssinatura(this.doc).eval()) && assinanteVO.getSubscritor().getSigla().equals(doc.getSubscritor().getSigla())) {
				this.listaOrdenadaCossigSub.put(doc.getSubscritor(), 0L);
			}
		}

		return this.listaOrdenadaCossigSub;
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
		
		this.podeAnexarArquivoAuxiliar = Ex.getInstance().getComp().pode(ExPodeAnexarArquivoAuxiliar.class, titular, lotaTitular, mob);
	}

	public ExDocumentoVO(Long idDoc, String sigla, String classificacaoSigla, 
						 String lotaCadastranteString, String cadastranteString, String descrDocumento) {
		this.idDoc = idDoc;
		this.sigla = sigla;
		this.classificacaoSigla = classificacaoSigla;
		this.lotaCadastranteString = lotaCadastranteString;
		this.cadastranteString = cadastranteString;
		this.descrDocumento = descrDocumento;
	}

	public void exibe() {
		List<ITipoDeMovimentacao> movimentacoesPermitidas = new ArrayList<>();

		movimentacoesPermitidas
				.add(ExTipoDeMovimentacao.JUNTADA);
		movimentacoesPermitidas
				.add(ExTipoDeMovimentacao.JUNTADA_A_DOCUMENTO_EXTERNO);
		movimentacoesPermitidas
				.add(ExTipoDeMovimentacao.JUNTADA_EXTERNO);
		movimentacoesPermitidas
				.add(ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA);
		movimentacoesPermitidas
				.add(ExTipoDeMovimentacao.ANEXACAO);
		movimentacoesPermitidas
				.add(ExTipoDeMovimentacao.ANEXACAO_DE_ARQUIVO_AUXILIAR);
		movimentacoesPermitidas
				.add(ExTipoDeMovimentacao.ANOTACAO);
		movimentacoesPermitidas
				.add(ExTipoDeMovimentacao.DESPACHO);
		movimentacoesPermitidas
				.add(ExTipoDeMovimentacao.DESPACHO_INTERNO);
		movimentacoesPermitidas
				.add(ExTipoDeMovimentacao.DESPACHO_INTERNO_TRANSFERENCIA);
		movimentacoesPermitidas
				.add(ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA);
		movimentacoesPermitidas
				.add(ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA_EXTERNA);
		movimentacoesPermitidas
				.add(ExTipoDeMovimentacao.DISPONIBILIZACAO);
		movimentacoesPermitidas
				.add(ExTipoDeMovimentacao.AGENDAMENTO_DE_PUBLICACAO_BOLETIM);
		movimentacoesPermitidas
				.add(ExTipoDeMovimentacao.PUBLICACAO_BOLETIM);
		movimentacoesPermitidas
				.add(ExTipoDeMovimentacao.PEDIDO_PUBLICACAO);
		movimentacoesPermitidas
				.add(ExTipoDeMovimentacao.ENCERRAMENTO_DE_VOLUME);
		movimentacoesPermitidas
				.add(ExTipoDeMovimentacao.COPIA);
		movimentacoesPermitidas
				.add(ExTipoDeMovimentacao.CIENCIA);		
		movimentacoesPermitidas
				.add(ExTipoDeMovimentacao.ENVIO_SIAFEM);	

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
				if (!exMovVO.isCancelada() && movimentacoesPermitidas.contains(exMovVO.getExTipoMovimentacao())) {
					if (exMovVO.getExTipoMovimentacao() == ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA && exMovVO.getMov() != null) {
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
			if (mobilGeral.getMarcasAtivas() != null)
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
		marcas = new ArrayList<ExMarcaVO>();
		Date now = dao().consultarDataEHoraDoServidor(); 
	
		for (ExMobil cadaMobil : doc.getExMobilSet()) {
			SortedSet<ExMarca> setSistema = new TreeSet<>();
			SortedSet<ExMarca> set = cadaMobil.getExMarcaSetAtivas();
			for (ExMarca m : set) {
				if ((m.getDtIniMarca() == null || !m.getDtIniMarca().after(now))
						&& (m.getDtFimMarca() == null || m.getDtFimMarca().after(now))) {
					if (m.getCpMarcador().getIdFinalidade().getIdTpMarcador() == CpTipoMarcadorEnum.TIPO_MARCADOR_SISTEMA)
						setSistema.add(m);
					if (m.getCpMarcador().getIdFinalidade().getIdTpMarcador() != CpTipoMarcadorEnum.TIPO_MARCADOR_SISTEMA && (cadaMobil == mob || cadaMobil.isGeral())) 
						getMarcasDoMobil().add(m);
				}
				marcas.add(new ExMarcaVO(m, titular, lotaTitular));
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
		addAcao(AcaoVO.builder().nome(SigaMessages.getMessage("documento.ver.mais")).descr("Exibe mais detalhes e possibilita auditar todas as movimentações.").icone(SigaMessages.getMessage("icon.ver.mais")).nameSpace("/app/expediente/doc").acao(SigaMessages.getMessage("documento.acao.exibirAntigo"))
				.params("sigla", doc.toString()).params("idVisualizacao", doc.getSigla()).exp(new CpPodeSempre()).pre(numUltMobil < 20 ? "" : "Exibir todos os " + numUltMobil + " volumes do processo simultaneamente pode exigir um tempo maior de processamento. Deseja exibi-los?").classe("once").build());
		
		addAcao(AcaoVO.builder().nome(SigaMessages.getMessage("documento.ver.impressao")).descr("Apresenta a versão em PDF do documento para fins de impressão.")
				.icone(SigaMessages.getMessage("icon.ver.impressao")).nameSpace("/app/arquivo").acao("exibir")
				.params("popup", "true").params("arquivo", doc.getReferenciaPDF())
				.params("idVisualizacao", Long.toString(idVisualizacao))
				.params("nomeAcao", SigaMessages.getMessage("documento.ver.impressao"))
				.exp(new CpPodeSempre()).build());
		
		addAcao(AcaoVO.builder().nome(SigaMessages.getMessage("documento.ver.dossie")).descr("Exibe um índice de todos os documentos juntados.").icone("folder_magnify").nameSpace("/app/expediente/doc").acao("exibirProcesso")
				.params("sigla", doc.toString()).params("idVisualizacao", Long.toString(idVisualizacao))
				.params("nomeAcao", SigaMessages.getMessage("documento.ver.dossie"))
				.exp(new CpPodeSempre()).build());
		
		docVO.getMobs().get(0).setAcoes(vo.getAcoes());
	}
	
	public String obterDataPrimeiraAssinatura(ExDocumento doc) {
		String retorno = "";
		
		List<ExMovimentacao> lista = new ArrayList<ExMovimentacao>();
		lista.addAll(doc.getMobilGeral().getMovsNaoCanceladas(ExTipoDeMovimentacao.ASSINATURA_COM_SENHA));
		lista.addAll(doc.getMobilGeral().getMovsNaoCanceladas(ExTipoDeMovimentacao.ASSINATURA_DIGITAL_DOCUMENTO));
		lista.addAll(doc.getMobilGeral().getMovsNaoCanceladas(ExTipoDeMovimentacao.CONFERENCIA_COPIA_COM_SENHA));
		
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
		
		vo.addAcao(AcaoVO.builder().nome(SigaMessages.getMessage("documento.ver.dossie"))
				.descr("Exibe um índice de todos os documentos juntados.")
				.icone("folder_magnify")
				.nameSpace("/app/expediente/doc").acao("exibirProcesso")
				.params("sigla", mob.getCodigoCompacto())
				.params("nomeAcao", SigaMessages.getMessage("documento.ver.dossie"))				
				.exp(new ExPodeVisualizarImpressao(mob, titular, lotaTitular)).classe("once").build());
		
		vo.addAcao(AcaoVO.builder().nome(SigaMessages.getMessage("documento.ver.impressao"))
				.descr("Apresenta a versão em PDF do documento para fins de impressão.")
				.icone(SigaMessages.getMessage("icon.ver.impressao")).nameSpace("/app/arquivo").acao("exibir")
				.params("sigla", mob.getCodigoCompacto()).params("popup", "true")
				.params("arquivo", doc.getReferenciaPDF())
				.params("nomeAcao", SigaMessages.getMessage("documento.ver.impressao"))
				.exp(new ExPodeVisualizarImpressao(mob, titular, lotaTitular)).classe("once").build());
		
		vo.addAcao(AcaoVO.builder().nome("Fina_lizar").icone("lock").descr("Conclui a elaboração do documento fazendo com que ele deixe de ser temporário (TMP) e atribui seu código definitivo.").nameSpace("/app/expediente/doc").acao("finalizar")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeFinalizar(doc, titular, lotaTitular)).classe("once").build());

		vo.addAcao(AcaoVO.builder().nome("Edita_r").descr("Exibe a página de edição para que ajustes possam ser feitos.").icone("pencil").nameSpace("/app/expediente/doc").acao("editar")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeEditar(mob, titular, lotaTitular)).build());

		vo.addAcao(AcaoVO.builder().nome("Excluir").descr("Exclui definitivamente o documento temporário. Atenção, esta ação não pode ser desfeita.").icone("delete").nameSpace("/app/expediente/doc").acao("excluir")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeExcluir(mob, titular, lotaTitular)).msgConfirmacao("Confirma a exclusão do documento?").classe("once").build());

		vo.addAcao(AcaoVO.builder().nome("Incluir Cossignatário").descr("Acrescenta um novo cossignatário (assinante) ao documento.").icone("user_add").nameSpace("/app/expediente/mov").acao("incluir_cosignatario")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeIncluirCossignatario(doc, titular, lotaTitular)).classe("once").build());

		vo.addAcao(AcaoVO.builder().nome("Ane_xar").descr("Captura um novo PDF e junta ele ao documento.").icone("attach").nameSpace("/app/expediente/mov").acao("anexar")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeAnexarArquivo(mob, titular, lotaTitular)).build());

		vo.addAcao(AcaoVO.builder().nome("_Anotar").descr("Acrescentar uma movimentação de anotação ao documento. As anotações podem ser excluídas a qualquer momento.").icone("note_add").nameSpace("/app/expediente/mov").acao("anotar")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeFazerAnotacao(mob, titular, lotaTitular)).build());

		vo.addAcao(AcaoVO.builder().nome(SigaMessages.getMessage("documento.definir.perfil")).descr("Indica que uma pessoa ou lotação desempenha um determinado papel em relação ao documento.").icone("folder_user").nameSpace("/app/expediente/mov").acao("vincularPapel")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeFazerVinculacaoDePapel(mob, titular, lotaTitular)).build());

		vo.addAcao(AcaoVO.builder().nome("Criar Via").descr("Cria uma via adicional que pode ser tramitada de forma independente.").icone("add").nameSpace("/app/expediente/doc").acao("criar_via")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeCriarVia(mob, titular, lotaTitular)).classe("once").build());

		vo.addAcao(AcaoVO.builder().nome("Abrir Novo Volume").descr("Inicia um novo volume no processo.").icone("add").nameSpace("/app/expediente/doc").acao("criar_volume")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeCriarVolume(mob, titular, lotaTitular)).msgConfirmacao("Confirma a abertura de um novo volume?").classe("once").build());

		vo.addAcao(AcaoVO.builder().nome("Criar Subprocesso").descr("Cria um subprocesso, filho do processo atual. Os subprocessos tem numeração semelhante, mas que é acrescida de um sufixo indicativo do número de sequência.").icone("link_add").nameSpace("/app/expediente/doc").acao("editar")
				.params("mobilPaiSel.sigla", mob.getCodigoCompacto()).params("idForma", Long.toString(mob.doc().getExFormaDocumento().getIdFormaDoc()))
				.params("criandoSubprocesso", "true").exp(new ExPodeCriarSubprocesso(mob, titular, lotaTitular)).classe("once").build());

		vo.addAcao(AcaoVO.builder().nome("Registrar A_ssinatura Manual").descr("Informa que a assinatura manual já foi realizada no documento físico original.").icone("script_edit").nameSpace("/app/expediente/mov").acao("registrar_assinatura")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeRegistrarAssinatura(mob, titular, lotaTitular)).classe("once").build());

		vo.addAcao(AcaoVO.builder().nome("A_ssinar").descr("Assina o documento utilizando certificado digital ou usuário e senha.").icone("script_key").nameSpace("/app/expediente/mov").acao("assinar")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeAssinar(mob, titular, lotaTitular)).classe("once").build());

        vo.addAcao(AcaoVO.builder().nome("A_utenticar").descr("Autentica o documento utilizando certificado digital ou usuário e senha.").icone("script_key").nameSpace("/app/expediente/mov").acao("autenticar_documento")
				.params("sigla", mob.getCodigoCompacto()).exp(serializavel ? new ExPodeAutenticarComSenha(doc, titular, lotaTitular) : new ExPodeAutenticarDocumento(doc, titular, lotaTitular)).classe("once").build());

		vo.addAcao(AcaoVO.builder().nome("Solicitar Assinatura").descr("Confirma que o documento já está revisado e solicita que o subscritor produza a assinatura.").icone("page_go").nameSpace("/app/expediente/mov").acao("solicitar_assinatura")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeSolicitarAssinatura(doc, titular, lotaTitular)).msgConfirmacao("Ao clicar em prosseguir, você estará sinalizando que revisou este documento e que ele deve ser incluído na lista para ser assinado em lote. Prosseguir?").classe("once").build());

		vo.addAcao(AcaoVO.builder().nome("Assinar Anexos Gerais").descr("Exibe a página de Anexos Pendentes de Assinatura.").icone("script_key").nameSpace("/app/expediente/mov").acao("assinarAnexos")
				.params("sigla", mob.getCodigoCompacto()).params("assinandoAnexosGeral", "true").exp(And.of(new ExEstaFinalizado(doc), new ExTemAnexos(mob))).build());

		vo.addAcao(AcaoVO.builder().nome("Redefinir Acesso").descr("Altera o nível de sigilo do documento.").icone("shield").nameSpace("/app/expediente/mov").acao("redefinir_nivel_acesso")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeRedefinirNivelDeAcesso(mob, titular, lotaTitular)).build());

		vo.addAcao(AcaoVO.builder().nome("Restrição de Acesso").icone("group_link").nameSpace("/app/expediente/mov").acao("restringir_acesso")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeRestringirAcesso(mob, titular, lotaTitular)).build());

		vo.addAcao(AcaoVO.builder().nome("Redefinir Acesso Padrão").icone("arrow_undo").nameSpace("/app/expediente/mov").acao("desfazer_restricao_acesso")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeDesfazerRestricaoDeAcesso(mob, titular, lotaTitular)).msgConfirmacao("Esta operação anulará as Restrições de Acesso. Prosseguir?").build());

		vo.addAcao(AcaoVO.builder().nome("Solicitar Publicação no Boletim").descr("Solicita que seja incluído na lista de documentos que serão publicados no próximo boletim interno.").icone("book_add").nameSpace("/app/expediente/mov").acao("boletim_agendar")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeAgendarPublicacaoNoBoletim(mob, titular, lotaTitular)).build());

		vo.addAcao(AcaoVO.builder().nome("Registrar Publicação do Boletim").descr("Informa que o documento foi publicado no boletim interno.").icone("book_link").nameSpace("/app/expediente/mov").acao("boletim_publicar")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodePublicar(mob, titular, lotaTitular)).classe("once").build());

		vo.addAcao(AcaoVO.builder().nome("Refazer").descr("Cancela este documento e cria uma cópia dele em um documento temporário.").icone("error_go").nameSpace("/app/expediente/doc").acao("refazer")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeRefazer(mob, titular, lotaTitular)).msgConfirmacao(SigaMessages.getMessage("mensagem.cancela.documento")).classe("once siga-btn-refazer").build());

		vo.addAcao(AcaoVO.builder().nome("Duplicar").descr("Cria um documento temporário que é uma cópia exata deste.").icone("arrow_divide").nameSpace("/app/expediente/doc").acao("duplicar")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeDuplicar(mob, titular, lotaTitular)).msgConfirmacao(SigaMessages.getMessage("documento.confirma.duplica")).classe("once").build());

		vo.addAcao(AcaoVO.builder().nome(SigaMessages.getMessage("documento.ver.mais")).descr("Exibe mais detalhes e possibilita auditar todas as movimentações.").icone(SigaMessages.getMessage("icon.ver.mais")).nameSpace("/app/expediente/doc").acao(SigaMessages.getMessage("documento.acao.exibirAntigo"))
				.params("sigla", mob.getCodigoCompacto()).exp(new CpPodeSempre()).msgConfirmacao(doc.getNumUltimoMobil() < 20 ? "" : "Exibir todos os " + doc.getNumUltimoMobil() + " volumes do processo simultaneamente pode exigir um tempo maior de processamento. Deseja exibi-los?").classe("once").build());

		vo.addAcao(AcaoVO.builder().nome("Auditar").descr("Apresenta informações mais detalhadas sobre o documento, inclusive exibindo movimenções canceladas.").icone("magnifier").nameSpace("/app/expediente/doc").acao("exibirAntigo")
				.params("sigla", mob.getCodigoCompacto()).params("exibirCompleto", "true").exp(new CpPodeBoolean(exibirAntigo, "auditando")).classe("once").build());

		vo.addAcao(AcaoVO.builder().nome("Agendar Publicação no Diário").descr("Agenda a publicação deste documento no diário eletrônico.").icone("report_link").nameSpace("/app/expediente/mov").acao("agendar_publicacao")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeAgendarPublicacao(mob, titular, lotaTitular)).classe("once").build());

		vo.addAcao(AcaoVO.builder().nome("Agendar Publicação DOE").icone("report_link").nameSpace("/app/expediente/mov").acao("agendar_publicacao_doe")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeAgendarPublicacaoDOE(mob, titular, lotaTitular)).classe("once").build());
		
		vo.addAcao(AcaoVO.builder().nome("Solicitar Publicação no Diário").descr("Solicita a publicação deste documento no diário eletrônico.").icone("report_add").nameSpace("/app/expediente/mov").acao("pedirPublicacao")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodePedirPublicacao(mob, titular, lotaTitular)).classe("once").build());

		vo.addAcao(AcaoVO.builder().nome("Desfazer Cancelamento").descr("Anula o cancelamento e torna o documento novamente editável.").icone("arrow_undo").nameSpace("/app/expediente/doc").acao("desfazerCancelamentoDocumento")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeDesfazerConcelamentoDeDocumento(mob, titular, lotaTitular)).msgConfirmacao("Esta operação anulará o cancelamento do documento e tornará o documento novamente editável. Prosseguir?").classe("once").build());

		vo.addAcao(AcaoVO.builder().nome("Cancelar").descr("Cancela documento já assinado, tornando ele sem efeito.").icone("delete").nameSpace("/app/expediente/doc").acao("tornarDocumentoSemEfeito")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeTornarDocumentoSemEfeito(mob, titular, lotaTitular)).msgConfirmacao(SigaMessages.getMessage("mensagem.semEfeito.documento")).classe("once siga-btn-tornar-documento-sem-efeito").build());

		vo.addAcao(AcaoVO.builder().nome("Cancelar").descr("Cancela documento pendente de assinatura.").icone("cancel").nameSpace("/app/expediente/doc").acao("cancelarDocumento")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeCancelarDocumento(doc, titular, lotaTitular)).msgConfirmacao("Esta operação cancelará o documento pendente de assinatura. Prosseguir?").classe("once").build());
		
		vo.addAcao(AcaoVO.builder().nome("Vi_ncular").icone("page_find").nameSpace("/app/expediente/mov").acao("referenciar")
				.params("sigla", mob.getCodigoCompacto()).exp(new ExPodeReferenciar(mob, titular, lotaTitular)).build());

		vo.addAcao(AcaoVO.builder().nome(SigaMessages.getMessage("documento.publicar.portaltransparencia"))
				.icone("report_link").nameSpace("/app/expediente/mov").acao("publicacao_transparencia")
				.params("sigla", mob.getCodigoCompacto())
				.exp(new ExPodePublicarPortalDaTransparencia(mob, titular, lotaTitular)).classe("once").build());
		
		vo.addAcao(AcaoVO.builder().nome("Gerar Protocolo").icone("printer").nameSpace("/app/expediente/doc").acao("gerarProtocolo")
				.params("sigla", mob.getCodigoCompacto()).params("popup", "true").exp(And.of(new CpPodeBoolean(mostrarGerarProtocolo(doc), "pode mostrar protocolo"), new ExPodeGerarProtocolo(doc, titular, lotaTitular))).classe("once").build());
		
		vo.addAcao(AcaoVO.builder().nome("Enviar ao SIAFEM").icone("email_go").nameSpace("/app/expediente/integracao").acao("integracaows")
				.params("sigla", mob.getCodigoCompacto()).exp(And.of(new CpPodeBoolean(mostrarEnviarSiafem(doc), "pode mostrar Siafem"), new ExPodeEnviarSiafem(doc, titular, lotaTitular))).classe("once").build());

		vo.addAcao(AcaoVO.builder().nome(SigaMessages.getMessage("documento.enviar.visualizacaoexterna"))
				.icone("email_go").nameSpace("/app/expediente/mov").acao("enviar_para_visualizacao_externa")
				.params("sigla", mob.getSigla()).params("popup", "true")
				.exp(new ExPodeEnviarParaVisualizacaoExterna(mob, titular, lotaTitular)).build());
		
		vo.addAcao(AcaoVO.builder().nome("Download").descr("Faz o download do arquivo de formato livre associado a este documento.").icone("arrow_down").nameSpace("/app/arquivo").acao("downloadFormatoLivre")
				.params("sigla", doc.getCodigoCompacto()).exp(new ExPodeFazerDownloadFormatoLivre(doc)).classe("once").build());
	}

	private boolean mostrarEnviarSiafem(ExDocumento doc) {
		return Ex.getInstance().getBL().obterFormularioSiafem(doc) != null && doc.getPrimeiraVia() != null;
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

	public void setCodigoUnico(String codigoUnico) {
		this.codigoUnico = codigoUnico;		
	}
	
	public String getCodigoUnico() {
		return this.codigoUnico;		
	}
	
	public String getTipoDePrincipal() {
		return tipoDePrincipal;
	}

	public void setTipoDePrincipal(String tipoDePrincipal) {
		this.tipoDePrincipal = tipoDePrincipal;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getPrincipalCompacto() {
		return principalCompacto;
	}

	public void setPrincipalCompacto(String principalCompacto) {
		this.principalCompacto = principalCompacto;
	}

	public Long getIdDoc() {
		return idDoc;
	}

	public void setIdDoc(Long idDoc) {
		this.idDoc = idDoc;
	}

	public String getClassificacaoSigla() {
		return classificacaoSigla;
	}

	public void setClassificacaoSigla(String classificacaoSigla) {
		this.classificacaoSigla = classificacaoSigla;
	}

	public void setCadastranteString(String cadastranteString) {
		this.cadastranteString = cadastranteString;
	}

	public void setLotaCadastranteString(String lotaCadastranteString) {
		this.lotaCadastranteString = lotaCadastranteString;
	}
}