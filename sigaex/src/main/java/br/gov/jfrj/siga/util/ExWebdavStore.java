package br.gov.jfrj.siga.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Date;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;

import br.gov.jfrj.itextpdf.Documento;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.vo.ExMovimentacaoVO;
import br.gov.jfrj.siga.hibernate.ExDao;
import net.sf.webdav.ITransaction;
import net.sf.webdav.IWebdavStore;
import net.sf.webdav.StoredObject;

public class ExWebdavStore implements IWebdavStore {
	private static final Logger log = Logger.getLogger(ExWebdavStore.class);

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

	public ExWebdavStore() {
	}

	public ExWebdavStore(File f) {
		log.error("criando" + f);
	}

	@Override
	public ITransaction begin(Principal principal) {
		// log.error("begin");
		return new Transaction(principal);
	}

	@Override
	public void checkAuthentication(ITransaction t) {
		// log.error("checkAuthentication");
		return;
	}

	@Override
	public void commit(ITransaction t) {
		// log.error("commit");
		return;
	}

	@Override
	public void createFolder(ITransaction t, String uri) {
		log.error("createFolter:" + uri);
		return;
	}

	@Override
	public void createResource(ITransaction t, String uri) {
		log.error("createResource:" + uri);
		return;
	}

	@Override
	public String[] getChildrenNames(ITransaction t, String uri) {
		log.error("getChildrenNames:" + uri);
		// return new String[] { "TMP11962_32790.docx" };
		return new String[] {};
	}

	@Override
	public InputStream getResourceContent(ITransaction t, String uri) {
		log.error("getResourceContent:" + uri);
		byte[] ab = getContent(uri);
		return new ByteArrayInputStream(ab);
	}

	@Override
	public long getResourceLength(ITransaction t, String uri) {
		log.error("getResourceLength:" + uri);
		byte[] ab = getContent(uri);
		return ab != null ? ab.length : 0;
	}

	@Override
	public StoredObject getStoredObject(ITransaction t, String uri) {
		log.error("getStoredObject:" + uri);
		if ("".equals(uri) || "/".equals(uri) || uri.endsWith("/") || uri.split("/").length == 2) {
			Date dt = new Date(2000, 1, 1);
			StoredObject o = new StoredObject();
			o.setFolder(true);
			o.setCreationDate(dt);
			o.setLastModified(dt);
			o.setResourceLength(0);
			log.error(" - folder");
			return o;
		}
		ExMovimentacao mov = null;
		try {
			mov = getMov(new Context(uri));
		} catch (Exception e) {
			log.error(" - null");
			return null;
		}
		StoredObject o = new StoredObject();
		o.setFolder(false);
		Date dt = mov.getDtIniMov();
		o.setCreationDate(dt);
		o.setLastModified(dt);

		byte ab[] = null;
		ab = mov.getConteudoBlobMov2();
		o.setResourceLength(ab != null ? ab.length : 0);
		log.error(" - resource");
		return o;
	}

	@Override
	public void removeObject(ITransaction t, String uri) {
		log.error("removeObject:" + uri);
		ExMovimentacao mov = getMov(new Context(uri));
		try {
			Ex.getInstance().getBL().cancelar(null, null, mov.getExMobil(), mov, null, null, null, null);
		} catch (Exception e) {
			throw new AplicacaoException("Não foi possível remover o arquivo auxiliar.", 0, e);
		}
	}

	@Override
	public void rollback(ITransaction t) {
		log.error("rollback:");
		return;
	}

	@Override
	public long setResourceContent(ITransaction t, String uri, InputStream content, String contentType,
			String encoding) {
		log.error("setResourceContent:" + uri);
		Context ctx = new Context(uri);

		if (contentType == null)
			contentType = getContentType(ctx.nmArq);

		ExMobil mob = null;
		try {
			mob = Documento.getMobil(ctx.mobilSigla);
		} catch (Exception e) {
			throw new AplicacaoException("Erro obtendo o documento.", 0, e);
		}
		byte[] ab;
		try {
			ab = IOUtils.toByteArray(content);
		} catch (IOException e) {
			throw new AplicacaoException("Não foi possível obter o conteúdo a ser gravado no arquivo auxiliar.", 0, e);
		}
		try {
			ExDao dao = ExDao.getInstance();
			DpPessoa cadastrante = dao.getPessoaFromSigla(ctx.cadastranteSigla);
			DpPessoa titular = dao.getPessoaFromSigla(ctx.titularSigla);
			DpLotacao lotaTitular = dao.getLotacaoFromSigla(ctx.unidadeSigla);
			Ex.getInstance().getBL().anexarArquivoAuxiliar(cadastrante, cadastrante.getLotacao(), mob, null, titular,
					ctx.nmArq, titular, lotaTitular, ab, contentType);
		} catch (Exception e) {
			throw new AplicacaoException("Não foi possível gravar o arquivo auxiliar.", 0, e);
		}
		return 0;
	}

	private byte[] getContent(String uri) {
		ExMovimentacao mov = getMov(new Context(uri));

		byte ab[] = null;
		ab = mov.getConteudoBlobMov2();
		return ab;
	}

	private ExMovimentacao getMov(Context ctx) {
		ExMobil mob;
		try {
			mob = Documento.getMobil(ctx.mobilSigla);
		} catch (Exception e) {
			throw new AplicacaoException("Erro obtendo o documento.", 0, e);
		}
		if (mob == null) {
			throw new AplicacaoException("A sigla informada não corresponde a um documento da base de dados.");
		}
		// if (!Ex.getInstance().getComp().podeAcessarDocumento(getTitular(),
		// getLotaTitular(), mob)) {
		// throw new AplicacaoException("Documento " + mob.getSigla() + "
		// inacessível ao usuário " + getTitular().getSigla() + "/"
		// + getLotaTitular().getSiglaCompleta() + ".");
		// }
		ExMovimentacao mov;
		try {
			mov = getMov(mob, ctx.nmArq);
		} catch (Exception e) {
			throw new AplicacaoException("Erro obtendo a movimentação.", 0, e);
		}
		final boolean isArquivoAuxiliar = mov != null && mov.getExTipoMovimentacao().getId()
				.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO_DE_ARQUIVO_AUXILIAR);
		if (!isArquivoAuxiliar) {
			throw new AplicacaoException("A sigla informada não corresponde a um arquivo auxiliar.");
		}
		return mov;
	}

	// private String getSigla(String uri) {
	// return uri.replace("/", "").replace(".docx", "").replace(".doc",
	// "").replace(".xlsx", "").replace(".xls", "")
	// .replace("_", ":");
	// }

	private static class ContextOld {
		String mobilSigla;
		String cadastranteSigla;
		String titularSigla;
		String unidadeSigla;
		String nmArq;

		public ContextOld(String uri) {
			mobilSigla = getMobilSigla(uri);
			cadastranteSigla = getCadastranteSigla(uri);
			titularSigla = getTitularSigla(uri);
			unidadeSigla = getUnidadeSigla(uri);
			nmArq = getNmArq(uri);
		}

		private String getMobilSigla(String uri) {
			return uri.split("_")[0].replace("/", "");
		}

		private String getCadastranteSigla(String uri) {
			return uri.split("_")[1];
		}

		private String getTitularSigla(String uri) {
			return uri.split("_")[2];
		}

		private String getUnidadeSigla(String uri) {
			String s = uri.split("_")[3];
			if (s.contains("/"))
				s = s.substring(0, s.indexOf("/"));
			return s;
		}

		private String getNmArq(String uri) {
			return uri.substring(uri.lastIndexOf("/") + 1);
		}

	}

	private static class Context {
		String mobilSigla;
		String cadastranteSigla;
		String titularSigla;
		String unidadeSigla;
		String nmArq;

		public Context(String uri) {
			String a[] = uri.split("/");
			String pwd = ExMovimentacaoVO.getWebdavPassword();
			if (pwd == null)
				throw new AplicacaoException("Propriedade siga.ex.webdav.pwd precisa ser configurada");

			Map<String, Object> jwt = ExMovimentacaoVO.getWebdavDecodedToken(a[1]);

			mobilSigla = jwt.get("mob").toString();
			cadastranteSigla = jwt.get("cad").toString();
			titularSigla = jwt.get("tit").toString();
			unidadeSigla = jwt.get("lot").toString();
			nmArq = getNmArq(uri);
		}

		private String getNmArq(String uri) {
			return uri.substring(uri.lastIndexOf("/") + 1);
		}

	}

	private ExMovimentacao getMov(ExMobil mob, String nmArq) {
		for (ExMovimentacao m : mob.getExMovimentacaoSet()) {
			if (m.getExTipoMovimentacao().getId()
					.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO_DE_ARQUIVO_AUXILIAR) && !m.isCancelada()
					&& m.getNmArqMov() != null && m.getNmArqMov().equals(nmArq))
				return m;
		}
		return null;
	}

	private String getContentType(String nmArq) {
		nmArq = nmArq.toLowerCase();
		if (nmArq.endsWith("docx"))
			return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		if (nmArq.endsWith("docx"))
			return "application/msword";
		if (nmArq.endsWith("xlsx"))
			return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		if (nmArq.endsWith("xls"))
			return "application/vnd.ms-excel";
		if (nmArq.endsWith("pptx"))
			return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
		if (nmArq.endsWith("ppt"))
			return "application/vnd.ms-powerpoint";
		return null;
	}

}
