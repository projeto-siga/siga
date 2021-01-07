package br.gov.jfrj.siga.ex.api.v1;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.crivano.swaggerservlet.PresentableUnloggedException;
import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaTramitarPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocumentosSiglaTramitarPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaTramitarPost;
import br.gov.jfrj.siga.ex.bl.CurrentRequest;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.RequestInfo;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class DocumentosSiglaTramitarPost implements IDocumentosSiglaTramitarPost {

	private void validarPreenchimentoDestino(DocumentosSiglaTramitarPostRequest req, DocumentosSiglaTramitarPostResponse resp)
			throws SwaggerException {
		if (StringUtils.isEmpty(req.orgao) && StringUtils.isEmpty(req.lotacao) && StringUtils.isEmpty(req.matricula)) {
			throw new SwaggerException("Você deve fornecer ou orgao (apenas) ou matricula ou lotacao *com* a matricula",
					400, null, req, resp, null);
		}
		if (!StringUtils.isEmpty(req.orgao) && !StringUtils.isEmpty(req.lotacao) && StringUtils.isEmpty(req.matricula)) {
			throw new SwaggerException("Você deve fornecer a lotacao caso a matricula esteja fornecida", 400, null, req,
					resp, null);
		}
		if (StringUtils.isNotEmpty(req.orgao)
				&& (StringUtils.isNotEmpty(req.lotacao) || StringUtils.isNotEmpty(req.matricula))) {
			throw new SwaggerException(
					"Orgão externo não deve ser fornecido se for tramitar para Lotação e/ou Matrícula", 400, null, req,
					resp, null);
		}
	}

	private void validarAcesso(DocumentosSiglaTramitarPostRequest req, DpPessoa titular, DpLotacao lotaTitular, ExMobil mob)
			throws Exception, PresentableUnloggedException {
		ApiContext.assertAcesso(mob, titular, lotaTitular);

		if (!Ex.getInstance().getComp().podeTransferir(titular, lotaTitular, mob))
			throw new PresentableUnloggedException("O documento " + req.sigla + " não pode ser tramitado por "
					+ titular.getSiglaCompleta() + "/" + lotaTitular.getSiglaCompleta());
	}

	private CpOrgao getOrgaoExterno(DocumentosSiglaTramitarPostRequest req, DocumentosSiglaTramitarPostResponse resp)
			throws SwaggerException {
		if (StringUtils.isEmpty(req.orgao)) {
			return null;
		}

		CpOrgao orgaoExternoDestino = ExDao.getInstance().getOrgaoFromSiglaExata(req.orgao);
		if (Objects.isNull(orgaoExternoDestino)) {
			throw new SwaggerException("Não foi encontrado Órgão Externo com o código " + req.orgao, 404, null, req,
					resp, null);
		}
		return orgaoExternoDestino;
	}

	private DpPessoa getResponsavel(DocumentosSiglaTramitarPostRequest req, CpOrgao orgaoExterno) {
		DpPessoa pes = null;
		if (Objects.isNull(orgaoExterno) && StringUtils.isNotEmpty(req.matricula)) {
			pes = new DpPessoa();
			pes.setSigla(req.matricula);
			pes = ExDao.getInstance().consultarPorSigla(pes);
		}
		return pes;
	}

	private DpLotacao getLotacao(DocumentosSiglaTramitarPostRequest req, CpOrgao orgaoExterno) {
		DpLotacao lot = null;
		if (Objects.isNull(orgaoExterno) && StringUtils.isNotEmpty(req.lotacao)) {
			lot = new DpLotacao();
			lot.setSigla(req.lotacao);
			lot = ExDao.getInstance().consultarPorSigla(lot);
		}
		return lot;
	}

	private Date getDataDevolucao(DocumentosSiglaTramitarPostRequest req, DocumentosSiglaTramitarPostResponse resp)
			throws SwaggerException {
		if (StringUtils.isEmpty(req.dataDevolucao)) {
			return null;
		}
		try {
			LocalDate localDate = LocalDate.parse(req.dataDevolucao);
			if (localDate.isBefore(LocalDate.now())) {
				throw new SwaggerException(
						"Data de devolução não pode ser anterior à data de hoje: " + req.dataDevolucao, 400, null, req,
						resp, null);
			}
			return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		} catch (DateTimeParseException e) {
			throw new SwaggerException("Data de Devolução inválida: " + req.dataDevolucao, 400, null, req, resp, null);
		}
	}

	@Override
	public void run(DocumentosSiglaTramitarPostRequest req, DocumentosSiglaTramitarPostResponse resp) throws Exception {
		try (ApiContext ctx = new ApiContext(true, true)) {
			CurrentRequest.set(
					new RequestInfo(null, SwaggerServlet.getHttpServletRequest(), SwaggerServlet.getHttpServletResponse()));
			ApiContext.assertAcesso("");
			validarPreenchimentoDestino(req, resp);
	
			SigaObjects so = ApiContext.getSigaObjects();
			DpPessoa cadastrante = so.getCadastrante();
			DpLotacao lotaCadastrante = cadastrante.getLotacao();
			DpPessoa titular = cadastrante;
			DpLotacao lotaTitular = cadastrante.getLotacao();

			ExMobil mob = SwaggerHelper.buscarEValidarMobil(req.sigla, so, req, resp, "Documento a Tramitar");

			validarAcesso(req, titular, lotaTitular, mob);

			CpOrgao orgaoExterno = this.getOrgaoExterno(req, resp);
			DpLotacao lot = getLotacao(req, orgaoExterno);
			DpPessoa pes = getResponsavel(req, orgaoExterno);
			String observacao = Objects.isNull(orgaoExterno) ? null : req.observacao;
			Date dtDevolucao = this.getDataDevolucao(req, resp);
			Date dt = ExDao.getInstance().consultarDataEHoraDoServidor();

			Ex.getInstance().getBL().transferir(//
					orgaoExterno, // CpOrgao orgaoExterno
					observacao, // String obsOrgao
					cadastrante, // DpPessoa cadastrante
					lotaCadastrante, // DpLotacao lotaCadastrante
					mob, // ExMobil mob
					dt, // final Date dtMov
					dt, // Date dtMovIni
					dtDevolucao, // Date dtFimMov
					lot, // DpLotacao lotaResponsavel
					pes, // final DpPessoa responsavel
					null, // DpLotacao lotaDestinoFinal
					null, // DpPessoa destinoFinal
					null, // DpPessoa subscritor
					titular, // DpPessoa titular
					null, // ExTipoDespacho tpDespacho.
					true, // final boolean fInterno
					null, // String descrMov
					null, // String conteudo
					null, // String nmFuncaoSubscritor
					false, // boolean forcarTransferencia
					false // boolean automatico
			);

			resp.status = "OK";
		} catch (AplicacaoException | SwaggerException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}

	@Override
	public String getContext() {
		return "tramitar documento";
	}

}
