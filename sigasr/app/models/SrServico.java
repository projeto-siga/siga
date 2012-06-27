package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.model.HistoricoAuditavel;
import br.gov.jfrj.siga.model.Assemelhavel;

import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.db.jpa.JPABase;
import play.db.jpa.Model;

@Entity
@Table(name = "SR_SERVICO")
public class SrServico extends GenericModel implements SrSelecionavel,
		HistoricoAuditavel {

	@Id
	@GeneratedValue
	@Column(name = "ID_SERVICO")
	public Long idServico;

	@Column(name = "SIGLA_SERVICO")
	public String siglaServico;

	@Column(name = "DESCR_SERVICO")
	public String descrServico;

	@Column(name = "TITULO_SERVICO")
	public String tituloServico;

	@Column(name = "HIS_ID_INI")
	private Long hisIdIni;

	@Column(name = "HIS_DT_INI")
	private Date hisDtIni;

	@Column(name = "HIS_DT_FIM")
	private Date hisDtFim;

	@Column(name = "HIS_ATIVO")
	private int hisAtivo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HIS_IDC_INI")
	private CpIdentidade hisIdcIni;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HIS_IDC_FIM")
	private CpIdentidade hisIdcFim;

	public SrServico() {
		this(null, null);
	}

	public SrServico(String descricao) {
		this(descricao, null);
	}

	public SrServico(String sigla, String descricao) {
		this.tituloServico = descricao;
		this.siglaServico = sigla;
	}

	@Override
	public Long getId() {
		return this.idServico;
	}

	public void setId(Long id) {
		idServico = id;
	}

	@Override
	public String getSigla() {
		return this.siglaServico;
	}

	@Override
	public String getDescricao() {
		return tituloServico;
	}

	@Override
	public void setDescricao(String descricao) {
		this.tituloServico = descricao;
	}

	@Override
	public Long getIdInicial() {
		return hisIdIni;
	}

	@Override
	public boolean equivale(Object other) {
		return false;
	}

	@Override
	public Long getHisIdIni() {
		return hisIdIni;
	}

	@Override
	public void setHisIdIni(Long hisIdIni) {
		this.hisIdIni = hisIdIni;
	}

	@Override
	public Date getHisDtIni() {
		return hisDtIni;
	}

	@Override
	public void setHisDtIni(Date hisDtIni) {
		this.hisDtIni = hisDtIni;
	}

	@Override
	public Date getHisDtFim() {
		return hisDtFim;
	}

	@Override
	public void setHisDtFim(Date hisDtFim) {
		this.hisDtFim = hisDtFim;
	}

	@Override
	public int getHisAtivo() {
		return getHisDtFim() != null ? 1 : 0;
	}

	@Override
	public void setHisAtivo(int hisAtivo) {
		// TODO Auto-generated method stub

	}

	@Override
	public CpIdentidade getHisIdcIni() {
		return hisIdcIni;
	}

	@Override
	public void setHisIdcIni(CpIdentidade hisIdcIni) {
		this.hisIdcIni = hisIdcIni;
	}

	@Override
	public CpIdentidade getHisIdcFim() {
		return hisIdcFim;
	}

	@Override
	public void setHisIdcFim(CpIdentidade hisIdcFim) {
		this.hisIdcFim = hisIdcFim;
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	@Override
	public SrServico selecionar(String sigla) {
		setSigla(sigla);
		List<SrServico> itens;
		if (getSigla() != null)
			itens = (List<SrServico>) JPA
					.em()
					.createQuery(
							"from SrServico where siglaServico like '%"
									+ getSigla() + "%'", SrServico.class)
					.getResultList();
		else
			itens = (List<SrServico>) JPA
					.em()
					.createQuery(
							"from SrServico where lower(tituloServico) like '%"
									+ getDescricao().toLowerCase() + "%'", getClass())
					.getResultList();
		if (itens.size() == 0 || itens.size() > 1)
			return null;
		return itens.get(0);

	}

	@Override
	public List<SrServico> buscar() {
		String query = "from SrServico where 1=1";
		if (getDescricao() != null && !getDescricao().equals(""))
			query += " and lower(tituloServico) like '%"
					+ getDescricao().toLowerCase() + "%'";
		if (getSigla() != null && !getSigla().equals(""))
			query += " and siglaServico like '%" + getSigla() + "%'";
		return SrServico.find(query).fetch();
	}

	@Override
	public void setSigla(String sigla) {
		String padrao = "([0-9][0-9]).?([0-9][0-9])";
		final Pattern p1 = Pattern.compile("^" + padrao);
		final Matcher m1 = p1.matcher(sigla);
		if (m1.find()) {
			String s = "";
			for (int i = 1; i <= m1.groupCount(); i++) {
				s += m1.group(i);
				s += (i < m1.groupCount()) ? "." : "";
			}
			siglaServico = s;
		} else
			tituloServico = sigla;
	}

}
