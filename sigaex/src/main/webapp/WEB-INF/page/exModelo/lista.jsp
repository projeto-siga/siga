<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Lista de Modelos">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">

			<h2>Cadastro de Modelos</h2>

			<div class="gt-content-box gt-for-table">

				<form id="listar" name="listar" method="GET" class="form100">
					<table class="gt-form-table">
						<tr class="header">
							<td align="center" valign="top" colspan="4">Dados para a
								pesquisa</td>
						</tr>
						<tr>
							<td class="tdLabel"><label for="script" class="label">Script:</label>
							</td>
							<td><input type="text" name="script" size="40"
								value="${script}" id="script" /></td>
						</tr>
						<tr>
							<td colspan="2"><siga:monobotao inputType="submit"
									value="Buscar" cssClass="gt-btn-small gt-btn-left" /></td>
						</tr>
					</table>
				</form>
			</div>

			<h3 style="margin-top: 25px;">Lista de Modelos</h3>
			<div class="gt-content-box" style="margin-bottom: 25px;">
				<table class="gt-table" width="100%">
					<thead>
						<tr class="header">
							<th>Forma</th>
							<th>Modelo</th>
							<th align="center">Tecnologia</th>
							<th align="center">Class. Documental</th>
							<th align="center">Class. Documental para Vias</th>
						</tr>
					</thead>
					<c:set var="evenorodd" value="" />
					<c:set var="tamanho" value="0" />
					<c:forEach var="modelo" items="${itens}">
						<tr class="${evenorodd}">
							<td width="20%">${modelo.exFormaDocumento.descrFormaDoc}</td>
							<td width="50%"><c:url var="url" value="editar">
									<c:param name="id" value="${modelo.idMod}" />
								</c:url> <a href="${url}">${modelo.nmMod} </a></td>
							<td align="center" width="10%"><c:if
									test="${modelo.conteudoTpBlob == 'template/freemarker'}">Freemarker</c:if>
								<c:if
									test="${empty modelo.conteudoTpBlob or modelo.conteudoTpBlob == 'template-file/jsp'}">JSP</c:if>
							</td>
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
			</div>
			<form name="frm" action="editar" method="GET">
				<input type="submit" value="Novo" class="gt-btn-medium gt-btn-left" />
				<input type="button" value="Exportar Zip"
					onclick="window.location.href = 'exportar'"
					class="gt-btn-medium gt-btn-left" /> <input type="button"
					value="Exportar XML" onclick="window.location.href = 'exportarxml'"
					class="gt-btn-medium gt-btn-left" />
			</form>
		</div>
	</div>
</siga:pagina>
