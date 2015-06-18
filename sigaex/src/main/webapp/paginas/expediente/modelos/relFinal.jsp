<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="">
			<mod:grupo>
				<mod:texto titulo="Forma de Endereçamento" var="formaDeEnderecamento" largura="80" valor="Ao Exmo. Sr. Diretor do Foro"/>
			</mod:grupo>
		<%-- 	<mod:grupo>
				<mod:texto titulo="Nome do órgão destinatário" var="orgao_dest" largura="30" valor="${orgao_dest_prov}" /> &nbsp&nbsp&nbsp <mod:selecao titulo="Gênero do Destinatário"
				var="genero" opcoes="Feminino;Masculino"/>
			</mod:grupo> --%>
		</mod:grupo>
		<mod:grupo titulo="Texto a ser inserido no corpo da pauta">
			<mod:grupo>
				<mod:editor titulo="" var="texto" />
			</mod:grupo>
		</mod:grupo>
		<mod:selecao
			titulo="Tamanho da letra"
			var="tamanhoLetra" opcoes="Normal;Pequeno;Grande"/>
	</mod:entrevista>
	<mod:documento>
		<c:if test="${tamanhoLetra=='Normal'}"><c:set var="tl" value="11pt"/></c:if>
		<c:if test="${tamanhoLetra=='Pequeno'}"><c:set var="tl" value="9pt"/></c:if>
		<c:if test="${tamanhoLetra=='Grande'}"><c:set var="tl" value="13pt"/></c:if>
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
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<br />
					<table width="100%">
						<tr>
							<td align="center"><p style="font-family:Arial;font-size:11pt;font-weight: bold;"><br/>Relatório ${fn:toUpperCase(tipoDePauta)} N&ordm; ${doc.codigo}</p></td>	
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->
		<!-- INICIO CABECALHO
				<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizado.jsp" />
				<br/><br/>
			FIM CABECALHO -->
		<mod:letra tamanho="${tl}">	
		<br />
	<%-- 	<c:if test="${not empty orgao_dest}">
			<c:if test="${genero=='Feminino'}">
				<p>À ${orgao_dest}</p>
			</c:if>
				<c:if test="${genero=='Masculino'}">
				<p>Ao ${orgao_dest}</p>
				</c:if>
		</c:if>  --%>

		<br />
		<br />
		${formaDeEnderecamento}
		<br />
		<br />
		<span style="font-size:${tl};">
		${texto}
		</span>
		<br />
		<br />
		<p align="center" style="TEXT-INDENT: 0 cm">${doc.dtExtenso}</p>
		<br />
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />

		<!-- INICIO PRIMEIRO RODAPE
			<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
			FIM PRIMEIRO RODAPE -->
		</mod:letra>	
		</body>
		</html>
	</mod:documento>
</mod:modelo>
