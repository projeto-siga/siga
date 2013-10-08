<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>


<siga:pagina titulo="Cadastro de Orgãos">

<body>

<div class="gt-bd clearfix">
	<div class="gt-content clearfix">		
		<form action="editar_gravar.action">
		<input type="hidden" name="postback" value="1" /> 
		<h1>Cadastro de orgão</h1>
		<div class="gt-content-box gt-for-table">
		<table class="gt-form-table" width="100%">
			<tr class="header">
				<td colspan="2">Dados do Orgão Externo</td>
			</tr>
			<tr>				
				<td><ww:textfield name="nmOrgao" label="Nome" maxlength="80" size="80" /></td>
			</tr>
			<tr>		
				<td><ww:textfield name="siglaOrgao" label="Sigla" maxlength="30" size="30" /></td>
			</tr>
			<tr>
				<ww:select name="ativo" label="Ativo" headerKey="-1" list="#{'S':'Sim', 'N':'Não'}" /> 
			</tr>
			<tr>		
				<td>Órgão Solicitante:</td>
				<td><ww:select name="idOrgaoUsu" list="orgaosUsu"
					listKey="idOrgaoUsu" listValue="nmOrgaoUsu" theme="simple" /></td>
			</tr>			
			<tr class="button">
				<td><input type="submit" value="Ok" class="gt-btn-large gt-btn-left" /> <input type="button"
					value="Cancela" onclick="javascript:history.back();" class="gt-btn-medium gt-btn-left" /></td>
				<td></td>
			</tr>
		</table>
		</div>
<br />
</div></div>
</body>

</siga:pagina>