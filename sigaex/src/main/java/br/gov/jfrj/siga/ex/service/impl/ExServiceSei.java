/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.siga.ex.service.impl;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebService;

import br.gov.jfrj.siga.Remote;

@WebService(targetNamespace = "http://impl.sei.service.ex.siga.jfrj.gov.br/")
public interface ExServiceSei extends Remote {

	/**
	 * Geracao de processos
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.RetornoGeracaoProcedimento gerarProcedimento(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade,
			br.gov.jfrj.siga.ex.service.sei.Procedimento procedimento,
			br.gov.jfrj.siga.ex.service.sei.Documento[] documentos, java.lang.String[] procedimentosRelacionados,
			java.lang.String[] unidadesEnvio, java.lang.String sinManterAbertoUnidade,
			java.lang.String sinEnviarEmailNotificacao, java.lang.String dataRetornoProgramado,
			java.lang.String diasRetornoProgramado, java.lang.String sinDiasUteisRetornoProgramado,
			java.lang.String idMarcador, java.lang.String textoMarcador, java.lang.String dataControlePrazo,
			java.lang.String diasControlePrazo, java.lang.String sinDiasUteisControlePrazo)
			throws java.rmi.RemoteException;

	/**
	 * Geracao de documentos
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.RetornoInclusaoDocumento incluirDocumento(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade,
			br.gov.jfrj.siga.ex.service.sei.Documento documento) throws java.rmi.RemoteException;

	/**
	 * Lista de unidades disponiveis
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.Unidade[] listarUnidades(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idTipoProcedimento, java.lang.String idSerie)
			throws java.rmi.RemoteException;

	/**
	 * Lista de tipos de processo disponiveis
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.TipoProcedimento[] listarTiposProcedimento(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idSerie)
			throws java.rmi.RemoteException;

	/**
	 * Lista de series disponiveis
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.Serie[] listarSeries(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idTipoProcedimento)
			throws java.rmi.RemoteException;

	/**
	 * Lista de contatos
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.Contato[] listarContatos(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idTipoContato,
			java.lang.String paginaRegistros, java.lang.String paginaAtual, java.lang.String sigla,
			java.lang.String nome, java.lang.String cpf, java.lang.String cnpj, java.lang.String matricula,
			java.lang.String[] idContatos) throws java.rmi.RemoteException;

	/**
	 * Atualizacao de contatos
	 */
	@WebMethod
	public java.lang.String atualizarContatos(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, br.gov.jfrj.siga.ex.service.sei.Contato[] contatos)
			throws java.rmi.RemoteException;

	/**
	 * Consulta de processos
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.RetornoConsultaProcedimento consultarProcedimento(
			java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade,
			java.lang.String protocoloProcedimento, java.lang.String sinRetornarAssuntos,
			java.lang.String sinRetornarInteressados, java.lang.String sinRetornarObservacoes,
			java.lang.String sinRetornarAndamentoGeracao, java.lang.String sinRetornarAndamentoConclusao,
			java.lang.String sinRetornarUltimoAndamento, java.lang.String sinRetornarUnidadesProcedimentoAberto,
			java.lang.String sinRetornarProcedimentosRelacionados, java.lang.String sinRetornarProcedimentosAnexados)
			throws java.rmi.RemoteException;

	/**
	 * Consulta de processos individuais por usuario interessado
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.ProcedimentoResumido consultarProcedimentoIndividual(
			java.lang.String siglaSistema, java.lang.String identificacaoServico, java.lang.String idUnidade,
			java.lang.String idOrgaoProcedimento, java.lang.String idTipoProcedimento, java.lang.String idOrgaoUsuario,
			java.lang.String siglaUsuario) throws java.rmi.RemoteException;

	/**
	 * Consulta de documentos
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.RetornoConsultaDocumento consultarDocumento(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloDocumento,
			java.lang.String sinRetornarAndamentoGeracao, java.lang.String sinRetornarAssinaturas,
			java.lang.String sinRetornarPublicacao, java.lang.String sinRetornarCampos) throws java.rmi.RemoteException;

	/**
	 * Cancelamento de documentos
	 */
	@WebMethod
	public java.lang.String cancelarDocumento(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String protocoloDocumento, java.lang.String motivo)
			throws java.rmi.RemoteException;

	/**
	 * Bloqueio de documentos
	 */
	@WebMethod
	public java.lang.String bloquearDocumento(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String protocoloDocumento) throws java.rmi.RemoteException;

	/**
	 * Geracao de bloco
	 */
	@WebMethod
	public java.lang.String gerarBloco(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String tipo, java.lang.String descricao,
			java.lang.String[] unidadesDisponibilizacao, java.lang.String[] documentos,
			java.lang.String sinDisponibilizar) throws java.rmi.RemoteException;

	/**
	 * Consulta de bloco
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.RetornoConsultaBloco consultarBloco(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idBloco,
			java.lang.String sinRetornarProtocolos) throws java.rmi.RemoteException;

	/**
	 * Exclusao de bloco
	 */
	@WebMethod
	public java.lang.String excluirBloco(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String idBloco) throws java.rmi.RemoteException;

	/**
	 * Exclusao de processo
	 */
	@WebMethod
	public java.lang.String excluirProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String protocoloProcedimento) throws java.rmi.RemoteException;

	/**
	 * Exclusao de documento
	 */
	@WebMethod
	public java.lang.String excluirDocumento(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String protocoloDocumento) throws java.rmi.RemoteException;

	/**
	 * Disponibilizacao de bloco
	 */
	@WebMethod
	public java.lang.String disponibilizarBloco(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String idBloco) throws java.rmi.RemoteException;

	/**
	 * Cancelamento de disponibilizacao de bloco
	 */
	@WebMethod
	public java.lang.String cancelarDisponibilizacaoBloco(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idBloco)
			throws java.rmi.RemoteException;

	/**
	 * Conclusao de bloco
	 */
	@WebMethod
	public java.lang.String concluirBloco(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String idBloco) throws java.rmi.RemoteException;

	/**
	 * Reabertura de bloco
	 */
	@WebMethod
	public java.lang.String reabrirBloco(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String idBloco) throws java.rmi.RemoteException;

	/**
	 * Devolucao de bloco
	 */
	@WebMethod
	public java.lang.String devolverBloco(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String idBloco) throws java.rmi.RemoteException;

	/**
	 * Inclusao de documento em bloco
	 */
	@WebMethod
	public java.lang.String incluirDocumentoBloco(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String idBloco, java.lang.String protocoloDocumento,
			java.lang.String anotacao) throws java.rmi.RemoteException;

	/**
	 * Remocao de documento de bloco
	 */
	@WebMethod
	public java.lang.String retirarDocumentoBloco(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String idBloco, java.lang.String protocoloDocumento)
			throws java.rmi.RemoteException;

	/**
	 * Inclusao de processo em bloco
	 */
	@WebMethod
	public java.lang.String incluirProcessoBloco(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String idBloco, java.lang.String protocoloProcedimento,
			java.lang.String anotacao) throws java.rmi.RemoteException;

	/**
	 * Remocao de processo de bloco
	 */
	@WebMethod
	public java.lang.String retirarProcessoBloco(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String idBloco, java.lang.String protocoloProcedimento)
			throws java.rmi.RemoteException;

	/**
	 * Reabertura de processo
	 */
	@WebMethod
	public java.lang.String reabrirProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String protocoloProcedimento) throws java.rmi.RemoteException;

	/**
	 * Conclusao de processo
	 */
	@WebMethod
	public java.lang.String concluirProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String protocoloProcedimento) throws java.rmi.RemoteException;

	/**
	 * Lista de extensoes de arquivos permitidas
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.ArquivoExtensao[] listarExtensoesPermitidas(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idArquivoExtensao)
			throws java.rmi.RemoteException;

	/**
	 * Movimentacao de processo entre unidades
	 */
	@WebMethod
	public java.lang.String enviarProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String protocoloProcedimento, java.lang.String[] unidadesDestino,
			java.lang.String sinManterAbertoUnidade, java.lang.String sinRemoverAnotacao,
			java.lang.String sinEnviarEmailNotificacao, java.lang.String dataRetornoProgramado,
			java.lang.String diasRetornoProgramado, java.lang.String sinDiasUteisRetornoProgramado,
			java.lang.String sinReabrir) throws java.rmi.RemoteException;

	/**
	 * Lista de usuarios com permissao na unidade
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.Usuario[] listarUsuarios(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idUsuario)
			throws java.rmi.RemoteException;

	/**
	 * Atribuicao de processo para usuario na unidade
	 */
	@WebMethod
	public java.lang.String atribuirProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String protocoloProcedimento, java.lang.String idUsuario,
			java.lang.String sinReabrir) throws java.rmi.RemoteException;

	/**
	 * Lista de hipoteses legais disponiveis
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.HipoteseLegal[] listarHipotesesLegais(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String nivelAcesso)
			throws java.rmi.RemoteException;

	/**
	 * Lista de tipos de conferencia disponiveis
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.TipoConferencia[] listarTiposConferencia(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade) throws java.rmi.RemoteException;

	/**
	 * Lista de paises disponiveis
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.Pais[] listarPaises(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade) throws java.rmi.RemoteException;

	/**
	 * Lista de estados disponiveis
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.Estado[] listarEstados(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idPais)
			throws java.rmi.RemoteException;

	/**
	 * Lista de cidades disponiveis
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.Cidade[] listarCidades(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idPais,
			java.lang.String idEstado) throws java.rmi.RemoteException;

	/**
	 * Lista de tipos de processo da ouvidoria
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.TipoProcedimento[] listarTiposProcedimentoOuvidoria(
			java.lang.String siglaSistema, java.lang.String identificacaoServico) throws java.rmi.RemoteException;

	/**
	 * Lista de cargos disponiveis
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.Cargo[] listarCargos(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idCargo)
			throws java.rmi.RemoteException;

	/**
	 * Adiciona um arquivo no repositorio
	 */
	@WebMethod
	public java.lang.String adicionarArquivo(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String nome, java.lang.String tamanho, java.lang.String hash,
			java.lang.String conteudo) throws java.rmi.RemoteException;

	/**
	 * Adiciona conteudo em um arquivo do repositorio
	 */
	@WebMethod
	public java.lang.String adicionarConteudoArquivo(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idArquivo,
			java.lang.String conteudo) throws java.rmi.RemoteException;

	/**
	 * Lanca andamento em processo
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.Andamento lancarAndamento(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimento,
			java.lang.String idTarefa, java.lang.String idTarefaModulo,
			br.gov.jfrj.siga.ex.service.sei.AtributoAndamento[] atributos) throws java.rmi.RemoteException;

	/**
	 * Lista andamentos do processo
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.Andamento[] listarAndamentos(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimento,
			java.lang.String sinRetornarAtributos, java.lang.String[] andamentos, java.lang.String[] tarefas,
			java.lang.String[] tarefasModulos) throws java.rmi.RemoteException;

	/**
	 * Bloqueia processo
	 */
	@WebMethod
	public java.lang.String bloquearProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String protocoloProcedimento) throws java.rmi.RemoteException;

	/**
	 * Desbloqueia processo
	 */
	@WebMethod
	public java.lang.String desbloquearProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String protocoloProcedimento) throws java.rmi.RemoteException;

	/**
	 * Relaciona processos
	 */
	@WebMethod
	public java.lang.String relacionarProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String protocoloProcedimento1,
			java.lang.String protocoloProcedimento2) throws java.rmi.RemoteException;

	/**
	 * Remove relacionamento entre processos
	 */
	@WebMethod
	public java.lang.String removerRelacionamentoProcesso(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimento1,
			java.lang.String protocoloProcedimento2) throws java.rmi.RemoteException;

	/**
	 * Sobrestar processo
	 */
	@WebMethod
	public java.lang.String sobrestarProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String protocoloProcedimento,
			java.lang.String protocoloProcedimentoVinculado, java.lang.String motivo) throws java.rmi.RemoteException;

	/**
	 * Remover sobrestamento processo
	 */
	@WebMethod
	public java.lang.String removerSobrestamentoProcesso(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimento)
			throws java.rmi.RemoteException;

	/**
	 * Anexar processo
	 */
	@WebMethod
	public java.lang.String anexarProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String protocoloProcedimentoPrincipal,
			java.lang.String protocoloProcedimentoAnexado) throws java.rmi.RemoteException;

	/**
	 * Desanexar processo
	 */
	@WebMethod
	public java.lang.String desanexarProcesso(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String protocoloProcedimentoPrincipal,
			java.lang.String protocoloProcedimentoAnexado, java.lang.String motivo) throws java.rmi.RemoteException;

	/**
	 * Lista marcadores da unidade
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.Marcador[] listarMarcadoresUnidade(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade) throws java.rmi.RemoteException;

	/**
	 * Define marcador em processos
	 */
	@WebMethod
	public java.lang.String definirMarcador(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, br.gov.jfrj.siga.ex.service.sei.DefinicaoMarcador[] definicoes)
			throws java.rmi.RemoteException;

	/**
	 * Define o controle de prazo do processo
	 */
	@WebMethod
	public java.lang.String definirControlePrazo(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, br.gov.jfrj.siga.ex.service.sei.DefinicaoControlePrazo[] definicoes)
			throws java.rmi.RemoteException;

	/**
	 * Conclui o controle de prazo do processo
	 */
	@WebMethod
	public java.lang.String concluirControlePrazo(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String[] protocolosProcedimentos) throws java.rmi.RemoteException;

	/**
	 * Remove o controle de prazo do processo
	 */
	@WebMethod
	public java.lang.String removerControlePrazo(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String[] protocolosProcedimentos) throws java.rmi.RemoteException;

	/**
	 * Lista andamentos de marcadores do processo
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.AndamentoMarcador[] listarAndamentosMarcadores(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimento,
			java.lang.String[] marcadores) throws java.rmi.RemoteException;

	/**
	 * Consulta Publicacao
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.RetornoConsultaPublicacao consultarPublicacao(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idPublicacao,
			java.lang.String idDocumento, java.lang.String protocoloDocumento, java.lang.String sinRetornarAndamento,
			java.lang.String sinRetornarAssinaturas) throws java.rmi.RemoteException;

	/**
	 * Agenda Publicacao
	 */
	@WebMethod
	public java.lang.String agendarPublicacao(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String idDocumento, java.lang.String protocoloDocumento,
			java.lang.String staMotivo, java.lang.String idVeiculoPublicacao, java.lang.String dataDisponibilizacao,
			java.lang.String resumo, br.gov.jfrj.siga.ex.service.sei.PublicacaoImprensaNacional imprensaNacional)
			throws java.rmi.RemoteException;

	/**
	 * Altera dados da Publicacao
	 */
	@WebMethod
	public java.lang.String alterarPublicacao(java.lang.String siglaSistema, java.lang.String identificacaoServico,
			java.lang.String idUnidade, java.lang.String idPublicacao, java.lang.String idDocumento,
			java.lang.String protocoloDocumento, java.lang.String staMotivo, java.lang.String idVeiculoPublicacao,
			java.lang.String dataDisponibilizacao, java.lang.String resumo,
			br.gov.jfrj.siga.ex.service.sei.PublicacaoImprensaNacional imprensaNacional)
			throws java.rmi.RemoteException;

	/**
	 * Cancela Agendamento da Publicacao
	 */
	@WebMethod
	public java.lang.String cancelarAgendamentoPublicacao(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idPublicacao,
			java.lang.String idDocumento, java.lang.String protocoloDocumento) throws java.rmi.RemoteException;

	/**
	 * Lista Feriados
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.Feriado[] listarFeriados(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String idOrgao,
			java.lang.String dataInicial, java.lang.String dataFinal) throws java.rmi.RemoteException;

	/**
	 * Confirma Disponibilização das Publicações
	 */
	@WebMethod
	public java.lang.String confirmarDisponibilizacaoPublicacao(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idVeiculoPublicacao,
			java.lang.String dataDisponibilizacao, java.lang.String dataPublicacao, java.lang.String numero,
			java.lang.String[] idDocumentos) throws java.rmi.RemoteException;

	/**
	 * Envia e-mail no processo
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.RetornoEnvioEmail enviarEmail(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idUnidade, java.lang.String protocoloProcedimento,
			java.lang.String de, java.lang.String para, java.lang.String CCO, java.lang.String assunto,
			java.lang.String mensagem, java.lang.String[] idDocumentos) throws java.rmi.RemoteException;

	/**
	 * Registrar Contato com Ouvidoria
	 */
	@WebMethod
	public br.gov.jfrj.siga.ex.service.sei.ProcedimentoResumido registrarOuvidoria(java.lang.String siglaSistema,
			java.lang.String identificacaoServico, java.lang.String idOrgao, java.lang.String nome,
			java.lang.String nomeSocial, java.lang.String email, java.lang.String cpf, java.lang.String rg,
			java.lang.String orgaoExpedidor, java.lang.String telefone, java.lang.String idEstado,
			java.lang.String idCidade, java.lang.String idTipoProcedimento, java.lang.String processos,
			java.lang.String sinRetorno, java.lang.String mensagem,
			br.gov.jfrj.siga.ex.service.sei.AtributoOuvidoria[] atributosAdicionais) throws java.rmi.RemoteException;

}
