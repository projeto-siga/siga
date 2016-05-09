<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>
		<mod:selecao titulo="Selecione a opção desejada" var="opcao"
			opcoes="Selecione;Marcação;Alteração;Cancelamento" reler="sim"></mod:selecao>
		<c:choose>
			<c:when test="${opcao eq 'Marcação'}">
				<mod:grupo titulo="Fruição/Marcacao de férias">
					<mod:selecao titulo="Número de marcações" var="marcacoes"
						opcoes="1;2;3" reler="ajax" idAjax="marcacoesAjax" />
					<mod:grupo depende="marcacoesAjax">
						<c:forEach var="i" begin="1" end="${marcacoes}">
							<mod:grupo>
								<mod:selecao titulo="<b>${i})</b> Período Aquisitivo"
									var="periodoAquisitivo${i}"
									opcoes="2011/2012;2012/2013;2013/2014;2014/2015;2015/2016;2016/2017;2017/2018;2018/2019;2019/2020" />
							&nbsp;&nbsp;&nbsp;	
							<mod:selecao titulo="Parcela" var="sequencial${i}" opcoes="1;2;3"
									reler="ajax" idAjax="seqMarcacoesAjax${i}" />
								   &nbsp;&nbsp;&nbsp;					
							<mod:texto titulo="Quantidade de dias" var="quantDias${i}"
									largura="3" maxcaracteres="2" obrigatorio="sim"/> &nbsp;&nbsp;&nbsp;
							<mod:data titulo="Data Inicial" var="dataInicio${i}"  obrigatorio="sim"/>
							</mod:grupo>
							<mod:grupo depende="seqMarcacoesAjax${i}">
								<c:if test="${requestScope[f:concat('sequencial',i)] == 1}">
									&nbsp;&nbsp;&nbsp;						    
									<mod:selecao
										titulo="Deseja o adiantamento da remuneração de férias?"
										var="adiantamento${i}" opcoes="Sim;Não" />
								</c:if>
							</mod:grupo>
							<br />
						</c:forEach>
					</mod:grupo>
				</mod:grupo>

			</c:when>
			<c:when test="${opcao eq 'Alteração'}">
				<mod:grupo titulo="Alteração de Férias">
					<mod:selecao titulo="Número de alterações" var="alteracoes"
						opcoes="1;2;3" reler="ajax" idAjax="alteracoesAjax" />
					<mod:grupo depende="alteracoesAjax">
						<c:forEach var="i" begin="1" end="${alteracoes}">
							<mod:grupo>
								<mod:selecao titulo="<b>${i})</b> Período Aquisitivo"
									var="periodoAquisitivo${i}"
									opcoes="2011/2012;2012/2013;2013/2014;2014/2015;2015/2016;2016/2017;2017/2018;2018/2019;2019/2020" />
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	
							<mod:selecao titulo="Sequencial" var="sequencial${i}"
									opcoes="1;2;3" reler="ajax" idAjax="seqAlteracoesAjax${i}" />
							</mod:grupo>
							<mod:grupo>
								<mod:data titulo="Período de" var="dataInicio${i}" />

								<mod:data titulo="a" var="dataFinal${i}" />

								<mod:data titulo="Alterar para de" var="novaDataInicio${i}" />
								<mod:data titulo="a" var="novaDataFinal${i}" />
							</mod:grupo>
							<mod:grupo depende="seqAlteracoesAjax${i}">
								<c:if test="${requestScope[f:concat('sequencial',i)] == 1}">
									<mod:grupo>
										<mod:selecao
											titulo="Deseja o adiantamento da remuneração de férias?"
											var="adiantamento${i}" opcoes="Sim;Não" />
									</mod:grupo>
								</c:if>
							</mod:grupo>
						</c:forEach>
					</mod:grupo>
				</mod:grupo>
			</c:when>
			<c:when test="${opcao eq 'Cancelamento'}">
				<mod:grupo titulo="Cancelamento de férias">
					<mod:selecao titulo="Número de cancelamentos" var="cancelamentos"
						opcoes="1;2;3" reler="ajax" idAjax="cancelamentosAjax" />
					<mod:grupo depende="cancelamentosAjax">
						<c:forEach var="i" begin="1" end="${cancelamentos}">
							<mod:grupo>
								<mod:selecao titulo="<b>${i})</b> Período Aquisitivo"
									var="periodoAquisitivo${i}"
									opcoes="2011/2012;2012/2013;2013/2014;2014/2015;2015/2016;2016/2017;2017/2018;2018/2019;2019/2020" />
								<mod:selecao titulo="Sequencial" var="sequencial${i}"
									opcoes="1;2;3" />
								<mod:data titulo="Período de" var="dataInicio${i}" />
								<mod:data titulo="a" var="dataFinal${i}" />
							</mod:grupo>
						</c:forEach>
					</mod:grupo>
				</mod:grupo>
			</c:when>
			<c:otherwise></c:otherwise>
		</c:choose>
		<c:if test="${(opcao eq 'Alteração') or (opcao eq 'Cancelamento')}">
			<mod:grupo titulo="Demais Informações">
				<mod:grupo>
					<mod:selecao titulo="Deseja manter os demais períodos já marcados?"
						var="manterPeriodos" opcoes="Sim;Não" />
				</mod:grupo>
				<mod:grupo>
					<mod:selecao titulo="Qual o motivo desta solicitação?" var="motivo"
						opcoes="Necessidade de serviço;Outros" reler="ajax"
						idAjax="outrosAjax" />
				</mod:grupo>
				<mod:grupo depende="outrosAjax">
					<c:if test="${motivo == 'Outros'}">
						<mod:grupo>
							<mod:texto titulo="Motivo" var="motivoDesc" largura="60" />
						</mod:grupo>
					</c:if>
				</mod:grupo>
			</mod:grupo>
		</c:if>
	</mod:entrevista>

	<mod:documento>

		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
@page {
	margin-left: 3cm;
	margin-right: 2cm;
	margin-top: 1cm;
	margin-bottom: 1cm;
}
</style>

		</head>
		<body>

		<c:import
			url="/paginas/expediente/modelos/inc_tit_SraDiretoraSecretariaSubsecretaria.jsp" />


		<p style="TEXT-INDENT: 2cm" align="justify">Matrícula:
		${doc.subscritor.sigla}</p>
		<p style="TEXT-INDENT: 2cm" align="justify">Nome:
		${doc.subscritor.descricao}</p>
		<br />
		<p style="TEXT-INDENT: 2cm" align="justify">Venho,
		respeitosamente, requerer a V.S&ordf;. <c:choose>
			<c:when test="${opcao eq 'Marcação'}">fruição de minhas férias regulamentares, na forma abaixo discriminada:
			    <br />
				<br />
				<table width="80%" align="center" border="1" cellpadding="2"
					cellspacing="1" bgcolor="#000000">
					<tr>
						<td bgcolor="#FFFFFF" width="05%" align="center">Período
						Aquisitivo</td>
						<td bgcolor="#FFFFFF" width="05%" align="center">Parcela</td>
						<td bgcolor="#FFFFFF" width="15%">Detalhes</td>
					</tr>

					<c:forEach var="i" begin="1" end="${marcacoes}">
						<tr>
							<td bgcolor="#FFFFFF" align="center">${requestScope[f:concat('periodoAquisitivo',i)]}</td>
							<td bgcolor="#FFFFFF" align="center">${requestScope[f:concat('sequencial',i)]}</td>
							<td bgcolor="#FFFFFF" align="left">(<b>${requestScope[f:concat('quantDias',i)]}</b>
							dias) de ${requestScope[f:concat('dataInicio',i)]} a
							${f:calculaData(requestScope[f:concat('quantDias',i)],requestScope[f:concat('dataInicio',i)])}
							<c:if test="${requestScope[f:concat('sequencial',i)] == 1}">
								<br />
								<u>Adiantamento:</u>
								<b>${requestScope[f:concat('adiantamento',i)]}</b>
							</c:if></td>
						</tr>
					</c:forEach>
				</table>
				<br />
				<br />
			</c:when>
			<c:when test="${opcao eq 'Alteração'}">
				<b>alteração</b> de minhas férias regulamentares, na forma abaixo discriminada:
			    <br />
				<br />
				<table width="80%" align="center" border="1" cellpadding="2"
					cellspacing="1" bgcolor="#000000">
					<tr>
						<td bgcolor="#FFFFFF" width="05%" align="center">Período
						Aquisitivo</td>
						<td bgcolor="#FFFFFF" width="05%" align="center">Sequencial</td>
						<td bgcolor="#FFFFFF" width="15%">Detalhes</td>
					</tr>

					<c:forEach var="i" begin="1" end="${alteracoes}">
						<tr>
							<td bgcolor="#FFFFFF" align="center">${requestScope[f:concat('periodoAquisitivo',i)]}</td>
							<td bgcolor="#FFFFFF" align="center">${requestScope[f:concat('sequencial',i)]}</td>
							<td bgcolor="#FFFFFF" align="left">Período:
							${requestScope[f:concat('dataInicio',i)]} a
							${requestScope[f:concat('dataFinal',i)]}<br />
							Alterar para: ${requestScope[f:concat('novaDataInicio',i)]} a
							${requestScope[f:concat('novaDataFinal',i)]} <c:if
								test="${requestScope[f:concat('sequencial',i)] == 1}">
								<br />
								<u>Adiantamento:</u>
								<b>${requestScope[f:concat('adiantamento',i)]}</b>
							</c:if></td>
						</tr>
					</c:forEach>
				</table>
			</c:when>
			<c:when test="${opcao eq 'Cancelamento'}">
				<b>cancelamento</b> de minhas férias regulamentares, na forma abaixo discriminada:
			    <br />
				<br />
				<br />
				<table width="80%" align="center" border="1" cellpadding="2"
					cellspacing="1" bgcolor="#000000">
					<tr>
						<td bgcolor="#FFFFFF" width="05%" align="center">Período
						Aquisitivo</td>
						<td bgcolor="#FFFFFF" width="05%" align="center">Sequencial</td>
						<td bgcolor="#FFFFFF" width="10%">Detalhes</td>
					</tr>
					<c:forEach var="i" begin="1" end="${cancelamentos}">
						<tr>
							<td bgcolor="#FFFFFF" align="center">${requestScope[f:concat('periodoAquisitivo',i)]}</td>
							<td bgcolor="#FFFFFF" align="center">${requestScope[f:concat('sequencial',i)]}</td>
							<td bgcolor="#FFFFFF" align="left">Período:${requestScope[f:concat('dataInicio',i)]}
							a ${requestScope[f:concat('dataFinal',i)]}</td>
						</tr>
					</c:forEach>
				</table>
			</c:when>
			<c:otherwise></c:otherwise>
		</c:choose> <c:if test="${(opcao eq 'Alteração') or (opcao eq 'Cancelamento')}">
			<p style="TEXT-INDENT: 2cm" align="justify">Deseja manter os
			demais períodos já marcados? <b>${manterPeriodos}</b></p>
			<c:choose>									
				<c:when test="${motivo == 'Outros'}">
					<p style="TEXT-INDENT: 2cm" align="justify">Qual o motivo desta
					solicitação? ${motivoDesc}</p>
				</c:when>
				<c:otherwise>										
					<p style="TEXT-INDENT: 2cm" align="justify">Qual o motivo desta
					solicitação? <b>${motivo}</b></p>
				</c:otherwise>	
			</c:choose>	
		</c:if> <c:if test="${(opcao eq 'Alteração') or (opcao eq 'Marcação')}">
			<p style="TEXT-INDENT: 2cm" align="justify">Conforme o art.8º, parágrafo 8º, da Resolução CF-RES-2012/00221 de 19/12/2012, declaro estar CIENTE de que cabe à Administração, comunicar, com antecedência de 90 dias do fim do prazo de fruição das férias, ao servidor e à chefia imediata, a obrigatoriedade de gozo dessas férias e que, não havendo manifestação de minha parte ou de meu superior hierárquico acerca da marcação, a Administração o fará de ofício.</p>
		</c:if> <c:import url="/paginas/expediente/modelos/inc_deferimento.jsp" /> <c:import
			url="/paginas/expediente/modelos/inc_localDataAssinatura.jsp" /> <c:import
			url="/paginas/expediente/modelos/inc_deAcordoAssSupHierarquico.jsp" />

		<c:import
			url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		</body>
		</html>
	</mod:documento>
</mod:modelo>
