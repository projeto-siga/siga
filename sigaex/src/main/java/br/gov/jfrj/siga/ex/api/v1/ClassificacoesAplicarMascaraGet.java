package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IClassificacoesAplicarMascaraGet;
import br.gov.jfrj.siga.ex.util.MascaraUtil;

public class ClassificacoesAplicarMascaraGet implements IClassificacoesAplicarMascaraGet {
    @Override
    public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
        resp.sigla = MascaraUtil.getInstance().formatar(req.sigla);
    }

    @Override
    public String getContext() {
        return "aplicando máscara de classificação documental";
    }
}
