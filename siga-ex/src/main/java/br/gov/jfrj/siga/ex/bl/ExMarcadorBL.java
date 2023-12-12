package br.gov.jfrj.siga.ex.bl;

import java.util.*;

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.cp.CpTipoMarcadorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorFinalidadeEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorTipoExibicaoEnum;
import br.gov.jfrj.siga.cp.model.enm.ITipoDeMovimentacao;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.ExTemporalidade;
import br.gov.jfrj.siga.ex.ExTipoDestinacao;
import br.gov.jfrj.siga.ex.bl.ExTramiteBL.Pendencias;
import br.gov.jfrj.siga.ex.logic.ExEAssinanteAtual;
import br.gov.jfrj.siga.ex.logic.ExEstaOrdenadoAssinatura;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeConfiguracao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.parser.PessoaLotacaoParser;

public class ExMarcadorBL {
	private ExMobil mob;
	private SortedSet<ExMarca> set;
	private SortedSet<ExMovimentacao> movs;

	public ExMarcadorBL(SortedSet<ExMarca> set, ExMobil mob) {
		this.mob = mob;
		this.set = set;

		movs = new TreeSet<>();
		for (ExMovimentacao mov : mob.getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			movs.add(mov);
		}
	}

	private SortedSet<ExMovimentacao> movs(ITipoDeMovimentacao... tipoMov) {
		SortedSet<ExMovimentacao> ss = new TreeSet<>();
		for (ExMovimentacao mov : mob.getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			for (int j = 0; j < tipoMov.length; j++)
				if (tipoMov[j] == mov.getExTipoMovimentacao())
					ss.add(mov);
		}
		return ss;
	}

	public void calcular(boolean apenasTemporalidade) {
		if (apenasTemporalidade) {
			acrescentarMarcadoresTemporalidade();
		} else {
			acrescentarMarcadores();
		}
	}

	/**
	 * Calcula quais as marcas cada mobil terá com base nas movimentações que foram
	 * feitas no documento.
	 * 
	 * @param mob
	 */
	private void acrescentarMarcadores() {
		acrescentarMarcadoresTemporalidade();

		if (mob.isGeral()) {
			calcularMarcasGeral();
			return;
		}

		if (acrescentarMarcadorCancelado())
			return;

		if (mob.doc().isSemEfeito()) {
			return;
		}

		// Nato: isso não faz muito sentido. REMOVER!
		if (mob.doc().getMobilGeral().isPendenteDeColaboracao()) {
			return;
		}

		boolean apensadoAVolumeDoMesmoProcesso = mob.isApensadoAVolumeDoMesmoProcesso();

		// converter as linhas abaixo em um único método que identifica a
		// movimentação principal, o marcador e a data.

		long m = 0L;
		long mAnterior = m;
		Date dt = null;

		for (ExMovimentacao mov : movs) {
			ITipoDeMovimentacao t = mov.getExTipoMovimentacao();
			if (t == ExTipoDeMovimentacao.CANCELAMENTO_DE_MOVIMENTACAO
					&& mov.getExMovimentacaoRef() != null && mov.getExMovimentacaoRef().getExTipoMovimentacao()
							 == ExTipoDeMovimentacao.CRIACAO)
				m = CpMarcadorEnum.CANCELADO.getId();
			if (t == ExTipoDeMovimentacao.PENDENCIA_DE_ANEXACAO)
				m = CpMarcadorEnum.PENDENTE_DE_ANEXACAO.getId();
			if (t == ExTipoDeMovimentacao.PEDIDO_PUBLICACAO)
				m = CpMarcadorEnum.PUBLICACAO_SOLICITADA.getId();
			if (t == ExTipoDeMovimentacao.DISPONIBILIZACAO)
				m = CpMarcadorEnum.DISPONIBILIZADO.getId();
			if (t == ExTipoDeMovimentacao.AGENDAMENTO_DE_PUBLICACAO)
				m = CpMarcadorEnum.REMETIDO_PARA_PUBLICACAO.getId();
			if (t == ExTipoDeMovimentacao.SOBRESTAR)
				m = CpMarcadorEnum.SOBRESTADO.getId();
			if (t == ExTipoDeMovimentacao.JUNTADA)
				m = CpMarcadorEnum.JUNTADO.getId();
			if (t == ExTipoDeMovimentacao.JUNTADA_EXTERNO)
				m = CpMarcadorEnum.JUNTADO_EXTERNO.getId();
			if (t == ExTipoDeMovimentacao.APENSACAO && apensadoAVolumeDoMesmoProcesso)
				m = CpMarcadorEnum.APENSADO.getId();
			if (t == ExTipoDeMovimentacao.TRANSFERENCIA_EXTERNA || t == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA_EXTERNA) {
				m = CpMarcadorEnum.TRANSFERIDO_A_ORGAO_EXTERNO.getId();
				// Quando é transferido para um órgão externo, a marca deve ficar
				// com o cadastrante e sua lotação, em vez do responsável
				acrescentarMarca(m, dt, mov.getCadastrante(), mov.getLotaCadastrante());
			}
//			if ((t == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA
//					|| t == ExTipoDeMovimentacao.TRANSFERENCIA) && !apensadoAVolumeDoMesmoProcesso) {
//				m = CpMarcadorEnum.CAIXA_DE_ENTRADA.getId();
//			}
			if (t == ExTipoDeMovimentacao.DESPACHO && mob.doc().isEletronico() && !mov.isAssinada()) {
				m = CpMarcadorEnum.DESPACHO_PENDENTE_DE_ASSINATURA.getId();
			}

			if (t == ExTipoDeMovimentacao.CRIACAO
					|| t == ExTipoDeMovimentacao.RECEBIMENTO
					|| t == ExTipoDeMovimentacao.DESOBRESTAR
					|| t == ExTipoDeMovimentacao.ASSINATURA_DIGITAL_DOCUMENTO
					|| t == ExTipoDeMovimentacao.ASSINATURA_COM_SENHA
					|| t == ExTipoDeMovimentacao.CANCELAMENTO_JUNTADA
					|| t == ExTipoDeMovimentacao.DESAPENSACAO) {
				m = 0L;
			}

			if (t == ExTipoDeMovimentacao.ANEXACAO && mob.doc().isEletronico() && !mov.isAssinada()) {
				m = CpMarcadorEnum.ANEXO_PENDENTE_DE_ASSINATURA.getId();
			}

			if (t == ExTipoDeMovimentacao.ASSINATURA_MOVIMENTACAO_COM_SENHA) {
				acrescentarMarca(CpMarcadorEnum.MOVIMENTACAO_ASSINADA_COM_SENHA.getId(), dt, mov.getSubscritor(), null);
			}

			if (t == ExTipoDeMovimentacao.CONFERENCIA_COPIA_COM_SENHA) {
				acrescentarMarca(CpMarcadorEnum.MOVIMENTACAO_CONFERIDA_COM_SENHA.getId(), dt, mov.getSubscritor(),
						null);
			}

			if (m != mAnterior) {
				dt = mov.getDtIniMov();
				mAnterior = m;
			}
		}

		if (!apensadoAVolumeDoMesmoProcesso && !mob.doc().isPendenteDeAssinatura() && !mob.isJuntado()
				&& !mob.isEliminado() && !mob.isEmTransitoExterno() && !mob.isArquivado() && !mob.isSobrestado()) {
			calcularMarcadoresDeTramite();
		}
		calcularMarcadoresDeNotificacao();

		if (!mob.isArquivado())
			calcularMarcadoresTransferenciaComData(dt);

		acrescentarMarcadoresManuais();

		if (m == CpMarcadorEnum.JUNTADO.getId() || m == CpMarcadorEnum.APENSADO.getId()) {
			if (!mob.isEliminado())
				acrescentarMarca(m, dt, null, null);
		} else {
			for (PessoaLotacaoParser pl : mob.getAtendente()) {
				if (m != 0L) {
					// Edson: Os marcadores "Arq Corrente" e
					// "Aguardando andamento" são mutuamente exclusivos
					if (m != CpMarcadorEnum.EM_ANDAMENTO.getId()
							|| !(mob.isArquivado() || mob.doc().getMobilGeral().isArquivado()))
						acrescentarMarca(m, dt, pl.getPessoa(), pl.getLotacao());

					// Nato: acredito que isso não seja mais necessário agora que temos a nova
					// implementação de marcadores.
//			if (SigaMessages.isSigaSP() && m == CpMarcadorEnum.CAIXA_DE_ENTRADA.getId()
//					&& ultMovNaoCanc.getIdTpMov() == ExTipoDeMovimentacao.TRANSFERENCIA) {
//				acrescentarMarcadoresManuaisPorOcorrenciaDeTransferencia(dt);
//			}
				}
			}
		}

		return;
	}

	private void acrescentarMarcadoresManuais() {
		// Acrescentar marcas manuais (Urgente, Idoso, etc)
		ExMobil geral = mob.doc().getMobilGeral();

		// Só produzir marcas no último volume de processos administrativos
		if (mob.doc().isProcesso() && !mob.isUltimoVolume())
			return;
		
		if(mob.isJuntado())
			return;

		// Conteplar movimentações gerais e também as da via específica
		List<ExMovimentacao> marcacoes = new ArrayList<>();
		marcacoes.addAll(geral.getMovimentacoesPorTipo(ExTipoDeMovimentacao.MARCACAO, true));
		if (mob.isVia())
			marcacoes.addAll(mob.getMovimentacoesPorTipo(ExTipoDeMovimentacao.MARCACAO, true));

		Set<PessoaLotacaoParser> atendentes = mob.getAtendente();
		Set<DpLotacao> lotacoesComMarcaDePasta = new TreeSet<>();

		// Marcações gerais
		for (ExMovimentacao mov : marcacoes) {
			CpMarcador marcador = mov.getMarcador();

			// Aplicar apenas no móbil correto
			if (marcador.isAplicacaoGeral() && !mob.isGeral())
				continue;
			if (marcador.isAplicacaoGeralOuViaEspecificaOuUltimoVolume() && (mob.isGeral() || mob.isVolumeEncerrado()))
				continue;
			if (marcador.isAplicacaoGeralOuTodasAsViasOuUltimoVolume()
					&& ((mob.isGeral() && mob.doc().isFinalizado()) || mob.isVolumeEncerrado()))
				continue;

			// Aplicar marcas de lotação apenas se o atendente for a lotação
//			if (marcador.getIdFinalidade().getIdTpMarcador() == CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO
//					&& marcador.getIdFinalidade().getIdTpInteressado() == CpMarcadorTipoInteressadoEnum.ATENDENTE
//					&& marcador.getDpLotacaoIni() != null) {
//				if (!mob.isAtendente(null, marcador.getDpLotacaoIni()))
//					continue;
//			}

//			// Calcular datas de referencia
//			Date dtRef1 = mov.getDtParam1();
//			Date dtRef2 = mov.getDtParam2();
//			
			// Calcular datas de início e fim
			Date dtIni = null;
			Date dtFim = null;
			if (dtIni == null && mov.getDtParam1() != null && mov.getDtParam2() != null
					&& marcador.getIdFinalidade().getIdTpExibicao() == CpMarcadorTipoExibicaoEnum.MENOR_DATA)
				dtIni = mov.getDtParam1().before(mov.getDtParam2()) ? mov.getDtParam1() : mov.getDtParam2();
			if (dtIni == null && mov.getDtParam1() != null
					&& marcador.getIdFinalidade().getIdTpExibicao() == CpMarcadorTipoExibicaoEnum.DATA_PLANEJADA) {
				dtIni = mov.getDtParam1();
			}

			     
			if (dtIni == null && mov.getDtParam2() != null
					&& marcador.getIdFinalidade().getIdTpExibicao() == CpMarcadorTipoExibicaoEnum.DATA_LIMITE)
				dtIni = mov.getDtParam2();
			if (dtIni == null)
				dtIni = mov.getDtIniMov();

			// Se a finalidade especificar que não é para incluir a marca quando está
			// arquivado
			if (marcador.getIdFinalidade().isArquivarOcultaAMarca() && mob.isArquivado())
				continue;

			// Calcular pessoa ou lotação
			DpPessoa pes = null;
			DpLotacao lot = null;
			if (marcador.isInteressadoAtentende()) {
				for (PessoaLotacaoParser atendente : atendentes) {
					pes = atendente.getPessoa();
					lot = atendente.getLotacao();

					// Pular se for marcador de outra lotação
					if (marcador.getIdFinalidade().getIdTpMarcador() == CpTipoMarcadorEnum.TIPO_MARCADOR_LOTACAO
							&& (atendente.getLotacao() == null || !Utils.equivale(marcador.getDpLotacaoIni(), lot)))
						continue;

					// Armazenar os atendentes que já tem marcação de pasta para não receberem a
					// pasta padrão
					if (marcador.getIdFinalidade() == CpMarcadorFinalidadeEnum.PASTA
							|| marcador.getIdFinalidade() == CpMarcadorFinalidadeEnum.PASTA_PADRAO)
						lotacoesComMarcaDePasta.add(atendente.getLotacao());

					acrescentarMarcaTransferencia(marcador.getIdMarcador(), dtIni, dtFim, pes, lot, mov);
				}
			} else if (marcador.isInteressadoPessoa() && mov.getSubscritor() != null) {
				pes = mov.getSubscritor();
				acrescentarMarcaTransferencia(marcador.getIdMarcador(), dtIni, dtFim, pes, lot, mov);
			} else if (marcador.isInteressadoLotacao() && mov.getLotaSubscritor() != null) {
				lot = mov.getLotaSubscritor();
				acrescentarMarcaTransferencia(marcador.getIdMarcador(), dtIni, dtFim, pes, lot, mov);
			}
		}

		// Marcar com a pasta padrão
		if (!mob.isGeral())
			for (PessoaLotacaoParser atendente : atendentes) {
				DpLotacao lot = atendente.getLotacao();

				if (lotacoesComMarcaDePasta.contains(lot))
					continue;
				
				if (lot != null) {
					CpMarcador mpp = ExDao.getInstance().obterPastaPadraoDaLotacao(lot);
					if (mpp != null) {
		                // Se a finalidade especificar que não é para incluir a marca quando está
		                // arquivado
		                if (mpp.getIdFinalidade().isArquivarOcultaAMarca() && mob.isArquivado())
		                    continue;

						acrescentarMarcaTransferencia(mpp.getId(), null, null, null, lot, null);
					}
				}
			}

	}

	protected boolean acrescentarMarcadorCancelado() {
		if (mob.isCancelada()) {
			ExMovimentacao ultimaMovimentacao = mob.getUltimaMovimentacao();
			acrescentarMarca(CpMarcadorEnum.CANCELADO.getId(), ultimaMovimentacao != null ? ultimaMovimentacao.getDtIniMov() : null, mob.doc().getCadastrante(),
					mob.doc().getLotaCadastrante());
			return true;
		}
		return false;
	}

	protected void calcularMarcasGeral() {
		acrescentarMarcadoresElaboracao();

		// Todas as outras só fazem sentido quando existem movimentações
		if (mob.getExMovimentacaoSet() == null)
			return;

		if (acrescentarMarcadoresSemEfeito())
			return;

		acrescentarMarcadoresColaboracao();
		acrescentarMarcadoresPapel();
		acrescentarMarcadoresDJe();
		acrescentarMarcadoresPendenciaDeAnexacao();
		acrescentarMarcadoresPendenciaDeAssinatura();
		acrescentarMarcadoresPendenciaDeAssinaturaMovimentacao();
		acrescentarMarcadoresDoCossignatario();
		acrescentarMarcadoresAssinaturaComSenha();
		acrescentarMarcadorPublicacaoPortalTransparencia();
		acrescentarMarcadoresManuais();
	}

	protected boolean acrescentarMarcadoresSemEfeito() {
		// Sem efeito
		for (ExMovimentacao mov : movs(ExTipoDeMovimentacao.TORNAR_SEM_EFEITO)) {
			acrescentarMarca(CpMarcadorEnum.SEM_EFEITO.getId(), mov.getDtIniMov(), mov.getCadastrante(),
					mov.getLotaCadastrante());
			return true;
		}
		return false;
	}

	protected void acrescentarMarcadoresElaboracao() {
		// Se não estiver finalizado
		if (!mob.doc().isFinalizado()) {
			acrescentarMarca(CpMarcadorEnum.EM_ELABORACAO.getId(), mob.doc().getDtRegDoc(), mob.doc().getCadastrante(),
					(Ex.getInstance().getConf().podePorConfiguracao(mob.doc().getCadastrante(),
							mob.doc().getLotaCadastrante(), null, mob.doc().getExModelo().getExFormaDocumento(), null,
							ExTipoDeConfiguracao.TMP_PARA_LOTACAO) ? mob.doc().getLotaCadastrante() : null));
			if (mob.getExDocumento().getSubscritor() != null && !(Prop.getBool("/siga.mesa.nao.revisar.temporarios")
					&& (mob.doc().getCadastrante() == null || !mob.doc().getCadastrante().equals(mob.doc().getSubscritor())) && !mob.doc().isFinalizado())) {
				acrescentarMarca(CpMarcadorEnum.REVISAR.getId(), mob.doc().getDtRegDoc(),
						mob.getExDocumento().getSubscritor(), null);
			}
		}
	}

	public void acrescentarMarcadoresColaboracao() {
		boolean fDocColaborativoPendencia = false;
		Date dtDocColaborativo = null;
		for (ExMovimentacao mov : movs(ExTipoDeMovimentacao.CONTROLE_DE_COLABORACAO)) {
			dtDocColaborativo = mov.getDtIniMov();
			ExParte parte = ExParte.create(mov.getDescrMov());
			if (parte.isAtivo() && !parte.isPreenchido()) {
				fDocColaborativoPendencia = true;
				acrescentarMarca(CpMarcadorEnum.PENDENTE_DE_COLABORACAO.getId(), mov.getDtIniMov(), mov.getSubscritor(),
						mov.getLotaSubscritor());
			}
		}
		if (dtDocColaborativo != null && !fDocColaborativoPendencia) {
			acrescentarMarca(CpMarcadorEnum.FINALIZAR_DOCUMENTO_COLABORATIVO.getId(), dtDocColaborativo,
					mob.doc().getCadastrante(), mob.doc().getLotaCadastrante());
		}
	}

	public void acrescentarMarcadoresPapel() {
		for (ExMovimentacao mov : movs(ExTipoDeMovimentacao.VINCULACAO_PAPEL)) {
			Long m = null;
			DpLotacao lotaPerfil = null;
			switch ((int) (long) mov.getExPapel().getIdPapel()) {
			case (int) ExPapel.PAPEL_GESTOR:
				m = CpMarcadorEnum.COMO_GESTOR.getId();
				break;
			case (int) ExPapel.PAPEL_REVISOR:
				m = CpMarcadorEnum.COMO_REVISOR.getId();
				break;
			case (int) ExPapel.PAPEL_INTERESSADO:
				m = CpMarcadorEnum.COMO_INTERESSADO.getId();
				break;
			}
			if (m != null && !mob.doc().isEliminado() && !mob.doc().isArquivadoPermanente()) {
				// Se o perfil foi cadastrado para uma pessoa
				if (mov.getSubscritor() != null)
					lotaPerfil = null;
				else
					lotaPerfil = mov.getLotaSubscritor();
				acrescentarMarca(m, mov.getDtIniMov(), mov.getSubscritor(), lotaPerfil);
			}
		}
	}

	public void acrescentarMarcadoresDJe() {
		Long mDje = null;
		ExMovimentacao movDje = null;

		for (ExMovimentacao mov : movs) {
			ITipoDeMovimentacao t = mov.getExTipoMovimentacao();
			if (t == ExTipoDeMovimentacao.PEDIDO_PUBLICACAO) {
				mDje = CpMarcadorEnum.PUBLICACAO_SOLICITADA.getId();
				movDje = mov;
			} else if (t == ExTipoDeMovimentacao.AGENDAMENTO_DE_PUBLICACAO) {
				mDje = CpMarcadorEnum.REMETIDO_PARA_PUBLICACAO.getId();
				movDje = mov;
			} else if (t == ExTipoDeMovimentacao.DISPONIBILIZACAO) {
				mDje = CpMarcadorEnum.DISPONIBILIZADO.getId();
				movDje = mov;
			}
		}
		if (mDje != null && !mob.doc().isEliminado()) {
			acrescentarMarca(mDje, movDje.getDtIniMov(), movDje.getTitular(), movDje.getLotaTitular());
		}
	}

	public void acrescentarMarcadoresPendenciaDeAnexacao() {
		for (ExMovimentacao mov : movs(ExTipoDeMovimentacao.PENDENCIA_DE_ANEXACAO)) {
			acrescentarMarca(CpMarcadorEnum.PENDENTE_DE_ANEXACAO.getId(), mov.getDtIniMov(), mov.getCadastrante(),
					mov.getLotaCadastrante());
		}
	}

	public void acrescentarMarcadoresPendenciaDeAssinatura() {
		if (!(SigaMessages.isSigaSP() && !mob.doc().isFinalizado()) && mob.doc().isPendenteDeAssinatura()
				&& !mob.doc().isCancelado()) {
			/*
			 * Não estava setando a amrca pendente de assinatura corretamente na
			 * susbituição. DpPessoa resp = ultMovNaoCanc != null ? ultMovNaoCanc.getResp()
			 * : mob.doc().getCadastrante(); DpLotacao lotaResp = ultMovNaoCanc != null ?
			 * ultMovNaoCanc.getLotaResp() : mob.doc().getLotaCadastrante();
			 */
			ExMovimentacao movPrazo = mob.doc().getMovPrazoDeAssinatura();
			Date dtPrazo = null;
			if (movPrazo != null) {
				dtPrazo = movPrazo.getDtParam1();
				acrescentarMarca(CpMarcadorEnum.PRAZO_DE_ASSINATURA_EXPIRADO.getId(), dtPrazo,
						mob.doc().getCadastrante(), mob.doc().getLotaCadastrante(), null);
			}
			if (!mob.getDoc().isPrazoDeAssinaturaVencido()) {
				acrescentarMarca(CpMarcadorEnum.PENDENTE_DE_ASSINATURA.getId(), mob.doc().getDtRegDoc(),
						mob.doc().getCadastrante(), mob.doc().getLotaCadastrante(), dtPrazo);
				if (!mob.getDoc().isAssinadoPeloSubscritorComTokenOuSenha()
						&& (!new ExEstaOrdenadoAssinatura(mob.getDoc()).eval() || new ExEAssinanteAtual(mob.getDoc(), mob.getDoc().getSubscritor()).eval())
						&& !(Prop.getBool("/siga.mesa.nao.revisar.temporarios")
								&& (mob.doc().getCadastrante() == null || !mob.doc().getCadastrante().equals(mob.doc().getSubscritor()))
								&& !mob.doc().isFinalizado())) {
					acrescentarMarca(CpMarcadorEnum.COMO_SUBSCRITOR.getId(), mob.doc().getDtRegDoc(),
							mob.getExDocumento().getSubscritor(), null, dtPrazo);
					ExMovimentacao m = mob.doc().getMovSolicitacaoDeAssinatura();
					if (m != null) {
						acrescentarMarca(CpMarcadorEnum.PRONTO_PARA_ASSINAR.getId(), m.getDtIniMov(),
								mob.getExDocumento().getSubscritor(), null, dtPrazo);
					}
				}
			}
		}
	}

	public void acrescentarMarcadoresPendenciaDeAssinaturaMovimentacao() {
		for (ExMovimentacao mov : movs(ExTipoDeMovimentacao.ANEXACAO)) {
			Long m = null;
			if (mob.doc().isEletronico()) {
				m = CpMarcadorEnum.ANEXO_PENDENTE_DE_ASSINATURA.getId();
				/*
				 * não é possível usar ExMovimentacao.isAssinada() pois não há tempo habil no BD
				 * de efetivar a inclusao de movimentacao de assinatura de movimentção Edson:
				 * Por que não?
				 */
				for (ExMovimentacao movAss : mob.getExMovimentacaoSet()) {
					if ((movAss.getExTipoMovimentacao()
							 == ExTipoDeMovimentacao.ASSINATURA_DIGITAL_MOVIMENTACAO
							|| movAss.getExTipoMovimentacao()
									 == ExTipoDeMovimentacao.CONFERENCIA_COPIA_DOCUMENTO
							|| movAss.getExTipoMovimentacao()
									 == ExTipoDeMovimentacao.ASSINATURA_MOVIMENTACAO_COM_SENHA
							|| movAss.getExTipoMovimentacao()
									 == ExTipoDeMovimentacao.CONFERENCIA_COPIA_COM_SENHA)
							&& movAss.getExMovimentacaoRef().getIdMov() == mov.getIdMov()) {
						m = null;
						break;
					}
				}
				if (m != null)
					acrescentarMarca(m, mov.getDtIniMov(), mov.getCadastrante(), mov.getLotaCadastrante());
			}
		}
	}

	public void acrescentarMarcadoresDoCossignatario() {
		if (!mob.doc().isInternoProduzido() && !mob.doc().isInternoCapturado())
			return;

		for (ExMovimentacao mov : movs(ExTipoDeMovimentacao.INCLUSAO_DE_COSIGNATARIO)) {
			if (mob.getDoc().isEletronico()) {
				ExMovimentacao movPrazo = mob.doc().getMovPrazoDeAssinatura();
				Date dtPrazo = null;
				if (movPrazo != null)
					dtPrazo = movPrazo.getDtParam1();
				if (mob.getDoc().isAssinadoPelaPessoaComTokenOuSenha(mov.getSubscritor())
						|| mob.getDoc().isPrazoDeAssinaturaVencido())
					continue;
				else if ((mob.getDoc().isAssinadoPeloSubscritorComTokenOuSenha() && !new ExEstaOrdenadoAssinatura(mob.getDoc()).eval())
						|| (new ExEstaOrdenadoAssinatura(mob.getDoc()).eval() && new ExEAssinanteAtual(mob.getDoc(), mov.getSubscritor()).eval())) {
					acrescentarMarca(CpMarcadorEnum.COMO_SUBSCRITOR.getId(), mov.getDtIniMov(), mov.getSubscritor(),
							null, dtPrazo);
				} else {
					if (!(Prop.getBool("/siga.mesa.nao.revisar.temporarios") && !mob.getDoc().isFinalizado()))
						acrescentarMarca(CpMarcadorEnum.REVISAR.getId(), mov.getDtIniMov(), mov.getSubscritor(), null,
								dtPrazo);
					if (!(Prop.getBool("/siga.mesa.nao.revisar.temporarios") && !mob.getDoc().isFinalizado())
							&& Ex.getInstance().getConf().podePorConfiguracao(mov.getSubscritor(),
									mov.getSubscritor().getLotacao(),
									ExTipoDeConfiguracao.COSIGNATARIO_ASSINAR_ANTES_SUBSCRITOR))
						acrescentarMarca(CpMarcadorEnum.COMO_SUBSCRITOR.getId(), mov.getDtIniMov(), mov.getSubscritor(),
								null, dtPrazo);
				}
			}
		}
	}

	// Nato: acredito que isso não seja mais necessário agora que temos a nova
	// implementação de marcadores.
//	public void acrescentarMarcadoresManuaisPorOcorrenciaDeTransferencia(Date dt) {
//		ExMobil geral = mob.doc().getMobilGeral();
//		if (geral.getExMovimentacaoSet() != null) {
//			for (ExMovimentacao mov : geral.getExMovimentacaoSet()) {
//				if (mov.isCancelada() || mov.getMarcador() == null)
//					continue;
//
//				Long tpMov = mov.getIdTpMov();
//				Long idMarcador = mov.getMarcador().getIdMarcador();
//				boolean temMarcaManual = (tpMov == ExTipoDeMovimentacao.MARCACAO
//						&& (idMarcador == CpMarcadorEnum.URGENTE.getId() || idMarcador == CpMarcadorEnum.IDOSO.getId()
//								|| idMarcador == CpMarcadorEnum.PRIORITARIO.getId()
//								|| idMarcador == CpMarcadorEnum.RESTRICAO_ACESSO.getId()
//								|| idMarcador == CpMarcadorEnum.COVID_19.getId()
//								|| idMarcador == CpMarcadorEnum.NOTA_EMPENHO.getId()
//								|| idMarcador == CpMarcadorEnum.DEMANDA_JUDICIAL_BAIXA.getId()
//								|| idMarcador == CpMarcadorEnum.DEMANDA_JUDICIAL_MEDIA.getId()
//								|| idMarcador == CpMarcadorEnum.DEMANDA_JUDICIAL_ALTA.getId()));
//
//				if (temMarcaManual) {
//					acrescentarMarca(mov.getMarcador().getIdMarcador(), dt, ultMovNaoCanc.getResp(),
//							ultMovNaoCanc.getLotaResp());
//				}
//			}
//		}
//	}

	public void acrescentarMarcadoresAssinaturaComSenha() {
		for (ExMovimentacao mov : movs(ExTipoDeMovimentacao.ASSINATURA_COM_SENHA)) {
			ITipoDeMovimentacao t = mov.getExTipoMovimentacao();

			boolean jaAutenticado = false;
			for (ExMovimentacao movAss : mob.getExMovimentacaoSet()) {
				if (movAss.getExTipoMovimentacao()
						== ExTipoDeMovimentacao.ASSINATURA_DIGITAL_DOCUMENTO
						|| movAss.getExTipoMovimentacao()
								== ExTipoDeMovimentacao.CONFERENCIA_COPIA_DOCUMENTO) {
					jaAutenticado = true;
					break;
				}
			}

			if (!jaAutenticado)
				acrescentarMarca(CpMarcadorEnum.DOCUMENTO_ASSINADO_COM_SENHA.getId(), mov.getDtIniMov(),
						mov.getSubscritor(), null);
		}
	}

	public void acrescentarMarcadorPublicacaoPortalTransparencia() {
		for (ExMovimentacao mov : movs(ExTipoDeMovimentacao.PUBLICACAO_PORTAL_TRANSPARENCIA)) {
			acrescentarMarca(CpMarcadorEnum.PORTAL_TRANSPARENCIA.getId(), mov.getDtIniMov(), mov.getCadastrante(),
					mov.getLotaCadastrante());
		}
	}

	public void calcularMarcadoresDeTramite() {
		Pendencias p = mob.calcularTramitesPendentes();
		
		Set<ExMovimentacao> enviados = new TreeSet<>();
		enviados.addAll(p.tramitesPendentes);
		enviados.removeAll(p.tramitesDeNotificacoesPendentes);
		for (ExMovimentacao tramite : enviados) {
			acrescentarMarcaTransferencia(
					mob.doc().isEletronico() ? CpMarcadorEnum.EM_TRANSITO_ELETRONICO.getId()
							: CpMarcadorEnum.EM_TRANSITO.getId(),
					tramite.getDtIniMov(), null, 
					(tramite.getTitular() != null? tramite.getTitular() : tramite.getCadastrante()),
					(tramite.getLotaTitular() != null ? tramite.getLotaTitular() : tramite.getLotaCadastrante()), 
					null);
			acrescentarMarcaTransferencia(
					mob.doc().isEletronico() ? CpMarcadorEnum.CAIXA_DE_ENTRADA.getId()
							: CpMarcadorEnum.A_RECEBER.getId(),
					tramite.getDtIniMov(), null, tramite.getResp(), tramite.getLotaResp(), null);
			if (tramite.getExTipoMovimentacao() == ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA
					&& mob.doc().isEletronico() && !tramite.isAssinada())
				acrescentarMarcaTransferencia(CpMarcadorEnum.DESPACHO_PENDENTE_DE_ASSINATURA.getId(),
						tramite.getDtIniMov(), null, tramite.getResp(), tramite.getLotaResp(), null);
		}
		
		Set<ExMovimentacao> recebidos = new TreeSet<>();
		recebidos.addAll(p.recebimentosPendentes);
		recebidos.removeAll(p.recebimentosDeNotificacoesPendentes);
		for (ExMovimentacao recebimento : recebidos) {
			acrescentarMarcaTransferencia(
					(mob.isAtendente(null, recebimento.getLotaResp()) ||
					(recebimento.getLotaResp() == null && mob.isAtendente(recebimento.getResp(), recebimento.getLotaResp())))
							? ((mob.getNumSequencia() > 1 || mob.doc().jaTransferido()) ? CpMarcadorEnum.EM_ANDAMENTO.getId()
									: CpMarcadorEnum.ASSINADO.getId())
							: CpMarcadorEnum.AGUARDANDO_CONCLUSAO.getId(),
					recebimento.getDtIniMov(), null, recebimento.getResp(), recebimento.getLotaResp(), null);
		}
		if (p.fIncluirCadastrante)
			acrescentarMarcaTransferencia(
					mob.isAtendente(mob.getTitular(), mob.getLotaTitular())
							? ((mob.getNumSequencia() > 1 || mob.doc().jaTransferido()) ? CpMarcadorEnum.EM_ANDAMENTO.getId()
									: CpMarcadorEnum.ASSINADO.getId())
							: CpMarcadorEnum.AGUARDANDO_CONCLUSAO.getId(),
					mob.doc().getDtRegDoc(), null, mob.getTitular(), mob.getLotaTitular(), null);
	}
	
	public void calcularMarcadoresDeNotificacao() {
		Pendencias p = mob.calcularTramitesPendentes();
		for (ExMovimentacao tramite : p.tramitesDeNotificacoesPendentes) {
			acrescentarMarcaTransferencia(CpMarcadorEnum.CAIXA_DE_ENTRADA.getId(),
					tramite.getDtIniMov(), null, tramite.getResp(), tramite.getLotaResp(), null);
		}

		for (ExMovimentacao recebimento : p.recebimentosDeNotificacoesPendentes) {
			acrescentarMarcaTransferencia(CpMarcadorEnum.AGUARDANDO_CONCLUSAO.getId(),
					recebimento.getDtIniMov(), null, recebimento.getResp(), recebimento.getLotaResp(), null);
		}
	}

	public void calcularMarcadoresTransferenciaComData(Date dt) {
		long m_aDevolverFora = CpMarcadorEnum.A_DEVOLVER_FORA_DO_PRAZO.getId();
		long m_aDevolver = CpMarcadorEnum.A_DEVOLVER.getId();
		long m_aguardando = CpMarcadorEnum.AGUARDANDO.getId();
		long m_aguardandoFora = CpMarcadorEnum.AGUARDANDO_DEVOLUCAO_FORA_DO_PRAZO.getId();

		List<ExMovimentacao> transferencias = mob.getMovimentacoesPorTipo(ExTipoDeMovimentacao.TRANSFERENCIA, false);
		transferencias.addAll(mob.getMovimentacoesPorTipo(ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA, false));
		transferencias.removeAll(mob.getMovimentacoesCanceladas());
		Set<ExMovimentacao> transferenciasComData = new TreeSet<ExMovimentacao>();

		Iterator it = transferencias.iterator();
		while (it.hasNext()) {
			ExMovimentacao elemento = (ExMovimentacao) it.next();
			if (elemento.getDtFimMov() != null) {
				transferenciasComData.add(elemento);
			}
		}

		Iterator itr = transferenciasComData.iterator();
		while (itr.hasNext()) {
			ExMovimentacao transfComData = (ExMovimentacao) itr.next();

			ExMobil mobil = transfComData.getExMobil();

			if (transfComData.getExMobil().isJuntado()) {
				mobil = transfComData.getExMobil().getMobilPrincipal();
			}

			ExMovimentacao movRetorno = contemTransferenciaRetorno(transfComData, mobil);

			if (movRetorno != null) {
				transferencias.remove(movRetorno);
			} else {
				Date dtMarca = transfComData.getDtFimMov();
				dtMarca.setHours(23);
				dtMarca.setMinutes(59);
				dtMarca.setSeconds(59);

				acrescentarMarcaTransferencia(m_aguardando, dt, dtMarca, transfComData.getCadastrante(),
						transfComData.getLotaCadastrante(), transfComData); // acrescenta a
				// marca
				// "Aguardando Devolução"

				acrescentarMarcaTransferencia(m_aDevolver, dt, dtMarca, transfComData.getResp(),
						transfComData.getLotaResp(), transfComData);// acrescenta
				// a
				// marca
				// "A Devolver"

				acrescentarMarcaTransferencia(m_aguardandoFora, dtMarca, null, transfComData.getCadastrante(),
						transfComData.getLotaCadastrante(), transfComData); // acrescenta a
				// marca
				// "Aguardando Devolução (Fora do Prazo)"

				acrescentarMarcaTransferencia(m_aDevolverFora, dtMarca, null, transfComData.getResp(),
						transfComData.getLotaResp(), transfComData);// acrescenta
				// a
				// marca
				// "A Devolver (Fora do Prazo)"
			}

		}
	}

	/**
	 * Calcula quais as marcas cada mobil terá com base nas movimentações que foram
	 * feitas no documento.
	 * 
	 * @param mob
	 */
	private SortedSet<ExMarca> acrescentarMarcadoresTemporalidade() {

		if (mob.isVolume() || !mob.doc().isFinalizado())
			return set;

		long[] mDest = new long[5];
		ExMovimentacao[] movDest = new ExMovimentacao[5];
		int nivelMDest = 0;

		for (ExMovimentacao mov : movs) {
			ITipoDeMovimentacao t = mov.getExTipoMovimentacao();

			if (t == ExTipoDeMovimentacao.ARQUIVAMENTO_CORRENTE) {
				nivelMDest++;
				mDest[nivelMDest] = CpMarcadorEnum.ARQUIVADO_CORRENTE.getId();
				movDest[nivelMDest] = mov;
			} else if (t == ExTipoDeMovimentacao.ARQUIVAMENTO_INTERMEDIARIO) {
				nivelMDest++;
				mDest[nivelMDest] = CpMarcadorEnum.ARQUIVADO_INTERMEDIARIO.getId();
				movDest[nivelMDest] = mov;
			} else if (t == ExTipoDeMovimentacao.ARQUIVAMENTO_PERMANENTE) {
				nivelMDest++;
				mDest[nivelMDest] = CpMarcadorEnum.ARQUIVADO_PERMANENTE.getId();
				movDest[nivelMDest] = mov;
			} else if (t == ExTipoDeMovimentacao.INCLUSAO_EM_EDITAL_DE_ELIMINACAO) {
				nivelMDest++;
				mDest[nivelMDest] = CpMarcadorEnum.EM_EDITAL_DE_ELIMINACAO.getId();
				movDest[nivelMDest] = mov;
			} else if (t == ExTipoDeMovimentacao.RETIRADA_DE_EDITAL_DE_ELIMINACAO
					|| t == ExTipoDeMovimentacao.DESARQUIVAMENTO_INTERMEDIARIO) {
				nivelMDest--;
			} else if (t == ExTipoDeMovimentacao.ELIMINACAO) {
				nivelMDest++;
				mDest[nivelMDest] = CpMarcadorEnum.ELIMINADO.getId();
				movDest[nivelMDest] = mov;
			} else if (t == ExTipoDeMovimentacao.DESARQUIVAMENTO_CORRENTE) {
				nivelMDest = 0;
			}

		}

		if (nivelMDest > 0) {
			acrescentarMarca(mDest[nivelMDest], 
					(movDest[nivelMDest].getExMovimentacaoRef() != null && ExTipoDeMovimentacao.hasArquivado(movDest[nivelMDest].getExMovimentacaoRef().getExTipoMovimentacao())) ?
					movDest[nivelMDest].getExMovimentacaoRef().getDtIniMov() : movDest[nivelMDest].getDtIniMov(), movDest[nivelMDest].getResp(),
					movDest[nivelMDest].getLotaResp());
			calcularMarcadoresFuturosTemporalidade(movDest[nivelMDest], mDest[nivelMDest]);
		}

		return set;
	}

	private void calcularMarcadoresFuturosTemporalidade(ExMovimentacao mov, Long marcador) {

		if (marcador != CpMarcadorEnum.ARQUIVADO_CORRENTE.getId()
				&& marcador != CpMarcadorEnum.ARQUIVADO_INTERMEDIARIO.getId())
			return;

		ExTemporalidade tmpCorrente = mob.getTemporalidadeCorrenteEfetiva();
		ExTemporalidade tmpIntermed = mob.getTemporalidadeIntermediarioEfetiva();
		ExTipoDestinacao destinacao = mob.getExDestinacaoFinalEfetiva();

		Date dtIniMarca = null;
		if (mov.getExMovimentacaoRef() != null && ExTipoDeMovimentacao.hasArquivado(mov.getExMovimentacaoRef().getExTipoMovimentacao()))
			dtIniMarca = mov.getExMovimentacaoRef().getDtIniMov();
		else
			dtIniMarca = mov.getDtIniMov();
		
		Long marcadorFuturo = 0L;

		if (marcador == CpMarcadorEnum.ARQUIVADO_CORRENTE.getId()) {
			if (tmpCorrente != null)
				dtIniMarca = tmpCorrente.getPrazoAPartirDaData(dtIniMarca);
			if (tmpIntermed != null)
				marcadorFuturo = CpMarcadorEnum.TRANSFERIR_PARA_ARQUIVO_INTERMEDIARIO.getId();
		} else if (tmpIntermed != null)
			dtIniMarca = tmpIntermed.getPrazoAPartirDaData(dtIniMarca);

		if (marcadorFuturo == 0)
			if (destinacao == null)
				return;
			else if (destinacao.getIdTpDestinacao().equals(ExTipoDestinacao.TIPO_DESTINACAO_ELIMINACAO))
				marcadorFuturo = CpMarcadorEnum.A_ELIMINAR.getId();
			else if (destinacao.getIdTpDestinacao().equals(ExTipoDestinacao.TIPO_DESTINACAO_GUARDA_PERMANENTE))
				marcadorFuturo = CpMarcadorEnum.RECOLHER_PARA_ARQUIVO_PERMANENTE.getId();
			else
				return;

		acrescentarMarca(marcadorFuturo, dtIniMarca, mov.getResp(), mov.getLotaResp());

	}

	private void acrescentarMarca(Long idMarcador, Date dt, DpPessoa pess, DpLotacao lota) {
		acrescentarMarca(idMarcador, dt, pess, lota, null);
	}

	private void acrescentarMarca(Long idMarcador, Date dt, DpPessoa pess, DpLotacao lota, Date dtFim) {
		ExMarca mar = new ExMarca();
		mar.setExMobil(mob);
		mar.setCpMarcador(ExDao.getInstance().consultar(idMarcador, CpMarcador.class, false));
		if (pess != null)
			mar.setDpPessoaIni(pess.getPessoaInicial());
		if (lota != null) {
			AcessoConsulta ac = new AcessoConsulta(0L, lota.getIdInicial(), 0L, lota.getOrgaoUsuario().getId());
			if (ac.podeAcessar(mob.doc(), null, lota))
				mar.setDpLotacaoIni(lota.getLotacaoInicial());
		}
		mar.setDtIniMarca(dt);

		if (dtFim != null)
			mar.setDtFimMarca(dtFim);
		set.add(mar);
	}

	private void acrescentarMarcaTransferencia(Long idMarcador, Date dtIni, Date dtFim, DpPessoa pess, DpLotacao lota,
			ExMovimentacao mov) {
		ExMarca mar = new ExMarca();
		mar.setExMobil(mob);
		mar.setCpMarcador(ExDao.getInstance().consultar(idMarcador, CpMarcador.class, false));
		mar.setExMovimentacao(mov);
		if (pess != null)
			mar.setDpPessoaIni(pess.getPessoaInicial());
		if (lota != null) {
			AcessoConsulta ac = new AcessoConsulta(0L, lota.getIdInicial(), 0L, lota.getOrgaoUsuario().getId());
			if (ac.podeAcessar(mob.doc(), null, lota))
				mar.setDpLotacaoIni(lota.getLotacaoInicial());
		}
		mar.setDtIniMarca(dtIni);
		mar.setDtFimMarca(dtFim);
		set.add(mar);
	}

	public ExMovimentacao contemTransferenciaRetorno(ExMovimentacao mov, ExMobil mob) {
		ExMovimentacao movRetorno = null;
		List<ExMovimentacao> transferencias = new ArrayList<>();
		
		if(mob.isVolume()) {
			for(ExMobil m : mob.getDoc().getExMobilSet()) {
				if(!m.isGeralDeProcesso() && m.getNumSequencia() >= mob.getNumSequencia()) {
					transferencias.addAll(m.getMovimentacoesPorTipo(ExTipoDeMovimentacao.TRANSFERENCIA, true));
				}
			}
		} else {
			transferencias.addAll(mob.getMovimentacoesPorTipo(ExTipoDeMovimentacao.TRANSFERENCIA, true));
		}
		transferencias.addAll(mob.getMovimentacoesPorTipo(ExTipoDeMovimentacao.DESPACHO_TRANSFERENCIA, true));
//		transferencias.removeAll(mob.getMovimentacoesCanceladas());

		Iterator it = transferencias.iterator();
		while (it.hasNext()) {
			ExMovimentacao transferencia = (ExMovimentacao) it.next();

			if (mov.getLotaCadastrante().equivale(transferencia.getLotaResp())
					&& transferencia.getData().after(mov.getData())) {
				movRetorno = transferencia;
				break;
			}
		}
		return movRetorno;
	}
	
}
