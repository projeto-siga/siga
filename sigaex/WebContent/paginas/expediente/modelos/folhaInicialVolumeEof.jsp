<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
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
		<table width="100%"  border="0" bgcolor="#FFFFFF">
	          <tr bgcolor="#FFFFFF">
		         <td align="left" valign="bottom" width="15%"><img src="contextpath/imagens/brasao2.png" width="65" height="65" /></td>
		         <td align="left" width="1%"></td>
		         <td width="35%">
		            <table align="left" width="100%">
			          <tr>
				          <td width="100%" align="left">
				          <p style="font-family: AvantGarde Bk BT, Arial; font-size: 11pt;">PODER JUDICIÁRIO</p>
				          </td>
			          </tr>
			          <tr>
				          <td width="100%" align="left">
				          <p style="font-family: Arial; font-size: 10pt; font-weight: bold;">JUSTIÇA FEDERAL</p>
				          </td>
			          </tr>
			          <tr>
			             <td width="100%" align="left">
				         <p style="font-family: AvantGarde Bk BT, Arial; font-size: 8pt;">
				         <c:choose>
					     <c:when test="${empty mov}">${doc.lotaTitular.orgaoUsuario.descricaoMaiusculas}</c:when>
					     <c:otherwise>${mov.lotaTitular.orgaoUsuario.descricaoMaiusculas}</c:otherwise>
				         </c:choose></p>
				         </td>
			          </tr>
		            </table>
		         </td>
		         <td width="59%" align="center"><p align="center" style=" font: bold; font-size: 16pt"><b>PROCESSO ADMINISTRATIVO &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b><br/><FONT size="2">${doc.exModelo.nmMod} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></p></td>
	          </tr>
           </table>  
           <br/>
           
		  <table border="0"  cellpadding="1" cellspacing="0" width="100%">
		    <tr> <%-- Código substituído, abaixo, porque o comando rowspan não é totalmente aceito no nheengatu.
		       <td width= "55%">
		          <table border="0" width="100%"  bordercolor="#000000"  bgcolor="#000000" align="left" cellpadding="0" cellspacing="1">
		             <tr> 
		                <td border="0"  width="22%" bgcolor="#FFFFFF" rowspan="2" " align="left"><b> Gerente do<br/>Contrato<b/></td>
			            <td border="0"  width="63%" bgcolor="#FFFFFF" align="center"><b>Titular</b><br/><br/></td>
			            <td border="0"  width="15%" bgcolor="#FFFFFF">Fls.<br/><br/></td>
		             </tr>
		             <tr> 	
			             <td border="0"  bgcolor="#FFFFFF" align="center"><b>Substituto<br/><br/></b></td>
		                 <td border="0"  bgcolor="#FFFFFF">Fls.<br/><br/></td>	
		             </tr>
		          </table>
		       </td> --%>
		       
		       <td width= "55%">
		          <table border="0" width="100%"  bordercolor="#000000"  bgcolor="#000000" align="" cellpadding="0" cellspacing="1">
		             <tr>
		                <td bgcolor="#FFFFFF" width="25%"><br/><b>&nbsp Gerente do <br/> &nbsp Contrato</b> <br/><br/>
		                </td>
		                <td > 
		                   <table border="0" width="100%" bordercolor="#000000"  bgcolor="#000000" align="" cellpadding="0" cellspacing="1">
		                      <tr>
		                         <td bgcolor="#FFFFFF" width="20%"><b>&nbsp  Titular</b><br/><br/><br/>
		                         </td>
		                         <td bgcolor="#FFFFFF" width="5%" valign="top"><b>&nbsp  Fls.</b><br/><br/>
		                         </td>
		                      </tr>
		                      <tr>
		                         <td bgcolor="#FFFFFF"><b>&nbsp  Substituto</b><br/><br/><br/>
		                         </td>
		                         <td bgcolor="#FFFFFF" valign="top"><b>&nbsp  Fls.</b><br/><br/>
		                         </td>
		                      </tr>
		                   </table>
		                </td>
		             </tr>
		          </table>
		       
		       <td width="5%">
		       </td>
		       <td width="45%">
		          <table border="0" width="100%" bordercolor="#000000" bgcolor="#000000" align="right" cellpadding="0" cellspacing="1">
		             <tr> 
			           <td  border="0"  width="60%" bgcolor="#FFFFFF"  align="center" valign="middle"><br/><b>Processo Nº</b><br/><br/></td>
			           <td  border="0"  width="20%" bgcolor="#FFFFFF"  align="center"><br/><b>Ap.</b><br/><br/></td>
			           <td  border="0"  width="20%" bgcolor="#FFFFFF"  align="center"><br/><b>Vol.</b><br/><br/></td>
		             </tr>
		             <tr> 	
			            <td border="0" bgcolor="#FFFFFF" align="center"><b>EOF ${doc.codigo}</b><br/><br/><br/></td>
		                <td border="0" bgcolor="#FFFFFF">&nbsp</td>
			            <td border="0" bgcolor="#FFFFFF">&nbsp</td>
		             </tr>
		          </table>
		       </td >
		    </tr>
		  </table> 
		  <br/>
		  <table border="0"  width="100%" bordercolor="#000000" bgcolor="#000000" cellpadding="0" cellspacing="1">
		    <tr> 
			   <td border="0" widht="15%" bgcolor="#FFFFFF" align="left" style="font-size: 10"><b>&nbsp CONTRATADA</b><br/></td>  
			   <td border="0" width="67%" bgcolor="#FFFFFF" >&nbsp</td>
			   <td>
			      <table cellpadding="0" cellspacing="1" width="100%">
			         <tr>
			            <td border="0" width="12%" bgcolor="#FFFFFF"  style=" font-size: 8pt" align="center" valign="middle">% de Retenção<br/>de Tributos<br/>Federais</td>
			            <td border="0" width="8%" bgcolor="#FFFFFF" align="center">Fls.</td>
			         </tr>
			          <tr>
			            <td border="0" width="11%" bgcolor="#FFFFFF"  style=" font-size: 8pt", align="center">&nbsp<br/><br/></td>
			            <td border="0" width="5%" bgcolor="#FFFFFF" align="center">&nbsp<br/><br/></td>
			         </tr>
			      </table>
			   </td> 
		    </tr>
		  </table><br/>
		   <c:choose>
		   
<%-- Início formulário Modelo Situações Residuais  --%>

		      <c:when test="${doc.exModelo.idMod=='534'}">
		  <table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" bordercolor="#000000" width="100%">
		     <tr>
		        <td border="0" bgcolor="#FFFFFF" align="center" width="50%">
		        <%-- Trecho substituído devido às alterações feitas nos formulários  
		           <table border="0" bgcolor="#000000" bordercolor="#FFFFFF" width="100%">
		              <tr bgcolor="#C0C0C0"" height="40">
		                 <td border="0" width="40%" align="center" style="font-size: 9pt">Nota de Empenho<br/>&nbsp</td>
		                 <td border="0" width="20%" align="center" style="font-size: 9pt">Tipo<br/>&nbsp</td>
		                 <td border="0" width="20%" align="center" style="font-size: 9pt">Data<br/>&nbsp</td>
		                 <td border="0" width="20%" align="center" style="font-size: 9pt">Valor (R$)&nbsp</td>
		              </tr>
		              <c:forEach var="x" begin="0" end="7">
		                 <tr height="40">           
		                    <td border="0" width="25%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="25%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="25%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="25%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 </tr>
		              </c:forEach>
		           </table> --%>
		           <table border="0" bgcolor="#000000" bordercolor="#FFFFFF" width="100%">
		              <tr bgcolor="#C0C0C0"" height="40">
		                 <td border="0" width="38%" align="center" style="font-size: 9pt">Nota de Empenho<br/>&nbsp</td>
		                 <td border="0" width="17%" align="center" style="font-size: 9pt">Tipo<br/>&nbsp</td>
		                 <td border="0" width="17%" align="center" style="font-size: 9pt">Data<br/>&nbsp</td>
		                 <td border="0" width="17%" align="center" style="font-size: 9pt">Valor <br/>(R$)&nbsp</td>
		                 <td border="0" width="11%" align="center" style="font-size: 9pt">Fls.</td>
		              </tr>
		              <c:forEach var="x" begin="0" end="7">
		                 <tr height="40">           
		                    <td border="0" width="38%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="17%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="17%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="17%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="11%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 </tr>
		              </c:forEach>
		           </table>
		           <table border="0" bgcolor="#000000" bordercolor="#000000" width="100%">
		              <tr height="40" bgcolor="#C0C0C0">
		                 <td border="0" width="20%" align="center">&nbsp</td>
		                 <td border="0" width="19%" align="center" style="font-size: 8pt">Vigência</td>
		                 <td border="0" width="12%"  align="center" style="font-size: 8pt">Fls.</td>
		                 <td border="0" width="13%" align="center" style="font-size: 8pt">Public.<br/>Fls.</td>
		                 <td border="0" width="18%" align="center" style="font-size: 8pt">SIAFI</td>
		                 <td border="0" width="18%" align="center" style="font-size: 8pt">SIASG</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Contrato Nº</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Garantia</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Termo<br/> Aditivo Nº 1</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Garantia</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Termo<br/> Aditivo Nº 2</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Garantia</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Termo<br/> Aditivo Nº 3</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Garantia</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		           </table>
		        </td>
		        <td border="0" bgcolor="#FFFFFF" align="center" width="50%">
		           <table border="0" bgcolor="#000000" bordercolor="#000000" width="100%">
		              <tr height="40" bgcolor="#C0C0C0">
		                 <td border="0" width="22%" align="center" style="font-size: 9pt">CND<br/>Válida até</td>
		                 <td border="0" width="12%" align="center" style="font-size: 9pt">Fls.</td>
		                 <td border="0" width="23%" align="center" style="font-size: 9pt">CCN<br/>Válida até</td>
		                 <td border="0" width="12%" align="center" style="font-size: 9pt">Fls.</td>
		                 <td border="0" width="21%" align="center" style="font-size: 9pt">CRF<br/>Válida até</td>
		                 <td border="0" width="12%" align="center" style="font-size: 9pt">Fls.</td>
		              </tr>
		              <c:forEach var="x" begin="0" end="7">
		                 <tr height="40">           
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 </tr>
		              </c:forEach>   
		              </table>
		              <table border="0" bgcolor="#000000" bordercolor="#000000" width="100%">
		              <tr height="40">
		                 <td border="0" width="22%" bgcolor="#C0C0C0" align="center" style="font-size: 8pt">Exclusividade<br/>Válida até</td>
		                 <td border="0" width="12%" bgcolor="#C0C0C0" align="center" style="font-size: 8pt">Fls.</td>
		                 <td border="0" width="23%" bgcolor="#C0C0C0" align="center" style="font-size: 8pt">Doc.<br/>Representante</td>
		                 <td border="0" width="12%" bgcolor="#C0C0C0" align="center" style="font-size: 8pt">Fls.</td>
		                 <td border="0" width="21%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <c:forEach var="x" begin="0" end="7">
		                 <tr height="40">           
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 </tr>
		              </c:forEach>
		              </table>   
		 
		           </table>
		        </td>
		     </tr>
		  </table>	
		      </c:when>		   
		   
<%-- Início formulário Contrato com Exclusividade  --%>

		      <c:when test="${doc.exModelo.idMod=='565'}">
		  <table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" bordercolor="#000000" width="100%">
		     <tr>
		        <td border="0" bgcolor="#FFFFFF" align="center" width="50%">
		        <%-- Trecho substituído devido às alterações feitas nos formulários  
		           <table border="0" bgcolor="#000000" bordercolor="#FFFFFF" width="100%">
		              <tr bgcolor="#C0C0C0"" height="40">
		                 <td border="0" width="40%" align="center" style="font-size: 9pt">Nota de Empenho<br/>&nbsp</td>
		                 <td border="0" width="20%" align="center" style="font-size: 9pt">Tipo<br/>&nbsp</td>
		                 <td border="0" width="20%" align="center" style="font-size: 9pt">Data<br/>&nbsp</td>
		                 <td border="0" width="20%" align="center" style="font-size: 9pt">Valor (R$)&nbsp</td>
		              </tr>
		              <c:forEach var="x" begin="0" end="7">
		                 <tr height="40">           
		                    <td border="0" width="25%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="25%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="25%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="25%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 </tr>
		              </c:forEach>
		           </table> --%>
		           <table border="0" bgcolor="#000000" bordercolor="#FFFFFF" width="100%">
		              <tr bgcolor="#C0C0C0"" height="40">
		                 <td border="0" width="38%" align="center" style="font-size: 9pt">Nota de Empenho<br/>&nbsp</td>
		                 <td border="0" width="17%" align="center" style="font-size: 9pt">Tipo<br/>&nbsp</td>
		                 <td border="0" width="17%" align="center" style="font-size: 9pt">Data<br/>&nbsp</td>
		                 <td border="0" width="17%" align="center" style="font-size: 9pt">Valor <br/>(R$)&nbsp</td>
		                 <td border="0" width="11%" align="center" style="font-size: 9pt">Fls.</td>
		              </tr>
		              <c:forEach var="x" begin="0" end="7">
		                 <tr height="40">           
		                    <td border="0" width="38%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="17%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="17%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="17%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="11%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 </tr>
		              </c:forEach>
		           </table>
		           <table border="0" bgcolor="#000000" bordercolor="#000000" width="100%">
		              <tr height="40" bgcolor="#C0C0C0">
		                 <td border="0" width="20%" align="center">&nbsp</td>
		                 <td border="0" width="19%" align="center" style="font-size: 8pt">Vigência</td>
		                 <td border="0" width="12%"  align="center" style="font-size: 8pt">Fls.</td>
		                 <td border="0" width="13%" align="center" style="font-size: 8pt">Public.<br/>Fls.</td>
		                 <td border="0" width="18%" align="center" style="font-size: 8pt">SIAFI</td>
		                 <td border="0" width="18%" align="center" style="font-size: 8pt">SIASG</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Contrato Nº</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Garantia</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Termo<br/> Aditivo Nº 1</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Garantia</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Termo<br/> Aditivo Nº 2</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Garantia</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Termo<br/> Aditivo Nº 3</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Garantia</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		           </table>
		        </td>
		        <td border="0" bgcolor="#FFFFFF" align="center" width="50%">
		           <table border="0" bgcolor="#000000" bordercolor="#000000" width="100%">
		              <tr height="40" bgcolor="#C0C0C0">
		                 <td border="0" width="22%" align="center" style="font-size: 9pt">CND<br/>Válida até</td>
		                 <td border="0" width="12%" align="center" style="font-size: 9pt">Fls.</td>
		                 <td border="0" width="23%" align="center" style="font-size: 9pt">CCN<br/>Válida até</td>
		                 <td border="0" width="12%" align="center" style="font-size: 9pt">Fls.</td>
		                 <td border="0" width="21%" align="center" style="font-size: 9pt">CRF<br/>Válida até</td>
		                 <td border="0" width="12%" align="center" style="font-size: 9pt">Fls.</td>
		              </tr>
		              <c:forEach var="x" begin="0" end="7">
		                 <tr height="40">           
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 </tr>
		              </c:forEach>   
		              </table>
		              <table border="0" bgcolor="#000000" bordercolor="#000000" width="100%">
		              <tr height="40">
		                 <td border="0" width="22%" bgcolor="#C0C0C0" align="center" style="font-size: 8pt">Exclusividade<br/>Válida até</td>
		                 <td border="0" width="12%" bgcolor="#C0C0C0" align="center" style="font-size: 8pt">Fls.</td>
		                 <td border="0" width="23%" bgcolor="#C0C0C0" align="center" style="font-size: 8pt">Doc.<br/>Representante</td>
		                 <td border="0" width="12%" bgcolor="#C0C0C0" align="center" style="font-size: 8pt">Fls.</td>
		                 <td border="0" width="21%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <c:forEach var="x" begin="0" end="7">
		                 <tr height="40">           
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 </tr>
		              </c:forEach>
		              </table>   
		 
		           </table>
		        </td>
		     </tr>
		  </table>	
		      </c:when>
		      
		      <c:when test="${doc.exModelo.idMod=='566'}">
<%-- Início formulário Contrato de prestação continuada  --%>
		  <table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" bordercolor="#000000" width="100%">
		     <tr>
		        <td border="0" bgcolor="#FFFFFF" align="center" width="50%">
		        <table border="0" bgcolor="#000000" bordercolor="#FFFFFF" width="100%">
		              <tr bgcolor="#C0C0C0"" height="40">
		                 <td border="0" width="38%" align="center" style="font-size: 9pt">Nota de Empenho<br/>&nbsp</td>
		                 <td border="0" width="17%" align="center" style="font-size: 9pt">Tipo<br/>&nbsp</td>
		                 <td border="0" width="17%" align="center" style="font-size: 9pt">Data<br/>&nbsp</td>
		                 <td border="0" width="17%" align="center" style="font-size: 9pt">Valor <br/>(R$)&nbsp</td>
		                 <td border="0" width="11%" align="center" style="font-size: 9pt">Fls.</td>
		              </tr>
		              <c:forEach var="x" begin="0" end="7">
		                 <tr height="40">           
		                    <td border="0" width="38%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="17%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="17%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="17%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="11%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 </tr>
		              </c:forEach>
		           </table>
		        <%--  Trecho substituído devido às alterações no formulário.
		           <table border="0" bgcolor="#000000" bordercolor="#FFFFFF" width="100%">
		              <tr bgcolor="#C0C0C0"" height="40">
		                 <td border="0" width="40%" align="center" style="font-size: 9pt">Nota de Empenho<br/>&nbsp</td>
		                 <td border="0" width="20%" align="center" style="font-size: 9pt">Tipo<br/>&nbsp</td>
		                 <td border="0" width="20%" align="center" style="font-size: 9pt">Data<br/>&nbsp</td>
		                 <td border="0" width="20%" align="center" style="font-size: 9pt">Valor (R$)<br/>&nbsp</td>
		              </tr>
		              <c:forEach var="x" begin="0" end="7">
		                 <tr height="40">           
		                    <td border="0" width="25%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="25%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="25%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="25%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 </tr>
		              </c:forEach>
		           </table> --%>
		           <table border="0" bgcolor="#000000" bordercolor="#000000" width="100%">
		              <tr height="40" bgcolor="#C0C0C0">
		                 <td border="0" width="20%" align="center">&nbsp</td>
		                 <td border="0" width="19%" align="center" style="font-size: 9pt"><b>Vigência</b></td>
		                 <td border="0" width="12%"  align="center" style="font-size: 9pt"><b>Fls.</b></td>
		                 <td border="0" width="13%" align="center" style="font-size: 9pt"><b>Public.<br/>Fls.</b></td>
		                 <td border="0" width="18%" align="center" style="font-size: 9pt"><b>SIAFI</b></td>
		                 <td border="0" width="18%" align="center" style="font-size: 9pt"><b>SIASG</b></td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Contrato Nº</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Garantia</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Termo<br/> Aditivo Nº 1</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Garantia</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Termo<br/> Aditivo Nº 2</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Garantia</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Termo<br/> Aditivo Nº 3</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0"  bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Garantia</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0"  bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		           </table>
		        </td>
		        <td border="0" bgcolor="#FFFFFF" align="center" width="50%">
		           <table border="0" bgcolor="#000000" bordercolor="#000000" width="100%">
		              <tr height="40" bgcolor="#C0C0C0">
		                 <td border="0" width="22%" align="center" style="font-size: 9pt">CND<br/>Válida até</td>
		                 <td border="0" width="12%" align="center" style="font-size: 9pt">Fls.</td>
		                 <td border="0" width="21%" align="center" style="font-size: 9pt">CCN<br/>Válida até</td>
		                 <td border="0" width="12%" align="center" style="font-size: 9pt">Fls.</td>
		                 <td border="0" width="21%" align="center" style="font-size: 9pt">CRF<br/>Válida até</td>
		                 <td border="0" width="12%" align="center" style="font-size: 9pt">Fls.</td>
		              </tr>
		              <c:forEach var="x" begin="0" end="7">
		                 <tr height="40">           
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 </tr>
		              </c:forEach>   
		              </table>
		              <table border="0" bgcolor="#000000" bordercolor="#000000" width="100%">
		              <tr height="40">
		                 <td border="0" width="22%" bgcolor="#FFFFFF" align="center" style="font-size: 8pt"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center" style="font-size: 8pt">&nbsp</td>
		                 <td border="0" width="21%" bgcolor="#FFFFFF" align="center" style="font-size: 8pt"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center" style="font-size: 8pt">&nbsp</td>
		                 <td border="0" width="21%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <c:forEach var="x" begin="0" end="7">
		                 <tr height="40">           
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 </tr>
		              </c:forEach>
		              </table>   
		 
		           </table>
		        </td>
		     </tr>
		  </table>	
		      </c:when>
		      <c:when test="${doc.exModelo.idMod=='567'}">	 
<%-- Início formulário Ata de Registro de Preços com Termo de Contrato --%>        
		  <table border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" bordercolor="#000000" width="100%">
		     <tr>
		        <td border="0" bgcolor="#FFFFFF" align="center" width="50%">
		           <table border="0" bgcolor="#000000" bordercolor="#FFFFFF" width="100%">
		              <tr bgcolor="#C0C0C0"" height="40">
		                 <td border="0" width="38%" align="center" style="font-size: 9pt">Nota de Empenho<br/>&nbsp</td>
		                 <td border="0" width="17%" align="center" style="font-size: 9pt">Tipo<br/>&nbsp</td>
		                 <td border="0" width="17%" align="center" style="font-size: 9pt">Data<br/>&nbsp</td>
		                 <td border="0" width="17%" align="center" style="font-size: 9pt">Valor <br/>(R$)&nbsp</td>
		                 <td border="0" width="11%" align="center" style="font-size: 9pt">Fls.</td>
		              </tr>
		              <c:forEach var="x" begin="0" end="6">
		                 <tr height="40">           
		                    <td border="0" width="38%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="17%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="17%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="17%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="11%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 </tr>
		              </c:forEach>
		           </table>
		           <table border="0" bgcolor="#000000" bordercolor="#000000" width="100%">
		              <tr height="40" bgcolor="#C0C0C0">
		                 <td border="0" width="32%" align="center">&nbsp</td>
		                 <td border="0" width="20%" align="center" style="font-size: 9pt"><b>Vigência</b></td>
		                 <td border="0" width="12%"  align="center" style="font-size: 9pt"><b>Fls.</b></td>
		                 <td border="0" width="13%" align="center" style="font-size: 9pt"><b>Public.<br/>Fls.</b></td>
		                 <td border="0" width="12%" align="center" style="font-size: 9pt"><b>SIAFI</b></td>
		                 <td border="0" width="14%" align="center" style="font-size: 9pt"><b>SIASG</b></td>
		              </tr>
		              <tr height="40">
		                 <td border="0" width="32%" bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Ata Nº</td>
		                 <td border="0" width="20%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0" width="32%" bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Contrato Nº</td>
		                 <td border="0" width="20%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0" width="32%" bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Garantia</td>
		                 <td border="0" width="20%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0" width="32%" bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Contrato Nº</td>
		                 <td border="0" width="20%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0" width="32%" bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Garantia</td>
		                 <td border="0" width="20%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0" width="32%" bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Contrato Nº</td>
		                 <td border="0" width="20%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0" width="32%" bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Garantia</td>
		                 <td border="0" width="20%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0" width="32%" bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Contrato Nº</td>
		                 <td border="0" width="20%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <tr height="40">
		                 <td border="0" width="32%" bgcolor="#FFFFFF" align="center" style="font-size: 8pt">Garantia</td>
		                 <td border="0" width="20%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		           </table>
		        </td>
		        <td border="0" bgcolor="#FFFFFF" align="center" width="50%">
		           <table border="0" bgcolor="#000000" bordercolor="#000000" width="100%">
		              <tr height="40" bgcolor="#C0C0C0">
		                 <td border="0" width="21%" align="center" style="font-size: 9pt">CND<br/>Válida até</td>
		                 <td border="0" width="11%" align="center" style="font-size: 9pt">Fls.</td>
		                 <td border="0" width="25%" align="center" style="font-size: 9pt">CCN<br/>Válida até</td>
		                 <td border="0" width="12%" align="center" style="font-size: 9pt">Fls.</td>
		                 <td border="0" width="20%" align="center" style="font-size: 9pt">CRF<br/>Válida até</td>
		                 <td border="0" width="11%" align="center" style="font-size: 9pt">Fls.</td>
		              </tr>
		              <c:forEach var="x" begin="0" end="6">
		                 <tr height="40">           
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 </tr>
		              </c:forEach>   
		              </table>
		              <table border="0" bgcolor="#000000" bordercolor="#000000" width="100%">
		              <tr height="40">
		                 <td border="0" width="21%" bgcolor="#FFFFFF" align="center" style="font-size: 8pt"><br/>&nbsp</td>
		                 <td border="0" width="11%" bgcolor="#FFFFFF" align="center" style="font-size: 8pt">&nbsp</td>
		                 <td border="0" width="25%" bgcolor="#FFFFFF" align="center" style="font-size: 8pt"><br/>&nbsp</td>
		                 <td border="0" width="12%" bgcolor="#FFFFFF" align="center" style="font-size: 8pt">&nbsp</td>
		                 <td border="0" width="20%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 <td border="0" width="11%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		              </tr>
		              <c:forEach var="x" begin="0" end="8">
		                 <tr height="40">           
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                    <td border="0" width="0%" bgcolor="#FFFFFF" align="center"><br/>&nbsp</td>
		                 </tr>
		              </c:forEach>
		              </table>   
		 
		           </table>
		        </td>
		     </tr>
		  </table>	
		      </c:when>
		      <c:when test="${doc.exModelo.idMod=='568'}">
<%-- Início formulário Ata de Registro de Preço sem Termo de Contrato  --%>
            
		          <table bgcolor="#000000" width="100%">
		              <tr height="30">
			             <td border="0"  bgcolor="#C0C0C0" width="44%"  align="center" style="font-size: 9pt"><b>Solicitação de Empenho</b></td>
			             <td border="0"  bgcolor="#C0C0C0" width="5%"  align="center" style="font-size: 9pt"><b>Fls.</b></td>
			             <td border="0"  bgcolor="#C0C0C0" width="12%"  align="center" style="font-size: 9pt"><b>CND<br/>Válida até</b></td>
			             <td border="0"  bgcolor="#C0C0C0" width="5%"   align="center" style="font-size: 9pt"><b>Fls.</b></td>
			             <td border="0"  bgcolor="#C0C0C0" width="12%"  align="center" style="font-size: 9pt"><b>CCN<br/>Válida até</b></td>
			             <td border="0"  bgcolor="#C0C0C0" width="5%"   align="center" style="font-size: 9pt"><b>Fls.</b></td>
			             <td border="0"  bgcolor="#C0C0C0" width="12%"  align="center" style="font-size: 9pt"><b>CRF<br/>Válida até</b></td>
			             <td border="0"  bgcolor="#C0C0C0" width="5%"   align="center" style="font-size: 9pt"><b>Fls.</b></td> 
			          </tr>
			           <c:forEach var="x" begin="0" end="5"> 
			              <tr height="30">
		                       <c:forEach var="x" begin="0" end="7">  
			                      <td border="0"  bgcolor="#FFFFFF" align="center">&nbsp<br/><br/></td>
			                   </c:forEach>
			              </tr>  
			           </c:forEach>  
			       </table>
			     
			       <table  bgcolor="#000000" width="100%">
			           <tr height="30">
			             <td border="0"  bgcolor="#C0C0C0"   align="center" style="font-size: 9pt" width="17%"><b>Nota de Empenho</b><br/><br/></td>
			             <td border="0"  bgcolor="#C0C0C0"   align="center" style="font-size: 9pt" width="9%"><b>Tipo</b><br/><br/></td>
			             <td border="0"  bgcolor="#C0C0C0"   align="center" style="font-size: 9pt" width="9%" ><b>Data</b><br/><br/></td>
			             <td border="0"  bgcolor="#C0C0C0"   align="center" style="font-size: 9pt" width="9%"><b>Valor (R$)</b><br/><br/></td>
			             <td border="0"  bgcolor="#C0C0C0"   align="center" style="font-size: 9pt" width="5%"><b>Fls.</b><br/><br/></td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center" width="12%" >&nbsp</td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center" width="5%" >&nbsp</td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center" width="12%">&nbsp</td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center" width="5%">&nbsp</td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center" width="12%">&nbsp</td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center" width="5%">&nbsp</td>
			           </tr>
			           <c:forEach var="x" begin="0" end="4"> 
			              <tr height="30">
			                 <c:forEach var="x" begin="0" end="10">  
			                    <td border="0"  bgcolor="#FFFFFF" align="center">&nbsp<br/><br/></td>
			                 </c:forEach>
			              </tr>
			           </c:forEach>
			        </table>
			     
			        <table bgcolor="#000000" width="100%">
			           <tr height="30">
			             <td border="0"  bgcolor="#C0C0C0"   align="center" width="15%">&nbsp</td>
			             <td border="0"  bgcolor="#C0C0C0"   align="center" style="font-size: 9pt" width="16%"><b>Vigência</b></td>
			             <td border="0"  bgcolor="#C0C0C0"   align="center" style="font-size: 9pt" width="5%"><b>Fls.</b></td>
			             <td border="0"  bgcolor="#C0C0C0"   align="center" style="font-size: 9pt" width="13%"><b>Public.<br/>Fls.</b></td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center" width="12%" >&nbsp</td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center" width="5%" >&nbsp</td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center" width="12%">&nbsp</td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center" width="5%">&nbsp</td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center" width="12%">&nbsp</td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center" width="5%">&nbsp</td> 
			           </tr>
			           
			           <tr height="30">
			                <td border="0"  bgcolor="#FFFFFF"   align="center" style="font-size: 9pt">Ata Nº<br/><br/></td>
			       			<td border="0"  bgcolor="#FFFFFF"   align="center">&nbsp<br/></td>
			             	<td border="0"  bgcolor="#FFFFFF"   align="center">&nbsp<br/></td>
			         		<td border="0"  bgcolor="#FFFFFF"   align="center" >&nbsp<br/></td>
			         		<c:forEach var="x" begin="0" end="5">
			         			<td border="0"  bgcolor="#FFFFFF"   align="center">&nbsp<br/></td>
			         		</c:forEach>
			           </tr>
			           <tr height="30">
			             <td border="0"  bgcolor="#FFFFFF"   align="center" style="font-size: 9pt">Termo Aditivo Nº<br/><br/></td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center">&nbsp<br/></td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center">&nbsp<br/></td>
			         	 <td border="0"  bgcolor="#FFFFFF"   align="center">&nbsp<br/></td>
			             <c:forEach var="x" begin="0" end="5">
			             	<td border="0"  bgcolor="#FFFFFF"   align="center">&nbsp</td>
			              </c:forEach>
			           </tr>
			           <tr height="30">
			             <td border="0"  bgcolor="#FFFFFF"   align="center" style="font-size: 9pt">Termo Aditivo Nº<br/><br/></td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center">&nbsp<br/></td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center">&nbsp<br/></td>
			         	 <td border="0"  bgcolor="#FFFFFF"   align="center">&nbsp<br/></td>
			             <c:forEach var="x" begin="0" end="5">
			             	<td border="0"  bgcolor="#FFFFFF"  align="center" >&nbsp</td>
			              </c:forEach>
			           </tr>
			           <tr height="30">
			             <td border="0"  bgcolor="#FFFFFF"   align="center" style="font-size: 9pt">Termo Aditivo Nº<br/><br/></td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center">&nbsp<br/></td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center">&nbsp<br/></td>
			         	 <td border="0"  bgcolor="#FFFFFF"   align="center">&nbsp<br/></td>
			             <c:forEach var="x" begin="0" end="5">
			             	<td border="0"  bgcolor="#FFFFFF" align="center">&nbsp</td>
			             </c:forEach>
			           </tr>
			   		  </table>
		      </c:when>
		      <c:otherwise>
		      </c:otherwise>
		   </c:choose>
		
		    
				<p align="center">Certifico que, nesta data, iniciei o volume ${mob.numSequencia} do processo em epígrafe.</p>
				<p align="center" >${doc.dtExtenso}</p>
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


