package br.gov.jfrj.siga.context;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings;

import com.crivano.swaggerservlet.IUnloggedException;
import com.crivano.swaggerservlet.SwaggerApiContextSupport;
import com.crivano.swaggerservlet.SwaggerAuthorizationException;
import com.crivano.swaggerservlet.SwaggerContext;
import com.crivano.swaggerservlet.SwaggerException;
import com.crivano.swaggerservlet.SwaggerServlet;

import br.gov.jfrj.siga.base.CurrentRequest;
import br.gov.jfrj.siga.base.HttpRequestUtils;
import br.gov.jfrj.siga.base.RegraNegocioException;
import br.gov.jfrj.siga.base.RequestInfo;
import br.gov.jfrj.siga.base.log.RequestLoggerFilter;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.auth.AutenticadorFabrica;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import br.gov.jfrj.siga.uteis.SafeListCustom;
import br.gov.jfrj.siga.vraptor.RequestParamsCheck;
import br.gov.jfrj.siga.vraptor.RequestParamsPermissiveCheck;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.vraptor.Transacional;

abstract public class ApiContextSupport extends SwaggerApiContextSupport {

	EntityManager em;
	boolean transacional = false;
	long inicio = System.currentTimeMillis();
	SigaObjects sigaObjects = null;
	
    private final Level BLAME = Level.forName("BLAME", 450); 
    private final Logger logger = LogManager.getLogger(ApiContextSupport.class);

	@Override
	public void init(SwaggerContext ctx) {
		super.init(ctx);
		
		try {
			CurrentRequest.set(new RequestInfo(null, SwaggerServlet.getHttpServletRequest(),
					SwaggerServlet.getHttpServletResponse()));
		} catch (NullPointerException e) {
			// Acontece quando estamos apenas rodando um /api/v1/test
		}
		        
		if (ctx != null && getCtx().getAction().getClass().isAnnotationPresent(Transacional.class))
			this.transacional = true;

		em = criarEntityManager();
		ContextoPersistencia.setEntityManager(em);

		ModeloDao.freeInstance();
		inicializarDao();
		try {
			if (ctx != null
					&& !getCtx().getAction().getClass().isAnnotationPresent(NaoAtualizarCacheDeConfiguracoes.class))
				atualizarCacheDeConfiguracoes();
		} catch (Exception e1) {
			throw new RuntimeException("Não foi possível atualizar o cache de configurações", e1);
		}
		if (this.transacional)
			em.getTransaction().begin();
	}
	
	public void upgradeParaTransacional() {
	    if (em.getTransaction().isActive()) 
			return;
		this.transacional = true;
		em.getTransaction().begin();
	}

	abstract public void atualizarCacheDeConfiguracoes() throws Exception;

	abstract public CpDao inicializarDao();

	abstract public EntityManager criarEntityManager();

	/**
	 * Retorna uma instância de {@link SigaObjects} a partir do Request do
	 * {@link SwaggerServlet}.
	 * 
	 * @throws Exception Se houver algo de errado.
	 */
	public SigaObjects getSigaObjects() throws Exception {
		if (sigaObjects == null)
			sigaObjects = new SigaObjects(getCtx().getRequest());
		return sigaObjects;
	}

	/**
	 * Verifica a presença de um usuário logado e o retorna.
	 * 
	 * @return O login do Usuário na sessão
	 * @throws SwaggerAuthorizationException Se não achar nenhum usuário logado na
	 *                                       sessão.
	 * @see ContextoPersistencia#getUserPrincipal()
	 */
	public static String buscarEValidarUsuarioLogado() throws SwaggerAuthorizationException {
		String userPrincipal = ContextoPersistencia.getUserPrincipal();
		if (isNull(userPrincipal)) {
			throw new SwaggerAuthorizationException("Usuário não está logado");
		}

		return userPrincipal;
	}

	/**
	 * Verifica se o usuário tem acesso ao sistema e ao serviço informado no
	 * parâmetro acesso.
	 * 
	 * @param acesso Caminho do serviço a ser verificado a permissão de acesso
	 * 
	 * @throws Exception Se houver algo de errado.
	 */
	public void assertAcesso(String acesso) throws Exception {
		getSigaObjects().assertAcesso(acesso);
	}
	
	@Override
	public void onTryBegin() throws Exception {
		
		if (!getCtx().getAction().getClass().isAnnotationPresent(AcessoPublico.class)) {
			try {
			    ContextoPersistencia.setUserPrincipal(AutenticadorFabrica.getInstance().obterPrincipal(getCtx().getRequest(), getCtx().getResponse()));
			} catch (Exception e) {
				if (!getCtx().getAction().getClass().isAnnotationPresent(AcessoPublicoEPrivado.class))
					throw new SwaggerAuthorizationException(e.getMessage(), e, null);
			}
		}
		
		//Verifica a conformidade dos parâmetros informados antes de continuar
		checkRequestParams();

		if (ContextoPersistencia.getUserPrincipal() != null)
			assertAcesso("");
	}
	
	@Override
	public void onCatch(Exception e) throws Exception {
		if (em.getTransaction().isActive())
			em.getTransaction().rollback();
		
    	if (e instanceof InvocationTargetException) { 
            Throwable cause = e.getCause();
            if (cause != null && cause instanceof Exception)
                e = (Exception) cause;
    	}
		
		if (!RequestLoggerFilter.isAplicacaoException(e) && !IUnloggedException.class.isAssignableFrom(e.getClass())) {
			RequestLoggerFilter.logException(null, inicio, e);
		}
		ContextoPersistencia.removeAll();
		if (e instanceof RegraNegocioException)
			throw new SwaggerException(e.getMessage(), 400, null, getCtx().getReq(), getCtx().getResp(), null);
		else
			throw e;
	}

	@Override
	public void onFinally() {
		ContextoPersistencia.removeUserPrincipal();
	}

	@Override
	public void close() throws IOException {
		try {
		    if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
				ContextoPersistencia.runAfterCommit();
		    }
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw new RuntimeException(e);
		} finally {
			em.close();
			ContextoPersistencia.setEntityManager(null);
			ContextoPersistencia.removeAll();
		}
	}

	public DpPessoa getCadastrante() throws Exception {
		return getSigaObjects().getCadastrante();
	}

	public DpLotacao getLotaCadastrante() throws Exception {
		return getSigaObjects().getCadastrante().getLotacao();
	}

	public DpPessoa getTitular() throws Exception {
		return getSigaObjects().getTitular();
	}

	public DpLotacao getLotaTitular() throws Exception {
		return getSigaObjects().getLotaTitular();
	}

	public CpIdentidade getIdentidadeCadastrante() throws Exception {
		return getSigaObjects().getIdentidadeCadastrante();
	}

	private void checkRequestParams() throws Exception {
        Class<?> classe = getCtx().getReq().getClass();      
        Field[] campos = classe.getDeclaredFields();  
        
        boolean permissiveCheckControl = getCtx().getAction().getClass().isAnnotationPresent(RequestParamsPermissiveCheck.class);
        try {
	        for (Field campo : campos) {  
	        	campo.setAccessible(true); 
	        	
				/*URLs permissivas aplica apenas o Clean de acordo com a SafeList */
				String dirtyParam = campo.get(getCtx().getReq()) instanceof String ? 
						(String) campo.get(getCtx().getReq()) 
						: campo.get(getCtx().getReq()).toString();	
	        	
	        	
	        	if (permissiveCheckControl) {
	        		if (!"".equals(dirtyParam) && RequestParamsCheck.hasHTMLTags(dirtyParam)) {
	        			
						//Ajusta saída do Clean
	        			OutputSettings outputSettings = RequestParamsCheck.buildOutputSettings();
	
						//Preserve Comments
	        			dirtyParam = RequestParamsCheck.replaceCommentTag(dirtyParam);	
						
						/*URLs permissivas aplica apenas o Clean de acordo com a SafeList */
						String cleanParam = Jsoup.clean(dirtyParam, "", SafeListCustom.relaxedCustom(),outputSettings);
						
						//Restore Comments
						cleanParam = RequestParamsCheck.restoreCommentTag(cleanParam);	
						
						//Devolve param safe
						campo.set(getCtx().getReq(),cleanParam);
						
						//Por hora, registra alterações para verificar possíveis ajustes na Safelist e CKEDITOR.config.disallowedContent
						if (!Jsoup.parseBodyFragment(dirtyParam, "").outputSettings(outputSettings).body().html().equals(cleanParam)) {
							logXss(dirtyParam,permissiveCheckControl); 
						}
						
	        		}
					
	        	} else {
					if (!RequestParamsCheck.checkParameter(dirtyParam,permissiveCheckControl)) {
						logXss(dirtyParam,permissiveCheckControl);
						throw new SwaggerException("Conteúdo inválido. Por favor, revise os valores fornecidos.", 400, null, getCtx().getReq(), getCtx().getResp(), null);
					}	        		
	        	}	
	        	
	        	dirtyParam= null;	

	        }
        } catch (SwaggerException e) {
        	throw e;	
        } catch (Exception e) {
        	// Não bloqueia caso não consiga verificar os parametros passados
        }
	}
	
    private void logXss(Object param, boolean permissive) {
    	String siglaCadastrante;
    	try {
    		siglaCadastrante = getLotaCadastrante() .getSigla() + "/" + getCadastrante().getSigla();
		} catch (Exception e) {
			siglaCadastrante = "Não identificado";
		}
    	logger.log(BLAME, (permissive ? "[Clean Param]" : "[Detectado XSS]") + " - Request: {}; Param XSS: {}; IP de Origem: {}; Usuário: {};", 
    			SwaggerServlet.getHttpServletRequest(), 
    			param, 
    			HttpRequestUtils.getIpAudit(SwaggerServlet.getHttpServletRequest()), 
    			siglaCadastrante);
    }
	
}
