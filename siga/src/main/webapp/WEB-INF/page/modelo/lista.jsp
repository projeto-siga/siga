<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%-- pageContext.setAttribute("sysdate", new java.util.Date()); --%>

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



<siga:pagina titulo="Lista de Modelos">
	<script src="/siga/codemirror/lib/codemirror.js"></script>
	<script src="/siga/codemirror/lib/overlay.js"></script>
	<script src="/siga/codemirror/mode/xml/xml.js"></script>
	<script src="/siga/codemirror/mode/javascript/javascript.js"></script>
	<script src="/siga/codemirror/mode/css/css.js"></script>
	<script src="/siga/codemirror/mode/htmlmixed/htmlmixed.js"></script>
	<script type="text/javascript" src="/siga/javascript/jquery.blockUI.js"></script>

	<script>
function sbmt(id, action) {
	var frm = document.getElementById(id);
	frm.action=action;
	frm.submit();
	return;
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

</script>
	<div class="container-fluid">
		
			<c:set var="i" value="${0}" />
			<c:forEach var="modelo" items="${itens}">
				<c:set var="i" value="${i+1}" />
				<div class="card bg-light mb-3" >
				<div class="card-header"><h5>
					<c:if test="${not empty modelo.cpOrgaoUsuario}">Orgão Usuário: ${modelo.cpOrgaoUsuario.descricao}</c:if>
					<c:if test="${empty modelo.cpOrgaoUsuario}">Edição de Modelos: GERAL</c:if>
				</h5></div>
				<div class="card-body">
					<form id="editar_${i}" name="editar_${i}"
						action="/siga/modelo/gravar" method="post">
						<div class="row mb-2">
							<div class="col">
								<input type="hidden" name="id" value="${modelo.id}" /> <input
									type="hidden" name="idOrgUsu"
									value="${modelo.cpOrgaoUsuario.id}" />
	
								<textarea id="conteudo${i}" style="width: 100%;" cols="1"
									rows="1" name="conteudo" class="form-control"><c:out value="${modelo.conteudoBlobString}" escapeXml="true"
											default="" /></textarea>
								<c:if test="${empty modelo.cpOrgaoUsuario}">
									<p align="right">Ctrl-I: Indentar, Crtl-S: Salvar</p>
								</c:if>
								<script>
									var editor${i} = CodeMirror.fromTextArea(document.getElementById("conteudo${i}"), {mode: "freemarker", tabMode: "indent", lineNumbers: true, electricChars: false, smartIndent: false, viewportMargin: Infinity});
								</script>
							</div>
						</div>
						<div class="row">
							<div class="col">
								<input name="salvar_conteudo" type="submit" id="but_gravar${i}"
									value="Salvar"
									onclick="javascript: this.form.action='/siga/app/modelo/gravar'; "
									class="btn btn-primary" />
							</div>
						</div>
					</form>
				</div>
				</div>
			</c:forEach>

		
	</div>

	<script>
		$(document).ready(function () {
			$(window).keydown(function(e) {
				if ((String.fromCharCode(event.which).toLowerCase() == 's' && (event.ctrlKey || event.metaKey)) || (event.which == 19)) {
					event.preventDefault();
					editor1.save();
					//		switch (e.keyCode) {
					//		case 116: // f5
					$.ajax({
						type : "POST",
						url : '/siga/app/modelo/gravar',
						data : $("#editar_1").serialize(), // serializes the form's elements.
                        beforeSend: function() {
                        	$.blockUI({message: '<span style="font-size: 200%">Gravando modelo geral...</span>'});
	                    },
	                    complete: function() {
	                    	$.unblockUI();
		                },
						error : function(data) {
							alert("Erro gravando as alterações!");
						}
					});
					return false; //"return false" will avoid further events
				} else if ((String.fromCharCode(event.which).toLowerCase() == 'i' && (event.ctrlKey || event.metaKey))) {
                    event.preventDefault();
                    editor1.save();
                    $.ajax({
                        type: "POST",
                        url: '/siga/public/app/modelo/indentar',
                        data: $("#editar_1").serialize(), // serializes the form's elements.
                        beforeSend: function() {
                        	$.blockUI({message: '<span style="font-size: 200%">Indentando modelo geral...</span>'});
	                    },
	                    complete: function() {
	                    	$.unblockUI();
		                },
                        success: function(result){
                        	editor1.setValue(result);
                        },
                        error: function(data) {
                            alert("Erro indentando modelo!");
                        }
                    });
					return false; //"return false" will avoid further events
				}
			});
		});
	</script>
</siga:pagina>