<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page isErrorPage="true" import="java.io.*" contentType="text/html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<c:catch var="selectException">
	<c:if test="${empty exceptionGeral or empty exceptionStackGeral}">
		<%
			java.lang.Throwable t = (Throwable) pageContext.getRequest().getAttribute("exception");
			if (t == null){
				t = (Throwable) exception;
			}
			if (t != null) {
				if (!t.getClass().getSimpleName()
						.equals("AplicacaoException")
						&& t.getCause() != null) {
					if (t.getCause().getClass().getSimpleName()
							.equals("AplicacaoException")) {
						t = t.getCause();
					} else if (t.getCause().getCause() != null
							&& t.getCause().getCause().getClass()
									.getSimpleName()
									.equals("AplicacaoException")) {
						t = t.getCause().getCause();
					}
				}
				// Get the ErrorData
				pageContext.getRequest().setAttribute("exceptionGeral", t);
				java.io.StringWriter sw = new java.io.StringWriter();
				java.io.PrintWriter pw = new java.io.PrintWriter(sw);
				t.printStackTrace(pw);
				pageContext.getRequest().setAttribute("exceptionStackGeral",
						sw.toString());
			}
		%>
	</c:if>
</c:catch>
<c:catch var="catchException">
	<siga:pagina titulo="Erro Geral" desabilitarbusca="sim" desabilitarmenu="sim" desabilitarComplementoHEAD="sim">

		<jsp:include page="main.jsp"></jsp:include>
		
		
		<div class="gt-bd clearfix">
			<div class="gt-content clearfix">

				<div id="caption" class="gt-error-page-hd">
					<h2>Não Foi Possível Completar a Operação (${pageContext.request.serverName})</h2>
						 <%-- <c:forEach items="${requestScope}" var='p'>
							<ul>
            				<li>Parameter Name: <c:out value='${p.key}'/></li>
							<li>Parameter Value: <c:out value='${p.value}'/></li>
      						</ul>
      					</c:forEach>--%>
				</div>

				<div class="gt-content-box">
					<table width="100%">
						<tr>
							<td align="center" valign="middle">
								<table class="form" width="50%">
									<c:catch>
										<c:if test="${not empty exceptionGeral}">
											<c:if test="${not empty exceptionGeral.message}">
												<tr>
													<td style="text-align: center; padding-top: 10px;"><h3 id="message">${exceptionGeral.message}</h3>
													</td>
												</tr>
											</c:if>
											<c:if test="${not empty exceptionGeral.cause}">

												<tr>
													<td 
														style="text-align: center;"><h4 id="details">${exceptionGeral.cause.message}</h4></td>
												</tr>
											</c:if>
										</c:if>
									</c:catch>
								</table>
							</td>
						</tr>
						<tr>
							<td style="text-align: center; padding:0;">
								<div style="display: none; padding: 8pt;" align="left" id="stack">
									<pre style="font-size: 8pt;">${exceptionStackGeral}</pre>
								</div>
							</td>
						</tr>
					</table>
				</div>
				<div style="padding-top:10px;float: right;">
					<table>
						<tr>
							<td><input type="button" value="Voltar" class="gt-btn-medium gt-btn-left"  onclick="javascript:history.back();" /></td>
							<td><input type="button" id="show_stack" value="Mais detalhes" class="gt-btn-large gt-btn-right" onclick="javascript: document.getElementById('caption').setAttribute('class',''); document.getElementById('stack').style.display=''; document.getElementById('show_stack').style.display='none';" /></td>
						</tr>
					</table>
				</div>
			</div>
		</div>

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


