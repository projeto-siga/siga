<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<script type="text/javascript" language="Javascript1.1">

function alteraTipoDaForma(){
	ReplaceInnerHTMLFromAjaxResponse('${pageContext.request.contextPath}/app/expediente/doc/carregar_lista_formas?tipoForma='+document.getElementById('tipoForma').value+'&idFormaDoc='+'${idFormaDoc}', null, document.getElementById('comboFormaDiv'))
}

function alteraForma(){
	ReplaceInnerHTMLFromAjaxResponse('${pageContext.request.contextPath}/app/expediente/doc/carregar_lista_modelos?forma='+document.getElementById('forma').value+'&idMod='+'${idMod}', null, document.getElementById('comboModeloDiv'))
}

function sbmt(offset) {
	if (offset==null) {d
		offset=0;
	}
	buscar["offset"].value=offset;
	buscar.submit();
}

function montaDescricao(id,via,descrDoc){
	var popW = 700;
	var popH = 500; 
	var winleft = (screen.width - popW) / 2;
	var winUp = (screen.height - popH) / 2;
	var winProp = '\'width='+popW+',height='+popH+',left='+winleft+',top='+winUp+',scrollbars=yes,resizable\'';
	var url='\'<c:url value="/app/expediente/doc/exibir"/>?popup=true&id='+id+'&via='+via+'\'';

	var onclick=' onclick="javascript:window.open('+url+',\'documento\','+winProp+')"';
	var href=' href="javascript:void(0)"';
	
	var a='<a'+href+onclick+'>'+descrDoc+'</a>';
	return a;
}

function alteraOrigem()
{
	var objSelecionado = document.getElementById('idTpDoc');
	
	switch (parseInt(objSelecionado.value))
	{
		case 0:
			document.getElementById('trNumOrigDoc').style.display = 'none';
			document.getElementById('trNumDocSistAntigo').style.display = 'none';
			document.getElementById('trOrgExterno').style.display = 'none';
			document.getElementById('trTipo').style.display = '';
			
			document.getElementById('idFormaDoc').value = '0';
			break;
		case 1:
			document.getElementById('trNumOrigDoc').style.display = 'none';
			document.getElementById('trNumDocSistAntigo').style.display = 'none';
			document.getElementById('trOrgExterno').style.display = 'none';
			document.getElementById('trTipo').style.display = '';
			
			document.getElementById('idFormaDoc').value = '0';
			break;
		case 2:
			document.getElementById('trNumOrigDoc').style.display = '';
			document.getElementById('trNumDocSistAntigo').style.display = '';
			document.getElementById('trOrgExterno').style.display = 'none';
			document.getElementById('trTipo').style.display = '';
			
			document.getElementById('idFormaDoc').value = '0';
			break;
		case 3:
			document.getElementById('trNumOrigDoc').style.display = '';
			document.getElementById('trNumDocSistAntigo').style.display = '';
			document.getElementById('trOrgExterno').style.display = '';
			document.getElementById('trTipo').style.display = 'none';
			
			document.getElementById('idFormaDoc').value = '5';
			break;
	}
}

function alteraAtendente()
{
	var objSelecionado = document.getElementById('ultMovTipoResp');
	
	switch (parseInt(objSelecionado.value))
	{
		case 1:
			document.getElementById('divUltMovResp').style.display = '';
			document.getElementById('divUltMovLotaResp').style.display = 'none';
			break;
		case 2:
			document.getElementById('divUltMovResp').style.display = 'none';
			document.getElementById('divUltMovLotaResp').style.display = '';
			break;
	}
}

function alteraCadastranteDocumento()
{
	var objSelecionado = document.getElementById('tipoCadastrante');
	
	switch (parseInt(objSelecionado.value))
	{
		case 1:
			document.getElementById('divCadastrante').style.display = '';
			document.getElementById('divLotaCadastrante').style.display = 'none';
			break;
		case 2:
			document.getElementById('divCadastrante').style.display = 'none';
			document.getElementById('divLotaCadastrante').style.display = '';
			break;
	}
}

function alteraDestinatarioDocumento()
{
	var objSelecionado = document.getElementById('tipoDestinatario');
	
	switch (parseInt(objSelecionado.value))
	{
		case 1:
			document.getElementById('divDestinatario').style.display = '';
			document.getElementById('divLotaDestinatario').style.display = 'none';
			document.getElementById('divOrgaoExternoDestinatario').style.display = 'none';
			document.getElementById('divNmDestinatario').style.display = 'none';
			break;
		case 2:
			document.getElementById('divDestinatario').style.display = 'none';
			document.getElementById('divLotaDestinatario').style.display = '';
			document.getElementById('divOrgaoExternoDestinatario').style.display = 'none';
			document.getElementById('divNmDestinatario').style.display = 'none';
			break;
		case 3:
			document.getElementById('divDestinatario').style.display = 'none';
			document.getElementById('divLotaDestinatario').style.display = 'none';
			document.getElementById('divOrgaoExternoDestinatario').style.display = '';
			document.getElementById('divNmDestinatario').style.display = 'none';
			break;
		case 4:
			document.getElementById('divDestinatario').style.display = 'none';
			document.getElementById('divLotaDestinatario').style.display = 'none';
			document.getElementById('divOrgaoExternoDestinatario').style.display = 'none';
			document.getElementById('divNmDestinatario').style.display = '';
			break;
	}
}

function limpaCampos()
{
	
	var ultMovTipoResp = document.getElementById('ultMovTipoResp');
	
	switch (parseInt(ultMovTipoResp.value))
	{
		case 1:
			document.getElementById('ultMovLotaRespSel_id').value = '';
			document.getElementById('ultMovLotaRespSel_descricao').value = '';
			document.getElementById('ultMovLotaRespSel_buscar').value = '';
			document.getElementById('ultMovLotaRespSel_sigla').value = '';
			document.getElementById('ultMovLotaRespSelSpan').innerHTML = '';
			break;
		case 2:
			document.getElementById('ultMovRespSel_id').value = '';
			document.getElementById('ultMovRespSel_descricao').value = '';
			document.getElementById('ultMovRespSel_buscar').value = '';
			document.getElementById('ultMovRespSel_sigla').value = '';
			document.getElementById('ultMovRespSelSpan').innerHTML = '';
			break;
	}
	
	var tipoCadastrante = document.getElementById('tipoCadastrante');
	
	switch (parseInt(tipoCadastrante.value))
	{
		case 1:
			document.getElementById('lotaCadastranteSel_id').value = '';
			document.getElementById('lotaCadastranteSel_descricao').value = '';
			document.getElementById('lotaCadastranteSel_buscar').value = '';
			document.getElementById('lotaCadastranteSel_sigla').value = '';
			document.getElementById('lotaCadastranteSelSpan').innerHTML = '';
			break;
		case 2:
			document.getElementById('cadastranteSel_id').value = '';
			document.getElementById('cadastranteSel_descricao').value = '';
			document.getElementById('cadastranteSel_buscar').value = '';
			document.getElementById('cadastranteSel_sigla').value = '';
			document.getElementById('cadastranteSelSpan').innerHTML = '';
			break;
	}
	
	var tipoDestinatario = document.getElementById('tipoDestinatario');
	
	switch (parseInt(tipoDestinatario.value))
	{      
		case 1:
			document.getElementById('lotacaoDestinatarioSel_id').value = '';
			document.getElementById('lotacaoDestinatarioSel_descricao').value = '';
			document.getElementById('lotacaoDestinatarioSel_buscar').value = '';
			document.getElementById('reqlotacaoDestinatarioSel').value = '';
			document.getElementById('lotacaoDestinatarioSel_sigla').value = '';
			document.getElementById('lotacaoDestinatarioSelSpan').innerHTML = '';
			document.getElementById('orgaoExternoDestinatarioSel_id').value = '';
			document.getElementById('orgaoExternoDestinatarioSel_descricao').value = '';
			document.getElementById('orgaoExternoDestinatarioSel_buscar').value = '';
			document.getElementById('reqorgaoExternoDestinatarioSel').value = '';
			document.getElementById('orgaoExternoDestinatarioSel_sigla').value = '';
			document.getElementById('orgaoExternoDestinatarioSelSpan').innerHTML = '';
			document.getElementById('nmDestinatario').value = '';

			break;
		case 2:
			document.getElementById('destinatarioSel_id').value = '';
			document.getElementById('destinatarioSel_descricao').value = '';
			document.getElementById('destinatarioSel_buscar').value = '';
			document.getElementById('reqdestinatarioSel').value = '';
			document.getElementById('destinatarioSel_sigla').value = '';
			document.getElementById('destinatarioSelSpan').innerHTML = '';
			document.getElementById('orgaoExternoDestinatarioSel_id').value = '';
			document.getElementById('orgaoExternoDestinatarioSel_descricao').value = '';
			document.getElementById('orgaoExternoDestinatarioSel_buscar').value = '';
			document.getElementById('reqorgaoExternoDestinatarioSel').value = '';
			document.getElementById('orgaoExternoDestinatarioSel_sigla').value = '';
			document.getElementById('orgaoExternoDestinatarioSelSpan').innerHTML = '';
			document.getElementById('nmDestinatario').value = '';

			break;
		case 3:
			document.getElementById('destinatarioSel_id').value = '';
			document.getElementById('destinatarioSel_descricao').value = '';
			document.getElementById('destinatarioSel_buscar').value = '';
			document.getElementById('reqdestinatarioSel').value = '';
			document.getElementById('destinatarioSel_sigla').value = '';
			document.getElementById('destinatarioSelSpan').innerHTML = '';
			document.getElementById('lotacaoDestinatarioSel_id').value = '';
			document.getElementById('lotacaoDestinatarioSel_descricao').value = '';
			document.getElementById('lotacaoDestinatarioSel_buscar').value = '';
			document.getElementById('reqlotacaoDestinatarioSel').value = '';
			document.getElementById('lotacaoDestinatarioSel_sigla').value = '';
			document.getElementById('lotacaoDestinatarioSelSpan').innerHTML = '';
			document.getElementById('nmDestinatario').value = '';
		
			break;
		case 4:
			document.getElementById('destinatarioSel_id').value = '';
			document.getElementById('destinatarioSel_descricao').value = '';
			document.getElementById('destinatarioSel_buscar').value = '';
			document.getElementById('reqdestinatarioSel').value = '';
			document.getElementById('destinatarioSel_sigla').value = '';
			document.getElementById('destinatarioSelSpan').innerHTML = '';
			document.getElementById('lotacaoDestinatarioSel_id').value = '';
			document.getElementById('lotacaoDestinatarioSel_descricao').value = '';
			document.getElementById('lotacaoDestinatarioSel_buscar').value = '';
			document.getElementById('reqlotacaoDestinatarioSel').value = '';
			document.getElementById('lotacaoDestinatarioSel_sigla').value = '';
			document.getElementById('lotacaoDestinatarioSelSpan').innerHTML = '';
			document.getElementById('orgaoExternoDestinatarioSel_id').value = '';
			document.getElementById('orgaoExternoDestinatarioSel_descricao').value = '';
			document.getElementById('orgaoExternoDestinatarioSel_buscar').value = '';
			document.getElementById('reqorgaoExternoDestinatarioSel').value = '';
			document.getElementById('orgaoExternoDestinatarioSel_sigla').value = '';
			document.getElementById('orgaoExternoDestinatarioSelSpan').innerHTML = '';
		
			break;
	}

	
	var listar_idTpDoc = document.getElementById('idTpDoc');
	
	switch (parseInt(listar_idTpDoc.value))
	{
		case 0:
			document.getElementById('numExtDoc').value = '';
			
			document.getElementById('cpOrgaoSel_id').value = '';
			document.getElementById('cpOrgaoSel_descricao').value = '';
			document.getElementById('cpOrgaoSel_buscar').value = '';
			document.getElementById('cpOrgaoSel_sigla').value = '';
			document.getElementById('cpOrgaoSelSpan').innerHTML = '';
			document.getElementById('numAntigoDoc').value = '';
			
			break;	
		case 1:
			document.getElementById('numExtDoc').value = '';
			document.getElementById('cpOrgaoSel_id').value = '';
			document.getElementById('cpOrgaoSel_descricao').value = '';
			document.getElementById('cpOrgaoSel_buscar').value = '';
			document.getElementById('cpOrgaoSel_sigla').value = '';
			document.getElementById('cpOrgaoSelSpan').innerHTML = '';
			document.getElementById('numAntigoDoc').value = '';
			
			break;
		case 2:
			document.getElementById('cpOrgaoSel_id').value = '';
			document.getElementById('cpOrgaoSel_descricao').value = '';
			document.getElementById('cpOrgaoSel_buscar').value = '';
			document.getElementById('cpOrgaoSel_sigla').value = '';
			document.getElementById('cpOrgaoSelSpan').innerHTML = '';
			
			break;
		case 3:
			document.getElementById('idFormaDoc').value = '5';

			break;
	}
	
	var count = 0;

	if (document.getElementById('idTpDoc').value != 0)
		count++;	
	
	if (document.getElementById('dtDocString').value != "")
		count++;
		
	if (document.getElementById('dtDocFinalString').value != "")
		count++;
	
	if (document.getElementById('idTipoFormaDoc').value != 0)
		count++;
	
	if (document.getElementById('idMod') != null && document.getElementById('idMod').value != 0)
		count++;				
		
	if (document.getElementById('idFormaDoc').value != 0)
		count++;	
	
	if (document.getElementById('anoEmissaoString').value != "")
		count++;	
		
	if (document.getElementById('numExpediente').value != "")
		count++;		
	
	if (document.getElementById('subscritorSel_id').value != "")
		count++;	
	
	if (document.getElementById('cadastranteSel_id').value != "")
		count++;	
		
	if (document.getElementById('lotaCadastranteSel_id').value != "")
		count++;	
		
	if (document.getElementById('destinatarioSel_id').value != "")
		count++;	

	if (document.getElementById('lotacaoDestinatarioSel_id').value != "")
		count++;	

	if (document.getElementById('orgaoExternoDestinatarioSel_id').value != "")
		count++;	

	if (document.getElementById('nmDestinatario').value != "")
		count++;	

	if (document.getElementById('classificacaoSel_id').value != "")
		count++;	

	if (document.getElementById('descrDocumento').value != "")
		count++;	
		
	if (document.getElementById('fullText').value != "")
		count++;

	if (document.getElementById('ultMovIdEstadoDoc').value != 0)
		count++;	

	if (document.getElementById('ultMovRespSel_id').value != "")
		count++;	

	if (document.getElementById('ultMovLotaRespSel_id').value != "")
		count++;	

	if (count < 2) {
		alert('Esta pesquisa retornará muitos resultados. Favor restringi-la um pouco mais.');
		descarrega();
		return false;
	}

	return true;
}

</script>

<siga:pagina titulo="Lista de Expedientes" popup="${popup}">
	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Pesquisa de Documentos</h5>
			</div>
			<div class="card-body">
			<c:if test="${((empty primeiraVez) or (primeiraVez != 'sim')) and ((empty apenasRefresh) or (apenasRefresh != 1))}">
				<c:if test="${not empty tamanho and tamanho > 0}">
					<div class="gt-content-box gt-for-table">
						<table class="table table-striped table-hover ">
							<thead class="${thead_color}">
								<tr>
									<th rowspan="3" align="right">
										Número
									</th>
									<th colspan="3" align="center">
										Documento
									</th>
									<th colspan="4" align="center">
										Situação
									</th>
									<th rowspan="3">
										Tipo
									</th>
									<th rowspan="3">
										Modelo
									</th>
									<th rowspan="3">
										Descrição
									</th>
									<c:if test="${visualizacao == 1}"> 
										<th rowspan="3">
											Última Anotação
										</th>
									</c:if>
								</tr>
								<tr>
									<th rowspan="2" align="center">
										Data
									</th>
									<th colspan="2" align="center">
										Subscritor
									</th>
									<th rowspan="2" align="center">
										Data
									</th>
									<th colspan="2" align="center">
										Atendente
									</th>
									<th rowspan="2" align="center">
										Situação
									</th>
								</tr>
								<tr>
									<th align="center">
										Lotação
									</th>
									<th align="center">
										Pessoa
									</th>
									<th align="center">
										Lotação
									</th>
									<th align="center">
										Pessoa
									</th>
								</tr>
							</thead>

							<siga:paginador maxItens="${itemPagina}" maxIndices="10" totalItens="${tamanho}" itens="${itens}" var="documento">
								<c:choose>
									<c:when test="${documento[0].eletronico}">
										<c:set var="exibedoc" value="even" />
									</c:when>
									<c:otherwise>
										<c:set var="exibedoc" value="fisicoeven" />
									</c:otherwise>
								</c:choose>

								<tr class="${exibedoc}">
									<c:set var="podeAcessar" value="${f:testaCompetencia('acessarDocumento',titular,lotaTitular, documento[1])}" />
									<td width="11.5%" align="right">
										<c:choose>
											<c:when test='${popup!="true"}'>
												<c:choose>
													<c:when test="${podeAcessar eq true}">
														<a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${documento[1].sigla}">
															${documento[1].codigo}
														</a>
													</c:when>
													<c:otherwise> 
														${documento[1].codigo}
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
												<a href="javascript:opener.retorna_${propriedade}('${documento[1].id}','${documento[1].sigla}','${f:selDescricaoConfidencial(documento[1], lotaTitular, titular)}');">
													${documento[1].codigo}
												</a>
											</c:otherwise>
										</c:choose>
									</td>
									<c:if test="${documento[1].numSequencia != 0}">
										<td width="5%" align="center">
											${documento[0].dtDocDDMMYY}
										</td>
										<td width="4%" align="center">
											<siga:selecionado sigla="${documento[0].lotaSubscritor.sigla}" descricao="${documento[0].lotaSubscritor.descricao}" 
												lotacaoParam="${documento[0].lotaSubscritor.orgaoUsuario.siglaOrgaoUsu}${documento[0].lotaSubscritor.sigla}" />
										</td>
										<td width="4%" align="center">
											<siga:selecionado sigla="${documento[0].subscritor.iniciais}" descricao="${documento[0].subscritor.descricao}"
												pessoaParam="${documento[0].subscritor.sigla}" />
										</td>
										<td width="5%" align="center">
											${documento[2].dtIniMarcaDDMMYYYY}
										</td>
										<td width="4%" align="center">
											<siga:selecionado sigla="${documento[2].dpLotacaoIni.lotacaoAtual.sigla}" descricao="${documento[2].dpLotacaoIni.lotacaoAtual.descricao}"
												lotacaoParam="${documento[2].dpLotacaoIni.orgaoUsuario.siglaOrgaoUsu}${documento[2].dpLotacaoIni.sigla}" />
										</td>
										<td width="4%" align="center">
											<siga:selecionado sigla="${documento[2].dpPessoaIni.iniciais}" descricao="${documento[2].dpPessoaIni.descricao}"
												pessoaParam="${documento[2].dpPessoaIni.sigla}" />
										</td>
										<td width="10.5%" align="center">
											${documento[2].cpMarcador.descrMarcador}
										</td>
									</c:if>
									<c:if test="${documento[1].numSequencia == 0}">
										<td width="5%" align="center">
											${documento[0].dtDocDDMMYY}
										</td>
										<td width="4%" align="center">
											<siga:selecionado sigla="${documento[0].lotaSubscritor.sigla}" descricao="${documento[0].lotaSubscritor.descricao}"
												lotacaoParam="${documento[0].lotaSubscritor.orgaoUsuario.siglaOrgao}${documento[0].lotaSubscritor.sigla}" />
										</td>
										<td width="4%" align="center">
											<siga:selecionado sigla="${documento[0].subscritor.iniciais}" descricao="${documento[0].subscritor.descricao}"
												pessoaParam="${documento[0].subscritor.sigla}" />
										</td>
										<td width="5%" align="center">
											tag1
										</td>
										<td width="4%" align="center">
										</td>
										<td width="4%" align="center">
										</td>
										<td width="10.5%" align="center">
											tag4
										</td>
									</c:if>

									<td width="6%">
										${documento[0].descrFormaDoc}
									</td>
									<td width="6%">
										${documento[0].nmMod}
									</td>

									<c:set var="acessivel" value="" />
									<c:set var="acessivel" value="${f:testaCompetencia('acessarDocumento',titular,lotaTitular,documento[1])}" />
									<c:choose>
										<c:when test="${acessivel eq true}">
											<c:set var="estilo" value="" />
											<c:if test="${f:mostraDescricaoConfidencial(documento[0], titular, lotaTitular) eq true}">
												<c:set var="estilo" value="confidencial" />
											</c:if>
											<td class="${estilo}" width="38%">
												${f:descricaoSePuderAcessar(documento[0], titular, lotaTitular)}
											</td>
											<c:if test="${visualizacao == 1}"> 
												<td class="${estilo}" width="38%">
													${f:anotacaoConfidencial(documento[1], titular,lotaTitular)}
												</td>
											</c:if>
										</c:when>
										<c:otherwise>
											<td>
												[Descrição Inacessível]
											</td>
											<c:if test="${visualizacao == 1}"> 
												<td>
													[Anotação Inacessível]
												</td>
											</c:if>
										</c:otherwise>
									</c:choose>
								</tr>
							</siga:paginador>
							<br />
							</c:if>
								<c:if test="${empty tamanho or tamanho == 0}">
									<p class="">
										A pesquisa não retornou resultados.
									</p>
								</c:if>
							</c:if>
						</div>
					</div>
					<div class="card bg-light mb-3">
						<div class="card-header">
							<h5>Dados do Documento</h5>
						</div>
						<div class="card-body">
							<form id="buscar" name="buscar" onsubmit="javascript: return limpaCampos()"
									action="buscar" method="get" class="form100">
								<input type="hidden" name="popup" value="${popup}" />
								<input type="hidden" name="propriedade" value="${propriedade}" />
								<input type="hidden" name="postback" value="1" />
								<input type="hidden" name="apenasRefresh" value="0" />
								<input type="hidden" name="offset" value="0" />
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group">
											<label>Situação</label> 
											<select name="ultMovIdEstadoDoc" onchange="javascript:sbmt();" class="form-control">
												<option value="0">
													[Todos]
												</option>
												<c:forEach items="${estados}" var="item">
													<option value="${item.idMarcador}" ${item.idMarcador == ultMovIdEstadoDoc ? 'selected' : ''}>
														${item.descrMarcador}
													</option>  
												</c:forEach>
											</select>
										</div>
									</div>
									<div class="col-sm-3">
										<div class="form-group">
											<label>Ordenação</label> 
											<select name="ordem" onchange="javascript:sbmt();" class="form-control" >
												<c:forEach items="${listaOrdem}" var="item">
													<option value="${item.key}" ${item.key == ordem ? 'selected' : ''}>
														${item.value}
													</option>  
												</c:forEach>
											</select> 

										</div>
									</div>
									<div class="col-sm-3">
										<div class="form-group">
											<label>Visualização</label> 
											<select name="visualizacao" onchange="javascript:sbmt();" class="form-control">
												<c:forEach items="${listaVisualizacao}" var="item">
													<option value="${item.key}" ${item.key == visualizacao ? 'selected' : ''}>
														${item.value}
													</option>  
												</c:forEach>
											</select>

										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-4">
										<div class="form-group">
											<label>Pessoa/Lotação</label> 
											<select id="ultMovTipoResp" name="ultMovTipoResp" onchange="javascript:alteraAtendente();" class="form-control">
												<c:forEach items="${listaTipoResp}" var="item">
													<option value="${item.key}" ${item.key == ultMovTipoResp ? 'selected' : ''}>
														${item.value}
													</option>  
												</c:forEach>
											</select>
										</div>
									</div>
									<div class="col-sm-8">
										<div class="form-group">
											<label>&nbsp;</label> 
											<c:if test="${ultMovTipoResp == 1}">
												<span id="divUltMovResp" style="display: ">
													<siga:selecao propriedade="ultMovResp" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
												</span>
												<span id="divUltMovLotaResp" style="display: none">
													<siga:selecao propriedade="ultMovLotaResp" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
												</span>
											</c:if>
											<c:if test="${ultMovTipoResp == 2}">
												<span id="divUltMovResp" style="display: none">
													<siga:selecao propriedade="ultMovResp" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
												</span>
												<span id="divUltMovLotaResp" style="display: ">
													<siga:selecao propriedade="ultMovLotaResp" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
												</span>
											</c:if>
										</div>
									</div>	
								</div>
				<div class="form-row">
						<div class="form-group col-md-3">
							<label for="orgaoUsu">Órgão</label> <select class="form-control"
								id="orgaoUsu" name="orgaoUsu">
								<option value="0">[Todos]</option>
								<c:forEach items="${orgaosUsu}" var="item">
									<option value="${item.idOrgaoUsu}"
										${item.idOrgaoUsu == orgaoUsu ? 'selected' : ''}>
										${item.nmOrgaoUsu}</option>
								</c:forEach>
							</select>
						</div>
						<c:if test="${siga_cliente != 'GOVSP'}">
							<div class="form-group col-md-3">
								<label for="idTpDoc">Origem</label> <select class="form-control"
									id="idTpDoc" name="idTpDoc"
									onchange="javascript:alteraOrigem();">
								<option value="0">[Todos]</option>
								<c:forEach items="${tiposDocumento}" var="item">
									<option value="${item.idTpDoc}"
										${item.idTpDoc == idTpDoc ? 'selected' : ''}>
										${item.descrTipoDocumento}</option>
								</c:forEach>
								</select>
							</div>
						</c:if>
						<div class="form-group col-md-3">
							<label for="dtDocString">Data Inicial</label> <input
								class="form-control" type="text" name="dtDocString"
								id="dtDocString" value="${dtDocString}"
								onblur="javascript:verifica_data(this,0);" />
						</div>
						<div class="form-group col-md-3">
							<label for="dtDocFinalString">Data Final</label> <input
								class="form-control" type="text" name="dtDocFinalString"
								id="dtDocFinalString" value="${dtDocString}"
								onblur="javascript:verifica_data(this,0);" />
						</div>
					</div>
								<c:choose>
									<c:when test="${tipoDocumento != 'externo'}">
										<c:set var="trTipo" value="display: " />
									</c:when>
									<c:otherwise>
										<c:set var="trTipo" value="display: none" />
									</c:otherwise>
								</c:choose>
								<div class="row" style="${trTipo}">
									<div class="col-sm-4">
										<div class="form-group">
											<label>Tipo</label>
											<select id="tipoForma" name="idTipoFormaDoc" onchange="javascript:alteraTipoDaForma();" class="form-control">
												<option value="0">
													[Todos]
												</option>
												<c:forEach items="${tiposFormaDoc}" var="item">
													<option value="${item.idTipoFormaDoc}" ${item.idTipoFormaDoc == idTipoFormaDoc ? 'selected' : ''}>
														${item.descTipoFormaDoc}
													</option>  
												</c:forEach>
											</select>
										</div>
									</div>
									<div class="col-sm-4">
										<div id="comboFormaDiv">
											<script type="text/javascript">
												setTimeout("alteraTipoDaForma()",500);
											</script>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-4">		
										<div id="comboModeloDiv">
											<script type="text/javascript">
												setTimeout("alteraForma()",500);
											</script>
										</div>										
									</div>
								</div>
								<div class="row">
									<div class="col-sm-4">
										<div class="form-group">
											<label>Ano de Emissão</label>
											<select  name="anoEmissaoString" class="form-control">
												<option value="0">
													[Todos]
												</option>
												<c:forEach items="${listaAnos}" var="item">
													<option value="${item}" ${item == anoEmissaoString ? 'selected' : ''}>
														${item}
													</option>
												</c:forEach>
											</select>
										</div>
									</div>
									<div class="col-sm-4">
										<div class="form-group">
											<label>Número</label>
											<input type="text"size="7" name="numExpediente" value="${numExpediente}" maxlength="6" class="form-control"/>
										</div>
									</div>
								</div>
								<c:choose>
									<c:when test='${tipoDocumento == "externo" || tipoDocumento == "antigo"}'>
										<c:set var="trNumOrigDoc" value="display: " />
									</c:when>
									<c:otherwise>
										<c:set var="trNumOrigDoc" value="display: none" />
									</c:otherwise>
								</c:choose>
								<div class="row" style="${trNumOrigDoc}">
									<div class="col-sm">
										<div class="form-group">
											<label for="numExtDoc" class="label">Nº Original do Documento</label>
											<input type="text" name="numExtDoc" value="${numExtDoc}" size="16" value="" id="numExtDoc" class="form-control"/>
										</div>
									</div>
								</div>
								<c:choose>
									<c:when test='${tipoDocumento == "externo"}'>
										<c:set var="trOrgExterno" value="display: " />
									</c:when>
									<c:otherwise>
										<c:set var="trOrgExterno" value="display: none" />
									</c:otherwise>
								</c:choose>
								<div class="row" style="${trOrgExterno}">
									<div class="col-sm">
										<div class="form-group">
											<label for="numExtDoc" class="label">Órgão Externo</label>
											<siga:selecao propriedade="cpOrgao" modulo="siga" titulo="Órgão Externo" tema="simple" />
										</div>
									</div>
								</div>
								<c:choose>
									<c:when test='${tipoDocumento == "externo" || tipoDocumento == "antigo"}'>
										<c:set var="trNumDocSistAntigo" value="display: " />
									</c:when>
									<c:otherwise>
										<c:set var="trNumDocSistAntigo" value="display: none" />
									</c:otherwise>
								</c:choose>
								<div class="row" style="${trNumDocSistAntigo}">
									<div class="col-sm">
										<div class="form-group">
											<label for="numExtDoc" class="label">Nº do Documento no Sistema Antigo</label>
											<input type="text" name="numAntigoDoc" value="${numAntigoDoc}" size="16" value="" id="numAntigoDoc" class="form-control"/>
										</div>
									</div>
								</div>

								<div class="row">
									<div class="col-sm">
										<div class="form-group">
											<label for="numExtDoc" class="label"><fmt:message key="tela.aBuscar.subscritor" /></label>											
											<c:choose>
												<c:when test="${tipoDocumento == 'externo'}">
													<input type="text" label="Subscritor" name="nmSubscritorExt" value="${nmSubscritorExt}" size="80" class="form-control"/>
												</c:when>
												<c:otherwise>
													<siga:selecao titulo="Subscritor:" propriedade="subscritor" paramList="buscarFechadas=true" tema="simple" modulo="siga"/>
												</c:otherwise>
											</c:choose>		
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-4">
										<div class="form-group">
											<label for="numExtDoc" class="label">Cadastrante</label>											
											<select id="tipoCadastrante" name="tipoCadastrante" onchange="javascript:alteraCadastranteDocumento();" class="form-control" >
												<c:forEach items="${listaTipoResp}" var="item">
													<option value="${item.key}" ${item.key == tipoCadastrante ? 'selected' : ''}>
														${item.value}
													</option>  
												</c:forEach>
											</select> 
										</div>
									</div>
									<div class="col-sm-8">
										<div class="form-group">
											<label>&nbsp;</label>
											<c:if test="${tipoCadastrante == 1}">
												<div id="divCadastrante" style="display: ">
													<siga:selecao propriedade="cadastrante" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
												</div>
												<div id="divLotaCadastrante" style="display: none">
													<siga:selecao propriedade="lotaCadastrante" tema="simple" paramList="buscarFechadas=true" modulo="siga" />
												</div>
											</c:if> 
											<c:if test="${tipoCadastrante == 2}">
												<div id="divCadastrante" style="display: none">
													<siga:selecao propriedade="cadastrante" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
												</div>
												<div id="divLotaCadastrante" style="display: ">
													<siga:selecao propriedade="lotaCadastrante" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
												</div>
											</c:if>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-4">
										<div class="form-group">
											<label>Destinatário</label>
											<select id="tipoDestinatario" name="tipoDestinatario"  onchange="javascript:alteraDestinatarioDocumento();" class="form-control" >
												<c:forEach items="${listaTipoDest}" var="item">
													<option value="${item.key}" ${item.key == tipoDestinatario ? 'selected' : ''}>
														${item.value}
													</option>  
												</c:forEach>
											</select>
										</div>
									</div>
									<div class="col-sm-4">
										<div class="form-group">
											<label>&nbsp;</label>
											<c:choose>
												<c:when test='${tipoDestinatario == 1}'>
													<div id="divDestinatario" style="display: ">
														<siga:selecao propriedade="destinatario" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
													</div>
													<div id="divLotaDestinatario" style="display: none">
														<siga:selecao propriedade="lotacaoDestinatario" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
													</div>
													<div id="divOrgaoExternoDestinatario" style="display: none">
														<siga:selecao propriedade="orgaoExternoDestinatario" tema="simple" modulo="siga"/>
													</div>
													<div id="divNmDestinatario" style="display: none">
														<input type="text" name="nmDestinatario" value="${nmDestinatario}" size="80" />
													</div>
												</c:when>
												<c:when test='${tipoDestinatario == 2}'>
													<div id="divDestinatario" style="display: none">
														<siga:selecao propriedade="destinatario" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
													</div>
													<div id="divLotaDestinatario" style="display: ">
														<siga:selecao propriedade="lotacaoDestinatario" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
													</div>
													<div id="divOrgaoExternoDestinatario" style="display: none">
														<siga:selecao propriedade="orgaoExternoDestinatario" tema="simple" modulo="siga"/>
													</div>
													<div id="divNmDestinatario" style="display: none">
														<input type="text" name="nmDestinatario" value="${nmDestinatario}" size="80" class="form-control"/>
													</div>
												</c:when>
												<c:when test='${tipoDestinatario == 3}'>
													<div id="divDestinatario" style="display: none">
														<siga:selecao propriedade="destinatario" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
													</div>
													<div id="divLotaDestinatario" style="display: none">
														<siga:selecao propriedade="lotacaoDestinatario" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
													</div>
													<div id="divOrgaoExternoDestinatario" style="display: ">
														<siga:selecao propriedade="orgaoExternoDestinatario" tema="simple" modulo="siga"/>
													</div>
													<div id="divNmDestinatario" style="display: none">
														<input type="text" name="nmDestinatario" value="${nmDestinatario}" size="80" class="form-control" />
													</div>
												</c:when>
												<c:otherwise>
													<div id="divDestinatario" style="display: none">
														<siga:selecao propriedade="destinatario" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
													</div>
													<div id="divLotaDestinatario" style="display: none">
														<siga:selecao propriedade="lotacaoDestinatario" tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
													</div>
													<div id="divOrgaoExternoDestinatario" style="display: none">
														<siga:selecao propriedade="orgaoExternoDestinatario" tema="simple" modulo="siga"/>
													</div>
													<div id="divNmDestinatario" style="display: ">
														<input type="text" name="nmDestinatario" value="${nmDestinatario}" size="80" class="form-control"/>
													</div>
												</c:otherwise>
											</c:choose>
										</div>
									</div>	
								</div>
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group">
											<label>Classificação</label>	
											<siga:selecao titulo="Classificação:" propriedade="classificacao" tema="simple" modulo="sigaex" urlAcao="buscar" urlSelecionar="selecionar"/>
										</div>
									</div>
								</div>	
								
								<div class="row">
									<div class="col-sm-4">
										<div class="form-group">	
											<label>Descrição</label>
											<input type="text" name="descrDocumento" value="${descrDocumento}" size="80" class="form-control" />
										</div>
									</div>
								</div>	
								<div class="row">
									<div class="col-sm-4">
										<div class="form-group">	
											${f:obterExtensaoBuscaTextual(lotaTitular.orgaoUsuario, fullText)}
										</div>
									</div>
								</div>							
								<div class="row">
									<div class="col-sm-4">
										<div class="form-group">	
											<siga:monobotao inputType="submit" value="Buscar" cssClass="btn btn-primary" />
										</div>
									</div>
								</div>	
							</form>
						</div>
					</div>
				</div>
</siga:pagina>