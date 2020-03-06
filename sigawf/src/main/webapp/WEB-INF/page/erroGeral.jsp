<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page isErrorPage="true" import="java.io.*" contentType="text/html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<c:catch var="selectException">
	<c:if test="${empty exceptionGeral or empty exceptionStackGeral}">
		<%
			java.lang.Throwable t = (Throwable) pageContext.getRequest().getAttribute("exception");
					if (t == null) {
						t = (Throwable) exception;
					}
					if (t != null) {
						pageContext.getRequest().setAttribute("exceptionGeral", t);
						pageContext.getRequest().setAttribute("exceptionStackGeral",
								br.gov.jfrj.siga.base.log.RequestExceptionLogger.simplificarStackTrace(t));
					}
		%>
	</c:if>
</c:catch>
<c:catch var="catchException">
	<siga:pagina titulo="Erro Geral" desabilitarbusca="sim"
		desabilitarmenu="sim" desabilitarComplementoHEAD="sim">
		<div class="container content">

			<!--
Unless this text is here, if your page is less than 513 bytes, Internet Explorer will display it's "Friendly HTTP Error Message",
and your custom error will never be displayed.  This text is just used as filler.
This is a useless buffer to fill the page to 513 bytes to avoid display of Friendly Error Pages in Internet Explorer
This is a useless buffer to fill the page to 513 bytes to avoid display of Friendly Error Pages in Internet Explorer
This is a useless buffer to fill the page to 513 bytes to avoid display of Friendly Error Pages in Internet Explorer
-->

			<div class="gt-bd clearfix">
				<div class="gt-content clearfix">

					<div id="caption">
						<h2>Não Foi Possível Completar a Operação
							(${pageContext.request.serverName})</h2>
					</div>

					<div align="left" id="stack">
						<pre style="font-size: 8pt;">${exceptionStackGeral}</pre>
					</div>
				</div>
			</div>

		</div>

	</siga:pagina>
</c:catch>

<c:if test="${catchException!=null}">
Erro: ${catchException.message}<br> <br> <br> <pre>
Erro original:
${exceptionStack}</pre>
</c:if>
