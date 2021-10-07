package br.gov.jfrj.siga.ex.api.v1;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExPreenchimento;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IModelosIdLotacoesIdLotacaoPreenchimentosGet;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.PreenchimentoItem;
import br.gov.jfrj.siga.hibernate.ExDao;

public class ModelosIdLotacoesIdLotacaoPreenchimentosGet implements IModelosIdLotacoesIdLotacaoPreenchimentosGet {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		resp.list = listarPreenchimentos(Long.parseLong(req.id), Long.parseLong(req.idLotacao));
	}

	public static List<PreenchimentoItem> listarPreenchimentos(Long idModelo, Long idLotacao) {
		ExDao dao = ExDao.getInstance();
		ExModelo mod = dao.consultar(idModelo, ExModelo.class, false);
		mod = dao.consultar(mod.getIdInicial(), ExModelo.class, false);
		DpLotacao lota = dao.consultar(idLotacao, DpLotacao.class, false);
		lota = dao.consultar(lota.getIdInicial(), DpLotacao.class, false);

		ExPreenchimento filtro = new ExPreenchimento();
		filtro.setExModelo(mod);
		filtro.setDpLotacao(lota);
		List<ExPreenchimento> l = dao.consultar(filtro);
		List<PreenchimentoItem> list = new ArrayList<>();
		for (ExPreenchimento i : l) {
			PreenchimentoItem item = new PreenchimentoItem();
			item.idPreenchimento = Long.toString(i.getIdPreenchimento());
			item.nome = i.getNomePreenchimento();
			list.add(item);
		}
		return list;
	}

	@Override
	public String getContext() {
		return "obter preenchimentos";
	}
}
