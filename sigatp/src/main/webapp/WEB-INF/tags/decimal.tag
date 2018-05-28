<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script src="${pageContext.request.contextPath}/public/javascripts/jquery.maskMoney.js"></script>

<script>
	$(function() {
		var $j_object = $(".decimal");
		$j_object.each( function() {
			var texto = $(this)[0].value;
			var vetorValores = texto.split('.');
			if (vetorValores != texto) { 
				if(vetorValores[1].length == 1) {
					$(this).val(texto + '0');
				}	
			}	
		} );
	
		$(".decimal").change(function () {		
			$(".decimal").maskMoney({showSymbol:false, decimal:',', thousands:'.', precision:2});
			$(".decimal").maskMoney('mask');
		})
		.change();
	});
</script>