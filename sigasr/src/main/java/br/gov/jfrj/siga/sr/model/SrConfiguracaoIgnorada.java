package br.gov.jfrj.siga.sr.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name = "sr_configuracao_ignorada", schema = "sigasr")
public class SrConfiguracaoIgnorada extends Objeto {

    private static final long serialVersionUID = 1L;

    public static final ActiveRecord<SrConfiguracaoIgnorada> AR = new ActiveRecord<>(SrConfiguracaoIgnorada.class);

    @Id
    @SequenceGenerator(sequenceName = "SIGASR" + ".SR_CONFIGURACAO_IGNORADA_SEQ", name = "srConfIgnSeq")
    @GeneratedValue(generator = "srConfIgnSeq")
    @Column(name = "ID_CONFIGURACAO_IGNORADA")
    private Long idConfiguracaoIgnorada;

    @ManyToOne
    @JoinColumn(name = "ID_ITEM_CONFIGURACAO", nullable = false)
    private SrItemConfiguracao itemConfiguracao;

    @ManyToOne
    @JoinColumn(name = "ID_CONFIGURACAO", nullable = false)
    private SrConfiguracao configuracao;

    public SrConfiguracaoIgnorada() {
        super();
    }

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

        List<SrConfiguracaoIgnorada> list = AR.em().createQuery(sb.toString()).getResultList();

        if (list != null && list.size() > 0)
            return list.get(0);
        else
            return null;
    }

    @SuppressWarnings("unchecked")
    public static List<SrConfiguracaoIgnorada> findByConfiguracao(SrConfiguracaoCache configuracao) {
        StringBuffer sb = new StringBuffer("select ig from SrConfiguracaoIgnorada as ig where ig.configuracao.idConfiguracao = ");
        sb.append(configuracao.idConfiguracao);

        List<SrConfiguracaoIgnorada> list = AR.em().createQuery(sb.toString()).getResultList();

        return list;
    }

    public Long getIdConfiguracaoIgnorada() {
        return idConfiguracaoIgnorada;
    }

    public void setIdConfiguracaoIgnorada(Long idConfiguracaoIgnorada) {
        this.idConfiguracaoIgnorada = idConfiguracaoIgnorada;
    }

    public SrItemConfiguracao getItemConfiguracao() {
        return itemConfiguracao;
    }

    public void setItemConfiguracao(SrItemConfiguracao itemConfiguracao) {
        this.itemConfiguracao = itemConfiguracao;
    }

    public SrConfiguracao getConfiguracao() {
        return configuracao;
    }

    public void setConfiguracao(SrConfiguracao configuracao) {
        this.configuracao = configuracao;
    }

    protected Long getId() {
        return this.getIdConfiguracaoIgnorada();
    }
}
