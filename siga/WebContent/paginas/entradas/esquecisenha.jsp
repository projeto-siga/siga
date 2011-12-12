<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<style>
#passwordStrength {
	height: 10px;
	display: block;
	float: left;
}

.strength0 {
	width: 250px;
	background: #cccccc;
}

.strength1 {
	width: 50px;
	background: #ff0000;
}

.strength2 {
	width: 100px;
	background: #ff5f5f;
}

.strength3 {
	width: 150px;
	background: #56e500;
}

.strength4 {
	background: #4dcd00;
	width: 200px;
}

.strength5 {
	background: #399800;
	width: 250px;
}
</style>

<script type="text/javascript" language="Javascript1.1">

/*  converte para maiúscula a sigla do estado  */
function converteUsuario(nomeusuario){
  re= /^[a-zA-Z0-9]{2}\d{4,6}$/;
  tmp = nomeusuario.value;
  if (tmp.match(re)){      
      nomeusuario.value=tmp.toUpperCase();
  }
}

function passwordStrength(password) {
        var desc = new Array();
        desc[0] = "Inaceitável";
        desc[1] = "Muito Fraca";
        desc[2] = "Fraca";
        desc[3] = "Razoável";
        desc[4] = "Boa";
        desc[5] = "Forte";
        var score   = 0;
        
        //if password bigger than 6 give 1 point
        if (password.length >= 6) score++;

        //if password has both lower and uppercase characters give 1 point      
        if ( ( password.match(/[a-z]/) ) && ( password.match(/[A-Z]/) ) ) score++;

        //if password has at least one number give 1 point
        if ( ( password.match(/[a-z]/) ||  password.match(/[A-Z]/) ) && (password.match(/\d+/))) score++;

        //if password has at least one special caracther give 1 point
        if ( password.match(/.[!,@,#,$,%,^,&,*,?,_,~,-,(,)]/) ) score++;

        //if password bigger than 12 give another 1 point
        if (password.length >= 12) score++;
        
        //mininum requirements to be accepted by the AD
        if (score > 2 && (password.length < 6 || !password.match(/[a-z]/) || !password.match(/[A-Z]/) || !password.match(/\d+/)))
        	score = 2;

         document.getElementById("passwordDescription").innerHTML = desc[score];
         document.getElementById("passwordStrength").className = "strength" + score;
}

function validateUsuarioForm(form) {
	var s = document.getElementById("passwordStrength").className;
	if (s == "strength0" || s == "strength1" || s == "strength2") {
		alert("Senha muito fraca. Por favor, utilize uma senha com pelo menos 6 caracteres incluindo letras maiúsculas, minúsculas e números");
		return false;
	}
	var p1 = document.getElementById("pass").value;
	var p2 = document.getElementById("pass2").value;
	if (p1 != p2) {
		alert("Repetição da nova senha não confere, favor redigitar.");
		return false;
	}
	return true;
}
</script>

<siga:pagina titulo="${param.titulo}">
	<div>
	<h1>${param.titulo}</h1>

	<h2>&nbsp;${mensagem }</h2>
	<h2>Método 1 - Envio de senha nova para o e-mail</h2>
	<p style="font-size: x-small; font-style: italic;">O sistema gera
	uma senha aleatoriamente e a envia para o email da pessoa informada.</p>
	<ww:form action="${param.proxima_acao}" theme="simple">
		<ww:hidden name="metodo" value="1" />
		<table class="form" width="100%">
			<ww:hidden name="page" value="1" />
			<tr class="header">
				<td colspan="2">Criar uma nova senha automaticamente e enviar
				para o e-mail de:</td>
			</tr>
			<tr>
				<td>Matrícula:</td>
				<td><ww:textfield name="matricula"
					onblur="javascript:converteUsuario(this)" theme="simple" />&nbsp;&nbsp;Ex.:
				XX99999, onde XX é a sigla do seu órgão (T2, RJ, ES, etc.) e 99999 é
				o número da sua matrícula.</td>
			</tr>
			<tr>
				<td>CPF:</td>
				<td><ww:textfield name="cpf" theme="simple" /></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><ww:submit label="OK" value="OK" theme="simple" />&nbsp;&nbsp;&nbsp;&nbsp;<ww:submit
					label="Cancelar" value="Cancelar" theme="simple" /></td>
			</tr>

		</table>
	</ww:form> <br />

	<h2>Método 2 - Alterar a senha com auxílio de 2 pessoas</h2>
	<p style="font-size: x-small; font-style: italic;">O sistema altera
	a senha da pessoa conforme solicitado, porém é necessário o apoio de
	mais 2 pessoas para completar o processo. <br />
	As pessoas auxiliares devem estar na mesma lotação da pessoa indicada
	para ter a senha substituída ou devem estar na lotação imediatamente
	superior na hierarquia organizacional.</p>
	<ww:form action="${param.proxima_acao}"
		onsubmit="return validateUsuarioForm(this);" method="post"
		theme="simple">
		<ww:hidden name="metodo" value="2" />

		<table class="form" width="100%">
			<tr class="header">
				<td colspan="2">Primeiro auxiliar</td>
			</tr>
			<tr>
				<td>Matrícula:</td>
				<td><ww:textfield name="auxiliar1"
					onblur="javascript:converteUsuario(this)" theme="simple" />&nbsp;&nbsp;Ex.:
				XX99999, onde XX é a sigla do seu órgão (T2, RJ, ES, etc.) e 99999 é
				o número da matrícula da primeira pessoa que auxiliará na alteração
				de senha.</td>
			</tr>
			<tr>
				<td>CPF:</td>
				<td><ww:textfield name="cpf1" theme="simple" /></td>
			</tr>
			<tr>
				<td>Senha:</td>
				<td><ww:password name="senha1"
					onblur="javascript:converteUsuario(this)" theme="simple" /></td>
			</tr>
			<tr class="header">
				<td colspan="2">Segundo auxiliar</td>
			</tr>
			<tr>
				<td>Matrícula:</td>
				<td><ww:textfield name="auxiliar2"
					onblur="javascript:converteUsuario(this)" theme="simple" />&nbsp;&nbsp;Ex.:
				XX99999, onde XX é a sigla do seu órgão (T2, RJ, ES, etc.) e 99999 é
				o número da matrícula da segunda pessoa que auxiliará na alteração
				de senha.</td>
			</tr>
			<tr>
				<td>CPF:</td>
				<td><ww:textfield name="cpf2" theme="simple" /></td>
			</tr>
			<tr>
				<td>Senha:</td>
				<td><ww:password name="senha2"
					onblur="javascript:converteUsuario(this)" theme="simple" /></td>
			</tr>
			<tr class="header">
				<td colspan="2">Alterar senha de:</td>
			</tr>
			<tr>
				<td>Matrícula:</td>
				<td><ww:textfield name="matricula"
					onblur="javascript:converteUsuario(this)" theme="simple" />&nbsp;&nbsp;Ex.:
				XX99999, onde XX é a sigla do seu órgão (T2, RJ, ES, etc.) e 99999 é
				o número da matrícula do usuário que terá a senha alterada.</td>
			</tr>
			<tr>
				<td>CPF:</td>
				<td><ww:textfield name="cpf" theme="simple" /></td>
			</tr>
			<tr>
				<td>Nova Senha:</td>
				<td><ww:password name="senhaNova" id="pass"
					onkeyup="passwordStrength(this.value)" theme="simple" /></td>
			</tr>
			<tr>
				<td>Força da senha:</td>
				<td>
				<div id="passwordDescription">Senha não informada</div>
				<div id="passwordStrength" class="strength0"></div>
				</td>
			</tr>
			<tr>
				<td>Repetição da nova senha:</td>
				<td><ww:password name="senhaConfirma" id="pass2"
					onblur="javascript:converteUsuario(this)" theme="simple" /></td>
			</tr>

			<tr>
				<td>&nbsp;</td>
				<td><ww:submit label="OK" value="OK" theme="simple" />&nbsp;&nbsp;&nbsp;&nbsp;<ww:submit
					label="Cancelar" value="Cancelar" theme="simple" /></td>
			</tr>
		</table>
	</ww:form></div>
</siga:pagina>
