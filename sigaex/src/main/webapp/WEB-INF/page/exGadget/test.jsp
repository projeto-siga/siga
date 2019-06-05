<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="/WEB-INF/tld/func.tld" prefix="f"%>

<script type="text/javascript">
submitOk = function() {
	var id = document.getElementsByName('documentoViaSel.sigla')[0].value;
	if (id == null || id == "") {
		return;
	}
	document.getElementsByName('sigla')[0].value = id;
	frm.action = frm.docAcao.value;
	frm.submit();
}
</script>

<siga:pagina titulo="Siga - Testes" desabilitarbusca="sim"  >
	<div class="container-fluid">
		<table class="table table-striped table-hover" width="100%">
			<tr class="header">
				<td width="50%">Situação</td>
				<td width="25%" align="right">Atendente</td>
				<td width="25%" align="right">Lota&ccedil;&atilde;o</td>
			</tr>
			<c:forEach var="listEstado" items="${listEstados}">
				<c:choose>
					<c:when test="${listEstado[0]==-4}">
						<tr>
							<td>
							<c:url var="url" value="/app/expediente/doc/listar">
								<c:param name="ultMovIdEstadoDoc">15</c:param>			
								<c:param name="orgaoUsu">0</c:param>
								<c:param name="subscritorSel.id">${titular.idPessoa}</c:param>
								<c:param name="idTipoFormaDoc">${idTpMarca}</c:param>
							</c:url> 
							<siga:monolink titulo="Documentos pendentes de assinatura cujo subscritor é o usuário logado" texto="${listEstado[1]}" href="${url}" />
							
		
							<td align="right" class="count">
							<c:url var="url" value="/app/expediente/doc/listar">
								<c:param name="ultMovIdEstadoDoc">15</c:param>			
								<c:param name="orgaoUsu">0</c:param>
								<c:param name="subscritorSel.id">${titular.idPessoa}</c:param>
								<c:param name="idTipoFormaDoc">${idTpMarca}</c:param>
							</c:url> 
							<siga:monolink texto="${listEstado[2]}" titulo="Documentos pendentes de assinatura cujo subscritor é o usuário logado" href="${url}" />
		
							<td align="right" class="count">---</td>
						</tr>
					</c:when>
					<c:when test="${listEstado[0]==-3}">
						<tr>
							<td>
								<c:url var="url" value="/app/expediente/doc/listar">
								<c:param name="ultMovIdEstadoDoc">14</c:param>
								<c:param name="ultMovLotaCadastranteSel.id">${lotaTitular.idLotacao}</c:param>
								<c:param name="orgaoUsu">0</c:param>
								<c:param name="idTipoFormaDoc">${idTpMarca}</c:param>
							</c:url> 
							<siga:monolink titulo="Documentos eletrônicos que foram transferidos, pelo usuário, para outra lotação/pessoa mas ainda não foram recebidos" texto="${listEstado[1]}" href="${url}" />
		
							<td align="right" class="count">
								<c:url var="url" value="/app/expediente/doc/listar">
								<c:param name="ultMovIdEstadoDoc">14</c:param>
								<c:param name="ultMovCadastranteSel.id">${titular.idPessoa}</c:param>
								<c:param name="orgaoUsu">0</c:param>
								<c:param name="idTipoFormaDoc">${idTpMarca}</c:param>
							</c:url> 
							<siga:monolink titulo="Documentos eletrônicos que foram transferidos, pelo usuário, para outra lotação/pessoa mas ainda não foram recebidos" texto="${listEstado[2]}" href="${url}" />
		
							<td align="right" class="count">
								<c:url var="url" value="/app/expediente/doc/listar">
								<c:param name="ultMovIdEstadoDoc">14</c:param>
								<c:param name="ultMovLotaCadastranteSel.id">${lotaTitular.idLotacao}</c:param>
								<c:param name="orgaoUsu">0</c:param>
								<c:param name="idTipoFormaDoc">${idTpMarca}</c:param>
							</c:url> 
							<siga:monolink titulo="Documentos eletrônicos que foram transferidos, pelo usuário, para outra lotação/pessoa mas ainda não foram recebidos" texto="${listEstado[3]}" href="${url}" />
						</tr>
					</c:when>
					<c:when test="${listEstado[0]==-2}">
						<tr>
							<td>
								<c:url var="url" value="/app/expediente/doc/listar">
								<c:param name="ultMovLotaSubscritorSel.id">${lotaTitular.idLotacao}</c:param>
								<c:param name="orgaoUsu">0</c:param>
								<c:param name="idTipoFormaDoc">${idTpMarca}</c:param>
							</c:url> 
							<siga:monolink texto="${listEstado[1]}" href="${url}" />
		
							<td align="right" class="count">
								<c:url var="url" value="/app/expediente/doc/listar">
								<c:param name="ultMovIdEstadoDoc">14</c:param>
								<c:param name="ultMovSubscritorSel.id">${titular.idPessoa}</c:param>
								<c:param name="orgaoUsu">0</c:param>
								<c:param name="idTipoFormaDoc">${idTpMarca}</c:param>
							</c:url> 
							<siga:monolink texto="${listEstado[2]}" href="${url}" />
		
							<td align="right" class="count">
								<c:url var="url" value="/app/expediente/doc/listar">
								<c:param name="ultMovIdEstadoDoc">14</c:param>
								<c:param name="ultMovLotaSubscritorSel.id">${lotaTitular.idLotacao}</c:param>
								<c:param name="orgaoUsu">0</c:param>
								<c:param name="idTipoFormaDoc">${idTpMarca}</c:param>
							</c:url> 
							<siga:monolink texto="${listEstado[3]}" href="${url}" />
						</tr>
					</c:when>
					<c:when test="${listEstado[0]==1}">
						<tr>
							<td>
								<c:url var="url" value="/app/expediente/doc/listar">
								<c:param name="ultMovIdEstadoDoc">${listEstado[0]}</c:param>
								<c:param name="lotaCadastranteSel.id">${lotaTitular.idLotacao}</c:param>
								<c:param name="orgaoUsu">0</c:param>
								<c:param name="idTipoFormaDoc">${idTpMarca}</c:param>
							</c:url><siga:monolink titulo="Documentos que estão em estado temporário(rascunho) e ainda podem ser editados." texto="${listEstado[1]}" href="${url}" />
							
							<td align="right" class="count">
								<c:url var="url" value="/app/expediente/doc/listar">
								<c:param name="ultMovIdEstadoDoc">${listEstado[0]}</c:param>
								<c:param name="cadastranteSel.id">${titular.idPessoa}</c:param>
								<c:param name="orgaoUsu">0</c:param>
								<c:param name="idTipoFormaDoc">${idTpMarca}</c:param>
							</c:url> 
							<siga:monolink titulo="Documentos que estão em estado temporário(rascunho) e ainda podem ser editados." texto="${listEstado[2]}" href="${url}" />
		
							<td align="right" class="count">
								<c:url var="url" value="/app/expediente/doc/listar">
								<c:param name="ultMovIdEstadoDoc">${listEstado[0]}</c:param>
								<c:param name="lotaCadastranteSel.id">${lotaTitular.idLotacao}</c:param>
								<c:param name="idTipoFormaDoc">${idTpMarca}</c:param>
								<c:param name="orgaoUsu">0</c:param>
							</c:url>
							<siga:monolink titulo="Documentos que estão em estado temporário(rascunho) e ainda podem ser editados." texto="${listEstado[3]}" href="${url}" />
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
							<td>
								<c:url var="url" value="/app/expediente/doc/listar">
								<c:param name="ultMovIdEstadoDoc">${listEstado[0]}</c:param>
								<c:choose>
									<c:when test="${listEstado[0]==11}">
										<c:param name="ultMovLotaCadastranteSel.id">${lotaTitular.idLotacao}</c:param>
									</c:when>
									<c:otherwise>
										<c:param name="ultMovLotaRespSel.id">${lotaTitular.idLotacao}</c:param>
									</c:otherwise>
								</c:choose>
								<c:param name="orgaoUsu">0</c:param>
								<c:param name="idTipoFormaDoc">${idTpMarca}</c:param>
							</c:url><siga:monolink titulo="${titulo}" texto="${listEstado[1]}" href="${url}" />
							
							<td align="right" class="count">
								<c:url var="url" value="/app/expediente/doc/listar">
								<c:param name="ultMovIdEstadoDoc">${listEstado[0]}</c:param>
								<c:choose>
									<c:when test="${listEstado[0]==11}">
										<c:param name="ultMovCadastranteSel.id">${titular.idPessoa}</c:param>
									</c:when>
									<c:otherwise>
										<c:param name="ultMovRespSel.id">${titular.idPessoa}</c:param>
									</c:otherwise>
								</c:choose>
								<c:param name="orgaoUsu">0</c:param>
								<c:param name="idTipoFormaDoc">${idTpMarca}</c:param>
							</c:url> 
							<siga:monolink titulo="${titulo}" texto="${listEstado[2]}" href="${url}" />
		
							<td align="right" class="count">
								<c:url var="url" value="/app/expediente/doc/listar">
								<c:param name="ultMovIdEstadoDoc">${listEstado[0]}</c:param>
								<c:choose>
									<c:when test="${listEstado[0]==11}">
										<c:param name="ultMovLotaCadastranteSel.id">${lotaTitular.idLotacao}</c:param>
									</c:when>
									<c:otherwise>
										<c:param name="ultMovLotaRespSel.id">${lotaTitular.idLotacao}</c:param>
									</c:otherwise>
								</c:choose>
								<c:param name="orgaoUsu">0</c:param>
								<c:param name="idTipoFormaDoc">${idTpMarca}</c:param>
							</c:url>
							<siga:monolink titulo="${titulo}" texto="${listEstado[3]}" href="${url}" />
						</tr>
					</c:when>
					<c:when test="${listEstado[0]==-1}">
						<tr>
							<td>
							<c:url var="url" value="/app/expediente/doc/listar">
								<c:param name="ultMovIdEstadoDoc">3</c:param>
								<c:param name="ultMovLotaCadastranteSel.id">${lotaTitular.idLotacao}</c:param>
								<c:param name="orgaoUsu">0</c:param>
								<c:param name="idTipoFormaDoc">${idTpMarca}</c:param>
							</c:url> 
							<siga:monolink titulo="Documentos não eletrônicos que foram transferidos, pelo usuário, para outra lotação/pessoa mas ainda não foram recebidos" texto="${listEstado[1]}" href="${url}" />		
		
							<td align="right" class="count">
							<c:url var="url" value="/app/expediente/doc/listar">
								<c:param name="ultMovIdEstadoDoc">3</c:param>
								<c:param name="ultMovCadastranteSel.id">${titular.idPessoa}</c:param>
								<c:param name="orgaoUsu">0</c:param>
								<c:param name="idTipoFormaDoc">${idTpMarca}</c:param>
							</c:url> 
							<siga:monolink titulo="Documentos não eletrônicos que foram transferidos, pelo usuário, para outra lotação/pessoa mas ainda não foram recebidos" texto="${listEstado[2]}" href="${url}" />
		
							<td align="right" class="count">
							<c:url var="url" value="/app/expediente/doc/listar">
								<c:param name="ultMovIdEstadoDoc">3</c:param>
								<c:param name="ultMovLotaCadastranteSel.id">${lotaTitular.idLotacao}</c:param>
								<c:param name="orgaoUsu">0</c:param>
								<c:param name="idTipoFormaDoc">${idTpMarca}</c:param>
							</c:url> 
							<siga:monolink titulo="Documentos não eletrônicos que foram transferidos, pelo usuário, para outra lotação/pessoa mas ainda não foram recebidos" texto="${listEstado[3]}" href="${url}" />
						</tr>
					</c:when>
				</c:choose>
			</c:forEach>
		</table>
		<form name="frm" action="editar" namespace="/expediente/doc" method="get" theme="simple">
			<input hidden name="sigla"/>
			
			<table class="table" width="100%" class="zero">
				<tr>
					<td>
						<c:url var="url1" value="/app/expediente/doc/exibir"/>
						<c:url var="url2" value="/app/expediente/mov/receber"/>
						<c:url var="url3" value="/app/expediente/doc/encerrar"/> 
							
						<c:url var="url4" value="/app/expediente/mov/transferir_lote"/>
						<c:url var="url5" value="/app/expediente/mov/receber_lote"/>
						<c:url var="url6" value="/app/expediente/mov" /> 
					
						<select name="docAcao" onchange="onChangeAcao(this.value)" class="custom-select">
							<option value="/sigaex/app/expediente/doc/exibir">Exibir</option>
							<option value="/sigaex/expediente/mov/receber.action">Receber</option>
							<option value="/sigaex/expediente/mov/encerrar.action">Encerrar</option>
						</select>
						<!-- , '04':'Transferir Lote', ${itemRemeter} '05':'Receber Lote', '06':'2? Via Protocolo'} -->
						<siga:selecao tema="simple" buscar="nao" ocultardescricao="sim" propriedade="documentoVia" modulo="sigaex"/>
						
						<input type="button" name="ok" value="Ok" onclick="javascript:submitOk();" class="btn btn-primary"/> 
						<c:url var="url" value="/app/expediente/doc/editar"/>
					</td>
					<td>
						<input type="submit" align="right" name="novo" value="Novo" class="btn btn-primary" title="Criar Novo Expediente" onclick="javascript:frm.action='${url}';" />
					</td>
				</tr>
			</table>
		</form>
	
	<script>
		function onChangeAcao(value) {
			console.log('foi', value);
			if (value == '04') 
				window.location='${url4}'; 
			else if (value == '05') 
				window.location='${url5}'; 
			else if (value == '06') 
				window.location='${url6}'; 
			else if (value == '07') 
				window.location='${url7}';
		}
	</script>
	</div>
</siga:pagina>