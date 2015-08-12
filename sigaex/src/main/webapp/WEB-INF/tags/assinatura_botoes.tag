<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<%@ attribute name="autenticar" required="false"%>
<%@ attribute name="assinarComSenha" required="false"%>
<%@ attribute name="autenticarComSenha" required="false"%>
<%@ attribute name="idMovimentacao" required="false"%>

<div class="gt-form-row">
	<a id="bot-assinar" href="#"
		onclick="javascript: AssinarDocumentos(false, ${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;POL:Política ICP-Brasil')});"
		class="gt-btn-medium gt-btn-left">Assinar</a>

	<c:if test="${not empty autenticar and autenticar}">
		<a id="bot-autenticar" href="#"
			onclick="javascript: AssinarDocumentos(true, ${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC;ASS;POL')});"
			class="gt-btn-medium gt-btn-left">Autenticar</a>
	</c:if>

	<%--
	<p class="gt-cancel" style="height: 38px; padding-top: 10px;">
		<label> <input type="checkbox" name="bot-com-senha" /> Com
			Senha
		</label>
	</p>
	 --%>

	<c:if test="${assinarComSenha || autenticarComSenha}">
		<a id="bot-assinar-senha" href="#"
			onclick="javascript: assinarComSenha();"
			class="gt-btn-medium gt-btn-left">Usar Senha</a>

		<div id="dialog-form" title="Assinar/Autenticar com Senha">
			<form id="form-assinarSenha" method="post"
				action="/sigaex/app/expediente/mov/assinar_mov_login_senha_gravar">
				<input type="hidden" id="id" name="id" value="${idMovimentacao}" />
				<input type="hidden" id="tipoAssinaturaMov" name="tipoAssinaturaMov"
					value="A" />
				<fieldset>
					<label>Matrícula</label> <br /> <input id="nomeUsuarioSubscritor"
						type="text" name="nomeUsuarioSubscritor"
						class="text ui-widget-content ui-corner-all"
						onblur="javascript:converteUsuario(this)" /><br /> <br /> <label>Senha</label>
					<br /> <input type="password" id="senhaUsuarioSubscritor"
						name="senhaUsuarioSubscritor"
						class="text ui-widget-content ui-corner-all" autocomplete="off" />
				</fieldset>
			</form>
		</div>

		<script> 
			    dialog = $("#dialog-form").dialog({
			      autoOpen: false,
			      height: 210,
			      width: 350,
			      modal: true,
			      buttons: {
			    	  <c:if test="${assinarComSenha}">
			          	"Assinar": assinarGravar,
			          </c:if>	
			    	  <c:if test="${autenticarComSenha}">
				          "Autenticar": conferirCopiaGravar,
			          </c:if>	
			          "Cancelar": function() {
			            dialog.dialog( "close" );
			          }
			      },
			      close: function() {
			        
			      }
			    });
			
			    function assinarComSenha() {
			       dialog.dialog( "open" );
			    }
	
			    function assinarGravar() {
			    	AssinarDocumentosSenha('false', this);
				}
	
			    function conferirCopiaGravar() {
			    	AssinarDocumentosSenha('true', this);
				}
			  </script>
	</c:if>
</div>