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
</script>

<siga:pagina popup="false" titulo="Troca de Senha">
	<h1><br />
	<br />
	<c:if test="${baseTeste}">
		<div id="msgSenha" style="font-size: 12pt;color: red; font-weight: bold;">ATENÇÃO: Esta é uma versão de testes. Para sua segurança, NÃO utilize a mesma senha da versão de PRODUÇÃO.</div>
	</c:if>
	&nbsp;Troca de senha!</h1>
	<table>
		<tr>
			<td><ww:form action="trocar_senha_gravar"
				onsubmit="return validateUsuarioForm(this);" method="POST">
				<h1>${mensagem }</h1>

				<table class="form" width="100%">
					<ww:hidden name="page" value="1" />
					<tr>
						<td>Matrícula:</td>
						<td><ww:textfield name="nomeUsuario"
							onblur="javascript:converteUsuario(this)" theme="simple" />&nbsp;&nbsp;Ex.:
						XX99999, onde XX é a sigla do seu órgão (T2, RJ e ES) e 99999 é o
						número da sua matrícula.</td>
					</tr>
					<tr>
						<td>Senha atual:</td>
						<td><ww:password name="senhaAtual" theme="simple" /></td>
					</tr>
					<tr>
						<td>Nova senha:</td>
						<td><ww:password name="senhaNova" theme="simple" id="pass"
							onkeyup="passwordStrength(this.value)" />&nbsp;&nbsp;Utilize
						maiúsculas, minúsculas e números para aumentar a força da senha.</td>
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
						<td><ww:password name="senhaConfirma" theme="simple"
							id="pass2" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><ww:submit label="OK" value="OK" theme="simple" />&nbsp;&nbsp;&nbsp;&nbsp;</td>
					</tr>
				</table>
			</ww:form></td>
		</tr>
	</table>

</siga:pagina>

