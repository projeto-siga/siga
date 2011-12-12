<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page isErrorPage="true"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<siga:pagina titulo="Login Inválido">

<%-- cria a url para redirecionar --%>
<c:url var="urlPagina" value="${pagina}" />

<table width="100%" height="100%">
	<tr>
		<td align="center" valign="center">
		<table class="form">
			<tr class="header">
				<td colspan="2" style="padding:2pt; font-size:14pt;">Login ou
				Senha Incorretos</td>
			</tr>
 			<tr>
				<td style="text-align:center; padding:8pt; font-size:14pt;"><a
					href="javascript:history.back();">Tentar Novamente...</a></td>
			</tr>
			<tr>
				<%-- 
				<td style="text-align:center; padding:8pt; font-size:14pt;"><a href="${pageContext.request.contextPath}">Voltar...</a></td>
			--%>
			</tr>
			<tr>
				<td style="text-align:center; padding:10pt; font-size:10pt;">Verifique
				se a matrícula foi preenchida na forma <br />
				<b>XX</b>99999, onde XX é a sigla do seu órgão (T2, RJ, ES, etc.) e 99999 é o número da sua matrícula.</td>
			</tr>

		</table>
		</td>
	</tr>
</table>

<c:remove var="pagina" scope="session" />

</siga:pagina>
