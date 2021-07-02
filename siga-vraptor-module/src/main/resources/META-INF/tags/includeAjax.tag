<%@ tag body-content="scriptless"%>
<%@ attribute name="action" required="true"%>
<%@ attribute name="trigger" required="true"%>

<script language="javascript">
function carregar${action}(){
	jQuery.blockUI(objBlock);
	frm = document.getElementById('div${action}').parent;
	params = '';
	paramsObj = {};
	for (i = 0; i < frm.length; i++){
		params = params + '&' + frm[i].name + '=' + escape(frm[i].value);
		paramsObj[frm[i].name] = frm[i].value;
	}
	/* Siga.ajax(${linkto['+action+']}, paramsObj, "GET", function(response){		
		carregou${action}(response);
	}); */
	$.post( ${linkto['+action+']}, paramsObj, function(response){
		carregou${action}(response);
	});

	//PassAjaxResponseToFunction('${linkto['+action+']}', 'carregou${action}', null, false, params);
}

function carregou${action}(response, param){
	var div = document.getElementById('div${action}');
	div.innerHTML = response;
	jQuery.unblockUI();
}

document.getElementById('${trigger}').onchange = 'carregar${action}();' + 
	document.getElementById('${trigger}').onchange; 
	
}
</script>

<div id="div${action}"><jsp:include page="${action}.jsp"></jsp:include></div>