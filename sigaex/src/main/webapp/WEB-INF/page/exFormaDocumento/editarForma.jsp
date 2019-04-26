<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>

<siga:pagina titulo="Forma">
	<div class="container-fluid">
		<div class="card bg-light mb-3" >
		
			<div class="card-header"><h5>Edição de Espécie Documental</h5></div>

			<div class="card-body">
			
			<form name="frm" action="gravar" theme="simple" method="POST">
				<input type="hidden" name="postback" value="1" />
				<input type="hidden" name="id" value="${id}" id="forma_gravar_id" />
			
				<div class="row">
					<div class="col-sm-4">
						<div class="form-group">
							<label>Descrição</label>
							<input type="text" value="${descricao}" name="descricao" size="80" class="form-control"/>
						</div>
					</div>
					<div class="col-sm-4">
						<div class="form-group">		
							<label>Sigla</label>
							<input type="text" value="${sigla}" name="sigla" size="3" id="gravar_sigla" class="form-control"/>
							<span id="mensagem"></span>
						</div>
					</div>
					<div class="col-sm-4">
						<div class="form-group">
							<label>Tipo</label>
							<select name="idTipoFormaDoc" value="${idTipoFormaDoc}" class="form-control">
								<c:forEach var="tipo" items="${listaTiposFormaDoc}">
									<option value="${tipo.idTipoFormaDoc}" ${tipo.idTipoFormaDoc == idTipoFormaDoc ? 'selected' : ''}>${tipo.descTipoFormaDoc}</option>	
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-1">
						<label>Origem</label>
					</div>
					<div class="col-sm-10">
						<div class="form-group">
							
							<div class="form-check form-check-inline">
							  <input class="form-check-input" type="checkbox" id="inlineCheckbox1" name="origemExterno" value="true" ${origemExterno ? 'checked' : ''}>
							  <label class="form-check-label" for="inlineCheckbox1">Externo</label>
							</div>
							<div class="form-check form-check-inline">
							  <input class="form-check-input" type="checkbox" id="inlineCheckbox2" name="origemInternoProduzido" value="true" ${origemInternoProduzido ? 'checked' : ''}>
							  <label class="form-check-label" for="inlineCheckbox2">Interno Produzido</label>
							</div>
							<div class="form-check form-check-inline">
							  <input class="form-check-input" type="checkbox" id="inlineCheckbox3" name="origemInternoImportado" value="true" ${origemInternoImportado ? 'checked' : ''}>
							  <label class="form-check-label" for="inlineCheckbox3">Interno Importado</label>
							</div>
							<div class="form-check form-check-inline">
							  <input class="form-check-input" type="checkbox" id="inlineCheckbox4" name="origemInternoCapturado" value="true" ${origemInternoCapturado ? 'checked' : ''}>
							  <label class="form-check-label" for="inlineCheckbox3">Interno Capturado</label>
							</div>
							<div class="form-check form-check-inline">
							  <input class="form-check-input" type="checkbox" id="inlineCheckbox5" name="origemExternoCapturado" value="true" ${origemExternoCapturado ? 'checked' : ''}>
							  <label class="form-check-label" for="inlineCheckbox3">Externo Capturado</label>
							</div>
							
							
						</div>
					</div>
				</div>
				
				<div class="row">
					<div class="col-sm-12">
						<div class="form-group">
							<input type="submit" value="Ok"  class="btn btn-primary"/>
							<input type="submit" name="submit" value="Aplicar" class="btn btn-primary"/>
							<input type="button"value="Cancelar" onclick="javascript:history.back();" class="btn btn-primary"/>
						</div>
					</div>
				</div>
			</form>
		</div>
		</div>
        <c:if test="${not empty id}">
        		<div style="clear: both; margin-bottom: 20px;">		
				<div id="tableCadastradasEletronico"></div>	
				<div>
					<input type="button" value="Novo" class="btn btn-primary" onclick="location.href='/sigaex/app/expediente/configuracao/editar?id=&idFormaDoc=${id}&idTpConfiguracao=4&nmTipoRetorno=forma&campoFixo=True'" />
				</div>
				</div>
				
				<div style="clear: both; margin-bottom: 20px;">		
				<div id="tableCadastradasCriar"></div>	
				<div>
					<input type="button" value="Novo" class="btn btn-primary" onclick="location.href='/sigaex/app/expediente/configuracao/editar?id=&idFormaDoc=${id}&idTpConfiguracao=2&nmTipoRetorno=forma&campoFixo=True'" />
				</div>	
				</div>
	
				<div style="clear: both; margin-bottom: 20px;">		
				<div id="tableCadastradasAssinar"></div>
				<div>
					<input type="button" value="Novo" class="btn btn-primary" onclick="location.href='/sigaex/app/expediente/configuracao/editar?id=&idFormaDoc=${id}&idTpConfiguracao=1&idTpMov=11&nmTipoRetorno=forma&campoFixo=True'" />
				</div>		
				</div>
	
				<div style="clear: both; margin-bottom: 20px;">		
				<div id="tableCadastradasAcessar"></div>
				<div>
					<input type="button" value="Novo" class="btn btn-primary" onclick="location.href='/sigaex/app/expediente/configuracao/editar?id=&idFormaDoc=${id}&idTpConfiguracao=6&nmTipoRetorno=forma&campoFixo=True'" />
				</div>		
				</div>
	
				<div style="clear: both; margin-bottom: 20px;">		
				<div id="tableCadastradasNivelAcessoMaximo"></div>	
				<div>
					<input type="button" value="Novo" class="btn btn-primary" onclick="location.href='/sigaex/app/expediente/configuracao/editar?id=&idFormaDoc=${id}&idTpConfiguracao=18&nmTipoRetorno=forma&campoFixo=True'" />
				</div>	
				</div>
	
				<div style="clear: both; margin-bottom: 20px;">		
				<div id="tableCadastradasNivelAcessoMinimo"></div>	
				<div>
					<input type="button" value="Novo" class="btn btn-primary" onclick="location.href='/sigaex/app/expediente/configuracao/editar?id=&idFormaDoc=${id}&idTpConfiguracao=19&nmTipoRetorno=forma&campoFixo=True'" />
				</div>	
				</div>
			</div>
		</c:if>
		
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
