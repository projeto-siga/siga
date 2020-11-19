package br.gov.jfrj.siga.api.v1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerAuthorizationException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.ILotacoesIdLotacaoIniMarcadoresGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.LotacoesIdLotacaoIniMarcadoresGetRequest;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.LotacoesIdLotacaoIniMarcadoresGetResponse;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Marcador;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpMarcadorCoresEnum;
import br.gov.jfrj.siga.cp.CpMarcadorTipoAplicacaoEnum;
import br.gov.jfrj.siga.cp.CpMarcadorTipoDataEnum;
import br.gov.jfrj.siga.cp.CpMarcadorTipoExibicaoEnum;
import br.gov.jfrj.siga.cp.CpMarcadorTipoInteressadoEnum;
import br.gov.jfrj.siga.cp.CpMarcadorTipoTextoEnum;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.CpTipoMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpLotacaoDaoFiltro;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@AcessoPublicoEPrivado
public class LotacoesIdLotacaoIniMarcadoresGet implements ILotacoesIdLotacaoIniMarcadoresGet {

	@Override
	public void run(LotacoesIdLotacaoIniMarcadoresGetRequest req, LotacoesIdLotacaoIniMarcadoresGetResponse resp)
			throws Exception {
		try (ApiContext ctx = new ApiContext(false)) {
			String usuario = ContextoPersistencia.getUserPrincipal();

			if (usuario == null)
				throw new SwaggerAuthorizationException("Usuário não está logado");

			HttpServletRequest request = SwaggerServlet.getHttpServletRequest();
			SigaObjects so = new SigaObjects(request);
			DpPessoa titular = so.getTitular();
			DpLotacao lotaTitular = so.getLotaTitular();
			DpLotacaoDaoFiltro lotacao = new DpLotacaoDaoFiltro();
			Long idOrgaoUsu = titular.getOrgaoUsuario().getId();
//			lotacao.setNome("");
//			lotacao.setIdOrgaoUsu(idOrgaoUsu);
//			List<DpLotacao> listaLotacao = new ArrayList<DpLotacao>();
//			DpLotacao l = new DpLotacao();
//			l.setNomeLotacao("Selecione");
//			l.setId(0L);
//			l.setSiglaLotacao("");
//			CpOrgaoUsuario cpOrgaoUsuario = new CpOrgaoUsuario();
//			cpOrgaoUsuario.setIdOrgaoUsu(0L);
//			cpOrgaoUsuario.setSiglaOrgaoUsu("");
//			l.setOrgaoUsuario(cpOrgaoUsuario);
//			listaLotacao.add(l);
//			if (idOrgaoUsu != null && idOrgaoUsu != 0)
//				listaLotacao.addAll(CpDao.getInstance().consultarPorFiltro(lotacao));


//			result.include("listaTipoExibicao", listaTipoMarcadorEnum);
//			String[] listaCores = (String[]) EnumUtils.getEnumMap(CpMarcador.CoresMarcadorEnum.class).keySet().toArray();
//			for (CoresMarcadorEnum item : CpMarcador.CoresMarcadorEnum.values()){
//			    listaCores.add(item.name().replace("COR_", ""));
//			}
//			resp.include("listaTipoMarcador", CpDao.listarTodos(CpTipoMarcador.class, null));
//			resp.include("listaLotacao", listaLotacao);
//			resp.include("listaCores", CpMarcadorCoresEnum.getList());
//			resp.include("listaTipoAplicacao", CpMarcadorTipoAplicacaoEnum.values());
//			resp.include("listaTipoExibicao", CpMarcadorTipoExibicaoEnum.values());
//			resp.include("listaTipoDataPlanejada", CpMarcadorTipoDataEnum.values());
//			resp.include("listaTipoDataLimite", CpMarcadorTipoDataEnum.values());
//			resp.include("listaTipoJustificativa", CpMarcadorTipoJustificativaEnum.values());
//			resp.include("listaTipoInteressado", CpMarcadorTipoInteressadoEnum.values());
			
			List<CpMarcador> marcadores = CpDao.getInstance()
					.listarCpMarcadoresPorLotacaoESublotacoes(lotaTitular, true);

			if (marcadores != null) {
				resp.list = new ArrayList<>();
				for (CpMarcador m : marcadores) {
					Marcador mr = new Marcador();
					mr.idMarcador = m.getIdMarcador().toString();
					mr.descricao = m.getDescrMarcador();
					mr.descricaoDetalhada = m.getDescrDetalhada();
					resp.list.add(mr);
				}
			}

		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public String getContext() {
		return "obter marcadores da lotacao";
	}

}