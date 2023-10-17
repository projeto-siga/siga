[@entrevista]
<br><b><center>RESUMO DA LICITAÇÃO</center></b>


 [@grupo]
                   [@mensagem2 texto="ID da contratação no PNCP:"  cor="black" titulo=""/]
                   [@texto var="idlicitacao" titulo="" largura="24" reler="sim" relertab="sim" obrigatorio="Sim" /][@br/]
                   
[/@grupo]
[#if idlicitacao?has_content] 

[#assign orgao_contratacao = idlicitacao?keep_before("-") /]
[#assign ano_contratacao = idlicitacao?keep_after("/")  /]
[#assign parteid_contratacao = idlicitacao?keep_before("/")  /]
[#assign id_contratacao = parteid_contratacao?keep_after_last("-")  /]
[@oculto var="cnpj_orgao" valor='${orgao_contratacao!}' /]
[@oculto var="ano_da_contratacao" valor='${ano_contratacao!}' /]
[@oculto var="id_da_contratacao" valor='${id_contratacao!}' /]

[#if orgao_contratacao?has_content]

[#assign linkapiitens=func.fetch('GET','https://pncp.gov.br/api/pncp/v1/orgaos/${orgao_contratacao}/compras/${ano_contratacao}/${id_contratacao}/itens',null,null,60000)/]
[#if linkapiitens?has_content] 
[#assign linkapiitens_json = linkapiitens?eval_json/] 

[#list linkapiitens_json as lista_apiitens]
[#assign k = lista_apiitens.numeroItem  /]
  [@oculto var="descitem"+k valor='${lista_apiitens.descricao!}' /]
  [@oculto var="numitem"+k valor='${lista_apiitens.numeroItem!}' /]
  [@oculto var="unidademedida"+k valor='${lista_apiitens.unidadeMedida!}' /]
  [@oculto var="itemquant"+k valor='${lista_apiitens.quantidade!}' /]
  [@oculto var="itemvalortotal_estimado"+k valor='${lista_apiitens.valorTotal!}' /]
  [@oculto var="itemvalorunit_estimado"+k valor='${lista_apiitens.valorUnitarioEstimado!}' /]
  [@oculto var="itemCriterioJulgamento"+k valor='${lista_apiitens.criterioJulgamentoNome!}' /]
  [@oculto var="itemsituacaocompra"+k valor='${lista_apiitens.situacaoCompraItemNome!}' /]
  [@oculto var="itemtemresultado"+k valor='${lista_apiitens.temResultado?string("Sim", "Não")!}' /]
   
  [#assign cont_temResultado = lista_apiitens.temResultado?string("Sim", "Não")]
  
 [#if cont_temResultado = "Sim"]  
  
  [#assign linkapiitensresultado=func.fetch('GET','https://pncp.gov.br/api/pncp/v1/orgaos/${orgao_contratacao}/compras/${ano_contratacao}/${id_contratacao}/itens/${k}/resultados',null,null,60000)/]
  [#if linkapiitensresultado?has_content]
  [#assign linkapiitensresultado_json = linkapiitensresultado?eval_json/]
  [#list linkapiitensresultado_json as lista_itensresultado]
  [@oculto var="item_data_resultado"+k valor='${lista_itensresultado.dataResultado!}' /]
  [@oculto var="item_id_fornecedor"+k valor='${lista_itensresultado.niFornecedor!}' /]
  [@oculto var="item_fornecedor_tipo"+k valor='${lista_itensresultado.tipoPessoa!}' /] 
  [@oculto var="item_fornecedor_nome"+k valor='${lista_itensresultado.nomeRazaoSocialFornecedor!}' /]
  [@oculto var="item_fornecedor_naturezajur"+k valor='${lista_itensresultado.naturezaJuridicaNome!}' /]
  [@oculto var="item_fornecedor_porte"+k valor='${lista_itensresultado.porteFornecedorNome!}' /] 
  [@oculto var="item_valorunitariohomolog"+k valor='${lista_itensresultado.valorUnitarioHomologado!}' /] 
  [@oculto var="item_valortotalhomolog"+k valor='${lista_itensresultado.valorTotalHomologado!}' /] 
  [@oculto var="item_quantidade_homolog"+k valor='${lista_itensresultado.quantidadeHomologada!}' /]
  [@oculto var="item_sit_resultado"+k valor='${lista_itensresultado.situacaoCompraItemResultadoNome!}' /]
  [/#list] 
  [/#if]
  
[/#if]  
[/#list]  
 [@oculto var="tamanho" valor='${k}' /] 
   
[/#if]  
[/#if] 



[#assign linkapi=func.fetch('GET','https://pncp.gov.br/api/pncp/v1/orgaos/${orgao_contratacao}/compras/${ano_contratacao}/${id_contratacao}',null,null,60000)/]
[#if linkapi?has_content] 
[#assign linkapi_json = linkapi?eval_json/]
[@oculto var="numerocompra" valor='${linkapi_json.numeroCompra!}' /]
[@oculto var="objetocomp" valor='${linkapi_json.objetoCompra!}' /]
[@oculto var="valorestimado" valor='${linkapi_json.valorTotalEstimado!}' /]

[#if linkapi_json.valorTotalHomologado?has_content] 
[@oculto var="valorhomologado" valor='${linkapi_json.valorTotalHomologado!}' /]
[#else]
[@oculto var="valorhomologado" valor="0" /]
[@oculto var="mensagem_valorhomologado" valor=" - Contratação não homologada" /]
[/#if]
[@oculto var="amparolegal" valor='${linkapi_json.amparoLegal.nome!}' /]
[@oculto var="descamparolegal" valor='${linkapi_json.amparoLegal.descricao!}' /]
[@oculto var="datapublicacao" valor='${linkapi_json.dataPublicacaoPncp!}' /]
[@oculto var="modalidade" valor='${linkapi_json.modalidadeNome!}' /]
[@oculto var="sitcompra" valor='${linkapi_json.situacaoCompraNome!}' /]
[@oculto var="orgaocontratante" valor='${linkapi_json.orgaoEntidade.razaoSocial!}' /]
[@oculto var="srp" valor='${linkapi_json.srp?string("Sim", "Não")!}' /]

        
 [/#if]



 [/#if]
[/@entrevista]

[@description]																																	
<p>Resumo da contratação nº [@print	expr=(idlicitacao)/] conforme dados do Comprasnet.</p>																	
[/@description]

[@documento]
  [#assign tl = "11pt" /]
  
 
 [@estiloBrasaoCentralizado tipo="RESUMO DA CONTRATAÇÃO"  formatarOrgao=true numeracaoCentralizada=false dataAntesDaAssinatura =true]
       
           <b><center>DADOS DO COMPRASNET</center></b>
           <b><center>ID DA CONTRATAÇÃO PNCP: ${idlicitacao} </center></b>
            <br>
           <br>
                    
           <p style="TEXT-INDENT: 2cm"><b>Objeto:</b>  ${objetocomp!}</p> 
           <p style="TEXT-INDENT: 2cm"><b>Modalidade da licitação:</b>  ${modalidade!}</p>
           <p style="TEXT-INDENT: 2cm"><b>É Registro de Preço?</b>  ${srp!}</p>
           <p style="TEXT-INDENT: 2cm"><b>Situação da Contratação:</b>  ${sitcompra!}</p>
           <p style="TEXT-INDENT: 2cm"><b>Valor total estimado: </b>[#assign valorestimado = (.vars['valorestimado']!)?replace('.', '')?replace(',', '.')?number][#assign valorestimado = valorestimado?string.currency] ${valorestimado!}</p>
          <p style="TEXT-INDENT: 2cm"><b>Valor total homologado:</b>[#assign valorhomologado = (.vars['valorhomologado']!)?replace('.', '')?replace(',', '.')?number] [#assign valorhomologado = valorhomologado?string.currency]  ${valorhomologado!} ${mensagem_valorhomologado!} </p>
           <p style="TEXT-INDENT: 2cm"><b>Data de publicação no PNCP:</b>  ${datapublicacao!}</p>  
           <p style="TEXT-INDENT: 2cm"> <b>Aparo Legal:</b>  ${amparolegal!}</p>
           <p style="TEXT-INDENT: 2cm"><b>Descrição dispositivo legal:</b>  ${descamparolegal!}</p> 
           <p style="TEXT-INDENT: 2cm"><b>CNPJ do órgão:</b>  ${cnpj_orgao!}</p> 
           <p style="TEXT-INDENT: 2cm"><b>Órgão:</b>  ${orgaocontratante!}</p>  
           <p style="TEXT-INDENT: 2cm"><b>Ano da Contratação:</b>  ${ano_da_contratacao!}</p>
            <p style="TEXT-INDENT: 2cm"><b>Sequencial no PNCP:</b>  ${id_da_contratacao!}</p>
           <p style="TEXT-INDENT: 2cm"><b>Número da Compra:</b>  ${numerocompra!}</p>
           
           
        <br>
        <br>
           <b><center>LISTA DE ITENS</center></b>
           <br>
           <br>
           [#list 1..(.vars['tamanho'])?number as k] 
           <table border="1" width="100%" cellpadding="0">
                                     <thead>
                                           <tr>
                                              <td width="25%" bgcolor="lightblue" style="font-family:Arial;font-size:${tl};"  align="center" colspan="2"><b>Item número ${.vars['numitem'+k]!}</b></td>
                                             
                                           </tr>                                         
                                     </thead> 
                                     <tbody>                                
                                                                                     
                                                  <tr>                                                        
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: bold" align="center">Descrição</td>
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: normal" align="center">${.vars['descitem'+k]!}</td>
                                                     
                                                                                                                                                 
                                                  </tr>  
                                                  <tr>                                                        
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: bold" align="center">Unidade de Medida</td>
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: normal" align="center">${.vars['unidademedida'+k]!}</td>
                                                
                                                                                                                                                 
                                                  </tr>  
                                                  <tr>                                                        
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: bold" align="center">Quantidade</td>
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: normal" align="center">${.vars['itemquant'+k]!}</td>
                                                    
                                                                                                                                                 
                                                  </tr> 
                                                  <tr>                                                        
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: bold" align="center">Valor unitário estimado</td>
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: normal" align="center">[#assign valorunititem = (.vars['itemvalorunit_estimado'+k]!)?replace('.', '')?replace(',', '.')?number][#assign valorunititem = valorunititem?string.currency] ${valorunititem}</td>
                                                    
                                                                                                                                                 
                                                  </tr>  
                                                  <tr>                                                        
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: bold" align="center">Valor Total estimado</td>
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: normal" align="center">[#assign valortotitem = (.vars['itemvalortotal_estimado'+k]!)?replace('.', '')?replace(',', '.')?number][#assign valortotitem = valortotitem?string.currency] ${valortotitem}</td>
                                                   
                                                                                                                                                 
                                                  </tr>  
                                                  <tr>                                                        
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: bold" align="center">Critério de Julgamento</td>
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: normal" align="center">${.vars['itemCriterioJulgamento'+k]!}</td>
                                                     
                                                                                                                                                 
                                                  </tr>  
                                                  <tr>                                                        
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: bold" align="center">Situação da contratação do item</td>
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: normal" align="center">${.vars['itemsituacaocompra'+k]!}</td>
                                                   
                                                                                                                                                 
                                                  </tr>  
                                            [#if .vars['itemtemresultado'+k] = "Sim"]   
                                            
                                            <tr>                                                        
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: bold" align="center">Data do Resultado</td>
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: normal" align="center">${.vars['item_data_resultado'+k]!}</td>
                                                    
                                                                                                                                                 
                                                  </tr>  
                                                  <tr>                                                        
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: bold" align="center">CNPJ/CPF do Fornecedor</td>
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: normal" align="center">${.vars['item_id_fornecedor'+k]!}</td>
                                                    
                                                                                                                                                 
                                                  </tr>  
                                                  <tr>                                                        
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: bold" align="center">Tipo de Fornecedor</td>
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: normal" align="center">${.vars['item_fornecedor_tipo'+k]!}</td>
                                                   
                                                                                                                                                 
                                                  </tr>  
                                                  <tr>                                                        
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: bold" align="center">Nome do Fornecedor</td>
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: normal" align="center">${.vars['item_fornecedor_nome'+k]!}</td>
                                                 
                                                                                                                                                 
                                                  </tr>  
                                                  <tr>                                                        
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: bold" align="center">Natureza Jurídica do Fornecedor</td>
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: normal" align="center">${.vars['item_fornecedor_naturezajur'+k]!}</td>
                                                  
                                                                                                                                                 
                                                  </tr>  
                                                  <tr>                                                        
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: bold" align="center">Porte do fornecedor</td>
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: normal" align="center">${.vars['item_fornecedor_porte'+k]!}</td>
                                                    
                                                                                                                                                 
                                                  </tr>  
                                                  <tr>                                                        
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: bold" align="center">Valor unitário homologado</td>
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: normal" align="center">[#assign valor_unit_homolog_item = (.vars['item_valorunitariohomolog'+k]!)?replace('.', '')?replace(',', '.')?number][#assign valor_unit_homolog_item = valor_unit_homolog_item?string.currency] ${valor_unit_homolog_item}</td>
                                                     
                                                                                                                                                 
                                                  </tr>  
                                                  <tr>                                                        
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: bold" align="center">Valor total homologado</td>
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: normal" align="center">[#assign valor_tot_homolog_item = (.vars['item_valortotalhomolog'+k]!)?replace('.', '')?replace(',', '.')?number][#assign valor_tot_homolog_item = valor_tot_homolog_item?string.currency] ${valor_tot_homolog_item}</td>
                                                     
                                                                                                                                                 
                                                  </tr>  
                                                  <tr>                                                        
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: bold" align="center">Quantidade homologada</td>
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: normal" align="center">${.vars['item_quantidade_homolog'+k]!}</td>
                                                     
                                                                                                                                                 
                                                  </tr>  
                                                  <tr>                                                        
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: bold" align="center">Situação do resultado do item</td>
                                                     <td width="25%" style="font-family:Arial;font-size:${tl}; font-weight: normal" align="center">${.vars['item_sit_resultado'+k]!}</td>
                                                    
                                                                                                                                                 
                                                  </tr>  
                                            
                                            
                                            [/#if]                               
                                     </tbody>
                              </table> 
                                    <br>
                               [/#list] 
            <br>
             <br>
           <p style="TEXT-INDENT: 2cm"><b>Informações obtidas em:</b>  ${doc.dtDocDDMMYYYY}</p> 
      
    [/@estiloBrasaoCentralizado]
   
[/@documento]