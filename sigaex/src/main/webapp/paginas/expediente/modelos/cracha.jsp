<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="esconderTexto" value="sim" scope="request" />
<mod:modelo urlBase="/paginas/expediente/modelos/requerimento_rh.jsp">
	<mod:entrevista>
		<mod:grupo>
			<mod:pessoa titulo="Servidor" obrigatorio="Sim" var="servidor" />
		</mod:grupo>

		<mod:grupo>
			<mod:texto titulo="Nome Abreviado" obrigatorio="Sim" var="nomeAbreviado" largura="23" maxcaracteres="21" />
		</mod:grupo>

		<mod:grupo>
			<mod:texto titulo="Nome Principal" obrigatorio="Sim" var="nomePrincipal" largura="24" maxcaracteres="21" />
		</mod:grupo>

			<mod:grupo largura="19">	
				<mod:selecao titulo="Motivo" var="motivo" reler="ajax" idAjax="motivoAjax" 
				opcoes="Desgaste Natural;Roubo;Extravio;Outro"></mod:selecao>
			</mod:grupo>
				<c:set var="valMotivo" value="${motivo}" />
				<c:if test="${empty valMotivo}">
					<c:set var="valMotivo" value="${param['motivo']}" />
				</c:if>
				<mod:grupo largura="81">
					<mod:grupo depende="motivoAjax">
						<c:if test="${valMotivo == 'Outro'}">
							<mod:texto titulo="Descreva outro motivo" obrigatorio="Sim" var="outroMotivo" largura="50" maxcaracteres="37"></mod:texto>
						</c:if>
					</mod:grupo>	
				</mod:grupo>
			<mod:grupo>
				<mod:mensagem texto="Deseja trocar a foto?"></mod:mensagem>
			</mod:grupo>
			<mod:grupo>
				<mod:radio titulo="Sim" var="trocarFoto" valor="1"></mod:radio>
				<mod:radio titulo="N&atilde;o" var="trocarFoto" valor="2" marcado="Sim"></mod:radio>
			</mod:grupo>
	</mod:entrevista>

	<mod:documento>
	<c:set var="outroMotivo" value="${fn:trim(outroMotivo)}" />	
		<mod:valor var="texto_requerimento">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF"><tr><td>
		
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<br/>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td align="center" width="100%">REQUERIMENTO DE CRACHÁ ${doc.codigo}</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br>
		
		<p style="TEXT-INDENT: 2cm" align="justify">
		Servidor: <b>${requestScope['servidor_pessoaSel.descricao']}</b>
		<br/>
		Matrícula: <b>${requestScope['servidor_pessoaSel.sigla']}</b>
		<br/>
		Nome Abreviado: <b>${nomeAbreviado }</b>
		<br/>
		Nome Principal: <b>${nomePrincipal }</b>
		</p>
		<c:if test="${not empty motivo}">
		<p style="TEXT-INDENT: 2cm" align="justify">
		Venho requerer a Vossa Senhoria a 2&ordf; via do crachá de identidade funcional pelo seguinte motivo:<br/>
		<c:choose>
		<c:when test="${motivo eq 'Selecione'}"><b>Não informado/selecionado</b></c:when>
		<c:when test="${motivo eq 'Outro' and not empty outroMotivo}">${outroMotivo}</c:when>
		<c:when test="${motivo eq 'Outro' and empty outroMotivo}">Não informado/selecionado</c:when>
		<c:otherwise>${motivo}</c:otherwise>
		</c:choose>
		
		<br/>Para tanto, 
		<c:choose>
		<c:when test="${trocarFoto eq '1'}">envio foto 3x4 colorida e atualizada via malote.</c:when>
		<c:otherwise>favor manter a foto do sistema.</c:otherwise>
		</c:choose>
		
		</p>
		
		</c:if>
		
		
		<p style="TEXT-INDENT: 2cm" align="justify">
		Declaro ainda estar ciente de que a Seção de Cadastro analisará a presente solicitação e encaminhará à consideração
		superior se não entender cabível de receber isenção de pagamento, hipótese na qual efetuarei o pagamento diretamente
		na Subsecretaria de Orçamento e Finanças por meio de GRU (no valor expresso na página da SECAD na INTRANET), nos termos
		da Portaria n&ordm; 05/2006-GDF.
		</p>
		</mod:valor>
		
	</mod:documento>
</mod:modelo>
