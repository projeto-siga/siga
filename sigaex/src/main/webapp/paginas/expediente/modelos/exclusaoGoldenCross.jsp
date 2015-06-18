<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb" %>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<mod:modelo>
	<mod:entrevista>
	
	<mod:grupo titulo="Dados do titular">
			<mod:pessoa var="nometit" titulo="Identifica&ccedil;&atilde;o do titular"/>
		<mod:grupo>	
			<mod:texto var="nrocartaogolden" titulo="N&uacute;mero do Cart&atildeo Golden Cross:" largura="15"/>
		</mod:grupo>
		<mod:grupo>
			<mod:mensagem titulo="Desejo proceder a exclus&atilde;o dos dependentes diretos abaixo discriminados, a partir de:" />
			<mod:selecao titulo="Dia" var="dtexclusao" opcoes="  ;01;11;21" />
			<mod:selecao titulo="M&ecirc;s" var="mes" opcoes="  ;01;02;03;04;05;06;07;08;09;10;11;12" />
			<mod:selecao titulo="Ano" var="ano" opcoes="  ;2007;2008;2009;2010;2011;2012;2013;2014;2015;2016;2017;2018;2019;2020" />
		</mod:grupo>
	</mod:grupo>	
	<mod:grupo titulo="Titular" />
		<mod:selecao var="situacaoTitular" titulo="Exclus&atilde;o do Titular ?" opcoes="Não;Sim" reler="ajax" idAjax="situacaoTitularAjax" />
			<mod:grupo depende="situacaoTitularAjax">
				<c:if test="${situacaoTitular=='Sim'}">
					<mod:grupo>
						<mod:texto titulo="Nome do Titular:" var="nometit" largura="60%"/>
					</mod:grupo>
				</c:if>
			</mod:grupo>
	<mod:grupo titulo="Dependentes Diretos" />
		<mod:grupo>
			<mod:texto titulo="C&ocirc;njuge/Companheiro(a):" var="conjcomp" largura="60" />
		</mod:grupo>
		<mod:selecao var="quantidadeDependentes" titulo="Quantidade de dependentes diretos" opcoes="0;1;2;3;4;5" reler="ajax" idAjax="quantDependenteAjax"/>		
			<mod:grupo depende="quantDependenteAjax">
				<c:forEach var="i" begin="1" end="${quantidadeDependentes}">
						<mod:grupo largura="60">
							<mod:texto titulo="Dependente ${i}:" var="nomedep${i}" largura="60"/>
						</mod:grupo>
						<mod:grupo largura="40">
							<mod:selecao titulo="Tipo Dependente:" var="tipodep${i}" opcoes="[Selecione];Filho menor de 18 anos;Filho entre 18 e 24 anos;Enteado;Menor sob guarda;Filho maior inválido;Outros" reler="ajax" idAjax="tipoDepAjax${i}" />
					       		<mod:grupo depende="tipoDepAjax${i}">	
					       			<c:if test="${requestScope[f:concat('tipodep',i)] eq 'Outros'}">
						         		<mod:texto var="outroTexto${i}" titulo="Outros:" largura="40" />
					       			</c:if>
					       		</mod:grupo>
						</mod:grupo>
				</c:forEach>	
			</mod:grupo>
			
			<mod:grupo titulo="Agregados e Dependentes" />
			<mod:selecao var="quantidadeAgregados" titulo="Quantidade de agregados e dependentes" opcoes="0;1;2;3;4;5" reler="ajax" idAjax="quantAgregadosAjax" />
				<mod:grupo depende="quantAgregadosAjax">
					<c:forEach var="j" begin="1" end="${quantidadeAgregados}">
						<mod:grupo largura="60">				
							<mod:texto titulo="Agregado ${j}:" var="nomeagr${j}" largura="60" />
						</mod:grupo>
						<mod:grupo largura="40">
							<mod:selecao titulo="Tipo de agregado:" var="tipoagr${j}" opcoes="[Selecione];Pai;Mãe;Outros" reler="ajax" idAjax="tipoagrAjax${j}"/>
							<mod:grupo depende="tipoagrAjax${j}">
						 		<c:if test="${requestScope[f:concat('tipoagr',j)] eq 'Outros'}">
						        	 <mod:texto var="outroTextoagr${j}" titulo="Outros:" largura="40" />
					       		</c:if>
					       	</mod:grupo>
						</mod:grupo>
					</c:forEach>		
	     </mod:grupo>
		
	</mod:entrevista>
	
	<mod:documento>	
				
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
	<table width="100%" border="1" cellpadding="3">
		<tr><td align="center">Golden Cross - movimenta&ccedil;&atilde;o cadastral     EXCLUS&Atilde;O</td></tr>
	</table>
	<br/>
	<table width="100%" border="1" cellpadding="3">
				<tr>
	                <td> <p>Identifica&ccedil;&atilde;o do Titular: ${requestScope['nometit_pessoaSel.descricao']}<br>Matr&iacute;cula: ${requestScope['nometit_pessoaSel.sigla']}</br>Lota&ccedil;&atilde;o: ${f:pessoa(requestScope['nometit_pessoaSel.id']).lotacao.descricao}</br>N&ordm; Cart&atilde;o Golden Cross: ${nrocartaogolden}</p></td>
				</tr>
	</table>
	<br/>
	<table width="100%" border="1" cellpadding="3">
				<tr>
					<td align="justify" colspan="2">Desejo proceder a<b> exclus&atilde;o</b> dos segurados abaixo discriminados,<br/>a partir de: <b> ${requestScope[f:concat('dtexclusao','/')]} ${dtexclusao} ${'/'} ${mes} ${'/'} ${ano}</b></td>
				</tr>
	</table>
	<br/>
	<table width="100%" border="1" cellpadding="3">
		<tr>
			<td align="justify" colspan="2"> As movimenta&ccedil;&otilde;es com efeito financeiro somente podem ser feitas com vig&ecirc;ncias 1&ordm;,11 e 21 de cada m&ecirc;s, sendo necess&aacute;rio que tais solicita&ccedil;&otilde;es <u>cheguem &agrave; SEBEN/SRH com at&eacute; 4 dias &uacute;teis de anteced&ecirc;ncia em rela&ccedil;&atilde;o &agrave; data pretendida.</u></td>
		</tr>
	</table>		
	<br/>
	<c:if test="${situacaoTitular=='Sim'}">
		<table width="100%" border="1" cellpadding="3">
		 	<tr>
				<td width="100%" align="center" colspan="2">TITULAR</td>
			</tr>
			<tr>
				<td width="100%" >Nome do titular: ${nometit}</td>
			</tr>	
		</table>
	</c:if>
	<br/>	
	<c:if test="${quantidadeDependentes > '0'}">
	
			<table width="100%" border="1" cellpadding="3">
				<tr><td align="center" colspan="2">DEPENDENTES DIRETOS</td></tr>
				<tr><td>Cônjuge/Companheiro(a): ${conjcomp }</td></tr>
			</table>
			<c:forEach var="i" begin="1" end="${quantidadeDependentes}">
					<table width="100%" border="1" cellpadding="3">
						<tr>
							<td width="80%">${requestScope[f:concat('nomedep',i)]}</td><c:choose>
							<c:when test="${requestScope[f:concat('tipodep',i)] == 'Outros'}">
							<td width="20%" align="center">${requestScope[f:concat('outroTexto',i)]}</td>
							</c:when>
							<c:otherwise>
							<td width="20%" align="center">${requestScope[f:concat('tipodep',i)]}</td>
							</c:otherwise>
							</c:choose>
						</tr>	
					</table>
			</c:forEach>
		
	</c:if>
	<br/>
		<c:if test="${quantidadeAgregados > '0'}">
	
			<table width="100%" border="1" cellpadding="3">
				<tr><td align="center">AGREGADOS E DEPENDENTES</td></tr>
			</table>
			<c:forEach var="j" begin="1" end="${quantidadeAgregados}">
					<table width="100%" border="1" cellpadding="3">
						<tr>
							<td width="80%">${requestScope[f:concat('nomeagr',j)]}</td><c:choose>
							<c:when test="${requestScope[f:concat('tipoagr',j)] == 'Outros'}">
							<td width="20%" align="center">${requestScope[f:concat('outroTextoagr',j)]}</td>
							</c:when>
							<c:otherwise>
							<td width="20%" align="center">${requestScope[f:concat('tipoagr',j)]}</td>
							</c:otherwise>
							</c:choose>
						</tr>	
					</table>
			</c:forEach>
		
		</c:if>
	<br/>
		<table width="100%" border="1" cellpadding="3">
			<tr>
				<td align="justify"> Estou ciente de que a <u>exclusão do titular implica também a exclusão de todos os dependentes</u>, sejam eles dependentes diretos, agregados ou dependentes econômicos. Comprometo-me ainda a devolver as carteiras &agrave; SEBEN/SRH <u>em at&eacute; 3 dias após a data de vigência da exclusão</u>.</td>
			</tr>
		</table>
	<br/>
	
	<c:import url="/paginas/expediente/modelos/inc_localDataAssinatura.jsp" />
	<br/>
	<p>Recebido pela SEBEN em:_____/_____/____ - por:_______________________</p>
	
	   	<table width="100%" border="1" cellpadding="3">
	   		<tr>
	   			<td align="justify">Caso o beneficiário a ser excluído pertença ao Golden Med, e a exclusão do plano de saúde não seja para dia 1&ordm;, a cessação de cobrança Golden Med, relativa a este beneficiário, somente ocorrerá a partir do dia 1&ordm; do mês subseqüente &agrave; data de exclusão do plano, por não haver cobrança <i>pro rata</i> no Golden Med.</td>
	   		</tr>
	   	</table>
	<br/>
	
	<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp"/>
	</body>
	</html>
	</mod:documento>
</mod:modelo>

