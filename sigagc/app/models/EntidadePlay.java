package models;

import play.db.jpa.GenericModel;
import play.db.jpa.JPA;

public abstract class EntidadePlay extends GenericModel implements
		HistoricoPersistivel {

	private EntidadeHistoricoSuporte suporte = new EntidadeHistoricoSuporte(
			this);

	@Override
	public Persistivel salvar() throws Exception {
		suporte.salvar();
		return this;
	}

	@Override
	public void  finalizar() throws Exception {
		suporte.finalizar();
	}

	@Override
	public void salvarSimples() throws Exception {
		save();
	}

	@Override
	public void darRefresh() throws Exception {
		refresh();
	}

	@Override
	public void flushSeNecessario() throws Exception {

	}

	@Override
	public void destacar() {
		JPA.em().detach(this);
	}

	@Override
	public Persistivel buscarPorId(Long id) {
		return (Persistivel) findById(id);
	}

//	public SrConfiguracao getConfiguracao(SrItemConfiguracao item,
//			SrServico servico, long idTipo) throws Exception {
//
//		SrConfiguracao conf = new SrConfiguracao(item, servico, JPA.em().find(
//				CpTipoConfiguracao.class, idTipo));
//
//		return SrConfiguracaoBL.get().buscarConfiguracao(conf);
//	}

}
