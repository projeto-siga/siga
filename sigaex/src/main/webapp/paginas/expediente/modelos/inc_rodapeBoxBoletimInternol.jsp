<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	buffer="64kb"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="idOrgaoUsu" value=""></c:set>
<c:set var="descricaoOrgaoUsu" value=""></c:set>
<c:set var="acronimoOrgaoUsu" value=""></c:set>
<c:choose>
	<c:when test="${empty mov}">
		<c:set var="descricaoOrgaoUsu" value="${doc.lotaTitular.orgaoUsuario.descricao}"/>
		<c:set var="idOrgaoUsu" value="${doc.lotaTitular.orgaoUsuario.idOrgaoUsu}"/>
		<c:set var="acronimoOrgaoUsu" value="${doc.lotaTitular.orgaoUsuario.acronimoOrgaoUsu}"/>
	</c:when>
	<c:otherwise>
		<c:set var="descricaoOrgaoUsu" value="${mov.lotaTitular.orgaoUsuario.descricao}"/>
		<c:set var="idOrgaoUsu" value="${mov.lotaTitular.orgaoUsuario.idOrgaoUsu}"/>
		<c:set var="acronimoOrgaoUsu" value="${mov.lotaTitular.orgaoUsuario.acronimoOrgaoUsu}"/>
	</c:otherwise>
</c:choose>

<span align="center">
<table cellspacing="0" width="96%" bgcolor="#FFFFFF" style="border-width: 1px; border-style: solid; ">
	<tr>
		<td width="100%">
			<c:import url="/paginas/expediente/modelos/inc_cabecalhoEsquerdaPrimeiraPagina2.jsp" />
		</td>
	</tr> 
	<tr>
		<td width="100%" style="padding-left: 0px; margin-left: 0px;">
		<table cellspacing="0" >
			<col width="35%"></col>
			<col width="65%"></col>
			<tr>
				<td width="35%" align="left" valign="center" style="margin-left:4px; font-size: 10pt; border-width: 1px 1px 0px 0px; border-style: solid">
					&nbsp;<br/>
					<c:choose>
						<c:when test="${idOrgaoUsu == 1 || idOrgaoUsu == 2}">
							${requestScope['nmDiretorForo']}<br/>
							Juiz Federal - Diretor do Foro<br/>
							<br/>&nbsp;<br/>
							${requestScope['nmDiretorRH']}<br/>
							Diretora da Secretaria Geral
						</c:when>
						<c:otherwise>
							PRESIDENTE:<br/>
							${requestScope['nmPresidente']}<br/>
							<br/>&nbsp;<br/>
							VICE-PRESIDENTE<br/>
							${requestScope['nmVicePresidente']}
							<br/>&nbsp;<br/>
							CORREGEDOR REGIONAL<br/>
							${requestScope['nmCorregedorRegional']}
						</c:otherwise>
					</c:choose>
					
					
				</td>
				<td width="65%" align="right" style="margin-right:4px; font-size: 10pt; border-width: 1px 0px 0px 0px; border-style: solid">
					<c:choose>
						<c:when test="${idOrgaoUsu == 1 || idOrgaoUsu == 2}">
							${doc.codigo} - Geração: <br/>
							${requestScope['geraImpress']}<br/>
							Setores responsáveis pelas informações:<br/>
							${requestScope['setoresResponsaveis']}<br/>
							Publicação diária na intranet ${acronimoOrgaoUsu}<br/>
							<br/>&nbsp;<br/>
							 Justiça Federal - ${descricaoOrgaoUsu}<br/>
						</c:when>
						<c:otherwise>
							DIRETOR GERAL: <br/>
							${requestScope['diretorGeral']}<br/>
							COORDENAÇÃO DE PRODUÇÃO:<br/>
							${requestScope['coordenacaoDeProducao']}<br/>
							DIAGRAMAÇÃO E IMPRESSÃO:<br/>
							${requestScope['diagramacaoEImpressao']}<br/>
							PERIODICIDADE:<br/>
							Diária
							<br/>&nbsp;<br/>
							 Tribunal Regional Federal da 2ªRegião<br/>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${idOrgaoUsu == 1}">Av. Almirante Barroso, 78 - Centro / RJ</c:when>
						<c:when test="${idOrgaoUsu == 2}">Rua São Francisco, 52, Centro - Vitória-ES</c:when>
						<c:when test="${idOrgaoUsu == 3}">
							Rua Acre, nº 80, Centro - Rio de Janeiro/RJ<br/>
							Cep. 20081-000 - Tel. (21) 3261-8000<br/>
							wwww.trf2.jus.br
						</c:when>
					</c:choose><br/>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</span>