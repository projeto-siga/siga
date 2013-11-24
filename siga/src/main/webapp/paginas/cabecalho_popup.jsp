<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<c:if test="true">
	<html>
	<head>
	<title>SIGA - ${titulo_pagina}</title>
	<META HTTP-EQUIV="Expires" CONTENT="0">
	<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
	<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
	<META HTTP-EQUIV="content-type" CONTENT="text/html; charset=UTF-8">
	${cabecalho_meta}
	<c:import url="/paginas/estilos.jsp" />
	</head>
	<c:url var='menusep' value='/imagens/menusep.gif' />
	<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
	<TABLE WIDTH="100%" height="100%" BORDER=0 CELLPADDING=0 CELLSPACING=0>
		<TR>
			<TD valign="top">
				</c:if>
				<c:if test="${title=='false'}">
			</TD>
		</TR>
	</TABLE>
	</body>
	</html>
</c:if>
