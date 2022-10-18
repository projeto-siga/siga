package br.gov.jfrj.siga.ex.model.enm;

public enum ConversaoDoeEnum {
	ADMITE(1, "admite", "Admitindo"),
	APLICA_PENALIDADE(2, "aplica penalidade", "Aplicando"),
	ARBITRA_GRATIFICACAO(3, "arbitra gratificação", "Arbritando"),
	AUTORIZA_OCUPACAO_RESIDENCIA(4, "autoriza a ocupação de residência", "Autorizando"),
	AUTORIZA(5, "autoriza", "Autorizando"),
	CESSA(6, "cessa", "Cessando"),
	CLASSIFICA_EM(7, "classifica em", "Classificando"),
	CONCEDE_LICENCA_PREMIO(9, "concede licença-prêmio", "Concedendo"),
	CONCEDE_LICENCA(8, "concede licença (com ou sem vencimentos)", "Concedendo"),
	CONCEDE(10, "concede", "Concedendo"),
	CONVOCA_PRESTACAO_SERVICOS_EXTRAORDINARIOS(11, "convoca para a prestação de serviços extraordinários", "Convocando"),
	DESIGNA_POSTO_TRABALHO(13, "designar para posto de trabalho", "Designando"),
	DESIGNA(12, "designa", "Designando"),
	EXONERA(14, "exonera", "Exonerando"),
	FIXA_SEDE_EXERCICIO(15, "fixa sede de exercício","Fixando"),
	NOMEA(16, "nomea","Nomeando"),
	REMOVE(17, "remove","Removendo"),
	TRANSFERIR(18, "transferir","Transferindo");

	private ConversaoDoeEnum(int id, String imperativo, String gerundio) {
		this.id = id;
		this.imperativo = imperativo;
		this.gerundio = gerundio;
	}
	
	public final int id;
	private final String imperativo;
	private final String gerundio;
	
	public int getId() {
		return id;
	}
	public String getImperativo() {
		return imperativo;
	}
	public String getGerundio() {
		return gerundio;
	}
}
