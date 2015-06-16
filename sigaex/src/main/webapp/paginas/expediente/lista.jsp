<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/sigatags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ taglib tagdir="/WEB-INF/tags/mod" prefix="mod"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>

<script type="text/javascript" language="Javascript1.1">

<ww:url id="url" action="carregar_lista_formas" namespace="/expediente/doc">
</ww:url>
function alteraTipoDaForma(){
	ReplaceInnerHTMLFromAjaxResponse('<ww:property value="%{url}"/>'+'?tipoForma='+document.getElementById('tipoForma').value+'&idFormaDoc='+'${idFormaDoc}', null, document.getElementById('comboFormaDiv'))
}

<ww:url id="url" action="carregar_lista_modelos" namespace="/expediente/doc">
</ww:url>
function alteraForma(){
	ReplaceInnerHTMLFromAjaxResponse('<ww:property value="%{url}"/>'+'?forma='+document.getElementById('forma').value+'&idMod='+'${idMod}', null, document.getElementById('comboModeloDiv'))
}

function sbmt(offset) {
	if (offset==null) {d
		offset=0;
	}
	listar["p.offset"].value=offset;
	listar.submit();
}

function montaDescricao(id,via,descrDoc){
	var popW = 700;
	var popH = 500; 
	var winleft = (screen.width - popW) / 2;
	var winUp = (screen.height - popH) / 2;
	var winProp = '\'width='+popW+',height='+popH+',left='+winleft+',top='+winUp+',scrollbars=yes,resizable\'';
	var url='\'<c:url value="/expediente/doc/exibir.action"/>?popup=true&id='+id+'&via='+via+'\'';

	var onclick=' onclick="javascript:window.open('+url+',\'documento\','+winProp+')"';
	var href=' href="javascript:void(0)"';
	
	var a='<a'+href+onclick+'>'+descrDoc+'</a>';
	return a;
}

function alteraOrigem()
{
	//Objeto selecionado
	var objSelecionado = document.getElementById('idTpDoc');
	
	switch (parseInt(objSelecionado.value))
	{
		//Todos
		case 0:
			document.getElementById('trNumOrigDoc').style.display = 'none';
			document.getElementById('trNumDocSistAntigo').style.display = 'none';
			document.getElementById('trOrgExterno').style.display = 'none';
			document.getElementById('trTipo').style.display = '';
			
			document.getElementById('idFormaDoc').value = '0';
			break;
		//Interno Produzido
		case 1:
			document.getElementById('trNumOrigDoc').style.display = 'none';
			document.getElementById('trNumDocSistAntigo').style.display = 'none';
			document.getElementById('trOrgExterno').style.display = 'none';
			document.getElementById('trTipo').style.display = '';
			
			document.getElementById('idFormaDoc').value = '0';
			break;
		//Interno Importado
		case 2:
			document.getElementById('trNumOrigDoc').style.display = '';
			document.getElementById('trNumDocSistAntigo').style.display = '';
			document.getElementById('trOrgExterno').style.display = 'none';
			document.getElementById('trTipo').style.display = '';
			
			document.getElementById('idFormaDoc').value = '0';
			break;
		//Externo
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
	//Objeto selecionado
	var objSelecionado = document.getElementById('ultMovTipoResp');
	
	switch (parseInt(objSelecionado.value))
	{
		//Pesquisa pessoa
		case 1:
			document.getElementById('divUltMovResp').style.display = '';
			document.getElementById('divUltMovLotaResp').style.display = 'none';
			break;
		//Pesquisa órgão
		case 2:
			document.getElementById('divUltMovResp').style.display = 'none';
			document.getElementById('divUltMovLotaResp').style.display = '';
			break;
	}
}

function alteraCadastranteDocumento()
{
	//Objeto selecionado
	var objSelecionado = document.getElementById('tipoCadastrante');
	
	switch (parseInt(objSelecionado.value))
	{
		//Pesquisa pessoa
		case 1:
			document.getElementById('divCadastrante').style.display = '';
			document.getElementById('divLotaCadastrante').style.display = 'none';
			break;
		//Pesquisa órgão
		case 2:
			document.getElementById('divCadastrante').style.display = 'none';
			document.getElementById('divLotaCadastrante').style.display = '';
			break;
	}
}

function alteraDestinatarioDocumento()
{
	//Objeto selecionado
	var objSelecionado = document.getElementById('tipoDestinatario');
	
	switch (parseInt(objSelecionado.value))
	{
		//Pesquisa Matrícula
		case 1:
			document.getElementById('divDestinatario').style.display = '';
			document.getElementById('divLotaDestinatario').style.display = 'none';
			document.getElementById('divOrgaoExternoDestinatario').style.display = 'none';
			document.getElementById('divNmDestinatario').style.display = 'none';
			break;
		//Pesquisa Órgão Integrado
		case 2:
			document.getElementById('divDestinatario').style.display = 'none';
			document.getElementById('divLotaDestinatario').style.display = '';
			document.getElementById('divOrgaoExternoDestinatario').style.display = 'none';
			document.getElementById('divNmDestinatario').style.display = 'none';
			break;
		//Pesquisa Órgão Externo	
		case 3:
			document.getElementById('divDestinatario').style.display = 'none';
			document.getElementById('divLotaDestinatario').style.display = 'none';
			document.getElementById('divOrgaoExternoDestinatario').style.display = '';
			document.getElementById('divNmDestinatario').style.display = 'none';
			break;
		//Pesquisa Campo Livre	
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
	
	//Campo Atendente
	var ultMovTipoResp = document.getElementById('ultMovTipoResp');
	
	switch (parseInt(ultMovTipoResp.value))
	{
		//Pesquisa pessoa
		case 1:
			document.getElementById('ultMovLotaRespSel_id').value = '';
			document.getElementById('ultMovLotaRespSel_descricao').value = '';
			document.getElementById('ultMovLotaRespSel_buscar').value = '';
			document.getElementById('ultMovLotaRespSel_sigla').value = '';
			document.getElementById('ultMovLotaRespSelSpan').innerHTML = '';
			break;
		//Pesquisa órgão
		case 2:
			document.getElementById('ultMovRespSel_id').value = '';
			document.getElementById('ultMovRespSel_descricao').value = '';
			document.getElementById('ultMovRespSel_buscar').value = '';
			document.getElementById('ultMovRespSel_sigla').value = '';
			document.getElementById('ultMovRespSelSpan').innerHTML = '';
			break;
	}
	
	//Campo Cadastrante Documento
	var tipoCadastrante = document.getElementById('tipoCadastrante');
	
	switch (parseInt(tipoCadastrante.value))
	{
		//Pesquisa pessoa
		case 1:
			document.getElementById('lotaCadastranteSel_id').value = '';
			document.getElementById('lotaCadastranteSel_descricao').value = '';
			document.getElementById('lotaCadastranteSel_buscar').value = '';
			document.getElementById('lotaCadastranteSel_sigla').value = '';
			document.getElementById('lotaCadastranteSelSpan').innerHTML = '';
			break;
		//Pesquisa órgão
		case 2:
			document.getElementById('cadastranteSel_id').value = '';
			document.getElementById('cadastranteSel_descricao').value = '';
			document.getElementById('cadastranteSel_buscar').value = '';
			document.getElementById('cadastranteSel_sigla').value = '';
			document.getElementById('cadastranteSelSpan').innerHTML = '';
			break;
	}
	
	//Campo Destinatario Documento
	var tipoDestinatario = document.getElementById('tipoDestinatario');
	
	switch (parseInt(tipoDestinatario.value))
	{      
		//Pesquisa Matrícula
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
		//Pesquisa Órgão Integrado
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
		//Pesquisa Órgão Externo	
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
		//Pesquisa Campo Livre	
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

	
	//Campo Origem
	var listar_idTpDoc = document.getElementById('idTpDoc');
	
	switch (parseInt(listar_idTpDoc.value))
	{
		//Todos
		case 0:
			//Campo 'Nº Original do Documento'
			document.getElementById('numExtDoc').value = '';
			
			//Campo 'Órgão Externo'
			document.getElementById('cpOrgaoSel_id').value = '';
			document.getElementById('cpOrgaoSel_descricao').value = '';
			document.getElementById('cpOrgaoSel_buscar').value = '';
			document.getElementById('cpOrgaoSel_sigla').value = '';
			document.getElementById('cpOrgaoSelSpan').innerHTML = '';
			
			//Campo 'Nº do Documento no Sistema Antigo'
			document.getElementById('numAntigoDoc').value = '';
			
			break;	
		//Interno Produzido
		case 1:
			//Campo 'Nº Original do Documento'
			document.getElementById('numExtDoc').value = '';
			
			//Campo 'Órgão Externo'
			document.getElementById('cpOrgaoSel_id').value = '';
			document.getElementById('cpOrgaoSel_descricao').value = '';
			document.getElementById('cpOrgaoSel_buscar').value = '';
			document.getElementById('cpOrgaoSel_sigla').value = '';
			document.getElementById('cpOrgaoSelSpan').innerHTML = '';
			
			//Campo 'Nº do Documento no Sistema Antigo'
			document.getElementById('numAntigoDoc').value = '';
			
			break;
		//Interno Importado
		case 2:
			//Campo 'Órgão Externo'
			document.getElementById('cpOrgaoSel_id').value = '';
			document.getElementById('cpOrgaoSel_descricao').value = '';
			document.getElementById('cpOrgaoSel_buscar').value = '';
			document.getElementById('cpOrgaoSel_sigla').value = '';
			document.getElementById('cpOrgaoSelSpan').innerHTML = '';
			
			break;
		//Externo
		case 3:
			//Campo 'Tipo', Tipo Externo
			document.getElementById('idFormaDoc').value = '5';

			break;
	}
	
	//Verifica a quantidade de campos que estão vazios
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

<siga:pagina titulo="Lista de Expedientes" popup="${param.popup}">
	<div class="gt-bd clearfix">
		<div class="gt-content clearfix">
			<h2>Pesquisa de Documentos</h2>

			<c:if
				test="${((empty param.primeiraVez) or (param.primeiraVez != 'sim')) and ((empty apenasRefresh) or (apenasRefresh != 1))}">

				<c:if test="${not empty tamanho and tamanho > 0}">

					<div class="gt-content-box gt-for-table">
						<table class="gt-table">
							<thead>
								<tr>
									<th rowspan="3" align="right">Número</th>
									<th colspan="3" align="center">Documento</th>
									<th colspan="4" align="center">Situação</th>
									<th rowspan="3">Tipo</th>
									<th rowspan="3">Modelo</th>
									<th rowspan="3">Descrição</th>
									<c:if test="${visualizacao == 1}"> 
										<th rowspan="3">Última Anotação</th>
									</c:if>
								</tr>
								<tr>
									<th rowspan="2" align="center">Data</th>
									<th colspan="2" align="center">Subscritor</th>
									<th rowspan="2" align="center">Data</th>
									<th colspan="2" align="center">Atendente</th>
									<th rowspan="2" align="center">Situação</th>
								</tr>
								<tr>
									<th align="center">Lotação</th>
									<th align="center">Pessoa</th>
									<th align="center">Lotação</th>
									<th align="center">Pessoa</th>
								</tr>
							</thead>

							<siga:paginador maxItens="${itemPagina}" maxIndices="10"
								totalItens="${tamanho}" itens="${itens}" var="documento">

								<c:choose>
									<c:when test="${documento[0].eletronico}">
										<c:set var="exibedoc" value="even" />
									</c:when>
									<c:otherwise>
										<c:set var="exibedoc" value="fisicoeven" />
									</c:otherwise>
								</c:choose>

								<tr class="${exibedoc}">
									<c:set var="podeAcessar"
										value="${f:testaCompetencia('acessarDocumento',titular,lotaTitular, documento[1])}" />
									<td width="11.5%" align="right"><c:choose>
											<c:when test='${param.popup!="true"}'>
												<c:choose>
													<c:when test="${podeAcessar eq true}">
														<ww:url id="url" action="exibir"
															namespace="/expediente/doc">
															<ww:param name="sigla">${documento[1].sigla}</ww:param>
														</ww:url>
														<ww:a href="%{url}">${documento[1].codigo}</ww:a>
													</c:when>
													<c:otherwise> 
							${documento[1].codigo}
						</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
												<a
													href="javascript:opener.retorna_${param.propriedade}('${documento[1].id}','${documento[1].sigla}','${f:selDescricaoConfidencial(documento[1], lotaTitular, titular)}');">${documento[1].codigo}</a>
											</c:otherwise>
										</c:choose>
									</td>
									<c:if test="${documento[1].numSequencia != 0}">
										<td width="5%" align="center">${documento[0].dtDocDDMMYY}</td>
										<td width="4%" align="center"><siga:selecionado
												sigla="${documento[0].lotaSubscritor.sigla}"
												descricao="${documento[0].lotaSubscritor.descricao}" 
												lotacaoParam="${documento[0].lotaSubscritor.orgaoUsuario.siglaOrgaoUsu}${documento[0].lotaSubscritor.sigla}" />
										</td>
										<td width="4%" align="center"><siga:selecionado
												sigla="${documento[0].subscritor.iniciais}"
												descricao="${documento[0].subscritor.descricao}"
												pessoaParam="${documento[0].subscritor.sigla}" />
										</td>

										<td width="5%" align="center">${documento[2].dtIniMarcaDDMMYYYY}</td>
										<td width="4%" align="center"><siga:selecionado
												sigla="${documento[2].dpLotacaoIni.lotacaoAtual.sigla}"
												descricao="${documento[2].dpLotacaoIni.lotacaoAtual.descricao}"
												lotacaoParam="${documento[2].dpLotacaoIni.orgaoUsuario.siglaOrgaoUsu}${documento[2].dpLotacaoIni.sigla}" />
										</td>
										<td width="4%" align="center"><siga:selecionado
												sigla="${documento[2].dpPessoaIni.iniciais}"
												descricao="${documento[2].dpPessoaIni.descricao}"
												pessoaParam="${documento[2].dpPessoaIni.sigla}" />
										</td>

										<td width="10.5%" align="center">${documento[2].cpMarcador.descrMarcador}</td>

									</c:if>
									<c:if test="${documento[1].numSequencia == 0}">
										<td width="5%" align="center">${documento[0].dtDocDDMMYY}</td>
										<td width="4%" align="center"><siga:selecionado
												sigla="${documento[0].lotaSubscritor.sigla}"
												descricao="${documento[0].lotaSubscritor.descricao}"
												lotacaoParam="${documento[0].lotaSubscritor.orgaoUsuario.siglaOrgao}${documento[0].lotaSubscritor.sigla}" />
										</td>
										<td width="4%" align="center"><siga:selecionado
												sigla="${documento[0].subscritor.iniciais}"
												descricao="${documento[0].subscritor.descricao}"
												pessoaParam="${documento[0].subscritor.sigla}" />
										</td>
										<td width="5%" align="center">tag1</td>
										<td width="4%" align="center"></td>
										<td width="4%" align="center"></td>
										<td width="10.5%" align="center">tag4</td>
									</c:if>

									<td width="6%">${documento[0].descrFormaDoc}</td>
									<td width="6%">${documento[0].nmMod}</td>

									<c:set var="acessivel" value="" />
									<c:set var="acessivel"
										value="${f:testaCompetencia('acessarDocumento',titular,lotaTitular,documento[1])}" />

									<c:choose>
										<c:when test="${acessivel eq true}">
											<c:set var="estilo" value="" />
											<c:if
												test="${f:mostraDescricaoConfidencial(documento[0], titular, lotaTitular) eq true}">
												<c:set var="estilo" value="confidencial" />
											</c:if>
											<td class="${estilo}" width="38%">${f:descricaoSePuderAcessar(documento[0], titular,
												lotaTitular)}</td>
											<c:if test="${visualizacao == 1}"> 
												<td class="${estilo}" width="38%">${f:anotacaoConfidencial(documento[1], titular,
													lotaTitular)}</td>
											</c:if>
										</c:when>
										<c:otherwise>
											<td>[Descrição Inacessível]</td>
											<c:if test="${visualizacao == 1}"> 
												<td>[Anotação Inacessível]</td>
											</c:if>
										</c:otherwise>
									</c:choose>
								</tr>
							</siga:paginador>
							
							<!-- </table></div> -->
							<br />
							</c:if>
							<c:if test="${empty tamanho or tamanho == 0}">
								<p class="gt-notice-box">A pesquisa não retornou resultados.</p>
							</c:if>
							</c:if>















							<div class="gt-content-box gt-for-table">

								<form id="listar" name="listar"
									onsubmit="javascript: return limpaCampos()"
									action="/sigaex/expediente/doc/listar.action" method="GET"
									class="form100">
									<table class="gt-form-table">
										<colgroup>
											<col style="width: 10em;" />
											<col />
										</colgroup>
										<input type="hidden" name="popup" value="${param.popup}" />
										<input type="hidden" name="propriedade"
											value="${param.propriedade}" />
										<ww:hidden name="postback" value="1" />
										<ww:hidden name="apenasRefresh" value="0" />
										<ww:hidden name="p.offset" value="0" />

										<tr class="header">
											<td align="center" valign="top" colspan="4">Dados do
												Documento</td>
										</tr>
										<tr>
											<td>Situação:</td>
											<td><ww:select label="Situação" name="ultMovIdEstadoDoc"
													list="estados" listKey="idMarcador"
													listValue="descrMarcador" headerKey="0"
													headerValue="[Todos]" theme="simple" />
												<span style="float: right; padding-left: 2em;">Ordenação:
													<ww:select name="ordem"
														list="#{0:'Data do documento', 1:'Data da situação', 2:'Ano e número', 3:'Data de finalização', 4:'Data de criação do temporário'}"
														theme="simple" /> </span> <span
												style="float: right; padding-left: 2em;">Visualização: <ww:select
														name="visualizacao" onchange="javascript:sbmt();"
														list="#{0:'Normal', 1:'Última anotação'}" theme="simple" />
											</span></td>
										</tr>
										<tr>
											<td>Pessoa/Lotação:</td>
											<td><ww:select name="ultMovTipoResp"
													list="listaTipoResp" theme="simple"
													onchange="javascript:alteraAtendente();" /> <ww:if
													test="ultMovTipoResp == 1">
													<span id="divUltMovResp" style="display: "><siga:selecao
															propriedade="ultMovResp" tema="simple"
															paramList="buscarFechadas=true" modulo="siga"/> </span>
													<span id="divUltMovLotaResp" style="display: none"><siga:selecao
															propriedade="ultMovLotaResp" tema="simple"
															paramList="buscarFechadas=true" modulo="siga"/> </span>
												</ww:if> <ww:elseif test="ultMovTipoResp == 2">
													<span id="divUltMovResp" style="display: none"><siga:selecao
															propriedade="ultMovResp" tema="simple"
															paramList="buscarFechadas=true" modulo="siga"/> </span>
													<span id="divUltMovLotaResp" style="display: "><siga:selecao
															propriedade="ultMovLotaResp" tema="simple"
															paramList="buscarFechadas=true" modulo="siga"/> </span>
												</ww:elseif>
											</td>
										</tr>
										<tr>
											<td>Órgão:</td>
											<td><ww:select name="orgaoUsu" list="orgaosUsu"
													listKey="idOrgaoUsu" listValue="nmOrgaoUsu" theme="simple"
													headerKey="0" headerValue="[Todos]" />
											</td>
										</tr>
										<tr>
											<td>Origem:</td>
											<td><ww:select name="idTpDoc" label="" headerKey="0"
													headerValue="[Todos]" list="tiposDocumento"
													listKey="idTpDoc" listValue="descrTipoDocumento"
													onchange="javascript:alteraOrigem();" theme="simple" />
												&nbsp;&nbsp;&nbsp;&nbsp;Data Inicial: <ww:textfield label=""
													name="dtDocString"
													onblur="javascript:verifica_data(this,0);" theme="simple" />
												&nbsp;&nbsp;Data Final: <ww:textfield label=""
													name="dtDocFinalString"
													onblur="javascript:verifica_data(this,0);" theme="simple" />
											</td>
										</tr>

										<c:choose>
											<c:when test="${tipoDocumento != 'externo'}">
												<tr id="trTipo" style="display: ">
											</c:when>
											<c:otherwise>
												<tr id="trTipo" style="display: none">
											</c:otherwise>
										</c:choose>

										<td>Tipo:</td>
										<td><ww:select name="idTipoFormaDoc" list="tiposFormaDoc"
												listKey="idTipoFormaDoc" listValue="descTipoFormaDoc"
												theme="simple" headerKey="0" headerValue="[Todos]"
												onchange="javascript:alteraTipoDaForma();" id="tipoForma" />&nbsp;&nbsp;&nbsp;
											<div style="display: inline" id="comboFormaDiv">
												<script type="text/javascript">alteraTipoDaForma();</script>
											</div></td>
										</tr>

										<!-- Esse timeout no modelo está estranho. Está sendo necessário porque primeiro
				 precisa ser executado o request ajax referente à FormaDocumento, da qual a lista 
				 de modelos depende. Talvez seria bom tornar síncronos esses dois requests ajax -->
										<tr>
											<td>Modelo:</td>
											<td>
												<div style="display: inline" id="comboModeloDiv">
													<script type="text/javascript">setTimeout("alteraForma()",2000);</script>
												</div></td>
										</tr>

										<tr>
											<td>Ano de Emissão:</td>
											<td><ww:select label="" name="anoEmissaoString"
													list="listaAnos" headerKey="" headerValue="[Todos]"
													theme="simple" /> &nbsp;&nbsp;&nbsp;&nbsp;Número: <ww:textfield
													size="7" label="" name="numExpediente" maxlength="6"
													theme="simple" /> <%--
			&nbsp;&nbsp;&nbsp;&nbsp;Via: <ww:select list="listaVias" label="Via"
				name="numVia" headerKey="0" headerValue="[Todas]" theme="simple" />--%>
											</td>
										</tr>

										<c:choose>
											<c:when
												test='${tipoDocumento == "externo" || tipoDocumento == "antigo"}'>
												<tr id="trNumOrigDoc" style="display: ">
											</c:when>
											<c:otherwise>
												<tr id="trNumOrigDoc" style="display: none">
											</c:otherwise>
										</c:choose>

										<td class="tdLabel"><label for="numExtDoc" class="label">Nº
												Original do Documento:</label>
										</td>
										<td><input type="text" name="numExtDoc" size="16"
											value="" id="numExtDoc" />
										</td>
										</tr>

										<c:choose>
											<c:when test='${tipoDocumento == "externo"}'>
												<tr id="trOrgExterno" style="display: ">
											</c:when>
											<c:otherwise>
												<tr id="trOrgExterno" style="display: none">
											</c:otherwise>
										</c:choose>

										<td>Órgão Externo:</td>
										<td><siga:selecao propriedade="cpOrgao" modulo="siga"
												titulo="Órgão Externo" tema="simple" />
										</td>
										</tr>

										<c:choose>
											<c:when
												test='${tipoDocumento == "externo" || tipoDocumento == "antigo"}'>
												<tr id="trNumDocSistAntigo" style="display: ">
											</c:when>
											<c:otherwise>
												<tr id="trNumDocSistAntigo" style="display: none">
											</c:otherwise>
										</c:choose>

										<td class="tdLabel"><label for="numAntigoDoc"
											class="label">Nº do Documento no Sistema Antigo:</label>
										</td>
										<td><input type="text" name="numAntigoDoc" size="16"
											value="" id="numAntigoDoc" />
										</td>
										</tr>

										<c:choose>
											<c:when test="${tipoDocumento == 'externo'}">
												<ww:textfield label="Subscritor" name="nmSubscritorExt"
													size="80" />
											</c:when>
											<c:otherwise>
												<siga:selecao titulo="Subscritor:" propriedade="subscritor"
													paramList="buscarFechadas=true" modulo="siga"/>
											</c:otherwise>
										</c:choose>

										<tr>
											<td>Cadastrante:</td>
											<td>
												<div style="float: left">
													<ww:select name="tipoCadastrante" list="listaTipoResp"
														theme="simple"
														onchange="javascript:alteraCadastranteDocumento();" />
												</div> <ww:if test="tipoCadastrante == 1">
													<div id="divCadastrante" style="display: ">
														<siga:selecao propriedade="cadastrante" tema="simple"
															paramList="buscarFechadas=true" modulo="siga"/>
													</div>
													<div id="divLotaCadastrante" style="display: none">
														<siga:selecao propriedade="lotaCadastrante" tema="simple"
															paramList="buscarFechadas=true" modulo="siga" />
													</div>
												</ww:if> <ww:elseif test="tipoCadastrante == 2">
													<div id="divCadastrante" style="display: none">
														<siga:selecao propriedade="cadastrante" tema="simple"
															paramList="buscarFechadas=true" modulo="siga"/>
													</div>
													<div id="divLotaCadastrante" style="display: ">
														<siga:selecao propriedade="lotaCadastrante" tema="simple"
															paramList="buscarFechadas=true" modulo="siga"/>
													</div>
												</ww:elseif>
											</td>
										</tr>

										<tr>
											<td>Destinatário:</td>
											<td>
												<div style="float: left">
													<ww:select label="Destinatário" name="tipoDestinatario"
														list="listaTipoDest" theme="simple"
														onchange="javascript:alteraDestinatarioDocumento();" />
												</div> <c:choose>
													<c:when test='${tipoDestinatario == 1}'>
														<div id="divDestinatario" style="display: ">
															<siga:selecao propriedade="destinatario" tema="simple"
																paramList="buscarFechadas=true" modulo="siga"/>
														</div>
														<div id="divLotaDestinatario" style="display: none">
															<siga:selecao propriedade="lotacaoDestinatario"
																tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
														</div>
														<div id="divOrgaoExternoDestinatario"
															style="display: none">
															<siga:selecao propriedade="orgaoExternoDestinatario"
																tema="simple" modulo="siga"/>
														</div>
														<div id="divNmDestinatario" style="display: none">
															<ww:textfield name="nmDestinatario" size="80"
																theme="simple" />
														</div>

														<%--<siga:selecao propriedade="destinatario" tema="simple" />--%>
													</c:when>
													<c:when test='${tipoDestinatario == 2}'>
														<div id="divDestinatario" style="display: none">
															<siga:selecao propriedade="destinatario" tema="simple"
																paramList="buscarFechadas=true" modulo="siga"/>
														</div>
														<div id="divLotaDestinatario" style="display: ">
															<siga:selecao propriedade="lotacaoDestinatario"
																tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
														</div>
														<div id="divOrgaoExternoDestinatario"
															style="display: none">
															<siga:selecao propriedade="orgaoExternoDestinatario"
																tema="simple" modulo="siga"/>
														</div>
														<div id="divNmDestinatario" style="display: none">
															<ww:textfield name="nmDestinatario" size="80"
																theme="simple" />
														</div>


														<%--<siga:selecao propriedade="lotacaoDestinatario" tema="simple" />--%>
													</c:when>
													<c:when test='${tipoDestinatario == 3}'>
														<div id="divDestinatario" style="display: none">
															<siga:selecao propriedade="destinatario" tema="simple"
																paramList="buscarFechadas=true" modulo="siga"/>
														</div>
														<div id="divLotaDestinatario" style="display: none">
															<siga:selecao propriedade="lotacaoDestinatario"
																tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
														</div>
														<div id="divOrgaoExternoDestinatario" style="display: ">
															<siga:selecao propriedade="orgaoExternoDestinatario"
																tema="simple" modulo="siga"/>
														</div>
														<div id="divNmDestinatario" style="display: none">
															<ww:textfield name="nmDestinatario" size="80"
																theme="simple" />
														</div>


														<%--<siga:selecao propriedade="orgaoExternoDestinatario" tema="simple" />--%>
													</c:when>
													<c:otherwise>
														<div id="divDestinatario" style="display: none">
															<siga:selecao propriedade="destinatario" tema="simple"
																paramList="buscarFechadas=true" modulo="siga"/>
														</div>
														<div id="divLotaDestinatario" style="display: none">
															<siga:selecao propriedade="lotacaoDestinatario"
																tema="simple" paramList="buscarFechadas=true" modulo="siga"/>
														</div>
														<div id="divOrgaoExternoDestinatario"
															style="display: none">
															<siga:selecao propriedade="orgaoExternoDestinatario"
																tema="simple" modulo="siga"/>
														</div>
														<div id="divNmDestinatario" style="display: ">
															<ww:textfield name="nmDestinatario" size="80"
																theme="simple" />
														</div>


														<%--<ww:textfield name="nmDestinatario" size="80" theme="simple" />--%>
													</c:otherwise>
												</c:choose>
											</td>
										</tr>

										<siga:selecao titulo="Classificação:"
											propriedade="classificacao" modulo="sigaex"/>

										<ww:textfield label="Descrição" name="descrDocumento"
											size="80" />

										${f:obterExtensaoBuscaTextual(lotaTitular.orgaoUsuario,
										fullText)}


										<tr>
											<td colspan="2"><siga:monobotao inputType="submit"
													value="Buscar" cssClass="gt-btn-medium gt-btn-left" /> <%--<ww:submit name="pesquisar" value="Pesquisar" theme="simple" />--%>
											</td>
										</tr>
									</table>
								</form>
							</div>
							</div>
							</div>
							</siga:pagina>