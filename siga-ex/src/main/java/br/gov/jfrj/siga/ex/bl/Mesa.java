package br.gov.jfrj.siga.ex.bl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class Mesa {

	public static class MesaItem implements ISwaggerModel {
		public String tipo;
		public Date datahora;
		public String tempoRelativo;
		public String codigo;
		public String sigla;
		public String descr;
		public String origem;
		public String grupo;
		public String grupoNome;
		public String grupoIcone;
		public String grupoOrdem;
		public String dataDevolucao;
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
		public Boolean daPessoa;
		public Boolean deOutraPessoa;
		public Boolean daLotacao;
	}

	public static class MeM {
		ExMarca marca;;
		CpMarcador marcador;
	}

	public static List<MesaItem> listarReferencias(TipoDePainelEnum tipo,
			Map<ExMobil, List<MeM>> references, DpPessoa pessoa,
			DpLotacao unidade, Date currentDate) {
		List<MesaItem> l = new ArrayList<>();

		for (ExMobil mobil : references.keySet()) {
			MesaItem r = new MesaItem();
			r.tipo = "Documento";

			Date datahora = null;
			if (mobil.getUltimaMovimentacao() != null)
				datahora = mobil.getUltimaMovimentacao().getDtIniMov();
			else
				datahora = mobil.getDoc().getDtAltDoc();
			r.datahora = datahora;
			r.tempoRelativo = Data.calcularTempoRelativo(datahora);

			r.codigo = mobil.getCodigoCompacto();
			r.sigla = mobil.getSigla();
			r.descr = mobil.doc().getDescrCurta(80);

			if (mobil.doc().getSubscritor() != null
					&& mobil.doc().getSubscritor().getLotacao() != null)

				if (SigaMessages.isSigaSP()) {
					r.origem = mobil.doc().getSubscritor().getLotacao()
							.getOrgaoUsuario()
							+ "/"
							+ mobil.doc().getSubscritor().getLotacao()
									.getSigla();
				} else {
					r.origem = mobil.doc().getSubscritor().getLotacao()
							.getSigla();
				}

			r.dataDevolucao = "ocultar";

			if (mobil.getUltimaMovimentacaoNaoCancelada() != null
					&& mobil.getUltimaMovimentacaoNaoCancelada().getDtFimMov() != null) {

				Date dataMovimentacao = mobil.getUltimaMovimentacaoNaoCancelada()
						.getDtFimMov();
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

			r.grupo = CpMarcadorGrupoEnum.NENHUM.name();
			r.grupoOrdem = Integer.toString(CpMarcadorGrupoEnum
					.valueOf(r.grupo).ordinal());
			r.grupoNome = CpMarcadorGrupoEnum.valueOf(r.grupo).getNome();
			r.grupoIcone = CpMarcadorGrupoEnum.valueOf(r.grupo).getIcone();

			r.list = new ArrayList<Marca>();

			for (MeM tag : references.get(mobil)) {
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
						.getIdMarcador().intValue());

				t.nome = mar.getNome();
				t.icone = mar.getIcone();
				t.titulo = Data
						.calcularTempoRelativo(tag.marca.getDtIniMarca());

				if (tag.marca.getDpPessoaIni() != null) {
					DpPessoa pes = tag.marca.getDpPessoaIni().getPessoaAtual();
					if (pes.getNomeExibicao() != null)
						t.pessoa = pes.getNomeExibicao();
				}
				if (tag.marca.getDpLotacaoIni() != null)
					t.lotacao = tag.marca.getDpLotacaoIni().getLotacaoAtual()
							.getSigla();
				t.inicio = tag.marca.getDtIniMarca();
				t.termino = tag.marca.getDtFimMarca();
				if(tag.marca.getCpMarcador().isDemandaJudicial()) {
					t.nome += " até " + tag.marca.getExMobil().getDoc().getMobilGeral()
							.getExMovimentacaoSet().stream() //
							.filter(mov -> mov.getExTipoMovimentacao().getId()
									.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_MARCACAO))
							.filter(mov -> !mov.isCancelada()) //
							.filter(mov -> mov.getMarcador().equals(tag.marca.getCpMarcador())) //
							.map(ExMovimentacao::getDtFimMovDDMMYY) //
							.findFirst().orElse("[indeterminado]");
				}

				if (CpMarcadorGrupoEnum.valueOf(r.grupo).ordinal() > mar.getGrupo()
						.ordinal()) {
					r.grupo = mar.getGrupo().name();
					r.grupoOrdem = Integer.toString(mar.getGrupo().ordinal());
					r.grupoNome = mar.getGrupo().getNome();
					r.grupoIcone = mar.getGrupo().getIcone();
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
				int i = Integer.compare(Integer.parseInt(o1.grupoOrdem),
						Integer.parseInt(o2.grupoOrdem));
				if (i != 0)
					return i;
				i = o2.datahora.compareTo(o1.datahora);
				if (i != 0)
					return i;
				return 0;
			}
		});
		return l;
	}

	public static List<MesaItem> getMesa(ExDao dao, DpPessoa titular,
			DpLotacao lotaTitular) {
		List<Object[]> l = dao.listarDocumentosPorPessoaOuLotacao(titular,
				lotaTitular);

		HashMap<ExMobil, List<MeM>> map = new HashMap<>();

		for (Object[] reference : l) {
			ExMarca marca = (ExMarca) reference[0];
			CpMarcador marcador = (CpMarcador) reference[1];
			ExMobil mobil = (ExMobil) reference[2];

			if (!map.containsKey(mobil))
				map.put(mobil, new ArrayList<MeM>());
			MeM mm = new MeM();
			mm.marca = marca;
			mm.marcador = marcador;
			map.get(mobil).add(mm);
		}

		if (Prop.getBool("/siga.mesa.carrega.lotacao")
				&& !Ex.getInstance().getComp().ehPublicoExterno(titular)) {
			List<Object[]> lLota = dao.listarDocumentosCxEntradaPorPessoaOuLotacao(null,
					lotaTitular);
	
			for (Object[] reference : lLota) {
				ExMarca marca = (ExMarca) reference[0];
				if (marca.getCpMarcador().getIdMarcador() == CpMarcadorEnum.CAIXA_DE_ENTRADA.getId()) {
					CpMarcador marcador = (CpMarcador) reference[1];
					ExMobil mobil = (ExMobil) reference[2];
		
					if (!map.containsKey(mobil))
						map.put(mobil, new ArrayList<MeM>());
					MeM mm = new MeM();
					mm.marca = marca;
					mm.marcador = marcador;
					map.get(mobil).add(mm);
				}
			}
		}
			
		return Mesa.listarReferencias(TipoDePainelEnum.UNIDADE, map, titular,
				titular.getLotacao(), dao.consultarDataEHoraDoServidor());
	}

}
