<%@page import="br.gov.jfrj.siga.sr.model.SrPrioridade"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<c:set var="prioridade_planejado">
    <%=SrPrioridade.PLANEJADO.name()%>
</c:set>

<c:set var="prioridade_desc">
    <%=SrPrioridade.PLANEJADO.getDescPrioridade()%>
</c:set>

<div id="divPrioridade" class="gt-form-row gt-width-66">
	<label style="float: left">Prioridade: &nbsp;</label>
	<span>${solicitacao.prioridade != null ? solicitacao.prioridade.descPrioridade : prioridade_desc}</span>
	<siga:select name="prioridade" id="prioridade" list="prioridadeList" listValue="descPrioridade" listKey="idPrioridade" isEnum="true"
				value="${solicitacao.prioridade != null ? solicitacao.prioridade: prioridade_planejado}" style="width:235px;border:none;display:none;"/>
	
	<br />
</div>
