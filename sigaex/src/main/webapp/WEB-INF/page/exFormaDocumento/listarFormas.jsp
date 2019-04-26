<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Lista de Formas">
	<div class="container-fluid mb-3">
		<div class="card bg-light mb-3" >		
			<div class="card-header"><h5>Cadastro de Esp&eacute;cies Documentais</h5></div>

				<div class="card-body">

					<form id="listar" name="listar" method="GET" class="form100">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<label>Ordenar Por</label>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<input type="radio" name="ordenar" value="descricao" onclick="this.form.submit();">Descri&ccedil;&atilde;o</input>
									<input type="radio" name="ordenar" value="sigla" onclick="this.form.submit();">Sigla</input>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
	<h5>Lista de Esp&eacute;cies Documentais</h5>
	
	<table border="0" class="table table-sm table-striped">
	<thead class="thead-dark">
		<tr class="header">
			<th>Descri&ccedil;&atilde;o</th>
			<th>Sigla</th>
			<th>Tipo</th>
            <th>Origem</th>
		</tr>
		</thead>
		<c:set var="evenorodd" value="" />
		<c:set var="tamanho" value="0" />
		<c:forEach var="forma" items="${itens}">
			<tr class="${evenorodd}">
				<td>
				   <a href="${pageContext.request.contextPath}/app/forma/editar?id=${forma.idFormaDoc}">${forma.descrFormaDoc}</a>
				</td>
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
	
	<form name="frm" action="editar" theme="simple"
		method="GET">
		<input type="submit" value="Novo" class="btn btn-primary"/>
	</form>
</div>
</siga:pagina>
