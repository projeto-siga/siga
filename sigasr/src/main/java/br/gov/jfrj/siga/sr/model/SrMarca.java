package br.gov.jfrj.siga.sr.model;

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
import br.gov.jfrj.siga.model.ContextoPersistencia;

@Entity
@DiscriminatorValue("2")
@SqlResultSetMapping(name = "colunas_contagem", columns = { @ColumnResult(name = "id_marcador"), @ColumnResult(name = "descr_marcador"), @ColumnResult(name = "cont_pessoa"),
        @ColumnResult(name = "cont_lota"), @ColumnResult(name = "cont_unassigned") })
@NamedNativeQuery(name = "contarSrMarcas", query = "" + "SELECT m.id_marcador, m.descr_marcador, c.cont_pessoa, c.cont_lota, c.cont_unassigned " + "FROM " + "corporativo" + ".cp_marcador m, "
        + "	(" + "		SELECT id_marcador," + "		SUM(CASE WHEN id_pessoa_ini = :idPessoaIni THEN 1 ELSE 0 END) cont_pessoa,"
        + "		SUM(CASE WHEN id_lotacao_ini = :idLotacaoIni THEN 1 ELSE 0 END) cont_lota, "
        + "		SUM(CASE WHEN id_lotacao_ini = :idLotacaoIni and id_pessoa_ini is null THEN 1 ELSE 0 END) cont_unassigned " + "		FROM " + "corporativo" + ".cp_marca marca"
        + "		WHERE(dt_ini_marca IS NULL OR dt_ini_marca < 	:dbDatetime)" + "		AND (dt_fim_marca IS NULL OR dt_fim_marca > 	:dbDatetime)"
        + "		AND ((id_pessoa_ini = :idPessoaIni) OR (id_lotacao_ini = :idLotacaoIni))" + "		AND id_tp_marca = 2" + "		GROUP BY id_marcador" + "	) c " + "WHERE m.id_marcador = c.id_marcador "
        + " AND m.id_marcador not in (43, 45)" + " order by m.descr_marcador", resultSetMapping = "colunas_contagem")
public class SrMarca extends CpMarca implements Comparable<SrMarca> {

    private static final long serialVersionUID = 1494193540156064691L;
    
    @ManyToOne
    @JoinColumn(name = "ID_REF")
    private SrSolicitacao solicitacao;

    public SrMarca() {

    }

    public SrMarca(Long idMarcador, DpPessoa pessoa, DpLotacao lota, SrSolicitacao sol) {
        if (pessoa != null)
            setDpPessoaIni(pessoa.getPessoaInicial());
        setDpLotacaoIni(lota.getLotacaoInicial());
        setCpMarcador(ContextoPersistencia.em().find(CpMarcador.class, idMarcador));
        setSolicitacao(sol);
    }

    public String getDescricao() {
        return this.getCpMarcador().getDescrMarcador() + " (" + getDpLotacaoIni().getSigla() + ")";
    }

    @Override
    public int compareTo(SrMarca other) {
        int i = getCpMarcador().getIdMarcador().compareTo(other.getCpMarcador().getIdMarcador());
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
                i = getDpLotacaoIni().getIdLotacao().compareTo(other.getDpLotacaoIni().getIdLotacao());
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
                i = getDpPessoaIni().getIdPessoa().compareTo(other.getDpPessoaIni().getIdPessoa());
        }
        if (i != 0)
            return i;
        return 0;
    }

    public SrSolicitacao getSolicitacao() {
        return solicitacao;
    }

    public void setSolicitacao(SrSolicitacao solicitacao) {
        this.solicitacao = solicitacao;
    }

}
