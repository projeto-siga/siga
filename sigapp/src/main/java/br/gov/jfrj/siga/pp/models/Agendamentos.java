package br.gov.jfrj.siga.pp.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.jfrj.siga.model.ActiveRecord;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.Objeto;
import br.gov.jfrj.siga.pp.dao.PpDao;

@Entity(name = "Agendamentos")
@Table(name = "Agendamentos", schema = "SIGAPMP")
public class Agendamentos extends Objeto {
    private static final long serialVersionUID = -1581620032066997549L;
    public static final ActiveRecord<Agendamentos> AR = new ActiveRecord<>(Agendamentos.class);
    @Id()
    @Column(name = "data_ag", nullable = false)
    private Date data_ag;
    @Id()
    @Column(name = "hora_ag", length = 4, nullable = false)
    private String hora_ag;
    @Id()
    @ManyToOne
    @JoinColumn(name = "cod_local", nullable = false)
    // FK
    private Locais localFk;
    @Column(name = "matricula", length = 9, nullable = true)
    private String matricula;
    @Column(name = "sesb_pessoa" , length = 2, nullable = true)
    private String sesb_pessoa;
    @Column(name = "periciado", length = 50, nullable = true)
    private String periciado;
    @Column(name = "perito_juizo", length = 50, nullable = true)
    private String perito_juizo;
    @Column(name = "perito_parte", length = 50, nullable = true)
    private String perito_parte;
    @Column(name = "processo", length = 50, nullable = true)
    private String processo;
    @Column(name = "orgao", length = 15, nullable = true)
    private String orgao;
    
    public Agendamentos() {
    }

    public Agendamentos(Date data_ag, String hora_ag, Locais localFk, String matricula, String sesb_pessoa, String periciado, String perito_juizo, String perito_parte, String processo, String orgao) {
        this.data_ag = data_ag;
        this.hora_ag = hora_ag;
        this.localFk = localFk;
        this.matricula = matricula;
        this.sesb_pessoa = sesb_pessoa;
        this.periciado = periciado;
        this.perito_juizo = perito_juizo;
        this.perito_parte = perito_parte;
        this.processo = processo;
        this.orgao = orgao;
    }
    
    public Date getData_ag() {
        return data_ag;
    }

    public void setData_ag(Date data_ag) {
        this.data_ag = data_ag;
    }

    public String getHora_ag() {
        return hora_ag;
    }

    public void setHora_ag(String hora_ag) {
        this.hora_ag = hora_ag;
    }

    public Locais getLocalFk() {
        return localFk;
    }

    public void setLocalFk(Locais localFk) {
        this.localFk = localFk;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    public String getSesb_pessoa(){
    	return sesb_pessoa;
    }
    public void setSesb_Pessoa(String sesb_pessoa){
    	this.sesb_pessoa = sesb_pessoa;
    }
    public String getPericiado() {
        return periciado;
    }

    public void setPericiado(String periciado) {
        this.periciado = periciado;
    }

    public String getPerito_juizo() {
        return perito_juizo;
    }

    public void setPerito_juizo(String perito_juizo) {
        this.perito_juizo = perito_juizo;
    }

    public String getPerito_parte() {
        return perito_parte;
    }

    public void setPerito_parte(String perito_parte) {
        this.perito_parte = perito_parte;
    }

    public String getProcesso() {
        return processo;
    }

    public void setProcesso(String processo) {
        this.processo = processo;
    }

    public String getOrgao() {
        return orgao;
    }

    public void setOrgao(String orgao) {
        this.orgao = orgao;
    }

    @Override
    public void save() {
        PpDao.getInstance().gravar(this);
    }
    
    @Override
    public void delete() {
        ContextoPersistencia.em().remove(this);
    }

}
