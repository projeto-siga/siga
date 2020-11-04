<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ attribute name="nome" required="false"%>
<%@ attribute name="value" required="false"%>
<%@ attribute name="onchange" required="false"%>
<%@ attribute name="cssClass" required="false"%>
<%@ attribute name="id" required="false"%>

<c:set var="nomeclean" value="${fn:replace(nome,'.','')}" />
<c:if test="${id != null}">
	<c:set var="nomeclean" value="${id}" />
</c:if>

<input type="text" name="${nome}" id="${nomeclean}" value="${value}" onchange="${onchange}" class="${cssClass} form-control">

<script>
	$(function() {
		$("#${nomeclean}").datepicker({
		    //showOn: "button",
		    //buttonImage: "/siga/css/famfamfam/icons/calendar.png",
		    //buttonImageOnly: true,
		    dateFormat: 'dd/mm/yy',
		    monthNames: ["Janeiro", "Fevereiro", "Mar\u00E7o", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"],
		    monthNamesShort : ["Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"],
		    dayNames: ["Domingo","Segunda", "Ter\u00E7a", "Quarta", "Quinta", "Sexta", "S\u00E1bado"],
		    dayNamesShort: ["Dom","Seg", "Ter", "Qua", "Qui", "Sex", "Sab"],
		    dayNamesMin: ["Dom","Seg", "Ter", "Qua", "Qui", "Sex", "Sab"],
			firstDay: 0
		});
		$("#${nomeclean}").mask("99/99/9999");
	});
</script>