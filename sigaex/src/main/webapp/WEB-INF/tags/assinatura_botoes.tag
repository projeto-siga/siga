<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<%@ attribute name="assinar" required="false"%>
<%@ attribute name="voltar" required="false"%>
<%@ attribute name="autenticar" required="false"%>
<%@ attribute name="assinarComSenha" required="false"%>
<%@ attribute name="assinarComSenhaChecado" required="false"%>
<%@ attribute name="autenticarComSenha" required="false"%>
<%@ attribute name="autenticarComSenhaChecado" required="false"%>
<%@ attribute name="idMovimentacao" required="false"%>
<%@ attribute name="juntarAtivo" required="false"%>
<%@ attribute name="juntarFixo" required="false"%>
<%@ attribute name="tramitarAtivo" required="false"%>
<%@ attribute name="tramitarFixo" required="false"%>

<div>
	<div class="row">
		<div class="col-5">
			<c:if test="${not empty assinar and assinar}">
				<a id="bot-assinar" accesskey="s" href="#"
					onclick="javascript: AssinarDocumentos(false, ${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA:Sistema Integrado de Gestão Administrativa;DOC:Módulo de Documentos;ASS:Assinatura digital;POL:Política ICP-Brasil')});"
					class="btn btn-primary m-3" role="button"><i class="fa fa-signature"></i> A<u>s</u>sinar</a>
			</c:if>
			<c:if test="${not empty autenticar and autenticar}">
				<a id="bot-autenticar" accesskey="u" href="#"
					onclick="javascript: AssinarDocumentos(true, ${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;DOC;ASS;POL')});"
					class="btn btn-primary m-3" role="button"><i class="fa fa-stamp"></i> A<u>u</u>tenticar</a>
			</c:if>
		</div>
		<div class="col-7">
			<p>
				<c:if test="${assinarComSenha || autenticarComSenha}">
					<div class="form-check">
					  <input class="form-check-input" type="checkbox" accesskey="c"	 name="ad_password_0" id="ad_password_0"  />
					  <label class="form-check-label" for="ad_juntar_0"><u>C</u>om Senha</label>
					</div>
					
				</c:if>
				<c:if test="${assinarComSenhaChecado || autenticarComSenhaChecado}">
					<div class="form-check">
					  <input class="form-check-input" type="checkbox" accesskey="c"	 name="ad_password_0" id="ad_password_0" checked />
					  <label class="form-check-label" for="ad_juntar_0"><u>C</u>om Senha</label>
					</div>
					
				</c:if>
		
				<c:if test="${not empty juntarAtivo}">
					<div class="form-check">
					  <input class="form-check-input" type="checkbox" name="ad_juntar_0"  id="ad_juntar_0"
							<c:if test="${juntarAtivo}">checked</c:if>
							<c:if test="${juntarFixo}">disabled</c:if> />
					  	<label class="form-check-label" for="ad_juntar_0">Juntar</label>
					</div>
				</c:if>
		
				<c:if test="${not empty tramitarAtivo}">		
					<div class="form-check">
					  <input class="form-check-input" type="checkbox" name="ad_tramitar_0" id="ad_tramitar_0"
							<c:if test="${tramitarAtivo}">checked</c:if>
							<c:if test="${tramitarFixo}">disabled</c:if> />
					  	<label class="form-check-label" for="ad_tramitar_0">Tramitar</label>
					</div>
				</c:if>
			</p>
		</div>
		<c:if test="${not empty voltar and voltar}">
			<div class="form-check">
				<input type="button" value="Voltar" onclick="javascript:history.back();" class="btn btn-cancel ml-2" />
			</div>
		</c:if>
	
	</div>


	
</div>