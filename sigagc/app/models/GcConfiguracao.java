package models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.gov.jfrj.siga.cp.CpConfiguracao;

@Entity
@Table(name = "SR_CONFIGURACAO")
@PrimaryKeyJoinColumn(name = "ID_CONFIGURACAO_SR")
public class GcConfiguracao extends CpConfiguracao //implements HistoricoPersistivel 
{

	// Simulação de herança múltipla
//	@Transient
//	private EntidadeSiga entidadePai = new EntidadeSiga(this);

//	@Column(name = "FORMA_ACOMPANHAMENTO")
//	public SrFormaAcompanhamento formaAcompanhamento;
//
	@ManyToOne
	@JoinColumn(name = "ID_TIPO_INFORMACAO")
	public GcTipoInformacao tipoInformacao;
	
	@ManyToOne
	@JoinColumn(name = "ID_TIPO_CLASSIFICACAO")
	public CpTipoClassificacao tipoClassificacao;
//
//	@ManyToOne
//	@JoinColumn(name = "ID_SERVICO")
//	public SrServico servico;
//
//	@Column(name = "GRAVIDADE")
//	public SrGravidade gravidade;
//
//	@Column(name = "TENDENCIA")
//	public SrTendencia tendencia;
//
//	@Column(name = "URGENCIA")
//	public SrUrgencia urgencia;
//
//	@ManyToOne
//	@JoinColumn(name = "ID_PRE_ATENDENTE")
//	public DpLotacao preAtendente;
//
//	@ManyToOne
//	@JoinColumn(name = "ID_ATENENTE")
//	public DpLotacao atendente;
//
//	@ManyToOne
//	@JoinColumn(name = "ID_POS_ATENDENTE")
//	public DpLotacao posAtendente;
//
//	@Column(name = "PESQUISA_SATISFACAO")
//	@Type(type = "yes_no")
//	public boolean pesquisaSatisfacao;

	public GcConfiguracao() {

	}

//	public GcConfiguracao(SrItemConfiguracao item, SrServico servico,
//			CpTipoConfiguracao tipo) {
//		this.itemConfiguracao = item;
//		this.servico = servico;
//		this.setCpTipoConfiguracao(tipo);
//	}
//
//	public String getPesquisaSatisfacaoString() {
//		return pesquisaSatisfacao ? "Sim" : "Não";
//	}
	
	

//	@Override
//	public GcConfiguracao salvar() throws Exception {
//		entidadePai.salvar();
//		return this;
//	}
//
//	@Override
//	public void finalizar() throws Exception {
//		entidadePai.finalizar();
//	}
//
//	@Override
//	public void salvarSimples() throws Exception {
//		entidadePai.salvarSimples();
//	}
//
//	@Override
//	public void darRefresh() throws Exception {
//		entidadePai.darRefresh();
//	}
//
//	@Override
//	public void flushSeNecessario() throws Exception {
//		entidadePai.flushSeNecessario();
//	}
//
//	@Override
//	public void destacar() {
//		entidadePai.destacar();
//	}
//
//	@Override
//	public Persistivel buscarPorId(Long id) {
//		return entidadePai.buscarPorId(id);
//	}

}
