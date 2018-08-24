package br.gov.jfrj.siga.tp.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import br.gov.jfrj.siga.tp.model.FinalidadeRequisicao;

public class FinalidadeRequisicaoRest {

    private FinalidadeRequisicao finalidade;

    public FinalidadeRequisicaoRest(FinalidadeRequisicao finalidade) {
        this.finalidade = finalidade;
    }

    public Long getId() {
        return finalidade.getId();
    }

    public String getDescricao() {
        return finalidade.getDescricao();
    }

    @JsonIgnore
    public static FinalidadeRequisicaoRest[] buscarFinalidades() {
        List<FinalidadeRequisicao> fins = FinalidadeRequisicao.listarTodos();
        List<FinalidadeRequisicaoRest> retorno = new ArrayList<FinalidadeRequisicaoRest>();

        for (Iterator<FinalidadeRequisicao> it = fins.iterator(); it.hasNext();) {
            FinalidadeRequisicao fin = it.next();
            retorno.add(new FinalidadeRequisicaoRest(fin));
        }

        return retorno.toArray(new FinalidadeRequisicaoRest[retorno.size()]);
    }

    public static FinalidadeRequisicaoRest buscarFinalidade(Long idBuscar) {
        FinalidadeRequisicao fin = FinalidadeRequisicao.buscar(idBuscar);
        if (fin == null) {
            return null;
        }
        return new FinalidadeRequisicaoRest(fin);
    }

    public static FinalidadeRequisicaoRest buscarFinalidade(String descricaoBuscar) {
        FinalidadeRequisicao fin = FinalidadeRequisicao.buscar(descricaoBuscar);
        if (fin == null) {
            return null;
        }
        return new FinalidadeRequisicaoRest(fin);
    }

}
