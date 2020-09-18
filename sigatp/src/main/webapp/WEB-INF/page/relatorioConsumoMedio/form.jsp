<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="sigatp" tagdir="/WEB-INF/tags/" %>

<jsp:include page="../tags/calendario.jsp" />
<sigatp:decimal />

<script src="/sigatp/public/javascripts/jquery/jquery-ui-1.8.16.custom.min.js"></script>
<script src="/sigatp/public/javascripts/jquery/jquery-1.6.4.min.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$('#lstAbastecimentoReferencia').css("display","none");
		
		$('#lstVeiculos').change(function() {
			carregarCombosAbastecimento();
		});

		$('#lstAbastecimentoInicial').change(function() {
			carregarComboAbastecimentoFinal();
		});

		carregarCombosAbastecimento();
	});

	function carregarCombosAbastecimento() {
		$('#lstAbastecimentoInicial').find('option').remove();
		$('#lstAbastecimentoFinal').find('option').remove();
		var idVeiculo = $('#lstVeiculos').val();
		var lstAbastecimento = "${linkTo[RelatorioConsumoMedioController].carregarComboAbastecimentoInicial()('"+idVeiculo+"')}";
		$.get(
				lstAbastecimento, 
		        function(html) {
			        var htmlarray = html.split("@");
             	    $('#lstAbastecimentoInicial').html(htmlarray[0])
	                $('#lstAbastecimentoFinal').html(htmlarray[1])
 	      	    }
   	    );
	}

	function carregarComboAbastecimentoFinal() {
		if ($('#lstAbastecimentoInicial').val() != null) {
			var itemSelecionado = new Date($('#lstAbastecimentoInicial').val().replace(/(\d{2})-(\d{2})-(\d{4}) (\d{2}):(\d{2})/, "$2/$1/$3 $4:$5"));
			var itemLista;
			
			$('#lstAbastecimentoFinal').find('option').remove();
			$('#lstAbastecimentoInicial option').each(function() {
				itemLista = new Date($(this).val().replace(/(\d{2})-(\d{2})-(\d{4}) (\d{2}):(\d{2})/, "$2/$1/$3 $4:$5"));
				if(itemSelecionado < itemLista) {
					$('#lstAbastecimentoFinal').append($("<option></option>").attr("value",$(this).val()).text($(this).text()));
				}
			});
			document.getElementById('lstAbastecimentoFinal').selectedIndex = 0;
		}
	}
</script>

<form name="formRelConsumoMedio" id="formRelConsumoMedio" action="${linkTo[RelatorioConsumoMedioController].gerarRelatorio}"
	 method="post" cssClass="form" enctype="multipart/form-data">
	 <sigatp:erros/>
	<div class="gt-content-box gt-form"> 
       	<div>
   			<label for="veiculos" class="obrigatorio">Ve&iacute;culo</label>
   			<div id="veiculos">
   				<siga:select name="relatorioConsumoMedio.veiculo.id" id="lstVeiculos"
   					list="veiculos" listKey="id" listValue="dadosParaExibicao" 
   					value="${relatorioConsumoMedio.veiculo.id}" />
   			</div>
		</div>	
		<div>
   			<label for="abastecimentoIniciais" class="obrigatorio">Abastecimento Inicial</label>
   			<div id="abastecimentoIniciais">
   				<siga:select name="relatorioConsumoMedio.abastecimentoInicial.id" id="lstAbastecimentoInicial"
   					listKey="id" listValue="dadosParaExibicao" value="${relatorioConsumoMedio.abastecimentoInicial.id}"  />
   			</div>
   		</div>
		<div>
   			<label for="abastecimentosFinais" class="obrigatorio">Abastecimento Final</label>
   			<div id="abastecimentosFinais">
   				<siga:select name="relatorioConsumoMedio.abastecimentoFinal.id" id="lstAbastecimentoFinal"
   					listKey="id" listValue="dadosParaExibicao" value="${relatorioConsumoMedio.abastecimentoFinal.id}"  />
   			</div>
   		</div>
	</div>
	
	<select name="relatorioConsumoMedio.referencia" size="1" id="lstAbastecimentoReferencia"></select>

	<span class="alerta menor"><fmt:message key="views.erro.preenchimentoObrigatorio"/></span>
	<div class="gt-table-buttons">
		<input type="submit" value="<fmt:message key="views.botoes.pesquisar"/>" class="gt-btn-medium gt-btn-left" />
	</div>
</form>