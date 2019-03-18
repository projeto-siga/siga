package br.gov.jfrj.siga.ex;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.ex.bl.Ex;
import br.gov.jfrj.siga.ex.bl.ExBL;
import br.gov.jfrj.siga.ex.bl.ExCompetenciaBL;
import br.gov.jfrj.siga.hibernate.ExDao;

public class ExEditalEliminacao {

	private static ExDao dao() {
		return ExDao.getInstance();
	}

	private static ExBL bl() {
		return Ex.getInstance().getBL();
	}

	private static ExCompetenciaBL comp() {
		return Ex.getInstance().getComp();
	}

	private ExEditalComparator editalComparator;

	private ExDocumento doc;

	private String msgErro;

	private int quantidadeDisponiveisEntrevista = -1;

	public ExDocumento getDoc() {
		return doc;
	}

	public void setDoc(ExDocumento edital) {
		this.doc = edital;
	}

	public Date getDtIniEntrevista() {
		try {
			return new SimpleDateFormat("dd/MM/yyyy").parse(getDoc().getForm()
					.get("dtIni"));
		} catch (Exception e) {
			return null;
		}
	}

	public Date getDtFimEntrevista() {
		try {
			return new SimpleDateFormat("dd/MM/yyyy").parse(getDoc().getForm()
					.get("dtFim"));
		} catch (Exception e) {
			return null;
		}
	}

	public boolean isListar() {
		return Boolean.parseBoolean(getDoc().getForm().get("listar"));
	}

	public String getMsgErro() {
		return msgErro;
	}

	public int getQuantidadeDisponiveis() {
		if (quantidadeDisponiveisEntrevista == -1) {
			quantidadeDisponiveisEntrevista = dao()
					.consultarQuantidadeAEliminar(getDoc().getSubscritor().getOrgaoUsuario(),
							getDtIniEntrevista(), getDtFimEntrevista());
		}
		return quantidadeDisponiveisEntrevista;
	}

	public ExEditalEliminacao() {
		this(null);
	}

	public ExEditalEliminacao(ExDocumento edital) {
		editalComparator = new ExEditalComparator();
		setDoc(edital);
	}

	public List<ExItemDestinacao> getEfetivamenteInclusos() {
		List<ExItemDestinacao> listaFinal = new ArrayList<ExItemDestinacao>();
		if (getDoc() != null)
			for (ExItemDestinacao o : dao().consultarEmEditalEliminacao(
					getDoc().getOrgaoUsuario(), null, null))
				if (o.getMob()
						.getUltimaMovimentacaoNaoCancelada(
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_EM_EDITAL_DE_ELIMINACAO,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_RETIRADA_DE_EDITAL_DE_ELIMINACAO)
						.getExMobilRef().equals(getDoc().getMobilGeral()))
					listaFinal.add(o);
		return listaFinal;
	}

	public List<ExItemDestinacao> getEfetivamenteInclusosDoPeriodo() {
		List<ExItemDestinacao> listaFinal = new ArrayList<ExItemDestinacao>();
		if (getDoc() != null)
			for (ExItemDestinacao o : dao().consultarEmEditalEliminacao(
					getDoc().getOrgaoUsuario(), getDtIniEntrevista(),
					getDtFimEntrevista()))
				if (o.getMob()
						.getUltimaMovimentacaoNaoCancelada(
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_EM_EDITAL_DE_ELIMINACAO,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_RETIRADA_DE_EDITAL_DE_ELIMINACAO)
						.getExMobilRef().equals(getDoc().getMobilGeral()))
					listaFinal.add(o);
		return listaFinal;
	}

	public List<ExMobil> getSelecionadosEntrevista() {
		List<ExMobil> lista = new ArrayList<ExMobil>();
		final Pattern p = Pattern.compile("^mob?([0-9]{1,10})");
		Map<String, String> form = getDoc().getForm();
		for (String chave : form.keySet()) {
			final Matcher m = p.matcher(chave);
			if (!m.find() || m.group(1) == null)
				continue;
			long l = Long.valueOf(m.group(1));
			ExMobil mob = dao().consultar(l, ExMobil.class, false);
			if (form.get(chave).equals("Sim"))
				lista.add(mob);
		}
		return lista;

	}

	@SuppressWarnings("unchecked")
	public List<ExTopicoDestinacao> getDisponiveisEntrevista() throws Exception {

		List<ExTopicoDestinacao> listaFinal = new ArrayList<ExTopicoDestinacao>();

		if (getDoc().getSubscritor() == null) {
			msgErro = "É necessário informar um subscritor para listar os documentos a eliminar do Órgão correspondente";
			return listaFinal;
		}

		if (getQuantidadeDisponiveis() > 1000) {
			msgErro = "Há "
					+ getQuantidadeDisponiveis()
					+ " documentos para o período informado. O máximo permitido é de 1000 documentos.";
			return listaFinal;
		}

		List<ExItemDestinacao> provisorio = new ArrayList<ExItemDestinacao>();
		provisorio.addAll(dao().consultarAEliminar(
				getDoc().getSubscritor().getOrgaoUsuario(),
				getDtIniEntrevista(), getDtFimEntrevista()));

		List<ExItemDestinacao> jaInclusos = getEfetivamenteInclusosDoPeriodo();
		quantidadeDisponiveisEntrevista += jaInclusos.size();
		provisorio.addAll(jaInclusos);

		if (provisorio.size() == 0) {
			msgErro = "Não há dados para o período informado.";
			return listaFinal;
		}

		Collections.sort(provisorio, editalComparator);

		ExTopicoDestinacao digitais = new ExTopicoDestinacao(
				"Documentos Digitais a Eliminar", true);
		ExTopicoDestinacao fisicos = new ExTopicoDestinacao(
				"Documentos Físicos a Eliminar", true);
		ExTopicoDestinacao indisponiveis = new ExTopicoDestinacao(
				"Documentos a Eliminar Não Disponíveis", false);

		for (ExItemDestinacao o : provisorio) {
			if (!comp().podeIncluirEmEditalEliminacao(
					getDoc().getCadastrante(), getDoc().getLotaCadastrante(),
					o.getMob()))
				indisponiveis.adicionar(o);
			else if (o.getMob().doc().isEletronico())
				digitais.adicionar(o);
			else
				fisicos.adicionar(o);
		}

		listaFinal.add(digitais);
		listaFinal.add(fisicos);
		listaFinal.add(indisponiveis);

		return listaFinal;
	}

	public void gravar() throws Exception {
		List<ExMobil> aIncluir = new ArrayList<ExMobil>();
		List<ExMobil> aManter = new ArrayList<ExMobil>();
		List<ExMobil> aRetirar = new ArrayList<ExMobil>();

		final Pattern p = Pattern.compile("^mob?([0-9]{1,10})");
		Map<String, String> form = getDoc().getForm();
		for (String chave : form.keySet()) {
			final Matcher m = p.matcher(chave);
			if (!m.find() || m.group(1) == null)
				continue;
			long l = Long.valueOf(m.group(1));
			ExMobil mob = dao().consultar(l, ExMobil.class, false);

			if (form.get(chave).equals("Sim")) {
				ExMovimentacao movInclusao = mob
						.getUltimaMovimentacaoNaoCancelada(
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_EM_EDITAL_DE_ELIMINACAO,
								ExTipoMovimentacao.TIPO_MOVIMENTACAO_RETIRADA_DE_EDITAL_DE_ELIMINACAO);
				boolean referenciaEsteDoc = movInclusao != null
						&& movInclusao.getExMobilRef().equals(
								getDoc().getMobilGeral());
				if (!comp().podeIncluirEmEditalEliminacao(
						getDoc().getCadastrante(),
						getDoc().getLotaCadastrante(), mob))
					throw new AplicacaoException("O documento "
							+ mob.getCodigo()
							+ "não está disponível para eliminação.");
				if (movInclusao == null)
					aIncluir.add(mob);
				else if (referenciaEsteDoc) {
					aManter.add(mob);
				} else
					throw new Exception(
							"Não foi possível fazer a gravação porque o documento "
									+ mob.getCodigo()
									+ "já faz parte do edital "
									+ movInclusao.getExMobilRef());
			}
		}

		for (ExItemDestinacao o : getEfetivamenteInclusos()) {
			if (!aIncluir.contains(o.getMob()) && !aManter.contains(o.getMob()))
				aRetirar.add(o.getMob());
		}

		for (ExMobil m : aIncluir) {
			bl().incluirEmEditalEliminacao(getDoc(), m);
		}

		for (ExMobil m : aRetirar) {
			ExMovimentacao movInclusao = m
					.getUltimaMovimentacaoNaoCancelada(
							ExTipoMovimentacao.TIPO_MOVIMENTACAO_INCLUSAO_EM_EDITAL_DE_ELIMINACAO,
							ExTipoMovimentacao.TIPO_MOVIMENTACAO_RETIRADA_DE_EDITAL_DE_ELIMINACAO);
			bl().excluirMovimentacao(getDoc().getCadastrante(),
					getDoc().getLotaCadastrante(), m, movInclusao.getIdMov());
		}

	}
}
