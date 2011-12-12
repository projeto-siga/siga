<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<siga:cabecalho titulo="Login" onLoad="try{document.getElementById('j_username').focus();document.getElementById('j_username').select()}catch(e){};"/>

<script type="text/javascript" language="Javascript1.1">
/*  converte para maiúscula a sigla do estado  */
function converteUsuario(nomeusuario){
  re= /^[a-zA-Z]{2}\d{4,6}$/;
  ret2= /^[a-zA-Z]{1}\d{4,6}$/;
  tmp = nomeusuario.value;
  if (tmp.match(re)||tmp.match(ret2)){      
      nomeusuario.value=tmp.toUpperCase();
  }
}
</script>
<c:set var="pagina" scope="session">${pageContext.request.requestURL}</c:set>
<div>
<form method="post" action="j_security_check">
<table cellpadding="30%" width="100%" height="100%">
	<tr>
		<td align='left' valign='top'><jsp:include flush="true"
			page="comentario.jsp" /></td>
		<td align="right" valign="top">
		<table class="login">
			<tr class="header">
				<td colspan="2">Identificação</td>
			</tr>
			<tr>
				<td>Matrícula</td>
				<td><input id="j_username" type="text" size=15
					name="j_username" onblur="javascript:converteUsuario(this)"></td>
			</tr>
			<tr>
				<td>Senha</td>
				<td><input type="password" size=15 name="j_password"></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Acessar" name="btAcessar"></td>
			</tr>
		</table>
		<br>
		<br>
		<table class="login" width="100%">
			<tr>
				<td>
				<center><b><font size="2"><ww:a
					href="incluir_usuario.action">Sou um novo usuário</ww:a></font></b></center>
				</td>
			</tr>
			<tr>
				<td>
				<center><b><font size="2"><ww:a
					href="esqueci_senha.action">Esqueci minha senha</ww:a></font></b></center>
				</td>
			</tr>
		</table>

		<br>
		<br>
		<table class="login" width="100%">
			<tr>
				<td>
				<center><b><font size="2"><a
					href="apostila_sigaex.pdf">Apostila do SIGA-Documentos</a></font></b></center>
				</td>
			</tr>
		</table>
		<br>
		<table class="login" width="100%">
			<tr>
				<td>
				<center><b><font size="2"><a
					href="apostila_sigawf.pdf">Apostila do SIGA-Workflow</a></font></b></center>
				</td>
			</tr>
		</table>
		<br>
		<table class="login" width="100%">
			<tr>
				<td>
				<center><b><font size="2"><a
					href="apostila_sigase.pdf">Apostila do SIGA-Serviços</a></font></b></center>
				</td>
			</tr>
		</table>
	</tr>
</table>
</form>
</div>
<table width="100%" height="100%">
</table>
<c:import url="/paginas/rodape.jsp" />