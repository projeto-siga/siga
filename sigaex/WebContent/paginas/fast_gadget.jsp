<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>

<script type="text/javascript">
submitOk = function() {
	var id=document.getElementById('editar_documentoViaSel_id').value;
	if (id==null || id=="") {
		return;
	}
	frmSigaEx.action=frmSigaEx.docAcao.value;
	//frmSigaEx.docAcao.value='';
	frmSigaEx.submit();
}
</script>

<div class="gt-content-box gt-for-table">
	<table border="0" class="gt-table">
		<thead>
			<tr>
				<th width="50%">Situação</th>
				<th width="25%" style="text-align: right">Atendente</th>
				<th width="25%" style="text-align: right">Lotação</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="listEstado" items="${listEstados}">
				<c:if
					test="${listEstado[0] != 6 && listEstado[0] != 9 && listEstado[0] != 10 
			&& listEstado[0] != 11 && listEstado[0] != 12 
			&& listEstado[0] != 13 && listEstado[0] != 16
			&& listEstado[0] != 18 && listEstado[0] != 20 
			&& listEstado[0] != 21 && listEstado[0] != 22 
			&& listEstado[0] != 26 && listEstado[0] != 32}">

					<c:set var="titulo1" value=""></c:set>
					<c:set var="titulo2" value=""></c:set>
					<c:set var="titulo3" value=""></c:set>
					<c:set var="ordem" value="0"/>
					<c:set var="visualizacao" value="0"/>
					<c:choose>
						<c:when test="${listEstado[0]==1}">
							<c:set var="titulo1"
								value="Documentos que estão em estado temporário(rascunho) e ainda podem ser editados."></c:set>
							<c:set var="titulo2"
								value="Documentos que estão em estado temporário(rascunho) e ainda podem ser editados."></c:set>
							<c:set var="titulo3" value="${titulo1}"></c:set>
						</c:when>
						<c:when test="${listEstado[0]==2}">
							<c:set var="titulo1"
								value="Documentos que já foram assinados ou tiveram a assinatura manual registrada. Também contém os documentos que já foram recebidos pela lotação ${lotaTitular.nomeLotacao} ou pelo usuário ${titular.nomePessoa}."></c:set>
							<c:set var="titulo2"
								value="Documentos que já foram assinados ou tiveram a assinatura manual registrada. Também contém os documentos que já foram recebidos pelo usuário ${titular.nomePessoa}."></c:set>
							<c:set var="titulo3" value="${titulo1}"></c:set>
							<c:set var="visualizacao" value="1"/>
						</c:when>
						<c:when test="${listEstado[0]==3}">
							<c:set var="titulo1"
								value="Documentos não eletrônicos transferidos para a ${lotaTitular.nomeLotacao} ou para o usuário ${titular.nomePessoa} que estão aguardando recebimento."></c:set>
							<c:set var="titulo2"
								value="Documentos não eletrônicos transferidos para o usuário ${titular.nomePessoa} que estão aguardando recebimento."></c:set>
							<c:set var="titulo3" value="${titulo1}"></c:set>
							<c:set var="ordem" value="1"/>
						</c:when>
						<c:when test="${listEstado[0]==14}">
							<c:set var="titulo1"
								value="Documentos eletrônicos transferidos para a ${lotaTitular.nomeLotacao} ou para o usuário ${titular.nomePessoa} que estão aguardando recebimento."></c:set>
							<c:set var="titulo2"
								value="Documentos eletrônicos transferidos para o usuário ${titular.nomePessoa} que estão aguardando recebimento."></c:set>
							<c:set var="titulo3" value="${titulo1}"></c:set>
							<c:set var="ordem" value="1"/>
						</c:when>
						<c:when test="${listEstado[0]==15}">
							<c:set var="titulo1"
								value="Documentos que foram finalizados mas ainda não foram assinados."></c:set>
							<c:set var="titulo2"
								value="Documentos que foram finalizados mas ainda não foram assinados."></c:set>
							<c:set var="titulo3" value="${titulo1}"></c:set>
							<c:set var="ordem" value="1"/>
						</c:when>
						<c:when test="${listEstado[0]==23}">
							<c:set var="titulo1"
								value="Documentos não eletrônicos que foram transferidos, pela lotação ${lotaTitular.nomeLotacao} ou pelo usuário ${titular.nomePessoa}, para outra lotação/pessoa mas ainda não foram recebidos."></c:set>
							<c:set var="titulo2"
								value="Documentos não eletrônicos que foram transferidos, pelo usuário ${titular.nomePessoa}, para outra lotação/pessoa mas ainda não foram recebidos."></c:set>
							<c:set var="titulo3" value="${titulo1}"></c:set>
							<c:set var="ordem" value="1"/>
						</c:when>
						<c:when test="${listEstado[0]==24}">
							<c:set var="titulo1"
								value="Documentos eletrônicos que foram transferidos, pela lotação ${lotaTitular.nomeLotacao} ou pelo usuário ${titular.nomePessoa}, para outra lotação/pessoa mas ainda não foram recebidos."></c:set>
							<c:set var="titulo2"
								value="Documentos eletrônicos que foram transferidos, pelo usuário ${titular.nomePessoa}, para outra lotação/pessoa mas ainda não foram recebidos."></c:set>
							<c:set var="titulo3" value="${titulo1}"></c:set>
							<c:set var="ordem" value="1"/>
						</c:when>
						<c:when test="${listEstado[0]==25}">
							<c:set var="titulo1"
								value="Documentos pendentes de assinatura cujo subscritor é o usuário ${titular.nomePessoa}."></c:set>
							<c:set var="titulo2" value="${titulo1}"></c:set>
							<c:set var="titulo3" value=""></c:set>
							<c:set var="ordem" value="1"/>
						</c:when>
						<c:when test="${listEstado[0]==31}">
							<c:set var="visualizacao" value="1"/>
						</c:when>
					</c:choose>
					<tr>
						<td><ww:url id="url" action="listar"
								namespace="/expediente/doc">
								<ww:param name="ultMovIdEstadoDoc">${listEstado[0]}</ww:param>
								<ww:param name="ultMovLotaRespSel.id">${lotaTitular.idLotacao}</ww:param>
								<ww:param name="orgaoUsu">0</ww:param>
								<ww:param name="idTipoFormaDoc">${idTpFormaDoc}</ww:param>
								<ww:param name="ordem">${ordem}</ww:param>
								<ww:param name="visualizacao">${visualizacao}</ww:param>
							</ww:url> <siga:monolink titulo="${titulo1}" texto="${listEstado[1]}"
								href="%{url}" />
						<td align="right" class="count"><ww:url id="url"
								action="listar" namespace="/expediente/doc">
								<ww:param name="ultMovIdEstadoDoc">${listEstado[0]}</ww:param>
								<ww:param name="ultMovRespSel.id">${titular.idPessoa}</ww:param>
								<ww:param name="orgaoUsu">0</ww:param>
								<ww:param name="idTipoFormaDoc">${idTpFormaDoc}</ww:param>
								<ww:param name="ordem">${ordem}</ww:param>
								<ww:param name="visualizacao">${visualizacao}</ww:param>
							</ww:url> <siga:monolink titulo="${titulo2}" texto="${listEstado[2]}"
								href="%{url}" />
						<td align="right" class="count"><ww:url id="url"
								action="listar" namespace="/expediente/doc">
								<ww:param name="ultMovIdEstadoDoc">${listEstado[0]}</ww:param>
								<ww:param name="ultMovLotaRespSel.id">${lotaTitular.idLotacao}</ww:param>
								<ww:param name="orgaoUsu">0</ww:param>
								<ww:param name="idTipoFormaDoc">${idTpFormaDoc}</ww:param>
								<ww:param name="ordem">${ordem}</ww:param>
								<ww:param name="visualizacao">${visualizacao}</ww:param>
							</ww:url> <siga:monolink titulo="${titulo3}" texto="${listEstado[3]}"
								href="%{url}" /> <%--<ww:a href="%{url}" title="${titulo1}">${listEstado[3]}</ww:a>--%>
						</td>
					</tr>
				</c:if>
			</c:forEach>
	</table>
</div>
<c:if test="${apenasQuadro != true}">
	<br />
	<ww:url id="url" action="editar" namespace="/expediente/doc" />
	<a class="gt-btn-small gt-btn-right"
		href="javascript: window.location.href='${url}'"
		title="Criar novo expediente ou processo administrativo">Novo</a>
	<ww:url id="url" action="listar" namespace="/expediente/doc" />
	<a class="gt-btn-medium gt-btn-right"
		href="javascript: window.location.href='${url}'"
		title="Pesquisar expedientes e processos administrativos">Pesquisar</a>
	<%-- 
	<ww:form name="frmSigaEx" action="editar" namespace="/expediente/doc"
		method="post" theme="simple">
		<table>
			<tr>
				<td><ww:url id="url1" action="exibir"
						namespace="/expediente/doc" /> <ww:url id="url2" action="receber"
						namespace="/expediente/mov" /> <ww:url id="url3"
						action="arquivar" namespace="/expediente/doc" />
						--%>
	<%--<c:if
						test="${f:podeRemeterPorConfiguracao(titular,lotaTitular)}">
						<ww:url id="url7" action="remeter_para_publicacao"
							namespace="/expediente/mov" />
						<c:set var="itemRemeter">'07':'Gerenciar Publicação',</c:set>
					</c:if> --%>

	<%-- 
					<ww:url id="url4" action="transferir_lote"
						namespace="/expediente/mov" /> <ww:url id="url5"
						action="receber_lote" namespace="/expediente/mov" /> <ww:url
						id="url6" action="via_protocolo" namespace="/expediente/mov" />--%>
	<%-- <ww:select name="docAcao" list="#{%{url1}:'Exibir', %{url2}:'Receber', %{url3}:'Arquivar'}" /> --%>
	<%--						
					<ww:select name="docAcao"
						list="#{'/sigaex/expediente/doc/exibir.action':'Exibir', '/sigaex/expediente/mov/receber.action':'Receber', '/sigaex/expediente/mov/arquivar.action':'Arquivar'}"
						onchange="javascript: if (this.value == '04') window.location='${url4}'; if (this.value == '05') window.location='${url5}'; if (this.value == '06') window.location='${url6}'; if (this.value == '07') window.location='${url7}';" />
					<!-- , '04':'Transferir Lote', ${itemRemeter} '05':'Receber Lote', '06':'2� Via Protocolo'} -->
					<siga:selecao tema="simple" buscar="nao" propriedade="documentoVia"
						ocultardescricao="sim" /> <input type="button" name="ok"
					value="Ok" onclick="javascript:submitOk();" /> <ww:url id="url"
						action="editar" namespace="/expediente/doc" /></td>
				<td><ww:submit align="right" name="novo" value="Novo"
						title="Criar Novo Expediente"
						onclick="javascript:frmSigaEx.action='%{url}';" /></td>
			</tr>
		</table>
	</ww:form>
--%>
</c:if>