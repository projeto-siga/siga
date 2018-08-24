<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script src="${pageContext.request.contextPath}/public/javascripts/jquery/jquery-1.6.4.min.js"></script>
<script src="${pageContext.request.contextPath}/public/javascripts/jquery/jquery-ui-1.8.16.custom.min.js"></script>
<script src="${pageContext.request.contextPath}/public/javascripts/jquery/i18n/jquery.ui.datepicker-pt-BR.js"></script>
<script src="${pageContext.request.contextPath}/public/javascripts/jquery.maskedinput-1.2.2.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/public/javascripts/jquery/jquery-ui-1.8.16.custom.css" type="text/css" media="screen">

<script>
$(document).ready(function() { 
	$('.email').blur(function() {
		var padrao = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
		if($('.email').val() != "" && !padrao.test($('.email').val())){
			var validacaoRepetida = $("#divErros").text().search("&{'email.validation'}");
			if (validacaoRepetida == -1) {
				$("#divErros").show();
				$('#divErros').append("<li>&{'email.validation'}</li>");
				$('.email').focus();
			}
		};		
	});
});
</script>