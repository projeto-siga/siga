<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<br />
		<mod:texto titulo="E-mail Institucional (alias)"  var="email" largura="30" maxcaracteres="30" obrigatorio="Sim" />@jfrj.jus.br
		<br /><br />
		<mod:mensagem vermelho="Sim" texto="Para indicação de novo usuário, no caso de remoção do magistrado para outra Seção Judiciária ou Tribunal, 
		deverá ser preenchido e encaminhado à SEJUD o respectivo formulário de exclusão. Caso o magistrado já 
		tenha sido removido, o formulário de solicitação de exclusão poderá ser encaminhado 
		pelo Diretor de Secretaria." titulo="OBS" />
	</mod:entrevista>
	<mod:documento>
	<c:set var="apenasNome" value="Sim" scope="request" />
	<c:set var="tl" value="8pt" />
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
@page {
	margin-left: 3cm;
	margin-right: 2cm;
	margin-top: 1cm;
	margin-bottom: 2cm;
}
@body {
	margin-top: 6cm;
	margin-bottom: 0.5cm; 
}
@first-page-body {
	margin-top: 6cm;
	margin-bottom: 0.5cm; 
}
		</style>
		</head>
		<body>
		<!-- FOP -->
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina2.jsp" />
		</td></tr><tr><td></tr></td>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
				<br/><br/>
					<table width="100%">
						<tr>
							<td align="center"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">FORMUL&Aacute;RIO PARA ACESSO AO SERVI&Ccedil;O INFOJUD-E-CAC
<br/>SECRETARIA DA RECEITA FEDERAL
</td>
						</tr>
						<tr><td><p align="right"><b>Formulário N&ordm; ${doc.codigo}</b></p><br/>
						</td></tr>
					</table>
				</td>
			</tr>
		</table>
	
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda2.jsp" />
		FIM CABECALHO -->
		<br />
		
				<mod:letra tamanho="${tl}">



			<table bgcolor="#000000" border="1" cellpadding="5" width="450" align="center" cellspacing="0">
				<tr>
					<td bgcolor="#FFFFFF" colspan="2" align="left"><b> 1.1 - SOLICITAÇÃO DE
					CADASTRAMENTO DE MAGISTRADO </b>
					</td>
				</tr>

				<tr>
					<td bgcolor="#FFFFFF" colspan="2" align="left"><b>1.1 - IDENTIFICAÇÃO DO
					USUÁRIO </b></td>
				</tr>
				<tr>
					<td bgcolor="#FFFFFF" width="500" ><b>NOME COMPLETO</b><br />
					${doc.subscritor.descricao}</td>


					<td bgcolor="#FFFFFF" width="300"><b>CPF</b><br />
					${f:formatarCPF(doc.subscritor.cpfPessoa)}</td>
				</tr>
				<tr>
					<td bgcolor="#FFFFFF"><b>LOTA&Ccedil;&Atilde;O</b><br />
					${doc.subscritor.lotacao.descricao}</td>
					<td bgcolor="#FFFFFF"><b>E-MAIL INSTITUCIONAL</b><br />
					${email}@jfrj.jus.br</td>
				</tr>




			</table>
			<table bgcolor="#000000" border="1" cellpadding="5" width="450" align="center" cellspacing="0">

				<tr align="justify">
					<td bgcolor="#FFFFFF" colspan="2"><b><p align="center">TERMO DE RESPONSABILIDADE</p></b> <br />
					<br />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					Comprometo-me a manter sigilo sobre os dados cadastrais a que tenha acesso ou conhecimento, em razão do Convênio INFOJUD, celebrado entre a Secretaria de Receita Federal (SRF) e o Tribunal Regional Federal da 2ª Região, em 18/12/2006, visando simplificar e agilizar o atendimento de requisição de informação protegida por sigilo fiscal. Do mesmo modo, estou ciente do que preceitua o Decreto nº 4.553 de 27/12/2002 (salvaguarda de dados, informações, documentos e materiais sigilosos). Comprometo-me a utilizar os dados a que tiver acesso exclusivamente dentro das atribuições de minha responsabilidade.</b> <br />
					<br />
					
					
					
					

               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
               Declaro estar ciente da necessidade de manter arquivados os termos de responsabilidade dos servidores a serem por mim cadastrados para acessar o referido sistema, para controle dos acessos concedidos, bem como de excluir do cadastro desse sistema os servidores que vierem a ser removidos para outra unidade.
					 <br />
					<br />


					</td>
				</tr>


				<tr valign="top">
					<td bgcolor="#FFFFFF" width="400">LOCAL/DATA<br />${doc.dtExtenso}</td>
					<td bgcolor="#FFFFFF" align="center" width="500"><br />
					<br />
					____________________________________________ <br />
					Assinatura do Juiz Solicitante<br />
					${doc.subscritor.descricao}
					</td>
				</tr>
			</table>
	</mod:letra>
		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental2.jsp" />
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita2.jsp" />
		FIM RODAPE -->

		</body>
		</html>
	</mod:documento>
</mod:modelo>
