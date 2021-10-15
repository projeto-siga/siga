package br.gov.jfrj.siga.ex.bl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.crivano.swaggerservlet.ISwaggerModel;

import br.gov.jfrj.siga.base.Data;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorGrupoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.cp.model.enm.TipoDePainelEnum;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;

public class Mesa2 {
	private static List<GrupoItem> gruposBase;
	
	public static class SelGrupo implements ISwaggerModel {
		public String grupoOrdem;
		public Long grupoQtd;
		public Long grupoQtdPag;
		public boolean grupoCollapsed;
		public boolean grupoHide;
	}
	
	public static class GrupoItem implements ISwaggerModel {
		public String grupo;
		public String grupoNome;
		public String grupoIcone;
		public Long grupoCounterUser;
		public Long grupoCounterLota;
		public Long grupoCounterAtivo;
		public Long grupoQtd;
		public Long grupoQtdPag;
		public String grupoOrdem;
		public boolean grupoCollapsed;
		public boolean grupoHide;
		public List<MesaItem> grupoDocs;
		public List<Integer> grupoMarcadores;
	}
	
	public static class MesaItem implements ISwaggerModel {
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

	private static List<MesaItem> listarReferencias(TipoDePainelEnum tipo,
			Map<ExMobil, DocDados> references, DpPessoa pessoa,
			DpLotacao unidade, Date currentDate, String grupoOrdem, boolean trazerAnotacoes, 
			boolean ordemCrescenteData, boolean usuarioPosse,
			List<Integer> marcasAIgnorar) {
		List<MesaItem> l = new ArrayList<>();
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		for (ExMobil mobil : references.keySet()) {
			MesaItem r = new MesaItem();
			r.tipo = "Documento";

			Date datahora = null;
			if (references.get(mobil).movUltimaDtIniMov != null)
				datahora = references.get(mobil).movUltimaDtIniMov;
			else
				datahora = mobil.getDoc().getDtAltDoc();
			r.datahora = datahora;
			r.datahoraDDMMYYYHHMM = df.format(datahora);
			r.tempoRelativo = Data.calcularTempoRelativo(datahora);

			r.codigo = mobil.getCodigoCompacto();
			r.sigla = mobil.getSigla();
			r.descr = mobil.doc().getDescrCurta(255).replace("\r", " ").replace("\f", " ").replace("\n", " ");
			if (references.get(mobil).isComposto) {
				r.tipoDoc = "Composto";
			} else {
				r.tipoDoc = "Avulso";
			}

			if (mobil.doc().getSubscritor() != null
					&& mobil.doc().getSubscritor().getLotacao() != null)

				if (SigaMessages.isSigaSP()) {
					if (references.get(mobil).movTramiteSiglaLotacao != null) {
						r.origem = references.get(mobil).movTramiteSiglaOrgao + " / "
								+ references.get(mobil).movTramiteSiglaLotacao;
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
			
			r.dataDevolucao = "ocultar";
			
			if (mobil.doc().getSubscritor() != null
					&& mobil.doc().getLotacao() != null)

				if (SigaMessages.isSigaSP()) {
					if (references.get(mobil).movTramiteSiglaLotacao != null) {
						r.lotaCadastrante = references.get(mobil).movTramiteSiglaOrgao;
					} else {
						r.lotaCadastrante = mobil.doc().getLotacao().getSigla();
					}
				} else {
					r.lotaCadastrante = mobil.doc().getLotacao().getSigla();
				}
			
			r.dataDevolucao = "ocultar";

			if (references.get(mobil).movUltimaDtFimMov != null
					&& references.get(mobil).movUltimaDtFimMov != null) {
				Date dataMovimentacao;
				dataMovimentacao = references.get(mobil).movUltimaDtFimMov;

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

			r.grupoOrdem = grupoOrdem;
			
			if (trazerAnotacoes && mobil.getDnmUltimaAnotacao() != null && !mobil.getDnmUltimaAnotacao().replace(" ", "").equals("")) { 
				r.anotacao = mobil.getDnmUltimaAnotacao().replace("\r\f", "<br/>").replace("\n", "<br/>");
			}

			if (usuarioPosse) {
				ExMovimentacao ultMov = mobil
					.getUltimaMovimentacao(new long[] { ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA,
							ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA,
							ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA,
							ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA,
							ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO
						}, new long[] {0L}, mobil, false, null);
				if (ultMov != null && ultMov.getCadastrante() != null) {
					r.nomePessoaPosse = ultMov.getCadastrante().getNomePessoa(); 
				} else {
					r.nomePessoaPosse = mobil.getDoc().getCadastrante().getNomePessoa();
				}
				if (ultMov != null && ultMov.getCadastrante() != null) {
					r.lotaPosse = ultMov.getCadastrante().getLotacao().getSigla();
				} else {
					r.lotaPosse = mobil.getDoc().getCadastrante().getLotacao().getSigla();
				}
			}
					
			
			r.list = new ArrayList<Marca>();

			for (MeM tag : references.get(mobil).listMeM) {
				if (tag.marca.getDtIniMarca() != null
						&& tag.marca.getDtIniMarca().getTime() > currentDate
								.getTime())
					continue;
				if (tag.marca.getDtFimMarca() != null
						&& tag.marca.getDtFimMarca().getTime() < currentDate
								.getTime())
					continue;

				Marca t = new Marca();
				CpMarcadorEnum mar = CpMarcadorEnum.getById(tag.marcador
						.getIdInicial().intValue());
				if (mar != null) {
					t.nome = mar.getNome();
					t.icone = mar.getIcone();
					if(mar.getId() == CpMarcadorEnum.SEM_EFEITO.id) {
						t.nome = SigaMessages.getMessage("marcador.semEfeito.label");
					} else if(mar.getId() == CpMarcadorEnum.CANCELADO.id) {
						t.nome = SigaMessages.getMessage("marcador.cancelado.label");
					}
				} else {
					t.nome = tag.marcador.getDescrMarcador();
					t.icone = tag.marcador.getIdIcone().getCodigoFontAwesome();
					t.cor = tag.marcador.getIdCor().getDescricao();
				}
				t.titulo = Data.formatDDMMYY(tag.marca.getDtIniMarca()) + " (" 
							+ Data.calcularTempoRelativo(tag.marca.getDtIniMarca()) + ")";

				if (tag.marca.getDpPessoaIni() != null) {
					t.pessoa = tag.marca.getDpPessoaIni().getIdInicial().toString();

				}
				if (tag.marca.getDpLotacaoIni() != null) {
					t.lotacao = tag.marca.getDpLotacaoIni().getIdInicial().toString();
				}
				
				t.inicio = tag.marca.getDtIniMarca();
				t.termino = tag.marca.getDtFimMarca();
				if (tag.dtRef1 != null)
					t.ref1 = Data.calcularTempoRelativoEmDias(tag.dtRef1);
					t.ref1DDMMYYYY = Data.formatDDMMYY(tag.dtRef1);
				if (tag.dtRef2 != null)
					t.ref2 = Data.calcularTempoRelativoEmDias(tag.dtRef2);
					t.ref2DDMMYYYY = Data.formatDDMMYY(tag.dtRef2);
				if(tag.marca.getCpMarcador().isDemandaJudicial()) {
					t.nome += " até " + tag.marca.getExMobil().getDoc().getMobilGeral()
							.getExMovimentacaoSet().stream() //
							.filter(mov -> mov.getExTipoMovimentacao().getId()
									.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_MARCACAO))
							.filter(mov -> !mov.isCancelada()) //
							.filter(mov -> tag.marca.getCpMarcador().equals(mov.getMarcador())) //
							.map(ExMovimentacao::getDtFimMovDDMMYY) //
							.findFirst().orElse("[indeterminado]");
				}

				if(tag.marca.getCpMarcador().isADevolverForaDoPrazo()) {
					t.nome += ", atribuído pela unidade " + tag.marca.getExMobil()
							.getExMovimentacaoSet().stream() //
							.filter(mov -> mov.getExTipoMovimentacao().getId()
									.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_MARCACAO))
							.filter(mov -> !mov.isCancelada()) //
							.filter(mov -> tag.marca.getCpMarcador().equals(mov.getMarcador())) //
							.map(ExMovimentacao::getCadastranteString) //
							.findFirst().orElse(r.lotaCadastrante);
				}

				r.list.add(t);
				if (pessoa != null && tag.marca.getDpPessoaIni() != null) {
					if (pessoa.getIdInicial().equals(
							tag.marca.getDpPessoaIni().getId()))
						t.daPessoa = true;
					else
						t.deOutraPessoa = tag.marca.getDpPessoaIni() != null;
				}
				if (unidade != null
						&& tag.marca.getDpLotacaoIni() != null
						&& unidade.getId().equals(
								tag.marca.getDpLotacaoIni().getId()))
					t.daLotacao = true;
			}
			l.add(r);
		}

		Collections.sort(l, new Comparator<MesaItem>() {
			@Override
			public int compare(MesaItem o1, MesaItem o2) {
				int i;
				if (ordemCrescenteData) {
					i = o1.datahora.compareTo(o2.datahora);
				} else {
					i = o2.datahora.compareTo(o1.datahora);
				}
				if (i != 0)
					return i;
				return 0;
			}
		});
		return l;
	}

	public static List<GrupoItem> getContadores(ExDao dao, DpPessoa titular, DpLotacao lotaTitular, 
			Map<String, SelGrupo> selGrupos, boolean exibeLotacao, 
			List<Integer> marcasAIgnorar) throws Exception {
		List<GrupoItem> gruposMesa = new ArrayList<GrupoItem>();
		gruposMesa = montaGruposUsuario(selGrupos);
		List<Object[]> l = dao.consultarTotaisPorMarcador(titular, lotaTitular, gruposMesa, 
				exibeLotacao, marcasAIgnorar);
		if (l == null)
			return gruposMesa;
		
		for (GrupoItem gItem : gruposMesa) {
			gItem.grupoCounterUser = 0L;
			gItem.grupoCounterLota = 0L;
			for (Object[] reference : l) {
				if (gItem.grupoOrdem.equals(reference[0].toString())) {
					if (reference[1] != null)  
						gItem.grupoCounterUser = Long.valueOf(reference[1].toString());
					if (reference[2] != null)  
						gItem.grupoCounterLota = Long.valueOf(reference[2].toString());
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

	public static List<GrupoItem> getMesa(ExDao dao, DpPessoa titular,
			DpLotacao lotaTitular, Map<String, SelGrupo> selGrupos, List<Mesa2.GrupoItem> gruposMesa, 
			boolean exibeLotacao, boolean trazerAnotacoes, boolean trazerComposto, boolean ordemCrescenteData,
			boolean usuarioPosse, List<Integer> marcasAIgnorar) throws Exception {
//		long tempoIni = System.nanoTime();
		Date dtNow = dao.consultarDataEHoraDoServidor();

		List<Object[]> l = dao.listarMobilsPorMarcas(titular,
				lotaTitular, exibeLotacao, ordemCrescenteData, marcasAIgnorar);

		Map<ExMobil, DocDados> map = new HashMap<>();
		// Cria hashmap para pesquisa do mobil nos grupos
		Map<Long, List<Long>> hashMobGrp = l.stream()
		        .collect(Collectors.groupingBy(k -> (Long.valueOf(((ExMobil) k[2]).getIdMobil())), 
		                Collectors.mapping(v -> (Long.valueOf(((CpMarcador) v[1]).getIdGrupo().getId())), 
		                		Collectors.toList())));
		
		List<Long> listIdMobil = new ArrayList<Long>();
		Long idMob = 0L;

		if (l != null && l.size() > 0) {
			// Para cada grupo da mesa, pesquisa no resultado da query
			for (GrupoItem gItem : gruposMesa) {
				Object[] reference = l.get(0);
				ExMarca marca = (ExMarca) reference[0];
				CpMarcador marcador = (CpMarcador) reference[1];
				ExMobil mobil = (ExMobil) reference[2];
				idMob = mobil.getIdMobil();
				
				for (Integer i = 0; i < l.size(); i++) {
					reference = l.get(i);
					// Inclui o mobil no grupo da mesa
					mobil = (ExMobil) reference[2];
					idMob = mobil.getIdMobil();
					// Se for TMP e o grupo nao for Em Elaboracao, nao deve mostrar no grupo (só GOVSP).
					if (SigaMessages.isSigaSP()
							&& reference[4] == null 
							&& !gItem.grupoNome.equals(CpMarcadorGrupoEnum.EM_ELABORACAO.getNome())) 
						continue;
					// Se for do grupo Aguardando Andamento e tiver marcador da caixa de entrada, nao inclui
					if (gItem.grupoNome.equals(CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO.getNome())
							&& temMarcador(hashMobGrp, idMob, Integer.valueOf(CpMarcadorGrupoEnum.CAIXA_DE_ENTRADA.getId()))) 
						continue;
					
					if (temMarcador(hashMobGrp, idMob, Integer.valueOf(gItem.grupoOrdem)) && !map.containsKey(mobil)) {
						// Se o mobil possui um marcador do grupo e ele ainda nao foi incluido,
						// inclui junto com as outras marcas que estao no resultado da query
						for (Integer i2 = 0; i2 < l.size(); i2++) {  
							reference = l.get(i2);
							mobil = (ExMobil) reference[2];
							if (mobil.getIdMobil() == idMob) {
								marca = (ExMarca) reference[0];
								marcador = (CpMarcador) reference[1];
								if (!map.containsKey(mobil)) {
									// Mobil ainda nao foi incluido no grupo, inclui
									if (listIdMobil.size() < gItem.grupoQtd) {
										DocDados docDados = new DocDados();
										MeM mm = new MeM();
										mm.marca = marca;
										mm.marcador = marcador;
										mm.dtRef1 = (Date) reference[5];
										mm.dtRef2 = (Date) reference[6];
										docDados.listMeM = new ArrayList<MeM>();
										docDados.listMeM.add(mm);
										map.put(mobil, docDados);
										listIdMobil.add(mobil.getId());
									} else {
										break;
									} 
								} else {
									// Mobil ja foi incluido no grupo, inclui so a marca no mobil
									MeM mm = new MeM();
									mm.marca = marca;
									mm.marcador = marcador;
									mm.dtRef1 = (Date) reference[5];
									mm.dtRef2 = (Date) reference[6];
									map.get(mobil).listMeM.add(mm);
								}
							}
						}
					}
				}
				if (!map.isEmpty()) {
					Integer iMobs = 0;
					while (iMobs < listIdMobil.size()) {
						Integer iMobsFim = listIdMobil.size();
						if ( iMobs + 1000 < iMobsFim )
							iMobsFim = iMobs + 1000;
						List<Object[]> refs = dao.listarMovimentacoesMesa(
								listIdMobil.subList(iMobs, iMobsFim), trazerComposto);
			
						for (Object[] ref : refs) {
							incluiMovimentacoesMesa(map, ref);
						}
						iMobs = iMobsFim;
					}
					gItem.grupoDocs = Mesa2.listarReferencias(TipoDePainelEnum.UNIDADE, map, titular,
							titular.getLotacao(), dtNow, gItem.grupoOrdem, trazerAnotacoes, ordemCrescenteData, 
							usuarioPosse, marcasAIgnorar);
					map = new HashMap<>();
					listIdMobil = new ArrayList<Long>();
				}
			}
		}
//		long tempoTotal = System.nanoTime() - tempoIni;
//		System.out.println("getMesa: " + tempoTotal
//		/ 1000000 + " ms ==> ");

		return gruposMesa;
	}

	private static void incluiMovimentacoesMesa(Map<ExMobil, DocDados> map, Object[] ref) {
		ExMobil mob = (ExMobil) ref[0];
		ExMovimentacao movUltima = null;
		ExMovimentacao movTramite = null;
		if (ref[2] != null)
			movUltima = (ExMovimentacao) ref[2];
		if (ref[3] != null)
			movTramite = (ExMovimentacao) ref[3];
		if (map.containsKey(mob)) {
			DocDados docDados = map.get(mob); 
			if (ref[1] != null) {
				docDados.isComposto = (((Integer) ref[1]) == 1);
			}
			if (movUltima != null) {
				docDados.movUltimaDtIniMov = movUltima.getDtIniMov();
				docDados.movUltimaDtFimMov = movUltima.getDtFimMov();
			}
			if (movTramite != null) {
				docDados.movTramiteSiglaLotacao = movTramite.getLotacao().getSigla();
				docDados.movTramiteSiglaOrgao = movTramite.getLotacao().getOrgaoUsuario().getSigla();
			}
		}
	}

	private static boolean temMarcador(Map<Long, List<Long>> hashMobGrp, Long idMobil, Integer grupoId) {
		// Pesquisa na lista retornada pela query se um determinado mobil (idMobil) tem
		// algum marcador do grupoId informado. Devolve true se existir.
		return ((List<Long>) hashMobGrp
				.get(idMobil))
				.contains(grupoId.longValue());
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
					} else {
						grpItem.grupoQtd = 15L;
					}
					grpItem.grupoQtdPag = 15L;
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
						grpItem.grupoQtdPag = selGrupos.get(grp.grupoNome).grupoQtdPag;
						grpItem.grupoQtd = selGrupos.get(grp.grupoNome).grupoQtd;
					} else {
						grpItem.grupoCollapsed = grp.grupoCollapsed;
						grpItem.grupoHide = grp.grupoHide;
						grpItem.grupoQtdPag = grp.grupoQtdPag;
						grpItem.grupoQtd = grp.grupoQtd;
					}
					grpItem.grupoMarcadores = grp.grupoMarcadores;
					lGrupo.add(grpItem);
				}
			}
		}
		return lGrupo;
	}
}
