<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>
			<mod:data titulo="Data de Exercício" var="dataExercicio" />		
		    <mod:data titulo="Data da Vigência da Exclusão" var="dataVigenciaExclusao"/>
		    <mod:texto titulo="Ramal" var="ramal" maxcaracteres="5" largura="5" />
	</mod:entrevista>
	
	<mod:documento>
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
			@page {
				margin-left: 3cm;
				margin-right: 2cm;
				margin-top: 1cm;
				margin-bottom: 2cm;
			}
		</style>
		</head>
		<body>
		
		<h1 algin="center">Ilmo(a). Sr(a). Supervisor(a) da Seção de Benefícios</h1>
		
			 <p style="TEXT-INDENT: 2cm" align="justify">
			 <br><br>
			 	<table width="100%" border="1" cellpadding="3">
					<tr>
						<td bgcolor="#FFFFFF" width="100%" align="center"><b>DADOS DO BENEFICIÁRIO</b></td>
					</tr>
				</table>
			   	<table width="100%" border="1" cellpadding="3">
					<tr>
						<td bgcolor="#FFFFFF" width="50%">Nome: <b>${doc.subscritor.descricao}</b></td>
						<td bgcolor="#FFFFFF" width="30%">Matrícula: <b>${doc.subscritor.sigla}</b></td>
					</tr>
				</table>
				<table width="100%" border="1" cellpadding="3">
					<tr>
						<td bgcolor="#FFFFFF" width="30%">Data de Exercício: <b>${dataExercicio}</b></td>
						<td bgcolor="#FFFFFF" width="60%">Lotação: <b>${doc.subscritor.lotacao.descricao}</b></td>
						<td bgcolor="#FFFFFF" width="10%">Ramal: <b>${ramal}</b></td>
					</tr>
				</table>
				<table width="100%" border="1" cellpadding="3">
					<tr>
						<td bgcolor="#FFFFFF" width="50%">Cargo: <b>${doc.subscritor.cargo.nomeCargo}</b></td>
						<td bgcolor="#FFFFFF" width="50%">Classe/Padrão: <b>${doc.subscritor.padraoReferenciaInvertido}</b></td>
					</tr>
				</table>
							 
			 <p style="TEXT-INDENT: 2cm" align="justify">
				 REQUEIRO A MINHA EXCLUSÃO DO BENEFÍCIO A PARTIR DE <b>${dataVigenciaExclusao}</b>.
			 </p>	
			 <p style="TEXT-INDENT: 2cm" align="justify">
				 Declaro ter conhecimento de que o benefício de Auxílio-Transporte é pago antecipadamente, estando portanto
				 <b>ciente de que deverei devolver qualquer valor que porventura já tenha recebido referente a período porterior
				 à vigência da exclusão.
			 </p>
			 <br>
			 
			<c:import url="/paginas/expediente/modelos/inc_deferimento.jsp" />

			<c:import url="/paginas/expediente/modelos/inc_localDataAssinatura.jsp" />
			
			<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
			
			<br><br><br>
			 
			 <table width="100%" border="1" cellpadding="2" cellspacing="1">
				<tr>
					<td width="60"valign="top">PARA USO DA SEÇÃO DE BENEFÍCIOS<br>Recebido por:_______ em: __/__/__<br>Cadastrado por:______ em:__/__/__<br>Obs: _____________________________________________________<br>_________________________________________________________<br><br></td>
				</tr>
			</table>
		
			</body>
		</html>
	</mod:documento>
</mod:modelo>
