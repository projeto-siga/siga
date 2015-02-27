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
import javax.persistence.Transient;

import models.vo.SelecionavelVO;
import util.Util;
import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.model.Assemelhavel;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
	
	@Transient
	private DpLotacao lotacaoEquipe;

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
			return SrConfiguracao.listarDesignacoes(this);
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

	public String toJson() {
		Gson gson = Util.createGson("lotacao", "lotacaoEquipe", "excecaoHorarioSet");
		
		JsonObject jsonObject = (JsonObject) gson.toJsonTree(this);
		jsonObject.add("ativo", gson.toJsonTree(isAtivo()));
		jsonObject.add("excecaoHorarioSet", excecaoHorarioArray());
		jsonObject.add("lotacaoEquipe", gson.toJsonTree(SelecionavelVO.createFrom(this.lotacao)));
		
		return jsonObject.toString();
	}
	
	private JsonArray excecaoHorarioArray() {
		Gson gson = Util.createGson("equipe");
		JsonArray jsonArray = new JsonArray();
		
		if (this.excecaoHorarioSet != null)
			for (SrExcecaoHorario srExcecaoHorario : this.excecaoHorarioSet) {
				JsonObject jsonObjectExcecao = (JsonObject) gson.toJsonTree(srExcecaoHorario);
				
				if (srExcecaoHorario.diaSemana != null)
					jsonObjectExcecao.add("descrDiaSemana", gson.toJsonTree(srExcecaoHorario.diaSemana.descrDiaSemana));
				
				jsonArray.add(jsonObjectExcecao);
			}
		
		return jsonArray;
	}
	
	public DpLotacao getLotacaoEquipe() {
		return this.lotacaoEquipe != null ? this.lotacaoEquipe : this.lotacao;
	}

	public void setLotacaoEquipe(DpLotacao lotacaoEquipe) {
		this.lotacaoEquipe = lotacaoEquipe;
		this.lotacao = lotacaoEquipe;
	}
}
