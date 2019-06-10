<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Lista de Espécies Documentais">
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Cadastro de Esp&eacute;cies Documentais</h5>
			</div>
			<div class="card-body">
				<div class="row">
					<div class="col-sm-4">
						<div class="form-group">
							<label class="font-weight-bold">Ordenar Por</label>
						</div>
					</div>
				</div>
				<div class="row">
					<form id="listar" name="listar" method="GET" class="form100">
						<div class="col-sm-4">
							<div class="form-group">
								<div class="form-check form-check-inline">
									<input type="radio" id="order1" name="ordenar"
										value="descricao" onclick="this.form.submit();"
										class="form-check-input"><label
										class="form-check-label" for="order1">Descri&ccedil;&atilde;o</label>
										<input type="radio" id="order2" name="ordenar" value="sigla"
										onclick="this.form.submit();" class="form-check-input ml-2"><label
											class="form-check-label" for="order2">Sigla</label>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="row">
					<div class="col-sm-2">
						<form name="frm" action="editar" theme="simple" method="GET">
							<button type="submit" class="btn btn-primary">Novo</button>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm">
				<table id="tableOrder" class="table table-sm table-striped">
					<thead class="thead-dark">
						<tr class="header">
							<th class="th-sm">Descri&ccedil;&atilde;o</th>
							<th>Sigla</th>
							<th>Tipo</th>
							<th>Origem</th>
						</tr>
					</thead>
					<tbody class="table-bordered">
						<c:set var="evenorodd" value="" />
						<c:set var="tamanho" value="0" />
						<c:forEach var="forma" items="${itens}">
							<tr class="${evenorodd}">
								<td><a
									href="${pageContext.request.contextPath}/app/forma/editar?id=${forma.idFormaDoc}">${forma.descrFormaDoc}</a>
								</td>
								<td>${forma.sigla}</td>
								<td>${forma.exTipoFormaDoc.descricao}</td>
								<td><c:forEach var="origem"
										items="${forma.exTipoDocumentoSet}">
											${origem.descricao}<br />
									</c:forEach></td>
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
			</div>
		</div>
	</div>
</siga:pagina>
