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

	<c:if test="${assinarComSenha || autenticarComSenha}">
		<p class="gt-cancel" style="height: 38px; padding-top: 10px;">
			<label> <input type="checkbox" name="ad_password_0" /> Com
				Senha
			</label>
		</p>
	</c:if>
</div>