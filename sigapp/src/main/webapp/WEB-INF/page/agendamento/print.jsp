<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<title>Agendamento Print</title>

<script language="javascript">
	alert('Quando terminar tecle BACKSPACE para retornar.');
</script>
<center>
	<img src="/siga/imagens/brasao.gif"><br/> 
	PODER JUDICI&Aacute;RIO<br> Justi&ccedil;a Federal do Rio de Janeiro
</center>
<div style="font-family: arial, calibri;">
	<center>
		<h4>Relat&oacute;rio de agendamento SigaPP</h4>
	</center>
	Rio de Janeiro, <input id="data01" type="text"
		style="border: thin solid white;" readonly="readonly" /> <br><b>Processo:</b>${frm_processo_ag}
		<br> <b>Periciado:</b>${listAgendamentos[0].periciado} <script
				language="javascript">
			//var dat = new Date('dec 15 2014');
			var dat = new Date();
			var mes = dat.getMonth() + 1;
			var dia = dat.getDate();
			var ano = dat.getFullYear();
			document.getElementById('data01').value = dia + "/" + mes + "/"
					+ ano;
		</script>
		<table border="2" cellspacing="0" cellpadding="4">
			<tr>
				<th>DATA</th>
				<th>HORA</th>
				<th>SALA</th>
				<th>&Oacute;RG&Atilde;O</th>
				<th>PERITO JU&Iacute;ZO</th>
				<th>PERITO PARTE</th>
			</tr>
			<c:forEach items="${listAgendamentos}" var="ag">
				<tr>
					<td><fmt:formatDate pattern="dd/MM/yyyy" value="${ag.data_ag}" /></td>
					<td>${ag.hora_ag.substring(0,2)}: ${ag.hora_ag.substring(2,4)}</td>
					<td>${ag.localFk.local}&nbsp;</td>
					<td>${ag.orgao}&nbsp;</td>
					<!-- <td>${ag.perito_juizo} &nbsp</td> -->
					<td>
						<c:if test="${null == ag.perito_juizo}">
							Sem perito do ju&iacute;zo
						</c:if> 
						<c:if test="${null != ag.perito_juizo}">
							<c:if test="${'' == ag.perito_juizo.trim()}">
								Sem perito do ju&iacute;zo.
							</c:if>
							<c:forEach items="${listPeritos}" var="prt">
								<c:if test="${ag.perito_juizo.trim() == prt.cpf_perito.trim()}">
									${prt.nome_perito}
								</c:if>
							</c:forEach>
						</c:if>
					</td>
					<td>${ag.perito_parte}&nbsp;</td>
				</tr>
			</c:forEach>
		</table>
</div>