package br.gov.jfrj.siga.tp.vraptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.SimpleMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.tp.model.Condutor;
import br.gov.jfrj.siga.tp.model.FinalidadeRequisicao;
import br.gov.jfrj.siga.tp.model.Missao;
import br.gov.jfrj.siga.tp.model.RelatorioRanking;
import br.gov.jfrj.siga.tp.model.RelatorioRanking.RankingCondutorRequisicao;
import br.gov.jfrj.siga.tp.model.RelatorioRanking.RankingFinalidadeRequisicao;
import br.gov.jfrj.siga.tp.model.RelatorioRanking.RankingTipoPassageiroRequisicao;
import br.gov.jfrj.siga.tp.model.RelatorioRanking.RankingVeiculoRequisicao;
import br.gov.jfrj.siga.tp.model.RequisicaoTransporte;
import br.gov.jfrj.siga.tp.model.TipoDePassageiro;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.model.Veiculo;
import br.gov.jfrj.siga.tp.util.FormatarDataHora;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/app/relatorioRanking")
public class RelatorioRankingController extends TpController {
    private static final String RELATORIO_RANKING = "relatorioRanking";
    private static final String VALOR_DEFAULT = "valorDefault";
    private static final String OPT_VALORES = "optValores";
    private static final String END_23_59_59 = "23:59:59";
    private static final String START_00_00_00 = "00:00:00";

	/**
	 * @deprecated CDI eyes only
	 */
	public RelatorioRankingController() {
		super();
	}
	
	@Inject
    public RelatorioRankingController(HttpServletRequest request, Result result,  Validator validator, SigaObjects so,  EntityManager em) {
        super(request, result, TpDao.getInstance(), validator, so, em);
    }

    private static String[] gerarVetorNumeros() {
        String[] vetor = new String[20];
        for (int i = 1; i <= 20; i++) {
            vetor[i - 1] = String.valueOf(i);
        }
        return vetor;
    }

    @Path("/consultar")
    public void consultar() {
        String[] numeros = gerarVetorNumeros();
        String valorDefault = "1";
        RelatorioRanking relatorioRanking = new RelatorioRanking();
        result.include(OPT_VALORES, numeros);
        result.include(VALOR_DEFAULT, valorDefault);

        result.include(RELATORIO_RANKING, relatorioRanking);
    }

    public void gerarRelatorios(RelatorioRanking relatorioRanking) throws Exception {
        String[] numeros = gerarVetorNumeros();
        String valorDefault = "1";

        if (validator.hasErrors()) {
            result.include(OPT_VALORES, numeros);
            result.include(VALOR_DEFAULT, valorDefault);

            validator.onErrorUse(Results.page()).of(RelatorioRankingController.class).consultar();

        } else {
            validaDatas(relatorioRanking);

            if (!validator.hasErrors()) {
                List<RankingCondutorRequisicao> rc = retornarCondutoresQueAtenderamMaisRequisicoes(relatorioRanking);
                List<RankingVeiculoRequisicao> rv = retornarVeiculosQueAtenderamMaisRequisicoes(relatorioRanking);
                List<RankingFinalidadeRequisicao> rf = retornarFinalidadesComMaisRequisicoes(relatorioRanking);
                List<RankingTipoPassageiroRequisicao> rtp = retornarTipoPassageiroComMaisRequisicoes(relatorioRanking);

                result.include("rc", rc.subList(0, Math.min(rc.size(), relatorioRanking.getQuantidadeDadosRetorno())));
                result.include("rv", rv.subList(0, Math.min(rv.size(), relatorioRanking.getQuantidadeDadosRetorno())));
                result.include("rf", rf.subList(0, Math.min(rf.size(), relatorioRanking.getQuantidadeDadosRetorno())));
                result.include("rtp", rtp.subList(0, Math.min(rtp.size(), relatorioRanking.getQuantidadeDadosRetorno())));
                result.include(RELATORIO_RANKING, relatorioRanking);
            } else {
                result.include(OPT_VALORES, numeros);
                result.include(VALOR_DEFAULT, valorDefault);
                result.include(RELATORIO_RANKING, relatorioRanking);
                result.redirectTo(this).consultar();
            }
        }
    }

    private void validaDatas(RelatorioRanking relatorioRanking) {
        StringBuilder msgErro = new StringBuilder();

        if (relatorioRanking.getDataInicio() == null || relatorioRanking.getDataFim() == null) {
            msgErro.append("Data in&iacute;cio / data fim deve(m) ser preenchida(s).");
        }

        if (relatorioRanking.getDataFim() != null && relatorioRanking.getDataInicio() != null && relatorioRanking.getDataFim().getTime().before(relatorioRanking.getDataInicio().getTime())) {
            msgErro.append("Data in&iacute;cio n&atilde;o deve ser maior que a data fim.");
        }

        if (!"".equals(msgErro.toString())) {
            result.include(OPT_VALORES, gerarVetorNumeros());
            validator.add(new SimpleMessage("relatorio",msgErro.toString()));
            validator.onErrorUse(Results.page()).of(RelatorioRankingController.class).consultar();
        }
    }

    @SuppressWarnings("unchecked")
    private List<RankingCondutorRequisicao> retornarCondutoresQueAtenderamMaisRequisicoes(RelatorioRanking relatorio) throws Exception {
        List<Object[]> lista;
        List<RankingCondutorRequisicao> listaRankingCondutor = new ArrayList<RankingCondutorRequisicao>();
        Set<Missao> setMissao = new HashSet<Missao>();
        Set<RequisicaoTransporte> setRequisicao = new HashSet<RequisicaoTransporte>();
        Condutor condutor = null;
        Missao missao = null;
        RequisicaoTransporte requisicao = null;
        CpOrgaoUsuario cpOrgaoUsuario = getTitular().getOrgaoUsuario();

        relatorio.getDataInicio().setTime(FormatarDataHora.formatarDataHora(relatorio.getDataInicio(), START_00_00_00));
        relatorio.getDataFim().setTime(FormatarDataHora.formatarDataHora(relatorio.getDataFim(), END_23_59_59));

        String qrl = "SELECT c.id, m.id, r.id "
                        + "FROM Condutor c, Missao m "
                        + "INNER JOIN m.requisicoesTransporte r "
                        + "WHERE c.id = m.condutor.id "
                        + "and dataHoraRetorno BETWEEN :dataInicio AND :dataFinal "
                        + "AND   r.cpOrgaoUsuario.idOrgaoUsu = :idOrgaoUsu "
                        + "ORDER BY c.id, m.id, r.id";

        Query qry = ContextoPersistencia.em().createQuery(qrl);
        qry.setParameter("dataInicio", relatorio.getDataInicio());
        qry.setParameter("dataFinal", relatorio.getDataFim());
        qry.setParameter("idOrgaoUsu", cpOrgaoUsuario.getIdOrgaoUsu());

        lista = (List<Object[]>) qry.getResultList();
        Long idProximoCondutor = 0L;
        RankingCondutorRequisicao itemRc = null;
        Boolean salvar = false;

        for (int i = 0; i < lista.size(); i++) {
            condutor = new Condutor();
            condutor.setId(Long.parseLong(lista.get(i)[0].toString()));

            missao = new Missao();
            missao.setId(Long.parseLong(lista.get(i)[1].toString()));
            setMissao.add((Missao) Missao.AR.findById(missao.getId()));

            requisicao = new RequisicaoTransporte();
            requisicao.setId(Long.parseLong(lista.get(i)[2].toString()));
            setRequisicao.add((RequisicaoTransporte) RequisicaoTransporte.AR.findById(requisicao.getId()));

            if (i < lista.size() - 1) {
                idProximoCondutor = Long.parseLong(lista.get(i + 1)[0].toString());

                if (!condutor.getId().equals(idProximoCondutor)) {
                    salvar = true;
                }
            } else {
                salvar = true;
            }

            if (salvar) {
                itemRc = new RelatorioRanking().new RankingCondutorRequisicao();
                itemRc.setCondutor(Condutor.AR.findById(condutor.getId()));
                itemRc.setMissoes(new ArrayList<Missao>(setMissao));
                itemRc.setRequisicoes(new ArrayList<RequisicaoTransporte>(setRequisicao));
                listaRankingCondutor.add(itemRc);
                setMissao.clear();
                setRequisicao.clear();
                salvar = false;
            }
        }

        Collections.sort(listaRankingCondutor);

        return listaRankingCondutor;
    }

    @SuppressWarnings("unchecked")
    public List<RankingVeiculoRequisicao> retornarVeiculosQueAtenderamMaisRequisicoes(RelatorioRanking relatorio) throws Exception {
        List<Object[]> lista;
        List<RankingVeiculoRequisicao> listaRankingVeiculo = new ArrayList<RankingVeiculoRequisicao>();
        Set<RequisicaoTransporte> setRequisicao = new HashSet<RequisicaoTransporte>();
        Veiculo veiculo = null;
        RequisicaoTransporte requisicao = null;
        CpOrgaoUsuario cpOrgaoUsuario = getTitular().getOrgaoUsuario();

        relatorio.getDataInicio().setTime(FormatarDataHora.formatarDataHora(relatorio.getDataInicio(), START_00_00_00));
        relatorio.getDataFim().setTime(FormatarDataHora.formatarDataHora(relatorio.getDataFim(), END_23_59_59));

        String qrl = "SELECT v.id, r.id "
                        + "FROM Veiculo v, Missao m "
                        + "INNER JOIN m.requisicoesTransporte r "
                        + "WHERE v.id = m.veiculo.id "
                        + "and   dataHoraRetorno BETWEEN :dataInicio AND :dataFinal "
                        + "AND   r.cpOrgaoUsuario.idOrgaoUsu = :idOrgaoUsu "
                        + "ORDER BY v.id, r.id";

        Query qry = ContextoPersistencia.em().createQuery(qrl);
        qry.setParameter("dataInicio", relatorio.getDataInicio());
        qry.setParameter("dataFinal", relatorio.getDataFim());
        qry.setParameter("idOrgaoUsu", cpOrgaoUsuario.getIdOrgaoUsu());
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

    @SuppressWarnings("unchecked")
    public List<RankingFinalidadeRequisicao> retornarFinalidadesComMaisRequisicoes(RelatorioRanking relatorio) throws Exception {
        List<Object[]> lista;
        List<RankingFinalidadeRequisicao> listaRankingFinalidade = new ArrayList<RankingFinalidadeRequisicao>();
        FinalidadeRequisicao finalidade = null;
        int totalFinalidade = 0;
        CpOrgaoUsuario cpOrgaoUsuario = getTitular().getOrgaoUsuario();

        relatorio.getDataInicio().setTime(FormatarDataHora.formatarDataHora(relatorio.getDataInicio(), START_00_00_00));
        relatorio.getDataFim().setTime(FormatarDataHora.formatarDataHora(relatorio.getDataFim(), END_23_59_59));

        String qrl = "SELECT f.id, count(f.id) as total_finalidade "
                        + "FROM  FinalidadeRequisicao f, RequisicaoTransporte r "
                        + "WHERE r.tipoFinalidade.id = f.id "
                        + "and   r.dataHora BETWEEN :dataInicio AND :dataFinal "
                        + "AND   r.cpOrgaoUsuario.idOrgaoUsu = :idOrgaoUsu "
                        + "GROUP BY f.id "
                        + "ORDER BY total_finalidade DESC";

        Query qry = ContextoPersistencia.em().createQuery(qrl);
        qry.setParameter("dataInicio", relatorio.getDataInicio());
        qry.setParameter("dataFinal", relatorio.getDataFim());
        qry.setParameter("idOrgaoUsu", cpOrgaoUsuario.getIdOrgaoUsu());

        lista = (List<Object[]>) qry.getResultList();
        RankingFinalidadeRequisicao itemRf = null;

        for (int i = 0; i < lista.size(); i++) {
            finalidade = new FinalidadeRequisicao();
            finalidade.setId(Long.parseLong(lista.get(i)[0].toString()));
            totalFinalidade = Integer.parseInt(lista.get(i)[1].toString());

            itemRf = new RelatorioRanking().new RankingFinalidadeRequisicao();
            itemRf.setFinalidade(FinalidadeRequisicao.AR.findById(finalidade.getId()));
            itemRf.setTotalFinalidade(totalFinalidade);
            listaRankingFinalidade.add(itemRf);
        }

        return listaRankingFinalidade;
    }

    public List<RankingTipoPassageiroRequisicao> retornarTipoPassageiroComMaisRequisicoes(RelatorioRanking relatorio) throws Exception {
        List<RequisicaoTransporte> lista;
        List<RankingTipoPassageiroRequisicao> listaRankingTipoPassageiro = new ArrayList<RankingTipoPassageiroRequisicao>();
        List<TipoDePassageiro> listaTipoDePassageiro = Arrays.asList(TipoDePassageiro.values());
        CpOrgaoUsuario cpOrgaoUsuario = getTitular().getOrgaoUsuario();

        relatorio.getDataInicio().setTime(FormatarDataHora.formatarDataHora(relatorio.getDataInicio(), START_00_00_00));
        relatorio.getDataFim().setTime(FormatarDataHora.formatarDataHora(relatorio.getDataFim(), END_23_59_59));
		Map<String, Object> parametros = new HashMap<String,Object>();
		parametros.put("idOrgaoUsu", cpOrgaoUsuario.getIdOrgaoUsu());
		parametros.put("dataInicial", relatorio.getDataInicio());
		parametros.put("dataFinal", relatorio.getDataFim());
        lista = RequisicaoTransporte.AR.find("dataHora BETWEEN :dataInicial AND :dataFinal " + "AND cpOrgaoUsuario.idOrgaoUsu = :idOrgaoUsu ", parametros).fetch();
        RankingTipoPassageiroRequisicao itemRp = null;

        for (int i = 0; i < listaTipoDePassageiro.size(); i++) {
            final TipoDePassageiro tipoPassageiro = listaTipoDePassageiro.get(i);

            if (!lista.isEmpty()) {
                List<RequisicaoTransporte> requisicoesFiltradas = Lists.newArrayList(Iterables.filter(lista, new Predicate<RequisicaoTransporte>() {

                    @Override
                    public boolean apply(RequisicaoTransporte requisicao) {
                        return requisicao.getTiposDePassageiro().contains(tipoPassageiro);
                    }
                }));

                itemRp = new RelatorioRanking().new RankingTipoPassageiroRequisicao();
                itemRp.setTipoPassageiro(tipoPassageiro.getDescricao());
                itemRp.setTotalTipoPassageiros(requisicoesFiltradas.size());
                listaRankingTipoPassageiro.add(itemRp);
            }
        }

        Collections.sort(listaRankingTipoPassageiro);

        return listaRankingTipoPassageiro;
    }
}