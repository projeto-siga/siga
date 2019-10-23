<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page isErrorPage="true"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:catch var="selectException">
	<%
		pageContext.getRequest().setAttribute("thread", Thread.currentThread().getName());
		java.lang.Throwable t = (Throwable) pageContext.getRequest().getAttribute("exception");
		if (t == null){
			t = (Throwable) exception;
		}
	    Throwable cause = null; 
	    while(null != (cause = t.getCause())  && (t != cause) ) {
	        t = cause;
	    }
	    String tipoException = "Geral";
	    if (t.getClass().getSimpleName()
				.equals("AplicacaoException")) {
			tipoException = "AplicacaoException";
	    }
				
		// Get the ErrorData
		pageContext.getRequest().setAttribute("tipoException", tipoException);
		if (pageContext.getRequest().getAttribute("exceptionGeral") == null 
				|| pageContext.getRequest().getAttribute("exceptionStackGeral") == null) {
			pageContext.getRequest().setAttribute("exceptionGeral", t);
			java.io.StringWriter sw = new java.io.StringWriter();
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);
			t.printStackTrace(pw);
			pageContext.getRequest().setAttribute("exceptionStackGeral",
					sw.toString());
		}
	%>
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
					<c:if test="${siga_cliente == 'GOVSP'}">
						<c:if test="${tipoException != 'AplicacaoException'}">
							<div class="row">
								<div class="col">
									<div class="form-group">
								      <table id="idBasico" class="table">
								      	<tr>
								      		<td>Data/Hora:</td>
											<jsp:useBean id="now" class="java.util.Date" />
											<fmt:formatDate var="datahora" value="${now}" pattern="yyyy-MM-dd HH:mm:ss" />
								      		<td><c:out value="${datahora}" /></td>
								      	</tr>
										<c:if test="${docVO.sigla != null}">
									      	<tr>
									      		<td>Documento:</td>
									      		<td><c:out value="${docVO.sigla}" /></td>
									      	</tr>
									    </c:if>
								      	<tr>
								      		<td>Servidor:</td>
											<c:set var="threadServer" value="${fn:split(thread, '/')}" />
								      		<td><c:out value="${threadServer[0]}" /></td>
								      	</tr>
									  	<tr>
									  		<td>Usuário</td>
											<td><c:out value="${cadastrante.lotacao}" /> / <c:out value="${cadastrante.sigla}" /></td>
									  	</tr>
								      </table>
								    </div>
								</div>
							</div>
						</c:if>
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