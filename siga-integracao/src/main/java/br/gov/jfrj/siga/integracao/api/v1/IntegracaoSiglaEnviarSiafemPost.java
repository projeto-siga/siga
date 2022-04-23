package br.gov.jfrj.siga.integracao.api.v1;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeEnviarSiafem;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.integracao.api.v1.IIntegracaoApiV1.IIntegracaoSiglaEnviarSiafemPost;
import br.gov.jfrj.siga.integracao.ws.siafem.ServicoSiafemWs;
import br.gov.jfrj.siga.integracao.ws.siafem.SiafDoc;
import br.gov.jfrj.siga.vraptor.Transacional;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Transacional
public class IntegracaoSiglaEnviarSiafemPost implements IIntegracaoSiglaEnviarSiafemPost {

    public void run(Request req, Response resp, IntegracaoApiV1Context ctx) throws Exception {
        String usuarioSiafem = req.usuarioSiafem;
        String senhaSiafem = req.senhaSiafem;

        DpPessoa cadastrante = ctx.getCadastrante();
        DpPessoa titular = cadastrante;
        DpLotacao lotaTitular = titular.getLotacao();

        ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a ser enviado ao SIAFEM");

        Ex.getInstance().getComp()
                .afirmar(
                        "O documento " + mob.getSigla() + " não pode ser enviado ao SIAFEM por "
                                + titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta(),
                        ExPodeEnviarSiafem.class, titular, lotaTitular, mob.doc());

        enviarSiafem(usuarioSiafem, senhaSiafem, mob.doc());

        resp.sigla = mob.doc().getCodigo();

        resp.status = "OK";
    }


    protected ExDao dao() {
        return ExDao.getInstance();
    }

    @Override
    public String getContext() {
        return "Enviar ao SIAFEM";
    }
    
    
    private void enviarSiafem(String usuarioSiafem, String senhaSiafem, ExDocumento exDoc) {
        ExDocumento formulario = obterFormularioSiafem(exDoc);

        if(formulario == null)
            throw new AplicacaoException("Favor preencher o \"" + Prop.get("ws.siafem.nome.modelo") + ".");

        Map<String, String> form = new TreeMap<String, String>();
        Utils.mapFromUrlEncodedForm(form, formulario.getConteudoBlobForm());
        SiafDoc siafDoc = new SiafDoc(form);

        siafDoc.setCodSemPapel(exDoc.getExMobilPai().doc().getSigla().replaceAll("[-/]", ""));

        ServicoSiafemWs.enviarDocumento(usuarioSiafem, senhaSiafem, siafDoc);
    }

    public ExDocumento obterFormularioSiafem(ExDocumento doc) {
        String modeloSiafem = Prop.get("ws.siafem.nome.modelo");//"Formulario Integracao Siafem";

        if(modeloSiafem == null)
            return null;

        if(doc.getNmMod().equals(modeloSiafem))
            return doc;

        ExMobil mDefault = doc.getMobilDefaultParaReceberJuntada();

        if(mDefault == null)
            return null;

        Set<ExMobil> mobilsJuntados = mDefault.getJuntados();

        for (ExMobil exMobil : mobilsJuntados) {
            if(!exMobil.isCancelada() && modeloSiafem.contains(exMobil.getDoc().getNmMod())) {
                return exMobil.getDoc();
            }
        }

        return null;
    }

}
