
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

import java.rmi.RemoteException;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.xml.ws.WebServiceContext;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.service.ExService;
import br.gov.jfrj.siga.ex.service.sei.Andamento;
import br.gov.jfrj.siga.ex.service.sei.AndamentoMarcador;
import br.gov.jfrj.siga.ex.service.sei.ArquivoExtensao;
import br.gov.jfrj.siga.ex.service.sei.AtributoAndamento;
import br.gov.jfrj.siga.ex.service.sei.AtributoOuvidoria;
import br.gov.jfrj.siga.ex.service.sei.Cargo;
import br.gov.jfrj.siga.ex.service.sei.Cidade;
import br.gov.jfrj.siga.ex.service.sei.Contato;
import br.gov.jfrj.siga.ex.service.sei.DefinicaoControlePrazo;
import br.gov.jfrj.siga.ex.service.sei.DefinicaoMarcador;
import br.gov.jfrj.siga.ex.service.sei.Documento;
import br.gov.jfrj.siga.ex.service.sei.Estado;
import br.gov.jfrj.siga.ex.service.sei.Feriado;
import br.gov.jfrj.siga.ex.service.sei.HipoteseLegal;
import br.gov.jfrj.siga.ex.service.sei.Marcador;
import br.gov.jfrj.siga.ex.service.sei.Pais;
import br.gov.jfrj.siga.ex.service.sei.Procedimento;
import br.gov.jfrj.siga.ex.service.sei.ProcedimentoResumido;
import br.gov.jfrj.siga.ex.service.sei.PublicacaoImprensaNacional;
import br.gov.jfrj.siga.ex.service.sei.RetornoConsultaBloco;
import br.gov.jfrj.siga.ex.service.sei.RetornoConsultaDocumento;
import br.gov.jfrj.siga.ex.service.sei.RetornoConsultaProcedimento;
import br.gov.jfrj.siga.ex.service.sei.RetornoConsultaPublicacao;
import br.gov.jfrj.siga.ex.service.sei.RetornoEnvioEmail;
import br.gov.jfrj.siga.ex.service.sei.RetornoGeracaoProcedimento;
import br.gov.jfrj.siga.ex.service.sei.RetornoInclusaoDocumento;
import br.gov.jfrj.siga.ex.service.sei.Serie;
import br.gov.jfrj.siga.ex.service.sei.TipoConferencia;
import br.gov.jfrj.siga.ex.service.sei.TipoProcedimento;
import br.gov.jfrj.siga.ex.service.sei.Unidade;
import br.gov.jfrj.siga.ex.service.sei.Usuario;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.hibernate.ExStarter;
import br.gov.jfrj.siga.jee.SoapContext;

@WebService(serviceName = "ExServiceSei", endpointInterface = "br.gov.jfrj.siga.ex.service.impl.ExServiceSei", targetNamespace = "http://impl.sei.service.ex.siga.jfrj.gov.br/")
public class ExServiceSeiImpl implements ExServiceSei {
	private final static Logger log = Logger.getLogger(ExService.class);

	private class ExSoapContext extends SoapContext {
		EntityManager em;
		boolean transacional;
		long inicio = System.currentTimeMillis();

		public ExSoapContext(boolean transacional) {
			super(context, ExStarter.emf, transacional);
		}

		@Override
		public void initDao() {
			ExDao.getInstance();
			try {
				Ex.getInstance().getConf().limparCacheSeNecessario();
			} catch (Exception e1) {
				throw new RuntimeException("Não foi possível atualizar o cache de configurações", e1);
			}
		}
	}

	@Resource
	private WebServiceContext context;

	private ExDao dao() {
		return ExDao.getInstance();
	}

	@Override
	public RetornoGeracaoProcedimento gerarProcedimento(String siglaSistema, String identificacaoServico,
			String idUnidade, Procedimento procedimento, Documento[] documentos, String[] procedimentosRelacionados,
			String[] unidadesEnvio, String sinManterAbertoUnidade, String sinEnviarEmailNotificacao,
			String dataRetornoProgramado, String diasRetornoProgramado, String sinDiasUteisRetornoProgramado,
			String idMarcador, String textoMarcador, String dataControlePrazo, String diasControlePrazo,
			String sinDiasUteisControlePrazo) throws RemoteException {

		return null;
	}

	@Override
	public RetornoInclusaoDocumento incluirDocumento(String siglaSistema, String identificacaoServico, String idUnidade,
			Documento documento) throws RemoteException {

		return null;
	}

	@Override
	public Unidade[] listarUnidades(String siglaSistema, String identificacaoServico, String idTipoProcedimento,
			String idSerie) throws RemoteException {

		return null;
	}

	@Override
	public TipoProcedimento[] listarTiposProcedimento(String siglaSistema, String identificacaoServico,
			String idUnidade, String idSerie) throws RemoteException {

		return null;
	}

	@Override
	public Serie[] listarSeries(String siglaSistema, String identificacaoServico, String idUnidade,
			String idTipoProcedimento) throws RemoteException {

		return null;
	}

	@Override
	public Contato[] listarContatos(String siglaSistema, String identificacaoServico, String idUnidade,
			String idTipoContato, String paginaRegistros, String paginaAtual, String sigla, String nome, String cpf,
			String cnpj, String matricula, String[] idContatos) throws RemoteException {

		return null;
	}

	@Override
	public String atualizarContatos(String siglaSistema, String identificacaoServico, String idUnidade,
			Contato[] contatos) throws RemoteException {

		return null;
	}

	@Override
	public RetornoConsultaProcedimento consultarProcedimento(String siglaSistema, String identificacaoServico,
			String idUnidade, String protocoloProcedimento, String sinRetornarAssuntos, String sinRetornarInteressados,
			String sinRetornarObservacoes, String sinRetornarAndamentoGeracao, String sinRetornarAndamentoConclusao,
			String sinRetornarUltimoAndamento, String sinRetornarUnidadesProcedimentoAberto,
			String sinRetornarProcedimentosRelacionados, String sinRetornarProcedimentosAnexados)
			throws RemoteException {

		return null;
	}

	@Override
	public ProcedimentoResumido consultarProcedimentoIndividual(String siglaSistema, String identificacaoServico,
			String idUnidade, String idOrgaoProcedimento, String idTipoProcedimento, String idOrgaoUsuario,
			String siglaUsuario) throws RemoteException {

		return null;
	}

	@Override
	public RetornoConsultaDocumento consultarDocumento(String siglaSistema, String identificacaoServico,
			String idUnidade, String protocoloDocumento, String sinRetornarAndamentoGeracao,
			String sinRetornarAssinaturas, String sinRetornarPublicacao, String sinRetornarCampos)
			throws RemoteException {

		return null;
	}

	@Override
	public String cancelarDocumento(String siglaSistema, String identificacaoServico, String idUnidade,
			String protocoloDocumento, String motivo) throws RemoteException {

		return null;
	}

	@Override
	public String bloquearDocumento(String siglaSistema, String identificacaoServico, String idUnidade,
			String protocoloDocumento) throws RemoteException {

		return null;
	}

	@Override
	public String gerarBloco(String siglaSistema, String identificacaoServico, String idUnidade, String tipo,
			String descricao, String[] unidadesDisponibilizacao, String[] documentos, String sinDisponibilizar)
			throws RemoteException {

		return null;
	}

	@Override
	public RetornoConsultaBloco consultarBloco(String siglaSistema, String identificacaoServico, String idUnidade,
			String idBloco, String sinRetornarProtocolos) throws RemoteException {

		return null;
	}

	@Override
	public String excluirBloco(String siglaSistema, String identificacaoServico, String idUnidade, String idBloco)
			throws RemoteException {

		return null;
	}

	@Override
	public String excluirProcesso(String siglaSistema, String identificacaoServico, String idUnidade,
			String protocoloProcedimento) throws RemoteException {

		return null;
	}

	@Override
	public String excluirDocumento(String siglaSistema, String identificacaoServico, String idUnidade,
			String protocoloDocumento) throws RemoteException {

		return null;
	}

	@Override
	public String disponibilizarBloco(String siglaSistema, String identificacaoServico, String idUnidade,
			String idBloco) throws RemoteException {

		return null;
	}

	@Override
	public String cancelarDisponibilizacaoBloco(String siglaSistema, String identificacaoServico, String idUnidade,
			String idBloco) throws RemoteException {

		return null;
	}

	@Override
	public String concluirBloco(String siglaSistema, String identificacaoServico, String idUnidade, String idBloco)
			throws RemoteException {

		return null;
	}

	@Override
	public String reabrirBloco(String siglaSistema, String identificacaoServico, String idUnidade, String idBloco)
			throws RemoteException {

		return null;
	}

	@Override
	public String devolverBloco(String siglaSistema, String identificacaoServico, String idUnidade, String idBloco)
			throws RemoteException {

		return null;
	}

	@Override
	public String incluirDocumentoBloco(String siglaSistema, String identificacaoServico, String idUnidade,
			String idBloco, String protocoloDocumento, String anotacao) throws RemoteException {

		return null;
	}

	@Override
	public String retirarDocumentoBloco(String siglaSistema, String identificacaoServico, String idUnidade,
			String idBloco, String protocoloDocumento) throws RemoteException {

		return null;
	}

	@Override
	public String incluirProcessoBloco(String siglaSistema, String identificacaoServico, String idUnidade,
			String idBloco, String protocoloProcedimento, String anotacao) throws RemoteException {

		return null;
	}

	@Override
	public String retirarProcessoBloco(String siglaSistema, String identificacaoServico, String idUnidade,
			String idBloco, String protocoloProcedimento) throws RemoteException {

		return null;
	}

	@Override
	public String reabrirProcesso(String siglaSistema, String identificacaoServico, String idUnidade,
			String protocoloProcedimento) throws RemoteException {

		return null;
	}

	@Override
	public String concluirProcesso(String siglaSistema, String identificacaoServico, String idUnidade,
			String protocoloProcedimento) throws RemoteException {

		return null;
	}

	@Override
	public ArquivoExtensao[] listarExtensoesPermitidas(String siglaSistema, String identificacaoServico,
			String idUnidade, String idArquivoExtensao) throws RemoteException {

		return null;
	}

	@Override
	public String enviarProcesso(String siglaSistema, String identificacaoServico, String idUnidade,
			String protocoloProcedimento, String[] unidadesDestino, String sinManterAbertoUnidade,
			String sinRemoverAnotacao, String sinEnviarEmailNotificacao, String dataRetornoProgramado,
			String diasRetornoProgramado, String sinDiasUteisRetornoProgramado, String sinReabrir)
			throws RemoteException {

		return null;
	}

	@Override
	public Usuario[] listarUsuarios(String siglaSistema, String identificacaoServico, String idUnidade,
			String idUsuario) throws RemoteException {

		return null;
	}

	@Override
	public String atribuirProcesso(String siglaSistema, String identificacaoServico, String idUnidade,
			String protocoloProcedimento, String idUsuario, String sinReabrir) throws RemoteException {

		return null;
	}

	@Override
	public HipoteseLegal[] listarHipotesesLegais(String siglaSistema, String identificacaoServico, String idUnidade,
			String nivelAcesso) throws RemoteException {

		return null;
	}

	@Override
	public TipoConferencia[] listarTiposConferencia(String siglaSistema, String identificacaoServico, String idUnidade)
			throws RemoteException {

		return null;
	}

	@Override
	public Pais[] listarPaises(String siglaSistema, String identificacaoServico, String idUnidade)
			throws RemoteException {

		return null;
	}

	@Override
	public Estado[] listarEstados(String siglaSistema, String identificacaoServico, String idUnidade, String idPais)
			throws RemoteException {

		return null;
	}

	@Override
	public Cidade[] listarCidades(String siglaSistema, String identificacaoServico, String idUnidade, String idPais,
			String idEstado) throws RemoteException {

		return null;
	}

	@Override
	public TipoProcedimento[] listarTiposProcedimentoOuvidoria(String siglaSistema, String identificacaoServico)
			throws RemoteException {

		return null;
	}

	@Override
	public Cargo[] listarCargos(String siglaSistema, String identificacaoServico, String idUnidade, String idCargo)
			throws RemoteException {

		return null;
	}

	@Override
	public String adicionarArquivo(String siglaSistema, String identificacaoServico, String idUnidade, String nome,
			String tamanho, String hash, String conteudo) throws RemoteException {

		return null;
	}

	@Override
	public String adicionarConteudoArquivo(String siglaSistema, String identificacaoServico, String idUnidade,
			String idArquivo, String conteudo) throws RemoteException {

		return null;
	}

	@Override
	public Andamento lancarAndamento(String siglaSistema, String identificacaoServico, String idUnidade,
			String protocoloProcedimento, String idTarefa, String idTarefaModulo, AtributoAndamento[] atributos)
			throws RemoteException {

		return null;
	}

	@Override
	public Andamento[] listarAndamentos(String siglaSistema, String identificacaoServico, String idUnidade,
			String protocoloProcedimento, String sinRetornarAtributos, String[] andamentos, String[] tarefas,
			String[] tarefasModulos) throws RemoteException {

		return null;
	}

	@Override
	public String bloquearProcesso(String siglaSistema, String identificacaoServico, String idUnidade,
			String protocoloProcedimento) throws RemoteException {

		return null;
	}

	@Override
	public String desbloquearProcesso(String siglaSistema, String identificacaoServico, String idUnidade,
			String protocoloProcedimento) throws RemoteException {

		return null;
	}

	@Override
	public String relacionarProcesso(String siglaSistema, String identificacaoServico, String idUnidade,
			String protocoloProcedimento1, String protocoloProcedimento2) throws RemoteException {

		return null;
	}

	@Override
	public String removerRelacionamentoProcesso(String siglaSistema, String identificacaoServico, String idUnidade,
			String protocoloProcedimento1, String protocoloProcedimento2) throws RemoteException {

		return null;
	}

	@Override
	public String sobrestarProcesso(String siglaSistema, String identificacaoServico, String idUnidade,
			String protocoloProcedimento, String protocoloProcedimentoVinculado, String motivo) throws RemoteException {

		return null;
	}

	@Override
	public String removerSobrestamentoProcesso(String siglaSistema, String identificacaoServico, String idUnidade,
			String protocoloProcedimento) throws RemoteException {

		return null;
	}

	@Override
	public String anexarProcesso(String siglaSistema, String identificacaoServico, String idUnidade,
			String protocoloProcedimentoPrincipal, String protocoloProcedimentoAnexado) throws RemoteException {

		return null;
	}

	@Override
	public String desanexarProcesso(String siglaSistema, String identificacaoServico, String idUnidade,
			String protocoloProcedimentoPrincipal, String protocoloProcedimentoAnexado, String motivo)
			throws RemoteException {

		return null;
	}

	@Override
	public Marcador[] listarMarcadoresUnidade(String siglaSistema, String identificacaoServico, String idUnidade)
			throws RemoteException {

		return null;
	}

	@Override
	public String definirMarcador(String siglaSistema, String identificacaoServico, String idUnidade,
			DefinicaoMarcador[] definicoes) throws RemoteException {

		return null;
	}

	@Override
	public String definirControlePrazo(String siglaSistema, String identificacaoServico, String idUnidade,
			DefinicaoControlePrazo[] definicoes) throws RemoteException {

		return null;
	}

	@Override
	public String concluirControlePrazo(String siglaSistema, String identificacaoServico, String idUnidade,
			String[] protocolosProcedimentos) throws RemoteException {

		return null;
	}

	@Override
	public String removerControlePrazo(String siglaSistema, String identificacaoServico, String idUnidade,
			String[] protocolosProcedimentos) throws RemoteException {

		return null;
	}

	@Override
	public AndamentoMarcador[] listarAndamentosMarcadores(String siglaSistema, String identificacaoServico,
			String idUnidade, String protocoloProcedimento, String[] marcadores) throws RemoteException {

		return null;
	}

	@Override
	public RetornoConsultaPublicacao consultarPublicacao(String siglaSistema, String identificacaoServico,
			String idUnidade, String idPublicacao, String idDocumento, String protocoloDocumento,
			String sinRetornarAndamento, String sinRetornarAssinaturas) throws RemoteException {

		return null;
	}

	@Override
	public String agendarPublicacao(String siglaSistema, String identificacaoServico, String idUnidade,
			String idDocumento, String protocoloDocumento, String staMotivo, String idVeiculoPublicacao,
			String dataDisponibilizacao, String resumo, PublicacaoImprensaNacional imprensaNacional)
			throws RemoteException {

		return null;
	}

	@Override
	public String alterarPublicacao(String siglaSistema, String identificacaoServico, String idUnidade,
			String idPublicacao, String idDocumento, String protocoloDocumento, String staMotivo,
			String idVeiculoPublicacao, String dataDisponibilizacao, String resumo,
			PublicacaoImprensaNacional imprensaNacional) throws RemoteException {

		return null;
	}

	@Override
	public String cancelarAgendamentoPublicacao(String siglaSistema, String identificacaoServico, String idUnidade,
			String idPublicacao, String idDocumento, String protocoloDocumento) throws RemoteException {

		return null;
	}

	@Override
	public Feriado[] listarFeriados(String siglaSistema, String identificacaoServico, String idUnidade, String idOrgao,
			String dataInicial, String dataFinal) throws RemoteException {

		return null;
	}

	@Override
	public String confirmarDisponibilizacaoPublicacao(String siglaSistema, String identificacaoServico,
			String idVeiculoPublicacao, String dataDisponibilizacao, String dataPublicacao, String numero,
			String[] idDocumentos) throws RemoteException {

		return null;
	}

	@Override
	public RetornoEnvioEmail enviarEmail(String siglaSistema, String identificacaoServico, String idUnidade,
			String protocoloProcedimento, String de, String para, String CCO, String assunto, String mensagem,
			String[] idDocumentos) throws RemoteException {

		return null;
	}

	@Override
	public ProcedimentoResumido registrarOuvidoria(String siglaSistema, String identificacaoServico, String idOrgao,
			String nome, String nomeSocial, String email, String cpf, String rg, String orgaoExpedidor, String telefone,
			String idEstado, String idCidade, String idTipoProcedimento, String processos, String sinRetorno,
			String mensagem, AtributoOuvidoria[] atributosAdicionais) throws RemoteException {

		return null;
	}

}
