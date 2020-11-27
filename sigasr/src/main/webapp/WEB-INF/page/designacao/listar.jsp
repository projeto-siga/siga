<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/sigasrtags" prefix="sigasr"%>

<siga:pagina titulo="Designação de Solicitações">
	<jsp:include page="../main.jsp"></jsp:include>

	<script src="/sigasr/javascripts/jquery.dataTables.min.js"></script>
	<script src="/siga/javascript/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
	<script src="/sigasr/javascripts/jquery.serializejson.min.js"></script>
	<script src="/sigasr/javascripts/jquery.populate.js"></script>
	<script src="/sigasr/javascripts/base-service.js"></script>
	<script src="/sigasr/javascripts/detalhe-tabela.js"></script>
	<script src="/sigasr/javascripts/jquery.maskedinput.min.js"></script>
	<script src="/sigasr/javascripts/jquery.validate.min.js"></script>
	<script src="/sigasr/javascripts/language/messages_pt_BR.min.js"></script>
	<script src="/sigasr/javascripts/moment.js"></script>
	
	<div class="container-fluid mb-2">
		<h2>Designa&ccedil;&otilde;es</h2>

		<script type="text/javascript">
			var telaOrigem = 'listarDesignacao';
			
			//removendo a referencia de '$' para o jQuery
			$( document ).ready(function() {
				if ("${mostrarDesativados}" != "") {
					document.getElementById('checkmostrarDesativados').checked = ${mostrarDesativados};
					document.getElementById('checkmostrarDesativados').value = ${mostrarDesativados};
				}
				
				$("#checkmostrarDesativados").click(function() {
					jQuery.blockUI(objBlock);
					if (document.getElementById('checkmostrarDesativados').checked)
						location.href = '${linkTo[DesignacaoController].listar}' + '?mostrarDesativados=true';
					else
						location.href = '${linkTo[DesignacaoController].listar}' + '?mostrarDesativados=false';
				});
			});
		</script>
		
		<div class="card card-body">
			<sigasr:designacao modoExibicao="modoExibicao" designacoes="designacoes" mostrarDesativados="mostrarDesativados"/> 
		</div>
			
		<script>
			$(document).ready(function() {
				designacaoService.configurarDataTable()
			});
		</script>
	</div>
</siga:pagina>
