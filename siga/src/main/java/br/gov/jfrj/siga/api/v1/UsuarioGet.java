package br.gov.jfrj.siga.api.v1;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.api.v1.ISigaApiV1.IUsuarioGet;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.SubstituicaoItem;
import br.gov.jfrj.siga.api.v1.ISigaApiV1.Usuario;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.dp.DpSubstituicao;

public class UsuarioGet implements IUsuarioGet {

	@Override
	public void run(Request req, Response resp, SigaApiV1Context ctx) throws Exception {
		List<DpSubstituicao> substituicoesPermitidas = ctx.getSigaObjects().getMeusTitulares();
		resp.usuario = new Usuario();
		if (ctx.getIdentidadeCadastrante() != null)
			resp.usuario.identidadeId = ctx.getIdentidadeCadastrante().getId().toString();

		if (ctx.getCadastrante() != null) {
			resp.usuario.cadastranteId = ctx.getCadastrante().getId().toString();
			resp.usuario.cadastranteSigla = ctx.getCadastrante().getSigla();
			resp.usuario.cadastranteNome = ctx.getCadastrante().getNomePessoa();
		}
		if (ctx.getLotaCadastrante() != null) {
			resp.usuario.lotaCadastranteId = ctx.getLotaCadastrante().getId().toString();
			resp.usuario.lotaCadastranteSigla = ctx.getLotaCadastrante().getSigla();
			resp.usuario.lotaCadastranteNome = ctx.getLotaCadastrante().getNomeLotacao();
		}
		if (ctx.getTitular() != null) {
			resp.usuario.titularId = ctx.getTitular().getId().toString();
			resp.usuario.titularSigla = ctx.getTitular().getSigla();
			resp.usuario.titularNome = ctx.getTitular().getNomePessoa();
		}
		if (ctx.getLotaTitular() != null) {
			resp.usuario.lotaTitularId = ctx.getLotaTitular().getId().toString();
			resp.usuario.lotaTitularSigla = ctx.getLotaTitular().getSigla();
			resp.usuario.lotaTitularNome = ctx.getLotaTitular().getNomeLotacao();
		}

		resp.usuario.substituicoesPermitidas = new ArrayList<>();
		if (substituicoesPermitidas != null)
			for (DpSubstituicao i : substituicoesPermitidas) {
				boolean titularSubstituido = !ctx.getCadastrante().equivale(ctx.getTitular());
				boolean lotaTitularSubstituida = !ctx.getCadastrante().getLotacao().equivale(ctx.getLotaTitular());
				if (titularSubstituido && ctx.getTitular().equivale(i.getTitular())
						|| lotaTitularSubstituida && ctx.getLotaTitular().equivale(i.getLotaTitular()))
					resp.usuario.substituicaoId = i.getIdSubstituicao().toString();

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
					r.substitutoNome = Texto.maiusculasEMinusculas(i.getSubstituto().getNomePessoa());
				}
				if (i.getLotaSubstituto() != null) {
					r.lotaSubstitutoId = i.getLotaSubstituto().getId().toString();
					r.lotaSubstitutoSigla = i.getLotaSubstituto().getSigla();
					r.lotaSubstitutoNome = Texto.maiusculasEMinusculas(i.getLotaSubstituto().getNomeLotacao());
				}
				if (i.getTitular() != null) {
					r.titularId = i.getTitular().getId().toString();
					r.titularSigla = i.getTitular().getSigla();
					r.titularNome = Texto.maiusculasEMinusculas(i.getTitular().getNomePessoa());
				}
				if (i.getLotaTitular() != null) {
					r.lotaTitularId = i.getLotaTitular().getId().toString();
					r.lotaTitularSigla = i.getLotaTitular().getSigla();
					r.lotaTitularNome = Texto.maiusculasEMinusculas(i.getLotaTitular().getNomeLotacao());
				}
				resp.usuario.substituicoesPermitidas.add(r);
			}
	}

	@Override
	public String getContext() {
		return "obter quadro quantitativo";
	}

}
