package br.gov.jfrj.siga.ex.api.v1;

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

        Ex.getInstance().getComp().afirmar("O documento " + mob.getSigla() + " não pode ser reclassificado por " + titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta(), ExPodeReclassificar.class, titular, lotaTitular, mob);

        final String motivo = req.motivo;
        final String dtMovString = req.data1;
        final DpPessoaSelecao responsavel = getPessoaSelecaoPorSigla(req.matriculaResponsavel);
        final ExClassificacaoSelecao classificacaoSelecao = getClassificacaoSelecaoPorSigla(req.novaClassificacao);

        final ExMovimentacao mov = ExMovimentacaoBuilder.novaInstancia().setDescrMov(motivo)
                .setDtMovString(dtMovString).setCadastrante(cadastrante).setLotaTitular(lotaTitular)
                .setSubscritorSel(responsavel).setClassificacaoSel(classificacaoSelecao)
                .construir(dao());
        mov.setDtIniMov(dao().consultarDataEHoraDoServidor());

        Ex.getInstance().getBL().avaliarReclassificar(cadastrante, lotaCadastrante, mob, mov.getDtMov(),
                mov.getSubscritor(), mov.getExClassificacao(), mov.getDescrMov(), false);

        resp.sigla = mob.doc().getCodigo();
        resp.status = "OK";
    }

    @Override
    public String getContext() {
        return "Reclassificar";
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

    private DpPessoaSelecao getPessoaSelecaoPorSigla(String sigla) {
        DpPessoaSelecao dpPessoaSelecao = null;
        if (StringUtils.isNotEmpty(sigla)) {
            dpPessoaSelecao = new DpPessoaSelecao();
            dpPessoaSelecao.setSigla(sigla);
            dpPessoaSelecao.buscarPorSigla();
        }
        return dpPessoaSelecao;
    }

    private ExDao dao() {
        return ExDao.getInstance();
    }
}
