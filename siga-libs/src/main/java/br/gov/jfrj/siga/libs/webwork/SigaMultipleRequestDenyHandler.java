package br.gov.jfrj.siga.libs.webwork;

import java.security.MessageDigest;

import javax.servlet.http.HttpServletRequest;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Criptografia;

import com.opensymphony.webwork.WebWorkStatics;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;

public class SigaMultipleRequestDenyHandler implements Interceptor {
	Cache cache;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		String cRegion = "multiple-request-deny";
		cache = CacheManager.getInstance().getCache(cRegion);
		if (cache == null) {
			CacheManager manager = CacheManager.getInstance();
			manager.addCache(cRegion);
			cache = manager.getCache(cRegion);
			CacheConfiguration config;
			config = cache.getCacheConfiguration();
			config.setEternal(false);
			config.setTimeToLiveSeconds(60);
			config.setTimeToIdleSeconds(0);
			config.setMaxElementsInMemory(1000);
			config.setOverflowToDisk(false);
			config.setMaxElementsOnDisk(0);
		}
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = (HttpServletRequest) invocation
				.getInvocationContext().get(WebWorkStatics.HTTP_REQUEST);

		byte ba[] = null;

		
		if (request.getQueryString() == null
				|| request.getUserPrincipal() == null
				|| request.getUserPrincipal().getName() == null) {
			return invocation.invoke();
		}
		
		// Monta o md5 do request
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		final HttpServletRequest httpServletRequest = request;
		md5.update(httpServletRequest.getRequestURI().getBytes());
		md5.update(httpServletRequest.getQueryString().getBytes());
		md5.update(httpServletRequest.getUserPrincipal().getName().getBytes());
		ba = md5.digest();

		String key = Criptografia.asHex(ba);

		// Verifica se já está no cache e exibe erro se estiver
		if (cache.get(key) != null) {
			throw new AplicacaoException(
					"Múltiplas solicitações para a mesma página.",
					0,
					new AplicacaoException(
							"Para evitar este erro, não repita uma operação em um curto espaço de tempo. Por favor, aguarde um minuto e tente novamente."));
		}

		try {
			// Coloca no cache
			cache.put(new Element(key, "processando..."));

			return invocation.invoke();
		} finally {
			// Retira do cache
			cache.remove(key);
		}
	}
}
