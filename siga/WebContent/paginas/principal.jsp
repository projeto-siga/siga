<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://localhost/libstag" prefix="f"%>

<%--
<html>
<head>
<title>Documento</title>
<c:import url="/paginas/estilos.jsp" />
</head>
<body>
<script>
function submitOk() {
	var id=document.getElementById('editar_documentoViaSel_id').value;
	if (id==null || id=="") {
		return;
	}
	frm.action=frm.docAcao.value;
	//frm.docAcao.value='';
	frm.submit();
}
</script>

<c:set var="titulo_pagina" scope="request">Página Inicial</c:set>
<c:import url="/paginas/cabecalho.jsp" />
--%>

<siga:pagina titulo="P&aacute;gina Inicial">

	<ww:token />
	<TABLE WIDTH="100%" height="100%" BORDER=0 CELLPADDING=0 CELLSPACING=0>
		<TR>
			<TR>
				<TD width="186" height="88" valign="top"><IMG
					SRC="<c:url value='/imagens/companyslogan.gif'/>" WIDTH=186
					HEIGHT=88 ALT=""></TD>
				<TD rowspan="2" valign="top" style="padding: 0">
				<TABLE width="100%" BORDER=0 CELLPADDING=0 CELLSPACING=0>
					<TR>
						<TD width="100%" height="88" valign="top"
							background="<c:url value='/imagens/middlebg.gif'/>"
							style="padding: 0">
						<table width="100%" height="100%" border="0" cellspacing="0"
							cellpadding="0">
							<tr>
								<td style="padding-left: 12; padding-top: 11"></td>
								<td valign="center" class="newsarticle"><c:catch>
									<c:set var="siglaOrgaoUsu"
										value="${titular.orgaoUsuario.siglaOrgaoUsu}" />
								</c:catch> <c:choose>
									<c:when test="${siglaOrgaoUsu == 'RJ'}">
										<p><strong>Missão:</strong> Assegurar o acesso à Justiça
										Federal, solucionando conflitos e garantindo direitos, por
										meio da entrega da prestação jurisdicional à sociedade como um
										todo, de forma eficaz, com celeridade e comprometimento,
										obedecendo aos princípios legais e considerando sua
										responsabilidade social.</p>
									</c:when>
									<c:when test="${siglaOrgaoUsu == 'ES'}">
										<p><strong>Missão:</strong> Assegurar o acesso à Justiça
										Federal, solucionando conflitos e garantindo direitos, por
										meio da entrega da prestação jurisdicional à sociedade como um
										todo, de forma eficaz, com celeridade e comprometimento,
										obedecendo aos princípios legais e considerando sua
										responsabilidade social.</p>
									</c:when>
									<c:when test="${siglaOrgaoUsu == 'T2'}">
										<p><strong>Srs. Usuários:</strong> lembramos que, ao serem
										recadastrados no SIGA, os expedientes recebem um novo número
										de registro, que deve ser anotado e inserido no antigo
										cadastro do expediente no SCE. Para isto deve ser utilizada a
										opção ASSOCIAÇÃO DE EXPEDIENTE SIGA x SCE no menu do SCE
										(W_Emul).<br />
										<br />
										<span style="font-weight: bold;"><a
											href="/siga/comunicado_trf.pdf" target="_new">Comunicado
										ao usuário do TRF</a></span></p>
									</c:when>
								</c:choose></td>
							</tr>
						</table>
						</TD>
						<TD height="168" ROWSPAN=2 valign="top"><IMG
							SRC="<c:url value='/imagens/mainpic.gif'/>" WIDTH=245 HEIGHT=168
							ALT=""></TD>
					</TR>
					<TR>
						<TD width="344" height="80" valign="top"
							style="padding-left: 12; padding-top: 7">
						<h1>Página Inicial</h1>
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<TR>
				<TD width="114" rowspan="2" valign="top"
					style="border-right: 1px dotted; padding-left: 19"><br>
				<table width="136" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td
							style="border-top: 1px dotted #CCCCCC; padding-top: 4; padding-bottom: 14"><strong>
						<a target="_new"
							href="http://intranet/sg/cgor/web_cgor/com_adm/listamodelos.htm"
							class="sidetable">Modelos SJRJ</a></strong></td>
					</tr>
					<tr>
						<td
							style="border-top: 1px dotted #CCCCCC; padding-top: 4; padding-bottom: 14"><strong><a
							target="_new"
							href="http://intranet/conteudos/gestao_documental/gestao_documental.asp"
							class="sidetable">Tabela de Temporalidade</a></strong></td>
					</tr>
					<tr>
						<td
							style="border-top: 1px dotted #CCCCCC; padding-top: 4; padding-bottom: 14"><strong><a
							href="/siga/apostila_sigaex.pdf" target="_new" class="sidetable">Apostila
						SIGA-Doc</a></strong></td>
					</tr>
					<tr>
						<td
							style="border-top: 1px dotted #CCCCCC; padding-top: 4; padding-bottom: 14"><strong><a
							href="/siga/apostila_sigawf.pdf" target="_new" class="sidetable">Apostila
						SIGA-Workflow</a></strong></td>
					</tr>
					<tr>
						<td
							style="border-top: 1px dotted #CCCCCC; padding-top: 4; padding-bottom: 14"><strong><a
							href="/siga/apostila_sigase.pdf" target="_new" class="sidetable">Apostila
						SIGA-Serviços</a></strong></td>
					</tr>
					<tr>
						<td
							style="border-top: 1px dotted #CCCCCC; padding-top: 4; padding-bottom: 14"><strong>
						<a href="/siga/roteiro_eletronicos.pdf" class="sidetable"
							target="_new">Roteiro Para Assinatura Digital</a></strong></td>
					</tr>
					<tr>
						<td
							style="border-top: 1px dotted #CCCCCC; padding-top: 4; padding-bottom: 14"><strong>
						<a href="news.action" class="sidetable" target="_new">Últimas
						Novidades</a></strong></td>
					</tr>
				</table>
				<br>
				</TD>
			</TR>
			<TR>
				<TD colspan="3" valign="top"
					style="padding-left: 12; padding-top: 7; padding-right: 12; padding-bottom: 7;">


				<table class="indexer" border="0" width="100%">
					<tr>
						<td class="left" valign="top" width="50%"><%--<c:if test="${f:resource('isWorkflowEnabled')}">--%>
						<div
							style="font-size: 14px; font-weight: bold; padding-bottom: 6pt">Quadro
						de Expedientes</div>
						<span id='left'></span> <script type="text/javascript">SetInnerHTMLFromAjaxResponse("/sigaex/expediente/doc/gadget.action?apenasQuadro=true&ts=${currentTimeMillis}&idTpFormaDoc=1",document.getElementById('left'));</script>
						<div
							style="font-size: 14px; font-weight: bold; padding-bottom: 6pt">Quadro
						de Processos Administrativos</div>
						<span id='leftbottom'></span> <script type="text/javascript">SetInnerHTMLFromAjaxResponse("/sigaex/expediente/doc/gadget.action?ts=${currentTimeMillis}&idTpFormaDoc=2",document.getElementById('leftbottom'));</script>
						</td>
						<td class="right" valign="top" width="50%"><!-- Enabled: ${f:resource('isWorkflowEnabled')} Accessible: ${f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;WF')} -->
						<c:if
							test="${f:resource('isWorkflowEnabled') and f:podeUtilizarServicoPorConfiguracao(titular,lotaTitular,'SIGA;WF')}">
							<div
								style="font-size: 14px; font-weight: bold; padding-bottom: 6pt">Quadro
							de Tarefas</div>
							<span id='right'></span>
							<script type="text/javascript">SetInnerHTMLFromAjaxResponse("/sigawf/inbox.action?ts=${currentTimeMillis}",document.getElementById('right'));</script>
						</c:if></td>
					</tr>
				</table>
				</TD>
			</TR>
	</TABLE>

</siga:pagina>