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
		var newwindow = '';
		function visualizarRelatorio(rel, idFormaDoc, descrFormaDoc) {
			if (!newwindow.closed && newwindow.location) {
			} else {
				var popW = 1000;
				var popH = 600;
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
			document.getElementById("idFormaDoc").value = idFormaDoc;
			document.getElementById("descrFormaDoc").value = descrFormaDoc;
			frmRelatorios.action = rel;
			frmRelatorios.submit();
			frmRelatorios.target = t;
			frmRelatorios.action = a;

			if (window.focus) {
				newwindow.focus()
			}
			return false;
		}
	</script>

	<!-- main content -->
	<div class="container-fluid">
		<fmt:setLocale value="pt-BR" />
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h4>Relatórios Gerenciais</h4>
				<h5>Tempo Médio de Tramitação Por Espécie Documental</h5>
			</div>
			<div class="card-body d-flex">
				<form name="frmRelatorios" id="frmRel" action="/sigaex/app/expediente/rel/relTempoTramitacaoPorEspecie" theme="simple" method="get">
					<div class="row">
						<jsp:include page="relGestaoInput.jsp" />
						<div class="form-group col-md-4">
							<input type="submit" value="Pesquisar" class="btn btn-primary mt-auto" />
							<input type="button" value="Voltar" onclick="javascript:history.back();" class="btn btn-cancel ml-2 mt-auto" />
						</div>
					</div>
					<c:if test="${listLinhas != null}">
						<input type="hidden" name="descrFormaDoc" id="descrFormaDoc" />
						<input type="hidden" name="idFormaDoc" id="idFormaDoc" />
						<table class="table table-sm table-hover">
							<thead class="thead-light">
								<tr>
									<th><strong>Documentos Tramitados : 
										<fmt:formatNumber type="number" pattern="###,###,###,##0" value="${totalDocumentos}" />
									</strong></th>
								</tr>
							</thead>
						</table>
						<table class="table table-hover table-striped">
							<thead class="thead-dark align-middle text-center">
								<tr>
									<th class="text-left w-80">Espécie Documental</th>
									<th class="text-center w-10">Total de Documentos Tramitados</th>
									<th class="text-right w-10 mx-5">Tempo Médio (dias)</th>
								</tr>
							</thead>
							<tbody class="table-bordered">
							<c:forEach items="${listEspecie}" var="itemEspecie">
								<tr>
									<td>
										${itemEspecie[0]}
									</td>
									<td class="text-center">
										<fmt:formatNumber type="number" pattern="###,###,###,##0" value="${itemEspecie[1]}" />
									</td>
									<td class="mx-auto">
										<div class="text-right mx-5">
											<fmt:formatNumber type="number" pattern="###,###,###,##0" value="${itemEspecie[2]}" />
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