package br.gov.jfrj.siga.ex.api.v1;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDocumentosSiglaIncluirCossignatarioPost;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.Transacional;
import br.gov.jfrj.siga.vraptor.builder.ExMovimentacaoBuilder;

@Transacional
public class DocumentosSiglaIncluirCossignatarioPost implements IDocumentosSiglaIncluirCossignatarioPost {

	private DpPessoa getPessoa(Request req) {
		DpPessoa pes = null;
		if (StringUtils.isNotEmpty(req.matricula)) {
			pes = new DpPessoa();
			pes.setSigla(req.matricula);
			pes = dao().consultarPorSigla(pes);
		}
		return pes;
	}

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExMobil mob = ctx.buscarEValidarMobil(req.sigla, req, resp, "Documento a Incluir Cossignatário");

		ctx.assertAcesso(mob, ctx.getTitular(), ctx.getLotaTitular());

		DpPessoa pes = getPessoa(req);
		Date dt = dao().consultarDataEHoraDoServidor();

		if (pes == null)
			throw new AplicacaoException("Pessoa deve ser informada.");

		if (!Ex.getInstance().getComp().podeIncluirCosignatario(ctx.getTitular(), ctx.getLotaTitular(), mob)) {
			throw new AplicacaoException("Não é possível incluir cossignatário");
		}

		if (req.funcao != null && req.funcao.trim().isEmpty())
			req.funcao = null;
		if (req.lotacao != null && req.lotacao.trim().isEmpty())
			req.lotacao = null;
		if (req.localidade != null && req.localidade.trim().isEmpty())
			req.localidade = null;
		String funcaoUnidadeCosignatario = null;
		if (req.funcao != null || req.lotacao != null || req.localidade != null)
			funcaoUnidadeCosignatario = StringUtils.trimToEmpty(req.funcao) + ";" + StringUtils.trimToEmpty(req.lotacao)
					+ ";" + StringUtils.trimToEmpty(req.localidade);

		final ExMovimentacaoBuilder movimentacaoBuilder = ExMovimentacaoBuilder.novaInstancia().setMob(mob)
				.setDescrMov(funcaoUnidadeCosignatario);

		final ExMovimentacao mov = movimentacaoBuilder.construir(dao());
		mov.setSubscritor(pes);

		Ex.getInstance().getBL().incluirCosignatario(ctx.getCadastrante(), ctx.getLotaTitular(), mob.doc(),
				mov.getDtMov(), mov.getSubscritor(), mov.getDescrMov());

		resp.status = "OK";
	}

	public ExDao dao() {
		return ExDao.getInstance();
	}

	@Override
	public String getContext() {
		return "definir perfil";
	}

}
