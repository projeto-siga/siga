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
        	var $msgCabecalho = $(".alert");
        	$msgCabecalho.on("close.bs.alert", function () {
        	      $msgCabecalho.hide();
        	      return false;
        	});
		    var frmRel = document.getElementById("frmRelatorios");
		    var fd = new FormData(frmRel);
			$.ajax({
		        url: "/sigaex/app/expediente/rel/exportCsv",
		        type: "POST",
		        dataType: "csv",
		        data: { orgaoPesqId: document.getElementById("orgaoPesqId").value,
			        lotacaoSel: document.getElementsByName("lotacaoSel.id")[0].value,
			        usuarioSel: document.getElementsByName("usuarioSel.id")[0].value,
			        dataInicial: document.getElementsByName("dataInicial")[0].value,
			        dataFinal: document.getElementsByName("dataFinal")[0].value,
		        },
		        beforeSend: function(){
		        	var buttonClose = $('.alert').find('button').clone();
		            $('.alert').text("Gerando arquivo CSV...").prepend(buttonClose);
		            $('.alert').removeClass("alert-success");
		            $('.alert').removeClass("alert-danger");
		            $('.alert').addClass("alert-warning");
		            $('.alert').closest(".row").removeClass("d-none");
		        },
		        complete: function(response, status, request){
		        	var buttonClose = $('.alert').find('button').clone();
			        cType = response.getResponseHeader('Content-Type');
			        if (cType && cType.indexOf('text/plain') !== -1) {
			            $('.alert').text(response.responseText).prepend(buttonClose);
			            $('.alert').removeClass("alert-success");
			            $('.alert').removeClass("alert-warning");
			            $('.alert').addClass("alert-danger");
				    } else {
					    var csv = [];
			            csv.push(response.responseText);
			            orgSel = document.getElementById("orgaoPesqId");
			            var opt;
			            for ( var i = 0, len = orgSel.options.length; i < len; i++ ) {
			                opt = orgSel.options[i];
			                if ( opt.selected === true ) {
			                    break;
			                }
			            }		            
					    downloadCSV(csv.join("\n"), "Documentos Por Orgao - "   
							    + document.getElementsByName("dataInicial")[0].value + " - " 
							    + document.getElementsByName("dataFinal")[0].value + " - " + opt.text + ".csv");
			            $('.alert').text("Download do arquivo CSV concluído.").prepend(buttonClose);
			            $('.alert').removeClass("alert-warning");
			            $('.alert').removeClass("alert-danger");
			            $('.alert').addClass("alert-success");
				    }
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
				<form name="frmRelatorios" id="frmRelatorios" action="/sigaex/app/expediente/rel/relArmazenamento" theme="simple" method="POST">
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
					</div>
					<div class="row">
						<div class="form-group col-md-4">
							<input type="button" value="Pesquisar" onclick="javascript:exportCsv();" class="btn btn-primary mt-auto" />
							<input type="button" value="Voltar" onclick="javascript:history.back();" class="btn btn-cancel ml-2 mt-auto" />
						</div>
					</div>
					<input type="hidden" name="nmOrgaoUsu" id="nmOrgaoUsu" value="${nmOrgaoUsu}">
				</form>
			</div>
		</div>
	</div>
</siga:pagina>