<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>

<%@ taglib prefix="ww" uri="/webwork"%>

<c:set var="titulo_pagina" scope="request">Relatório de Classificação Documental</c:set>

<tr>
	<td>Digite a classificação desejada (opcional):</td>
	<td><input type="text" id="codificacao" name="codificacao"></td>
</tr>
