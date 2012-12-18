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
import br.gov.jfrj.siga.model.Objeto;

import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.db.jpa.JPABase;
import play.db.jpa.Model;

@Entity
@Table(name = "SR_SERVICO")
public class SrServico extends ObjetoPlayComHistorico implements SrSelecionavel{

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
	public int getHisAtivo() {
		return getHisDtFim() != null ? 1 : 0;
	}

	@Override
	public void setHisAtivo(int hisAtivo) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}

	@Override
	public SrServico selecionar(String sigla) {
		setSigla(sigla);
		List<SrServico> itens = buscar();
		if (itens.size() == 0 || itens.size() > 1)
			return null;
		return itens.get(0);

	}

	@Override
	public List<SrServico> buscar() {
		String query = "from SrServico where 1=1";
		if (tituloServico != null && !tituloServico.equals("")) {
			for (String s : tituloServico.toLowerCase().split("\\s"))
				query += " and lower(tituloServico) like '%" + s + "%'";
		}
		if (siglaServico != null && !siglaServico.equals(""))
			query += " and siglaServico like '%" + siglaServico + "%' ";
		query += " and hisDtFim is null";
		return SrServico.find(query).fetch();
	}

	@Override
	public void setSigla(String sigla) {
		if (sigla == null) {
			tituloServico = "";
			siglaServico = "";
		} else {
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

	public int getNivel() {
		int camposVazios = 0;
		int pos = getSigla().indexOf(".00", 0);
		while (pos > -1) {
			camposVazios++;
			pos = getSigla().indexOf(".00", pos + 1);
		}
		return 2 - camposVazios;
	}
	
	public boolean isPaiDeOuIgualA(SrServico outroServico) {
		if (outroServico == null || outroServico.getSigla() == null)
			return false;
		if (this.equals(outroServico))
			return true;
		int posFimComparacao = getSigla().indexOf(".00");
		if (posFimComparacao < 0)
			posFimComparacao = getSigla().length()-1;
		return outroServico.getSigla().startsWith(
				getSigla().substring(0, posFimComparacao + 1));
	}

	public boolean isFilhoDeOuIgualA(SrServico outroItem) {
		return outroItem.isPaiDeOuIgualA(this);
	}
	
	public static List<SrServico> listar(){
		return SrServico.find("byHisDtFimIsNull").fetch();
	}

}
