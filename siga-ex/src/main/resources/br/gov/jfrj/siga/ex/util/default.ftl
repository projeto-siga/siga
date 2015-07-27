[#macro parte id titulo depende="" responsavel="" bloquear=true esconder=false]
    [#if !esconder]
      [#if gerar_partes!false]
        [#if root[id]??]
          [#local preenchido = root[id]/]
        [#else]
          [#local preenchido = false/]
        [/#if]
        [#local body][#nested/][/#local]
        [#local hash = false/]
          <parte id="${id}" titulo="${titulo}" preenchido="${(preenchido == "Sim")?string("1","0")}" responsavel="${responsavel}" [#if .vars["parte_mensagem_" + id]??] mensagem="${.vars["parte_mensagem_" + id]!default}" [/#if]>
          [#if depende!=""]
            [#list depende?split(";") as dependencia]
              <depende id="${dependencia}" hash="hash"/>
            [/#list]
          [/#if]
        </parte>
      [#else]
        [#local idDiv = "parte_div_" + id /] 
        [@div id=idDiv depende=depende suprimirIndependente=true]
          <input type="hidden" id="parte_dependentes_${id}" class="parte_dependentes" value="${id}:${depende}:${bloquear?c}:${responsavel}"/>
          [@oculto var="parte_mensagem_${id}" /]
          
          <table class="parte" width="100%">
            <tr class="header">
              <td>${titulo}
                <span style="float: right"><input type="button" value="Solicitar Alteração" onclick="parte_solicitar_alteracao('${id}', '${titular}', '${lotaTitular}');"/> [@checkbox titulo="Preenchimento Concluído" var=id reler=true idAjax=id id="parte_chk_"+id onclique="parte_atualizar('${titular}', '${lotaTitular}');" /]</span>
                <span style="float: right; padding-right: 2em;">Responsável: ${responsavel}</span>
              </td>
            </tr>
            <tr>
              <td>
              	<div id="parte_div_mensagem_${id}" class="gt-error"></div>
				<fieldset id="parte_fieldset_${id}">[#nested]</fieldset></td>
            </tr>
          </table>
          [#local titular = .vars['sigla_titular']!""]
          [#local lotaTitular = .vars['sigla_lota_titular']!""]
            <script type="text/javascript">
                $(document).ready(function(){parte_atualizar('${titular}', '${lotaTitular}')});
            </script>
        [/@div]
      [/#if]
    [#else]
        [#nested]
    [/#if]
[/#macro]

[#-- Assinatura de Parte de Documento Colaborativo --]
[#macro assinaturaParte doc parte formatarOrgao=false]
  [#attempt]
    [#assign mov = func.parteUltimaMovimentacao(doc, parte) /]
  [#recover]
    [#return]
  [/#attempt]
  <p style="font-family: Arial; font-size: 11pt; text-align: center">
    ${(mov.subscritor.descricao)!}<br />
  
  [#if mov.nmFuncao??]
    ${mov.nmFuncao} 
  [#elseif mov.titular?? && doc.titular?? && mov.titular.idPessoa == doc.titular.idPessoa && doc.nmFuncao??]
    ${doc.nmFuncao} 
  [#elseif mov.subscritor?? && doc.subscritor?? && mov.subscritor.idPessoa == doc.subscritor.idPessoa && doc.nmFuncao??]
    ${doc.nmFuncao} 
  [#elseif (mov.titular.funcaoConfianca.nomeFuncao)??]
    ${mov.titular.funcaoConfianca.nomeFuncao} ${(mov.titular.idPessoa != mov.subscritor.idPessoa)?string('EM EXERCÍCIO', '')} 
  [#elseif (mov.subscritor.funcaoConfianca.nomeFuncao)??]
    ${mov.subscritor.funcaoConfianca.nomeFuncao} 
  [#else]
    ${(mov.subscritor.cargo.nomeCargo)!}
  [/#if]
  <br />
   
  [#if mov.nmLotacao??]
    ${mov.nmLotacao} 
  [#else]
    ${(mov.titular.lotacao.nomeLotacao)!}
  [/#if]
  </p>
[/#macro]


[#function par parameter]
    [#if param[parameter]??]
        [#return param[parameter]]
    [#else]
        [#return ""]
    [/#if]
    [#assign inlineTemplate = ["[#assign default_${var} = true/]", "assignInlineTemplate"]?interpret /]
    [@inlineTemplate/]
[/#function]
[#macro entrevista acaoGravar="" acaoExcluir="" acaoCancelar="" acaoFinalizar=""]
    [#if gerar_entrevista!false || gerar_formulario!false || gerar_partes!false]
        [#if acaoGravar!=""]
            <input type="hidden" name="acaoGravar" id="acaoGravar" value="${acaoGravar}" />
	    <input type="hidden" name="vars" value="acaoGravar" />
        [/#if]
        [#if acaoExcluir!=""]
            <input type="hidden" name="acaoExcluir" id="acaoExcluir" value="${acaoExcluir}" />
	    <input type="hidden" name="vars" value="acaoExcluir" />
        [/#if]
        [#if acaoCancelar!=""]
            <input type="hidden" name="acaoCancelar" id="acaoCancelar" value="${acaoCancelar}" />
	    <input type="hidden" name="vars" value="acaoCancelar" />
        [/#if]
        [#if acaoFinalizar!=""]
            <input type="hidden" name="acaoFinalizar" id="acaoFinalizar" value="${acaoFinalizar}" />
	    <input type="hidden" name="vars" value="acaoFinalizar" />
        [/#if]
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
[#macro resumo visivel=false]
  [#assign visivel = visivel /]
  [#if gerar_resumo!false]
    <!-- resumo -->
      [#nested]
    <!-- /resumo -->
  [/#if]
[/#macro]
[#macro topico descricao valor]
  [#if visivel!false]
    <li>${descricao}=${valor}</li>
  [/#if]
  <!-- topico -->
    <input type="hidden" name="${descricao}" value="${valor}"/>
  <!-- /topico -->
[/#macro]





[#macro moeda var titulo="" largura="" maxcaracteres="" idAjax="" reler="" relertab="" obrigatorio="nao" default="" somenteLeitura=false]
    [#if reler == 'ajax']
        [#local jreler = " onblur=\"javascript: sbmt('" + idAjax + "');\""]
    [/#if]

    [#if reler == 'sim']
        [#local jreler = " onblur=\"javascript: sbmt();\""]
    [/#if]

    [#if relertab == 'sim']
        [#local jrelertab = " onblur=\"javascript: sbmt();\""]
    [/#if]

    [#if largura?string != ""]
        [#local jlargura = " size=\"" + largura + "\""]
    [/#if]

    [#if maxcaracteres != ""]
        [#local jmaxcaracteres = " maxlength=\"" + maxcaracteres + "\""]
    [/#if]

    [#local v = .vars[var]!""]
    [#if v == ""]
        [#local v = default/]
    [/#if]

    <input type="hidden" name="vars" value="${var}" />

    [#if (alerta!"Não") = 'Sim' && v = ""]
    [#list obrigatorios?split(",") as campo]
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
    <input onkeypress="return formataReais(this, '.' , ',', event)"
    type="text" name="${var}" value="${v}" ${jreler!""}${jrelertab!""}${jlargura!""}${jmaxcaracteres!""}/>
    [#else]
    <span class="valor">${v}</span>
    [/#if]
[/#macro]

[#function formatarCPF fmtCPF_param]
[#-- Início do comentário
Aplicação: Função para formatar um CPF
Acrônimo: fmtCPF_
Autor:    Ruben
Data:     13/03/2012
Descrição:
Esta função obtém uma string contendo o CPF a ser formatado e o devolve formatado
com a seguinte apresentação: 999.999.999-99  


Pré-condições:ext
A string passada como CPF pode conter quaisquer caracteres numéricos e não numéricos, visto que a função irá colher somente os dígitos numéricos. 
Se a quantidade de dígitos numéricos for menor que 11 isto indica que possivelmente não foram passados os zeros a esquerda e neste caso, 
a função introduzirá a quantidade necessária de zeros a esquerda.
Exemplos
String passada             String considerada        String retornada      
4751084679                 47510846749               475.108.467-49
475.108.467/49             47510846749               475.108.467-49
510846749                  00510846748               005.108.467-49 
475xpto108bb46749          47510846749               475.108.467-49

Condições de erro:
Se a string for: nula ou maior que 11 ou menor que 3 dígitos numéricos,
será retornado "erro" pela função.

Parâmetros recebidos:                                                       
+----------------+-----------+-------------+----------+-----------------------------
|  Parâmetro     | Tipo/Tam  | Obrigatório | Default  | Descrição
+----------------+-----------+-------------+----------+-----------------------------
|  fmtCPF_param  | String    |  Sim        |          |  O CPF a ser formatado
+----------------+-----------+-------------+----------+------------------------------
Parâmetros devolvidos:
+----------------+-----------+-------------+----------+------------------------------
| Parâmetro      | Tipo/Tam  | Obrigatório | Default  | Descrição
+----------------+-----------+-------------+----------+------------------------------
|                | String    |  Sim        |          |  O CPF formatado ou ?erro?
+--------------+---------+--------+----------+---------------------------------------

Chamada da function:
A chamada nas aplicações poderá ser algo tipo:

   [#assign antigoCPF = "47510846749"
   [#assign novoCPF = formatarCPF(antigoCPF)/]
   [#if novoCPF == ?erro?]
      Tratar o erro, pois o CPF fornecido está inconsistente
   [/#if]     

Variáveis:
fmtCPF_param    Variável local para o CPF recebido pela função
fmtCPF_novo     Variável local para construir o novo CPF   
fmtCPF_i        Variável de looping usada como índice em ?substring 
fmtCPF_j        Variável de local usada como índice em ?substring
fmtCPF_digito   Variável local para  cada caracter do CPF recebido 
fmtCPF_retorno  Variável local com o novo CPF formatado retornado pela função

Fim comentário --]

[#-- Início do código --]

[#if !fmtCPF_param??]
 [#return "erro"]
[/#if]  

[#assign fmtCPF_tam = fmtCPF_param?length /]
[#assign fmtCPF_novo = ""/]
[#assign fmtCPF_digito = ""/]

[#list 1..fmtCPF_tam as fmtCPF_i]
 [#assign fmtCPF_digito = fmtCPF_param?substring(fmtCPF_i - 1, fmtCPF_i)/]
  [#if fmtCPF_digito == "0"  || fmtCPF_digito == "1" || fmtCPF_digito == "2" ||  fmtCPF_digito == "3" ||  fmtCPF_digito == "4" ||   fmtCPF_digito == "5" ||  fmtCPF_digito == "6" ||  fmtCPF_digito == "7" ||  fmtCPF_digito == "8" || fmtCPF_digito == "9"]
   [#assign fmtCPF_novo = fmtCPF_novo + fmtCPF_digito /] 
  [/#if]  
[/#list]  

[#assign fmtCPF_tam = fmtCPF_novo?length/]
[#if fmtCPF_tam > 11 || fmtCPF_tam < 3]
 [#return "erro"]
[#elseif fmtCPF_tam < 11]
 [#assign fmtCPF_qtezeros = 11 - fmtCPF_tam/]
 [#list 1..fmtCPF_qtezeros as fmtCPF_i]
  [#assign fmtCPF_novo = "0" + fmtCPF_novo/]
 [/#list]
[/#if]

[#assign fmtCPF_retorno = fmtCPF_novo?substring(0, 3) + "." + fmtCPF_novo?substring(3, 6) + "." + fmtCPF_novo?substring(6, 9) + "-" + fmtCPF_novo?substring(9, 11) /]
[#return fmtCPF_retorno]
[/#function]
[#macro numeroDJE]
    <!-- INICIO NUMERO -->
        [#nested/]
    <!-- FIM NUMERO -->
[/#macro]

[#macro mioloDJE]
    <!-- INICIO MIOLO -->
        [#nested/]
    <!-- FIM MIOLO -->
[/#macro]


[#macro inicioMioloDJE]
    <!-- INICIO MIOLO -->
[/#macro]

[#macro fimMioloDJE]
    <!-- FIM MIOLO -->
[/#macro]


[#macro tituloDJE]
    <!-- INICIO TITULO 
        [#nested/]
    FIM TITULO -->
[/#macro]


[#macro br]
    <br>
[/#macro]

[#macro grupo titulo="" largura=0 depende="" esconder=false]
    [#if !esconder]
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
    [#else]
        [#nested]
    [/#if]
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
        [#local jreler = " onchange=\"javascript: sbmt('" + idAjax + "');\""]
    [/#if]

    [#if reler == 'sim']
        [#local jreler = " onchange=\"javascript: sbmt();\""]
    [/#if]

    [#if relertab == 'sim']
        [#local jrelertab = " onblur=\"javascript: sbmt();\""]
    [/#if]

    [#if largura?string != ""]
        [#local jlargura = " size=\"" + largura + "\""]
    [/#if]

    [#if maxcaracteres != ""]
        [#local jmaxcaracteres = " maxlength=\"" + maxcaracteres + "\""]
    [/#if]

    [#local v = .vars[var]!""]
    [#if v == ""]
        [#local v = default/]
    [/#if]

    <input type="hidden" name="vars" value="${var}" />

    [#if alerta!"" == 'Sim' && v==""]
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
[#macro oculto var valor="" default=""]
    [#local v = (valor != "")?string(valor,.vars[var]!default) /]
    <input type="hidden" name="vars" value="${var}" />
    [#if !gerar_formulario!false]
        <input type="hidden" id="${var}" name="${var}" value="${v}"/>
    [/#if]
[/#macro]
[#macro checkbox var titulo="" default="Nao" idAjax="" reler=false onclique="" obrigatorio=false id=""]
    [#if reler == true && idAjax != ""]
            [#local jreler = " sbmt('" + idAjax + "');\""]
    [#elseif reler == true]
            [#local jreler = " sbmt();\""]
    [/#if]

    [#if .vars[var]??]
    [#local v = .vars[var]/]
    [#else]
    [#local v = default/]
        [#assign inlineTemplate = ["[#assign ${var} = v/]", "assignInlineTemplate"]?interpret /]
        [@inlineTemplate/]
    [/#if]

    <input type="hidden" name="vars" value="${var}" />
    <input type="hidden" id="${var}" name="${var}" value="${v}" />

    [#if alerta!"" == 'Sim' && v == ""]
    [#list paramValues.obrigatorios as campo]
         [#if campo == var]
         [#local vermelho = "color:red"]
             [/#if]
        [/#list]
    [/#if]

    [#if obrigatorio]
    [#local negrito = "font-weight:bold"]
    <input type="hidden" name="obrigatorios" value="${var}" />
    [/#if]

    [#if !gerar_formulario!false]
        <input id="${id}" type="checkbox" name="${var}_chk" value="Sim"
               [#if v=='Sim']checked[/#if] 
               onclick="javascript: if (this.checked) document.getElementById('${var}').value = 'Sim'; else document.getElementById('${var}').value = 'Nao'; ${onclique!""}; ${jreler!""}" /> <label for="${id}" style="${negrito!""};${vermelho!""}">${titulo!""}</label>

    [#else]
    <span class="valor">${v}</span>
    [/#if]
[/#macro]
[#macro radio titulo var reler=false idAjax="" default="Não" valor="Sim" onclique=""]
    [#if reler == true && idAjax != ""]
            [#local jreler = " sbmt('" + idAjax + "');"]
    [#elseif reler == true]
            [#local jreler = " sbmt();"]
    [/#if]

    [#local v = .vars[var]!(default == "Sim")?string(valor, "") /]

    [#if !.vars["temRadio_"+var]??]
        <input type="hidden" name="vars" value="${var}" />
        <input type="hidden" id="${var}" name="${var}" value="" />
        [#assign inlineTemplate = ["[#assign temRadio_${var} = true/]", "assignInlineTemplate"]?interpret /]
        [@inlineTemplate/]
    [/#if]
    [#if v == valor]
        <script>document.getElementById('${var}').value = '${valor}';</script>
    [/#if]

    [#if !gerar_formulario!false]
    <table><tr><td>
            <input type="radio" name="${var}_chk" value="${valor}" [#if v == valor]checked[/#if]
        onclick="javascript: if (this.checked) document.getElementById('${var}').value = '${valor}'; 
                ${onclique}; ${jreler!};" /> 
    </td><td>${titulo}</td></tr></table>
    [#else]
    <span class="valor">${v}</span>
    [/#if]
[/#macro]
[#macro editor var titulo semBotaoSalvar=false]
    [#if .vars[var]??]
        [#local v = .vars[var]/]
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

[#macro XStandard nome="" conteudo=""]
        <script type="text/javascript" language="Javascript1.1">

        var insertingTable = false;
     
        function onSave() {
            document.getElementById('${nome}').EscapeUnicode = false;
        document.getElementById('${nome}').value = document.getElementById('xstandard').value;
            }
    
        function xsDialogPropertiesActivated(id, qpath, element, attributes, metadata) {
        if (qpath == '' && element == 'table'){
            document.getElementById('xstandard').SetDialogProperties("<attributes><attr><name>summary</name><value>Tabela</value></attr><attr><name>bordercolor</name><value>#000000</value></attr><attr><name>style</name><value>border-width:1px;border-style:solid;border-collapse:collapse</value></attr></attributes>", false, false);
            }
        }
    
        </script>

        <input type="hidden" id="${nome}" name="${nome}" />

        <object classid="clsid:0EED7206-1661-11D7-84A3-00606744831D"
        codebase="/sigaex/XStandard/XStandard.cab#Version=3,0,0,0"
        type="application/x-xstandard" id="xstandard" width="800" height="400">

        <!-- 
        <param name="License" value="%AppPath%\XStandard\license.txt" />
        <param name="CSS" value="%AppPath%\XStandard\format.css" />
        <param name="Styles" value="%AppPath%\XStandard\styles.xml" />
         <param name="SpellCheckerLangFilter"
            value="da,de,en-ca,en-us,en-gb,es,fr,it,nl,no,pt,sv" />
         <param name="ToolbarWysiwyg"
            value="ordered-list,unordered-list,definition-list,,draw-layout-table,draw-data-table,image,separator,hyperlink,attachment,directory,spellchecker,,wysiwyg,source,preview,screen-reader,help" />            
             -->

        <param nams="ImageLibraryURL"
        value="http://soap.xstandard.com/imagelibrary.aspx" />
        <param name="AttachmentLibraryURL"
        value="http://soap.xstandard.com/attachmentlibrary.aspx" />
        <param name="SpellCheckerURL"
        value="http://soap.xstandard.com/spellchecker.aspx" />
        <param name="DirectoryURL"
        value="http://soap.xstandard.com/directory.aspx" />
        <param name="SubdocumentURL"
        value="http://soap.xstandard.com/subdocument.aspx" />
        <param name="EscapeUnicode" value="false" />

            <!--<c:set var="conteudo">
        <c:out value="${conteudo}" escapeXml="true" />
        </c:set>-->
        <param name="Value" value="${conteudo}" />

        <param name="SpellCheckerLangFilter" value="pt" />
        <param name="SpellCheckerLang" value="pt" />
            <param name="License" value="&{abs('/sigaex/XStandard/license.txt')};" />
            <param name="CSS" value="/sigaex/XStandard/format.css" />
        <param name="Styles" value="/sigaex/XStandard/styles-pt.xml" />
        <param name="Buttons" value="/sigaex/XStandard/buttons-pt.xml" />
        <param name="Icons" value="/sigaex/XStandard/icons.xml" />
        <!-- Ver como coloca português -->
        <param name="Lang" value="pt" />
        <param name="Localization" value="/sigaex/XStandard/localization-pt.xml" />
        <param name="EnablePasteMarkup" value="yes" />
        <param name="ToolbarWysiwyg"
        value="cut,copy,paste,undo,redo,find-replace,,strong,em,underline,,align-left,align-center,align-right,justify,,undo-blockquote,blockquote,,undo-indent-first,indent-first,,ordered-list,unordered-list,,draw-data-table,,separator,pagebreak,,spellchecker,,source,,help" />
        <param name="BackgroundColor" value="white" />
        <param name="BorderColor" value="#888888" />
        <!-- <param name="Base" value="http://soap.xstandard.com/library/" /> -->
        <param name="LatestVersion" value="2.0.5.0" />
        <param name="ToolbarEffect" value="linear-gradient" />
        <param name="ShowStyles" value="yes" />
        <param name="ShowToolbar" value="yes" />
        <param name="Mode" value="wysiwyg" />
        <param name="Options" value="0" />
        <param name="IndentOutput" value="yes" />
        <param name="ProxySetting" value="platform" />
        <param name="Debug" value="yes" />

        <!-- Tem duas opções que talvez sejam úteis: PreviewXSLT e ScreenReaderXSLT -->
        <!-- A opção icons é pros ícones das operações principais. O Placeholders é pros ícones das tags customizadas -->
        <!-- Ver qual a utilidade desse aqui: param name = EditorCSS --> <!-- Essas abaixo definem os botões em outros modos de visualização 
        <param name="ToolbarSource" value="" />
        <param name="ToolbarPreview" value="" />
        <param name="ToolbarScreenReader" value="" /> 
        Talvez CustomInlineElements, CustomBlockElements e CustomEmptyElements sirvam pras tabelas
        Depois, ver se as integration settings servem pra alguma coisa
        VER HeartbeatURL e Heartbeat Interval. Parecem ser úteis pra verificar sessão
        Talvez algumas subs sejam úteis para mudar os contexts menus. Ver na seção Hooks & Extensions
        Funções TagList, Path e QPath e TagListXML são interessantes
        --> 
        </object>
[/#macro]
[#macro selecao var titulo opcoes reler=false idAjax="" onclick=""]
    [#local l=opcoes?split(";")]
    [#if .vars[var]??]
        [#local v = .vars[var]/]
    [#else]
        [#local v = l?first/]
                [#assign inlineTemplate = ["[#assign ${var} = v/]", "assignInlineTemplate"]?interpret /]
                [@inlineTemplate/]
        [/#if]
    
        ${titulo!""}[#if titulo != ""]:[/#if]

    [#if !gerar_formulario!false]
        <input type="hidden" name="vars" value="${var}" />
        <select name="${var}" [#if reler] onchange="javascript: sbmt([#if idAjax != ""]'${idAjax}'[/#if]);"[/#if] onclick="${onclick}">
                    [#list l as opcao]
                        <option[#if v == opcao] selected[/#if] value="${opcao}">${opcao}</option><br/>
            [/#list]
        </select>
    [#else]
        <span class="valor">${v}</span>
    [/#if]
[/#macro]
[#macro memo var titulo colunas linhas reler=false obrigatorio=false default=""]
        [#if reler == true]
                [#local jreler = " onchange=\"javascript: sbmt();\""]
        [/#if]

        [#local v = .vars[var]!default]

        <input type="hidden" name="vars" value="${var}" />

        [#if alerta!"" == 'Sim' && v==""]
            [#list paramValues.obrigatorios as campo]
                    [#if campo == var]
                        [#local vermelho = "color:red"]
                        [/#if]
                [/#list]
        [/#if]

        [#if obrigatorio]
            [#local negrito = "font-weight:bold"]
            <input type="hidden" name="obrigatorios" value="${var}" />
        [/#if]

        <div style="padding-top:5px;">
                [#if titulo != ""] 
                        <span style="${negrito!""};${vermelho!""}">${titulo}:<br/></span>
                [/#if]

                [#if !gerar_formulario!false]
                    <textarea cols="${colunas}" rows="${linhas}" name="${var}" ${jreler!""}>${v}</textarea>
                [#else]
                    <span class="valor">${v}</span>
                [/#if]
        </div>
[/#macro]
[#macro mensagem texto titulo="" vermelho=false]
    <span style="[#if vermelho]color=#ff0000[/#if]">[#if titulo?? && titulo!=""]<b>${titulo}</b>: [/#if]${texto!""}</span>
[/#macro]
[#macro separador]
    <hr color="#FFFFFF"/>
[/#macro]
[#macro caixaSelecao titulo var tipo="" idInicial="" siglaInicial="" descricaoInicial="" modulo="" desativar=false buscar=true ocultarDescricao=false reler=false idAjax="" default="" alerta=false obrigatorio=false relertab="" paramList="" grande=false]
    [#local larguraPopup = 600 /]
    [#local alturaPopup =400 /]
    [#local tipoSel = "_" + tipo /]
    [#local acaoBusca = (modulo=="")?string("/siga/app/","/"+modulo+"/") + tipo /]
    
        [#if paramList != ""]
            [#list paramList?split(";") as parametro]
                [#local p2 = parametro?split("=") /]
            [#if p2??]
                [#local selecaoParams = selecaoParams!"" + "&" + p2[0] + "=" + p2[1] /]
            [/#if]
        [/#list]
        [/#if]
    
    <script type="text/javascript">
    
    self.retorna_${var}${tipoSel} = function(id, sigla, descricao) {
        try {
            newwindow_${var}.close();
        } catch (E) {
        } finally {
        }
        document.getElementsByName('${var}${tipoSel}Sel.id')[0].value = id;
        [#if !ocultarDescricao]
            try {
                document.getElementsByName('${var}${tipoSel}Sel.descricao')[0].value = descricao;
                document.getElementById('${var}${tipoSel}SelSpan').innerHTML = descricao;
            } catch (E) {
            }
        [/#if]
        document.getElementsByName('${var}${tipoSel}Sel.sigla')[0].value = sigla;
        [#if reler]
                    //window.alert("vou reler tudo!");
            document.getElementsByName('req${var}${tipoSel}Sel')[0].value = "sim";
            document.getElementById('alterouSel').value='${var}';
            sbmt([#if idAjax != ""]'${idAjax}'[/#if]);
        [/#if]
    }
     
    self.newwindow_${var} = '';
    self.popitup_${var}${tipoSel} = function(sigla) {
              var url = '${acaoBusca}/buscar?propriedade=${var}${tipoSel}&sigla='+encodeURI(sigla)+'${selecaoParams!}';
        
        if (!newwindow_${var}.closed && newwindow_${var}.location) {
            newwindow_${var}.location.href = url;
        } else {
            var popW = ${larguraPopup};
            var popH = ${alturaPopup};
            
            [#if grande]
                popW = screen.width*0.75;
                popH = screen.height*0.75;
            [/#if]
            var winleft = (screen.width - popW) / 2;
            var winUp = (screen.height - popH) / 2; 
            winProp = 'width='+popW+',height='+popH+',left='+winleft+',top='+winUp+',scrollbars=yes,resizable'
            newwindow_${var}=window.open(url,'${var}${tipoSel}',winProp);
        }
        newwindow_${var}.opener = self;
        
        if (window.focus) {
            newwindow_${var}.focus()
        }
        return false;
    }
    
    self.resposta_ajax_${var}${tipoSel} = function(response, d1, d2, d3) {
        var sigla = document.getElementsByName('${var}${tipoSel}Sel.sigla')[0].value;
        var data = response.split(';');
        if (data[0] == '1')
            return retorna_${var}${tipoSel}(data[1], data[2], data[3]);
        retorna_${var}${tipoSel}('', '', '');
    
        [#if buscar]
            return popitup_${var}${tipoSel}(sigla);
        [#else]
            return;
        [/#if]
    }
    
    self.ajax_${var}${tipoSel} = function() {
        var sigla = document.getElementsByName('${var}${tipoSel}Sel.sigla')[0].value;
        if (sigla == '') {
            return retorna_${var}${tipoSel}('', '', '');
        }
          var url = '${acaoBusca}/selecionar?var=${var}${tipoSel}&sigla='+encodeURI(sigla)+'${selecaoParams!}';
        url = url + '&sigla=' + sigla;
        PassAjaxResponseToFunction(url, 'resposta_ajax_${var}${tipoSel}', false);
    }
    
    </script>
    
    <input type="hidden" name="${var}${tipoSel}Sel.id" />
    <input type="hidden" name="${var}${tipoSel}Sel.descricao" />
    <input type="hidden" name="${var}${tipoSel}Sel.buscar" />
    <input type="hidden" name="req${var}${tipoSel}Sel" />
    <input type="hidden" name="alterouSel" value="" id="alterouSel" />
    <input type="text" name="${var}${tipoSel}Sel.sigla" onkeypress="return handleEnter(this, event)"
        onblur="javascript: ajax_${var}${tipoSel}();" size="25" ${desativar?string('disabled="true"','')} />
    [#if buscar]
        <input type="button" id="${var}${tipoSel}SelButton" value="..."
            onclick="javascript: popitup_${var}${tipoSel}('');"
            ${desativar?string("disabled","")} theme="simple">
    [/#if]
    [#if !ocultarDescricao]
        <span id="${var}${tipoSel}SelSpan">${.vars[var+tipoSel+"Sel.descricao"]!}</span>
    [/#if]
    
    <script type="text/javascript">
        document.getElementsByName('${var}${tipoSel}Sel.id')[0].value = '${(idInicial=="")?string(.vars[var+tipoSel+"Sel.id"]!, idInicial)}';
        document.getElementsByName('${var}${tipoSel}Sel.sigla')[0].value = '${(siglaInicial=="")?string(.vars[var+tipoSel+"Sel.sigla"]!, siglaInicial)}';
        document.getElementsByName('${var}${tipoSel}Sel.descricao')[0].value = '${(descricaoInicial=="")?string(.vars[var+tipoSel+"Sel.descricao"]!, descricaoInicial)}';
        [#if !ocultarDescricao]
        document.getElementById('${var}${tipoSel}SelSpan').innerHTML = '${(descricaoInicial=="")?string(.vars[var+tipoSel+"Sel.descricao"]!, descricaoInicial)}';
        [/#if]
    </script>
[/#macro]
[#macro selecionavel titulo var tipo reler=false idAjax="" default="" alerta=false obrigatorio=false relertab="" paramList=""]
    [#assign tipoSel = "_" + tipo /]

    [#assign varName = var + tipoSel + "Sel.id" /]
    [#local vId = .vars[varName]!default]
    <input type="hidden" name="vars" value="${varName}" />

    [#assign varName = var + tipoSel + "Sel.sigla" /]
    [#local vSigla = .vars[varName]!default]
    <input type="hidden" name="vars" value="${varName}" />

    [#assign varName = var + tipoSel + "Sel.descricao" /]
    [#local vDescricao = .vars[varName]!default]
    <input type="hidden" name="vars" value="${varName}" />

    [#if alerta && vId==""]
    [#list paramValues.obrigatorios as campo]
         [#if campo == var]
         [#local vermelho = "color:red"]
             [/#if]
        [/#list]
    [/#if]

    [#if obrigatorio]
    [#local negrito = "font-weight:bold"]
    <input type="hidden" name="obrigatorios" value="${var}" />
    [/#if]

    [#if titulo?? && titulo != ""]<span style="${negrito!};${vermelho!}">${titulo}:</span>[/#if]

    [#if !gerar_formulario!false]
        [@caixaSelecao titulo=titulo var=var tipo=tipo reler=reler idAjax=idAjax relertab=relertab paramList=paramList /]
    [#else]
    <span class="valor">[#if vSigla??]${vSigla} - [/#if]${vDescricao}</span>
    [/#if]
[/#macro]
[#macro pessoa titulo var reler=false relertab="" buscarFechadas=false idAjax="" default="" obrigatorio=false paramList=""]
    [#if buscarFechadas]
        [@assign paramList = "buscarFechadas=true" /]
    [/#if]
    [@selecionavel tipo="pessoa" titulo=titulo var=var reler=reler idAjax=idAjax relertab=relertab paramList=paramList obrigatorio=obrigatorio /]
[/#macro]
[#macro funcao titulo var reler=false relertab="" buscarFechadas=false idAjax="" default="" obrigatorio=false paramList=""]
    [@selecionavel tipo="funcao" titulo=titulo var=var reler=reler relertab=relertab paramList=paramList obrigatorio=obrigatorio /]
[/#macro]
[#macro lotacao titulo var reler=false relertab="" buscarFechadas=false idAjax="" default="" obrigatorio=false paramList=""]
    [@selecionavel tipo="lotacao" titulo=titulo var=var reler=reler relertab=relertab paramList=paramList obrigatorio=obrigatorio /]
[/#macro]
[#macro data titulo var reler=false idAjax="" default="" alerta=false obrigatorio=false]
    [#if reler == true && idAjax != ""]
            [#local jreler = " sbmt('" + idAjax + "');\""]
    [#elseif reler == true]
            [#local jreler = " sbmt();\""]
    [/#if]

    [#local v = .vars[var]!default]

[#if alerta!"" == 'Sim' && v==""]
    [#list paramValues.obrigatorios as campo]
         [#if campo == var]
         [#local vermelho = "color:red"]
             [/#if]
        [/#list]
    [/#if]

    [#if obrigatorio]
    [#local negrito = "font-weight:bold"]
    <input type="hidden" name="obrigatorios" value="${var}" />
    [/#if]

    [#if titulo?? && titulo != ""]<span style="${negrito!};${vermelho!}">${titulo}</span>[/#if]

    [#if !gerar_formulario!false]
        <input type="hidden" name="vars" value="${var}" />
        <input type="text" name="${var}" value="${v}" size="10" maxlength="10" onblur="javascript:verifica_data(this[#if alerta], 'Sim'[/#if]);${jreler!}" />
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
[#macro fixcrlf var=""]
    ${(var?replace("\r\f", "<br/>"))?replace("\n", "<br/>")} 
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
[#macro aberturaBIE]
    <!-- INICIO ABERTURA -->
        [#nested/]
    <!-- FIM ABERTURA -->
[/#macro]
[#macro corpoBIE]
    <!-- INICIO CORPO -->
        [#nested/]
    <!-- FIM CORPO -->
[/#macro]
[#macro fechoBIE]
    <!-- INICIO FECHO -->
        [#nested/]
    <!-- FIM FECHO -->
[/#macro]
[#macro assinaturaBIE]
    <!-- INICIO ASSINATURA -->
        [#nested/]
    <!-- FIM ASSINATURA -->
[/#macro]
[#macro numeroDJE]
    <!-- INICIO NUMERO -->
        [#nested/]
    <!-- FIM NUMERO -->
[/#macro]
[#macro mioloDJE]
    <!-- INICIO MIOLO -->
        [#nested/]
    <!-- FIM MIOLO -->
[/#macro]
[#macro tituloDJE]
    <!-- INICIO TITULO 
        [#nested/]
    FIM TITULO -->
[/#macro]
[#macro cabecalhoCentralizadoPrimeiraPagina]
<table style="float:none; clear:both;" width="100%" align="left" border="0" cellpadding="0"
    cellspacing="0" bgcolor="#FFFFFF">
    <tr bgcolor="#FFFFFF">
        <td width="100%">
        <table width="100%" border="0" cellpadding="2">
            <tr>
                <td width="100%" align="center" valign="bottom"><img src="contextpath/imagens/brasao2.png" width="65" height="65" /></td>
            </tr>
            <tr>
                <td width="100%" align="center">
                <p style="font-family: AvantGarde Bk BT, Arial; font-size: 11pt;">PODER JUDICIÁRIO</p>
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
[#macro cabecalhoCentralizado]
<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
    <tr bgcolor="#FFFFFF">
        <td width="100%">
        <table width="100%" border="0" cellpadding="2">
            <tr>
                <td width="100%" align="center">
                <p style="font-family: AvantGarde Bk BT, Arial; font-size: 11pt;">PODER JUDICIÁRIO</p>
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
                [/#if]<br />
                 </p>
                </td>
            </tr>
        </table>
        </td>
    </tr>
</table>
[/#macro]
[#macro rodapeClassificacaoDocumental somenteTR=false]
[#if !somenteTR]
<table align="left" width="100%" bgcolor="#FFFFFF">
[/#if]
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
[#if !somenteTR]
</table>
[/#if]
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
[#macro assinaturaCentro formatarOrgao=false]
[@assinaturaBIE]
<p style="font-family: Arial; font-size: 11pt;" align="center">
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
         
        [#if formatarOrgao]
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
                                [#if substituicao!false && ((doc.titular.idPessoa)!-1) != ((doc.subscritor.idPessoa)!-1)] EM EXERCÍCIO [/#if]
                        [#elseif (mov.subscritor.funcaoConfianca.nomeFuncao)??]
                            ${mov.subscritor.funcaoConfianca.nomeFuncao}
                        [#else]
                            ${(mov.subscritor.cargo.nomeCargo)!}
                        [/#if]
                        [#if formatarOrgao]
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
[/@assinaturaBIE]
[/#macro]
[#macro assinaturaMovCentro formatarOrgao=false]
<!-- INICIO ASSINATURA -->
<p style="font-family: Arial; font-size: 11pt;" align="center">
    [#list doc.mobilGeral.exMovimentacaoSet as movim]
        [#if movim.exTipoMovimentacao.idTpMov == 24 && ((mov.titular?? && movim.titular?? && mov.titular.idPessoa == movim.titular.idPessoa) || (mov.subscritor?? && movim.subscritor?? && mov.subscritor.idPessoa == movim.subscritor.idPessoa)) && movim.descrMov??]
            [local funcSubscrDoc = movim.descrMov /]
        [/#if]
    [/#list]
    <p align="center" style="font-family:Arial;font-size:11pt;">${(mov.subscritor.descricao)!}<br />
    [#if mov.nmFuncao??]
        ${mov.nmFuncao}
    [#elseif mov.titular?? && doc.titular?? && mov.titular.idPessoa == doc.titular.idPessoa && doc.nmFuncao??]
        ${doc.nmFuncao}
    [#elseif mov.subscritor?? && doc.subscritor?? && mov.subscritor.idPessoa == doc.subscritor.idPessoa && doc.nmFuncao??]
        ${doc.nmFuncao}
    [#elseif funcSubscrDoc??]
        ${funcSubscrDoc}
    [#elseif (mov.titular.funcaoConfianca.nomeFuncao)??]
        ${mov.titular.funcaoConfianca.nomeFuncao} ${(mov.titular.idPessoa != mov.subscritor.idPessoa)?string('EM EXERCÍCIO', '')}
    [#elseif (mov.subscritor.funcaoConfianca.nomeFuncao)??]
        ${mov.subscritor.funcaoConfianca.nomeFuncao}
    [#else]
        ${(mov.subscritor.cargo.nomeCargo)!}
    [/#if]
    <br/>
    [#if mov.nmLotacao??]
        ${mov.nmLotacao}
    [#else]
        ${(mov.titular.lotacao.nomeLotacao)!}
    [/#if]

    [#if textoFinal??]
    <br/>${textoFinal}
    [/#if]
</p>
<!-- FIM ASSINATURA -->
[/#macro]
[#macro estiloBrasaoAEsquerda tipo tamanhoLetra="11pt" obs=""]
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
        [#if obs != ""]
                <p>&nbsp;</p>
                ${obs}
        [/#if]
    [@primeiroRodape]
    [@rodapeClassificacaoDocumental/]
    [/@primeiroRodape]

    [@rodape]
    [@rodapeNumeracaoADireita/]
    [/@rodape]
[/#macro]
[#macro estiloBrasaoCentralizado tipo tamanhoLetra="11pt" formatarOrgao=true numeracaoCentralizada=false dataAntesDaAssinatura=false]
    [@primeiroCabecalho]
    [@cabecalhoCentralizadoPrimeiraPagina/]
    [/@primeiroCabecalho]

    [@cabecalho]
    [@cabecalhoCentralizado/]
    [/@cabecalho]

    [@letra tamanho=tamanhoLetra]
            [#if !numeracaoCentralizada]
            <table style="float:none; clear:both;" width="100%" border="0" bgcolor="#FFFFFF">
                <tr>
                                <td align="left"><p style="font-family:Arial;font-weight:bold;font-size:11pt;"><br/>[@numeroDJE]${tipo} N&ordm; ${(doc.codigo)!}[/@numeroDJE]</p></td>
                </tr>
                        [#if !dataAntesDaAssinatura]
                <tr>
                    <td align="right">[@letra tamanho="11pt"]<p>[#if mov??]${mov.dtExtenso!}[#else]${doc.dtExtenso!}[/#if]</p>[/@letra]</td>
                </tr>
                        [/#if]
            </table>
            [#else]
            <table style="float:none; clear:both;" width="100%" border="0" bgcolor="#FFFFFF">
                <tr>
                            <td align="center">
                                <p style="font-family:Arial;font-weight:bold;font-size:11pt;"><br/>
                                 [@numeroDJE]${tipo} N&ordm; ${(doc.codigo)!}[/@numeroDJE]
                                [#if !dataAntesDaAssinatura && doc?? && doc.dtD??]
                                    de ${doc.dtD} de ${doc.dtMMMM} de ${doc.dtYYYY}</p>
                                [/#if]
                            </td>
                </tr>
            </table>
            [/#if]
            [@tituloDJE]
		${(doc.codigo)!}
            [/@tituloDJE]
            [#nested]
            [#if dataAntesDaAssinatura]<p style="text-align:center">[#if mov??]${mov.dtExtenso!}[#else]${doc.dtExtenso!}[/#if]</p>[/#if]
        <p>&nbsp;</p>
            [#if mov??]
            [@assinaturaMovCentro formatarOrgao=formatarOrgao/]
            [#else]
            [@assinaturaCentro formatarOrgao=formatarOrgao/]
            [/#if]
    [/@letra]

    [@primeiroRodape]
    [@rodapeClassificacaoDocumental/]
    [/@primeiroRodape]

    [@rodape]
    [@rodapeNumeracaoADireita/]
    [/@rodape]
[/#macro]
[#macro memorando texto fecho="Atenciosamente," tamanhoLetra="Normal" _tipo="MEMORANDO"]
    [#if tamanhoLetra! == "Normal"]
        [#assign tl = "11pt" /]
    [#elseif tamanhoLetra! == "Pequeno"]
        [#assign tl = "9pt" /]
    [#elseif tamanhoLetra! == "Grande"]
        [#assign tl = "13pt" /]
    [#else]     
        [#assign tl = "11pt"]
    [/#if]

        [@estiloBrasaoCentralizado tipo=_tipo tamanhoLetra=tl formatarOrgao=false]
                <p align="left">
                     De: [#if (doc.nmLotacao)??]${doc.nmLotacao}[#else]${(doc.titular.lotacao.nomeLotacao)!}[/#if]<br/> 
                     Para: ${(doc.destinatarioString)!}<br>
                     Assunto: ${(doc.exClassificacao.descrClassificacao)!}</p>
        <span style="font-size: ${tl}"> ${texto!} </span>
                <p style="align: justify; TEXT-INDENT: 2cm">${fecho}</p>
        [/@estiloBrasaoCentralizado]
[/#macro]
[#macro portaria texto abertura="" tamanhoLetra="Normal" _tipo="PORTARIA" dispoe_sobre=""]
    [#if tamanhoLetra! == "Normal"]
        [#assign tl = "11pt" /]
    [#elseif tamanhoLetra! == "Pequeno"]
        [#assign tl = "9pt" /]
    [#elseif tamanhoLetra! == "Grande"]
        [#assign tl = "13pt" /]
    [#else]     
        [#assign tl = "11pt"]
    [/#if]
    [@estiloBrasaoCentralizado tipo=_tipo tamanhoLetra=tl formatarOrgao=false numeracaoCentralizada=true]
        [@mioloDJE]
            [#if dispoe_sobre != ""]     
              <table style="float:none;" width="100%" border="0" cellpadding="2" cellspacing="0" bgcolor="#FFFFFF">
                  <tr>
                      <td align="left" width="50%"></td>
                    <td align="left" width="50%" style="font-family: Arial; font-size: ${tl};"><br/>Dispõe sobre ${dispoe_sobre!}</td>
                  </tr>
              </table>
            [/#if]
            
            <div style="font-family: Arial; font-size: ${tl};">
                [#if abertura != ""] 
                    [@aberturaBIE]
                        ${abertura!}
                    [/@aberturaBIE]
                [/#if]
                [@corpoBIE]
                    ${texto!}
                [/@corpoBIE]
                <span style="font-family: Arial; font-size: ${tl}"><center>
                [@fechoBIE]
                    PUBLIQUE-SE. REGISTRE-SE. CUMPRA-SE.</center></span></p>
                [/@fechoBIE]
                </center></span></p>
            </div>            
        [/@mioloDJE]
     [/@estiloBrasaoCentralizado]
[/#macro]
[#macro quebraPagina]
<div style="PAGE-BREAK-AFTER: always"/>
[/#macro]
[#macro oficio _texto="" _tipo_autoridade="" _genero="" _vocativo="" _enderecamento_dest="" _nome_dest="" 
    _cargo_dest="" _orgao_dest="" _endereco_dest="" _fecho="" _tamanho_letra="" _autoridade={} _tipo="OFÍCIO"]
    [#if _autoridade?size > 0]
        [#local _vocativo=_autoridade.vocativo /]
        [#local _enderecamento_dest=_autoridade.enderecamento /]
        [#local _nome_dest=_autoridade.nome /]
        [#local _cargo_dest=_autoridade.cargo /]
        [#local _orgao_dest=_autoridade.orgao /]
        [#local _endereco_dest=_autoridade.endereco /]
    [/#if]
    [@entrevista]
        [#if _vocativo!="" && _enderecamento_dest!=""]
        [#elseif (_tipo_autoridade != "" && _genero != "")]
            [#assign tratamento = func.tratamento(_tipo_autoridade,_genero) /]
        [#else]
           [@grupo]
                [@grupo]
                    [#assign tipoAutoridade = tipoAutoridade!"[Nenhum]" /]
                    [@selecao titulo="Tipo de Autoridade" var="tipoAutoridade" opcoes="[Nenhum];Auditor da Justiça Militar;Bispo e Arcebispo;Cardeal;Chefe de Gabinete Civil;Chefe de Gabinete Militar da Presidência da República;Consultor-Geral da República;Corregedor do Tribunal Regional Federal;Dirigente administrativo e Procurador;Embaixador;Governador de Estado e do Distrito Federal;Juiz Federal;Juiz em geral;Membro do Congresso Nacional;Membro do Supremo Tribunal Federal;Membro do Tribunal Superior;Membro do Tribunal de Contas da União;Membro do Tribunal Regional Federal;Membro do Tribunal de Justiça;Membro da Assembléia Legislativa;Ministro de Estado;Monsenhor, Cônego;Prefeito;Presidente da República;Presidente do Supremo Tribunal Federal;Presidente do Tribunal Superior;Presidente do Tribunal Regional Federal;Presidente do Tribunal de Justiça;Presidente da Assembléia Legislativa;Presidente do Tribunal de Contas da União;Procurador-Geral da República;Procurador-Geral junto ao Tribunal;Secretário de Estado do Governo Estadual;Reitor de Universidade;Vice-Presidente da República;Oficial General das Forças Armadas;[Outros]" reler=true /]
                    [#if tipoAutoridade == "[Nenhum]" || func.verificaGenero(tipoAutoridade) == 'F']
                        [@selecao titulo="Gênero da Autoridade" var="genero" opcoes="M;F" reler=true /]
                    [#else]
                        [#assign genero = func.verificaGenero(tipoAutoridade) /]
                    [/#if]
    
                    [#if tipoAutoridade != "[Nenhum]"]
                        [#assign tratamento = func.tratamento(tipoAutoridade,genero) /]
                        [#if tratamento??]
                            [@grupo]
                                [@mensagem titulo="Forma de tratamento" texto="${tratamento.formaDeTratamento} (${tratamento.abreviatura})" /]
                            [/@grupo]
                        [/#if]
                    [/#if]
                [/@grupo]
            [/@grupo]
        [/#if]
        
        <!-- Vocativo -->
        [#if _vocativo != ""]
            [@oculto var="vocativo" valor="${_vocativo}" /]
        [#else]
            [#if tratamento??]
                [@grupo]
                    [@checkbox titulo="Especificar manualmente o vocativo" var="especificarVocativo" reler=true /]
                [/@grupo]
            [/#if]
            [#if (!tratamento??) || (especificarVocativo?? && especificarVocativo == 'Sim')]
                [@grupo]
                    [@texto titulo="Vocativo" var="vocativo" largura="45" /]
                [/@grupo]
            [#else]
                [@oculto var="vocativo" valor="${tratamento.vocativo}" /]
            [/#if]
        [/#if]
        
        <!-- Conteudo -->
        [#if _texto!=""]
            [@oculto var="texto_oficio" valor="${_texto}"/]
        [#else]
            [@grupo titulo="Texto a ser inserido no corpo do ofício"]
                [@grupo]
                    [@editor titulo="" var="texto_oficio" /]
                [/@grupo]
            [/@grupo]
        [/#if]

        <!-- Fecho -->
        [#if _fecho != ""]
            [@oculto var="fecho" valor="${_fecho}"/]
        [#else]
            [@grupo]
                [@texto titulo="Fecho (de acordo com o vocativo)" var="fecho" largura="50" /]
            [/@grupo]
        [/#if]
            [@grupo titulo="Dados do destinatário" esconder=(_enderecamento_dest!="" && _nome_dest!="" && _cargo_dest!="" && _orgao_dest!="" && _endereco_dest!="")]
                <!-- Tratamento -->
                [#if _enderecamento_dest != ""]
                    [@oculto var="enderecamento_dest" valor=_enderecamento_dest /]
                [#else]
                    [#if tratamento??]
                        [@grupo]
                            [@checkbox titulo="Especificar manualmente a forma de endereçamento" var="especificarEnderecamento" reler=true /]
                        [/@grupo]
                    [/#if]
                    [#if !tratamento?? || (especificarEnderecamento?? && especificarEnderecamento == 'Sim')]
                        [@grupo]
                            [@texto titulo="Forma de endereçamento" var="enderecamento_dest" largura="45" default="${(tratamento.formaDeEnderecamento)!}"/]
                        [/@grupo]
                    [#else]
                        [@oculto var="enderecamento_dest" valor="${(tratamento.formaDeEnderecamento)!}" /]
                    [/#if]
                [/#if]
    
                <!-- Nome -->
                [#if _nome_dest != ""]
                    [@oculto var="nome_dest" valor="${_nome_dest}"/]
                [#else]
                    [#if (doc.destinatario.descricao)??]
                        [@grupo]
                            [@checkbox titulo="Especificar manualmente o nome do destinatário" var="especificarNome" reler=true /]
                        [/@grupo]
                    [/#if]
                    [#if !(doc.destinatario.descricao)?? || (especificarNome?? && especificarNome == 'Sim')]
                        [@grupo]
                            [@texto titulo="Nome" var="nome_dest" largura="60" default="${(doc.destinatario.descricao)!}"/]
                        [/@grupo]
                    [#else]
                        [@oculto var="nome_dest" valor="${(doc.destinatario.descricao)!}" /]
                    [/#if]
                [/#if]
    
                <!-- Cargo -->
                [#if _cargo_dest != ""]
                    [@oculto var="cargo_dest" valor="${_cargo_dest}"/]
                [#else]
                    [#if (doc.destinatario.cargo.nomeCargo)??]
                        [@grupo]
                            [@checkbox titulo="Especificar manualmente o cargo do destinatário" var="especificarCargo" reler=true /]
                        [/@grupo]
                    [/#if]
                    [#if !(doc.destinatario.cargo.nomeCargo)?? || (especificarCargo?? && especificarCargo == 'Sim')]
                        [@grupo]
                            [@texto titulo="Cargo" var="cargo_dest" largura="60" default="${(doc.destinatario.cargo.nomeCargo)!}" /]
                        [/@grupo]
                    [#else]
                        [@oculto var="cargo_dest" valor="${(doc.destinatario.cargo.nomeCargo)!}" /]
                    [/#if]              
                [/#if]              
    
                <!-- Órgão -->
                [#if _orgao_dest != ""]
                    [@oculto var="orgao_dest" valor="${_orgao_dest}"/]
                [#else]
                    [#if (doc.lotaDestinatario.descricao)??]
                        [#assign orgaoAux = (doc.lotaDestinatario.descricao)! /]
                    [#else]
                        [#if (orgaoExternoDestinatarioSel.descricao)??]
                            [#assign orgaoAux = orgaoExternoDestinatarioSel.descricao /]
                        [/#if]
                    [/#if]
                    [#if orgaoAux??]
                        [@grupo]
                            [@checkbox titulo="Especificar manualmente o órgão do destinatário" var="especificarOrgao" reler=true /]
                        [/@grupo]
                    [/#if]
                    [#if !orgaoAux?? || (especificarOrgao?? && especificarOrgao == 'Sim')]
                       [@grupo]
                            [@texto titulo="Órgão" var="orgao_dest" largura="60" default="${orgaoAux!}" /]
                       [/@grupo]
                    [#else]
                        [@oculto var="orgao_dest" valor="${orgaoAux}" /]
                    [/#if]
                [/#if]
    
                [#if _endereco_dest != ""]
                    [@oculto var="endereco_dest" valor="${_endereco_dest}"/]
                [#else]
                    [@memo var="endereco_dest" titulo="Endereço" colunas="60" linhas="4" /]
                [/#if]
            [/@grupo]
        [#if _tamanho_letra != ""]
            [@oculto var="tamanhoLetra" valor="${_tamanho_letra}"/]
        [#else]
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

        [#if !(func.contains(fecho!'','.')) && !(func.contains(fecho!'',','))]
            [#assign virgula ="," /]
        [/#if]
        [@estiloBrasaoCentralizado tipo=_tipo tamanhoLetra=tl formatarOrgao=true]
            <table width="100%" border="0" cellpadding="2" cellspacing="0" bgcolor="#FFFFFF">
                <tr>
                    <td align="left">
                        <table align="left" width="100%" border="0" cellpadding="2" cellspacing="0">
                            <tr><td>[@letra tamanho=tl]<p>${enderecamento_dest!}</p>[/@letra]</td></tr>
                            <tr><td>[@letra tamanho=tl]<p>${nome_dest!}</p>[/@letra]</td></tr>
                            [#if cargo_dest??]
                                <tr><td>[@letra tamanho=tl]<p>${cargo_dest!}</p>[/@letra]</td></tr>
                            [/#if]
                            [#if orgao_dest??]
                                <tr><td>[@letra tamanho=tl]<p>${orgao_dest!}</p>[/@letra]</td></tr>
                            [/#if]
                            [#if endereco_dest??]
                                <tr><td>[@letra tamanho=tl]<p>[@fixcrlf var=endereco_dest! /]</p>[/@letra]</td></tr>
                            [/#if]
                            [#if (doc.exClassificacao.descrClassificacao)??]
                                <tr><td>[@letra tamanho=tl]<p><br/>Assunto: ${(doc.exClassificacao.descrClassificacao)!}</p>[/@letra]</td></tr>
                            [/#if]
                        </table>
                    </td>
                </tr>
            </table>
            [@mioloDJE]
                <div style="font-family: Arial; font-size: 10pt;">
                <p>&nbsp;</p>
                [@corpoBIE]
                    [#if vocativo??]
                        <p align="left" style="TEXT-INDENT: 2cm">${vocativo!},</p>
                    [/#if]
                    ${texto_oficio!}
                [/@corpoBIE]
                [#if fecho??]<p style="font-size: ${tl}; TEXT-INDENT: 2cm">[@fechoBIE]${fecho!}${virgula!}[/@fechoBIE][/#if]</p>
                </div>
           [/@mioloDJE]
        [/@estiloBrasaoCentralizado]
    [/@documento]
[/#macro]
[#macro identificacao pessoa funcao="" nivelHierarquicoMaximoDaLotacao="" obs="" negrito="nao"]
    [#if pessoa?? && pessoa != ""] 		
        [#if negrito == "nao"]
	    ${pessoa.descricao}, 
        [#else]
            <b>${pessoa.descricao}</b>, 
        [/#if]
        [#if negrito == "total"]   
            matrícula n&ordm; <b>${pessoa.sigla}</b>, <b>${pessoa.cargo.nomeCargo}</b>, classe <b>${pessoa.padraoReferencia}</b>, 
	    [#if obs != ""]
	         ${obs},
            [/#if]
            lotado(a) no(a) <b>${(nivelHierarquicoMaximoDaLotacao?? && nivelHierarquicoMaximoDaLotacao != "")?string(func.lotacaoPorNivelMaximo(pessoa.lotacao, nivelHierarquicoMaximoDaLotacao?number).descricao,pessoa.lotacao.descricao)}</b>,
            [#if funcao?? && funcao == "sim" && pessoa.funcaoConfianca != ""] 
                ocupante do(a) cargo em comissão/função comissionada de <b>${pessoa.funcaoConfianca.descricao}</b>,
            [/#if]          
        [#else]
             matrícula n&ordm; ${pessoa.sigla}, ${pessoa.cargo.nomeCargo}, classe ${pessoa.padraoReferencia}, 
	    [#if obs != ""]
	         ${obs},
            [/#if]
            lotado(a) no(a) ${(nivelHierarquicoMaximoDaLotacao?? && nivelHierarquicoMaximoDaLotacao != "")?string(func.lotacaoPorNivelMaximo(pessoa.lotacao, nivelHierarquicoMaximoDaLotacao?number).descricao,pessoa.lotacao.descricao)},
            [#if funcao?? && funcao == "sim" && pessoa.funcaoConfianca?? && pessoa.funcaoConfianca != ""] 
               ocupante do(a) cargo em comissão/função comissionada de ${pessoa.funcaoConfianca.descricao},
            [/#if]          
	[/#if]
    [/#if]
[/#macro]
[#assign _presidente = {
    "genero":"M", 
    "vocativo":"Excelentíssimo Senhor", 
    "vocativo_carta":"Exmo. Sr. Juiz Federal - Diretor do Foro", 
    "enderecamento":"Exmo. Sr. Dr.", 
    "nome":"<DEFINIR_NOME>", 
    "cargo":"<DEFINIR_CARGO>",
    "orgao":"<DEFINIR_ORGAO>",
    "endereco":"Avenida Almirante Barroso, 78 / 13º andar - Centro - Rio de Janeiro/RJ - CEP: 20031-004"} /]
[#assign _secretario_geral = {
    "genero":"F", 
    "vocativo":"Senhora", 
    "vocativo_carta":"Sra. Diretora da Secretaria Geral", 
    "enderecamento":"Sra. Dra.", 
    "nome":"<DEFINIR_NOME>", 
    "cargo":"<DEFINIR_CARGO>",
    "orgao":"<DEFINIR_ORGAO>",
    "endereco":"Avenida Almirante Barroso, 78  - Centro - Rio de Janeiro/RJ - CEP: 20031-004"} /]
[#assign _secretario_rh = {
    "genero":"F", 
    "vocativo":"Senhora", 
    "vocativo_carta":"Sra. Diretora da Subsecretaria de Gestão de Pessoas", 
    "enderecamento":"Sra. Dra.", 
    "nome":"<DEFINIR_NOME>", 
    "cargo":"<DEFINIR_CARGO>",
    "orgao":"<DEFINIR_ORGAO>",
    "endereco":"Avenida Almirante Barroso, 78  - Centro - Rio de Janeiro/RJ - CEP: 20031-004"} /]
[#assign enderecamentoPresidente = "Exmo. Sr. Juiz Federal - Diretor de Foro" /]
[#assign enderecamentoDiretorGeral = "Ilmo(a). Sr(a). Diretor(a)-Geral" /]
[#assign enderecamentoDiretorDeRH = "Ilma. Sra. Diretora da Subsecretaria de Gestão de Pessoas" /]
[#macro dadosComplementares][/#macro]
[#macro extensaoBuscaTextual][/#macro]
[#macro extensaoEditor nomeExtensao conteudoExtensao]
   [@editor titulo="" var=nomeExtensao /]
[/#macro]
[#macro extensaoAssinador][/#macro]
[#macro complementoHEAD][/#macro]
