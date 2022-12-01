package br.gov.jfrj.siga.ex.bl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringEscapeUtils;

import com.crivano.swaggerservlet.ISwaggerModel;

import br.gov.jfrj.siga.base.Data;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorGrupoEnum;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeMovimentacao;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;

public class Mesa2 {
	private static final int MESA_QTD_MAX_INICIAL = 20;
	private static List<GrupoItem> gruposBase;
	
	public static class SelGrupo implements ISwaggerModel {
		public String grupoOrdem;
		public Long grupoQtd;
		public Long grupoQtdLota;
		public boolean grupoCollapsed;
		public boolean grupoHide;
	}
	
	public static class GrupoItem implements ISwaggerModel {
		public String grupo;
		public String grupoNome;
		public String grupoIcone;
		public Long grupoCounterUser;	// Qtd de docs para o usuario (Contador na barra de titulo)
		public Long grupoCounterLota;	// Qtd de docs para a lotação (Contador na barra de título)
		public Long grupoCounterAtivo;	// Qtd de docs para o usuário ou lotação (o que estiver ativo)
		public Long grupoQtd; 			// Qtd possivel de rolagem na tela (pessoa)
		public Long grupoQtdLota;		// Qtd possivel de rolagem na tela (lotacao)
		public String grupoOrdem;		// Ordem e id do grupo
		public boolean grupoCollapsed;
		public boolean grupoHide;
		public boolean grupoAtingiuLimite;
		public List<MesaItem> grupoDocs;
		public List<Integer> grupoMarcadores;
	}
	
	public static class MesaItem implements ISwaggerModel {
		public String offset;
		public String grupoOrdem;
		public String tipo;
		public Date datahora;
		public String datahoraDDMMYYYHHMM;
		public String tempoRelativo;
		public String codigo;
		public String sigla;
		public String descr;
		public String origem;
		public String anotacao;
		public String dataDevolucao;
		public String tipoDoc;
		public String lotaPosse;
		public String lotaCadastrante;
		public String nomePessoaPosse;
		public List<Marca> list;
	}

	public static class Marca implements ISwaggerModel {
		public String pessoa;
		public String lotacao;
		public String nome;
		public String icone;
		public String titulo;
		public Date inicio;
		public Date termino;
		public String ref1;
		public String ref2;
		public String ref1DDMMYYYY;
		public String ref2DDMMYYYY;
		public Boolean daPessoa;
		public Boolean deOutraPessoa;
		public Boolean daLotacao;
		public String cor;
	}

	public static class DocDados {
		List<MeM> listMeM;
		Date movUltimaDtIniMov;
		Date movUltimaDtFimMov;
		String movTramiteSiglaOrgao;
		String movTramiteSiglaLotacao;
		String movTramiteNomePessoa;
		String movAnotacaoDescrMov;
		boolean isComposto;
	}

	public static class MeM {
		ExMarca marca;
		CpMarcador marcador;
		Date dtRef1;
		Date dtRef2;
	}

	private static List<MesaItem> listarReferencias(
			final List<ExMobil> references, final DpPessoa pessoa,
			final DpLotacao lota, final String grupoOrdem, final boolean trazerAnotacoes, 
			final boolean ordemCrescenteData, final boolean usuarioPosse, final Integer offset) {
		ExDao dao = ExDao.getInstance();
		Date currentDate = dao.consultarDataEHoraDoServidor();
		List<MesaItem> l = new ArrayList<>();
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Integer offsetItem = offset;

		for (ExMobil mobil : references) {
			MesaItem r = new MesaItem();
			r.tipo = "Documento";
			r.grupoOrdem = grupoOrdem;
			r.offset = offsetItem.toString();
			offsetItem++;

			try {
				r.sigla = mobil.getDnmSigla();
				r.codigo = mobil.getCodigoCompacto();
				Date datahora = null;
				ExMovimentacao ultimaMov = mobil.getUltimaMovimentacaoNaoCanceladaENaoCanceladora();
				datahora = mobil.getDnmDataUltimaMovimentacaoNaoCancelada();
				if (datahora == null)
					datahora = mobil.getDoc().getDtAltDoc();
				r.datahora = datahora;
				r.datahoraDDMMYYYHHMM = df.format(datahora);
				r.tempoRelativo = Data.calcularTempoRelativo(datahora);
				r.descr = StringEscapeUtils.unescapeHtml4(mobil.doc().getDescrCurta(255).replace("\r", " ").replace("\f", " ").replace("\n", " "));
				r.tipoDoc = (mobil.doc().isComposto()? "Composto" : "Avulso");
				ExMovimentacao ultMovPosse = null;
	
				if (mobil.doc().getSubscritor() != null
						&& mobil.doc().getSubscritor().getLotacao() != null) {
					if (SigaMessages.isSigaSP()) {
						ultMovPosse = mobil
								.getUltimaMovimentacao(ExMovimentacao.tpMovimentacoesDePosse, 
										new ITipoDeMovimentacao[] {}, mobil, false, null, false);
						
						if (ultMovPosse != null) {
							r.origem = ultMovPosse.getLotacao()
									.getOrgaoUsuario().getSigla() + " / "
									+ ultMovPosse.getLotacao().getSigla();
						} else {
							r.origem = mobil.doc().getSubscritor().getLotacao()
									.getOrgaoUsuario() + " / "
									+ mobil.doc().getSubscritor().getLotacao()
											.getSigla();
						}
					} else {
						r.origem = mobil.doc().getSubscritor().getLotacao()
								.getSigla();
					}
				}
				
				r.dataDevolucao = "ocultar";
				
				if (mobil.doc().getSubscritor() != null
						&& mobil.doc().getLotacao() != null) {
					if (SigaMessages.isSigaSP()) {
						if (ultMovPosse != null && ultMovPosse.getLotacao() != null) {
							r.lotaCadastrante = ultMovPosse.getLotacao().getSigla();
						} else {
							r.lotaCadastrante = mobil.doc().getLotacao().getSigla();
						}
					} else {
						r.lotaCadastrante = mobil.doc().getLotacao().getSigla();
					}
				}
				
				r.dataDevolucao = "ocultar";
	
				if (ultimaMov != null && ultimaMov.getDtFimMov() != null) {
					Date dataMovimentacao = ultimaMov.getDtFimMov();
					Date dataHoje = new Timestamp(System.currentTimeMillis());
					int dias;
					if (dataHoje.compareTo(dataMovimentacao) <= 0) {
						dias = Integer
								.parseInt(((dataMovimentacao.getTime() - dataHoje.getTime() - +3600000L) / 86400000L)
										+ "");
						String qtdDias = Prop.get("/siga.devolucao.dias");
						if(qtdDias == null){
							qtdDias = "5";
						}
						
						if (dias <= Integer.parseInt(qtdDias)) {
							r.dataDevolucao = "alerta";
						}
					} else {
						r.dataDevolucao = "atrasado";
					}
				}
	
				if (trazerAnotacoes && mobil.getDnmUltimaAnotacao() != null && !mobil.getDnmUltimaAnotacao().replace(" ", "").equals("")) { 
					r.anotacao = mobil.getDnmUltimaAnotacao().replace("\r\f", "<br/>").replace("\n", "<br/>");
				}
	
				if (usuarioPosse) {
					if (ultMovPosse == null)
						ultMovPosse = mobil
								.getUltimaMovimentacao(ExMovimentacao.tpMovimentacoesDePosse, 
										new ITipoDeMovimentacao[] {}, mobil, false, null, false);
						
					if (ultMovPosse != null && ultMovPosse.getCadastrante() != null) {
						r.nomePessoaPosse = ultMovPosse.getCadastrante().getNomePessoa(); 
						r.lotaPosse = ultMovPosse.getCadastrante().getLotacao().getSigla();
					} else {
						r.nomePessoaPosse = mobil.getDoc().getCadastrante().getNomePessoa();
						r.lotaPosse = mobil.getDoc().getCadastrante().getLotacao().getSigla();
					}
				}
				
				r.list = new ArrayList<Marca>();
				Set<ExMarca> lstMarcas = mobil.getExMarcaSetAtivas();
	
				for (ExMarca marca : lstMarcas) {
					if (!((pessoa != null && marca.getDpPessoaIni() != null
							&& pessoa.equivale(
									marca.getDpPessoaIni()))
						|| (lota != null && marca.getDpLotacaoIni() != null
							&& lota.equivale(
									marca.getDpLotacaoIni()))))
						continue;
					
					ExMovimentacao mov = marca.getExMovimentacao();
					if (marca.getDtIniMarca() != null
							&& marca.getDtIniMarca().getTime() > currentDate
									.getTime())
						continue;
					if (marca.getDtFimMarca() != null
							&& marca.getDtFimMarca().getTime() < currentDate
									.getTime())
						continue;
	
					Marca t = new Marca();
					CpMarcador marcador = marca.getCpMarcador();
					CpMarcadorEnum mar = CpMarcadorEnum.getById(marcador
							.getIdInicial().intValue());
					
					if (mar != null) {
						if(mar.getId() == CpMarcadorEnum.MOVIMENTACAO_ASSINADA_COM_SENHA.id
								|| mar.getId() == CpMarcadorEnum.DOCUMENTO_ASSINADO_COM_SENHA.id) 
							continue;
							
						t.nome = mar.getNome();
						t.icone = mar.getIcone();
						if(mar.getId() == CpMarcadorEnum.SEM_EFEITO.id) {
							t.nome = SigaMessages.getMessage("marcador.semEfeito.label");
						} else if(mar.getId() == CpMarcadorEnum.CANCELADO.id) {
							t.nome = SigaMessages.getMessage("marcador.cancelado.label");
						}
					} else {
						t.nome = marcador.getDescrMarcador();
						t.icone = marcador.getIdIcone().getCodigoFontAwesome();
						t.cor = marcador.getIdCor().getDescricao();
					}
					t.titulo = Data.formatDDMMYY(marca.getDtIniMarca()) + " (" 
								+ Data.calcularTempoRelativo(marca.getDtIniMarca()) + ")";
	
					if (marca.getDpPessoaIni() != null) {
						t.pessoa = marca.getDpPessoaIni().getIdInicial().toString();
	
					}
					if (marca.getDpLotacaoIni() != null) {
						t.lotacao = marca.getDpLotacaoIni().getIdInicial().toString();
					}
					
					t.inicio = marca.getDtIniMarca();
					t.termino = marca.getDtFimMarca();
					if (mov != null) {
						if (mov.getDtParam1() != null) {
							t.ref1 = Data.calcularTempoRelativoEmDias(mov.getDtParam1());
							t.ref1DDMMYYYY = Data.formatDDMMYY(mov.getDtParam1());
						}
						if (mov.getDtParam2() != null) {
							t.ref2 = Data.calcularTempoRelativoEmDias(mov.getDtParam2());
							t.ref2DDMMYYYY = Data.formatDDMMYY(mov.getDtParam2());
						}
						if(marca.getCpMarcador().isDemandaJudicial()) {
							t.nome += " até " + marca.getExMobil().getDoc().getMobilGeral()
									.getExMovimentacaoSet().stream() //
									.filter(mv -> mv.getExTipoMovimentacao() ==
											ExTipoDeMovimentacao.MARCACAO)
									.filter(mv -> !mv.isCancelada()) //
									.filter(mv -> marca.getCpMarcador().equals(mv.getMarcador())) //
									.map(ExMovimentacao::getDtFimMovDDMMYY) //
									.findFirst().orElse("[indeterminado]");
						}

						if(marca.getCpMarcador().isADevolverForaDoPrazo()) {
							t.nome += ", atribuído pela unidade " + marca.getExMobil()
									.getExMovimentacaoSet().stream() //
									.filter(mv -> mv.getExTipoMovimentacao() ==
											ExTipoDeMovimentacao.MARCACAO)
									.filter(mv -> !mv.isCancelada()) //
									.filter(mv -> marca.getCpMarcador().equals(mv.getMarcador())) //
									.map(ExMovimentacao::getCadastranteString) //
									.findFirst().orElse(r.lotaCadastrante);
						}
					}
	
					if (pessoa != null && marca.getDpPessoaIni() != null) {
						if (pessoa.getIdInicial().equals(
								marca.getDpPessoaIni().getId()))
							t.daPessoa = true;
						else
							t.deOutraPessoa = marca.getDpPessoaIni() != null;
					}
					if (lota != null
							&& marca.getDpLotacaoIni() != null
							&& lota.getId().equals(
									marca.getDpLotacaoIni().getId()))
						t.daLotacao = true;
					
					r.list.add(t);
				}
			} catch (Exception e) {
				e.printStackTrace();
				r.descr = "*** Doc. corrompido (Mobil:" + mobil + ") *** " + ( r.descr != null ? r.descr : "" );
			}
			l.add(r);
		}
		return l;
	}

	public static List<GrupoItem> getContadores(final boolean contar, final DpPessoa titular, 
			final DpLotacao lotaTitular, final Map<String, SelGrupo> selGrupos, final boolean exibeLotacao, 
			final List<Integer> marcasAIgnorar, final String filtro) throws Exception {
		List<GrupoItem> gruposMesa = new ArrayList<GrupoItem>();
		gruposMesa = montaGruposUsuario(selGrupos);
		if (!contar)
			return gruposMesa;
		
		List<Object[]> listaPessoa = ExDao.getInstance().listarMobilsPorGrupoEMarcas(true, null, null, titular, null, true, marcasAIgnorar, null, filtro);
		List<Object[]> listaLotacao = ExDao.getInstance().listarMobilsPorGrupoEMarcas(true, null, null, titular, titular.getLotacao(), true, marcasAIgnorar, null, filtro);
		
		if (listaPessoa == null && listaLotacao == null)
			return gruposMesa;
		
		for (GrupoItem gItem : gruposMesa) {
			gItem.grupoCounterUser = 0L;
			gItem.grupoCounterLota = 0L;
			for (Object[] reference : listaPessoa) {
				if (gItem.grupoOrdem.equals(reference[0].toString())) {
					if (reference[1] != null)  
						gItem.grupoCounterUser = Long.valueOf(reference[1].toString());
				}
			}
			for (Object[] reference : listaLotacao) {
				if (gItem.grupoOrdem.equals(reference[0].toString())) {
					if (reference[1] != null)  
						gItem.grupoCounterLota = Long.valueOf(reference[1].toString());
				}
			}
			if (exibeLotacao) {
				gItem.grupoCounterAtivo = gItem.grupoCounterLota;
			} else {
				gItem.grupoCounterAtivo = gItem.grupoCounterUser;
			}
		}
		return gruposMesa;
	}

	public static List<GrupoItem> getMesa(final boolean contar, final Integer qtd, final Integer offset, final DpPessoa titular, final Map<String, SelGrupo> selGrupos, 
			final boolean exibeLotacao, final boolean trazerAnotacoes, final boolean trazerComposto, final boolean ordemCrescenteData,
			final boolean usuarioPosse, List<Integer> marcasAIgnorar, final String filtro) throws Exception {
//		long tempoIni = System.nanoTime();
		ExDao dao = ExDao.getInstance();

		List<Mesa2.GrupoItem> gruposMesa = getContadores(contar, titular, titular.getLotacao(), selGrupos, exibeLotacao, marcasAIgnorar, filtro);

		int qtTotal = 0;
		for (Mesa2.GrupoItem g : gruposMesa) {
			if (g.grupoCollapsed || g.grupoHide)
				continue;
			List<CpMarcadorGrupoEnum> lGrp = Arrays.asList(new CpMarcadorGrupoEnum[] 
					{CpMarcadorGrupoEnum.getById(Integer.valueOf(g.grupoOrdem))});
			List<Object[]> l;
			int parmOffset = (offset != null? offset : 0);
			int q = (qtd != null && qtd < MESA_QTD_MAX_INICIAL? qtd : MESA_QTD_MAX_INICIAL);
			if (exibeLotacao) {
				l = dao.listarMobilsPorGrupoEMarcas(false, q, parmOffset, titular,
						titular.getLotacao(), ordemCrescenteData, marcasAIgnorar, lGrp, filtro);
			} else {
				l = dao.listarMobilsPorGrupoEMarcas(false, q, parmOffset, titular,
						null, ordemCrescenteData, marcasAIgnorar, lGrp, filtro);
			}
			
			// Cria hashmap contendo grupo e mobils do grupo
			Map<String, List<ExMobil>> hashMobGrp = l.stream()
			        .collect(Collectors.groupingBy(k -> ((k[0]).toString()), 
			                Collectors.mapping(v -> dao.consultar(Long.valueOf(v[1].toString()), ExMobil.class, false), 
			                		Collectors.toList())));
	
			// Insere no grupo os documentos e seus dados a serem apresentados na mesa
			for (Map.Entry<String, List<ExMobil>> entry : hashMobGrp.entrySet()) {
				g.grupoDocs = Mesa2.listarReferencias(entry.getValue(), titular,
						titular.getLotacao(), g.grupoOrdem, trazerAnotacoes, ordemCrescenteData, 
						usuarioPosse, parmOffset);
				if (exibeLotacao) {
					g.grupoQtdLota = g.grupoQtdLota > 0 ? g.grupoQtdLota : l.size();
				} else {
					g.grupoQtd = g.grupoQtd > 0 ? g.grupoQtd : l.size();
				}
			}
			qtTotal = qtTotal + l.size();
			if (qtTotal >= MESA_QTD_MAX_INICIAL)
				break;
		}
//		long tempoTotal = System.nanoTime() - tempoIni;
//		System.out.println("getMesa: " + tempoTotal	/ 1000000 + " ms ==> ");
		return gruposMesa;
	}

	public static void carregaGruposBase() {
		if (gruposBase == null) {
			gruposBase = new ArrayList<GrupoItem>();
			for (CpMarcadorGrupoEnum gEnum : CpMarcadorGrupoEnum.values()) {
				if (gEnum.isVisible()) {
					GrupoItem grpItem = new GrupoItem();
					grpItem.grupo = gEnum.name();
					grpItem.grupoOrdem = Integer.toString(gEnum.getId());
					grpItem.grupoNome = gEnum.getNome();
					grpItem.grupoIcone = gEnum.getIcone();
					grpItem.grupoCollapsed = gEnum.isCollapsed();
					if (gEnum.isCollapsed()) {
						grpItem.grupoQtd = 0L;
						grpItem.grupoQtdLota = 0L;
					} else {
						grpItem.grupoQtd = 15L;
						grpItem.grupoQtdLota = 15L;
					}
					grpItem.grupoHide = gEnum.isHide();
					grpItem.grupoMarcadores = CpMarcadorEnum.getListIdByGrupo(gEnum.getNome());
					gruposBase.add(grpItem);
				}
			}
		}
	}
	
	public static List<GrupoItem> montaGruposUsuario(Map<String, SelGrupo> selGrupos) {
		carregaGruposBase();
		List<String> ordemGrupos = new ArrayList<String>(); 
		List<GrupoItem> lGrupo = new ArrayList<GrupoItem>();
		if (selGrupos != null && selGrupos.size() > 0) {
			for (Map.Entry<String, SelGrupo> selGrupo : selGrupos.entrySet())
				ordemGrupos.add(CpMarcadorGrupoEnum.getByNome(selGrupo.getKey()).getId().toString());
		} else {
			ordemGrupos = CpMarcadorGrupoEnum.getIdList();
		}
		for (GrupoItem grp : gruposBase) {
			for (String ordemGrupo : ordemGrupos) {
				if (grp.grupoOrdem.equals(ordemGrupo)) {
					GrupoItem grpItem = new GrupoItem();
					grpItem.grupo = grp.grupo;
					grpItem.grupoOrdem = grp.grupoOrdem;
					grpItem.grupoNome = grp.grupoNome;
					grpItem.grupoIcone = grp.grupoIcone;
					if (selGrupos != null && selGrupos.size() > 0) {
						grpItem.grupoCollapsed = selGrupos.get(grp.grupoNome).grupoCollapsed;
						grpItem.grupoHide = selGrupos.get(grp.grupoNome).grupoHide;
						grpItem.grupoQtd = selGrupos.get(grp.grupoNome).grupoQtd;
						grpItem.grupoQtdLota = selGrupos.get(grp.grupoNome).grupoQtdLota;
					} else {
						grpItem.grupoCollapsed = grp.grupoCollapsed;
						grpItem.grupoHide = grp.grupoHide;
						grpItem.grupoQtd = grp.grupoQtd;
						grpItem.grupoQtdLota = grp.grupoQtdLota;
					}
					grpItem.grupoMarcadores = grp.grupoMarcadores;
					lGrupo.add(grpItem);
				}
			}
		}
		return lGrupo;
	}
}
