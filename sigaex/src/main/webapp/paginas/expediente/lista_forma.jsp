<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<siga:pagina titulo="Lista de Formas">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		
		<h2>Cadastro de Formas</h2>

			<div class="gt-content-box gt-for-table">

	<form id="listar" name="listar" method="GET" class="form100">
		<table class="gt-form-table">
			<tr class="header">
				<td align="center" valign="top" colspan="4">Dados para a pesquisa</td>
			</tr>
			<tr>
				<td class="tdLabel"><label for="script" class="label">Script:</label>
				</td>
				<td><input type="text" name="script" size="40" value="${script}"
					id="script" /></td>
			</tr>
			<tr>
				<td colspan="2"><siga:monobotao inputType="submit" value="Buscar" cssClass="gt-btn-small gt-btn-left" />
				</td>
			</tr>
		</table>
	</form>
	</div>

	<h3 style="margin-top: 25px;">Lista de Formas</h3>
	<div class="gt-content-box" style="margin-bottom: 25px;">
	<table class="gt-table" width="100%">
	<thead>
		<tr class="header">
			<th>Descrição</th>
			<th>Sigla</th>
			<th>Tipo</th>
            <th>Origem</th>
		</tr>
		</thead>
		<c:set var="evenorodd" value="" />
		<c:set var="tamanho" value="0" />
		<c:forEach var="forma" items="${itens}">
			<tr class="${evenorodd}">
				<td>${forma.descrFormaDoc}</td>
				<td>${forma.sigla}</td>
				<td>${forma.exTipoFormaDoc.descricao}</td>
				<td>
					<c:forEach var="origem" items="${forma.exTipoDocumentoSet}">
						${origem.descricao}<br/>
					</c:forEach>
				</td>
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
	</div>
	<ww:form name="frm" action="editar" namespace="/forma" theme="simple"
		method="GET">
		<ww:submit value="Novo" cssClass="gt-btn-medium gt-btn-left"/>
	</ww:form>
</div></div>
</siga:pagina>
