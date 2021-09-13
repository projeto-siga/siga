package br.gov.jfrj.siga.ex.api.v1;

import java.nio.charset.StandardCharsets;

import br.gov.jfrj.siga.ex.ExModelo;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IModelosIdGet;
import br.gov.jfrj.siga.hibernate.ExDao;

public class ModelosIdGet implements IModelosIdGet {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		ExModelo mod = ExDao.getInstance().consultar(Long.parseLong(req.id), ExModelo.class, false);
		resp.idModelo = mod.getId().toString();
		resp.nome = mod.getNmMod();
		resp.descr = mod.getDescMod();
		resp.especie = mod.getExFormaDocumento().getSigla();
		resp.nivelDeAcesso = mod.getExNivelAcesso() != null ? mod.getExNivelAcesso().getNmNivelAcesso() : null;
		resp.classificacao = mod.getExClassificacao() != null ? mod.getExClassificacao().getSigla() : null;
		resp.classificacaoParaCriacaoDeVias = mod.getExClassCriacaoVia() != null ? mod.getExClassCriacaoVia().getSigla()
				: null;
//			resp.tipoDeSubscritor;
//			resp.tipoDeDestinatario;
		resp.tipoDeConteudo = mod.getConteudoTpBlob();
		for (ExTipoDocumento tipo : mod.getExFormaDocumento().getExTipoDocumentoSet())
			resp.tipoDeDocumento = tipo.getDescricaoSimples();

		byte[] conteudo = mod.getConteudoBlobMod2();
		resp.conteudo = conteudo != null ? new String(conteudo, StandardCharsets.UTF_8) : null;
	}

	@Override
	public String getContext() {
		return "obter informações sobre um modelo";
	}
}
