package br.gov.jfrj.siga.tp.vraptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.SimpleMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.tp.model.RelatorioRanking;
import br.gov.jfrj.siga.tp.model.RelatorioRanking.RankingVeiculoRequisicao;
import br.gov.jfrj.siga.tp.model.RelatorioUsoVeiculos;
import br.gov.jfrj.siga.tp.model.RequisicaoTransporte;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.model.Veiculo;
import br.gov.jfrj.siga.tp.util.FormatarDataHora;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/app/relatorioUsoVeiculos")
public class RelatorioUsoVeiculosController extends TpController {
    private static final String _NOME_VAR_RELAT_CONSULTA = "relatorioUsoVeiculos";
    private static final String _HORA_FIM_DIA = "23:59:59";
    private static final String _HORA_INICIO_DIA = "00:00:00";

	/**
	 * @deprecated CDI eyes only
	 */
	public RelatorioUsoVeiculosController() {
		super();
	}
	
	@Inject
    public RelatorioUsoVeiculosController(HttpServletRequest request, Result result,  Validator validator, SigaObjects so,  EntityManager em) {
        super(request, result, TpDao.getInstance(), validator, so, em);
    }

    @Path("/consultar")
    public void consultar() {
    	RelatorioUsoVeiculos r = new RelatorioUsoVeiculos();
    	result.include("relatorio", r);
    }

    public void gerarRelatorio(RelatorioUsoVeiculos relatorio) throws Exception {
        if (validator.hasErrors()) {
            validator.onErrorUse(Results.page()).of(RelatorioUsoVeiculosController.class).consultar();

        } else {
            validaDatas(relatorio);

            if (validator.hasErrors()) {
            	result.include("relatorio", relatorio);
                result.redirectTo(this).consultar();
            }
            
            relatorio.recuperarUsoVeiculos(getTitular().getOrgaoUsuario());
            
            result.include(_NOME_VAR_RELAT_CONSULTA, relatorio);

        }
    }

    private void validaDatas(RelatorioUsoVeiculos relat) {
        if (!relat.datasEstaoCorretas()) {
        	validator.add(new SimpleMessage("relatorio","Verifique se a data de in&iacute;cio e data de fim est&atilde;o preenchidas e se a data de in&iacute;cio n&atilde;o &eacute; maior que a data fim."));
            validator.onErrorUse(Results.page()).of(RelatorioUsoVeiculosController.class).consultar();
        }
    }

    @SuppressWarnings("unchecked")
    public List<RankingVeiculoRequisicao> retornarVeiculosQueAtenderamMaisRequisicoes(RelatorioRanking relatorio) throws Exception {
        List<Object[]> lista;
        List<RankingVeiculoRequisicao> listaRankingVeiculo = new ArrayList<RankingVeiculoRequisicao>();
        Set<RequisicaoTransporte> setRequisicao = new HashSet<RequisicaoTransporte>();
        Veiculo veiculo = null;
        RequisicaoTransporte requisicao = null;
        CpOrgaoUsuario cpOrgaoUsuario = getTitular().getOrgaoUsuario();

        relatorio.getDataInicio().setTime(FormatarDataHora.formatarDataHora(relatorio.getDataInicio(), _HORA_INICIO_DIA));
        relatorio.getDataFim().setTime(FormatarDataHora.formatarDataHora(relatorio.getDataFim(), _HORA_FIM_DIA));

        String qrl = "SELECT v.id, r.id "
                        + "FROM Veiculo v, Missao m "
                        + "INNER JOIN m.requisicoesTransporte r "
                        + "WHERE m.dataHoraRetorno BETWEEN :dataInicio AND :dataFinal "
                        + "ORDER BY v.id, r.id";

        Query qry = ContextoPersistencia.em().createQuery(qrl);
        qry.setParameter("dataInicio", relatorio.getDataInicio());
        qry.setParameter("dataFinal", relatorio.getDataFim());

         lista = (List<Object[]>) qry.getResultList();

        Long idProximoVeiculo = 0L;
        RankingVeiculoRequisicao itemRv = null;
        Boolean salvar = false;

        for (int i = 0; i < lista.size(); i++) {
            veiculo = new Veiculo();
            veiculo.setId(Long.parseLong(lista.get(i)[0].toString()));

            requisicao = new RequisicaoTransporte();
            requisicao.setId(Long.parseLong(lista.get(i)[1].toString()));
            setRequisicao.add((RequisicaoTransporte) RequisicaoTransporte.AR.findById(requisicao.getId()));

            if (i < lista.size() - 1) {
                idProximoVeiculo = Long.parseLong(lista.get(i + 1)[0].toString());

                if (!veiculo.getId().equals(idProximoVeiculo)) {
                    salvar = true;
                }
            } else {
                salvar = true;
            }

            if (salvar) {
                itemRv = new RelatorioRanking().new RankingVeiculoRequisicao();
                itemRv.setVeiculo(Veiculo.AR.findById(veiculo.getId()));
                itemRv.setRequisicoes(new ArrayList<RequisicaoTransporte>(setRequisicao));
                listaRankingVeiculo.add(itemRv);
                setRequisicao.clear();
                salvar = false;
            }
        }

        Collections.sort(listaRankingVeiculo);

        return listaRankingVeiculo;
    }

}