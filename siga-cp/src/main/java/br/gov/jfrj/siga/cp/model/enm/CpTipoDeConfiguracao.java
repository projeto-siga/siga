package br.gov.jfrj.siga.cp.model.enm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum CpTipoDeConfiguracao implements ITipoDeConfiguracao {

	CADASTRAR_QUALQUER_SUBST(20, "Cadastrar Qualquer Subst",
			"Utilizada para configurar quais são as pessoas que tem permissão de cadastrar qualquer substituição. Nomalmente, a regra de negócio diz que uma pessoa só pode cadastrar substitutos para si mesma ou para sua lotação, ou outras regras envolvendo hierarquias. No entanto, uma pessoa indicada para \"Cadastrar Qualquer Substituição\" poderá cadastrar em nome de outras. Esta configuração normalmente é atribuída aos administradores do sistema ou a equipe de suporte.",
			new CpParamCfg[] { CpParamCfg.PESSOA, CpParamCfg.LOTACAO }, new CpParamCfg[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true),

	//
	UTILIZAR_SERVICO(200, "Utilizar Serviço",
			"Selecione órgão, lotação, pessoa, cargo ou função comissionada que tem permissão para utilizar determinado serviço.",
			new CpParamCfg[] {
					CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.CARGO, CpParamCfg.FUNCAO, CpParamCfg.ORGAO },
			new CpParamCfg[] { CpParamCfg.SERVICO, CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE, CpSituacaoDeConfiguracaoEnum.IGNORAR_CONFIGURACAO_ANTERIOR },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, false),
	//
	HABILITAR_SERVICO(201, "Habilitar Serviço",
			"Selecione órgão, lotação, pessoa, cargo ou função comissionada que tem permissão para habilitar determinado serviço.",
			new CpParamCfg[] {
					CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.CARGO, CpParamCfg.FUNCAO, CpParamCfg.ORGAO },
			new CpParamCfg[] { CpParamCfg.SERVICO, CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE, CpSituacaoDeConfiguracaoEnum.IGNORAR_CONFIGURACAO_ANTERIOR },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, false),
	//
	HABILITAR_SERVICO_DE_DIRETORIO(202, "Habilitar Serviço de Diretório",
			"Selecione órgão, lotação, pessoa, cargo ou função comissionada que tem permissão para habilitar determinado serviço de diretório.",
			new CpParamCfg[] {
					CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.CARGO, CpParamCfg.FUNCAO, CpParamCfg.ORGAO },
			new CpParamCfg[] { CpParamCfg.SERVICO, CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE, CpSituacaoDeConfiguracaoEnum.IGNORAR_CONFIGURACAO_ANTERIOR },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, false),
	//
	PERTENCER(203, "Pertencer à Grupo",
			"Selecione órgão, lotação, pessoa, cargo ou função comissionada que pertencem a determinado grupo.",
			new CpParamCfg[] {
					CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.CARGO, CpParamCfg.FUNCAO, CpParamCfg.ORGAO },
			new CpParamCfg[] { CpParamCfg.SERVICO, CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE, CpSituacaoDeConfiguracaoEnum.IGNORAR_CONFIGURACAO_ANTERIOR },
			CpSituacaoDeConfiguracaoEnum.PODE, false),
	//
	FAZER_LOGIN(204, "Fazer Login",
			"Selecione órgão, lotação, pessoa, cargo ou função comissionada que tem permissão para fazer login.",
			new CpParamCfg[] {
					CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.CARGO, CpParamCfg.FUNCAO, CpParamCfg.ORGAO },
			new CpParamCfg[] { CpParamCfg.SERVICO, CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE, CpSituacaoDeConfiguracaoEnum.IGNORAR_CONFIGURACAO_ANTERIOR },
			CpSituacaoDeConfiguracaoEnum.PODE, false),
	//
	UTILIZAR_SERVICO_OUTRA_LOTACAO(205, "Utilizar Serviço de Outra Lotação",
			"Selecione órgão, lotação, pessoa, cargo ou função comissionada que tem permissão para utilizar determinado serviço de outra lotação.",
			new CpParamCfg[] {
					CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.CARGO, CpParamCfg.FUNCAO, CpParamCfg.ORGAO },
			new CpParamCfg[] { CpParamCfg.SERVICO, CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE, CpSituacaoDeConfiguracaoEnum.IGNORAR_CONFIGURACAO_ANTERIOR },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, false),
	//
	GERENCIAR_GRUPO(206, "Gerenciar Grupo",
			"Selecione órgão, lotação, pessoa, cargo ou função comissionada que tem permissão para gerenciar determinado grupo.",
			new CpParamCfg[] {
					CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.CARGO, CpParamCfg.FUNCAO, CpParamCfg.ORGAO },
			new CpParamCfg[] { CpParamCfg.SERVICO, CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE, CpSituacaoDeConfiguracaoEnum.IGNORAR_CONFIGURACAO_ANTERIOR },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, false),

	SEGUNDO_FATOR_PIN(208, "PIN como Segundo Fator de Autenticação",
			"Utilizada para ativar e configurar o uso do Personal Identification Number (PIN) no SIGA como segundo fator de autenticação e seu comportamento com uso na Assinatura com Senha e outras funcionalidades que requerem uma segunda validação de autenticação.\n"
					+ "PODE: Habilita uso do PIN como Segundo Fator e combinado com o Assinar com Senha libera o uso de autenticar com PIN.\n"
					+ "NÃO PODE: Desativa uso do PIN como Segundo Fator.\n"
					+ "DEFAULT: Combinado com outras funcionalidades dita qual é o provedor de segundo fator de autenticação padrão.\n"
					+ "OBRIGATORIO: Combinado com outras funcionalidades obriga a o segundo fator de autenticação ocorrer somente via PIN e torna o DEFAULT automaticamente.\n",
			new CpParamCfg[] { CpParamCfg.ORGAO, CpParamCfg.PESSOA, CpParamCfg.LOTACAO },
			new CpParamCfg[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE, CpSituacaoDeConfiguracaoEnum.DEFAULT,
					CpSituacaoDeConfiguracaoEnum.OBRIGATORIO },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true), 	
	
	EXIBIR_REGRA_DE_NEGOCIO_EM_BOTOES(210, "Exibir Regra de Negócio em botões",
			"Utilizada para ativar exibição dos motivos pelos quais determinada ação está habilitada ou desabilitada.\n"
					+ "PODE: Exibe.\n"
					+ "NÃO PODE: Não exibe.",
			new CpParamCfg[] { CpParamCfg.ORGAO, CpParamCfg.PESSOA, CpParamCfg.LOTACAO },
			new CpParamCfg[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true), 

	EXIBIR_REGRA_DE_NEGOCIO_EM_MENSAGENS(211, "Exibir Regra de Negócio em mensagens",
			"Utilizada para ativar exibição dos motivos pelos quais determinada ação estava desabilitada quando foi produzido um erro.\n"
					+ "PODE: Exibe.\n"
					+ "NÃO PODE: Não exibe.",
			new CpParamCfg[] { CpParamCfg.ORGAO, CpParamCfg.PESSOA, CpParamCfg.LOTACAO },
			new CpParamCfg[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, true), 

	UTILIZAR_PESQUISA_AVANCADA_VIA_XJUS(212, "Utilizar X-JUS na Pesquisa Avançada",
			"Utilizada para ativar pesquisa avançada via X-JUS.\n"
					+ "PODE: Ativa.\n"
					+ "NÃO PODE: Não ativa.",
			new CpParamCfg[] { CpParamCfg.ORGAO, CpParamCfg.PESSOA, CpParamCfg.LOTACAO },
			new CpParamCfg[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true),

	UTILIZAR_PESQUISA_GENERICA_VIA_XJUS(213, "Utilizar X-JUS na Pesquisa Genérica",
			"Utilizada para ativar pesquisa genérica via X-JUS.\n"
					+ "PODE: Ativa.\n"
					+ "NÃO PODE: Não ativa.",
			new CpParamCfg[] { CpParamCfg.ORGAO, CpParamCfg.PESSOA, CpParamCfg.LOTACAO },
			new CpParamCfg[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true),

	EXIBIR_MESA_VIRTUAL(214, "Exibir Mesa Virtual na Tela Inicial do Usuário",
			"Utilizada para ativar exibição da Mesa Virtual no luga do Quadro Quantitativo.\n"
					+ "PODE: Exibe.\n"
					+ "NÃO PODE: Não exibe.",
			new CpParamCfg[] { CpParamCfg.ORGAO, CpParamCfg.PESSOA, CpParamCfg.LOTACAO },
			new CpParamCfg[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true), 
	
	UTILIZAR_COMPLEXO(400, "Utilizar Complexo Padrão",
			"Selecione órgão, lotação, pessoa, cargo ou função comissionada que tem permissão para utilizar determinado complexo como padrão.",
			new CpParamCfg[] {
					CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.CARGO, CpParamCfg.FUNCAO, CpParamCfg.ORGAO },
			new CpParamCfg[] { CpParamCfg.SERVICO, CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE, CpSituacaoDeConfiguracaoEnum.IGNORAR_CONFIGURACAO_ANTERIOR },
			CpSituacaoDeConfiguracaoEnum.NAO_PODE, false),
	
	EXIBIR_EM_CAMPO_DE_BUSCA(401, "Exibir em Campo de Pesquisa", "Esta configuração tem o objetivo de exibir [órgão(s), lotação(s), pessoa(s). O objeto preenchido no cadastro de configuração "
			+ "não aparecerá na lista da combo. O padrão é pode. Implementado somente para limitar órgão na combo de pesquisa avançada.",
			new CpParamCfg[] {CpParamCfg.PESSOA, CpParamCfg.LOTACAO, CpParamCfg.CARGO,CpParamCfg.FUNCAO, CpParamCfg.ORGAO,
					CpParamCfg.PESSOA_OBJETO, CpParamCfg.LOTACAO_OBJETO, CpParamCfg.CARGO_OBJETO,
					CpParamCfg.FUNCAO_OBJETO, CpParamCfg.ORGAO_OBJETO},
			new CpParamCfg[] { CpParamCfg.SITUACAO },
			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
					CpSituacaoDeConfiguracaoEnum.NAO_PODE },
			CpSituacaoDeConfiguracaoEnum.PODE, false),
	

	// 
//	AUTORIZAR_MOVIMENTACAO_POR_WS(?, "Autorizar Movimentação por WS",
//			"Utilizada para ativar e configurar o uso do Personal Identification Number (PIN) no SIGA como segundo fator de autenticação e seu comportamento com uso na Assinatura com Senha e outras funcionalidades que requerem uma segunda validação de autenticação.\n"
//					+ "PODE: Habilita uso do PIN como Segundo Fator e combinado com o Assinar com Senha libera o uso de autenticar com PIN.\n"
//					+ "NÃO PODE: Desativa uso do PIN como Segundo Fator.\n"
//					+ "DEFAULT: Combinado com outras funcionalidades dita qual é o provedor de segundo fator de autenticação padrão.\n"
//					+ "OBRIGATORIO: Combinado com outras funcionalidades obriga a o segundo fator de autenticação ocorrer somente via PIN e torna o DEFAULT automaticamente.\n",
//			new CpParamCfg[] { CpParamCfg.ORGAO, CpParamCfg.PESSOA, CpParamCfg.LOTACAO },
//			new CpParamCfg[] { CpParamCfg.SITUACAO },
//			new CpSituacaoDeConfiguracaoEnum[] { CpSituacaoDeConfiguracaoEnum.PODE,
//					CpSituacaoDeConfiguracaoEnum.NAO_PODE, CpSituacaoDeConfiguracaoEnum.DEFAULT,
//					CpSituacaoDeConfiguracaoEnum.OBRIGATORIO },
//			CpSituacaoDeConfiguracaoEnum.NAO_PODE, true)
	;


	public static final long TIPO_CONFIG_SR_DESIGNACAO = 300;

	public static final long TIPO_CONFIG_SR_ASSOCIACAO_TIPO_ATRIBUTO = 301;

	public static final long TIPO_CONFIG_SR_PERMISSAO_USO_LISTA = 302;

	public static final long TIPO_CONFIG_SR_DEFINICAO_INCLUSAO_AUTOMATICA = 303;

	public static final long TIPO_CONFIG_SR_ABRANGENCIA_ACORDO = 304;

	public static final long TIPO_CONFIG_SR_ASSOCIACAO_PESQUISA = 305;

	public static final long TIPO_CONFIG_SR_ESCALONAMENTO_SOL_FILHA = 306;
	

	
	

	private static Map<Integer, ITipoDeConfiguracao> map = new HashMap<>();

	private final int id;
	private final String descr;
	private final String explicacao;
	private final CpParamCfg[] params;
	private final CpParamCfg[] obrigatorios;
	private final CpSituacaoDeConfiguracaoEnum[] situacoes;
	private final CpSituacaoDeConfiguracaoEnum situacaoDefault;
	private final boolean editavel;

	CpTipoDeConfiguracao(int id, String descr, String explicacao, CpParamCfg[] params, CpParamCfg[] obrigatorios,
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

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getDescr() {
		return this.descr;
	}

	@Override
	public String getExplicacao() {
		return this.explicacao;
	}

	public static ITipoDeConfiguracao getById(Integer id) {
		if (id == null)
			return null;
		return map.get(id);
	}

	@Override
	public CpSituacaoDeConfiguracaoEnum[] getSituacoes() {
		return situacoes;
	}

	@Override
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

	public static void mapear(ITipoDeConfiguracao t) {
		map.put(t.getId(), t);
	}

	public static void mapear(ITipoDeConfiguracao[] l) {
		for (ITipoDeConfiguracao t : l)
			map.put(t.getId(), t);
	}

	public static Set<Integer> getIdsMapeadas() {
		return map.keySet();
	}

	public static Collection<ITipoDeConfiguracao> getValoresMapeados() {
		return map.values();
	}
}
