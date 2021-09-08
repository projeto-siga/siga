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

[#function formatarCPF fmtCPF_param]
[#-- Início do comentário
Aplicação: Função para formatar um CPF
Acrônimo: fmtCPF_
Autor:    Ruben
Data:     13/03/2012
Descrição:
Esta função obtém uma string contendo o CPF a ser formatado e o devolve formatado
com a seguinte apresentação: 999.999.999-99  


Pré-condições:
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


[#function validarCPF vldCPF_cpfrecebido] 
[#--
Aplicação: Função para validar um CPF 
Versão:    1.0
Acrônimo:  vldCPF_
Autor:     Ruben
Data:      13/03/2012
Descrição:
A validação do CPF se dá pelo confronto dos dígitos verificadores passado a função e calculados. O cálculo baseia-se em aplicar o módulo 11 nos primeiros 9 dígitos do CPF, obtendo-se assim o primeiro dígito verificador. Depois aplica-se o módulo 11 novamente nos primeiros 9 dígitos e no primeiro dígito verificador.
O site http://br.answers.yahoo.com/question/index?qid=20060829190814AAHOqY6 possui uma descrição detalhada de como o algoritmo deve funcionar.

Condições de erro:
Se a string for nula ou o dígito verificador calculado não bater com o fornecido a funnçõa retornará False.

Pré-condições:
O CPF deve estar no formato xxx.xxx.xxx-xx 
 
Parâmetros recebidos:                                                        
+--------------+---------+--------+----------+-----------------------------------
| Parâmetro    |Tipo/Tam | Obrig. | Default  | Descrição
+--------------+---------+--------+----------+-----------------------------------
|vldCPF_param  | String  |  Sim   |          |  O CPF a ser validado no formato
|              |         |        |          |  xxx.xxx.xxx-xx   
+--------------+---------+--------+----------+-----------------------------------

Parâmetros retornados:                                                        
+--------------+---------+--------+----------+-----------------------------------
| Parâmetro    |Tipo/Tam | Obrig. | Default  | Descrição
+--------------+---------+--------+----------+-----------------------------------
|              | Boolean |  Sim   |          |  True ? CPF OK
|              |         |        |          |  False ? CPF inválido   
+--------------+---------+--------+----------+-----------------------------------

Chamada da function: 
A chamada nas aplicações poderá ser algo tipo:
  
  [#assign testeCPF = validarCPF(CPFaservalidado)/]
  [#if !testeCPF]
      Tratar o erro, pois o CPF fornecido é inválido
  [/#if]      

Variáveis:
vldCPF_cpfrecebido   O CPF passado a função ***** Não podemos alterar o parãmetro passado
vldCPF_cpf           Cópia do CPF passado a função para que possamos trabalhar nele
vldCPF_d1            Conterá a soma dos dígitos para o primeiro dígito verificador 
vldCPF_d2            Conterá a soma dos dígitos para o segundo dígito verificador
vldCPF_digito1       Conterá o primeiro dígito verificador
vldCPF_digito2       Conterá o segundo dígito verificador
vldCPF_resto         Utilizado para obter o reswto da divisão
vldCPF_digitoCPF     Conterá cada caracter lido do CPF passado a função 
vldCPF_tam           Conterá o tamanho da string do CPF passado sem formatação (. e -)
vldCPF_ndigverific   Conterá os dígitos verificadores passados a função 
vldCPF_ndigresult    Conterá os dígitos verificadores calculados (vldCPF_digito1 + 
                     vldCPF_digito2)

                                                                                              Fim comentário --]

[#if !vldCPF_cpfrecebido??] 
      [#return false]   
[/#if]  

[#assign vldCPF_d1 = 0/]
[#assign vldCPF_d2 = 0/]
[#assign vldCPF_digito1 = 0/]
[#assign vldCPF_digito2 = 0/]
[#assign vldCPF_resto = 0/]
[#assign vldCPF_digitoCPF = ""/]
[#assign vldCPF_ndigverific = ""/]
[#assign vldCPF_ndigresult = ""/]

[#-- Não podemos trabalhar em cima do vldCPF_cpfrecebido pois gera resultados estranhos, por isso é feito uma cópia dele para a variável vldCPF_cpf  --]
[#assign vldCPF_cpf = ""/]
[#assign vldCPF_cpf = vldCPF_cpfrecebido/]

[#-- Retirar . e ? do CPF                                                                                     --]
[#assign vldCPF_cpf = vldCPF_cpf?replace(".", "") /]
[#assign vldCPF_cpf = vldCPF_cpf?replace("-", "") /]


[#assign vldCPF_tam = vldCPF_cpf?length - 2/]
[#--Lógica                                                                                                     --]
[#list 1..vldCPF_tam  as vldCPF_ncount]
   
[#assign vldCPF_digitoCPF =  vldCPF_cpf?substring(vldCPF_ncount - 1, vldCPF_ncount)/]

[#--multiplique a ultima casa por 2 a seguinte por 3 a seguinte por 4 e assim por diante                       --]

   [#assign vldCPF_d1 = vldCPF_d1 + (11 - vldCPF_ncount) * vldCPF_digitoCPF?number /]

[#-- para o segundo digito repita o procedimento incluindo o primeiro digito calculado no passo anterior       --]

   [#assign vldCPF_d2 = vldCPF_d2 + (12 - vldCPF_ncount) * vldCPF_digitoCPF?number /]
[/#list]  

[#-- Primeiro resto da divisão por 11                                                                          --] 

[#assign vldCPF_resto = (vldCPF_d1 % 11) /]

[#--Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior             --] 

[#if vldCPF_resto < 2]
 [#assign vldCPF_digito1 = 0/]
[#else]
  [#assign vldCPF_digito1 = 11 - vldCPF_resto /]
[/#if]  

[#-- parte que ficou faltando para o segundo digito verificador                                                --]
[#assign vldCPF_d2 =  vldCPF_d2 + (2 * vldCPF_digito1)/]

[#-- Segundo resto da divisão por 11                                                                           --]
[#assign vldCPF_resto = (vldCPF_d2 % 11) /]

[#--Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior             --] 

[#if vldCPF_resto < 2]
 [#assign vldCPF_digito2 = 0/]
[#else]
  [#assign vldCPF_digito2 = 11 - vldCPF_resto /]
[/#if]  

[#-- Digito verificador do CPF que está sendo validado                                                         --]
[#assign vldCPF_ndigverific = vldCPF_cpf?substring(vldCPF_cpf?length - 2, vldCPF_cpf?length)/]

[#-- Concatenando o primeiro resto com o segundo                                                               --] 
[#assign vldCPF_ndigresult = vldCPF_digito1?string + vldCPF_digito2?string /]

[#-- comparar o digito verificador do cpf com o primeiro resto + o segundo resto                               --]
[#if vldCPF_ndigverific == vldCPF_ndigresult]
  [#return true]
[#else]
  [#return false]
[/#if]  

[/#function]  


[#function fmtvldCPF fmtvldCPF_param]
[#-- Início do comentário
Aplicação: Função para formatar e validar um CPF
Acrônimo: fmtvldCPF_
Autor:    Ruben
Data:     13/03/2012
Descrição:
Esta função obtém uma string contendo o CPF a ser formatado e validado e o devolve formatado
com a seguinte apresentação: 999.999.999-99  

Pré-condições:
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
será retornado ?E1? pela função, indicando que o CPF passado não pode ser formatado.
Se o dígito verificador calculado não bater com dígito passado será retornado ?E2? pela função, indicando que o CPF é inválido.

Parâmetros recebidos:                                                       
+----------------+-----------+-------------+----------+-------------------------------------------------
|  Parâmetro     | Tipo/Tam  | Obrigatório | Default  | Descrição
+----------------+-----------+-------------+----------+-------------------------------------------------
|fmtvldCPF_param | String    |  Sim        |          |  O CPF a ser formatado e validado
+----------------+-----------+-------------+----------+-------------------------------------------------
Parâmetros devolvidos:
+----------------+-----------+-------------+----------+-------------------------------------------------
| Parâmetro      | Tipo/Tam  | Obrigatório | Default  | Descrição
+----------------+-----------+-------------+----------+-------------------------------------------------
|                | String    |  Sim        |          |  O CPF formatado ou os seguintes tipos de erro:
|                |           |             |          |  E1 - O CPF passado não pode ser formatado
|                |           |             |          |  E2 - O CPF passado é inválido
+--------------+---------+--------+----------+----------------------------------------------------------

Chamada da function:
A chamada nas aplicações poderá ser algo tipo:

   [#assign antigoCPF = "47510846749"/]
   [#assign novoCPF = fmtvldCPF(antigoCPF)/]
   [#if novoCPF == "E1"]
      Tratar o erro, pois o CPF fornecido está inconsistente e não pode ser formatado
	[#elseif novoCPF == "E2"]
      Tratar o erro, pois o CPF fornecido é inválido, ou seja, possui dígitos verificadores inválidos 
	[#else]
      Utilizar o novoCPF	
   [/#if]     
 
 Fim comentário --]

[#if !fmtvldCPF_param??]
 [#return "E1"]
[/#if]  
 
[#assign fmtvldCPF_cpfrecebido = fmtvldCPF_param /]
[#assign fmtvldCPF_formatado = formatarCPF(fmtvldCPF_cpfrecebido)/]
[#if fmtvldCPF_formatado == "erro"]
     [#return "E1"]
[/#if] 

[#assign fmtvldCPF_validado = validarCPF(fmtvldCPF_formatado)/]
[#if !fmtvldCPF_validado]
     [#return "E2"]
[/#if]    

[#return fmtvldCPF_formatado]

[/#function]

[#macro dumpall]
[#local dump_var = "variável local criada para não gerar erro na rotina de listar as variáveis locais"/]

[#--
D D     U     U       M        M        P P    
D  D    U     U      M  M     M M       P  P    
D   D   U     U     M    M   M   M      P P     
D  D    U     U    M      M M     M     P      
D D     UUUUUUU   M        M       M    P       



DATA MODEL                               AMBIENTE DE EXECUÇÃO

root                                     +-------------+
  + Func                                 | Global      |
  |                                      +-------------+
  + Exbl
  |                                      +-------------+
  + Doc                                  | Main        |
  |                                      |             |  
  + Outra variáveis                      | +---------+ | 
                                         | | Local   | |
                                         | +---------+ |
                                         +-------------+

  
--]

   Cada template roda no NAMESPACE default chamado Main, onde constam as variáveis definidas via diretiva Assign [@br/]
   Nas macros e functions sao permitidas  a criação de variáveis locais via diretiva Local [@br/]
   As variáveis definidas via diretiva Global podem ser utilizadas em todo o ambiente do freemarker [@br/]
   O DATA MODEL e o Hash Root são idênticos [@br/]

[@separador/]
=================================== VARIÁVEIS DO DATA MODEL ============================================================ 
[@separador/]
[#list .data_model?keys as chave]
  [#assign key = chave! /]
  [#assign item = .data_model[key]! /]
  [@vertipo/]   [@br/]
[/#list]

[@separador/]

================================== VARIÁVEIS DO TEMPLATE SENDO EXECUTADO (NAMESPACE MAIN)=================================  
[@separador/]
[#list .main?keys as chave]
  [#assign key = chave! /]
  [#assign item = .main[key]! /]
  [@vertipo/]   [@br/]
[/#list]
[@separador/]

================================== VARIÁVEIS DO HASH ROOT (Idêntica do DATA_MODEL) ========================================  
[@separador/]
[#list root?keys as chave]
  [#assign key = chave! /]
  [#assign item = root[key]! /]
  [@vertipo/]   [@br/]
[/#list]
[@separador/]

================================== VARIÁVEIS DO HASH (DA CLASSE) FUNC  =====================================================  
[@separador/]
[#list func?keys as chave]
  [#assign key = chave! /]
  [#assign item = func[key]! /]
  [@vertipo/]   [@br/]
[/#list]
[@separador/]

================================== VARIÁVEIS DO HASH (DA CLASSE) EXBL  =====================================================  
[@separador/]
[#list exbl?keys as chave]
  [#assign key = chave! /]
  [#assign item = exbl[key]! /]
  [@vertipo/]   [@br/]
[/#list]
[@separador/]

================================== VARIÁVEIS DO HASH (DA CLASSE) DOC  =====================================================  
[@separador/]
[#list doc?keys as chave]
  [#assign key = chave! /]
  [#assign item = '?? (method)' /]
  [@vertipo/]   [@br/]
[/#list]
[@separador/]

================================== VARIÁVEIS DEFINIDAS COMO LOCAL  ==========================================================  
[@separador/]
[#list .locals?keys as chave]
  [#assign key = chave! /]
  [#assign item = (.locals[key])! /]
  [@vertipo/]   [@br/]
[/#list]

[/#macro]

[#macro vertipo]
[#if item?is_method]
   ${key} = ?? (method)
[#elseif item?is_enumerable]
   ${key} = ??  (enumerável)
[#elseif item?is_hash_ex]
   ${key} = ?? (hash)
[#elseif item?is_number]
   ${key} = ${item}
[#elseif item?is_string]
   ${key} = "${item}"
[#elseif item?is_boolean]
   ${key} = ${item?string}
[#elseif item?is_date]
   ${key} = ${item?string("yyyy-MM-dd HH:mm:ss zzzz")}
[#elseif item?is_transform]
   ${key} = ?? (transform)
[#elseif item?is_macro]
   ${key} = ?? (macro)
[#elseif item?is_hash]
   ${key} = ?? (hash)
[#elseif item?is_node]
   ${key} = ?? (node)
[#else]
   ${key} = ?? (tipo desconhecido)
[/#if]
[/#macro]


[#macro dump]
[#-- Início do comentário
Aplicação: Realiza um dump das variáveis do data-model
Autor    : Ruben
Data     : 13/08/2012
Descrição: É o dump simplificado da macro @dumpall 
--]
[#list .data_model?keys as chave]
   [#assign key = chave! /]
   [#assign item = .data_model[key]! /]
   [#if item?is_number]
     ${key} = ${item}
   [#elseif item?is_string]
     ${key} = "${item}"
   [#elseif item?is_boolean]
     ${key} = ${item?string}
   [#elseif item?is_date]
     ${key} = ${item?string("yyyy-MM-dd HH:mm:ss zzzz")}
   [/#if]
[/#list] 
[/#macro]

[#macro br]
    <br>
[/#macro]

[#macro atualizaoculto var valor="" default="1"]
[#-- Início do comentário
Aplicação: Atualiza o valor de um campo oculto criado pela macro @oculto
Autor:    Ruben
Data:     13/05/2012
Descrição: Rcebe o nome da variável e o valor que se deseja modificar, sendo "1" o default
--]
[#local v = (valor != "")?string(valor,.vars[var]!default) /]
<script type="text/javascript">
document.getElementById("${var}").value="${v}";
</script>
[/#macro]


[#macro atualizacampo varantigo varnovo]
[#-- Início do comentário
Aplicação: Atualiza o valor de um campo via DOM
Autor:    Ruben
Data:     13/05/2012
Descrição: Recebe o nome da variável que deve ser modificada e a variável com conteúdo novo
--]
<script type="text/javascript">
document.getElementsByName("${varantigo}")[0].value="${varnovo}";
</script>
[/#macro]


[#macro msgid idspan texto vermelho=false]
[#-- 
Aplicação: Cria um campo de mensagem com id para que se possa mudar o seu atributo dinamicamente
Autor:    Ruben
Data:     13/05/2012
Descrição: Recebe o id e o texto da mensagem para que se possa alterar o style.diplay para "none" ou "inline"
--]
<span id="idspan" style="[#if vermelho]color=#ff0000[/#if]"> ${texto!""}</span>
[/#macro]

[#macro memooculto var titulo colunas linhas reler=false obrigatorio=false default=""]
[#-- Início do comentário
Aplicação: Campo memo (textarea) oculto
Autor:    Ruben
Data:     13/08/2012
Descrição: Cria um campo memo (textarea) oculto
--]
[#if reler == true]
[#local jreler = " onchange=\"javascript: sbmt();\""]
[/#if]
[#local v = .vars[var]!default]
<input type="hidden" name="vars" value="${var}" />
[#if (alerta!"Não") = 'Sim' && v = ""]
    [#list obrigatorios?split(",") as campo]
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
[#if !gerar_formulario!false]
<textarea cols="${colunas}" rows="${linhas}" style="display: none" name="${var}" ${jreler!""}>${v}</textarea>
[#else]
<span class="valor">${v}</span>
[/#if]
</div>
[/#macro]

[#macro dumpvarantes]
[#-- Início do comentário
Aplicação: Realiza um dump antes da execução do template
Autor    : Ruben
Data     : 13/08/2012
Descrição: Esta macro é utilizada pelo Integrador
--]
[#assign datahoje = doc.getData()?substring(0,10)/]
[#assign horahoje = doc.getData()?substring(11,19)/]
[#assign fm_logDump]
================================================================================== 
DUMP INÍCIO	 do DATA-MODEL / Variáveis do Template  ${datahoje} às ${horahoje}
==================================================================================
-
---------------------------------
LISTAGEM DO DATA-MODEL
--------------------------------
LINHA  VARIÁVEL / CONTEÚDO
------       ------------------------------------------------
[#assign fm_i = 1/]
[#list .data_model?keys as chave]
   [#assign key = chave! /]
   [#assign item = .data_model[key]! /]
  [#if key != "template" && key != "fm_dumpAntesFm" && key != "fm_dumpDepoisFm" && key!= "fm_aplicacao" && key != "fm_divcarregar" && key != "fm_exibirinicial" && key != "fm_conteudo"]
   [#if item?is_number]
     ${fm_i?string("00000")} ${key} = ${item}  
     [#assign fm_i = fm_i + 1 /] 
   [#elseif item?is_string]
     ${fm_i?string("00000")}  ${key} = "${item}"  
     [#assign fm_i = fm_i + 1 /]  
   [#elseif item?is_boolean]
     ${fm_i?string("00000")}  ${key} = ${item?string}
     [#assign fm_i = fm_i + 1 /]  
   [#elseif item?is_date]
     ${fm_i?string("00000")}  ${key} = ${item?string("yyyy-MM-dd HH:mm:ss zzzz")} 
     [#assign fm_i = fm_i + 1 /]  
   [/#if]
  [/#if] 
[/#list]
-
-
---------------------------------------------
LISTAGEM DAS VARIÁVEIS DO TEMPLATE
--------------------------------------------
LINHA  VARIÁVEL / CONTEÚDO
------      ------------------------------------------------
[#assign fm_i = 1/]
[#list .main?keys as chave]
  [#assign key = chave! /]
  [#assign item = .main[key]! /]
  [#if key != "fm_logDump" && key != "fm_dumpAntesFm"]
   [#if item?is_number]
     ${fm_i?string("00000")} ${key} = ${item}  
     [#assign fm_i = fm_i + 1 /] 
   [#elseif item?is_string]
     ${fm_i?string("00000")}  ${key} = "${item}"  
     [#assign fm_i = fm_i + 1 /]  
   [#elseif item?is_boolean]
     ${fm_i?string("00000")}  ${key} = ${item?string}
     [#assign fm_i = fm_i + 1 /]  
   [#elseif item?is_date]
     ${fm_i?string("00000")}  ${key} = ${item?string("yyyy-MM-dd HH:mm:ss zzzz")} 
     [#assign fm_i = fm_i + 1 /]  
   [/#if]
  [/#if] 
[/#list]

[/#assign]
[@oculto var="fm_dumpAntesFm" valor="${fm_logDump?html}"/]
[@grupo titulo="DUMP"]
<input id="fm_verdumpantes" type=button onClick="fm_verDumpFm(1);" value='Início da execução'>
<span>    </span>
<input id="fm_verdumpdepois" type=button onClick="fm_verDumpFm(2);" value='Fim da execução'>
[@separador/]
[/@grupo]

<script type="text/javascript" language="javascript">
function fm_verDumpFm(tipoDump)
{
try 
{
if (tipoDump == 1)
{
newwinantes.close();
} // do if
if (tipoDump == 2)
{
newwindepois.close();
} 
} // do try  
catch(err) 
{
} 
if (tipoDump == 1)
{
meuString = document.getElementById("fm_dumpAntesFm").value;
meuArray = meuString.split('\n');
newwinantes = window.open('minhapagantes.htm','DATAINICIO','height=160,width=800,status=yes,toolbar=yes,scrollbars=yes,menubar=yes,location=yes');
for (i=0; i<meuArray.length; i++)
  {
  newwinantes.document.write(meuArray[i]);
  newwinantes.document.write('<br>');  
  }
} // do if tipoDump = 1
 
if (tipoDump == 2)
{
meuString = document.getElementById("fm_dumpDepoisFm").value;
meuArray = meuString.split('\n');   
newwindepois = window.open('minhapagdepois.htm','DATAFIM','height=160,width=800,status=yes,toolbar=yes,scrollbars=yes,menubar=yes,location=yes');
for (i=0; i<meuArray.length; i++)
  {
  newwindepois.document.write(meuArray[i]);
  newwindepois.document.write('<br>');  
  }
} // do if tipoDump = 2
} //da function
</script>
[/#macro]


[#macro dumpvardepois]
[#-- Início do comentário
Aplicação: Realiza um dump depois da execução do template
Autor    : Ruben
Data     : 13/08/2012
Descrição: Esta macro é utilizada pelo Integrador
--]
[#assign datahoje = doc.getData()?substring(0,10)/]
[#assign horahoje = doc.getData()?substring(11,19)/]
[#assign fm_logDump]
========================================================================== 
DUMP FIM do DATA-MODEL / Variáveis do Template  ${datahoje} às ${horahoje}
==========================================================================
-
----------------------
LISTAGEM DO DATA-MODEL
----------------------
LINHA  VARIÁVEL / CONTEÚDO
------ ------------------------------------------------
[#assign fm_i = 1/]
[#list .data_model?keys as chave]
   [#assign key = chave! /]
   [#assign item = .data_model[key]! /]
  [#if key != "template" && key != "fm_dumpAntesFm" && key != "fm_dumpDepoisFm" && key!= "fm_aplicacao" && key != "fm_divcarregar" && key != "fm_exibirinicial" && key != "fm_conteudo"]
   [#if item?is_number]
     ${fm_i?string("00000")} ${key} = ${item}  
     [#assign fm_i = fm_i + 1 /] 
   [#elseif item?is_string]
     ${fm_i?string("00000")}  ${key} = "${item}"  
     [#assign fm_i = fm_i + 1 /]  
   [#elseif item?is_boolean]
     ${fm_i?string("00000")}  ${key} = ${item?string}
     [#assign fm_i = fm_i + 1 /]  
   [#elseif item?is_date]
     ${fm_i?string("00000")}  ${key} = ${item?string("yyyy-MM-dd HH:mm:ss zzzz")} 
     [#assign fm_i = fm_i + 1 /]  
   [/#if]
  [/#if] 
[/#list]
-
-
------------------------------------------ 
LISTAGEM DAS VARIÁVEIS DO TEMPLATE
------------------------------------------
LINHA  VARIÁVEL / CONTEÚDO
------      ------------------------------------------------
[#assign fm_i = 1/]
[#list .main?keys as chave]
  [#assign key = chave! /]
  [#assign item = .main[key]! /]
  [#if key != "fm_logDump" && key != "fm_dumpAntesFm"]
   [#if item?is_number]
     ${fm_i?string("00000")} ${key} = ${item}  
     [#assign fm_i = fm_i + 1 /] 
   [#elseif item?is_string]
     ${fm_i?string("00000")}  ${key} = "${item}"  
     [#assign fm_i = fm_i + 1 /]  
   [#elseif item?is_boolean]
     ${fm_i?string("00000")}  ${key} = ${item?string}
     [#assign fm_i = fm_i + 1 /]  
   [#elseif item?is_date]
     ${fm_i?string("00000")}  ${key} = ${item?string("yyyy-MM-dd HH:mm:ss zzzz")} 
     [#assign fm_i = fm_i + 1 /]  
   [/#if]
  [/#if] 
[/#list]

[/#assign]
[@oculto var="fm_dumpDepoisFm" valor="${fm_logDump?html}"/]
[/#macro]

[#function descricaoComposta args...]
  [#if args?size = 1]
  	[#local args = args[0] /]
  [/#if]

  [#local descr = " " /]
  [#local primeiro = true /]
  [#list args as arg]
    [#if primeiro]
      [#local titulo = arg /]
      [#local primeiro = false/]
    [#else]
      [#if arg?has_content]
        [#if titulo?has_content]
          [#if arg?is_boolean]
            [#if arg]
              [#local descr =descr + " " + titulo + '.' /]
            [/#if]
          [#else]
            [#local descr =descr + " " + titulo + ': ' + arg + '.' /]
          [/#if]
        [#else]
          [#local descr =descr + " " + arg + '.' /]
        [/#if]
      [/#if]
      [#local primeiro = true/]
    [/#if]
  [/#list]
  [#return descr /]
[/#function]

[#function descricaoDefault args...]
  [#if doc.exModelo.nmMod?last_index_of(": ") > 0]
    [#local descr = doc.exModelo.nmMod?substring(doc.exModelo.nmMod?last_index_of(": ") + 2) /]
  [#else]
    [#local descr = doc.exModelo.nmMod /]
  [/#if]
  [#if doc.exTipoDocumento.id = 3 || doc.exTipoDocumento.id = 4]
    [#if doc.numExtDoc?has_content]
      [#local descr = descr + ' nº ' + doc.numExtDoc + '. ' /]
    [/#if]
    [#if doc.dtDocOriginalDDMMYYYY?has_content]
      [#local descr = descr + ' Data: ' + doc.dtDocOriginalDDMMYYYY + '. ' /]
    [/#if]
    [#if doc.orgaoExterno??]
      [#local descr = descr + ' Órgão: ' + doc.orgaoExterno.descricao + '. ' /]
    [#else]
      [#if doc.obsOrgao?has_content]
        [#local descr = descr + ' Órgão: ' + doc.obsOrgao + '. ' /]
      [/#if]
    [/#if]
    [#if doc.nmSubscritorExt?has_content]
      [#local descr = descr + ' Subscritor: ' + doc.nmSubscritorExt + '. ' /]
    [/#if]
  [/#if]
  [#if doc.subscritor??]
    [#local descr = descr + ' de ' + doc.subscritorString /]
  [/#if]
  [#if doc.lotaTitular??]
    [#local descr = descr + ' / ' + doc.lotaTitular.sigla /]
  [/#if]
  [#local descr = descr + descricaoComposta(args) /]
  [#return descr /]
[/#function]

[#macro retorna tag valor]
    <!-- ${tag} --><!--{${valor}}--><!-- /${tag} -->
[/#macro]

[#if gerar_descricaodefault!false]
	[@retorna tag="descricaodefault" valor=descricaoDefault() /]
[/#if]

[#macro entrevista acaoGravar="" acaoExcluir="" acaoCancelar="" acaoFinalizar="" descricao=""]
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
        [#if descricao!=""]
        	[@retorna tag="descricaoentrevista" valor=descricao /]
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
                    .footnotes {
                    	font-size:8pt !important;
                    	margin-top: 25pt !important;
                    }
                    .footnotes hr {
					   width: 25% !important;
					   border-top: 1px solid #000 !important;
					   text-align: left !important;
					   margin-left: 0 !important;
                    }
                    a.doc-sign {
						color: #000;
						text-decoration: none;
					}
					                    
                </style>
            </head>
            <body>
            	[#if func.resource('conversor.html.ext') == 'br.gov.jfrj.itextpdf.MyPD4ML']
			    	[#nested]
	            [#else]
					<div style="word-wrap: break-word" class="divDoc">
				     	[#nested]
		           	</div>
			    [/#if]
            </body>
        </html>
    [/#if]
[/#macro]

[#macro gravacao]
    [#if gerar_gravacao!false]
        [#nested]
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

[#macro pre_assinatura]
    [#if gerar_pre_assinatura!false]
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

[#macro grupo titulo="" largura=0 depende="" esconder=false atts={}]
    [#if !esconder]
    [#local id = (depende=="")?string("", "div" + depende)] 
    [@div id=id depende=depende suprimirIndependente=true atts=atts]
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
            [#if (grupoLarguraTotal >= 100)]
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

[#macro div id="" depende="" suprimirIndependente=false atts={}]
	[#assign attsHtml][#list atts?keys as k]${k}="${atts[k]}"[/#list][/#assign]
    [#if suprimirIndependente || depende != ""]
        <div[#if id != ""] id="${id}"[/#if][#if depende != ""] depende=";${depende};"[/#if] ${attsHtml}>[#if id != ""]<!--ajax:${id}-->[/#if][#nested][#if id != ""]<!--/ajax:${id}-->[/#if]</div>
    [#else]
    [#nested]
    [/#if]
[/#macro]

[#macro texto var titulo="" largura="" maxcaracteres="" idAjax="" reler="" relertab="" obrigatorio="nao" default="" atts={} onkeyup="" isCpf=false isCnpj=false ]
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
    
	<div class="form-group" style="margin-bottom:0">
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
	
		[#assign attsHtml][#list atts?keys as k]${k}="${atts[k]}"[/#list][/#assign]
	    [#if !gerar_formulario!false]    	
    		[#if titulo != ""]    			
    			<label for="${var}" style="${negrito!};${vermelho!}">${titulo}</label>
    		[/#if]
    		
       		<input type="text" id="${var}" name="${var}" value="${v}" ${jreler!""}${jrelertab!""} ${attsHtml} onkeyup="${onkeyup}" class="form-control" [#if isCpf]data-formatar-cpf="true" placeholder="000.000.000-00" maxlength="14" style="max-width: 150px"[#elseif isCnpj]data-formatar-cnpj="true" placeholder="00.000.000/000-00" maxlength="18" style="max-width: 180px"[#else]${jlargura!""}${jmaxcaracteres!""}[/#if]/>
       		<div class="invalid-feedback  invalid-feedback-${var}">Preenchimento obrigatório</div>	     	
	     	[#if isCpf]    
		     	<script>
		     		function aplicarMascaraCPF(evento) {	     			             
	            		cpf = this.value.replace(/([^\d])/g, '');
	
			            if (evento.type == 'change') {
			              while (cpf.length < 11) {
			                cpf = '0' + cpf;
			              }               
			            }
			            cpf = cpf.replace(/^(\d{3})(\d)/, '$1.$2');
			            cpf = cpf.replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3');
			            cpf = cpf.replace(/\.(\d{3})(\d)/, '.$1-$2');            
			            this.value = cpf;
		     		}
		     		document.querySelector('input[name=${var}]').addEventListener('input', aplicarMascaraCPF);
	      			document.querySelector('input[name=${var}]').addEventListener('change', aplicarMascaraCPF);
		     	</script>  
	     	[#elseif isCnpj]
	     		<script>
		     		function aplicarMascaraCNPJ(evento) {	     			             
	            		cnpj = this.value.replace(/([^\d])/g, '');
	
			            if (evento.type == 'change') {
			              while (cnpj.length < 14) {
			                cnpj = '0' + cnpj;
			              }
			            }
			            cnpj = cnpj.replace(/^(\d{2})(\d)/, '$1.$2');
			            cnpj = cnpj.replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3');
			            cnpj = cnpj.replace(/\.(\d{3})(\d)/, '.$1/$2');
			            cnpj = cnpj.replace(/(\d{4})(\d)/, '$1-$2');
			            this.value = cnpj;
		     		}
		     		document.querySelector('input[name=${var}]').addEventListener('input', aplicarMascaraCNPJ);
	      			document.querySelector('input[name=${var}]').addEventListener('change', aplicarMascaraCNPJ);
		     	</script> 
	     	[/#if] 
	    [#else]
	    <span class="valor">${v}</span>
	    [/#if]
	</div>	    
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
            [#local jreler = " sbmt('" + idAjax + "');"]
    [#elseif reler == true]
            [#local jreler = " sbmt();"]
    [/#if]

    [#if .vars[var]??]
    [#local v = .vars[var]/]
    [#else]
    [#local v = default/]
        [#assign inlineTemplate = ["[#assign ${var} = v/]", "assignInlineTemplate"]?interpret /]
        [@inlineTemplate/]
    [/#if]

	<div class="form-group" style="margin-bottom:0">
	    <input type="hidden" name="vars" value="${var}" />
	    <input type="hidden" id="${var}" name="${var}" value="${v}" />
	
	    [#if (alerta!"Não") = 'Sim' && v = ""]
	    [#list obrigatorios?split(",") as campo]
	         [#if campo == var]
	         [#local vermelho = "color:red"]
	             [/#if]
	        [/#list]
	    [/#if]
	
	    [#if obrigatorio]
	    [#local negrito = "font-weight:bold"]
	    <input type="hidden" name="obrigatorios" value="${var}_chk" />
	    [/#if]
	
	    [#if !gerar_formulario!false]    	
			<div class="form-check">
		        <input class="form-check-input" id="${id}" type="checkbox" name="${var}_chk" value="Sim"
		               [#if v=='Sim']checked[/#if] 
		               onclick="javascript: if (this.checked) document.getElementById('${var}').value = 'Sim'; else document.getElementById('${var}').value = '${default}'; ${onclique!""}; ${jreler!""}" [#if id == ""]data-criar-id="true"[/#if]/> 
		        <label class="form-check-label" for="${id}" style="${negrito!""};${vermelho!""}" [#if id == ""]data-nome-ref="${var}_chk"[/#if]>${titulo!""}</label>
		        [#if obrigatorio]
					<div class="invalid-feedback  invalid-feedback-${var}_chk">Preenchimento obrigatório</div>
				[/#if]		       
			</div>		
	    [#else]
	    <span class="valor">${v}</span>
	    [/#if]
	</div>	    
[/#macro]

[#macro radio titulo var reler=false idAjax="" default="Não" valor="Sim" onclique="" atts={} obrigatorio=false id=""]	
    [#if reler == true && idAjax != ""]
            [#local jreler = " sbmt('" + idAjax + "');"]
    [#elseif reler == true]
            [#local jreler = " sbmt();"]
    [/#if]

    [#local v = .vars[var]!(default == "Sim")?string(valor, "") /]

	<div class="form-group" style="margin-bottom:0">
	    [#if !.vars["temRadio_"+var]??]
	        <input type="hidden" name="vars" value="${var}" />
	        <input type="hidden" id="${var}" name="${var}" value="${v}" />
	        [#assign inlineTemplate = ["[#assign temRadio_${var} = true/]", "assignInlineTemplate"]?interpret /]
	        [@inlineTemplate/]
	    [/#if]
	    [#if v == valor]
	        <script>document.getElementById('${var}').value = '${valor}';</script>
	    [/#if]
	    
	    [#if obrigatorio]
	    	[#local negrito = "font-weight:bold"]
	    	<input type="hidden" name="obrigatorios" value="${var}_chk" />
	    [/#if]	    	   
	
		[#assign attsHtml][#list atts?keys as k]${k}="${atts[k]}"[/#list][/#assign]
	    [#if !gerar_formulario!false]        	
			<div class="custom-control custom-radio">
	        	<input class="custom-control-input" type="radio" id="${id}" name="${var}_chk" value="${valor}" [#if v == valor]checked[/#if] onclick="javascript: if (this.checked) document.getElementById('${var}').value = '${valor}'; ${onclique}; ${jreler!};" ${attsHtml} [#if id == ""]data-criar-id="true"[/#if]/>     			
				<label class="custom-control-label" for="${id}" style="${negrito!""};${vermelho!""}" [#if id == ""]data-nome-ref="${var}_chk"[/#if]>${titulo!""}</label>
				[#if obrigatorio]
					<div class="invalid-feedback  invalid-feedback-${var}_chk">Preenchimento obrigatório</div>
				[/#if]						
			</div>  			  
	    [#else]
	    <span class="valor">${v}</span>    
	    [/#if]
    </div>
[/#macro]

[#macro editor_antigo var titulo="" default=""]
    [#if .vars[var]??]
        [#local v = .vars[var]/]
    [#else]
        [#local v = ""/]
    [/#if]
    [#if v != ""]
        [#local v = exbl.canonicalizarHtml(v, false, true, false, true)/]
    [#else]
        [#local v = '<p style="text-indent:2cm; text-align: justify">&nbsp;</p>'/]
    [/#if]

        <div>
        [#if titulo != ""]
                        <b>${titulo}</b>
        [/#if]

        [#if !gerar_formulario!false]

            <input type="hidden" name="vars" value="${var}" />
            <input type="hidden" id="desconsiderarExtensao" name="desconsiderarExtensao" value="${desconsiderarExtensao!'false'}" />

                        [#if ( (func.podeUtilizarExtensaoEditor(lotaCadastrante, doc.exModelo.idMod?number)!false)
                           && (!((desconsiderarExtensao == 'true')!false)) )]
[#else]
<textarea id="${var}" name="${var}" class="form-control">${default!}${v?html}</textarea>
[/#if]
            <table class="entrevista" width="100%">
                <tr>
                    <td></td>
                    <td colspan="3">

                        
                         
                        [#if ( (func.podeUtilizarExtensaoEditor(lotaCadastrante, doc.exModelo.idMod?number)!false)
                           && (!((desconsiderarExtensao == 'true')!false)) )]
                             <input type="hidden" id="${var}" name="${var}" value="${v?html}">
                            [@extensaoEditor nomeExtensao=var conteudoExtensao=v/]
                        [#else]
                            
                            <script type="text/javascript">

CKEDITOR.config.scayt_autoStartup = true;
CKEDITOR.config.scayt_sLang = 'pt_BR';
CKEDITOR.config.height = '400';
CKEDITOR.config.stylesSet = 'siga_ckeditor_styles';



CKEDITOR.stylesSet.add('siga_ckeditor_styles',[
                                               {
                                                   name:'Título',
                                                   element:'h1',
                                                   styles:{
                                                           'text-align':'justify',
                                                           'text-indent':'2cm'
                                                                   }
                                               },
                                               {
                                                   name:'Subtítulo',
                                                   element:'h2',
                                                   styles:{
                                                           'text-align':'justify',
                                                           'text-indent':'2cm'
                                                                   }
                                               },
                                               {
                                                   name:'Com recuo',
                                                   element:'p',
                                                   styles:{
                                                           'text-align':'justify',
                                                           'text-indent':'2cm'
                                                                   }
                                               }]);
        CKEDITOR.config.toolbar = 'SigaToolbar';
 
        CKEDITOR.config.toolbar_SigaToolbar =
        [
                { name: 'styles', items : [ 'Styles' ] },
                { name: 'clipboard', items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] },
                { name: 'editing', items : [ 'Find','Replace','-','SelectAll','-','Scayt' ] },
                '/',
                { name: 'basicstyles', items : [ 'Bold','Italic','Underline','Strike','-','RemoveFormat' ] },
                { name: 'paragraph', items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','JustifyLeft','JustifyCenter','JustifyBlock','JustifyRight' ] },
                { name: 'insert', items : [ 'Table','-','SpecialChar','-','PageBreak' ] },
                { name: 'document', items : [ 'Source' ] }
        ];

window.onload = function(){
CKEDITOR.replace( '${var}',
        {
                toolbar : 'SigaToolbar'
        });
}
                            </script>
                            
                        [/#if]

                    </td>
                </tr>
            </table>
        [#else]
            <br>${v}<br><br>
        [/#if]
    </div>
[/#macro]

[#macro editor var titulo="" default=""]
    [#if .vars[var]??]
        [#local v = .vars[var]/]
    [#else]
        [#local v = ""/]
    [/#if]
    [#if v != ""]
        [#local v = exbl.canonicalizarHtml(v, false, true, false, true)/]
    [#else]
        [#local v = '<p style="text-indent:2cm; text-align: justify">&nbsp;</p>'/]
    [/#if]

        <div>
        [#if titulo != ""]
                        <b>${titulo}</b>
        [/#if]

        [#if !gerar_formulario!false]
            <input type="hidden" name="vars" value="${var}" />
            <input type="hidden" id="desconsiderarExtensao" name="desconsiderarExtensao" value="${desconsiderarExtensao!'false'}" />

                        [#if ( (func.podeUtilizarExtensaoEditor(lotaCadastrante, doc.exModelo.idMod?number)!false)
                           && (!((desconsiderarExtensao == 'true')!false)) )]
		[#else]
		<textarea id="${var}" name="${var}" class="editor"> ${default!}${v?html}</textarea>
		[/#if]
            <table class="entrevista" width="100%">
                <tr>
                    <td></td>
                    <td colspan="3">

                        
                         
                        [#if ( (func.podeUtilizarExtensaoEditor(lotaCadastrante, doc.exModelo.idMod?number)!false)
                           && (!((desconsiderarExtensao == 'true')!false)) )]
                             <input type="hidden" id="${var}" name="${var}" value="${v?html}">
                            [@extensaoEditor nomeExtensao=var conteudoExtensao=v/]
                        [#else]
                            <script type="text/javascript">

								CKEDITOR.config.disableNativeSpellChecker = false;
								CKEDITOR.config.scayt_autoStartup = false;
								CKEDITOR.config.scayt_sLang = 'pt_BR';
								CKEDITOR.config.stylesSet = 'siga_ckeditor_styles';
								
								CKEDITOR.stylesSet.add('siga_ckeditor_styles', [{
								        name: 'Título',
								        element: 'h1',
								        styles: {
								            'text-align': 'justify',
								            'text-indent': '2cm'
								        }
								    },
								    {
								        name: 'Subtítulo',
								        element: 'h2',
								        styles: {
								            'text-align': 'justify',
								            'text-indent': '2cm'
								        }
								    },
								    {
								        name: 'Com recuo',
								        element: 'p',
								        styles: {
								            'text-align': 'justify',
								            'text-indent': '2cm'
								        }
								    },
								    {
								        name: 'Marcador',
								        element: 'span',
								        styles: {
								        	'background-color' : '#FFFF00'
								        }
								    },
								    {
								        name: 'Normal',
								        element: 'span'
								    }
								]);
								CKEDITOR.config.toolbar = 'SigaToolbar';
								
								CKEDITOR.config.toolbar_SigaToolbar = [{
								        name: 'styles',
								        items: ['Styles']
								    },
								    {
								        name: 'clipboard',
								        items: ['Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'Undo', 'Redo']
								    },
								    {
								        name: 'editing',
								        items: ['Find', 'Replace', '-', 'SelectAll']
								    },
								    '/',
								    {
								        name: 'basicstyles',
								        items: ['Bold', 'Italic', 'Subscript', 'Underline', 'Strike', '-', 'RemoveFormat']
								    },
								    {
								        name: 'paragraph',
								        items: ['NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 'JustifyLeft', 'JustifyCenter', 'JustifyBlock', 'JustifyRight']
								    },
								    {
								        name: 'insert',
								        items: ['Table' , 'Footnotes', '-', 'SpecialChar', '-', 'PageBreak']
								    },
								    {
								        name: 'document',
								        items: ['Source']
								    }
								];
								CKEDITOR.config.extraPlugins = 'footnotes';
								window.onload = function() {
								    $("textarea.editor").each(function(index) {
								        CKEDITOR.replace(this, {
								            toolbar: 'SigaToolbar'
								        });
								    });
								}

                            </script>
                            
                        [/#if]

                    </td>
                </tr>
            </table>
        [#else]
            <br>${v}<br><br>
        [/#if]
    </div>
[/#macro]


[#macro selecao var titulo opcoes reler=false idAjax="" onclick="" pontuacao=":" atts={} opcaoNeutra="" obrigatorio=false]
    [#local l=opcoes?split(";")]
    [#if .vars[var]??]
        [#local v = .vars[var]/]
        [#local temValor = v]
    [#else]
        [#local v = l?first/]
        [#--assign .vars[var] = v / --]
        		[#--Edson: voltei a comentar este trecho, pois não identifiquei pra que serve, e está dando erro com o Angular --]
               	 [#assign inlineTemplate = ["[#assign ${var} = v/]", "assignInlineTemplate"]?interpret /]
              	[@inlineTemplate/] 
    [/#if]
            
	[#assign attsHtml][#list atts?keys as k]${k}="${atts[k]}"[/#list][/#assign]

    [#if !gerar_formulario!false]    
    	<div class="form-group" style="margin-bottom:0">
    		[#if titulo?? && titulo != ""]<label for="${var}" [#if obrigatorio]style="font-weight:bold"[/#if]>${titulo}</label>[/#if]  
    		<select id="${var}" name="${var}" [#if reler] onchange="javascript: sbmt([#if idAjax != ""]'${idAjax}'[/#if]);"[/#if] onclick="${onclick}" class="form-control" ${attsHtml}>
    			[#if opcaoNeutra?? && opcaoNeutra != "" && obrigatorio]
    				<option id="opcaoNeutra" value="${opcaoNeutra}" [#if !(temValor??)]selected[/#if]>${opcaoNeutra}</option>
    			[/#if]
                [#list l as opcao]
                    <option value="${opcao}" [#if v == opcao && (opcaoNeutra == "" || (temValor?? && temValor != ""))] selected[/#if]>${opcao}</option><br/>
            	[/#list]
        	</select> 
        	[#if obrigatorio]            		    
				<div class="invalid-feedback  invalid-feedback-${var}">Selecione um opção</div>
				<input type="hidden" name="obrigatorios" value="${var}" />
			[/#if]    
			<input type="hidden" name="vars" value="${var}" />                  	    	    	  	   		   								    			 				
		</div>                
    [#else]
        <span class="valor">${v}</span>
    [/#if]
[/#macro]


[#macro selecaocomposta var titulo opcoes reler=false idAjax="" onclick="" pontuacao=":" mostrarSelecione=false multiplo=false tamanho="1" disabled=false default=""]
    [#local l=opcoes?split(";")]
    [#if .vars[var]??]
    	[#assign estamosRelendo = true /]
        [#local v = .vars[var]/]
    [#else]
    	[#assign estamosRelendo = false /]
        [#local v = l?first/]
		[#assign inlineTemplate = ["[#assign ${var} = v/]", "assignInlineTemplate"]?interpret /]
		[@inlineTemplate/]
	[/#if]
    
	${titulo!""}[#if titulo != ""]${pontuacao!""}[/#if]

    [#if !gerar_formulario!false]
        <input type="hidden" name="vars" value="${var}" />
        [#assign hiddens = "" /]
        <select name="${var}" [#if reler] dados="${v}" onchange="javascript: sbmt([#if idAjax != ""]'${idAjax}'[/#if]);"[/#if] onclick="${onclick}"[#if multiplo] multiple size="${tamanho}"[/#if][#if disabled] disabled[/#if]>
        [#if mostrarSelecione && !estamosRelendo]<option>Selecione...</option>[/#if]
			[#list l as opcao]
            	[#local tupla=opcao?split("*") /]
				[#local opcaoValue = tupla?first /]
				[#local opcaoOption = tupla?last /]
				
                [#assign selecionado = false /]
                [#if !(v?contains(",")) && (opcaoValue == v || opcaoValue == default)]
                	[#assign selecionado = true /]
                [/#if]
				[#if v?contains(",") && (v?contains(",${opcaoValue},") || v?starts_with("${opcaoValue},") || v?ends_with(",${opcaoValue}"))]
                	[#assign selecionado = true /]
                [/#if]
                <option[#if selecionado] selected[/#if] value="${opcaoValue}">${opcaoOption}</option><br/>
                [#assign hiddens = hiddens + "<input type=\"hidden\" name=\"vars\" value=\"${var}_${opcaoValue}\" /><input type=\"hidden\" name=\"${var}_${opcaoValue}\" value=\"${opcaoOption}\" />" /]
            [/#list]
        </select>
        [#if disabled]
        	<input type="hidden" name="${var}" value="${v}" />
        [/#if]
        ${hiddens}
    [#else]
        <span class="valor">${v}</span>
    [/#if]
[/#macro]

[#macro memo var titulo colunas linhas reler=false obrigatorio=false default=""]
        [#if reler == true]
                [#local jreler = " onchange=\"javascript: sbmt();\""]
        [/#if]

        [#local v = .vars[var]!default]

	<div class="form-group" style="margin-bottom:0">
        <input type="hidden" name="vars" value="${var}" />

        [#if (alerta!"Não") = 'Sim' && v = ""]
    		[#list obrigatorios?split(",") as campo]
                [#if campo == var]
                	[#local vermelho = "color:red"]
            	[/#if]
           	[/#list]
        [/#if]

        [#if obrigatorio]
            [#local negrito = "font-weight:bold"]
            <input type="hidden" name="obrigatorios" value="${var}" />
        [/#if]
        
        [#if titulo != ""]                         
        	<label for="${var}" style="${negrito!};${vermelho!}">${titulo}</label>
        [/#if]

        [#if !gerar_formulario!false]
        	<textarea id="${var}" cols="${colunas}" rows="${linhas}" name="${var}" ${jreler!""} style="width:100%;" class="form-control">${v}</textarea>
        	<div class="invalid-feedback  invalid-feedback-${var}">Preenchimento obrigatório</div>
        [#else]
            <span class="valor">${v}</span>
        [/#if]
	</div>
[/#macro]

[#macro memocomposto var titulo colunas linhas reler=false obrigatorio=false default="" forceDefault=false disabled=false]
    [#if reler == true]
            [#local jreler = " onchange=\"javascript: sbmt();\""]
    [/#if]

    [#if forceDefault == true]
    	[#local v = default! ]
    [#else]
    	[#local v = .vars[var]!default]
	[/#if]
	<div class="form-group" style="margin-bottom:0">
        <input type="hidden" name="vars" value="${var}" />

        [#if (alerta!"Não") = 'Sim' && v = ""]
    		[#list obrigatorios?split(",") as campo]
                [#if campo == var]
                	[#local vermelho = "color:red"]
                [/#if]
             [/#list]
        [/#if]

        [#if obrigatorio]
            [#local negrito = "font-weight:bold"]
            <input type="hidden" name="obrigatorios" value="${var}" />
        [/#if]

        
                [#if titulo != ""]                         
                    <label for="${var}" style="${negrito!};${vermelho!}">${titulo}</label>
                [/#if]

                [#if !gerar_formulario!false]
                    <textarea class="form-control" cols="${colunas}" rows="${linhas}" name="${var}" ${jreler!""} style="width:100%;"[#if disabled == true] readonly[/#if]>${v}</textarea>
               		<div class="invalid-feedback  invalid-feedback-${var}">Preenchimento obrigatório</div>
                [#else]
                    <span class="valor">${v}</span>
                [/#if]
    </div>
[/#macro]
[#macro XStandard nome="" conteudo=""]
        <script type="text/javascript" language="Javascript1.1">

        var insertingTable = false;
     
        /*function onSave() {
            var xstandard = document.getElementById('xstandard');
            if (xstandard && xstandard.readyState == 4){
                xstandard.value = xstandard.value.replace(/class=\"indent-first\"/g, 'style=\"text-indent: 2cm; text-align:justify;\"');
                xstandard.value = xstandard.value.replace(/class=\"underline\"/g, 'style=\"text-decoration: underline\"');
                xstandard.value = xstandard.value.replace(/class=\"justify\"/g, 'style=\"text-align:justify;\"');
                document.getElementById('${nome}').EscapeUnicode = false;
                document.getElementById('${nome}').value = xstandard.value;
            }
        }*/
        function onSave() {
            var xstandard = document.getElementById('xstandard');
            var inputHidden = document.getElementById('${nome}');
            if (xstandard && xstandard.readyState == 4){
                inputHidden.EscapeUnicode = false;
                inputHidden.value = xstandard.value;
                inputHidden.value = inputHidden.value.replace(/class=\"indent-first\"/g, 'style=\"text-indent: 2cm; text-align:justify;\"');
                inputHidden.value = inputHidden.value.replace(/class=\"underline\"/g, 'style=\"text-decoration: underline\"');
                inputHidden.value = inputHidden.value.replace(/class=\"justify\"/g, 'style=\"text-align:justify;\"');
            }
        }
    
        function xsDialogPropertiesActivated(id, qpath, element, attributes, metadata) {
        if (qpath == '' && element == 'table'){
            document.getElementById('xstandard').SetDialogProperties("<attributes><attr><name>summary</name><value>Tabela</value></attr><attr><name>bordercolor</name><value>#000000</value></attr><attr><name>style</name><value>border-width:1px;border-style:solid;border-collapse:collapse</value></attr></attributes>", false, false);
            }
        }

        setTimeout("verificaSeCarregou()",2000);
        function verificaSeCarregou()
        {
          var xstandard = document.getElementById('xstandard');
          if (!xstandard || xstandard.readyState != 4){
              document.getElementById('desconsiderarExtensao').value='true';
              document.getElementById('${nome}').value = 
                  document.getElementById('xstandard_temp').innerHTML;
              sbmt();
            }
        }
  
        </script>

        <div id="xstandard_temp" style="display:none">
        ${conteudo}
        </div>

        <object classid="clsid:0EED7206-1661-11D7-84A3-00606744831D"
        codebase="http://${serverAndPort}/siga-ext-editor/XStandard/XStandard.cab#Version=3,0,0,0"
        type="application/x-xstandard" id="xstandard" width="100%" height="400">
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

         
        <param name="Value" value="${conteudo?html}" />

        <param name="SpellCheckerLangFilter" value="pt" />
        <param name="SpellCheckerLang" value="pt" />
        <param name="License" value="http://${serverAndPort}/siga-ext-editor/XStandard/license.txt" />
        <param name="CSS" value="http://${serverAndPort}/siga-ext-editor/XStandard/format.css" />
        <param name="Styles" value="http://${serverAndPort}/siga-ext-editor/XStandard/styles-pt.xml" />
        <param name="Buttons" value="http://${serverAndPort}/siga-ext-editor/XStandard/buttons-pt.xml" />
        <param name="Icons" value="http://${serverAndPort}/siga-ext-editor/XStandard/icons.xml" />
        <!-- Ver como coloca português -->
        <param name="Lang" value="pt" />
        <param name="Localization" value="http://${serverAndPort}/siga-ext-editor/XStandard/localization-pt.xml" />
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
[#macro formulario texto fecho="" tamanhoLetra="Normal" _tipo="FORMULÁRIO"]
[#--
  Aplicação: Formatar documento para o tipo Formulário
  Autor:     André
  Data:      29/03/2012
--]
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
        <span style="font-size: ${tl}"> ${texto!} </span>
                <p style="align: justify; TEXT-INDENT: 2cm">${fecho}</p>
        [/@estiloBrasaoCentralizado]
[/#macro]

[#macro assentamento_funcional texto fecho="" tamanhoLetra="Normal" _tipo="ASSENTAMENTO FUNCIONAL"]
[#--
  Aplicação: Formatar documento para o tipo Assentamento Funcional
  Autor:     André
  Data:      20/03/2012
--]
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
        <span style="font-size: ${tl}"> ${texto!} </span>
                <p style="align: justify; TEXT-INDENT: 2cm">${fecho}</p>
        [/@estiloBrasaoCentralizado]
[/#macro]

[#macro requerimento texto fecho="" tamanhoLetra="Normal" _tipo=""]
[#--
  Aplicação: Formatar documento para o tipo Formulário
  Autor:     André
  Data:      29/05/2012
--]
    [#if tamanhoLetra! == "Normal"]
        [#assign tl = "11pt" /]
    [#elseif tamanhoLetra! == "Pequeno"]
        [#assign tl = "9pt" /]
    [#elseif tamanhoLetra! == "Grande"]
        [#assign tl = "13pt" /]
    [#else]     
        [#assign tl = "11pt"]
    [/#if]

        [@estiloBrasaoCentralizado tipo=_tipo tamanhoLetra=tl formatarOrgao=false dataAntesDaAssinatura=true]
        <span style="font-size: ${tl}"> ${texto!} </span>
                <p style="align: justify; TEXT-INDENT: 2cm">${fecho}</p>
        [/@estiloBrasaoCentralizado]
[/#macro]

[#macro requerimento2 texto _tipo="REQUERIMENTO"]
[#--
  Aplicação: Formatar documento para o tipo Requerimento
  Autor:     Priscila
  Data:      30/05/2012
--]
<span style="align: justify; font-family: Arial; font-size: 15pt; font-weight: bold;">
${enderecamentoDiretorDeRH}<br/>
</span>
${texto}<br/><br/>
<span style="align: right; TEXT-INDENT: 2cm">Nestes termos,<br/>
Pede deferimento.</span><br/><br/><br/>
[#if mov??]
[@assinaturaMovCentro formatarOrgao=formatarOrgao/]
[#else]
[@assinaturaCentro formatarOrgao=formatarOrgao/]
[/#if]
[@primeiroRodape]
[@rodapeClassificacaoDocumental/]
[/@primeiroRodape]
[/#macro]

[#macro mensagem texto titulo="" vermelho=false]
    <span style="[#if vermelho]color=#ff0000[/#if]">[#if titulo?? && titulo!=""]<b>${titulo}</b>: [/#if]${texto!""}</span>
[/#macro]

[#macro mensagem2 texto titulo="" cor="black"]
<span style="color:${cor}"> [#if titulo?? && titulo!=""]<b>${titulo}</b>:[/#if] <b>${texto}</b></span>
[/#macro]


[#-- macro obrigatorios texto="Os campos em negrito são de preenchimento obrigatório" titulo="Atenção" vermelho=false]
<span style="[#if vermelho]color=#ff0000[/#if]">[#if titulo?? && titulo!=""]<b>${titulo}</b>: [/#if]${texto!""}</span>
[/#macro --]

[#macro separador]
    <hr color="#FFFFFF"/>
[/#macro]

[#macro caixaSelecao titulo var tipo="" idInicial="" siglaInicial="" descricaoInicial="" modulo="" desativar=false buscar=true ocultarDescricao=false reler=false idAjax="" default="" obrigatorio=false relertab="" paramList="" grande=false]
    [#local larguraPopup = 600 /]
    [#local alturaPopup =400 /]
    [#local tipoSel = "_" + tipo /]
        [#local acaoBusca = (modulo=="")?string("/siga/","/"+modulo+"/") + tipo /]
    
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
                 var url = '${acaoBusca}/buscar.action?propriedade=${var}${tipoSel}&sigla='+encodeURI(sigla)+'${selecaoParams!}';
        
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
        var url = '${acaoBusca}/selecionar.action?var=${var}${tipoSel}&sigla='+encodeURI(sigla)+'${selecaoParams!}';
        url = url + '&sigla=' + sigla;
        PassAjaxResponseToFunction(url, 'resposta_ajax_${var}${tipoSel}', false);
    }
    
    </script>
    
    <input type="hidden" name="${var}${tipoSel}Sel.id" value="${.vars[var+tipoSel+"Sel.id"]!}" />
    <input type="hidden" name="${var}${tipoSel}Sel.descricao" />
    <input type="hidden" name="${var}${tipoSel}Sel.buscar" />
    <input type="hidden" name="req${var}${tipoSel}Sel" />
    <input type="hidden" name="alterouSel" value="" id="alterouSel" />
    <div class="input-group">
    <input type="text" class="form-control" name="${var}${tipoSel}Sel.sigla" value="${.vars[var+tipoSel+"Sel.sigla"]!}" onkeypress="return handleEnter(this, event)"
	        onblur="javascript: ajax_${var}${tipoSel}();" size="25" ${desativar?string('disabled="true"','')} />
	    [#if buscar]
	    	<div class="input-group-append">
		        <input type="button" class="btn btn-secondary" id="${var}${tipoSel}SelButton" value="..."
		            onclick="javascript: popitup_${var}${tipoSel}('');"
		            ${desativar?string("disabled","")} theme="simple">
	        </div>
	    [/#if]	    
		<div class="invalid-feedback  invalid-feedback-${var}${tipoSel}Sel.sigla">Preenchimento obrigatório</div>		
    </div>
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

[#macro selecionavel titulo var tipo reler=false idAjax="" default="" obrigatorio=false relertab="" paramList="" modulo=""]
    [#assign tipoSel = "_" + tipo /]

    [#assign varName = var + tipoSel + "Sel.id" /]    
    [#local vId = .vars[varName]!default]
    <div class="form-group" style="margin-bottom:0">
	    <input type="hidden" name="vars" value="${varName}" />
	
	    [#assign varName = var + tipoSel + "Sel.sigla" /]
	    [#local vSigla = .vars[varName]!default]
	    <input type="hidden" name="vars" value="${varName}" />
	
	    [#assign varName = var + tipoSel + "Sel.descricao" /]
	    [#local vDescricao = .vars[varName]!default]
	    <input type="hidden" name="vars" value="${varName}" />
	
	    [#if (alerta!"Não") = 'Sim' && vId == ""]
	    [#list obrigatorios?split(",") as campo]
	         [#if campo == varName]
	         [#local vermelho = "color:red"]
	             [/#if]
	        [/#list]
	    [/#if]
	    [#if obrigatorio]
	    [#local negrito = "font-weight:bold"]
	    [#assign varName = var + tipoSel + "Sel.sigla" /]
	    <input type="hidden" name="obrigatorios" value="${varName}" />	    
	    [/#if]
	
	    [#if titulo?? && titulo != ""]	    	
	    	<label for="${varName}" style="${negrito!};${vermelho!}">${titulo}</label>
	    [/#if]
	
	    [#if !gerar_formulario!false]
	        [@caixaSelecao titulo=titulo var=var tipo=tipo reler=reler idAjax=idAjax relertab=relertab paramList=paramList modulo=modulo /]	        
	    [#else]
	    <span class="valor">[#if vSigla??]${vSigla} - [/#if]${vDescricao}</span>
	    [/#if]
    </div>
[/#macro]

[#macro pessoa titulo var reler=false relertab="" buscarFechadas=false idAjax="" default="" obrigatorio=false paramList=""]
    [#if buscarFechadas]
        [@assign paramList = "buscarFechadas=true" /]
    [/#if]
    [@selecionavel tipo="pessoa" titulo=titulo var=var reler=reler idAjax=idAjax relertab=relertab paramList=paramList obrigatorio=obrigatorio /]
[/#macro]

[#macro cosignatario titulo var reler=false relertab="" buscarFechadas=false idAjax="" default="" obrigatorio=false paramList=""]
    [#if buscarFechadas]
        [@assign paramList = "buscarFechadas=true" /]
    [/#if]
    [@selecionavel tipo="cosignatario" titulo=titulo var=var reler=reler idAjax=idAjax relertab=relertab paramList=paramList obrigatorio=obrigatorio /]
[/#macro]

[#macro funcao titulo var reler=false relertab="" buscarFechadas=false idAjax="" default="" obrigatorio=false paramList=""]
    [@selecionavel tipo="funcao" titulo=titulo var=var reler=reler relertab=relertab paramList=paramList obrigatorio=obrigatorio /]
[/#macro]

[#macro lotacao titulo var reler=false relertab="" buscarFechadas=false idAjax="" default="" obrigatorio=false paramList=""]
    [@selecionavel tipo="lotacao" titulo=titulo var=var reler=reler relertab=relertab paramList=paramList obrigatorio=obrigatorio /]
[/#macro]

[#macro data titulo var reler=false idAjax="" default="" onSelect="" obrigatorio=false atts={} ]
    [#if reler == true && idAjax != ""]
            [#local jreler = " sbmt('" + idAjax + "');\""]
    [#elseif reler == true]
            [#local jreler = " sbmt();\""]
    [/#if]

    [#local v = (.vars[var]!"")?string /]
    [#if v == ""]
        [#if default?is_date]
            [#local v = default?string("dd/MM/yyyy") /]
        [#else]
            [#local v = default /]
        [/#if]
    [/#if]


    [#if (alerta!"Não") = 'Sim' && v==""]
    [#list obrigatorios?split(",") as campo]
         [#if campo == var]
         [#local vermelho = "color:red"]
             [/#if]
        [/#list]
    [/#if]

	<div class="form-group" style="margin-bottom:0">  
	    [#if obrigatorio]
	    [#local negrito = "font-weight:bold"]
	    <input type="hidden" name="obrigatorios" value="${var}" />
	    [/#if]
	    
	    [#if !gerar_formulario!false]
	        <input type="hidden" name="vars" value="${var}" />
	         
		[#if titulo?? && titulo != ""]<label for="${var}" style="${negrito!};${vermelho!}">${titulo}</label>[/#if] 
		[#assign attsHtml][#list atts?keys as k]${k}="${atts[k]}"[/#list][/#assign]
		<input type="text" id="${var}" name="${var}" value="${v}" size="10" maxlength="10" class="form-control  campoData" ${attsHtml} style="max-width: 115px" placeholder="00/00/0000"/>		
		<div class="invalid-feedback  invalid-feedback-${var}">Preenchimento obrigatório</div>				
	    [#else]
	    <span class="valor">${v}</span>
	    [/#if]
	    <script>
	    	$('.campoData').mousedown(function() {
	  			$('.campoData').datepicker({
	            	onSelect: function(){
	                    ${onSelect}
					}
				});
			});
		</script>
	</div>		
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

[#macro primeiroRodape exibeClassificacaoDocumental=true]
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

[#macro inicioSubscritor sigla]
    <!-- INICIO SUBSCRITOR [#nested/] --><!-- SIGLA ${sigla!} -->
[/#macro]

[#macro fimSubscritor]
    <!-- FIM SUBSCRITOR [#nested/] -->
[/#macro]

[#macro cabecalhoCentralizadoPrimeiraPagina orgaoCabecalho=false]
	<table style="float:none; clear:both;" width="100%" align="left" border="0" cellpadding="0"
	    cellspacing="0" bgcolor="#FFFFFF">
	    <tr bgcolor="#FFFFFF">
	        <td width="100%">
	        <table width="100%" border="0" cellpadding="2">
	            <tr>
	                <td width="100%" align="center" valign="bottom"><img src="${_pathBrasao}" width="${_widthBrasao}" height="${_heightBrasao}" /></td>
	            </tr>
	            <tr>
	                <td width="100%" align="center">
	                <p style="font-family: AvantGarde Bk BT, Arial; font-size: 11pt;">${_tituloGeral}</p>
	                </td>
	            </tr>
	            [#if _subtituloGeral?has_content]
	            <tr>
	                <td width="100%" align="center">
	                <p style="font-family: Arial; font-size: 10pt; font-weight: bold;">${_subtituloGeral}</p>
	                </td>
	            </tr>
	            [/#if]
	            [#if orgaoCabecalho?? && orgaoCabecalho]
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
	            [/#if]
	        </table>
	        </td>
	    </tr>
	</table>
[/#macro]

[#macro cabecalhoCentralizado orgaoCabecalho=true]
<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
    <tr bgcolor="#FFFFFF">
        <td width="100%">
        <table width="100%" border="0" cellpadding="2">
            <tr>
                <td width="100%" align="center">
                <p style="font-family: AvantGarde Bk BT, Arial; font-size: 11pt;">${_tituloGeral}</p>
                </td>
            </tr>
            [#if _subtituloGeral?has_content]
            <tr>
                <td width="100%" align="center">
                <p style="font-family: Arial; font-size: 10pt; font-weight: bold;">${_subtituloGeral}</p>
                </td>
            </tr>
            [/#if]
            [#if orgaoCabecalho?? && orgaoCabecalho] 
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
           [/#if]
        </table>
        </td>
    </tr>
</table>
[/#macro]

[#macro cabecalhoEsquerdaPrimeiraPagina width=65 height=65 exibirOrgao=true]
<table width="100%" align="left" border="0">
    <tr>
        <td align="left" valign="bottom" width="15%"><img src="${_pathBrasao}" width="${width}" height="${height}" /></td>
        <td align="left" width="1%"></td>
        <td width="84%">
        <table align="left" width="100%">
            <tr>
                <td width="100%" align="left">
                <p style="font-family: AvantGarde Bk BT, Arial; font-size: 11pt;">${_tituloGeral}</p>
                </td>
            </tr>
            [#if _subtituloGeral?has_content]
            <tr>
                <td width="100%" align="center">
                <p style="font-family: Arial; font-size: 10pt; font-weight: bold;">${_subtituloGeral}</p>
                </td>
            </tr>
            [/#if]
            [#if exibirOrgao]
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
            [/#if]
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
                <p style="font-family: AvantGarde Bk BT, Arial; font-size: 11pt;">${_tituloGeral}</p>
                </td>
            </tr>
            [#if _subtituloGeral?has_content]
            <tr>
                <td width="100%" align="center">
                <p style="font-family: Arial; font-size: 10pt; font-weight: bold;">${_subtituloGeral}</p>
                </td>
            </tr>
            [/#if]
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

[#macro cabecalhoDireitaGenerico width="65" height="65" exibirOrgao=false]
<table width="100%" border="0" bgcolor="#FFFFFF">
    <tr bgcolor="#FFFFFF">
        <td width="100%">
        <table width="100%">
            <tr>
	        <td align="right" valign="bottom"><img src="${_pathBrasaoSecundario}" width="200"/></td>
	        <td width="1%">
	        <table align="right" width="100%">
	        </table>
	        </td>
   			</tr>
        </table>
        </td>
    </tr>
</table>
[/#macro]

[#macro cabecalhoDireita]
<table width="100%" border="0" bgcolor="#FFFFFF">
    <tr bgcolor="#FFFFFF">
        <td width="100%">
        <table width="100%">
            <tr>
                <td width="100%" align="left">
                <p style="font-family: AvantGarde Bk BT, Arial; font-size: 11pt;">${_tituloGeral}</p>
                </td>
            </tr>
            [#if _subtituloGeral?has_content]
            <tr>
                <td width="100%" align="center">
                <p style="font-family: Arial; font-size: 10pt; font-weight: bold;">${_subtituloGeral}</p>
                </td>
            </tr>
            [/#if]
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

[#macro rodapeClassificacaoDocumental align="left" somenteTR=false texto=""]
[#if !somenteTR]
<table align="left" width="100%" bgcolor="#FFFFFF">
[/#if]
[#if texto?? && texto!=""]
	<tr>
		<td colspan="2" align="${align}" style="border-collapse: collapse; border-color: black; font-family:Arial; font-size:8pt;">
			${texto} 
		</td>
	</tr>
[/#if]
<tr>
<td width="60%" ></td>
<td width="40%" >
<table align="right" width="100%" border="1" style="border-color: black; border-spacing: 0px; border-collapse: collapse" bgcolor="#000000">
<tr>
<td align="center" width="55%" style="border-collapse: collapse; border-color: black; font-family:Arial; font-size:8pt;" bgcolor="#FFFFFF">
	<i>Classif. documental</i>
</td>
<td align="center" width="45%" style="border-collapse: collapse; border-color: black; font-family:Arial;font-size:8pt;" bgcolor="#FFFFFF">
	<span>${(doc.exClassificacao.sigla)!}</span>
</td>
</tr>
</table>
</td>
</tr>
[#if !somenteTR]
</table>
[/#if]
[/#macro]

[#macro rodapeNumeracaoADireita texto=""]
<table width="100%" border="0" cellpadding="0" bgcolor="#FFFFFF">
	[#if texto?? && texto!=""]
		<tr>
			<td align="left" style="border-collapse: collapse; border-color: black; font-family:Arial; font-size:8pt;">
				${texto} 
			</td>
		</tr>
	[/#if]

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

[#macro assinaturaCentro formatarOrgao=false incluirAssinaturaBIE=true]
[#if incluirAssinaturaBIE == true]
   <!-- INICIO ASSINATURA -->
[/#if]
<p style="font-family: Arial; font-size: 11pt;" align="center">
	<br/>
    [#if (doc.subscritor)??]
       [@inicioSubscritor sigla=doc.codigoCompacto]${(doc.subscritor.idPessoa)!}[/@inicioSubscritor]
    [/#if]
    [#if (doc.nmSubscritor)??]
        ${doc.nmSubscritor}
    [#else]
       [#if (doc.subscritor.nomeExibicao)??]
           ${doc.subscritor.nomeExibicao}
       [#else]
           ${(doc.subscritor.nomePessoa)!}
       [/#if]
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
        [#if (doc.subscritor)??]
            [@fimSubscritor]${(doc.subscritor.idPessoa)}[/@fimSubscritor]
        [/#if]
        
                [#if (doc.mobilGeral.exMovimentacaoSet)??]
        [#list doc.mobilGeral.exMovimentacaoSet as mov]
                    [#if (mov.exTipoMovimentacao.idTpMov)! == 24]
                        <br/><br/><br/>
                        [@inicioSubscritor sigla=doc.codigoCompacto]${(mov.subscritor.idPessoa)}[/@inicioSubscritor]
                        [#if mov.nmSubscritor??]
                            ${mov.nmSubscritor}
                        [#else]
                           [#if (mov.subscritor.nomeExibicao)??]
                           		${mov.subscritor.nomeExibicao}
                           [#else]
                           ${(mov.subscritor.nomePessoa)!}
                           [/#if]
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
                        [@fimSubscritor]${(mov.subscritor.idPessoa)}[/@fimSubscritor]
            [/#if]
        [/#list]
            [/#if]
    [/#if]
    [#if textoFinal??]
        <br/>${textoFinal}
    [/#if]
</p>
[#if incluirAssinaturaBIE == true]
   <!-- FIM ASSINATURA -->
[/#if]
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

[#macro estiloBrasaoAEsquerda tipo exibeData=true formatarOrgao=false numeracaoEsquerda=false tamanhoLetra="11pt" obs="" omitirCodigo=false width=65 height=65 exibirOrgao=true texto=""]
    [@primeiroCabecalho]
    <table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
    [@cabecalhoEsquerdaPrimeiraPagina width=width height=height exibirOrgao=exibirOrgao/]
    </td></tr>
        <tr bgcolor="#FFFFFF">
            <td width="100%">
                <table width="100%">
                    <tr>
                       [#if !numeracaoEsquerda]
		                	<td align="right">
		                    	[#if !omitirCodigo]                           
		                           <p style="font-family:Arial;font-weight:bold;font-size:11pt;">${tipo} SIGA N&ordm; ${(doc.codigo)!}</p>
		                        [/#if]
		                    </td>
	                    [#else]
	                    	<td align="left">
		                         [#if !omitirCodigo]                           
		                           <p style="font-family:Arial;font-weight:bold;font-size:11pt;"><br><br>${tipo} SIGA N&ordm; ${(doc.codigo)!}</p>
		                         [/#if]
		                    </td>
	                    [/#if]
                    </tr>
                    
                    [#if exibeData]
                    <tr>
                        <td align="right">[@letra tamanho="11pt"]<p>${(doc.dtExtenso)!}</p>[/@letra]</td>
                    </tr>                    
                    [/#if]
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
        [@assinaturaCentro formatarOrgao=formatarOrgao/]
    [/@letra]
        [#if obs != ""]
                <p>&nbsp;</p>
                ${obs}
        [/#if]
    [@primeiroRodape]
    [@rodapeClassificacaoDocumental texto=texto/]
    [/@primeiroRodape]

    [@rodape]
    [@rodapeNumeracaoADireita texto=texto/]
    [/@rodape]
[/#macro]

 [#macro estiloBrasaoADireita tipo exibeData=true formatarOrgao=false numeracaoEsquerda=false tamanhoLetra="11pt" obs="" omitirCodigo=false width=65 height=65 exibirOrgao=false texto=""]
    [@primeiroCabecalho]
    <table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
    [@cabecalhoDireitaGenerico width=65 height=65 exibirOrgao=true/]
    </td></tr>
        <tr bgcolor="#FFFFFF">
            <td width="100%">
                <table width="100%">                  
                    [#if exibeData]
                    <tr>
                        <td align="right">[@letra tamanho="10pt"]<p>${(doc.dtExtenso)!}</p>[/@letra]</td>
                    </tr>                    
                    [/#if]
                </table>
            </td>
        </tr>
    </table>
    [/@primeiroCabecalho]

    [@cabecalho]
    [@cabecalhoDireitaGenerico/]
    [/@cabecalho]

    [@letra tamanho=tamanhoLetra]
        <p>&nbsp;</p>
                [#nested]
        <p>&nbsp;</p>
        [@assinaturaCentro formatarOrgao=formatarOrgao/]
    [/@letra]
        [#if obs != ""]
                <p>&nbsp;</p>
                ${obs}
        [/#if]
 
 	[@primeiroRodape]
    [@rodapeClassificacaoDocumental align="right" texto=texto/]
    [/@primeiroRodape]
    
    [@rodape]
   	[@rodapeClassificacaoDocumental align="right" texto=texto/]
    [@rodapeNumeracaoADireita texto="" /]
    [/@rodape]
[/#macro]


[#macro estiloBrasaoCentralizado tipo tamanhoLetra="11pt"  exibeAssinatura=true formatarOrgao=true orgaoCabecalho=true numeracaoCentralizada=false dataAntesDaAssinatura=false incluirMioloDJE=false omitirCodigo=false omitirData=false topoPrimeiraPagina='' incluirAssinaturaBIE=true exibeClassificacaoDocumental=true]
    [@primeiroCabecalho]${topoPrimeiraPagina!}
    [@cabecalhoCentralizadoPrimeiraPagina orgaoCabecalho/]
    [/@primeiroCabecalho]
    [@cabecalho]
    [@cabecalhoCentralizado orgaoCabecalho/]
    [/@cabecalho]
    [@letra tamanhoLetra]
        [#if !numeracaoCentralizada]
              <table style="float:none; clear:both; margin: 0; padding: 0;border-collapse: collapse;" width="100%" border="0" bgcolor="#FFFFFF">
              <tr style="margin: 0; padding: 0;">
              <td align="left" style="margin: 0; padding: 0;">
              [#if !omitirCodigo]
                    <p style="font-family:Arial;font-weight:bold;font-size:11pt;"><br/>[@numeroDJE]${tipo}[#if tipo != ""] SIGA N&ordm; ${(doc.codigo)!} [/#if] [/@numeroDJE]</p>
              [/#if]
              </td>
              </tr>
              [#if !dataAntesDaAssinatura && !omitirData]
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
                  [@numeroDJE] [#if tipo != ""] ${tipo} SIGA N&ordm; ${(doc.codigo)!}[/#if] [/@numeroDJE]
                  [#if !dataAntesDaAssinatura && doc?? && doc.dtD??] de ${doc.dtD} de ${doc.dtMMMM} de ${doc.dtYYYY}[/#if]</p>
              </td>
              </tr>
              </table>
        [/#if]
        [@tituloDJE]
            <p style="font-family:Arial;font-weight:bold;font-size:11pt;">[#if tipo != ""] ${tipo}[/#if] ${(doc.codigo)!}</p>
        [/@tituloDJE]
        [#if incluirMioloDJE]
            [@mioloDJE]
            [#nested]
            [#if dataAntesDaAssinatura]<p style="text-align:center">[#if mov??]${mov.dtExtenso!}[#else]${doc.dtExtenso!}[/#if]</p>[/#if]
            <p>&nbsp;</p>
            [#if exibeAssinatura]
            [#if mov??]
                [@assinaturaMovCentro formatarOrgao=formatarOrgao/]
            [#else]
                [@assinaturaCentro formatarOrgao=formatarOrgao incluirAssinaturaBIE=incluirAssinaturaBIE/]
            [/#if]
            [/#if]
            [/@mioloDJE]
        [#else]
            [#nested]
      		[#assign data = func.dataAtual(doc) /]
            [#if dataAntesDaAssinatura]
            	<p style="text-align:center">
            	[#if mov??] 
            		${mov.dtExtenso!}
            	[#else] 
            		[#if data != ""]
            			${data}
            		[#else]
            			${doc.dtExtenso!}
            		[/#if]
            	[/#if]
            	</p>
            [/#if]
            <p>&nbsp;</p>
            [#if exibeAssinatura]
            [#if mov??]
                [@assinaturaMovCentro formatarOrgao=formatarOrgao/]
            [#else]
                [@assinaturaCentro formatarOrgao=formatarOrgao incluirAssinaturaBIE=incluirAssinaturaBIE/]
            [/#if]
        [/#if]
        [/#if]      
   [/@letra]
   [@primeiroRodape exibeClassificacaoDocumental=exibeClassificacaoDocumental]
	   [#if exibeClassificacaoDocumental]
	   		[@rodapeClassificacaoDocumental/]
	   [/#if]
   [/@primeiroRodape]
   [@rodape]
   [@rodapeNumeracaoADireita/]
   [/@rodape]
[/#macro]

[#macro processo]
    [#assign _topoPrimeiraPagina]
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					<table border="0" align="center" bgcolor="#ffffff" width="100%">
						<tr>
							<td width="20%" bgcolor="#787878"></td>
							<td width="60%" align="center" style=" font: bold; font-size: 14pt"><b>PROCESSO&nbsp;ADMINISTRATIVO</b></td>
							<td width="20%" bgcolor="#787878"></td>
						</tr>
					</table>
				</td>
			<tr>
			<tr bgcolor="#FFFFFF">
				<td cellpadding="5">
					&nbsp;
				</td>
			</tr>
		</table>
    [/#assign]
[@estiloBrasaoCentralizado tipo='EOF' tamanhoLetra='14pt' formatarOrgao=true omitirCodigo=true omitirData=true topoPrimeiraPagina=_topoPrimeiraPagina]
  
	<br />
<table align="center" width="60%" border="1" cellspacing="1" bgcolor="#000000">
	<tr>
		<td width="30%" bgcolor="#FFFFFF" align="center"><br />
		<b>Processo SIGA N&ordm;</b><br />
		<br /></td>
	</tr>
	<tr>
		<td bgcolor="#FFFFFF" align="center"><br />
		${(doc.codigo)!}</td>
	</tr>
</table>
  
<br />
<br />
<table align="center" width="50%" border="1" cellspacing="1"
	bgcolor="#000000">
	<tr>
		<td bgcolor="#FFFFFF" align="center"><br />
		<b>Data de abertura</b><br />
		<br /></td>
		<td bgcolor="#FFFFFF" align="center">${(doc.dtDocDDMMYYYY)!}</td>
	</tr>
</table>
<br />
<br />

<table align="center" width="85%" border="1" cellspacing="1"
	bgcolor="#000000">
	<tr>
		<td bgcolor="#FFFFFF" align="center"><b>OBJETO</b></td>
	</tr>
	<tr>
		<td bgcolor="#FFFFFF" align="center"><br />
		${(doc.descrDocumento)!}<br /></td>
	</tr>
</table>
[/@estiloBrasaoCentralizado]
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


[#macro provimento texto="" abertura="" tamanhoLetra="Normal" _tipo="PROVIMENTO" ementa="" titulo="" subtitulo=""]

    [#-- Versão modificada da macro portaria --]

    [#if tamanhoLetra! == "Normal"]
        [#assign tl = "11pt" /]
    [#elseif tamanhoLetra! == "Pequeno"]
        [#assign tl = "9pt" /]
    [#elseif tamanhoLetra! == "Grande"]
       [#assign tl = "13pt" /]
    [#else]
       [#assign tl = "11pt"]
    [/#if]
    [@br/]
    [@estiloBrasaoCentralizadoTrf tipo=_tipo tamanhoLetra=tl formatarOrgao=true numeracaoCentralizada=true]
      [@inicioMioloDJE]
      [/@inicioMioloDJE] 
       [#if ementa != ""]
	    <table style="float:none;" width="100%" border="0" cellpadding="2" cellspacing="0" bgcolor="#FFFFFF">
	        <tr>
	        <td align="left" width="50%"></td>
	        <td align="left" width="50%" style="font-family: Arial; font-size: ${tl};"><br/><p align="justify">${ementa!}</p></td>
	        </tr>
	    </table>
	    [@br/][@br/]
	[/#if]
	<div style="font-family: Arial; font-size: ${tl};">
	[#if abertura != ""]
	    [@br/][@br/]
	    [@aberturaBIE] 
	        ${abertura!}
	    [/@aberturaBIE]
	[/#if]
	[@corpoBIE]
	    [#if titulo != ""][@br/]<center><b>${titulo}</b></center>[@br/][/#if]
	    [#if subtitulo != ""]<center><b>${subtitulo}</b></center>[@br/][/#if]
	    [@br/]${texto}
	[/@corpoBIE]
	<p style="font-family: Arial; font-size: ${tl}; font-weight: bold;" align="center">
	[@fechoBIE]
	   [@br/]
	   [#if _tipo != "ORDEM DE SERVIÇO"]
		  PUBLIQUE-SE. REGISTRE-SE. 
	   [/#if]
	   CUMPRA-SE.
	[/@fechoBIE]
	</p>
	</div>
      [/@estiloBrasaoCentralizadoTrf]
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
    [@estiloBrasaoCentralizado tipo=_tipo tamanhoLetra=tl formatarOrgao=false numeracaoCentralizada=true incluirMioloDJE=true]
            [#if dispoe_sobre != ""]     
              <table style="float:none;" width="100%" border="0" cellpadding="2" cellspacing="0" bgcolor="#FFFFFF">
                  <tr>
                      <td align="left" width="50%"></td>
                    <td align="left" width="50%" style="text-align: justify; font-family: Arial; font-size: ${tl};"><br/>Dispõe sobre ${dispoe_sobre!}</td>
                  </tr>
              </table>
            <br/>
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
                            [@texto titulo="Forma de endereçamento" var="enderecamento_dest" largura="45" /]
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
        [#if tamanhoLetra! == "Normal"]
            [#assign tl = "11pt" /]
        [#elseif tamanhoLetra! == "Pequeno"]
            [#assign tl = "9pt" /]
        [#elseif tamanhoLetra! == "Grande"]
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
                        <p align="left" style="font-size: ${tl}; TEXT-INDENT: 2cm">${vocativo!},</p>
                    [/#if]
                    [@letra tamanho=tl]${texto_oficio!}[/@letra]
                [/@corpoBIE]
                [#if fecho??]<p style="font-size: ${tl}; TEXT-INDENT: 2cm">[@fechoBIE]${fecho!}${virgula!}[/@fechoBIE][/#if]</p>
                </div>
           [/@mioloDJE]
        [/@estiloBrasaoCentralizado]
    [/@documento]
[/#macro]

[#macro folhaDeRostoExterno]
	[@documento]
	  [@estiloBrasaoCentralizado tipo="DOCUMENTO EXTERNO" tamanhoLetra="11pt"]
	    <table width="100%" border="0" cellpadding="6" cellspacing="6" bgcolor="#FFFFFF">
	      <tr>
	        <td width="50%">
	          Órgão Externo:
	        </td>
	        <td width="50%">
	          ${(doc.orgaoExterno.descricao)!} ${(doc.obsOrgao)!}
	        </td>
	      </tr>
	      <tr>
	        <td>
	          Data Original do Documento:
	        </td>
	        <td>
	          ${(doc.dtDocOriginalDDMMYYYY)!}
	        </td>
	      </tr>
	      <tr>
	        <td>
	          Número Original:
	        </td>
	        <td>
	          ${(doc.numExtDoc)!}
	        </td>
	      </tr>
	      [#if (doc.numAntigoDoc)! != '']
	        <tr>
	          <td>
	            Número no Sistema Antigo:
	          </td>
	          <td>
	            ${(doc.numAntigoDoc)!}
	          </td>
	        </tr>
	      [/#if]
	      <tr>
	        <td>
	          Data:
	        </td>
	        <td>
	          ${(doc.dtDocDDMMYY)!}
	        </td>
	      </tr>
	      <tr>
	        <td>
	          Subscritor:
	        </td>
	        <td>
	          ${(doc.nmSubscritorExt)!}
	        </td>
	      </tr>
	      <tr>
	        <td>
	          Descrição:
	        </td>
	        <td>
	          ${(doc.descrDocumento)!}
	        </td>
	      </tr>
	      <tr>
	        <td>
	        </td>
	        <td>
	        </td>
	      </tr>
	      [#if doc.cadastrante??]
	        <tr>
	          <td>
	            Cadastrante:
	          </td>
	          <td>
	            ${(doc.cadastrante.descricao)!}
	          </td>
	        </tr>
	      [/#if]
	      <tr>
	        <td>
	          Data do cadastro:
	        </td>
	        <td>
	          ${(doc.dtRegDocDDMMYYHHMMSS)!}
	        </td>
	      </tr>
	    </table>
	  [/@estiloBrasaoCentralizado]
	[/@documento]
[/#macro]

[#macro folhaDeRostoInterno]
	[@documento]
	  [@estiloBrasaoCentralizado tipo="DOCUMENTO INTERNO" tamanhoLetra="11pt"]
	    <table width="100%" border="0" cellpadding="6" cellspacing="6" bgcolor="#FFFFFF">
	      <tr>
	        <td width="50%">
	          Número Original:
	        </td>
	        <td width="50%">
	          ${doc.numExtDoc!}
	        </td>
	      </tr>
	      <tr>
	        <td>
	          Número no Sistema Antigo:
	        </td>
	        <td>
	          ${doc.numAntigoDoc!}
	        </td>
	      </tr>
	      <tr>
	        <td>
	          Forma:
	        </td>
	        <td>
	          ${doc.exFormaDocumento.descrFormaDoc!}
	        </td>
	      </tr>
	      <tr>
	        <td>
	          Modelo:
	        </td>
	        <td>
	          ${doc.exModelo.nmMod!}
	        </td>
	      </tr>
	      <tr>
	        <td>
	          Subscritor:
	        </td>
	        <td>
	          ${doc.subscritorString!}
	        </td>
	      </tr>
	      <tr>
	        <td>
	          Destinatário:
	        </td>
	        <td>
	          ${doc.destinatarioString!}
	        </td>
	      </tr>
	      <tr>
	        <td>
	          Descrição:
	        </td>
	        <td>
	          ${doc.descrDocumento!}
	        </td>
	      </tr>
	      <tr>
	        <td>
	          Cadastrante:
	        </td>
	        <td>
	          ${doc.cadastrante.descricao!}
	        </td>
	      </tr>
	      <tr>
	        <td>
	          Data do cadastro:
	        </td>
	        <td>
	          ${doc.dtRegDocDDMMYYHHMMSS!}
	        </td>
	      </tr>
	    </table>
	  [/@estiloBrasaoCentralizado]
	[/@documento]
[/#macro]


[#macro moeda var titulo="" largura="" maxcaracteres="" idAjax="" reler="" relertab="" obrigatorio="nao" default=""]
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

	<div class="form-group" style="margin-bottom:0">
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
	    	<label for="${var}" style="${negrito!};${vermelho!}">${titulo}</label>
	    [/#if]
	    
	    [#if !gerar_formulario!false]
	    <input onkeypress="return formataReais(this, '.' , ',', event)"
	    type="text" name="${var}" value="${v}" ${jreler!""}${jrelertab!""}${jlargura!""}${jmaxcaracteres!""} class="form-control"/>
	    <div class="invalid-feedback  invalid-feedback-${var}">Preenchimento obrigatório</div>	    
	    [#else]
	    <span class="valor">${v}</span>
	    [/#if]
    </div>
[/#macro]
[#macro identificacao pessoa="" funcao="" nivelHierarquicoMaximoDaLotacao="" obs="" negrito="nao"]
    [#if pessoa?? && pessoa.sigla != ""]
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
    "endereco":"<DEFINIR_ENDERECO>"} /] 
[#assign _secretario_geral = {
    "genero":"F", 
    "vocativo":"Senhora", 
    "vocativo_carta":"Sra. Diretora da Secretaria Geral", 
    "enderecamento":"Sra. Dra.", 
    "nome":"<DEFINIR_NOME>", 
    "cargo":"<DEFINIR_CARGO>",
    "orgao":"<DEFINIR_ORGAO>",
    "endereco":"<DEFINIR_ENDERECO>"} /]
[#assign _secretario_rh = {
    "genero":"F", 
    "vocativo":"Senhora", 
    "vocativo_carta":"Sra. Diretora da Subsecretaria de Gestão de Pessoas", 
    "enderecamento":"Sra. Dra.", 
    "nome":"REGINA HELENA MOREIRA FARIA", 
    "cargo":"<DEFINIR_CARGO>",
    "orgao":"<DEFINIR_ORGAO>",
    "endereco":"<DEFINIR_ENDERECO>"} /]
[#assign enderecamentoPresidente = "Exmo. Sr. Juiz Federal - Diretor de Foro" /]
[#assign enderecamentoDiretorGeral = "Ilmo(a). Sr(a). Diretor(a)-Geral" /]
[#assign enderecamentoDiretorDeRH = "Ilma. Sra. Diretora da Subsecretaria de Gestão de Pessoas" /]

[#macro dadosComplementares][/#macro]


[#macro extensaoBuscaTextual]

[/#macro]

[#macro extensaoBuscaTextualbs4]
<!--	<div class="form-row">
		<div class="form-group col-md-6">
			<label for="conteudo">Conte&uacute;do</label>
	        <input type="text" id="fullText" value="${valFullText}" size="80" name="fullText" class="form-control" />
		</div>
	</div> -->
[/#macro]

[#macro extensaoEditor nomeExtensao="" conteudoExtensao=""]
          [#if nomeExtensao == ""]
                  [#local nomeExtensao = nomeExtensaoJsp!"" /]
          [/#if]
          [#if conteudoExtensao == ""]
                  [#local conteudoExtensao = conteudoExtensaoJsp!"" /]
          [/#if]
          [@XStandard nome=nomeExtensao conteudo=conteudoExtensao /]
[/#macro]


[#assign ext_assinatura_com_politica = false /] 
[#assign ext_assinatura_config_remoto = true /]

[#macro extensaoAssinador]    

       [#if lote == 'true']
          [#assign emLote = "em Lote" /]  
       [#else]
          [#assign emLote = "(Java)" /]
       [/#if]

       [#if botao == 'ambos']
          <input type="button" name="cmdConfCopia" id="cmdConfCopia" onClick="javascript: incluiApplet(true,false)" value="Autenticar ${emLote!""}" class="btn btn-primary">
          <input type="button" name="cmdAssinar" id="cmdAssinar" onClick="javascript: incluiApplet(false,false)" value="Assinar ${emLote!""}" class="btn btn-primary">       
       [#else]
          <input type="button" name="cmdAssinar" id="cmdAssinar" onClick="javascript: incluiApplet(false)" value="Assinar ${emLote!""}" class="btn btn-primary">
       [/#if]
       [#if !ext_assinatura_config_remoto]
        <input type="button" name="cmdConfigurar" id="cmdConfigurar" onClick="javascript: incluiApplet(false,true)" value="Configurar Assinador" class="btn btn-primary">
       [/#if]



        <div id="applet"></div>
        
        <script type="text/javascript">                


		function checkAppletStarted(configurar) {
                  
                  var started = false;
                  if (document.applets['oApplet']) {
			try {
				started = document.applets['oApplet'].isStarted();
			} catch (err) {}
                  }
		
                  if (!started){
			window.setTimeout("checkAppletStarted()", 1000);
                  }else{
                    if (configurar) {
			document.applets['oApplet'].showConfiguration();
                    } else {
                    	document.applets['oApplet'].markAllDocuments();
                    	document.applets['oApplet'].signAndSendMarkedDocuments();
                    }
                  }

		} 

		function incluiApplet(copia,configurar){

                  
                  var urlbase= "${request_scheme}://${request_serverName}:${request_localPort}" ;
                  var codebase= urlbase + "/siga-ext-assinatura/applet";
                  var code= "br/com/esec/signapplet/DefaultSignApplet.class";  
                  var nextURL= "${nextURL}";
                  var urlPath= "${urlPath}";
                  var arquivos = "";
                  var action= "${jspServer}";
                  
                   var urlHash;
                  if (${ext_assinatura_com_politica?string('true','false')})
                    urlHash = "&semmarcas=1";
                  else
                    urlHash = "&semmarcas=1&hash=SHA1";
                  
                  if (copia)
                      action= "${jspServer}" + "?copia=" + copia; 
                  
   
                  
                  if (${lote!""} == true) {
                  
                      if($("input[name^='chk']:checked").length == 0){
                           alert('Nenhum arquivo selecionado');
                      }else{    
                                            
                           jQuery("input[name^='chk']:checked").each(function(index) {
                                arquivos = arquivos + '<param name="Arquivo.'+index+'" value="'+
                                           $('input[name="pdf'+this.name+'"]').val()+'"><param name="url.'+
                                           index+'" value="'+ urlbase + urlPath  +
                                           $('input[name="url'+this.name+'"]').val()+urlHash+'"/>'});
		      }
                   }else{                  
                      arquivos = '<param name="Arquivo.0" value="'+
                                       $('input[name^="pdfchk"]').val()+'"><param name="url.0" value="'+  urlbase + 
                                  urlPath + $('input[name^="urlchk"]').val()+urlHash+'"/>';
                   }  
                  
                  var parametrosPolitica;
                  if (${ext_assinatura_com_politica?string('true','false')}) {
                       parametrosPolitica = 
                         ' <param name="addCertificatePath" value="true"/>'
                         + ' <param name="policyAlias" value="ad-rb" />'
                         + ' <param name="policyAliasField" value="POLICY" />'
                         + ' <param name="certificateField" value="CERTIFICATE" />'
                         + ' <param name="policyListURL" value="${request_scheme}://${request_serverName}:${request_localPort}/siga-ext-assinatura/selecionarPolitica" />'
                         + ' <param name="hashAlgorithmField" value="HASH_ALGORITHM" />'
                         + ' <param name="envelopeType" value="cades" />'
                  } else {
                       parametrosPolitica = 
                           ' <param name="digestAlgorithm" value="SHA1"/>'
                         + ' <param name="signingAlgorithm" value="SHA1withRSA"/>';
                  }

                  var parametrosConfig;
                  if (${ext_assinatura_config_remoto?string('true','false')}) {
                       parametrosConfig = 
                           ' <param name="config.type" value="remote"/>'
                         + ' <param name="config.field" value="sdk-web-config.properties"/>'
                         + ' <param name="config.download" value="'+codebase+'/sdk-web-config.properties"/>';
                  } else {
                       parametrosConfig = 
                           ' <param name="config.type" value="local"/>';
                  }
                  var strApplet='<applet id="oApplet" codebase="'+codebase+'" code="'+code+'" archive="sdk-web.jar-1-12-0-0" width="1" height="1">'  
                  + ' <param name="cache_archive" value="sdk-web-1-12-0-0.jar"/>'
                  + ' <param name="cache_version" value="1.12.0.0"/>'
                       
                  // var strApplet = ' <applet id="oApplet" type="application/x-java-applet;version=1.6" codebase="'+codebase+'" code="'+code+'" archive="sdk-web.jar" width="1" height="1" pluginspage="http://java.com/download/" scriptable="false">'
                  //       + ' <param name="cache_archive" value="sdk-web.jar"/>'
                  //       + ' <param name="cache_option" value="Plugin">'
                  //       + ' <param name="cache_version" value="1.12.0.0"/>'
                       
                           + ' <param name="sdk-base-version" value="1.12.0.0"/>'
                           + ' <param name="userid" value="sdk-web"/>'
                           + ' <param name="mode" value="1"/>'
                           + ' <param name="autoCommit" value="true" />'
                           + ' <param name="signFunction" value="true" />'
                           + ' <param name="encryptFunction" value="false" />'
                       
                           + ' <param name="checkLibs" value="true"/>' 
                           + ' <param name="encodedFileCount" value="QTYDATA"/>'
                           + ' <param name="encodedFileId" value="IDDATA"/>'
                           + ' <param name="encodedFileParam" value="ENCDATA"/>'
                           + ' <param name="jspServer" value="'+action+'"/>'
                           + ' <param name="nextURL" value="${nextURL}"/>'
              
                           + ' <param name="allowAddFiles" value="false"/>'
                           + ' <param name="allowViewFiles" value="true"/>'
                           + ' <param name="viewGui" value="false"/>'
              
                           + ' <param name="detachedSignature" value="true"/>'

                           + ' <param name="useHashOnly" value="true"/>'
                       
                           + parametrosConfig
                           + parametrosPolitica

                           + ' <param name="colCount" value="1"/>'
                           + ' <param name="colName.0" value="Arquivo" />'
                           + ' <param name="colAlias.0" value="#arquivo" />'+arquivos+'</applet>';

                  
 
                     try {
			jQuery('#applet').html(strApplet);
		     } catch (err) {}    

                     checkAppletStarted(configurar);
             }
        </script>
[/#macro]

[#macro extensaoAssinador_apagar_depois_de_01_2014]    

       [#if lote == 'true']
          [#assign emLote = "em Lote" /]  
       [#else]
          [#assign emLote = "(Java)" /]
       [/#if]

       [#if botao == 'ambos']
          <input type="button" name="cmdConfCopia" id="cmdConfCopia" onClick="javascript: incluiApplet(true,false)" value="Conferir Cópia ${emLote!""}" class="btn btn-primary">
          <input type="button" name="cmdAssinar" id="cmdAssinar" onClick="javascript: incluiApplet(false,false)" value="Assinar ${emLote!""}" class="btn btn-primary">       
       [#else]
          <input type="button" name="cmdAssinar" id="cmdAssinar" onClick="javascript: incluiApplet(false)" value="Assinar ${emLote!""}" class="btn btn-primary">
       [/#if]
       [#if !ext_assinatura_config_remoto]
        <input type="button" name="cmdConfigurar" id="cmdConfigurar" onClick="javascript: incluiApplet(false,true)" value="Configurar Assinador" class="btn btn-primary">
       [/#if]



        <div id="applet"></div>
        
        <script type="text/javascript">                


		function checkAppletStarted(configurar) {
                  
                  var started = false;
                  if (document.applets['oApplet']) {
			try {
				started = document.applets['oApplet'].isStarted();
			} catch (err) {}
                  }
		
                  if (!started){
			window.setTimeout("checkAppletStarted()", 1000);
                  }else{
                    if (configurar) {
			document.applets['oApplet'].showConfiguration();
                    } else {
                    	document.applets['oApplet'].markAllDocuments();
                    	document.applets['oApplet'].signAndSendMarkedDocuments();
                    }
                  }

		} 

		function incluiApplet(copia,configurar){

                  
                  var urlbase= "${request_scheme}://${request_serverName}:${request_localPort}" ;
                  var codebase= urlbase + "/siga-ext-assinatura/applet";
                  var code= "br/com/esec/signapplet/DefaultSignApplet.class";  
                  var nextURL= "${nextURL}";
                  var urlPath= "${urlPath}";
                  var arquivos = "";
                  var action= "${jspServer}";
                  
                  var urlHash;
                  if (${ext_assinatura_com_politica?string('true','false')})
                    urlHash = "/semmarcas/hash/";
                  else
                    urlHash = "/semmarcas/hashSHA1/";
                  
                  if (copia)
                      action= "${jspServer}" + "?copia=" + copia; 
                  
   
                  
                  if (${lote!""} == true) {
                  
                      if($("input[name^='chk']:checked").length == 0){
                           alert('Nenhum arquivo selecionado');
                      }else{    
                                            
                           jQuery("input[name^='chk']:checked").each(function(index) {
                                arquivos = arquivos + '<param name="Arquivo.'+index+'" value="'+
                                           $('input[name="pdf'+this.name+'"]').val()+'"><param name="url.'+
                                           index+'" value="'+ urlbase + urlPath + urlHash +
                                           $('input[name="url'+this.name+'"]').val()+'"/>'});
		      }
                   }else{                  
                      arquivos = '<param name="Arquivo.0" value="'+
                                       $('input[name^="pdfchk"]').val()+'"><param name="url.0" value="'+  urlbase + 
                                  urlPath + urlHash + $('input[name^="urlchk"]').val()+'"/>';
                   }  
                  
                  var parametrosPolitica;
                  if (${ext_assinatura_com_politica?string('true','false')}) {
                       parametrosPolitica = 
                         ' <param name="addCertificatePath" value="true"/>'
                         + ' <param name="policyAlias" value="ad-rb" />'
                         + ' <param name="policyAliasField" value="POLICY" />'
                         + ' <param name="certificateField" value="CERTIFICATE" />'
                         + ' <param name="policyListURL" value="${request_scheme}://${request_serverName}:${request_localPort}/siga-ext-assinatura/selecionarPolitica" />'
                         + ' <param name="hashAlgorithmField" value="HASH_ALGORITHM" />'
                         + ' <param name="envelopeType" value="cades" />'
                  } else {
                       parametrosPolitica = 
                           ' <param name="digestAlgorithm" value="SHA1"/>'
                         + ' <param name="signingAlgorithm" value="SHA1withRSA"/>';
                  }

                  var parametrosConfig;
                  if (${ext_assinatura_config_remoto?string('true','false')}) {
                       parametrosConfig = 
                           ' <param name="config.type" value="remote"/>'
                         + ' <param name="config.field" value="sdk-web-config.properties"/>'
                         + ' <param name="config.download" value="'+codebase+'/sdk-web-config.properties"/>';
                  } else {
                       parametrosConfig = 
                           ' <param name="config.type" value="local"/>';
                  }
                  var strApplet=
                       ' <object id="oApplet"  type="application/x-java-applet;version=1.6"' 
                           + ' width="1" height="1" pluginspage="http://java.com/download/"'
                           + ' scriptable="false">'
                           + ' <param name="codebase" value="'+codebase+'"/>'
                           + ' <param name="code" value="'+code+'"/>'
                           + ' <param name="archive" value="sdk-web-1-12-0-3.jar"/>'
                      
                           + ' <param name="sdk-base-version" value="1.12.0.3"/>'
                           + ' <param name="userid" value="sdk-web"/>'
                           + ' <param name="mode" value="1"/>'
                           + ' <param name="autoCommit" value="true" />'
                           + ' <param name="signFunction" value="true" />'
                           + ' <param name="encryptFunction" value="false" />'
                       
                           + ' <param name="checkLibs" value="true"/>'
                           + ' <param name="encodedFileCount" value="QTYDATA"/>'
                           + ' <param name="encodedFileId" value="IDDATA"/>'
                           + ' <param name="encodedFileParam" value="ENCDATA"/>'
                           + ' <param name="jspServer" value="'+action+'"/>'
                           + ' <param name="nextURL" value="${nextURL}"/>'
             
                           + ' <param name="allowAddFiles" value="false"/>'
                           + ' <param name="allowViewFiles" value="true"/>'
                           + ' <param name="viewGui" value="false"/>'
             
                           + ' <param name="detachedSignature" value="true"/>'

                           + ' <param name="useHashOnly" value="true"/>'
                       
                           + parametrosConfig
                           + parametrosPolitica

                           + ' <param name="colCount" value="1"/>'
                           + ' <param name="colName.0" value="Arquivo" />'
                      + ' <param name="colAlias.0" value="#arquivo" />'+arquivos+'</object>';
                  
 
                     try {
			jQuery('#applet').html(strApplet);
		     } catch (err) {}    

                     checkAppletStarted(configurar);
             }
        </script>
[/#macro]


[#macro extensaoAssinador2_apagar_depois_de_01_2014]    
       [#if lote == 'true']
          [#assign emLote = "em Lote" /]  
       [#else]
          [#assign emLote = "(Java)" /]
       [/#if]

       [#if botao == 'ambos']
          <input type="button" name="cmdConfCopia" id="cmdConfCopia" onClick="javascript: incluiApplet(true,false)" value="Conferir Cópia ${emLote!""}" class="btn btn-primary">
          <input type="button" name="cmdAssinar" id="cmdAssinar" onClick="javascript: incluiApplet(false,false)" value="Assinar ${emLote!""}" class="btn btn-primary">       
       [#else]
          <input type="button" name="cmdAssinar" id="cmdAssinar" onClick="javascript: incluiApplet(false)" value="Assinar ${emLote!""}" class="btn btn-primary">
       [/#if]
       [#if !ext_assinatura_config_remoto]
        <input type="button" name="cmdConfigurar" id="cmdConfigurar" onClick="javascript: incluiApplet(false,true)" value="Configurar Assinador" class="btn btn-primary">
       [/#if]



        <div id="applet"></div>
        
        <script type="text/javascript">                


		function checkAppletStarted(configurar) {
                  
                  var started = false;
                  if (document.applets['oApplet']) {
			try {
				started = document.applets['oApplet'].isStarted();
			} catch (err) {}
                  }
		
                  if (!started){
			window.setTimeout("checkAppletStarted()", 1000);
                  }else{
                    if (configurar) {
			document.applets['oApplet'].showConfiguration();
                    } else {
                    	document.applets['oApplet'].markAllDocuments();
                    	document.applets['oApplet'].signAndSendMarkedDocuments();
                    }
                  }

		} 

		function incluiApplet(copia,configurar){

                  
                  var urlbase= "${request_scheme}://${request_serverName}:${request_localPort}" ;
                  var codebase= urlbase + "/siga-ext-assinatura/applet";
                  var code= "br/com/esec/signapplet/DefaultSignApplet.class";  
                  var nextURL= "${nextURL}";
                  var urlPath= "${urlPath}";
                  var arquivos = "";
                  var action= "${jspServer}";
                  
                  var urlHash = '';
                  //if (${ext_assinatura_com_politica?string('true','false')})
                  //  urlHash = "/semmarcas/hash/";
                  //else
                  //  urlHash = "/semmarcas/hashSHA1/";
                  
                  if (copia)
                      action= "${jspServer}" + "?copia=" + copia; 
                  
   
                  
                  if (${lote!""} == true) {
                  
                      if($("input[name^='chk']:checked").length == 0){
                           alert('Nenhum arquivo selecionado');
                      }else{    
                                            
                           jQuery("input[name^='chk']:checked").each(function(index) {
                                arquivos = arquivos + '<param name="Arquivo.'+index+'" value="'+
                                           $('input[name="pdf'+this.name+'"]').val()+'"><param name="url.'+
                                           index+'" value="'+ urlbase + urlPath + urlHash +
                                           $('input[name="url'+this.name+'"]').val()+'"/>'});
		      }
                   }else{                  
                      arquivos = '<param name="Arquivo.0" value="'+
                                       $('input[name^="pdfchk"]').val()+'"><param name="url.0" value="'+  urlbase + 
                                  urlPath + urlHash + $('input[name^="urlchk"]').val()+'?semmarcas=1"/>';
                   }  
                 
                  var parametrosPolitica;
                  if (${ext_assinatura_com_politica?string('true','false')}) {
                       parametrosPolitica = 
                         ' <param name="addCertificatePath" value="true"/>'
                         + ' <param name="policyAlias" value="ad-rb" />'
                         + ' <param name="policyAliasField" value="POLICY" />'
                         + ' <param name="certificateField" value="CERTIFICATE" />'
                         + ' <param name="policyListURL" value="${request_scheme}://${request_serverName}:${request_localPort}/siga-ext-assinatura/selecionarPolitica" />'
                         + ' <param name="hashAlgorithmField" value="HASH_ALGORITHM" />'
                         + ' <param name="envelopeType" value="cades" />'
                  } else {
                       parametrosPolitica = 
                           ' <param name="digestAlgorithm" value="SHA1"/>'
                         + ' <param name="signingAlgorithm" value="SHA1withRSA"/>';
                  }

                  var parametrosConfig;
                  if (${ext_assinatura_config_remoto?string('true','false')}) {
                       parametrosConfig = 
                           ' <param name="config.type" value="remote"/>'
                         + ' <param name="config.field" value="sdk-web-config.properties"/>'
                         + ' <param name="config.download" value="'+codebase+'/sdk-web-config.properties"/>';
                  } else {
                       parametrosConfig = 
                           ' <param name="config.type" value="local"/>';
                  }
                  var strApplet='<applet id="oApplet" codebase="'+codebase+'" code="'+code+'" archive="sdk-web.jar-1-12-0-0" width="1" height="1">'  
                  + ' <param name="cache_archive" value="sdk-web-1-12-0-0.jar"/>'
                  + ' <param name="cache_version" value="1.12.0.0"/>'
                       
                  // var strApplet = ' <applet id="oApplet" type="application/x-java-applet;version=1.6" codebase="'+codebase+'" code="'+code+'" archive="sdk-web.jar" width="1" height="1" pluginspage="http://java.com/download/" scriptable="false">'
                  //       + ' <param name="cache_archive" value="sdk-web.jar"/>'
                  //       + ' <param name="cache_option" value="Plugin">'
                  //       + ' <param name="cache_version" value="1.12.0.0"/>'
                       
                           + ' <param name="sdk-base-version" value="1.12.0.0"/>'
                           + ' <param name="userid" value="sdk-web"/>'
                           + ' <param name="mode" value="1"/>'
                           + ' <param name="autoCommit" value="true" />'
                           + ' <param name="signFunction" value="true" />'
                           + ' <param name="encryptFunction" value="false" />'
                       
                           + ' <param name="checkLibs" value="true"/>' 
                           + ' <param name="encodedFileCount" value="QTYDATA"/>'
                           + ' <param name="encodedFileId" value="IDDATA"/>'
                           + ' <param name="encodedFileParam" value="ENCDATA"/>'
                           + ' <param name="jspServer" value="'+action+'"/>'
                           + ' <param name="nextURL" value="${nextURL}"/>'
              
                           + ' <param name="allowAddFiles" value="false"/>'
                           + ' <param name="allowViewFiles" value="true"/>'
                           + ' <param name="viewGui" value="false"/>'
              
                           + ' <param name="detachedSignature" value="true"/>'

                           + ' <param name="useHashOnly" value="true"/>'
                       
                           + parametrosConfig
                           + parametrosPolitica

                           + ' <param name="colCount" value="1"/>'
                           + ' <param name="colName.0" value="Arquivo" />'
                           + ' <param name="colAlias.0" value="#arquivo" />'+arquivos+'</applet>';

                  
 
                     try {
			jQuery('#applet').html(strApplet);
		     } catch (err) {}    

                     checkAppletStarted(configurar);
             }
        </script>
[/#macro]

[#macro complementoHEAD]
<!-- Google Analytics -->
<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setDomainName', 'none']);
  _gaq.push(['_setAccount', 'UA-36509025-1']);
  _gaq.push(['_trackPageview']);
  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>
[/#macro]

[#macro requerimentoTrf texto fecho="" tamanhoLetra="Normal" _tipo="" vocat=""]

    [#if tamanhoLetra! == "Normal"]
        [#assign tl = "11pt" /]
    [#elseif tamanhoLetra! == "Pequeno"]
        [#assign tl = "9pt" /]
    [#elseif tamanhoLetra! == "Grande"]
        [#assign tl = "13pt" /]
    [#else]     
        [#assign tl = "11pt" /]
    [/#if]

[@estiloBrasaoCentralizadoTrf tipo=_tipo tamanhoLetra=tl formatarOrgao=true dataAntesDaAssinatura=true]
    [@br/]
    <center><b><p>${vocat!}</p></b></center>
    <span style="font-size: tl"> ${texto!}</span>
    <p style="align: justify; TEXT-INDENT: 0cm">${fecho}</p>
[/@estiloBrasaoCentralizadoTrf]

[/#macro]

[#macro cabecalhoCentralizadoPrimeiraPaginaTrf tipo=""]
<table style="float:none; clear:both;" width="100%" align="left" border="0" cellpadding="0"
    cellspacing="0" bgcolor="#FFFFFF">
    <tr bgcolor="#FFFFFF">
        <td width="100%">
        <table width="100%" border="0" cellpadding="2">
[#--            [#if tipo == "PORTARIA EMARF"]
            <tr>
                <td width="100%" align="center" valign="bottom"><img src="contextpath/imagens/brasaoemarf.jpg" width="65" height="65" /></td>
            </tr>
            [#else]
--]            
            <tr>
                <td width="100%" align="center" valign="bottom"><img src="${_pathBrasao}" width="65" height="65" /></td>
            </tr>
            
            <tr>
                <td width="100%" align="center">
                <p style="font-family: AvantGarde Bk BT, Arial; font-size: 11pt;">${_tituloGeral}</p>
                </td>
            </tr>
            [#if _subtituloGeral?has_content]
            <tr>
                <td width="100%" align="center">
                <p style="font-family: Arial; font-size: 10pt; font-weight: bold;">${_subtituloGeral}</p>
                </td>
            </tr>
            [/#if]
            
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
[#--            [/#if] --]
        </table>
        </td>
    </tr>
</table>
[/#macro]

[#macro estiloBrasaoCentralizadoTrf tipo tamanhoLetra="11pt" formatarOrgao=true numeracaoCentralizada=false dataAntesDaAssinatura=false omitirCodigo=false omitirData=false topoPrimeiraPagina='']
    [@primeiroCabecalho]${topoPrimeiraPagina!}
    [@cabecalhoCentralizadoPrimeiraPaginaTrf tipo/]
    [/@primeiroCabecalho]
    [@cabecalho]
    [@cabecalhoCentralizado/]
    [/@cabecalho]
    [@letra tamanhoLetra]
        [#if !numeracaoCentralizada]
              <table style="float:none; clear:both;" width="100%" border="0" bgcolor="#FFFFFF">
              <tr>
              <td align="left">
              [#if !omitirCodigo]
                <p style="font-family:Arial;align:center;font-weight:bold;font-size:11pt;"><br/>[@numeroDJE][#if tipo != ""] ${tipo} SIGA N&ordm; ${(doc.codigo)!} [/#if] [/@numeroDJE]</p>
              [/#if]
              </td>
              </tr>
              [#if !dataAntesDaAssinatura && !omitirData]
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
                  [@numeroDJE] [#if tipo != ""] ${tipo} SIGA N&ordm; ${(doc.codigo)!} [/#if] [/@numeroDJE]
                  [#if !dataAntesDaAssinatura && doc?? && doc.dtD??] de ${doc.dtD!} de ${doc.dtMMMM!} de ${doc.dtYYYY!}[/#if]</p>
              </td>
              </tr>
              </table>
        [/#if]

        [@tituloDJE]
            <center>${tipo!} ${(doc.codigo)!} de ${doc.dtD!} de ${doc.dtMMMM!} de ${doc.dtYYYY!}</center>
        [/@tituloDJE]        
        [#nested]
        [#if dataAntesDaAssinatura]<p style="text-align:center">[#if mov??]${mov.dtExtenso!}[#else]${doc.dtExtenso!}[/#if]</p>[/#if]
        <p>&nbsp;</p>
        [#if mov??]
              [@assinaturaMovCentro formatarOrgao=formatarOrgao/]
        [#else]
              [@assinaturaCentro formatarOrgao=formatarOrgao/]
        [/#if]
        [@fimMioloDJE]
        [/@fimMioloDJE]

   [/@letra]

   [@primeiroRodape]
   [@rodapeClassificacaoDocumental/]
   [/@primeiroRodape]
   [@rodape]
   [@rodapeNumeracaoADireita/]
   [/@rodape]
[/#macro]

[#macro numero var titulo="" largura="" maxcaracteres="" idAjax="" reler="" relertab="" obrigatorio="nao" default=""]
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

	<div class="form-group" style="margin-bottom:0">
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
	
	    [#if !gerar_formulario!false]    	    	  
		   	[#if titulo?? && titulo != ""]<label for="${var}" style="${negrito!};${vermelho!}">${titulo}</label>[/#if]					
		    <input onkeypress="javascript: var tecla=(window.event)?event.keyCode:e.which;if((tecla>47 && tecla<58)) return true;  else{  if (tecla==8 || tecla==0) return true;  else  return false;  }" 
				id="${var}" type="text" name="${var}" value="${v}" ${jreler!""}${jrelertab!""}${jlargura!""}${jmaxcaracteres!""} class="form-control"/>			 					
			<div class="invalid-feedback  invalid-feedback-${var}">Preenchimento obrigatório</div>			         
	    [#else]
	    <span class="valor">${v}</span>
	    [/#if]
    </div>
[/#macro]

[#macro horaMinuto titulo var reler=false idAjax="" default="" alerta=false obrigatorio=false]
    [#if reler == true && idAjax != ""]
            [#local jreler = " sbmt('" + idAjax + "');\""]
    [#elseif reler == true]
            [#local jreler = " sbmt();\""]
    [/#if]

    [#local v = .vars[var]!default]

	<div class="form-group" style="margin-bottom:0">
	    [#if obrigatorio]
			[#local negrito = "font-weight:bold"]
			<input type="hidden" name="obrigatorios" value="${var}" />
	    [/#if]          
	
	    [#if !gerar_formulario!false]
	        <input type="hidden" name="vars" value="${var}" />                            	  
			[#if titulo?? && titulo != ""]<label for="${var}" style="${negrito!};${vermelho!}">${titulo}</label>[/#if] 			
			<input type="text" id="${var}" name="${var}" value="${v}" size="6" maxlength="5" class="form-control  campoHoraMinuto" style="max-width: 70px" placeholder="00:00" />
			<div class="invalid-feedback  invalid-feedback-${var}">Preenchimento obrigatório</div>
	    [#else]
			<span class="valor">${v}</span>
	    [/#if]
	</div>	    
[/#macro]

[#macro webservice var url timeout cache=""]
  [#if cache?has_content]
    <input type="hidden" name="vars" value="${cache}" />
  [/#if]
  [#if cache?has_content && .vars[cache]??]
    [#local str=.vars[cache] /]
    <input type="hidden" name="${cache}" value="${str}">
  [#else]
    [#local payload][#nested][/#local]
    [#local str=func.webservice(url,payload,timeout) /]
    <input type="hidden" name="${cache}" value="${str?url('UTF-8')}">
  [/#if]
  [#if str?has_content]
     [#local retornoSource="[#assign " + var + "=func.parseXML(str) /]"/]
  [#else]
     [#local retornoSource="[#assign " + var + "=str /]"/]
  [/#if]
  [#local retornoTemplate = retornoSource?interpret]
  [@retornoTemplate /] 
[/#macro]

[#macro descricao]
   [#if gerar_descricao!false]
    <!-- descricao -->
      [#nested]
    <!-- /descricao -->
   [/#if]
[/#macro]

[#macro classificacao codigo]
   <input type="hidden" name="vars" value="codigoClassificacao" />
   <input type="hidden" id="codigoClassificacao" name="codigoClassificacao" value="${codigo}" />
[/#macro]

[#macro solicitacao tamanhoLetra="Normal" _tipo="FORMULÁRIO" assunto=""]
    [#if tamanhoLetra! == "Normal"]
        [#assign tl = "11pt" /]
    [#elseif tamanhoLetra! == "Pequeno"]
        [#assign tl = "9pt" /]
    [#elseif tamanhoLetra! == "Grande"]
        [#assign tl = "13pt" /]
    [#else]     
        [#assign tl = "11pt"]
    [/#if]
    [@estiloBrasaoCentralizado tipo=_tipo tamanhoLetra=tl formatarOrgao=false numeracaoCentralizada=false incluirMioloDJE=false]
              <table style="float:none;" width="100%" border="0" cellpadding="2" cellspacing="0" bgcolor="#FFFFFF">
                  <tr>
                     <td align="left" style="text-align: justify; font-family: Arial; font-size: ${tl};"><br/>Assunto: ${assunto}</td>
                  </tr>
              </table>
            <br/>
            [#nested] 
     [/@estiloBrasaoCentralizado]
[/#macro]

[#macro pessoaLotacao titulo var reler=false relertab="" buscarFechadas=false idAjax="" default="" obrigatorio=false paramList=""]
[@selecaoX2 titulo=titulo var=var opcoes="Matrícula;Orgão Integrado" reler=true idAjax=idAjax/]   
    [#if buscarFechadas]
        [@assign paramList = "buscarFechadas=true" /]
    [/#if]
[@grupoX2 depende=idAjax]
  [#if .vars[var] == "Orgão Integrado"]  
[@selecionavel tipo="lotacao" titulo="" var=var reler=reler idAjax=idAjax+"1" relertab=relertab paramList=paramList obrigatorio=obrigatorio /]
  [#else]
[@selecionavel tipo="pessoa" titulo="" var=var reler=reler idAjax=idAjax+"2" relertab=relertab paramList=paramList obrigatorio=obrigatorio /]
  [/#if]
[/@grupoX2]
[/#macro]


[#macro divX2 id="" depende="" suprimirIndependente=false] [#-- macro utilizada na macro pessoaLotacao --]
    [#if suprimirIndependente || depende != ""]
        <div style="float: left" [#if id != ""] id="${id}"[/#if][#if depende != ""] depende=";${depende};"[/#if]>[#if id != ""]<!--ajax:${id}-->[/#if][#nested][#if id != ""]<!--/ajax:${id}-->[/#if]</div>
    [/#if]
[/#macro]

[#macro grupoX2 titulo="" largura=0 depende="" esconder=false]  [#-- macro utilizada na macro pessoaLotacao --]
    [#if !esconder]
    [#local id = (depende=="")?string("", "div" + depende)] 
    [@divX2 id=id depende=depende suprimirIndependente=true]
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
            [#if (grupoLarguraTotal >= 100)]
                </td>
                </table>
                [#assign grupoLarguraTotal = 0/]
            [/#if]
        [/#if]
    [/@divX2]
    [#else]
        [#nested]
    [/#if]
[/#macro]

[#macro selecaoX2 var titulo opcoes reler=false idAjax="" onclick=""]  [#-- macro utilizada na macro pessoaLotacao --]
    [#local l=opcoes?split(";")]
    [#if .vars[var]??]
        [#local v = .vars[var]/]
    [#else]
        [#local v = l?first/]
                [#assign inlineTemplate = ["[#assign ${var} = v/]", "assignInlineTemplate"]?interpret /]
                [@inlineTemplate/]
        [/#if]
    
<div style="float:left">    ${titulo!""}[#if titulo != ""]:[/#if]

    [#if !gerar_formulario!false]
        <input type="hidden" name="vars" value="${var}" />
        <select name="${var}" [#if reler] onchange="javascript: sbmt([#if idAjax != ""]'${idAjax}'[/#if]);"[/#if] onclick="${onclick}" class="form-control">
                    [#list l as opcao]
                        <option[#if v == opcao] selected[/#if] value="${opcao}">${opcao}</option><br/>
            [/#list]
        </select>
    [#else]
        <span class="valor">${v}</span>
    [/#if]
</div>
[/#macro]

[#function acerto orgao=""]
  [#if orgao == ""]
     [#return "sem lotação" /]
  [#elseif func.contains(orgao,"NÚCLEO") || (func.contains(orgao,"GABINETE") && !func.contains(orgao,"RECURSAL")) || func.contains(orgao,"COMIT") || func.contains(orgao,"CENTRO") || 
           func.contains(orgao,"JUIZADO ESP") || func.contains(orgao,"MAGISTRADO") || func.contains(orgao,"SERVIDORES") || func.contains(orgao,"SETOR")]
     [#return "no" /]
  [#else]
     [#return "na" /]
  [/#if]
[/#function]

[#function quadroLotacao mat=""]
    [#if mat == ""]
         [#return "Lotação Indeterminada" /]
    [#elseif func.contains(mat,"T2")]
         [#return "T2" /]
    [#elseif func.contains(mat,"RJ")]
         [#return "RJ" /]
    [#else]
         [#return "ES" /]
    [/#if]
[/#function]

[#macro materiasBoletimInterno materias]
				[#assign ultAbertura="" /]
            	[#list materias as d]   
					[#if ultFecho?? && ultFecho != d.fecho]
						<div align="center">${ultFecho!}</div><br/>
					[/#if]
                    <span style="font-weight: bold">${d.numero}</span>
                    [#if ultAbertura != d.abertura]
                    	<div align="center">${d.abertura}</div><br/>
                    [/#if]
					${d.conteudo}
					[#assign ultFecho=d.fecho /]
                    [#assign ultAbertura=d.abertura /]
                    <br/>
				[/#list]
		        <div align="center">${ultFecho!}</div>
                <br/>
				[#assign ultFecho="" /]
                [#assign ultAbertura="" /]
            [/#macro]

[#macro boletimInterno xmlHierarquia templateInfosComplementaresEntrevista templateInfosComplementaresDocumento pathImgCabecalho]
														
	[@entrevista acaoGravar="gravarBIE" acaoExcluir="excluirBIE" acaoCancelar="refazerBIE" acaoFinalizar="finalizarBIE"]
		${templateInfosComplementaresEntrevista}
		[@grupo titulo="Documentos a Publicar"]
			<table>
				[#assign publicacoes = func.consultarDocsDisponiveisParaInclusaoEmBoletim(doc.lotaCadastrante.orgaoUsuario)!/]
				[#if publicacoes??]
					[#list publicacoes as ex ]
						<tr>
							<td>[@checkbox var="doc_boletim${ex.idDoc!}" titulo="" default="Sim"/]</td>
							<td><a href="javascript:void(0)" onclick="javascript: window.open('/sigaex/expediente/doc/exibir.action?popup=true&sigla=${ex.sigla!}', '_new', 	'width=700,height=500,scrollbars=yes,resizable')">${ex.codigo!}</a></td>
							<td>&nbsp;&nbsp;&nbsp;&nbsp;${ex.dtFinalizacaoDDMMYY!}</td>
							<td>&nbsp;&nbsp;&nbsp;&nbsp;${(ex.lotaCadastrante.sigla)!}</td>
							<td>&nbsp;&nbsp;&nbsp;&nbsp;${ex.descrDocumento!}</td>
							<td>&nbsp;&nbsp;&nbsp;&nbsp;${ex.sigla!}</td>
							<td>&nbsp;&nbsp;&nbsp;&nbsp;${ex.idDoc!}</td>
							<td style="padding-left: 30px">
								<a href="/sigaex/expediente/mov/cancelar_pedido_publicacao_boletim.action?sigla=${ex.sigla!}">Cancelar Pedido</a>
							</td>	
						</tr>
					[/#list]
				[/#if]
			</table>
		[/@grupo]
		
		[@grupo titulo="Documentos Selecionados"]
			<table>
				[#assign publicacoesPorDoc = (func.consultarDocsInclusosNoBoletim(doc))! /]			      	
				[#if publicacoesPorDoc??]
					[#list publicacoesPorDoc as ex]
						<tr>
							<td>[@checkbox var="doc_boletim${ex.idDoc!}" titulo="" default="Sim"/]</td>
							<td><a href="javascript:void(0)"
								onclick="javascript: window.open('/sigaex/expediente/doc/exibir.action?popup=true&sigla=${ex.sigla!}', '_new', 'width=700,height=500,scrollbars=yes,resizable')">${ex.codigo!}</a></td>
							<td style="padding-left: 30px">&nbsp;&nbsp;&nbsp;&nbsp${ex.dtFinalizacaoDDMMYY!}</td>
							<td style="padding-left: 30px">&nbsp;&nbsp;&nbsp;&nbsp${ex.lotaCadastrante.sigla!}</td>
							<td style="padding-left: 30px">&nbsp;&nbsp;&nbsp;&nbsp${ex.descrDocumento!}</td>
						</tr>
					[/#list]
				[/#if]
			</table>
		[/@grupo]
	[/@entrevista]
	
	[@documento]
    
			<!-- INICIO PRIMEIRO CABECALHO
			<table width="100%" border="0" bgcolor="#FFFFFF"><tr><td>
				<table width="100%" align="left" border="0" bgcolor="#FFFFFF">
					<tr bgcolor="#FFFFFF">
						<td align="left" valign="bottom" width="100%"><img
							src="${pathImgCabecalho}" width="450" height="65" /></td>
					</tr>
				</table>
			</td></tr>
			<tr>
				<td style="border-width: 2px 0px 2px 0px; border-style: solid; padding-bottom: 0px">
					<table width="100%">
						<tr>
							<td align="left" width="50%">
								${doc.codigo!}
							</td>
							<td align="right" width="50%">
								Publicação Diária - Data: ${doc.dtDocDDMMYYYY!}
							</td>	
						</tr>
					</table>
				</td>
			</tr>
			</table>
			
			FIM PRIMEIRO CABECALHO -->		
	
			[@br/]
			[@br/]
			[@br/]
	
			<!-- INICIO CABECALHO
				<table width="100%" bgcolor="#FFFFFF">
					<tr>
						<td valign="bottom" align="center" style="border-width: 0px 0px 1px 0px; border-style: solid; padding-top: 3px">
							<span style="font-size:11px">${doc.codigo!} de ${doc.dtDocDDMMYY!}</span>
						</td>
					</tr>
				</table>
			FIM CABECALHO -->		
		        
			[#if lotaCadastrante??]
				[#assign hBIE=func.obterBIE(doc, xmlHierarquia)!/]
			[/#if]
	
			[#if hBIE??]
				[#list hBIE.topicos as topico]
					[#if !topico.vazio]
						<table align="left" border="0" width="100%" bgcolor="#FFFFFF">
							<tr>
                            	[#if topico.descr??]
								<td valign="bottom" style="border-width: 0px 0px 0px 0px; border-style: solid; padding-top: 3px">
								<h2>${(topico.descr)!}</h2>
								</td>
                                [/#if]
							</tr>
						</table>	   
                        [#if topico.materias??]
                        	[@materiasBoletimInterno topico.materias /]
                        [/#if]
						[#if topico.topicos??]
                        	[#list topico.topicos as subTopico]
		                    	[#if !subTopico.vazio]
									<span style="page-break-inside: avoid">
										[#if subTopico.descr??]<h3 align="right">${(subTopico.descr)!}</h3>[/#if]
										[@materiasBoletimInterno subTopico.materias /]
									</span>
                                    <br/>
		                        [/#if] 
							[/#list] 
                        [/#if]
                        <br/><br/>
					[/#if]
				[/#list] 
			[/#if] 
	
			<span>&nbsp;</span>
			[@br/]
			<center><span style="font-size: 15px;">********************************* FIM *********************************</span></center>
		
		
			<!-- INICIO PRIMEIRO RODAPE
			[#assign idOrgaoUsu=""/]
			[#assign acronimoOrgaoUsu=""/]
			[#assign descricaoOrgaoUsu=""/]
	
			[#if !mov??]
				[#if doc.lotaCadastrante??]
					[#assign descricaoOrgaoUsu=(doc.lotaCadastrante.orgaoUsuario.descricao)!/]
					[#assign idOrgaoUsu=(doc.lotaCadastrante.orgaoUsuario.idOrgaoUsu)!/]
					[#assign acronimoOrgaoUsu=(doc.lotaCadastrante.orgaoUsuario.acronimoOrgaoUsu)!/]
				[/#if]
			[#else]
				[#assign descricaoOrgaoUsu=mov.lotaCadastrante.orgaoUsuario.descricao/]
				[#assign idOrgaoUsu=mov.lotaCadastrante.orgaoUsuario.idOrgaoUsu/]
				[#assign acronimoOrgaoUsu=mov.lotaCadastrante.orgaoUsuario.acronimoOrgaoUsu/]
			[/#if]
	
			<span align="center">
			<table bgcolor="#000000" width="96%">
				<tr>
					<td width="100%">
						<table width="100%" align="left" bgcolor="#FFFFFF">
							<col width="15%"></col>
							<col width="85%"></col>
							<tr bgcolor="#FFFFFF">
								<td width="15%" align="left" valign="bottom"><img
									src="${_pathBrasao}" width="65" height="65" />
								</td>
								<td>
									<table align="left" width="100%" bgcolor="#FFFFFF">
										<tr>
											<td width="18%" width="100%" align="left">
											<p style="font-size: 11pt;">${func.resource('modelos.cabecalho.titulo')!}</p>
											</td>
										</tr>
										[#if func.resource('siga.ex.modelos.cabecalho.subtitulo')??]
											<tr>
												<td width="100%" align="left">
												<p style="font-size: 10pt; font-weight: bold;">${func.resource('modelos.cabecalho.subtitulo')!}</p>
												</td>
											</tr>
										[/#if]
										<tr>
											<td width="100%" align="left">
												<p style="font-size: 8pt;">
												[#if !mov??]
													[#if doc.lotaCadastrante??]
														${(doc.lotaCadastrante.orgaoUsuario.descricaoMaiusculas)!}
													[/#if]
												[#else]
													${(mov.lotaCadastrante.orgaoUsuario.descricaoMaiusculas)!}
												[/#if]</p>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>						
					</td>
				</tr>
				<tr>
					<td width="100%" style="padding-left: 0px; margin-left: 0px;">
						${templateInfosComplementaresDocumento}
					</td>
				</tr>
			</table>			
	        </span>		
		FIM PRIMEIRO RODAPE -->
	
		<!-- INICIO RODAPE
			<table width="100%" border="0" cellpadding="0" bgcolor="#000000">
				<col></col>
				<tr>
					<td width="100%" align="right"><div id="numero_pagina" /></td>
				</tr>
			</table>
		FIM RODAPE -->		
	[/@documento]													
[/#macro]

[#macro diminuirEspacamento texto=""]
<style>
  p {
	margin-bottom:0px;
	margin-top:0px;
  }
  ol {
    margin-bottom:0px;
	margin-top:0px;
  }
  ol li {
    margin-bottom:0px;
	margin-top:0px;
  }
  ul {
    margin-bottom:0px;
	margin-top:0px;
  }
  ul li {
    margin-bottom:0px;
	margin-top:0px;
  }
</style>
${texto} 
[/#macro]

[#assign _pathBrasao = "contextpath/imagens/brasaoColoridoTRF2.png" /]
[#assign _pathBrasaoSecundario = "contextpath/imagens/Logotipo_Prodesp_Governo_SP.png" /]
[#assign _widthBrasao = "auto" /]
[#assign _heightBrasao = "65" /]
[#assign _tituloGeral = "PODER JUDICIÁRIO" /]
[#assign _subtituloGeral = "JUSTIÇA FEDERAL" /]
