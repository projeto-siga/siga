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
		function submitRel(act) {
			frmRelatorios = document.getElementById('frmRel');
			frmRelatorios.action = act;
			frmRelatorios.submit();
		}
		var newwindow = '';
		function visualizarRelatorio(rel) {
			frmRelatorios.tipoRel.value = "pdf";
			if (!newwindow.closed && newwindow.location) {
			} else {
				var popW = 600;
				var popH = 400;
				var winleft = (screen.width - popW) / 2;
				var winUp = (screen.height - popH) / 2;
				winProp = 'width=' + popW + ',height=' + popH + ',left='
						+ winleft + ',top=' + winUp
						+ ',scrollbars=yes,resizable'
				newwindow = window.open('', '', winProp);
				newwindow.name = 'doc';
			}

			newwindow.opener = self;
			t = frmRelatorios.target;
			a = frmRelatorios.action;
			frmRelatorios.target = newwindow.name;
			frmRelatorios.action = rel;
			frmRelatorios.submit();
			frmRelatorios.target = t;
			frmRelatorios.action = a;

			if (window.focus) {
				newwindow.focus()
			}
			return false;
		}
		
		function visualizarCVS(rel) {
			frmRelatorios.tipoRel.value = "cvs";
			t = frmRelatorios.target;
			a = frmRelatorios.action;
			frmRelatorios.action = rel;
			frmRelatorios.submit();
			frmRelatorios.action = a;

		}

	</script>
	<!-- main content -->
	<div class="container-fluid">
		<fmt:setLocale value="pt-BR" />
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h4>Relatórios Gerenciais</h4>
				<h5>Total de Documentos Por Órgão Interessado</h5>
			</div>
			<div class="card-body d-flex">
				<form name="frmRelatorios" id="frmRel" action="/sigaex/app/expediente/rel/carregar_lista_orgaos" theme="simple" method="get">
					<input type="hidden" name="tipoRel" id="tipoRel" />
					<div class="row">
						<jsp:include page="relGestaoInput.jsp" />
						<div class="form-group col-md-10">
							<input type="submit" value="Pesquisar" class="btn btn-primary mt-auto" />
							<input type="button" value="Voltar" onclick="javascript:history.back();" class="btn btn-cancel ml-2 mt-auto" />
						</div>
						<div class="form-group col-md-2">
							<div class="btn-group  float-right">	
								<a href="javascript:visualizarRelatorio('${pageContext.request.contextPath}/app/expediente/rel/relDocsOrgaoInteressadoPDF');" class="btn btn-primary float-right" role="button" aria-pressed="true" style="min-width: 80px;">Exportar PDF</a>
											<button type="button" class="btn btn-primary float-right dropdown-toggle dropdown-toggle-split" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
											    <span class="sr-only"></span>
										    </button>
									<div class="dropdown-menu">
									  	<a href="javascript:visualizarCVS('${pageContext.request.contextPath}/app/expediente/rel/relDocsOrgaoInteressadoPDF');" class="dropdown-item" role="button" aria-pressed="true">Exportar CSV</a>								   
									  </div>
							</div>
						</div>
					</div>
					<c:choose>
						<c:when test="${listOrgaos != null}">
							<div class="row">
								<div class="col-md-6">
									<div class="form-group">
										<label for="orgaoPesqId">Interessado</label>
										<select name="orgaoPesqId" id="orgaoPesqId" value="${orgaoPesqId}" class="form-control">
											<c:forEach items="${listOrgaos}" var="item">
												<option value="${item.idOrgaoUsu}"
													${item.idOrgaoUsu == orgaoPesqId ? 'selected' : ''}>
													${item.nmOrgaoUsu}
												</option>
											</c:forEach>
										</select>
									</div>					
								</div>
								<div class="col-md-2">
									<div class="form-group">
										<button class="btn btn-primary mt-4" type="button" 
											onclick="javascript:submitRel('/sigaex/app/expediente/rel/relDocsOrgaoInteressado');">
											Documentos
										</button>
									</div>
								</div>
							</div>
						</c:when>
						<c:otherwise>
							<input type="hidden" name="orgaoPesqId" id="orgaoPesqId" value="${orgaoPesqId}"/>
						</c:otherwise>
					</c:choose>
					<c:if test="${listLinhas != null}">
						<table class="table table-sm table-hover">
							<thead class="thead-light">
								<tr>
									<th><strong>Interessado: 
										${orgaoPesqName}
									</strong></th>
									<th><strong>Total de Documentos: 
										<fmt:formatNumber type="number" pattern="###,###,###,##0" value="${totalDocumentos}" />
									</strong></th>
								</tr>
							</thead>
						</table>
						<table class="table table-hover table-striped">
							<thead class="thead-dark align-middle text-center">
								<tr>
									<th class="text-left w-10">Unidade</th>
									<th class="text-left w-30">Nome do Documento</th>
									<th class="text-left w-15">Número do Documento</th>
									<th class="text-left w-15">Cadastrante</th>
									<th class="text-left w-15">Responsável pela Assinatura</th>
									<th class="text-center w-15">Data da Assinatura</th>
								</tr>
							</thead>
							<tbody class="table-bordered">
							<c:forEach items="${listLinhas}" var="item" varStatus="status">
								<tr>
									<td>
										${item[0]}
									</td>
									<td>
										${item[1]}
									</td>
									<td>
										${item[2]}
									</td>
									<td>
										${item[3]}
									</td>
									<td>
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