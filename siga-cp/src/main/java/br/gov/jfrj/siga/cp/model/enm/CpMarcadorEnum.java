package br.gov.jfrj.siga.cp.model.enm;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.dp.CpMarcador;

public enum CpMarcadorEnum {
	//
	EM_ELABORACAO(1, "Em Elaboração", "fas fa-lightbulb", "", CpMarcadorGrupoEnum.EM_ELABORACAO),
	//
	EM_ANDAMENTO(2, "Aguardando Andamento", "fas fa-clock", "", CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	A_RECEBER(3, "A Receber", "fas fa-inbox", "", CpMarcadorGrupoEnum.CAIXA_DE_ENTRADA),
	//
	EXTRAVIADO(4, "Extraviado", "fas fa-inbox", "", CpMarcadorGrupoEnum.ALERTA),
	//
	A_ARQUIVAR(5, "A Arquivar", "fas fa-inbox", "", CpMarcadorGrupoEnum.OUTROS),
	//
	ARQUIVADO_CORRENTE(6, "Arquivado Corrente", "fas fa-inbox", "", CpMarcadorGrupoEnum.OUTROS),
	//
	A_ELIMINAR(7, "A Eliminar", "fas fa-inbox", "", CpMarcadorGrupoEnum.AGUARDANDO_ACAO_DE_TEMPORALIDADE),
	//
	ELIMINADO(8, "Eliminado", "fas fa-power-off", "", CpMarcadorGrupoEnum.OUTROS),
	//
	JUNTADO(9, "Juntado", "fas fa-lock", "", CpMarcadorGrupoEnum.OUTROS),
	//
	JUNTADO_EXTERNO(16, "Juntado Externo", "fas fa-lock", "", CpMarcadorGrupoEnum.OUTROS),
	//
	CANCELADO(CpMarcador.ID_MARCADOR_CANCELADO, SigaMessages.getMessage("marcador.cancelado.label"), "fas fa-ban", "",
			CpMarcadorGrupoEnum.OUTROS),
	//
	TRANSFERIDO_A_ORGAO_EXTERNO(11, "Tranferido a Órgão Externo", "fas fa-paper-plane", "", CpMarcadorGrupoEnum.OUTROS),

	//
	ARQUIVADO_INTERMEDIARIO(12, "Arquivado Intermediário", "fas fa-inbox", "", CpMarcadorGrupoEnum.OUTROS),
	//
	CAIXA_DE_ENTRADA(14, "A Receber", "fas fa-inbox", "", CpMarcadorGrupoEnum.CAIXA_DE_ENTRADA),
	//
	ARQUIVADO_PERMANENTE(13, "Arquivado Permanente", "fas fa-inbox", "", CpMarcadorGrupoEnum.OUTROS),
	//
	PENDENTE_DE_ASSINATURA(15, "Pendente de Assinatura", "fas fa-key", "", CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	JUNTADO_A_DOCUMENTO_EXTERNO(16, "Juntado a Documento Externo", "fas fa-inbox", "", CpMarcadorGrupoEnum.OUTROS),
	//
	A_REMETER_PARA_PUBLICACAO(17, "A Remeter para Publicação", "fas fa-scroll", "",
			CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),

	//
	REMETIDO_PARA_PUBLICACAO(18, "Remetido para Publicação", "fas fa-scroll", "", CpMarcadorGrupoEnum.OUTROS),
	//
	A_REMETER_MANUALMENTE(19, "A Remeter Manualmente", "fas fa-scroll", "", CpMarcadorGrupoEnum.OUTROS),
	//
	PUBLICADO(20, "Publicado", "fas fa-scroll", "", CpMarcadorGrupoEnum.OUTROS),
	//
	PUBLICACAO_SOLICITADA(21, "Publicação Solicitada", "fas fa-scroll", "", CpMarcadorGrupoEnum.OUTROS),
	//
	DISPONIBILIZADO(22, "Disponibilizado", "fas fa-scroll", "", CpMarcadorGrupoEnum.OUTROS),

	//
	EM_TRANSITO(23, "Em Trâmite Físico", "fas fa-truck", "", CpMarcadorGrupoEnum.CAIXA_DE_SAIDA),
	//
	EM_TRANSITO_ELETRONICO(24, "Em Trâmite", "fas fa-shipping-fast", "", CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	COMO_SUBSCRITOR(25, "Como Subscritor", "fas fa-key", "", CpMarcadorGrupoEnum.A_ASSINAR),
	//
	APENSADO(26, "Apensado", "fas fa-compress-arrows-alt", "", CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	COMO_GESTOR(27, "Gestor", "fas fa-tag", "", CpMarcadorGrupoEnum.ACOMPANHANDO),

	//
	COMO_INTERESSADO(28, "Interessado", "fas fa-tag", "", CpMarcadorGrupoEnum.ACOMPANHANDO),
	//
	DESPACHO_PENDENTE_DE_ASSINATURA(29, "Despacho Pendente de Assinatura", "fas fa-key", "",
			CpMarcadorGrupoEnum.ALERTA),
	//
	ANEXO_PENDENTE_DE_ASSINATURA(30, "Anexo Pendente de Assinatura", "fas fa-key", "", CpMarcadorGrupoEnum.ALERTA),
	//
	SOBRESTADO(31, "Sobrestado", "fas fa-hourglass-start", "", CpMarcadorGrupoEnum.ACOMPANHANDO),
	//
	SEM_EFEITO(32, SigaMessages.getMessage("marcador.semEfeito.label"), "fas fa-power-off", "",
			CpMarcadorGrupoEnum.NENHUM),

	//
	ATIVO(36, "Ativo", "inbox", "", CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	NOVO(37, "Novo", "inbox", "", CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	POPULAR(38, "Popular", "inbox", "", CpMarcadorGrupoEnum.ALERTA),
	//
	REVISAR(39, "A Revisar", "fas fa-glasses", "", CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	TOMAR_CIENCIA(40, "Tomar Ciência", "inbox", "", CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),

	//
	SOLICITACAO_A_RECEBER(41, "A Receber", "inbox", "", CpMarcadorGrupoEnum.CAIXA_DE_ENTRADA),
	//
	SOLICITACAO_EM_ANDAMENTO(42, "Em Andamento", "inbox", "", CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	SOLICITACAO_FECHADO(43, "Fechado", "inbox", "", CpMarcadorGrupoEnum.OUTROS),
	//
	SOLICITACAO_PENDENTE(44, "Pendente", "inbox", "", CpMarcadorGrupoEnum.OUTROS),
	//
	SOLICITACAO_CANCELADO(45, "Cancelado", "inbox", "", CpMarcadorGrupoEnum.NENHUM),

	//
	SOLICITACAO_PRE_ATENDIMENTO(46, "Pré-atendimento", "inbox", "", CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	SOLICITACAO_POS_ATENDIMENTO(47, "Pós-atendimento", "inbox", "", CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	SOLICITACAO_COMO_CADASTRANTE(48, "Cadastrante", "inbox", "", CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	SOLICITACAO_COMO_SOLICITANTE(49, "Solicitante", "inbox", "", CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	RECOLHER_PARA_ARQUIVO_PERMANENTE(50, "Recolher Arq. Permante", "fas fa-inbox", "",
			CpMarcadorGrupoEnum.AGUARDANDO_ACAO_DE_TEMPORALIDADE),

	//
	TRANSFERIR_PARA_ARQUIVO_INTERMEDIARIO(51, "Transferir Arq. Intermediário", "fas fa-inbox", "",
			CpMarcadorGrupoEnum.AGUARDANDO_ACAO_DE_TEMPORALIDADE),
	//
	EM_EDITAL_DE_ELIMINACAO(52, "Em Edital de Eliminação", "fas fa-inbox", "",
			CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	SOLICITACAO_FECHADO_PARCIAL(53, "Fechado Parcial", "inbox", "", CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	SOLICITACAO_EM_CONTROLE_QUALIDADE(54, "Em Controle de Qualidade", "inbox", "",
			CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	A_DEVOLVER(56, "A Devolver", "fas fa-exchange-alt", "", CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),

	//
	AGUARDANDO(57, "Aguardando", "fas fa-clock", "", CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	A_DEVOLVER_FORA_DO_PRAZO(58, "A Devolver Fora do Prazo", "fas fa-exchange-alt", "", CpMarcadorGrupoEnum.ALERTA),
	//
	AGUARDANDO_DEVOLUCAO_FORA_DO_PRAZO(59, "Aguardando Devolução Fora Do Prazo", "fas fa-exchange-alt", "",
			CpMarcadorGrupoEnum.ALERTA),
	//
	PENDENTE_DE_ANEXACAO(60, "Pendente de Anexação", "fas fa-arrow-alt-circle-up", "", CpMarcadorGrupoEnum.ALERTA),
	//
	SOLICITACAO_EM_ELABORACAO(61, "Em Elaboração", "inbox", "", CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),

	//
	DOCUMENTO_ASSINADO_COM_SENHA(62, "Assinado com Senha", "fas fa-key", "", CpMarcadorGrupoEnum.NENHUM),
	//
	MOVIMENTACAO_ASSINADA_COM_SENHA(63, "Movimentação Ass. com Senha", "fas fa-key", "", CpMarcadorGrupoEnum.NENHUM),
	//
	MOVIMENTACAO_CONFERIDA_COM_SENHA(64, "Movimentação Autenticada com Senha", "fas fa-key", "",
			CpMarcadorGrupoEnum.NENHUM),
	//
	SOLICITACAO_FORA_DO_PRAZO(65, "Fora do Prazo", "inbox", "", CpMarcadorGrupoEnum.ALERTA),
	//
	SOLICITACAO_ATIVO(66, "Ativo", "inbox", "", CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),

	//
	PENDENTE_DE_COLABORACAO(67, "Pendente de Colaboração", "fas fa-users-cog", "",
			CpMarcadorGrupoEnum.CAIXA_DE_ENTRADA),
	//
	FINALIZAR_DOCUMENTO_COLABORATIVO(68, "Finalizar Documento Colaborativo", "fas fa-users-cog", "",
			CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	SOLICITACAO_NECESSITA_PROVIDENCIA(69, "Necessita Providência", "inbox", "", CpMarcadorGrupoEnum.ALERTA),
	//
	COMO_EXECUTOR(70, "Executor", "inbox", "", CpMarcadorGrupoEnum.ACOMPANHANDO),
	//
	PRONTO_PARA_ASSINAR(71, "Pronto para Assinar", "fas fa-check-circle", "", CpMarcadorGrupoEnum.PRONTO_PARA_ASSINAR),
	//
	COMO_REVISOR(72, "Como Revisor", "fas fa-glasses", "", CpMarcadorGrupoEnum.A_REVISAR),
	//
	PORTAL_TRANSPARENCIA(73, "Portal da Transparência", "fas fa-globe", "", CpMarcadorGrupoEnum.NENHUM),
	//
	PRAZO_DE_ASSINATURA_EXPIRADO(74, "Prazo de Assinatura Expirado", "fas fa-user-clock", "", CpMarcadorGrupoEnum.ALERTA),
	//
	ASSINADO(75, "Assinado", "fas fa-certificate", "", CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	AGUARDANDO_CONCLUSAO(76, "Aguardando Conclusão", "fas fa-clock", "", CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	URGENTE(1000, "Urgente", "fas fa-bomb", "", CpMarcadorGrupoEnum.ALERTA),

	//
	IDOSO(1001, "Idoso", "fas fa-user-tag", "", CpMarcadorGrupoEnum.ALERTA),

	//
	RETENCAO_INSS(1002, "Retenção de INSS", "fas fa-tag", "", CpMarcadorGrupoEnum.ALERTA),
	//
	PRIORITARIO(1003, "Prioritário", "fas fa-star", "", CpMarcadorGrupoEnum.ALERTA),
	//
	RESTRICAO_ACESSO(1004, "Restrição de Acesso", "fas fa-user-secret", "", CpMarcadorGrupoEnum.ALERTA),
	//
	DOCUMENTO_ANALISADO(1005, "Documento Analisado", "fas fa-book-reader", "",
			CpMarcadorGrupoEnum.AGUARDANDO_ANDAMENTO),
	//
	COVID_19(1006, "COVID-19", "fas fa-tag", "", CpMarcadorGrupoEnum.NENHUM),
	//
	NOTA_EMPENHO(1007, "Nota de Empenho", "fas fa-tag", "", CpMarcadorGrupoEnum.NENHUM),
	//
	DEMANDA_JUDICIAL_BAIXA(1008, "Demanda Judicial Prioridade Baixa", "fas fa-tag", "", CpMarcadorGrupoEnum.ALERTA),
	//
	DEMANDA_JUDICIAL_MEDIA(1009, "Demanda Judicial Prioridade Média", "fas fa-tag", "", CpMarcadorGrupoEnum.ALERTA),
	//
	DEMANDA_JUDICIAL_ALTA(1010, "Demanda Judicial Prioridade Alta", "fas fa-tag", "", CpMarcadorGrupoEnum.ALERTA);

	private CpMarcadorEnum(int id, String nome, String icone, String descricao, CpMarcadorGrupoEnum grupo) {
		this.id = id;
		this.nome = nome;
		this.icone = icone;
		this.descricao = descricao;
		this.grupo = grupo;
	}

	public static CpMarcadorEnum getById(int id) {
		for (CpMarcadorEnum i : CpMarcadorEnum.values()) {
			if (i.id == id)
				return i;
		}
		return null;
	}

	public static CpMarcadorEnum getByNome(String nome) {
		for (CpMarcadorEnum i : CpMarcadorEnum.values()) {
			if (i.nome.equals(nome))
				return i;
		}
		return null;
	}

	public static List<Integer> getListIdByGrupo(String nomegrupo) {
		List<Integer> listMar = new ArrayList<Integer>();
		for (CpMarcadorEnum mar : CpMarcadorEnum.values()) {
			if (mar.getGrupo().getNome().equals(nomegrupo)) {
				listMar.add(mar.id);
			}
		}
		return listMar;
	}

	public String getIcone() {
		return icone;
	}

	public long getId() {
		return (long) id;
	}

	public String getNome() {
		if (SigaMessages.isSigaSP() && nome.equals("Como Subscritor")) {
			return "Responsável pela Assinatura";
		} else {
			return nome;
		}
	}

	public CpMarcadorGrupoEnum getGrupo() {
		return grupo;
	}

	public final int id;
	private final String nome;
	private final String icone;
	private final String descricao;
	private final CpMarcadorGrupoEnum grupo;

}