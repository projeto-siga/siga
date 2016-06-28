<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<siga:pagina titulo="Agendamento Imprime">
	<link rel="stylesheet" href="/sigapp/stylesheets/jquery-ui.css" type="text/css" media="screen, projection" />
	<script type="text/javascript" language="Javascript1.1">
	
	 $(function () {
		 $.datepicker.setDefaults({monthNames: [ "Janeiro", "Fevereiro", "Mar&ccedilo", "Abril", "Maio", "Junho", "Julho", "Agosto",  "Setembro", "Outubro", "Novembro", "Dezembro" ] ,
			  dayNamesMin: [ "Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab" ],
			  dateFormat: "dd-mm-yy",
			  duration: "slow",
			  prevText: "M&ecirc;s anterior",
			  nextText: "Pr&otilde;ximo m&ecirc;s",
			  showOn: "both",
			  buttonText: "...",
			  minDate: new Date()        					  
			  });  	  
	  	$( "#frm_data_ag01" ).datepicker();
	 } );
	 
	</script>
	<br/>
	<br/>
	<center> <h3>IMPRIME <i>MEUS<i></>AGENDAMENTOS</h3> </center>
	<table class="ui-tabs"  align="center" style="font-size:100%;">
		<tr bgcolor="Silver">
			<th>&nbsp; Data &nbsp; </th>
			<th>&nbsp; Hora &nbsp; </th>
			<th>&nbsp; Sala/Local &nbsp;</th>
			<th>&nbsp; Periciado &nbsp;</th>
			<th>&nbsp; &Oacute;rg&atilde;o &nbsp;</th>
			<th>&nbsp; Processo &nbsp;</th>
			<th>&nbsp; Perito Ju&iacute;zo &nbsp;</th>
			<th>&nbsp; Perito Parte &nbsp;</th>
			<th></th>
		 </tr>
	
		<c:forEach items="${listAgendamentos}" var="ag">	 
			<tr class="ui-button-icon-only"
				<c:if test="${!b}">
					bgcolor="#dddddd"
				</c:if>
			/>
			<c:set var="b" value="${!b}"/>
			<td>&nbsp <fmt:formatDate pattern="dd/MM/yyyy" value="${ag.data_ag}" /></td>
			<td>&nbsp ${ag.hora_ag.substring(0,2)}:${ag.hora_ag.substring(2,4)}</td>
			<td>&nbsp ${ag.localFk.local}</td>
			<td>&nbsp ${ag.periciado}</td>
			<td>&nbsp ${ag.orgao}</td>
			<td>&nbsp ${ag.processo}</td>
			<!-- <td>&nbsp ${ag.perito_juizo}</td> -->
			<td>
			<c:if test="${ag.perito_juizo==null}">
				Sem perito do ju&iacute;zo
			</c:if>
			<c:if test="${ag.perito_juizo!=null}">
				<c:if test="${ag.perito_juizo.trim()==''}"> Sem perito do ju&iacute;zo. </c:if>
				<c:forEach items="${listPeritos}" var="prt"> 
					<c:if test="${ag.perito_juizo.trim()==prt.cpf_perito.trim()}">
						${prt.nome_perito}
					</c:if>
		 		</c:forEach>
			</c:if>
			</td>
				<td>&nbsp; ${ag.perito_parte}</td>
				<td>&nbsp;
					<form enctype="multipart/form-data" name="agendamento_imprime01" action="${linkTo[AgendamentoController].print}" method="get">
						<img  src="/siga/css/famfamfam/icons/printer.png">
						<input type="hidden" name="frm_sala_ag" value="${ag.localFk.cod_local}" />
						<input type="hidden" name="frm_data_ag" value="${ag.data_ag}" />
						<input type="hidden" name="frm_periciado" value="${ag.periciado}" />
						<input type="hidden" name="frm_processo_ag" value="${ag.processo}">&nbsp;
						<input type="submit" value="Imprime"/>
					</form>
				</td>
			</tr>
		</c:forEach>
	 </table>
	 <br><br>
	 <div style="position:absolute;left:5%";>
	  <form style="border-style: groove; border-color: silver;" method="get" action="${linkTo[AgendamentoController].imprime}" enctype="multipart/form-data">
	  	<br>
	 	&nbsp; Data:<input type="text" name="frm_data_ag" id="frm_data_ag01" maxlength="10" readonly="readonly" />
	 	
	 	<input type="submit" value="Buscar" /> &nbsp; <br><br>
	 	
	  </form>
	  <br>
	  <a style="position:absolute;left:5%;" class="ui-state-hover" href="/sigapp/">In&iacute;cio</a>
	 </div>

</siga:pagina>