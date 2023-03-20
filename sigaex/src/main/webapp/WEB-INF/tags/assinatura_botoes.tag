<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<%@ attribute name="assinar" required="false"%>
<%@ attribute name="emLote" required="false"%>
<%@ attribute name="voltar" required="false"%>
<%@ attribute name="linkVoltar" required="false"%>
<%@ attribute name="autenticar" required="false"%>
<%@ attribute name="assinarCertDigital" required="false"%>
<%@ attribute name="assinarComSenha" required="false"%>
<%@ attribute name="assinarComSenhaPin" required="false"%>
<%@ attribute name="assinarComSenhaChecado" required="false"%>
<%@ attribute name="assinarComSenhaPinChecado" required="false"%>
<%@ attribute name="autenticarComSenha" required="false"%>
<%@ attribute name="autenticarComSenhaPin" required="false"%>
<%@ attribute name="autenticarComSenhaChecado" required="false"%>
<%@ attribute name="autenticarComSenhaPinChecado" required="false"%>
<%@ attribute name="idMovimentacao" required="false"%>
<%@ attribute name="juntarAtivo" required="false"%>
<%@ attribute name="juntarFixo" required="false"%>
<%@ attribute name="tramitarAtivo" required="false"%>
<%@ attribute name="tramitarFixo" required="false"%>
<%@ attribute name="tramitarExplicacao" required="false"%>
<%@ attribute name="exibirNoProtocoloAtivo" required="false"%>
<%@ attribute name="exibirNoProtocoloFixo" required="false"%>

<div class="col pt-2 pb-2">
	<div class="row">
		<div class="col-auto my-auto">
		
			<c:if test="${not empty voltar and voltar}">
				<input type="button" value="Voltar" onclick="${(empty linkVoltar) ? 'javascript:history.back();' : 'javascript:window.location.href=\''.concat(linkVoltar).concat('\';')}" class="btn btn-secondary mr-2" />
			</c:if>
			
			<c:choose>
				<c:when test="${assinarCertDigital or assinarComSenha or assinarComSenhaPin or autenticarComSenha or autenticarComSenhaPin or autenticar or emLote}">
					<c:if test="${not empty assinar and assinar}">
						<a id="bot-assinar" accesskey="s" href="#"
								onclick="javascript: AssinarDocumentos(false, ${assinarCertDigital or assinarComSenha or assinarComSenhaPin});"
								class="btn btn-primary mr-2" role="button"> Assinar <i class="fa fa-signature"></i> </a> 
					</c:if>
					<c:if test="${not empty autenticar and autenticar}">
						<a id="bot-autenticar" accesskey="u" href="#"
								onclick="javascript: AssinarDocumentos(true, ${autenticarComSenha or autenticarComSenhaPin or autenticar});"
								class="btn btn-primary mr-2" role="button">	Autenticar <i class="fa fa-stamp"></i> </a>
					</c:if>
				</c:when>
				<c:otherwise>
					Nenhum provedor de assinatura/autenticação habilitado.
				</c:otherwise>	
			</c:choose>	
		</div>
		<c:if test="${assinarCertDigital or assinarComSenha or assinarComSenhaPin or autenticarComSenha or autenticarComSenhaPin or autenticar}">
			<div class="col-auto my-auto mr-5">
				<h5>Formas de ${(not empty assinar and assinar)? 'assinatura': (not empty autenticar and autenticar) ? 'autenticação':''}:</h5>
				
				<c:set var="SenhaChecked" value="${assinarComSenhaChecado || autenticarComSenhaChecado}" /> 
				<c:set var="PinChecked" value="${(not empty identidadeCadastrante.pinIdentidade) and (assinarComSenhaPinChecado || autenticarComSenhaPinChecado)}" /> 
				<c:set var="CertificadoChecked" value="${not SenhaChecked and not PinChecked}" /> 
				
				<c:if test="${assinarComSenha || autenticarComSenha}">
					<div class="custom-control custom-radio">
						<input class="custom-control-input" type="radio" 
							   accesskey="c" name="radioProviderAssinatura" id="ad_password_0" 
							   <c:if test="${SenhaChecked}">checked</c:if> onclick="setUserLocalStorage('tipo-assinatura', 'senha')" /> 
						<label class="custom-control-label" for="ad_password_0">Senha</label>
	
					</div>
				</c:if>
				
				
				<c:if test="${assinarComSenhaPin || autenticarComSenhaPin}">
					<div class="custom-control custom-radio">
						<input class="custom-control-input" type="radio" 
							accesskey="p" name="radioProviderAssinatura" id="ad_pin_0" 
							<c:if test="${PinChecked}">checked</c:if>
							<c:if test="${empty identidadeCadastrante.pinIdentidade}">disabled</c:if>
							identidadeCadastrante onclick="setUserLocalStorage('tipo-assinatura', 'pin')" /> 
						<label class="custom-control-label" for="ad_pin_0">PIN</label>
						<c:if test="${empty identidadeCadastrante.pinIdentidade}">
							<small class="text-muted"> - Clique <strong><a href='/siga/app/pin/cadastro'>aqui</a></strong> saber mais e definir seu PIN.</small>
						</c:if>
					</div>
				</c:if>
				
				<c:if test="${assinarCertDigital || autenticar}">
					<div class="custom-control custom-radio">
						<input class="custom-control-input" type="radio" 
							accesskey="d" name="radioProviderAssinatura" id="ad_certificado_0" 
							<c:if test="${CertificadoChecked}">checked</c:if> onclick="setUserLocalStorage('tipo-assinatura', 'certificado-digital')" /> 
						<label class="custom-control-label" for="ad_certificado_0">Certificado Digital</label>
					</div>
				</c:if>
				
			</div>

			<script>
				$(document).ready(function() {
					var tipoAssinatura = getUserLocalStorage('tipo-assinatura');
					var i = undefined;
					
					switch (tipoAssinatura) {
					case 'certificado-digital': 
						i = $('#ad_certificado_0');
						break;						
					case 'senha': 
						i = $('#ad_password_0');
						break;						
					case 'pin': 
						i = $('#ad_pin_0');
						break;						
					}
					if (i && !i.prop('checked') && !i.prop('disabled'))
						i.prop('checked', true);
				});
			</script>
			
			<div class="col">	
				<c:if test="${(not empty juntarAtivo and siga_cliente != 'GOVSP') or (not empty tramitarAtivo) or (not empty exibirNoProtocoloAtivo)}">
					<h5>Após ${(not empty assinar and assinar)? 'assinatura':'autenticação'}:</h5>
				</c:if>
				<c:if test="${not empty juntarAtivo}">
					<div class="custom-control custom-checkbox ${hide_only_GOVSP}">
						<input class="form-check-input " type="checkbox" name="ad_juntar_0"
							id="ad_juntar_0" <c:if test="${juntarAtivo}">checked</c:if>
							<c:if test="${juntarFixo}">disabled</c:if> /> <label
							class="form-check-label" for="ad_juntar_0">Juntar</label>
					</div>
				</c:if>
			
				<c:if test="${not empty tramitarAtivo}">
					<div class="custom-control custom-checkbox ">
						<input class="form-check-input" type="checkbox" name="ad_tramitar_0"
							id="ad_tramitar_0" <c:if test="${tramitarAtivo}">checked</c:if>
							<c:if test="${tramitarFixo}">disabled</c:if> /> <label
							class="form-check-label" for="ad_tramitar_0" <siga:tooltip title="Tramitar Automaticamente" explicacao="${tramitarExplicacao}"/> >Tramitar</label>
					</div>
					<c:set var="exibirOpcoes" scope="request" value="d-block" />
				</c:if>
				<c:if test="${not empty exibirNoProtocoloAtivo}">
					<div class="custom-control custom-checkbox ">
						<input class="form-check-input" type="checkbox" name="ad_exibirNoProtocolo_0"
							id="ad_exibirNoProtocolo_0" onchange="confirmaExibirNoProtocolo(this)" <c:if test="${exibirNoProtocoloAtivo}">checked</c:if>
							<c:if test="${exibirNoProtocoloFixo}">disabled</c:if> /> <label 
							class="form-check-label" for="ad_exibirNoProtocolo_0">Disponibilizar no Acompanhamento do Protocolo</label>
					</div>
					<script type="text/javascript">
						function confirmaExibirNoProtocolo(checkbox) {
						  if (checkbox.checked) {
						    if (!confirm(<c:if test="${doc.mobilGeral.acessoRestrito}">"Atenção: Documento com Restrição de Acesso. \n" + </c:if>"Ao clicar em OK o conteúdo deste documento ficará disponível através do número do protocolo de acompanhamento. Deseja continuar? ")) {
							  checkbox.checked = false;
						    }
						  }
						}	
					</script>
				</c:if>
			</div>
		</c:if>
	
	</div>
	

</div>