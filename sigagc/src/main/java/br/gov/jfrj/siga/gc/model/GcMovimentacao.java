package br.gov.jfrj.siga.gc.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.jfrj.siga.cp.CpGrupo;
import br.gov.jfrj.siga.cp.CpGrupoDeEmail;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name = "GC_MOVIMENTACAO", schema = "SIGAGC")
@NamedQueries({
		@NamedQuery(name = "numeroEquipeLotacao", query = "select count(distinct p.idPessoaIni) from DpPessoa p join p.lotacao l where l.idLotacao = :idLotacao"),
		@NamedQuery(name = "numeroEquipeCiente", query = "select count(*) from GcMovimentacao m where m.tipo.id= 7 and m.inf.id = :idInfo and m.lotacaoAtendente.idLotacao = :idLotacao and m.movRef = :movRef") })
public class GcMovimentacao extends Objeto implements
		Comparable<GcMovimentacao> {
	private static final long serialVersionUID = 7936898203057280892L;

	public static ActiveRecord<GcMovimentacao> AR = new ActiveRecord<>(
			GcMovimentacao.class);

	@Id
	@SequenceGenerator(sequenceName = "SIGAGC.hibernate_sequence", name = "gcMovimentacaoSeq")
	@GeneratedValue(generator = "gcMovimentacaoSeq")
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

	@ManyToOne(optional = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_MOVIMENTACAO_CANCELADORA")
	public GcMovimentacao movCanceladora;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_PESSOA_ATENDENTE")
	public DpPessoa pessoaAtendente;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_LOTACAO_ATENDENTE")
	public DpLotacao lotacaoAtendente;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_PAPEL")
	public GcPapel papel;

	// Edson: pode ser usado quando o grupo de e-mail notificado for do siga-gi
	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_GRUPO")
	public CpGrupo grupo;

	@Column(name = "DESCRICAO")
	public String descricao;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_PESSOA_TITULAR")
	public DpPessoa pessoaTitular;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_LOTACAO_TITULAR")
	public DpLotacao lotacaoTitular;

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

	public boolean todaLotacaoCiente(GcMovimentacao movRef) {
		Query query = AR.em().createNamedQuery("numeroEquipeLotacao");
		query.setParameter("idLotacao", lotacaoAtendente.getId());
		Long count = (Long) query.getSingleResult();

		Query query2 = AR.em().createNamedQuery("numeroEquipeCiente");
		query2.setParameter("idInfo", inf.id);
		query2.setParameter("idLotacao", lotacaoAtendente.getId());
		query2.setParameter("movRef", movRef);
		Long count2 = (Long) query2.getSingleResult();

		return (count == count2);
	}

	public long getId() {
		return id;
	}

	public GcTipoMovimentacao getTipo() {
		return tipo;
	}

	public GcInformacao getInf() {
		return inf;
	}

	public GcMovimentacao getMovRef() {
		return movRef;
	}

	public GcMovimentacao getMovCanceladora() {
		return movCanceladora;
	}

	public DpPessoa getPessoaAtendente() {
		return pessoaAtendente;
	}

	public DpLotacao getLotacaoAtendente() {
		return lotacaoAtendente;
	}

	public String getDescricao() {
		return descricao;
	}

	public DpPessoa getPessoaTitular() {
		return pessoaTitular;
	}

	public DpLotacao getLotacaoTitular() {
		return lotacaoTitular;
	}

	public GcArquivo getArq() {
		return arq;
	}

	public Date getHisDtIni() {
		return hisDtIni;
	}

	public CpIdentidade getHisIdcIni() {
		return hisIdcIni;
	}

	public GcPapel getPapel() {
		return papel;
	}

	public void setPapel(GcPapel papel) {
		this.papel = papel;
	}

	public CpGrupo getGrupo() {
		return grupo;
	}

	public void setGrupo(CpGrupo grupo) {
		this.grupo = grupo;
	}

}
