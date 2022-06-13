package br.gov.jfrj.siga.ex.model.enm;

import java.util.HashSet;
import java.util.Set;

import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.cp.model.enm.CpTipoDeMovimentacao;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeMovimentacao;

public enum ExTipoDeMovimentacao implements ITipoDeMovimentacao {

	CRIACAO(1, "Criação"),
	//
	ANEXACAO(2, "Anexação"),
	//
	TRANSFERENCIA(3, "Transferência"),
	//
	RECEBIMENTO(4, "Recebimento"),
	//
	DESPACHO(5, "Despacho"),
	//
	DESPACHO_TRANSFERENCIA(6, "Despacho com Transferência"),
	//
	DESPACHO_INTERNO(7, "Despacho Interno"),
	//
	DESPACHO_INTERNO_TRANSFERENCIA(8, "Despacho Interno com Transferência"),
	//
	ARQUIVAMENTO_CORRENTE(9, "Arquivamento Corrente"),
	//
	// TODO: remover o teste da existencia de tipo de movimentacao de eliminacao
	// na hora da busca, pois isso aumenta muito o tempo do query. Se for o
	// caso, criar um DNM_ELIMINADO.
	ELIMINACAO(10, "Eliminação"),
	//
	ASSINATURA_DIGITAL_DOCUMENTO(11, "Assinatura"),
	//
	JUNTADA(12, "Juntada"),
	//
	JUNTADA_EXTERNO(31, "Desentranhamento"),
	//
	CANCELAMENTO_JUNTADA(13, "Desentranhamento"),
	//
	CANCELAMENTO_DE_MOVIMENTACAO(14, "Cancelamento de Movimentação"),
	//
	EXTRAVIO(15, "Extravio"),
	//
	REFERENCIA(16, "Vinculação"),
	//
	// CANCELAMENTO(17, "CANCELAMENTO"),
	//
	TRANSFERENCIA_EXTERNA(17, "Transferência Externa"),
	//
	DESPACHO_TRANSFERENCIA_EXTERNA(18, "Despacho com Transferência Externa"),
	//
	ARQUIVAMENTO_PERMANENTE(19, "Recolhimento ao Arq. Permanente"),
	//
	ARQUIVAMENTO_INTERMEDIARIO(20, "Arquivamento Intermediário"),
	//
	DESARQUIVAMENTO_CORRENTE(21, "Desarquivamento"),
	//
	ASSINATURA_DIGITAL_MOVIMENTACAO(22, "Assinatura de Movimentação"),
	//
	RECEBIMENTO_TRANSITORIO(23, "Recebimento Transitório"),
	//
	INCLUSAO_DE_COSIGNATARIO(24, "Inclusão de Cossignatário"),
	//
	REGISTRO_ASSINATURA_DOCUMENTO(25, "Registro de Assinatura"),
	//
	REGISTRO_ASSINATURA_MOVIMENTACAO(26, "Registro de Assinatura de Movimentação"),
	//
	ATUALIZACAO(27, "Atualização"),
	//
	ANOTACAO(28, "Anotação"),
	//
	REDEFINICAO_NIVEL_ACESSO(29, "Redefinição de Sigilo"),
	//
	REGISTRO_ACESSO_INDEVIDO(30, "Registro de Acesso Alheio"),
	//
	JUNTADA_A_DOCUMENTO_EXTERNO(31, "Juntada a Documento Externo"),
	//
	AGENDAMENTO_DE_PUBLICACAO(32, "Agendamento de Publicação no DJE"),
	//
	REMESSA_PARA_PUBLICACAO(33, "Remessa para Publicação"),
	//
	CONFIRMACAO_DE_REMESSA_MANUAL(34, "Confirmação de Remessa Manual"),
	//
	DISPONIBILIZACAO(35, "Disponibilização"),
	//
	AGENDAMENTO_DE_PUBLICACAO_BOLETIM(36, "Solicitação de Publicação no Boletim"),
	//
	PUBLICACAO_BOLETIM(37, "Publicação do Boletim"),
	//
	PEDIDO_PUBLICACAO(38, "Pedido de Publicação no DJE"),
	//
	// DISPONIBILIZACAO(39, "Revolvimento Unidirecional"),
	//
	NOTIFICACAO_PUBL_BI(40, "Notificação de Publicação no Boletim"),
	//
	APENSACAO(41, "Apensação"),
	//
	DESAPENSACAO(42, "Desapensação"),
	//
	ENCERRAMENTO_DE_VOLUME(43, "Encerramento de Volume"),
	//
	VINCULACAO_PAPEL(44, "Definição de Perfil"),
	//
	CONFERENCIA_COPIA_DOCUMENTO(45, "Autenticação de Documento"),
	//
	SOBRESTAR(46, "Sobrestar"),
	//
	DESOBRESTAR(47, "Dessobrestar"),
	//
	TORNAR_SEM_EFEITO(48, "Tornar sem Efeito"),
	//
	INDICACAO_GUARDA_PERMANENTE(49, "Indicação para Guarda Permanente"),
	//
	REVERSAO_INDICACAO_GUARDA_PERMANENTE(50, "Reversão de Ind. para Guarda Permanente"),
	//
	RECLASSIFICACAO(51, "Reclassificação"),
	//
	AVALIACAO(52, "Avaliação"),
	//
	AVALIACAO_COM_RECLASSIFICACAO(53, "Avaliação com Reclassificação"),
	//
	INCLUSAO_EM_EDITAL_DE_ELIMINACAO(54, "Inclusão em Edital de Eliminação"),
	//
	RETIRADA_DE_EDITAL_DE_ELIMINACAO(55, "Retirada de Edital de Eliminação"),
	//
	DESARQUIVAMENTO_INTERMEDIARIO(56, "Desarquivamento Intermediário"),
	//
	PENDENCIA_DE_ANEXACAO(57, "Pendência de Anexação"),
	//
	ASSINATURA_COM_SENHA(58, "Assinatura com senha"),
	//
	ASSINATURA_MOVIMENTACAO_COM_SENHA(59, "Assinatura de movimentação com senha"),
	//
	CONFERENCIA_COPIA_COM_SENHA(60, "Autenticação de Documento com senha"),
	//
	CONTROLE_DE_COLABORACAO(61, "Controle de Coloboração"),
	//
	MARCACAO(62, "Marcação"),
	//
	COPIA(63, "Inclusão de Cópia"),
	//
	ANEXACAO_DE_ARQUIVO_AUXILIAR(64, "Anexação de Arquivo Auxiliar"),
	//
	SOLICITACAO_DE_ASSINATURA(65, "Solicitação de Assinatura"),
	//
	CIENCIA(66, "Tomar Ciência"),
	//
	AUTUAR(67, "Autuação"),
	//
	RESTRINGIR_ACESSO(70, "Restringir Acesso"),
	//
	REFAZER(71, "Refazer Documento"),
	//
	ASSINATURA_POR(72, "Assinatura \"por\""),
	//
	GERAR_PROTOCOLO(73, "Gerar Protocolo"),
	//
	/*
	 * // alteracao para insercao do historico do substituto de assinatura
	 */
	//
	SUBSTITUICAO_RESPONSAVEL(74, "Substitução de Responsável"),
	//
	REORDENACAO_DOCUMENTO(75, "Reordenar"),
	//
	ORDENACAO_ORIGINAL_DOCUMENTO(76, "Ordenar"),
	//
	PUBLICACAO_PORTAL_TRANSPARENCIA(77, "Publicação no Portal da Transparência"),
	//
	EXIBIR_NO_ACOMPANHAMENTO_DO_PROTOCOLO(79, "Disponibilizar no Acompanhamento do Protocolo"),
	//
	PRAZO_ASSINATURA(81, "Prazo para Assinatura"),
	//
	TRAMITE_PARALELO(82, "Trâmite Paralelo"),
	//
	NOTIFICACAO(83, "Notificação"),
	//
	CONCLUSAO(84, "Conclusão de Trâmite"),
	//
	ENVIO_SIAFEM(85, "Envio ao SIAFEM"),
	//
	GERAR_LINK_PUBLICO_PROCESSO(86, "Gerar link público do Processo"),

	VISUALIZACAO_EXTERNA(87, "Visualização Externa");

	private final int id;
	private final String descr;

	ExTipoDeMovimentacao(int id, String descr) {
		this.id = id;
		this.descr = descr;
	}

	public int getId() {
		return id;
	}

	public String getDescr() {
        String descricaoi18n = SigaMessages.getMessage("enum.extipodemovimentacao." 
        										+ this.toString().toLowerCase().replaceAll("_", "."));
        if (descricaoi18n.startsWith("???."))
            return this.descr;
	 	else
	 		return descricaoi18n;
	}

	public static ITipoDeMovimentacao getById(Integer id) {
		if (id == null)
			return null;
		return CpTipoDeMovimentacao.getById(id);
	}
	
	public static boolean hasDespacho(ITipoDeMovimentacao id) {
		return id == ExTipoDeMovimentacao.DESPACHO
				|| id == ExTipoDeMovimentacao.DESPACHO_INTERNO
				|| id == ExTipoDeMovimentacao.DESPACHO_INTERNO_TRANSFERENCIA
				|| id == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA
				|| id == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA_EXTERNA;
	}

	public static boolean hasDocumento(ITipoDeMovimentacao id) {
		return id == ExTipoDeMovimentacao.ANEXACAO || hasDespacho(id);
	}

	public static boolean hasApensacao(ITipoDeMovimentacao id) {
		return id == ExTipoDeMovimentacao.APENSACAO
				|| id == ExTipoDeMovimentacao.DESAPENSACAO;
	}

	public static boolean hasTransferencia(ITipoDeMovimentacao id) {
		return id == ExTipoDeMovimentacao.TRANSFERENCIA
				|| id == ExTipoDeMovimentacao.TRAMITE_PARALELO
				|| id == ExTipoDeMovimentacao.NOTIFICACAO
				|| id == ExTipoDeMovimentacao.DESPACHO_INTERNO_TRANSFERENCIA
				|| id == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA
				|| id == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA_EXTERNA
				|| id == ExTipoDeMovimentacao.TRANSFERENCIA_EXTERNA;
	}

	public static boolean hasRecebimento(ITipoDeMovimentacao id) {
		return id == ExTipoDeMovimentacao.RECEBIMENTO
				|| id == ExTipoDeMovimentacao.RECEBIMENTO_TRANSITORIO
				|| id == ExTipoDeMovimentacao.CONCLUSAO;
	}
	
   public static Set<ExTipoDeMovimentacao> listaTipoMovimentacoesExcluiveisFisicamente() {
         Set<ExTipoDeMovimentacao> listaTipoMovimentacoes  = new HashSet<ExTipoDeMovimentacao>();

         listaTipoMovimentacoes.add(ANOTACAO);
         listaTipoMovimentacoes.add(INCLUSAO_DE_COSIGNATARIO);
         listaTipoMovimentacoes.add(ANEXACAO);
         listaTipoMovimentacoes.add(INCLUSAO_EM_EDITAL_DE_ELIMINACAO);

         return listaTipoMovimentacoes;

    }

	public static boolean hasRecebimentoOuCriacao(ITipoDeMovimentacao id) {
		return id == ExTipoDeMovimentacao.CRIACAO
				|| id == ExTipoDeMovimentacao.RECEBIMENTO;
	}

}
