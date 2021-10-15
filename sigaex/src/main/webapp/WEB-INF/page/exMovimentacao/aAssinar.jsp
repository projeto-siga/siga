<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<siga:pagina titulo="Documento" compatibilidade="IE=EmulateIE9">
	<script type="text/javascript" language="Javascript1.1">
		/*  converte para maiúscula a sigla do estado  */
		function converteUsuario(nomeusuario) {
			re = /^[a-zA-Z]{2}\d{3,6}$/;
			ret2 = /^[a-zA-Z]{1}\d{3,6}$/;
			tmp = nomeusuario.value;
			if (tmp.match(re) || tmp.match(ret2)) {
				nomeusuario.value = tmp.toUpperCase();
			}
		}
		
		var getFFVersion = navigator.userAgent.substring(
				navigator.userAgent.indexOf("Firefox")).split("/")[1]
		var FFextraHeight = parseFloat(getFFVersion) >= 0.1 ? 3 : 0
		var is_chrome = navigator.userAgent.toLowerCase().indexOf('chrome') > -1;
		if (is_chrome) {
			FFextraHeight = 30;

		}
		
		function pageHeight() {
			return window.innerHeight != null ? window.innerHeight
					: document.documentElement
							&& document.documentElement.clientHeight ? document.documentElement.clientHeight
							: document.body != null ? document.body.clientHeight
									: null;
		}
		
		function resize() {
			var ifr = document.getElementById('painel');
			var ifrp = document.getElementById('paipainel');

			ifr.height = pageHeight() - 200;

			if (ifr && !window.opera) {
				ifr.style.display = "block";
				if (ifr.contentDocument && ifr.contentDocument.body.offsetHeight) //ns6 syntax
					ifr.height = ifr.contentDocument.body.offsetHeight
							+ FFextraHeight;
				else if (ifr.Document && ifr.Document.body.scrollHeight) //ie5+ syntax
					ifr.height = ifr.Document.body.scrollHeight;
			}
		}
	</script>

	<c:if test="${not doc.eletronico}">
		<script type="text/javascript">
			$("html").addClass("fisico");
		</script>
	</c:if>

	<div class="container-fluid">
		<div class="row">
			<div class="col col-12 col-lg-6 mb-1">
				<c:if test="${doc.pdf != null}">
					<div class="card-body bg-white">
						<c:set var="url" value="/sigaex/app/arquivo/exibir?arquivo=${doc.referenciaPDF}"/>
						<input type="hidden" id="visualizador" value="${f:resource('/sigaex.pdf.visualizador') }"/>
						<iframe style="display: block;" name="painel" id="painel"
							src=""
							width="100%" height="900" frameborder="0" scrolling="auto"></iframe>
						<script>
							$(document).ready(function(){resize();$(window).resize(function(){resize();});});
						</script>
					</div>
				</c:if>
			</div>
			<div class="col col-12 col-lg-6">
				<div class="card bg-light mb-3">
					<div class="card-header">
						<h5>Confirme os dados do documento</h5>
					</div>
					<div class="card-body">
						<p class="p-0 m-0">
							<b>Documento ${doc.exTipoDocumento.descricao}:</b> ${doc.codigo}
						</p>
						<p class="p-0 m-0">
							<b><fmt:message key="tela.assinar.data"/>:</b> ${doc.dtDocDDMMYY}
						</p>
						<p class="p-0 m-0">
							<b>Classificação:</b> ${doc.exClassificacao.descricaoCompleta}
						</p>
						<p class="p-0 m-0">
							<b>Descrição:</b> ${doc.descrDocumento}
						</p>
						<c:if test="${siga_cliente != 'GOVSP'}">
						<p class="p-0 m-0">
							<b>De:</b> ${doc.subscritorString}
						</p>
						<p class="p-0 m-0">	
							<b>Para:</b> ${doc.destinatarioString}
						</p>
						</c:if>
					</div>
		
					<c:set var="acao" value="assinar_gravar" />
						<div class="card-footer" style="padding-top: 10px;">
							<div id="dados-assinatura" style="visible: hidden">
								<input type="hidden" name="ad_url_base" value="" /> <input
									type="hidden" name="ad_url_next"
									value="/sigaex/app/expediente/doc/exibir?sigla=${sigla}" /> <input
									type="hidden" name="ad_descr_0" value="${sigla}" /> <input
									type="hidden" name="ad_url_pdf_0"
									value="/sigaex/app/arquivo/exibir?arquivo=${doc.codigoCompacto}.pdf" />
								<input type="hidden" name="ad_url_post_0"
									value="/sigaex/app/expediente/mov/assinar_gravar" /> <input
									type="hidden" name="ad_url_post_password_0"
									value="/sigaex/app/expediente/mov/assinar_senha_gravar" /> <input
									type="hidden" name="ad_id_0" value="${doc.codigoCompacto}" /> <input
									type="hidden" name="ad_description_0"
									value="${doc.descrDocumento}" /> <input type="hidden"
									name="ad_kind_0" value="${doc.descrFormaDoc}" />
							</div>
							
							<c:set var="podeAssinarComSenha" value="${assinando and f:podeAssinarComSenha(cadastrante,lotaCadastrante,doc.mobilGeral) }" />
							<c:set var="podeAutenticarComSenha" value="${autenticando and f:podeAutenticarComSenha(cadastrante,lotaCadastrante,doc.mobilGeral) }" />
							<c:set var="defaultAssinarComSenha" value="${f:deveAssinarComSenha(cadastrante,lotaCadastrante,doc.mobilGeral) }" />
							<c:set var="defaultAutenticarComSenha" value="${f:deveAutenticarComSenha(cadastrante,lotaCadastrante,doc.mobilGeral) }" />
							
							<c:set var="podeUtilizarSegundoFatorPin" value="${f:podeUtilizarSegundoFatorPin(cadastrante,cadastrante.lotacao)}" />
							<c:set var="obrigatorioUtilizarSegundoFatorPin" value="${f:deveUtilizarSegundoFatorPin(cadastrante,cadastrante.lotacao)}" />
							<c:set var="defaultUtilizarSegundoFatorPin" value="${f:defaultUtilizarSegundoFatorPin(cadastrante,cadastrante.lotacao) }" />
			
							<tags:assinatura_botoes assinar="${assinando}" voltar="${voltarAtivo}"
								linkVoltar="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${sigla}"
								autenticar="${autenticando}"
		
									assinarComSenha="${podeAssinarComSenha and not obrigatorioUtilizarSegundoFatorPin}"
								    autenticarComSenha="${podeAutenticarComSenha and not obrigatorioUtilizarSegundoFatorPin}"			
									assinarComSenhaChecado="${podeAssinarComSenha and defaultAssinarComSenha and not defaultUtilizarSegundoFatorPin}"
									autenticarComSenhaChecado="${podeAutenticarComSenha and defaultAutenticarComSenha and not defaultUtilizarSegundoFatorPin}"
		
		
									assinarComSenhaPin="${podeAssinarComSenha and podeUtilizarSegundoFatorPin}"
									autenticarComSenhaPin="${podeAutenticarComSenha and podeUtilizarSegundoFatorPin}"
									assinarComSenhaPinChecado="${podeAssinarComSenha and podeUtilizarSegundoFatorPin and defaultUtilizarSegundoFatorPin}"
									autenticarComSenhaPinChecado="${podeAutenticarComSenha and podeUtilizarSegundoFatorPin and defaultUtilizarSegundoFatorPin}"
				
								
								juntarAtivo="${juntarAtivo}" juntarFixo="${juntarFixo}" 
								tramitarAtivo="${tramitarAtivo}" tramitarFixo="${tramitarFixo}" 
								exibirNoProtocoloAtivo="${f:podeDisponibilizarNoAcompanhamentoDoProtocolo(titular,lotaTitular,doc)? false:undefined}" 
								exibirNoProtocoloFixo="${not f:podeDisponibilizarNoAcompanhamentoDoProtocolo(titular,lotaTitular,doc)}"/>
		
					</div>
				</div>
				<c:if test="${doc.pdf != null}">
					<tags:assinatura_rodape podeAssinarPor="${f:podeAssinarPor(titular,lotaTitular,doc.mobilGeral)}"/>
				</c:if>
			</div>
		</div>
	</div>

	<script>
		window.onload = function () { 
			document.getElementById('painel').src = montarUrlDocPDF('${url }',document.getElementById('visualizador').value);
		} 
	</script>
</siga:pagina>