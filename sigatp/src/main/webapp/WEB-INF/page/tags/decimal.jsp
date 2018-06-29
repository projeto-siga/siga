<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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