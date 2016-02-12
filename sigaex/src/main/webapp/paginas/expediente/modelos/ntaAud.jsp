<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
	    <br>
	    <mod:grupo
	        titulo="Elementos essenciais da Nota de Auditoria"><br>
	        <mod:lotacao titulo="<b>Divisão / Secretaria</b>" var="divsec" />
	        <mod:lotacao titulo="<b>Seção</b>" var="secao" />
	    </mod:grupo>
	    <br><br>
		<mod:grupo>
				<mod:selecao var="uniges"
				titulo="<b>Unidade Gestora Auditada</b>"
				opcoes="TRIBUNAL REGIONAL FEDERAL DA 2ª REGIÃO - UG 090028;SEÇÃO JUDICIÁRIA DO ESTADO DO RIO DE JANEIRO - UG 090016;SEÇÃO JUDICIÁRIA DO ESTADO DO ESPÍRITO SANTO - UG 090014"	
				reler="sim" />
		</mod:grupo>
	  	
	  	<mod:grupo
	  	        titulo=""><br>
				<mod:lotacao titulo="<b>Setor Responsável</b>" var="secres" />
		</mod:grupo>
		 <br><hr style="color: #FFFFFF;" /><br>
		<mod:grupo>
		    <c:if test="${empty esconderTexto}">
			    <mod:grupo titulo="<b>Texto da Nota de Auditoria (Constatação, Fato, Consequência, Recomendação)</b>">
			    	<mod:grupo>
					    <mod:editor titulo="" var="texto_audit" />
				    </mod:grupo>
			    </mod:grupo>
			    <br>
			    <mod:selecao titulo="<b>Tamanho da letra</b>" var="tamanhoLetra"
				   opcoes="Normal;Pequeno;Grande" />
	    	</c:if>
		</mod:grupo>
		
		<hr style="color: #FFFFFF;" />
		<mod:grupo>  
		     <mod:data titulo="Prazo para atendimento" var="dtprazo" obrigatorio="Sim"/>
        </mod:grupo><br>	
        
	</mod:entrevista>
	
	<mod:documento>
		<c:if test="${tamanhoLetra=='Normal'}">
			<c:set var="tl" value="11pt" />
		</c:if>
		<c:if test="${tamanhoLetra=='Pequeno'}">
			<c:set var="tl" value="9pt" />
		</c:if>
		<c:if test="${tamanhoLetra=='Grande'}">
			<c:set var="tl" value="13pt" />
		</c:if>
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
@page {
	margin-left: 0.5cm;
	margin-right: 0.5cm;
	margin-top: 0.5cm;
	margin-bottom: 0.5cm;
}
</style>
		</head>
		<body>

		<c:if test="${empty tl}">
			<c:set var="tl" value="11pt"></c:set>
		</c:if>
        <c:set var="divis" value="${requestScope['divsec_lotacaoSel.sigla']}" />
        <c:set var="subdivis" value="${requestScope['secao_lotacaoSel.sigla']}" />
        
        <c:set var="tuc" value="${requestScope['secres_lotacaoSel.descricao']}" />
        <c:set var="setresp" value="${f:maiusculasEMinusculas(tuc)}" />
        <c:set var="setrespsigla" value="${requestScope['secres_lotacaoSel.sigla']}" />
		
				
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
	    <tr><td align="center"><p style="font-family:Arial;font-weight:bold;font-size:10pt;"><br>SECRETARIA DE CONTROLE INTERNO</p></td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr>
							<td align="center">&nbsp;</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->     
	    <br> 	
	  	<table width="100%" border="1" style="font-size:9" cellpadding="2" cellspacing="1">
			<tr>
				<td>
		
		<table border="1" bgcolor="#FFFFFF" cellpadding="1" width="100%" align="left" >
	  		   <tr>
  				   <td colspan="2" style="font-size:9" align="justify" ><b>NOTA DE AUDITORIA N&ordm; ${doc.codigo} - ${divis}/${subdivis} DE ${doc.dtExtensoMaiusculasSemLocalidade}</b></td>
  			   </tr>
  			   <tr>
  				   <td colspan="2" style="font-size:9" align="justify" ><b>UNIDADE GESTORA AUDITADA : ${uniges}</b></td>
  			   </tr>
  			   <tr>
  				   <td colspan="2" style="font-size:9" align="justify" ><b>SETOR RESPONSÁVEL : ${setresp} - ${setrespsigla}</b></td>
  			   </tr>
  		</table>
		</td>
		</tr>
		</table>
			
	    
		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->

		<mod:letra tamanho="${tl}">
		    <!-- INICIO MIOLO -->
			<!-- INICIO CORPO -->
			<br>
			${texto_audit}
			<br>
			<!-- FIM CORPO -->
			<!-- INICIO FECHO -->
			<p align="left"><b>Prazo para atendimento:</b> ${dtprazo}</p>
			
			<br><br>
			<!-- FIM FECHO -->
			<!-- FIM MIOLO -->
			<!-- INICIO ASSINATURA -->
			<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
			<!-- FIM ASSINATURA -->
		</mod:letra>
		/${doc.cadastrante.siglaPessoa}<br>
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