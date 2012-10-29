package models;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.model.HistoricoAuditavel;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Historico;

@Entity
@Table(name = "GC_MOVIMENTACAO")
public class GcMovimentacao extends GenericModel {
	@Id
	@GeneratedValue
	@Column(name = "ID_MOVIMENTACAO")
	public long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_TIPO_MOVIMENTACAO")
	public GcTipoMovimentacao tpMov;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_INFORMACAO")
	public GcInformacao inf;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_MOVIMENTACAO_REF")
	public GcMovimentacao movRef;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_MOVIMENTACAO_CANCELADORA")
	public GcMovimentacao movCanceladora;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_PESSOA")
	public DpPessoa autor;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_LOTACAO")
	public DpLotacao lotacao;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_SERVICO")
	public CpServico servico;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_CLASSIFICACAO")
	public CpClassificacao classificacao;

	@Column(name = "TITULO_MOVIMENTACAO")
	public String titulo;

	@Column(name = "DESC_MOVIMENTACAO")
	public String desc;

	@Column(name = "PUBLICACAO_DT_INI")
	public Date publicacaoDtIni;

	@Column(name = "PUBLICACAO_DT_FIM")
	public Date publicacaoDtFim;

	@Column(name = "HIS_DT_INI")
	public Date hisDtIni;

	@Column(name = "HIS_IDC_INI")
	public CpIdentidade hisIdcIni;
}
