<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>

<c:set var="esconderTexto" value="sim" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/memorando.jsp">
	<mod:entrevista>
		<mod:grupo>
			<mod:selecao var="qtdServidores" titulo="Quantidade de substituições"
				reler="ajax" idAjax="qtdServidoresAjax"
				opcoes="1;2;3;4;5;6;7;8;9;10" />
		</mod:grupo>
		<mod:grupo depende="qtdServidoresAjax">
			<c:forEach var="i" begin="1" end="${qtdServidores}">
				<mod:grupo>
					<mod:pessoa titulo="Substituto" var="substituto${i}" />
				</mod:grupo>
				<mod:grupo>
					<mod:pessoa titulo="Titular" var="titular${i}" />
				</mod:grupo>
				<mod:grupo titulo="Período Solicitado">
					<mod:data titulo="Data inicial" var="dataInicio${i}" />
					<mod:data titulo="Data final (opcional)" var="dataFim${i}" />
				</mod:grupo>
				<hr color="#FFFFFF" />
			</c:forEach>
		</mod:grupo>
		<mod:grupo>
			<mod:selecao titulo="Documento feito de ordem de Magistrado?"
				var="autoridade" opcoes="NÃO;SIM" reler="ajax"
				idAjax="autoridadeAjax" />
			<mod:grupo depende="autoridadeAjax">
				<c:if test="${autoridade eq 'SIM'}">
					<mod:pessoa titulo="Matrícula da Autoridade competente"
						var="autoridade" />
					<mod:grupo>
						<mod:radio marcado="Sim" titulo="Titular" var="botao"
							valor="Titular" />
						<mod:radio titulo="Substituto(a)" var="botao"
							valor="Substituto(a)" />
					</mod:grupo>
				</c:if>
			</mod:grupo>
		</mod:grupo>
		<mod:grupo>
			<b> <mod:mensagem titulo="Atenção"
				texto="preencha o destinatário com SELOT e, após finalizar, transfira para a SELOT." />
			</b>
		</mod:grupo>
	</mod:entrevista>
	<mod:documento>
		<mod:valor var="texto_memorando">
			<c:if test="${autoridade eq 'SIM'}">
				<p style="TEXT-INDENT: 2cm" align="justify">De ordem do(a)
				Exmo(a). Juiz(a) Federal ${requestScope['botao']} do(a) <b>${f:lotacaoPessoa(requestScope['autoridade_pessoaSel.id'])}</b>,
				Dr(a). <b>${requestScope['autoridade_pessoaSel.descricao']}</b>,
				indico
			</c:if>

			<c:if test="${autoridade eq 'NÃO'}">
				<p style="TEXT-INDENT: 2cm" align="justify">Indico
			</c:if>
			o(s) servidor(es) abaixo relacionado(s) para atuar(em) como substituto(s) eventual(is) 
			do(s) cargo(s) em comissão/função(ões) comissionada(s), conforme discriminado:			
			<br /> <br />
			<c:forEach var="i" begin="1" end="${qtdServidores}">
				<c:set var="substituto"
					value="${f:pessoa(requestScope[f:concat(f:concat('substituto',i),'_pessoaSel.id')])}" />
				<c:set var="titular"
					value="${f:pessoa(requestScope[f:concat(f:concat('titular',i),'_pessoaSel.id')])}" />

				<table bgcolor="#000000" border="1" cellpadding="5" width="100%"
					align="center">
					<tr>
						<td style="font-size: 11pt;" bgcolor="#FFFFFF" width="50%"
							align="left" colspan="1">SUBSTITUTO: <b>${substituto.descricao}</b></td>
					</tr>
					<tr>
						<td style="font-size: 11pt;" bgcolor="#FFFFFF" width="50%"
							align="left" colspan="1">MATRÍCULA: ${substituto.matricula}</td>
					</tr>
					<tr>
						<td style="font-size: 11pt;" bgcolor="#FFFFFF" width="50%"
							align="left" colspan="1">TITULAR: <b>${titular.descricao}</b></td>
					</tr>
					<tr>
					<td style="font-size: 11pt;" bgcolor="#FFFFFF" width="50%"
							align="left" colspan="1">MATRÍCULA: ${titular.matricula}</td>
					</tr> 

					<tr>
						<td style="font-size: 11pt;" bgcolor="#FFFFFF" width="50%"
							align="left" colspan="1">LOTAÇÃO:
						${f:removeAcentoMaiusculas(titular.lotacao.descricao) }</td>
					</tr>
					<tr>
						<td style="font-size: 11pt;" bgcolor="#FFFFFF" width="50%"
							align="left" colspan="1">FUNÇÃO COMISSIONADA:
						<b>${titular.funcaoConfianca.descricao}</b></td>
					</tr>
					<tr>
						<td style="font-size: 11pt;" bgcolor="#FFFFFF" width="50%"
							align="left" colspan="1">PERÍODO: 
						<ww:if test="${requestScope[f:concat('dataInicio',i)] == requestScope[f:concat('dataFim',i)] or (empty requestScope[f:concat('dataFim',i)])}">
							 a partir de <b>${requestScope[f:concat('dataInicio',i)]}</b>
						</ww:if> 
						<ww:else>
							<b>${requestScope[f:concat('dataInicio',i)]} a 
							${requestScope[f:concat('dataFim',i)]}</b>
						</ww:else>			
						</td>
					</tr>
				</table>
				<c:if test="${i mod 3 == '0' and i < qtdServidores}">
					<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
				</c:if>
				<br />
			</c:forEach>

		</mod:valor>
	</mod:documento>
</mod:modelo>




