<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="siga"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<mod:modelo>
	<%-- <c:set var="esconderTexto" value="sim" scope="request" /> --%>
	<%-- <mod:modelo urlBase="/paginas/expediente/modelos/memorando.jsp"> --%>
	<mod:entrevista>
		<mod:selecao titulo="<b>Quantidade de servidores a informar</b>"
			opcoes="1;2;3;4;5;6;7;8;9;10" var="servidores" reler="sim" />
		<c:forEach var="i" begin="1" end="${servidores}">
			<mod:grupo>
				<mod:pessoa titulo="Servidor" var="servidor${i}" />
			</mod:grupo>
			<mod:grupo>
				<mod:texto titulo="Função" var="funcao${i}" />
			</mod:grupo>
			<br />
		</c:forEach>
		<mod:grupo>
				<mod:selecao titulo="Documento feito de ordem de Magistrado?"
					var="autoridade" opcoes="NÃO;SIM" reler="ajax"
					idAjax="autoridadeAjax" />
				<mod:grupo depende="autoridadeAjax">
					<c:if test="${requestScope[f:concat('autoridade',i)] eq 'SIM'}">
						<mod:pessoa titulo="Matrícula da Autoridade competente"
							var="autoridade" />
						<mod:grupo>
							<mod:radio marcado="Sim" titulo="Titular" var="botao${i}"
								valor="Titular" />
							<mod:radio titulo="Substituto(a)" var="botao${i}"
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


		<c:choose>
			<c:when test="${tamanhoLetra=='Pequeno'}">
				<c:set var="tl" value="9pt" />
			</c:when>
			<c:when test="${tamanhoLetra=='Grande'}">
				<c:set var="tl" value="13pt" />
			</c:when>
			<c:otherwise>
				<c:set var="tl" value="11pt" />
			</c:otherwise>
		</c:choose>
		
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
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<table width="100%">
						<tr>
							<td align="right"><p style="font-family:Arial;font-weight:bold;font-size:11pt;">MEMORANDO N&ordm; ${doc.codigo}</p></td>
						</tr>
						<tr>
							<td align="right"><mod:letra tamanho="${tl}"><p>${doc.dtExtenso}</p></mod:letra></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerda.jsp" />
		FIM CABECALHO -->



		<mod:letra tamanho="${tl}">

			<p align="left">DE: <c:choose>
				<c:when test="${not empty doc.nmLotacao}">
			${doc.nmLotacao}
			</c:when>
				<c:otherwise>${doc.titular.lotacao.descricao}</c:otherwise>
			</c:choose> <br>
			PARA: ${doc.destinatarioString}</p>

		</mod:letra>

		
			<%-- <mod:valor var="texto_memorando"> --%>
			<p style="TEXT-INDENT: 2cm" align="justify">
			<c:if	test="${autoridade eq 'SIM'}">
					De ordem do(a) Exmo(a).
					Juiz(a) Federal ${requestScope['botao']} do(a) <b>${f:lotacaoPessoa(requestScope['autoridade_pessoaSel.id'])}</b>,
					Dr(a). <b>${requestScope['autoridade_pessoaSel.descricao']}</b>, indico 
			</c:if>
			<c:if test="${autoridade eq 'NÃO'}">
					Indico 
			</c:if>
			<c:if test="${servidores == 1}">
				o(a) servidor(a) abaixo relacionado(a) para exercer a seguinte função comissionada
			</c:if>
			<c:if test="${servidores > 1}">
				os(as) servidores(as) abaixo relacionados(as) para exercerem as seguintes funções comissionadas
			</c:if>
			a partir da publicação da respectiva portaria.
			</p>

			<p style="TEXT-INDENT: 2cm" align="justify">
			<table width="100%" border="1" cellpadding="2" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td bgcolor="#FFFFFF" align="center" width="23%">Servidor</td>
				<td bgcolor="#FFFFFF" align="center" width="12%">Matrícula</td>
				<td bgcolor="#FFFFFF" align="center" width="20%">Cargo</td>
				<td bgcolor="#FFFFFF" align="center" width="10%">Nível / Classe / Padrão</td>
				<td bgcolor="#FFFFFF" align="center" width="18%">Lotação</td>
				<td bgcolor="#FFFFFF" align="center" width="17%">Função Comissionada</td>
			</tr>
			</table>			
			<c:forEach var="i" begin="1" end="${servidores}">
				<c:set var="pes" value="${f:pessoa(requestScope[f:concat(f:concat('servidor',i),'_pessoaSel.id')])}"/>
				<table width="100%" border="1" cellpadding="2" cellspacing="1"	bgcolor="#000000">
					<tr>
						<td bgcolor="#FFFFFF" align="center" width="23%">${pes.descricao}</td>
						<td bgcolor="#FFFFFF" align="center" width="12%">${pes.sigla}</td>
						<td bgcolor="#FFFFFF" align="center" width="20%">${pes.cargo.nomeCargo}</td>
						<td bgcolor="#FFFFFF" align="center" width="10%">${pes.padraoReferenciaInvertido}</td>
						<td bgcolor="#FFFFFF" align="center" width="18%">${pes.lotacao.descricao}</td>
						<td bgcolor="#FFFFFF" align="center" width="17%">${requestScope[f:concat('funcao',i)]}</td>
					</tr>
				</table>
			</c:forEach>
			</p>			
			<%-- </mod:valor> --%>

		

		<p style="align: justify; TEXT-INDENT: 2cm">Atenciosamente,</p>
		<p>&nbsp;</p>
		<c:import url="/paginas/expediente/modelos/inc_assinatura.jsp" />
		
		<c:import
			url="/paginas/expediente/modelos/inc_classificacaoDocumental.jsp" />
		</body>
		</html>
	</mod:documento>
</mod:modelo>

