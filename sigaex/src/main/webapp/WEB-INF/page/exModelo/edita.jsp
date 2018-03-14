<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>

<link rel="stylesheet" href="/siga/codemirror/lib/codemirror.css" />
<link rel="stylesheet" href="/siga/codemirror/theme/default.css" />

<style type="text/css">
.CodeMirror {
	width: 100%;
	height: auto;
	border: 1px solid #eee;
}

.cm-freemarker-comment {
	color: limegreen;
	font-bold: yes;
}

.cm-freemarker-cmd {
	color: navy;
	font-bold: yes;
}

.cm-freemarker-mac {
	color: darkmagenta;
	font-bold: yes;
}

.cm-freemarker-exp {
	color: saddlebrown;
	font-bold: yes;
}
</style>

<siga:pagina titulo="Modelo">
	<script src="/siga/codemirror/lib/codemirror.js"></script>
	<script src="/siga/codemirror/lib/overlay.js"></script>
	<script src="/siga/codemirror/mode/xml/xml.js"></script>
	<script src="/siga/codemirror/mode/javascript/javascript.js"></script>
	<script src="/siga/codemirror/mode/css/css.js"></script>
	<script src="/siga/codemirror/mode/htmlmixed/htmlmixed.js"></script>
	<script type="text/javascript" src="/siga/javascript/jquery.blockUI.js"></script>

	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">

			<h2>Edição de Modelo</h2>

			<div class="gt-content-box gt-for-table">

				<form name="frm" id="frm" action="gravar" method="POST">
					<input type="hidden" name="postback" value="1" /> <input
						type="hidden" name="id" value="${id}" id="modelo_gravar_id" />

					<table class="gt-form-table">
						<tr class="header">
							<td colspan="2">Dados do Modelo</td>
						</tr>
						<tr>
							<td width="20%">Nome:</td>
							<td width="80%"><input type="text" name="nome"
								value="${nome}" size="80" /></td>
						</tr>
						<tr>
							<td>Descrição:</td>
							<td><input type="text" name="descricao" value="${descricao}"
								size="80" /></td>
						</tr>
						<tr>
							<td>Classificação:</td>
							<td><siga:selecao tema="simple" propriedade="classificacao"
									modulo="sigaex" urlAcao="buscar" urlSelecionar="selecionar" /></td>
						</tr>

						<tr>
							<td>Classificação para criação de vias:</td>
							<td><siga:selecao tema="simple" modulo="sigaex"
									propriedade="classificacaoCriacaoVias" urlAcao="buscar"
									urlSelecionar="selecionar" /></td>
						</tr>
						<tr>
							<td>Espécie:</td>
							<td><select name="forma" value="${forma}">
									<c:forEach var="item" items="${listaForma}">
										<option value="${item.idFormaDoc}"
											${item.idFormaDoc == forma ? 'selected' : ''}>${item.descrFormaDoc}</option>
									</c:forEach>
							</select></td>
						</tr>

						<tr>
							<td>Nivel de acesso:</td>
							<td><select name="nivel" value="${nivel}">
									<option value="0">[Indeterminado]</option>
									<c:forEach var="item" items="${listaNivelAcesso}">
										<option value="${item.idNivelAcesso}"
											${item.idNivelAcesso == nivel ? 'selected' : ''}>${item.nmNivelAcesso}</option>
									</c:forEach>
							</select></td>
						</tr>
						<tr>
							<td>Diretório:</td>
							<td><input type="text" name="diretorio" value="${diretorio}"
								size="80" /></td>
						</tr>
						<tr>
							<td>Identificador para sincronismo:</td>
							<td><input readonly type="text" name="uuid" value="${uuid}"
								size="80" /></td>
						</tr>
						<tr>
							<td>Tipo do Modelo:</td>
							<td><siga:escolha id="tipoModelo" var="tipoModelo">
									<siga:opcao id="template/freemarker" texto="Freemarker">
										<textarea id="conteudo" style="width: 100%;" cols="1" rows="1"
											name="conteudo"><c:if test="${not empty conteudo}">
														<c:out value="${conteudo}" default="" />
													</c:if></textarea>
													<p align="right">Ctrl-I: Indentar, Crtl-S: Salvar</p>
									</siga:opcao>
									<siga:opcao id="template-file/jsp" texto="JSP">
									&nbsp;&nbsp;&nbsp;&nbsp;Nome do arquivo:
									<input type="text" name="arquivo" size="80" value="${arquivo}" />
									</siga:opcao>
								</siga:escolha></td>
						</tr>
						<tr class="button">
							<td colspan="2"><input type="submit" value="Ok"
								class="gt-btn-medium gt-btn-left" /> <input type="submit"
								name="submit" value="Aplicar" class="gt-btn-medium gt-btn-left" />
								<input type="button" value="Desativar"
								class="gt-btn-medium gt-btn-left"
								onclick="location.href='desativar?id=${id}'" /> <input
								type="button" value="Cancela"
								onclick="javascript:history.back();"
								class="gt-btn-medium gt-btn-left" /></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		<c:if test="${not empty id}">
			<div style="clear: both; margin-bottom: 20px;">
				<div id="tableCadastradasEletronico"></div>
				<div>
					<a
						href="/sigaex/app/expediente/configuracao/editar?id=&idMod=${id}&idTpConfiguracao=4&nmTipoRetorno=modelo&campoFixo=True"
						style="margin-top: 10px;" class="gt-btn-medium">Novo</a>
				</div>
			</div>

			<div style="clear: both; margin-bottom: 20px;">
				<div id="tableCadastradasCriar"></div>
				<div>
					<a
						href="/sigaex/app/expediente/configuracao/editar?id=&idMod=${id}&idTpConfiguracao=2&nmTipoRetorno=modelo&campoFixo=True"
						style="margin-top: 10px;" class="gt-btn-medium">Novo</a>
				</div>
			</div>

			<div style="clear: both; margin-bottom: 20px;">
				<div id="tableCadastradasAssinar"></div>
				<div>
					<a
						href="/sigaex/app/expediente/configuracao/editar?id=&idMod=${id}&idTpConfiguracao=1&idTpMov=11&nmTipoRetorno=modelo&campoFixo=True"
						style="margin-top: 10px;" class="gt-btn-medium">Novo</a>
				</div>
			</div>

			<div style="clear: both; margin-bottom: 20px;">
				<div id="tableCadastradasAssinarComSenha"></div>
				<div>
					<a
						href="/sigaex/app/expediente/configuracao/editar?id=&idMod=${id}&idTpConfiguracao=1&idTpMov=58&nmTipoRetorno=modelo&campoFixo=True"
						style="margin-top: 10px;" class="gt-btn-medium">Novo</a>
				</div>
			</div>

			<div style="clear: both; margin-bottom: 20px;">
				<div id="tableCadastradasAcessar"></div>
				<div>
					<a
						href="/sigaex/app/expediente/configuracao/editar?id=&idMod=${id}&idTpConfiguracao=6&nmTipoRetorno=modelo"
						style="margin-top: 10px;" class="gt-btn-medium">Novo</a>
				</div>
			</div>

			<div style="clear: both; margin-bottom: 20px;">
				<div id="tableCadastradasNivelAcessoMaximo"></div>
				<div>
					<a
						href="/sigaex/app/expediente/configuracao/editar?id=&idMod=${id}&idTpConfiguracao=18&nmTipoRetorno=modelo&campoFixo=True"
						style="margin-top: 10px;" class="gt-btn-medium">Novo</a>
				</div>
			</div>

			<div style="clear: both; margin-bottom: 20px;">
				<div id="tableCadastradasNivelAcessoMinimo"></div>
				<div>
					<a
						href="/sigaex/app/expediente/configuracao/editar?id=&idMod=${id}&idTpConfiguracao=19&nmTipoRetorno=modelo&campoFixo=True"
						style="margin-top: 10px;" class="gt-btn-medium">Novo</a>
				</div>
			</div>
	</div>
	</c:if>

	<script> 
		var editor = null;
		function sbmt() {
			frm.action='/modelo/editar';
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
				editor = CodeMirror.fromTextArea(document.getElementById("conteudo"), {mode: "freemarker", tabMode: "indent", lineNumbers: true, firstLineNumber: 4, electricChars: false, smartIndent: false, viewportMargin: Infinity});
			}
		}
		
		CodeMirror.defineMode("freemarker", function(config, parserConfig) {
		    var freemarkerOverlay = {
		        token: function(stream, state) {
		            if (stream.match("[#--")) {
		                while ((ch = stream.next()) != null)
		                    if (stream.match("--]", true)) break;
		                return "freemarker-comment";
		            }
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
					  url:'/sigaex/app/expediente/configuracao/listar_cadastradas',
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

	<script>
			$(document).ready(function() {
			    $(window)
			        .keydown(
			            function(e) {
			                if ((String.fromCharCode(event.which).toLowerCase() == 's' && (event.ctrlKey || event.metaKey)) || (event.which == 19)) {
			                    event.preventDefault();
			                    editor.save();
			                    //		switch (e.keyCode) {
			                    //		case 116: // f5
			                    $.ajax({
			                        type: "POST",
			                        url: '/sigaex/app/modelo/gravar',
			                        data: $("#frm").serialize(), // serializes the form's elements.
			                        beforeSend: function() {
			                        	$.blockUI({message: '<span style="font-size: 200%">Gravando modelo...</span>'});
				                    },
				                    complete: function() {
				                    	$.unblockUI();
					                },
			                        error: function(data) {
			                            alert("Erro gravando as alterações!");
			                        }
			                    });

			                    return false; //"return false" will avoid further events
			                    return; //using "return" other attached events will execute
			                } else if ((String.fromCharCode(event.which).toLowerCase() == 'i' && (event.ctrlKey || event.metaKey))) {
			                    event.preventDefault();
			                    editor.save();
			                    $.ajax({
			                        type: "POST",
			                        url: '/siga/public/app/modelo/indentar',
			                        data: $("#frm").serialize(), // serializes the form's elements.
			                        beforeSend: function() {
			                        	$.blockUI({message: '<span style="font-size: 200%">Indentando modelo...</span>'});
				                    },
				                    complete: function() {
				                    	$.unblockUI();
					                },
			                        success: function(result){
			                            editor.setValue(result);
			                        },
			                        error: function(data) {
			                            alert("Erro indentando modelo!");
			                        }
			                    });

			                    return false; //"return false" will avoid further events
			                    return; //using "return" other attached events will execute
			                }
			                
			            });
			});
	</script>

</siga:pagina>