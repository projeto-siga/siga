package br.gov.jfrj.siga.vraptor;

import static br.com.caelum.vraptor.view.Results.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Lists;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.com.caelum.vraptor.view.HttpResult;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.util.Paginador;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpSubstituicao;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class SigaController {
	protected SigaObjects so;

	protected Result result;

	protected HttpServletRequest request;

	private EntityManager em;
	protected CpDao dao;

	private Paginador p = new Paginador();
		
	private Integer postback;
	
	
	private String mensagemAguarde = null;
	
	//Todo: verificar se após a migração do vraptor se ainda necessita deste atributo "par"
	private Map<String, String[]> par;
	
	protected Map<String, String[]> getPar() {
		return par;
	}
	
	protected void setParam(final String parameterName, final String parameterValue) {
		final String as[] = { parameterValue };
		getPar().put(parameterName, as);
		return;
	}	
	
	protected String getUrlEncodedParameters()
			throws UnsupportedEncodingException, IOException {
		if (getPar() != null) {
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				for (final String key : getPar().keySet()) {
					final String as[] = getPar().get(key);
					for (final String val : as) {
						if (baos.size() > 0)
							baos.write('&');
						baos.write(URLEncoder.encode(key, "utf-8").getBytes());
						baos.write('=');
						baos.write(URLEncoder.encode(val, "utf-8").getBytes());
					}
				}
				return new String(baos.toByteArray());
			}
		}
		return null;
	}
	
	protected CpDao dao() {
		return CpDao.getInstance();
	}

	protected Integer getPostback() {
		return postback;
	}
	
	/**
	 * @deprecated CDI eyes only
	 */
	public SigaController() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	public SigaController(HttpServletRequest request, Result result, CpDao dao,
			SigaObjects so, EntityManager em) {
		super();
		this.setRequest(request);
		this.dao = dao;
		this.setPar(new HashMap<>( getRequest().getParameterMap()));
		this.result = result;
		this.so = so;
		this.em = em;

		//result.on(AplicacaoException.class).forwardTo(this).appexception();
		//result.on(Exception.class).forwardTo(this).exception();
		
		result.include("cadastrante", getCadastrante());
		result.include("lotaCadastrante", getLotaCadastrante());
		result.include("titular", getTitular());
		result.include("lotaTitular", getLotaTitular());
		result.include("meusTitulares", getMeusTitulares());
		result.include("identidadeCadastrante",getIdentidadeCadastrante());
	}

	protected List<DpSubstituicao> getMeusTitulares() {
		try {
			List<DpSubstituicao> substituicoes = so.getMeusTitulares();
			if (substituicoes == null) {
				return Lists.newArrayList();
			}
			resolveLazy(substituicoes);
			return substituicoes;
		} catch (Exception e) {
			throw new AplicacaoException("Erro", 500, e);
		}
	}
	
	protected byte[] toByteArray(final UploadedFile upload) throws IOException {
		try (InputStream is = upload.getFile()) {
			// Get the size of the file
			final long tamanho = upload.getSize();
	
			// Não podemos criar um array usando o tipo long.
			// é necessário usar o tipo int.
			if (tamanho > Integer.MAX_VALUE)
				throw new IOException("Arquivo muito grande");
	
			// Create the byte array to hold the data
			final byte[] meuByteArray = new byte[(int) tamanho];
	
			// Read in the bytes
			int offset = 0;
			int numRead = 0;
			while (offset < meuByteArray.length && (numRead = is.read(meuByteArray, offset, meuByteArray.length - offset)) >= 0) {
				offset += numRead;
			}
	
			// Ensure all the bytes have been read in
			if (offset < meuByteArray.length)
				throw new IOException("Não foi possível ler o arquivo completamente " + upload.getFileName());
	
			return meuByteArray;
		}
	}



	private void resolveLazy(List<DpSubstituicao> substituicoes) {
		for (DpSubstituicao dpSubstituicao : substituicoes) {
			if (dpSubstituicao.getTitular() != null) {
				dpSubstituicao.getTitular().getId();
			}
			
			if (dpSubstituicao.getLotaTitular() != null) {
				dpSubstituicao.getLotaTitular().getId();
			}
		}
	}
	
	public void appexception() {
		configurarHttpResult(400);
	}

	public void exception() {
		configurarHttpResult(500);
	}
	
	private void configurarHttpResult(int statusCode) {
		HttpResult res = this.result.use(http());
		res.setStatusCode(statusCode);
		definirPaginaDeErro();
	}
    
	private void definirPaginaDeErro() {
		if (requisicaoEhAjax())
		    result.forwardTo("/WEB-INF/page/erroGeralAjax.jsp");
		else 
		    result.forwardTo("/WEB-INF/page/erroGeral.jsp");
    }
	
    private boolean requisicaoEhAjax() {
        return request.getHeader("X-Requested-With") != null;
    }
    
	protected DpLotacao getLotaTitular() {
		return so.getLotaTitular();
	}
	
	protected void setLotaTitular(DpLotacao lotaTitular) {
		so.setLotaTitular(lotaTitular);
	}	

	protected DpPessoa getTitular() {
		return so.getTitular();
	}
	
	protected void setTitular(DpPessoa titular) {
		so.setTitular(titular);
	}
	
	protected DpLotacao getLotaCadastrante(){
		if(null != so.getCadastrante()) {
			return so.getCadastrante().getLotacao();
		} else {
			return null;
		}
	}

	protected DpPessoa getCadastrante() {
		return so.getCadastrante();
	}
	
	protected void setCadastrante(DpPessoa cadastrante) {
		so.setCadastrante(cadastrante);		
	}

	protected CpIdentidade getIdentidadeCadastrante() {
		return so.getIdentidadeCadastrante();
	}

	protected String param(final String parameterName) {
		final String[] as = getRequest().getParameterValues(parameterName);
		if (as == null || as[0].equals("null"))
			return null;
		return as[0];
	}

	protected Integer paramInteger(final String parameterName) {
		final String s = param(parameterName);
		if (s == null || s.equals(""))
			return null;
		return Integer.parseInt(s);
	}

	protected Long paramLong(final String parameterName) {
		final String s = param(parameterName);
		try {
			return Long.parseLong(s);
		} catch (Throwable t) {
			return null;
		}
	}

	protected Short paramShort(final String parameterName) {
		final String s = param(parameterName);
		if (s == null || s.equals(""))
			return null;
		return Short.parseShort(s);
	}

	protected Date paramDate(final String parameterName) {
		Date dt = null;
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			dt = df.parse(param(parameterName));
		} catch (final ParseException e) {
			return null;
		} catch (final NullPointerException e) {
			return null;
		}
		return dt;
	}

	protected DpPessoa daoPes(long id) {
		return so.daoPes(id);
	}

	protected DpPessoa daoPes(String sigla) {
		return so.daoPes(sigla);
	}

	protected DpLotacao daoLot(long id) {
		return so.daoLot(id);
	}

	protected DpLotacao daoLot(String sigla) {
		return so.daoLot(sigla);
	}

	protected CpOrgaoUsuario daoOU(String sigla) {
		return so.daoOU(sigla);
	}

	protected EntityManager em() {
		return this.em;
	}

	protected int redirectToHome() {
		result.redirectTo("/../siga/app/principal");
		return 0;
	}

	protected void resultOK() {
		result.use(Results.http()).body("OK").setStatusCode(200);
	}

	protected void setMensagem(String mensagem) {
		result.include("mensagem", mensagem);
	}

	protected HttpServletRequest getRequest() {
		return request;
	}

	protected void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	protected void assertAcesso(final String pathServico) {
		so.assertAcesso(pathServico);
	}	
	
	protected void setP(Paginador p) {
		this.p = p;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void setPar(final Map par) {
		this.par = par;
	}

	protected void setPostback(final Integer postback) {
		this.postback = postback;
	}
	
	protected String getMensagemAguarde() {
		return mensagemAguarde;
	}
	
	protected void setMensagemAguarde(String mensagemAguarde) {
		this.mensagemAguarde = mensagemAguarde;
	}

	protected List<CpOrgaoUsuario> getOrgaosUsu() throws AplicacaoException {
		return dao().listarOrgaosUsuarios();
	}
	
	protected Paginador getP() {
		return p;
	}
	
	protected boolean podeUtilizarServico(String servico)
			throws Exception {
		return Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getTitular(), getLotaTitular(), servico);
	}

}
