<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests">
<link rel="stylesheet" href="/siga/javascript/select2/select2.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="/siga/javascript/select2/select2-bootstrap.css" type="text/css" media="screen, projection" />
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
	<link rel=stylesheet href="/siga/codemirror_merge/addon/merge/merge.css">
	<script src="/siga/codemirror_merge/addon/merge/dep/diff_match_patch.js"></script>
	<script src="/siga/codemirror_merge/addon/merge/merge.js"></script>


	<div class="container-fluid">
		<div class="card bg-light mb-3">

			<div class="card-header">
				<h5>Edição de Modelo</h5>
			</div>

			<div class="card-body">

				<form name="frm" id="frm" action="gravar" method="post">
					<input type="hidden" name="postback" value="1" /> <input
						type="hidden" name="id" value="${id}" id="modelo_gravar_id" />

					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label>Nome</label> <input type="text" name="nome"
									value="${nome}" size="80" class="form-control" />
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<label>Descrição</label> <input type="text" name="descricao"
									value="${descricao}" size="80" maxlength="256"
									class="form-control" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								<label>Classificação</label>
								<siga:selecao tema="simple" propriedade="classificacao"
									modulo="sigaex" urlAcao="buscar" urlSelecionar="selecionar" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								<label>Classificação para criação de vias</label>
								<siga:selecao tema="simple" modulo="sigaex"
									propriedade="classificacaoCriacaoVias" urlAcao="buscar"
									urlSelecionar="selecionar" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label>Espécie</label> <select name="forma" id="forma"
									class="custom-select  siga-select2" onchange="mudouForma(this.options[this.selectedIndex])">
									<c:forEach var="item" items="${listaForma}">
										<option value="${item.idFormaDoc}" data-isformatolivre="${item.isCapturadoFormatoLivre()}"
											${item.idFormaDoc == forma ? 'selected' : ''}>${item.descrFormaDoc}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<label>Nível de acesso</label> <select name="nivel"
									value="${nivel}" class="custom-select">
									<option value="0">[Indeterminado]</option>
									<c:forEach var="item" items="${listaNivelAcesso}">
										<option value="${item.idNivelAcesso}"
											${item.idNivelAcesso == nivel ? 'selected' : ''}>${item.nmNivelAcesso}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								<label for="extensoesArquivo">Extensões permitidas para o arquivo capturado de formato livre</label> <select
									class="form-control siga-select2" id="extensoesArquivo" name="extensoesArquivo[]"
									multiple="">
									<c:forEach items="${listaExtensoes}" var="item">
										<option value="${item}"
												${fn:contains(extensoesArquivo, item) ? 'selected' : ''}
										><div>${item}</div></option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label>Diretório</label> <input type="text" name="diretorio"
									value="${diretorio}" size="80" class="form-control" />
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label>Identificador para sincronismo</label> <input readonly
									type="text" name="uuid" value="${uuid}" size="80"
									class="form-control" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3">
							<div class="form-group">
								<label>Marca d'água no documento</label> <input type="text"
									name="marcaDagua" value="${marcaDagua}" maxlength="13"
									class="form-control" />
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-2">
							<div class="form-group">
								<label>Tipo do Modelo</label>
								<siga:escolha id="tipoModelo" var="tipoModelo"
									executarJavascript="false">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								<siga:opcao id="template/freemarker" texto="Freemarker">
									<textarea id="conteudo" style="width: 100%;" cols="1" rows="1"
										name="conteudo" class="form-control"><c:out
											value="${conteudo}" default="" /></textarea>
									<p align="right">Ctrl-I: Indentar, Crtl-S: Salvar</p>
								</siga:opcao>
								<siga:opcao id="template-file/jsp" texto="JSP">
									<div class="col-md-8  p-0">
										<label for="nomeArquivo">Nome do arquivo</label> <input
											type="text" id="nomeArquivo" class="form-control"
											name="arquivo" size="80" value="${arquivo}" />
									</div>
								</siga:opcao>
								</siga:escolha>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<p>
								<a class="btn btn-primary" data-toggle="collapse" href="#collapseComparacao" role="button" aria-expanded="false" aria-controls="collapseComparacao">
									Comparar Versões
								</a>
							</p>
							<div class="collapse show" id="collapseComparacao">
								<h2>Diferenças</h2>
								<div class="form-group">
									<label><b>Versões</b></label>
									<select id="versao" name="versao" class="custom-select">
										<option value="${id}">[Versão Atual]</option>
										<c:forEach var="item" items="${listaModeloHistorico}">
											<option value="${item[0]}">${item[1]}</option>
										</c:forEach>
									</select>
								</div>
								<div id=view></div>
							</div>
							<br>
							<script>

								$('#versao').on('change', function() {
									$.ajax({
										url:'/sigaex/app/modelo/buscar-json-conteudo-para-comparar/'+ this.value,
										type: "GET",
										success: function(data) {
											dv.rightOriginal().setValue(data.conteudoModelo);
										}
									});
								});

								var dv, hilight= true;
								function initUI(conteudo) {

									if (conteudo == null) return;
									var target = document.getElementById("view");
									target.innerHTML = "";
									dv = CodeMirror.MergeView(target, {
										value: conteudo,
										origLeft: null,
										orig: conteudo,
										lineNumbers: true,
										mode: "freemarker",
										highlightDifferences: hilight

									});
								}

								function toggleDifferences() {
									dv.setShowDifferences(hilight = !hilight);
								}

								window.onload = function() {
									initUI(document.getElementById("conteudo").value);
									$("#collapseComparacao").removeClass("show");
								};

							</script>
						</div>

					</div>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								<input type="submit" name="ok" value="Ok"
									class="btn btn-primary" /> <input type="submit" name="submit"
									value="Aplicar" class="btn btn-primary" /> <input
									type="button" value="Desativar" class="btn btn-primary"
									onclick="location.href='desativar?id=${id}'" /> <input
									type="button" value="Cancelar"
									onclick="javascript:history.back();" class="btn btn-primary" />
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
					<input type="button" value="Novo" class="btn btn-primary"
						onclick="location.href='/sigaex/app/configuracao/editar?id=&idMod=${id}&idTpConfiguracao=4&nmTipoRetorno=modelo&campoFixo=True'" />
				</div>
			</div>

			<div style="clear: both; margin-bottom: 20px;">
				<div id="tableCadastradasCriar"></div>
				<div>
					<input type="button" value="Novo" class="btn btn-primary"
						onclick="location.href='/sigaex/app/configuracao/editar?id=&idMod=${id}&idTpConfiguracao=2&nmTipoRetorno=modelo&campoFixo=True'" />
				</div>
			</div>

			<div style="clear: both; margin-bottom: 20px;">
				<div id="tableCadastradasCriarComoNovo"></div>
				<div>
					<input type="button" value="Novo" class="btn btn-primary"
						onclick="location.href='/sigaex/app/configuracao/editar?id=&idMod=${id}&idTpConfiguracao=42&nmTipoRetorno=modelo&campoFixo=True'" />
				</div>
			</div>

			<div style="clear: both; margin-bottom: 20px;">
				<div id="tableCadastradasDespachavel"></div>
				<div>
					<input type="button" value="Novo" class="btn btn-primary"
						onclick="location.href='/sigaex/app/configuracao/editar?id=&idMod=${id}&idTpConfiguracao=30&nmTipoRetorno=modelo&campoFixo=True'" />
				</div>
			</div>

			<div style="clear: both; margin-bottom: 20px;">
				<div id="tableCadastradasAssinar"></div>
				<div>
					<input type="button" value="Novo" class="btn btn-primary"
						onclick="location.href='/sigaex/app/configuracao/editar?id=&idMod=${id}&idTpConfiguracao=1&idTpMov=11&nmTipoRetorno=modelo&campoFixo=True'" />
				</div>
			</div>

			<div style="clear: both; margin-bottom: 20px;">
				<div id="tableCadastradasAssinarComSenha"></div>
				<div>
					<input type="button" value="Novo" class="btn btn-primary"
						onclick="location.href='/sigaex/app/configuracao/editar?id=&idMod=${id}&idTpConfiguracao=1&idTpMov=58&nmTipoRetorno=modelo&campoFixo=True'" />
				</div>
			</div>

			<div style="clear: both; margin-bottom: 20px;">
				<div id="tableCadastradasAcessar"></div>
				<div>
					<input type="button" value="Novo" class="btn btn-primary"
						onclick="location.href='/sigaex/app/configuracao/editar?id=&idMod=${id}&idTpConfiguracao=6&nmTipoRetorno=modelo'" />
				</div>
			</div>

			<div style="clear: both; margin-bottom: 20px;">
				<div id="tableCadastradasNivelAcessoMaximo"></div>
				<div>
					<input type="button" value="Novo" class="btn btn-primary"
						onclick="location.href='/sigaex/app/configuracao/editar?id=&idMod=${id}&idTpConfiguracao=18&nmTipoRetorno=modelo&campoFixo=True'" />
				</div>
			</div>

			<div style="clear: both; margin-bottom: 20px;">
				<div id="tableCadastradasNivelAcessoMinimo"></div>
				<div>
					<input type="button" value="Novo" class="btn btn-primary"
						onclick="location.href='/sigaex/app/configuracao/editar?id=&idMod=${id}&idTpConfiguracao=19&nmTipoRetorno=modelo&campoFixo=True'" />
				</div>
			</div>
		</c:if>
	</div>


	<script type="text/javascript"
		src="/siga/javascript/select2/select2.min.js"></script>
	<script type="text/javascript"
		src="/siga/javascript/select2/i18n/pt-BR.js"></script>
	<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>
	<script type="text/javascript">

		function getDocHeight(element) {

			return Math.max(
					element.scrollHeight, element.offsetHeight, element.clientHeight
			);
		}

		function KeyPress(e) {
			var evtobj = window.event? event : e

			if (evtobj.keyCode === 36 && evtobj.ctrlKey)
			{
				window.scrollTo(0, 600)
			}
			if (evtobj.keyCode === 35 && evtobj.ctrlKey)
			{
				window.scrollTo(0, getDocHeight(document.querySelectorAll('div[class="CodeMirror-code"]')[0].parentElement)+600);
			}
		}
		document.getElementById("template/freemarker").addEventListener('keydown', KeyPress)


		var editor = null;
		function sbmt() {
			frm.action='/modelo/editar';
			//frm.postback.value=1;
			frm.submit();
		}
		
		function muda_escolha(id) {
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
		
		function montaTableCadastradas(tabelaAlvo, idTpConfiguracao, idTpMov, idMod){	
			$('#' + tabelaAlvo).html('Carregando...');			
			$.ajax({				     				  
				  url:'/sigaex/app/configuracao/listar_cadastradas',
				  type: "GET",
				  data: { idTpConfiguracao : idTpConfiguracao, idTpMov : idTpMov, idMod : idMod, nmTipoRetorno : "modelo", campoFixo : "True"},					    					   					 
				  success: function(data) {
			    	$('#' + tabelaAlvo).html(data);				    
			 	 }
			});	
		}
	</script>

	<c:if test="${not empty id}">
		<script>
			montaTableCadastradas('tableCadastradasEletronico', 4, 0, ${id});
			montaTableCadastradas('tableCadastradasCriar', 2, 0, ${id});
			montaTableCadastradas('tableCadastradasCriarComoNovo', 42, 0, ${id});
			montaTableCadastradas('tableCadastradasDespachavel', 30, 0, ${id});
			montaTableCadastradas('tableCadastradasAssinar', 1, 11, ${id});
			montaTableCadastradas('tableCadastradasAssinarComSenha', 1, 58, ${id});		
			montaTableCadastradas('tableCadastradasAcessar', 6, 0, ${id});
			montaTableCadastradas('tableCadastradasNivelAcessoMaximo', 18, 0, ${id});
			montaTableCadastradas('tableCadastradasNivelAcessoMinimo', 19, 0, ${id});
		</script>
	</c:if>

	<script>

		function atualizaMergeView(id) {
			$.ajax({
				url:'/sigaex/app/modelo/buscar-json-conteudo-para-comparar/'+ id,
				type: "GET",
				success: function(data) {
					initUI(data.conteudoModelo);

				}
			});
		}

        // adiciona atualização no evento de collapse para que o quadro seja atualizado
		$('#collapseComparacao').on('shown.bs.collapse', function () {
			// Atualiza CodeMirror
			$('.CodeMirror').each(function(i, el){
				el.CodeMirror.refresh();
			});
		});

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
				                    complete: function(data) {
										var options, index, select, option, id;

										// Obtem DOM versao
										select = document.getElementById('versao');

										// Limpa select
										select.options.length = 0;

										// Obtem listaModeloHistorico
										options = JSON.parse(data.responseText);
										if(options !== null) {
											// Carrega nova listaModeloHistorico
					     					var listaModeloHistorico = options.listaModeloHistorico;
											for (index = 0; index < listaModeloHistorico.length; ++index) {
												option = listaModeloHistorico[index];
												select.options.add(new Option(option[1], option[0]));
											}
											id = listaModeloHistorico[0][0];
										}
										// atualiza mergeview
										atualizaMergeView(id);

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
				var selExtensoes = $("#extensoesArquivo").select2({
				    placeholder: "Escolha as extensões de arquivos possíveis para o arquivo capturado.",
				    tags: true
				});
			
				function mudouForma(elem) {
					if (elem.getAttribute("data-isformatolivre") === "true")
						$("#extensoesArquivo").prop("disabled", false);
					else
						$("#extensoesArquivo").prop("disabled", true);
				}
	</script>
</siga:pagina>