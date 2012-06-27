package models;

import javax.persistence.ColumnResult;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import br.gov.jfrj.siga.dp.CpMarca;

@Entity
@DiscriminatorValue("2")
@SqlResultSetMapping(name = "colunas_contagem", columns = {
		@ColumnResult(name = "id_marcador"),
		@ColumnResult(name = "descr_marcador"),
		@ColumnResult(name = "cont_pessoa"), @ColumnResult(name = "cont_lota") })
@NamedNativeQuery(name = "contarSrMarcas", query = "" +
		"SELECT m.id_marcador, m.descr_marcador, c.cont_pessoa, c.cont_lota " +
		"FROM " +
		"	corporativo.cp_marcador m, " +
		"	(" +
		"		SELECT id_marcador," +
		"		SUM(CASE WHEN id_pessoa_ini = :idPessoaIni THEN 1 ELSE 0 END) cont_pessoa," +
		"		SUM(CASE WHEN id_lotacao_ini = :idLotacaoIni THEN 1 ELSE 0 END) cont_lota " +
		"		FROM corporativo.cp_marca marca" +
		"		WHERE(dt_ini_marca IS NULL OR dt_ini_marca < sysdate)"+
		"		AND (dt_fim_marca IS NULL OR dt_fim_marca > sysdate)" +
		"		AND((id_pessoa_ini = :idPessoaIni) OR(id_lotacao_ini = :idLotacaoIni))" +
		"		AND id_tp_marca = 2" +
		"		GROUP BY id_marcador" +
		"	) c " +
		"WHERE m.id_marcador = c.id_marcador", resultSetMapping = "colunas_contagem")
public class SrMarca extends CpMarca {

	@ManyToOne
	@JoinColumn(name = "ID_REF")
	public SrSolicitacao solicitacao;

	public String getDescricao() {
		return this.getCpMarcador().getDescrMarcador() + " ("
				+ getDpLotacaoIni().getSigla() + ")";
	}

}
