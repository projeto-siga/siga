package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IPainelListaGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PainelListaItem;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorFinalidadeGrupoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorGrupoEnum;
import br.gov.jfrj.siga.dp.CpMarca;
import br.gov.jfrj.siga.dp.CpTipoMarca;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class PainelListaGet implements IPainelListaGet {

	@SuppressWarnings("unchecked")
	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		CpTipoMarca cpTipoMarca = CpTipoMarca.findByName(req.tipoMarca);

		int tipoResp = 0;
		if (req.filtroPessoaLotacao != null) {
			switch (req.filtroPessoaLotacao.toUpperCase()) {
			case "PESSOA":
				tipoResp = 1;
				break;
			case "LOTACAO":
				tipoResp = 2;
				break;
			}
		}

		List<Long> idsMarcadorIni = new ArrayList<>();
		if (req.idMarcadores != null && !req.idMarcadores.isEmpty()) {
			String[] aMarcadores = req.idMarcadores.split(",");
			for (String s : aMarcadores)
				idsMarcadorIni.add(Long.parseLong(s));
		}
		List<CpMarca> l = CpDao.getInstance().consultarPainelLista(idsMarcadorIni,
				tipoResp != 2 ? ctx.getTitular() : null, tipoResp != 1 ? ctx.getLotaTitular() : null, cpTipoMarca,
				Integer.parseInt(req.itensPorPagina), Integer.parseInt(req.pagina));

		for (CpMarca marca : l) {
			PainelListaItem r = new PainelListaItem();
			r.marcaId = marca.getIdMarca().toString();

			CpMarcadorEnum enmMarcador = CpMarcadorEnum.getById(marca.getCpMarcador().getId().intValue());
			r.marcaTexto = enmMarcador != null && !enmMarcador.getNome().startsWith("???") ? enmMarcador.getNome() : marca.getCpMarcador().getDescrMarcador();
			r.marcaIcone = enmMarcador != null ? enmMarcador.getIcone() 
					: marca.getCpMarcador().getIdIcone().getCodigoFontAwesome();
			r.dataIni = marca.getDtIniMarca();
			r.dataFim = marca.getDtFimMarca();
			r.moduloId = marca.getCpTipoMarca().getIdTpMarca().toString();
			r.tipo = marca.getCpTipoMarca().getDescrTipoMarca();
			resp.list.add(r);
		}
	}

	@Override
	public String getContext() {
		return "obter lista de documentos por marcador";
	}

}
