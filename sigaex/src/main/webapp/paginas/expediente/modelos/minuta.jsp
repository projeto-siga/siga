<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>

<!-- 
     Modelo : Contrato
     Data da Criacao : 07/02/2007
     Ultima alteracao : 02/03/2007 
-->
<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="Detalhes do Contrato" />
		<mod:grupo>
			<mod:monetario titulo="Processo Nº" var="numProcesso1" largura="6"
				maxcaracteres="5" verificaNum="sim" />
			<mod:selecao titulo="" var="mes" opcoes="JAN;FEV;MAR;ABR;MAIO;JUN;JUL;AGO;SET;OUT;NOV;DEZ" reler="nao" />
			<mod:monetario titulo="" var="numProcesso3" largura="6"
				maxcaracteres="4" verificaNum="sim" />
		</mod:grupo>
		<mod:grupo>
			<mod:selecao titulo="Forma" var="forma" opcoes="Minuta;Contrato;Aditivo"/>
		</mod:grupo>
		<mod:grupo>  
			<mod:selecao titulo="Tipo do Contrato" var="tipoContrato"
				opcoes="ATA de RP;Obras Serv_Eventuais;Outros" reler="sim" />
			<mod:monetario titulo="Número" var="numero" largura="6"
				maxcaracteres="4" verificaNum="sim" />
			<mod:monetario titulo="Ano" var="ano" largura="6" maxcaracteres="4"
				verificaNum="sim" />
		</mod:grupo>
		<c:if test="${tipoContrato !='Obras Serv_Eventuais'}">
			<mod:grupo>
				<mod:selecao titulo="Finalidade" var="finalidadeContrato"
					opcoes="Aquisição de;Fornecimento de;Fornecimento e Instalação de;Prestação de Serviços"
					reler="sim" />
			</mod:grupo>
		</c:if>
		<c:if test="${finalidadeContrato=='Prestação de Serviços'}">
			<mod:grupo>
				<mod:texto titulo="Nome da Empresa" var="variavel"
					maxcaracteres="50" largura="55" />
			</mod:grupo>
		</c:if>
		<c:if
			test="${tipoContrato=='Obras Serv_Eventuais'}">
			<mod:grupo>
				<mod:texto titulo="Objeto do Contrato" var="variavel2"
					maxcaracteres="35" largura="40" />
			</mod:grupo>
			<mod:grupo>
				<mod:memo titulo="Texto" var="texto" colunas="60" linhas="3" />
			</mod:grupo>
			<mod:grupo titulo="Dados do Juiz">
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
		</mod:grupo>

		<mod:grupo titulo="Dados da Empresa Contratada">
			<mod:texto titulo="Nome da Empresa" largura="60" maxcaracteres="40"
				var="nomeEmpresa" />
			<mod:grupo>
				<mod:texto titulo="Endereço" largura="70" maxcaracteres="50"
					var="endEmpresa" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="CGC/CNPJ" largura="15" maxcaracteres="13"
					var="cnpj" />
				<mod:texto titulo="Telefone" largura="13" maxcaracteres="12"
					var="tel" />
				<mod:texto titulo="Fax" largura="13" maxcaracteres="12" var="fax" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Nome do Representante SR(a)" largura="60"
					maxcaracteres="40" var="nomeRepresentante" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Documento Identidade" largura="12"
					maxcaracteres="9" var="identRepresentante" />
				<mod:texto titulo="Orgão Emissor" largura="5"
					var="orgEmissorRepresentante" />
				<mod:texto titulo="CPF" largura="15" maxcaracteres="11"
					var="cpfRepresentante" />
			</mod:grupo>
		</mod:grupo>
		</c:if>
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
                             <u><font size="2">Processo nº${numProcesso1}/${numProcesso2}/${numProcesso3}-EOF - Contrato nº ${numero}/${ano} Nome da Empresa ${nomeEmpresa} </font></u>                              
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
             <u><font size="2">Processo nº${numProcesso1}/${numProcesso2}/${numProcesso3}-EOF - Contrato nº ${numero}/${ano} Nome da Empresa ${nomeEmpresa}</font></u>                              
		   </td>
		</tr>		 
        </table>
		FIM CABECALHO -->
		<br>
				
		<p align="center"><b>TERMO DE CONTRATO N.º&nbsp;
		${termoContrato }/(${anoContrato }) </b></p>
		
		<p align="right"><b>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CONTRATO
		DE PRESTAÇÃO DE SERVIÇOS DE<br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ENGENHARIA
		PARA ${variavel2 }, QUE ENTRE<br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SI
		FAZEM A JUSTIÇA FEDERAL DE 1º GRAU NO RIO DE<br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;JANEIRO
		E A EMPRESA ${nomeEmpresa }</b></p>

		<p align="center"><b>PROCESSO Nº &nbsp; ${numProcesso1 }/${numProcesso2 }/${numProcesso3 } -
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
