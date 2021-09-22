package br.gov.jfrj.siga.ex.api.v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.context.AcessoPublico;
import br.gov.jfrj.siga.ex.api.v1.IExApiV1.IDownloadJwtFilenameGet;

@AcessoPublico
public class DownloadJwtFilenameGet implements IDownloadJwtFilenameGet {

	@Override
	public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		Map<String, Object> map = verify(req.jwt);

		// String principal = (String) map.get("principal");
		String uuid = (String) map.get("uuid");
		String doc = (String) map.get("doc");
		String filename = (String) map.get("fname");
		String kind = (String) map.get("knd");
		String contenttype = (String) map.get("typ");
		String disposition = "attachment".equals(req.disposition) ? "attachment" : "inline";
		if (!"download".equals(kind))
			throw new Exception("Tipo de token JWT inválido");

		// PDF Completo
		String bufName = DownloadAssincrono.getBufName(uuid, contenttype, doc);

		if (filename != null)
			resp.contentdisposition = disposition + ";filename=" + filename;
		else
			resp.contentdisposition = disposition + ";filename=" + doc + "-completo."
					+ ("application/pdf".equals(contenttype) ? "pdf" : "html");
		resp.contentlength = (long) new File(bufName).length();
		resp.contenttype = contenttype;
		resp.inputstream = new FileInputStream(bufName);
	}

	@Override
	public String getContext() {
		return "obter arquivo";
	}

	public static Map<String, Object> verify(String jwt) throws InvalidKeyException, NoSuchAlgorithmException,
			IllegalStateException, SignatureException, IOException, JWTVerifyException {
		final JWTVerifier verifier = new JWTVerifier(getPassword());
		Map<String, Object> map;
		map = verifier.verify(jwt);
		return map;
	}

	public static String jwt(String principal, String uuid, String doc, String contenttype, String filename)
			throws Exception {
		final long iat = System.currentTimeMillis() / 1000L; // issued at claim
		// token expires in 12h
		final long exp = iat + 12 * 60 * 60L;

		String password = getPassword();
		final JWTSigner signer = new JWTSigner(password);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("iss", getIssuer());
		claims.put("exp", exp);
		claims.put("iat", iat);

		if (principal != null)
			claims.put("principal", principal);
		if (uuid != null)
			claims.put("uuid", uuid);
		if (doc != null)
			claims.put("doc", doc);
		if (filename != null)
			claims.put("fname", filename);
		claims.put("typ", contenttype);
		claims.put("knd", "download");

		final String jwt = signer.sign(claims);
		return jwt;
	}

	private static String getIssuer() {
		return "siga-ex-api";
	}

	private static String getPassword() {
		return Prop.get("/siga.jwt.secret");
	}

}
