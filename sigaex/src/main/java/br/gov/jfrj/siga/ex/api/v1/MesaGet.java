package br.gov.jfrj.siga.ex.api.v1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.ocpsoft.prettytime.PrettyTime;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorGrupoEnum;
import br.gov.jfrj.siga.cp.model.enm.TipoDePainelEnum;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IMesaGet;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.Marca;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.MesaItem;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExCompetenciaBL;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.Transacional;

@Transacional
public class MesaGet implements IMesaGet {

	private static class MeM {
		ExMarca marca;
		CpMarcador marcador;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		DpPessoa pes = null;
		DpLotacao lota = null;
		DpPessoa cadastrante = ctx.getCadastrante();
		DpLotacao lotaCadastrante = cadastrante.getLotacao();
		if (req.filtroPessoaLotacao != null) {
			switch (req.filtroPessoaLotacao) {
			case "Pessoa":
				pes = cadastrante;
				break;
			case "Lotacao":
				lota = lotaCadastrante;
				break;
			default:
				pes = cadastrante;
				lota = lotaCadastrante;
			}
		} else {
			pes = cadastrante;
			lota = lotaCadastrante;
		}

		List<Object[]> l = ExDao.getInstance().listarDocumentosPorPessoaOuLotacao(pes, lota);

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

		resp.list = listarReferencias(TipoDePainelEnum.UNIDADE, map, cadastrante, lotaCadastrante,
				ExDao.getInstance().consultarDataEHoraDoServidor());
	}

	@Override
	public String getContext() {
		return "obter classe processual";
	}

	private static String calcularTempoRelativo(Date anterior) {
		PrettyTime p = new PrettyTime(new Date(), new Locale("pt"));

		String tempo = p.format(anterior);
		tempo = tempo.replace(" atrás", "");
		tempo = tempo.replace(" dias", " dias");
		tempo = tempo.replace(" horas", "h");
		tempo = tempo.replace(" minutos", "min");
		tempo = tempo.replace(" segundos", "s");
		tempo = tempo.replace("agora há pouco", "agora");
		return tempo;
	}

	public static List<MesaItem> listarReferencias(TipoDePainelEnum tipo, Map<ExMobil, List<MeM>> references,
			DpPessoa pessoa, DpLotacao unidade, Date currentDate) {
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
			r.tempoRelativo = MesaGet.calcularTempoRelativo(datahora);

			r.codigo = mobil.getCodigoCompacto();
			r.sigla = mobil.getSigla();
			r.descr = mobil.doc().getDescrCurta();

			if (mobil.doc().getSubscritor() != null && mobil.doc().getSubscritor().getLotacao() != null)
				r.origem = mobil.doc().getSubscritor().getLotacao().getSigla();

			r.grupo = CpMarcadorGrupoEnum.NENHUM.name();
			r.grupoOrdem = Integer.toString(CpMarcadorGrupoEnum.valueOf(r.grupo).ordinal());
			r.grupoNome = CpMarcadorGrupoEnum.valueOf(r.grupo).getNome();

			ExCompetenciaBL comp = Ex.getInstance().getComp();
			r.podeAnotar = comp.podeFazerAnotacao(pessoa, unidade, mobil);
			r.podeAssinar = comp.podeAssinar(pessoa, unidade, mobil);

			boolean apenasComSolicitacaoDeAssinatura = !Ex.getInstance().getConf().podePorConfiguracao(pessoa,
					ExTipoDeConfiguracao.PODE_ASSINAR_SEM_SOLICITACAO);

			r.podeAssinarEmLote = apenasComSolicitacaoDeAssinatura
					? r.podeAssinar && mobil.doc().isAssinaturaSolicitada()
					: r.podeAssinar;
			r.podeTramitar = comp.podeTransferir(pessoa, unidade, mobil);

			r.list = new ArrayList<IExApiV1.Marca>();

			for (MeM tag : references.get(mobil)) {
				if (tag.marca.getDtIniMarca() != null && tag.marca.getDtIniMarca().getTime() > currentDate.getTime())
					continue;
				if (tag.marca.getDtFimMarca() != null && tag.marca.getDtFimMarca().getTime() < currentDate.getTime())
					continue;

				Marca t = new Marca();
				t.marcaId = tag.marca.getIdMarca().toString();
				CpMarcadorEnum mar = CpMarcadorEnum.getById(tag.marcador.getIdMarcador().intValue());

				CpMarcadorGrupoEnum grupo = null;
				if (mar != null) {
					t.nome = mar.getNome();
					t.icone = mar.getIcone();
					grupo = mar.getGrupo();
				} else {
					t.nome = tag.marcador.getDescrMarcador();
					t.icone = tag.marcador.getIdIcone().getCodigoFontAwesome();
					grupo = tag.marcador.getIdGrupo();
				}
				if (grupo == null)
					grupo = CpMarcadorGrupoEnum.NENHUM;

				t.titulo = MesaGet.calcularTempoRelativo(tag.marca.getDtIniMarca());

				if (tag.marca.getDpPessoaIni() != null) {
					DpPessoa pes = tag.marca.getDpPessoaIni().getPessoaAtual();
					if (pes.getNomeExibicao() != null)
						t.pessoa = pes.getNomeExibicao();
				}
				if (tag.marca.getDpLotacaoIni() != null)
					t.lotacao = tag.marca.getDpLotacaoIni().getLotacaoAtual().getSigla();
				t.inicio = tag.marca.getDtIniMarca();
				t.termino = tag.marca.getDtFimMarca();

				if (CpMarcadorGrupoEnum.valueOf(r.grupo).ordinal() > grupo.ordinal()) {
					r.grupo = grupo.name();
					r.grupoOrdem = Integer.toString(grupo.ordinal());
					r.grupoNome = grupo.getNome();
				}

				r.list.add(t);
				if (pessoa != null && tag.marca.getDpPessoaIni() != null) {
					if (pessoa.getIdInicial().equals(tag.marca.getDpPessoaIni().getId()))
						t.daPessoa = true;
					else
						t.deOutraPessoa = tag.marca.getDpPessoaIni() != null;
				}
				if (unidade != null && tag.marca.getDpLotacaoIni() != null
						&& unidade.getId().equals(tag.marca.getDpLotacaoIni().getId()))
					t.daLotacao = true;
			}
			l.add(r);
		}

		Collections.sort(l, new Comparator<MesaItem>() {
			@Override
			public int compare(MesaItem o1, MesaItem o2) {
				int i = Integer.compare(Integer.parseInt(o1.grupoOrdem), Integer.parseInt(o2.grupoOrdem));
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

}
