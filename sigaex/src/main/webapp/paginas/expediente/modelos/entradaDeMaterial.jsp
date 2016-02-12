<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<c:set var="apenasNome" value="Sim" scope="request" />
	<mod:entrevista>
		<mod:grupo titulo="Tipo de Formulário">	
			<mod:radio titulo="Entrada de Material" var="tipoFormulario" valor="1" marcado="Sim"/>
			<mod:radio titulo="Saída de Material" var="tipoFormulario" valor="2"/>
		</mod:grupo>
		
		<mod:data titulo="Data" var="data" obrigatorio="Sim"></mod:data>
		
		<mod:grupo titulo="Localidade">
		
		<mod:selecao
				titulo="Selecione o tipo de origem e destino"
				var="tipo" opcoes="Interno/Externo;Externo/Interno;Interno/Interno" reler="ajax"
				idAjax="tipoAjax" />
			<mod:grupo depende="tipoAjax">
				<c:if test="${tipo eq 'Interno/Externo'}">
					<mod:lotacao titulo="Origem Interna" var="origem" /><mod:texto titulo="Localização" var="localizacao"/><br />
					<mod:texto var="destino" titulo="Destino Externo" largura="60" />
				</c:if>	
				<c:if test="${tipo eq 'Externo/Interno'}">
					<mod:texto var="origem" titulo="Origem Externa" largura="60" /><br />
					<mod:lotacao titulo="Destino Interno" var="destino" /><mod:texto titulo="Localização" var="localizacao"/><br />
				</c:if>
				<c:if test="${tipo eq 'Interno/Interno'}">
					<mod:lotacao titulo="Origem Interna" var="origem" /><mod:texto titulo="Localização" var="localizacaoOri" largura="20"/><br />
					<mod:lotacao titulo="Destino Interno" var="destino" /><mod:texto titulo="Localização" var="localizacaoDest" largura="20"/><br />
					
				</c:if>
			</mod:grupo>
			
		</mod:grupo>
	
		<mod:grupo titulo="Material">		
			<mod:selecao titulo="Total de materiais"
			var="totalDeMaterial" opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20" reler="sim" />
			<c:forEach var="i" begin="1" end="${totalDeMaterial}">
				<mod:grupo>
					<mod:texto titulo="Descrição" var="descricao${i}" largura="60" />
				</mod:grupo>
				<mod:grupo>
					<mod:texto titulo="Nº de Série/Nº do Patrimônio" var="serie${i}" largura="60" />
				</mod:grupo>
				<mod:grupo>
					<mod:selecao titulo="Quantidade"
						var="quantidade${i}" opcoes="1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20" />
				</mod:grupo>
				<br />
			</c:forEach>
		</mod:grupo>
		
		<mod:grupo titulo="">
				<mod:texto titulo="Observações" var="obs" largura="80" />
		</mod:grupo>
		<br />
		<mod:grupo titulo="">
				<mod:texto titulo="Responsável" var="responsavel" largura="80" /><br />
				<mod:texto titulo="Identidade" var="identidade" largura="10" /><b>(Ex: 99999999-9)</b> 
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

					<table width="100%">
						<tr>
							<td align="right"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">
							<c:choose>	
								<c:when test="${tipoFormulario == 1}">
									ENTRADA DE MATERIAL
								</c:when>
								<c:otherwise>
									SAÍDA DE MATERIAL
								</c:otherwise>
							</c:choose>
							<br /><br />N&ordm; ${doc.codigo}</p></td>
						</tr>
					</table>

		<br />

		<table width="100%" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF" align="center"><b>Fica autorizado a 
							<c:choose>	
								<c:when test="${tipoFormulario == 1}">
									ENTRADA
								</c:when>
								<c:otherwise>
									SAÍDA
								</c:otherwise>
							</c:choose>				
					do(s) material(is), abaixo relacionado(s)<br />
					nesta Seção Judiciária 
				    <c:if test="${not empty data}"> na data </c:if>${data }.</b></td>
			</tr>
		</table>
		
		<br />
		
		<table width="100%" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td width="50%" bgcolor="#FFFFFF"><b>Origem</b></td>
				<td width="50%" bgcolor="#FFFFFF"><b>Destino</b></td>
			</tr>
			<tr>
				<c:if test="${tipo eq 'Interno/Externo'}">
					<td width="50%" bgcolor="#FFFFFF" align="center">${requestScope['origem_lotacaoSel.sigla']}</td>
					<td width="50%" bgcolor="#FFFFFF" align="center">${destino}</td>
					<tr>
						<td width="50%" bgcolor="#FFFFFF"><b>Localização:</b> ${localizacao}</td>
						<td width="50%" bgcolor="#FFFFFF"></td>						
					</tr>
				</c:if>
				<c:if test="${tipo eq 'Externo/Interno'}">
					<td width="50%" bgcolor="#FFFFFF" align="center">${origem}</td>
					<td width="50%" bgcolor="#FFFFFF" align="center">${requestScope['destino_lotacaoSel.sigla']}</td>
					<tr>
					    <td width="50%" bgcolor="#FFFFFF"></td>
						<td width="50%" bgcolor="#FFFFFF"><b>Localização:</b> ${localizacao}</td>						
					</tr>
				</c:if>
				<c:if test="${tipo eq 'Interno/Interno'}">
					<td width="50%" bgcolor="#FFFFFF" align="center">${requestScope['origem_lotacaoSel.sigla']}</td>
					<td width="50%" bgcolor="#FFFFFF" align="center">${requestScope['destino_lotacaoSel.sigla']}</td>
					<tr>
						<td width="50%" bgcolor="#FFFFFF"><b>Localização:</b> ${localizacaoOri}</td>
						<td width="50%" bgcolor="#FFFFFF"><b>Localização:</b> ${localizacaoDest}</td>						
					</tr>
				</c:if>
			</tr>
		<%--	<tr>
				<td width="50%" bgcolor="#FFFFFF"><b>Localização</b></td>
				<td width="50%" bgcolor="#FFFFFF" align="center"><c:if test="${empty localizacao}">&nbsp;</c:if>${localizacao }</td>
			</tr> --%>
			
		</table>

		<br />
		
		<table width="100%" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td width="50%" bgcolor="#FFFFFF" align="center"><b>Descrição do Material</b></td>
				<td width="35%" bgcolor="#FFFFFF" align="center"><b>Nº de Série/Nº do Patrimônio</b></td>
				<td width="15%" bgcolor="#FFFFFF" align="center"><b>Quantidade</b></td>
			</tr>
		</table>
		<c:forEach var="i" begin="1" end="${totalDeMaterial}">
				<table width="100%" border="1" cellpadding="2" cellspacing="1" bgcolor="#000000">
					<tr>
						<td width="50%" bgcolor="#FFFFFF" align="center">${requestScope[f:concat('descricao',i)]}</td>
						<td width="35%" bgcolor="#FFFFFF" align="center">${requestScope[f:concat('serie',i)]}</td>
						<td width="15%" bgcolor="#FFFFFF" align="center">${requestScope[f:concat('quantidade',i)]}</td>
					</tr>
				</table>
			</c:forEach>

		<br />
		
		<table width="100%" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF" ><b>Observações</b></td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF">
				<p>&nbsp ${obs}</p>
				</td>
			</tr>
		</table>

		<br />

		<table width="100%" height="270px" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF" width="30%" align="center"><b>Autorizado por</b></td>
				<td bgcolor="#FFFFFF" width="70%" align="center"><c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" /></td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" width="30%" align="center"><b>Responsável</b></td>
				<td bgcolor="#FFFFFF" width="70%">&nbsp ${responsavel}</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" width="30%" align="center"><b>Identidade</b></td>
				<td bgcolor="#FFFFFF" width="70%">&nbsp ${identidade}</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" width="30%" align="center"><b>Assinatura</b></td>
				<td bgcolor="#FFFFFF" width="70%">&nbsp</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" width="30%" align="center"><b>Controle Portaria</b></td>
				<td bgcolor="#FFFFFF" width="70%" align="center">
				<b>
				<c:choose>	
					<c:when test="${tipoFormulario == 1}">
						ENTRADA
					</c:when>
					<c:otherwise>
						SAÍDA
					</c:otherwise>
				</c:choose>
				</b></td>
			</tr>
				<tr>
				<td bgcolor="#FFFFFF" width="30%" align="center"><b>Conferido por</b></td>
				<td bgcolor="#FFFFFF" width="70%">&nbsp</td>
			</tr>
				<tr>
				<td bgcolor="#FFFFFF" width="30%" align="center"><b>Data/Hora</b></td>
				<td bgcolor="#FFFFFF" width="70%">&nbsp</td>
			</tr>
		</table>
		
		<br />
		
		

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
