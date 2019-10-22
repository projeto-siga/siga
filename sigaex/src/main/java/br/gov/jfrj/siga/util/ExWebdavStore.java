package br.gov.jfrj.siga.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.jboss.logging.Logger;

import br.gov.jfrj.itextpdf.Documento;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.vo.ExMovimentacaoVO;
import br.gov.jfrj.siga.hibernate.ExDao;
import net.sf.webdav.DavExtensionConfig;
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
		log.info("criando" + f);
	}

	@Override
	public ITransaction begin(Principal principal) {
		log.info("begin");
		return new Transaction(principal);
	}

	@Override
	public void checkAuthentication(ITransaction t) {
		log.info("checkAuthentication");
		return;
	}

	@Override
	public void commit(ITransaction t) {
		log.info("commit");
		return;
	}

	@Override
	public void createFolder(ITransaction t, String uri) {
		log.info("createFolter:" + uri);
		return;
	}

	@Override
	public void createResource(ITransaction t, String uri) {
		log.info("createResource:" + uri);
		return;
	}

	@Override
	public String[] getChildrenNames(ITransaction t, String uri) {
		log.info("getChildrenNames:" + uri);
		try {
			Ret ret = getMovs(new Context(uri));
			int i = 0;
			String[] s = new String[ret.movs.size()];
			for (ExMovimentacao m : ret.movs) {
				s[i] = m.getNmArqMov();
				i++;
			}
			log.info(s);
			return s;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String[] {};
	}

	@Override
	public InputStream getResourceContent(ITransaction t, String uri) {
		log.info("getResourceContent:" + uri);
		byte[] ab;
		try {
			ab = getContent(uri);
		} catch (Exception e) {
			throwException("Não foi possível obter o conteúdo do arquivo auxiliar", e);
			return null;
		}
		log.info(ab);
		return new ByteArrayInputStream(ab);
	}

	@Override
	public long getResourceLength(ITransaction t, String uri) {
		log.info("getResourceLength:" + uri);
		byte[] ab;
		try {
			ab = getContent(uri);
		} catch (Exception e) {
			throwException("Não foi possível obter o tamanho do arquivo auxiliar", e);
			return 0;
		}
		return ab != null ? ab.length : 0;
	}

	@Override
	public StoredObject getStoredObject(ITransaction t, String uri) {
		log.info("getStoredObject:" + uri);
		Ret ret = null;
		try {
			if (uri.startsWith("/!"))
				ret = getMovs(new Context(uri));
		} catch (Exception e) {
			log.info(" - null");
			return null;
		}
		Date dt = new Date(10, 1, 1);
		if ("".equals(uri) || "/".equals(uri) || uri.endsWith("/") || uri.split("/").length == 2) {
			StoredObject o = new StoredObject();
			if (ret != null)
				dt = ret.mob.doc().getDtRegDoc();
			o.setFolder(true);
			o.setCreationDate(dt);
			o.setLastModified(dt);
			o.setResourceLength(0);
			log.info(" - folder" + ReflectionToStringBuilder.toString(o));
			return o;
		}
		StoredObject o = new StoredObject();
		o.setFolder(false);
		dt = ret.mov.getDtIniMov();
		o.setCreationDate(dt);
		o.setLastModified(dt);
		o.setMimeType(getContentType(ret.mov.getNmArqMov()));
		o.setEtag("teste");
		// o.setNullResource(false);
		// o.setResourceTypes(new ArrayList<String>());

		byte ab[] = null;
		ab = ret.mov.getConteudoBlobMov2();

		o.setResourceLength(ab != null ? ab.length : 0);
		log.info(" - resource" + ReflectionToStringBuilder.toString(o));
		return o;
	}

	@Override
	public void removeObject(ITransaction t, String uri) {
		log.info("removeObject:" + uri);
		try {
			ExMovimentacao mov = getMovs(new Context(uri)).mov;
			Ex.getInstance().getBL().cancelar(null, null, mov.getExMobil(), mov, null, null, null, null);
		} catch (Exception e) {
			throwException("Não foi possível remover o arquivo auxiliar.", e);
		}
	}

	@Override
	public void rollback(ITransaction t) {
		log.info("rollback:");
		return;
	}

	@Override
	public long setResourceContent(ITransaction t, String uri, InputStream content, String contentType,
			String encoding) {
		log.info("setResourceContent:" + uri);
		Context ctx = new Context(uri);

		if (contentType == null)
			contentType = getContentType(ctx.nmArq);

		ExMobil mob = null;
		try {
			mob = Documento.getMobil(ctx.mobilSigla);
		} catch (Exception e) {
			throwException("Erro obtendo o documento.", e);
		}
		byte[] ab;
		try {
			ab = IOUtils.toByteArray(content);
		} catch (IOException e) {
			throwException("Não foi possível obter o conteúdo a ser gravado no arquivo auxiliar.", e);
			return 0;
		}
		try {
			ExDao dao = ExDao.getInstance();
			DpPessoa cadastrante = dao.getPessoaFromSigla(ctx.cadastranteSigla);
			DpPessoa titular = dao.getPessoaFromSigla(ctx.titularSigla);
			DpLotacao lotaTitular = dao.getLotacaoFromSigla(ctx.unidadeSigla);
			Ex.getInstance().getBL().anexarArquivoAuxiliar(cadastrante, cadastrante.getLotacao(), mob, null, titular,
					ctx.nmArq, titular, lotaTitular, ab, contentType);
		} catch (Exception e) {
			throwException("Não foi possível gravar o arquivo auxiliar.", e);
		}
		return ab.length;
	}

	private byte[] getContent(String uri) throws Exception {
		ExMovimentacao mov = getMovs(new Context(uri)).mov;

		byte ab[] = null;
		ab = mov.getConteudoBlobMov2();
		return ab;
	}

	private static class Ret {
		ExMobil mob;
		List<ExMovimentacao> movs;
		ExMovimentacao mov;
	}

	private Ret getMovs(Context ctx) throws Exception {
		Ret ret = new Ret();

		try {
			ret.mob = Documento.getMobil(ctx.mobilSigla);
		} catch (Exception e) {
			throw new Exception("Erro obtendo o documento.", e);
		}
		if (ret.mob == null) {
			throw new Exception("A sigla informada não corresponde a um documento da base de dados.");
		}
		// (!Ex.getInstance().getComp().podeAcessarDocumento(getTitular(),
		// getLotaTitular(), mob)) {
		// return throwException("Documento " + mob.getSigla() + "
		// inacessível ao usuário " + getTitular().getSigla() + "/"
		// + getLotaTitular().getSiglaCompleta() + ".");
		// }
		ret.movs = new ArrayList<>();
		try {
			for (ExMovimentacao m : ret.mob.getExMovimentacaoSet()) {
				if (m.getExTipoMovimentacao().getId()
						.equals(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO_DE_ARQUIVO_AUXILIAR) && !m.isCancelada()
						&& m.getNmArqMov() != null)
					ret.movs.add(m);
			}
		} catch (Exception e) {
			throw new Exception("Erro obtendo a movimentação.", e);
		}

		ExMovimentacao mov;
		if (ctx.nmArq != null && ctx.nmArq.length() > 0) {
			for (ExMovimentacao m : ret.movs)
				if (!m.isCancelada() && m.getNmArqMov() != null && m.getNmArqMov().equals(ctx.nmArq)) {
					ret.mov = m;
					break;
				}
			if (ret.mov == null)
				throwException("A sigla informada não corresponde a um arquivo auxiliar.", null);
		}
		return ret;
	}

	// private String getSigla(String uri) {
	// return uri.replace("/", "").replace(".docx", "").replace(".doc",
	// "").replace(".xlsx", "").replace(".xls", "")
	// .replace("_", ":");
	// }

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
				throwException("Propriedade siga.ex.webdav.pwd precisa ser configurada", null);

			Map<String, Object> jwt = ExMovimentacaoVO.getWebdavDecodedToken(a[1]);

			mobilSigla = jwt.get("mob").toString();
			cadastranteSigla = jwt.get("cad").toString();
			titularSigla = jwt.get("tit").toString();
			unidadeSigla = jwt.get("lot").toString();
			nmArq = getNmArq(uri);
			if (nmArq != null && nmArq.equals(a[1]))
				nmArq = null;
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
		if (nmArq.endsWith(".docx"))
			return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		if (nmArq.endsWith(".doc"))
			return "application/msword";
		if (nmArq.endsWith(".xlsx"))
			return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		if (nmArq.endsWith(".xls"))
			return "application/vnd.ms-excel";
		if (nmArq.endsWith(".pptx"))
			return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
		if (nmArq.endsWith(".ppt"))
			return "application/vnd.ms-powerpoint";
		return null;
	}

	private static void throwException(String s, Throwable t) {
		log.error(s, t);
		throw new RuntimeException(s, t);
	}

	@Override
	public void addNamespace(HashMap<String, String> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Principal createPrincipal(HttpServletRequest req) {
		Principal p = new Principal() {

			@Override
			public String getName() {
				return "TESTE";
			}
			
		};
		return req.getUserPrincipal();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, String> getAdditionalProperties(String arg0, Vector<String> arg1) {
		return new HashMap<String, String>();
	}

	@Override
	public DavExtensionConfig getConfig() {
		// TODO Auto-generated method stub
		return new DavExtensionConfig();
	}

	@Override
	public List<String> getReportSubEntries(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void moveResource(ITransaction arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean supportsMoveOperation() {
		// TODO Auto-generated method stub
		return false;
	}

}
