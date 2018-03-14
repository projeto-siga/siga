package br.gov.jfrj.siga.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;

import br.gov.jfrj.itextpdf.Documento;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import net.sf.webdav.ITransaction;
import net.sf.webdav.LocalFileSystemStore;
import net.sf.webdav.StoredObject;

public class ExWebdavStore2 extends LocalFileSystemStore {
	private static final Logger log = Logger.getLogger(ExWebdavStore2.class);

	private static class Transaction implements ITransaction {
		Principal principal;

		public Transaction(Principal principal) {
			this.principal = principal;
		}

		@Override
		public Principal getPrincipal() {
			return principal;
		}

	}

	public ExWebdavStore2(File f) {
		super(f);
	}

	@Override
	public ITransaction begin(Principal principal) {
		ITransaction t = super.begin(principal);
		log.error("begin");
		return t;
	}

	@Override
	public void checkAuthentication(ITransaction t) {
		log.error("checkAuthentication");
		super.checkAuthentication(t);
	}

	@Override
	public void commit(ITransaction t) {
		log.error("commit");

		super.commit(t);
		return;
	}

	@Override
	public void createFolder(ITransaction t, String uri) {
		log.error("createFolter:" + uri);
		super.createFolder(t, uri);
		return;
	}

	@Override
	public void createResource(ITransaction t, String uri) {
		log.error("createResource:" + uri);
		super.createResource(t, uri);
		return;
	}

	@Override
	public String[] getChildrenNames(ITransaction t, String uri) {
		log.error("getChildrenNames:" + uri);
		String[] a = super.getChildrenNames(t, uri);
		log.error(" - "  + a);
		return a;
		// return new String[] { "TMP11962_32790.docx" };
		// return new String[] {};
	}

	@Override
	public InputStream getResourceContent(ITransaction t, String uri) {
		log.error("getResourceContent:" + uri);
		InputStream is = super.getResourceContent(t, uri);
		return is;
	}

	@Override
	public long getResourceLength(ITransaction t, String uri) {
		log.error("getResourceLength:" + uri);
		return super.getResourceLength(t, uri);
	}

	@Override
	public StoredObject getStoredObject(ITransaction t, String uri) {
		log.error("getStoredObject:" + uri); 
		StoredObject so = super.getStoredObject(t, uri);
		log.error(" - " + so.getCreationDate() + " - " + so.getLastModified() + " - " + so.getResourceLength() + 
				" - folder: " + so.isFolder() + " - resource: " + so.isResource() + " - null: " + so.isNullResource());
		return so;
	}

	@Override
	public void removeObject(ITransaction t, String uri) {
		log.error("removeObject:" + uri);
		super.removeObject(t, uri);
	}

	@Override
	public void rollback(ITransaction t) {
		log.error("rollback:");
		super.rollback(t);
	}

	@Override
	public long setResourceContent(ITransaction t, String uri, InputStream content, String contentType,
			String encoding) {
		log.error("setResourceContent:" + uri);
		return super.setResourceContent(t, uri, content, contentType, encoding);
	}
}
