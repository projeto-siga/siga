<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="esconderTexto" value="sim" scope="request" />

<mod:modelo
	urlBase="/paginas/expediente/modelos/portaria_presidencia.jsp">
	<mod:entrevista>
		<br>
		<mod:grupo>
			<mod:texto titulo="<b>Processo Administrativo</b>" var="proc"
				largura="15" />
			<mod:selecao titulo="<b>Tipo</b>" var="tipproc"
				opcoes="ADM;PES;Outro" reler="sim" />
		</mod:grupo>
		<br>

		<mod:grupo titulo="Juiz Federal Participante:">
			<mod:grupo>
				<mod:pessoa titulo="Nome" var="partic" />
			</mod:grupo>
			<br>
		</mod:grupo>
		<!-- 	    <mod:grupo titulo="Período">
				<mod:data titulo="De" var="dtin" />
				<mod:data titulo="a" var="dtfin" />
			</mod:grupo>    -->
		<br>
		<c:set var="nume" value="1" />
		<mod:grupo titulo="Dados do Curso">
			<br>
			<mod:texto titulo="Curso" var="curs" largura="40" />
			<mod:texto titulo="Cidade" var="cidad" largura="20" />
			<br>
			<br>
			<mod:selecao titulo="Acerto Gramatical" var="acgra"
				opcoes="em;no;na;nos;nas" reler="sim" />
			<mod:texto titulo="País" var="pais" largura="30" />
			<mod:texto titulo="Número de Dias" var="nume" largura="5" />
			<br>
			<br>
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Número de Dias por Extenso" var="diasext"
				largura="25" />
			<mod:data titulo="Data de Início de Afastamento" var="dtin" />
		</mod:grupo>
		<br>
		<br>

	</mod:entrevista>

	<mod:documento>
		<c:set var="juiz_federal"
			value="${f:pessoa(requestScope['partic_pessoaSel.id'])}" />
		<mod:valor var="texto_ptp">
			<br />
			<br />
			<p style="TEXT-INDENT: 2cm" align="justify"><b><c:choose>
				<c:when test="${doc.subscritor.sexo == 'M'}">O PRESIDENTE</c:when>
				<c:otherwise>A PRESIDENTE</c:otherwise>
			</c:choose> DO TRIBUNAL REGIONAL FEDERAL DA 2ª REGIÃO</b>, no uso de suas
			atribuições, e considerando o que consta nos autos do Processo
			Administrativo nº <c:choose>
				<c:when test="${tipproc == 'Outro'}">${proc}</c:when>
				<c:otherwise>${proc}-${tipproc}</c:otherwise>
			</c:choose>, <b>RESOLVE</b>: <br>
			<br>
			<b>AUTORIZAR</b> o afastamento <c:choose>
				<c:when test="${juiz_federal.sexo == 'M'}">do Juiz Federal </c:when>
				<c:otherwise>da Juíza Federal </c:otherwise>
			</c:choose> ${juiz_federal.nomePessoa}, para participar do ${curs} em ${cidad},
			${acgra} ${pais}, <c:choose>
				<c:when test="${nume == '1'}">no dia ${dtin}, </c:when>
				<c:otherwise>por ${nume} (${diasext}) dias, no período de ${dtin} a ${f:calculaData(nume,requestScope['dtin'])},</c:otherwise>
			</c:choose> com ônus limitado aos vencimentos.
		</mod:valor>
	</mod:documento>
</mod:modelo>