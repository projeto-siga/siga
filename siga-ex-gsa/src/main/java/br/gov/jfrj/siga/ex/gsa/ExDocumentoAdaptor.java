package br.gov.jfrj.siga.ex.gsa;

//Copyright 2011 Google Inc. All Rights Reserved.
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.cfg.Configuration;

import br.gov.jfrj.siga.cp.bl.CpAmbienteEnumBL;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

import com.google.enterprise.adaptor.AbstractAdaptor;
import com.google.enterprise.adaptor.Acl;
import com.google.enterprise.adaptor.Adaptor;
import com.google.enterprise.adaptor.AdaptorContext;
import com.google.enterprise.adaptor.Config;
import com.google.enterprise.adaptor.DocId;
import com.google.enterprise.adaptor.DocIdPusher;
import com.google.enterprise.adaptor.GroupPrincipal;
import com.google.enterprise.adaptor.PollingIncrementalLister;
import com.google.enterprise.adaptor.Request;
import com.google.enterprise.adaptor.Response;

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

	@Override
	public String getIdsHql() {
		return "select doc.idDoc from ExDocumento doc where doc.dtFinalizacao != null and doc.dtFinalizacao > :dt order by doc.idDoc desc";
	}

	@Override
	public String getFeedName() {
		return "siga-documentos";
	}

	@Override
	public int getServerPortIncrement() {
		return 0;
	}

	/** Gives the bytes of a document referenced with id. */
	public void getDocContent(Request req, Response resp) throws IOException {
		ExDao dao = ExDao.getInstance();

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
		// resp.setCrawlOnce(true);
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
	}

	protected static void addAclForDoc(ExDocumento doc, Response resp) {
		String sAcessos = doc.getDnmAcesso();
		if ("PUBLICO".equals(sAcessos))
			return;

		List<GroupPrincipal> groups = new ArrayList<>();
		if (sAcessos == null) {
			log.fine("acessos is null for");
			return;
		}
		for (String s : sAcessos.split(",")) {
			groups.add(new GroupPrincipal(s));
		}
		Acl acl = new Acl.Builder().setPermitGroups(groups)
				.setEverythingCaseInsensitive().build();
		resp.setAcl(acl);

	}

	private void addMetadataForDoc(ExDocumento doc, Response resp) {
		if (doc.getExTipoDocumento() != null) {
			resp.addMetadata("Origem", doc.getExTipoDocumento().getSigla());
		}
		if (doc.getExFormaDocumento() != null)
			resp.addMetadata("Espécie", doc.getExFormaDocumento()
					.getDescricao());
		if (doc.getExModelo() != null)
			resp.addMetadata("Modelo", doc.getExModelo().getNmMod());
		if (doc.getDescrDocumento() != null)
			resp.addMetadata("Descrição", doc.getDescrDocumento());
		if (doc.getDnmExNivelAcesso() != null)
			resp.addMetadata("Nível de Acesso", doc.getDnmExNivelAcesso()
					.getNmNivelAcesso());
		if (doc.getDtDocYYYYMMDD() != null)
			resp.addMetadata("Data", doc.getDtDocYYYYMMDD());
		if (doc.getExClassificacaoAtual() != null) {
			resp.addMetadata("Código da Classificação", doc
					.getExClassificacaoAtual().getSigla());
			resp.addMetadata(
					"Descrição da Classificação",
					doc.getExClassificacaoAtual().getDescricao()
							.replace(": ", ", "));
		}
		if (doc.getLotaSubscritor() != null)
			resp.addMetadata("Lotação do Subscritor", doc.getLotaSubscritor()
					.getSiglaLotacao());
		if (doc.getSubscritor() != null)
			resp.addMetadata("Subscritor", doc.getSubscritor().getNomePessoa());

		if (doc.getLotaCadastrante() != null)
			resp.addMetadata("Lotação do Cadastrante", doc.getLotaCadastrante()
					.getSiglaLotacao());
		if (doc.getCadastrante() != null)
			resp.addMetadata("Cadastrante", doc.getCadastrante()
					.getNomePessoa());

		Map<String, String> map = doc.getResumo();
		if (map != null)
			for (String s : map.keySet()) {
				resp.addMetadata(s, map.get(s));
			}
	}

	public static void main(String[] args) {
		AbstractAdaptor.main(new ExDocumentoAdaptor(), args);
	}

}