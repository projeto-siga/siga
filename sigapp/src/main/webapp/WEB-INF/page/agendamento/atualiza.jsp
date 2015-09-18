<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Agendamento Atualiza">
	<link rel="stylesheet" href="/sigapp/stylesheets/jquery-ui.css" type="text/css" media="screen, projection" />
	<br/><br/><br/><br/>
	<div>	
	 <form name="agendamento_atualiza01" method="post" style="border-style: groove; border-color: silver;" action="${linkTo[AgendamentoController].update}">
		  <h2><font color="red">Modo de Edi&ccedil;&atilde;o</font></h2>	
		  <font class="ui-state-default" size="3px" style="position:absolute;left:25%;">Sala:</font>
		
		  <input class="ui-widget" name="sala" type="text" style="position:absolute;left:40%;" size="30" maxlength="30" readonly="readonly" value="${sala_ag}" />
		  <input type="hidden" name="cod_sala" value=${cod_sala} />
		  <br><br>
		  <font class="ui-state-default" size="3px" style="position:absolute;left:25%;">Data:</font>
		  
		  <input class="ui-widget" name="data_ag" type="text" style="position:absolute;left:40%;" size="10" maxlength="10" readonly="readonly" value="${data_ag.substring(8,10)}/${data_ag.substring(5,7)}/${data_ag.substring(0,4)}" />
		  <br><br>
		  <font class="ui-state-default" size="3px" style="position:absolute;left:25%;">Hora:</font>
		  
		  <input class="ui-widget" name="hora_ag" type="text" style="position:absolute;left:40%;" size="5" maxlength="5" readonly="readonly" value="${hora_ag.substring(0,2)}:${hora_ag.substring(2,4)}" />
		  <br><br>
		  <font class="ui-state-default" size="3px" style="position:absolute;left:25%;">N&uacute;mero processo: </font>
		  
		  <input class="ui-widget" name="processo"  type="text" style="position:absolute;left:40%;" size="25" maxlength="50" value="${processo}" />
		  <c:if test="${processo != null}"></c:if>
		  <br><br>
		  <font class="ui-state-default" size="3px" style="position:absolute;left:25%;">Nome periciado: </font>
		 
		  <input class="ui-widget" name="periciado" type="text" style="position:absolute;left:40%;" size="50" maxlength="50" value="${periciado}" />
		  <br><br>
		  <font class="ui-state-default" size="3px" style="position:absolute;left:25%;">Perito ju&iacute;zo: </font>
		  <!--  
		  <input class="ui-widget" name="perito_juizo" type="text" style="position:absolute;left:40%;" size="50" maxlength="50" value="${perito_juizo}" />
		   -->
		   <select  name="perito_juizo" class="ui-widget" style="position:absolute;left:40%;" >
		  		<c:if test="${perito_juizo.trim()!='-'}">
		  			<option value="${perito_juizo}">${nome_perito_juizo} </option>
		  		</c:if>
		  		<option value="-">(escolha o perito)</option>
		  		<c:forEach items="${listPeritos}" var="perito">
		  			<option value="${perito.cpf_perito}">${perito.nome_perito}</option>
		  		</c:forEach>
		  </select>
		  <br><br>
		  <font class="ui-state-default" size="3px" style="position:absolute;left:25%;">Perito parte: </font>
		  
		  <input class="ui-widget" name="perito_parte" type="text" style="position:absolute;left:40%;" size="50" maxlength="50" value="${perito_parte}" />
		  <br><br> 
		  <font class="ui-state-default" size="3px" style="position:absolute;left:25%;left:25%;">&Oacute;rg&atilde;o julgador: </font>
		 
		  <input class="ui-widget" name="orgao_ag" type="text" style="position:absolute;left:40%;" size="50" maxlength="15" value="${orgao_julgador}" readonly="readonly" />
		  <br><br><br>
		  <input class="ui-button" style="position: absolute; left:25%;" type="submit" value="Atualizar" />
		</form> 
		<br><br><br><br><br><br>
		<a style="position:absolute;left:25%;" class="ui-state-hover" href="/sigapp/">Voltar</a> 
	</div>
</siga:pagina>
