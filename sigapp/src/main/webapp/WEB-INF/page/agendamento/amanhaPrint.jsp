<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
 <head>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
 <%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
 <link rel="stylesheet" href="/sigapp/stylesheets/jquery-ui.css" type="text/css" media="screen, projection" />
 <title>Imprime agendadas amanh&atilde;</title>
 <script language="javascript">
	alert('Quando terminar tecle BACKSPACE para retornar.');
 </script>
 </head>
 <body>
 <center>
	<img src="/siga/imagens/brasao.gif"> <br> 
	PODER JUDICI&Aacute;RIO <br>
	Justi&ccedil;a Federal do Rio de Janeiro <br> 
 </center>
 <div name="relatorio" style="font-family: arial, calibri;">
	<center>
		<h4>AGENDAMENTOS DE AMANH&Atilde; - SigaPP</h4>
	</center>
	<div name="exibe data dos agendamentos" style="position: absolute; left: 10%;">
		DATA:
		<c:if
			test="${listAgendamentos != null || listAgendamentos[0] != null}">
			<fmt:formatDate pattern="dd/MM/yyyy"
				value="${listAgendamentos[0].data_ag}" />
		</c:if>
	</div>
	<br>
	<br>
	<div name="tabela dos agendamentos" style="position: absolute; left: 10%;">
		<table border="1" cellspacing="0" cellpadding="4">
			<tr> <!-- cabecalho da tabela dos agendamentos -->
				<th> HORA </th>
				<th> SALA </th>
				<th> PERICIADO </th>
				<th> &Oacute;RG&Atilde;O </th>
				<th> PERITO JU&Iacute;ZO </th>
				<th> PERITO PARTE </th>
			</tr> <!-- detalhe dos agendamentos -->
			<c:forEach items="${listAgendamentos}" var="ag">
				<tr>
					<td>${ag.hora_ag.substring(0,2)}:${ag.hora_ag.substring(2,4)}
					</td>
					<td>${ag.localFk.local}&nbsp;</td>
					<td>${ag.periciado}</td>
					<td>${ag.orgao}&nbsp;</td>
					<td><c:if test="${null == ag.perito_juizo}"> Sem perito do ju&iacute;zo	</c:if>
					    <c:if test="${null != ag.perito_juizo}"> 
					     <c:if test="${'' == ag.perito_juizo.trim()}"> Sem perito do ju&iacute;zo. </c:if>
							<c:forEach items="${listPeritos}" var="prt">
								<c:if test="${ag.perito_juizo.trim() == prt.cpf_perito.trim()}"> ${prt.nome_perito} </c:if>
							</c:forEach>
						</c:if>
						&nbsp; </td>
					<td>${ag.perito_parte}&nbsp;</td>
				</tr>
			</c:forEach>
		</table>
	</div>
 </div>
 </body>
</html>