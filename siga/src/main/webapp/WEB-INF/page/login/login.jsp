<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<siga:pagina titulo="Página de Login" desabilitarbusca="sim"
	desabilitarmenu="sim"
	incluirJs="/siga/javascript/jquery.placeholder.js">

	<script type="text/javascript">
		/*  converte para maiï¿ºscula a sigla do estado  */
		function converteUsuario(nomeusuario) {
			tmp = nomeusuario.value;
			nomeusuario.value = tmp.toUpperCase();
		}
	</script>
	
	
	<c:choose>
	<c:when test="${siga_cliente == 'GOVSP'}">
		<c:set var="login_box_class" value="box_login" />
		<c:set var="login_box_logo" value="/siga/imagens/logo-sem-papel-cor.png" />
		<c:set var="login_box_logo_size" value="150" />
		<c:set var="login_box_text" value="" />
	</c:when>
	<c:otherwise>
		<c:set var="login_box_class" value="" />
		<c:set var="login_box_logo" value="" />
		<c:set var="login_box_logo_size" value="" />
		<c:set var="login_box_text" value="<fmt:message key='usuario.login.formulario' />" />
	</c:otherwise>
</c:choose>

	<div class="container content pt-2">
		<div class="row justify-content-center">
			<div class="col col-sm-12 col-md-5">
				<div class="jumbotron d-block mx-auto ${login_box_class}">

					<div class="text-center">
						<img alt="" src="${login_box_logo}" width="${login_box_logo_size}" align="center"/>
					</div>
					
					<h2 class="text-center pb-1 pt-2">${login_box_text}</h2>

					<c:if test="${not empty loginMensagem}">
						<div class="login-invalido ">
							<div class="login-invalido-titulo ${hide_only_GOVSP}">
								<p class="alert alert-danger ">${loginMensagem}</p>
							</div>

							<div class="login-invalido-descricao ">	
								<div class="${hide_only_GOVSP}">
									<p class="alert alert-danger">${f:resource('siga.gi.texto.login')}</p>
								</div>														
								<p class="alert alert-danger ${hide_only_TRF2}">${loginMensagem}</p>
							</div>
						</div>
					</c:if>

					<!---->
					<form role="form" method="post"
						enctype="application/x-www-form-urlencoded">
						<div class="form-group">
							<label for="username"><fmt:message key="usuario.matricula"/></label> 
					
						    <div class="input-group">
						      <div class="input-group-prepend">
						        <span class="input-group-text" id="icon-user"><i class="fas fa-user"></i></span>
						      </div>
						      <input id="username" type="text" name="username" placeholder="<fmt:message key="usuario.digite.usuario"/>" onblur="javascript:converteUsuario(this)" autocorrect="off"
								autocapitalize="none" class="form-control" aria-label="Usuário" aria-describedby="icon-user">
						    </div>
	
						</div>
						<div class="form-group">
							<label for="password">Senha</label>
							<div class="input-group">
						      <div class="input-group-prepend">
						        <span class="input-group-text" id="icon-pass"><i class="fas fa-lock"></i></span>
						      </div>
						      <input type="password" name="password" id="password" placeholder="Senha"
								class="form-control" aria-label="Usuário" aria-describedby="icon-pass">
						    </div>
						
						</div>
						<div class="row pt-3">
							<div class="col">
								<div class="text-center">
									<button type="submit" class="btn btn-lg btn-primary btn-block"><i class="fas fa-sign-in-alt"></i> Entrar</button>
									
									<hr class="my-4">
									<div class="mt-4">
										<c:if test="${siga_cliente != 'GOVSP'}">
											<a href="/siga/public/app/usuario/incluir_usuario"
												class="btn btn-secondary btn-block mb-2"><fmt:message key = "usuario.sounovo"/></a> 
										</c:if>
										<a href="/siga/public/app/usuario/esqueci_senha"
										class="btn btn-secondary btn-block">Esqueci minha senha</a>
									</div>
									<div class="mt-3">
									    <div class="d-flex justify-content-between">
									    	   	<div>
									    		Versão: ${versao}
									    	    </div>
									    	    <div>
									    		<a class="text-top" href="http://www-desen4.jf.trf2.gov.br/portal/sistemas-open-source-disponibilizados-pelo-trf2-vii/" target="_blank" class="btn btn-link">sobre</a> 
									    	    </div>
									    </div>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		//$('input, textarea').placeholder();
		$("#username").focus();

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
	</script>
	<script src="../../javascript/service-worker.js" async></script>
</siga:pagina>
