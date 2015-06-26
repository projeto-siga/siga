<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${false}">
	<html>
	<body>
	<table>
		<tr>
			<td></c:if></TD>
		</TR>
		<TR>
			<TD height="27" COLSPAN=4><IMG
				SRC="<c:url value='/imagens/base1.gif'/>" WIDTH="100%" HEIGHT=27
				ALT=""></TD>
		</TR>
		<TR>
			<TD height="22" COLSPAN=4
				background="<c:url value='/imagens/base2.gif'/>">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="base">&nbsp;&nbsp;<c:catch><c:out default="Convidado"
						value="${cadastrante.nomePessoa}" /><c:choose>
						<c:when test="${empty cadastrante}"> - Sem Lotação</c:when>
						<c:when test="${not empty titular && titular.idPessoa!=cadastrante.idPessoa}"> COMO ${titular.nomePessoa}</c:when>
						<c:when
							test="${not empty lotaTitular && lotaTitular.idLotacao!=cadastrante.lotacao.idLotacao}"> COMO ${lotaTitular.nomeLotacao}</c:when>
						<c:when test="${not empty cadastrante.lotacao}"> - ${cadastrante.lotacao.nomeLotacao}</c:when>
					</c:choose></c:catch></td>
					<td align="right"><img
						src="<c:url value='/imagens/sinlogo.gif'/>" border="0">&nbsp;&nbsp;</td>
				</tr>
			</table>
			</TD>
		</TR>
	</TABLE>
	</body>
	</html>

	<c:if test="${false}">
		<DIV align="center" style="margin-top: 12px;">
		<table border="0" width="730" bgcolor="#FFCC66" cellspacing="1">
			<tr>
				<td width="242" align="center">&nbsp;</td>
				<td width="399" align="center"><font color="#006699" size="2"
					face="Arial"> <c:catch> <c:out default="Convidado"
					value="${usuarioLogin.sigla}" /> - <c:out
					value="${usuarioLogin.lotacao.descricaoLotacao}"
					default="Sem Lotação" /> </c:catch> </font></td>
				<td width="69"><img border="0"
					src="<c:url value='/imagens/imgLogoSin.gif'/>" width="44"
					height="15"></td>
			</tr>
		</table>
		</DIV>

	</c:if>