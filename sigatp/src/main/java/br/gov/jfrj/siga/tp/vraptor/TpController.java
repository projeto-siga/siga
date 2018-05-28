package br.gov.jfrj.siga.tp.vraptor;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.tp.auth.AutorizacaoGI;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.vraptor.i18n.MessagesBundle;
import br.gov.jfrj.siga.vraptor.SigaController;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class TpController extends SigaController {

    protected Validator validator;

    public TpController(HttpServletRequest request, Result result, CpDao dao, Validator validator, SigaObjects so, EntityManager em) {
        super(request, result, dao, so, em);
        this.validator = validator;
        this.result.include("currentTimeMillis", new Date().getTime());
    }

    protected void error(boolean errorCondition, String category, String message) {
        if (errorCondition) {
            validator.add(new I18nMessage(category, message));
        }
    }

    protected CpComplexo recuperarComplexoPadrao() {
        return recuperarComplexoPadrao(getTitular());
    }

    protected CpComplexo getComplexoAdministrado() {
        return (CpComplexo) getRequest().getAttribute(AutorizacaoGI.CP_COMPLEXO_ADMINISTRADOR);
    }

    public CpComplexo recuperarComplexoPadrao(DpPessoa dpPessoa) {
        long TIPO_CONFIG_COMPLEXO_PADRAO = 400;
        CpTipoConfiguracao tpConf = TpDao.findById(CpTipoConfiguracao.class, TIPO_CONFIG_COMPLEXO_PADRAO);
        CpSituacaoConfiguracao cpSituacaoConfiguracaoPode = TpDao.findById(CpSituacaoConfiguracao.class, 1L);
        CpSituacaoConfiguracao cpSituacaoConfiguracaoPadrao = TpDao.findById(CpSituacaoConfiguracao.class, 5L);
        List<CpConfiguracao> configuracoes = null;
        CpComplexo cpComplexo = null;

        // Recuperando Configuracao Pode para uma lotacao especifica
        Object[] parametros = { dpPessoa.getLotacao().getIdLotacaoIni(), cpSituacaoConfiguracaoPode, dpPessoa.getOrgaoUsuario(), tpConf };
        configuracoes = TpDao.find(CpConfiguracao.class, "((lotacao.idLotacaoIni = ? and cpSituacaoConfiguracao = ?) and orgaoUsuario = ?  and cpTipoConfiguracao = ? and hisIdcFim is null  )",
                parametros).fetch();
        if (configuracoes != null && !configuracoes.isEmpty()) {
            cpComplexo = configuracoes.get(0).getComplexo();
        } else {

            // Recuperando Configuracao default para um orgao especifico
            Object[] parametros1 = { cpSituacaoConfiguracaoPadrao, dpPessoa.getOrgaoUsuario(), tpConf };
            configuracoes = TpDao.find(CpConfiguracao.class, "((cpSituacaoConfiguracao = ?) and orgaoUsuario = ?  and cpTipoConfiguracao = ? and hisIdcFim is null  )", parametros1).fetch();
            if (configuracoes != null && !configuracoes.isEmpty()) {
                cpComplexo = configuracoes.get(0).getComplexo();
            }
        }
        if (cpComplexo == null) {
            throw new NullPointerException(MessagesBundle.getMessage("cpComplexo.null.exception", ""));
        }

        return cpComplexo;
    }
}