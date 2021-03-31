<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Lista de Diagramas">
	<div class="container-fluid content mb-3">
		<h3>Lista de Diagramas</h3>
		<table class="table table-sm table-striped">
			<thead class="thead-dark">
				<tr class="header">
					<th>Sigla</th>
					<th>Nome</th>
					<th>Descrição</th>
					<th>Lotação Resp.</th>
				</tr>
			</thead>
			<tbody class="table-bordered">
				<c:forEach var="item" items="${itens}">
					<tr>
						<td><c:url var="url" value="exibir">
								<c:param name="id" value="${item.id}" />
							</c:url> <a href="${url}">${item.sigla}</a></td>
						<td>${item.nome}</td>
						<td>${item.descr}</td>
						<td><siga:selecionado sigla="${item.lotaResponsavel.siglaCompleta}"
								descricao="${item.lotaResponsavel.descricao}"
								lotacaoParam="${item.lotaResponsavel.siglaCompleta}" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<form>
			<div class="gt-table-buttons">
				<a href="editar" class="btn btn-primary" style="cursor: pointer;"
					accesskey="n"><u>N</u>ovo Diagrama</a>
			</div>
		</form>
	</div>
</siga:pagina>
