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
		<br>
        
		<mod:grupo titulo="Alteração de Ato - Escala de Plantão">
			<mod:grupo>
				<mod:texto titulo="Ato nº" var="numact" largura="25" />

				<mod:data titulo="Datado de" var="dtato" />

				<mod:selecao titulo="Exercício" var="exerc"
					opcoes="2011;2012;2013;2014;2015;2016;2017;2018;2019;2020"
					reler="sim" />
			</mod:grupo>
			<br>
			<mod:grupo>
				<mod:data titulo="Data de publicação no DJE 2ª Região" var="dtpub" />

				<mod:texto titulo="Fls." var="folha" largura="5" />
			</mod:grupo>
			<br>
		</mod:grupo>
		
		<mod:selecao var="contAlts" titulo="Quantidade de Alterações"
			opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;25;26;27;28"
			reler="sim" />
		<br />
		<br>
		<mod:grupo depende="contDependAjax">
			<c:forEach var="i" begin="1" end="${contAlts}">
				<mod:grupo titulo="Alteração nº ${i}">

					<mod:grupo>
						<mod:selecao titulo="Plantonista" var="opdj${i}"
							opcoes="Juiz Federal Convocado;Juíza Federal Convocada;Desembargador Federal;Desembargadora Federal"
							reler="não" />
						<mod:pessoa titulo="Nome" var="titular${i}" />
					</mod:grupo>

					<mod:grupo>
						<mod:data titulo="Data 1" var="dt1${i}" obrigatorio="Sim" />
						<mod:selecao titulo="Dia Único" var="dun${i}" opcoes="Sim;Não"
							reler="sim" />
						<c:if test="${requestScope[(f:concat('dun',i))] == 'Não'}">
							<mod:data titulo="Data 2" var="dt2${i}" obrigatorio="Sim" />
							<mod:selecao titulo="Inclui os dias entre as datas "
								var="inclui${i}" opcoes="Sim;Não" reler="sim" />
						</c:if>
					</mod:grupo>


				</mod:grupo>

				<hr style="color: #FFFFFF;" />
			</c:forEach>
		</mod:grupo>
		<br>
		<br>
	</mod:entrevista>

	<mod:documento>
		<mod:valor var="texto_ato">
			<br />
			<p style="TEXT-INDENT: 2cm" align="justify"><b><c:choose>
				<c:when test="${doc.subscritor.sexo == 'M'}">O PRESIDENTE</c:when>
				<c:otherwise>A PRESIDENTE</c:otherwise>
			</c:choose> DO TRIBUNAL REGIONAL FEDERAL DA 2ª REGIÃO</b>, no uso de suas
			atribuições, <b>RESOLVE</b>:<br>
			<br>
			<b>ALTERAR</b> o Ato nº ${numact}, de ${dtato}, disponibilizado no Diário
			da Justiça Eletrônico da 2ª Região, no dia ${dtpub}, às fls.
			${folha}, que trata da escala de plantão dos Exmos. Magistrados deste
			Tribunal, para o exercício de ${exerc}, relativa aos sábados,
			domingos e feriados, na forma abaixo:</p>
			<p align="left"><c:forEach var="i" begin="1" end="${contAlts}">
				<c:set var="tit" value="${requestScope[(f:concat('opdj',i))]}" />
				<c:set var="titul"
					value="${f:pessoa(requestScope[f:concat(f:concat('titular',i),'_pessoaSel.id')])}" />
				<c:set var="data1" value="${requestScope[(f:concat('dt1',i))]}" />
				<c:set var="incl" value="${requestScope[(f:concat('inclui',i))]}" />
				<c:set var="data2" value="${requestScope[(f:concat('dt2',i))]}" />
				<b>${tit}</b>&nbsp;<b>&nbsp;${titul.nomePessoa}</b> -  ${data1} 
			    <c:if test="${(data1 != data2) and not empty data2}">
					<c:choose>
						<c:when test="${incl == 'Sim'}"> a </c:when>
						<c:otherwise> e </c:otherwise>
					</c:choose> ${data2}</u>
				</c:if>
				<br>
			</c:forEach>
		</mod:valor>
        
	</mod:documento>
</mod:modelo>