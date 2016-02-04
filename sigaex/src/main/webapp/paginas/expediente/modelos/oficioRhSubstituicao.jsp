<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:import
	url="/paginas/expediente/modelos/rj_inc_dad_juizFedDirForo.jsp" />

<mod:modelo urlBase="/paginas/expediente/modelos/oficio.jsp">
	<mod:entrevista>
		<mod:grupo>
			<mod:pessoa titulo="Substituto" var="substituto" />
		</mod:grupo>
		<mod:grupo>
			<mod:pessoa titulo="Titular" var="titular" />
		</mod:grupo>
		<mod:grupo titulo="Período Solicitado">
			<mod:data titulo="De" var="dataInicio" />
			<mod:data titulo="a" var="dataFim" />
		</mod:grupo>
		<mod:grupo>
			<mod:texto titulo="Por motivo de" var="motivo" largura="60" />
		</mod:grupo>
	</mod:entrevista>
	<mod:documento>
		<mod:valor var="texto_oficio">
			<p style="TEXT-INDENT: 2cm" align="justify">Solicito a Vossa
			Excelência as providências necessárias para que o(a) servidor(a)
			<mod:identificacao pessoa="${requestScope['substituto_pessoaSel.id']}"/>
			<b>substitua</b> o(a) servidor(a) 
			<mod:identificacao pessoa="${requestScope['titular_pessoaSel.id']}" funcao="sim" />
				
			<c:choose>
				<c:when test="${dataInicio == dataFim}">
					no dia <b>${dataInicio}</b>,
				</c:when>
				<c:otherwise>
					no período de <b>${dataInicio}</b> a <b>${dataFim}</b>,
				</c:otherwise>
			</c:choose>
				por motivo de <b>${motivo}</b>.</p>
		</mod:valor>
	</mod:documento>
</mod:modelo>

