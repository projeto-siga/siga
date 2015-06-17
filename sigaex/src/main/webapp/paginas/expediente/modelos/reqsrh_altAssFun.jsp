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
		<br/><br/>
		
		<mod:grupo titulo=" ">	
			<mod:grupo>
				 <mod:selecao var="ilustrissimo"
				titulo="VOCATIVO"
				opcoes="ILUSTRÍSSIMA SENHORA DIRETORA;ILUSTRÍSSIMO SENHOR DIRETOR"	
				reler="sim" />
			</mod:grupo>
			<br>	
			<mod:grupo titulo=""> 
		       	<mod:texto titulo="RAMAL DO REQUERENTE" var="ramal"/></mod> <br><br>
		        <mod:selecao titulo="Acerto Gramatical da Lotação" var="acgr"  opcoes="no;na" reler="sim" />
			</mod:grupo>
			 <br/><br>
		</mod:grupo>
		
		<mod:grupo titulo="Alterações solicitadas:">
				
			  <mod:caixaverif titulo="Nome"
							var="mudnome" reler="sim"/><br/>
						
		      <mod:caixaverif titulo="Escolaridade"
							var="altesc" reler="sim" /><br/>
				
		      <mod:caixaverif titulo="Estado Civil"
							var="mudest" reler="sim"/><br/>
				
			  <mod:caixaverif titulo="Endereço"
							var="altend" reler="sim" /><br/>
				
			  <c:if test="${ altend == 'Sim'}">
			    <br>
			    <mod:grupo titulo="Novo Endereço:">
			         <mod:grupo>
			            <mod:selecao titulo="Logradouro" var="via" opcoes="Alameda;Avenida;Beco;Estrada;Praça;Rua;Travessa" reler="sim" />
			            <mod:texto titulo="" var="descvia" largura="40"/>
			            <mod:texto titulo="Nº" var="numer" largura="5" maxcaracteres="11" />
			            <mod:texto titulo="Complemento" var="complem" largura="10" />	
		             </mod:grupo>
		             <mod:grupo>
			            <mod:texto titulo="Bairro" var="bairr" largura="40" />
			            <mod:texto titulo="Cidade" var="cidad" largura="30" />
			            <mod:selecao titulo="UF" var="uf" opcoes="AC;AL;AM;AP;BA;CE;DF;ES;GO;MA;MG;MT;MS;PA;PB;PE;PI;PR;RJ;RN;RO;RR;RS;TO;SC;SE;SP" reler="sim" />
			            <mod:texto titulo="CEP" var="cepp" largura="10" maxcaracteres="10" />	
		            </mod:grupo>
		            <mod:grupo>
			            <mod:texto titulo="Telefone" var="telef" largura="12" />
			            <mod:caixaverif titulo="Recado" var="rec" reler="sim" />
			            <c:if test="${ rec == 'Sim'}">
			                <mod:texto titulo="***  Contato " var="contato" largura="40" />
			            </c:if>
			       </mod:grupo>
			    </mod:grupo> <br>  
	    	  </c:if>
	    				
		      <mod:grupo>
		  	     <mod:caixaverif titulo="Outros" var="outros" reler="sim" />
			     <c:if test="${ outros == 'Sim'}">
			        <mod:texto titulo="Especificar" var="outrostext" largura="40" />
		    	 </c:if>
		      </mod:grupo>
		
		</mod:grupo> 
					
		<hr style="color: #FFFFFF;" />
		
	    <br/> 
	    <mod:selecao var="contDocs"
				titulo="Quantidade de Documentos Comprobatórios"
				opcoes="1;2;3;4;5"
				reler="sim"  /><br/>
		<mod:grupo depende="contDependAjax">
				<c:forEach var="i" begin="1" end="${contDocs}">
					<mod:grupo>
						<mod:texto titulo="Documento nº ${i}" var="documento${i}" largura="30" 
						maxcaracteres="50" obrigatorio="Sim"/>
					</mod:grupo>
                    <hr style="color: #FFFFFF;" />
				</c:forEach>
		</mod:grupo>	
	    <br/> 		
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
						<td align="center"><p style="font-family:Arial;font-weight:bold;font-size:9pt;">ALTERAÇÃO DE ASSENTAMENTOS FUNCIONAIS</p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>	
		FIM PRIMEIRO CABECALHO -->
        
		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->
		<c:set var="altt" value="seja efetuada a seguinte alteração"></c:set>
		<c:set var="mud" value="Mudança Solicitada"></c:set>
		<c:if test="${(mudnome == 'Sim' and altesc == 'Sim') or (mudnome == 'Sim' and mudest == 'Sim') or (mudnome == 'Sim' and altend == 'Sim')or (mudnome == 'Sim' and outros == 'Sim') or (altesc == 'Sim' and mudest == 'Sim') or (altesc == 'Sim' and altend == 'Sim')or (altesc == 'Sim' and outros == 'Sim') or (mudest == 'Sim' and altend == 'Sim') or (mudest == 'Sim' and outros == 'Sim') or (altend == 'Sim' and outros == 'Sim')}">
          <c:set var="altt" value="sejam efetuadas as seguintes alterações"></c:set>
          <c:set var="mud" value="Mudanças Solicitadas"></c:set>
        </c:if>
			<c:set var="opt" value="${f:classNivPadr(doc.subscritor.padraoReferencia)}"/>           
			<p style="font-family:Arial;font-weight:bold;font-size:8pt;"> <b><center> ${ilustrissimo} DA SECRETARIA DE RECURSOS HUMANOS</center></b></p>
			<p style="font-family:Arial;font-size:9pt;">			
			${doc.subscritor.descricao}, matrícula ${doc.subscritor.matricula}, ${doc.subscritor.cargo.nomeCargo}, ${opt},
			do Quadro de Pessoal deste Tribunal, 
			<c:if test="${ doc.subscritor.sexo == 'M'}">
			   	lotado
			</c:if>
			<c:if test="${ doc.subscritor.sexo == 'F'}">
			    lotada
			</c:if>
			 ${acgr} ${doc.subscritor.lotacao.descricao},  ramal ${ramal}, requer a Vª.Sª. que
			${altt} em seus assentamentos funcionais :<br><br>  
			<b>${mud}:</b><br>
		    <c:if test="${ mudnome == 'Sim'}">Alteração de Nome<br></c:if>
		    <c:if test="${ altesc == 'Sim'}">Alteração de Escolaridade<br></c:if>
		    <c:if test="${ mudest == 'Sim'}">Alteração de Estado Civil<br></c:if>
		    <c:if test="${outros == 'Sim'}">${outrostext}</c:if><br>
		    <c:if test="${ altend == 'Sim'}">
		   <!--      Alteração de Endereço<br><br>   --> 
		         <br><b>Novo Endereço:</b><br>
		          ${via}&nbsp;${descvia}&nbsp;nº&nbsp;${numer}&nbsp;***&nbsp;Complemento : ${complem}<br>
		          Bairro : ${bairr}&nbsp;***&nbsp;Cidade : ${cidad}&nbsp;***&nbsp;UF : ${uf}&nbsp;***&nbsp;CEP : ${cepp}<br>
		    
		    <c:if test="${rec == 'Sim'}">Telefone para recados : ${telef}&nbsp;&nbsp;***&nbsp;Contato : ${contato}<br></c:if>
		    <c:if test="${rec != 'Sim'}">Telefone : ${telef}<br></c:if>
			</c:if>
		      
		    
			<br>	
			<b>
			<c:choose>
					<c:when test="${contDocs == 1}">Documento comprobatório anexado:</c:when>
					<c:otherwise>Documentos comprobatórios anexados:</c:otherwise>
		    </c:choose>
		    </b>
		    <br>
			<c:forEach var="j" begin="1" end="${contDocs}">
			<c:set var="doccs" value="${requestScope[(f:concat('documento',j))]}"/>
			${doccs}<br>
			</c:forEach>
	        <br>
			Termos em que <br/>
			Espera deferimento.</p>
		<p>
		    
		
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