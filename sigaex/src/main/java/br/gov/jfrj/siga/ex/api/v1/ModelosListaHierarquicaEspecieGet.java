package br.gov.jfrj.siga.ex.api.v1;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IModelosListaHierarquicaEspecieGet;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.ModeloListaHierarquicaEspecieItem;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.util.ListaHierarquica;
import br.gov.jfrj.siga.util.ListaHierarquicaEspecie;
import br.gov.jfrj.siga.util.ListaHierarquicaEspecieItem;
import br.gov.jfrj.siga.util.ListaHierarquicaItem;

public class ModelosListaHierarquicaEspecieGet implements IModelosListaHierarquicaEspecieGet {

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
		for (ListaHierarquicaEspecieItem m : getListaHierarquicaEspecie(modelos).getList()) {
			ModeloListaHierarquicaEspecieItem mi = new ModeloListaHierarquicaEspecieItem();
			mi.idModelo = (m.getValue() != null ? m.getValue().toString() : "");
			mi.idModeloInicial = (m.getIdInicial() != null ? m.getIdInicial().toString() : "");
			mi.nome = m.getText();
			mi.descr = m.getSearchText();
			mi.level = (long) m.getLevel();
			mi.group = m.getGroup();
			mi.selected = m.getSelected();
			mi.keywords = m.getKeywords();
			mi.especie = m.getEspecie();
			resp.list.add(mi);
		}
	}

	private ListaHierarquicaEspecie getListaHierarquicaEspecie(List<ExModelo> modelos) {
		ListaHierarquicaEspecie lh = new ListaHierarquicaEspecie();
		for (ExModelo m : modelos) {
			String especie = "";
			if (m.getExFormaDocumento() != null ) {
				if (m.getExFormaDocumento().getDescricao() != null) {
					especie = m.getExFormaDocumento().getDescricao();
				}
			}
			
			lh.add(m.getIdInicial().toString(), m.getNmMod(), m.getDescMod(), m.getId(), false, especie);
		}
		return lh;
	}
}
