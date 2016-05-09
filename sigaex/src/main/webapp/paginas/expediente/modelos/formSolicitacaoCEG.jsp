<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- este modelo trata de
FORMULARIO BANCO DE DADOS DA CEG-->

<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="Tipo de Formulário">	
			<mod:radio titulo="Inclusão" var="tipoFormulario" valor="1" marcado="sim" reler="sim" />
			<mod:radio titulo="Exclusão" var="tipoFormulario" valor="2" reler="sim" gerarHidden="Não"/>
		</mod:grupo>
		
		<c:set var="valorTipoDeForm" value="${tipoFormulario}" />
		<c:if test="${empty valorTipoDeForm}">
			<c:set var="valorTipoDeForm" value="${param['tipoFormulario']}" />
		</c:if>
		
		<mod:grupo titulo="Identificação do Usuário">
			<mod:pessoa titulo="Matrícula do Usuário" var="usuario"/> 
		</mod:grupo>
		
		<mod:grupo>	
			<mod:texto titulo="E-mail Institucional" var="email" largura="30"/>
		</mod:grupo>
		
		<mod:grupo>	
			<mod:selecao titulo="Tipo de Usuário" opcoes="Magistrado;Servidor" var="tipoUsuario" reler="sim"/>
		</mod:grupo>
		
		<c:if test="${valorTipoDeForm == 1 && tipoUsuario == 'Servidor'}">
			<mod:grupo>
				<mod:pessoa titulo="Nome do Juiz autorizador" var="juiz"/> 
			</mod:grupo>
		</c:if>
		<br/>
		<c:if test="${valorTipoDeForm == 1}">
			<c:if test="${tipoUsuario == 'Magistrado'}">
				<b>Para indicação de novo usuário, no caso de remoção do magistrado para órgão externo à SJRJ, deverá ser 
				preenchido e encaminhado à SEJUD, pelo diretor de secretaria, o respectivo formulário de exclusão. </b>
			</c:if>
			<c:if test="${tipoUsuario == 'Servidor'}">
				<b>Para indicação de novo usuário, no caso de dispensa do cargo ou remoção do Diretor de Secretaria e/ou de 
				seu substituto, deverá ser preenchido e encaminhado à SEJUD, pelo diretor de secretaria, 
				o respectivo formulário de exclusão. </b>
			</c:if>
	</c:if>
	
	</mod:entrevista>
	
	<mod:documento>
	<c:set var="tl" value="10pt" />
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
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF">
			<tr>
				<td>
					<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr>
							<td align="center">
								<p style="font-family:Arial;font-size:11pt;font-weight:bold;"></p>
							</td>
						</tr>
						<tr>
							<td align="right">
								<p><b>Formulário N&ordm; ${doc.codigo}</b></p><br/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->

		<br />
	<mod:letra tamanho="${tl}">	
		<table border="1" cellpadding="5" width="900" align="center">
  <tr>
    <td align="center"><b> 
    <c:if test="${tipoFormulario == 1}">
    FORMULÁRIO DE INCLUSÃO DE USUÁRIO NO BANCO DE DADOS DA CEG
    </c:if>
    <c:if test="${tipoFormulario == 2}">
    FORMULÁRIO DE EXCLUSÃO DE USUÁRIO NO BANCO DE DADOS DA CEG
    </c:if>
 </b></td>
  </tr>
  </table>	
  
  <br/><br/>
  
		<table border="1" cellpadding="5" width="900" align="center">
	<tr>
    <td colspan="2" align="left">
    <b>
    1 - SOLICITAÇÃO DE 
      <c:if test="${tipoFormulario == 1}">
    CADASTRAMENTO DE 
     </c:if>
    <c:if test="${tipoFormulario == 2}">
    EXCLUSÃO DE 
     </c:if>
     
    <c:if test="${tipoUsuario == 'Magistrado'}">
    MAGISTRADO 
    </c:if>
    <c:if test="${tipoUsuario == 'Servidor'}">
    DIRETOR DE SECRETARIA/SUBSTITUTO EVENTUAL
    </c:if>
    </b></td>
  </tr>
  <tr>
    <td align="left" colspan="2"><b>1.1 - IDENTIFICAÇÃO DO USUÁRIO </b></td>
  </tr>
  <tr>
    <td width="900" colspan="2" align="left"><b>NOME COMPLETO:</b> ${f:pessoa(requestScope['usuario_pessoaSel.id']).nomePessoa}&nbsp;&nbsp;<b>SIGLA:</b> ${f:pessoa(requestScope['usuario_pessoaSel.id']).siglaPessoa} </td>
  </tr>
  <tr>
    <td width="300"><b>MATR&Iacute;CULA:</b> RJ${f:pessoa(requestScope['usuario_pessoaSel.id']).matricula}</td>
    <td width="500"><b>CARGO/FUN&Ccedil;&Atilde;O:</b><br/> ${f:pessoa(requestScope['usuario_pessoaSel.id']).cargo.nomeCargo}-${f:pessoa(requestScope['usuario_pessoaSel.id']).funcaoConfianca.nomeFuncao}</td>
  </tr>
  <tr>
    <td><b>LOTA&Ccedil;&Atilde;O:</b><br/> ${f:pessoa(requestScope['usuario_pessoaSel.id']).lotacao.descricao}</td>
    <td><b>E-MAIL INSTITUCIONAL:</b><br/> ${email}</td>
  </tr>
  
  <c:if test="${tipoFormulario == 1}">
  <tr align="justify">
    <td colspan="2"><b>TERMO DE RESPONSABILIDADE</b>
    <br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	Comprometo-me a manter sigilo sobre os dados cadastrais a que tenha acesso ou conhecimento, por meio de canal de 
	consulta de dados de clientes, via internet, em razão do Convênio de Cooperação Técnica entre a Justiça Federal de 
	Primeiro Grau no Rio de Janeiro e a Companhia de Gás do Rio de Janeiro (CEG), publicado no DOU N. 185 de 26/9/2006, e a 
	utilizá-los exclusivamente para instrução das ações judiciais sob minha responsabilidade. Do mesmo modo, estou ciente 
	do que preceitua o Decreto N. 4.553 de 27/12/2002 (salvaguarda de dados, informações, documentos e materiais sigilosos).</td>
  </tr>
  </c:if>
  
  			<tr><td width="400">
			${doc.dtExtenso}
			</td> 
			<td align="center" width="500"><br/><br/>______________________
			<br>Assinatura do Solicitante</td>
			</tr>
</table>
		

		<br/><br/>
			<table border="1" cellpadding="2" width="900" align="center">
			<tr>
				<td colspan="2" align="center">
				<b>2. CAMPO RESTRITO À SEJUD</b>
				</td>
			</tr>
			<tr><td width="400" align="left">
			CHAMADO Nº: 
			</td> 
			<td width="500" align="left">
				<c:if test="${tipoFormulario == 1}">
					ACESSO LIBERADO EM: 
				</c:if>		
				<c:if test="${tipoFormulario == 2}">
					EXCLUÍDO EM: 
				</c:if>		
			</td>
			</tr>
			</table>
	
		
		
		<c:if test="${tipoFormulario == 1 && tipoUsuario == 'Servidor'}">
		<br/><br/>
			<table border="1" cellpadding="2" width="900" align="center">
			<tr>
				<td colspan="2" align="center">
				<b>3- AUTORIZAÇÃO PARA CADASTRAMENTO</b>
				</td>
			</tr>
			<tr><td width="400">
			<b>Nome do Juiz Autorizador: </b><br/>${f:pessoa(requestScope['juiz_pessoaSel.id']).nomePessoa}
			</td> 
			<td width="500" align="center"><br/><br/>______________________
			<br>Assinatura do Juiz Federal Autorizador</td>
			</tr>
			</table>
		</c:if>		
</mod:letra>
		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita.jsp" />
		FIM RODAPE -->

		</body>
		</html>
	</mod:documento>
</mod:modelo>
