<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script src="${pageContext.request.contextPath}/public/javascripts/jquery/jquery-1.6.4.min.js"></script>
<script src="${pageContext.request.contextPath}/public/javascripts/jquery/jquery-ui-1.8.16.custom.min.js"></script>
<script src="${pageContext.request.contextPath}/public/javascripts/jquery/i18n/jquery.ui.datepicker-pt-BR.js"></script>
<script src="${pageContext.request.contextPath}/public/javascripts/jquery.maskedinput-1.2.2.min.js'}"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/public/javascripts/jquery/jquery-ui-1.8.16.custom.css" type="text/css" media="screen">

<script>

function validarCNPJ(cnpj) {
	 
    cnpj = cnpj.replace(/[^\d]+/g,'');
 
    if(cnpj == '') return false;
     
    if (cnpj.length != 14)
        return false;
 
    // Elimina CNPJs invalidos conhecidos
    if (cnpj == "00000000000000" || 
        cnpj == "11111111111111" || 
        cnpj == "22222222222222" || 
        cnpj == "33333333333333" || 
        cnpj == "44444444444444" || 
        cnpj == "55555555555555" || 
        cnpj == "66666666666666" || 
        cnpj == "77777777777777" || 
        cnpj == "88888888888888" || 
        cnpj == "99999999999999")
        return false;
         
    // Valida DVs
    tamanho = cnpj.length - 2
    numeros = cnpj.substring(0,tamanho);
    digitos = cnpj.substring(tamanho);
    soma = 0;
    pos = tamanho - 7;
    for (i = tamanho; i >= 1; i--) {
      soma += numeros.charAt(tamanho - i) * pos--;
      if (pos < 2)
            pos = 9;
    }
    resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
    if (resultado != digitos.charAt(0))
        return false;
         
    tamanho = tamanho + 1;
    numeros = cnpj.substring(0,tamanho);
    soma = 0;
    pos = tamanho - 7;
    for (i = tamanho; i >= 1; i--) {
      soma += numeros.charAt(tamanho - i) * pos--;
      if (pos < 2)
            pos = 9;
    }
    resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
    if (resultado != digitos.charAt(1))
          return false;
           
    return true;
    
}

$(function() {
	$( ".cnpj" ).mask('99.999.999/9999-99');
});

$(document).ready(function() { 
	$('.cnpj').blur(function() {
		if($('.cnpj').val() != "" && !validarCNPJ($('.cnpj').val())){
			var validacaoRepetida = $("#divErros").text().search("&{'cnpj.validation'}");
			if (validacaoRepetida == -1) {
				$("#divErros").show();
				$('#divErros').append("<li>&{'cnpj.validation'}</li>");
				$('.cnpj').focus();
			}
		};		
	});
});

</script>
