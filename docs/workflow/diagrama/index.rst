Cadastro de Diagramas
=====================

Um usuário com as devidas permissões pode acessar a página de cadastro de diagramas. Para isso, o usuário escolherá, no menu do módulo de workflow, a opção Ferramentas > Cadastro de Diagramas.

.. image:: cadastro-de-diagramas-menu.png

A página inicial permite a pesquisa em diagramas previamente cadastrados e possui links para editar ou remover um diagrama, além de um botão para criar um novo diagrama.

.. image:: cadastro-de-diagramas-listagem.png

A página de edição/criação de diagrama possui diversos campos que devem ser preenchidos de modo a especificar qual é nome e a descrição do diagrama, quem tem acesso para editá-lo e iniciá-lo, e qual é a lotação e a pessoa que são responsáveis por este diagrama.

.. image:: cadastro-de-diagramas-edicao.png

Para definir as tarefas que compõem um diagrama, o usuário deve clicar em "+ Tarefa" e prencher os campos que serão apresentados.

Primeiro ele escolherá o tipo da tarefa, numa lista que será detalhada mais a frente. 
Também deve preencher o título que será exibido no diagrama.
Caso haja a necessidade de desviar para uma outra tarefa após a conclusão desta, a próxima tarefa deve ser informada no campo "Depois", se este campo for deixado em branco, após esta tarefa será executada a seguinte na lista. 
Dependendo do tipo serão apresentados outros campos que serão descritos a seguir.

A lista completa de tipos de tarefas, detalhes de como cada uma delas funciona e quais são os campos adicionais podem ser vistos a seguir.

Tipos de Tarefas
================

Formulário
----------

Um formulário indica uma tarefa que deve ser realizada por um usuário, uma tarefa "humana" no jargão do workflow. Essa é a mais complexa das tarefas porque ela envolve três conceitos importantes: responsável, variável e desvio.

Primeiro deve ser selecionado o tipo de responsável de uma lista bem extensa. Por exemplo, podemos selecionar uma "Lotação" ou uma "Pessoa" específica para simplificar, mas existem muitas outras opções. A lista completa está mais a frente.

Basicamente, o usuário pode interagir com o sistema visualizando ou informando o valor de variáveis associadas a esse procedimento, e também pode decidir para onde o fluxo deve seguir escolhendo um desvio.

Para configurar as variáveis com as quais o usuário irá interagir, clique em "+ Variável" e preencha o título da variável, o identificador, o tipo e indique se ela é editável, se é obrigatória ou se é apenas para ser apresentada. O título é a descrição da variável. Já o identificador é o "nome da variável", ele deve ser composto apenas de letras minúsculas, números e *under_score*, sem espaços ou caracteres especiais. Os tipos de variáveis disponíveis são:

1. **Texto**: uma string simples.
2. **Número**: um número fracionário.
3. **Data**: no formato DD/MM/YYYY.
4. **Booleano**: um valor tipo Sim/Não.
5. **Seleção**: uma lista de opções. As opções devem ser informadas no campo título, por exemplo: "Estado Civil (Solteiro;Casado)"
6. **Pessoa**: campo de seleção de pessoa.
7. **Lotação**: seleção de lotação.
8. **Documento**: seleção de documento.

Já os desvios são configuráveis clicando no botão "+ Desvio". Os campos a serem preenchidos são o título, uma seleção que inclui todas as outras tarefas ou "Nenhuma" ou "Fim" e a condição. 
O título será apresentado ao lado da seta que representa o desvio.
A tarefa representa a próxima tarefa que será executada quando o usuário escolher este desvio. A opção "Nenhuma" significa que ao escolher esse desvio o fluxo seguirá para a tarefa seguinte da lista ou para o "Fim" se a tarefa atual for a última.
Por fim, a condição é uma expressão calculada com o `MVel2 <http://mvel.documentnode.com/>`_ a partir das variáveis disponíveis no procedimento. Se ela for deixada em branco, o desvio sempre estará ativo, caso seja preenchida, só estará ativo se o resultado da expressão for o valor lógico "verdadeiro".

Crie quantas variáveis e desvios desejar. Se nenhum desvio for criado, o sistema automaticamente criará um botão para o usuário clicar chamado "Prosseguir". Ele funciona como um desvio para a próxima tarefa da lista. 

O caso mais simples de formulário requer apenas o título da tarefa e a definição do responsável, sem nenhuma variável ou desvio.

.. image:: cadastro-de-diagramas-formulario.png

Decisão
-------

E-mail
------

Executar
--------

Principal: Aguardar Assinatura
------------------------------

Principal: Tramitar
-------------------

Principal: Arquivar
-------------------

Principal: Incluir Documento
------------------------------

