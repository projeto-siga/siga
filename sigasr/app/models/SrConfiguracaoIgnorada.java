package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import play.db.jpa.JPA;
import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name = "SR_CONFIGURACAO_IGNORADA", schema = "SIGASR")
public class SrConfiguracaoIgnorada extends Objeto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_CONFIGURACAO_IGNORADA_SEQ", name = "srConfIgnSeq")
	@GeneratedValue(generator = "srConfIgnSeq")
	@Column(name = "ID_CONFIGURACAO_IGNORADA")	
	public Long idConfiguracaoIgnorada;
	
	@ManyToOne
	@JoinColumn(name = "ID_ITEM_CONFIGURACAO", nullable = false)
	public SrItemConfiguracao itemConfiguracao;
	
	@ManyToOne
	@JoinColumn(name = "ID_CONFIGURACAO", nullable = false)
	public SrConfiguracao configuracao;
	
	
	public static SrConfiguracaoIgnorada createNew(SrItemConfiguracao itemConfiguracao, SrConfiguracao configuracao) {
		SrConfiguracaoIgnorada newItem = new SrConfiguracaoIgnorada();
		newItem.itemConfiguracao = itemConfiguracao;
		newItem.configuracao = configuracao;
		
		return newItem;
	}
	
	@SuppressWarnings("unchecked")
	public static SrConfiguracaoIgnorada findByItemEConfiguracao(SrItemConfiguracao item, SrConfiguracao configuracao) {
		StringBuffer sb = new StringBuffer("select ig from SrConfiguracaoIgnorada as ig where ig.itemConfiguracao.id = ");
		sb.append(item.getId());
		sb.append(" and ig.configuracao.id = ");
		sb.append(configuracao.getId());
		
		List<SrConfiguracaoIgnorada> list = JPA.em().createQuery(sb.toString()).getResultList();
		
		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;
	}
	
	@SuppressWarnings("unchecked")
	public static List<SrConfiguracaoIgnorada> findByConfiguracao(SrConfiguracao configuracao) {
		StringBuffer sb = new StringBuffer("select ig from SrConfiguracaoIgnorada as ig where ig.configuracao.id = ");
		sb.append(configuracao.getId());
		
		List<SrConfiguracaoIgnorada> list = JPA.em().createQuery(sb.toString()).getResultList();
		
		return list;
	}
}
