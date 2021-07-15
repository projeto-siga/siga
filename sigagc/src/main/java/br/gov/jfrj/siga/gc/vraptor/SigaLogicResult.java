package br.gov.jfrj.siga.gc.vraptor;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.enterprise.inject.Specializes;
import javax.enterprise.inject.Typed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.http.MutableRequest;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.interceptor.TypeNameExtractor;
import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.proxy.MethodInvocation;
import br.com.caelum.vraptor.proxy.Proxifier;
import br.com.caelum.vraptor.proxy.ProxyInvocationException;
import br.com.caelum.vraptor.proxy.SuperMethod;
import br.com.caelum.vraptor.view.DefaultLogicResult;
import br.com.caelum.vraptor.view.FlashScope;
import br.gov.jfrj.siga.vraptor.PathResolver;

/**
 * The default implementation of LogicResult.<br>
 * Uses cglib to provide proxies for client side redirect (url creation).
 *
 * @author Guilherme Silveira
 */
@Specializes
public class SigaLogicResult extends DefaultLogicResult {

	private static final Logger logger = LoggerFactory.getLogger(DefaultLogicResult.class);

	private final Proxifier proxifier;
	private final Router router;
	private final MutableRequest request;
	private final HttpServletResponse response;


	/** 
	 * @deprecated CDI eyes only
	 */
	protected SigaLogicResult() {
		this(null, null, null, null, null, null, null, null, null);
	}

	@Inject
	public SigaLogicResult(Proxifier proxifier, Router router, MutableRequest request, HttpServletResponse response,
			Container container, PathResolver resolver, TypeNameExtractor extractor, FlashScope flash, MethodInfo methodInfo) {
		super(proxifier,router,request,response,container,resolver,extractor,flash,methodInfo);
		this.proxifier = proxifier;
		this.router = router;
		this.request = request;
		this.response = response;
	}
	

	public <T> T getRedirectURL(final StringBuilder sb, final Class<T> type) {
		return proxifier.proxify(type, new MethodInvocation<T>() {
			public Object intercept(T proxy, Method method, Object[] args, SuperMethod superMethod) {
				if (!acceptsHttpGet(method)) {
					throw new IllegalArgumentException(
							"Your logic method must accept HTTP GET method if you want to redirect to it");
				}
				String url = router.urlFor(type, method, args);
				String path = request.getContextPath() + url;
				// includeParametersInFlash(type, method, args);

				//Nato: inseri essas duas linhas para corrigir um problema de codepage no redirecionamento
				response.setContentType("text/html; charset=UTF-8");
//				path = new String(Charset.forName("UTF-8").encode(path).array());
				
				sb.setLength(0);
				sb.append(path);
				return null;
			}
		});
	}


}
