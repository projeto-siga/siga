package br.gov.jfrj.siga.api.v1;

import org.apache.commons.lang3.StringUtils;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.Cargo;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.FuncaoConfianca;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.IPessoasSiglaGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Lotacao;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Orgao;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Pessoa;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
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

		resp.pessoa = pessoaToResultadoPesquisa(pes);
	}

	private Pessoa pessoaToResultadoPesquisa(DpPessoa p) {
		Pessoa pessoa = new Pessoa();
		Orgao orgao = new Orgao();
		Lotacao lotacao = new Lotacao();
		Cargo cargo = new Cargo();
		FuncaoConfianca funcao = new FuncaoConfianca();
		// Pessoa
		pessoa.idPessoaIni = p.getIdInicial().toString();
		pessoa.sigla = p.getSiglaCompleta();
		pessoa.nome = p.getNomePessoa();
		pessoa.isExternaPessoa = p.isUsuarioExterno();

		// Orgao Pessoa
		CpOrgaoUsuario o = p.getOrgaoUsuario();
		orgao.idOrgao = o.getId().toString();
		orgao.nome = o.getNmOrgaoAI();
		orgao.sigla = o.getSigla();

		// Lotacao Pessoa
		DpLotacao l = p.getLotacao();
		lotacao.idLotacao = l.getId().toString();
		lotacao.nome = l.getNomeLotacao();
		lotacao.sigla = l.getSigla();
		lotacao.orgao = orgao;

		// Cargo Pessoa
		DpCargo c = p.getCargo();
		if (c != null) {
			cargo.idCargo = c.getId().toString();
			cargo.nome = c.getNomeCargo();
		}
		// Função Pessoa
		DpFuncaoConfianca f = p.getFuncaoConfianca();
		if (f != null) {
			funcao.idFuncaoConfianca = f.getId().toString();
			funcao.nome = f.getNomeFuncao();
		}

		pessoa.lotacao = lotacao;
		pessoa.cargo = cargo;
		pessoa.funcaoConfianca = funcao;
		return pessoa;
	}

	@Override
	public String getContext() {
		return "selecionar pessoas";
	}
}
