package br.gov.jfrj.siga.gc.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.gov.jfrj.siga.cp.CpConfiguracao;

@Entity
@Table(name = "sigagc.gc_configuracao")
@PrimaryKeyJoinColumn(name = "ID_CONFIGURACAO_GC")
public class GcConfiguracao extends CpConfiguracao // implements
// HistoricoPersistivel
{

	// SimulaÃ§Ã£o de heranÃ§a mÃºltipla
	// @Transient
	// private EntidadeSiga entidadePai = new EntidadeSiga(this);

	// @Column(name = "FORMA_ACOMPANHAMENTO")
	// public SrFormaAcompanhamento formaAcompanhamento;
	//
	@ManyToOne
	@JoinColumn(name = "ID_TIPO_INFORMACAO")
	private GcTipoInformacao tipoInformacao;

	public GcTipoInformacao getTipoInformacao() {
		return tipoInformacao;
	}

	public void setTipoInformacao(GcTipoInformacao tipoInformacao) {
		this.tipoInformacao = tipoInformacao;
	}

	public GcConfiguracao() {

	}

	// public GcConfiguracao(SrItemConfiguracao item, SrServico servico,
	// CpTipoConfiguracao tipo) {
	// this.itemConfiguracao = item;
	// this.servico = servico;
	// this.setCpTipoConfiguracao(tipo);
	// }
	//
	// public String getPesquisaSatisfacaoString() {
	// return pesquisaSatisfacao ? "Sim" : "NÃ£o";
	// }

	// @Override
	// public GcConfiguracao salvar() throws Exception {
	// entidadePai.salvar();
	// return this;
	// }
	//
	// @Override
	// public void finalizar() throws Exception {
	// entidadePai.finalizar();
	// }
	//
	// @Override
	// public void salvarSimples() throws Exception {
	// entidadePai.salvarSimples();
	// }
	//
	// @Override
	// public void darRefresh() throws Exception {
	// entidadePai.darRefresh();
	// }
	//
	// @Override
	// public void flushSeNecessario() throws Exception {
	// entidadePai.flushSeNecessario();
	// }
	//
	// @Override
	// public void destacar() {
	// entidadePai.destacar();
	// }
	//
	// @Override
	// public Persistivel buscarPorId(Long id) {
	// return entidadePai.buscarPorId(id);
	// }

}
