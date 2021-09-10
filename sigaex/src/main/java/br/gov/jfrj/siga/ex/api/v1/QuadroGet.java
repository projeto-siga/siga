package br.gov.jfrj.siga.ex.api.v1;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.cp.model.enm.CpMarcadorCorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorFinalidadeEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorIconeEnum;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IQuadroGet;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.QuadroItem;
import br.gov.jfrj.siga.hibernate.ExDao;

public class QuadroGet implements IQuadroGet {

	@SuppressWarnings("unchecked")
	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		Integer idTpFormaDoc = 0;
		if (req.filtroExpedienteProcesso != null) {
			switch (req.filtroExpedienteProcesso) {
			case "Expediente":
				idTpFormaDoc = 1;
				break;
			case "Processo":
				idTpFormaDoc = 2;
				break;
			}
		}

		List<Object[]> listEstados = ExDao.getInstance().consultarPaginaInicial(ctx.getTitular(), ctx.getLotaTitular(),
				idTpFormaDoc);

		resp.list = new ArrayList<>();

		for (Object[] i : listEstados) {
			QuadroItem r = new QuadroItem();
			r.marcadorId = ((Long) i[0]).toString();
			r.marcadorNome = (String) i[1];
			CpMarcadorCorEnum cor = (CpMarcadorCorEnum) i[6];
			if (cor != null)
				r.marcadorCor = cor.name().substring(4);
			CpMarcadorIconeEnum icone = (CpMarcadorIconeEnum) i[7];
			if (icone != null)
				r.marcadorIcone = icone.getCodigoFontAwesome();

			CpMarcadorFinalidadeEnum finalidade = (CpMarcadorFinalidadeEnum) ((Object[]) i)[4];
			if (finalidade != null) {
				r.finalidadeId = finalidade.name();
				r.grupoId = finalidade.getIdTpMarcador().name();
				r.grupoNome = finalidade.getIdTpMarcador().getDescricao();
			}
			r.qtdAtendente = ((Long) i[2]).toString();
			r.qtdLotaAtendente = ((Long) i[3]).toString();

			resp.list.add(r);
		}
	}

	@Override
	public String getContext() {
		return "obter quadro quantitativo";
	}

}
