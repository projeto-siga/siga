<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<siga:pagina titulo="Cadastro de ocorrência de feriado">
	<!-- main content -->
	<script type="text/javascript">
	function validar() {
		var dataIni = $('#dtIniFeriado').val();		
		if (dataIni==null || dataIni=="") {			
			alert("Preencha a data de início");
			document.getElementById('dtIniFeriado').focus();		
		}else
			frm.submit();					
	}
	</script>

<body>

<div class="gt-bd clearfix" style="width: 70% !important;">
	<div class="gt-content clearfix" style="width: 70% !important;">		
		<form name="frm" action="editar_ocorrencia_gravar.action">
		<input type="hidden" name="postback" value="1" /> 
		<ww:hidden name="id" />
		<ww:hidden name="idOcorrencia" />  
		<h2 class="gt-table-head">Cadastrar ocorrência de feriado</h2>
		<div class="gt-content-box gt-for-table">
		<table class="gt-form-table">
			<tr class="header">
				<td colspan="2"><b>${dscFeriado}</h1></td>
			</tr>
			<tr>
				<td width="20%">Data de Início</td>
				<td width="80%" align="left"><ww:textfield name="dtIniFeriado" id="dtIniFeriado" label="Data de Início"
					onblur="javascript:verifica_data(this, true);" theme="simple" /></td>
			</tr>
			<tr>
				<td width="20%">Data de Fim</td>
				<td width="80%" align="left"><ww:textfield name="dtFimFeriado" label="Data de Fim"
						onblur="javascript:verifica_data(this, true);" theme="simple" /></td>			
			<tr class="button">
				<td colspan="2"><input type="button" value="Ok" onclick="javascript: validar();" class="gt-btn-large gt-btn-left" /> <input type="button"
					value="Cancela" onclick="javascript:history.back();" class="gt-btn-medium gt-btn-left" /></td>				
			</tr>
		</table>
		</div>
	<br />
	</div></div>
</body>
</siga:pagina>