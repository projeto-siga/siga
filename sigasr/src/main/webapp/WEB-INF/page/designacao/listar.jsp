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
				
				var QueryString = function () {
					// This function is anonymous, is executed immediately and
					// the return value is assigned to QueryString!
					var query_string = {};
					var query = window.location.search.substring(1);
					var vars = query.split("&");
					for (var i=0;i<vars.length;i++) {
						var pair = vars[i].split("=");
				    	// If first entry with this name
				    	if (typeof query_string[pair[0]] === "undefined") {
							query_string[pair[0]] = pair[1];
							// If second entry with this name
						} else if (typeof query_string[pair[0]] === "string") {
							var arr = [ query_string[pair[0]], pair[1] ];
							query_string[pair[0]] = arr;
							// If third or later entry with this name
						} else {
							query_string[pair[0]].push(pair[1]);
						}
					}
					return query_string;
				}();
				
				//removendo a referencia de '$' para o jQuery
				$( document ).ready(function() {
					if (QueryString.mostrarDesativados != undefined) {
						document.getElementById('checkmostrarDesativado').checked = QueryString.mostrarDesativados == 'true';
						document.getElementById('checkmostrarDesativado').value = QueryString.mostrarDesativados == 'true';
					}
						
					$("#checkmostrarDesativado").click(function() {
						jQuery.blockUI(objBlock);
						if (document.getElementById('checkmostrarDesativado').checked)
							location.href = '${linkTo[DesignacaoController].listar[true]}';
						else
							location.href = '${linkTo[DesignacaoController].listar[false]}';
					});
				});
			</script>
		
			<siga:designacao modoExibicao="modoExibicao" designacoes="designacoes" mostrarDesativado="mostrarDesativado"/> 
			
			<script>
				$(document).ready(function() {
					designacaoService.configurarDataTable()
				});
			</script>
		</div>
	</div>
</siga:pagina>
