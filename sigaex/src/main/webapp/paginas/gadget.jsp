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
	frm.action=frm.docAcao.value;
	//frm.docAcao.value='';
	frm.submit();
}
</script>

<table class="index" width="100%">
	<tr class="header">
		<td width="50%">Situação</td>
		<td width="25%" align="right">Atendente</td>
		<td width="25%" align="right">Lota&ccedil;&atilde;o</td>
	</tr>
	<c:forEach var="listEstado" items="${listEstados}">
		<c:choose>
			<c:when test="${listEstado[0]==-4}">
				<tr>
					<td><ww:url id="url" action="listar"
						namespace="/expediente/doc">
						<ww:param name="ultMovIdEstadoDoc">15</ww:param>			
						<ww:param name="orgaoUsu">0</ww:param>
						<ww:param name="subscritorSel.id">${titular.idPessoa}</ww:param>
						<ww:param name="idTipoFormaDoc">${idTpMarca}</ww:param>
					</ww:url> 
					<siga:monolink titulo="Documentos pendentes de assinatura cujo subscritor é o usuário logado" texto="${listEstado[1]}" href="%{url}" />
					<%--<ww:a href="%{url}" title="Documentos pendentes de assinatura cujo subscritor é o usuário logado.">${listEstado[1]}</ww:a>--%></td>
					

					<td align="right" class="count"><ww:url id="url"
						action="listar" namespace="/expediente/doc">
						<ww:param name="ultMovIdEstadoDoc">15</ww:param>			
						<ww:param name="orgaoUsu">0</ww:param>
						<ww:param name="subscritorSel.id">${titular.idPessoa}</ww:param>
						<ww:param name="idTipoFormaDoc">${idTpMarca}</ww:param>
					</ww:url> 
					<siga:monolink texto="${listEstado[2]}" titulo="Documentos pendentes de assinatura cujo subscritor é o usuário logado" href="%{url}" />
					<%--<ww:a href="%{url}" title="Documentos pendentes de assinatura cujo subscritor é o usuário logado.">${listEstado[2]}</ww:a>--%></td>

					<td align="right" class="count">---</td>
				</tr>
			</c:when>
			<c:when test="${listEstado[0]==-3}">
				<tr>
					<td><ww:url id="url" action="listar"
						namespace="/expediente/doc">
						<ww:param name="ultMovIdEstadoDoc">14</ww:param>
						<ww:param name="ultMovLotaCadastranteSel.id">${lotaTitular.idLotacao}</ww:param>
						<ww:param name="orgaoUsu">0</ww:param>
						<ww:param name="idTipoFormaDoc">${idTpMarca}</ww:param>
					</ww:url> 
					<siga:monolink titulo="Documentos eletrônicos que foram transferidos, pelo usuário, para outra lotação/pessoa mas ainda não foram recebidos" texto="${listEstado[1]}" href="%{url}" />
					<%--<ww:a href="%{url}" title="Documentos eletrônicos que foram transferidos, pelo usuário, para outra lotação/pessoa mas ainda não foram recebidos.">${listEstado[1]}</ww:a>--%></td>

					<td align="right" class="count"><ww:url id="url"
						action="listar" namespace="/expediente/doc">
						<ww:param name="ultMovIdEstadoDoc">14</ww:param>
						<ww:param name="ultMovCadastranteSel.id">${titular.idPessoa}</ww:param>
						<ww:param name="orgaoUsu">0</ww:param>
						<ww:param name="idTipoFormaDoc">${idTpMarca}</ww:param>
					</ww:url> 
					<siga:monolink titulo="Documentos eletrônicos que foram transferidos, pelo usuário, para outra lotação/pessoa mas ainda não foram recebidos" texto="${listEstado[2]}" href="%{url}" />
					<%--<ww:a href="%{url}" title="Documentos eletrônicos que foram transferidos, pelo usuário, para outra lotação/pessoa mas ainda não foram recebidos.">${listEstado[2]}</ww:a>--%></td>

					<td align="right" class="count"><ww:url id="url"
						action="listar" namespace="/expediente/doc">
						<ww:param name="ultMovIdEstadoDoc">14</ww:param>
						<ww:param name="ultMovLotaCadastranteSel.id">${lotaTitular.idLotacao}</ww:param>
						<ww:param name="orgaoUsu">0</ww:param>
						<ww:param name="idTipoFormaDoc">${idTpMarca}</ww:param>
					</ww:url> 
					<siga:monolink titulo="Documentos eletrônicos que foram transferidos, pelo usuário, para outra lotação/pessoa mas ainda não foram recebidos" texto="${listEstado[3]}" href="%{url}" />
					<%--<ww:a href="%{url}" title="Documentos eletrônicos que foram transferidos, pelo usuário, para outra lotação/pessoa mas ainda não foram recebidos">${listEstado[3]}</ww:a>--%></td>
				</tr>
			</c:when>
			<c:when test="${listEstado[0]==-2}">
				<tr>
					<td><ww:url id="url" action="listar"
						namespace="/expediente/doc">
						<ww:param name="ultMovLotaSubscritorSel.id">${lotaTitular.idLotacao}</ww:param>
						<ww:param name="orgaoUsu">0</ww:param>
						<ww:param name="idTipoFormaDoc">${idTpMarca}</ww:param>
					</ww:url> 
					<siga:monolink texto="${listEstado[1]}" href="%{url}" />
					<%--<ww:a href="%{url}">${listEstado[1]}</ww:a>--%></td>

					<td align="right" class="count"><ww:url id="url"
						action="listar" namespace="/expediente/doc">
						<ww:param name="ultMovIdEstadoDoc">14</ww:param>
						<ww:param name="ultMovSubscritorSel.id">${titular.idPessoa}</ww:param>
						<ww:param name="orgaoUsu">0</ww:param>
						<ww:param name="idTipoFormaDoc">${idTpMarca}</ww:param>
					</ww:url> 
					<siga:monolink texto="${listEstado[2]}" href="%{url}" />
					<%--<ww:a href="%{url}">${listEstado[2]}</ww:a>--%></td>

					<td align="right" class="count"><ww:url id="url"
						action="listar" namespace="/expediente/doc">
						<ww:param name="ultMovIdEstadoDoc">14</ww:param>
						<ww:param name="ultMovLotaSubscritorSel.id">${lotaTitular.idLotacao}</ww:param>
						<ww:param name="orgaoUsu">0</ww:param>
						<ww:param name="idTipoFormaDoc">${idTpMarca}</ww:param>
					</ww:url> 
					<siga:monolink texto="${listEstado[3]}" href="%{url}" />
					<%--<ww:a href="%{url}">${listEstado[3]}</ww:a>--%></td>
				</tr>
			</c:when>
			<c:when test="${listEstado[0]==1}">
				<tr>
					<td><ww:url id="url" action="listar"
						namespace="/expediente/doc">
						<ww:param name="ultMovIdEstadoDoc">${listEstado[0]}</ww:param>
						<ww:param name="lotaCadastranteSel.id">${lotaTitular.idLotacao}</ww:param>
						<ww:param name="orgaoUsu">0</ww:param>
						<ww:param name="idTipoFormaDoc">${idTpMarca}</ww:param>
					</ww:url><siga:monolink titulo="Documentos que estão em estado temporário(rascunho) e ainda podem ser editados." texto="${listEstado[1]}" href="%{url}" />
					
					<td align="right" class="count"><ww:url id="url"
						action="listar" namespace="/expediente/doc">
						<ww:param name="ultMovIdEstadoDoc">${listEstado[0]}</ww:param>
						<ww:param name="cadastranteSel.id">${titular.idPessoa}</ww:param>
						<ww:param name="orgaoUsu">0</ww:param>
						<ww:param name="idTipoFormaDoc">${idTpMarca}</ww:param>
					</ww:url> 
					<siga:monolink titulo="Documentos que estão em estado temporário(rascunho) e ainda podem ser editados." texto="${listEstado[2]}" href="%{url}" />

					<td align="right" class="count"><ww:url id="url"
						action="listar" namespace="/expediente/doc">
						<ww:param name="ultMovIdEstadoDoc">${listEstado[0]}</ww:param>
						<ww:param name="lotaCadastranteSel.id">${lotaTitular.idLotacao}</ww:param>
						<ww:param name="idTipoFormaDoc">${idTpMarca}</ww:param>
						<ww:param name="orgaoUsu">0</ww:param>
					</ww:url>
					<siga:monolink titulo="Documentos que estão em estado temporário(rascunho) e ainda podem ser editados." texto="${listEstado[3]}" href="%{url}" />
					<%--<ww:a href="%{url}" title="${titulo}">${listEstado[3]}</ww:a>--%></td>
				</tr>
			</c:when>		
				
			<c:when test="${listEstado[0]>1}">
				<c:set var="titulo" value=""></c:set>
				
				<c:if test="${listEstado[0]==1}">
					<c:set var="titulo" value="Documentos que estão em estado temporário(rascunho) e ainda podem ser editados."></c:set>
				</c:if>

				<c:if test="${listEstado[0]==2}">
					<c:set var="titulo" value="Documentos que já foram assinados ou tiveram a assinatura manual registrada. Também contém os documentos que já foram recebidos pela lotação/pessoa do usuário logado."></c:set>
				</c:if>
				
				<c:if test="${listEstado[0]==3}">
					<c:set var="titulo" value="Documentos não eletrônicos transferidos para a lotação/pessoa do usuário logado que estão aguardando recebimento."></c:set>
				</c:if>

				<c:if test="${listEstado[0]==14}">
					<c:set var="titulo" value="Documentos eletrônicos transferidos para a lotação/pessoa do usuário logado que estão aguardando recebimento."></c:set>
				</c:if>

				<c:if test="${listEstado[0]==15}">
					<c:set var="titulo" value="Documentos que foram finalizados mas ainda não foram assinados."></c:set>
				</c:if>

				<tr>
					<td><ww:url id="url" action="listar"
						namespace="/expediente/doc">
						<ww:param name="ultMovIdEstadoDoc">${listEstado[0]}</ww:param>
						<c:choose>
							<c:when test="${listEstado[0]==11}">
								<ww:param name="ultMovLotaCadastranteSel.id">${lotaTitular.idLotacao}</ww:param>
							</c:when>
							<c:otherwise>
								<ww:param name="ultMovLotaRespSel.id">${lotaTitular.idLotacao}</ww:param>
							</c:otherwise>
						</c:choose>
						<ww:param name="orgaoUsu">0</ww:param>
						<ww:param name="idTipoFormaDoc">${idTpMarca}</ww:param>
					</ww:url><siga:monolink titulo="${titulo}" texto="${listEstado[1]}" href="%{url}" />
					
					 <%--<ww:a href="%{url}" title="${titulo}">${listEstado[1]}</ww:a>--%></td>

					<td align="right" class="count"><ww:url id="url"
						action="listar" namespace="/expediente/doc">
						<ww:param name="ultMovIdEstadoDoc">${listEstado[0]}</ww:param>
						<c:choose>
							<c:when test="${listEstado[0]==11}">
								<ww:param name="ultMovCadastranteSel.id">${titular.idPessoa}</ww:param>
							</c:when>
							<c:otherwise>
								<ww:param name="ultMovRespSel.id">${titular.idPessoa}</ww:param>
							</c:otherwise>
						</c:choose>
						<ww:param name="orgaoUsu">0</ww:param>
						<ww:param name="idTipoFormaDoc">${idTpMarca}</ww:param>
					</ww:url> 
					<siga:monolink titulo="${titulo}" texto="${listEstado[2]}" href="%{url}" />
					<%--<ww:a href="%{url}" title="${titulo}">${listEstado[2]}</ww:a>--%></td>

					<td align="right" class="count"><ww:url id="url"
						action="listar" namespace="/expediente/doc">
						<ww:param name="ultMovIdEstadoDoc">${listEstado[0]}</ww:param>
						<c:choose>
							<c:when test="${listEstado[0]==11}">
								<ww:param name="ultMovLotaCadastranteSel.id">${lotaTitular.idLotacao}</ww:param>
							</c:when>
							<c:otherwise>
								<ww:param name="ultMovLotaRespSel.id">${lotaTitular.idLotacao}</ww:param>
							</c:otherwise>
						</c:choose>
						<ww:param name="orgaoUsu">0</ww:param>
						<ww:param name="idTipoFormaDoc">${idTpMarca}</ww:param>
					</ww:url>
					<siga:monolink titulo="${titulo}" texto="${listEstado[3]}" href="%{url}" />
					<%--<ww:a href="%{url}" title="${titulo}">${listEstado[3]}</ww:a>--%></td>
				</tr>
			</c:when>
			<c:when test="${listEstado[0]==-1}">
				<tr>
					<td><ww:url id="url" action="listar"
						namespace="/expediente/doc">
						<ww:param name="ultMovIdEstadoDoc">3</ww:param>
						<ww:param name="ultMovLotaCadastranteSel.id">${lotaTitular.idLotacao}</ww:param>
						<ww:param name="orgaoUsu">0</ww:param>
						<ww:param name="idTipoFormaDoc">${idTpMarca}</ww:param>
					</ww:url> 
					<siga:monolink titulo="Documentos não eletrônicos que foram transferidos, pelo usuário, para outra lotação/pessoa mas ainda não foram recebidos" texto="${listEstado[1]}" href="%{url}" />		
					<%--<ww:a href="%{url}" title="Documentos não eletrônicos que foram transferidos, pelo usuário, para outra lotação/pessoa mas ainda não foram recebidos.">${listEstado[1]}</ww:a>--%></td>

					<td align="right" class="count"><ww:url id="url"
						action="listar" namespace="/expediente/doc">
						<ww:param name="ultMovIdEstadoDoc">3</ww:param>
						<ww:param name="ultMovCadastranteSel.id">${titular.idPessoa}</ww:param>
						<ww:param name="orgaoUsu">0</ww:param>
						<ww:param name="idTipoFormaDoc">${idTpMarca}</ww:param>
					</ww:url> 
					<siga:monolink titulo="Documentos não eletrônicos que foram transferidos, pelo usuário, para outra lotação/pessoa mas ainda não foram recebidos" texto="${listEstado[2]}" href="%{url}" />
					<%--<ww:a href="%{url}" title="Documentos não eletrônicos que foram transferidos, pelo usuário, para outra lotação/pessoa mas ainda não foram recebidos.">${listEstado[2]}</ww:a>--%></td>

					<td align="right" class="count"><ww:url id="url"
						action="listar" namespace="/expediente/doc">
						<ww:param name="ultMovIdEstadoDoc">3</ww:param>
						<ww:param name="ultMovLotaCadastranteSel.id">${lotaTitular.idLotacao}</ww:param>
						<ww:param name="orgaoUsu">0</ww:param>
						<ww:param name="idTipoFormaDoc">${idTpMarca}</ww:param>
					</ww:url> 
					<siga:monolink titulo="Documentos não eletrônicos que foram transferidos, pelo usuário, para outra lotação/pessoa mas ainda não foram recebidos" texto="${listEstado[3]}" href="%{url}" />
					<%--<ww:a href="%{url}" title="Documentos não eletrônicos que foram transferidos, pelo usuário, para outra lotação/pessoa mas ainda não foram recebidos.">${listEstado[3]}</ww:a>--%></td>
				</tr>
			</c:when>
		</c:choose>
	</c:forEach>
</table>
<ww:form name="frm" action="editar" namespace="/expediente/doc"
	method="post" theme="simple">
	<table width="100%" class="zero">
		<tr>
			<td><ww:url id="url1" action="exibir"
				namespace="/expediente/doc" /> <ww:url id="url2" action="receber"
				namespace="/expediente/mov" /> <ww:url id="url3" action="arquivar"
				namespace="/expediente/doc" /> <%--<c:if
						test="${f:podeRemeterPorConfiguracao(titular,lotaTitular)}">
						<ww:url id="url7" action="remeter_para_publicacao"
							namespace="/expediente/mov" />
						<c:set var="itemRemeter">'07':'Gerenciar Publicação',</c:set>
					</c:if> --%><ww:url id="url4" action="transferir_lote"
				namespace="/expediente/mov" /> <ww:url id="url5"
				action="receber_lote" namespace="/expediente/mov" /> <ww:url
				id="url6" action="via_protocolo" namespace="/expediente/mov" /> <%-- <ww:select name="docAcao" list="#{%{url1}:'Exibir', %{url2}:'Receber', %{url3}:'Arquivar'}" /> --%>
			<ww:select name="docAcao"
				list="#{'/sigaex/expediente/doc/exibir.action':'Exibir', '/sigaex/expediente/mov/receber.action':'Receber', '/sigaex/expediente/mov/arquivar.action':'Arquivar'}"
				onchange="javascript: if (this.value == '04') window.location='${url4}'; if (this.value == '05') window.location='${url5}'; if (this.value == '06') window.location='${url6}'; if (this.value == '07') window.location='${url7}';" />
			<!-- , '04':'Transferir Lote', ${itemRemeter} '05':'Receber Lote', '06':'2� Via Protocolo'} -->
			<siga:selecao tema="simple" buscar="nao" ocultardescricao="sim" propriedade="documentoVia" modulo="siga"/>
			<input type="button" name="ok" value="Ok"
				onclick="javascript:submitOk();" /> <ww:url id="url"
				action="editar" namespace="/expediente/doc" /></td>
			<td><ww:submit align="right" name="novo" value="Novo"
				title="Criar Novo Expediente"
				onclick="javascript:frm.action='%{url}';" /></td>
		</tr>
	</table>
</ww:form>