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
			<c:set var="login_box_logo_size" value="132" />
			<c:set var="login_box_text" value="" />
			<c:set var="login_titulo_modal" value="SP Sem Papel"/>
		</c:when>
		<c:otherwise>
			<c:set var="login_box_class" value="" />
			<c:set var="login_box_logo" value="" />
			<c:set var="login_box_logo_size" value="" />
			<c:set var="login_box_text" value="<fmt:message key='usuario.login.formulario' />" />
			<c:set var="login_titulo_modal" value="Siga"/>
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

							<div class="login-invalido-descricao ">	
								<p class="alert alert-danger">${loginMensagem}</p>
							</div>
						</div>
					</c:if>
					
					<form id="formLogin" role="form" method="post"
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
						<c:if test="${isSenhaUsuarioExpirada}">			
							<div class="js-link-trocar-senha  hidden" style="text-align: center; margin: 0; padding: 0;">																							
								<button type="button" class="btn  btn-link" data-siga-modal-abrir="trocaSenhaUsuario">
								    Trocar senha
								</button>																														
							</div>
						</c:if>
						<div class="row pt-3">
							<div class="col">
								<div class="text-center">
									<button type="submit" class="btn btn-lg btn-primary btn-block"><i class="fas fa-sign-in-alt"></i> Entrar</button>								
									<div class="mt-4">
										<c:if test="${siga_cliente != 'GOVSP'}">
											<a href="/siga/public/app/usuario/incluir_usuario"
												class="btn btn-secondary btn-block mb-2"><fmt:message key = "usuario.sounovo"/></a> 
										</c:if>
										<a href="/siga/public/app/usuario/senha/reset" class="btn btn-link btn-block"><strong>Esqueci minha senha</strong></a>
									</div>
									
									<c:choose>
										<c:when test="${siga_cliente_sso}">
											<hr class="my-2">
											<p class="text-left font-weight-bold">Ou acesse com: </p>
							
											<a href="/siga/public/app/loginSSO"class="btn btn-dark btn-lg btn-block btn-gov-br">Entrar com&nbsp;<span><b>${siga_cliente_sso_btn_txt}</b></span> </a>
										</c:when>
									</c:choose>
									
									<c:if test="${siga_cliente ne 'GOVSP'}">
										<div class="mt-3">
										    <div class="d-flex justify-content-between">
										    	   	<div>
										    		Versão: ${versao}
										    	    </div>
										    	    <div>
										    		<a class="text-top" href="http://linksiga.trf2.jus.br" target="_blank" class="btn btn-link">Sobre o SIGA</a> 
										    	    </div>
										    </div>
										</div>
									</c:if>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	<c:if test="${isSenhaUsuarioExpirada}">
		<siga:siga-modal id="trocaSenhaUsuarioMensagem" centralizar="true" abrirAoCarregarPagina="true">						
			<div class="modal-body">Sua senha expirou e deve ser alterada</div>     		       				
			<div class="modal-footer">
				<button type="button" class="btn  btn-secondary  js-troca-senha-mensagem-btn" 
					data-siga-modal-abrir="trocaSenhaUsuario" data-siga-modal-fechar="trocaSenhaUsuarioMensagem">
				    Trocar senha
				</button>			      			      				      														
			</div>  
		</siga:siga-modal>											
		<siga:troca-senha-usuario idModal="trocaSenhaUsuario" />			
	</c:if>
	
	<c:if test="${!isSenhaUsuarioExpirada}">		
		<script>					
			$(function() {								
				var isOpera = !!navigator.userAgent.match(/OPR/);
				var isEdge = !!navigator.userAgent.match(/Edge/);
				var isChrome = !!navigator.userAgent.match(/Chrome/) && !isOpera && !isEdge
				if(!isChrome) {			    						
					var usernameInput = document.getElementById('username');
					sigaModal.alerta('Recomendamos o navegador Google Chrome para acesso', true, ' ').focus(usernameInput);
				}
			});
		</script>
	</c:if>

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
	<c:if test="${empty loginMensagem}">							
		<siga:siga-modal id="avisoCabecModal" centralizar="true" tituloADireita="Aviso importante" 
			exibirRodape="true" descricaoBotaoFechaModalDoRodape="Ok" abrirAoCarregarPagina="${fAviso}">
			<div class="modal-body  text-justify">${avisoMensagem}</div>
		</siga:siga-modal>						
	</c:if>	
	<script src="../../javascript/service-worker.js" async></script>
</siga:pagina>
<c:if test="${isSenhaUsuarioExpirada}">
	<script src="../../javascript/usuario.troca-senha.js"></script>
	<script>
		$(function() {
			var trocaSenhaUsuarioMensagemModal = $('#trocaSenhaUsuarioMensagem'); 
			trocaSenhaUsuarioMensagemModal.on('shown.bs.modal', function() {
				$('.js-troca-senha-mensagem-btn').focus();
			}).on('hidden.bs.modal', function() {
				$('.js-link-trocar-senha').removeClass('hidden');	
			});																								
		});					
	</script>
</c:if>
