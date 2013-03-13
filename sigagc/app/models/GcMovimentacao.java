package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

@Entity
@Table(name = "GC_MOVIMENTACAO")
public class GcMovimentacao extends GenericModel implements
		Comparable<GcMovimentacao> {
	@Id
	@GeneratedValue
	@Column(name = "ID_MOVIMENTACAO")
	public long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_TIPO_MOVIMENTACAO")
	public GcTipoMovimentacao tipo;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_INFORMACAO")
	public GcInformacao inf;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_MOVIMENTACAO_REF")
	public GcMovimentacao movRef;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_MOVIMENTACAO_CANCELADORA")
	public GcMovimentacao movCanceladora;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_PESSOA")
	public DpPessoa pessoa;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_LOTACAO")
	public DpLotacao lotacao;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_CONTEUDO")
	public GcArquivo arq;

	@Column(name = "HIS_DT_INI")
	public Date hisDtIni;

	@ManyToOne(optional = false)
	@JoinColumn(name = "HIS_IDC_INI")
	public CpIdentidade hisIdcIni;

	/**
	 * verifica se uma movimentação está cancelada. Uma movimentação está
	 * cancelada quando o seu atributo movimentacaoCanceladora está preenchido
	 * com um código de movimentação de cancelamento.
	 * 
	 * @return Verdadeiro se a movimentação está cancelada e Falso caso
	 *         contrário.
	 */
	public boolean isCancelada() {
		return movCanceladora != null;
	}

	public boolean isCanceladora() {
		switch ((int) this.tipo.id) {
		case (int) GcTipoMovimentacao.TIPO_MOVIMENTACAO_CIENTE:
		case (int) GcTipoMovimentacao.TIPO_MOVIMENTACAO_REVISADO:
			return true;
		}
		return false;
	}

	@Override
	public int compareTo(GcMovimentacao o) {
		return compare(this, o);
	}

	public static int compare(GcMovimentacao o1, GcMovimentacao o2) {
		int i = o2.hisDtIni.compareTo(o1.hisDtIni);
		if (i != 0)
			return i;
		if (o2.id > o1.id)
			return 1;
		if (o2.id < o1.id)
			return -1;
		return 0;
	}
}
