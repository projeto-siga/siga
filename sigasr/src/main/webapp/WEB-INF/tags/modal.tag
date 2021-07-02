<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="nome" required="false"%>
<%@ attribute name="url" required="false"%>
<%@ attribute name="titulo" required="false"%>
<%@ attribute name="altura" required="false"%>
<%@ attribute name="largura" required="false"%>

<script language="javascript">

function ${nome}(){
	var url = '${url}';
	
	if(url != ""){
		/* Siga.ajax(url, null, "GET", function(response){		
			carregouAjax${nome}(response);
		}); */
		$.get( url, function(response){
			carregouAjax${nome}(response);
		});
	} else{ 
		${nome}_dialog.dialog("open");
	}
}

function ${nome}_fechar(){
	${nome}_dialog.dialog( "close" );
}

function carregouAjax${nome}(response, param){
	if (param !== undefined) 
		param.html(response);
	else
		${nome}_dialog.html(response);
	${nome}_dialog.dialog( "open" );
}

</script>

<div id="${nome}_dialog" title="${titulo}">
	<jsp:doBody/>
</div>

<script>
var altura = "${altura}" != "" ? "${altura}" : "auto";
var largura = "${largura}" != "" ? "${largura}" : "auto";
var ${nome}_dialog = $("#${nome}_dialog").dialog({
    autoOpen: false,
    height: altura,
    width: largura,
    modal: true,
    resizable: false
  });
</script>