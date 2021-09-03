package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IUsuarioGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.SubstituicaoItem;
import br.gov.jfrj.siga.dp.DpSubstituicao;

public class UsuarioGet implements IUsuarioGet {

	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		List<DpSubstituicao> substituicoesPermitidas = ctx.getSigaObjects().getMeusTitulares();

		if (ctx.getIdentidadeCadastrante() != null)
			resp.identidadeId = ctx.getIdentidadeCadastrante().getId().toString();

		if (ctx.getCadastrante() != null) {
			resp.cadastranteId = ctx.getCadastrante().getId().toString();
			resp.cadastranteSigla = ctx.getCadastrante().getSigla();
			resp.cadastranteNome = ctx.getCadastrante().getNomePessoa();
		}
		if (ctx.getLotaCadastrante() != null) {
			resp.lotaCadastranteId = ctx.getLotaCadastrante().getId().toString();
			resp.lotaCadastranteSigla = ctx.getLotaCadastrante().getSigla();
			resp.lotaCadastranteNome = ctx.getLotaCadastrante().getNomeLotacao();
		}
		if (ctx.getTitular() != null) {
			resp.titularId = ctx.getTitular().getId().toString();
			resp.titularSigla = ctx.getTitular().getSigla();
			resp.titularNome = ctx.getTitular().getNomePessoa();
		}
		if (ctx.getLotaTitular() != null) {
			resp.lotaTitularId = ctx.getLotaTitular().getId().toString();
			resp.lotaTitularSigla = ctx.getLotaTitular().getSigla();
			resp.lotaTitularNome = ctx.getLotaTitular().getNomeLotacao();
		}

		resp.substituicoesPermitidas = new ArrayList<>();
		if (substituicoesPermitidas != null)
			for (DpSubstituicao i : substituicoesPermitidas) {
				SubstituicaoItem r = new SubstituicaoItem();
				r.substituicaoId = i.getIdSubstituicao().toString();
				r.substituicaoDataFim = i.getDtFimSubst();
				r.substituicaoDataInicio = i.getDtIniSubst();
				if (i.getIdRegistroInicial() != null)
					r.registroIdInicial = i.getIdRegistroInicial().toString();
				r.registroDataFim = i.getDtFimRegistro();
				r.registroDataInicio = i.getDtIniRegistro();
				if (i.getSubstituto() != null) {
					r.substitutoId = i.getSubstituto().getId().toString();
					r.substitutoSigla = i.getSubstituto().getSigla();
					r.substitutoNome = i.getSubstituto().getNomePessoa();
				}
				if (i.getLotaSubstituto() != null) {
					r.lotaSubstitutoId = i.getLotaSubstituto().getId().toString();
					r.lotaSubstitutoSigla = i.getLotaSubstituto().getSigla();
					r.lotaSubstitutoNome = i.getLotaSubstituto().getNomeLotacao();
				}
				if (i.getTitular() != null) {
					r.titularId = i.getTitular().getId().toString();
					r.titularSigla = i.getTitular().getSigla();
					r.titularNome = i.getTitular().getNomePessoa();
				}
				if (i.getLotaTitular() != null) {
					r.lotaTitularId = i.getLotaTitular().getId().toString();
					r.lotaTitularSigla = i.getLotaTitular().getSigla();
					r.lotaTitularNome = i.getLotaTitular().getNomeLotacao();
				}
				resp.substituicoesPermitidas.add(r);
			}
	}

	@Override
	public String getContext() {
		return "obter quadro quantitativo";
	}

}
