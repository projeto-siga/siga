<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page isErrorPage="true"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<c:catch>
	<c:if test="${empty exceptionStack}">
		<%
			java.lang.Throwable t = null;
					if (pageContext != null
							&& pageContext.getErrorData() != null
							&& pageContext.getErrorData().getThrowable() != null) {
						t = pageContext.getErrorData().getThrowable();
						if (!t.getClass().getSimpleName().equals(
								"AplicacaoException")
								&& t.getCause() != null) {
							if (t.getCause().getClass().getSimpleName().equals(
									"AplicacaoException")) {
								t = t.getCause();
							} else if (t.getCause().getCause() != null
									&& t.getCause().getCause().getClass()
											.getSimpleName().equals(
													"AplicacaoException")) {
								t = t.getCause().getCause();
							}
						}
						// Get the ErrorData
						pageContext.getRequest().setAttribute("exception", t);
						java.io.StringWriter sw = new java.io.StringWriter();
						java.io.PrintWriter pw = new java.io.PrintWriter(sw);
						t.printStackTrace(pw);
						pageContext.getRequest().setAttribute("exceptionStack",
								sw.toString());

					}
		%>
	</c:if>
</c:catch>

<c:catch  var ="catchException">
	<siga:pagina titulo="Erro Geral">
<!--
Unless this text is here, if your page is less than 513 bytes, Internet Explorer will display it's "Friendly HTTP Error Message",
and your custom error will never be displayed.  This text is just used as filler.
This is a useless buffer to fill the page to 513 bytes to avoid display of Friendly Error Pages in Internet Explorer
This is a useless buffer to fill the page to 513 bytes to avoid display of Friendly Error Pages in Internet Explorer
This is a useless buffer to fill the page to 513 bytes to avoid display of Friendly Error Pages in Internet Explorer
-->

<table width="100%" height="100%">
	<tr>
		<td align="center" valign="middle">
		<table class="form" width="50%">
			<tr class="header">
				<td style="padding: 2pt; font-size: 14pt;">Não Foi Possível
				Completar a Operação</td>
			</tr>
			<c:catch>
				<c:if test="${not empty exception}">
					<c:if test="${not empty exception.message}">
						<tr>
							<td style="text-align: center; padding: 8pt; font-size: 14pt;">${exception.message}</td>
						</tr>
					</c:if>
					<c:if test="${not empty exception.cause}">

						<tr>
							<td style="text-align: center; padding: 8pt; font-size: 14pt;">${exception.cause.message}</td>
						</tr>
					</c:if>
				</c:if>
			</c:catch>
			<tr>
				<td style="text-align: center; padding: 8pt; font-size: 14pt;"><input
					type="button" id="show_stack" value="Mais detalhes"
					onclick="javascript: document.getElementById('stack').style.display=''; document.getElementById('show_stack').style.display='none';" />
				<div style="display: none" align="left" id="stack"><pre>${exceptionStack}</pre>
				</div>
				</td>
			</tr>

		</table>
		</td>
	</tr>
</table>
	</siga:pagina>
</c:catch>

<c:if test = "${catchException!=null}">
Erro: ${catchException.message}<br><br><br>

<pre>
Erro original:
${exceptionStack}</pre>
</c:if>


