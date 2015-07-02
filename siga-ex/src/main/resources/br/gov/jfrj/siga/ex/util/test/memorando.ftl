<#compress>
<#macro entrevista>
	<#if gerar_entrevista!false || gerar_formulario!false>
		<#nested>
	</#if>
</#macro>

<#macro documento formato="A4" orientacao="retrato" margemEsquerda="3cm" margemDireita="2cm" margemSuperior="1cm" margemInferior="2cm">
	<#if !gerar_entrevista!false || gerar_finalizacao!false || gerar_assinatura!false>
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
				<#nested>
			</body>
		</html>
	</#if>
</#macro>

<#macro finalizacao>
	<#if gerar_finalizacao!false>
		<#nested>
	</#if>
</#macro>

<#macro assinatura>
	<#if gerar_assinatura!false>
		<#nested>
	</#if>
</#macro>

<#macro grupo titulo="" largura=0 depende="">
	<#local id = (depende=="")?string("", "div" + depende)> 
	<@div id=id depende=depende suprimirIndependente=true>
		<#if largura != 0>
			<#if !grupoLarguraTotal??>
				<#assign grupoLarguraTotal = 0/>
		<table width="100%">
		<tr>
			</#if>
			<#assign grupoLarguraTotal = grupoLarguraTotal + largura/>
		<td width="${largura}%" valign="top">
		</#if>
		<table class="entrevista" width="100%">
			<#if titulo != "">
				<tr class="header">
					<td>${titulo}</td>
				</tr>
			</#if>
			<tr>
				<td><#nested></td>
			</tr>
		</table>
		<#if largura != 0>
			</td>
			<#if grupoLarguraTotal >= 100>
				</td>
				</table>
				<#assign grupoLarguraTotal = 0/>
			</#if>
		</#if>
	</@div>
</#macro>

<#macro div id="" depende="" suprimirIndependente=false>
	<#if suprimirIndependente || depende != "">
		<div<#if id != ""> id="${id}"</#if><#if depende != ""> depende=";${depende};"</#if>><#if id != ""><!--ajax:${id}--></#if><#nested><#if id != ""><!--/ajax:${id}--></#if></div>
	<#else>
	<#nested>
	</#if>
</#macro>

<#macro editor var titulo semBotaoSalvar=false>
	<#if param[var]??>
		<#local v = param[var]/>
	<#else>
		<#local v = ""/>
	</#if>
	<#if v != "">
		<#local v = ex.canonicalizarHtml(v, false, true, false, true)/>
	<#else>
		<#local v = "&lt;p style=&quot;TEXT-INDENT: 2cm&quot; align=&quot;justify&quot;&gt;&amp;nbsp;&lt;/p&gt;"/>
	</#if>
	<div>
		<#if titulo != "">
			<b>${titulo}</b>
		</#if>
		<#if !formulario!false>
			<input type="hidden" name="vars" value="${var}" />
			<script type="text/javascript">FCKeditorAPI = null;__FCKeditorNS = null;</script>	
			<table class="entrevista" width="100%">
				<tr>
					<td></td>
					<td colspan="3">
						<input type="hidden" id="${var}" name="${var}" value="&lt;p style=&quot;TEXT-INDENT: 2cm&quot; align=&quot;justify&quot;&gt;&amp;nbsp;&lt;/p&gt;">
						<input type="hidden" id="${var}___Config" value="Default<#if semBotaoSalvar>SemSave</#if>">
						<iframe id="${var}___Frame" src="/fckeditor/editor/fckeditor.html?InstanceName=${var}" width="100%" height="300" frameborder="no" scrolling="no"></iframe>
					</td>
				</tr>
			</table>
		<#else>
			<br>${v}<br><br>
		</#if>
	</div>
</#macro>

<#macro selecao var titulo opcoes reler=false idAjax="" onclick="">
	<#local l=opcoes?split(";")>
	<#if param[var]??>
		<#local v = param[var]/>
	<#else>
		<#local v = l?first/>
	</#if>
	
	${titulo}:
	<#if !gerar_formulario!false>
		<input type="hidden" name="vars" value="${var}" />
		<select name="${var}" <#if reler> onchange="javascript: sbmt(<#if idAjax != "">'${idAjax}'</#if>);"</#if> onclick="${onclick}">
			<#list l as opcao>
				<option<#if v == opcao> selected</#if> value="${opcao}">${opcao}</option>
			</#list>
		</select>
	<#else>
		<span class="valor">${v}</span>
	</#if>
</#macro>

<#macro letra tamanho>
	<#local body><#nested/></#local>
	<span style="font-size:${tamanho}">
		${func.fixFontSize(body,tamanho)}
	</span>
</#macro>

<#macro primeiroCabecalho>
	<!-- INICIO PRIMEIRO CABECALHO
		<#nested/>
	FIM PRIMEIRO CABECALHO -->
</#macro>

<#macro cabecalho>
	<!-- INICIO CABECALHO
		<#nested/>
	FIM CABECALHO -->
</#macro>

<#macro primeiroRodape>
	<!-- INICIO PRIMEIRO RODAPE
		<#nested/>
	FIM PRIMEIRO RODAPE -->
</#macro>

<#macro rodape>
	<!-- INICIO RODAPE
		<#nested/>
	FIM RODAPE -->
</#macro>

<#macro cabecalhoEsquerdaPrimeiraPagina>
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
				<#if mov??>
					${mov.lotaTitular.orgaoUsuario.descricaoMaiusculas}
				<#else>
					${doc.lotaTitular.orgaoUsuario.descricaoMaiusculas}
				</#if></p>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</#macro>

<#macro cabecalhoEsquerda>
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
				<#if mov??>
					${mov.lotaTitular.orgaoUsuario.descricaoMaiusculas}
				<#else>
					${doc.lotaTitular.orgaoUsuario.descricaoMaiusculas}
				</#if><br />
				&nbsp;</p>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</#macro>

<#macro rodapeClassificacaoDocumental>
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
</#macro>

<#macro rodapeNumeracaoADireita>
<table width="100%" border="0" cellpadding="0" bgcolor="#FFFFFF">
	<tr>
		<td width="100%" align="right">#pg</td>
	</tr>
</table>
</#macro>

<#macro assinaturaCentro>
<p style="font-family: Arial; font-size: 11pt; font-weight: bold;" align="center">
	<#if (doc.nmSubscritor)??>
		${doc.nmSubscritor}
	<#else>
		${(doc.subscritor.descricao)!}
	</#if>
	<#if !apenasNome??>	
		<br />
		<#if param.apenasCargo??>
				${(doc.subscritor.cargo.nomeCargo)!}
		<#else>
			<#if (doc.nmFuncao)??>
				${doc.nmFuncao}
			<#elseif (doc.titular.funcaoConfianca.nomeFuncao)??>
				${doc.titular.funcaoConfianca.nomeFuncao}
				<#if (doc.titular.idPessoa)! != (doc.subscritor.idPessoa)!>	EM EXERCÍCIO </#if>
			<#elseif (doc.subscritor.funcaoConfianca.nomeFuncao)??>
				${doc.subscritor.funcaoConfianca.nomeFuncao}
			<#else>
				${doc.subscritor.cargo.nomeCargo}
			</#if>
		</#if>
		 
		<#if param.formatarOrgao??>
			<br>
			<#if (doc.nmLotacao)??>
				${doc.nmLotacao}
			<#else>
				${(doc.titular.lotacao.nomeLotacao)!}
			</#if>
		</#if>
		
		<#list doc.mobilGeral.exMovimentacaoSet as mov>
			<#if (mov.exTipoMovimentacao.idTpMov)! == 24>
				<br/><br/><br/>
				<#if mov.nmSubscritor??>
					${mov.nmSubscritor}
				<#else>
					${(mov.subscritor.nomePessoa)!}
				</#if>
				<br>
				<#if mov.nmFuncao??>
					${mov.nmFuncao}
				<#elseif (mov.titular.funcaoConfianca.nomeFuncao)??>
					${mov.titular.funcaoConfianca.nomeFuncao} 
					<#if substituicao && (doc.titular.idPessoa)! != (doc.subscritor.idPessoa)!>	EM EXERCÍCIO </#if>
				<#elseif (mov.subscritor.funcaoConfianca.nomeFuncao)??>
					${mov.subscritor.funcaoConfianca.nomeFuncao}
				<#else>
					${(mov.subscritor.cargo.nomeCargo)!}
				</#if>
				<#if param.formatarOrgao??>
					<br>
					<#if mov.nmLotacao??>
						${mov.nmLotacao}
					<#else>
						${mov.titular.lotacao.nomeLotacao}
					</#if>
				</#if>
			</#if>
		</#list>
	</#if>
	<#if param.textoFinal??>
		<br/>${param.textoFinal}
	</#if>
</p>
</#macro>





<@entrevista>
	<#if !esconderTexto!false>
		<@grupo titulo="Texto a ser inserido no corpo do memorando">
			<@grupo>
				<@editor titulo="" var="texto_memorando" />
			</@grupo>
		</@grupo>
		<@selecao titulo="Tamanho da letra" var="tamanhoLetra" opcoes="Normal;Pequeno;Grande" />
	</#if>
</@entrevista>

<@documento>
	<#if param.tamanhoLetra! == "Normal">
		<#assign tl = "11pt" />
	<#elseif param.tamanhoLetra! == "Pequeno">
		<#assign tl = "9pt" />
	<#elseif param.tamanhoLetra! == "Grande">
		<#assign tl = "13pt" />
	<#else>		
		<#assign tl = "11pt">
	</#if>
		
	<@primeiroCabecalho>
	<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
	<@cabecalhoEsquerdaPrimeiraPagina/>
	</td></tr>
		<tr bgcolor="#FFFFFF">
			<td width="100%">
				<table width="100%">
					<tr>
						<td align="right"><p style="font-family:Arial;font-weight:bold;font-size:11pt;">MEMORANDO N&ordm; ${(doc.codigo)!}</p></td>
					</tr>
					<tr>
						<td align="right"><@letra tamanho="11pt"><p>${(doc.dtExtenso)!}</p></@letra></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</@primeiroCabecalho>

	<@cabecalho>
	<@cabecalhoEsquerda/>
	</@cabecalho>

	<@letra tamanho=tl>
		<p align="left">DE:
		<#if (doc.nmLotacao)??>
			${doc.nmLotacao}
		<#else>
			${(doc.titular.lotacao.descricao)!}
		</#if>
		<br>
		PARA: ${(doc.destinatarioString)!}</p>
		<span style="font-size: ${tl}"> ${texto_memorando!} </span>
		<p style="align: justify; TEXT-INDENT: 2cm">Atenciosamente,</p>
		<p>&nbsp;</p>
		<@assinaturaCentro/>
	</@letra>

	<@primeiroRodape>
	<@rodapeClassificacaoDocumental/>
	</@primeiroRodape>

	<@rodape>
	<@rodapeNumeracaoADireita/>
	</@rodape>
</@documento>
<@finalizacao>
	{Memorando Finalizado!}
	<#assign f = wf.criarWorkflow('Teste de Integração', doc)/>
</@finalizacao>
<@assinatura>
	{Memorando Assinado!}
	<#assign f = wf.criarWorkflow('Teste de Integração', doc)/>
</@assinatura>
</#compress>