package br.gov.jfrj.siga.ex.api.v1;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaTramitarSpPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaTramitarSpPostResponse;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocSiglaTramitarSpPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class DocSiglaTramitarSpPost implements IDocSiglaTramitarSpPost {

	void efetuarTramitacaoParaLotacao(SigaObjects so, ExMobil mob, Date dtDevolucao, Date dt,
			DocSiglaTramitarSpPostRequest req, DocSiglaTramitarSpPostResponse resp) throws SwaggerException {
		DpLotacao lot = new DpLotacao();
		lot.setSigla(req.destinatario.replace("-", ""));
		lot = ExDao.getInstance().consultarPorSigla(lot);
		if (Objects.isNull(lot)) {
			throw new SwaggerException("Não foi encontrado Órgão com sigla " + req.destinatario, 404, null, req, resp,
					null);
		}
		Ex.getInstance().getBL().transferir(//
				null, // CpOrgao orgaoExterno
				null, // String obsOrgao
				so.getCadastrante(), // DpPessoa cadastrante
				so.getCadastrante().getLotacao(), // DpLotacao lotaCadastrante
				mob, // ExMobil mob
				dt, // final Date dtMov
				dt, // Date dtMovIni
				dtDevolucao, // Date dtFimMov
				lot, // DpLotacao lotaResponsavel
				null, // final DpPessoa responsavel
				null, // DpLotacao lotaDestinoFinal
				null, // DpPessoa destinoFinal
				null, // DpPessoa subscritor
				so.getCadastrante(), // DpPessoa titular
				null, // ExTipoDespacho tpDespacho. Que tipo de despacho?
				true, // final boolean fInterno: QUANDO USO ISSO?
				null, // String descrMov
				null, // String conteudo
				null, // String nmFuncaoSubscritor
				false, // boolean forcarTransferencia
				false // boolean automatico
		);

	}

	void efetuarTramitacaoParaUsuario(SigaObjects so, ExMobil mob, Date dtDevolucao, Date dt,
			DocSiglaTramitarSpPostRequest req, DocSiglaTramitarSpPostResponse resp) throws SwaggerException {
		DpPessoa usuarioDestino = new DpPessoa();
		usuarioDestino.setSigla(req.destinatario);
		usuarioDestino = ExDao.getInstance().consultarPorSigla(usuarioDestino);
		if (Objects.isNull(usuarioDestino)) {
			throw new SwaggerException("Não foi encontrado Usuário com a Matrícula " + req.destinatario, 404, null, req,
					resp, null);
		}

		Ex.getInstance().getBL().transferir(//
				null, // CpOrgao orgaoExterno
				null, // String obsOrgao
				so.getCadastrante(), // DpPessoa cadastrante
				so.getCadastrante().getLotacao(), // DpLotacao lotaCadastrante
				mob, // ExMobil mob
				dt, // final Date dtMov
				dt, // Date dtMovIni
				dtDevolucao, // Date dtFimMov
				usuarioDestino.getLotacao(), // DpLotacao lotaResponsavel
				usuarioDestino, // final DpPessoa responsavel
				null, // DpLotacao lotaDestinoFinal
				null, // DpPessoa destinoFinal
				null, // DpPessoa subscritor
				so.getCadastrante(), // DpPessoa titular
				null, // ExTipoDespacho tpDespacho. Que tipo de despacho?
				true, // final boolean fInterno: QUANDO USO ISSO?
				null, // String descrMov
				null, // String conteudo
				null, // String nmFuncaoSubscritor
				false, // boolean forcarTransferencia
				false // boolean automatico
		);
	}

	void efetuarTramitacaoParaOrgaoExtrno(SigaObjects so, ExMobil mob, Date dtDevolucao, Date dt,
			DocSiglaTramitarSpPostRequest req, DocSiglaTramitarSpPostResponse resp) throws SwaggerException {
		CpOrgao orgaoExternoDestino = ExDao.getInstance().getOrgaoFromSiglaExata(req.destinatario);
		if (Objects.isNull(orgaoExternoDestino)) {
			throw new SwaggerException("Não foi encontrado Órgão Externo com o código " + req.destinatario, 404, null,
					req, resp, null);
		}

		Ex.getInstance().getBL().transferir(//
				orgaoExternoDestino, // CpOrgao orgaoExterno
				req.observacao, // String obsOrgao
				so.getCadastrante(), // DpPessoa cadastrante
				so.getCadastrante().getLotacao(), // DpLotacao lotaCadastrante
				mob, // ExMobil mob
				dt, // final Date dtMov
				dt, // Date dtMovIni
				dtDevolucao, // Date dtFimMov
				null, // DpLotacao lotaResponsavel
				null, // final DpPessoa responsavel
				null, // DpLotacao lotaDestinoFinal
				null, // DpPessoa destinoFinal
				null, // DpPessoa subscritor
				so.getCadastrante(), // DpPessoa titular
				null, // ExTipoDespacho tpDespacho. Que tipo de despacho?
				true, // final boolean fInterno: QUANDO USO ISSO?
				null, // String descrMov
				null, // String conteudo
				null, // String nmFuncaoSubscritor
				false, // boolean forcarTransferencia
				false // boolean automatico
		);
	}

	@Override
	public String getContext() {
		return "tramitar documento (SP sem Papel)";
	}

	private Date getDataDevolucao(DocSiglaTramitarSpPostRequest req, DocSiglaTramitarSpPostResponse resp)
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

	private TramitacaoTipoDestinoEnum getTipoTramitcao(DocSiglaTramitarSpPostRequest req,
			DocSiglaTramitarSpPostResponse resp) throws SwaggerException {
		try {
			TramitacaoTipoDestinoEnum tipoTramitacao = TramitacaoTipoDestinoEnum.valueOf(req.tipoDestinatario);

			if (StringUtils.isEmpty(req.destinatario)) {
				throw new SwaggerException(tipoTramitacao.descricao + " não Fornecido", 400, null, req, resp, null);
			}
			if (!req.destinatario.matches(tipoTramitacao.pattern)) {
				throw new SwaggerException(tipoTramitacao.descricao + " (" + req.destinatario + ") Inválido. "
						+ tipoTramitacao.msgErroPattern, 400, null, req, resp, null);
			}
			return tipoTramitacao;
		} catch (IllegalArgumentException e) {
			throw new SwaggerException("Tipo de Tramitação inválido: " + req.tipoDestinatario, 400, null, req, resp,
					null);
		}
	}

	@Override
	public void run(DocSiglaTramitarSpPostRequest req, DocSiglaTramitarSpPostResponse resp) throws Exception {
		req.sigla = SwaggerHelper.decodePathParam(req.sigla);
		TramitacaoTipoDestinoEnum tipoDestino = getTipoTramitcao(req, resp);
		Date dataDevolucao = getDataDevolucao(req, resp);

		try {
			SwaggerHelper.buscarEValidarUsuarioLogado();
			SigaObjects so = SwaggerHelper.getSigaObjects();

			ExMobil mob = SwaggerHelper.buscarEValidarMobil(req.sigla, req, resp);
			System.out.println("MobilTramitarSiglaPost.run(): " + mob);

			Utils.assertAcesso(mob, so.getCadastrante(), so.getCadastrante().getLotacao());

			if (!Ex.getInstance().getComp().podeTransferir(so.getCadastrante(), so.getCadastrante().getLotacao(),
					mob)) {
				throw new SwaggerException("O documento " + req.sigla + " não pode ser tramitado por "
						+ so.getCadastrante().getSiglaCompleta() + "/"
						+ so.getCadastrante().getLotacao().getSiglaCompleta(), 403, null, req, resp, null);
			}

			Date dt = ExDao.getInstance().consultarDataEHoraDoServidor();

			switch (tipoDestino) {
			case ORGAO:
				this.efetuarTramitacaoParaLotacao(so, mob, dataDevolucao, dt, req, resp);
				break;
			case USUARIO:
				this.efetuarTramitacaoParaUsuario(so, mob, dataDevolucao, dt, req, resp);
				break;
			case EXTERNO:
				this.efetuarTramitacaoParaOrgaoExtrno(so, mob, dataDevolucao, dt, req, resp);
				break;
			}

			return;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}

}