Cadastro de Configurações
=========================

Um usuário com as devidas permissões pode acessar a página de cadastro de configurações. Para isso, o usuário escolherá, no menu principal, a opção Siga > Ferramentas > Cadastro de Configurações.

.. image:: cadastro-de-configuracoes-menu.png

A página inicial permite a pesquisa em configurações e possui links para editar ou remover uma configuração, além de um botão para criar uma nova configuração.

.. image:: cadastro-de-configuracoes.png

A página de edição/criação de configuração possui diversos campos que devem ser preenchidos de modo a especificar qual é o tipo da configuração, a quem a configuração se refere, e a que espécie/modelo ela está relacionada.

.. image:: cadastro-de-configuracoes-edicao.png

As configurações alteram o comportamento normal do sistema em diversas áreas diferentes.

A lista completa de configurações e detalhes de como cada uma delas funciona podem ser vistos a seguir.

Configurações do Módulo de Gestão de Documentos
===============================================

Acessar
-------

Esta é uma configuração bastante delicada. A função dela é permitir que determinadas pessoas tenham acesso 
a documentos sigilosos mesmo sem estarem na lista de permissões.

O ideal é que essa configuração nunca seja 
utilizada. Melhor ainda seria apagar esse registro da tabela CP_TIPO_CONFIGURACAO para garantir que não 
será acidentalmente atribuída a ninguém.

Atender Pedido de Publicação  
----------------------------


Autuável  
--------

Indica se determinado modelo ou espécie pode ser alvo te autação. Por exemplo, se queremos autuar um memorando, qual
a lista de modelos que deve ser apresentada para o usuário escolher. Normalmente, os modelos e espécies configurados
devem ser aqueles de Processos Administrativos.

Cadastrar Qualquer Subst  
------------------------

Utilizada para configurar quais são as pessoas que tem permissão de cadastrar qualquer substituição. Nomalmente, a regra
de negócio diz que uma pessoa só pode cadastrar substitutos para si mesma ou para sua lotação, ou outras regras envolvendo
hierarquias. No entanto, uma pessoa indicada para "Cadastrar Qualquer Substituição" poderá cadastrar em nome de outras. 
Esta configuração normalmente é atribuída aos administradores do sistema ou a equipe de suporte.

Cancelar Movimentação  
---------------------

Por padão as movimentações não podem ser canceladas. Utilizando essa configuração, é possível habilitar o
cancelamento de movimentações de determinado tipo, dependendo ainda de regras de negócios específicas. Por exemplo,
para que seja possível excluir "Arquivos Auxiliares" é necessário criar uma configuração habilitando o cancelamento
de "Anexação de Arquivo Auxiliar".

Cancelar Via  
------------

Indica situações nas quais uma via pode ser cancelada.

Configurar  
----------


Corrigir Erro  
-------------


Criar  
-----

Normalmente todos os modelos de documentos podem ser criados por qualquer usuário, mas restrições podem ser impostas
através dessa configuração. Modelos ou espécies específicas podem ser restritas de modo que apenas algumas pessoas
ou lotações tenham permissão para criá-las.

Criar como Novo
---------------

Criar Via  
---------

Utilize essa configuração para desabilitar o botão "Criar Via" em alguns ou todos os modelos de expedientes.

Definir Publicadores  
--------------------


Definição Automática de Perfil
------------------------------


Destinatário
------------


Diretor do Foro  
---------------


Duplicar  
--------

Configura quem e quais modelos podem ser duplicados. Por padrão, qualquer documento poderá ser duplicado por 
qualquer pessoa.

Editar  
------

Desabilita a edição de determinados modelos de documentos por determinadas pessoas. Por padrão, a edição é sempre
permitida, exceto quando viola regras de negócio.

Editar Data  
-----------

Define se a data do documento será editável ou se ela será gerada automaticamente 
pelo sistema em função da data em que o documento foi finalizado ou assinado. 

O ideal é que ninguém tenha permissão de editar a data, considerando que não é boa 
prática criar documentos com datas futuras ou retroativas.

Editar Descrição  
----------------

Define se a descrição será editável ou se será gerada automaticamente pelo sistema. Caso o sistema crie a 

Eletrônico  
----------

Identifica os modelos documentos que são exclusivamente digitais, fisícos ou os que podem assumir as duas formas. Para
forçar um modelo de documento a ser digital, utilize a situação "Obrigatório".


Excluir  
-------


Excluir Anexo  
-------------


Excluir Anotação  
----------------


Finalizar  
---------


Gerenciar Publicação Boletim  
----------------------------


Incluir como Filho  
------------------


Incluir Documento
-----------------


Juntada Automática
------------------


Movimentar
----------


Nível de Acesso  
---------------


Nível de Acesso Máximo  
----------------------


Nível de Acesso Mínimo  
----------------------


Notificar Por E-mail  
--------------------


Pode Assinar sem Solicitação
----------------------------


Pode criar documento filho  
--------------------------


Pode receber documento sem assinatura
-------------------------------------


Refazer
-------


Reiniciar Numeração Todo Ano  
----------------------------

	    
Simular Usuário
---------------


Trâmite Automático  
------------------

	    
Usar Lista  
----------

	    
Utilizar Extensão de Conversor HTML  
-----------------------------------

	    
Utilizar Extensão de Editor  
---------------------------


Visualizar Impressão
--------------------


