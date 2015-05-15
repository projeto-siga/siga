<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Seguran&ccedil;a">
	<jsp:include page="../main.jsp"></jsp:include>

	<script src="//cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
	<script src="/siga/javascript/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
	<script src="/sigasr/javascripts/jquery.serializejson.min.js"></script>
	<script src="/sigasr/javascripts/jquery.populate.js"></script>
	<script src="/sigasr/javascripts/base-service.js"></script>
	<script src="/sigasr/javascripts/detalhe-tabela.js"></script>
	<script src="/sigasr/javascripts/jquery.maskedinput.min.js"></script>
	<script src="/sigasr/javascripts/jquery.validate.min.js"></script>
	<script src="/sigasr/javascripts/language/messages_pt_BR.min.js"></script>
	<script src="/sigasr/javascripts/moment.js"></script>
	
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Designa&ccedil;&otilde;es</h2>

			<script type="text/javascript">
				var telaOrigem = 'listarDesignacao';
				
				//removendo a referencia de '$' para o jQuery
				$( document ).ready(function() {
					$("#checkmostrarDesativados").click(function() {
						jQuery.blockUI(objBlock);
						if (document.getElementById('checkmostrarDesativados').checked)
							location.href = '${linkTo[DesignacaoController].listarDesativados}';
						else
							location.href = '${linkTo[DesignacaoController].listar}';
					});
				});
			</script>
		
			<siga:designacao modoExibicao="modoExibicao" designacoes="designacoes" mostrarDesativados="mostrarDesativados"/> 
			
			<script>
				$(document).ready(function() {
					designacaoService.configurarDataTable()
				});
			</script>
		</div>
	</div>
</siga:pagina>
