<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page isErrorPage="true" import="java.io.*" contentType="text/html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>  
<%@ taglib uri="http://localhost/libstag" prefix="f"%> 

<c:catch var="selectException">
	<c:if test="${empty exceptionGeral or empty exceptionStackGeral}">
		<%
		
		
			try {
				Cookie[] cookies = request.getCookies();
				String jsessionid = "";
				if (cookies != null) {
					for (Cookie c : cookies) {
						if ("JSESSIONID".equals(c.getName())) {
							jsessionid = c.getValue();
							break;
						}
					}
				}
				
				String serverName = java.util.Base64.getEncoder().encodeToString(jsessionid.split("[.]")[1].getBytes()); 
	
				pageContext.getRequest().setAttribute("serverName", serverName);
			} catch (Exception e) {
				pageContext.getRequest().setAttribute("serverName", "* Ambiente fora do padrão");
			}
		
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
	<siga:pagina titulo="Erro Geral" desabilitarbusca="sim"	desabilitarmenu="sim" desabilitarComplementoHEAD="sim">
		<c:set var="hasMessage" scope="request" value="${not empty exceptionStackGeral or not empty exceptionMsg}" />
		<c:choose>
			<c:when test="${exceptionApp}">
				<c:set var="titleException" scope="request" value="Operação Inválida" />
				<c:set var="msgException" scope="request" value="${exceptionMsg}" />
				<c:set var="classMsg" scope="request" value="alert alert-warning" />
				<c:set var="contextException" scope="request" value="${pageContext.request.serverName}" />
			</c:when>
			<c:otherwise>
				<c:set var="titleException" scope="request" value="Não Foi Possível Completar a Operação" />
				<c:set var="msgException" scope="request" value="<pre style='font-size: 8pt;overflow: auto!important;max-height: 100px;'>${exceptionStackGeral}</pre>" />
				<c:set var="classMsg" scope="request" value="alert alert-danger" />
				<c:set var="contextException" scope="request" value="${pageContext.request.serverName}" />
			</c:otherwise>
		</c:choose>

		<div class="jumbotron pt-2" style='font-size: 10pt;'>
		  <h4 class="display-5">${titleException}<span class="lead">&nbsp;(${contextException})</span></h4>
		  <c:if test="${hasMessage}"> 
		  	<div class="${classMsg}" role="alert">${msgException}</div>
		  </c:if>
          <c:if test="${tipoException != 'AplicacaoException'}">  
				<div class="card" >
				  <div class="card-header bg-dark text-white">
				  	Dados da Execução
				  </div>
				  <ul class="list-group list-group-flush">
				  	<li class="list-group-item">
				  		<strong>Usuário</strong> <c:out value="${cadastrante.lotacao}" /> / <c:out value="${cadastrante.sigla}" />
				    	&nbsp;<strong>Data/Hora</strong>
				    	<jsp:useBean id="now" class="java.util.Date" />  
						<fmt:formatDate var="datahora" value="${now}" pattern="dd/MM/yyyy HH:mm:ss" /> 
						<c:out value="${datahora}" />
				    </li>
				    <c:if test="${param.sigla != null}"> 
				    	<li class="list-group-item"><strong>Documento </strong><c:out value="${param.sigla}" /></li>
				    </c:if>  
				    <li class="list-group-item">
				    	<strong>Ambiente </strong>
				    	<c:out value="${f:resource('/siga.ambiente')}" /> [<c:out value="${serverName}" />]
				    	&nbsp;<strong>Versão </strong>SIGA <c:out value="${siga_version}" />
				    </li>
				  </ul>
				  <div class="card-footer text-center pt-2">
				   	<c:if test="${newWindow != 1}">
						<input type="button" value="Voltar"	onclick="javascript:history.back();" class="btn btn-primary" />
					</c:if>
					<c:if test="${newWindow eq 1}">
						<input type="button" value="Fechar"	onclick="javascript:window.close();" class="btn btn-primary" />
					</c:if>
				  </div>
				</div> 
			</c:if> 
		</div>


	</siga:pagina>
</c:catch>	