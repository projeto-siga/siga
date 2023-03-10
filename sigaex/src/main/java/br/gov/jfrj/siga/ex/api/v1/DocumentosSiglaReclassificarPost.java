package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeReclassificar;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.ExClassificacaoSelecao;
import br.gov.jfrj.siga.vraptor.builder.ExMovimentacaoBuilder;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.vraptor.Transacional;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Objects;

@Transacional
public class DocumentosSiglaReclassificarPost implements IExApiV1.IDocumentosSiglaReclassificarPost {

    @Override
    public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
        DpPessoa cadastrante = ctx.getCadastrante();
        DpLotacao lotaCadastrante = cadastrante.getLotacao();

        ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Reclassificar");

        ctx.assertAcesso(mob, cadastrante, lotaCadastrante);

        Ex.getInstance().getComp().afirmar("O documento " + mob.getSigla() + " não pode ser reclassificado por " 
                + cadastrante.getSiglaCompleta() + "/" + lotaCadastrante.getSiglaCompleta(), 
                ExPodeReclassificar.class, cadastrante, lotaCadastrante, mob);

        final ExClassificacaoSelecao classificacaoSelecao =
                validarEBuscarClassificacao(mob.doc().getExClassificacaoAtual().getSigla(), req.novaClassificacao);
        
        final String motivo = req.motivo;
        final Date dataReclassificacao = getDataMovimentacao(req.data);
        final DpPessoa responsavel = validarEBuscarPessoaPorMatricula(req.responsavel);
        final DpPessoa titular = validarEBuscarPessoaPorMatricula(req.titular);

        Ex.getInstance().getBL().avaliarReclassificar(cadastrante, lotaCadastrante, mob, dataReclassificacao,
                responsavel, titular, classificacaoSelecao.buscarObjeto(), motivo, false);

        resp.sigla = mob.doc().getCodigo();
        resp.classificacao = mob.doc().getExClassificacaoAtual().getDescricaoCompleta();
        resp.status = "OK";
    }

    @Override
    public String getContext() {
        return "Reclassificar";
    }

    private ExClassificacaoSelecao validarEBuscarClassificacao(final String classificacaoAtual, 
                                                               final String novaClassificacao){
        if (StringUtils.isEmpty(novaClassificacao)) {
            throw new AplicacaoException("É necessário informar a nova classificação");
        }
        
        if (classificacaoAtual.equals(novaClassificacao)) {
            throw new AplicacaoException("Classificação " + novaClassificacao + " é a mesma da atual. " 
                    + "Selecione valores diferentes para a reclassificação");
        }

        final ExClassificacaoSelecao classificacaoSelecao = getClassificacaoSelecaoPorSigla(novaClassificacao);
        if(Objects.isNull(classificacaoSelecao) || Objects.isNull(classificacaoSelecao.getId())){
            throw new AplicacaoException("Classificação " + novaClassificacao + " inválida");
        }
        
        return classificacaoSelecao;
    }
    
    private ExClassificacaoSelecao getClassificacaoSelecaoPorSigla(String sigla) {
        ExClassificacaoSelecao exClassificacaoSelecao = null;
        if (StringUtils.isNotEmpty(sigla)) {
            exClassificacaoSelecao = new ExClassificacaoSelecao();
            exClassificacaoSelecao.setSigla(sigla);
            exClassificacaoSelecao.buscarPorSigla();
        }
        return exClassificacaoSelecao;
    }

    private DpPessoa validarEBuscarPessoaPorMatricula(final String matricula){

        final DpPessoa dpPessoaFlt = new DpPessoa();
        dpPessoaFlt.setSigla(matricula);
        
        final DpPessoa dpPessoa = CpDao.getInstance().consultarPorSigla(dpPessoaFlt);
        if (StringUtils.isNotEmpty(matricula) && 
                ( Objects.isNull(dpPessoa) || Objects.isNull(dpPessoa.getId()) )) {
            throw new AplicacaoException("Matrícula " + matricula + " inválida");
        }

        return dpPessoa;
    }

    private Date getDataMovimentacao(final String dataMovimentacaoString) throws AplicacaoException {
        if (StringUtils.isEmpty(dataMovimentacaoString)) {
            return null;
        }
        try {
            LocalDate localDate = LocalDate.parse(dataMovimentacaoString);
            if (localDate.isAfter(LocalDate.now())) {
                throw new AplicacaoException("Data de reclassificação " + dataMovimentacaoString
                        + " não pode ser maior que a data de hoje");
            }
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (DateTimeParseException e) {
            throw new AplicacaoException("Data de reclassificação inválida: " + dataMovimentacaoString);
        }
    }

    private ExDao dao() {
        return ExDao.getInstance();
    }
}
