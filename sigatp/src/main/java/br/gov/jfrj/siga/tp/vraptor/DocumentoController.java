package br.gov.jfrj.siga.tp.vraptor;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import br.gov.jfrj.siga.tp.auth.AutorizacaoGI;
import br.gov.jfrj.siga.tp.exceptions.ApplicationControllerException;
import br.gov.jfrj.siga.tp.model.RequisicaoTransporte;
import br.gov.jfrj.siga.tp.model.SelecaoDocumento;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.vraptor.i18n.MessagesBundle;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/app/documento")
public class DocumentoController extends TpController {
	
    @SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);

    @Inject
    private AutorizacaoGI autorizacaoGI;

    private MissaoController missaoController;

    private RequisicaoController requisicaoController;

    private ServicoVeiculoController servicoVeiculoController;
	
	/**
	 * @deprecated CDI eyes only
	 */
	public DocumentoController() {
		super();
	}
	
	@Inject
    public DocumentoController(HttpServletRequest request, Result result,  Validator validator, SigaObjects so,   EntityManager em, MissaoController missaoController,
            RequisicaoController requisicaoController, ServicoVeiculoController servicoVeiculoController) {
        super(request, result, TpDao.getInstance(), validator, so, em);
        this.missaoController = missaoController;
        this.requisicaoController = requisicaoController;
        this.servicoVeiculoController = servicoVeiculoController;
    }
	

    @Get
    @Post
    @Path("/selecionar")
    public void selecionar(String sigla) {
        SelecaoDocumento sel = new SelecaoDocumento();
        sel.setSigla(montarSigla(sigla));
        sel.setId(0L);
        sel.setDescricao("");
        result.include("sel", sel);
    }

    @Path({ "/exibir/{sigla}", "/exibir" })
    public void exibir(String sigla) throws ApplicationControllerException {

        String[] partesDoCodigo = null;
        try {
            partesDoCodigo = sigla.split("[-/]");
        } catch (Exception e) {
            throw new ApplicationControllerException(MessagesBundle.getMessage("application.exibir.sigla.exception", sigla), e);
        }

        result.include("sigla", partesDoCodigo[4]);

        try {
            if ("TP".equals(partesDoCodigo[1])) {
                if ("M".equals(partesDoCodigo[4])) {
                    missaoController.recuperarPelaSigla(sigla, !autorizacaoGI.ehAdministrador());
                }

                if ("R".equals(partesDoCodigo[4])) {
                    RequisicaoTransporte req = requisicaoController.recuperarPelaSigla(sigla, !autorizacaoGI.ehAdministrador());
                    requisicaoController.carregarTiposDeCarga(req);
                    requisicaoController.carregarFinalidades();
                }

                if ("S".equals(partesDoCodigo[4])) {
                    servicoVeiculoController.recuperarPelaSigla(sigla, !autorizacaoGI.ehAdministrador());
                }
            }
        } catch (Exception e) {
            throw new ApplicationControllerException(e);
        }
    }
    
    private String montarSigla(String sigla) {
        String retorno = "";
        String siglaUpper = sigla.trim().toUpperCase();

        // substitui o pen�ltimo "-" por "/" caso n�o tenha nenhum.
        if (StringUtils.countMatches(siglaUpper, "-") > 0 && StringUtils.countMatches(siglaUpper, "/") == 0) {
            int total = StringUtils.countMatches(siglaUpper, "-");
            int posicao = StringUtils.ordinalIndexOf(siglaUpper, "-", total - 1);
            StringBuilder strSigla = new StringBuilder(siglaUpper);
            strSigla.setCharAt(posicao, "/".charAt(0));
            siglaUpper = strSigla.toString();
        }

        // Formato TRF2-TP-2014/00001-R
        final Pattern p = Pattern.compile("^?([A-Z]{2})?-?(TP{1})-?([0-9]{4})?/?([0-9]{1,5})-?([MSR]{1})?$");
        final Matcher m = p.matcher(siglaUpper);

        if (m.find()) {
            if (m.group(1) != null) {
                retorno = m.group(1).toString();
            } else {
                retorno = getCadastrante().getOrgaoUsuario().getAcronimoOrgaoUsu().replace("-", "").toString();
            }

            retorno = retorno + "-" + m.group(2).toString();

            Calendar c1 = Calendar.getInstance();

            if (m.group(3) != null && m.group(4) != null && (m.group(3).toString() + m.group(5).toString()).length() <= 5) {
                c1.set(Calendar.YEAR, Integer.valueOf(m.group(3)));
                c1.set(Calendar.DAY_OF_YEAR, 1);
            }

            retorno = retorno + "-" + String.format("%04d", c1.get(Calendar.YEAR));

            if (m.group(3) != null && m.group(4) != null) {
                if ((m.group(3).toString() + m.group(4).toString()).length() <= 5) {
                    retorno = retorno + "/" + String.format("%05d", Integer.parseInt(m.group(3).toString() + m.group(4).toString()));
                } else {
                    retorno = retorno + "/" + String.format("%05d", Integer.parseInt(m.group(4)));
                }
            } else if (m.group(3) != null && m.group(4) == null) {
                retorno = retorno + "/" + String.format("%05d", Integer.parseInt(m.group(3)));
            } else if (m.group(4) != null) {
                retorno = retorno + "/" + String.format("%05d", Integer.parseInt(m.group(4)));
            } else {
                retorno = retorno + "/0";
            }

            retorno = retorno + "-" + m.group(5);

            return retorno;
        }

        return siglaUpper;
    }
	
}
