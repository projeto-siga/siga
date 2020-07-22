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
	
	<c:set var="siga_cliente_sso" scope="request" value="${f:resource('siga.integracao.sso')}" />
	<c:set var="siga_cliente_sso_btn_txt" scope="request" value="${f:resource('siga.integracao.sso.btn.txt')}" />
	
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
								<a href="#" class="btn  btn-default" title="Troca de senha" data-toggle="modal" data-target="#trocaSenhaUsuario" data-dismiss="modal">
									Trocar senha
								</a>	
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
										<a href="/siga/public/app/usuario/esqueci_senha" class="btn btn-link btn-block"><strong>Esqueci minha senha</strong></a>
									</div>
									
									<c:choose>
										<c:when test="${siga_cliente_sso}">
											<hr class="my-2">
											<p class="text-left font-weight-bold">Ou acesse com: </p>
							
											<a href="/siga/public/app/loginSSO"class="btn btn-lg btn-dark btn-block">${siga_cliente_sso_btn_txt}</a>
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
		<div class="modal  fade" id="trocaSenhaUsuarioMensagem" tabindex="-1" role="dialog">
		 	<div class="modal-dialog modal-dialog-centered" role="alert">
		   		<div class="modal-content">		
		   			<div class="modal-header">		   			
		   				<img src="/siga/imagens/${siga_cliente eq 'GOVSP' ? 'logo-sem-papel-cor.png' : 'logo-siga-novo-166px.png'}" height="40">			
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
				          <span aria-hidden="true">&times;</span>
				        </button>
			      	</div>   					   					      	
		      		<div class="modal-body">		      		
		      			Sua senha expirou e deve ser alterada
		      		</div>
	      			<div class="modal-footer">		      			
		      			<a href="#" class="btn  btn-secondary  js-troca-senha-mensagem-btn" title="Troca de senha"
							data-toggle="modal" data-target="#trocaSenhaUsuario" data-dismiss="modal">
						     	Trocar senha
						</a>		      															
					</div>   								  						   
		   		</div>
		 	</div> 	 	
		</div>					
		<siga:troca-senha-usuario></siga:troca-senha-usuario>			
	</c:if>
	
	<c:if test="${!isSenhaUsuarioExpirada}">			
		<div class="modal fade" id="msgModal" tabindex="-1" role="dialog" aria-labelledby="msgModalLabel" aria-hidden="true">
		  <div class="modal-dialog modal-dialog-centered" role="document">
		    <div class="modal-content">
		    <div class="modal-header">
		        <c:if test="${siga_cliente eq 'GOVSP'}">		        	
		        	<img src="/siga/imagens/${siga_cliente eq 'GOVSP' ? 'logo-sem-papel-cor.png' : 'logo-siga-novo-166px.png'}" height="40">			
		        </c:if>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		        Recomendamos o navegador Google Chrome para acesso.
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>
		      </div>
		    </div>
		  </div>
		</div>
		<script type="text/javascript">
			$(document).ready(function() {
				var isOpera = !!navigator.userAgent.match(/OPR/);
				var isEdge = !!navigator.userAgent.match(/Edge/);
				var isChrome = !!navigator.userAgent.match(/Chrome/) && !isOpera && !isEdge
				if(!isChrome) {
			    	$('#msgModal').modal('show');
				}
			})
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
		<c:set var="avisoTituloCabec" value="Aviso Importante" />
		<c:set var="avisoCabec" value="${avisoMensagem}" />	
		<div class="${avisoCabec==null?'d-none':''}" id="avisoCabecId" >
			<div id="avisoCabecModal" class="modal" tabindex="-1" role="dialog">
			  <div class="modal-dialog" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title">${avisoTituloCabec}</h5>
			        <button type="button" class="close" data-dismiss="modal" aria-label="Fechar">
			          <span aria-hidden="true">&times;</span>
			        </button>
			      </div>
			      <div class="modal-body">
			        <p>${avisoCabec}</p>
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-primary" data-dismiss="modal">Ok</button>
			      </div>
			    </div>
			  </div>
			</div>
		</div>
		<c:if test="${fAviso}">
			<script type="text/javascript">
				$(window).load(function() {
					$('#avisoCabecModal').modal('show');
				});
			</script>
		</c:if>	
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
			trocaSenhaUsuarioMensagemModal.modal('show');																		
		});					
	</script>
</c:if>
