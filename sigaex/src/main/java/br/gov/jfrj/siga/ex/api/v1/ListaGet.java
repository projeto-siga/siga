package br.gov.jfrj.siga.ex.api.v1;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.dp.CpMarca;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IListaGet;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.ListaItem;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

public class ListaGet implements IListaGet {

	@SuppressWarnings("unchecked")
	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		int ultMovTipoResp = 0;
		if (req.filtroPessoaLotacao != null) {
			switch (req.filtroPessoaLotacao) {
			case "Pessoa":
				ultMovTipoResp = 1;
				break;
			case "Lotacao":
				ultMovTipoResp = 2;
				break;
			}
		}

		Long idTpFormaDoc = 0L;
		if (req.filtroExpedienteProcesso != null) {
			switch (req.filtroExpedienteProcesso) {
			case "Expediente":
				idTpFormaDoc = 1L;
				break;
			case "Processo":
				idTpFormaDoc = 2L;
				break;
			}
		}

		final ExMobilDaoFiltro flt = new ExMobilDaoFiltro();

		flt.setIdTipoFormaDoc(idTpFormaDoc);

		flt.setUltMovIdEstadoDoc(Long.parseLong(req.idMarcador));

		if (ultMovTipoResp == 1)
			flt.setUltMovRespSelId(ctx.getTitular().getIdInicial());
		else
			flt.setUltMovLotaRespSelId(ctx.getLotaTitular().getIdInicial());

		List<Object[]> l = ExDao.getInstance().consultarPorFiltroOtimizado(flt, 0, 1000, ctx.getTitular(),
				ctx.getLotaTitular());

		resp.list = new ArrayList<>();

		for (Object[] i : l) {
			ExDocumento doc = (ExDocumento) i[0];
			ExMobil mob = (ExMobil) i[1];
			CpMarca marca = (CpMarca) i[2];

			ListaItem r = new ListaItem();

			r.sigla = mob.getSigla();
			r.documentoData = doc.getData();
			r.documentoSubscritor = doc.getSubscritor() != null ? doc.getSubscritor().getSigla() : null;
			r.documentoLotaSubscritor = doc.getLotaSubscritor() != null ? doc.getLotaSubscritor().getSigla() : null;
			r.documentoEspecie = doc.getExFormaDocumento().getDescricao();
			r.documentoModelo = doc.getExModelo().getNmMod();
			if (Ex.getInstance().getBL().mostraDescricaoConfidencial(doc, ctx.getTitular(), ctx.getLotaTitular())) {
				r.documentoDescricao = doc.getDescrDocumento();
				r.mobilUltimaAnotacao = mob.getDnmUltimaAnotacao();
			} else {
				r.documentoDescricao = doc.getDescrDocumento() != null ? "[CONFIDENCIAL]" : null;
				r.mobilUltimaAnotacao = mob.getDnmUltimaAnotacao() != null ? "[CONFIDENCIAL]" : null;
			}
			r.marcadorId = marca.getCpMarcador().getId().toString();
			r.marcadorNome = marca.getCpMarcador().getDescrMarcador();
			if (marca.getCpMarcador().getIdCor() != null)
				r.marcadorCor = marca.getCpMarcador().getIdCor().name();
			if (marca.getCpMarcador().getIdIcone() != null)
				r.marcadorIcone = marca.getCpMarcador().getIdIcone().getCodigoFontAwesome();
			r.marcaData = marca.getDtIniMarca();
			if (marca.getDpPessoaIni() != null)
				r.marcaResponsavel = marca.getDpPessoaIni().getPessoaAtual().getSigla();
			if (marca.getDpLotacaoIni() != null)
				r.marcaLotaResponsavel = marca.getDpLotacaoIni().getLotacaoAtual().getSigla();

			resp.list.add(r);
		}
	}

	@Override
	public String getContext() {
		return "obter lista de documentos por marcador";
	}

}
