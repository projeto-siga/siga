package br.gov.jfrj.siga.vraptor;

import java.util.HashMap;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.JWTSigner;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.dp.dao.CpDao;

@Controller
public class SeccController extends SigaController {
	
	/**
	 * @deprecated CDI eyes only
	 */
	public SeccController() {
		super();
	}
	
	static final String PROVIDER_ISSUER = "secret";

	@Inject
	public SeccController(HttpServletRequest request, Result result, CpDao dao,
			SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}
	
	@Get("app/secc/acesso")
	public void modulo() {
		String cookieDomain = Prop.get("/siga.jwt.cookie.domain");
		final JWTSigner signer = new JWTSigner(Prop.get("/secc.jwt.secret"));
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		final long iat = System.currentTimeMillis() / 1000L;
		
		final long exp = iat + (60 * 60L); 
		claims.put("sub", getTitular().getSigla());
		claims.put("nome", getTitular().getNomePessoa());
		claims.put("aud", cookieDomain);
		claims.put("nbf", iat);
		claims.put("lota", getLotaTitular().getSigla());
		claims.put("exp", exp);
		claims.put("iat", iat);
		String token = signer.sign(claims);
		
		result.redirectTo(Prop.get("/secc.ui.url") + token); 
		
	}
	
}