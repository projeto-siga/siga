<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="esconderTexto" value="sim" scope="request" />

<mod:modelo>
	<mod:entrevista>
		<br>
		<mod:grupo>
			<mod:pessoa titulo="<b>Nome do Destinatário</b>" var="nom" />
			<br>
			<br>
		</mod:grupo>
		<mod:grupo>
			<mod:selecao titulo="<b>Acerto Gramatical da Lotação</b>" var="act"
				opcoes="da;do;de" reler="sim" />
			<mod:lotacao titulo="<b>Setor</b>" var="set" />
		</mod:grupo>
		<br></br>
		<mod:grupo>
			<mod:texto titulo="<b>Portaria nº</b>" var="port" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<mod:texto titulo="<b>Ano</b>" var="ano" largura="5" />
			<mod:texto titulo="<b>Circular nº</b>" var="circular" />
			<br>
			<br>
		</mod:grupo>
		<mod:selecao titulo="<b>Quantidade de Bens</b>" var="contitem"
			opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20" reler="sim" />
		<br>
		<mod:grupo depende="contDependAjax">
			<c:forEach var="i" begin="1" end="${contitem}">
				<mod:grupo titulo="Item nº ${i}">
					<mod:texto titulo="<b>Nº do Patrimônio</b>" var="patri${i}" />
					<mod:texto titulo="<b>Descrição do Bem</b>" var="des${i}"
						largura="60" />
				</mod:grupo>
			</c:forEach>
		</mod:grupo>
		<br>
	</mod:entrevista>

	<mod:documento>

		<head>
		<style type="text/css">
@page {
	margin-left: 1cm;
	margin-right: 1cm;
	margin-top: 1cm;
	margin-bottom: 1cm;
}
</style>
		</head>
		<body>

		<c:if test="${empty tl}">
			<c:set var="tl" value="10pt"></c:set>
		</c:if>

		<c:set var="pessoa_nome"
			value="${f:pessoa(requestScope['nom_pessoaSel.id'])}" />
		<c:set var="lotac" value="${requestScope['set_lotacaoSel.descricao']}" />

		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
		
		<tr><td><br/>&nbsp;<br/></td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr>
							<td align="center"><p style="font-family:Arial;font-weight:bold;font-size:11pt;">COMUNICADO N&ordm; ${doc.codigo} DE ${doc.dtExtensoMaiusculasSemLocalidade}</p></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" /> 
		FIM CABECALHO -->
		<br></br>
		<br>
		<p align="justify">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:choose>
			<c:when test="${doc.subscritor.sexo == 'M'}">O Presidente</c:when>
			<c:otherwise>A Presidente</c:otherwise>
		</c:choose>&nbsp;da Comissão de Inventário Físico Anual de
		Patrimônio/Exercício&nbsp;${ano}, instituída pela Portaria
		nº&nbsp;${port} e com base na Circular Nº&nbsp;${circular},
		comunica expressamente&nbsp;<c:choose>
			<c:when test="${pessoa_nome.sexo=='M'}">ao Sr</c:when>
			<c:otherwise>à Srª</c:otherwise>
		</c:choose>&nbsp;${f:maiusculasEMinusculas(pessoa_nome.nomePessoa)},&nbsp;${act}&nbsp;${f:maiusculasEMinusculas(lotac)},
		que apresente&nbsp; <c:choose>
			<c:when test="${contitem=='1'}">o bem</c:when>
			<c:otherwise>os bens</c:otherwise>
		</c:choose> abaixo no prazo de 05 (cinco) dias úteis a contar da presente data.<br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Vale ressaltar
		que a não-observância do indicado no parágrafo anterior poderá ensejar
		a abertura de sindicância, na forma dos artigos 143 a 146 da Lei nº
		8.112/90, de 11/12/1990.<br>
		<br>


		<table width="100%" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td width="5%" bgcolor="#FFFFFF" align="center"><b>Item</b></td>
				<td width="25%" bgcolor="#FFFFFF" align="center"><b>Nº de
				Patrimônio</b></td>
				<td width="70%" bgcolor="#FFFFFF" align="center"><b>Descrição
				do Bem</b></td>
			</tr>
		</table>
		<c:if test="${contitem > '0'}">
			<c:forEach var="i" begin="1" end="${contitem}">
				<c:choose>
					<c:when test="${i < '10'}">
						<c:set var="ind" value="0${i}" />
					</c:when>
					<c:otherwise>
						<c:set var="ind" value="${i}" />
					</c:otherwise>
				</c:choose>
				<c:set var="numpatri" value="${requestScope[(f:concat('patri',i))]}" />
				<c:set var="descricao" value="${requestScope[(f:concat('des',i))]}" />

				<table width="100%" border="0" cellpadding="3" cellspacing="1"
					bgcolor="#000000">
					<tr>
						<td width="5%" bgcolor="#FFFFFF" align="center" colspan="1">
						${ind}</td>
						<td width="25%" bgcolor="#FFFFFF" align="center" colspan="1">
						${numpatri}</td>
						<td width="70%" bgcolor="#FFFFFF" align="center" colspan="1">
						${descricao}</td>
					</tr>
				</table>
			</c:forEach>
		</c:if> 
		
		<!-- 

		<c:forEach var="i" begin="1" end="${contitem}">
			<c:set var="num_patri" value="${requestScope[(f:concat('patri',i))]}" />
			<c:set var="descricao" value="${requestScope[(f:concat('des',i))]}" />
			<c:choose>
				<c:when test="${i < '10'}">Item&nbsp;0${i})&nbsp;&nbsp;${num_patri}&nbsp;&nbsp;${descricao}</c:when>
				<c:otherwise>Item&nbsp;${i})&nbsp;&nbsp;${num_patri}&nbsp;&nbsp;${descricao}</c:otherwise>
			</c:choose>
			<br>
		</c:forEach></p>
		
 --> <br>

		<!-- INICIO FECHO -->
		<p align="center">${doc.dtExtenso}</p>
		<br>
		<!-- FIM FECHO --> <mod:letra tamanho="${tl}">

			<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />

		</mod:letra> <!-- INICIO PRIMEIRO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
		FIM PRIMEIRO RODAPE --> <!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoADireita.jsp" />
		FIM RODAPE -->
		</body>

	</mod:documento>

</mod:modelo>