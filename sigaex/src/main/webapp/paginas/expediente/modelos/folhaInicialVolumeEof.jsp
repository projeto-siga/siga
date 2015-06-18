<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<mod:modelo>
   <mod:entrevista></mod:entrevista>
      <mod:documento>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
				<title></title>
			 	<style type="text/css">
				   @page {
	               margin-left: 1 cm;
	               margin-right: 1.5 cm;
	               margin-top: 0.5 cm;
	               margin-bottom: 1 cm;
                   }
				</style> 
			</head>
		<body>		
           
		  <table  cellpadding="1" cellspacing="0" width="100%" >
		    <tr> 
		       <td width= "55%">
		          <table width="100%"  align="left" cellpadding="0" cellspacing="1" border="1" style="border-color: black; border-spacing: 0px; border-collapse: collapse">
		             <tr> 
		                <td  width="22%" rowspan="2" align="left" style="border-collapse: collapse; border-color: black; font-size: 8pt"><b> Gerente do<br/>Contrato<b/></td>
			            <td  width="63%" align="left" style="border-collapse: collapse; border-color: black; font-size: 8pt" valign="top"><b>Titular<br/>&nbsp;</td>
			            <td  width="15%"  style="border-collapse: collapse; border-color: black; font-size: 8pt" valign="top">Fls.<br/><br/></td>
		             </tr>
		             <tr> 	
			             <td   align="left" style="border-collapse: collapse; border-color: black; font-size: 8pt" valign="top"><b>Substituto<br/>&nbsp;</b></td>
		                 <td  style="border-collapse: collapse; border-color: black; font-size: 8pt" valign="top">Fls.<br/><br/></td>	
		             </tr>
		          </table>
		       </td> 
		       
		       <td width="5%">
		       </td>
		       <td width="45%">
		          <table width="100%" align="right" cellpadding="0" cellspacing="1" border="1" style="border-color: black; border-spacing: 0px; border-collapse: collapse">
		             <tr> 
			           <td   width="60%"  align="center" valign="middle" style="border-collapse: collapse; border-color: black; font-size: 9pt"><br/><b>Processo Nº</b><br/><br/></td>
			           <td   width="20%"  align="center"  style="border-collapse: collapse; border-color: black; font-size: 9pt"><br/><b>Ap.</b><br/><br/></td>
			           <td   width="20%"  align="center" style="border-collapse: collapse; border-color: black; font-size: 9pt"><br/><b>Vol.</b><br/><br/></td>
		             </tr>
		             <tr> 	
			            <td  align="center" valign="middle" style="border-collapse: collapse; border-color: black; font-size: 9pt"><b>EOF ${doc.codigo}</b></td>
		                <td style="border-collapse: collapse; border-color: black;">&nbsp</td>
			            <td style="border-collapse: collapse; border-color: black;">&nbsp</td>
		             </tr>
		          </table>
		       </td >
		    </tr>
		  </table> 
		  <br/>
		  <table  width="100%" cellpadding="0" cellspacing="1" border="1" style="border-color: black; border-spacing: 0px; border-collapse: collapse">
		    <tr> 
			   <td width="15%" align="left" style="font-size: 10" style="border-collapse: collapse; border-color: black;" rowspan="2" ><b>&nbsp CONTRATADA</b><br/></td>
			   <td width="67%" style="border-collapse: collapse; border-color: black;" rowspan="2">&nbsp</td>
			   <td width="12%" style="font-size: 8pt; border-collapse: collapse; border-color: black;" align="center" valign="middle">% de Retenção<br/>de Tributos<br/>Federais</td>
			   <td width="8%"  align="center" style="border-collapse: collapse; border-color: black;">Fls.</td>
			</tr>
			<tr>
			   <td  width="11%"   align="center" style=" font-size: 8pt; border-collapse: collapse; border-color: black;">&nbsp<br/><br/></td>
			   <td  width="5%"  align="center" style="border-collapse: collapse; border-color: black;">&nbsp<br/><br/></td>
			</tr>
		  </table>
			   <br/>
		   <c:choose>
		      <c:when test="${doc.exModelo.idMod=='534'}">
		  		<c:import url="/paginas/expediente/modelos/folhaInicialVolumeSituacoesResiduais.jsp"/>
		      </c:when>	
		      <c:when test="${doc.exModelo.idMod=='565'}">
		      	<c:import url="/paginas/expediente/modelos/folhaInicialVolumeContratoExclusividade.jsp"/>
		      </c:when>
		      <c:when test="${doc.exModelo.idMod=='566'}">
		      	<c:import url="/paginas/expediente/modelos/folhaInicialVolumeContratoContinuada.jsp"/>
		      </c:when>
		      <c:when test="${doc.exModelo.idMod=='567'}">
		      	<c:import url="/paginas/expediente/modelos/folhaInicialVolumeAtaComTermo.jsp"/>	         
		      </c:when>
		      <c:when test="${doc.exModelo.idMod=='568'}">
		      	<c:import url="/paginas/expediente/modelos/folhaInicialVolumeAtaSemTermo.jsp"/>
		      </c:when>
		      <c:otherwise>
		      </c:otherwise>
		   </c:choose>
				<p align="center" style="font-size: 9pt">Certifico que, nesta data, iniciei o volume ${mob.numSequencia} do processo em epígrafe.</p>
				<p align="center" style="font-size: 9pt">${doc.dtExtenso}</p>
				<br/>
				<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp"/>
				
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


