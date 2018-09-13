<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<siga:pagina titulo="Movimentação" desabilitarmenu="sim"
	onLoad="try{var num = document.getElementById('id_number');if (num.value == ''){num.focus();num.select();}else{var cap = document.getElementById('id_captcha');cap.focus();cap.select();}}catch(e){};">
	<script src='https://www.google.com/recaptcha/api.js'></script>
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Autenticação de Documentos</h2>
			<div class="gt-content-box gt-for-table">

				<form action="${request.contextPath}/public/app/autenticar"
					method="GET" class="form">
					<table class="gt-form-table">
						<tr class="header">
							<td colspan="2">Dados do Documento</td>
						</tr>
						<tr>
							<td><label>Número de referência:</label></td>
							<td><input type="text" id="id_number" name="n"
								style="width: 150px;" /></td>
						</tr>
						<tr>
							<td><label>Verificação:</label></td>
							<td>
								<div class="g-recaptcha" data-sitekey="${recaptchaSiteKey}"></div>
							</td>
						</tr>

						<tr class="button">
							<td colspan="2"><input type="submit" value="Ok"
								class="gt-btn-small gt-btn-left" /></td>
						</tr>
					</table>
				</form>
			</div>

			<br />
			<h3>Informações Gerais</h3>
			<p>Para utilizar a Confirmação da Autenticidade do Documento é
				obrigatório informar o número do documento que se encontra no rodapé
				do documento a ser consultado.</p>
			<p>
				<u>Preenchimento do campo Número do Documento</u>
			</p>
			<ul>
				<li>O campo deve ser preenchido com todos os números e traços
					('-').</li>
				<li>Exemplo de preenchimento: 123456-1011</li>
			</ul>
		</div>
	</div>

</siga:pagina>