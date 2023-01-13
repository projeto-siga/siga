package br.gov.jfrj.siga.wf.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.gov.jfrj.siga.dp.CpMarca;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.ContextoPersistencia;

@Entity
@DiscriminatorValue("4")
public class WfMarca extends CpMarca implements Comparable<WfMarca> {

	private static final long serialVersionUID = 1494193540156064691L;

	@ManyToOne
	@JoinColumn(name = "ID_REF")
	private WfProcedimento procedimento;

	public WfMarca() {

	}

	public WfMarca(Long idMarcador, DpPessoa pessoa, DpLotacao lota, WfProcedimento pi) {
		if (pessoa != null)
			setDpPessoaIni(pessoa.getPessoaInicial());
		setDpLotacaoIni(lota.getLotacaoInicial());
		setCpMarcador(ContextoPersistencia.em().find(CpMarcador.class, idMarcador));
		setProcedimento(pi);
	}

	public String getDescricao() {
		return this.getCpMarcador().getDescrMarcador() + " (" + getDpLotacaoIni().getSigla() + ")";
	}

	public WfProcedimento getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(WfProcedimento procedimento) {
		this.procedimento = procedimento;
	}

	@Override
	public int compareTo(WfMarca other) {
		int i = getCpMarcador().getIdMarcador().compareTo(other.getCpMarcador().getIdMarcador());
		if (i != 0)
			return i;
		if (getDpLotacaoIni() == null) {
			if (other.getDpLotacaoIni() == null)
				i = 0;
			else
				i = -1;
		} else {
			if (other.getDpLotacaoIni() == null)
				i = 1;
			else
				i = getDpLotacaoIni().getIdLotacao().compareTo(other.getDpLotacaoIni().getIdLotacao());
		}
		if (i != 0)
			return i;
		if (getDpPessoaIni() == null) {
			if (other.getDpPessoaIni() == null)
				i = 0;
			else
				i = -1;
		} else {
			if (other.getDpPessoaIni() == null)
				i = 1;
			else
				i = getDpPessoaIni().getIdPessoa().compareTo(other.getDpPessoaIni().getIdPessoa());
		}
		if (i != 0)
			return i;
		return 0;
	}

}
