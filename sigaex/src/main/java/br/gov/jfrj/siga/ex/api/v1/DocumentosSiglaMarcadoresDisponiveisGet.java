package br.gov.jfrj.siga.ex.api.v1;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.AcaoVO;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaMarcadoresDisponiveisGetRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaMarcadoresDisponiveisGetResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaMarcadoresDisponiveisGet;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.Marcador;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.logic.ExPodeMarcarComMarcador;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@AcessoPublicoEPrivado
public class DocumentosSiglaMarcadoresDisponiveisGet implements IDocumentosSiglaMarcadoresDisponiveisGet {

	@Override
	public void run(DocumentosSiglaMarcadoresDisponiveisGetRequest req, DocumentosSiglaMarcadoresDisponiveisGetResponse resp)
			throws Exception {
		try (ApiContext ctx = new ApiContext(false, true)) {
			ApiContext.assertAcesso("");
			final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
			filter.setSigla(req.sigla);
			ExMobil mob = (ExMobil) ExDao.getInstance().consultarPorSigla(filter);
			if (mob == null)
				throw new PresentableUnloggedException(
						"Não foi possível encontrar um documento a partir da sigla fornecida");

			HttpServletRequest request = SwaggerServlet.getHttpServletRequest();
			SigaObjects so = new SigaObjects(request);
			DpPessoa titular = so.getTitular();
			DpLotacao lotaTitular = so.getLotaTitular();
			if (!Ex.getInstance().getComp().podeAcessarDocumento(titular, lotaTitular, mob))
				throw new AplicacaoException(
						"Acesso ao documento " + mob.getSigla() + " permitido somente a usuários autorizados. ("
								+ titular.getSigla() + "/" + lotaTitular.getSiglaCompleta() + ")");

			List<CpMarcador> marcadores = ExDao.getInstance().listarCpMarcadoresDisponiveis(so.getLotaTitular());

			if (marcadores != null) {
				resp.list = new ArrayList<>();
				for (CpMarcador m : marcadores) {
					Marcador mr = new Marcador();
					mr.idMarcador = m.getIdMarcador().toString();
					mr.grupo = m.getIdFinalidade().getGrupo().getNome();
					mr.nome = m.getDescrMarcador();
					ExPodeMarcarComMarcador pode = new ExPodeMarcarComMarcador(mob, m, titular, lotaTitular);
					mr.ativo = pode.eval();
					mr.explicacao = AcaoVO.Helper.formatarExplicacao(pode, mr.ativo);
					mr.interessado = m.getIdFinalidade().getIdTpInteressado() != null ? m.getIdFinalidade().getIdTpInteressado().name() : null;
					mr.planejada = m.getIdFinalidade().getIdTpDataPlanejada() != null ? m.getIdFinalidade().getIdTpDataPlanejada().name() : null;
					mr.limite = m.getIdFinalidade().getIdTpDataLimite() != null ? m.getIdFinalidade().getIdTpDataLimite().name() : null;
					mr.texto = m.getIdFinalidade().getIdTpTexto() != null ? m.getIdFinalidade().getIdTpTexto().name() : null;
					resp.list.add(mr);
				}
			}
		} catch (AplicacaoException | SwaggerException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}

	@Override
	public String getContext() {
		return "obter documento completo";
	}

}