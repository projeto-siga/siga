<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<siga:pagina titulo="Lista de Modelos">
	<h1>Cadastro de Modelos</h1>

	<form id="listar" name="listar" method="GET" class="form100">
		<table class="form100">
			<tr class="header">
				<td align="center" valign="top" colspan="4">Dados do Modelo</td>
			</tr>
			<tr>
				<td class="tdLabel"><label for="script" class="label">Script:</label>
				</td>
				<td><input type="text" name="script" size="40" value="${script}"
					id="script" /></td>
			</tr>
			<tr>
				<td></td>
				<td><siga:monobotao inputType="submit" value="Buscar" />
				</td>
			</tr>
		</table>
	</form>


	<table class="list" width="100%">
		<tr class="header">
			<td>Forma</td>
			<td>Modelo</td>
			<td align="center">Nivel de Acesso</td>
			<td align="center">Class. Documental</td>
			<td align="center">Class. Documental para Vias</td>
		</tr>
		<c:set var="evenorodd" value="" />
		<c:set var="tamanho" value="0" />
		<c:forEach var="modelo" items="${itens}">
			<tr class="${evenorodd}">
				<td width="20%">${modelo.exFormaDocumento.descrFormaDoc}</td>
				<td width="50%"><ww:url id="url" action="editar"
						namespace="/modelo">
						<ww:param name="id">${modelo.idMod}</ww:param>
					</ww:url> <ww:a href="%{url}">${modelo.nmMod}
				</ww:a></td>
				<td align="center" width="10%">${modelo.exNivelAcesso.nmNivelAcesso}</td>
				<td align="center" width="10%">${modelo.exClassificacao.sigla}</td>
				<td align="center" width="10%">${modelo.exClassCriacaoVia.sigla}</td>
			</tr>
			<c:choose>
				<c:when test='${evenorodd == "even"}'>
					<c:set var="evenorodd" value="odd" />
				</c:when>
				<c:otherwise>
					<c:set var="evenorodd" value="even" />
				</c:otherwise>
			</c:choose>
			<c:set var="tamanho" value="${tamanho + 1 }" />
		</c:forEach>
	</table>
	<br />
	<ww:form name="frm" action="editar" namespace="/modelo" theme="simple"
		method="GET">
		<ww:submit value="Novo" />
	</ww:form>

</siga:pagina>
