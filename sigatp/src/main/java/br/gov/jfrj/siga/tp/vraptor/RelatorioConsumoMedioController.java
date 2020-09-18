package br.gov.jfrj.siga.tp.vraptor;

import java.util.ArrayList;
import java.util.Calendar;
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

import org.apache.commons.lang.StringUtils;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.SimpleMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.tp.exceptions.RelatorioConsumoMedioException;
import br.gov.jfrj.siga.tp.model.Abastecimento;
import br.gov.jfrj.siga.tp.model.EstadoMissao;
import br.gov.jfrj.siga.tp.model.Missao;
import br.gov.jfrj.siga.tp.model.RelatorioConsumoMedio;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.model.Veiculo;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/app/relatorioConsumoMedio")
public class RelatorioConsumoMedioController extends TpController {

/*    private static final String SEPARADOR = "'";
    private static final String SELECTED_SELECTED = " selected='selected'";
    private static final String END_OPTION = ">";
    private static final String OPTION = "</option>";
    private static final String OPTION_VALUE = "<option value='";
*/
	/**
	 * @deprecated CDI eyes only
	 */
	public RelatorioConsumoMedioController() {
		super();
	}
	
	@Inject
	public RelatorioConsumoMedioController(HttpServletRequest request, Result result,   Validator validator, SigaObjects so,  EntityManager em) {
        super(request, result, TpDao.getInstance(), validator, so, em);
    }

    @Path("/consultar")
    public void consultar() throws RelatorioConsumoMedioException {
        RelatorioConsumoMedio relatorioConsumoMedio = new RelatorioConsumoMedio();
        montarCombos(null);
        result.include("relatorioConsumoMedio", relatorioConsumoMedio);
    }

    @Path("/gerarRelatorio")
    public void gerarRelatorio(RelatorioConsumoMedio relatorioConsumoMedio) throws RelatorioConsumoMedioException {
        if (validator.hasErrors()) {
            montarCombos(null);
            validator.onErrorUsePageOf(this).consultar();
        } else {
            String msgErro = validarDadosRelatorio(relatorioConsumoMedio);
            tratarMensagemValidacaoRelatorio(msgErro);

            if (!validator.hasErrors()) {
                RelatorioConsumoMedio relatoriocm = calcularConsumoMedio(relatorioConsumoMedio);
                result.include("relatoriocm", relatoriocm);
            } else {
                montarCombos(relatorioConsumoMedio.getVeiculo());
                result.include("relatorioConsumoMedio", relatorioConsumoMedio);
                validator.onErrorUsePageOf(this).consultar();
            }
        }
    }

    private String tratarMensagemValidacaoRelatorio(String msgErro) {
        String msgErroTratada = msgErro;
        if (!"".equals(msgErroTratada)) {
            boolean plural = StringUtils.countMatches(msgErroTratada, ",") > 1 ? true : false;
            msgErroTratada = msgErroTratada.substring(0, msgErroTratada.length() - 2);
            msgErroTratada += " deve" + (plural ? "m" : "") + " ser preenchido" + (plural ? "s" : "");
            validator.add(new SimpleMessage("RelatorioConsumoMedio",msgErroTratada));
        }
        return msgErroTratada;
    }

    private String validarDadosRelatorio(RelatorioConsumoMedio relatorioConsumoMedio) {
        String msgErro = "";

        if (relatorioConsumoMedio.getAbastecimentoInicial() == null || relatorioConsumoMedio.getAbastecimentoInicial().getId() == 0) {
            msgErro += "Abastecimento Inicial, ";
        }
        if (relatorioConsumoMedio.getAbastecimentoFinal() == null || relatorioConsumoMedio.getAbastecimentoFinal().getId() == 0) {
            msgErro += "Abastecimento Final, ";
        }
        return msgErro;
    }

    @SuppressWarnings("unchecked")
    private RelatorioConsumoMedio calcularConsumoMedio(RelatorioConsumoMedio relatorio) throws RelatorioConsumoMedioException {
        try {
            List<Object[]> lista;
            Set<Missao> setMissao = new HashSet<Missao>();
            Missao missao = null;
            CpOrgaoUsuario cpOrgaoUsuario = getTitular().getOrgaoUsuario();
            RelatorioConsumoMedio resultado = new RelatorioConsumoMedio();

            Calendar dataInicial = Calendar.getInstance();
            relatorio.setAbastecimentoInicial(Abastecimento.AR.findById(relatorio.getAbastecimentoInicial().getId()));
            dataInicial.setTime(relatorio.getAbastecimentoInicial().getDataHora().getTime());

            Calendar dataFinal = Calendar.getInstance();
            relatorio.setAbastecimentoFinal(Abastecimento.AR.findById(relatorio.getAbastecimentoFinal().getId()));
            dataFinal.setTime(relatorio.getAbastecimentoFinal().getDataHora().getTime());
            String qrl = "SELECT m.id, m.consumoEmLitros, m.odometroSaidaEmKm, m.odometroRetornoEmKm " + "FROM  Missao m " + "WHERE m.veiculo.id = :idVeiculo " + "AND   m.dataHora BETWEEN :dataInicial AND :dataFinal "
                    + "AND   m.cpOrgaoUsuario.idOrgaoUsu = :idOrgaoUsu " + "AND   m.estadoMissao = :estadoMissao ";

            Query qry = Missao.AR.em().createQuery(qrl);
            qry.setParameter("idVeiculo", relatorio.getVeiculo().getId());
            qry.setParameter("dataInicial", dataInicial);
            qry.setParameter("dataFinal", dataFinal);
            qry.setParameter("idOrgaoUsu", cpOrgaoUsuario.getIdOrgaoUsu());
            qry.setParameter("estadoMissao", EstadoMissao.FINALIZADA);

            lista = (List<Object[]>) qry.getResultList();

            double kmInicial = relatorio.getAbastecimentoInicial().getOdometroEmKm();
            double kmFinal = relatorio.getAbastecimentoFinal().getOdometroEmKm();
            double quantidadeEmLitros = relatorio.getAbastecimentoFinal().getQuantidadeEmLitros();

            for (int i = 0; i < lista.size(); i++) {
                if ((Double.parseDouble(lista.get(i)[2].toString()) >= kmInicial) || (Double.parseDouble(lista.get(i)[3].toString()) <= kmFinal)) {
                    missao = new Missao();
                    missao.setId(Long.parseLong(lista.get(i)[0].toString()));
                    setMissao.add((Missao) Missao.AR.findById(missao.getId()));
                }
            }

            resultado.setAbastecimentoInicial(new Abastecimento());
            resultado.getAbastecimentoInicial().setDataHora(dataInicial);

            resultado.setAbastecimentoFinal(new Abastecimento());
            resultado.getAbastecimentoFinal().setDataHora(dataFinal);

            resultado.setVeiculo(Veiculo.AR.findById(relatorio.getVeiculo().getId()));
            resultado.setMissoes(new ArrayList<Missao>(setMissao));
            resultado.setKmPercorridos(Double.parseDouble(String.format("%.2f", kmFinal - kmInicial).replace(",", ".")));
            resultado.setConsumoMedio(Double.parseDouble(String.format("%.2f", quantidadeEmLitros <= 0 ? 0 : (kmFinal - kmInicial) / quantidadeEmLitros).replace(",", ".")));
            setMissao.clear();
            return resultado;
        } catch (Exception e) {
            throw new RelatorioConsumoMedioException(e);
        }
    }

    private void montarCombos(Veiculo veiculo) throws RelatorioConsumoMedioException {
        try {
        	List<Veiculo> veiculos = Veiculo.listarParaConsumoMedio(getTitular().getOrgaoUsuario());
            result.include("veiculos", veiculos);
        } catch (Exception e) {
            throw new RelatorioConsumoMedioException(e);
        }
    }
    
    public List<Abastecimento> montarCombosAbastecimento(Long idVeiculo) throws RelatorioConsumoMedioException {
        Veiculo veiculo;
        try {
            veiculo = Veiculo.AR.findById(idVeiculo);
            return Abastecimento.buscarTodosPorVeiculo(veiculo);
        } catch (Exception e) {
            throw new RelatorioConsumoMedioException(e);
        }
    }

    @Path("/carregarComboAbastecimentoInicial/{idVeiculo}")
    public void carregarComboAbastecimentoInicial(Long idVeiculo) throws RelatorioConsumoMedioException {
    	StringBuffer htmlSelectAbastecimentoInicial = new StringBuffer();
		StringBuffer htmlSelectAbastecimentoReferencia = new StringBuffer();
        List<Abastecimento> lstAbastecimento = montarCombosAbastecimento(idVeiculo);

        for (Abastecimento abastecimento : lstAbastecimento) {
			if (lstAbastecimento.indexOf(abastecimento) >= 1) {
				htmlSelectAbastecimentoInicial.append("<option value='"+ abastecimento.getId() + "'");
				if (lstAbastecimento.indexOf(abastecimento) == 1) {
					htmlSelectAbastecimentoInicial.append(" selected='selected'");
				}
				htmlSelectAbastecimentoInicial.append(">" + abastecimento.getDadosParaExibicao());
				htmlSelectAbastecimentoInicial.append("</option>");
			}
			
			htmlSelectAbastecimentoReferencia.append("<option value='"+ abastecimento.getId() + "'");
			htmlSelectAbastecimentoReferencia.append(">" + abastecimento.getDadosParaExibicao());
			htmlSelectAbastecimentoReferencia.append("</option>");
        }
        
        String html = htmlSelectAbastecimentoInicial.toString() + "@" + htmlSelectAbastecimentoReferencia.toString();
        result.use(Results.http()).body(html);
    }

/*    private void gerarHTMLVariasOpcoes(StringBuilder htmlSelectAbastecimentoInicial, StringBuilder htmlSelectAbastecimentoFinal, List<Abastecimento> lstAbastecimento) {
        for (Abastecimento abastecimento : lstAbastecimento) {

            htmlSelectAbastecimentoInicial.append(OPTION_VALUE + abastecimento.getId() + SEPARADOR);

            if (lstAbastecimento.indexOf(abastecimento) == 1) {
                htmlSelectAbastecimentoInicial.append(SELECTED_SELECTED);
            }

            htmlSelectAbastecimentoInicial.append(END_OPTION + abastecimento.getDadosParaExibicao());
            htmlSelectAbastecimentoInicial.append(OPTION);

            if (abastecimento.getDataHora().after(lstAbastecimento.get(1).getDataHora())) {
                htmlSelectAbastecimentoFinal.append(OPTION_VALUE + abastecimento.getId() + SEPARADOR);

                if (lstAbastecimento.indexOf(abastecimento) == 2) {
                    htmlSelectAbastecimentoFinal.append(SELECTED_SELECTED);
                }

                htmlSelectAbastecimentoFinal.append(END_OPTION + abastecimento.getDadosParaExibicao());
                htmlSelectAbastecimentoFinal.append(OPTION);
            }
        }
    }

    private void gerarHTMLOpcao(StringBuilder htmlSelectAbastecimentoInicial, List<Abastecimento> lstAbastecimento) {
        htmlSelectAbastecimentoInicial.append(OPTION_VALUE + lstAbastecimento.get(0).getId() + SEPARADOR);
        htmlSelectAbastecimentoInicial.append(END_OPTION + lstAbastecimento.get(0).getDadosParaExibicao());
        htmlSelectAbastecimentoInicial.append(OPTION);
    }
*/
}