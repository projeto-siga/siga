<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>

<!-- 
     Modelo : Contrato
     Data da Criacao : 07/02/2007
     Ultima alteracao : 07/03/2007 
-->
<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="Detalhes do contrato" />
		
		<mod:grupo>
			<mod:monetario titulo="Nº processo" var="numeroProcesso" largura="15"
				maxcaracteres="10" verificaNum="sim" />			
			<mod:monetario titulo="Ano" var="ano" largura="6" maxcaracteres="4"
				verificaNum="sim" />
		</mod:grupo>
		<mod:grupo>
			<mod:selecao titulo="Forma" var="forma"
				opcoes="MINUTA;CONTRATO;ADITIVO" reler="sim"/>
			<c:if test="${forma == 'CONTRATO'}">
				
				<mod:monetario titulo="Nº Folhas do auto" largura="4" maxcaracteres="4" var="folhas" verificaNum="sim" />
			</c:if>
			<c:if test="${forma == 'ADITIVO' }">
				<mod:selecao titulo="Nº Contrato Aditivo" var="numeroContratoAditivo" opcoes="01;02;03;04;05"/>
				<mod:monetario titulo="Numero do Contrato de Origem" var="numeroContratoOrigem" largura="15"
				maxcaracteres="10" verificaNum="sim" />
			</c:if>	
			<c:if test="${forma == 'MINUTA'}">
				
				<mod:monetario titulo="Nº Folhas do auto" largura="4" maxcaracteres="4" var="folhas" verificaNum="sim" />
			</c:if>

		</mod:grupo>
		<mod:grupo>
			<mod:selecao titulo="Tipo de Contrato" var="tipoContrato"
				opcoes="Aquisição em Geral;Prestação Serviços Continuados;Material Consumo Entrega Parcelada;Obras Serviços Eventuais;"
				reler="nao" />
					
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Objeto do Contrato" var="objeto" largura="70" />
		</mod:grupo>
		<mod:grupo>
			<mod:memo titulo="Texto Complementar..." var="texto" colunas="60"
				linhas="3" />
		</mod:grupo>
		
		<mod:grupo titulo="Dados do Juiz">
			
			<mod:grupo>				
				<mod:monetario titulo="Documento Identidade" largura="12"
					maxcaracteres="9" var="identidadeJuiz" verificaNum="sim" />
					${requestScope[juiz_pessoaSel.descricao]}
				<mod:texto titulo="Orgão Emissor" largura="10" var="orgaoEmissorJuiz" />
				<mod:monetario titulo="CPF" largura="15" maxcaracteres="11"
					var="cpfJuiz" verificaNum="sim" />
			</mod:grupo>
		</mod:grupo>
		<mod:grupo titulo="Dados da Empresa Contratada">
			<mod:texto titulo="Nome da Empresa" largura="60" maxcaracteres="40"
				var="nomeEmpresa" />
			<mod:grupo>
				<mod:texto titulo="Endereço" largura="70" maxcaracteres="50"
					var="enderecoEmpresa" />
			</mod:grupo>
			<mod:grupo>
				<mod:monetario titulo="CGC/CNPJ" largura="15" maxcaracteres="13"
					var="cnpj" verificaNum="sim" />
				<mod:monetario verificaNum="sim" titulo="Telefone" largura="13" maxcaracteres="12"
					var="tel" />
				<mod:monetario verificaNum="sim" titulo="Fax" largura="13" maxcaracteres="12" var="fax" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Nome do Representante Sr(a)" largura="60"
					maxcaracteres="40" var="nomeRepresentante" />
			</mod:grupo>
			<mod:grupo>
				<mod:monetario titulo="Documento Identidade" largura="12"
					maxcaracteres="9" var="identidadeRepresentante" verificaNum="sim" />
				<mod:texto titulo="Orgão Emissor" largura="10"
					var="orgaoEmissorRepresentante" />
				<mod:monetario titulo="CPF" largura="15" maxcaracteres="11"
					var="cpfRepresentante" verificaNum="sim" />
			</mod:grupo>
		</mod:grupo>
		<mod:grupo titulo="Texto a ser inserido no corpo do contrato">
			<mod:grupo>
				<mod:editor titulo="" var="texto_contrato" />
			</mod:grupo>
		</mod:grupo>
		<mod:selecao
			titulo="Tamanho da letra"
			var="tamanhoLetra" opcoes="Normal;Pequeno;Grande"/>
	</mod:entrevista>

	<mod:documento>
		<c:if test="${tamanhoLetra=='Normal'}"><c:set var="tl" value="11pt"/></c:if>
		<c:if test="${tamanhoLetra=='Pequeno'}"><c:set var="tl" value="9pt"/></c:if>
		<c:if test="${tamanhoLetra=='Grande'}"><c:set var="tl" value="13pt"/></c:if>
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
			
		</tr>
		<tr> 
			<td width="100%">
				<table width="100%">				
					<tr>
					   <td><p align="center">
				        	<u><font size="2">Processo nº of ${numeroProcesso}/${ano} - EOF - ${forma} nº
				        	 
				        	<c:if test="${forma == 'CONTRATO' }">
			 					${doc.codigo}/${ano}&nbsp;
							</c:if>
							<c:if test="${forma == 'ADITIVO' }">
								${numeroContratoAditivo}/${numeroContratoOrigem}/${ano}&nbsp;
							</c:if>
							<c:if test="${forma == 'MINUTA' }">
								${doc.codigo}/${ano}&nbsp;
							</c:if>
					        	- 
							<c:if test="${tipoContrato == 'Prestação Serviços Continuados'}"><c:set var="variavel" value="${objeto}" /></c:if>
							<c:if test="${tipoContrato == 'Obras Serviços Eventuais'}"><c:set var="variavel" value="${nomeEmpresa}" /></c:if>
							<c:if test="${tipoContrato == 'Material Consumo Entrega Parcelada'}">Fornecimento de <c:set var="variavel" value="${objeto}" /></c:if>
							<c:if test="${tipoContrato == 'Aquisição em Geral'}">Aquisição de <c:set var="variavel" value="${objeto}" /></c:if>
								${variavel}</font></u></p>                              
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
		   <td><p align="center">
             <u><font size="2">Processo nº ${numeroProcesso}/${ano} - EOF - ${forma} nº 
         <c:if test="${forma == 'CONTRATO' }">
			 ${doc.codigo}/${ano}&nbsp;
		</c:if>
		<c:if test="${forma == 'ADITIVO' }">
			${numeroContratoAditivo}/${numeroContratoOrigem}/${ano}&nbsp;
		</c:if>
		<c:if test="${forma == 'MINUTA' }">
			${doc.codigo}/${ano}&nbsp;
		</c:if>
		 - 
		<c:if test="${tipoContrato == 'Prestação Serviços Continuados'}"><c:set var="variavel" value="${objeto}" /></c:if>
		<c:if test="${tipoContrato == 'Obras Serviços Eventuais'}"><c:set var="variavel" value="${nomeEmpresa}" /></c:if>
		<c:if test="${tipoContrato == 'Material Consumo Entrega Parcelada'}">Fornecimento de <c:set var="variavel" value="${objeto}" /></c:if>
		<c:if test="${tipoContrato == 'Aquisição em Geral'}">Aquisição de <c:set var="variavel" value="${objeto}" /></c:if>
		${variavel}</font></u></p>              
		   <br><br>
		   </td>
		</tr>		 
        </table>
		FIM CABECALHO -->
		<mod:letra tamanho="${tl}">
		<br>
		<c:if test="${forma == 'ADITIVO' }">
			<p align="center"><b>ADITIVO DE CONTRATO Nº: ${numeroContratoAditivo}/${numeroContratoAditivo}/${ano}&nbsp;
		 	</b></p>
		</c:if>
		<c:if test="${forma == 'CONTRATO' }">
			<p align="center"><b>TERMO DE CONTRATO Nº: ${doc.codigo}/${ano}&nbsp;
		 	</b></p>
		</c:if>
		<c:if test="${forma == 'MINUTA' }">
			<p align="center"><b>MINUTA DE CONTRATO Nº: ${doc.codigo}/${ano}&nbsp;
		 	</b></p>
		</c:if>
		
		<p style="MARGIN-LEFT: 7cm" align="justify"><b>CONTRATO DE<c:if test="${tipoContrato == ('Prestação Serviços Continuados' )}">
		PRESTAÇÃO DE SERVIÇOS DE ENGENHARIA PARA 
		</c:if>
		<c:if test="${tipoContrato == ('Obras Serviços Eventuais' )}">
		PRESTAÇÃO DE SERVIÇOS DE ENGENHARIA PARA
		</c:if>
		<c:if test="${tipoContrato == ('Aquisição em Geral')}">
		FORNECIMENTO DE
		</c:if>
		<c:if test="${tipoContrato == ('Material Consumo Entrega Parcelada')}">
		FORNECIMENTO DE
		</c:if>
		${objeto }, QUE ENTRE SI FAZEM A JUSTIÇA
		FEDERAL DE 1º GRAU NO RIO DE JANEIRO E A EMPRESA ${nomeEmpresa }</b></p>

		<p align="center"><b>PROCESSO Nº OF ${numeroProcesso}/${ano}</b></p>

		<p style="TEXT-INDENT: 2cm" align="justify">A justiça Federal de
		1&deg; Grau no Rio de Janeiro, com sede na Av.Rio Branco, 243 - Anexo
		I - 14&deg; andar, Centro/RJ, inscrita no C.N.P.J. sob o n&deg;
		05.424.540./0001-16, neste ato representada pelo Dr. <b>${doc.subscritor.descricao}</b>,
		Juiz Federal - Diretor do Foro, Identidade n&deg; <b>${identidadeJuiz }</b>
		- <b>${orgaoEmissorJuiz }</b>, CPF: <b>${cpfJuiz }</b> doravante
		denominada CONTRATANTE, e a empresa <b>${nomeEmpresa }</b>,
		estabelecida na <b>${enderecoEmpresa }</b>, inscrita no C.N.P.J sob o nº <b>${cnpj
		}</b>, TEL: <b>${tel }</b>, FAX: <b>${fax }</b>, representada neste ato
		pelo Sr.(a) <b>${nomeRepresentante }</b>, Identidade nº <b>${identidadeRepresentante}</b>
		- <b>${orgaoEmissorRepresentante }</b>, CPF: <b>${cpfRepresentante }</b>,
		doravante denominada CONTRATADA, tendo em vista o decidido no Processo
		Administrativo nº <b>${numeroProcesso}</b>/<b>${ano}</b>
		- EOF, por despacho do Exmº Sr. <b>${doc.subscritor.descricao}</b> Juiz Federal -
		Diretor do Foro<c:if test="${not empty(folhas)}"> (às Fls. <b>${folhas }</b> dos autos)</c:if> firmam o
		presente contrato, ${texto }</p>
		<br>
		<span style="font-size:${tl};">
		${texto_contrato}
		</span>
		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita.jsp" />
		FIM RODAPE -->
		</mod:letra>
		</body>
		</html>
	</mod:documento>
</mod:modelo>
