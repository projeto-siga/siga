package br.gov.jfrj.siga.gc.model;

import javax.persistence.ColumnResult;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import br.gov.jfrj.siga.dp.CpMarca;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.ActiveRecord;

@Entity
@DiscriminatorValue("3")
@SqlResultSetMapping(name = "colunas_contagem", columns = {
		@ColumnResult(name = "id_marcador"),
		@ColumnResult(name = "descr_marcador"),
		@ColumnResult(name = "cont_pessoa"), @ColumnResult(name = "cont_lota") })
@NamedNativeQuery(name = "contarGcMarcas", query = ""
		+ "SELECT m.id_marcador, m.descr_marcador, c.cont_pessoa, c.cont_lota "
		+ "FROM "
		+ "	corporativo.cp_marcador m, "
		+ "	("
		+ "		SELECT id_marcador,"
		+ "		SUM(CASE WHEN id_pessoa_ini = :idPessoaIni THEN 1 ELSE 0 END) cont_pessoa,"
		+ "		SUM(CASE WHEN id_lotacao_ini = :idLotacaoIni THEN 1 ELSE 0 END) cont_lota "
		+ "		FROM corporativo.cp_marca marca"
		+ "		WHERE(dt_ini_marca IS NULL OR dt_ini_marca < sysdate)"
		+ "		AND (dt_fim_marca IS NULL OR dt_fim_marca > sysdate)"
		+ "		AND((id_pessoa_ini = :idPessoaIni) OR(id_lotacao_ini = :idLotacaoIni))"
		+ "		AND id_tp_marca = 3" + "		AND id_marcador <> "
		+ CpMarcador.MARCADOR_CANCELADO + "		GROUP BY id_marcador" + "	) c "
		+ "WHERE m.id_marcador = c.id_marcador", resultSetMapping = "colunas_contagem")
public class GcMarca extends CpMarca implements Comparable<GcMarca> {
	static ActiveRecord<GcMarca> AR = new ActiveRecord<GcMarca>(GcMarca.class);

	@ManyToOne
	@JoinColumn(name = "ID_REF")
	private GcInformacao inf;

	public GcInformacao getInf() {
		return inf;
	}

	public void setInf(GcInformacao inf) {
		this.inf = inf;
	}

	public GcMarca() {

	}

	public GcMarca(Long idMarcador, DpPessoa pessoa, DpLotacao lota,
			GcInformacao inf) {
		if (pessoa != null)
			setDpPessoaIni(pessoa.getPessoaInicial());
		setDpLotacaoIni(lota.getLotacaoInicial());
		setCpMarcador(AR.em().find(CpMarcador.class, idMarcador));
	}

	public String getDescricao() {
		return this.getCpMarcador().getDescrMarcador() + " ("
				+ getDpLotacaoIni().getSigla() + ")";
	}

	// public GcMarca salvar() {
	// JPA.em().persist(this);
	// return this;
	// }

	// @Override
	// public <T extends JPABase> T save() {
	// // Edson: Ver no Util o comentário sobre a chamada abaixo
	// if (getIdMarca() == null)
	// setIdMarca(Util.nextVal("CORPORATIVO.CP_MARCA_SEQ"));
	// return super.save();
	// }

	public int compareTo(GcMarca other) {
		int i = getCpMarcador().getIdMarcador().compareTo(
				other.getCpMarcador().getIdMarcador());
		if (i != 0)
			return i;
		if (getDpLotacaoIni() == null) {
			if (other.getDpLotacaoIni() == null)
				i = 0;
			else
				i = -1;
		} else {
			if (other.getDpLotacaoIni() == null)
				i = 1;
			else
				i = getDpLotacaoIni().getIdLotacao().compareTo(
						other.getDpLotacaoIni().getIdLotacao());
		}
		if (i != 0)
			return i;
		if (getDpPessoaIni() == null) {
			if (other.getDpPessoaIni() == null)
				i = 0;
			else
				i = -1;
		} else {
			if (other.getDpPessoaIni() == null)
				i = 1;
			else
				i = getDpPessoaIni().getIdPessoa().compareTo(
						other.getDpPessoaIni().getIdPessoa());
		}
		if (i != 0)
			return i;
		return 0;
	}

	@Override
	public String toString() {
		return getDescricao();
	}

	/*
	 * @Override public int compare(GcMarca o1, GcMarca o2) { int diff =
	 * o1.getDtIniMarca().compareTo(o2.getDtIniMarca()); if (diff == 0){ //
	 * equal on description diff =
	 * o1.getCpMarcador().getDescrMarcador().compareTo
	 * (o2.getCpMarcador().getDescrMarcador()); } return diff; }
	 */

}