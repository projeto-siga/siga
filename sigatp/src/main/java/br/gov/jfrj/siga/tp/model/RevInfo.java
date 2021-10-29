package br.gov.jfrj.siga.tp.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import br.gov.jfrj.siga.tp.listener.RevInfoListener;

@SuppressWarnings("serial")
@Entity
@Table(name = "revinfo", schema = "sigatp")
@RevisionEntity(RevInfoListener.class)
public class RevInfo implements Serializable {
	@Id
	@GeneratedValue(generator = "hibernate_sequence_generator") @SequenceGenerator(name = "hibernate_sequence_generator", sequenceName="sigatp.hibernate_sequence") 
	@RevisionNumber
	private int rev;

	@RevisionTimestamp
	private long revtstmp;
	private String matricula;
	private String enderecoIp;
	private String motivoLog;
	
	public int getRev() {
		return rev;
	}

	public void setRev(int rev) {
		this.rev = rev;
	}

    @Transient
    public Date getRevisionDate() {
        return new Date(revtstmp);
    }

	public long getRevtstmp() {
		return revtstmp;
	}

	public void setTimestamp(long revtstmp) {
		this.revtstmp = revtstmp;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getEnderecoIp() {
		return enderecoIp;
	}

	public void setEnderecoIp(String enderecoIp) {
		this.enderecoIp = enderecoIp;
	}

	public String getMotivoLog() {
		return motivoLog;
	}

	public void setMotivoLog(String motivoLog) {
		this.motivoLog = motivoLog;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof RevInfo))
			return false;

		RevInfo that = (RevInfo) o;

		if (rev != that.rev)
			return false;
		if (revtstmp != that.revtstmp)
			return false;

		return true;
	}

	public int hashCode() {
		int result;
		result = rev;
		result = 31 * result + (int) (revtstmp ^ (revtstmp >>> 32));
		return result;
	}

	public String toString() {
		return "DefaultRevisionEntity(id = " + rev + ", revisionDate = "
				+ DateFormat.getDateTimeInstance().format(getRevisionDate())
				+ ")";
	}
}
