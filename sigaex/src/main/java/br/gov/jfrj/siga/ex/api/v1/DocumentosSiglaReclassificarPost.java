package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeReclassificar;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.Selecao;
import br.gov.jfrj.siga.model.SelecaoGenerica;
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

        Ex.getInstance().getComp().afirmar("O documento " + mob.getSigla() + " não pode ser reclassificado por " 
                + titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta(), 
                ExPodeReclassificar.class, titular, lotaTitular, mob);
        
        final String motivo = req.motivo;
        final String dtMovString = req.data;
        final DpPessoaSelecao responsavel = (DpPessoaSelecao) getSelecaoPorSigla(req.matriculaResponsavel);
        final ExClassificacaoSelecao classificacaoSelecao = (ExClassificacaoSelecao) getSelecaoPorSigla(req.novaClassificacao);
                
        final ExMovimentacao mov = ExMovimentacaoBuilder.novaInstancia()
                .setDescrMov(motivo).setDtMovString(dtMovString)
                .setCadastrante(cadastrante).setLotaTitular(lotaTitular)
                .setSubscritorSel(responsavel).setClassificacaoSel(classificacaoSelecao).construir(dao());
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

    private Selecao getSelecaoPorSigla(String sigla) {
        Selecao selecao = null;
        if(StringUtils.isNotEmpty(sigla)){
            selecao = new SelecaoGenerica();
            selecao.setSigla(sigla);
            selecao.buscarPorSigla();
        }
        return selecao;
    }
    
    private ExDao dao(){
        return ExDao.getInstance();
    }
}
