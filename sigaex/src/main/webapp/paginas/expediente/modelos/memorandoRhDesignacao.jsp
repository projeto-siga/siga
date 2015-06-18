<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="esconderTexto" value="sim" scope="request" />
<%-- a variavel esconderTexto foi configurada para que o formulario nao exiba o editor de textos na entrevista --%>

<mod:modelo urlBase="/paginas/expediente/modelos/memorando.jsp">
	<mod:entrevista>
		<mod:selecao var="qtdServidores" titulo="Quantidade de servidores indicados"
			reler="ajax" idAjax="qtdServidoresAjax" opcoes="1;2;3;4;5;6;7;8;9;10" />
		<mod:grupo depende="qtdServidoresAjax">
			<c:forEach var="i" begin="1" end="${qtdServidores}">
				<mod:pessoa titulo="Servidor" var="servidor${i}" />
				<br />
				<mod:funcao titulo="Função Indicada" var="funcao_indicada${i}"></mod:funcao>
				<hr color="#FFFFFF" />
			</c:forEach>
		</mod:grupo>

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
					<mod:radio titulo="Substituto(a)" var="botao" valor="Substituto(a)" />
				</mod:grupo>
			</c:if>
		</mod:grupo>
		<br />
		<b> <mod:mensagem titulo="Atenção"
			texto="preencha o destinatário com SELOT e, após finalizar, transfira para a SELOT." />
		</b>
		<br />
	</mod:entrevista>
	<mod:documento>
		<mod:valor var="texto_memorando">
			<p style="text-indent: 2cm; text-align: justify"><c:if
				test="${autoridade eq 'SIM'}">
					De ordem do(a) Exmo(a).
					Juiz(a) Federal ${requestScope['botao']} do(a) <b>${f:lotacaoPessoa(requestScope['autoridade_pessoaSel.id'])}</b>,
					Dr(a). <b>${requestScope['autoridade_pessoaSel.descricao']}</b>, indico 
					
				</c:if> <c:if test="${autoridade eq 'NÃO'}">
					Indico 
				</c:if> o(os) servidor(es) abaixo relacionado(s), para exercer(em) a(s)
			função(ões) comissionada(s) conforme discriminado, a partir da
			publicação da respectiva portaria:</p>
			<c:forEach var="i" begin="1" end="${qtdServidores}">
				<c:set var="pes"
					value="${f:pessoa(requestScope[f:concat(f:concat('servidor',i),'_pessoaSel.id')])}" />

				<table bgcolor="#000000" border="1" cellpadding="5" width="100%"
					align="center">

					<tr>
						<td style="font-size: 11pt;" bgcolor="#FFFFFF" width="50%"
							align="left" colspan="1">MATRÍCULA: ${pes.matricula}</td>
					</tr>
					<tr>
						<td style="font-size: 11pt;" bgcolor="#FFFFFF" width="50%"
							align="left" colspan="1">NOME: ${pes.descricao}</td>
					</tr>

					<tr>
						<td style="font-size: 11pt;" bgcolor="#FFFFFF" width="50%"
							align="left" colspan="1">LOTAÇÃO: ${f:removeAcentoMaiusculas(pes.lotacao.descricao) }</td>
					</tr>
					<tr>
						<td style="font-size: 11pt;" bgcolor="#FFFFFF" width="50%"
							align="left" colspan="1">FUNÇÃO ATUAL:
						${pes.funcaoConfianca.descricao}</td>
					</tr>
					<tr>
						<td style="font-size: 11pt;" bgcolor="#FFFFFF" width="50%"
							align="left" colspan="1">FUNÇÃO INDICADA:
						${requestScope[f:concat(f:concat('funcao_indicada',i),'_funcaoSel.descricao')]}
						</td>
					</tr>
				</table>
				<c:if test="${i mod 4 == '0' and i < qtdServidores}">
					<c:import url="/paginas/expediente/modelos/inc_quebra_pagina.jsp" />
				</c:if>
				<br />
			</c:forEach>
		</mod:valor>
	</mod:documento>
</mod:modelo>

