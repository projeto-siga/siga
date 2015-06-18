<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<mod:entrevista>
		<mod:selecao titulo="Conjunto de processos a informar" opcoes="1;2;3;4;5;6;7;8;9;10" var="quantProcessos" reler="ajax"  idAjax="quantProcessosAjax" />
		<mod:grupo depende="quantProcessosAjax">
		<c:forEach var="i" begin="1" end="${quantProcessos}">
		<mod:selecao titulo="Processos a informar" opcoes="1;2;3;4;5;6;7;8;9;10" var="quantProcessosInformar${i}" reler="ajax"  idAjax="quantProcessosInfAjax${i}" />
		<mod:selecao titulo="Tipo de Processo" var="tipoProcesso${i}" opcoes="Adicional de Insalubridade;Ajuda de Custo;Anuênio;Ausência ao Serviço em Razão de Casamento;Ausência ao Serviço em Razão de Falecimento em Família;Ausência ao Serviço para Doação de Sangue;Auxílio Natalidade;Averbação Tempo de Serviço/Contribuição;Concessão de Licença-Prêmio por Assiduidade;Consignação de Aluguel em Folha de Pagamento;Exclusão de Dependentes para Dedução IR na Fonte;Fruição de Licença Prêmio por Assiduidade;Horário Especial;Horário Especial ao Servidor Estudante;Inclusão de Dependente para Dedução IR na Fonte;Incorporação de Décimos;Licença Maternidade;Licença Paternidade;Quintos;Outros" reler="ajax" idAjax="tipoProcAjax${i}" />
			<mod:grupo depende="tipoProcAjax${i}">
				<c:if test="${requestScope[f:concat('tipoProcesso',i)] eq 'Outros'}">
					<mod:texto titulo="Outros:" var="outros${i}" largura="130" maxcaracteres="120" />
				</c:if>
			</mod:grupo>	
		<mod:grupo depende="quantProcessosInfAjax${i}">
			<c:forEach var="j" begin="1" end="${requestScope[f:concat(f:concat('quantProcessosInformar',i),j)]}">
				<mod:grupo>
					<mod:texto titulo="Número do Processo:" var="numproc${i}${j}" largura="6" />	
				</mod:grupo>	
				<mod:grupo>
					<mod:pessoa titulo="Nome" var="servidor${i}${j}" />
				</mod:grupo>
				<mod:grupo>
					<mod:memo titulo="Despacho" var="despacho${i}${j}" colunas="55" linhas="5"/>
				</mod:grupo>
			</c:forEach>
		</mod:grupo>
			<hr>
		</c:forEach>
		</mod:grupo>
			<mod:grupo>
				<mod:selecao titulo="Tamanho da letra" var="tamanhoLetra" opcoes="Normal;Pequeno;Grande"/>
			</mod:grupo>
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
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<br/>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td align="left" width="40%"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">${doc.codigo}</p></td>
							<td align="right" width="60%"><mod:letra tamanho="${tl}"><p>${doc.dtExtenso}</p></mod:letra></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->
		<mod:letra tamanho="${tl}">
		<p>DE: <c:choose>
			<c:when test="${not empty doc.nmLotacao}">
			${doc.nmLotacao}
			</c:when>
			<c:otherwise>${doc.titular.lotacao.descricao}</c:otherwise>
		</c:choose> <br>
		PARA: ${doc.destinatarioString}</p>
	
		<p align="center"><B>SOLICITAÇÃO DE PUBLICAÇÃO BOLETIM INTERNO</B><br/>Processos</p>
		<c:forEach var="i" begin="1" end="${quantProcessos}">
		<!-- INICIO CORPO -->
		<p><c:choose>
			<c:when test="${requestScope[f:concat('tipoProcesso',i)] ne 'Outros'}">
				<span style="font-weight: bold">${requestScope[f:concat('tipoProcesso', i)]}</span>
			</c:when>
			<c:otherwise>
				<span style="font-weight: bold">${requestScope[f:concat('outros', i)]}</span>
			</c:otherwise>
			</c:choose>
		</p>
		<!-- FIM CORPO -->
		<c:forEach var="j" begin="1" end="${requestScope[f:concat(f:concat('quantProcessosInformar',i),j)]}">
		<br/>
		<!-- INICIO CORPO -->
		<p><span style="font-weight: bold">Número do Processo:</span> ${requestScope[f:concat(f:concat('numproc',i),j)]}</p>
		<p><span style="font-weight: bold">Nome:</span> ${requestScope[f:concat(f:concat(f:concat('servidor',i),j),'_pessoaSel.descricao')]}</p>
		<p><span style="font-weight: bold">Despacho:</span> ${requestScope[f:concat(f:concat('despacho',i),j)]}</p>
		<!-- FIM CORPO -->	
		</c:forEach>
		<!-- INICIO CORPO -->
		<br/>
		<!-- FIM CORPO -->
		</c:forEach>	
			
		<br>
		
		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />
		
		</mod:letra>
		
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

