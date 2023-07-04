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
	
	// TODO: agrupar os textos padrão por lotação ao invés de ordem alfabética
	// TODO: mostrar a lotação antes do nome
	// Lista todos os preenchimentos, primeiro os preenchimentos da lotação selecionada, depois os das outras
	public static List<PreenchimentoItem> listarPreenchimentos(Long idModelo, Long idLotacao) {
	    ExDao dao = ExDao.getInstance();
	    ExModelo mod = dao.consultar(idModelo, ExModelo.class, false);
	    mod = dao.consultar(mod.getIdInicial(), ExModelo.class, false);
	    ExPreenchimento filtro = new ExPreenchimento();
	    filtro.setExModelo(mod);
	    List<ExPreenchimento> l = dao.consultar(filtro);
	    
	    // variaveis para gravar os preenchimentos/textos-padrões
	    List<PreenchimentoItem> list = new ArrayList<>();
	    List<PreenchimentoItem> preenchimentosDeOutrasLotacoes = new ArrayList<>();
	    
	    // variaveis para gravar as siglas das lotações
	    List siglas = new ArrayList<>();
	    List siglasDeOutrasLotacoes = new ArrayList<>();
	    
	    for (ExPreenchimento i : l) {
	        PreenchimentoItem item = new PreenchimentoItem();
	        item.idPreenchimento = Long.toString(i.getIdPreenchimento());
	        item.nome = i.getNomePreenchimento();
	        
	        // variaveis para gravar as siglas das lotações
	        String siglaLotacao = new String();
	        siglaLotacao = i.getDpLotacao().getSigla();
	        
	        // Se a lotação do preenchimento atual corresponder à lotação selecionada, adiciona à lista principal
	        if (i.getDpLotacao().getId().equals(idLotacao)) {
	        	//Pegar as siglas das  lotações selecionadas aqui e adicionar em uma lista
	            list.add(item);
	            siglas.add(siglaLotacao);
	        } else {
	        	//Pegar as siglas das outras lotações aqui e adicionar em uma lista
	        	preenchimentosDeOutrasLotacoes.add(item);
	        	siglasDeOutrasLotacoes.add(siglaLotacao);
	        }
	    }

	    list.addAll(preenchimentosDeOutrasLotacoes);
	    siglas.addAll(siglasDeOutrasLotacoes);
	    
	    //TODO: Concatenar no padrão SIGLA + Preenchimento para montar saida
	    List<String> resultadoConcatenacao = concatenarSiglasComPreenchimentos(siglas, list);
	    
	    //resultado precisa ser do tipo PreenchimentoItem
	    return list;
	}
	
    public static List<String> concatenarSiglasComPreenchimentos(List<String> siglas, List<PreenchimentoItem> preenchimentos) {
        List<String> resultado = new ArrayList<String>();
        for(int i = 0; i < siglas.size(); i++) {
            resultado.add(siglas.get(i) + " " + preenchimentos.get(i).nome);
        }
        return resultado;
    }

	@Override
	public String getContext() {
		return "obter preenchimentos";
	}
}
