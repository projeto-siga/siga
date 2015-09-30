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
import br.gov.jfrj.siga.ex.ExMovimentacao;
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
 * Adaptador Google Search Appliance para movimentações do SIGA-DOC.
 * <p>
 * Example command line:
 * <p>
 *
 * /java/bin/java \ -cp
 * adaptor-withlib.jar:adaptor-examples.jar:mysql-5.1.10.jar \
 * br.gov.jfrj.siga.ex.gsa.ExMovimentacaoAdaptor \ -Dgsa.hostname=myGSA
 * -Dservidor=desenv \ -Djournal.reducedMem=true
 */
public class ExMovimentacaoAdaptor extends ExAdaptor {

	@Override
	public String getIdsHql() {
		return "select mov.idMov from ExMovimentacao mov where mov.exTipoMovimentacao.idTpMov in (2, 5, 6, 7, 8, 18) and (:dt is null or mov.exMobil.exDocumento.dtFinalizacao > :dt or mov.dtIniMov > :dt) order by mov.idMov desc";
	}

	@Override
	public String getFeedName() {
		return "siga-doc-movimentacoes";
	}

	@Override
	public int getServerPortIncrement() {
		return 1;
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
		ExMovimentacao mov = ExDao.getInstance().consultar(primaryKey,
				ExMovimentacao.class, false);

		if (mov == null || mov.isCancelado()) {
			resp.respondNotFound();
			return;
		}

		ExDocumento doc = mov.getExDocumento();

		if (doc == null || doc.isCancelado()) {
			resp.respondNotFound();
			return;
		}

		Date dt = doc.getDtFinalizacao();
		if (dt == null || dt.before(mov.getDtIniMov()))
			dt = mov.getDtIniMov();

		addMetadataForMov(doc, mov, resp);
		ExDocumentoAdaptor.addAclForDoc(doc, resp);
		// resp.setCrawlOnce(true);
		resp.setLastModified(dt);
		try {
			resp.setDisplayUrl(new URI(permalink + doc.getCodigoCompacto()
					+ "/" + mov.getIdMov()));
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

		String html = mov.getHtml();
		if (html != null) {
			resp.setContentType("text/html");
			resp.getOutputStream().write(html.getBytes());
			return;
		}

		byte pdf[] = mov.getPdf();
		if (pdf != null) {
			resp.setContentType("application/pdf");
			resp.getOutputStream().write(pdf);
			return;
		}
		log.fine("no content from mov: " + mov.toString());
	}

	private void addMetadataForMov(ExDocumento doc, ExMovimentacao mov,
			Response resp) {
		if (doc.getDnmExNivelAcesso() != null)
			resp.addMetadata("acesso", doc.getDnmExNivelAcesso()
					.getNmNivelAcesso());
		if (mov.getDtMovYYYYMMDD() != null)
			resp.addMetadata("data", mov.getDtMovYYYYMMDD());
		if (mov.getLotaSubscritor() != null)
			resp.addMetadata("subscritor_lotacao", mov.getLotaSubscritor()
					.getSiglaLotacao());
		if (mov.getSubscritor() != null)
			resp.addMetadata("subscritor", mov.getSubscritor().getNomePessoa());

		if (mov.getLotaCadastrante() != null)
			resp.addMetadata("cadastrante_lotacao", mov.getLotaCadastrante()
					.getSiglaLotacao());
		if (mov.getCadastrante() != null)
			resp.addMetadata("cadastrante", mov.getCadastrante()
					.getNomePessoa());
	}

	public static void main(String[] args) {
		AbstractAdaptor.main(new ExMovimentacaoAdaptor(), args);
	}

}