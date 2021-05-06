package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.ILotacoesGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Lotacao;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.LotacoesGetRequest;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.LotacoesGetResponse;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Orgao;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.CpOrgaoUsuarioDaoFiltro;
import br.gov.jfrj.siga.dp.dao.DpLotacaoDaoFiltro;

public class LotacoesGet implements ILotacoesGet {
	@Override
	public void run(LotacoesGetRequest req, LotacoesGetResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(false, true)) {
			if ((((req.texto != null? 1:0) + (req.idLotacaoIni != null? 1:0) + (req.siglaOrgaoQuery != null? 1:0)) > 1)) {
				throw new AplicacaoException("Pesquisa permitida somente por um dos argumentos.");
			}
	
			if (req.siglaOrgaoQuery != null && !req.siglaOrgaoQuery.isEmpty()) {
				resp.list = pesquisarPorOrgao(req, resp);
				return;
			}
				
			if (req.texto != null && !req.texto.isEmpty()) {
				resp.list = pesquisarPorTexto(req, resp);
				return;
			}
				
			if (req.idLotacaoIni != null && !req.idLotacaoIni.isEmpty()) {
				resp.list = pesquisarLotacaoAtualPorIdIni(req, resp);
				return;
			}
	
			throw new AplicacaoException("Não foi fornecido nenhum parâmetro.");
		} catch (AplicacaoException | SwaggerException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}

	private List<Lotacao> pesquisarPorOrgao(LotacoesGetRequest req, LotacoesGetResponse resp) {
		final DpLotacaoDaoFiltro flt = new DpLotacaoDaoFiltro();
		final CpOrgaoUsuarioDaoFiltro fltOrg = new CpOrgaoUsuarioDaoFiltro();
		fltOrg.setSigla(req.siglaOrgaoQuery);
		CpOrgaoUsuario org = (CpOrgaoUsuario) CpDao.getInstance()
				.consultarPorSigla(fltOrg);
		if (org != null)
			flt.setIdOrgaoUsu(Long.valueOf(org.getId()));
		else
			throw new AplicacaoException("Órgão não encontrado.");
		
		List<DpLotacao> l; 
		l = CpDao.getInstance().consultarLotacaoPorOrgao(org);
		return l.stream().map(this::lotacaoToResultadoPesquisa).collect(Collectors.toList());
	}

	private List<Lotacao> pesquisarPorTexto(LotacoesGetRequest req, LotacoesGetResponse resp) {
		final DpLotacaoDaoFiltro flt = new DpLotacaoDaoFiltro();
		flt.setNome(Texto.removeAcentoMaiusculas(req.texto));
		List<DpLotacao> l; 
		l = CpDao.getInstance().consultarPorFiltro(flt);
		return l.stream().map(this::lotacaoToResultadoPesquisa).collect(Collectors.toList());
	}

	private List<Lotacao> pesquisarLotacaoAtualPorIdIni(LotacoesGetRequest req, LotacoesGetResponse resp) throws SwaggerException {
		try {
			DpLotacao Lotacao = new DpLotacao();
			Lotacao.setIdLotacaoIni(Long.valueOf(req.idLotacaoIni));
			DpLotacao lotacaoAtual = CpDao.getInstance().obterLotacaoAtual(Lotacao);
			List<Lotacao> l = new ArrayList<>();
			l.add(lotacaoToResultadoPesquisa(lotacaoAtual));
			return l;
		}catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}
	
	private Lotacao lotacaoToResultadoPesquisa(DpLotacao lota) {
		Lotacao rp = new Lotacao();
		Orgao orgao = new Orgao();

		rp.siglaLotacao = lota.getSigla();
		rp.idLotacao = lota.getId().toString();
		rp.idLotacaoIni = lota.getIdLotacaoIni().toString();
		rp.nome = lota.getNomeLotacao();
		rp.sigla = lota.getSiglaCompleta();
		// Orgao Pessoa
		CpOrgaoUsuario o = lota.getOrgaoUsuario();
		orgao.idOrgao = o.getId().toString();
		orgao.nome = o.getNmOrgaoAI();
		orgao.sigla = o.getSigla();
		
		rp.orgao = orgao;
		return rp;
	}

	@Override
	public String getContext() {
		return "selecionar lotações";
	}

}
