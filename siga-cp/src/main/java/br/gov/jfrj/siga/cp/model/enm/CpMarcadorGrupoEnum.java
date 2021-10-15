package br.gov.jfrj.siga.cp.model.enm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.gov.jfrj.siga.cp.CpTipoMarcadorEnum;
import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum CpMarcadorGrupoEnum implements IEnumWithId {
	PRONTO_PARA_ASSINAR(1, "Pronto para Assinar", "fas fa-inbox", true, true, false),
	//
	ALERTA(2, "Alertas", "fas fa-hourglass-end", true, false, false),
	//
	A_REVISAR(3, "Pendente de Revisão", "fas fa-glasses", true, true, false),
	//
	A_ASSINAR(4, "Pendente de Assinatura", "fas fa-key", true, false, false),
	//
	CAIXA_DE_ENTRADA(5, "Caixa de Entrada", "fas fa-inbox", true, false, false),
	//
	EM_ELABORACAO(6, "Em Elaboração", "fas fa-lightbulb", true, false, false),
	//
	AGUARDANDO_ANDAMENTO(7, "Aguardando Andamento", "fas fa-clock", true, true, false),
	//
	CAIXA_DE_SAIDA(8, "Caixa de Saída", "fas fa-inbox", false, true, true),
	//
	ACOMPANHANDO(9, "Acompanhando", "fas fa-tags", true, true, false),
	//
	MONITORANDO(10, "Monitorando", "fas fa-hourglass-half", true, true, false),
	//
	AGUARDANDO_ACAO_DE_TEMPORALIDADE(11, "Aguardando Ação de Temporalidade", "fas fa-hourglass-half", true, true, true),
	//
	OUTROS(12, "Outros", "fas fa-inbox", true, true, false),
	//
	QUALQUER(13, "Qualquer", "fas fa-inbox", false, true, true),
	//
	NENHUM(14, "Nenhum", "fas fa-inbox", false, true, true);

	private final Integer id;
	private final String nome;
	private final String icone;
	private final boolean visible;
	private final boolean collapsed;
	private final boolean hide;

	private CpMarcadorGrupoEnum(Integer id, String nome, String icone, boolean visible, boolean collapsed,
			boolean hide) {
		this.id = id;
		this.nome = nome;
		this.icone = icone;
		this.visible = visible;
		this.collapsed = collapsed;
		this.hide = hide;
	}

	public String getNome() {
		return this.nome;
	}

	public Integer getId() {
		return this.id;
	}

	public static CpMarcadorGrupoEnum getByNome(String nome) {
		for (CpMarcadorGrupoEnum i : CpMarcadorGrupoEnum.values()) {
			if (i.nome.equals(nome))
				return i;
		}
		return null;
	}

	public static CpMarcadorGrupoEnum getById(Integer id) {
		for (CpMarcadorGrupoEnum i : CpMarcadorGrupoEnum.values()) {
			if (id.equals(i.id))
				return i;
		}
		return null;
	}

	public static List<String> getIdList() {
		List<String> idList = new ArrayList<String>();
		for (CpMarcadorGrupoEnum i : CpMarcadorGrupoEnum.values()) {
			idList.add((i.id).toString());
		}
		return idList;
	}

	public String getIcone() {
		return icone;
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean isCollapsed() {
		return collapsed;
	}

	public boolean isHide() {
		return hide;
	}

	public static List<CpMarcadorGrupoEnum> disponiveis() {
		return Arrays.asList(values()).stream().filter(f -> f != QUALQUER && f != NENHUM).collect(Collectors.toList());
	}
}