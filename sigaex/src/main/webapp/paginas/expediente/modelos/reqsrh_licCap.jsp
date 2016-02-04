<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista> 
	         <br>
	         <span style="color:red"> <b>PREENCHER OBRIGATORIAMENTE O CAMPO DESCRIÇÃO COM NOME COMPLETO E ASSUNTO</b></span><br>
		     <span style="color:red"> <b>ESTE DOCUMENTO DEVERÁ SER ENVIADO À SRH</b></span>	
	         <br><br>
	         <mod:grupo titulo=""> 
		         <mod:grupo>
			     	<mod:selecao var="ilustrissimo"
				    titulo="VOCATIVO"
				    opcoes="ILUSTRÍSSIMA SENHORA DIRETORA;ILUSTRÍSSIMO SENHOR DIRETOR"	
			        reler="sim" />
		         </mod:grupo>
                 <br>
	         </mod:grupo>
		
	         <mod:grupo>
		        <mod:texto titulo="RAMAL DO REQUERENTE" var="ramal"/></mod> <br><br>
		        <mod:selecao titulo="Acerto Gramatical da Lotação" var="acgr"  opcoes="no;na" reler="sim" />
	         </mod:grupo>

		     <hr style="color: #FFFFFF;" />
		    		                                          
		     <mod:grupo titulo="Tipo:"> 
	               <mod:radio titulo="Curso" var="tipo" valor="1" reler="sim" />
		           <mod:radio titulo="Evento" var="tipo" valor="2" reler="sim" />
	         </mod:grupo> 
		    
		     
		     
			 <c:set var="valortipo" value="${tipo}" />
		 	 
	         <c:if test="${empty valortipo}">
	             <c:set var="valortipo" value="${param['tipo']}" />
	         </c:if>
	
	
	
	  	     <c:if test="${valortipo == 1}">   
	  		        <mod:grupo titulo="DADOS DO CURSO:">
			            <mod:texto titulo="Nome" var="curso" largura="40"/>
		            </mod:grupo> 
	 		 </c:if>                      
		 
		    
	  		 <c:if test="${valortipo == 2}">   
	  		        <mod:grupo titulo="DADOS DO EVENTO:">
			             <mod:texto titulo="Nome" var="curso" largura="40"/>
		            </mod:grupo> 
	 		 </c:if>         
		    
		    
		    <!--  
	         <mod:grupo titulo="DADOS DO CURSO:">
			        <mod:texto titulo="Nome" var="curso" largura="40"/>
	        </mod:grupo> -->
		
		
		     <mod:grupo>  
		            <mod:data titulo="Periodo de " var="datiniciocur" obrigatorio="sim"/>
		            <mod:data titulo="a" var="datfimcur" obrigatorio="sim"/>
             </mod:grupo>   
        
		     <mod:grupo>
		            <mod:texto titulo="Instituição Promotora" var="instituto" largura="45"/>
		     </mod:grupo>
		
	         <mod:grupo>
		            <mod:texto titulo="Local de Realização" var="local" largura="36"/>
		     </mod:grupo>
		
		
		
	         <mod:grupo>
				<mod:texto titulo="Cidade" var="city" largura="20" />
				<mod:selecao titulo="UF" var="uf" opcoes="AC;AL;AM;AP;BA;CE;DF;ES;GO;MA;MG;MT;MS;PA;PB;PE;PI;PR;RJ;RN;RO;RR;RS;TO;SC;SE;SP" reler="sim" />
				<mod:texto titulo="Telefone" var="telefone" largura="12" />
		     </mo:grupo>
		
		     <mod:grupo>
		        <mod:texto titulo="Carga Horária" var="cargahora"largura="10"/>
	         </mod:grupo>
		
		     <mod:grupo>
		        <mod:texto titulo="Justificativa" var="justif"largura="60"/>
		     </mod:grupo>
		
		     <mod:grupo>
		    	<mod:caixaverif titulo="Beneficiário de Bolsa de Estudo" 
				var="bolsaestu" reler="sim" />
		     </mod:grupo>
	</mod:grupo>
	          <hr style="color: #FFFFFF;" />    
	
	          	          
	         <mod:grupo titulo="Período de Licença:">
				<mod:radio titulo="Integral" var="periodo" valor="1" reler="sim" />
				<mod:radio titulo="Parcelado" var="periodo" valor="2" reler="sim" />
	
	            <c:set var="valorperiodo" value="${periodo}" />
	            <c:if test="${empty valorperiodo}">
	                <c:set var="valorperiodo" value="${param['periodo']}" />
	            </c:if>
	
	   		    <c:if test="${valorperiodo == 1}">   
	  				<mod:grupo titulo=" Período de Licença">
		               <mod:data titulo="De" var="datinicio" obrigatorio="sim"/>
		               <mod:data titulo="a" var="datfim" obrigatorio="sim"/>
	                </mod:grupo>   
	 		    </c:if>   
	 		    
	 		                              
     	        <c:if test="${valorperiodo == 2}">   
	                   <hr style="color: #FFFFFF;" />
		               <mod:selecao var="contadorinterv"
				       titulo="Quantidade de Intervalos"
				       opcoes="2;3"
			           reler="sim"  /><br/>
		               <br>
	            	   <mod:grupo titulo=" Períodos de Licença"> </mod:grupo>   
	                   <mod:grupo depende="contDependAjax">
	                   
		                   <c:if test="${valorperiodo == 2}">
		                   	    <c:forEach var="i" begin="1" end="${contadorinterv}">
				     	             <mod:grupo titulo=" Período nº ${i}">
		                                 <mod:data titulo="De" var="datinicio${i}" obrigatorio="sim"/>
		            	                 <mod:data titulo="a" var="datfim${i}" obrigatorio="sim"/>
	                                 </mod:grupo>   
  			     	            </c:forEach>
			               </c:if>	
			               	
		               </mod:grupo>	
		        </c:if>	
	
	
	    
        </mod:grupo>   
	<hr style="color: #CCCCCC;" />
	
	<mod:grupo titulo="Documentos Anexados">
		<mod:grupo>
				<mod:caixaverif titulo="Declaração de matrícula" 
				var="dclmatr" reler="sim" />
		</mod:grupo>
		<mod:grupo>
				<mod:caixaverif titulo="Contrato com a instituição" 
				var="ctrinst" reler="sim" />
		</mod:grupo>
		<mod:grupo>
				<mod:caixaverif titulo="Recibo de pagamento de matrícula" 
				var="recpag" reler="sim" />
		</mod:grupo>
		<mod:grupo>
				<mod:caixaverif titulo="Declaração de aluno regular" 
				var="declalu" reler="sim" />
		</mod:grupo>
		<mod:grupo>
				<mod:caixaverif titulo="Conteúdo programático expedido pela instituição promotora, 
				contendo a carga horária e o período de realização do curso" 
				var="contprogram" reler="sim" />
		</mod:grupo>
		<mod:grupo>
				<mod:caixaverif titulo="Folder da instituição" var="folderinst" reler="sim" />
		</mod:grupo>
		<mod:grupo>
			<mod:caixaverif titulo="Outros" var="outros" reler="sim" />
			<c:if test="${ outros == 'Sim'}">
			<mod:texto titulo="Especificar" var="outrostext" largura="40" />
			</c:if>
		</mod:grupo>
		
  <!-- 
		<c:if test="${outros == 'Sim'}">
		<c:set var="dclmatr" value="nao"></c:set>
		</c:if>  -->	
		
		
		
		
		
		
		
		<br>
		<br>
		<span style="color:red"> <b>ANUÊNCIA DA CHEFIA IMEDIATA:</b></span><br>
		<span style="color:red"> <b>Declaro estar de acordo com a solicitação do servidor e informo não haver outro servidor <br>em gozo de licença para capacitação no mesmo período pleiteado neste requerimento.</b></span>
	</mod:grupo> 
	</mod:entrevista>
	
<mod:documento>
-		<head>
		<style type="text/css">
@page {
	margin-left: 0.2cm;
	margin-right: 0.2cm;
	margin-top: 0.2cm;
	margin-bottom: 0.2cm;
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
						<tr>
						<td align="center"><p style="font-family:Arial;font-weight:bold;font-size:10pt;">LICENÇA PARA CAPACITAÇÃO</p></td>
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
		<c:set var="benef1" value="Beneficiário"></c:set>
		<c:set var="benef2" value="beneficiário"></c:set>		
		<c:if test="${doc.subscritor.sexo == 'F'}">
			<c:set var="lotc" value="lotada"></c:set>
			<c:set var="benef1" value="Beneficiária"></c:set>
			<c:set var="benef2" value="beneficiária"></c:set>
		</c:if>
        <c:set var="opt" value="${f:classNivPadr(doc.subscritor.padraoReferencia)}"/>
		<p style="text-align: center;font-weight:bold;font-size:8pt;"><center><b> ${ilustrissimo} DA SECRETARIA DE RECURSOS HUMANOS</b></center></p>
		
		<p style="font-family:Arial;font-size:10pt">				
		${doc.subscritor.descricao}, matrícula ${doc.subscritor.matricula}, ${doc.subscritor.cargo.nomeCargo}, ${opt},
		do Quadro de Pessoal do Tribunal Regional Federal da 2ª Região, ${lotc} ${acgr} ${doc.subscritor.lotacao.descricao}, 
		ramal ${ramal}, requer, nos termos do art.87, da Lei nº 8.112/90, regulamentado pela Resolução nº. 05, de 10/03/2008, do Conselho da Justiça Federal e Resolução nº. 22/2008, de 03/10/2008, deste 
		Tribunal, licença para capacitação para participar do 
		<c:choose>
					<c:when test="${tipo == 1}">&nbsp;curso&nbsp;</c:when>
					<c:otherwise>&nbsp;evento&nbsp;</c:otherwise>
		    </c:choose>
		abaixo discriminado:  
		</p>
			<p style="font-family:Arial;font-size:9pt"> 
	       
			<c:choose>
					<c:when test="${tipo == 1}">Nome do Curso</c:when>
					<c:otherwise>Nome do evento</c:otherwise>
		    </c:choose>
		    
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: &nbsp;&nbsp;${curso} <br>
			Período &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: &nbsp;&nbsp;&nbsp;${datiniciocur} a ${datfimcur}
			&nbsp;&nbsp;&nbsp;___________&nbsp;&nbsp;&nbsp;&nbsp;Carga Horária:&nbsp;&nbsp;&nbsp;${cargahora}<br>
			Instituição Promotora &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;${instituto} <br> <!-- &nbsp;&nbsp;&nbsp;___________&nbsp;&nbsp;&nbsp;&nbsp;Local de Realização&nbsp;&nbsp;  :&nbsp;&nbsp;&nbsp;${local} <br> <br> -->
	     	Local de Realização&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  :&nbsp;&nbsp;&nbsp;${local} <br>
			Cidade &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;${city}
			&nbsp;&nbsp;&nbsp;______&nbsp;&nbsp;&nbsp;&nbsp;UF:&nbsp;${uf}
			&nbsp;&nbsp;&nbsp;______&nbsp;&nbsp;&nbsp;&nbsp;Telefone:&nbsp;&nbsp;&nbsp;${telefone}<br>
			Justificativa para participação :&nbsp;&nbsp;&nbsp;${justif}<br>
            [X]
            <c:choose>
					<c:when test="${bolsaestu == 'Sim'}">&nbsp;${benef1} de Bolsa de Estudo</c:when>
					<c:otherwise>&nbsp;Não ${benef2} de Bolsa de Estudo</c:otherwise>
		    </c:choose>
		    
		    <br><br>	
		
		    <c:if test="${periodo == 1}">
			<b>Período de Licença:</b> ${datinicio} a ${datfim}<br>
		    </c:if>
		    		    
			<c:if test="${periodo == 2}">
			<b>Períodos de Licença:</b>
			<c:forEach var="i" begin="1" end="${contadorinterv}">
			<c:set var="dt1" value="${requestScope[(f:concat('datinicio',i))]}" />
	 		<c:set var="dt2" value="${requestScope[(f:concat('datfim',i))]}" />
			&nbsp;&nbsp;${dt1} a ${dt2} 
			</c:forEach>
			</c:if>	
			</p>
			<p style="font-family:Arial;font-size:9pt">
			<b>Documentos Anexados:</b><br>
		    <c:if test="${ dclmatr == 'Sim'}">[X] Declaração de matrícula.</c:if>
			<c:if test="${ dclmatr == 'Nao'}">[&nbsp;&nbsp;] Declaração de matrícula.</c:if>
			<br>	
			<c:if test="${ ctrinst == 'Sim'}">[X] Contrato com a instituição.</c:if>
			<c:if test="${ ctrinst == 'Nao'}">[&nbsp;&nbsp;] Contrato com a instituição.</c:if>
		    <br>	
			<c:if test="${ recpag == 'Sim'}">[X] Recibo de pagamento de matrícula.</c:if>
			<c:if test="${ recpag == 'Nao'}">[&nbsp;&nbsp;] Recibo de pagamento de matrícula.</c:if>
			<br>	
			<c:if test="${ declalu == 'Sim'}">[X] Declaração de aluno regular.</c:if>
			<c:if test="${ declalu == 'Nao'}">[&nbsp;&nbsp;] Declaração de aluno regular.</c:if>
			<br>
			<c:if test="${ contprogram == 'Sim'}">[X] Conteúdo programático expedido pela instituição promotora, contendo carga horária e período de realização do curso.</c:if>
			<c:if test="${ contprogram == 'Nao'}">[&nbsp;&nbsp;] Conteúdo programático expedido pela instituição promotora, contendo carga horária e período de realização do curso.</c:if>
			<br>
			<c:if test="${ folderinst == 'Sim'}">[X] Folder da instituição.</c:if>
			<c:if test="${ folderinst == 'Nao'}">[&nbsp;&nbsp;] Folder da instituição.</c:if>
			<br>
			<c:if test="${ outros == 'Sim'}">[X] ${outrostext}</c:if><br>
			</p>
		    
		    <p style="font-family:Arial;font-size:8pt">
			Declaro que todas as informações aqui prestadas são verdadeiras e que tenho conhecimento do inteiro teor das Resoluções nº 05/CJF, de 03/10/2008 e nº 22/2008, de 03/10/2008, deste Tribunal.
			Declaro, ainda, estar ciente que, ao final da licença para capacitação ora requerida, deverei apresentar, no prazo máximo de 30 (trinta) dias, o(s) seguintes documentos:
			<br>
			a) - No caso de afastamento para participação de curso: comprovante de frequência no curso ou certificado de conclusão, conforme previsto na Resolução nº 05/2008 - CJF. 
			<br>b) - Na hipótese de licença para conclusão de curso de especialização, mestrado ou doutorado: relatório semanal das atividades desenvolvidas, devidamente endossado pelo orientador ou
			coordenador do respectivo curso; certificado de conclusão e cópia da monografia/dissertação, conforme disposto nas Resoluções nº 05/2008-CJE e nº 22/2008-TRF2.
			</p>
					
		<!-- INICIO FECHO -->
		<p align="center">${doc.dtExtenso}</p>
	    <!-- FIM FECHO -->
	    <br>
		<!-- INICIO ASSINATURA -->
		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
		<!-- FIM ASSINATURA -->
		<br>  
		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->
		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita.jsp" />
		FIM RODAPE -->
		
		
		</body>
	</mod:documento>
</mod:modelo>
