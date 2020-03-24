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
 				while (t.getCause() != null && t != t.getCause()) {
 					if (t.getClass().getSimpleName().equals("AplicacaoException"))
						break;
 					t = t.getCause();
 				}
 				pageContext.getRequest().setAttribute("exceptionGeral", t);
 				java.io.StringWriter sw = new java.io.StringWriter();
 				java.io.PrintWriter pw = new java.io.PrintWriter(sw);
 				t.printStackTrace(pw);
 				String s = sw.toString();
 				String[] lines = s.split(System.getProperty("line.separator"));
 				for (int i=0; i<lines.length; i++) {
 					if (lines[i].contains("br.com.caelum.vraptor.core.DefaultReflectionProvider.invoke")) {
 						for (int j=i-1; j>0; j--) {
 							if (lines[j].trim().startsWith("at br.") && !lines[j].contains("$Proxy$_$$_WeldClientProxy")) {
				 				StringBuilder sb = new StringBuilder();
 								for (int k=0; k<=j; k++) {
 				 					sb.append(lines[k]);
 				 					sb.append(System.getProperty("line.separator"));
 								}
 								s = sb.toString();
 								break;
 							}			
 						}
 						break;
 					}
 				}
 				pageContext.getRequest().setAttribute("exceptionStackGeral", s);
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
						N&atilde;o Foi Poss&iacute;vel Completar a Opera&ccedil;&atilde;o (${pageContext.request.serverName})
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
								
								<!-- Inclui mensagens de validação -->
								<c:if test="${not empty errors}">
									<c:forEach var="error" items="${errors}">
	    								${error.category} - ${error.message}<br />
									</c:forEach>
								</c:if>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col">
							<div class="form-group">
								<div style="display: none; padding: 8pt;" align="left" id="stack">
									<pre style="font-size: 8pt;">${exceptionStackGeral}</pre>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col">
							<div class="form-group">		
								<input type="button" value="Voltar" class="btn btn-primary"  onclick="javascript:history.back();" />
								<input type="button" id="show_stack" value="Mais detalhes" class="btn btn-primary" onclick="javascript: document.getElementById('stack').style.display=''; document.getElementById('show_stack').style.display='none';" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</siga:pagina>
</c:catch>

<c:if test="${catchException!=null}">
	Erro: ${catchException.message}
	<br>
	<br>
	<br>

	<pre>
		Erro original:
		${exceptionStack}
	</pre>
</c:if>

