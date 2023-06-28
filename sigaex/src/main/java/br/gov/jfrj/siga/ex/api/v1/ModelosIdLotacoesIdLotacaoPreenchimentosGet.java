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
		
		//TODO: Refatoração: reduzir repetição de código
		resp.list = listarPreenchimentosPriorizandoLotacao(Long.parseLong(req.id), Long.parseLong(req.idLotacao));	
	}
	
	// Lista todos os preenchimentos, primeiro os preenchimentos da lotação selecionada, depois os das outras
	public static List<PreenchimentoItem> listarPreenchimentosPriorizandoLotacao(Long idModelo, Long idLotacao) {
	    ExDao dao = ExDao.getInstance();
	    ExModelo mod = dao.consultar(idModelo, ExModelo.class, false);
	    mod = dao.consultar(mod.getIdInicial(), ExModelo.class, false);
	    ExPreenchimento filtro = new ExPreenchimento();
	    filtro.setExModelo(mod);
	    List<ExPreenchimento> l = dao.consultar(filtro);
	    List<PreenchimentoItem> listaDePreenchimentos = new ArrayList<>();
	    List<PreenchimentoItem> preenchimentosDeOutrasLotacoes = new ArrayList<>();

	    for (ExPreenchimento i : l) {
	        PreenchimentoItem item = new PreenchimentoItem();
	        item.idPreenchimento = Long.toString(i.getIdPreenchimento());
	        item.nome = i.getNomePreenchimento();

	        // Se a lotação do preenchimento atual corresponder à lotação selecionada, adiciona à lista principal
	        if (i.getDpLotacao().getId().equals(idLotacao)) {
	            listaDePreenchimentos.add(item);
	        } else {
	        	preenchimentosDeOutrasLotacoes.add(item);
	        }
	    }

	    listaDePreenchimentos.addAll(preenchimentosDeOutrasLotacoes);

	    return listaDePreenchimentos;
	}

	
	//Lista todos os preenchimentos de 1 lotação específica.
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
	
		//Lista os preenchimentos sem filtrar por lotações, filtra apenas por modelos
		public static List<PreenchimentoItem> listarPreenchimentosSemFiltroLotacao(Long idModelo) {
			ExDao dao = ExDao.getInstance();
			ExModelo mod = dao.consultar(idModelo, ExModelo.class, false);
			mod = dao.consultar(mod.getIdInicial(), ExModelo.class, false);

			ExPreenchimento filtro = new ExPreenchimento();
			filtro.setExModelo(mod);
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
