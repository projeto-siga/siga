package br.gov.jfrj.siga.tp.vraptor;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.tp.auth.AutorizacaoGI;
import br.gov.jfrj.siga.tp.model.EstadoMissao;
import br.gov.jfrj.siga.tp.model.EstadoRequisicao;
import br.gov.jfrj.siga.tp.model.EstadoServico;
import br.gov.jfrj.siga.tp.model.ItemMenu;

public class MenuMontador {

	private Result result;
	private AutorizacaoGI autorizacaoGI;

	private MenuMontador(Result result) {
		this.result = result;
	}

	private MenuMontador(Result result, AutorizacaoGI autorizacaoGI) {
        this.result = result;
        this.autorizacaoGI = autorizacaoGI;
    }

	public static MenuMontador instance(Result result) {
		return new MenuMontador(result);
	}

	public static MenuMontador instance(Result result, AutorizacaoGI autorizacaoGI) {
        return new MenuMontador(result, autorizacaoGI);
    }

	public void recuperarMenuVeiculos(Long id, ItemMenu menuVeiculos) {
		result.include("idVeiculo", id);
		result.include("menuVeiculosIncluir", id == 0);
		result.include("menuVeiculosEditar", id != 0 && menuVeiculos != ItemMenu.DADOSCADASTRAIS);
		result.include("menuAvarias", id != 0 && menuVeiculos != ItemMenu.AVARIAS);
		result.include("menuRelatoriosdiarios", id != 0 && menuVeiculos != ItemMenu.RELATORIOSDIARIOS);
		result.include("menuAgenda", id != 0 && menuVeiculos != ItemMenu.AGENDA);
		result.include("menuAbastecimentos", id != 0 && menuVeiculos != ItemMenu.ABASTECIMENTOS);
		result.include("menuAutosdeinfracoes", id != 0 && menuVeiculos != ItemMenu.INFRACOES);
		result.include("menuLotacoes", id != 0 && menuVeiculos != ItemMenu.LOTACOES);
	}


	public void recuperarMenuCondutores(Long id, ItemMenu menuCondutor) {
		result.include("idCondutor", id);
		result.include("menuCondutoresIncluir", id == 0);
		result.include("menuCondutoresEditar", id != 0 && menuCondutor != ItemMenu.DADOSCADASTRAIS);
		result.include("menuPlantoes", id != 0 && menuCondutor != ItemMenu.PLANTOES);
		result.include("menuAfastamentos", id != 0 && menuCondutor != ItemMenu.AFASTAMENTOS);
		result.include("menuEscalasDeTrabalho", id != 0 && menuCondutor != ItemMenu.ESCALASDETRABALHO);
		result.include("menuAgenda", id != 0 && menuCondutor != ItemMenu.AGENDA);
		result.include("menuInfracoes", id != 0 && menuCondutor != ItemMenu.INFRACOES);
	}

	public void recuperarMenuRequisicoes(Long id, boolean popUp, boolean mostrarBotaoRequisicao) {
		result.include("idRequisicao", id);
		result.include("popUp", popUp);
		if(!popUp) {
			result.include("menuRequisicoesIncluir", id == null);
			result.include("menuRequisicoesEditar", id != null);
			result.include("menuRequisicoesCancelar", id != null);
		}
		if(mostrarBotaoRequisicao) {
			result.include("menuRequisicoesMostrarRequisicao", id != null);
		} else {
			result.include("menuRequisicoesListarAndamentos", id != null);
		}

	}

	public void  recuperarMenuMissoes(EstadoMissao estado) {
		result.include("menuMissoesMostrarVoltar", false);
		result.include("menuMissoesMostrarTodas", estado != null);
		result.include("menuMissoesMostrarFinalizadas", estado != EstadoMissao.FINALIZADA);
		result.include("menuMissoesMostrarIniciadas", estado != EstadoMissao.INICIADA);
		result.include("menuMissoesMostrarProgramadas", estado != EstadoMissao.PROGRAMADA);
		result.include("menuMissoesMostrarCanceladas", estado != EstadoMissao.CANCELADA);
		result.include("menuMissoesMostrarAvancado", true);
	}

	public void  recuperarMenuMissoesAvancado() {
		result.include("menuMissoesMostrarVoltar", true);
		result.include("menuMissoesMostrarTodas", false);
		result.include("menuMissoesMostrarFinalizadas", false);
		result.include("menuMissoesMostrarIniciadas", false);
		result.include("menuMissoesMostrarProgramadas", false);
		result.include("menuMissoesMostrarCanceladas", false);
		result.include("menuMissoesMostrarAvancado", false);
	}

	public void  recuperarMenuFinalidades(boolean mostrarBotaoTodas) {
		result.include("menuFinalidadesMostrarVoltar", !mostrarBotaoTodas);
		result.include("menuFinalidadesMostrarTodas", mostrarBotaoTodas);
	}

	public void recuperarMenuListarRequisicoes(EstadoRequisicao estado) {
		recuperarMenuListarRequisicoes(estado,estado);
	}

	public void  recuperarMenuListarPAprovarRequisicoes(EstadoRequisicao estado) {
		result.include("menuRequisicoesMostrarVoltar", false);
		result.include("menuRequisicoesMostrarTodas", estado != null);
		result.include("menuRequisicoesMostrarAbertas", estado != EstadoRequisicao.ABERTA);
		result.include("menuRequisicoesMostrarAutorizadas", estado != EstadoRequisicao.AUTORIZADA);
		result.include("menuRequisicoesMostrarRejeitadas", estado != EstadoRequisicao.REJEITADA);
		result.include("menuRequisicoesMostrarAvancado", true);
	}

	public void  recuperarMenuListarPAprovarRequisicoesAvancado() {
		result.include("menuRequisicoesMostrarVoltar", true);
		result.include("menuRequisicoesMostrarTodas", false);
		result.include("menuRequisicoesMostrarAbertas", false);
		result.include("menuRequisicoesMostrarAutorizadas", false);
		result.include("menuRequisicoesMostrarRejeitadas", false);
		result.include("menuRequisicoesMostrarAvancado", false);
	}
	
	public void  recuperarMenuMissao(Long id, EstadoMissao estado) {
		result.include("idMissao", id);
		result.include("menuMissaoEditar", estado == EstadoMissao.PROGRAMADA || estado == EstadoMissao.INICIADA);
		if (autorizacaoGI.ehAdministrador()) {
			result.include("menuMissaoCancelar", estado == EstadoMissao.PROGRAMADA);
		} else {
			result.include("menuMissaoCancelar", false);
		}
		result.include("menuMissaoIniciar", estado == EstadoMissao.PROGRAMADA);
		result.include("menuMissaoFinalizar", estado == EstadoMissao.INICIADA);
	}

	public void recuperarMenuServicoVeiculo(Long id, EstadoServico estado) {
		result.include("idServico", id);
		result.include("menuServicoVeiculoEditar", estado == EstadoServico.AGENDADO || estado == EstadoServico.INICIADO);
		result.include("menuServicoVeiculoExcluir", estado == EstadoServico.AGENDADO);
	}

	public void  recuperarMenuServicosVeiculo(EstadoServico estado) {
		result.include("menuServicosVeiculoMostrarTodos", estado != null);
		result.include("menuServicosVeiculoMostrarRealizados", estado != EstadoServico.REALIZADO);
		result.include("menuServicosVeiculoMostrarIniciados", estado != EstadoServico.INICIADO);
		result.include("menuServicosVeiculoMostrarAgendados", estado != EstadoServico.AGENDADO);
		result.include("menuServicosVeiculoMostrarCancelados", estado != EstadoServico.CANCELADO);
	}

	public void recuperarMenuListarRequisicoes(EstadoRequisicao estadoRequisicao, EstadoRequisicao estadoRequisicaoP) {
		result.include("menuRequisicoesMostrarVoltar", false);
		result.include("menuRequisicoesMostrarTodas", estadoRequisicao != null && estadoRequisicaoP!= null );
		result.include("menuRequisicoesMostrarAutorizadasENaoAtendidas", estadoRequisicao != EstadoRequisicao.AUTORIZADA || estadoRequisicaoP != EstadoRequisicao.NAOATENDIDA);
		result.include("menuRequisicoesMostrarAbertas", estadoRequisicao != EstadoRequisicao.ABERTA && estadoRequisicaoP != EstadoRequisicao.ABERTA);
		result.include("menuRequisicoesMostrarAutorizadas", estadoRequisicao != EstadoRequisicao.AUTORIZADA && estadoRequisicaoP != EstadoRequisicao.AUTORIZADA || estadoRequisicao == EstadoRequisicao.AUTORIZADA && estadoRequisicaoP == EstadoRequisicao.NAOATENDIDA);
		result.include("menuRequisicoesMostrarRejeitadas", estadoRequisicao != EstadoRequisicao.REJEITADA && estadoRequisicaoP != EstadoRequisicao.REJEITADA);
		result.include("menuRequisicoesMostrarProgramadas", estadoRequisicao != EstadoRequisicao.PROGRAMADA && estadoRequisicaoP != EstadoRequisicao.PROGRAMADA);
		result.include("menuRequisicoesMostrarEmAtendimento", estadoRequisicao != EstadoRequisicao.EMATENDIMENTO && estadoRequisicaoP != EstadoRequisicao.EMATENDIMENTO);
		result.include("menuRequisicoesMostrarAtendidas", estadoRequisicao != EstadoRequisicao.ATENDIDA && estadoRequisicaoP != EstadoRequisicao.ATENDIDA);
		result.include("menuRequisicoesMostrarNaoAtendidas", estadoRequisicao != EstadoRequisicao.NAOATENDIDA && estadoRequisicaoP != EstadoRequisicao.NAOATENDIDA || estadoRequisicao == EstadoRequisicao.AUTORIZADA && estadoRequisicaoP == EstadoRequisicao.NAOATENDIDA);
		result.include("menuRequisicoesMostrarCanceladas", estadoRequisicao != EstadoRequisicao.CANCELADA && estadoRequisicaoP != EstadoRequisicao.CANCELADA);
		result.include("menuRequisicoesMostrarAvancado", true);
	}

	public void recuperarMenuListarRequisicoesAvancado() {
		result.include("menuRequisicoesMostrarVoltar", true);
		result.include("menuRequisicoesMostrarTodas", false);
		result.include("menuRequisicoesMostrarAutorizadasENaoAtendidas", false);
		result.include("menuRequisicoesMostrarAbertas", false);
		result.include("menuRequisicoesMostrarAutorizadas", false);
		result.include("menuRequisicoesMostrarRejeitadas", false);
		result.include("menuRequisicoesMostrarProgramadas", false);
		result.include("menuRequisicoesMostrarEmAtendimento", false);
		result.include("menuRequisicoesMostrarAtendidas", false);
		result.include("menuRequisicoesMostrarNaoAtendidas", false);
		result.include("menuRequisicoesMostrarCanceladas", false);
		result.include("menuRequisicoesMostrarAvancado", false);
	}

	public void recuperarMenuAbastecimentos() {
		result.include("menuAbastecimentosMostrarVoltar", false);
		result.include("menuAbastecimentosMostrarAvancado", true);
	}
	
	public void RecuperarMenuAbastecimentosAvancado() {
		result.include("menuAbastecimentosMostrarVoltar", true);
		result.include("menuAbastecimentosMostrarAvancado", false);
	}
}
