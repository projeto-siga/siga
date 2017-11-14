package br.gov.jfrj.siga.idp.jwt.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.picketlink.common.util.Base64;

import br.gov.jfrj.siga.idp.jwt.SigaJwtProviderException;

/**
 * Servlet para troca de tokens JWT. Ainda não foi utilizado o swagger, pois as bibliotecas do siga são incompatíveis com a versão atual.
 * @author kpf
 *
 */
public class SigaJwtProviderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SigaJwtBL instance;

	public SigaJwtProviderServlet() throws SigaJwtProviderException {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		SigaJwtBL jwtBL;
		try {
			jwtBL = SigaJwtBL.getInstance();
		} catch (SigaJwtProviderException e) {
			throw new ServletException("Erro ao iniciar o provider", e);
		}
		String result = null;
		if(request.getPathInfo().endsWith("/login")){
			String b64 = extrairPayload(request);
			String[] auth = new String(Base64.decode(b64)).split(":");
			
			String[] usuarioELotacao = auth[0].split("@");
			String usuario = usuarioELotacao[0];
			String lotacao = usuarioELotacao.length>1?usuarioELotacao[1]:null;
			
			String senha = auth[1];
			String permissoes = null;
			
			String body = request.getReader().readLine();
			if(body != null){
				permissoes = new JSONObject(body).getString("perm");
			}
			
			result = jwtBL.login(usuario,lotacao,senha,body,permissoes);
			
		}else if(request.getPathInfo().endsWith("/validar")){
			String token = extrairPayload(request);;
			result = jwtBL.validar(token);
		}
		
		response.setContentType("text/plain");
	    PrintWriter out = response.getWriter();
	    out.println(result);

		
	}

	private String extrairPayload(HttpServletRequest request) {
		return request.getHeader("Authorization").replaceAll(".* ", "").trim();
	}

}
