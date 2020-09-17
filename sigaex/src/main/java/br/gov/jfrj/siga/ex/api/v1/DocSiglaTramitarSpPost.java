package br.gov.jfrj.siga.ex.api.v1;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.tuple.Pair;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaTramitarSpPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaTramitarSpPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocSiglaTramitarSpPost;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class DocSiglaTramitarSpPost implements IDocSiglaTramitarSpPost {

	@Override
	public String getContext() {
		return "tramitar documento (SP sem Papel)";
	}
	
	private Pair<TramitacaoTipoDestinoEnum, LocalDate> validarRequestEExtrairTipoDestinoEDataDevolucao(DocSiglaTramitarSpPostRequest req, DocSiglaTramitarSpPostResponse resp) throws Exception {
		try {
			TramitacaoTipoDestinoEnum tipoTramitacao = TramitacaoTipoDestinoEnum.valueOf(req.tipoDestinatario);

			if (StringUtils.isEmpty(req.destinatario)) {
				throw new SwaggerException(tipoTramitacao.descricao + " não Fornecido", 400, null, req, resp, null);
			}
			if (!req.destinatario.matches(tipoTramitacao.pattern)) {
				throw new SwaggerException(tipoTramitacao.descricao + " (" + req.destinatario + ") Inválido. "
						+ tipoTramitacao.msgErroPattern, 400, null, req, resp, null);
			}

			LocalDate dataDevolucao = null;
			/*
			if (StringUtils.isNotEmpty(req.dataDevolucao)) {
				dataDevolucao = LocalDate.parse(req.dataDevolucao);
				if (dataDevolucao.isBefore(LocalDate.now())) {
					throw new SwaggerException(
							"Data de devolução não pode ser anterior à data de hoje: " + req.dataDevolucao, 400, null,
							req, resp, null);
				}
			}
			*/

			return Pair.of(tipoTramitacao, dataDevolucao);
		} catch (IllegalArgumentException e) {
			throw new SwaggerException("Tipo de Tramitação inválido: " + req.tipoDestinatario, 400, null, req, resp,
					null);
		} catch (DateTimeParseException e) {
			throw new SwaggerException("Data de Devolução inválida: " + req.dataDevolucao, 400, null, req, resp, null);
		}
	}

	@Override
	public void run(DocSiglaTramitarSpPostRequest req, DocSiglaTramitarSpPostResponse resp) throws Exception {
		req.sigla = SwaggerHelper.decodePathParam(req.sigla);
		Pair<TramitacaoTipoDestinoEnum, LocalDate> tipoDestinoDataDevolucao = validarRequestEExtrairTipoDestinoEDataDevolucao(req, resp);
		System.out.println(
				"MobilTramitarSiglaPost: " + ToStringBuilder.reflectionToString(req, ToStringStyle.SHORT_PREFIX_STYLE)
						+ ", " + tipoDestinoDataDevolucao);

		ApiContext_Remover apiContext = new ApiContext_Remover(true);
		try {
			SwaggerHelper.buscarEValidarUsuarioLogado();

			SigaObjects so = SwaggerHelper.getSigaObjects();

			ExMobil mob = SwaggerHelper.buscarEValidarMobil(req.sigla, req, resp);
			System.out.println("MobilTramitarSiglaPost.run(): " + mob);

			Date dt = ExDao.getInstance().consultarDataEHoraDoServidor();

			apiContext.close();
			return;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			apiContext.rollback(e);
			throw e;
		}
	}

}
