<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<mod:grupo titulo="Período">
			<mod:obrigatorios />
			<br />
			<br />
			<mod:selecao var="qtdeDias"
				titulo="Selecione a quantidade de dias intercalados e/ou períodos"
				opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;25;26;27;28;29;30;31;32;33;34;35;36;37;38;39;40"
				reler="sim" idAjax="quantDependAjax" />
			<mod:grupo depende="quantDependAjax"
				titulo="<font color='red'>Atenção:</font> <font color='blue'>Para dias intercalados, preencha apenas o campo Data Início</font>">
				<c:forEach var="i" begin="1" end="${qtdeDias}">
					<mod:data titulo="Data Início" var="data_ini${i}" obrigatorio="Sim"
						reler="sim" />
					<mod:data titulo="Data Fim" var="data_fim${i}" alerta="Nao"
						reler="sim" />
					<c:choose>
						<c:when
							test="${not empty requestScope[f:concat('data_ini',i)] and not empty requestScope[f:concat('data_fim',i)]}">
							<mod:mensagem
								texto="${f:calculaDiferencaDatas(requestScope[f:concat('data_fim',i)],requestScope[f:concat('data_ini',i)])} dia(s)"></mod:mensagem>
						</c:when>
						<c:when test="${not empty requestScope[f:concat('data_ini',i)] and empty requestScope[f:concat('data_fim',i)]}">
							<mod:mensagem texto="1 dia(s)"/>
						</c:when>
						<c:when test="${empty requestScope[f:concat('data_ini',i)] and empty requestScope[f:concat('data_fim',i)]}">
							<mod:mensagem texto="0 dia(s)"/>
						</c:when>
						<c:otherwise></c:otherwise>
					</c:choose>
					
					<mod:hora titulo="Hora Início" var="hora_ini${i}" alerta="Nao"/>
					<mod:hora titulo="Fim" var="hora_fim${i}" alerta="Nao"/>
					<br />
				</c:forEach>
			</mod:grupo>
			<mod:texto titulo="Observação" var="obs" largura="60" />
		</mod:grupo>
		<mod:grupo titulo="Local de realização">
			<mod:selecao titulo="Total de locais" var="totalDeLocais"
				opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20"
				reler="sim" />
			<br />
			<c:forEach var="i" begin="1" end="${totalDeLocais}">
				<mod:grupo>
					<mod:selecao titulo="Local de realização" var="local${i}"
						opcoes="Angra dos Reis;Av Almirante Barroso;Av Rio Branco;Av Venezuela;Barra do Piraí;Campos;Duque de Caxias;Itaboraí;Itaperuna;Macaé;Magé;Niterói;Nova Friburgo;Nova Iguaçu;Petrópolis;Resende;Rua Equador;São Cristóvão;São Gonçalo;São João de Meriti;São Pedro d'Aldeia;Teresópolis;Três Rios;Volta Redonda"
						reler="não" />
				</mod:grupo>
				<br />
			</c:forEach>
		</mod:grupo>
		<mod:grupo titulo="Serviço">
			<mod:memo titulo="Descrição" var="desc" colunas="80" />
		</mod:grupo>
		<mod:grupo titulo="Relação de pessoal">
			<mod:texto titulo="Nome da empresa" var="empresa" largura="60" />
			<br />
			<br />
			<mod:selecao titulo="Total de pessoas" var="totalDePessoal"
				opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20"
				reler="sim" />
			<br />
			<br />
			<c:forEach var="i" begin="1" end="${totalDePessoal}">
				<mod:grupo>
					<mod:texto titulo="Nome do funcionário" var="funcionario${i}"
						largura="60" />
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="RG" var="rg${i}" largura="60" />
				</mod:grupo>
				<br />
			</c:forEach>
		</mod:grupo>
	</mod:entrevista>
	<mod:documento>
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
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina.jsp" />
		</td></tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->

		<table width="100%" border="0" bgcolor="#FFFFFF">
		<tr>
			<td align="right"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">ENTRADA DE PESSOAL<br /><br />N&ordm; ${doc.codigo}</p></td>
		</tr>
		</table>

		<table width="100%" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF"><b>1 - PERÍODO</b></td>
			</tr>
			<c:forEach var="i" begin="1" end="${qtdeDias}">

				<tr>
					<td bgcolor="#FFFFFF"><b>Data(s):&nbsp;</b> <c:choose>
						<c:when
							test="${requestScope[f:concat('data_ini',i)] eq requestScope[f:concat('data_fim',i)]}">
							${requestScope[f:concat('data_ini',i)]}
						</c:when>
						<c:when test="${empty requestScope[f:concat('data_fim',i)]}">
							${requestScope[f:concat('data_ini',i)]}
						</c:when>
						<c:otherwise>
							de ${requestScope[f:concat('data_ini',i)]} a
							${requestScope[f:concat('data_fim',i)]} 
						</c:otherwise>
					</c:choose> - <b>Horário:</b> de ${requestScope[f:concat('hora_ini',i)]} às
					${requestScope[f:concat('hora_fim',i)]}</td>
				</tr>
			</c:forEach>
			<tr>
				<td bgcolor="#FFFFFF"><b>Observação:&nbsp;</b><c:if
					test="${not empty obs}">
					${obs}</c:if></td>
			</tr>
		</table>

		<br />

		<table width="100%" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF"><b>2 - LOCAL DE REALIZAÇÃO DO SERVIÇO</b></td>
			</tr>
			<c:forEach var="i" begin="1" end="${totalDeLocais}">
				<tr>
					<td bgcolor="#FFFFFF">
					<p>${requestScope[f:concat('local',i)]}</p>
					</td>
				</tr>
			</c:forEach>
		</table>

		<br />

		<table width="100%" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF"><b>3 - DESCRIÇÃO DO SERVIÇO</b></td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF">
				<p><c:choose>
					<c:when test="${not empty desc}">${desc}</c:when>
					<c:otherwise>Não foi informado.</c:otherwise>
				</c:choose></p>
				</td>
			</tr>
		</table>

		<br />

		<table width="100%" border="1" cellpadding="2"
			cellspacing="1" bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF" ><b>4 - RELAÇÃO DE
				PESSOAL</b></td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF">Empresa:&nbsp;${empresa}</td>
			</tr>
		</table>
		<table width="100%" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td width="40%" bgcolor="#FFFFFF" align="center"><b>Nome do
				funcionário</b></td>
				<td width="20%" bgcolor="#FFFFFF" align="center"><b>RG</b></td>
			</tr>
			<c:forEach var="i" begin="1" end="${totalDePessoal}">
				<tr>
					<td width="40%" bgcolor="#FFFFFF" align="left">${requestScope[f:concat('funcionario',i)]}</td>
					<td width="20%" bgcolor="#FFFFFF" align="center">${requestScope[f:concat('rg',i)]}</td>
				</tr>
			</c:forEach>
		</table>

		<br />

		<table width="100%" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF"><b>SOLICITANTE</b></td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" align="center"><br />
				<br />
				${doc.dtExtenso}<br />
				<br />
				<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" /> <br />
				<br />
				</td>
			</tr>
		</table>

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
