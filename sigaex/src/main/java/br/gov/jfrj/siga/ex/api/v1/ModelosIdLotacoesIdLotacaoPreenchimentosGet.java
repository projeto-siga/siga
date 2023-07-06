package br.gov.jfrj.siga.ex.api.v1;

import java.util.ArrayList;
import java.util.HashMap;
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
	    ExPreenchimento filtro = new ExPreenchimento();
	    filtro.setExModelo(mod);
	    List<ExPreenchimento> l = dao.consultar(filtro);

	    List<PreenchimentoItem> lotacaoSelecionadaList = new ArrayList<>();
	    HashMap<Long, List<PreenchimentoItem>> lotacoesMap = new HashMap<>();
	    for (ExPreenchimento i : l) {
	        PreenchimentoItem item = new PreenchimentoItem();
	        item.idPreenchimento = Long.toString(i.getIdPreenchimento());
	        String nome = i.getNomePreenchimento();
	        String siglaLotacaoEPreenchimento = i.getSiglaDaLotacaoENomedoPreenchimento();
	        item.nome = siglaLotacaoEPreenchimento;

	        Long idLotacaoAtual = i.getDpLotacao().getId();
	        
	        if (idLotacaoAtual.equals(idLotacao)) {
	            lotacaoSelecionadaList.add(item);
	        } else {
	            if (!lotacoesMap.containsKey(idLotacaoAtual)) {
	                lotacoesMap.put(idLotacaoAtual, new ArrayList<>());
	            }
	            lotacoesMap.get(idLotacaoAtual).add(item);
	        }
	    }

	    List<PreenchimentoItem> list = new ArrayList<>();
	    list.addAll(lotacaoSelecionadaList);
	    for (Long id : lotacoesMap.keySet()) {
	        list.addAll(lotacoesMap.get(id));
	    }
	    return list;
	}

	

	@Override
	public String getContext() {
		return "obter preenchimentos";
	}
}
