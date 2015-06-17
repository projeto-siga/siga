<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- este modelo trata de
REQUERIMENTO PARA AVERBAÇÃO DE TEMPO DE SERVIÇO -->

<mod:modelo>
	<mod:entrevista>
		<br/>
		<span style="color:red"> <b>PREENCHER OBRIGATORIAMENTE O CAMPO DESCRIÇÃO COM NOME COMPLETO E ASSUNTO</b></span><br>
		<span style="color:red"> <b>ESTE DOCUMENTO DEVERÁ SER ENVIADO À SRH</b></span>	
		<br/><br/>
	<mod:grupo titulo=" ">	
		<mod:grupo>
				<mod:selecao var="ilustrissimo"
				titulo="VOCATIVO"
				opcoes="ILUSTRÍSSIMA SENHORA DIRETORA;ILUSTRÍSSIMO SENHOR DIRETOR"	
				reler="sim" />
		</mod:grupo>
		<br>	
	</mod:grupo>
			<mod:grupo titulo=""> 
		        <mod:texto titulo="RAMAL DO REQUERENTE" var="ramal"/></mod> <br><br>
		        <mod:selecao titulo="Acerto Gramatical da Lotação" var="acgr"  opcoes="no;na" reler="sim" />
			</mod:grupo>
			 <br/><br>
			 <mod:selecao var="contAverb"
				titulo="Quantidade de Averbações"
				opcoes="1;2;3;4;5"
				reler="sim"  /><br/>
		<mod:grupo depende="contDependAjax">
		<br>
				<c:forEach var="i" begin="1" end="${contAverb}">
					<mod:grupo>
						<mod:texto titulo="Instituição nº ${i}" var="inst${i}" largura="30" 
						maxcaracteres="50" obrigatorio="Sim"/>
					</mod:grupo>
                    <hr style="color: #FFFFFF;" />
				</c:forEach>
		</mod:grupo>	
	    <br/> 		
			
	<mod:grupo titulo="Certidão(ões) anexada(s) (Resolução nº.260/2002/CJF)">	
		    <br>
		   <mod:grupo>
				<mod:caixaverif titulo="Cópia de Certidão expedida por Órgão público (para Serviço Público da administração direta, autárquica ou fundacional)" 
				var="copcertorg" reler="sim" />
		   	 </mod:grupo>
		     <br>
		     <mod:grupo>
				<mod:caixaverif titulo="Cópia de Certidão expedida pelo INSS (para atividade privada/autônoma)" 
				var="copcertinss" reler="sim" />
		     </mod:grupo>
		   <br>
		    <mod:grupo>
		        <mod:caixaverif titulo="Outros" var="outros" reler="sim" />
			    <c:if test="${ outros == 'Sim'}">
			    <mod:texto titulo="- &nbsp;Especificar" var="outrostext" largura="40" />
		    	</c:if>
		     </mod:grupo>
		    <!--  
           	<mod:grupo>
             	<mod:caixaverif titulo="Outros" var="outros" reler="sim"/>
				<mod:texto titulo="" var="descricaoutros" largura="50" /></mod>
			</mod:grupo> -->
			<br><br>
    	</mod:grupo>
		
			
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
		<c:set var="opt" value="${f:classNivPadr(doc.subscritor.padraoReferencia)}"/>	
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#ffffff"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr><br>
		   <tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr><br><br>
						<td align="center"><p style="font-family:Arial;font-weight:bold;font-size:9pt;">AVERBAÇÃO DE TEMPO DE SERVIÇO</p></td>
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
		<c:set var="prest" value="prestado às instituições abaixo"></c:set>
		<c:set var="plur" value="Instituições:"></c:set>
		<c:if test="${contAverb < 2}">
			<c:set var="prest" value="prestado à instituição abaixo"></c:set>
			<c:set var="plur" value="Instituição:"></c:set>
		</c:if>
		<c:set var="anex" value="anexo"></c:set>
		<c:set var="cert" value="Certidão anexada"></c:set>
        <c:if test="${(copcertorg == 'Sim' and copcertinss == 'Sim') or (copcertorg == 'Sim' and outros == 'Sim') or (copcertinss == 'Sim' and outros == 'Sim')}">
          <c:set var="anex" value="anexos"></c:set>
          <c:set var="cert" value="Certidões anexadas"></c:set>
        </c:if>
		<p style="font-family:Arial;font-weight:bold;font-size:9pt;"><center><br><br><b> ${ilustrissimo} DA SECRETARIA DE RECURSOS HUMANOS</center></b></p>
				
			${doc.subscritor.descricao}, matrícula ${doc.subscritor.matricula}, ${doc.subscritor.cargo.nomeCargo}, ${opt}, 
			do Quadro de Pessoal do Tribunal Regional Federal da 2ª Região, ${lotc} ${acgr} ${doc.subscritor.lotacao.descricao}, ramal ${ramal}, vem requerer 
			<b>averbação de tempo de serviço</b> ${prest}, conforme ${anex}, para todos os fins de direito.
            <br><br>
			&nbsp;&nbsp;&nbsp;<b>${plur}</b>
		    <br>
			<c:forEach var="j" begin="1" end="${contAverb}">
			<c:set var="instt" value="${requestScope[(f:concat('inst',j))]}"/>
			&nbsp;&nbsp;&nbsp;${instt}<br>
			</c:forEach>
	        <br>
			&nbsp;&nbsp;&nbsp;<b>${cert} (Resolução nº.260/2002/CJF):</b>
			<br>
			&nbsp;&nbsp;&nbsp;
			<c:if test="${ copcertorg == 'Sim'}">
			    [X] Cópia de Certidão expedida por Órgão Público
			    <br>		
			</c:if>
			<c:if test="${ copcertorg == 'Nao'}">
			    [&nbsp;&nbsp;] Cópia de Certidão expedida por Órgão Público
			    <br>		
			</c:if>
			&nbsp;&nbsp;&nbsp;
			<c:if test="${ copcertinss == 'Sim'}">
			    [X] Cópia de Certidão expedida pelo INSS
			    <br>		
			</c:if>
			<c:if test="${ copcertinss == 'Nao'}">
			    [&nbsp;&nbsp;] Cópia de Certidão expedida pelo INSS
			    <br>		
			</c:if>
			&nbsp;&nbsp;&nbsp;
			<c:if test="${ outros == 'Sim'}">
			    [X] ${outrostext}
			</c:if>
		<br/><br/>
			
		<!-- INICIO FECHO -->
		<p align="center">${doc.dtExtenso}</p>
		<!-- FIM FECHO -->
		
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