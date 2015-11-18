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
import java.util.Date;

import com.google.enterprise.adaptor.AbstractAdaptor;
import com.google.enterprise.adaptor.DocId;
import com.google.enterprise.adaptor.Request;
import com.google.enterprise.adaptor.Response;

import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;

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
		try {
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
		} finally {
			ExDao.freeInstance();
		}
	}

	private void addMetadataForMov(ExDocumento doc, ExMovimentacao mov,
			Response resp) {
		if (doc.getDnmExNivelAcesso() != null)
			addMetadata(resp, "acesso", doc.getDnmExNivelAcesso()
					.getNmNivelAcesso());
		if (mov.getDtMovYYYYMMDD() != null)
			addMetadata(resp, "data", mov.getDtMovYYYYMMDD());
		if (mov.getLotaSubscritor() != null)
			addMetadata(resp, "subscritor_lotacao", mov.getLotaSubscritor()
					.getSiglaLotacao());
		if (mov.getSubscritor() != null)
			addMetadata(resp, "subscritor", mov.getSubscritor().getNomePessoa());

		if (mov.getLotaCadastrante() != null)
			addMetadata(resp, "cadastrante_lotacao", mov.getLotaCadastrante()
					.getSiglaLotacao());
		if (mov.getCadastrante() != null)
			addMetadata(resp, "cadastrante", mov.getCadastrante()
					.getNomePessoa());
	}

	public static void main(String[] args) {
		AbstractAdaptor.main(new ExMovimentacaoAdaptor(), args);
	}

}