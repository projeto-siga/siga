package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
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
        DpPessoa titular = cadastrante;
        DpLotacao lotaCadastrante = cadastrante.getLotacao();
        DpLotacao lotaTitular = ctx.getLotaTitular();

        ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Reclassificar");

        ctx.assertAcesso(mob, titular, lotaTitular);

        Ex.getInstance().getComp().afirmar("O documento " + mob.getSigla() + " não pode ser reclassificado por " 
                + titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta(), 
                ExPodeReclassificar.class, titular, lotaTitular, mob);

        final ExClassificacaoSelecao classificacaoSelecao =
                validarEBuscarClassificacao(mob.doc().getExClassificacaoAtual().getSigla(), req.novaClassificacao);
        
        final String motivo = req.motivo;
        final Date dataReclassificacao = getDataMovimentacao(req.dataMovimentacao);
        final DpPessoaSelecao responsavel = validarEBuscarResponsavel(req.matriculaResponsavel);

        final ExMovimentacao mov = ExMovimentacaoBuilder.novaInstancia()
                .setDescrMov(motivo)
                .setCadastrante(cadastrante)
                .setTitularSel(responsavel)
                .setLotaTitular(lotaTitular)
                .setResponsavelSel(responsavel)
                .setClassificacaoSel(classificacaoSelecao)
                .construir(dao());
        mov.setDtIniMov(dao().consultarDataEHoraDoServidor());
        mov.setDtMov(dataReclassificacao);
        
        Ex.getInstance().getBL().avaliarReclassificar(cadastrante, lotaCadastrante, mob, mov.getDtMov(),
                mov.getSubscritor(), mov.getExClassificacao(), mov.getDescrMov(), false);

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

    private DpPessoaSelecao validarEBuscarResponsavel(final String matricula){

        final DpPessoaSelecao dpPessoaSelecao = getPessoaSelecaoPorSigla(matricula);
        if (StringUtils.isNotEmpty(matricula) && 
                ( Objects.isNull(dpPessoaSelecao) || Objects.isNull(dpPessoaSelecao.getId()) )) {
            throw new AplicacaoException("Matrícula do responsável " + matricula + " inválida");
        }

        return dpPessoaSelecao;
    }
    
    private DpPessoaSelecao getPessoaSelecaoPorSigla(String sigla) {
        DpPessoaSelecao dpPessoaSelecao = null;
        if (StringUtils.isNotEmpty(sigla)) {
            dpPessoaSelecao = new DpPessoaSelecao();
            dpPessoaSelecao.setSigla(sigla);
            dpPessoaSelecao.buscarPorSigla();
        }
        return dpPessoaSelecao;
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
