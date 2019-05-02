<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>



<siga:pagina titulo="P�gina de Login" desabilitarbusca="sim"
	desabilitarmenu="sim"
	incluirJs="/siga/javascript/jquery.placeholder.js">
	
	<c:if test="${siga_cliente == 'GOVSP'}">
		<c:redirect url="../../siga"/>
	</c:if>


	<script type="text/javascript">
		/*  converte para maiúscula a sigla do estado  */
		function converteUsuario(nomeusuario) {
			re = /^[a-zA-Z]{2}\d{3,6}$/;
			ret2 = /^[a-zA-Z]{1}\d{3,6}$/;
			tmp = nomeusuario.value;
			if (tmp.match(re) || tmp.match(ret2)) {
				nomeusuario.value = tmp.toUpperCase();
			}
		}
	</script>

	<c:set var="pagina" scope="session">${pageContext.request.requestURL}</c:set>

	<div class="container-fluid content">
		<div class="row">
			<!-- sidebar -->
		
			<div class="col col-12 col-md-4 order-md-2 mt-3">
				<!-- login form head -->
				<div class="gt-mylogin-hd">Identificação </div>

				<!-- login box -->
				<div class="gt-mylogin-box" style="margin-bottom: 0">
					<!-- login form -->
					<form method="post" action="j_security_check"
						enctype="application/x-www-form-urlencoded" class="gt-form">
						<!-- form row -->
						<div class="gt-form-row">
							<label class="gt-label">Matrícula</label> <input id="j_username"
								type="text" name="j_username" placeholder="XX99999"
								onblur="javascript:converteUsuario(this)" class="gt-form-text">
						</div>
						<!-- /form row -->

						<!-- form row -->
						<div class="gt-form-row">
							<label class="gt-label">Senha</label> <input type="password"
								name="j_password" class="gt-form-text">
						</div>
						<!-- /form row -->

						<!-- form row -->
						<div class="gt-form-row">
							<input type="submit" value="Acessar"
								class="gt-btn-medium gt-btn-right">
						</div>
						<!-- /form row -->

						<p class="gt-forgot-password">
							<a href="/siga/app/usuario/incluir_usuario">Sou um novo
								usuário</a>
						</p>
						<p class="gt-forgot-password">
							<a href="/siga/app/usuario/esqueci_senha">Esqueci minha senha</a>
						</p>
					</form>
					<!-- /login form -->
				</div>
				<!-- /login box -->

				<!-- Sidebar Navigation -->
				<div
					class="gt-sidebar-nav gt-sidebar-nav-blue d-none d-md-block mt-3">
					<h3>Links Úteis</h3>
					<ul>
						<li><a href="/siga/arquivos/apostila_sigaex.pdf">Apostila
								SIGA-Doc</a></li>
						<li><a href="/siga/arquivos/apostila_sigawf.pdf">Apostila
								SIGA-Workflow</a></li>
					</ul>
				</div>
				<!-- /Sidebar Navigation -->
				<!-- Sidebar Content -->
			</div>
			<!-- / sidebar -->


			<!-- main content -->
			<div id="gc-ancora" class="col col-12 col-md-8 order-md-1 mt-3">
				<c:choose>
					<c:when test="${f:resource('siga.gc.paginadelogin')}">
						<c:url var="url" value="/../sigagc/publicKnowledge">
							<c:param name="tags">^pagina-de-login</c:param>
							<c:param name="estilo">inplace</c:param>
							<c:param name="msgvazio">Ainda não existem informações para serem exibidas aqui. Por favor, clique <a
									href="$1">aqui</a> para contribuir.</c:param>
							<c:param name="titulo">Página de Login</c:param>
							<c:param name="ts">${currentTimeMillis}</c:param>
						</c:url>
						<script type="text/javascript">
							SetInnerHTMLFromAjaxResponse("${url}", document
									.getElementById('gc-ancora'));
						</script>
					</c:when>
					<c:otherwise>
						<c:import url="comentario.jsp" />
					</c:otherwise>
				</c:choose>
				<h4>Versão: ${siga.versao}</h4>
			</div>
			<!-- / main content -->
		</div>
	</div>
	<script type="text/javascript">
		$('input, textarea').placeholder();
		$("#j_username").focus();

		function getCookie(cname) {
			var name = cname + "=";
			var ca = document.cookie.split(';');
			for (var i = 0; i < ca.length; i++) {
				var c = ca[i];
				while (c.charAt(0) == ' ')
					c = c.substring(1);
				if (c.indexOf(name) == 0)
					return c.substring(name.length, c.length);
			}
			return "";
		}

		var idp = getCookie("JSESSIONID").replace(/"/g, "");
		if (idp != null && idp != "undefined")
			sessionStorage.setItem("idp", idp);
	</script>
</siga:pagina>
