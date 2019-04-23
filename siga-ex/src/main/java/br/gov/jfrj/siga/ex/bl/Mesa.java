package br.gov.jfrj.siga.ex.bl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crivano.swaggerservlet.ISwaggerModel;

import br.gov.jfrj.siga.base.Data;
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
		public String grupoOrdem;
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
		ALERTA("Alertas", "hourglass-end"),
		//
		A_ASSINAR("Pendente de Assinatura", "inbox"),
		//
		CAIXA_DE_ENTRADA("Caixa de Entrada", "inbox"),
		//
		EM_ELABORACAO("Em Elaboração", "inbox"),
		//
		AGUARDANDO_ANDAMENTO("Aguardando Andamento", "lightbulb"),
		//
		CAIXA_DE_SAIDA("Caixa de Saída", "inbox"),
		//
		ACOMPANHANDO("Acompanhando", "key"),
		//
		MONITORANDO("Monitorando", "hourglass-half"),
		//
		AGUARDANDO_ACAO_DE_TEMPORALIDADE("Aguardando Ação de Temporalidade", "hourglass-half"),
		//
		OUTROS("Outros", "paper-plane"),
		//
		QUALQUER("Qualquer", "inbox"),
		//
		NENHUM("Nenhum", "inbox");

		private final String nome;
		private final String icone;

		private GrupoDeMarcadorEnum(String nome, String icone) {
			this.nome = nome;
			this.icone = icone;
		}
	}

	public enum MarcadorEnum {
		//
		EM_ELABORACAO(1, "Em Elaboração", "lightbulb", "", GrupoDeMarcadorEnum.EM_ELABORACAO),
		//
		EM_ANDAMENTO(2, "Aguardando Andamento", "clock-o", "", GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		A_RECEBER(3, "A Receber", "inbox", "", GrupoDeMarcadorEnum.CAIXA_DE_ENTRADA),
		//
		EXTRAVIADO(4, "Extraviado", "inbox", "", GrupoDeMarcadorEnum.ALERTA),
		//
		A_ARQUIVAR(5, "A Arquivar", "inbox", "", GrupoDeMarcadorEnum.OUTROS),
		//
		ARQUIVADO_CORRENTE(6, "Arquivado Corrente", "inbox", "", GrupoDeMarcadorEnum.OUTROS),
		//
		A_ELIMINAR(7, "A Eliminar", "inbox", "", GrupoDeMarcadorEnum.AGUARDANDO_ACAO_DE_TEMPORALIDADE),
		//
		ELIMINADO(8, "Eliminado", "power-off", "", GrupoDeMarcadorEnum.OUTROS),
		//
		JUNTADO(9, "Juntado", "lock", "", GrupoDeMarcadorEnum.OUTROS),
		//
		JUNTADO_EXTERNO(16, "Juntado Externo", "lock", "", GrupoDeMarcadorEnum.OUTROS),
		//
		CANCELADO(10, "Cancelado", "ban", "", GrupoDeMarcadorEnum.OUTROS),
		//
		TRANSFERIDO_A_ORGAO_EXTERNO(11, "Tranferido a Órgão Externo", "inbox", "", GrupoDeMarcadorEnum.OUTROS),

		//
		ARQUIVADO_INTERMEDIARIO(12, "Arquivado Intermediário", "inbox", "", GrupoDeMarcadorEnum.OUTROS),
		//
		CAIXA_DE_ENTRADA(14, "A Receber", "inbox", "", GrupoDeMarcadorEnum.CAIXA_DE_ENTRADA),
		//
		ARQUIVADO_PERMANENTE(13, "Arquivado Permanente", "inbox", "", GrupoDeMarcadorEnum.OUTROS),
		//
		PENDENTE_DE_ASSINATURA(15, "Pendente de Assinatura", "key", "", GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		JUNTADO_A_DOCUMENTO_EXTERNO(16, "Juntado a Documento Externo", "inbox", "", GrupoDeMarcadorEnum.OUTROS),
		//
		A_REMETER_PARA_PUBLICACAO(17, "A Remeter para Publicação", "inbox", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),

		//
		REMETIDO_PARA_PUBLICACAO(18, "Remetido para Publicação", "inbox", "", GrupoDeMarcadorEnum.OUTROS),
		//
		A_REMETER_MANUALMENTE(19, "A Remeter Manualmente", "inbox", "", GrupoDeMarcadorEnum.OUTROS),
		//
		PUBLICADO(20, "Publicado", "inbox", "", GrupoDeMarcadorEnum.OUTROS),
		//
		PUBLICACAO_SOLICITADA(21, "Publicação Solicitada", "inbox", "", GrupoDeMarcadorEnum.OUTROS),
		//
		DISPONIBILIZADO(22, "Disponibilizado", "inbox", "", GrupoDeMarcadorEnum.OUTROS),

		//
		EM_TRANSITO(23, "Em Trâmite Físico", "inbox", "", GrupoDeMarcadorEnum.CAIXA_DE_SAIDA),
		//
		EM_TRANSITO_ELETRONICO(24, "Em Trâmite", "inbox", "", GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		COMO_SUBSCRITOR(25, "Como Subscritor", "key", "", GrupoDeMarcadorEnum.A_ASSINAR),
		//
		APENSADO(26, "Apensado", "inbox", "", GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		MARCADOR_COMO_GESTOR(27, "Gestor", "pushpin", "", GrupoDeMarcadorEnum.ACOMPANHANDO),

		//
		MARCADOR_COMO_INTERESSADO(28, "Interessado", "pushpin", "", GrupoDeMarcadorEnum.ACOMPANHANDO),
		//
		DESPACHO_PENDENTE_DE_ASSINATURA(29, "Despacho Pendente de Assinatura", "key", "", GrupoDeMarcadorEnum.ALERTA),
		//
		ANEXO_PENDENTE_DE_ASSINATURA(30, "Anexo Pendente de Assinatura", "key", "", GrupoDeMarcadorEnum.ALERTA),
		//
		SOBRESTADO(31, "Sobrestado", "hourglass-1", "", GrupoDeMarcadorEnum.ACOMPANHANDO),
		//
		SEM_EFEITO(32, "Sem Efeito", "power-off", "", GrupoDeMarcadorEnum.NENHUM),

		//
		ATIVO(36, "Ativo", "inbox", "", GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		NOVO(37, "Novo", "inbox", "", GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		POPULAR(38, "Popular", "inbox", "", GrupoDeMarcadorEnum.ALERTA),
		//
		REVISAR(39, "A Revisar", "search", "", GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		TOMAR_CIENCIA(40, "Tomar Ciência", "inbox", "", GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),

		//
		SOLICITACAO_A_RECEBER(41, "A Receber", "inbox", "", GrupoDeMarcadorEnum.CAIXA_DE_ENTRADA),
		//
		SOLICITACAO_EM_ANDAMENTO(42, "Em Andamento", "inbox", "", GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		SOLICITACAO_FECHADO(43, "Fechado", "inbox", "", GrupoDeMarcadorEnum.OUTROS),
		//
		SOLICITACAO_PENDENTE(44, "Pendente", "inbox", "", GrupoDeMarcadorEnum.OUTROS),
		//
		SOLICITACAO_CANCELADO(45, "Cancelado", "inbox", "", GrupoDeMarcadorEnum.NENHUM),

		//
		SOLICITACAO_PRE_ATENDIMENTO(46, "Pré-atendimento", "inbox", "", GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		SOLICITACAO_POS_ATENDIMENTO(47, "Pós-atendimento", "inbox", "", GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		SOLICITACAO_COMO_CADASTRANTE(48, "Cadastrante", "inbox", "", GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		SOLICITACAO_COMO_SOLICITANTE(49, "Solicitante", "inbox", "", GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		RECOLHER_PARA_ARQUIVO_PERMANENTE(50, "Recolher Arq. Permante", "inbox", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ACAO_DE_TEMPORALIDADE),

		//
		TRANSFERIR_PARA_ARQUIVO_INTERMEDIARIO(51, "Transferir Arq. Intermediário", "inbox", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ACAO_DE_TEMPORALIDADE),
		//
		EM_EDITAL_DE_ELIMINACAO(52, "Em Edital de Eliminação", "inbox", "", GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		SOLICITACAO_FECHADO_PARCIAL(53, "Fechado Parcial", "inbox", "", GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		SOLICITACAO_EM_CONTROLE_QUALIDADE(54, "Em Controle de Qualidade", "inbox", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		A_DEVOLVER(56, "A Devolver", "inbox", "", GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),

		//
		AGUARDANDO(57, "Aguardando", "inbox", "", GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		A_DEVOLVER_FORA_DO_PRAZO(58, "A Devolver Fora do Prazo", "inbox", "", GrupoDeMarcadorEnum.ALERTA),
		//
		AGUARDANDO_DEVOLUCAO_FORA_DO_PRAZO(59, "Aguardando Devolução Fora Do Prazo", "inbox", "",
				GrupoDeMarcadorEnum.ALERTA),
		//
		PENDENTE_DE_ANEXACAO(60, "Pendente de Anexação", "inbox", "", GrupoDeMarcadorEnum.ALERTA),
		//
		SOLICITACAO_EM_ELABORACAO(61, "Em Elaboração", "inbox", "", GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),

		//
		DOCUMENTO_ASSINADO_COM_SENHA(62, "Assinado com Senha", "inbox", "", GrupoDeMarcadorEnum.NENHUM),
		//
		MOVIMENTACAO_ASSINADA_COM_SENHA(63, "Movimentação Ass. com Senha", "inbox", "", GrupoDeMarcadorEnum.NENHUM),
		//
		MOVIMENTACAO_CONFERIDA_COM_SENHA(64, "Movimentação Autenticada com Senha", "inbox", "",
				GrupoDeMarcadorEnum.NENHUM),
		//
		SOLICITACAO_FORA_DO_PRAZO(65, "Fora do Prazo", "inbox", "", GrupoDeMarcadorEnum.ALERTA),
		//
		SOLICITACAO_ATIVO(66, "Ativo", "inbox", "", GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),

		//
		PENDENTE_DE_COLABORACAO(67, "Pendente de Colaboração", "inbox", "", GrupoDeMarcadorEnum.CAIXA_DE_ENTRADA),
		//
		FINALIZAR_DOCUMENTO_COLABORATIVO(68, "Finalizar Documento Colaborativo", "inbox", "",
				GrupoDeMarcadorEnum.AGUARDANDO_ANDAMENTO),
		//
		SOLICITACAO_NECESSITA_PROVIDENCIA(69, "Necessita Providência", "inbox", "", GrupoDeMarcadorEnum.ALERTA),
		//
		COMO_EXECUTOR(70, "Executor", "inbox", "", GrupoDeMarcadorEnum.ACOMPANHANDO),
		//
		MARCADOR_PRONTO_PARA_ASSINAR(71, "Pronto para Assinar", "key", "", GrupoDeMarcadorEnum.A_ASSINAR),
		//
		URGENTE(1000, "Urgente", "inbox", "", GrupoDeMarcadorEnum.ALERTA),

		//
		IDOSO(1001, "Idoso", "inbox", "", GrupoDeMarcadorEnum.ALERTA),

		//
		RETENCAO_INSS(1002, "Retenção de INSS", "inbox", "", GrupoDeMarcadorEnum.ALERTA);

		private MarcadorEnum(int id, String nome, String icone, String descricao, GrupoDeMarcadorEnum grupo) {
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
			return nome;
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

	public static List<MesaItem> listarReferencias(TipoDePainelEnum tipo, Map<ExMobil, List<MeM>> references,
			DpPessoa pessoa, DpLotacao unidade, Date currentDate) {
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
			r.descr = mobil.doc().getDescrCurta();

			if (mobil.doc().getSubscritor() != null && mobil.doc().getSubscritor().getLotacao() != null)
				r.origem = mobil.doc().getSubscritor().getLotacao().getSigla();

			r.grupo = GrupoDeMarcadorEnum.NENHUM.name();
			r.grupoOrdem = Integer.toString(GrupoDeMarcadorEnum.valueOf(r.grupo).ordinal());
			r.grupoNome = GrupoDeMarcadorEnum.valueOf(r.grupo).nome;

			r.list = new ArrayList<Marca>();

			for (MeM tag : references.get(mobil)) {
				if (tag.marca.getDtIniMarca() != null && tag.marca.getDtIniMarca().getTime() > currentDate.getTime())
					continue;
				if (tag.marca.getDtFimMarca() != null && tag.marca.getDtFimMarca().getTime() < currentDate.getTime())
					continue;

				Marca t = new Marca();
				MarcadorEnum mar = MarcadorEnum.getById(tag.marcador.getIdMarcador().intValue());

				t.nome = mar.getNome();
				t.icone = mar.getIcone();
				t.titulo = Data.calcularTempoRelativo(tag.marca.getDtIniMarca());

				if (tag.marca.getDpPessoaIni() != null) {
					DpPessoa pes = tag.marca.getDpPessoaIni().getPessoaAtual();
					if (pes.getNomeExibicao() != null)
						t.pessoa = pes.getNomeExibicao();
				}
				if (tag.marca.getDpLotacaoIni() != null)
					t.lotacao = tag.marca.getDpLotacaoIni().getLotacaoAtual().getSigla();
				t.inicio = tag.marca.getDtIniMarca();
				t.termino = tag.marca.getDtFimMarca();

				if (GrupoDeMarcadorEnum.valueOf(r.grupo).ordinal() > mar.grupo.ordinal()) {
					r.grupo = mar.grupo.name();
					r.grupoOrdem = Integer.toString(mar.grupo.ordinal());
					r.grupoNome = mar.grupo.nome;
				}

				r.list.add(t);
				if (pessoa != null && tag.marca.getDpPessoaIni() != null) {
					if (pessoa.getIdInicial().equals(tag.marca.getDpPessoaIni().getId()))
						t.daPessoa = true;
					else
						t.deOutraPessoa = tag.marca.getDpPessoaIni() != null;
				}
				if (unidade != null && tag.marca.getDpLotacaoIni() != null
						&& unidade.getId().equals(tag.marca.getDpLotacaoIni().getId()))
					t.daLotacao = true;
			}
			l.add(r);
		}

		Collections.sort(l, new Comparator<MesaItem>() {
			@Override
			public int compare(MesaItem o1, MesaItem o2) {
				int i = Integer.compare(Integer.parseInt(o1.grupoOrdem), Integer.parseInt(o2.grupoOrdem));
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

	public static List<MesaItem> getMesa(ExDao dao, DpPessoa titular, DpLotacao lotaTitular) {
		List<Object[]> l = dao.listarDocumentosPorPessoaOuLotacao(titular, lotaTitular);

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

		return Mesa.listarReferencias(TipoDePainelEnum.UNIDADE, map, titular, titular.getLotacao(),
				dao.consultarDataEHoraDoServidor());
	}

}
