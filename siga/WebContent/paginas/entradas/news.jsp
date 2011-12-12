<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<siga:pagina popup="true" titulo="&Uacute;ltimas Novidades">
<table style="padding-left: 60px; padding-top: 60px">
<tr>
<td>
<jsp:include flush="true" page="comentario.jsp" />
</td>
</tr>
</table>
</siga:pagina>