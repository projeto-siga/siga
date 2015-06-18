<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="esconderTexto" value="sim" scope="request" />

<mod:modelo>
	<mod:entrevista>
			<br/>
				<span style="color:red"> <b>PREENCHER OBRIGATORIAMENTE O CAMPO DESCRIÇÃO COM NOME COMPLETO E ASSUNTO</b></span><br>
	        	<span style="color:red"> <b>ESTE DOCUMENTO DEVERÁ SER ENVIADO À SRH</b></span>	
			<br><br>
			<mod:grupo titulo=" ">	
		        <mod:grupo>
				    <mod:selecao var="ilustrissimo"
				    titulo="VOCATIVO"
			    	opcoes="ILUSTRÍSSIMA SENHORA DIRETORA;ILUSTRÍSSIMO SENHOR DIRETOR"		
			    	reler="sim" />
		        </mod:grupo>
		    </mod:grupo>
		    <br>	
			<mod:grupo titulo=""> 
		        	<mod:texto titulo="RAMAL DO REQUERENTE" var="ramal"/></mod> <br><br>
		        <mod:selecao titulo="Acerto Gramatical da Lotação" var="acgr"  opcoes="no;na" reler="sim" />
			</mod:grupo>
			<br/>
			<mod:grupo titulo=""> 
		        	<mod:texto titulo="MOTIVO DA SOLICITAÇÃO" var="motiv" largura="60"/></mod> <br/>
			</mod:grupo>
			<br/>
		    <mod:grupo titulo="Documentos em anexo, nos termos da IN-23-08:"> 
		        <mod:radio titulo="Certidão de Casamento" var="periodo" marcado="Sim" valor="1" reler="sim" />
		        <mod:radio titulo="Certidão de averbação de separação ou divórcio" var="periodo" valor="2" reler="sim" />
	            <mod:radio titulo="Publicação em jornal de grande circulação e cópia do registro de ocorrência policial (caso de extravio, perda, furto ou roubo)" var="periodo" valor="3" reler="sim" />
		    </mod:grupo>
		    	    
		    <mod:radio titulo="<b>01(uma) foto 3x4 (obrigatório)</b>" marcado="Sim" reler="nao" />
		    							
		    <br><br> 
		    					  
	</mod:entrevista>
<mod:documento>	
		
<head>
		<style type="text/css">
@page {
	margin-left: 1cm;
	margin-right: 1cm;
	margin-top: 1cm;
	margin-bottom: 1cm;
	  }
      
</style>
		</head>
		<body>

		<c:if test="${empty tl}">
			<c:set var="tl" value="7pt"></c:set>
		</c:if>
			
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#ffffff"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
				<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr><br><br>
						<td align="center"><p style="font-family:Arial;font-weight:bold;font-size:11pt;">SOLICITAÇÃO DE 2ª VIA DE CÉDULA DE IDENTIDADE</p></td>
						</tr>
					</table>
				</td>
			</tr>
			</table>
		FIM PRIMEIRO CABECALHO -->
       
		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->
		<c:set var="lotc" value="lotado"></c:set>
		<c:if test="${doc.subscritor.sexo == 'F'}">
			<c:set var="lotc" value="lotada"></c:set>
		</c:if>
		 <c:set var="opt" value="${f:classNivPadr(doc.subscritor.padraoReferencia)}"/>
            <br>
			<p style="text-align: justify;font-size:10pt"> <center><b> ${ilustrissimo} DA SECRETARIA DE RECURSOS HUMANOS</b></center></p>
			
			<p style="font-family:Arial;font-size:10pt">	
			${doc.subscritor.descricao}, matrícula ${doc.subscritor.matricula}, ${doc.subscritor.cargo.nomeCargo}, ${opt},
			do Quadro de Pessoal do Tribunal Regional Federal da 2ª Região, ${lotc} ${acgr} ${doc.subscritor.lotacao.descricao}, 
			ramal ${ramal}, vem requerer a 2ª via da <b>Carteira Funcional</b> em virtude de ${motiv}.<br/>
						
			
			<br />
			
			<br /><br />
			 
			Seguem anexos os seguintes documentos : <br><br>
			
			<c:if test="${ periodo == 1}">&nbsp;[X] Certidão de Casamento.<br></c:if>
			
			<c:if test="${ periodo == 2}">&nbsp;[X] Certidão de averbação de separação ou divórcio.<br></c:if>
			
			<c:if test="${ periodo == 3}">&nbsp;[X] Publicação em jornal de grande circulação e cópia do registro de ocorrência policial.<br></c:if>
			&nbsp;[X] 01(uma) foto 3x4.
			    <br>	
			</p>
			<br/><br/>	
			 
			Termos em que <br/>
			Espera deferimento.
			
	
				
		
		<!-- INICIO FECHO -->
		<p align="center">${doc.dtExtenso}</p>
		<!-- FIM FECHO -->
				
		
			
		<!-- INICIO ASSINATURA -->
		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
		<!-- FIM ASSINATURA -->
				
		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita.jsp" />
		FIM RODAPE -->
		
		</body>
	</mod:documento>
</mod:modelo>