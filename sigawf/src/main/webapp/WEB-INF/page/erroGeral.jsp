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
						while ((t.getClass().getSimpleName().equals("ServletException")
								|| t.getClass().getSimpleName().equals("InterceptionException")
								|| t.getClass().getSimpleName().equals("InvocationTargetException")
								|| t.getClass().getSimpleName().equals("ProxyInvocationException"))
								&& t.getCause() != null && t != t.getCause()) {
							t = t.getCause();
						}

						pageContext.getRequest().setAttribute("exceptionApp",
								t instanceof br.gov.jfrj.siga.base.AplicacaoException);
						pageContext.getRequest().setAttribute("exceptionMsg", t.getMessage());
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

					<c:choose>
						<c:when test="${exceptionApp}">
							<p class="mb-2 mt-5">Operação Inválida:</p>
							<h2 class="mt-0">${exceptionMsg}</h2>
						</c:when>
						<c:otherwise>
							<div id="caption">
								${t.exceptionStackGeral }
								<h2>Não Foi Possível Completar a Operação
									(${pageContext.request.serverName})</h2>
							</div>
							<div align="left" id="stack">
								<pre style="font-size: 8pt;"><c:out value="${exceptionStackGeral}" /></pre>
							</div>
						</c:otherwise>
					</c:choose>

					<div class="row mt-3">
						<div class="col">
							<div class="form-group">
							<c:if test="${newWindow != 1}">
								<input type="button" value="Voltar"
									onclick="javascript:history.back();" class="btn btn-secondary btn-sm" />
							</c:if>
							<c:if test="${newWindow eq 1}">
								<input type="button" value="Fechar"
									onclick="javascript:window.close();" class="btn btn-secondary btn-sm" />
							</c:if>
							</div>
						</div>
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
