package br.gov.jfrj.siga.ex.api.v1;

import java.util.Date;
import java.util.Objects;

import com.crivano.swaggerservlet.SwaggerException;

import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaTramitarSpPostRequest;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.DocSiglaTramitarSpPostResponse;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;

enum TramitacaoTipoDestinoEnum {

	ORGAO("Código de Órgão", "^[A-Z]+-[A-Z0-9]+$", "Deve estar no formato 'OO-UUU', "
			+ "onde 'OO' (apenas letras maiúsculas, quantidade variável) corresponde ao código do órgão propriamente dito "
			+ "e 'UUU' (letras maiúsculas e dígitos, quantidade variável) correspondem ao código da unidade.") {

		private DpLotacao getLoacaoDestino(DocSiglaTramitarSpPostRequest req, DocSiglaTramitarSpPostResponse resp)
				throws SwaggerException {
			DpLotacao lotacaoDestino = new DpLotacao();
			lotacaoDestino.setSigla(req.destinatario.replace("-", ""));
			lotacaoDestino = ExDao.getInstance().consultarPorSigla(lotacaoDestino);
			if (Objects.isNull(lotacaoDestino)) {
				throw new SwaggerException("Não foi encontrado Órgão com sigla " + req.destinatario, 404, null, req,
						resp, null);
			}
			return lotacaoDestino;
		}

		@Override
		void efetuarTramitacaoParaDestino(SigaObjects so, ExMobil mob, Date dtDevolucao, Date dt,
				DocSiglaTramitarSpPostRequest req, DocSiglaTramitarSpPostResponse resp) throws SwaggerException {
			DpLotacao lotacaoDestino = getLoacaoDestino(req, resp);

			Ex.getInstance().getBL().transferir(//
					null, // CpOrgao orgaoExterno
					null, // String obsOrgao
					so.getCadastrante(), // DpPessoa cadastrante
					so.getCadastrante().getLotacao(), // DpLotacao lotaCadastrante
					mob, // ExMobil mob
					dt, // final Date dtMov
					dt, // Date dtMovIni
					dtDevolucao, // Date dtFimMov
					lotacaoDestino, // DpLotacao lotaResponsavel
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

	},
	USUARIO("Código do Usuário", "^[A-Z]+\\d+$", "Deve estar no formato OONNNNN, "
			+ "onde 'OO' (apenas letras maiúsculas, quantidade variável) corresponde ao código do órgão propriamente dito "
			+ "e 'NNNN' (apenas dígitos, quantidade variável) à sua respectiva matrícula nesse órgão.") {

		@Override
		void efetuarTramitacaoParaDestino(SigaObjects so, ExMobil mob, Date dtDevolucao, Date dt,
				DocSiglaTramitarSpPostRequest req, DocSiglaTramitarSpPostResponse resp) throws SwaggerException {
			DpPessoa usuarioDestino = getUsuarioDestino(req, resp);

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

		private DpPessoa getUsuarioDestino(DocSiglaTramitarSpPostRequest req, DocSiglaTramitarSpPostResponse resp)
				throws SwaggerException {
			DpPessoa usuarioDestino = new DpPessoa();
			usuarioDestino.setSigla(req.destinatario);
			usuarioDestino = ExDao.getInstance().consultarPorSigla(usuarioDestino);
			if (Objects.isNull(usuarioDestino)) {
				throw new SwaggerException("Não foi encontrado Usuário com a Matrícula " + req.destinatario, 404, null,
						req, resp, null);
			}
			return usuarioDestino;
		}

	},
	EXTERNO("Código de Órgão Externo", "^[A-Z]{1,10}$", "Deve ter até 10 letras maiúsculas.") {

		private CpOrgao getUsuarioExterno(DocSiglaTramitarSpPostRequest req, DocSiglaTramitarSpPostResponse resp)
				throws SwaggerException {
			CpOrgao orgaoExternoDestino = ExDao.getInstance().getOrgaoFromSiglaExata(req.destinatario);
			if (Objects.isNull(orgaoExternoDestino)) {
				throw new SwaggerException("Não foi encontrado Órgão Externo com o código " + req.destinatario, 404,
						null, req, resp, null);
			}
			return orgaoExternoDestino;
		}

		@Override
		void efetuarTramitacaoParaDestino(SigaObjects so, ExMobil mob, Date dtDevolucao, Date dt,
				DocSiglaTramitarSpPostRequest req, DocSiglaTramitarSpPostResponse resp) throws SwaggerException {
			CpOrgao orgaoExternoDestino = getUsuarioExterno(req, resp);
		
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

	};

	final String descricao;

	final String pattern;

	final String msgErroPattern;

	private TramitacaoTipoDestinoEnum(String descricao, String pattern, String msgErroPattern) {
		this.descricao = descricao;
		this.pattern = pattern;
		this.msgErroPattern = msgErroPattern;
	}

	abstract void efetuarTramitacaoParaDestino(SigaObjects so, ExMobil mob, Date dtDevolucao, Date dt,
			DocSiglaTramitarSpPostRequest req, DocSiglaTramitarSpPostResponse resp) throws SwaggerException;

}
