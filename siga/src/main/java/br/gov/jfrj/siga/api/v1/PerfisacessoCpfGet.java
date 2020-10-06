package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.crivano.swaggerservlet.SwaggerException;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.CargoPerfil;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.FuncaoConfiancaPerfil;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.IPerfisacessoCpfGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.LotacaoPerfil;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.OrgaoPerfil;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PerfilAcesso;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PerfisacessoCpfGetRequest;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.PerfisacessoCpfGetResponse;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class PerfisacessoCpfGet implements IPerfisacessoCpfGet {

	@Override
	public void run(PerfisacessoCpfGetRequest req,
			PerfisacessoCpfGetResponse resp) throws Exception {
		String cpf = req.cpf;
		List<PerfilAcesso> resultado = new ArrayList<>();
		try {
			if (Pattern.matches("\\d+", cpf) && cpf.length() == 11) {
				List<CpIdentidade> lista = new CpDao().consultaIdentidadesCadastrante(cpf, Boolean.TRUE);
				if (!lista.isEmpty()) {
					for (CpIdentidade ident : lista) {
						resultado.add(criaItem(ident));
					}
				} else {
					throw new SwaggerException("Não foi possível buscar acessos. Acessos não localizados.",
							404, null, req, resp, null);
				}
			} else {
				throw new SwaggerException("Não foi possível buscar acessos. CPF inválido.",
						400, null, req, resp, null);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
		resp.lista = resultado;
	}

	private PerfilAcesso criaItem(CpIdentidade identidade) {
		PerfilAcesso perfil = new PerfilAcesso();
		OrgaoPerfil orgao = new OrgaoPerfil();
		LotacaoPerfil lotacao = new LotacaoPerfil();
		CargoPerfil cargo = new CargoPerfil();
		FuncaoConfiancaPerfil funcao = new FuncaoConfiancaPerfil();
		// Pessoa
		DpPessoa p = identidade.getPessoaAtual();
		perfil.siglaPessoa = p.getSiglaCompleta();
		perfil.nomePessoa = p.getNomePessoa();
		perfil.isExternaPessoa = p.isUsuarioExterno();
		
		// Orgao Pessoa
		CpOrgaoUsuario o = p.getOrgaoUsuario();
		orgao.id = o.getId().toString();
		orgao.nome = o.getNmOrgaoAI();
		orgao.sigla = o.getSigla();

		// Lotacao Pessoa
		DpLotacao l = p.getLotacao();
		lotacao.id = l.getId().toString();
		lotacao.nome = l.getNomeLotacao();
		lotacao.sigla = l.getSigla();

		// Cargo Pessoa
		DpCargo c = p.getCargo();
		if (c != null) {
			cargo.id = c.getId().toString();
			cargo.nome = c.getNomeCargo();
		}
		// Função Pessoa
		DpFuncaoConfianca f = p.getFuncaoConfianca();
		if (f != null) {
			funcao.id = f.getId().toString();
			funcao.nome = f.getNomeFuncao();
		}

		perfil.orgao = orgao;
		perfil.lotacao = lotacao;
		perfil.cargo = cargo;
		perfil.funcaoConfianca = funcao;
		return perfil;
	}

	@Override
	public String getContext() {
		return "Listar perfis de acesso por cpf";
	}
}