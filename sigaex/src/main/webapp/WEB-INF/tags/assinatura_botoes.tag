<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<%@ attribute name="assinar" required="false"%>
<%@ attribute name="autenticar" required="false"%>
<%@ attribute name="assinarComSenha" required="false"%>
<%@ attribute name="autenticarComSenha" required="false"%>
<%@ attribute name="idMovimentacao" required="false"%>
<%@ attribute name="juntarAtivo" required="false"%>
<%@ attribute name="juntarFixo" required="false"%>
<%@ attribute name="tramitarAtivo" required="false"%>
<%@ attribute name="tramitarFixo" required="false"%>

<div>
	<c:if test="${not empty assinar and assinar}">
		<a id="bot-assinar" accesskey="s" href="#"
			onclick="javascript: AssinarDocumentos(false, ${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;POL:Política ICP-Brasil')});"
			class="btn btn-primary m-1" role="button">A<u>s</u>sinar</a>
	</c:if>

	<c:if test="${not empty autenticar and autenticar}">
		<a id="bot-autenticar" accesskey="u" href="#"
			onclick="javascript: AssinarDocumentos(true, ${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC;ASS;POL')});"
			class="btn btn-primary m-1" role="button">A<u>u</u>tenticar</a>
	</c:if>

	<p class="gt-cancel">
		<c:if test="${assinarComSenha || autenticarComSenha}">
			<span class="ml-2"> <label> <input accesskey="c"
					type="checkbox" name="ad_password_0" /> <u>C</u>om Senha
			</label>
			</span>
		</c:if>

		<c:if test="${not empty juntarAtivo}">
			<span class="ml-3"> <label> <input
					type="checkbox" name="ad_juntar_0"
					<c:if test="${juntarAtivo}">checked</c:if>
					<c:if test="${juntarFixo}">disabled</c:if> /> Juntar
			</label>
			</span>
		</c:if>

		<c:if test="${not empty tramitarAtivo}">
			<span class="ml-3"> <label> <input
					type="checkbox" name="ad_tramitar_0"
					<c:if test="${tramitarAtivo}">checked</c:if>
					<c:if test="${tramitarFixo}">disabled</c:if> /> Tramitar
			</label>
			</span>
		</c:if>
	</p>
</div>