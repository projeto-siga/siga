package br.gov.jfrj.siga.tp.interceptor;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.gov.jfrj.siga.tp.auth.AutorizacaoGI;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdmin;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminFrota;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminGabinete;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissao;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissaoComplexo;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAgente;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAprovador;
import br.gov.jfrj.siga.tp.auth.annotation.RoleGabinete;

/**
 * Interceptor responsavel por verificar se o usuario tem permissao para acessar determinada URL (metodo) do controller. 
 * Verifica se o metodo possui alguma das anotacoes de validacao, caso possua, osistema executa a regra de verificacao 
 * da permissao. Se o usuario nao possui acesso, o sistema lanca excecao informando 
 * o acesso negado.
 * 
 * @author db1
 *
 */
@RequestScoped
@Intercepts(after = { PreencherAutorizacaoGIInterceptor.class }, before = MotivoLogInterceptor.class)
public class AutorizacaoAcessoInterceptor implements Interceptor {

	private AutorizacaoGI autorizacaoGI;

	public AutorizacaoAcessoInterceptor(AutorizacaoGI dadosAutorizacao) {
		this.autorizacaoGI = dadosAutorizacao;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) throws InterceptionException {
		try {
			DadosValidacaoAutorizacao dados = new DadosValidacaoAutorizacao(method);

			this.validarAdmin(dados);
			this.validarAprovador(dados);
			this.validarAgente(dados);
			this.validarGabinete(dados);
			this.validarAdminGabinete(dados);
			this.validarAdminFrota(dados);
			this.validarAdminMissao(dados);
			this.validarAdminMissaoComplexo(dados);

			stack.next(method, resourceInstance);
		} catch (Exception e) {
			throw new InterceptionException(e);
		}
	}

	private void validarAdminMissaoComplexo(DadosValidacaoAutorizacao dados) throws Exception {
		if (dados.isAdmMissaoComplexoAnnotation()) {
			if (!autorizacaoGI.ehAdministradorMissaoPorComplexo() 
					&& !dados.isAdminAnnotation() 
					&& !dados.isGabineteAnnotation() 
					&& !dados.isAprovadorAnnotation() 
					&& !dados.isAgenteAnnotation()
					&& !dados.isAdminGabineteAnnotation() 
					&& !dados.isAdmFrotaAnnotation() 
					&& !dados.isAdmMissaoAnnotation()) {
				throw new Exception("admMissaoComplexoAnnotation.exception");
			}
			if (!autorizacaoGI.ehAdministradorMissaoPorComplexo())
				dados.setAdmMissaoComplexoAnnotation(false);
		}
	}

	private void validarAdminMissao(DadosValidacaoAutorizacao dados) throws Exception {
		if (dados.isAdmMissaoAnnotation()) {
			if (!autorizacaoGI.ehAdministradorMissao() 
					&& !dados.isAdminAnnotation() 
					&& !dados.isGabineteAnnotation() 
					&& !dados.isAprovadorAnnotation() 
					&& !dados.isAgenteAnnotation()
					&& !dados.isAdminGabineteAnnotation()
					&& !dados.isAdmFrotaAnnotation() 
					&& !dados.isAdmMissaoComplexoAnnotation()) {
				throw new Exception("admMissaoAnnotation.exception");
			}

			if (!autorizacaoGI.ehAdministradorMissao())
				dados.setAdmMissaoAnnotation(false);
		}
	}

	private void validarAdminFrota(DadosValidacaoAutorizacao dados) throws Exception {
		if (dados.isAdmFrotaAnnotation()) {
			if (!autorizacaoGI.ehAdministradorFrota() 
					&& !dados.isAdminAnnotation() 
					&& !dados.isGabineteAnnotation() 
					&& !dados.isAprovadorAnnotation() 
					&& !dados.isAgenteAnnotation()
					&& !dados.isAdminGabineteAnnotation() 
					&& !dados.isAdmMissaoAnnotation() 
					&& !dados.isAdmMissaoComplexoAnnotation()) {
				throw new Exception("admFrotaAnnotation.exception");
			}

			if (!autorizacaoGI.ehAdministradorFrota())
				dados.setAdmFrotaAnnotation(false);
		}
	}

	private void validarAdminGabinete(DadosValidacaoAutorizacao dados) throws Exception {
		if (dados.isAdminGabineteAnnotation()) {
			if (!autorizacaoGI.ehAdminGabinete() 
					&& !dados.isAdminAnnotation() 
					&& !dados.isGabineteAnnotation() 
					&& !dados.isAprovadorAnnotation() 
					&& !dados.isAgenteAnnotation()
					&& !dados.isAdmFrotaAnnotation() 
					&& !dados.isAdmMissaoAnnotation() 
					&& !dados.isAdmMissaoComplexoAnnotation()) {
				throw new Exception("adminGabineteAnnotation.exception");
			}

			if (!autorizacaoGI.ehAdminGabinete())
				dados.setAdminGabineteAnnotation(false);
		}
	}

	private void validarGabinete(DadosValidacaoAutorizacao dados) throws Exception {
		if (dados.isGabineteAnnotation()) {
			if (!autorizacaoGI.ehGabinete() 
					&& !dados.isAdminAnnotation() 
					&& !dados.isAprovadorAnnotation() 
					&& !dados.isAdminGabineteAnnotation() 
					&& !dados.isAgenteAnnotation()
					&& !dados.isAdmFrotaAnnotation() 
					&& !dados.isAdmMissaoAnnotation() 
					&& !dados.isAdmMissaoComplexoAnnotation()) {
				throw new Exception("gabineteAnnotation.exception");
			}

			if (!autorizacaoGI.ehGabinete())
				dados.setGabineteAnnotation(false);
		}
	}

	private void validarAgente(DadosValidacaoAutorizacao dados) throws Exception {
		if (dados.isAgenteAnnotation()) {
			if (!autorizacaoGI.ehAgente() 
					&& !dados.isAdminAnnotation() 
					&& !dados.isGabineteAnnotation() 
					&& !dados.isAdminGabineteAnnotation() 
					&& !dados.isAprovadorAnnotation()
					&& !dados.isAdmFrotaAnnotation() 
					&& !dados.isAdmMissaoAnnotation() 
					&& !dados.isAdmMissaoComplexoAnnotation()) {
				throw new Exception("agenteAnnotation.exception");
			}

			if (!autorizacaoGI.ehAgente())
				dados.setAgenteAnnotation(false);
		}
	}

	private void validarAprovador(DadosValidacaoAutorizacao dados) throws Exception {
		if (dados.isAprovadorAnnotation()) {
			if (!autorizacaoGI.ehAprovador() 
					&& !dados.isAdminAnnotation() 
					&& !dados.isGabineteAnnotation() 
					&& !dados.isAdminGabineteAnnotation() 
					&& !dados.isAgenteAnnotation()
					&& !dados.isAdmFrotaAnnotation() 
					&& !dados.isAdmMissaoAnnotation() 
					&& !dados.isAdmMissaoComplexoAnnotation()) {
				throw new Exception("aprovadorAnnotation.exception");
			}

			if (!autorizacaoGI.ehAprovador())
				dados.setAprovadorAnnotation(false);
		}
	}

	private void validarAdmin(DadosValidacaoAutorizacao dados) throws Exception {
		if (dados.isAdminAnnotation()) {
			if (!autorizacaoGI.ehAdministrador() 
					&& !dados.isAprovadorAnnotation() 
					&& !dados.isGabineteAnnotation() 
					&& !dados.isAdminGabineteAnnotation() 
					&& !dados.isAgenteAnnotation()
					&& !dados.isAdmFrotaAnnotation() 
					&& !dados.isAdmMissaoAnnotation() 
					&& !dados.isAdmMissaoComplexoAnnotation()) {
				throw new Exception("adminAnnotation.exception");
			}

			if (!autorizacaoGI.ehAdministrador())
				dados.setAdminAnnotation(Boolean.FALSE);
		}
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return Boolean.TRUE;
	}

	class DadosValidacaoAutorizacao {
		boolean adminAnnotation;
		boolean aprovadorAnnotation;
		boolean gabineteAnnotation;
		boolean adminGabineteAnnotation;
		boolean agenteAnnotation;
		boolean admFrotaAnnotation;
		boolean admMissaoAnnotation;
		boolean admMissaoComplexoAnnotation;

		public DadosValidacaoAutorizacao(ResourceMethod method) {
			this.adminAnnotation = method.containsAnnotation(RoleAdmin.class);
			this.aprovadorAnnotation = method.containsAnnotation(RoleAprovador.class);
			this.gabineteAnnotation = method.containsAnnotation(RoleGabinete.class);
			this.adminGabineteAnnotation = method.containsAnnotation(RoleAdminGabinete.class);
			this.agenteAnnotation = method.containsAnnotation(RoleAgente.class);
			this.admFrotaAnnotation = method.containsAnnotation(RoleAdminFrota.class);
			this.admMissaoAnnotation = method.containsAnnotation(RoleAdminMissao.class);
			this.admMissaoComplexoAnnotation = method.containsAnnotation(RoleAdminMissaoComplexo.class);
		}

		public boolean isAdminAnnotation() {
			return adminAnnotation;
		}

		public boolean isAprovadorAnnotation() {
			return aprovadorAnnotation;
		}

		public boolean isGabineteAnnotation() {
			return gabineteAnnotation;
		}

		public boolean isAdminGabineteAnnotation() {
			return adminGabineteAnnotation;
		}

		public boolean isAgenteAnnotation() {
			return agenteAnnotation;
		}

		public boolean isAdmFrotaAnnotation() {
			return admFrotaAnnotation;
		}

		public boolean isAdmMissaoAnnotation() {
			return admMissaoAnnotation;
		}

		public boolean isAdmMissaoComplexoAnnotation() {
			return admMissaoComplexoAnnotation;
		}

		public void setAdminAnnotation(boolean adminAnnotation) {
			this.adminAnnotation = adminAnnotation;
		}

		public void setAprovadorAnnotation(boolean aprovadorAnnotation) {
			this.aprovadorAnnotation = aprovadorAnnotation;
		}

		public void setGabineteAnnotation(boolean gabineteAnnotation) {
			this.gabineteAnnotation = gabineteAnnotation;
		}

		public void setAdminGabineteAnnotation(boolean adminGabineteAnnotation) {
			this.adminGabineteAnnotation = adminGabineteAnnotation;
		}

		public void setAgenteAnnotation(boolean agenteAnnotation) {
			this.agenteAnnotation = agenteAnnotation;
		}

		public void setAdmFrotaAnnotation(boolean admFrotaAnnotation) {
			this.admFrotaAnnotation = admFrotaAnnotation;
		}

		public void setAdmMissaoAnnotation(boolean admMissaoAnnotation) {
			this.admMissaoAnnotation = admMissaoAnnotation;
		}

		public void setAdmMissaoComplexoAnnotation(boolean admMissaoComplexoAnnotation) {
			this.admMissaoComplexoAnnotation = admMissaoComplexoAnnotation;
		}
	}
}