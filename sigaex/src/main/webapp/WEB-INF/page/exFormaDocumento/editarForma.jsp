<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>

<link rel="stylesheet" href="/siga/codemirror/lib/codemirror.css">
<script src="/siga/codemirror/lib/codemirror.js"></script> 
<script src="/siga/codemirror/lib/overlay.js"></script> 
<script src="/siga/codemirror/mode/xml/xml.js"></script> 
<script src="/siga/codemirror/mode/javascript/javascript.js"></script> 
<script src="/siga/codemirror/mode/css/css.js"></script> 
<link rel="stylesheet" href="/siga/codemirror/theme/default.css">
<script src="/siga/codemirror/mode/htmlmixed/htmlmixed.js"></script> 
<!--<link rel="stylesheet" href="/siga/codemirror/css/docs.css"> -->

<style type="text/css"> 
  .CodeMirror {
	border: 1px solid #eee;
  }
  .CodeMirror-scroll {
	height: auto;
	overflow-y: hidden;
	overflow-x: auto;
  }
  .activeline {background: #f7f7f7 !important;}
  .cm-freemarker-cmd {color: navy; font-bold: yes;}
  .cm-freemarker-mac {color: darkmagenta; font-bold: yes;}
  .cm-freemarker-exp {color: saddlebrown; font-bold: yes;}
</style> 

<siga:pagina titulo="Forma">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		
			<h2>Edição de Espécie Documental</h2>

			<div class="gt-content-box gt-for-table">
			
			<form name="frm" action="gravar"
				theme="simple" method="POST">
				<input type="hidden" name="postback" value="1" />
				<input type="hidden" name="id" value="${id}" id="forma_gravar_id" />
			
				<table class="gt-form-table">
					<tr class="header">
						<td colspan="2">Dados da Espécie Documental</td>
					</tr>
					<tr>
						<td width="20%">Descrição:</td>
						<td width="80%"><input type="text" value="${descricao}" name="descricao" size="80" /></td>
					</tr>
					<tr>
						<td>Sigla:</td>
						<td>
							<input type="text" value="${sigla}" name="sigla" size="3" id="gravar_sigla" />
							<span id="mensagem"></span>
						</td>
					</tr>
					<tr>
						<td>Tipo:</td>
						<td>
							<select name="idTipoFormaDoc" value="${idTipoFormaDoc}">
								<c:forEach var="tipo" items="${listaTiposFormaDoc}">
									<option value="${tipo.idTipoFormaDoc}" ${tipo.idTipoFormaDoc == idTipoFormaDoc ? 'selected' : ''}>${tipo.descTipoFormaDoc}</option>	
								</c:forEach>
							</select>
						</td>
					</tr>

					<tr>
						<td>Origem:</td>
						<td>
						   <input type="checkbox" name="origemExterno" value="true" ${origemExterno ? 'checked' : ''} /> Externo &nbsp;
						   <input type="checkbox" name="origemInternoProduzido" value="true" ${origemInternoProduzido ? 'checked' : ''} /> Interno Produzido &nbsp;
						   <input type="checkbox" name="origemInternoImportado" value="true" ${origemInternoImportado ? 'checked' : ''} /> Interno Importado 
						</td>
					</tr>

					<tr class="button">
						<td colspan="2"><input type="submit" value="Ok"  class="gt-btn-medium gt-btn-left"/> <input type="submit" name="submit" value="Aplicar" class="gt-btn-medium gt-btn-left"/> <input type="button"
							value="Cancela" onclick="javascript:history.back();" class="gt-btn-medium gt-btn-left"/></td>
					</tr>
				</table>
			</form>
		</div>
			</div>
        <c:if test="${not empty id}">
        		<div style="clear: both; margin-bottom: 20px;">		
				<div id="tableCadastradasEletronico"></div>	
				<div><a href="/sigaex/app/expediente/configuracao/editar?id=&idFormaDoc=${id}&idTpConfiguracao=4&nmTipoRetorno=forma&campoFixo=True" style="margin-top: 10px;" class="gt-btn-medium">Novo</a></div>	
				</div>
				
				<div style="clear: both; margin-bottom: 20px;">		
				<div id="tableCadastradasCriar"></div>	
				<div><a href="/sigaex/app/expediente/configuracao/editar?id=&idFormaDoc=${id}&idTpConfiguracao=2&nmTipoRetorno=forma&campoFixo=True" style="margin-top: 10px;" class="gt-btn-medium">Novo</a></div>	
				</div>
	
				<div style="clear: both; margin-bottom: 20px;">		
				<div id="tableCadastradasAssinar"></div>
				<div><a href="/sigaex/app/expediente/configuracao/editar?id=&idFormaDoc=${id}&idTpConfiguracao=1&idTpMov=11&nmTipoRetorno=forma&campoFixo=True" style="margin-top: 10px;" class="gt-btn-medium">Novo</a></div>		
				</div>
	
				<div style="clear: both; margin-bottom: 20px;">		
				<div id="tableCadastradasAcessar"></div>
				<div><a href="/sigaex/app/expediente/configuracao/editar?id=&idFormaDoc=${id}&idTpConfiguracao=6&nmTipoRetorno=forma&campoFixo=True" style="margin-top: 10px;" class="gt-btn-medium">Novo</a></div>		
				</div>
	
				<div style="clear: both; margin-bottom: 20px;">		
				<div id="tableCadastradasNivelAcessoMaximo"></div>	
				<div><a href="/sigaex/app/expediente/configuracao/editar?id=&idFormaDoc=${id}&idTpConfiguracao=18&nmTipoRetorno=forma&campoFixo=True" style="margin-top: 10px;" class="gt-btn-medium">Novo</a></div>	
				</div>
	
				<div style="clear: both; margin-bottom: 20px;">		
				<div id="tableCadastradasNivelAcessoMinimo"></div>	
				<div><a href="/sigaex/app/expediente/configuracao/editar?id=&idFormaDoc=${id}&idTpConfiguracao=19&nmTipoRetorno=forma&campoFixo=True" style="margin-top: 10px;" class="gt-btn-medium">Novo</a></div>	
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
