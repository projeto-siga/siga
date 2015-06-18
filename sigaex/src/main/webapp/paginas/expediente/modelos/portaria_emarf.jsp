<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib uri="http://localhost/modelostag" prefix="mod"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
              	<c:if test="${empty esconderTexto}">
			<mod:grupo
				titulo="Texto a ser inserido no corpo da Portaria da EMARF">
				<mod:grupo>
					<mod:editor titulo="" var="texto_pte" />
				</mod:grupo>  
			</mod:grupo>
		</c:if>
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
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPaginaEmarf.jsp" />
		</td></tr>
		
		<tr><td><br/>&nbsp;<br/></td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr>
							<td align="center"><p style="font-family:Arial;font-weight:bold;font-size:11pt;">Portaria N&ordm; ${doc.codigo} DE ${doc.dtExtensoMaiusculasSemLocalidade}</p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->

		<mod:letra tamanho="${tl}">
		    <!-- INICIO MIOLO -->
			<!-- INICIO CORPO -->
			<br>
			<span style="font-size: ${tl};line-height: 1px"> ${texto_pte} </span>
			<!-- FIM CORPO -->
			<p align="center"><!-- INICIO FECHO -->PUBLIQUE-SE. REGISTRE-SE. CUMPRA-SE.<!-- FIM FECHO --><br/><br/><br></p>
			<!-- FIM MIOLO -->
			<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
		</mod:letra>
		/${doc.cadastrante.siglaPessoa}</br>
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