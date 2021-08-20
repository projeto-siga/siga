package br.gov.jfrj.siga.ex.model.enm;

import br.gov.jfrj.siga.cp.model.enm.CpParamCfg;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeConfiguracao;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeConfiguracao;

//	Ainda ficou faltando: TIPO_CONFIG_VISUALIZAR_IMPRESSAO, TIPO_CONFIG_SIMULAR_USUARIO, TIPO_CONFIG_CORRIGIR_ERRO,  
public enum ExTipoDeConfiguracao implements ITipoDeConfiguracao {

	ACESSAR(6, "Acessar",
			"Esta é uma configuração bastante delicada. A função dela é permitir que determinadas pessoas tenham acesso a documentos sigilosos mesmo sem estarem na lista de permissões.\n"
					+ "\n"
					+ "O ideal é que essa configuração nunca seja utilizada. Melhor ainda seria apagar esse registro da tabela CP_TIPO_CONFIGURACAO para garantir que não será acidentalmente atribuída a ninguém.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, ExParamCfg.CLASSIFICACAO, ExParamCfg.TIPO_DOCUMENTO,
					ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO,
					ExParamCfg.NIVEL_DE_ACESSO, },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true),
	//
	ATENDER_PEDIDO_PUBLICACAO(22, "Atender Pedido de Publicação",
			"Indica se é possível utilizar a rotina de atendimento de pedidos indiretos de publicação no Diário Eletrônico. Esta configuração deve ser aplicada apenas aos gestores do diário.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO }, new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true),
	//
	AUTUAVEL(35, "Autuável",
			"Indica se determinado modelo ou espécie pode ser alvo te autação. Por exemplo, se queremos autuar um memorando, qual a lista de modelos que deve ser apresentada para o usuário escolher. Normalmente, os modelos e espécies configurados devem ser aqueles de Processos Administrativos.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, CpParamCfg.PERFIL, CpParamCfg.SERVICO,
					ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO,
					ExParamCfg.MODELO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true),
	//
	CANCELAR_MOVIMENTACAO(29, "Cancelar Movimentação",
			"Por padão as movimentações não podem ser canceladas. Utilizando essa configuração, é possível habilitar o cancelamento de movimentações de determinado tipo, dependendo ainda de regras de negócios específicas. Por exemplo, para que seja possível excluir \"Arquivos Auxiliares\" é necessário criar uma configuração habilitando o cancelamento de \"Anexação de Arquivo Auxiliar\".",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO },
			new Enum[] { ExParamCfg.TIPO_MOVIMENTACAO, CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true),
	//
	CANCELAR_VIA(16, "Cancelar Via", "Indica situações nas quais uma via pode ser cancelada.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO,
					ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	CRIAR(2, "Criar",
			"Normalmente todos os modelos de documentos podem ser criados por qualquer usuário, mas restrições podem ser impostas através dessa configuração. Modelos ou espécies específicas podem ser restritas de modo que apenas algumas pessoas ou lotações tenham permissão para criá-las.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO,
					ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO, ExParamCfg.CLASSIFICACAO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	CRIAR_COMO_NOVO(42, "Criar como Novo",
			"Normalmente todos os modelos de documentos podem ser criados por qualquer usuário, mas restrições podem ser impostas através dessa configuração. Modelos ou espécies específicas podem ser restritas de modo que apenas algumas pessoas ou lotações tenham permissão para criá-las quando clica no botão \"Criar Documento\". Veja também a configuração \"Despachável\".",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO,
					ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	CRIAR_VIA(17, "Criar Via",
			"Utilize essa configuração para desabilitar o botão \"Criar Via\" em alguns ou todos os modelos de expedientes.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO,
					ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	DEFINIR_PUBLICADORES(21, "Definir Publicadores",
			"Serve para dar permissão de utilizar rotina para redefinição de permissões de publicação no Diário Eletrônico. Esta configuração deve ser aplicada apenas aos gestores do diário.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO }, new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true),
	//
	DEFINICAO_AUTOMATICA_DE_PAPEL(40, "Definição Automática de Perfil",
			"Esta configuração permite que sejam definidos perfis automaticamente quando um documento é criado para determinado subscritor. Normalmente ela é utilizada para que seja atribuído o perfil de revisor para documentos que serão assinados por presidentes e diretores. Veja maiores explicações na configuração \"Pode Assinar Sem Solicitação\".\n"
					+ "\n"
					+ "Para realizar a configuração, informe a matrícula do diretor em \"Pessoa\" e a matrícula do revisor em \"Pessoa Objeto\". Além disso, escolha no \"Perfil\" a opção \"Revisor\".\n"
					+ "\n"
					+ "Esta configuração costuma ser utilizada juntamente com a configuração de \"Pode Assinar sem Solicitação\".",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.PERFIL, CpParamCfg.PESSOA_OBJETO, ExParamCfg.TIPO_DOCUMENTO,
					ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO, ExParamCfg.PAPEL },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	DESPACHAVEL(30, "Despachável",
			"Indica se determinada espécie ou modelo podem ser escolhidos quando o usuário pede para \"Incluir Documento\".",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO,
					ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	DESTINATARIO(31, "Destinatário",
			"Indica se determinada espécie ou modelo requerem a especificação de um destinatário, ou não. Para fazer com que o destinatário seja exigido, preencher o campo situação com o valor \"Obrigatório\".",
			new Enum[] { ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO,
					ExParamCfg.MODELO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.OPCIONAL, true),
	//
	DUPLICAR(9, "Duplicar",
			"Configura quem e quais modelos podem ser duplicados. Por padrão, qualquer documento poderá ser duplicado por qualquer pessoa.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO,
					ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	EDITAR(10, "Editar",
			"Desabilita a edição de determinados modelos de documentos por determinadas pessoas. Por padrão, a edição é sempre permitida, exceto quando viola regras de negócio.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, ExParamCfg.CLASSIFICACAO, ExParamCfg.TIPO_DOCUMENTO,
					ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO,
					ExParamCfg.NIVEL_DE_ACESSO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	EDITAR_DATA(36, "Editar Data",
			"Define se a data do documento será editável ou se ela será gerada automaticamente pelo sistema em função da data em que o documento foi finalizado ou assinado.\n"
					+ "\n"
					+ "O ideal é que ninguém tenha permissão de editar a data, considerando que não é boa prática criar documentos com datas futuras ou retroativas.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO,
					ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true),
	//
	EDITAR_DESCRICAO(37, "Editar Descrição",
			"Define se a descrição será editável ou se será gerada automaticamente pelo sistema.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO,
					ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	ELETRONICO(4, "Eletrônico",
			"Identifica os modelos documentos que são exclusivamente digitais, fisícos ou os que podem assumir as duas formas. Para forçar um modelo de documento a ser digital, utilize a situação \"Obrigatório\".",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO,
					ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.OBRIGATORIO,
					CpSituacaoDeConfiguracaoEnum.DEFAULT, CpSituacaoDeConfiguracaoEnum.NAO_PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.OBRIGATORIO, true),
	//
	EXCLUIR(11, "Excluir",
			"Indica se é possível excluir o documento. Além dessa configuração, as regras para a exclusão de documento incluem: não pode estar finalizado e lotação do usuário tem de ser a do cadastrante do documento.\n"
					+ "\n" + "Também é utilizado para verificar se pode ser realizada a exclusão de um cossignatário.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO,
					ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	EXCLUIR_ANEXO(12, "Excluir Anexo",
			"Indica se é permitido excluir uma movimentação de anexação. Além dessa configuração, as regras para a exclusão de anexo incluem: a anexação não pode estar cancelada, o anexo não pode estar assinado, se o documento for físico, não pode estar finalizado, se o documento for eletrônico, não pode estar assinado, a lotação do usuário tem de ser a lotação cadastrante da movimentação.\n"
					+ "\n"
					+ "Atualmente não é recomendado o uso de movimentações de anexação. Em substituição, sugerimos a utilização de documentos capturados.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO,
					ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	EXCLUIR_ANOTACAO(14, "Excluir Anotação", "Indica se é permitido excluir anotação.", new Enum[] { CpParamCfg.PESSOA,
			CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO, CpParamCfg.FUNCAO, CpParamCfg.ORGAO,
			ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	FINALIZAR(3, "Finalizar", "Indica se é permitido finalizar um documento.", new Enum[] { CpParamCfg.PESSOA,
			CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO, CpParamCfg.FUNCAO, CpParamCfg.ORGAO,
			ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	GERENCIAR_PUBLICACAO_BOLETIM(25, "Gerenciar Publicação Boletim",
			"Indica se determinado usuário pode, utilizar rotina para redefinição de permissões de publicação do Boletim Interno.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO }, new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true),
	//
	CRIAR_DOC_FILHO(24, "Incluir como Filho",
			"Quando é utilizada a ação \"Incluir Documento\", essa propriedade configura quais os modelos que devem ser apresentados na lista para que o usuário faça a seleção. Normalmente, serão permitidos apenas modelos de despachos, pareceres, capturados, etc. Não faz sentido, por exemplo, se seja permitido \"Incluir\" um Processo Administrativo num Expediente.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO,
					ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true),
	//
	INCLUIR_DOCUMENTO(41, "Incluir Documento",
			"Esta configuração indica se o usuário poderá executar a ação de \"Incluir Documento\".",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO,
					ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	JUNTADA_AUTOMATICA(43, "Juntada Automática",
			"Esta configuração indica se haverá a opção de \"Juntar\" automaticamente no momento da assinatura do documento.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO,
					ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE,CpSituacaoDeConfiguracaoEnum.DEFAULT },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	MOVIMENTAR(1, "Movimentar",
			"Muitas das operações realizadas pelo Siga-Doc se enquadram na categoria \"Movimentação\". As movimentações são ações que são registradas em relação à determinado documento. Trâmites, juntadas, arquivamentos, e definições de perfil são exemplos de movimentações.\n"
					+ "\n"
					+ "Esta configuração indica se é permitido a determinado usuário ou lotação realizar certo tipo de movimentação em algum modelo ou espécie.\n"
					+ "\n"
					+ "Ao introduzir configurações desse tipo, é sempre necessário informar o campo \"Tipo da Movimentação\".",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, CpParamCfg.PERFIL, CpParamCfg.SERVICO,
					CpParamCfg.PESSOA_OBJETO, CpParamCfg.LOTACAO_OBJETO, CpParamCfg.CARGO_OBJETO,
					CpParamCfg.FUNCAO_OBJETO, CpParamCfg.ORGAO_OBJETO, ExParamCfg.CLASSIFICACAO,
					ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO,
					ExParamCfg.MODELO, ExParamCfg.NIVEL_DE_ACESSO, ExParamCfg.PAPEL, ExParamCfg.VIA },
			new Enum[] { ExParamCfg.TIPO_MOVIMENTACAO, CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE, CpSituacaoDeConfiguracaoEnum.NAO_DEFAULT,
					CpSituacaoDeConfiguracaoEnum.DEFAULT },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	NIVEL_DE_ACESSO(5, "Nível de Acesso",
			"Utilize essa configuração para indicar o nível de acesso menos restritivo de modelos ou espécies.\n" + "\n"
					+ "Por exemplo, pode ser utilizado para indicar que determinado modelo pode ir até \"Público\".\n"
					+ "\n"
					+ "Se desejar, por exemplo, que determinado modelo seja apenas \"Público\" ou \"Limitado entre Órgãos\", configure o nível mínimo com \"Público\" e o máximo com \"Limitado entre Órgãos\".",
			new Enum[] { ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO,
					ExParamCfg.MODELO, ExParamCfg.NIVEL_DE_ACESSO, },
			null,
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.OPCIONAL, true),
	//
	NIVEL_ACESSO_MAXIMO(18, "Nível de Acesso Máximo",
			"Utilize essa configuração para indicar o nível de acesso mais restritivo de modelos ou espécies.\n" + "\n"
					+ "Por exemplo, pode ser utilizado para indicar que determinado modelo pode ir até \"Limitado entre Pessoas\".",
			new Enum[] { ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO,
					ExParamCfg.MODELO, ExParamCfg.NIVEL_DE_ACESSO, },
			null,
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.OPCIONAL, true),
	//
	NIVEL_ACESSO_MINIMO(19, "Nível de Acesso Mínimo",
			"Utilize essa configuração para indicar o nível de acesso menos restritivo de modelos ou espécies.\n" + "\n"
					+ "Por exemplo, pode ser utilizado para indicar que determinado modelo pode ir até \"Público\".\n"
					+ "\n"
					+ "Se desejar, por exemplo, que determinado modelo seja apenas \"Público\" ou \"Limitado entre Órgãos\", configure o nível mínimo com \"Público\" e o máximo com \"Limitado entre Órgãos\".",
			new Enum[] { ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO,
					ExParamCfg.MODELO, ExParamCfg.NIVEL_DE_ACESSO, },
			null,
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.OPCIONAL, true),
	//
	NOTIFICAR_POR_EMAIL(27, "Notificar Por E-mail",
			"Configuração utilizada para desativar o envio de notificações por e-mail que o Siga-Doc faz, por exemplo, quando um documento é tramitado para determinada pessoa.\n"
					+ "\n" + "Normalmente a notificação é desabilitada para determinado usuário ou lotação.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, ExParamCfg.CLASSIFICACAO, ExParamCfg.TIPO_DOCUMENTO,
					ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO,
					ExParamCfg.NIVEL_DE_ACESSO, ExParamCfg.VIA },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	PODE_ASSINAR_SEM_SOLICITACAO(39, "Pode Assinar sem Solicitação",
			"Esta configuração é utilizada para obter um efeito muito interessante no Siga-Doc. Algumas pessoas, geralmente de alto escalão, assinam grande quantidade de documentos por dia. Diremos que uma dessas pessoas é uma \"autoridade\". Devido a dificuldade de validar todos esses documentos, a autoridade pode precisar identificar pessoas de confiança para realizarem a revisão dos documentos antes que estes apareçam na lista para assinatura em lote.\n"
					+ "\n"
					+ "Ou seja, para que o Siga produza o comportamento esperado, é necessário fazer 3 configurações simultâneas: 1. Definir automaticamente revisores para documentos que serão assinados por determinada autoridade; 2. Indicar que a autoridade não verá na lista de assinatura em lote documentos cuja assinatura não tenha sido solicitada por um revisor; 3. Garantir que apenas os revisores de confiança da autoridade terão a permissão de \"Solicitar a Assinatura\".\n"
					+ "\n"
					+ "O primeiro passo é resolvido pela configuração \"Definição Automática de Perfil\", descrita acima. O segundo passo é realizado através desta configuração. E, o terceiro passo pode ser resolvido com uma configuração de \"Movimentar\", especificando quem pode \"Solicitar Assinatura\" para a autoridade. Nesse caso, a autoridade será informada no campo \"Pessoa\" e o revisor no campo \"Pessoa Objeto\".",
			new Enum[] { CpParamCfg.PESSOA, }, new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	RECEBER_DOC_NAO_ASSINADO(23, "Pode receber documento sem assinatura",
			"Normalmente é impedido o trâmite de documentos que não foram assinados. Utilizando essa configuração é possível indicar que determinada lotação pode receber documentos que ainda não estão assinados.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, ExParamCfg.CLASSIFICACAO, ExParamCfg.TIPO_DOCUMENTO,
					ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO,
					ExParamCfg.NIVEL_DE_ACESSO, ExParamCfg.VIA },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true),
	//
	REFAZER(8, "Refazer",
			"Indica se é permitido refazer um documento. Também têm de ser satisfeitas as seguintes condições:o documento tem de estar finalizado, o usuário tem de ser o subscritor ou o titular do documento ou ser da lotação cadastrante do documento, o documento não pode estar assinado, a não ser que seja dos tipos externo ou interno importado, que são naturalmente considerados assinados. Porém, se for documento de um desses tipos, não pode haver pdf anexado. O documento tem de possuir via não cancelada ou volume não cancelado.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, ExParamCfg.CLASSIFICACAO, ExParamCfg.TIPO_DOCUMENTO,
					ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO,
					ExParamCfg.NIVEL_DE_ACESSO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	REINICIAR_NUMERACAO_TODO_ANO(34, "Reiniciar Numeração Todo Ano",
			"Configuração utilizada para configurar modelos ou espécies que não têm sua contagem reiniciada todo ano.",
			new Enum[] { ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO,
					ExParamCfg.MODELO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	TRAMITE_AUTOMATICO(38, "Trâmite Automático",
			"Esta configuração indica se haverá a opção de \"Tramitar\" automaticamente no momento da assinatura do documento. Também configura o trâmite automático no caso da assinatura em lote.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, ExParamCfg.CLASSIFICACAO, ExParamCfg.TIPO_DOCUMENTO,
					ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO,
					ExParamCfg.NIVEL_DE_ACESSO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE, CpSituacaoDeConfiguracaoEnum.DEFAULT },
			CpSituacaoDeConfiguracaoEnum.DEFAULT, true),
	//
	UTILIZAR_EXTENSAO_CONVERSOR_HTML(33, "Utilizar Extensão de Conversor HTML",
			"Certa feita foi acrescentada ao Siga-Doc a possibilidade de utilizar um conversor chamado PD4ML para transformar HTML em PDF. Esta configuração servia para configurar quais os modelos que utilizariam o PD4ML. Seu uso não é mais recomendado pois esse componente está desatualizado.",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.ORGAO, ExParamCfg.TIPO_DOCUMENTO,
					ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true),
	//
	INCLUIR_EM_AVULSO(45, "Incluir em Avulso?", "",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, CpParamCfg.PERFIL, CpParamCfg.SERVICO,
					CpParamCfg.PESSOA_OBJETO, CpParamCfg.LOTACAO_OBJETO, CpParamCfg.CARGO_OBJETO,
					CpParamCfg.FUNCAO_OBJETO, CpParamCfg.ORGAO_OBJETO, ExParamCfg.CLASSIFICACAO,
					ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO,
					ExParamCfg.MODELO, ExParamCfg.NIVEL_DE_ACESSO, ExParamCfg.PAPEL, ExParamCfg.VIA },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	COSIGNATARIO_ASSINAR_ANTES_SUBSCRITOR(46, "Cossignatário Assinar Antes do Subscritor?", "",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, CpParamCfg.PERFIL, CpParamCfg.SERVICO,
					CpParamCfg.PESSOA_OBJETO, CpParamCfg.LOTACAO_OBJETO, CpParamCfg.CARGO_OBJETO,
					CpParamCfg.FUNCAO_OBJETO, CpParamCfg.ORGAO_OBJETO, ExParamCfg.CLASSIFICACAO,
					ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO,
					ExParamCfg.MODELO, ExParamCfg.NIVEL_DE_ACESSO, ExParamCfg.PAPEL, ExParamCfg.VIA },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true),
	//
	FINALIZAR_AUTOMATICAMENTE_CAPTURADOS(47, "Finalizar Automaticamente Capturados?", "",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, CpParamCfg.PERFIL, CpParamCfg.SERVICO,
					CpParamCfg.PESSOA_OBJETO, CpParamCfg.LOTACAO_OBJETO, CpParamCfg.CARGO_OBJETO,
					CpParamCfg.FUNCAO_OBJETO, CpParamCfg.ORGAO_OBJETO, ExParamCfg.CLASSIFICACAO,
					ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO,
					ExParamCfg.MODELO, ExParamCfg.NIVEL_DE_ACESSO, ExParamCfg.PAPEL, ExParamCfg.VIA },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true),
	//
	TROCAR_PDF_CAPTURADOS(48, "Trocar PDFs Capturados?", "",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, CpParamCfg.PERFIL, CpParamCfg.SERVICO,
					CpParamCfg.PESSOA_OBJETO, CpParamCfg.LOTACAO_OBJETO, CpParamCfg.CARGO_OBJETO,
					CpParamCfg.FUNCAO_OBJETO, CpParamCfg.ORGAO_OBJETO, ExParamCfg.CLASSIFICACAO,
					ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO,
					ExParamCfg.MODELO, ExParamCfg.NIVEL_DE_ACESSO, ExParamCfg.PAPEL, ExParamCfg.VIA },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	TMP_PARA_LOTACAO(49, "Mostrar temporários para Lotação", "",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, CpParamCfg.PERFIL, CpParamCfg.SERVICO,
					CpParamCfg.PESSOA_OBJETO, CpParamCfg.LOTACAO_OBJETO, CpParamCfg.CARGO_OBJETO,
					CpParamCfg.FUNCAO_OBJETO, CpParamCfg.ORGAO_OBJETO, ExParamCfg.CLASSIFICACAO,
					ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO,
					ExParamCfg.MODELO, ExParamCfg.NIVEL_DE_ACESSO, ExParamCfg.PAPEL, ExParamCfg.VIA },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	RESTRINGIR_ACESSO_APOS_RECEBER(50, "Restringir Acesso Após Receber?", "",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, CpParamCfg.PERFIL, CpParamCfg.SERVICO,
					CpParamCfg.PESSOA_OBJETO, CpParamCfg.LOTACAO_OBJETO, CpParamCfg.CARGO_OBJETO,
					CpParamCfg.FUNCAO_OBJETO, CpParamCfg.ORGAO_OBJETO, ExParamCfg.CLASSIFICACAO,
					ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO,
					ExParamCfg.MODELO, ExParamCfg.NIVEL_DE_ACESSO, ExParamCfg.PAPEL, ExParamCfg.VIA },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true),
	//
	AUTORIZAR_MOVIMENTACAO_POR_WS(51, "Autorizar Movimentação por WS?", "",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, CpParamCfg.PERFIL, CpParamCfg.SERVICO,
					CpParamCfg.PESSOA_OBJETO, CpParamCfg.LOTACAO_OBJETO, CpParamCfg.CARGO_OBJETO,
					CpParamCfg.FUNCAO_OBJETO, CpParamCfg.ORGAO_OBJETO, ExParamCfg.CLASSIFICACAO,
					ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO,
					ExParamCfg.MODELO, ExParamCfg.NIVEL_DE_ACESSO, ExParamCfg.PAPEL, ExParamCfg.VIA },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true),
	//
	DELEGAR_VISUALIZACAO(44, "Delegar Visualização?", "",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, CpParamCfg.PERFIL, CpParamCfg.SERVICO,
					CpParamCfg.PESSOA_OBJETO, CpParamCfg.LOTACAO_OBJETO, CpParamCfg.CARGO_OBJETO,
					CpParamCfg.FUNCAO_OBJETO, CpParamCfg.ORGAO_OBJETO, ExParamCfg.CLASSIFICACAO,
					ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO,
					ExParamCfg.MODELO, ExParamCfg.NIVEL_DE_ACESSO, ExParamCfg.PAPEL, ExParamCfg.VIA },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true),
	//
	TRAMITAR_SEM_CAPTURADO(52, "Tramitar sem Capturado?", "",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, CpParamCfg.PERFIL, CpParamCfg.SERVICO,
					CpParamCfg.PESSOA_OBJETO, CpParamCfg.LOTACAO_OBJETO, CpParamCfg.CARGO_OBJETO,
					CpParamCfg.FUNCAO_OBJETO, CpParamCfg.ORGAO_OBJETO, ExParamCfg.CLASSIFICACAO,
					ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO,
					ExParamCfg.MODELO, ExParamCfg.NIVEL_DE_ACESSO, ExParamCfg.PAPEL, ExParamCfg.VIA },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true),
	//
	CRIAR_NOVO_EXTERNO(53, "Criar Novo Externo", "",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, CpParamCfg.PERFIL, CpParamCfg.SERVICO,
					CpParamCfg.PESSOA_OBJETO, CpParamCfg.LOTACAO_OBJETO, CpParamCfg.CARGO_OBJETO,
					CpParamCfg.FUNCAO_OBJETO, CpParamCfg.ORGAO_OBJETO, ExParamCfg.CLASSIFICACAO,
					ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO,
					ExParamCfg.MODELO, ExParamCfg.NIVEL_DE_ACESSO, ExParamCfg.PAPEL, ExParamCfg.VIA },
			new Enum[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true),
	//

	TRAMITAR_PARA_LOTACAO_SEM_USUARIOS_ATIVOS(54, "Tramitar para Lotação sem Usuários Ativos?", "",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.TIPO_DE_LOTACAO, CpParamCfg.CARGO,
					CpParamCfg.FUNCAO, CpParamCfg.ORGAO, CpParamCfg.PERFIL, CpParamCfg.SERVICO,
					CpParamCfg.PESSOA_OBJETO, CpParamCfg.LOTACAO_OBJETO, CpParamCfg.CARGO_OBJETO,
					CpParamCfg.FUNCAO_OBJETO, CpParamCfg.ORGAO_OBJETO, ExParamCfg.CLASSIFICACAO,
					ExParamCfg.TIPO_DOCUMENTO, ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO,
					ExParamCfg.MODELO, ExParamCfg.NIVEL_DE_ACESSO, ExParamCfg.PAPEL, ExParamCfg.VIA },
			new Enum[] { CpParamCfg.SITUACAO }, new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true), 
	
	EXIBIR_QUEM_TEM_ACESSO_DOCUMENTO_LIMITADO(55,"Exibir quem tem acesso a documentos limitados?", "Tem como objetivo não exibir as informações das pessoas e lotações que tem acesso a um documento limitado",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.ORGAO, ExParamCfg.TIPO_DOCUMENTO,
					ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO },
			new Enum[] { CpParamCfg.SITUACAO }, new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true), 
	//
	UTILIZAR_EXTENSAO_EDITOR(32, "Utilizar Extensão de Editor", "",
			new Enum[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.ORGAO, ExParamCfg.TIPO_DOCUMENTO,
					ExParamCfg.TIPO_FORMA_DOCUMENTO, ExParamCfg.FORMA_DOCUMENTO, ExParamCfg.MODELO },
			new Enum[] { CpParamCfg.SITUACAO }, new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true);

	private final int id;
	private final String descr;
	private final String explicacao;
	private final Enum[] params;
	private final Enum[] obrigatorios;
	private final CpSituacaoDeConfiguracaoEnum[] situacoes;
	private final CpSituacaoDeConfiguracaoEnum situacaoDefault;
	private final boolean editavel;

	ExTipoDeConfiguracao(int id, String descr, String explicacao, Enum[] params, Enum[] obrigatorios,
			CpSituacaoDeConfiguracaoEnum[] situacoes, CpSituacaoDeConfiguracaoEnum situacaoDefault, boolean editavel) {
		this.id = id;
		this.descr = descr;
		this.explicacao = explicacao;
		this.params = params;
		this.obrigatorios = obrigatorios;
		this.situacoes = situacoes;
		this.situacaoDefault = situacaoDefault;
		this.editavel = editavel;
	}

	public int getId() {
		return id;
	}

	public String getDescr() {
		return this.descr;
	}

	public String getExplicacao() {
		return this.explicacao;
	}

	public static ITipoDeConfiguracao getById(Integer id) {
		if (id == null)
			return null;
		return CpTipoDeConfiguracao.getById(id);
	}

	public CpSituacaoDeConfiguracaoEnum[] getSituacoes() {
		return situacoes;
	}

	public Enum[] getObrigatorios() {
		return obrigatorios;
	}

	@Override
	public Enum[] getParams() {
		return params;
	}

	@Override
	public CpSituacaoDeConfiguracaoEnum getSituacaoDefault() {
		return situacaoDefault;
	}

	@Override
	public boolean isEditavel() {
		return editavel;
	}
}
