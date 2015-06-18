<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista> 
	  
	         <br>
	         <span style="color:red"> <b>PREENCHER OBRIGATORIAMENTE O CAMPO DESCRIÇÃO COM NOME COMPLETO E ASSUNTO</b></span><br>
		     <span style="color:red"> <b>ESTE DOCUMENTO DEVERÁ SER ENVIADO À CHEFIA IMEDIATA PARA ANUÊNCIA E EM SEGUIDA AO RH LOCAL</b></span>	
	         <br><br>
	         	         
		        <mod:grupo>
		           <mod:texto titulo="<b>RAMAL DO REQUERENTE</b>" var="ramal"/></mod> <br><br>
		           <mod:selecao titulo="<b>Acerto Gramatical da Lotação</b>" var="acgr"  opcoes="no;na" reler="sim" /><br></br>
		           <mod:texto titulo="<b>E-mail</b>" var="ema"/></mod> <br><br>
		           <mod:texto titulo="<b>Idade</b>" var="idade" largura="3" /></mod>
		         </mod:grupo>
		          <hr style="color: #FFFFFF;" />
		           <mod:grupo>
		               <mod:texto titulo="<b>Número de Filhos / Enteados registrados nos assentamentos funcionais</b>" var="numfil" largura="3" /></mod> <br><br>
		               <mod:texto titulo="<b>Por extenso</b>" var="pexte" largura="7" />
	               </mod:grupo>
	               <hr style="color: #FFFFFF;" />
             <br>
	            
		     <mod:grupo titulo="Quadro de Pessoal de Origem">
		        <mod:radio titulo="TRF2" var="quadr1" valor="1" marcado="Sim" reler="sim" />
		        <mod:radio titulo="SJRJ" var="quadr1" valor="2" reler="sim" />
	            <mod:radio titulo="SJES" var="quadr1" valor="3" reler="sim" />
		     </mod:grupo>
		     
		     <mod:grupo titulo="Quadro de Pessoal Atual">
		        <mod:radio titulo="TRF2" var="quadr2" valor="1" marcado="Sim" reler="sim" />
		        <mod:radio titulo="SJRJ" var="quadr2" valor="2" reler="sim" />
	            <mod:radio titulo="SJES" var="quadr2" valor="3" reler="sim" />
	         </mod:grupo>
	         
	         <mod:grupo titulo="Quadro de Pessoal Desejado">
	            <mod:radio titulo="TRF2" var="quadr3" valor="1" marcado="Sim" reler="sim" />
		        <mod:radio titulo="SJRJ" var="quadr3" valor="2" reler="sim" />
	            <mod:radio titulo="SJES" var="quadr3" valor="3" reler="sim" />
		     </mod:grupo>
		     
		     <c:set var="valorquadr1" value="${quadr1}" />
		     <c:set var="valorquadr2" value="${quadr2}" />
		     <c:set var="valorquadr3" value="${quadr3}" />
		     
		     <c:set var="valornumfil" value="${numfil}" />

		     <hr style="color: #FFFFFF;" />
		     	     
	  
	</mod:entrevista>
	
<mod:documento>
		<head>
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
			<c:set var="tl" value="11pt"></c:set>
		</c:if>
		
		
		
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="2" bgcolor="#ffffff"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
		   <tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr>
						<td align="center"><p style="font-family:Arial;font-weight:bold;font-size:11pt;"><br></br>INSCRIÇÃO PARA FINS DO EDITAL DE REDISTRIBUIÇÃO DE CARGOS</p></td>
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
		<c:set var="lotd" value="cedido"></c:set>
		<c:set var="doo" value="da"></c:set>
		<c:set var="prepo" value="à"></c:set>
		<c:set var="doa" value="da"></c:set>
		<c:set var="prepa" value="à"></c:set>
		<c:set var="dod" value="da"></c:set>
		<c:set var="prepd" value="à"></c:set>
		
		
		<c:if test="${doc.subscritor.sexo == 'F'}">
			<c:set var="lotc" value="lotada"></c:set>
			<c:set var="lotd" value="cedida"></c:set>
		</c:if>
		
		
						  		    
		        
		<c:if test="${quadr1 == 1}"><c:set var="qpo" value="Tribunal Regional Federal da 2ª Região"></c:set><c:set var="doo" value="do"></c:set><c:set var="prepo" value="ao"></c:set></c:if>
		<c:if test="${quadr1 == 2}"><c:set var="qpo" value="Seção Judiciária do Estado do Rio de Janeiro"></c:set></c:if>
		<c:if test="${quadr1 == 3}"><c:set var="qpo" value="Seção Judiciária do Estado do Espírito Santo"></c:set></c:if>
		
		<c:if test="${quadr2 == 1}"><c:set var="qpa" value="Tribunal Regional Federal da 2ª Região"></c:set><c:set var="doa" value="do"></c:set><c:set var="prepa" value="ao"></c:set></c:if>
		<c:if test="${quadr2 == 2}"><c:set var="qpa" value="Seção Judiciária do Estado do Rio de Janeiro"></c:set></c:if>
		<c:if test="${quadr2 == 3}"><c:set var="qpa" value="Seção Judiciária do Estado do Espírito Santo"></c:set></c:if>
		
		
		<c:if test="${quadr3 == 1}"><c:set var="qpd" value="Tribunal Regional Federal da 2ª Região"></c:set><c:set var="dod" value="do"></c:set><c:set var="prepd" value="ao"></c:set></c:if>
		<c:if test="${quadr3 == 2}"><c:set var="qpd" value="Seção Judiciária do Estado do Rio de Janeiro"></c:set></c:if>
		<c:if test="${quadr3 == 3}"><c:set var="qpd" value="Seção Judiciária do Estado do Espírito Santo"></c:set></c:if>
		
		<c:if test="${numfil == 1}"><c:set var="texto1" value=" e"></c:set><c:set var="numf" value=""></c:set><c:set var="texto2" value=" um filho/enteado registrado em seus assentamentos funcionais."></c:set><c:set var="texto3" value=""></c:set></c:if>
		<c:if test="${numfil >  1}"><c:set var="texto1" value=" e"></c:set><c:set var="numf" value="${numfil}"></c:set><c:set var="texto2" value="filhos/enteados registrados em seus assentamentos funcionais."></c:set><c:set var="texto3" value=" (${pexte}) "></c:set></c:if>
		<c:if test="${empty numfil || numfil == 0}"><c:set var="texto1" value=""></c:set><c:set var="numf" value=""></c:set><c:set var="texto2" value=" e nenhum filho/enteado registrado em seus assentamentos funcionais."></c:set><c:set var="texto3" value=""></c:set></c:if>
		
		<br></br><br></br>
		
		<p style="font-family:Arial;font-size:11pt"> 
		${doc.subscritor.descricao}, matrícula ${doc.subscritor.matricula}, ${f:maiusculasEMinusculas(doc.subscritor.cargo.nomeCargo)},
		do Quadro de Pessoal ${doo} ${qpo}, ora ${lotd} ${prepa} ${qpa}, ${lotc} ${acgr} ${f:maiusculasEMinusculas(doc.subscritor.lotacao.descricao)}, 
		ramal ${ramal}, e-mail ${ema}, vem apresentar sua inscrição para fins de redistribuição de seu cargo para o Quadro de Pessoal ${dod} ${qpd}.
		<br><br>Informa ainda ter ${idade} anos de idade${texto1} ${numf} ${texto3}${texto2}
        </p>
		<br></br><br>
		${valornumfil}
		<p align="center">${doc.dtExtenso}</p>
	   
	    <br>
		
		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
		
		<br>/${doc.cadastrante.siglaPessoa}</br> 
		
		<!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE -->
		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita.jsp" />
		FIM RODAPE -->
				
 
</body>
	</mod:documento>
</mod:modelo>
