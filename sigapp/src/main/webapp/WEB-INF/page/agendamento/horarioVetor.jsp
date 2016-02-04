<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<select class="ui-widget" name="aux_hora_ag" onchange="frm_inclui_agendamento.frm_hora_ag.value=frm_inclui_agendamento.aux_hora_ag.value">
	<option value="">Escolha a hora</option>
	<c:forEach items="${listHorasLivres}" var="hora">
		<option value="${hora}">${hora}</option>
	</c:forEach>
</select>