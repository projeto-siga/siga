<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de AFASTAMENTOS AUTORIZADOS -->

<mod:modelo>
	<mod:entrevista>
		<br/>
		<span style="color:red"> <b>ESTE DOCUMENTO DEVERÁ SER ENVIADO À CHEFIA IMEDIATA PARA CIÊNCIA E, EM SEGUIDA, À DICAP</b></span>	
		<br/><br/>
	<mod:grupo titulo=" ">	
		<mod:grupo>
				<mod:selecao var="ilustrissima"
				titulo="VOCATIVO"
				opcoes="ILUSTRÍSSIMA SENHORA DIRETORA;ILUSTRÍSSIMO SENHOR DIRETOR; SENHOR PAL´PITE INFELIZ"	
				reler="sim" />
		</mod:grupo>
		<br/>	
	</mod:grupo>
			<mod:grupo titulo=""> 
		        <mod:texto titulo="RAMAL DO REQUERENTE" var="ramal"/></mod> <br/>
			</mod:grupo>
			 <br/> 
			 
			 
	   
      <br/> 
	 <mod:grupo titulo="Documento Anexado:">
	    
	     <mod:radio titulo="Comprovante de doação de sangue" var="periodo" valor="1" reler="sim" />
		 <mod:radio titulo="Comprovante ao se alistar como eleitor" var="periodo" valor="2" reler="sim" />
	     <mod:radio titulo="Xerox de Certidão de Casamento" var="periodo" valor="3" reler="sim" />
		 <mod:radio titulo="Xerox da Certidão de Óbito" var="periodo" valor="4" reler="sim" />
		 <mod:radio titulo="Outros" var="periodo" valor="5" reler="sim" />
		 <c:set var="valorperiodo" value="${periodo}" />
		 <c:if test="${empty valorperiodo}"><c:set var="valorperiodo" value="${param['periodo']}" /></c:if>
		 <c:if test="${valorperiodo == 5}"><mod:texto titulo="&nbsp; Motivo (por etc etc)" var="outromot" largura="40" obrigatorio="Sim" /><br/><mod:texto titulo="&nbsp; Comprovante" var="comprov" largura="40" obrigatorio="Sim"/></c:if>     
	  </mod:grupo>
		<br/>
		<mod:grupo>  
		     <mod:data titulo="&nbsp;&nbsp;Data do Evento" var="dtiniafast" obrigatorio="Sim"/>
             <c:if test="${valorperiodo == 5}"><mod:data titulo="Data Final do Afastamento" var="dtfimafast" obrigatorio="Sim"/></c:if>    
        </mod:grupo><br/>	
        <mod:obrigatorios /><br/><br/>
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
		<table width="100%" border="2" bgcolor="#ffffff"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
		   <tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr>
						<td align="center"><p style="font-family:Arial;font-weight:bold;font-size:11pt;">AFASTAMENTOS AUTORIZADOS</p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->
		
		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" /> 
		FIM CABECALHO -->
				
				
		<c:if test="${doc.subscritor.sexo == 'M'}">
			<c:set var="lotc" value="lotado"></c:set>
		</c:if>
		<c:if test="${doc.subscritor.sexo == 'F'}">
			<c:set var="lotc" value="lotada"></c:set>
		</c:if>

		<p style="text-align: center;font-weight:bold;font-size:11pt;"><b> ${ilustrissima} DA SECRETARIA DE RECURSOS HUMANOS DO TRIBUNAL REGIONAL FEDERAL DA 2ª. REGIÃO</b></p>
				<br/><br/><br/><br/>		
			${doc.subscritor.descricao}, ${doc.subscritor.cargo.nomeCargo}, matrícula ${doc.subscritor.matricula}, ${doc.subscritor.padraoReferencia}, ${lotc} no(a) ${doc.subscritor.lotacao.descricao}, ramal ${ramal}, vem à presença  
			de V.Sª apresentar justificativa pelo afastamento  
			<c:if test="${periodo == 1}">no dia ${dtiniafast}, nos termos do art. 97, inciso I da Lei nº 8.112 de 11/12/1990.</c:if>
            <c:if test="${periodo == 2}">no período de ${dtiniafast} a ${f:calculaData(2,requestScope['dtiniafast'])}, nos termos do art. 97, inciso II da Lei nº 8.112 de 11/12/1990.</c:if>
            <c:if test="${periodo == 3}">no período de ${dtiniafast} a ${f:calculaData(8,requestScope['dtiniafast'])}, nos termos do art. 97, inciso III, alínea 'a' da Lei nº 8.112 de 11/12/1990.</c:if>
            <c:if test="${periodo == 4}">no período de ${dtiniafast} a ${f:calculaData(8,requestScope['dtiniafast'])}, nos termos do art. 97, inciso III, alínea 'b' da Lei nº 8.112 de 11/12/1990.</c:if>
            <c:if test="${periodo == 5}">no período de ${dtiniafast} a ${dtfimafast}, ${outromot}.</c:if>
			
			<br/><br/><br/><br/>
				
			&nbsp;&nbsp;&nbsp;Documento Anexado: 
			<br/>
			&nbsp;&nbsp;
			<c:if test="${ periodo == 1}">
			    [X] Comprovante de doação de sangue
			    <br/>		
			</c:if>
			<c:if test="${ periodo == 2}">
			    [X] Comprovante ao se alistar como eleitor
			    <br/>		
			</c:if><c:if test="${ periodo == 3}">
			    [X] Xerox da Certidão de Casamento
			    <br/>		
			</c:if><c:if test="${ periodo == 4}">
			    [X] Xerox da Certidão de Óbito
			    <br/>		
			</c:if>
			<c:if test="${ periodo == 5}">
			    [X] ${comprov}
			    <br/>		
			</c:if>
				 	 
		<br/><br/>
			
		<!-- INICIO FECHO -->
		<p align="center">${doc.dtExtenso}</p>
		<!-- FIM FECHO -->
		
		<!-- import java.util.Date; Date hoje = new Date();  -->
		
		<!-- INICIO ASSINATURA -->
		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
		<!-- FIM ASSINATURA -->
		<p style="font-weight:bold;font-size:7pt;"><br/><br/><b>ESTE DOCUMENTO DEVERÁ SER ENVIADO À CHEFIA IMEDIATA PARA CIÊNCIA E, EM SEGUIDA, À DICAP</b></p>
		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita.jsp" />
		FIM RODAPE -->
		<br/><br/>
		</body>
	</mod:documento>
</mod:modelo>