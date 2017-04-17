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
package br.gov.jfrj.siga.ex.gsa;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.enterprise.adaptor.AbstractAdaptor;
import com.google.enterprise.adaptor.Acl;
import com.google.enterprise.adaptor.Config;
import com.google.enterprise.adaptor.ConfigUtils;
import com.google.enterprise.adaptor.DocId;
import com.google.enterprise.adaptor.DocIdPusher;
import com.google.enterprise.adaptor.GroupPrincipal;
import com.google.enterprise.adaptor.Request;
import com.google.enterprise.adaptor.Response;

import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExAcesso;
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
	}
	
	@Override
	public void getModifiedDocIds(DocIdPusher pusher) throws IOException,
	InterruptedException {
		String path = "doc_last_modified" ;
		Date dt = null;
		try {
			dt = ExDao.getInstance().dt();
			getLastModified(dt, path);
			super.pushDocIds(pusher, this.dateLastUpdated);
		} finally {
			saveLastModified(dt, path);
			ExDao.freeInstance();
		}
	}

	@Override
	public void initConfig(Config config) {
		String feedName = adaptorProperties.getProperty("siga.doc.feed.name");
		String port = adaptorProperties.getProperty("siga.doc.server.port");
		String dashboardPort = adaptorProperties
				.getProperty("siga.doc.server.dashboardPort");
		if (feedName != null) {
			ConfigUtils.setValue("feed.name", feedName, config);
		}
		if (port != null) {
			ConfigUtils.setValue("server.port", port, config);
		}
		if (dashboardPort != null) {
			ConfigUtils.setValue("server.dashboardPort", dashboardPort, config);
		}
	}

	@Override
	public String getIdsHql() {
		return "select doc.idDoc from ExDocumento doc where doc.dtFinalizacao != null and (:dt is null or doc.dtAltDoc > :dt) order by doc.idDoc desc";
	}

	/** Gives the bytes of a document referenced with id. */
	public void getDocContent(Request req, Response resp) throws IOException {
		try {
			DocId id = req.getDocId();
			long primaryKey;
			try {
				primaryKey = Long.parseLong(id.getUniqueId());
			} catch (NumberFormatException nfe) {
				resp.respondNotFound();
				return;
			}
			ExDocumento doc = ExDao.getInstance().consultar(primaryKey,
					ExDocumento.class, false);

			if (doc == null || doc.isCancelado()) {
				resp.respondNotFound();
				return;
			}

			addMetadataForDoc(doc, resp);
			addAclForDoc(doc, resp);
			resp.setCrawlOnce(true);
			resp.setLastModified(doc.getDtFinalizacao());
			try {
				resp.setDisplayUrl(new URI(permalink + doc.getCodigoCompacto()));
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}

			String html = doc.getHtml();
			if (html != null) {
				resp.setContentType("text/html");
				resp.getOutputStream().write(html.getBytes());
				return;
			}

			byte pdf[] = doc.getPdf();
			if (pdf != null) {
				resp.setContentType("application/pdf");
				resp.getOutputStream().write(pdf);
				return;
			}
			log.fine("no content from doc: " + doc.toString());
		} finally {
			ExDao.freeInstance();
		}
	}

	protected static void addAclForDoc(ExDocumento doc, Response resp) {
		if (doc.getDnmAcesso() == null || doc.isDnmAcessoMAisAntigoQueODosPais()) {
			Ex.getInstance().getBL().atualizarDnmAcesso(doc);
		}
		String sAcessos = doc.getDnmAcesso();
		List<GroupPrincipal> groups = new ArrayList<>();

		if (sAcessos == null) {
			Date dt = ExDao.getInstance().dt();
			ExAcesso acesso = new ExAcesso();
			sAcessos = acesso.getAcessosString(doc, dt);
			if (sAcessos == null || sAcessos.trim().length() == 0)
				throw new RuntimeException(
						"Não foi possível calcular os acesos de "
								+ doc.getSigla());
		}

		if (ExAcesso.ACESSO_PUBLICO.equals(sAcessos))
			return;
		for (String s : sAcessos.split(",")) {
			groups.add(new GroupPrincipal(s));
		}
		Acl acl = new Acl.Builder().setPermitGroups(groups)
				.setEverythingCaseInsensitive().build();
		resp.setAcl(acl);

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

	public static void main(String[] args) {
		AbstractAdaptor.main(new ExDocumentoAdaptor(), args);
	}

}