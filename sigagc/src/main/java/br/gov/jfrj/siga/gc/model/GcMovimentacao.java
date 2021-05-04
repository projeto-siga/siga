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
@Table(name = "gc_movimentacao", schema = "sigagc")
@NamedQueries({
	@NamedQuery(name = "buscarInformacaoPorAnexo", query = 
			"select info from GcMovimentacao mov "
						+ "join mov.inf info "
						+ "where mov.tipo.id = :idTipoMovAnexarArquivo "
						+ "and mov.movCanceladora is null and mov.arq.id = :idArqMov and info.id = :idInformacao"),
	@NamedQuery(name = "numeroEquipeLotacao", query = "select count(distinct p.idPessoaIni) from DpPessoa p join p.lotacao l where l.idLotacao = :idLotacao"),
	@NamedQuery(name = "numeroEquipeCiente", query = "select count(*) from GcMovimentacao m where m.tipo.id= 7 and m.inf.id = :idInfo and m.lotacaoAtendente.idLotacao = :idLotacao and m.movRef = :movRef") 
})
public class GcMovimentacao extends Objeto implements
		Comparable<GcMovimentacao> {
	private static final long serialVersionUID = 7936898203057280892L;

	public static ActiveRecord<GcMovimentacao> AR = new ActiveRecord<>(
			GcMovimentacao.class);

	@Id
	@SequenceGenerator(sequenceName = "SIGAGC.hibernate_sequence", name = "gcMovimentacaoSeq")
	@GeneratedValue(generator = "gcMovimentacaoSeq")
	@Column(name = "ID_MOVIMENTACAO")
	private long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_TIPO_MOVIMENTACAO")
	private GcTipoMovimentacao tipo;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_INFORMACAO")
	private GcInformacao inf;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_MOVIMENTACAO_REF")
	private GcMovimentacao movRef;

	@ManyToOne(optional = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_MOVIMENTACAO_CANCELADORA")
	private GcMovimentacao movCanceladora;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_PESSOA_ATENDENTE")
	private DpPessoa pessoaAtendente;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_LOTACAO_ATENDENTE")
	private DpLotacao lotacaoAtendente;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_PAPEL")
	private GcPapel papel;

	// Edson: pode ser usado quando o grupo de e-mail notificado for do siga-gi
	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_GRUPO")
	private CpGrupo grupo;

	@Column(name = "DESCRICAO")
	private String descricao;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_PESSOA_TITULAR")
	private DpPessoa pessoaTitular;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_LOTACAO_TITULAR")
	private DpLotacao lotacaoTitular;

	@ManyToOne(optional = true)
	@JoinColumn(name = "ID_CONTEUDO")
	private GcArquivo arq;

	@Column(name = "HIS_DT_INI")
	private Date hisDtIni;

	@ManyToOne(optional = false)
	@JoinColumn(name = "HIS_IDC_INI")
	private CpIdentidade hisIdcIni;

	public void setId(long id) {
		this.id = id;
	}

	public void setTipo(GcTipoMovimentacao tipo) {
		this.tipo = tipo;
	}

	public void setInf(GcInformacao inf) {
		this.inf = inf;
	}

	public void setMovRef(GcMovimentacao movRef) {
		this.movRef = movRef;
	}

	public void setMovCanceladora(GcMovimentacao movCanceladora) {
		this.movCanceladora = movCanceladora;
	}

	public void setPessoaAtendente(DpPessoa pessoaAtendente) {
		this.pessoaAtendente = pessoaAtendente;
	}

	public void setLotacaoAtendente(DpLotacao lotacaoAtendente) {
		this.lotacaoAtendente = lotacaoAtendente;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setPessoaTitular(DpPessoa pessoaTitular) {
		this.pessoaTitular = pessoaTitular;
	}

	public void setLotacaoTitular(DpLotacao lotacaoTitular) {
		this.lotacaoTitular = lotacaoTitular;
	}

	public void setArq(GcArquivo arq) {
		this.arq = arq;
	}

	public void setHisDtIni(Date hisDtIni) {
		this.hisDtIni = hisDtIni;
	}

	public void setHisIdcIni(CpIdentidade hisIdcIni) {
		this.hisIdcIni = hisIdcIni;
	}

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
		switch ((int) this.tipo.getId()) {
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
	
	public static GcInformacao buscarInformacaoPorAnexo(GcArquivo anexo, Long idInformacao) {
		Query query = AR.em().createNamedQuery("buscarInformacaoPorAnexo");
		
		// BJN workaround pro erro na hora de usar a constante dentro do HQL direto
		query.setParameter("idTipoMovAnexarArquivo", GcTipoMovimentacao.TIPO_MOVIMENTACAO_ANEXAR_ARQUIVO);
		
		query.setParameter("idArqMov", anexo.getId());
		query.setParameter("idInformacao",idInformacao);
		GcInformacao retorno = null;
		try {
			retorno = (GcInformacao) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}

		return retorno;
	}

	public boolean todaLotacaoCiente(GcMovimentacao movRef) {
		Query query = AR.em().createNamedQuery("numeroEquipeLotacao");
		query.setParameter("idLotacao", lotacaoAtendente.getId());
		Long count = (Long) query.getSingleResult();

		Query query2 = AR.em().createNamedQuery("numeroEquipeCiente");
		query2.setParameter("idInfo", inf.getId());
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
