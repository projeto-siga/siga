package br.gov.jfrj.siga.vraptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.DefaultResult;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.http.FormatResolver;
import br.com.caelum.vraptor.interceptor.ExecuteMethodInterceptor;
import br.com.caelum.vraptor.interceptor.ForwardToDefaultViewInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.view.Results;

/**
 * Intercepts the request and forwards to the default view if no view was
 * rendered so far. If the request has included parameters and the header says
 * the client supports json, it will be forwarded to a serializer result.
 *
 * @author Renato Crivano
 */
@Intercepts(after = ExecuteMethodInterceptor.class, before = { ForwardToDefaultViewInterceptor.class })
public class SigaDefaultViewInterceptor implements Interceptor {
	private final Result result;
	private FormatResolver formatResolver;

	private static final Logger logger = LoggerFactory
			.getLogger(SigaDefaultViewInterceptor.class);

	public SigaDefaultViewInterceptor(Result result,
			FormatResolver formatResolver) {
		this.result = result;
		this.formatResolver = formatResolver;
	}

	public boolean accepts(ResourceMethod method) {
		return true;
	}

	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		if (!result.used() && result.included().size() > 0
				&& ("json".equals(formatResolver.getAcceptFormat()))) {
			logger.debug("forwarding to the json serializer for this logic");

			result.use(Results.json()).indented().withoutRoot()
					.from(new IncludedParameters(result.included()))
					.serialize();
			;
			// NoRootSerialization serializer =
			// result.use(Results.json()).indented().withoutRoot();
			// for (String key : result.included().keySet()) {
			// serializer.from(result.included().get(key)).serialize();
			// }

		}
		stack.next(method, resourceInstance);
	}

}