<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Relatório">
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h4>Relatórios Gerenciais</h4>
				<h5>Tempo Médio por Situação</h5>
			</div>
			<div class="card-body d-flex">
				<form name="frmRelatorios"
					action="/sigaex/app/expediente/rel/relTempoMedioSituacao"
					theme="simple" method="get">
					<input type="hidden" name="postback" value="1" />
					<div class="row">
						<jsp:include page="relGestaoInput.jsp" />
						<div class="form-group col-md-4">
							<input type="submit" value="Pesquisar" class="btn btn-primary" />
							<input type="button" value="Voltar"
								onclick="javascript:history.back();" class="btn btn-primary" />
						</div>
					</div>
					<c:if test="${listModelos != null}">
						<table class="table table-sm table-hover">
							<thead class="thead-light">
								<tr>
								</tr>
							</thead>
						</table>
						<table class="table table-hover table-striped">
							<thead class="thead-dark align-middle text-center">
								<tr>
									<th class="text-left w-20">Unidade</th>
									<th class="text-left w-40">Nome do documento</th>
									<th class="text-left w-20">Situação</th>
									<th class="text-right w-20 mx-5">Tempo médio em dias</th>
								</tr>
							</thead>
							<tbody class="table-bordered">
							<c:forEach items="${listModelos}" var="itemModelos">
								<tr>
									<td>
										${itemModelos[0]}
									</td>
									<td>
										${itemModelos[1]}
									</td>
									<td>
										${itemModelos[2]}
									</td>
									<td class="mx-auto">
										<div class="text-right mx-5">
											<fmt:formatNumber type="number" pattern="###,###,###,##0" value="${itemModelos[3]}" />
										</div>
									</td>
								</tr>
							</c:forEach> 
							</tbody>
						</table>
					</c:if>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>