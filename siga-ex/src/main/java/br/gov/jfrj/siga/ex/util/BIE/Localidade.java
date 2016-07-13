package br.gov.jfrj.siga.ex.util.BIE;


public enum Localidade {

	RIO_DE_JANEIRO("Rio de Janeiro"),
	ANGRA_DOS_REIS("Angra dos Reis"),
	BARRA_DO_PIRAI("Barra do Piraí"),
	CAMPO_GRANDE("Campo Grande"),
	CAMPOS("Campos"),
	DUQUE_DE_CAXIAS("Duque de Caxias"),
	ITABORAI("Itaboraí"),
	ITAPERUNA("Itaperuna"),
	MACAE("Macaé"),
	MAGE("Magé"),
	NITEROI("Niterói"),
	NOVA_FRIBURGO("Nova Friburgo"),
	NOVA_IGUACU("Nova Iguaçu"),
	PETROPOLIS("Petrópolis"),
	RESENDE("Resende"),
	SAO_GONCALO("São Gonçalo"),
	SAO_JOAO_DE_MERITI("São João de Meriti"),
	SAO_PEDRO_DA_ALDEIA("São Pedro da Aldeia"),
	TERESOPOLIS("Teresópolis"),
	TRES_RIOS("Três Rios"),
	VOLTA_REDONDA("Volta Redonda"),
	OUTRA("");

	private String descricao;

	public String getDescricao() {
		return descricao;
	}

	Localidade(String descricao) {
		this.descricao = descricao;
	}

	public static Localidade porDescricao(String descr) {
		for (Localidade l : Localidade.values())
			if (l.getDescricao().equals(descr))
				return l;
		return Localidade.OUTRA;
	}
}
