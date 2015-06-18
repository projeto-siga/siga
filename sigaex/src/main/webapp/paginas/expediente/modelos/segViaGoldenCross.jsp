<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
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
			<mod:selecao titulo="Motivo" var="motivo" opcoes="[Selecione];Furto;Roubo;Extravio;Outros" reler="ajax" idAjax="motivoAjax" />
				<mod:grupo depende="motivoAjax">
					<c:if test="${motivo eq 'Outros'}">
						<mod:texto var="outroMotivo" titulo="Outro:" largura="30" />
					</c:if>
				</mod:grupo>
		</mod:grupo>
	</mod:grupo>
	
	<mod:grupo titulo="Pedido de 2&ordf; Via do Titular">
		<mod:selecao var="situacaoTitular" titulo="Pedido do Titular ?" opcoes="Não;Sim" reler="ajax" idAjax="sitTitularAjax" />
			<mod:grupo depende="sitTitularAjax">
				<c:if test="${situacaoTitular eq 'Sim'}">
					<mod:texto titulo="Nome do Titular:" var="nometit" largura="60" />
				</c:if>
			</mod:grupo>
	</mod:grupo>
	
	<mod:grupo titulo="Pedido de 2&ordf; Via de Dependentes Diretos">
		<mod:grupo>
			<mod:texto titulo="C&ocirc;njuge/Companheiro(a):" var="conjcomp" largura="60" />
		</mod:grupo>
			<mod:selecao var="quantidadeDependentes" titulo="Quantidade de dependentes diretos" opcoes="0;1;2;3;4;5" reler="ajax" idAjax="quantDependAjax" />
				<mod:grupo depende="quantDependAjax">
					<c:forEach var="i" begin="1" end="${quantidadeDependentes}">
						<mod:grupo largura="60">
							<mod:texto titulo="Dependente ${i}:" var="nomedep${i}" largura="60"/>
						</mod:grupo>
						<mod:grupo largura="40">
							<mod:selecao titulo="Tipo Dependente:" var="tipodep${i}" opcoes="[Selecione];Filho menor de 18 anos;Filho enter 18 e 24 anos;Enteado;Menor sob guarda;Filho maior inválido;Outros" reler="ajax" idAjax="tipoDepAjax${i}" />
					       		<mod:grupo depende="tipoDepAjax${i}">
					       			<c:if test="${requestScope[f:concat('tipodep',i)] eq 'Outros'}">
						         		<mod:texto var="outroTexto${i}" titulo="Outros:" largura="40" />
					       			</c:if>
					       		</mod:grupo>
						</mod:grupo>
					</c:forEach>	
				</mod:grupo>
	</mod:grupo>
	
	<mod:grupo titulo="Pedido de 2&ordf; Via de Agregados e Dependentes">
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
		<tr><td align="center">Golden Cross - movimenta&ccedil;&atilde;o cadastral     SEGUNDA-VIA</td></tr>
	</table>
	
	<table width="100%" border="1" cellpadding="3">
		<tr>
			<td>Eu, <b>${requestScope['nometit_pessoaSel.descricao']}</b>, matr&iacute;cula: <b>${requestScope['nometit_pessoaSel.sigla']}</b>, cart&atilde;o n&ordm;: <b>${nrocartaogolden}</b>, lotado(a) no(a) <b>${f:pessoa(requestScope['nometit_pessoaSel.id']).lotacao.descricao}</b>, solicito a emiss&atilde;o de segunda-via da(s) carteira(s) do Plano de Sa&uacute;de dos associados abaixo elencados por motivo de: <b>${motivo}</b></td>
		</tr>
	</table>
	<br/>
	
	<c:if test="${situacaoTitular eq 'Sim'}">
		<table width="100%" border="1" cellpadding="3">
		 	<tr>
				<td width="100%" align="center">TITULAR</td>
			</tr>
			<tr>
				<td width="100%">Nome do titular: ${nometit}</td>
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
	
	<c:import url="/paginas/expediente/modelos/inc_localDataAssinatura.jsp" />
	<br/>
	<p>Recebido pela SEBEN em:_____/_____/____ - por:_______________________</p>
	
	<br/>
	<c:import url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp"/>
	</body>
	</html>
	</mod:documento>
</mod:modelo>

