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
					<input type="hidden" id="id" name="id" value="${id}" />
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label for="descricao">Descrição</label> 
								<input type="text" id="descricao" value="${descricao}" name="descricao" class="form-control" />
							</div>							
						</div>
						<div class="col-lg-1 col-md-2">
							<div class="form-group">
								<label for="sigla">Sigla</label> 
								<input type="text" id="sigla" value="${sigla}" name="sigla" id="gravar_sigla" class="form-control"/> <span id="mensagem"></span>
							</div>
						</div>
						<div class="col-md-3">
							<div class="form-group">
								<label for="tipoFormaDoc">Tipo</label> 
								<select id="tipoFormaDoc" name="idTipoFormaDoc" value="${idTipoFormaDoc}"
									class="form-control">
									<c:forEach var="tipo" items="${listaTiposFormaDoc}">
										<option value="${tipo.idTipoFormaDoc}"
											${tipo.idTipoFormaDoc == idTipoFormaDoc ? 'selected' : ''}>${tipo.descTipoFormaDoc}</option>
									</c:forEach>
								</select>
							</div>
						</div>						
						<div class="col-md-2">
							<div class="form-group">
								<div class="form-check form-check-inline mt-4">
								  <input class="form-check-input" type="checkbox" name="isComposto" id="isComposto" value="1" <c:if test="${isComposto == 1}">checked</c:if>/>
								  <label class="form-check-label" for="isComposto">Documento Composto</label>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<h3 style="font-size: 1rem; margin-bottom: 3px; font-weight: normal;">Origem</h3>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">								
								<div class="form-check  form-check-inline">
									<input type="checkbox" id="origemExterno" class="form-check-input" name="origemExterno" value="true" ${origemExterno ? 'checked' : ''}/> 
									<label for="origemExterno">Externo</label>
								</div>								
								<div class="form-check  form-check-inline">	
									<input type="checkbox" id="origemInternoProduzido" class="form-check-input" name="origemInternoProduzido" value="true" ${origemInternoProduzido ? 'checked' : ''}/>
									<label for="origemInternoProduzido">Interno Produzido</label>
								</div>
								<div class="form-check  form-check-inline">	
									<input type="checkbox" id="origemInternoImportado" class="form-check-input" name="origemInternoImportado" value="true" ${origemInternoImportado ? 'checked' : ''}/>
									<label for="origemInternoImportado">Interno Importado</label>
								</div>
								<div class="form-check  form-check-inline">	
									<input type="checkbox" id="origemInternoCapturado" class="form-check-input" name="origemInternoCapturado" value="true" ${origemInternoCapturado ? 'checked' : ''}/> 
									<label for="origemInternoCapturado">Interno Capturado</label>
								</div>
								<div class="form-check  form-check-inline">	 
									<input type="checkbox" id="origemExternoCapturado" class="form-check-input" name="origemExternoCapturado" value="true" ${origemExternoCapturado ? 'checked' : ''} /> 
									<label for="origemExternoCapturado">Externo Capturado</label>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-8">
							<div class="form-group">
								<button type="submit" class="btn btn-primary">Ok</button>
								<button type="submit" name="submit" class="btn btn-primary">Aplicar</button>								
								<a href="${linkTo[ExFormaDocumentoController].listarFormas()}?ordenar=descricao" class="btn btn-cancel">Cancela</a>
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
