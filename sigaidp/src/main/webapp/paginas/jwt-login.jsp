<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

<siga:pagina titulo="Página de Login" desabilitarbusca="sim"
	desabilitarmenu="sim"
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

	<div class="container content pt-5">
		<div class="row justify-content-center">
			<div class="col col-sm-12 col-md-6">
				<div class="jumbotron d-block mx-auto">
					<h2 class="text-center pb-3">Autentica&ccedil;&atilde;o</h2>

					<c:if test="${not empty mensagem}">
						<div class="login-invalido">
							<div class="login-invalido-titulo">
								<p class="alert alert-danger">${mensagem}</p>
							</div>

							<div class="login-invalido-descricao">
								<p>XX é a sigla do seu órgão (T2, RJ, ES, etc.)</p>
								<p>99999 ￩ o número da matrícula.</p>
							</div>
						</div>
					</c:if>

					<!---->
					<form role="form" method="post"
						enctype="application/x-www-form-urlencoded">
						<div class="form-group">
							<label for="username">Usu&aacute;rio</label> <input id="username"
								type="text" name="username" placeholder="XX99999"
								onblur="javascript:converteUsuario(this)" autocorrect="off"
								autocapitalize="none" class="form-control"">
						</div>
						<div class="form-group">
							<label for="password">Senha</label> <input type="password"
								name="password" id="password" placeholder="Senha"
								class="form-control">
						</div>
						<div class="row pt-3">
							<div class="col">
								<div class="text-center">
									<input type="submit" class="btn btn-primary" value="Enviar"></input>
									<div class="mt-4">
										<a href="/siga/public/app/usuario/incluir_usuario"
											class="btn btn-secondary">Sou um novo usu&aacute;rio</a> <a
											href="/siga/public/app/usuario/esqueci_senha"
											class="btn btn-secondary">Esqueci minha senha</a>
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
		$('input, textarea').placeholder();
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
</siga:pagina>
