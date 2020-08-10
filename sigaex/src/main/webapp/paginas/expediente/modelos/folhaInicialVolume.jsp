

<%-- Este arquivo referia-se ao formulário Folha Incicial de Volume dos processos administrativos de Execução
Orçamentária Financeira (EOF). Este formulário foi desmembrado em quatro novos modelos. Criei o
arquivo folhaInicialVolumeEof.jsp , que contém os novos quatro modelos. --%>



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
	               margin-right: 2.1 cm;
	               margin-top: 0.5 cm;
	               margin-bottom: 2 cm;
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
				          <p style="font-family: AvantGarde Bk BT, Arial; font-size: 11pt;">${f:resource('modelos.cabecalho.titulo')}</p>
				          </td>
			          </tr>
			          <c:if test="${not empty f:resource('modelos.cabecalho.subtitulo')}">
				          <tr>
					          <td width="100%" align="left">
					          <p style="font-family: Arial; font-size: 10pt; font-weight: bold;">${f:resource('modelos.cabecalho.subtitulo')}</p>
					          </td>
				          </tr>
			          </c:if>
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
		         <td td width="59%" align="center"><p align="right" style=" font: bold; font-size: 16pt"><b>PROCESSO ADMINISTRATIVO</b></td>
	          </tr>
           </table></br>
           
		  <table border="0"  cellpadding="0" cellspacing="0" width=100%>
		    <tr>
		       <td width= 55%>
		          <table border="1" width=100%  bordercolor="#000000"  bgcolor="#000000" align="left">
		             <tr> 
			            <td border="0"  width=40% bgcolor="#FFFFFF" align="center"><b>Gestor SJRJ<br/><br/></b></td>
			            <td border="0"  width=60% bgcolor="#FFFFFF">&nbsp</td>
		             </tr>
		             <tr> 	
			             <td border="0" width=40% bgcolor="#FFFFFF" align="center"><b>Gestor Contratada<br/><br/></b></td>
		                 <td border="0" width=60% bgcolor="#FFFFFF">&nbsp</td>	
		             </tr>
		          </table>
		       </td>
		       <td width=5%>
		       </td>
		       <td>
		          <table border="1" width=100% bordercolor="#000000" bgcolor="#000000" align="right">
		             <tr> 
			           <td  border="0"  width=60% bgcolor="#FFFFFF"  align="center"><b>Processo Nº</b><br/><br/></td>
			           <td  border="0"  width=20% bgcolor="#FFFFFF"  align="center"><b>Ap.</b></td>
			           <td  border="0"  width=20% bgcolor="#FFFFFF"  align="center"><b>Vol.</b></td>
		             </tr>
		             <tr> 	
			            <td border="0" bgcolor="#FFFFFF" align="center"><b>EOF ${doc.codigo}</b><br/><br/></td>
		                <td border="0" bgcolor="#FFFFFF">&nbsp</td>
			            <td border="0" bgcolor="#FFFFFF">&nbsp</td>
		             </tr>
		          </table>
		       </td width=40%>
		    </tr>
		  </table>
		  <br/>
		  <table border="1" width=100% bordercolor="#000000" bgcolor="#000000">
		    <tr> 
			   <td border="0" width=20% bgcolor="#FFFFFF"  align="center"><b>OBJETO</b><br/><br/></td>
			   <td border="0" width=80% bgcolor="#FFFFFF">&nbsp</td>
		    </tr>
		    <tr> 	
			   <td border="0" bgcolor="#FFFFFF" align="center"><b>BASE LEGAL</b><br/><br/></td>
		       <td border="0" bgcolor="#FFFFFF" >&nbsp</td>
		    </tr>
		  </table><br/>
		           <table border="1"  width=100% bgcolor="#000000">
		              <tr>
			             <td border="0"  bgcolor="#FFFFFF" width=35% align="center" colspan="2">Empresa Contratada</td>
			             <td border="0"  bgcolor="#FFFFFF" width=5%  align="center">NE</td>
			             <td border="0"  bgcolor="#FFFFFF" width=10% align="center">Data</td>
			             <td border="0"  bgcolor="#FFFFFF" width=10% align="center">Valor(R$)</td>
			             <td border="0"  bgcolor="#FFFFFF" width=10% align="center">% Retenção</td>
			             <td border="0"  bgcolor="#FFFFFF" width=10% align="center">CND Válida até</td>
			             <td border="0"  bgcolor="#FFFFFF" width=5%  align="center">Fls.</td>
			             <td border="0"  bgcolor="#FFFFFF" width=10% align="center">FGTS Válida até</td>
			             <td border="0"  bgcolor="#FFFFFF" width=5%  align="center">Fls.</td>
			          </tr>
			           <c:forEach var="x" begin="0" end="4"> 
			              <tr>
			                 <td border="0"  bgcolor="#FFFFFF" colspan="2">&nbsp
			                 </td>
		                        <c:forEach var="x" begin="0" end="7">  
			                       <td border="0"  bgcolor="#FFFFFF" align="center">&nbsp<br/><br/></td>
			                    </c:forEach>
			              </tr>  
			           </c:forEach>  
			       
			           <tr>
			             <td border="0"  bgcolor="#FFFFFF"   align="center">ATA Nº</td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center" >Vigência</td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center" >Contrato Fls.</td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center">Public. Fls.</td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center" >Garantia Fls.</td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center">Validade da Garantia</td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center">&nbsp</td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center">&nbsp</td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center">&nbsp</td>
			             <td border="0"  bgcolor="#FFFFFF"   align="center">&nbsp</td>
			           </tr>
			           <c:forEach var="x" begin="0" end="0"> 
			              <tr>
			                 <c:forEach var="x" begin="0" end="9">  
			                    <td border="0"  bgcolor="#FFFFFF" align="center">&nbsp<br/><br/></td>
			                 </c:forEach>
			              </tr>
			           </c:forEach>
			           <tr>
			             <td border="0"  bgcolor="#FFFFFF"  width=5% height="20" align="center">Termo aditivo Nº</td>
			             <td border="0"  bgcolor="#FFFFFF"  width=10% height="20" align="center">Vigência</td>
			             <td border="0"  bgcolor="#FFFFFF"  height="20" align="center">Contrato Fls.</td>
			             <td border="0"  bgcolor="#FFFFFF"  height="20" align="center">Public. Fls.</td>
			             <td border="0"  bgcolor="#FFFFFF"  height="20" align="center">Garantia Fls.</td>
			             <td border="0"  bgcolor="#FFFFFF"  align="center">Validade da Garantia</td>
			             <td border="0"  bgcolor="#FFFFFF"  align="center">C C D</td>
			             <td border="0"  bgcolor="#FFFFFF"  align="center">&nbsp</td>
			             <td border="0"  bgcolor="#FFFFFF"  align="center">&nbsp</td>
			             <td border="0"  bgcolor="#FFFFFF"  align="center">&nbsp</td>
			           </tr>
			           <c:forEach var="x" begin="0" end="5">
			              <tr>
			                  <c:forEach var="x" begin="0" end="9">  
			                    <td border="0"  bgcolor="#FFFFFF" align="center">&nbsp<br/><br/></td>
			                  </c:forEach>
			              </tr>    
			           </c:forEach>
			           <tr>
			             <td border="0"  bgcolor="#FFFFFF"  >&nbsp</td>
			             <td border="0"  bgcolor="#FFFFFF"  >&nbsp</td>
			             <td border="0"  bgcolor="#FFFFFF"  >&nbsp</td>
			             <td border="0"  bgcolor="#FFFFFF"  >&nbsp</td>
			             <td border="0"  bgcolor="#FFFFFF"  >&nbsp</td>
			             <td border="0"  bgcolor="#FFFFFF"  >&nbsp</td>
			             <td border="0"  bgcolor="#FFFFFF"  align="center" style=" font-size: 7pt"><b>Exclusividade<b/><br/><b>Válida até<b/></td>
			             <td border="0"  bgcolor="#FFFFFF"  align="center">Fls.</td>
			             <td border="0"  bgcolor="#FFFFFF"  align="center" style=" font-size: 7pt"><b>Doc. Representante<b/></td>
			             <td border="0"  bgcolor="#FFFFFF"  align="center">Fls.</td>
			           </tr>   
			           <c:forEach var="x" begin="0" end="3"> 
			              <tr>
			                 <c:forEach var="x" begin="0" end="9">  
			                    <td border="0"  bgcolor="#FFFFFF" align="center">&nbsp<br/><br/></td>
			                 </c:forEach>
			              </tr>
			           </c:forEach>
			       </table>
			 
				<p style="TEXT-INDENT: 2cm">Certifico que, nesta data, iniciei o volume ${mob.numSequencia} do processo em epígrafe.</p>
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

