package br.gov.jfrj.siga.ex.bl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoMarcadorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorTipoExibicaoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorTipoInteressadoEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorFinalidadeGrupoEnum;
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
				m = CpMarcadorEnum.CANCELADO.getId();
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_PENDENCIA_DE_ANEXACAO)
				m = CpMarcadorEnum.PENDENTE_DE_ANEXACAO.getId();
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_PUBLICACAO)
				m = CpMarcadorEnum.PUBLICACAO_SOLICITADA.getId();
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DISPONIBILIZACAO)
				m = CpMarcadorEnum.DISPONIBILIZADO.getId();
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO)
				m = CpMarcadorEnum.REMETIDO_PARA_PUBLICACAO.getId();
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_SOBRESTAR)
				m = CpMarcadorEnum.SOBRESTADO.getId();
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA)
				m = CpMarcadorEnum.JUNTADO.getId();
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_JUNTADA_EXTERNO)
				m = CpMarcadorEnum.JUNTADO_EXTERNO.getId();
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_APENSACAO && apensadoAVolumeDoMesmoProcesso)
				m = CpMarcadorEnum.APENSADO.getId();
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA_EXTERNA
					|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA_EXTERNA) {
				m = CpMarcadorEnum.TRANSFERIDO_A_ORGAO_EXTERNO.getId();
			}
			if ((t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA
					|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA) && !apensadoAVolumeDoMesmoProcesso) {
				m = CpMarcadorEnum.CAIXA_DE_ENTRADA.getId();
			}
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO && mob.doc().isEletronico() && !mov.isAssinada()) {
				m = CpMarcadorEnum.DESPACHO_PENDENTE_DE_ASSINATURA.getId();
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
						m = CpMarcadorEnum.EM_ANDAMENTO.getId();
					} else
						m = CpMarcadorEnum.APENSADO.getId();

				} else if (mob.isApensado()) {
					m = CpMarcadorEnum.APENSADO.getId();
				}
			}
			
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO && mob.doc().isEletronico() && !mov.isAssinada()) {
				m = CpMarcadorEnum.ANEXO_PENDENTE_DE_ASSINATURA.getId();
			}

			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ASSINATURA_MOVIMENTACAO_COM_SENHA) {
				acrescentarMarca(CpMarcadorEnum.MOVIMENTACAO_ASSINADA_COM_SENHA.getId(), dt, mov.getSubscritor(), null);
			}

			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_CONFERENCIA_COPIA_COM_SENHA) {
				acrescentarMarca(CpMarcadorEnum.MOVIMENTACAO_CONFERIDA_COM_SENHA.getId(), dt, mov.getSubscritor(), null);
			}

			if (m != mAnterior) {
				dt = mov.getDtIniMov();
				mAnterior = m;
			}
		}
		
		if (!mob.isArquivado())
			calcularMarcadoresTransferencia(dt);

		acrescentarMarcadoresManuais();

		// Quando está na caixa de entrada, substituir por "A Receber", se for
		// físico
		if (m == CpMarcadorEnum.CAIXA_DE_ENTRADA.getId()) {
			if (!mob.doc().isEletronico()) {
				m = CpMarcadorEnum.A_RECEBER.getId();
				acrescentarMarca(CpMarcadorEnum.EM_TRANSITO.getId(), dt, ultMovNaoCanc.getCadastrante(),
						ultMovNaoCanc.getLotaCadastrante());
			} else {
				// Nato: isso aqui precisa melhorar, pois a última movimentação
				// pode não ser o despacho e mesmo assim ele não estar assinado.
				// Se o despacho ainda não foi assinado, marcar com
				// "Despacho pendente de assinatura"
				if (ultMovNaoCanc.getExTipoMovimentacao()
						.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESPACHO_TRANSFERENCIA) {
					m = CpMarcadorEnum.DESPACHO_PENDENTE_DE_ASSINATURA.getId();
				} else {
					acrescentarMarca(CpMarcadorEnum.EM_TRANSITO_ELETRONICO.getId(), dt, ultMovNaoCanc.getCadastrante(),
							ultMovNaoCanc.getLotaCadastrante());
				}
			}
		}

		if (m == CpMarcadorEnum.TRANSFERIDO_A_ORGAO_EXTERNO.getId()) {
			// Quando é transferido para um órgão externo, a marca deve ficar
			// com o cadastrante e sua lotação, em vez do responsável
			acrescentarMarca(m, dt, ultMovNaoCanc.getCadastrante(), ultMovNaoCanc.getLotaCadastrante());
		} else if (m == CpMarcadorEnum.DESPACHO_PENDENTE_DE_ASSINATURA.getId()) {
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
		} else if (m == CpMarcadorEnum.JUNTADO.getId() || m == CpMarcadorEnum.APENSADO.getId()) {
			if (!mob.isEliminado())
				acrescentarMarca(m, dt, null, null);
		} else if (m != 0L) {
			// Edson: Os marcadores "Arq Corrente" e
			// "Aguardando andamento" são mutuamente exclusivos
			if (m != CpMarcadorEnum.EM_ANDAMENTO.getId()
					|| !(mob.isArquivado() || mob.doc().getMobilGeral().isArquivado()))
				acrescentarMarca(m, dt, ultMovNaoCanc.getResp(), ultMovNaoCanc.getLotaResp());
			
 			if (SigaMessages.isSigaSP() && 
					m == CpMarcadorEnum.CAIXA_DE_ENTRADA.getId() && 
					ultMovNaoCanc.getIdTpMov() == ExTipoMovimentacao.TIPO_MOVIMENTACAO_TRANSFERENCIA) {
				acrescentarMarcadoresManuaisPorOcorrenciaDeTransferencia(dt);
			}
		
		}

		return;
	}

	private void acrescentarMarcadoresManuais() {
		boolean fPasta = false;
		
		// Acrescentar marcas manuais (Urgente, Idoso, etc)
		ExMobil geral = mob.doc().getMobilGeral();
		
		// Conteplar movimentações gerais e também as da via específica
		List<ExMovimentacao> marcacoes = new ArrayList<>();
		marcacoes.addAll(geral.getMovimentacoesPorTipo(ExTipoMovimentacao.TIPO_MOVIMENTACAO_MARCACAO, true));
		if (mob.isVia()) 
			marcacoes.addAll(mob.getMovimentacoesPorTipo(ExTipoMovimentacao.TIPO_MOVIMENTACAO_MARCACAO, true));

		// Marcações gerais
		for (ExMovimentacao mov : marcacoes) {
			CpMarcador marcador = mov.getMarcador(); 
			
			// Aplicar apenas no móbil correto
			if (marcador.isAplicacaoGeral() && !mob.isGeral())
				continue;
			if (marcador.isAplicacaoGeralOuViaEspecificaOuUltimoVolume() && (mob.isGeral() || mob.isVolumeEncerrado()))
				continue;
			if (marcador.isAplicacaoGeralOuTodasAsViasOuUltimoVolume() && ((mob.isGeral() && mob.doc().isFinalizado())
					|| mob.isVolumeEncerrado()))
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
			if (dtIni == null && mov.getDtParam1() != null && mov.getDtParam2() != null && marcador.getIdFinalidade().getIdTpExibicao() == CpMarcadorTipoExibicaoEnum.MENOR_DATA)
				dtIni = mov.getDtParam1().before(mov.getDtParam2()) ? mov.getDtParam1() : mov.getDtParam2();
			if (dtIni == null && mov.getDtParam1() != null && marcador.getIdFinalidade().getIdTpExibicao() == CpMarcadorTipoExibicaoEnum.DATA_PLANEJADA)
				dtIni = mov.getDtParam1();
			if (dtIni == null && mov.getDtParam2() != null && marcador.getIdFinalidade().getIdTpExibicao() == CpMarcadorTipoExibicaoEnum.DATA_LIMITE)
				dtIni = mov.getDtParam2();
			if (dtIni == null)
				dtIni = mov.getDtIniMov();

			// Calcular pessoa ou lotação
			DpPessoa pes = null;
			DpLotacao lot = null;
			if (marcador.isInteressadoAtentende()) {
				pes = ultMovNaoCanc.getResp();
				lot = ultMovNaoCanc.getLotaResp();
			} else if (marcador.isInteressadoPessoa() && mov.getSubscritor() != null) {
				pes = mov.getSubscritor();
			} else if (marcador.isInteressadoLotacao() && mov.getLotaSubscritor() != null) {
				lot = mov.getLotaSubscritor();
			}
			
			// Indicar se está alocado numa pasta
			if (marcador.getIdFinalidade().getGrupo() == CpMarcadorFinalidadeGrupoEnum.PASTA) {
				if (!mob.isAguardandoAndamento())
					continue;
				fPasta = true;
			}
			
			acrescentarMarcaTransferencia(marcador.getIdMarcador(), dtIni, dtFim, pes,	lot, mov);
		}
		
		if (!fPasta && mob.isAguardandoAndamento()) {
			DpLotacao lotaAtendente = mob.getAtendente().getLotacao();
			if (lotaAtendente != null) {
				CpMarcador mpp = ExDao.getInstance().obterPastaPadraoDaLotacao(lotaAtendente);
				if (mpp != null) 
					acrescentarMarcaTransferencia(mpp.getId(), null, null, null, lotaAtendente, null);
			}
		}
	}
	
	protected boolean acrescentarMarcadorCancelado() {
		// Cancelado
		if (ultMovNaoCanc == null) {
			ExMovimentacao ultMov = mob.getUltimaMovimentacao();
			Date dt = null;
			if (ultMov != null) {
				dt = ultMov.getDtIniMov();
			}
			acrescentarMarca(CpMarcadorEnum.CANCELADO.getId(), dt, mob.doc().getCadastrante(),
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
		for (ExMovimentacao mov : movs(ExTipoMovimentacao.TIPO_MOVIMENTACAO_TORNAR_SEM_EFEITO)) {
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
					(Ex.getInstance().getConf().podePorConfiguracao(mob.doc().getCadastrante(), mob.doc().getLotaCadastrante(), 
							null, mob.doc().getExModelo().getExFormaDocumento(), null, CpTipoConfiguracao.TIPO_CONFIG_TMP_PARA_LOTACAO) ? 
									mob.doc().getLotaCadastrante() : null));
			if (mob.getExDocumento().getSubscritor() != null
					&& !(Prop.getBool("/siga.mesa.nao.revisar.temporarios") 
							&& !mob.doc().getCadastrante().equals(mob.doc().getSubscritor()) 
							&& !mob.doc().isFinalizado())) {
				acrescentarMarca(CpMarcadorEnum.REVISAR.getId(), mob.doc().getDtRegDoc(),
						mob.getExDocumento().getSubscritor(), null);
			}
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
		for (ExMovimentacao mov : movs(ExTipoMovimentacao.TIPO_MOVIMENTACAO_VINCULACAO_PAPEL)) {
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
			Long m = null;
			Long t = mov.getIdTpMov();
			if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_PEDIDO_PUBLICACAO) {
				mDje = CpMarcadorEnum.PUBLICACAO_SOLICITADA.getId();
				movDje = mov;
			} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_AGENDAMENTO_DE_PUBLICACAO) {
				mDje = CpMarcadorEnum.REMETIDO_PARA_PUBLICACAO.getId();
				movDje = mov;
			} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DISPONIBILIZACAO) {
				mDje = CpMarcadorEnum.DISPONIBILIZADO.getId();
				movDje = mov;
			}
		}
		if (mDje != null && !mob.doc().isEliminado()) {
			acrescentarMarca(mDje, movDje.getDtIniMov(), movDje.getTitular(), movDje.getLotaTitular());
		}
	}

	public void acrescentarMarcadoresPendenciaDeAnexacao() {
		for (ExMovimentacao mov : movs(ExTipoMovimentacao.TIPO_MOVIMENTACAO_PENDENCIA_DE_ANEXACAO)) {
			acrescentarMarca(CpMarcadorEnum.PENDENTE_DE_ANEXACAO.getId(), mov.getDtIniMov(), mov.getCadastrante(),
					mov.getLotaCadastrante());
		}
	}

	public void acrescentarMarcadoresPendenciaDeAssinatura() {
		if (!(SigaMessages.isSigaSP() && !mob.doc().isFinalizado()) 
		                && mob.doc().isPendenteDeAssinatura() && !mob.doc().isCancelado()) {


	/*		Não estava setando a amrca pendente de assinatura corretamente na susbituição.
	 *      DpPessoa resp = ultMovNaoCanc != null ? ultMovNaoCanc.getResp() : mob.doc().getCadastrante();
			DpLotacao lotaResp  = ultMovNaoCanc != null ? ultMovNaoCanc.getLotaResp() : mob.doc().getLotaCadastrante(); */
			acrescentarMarca(CpMarcadorEnum.PENDENTE_DE_ASSINATURA.getId(), mob.doc().getDtRegDoc(), mob.doc().getCadastrante(),
					 mob.doc().getLotaCadastrante());
			if (!mob.getDoc().isAssinadoPeloSubscritorComTokenOuSenha()
					&& !(Prop.getBool("/siga.mesa.nao.revisar.temporarios")
						&& !mob.doc().getCadastrante().equals(mob.doc().getSubscritor()) 
						&& !mob.doc().isFinalizado())) {
				acrescentarMarca(CpMarcadorEnum.COMO_SUBSCRITOR.getId(), mob.doc().getDtRegDoc(), mob.getExDocumento().getSubscritor(), null);
				ExMovimentacao m = mob.doc().getMovSolicitacaoDeAssinatura();
				if (m != null) {
					acrescentarMarca(CpMarcadorEnum.PRONTO_PARA_ASSINAR.getId(), m.getDtIniMov(), mob.getExDocumento().getSubscritor(),
							null);
				}
			}
		}
	}

	public void acrescentarMarcadoresPendenciaDeAssinaturaMovimentacao() {
		for (ExMovimentacao mov : movs(ExTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXACAO)) {
			Long m = null;
			if (mob.doc().isEletronico()) {
				m = CpMarcadorEnum.ANEXO_PENDENTE_DE_ASSINATURA.getId();
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
					continue;
				else if (mob.getDoc().isAssinadoPeloSubscritorComTokenOuSenha())
					acrescentarMarca(CpMarcadorEnum.COMO_SUBSCRITOR.getId(), mov.getDtIniMov(), mov.getSubscritor(), null);
				else {
					if (!(Prop.getBool("/siga.mesa.nao.revisar.temporarios") 
								&& !mob.getDoc().isFinalizado())) 
						acrescentarMarca(CpMarcadorEnum.REVISAR.getId(), mov.getDtIniMov(), mov.getSubscritor(), null);
					if (!(Prop.getBool("/siga.mesa.nao.revisar.temporarios") 
							&& !mob.getDoc().isFinalizado()) && Ex.getInstance().getConf().podePorConfiguracao(mov.getSubscritor(), mov.getSubscritor().getLotacao(), CpTipoConfiguracao.TIPO_CONFIG_COSIGNATARIO_ASSINAR_ANTES_SUBSCRITOR))
						acrescentarMarca(CpMarcadorEnum.COMO_SUBSCRITOR.getId(), mov.getDtIniMov(), mov.getSubscritor(), null);						
				}	
			}
		}
	}

	public void acrescentarMarcadoresManuaisPorOcorrenciaDeTransferencia(Date dt) {	
		ExMobil geral = mob.doc().getMobilGeral();
		if (geral.getExMovimentacaoSet() != null) {
			for (ExMovimentacao mov : geral.getExMovimentacaoSet()) {
				if (mov.isCancelada() || mov.getMarcador() == null)
					continue;
				
				Long tpMov = mov.getIdTpMov();
				Long idMarcador = mov.getMarcador().getIdMarcador();
				boolean temMarcaManual = (tpMov == ExTipoMovimentacao.TIPO_MOVIMENTACAO_MARCACAO &&
						(idMarcador == CpMarcadorEnum.URGENTE.getId() 
						|| idMarcador == CpMarcadorEnum.IDOSO.getId() 
						|| idMarcador == CpMarcadorEnum.PRIORITARIO.getId()  
						|| idMarcador == CpMarcadorEnum.RESTRICAO_ACESSO.getId()
						|| idMarcador == CpMarcadorEnum.COVID_19.getId()
						|| idMarcador == CpMarcadorEnum.NOTA_EMPENHO.getId()
						|| idMarcador == CpMarcadorEnum.DEMANDA_JUDICIAL_BAIXA.getId()
						|| idMarcador == CpMarcadorEnum.DEMANDA_JUDICIAL_MEDIA.getId()
						|| idMarcador == CpMarcadorEnum.DEMANDA_JUDICIAL_ALTA.getId()));
								
				if (temMarcaManual)	{
					acrescentarMarca(mov.getMarcador().getIdMarcador(), dt, ultMovNaoCanc.getResp(), ultMovNaoCanc.getLotaResp());
				}																								
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
				acrescentarMarca(CpMarcadorEnum.DOCUMENTO_ASSINADO_COM_SENHA.getId(), mov.getDtIniMov(),
						mov.getSubscritor(), null);
		}
	}
	

	public void acrescentarMarcadorPublicacaoPortalTransparencia() {
		for (ExMovimentacao mov : movs(ExTipoMovimentacao.TIPO_MOVIMENTACAO_PUBLICACAO_PORTAL_TRANSPARENCIA)) {
			acrescentarMarca(CpMarcadorEnum.PORTAL_TRANSPARENCIA.getId(), mov.getDtIniMov(), mov.getCadastrante(),
					mov.getLotaCadastrante());
		}
	}

	public void calcularMarcadoresTransferencia(Date dt) {
		long m_aDevolverFora = CpMarcadorEnum.A_DEVOLVER_FORA_DO_PRAZO.getId();
		long m_aDevolver = CpMarcadorEnum.A_DEVOLVER.getId();
		long m_aguardando = CpMarcadorEnum.AGUARDANDO.getId();
		long m_aguardandoFora = CpMarcadorEnum.AGUARDANDO_DEVOLUCAO_FORA_DO_PRAZO.getId();

		List<ExMovimentacao> transferencias = mob.getMovimentacoesPorTipo(3, false);
		transferencias.addAll(mob.getMovimentacoesPorTipo(6, false));
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
				mDest[nivelMDest] = CpMarcadorEnum.ARQUIVADO_CORRENTE.getId();
				movDest[nivelMDest] = mov;
			} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_INTERMEDIARIO) {
				nivelMDest++;
				mDest[nivelMDest] = CpMarcadorEnum.ARQUIVADO_INTERMEDIARIO.getId();
				movDest[nivelMDest] = mov;
			} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ARQUIVAMENTO_PERMANENTE) {
				nivelMDest++;
				mDest[nivelMDest] = CpMarcadorEnum.ARQUIVADO_PERMANENTE.getId();
				movDest[nivelMDest] = mov;
			} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_EM_EDITAL_DE_ELIMINACAO) {
				nivelMDest++;
				mDest[nivelMDest] = CpMarcadorEnum.EM_EDITAL_DE_ELIMINACAO.getId();
				movDest[nivelMDest] = mov;
			} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_RETIRADA_DE_EDITAL_DE_ELIMINACAO
					|| t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_DESARQUIVAMENTO_INTERMEDIARIO) {
				nivelMDest--;
			} else if (t == ExTipoMovimentacao.TIPO_MOVIMENTACAO_ELIMINACAO) {
				nivelMDest++;
				mDest[nivelMDest] = CpMarcadorEnum.ELIMINADO.getId();
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

		if (marcador != CpMarcadorEnum.ARQUIVADO_CORRENTE.getId()
				&& marcador != CpMarcadorEnum.ARQUIVADO_INTERMEDIARIO.getId())
			return;

		ExTemporalidade tmpCorrente = mob.getTemporalidadeCorrenteEfetiva();
		ExTemporalidade tmpIntermed = mob.getTemporalidadeIntermediarioEfetiva();
		ExTipoDestinacao destinacao = mob.getExDestinacaoFinalEfetiva();

		Date dtIniMarca = mov.getDtIniMov();
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
		ExMarca mar = new ExMarca();
		mar.setExMobil(mob);
		mar.setCpMarcador(ExDao.getInstance().consultar(idMarcador, CpMarcador.class, false));
		if (pess != null)
			mar.setDpPessoaIni(pess.getPessoaInicial());
		if (lota != null) {
			AcessoConsulta ac = new AcessoConsulta(0L, lota.getIdInicial(), 
					0L, lota.getOrgaoUsuario().getId());
			if (ac.podeAcessar(mob.doc(), null, lota)) 
				mar.setDpLotacaoIni(lota.getLotacaoInicial());
		}
		mar.setDtIniMarca(dt);
		set.add(mar);
	}

	private void acrescentarMarcaTransferencia(Long idMarcador, Date dtIni, Date dtFim, DpPessoa pess, DpLotacao lota, ExMovimentacao mov) {
		ExMarca mar = new ExMarca();
		mar.setExMobil(mob);
		mar.setCpMarcador(ExDao.getInstance().consultar(idMarcador, CpMarcador.class, false));
		mar.setExMovimentacao(mov);
		if (pess != null)
			mar.setDpPessoaIni(pess.getPessoaInicial());
		if (lota != null) {
			AcessoConsulta ac = new AcessoConsulta(0L, lota.getIdInicial(), 
					0L, lota.getOrgaoUsuario().getId());
			if (ac.podeAcessar(mob.doc(), null, lota)) 
				mar.setDpLotacaoIni(lota.getLotacaoInicial());
		}
		mar.setDtIniMarca(dtIni);
		mar.setDtFimMarca(dtFim);
		set.add(mar);
	}

	public ExMovimentacao contemTransferenciaRetorno(ExMovimentacao mov, ExMobil mob) {
		ExMovimentacao movRetorno = null;
		List<ExMovimentacao> transferencias = mob.getMovimentacoesPorTipo(3, false);
		transferencias.addAll(mob.getMovimentacoesPorTipo(6, false));
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
