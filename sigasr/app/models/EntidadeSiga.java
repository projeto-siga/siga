package models;

import java.util.Date;

import br.gov.jfrj.siga.model.Assemelhavel;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;

public class EntidadeSiga implements HistoricoPersistivel {

	private EntidadeHistoricoSuporte suporte = new EntidadeHistoricoSuporte(
			this);

	private HistoricoPersistivel entidadeFilha;

	public EntidadeSiga(HistoricoPersistivel entidadeFilha) {
		this.entidadeFilha = entidadeFilha;
	}

	@Override
	public void salvarSimples() throws Exception {
		JPA.em().persist(this);
	}

	@Override
	public void darRefresh() throws Exception {
		JPA.em().refresh(this);
	}

	@Override
	public void flushSeNecessario() throws Exception {
		JPA.em().flush();
	}

	@Override
	public Persistivel salvar() throws Exception {
		suporte.salvar();
		return this;
	}

	@Override
	public void finalizar() throws Exception {
		suporte.finalizar();
	}

	@Override
	public void destacar() {
		JPA.em().detach(this);
	}

	@Override
	public Persistivel buscarPorId(Long id) {
		return JPA.em().find(entidadeFilha.getClass(), id);
	}

	@Override
	public Long getIdInicial() {
		return entidadeFilha.getIdInicial();
	}

	@Override
	public boolean equivale(Object other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Long getId() {
		return entidadeFilha.getId();
	}

	@Override
	public void setId(Long id) {
		entidadeFilha.setId(id);

	}

	@Override
	public Long getHisIdIni() {
		return entidadeFilha.getHisIdIni();
	}

	@Override
	public void setHisIdIni(Long hisIdIni) {
		entidadeFilha.setHisIdIni(hisIdIni);
	}

	@Override
	public Date getHisDtIni() {
		return entidadeFilha.getHisDtIni();
	}

	@Override
	public void setHisDtIni(Date hisDtIni) {
		entidadeFilha.setHisDtIni(hisDtIni);
	}

	@Override
	public Date getHisDtFim() {
		return entidadeFilha.getHisDtFim();
	}

	@Override
	public void setHisDtFim(Date hisDtFim) {
		entidadeFilha.setHisDtFim(hisDtFim);
	}

	@Override
	public int getHisAtivo() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setHisAtivo(int hisAtivo) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		// TODO Auto-generated method stub
		return false;
	}

}
