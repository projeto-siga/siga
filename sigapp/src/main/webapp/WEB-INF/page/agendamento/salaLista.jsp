<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Agenda Sala Lista">
	<link rel="stylesheet" href="/sigapp/stylesheets/jquery-ui.css" type="text/css" media="screen, projection" />
	<script type="text/javascript" language="Javascript1.1" >
	function funcData(){
		var dt = document.getElementById('datepicker').value;
		var sl = document.getElementById('frm_cod_local01').value;
		if(sl=="Escolha a sala"){
			alert('A sala de per\u00edcia n\u00e3o foi escolhida');
			return;	
		}
		if(dt==""){
			alert('A data n\u00e3o foi escolhida');
			return;
		}
		
		if(dt=="--"){
			document.getElementById('datepicker').value="";
			alert('Campo data n\u00e3o foi preenchido');
			return;
		}else{
			if(dt!=""){
				document.getElementById('datepicker').value = dt.substr(6,4)+"-"+dt.substr(3,2)+"-"+dt.substr(0,2);
				frm_agendamento_sala_lista.submit();
			}
		}
	}
	
	function mudouSala(){
		auxCodLocal = "frm_cod_local= ";
		auxCodLocal = auxCodLocal + frm_agendamento_sala_lista.frm_cod_local.value;
		$.ajax({
				url: "calendarioVetor?"+auxCodLocal,
				type: "get",	
				success: function(response){calendario_resposta(response)} ,
				error: "" ,
				async: true  
				}).done();
		auxCodLocal = "";	
	}
	function calendario_resposta(param){
		$(function () {
			$.datepicker.setDefaults({monthNames: [ "Janeiro", "Fevereiro", "Mar&ccedil;o", "Abril", "Maio", "Junho", "Julho", "Agosto",  "Setembro", "Outubro", "Novembro", "Dezembro" ] ,
		        					  dayNamesMin: [ "Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab" ],
		        					  dateFormat: "dd-mm-yy",
		        					  duration: "slow",
		        					  prevText: "M&ecirc;s anterior",
		        					  nextText: "Pr&oacute;ximo m&ecirc;s",
		        					  showOn: "button",
		        					  buttonText: "...",
		        					  minDate: new Date()
		        					    	  
		  	});
			$( "#datepicker" ).datepicker();
		 } );
	 }
	</script>
	<center><h3 class="ui-accordion-header">IMPRIME AGENDAMENTOS DE <i>UMA</i> SALA</h3></center>
	<div style="position:relative;left:05%;">
		<h4 class="ui-accordion-header">&Oacute;RG&Atilde;O:<input class="ui-spinner-input" type="text" value="${lotacaoSessao}" readonly="true" /></h4>
		<br>
		<h4 class="ui-accordion-header">SALA: &nbsp;<input class="ui-spinner-input" type="text" value="${local}" readonly="true" /></h4>
		</div> 
	<table class="ui-tabs"  align="center" style="font-size:100%;">
		<tr bgcolor="Silver"><th>&nbsp Data &nbsp</th><th>&nbsp Hora &nbsp</th><th>&nbsp Periciado &nbsp</th><th>&nbsp Processo &nbsp</th><th>&nbsp Perito Ju&iacute;zo &nbsp</th><th>&nbsp Perito Parte &nbsp</th><th>&nbsp</th>
	 	</tr>
	 	  <c:forEach items="${listAgendamentosMeusSala}" var="ag">	
			  <tr class="ui-button-icon-only"
			  	<c:if test="${!b}">
			  		bgcolor="#dddddd"
			  	</c:if>
			  />
			  <c:set var="b" value="${!b}"/>
			  <td>&nbsp;
			  	${ag.data_ag.toString().substring(8,10)}/
			  	${ag.data_ag.toString().substring(5,7)}/
			  	${ag.data_ag.toString().substring(0,4)}
			  </td>
			  <td>&nbsp;
				  ${ag.hora_ag.substring(0,2)}:
				  ${ag.hora_ag.substring(2,4)}
			  </td>
			  <td>&nbsp; ${ag.periciado} </td>
			  <td>&nbsp; ${ag.processo} </td>
			  <td>
			  <c:if test="${null == ag.perito_juizo}"> Sem perito do ju&iacute;zo </c:if>
			  <c:if test="${null != ag.perito_juizo}">
				<c:if test="${'' == ag.perito_juizo.trim()}"> Sem perito do ju&iacute;zo. </c:if>
					<c:forEach items="${listPeritos}" var="prt"> 
						<c:if test="${ag.perito_juizo.trim() == prt.cpf_perito.trim()}"> ${prt.nome_perito} </c:if>
			 		</c:forEach>
			  </c:if>
			  </td>
			  <td>&nbsp; ${ag.perito_parte}</td>
			  <td>&nbsp; <form name="agendamento_sala-lista01" action="${linkTo[AgendamentoController].print}" method="get"><img  src="/siga/css/famfamfam/icons/printer.png"><input type="hidden" name="frm_sala_ag" value="${ag.localFk.cod_local}" /><input type="hidden" name="frm_data_ag" value="${ag.data_ag}" /> <input type="hidden" name="frm_periciado" value="${ag.periciado}" /> <input type="hidden" name="frm_processo_ag" value="${ag.processo}">&nbsp<input type="submit" value="Imprime"/></form></td>
			  </tr>
		</c:forEach>
	</table>
	<form name="frm_agendamento_sala_lista" method="get" action="${linkTo[AgendamentoController].salaLista}" >
	<br>
	 <div style="position: absolute;left:05%;">
		<h3 class="ui-accordion-header">Escolha a sala</h3>
		<select class="ui-widget" name="frm_cod_local" id="frm_cod_local01" onchange="mudouSala();">
			<option value="Escolha a sala">Escolha a sala</option>
			<c:forEach items="${listSalas}" var="sala">
				<option value="${sala.cod_local}">
					${sala.local}
				</option>
			</c:forEach>
		</select>
	 </div>
	 
	 <div style="position:absolute;left:30%;">
		<h3 class="ui-accordion-header">Escolha o dia</h3>
		<input type="text" id="datepicker"  name="frm_data_ag" readonly="readonly"  maxlength="10"  />
	 </div>
	
	 <br><br><br><br><br><br><br><br><br>
	   <input class="ui-button" style="position: absolute; left:05%;" type="button" value="Listar" onclick="funcData();" />
		<br><br><br><br>
		<a style="position:absolute;left:05%;" class="ui-state-hover" href="/sigapp/">In&iacute;cio</a>  
	 </div>
	</form>
</siga:pagina>