<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib prefix="ww" uri="/webwork"%>

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

<siga:pagina titulo="Modelo">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
		
			<h2>Edição de Modelo</h2>

			<div class="gt-content-box gt-for-table">
			
			<ww:form name="frm" action="gravar" namespace="/modelo"
				theme="simple" method="POST">
				<ww:hidden name="postback" value="1" />
				<ww:hidden name="id" value="${id}" id="modelo_gravar_id" />
				<!--
 				<ww:hidden name="mobilPaiSel.sigla" value=""
					id="transferir_gravar_pai" />
-->
				<table class="gt-form-table">
					<tr class="header">
						<td colspan="2">Dados do Modelo</td>
					</tr>
					<tr>
						<td width="20%">Nome:</td>
						<td width="80%"><ww:textfield name="nome" size="80" /></td>
					</tr>
					<tr>
						<td>Descrição:</td>
						<td><ww:textfield name="descricao" size="80" /></td>
					</tr>
					<tr>
						<td>Classificação:</td>
						<td><siga:selecao tema="simple" propriedade="classificacao" modulo="sigaex"/></td>
					</tr>

					<tr>
						<td>Classificação para criação de vias:</td>
						<td><siga:selecao tema="simple" modulo="sigaex"
							propriedade="classificacaoCriacaoVias" /></td>
					</tr>
					<tr>
						<td>Forma:</td>
						<td><ww:select name="forma" list="listaForma"
							listKey="idFormaDoc" listValue="descrFormaDoc" /></td>
					</tr>

					<tr>
						<td>Nivel de acesso:</td>
						<td><ww:select name="nivel" list="listaNivelAcesso"
							listKey="idNivelAcesso" listValue="nmNivelAcesso" /></td>
					</tr>

					<tr>
						<td>Tipo do Modelo:</td>
						<td>
							<siga:escolha id="tipoModelo" var="tipoModelo">
								<siga:opcao id="template/freemarker" texto="Freemarker">
									<textarea id="conteudo" style="width: 100%;" cols="1" rows="1" name="conteudo"><c:if test="${not empty conteudo}"><ww:property  value="conteudo" escape="true" default=""/></c:if></textarea>
								</siga:opcao>
								<siga:opcao id="template-file/jsp" texto="JSP">
									&nbsp;&nbsp;&nbsp;&nbsp;Nome do arquivo:
									<ww:textfield name="arquivo" size="80" />
								</siga:opcao>
							</siga:escolha>
						</td>
					</tr>
					<tr class="button">
						<td colspan="2"><input type="submit" value="Ok"  class="gt-btn-medium gt-btn-left"/> <input type="submit" name="submit" value="Aplicar"  class="gt-btn-medium gt-btn-left"/>
						<ww:url id="urlDesativar" action="desativar" namespace="/modelo" /> 
						<input type="button" value="Desativar"
							class="gt-btn-medium gt-btn-left"
							onclick="location.href='<ww:property value="%{urlDesativar}"/>?id=${id}'" /> 
						<input type="button"
							value="Cancela" onclick="javascript:history.back();" class="gt-btn-medium gt-btn-left"/></td>
					</tr>
				</table>
			</ww:form>
		</div>
			</div>
		<c:if test="${not empty id}">			
				<div style="clear: both; margin-bottom: 20px;">		
				<div id="tableCadastradasEletronico"></div>	
				<div><a href="/sigaex/expediente/configuracao/editar.action?idMod=${id}&idTpConfiguracao=4&nmTipoRetorno=modelo&campoFixo=True" style="margin-top: 10px;" class="gt-btn-medium">Novo</a></div>	
				</div>
				
				<div style="clear: both; margin-bottom: 20px;">		
				<div id="tableCadastradasCriar"></div>	
				<div><a href="/sigaex/expediente/configuracao/editar.action?idMod=${id}&idTpConfiguracao=2&nmTipoRetorno=modelo&campoFixo=True" style="margin-top: 10px;" class="gt-btn-medium">Novo</a></div>	
				</div>
	
				<div style="clear: both; margin-bottom: 20px;">		
				<div id="tableCadastradasAssinar"></div>
				<div><a href="/sigaex/expediente/configuracao/editar.action?idMod=${id}&idTpConfiguracao=1&idTpMov=11&nmTipoRetorno=modelo&campoFixo=True" style="margin-top: 10px;" class="gt-btn-medium">Novo</a></div>		
				</div>
	
				<div style="clear: both; margin-bottom: 20px;">		
				<div id="tableCadastradasAssinarComSenha"></div>
				<div><a href="/sigaex/expediente/configuracao/editar.action?idMod=${id}&idTpConfiguracao=1&idTpMov=58&nmTipoRetorno=modelo&campoFixo=True" style="margin-top: 10px;" class="gt-btn-medium">Novo</a></div>		
				</div>

				<div style="clear: both; margin-bottom: 20px;">		
				<div id="tableCadastradasAcessar"></div>
				<div><a href="/sigaex/expediente/configuracao/editar.action?idMod=${id}&idTpConfiguracao=6&nmTipoRetorno=modelo" style="margin-top: 10px;" class="gt-btn-medium">Novo</a></div>		
				</div>
	
				<div style="clear: both; margin-bottom: 20px;">		
				<div id="tableCadastradasNivelAcessoMaximo"></div>	
				<div><a href="/sigaex/expediente/configuracao/editar.action?idMod=${id}&idTpConfiguracao=18&nmTipoRetorno=modelo&campoFixo=True" style="margin-top: 10px;" class="gt-btn-medium">Novo</a></div>	
				</div>
	
				<div style="clear: both; margin-bottom: 20px;">		
				<div id="tableCadastradasNivelAcessoMinimo"></div>	
				<div><a href="/sigaex/expediente/configuracao/editar.action?idMod=${id}&idTpConfiguracao=19&nmTipoRetorno=modelo&campoFixo=True" style="margin-top: 10px;" class="gt-btn-medium">Novo</a></div>	
				</div>
			</div>
		</c:if>
	
	
	<ww:url id="urlEditar" action="editar" namespace="/modelo" /> 
	<script> 
		var editor = null;
		function sbmt() {
			frm.action='<ww:property value="%{urlEditar}"/>';
			//frm.postback.value=1;
			frm.submit();
		}
		
		function muda_escolha(id) 
		{
			document.getElementById("template-file/jsp").style.display = "none";
			document.getElementById("template/freemarker").style.display = "none";
			var span = document.getElementById(id.value);
			span.style.display="";
			if (editor == null && id.value == "template/freemarker") {
				editor = CodeMirror.fromTextArea(document.getElementById("conteudo"), {mode: "freemarker", tabMode: "indent", lineNumbers: true, firstLineNumber: 4,
					onCursorActivity: function() {
						editor.setLineClass(hlLine, null);
						hlLine = editor.setLineClass(editor.getCursor().line, "activeline");
					}});
				var hlLine = editor.setLineClass(0, "activeline");
			}
		}
		
		CodeMirror.defineMode("freemarker", function(config, parserConfig) {
		  var freemarkerOverlay = {
			token: function(stream, state) {
			  if (stream.match("[#") || stream.match("[/#")) {
				while ((ch = stream.next()) != null)
				  if (ch == "]") break;
				return "freemarker-cmd";
			  }
			  if (stream.match("[@") || stream.match("[/@")) {
				while ((ch = stream.next()) != null)
				  if (ch == "]") break;
				return "freemarker-mac";
			  }
			  if (stream.match("\${")) {
				while ((ch = stream.next()) != null)
				  if (ch == "}") break;
				return "freemarker-exp";
			  }
			  while (stream.next() != null && !(stream.match("[", false) || stream.match("\${", false))) {}
			  return null;
			}
		  };
		  return CodeMirror.overlayParser(CodeMirror.getMode(config, parserConfig.backdrop || "text/html"), freemarkerOverlay);
		});
		
		muda_escolha(document.getElementById("tipoModelo"));
		<c:if test="${not empty id}">
			function montaTableCadastradas(tabelaAlvo, idTpConfiguracao, idTpMov, idMod){	
				$('#' + tabelaAlvo).html('Carregando...');			
				$.ajax({				     				  
					  url:'/sigaex/expediente/configuracao/listar_cadastradas.action',
					  type: "GET",
					  data: { idTpConfiguracao : idTpConfiguracao, idTpMov : idTpMov, idMod : idMod, nmTipoRetorno : "modelo", campoFixo : "True"},					    					   					 
					  success: function(data) {
				    	$('#' + tabelaAlvo).html(data);				    
				 	 }
				});	
			}
				
			montaTableCadastradas('tableCadastradasEletronico', 4, 0 ,${id});
			montaTableCadastradas('tableCadastradasCriar', 2, 0 ,${id});
			montaTableCadastradas('tableCadastradasAssinar', 1, 11 ,${id});
			montaTableCadastradas('tableCadastradasAssinarComSenha', 1, 58 ,${id});		
			montaTableCadastradas('tableCadastradasAcessar', 6, 0 ,${id});
			montaTableCadastradas('tableCadastradasNivelAcessoMaximo', 18, 0 ,${id});
			montaTableCadastradas('tableCadastradasNivelAcessoMinimo', 19, 0 ,${id});
		</c:if>
	</script>
	
</siga:pagina>
