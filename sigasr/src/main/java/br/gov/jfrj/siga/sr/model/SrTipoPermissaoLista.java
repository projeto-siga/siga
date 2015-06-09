package br.gov.jfrj.siga.sr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.jfrj.siga.base.util.Catalogs;
import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.vraptor.converter.ConvertableEntity;
import br.gov.jfrj.siga.vraptor.entity.ObjetoVraptor;

@Entity
@Table(name = "SR_TIPO_PERMISSAO_LISTA", schema = Catalogs.SIGASR)
public class SrTipoPermissaoLista extends ObjetoVraptor implements ConvertableEntity {

    private static final long serialVersionUID = 1555809464123606397L;

    public static final ActiveRecord<SrTipoPermissaoLista> AR = new ActiveRecord<>(SrTipoPermissaoLista.class);

    public static final long GESTAO = 1;

    public static final long PRIORIZACAO = 2;

    public static final long INCLUSAO = 3;

    public static final long CONSULTA = 4;

    @Id
    @Column(name = "ID_TIPO_PERMISSAO")
    private Long idTipoPermissaoLista;

    @Column(name = "DESCR_TIPO_PERMISSAO", nullable = false)
    private String descrTipoPermissaoLista;

    public SrTipoPermissaoLista() {

    }

    public SrTipoPermissaoLista(int id, String descricao) {
        super();
        this.setIdTipoPermissaoLista(Long.valueOf(id));
        this.setDescrTipoPermissaoLista(descricao);
    }

    public Long getIdTipoPermissaoLista() {
        return idTipoPermissaoLista;
    }

    public String getDescrTipoPermissaoLista() {
        return descrTipoPermissaoLista;
    }

    /**
     * Classe que representa um V.O. de {@link SrTipoPermissaoLista}.
     */
    public class SrTipoPermissaoListaVO {

        public Long idTipoPermissaoLista;
        public String descrTipoPermissaoLista;

        public SrTipoPermissaoListaVO(Long id, String descricao) {
            this.idTipoPermissaoLista = id;
            this.descrTipoPermissaoLista = descricao;
        }
    }

    public SrTipoPermissaoListaVO toVO() {
        return new SrTipoPermissaoListaVO(this.getIdTipoPermissaoLista(), this.getDescrTipoPermissaoLista());
    }

    @Override
	public Long getId() {
        return this.getIdTipoPermissaoLista();
    }

    public void setIdTipoPermissaoLista(Long idTipoPermissaoLista) {
        this.idTipoPermissaoLista = idTipoPermissaoLista;
    }

    public void setDescrTipoPermissaoLista(String descrTipoPermissaoLista) {
        this.descrTipoPermissaoLista = descrTipoPermissaoLista;
    }

}
