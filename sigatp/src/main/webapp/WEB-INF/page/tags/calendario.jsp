<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script src="${pageContext.request.contextPath}/public/javascripts/jquery/jquery-1.6.4.min.js"></script>
<script src="${pageContext.request.contextPath}/public/javascripts/jquery/jquery-ui-1.8.16.custom.min.js"></script>
<script src="${pageContext.request.contextPath}/public/javascripts/jquery/i18n/jquery.ui.datepicker-pt-BR.js"></script>
<script src="${pageContext.request.contextPath}/public/javascripts/jquery.maskedinput-1.2.2.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/public/javascripts/jquery/jquery-ui-1.8.16.custom.css" type="text/css" media="screen">

<script>
	var carregarMascaraHora = function(){
			$(".hora").unmask('99:99').mask('99:99');
		};

	var carregarMascaraData = function() {
			$(".dataHora").unmask('99/99/9999 99:99').mask('99/99/9999 99:99');
		};
		
	$(function() {
		$.datepicker.setDefaults($.datepicker.regional['pt-BR']);
		$(".datePicker").datepicker({
			inline : true,
			showOn : 'button',
			buttonText : 'Escolher data',
			buttonImageOnly : true,
			buttonImage : '/siga/css/famfamfam/icons/date.png',
			dateFormat : 'dd/mm/yy',
			constrainInput : true
		}).mask('99/99/9999');

		carregarMascaraHora();
		carregarMascaraData();
	});
</script>
