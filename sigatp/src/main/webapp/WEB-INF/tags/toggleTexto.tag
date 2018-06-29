<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="totalCaracteres"%>
<%@ attribute name="tipoLista"%>
<%@ attribute name="tagSeparador"%>
<%@ attribute name="tagInicial"%>
<%@ attribute name="incluirNoFinal"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<style>
	.completo{
	    display:none;
	    width:100%;
	}
	
	.mais{
	    color:navy;
	    font-size:13px;
	    padding:3px;
	    cursor:pointer;
	    width:100%;
	    font-weight:bold;
	}
</style>

<script type="text/javascript" src="/sigatp/public/javascripts/jquery/jquery-ui-1.8.16.custom.min.js"></script>

<script>
	var aux = "";
	var total = "";
	var tagSeparador = "";
	var tagInicial = "";
	var incluirNoFinal = "";

	function dividirLinhas(linha, conteudo) {
		var itensInicio = $(".inicio");
		var itensCompleto = $(".completo");
		var itensMore = $(".mais");

		if (tagSeparador != "") {
	        aux = conteudo.split(tagSeparador);
	        
	        if(aux.length - 1 > total) { 
	            itensInicio[linha].innerHTML = aux.slice(0, total).join(tagSeparador) + (incluirNoFinal != "" ? tagSeparador : "");
	            var linhaCompleta = tagInicial + aux.slice(total, aux.length).join(tagSeparador);
	            itensCompleto[linha].innerHTML = linhaCompleta.substring(0, linhaCompleta.lastIndexOf(tagSeparador)); 
	        }
	        else {
	            itensInicio[linha].innerHTML = conteudo;
	            itensMore[linha].textContent = "";  
	        }
		} else {
			var pontuacao = [",", ".", ";", "-", ":", "(", ")"," "];
	        aux = conteudo.substring(0, total);
			var n = 0;
	        var pos;

	        if (conteudo!=aux) {
		        for (j = 0; j < pontuacao.length; j++) { 
		             pos = aux.lastIndexOf(pontuacao[j]);
		             if (pos != -1 && n < pos) {
		                 n = pos;
		             } 
		        } 

		        itensInicio[linha].textContent = aux.substring(0, n+1);
	        	itensCompleto[linha].textContent = aux.substring(n+1) + conteudo.replace(aux,'');
	        } else {
	        	itensInicio[linha].textContent = aux;
	        	itensMore[linha].textContent = "";
	        } 
		}
	}
	
	$(function() {
		var itens = $(".toggleTexto");
		var inputHidden = $("#txtToggleTexto");

		<c:if test="${not empty tagSeparador}">
			tagSeparador = "${tagSeparador}";
		</c:if>

		<c:if test="${not empty tagInicial}">
			tagInicial = "${tagInicial}";
		</c:if>
	
		<c:if test="${not empty totalCaracteres}">
			total = "${totalCaracteres}";
		</c:if>
		
		<c:if test="${not empty tipoLista}">
			total = acessarParametro();
		</c:if>

		<c:if test="${not empty incluirNoFinal}">
			incluirNoFinal= "${incluirNoFinal}";
		</c:if>
		
		for (i=0; i<itens.length; i++) {
			inputHidden.val(itens[i].innerHTML);
			dividirLinhas(i, inputHidden.val());
		}

		$(".mais").toggle(function() {
		    $(this).text('\u00AB menos').siblings(".completo").show();
		}, function(){
		    $(this).text("mais \u00BB").siblings(".completo").hide();    
		});
	});

	function acessarParametro() {
		var url;
		var tipo = "${tipoLista}";
		var total="";

		if (tipo == 'requisicao') {
			url = "${linkTo[RequisicaoController].getRequisicaoLinhasTotal}";
		}
		else if (tipo == 'missao') {
			url = "${linkTo[MissaoController].getMissaoLinhasTotal}";
		}
		
		$.ajax({
			url : url,
			dataType : 'text',
			async: false,  
			success : function(data) {
				total = data;
			}
		});

		return total;
	}
</script>