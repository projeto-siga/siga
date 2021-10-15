package br.gov.jfrj.siga.wf.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.wf.model.enm.WfTipoDeDesignacao;

@Entity
@DiscriminatorValue("D")
public class WfMovDesignacao extends WfMov {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PESS_ID_DE")
	private DpPessoa pessoaDe;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PESS_ID_PARA")
	private DpPessoa pessoaPara;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOTA_ID_DE")
	private DpLotacao lotaDe;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOTA_ID_PARA")
	private DpLotacao lotaPara;

	@Enumerated(EnumType.STRING)
	@Column(name = "MOVI_TP_DESIGNACAO")
	private WfTipoDeDesignacao tipo = WfTipoDeDesignacao.TODAS;

	public WfMovDesignacao() {
		super();
	}

	public WfMovDesignacao(WfProcedimento pi, Date dtIni, DpPessoa titular, DpLotacao lotaTitular,
			CpIdentidade identidade, DpPessoa pessoaDe, DpLotacao lotaDe, DpPessoa pessoaPara, DpLotacao lotaPara) {
		super(pi, null, dtIni, titular, lotaTitular, identidade);
		this.pessoaDe = pessoaDe;
		this.lotaDe = lotaDe;
		this.pessoaPara = pessoaPara;
		this.lotaPara = lotaPara;
	}

	@Override
	public String getEvento() {
		return "Designação";
	}

	@Override
	public String getDescricaoEvento() {
		return (pessoaDe != null ? pessoaDe.getSigla() : lotaDe != null ? lotaDe.getSigla() : "[não definido]") + " -> "
				+ (pessoaPara != null ? pessoaPara.getSigla()
						: lotaPara != null ? lotaPara.getSigla() : "[não definido]")
				+ " (" + tipo.getDescr().toLowerCase() + ")";
	}

	public DpPessoa getPessoaDe() {
		return pessoaDe;
	}

	public void setPessoaDe(DpPessoa pessoaDe) {
		this.pessoaDe = pessoaDe;
	}

	public DpPessoa getPessoaPara() {
		return pessoaPara;
	}

	public void setPessoaPara(DpPessoa pessoaPara) {
		this.pessoaPara = pessoaPara;
	}

	public DpLotacao getLotaDe() {
		return lotaDe;
	}

	public void setLotaDe(DpLotacao lotaDe) {
		this.lotaDe = lotaDe;
	}

	public DpLotacao getLotaPara() {
		return lotaPara;
	}

	public void setLotaPara(DpLotacao lotaPara) {
		this.lotaPara = lotaPara;
	}

	public WfTipoDeDesignacao getTipo() {
		return tipo;
	}

	public void setTipo(WfTipoDeDesignacao tipo) {
		this.tipo = tipo;
	}

}
