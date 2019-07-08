<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="popup"%>
<%@ attribute name="pagina_de_erro"%>

<%--
<c:if test="${not empty pagina_de_erro}">
	<c:set var="pagina_de_erro" scope="request" value="${pagina_de_erro}"/>
</c:if>

<c:if test="${popup!='true' and empty pagina_de_erro}">
	<c:if test="${false}">
		<html>

		<body>

		<table>
			<tr>
				<td></c:if></TD>
			</TR>
			<TR>
				<TD height="27" COLSPAN=4><IMG
					SRC="<c:url value='/imagens/base1.gif'/>"
					onMouseOut="javascript:document.getElementById('menuSuspenso').style.visibility='hidden';"
					WIDTH="100%" HEIGHT=27 ALT=""></TD>
			</TR>
			<TR>
				<TD height="22" COLSPAN=4
					background="<c:url value='/imagens/base2.gif'/>">
				<table name="rodapeSuspenso" width="100%" border="0" cellspacing="0"
					cellpadding="0">
					<tr>
						<td class="base"><span id="spanMenuSuspenso"
							onMouseOver="javascript:document.getElementById('menuSuspenso').style.visibility='visible';"
							style="cursor: pointer">&nbsp;&nbsp;<c:catch>
							<c:out default="Convidado" value="${cadastrante.nomePessoa}" />
							<c:choose>
								<c:when test="${empty cadastrante}"> - Sem LotaÃ§Ã£o</c:when>
								<c:when
									test="${not empty titular && titular.idPessoa!=cadastrante.idPessoa}">
					 COMO ${titular.nomePessoa}</c:when>
								<c:when
									test="${not empty lotaTitular && lotaTitular.idLotacao!=cadastrante.lotacao.idLotacao}">
							 COMO ${lotaTitular.nomeLotacao}</c:when>
								<c:when test="${not empty cadastrante.lotacao}">
						 - ${cadastrante.lotacao.nomeLotacao}</c:when>
							</c:choose>
						</c:catch> </span></td>
						<td align="right"><img
							src="<c:url value='/imagens/sinlogo.gif'/>" border="0">&nbsp;&nbsp;</td>
					</tr>
				</table>
				</TD>
			</TR>
		</TABLE>

		<div id="menuSuspenso"
			style="position: absolute; border: 1px solid #000000; background: #3B437B; height: auto; z-index: 1; left: 30px; bottom: 21px; visibility: hidden; font-color: #FFFFFF;"
			onMouseOver="javascript:this.style.visibility='visible';"
			onMouseOut="javascript:this.style.visibility='hidden';"><c:forEach
			var="substituicao" items="${meusTitulares}">
			
<a
	href="/siga/substituicao/substituir_gravar.action?idTitular=${substituicao.titular.idPessoa}&idLotaTitular=${substituicao.lotaTitular.idLotacao}"
	style="color: #FFFFFF;"> <c:choose>
		<c:when test="${not empty substituicao.titular}">
						${substituicao.titular.nomePessoa}
					</c:when>
		<c:otherwise>
						${substituicao.lotaTitular.nomeLotacao}
					</c:otherwise>
	</c:choose> <a> <br /> </c:forEach> <c:if
			test="${(not empty lotaTitular && lotaTitular.idLotacao!=cadastrante.lotacao.idLotacao) ||(not empty titular && titular.idPessoa!=cadastrante.idPessoa)}">
			<ww:a cssStyle="color:#FFFFFF"
				href="/siga/substituicao/finalizar.action">
					Finalizar substitui&ccedil;&atilde;o de 
					<c:choose>
					<c:when
						test="${not empty titular && titular.idPessoa!=cadastrante.idPessoa}">${titular.nomePessoa}</c:when>
					<c:otherwise>${lotaTitular.siglaLotacao}</c:otherwise>
				</c:choose>
			</ww:a>
		</c:if>
		</div> </c:if> --%>
</body>
</html>