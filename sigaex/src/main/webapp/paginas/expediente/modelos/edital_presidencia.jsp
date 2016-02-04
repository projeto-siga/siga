<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<c:if test="${empty esconderTexto}">
			<mod:grupo
				titulo="Texto a ser inserido no corpo do Edital da Presidência">
				<mod:grupo>
					<mod:editor titulo="" var="texto_edital" />
				</mod:grupo>
			</mod:grupo>
		</c:if>
		<br></br>
		<mod:grupo>
			<mod:texto titulo="<b>Título do Edital</b>" var="tit_edital"
				largura="60" />
		</mod:grupo>
		<br></br>
		<mod:grupo>
			<mod:texto titulo="<b>Sub-Título</b>" var="subtit_edital"
				largura="40" />
		</mod:grupo>
		<br></br>
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
	margin-left: 3cm;
	margin-right: 2cm;
	margin-top: 1cm;
	margin-bottom: 2cm;
}
</style>
		</head>
		<body>

		<c:if test="${empty tl}">
			<c:set var="tl" value="11pt"></c:set>
		</c:if>

                
        <!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0"  bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
				<br/><br/>
					<table width="100%" border="0" >
						<tr>
							<td align="center"><p style="font-family:Arial;font-size:11pt;font-weight:bold;" >EDITAL N&ordm; ${doc.codigo} DE ${doc.dtExtensoMaiusculasSemLocalidade}</p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->
        <!-- <p align="center" style="font-family:Arial;font-size:11pt;"><span style="font-weight: bold;">EDITAL N&ordm; ${doc.codigo}</span> DE ${doc.dtExtensoMaiusculasSemLocalidade}</p> -->
        
		<!-- INICIO TITULO 
			<mod:letra tamanho="${tl}">
				<p align="center" style="font-family:Arial;font-size:11pt;"><span style="font-weight: bold;">EDITAL N&ordm; ${doc.codigo} DE ${doc.dtExtensoMaiusculasSemLocalidade}</span></p>
			</mod:letra>
		FIM TITULO -->

		<!-- INICIO NUMERO <span style="font-weight: bold;">EDITAL N&ordm; ${doc.codigo} DE ${doc.dtExtensoMaiusculasSemLocalidade}</span> FIM NUMERO -->	
		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->
        <mod:letra tamanho="${tl}">
        	<!-- INICIO MIOLO -->
			<!-- INICIO CORPO -->
			<p align="center"><b>${tit_edital}</b><br><b>${subtit_edital}</b></p>
			<span style="font-size: ${tl};line-height: 1px">${texto_edital}</span>
			<!-- FIM CORPO -->
			<br>
			<p align="center"><!-- INICIO FECHO -->PUBLIQUE-SE. REGISTRE-SE. CUMPRA-SE.<!-- FIM FECHO --><br/><br/><br></p>
			<!-- INICIO ASSINATURA -->
			<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />   
			<!-- FIM ASSINATURA -->
            <!-- FIM MIOLO -->
		</mod:letra>
        <br>/${doc.cadastrante.siglaPessoa}</br>
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