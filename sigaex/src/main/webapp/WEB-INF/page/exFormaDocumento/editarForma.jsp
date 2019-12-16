<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>

<siga:pagina titulo="Forma">
	<!-- main content -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Dados da Espécie Documental</h5>
			</div>
			<div class="card-body">
				<form name="frm" action="gravar" theme="simple" method="POST">

					<div class="row">
						<div class="col-sm-4">
							<div class="form-group">
								<label for="descricao">Descrição</label> <input type="text"
									value="${descricao}" name="descricao" class="form-control" />
							</div>
						</div>
						<div class="col-sm-1">
							<div class="form-group">
								<label for="sigla">Sigla</label> <input type="text"
									value="${sigla}" name="sigla" id="gravar_sigla"
									class="form-control" /> <span id="mensagem"></span>
							</div>
						</div>
						<div class="col-sm-2">
							<div class="form-group">
								<label for="idTipoFormaDoc">Tipo</label> <select
									name="idTipoFormaDoc" value="${idTipoFormaDoc}"
									class="form-control">
									<c:forEach var="tipo" items="${listaTiposFormaDoc}">
										<option value="${tipo.idTipoFormaDoc}"
											${tipo.idTipoFormaDoc == idTipoFormaDoc ? 'selected' : ''}>${tipo.descTipoFormaDoc}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label for="idTipoFormaDoc">Origem</label><br />
								<div class="form-check form-check-inline">
									<input class="form-check-input ml-2" type="checkbox"
										name="origemExterno" value="true"
										${origemExterno ? 'checked' : ''} /> Externo &nbsp; <input
										class="form-check-input ml-3" type="checkbox"
										name="origemInternoProduzido" value="true"
										${origemInternoProduzido ? 'checked' : ''} /> Interno
									Produzido &nbsp; <input class="form-check-input ml-3"
										type="checkbox" name="origemInternoImportado" value="true"
										${origemInternoImportado ? 'checked' : ''} /> Interno
									Importado <input class="form-check-input ml-3" type="checkbox"
										name="origemInternoCapturado" value="true"
										${origemInternoCapturado ? 'checked' : ''} /> Interno
									Capturado <input class="form-check-input ml-3" type="checkbox"
										name="origemExternoCapturado" value="true"
										${origemExternoCapturado ? 'checked' : ''} /> Externo
									Capturado
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
								<button type="submit" class="btn btn-primary">Ok</button>
								<button type="submit" name="submit" class="btn btn-primary">Aplicar</button>
								<button type="button" onclick="javascript:history.back();"
									class="btn btn-primary">Cancela</button>
							</div>
						</div>
					</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm">
				<div class="form-group">
					<c:if test="${not empty id}">
						<div style="clear: both; margin-bottom: 20px;">
							<div id="tableCadastradasEletronico"></div>
							<div>
								<a
									href="/sigaex/app/expediente/configuracao/editar?id=&idFormaDoc=${id}&idTpConfiguracao=4&nmTipoRetorno=forma&campoFixo=True"
									style="margin-top: 10px;" class="btn btn-primary">Novo</a>
							</div>
						</div>

						<div style="clear: both; margin-bottom: 20px;">
							<div id="tableCadastradasCriar"></div>
							<div>
								<a
									href="/sigaex/app/expediente/configuracao/editar?id=&idFormaDoc=${id}&idTpConfiguracao=2&nmTipoRetorno=forma&campoFixo=True"
									style="margin-top: 10px;" class="btn btn-primary">Novo</a>
							</div>
						</div>

						<div style="clear: both; margin-bottom: 20px;">
							<div id="tableCadastradasAssinar"></div>
							<div>
								<a
									href="/sigaex/app/expediente/configuracao/editar?id=&idFormaDoc=${id}&idTpConfiguracao=1&idTpMov=11&nmTipoRetorno=forma&campoFixo=True"
									style="margin-top: 10px;" class="btn btn-primary">Novo</a>
							</div>
						</div>

						<div style="clear: both; margin-bottom: 20px;">
							<div id="tableCadastradasAcessar"></div>
							<div>
								<a
									href="/sigaex/app/expediente/configuracao/editar?id=&idFormaDoc=${id}&idTpConfiguracao=6&nmTipoRetorno=forma&campoFixo=True"
									style="margin-top: 10px;" class="btn btn-primary">Novo</a>
							</div>
						</div>

						<div style="clear: both; margin-bottom: 20px;">
							<div id="tableCadastradasNivelAcessoMaximo"></div>
							<div>
								<a
									href="/sigaex/app/expediente/configuracao/editar?id=&idFormaDoc=${id}&idTpConfiguracao=18&nmTipoRetorno=forma&campoFixo=True"
									style="margin-top: 10px;" class="btn btn-primary">Novo</a>
							</div>
						</div>

						<div style="clear: both; margin-bottom: 20px;">
							<div id="tableCadastradasNivelAcessoMinimo"></div>
							<div>
								<a
									href="/sigaex/app/expediente/configuracao/editar?id=&idFormaDoc=${id}&idTpConfiguracao=19&nmTipoRetorno=forma&campoFixo=True"
									style="margin-top: 10px;" class="btn btn-primary">Novo</a>
							</div>
						</div>
					</c:if>
				</div>
			</div>
		</div>
		</form>
	</div>
	<script> 
$("#gravar_sigla").change(function () {
    var sigla = $("#gravar_sigla").val().toUpperCase();
	$("#gravar_sigla").attr("value", sigla);
	var characterReg = /^[A-Za-z]{3}$/;
	if(!characterReg.test(sigla)) {
		$('#mensagem').html('Sigla inválida. A sigla deve ser formada por 3 letras.').css('color','#FF0000');
		return;
    }	
	$.ajax({				     				  
		  url:'${pageContext.request.contextPath}/app/forma/verificar_sigla',
		  type: "GET",
		  data: {sigla : sigla, id : ${id} },
		  success: function(data) {
	    	$('#mensagem').html(data);				    
	 	 }
	});	
});

<c:if test="${not empty id}">
	function montaTableCadastradas(tabelaAlvo, idTpConfiguracao, idTpMov, idFormaDoc){	
		$('#' + tabelaAlvo).html('Carregando...');			
		$.ajax({				     				  
			  url:'/sigaex/app/expediente/configuracao/listar_cadastradas',
			  type: "GET",
			  data: { idTpConfiguracao : idTpConfiguracao, idTpMov : idTpMov, idFormaDoc : idFormaDoc, nmTipoRetorno : "forma", campoFixo : "True"},					    					   					 
			  success: function(data) {
		    	$('#' + tabelaAlvo).html(data);				    
		 	 }
		});	
	}
		
	montaTableCadastradas('tableCadastradasEletronico', 4, 0 ,${id});
	montaTableCadastradas('tableCadastradasCriar', 2, 0 ,${id});
	montaTableCadastradas('tableCadastradasAssinar', 1, 11 ,${id});	
	montaTableCadastradas('tableCadastradasAcessar', 6, 0 ,${id});
	montaTableCadastradas('tableCadastradasNivelAcessoMaximo', 18, 0 ,${id});
	montaTableCadastradas('tableCadastradasNivelAcessoMinimo', 19, 0 ,${id});			
</c:if>
</script>
</siga:pagina>
