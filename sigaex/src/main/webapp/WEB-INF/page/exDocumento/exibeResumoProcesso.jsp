<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<%@page import="br.gov.jfrj.siga.ex.ExDocumento"%>
<%@page import="br.gov.jfrj.siga.ex.ExArquivoNumerado"%>
<%@page import="br.gov.jfrj.siga.ex.ExArquivo"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashSet"%>
<%@page import="br.gov.jfrj.siga.ex.ExMobil"%>

<c:set var="path" scope="request">${pageContext.request.contextPath}</c:set>
<link rel="StyleSheet" href="${path}/sigalibs/siga.css" type="text/css"
	title="SIGA Estilos" media="screen">
	<script src="${path}/sigalibs/ajax.js" language="JavaScript1.1"
		type="text/javascript"></script>
	<script src="${path}/sigalibs/static_javascript.js"
		language="JavaScript1.1" type="text/javascript"></script>

	<link href="${pageContext.request.contextPath}/sigalibs/menu.css"
		rel="stylesheet" type="text/css" />

	<%
		ExMobil proc = (ExMobil) request.getAttribute("mob");
		List<ExArquivoNumerado> an = proc.getArquivosNumerados();
		request.setAttribute("arqsNum", an);
		HashSet<String> formas = new HashSet<String>();
		for (ExArquivoNumerado a : an) {
			if (a.getArquivo() instanceof ExDocumento) {
				if (a.getArquivo().getResumo().size() > 0) {
					formas.add(((ExDocumento) a.getArquivo()).getExModelo()
							.getNmMod());
				}
			}
		}
		request.setAttribute("formas", formas);
	%>

	<%@page import="br.gov.jfrj.siga.ex.ExMovimentacao"%>

	<html>
<body leftmargin="0px" topmargin="3px">
	<table bordercolor="#000000" cellpadding="10px"
		style="border-width: 2px; border-style: solid; table-layout: auto; width: 100%;">
		<tr>
			<td>

				<table class="list" style="width: 100%;">
					<tr class="header">
						<b>Anexações&nbsp;com&nbsp;motivo</b>
					</tr>
					<tr class="header">
						<td>Documento</td>
						<td>Motivo</td>
					</tr>
					<c:forEach var="arqNumerado" items="${arqsNum}">

						<c:set var="arquivo" value="${arqNumerado}" scope="request" />
						<%
							ExArquivo arq = ((ExArquivoNumerado) request
										.getAttribute("arquivo")).getArquivo();

								if (arq instanceof ExMovimentacao) {
									request.setAttribute("mov", (ExMovimentacao) arq);
						%>
						<c:if
							test="${mov.exTipoMovimentacao == 'ANEXACAO' && !empty mov.descrMov}">
							<tr>
								<td><a
									href="javascript:exibir('./${arqNumerado.referenciaPDF}')">${arqNumerado.nome}</a>
								</td>
								<td>${mov.descrMov}</td>
							</tr>
						</c:if>
						<%
							}
						%>
					</c:forEach>

				</table> <br /> <!-- --> <c:forEach var="forma" items="${formas}">
					<c:set var="jaTemCabecalho" value="nao" scope="page" />
					<table class="list" style="width: 100%;">
						<c:forEach var="arqNumerado" items="${arqsNum}">
							<c:set var="arquivo" value="${arqNumerado}" scope="request" />
							<%
								ExArquivo arquivo = ((ExArquivoNumerado) request
												.getAttribute("arquivo")).getArquivo();
										if (arquivo instanceof ExDocumento) {
							%>
							<c:if test="${!empty arqNumerado.arquivo.resumo}">
								<c:if test="${forma == arqNumerado.arquivo.exModelo.nmMod}">
									<c:if test="${jaTemCabecalho == 'nao'}">
										<tr class="header">
											<b>${fn:replace(forma, " ", "&nbsp")}</b>
										</tr>
										<tr class="header">
											<td>Documento</td>
											<c:forEach var="topico" items="${arqNumerado.arquivo.resumo}">
												<td style="text-align: center">${topico.key}</td>
												<c:set var="jaTemCabecalho" value="sim" />
											</c:forEach>
										</tr>
									</c:if>
									<tr>
										<td><a
											href="javascript:exibir('./${arqNumerado.referenciaPDF}')">${arqNumerado.nome}</a>
										</td>
										<c:forEach var="topico" items="${arqNumerado.arquivo.resumo}">
											<c:set var="em" value="" />
											<%-- <c:if test="${f:dataFutura(topico.value)}">--%>
											<c:set var="em" value="background-color:#AfFFAf;" />
											<%-- </c:if>--%>
											<td style="text-align: center;${em}">${topico.value}</td>
										</c:forEach>
									</tr>
								</c:if>
							</c:if>
							<%
								}
							%>
						</c:forEach>
					</table>
					<br>
				</c:forEach>
			</td>
		</tr>
	</table>
</body>
	</html>