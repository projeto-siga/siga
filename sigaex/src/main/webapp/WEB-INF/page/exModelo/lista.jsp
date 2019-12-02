<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Lista de Modelos">
	<div class="container-fluid content mb-3">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5 class="mb-0"><fmt:message key = "modelo.lista.form.titulo"/></h5>
			</div>
			<div class="card-body">
				<form id="listar" name="listar" method="get" class="form100">

					<div class="form-row">
						<div class="form-group col-md-6">
							<label for="ultMovIdEstadoDoc">Script</label> <input type="text"
								name="script" size="40" value="${script}" id="script"
								class="form-control" />
						</div>
					</div>
					<input type="submit" class="btn btn-primary" value="Pesquisar"></input>
				</form>
			</div>
		</div>

		<h3>Lista de Modelos</h3>
		<table class="table table-sm table-striped">
			<thead class="${thead_color}">
				<tr class="header">
					<th>Forma</th>
					<th>Modelo</th>
					<th align="center">Tecnologia</th>
					<th align="center">Class. Documental</th>
					<th align="center">Class. Documental para Vias</th>
				</tr>
			</thead>
			<tbody class="table-bordered">
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
			</tbody>
		</table>
		<form>
			<div class="gt-table-buttons">
				<a href="editar" class="btn btn-primary" style="cursor: pointer;"
					accesskey="n"><u>N</u>ovo Modelo</a> <a href="exportar"
					class="btn btn-primary" style="cursor: pointer;">Exportar Zip</a> <a
					href="exportarxml" class="btn btn-primary" style="cursor: pointer;">Exportar
					XML</a>
			</div>
		</form>
	</div>
</siga:pagina>
