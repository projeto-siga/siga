<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Lista de Espécies Documentais">

<style>
	#tableOrder tr td span.badge:nth-last-child(2) {
		margin-bottom: 0!important;
	}
</style>
		
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
					<form id="listar" name="listar" method="get" class="form100">						
						<div class="col-sm-4">
							<div class="form-group">
								<div class="form-check form-check-inline">
									<input type="radio" id="ordenadoPorDescricao" name="ordenar" value="descricao" onclick="this.form.submit();"
										class="form-check-input" ${ordenadoPor eq 'descricao' ? 'checked' : ''} />
									<label class="form-check-label" for="ordenadoPorDescricao">Descri&ccedil;&atilde;o</label>
									<input type="radio" id="ordenadoPorSigla" name="ordenar" value="sigla" onclick="this.form.submit();" 
										class="form-check-input ml-2" ${ordenadoPor eq 'sigla' ? 'checked' : ''} />
									<label class="form-check-label" for="ordenadoPorSigla">Sigla</label>
								</div>								
							</div>
						</div>
					</form>
				</div>
				<div class="row">
					<div class="col-sm-2">
						<form name="frm" action="editar" theme="simple" method="get">
							<button type="submit" class="btn btn-primary">Novo</button>
						</form>
					</div>
				</div>
			</div>
		</div>		
		<div class="row">
			<div class="col-sm">
				<table id="tableOrder" class="table table-sm table-striped">
					<thead class="${thead_color}">
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
								<td>
									<a href="${pageContext.request.contextPath}/app/forma/editar?id=${forma.idFormaDoc}">${forma.descrFormaDoc}</a>
								</td>
								<td>${forma.sigla}</td>
								<td class="pt-2">
									<span class="badge badge-${forma.exTipoFormaDoc.id eq 1 ? 'primary' : 'secondary'}">
										${forma.exTipoFormaDoc.descricao}
									</span>
								</td>
								<td class="pt-2">
									<c:forEach var="origem" items="${forma.exTipoDocumentoSet}">
										<c:choose>
											<c:when test="${origem.id eq 1}">												
												<span class="badge badge-primary  mb-1">
													${origem.descricaoSimples}
												</span>
												<br />
											</c:when>
											<c:when test="${origem.id eq 2}">												
												<span class="badge badge-info  mb-1">
													${origem.descricaoSimples}
												</span>
												<br />
											</c:when>
											<c:when test="${origem.id eq 3}">												
												<span class="badge badge-secondary  mb-1">
													${origem.descricaoSimples}
												</span>
												<br />
											</c:when>
											<c:when test="${origem.id eq 4}">												
												<span class="badge badge-dark  mb-1">
													${origem.descricaoSimples}
												</span>
												<br />
											</c:when>
											<c:when test="${origem.id eq 5}">												
												<span class="badge badge-light  mb-1">
													${origem.descricaoSimples}
												</span>
												<br />
											</c:when>
											<c:otherwise>
												<span class="badge badge-warning  mb-1">
													${origem.descricao}
												</span>
												<br />
											</c:otherwise>										
										</c:choose>										
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
					</tbody>
				</table>
			</div>
		</div>
	</div>
</siga:pagina>
