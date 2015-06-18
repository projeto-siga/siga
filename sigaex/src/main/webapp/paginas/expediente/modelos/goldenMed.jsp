<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>
		<mod:grupo>
			<mod:pessoa var="servidor" titulo="Servidor"/>
		</mod:grupo>
		<mod:data var="data" titulo="Elencado a partir de"/>
		<mod:grupo>	
			<mod:texto var="identificacaoServidor" titulo="Identificação do servidor"/>
			<mod:selecao reler="ajax" idAjax="solicitacaoAjax" opcoes="[Selecione];adesão;exclusão" var="solicitacao" titulo="Solicito a:"/>
		</mod:grupo>
		<mod:grupo depende="solicitacaoAjax">
		<c:choose>
			<c:when test="${solicitacao!='[Selecione]'}">
				<mod:selecao titulo="Quantidade de dependentes" idAjax="qtdDependenteAjax" var="qtdDependente" opcoes="1;2;3;4;5;6;7;8;9;10" reler="ajax"/>
					<mod:grupo titulo="Nome dos dependentes">
						<mod:grupo depende="qtdDependenteAjax">
							<c:forEach var="i" begin="1" end="${qtdDependente}">
								<mod:grupo>
									<mod:texto titulo="${i}" var="dependente${i}" largura="50"/>
								</mod:grupo>
							</c:forEach>
						</mod:grupo>
					</mod:grupo>
			</c:when>
		</c:choose>
		</mod:grupo>
	</mod:entrevista>
	<mod:documento>
	<c:set var="tl" value="8pt" />	
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		<head>
		<style type="text/css">
			@page {
				margin-left: 3cm;
				margin-right: 3cm;
				margin-top: 1cm;
				margin-bottom: 2cm;
			}
		</style>
		</head>
	<body>
	<mod:letra tamanho="${tl}">
	<c:set var="pes" value="${f:pessoa(requestScope['servidor_pessoaSel.id'])}"/> 
	<table width="100%" border="1" cellpadding="3">
		<tr>	
			<td align="center" width="70%">
			<br>
				<h2>Golden Cross</h2> - Cobertura opcional
			<br>
			<br>
			<td align="center">				
				Golden Med						
			</td>
		</tr>
	</table>
	<table width="100%" border="1" cellpadding="3">
		<tr>
			<td><p align="justify">Identificação do titular:&nbsp;&nbsp;${identificacaoServidor}</p><br/>
				<p align="justify">Matricula:&nbsp;&nbsp;${requestScope['servidor_pessoaSel.sigla']}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Lotação: ${pes.lotacao.descricao}</p><br/>
			</td>			
		</tr>		
	</table>
	<table width="100%" border="1" cellpadding="3">
		<tr>
			<td align="center" width="20%"><br><b>Solicito a</b><br/><br/></td>
			<td width="20%"><c:choose><c:when test="${solicitacao=='adesão'}">[x] adesão<br><br>[&nbsp;&nbsp;] exclusão</c:when><c:when test="${solicitacao=='exclusão'}">[&nbsp;&nbsp;] adesão<br><br>[x] exclusão</c:when><c:otherwise>[&nbsp;&nbsp;] adesão<br><br>[&nbsp;&nbsp;] exclusão</c:otherwise></c:choose></td>
			<td>&nbsp;&nbsp;dos beneficiários abaixo elencados na <b>Golden Med</b> a partir de ${data} </td>
		</tr>		
	</table>
	<table width="100%" border="1" cellpadding="3">
		<tr>
			<td>
				Titular:&nbsp;&nbsp; ${requestScope['servidor_pessoaSel.descricao']}<br><br> 
				<p>Dependentes:</p>
				<c:forEach var="i" begin="1" end="${qtdDependente}">
						<c:if test="${i==1}">&nbsp;</c:if><c:if test="${i!=1}"><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</c:if>
					${i} - &nbsp;${requestScope[f:concat('dependente',i)]}
				</c:forEach>				
			</td>
		</tr>
	</table>
	<table width="100%" border="1" cellpadding="3">
		<tr>
			<td>
				<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- No Golden Med não há cobrança proporcional a período(pro rata), <b>nem movimentação retroativa</b> sendo a vingência SEMPRE EM DIA PRIMEIRO.<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Agregados, Dependentes Ecônomicos e Beneficiários Designados descontam antecipado.<br> 
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Caso o titular solicite a sua exclusão do Golden Med e deseje incluir dependentes, deverá elenca-los no campo acima.<br>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Não será possível solicitar a inclusão de qualquer dependente (dependentes diretos, agregados, pais dependentes econômicos e beneficiários designados), sem que o titular esteja cadastrado no benefício. 
				<br><br>
			</td>
		</tr>
	</table>
	<table width="100%" border="1" cellpadding="3">
		<tr>
			<td>
				<br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Tenho ciência de que deverei entregar este requerimento à <b>SEBENS/SRH até o dia 20</b>, para que o início da vingência(da inclusão ou da exclusão) seja da data acima solicitada.
				<br><br>
			</td>
		</tr>
	</table>
	<br>
	<p style="TEXT-INDENT: 2cm">${doc.dtExtenso}</p><br>
		<c:import
			url="/paginas/expediente/modelos/inc_assinatura.jsp" />
	
	<p align="center">Recebido pela SEBENS/SRH em ____/____/____ &nbsp;&nbsp; - por:_________________________ 
	
	</mod:letra>
	</mod:documento>
</mod:modelo>

