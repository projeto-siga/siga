package br.gov.jfrj.siga.cp.model.enm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.gov.jfrj.siga.cp.CpTipoMarcadorEnum;
import br.gov.jfrj.siga.cp.converter.IEnumWithId;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaMessages;

public enum CpMarcadorFinalidadeEnum implements IEnumWithId {
	SISTEMA(1, CpMarcadorFinalidadeGrupoEnum.SISTEMA, "Sistema", "Marcador de Sistema",
			CpTipoMarcadorEnum.TIPO_MARCADOR_SISTEMA, null, null, null, null, null, null, false, false),
	//
	GERAL(2, CpMarcadorFinalidadeGrupoEnum.GERAL, "Geral",
			"Marcador que pode ser definido por qualquer pessoa e estará visível para quem receber o documento",
			CpTipoMarcadorEnum.TIPO_MARCADOR_GERAL, CpMarcadorTipoAplicacaoEnum.TODAS_AS_VIAS_OU_ULTIMO_VOLUME,
			CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoExibicaoEnum.IMEDIATA,
			CpMarcadorTipoInteressadoEnum.ATENDENTE, CpMarcadorTipoTextoEnum.OPCIONAL, false, true),
	//
	GERAL_AGENDADA(3, CpMarcadorFinalidadeGrupoEnum.GERAL, "Geral Agendada",
			"Marcador que pode ser definido por qualquer pessoa e estará visível, a partir de uma data definida, para quem receber o documento",
			CpTipoMarcadorEnum.TIPO_MARCADOR_GERAL, CpMarcadorTipoAplicacaoEnum.TODAS_AS_VIAS_OU_ULTIMO_VOLUME,
			CpMarcadorTipoDataEnum.OBRIGATORIA, CpMarcadorTipoDataEnum.DESATIVADA,
			CpMarcadorTipoExibicaoEnum.MENOR_DATA, CpMarcadorTipoInteressadoEnum.ATENDENTE,
			CpMarcadorTipoTextoEnum.OPCIONAL, false, false),
	//
	GERAL_PRAZOS(4, CpMarcadorFinalidadeGrupoEnum.GERAL, "Geral com Prazos",
			"Marcador que pode ser definido por qualquer pessoa e estará visível, a partir de uma data definida, para quem receber o documento, contendo a informação sobre a proximidade de um prazo",
			CpTipoMarcadorEnum.TIPO_MARCADOR_GERAL, CpMarcadorTipoAplicacaoEnum.TODAS_AS_VIAS_OU_ULTIMO_VOLUME,
			CpMarcadorTipoDataEnum.OBRIGATORIA, CpMarcadorTipoDataEnum.OBRIGATORIA,
			CpMarcadorTipoExibicaoEnum.MENOR_DATA, CpMarcadorTipoInteressadoEnum.ATENDENTE,
			CpMarcadorTipoTextoEnum.OPCIONAL, false, false),
	//
	GERAL_DIRECIONADA(5, CpMarcadorFinalidadeGrupoEnum.GERAL, "Geral Direcionada",
			"Marcador que pode ser definido por qualquer pessoa e estará visível para uma lotação ou pessoa  definida, independente da localização do documento",
			CpTipoMarcadorEnum.TIPO_MARCADOR_GERAL, CpMarcadorTipoAplicacaoEnum.GERAL,
			CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoExibicaoEnum.IMEDIATA,
			CpMarcadorTipoInteressadoEnum.LOTACAO_OU_PESSOA, CpMarcadorTipoTextoEnum.OPCIONAL, false, true),
	//
	GERAL_AGENDADA_DIRECIONADA(7, CpMarcadorFinalidadeGrupoEnum.GERAL, "Geral Agendada Direcionada",
			"Marcador que pode ser definido por qualquer pessoa e estará visível, a partir de uma data definida, para uma lotação ou pessoa definida, independente da localização do documento",
			CpTipoMarcadorEnum.TIPO_MARCADOR_GERAL, CpMarcadorTipoAplicacaoEnum.GERAL,
			CpMarcadorTipoDataEnum.OBRIGATORIA, CpMarcadorTipoDataEnum.DESATIVADA,
			CpMarcadorTipoExibicaoEnum.MENOR_DATA, CpMarcadorTipoInteressadoEnum.LOTACAO_OU_PESSOA,
			CpMarcadorTipoTextoEnum.OPCIONAL, false, false),
	//
	GERAL_LIMITE_XOR(6, CpMarcadorFinalidadeGrupoEnum.GERAL, "Geral Com Data Limite e Mutuamente Exclusiva",
			"Marcador, mutuamente exclusivo, que pode ser definido por qualquer pessoa e estará visível, com data limite, para quem receber com o documento. Apenas um marcador desse tipo pode ser definido para um mesmo documento",
			CpTipoMarcadorEnum.TIPO_MARCADOR_GERAL, CpMarcadorTipoAplicacaoEnum.TODAS_AS_VIAS_OU_ULTIMO_VOLUME,
			CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoDataEnum.OBRIGATORIA, CpMarcadorTipoExibicaoEnum.IMEDIATA,			
			CpMarcadorTipoInteressadoEnum.ATENDENTE, CpMarcadorTipoTextoEnum.OPCIONAL, true, false),
	//
	LOCAL(100, CpMarcadorFinalidadeGrupoEnum.LOCAL, "Local",
			"Marcador que pode ser definido por pessoa da minha lotação e estará visível para quem receber qualquer via do documento",
			CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO, CpMarcadorTipoAplicacaoEnum.TODAS_AS_VIAS_OU_ULTIMO_VOLUME,
			CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoExibicaoEnum.IMEDIATA,
			CpMarcadorTipoInteressadoEnum.ATENDENTE, CpMarcadorTipoTextoEnum.OPCIONAL, false, true),
	//
	LOCAL_AGENDADA(101, CpMarcadorFinalidadeGrupoEnum.LOCAL, "Local Agendada",
			"Marcador que pode ser definido por pessoa da minha lotação e estará visível, a partir de uma data definida, para quem receber qualquer via do documento",
			CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO, CpMarcadorTipoAplicacaoEnum.TODAS_AS_VIAS_OU_ULTIMO_VOLUME,
			CpMarcadorTipoDataEnum.OBRIGATORIA, CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoExibicaoEnum.IMEDIATA,
			CpMarcadorTipoInteressadoEnum.ATENDENTE, CpMarcadorTipoTextoEnum.OPCIONAL, false, false),
	//
	LOCAL_DIRECIONADA(102, CpMarcadorFinalidadeGrupoEnum.LOCAL, "Local Direcionada",
			"Marcador que pode ser definido por pessoa da minha lotação e estará visível para uma lotação ou pessoa definida, independente da localização do documento",
			CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO, CpMarcadorTipoAplicacaoEnum.GERAL,
			CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoExibicaoEnum.IMEDIATA,
			CpMarcadorTipoInteressadoEnum.LOTACAO_OU_PESSOA, CpMarcadorTipoTextoEnum.OPCIONAL, false, true),
	//
	PASTA(200, CpMarcadorFinalidadeGrupoEnum.PASTA, "Pasta", "Organizar o acervo em andamento na minha lotação",
			CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO, CpMarcadorTipoAplicacaoEnum.VIA_ESPECIFICA_OU_ULTIMO_VOLUME,
			CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoExibicaoEnum.IMEDIATA,
			CpMarcadorTipoInteressadoEnum.ATENDENTE, CpMarcadorTipoTextoEnum.OPCIONAL, true, true),
	//
	PASTA_PADRAO(201, CpMarcadorFinalidadeGrupoEnum.PASTA, "Pasta Padrão", "Pasta atribuída automaticamente ao documento tramitado para a minha lotação.",
			CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO, CpMarcadorTipoAplicacaoEnum.VIA_ESPECIFICA_OU_ULTIMO_VOLUME,
			CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoExibicaoEnum.IMEDIATA,
			CpMarcadorTipoInteressadoEnum.ATENDENTE, CpMarcadorTipoTextoEnum.OPCIONAL, true, true),
	//
	LISTA(300, CpMarcadorFinalidadeGrupoEnum.LISTA, "Lista",
			"Marcador que pode ser definido por pessoa da minha lotação para agrupar documentos de interesse de Pessoa ou Lotação definida",
			CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO, CpMarcadorTipoAplicacaoEnum.VIA_ESPECIFICA_OU_ULTIMO_VOLUME,
			CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoExibicaoEnum.IMEDIATA,
			CpMarcadorTipoInteressadoEnum.LOTACAO_OU_PESSOA, CpMarcadorTipoTextoEnum.OPCIONAL, false, false),
	//
	LISTA_AGENDADA(301, CpMarcadorFinalidadeGrupoEnum.LISTA, "Lista Agendada",
			"Marcador que pode ser definido por pessoa da minha lotação para agrupar documentos de interesse de Pessoa ou Lotação definida, a partir de uma data definida",
			CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO, CpMarcadorTipoAplicacaoEnum.VIA_ESPECIFICA_OU_ULTIMO_VOLUME,
			CpMarcadorTipoDataEnum.OBRIGATORIA, CpMarcadorTipoDataEnum.DESATIVADA, CpMarcadorTipoExibicaoEnum.MENOR_DATA,
			CpMarcadorTipoInteressadoEnum.LOTACAO_OU_PESSOA, CpMarcadorTipoTextoEnum.OPCIONAL, false, false);

	private final Integer id;
	private final CpMarcadorFinalidadeGrupoEnum grupo;
	private final String nome;
	private final String descricao;
	private final CpTipoMarcadorEnum idTpMarcador;
	private CpMarcadorTipoAplicacaoEnum idTpAplicacao;
	private CpMarcadorTipoDataEnum idTpDataPlanejada;
	private CpMarcadorTipoDataEnum idTpDataLimite;
	private CpMarcadorTipoExibicaoEnum idTpExibicao;
	private CpMarcadorTipoInteressadoEnum idTpInteressado;
	private CpMarcadorTipoTextoEnum idTpTexto;
	private boolean xor;
	private boolean arquivarOcultaAMarca;

	private CpMarcadorFinalidadeEnum(Integer id, CpMarcadorFinalidadeGrupoEnum grupo, String nome, String descricao,
			CpTipoMarcadorEnum idTpMarcador, CpMarcadorTipoAplicacaoEnum idTpAplicacao,
			CpMarcadorTipoDataEnum idTpDataPlanejada, CpMarcadorTipoDataEnum idTpDataLimite,
			CpMarcadorTipoExibicaoEnum idTpExibicao, CpMarcadorTipoInteressadoEnum idTpInteressado,
			CpMarcadorTipoTextoEnum idTpTexto, boolean xor, boolean arquivarOcultaAMarca) {
		this.id = id;
		this.grupo = grupo;
		this.nome = nome;
		this.descricao = descricao;
		this.idTpMarcador = idTpMarcador;
		this.idTpAplicacao = idTpAplicacao;
		this.idTpDataLimite = idTpDataLimite;
		this.idTpDataPlanejada = idTpDataPlanejada;
		this.idTpExibicao = idTpExibicao;
		this.idTpInteressado = idTpInteressado;
		this.idTpTexto = idTpTexto;
		this.xor = xor;
		this.arquivarOcultaAMarca = arquivarOcultaAMarca;
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

	public String getNome() {
		return nome;
	}

	public CpMarcadorFinalidadeGrupoEnum getGrupo() {
		return grupo;
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

	public static CpMarcadorFinalidadeEnum getById(Integer id) {
		for (CpMarcadorFinalidadeEnum tp : CpMarcadorFinalidadeEnum.values())
			if (tp.id.equals(id))
				return tp;
		return null;
	}

	public boolean isXor() {
		return xor;
	}

	public static List<CpMarcadorFinalidadeEnum> disponiveis(boolean geral, boolean lotacao) {
		List<String> listaFinalidadesLotaValidas = Arrays.asList(Prop.get("marcadores.lota.finalidades").split(","));
		return Arrays.asList(values()).stream()
				.filter(f -> (geral && f.getIdTpMarcador() == CpTipoMarcadorEnum.TIPO_MARCADOR_GERAL)
						|| (lotacao && f.getIdTpMarcador() == CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO
								&& (listaFinalidadesLotaValidas.contains(f.toString())
										|| listaFinalidadesLotaValidas.get(0).equals(""))))
				.collect(Collectors.toList());
	}

	public boolean isArquivarOcultaAMarca() {
		return arquivarOcultaAMarca;
	}
}