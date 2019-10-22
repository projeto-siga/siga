<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="128kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<siga:pagina titulo="Relatório">
	<script type="text/javascript" language="Javascript1.1">
		function visualizarRelatorio(rel, idMod) {
			document.getElementById("idMod").value = idMod;
			frmRelatorios.action = rel;
			frmRelatorios.submit();
		}
	</script>

	<!-- main content -->
	<div class="container-fluid">
		<fmt:setLocale value="pt-BR" />
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h4>Relatórios Gerenciais</h4>
				<h5>Volume de Tramitação Por Nome do Documento</h5>
			</div>
			<div class="card-body d-flex">
				<form name="frmRelatorios" id="frmRel" action="/sigaex/app/expediente/rel/relVolumeTramitacaoPorModelo" theme="simple" method="get">
					<div class="row">
						<jsp:include page="relGestaoInput.jsp" />
						<div class="form-group col-md-4">
							<input type="submit" value="Pesquisar" class="btn btn-primary mt-auto" />
							<input type="button" value="Voltar" onclick="javascript:history.back();" class="btn btn-cancel ml-2 mt-auto" />
						</div>
					</div>
					<c:if test="${listModelos != null}">
						<input type="hidden" name="idMod" id="idMod" />
						<table class="table table-sm table-hover">
							<thead class="thead-light">
								<tr>
									<th><strong>Total de Documentos : 
										<fmt:formatNumber type="number" pattern="###,###,###,##0" value="${totalDocumentos}" />
									</strong></th>
									<th><strong>Total de Tramitações : 
										<fmt:formatNumber type="number" pattern="###,###,###,##0" value="${totalTramites}" />
									</strong></th>
								</tr>
							</thead>
						</table>
						<table class="table table-hover table-striped">
							<thead class="thead-dark align-middle text-center">
								<tr>
									<th class="text-left w-80">Espécie Documental</th>
									<th class="text-left w-80">Nome do Documento</th>
									<th class="text-right w-10 mx-5">Tramitações</th>
								</tr>
							</thead>
							<tbody class="table-bordered">
							<c:forEach items="${listModelos}" var="itemModelo">
								<tr>
									<td>
										${itemModelo[0]}
									</td>
									<td>
										${itemModelo[1]}
									</td>
									<td class="mx-auto">
										<div class="text-right mx-5">
											<fmt:formatNumber type="number" pattern="###,###,###,##0" value="${itemModelo[2]}" />
										</div>
									</td>
								</tr>
							</c:forEach> 
							</tbody>
						</table>
					</c:if>
					<c:if test="${listLinhas != null}">
						<table class="table table-sm table-hover">
							<thead class="thead-light">
								<tr>
									<th><strong>Nome do Documento : 
										${modelo}
									</strong></th>
									<th><strong>Total de Tramitações : 
										<fmt:formatNumber type="number" pattern="###,###,###,##0" value="${totalModeloTramites}" />
									</strong></th>
								</tr>
							</thead>
						</table>
						<table class="table table-hover table-striped">
							<thead class="thead-dark align-middle text-center">
								<tr>
									<th class="text-left w-10">${listColunas[0]}</th>
									<th class="text-left w-40">${listColunas[1]}</th>
									<th class="text-center w-20">${listColunas[2]}</th>
									<th class="text-center w-10">${listColunas[3]}</th>
									<th class="text-center w-10">${listColunas[4]}</th>
									<th class="text-center w-10">${listColunas[5]}</th>
								</tr>
							</thead>
							<tbody class="table-bordered">
							<c:forEach items="${listLinhas}" var="item">
								<tr>
									<td>
										${item[0]}
									</td>
									<td>
										${item[1]}
									</td>
									<td class="text-center">
										${item[2]}
									</td>
									<td class="text-center">
										${item[3]}
									</td>
									<td class="text-center">
										${item[4]}
									</td>
									<td class="text-center">
										${item[5]}
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