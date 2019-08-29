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
		function visualizarRelatorio(rel) {
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
		function geraCsv(arq) {
		    var csv = [];
		    var rows = document.querySelectorAll("table tr");
		    for (var i = 0; i < rows.length; i++) {
		        var row = [], cols = rows[i].querySelectorAll("td, th");
		        for (var j = 0; j < cols.length; j++) 
		            row.push(decodeURIComponent(cols[j].innerText));
		        csv.push(row.join(";"));        
		    }
		    downloadCSV(csv.join("\n"), arq);
		}	
		function downloadCSV(csv, filename) {
		    var csvFile;
		    var downloadLink;
		    csvFile = new Blob([csv],{encoding:"ISO-8859-1",type:"text/csv;charset=ISO-8859-1"});
		    downloadLink = document.createElement("a");
		    downloadLink.download = filename;
		    downloadLink.href = window.URL.createObjectURL(csvFile);
		    downloadLink.style.display = "none";
		    document.body.appendChild(downloadLink);
		    downloadLink.click();
		}			
	</script>

	<!-- main content -->
	<div class="container-fluid">
		<fmt:setLocale value="pt-BR" />
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h4>Relatórios Gerenciais</h4>
				<h5>Relatório de Cobrança - Órgão ${lotaTitular.orgaoUsuario.descricaoMaiusculas}</h5>
			</div>
			<div class="card-body d-flex">
				<form name="frmRelatorios" action="/sigaex/app/expediente/rel/relArmazenamento" theme="simple" method="get">
					<div class="row">
						<div class="col-md-6">
							<div class="form-group">
								<label for="orgaoPesqId">Órgão</label>
								<select name="orgaoPesqId" id="orgaoPesqId" value="${orgaoPesqId}" class="form-control">
									<option value="0"
										${item.idOrgaoUsu == orgaoPesqId ? 'selected' : ''}>
										[Todos]
									</option>
									<c:forEach items="${listOrgaos}" var="item">
										<option value="${item.idOrgaoUsu}"
											${item.idOrgaoUsu == orgaoPesqId ? 'selected' : ''}>
											${item.nmOrgaoUsu}
										</option>
									</c:forEach>
								</select>
							</div>					
						</div>
						<jsp:include page="relGestaoInput.jsp" />
						<div class="col-sm-2">
							<div class="form-group">
								<div class="form-check form-check-inline mt-4">
									<input type="checkbox" name="getAll" />
									<label class="form-check-label ml-1" for="getAll">Sem Dados dos Anexos</label>
								</div>
							</div>
						</div>
						
					</div>
					<div class="row">
						<div class="form-group col-sm-4">
							<input type="submit" value="Pesquisar" class="btn btn-primary mt-auto" />
							<input type="button" value="Voltar" onclick="javascript:history.back();" class="btn btn-cancel ml-2 mt-auto" />
						</div>
					</div>
<%-- 					<c:if test="${totalDocumentos != null}"> --%>
					<c:if test="${false}">
						<div class="row">
							<div class="col-sm-12">
								<div class="card">
									<div class="card-header">Indicadores de Produção</div>
									<div class="card-body">
										<table class="table table-sm table-hover table-striped">
											<tr class="card-text col-sm-6">
												<td class='w-80'>Total de Documentos Produzidos</td>
												<td class='text-right'>
													<fmt:formatNumber type="number" pattern="###,###,###,##0" value="${totalDocumentos}" />
												</td>
											</tr>
											<tr>
												<td class='w-80'>Total de Páginas Geradas</td>
												<td class='text-right'>
													<fmt:formatNumber type = "number" pattern = "###,###,###,##0" value = "${totalPaginas}" />
												</td>
											</tr>
											<tr>
												<td class='w-80'>Total de MBytes dos Documentos</td>
												<td class='text-right'>
													<fmt:formatNumber type = "number" pattern = "###,###,###,##0" value = "${totalBlobsDoc}" />
												</td>
											</tr>
											<tr>
												<td class='w-80'>Total de MBytes dos Anexos</td>
												<td class='text-right'>
													<fmt:formatNumber type = "number" pattern = "###,###,###,##0" value = "${totalBlobsAnexos}" />
												</td>
											</tr>
										</table>
									</div>
								</div>		
							</div>
						</div>
					</c:if>
					<c:if test="${listLinhas != null}">
						<div class="row mb-3">
							<div class="col">		
<!-- 								<button type="button" class="btn btn-outline-success" title="Exportar para CSV"	onclick="javascript:csv('listar', '/sigaex/app/expediente/rel/relArmazenamento/exportarCsv');"><i class="fa fa-file-csv"></i> Exportar</button>	 -->
								<button type="button" class="btn btn-outline-success" title="Exportar para CSV"	onclick="geraCsv('DocsProduzidos.csv')"><i class="fa fa-file-csv"></i> Exportar</button>	
							</div>
						</div>
						<div class="row">
							<table class="table table-hover table-striped">
								<thead class="thead-dark align-middle text-center">
									<tr>
										<th class="text-left">Sigla Órgão</th>
										<th class="text-left">Órgão</th>
										<th class="text-left">Sigla Unidade</th>
										<th class="text-left">Unidade</th>
										<th class="text-left">Nome do Documento</th>
										<th class="text-left">Núm.</th>
										<th class="text-center w-5">Data Documento</th>
										<th class="text-center w-5">Data TMP</th>
										<th class="text-center w-5">Cadastrante</th>
										<th class="text-right w-5">Págs. Doc.</th>
										<th class="text-right w-5">Tam.Doc.</th>
										<th class="text-right w-5">Tam.Doc. (no Oracle)</th>
										<th class="text-right w-5">Qtd. Anexos</th>
										<th class="text-right w-5">Págs. Anexos</th>
										<th class="text-right w-5">Tam.Anexos</th>
										<th class="text-right w-5">Tam.Anexos (no Oracle)</th>
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
										<td>
											${item[5]}
										</td>
										<td>
											${item[6]}
										</td>
										<td>
											${item[7]}
										</td>
										<td class="text-right">
											${item[8]}
										</td>
										<td class="text-right">
											${item[9]}
										</td>
										<td class="text-right">
											${item[10]}
										</td>
										<td class="text-right">
											${item[11]}
										</td>
										<td class="text-right">
											${item[12]}
										</td>
										<td class="text-right">
											${item[13]}
										</td>
										<td class="text-right">
											${item[14]}
										</td>
										<td class="text-right">
											${item[15]}
										</td>
									</tr>
								</c:forEach> 
								</tbody>
							</table>
						</div>
					</c:if>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>