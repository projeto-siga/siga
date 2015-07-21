package br.gov.jfrj.siga.sr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.jfrj.siga.feature.converter.entity.vraptor.ConvertableEntity;
import br.gov.jfrj.siga.model.Objeto;

@Entity
@Table(name="SR_ATRIBUTO_SOLICITACAO", schema = "SIGASR")
public class SrAtributoSolicitacao extends Objeto implements ConvertableEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(sequenceName = "SIGASR" +".SR_ATRIBUTO_SOLICITACAO_SEQ", name = "srAtributoSolicitacaoSeq")
	@GeneratedValue(generator = "srAtributoSolicitacaoSeq")
	@Column(name = "ID_ATRIBUTO_SOLICITACAO")
    private long id;
	
	@Column(name = "VALOR_ATRIBUTO_SOLICITACAO")
    private String valorAtributoSolicitacao;
	
	@ManyToOne
	@JoinColumn(name="ID_ATRIBUTO")
    private SrAtributo atributo;
	
	@ManyToOne
	@JoinColumn(name="ID_SOLICITACAO")
    private SrSolicitacao solicitacao;
	
	public SrAtributoSolicitacao(){
	}
	
	public SrAtributoSolicitacao(SrAtributo tipo, String valor, SrSolicitacao sol){
		this.setAtributo(tipo);
		this.setValorAtributoSolicitacao(valor);
		this.setSolicitacao(sol);
	}

    @Override
    public Long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValorAtributoSolicitacao() {
        return valorAtributoSolicitacao;
    }

    public void setValorAtributoSolicitacao(String valorAtributoSolicitacao) {
        this.valorAtributoSolicitacao = valorAtributoSolicitacao;
    }

    public SrAtributo getAtributo() {
        return atributo;
    }

    public void setAtributo(SrAtributo atributo) {
        this.atributo = atributo;
    }

    public SrSolicitacao getSolicitacao() {
        return solicitacao;
    }

    public void setSolicitacao(SrSolicitacao solicitacao) {
        this.solicitacao = solicitacao;
    }
}
