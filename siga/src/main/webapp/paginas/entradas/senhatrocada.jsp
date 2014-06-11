<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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


