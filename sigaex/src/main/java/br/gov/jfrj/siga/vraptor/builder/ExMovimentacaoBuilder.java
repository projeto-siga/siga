package br.gov.jfrj.siga.vraptor.builder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Data;
import br.gov.jfrj.siga.cp.model.CpOrgaoSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.ExPapel;
import br.gov.jfrj.siga.ex.ExTipoDespacho;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.vraptor.ExClassificacaoSelecao;
import br.gov.jfrj.siga.vraptor.ExMobilSelecao;

public final class ExMovimentacaoBuilder {

	private DpLotacao lotaTitular;
	private DpPessoa cadastrante;
	private ExMobil mob;
	private String conteudo;
	private CpOrgaoSelecao cpOrgaoSel;
	private String descrMov;
	private DpPessoaSelecao destinoFinalSel;
	private ExMobilSelecao documentoRefSel;
	private ExClassificacaoSelecao classificacaoSel;
	private String dtDispon;
	private String dtPubl;
	private String dtMovString;
	private String dtDevolucaoMovString;
	private Long idTpDespacho;
	private DpLotacaoSelecao lotaDestinoFinalSel;
	private boolean substituicao;
	private DpLotacaoSelecao lotaResponsavelSel;
	private String obsOrgao;
	private String nmFuncaoSubscritor;
	private DpPessoaSelecao responsavelSel;
	private DpPessoaSelecao titularSel;
	private DpPessoaSelecao subscritorSel;
	private DpLotacaoSelecao lotaSubscritorSel;
	private Long idPapel;
	private Long idMarcador;
	private String contentType;
	private String fileName;

	private ExMovimentacaoBuilder() {
		substituicao = false;
		cpOrgaoSel = new CpOrgaoSelecao();
		titularSel = new DpPessoaSelecao();
		subscritorSel = new DpPessoaSelecao();
		lotaSubscritorSel = new DpLotacaoSelecao();
		documentoRefSel = new ExMobilSelecao();
		responsavelSel = new DpPessoaSelecao();
		destinoFinalSel = new DpPessoaSelecao();
		lotaResponsavelSel = new DpLotacaoSelecao();
		lotaDestinoFinalSel = new DpLotacaoSelecao();
		classificacaoSel = new ExClassificacaoSelecao();
	}

	public static ExMovimentacaoBuilder novaInstancia() {
		return new ExMovimentacaoBuilder();
	}

	public ExMovimentacao construir(final ExDao dao) {
		final ExMovimentacao mov = new ExMovimentacao();
		construir(mov, dao);
		return mov;
	}

	public void construir(final ExMovimentacao mov, final ExDao dao) {
		if (mov == null) {
			throw new AplicacaoException("Informe a Movimentacao");
		}

		mov.setExMobil(mob);
		mov.setDescrMov(descrMov);

		if (idPapel != null) {
			mov.setExPapel(dao.consultar(idPapel, ExPapel.class, false));
		}

		if (idMarcador != null) {
			mov.setMarcador(dao.consultar(idMarcador, CpMarcador.class, false));
		}

		if (idTpDespacho != null) {
			switch (idTpDespacho.intValue()) {
			case 0:
				break;
			case -1:
				break;
			case -2:
				break;
			default:
				mov.setExTipoDespacho(dao.consultar(idTpDespacho.longValue(), ExTipoDespacho.class, false));
			}
		}

		conteudo = getConteudo();

		if (mov.getCadastrante() == null) {
			mov.setCadastrante(cadastrante);
		}
		mov.setLotaCadastrante(lotaTitular);
		if (mov.getLotaCadastrante() == null && mov.getCadastrante() != null) {
			mov.setLotaCadastrante(mov.getCadastrante().getLotacao());
		}

		if (lotaSubscritorSel != null && lotaSubscritorSel.getId() != null) {
			mov.setLotaSubscritor(dao.consultar(lotaSubscritorSel.getId(), DpLotacao.class, false));
		}

		if (subscritorSel != null && subscritorSel.getId() != null) {
			mov.setSubscritor(dao.consultar(subscritorSel.getId(), DpPessoa.class, false));
			if (mov.getLotaSubscritor() == null)
				mov.setLotaSubscritor(mov.getSubscritor().getLotacao());
		}

		mov.setNmFuncaoSubscritor(nmFuncaoSubscritor);

		if (substituicao) {
			if (titularSel != null && titularSel.getId() != null) {
				mov.setTitular(dao.consultar(titularSel.getId(), DpPessoa.class, false));
			}
		} else {
			mov.setTitular(mov.getSubscritor());
			mov.setLotaTitular(mov.getLotaSubscritor());
		}

		if (documentoRefSel != null && documentoRefSel.getId() != null) {
			mov.setExMobilRef(dao.consultar(documentoRefSel.getId(), ExMobil.class, false));
		}

		if (classificacaoSel != null && classificacaoSel.getId() != null) {
			mov.setExClassificacao(dao.consultar(classificacaoSel.getId(), ExClassificacao.class, false));
		}

		if (responsavelSel != null && responsavelSel.getId() != null) {
			mov.setResp(dao.consultar(responsavelSel.getId(), DpPessoa.class, false));
		}

		if (lotaResponsavelSel != null && lotaResponsavelSel.getId() != null) {
			mov.setLotaResp(dao.consultar(lotaResponsavelSel.getId(), DpLotacao.class, false));
		} else {
			if (mov.getResp() != null) {
				mov.setLotaResp(mov.getResp().getLotacao());
			}
		}

		if (cpOrgaoSel != null && cpOrgaoSel.getId() != null) {
			mov.setOrgaoExterno(dao.consultar(cpOrgaoSel.getId(), CpOrgao.class, false));
		}

		mov.setObsOrgao(obsOrgao);

		if (destinoFinalSel != null && destinoFinalSel.getId() != null) {
			mov.setDestinoFinal(dao.consultar(destinoFinalSel.getId(), DpPessoa.class, false));
		}

		if (lotaDestinoFinalSel != null && lotaDestinoFinalSel.getId() != null) {
			mov.setLotaDestinoFinal(dao.consultar(lotaDestinoFinalSel.getId(), DpLotacao.class, false));
		} else {
			if (mov.getDestinoFinal() != null) {
				mov.setLotaDestinoFinal(mov.getDestinoFinal().getLotacao());
			}
		}

		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		try {
			mov.setDtMov(df.parse(dtMovString));
			if (mov.getDtMov() != null && !Data.dataDentroSeculo21(mov.getDtMov()))
				throw new AplicacaoException("Data inválida, deve estar entre o ano 2000 e ano 2100");
		} catch (final Exception e) {
		}

		try {
			mov.setDtFimMov(df.parse(dtDevolucaoMovString));
		} catch (final Exception e) {
		}

		if (getDtPubl() != null) {
			try {
				mov.setDtMov(df.parse(dtPubl));
			} catch (final ParseException e) {
				mov.setDtMov(new Date());
			} catch (final NullPointerException e) {
				mov.setDtMov(new Date());
			}
		}

		try {
			mov.setDtDispPublicacao(df.parse(dtDispon));
		} catch (final Exception e) {
		}

		mov.setConteudoTpMov(contentType);
		mov.setNmArqMov(fileName);

		if ((mov.getTitular() != null && mov.getSubscritor() == null) || (mov.getLotaTitular() != null && mov.getLotaSubscritor() == null)) {
			throw new AplicacaoException("Não foi selecionado o substituto para o titular");
		}
	}

	public String getConteudo() {
		if (conteudo == null)
			return null;
		if (conteudo.trim().length() == 0)
			return null;
		return conteudo.trim();
	}

	public DpLotacao getLotaTitular() {
		return lotaTitular;
	}

	public DpPessoa getCadastrante() {
		return cadastrante;
	}

	public ExMobil getMob() {
		return mob;
	}

	public CpOrgaoSelecao getCpOrgaoSel() {
		return cpOrgaoSel;
	}

	public String getDescrMov() {
		return descrMov;
	}

	public DpPessoaSelecao getDestinoFinalSel() {
		return destinoFinalSel;
	}

	public ExMobilSelecao getDocumentoRefSel() {
		return documentoRefSel;
	}

	public ExClassificacaoSelecao getClassificacaoSel() {
		return classificacaoSel;
	}

	public String getDtDispon() {
		return dtDispon;
	}

	public String getDtPubl() {
		return dtPubl;
	}

	public String getDtMovString() {
		return dtMovString;
	}

	public String getDtDevolucaoMovString() {
		return dtDevolucaoMovString;
	}

	public Long getIdTpDespacho() {
		return idTpDespacho;
	}

	public DpLotacaoSelecao getLotaDestinoFinalSel() {
		return lotaDestinoFinalSel;
	}

	public boolean isSubstituicao() {
		return substituicao;
	}

	public DpLotacaoSelecao getLotaResponsavelSel() {
		return lotaResponsavelSel;
	}

	public String getObsOrgao() {
		return obsOrgao;
	}

	public String getNmFuncaoSubscritor() {
		return nmFuncaoSubscritor;
	}

	public DpPessoaSelecao getResponsavelSel() {
		return responsavelSel;
	}

	public DpPessoaSelecao getTitularSel() {
		return titularSel;
	}

	public DpPessoaSelecao getSubscritorSel() {
		return subscritorSel;
	}

	public DpLotacaoSelecao getLotaSubscritorSel() {
		return lotaSubscritorSel;
	}

	public Long getIdPapel() {
		return idPapel;
	}

	public Long getIdMarcador() {
		return idMarcador;
	}

	public String getContentType() {
		return contentType;
	}

	public String getFileName() {
		return fileName;
	}

	public ExMovimentacaoBuilder setLotaTitular(DpLotacao lotaTitular) {
		this.lotaTitular = lotaTitular;
		return this;
	}

	public ExMovimentacaoBuilder setCadastrante(DpPessoa cadastrante) {
		this.cadastrante = cadastrante;
		return this;
	}

	public ExMovimentacaoBuilder setMob(ExMobil mob) {
		this.mob = mob;
		return this;
	}

	public ExMovimentacaoBuilder setConteudo(String conteudo) {
		this.conteudo = conteudo;
		return this;
	}

	public ExMovimentacaoBuilder setCpOrgaoSel(CpOrgaoSelecao cpOrgaoSel) {
		this.cpOrgaoSel = cpOrgaoSel;
		return this;
	}

	public ExMovimentacaoBuilder setDescrMov(String descrMov) {
		this.descrMov = descrMov;
		return this;
	}

	public ExMovimentacaoBuilder setDestinoFinalSel(DpPessoaSelecao destinoFinalSel) {
		this.destinoFinalSel = destinoFinalSel;
		return this;
	}

	public ExMovimentacaoBuilder setDocumentoRefSel(ExMobilSelecao documentoRefSel) {
		this.documentoRefSel = documentoRefSel;
		return this;
	}

	public ExMovimentacaoBuilder setClassificacaoSel(ExClassificacaoSelecao classificacaoSel) {
		this.classificacaoSel = classificacaoSel;
		return this;
	}

	public ExMovimentacaoBuilder setDtDispon(String dtDispon) {
		this.dtDispon = dtDispon;
		return this;
	}

	public ExMovimentacaoBuilder setDtPubl(String dtPubl) {
		this.dtPubl = dtPubl;
		return this;
	}

	public ExMovimentacaoBuilder setDtMovString(String dtMovString) {
		this.dtMovString = dtMovString;
		return this;
	}

	public ExMovimentacaoBuilder setDtDevolucaoMovString(String dtDevolucaoMovString) {
		this.dtDevolucaoMovString = dtDevolucaoMovString;
		return this;
	}

	public ExMovimentacaoBuilder setIdTpDespacho(Long idTpDespacho) {
		this.idTpDespacho = idTpDespacho;
		return this;
	}

	public ExMovimentacaoBuilder setLotaDestinoFinalSel(DpLotacaoSelecao lotaDestinoFinalSel) {
		this.lotaDestinoFinalSel = lotaDestinoFinalSel;
		return this;
	}

	public ExMovimentacaoBuilder setSubstituicao(boolean substituicao) {
		this.substituicao = substituicao;
		return this;
	}

	public ExMovimentacaoBuilder setLotaResponsavelSel(DpLotacaoSelecao lotaResponsavelSel) {
		this.lotaResponsavelSel = lotaResponsavelSel;
		return this;
	}

	public ExMovimentacaoBuilder setObsOrgao(String obsOrgao) {
		this.obsOrgao = obsOrgao;
		return this;
	}

	public ExMovimentacaoBuilder setNmFuncaoSubscritor(String nmFuncaoSubscritor) {
		this.nmFuncaoSubscritor = nmFuncaoSubscritor;
		return this;
	}

	public ExMovimentacaoBuilder setResponsavelSel(DpPessoaSelecao responsavelSel) {
		this.responsavelSel = responsavelSel;
		return this;
	}

	public ExMovimentacaoBuilder setTitularSel(DpPessoaSelecao titularSel) {
		this.titularSel = titularSel;
		return this;
	}

	public ExMovimentacaoBuilder setSubscritorSel(DpPessoaSelecao subscritorSel) {
		this.subscritorSel = subscritorSel;
		return this;
	}

	public ExMovimentacaoBuilder setLotaSubscritorSel(DpLotacaoSelecao lotaSubscritorSel) {
		this.lotaSubscritorSel = lotaSubscritorSel;
		return this;
	}

	public ExMovimentacaoBuilder setIdPapel(Long idPapel) {
		this.idPapel = idPapel;
		return this;
	}

	public ExMovimentacaoBuilder setIdMarcador(Long idMarcador) {
		this.idMarcador = idMarcador;
		return this;
	}

	public ExMovimentacaoBuilder setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public ExMovimentacaoBuilder setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

}
