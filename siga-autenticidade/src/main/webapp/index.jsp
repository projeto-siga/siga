<%@ page language="java"%>
<html>
<head>
<title>Verificação de Autenticidade de Documento</title>
</head>
<body onload="javascript: document.getElementById('numDoc').focus();">
<br />
<br />
<table border="0" align="center" width="50%">
	<tr>
		<td><img border="0" id="logo" src="topo_cons_bg2.jpg" /><br />
		<br />
		</td>
	</tr>
	<tr>
		<td><br />
		<table border="0" align="center">
			<tr>
				<td
					style="font-weight: bold; background-color: #C0C0C0; border-bottom: 15px solid #FFFFFF;">
				Verifica&ccedil;&atilde;o de Autenticidade de Documento</td>
			</tr>
			<tr>
				<td>Digite, com os separadores, o n&uacute;mero que se encontra pr&oacute;ximo
				&agrave; assinatura, na parte inferior do documento impresso</td>
			</tr>
			<tr>
				<td>
				<form id="form" action="./busca"><input type="text"
					id="numDoc" name="numDoc" value="${requestScope['numDoc']}"
					onchange="javascript:document.getElementById('mensagens').innerHTML = '';" />
				<input type="submit" id="submit" value="Verificar" /></form>
				<p id="mensagens" style="color: red;">${requestScope['erro']}</p>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</body>
</html>