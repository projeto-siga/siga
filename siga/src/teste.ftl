[#macro entrevista]
	[#if gerar_entrevista!false || gerar_formulario!false]
		[#nested]
	[/#if]
[/#macro]

[#macro documento formato="A4" orientacao="retrato" margemEsquerda="3cm" margemDireita="2cm" margemSuperior="1cm" margemInferior="2cm"]
	[#if !gerar_entrevista!false || gerar_finalizacao!false || gerar_assinatura!false]
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
			<head>
				<style type="text/css">
					@page {
						margin-left: ${margemEsquerda};
						margin-right: ${margemDireita};
						margin-top: ${margemSuperior};
						margin-bottom: ${margemInferior};
					}
				</style>
			</head>
			<body>
				[#nested]
			</body>
		</html>
	[/#if]
[/#macro]

[#macro finalizacao]
	[#if gerar_finalizacao!false]
		[#nested]
	[/#if]
[/#macro]

[#macro assinatura]
	[#if gerar_assinatura!false]
		[#nested]
	[/#if]
[/#macro]

[#macro grupo titulo="" largura=0 depende=""]
	[#local id = (depende=="")?string("", "div" + depende)] 
	[@div id=id depende=depende suprimirIndependente=true]
		[#if largura != 0]
			[#if !grupoLarguraTotal??]
				[#assign grupoLarguraTotal = 0/]
		<table width="100%">
		<tr>
			[/#if]
			[#assign grupoLarguraTotal = grupoLarguraTotal + largura/]
		<td width="${largura}%" valign="top">
		[/#if]
		<table class="entrevista" width="100%">
			[#if titulo != ""]
				<tr class="header">
					<td>${titulo}</td>
				</tr>
			[/#if]
			<tr>
				<td>[#nested]</td>
			</tr>
		</table>
		[#if largura != 0]
			</td>
			[#if grupoLarguraTotal ]= 100>
				</td>
				</table>
				[#assign grupoLarguraTotal = 0/]
			[/#if]
		[/#if]
	[/@div]
[/#macro]

[#macro div id="" depende="" suprimirIndependente=false]
	[#if suprimirIndependente || depende != ""]
		<div[#if id != ""] id="${id}"[/#if][#if depende != ""] depende=";${depende};"[/#if]>[#if id != ""]<!--ajax:${id}-->[/#if][#nested][#if id != ""]<!--/ajax:${id}-->[/#if]</div>
	[#else]
	[#nested]
	[/#if]
[/#macro]

[#macro editor var titulo semBotaoSalvar=false]
	[#if root[var]??]
		[#local v = root[var]/]
	[#else]
		[#local v = ""/]
	[/#if]
	[#if v != ""]
		[#local v = exbl.canonicalizarHtml(v, false, true, false, true)/]
	[#else]
		[#local v = '<p style="TEXT-INDENT: 2cm" align="justify"] </p>'/>
	[/#if]
	<div>
		[#if titulo != ""]
			<b>${titulo}</b>
		[/#if]
		[#if !formulario!false]
			<input type="hidden" name="vars" value="${var}" />
			<script type="text/javascript">FCKeditorAPI = null;__FCKeditorNS = null;</script>	
			<table class="entrevista" width="100%">
				<tr>
					<td></td>
					<td colspan="3">
						<input type="hidden" id="${var}" name="${var}" value="${v?html}">
						<input type="hidden" id="${var}___Config" value="Default[#if semBotaoSalvar]SemSave[/#if]">
						<iframe id="${var}___Frame" src="/fckeditor/editor/fckeditor.html?InstanceName=${var}" width="100%" height="300" frameborder="no" scrolling="no" FCK="true"></iframe>
					</td>
				</tr>
			</table>
		[#else]
			<br>${v}<br><br>
		[/#if]
	</div>
[/#macro]

[#macro selecao var titulo opcoes reler=false idAjax="" onclick=""]
	[#local l=opcoes?split(";")]
	[#if root[var]??]
		[#local v = root[var]/]
	[#else]
		[#local v = l?first/]
	[/#if]
	
	${titulo}:
	[#if !gerar_formulario!false]
		<input type="hidden" name="vars" value="${var}" />
		<select name="${var}" [#if reler] onchange="javascript: sbmt([#if idAjax != ""]'${idAjax}'[/#if]);"[/#if] onclick="${onclick}">
			[#list l as opcao]
				<option[#if v == opcao] selected[/#if] value="${opcao}">${opcao}</option>
			[/#list]
		</select>
	[#else]
		<span class="valor">${v}</span>
	[/#if]
[/#macro]

[#macro letra tamanho]
	[#local body][#nested/][/#local]
	<span style="font-size:${tamanho}">
		${func.fixFontSize(body,tamanho)}
	</span>
[/#macro]

[#macro primeiroCabecalho]
	<!-- INICIO PRIMEIRO CABECALHO
		[#nested/]
	FIM PRIMEIRO CABECALHO -->
[/#macro]

[#macro cabecalho]
	<!-- INICIO CABECALHO
		[#nested/]
	FIM CABECALHO -->
[/#macro]

[#macro primeiroRodape]
	<!-- INICIO PRIMEIRO RODAPE
		[#nested/]
	FIM PRIMEIRO RODAPE -->
[/#macro]

[#macro rodape]
	<!-- INICIO RODAPE
		[#nested/]
	FIM RODAPE -->
[/#macro]

[#macro cabecalhoEsquerdaPrimeiraPagina]
<table width="100%" align="left" border="0">
	<tr>
		<td align="left" valign="bottom" width="15%"><img src="contextpath/imagens/brasao2.png" width="65" height="65" /></td>
		<td align="left" width="1%"></td>
		<td width="84%">
		<table align="left" width="100%">
			<tr>
				<td width="100%" align="left">
				<p style="font-family: AvantGarde Bk BT, Arial; font-size: 11pt;">PODER JUDICIÁRIO</p>
				</td>
			</tr>
			<tr>
				<td width="100%" align="left">
				<p style="font-family: Arial; font-size: 10pt; font-weight: bold;">JUSTIÇA FEDERAL</p>
				</td>
			</tr>
			<tr>
				<td width="100%" align="left">
				<p style="font-family: AvantGarde Bk BT, Arial; font-size: 8pt;">
				[#if mov??]
					${mov.lotaTitular.orgaoUsuario.descricaoMaiusculas}
				[#else]
					${doc.lotaTitular.orgaoUsuario.descricaoMaiusculas}
				[/#if]</p>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
[/#macro]

[#macro cabecalhoEsquerda]
<table width="100%" border="0" bgcolor="#FFFFFF">
	<tr bgcolor="#FFFFFF">
		<td width="100%">
		<table width="100%">
			<tr>
				<td width="100%" align="left">
				<p style="font-family: AvantGarde Bk BT, Arial; font-size: 11pt;">PODER	JUDICIÁRIO</p>
				</td>
			</tr>
			<tr>
				<td width="100%" align="left">
				<p style="font-family: Arial; font-size: 10pt; font-weight: bold;">JUSTIÇA FEDERAL</p>
				</td>
			</tr>
			<tr>
				<td width="100%" align="left">
				<p style="font-family: AvantGarde Bk BT, Arial; font-size: 8pt;">
				[#if mov??]
					${mov.lotaTitular.orgaoUsuario.descricaoMaiusculas}
				[#else]
					${doc.lotaTitular.orgaoUsuario.descricaoMaiusculas}
				[/#if]<br />
				 </p>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
[/#macro]

[#macro rodapeClassificacaoDocumental]
<table align="left" width="100%" bgcolor="#FFFFFF">
	<tr>
		<td width="70%"></td>
		<td width="30%">
		<table align="right" width="100%" border="1" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td align="center" width="60%"
					style="font-family:Arial;font-size:8pt;text-decoration:italic;"
					bgcolor="#FFFFFF">Classif. documental</td>
				<td align="center" width="40%"
					style="font-family:Arial;font-size:8pt;" bgcolor="#FFFFFF">${(doc.exClassificacao.sigla)!}</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
[/#macro]

[#macro rodapeNumeracaoADireita]
<table width="100%" border="0" cellpadding="0" bgcolor="#FFFFFF">
	<tr>
		<td width="100%" align="right">#pg</td>
	</tr>
</table>
[/#macro]

[#macro assinaturaCentro]
<p style="font-family: Arial; font-size: 11pt; font-weight: bold;" align="center">
	[#if (doc.nmSubscritor)??]
		${doc.nmSubscritor}
	[#else]
		${(doc.subscritor.descricao)!}
	[/#if]
	[#if !apenasNome??]	
		<br />
		[#if apenasCargo??]
				${(doc.subscritor.cargo.nomeCargo)!}
		[#else]
			[#if (doc.nmFuncao)??]
				${doc.nmFuncao}
			[#elseif (doc.titular.funcaoConfianca.nomeFuncao)??]
				${doc.titular.funcaoConfianca.nomeFuncao}
				[#if (doc.titular.idPessoa)! != (doc.subscritor.idPessoa)!] EM EXERCÍCIO [/#if]
			[#elseif (doc.subscritor.funcaoConfianca.nomeFuncao)??]
				${doc.subscritor.funcaoConfianca.nomeFuncao}
			[#else]
				${(doc.subscritor.cargo.nomeCargo)!}
			[/#if]
		[/#if]
		 
		[#if formatarOrgao??]
			<br>
			[#if (doc.nmLotacao)??]
				${doc.nmLotacao}
			[#else]
				${(doc.titular.lotacao.nomeLotacao)!}
			[/#if]
		[/#if]
		
		[#list doc.mobilGeral.exMovimentacaoSet as mov]
			[#if (mov.exTipoMovimentacao.idTpMov)! == 24]
				<br/><br/><br/>
				[#if mov.nmSubscritor??]
					${mov.nmSubscritor}
				[#else]
					${(mov.subscritor.nomePessoa)!}
				[/#if]		
				<br>
				[#if mov.nmFuncao??]
					${mov.nmFuncao}
				[#elseif (mov.titular.funcaoConfianca.nomeFuncao)??]
					${mov.titular.funcaoConfianca.nomeFuncao} 
					[#if substituicao! == true && (doc.titular.idPessoa)! != (doc.subscritor.idPessoa)!] EM EXERCÍCIO [/#if]
				[#elseif (mov.subscritor.funcaoConfianca.nomeFuncao)??]
					${mov.subscritor.funcaoConfianca.nomeFuncao}
				[#else]
					${(mov.subscritor.cargo.nomeCargo)!}
				[/#if]
				[#if formatarOrgao??]
					<br>
					[#if mov.nmLotacao??]
						${mov.nmLotacao}
					[#else]
						${mov.titular.lotacao.nomeLotacao}
					[/#if]
				[/#if]
			[/#if]
		[/#list]
	[/#if]
	[#if textoFinal??]
		<br/>${textoFinal}
	[/#if]
</p>
[/#macro]









[@entrevista]
	[#if !esconderTexto!false]
		[@grupo titulo="Texto a ser inserido no corpo do memorando"]
			[@grupo]
				[@editor titulo="" var="texto_memorando" /]
			[/@grupo]
		[/@grupo]
		[@selecao titulo="Tamanho da letra" var="tamanhoLetra" opcoes="Normal;Pequeno;Grande" /]
	[/#if]
[/@entrevista]

[@documento]
	[#if param.tamanhoLetra! == "Normal"]
		[#assign tl = "11pt" /]
	[#elseif param.tamanhoLetra! == "Pequeno"]
		[#assign tl = "9pt" /]
	[#elseif param.tamanhoLetra! == "Grande"]
		[#assign tl = "13pt" /]
	[#else]		
		[#assign tl = "11pt"]
	[/#if]
		
	[@primeiroCabecalho]
	<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
	[@cabecalhoEsquerdaPrimeiraPagina/]
	</td></tr>
		<tr bgcolor="#FFFFFF">
			<td width="100%">
				<table width="100%">
					<tr>
						<td align="right"><p style="font-family:Arial;font-weight:bold;font-size:11pt;">MEMORANDO N&ordm; ${(doc.codigo)!}</p></td>
					</tr>
					<tr>
						<td align="right">[@letra tamanho="11pt"]<p>${(doc.dtExtenso)!}</p>[/@letra]</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	[/@primeiroCabecalho]

	[@cabecalho]
	[@cabecalhoEsquerda/]
	[/@cabecalho]

	[@letra tamanho=tl]
		<p align="left">DE:
		[#if (doc.nmLotacao)??]
			${doc.nmLotacao}
		[#else]
			${(doc.titular.lotacao.descricao)!}
		[/#if]
		<br>
		PARA: ${(doc.destinatarioString)!}</p>
		<span style="font-size: ${tl}"> ${texto_memorando!} </span>
		<p style="align: justify; TEXT-INDENT: 2cm">Atenciosamente,</p>
		<p>&nbsp;</p>
		[@assinaturaCentro/]
	[/@letra]

	[@primeiroRodape]
	[@rodapeClassificacaoDocumental/]
	[/@primeiroRodape]

	[@rodape]
	[@rodapeNumeracaoADireita/]
	[/@rodape]
[/@documento]
[@finalizacao]
	{Memorando Finalizado!}
	[#assign f = wf.criarWorkflow('Teste de Integração', doc)/]
[/@finalizacao]
[@assinatura]
	{Memorando Assinado!}
	[#assign f = wf.criarWorkflow('Teste de Integração', doc)/]
[/@assinatura]



























































[#macro entrevista]
	[#if gerar_entrevista!false || gerar_formulario!false]
		[#nested]
	[/#if]
[/#macro]

[#macro documento formato="A4" orientacao="retrato" margemEsquerda="3cm" margemDireita="2cm" margemSuperior="1cm" margemInferior="2cm"]
	[#if !gerar_entrevista!false || gerar_finalizacao!false || gerar_assinatura!false]
		<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
			<head>
				<style type="text/css">
					@page {
						margin-left: ${margemEsquerda};
						margin-right: ${margemDireita};
						margin-top: ${margemSuperior};
						margin-bottom: ${margemInferior};
					}
				</style>
			</head>
			<body>
				[#nested]
			</body>
		</html>
	[/#if]
[/#macro]

[#macro finalizacao]
	[#if gerar_finalizacao!false]
		[#nested]
	[/#if]
[/#macro]

[#macro assinatura]
	[#if gerar_assinatura!false]
		[#nested]
	[/#if]
[/#macro]

[#macro grupo titulo="" largura=0 depende=""]
	[#local id = (depende=="")?string("", "div" + depende)] 
	[@div id=id depende=depende suprimirIndependente=true]
		[#if largura != 0]
			[#if !grupoLarguraTotal??]
				[#assign grupoLarguraTotal = 0/]
		<table width="100%">
		<tr>
			[/#if]
			[#assign grupoLarguraTotal = grupoLarguraTotal + largura/]
		<td width="${largura}%" valign="top">
		[/#if]
		<table class="entrevista" width="100%">
			[#if titulo != ""]
				<tr class="header">
					<td>${titulo}</td>
				</tr>
			[/#if]
			<tr>
				<td>[#nested]</td>
			</tr>
		</table>
		[#if largura != 0]
			</td>
			[#if grupoLarguraTotal ]= 100>
				</td>
				</table>
				[#assign grupoLarguraTotal = 0/]
			[/#if]
		[/#if]
	[/@div]
[/#macro]

[#macro div id="" depende="" suprimirIndependente=false]
	[#if suprimirIndependente || depende != ""]
		<div[#if id != ""] id="${id}"[/#if][#if depende != ""] depende=";${depende};"[/#if]>[#if id != ""]<!--ajax:${id}-->[/#if][#nested][#if id != ""]<!--/ajax:${id}-->[/#if]</div>
	[#else]
	[#nested]
	[/#if]
[/#macro]

[#macro texto var titulo="" largura="" maxcaracteres="" idAjax="" reler="" relertab="" obrigatorio="nao" default=""]
    [#if reler == 'ajax']
        [#local jreler = " onchange\"=javascript: sbmt('" + idAjax + "');\""]
    [/#if]

    [#if reler == 'sim']
        [#local jreler = " onchange=\"javascript: sbmt();\""]
    [/#if]

    [#if relertab == 'sim']
        [#local jrelertab = " onblur=\"javascript: sbmt();\""]
    [/#if]

    [#if largura != ""]
        [#local jlargura = " size=\"" + largura + "\""]
    [/#if]

    [#if maxcaracteres != ""]
        [#local jmaxcaracteres = " maxlength=\"" + maxcaracteres + "\""]
    [/#if]

    [#local v = root[var]!default]

    <input type="hidden" name="vars" value="${var}" />

    [#if alerta!"" == 'Sim' && v=""]
	[#list paramValues.obrigatorios as campo]
	     [#if campo == var]
		 [#local vermelho = "color:red"]
             [/#if]
        [/#list]
    [/#if]

    [#if obrigatorio == 'Sim']
	[#local negrito = "font-weight:bold"]
	<input type="hidden" name="obrigatorios" value="${var}" />
    [/#if]

    [#if titulo != ""]
	<span style="${negrito!""};${vermelho!""}">${titulo}:</span>
    [/#if]
    
    [#if !gerar_formulario!false]
	<input type="text" name="${var}" value="${v}" ${jreler!""}${jrelertab!""}${jlargura!""}${jmaxcaracteres!""}/>
    [#else]
	<span class="valor">${v}</span>
    [/#if]
[/#macro]

[#macro editor var titulo semBotaoSalvar=false]
	[#if root[var]??]
		[#local v = root[var]/]
	[#else]
		[#local v = ""/]
	[/#if]
	[#if v != ""]
		[#local v = exbl.canonicalizarHtml(v, false, true, false, true)/]
	[#else]
		[#local v = '<p style="TEXT-INDENT: 2cm" align="justify"] </p>'/>
	[/#if]
	<div>
		[#if titulo != ""]
			<b>${titulo}</b>
		[/#if]
		[#if !formulario!false]
			<input type="hidden" name="vars" value="${var}" />
			<script type="text/javascript">FCKeditorAPI = null;__FCKeditorNS = null;</script>	
			<table class="entrevista" width="100%">
				<tr>
					<td></td>
					<td colspan="3">
						<input type="hidden" id="${var}" name="${var}" value="${v?html}">
						<input type="hidden" id="${var}___Config" value="Default[#if semBotaoSalvar]SemSave[/#if]">
						<iframe id="${var}___Frame" src="/fckeditor/editor/fckeditor.html?InstanceName=${var}" width="100%" height="300" frameborder="no" scrolling="no" FCK="true"></iframe>
					</td>
				</tr>
			</table>
		[#else]
			<br>${v}<br><br>
		[/#if]
	</div>
[/#macro]

[#macro selecao var titulo opcoes reler=false idAjax="" onclick=""]
	[#local l=opcoes?split(";")]
	[#if root[var]??]
		[#local v = root[var]/]
	[#else]
		[#local v = l?first/]
	[/#if]
	
	${titulo}:
	[#if !gerar_formulario!false]
		<input type="hidden" name="vars" value="${var}" />
		<select name="${var}" [#if reler] onchange="javascript: sbmt([#if idAjax != ""]'${idAjax}'[/#if]);"[/#if] onclick="${onclick}">
			[#list l as opcao]
				<option[#if v == opcao] selected[/#if] value="${opcao}">${opcao}</option>
			[/#list]
		</select>
	[#else]
		<span class="valor">${v}</span>
	[/#if]
[/#macro]

[#macro letra tamanho]
	[#local body][#nested/][/#local]
	<span style="font-size:${tamanho}">
		${func.fixFontSize(body,tamanho)}
	</span>
[/#macro]

[#macro primeiroCabecalho]
	<!-- INICIO PRIMEIRO CABECALHO
		[#nested/]
	FIM PRIMEIRO CABECALHO -->
[/#macro]

[#macro cabecalho]
	<!-- INICIO CABECALHO
		[#nested/]
	FIM CABECALHO -->
[/#macro]

[#macro primeiroRodape]
	<!-- INICIO PRIMEIRO RODAPE
		[#nested/]
	FIM PRIMEIRO RODAPE -->
[/#macro]

[#macro rodape]
	<!-- INICIO RODAPE
		[#nested/]
	FIM RODAPE -->
[/#macro]

[#macro cabecalhoCentralizadoPrimeiraPagina]
<table width="100%" align="left" border="0" cellpadding="0"
	cellspacing="0" bgcolor="#FFFFFF">
	<tr bgcolor="#FFFFFF">
		<td width="100%">
		<table width="100%" border="0" cellpadding="2">
			<tr>
				<td width="100%" align="center" valign="bottom"><img src="contextpath/imagens/brasao2.png" width="65" height="65" /></td>
			</tr>
			<tr>
				<td width="100%" align="center">
				<p style="font-family: AvantGarde Bk BT, Arial; font-size: 11pt;">PODER	JUDICIÁRIO</p>
				</td>
			</tr>
			<tr>
				<td width="100%" align="center">
				<p style="font-family: Arial; font-size: 10pt; font-weight: bold;">JUSTIÇA FEDERAL</p>
				</td>
			</tr>
			<tr>
			<tr>
				<td width="100%" align="center">
				<p style="font-family: AvantGarde Bk BT, Arial; font-size: 8pt;">
				[#if mov??]
					${(mov.lotaTitular.orgaoUsuario.descricaoMaiusculas)!}
				[#else]
					${(doc.lotaTitular.orgaoUsuario.descricaoMaiusculas)!}
				[/#if]</p>
				</td>
			</tr>
			</tr>
		</table>
		</td>
	</tr>
</table>
[/#macro]


[#macro cabecalhoCentralizado]
<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
	<tr bgcolor="#FFFFFF">
		<td width="100%">
		<table width="100%" border="0" cellpadding="2">
			<tr>
				<td width="100%" align="center">
				<p style="font-family: AvantGarde Bk BT, Arial; font-size: 11pt;">PODER	JUDICIÁRIO</p>
				</td>
			</tr>
			<tr>
				<td width="100%" align="center">
				<p style="font-family: Arial; font-size: 10pt; font-weight: bold;">JUSTIÇA FEDERAL</p>
				</td>
			</tr>
			<tr>
				<td width="100%" align="center">
				<p style="font-family: AvantGarde Bk BT, Arial; font-size: 8pt;">
				[#if mov??]
					${(mov.lotaTitular.orgaoUsuario.descricaoMaiusculas)!}
				[#else]
					${(doc.lotaTitular.orgaoUsuario.descricaoMaiusculas)!}
				[/#if]</p>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
[/#macro]


[#macro cabecalhoEsquerdaPrimeiraPagina]
<table width="100%" align="left" border="0">
	<tr>
		<td align="left" valign="bottom" width="15%"><img src="contextpath/imagens/brasao2.png" width="65" height="65" /></td>
		<td align="left" width="1%"></td>
		<td width="84%">
		<table align="left" width="100%">
			<tr>
				<td width="100%" align="left">
				<p style="font-family: AvantGarde Bk BT, Arial; font-size: 11pt;">PODER JUDICIÁRIO</p>
				</td>
			</tr>
			<tr>
				<td width="100%" align="left">
				<p style="font-family: Arial; font-size: 10pt; font-weight: bold;">JUSTIÇA FEDERAL</p>
				</td>
			</tr>
			<tr>
				<td width="100%" align="left">
				<p style="font-family: AvantGarde Bk BT, Arial; font-size: 8pt;">
				[#if mov??]
					${(mov.lotaTitular.orgaoUsuario.descricaoMaiusculas)!}
				[#else]
					${(doc.lotaTitular.orgaoUsuario.descricaoMaiusculas)!}
				[/#if]</p>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
[/#macro]

[#macro cabecalhoEsquerda]
<table width="100%" border="0" bgcolor="#FFFFFF">
	<tr bgcolor="#FFFFFF">
		<td width="100%">
		<table width="100%">
			<tr>
				<td width="100%" align="left">
				<p style="font-family: AvantGarde Bk BT, Arial; font-size: 11pt;">PODER	JUDICIÁRIO</p>
				</td>
			</tr>
			<tr>
				<td width="100%" align="left">
				<p style="font-family: Arial; font-size: 10pt; font-weight: bold;">JUSTIÇA FEDERAL</p>
				</td>
			</tr>
			<tr>
				<td width="100%" align="left">
				<p style="font-family: AvantGarde Bk BT, Arial; font-size: 8pt;">
				[#if mov??]
					${(mov.lotaTitular.orgaoUsuario.descricaoMaiusculas)!}
				[#else]
					${(doc.lotaTitular.orgaoUsuario.descricaoMaiusculas)!}
				[/#if]<br />
				 </p>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
[/#macro]

[#macro rodapeClassificacaoDocumental]
<table align="left" width="100%" bgcolor="#FFFFFF">
	<tr>
		<td width="70%"></td>
		<td width="30%">
		<table align="right" width="100%" border="1" cellspacing="1"
			bgcolor="#000000">
			<tr>
				<td align="center" width="60%"
					style="font-family:Arial;font-size:8pt;text-decoration:italic;"
					bgcolor="#FFFFFF">Classif. documental</td>
				<td align="center" width="40%"
					style="font-family:Arial;font-size:8pt;" bgcolor="#FFFFFF">${(doc.exClassificacao.sigla)!}</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
[/#macro]

[#macro rodapeDestinatario]
		<table width="100%" border="0" cellpadding="2" cellspacing="0" bgcolor="#FFFFFF">
			<tr>
				<td align="left">
					<table align="left" width="100%" border="0" cellpadding="2" cellspacing="0">
						<tr><td><mod:letra tamanho="${tl}"><p>${enderecamento_dest}</p></mod:letra></td></tr>
						<tr><td><mod:letra tamanho="${tl}"><p>${nome_dest}</p></mod:letra></td></tr>
						<c:if test="${not empty cargo_dest}">
						<tr><td><mod:letra tamanho="${tl}"><p>${cargo_dest}</p></mod:letra></td></tr>
						</c:if>
						<c:if test="${not empty orgao_dest}">
						<tr><td><mod:letra tamanho="${tl}"><p>${orgao_dest}</p></mod:letra></td></tr>
						</c:if>
						<c:if test="${not empty endereco_dest}">
						<tr><td><mod:letra tamanho="${tl}"><p><siga:fixcrlf>${endereco_dest}</siga:fixcrlf></p></mod:letra></td></tr>
						</c:if>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
				</td>
			</tr>
		</table>
[/#macro]

[#macro rodapeNumeracaoADireita]
<table width="100%" border="0" cellpadding="0" bgcolor="#FFFFFF">
	<tr>
		<td width="100%" align="right">#pg</td>
	</tr>
</table>
[/#macro]

[#macro rodapeNumeracaoCentralizada]
<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
	<tr>
		<td width="100%" align="center">#pg</td>
	</tr>
</table>
[/#macro]


[#macro assinaturaCentro]
<p style="font-family: Arial; font-size: 11pt; font-weight: bold;" align="center">
	[#if (doc.nmSubscritor)??]
		${doc.nmSubscritor}
	[#else]
		${(doc.subscritor.descricao)!}
	[/#if]
	[#if !apenasNome??]	
		<br />
		[#if apenasCargo??]
				${(doc.subscritor.cargo.nomeCargo)!}
		[#else]
			[#if (doc.nmFuncao)??]
				${doc.nmFuncao}
			[#elseif (doc.titular.funcaoConfianca.nomeFuncao)??]
				${doc.titular.funcaoConfianca.nomeFuncao}
				[#if (doc.titular.idPessoa)! != (doc.subscritor.idPessoa)!] EM EXERCÍCIO [/#if]
			[#elseif (doc.subscritor.funcaoConfianca.nomeFuncao)??]
				${doc.subscritor.funcaoConfianca.nomeFuncao}
			[#else]
				${(doc.subscritor.cargo.nomeCargo)!}
			[/#if]
		[/#if]
		 
		[#if formatarOrgao??]
			<br>
			[#if (doc.nmLotacao)??]
				${doc.nmLotacao}
			[#else]
				${(doc.titular.lotacao.nomeLotacao)!}
			[/#if]
		[/#if]
		
                [#if (doc.mobilGeral.exMovimentacaoSet)??]
		[#list doc.mobilGeral.exMovimentacaoSet as mov]
                    [#if (mov.exTipoMovimentacao.idTpMov)! == 24]
                        <br/><br/><br/>
                        [#if mov.nmSubscritor??]
                            ${mov.nmSubscritor}
                        [#else]
                            ${(mov.subscritor.nomePessoa)!}
                        [/#if]		
                        <br>
                        [#if mov.nmFuncao??]
                            ${mov.nmFuncao}
                        [#elseif (mov.titular.funcaoConfianca.nomeFuncao)??]
                            ${mov.titular.funcaoConfianca.nomeFuncao} 
                                [#if substituicao! == true && (doc.titular.idPessoa)! != (doc.subscritor.idPessoa)!] EM EXERCÍCIO [/#if]
                        [#elseif (mov.subscritor.funcaoConfianca.nomeFuncao)??]
                            ${mov.subscritor.funcaoConfianca.nomeFuncao}
                        [#else]
                            ${(mov.subscritor.cargo.nomeCargo)!}
                        [/#if]
                        [#if formatarOrgao??]
                            <br>
                            [#if mov.nmLotacao??]
                                ${mov.nmLotacao}
                            [#else]
                                ${mov.titular.lotacao.nomeLotacao}
                            [/#if]
                        [/#if]
		    [/#if]
		[/#list]
            [/#if]
	[/#if]
	[#if textoFinal??]
		<br/>${textoFinal}
	[/#if]
</p>
[/#macro]

[#macro estiloBrasaoAEsquerda tipo tamanhoLetra="11pt"]
	[@primeiroCabecalho]
	<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
	[@cabecalhoEsquerdaPrimeiraPagina/]
	</td></tr>
		<tr bgcolor="#FFFFFF">
			<td width="100%">
				<table width="100%">
					<tr>
                                                <td align="right"><p style="font-family:Arial;font-weight:bold;font-size:11pt;">${tipo} N&ordm; ${(doc.codigo)!}</p></td>
					</tr>
					<tr>
						<td align="right">[@letra tamanho="11pt"]<p>${(doc.dtExtenso)!}</p>[/@letra]</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	[/@primeiroCabecalho]

	[@cabecalho]
	[@cabecalhoEsquerda/]
	[/@cabecalho]

	[@letra tamanho=tamanhoLetra]
		<p>&nbsp;</p>
                [#nested]
		<p>&nbsp;</p>
		[@assinaturaCentro/]
	[/@letra]

	[@primeiroRodape]
	[@rodapeClassificacaoDocumental/]
	[/@primeiroRodape]

	[@rodape]
	[@rodapeNumeracaoADireita/]
	[/@rodape]
[/#macro]

[#macro estiloBrasaoCentralizado tipo tamanhoLetra="11pt"]
	[@primeiroCabecalho]
	<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF"><tr><td>
	[@cabecalhoCentralizadoPrimeiraPagina/]
	</td></tr>
                <tr bgcolor="#FFFFFF">
			<td width="100%">
				<br/>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td align="left" width="40%"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">${tipo} N&ordm; ${(doc.codigo)!}</p></td>
						<td align="right" width="60%">[@letra tamanho="11pt"]<p>${doc.dtExtenso}</p>[@letra tamanho="11pt"]</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	[/@primeiroCabecalho]

	[@cabecalho]
	[@cabecalhoCentralizado/]
	[/@cabecalho]

	[@letra tamanho=tamanhoLetra]
		<p>&nbsp;</p>
                [#nested]
		<p>&nbsp;</p>
		[@assinaturaCentro/]
	[/@letra]

	[@primeiroRodape]
	[@rodapeClassificacaoDocumental/]
	[/@primeiroRodape]

	[@rodape]
	[@rodapeNumeracaoADireita/]
	[/@rodape]
[/#macro]

[#macro memorando]
[@entrevista]
	[#if !esconderTexto!false]
		[@grupo titulo="Texto a ser inserido no corpo do memorando"]
			[@grupo]
				[@editor titulo="" var="texto_memorando" /]
				[@texto titulo="campo" var="texto1" /]
				[@texto titulo="campo teste CJF" var="texto2" /]
			[/@grupo]
		[/@grupo]
		[@selecao titulo="Tamanho da letra" var="tamanhoLetra" opcoes="Normal;Pequeno;Grande" /]
	[/#if]
[/@entrevista]

[@documento]
	[#if param.tamanhoLetra! == "Normal"]
		[#assign tl = "11pt" /]
	[#elseif param.tamanhoLetra! == "Pequeno"]
		[#assign tl = "9pt" /]
	[#elseif param.tamanhoLetra! == "Grande"]
		[#assign tl = "13pt" /]
	[#else]		
		[#assign tl = "11pt"]
	[/#if]

        [@estiloBrasaoAEsquerda tipo="MEMORANDO" tamanhoLetra=tl]
		<p align="left">DE:
		[#if (doc.nmLotacao)??]
			${doc.nmLotacao}
		[#else]
			${(doc.titular.lotacao.descricao)!}
		[/#if]
		<br>
		PARA: ${(doc.destinatarioString)!}</p>
		<span style="font-size: ${tl}"> ${texto_memorando!} </span>
		<p style="align: justify; TEXT-INDENT: 2cm">Atenciosamente,</p>
        [/@estiloBrasaoAEsquerda]
[/@documento]
[/#macro]

[#macro oficio]
[@entrevista]
	[#if !esconderDest!false]
		[@grupo]
			[@grupo]
                                [@selecao titulo="Tipo de Autoridade" var="tipoAutoridade" opcoes="[Nenhum];Auditor da Justiça Militar;Bispo e Arcebispo;Cardeal;Chefe de Gabinete Civil;Chefe de Gabinete Militar da Presidência da República;Consultor-Geral da República;Corregedor do Tribunal Regional Federal;Dirigente administrativo e Procurador;Embaixador;Governador de Estado e do Distrito Federal;Juiz Federal;Juiz em geral;Membro do Congresso Nacional;Membro do Supremo Tribunal Federal;Membro do Tribunal Superior;Membro do Tribunal de Contas da União;Membro do Tribunal Regional Federal;Membro do Tribunal de Justiça;Membro da Assembléia Legislativa;Ministro de Estado;Monsenhor, Cônego;Prefeito;Presidente da República;Presidente do Supremo Tribunal Federal;Presidente do Tribunal Superior;Presidente do Tribunal Regional Federal;Presidente do Tribunal de Justiça;Presidente da Assembléia Legislativa;Presidente do Tribunal de Contas da União;Procurador-Geral da República;Procurador-Geral junto ao Tribunal;Secretário de Estado do Governo Estadual;Reitor de Universidade;Vice-Presidente da República;Oficial General das Forças Armadas;[Outros]" reler="sim" /]
				[#if f:verificaGenero(requestScope['tipoAutoridade'])=='F']
                                        [@selecao titulo="Gênero da Autoridade" var="genero" opcoes="M;F" reler="sim" /]
                                [#else]
	                                [#assign genero = f:verificaGenero(requestScope['tipoAutoridade']) /]
	                        [/#if]
		                [#assign tratamento = f:tratamento(requestScope['tipoAutoridade'],genero) /]
				[#if tratamento??]
                                        [@grupo]
                                                [@mensagem titulo="Forma de tratamento" texto="${tratamento.formaDeTratamento} (${tratamento.abreviatura})" /]
                                        [/@grupo]
	                      	[/#if]
                        [/@grupo]
			[#if tratamento??]
                                [@grupo]
                                        [@selecao titulo="Especificar manualmente o vocativo" var="especificarVocativo" opcoes="Não/Sim" reler="sim" /]
                                [/@grupo]
                      	[/#if]
			[#if !tratamento?? || especificarVocativo == 'Sim']
                                [@grupo]
                                        [@texto titulo="Vocativo" var="vocativo" largura="45" /]
                                [/@grupo]
                        [#else]
                                [@oculto var="vocativo" valor="${tratamento.vocativo}" /]
                        [/#if]
                [/@grupo]
		[@grupo titulo="Texto a ser inserido no corpo do ofício"]
			[@grupo]
				[@editor titulo="" var="texto_oficio" /]
			[/@grupo]
		[/@grupo]
                [@grupo]
                        [@texto titulo="Fecho (de acordo com o vocativo)" var="fecho" largura="50" /]
                [/@grupo]

			[@grupo titulo="Dados do destinatário"]
                                <!-- Tratamento -->
				[#if tratamento??]
					[@grupo]
                                                [@caixaverif titulo="Especificar manualmente a forma de endereçamento" var="especificarEnderecamento" reler="sim" /]
					[/@grupo]
                                [/#if]
				[#if !tratamento?? || especificarEnderecamento == 'Sim']
					[@grupo]
                                                [@texto titulo="Forma de endereçamento"	var="enderecamento_dest" largura="45" /]
					[/@grupo]
                                [#else]
					[@oculto var="enderecamento_dest" valor="${tratamento.formaDeEnderecamento}" /]
                                [/#if]

				<!-- Nome -->
				[#if (doc.destinatario.descricao)??]
					[@grupo]
                                                [@caixaverif titulo="Especificar manualmente o nome do destinatário" var="especificarNome" reler="sim" /]
					[/@grupo]
                                [/#if]
				[#if !(doc.destinatario.descricao)?? || especificarNome == 'Sim']
					[@grupo]
                                                [@texto titulo="Nome" var="nome_dest" largura="60" /]
					[/@grupo]
                                [#else]
					[@oculto var="nome_dest" valor="${doc.destinatario.descricao}" /]
                                [/#if]

				<!-- Cargo -->
				[#if (doc.destinatario.cargo.nomeCargo)??]
					[@grupo]
                                                [@caixaverif titulo="Especificar manualmente o cargo do destinatário" var="especificarCargo" reler="sim" /]
					[/@grupo]
                                [/#if]
				[#if !(doc.destinatario.cargo.nomeCargo)?? || especificarCargo == 'Sim']
					[@grupo]
                                                [@texto titulo="Cargo" var="cargo_dest" largura="60" /]
					[/@grupo]
                                [#else]
					[@oculto var="cargo_dest" valor="${doc.destinatario.cargo.nomeCargo}" /]
                                [/#if]				<c:if test="${not empty doc.destinatario.cargo.nomeCargo}">

				<!-- Órgão -->
				[#if (doc.lotaDestinatario.descricao)??]
        		                [#assign orgaoAux = doc.lotaDestinatario.descricao /]
                                [#else]
        				[#if (orgaoExternoDestinatarioSel.descricao)??]
                		                [#assign orgaoAux = orgaoExternoDestinatarioSel.descricao /]
                                        [/#if]
                                [/#if]


				[#if orgaoAux??]
					[@grupo]
                                                [@caixaverif titulo="Especificar manualmente o órgão do destinatário" var="especificarOrgao" reler="sim" /]
					[/@grupo]
                                [/#if]
				[#if !orgaoAux?? || especificarOrgao == 'Sim']
					[@grupo]
                                                [@texto titulo="Órgão" var="orgao_dest" largura="60" /]
					[/@grupo]
                                [#else]
					[@oculto var="orgao_dest" valor="${orgaoAux}" /]
                                [/#if]	

                                [@memo titulo="Endereço" var="endereco_dest" linhas="4"	colunas="60" /]
			[/@grupo]
		[@selecao titulo="Tamanho da letra" var="tamanhoLetra" opcoes="Normal;Pequeno;Grande" /]
	[/#if]
[/@entrevista]

[@documento margemDireita="3cm"]
	[#if param.tamanhoLetra! == "Normal"]
		[#assign tl = "11pt" /]
	[#elseif param.tamanhoLetra! == "Pequeno"]
		[#assign tl = "9pt" /]
	[#elseif param.tamanhoLetra! == "Grande"]
		[#assign tl = "13pt" /]
	[#else]		
		[#assign tl = "11pt"]
	[/#if]

        [@estiloBrasaoCentralizado tipo="OFÍCIO" tamanhoLetra=tl]

		</head>
		<body>
		<!-- INICIO PRIMEIRO CABECALHO
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF"><tr><td>
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizadoPrimeiraPagina.jsp" />
		</td></tr>
			<tr bgcolor="#FFFFFF">
				<td width="100%">
					<br/>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td align="left" width="40%"><p style="font-family:Arial;font-size:11pt;font-weight:bold;">OF&Iacute;CIO N&ordm; ${doc.codigo}</p></td>
							<td align="right" width="60%"><mod:letra tamanho="${tl}"><p>${doc.dtExtenso}</p></mod:letra></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		FIM PRIMEIRO CABECALHO -->

		<!-- INICIO CABECALHO
		<c:import url="/paginas/expediente/modelos/inc_cabecalhoCentralizado.jsp" />
		FIM CABECALHO -->
		<c:if
			test="${(not f:contains(fecho,'.')) && (not f:contains(fecho,','))}">
			<c:set var="virgula" value="," />
		</c:if>
		<mod:letra tamanho="${tl}">
			<div style="font-family: Arial; font-size: 10pt;">
			<p>&nbsp;</p>
			<c:if test="${empty vocativo}">
			<p align="left" style="TEXT-INDENT: 2cm">${vocativo}</p>
			</c:if>
			<p align="left" style="TEXT-INDENT: 2cm">${vocativo},</p>
			<p><span style="font-size: ${tl}"> ${texto_oficio} </span> <c:if
				test="${not empty fecho}">
				<span style="font-size: ${tl}">
				<center>${fecho}${virgula}</center>
				</span>
			</c:if></p>
			</div>
			<c:import
				url="/paginas/expediente/modelos/inc_assinatura.jsp?formatarOrgao=sim" />
		</mod:letra>

		<!-- INICIO PRIMEIRO RODAPE
		<table width="100%" border="0" cellpadding="2" cellspacing="0" bgcolor="#FFFFFF">
			<tr>
				<td align="left">
					<table align="left" width="100%" border="0" cellpadding="2" cellspacing="0">
						<tr><td><mod:letra tamanho="${tl}"><p>${enderecamento_dest}</p></mod:letra></td></tr>
						<tr><td><mod:letra tamanho="${tl}"><p>${nome_dest}</p></mod:letra></td></tr>
						<c:if test="${not empty cargo_dest}">
						<tr><td><mod:letra tamanho="${tl}"><p>${cargo_dest}</p></mod:letra></td></tr>
						</c:if>
						<c:if test="${not empty orgao_dest}">
						<tr><td><mod:letra tamanho="${tl}"><p>${orgao_dest}</p></mod:letra></td></tr>
						</c:if>
						<c:if test="${not empty endereco_dest}">
						<tr><td><mod:letra tamanho="${tl}"><p><siga:fixcrlf>${endereco_dest}</siga:fixcrlf></p></mod:letra></td></tr>
						</c:if>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<c:import url="/paginas/expediente/modelos/inc_rodapeClassificacaoDocumental.jsp" />
				</td>
			</tr>
		</table>
		FIM PRIMEIRO RODAPE -->

		<!-- INICIO RODAPE
		<c:import url="/paginas/expediente/modelos/inc_rodapeNumeracaoCentralizada.jsp" />
		FIM RODAPE -->

		</body>
		</html>






		<p align="left">DE:
		[#if (doc.nmLotacao)??]
			${doc.nmLotacao}
		[#else]
			${(doc.titular.lotacao.descricao)!}
		[/#if]
		<br>
		PARA: ${(doc.destinatarioString)!}</p>
		<span style="font-size: ${tl}"> ${texto_memorando!} </span>
		<p style="align: justify; TEXT-INDENT: 2cm">Atenciosamente,</p>
        [/@estiloBrasaoAEsquerda]
[/@documento]
[@finalizacao]
[/@finalizacao]
[@assinatura]
	{Memorando Assinado!}
	[#assign f = wf.criarWorkflow('Teste de Integração', doc)/]
[/@assinatura
[/#macro]






<mod:modelo>
	<mod:entrevista>
	</mod:entrevista>


	<mod:documento>
	</mod:documento>
</mod:modelo>
