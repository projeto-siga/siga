package br.gov.jfrj.siga.cp.model.enm;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.cp.CpTipoMarcadorEnum;
import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum CpMarcadorFinalidadeEnum implements IEnumWithId {
	SISTEMA(1, "Sistema", "Marcador de Sistema", CpTipoMarcadorEnum.TIPO_MARCADOR_SISTEMA, null, null, null, null, null,
			null),
	//
	GERAL_ATENDENTE(2, "Geral - Tarja", "Marcador Geral", CpTipoMarcadorEnum.TIPO_MARCADOR_GERAL,
			CpMarcadorTipoAplicacaoEnum.VIA_ESPECIFICA_OU_ULTIMO_VOLUME, CpMarcadorTipoDataEnum.DESATIVADA,
			CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoExibicaoEnum.IMEDIATA,
			CpMarcadorTipoInteressadoEnum.ATENDENTE, CpMarcadorTipoTextoEnum.DESATIVADA),
	//
	GERAL_ATENDENTE_PRAZO(3, "Geral - Alerta", "Exibir o marcador a partir de uma data especificada",
			CpTipoMarcadorEnum.TIPO_MARCADOR_GERAL, CpMarcadorTipoAplicacaoEnum.VIA_ESPECIFICA_OU_ULTIMO_VOLUME,
			CpMarcadorTipoDataEnum.OBRIGATORIA, CpMarcadorTipoDataEnum.OPCIONAL, CpMarcadorTipoExibicaoEnum.MENOR_DATA,
			CpMarcadorTipoInteressadoEnum.ATENDENTE, CpMarcadorTipoTextoEnum.DESATIVADA),
	//
	GERAL_ATENDENTE_PRAZOS(4, "Geral - Alerta de Prazo", "Alertar quanto a proximidade de um prazo final",
			CpTipoMarcadorEnum.TIPO_MARCADOR_GERAL, CpMarcadorTipoAplicacaoEnum.VIA_ESPECIFICA_OU_ULTIMO_VOLUME,
			CpMarcadorTipoDataEnum.OBRIGATORIA, CpMarcadorTipoDataEnum.OBRIGATORIA,
			CpMarcadorTipoExibicaoEnum.MENOR_DATA, CpMarcadorTipoInteressadoEnum.ATENDENTE,
			CpMarcadorTipoTextoEnum.DESATIVADA),
	//
	GERAL_DIRECIONADO_PRAZO(4, "Geral - Alerta Direcionado", "Exibir o marcador para unidades ou pessoas",
			CpTipoMarcadorEnum.TIPO_MARCADOR_GERAL, CpMarcadorTipoAplicacaoEnum.VIA_ESPECIFICA_OU_ULTIMO_VOLUME,
			CpMarcadorTipoDataEnum.OBRIGATORIA, CpMarcadorTipoDataEnum.OPCIONAL, CpMarcadorTipoExibicaoEnum.MENOR_DATA,
			CpMarcadorTipoInteressadoEnum.LOTACAO_OU_PESSOA, CpMarcadorTipoTextoEnum.DESATIVADA),
	//
	LOTACAO_PASTA(100, "Interno - Pasta", "Organizar o acervo em andamento na minha unidade",
			CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO, CpMarcadorTipoAplicacaoEnum.VIA_ESPECIFICA_OU_ULTIMO_VOLUME,
			CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoExibicaoEnum.IMEDIATA,
			CpMarcadorTipoInteressadoEnum.ATENDENTE, CpMarcadorTipoTextoEnum.DESATIVADA),
	//
	LOTACAO(101, "Interno - Etiqueta", "Marcar expediente/processo para todos",
			CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO, CpMarcadorTipoAplicacaoEnum.TODAS_AS_VIAS_OU_ULTIMO_VOLUME,
			CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoExibicaoEnum.IMEDIATA,
			CpMarcadorTipoInteressadoEnum.ATENDENTE, CpMarcadorTipoTextoEnum.DESATIVADA),
	//
	LOTACAO_PRAZO(102, "Interno - Etiqueta Agendada", "Marcar expediente/processo a partir de uma data",
			CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO, CpMarcadorTipoAplicacaoEnum.TODAS_AS_VIAS_OU_ULTIMO_VOLUME,
			CpMarcadorTipoDataEnum.OBRIGATORIA, CpMarcadorTipoDataEnum.OPCIONAL, CpMarcadorTipoExibicaoEnum.IMEDIATA,
			CpMarcadorTipoInteressadoEnum.ATENDENTE, CpMarcadorTipoTextoEnum.DESATIVADA),
	//
	LOTACAO_PRAZO_XOR(103, "Interno - Etiqueta Agendada Mutuamente Exclusiva",
			"Remove o marcador atual caso seja definido um novo", CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO,
			CpMarcadorTipoAplicacaoEnum.TODAS_AS_VIAS_OU_ULTIMO_VOLUME, CpMarcadorTipoDataEnum.OBRIGATORIA,
			CpMarcadorTipoDataEnum.OPCIONAL, CpMarcadorTipoExibicaoEnum.IMEDIATA,
			CpMarcadorTipoInteressadoEnum.ATENDENTE, CpMarcadorTipoTextoEnum.DESATIVADA),
	//
	LOTACAO_DIRECIONADO(104, "Interno - Etiqueta Direcionada", "Marcar expediente/processo para unidades ou pessoas",
			CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO, CpMarcadorTipoAplicacaoEnum.VIA_ESPECIFICA_OU_ULTIMO_VOLUME,
			CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoExibicaoEnum.IMEDIATA,
			CpMarcadorTipoInteressadoEnum.LOTACAO_OU_PESSOA, CpMarcadorTipoTextoEnum.DESATIVADA),
	//
	LOTACAO_LISTA(105, "Interno - Lista", "Agrupar vias/processos de interesse",
			CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO, CpMarcadorTipoAplicacaoEnum.VIA_ESPECIFICA_OU_ULTIMO_VOLUME,
			CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoExibicaoEnum.IMEDIATA,
			CpMarcadorTipoInteressadoEnum.LOTACAO_OU_PESSOA, CpMarcadorTipoTextoEnum.DESATIVADA),
	//
	LOTACAO_LISTA_PRAZO(106, "Interno - Lista Agendada", "Inserir via/processo em lista a partir de uma data",
			CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO, CpMarcadorTipoAplicacaoEnum.VIA_ESPECIFICA_OU_ULTIMO_VOLUME,
			CpMarcadorTipoDataEnum.OBRIGATORIA, CpMarcadorTipoDataEnum.OPCIONAL, CpMarcadorTipoExibicaoEnum.MENOR_DATA,
			CpMarcadorTipoInteressadoEnum.LOTACAO_OU_PESSOA, CpMarcadorTipoTextoEnum.DESATIVADA),
//	//
//	SINALIZAR_ATENDENTE_IMEDIATA(101, "Sinalizar",
//			"Sinalizar, para todas as unidades que receberem o documento, no momento do recebimento",
//			CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO, CpMarcadorTipoAplicacaoEnum.VIA_ESPECIFICA_OU_ULTIMO_VOLUME,
//			CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoExibicaoEnum.IMEDIATA,
//			CpMarcadorTipoInteressadoEnum.ATENDENTE, CpMarcadorTipoTextoEnum.DESATIVADA),
//	//
//	SINALIZAR_ATENDENTE_DATAS(102, "Sinalizar com Prazo",
//			"Sinalizar, para a unidade que estiver com o documento, a partir de uma data",
//			CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO, CpMarcadorTipoAplicacaoEnum.VIA_ESPECIFICA_OU_ULTIMO_VOLUME,
//			CpMarcadorTipoDataEnum.OBRIGATORIA, CpMarcadorTipoDataEnum.OPCIONAL, CpMarcadorTipoExibicaoEnum.MENOR_DATA,
//			CpMarcadorTipoInteressadoEnum.ATENDENTE, CpMarcadorTipoTextoEnum.DESATIVADA)
	;

	private final Integer id;
	private final String nome;
	private final String descricao;
	private final CpTipoMarcadorEnum idTpMarcador;
	private CpMarcadorTipoAplicacaoEnum idTpAplicacao;
	private CpMarcadorTipoDataEnum idTpDataPlanejada;
	private CpMarcadorTipoDataEnum idTpDataLimite;
	private CpMarcadorTipoExibicaoEnum idTpExibicao;
	private CpMarcadorTipoInteressadoEnum idTpInteressado;
	private CpMarcadorTipoTextoEnum idTpTexto;

	private CpMarcadorFinalidadeEnum(Integer id, String nome, String descricao, CpTipoMarcadorEnum idTpMarcador,
			CpMarcadorTipoAplicacaoEnum idTpAplicacao, CpMarcadorTipoDataEnum idTpDataPlanejada,
			CpMarcadorTipoDataEnum idTpDataLimite, CpMarcadorTipoExibicaoEnum idTpExibicao,
			CpMarcadorTipoInteressadoEnum idTpInteressado, CpMarcadorTipoTextoEnum idTpTexto) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.idTpMarcador = idTpMarcador;
		this.idTpAplicacao = idTpAplicacao;
		this.idTpDataLimite = idTpDataLimite;
		this.idTpDataPlanejada = idTpDataPlanejada;
		this.idTpExibicao = idTpExibicao;
		this.idTpInteressado = idTpInteressado;
		this.idTpTexto = idTpTexto;
	}

	public static List<String> getList() {
		List<String> list = new ArrayList<String>();
		for (CpMarcadorFinalidadeEnum item : values()) {
			list.add(item.descricao);
		}
		return list;
	}

	public Integer getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public CpTipoMarcadorEnum getIdTpMarcador() {
		return idTpMarcador;
	}

	public CpMarcadorTipoAplicacaoEnum getIdTpAplicacao() {
		return idTpAplicacao;
	}

	public CpMarcadorTipoDataEnum getIdTpDataPlanejada() {
		return idTpDataPlanejada;
	}

	public CpMarcadorTipoDataEnum getIdTpDataLimite() {
		return idTpDataLimite;
	}

	public CpMarcadorTipoExibicaoEnum getIdTpExibicao() {
		return idTpExibicao;
	}

	public CpMarcadorTipoInteressadoEnum getIdTpInteressado() {
		return idTpInteressado;
	}

	public CpMarcadorTipoTextoEnum getIdTpTexto() {
		return idTpTexto;
	}

}