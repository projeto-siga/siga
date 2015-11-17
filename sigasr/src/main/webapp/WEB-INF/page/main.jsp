<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<!-- DataTables CSS -->
<link rel="stylesheet" type="text/css" href="/sigasr/stylesheets/main.css">
<link rel="stylesheet" type="text/css" href="/sigasr/stylesheets/jquery.dataTables.min.css">

<style>
	.ui-widget-header {
		border: 1px solid #365b6d;
		background: #365b6d;
	}
</style>

<script>
	
	function queryDesativarReativar(id0, mostrarDesativados0) {
		return jQuery.param({
			id : id0,
			mostrarDesativados : mostrarDesativados0
		});
	}

	var requesting=false;
	
	function block(){
		if (requesting)
			return false;
		
		requesting = true;
		return true;
	}
	function unblock(){
		requesting = false;
	}

	function sbmt(id) {
		jQuery.blockUI(objBlock);
		if (typeof postbackURL == 'function')
			Siga.ajax(postbackURL(), null, "GET", function(response){		
	    		sbmtResponse(response, id);
			});	
	}
	
	function sbmtResponse(response, id) {
		$("div[depende*='"+id+"']").each(function() {
			var responseHtml = $(response).find("#"+this.id).html();
			$(this).html(responseHtml);
			$(responseHtml).find("script").each(function(){
				eval($(this).text());
			});
		});	
		$("div[depende*='"+id+"']").show();
		jQuery.unblockUI();
	}
</script>

<script src="/sigasr/javascripts/jquery-1.11.1.min.js"></script>
<script src="/sigasr/javascripts/jquery-migrate-1.0.0.js"></script>
<script src="/siga/javascript/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
<script src="/sigasr/javascripts/jquery.blockUI.js"></script>
<script src="/sigasr/javascripts/jquery-config.js"></script>

<sigasr:modal nome="server_error" titulo="Erro interno no servidor">
	<div id="erroInterno">
		<div id="responseText"></div>
	</div>
</sigasr:modal>