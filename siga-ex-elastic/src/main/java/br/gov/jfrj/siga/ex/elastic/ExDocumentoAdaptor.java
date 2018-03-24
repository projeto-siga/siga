/*******************************************************************************
 * Copyright (c) 2006 - 2015 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.siga.ex.elastic;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Map;

import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.util.MascaraUtil;
import br.gov.jfrj.siga.hibernate.ExDao;

/**
 * Adaptador Google Search Appliance para documentos do SIGA-DOC.
 * <p>
 * Example command line:
 * <p>
 *
 * /java/bin/java \ -cp
 * adaptor-withlib.jar:adaptor-examples.jar:mysql-5.1.10.jar \
 * br.gov.jfrj.siga.ex.gsa.ExDocumentoAdaptor \ -Dgsa.hostname=myGSA
 * -Dservidor=desenv \ -Djournal.reducedMem=true
 */
public class ExDocumentoAdaptor extends ExAdaptor {

	public ExDocumentoAdaptor() {
		loadSigaAllProperties();
		init();
	}

	@Override
	public String getIdsHql() {
		return "select doc.idDoc from ExDocumento doc where doc.dtFinalizacao != null and (:dt is null or doc.dtAltDoc > :dt) order by doc.idDoc desc";
	}

	/** Gives the bytes of a document referenced with id. */
	@Override
	public void pushItem(Long id, Response resp) throws IOException {
		try {
			ExDocumento doc = ExDao.getInstance().consultar(id,
					ExDocumento.class, false);

			if (doc == null || doc.isCancelado()) {
				resp.respondNotFound();
				return;
			}

			addMetadataForDoc(doc, resp);
			addAclForDoc(doc, resp);
			resp.setLastModified(doc.getDtFinalizacao());
			try {
				resp.setDisplayUrl(new URI(permalink + doc.getCodigoCompacto()));
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}

			String html = doc.getHtml();
			if (html != null) {
				resp.setContentType("text/html");
				resp.setContent(html.getBytes());
				return;
			}

			byte pdf[] = doc.getPdf();
			if (pdf != null) {
				resp.setContentType("application/pdf");
				resp.setContent(pdf);
				return;
			}
			log.fine("no content from doc: " + doc.toString());
		} finally {
			ExDao.freeInstance();
		}
	}

	protected static void addAclForDoc(ExDocumento doc, Response resp) {
		// if (doc.getDnmAcesso() == null
		// || doc.isDnmAcessoMAisAntigoQueODosPais()) {
		// Ex.getInstance().getBL().atualizarDnmAcesso(doc);
		// }
		// String sAcessos = doc.getDnmAcesso();
		// List<GroupPrincipal> groups = new ArrayList<>();
		//
		// if (sAcessos == null) {
		// Date dt = ExDao.getInstance().dt();
		// ExAcesso acesso = new ExAcesso();
		// sAcessos = acesso.getAcessosString(doc, dt);
		// if (sAcessos == null || sAcessos.trim().length() == 0)
		// throw new RuntimeException(
		// "Não foi possível calcular os acesos de "
		// + doc.getSigla());
		// }
		//
		// if (ExAcesso.ACESSO_PUBLICO.equals(sAcessos))
		// return;
		// for (String s : sAcessos.split(",")) {
		// groups.add(new GroupPrincipal(s));
		// }
		// Acl acl = new Acl.Builder().setPermitGroups(groups)
		// .setEverythingCaseInsensitive().build();
		// resp.setAcl(acl);
	}

	private void addMetadataForDoc(ExDocumento doc, Response resp) {
		addMetadata(resp, "orgao", doc.getOrgaoUsuario().getAcronimoOrgaoUsu());
		addMetadata(resp, "codigo", doc.getCodigo());
		if (doc.getExTipoDocumento() != null) {
			addMetadata(resp, "origem", doc.getExTipoDocumento().getSigla());
		}
		if (doc.getExFormaDocumento() != null)
			addMetadata(resp, "especie", doc.getExFormaDocumento()
					.getDescricao());
		if (doc.getExModelo() != null)
			addMetadata(resp, "modelo", doc.getExModelo().getNmMod());
		if (doc.getDescrDocumento() != null)
			addMetadata(resp, "descricao", doc.getDescrDocumento());
		if (doc.getDnmExNivelAcesso() != null)
			addMetadata(resp, "acesso", doc.getDnmExNivelAcesso()
					.getNmNivelAcesso());
		if (doc.getDtDocYYYYMMDD() != null)
			addMetadata(resp, "data", doc.getDtDocYYYYMMDD());

		ExClassificacao cAtual = doc.getExClassificacaoAtual();
		if (cAtual == null && doc.getExClassificacao() != null)
			cAtual = doc.getExClassificacao();
		if (cAtual != null) {
			String[] pais = MascaraUtil.getInstance().getPais(
					cAtual.getCodificacao());
			if (pais != null) {
				for (String sigla : pais) {
					ExClassificacao c = new ExClassificacao();
					c.setSigla(sigla);
					ExClassificacao cPai = ExDao.getInstance()
							.consultarPorSigla(c);
					if (cPai != null) {
						addMetadata(
								resp,
								"classificacao_"
										+ MascaraUtil.getInstance()
												.calcularNivel(
														c.getCodificacao()),
								cPai.getDescrClassificacao());
					}
				}
			}

			addMetadata(
					resp,
					"classificacao_"
							+ MascaraUtil.getInstance().calcularNivel(
									cAtual.getCodificacao()),
					cAtual.getDescricao());
		}

		if (doc.getLotaSubscritor() != null) {
			addMetadata(resp, "subscritor_lotacao", doc.getLotaSubscritor()
					.getSiglaLotacao());
		}
		if (doc.getSubscritor() != null) {
			addMetadata(resp, "subscritor", doc.getSubscritor().getNomePessoa());
		}
		if (doc.getLotaCadastrante() != null) {
			addMetadata(resp, "cadastrante_lotacao", doc.getLotaCadastrante()
					.getSiglaLotacao());
		}
		if (doc.getCadastrante() != null) {
			addMetadata(resp, "cadastrante", doc.getCadastrante()
					.getNomePessoa());
		}

		Map<String, String> map = doc.getResumo();
		if (map != null)
			for (String s : map.keySet()) {
				addMetadata(resp, s, map.get(s));
			}
	}

	@Override
	protected String getLastModifiedFileName() {
		return "doc_last_modified";
	}

	public static void main(String[] args) {
		ExAdaptor.run(new ExMovimentacaoAdaptor(), args);
	}


}