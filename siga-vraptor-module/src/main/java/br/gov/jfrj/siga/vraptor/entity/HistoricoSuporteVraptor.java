package br.gov.jfrj.siga.vraptor.entity;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.MappedSuperclass;

import br.gov.jfrj.siga.cp.model.HistoricoSuporte;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.Historico;

@MappedSuperclass
public abstract class HistoricoSuporteVraptor extends HistoricoSuporte {

	private static final long serialVersionUID = 1340587106297071271L;

	public void salvar() throws Exception {
		EntityManager em = ContextoPersistencia.em();

		try {
			Historico thisHistorico = (Historico) this;
			thisHistorico.setHisDtIni(new Date());
			thisHistorico.setHisDtFim(null);
			if (thisHistorico.getId() == null) {
				this.save();
				thisHistorico.setHisIdIni(thisHistorico.getId());
			} else {
				em.detach(this);
				// Edson: Na linha abaixo, não funciona findById. Dá
				// UnsupportedOpException.
				// Isso porque ObjetoBase não está anotado com @Entity. No
				// máximo, é @MappedSuperclass
				// Veja
				// https://groups.google.com/forum/?fromgroups=#!topic/play-framework/waYNFtLCH40
				HistoricoSuporteVraptor thisAntigo = em.find(this.getClass(), thisHistorico.getId());

				if (((Historico) thisAntigo).getHisDtFim() == null) {
					thisAntigo.finalizar();
				}
				thisHistorico.setHisIdIni(((Historico) thisAntigo).getHisIdIni());
				thisHistorico.setId(null);
			}
			this.save();
			// em.refresh(this);
		} catch (ClassCastException cce) {
			this.save();
		}
	}

	public void finalizar() throws Exception {
		try {
			Historico historico = ((Historico) this);
			historico.setHisDtFim(new Date());
			this.save();
		} catch (ClassCastException cce) {
			this.save();
		}
	}
}