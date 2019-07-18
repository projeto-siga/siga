package br.gov.jfrj.siga.ex.bl;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crivano.swaggerservlet.ISwaggerModel;

import br.gov.jfrj.siga.base.Data;
import br.gov.jfrj.siga.base.SigaBaseProperties;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExMarca;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.hibernate.ExDao;

public class Mesa {

	public static class MesaItem implements ISwaggerModel {
		public String tipo;
		public Date datahora;
		public String tempoRelativo;
		public String codigo;
		public String sigla;
		public String descr;
		public String origem;
		public String grupo;
		public String grupoNome;
		public String grupoIcone;
		public String grupoOrdem;
		public String dataDevolucao;
		public List<Marca> list;
	}

	public static class Marca implements ISwaggerModel {
		public String pessoa;
		public String lotacao;
		public String nome;
		public String icone;
		public String titulo;
		public Date inicio;
		public Date termino;
		public Boolean daPessoa;
		public Boolean deOutraPessoa;
		public Boolean daLotacao;
	}

	public enum TipoDePainelEnum {
		PESSOAL, UNIDADE;
	}

	public enum GrupoDeMarcadorEnum {
		ALERTA("Alertas", "fas fa-hourglass-end"),
		//
		A_REVISAR("Pendente de Revisão", "fas fa-glasses"),
		//
		A_ASSINAR("Pendente de Assinatura", "fas fa-key"),
		//
		CAIXA_DE_ENTRADA("Caixa de Entrada", "fas fa-inbox"),
		//
		EM_ELABORACAO("Em Elaboração", "fas fa-lightbulb"),
		//
		AGUARDANDO_ANDAMENTO("Aguardando Andamento", "fas fa-clock"),
		//
		CAIXA_DE_SAIDA("Caixa de Saída", "fas fa-inbox"),
		//
		ACOMPANHANDO("Acompanhando", "fas fa-tags"),
		//
		MONITORANDO("Monitorando", "fas fa-hourglass-half"),
		//
		AGUARDANDO_ACAO_DE_TEMPORALIDADE("Aguardando Ação de Temporalidade",
				"fas fa-hourglass-half"),
		//
		OUTROS("Outros", "fas fa-inbox"),
		//
		QUALQUER("Qualquer", "fas fa-inbox"),
		//
		NENHUM("Nenhum", "fas fa-inbox");

		private final String nome;
		private final String icone;

		private GrupoDeMarcadorEnum(String nome, String icone) {
			this.nome = nome;
			this.icone = icone;
		}
	}

	public enum MarcadorEnum {
		//
		EM_ELABORACAO(1, "Em Elaboração", "fas fa-lightbulb", "",
				GrupoDeMarcadorEnum.EM_ELABORACAO),
		//
		EM_ANDAMENTO(2, "Aguardando Andamento", "fas fa-clock", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		A_RECEBER(3, "A Receber", "fas fa-inbox", "",
				GrupoDeMarcadorEnum.CAIXA_DE_ENTRADA),
		//
		EXTRAVIADO(4, "Extraviado", "fas fa-inbox", "", GrupoDeMarcadorEnum.ALERTA),
		//
		A_ARQUIVAR(5, "A Arquivar", "fas fa-inbox", "", GrupoDeMarcadorEnum.OUTROS),
		//
		ARQUIVADO_CORRENTE(6, "Arquivado Corrente", "fas fa-inbox", "",
				GrupoDeMarcadorEnum.OUTROS),
		//
		A_ELIMINAR(7, "A Eliminar", "fas fa-inbox", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ACAO_DE_TEMPORALIDADE),
		//
		ELIMINADO(8, "Eliminado", "fas fa-power-off", "", GrupoDeMarcadorEnum.OUTROS),
		//
		JUNTADO(9, "Juntado", "fas fa-lock", "", GrupoDeMarcadorEnum.OUTROS),
		//
		JUNTADO_EXTERNO(16, "Juntado Externo", "fas fa-lock", "",
				GrupoDeMarcadorEnum.OUTROS),
		//
		CANCELADO(10, "Cancelado", "fas fa-ban", "", GrupoDeMarcadorEnum.OUTROS),
		//
		TRANSFERIDO_A_ORGAO_EXTERNO(11, "Tranferido a Órgão Externo", "fas fa-paper-plane",
				"", GrupoDeMarcadorEnum.OUTROS),

		//
		ARQUIVADO_INTERMEDIARIO(12, "Arquivado Intermediário", "fas fa-inbox", "",
				GrupoDeMarcadorEnum.OUTROS),
		//
		CAIXA_DE_ENTRADA(14, "A Receber", "fas fa-inbox", "",
				GrupoDeMarcadorEnum.CAIXA_DE_ENTRADA),
		//
		ARQUIVADO_PERMANENTE(13, "Arquivado Permanente", "fas fa-inbox", "",
				GrupoDeMarcadorEnum.OUTROS),
		//
		PENDENTE_DE_ASSINATURA(15, "Pendente de Assinatura", "fas fa-key", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		JUNTADO_A_DOCUMENTO_EXTERNO(16, "Juntado a Documento Externo", "fas fa-inbox",
				"", GrupoDeMarcadorEnum.OUTROS),
		//
		A_REMETER_PARA_PUBLICACAO(17, "A Remeter para Publicação", "fas fa-scroll", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),

		//
		REMETIDO_PARA_PUBLICACAO(18, "Remetido para Publicação", "fas fa-scroll", "",
				GrupoDeMarcadorEnum.OUTROS),
		//
		A_REMETER_MANUALMENTE(19, "A Remeter Manualmente", "fas fa-scroll", "",
				GrupoDeMarcadorEnum.OUTROS),
		//
		PUBLICADO(20, "Publicado", "fas fa-scroll", "", GrupoDeMarcadorEnum.OUTROS),
		//
		PUBLICACAO_SOLICITADA(21, "Publicação Solicitada", "fas fa-scroll", "",
				GrupoDeMarcadorEnum.OUTROS),
		//
		DISPONIBILIZADO(22, "Disponibilizado", "fas fa-scroll", "",
				GrupoDeMarcadorEnum.OUTROS),

		//
		EM_TRANSITO(23, "Em Trâmite Físico", "fas fa-truck", "",
				GrupoDeMarcadorEnum.CAIXA_DE_SAIDA),
		//
		EM_TRANSITO_ELETRONICO(24, "Em Trâmite", "fas fa-shipping-fast", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		COMO_SUBSCRITOR(25, "Como Subscritor", "fas fa-key", "",
				GrupoDeMarcadorEnum.A_ASSINAR),
		//
		APENSADO(26, "Apensado", "fas fa-compress-arrows-alt", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		MARCADOR_COMO_GESTOR(27, "Gestor", "fas fa-tag", "",
				GrupoDeMarcadorEnum.ACOMPANHANDO),

		//
		MARCADOR_COMO_INTERESSADO(28, "Interessado", "fas fa-tag", "",
				GrupoDeMarcadorEnum.ACOMPANHANDO),
		//
		DESPACHO_PENDENTE_DE_ASSINATURA(29, "Despacho Pendente de Assinatura",
				"fas fa-key", "", GrupoDeMarcadorEnum.ALERTA),
		//
		ANEXO_PENDENTE_DE_ASSINATURA(30, "Anexo Pendente de Assinatura", "fas fa-key",
				"", GrupoDeMarcadorEnum.ALERTA),
		//
		SOBRESTADO(31, "Sobrestado", "fas fa-hourglass-start", "",
				GrupoDeMarcadorEnum.ACOMPANHANDO),
		//
		SEM_EFEITO(32, "Sem Efeito", "fas fa-power-off", "",
				GrupoDeMarcadorEnum.NENHUM),

		//
		ATIVO(36, "Ativo", "inbox", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		NOVO(37, "Novo", "inbox", "", GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		POPULAR(38, "Popular", "inbox", "", GrupoDeMarcadorEnum.ALERTA),
		//
		REVISAR(39, "A Revisar", "fas fa-glasses", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		TOMAR_CIENCIA(40, "Tomar Ciência", "inbox", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),

		//
		SOLICITACAO_A_RECEBER(41, "A Receber", "inbox", "",
				GrupoDeMarcadorEnum.CAIXA_DE_ENTRADA),
		//
		SOLICITACAO_EM_ANDAMENTO(42, "Em Andamento", "inbox", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		SOLICITACAO_FECHADO(43, "Fechado", "inbox", "",
				GrupoDeMarcadorEnum.OUTROS),
		//
		SOLICITACAO_PENDENTE(44, "Pendente", "inbox", "",
				GrupoDeMarcadorEnum.OUTROS),
		//
		SOLICITACAO_CANCELADO(45, "Cancelado", "inbox", "",
				GrupoDeMarcadorEnum.NENHUM),

		//
		SOLICITACAO_PRE_ATENDIMENTO(46, "Pré-atendimento", "inbox", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		SOLICITACAO_POS_ATENDIMENTO(47, "Pós-atendimento", "inbox", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		SOLICITACAO_COMO_CADASTRANTE(48, "Cadastrante", "inbox", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		SOLICITACAO_COMO_SOLICITANTE(49, "Solicitante", "inbox", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		RECOLHER_PARA_ARQUIVO_PERMANENTE(50, "Recolher Arq. Permante", "fas fa-inbox",
				"", GrupoDeMarcadorEnum.AGUARDANDO_ACAO_DE_TEMPORALIDADE),

		//
		TRANSFERIR_PARA_ARQUIVO_INTERMEDIARIO(51,
				"Transferir Arq. Intermediário", "fas fa-inbox", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ACAO_DE_TEMPORALIDADE),
		//
		EM_EDITAL_DE_ELIMINACAO(52, "Em Edital de Eliminação", "fas fa-inbox", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		SOLICITACAO_FECHADO_PARCIAL(53, "Fechado Parcial", "inbox", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		SOLICITACAO_EM_CONTROLE_QUALIDADE(54, "Em Controle de Qualidade",
				"inbox", "", GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		A_DEVOLVER(56, "A Devolver", "fas fa-exchange-alt", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),

		//
		AGUARDANDO(57, "Aguardando", "fas fa-clock", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		A_DEVOLVER_FORA_DO_PRAZO(58, "A Devolver Fora do Prazo", "fas fa-exchange-alt", "",
				GrupoDeMarcadorEnum.ALERTA),
		//
		AGUARDANDO_DEVOLUCAO_FORA_DO_PRAZO(59,
				"Aguardando Devolução Fora Do Prazo", "fas fa-exchange-alt", "",
				GrupoDeMarcadorEnum.ALERTA),
		//
		PENDENTE_DE_ANEXACAO(60, "Pendente de Anexação", "fas fa-arrow-alt-circle-up", "",
				GrupoDeMarcadorEnum.ALERTA),
		//
		SOLICITACAO_EM_ELABORACAO(61, "Em Elaboração", "inbox", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),

		//
		DOCUMENTO_ASSINADO_COM_SENHA(62, "Assinado com Senha", "fas fa-key", "",
				GrupoDeMarcadorEnum.NENHUM),
		//
		MOVIMENTACAO_ASSINADA_COM_SENHA(63, "Movimentação Ass. com Senha",
				"fas fa-key", "", GrupoDeMarcadorEnum.NENHUM),
		//
		MOVIMENTACAO_CONFERIDA_COM_SENHA(64,
				"Movimentação Autenticada com Senha", "fas fa-key", "",
				GrupoDeMarcadorEnum.NENHUM),
		//
		SOLICITACAO_FORA_DO_PRAZO(65, "Fora do Prazo", "inbox", "",
				GrupoDeMarcadorEnum.ALERTA),
		//
		SOLICITACAO_ATIVO(66, "Ativo", "inbox", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),

		//
		PENDENTE_DE_COLABORACAO(67, "Pendente de Colaboração", "fas fa-users-cog", "",
				GrupoDeMarcadorEnum.CAIXA_DE_ENTRADA),
		//
		FINALIZAR_DOCUMENTO_COLABORATIVO(68,
				"Finalizar Documento Colaborativo", "fas fa-users-cog", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		SOLICITACAO_NECESSITA_PROVIDENCIA(69, "Necessita Providência", "inbox",
				"", GrupoDeMarcadorEnum.ALERTA),
		//
		COMO_EXECUTOR(70, "Executor", "inbox", "",
				GrupoDeMarcadorEnum.ACOMPANHANDO),
		//
		MARCADOR_PRONTO_PARA_ASSINAR(71, "Pronto para Assinar", "fas fa-check-circle", "",
				GrupoDeMarcadorEnum.A_ASSINAR),
		//
		MARCADOR_COMO_REVISOR(72, "Como Revisor", "fas fa-glasses", "",
				GrupoDeMarcadorEnum.A_REVISAR),
		//
		URGENTE(1000, "Urgente", "fas fa-bomb", "", GrupoDeMarcadorEnum.ALERTA),

		//
		IDOSO(1001, "Idoso", "fas fa-user-tag", "", GrupoDeMarcadorEnum.ALERTA),

		//
		RETENCAO_INSS(1002, "Retenção de INSS", "fas fa-tag", "",
				GrupoDeMarcadorEnum.ALERTA),
		//
		PRIORITARIO(1003, "Prioritário", "fas fa-star", "", GrupoDeMarcadorEnum.ALERTA),
		//		
		RESTRICAO_ACESSO(1004, "Restrição de Acesso", "fas fa-user-secret", "", GrupoDeMarcadorEnum.ALERTA);
		//		

		private MarcadorEnum(int id, String nome, String icone,
				String descricao, GrupoDeMarcadorEnum grupo) {
			this.id = id;
			this.nome = nome;
			this.icone = icone;
			this.descricao = descricao;
			this.grupo = grupo;
		}

		public static MarcadorEnum getById(int id) {
			for (MarcadorEnum i : MarcadorEnum.values()) {
				if (i.id == id)
					return i;
			}
			return null;
		}

		public String getIcone() {
			return icone;
		}

		public String getNome() {
			if (SigaMessages.isSigaSP() && nome.equals("Como Subscritor")) {
				return "Responsável pela Assinatura";
			} else {
				return nome;
			}
		}

		private final int id;
		private final String nome;
		private final String icone;
		private final String descricao;
		private final GrupoDeMarcadorEnum grupo;

	}

	public static class MeM {
		ExMarca marca;;
		CpMarcador marcador;
	}

	public static List<MesaItem> listarReferencias(TipoDePainelEnum tipo,
			Map<ExMobil, List<MeM>> references, DpPessoa pessoa,
			DpLotacao unidade, Date currentDate) {
		List<MesaItem> l = new ArrayList<>();

		for (ExMobil mobil : references.keySet()) {
			MesaItem r = new MesaItem();
			r.tipo = "Documento";

			Date datahora = null;
			if (mobil.getUltimaMovimentacao() != null)
				datahora = mobil.getUltimaMovimentacao().getDtIniMov();
			else
				datahora = mobil.getDoc().getDtAltDoc();
			r.datahora = datahora;
			r.tempoRelativo = Data.calcularTempoRelativo(datahora);

			r.codigo = mobil.getCodigoCompacto();
			r.sigla = mobil.getSigla();
			r.descr = mobil.doc().getDescrCurta(80);

			if (mobil.doc().getSubscritor() != null
					&& mobil.doc().getSubscritor().getLotacao() != null)

				if (SigaMessages.isSigaSP()) {
					r.origem = mobil.doc().getSubscritor().getLotacao()
							.getOrgaoUsuario()
							+ "/"
							+ mobil.doc().getSubscritor().getLotacao()
									.getSigla();
				} else {
					r.origem = mobil.doc().getSubscritor().getLotacao()
							.getSigla();
				}

			r.dataDevolucao = "ocultar";

			if (mobil.getUltimaMovimentacaoNaoCancelada() != null
					&& mobil.getUltimaMovimentacaoNaoCancelada().getDtFimMov() != null) {

				Date dataMovimentacao = mobil.getUltimaMovimentacaoNaoCancelada()
						.getDtFimMov();
				Date dataHoje = new Timestamp(System.currentTimeMillis());

				int dias;

				if (dataHoje.compareTo(dataMovimentacao) <= 0) {
					dias = Integer
							.parseInt(((dataMovimentacao.getTime() - dataHoje.getTime() - +3600000L) / 86400000L)
									+ "");

					String qtdDias = SigaBaseProperties
							.getString("siga.qtdDiasDevolucao");
					
					if(qtdDias == null){
						qtdDias = "5";
					}
					
					if (dias <= Integer.parseInt(qtdDias)) {
						r.dataDevolucao = "alerta";
					}

				} else {
					r.dataDevolucao = "atrasado";
				}

			}

			r.grupo = GrupoDeMarcadorEnum.NENHUM.name();
			r.grupoOrdem = Integer.toString(GrupoDeMarcadorEnum
					.valueOf(r.grupo).ordinal());
			r.grupoNome = GrupoDeMarcadorEnum.valueOf(r.grupo).nome;
			r.grupoIcone = GrupoDeMarcadorEnum.valueOf(r.grupo).icone;

			r.list = new ArrayList<Marca>();

			for (MeM tag : references.get(mobil)) {
				if (tag.marca.getDtIniMarca() != null
						&& tag.marca.getDtIniMarca().getTime() > currentDate
								.getTime())
					continue;
				if (tag.marca.getDtFimMarca() != null
						&& tag.marca.getDtFimMarca().getTime() < currentDate
								.getTime())
					continue;

				Marca t = new Marca();
				MarcadorEnum mar = MarcadorEnum.getById(tag.marcador
						.getIdMarcador().intValue());

				t.nome = mar.getNome();
				t.icone = mar.getIcone();
				t.titulo = Data
						.calcularTempoRelativo(tag.marca.getDtIniMarca());

				if (tag.marca.getDpPessoaIni() != null) {
					DpPessoa pes = tag.marca.getDpPessoaIni().getPessoaAtual();
					if (pes.getNomeExibicao() != null)
						t.pessoa = pes.getNomeExibicao();
				}
				if (tag.marca.getDpLotacaoIni() != null)
					t.lotacao = tag.marca.getDpLotacaoIni().getLotacaoAtual()
							.getSigla();
				t.inicio = tag.marca.getDtIniMarca();
				t.termino = tag.marca.getDtFimMarca();

				if (GrupoDeMarcadorEnum.valueOf(r.grupo).ordinal() > mar.grupo
						.ordinal()) {
					r.grupo = mar.grupo.name();
					r.grupoOrdem = Integer.toString(mar.grupo.ordinal());
					r.grupoNome = mar.grupo.nome;
					r.grupoIcone = mar.grupo.icone;
				}

				r.list.add(t);
				if (pessoa != null && tag.marca.getDpPessoaIni() != null) {
					if (pessoa.getIdInicial().equals(
							tag.marca.getDpPessoaIni().getId()))
						t.daPessoa = true;
					else
						t.deOutraPessoa = tag.marca.getDpPessoaIni() != null;
				}
				if (unidade != null
						&& tag.marca.getDpLotacaoIni() != null
						&& unidade.getId().equals(
								tag.marca.getDpLotacaoIni().getId()))
					t.daLotacao = true;
			}
			l.add(r);
		}

		Collections.sort(l, new Comparator<MesaItem>() {
			@Override
			public int compare(MesaItem o1, MesaItem o2) {
				int i = Integer.compare(Integer.parseInt(o1.grupoOrdem),
						Integer.parseInt(o2.grupoOrdem));
				if (i != 0)
					return i;
				i = o2.datahora.compareTo(o1.datahora);
				if (i != 0)
					return i;
				return 0;
			}
		});
		return l;
	}

	public static List<MesaItem> getMesa(ExDao dao, DpPessoa titular,
			DpLotacao lotaTitular) {
		List<Object[]> l = dao.listarDocumentosPorPessoaOuLotacao(titular,
				lotaTitular);

		HashMap<ExMobil, List<MeM>> map = new HashMap<>();

		for (Object[] reference : l) {
			ExMarca marca = (ExMarca) reference[0];
			CpMarcador marcador = (CpMarcador) reference[1];
			ExMobil mobil = (ExMobil) reference[2];

			if (!map.containsKey(mobil))
				map.put(mobil, new ArrayList<MeM>());
			MeM mm = new MeM();
			mm.marca = marca;
			mm.marcador = marcador;
			map.get(mobil).add(mm);
		}

		if (SigaBaseProperties.getBooleanValue("siga.mesa.carrega.lotacao")) {
			List<Object[]> lLota = dao.listarDocumentosCxEntradaPorPessoaOuLotacao(null,
					lotaTitular);
	
			for (Object[] reference : lLota) {
				ExMarca marca = (ExMarca) reference[0];
				if (marca.getCpMarcador().getIdMarcador() == CpMarcador.MARCADOR_CAIXA_DE_ENTRADA) {
					CpMarcador marcador = (CpMarcador) reference[1];
					ExMobil mobil = (ExMobil) reference[2];
		
					if (!map.containsKey(mobil))
						map.put(mobil, new ArrayList<MeM>());
					MeM mm = new MeM();
					mm.marca = marca;
					mm.marcador = marcador;
					map.get(mobil).add(mm);
				}
			}
		}
			
		return Mesa.listarReferencias(TipoDePainelEnum.UNIDADE, map, titular,
				titular.getLotacao(), dao.consultarDataEHoraDoServidor());
	}

}
