package models;

import java.util.Date;
import java.util.List;

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

import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

@Entity
@Table(name = "GC_MOVIMENTACAO", schema = "SIGAGC")
@NamedQueries({
		@NamedQuery(name = "numeroEquipeLotacao", query = "select count(distinct p.idPessoaIni) from DpPessoa p join p.lotacao l where l.idLotacao = :idLotacao"),
		@NamedQuery(name = "numeroEquipeCiente", query = "select count(*) from GcMovimentacao m where m.tipo.id= 7 and m.inf.id = :idInfo and m.lotacaoAtendente.idLotacao = :idLotacao and m.movRef = :movRef")
})
public class GcMovimentacao extends GenericModel implements
		Comparable<GcMovimentacao> {
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

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_MOVIMENTACAO_CANCELADORA")
	public GcMovimentacao movCanceladora;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_PESSOA_ATENDENTE")
	public DpPessoa pessoaAtendente;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_LOTACAO_ATENDENTE")
	public DpLotacao lotacaoAtendente;

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
		Query query  = JPA.em().createNamedQuery("numeroEquipeLotacao");
		query.setParameter("idLotacao", lotacaoAtendente.getId());
		Long count = (Long) query.getSingleResult();
		
		Query query2  = JPA.em().createNamedQuery("numeroEquipeCiente");
		query2.setParameter("idInfo", inf.id);
		query2.setParameter("idLotacao", lotacaoAtendente.getId());
		query2.setParameter("movRef", movRef);
		Long count2 = (Long) query2.getSingleResult();

		return (count == count2);
	}
}
