<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page isErrorPage="true"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
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
 				if (!t.getClass().getSimpleName().equals("AplicacaoException") && t.getCause() != null) {
 					if (t.getCause().getClass().getSimpleName().equals("AplicacaoException")) {
 						t = t.getCause();
 					} else if (t.getCause().getCause() != null && t.getCause().getCause().getClass().getSimpleName().equals("AplicacaoException")) {
 						t = t.getCause().getCause();
 					}
 				}
// 				// Get the ErrorData
 				pageContext.getRequest().setAttribute("exceptionGeral", t);
 				java.io.StringWriter sw = new java.io.StringWriter();
 				java.io.PrintWriter pw = new java.io.PrintWriter(sw);
 				t.printStackTrace(pw);
 				pageContext.getRequest().setAttribute("exceptionStackGeral", sw.toString());
 			}
		%>
	</c:if>
</c:catch>

<c:catch var="catchException">
	<siga:pagina titulo="Erro Geral" desabilitarbusca="sim" desabilitarmenu="sim" desabilitarComplementoHEAD="sim">
	<!--
	Unless this text is here, if your page is less than 513 bytes, Internet Explorer will display it's "Friendly HTTP Error Message",
	and your custom error will never be displayed.  This text is just used as filler.
	This is a useless buffer to fill the page to 513 bytes to avoid display of Friendly Error Pages in Internet Explorer
	This is a useless buffer to fill the page to 513 bytes to avoid display of Friendly Error Pages in Internet Explorer
	This is a useless buffer to fill the page to 513 bytes to avoid display of Friendly Error Pages in Internet Explorer
	-->

		<div class="container-fluid">
			<div class="card bg-light mb-3" >
				<div class="card-header">
					<h5>
						Não Foi Possível Completar a Operação (${pageContext.request.serverName})
					</h5>
				</div>

				<div class="card-body">
					<div class="row">
						<div class="col">
							<div class="form-group">
									<c:catch>
										<c:if test="${not empty exceptionGeral}">
											<c:if test="${not empty exceptionGeral.message}">
												<h3>${exceptionGeral.message}</h3>
											</c:if>
											<c:if test="${not empty exceptionGeral.cause}">
												<h4>${exceptionGeral.cause.message}</h4>
											</c:if>
										</c:if>
									</c:catch>
								</div>
							</div>
						</div>
						<c:if test="${siga_cliente != 'GOVSP'}">
							<div class="row">
								<div class="col">
									<div class="form-group">
										<div style="display: none; padding: 8pt;" align="left" id="stack">
											<pre style="font-size: 8pt;">${exceptionStackGeral}</pre>
										</div>
									</div>
								</div>
							</div>
						</c:if>
						<div class="row">
							<div class="col">
								<div class="form-group">		
									<input type="button" value="Voltar" class="btn btn-primary"  onclick="javascript:history.back();" />
									<c:if test="${siga_cliente != 'GOVSP'}">
										<input type="button" id="show_stack" value="Mais detalhes" class="btn btn-primary" onclick="javascript: document.getElementById('caption').setAttribute('class',''); document.getElementById('stack').style.display=''; document.getElementById('show_stack').style.display='none';" />
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
Erro: ${catchException.message}<br>
	<br>
	<br>

	<pre>
Erro original:
${exceptionStack}</pre>
</c:if>