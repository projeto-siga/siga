<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>

<!-- 
     Modelo : Contrato
     Data da Criacao : 07/02/2007
     Ultima alteracao : 01/03/2007 
-->
<mod:modelo>
	<mod:entrevista>
	<mod:grupo titulo="Detalhes do contrato" />
		<mod:grupo>
			<mod:monetario titulo="Processo Nº" var="numMinuta" largura="6"
				maxcaracteres="5" verificaNum="sim" />
			<mod:monetario titulo="Mês" var="mesMinuta" largura="6"
				maxcaracteres="5" verificaNum="sim" />
			<mod:monetario titulo="Ano " var="anoMinuta" largura="6"
				maxcaracteres="4" verificaNum="sim" />
		</mod:grupo>
				
		<mod:grupo>	
			<mod:grupo>
				<mod:numero titulo="Nº Termo de Contrato" var="termoContrato" largura="6"  maxcaracteres="4" ></mod:numero>
				<mod:texto titulo="Ano" var="anoContrato" maxcaracteres="4" largura="6" />
			</mod:grupo>
			<mod:grupo titulo="Detalhes da empresa">
					<mod:grupo>
						<mod:texto titulo="Nome da Empresa" var="nomeEmpresa" maxcaracteres="4" largura="6" />
					</mod:grupo>
					<mod:grupo>
						<mod:texto titulo="Endereço da Empresa" var="endEmpresa" maxcaracteres="4" largura="6" />
					</mod:grupo>
					
					<mod:grupo>
						<mod:monetario titulo="CNPJ" var="cnpj" largura="6" verificaNum="sim" />
					</mod:grupo>
					<mod:grupo titulo="Empresa contratada">
						<mod:texto titulo="Nome representante" var="nomeRepresentante" maxcaracteres="4" largura="6" />
					
						<mod:monetario titulo="Identidade representante" var="identRepresentante" largura="6" verificaNum="sim" />
						<mod:texto titulo="Orgão emissor representante" var="orgEmissorRepresentante" maxcaracteres="4" largura="6" />
						<mod:texto titulo="Endereço" var="cpfRepresentante" maxcaracteres="4" largura="6" />
					</mod:grupo>
				</mod:grupo>
		</mod:grupo>
		 
			<mod:grupo>
				<mod:texto titulo="Objeto do Contrato" var="variavel2"
					maxcaracteres="35" largura="40" />
			</mod:grupo>
			<mod:grupo titulo="Dados do Juiz">
			</mod:grupo>
			<mod:texto titulo="Juiz(a) Federal (Diretor(a) do Foro)" largura="60"
				maxcaracteres="40" var="nomeJuiz" />
			<mod:grupo>
				<mod:texto titulo="Documento Identidade" largura="12"
					maxcaracteres="9" var="identJuiz" />
				<mod:texto titulo="Orgão Emissor" largura="5" var="orgEmissorJuiz" />
				<mod:texto titulo="CPF" largura="15" maxcaracteres="11"
					var="cpfJuiz" />
				<mod:texto titulo="Nº Folhas do auto" largura="4" var="folhas" />
			</mod:grupo>
		
		<%--
			<c:choose>
				<c:when test="${finalidadeContrato!='Prestação de Serviços'}">
					<mod:oculto var="variavel"
						valor="${finalidadeContrato} ${variavel2}" />
				</c:when>
				<c:otherwise>
					<mod:oculto var="variavel" valor="${variavel2}" />
				</c:otherwise>
			</c:choose>
			--%>
		<mod:grupo titulo="Texto a ser inserido no corpo do contrato">
			<mod:grupo>
				<mod:editor titulo="" var="texto_contrato" />
			</mod:grupo>
		</mod:grupo>
		
		
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
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
			<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp"/>
		</td></tr>
		<tr> 
			<td width="100%">
				<table width="100%">				
					<tr>
					   <td>
                             <u><font size="2">Processo nº${numMinuta}/${mesMinuta}/${anoMinuta}-EOF - Contrato nº ${numero}/${ano} Nome da Empresa ${nomeEmpresa} </font></u>                              
					   </td>
					</tr>
				</table>
			</td>
		</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF">
		 <tr>
		   <td>
		     <c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		   </td>
		 </tr>
		 <tr>
		   <td>
             <u><font size="2">Processo nº${numMinuta}/${mesMinuta}/${anoMinuta}-EOF - ${tipoMinuta} nº ${numero}/${ano} Nome da Empresa ${nomeEmpresa}</font></u>                              
		   </td>
		</tr>		 
        </table>
		FIM CABECALHO -->
		<br>
		
			
		<p align="center"><b>Contrato de Prestação de Serviços de Engenharia    N.º&nbsp;
		${numMinuta}/(${anoMinuta }) </b></p>
		
		<p align="right"><b>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CONTRATO
		DE PRESTAÇÃO DE SERVIÇOS<br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;DE ENGENHARIA
		PARA ${variavel2 }, QUE ENTRE<br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SI
		FAZEM A JUSTIÇA FEDERAL DE 1º GRAU<br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;NO RIO DE JANEIRO
		E A EMPRESA ${nomeEmpresa }</b></p>

		<p align="center"><b>PROCESSO Nº &nbsp; ${numMinuta}/${mesMinuta}/${anoMinuta} -
		EOF</b></p>

		<p style="TEXT-INDENT: 2cm" align="justify">A justiça Federal de
		1&deg; Grau no Rio de Janeiro, com sede na Av.Rio Branco, 243 - Anexo
		I - 14&deg; andar, Centro/RJ, inscrita no C.N.P.J. sob o n&deg;
		05.424.540./0001-16, neste ato representada pelo Dr. <b>${nomeJuiz
		}</b>, Juiz Federal - Diretor do Foro, Identidade n&deg; <b>${identJuiz
		}</b> - <b>${orgEmissorJuiz }</b>, CPF: <b>${cpfJuiz }</b> doravante
		denominada CONTRATANTE, e a empresa <b>${nomeEmpresa }</b>,
		estabelecida na <b>${endEmpresa }</b>, inscrita no C.N.P.J sob o nº <b>${cnpj
		}</b>, TEL: <b>${tel }</b>, FAX: <b>${fax }<b>, representada neste
		ato pelo Sr.(a) <b>${nomeRepresentante }<b>, Identidade nº <b>${identRepresentante}</b>
		- <b>${orgEmissorRepresentante }</b>, CPF: <b>${cpfRepresentante }</b>,
		doravante denominada CONTRATADA, tendo em vista o decidido no Processo
		Administrativo nº <b>${numProcesso1 }</b>/<b>${numProcesso2 }</b>/<b>${numProcesso3 }</b> - EOF, por
		despacho do Exmº Sr. <b>${nomeJuiz }</b> Juiz Federal - Diretor do
		Foro, às Fls. <b>${folhas }</b> dos autos, firmam o presente contrato,
		nos termos e sujeitas as partes às normas da Lei nº 8.666/93 e às
		cláusulas contratuais a seguir:</p>
		<br>
		${texto_contrato}
						
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
