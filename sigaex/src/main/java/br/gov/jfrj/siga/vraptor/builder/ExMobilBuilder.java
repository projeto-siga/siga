package br.gov.jfrj.siga.vraptor.builder;

import br.gov.jfrj.siga.cp.model.CpOrgaoSelecao;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.vraptor.ExClassificacaoSelecao;

public final class ExMobilBuilder {

	private Integer postback;
	private Integer ultMovTipoResp;
	private DpPessoaSelecao ultMovRespSel;
	private DpLotacaoSelecao ultMovLotaRespSel;
	private Long orgaoUsu;
	private Long idTpDoc;
	private CpOrgaoSelecao cpOrgaoSel;
	private DpPessoaSelecao subscritorSel;
	private Integer tipoCadastrante;
	private DpPessoaSelecao cadastranteSel;
	private DpLotacaoSelecao lotaCadastranteSel;
	private Integer tipoDestinatario;
	private DpPessoaSelecao destinatarioSel;
	private DpLotacaoSelecao lotacaoDestinatarioSel;
	private CpOrgaoSelecao orgaoExternoDestinatarioSel;
	private ExClassificacaoSelecao classificacaoSel;
	private Integer offset;

	private ExMobilBuilder() {
	}

	public static ExMobilBuilder novaInstancia() {
		return new ExMobilBuilder();
	}

	public void processar(final DpLotacao lotaTitular) {
		if (ultMovRespSel == null) {
			ultMovRespSel = new DpPessoaSelecao();
		}
		if (ultMovLotaRespSel == null) {
			ultMovLotaRespSel = new DpLotacaoSelecao();
		}
		if (cpOrgaoSel == null) {
			cpOrgaoSel = new CpOrgaoSelecao();
		}
		if (subscritorSel == null) {
			subscritorSel = new DpPessoaSelecao();
		}
		if (cadastranteSel == null) {
			cadastranteSel = new DpPessoaSelecao();
		}
		if (lotaCadastranteSel == null) {
			lotaCadastranteSel = new DpLotacaoSelecao();
		}
		if (destinatarioSel == null) {
			destinatarioSel = new DpPessoaSelecao();
		}
		if (lotacaoDestinatarioSel == null) {
			lotacaoDestinatarioSel = new DpLotacaoSelecao();
		}
		if (orgaoExternoDestinatarioSel == null) {
			orgaoExternoDestinatarioSel = new CpOrgaoSelecao();
		}
		if (classificacaoSel == null) {
			classificacaoSel = new ExClassificacaoSelecao();
		}

		if (postback == null) {
			if (orgaoUsu == null) {
				orgaoUsu = lotaTitular.getOrgaoUsuario().getIdOrgaoUsu();
			}
			idTpDoc = 0L;
			tipoDestinatario = 2;
			if (ultMovLotaRespSel.getId() != null) {
				ultMovTipoResp = 2;
			} else {
				ultMovTipoResp = 1;
			}
			if (lotaCadastranteSel.getId() != null) {
				tipoCadastrante = 2;
			} else {
				tipoCadastrante = 1;
			}
		}

		if (offset == null) {
			offset = 0;
		}

		classificacaoSel.buscar();
		destinatarioSel.buscar();
		lotacaoDestinatarioSel.buscar();
		orgaoExternoDestinatarioSel.buscar();
		subscritorSel.buscar();
		ultMovLotaRespSel.buscar();
		ultMovRespSel.buscar();
		cadastranteSel.buscar();
		lotaCadastranteSel.buscar();
	}

	public Integer getPostback() {
		return postback;
	}

	public Integer getUltMovTipoResp() {
		return ultMovTipoResp;
	}

	public DpPessoaSelecao getUltMovRespSel() {
		return ultMovRespSel;
	}

	public DpLotacaoSelecao getUltMovLotaRespSel() {
		return ultMovLotaRespSel;
	}

	public Long getOrgaoUsu() {
		return orgaoUsu;
	}

	public Long getIdTpDoc() {
		return idTpDoc;
	}

	public CpOrgaoSelecao getCpOrgaoSel() {
		return cpOrgaoSel;
	}

	public DpPessoaSelecao getSubscritorSel() {
		return subscritorSel;
	}

	public Integer getTipoCadastrante() {
		return tipoCadastrante;
	}

	public DpPessoaSelecao getCadastranteSel() {
		return cadastranteSel;
	}

	public DpLotacaoSelecao getLotaCadastranteSel() {
		return lotaCadastranteSel;
	}

	public Integer getTipoDestinatario() {
		return tipoDestinatario;
	}

	public DpPessoaSelecao getDestinatarioSel() {
		return destinatarioSel;
	}

	public DpLotacaoSelecao getLotacaoDestinatarioSel() {
		return lotacaoDestinatarioSel;
	}

	public CpOrgaoSelecao getOrgaoExternoDestinatarioSel() {
		return orgaoExternoDestinatarioSel;
	}

	public ExClassificacaoSelecao getClassificacaoSel() {
		return classificacaoSel;
	}

	public Integer getOffset() {
		return offset;
	}

	public ExMobilBuilder setPostback(Integer postback) {
		this.postback = postback;
		return this;
	}

	public ExMobilBuilder setUltMovTipoResp(Integer ultMovTipoResp) {
		this.ultMovTipoResp = ultMovTipoResp;
		return this;
	}

	public ExMobilBuilder setUltMovRespSel(DpPessoaSelecao ultMovRespSel) {
		this.ultMovRespSel = ultMovRespSel;
		return this;
	}

	public ExMobilBuilder setUltMovLotaRespSel(DpLotacaoSelecao ultMovLotaRespSel) {
		this.ultMovLotaRespSel = ultMovLotaRespSel;
		return this;
	}

	public ExMobilBuilder setOrgaoUsu(Long orgaoUsu) {
		this.orgaoUsu = orgaoUsu;
		return this;
	}

	public ExMobilBuilder setIdTpDoc(Long idTpDoc) {
		this.idTpDoc = idTpDoc;
		return this;
	}

	public ExMobilBuilder setCpOrgaoSel(CpOrgaoSelecao cpOrgaoSel) {
		this.cpOrgaoSel = cpOrgaoSel;
		return this;
	}

	public ExMobilBuilder setSubscritorSel(DpPessoaSelecao subscritorSel) {
		this.subscritorSel = subscritorSel;
		return this;
	}

	public ExMobilBuilder setTipoCadastrante(Integer tipoCadastrante) {
		this.tipoCadastrante = tipoCadastrante;
		return this;
	}

	public ExMobilBuilder setCadastranteSel(DpPessoaSelecao cadastranteSel) {
		this.cadastranteSel = cadastranteSel;
		return this;
	}

	public ExMobilBuilder setLotaCadastranteSel(DpLotacaoSelecao lotaCadastranteSel) {
		this.lotaCadastranteSel = lotaCadastranteSel;
		return this;
	}

	public ExMobilBuilder setTipoDestinatario(Integer tipoDestinatario) {
		this.tipoDestinatario = tipoDestinatario;
		return this;
	}

	public ExMobilBuilder setDestinatarioSel(DpPessoaSelecao destinatarioSel) {
		this.destinatarioSel = destinatarioSel;
		return this;
	}

	public ExMobilBuilder setLotacaoDestinatarioSel(DpLotacaoSelecao lotacaoDestinatarioSel) {
		this.lotacaoDestinatarioSel = lotacaoDestinatarioSel;
		return this;
	}

	public ExMobilBuilder setOrgaoExternoDestinatarioSel(CpOrgaoSelecao orgaoExternoDestinatarioSel) {
		this.orgaoExternoDestinatarioSel = orgaoExternoDestinatarioSel;
		return this;
	}

	public ExMobilBuilder setClassificacaoSel(ExClassificacaoSelecao classificacaoSel) {
		this.classificacaoSel = classificacaoSel;
		return this;
	}

	public ExMobilBuilder setOffset(Integer offset) {
		this.offset = offset;
		return this;
	}
}
