package br.gov.jfrj.siga.ex.bl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.ExTemporalidade;
import br.gov.jfrj.siga.ex.ExTipoDestinacao;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;

public class ExMarcadorBL {
	private ExMobil mob;
	private SortedSet<ExMarca> set;
	private SortedSet<ExMovimentacao> movs;
	ExMovimentacao ultMovNaoCanc;

	public ExMarcadorBL(SortedSet<ExMarca> set, ExMobil mob) {
		this.mob = mob;
		this.set = set;
		this.ultMovNaoCanc = mob.getUltimaMovimentacaoNaoCancelada();

		movs = new TreeSet<>();
		for (ExMovimentacao mov : mob.getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			movs.add(mov);
		}
	}

	private SortedSet<ExMovimentacao> movs(long... tipoMov) {
		SortedSet<ExMovimentacao> ss = new TreeSet<>();
		for (ExMovimentacao mov : mob.getExMovimentacaoSet()) {
			if (mov.isCancelada())
				continue;
			for (int j = 0; j < tipoMov.length; j++)
				if (tipoMov[j] == mov.getIdTpMov())
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
	 * Calcula quais as marcas cada mobil terá com base nas movimentações que
	 * foram feitas no documento.
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
			Long t = mov.getIdTpMov();
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_DE_MOVIMENTACAO
					&& mov.getExMovimentacaoRef() != null && mov.getExMovimentacaoRef().getExTipoMovimentacao()
							.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO)
				m = CpMarcador.MARCADOR_CANCELADO;
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_PENDENCIA_DE_ANEXACAO)
				m = CpMarcador.MARCADOR_PENDENTE_DE_ANEXACAO;
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_PUBLICACAO)
				m = CpMarcador.MARCADOR_PUBLICACAO_SOLICITADA;
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DISPONIBILIZACAO)
				m = CpMarcador.MARCADOR_DISPONIBILIZADO;
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO)
				m = CpMarcador.MARCADOR_REMETIDO_PARA_PUBLICACAO;
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_SOBRESTAR)
				m = CpMarcador.MARCADOR_SOBRESTADO;
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA)
				m = CpMarcador.MARCADOR_JUNTADO;
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA_EXTERNO)
				m = CpMarcador.MARCADOR_JUNTADO_EXTERNO;
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_APENSACAO && apensadoAVolumeDoMesmoProcesso)
				m = CpMarcador.MARCADOR_APENSADO;
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA
					|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA) {
				m = CpMarcador.MARCADOR_TRANSFERIDO_A_ORGAO_EXTERNO;
			}
			if ((t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA
					|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA) && !apensadoAVolumeDoMesmoProcesso) {
				m = CpMarcador.MARCADOR_CAIXA_DE_ENTRADA;
			}
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO && mob.doc().isEletronico() && !mov.isAssinada()) {
				m = CpMarcador.MARCADOR_DESPACHO_PENDENTE_DE_ASSINATURA;
			}

			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CRIACAO
					|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_RECEBIMENTO
					|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESOBRESTAR
					|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO
					|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA
					|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CANCELAMENTO_JUNTADA
					|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESAPENSACAO) {
				if (!mob.doc().isPendenteDeAssinatura() || mob.doc().getExTipoDocumento().getIdTpDoc() == 2
						|| mob.doc().getExTipoDocumento().getIdTpDoc() == 3) {

					if (!apensadoAVolumeDoMesmoProcesso) {
						m = CpMarcador.MARCADOR_EM_ANDAMENTO;
					} else
						m = CpMarcador.MARCADOR_APENSADO;

				} else if (mob.isApensado()) {
					m = CpMarcador.MARCADOR_APENSADO;
				}
			}

			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO && mob.doc().isEletronico() && !mov.isAssinada()) {
				m = CpMarcador.MARCADOR_ANEXO_PENDENTE_DE_ASSINATURA;
			}

			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA) {
				acrescentarMarca(CpMarcador.MARCADOR_MOVIMENTACAO_ASSINADA_COM_SENHA, dt, mov.getSubscritor(), null);
			}

			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA) {
				acrescentarMarca(CpMarcador.MARCADOR_MOVIMENTACAO_CONFERIDA_COM_SENHA, dt, mov.getSubscritor(), null);
			}

			if (m != mAnterior) {
				dt = mov.getDtIniMov();
				mAnterior = m;
			}
		}

		acrescentarMarcadoresPendenciaDeAssinatura(dt);

		calcularMarcadoresTransferencia(dt);

		// Acrescentar marcas manuais (Urgente, Idoso, etc)
		if (m == CpMarcador.MARCADOR_EM_ANDAMENTO) {
			ExMobil geral = mob.doc().getMobilGeral();
			if (geral.getExMovimentacaoSet() != null) {
				for (ExMovimentacao mov : geral.getExMovimentacaoSet()) {
					if (mov.isCancelada())
						continue;
					Long t = mov.getIdTpMov();
					if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_MARCACAO)
						acrescentarMarca(mov.getMarcador().getIdMarcador(), dt, ultMovNaoCanc.getResp(),
								ultMovNaoCanc.getLotaResp());
				}
			}
		}

		// Quando está na caixa de entrada, substituir por "A Receber", se for
		// físico
		if (m == CpMarcador.MARCADOR_CAIXA_DE_ENTRADA) {
			if (!mob.doc().isEletronico()) {
				m = CpMarcador.MARCADOR_A_RECEBER;
				acrescentarMarca(CpMarcador.MARCADOR_EM_TRANSITO, dt, ultMovNaoCanc.getCadastrante(),
						ultMovNaoCanc.getLotaCadastrante());
			} else {
				// Nato: isso aqui precisa melhorar, pois a última movimentação
				// pode não ser o despacho e mesmo assim ele não estar assinado.
				// Se o despacho ainda não foi assinado, marcar com
				// "Despacho pendente de assinatura"
				if (ultMovNaoCanc.getExTipoMovimentacao()
						.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA) {
					m = CpMarcador.MARCADOR_DESPACHO_PENDENTE_DE_ASSINATURA;
				} else {
					acrescentarMarca(CpMarcador.MARCADOR_EM_TRANSITO_ELETRONICO, dt, ultMovNaoCanc.getCadastrante(),
							ultMovNaoCanc.getLotaCadastrante());
				}
			}
		}

		if (m == CpMarcador.MARCADOR_TRANSFERIDO_A_ORGAO_EXTERNO) {
			// Quando é transferido para um órgão externo, a marca deve ficar
			// com o cadastrante e sua lotação, em vez do responsável
			acrescentarMarca(m, dt, ultMovNaoCanc.getCadastrante(), ultMovNaoCanc.getLotaCadastrante());
		} else if (m == CpMarcador.MARCADOR_DESPACHO_PENDENTE_DE_ASSINATURA) {
			// Se o cadastrante for digerente do subscritor
			if (ultMovNaoCanc.getCadastrante().getId() != ultMovNaoCanc.getSubscritor().getId()) {
				// Se a lotação do cadastrante for diferente da lotação do
				// subscritor
				if (ultMovNaoCanc.getLotaCadastrante().getIdLotacao() != ultMovNaoCanc.getLotaSubscritor()
						.getIdLotacao()) {
					// Acrescenta a marca para o subscritor e na sua lotação
					acrescentarMarca(m, dt, ultMovNaoCanc.getSubscritor(), ultMovNaoCanc.getLotaSubscritor());
				} else {
					// Acrescenta a marca na própria lotação do subscritor
					acrescentarMarca(m, dt, ultMovNaoCanc.getSubscritor(), null);
				}
			}
			// Acrecenta, também, a marca para o cadastrante e em sua lotação
			acrescentarMarca(m, dt, ultMovNaoCanc.getCadastrante(), ultMovNaoCanc.getLotaCadastrante());
		} else if (m == CpMarcador.MARCADOR_JUNTADO || m == CpMarcador.MARCADOR_APENSADO) {
			if (!mob.isEliminado())
				acrescentarMarca(m, dt, null, null);
		} else if (m != 0L) {
			// Edson: Os marcadores "Arq Corrente" e
			// "Aguardando andamento" são mutuamente exclusivos
			if (m != CpMarcador.MARCADOR_EM_ANDAMENTO
					|| !(mob.isArquivado() || mob.doc().getMobilGeral().isArquivado()))
				acrescentarMarca(m, dt, ultMovNaoCanc.getResp(), ultMovNaoCanc.getLotaResp());
		}

		return;
	}

	protected boolean acrescentarMarcadorCancelado() {
		// Cancelado
		if (ultMovNaoCanc == null) {
			ExMovimentacao ultMov = mob.getUltimaMovimentacao();
			Date dt = null;
			if (ultMov != null) {
				dt = ultMov.getDtIniMov();
			}
			acrescentarMarca(CpMarcador.MARCADOR_CANCELADO, dt, mob.doc().getCadastrante(),
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
		acrescentarMarcadoresPendenciaDeAssinaturaMovimentacao();
		acrescentarMarcadoresDoCossignatario();
		acrescentarMarcadoresAssinaturaComSenha();
	}

	protected boolean acrescentarMarcadoresSemEfeito() {
		// Sem efeito
		for (ExMovimentacao mov : movs(ExTipoMovimentacao.TIPO_MOVIMENTACAO_TORNAR_SEM_EFEITO)) {
			acrescentarMarca(CpMarcador.MARCADOR_SEM_EFEITO, mov.getDtIniMov(), mov.getCadastrante(),
					mov.getLotaCadastrante());
			return true;
		}
		return false;
	}

	protected void acrescentarMarcadoresElaboracao() {
		// Se não estiver finalizado
		if (!mob.doc().isFinalizado()) {
			acrescentarMarca(CpMarcador.MARCADOR_EM_ELABORACAO, mob.doc().getDtRegDoc(), mob.doc().getCadastrante(),
					mob.doc().getLotaCadastrante());
			if (mob.getExDocumento().getSubscritor() != null)
				acrescentarMarca(CpMarcador.MARCADOR_REVISAR, mob.doc().getDtRegDoc(),
						mob.getExDocumento().getSubscritor(), null);

		}
	}

	public void acrescentarMarcadoresColaboracao() {
		boolean fDocColaborativoPendencia = false;
		Date dtDocColaborativo = null;
		for (ExMovimentacao mov : movs(ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONTROLE_DE_COLABORACAO)) {
			dtDocColaborativo = mov.getDtIniMov();
			ExParte parte = ExParte.create(mov.getDescrMov());
			if (parte.isAtivo() && !parte.isPreenchido()) {
				fDocColaborativoPendencia = true;
				acrescentarMarca(CpMarcador.MARCADOR_PENDENTE_DE_COLABORACAO, mov.getDtIniMov(), mov.getSubscritor(),
						mov.getLotaSubscritor());
			}
		}
		if (dtDocColaborativo != null && !fDocColaborativoPendencia) {
			acrescentarMarca(CpMarcador.MARCADOR_FINALIZAR_DOCUMENTO_COLABORATIVO, dtDocColaborativo,
					mob.doc().getCadastrante(), mob.doc().getLotaCadastrante());
		}
	}

	public void acrescentarMarcadoresPapel() {
		for (ExMovimentacao mov : movs(ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL)) {
			Long m = null;
			DpLotacao lotaPerfil = null;
			switch ((int) (long) mov.getExPapel().getIdPapel()) {
			case (int) ExPapel.PAPEL_GESTOR:
				m = CpMarcador.MARCADOR_COMO_GESTOR;
				break;
			case (int) ExPapel.PAPEL_REVISOR:
				m = CpMarcador.MARCADOR_COMO_REVISOR;
				break;
			case (int) ExPapel.PAPEL_INTERESSADO:
				m = CpMarcador.MARCADOR_COMO_INTERESSADO;
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
			Long m = null;
			Long t = mov.getIdTpMov();
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_PUBLICACAO) {
				mDje = CpMarcador.MARCADOR_PUBLICACAO_SOLICITADA;
				movDje = mov;
			} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO) {
				mDje = CpMarcador.MARCADOR_REMETIDO_PARA_PUBLICACAO;
				movDje = mov;
			} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DISPONIBILIZACAO) {
				mDje = CpMarcador.MARCADOR_DISPONIBILIZADO;
				movDje = mov;
			}
		}
		if (mDje != null && !mob.doc().isEliminado()) {
			acrescentarMarca(mDje, movDje.getDtIniMov(), movDje.getTitular(), movDje.getLotaTitular());
		}
	}

	public void acrescentarMarcadoresPendenciaDeAnexacao() {
		for (ExMovimentacao mov : movs(ExTipoMovimentacao.TIPO_MOVIMENTACAO_PENDENCIA_DE_ANEXACAO)) {
			acrescentarMarca(CpMarcador.MARCADOR_PENDENTE_DE_ANEXACAO, mov.getDtIniMov(), mov.getCadastrante(),
					mov.getLotaCadastrante());
		}
	}

	public void acrescentarMarcadoresPendenciaDeAssinatura(Date dt) {
		if (mob.doc().isPendenteDeAssinatura()) {
			acrescentarMarca(CpMarcador.MARCADOR_PENDENTE_DE_ASSINATURA, dt, ultMovNaoCanc.getResp(),
					ultMovNaoCanc.getLotaResp());
			if (!mob.getDoc().isAssinadoPeloSubscritorComTokenOuSenha()) {
				acrescentarMarca(CpMarcador.MARCADOR_COMO_SUBSCRITOR, dt, mob.getExDocumento().getSubscritor(), null);
			}
			if (mob.getDoc().isAssinaturaSolicitada()) {
				acrescentarMarca(CpMarcador.MARCADOR_PRONTO_PARA_ASSINAR, dt, mob.getExDocumento().getSubscritor(),
						null);
			}
		}
	}

	public void acrescentarMarcadoresPendenciaDeAssinaturaMovimentacao() {
		for (ExMovimentacao mov : movs(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO)) {
			Long m = null;
			if (mob.doc().isEletronico()) {
				m = CpMarcador.MARCADOR_ANEXO_PENDENTE_DE_ASSINATURA;
				/*
				 * não é possível usar ExMovimentacao.isAssinada() pois não há
				 * tempo habil no BD de efetivar a inclusao de movimentacao de
				 * assinatura de movimentção Edson: Por que não?
				 */
				for (ExMovimentacao movAss : mob.getExMovimentacaoSet()) {
					if ((movAss.getExTipoMovimentacao()
							.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_MOVIMENTACAO
							|| movAss.getExTipoMovimentacao()
									.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO
							|| movAss.getExTipoMovimentacao()
									.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA
							|| movAss.getExTipoMovimentacao()
									.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA)
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

		for (ExMovimentacao mov : movs(ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_DE_COSIGNATARIO)) {
			if (mob.getDoc().isEletronico()) {
				if (mob.getDoc().isAssinadoPelaPessoaComTokenOuSenha(mov.getSubscritor()))
					return;
				else if (mob.getDoc().isAssinadoPeloSubscritorComTokenOuSenha())
					acrescentarMarca(CpMarcador.MARCADOR_COMO_SUBSCRITOR, mov.getDtIniMov(), mov.getSubscritor(), null);
				else
					acrescentarMarca(CpMarcador.MARCADOR_REVISAR, mov.getDtIniMov(), mov.getSubscritor(), null);

			}
		}
	}

	public void acrescentarMarcadoresAssinaturaComSenha() {
		for (ExMovimentacao mov : movs(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_COM_SENHA)) {
			Long t = mov.getIdTpMov();

			boolean jaAutenticado = false;
			for (ExMovimentacao movAss : mob.getExMovimentacaoSet()) {
				if (movAss.getExTipoMovimentacao()
						.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_DIGITAL_DOCUMENTO
						|| movAss.getExTipoMovimentacao()
								.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_DOCUMENTO) {
					jaAutenticado = true;
					break;
				}
			}

			if (!jaAutenticado)
				acrescentarMarca(CpMarcador.MARCADOR_DOCUMENTO_ASSINADO_COM_SENHA, mov.getDtIniMov(),
						mov.getSubscritor(), null);
		}
	}

	public void calcularMarcadoresTransferencia(Date dt) {
		long m_aDevolverFora = CpMarcador.MARCADOR_A_DEVOLVER_FORA_DO_PRAZO;
		long m_aDevolver = CpMarcador.MARCADOR_A_DEVOLVER;
		long m_aguardando = CpMarcador.MARCADOR_AGUARDANDO;
		long m_aguardandoFora = CpMarcador.MARCADOR_AGUARDANDO_DEVOLUCAO_FORA_DO_PRAZO;

		List<ExMovimentacao> transferencias = mob.getMovimentacoesPorTipo(3);
		transferencias.addAll(mob.getMovimentacoesPorTipo(6));
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
						transfComData.getLotaCadastrante()); // acrescenta a
				// marca
				// "Aguardando Devolução"

				acrescentarMarcaTransferencia(m_aDevolver, dt, dtMarca, transfComData.getResp(),
						transfComData.getLotaResp());// acrescenta
				// a
				// marca
				// "A Devolver"

				acrescentarMarcaTransferencia(m_aguardandoFora, dtMarca, null, transfComData.getCadastrante(),
						transfComData.getLotaCadastrante()); // acrescenta a
				// marca
				// "Aguardando Devolução (Fora do Prazo)"

				acrescentarMarcaTransferencia(m_aDevolverFora, dtMarca, null, transfComData.getResp(),
						transfComData.getLotaResp());// acrescenta
				// a
				// marca
				// "A Devolver (Fora do Prazo)"
			}

		}
	}

	/**
	 * Calcula quais as marcas cada mobil terá com base nas movimentações que
	 * foram feitas no documento.
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
			Long t = mov.getIdTpMov();

			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_CORRENTE) {
				nivelMDest++;
				mDest[nivelMDest] = CpMarcador.MARCADOR_ARQUIVADO_CORRENTE;
				movDest[nivelMDest] = mov;
			} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_INTERMEDIARIO) {
				nivelMDest++;
				mDest[nivelMDest] = CpMarcador.MARCADOR_ARQUIVADO_INTERMEDIARIO;
				movDest[nivelMDest] = mov;
			} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_PERMANENTE) {
				nivelMDest++;
				mDest[nivelMDest] = CpMarcador.MARCADOR_ARQUIVADO_PERMANENTE;
				movDest[nivelMDest] = mov;
			} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_EM_EDITAL_DE_ELIMINACAO) {
				nivelMDest++;
				mDest[nivelMDest] = CpMarcador.MARCADOR_EM_EDITAL_DE_ELIMINACAO;
				movDest[nivelMDest] = mov;
			} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_RETIRADA_DE_EDITAL_DE_ELIMINACAO
					|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESARQUIVAMENTO_INTERMEDIARIO) {
				nivelMDest--;
			} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ELIMINACAO) {
				nivelMDest++;
				mDest[nivelMDest] = CpMarcador.MARCADOR_ELIMINADO;
				movDest[nivelMDest] = mov;
			} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESARQUIVAMENTO_CORRENTE) {
				nivelMDest = 0;
			}

		}

		if (nivelMDest > 0) {
			acrescentarMarca(mDest[nivelMDest], movDest[nivelMDest].getDtIniMov(), movDest[nivelMDest].getResp(),
					movDest[nivelMDest].getLotaResp());
			calcularMarcadoresFuturosTemporalidade(movDest[nivelMDest], mDest[nivelMDest]);
		}

		return set;
	}

	private void calcularMarcadoresFuturosTemporalidade(ExMovimentacao mov, Long marcador) {

		if (marcador != CpMarcador.MARCADOR_ARQUIVADO_CORRENTE
				&& marcador != CpMarcador.MARCADOR_ARQUIVADO_INTERMEDIARIO)
			return;

		ExTemporalidade tmpCorrente = mob.getTemporalidadeCorrenteEfetiva();
		ExTemporalidade tmpIntermed = mob.getTemporalidadeIntermediarioEfetiva();
		ExTipoDestinacao destinacao = mob.getExDestinacaoFinalEfetiva();

		Date dtIniMarca = mov.getDtIniMov();
		Long marcadorFuturo = 0L;

		if (marcador == CpMarcador.MARCADOR_ARQUIVADO_CORRENTE) {
			if (tmpCorrente != null)
				dtIniMarca = tmpCorrente.getPrazoAPartirDaData(dtIniMarca);
			if (tmpIntermed != null)
				marcadorFuturo = CpMarcador.MARCADOR_TRANSFERIR_PARA_ARQUIVO_INTERMEDIARIO;
		} else if (tmpIntermed != null)
			dtIniMarca = tmpIntermed.getPrazoAPartirDaData(dtIniMarca);

		if (marcadorFuturo == 0)
			if (destinacao == null)
				return;
			else if (destinacao.getIdTpDestinacao().equals(ExTipoDestinacao.TIPO_DESTINACAO_ELIMINACAO))
				marcadorFuturo = CpMarcador.MARCADOR_A_ELIMINAR;
			else if (destinacao.getIdTpDestinacao().equals(ExTipoDestinacao.TIPO_DESTINACAO_GUARDA_PERMANENTE))
				marcadorFuturo = CpMarcador.MARCADOR_RECOLHER_PARA_ARQUIVO_PERMANENTE;
			else
				return;

		acrescentarMarca(marcadorFuturo, dtIniMarca, mov.getResp(), mov.getLotaResp());

	}

	private void acrescentarMarca(Long idMarcador, Date dt, DpPessoa pess, DpLotacao lota) {
		ExMarca mar = new ExMarca();
		mar.setExMobil(mob);
		mar.setCpMarcador(ExDao.getInstance().consultar(idMarcador, CpMarcador.class, false));
		if (pess != null)
			mar.setDpPessoaIni(pess.getPessoaInicial());
		if (lota != null)
			mar.setDpLotacaoIni(lota.getLotacaoInicial());
		mar.setDtIniMarca(dt);
		set.add(mar);
	}

	private void acrescentarMarcaTransferencia(Long idMarcador, Date dtIni, Date dtFim, DpPessoa pess, DpLotacao lota) {
		ExMarca mar = new ExMarca();
		mar.setExMobil(mob);
		mar.setCpMarcador(ExDao.getInstance().consultar(idMarcador, CpMarcador.class, false));
		if (pess != null)
			mar.setDpPessoaIni(pess.getPessoaInicial());
		if (lota != null)
			mar.setDpLotacaoIni(lota.getLotacaoInicial());
		mar.setDtIniMarca(dtIni);
		mar.setDtFimMarca(dtFim);
		set.add(mar);
	}

	public ExMovimentacao contemTransferenciaRetorno(ExMovimentacao mov, ExMobil mob) {
		ExMovimentacao movRetorno = null;
		List<ExMovimentacao> transferencias = mob.getMovimentacoesPorTipo(3);
		transferencias.addAll(mob.getMovimentacoesPorTipo(6));
		transferencias.removeAll(mob.getMovimentacoesCanceladas());

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
