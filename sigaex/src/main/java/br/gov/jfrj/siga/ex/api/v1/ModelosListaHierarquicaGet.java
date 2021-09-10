package br.gov.jfrj.siga.ex.api.v1;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IModelosListaHierarquicaGet;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.ModeloListaHierarquicaItem;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.util.ListaHierarquica;
import br.gov.jfrj.siga.util.ListaHierarquicaItem;

public class ModelosListaHierarquicaGet implements IModelosListaHierarquicaGet {

	@Override
	public String getContext() {
		return "obter lista de modelos";
	}

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		boolean isEditandoAnexo = (req.isEditandoAnexo != null && req.isEditandoAnexo ? true : false);
		boolean isCriandoSubprocesso = (req.isCriandoSubprocesso != null && req.isCriandoSubprocesso ? true : false);
		ExMobil mobPai = null;
		String headerValue = null;
		boolean isAutuando = (req.isAutuando != null && req.isAutuando ? true : false);

		if (req.siglaMobPai != null) {
			mobPai = ctx.buscarMobil(req.siglaMobPai, req, resp, "Documento Pai");
		}
		DpPessoa titular = ctx.getTitular();
		DpLotacao lotaTitular = ctx.getLotaTitular();
		List<ExModelo> modelos = Ex.getInstance().getBL().obterListaModelos(null, null, isEditandoAnexo,
				isCriandoSubprocesso, mobPai, headerValue, true, titular, lotaTitular, isAutuando);

		resp.list = new ArrayList<>();
		for (ListaHierarquicaItem m : getListaHierarquica(modelos).getList()) {
			ModeloListaHierarquicaItem mi = new ModeloListaHierarquicaItem();
			mi.idModelo = (m.getValue() != null ? m.getValue().toString() : "");
			mi.nome = m.getText();
			mi.descr = m.getSearchText();
			mi.level = (long) m.getLevel();
			mi.group = m.getGroup();
			mi.selected = m.getSelected();
			resp.list.add(mi);
		}
	}

	private ListaHierarquica getListaHierarquica(List<ExModelo> modelos) {
		ListaHierarquica lh = new ListaHierarquica();
		for (ExModelo m : modelos) {
			lh.add(m.getNmMod(), m.getDescMod(), m.getId(), false);
		}
		return lh;
	}
}