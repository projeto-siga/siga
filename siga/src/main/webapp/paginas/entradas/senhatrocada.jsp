<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<siga:pagina titulo="${param.titulo}">

	<table border="0" width="100%">
		<tr>
			<td>
			<h1>${mensagem }</h1>
		</tr>
		<tr>
			<td><a href="/siga/"> <font size="2"> Ir para o SIGA
			</font> </a></td>
		</tr>
	</table>

</siga:pagina>


