package br.gov.jfrj.siga.ex.api.v1;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IModelosGet;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.ModeloItem;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExModeloDaoFiltro;

public class ModelosGet implements IModelosGet {

	@Override
	public String getContext() {
		return "obter lista de modelos";
	}

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		if (req.permitidos != null && !req.permitidos) {
			listarModelos(req, resp, ctx);
		} else {
			listarModelosPermitidosAoUsuario(req, resp, ctx);
		}
	}

	private void listarModelos(Request req, Response resp, ExApiV1Context ctx) 
			throws Exception {
		final ExModeloDaoFiltro flt = new ExModeloDaoFiltro();
		flt.setNome(req.nomemodelo);
		List<ExModelo> modelos = ExDao.getInstance().consultarPorFiltro(flt);
		
		resp.list = new ArrayList<>();
		for (ExModelo m : modelos) {
			ModeloItem mi = new ModeloItem();
			mi.idModelo = m.getId().toString();
			mi.idModeloInicial = m.getIdInicial().toString();
			mi.nome = m.getNmMod();
			mi.descr = m.getDescMod();
			resp.list.add(mi);
		}
	}

	private void listarModelosPermitidosAoUsuario(Request req, Response resp, ExApiV1Context ctx) 
			throws Exception {
		boolean isEditandoAnexo = false;
		boolean isCriandoSubprocesso = false;
		ExMobil mobPai = null;
		String headerValue = null;
		boolean isAutuando = false;
		
		DpPessoa titular = ctx.getTitular();
		DpLotacao lotaTitular = ctx.getLotaTitular();
		List<ExModelo> modelos = Ex.getInstance().getBL().obterListaModelos(null, null, isEditandoAnexo,
				isCriandoSubprocesso, mobPai, headerValue, true, titular, lotaTitular, isAutuando);
		
		String[] args = new String[] {};
		if (req.nomemodelo != null && !req.nomemodelo.isEmpty())
			args = req.nomemodelo.split(" ");
		
		resp.list = new ArrayList<>();
		for (ExModelo m : modelos) {
			ModeloItem mi = new ModeloItem();
			if (args.length == 0 || contemTexto(m.getNmMod(), args)) {
				mi.idModelo = m.getId().toString();
				mi.idModeloInicial = m.getIdInicial().toString();
				mi.nome = m.getNmMod();
				mi.descr = m.getDescMod();
				resp.list.add(mi);
			}
		}
	}
		
	private boolean contemTexto(String str, String[] args) {
		for (String arg : args) {
			if (!(str != null && str.toUpperCase()
					.contains(arg.toUpperCase()))) 
				return false;
		}
		return true;
	}
}