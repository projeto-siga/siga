<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>Desconexão do Certificado</title>
		<script language="javascript" type="text/javascript">
				function logoffSSL()  {
					try{
						  var nodMensagem = document.getElementById("mensagem");
						  var agt=navigator.userAgent.toLowerCase();
						  if (agt.indexOf("msie") != -1) {
						    // IE clear HTTP Authentication
						    document.execCommand("ClearAuthenticationCache");
						    nodMensagem.innerHTML = "Logoff realizado! Utilize o menu do SIGA se desejar conectar novamente.";
						  }
						  else {
							  nodMensagem.innerHTML = "Navegador n&atilde;o suporta desconex&atilde;o! Para a sua seguran&ccedil;a, o mesmo dever&aacute; ser encerrado!";
							  window.open('','_self',''); 
							  window.close();
						  }
					} catch(e) {
							
						    var desc = "";
						    if (e.description) {
						    	desc = e.description ;
						    } else {
							    desc = e.message ;
							}	 
							document.write("Ocorreu um erro ao desconectar: \n" + desc + "\nPor favor, feche e inicie novamente o navegador." );
							window.close();
					}
				}
		
		</script>
	
	</head>

	<siga:pagina titulo="Logoff">

			<table  width="90%"  border="1" cellspacing="0" cellpadding="0"
				bordercolor="#FFFFFF" bgcolor="#FFFFFF">
				<tr align="center"><td style="color: darkblue; padding: 2pt; font-size: 14pt;"><span>Logoff selecionado.</td></td></tr>
				<tr align="center">
					<td width="100%" style="color: darkblue; padding: 2pt; font-size: 12pt;">
						<span id="mensagem"></span>
				</tr>
			</table>
	</siga:pagina>
	<script language="javascript" type="text/javascript">
		var t=setTimeout("logoffSSL()",500);
	</script>
</html>
