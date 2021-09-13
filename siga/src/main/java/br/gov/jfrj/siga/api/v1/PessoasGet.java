package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.Cargo;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.FuncaoConfianca;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.IPessoasGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Lotacao;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Orgao;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Pessoa;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;

public class PessoasGet implements IPessoasGet {
	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		if (((req.cpf != null ? 1 : 0) + (req.texto != null ? 1 : 0) + (req.idPessoaIni != null ? 1 : 0)) > 1) {
			throw new AplicacaoException("Pesquisa permitida somente por um dos argumentos.");
		}

		if (req.cpf != null && !req.cpf.isEmpty()) {
			resp.list = pesquisarPorCpf(req, resp);
			return;
		}

		if (req.texto != null && !req.texto.isEmpty()) {
			resp.list = pesquisarPorTexto(req, resp);
			return;
		}

		if (req.idPessoaIni != null && !req.idPessoaIni.isEmpty()) {
			resp.list = pesquisarPessoaAtualPorIdIni(req, resp);
			return;
		}

		throw new AplicacaoException("Não foi fornecido nenhum parâmetro.");
	}

	private List<Pessoa> pesquisarPessoaAtualPorIdIni(Request req, Response resp) throws SwaggerException {
		try {
			// TODO: ver se precisa de outros parametros listarPessoa
			List<Pessoa> resultado = new ArrayList<>();
			DpPessoa pessoa = new DpPessoa();
			pessoa.setIdPessoaIni(Long.valueOf(req.idPessoaIni));

			DpPessoa pes = CpDao.getInstance().obterPessoaAtual(pessoa);

			resultado.add(pessoaToResultadoPesquisa(pes));
			return resultado;

		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}

	private List<Pessoa> pesquisarPorTexto(Request req, Response resp) throws SwaggerException {
		final DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
		flt.setNome(Texto.removeAcentoMaiusculas(req.texto));
		List<DpPessoa> l = CpDao.getInstance().consultarPorFiltro(flt);
		if (l.isEmpty())
			throw new SwaggerException("Nenhuma pessoa foi encontrada contendo o texto informado.", 404, null, req,
					resp, null);

		return l.stream().map(this::pessoaToResultadoPesquisa).collect(Collectors.toList());
	}

	private List<Pessoa> pesquisarPorCpf(Request req, Response resp) throws Exception {
		String cpf = req.cpf;
		List<Pessoa> resultado = new ArrayList<>();
		try {
			if (Pattern.matches("\\d+", cpf) && cpf.length() == 11) {
				List<CpIdentidade> lista = new CpDao().consultaIdentidadesCadastrante(cpf, Boolean.TRUE);
				if (!lista.isEmpty()) {
					for (CpIdentidade ident : lista) {
						resultado.add(identidadeToResultadoPesquisa(ident));
					}
				} else {
					throw new SwaggerException("Nenhuma pessoa foi encontrada para o CPF informado.", 404, null, req,
							resp, null);
				}
			} else {
				throw new SwaggerException("CPF inválido.", 400, null, req, resp, null);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
		return resultado;
	}

	private Pessoa identidadeToResultadoPesquisa(CpIdentidade identidade) {
		DpPessoa p = identidade.getPessoaAtual();
		return pessoaToResultadoPesquisa(p);
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
		lotacao.idLotacaoIni = l.getIdLotacaoIni().toString();
		lotacao.nome = l.getNomeLotacao();
		lotacao.sigla = l.getSigla();
		lotacao.orgao = orgao;

		// Cargo Pessoa
		DpCargo c = p.getCargo();
		if (c != null) {
			cargo.idCargo = c.getId().toString();
			cargo.idCargoIni = c.getIdInicial().toString();
			cargo.nome = c.getNomeCargo();
		}
		// Função Pessoa
		DpFuncaoConfianca f = p.getFuncaoConfianca();
		if (f != null) {
			funcao.idFuncaoConfianca = f.getId().toString();
			funcao.idFuncaoConfiancaIni = f.getIdInicial().toString();
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
