package br.gov.jfrj.siga.ex.api.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.ClassificacaoItem;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IClassificacoesGet;
import br.gov.jfrj.siga.hibernate.ExDao;

public class ClassificacoesGet implements IClassificacoesGet {
	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		if (((req.texto != null ? 1 : 0) + (req.idClassificacaoIni != null ? 1 : 0)) > 1) {
			throw new AplicacaoException("Pesquisa permitida somente por um dos argumentos.");
		}

		if (req.texto != null && !req.texto.isEmpty()) {
			resp.list = pesquisarPorTexto(req, resp);
			return;
		}

		if (req.idClassificacaoIni != null && !req.idClassificacaoIni.isEmpty()) {
			resp.list = pesquisarClassificacaoAtualPorIdIni(req, resp);
			return;
		}

		throw new AplicacaoException("Não foi fornecido nenhum parâmetro.");
	}

	private List<ClassificacaoItem> pesquisarClassificacaoAtualPorIdIni(Request req, Response resp)
			throws SwaggerException {
		List<ClassificacaoItem> resultado = new ArrayList<>();

		ExDao dao = ExDao.getInstance();
		ExClassificacao c = dao.consultar(Long.valueOf(req.idClassificacaoIni), ExClassificacao.class, false);
		ExClassificacao cIni = dao.obterAtual(c);

		resultado.add(classificacaoToResultadoPesquisa(cIni));
		return resultado;
	}

	private List<ClassificacaoItem> pesquisarPorTexto(Request req, Response resp) throws PresentableUnloggedException {
		String texto = Texto.removeAcentoMaiusculas(req.texto);
		ExClassificacao c = (ExClassificacao) ExDao.getInstance().consultarExClassificacao(texto);
		List<ExClassificacao> l = new ArrayList<>();
		if (c != null) {
			l.add(c);
		} else {
			l = ExDao.getInstance().consultarExClassificacao("__.__.__.__", texto);
		}
		if (l.isEmpty())
			throw new PresentableUnloggedException("Nenhuma classificacao foi encontrada contendo o texto informado.");
		return l.stream().map(this::classificacaoToResultadoPesquisa).collect(Collectors.toList());
	}

	private ClassificacaoItem classificacaoToResultadoPesquisa(ExClassificacao p) {
		ClassificacaoItem c = new ClassificacaoItem();
		c.sigla = p.getSigla();
		c.nome = p.getDescricao();
		c.idClassificacao = p.getId().toString();
		return c;
	}

	@Override
	public String getContext() {
		return "selecionar classificações";
	}
}
