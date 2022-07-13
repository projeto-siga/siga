
package br.gov.jfrj.siga.ex.api.v1;

import java.util.List;

import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosLocalizarMaisRecenteGet;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;

public class DocumentosLocalizarMaisRecenteGet implements IDocumentosLocalizarMaisRecenteGet {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		final ExMobilDaoFiltro builder = new ExMobilDaoFiltro();

		// Modelo
		ExModelo modelo = null;
		if (req.modelo != null && !req.modelo.trim().isEmpty()) {
			modelo = ExDao.getInstance().consultarExModelo(null, req.modelo);
			if (modelo == null)
				throw new SwaggerException("Não existe modelo com o nome especificado (" + req.modelo + ")", 400, null,
						req, resp, null);
			builder.setIdMod(modelo.getHisIdIni());
			builder.setIdFormaDoc(modelo.getExFormaDocumento().getId());
		}

		// Lota Subscritor
		if (req.lotaSubscritor != null && !req.lotaSubscritor.trim().isEmpty()) {
			final DpLotacao fltLotaSubscritor = new DpLotacao();
			fltLotaSubscritor.setSigla(req.lotaSubscritor.toUpperCase());
			DpLotacao lotaSubscritor = CpDao.getInstance().consultarPorSigla(fltLotaSubscritor);
			if (lotaSubscritor == null)
				throw new SwaggerException("Nenhuma lotação foi encontrada contendo a sigla informada.", 400, null, req,
						resp, null);
			builder.setLotaSubscritorSelId(lotaSubscritor.getHisIdIni());
		}

		// Ano
		if (req.ano != null && !req.ano.trim().isEmpty())
			builder.setAnoEmissao(Long.parseLong(req.ano));
//		else
//			throw new SwaggerException("Nenhum ano foi informado.", 400, null, req, resp, null);

		// Marcador
		if (req.marcador != null && !req.marcador.trim().isEmpty()) {
			List<CpMarcador> listMarcador = ExDao.getInstance().consultaCpMarcadorAtivoPorNome(req.marcador, null);
			if (listMarcador.size() > 0) {
				CpMarcador marcador = listMarcador.get(0);
				builder.setUltMovIdEstadoDoc(marcador.getHisIdIni());
			} else {
				throw new SwaggerException("Não existe marcador com o nome especificado (" + req.marcador + ")", 400,
						null, req, resp, null);
			}
		}

		List<Object[]> l = ExDao.getInstance().consultarPorFiltro(builder, 0, 1);
		if (l.isEmpty())
			throw new SwaggerException("Nenhum documento foi encontrado com os argumentos informados.", 404, null, req,
					resp, null);
		ExMobil mob = (ExMobil) l.get(0)[1];
		SwaggerServlet.getHttpServletResponse().sendRedirect("/sigaex/api/v1/documentos/" + mob.getCodigoCompacto());
	}

	@Override
	public String getContext() {
		return "localizar documento mais recente";
	}

}
