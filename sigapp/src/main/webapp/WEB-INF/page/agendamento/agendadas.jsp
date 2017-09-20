<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<siga:pagina titulo="Agendadas">
	<link rel="stylesheet" href="/sigapp/stylesheets/jquery-ui.css" type="text/css" media="screen, projection" />
	<script type="text/javascript" language="Javascript1.1">
	
	 $(function () {
		 $.datepicker.setDefaults({monthNames: [ "Janeiro", "Fevereiro", "Mar&ccedil;o", "Abril", "Maio", "Junho", "Julho", "Agosto",  "Setembro", "Outubro", "Novembro", "Dezembro" ] ,
			  dayNamesMin: [ "Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab" ],
			  dateFormat: "dd-mm-yy",
			  duration: "slow",
			  prevText: "M&ecirc;s anterior",
			  nextText: "Pr&oacute;ximo m&ecirc;s",
			  showOn: "both",
			  buttonText: "...",
			  minDate: -360      					  
			  });  	  
	  	$( "#frm_data_ag01" ).datepicker();
	 });
	 
	</script>
	<br>
	<br>
	 <table class="ui-tabs"  align="center" style="font-size:100%;">
	 <tr bgcolor="Silver">
	  <th>&nbsp Data &nbsp </th>
	  <th>&nbsp Hora &nbsp </th>
	  <th>&nbsp Sala/Local &nbsp</th>
	  <th>&nbsp Periciado &nbsp</th>
	  <th>&nbsp &Oacute;rg&atilde;o &nbsp</th>
	  <th>&nbsp Processo &nbsp</th>
	  <th>&nbsp Perito Ju&iacute;zo &nbsp</th>
	  <th>&nbsp Perito Parte &nbsp</th>
	 </tr>
		 
	<c:forEach items="${listAgendamentos}" var="ag">
		<c:choose>
			<c:when test="${b}">
				<tr class="ui-button-icon-only" >
			</c:when>
			<c:otherwise>
				<tr class="ui-button-icon-only" bgcolor="#dddddd">
			</c:otherwise>
		</c:choose>
	<c:choose>
		<c:when test="${b}">
			<tr class="ui-button-icon-only" >
		</c:when>
		<c:otherwise>
			<tr class="ui-button-icon-only" bgcolor="#dddddd">
		</c:otherwise>
	</c:choose>
	<c:set var="b" value="${!b}"></c:set>
		<td>&nbsp ${ag.data_ag.toString().substring(8,10)}-${ag.data_ag.toString().substring(5,7)}-${ag.data_ag.toString().substring(0,4)}</td>
		<td>&nbsp ${ag.hora_ag.substring(0,2)}:${ag.hora_ag.substring(2,4)}</td>
		<td>&nbsp ${ag.localFk.local}</td>
		<td>&nbsp ${ag.periciado}</td>
		<td>&nbsp ${ag.orgao}</td>
		<td>&nbsp ${ag.processo}</td>
		<td>&nbsp
		<c:choose>
			<c:when test="${ag.perito_juizo==null}">
				Sem perito do ju&iacute;zo
			</c:when>
			<c:otherwise>
				<c:if test="${ag.perito_juizo.trim()==''}">
					Sem perito do ju&iacute;zo.
				</c:if>
				<c:forEach items="${listPeritos}" var="prt">
					<c:if test="${ag.perito_juizo.trim()==prt.cpf_perito.trim()}">
						${prt.nome_perito}
					</c:if>
				</c:forEach>
			</c:otherwise>
		</c:choose>
			</td>	
		<td>&nbsp ${ag.perito_parte}</td>
		</tr>
	</c:forEach>
	 </table>
	 <br><br>
	 <div style="position:absolute;left:5%";>
	  <form style="border-style: groove; border-color: silver;" method="get" action="${linkTo[AgendamentoController].agendadas}">
	  	<br>
	 	&nbsp Data:<input type="text" name="data" id="frm_data_ag01" maxlength="10" readonly="readonly" />
	 	
	 	<input type="submit" value="Buscar" /> &nbsp <br><br>
	 	
	  </form>
	  <br>
	  <a style="position:absolute;left:5%;" class="ui-state-hover" href="/sigapp/">In&iacute;cio</a>
	 </div>
 </siga:pagina>