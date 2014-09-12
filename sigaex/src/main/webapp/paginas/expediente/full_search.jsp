<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>


<siga:pagina titulo="Pesquisa de documentos">

	<script type="text/javascript" language="Javascript1.1"
		src="<c:url value="/staticJavascript.action"/>"></script>

	<script type="text/javascript" language="Javascript1.1">

var newwindow = '';
function popitup_movimentacao(url) {
	if (!newwindow.closed && newwindow.location) {
	} else {
		var popW = 600;
		var popH = 400;
		var winleft = (screen.width - popW) / 2;
		var winUp = (screen.height - popH) / 2;
		winProp = 'width='+popW+',height='+popH+',left='+winleft+',top='+winUp+',scrollbars=yes,resizable'
		newwindow=window.open(url,'mov',winProp);
		//newwindow.name='mov';
		//newwindow.navigate(url);
	}
	
	if (window.focus) {
		newwindow.focus()
	}
}

	<ww:url id="url" action="full_search" namespace="/expediente/doc"/>
	function sbmt(offset) {
		if (offset==null) {
			offset=0;
		}
		frm["p.offset"].value=offset;
		
		frm.action = '${url}';
		frm.submit();
	}
</script>

	<div class="gt-bd clearfix">
	<div class="gt-content clearfix">
	
		<h2>Pesquisa de Documentos</h2>

		<div class="gt-content-box gt-for-table">

	<ww:form name="frm" action="full_search" namespace="/expediente/doc"
		method="POST" theme="simple">
		<ww:token />
		<ww:hidden name="postback" value="1" />
		<ww:hidden name="p.offset" value="0" />
		<table class="gt-form-table">
			<tr class="header">
				<td colspan="2">Dados da Pesquisa</td>
			</tr>
			<tr>
				<td><ww:textfield name="userQuery" size="30" />&nbsp;
				<siga:monobotao inputType="submit" value="Buscar" /></td>
			</tr>
			<tr>
			<td>
				<ww:checkbox name="fullSearchTambemSigilosos" />Buscar documentos com nível de acesso limitado
			</td>
			</tr>
			<%--<tr><span style="color: red">O serviço de busca textual está temporariamente indisponível</span></tr> --%>
			<%--
		<tr>
			<td>Priorizar documentos</td>
			<td><input type="checkbox" name="sub" value="s">Eu sou o subscritor</input>
			<input type="checkbox" name="cad" value="s">Eu sou o cadastrante</input>
			<input type="checkbox" name="tit" value="s">Eu sou o titular</input>
			<input type="checkbox" name="dat" value="s" checked="checked">Ordenar por data</input></td>
		</tr>
		--%>
			<!--  <tr>
		<td>&lt;&lt;Mais Opções&gt;&gt;</td>
		</tr>-->
		</table>
		<ul style="font-size: 10px">
			<li>Digite a palavra entre aspas para pesquisar por toda a
			expressão. Exemplo: <span style="font-weight: bold;"> "fulano
			de tal" memorando</span> trará apenas documentos com a palavra <span style="font-weight: bold;">memorando</span>
			que contenham o nome <span style="font-weight: bold;">completo</span>
			Fulano de Tal</li>
			<li>Digite o sinal de - junto a uma palavra para excluí-la da
			busca. Exemplo: <span style="font-weight: bold;"> "fulano de
			tal" -designação</span> trará todos os documentos que contenham o nome
			Fulano de Tal e não possuam o termo <span style="font-weight: bold;">designação</span></li>
			<li>As palavras são sempre pesquisadas nos seguintes locais:
			corpo do documento, anotações, despachos curtos e anexos</li>
			<li>Apenas os documentos assinados são
			alcançados pela busca</li>
		</ul>

	</ww:form>
	</div>
	
	<c:if test="${empty showedResults && postback==1}">
		<p class="no_results">A pesquisa não retornou resultados</p>
	</c:if>

	<siga:paginador maxItens="${itemPagina}" maxIndices="10"
		totalItens="${tamanho}" itens="${showedResults}" var="result">
		<table class="result_table" width="100%">
			<c:set var="notExDoc"></c:set>
			<c:catch var="notExDoc">
				<%--	<c:set var="testeTipo" value="${result.idDoc}" /> --%>
				<br />
				<ww:url id="url" action="exibir" namespace="/expediente/doc">
					<ww:param name="id">${result[3]}</ww:param>
				</ww:url>
				<tr class="result_top">
					<td class="result_title"><b><ww:a cssClass="result_title"
						cssStyle="result_title" href="%{url}">${result[2]}</ww:a></b></td>
				</tr>
				<tr>
					<td class="result_descr">${result[0]}</td>
				</tr>
				<tr>
					<td class="result_descr_compl"><c:if
						test="${not empty result[4]}">${result[4]},</c:if> ${result[1]} <c:if
						test="${not empty result[5]}">de ${result[5]}</c:if> <c:if
						test="${not empty result[6]}">para ${result[6]}</c:if></td>
				</tr>
			</c:catch>
			<c:if test="${not empty notExDoc}">
				<tr>
					<td class="result_mov_td"><span class="result_mov"><b>

					${result.exTipoMovimentacao.descrTipoMovimentacao} </b><c:if
						test="${result.numVia != null}"> na ${result.numVia}&ordf; via , </c:if>
					em ${result.dtMovDDMMYY}</span><c:if test="${not empty result.descrMov}">: <span
							class="result_descr_mov">${result.descrMov} </span>
					</c:if><c:if test="${result.exTipoMovimentacao.idTpMov == 2}">
						<c:url var='anexo'
							value='/anexo/${result.idMov}/${result.nmArqMov}' />
						<a href="${anexo}" target="_blank" class="link_mov">(${result.nmArqMov})</a>
					</c:if> <c:if
						test="${result.exTipoMovimentacao.idTpMov == 5 || result.exTipoMovimentacao.idTpMov == 6 || result.exTipoMovimentacao.idTpMov == 18}">
						<ww:url id="despacho" action="exibir" namespace="/expediente/mov">
							<ww:param name="id">${result.idMov}</ww:param>
							<ww:param name="popup">true</ww:param>
						</ww:url>
						<a href="${despacho}" target="_blank" class="link_mov">Ver
						despacho</a>
					</c:if></td>
				</tr>
			</c:if>
		</table>
	</siga:paginador>

	<%--
<c:forEach items="${showedResults}" var="result" varStatus="i">
	<table class="result_table" width="50%">
		<c:set var="notExDoc"></c:set>
		<c:catch var="notExDoc">
			<c:set var="testeTipo" value="${result.idDoc}" />
			<br/>
			<ww:url id="url" action="exibir" namespace="/expediente/doc">
				<ww:param name="id">${result.idDoc}</ww:param>
			</ww:url>
			<tr class="result_top">
				<td class="result_title"><b><ww:a cssClass="result_title"
					cssStyle="result_title" href="%{url}">${result.codigo}</ww:a></b></td>
			</tr>
			<tr>
				<td class="result_descr">${result.descrDocumento}</td>
			</tr>
			<tr>
				<td class="result_descr_compl"><c:if
					test="${not empty result.dtDocDDMMYY}">${result.dtDocDDMMYY},</c:if>
				${result.exModelo.nmMod} <c:if
					test="${not empty result.subscritorString}">de ${result.subscritorString}</c:if>
				<c:if test="${not empty result.destinatarioString}">para ${result.destinatarioString}</c:if></td>
			</tr>
		</c:catch>
		<c:if test="${not empty notExDoc}">
			<tr>
				<td class="result_mov_td"><span class="result_mov"><b>

				${result.exTipoMovimentacao.descrTipoMovimentacao} </b><c:if
					test="${result.numVia != null}"> na ${result.numVia}&ordf; via , </c:if>
				em ${result.dtMovDDMMYY}</span><c:if test="${not empty result.descrMov}">: <span
						class="result_descr_mov">${result.descrMov} </span>
				</c:if><c:if test="${result.exTipoMovimentacao.idTpMov == 2}">
					<c:url var='anexo'
						value='/anexo/${result.idMov}/${result.nmArqMov}' />
					<a href="${anexo}" target="_blank" class="link_mov">(${result.nmArqMov})</a>
				</c:if> <c:if
					test="${result.exTipoMovimentacao.idTpMov == 5 || result.exTipoMovimentacao.idTpMov == 6 || result.exTipoMovimentacao.idTpMov == 18}">
					<ww:url id="despacho" action="exibir" namespace="/expediente/mov">
						<ww:param name="id">${result.idMov}</ww:param>
						<ww:param name="popup">true</ww:param>
					</ww:url>
					<a href="${despacho}" target="_blank" class="link_mov">Ver despacho</a>
				</c:if></td>
			</tr>
		</c:if>
	</table>
</c:forEach>
 --%>
	<%-- <br>

	<c:if test="${tamanho >= 40}">
		<p class="aviso30">A pesquisa retornou mais de quarenta
		resultados. Favor, restringi-la um pouco mais.</p>
	</c:if>
	<br> --%>
	</div></div>
</siga:pagina>
