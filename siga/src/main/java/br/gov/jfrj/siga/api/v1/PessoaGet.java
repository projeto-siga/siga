package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.Cargo;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.FuncaoConfianca;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.IPessoaGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Lotacao;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Orgao;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Pessoa;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PessoaGetRequest;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PessoaGetResponse;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;

public class PessoaGet implements IPessoaGet {

	private Pessoa pessoaToResultadoPesquisa(DpPessoa p) {
		Pessoa pes = new Pessoa();

		pes.nome = p.getNomePessoa();
		pes.sigla = p.getSigla();
		pes.siglaLotacao = Objects.isNull(p.getLotacao()) ? null : p.getLotacao().getSigla();

		return pes;
	}

	@Override
	public void run(PessoaGetRequest req, PessoaGetResponse resp) throws Exception {
		CurrentRequest.set(
				new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
		SwaggerHelper.buscarEValidarUsuarioLogado();

		if (req.cpf != null && !req.cpf.isEmpty()) {
			String cpf = req.cpf;
			List<Pessoa> resultado = new ArrayList<>();
			try {
				if (Pattern.matches("\\d+", cpf) && cpf.length() == 11) {
					List<CpIdentidade> lista = new CpDao().consultaIdentidadesCadastrante(cpf, Boolean.TRUE);
					if (!lista.isEmpty()) {
						for (CpIdentidade ident : lista) {
							resultado.add(criaItem(ident));
						}
					} else {
						throw new SwaggerException("Nenhuma pessoa foi encontrada para o CPF informado.",
								404, null, req, resp, null);
					}
				} else {
					throw new SwaggerException("CPF inválido.",
							400, null, req, resp, null);
				}
			} catch (Exception e) {
				e.printStackTrace(System.out);
				throw e;
			}
			resp.list = resultado;
		} else {
			if (req.texto != null && !req.texto.isEmpty()) {
				final DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
				flt.setNome(Texto.removeAcentoMaiusculas(req.texto));
				// TODO: ver se precisa de outros parametros listarPessoa
				List<DpPessoa> l = CpDao.getInstance().consultarPorFiltro(flt);
				if (l.isEmpty())
					throw new SwaggerException("Nenhuma pessoa foi encontrada contendo o texto informado.",
							404, null, req, resp, null);
		
				resp.list = l.stream().map(this::pessoaToResultadoPesquisa).collect(Collectors.toList());
			} else {
				throw new AplicacaoException("Não foi fornecido nenhum parâmetro.");
			}
		}
	}

	private Pessoa criaItem(CpIdentidade identidade) {
		Pessoa pessoa = new Pessoa();
		Orgao orgao = new Orgao();
		Lotacao lotacao = new Lotacao();
		Cargo cargo = new Cargo();
		FuncaoConfianca funcao = new FuncaoConfianca();
		// Pessoa
		DpPessoa p = identidade.getPessoaAtual();
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
