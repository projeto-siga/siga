package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IPainelListaGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PainelListaItem;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorGrupoEnum;
import br.gov.jfrj.siga.dp.CpMarca;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class PainelListaGet implements IPainelListaGet {

	@SuppressWarnings("unchecked")
	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
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
		if (req.idMarcador != null)
			idsMarcadorIni.add(Long.parseLong(req.idMarcador));
		else if (req.idGrupo != null) {
			CpMarcadorGrupoEnum grupo = CpMarcadorGrupoEnum.valueOf(req.idGrupo);
			for (CpMarcadorEnum marcador : CpMarcadorEnum.values())
				if (marcador.getGrupo() == grupo)
					idsMarcadorIni.add(marcador.getId());
		}
		List<CpMarca> l = CpDao.getInstance().consultarPainelLista(idsMarcadorIni,
				tipoResp != 2 ? ctx.getTitular() : null, tipoResp != 1 ? ctx.getLotaTitular() : null, null, null);

		for (CpMarca marca : l) {
			PainelListaItem r = new PainelListaItem();

			r.marcaId = marca.getIdMarca().toString();
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
