package br.gov.jfrj.siga.ex.api.v1;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorTipoInteressadoEnum;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaMarcarPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaMarcarPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaMarcarPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.builder.ExMovimentacaoBuilder;

public class DocumentosSiglaMarcarPost implements IDocumentosSiglaMarcarPost {
	private void validarAcesso(DocumentosSiglaMarcarPostRequest req, DpPessoa titular, DpLotacao lotaTitular,
			ExMobil mob) throws Exception, PresentableUnloggedException {
		ApiContext.assertAcesso(mob, titular, lotaTitular);

//		if (!Ex.getInstance().getComp().podeMarcar(titular, lotaTitular, mob))
//			throw new PresentableUnloggedException("O documento " + req.sigla + " não pode ser marcado por "
//					+ titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta());
	}

	private DpPessoa getPessoa(DocumentosSiglaMarcarPostRequest req) {
		DpPessoa pes = null;
		if (StringUtils.isNotEmpty(req.matricula)) {
			pes = new DpPessoa();
			pes.setSigla(req.matricula);
			pes = dao().consultarPorSigla(pes);
		}
		return pes;
	}

	private DpLotacao getLotacao(DocumentosSiglaMarcarPostRequest req) {
		DpLotacao lot = null;
		if (StringUtils.isNotEmpty(req.lotacao)) {
			lot = new DpLotacao();
			lot.setSigla(req.lotacao);
			lot = dao().consultarPorSigla(lot);
		}
		return lot;
	}

	private Date getData1(DocumentosSiglaMarcarPostRequest req) throws AplicacaoException {
		if (StringUtils.isEmpty(req.data1)) {
			return null;
		}
		try {
			LocalDate localDate = LocalDate.parse(req.data1);
			return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		} catch (DateTimeParseException e) {
			throw new AplicacaoException("Data1 inválida: " + req.data1);
		}
	}

	private Date getData2(DocumentosSiglaMarcarPostRequest req) throws AplicacaoException {
		if (StringUtils.isEmpty(req.data2)) {
			return null;
		}
		try {
			LocalDate localDate = LocalDate.parse(req.data2);
			return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		} catch (DateTimeParseException e) {
			throw new AplicacaoException("Data2 inválida: " + req.data2);
		}
	}

	@Override
	public void run(DocumentosSiglaMarcarPostRequest req, DocumentosSiglaMarcarPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			try {
				ApiContext.assertAcesso("");

				ExMobil mob = SwaggerHelper.buscarEValidarMobil(req.sigla, ctx.getSigaObjects(), req, resp,
						"Documento a Marcar");

				validarAcesso(req, ctx.getTitular(), ctx.getLotaTitular(), mob);

				CpMarcador m = dao().consultar(Long.parseLong(req.idMarcador), CpMarcador.class, false);
				DpLotacao lot = getLotacao(req);
				DpPessoa pes = getPessoa(req);
				Date dt1 = this.getData1(req);
				Date dt2 = this.getData2(req);
				Date dt = dao().consultarDataEHoraDoServidor();

				if (m == null)
					throw new AplicacaoException("Marcador deve ser informado.");

				final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder.novaInstancia();

				if (m.getIdFinalidade().getIdTpInteressado() == CpMarcadorTipoInteressadoEnum.PESSOA) {
					if (pes == null)
						throw new AplicacaoException("Pessoa deve ser informada.");
				}

				if (m.getIdFinalidade().getIdTpInteressado() == CpMarcadorTipoInteressadoEnum.LOTACAO) {
					if (lot == null)
						throw new AplicacaoException("Lotação deve ser informada.");
				}

				if (m.getIdFinalidade().getIdTpInteressado() == CpMarcadorTipoInteressadoEnum.PESSOA_OU_LOTACAO || m
						.getIdFinalidade().getIdTpInteressado() == CpMarcadorTipoInteressadoEnum.LOTACAO_OU_PESSOA) {
					if (pes == null && lot == null)
						throw new AplicacaoException("Pessoa ou lotação devem ser informadas.");
				}

				final ExMovimentacao mov = movimentacaoBuilder.construir(dao());
				mov.setMarcador(m);
				mov.setSubscritor(pes);
				mov.setLotaSubscritor(lot);
				mov.setDescrMov(req.texto);
				Ex.getInstance().getBL().marcar(ctx.getCadastrante(), ctx.getLotaCadastrante(), ctx.getTitular(),
						ctx.getLotaTitular(), mob, mov.getDtMov(), mov.getSubscritor(), mov.getLotaSubscritor(),
						mov.getDescrMov(), mov.getMarcador(), dt1, dt2, true);
				resp.status = "OK";
			} catch (RegraNegocioException | AplicacaoException e) {
				ctx.rollback(e);
				throw new SwaggerException(e.getMessage(), 400, null, req, resp, null);
			} catch (Exception e) {
				ctx.rollback(e);
				throw e;
			}
		}
	}

	public ExDao dao() {
		return ExDao.getInstance();
	}

	@Override
	public String getContext() {
		return "marcar documento";
	}

}
