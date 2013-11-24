<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/xhtml; charset=UTF-8" />
<title>Acesso n&atilde;o autorizado</title>
<script language="javascript" type="text/javascript">
				function logoffSSL()  {
					try{
						  var nodMensagem = document.getElementById("mensagem");
						  var agt=navigator.userAgent.toLowerCase();
						  if (agt.indexOf("msie") != -1) {
						    // IE clear HTTP Authentication
						    document.execCommand("ClearAuthenticationCache");
						    nodMensagem.innerHTML =  "O certificado utilizado n&atilde;o tem autoriza&ccedil;&atilde;o para o acesso!";
						    setTimeout("location.href = 'about:blank'",5000);
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

<body>
	<table width="100%" border="1" cellspacing="0" cellpadding="0"
		bordercolor="#FFFFFF" bgcolor="#FFFFFF">
		<tr  align="center">
			<td style="color: yellow; padding: 2pt; font-size: 14pt; background-color: black;"><span id="acesso">Acesso n&atilde;o autorizado!</span></td>
		</tr>
		<tr align="center">
			<td width="90%" style="color: black; padding: 2pt; font-size: 12pt; "><span
				id="mensagem"></span> </td>
		</tr>

	</table>
</body>
<script language="javascript" type="text/javascript">
	var t=setTimeout("logoffSSL()",500);
</script>
</html>
