<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" buffer="32kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>

<siga:pagina titulo="Ger&ecirc;ncia de Publica&ccedil;&atilde;o Boletim Interno">
<link rel="stylesheet" href="/siga/javascript/select2/select2.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="/siga/javascript/select2/select2-bootstrap.css" type="text/css" media="screen, projection" />

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
			frm.action='${request.contextPath}/app/configuracao/gerenciar_publicacao_boletim?id=${doc.idDoc}';
			frm.submit();
		}
	</script>
	<div class="container-fluid">
		<div class="card bg-light mb-3" >		
			<div class="card-header"><h5>
				Gerenciamento de permiss&otilde;es para solicita&ccedil;&atilde;o de publica&ccedil;&atilde;o no BI
			</h5></div>	
			<form id="frm" name="frm" action="${request.contextPath}/app/configuracao/gerenciar_publicacao_boletim_gravar" method="post">
				<input type="hidden" name="postback" value="1" />
				<input type="hidden" name="gerenciaPublicacao" value="sim" />
				<input type="hidden" name="idTpMov" value="36" />
				<input type="hidden" name="idTpConfiguracao" value="1" />
				
				<div class="card-body">
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								<label>Incluir Permiss&atilde;o de Publica&ccedil;&atilde;o</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label>Forma</label>
								<select name="idFormaDoc" onchange="javascript:sbmt();" class="form-control">
									<option value="0">[Todas]</option>
									<c:forEach var="forma" items="${listaFormas}">
										<option value="${forma.idFormaDoc}">${forma.descrFormaDoc}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<label>Modelo</label>
								<select name="idMod" class="form-control  siga-select2">
									<option value="0">[Todos]</option>
									<c:forEach var="modelo" items="${listaModelosPorForma}">
										<option value="${modelo.idMod}">${modelo.nmMod}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label>Aplicar a</label>
								<select name="tipoPublicador" onchange="javascript:hideShowSel(this);" class="form-control">
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
								
								
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<span id="spanPublicador" style="${publicadorStyle}"> 
									<siga:selecao propriedade="pessoa" tema="simple" modulo="siga"/>
								</span>
								
								<span id="spanLotaPublicador" style="${lotaPublicadorStyle}"> 
									<siga:selecao propriedade="lotacao" tema="simple" modulo="siga"/>
								</span>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-2">
							<div class="form-group">
								<label>Permiss&atilde;o</label>
								<select name="idSituacao" class="form-control">
									<c:forEach var="situacao" items="${listaSituacaoPodeNaoPode}">
										<option value="${situacao.idSitConfiguracao}">${situacao.descr}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">		
								<input type="submit" value="Incluir" class="btn btn-primary">
							</input>
						</div>
					</div>
					
				</div>
			</form>
		</div>
	</div>
	<c:forEach var="config" items="${itens}">
		<h5>${config[0]}</h5>
		<table border="0" class="table table-sm table-striped">
			<thead class="${thead_color}">
				<th align="center">Pessoa</th>
				<th align="center">Lota&ccedil;&atilde;o</th>
				<th align="center">Permiss&atilde;o</th>
				<th align="center">Data de Fim de Vig&ecirc;ncia</th>
				<th align="center"></th>
				<th></th>
			</thead>
			<tbody class="table-bordered">
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

					<td>${configuracao.cpSituacaoConfiguracao.descr}</td>
					<td>${configuracao.hisDtFimDDMMYY_HHMMSS}<td>
					<td>
						<c:if test="${configuracao.hisDtFim != null}">
							<c:url var="url" value="/app/configuracao/excluir_configuracao_publicacao_boletim">
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
			</tbody>
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

<script type="text/javascript" src="/siga/javascript/select2/select2.min.js"></script>
<script type="text/javascript" src="/siga/javascript/select2/i18n/pt-BR.js"></script>
<script type="text/javascript" src="/siga/javascript/siga.select2.js"></script>	
</siga:pagina>
