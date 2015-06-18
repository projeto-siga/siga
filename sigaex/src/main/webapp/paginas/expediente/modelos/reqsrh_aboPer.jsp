<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de ABONO DE PERMANENCIA -->

<mod:modelo>
	<mod:entrevista>
		<br/>
		<span style="color:red"> <b>PREENCHER OBRIGATORIAMENTE O CAMPO DESCRIÇÃO COM NOME COMPLETO E ASSUNTO</b></span><br>
		<span style="color:red"> <b>ESTE DOCUMENTO DEVERÁ SER ENVIADO À SRH</b></span>
		<br/><br/>
		<mod:grupo titulo=" ">	
			<mod:grupo>
					<mod:selecao var="excel"
					titulo="VOCATIVO"
					opcoes="EXCELENTÍSSIMA SENHORA DESEMBARGADORA FEDERAL; EXCELENTÍSSIMO SENHOR DESEMBARGADOR FEDERAL"	
					reler="sim" />
			</mod:grupo>
			<br>	
			<mod:grupo titulo=""> 
		       	<mod:texto titulo="RAMAL DO REQUERENTE" var="ramal"/></mod> <br><br>
		        <mod:selecao titulo="Acerto Gramatical da Lotação" var="acgr"  opcoes="no;na" reler="sim" />
			</mod:grupo>
			 <br/><br>
		</mod:grupo>
		
		<br>
		<br>
	
		 <mod:grupo titulo="Opção Manifestada:"> 
		        <mod:radio titulo="Não possui período de licença-prêmio a usufruir" var="periodo" valor="1" marcado="Sim" reler="sim" />
		        <mod:radio titulo="Não contar em dobro períodos de licença-prêmio não usufruídos" var="periodo" valor="2" reler="sim" />
	            <mod:radio titulo="Contar em dobro períodos de licença-prêmio não usufruídos para fins de aposentadoria, estando ciente de que o(s) período(s) não poderá(ão) mais ser usufruído(s) ou convertido(s) em pecúnia" var="periodo" valor="3" reler="sim" />
				<c:if test="${empty valorperiodo}"><c:set var="valorperiodo" value="${param['periodo']}" /></c:if>
		        <c:if test="${valorperiodo == 3}"><mod:selecao titulo="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Quantidade de meses" opcoes="1 (um);2 (dois);3 (três);4 (quatro);5 (cinco);6 (seis);7 (sete);8 (oito);9 (nove)" var="nummeses" reler="sim" /></c:if>     
		 </mod:grupo> <br><br> 
		
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
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" /><br>
		</td></tr>
		   <tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr>
						<td align="center"><p style="font-family:Arial;font-weight:bold;font-size:10pt;">ABONO DE PERMANÊNCIA</p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->
		
        <!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" /> 
		FIM CABECALHO -->
			<br><br><br>
		<c:if test="${doc.subscritor.sexo == 'M'}">
			<c:set var="lotc" value="lotado"></c:set>
		</c:if>
		<c:if test="${doc.subscritor.sexo == 'F'}">
			<c:set var="lotc" value="lotada"></c:set>
		</c:if>
		<c:set var="opt" value="${f:classNivPadr(doc.subscritor.padraoReferencia)}"/>	
			<p style="text-align: justify"><center><br><b> ${excel} PRESIDENTE DO TRIBUNAL REGIONAL FEDERAL DA 2ª REGIÃO</b></center></p>
			
			<br>
			
					
			${doc.subscritor.descricao}, matrícula ${doc.subscritor.matricula}, ${doc.subscritor.cargo.nomeCargo}, ${opt},
			do Quadro de Pessoal do Tribunal Regional Federal da 2ª Região, ${lotc} ${acgr} ${doc.subscritor.lotacao.descricao}, ramal ${ramal},
			vem manifestar interesse em permanecer em atividade e requerer a V. Exª. o abono de permanência 
			 equivalente ao valor da contribuição previdenciária, em face do disposto na Emenda Constitucional 
			 nº 41, por já ter implementado as condições para a aposentadoria voluntária 
	       
	        <c:if test="${periodo == 1 }">.</c:if>
	        <c:if test="${periodo == 2 }">, optando por não contar em dobro qualquer período de licença-prêmio não usufruído.</c:if>
	        <c:if test="${periodo == 3 }">
	        <c:choose>
					<c:when test="${nummeses == '1 (um)'}">
						<c:set var="justone" value="mês"></c:set>
						<c:set var="usufr" value="usufruído"></c:set>
						<c:set var="pod" value="poderá"></c:set>
						<c:set var="convert" value="convertido"></c:set>
					</c:when>
					<c:otherwise>
					    <c:set var="justone" value="meses"></c:set>
						<c:set var="usufr" value="usufruídos"></c:set>
						<c:set var="pod" value="poderão"></c:set>
						<c:set var="convert" value="convertidos"></c:set>
					</c:otherwise>
			</c:choose>				
			, optando por contar em dobro,  para fins de aposentadoria, ${nummeses} ${justone} de licença-prêmio não ${usufr}, estando ciente de que não ${pod} mais ser ${usufr} ou ${convert} em pecúnia.
			</c:if>
			
		<br/><br/>
		 <br/><br>		
			Termos em que <br/>
			Espera deferimento.<br><br><br>
		<!-- INICIO FECHO -->
		   <p align="center">${doc.dtExtenso}</p>
		<!-- FIM FECHO -->
		<br>
		<!-- import java.util.Date; Date hoje = new Date();  -->
		
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
