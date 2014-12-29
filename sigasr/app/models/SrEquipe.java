package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Assemelhavel;

@Entity
@Table(name = "SR_EQUIPE", schema = "SIGASR")
public class SrEquipe extends HistoricoSuporte {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(sequenceName = "SIGASR.SR_EQUIPE_SEQ", name = "srEquipeSeq")
	@GeneratedValue(generator = "srEquipeSeq")
	@Column(name = "ID_EQUIPE")
	public Long idEquipe;
	
	@ManyToOne
	@JoinColumn(name = "ID_LOTA_EQUIPE")
	public DpLotacao lotacao;
	
	@OneToMany(targetEntity = SrExcecaoHorario.class, mappedBy = "equipe", fetch = FetchType.LAZY)
	public List<SrExcecaoHorario> excecaoHorarioSet;

	@Override
	public Long getId() {
		return this.idEquipe;
	}

	@Override
	public void setId(Long id) {
		this.idEquipe = id;
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return false;
	}
	
	// Edson: Nao foi possivel deixar cascade automatico.
	// Isso porque, no primeiro salvamento, o formulario nao consegue
	// fazer automaticamente a conexao abaixo, entre os horarios e a equipe,
	// visto que a equipe nao tem ID
	@Override
	public void salvar() throws Exception {
		super.salvar();
		if (excecaoHorarioSet != null)
			for (SrExcecaoHorario eh : excecaoHorarioSet) {
				eh.equipe = this;
				eh.salvar();
			}
	}

	public List<SrConfiguracao> getDesignacoes() throws Exception {
		if (lotacao == null)
			return null;
		else
			return SrConfiguracao.listarDesignacoes(false, lotacao);
	}

	public static List<SrEquipe> listar(boolean mostrarDesativados) {
		StringBuffer sb = new StringBuffer();
		
		if (!mostrarDesativados)
			sb.append(" hisDtFim is null ");
		else {
			sb.append(" idEquipe in (");
			sb.append(" SELECT max(idEquipe) as idEquipe FROM ");
			sb.append(" SrEquipe GROUP BY hisIdIni) ");
		}
		
		return SrEquipe.find(sb.toString()).fetch();
		
	}
	
	public boolean podeEditar(DpLotacao lotaTitular, DpPessoa titular){
		return lotaTitular.equivale(this.lotacao);
	}

}
