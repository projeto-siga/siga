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
	</script>

	<c:if test="${not doc.eletronico}">
		<script type="text/javascript">
			$("html").addClass("fisico");
		</script>
	</c:if>

	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Confirme os dados do documento abaixo:</h5>
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

			<c:if test="${doc.conteudoBlobHtmlStringComReferencias != null}">
				<div id="divClassificacao" class="card-body bg-white border">
					<div class="gt-content-box" style="padding: 10px;">
						<table class="message" width="100%">
							<tr>
								<td colspan="2">
									<div id="conteudo" style="padding-top: 10px;">
										<tags:fixdocumenthtml>
											${doc.conteudoBlobHtmlStringComReferencias}
										</tags:fixdocumenthtml>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</c:if>
			<c:if test="${doc.pdf != null && doc.conteudoBlobHtmlStringComReferencias == null}">
				<div class="card-body bg-white">
					<iframe style="display: block;" name="painel" id="painel"
						src="/sigaex/app/arquivo/exibir?arquivo=${doc.referenciaPDF}"
						width="100%" height="400" frameborder="0" scrolling="auto"></iframe>
					<script>
						$(document).ready(function(){resize();$(window).resize(function(){resize();});});
					</script>
				</div>
			</c:if>

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

					<c:if test="${siga_cliente != 'GOVSP'}">
						<tags:assinatura_botoes assinar="${assinando}"
							autenticar="${autenticando}"
							assinarComSenha="${assinando and f:podeAssinarComSenha(titular,lotaTitular,doc.mobilGeral)}"
							autenticarComSenha="${autenticando and f:podeAutenticarComSenha(titular,lotaTitular,doc.mobilGeral)}"
							juntarAtivo="${juntarAtivo}" juntarFixo="${juntarFixo}"
							tramitarAtivo="${tramitarAtivo}" tramitarFixo="${tramitarFixo}" />
					</c:if>

					<c:if test="${siga_cliente == 'GOVSP'}">
						<tags:assinatura_botoes assinar="${assinando}" voltar="true"
							linkVoltar="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${sigla}"
							autenticar="${autenticando}"
							assinarComSenhaChecado="${assinando and f:podeAssinarComSenha(titular,lotaTitular,doc.mobilGeral)}"
							autenticarComSenhaChecado="${autenticando and f:podeAutenticarComSenha(titular,lotaTitular,doc.mobilGeral)}"
							juntarAtivo="${juntarAtivo}" juntarFixo="${juntarFixo}" 
							tramitarAtivo="" />
					</c:if>
			</div>
		</div>
	</div>

	<tags:assinatura_rodape />
</siga:pagina>