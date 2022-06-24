package br.gov.jfrj.siga.ex.api.v1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.gov.jfrj.siga.cp.model.enm.TipoDePainelEnum;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IPainelListaGet;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.PainelListaItem;
import br.gov.jfrj.siga.ex.bl.Mesa.MeM;
import br.gov.jfrj.siga.hibernate.ExDao;

public class PainelListaGet implements IPainelListaGet {

	@SuppressWarnings("unchecked")
	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		if (req.idMarcas == null || req.idMarcas.trim().isEmpty())
			return;
		String[] aMarcas = req.idMarcas.split(",");
		List<Long> lMarcas = new ArrayList<>();
		for (String s : aMarcas)
			lMarcas.add(Long.parseLong(s));
		List<Object[]> l = ExDao.getInstance().consultarPainelLista(lMarcas);

		for (Object[] o : l) {
			ExDocumento doc = (ExDocumento) o[0];
			ExMobil mobil = (ExMobil) o[1];
			ExMarca marca = (ExMarca) o[2];
			PainelListaItem r = new PainelListaItem();

			r.marcaId = marca.getIdMarca().toString();
			r.dataIni = marca.getDtIniMarca();
			r.dataFim = marca.getDtFimMarca();
			r.moduloId = marca.getCpTipoMarca().getIdTpMarca().toString();

			r.tipo = mobil.doc().getExFormaDocumento().getExTipoFormaDoc().getDescricao();
			r.codigo = mobil.getCodigoCompacto();
			r.sigla = mobil.getSigla();
			if (mobil.doc().getSubscritor() != null && mobil.doc().getSubscritor().getLotacao() != null)
				r.origem = mobil.doc().getSubscritor().getLotacao().getSigla();
			r.descricao = mobil.doc().getDescrCurta();
			r.ultimaAnotacao = mobil.getDnmUltimaAnotacao();

			resp.list.add(r);
		}
	}

//	public static List<PainelListaItem> listarReferencias(TipoDePainelEnum tipo, Map<ExMobil, List<MeM>> references,
//			DpPessoa pessoa, DpLotacao unidade, Date currentDate) {
//		List<PainelListaItem> l = new ArrayList<>();
//
//		for (ExMobil mobil : references.keySet()) {
//			PainelListaItem r = new PainelListaItem();
//
////			Date datahora = null;
////			if (mobil.getUltimaMovimentacao() != null)
////				datahora = mobil.getUltimaMovimentacao().getDtIniMov();
////			else
////				datahora = mobil.getDoc().getDtAltDoc();
////			r.datahora = datahora;
////			r.tempoRelativo = MesaGet.calcularTempoRelativo(datahora);
//
////			r.codigo = mobil.getCodigoCompacto();
//
////			if (mobil.doc().getSubscritor() != null && mobil.doc().getSubscritor().getLotacao() != null)
////				r.origem = mobil.doc().getSubscritor().getLotacao().getSigla();
////
////			r.grupo = CpMarcadorGrupoEnum.NENHUM.name();
////			r.grupoOrdem = Integer.toString(CpMarcadorGrupoEnum.valueOf(r.grupo).ordinal());
////			r.grupoNome = CpMarcadorGrupoEnum.valueOf(r.grupo).getNome();
//
////			ExCompetenciaBL comp = Ex.getInstance().getComp();
////			r.podeAnotar = comp.pode(ExPodeFazerAnotacao.class, pessoa, unidade, mobil);
////			r.podeAssinar = comp.pode(ExPodeAssinar.class, pessoa, unidade, mobil);
////
////			boolean apenasComSolicitacaoDeAssinatura = !Ex.getInstance().getConf().podePorConfiguracao(pessoa,
////					ExTipoDeConfiguracao.PODE_ASSINAR_SEM_SOLICITACAO);
////
////			r.podeAssinarEmLote = apenasComSolicitacaoDeAssinatura
////					? r.podeAssinar && mobil.doc().isAssinaturaSolicitada()
////					: r.podeAssinar;
////			r.podeTramitar = comp.pode(ExPodeTransferir.class, pessoa, unidade, mobil);
//
////			r.marcas = new ArrayList<IExApiV1.Marca>();
////
////			for (MeM tag : references.get(mobil)) {
////				if (tag.marca.getDtIniMarca() != null && tag.marca.getDtIniMarca().getTime() > currentDate.getTime())
////					continue;
////				if (tag.marca.getDtFimMarca() != null && tag.marca.getDtFimMarca().getTime() < currentDate.getTime())
////					continue;
////
////				Marca t = new Marca();
////				t.marcaId = tag.marca.getIdMarca().toString();
////				CpMarcadorEnum mar = CpMarcadorEnum.getById(tag.marcador.getIdMarcador().intValue());
////
////				CpMarcadorGrupoEnum grupo = null;
////				if (mar != null) {
////					t.nome = mar.getNome();
////					t.icone = mar.getIcone();
////					grupo = mar.getGrupo();
////				} else {
////					t.nome = tag.marcador.getDescrMarcador();
////					t.icone = tag.marcador.getIdIcone().getCodigoFontAwesome();
////					grupo = tag.marcador.getIdGrupo();
////				}
////				if (grupo == null)
////					grupo = CpMarcadorGrupoEnum.NENHUM;
////
////				t.titulo = MesaGet.calcularTempoRelativo(tag.marca.getDtIniMarca());
////
////				if (tag.marca.getDpPessoaIni() != null) {
////					DpPessoa pes = tag.marca.getDpPessoaIni().getPessoaAtual();
////					if (pes.getNomeExibicao() != null)
////						t.pessoa = pes.getNomeExibicao();
////				}
////				if (tag.marca.getDpLotacaoIni() != null)
////					t.lotacao = tag.marca.getDpLotacaoIni().getLotacaoAtual().getSigla();
////				t.inicio = tag.marca.getDtIniMarca();
////				t.termino = tag.marca.getDtFimMarca();
////
////				if (CpMarcadorGrupoEnum.valueOf(r.grupo).ordinal() > grupo.ordinal()) {
////					r.grupo = grupo.name();
////					r.grupoOrdem = Integer.toString(grupo.ordinal());
////					r.grupoNome = grupo.getNome();
////				}
////
////				r.list.add(t);
////				if (pessoa != null && tag.marca.getDpPessoaIni() != null) {
////					if (pessoa.getIdInicial().equals(tag.marca.getDpPessoaIni().getId()))
////						t.daPessoa = true;
////					else
////						t.deOutraPessoa = tag.marca.getDpPessoaIni() != null;
////				}
////				if (unidade != null && tag.marca.getDpLotacaoIni() != null
////						&& unidade.getId().equals(tag.marca.getDpLotacaoIni().getId()))
////					t.daLotacao = true;
////			}
//			l.add(r);
//		}

	@Override
	public String getContext() {
		return "obter lista de documentos por marcador";
	}

}
