<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Lista de Diagramas">
	<div class="container-fluid content mb-3">
		<h3>Lista de Responsáveis</h3>
		<table class="table table-sm table-striped">
			<thead class="thead-dark">
				<tr class="header">
					<th>Nome</th>
					<th>Descrição</th>
				</tr>
			</thead>
			<tbody class="table-bordered">
				<c:forEach var="item" items="${itens}">
					<tr>
						<td><a
							href="${linkTo[WfResponsavelController].edita(item.id)}">${item.nome}</a></td>
						<td>${item.descr}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<form>
			<div class="gt-table-buttons">
				<a href="novo" class="btn btn-primary" style="cursor: pointer;"
					accesskey="n"><u>N</u>ovo Responsável</a>
			</div>
		</form>
	</div>
</siga:pagina>
