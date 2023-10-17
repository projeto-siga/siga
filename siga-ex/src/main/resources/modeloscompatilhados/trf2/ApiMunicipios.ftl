[@entrevista]

    [#assign at=func.fetch('GET','https://servicodados.ibge.gov.br/api/v1/localidades/estados?orderBy=nome',null,null,60000)/]
[#assign at_json = at?eval_json/] 
[#assign unids = ""/]
[#list at_json as lista_api]
[#assign unids += lista_api.id + "*" + lista_api.nome + ";" /]
[/#list]
[#if unids != ""]
[#assign final = unids?length - 2 /]

[@selecaocomposta var="uf" titulo="UF" reler=true idAjax="preencheuf" mostrarSelecione=true opcoes="${unids[0..final]}" /]
    [/#if]    
    
 [@grupo depende="preencheuf"]
 

     
        [#assign att=func.fetch('GET','https://servicodados.ibge.gov.br/api/v1/localidades/estados/${uf}/municipios',null,null,60000)/]
         
         
         [#if att?has_content] 
          [#assign att_json = att?eval_json/]
          
        [#assign unids2 = ""/]
[#list att_json as lista_api2]
[#assign unids2 += lista_api2.nome + ";" /]
[/#list]
[#if unids2 != ""]
[#assign final = unids2?length - 2 /]

[#assign k = 1 /]
[@oculto var="nometeste"+k valor="10" /]

[@selecaocomposta var="mun" titulo="municipio" reler=true  mostrarSelecione=true opcoes="${unids2[0..final]}" /]
    [/#if]   
        [/#if]
    [/@grupo]    
    
[/@entrevista]

[@documento margemDireita="3cm"]
   [#assign tl = "11pt" /]
 
    [@estiloBrasaoCentralizado tipo="INFORMAÇÃO" tamanhoLetra=tl formatarOrgao=true numeracaoCentralizada=false dataAntesDaAssinatura =true]
        <div style="font-family: Arial; font-size: ${tl};">
            Assunto: ${(doc.exClassificacao.descrClassificacao)!}
           [#assign ufnome = .vars['uf_'+'${uf}'] /]
            <p style="TEXT-INDENT: 2cm"><span style="font-size: ${tl}">UF:  ${ufnome}, Município: ${mun}, Variável oculta: ${nometeste1}</span></p>
                
        </div>
    [/@estiloBrasaoCentralizado]
[/@documento]