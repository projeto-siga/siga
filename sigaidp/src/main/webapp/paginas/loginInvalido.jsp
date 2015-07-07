<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

<siga:pagina titulo="Login Inválido" desabilitarbusca="sim"
	incluirJs="/siga/javascript/jquery.placeholder.js">

	<script type="text/javascript">
		/*  converte para mai￺scula a sigla do estado  */
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

	<div class="gt-bd gt-cols clearfix">

		<!-- main content -->
		<div id="gc-ancora" class="gt-content">
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

		<!-- sidebar -->
		<div class="gt-sidebar">
			<!-- login form head -->
			<div class="gt-mylogin-hd">Identificação</div>

			<!-- login box -->
			<div class="gt-mylogin-box">
				<!-- login form -->
				<form method="post" action="j_security_check"
					enctype="application/x-www-form-urlencoded" class="gt-form">
					<div class="login-invalido">
						<div class="login-invalido-titulo">
							<p>Login e/ou senha incorretos!</p>
						</div>

						<div class="login-invalido-descricao">
							<p>XX ￩ a sigla do seu órgão (T2, RJ, ES, etc.)</p>
							<p>99999 ￩ o número da matrícula.</p>
						</div>
					</div>
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
			<div class="gt-sidebar-nav gt-sidebar-nav-blue">
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
	</div>
	<script type="text/javascript">
		$('input, textarea').placeholder();
		$("#j_username").focus();
	</script>
</siga:pagina>