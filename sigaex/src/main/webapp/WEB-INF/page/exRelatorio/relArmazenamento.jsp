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

		function downloadCSV(csv, filename) {
			var link = window.document.createElement("a");
			link.setAttribute("href", "data:text/csv;charset=utf-8,%EF%BB%BF" + encodeURI(csv));
		    link.style.display = "none";
			link.setAttribute("download", filename);
			link.click();
		}	
		function exportCsv(){
		    var frmRel = document.getElementById("frmRelatorios");
		    var fd = new FormData(frmRel);
			$.ajax({
		        url: "/sigaex/app/expediente/rel/exportCsv",
		        type: "POST",
		        dataType: "csv",
		        data: fd,
		        cache: false,
		        processData: false,
		        contentType: false, 
		        success: function(result){
		        },
		        beforeSend: function(){
		            $('.alert').text("Gerando arquivo CSV...");
		            $('.alert').closest(".row").css({display:"block alert alert-warning"});
		        },
		        complete: function(data){
				    var csv = [];
		            csv.push(data.responseText);
				    downloadCSV(csv.join("\n"), "DocRelatorio.csv");
		        	
		            $('.alert').closest(".row").css({display:"none"});
		        },
		        timeout: 600000
		    });
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
				<form name="frmRelatorios" id="frmRelatorios" action="/sigaex/app/expediente/rel/exportCsv" theme="simple" method="POST">
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
									<input type="checkbox" name="getAll" ${getAll ? 'checked=\'checked\'' : ''}	 />
									<label class="form-check-label ml-1" for="getAll">Sem Dados de Compactação</label>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="form-group col-sm-6">
							<input type="text" name="emailDest" class="form-control" />
						</div>
					</div>
					<div class="row">
						<div class="form-group col-sm-4">
							<input type="button" value="Pesquisar" class="btn btn-primary mt-auto" onclick="javascript:exportCsv();"/>
							<input type="button" value="Voltar" onclick="javascript:history.back();" class="btn btn-cancel ml-2 mt-auto" />
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</siga:pagina>