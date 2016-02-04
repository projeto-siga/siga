<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<!-- 
     Modelo : Contrato
     Data da Criacao : 07/02/2007
     Ultima alteracao : 06/03/2007 
-->
<mod:modelo>
	<mod:entrevista>
	<mod:grupo titulo="Detalhes da Ata" />
		<mod:grupo>
			<mod:monetario titulo="Numero do Processo" var="numeroProcesso" largura="15"
				maxcaracteres="10" verificaNum="sim" />
			<mod:monetario titulo="Ano" var="ano" largura="6"
				maxcaracteres="4" verificaNum="sim" />
		</mod:grupo>
		
			<mod:grupo>
			
			<mod:selecao titulo="Forma" var="forma" opcoes="MINUTA;ATA;ADITIVO" reler="sim"/>
			
			<c:if test="${forma == 'ADITIVO'}">
				<mod:selecao titulo="Numero do Aditivo" var="numeroAtaAditivo" opcoes="01;02;03;04;05"/>
				<mod:monetario titulo="Numero da Ata de Origem" var="numeroAtaOrigem" largura="15"
				maxcaracteres="10" verificaNum="sim" />
			</c:if>
			
				
				
			
			</mod:grupo>
			<mod:grupo>
				<mod:selecao titulo="Tipo de Ata" var="tipoAta" opcoes="Aquisição com Contrato;Aquisição sem Contrato;Fornecimento e Instalação" reler="nao" />
			</mod:grupo>
			<mod:grupo>
				<mod:memo titulo="Texto Complementar..." var="texto" colunas="60" linhas="3" />
			</mod:grupo>
			
			<!-- 
			  <c:choose>
			<c:when test="${tipoContrato == 'Material Consumo Entrega Parcelada' || 'Aquisição em Geral'}">
				<mod:oculto var="variavel" valor="FORNECIMENTO DE" />
			</c:when>
				<c:otherwise>
					<mod:oculto var="variavel" valor="PRESTAÇÃO DE SERVIÇOS DE" />
				</c:otherwise>
			</c:choose>
			-->
		<mod:grupo titulo="Dados do Juiz">
			
			<mod:grupo>
				<mod:monetario verificaNum="sim" titulo="Documento Identidade" largura="12"
					maxcaracteres="9" var="identJuiz" />
				<mod:texto titulo="Orgão Emissor" largura="5" var="orgaoEmissorJuiz" />
				<mod:monetario verificaNum="sim" titulo="CPF" largura="15" maxcaracteres="11"
					var="cpfJuiz" />
				
			</mod:grupo>
		</mod:grupo>
			
			<mod:grupo titulo="Pregão">
				<mod:monetario titulo="Nº" var="numeroPregao"
					maxcaracteres="6" largura="10" verificaNum="sim" />
			
				<mod:monetario titulo="Ano" var="anoPregao" largura="6"
					maxcaracteres="4" verificaNum="sim" />
			</mod:grupo>			
			<mod:grupo>
				<mod:texto titulo="Objeto do Contrato" var="objeto"
					maxcaracteres="35" largura="70" />
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
			<td width="100%">
				<table width="100%">				
					<tr>
					   <td>
				        	<u><font size="2">Processo nº of ${ano}/${numeroProcesso}-EOF - ${forma} nº
								<c:if test="${forma == 'ATA' }">
									${doc.codigo}/${ano}
								</c:if>
								<c:if test="${forma == 'ADITIVO' }">
									${numeroAtaAditivo}/${numeroAtaOrigem}/${ano}
								</c:if>
								<c:if test="${forma == 'MINUTA' }">
									${doc.codigo}/${ano}
								</c:if>
								-  
								<c:if test="${tipoAta == 'Aquisição sem Contrato' }">
									AQUISIÇÂO DE
								</c:if>
								<c:if test="${tipoAta == 'Aquisição com Contrato' }">
									FORNECIMENTO DE	
								</c:if>
								<c:if test="${tipoAta == 'Fornecimento e Instalação' }">
									FORNECIMENTO E INSTALAÇÃO DE
								</c:if>: ${objeto}</font></u>                              
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
             <u><font size="2">Processo nº of ${ano}/${numeroProcesso} -EOF - ${forma} nº 
             <c:if test="${forma == 'ATA' }">
			${doc.codigo}/${ano}
			</c:if>
			<c:if test="${forma == 'ADITIVO' }">
			${numeroAtaAditivo}/${numeroAtaOrigem}/${ano}
			</c:if>
			<c:if test="${forma == 'MINUTA' }">
			${doc.codigo}/${ano}
			</c:if>
             -
            <c:if test="${tipoAta == 'Aquisição sem Contrato'}">
				AQUISIÇÂO DE
			</c:if>
			<c:if test="${tipoAta == 'Aquisição com Contrato'}">
				FORNECIMENTO DE	
			</c:if>
			<c:if test="${tipoAta == 'Fornecimento e Instalação'}">
				FORNECIMENTO E INSTALAÇÃO DE
			</c:if>: ${objeto}</font></u>                              
		   <br><br>
		   </td>
		</tr>		 
        </table>
		FIM CABECALHO -->
		<mod:letra tamanho="${tl}">
		<br>
		<c:if test="${forma == 'ATA' }">
			<p align="center"><b> ATA DE REGISTRO DE PREÇO N.º&nbsp; ${doc.codigo}/${ano} </b></p>
		</c:if>
		<c:if test="${forma == 'ADITIVO' }">
			<p align="center"><b>ADITIVO DE ATA DE REGISTRO DE PREÇO N.º&nbsp; ${numeroAtaAditivo}/${numeroAtaOrigem}/${ano} </b></p>
		</c:if>
		<c:if test="${forma == 'MINUTA' }">
			<p align="center"><b>MINUTA DE ATA DE REGISTRO DE PREÇO N.º&nbsp; ${doc.codigo}/${ano} </b></p>
		</c:if>
		<p align="center"><b>PROCESSO Nº OF&nbsp; ${ano}/${numeroProcesso} -
		EOF</b></p>

		<p style="TEXT-INDENT: 2cm" align="justify">A justiça Federal de
		1&deg; Grau no Rio de Janeiro, com sede na Av.Rio Branco, 243 - Anexo
		I - 14&deg; andar, Centro/RJ, inscrita no C.N.P.J. sob o n&deg;
		05.424.540./0001-16, neste ato representada pelo Dr. <b>${doc.subscritor.descricao}</b>,
		 Juiz Federal - Diretor do Foro, Identidade n&deg; <b>${identJuiz
		}</b> - <b>${orgaoEmissorJuiz }</b>, CPF: <b>${cpfJuiz }</b> doravante
		denominada JUSTIÇA FEDERAL, resolve, em face das propostas apresentadas 
		no <b>Pregão nº ${numeroPregao}/${anoPregao}, REGISTRAR O PREÇO<b> da 
		empresa classificada em primeiro lugar para o objeto da licitação e 
		igualmente daquelas que manifestaram interesse em se manifestar tambem pelo menor preço
		doravante denominadas fornecedoras, ${texto }</p>
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
