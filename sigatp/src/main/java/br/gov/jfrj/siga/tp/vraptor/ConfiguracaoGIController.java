package br.gov.jfrj.siga.tp.vraptor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.tp.exceptions.ConfiguracaoGIControllerException;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/configuracaoGI")
public class ConfiguracaoGIController extends TpController {

    private static final String CP_COMPLEXOS = "cpComplexos";
    private static final String CP_SITUACOES_CONFIGURACAO = "cpSituacoesConfiguracao";
    private static final String CP_TIPOS_CONFIGURACAO = "cpTiposConfiguracao";
    private static final String CP_CONFIGURACAO = "cpConfiguracao";
    private static final String CP_ORGAO_USUARIOS = "cpOrgaoUsuarios";
    private static final String CP_ORGAO_USUARIO = "cpOrgaoUsuario";
    private static final String CP_CONFIGURACOES = "cpConfiguracoes";

    public ConfiguracaoGIController(HttpServletRequest request, Result result, CpDao dao, Validator validator, SigaObjects so, EntityManager em) {
        super(request, result, TpDao.getInstance(), validator, so, em);
    }

    @Path("/pesquisar")
    public void pesquisar() throws ConfiguracaoGIControllerException {
        try {
            result.redirectTo(ConfiguracaoGIController.class).pesquisar(getTitular().getOrgaoUsuario().getIdOrgaoUsu());
        } catch (Exception e) {
            throw new ConfiguracaoGIControllerException(e);
        }
    }

    @Path("/pesquisar/{idOrgaoUsu}")
    public void pesquisar(Long idOrgaoUsu) throws ConfiguracaoGIControllerException {
        pesquisarPorOrgaoUsuario(idOrgaoUsu);
    }

    @SuppressWarnings("unchecked")
    private void pesquisarPorOrgaoUsuario(Long idOrgaoUsu) throws ConfiguracaoGIControllerException {
        try {
            CpOrgaoUsuario cpOrgaoUsuario = CpOrgaoUsuario.AR.findById(idOrgaoUsu);
            List<CpOrgaoUsuario> cpOrgaoUsuarios = CpOrgaoUsuario.AR.findAll();
            String servicoComplexoAdministrador = "SIGA-TP-ADMMISSAOCOMPLEXO";
            CpServico cpServico = CpServico.AR.find("siglaServico", servicoComplexoAdministrador).first();
            Long tipoConfigComplexoPadrao = 400L;
            CpTipoConfiguracao tpConf = CpTipoConfiguracao.AR.findById(tipoConfigComplexoPadrao);
            Object[] parametros = { idOrgaoUsu, cpServico };
            List<CpConfiguracao> cpConfiguracoesCp = CpConfiguracao.AR.find("(dpPessoa in (select d from DpPessoa d where d.orgaoUsuario.idOrgaoUsu = ?) and cpServico = ? and hisIdcFim is null )",
                    parametros).fetch();
            // Recuperando configuracao pode para uma lotacao especifica
            Object[] parametros1 = { idOrgaoUsu, tpConf };
            List<CpConfiguracao> cpConfiguracoesCl = CpConfiguracao.AR.find("((lotacao is not null) and orgaoUsuario.idOrgaoUsu = ?  and cpTipoConfiguracao = ? and hisIdcFim is null  )", parametros1)
                    .fetch();
            // Recuperando configuracao default para um  orgao especifico
            Object[] parametros2 = { idOrgaoUsu, tpConf };
            List<CpConfiguracao> cpConfiguracoesCo = CpConfiguracao.AR.find("( lotacao is null and orgaoUsuario.idOrgaoUsu = ?  and cpTipoConfiguracao = ? and hisIdcFim is null )", parametros2)
                    .fetch();
            List<CpConfiguracao> cpConfiguracoes = new ArrayList<CpConfiguracao>();
            cpConfiguracoes.addAll(cpConfiguracoesCp);
            cpConfiguracoes.addAll(cpConfiguracoesCl);
            cpConfiguracoes.addAll(cpConfiguracoesCo);

            result.include(CP_CONFIGURACOES, cpConfiguracoes);
            result.include(CP_ORGAO_USUARIO, cpOrgaoUsuario);
            result.include(CP_ORGAO_USUARIOS, cpOrgaoUsuarios);

            result.include("lotacaoSel", new DpLotacaoSelecao());
        } catch (Exception e) {
            throw new ConfiguracaoGIControllerException(e);
        }
    }

    @Path("/incluir/{idOrgaoUsu}")
    public void incluir(Long idOrgaoUsu) throws ConfiguracaoGIControllerException {
        CpConfiguracao cpConfiguracao = new CpConfiguracao();
        carregarDadosPerifericos(idOrgaoUsu);
        cpConfiguracao.setOrgaoUsuario((CpOrgaoUsuario) result.included().get(CP_ORGAO_USUARIO));

        /*
         * insert into corporativo.cp_configuracao ( id_configuracao, his_id_ini, id_tp_configuracao, id_sit_configuracao, id_servico, dt_ini_vig_configuracao, his_idc_ini,id_pessoa, id_complexo )
         * values( corporativo.cp_configuracao_seq.nextval, corporativo.cp_configuracao_seq.currval, 200, 1, (select id_servico from corporativo.cp_servico where
         * sigla_servico='SIGA-TP-ADMMISSAOCOMPLEXO'), sysdate, 13332, (select id_pessoa from corporativo.dp_pessoa where matricula = '10596' and data_fim_pessoa is null and id_orgao_usu = 1), 1 );
         */
        cpConfiguracao.setId(0L);
        result.include(CP_CONFIGURACAO, cpConfiguracao);
    }

    @Path("/editar/{id}")
    public void editar(Long id) throws ConfiguracaoGIControllerException {
        try {
            CpConfiguracao cpConfiguracao = CpConfiguracao.AR.findById(id);

            if (cpConfiguracao.getOrgaoUsuario() != null)
                carregarDadosPerifericos(cpConfiguracao.getOrgaoUsuario().getIdOrgaoUsu());
            else if (cpConfiguracao.getDpPessoa() != null) {
                carregarDadosPerifericos(cpConfiguracao.getDpPessoa().getLotacao().getOrgaoUsuario().getIdOrgaoUsu());
                cpConfiguracao.setOrgaoUsuario(cpConfiguracao.getDpPessoa().getLotacao().getOrgaoUsuario());
            } else if (cpConfiguracao.getLotacao() != null) {
                carregarDadosPerifericos(cpConfiguracao.getLotacao().getOrgaoUsuario().getIdOrgaoUsu());
                cpConfiguracao.setOrgaoUsuario(cpConfiguracao.getLotacao().getOrgaoUsuario());
            }

            result.include(CP_CONFIGURACAO, cpConfiguracao);
        } catch (Exception e) {
            throw new ConfiguracaoGIControllerException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarDadosPerifericos(Long idOrgaoUsu) throws ConfiguracaoGIControllerException {
        try {
            CpOrgaoUsuario cpOrgaoUsuario = CpOrgaoUsuario.AR.findById(idOrgaoUsu);
            long tipoConfigComplexoPadrao = 400;
            CpTipoConfiguracao tpConf1 = CpTipoConfiguracao.AR.findById(tipoConfigComplexoPadrao);
            long tipoConfigUtilizarServico = 200;
            CpTipoConfiguracao tpConf2 = CpTipoConfiguracao.AR.findById(tipoConfigUtilizarServico);

            List<CpTipoConfiguracao> cpTiposConfiguracao = new ArrayList<CpTipoConfiguracao>();
            cpTiposConfiguracao.add(tpConf2);
            cpTiposConfiguracao.add(tpConf1);

            List<CpSituacaoConfiguracao> cpSituacoesConfiguracao = CpSituacaoConfiguracao.AR.findAll();
            List<CpComplexo> cpComplexos = CpComplexo.AR.find(" orgaoUsuario.idOrgaoUsu = ? ", idOrgaoUsu).fetch();

            result.include(CP_ORGAO_USUARIO, cpOrgaoUsuario);
            result.include(CP_TIPOS_CONFIGURACAO, cpTiposConfiguracao);
            result.include(CP_SITUACOES_CONFIGURACAO, cpSituacoesConfiguracao);
            result.include(CP_COMPLEXOS, cpComplexos);
        } catch (Exception e) {
            throw new ConfiguracaoGIControllerException(e);
        }
    }

    @Path("/excluir/{id}")
    public void excluir(Long id) throws ConfiguracaoGIControllerException {
        try {
            CpConfiguracao cpConfiguracao = CpConfiguracao.AR.findById(id);
            cpConfiguracao.delete();
            redirecionaParaListagem(cpConfiguracao);
        } catch (Exception e) {
            throw new ConfiguracaoGIControllerException(e);
        }
    }

    public void salvar(CpConfiguracao cpConfiguracao) throws ConfiguracaoGIControllerException {
        try {
            isValid(cpConfiguracao);
            CpConfiguracao cpConfiguracaoNova = new CpConfiguracao();
            CpConfiguracao cpConfiguracaoAnterior = CpConfiguracao.AR.findById(cpConfiguracao.getId());
            if (cpConfiguracaoAnterior != null) {
                if (cpConfiguracaoAnterior.getConfiguracaoInicial() == null) {
                    cpConfiguracaoAnterior.setConfiguracaoInicial(cpConfiguracaoAnterior);
                    cpConfiguracaoAnterior.save();
                }
                cpConfiguracaoAnterior.setHisDtFim(new Date());
                cpConfiguracaoAnterior.setHisIdcFim(getIdentidadeCadastrante());
                cpConfiguracaoAnterior.save();

                cpConfiguracaoNova.setConfiguracaoInicial(cpConfiguracaoAnterior.getConfiguracaoInicial());
            }

            if (cpConfiguracao.getCpTipoConfiguracao().getIdTpConfiguracao() == 200) {
                String servicoComplexoAdminstrador = "SIGA-TP-ADMMISSAOCOMPLEXO";
                cpConfiguracaoNova.setCpServico((CpServico) CpServico.AR.find("siglaServico", servicoComplexoAdminstrador).first());
            }

            cpConfiguracaoNova.setCpSituacaoConfiguracao(cpConfiguracao.getCpSituacaoConfiguracao());
            cpConfiguracaoNova.setCpTipoConfiguracao(cpConfiguracao.getCpTipoConfiguracao());
            cpConfiguracaoNova.setComplexo(cpConfiguracao.getComplexo());
            cpConfiguracaoNova.setOrgaoUsuario(cpConfiguracao.getOrgaoUsuario());
            cpConfiguracaoNova.setLotacao(cpConfiguracao.getLotacao());
            cpConfiguracaoNova.setDpPessoa(cpConfiguracao.getDpPessoa());
            cpConfiguracaoNova.setHisDtIni(new Date());
            cpConfiguracaoNova.setHisIdcIni(getIdentidadeCadastrante());
            cpConfiguracaoNova.save();
            if (cpConfiguracaoNova.getConfiguracaoInicial() == null) {
                cpConfiguracaoNova.setConfiguracaoInicial(cpConfiguracaoNova);
                cpConfiguracaoNova.save();
            }
            redirecionaParaListagem(cpConfiguracaoNova);
        } catch (Exception e) {
            throw new ConfiguracaoGIControllerException(e);
        }
    }

    private void isValid(CpConfiguracao cpConfiguracao) throws ConfiguracaoGIControllerException {
        validaCamposNulos(cpConfiguracao);

        if (cpConfiguracao.getComplexo() == null || cpConfiguracao.getCpTipoConfiguracao() == null || cpConfiguracao.getCpSituacaoConfiguracao() == null)
            validator.add(new I18nMessage("configuracaoGI", "views.erro.campoObrigatorio"));

        if (validator.hasErrors()) {
            carregarDadosPerifericos(cpConfiguracao.getOrgaoUsuario().getIdOrgaoUsu());
            validator.onErrorUse(Results.page()).of(ConfiguracaoGIController.class).editar(cpConfiguracao.getOrgaoUsuario().getIdOrgaoUsu());
        }
    }

    private void validaCamposNulos(CpConfiguracao cpConfiguracao) {
        if (cpConfiguracao.getComplexo().getIdComplexo() == null)
            cpConfiguracao.setComplexo(null);
        if (cpConfiguracao.getCpSituacaoConfiguracao().getIdSitConfiguracao() == null)
            cpConfiguracao.setCpSituacaoConfiguracao(null);
        if (cpConfiguracao.getCpTipoConfiguracao().getIdTpConfiguracao() == null)
            cpConfiguracao.setCpTipoConfiguracao(null);
        if (cpConfiguracao.getLotacao().getIdeLotacao() == null)
            cpConfiguracao.setLotacao(null);
        if (cpConfiguracao.getDpPessoa().getIdePessoa() == null)
            cpConfiguracao.setDpPessoa(null);
    }

    private void redirecionaParaListagem(CpConfiguracao cpConfiguracao) throws ConfiguracaoGIControllerException {
        if (cpConfiguracao.getOrgaoUsuario() != null)
            result.redirectTo(this).pesquisar(cpConfiguracao.getOrgaoUsuario().getIdOrgaoUsu());
        else if (cpConfiguracao.getDpPessoa() != null)
            result.redirectTo(this).pesquisar(cpConfiguracao.getDpPessoa().getLotacao().getOrgaoUsuario().getIdOrgaoUsu());
        else if (cpConfiguracao.getLotacao() != null)
            result.redirectTo(this).pesquisar(cpConfiguracao.getLotacao().getOrgaoUsuario().getIdOrgaoUsu());
    }

}
