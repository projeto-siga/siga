<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

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
</script>

<!-- DataTables CSS -->
<link rel="stylesheet" type="text/css" href="../../../stylesheets/main.css">
<link rel="stylesheet" type="text/css" href="//cdn.datatables.net/1.10.2/css/jquery.dataTables.css">


<script>
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
</script>

<script src="/siga/javascript/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
<script src="../../../javascripts/jquery.blockUI.js"></script>
<script src="../../../javascripts/jquery-config.js"></script>

<siga:modal nome="server_error" titulo="Erro interno no servidor">
	<div id="erroInterno">
		Ocorreu um erro interno no sistema. Entre em contato com o administrador.
		</br>
		Descri&ccedil;&atilde;o do Erro: <span id="codigoErro"></span> - <span id="responseStatus"></span> - <span id="responseText"></span>
	</div>
</siga:modal>