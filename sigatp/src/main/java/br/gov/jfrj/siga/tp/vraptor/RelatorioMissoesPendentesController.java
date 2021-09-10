package br.gov.jfrj.siga.tp.vraptor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.tp.model.Condutor;
import br.gov.jfrj.siga.tp.model.EstadoMissao;
import br.gov.jfrj.siga.tp.model.RelatorioMissoesPendentes;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;


@Controller
@Path("/app/relatorioMissoesPendentes")
public class RelatorioMissoesPendentesController extends TpController {
	private static final String MISSOES_PENDENTES = "missoesPendentes";

	/**
	 * @deprecated CDI eyes only
	 */
	public RelatorioMissoesPendentesController() {
		super();
	}
	
	@Inject
	public RelatorioMissoesPendentesController(HttpServletRequest request, Result result,  Validator validator, SigaObjects so,  EntityManager em) {
        super(request, result, TpDao.getInstance(), validator, so, em);
    }

    @SuppressWarnings("unchecked")
    @Path("/gerarRelatorio")
    public void gerarRelatorio() throws Exception {
        List<Object[]> lista;
        List<RelatorioMissoesPendentes> missoesPendentes = new ArrayList<RelatorioMissoesPendentes>();
        Long totalMissoes = 0L;
        Condutor condutor = null;
        CpOrgaoUsuario cpOrgaoUsuario = getTitular().getOrgaoUsuario();

        String qrl = "SELECT     c.id, COUNT(m.id) AS TotalMissoes "
                   + "FROM       Condutor c, Missao m "
                   + "WHERE      c.id = m.condutor.id "
				   + "AND        m.cpOrgaoUsuario.idOrgaoUsu = :idOrgaoUsu "
				   + "AND        m.estadoMissao = :estado "
				   + "GROUP BY   c.id "
                   + "ORDER BY   c.id";

        Query qry = ContextoPersistencia.em().createQuery(qrl);
        qry.setParameter("idOrgaoUsu", cpOrgaoUsuario.getIdOrgaoUsu());
        qry.setParameter("estado", EstadoMissao.PROGRAMADA);

        lista = (List<Object[]>) qry.getResultList();
        RelatorioMissoesPendentes missaoPendente = null;

        for (int i = 0; i < lista.size(); i++) {
            condutor = new Condutor();
            condutor.setId(Long.parseLong(lista.get(i)[0].toString()));

            totalMissoes = Long.parseLong(lista.get(i)[1].toString());

			missaoPendente = new RelatorioMissoesPendentes();
			missaoPendente.setCondutor(Condutor.AR.findById(condutor.getId()));
			missaoPendente.setTotalMissoes(totalMissoes);
			missoesPendentes.add(missaoPendente);
        }

        result.include(MISSOES_PENDENTES, missoesPendentes);
    }	
}