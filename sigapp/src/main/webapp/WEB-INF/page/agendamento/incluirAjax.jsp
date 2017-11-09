<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Agendar Ajax">
	<link rel="stylesheet" href="/sigapp/stylesheets/jquery-ui.css" type="text/css" media="screen, projection" />
	<script type="text/javascript" language="Javascript1.1" >
	
	function funcData(){
		var dt = document.getElementById('datepicker').value;
		var sl = document.getElementById('frm_cod_local01').value;
		if(sl=="Escolha a sala"){
			alert('A sala de per\u00edcia n\u00e3o foi escolhida.');
			return;	
		}
		if(dt==""){
			alert('A data n\u00e3o foi escolhida');
			return;
		}
		if(document.getElementById('frm_hora_ag01').value==""){	
			alert('A hora n\u00e3o foi escolhida');
			return;
		}
		if(dt=="--"){
			document.getElementById('datepicker').value="";
			alert('Campo data n\u00e3o foi preenchido');
			return;
		}else{
			if(dt!=""){
				document.getElementById('datepicker').value = dt.substr(6,4)+"-"+dt.substr(3,2)+"-"+dt.substr(0,2);
				frm_inclui_agendamento.submit();
			}
		}
	}
	
	function mudouSala(){
		// isso roda
		auxCodLocal = "frm_cod_local= " 
		auxCodLocal = auxCodLocal + frm_inclui_agendamento.frm_cod_local.value;
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
		// vem parametro numa string com as ocorrencias do arraylist separadas por vírgulas
		var i=0;
	 	var diasLotados = new Array(31);
	 	param = param.substr(5,param.length - 13);
	 	diasLotados = param.split(",");
	 	
	 	for(i=0; i<31 ; i++){
	 	 	if(diasLotados[i]!=null){
	 	 		diasLotados[i] = diasLotados[i].replace(" 00:00:00.0","")
	 	 		diasLotados[i] = diasLotados[i].replace(" ","");
	 	 	}else{
	 	 	 	i=31;
	 	 	}
	 	}
	 	
		$(function () {
			$.datepicker.setDefaults({monthNames: [ "Janeiro", "Fevereiro", "Mar&ccedil;o", "Abril", "Maio", "Junho", "Julho", "Agosto",  "Setembro", "Outubro", "Novembro", "Dezembro" ] ,
		        					  dayNamesMin: [ "Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab" ],
		        					  dateFormat: "dd-mm-yy",
		        					  duration: "slow",
		        					  prevText: "M&ecirc;s anterior",
		        					  nextText: "Pr&otilde;ximo m&ecirc;s",
		        					  //buttonImage: "public/images/favicon.png",
		        					  showOn: "button",
		        					  buttonText: "...",
		        					  minDate: new Date(),
		        					  beforeShowDay: function(date){ if($.inArray($.datepicker.formatDate("yy-mm-dd" , date ) , diasLotados )>=0 || date.getDay()==0 || date.getDay()==6){ return [ false , '' , 'Sem horário livre' ];} else {return [ true , '' , '' ];}
		        					  }  	  
		  	});
			$( "#datepicker" ).datepicker();
		 } );
	}
	
	function mudou_data(){
		auxLocaleData = "frm_cod_local= "+ frm_inclui_agendamento.frm_cod_local.value; 
		auxLocaleData = auxLocaleData + "&frm_data_ag= "+ frm_inclui_agendamento.frm_data_ag.value;
		$.ajax({
				url: "horarioVetor?"+auxLocaleData ,
				type: "get" ,	
				success: function(response){document.getElementById('horario01').innerHTML=response;} ,
				error: "" ,
				async: true  
				}).done();
		auxLocaleData = "";
	}
	
	</script>
	<form name="frm_inclui_agendamento" method="post" action="${linkTo[AgendamentoController].insert}">
	<br>
	 <div style="position: absolute;left:05%;">
		<h1 class="ui-accordion-header">Passo um</h1>
		<select class="ui-widget" name="frm_cod_local" id="frm_cod_local01" onchange="mudouSala()">
			<option value="Escolha a sala">Escolha a sala</option>
			<c:forEach items="${listSalas}" var="sala">
				<option value="${sala.cod_local}">
					${sala.local}
				</option>
			</c:forEach>
		</select>
	 </div>
	 
	 <div style="position:absolute;left:30%;">
		<h1 class="ui-accordion-header">Passo dois</h1>
		<input type="text" id="datepicker"  name="frm_data_ag" readonly="readonly"  maxlength="10" onchange="mudou_data()" />
	 </div>
	
	<h1 class="ui-accordion-header" style="position:absolute;left:65%;">Passo tr&ecirc;s</h1>
	<br> <br>
	 <div id="passo03" style="position: absolute;left:65%;">
	 	<input type="hidden" name="frm_hora_ag" id="frm_hora_ag01">
		<div id="horario01">
		</div>	
	 </div>
	 <div style="position: absolute;left:85%;">
	  <h3 class="ui-accordion-header">Lote de hor&aacute;rios<br> em sequ&ecirc;ncia</h3>
	  <select name="lote" class="ui-widget"  >
	   <option value="1">1</option>
	   <option value="2">2</option>
	   <option value="3">3</option>
	   <option value="4">4</option>
	   <option value="5">5</option>
	   <option value="6">6</option>
	   <option value="7">7</option>
	   <option value="8">8</option>
	   <option value="9">9</option>
	   <option value="10">10</option>
	   <option value="11">11</option>
	   <option value="12">12</option>
	   <option value="13">13</option>
	   <option value="14">14</option>
	   <option value="15">15</option>
	   <option value="16">16</option>
	   <option value="17">17</option>
	   <option value="18">18</option>
	  </select>
	 </div>
	 
	 <input type="hidden" name="matricula" value="">
	 <br><br><br><br><br><br><br><br><br>
	 <div>
	  <font class="ui-state-default" size="3px" style="position:absolute;left:25%;">N&uacute;mero processo: </font>
	  
	  <input class="ui-widget" name="processo"  type="text" style="position:absolute;left:40%;" size="25" maxlength="50" />
	  <br><br>
	  <font class="ui-state-default" size="3px" style="position:absolute;left:25%;">Nome periciado: </font>
	 
	  <input class="ui-widget" name="periciado" type="text" style="position:absolute;left:40%;" size="50" maxlength="50" />
	  <br><br>
	  <font class="ui-state-default" size="3px" style="position:absolute;left:25%;">Perito ju&iacute;zo: </font>
	  
	  <select  name="perito_juizo" class="ui-widget" style="position:absolute;left:40%;" >
		<c:if test="${fixo_perito_juizo==null}">
			<option value="-">
				(escolha o perito)
			</option>
		</c:if>
		<c:if test="${fixo_perito_juizo!=null}">
			<option value="${fixo_perito_juizo}">
				${fixo_perito_juizo_nome}
			</option>
		</c:if>
		<c:forEach items="${listPeritos}" var="perito">
			<option value="${perito.cpf_perito.trim()}">
				${perito.nome_perito}
			</option>
		</c:forEach>
	  </select>
	  <br><div style="position:absolute; left:75%;">(use seta pra baixo, ou, mouse)</div>
	  <br>
	  <font class="ui-state-default" size="3px" style="position:absolute;left:25%;">Perito parte: </font>
	  
	  <input class="ui-widget" name="perito_parte" type="text" style="position:absolute;left:40%;" size="50" maxlength="50" />
	  <br><br> 
	  <font class="ui-state-default" size="3px" style="position:absolute;left:25%;right:60%;">&Oacute;rg&atilde;o julgador: </font>
	 
	  <input class="ui-widget" name="orgao" type="text" style="position:absolute;left:40%;" value="${lotacaoSessao}" size="50" maxlength="15" readonly="readonly" />
	  <br><br><br>
	  <input class="ui-button" style="position: absolute; left:25%;" type="button" value="Agendar" onclick="funcData()" />
		<br><br><br><br>
		<a style="position:absolute;left:25%;" class="ui-state-hover" href="/sigapp/">In&iacute;cio</a>  
	 </div>
	 
	</form>
</siga:pagina>