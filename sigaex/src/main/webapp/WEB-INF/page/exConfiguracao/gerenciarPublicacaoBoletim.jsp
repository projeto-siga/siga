<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Gerência de Publicação Boletim Interno">
	<script type="text/javascript">
		function hideShowSel(combo){
			var sel1Span = document.getElementById('span' + combo.name.substring(4));
			var sel2Span = document.getElementById('spanLota' + combo.name.substring(4));

			if (combo.selectedIndex==0){
				sel1Span.style.display="";
				sel2Span.style.display="none";
			}else {
				sel1Span.style.display="none";
				sel2Span.style.display="";
			}
		}
		
		function sbmt(id) {
			var frm = document.getElementById('frm');
			frm.action='${request.contextPath}/app/expediente/configuracao/gerenciar_publicacao_boletim?id=${doc.idDoc}';
			frm.submit();
		}
	</script>
	
	<h1>Gerenciamento de permissões para solicitação de publicação no BI:</h1>
	<br />
	
	<form id="frm" name="frm" action="${request.contextPath}/app/expediente/configuracao/gerenciar_publicacao_boletim_gravar" method="POST">
		<input type="hidden" name="postback" value="1" />
		<input type="hidden" name="gerenciaPublicacao" value="sim" />
		<input type="hidden" name="idTpMov" value="36" />
		<input type="hidden" name="idTpConfiguracao" value="1" />
		
		<table class="form">
			<tr class="header">
				<td colspan="2">Incluir Permissão de Publicação</td>
			</tr>
			<tr>
				<td>Forma:</td>
				<td>
					<select name="idFormaDoc" onchange="javascript:sbmt();">
						<option value="0">[Todas]</option>
						<c:forEach var="forma" items="${listaFormas}">
							<option value="${forma.idFormaDoc}">${forma.descrFormaDoc}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>Modelo:</td>
				<td>
					<select name="idMod">
						<option value="0">[Todos]</option>
						<c:forEach var="modelo" items="${listaModelosPorForma}">
							<option value="${modelo.idMod}">${modelo.nmMod}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>Aplicar a:</td>
				<td>
					<select name="tipoPublicador" onchange="javascript:hideShowSel(this);">
						<c:forEach var="tipoEntry" items="${listaTipoPublicador}">
							<option value="${tipoEntry.key}">${tipoEntry.value}</option>
						</c:forEach>
					</select>
					
					<c:choose>
						<c:when test="${tipoPublicador == 1}">
							<c:set var="publicadorStyle" value="" />
							<c:set var="lotaPublicadorStyle">display:none</c:set>
						</c:when>
						<c:when test="${tipoPublicador == 2}">
							<c:set var="publicadorStyle">display:none</c:set>
							<c:set var="lotaPublicadorStyle" value="" />
						</c:when>
					</c:choose>
					
					<span id="spanPublicador" style="${publicadorStyle}"> 
						<siga:selecao propriedade="pessoa" tema="simple" modulo="siga"/>
					</span>
					
					<span id="spanLotaPublicador" style="${lotaPublicadorStyle}"> 
						<siga:selecao propriedade="lotacao" tema="simple" modulo="siga"/>
					</span>
				</td>
			</tr>
			<tr>
				<td>Permissão:</td>
				<td>
					<select name="idSituacao">
						<c:forEach var="situacao" items="${listaSituacaoPodeNaoPode}">
							<option value="${situacao.idSitConfiguracao}">${situacao.dscSitConfiguracao}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Incluir"></td>
			</tr>
		</table>
	</form>
	<br />
	<br />
	
	<c:forEach var="config" items="${itens}">
		<h1>${config[0]}</h1>
		
		<table class="list">
			<tr class="header">
				<td align="center">Pessoa</td>
				<td align="center">Lotação</td>
				<td align="center">Permissão</td>
				<td align="center">Data de Fim de Vigência</td>
				<td align="center"></td>
				<td></td>
			</tr>
			
			<c:set var="evenorodd" value="" />

			<c:forEach var="configuracao" items="${config[1]}">
				<tr class="${evenorodd}">
					<c:choose>
						<c:when
							test="${(empty configuracao.dpPessoa) and (empty configuracao.lotacao)}">
							<td colspan="2" align="center">--Todos--</td>
						</c:when>
						<c:otherwise>
							<td>
								<c:if test="${not empty configuracao.dpPessoa}">
									${configuracao.dpPessoa.descricao}
								</c:if>
							</td>
							<td>
								<c:if test="${not empty configuracao.lotacao}">
									<siga:selecionado sigla="${configuracao.lotacao.sigla}" descricao="${configuracao.lotacao.descricao}" />
								</c:if>
							</td>
						</c:otherwise>
					</c:choose>

					<td>${configuracao.cpSituacaoConfiguracao.dscSitConfiguracao}</td>
					<td>${configuracao.hisDtFimDDMMYY_HHMMSS}<td>
					<td>
						<c:if test="${configuracao.hisDtFim != null}">
							<c:url var="url" value="/app/expediente/configuracao/excluir_configuracao_publicacao_boletim">
								<c:param name="id">${configuracao.idConfiguracao}</c:param>
							</c:url>
							 <a href="${url}">Excluir</a>
						</c:if>
					</td>
				</tr>
				<c:choose>
					<c:when test='${evenorodd == "even"}'>
						<c:set var="evenorodd" value="odd" />
					</c:when>
					<c:otherwise>
						<c:set var="evenorodd" value="even" />
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</table>
	</c:forEach>
	
	<script>
		var idFormaDoc = '${idFormaDoc}',
			tipoPublicador = '${tipoPublicador}',
			idMod = '${idMod}',
			idSituacao = '${idSituacao}';

		select('[name=idFormaDoc]', idFormaDoc);
		select('[name=tipoPublicador]', tipoPublicador);
		select('[name=idMod]', idMod);
		select('[name=idSituacao]', idSituacao);

		function select(selector, value) {
			if(value) {
				$(selector).val(value);
			}
		}
	</script>
	
</siga:pagina>
