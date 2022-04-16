package br.gov.jfrj.siga.integracao.service.impl;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.integracao.service.IntegracaoService;
import br.gov.jfrj.siga.integracao.ws.siafem.ServicoSiafemWs;
import br.gov.jfrj.siga.integracao.ws.siafem.SiafDoc;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import org.jboss.logging.Logger;

import javax.jws.WebService;
import java.util.Map;
import java.util.TreeMap;

@WebService(serviceName = "IntegracaoService", endpointInterface = "br.gov.jfrj.siga.integracao.service.IntegracaoService", targetNamespace = "http://impl.service.integracao.siga.jfrj.gov.br/")
public class IntegracaoServiceImpl implements IntegracaoService {
    private final static Logger log = Logger.getLogger(IntegracaoService.class);

    @Override
    public Boolean enviarSiafem(String usuarioSiafem, String senhaSiafem, String siglaDocumento) {
        ExMobilDaoFiltro exMobilDaoFiltro = new ExMobilDaoFiltro();
        exMobilDaoFiltro.setSigla(siglaDocumento);

        ExDocumento exDoc = ExDao.getInstance().consultarPorSigla(exMobilDaoFiltro).doc();
        ExDocumento formulario = Ex.getInstance().getBL().obterFormularioSiafem(exDoc);

        if (formulario == null)
            throw new AplicacaoException("Favor preencher o \"" + Prop.get("ws.siafem.nome.modelo") + ".");

        Map<String, String> form = new TreeMap<>();
        Utils.mapFromUrlEncodedForm(form, formulario.getConteudoBlobForm());
        SiafDoc siafDoc = new SiafDoc(form);

        siafDoc.setCodSemPapel(exDoc.getExMobilPai().doc().getSigla().replaceAll("[-/]", ""));

        return ServicoSiafemWs.enviarDocumento(usuarioSiafem, senhaSiafem, siafDoc);
    }


}
