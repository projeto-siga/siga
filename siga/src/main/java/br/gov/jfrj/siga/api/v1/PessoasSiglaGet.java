package br.gov.jfrj.siga.api.v1;

import org.apache.commons.lang3.StringUtils;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IPessoasSiglaGet;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class PessoasSiglaGet implements IPessoasSiglaGet {
    @Override
    public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
        if (StringUtils.isEmpty(req.sigla))
            throw new SwaggerException("O parâmetro sigla é obrigatório.", 400, null, req, resp, null);

        final DpPessoa flt = new DpPessoa();
        flt.setSigla(req.sigla.toUpperCase());
        DpPessoa pes = CpDao.getInstance().consultarPorSigla(flt);
        if (pes == null)
            throw new SwaggerException("Nenhuma pessoa foi encontrada contendo a sigla informada.", 404, null, req,
                    resp, null);

        Boolean exibirDadosSensiveis = PessoasGet.exibirDadosSensíveis(ctx);
        if (!exibirDadosSensiveis && Utils.comparar(pes.getCpfFormatado(), ctx.getTitular().getCpfFormatado()) == 0)
            exibirDadosSensiveis = true;

        resp.pessoa = PessoasGet.pessoaToResultadoPesquisa(pes, exibirDadosSensiveis);
    }

    @Override
    public String getContext() {
        return "selecionar pessoas";
    }
}
