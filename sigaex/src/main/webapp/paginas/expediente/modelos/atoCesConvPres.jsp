<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="esconderTexto" value="sim" scope="request" />

<mod:modelo urlBase="/paginas/expediente/modelos/ato_presidencia.jsp">
	<mod:entrevista>

		<br>
		<mod:grupo>
			<mod:pessoa titulo="<b> Juiz Federal Convocado</b>" var="jfc" />
			<br>
			<br>
			<mod:selecao titulo="<b>Acerto Gramatical</b>" var="acgra"
				opcoes="do;da" reler="sim" />
			<mod:lotacao titulo="<b> Lotação de Origem</b>" var="lota" />
			<br><br>
		</mod:grupo>	
		<mod:grupo>
			<mod:data titulo="<b>Data de Vigência</b>" var="datfin" />
		</mod:grupo>
		<br>
	</mod:entrevista>

	<mod:documento>

		<c:set var="federal" value="${f:pessoa(requestScope['jfc_pessoaSel.id'])}" />
		<c:set var="lotac" value="${requestScope['lota_lotacaoSel.descricao']}" />
		<c:set var="lotc" value="${f:maiusculasEMinusculas(lotac)}" />
		
		<mod:valor var="texto_ato">
			<br />
			<br />
			<p style="TEXT-INDENT: 2cm" align="justify"><b><c:choose>
				<c:when test="${doc.subscritor.sexo == 'M'}">O PRESIDENTE</c:when>
				<c:otherwise>A PRESIDENTE</c:otherwise>
			</c:choose> DO TRIBUNAL REGIONAL FEDERAL DA 2ª REGIÃO</b>, no uso de suas
			atribuições, <b>RESOLVE</b>:<br>
			<br>
			<br>
			<b>CESSAR</b>, a partir de ${datfin} , a convocação <c:choose>
				<c:when test="${federal.sexo == 'M'}">
					<c:set var="el" value="do Juiz Federal"></c:set>
					<c:set var="tit" value="Dr">
					</c:set>
				</c:when>
				<c:otherwise>
					<c:set var="el" value="da Juíza Federal"></c:set>
					<c:set var="tit" value="Dra"></c:set>
				</c:otherwise>
			</c:choose> ${el} ${acgra} ${lotc} - ${federal.orgaoUsuario.nmOrgaoUsu}, ${tit} ${federal.nomePessoa}</b>. <br>
			
		</mod:valor>
	</mod:documento>
</mod:modelo>