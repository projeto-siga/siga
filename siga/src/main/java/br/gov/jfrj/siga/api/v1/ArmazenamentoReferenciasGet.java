package br.gov.jfrj.siga.api.v1;

import java.util.List;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.ArmazenamentoRef;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.IArmazenamentoReferenciasGet;
import br.gov.jfrj.siga.cp.CpArquivo;
import br.gov.jfrj.siga.cp.CpArquivoTipoArmazenamentoEnum;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.CpDao.ResultadoDeEstatisticasParaMigracaoDeArmazenamento;

public class ArmazenamentoReferenciasGet implements IArmazenamentoReferenciasGet {

    @Override
    public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
        CpArquivoTipoArmazenamentoEnum origem = CpArquivoTipoArmazenamentoEnum.valueOf(req.origem);
        CpArquivoTipoArmazenamentoEnum destino = CpArquivoTipoArmazenamentoEnum.valueOf(req.destino);
        ResultadoDeEstatisticasParaMigracaoDeArmazenamento r = CpDao.getInstance()
                .estatisticasParaMigracaoDeArmazenamento(origem, destino);
        List<CpArquivo> l = CpDao.getInstance().consultarReferenciasParaMigracaoDeArmazenamento(origem, destino,
                req.quantidade.intValue());
        for (CpArquivo arq : l) {
            ArmazenamentoRef ref = new ArmazenamentoRef();
            ref.id = arq.getIdArq().toString();
            resp.list.add(ref);
        }
    }

    @Override
    public String getContext() {
        return "obter quadro quantitativo";
    }

}
