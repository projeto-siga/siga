<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page isErrorPage="true"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<c:catch var="catchException">
	<siga:pagina titulo="Login Inválido" desabilitarbusca="sim">
		<%-- cria a url para redirecionar --%>
		<c:url var="urlPagina" value="${pagina}" />
		<div class="gt-bd clearfix">
			<div class="gt-content clearfix">

				<div id="caption" class="gt-error-page-hd">
					<h2>Não Foi Possível Completar a Operação</h2>
				</div>

				<div class="gt-content-box">
					<table width="100%">
						<tr>
							<td align="center" valign="middle">
								<table class="form" width="50%">
									<tr>
										<td style="text-align: center; padding-top: 10px;"><h3>Login
												ou Senha Incorretos</h3>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td style="text-align: center; padding: 10pt; font-size: 10pt;">Verifique
								se a matrícula foi preenchida na forma <br /> <b>XX</b>99999,
								onde XX é a sigla do seu órgão (T2, RJ, ES, etc.) e 99999 é o
								número da sua matrícula.</td>
						</tr>
					</table>
				</div>
				<div style="padding-top: 10px;">
					<input type="button" id="show_stack" value="Tentar Novamente"
						class="gt-btn-large gt-btn-right"
						onclick="javascript:history.back();" />
				</div>
			</div>
		</div>

		<c:remove var="pagina" scope="session" />
	</siga:pagina>
</c:catch>

<c:if test="${catchException!=null}">
Erro: ${catchException.message}<br>
	<br>
	<br>

	<pre>
Erro original:
${exceptionStack}</pre>
</c:if>