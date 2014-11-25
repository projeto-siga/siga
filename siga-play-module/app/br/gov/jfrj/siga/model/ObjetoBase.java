package br.gov.jfrj.siga.model;

import java.util.Date;

import javax.persistence.MappedSuperclass;

import play.db.jpa.GenericModel;
import play.db.jpa.JPA;

@MappedSuperclass
public abstract class ObjetoBase extends GenericModel {

	public void salvar() throws Exception {
		try {
			Historico thisHistorico = (Historico) this;
			thisHistorico.setHisDtIni(new Date());
			thisHistorico.setHisDtFim(null);
			if (thisHistorico.getId() == null) {
				this.save();
				thisHistorico.setHisIdIni(thisHistorico.getId());
			} else {
				JPA.em().detach(this);
				// Edson: Na linha abaixo, n�o funciona findById. D�
				// UnsupportedOpException.
				// Isso porque ObjetoBase n�o est� anotado com @Entity. No
				// m�ximo, � @MappedSuperclass
				// Veja
				// https://groups.google.com/forum/?fromgroups=#!topic/play-framework/waYNFtLCH40
				ObjetoBase thisAntigo = JPA.em().find(this.getClass(),
						thisHistorico.getId());
				
				if(((Historico) thisAntigo).getHisDtFim() == null) {
					thisAntigo.finalizar();
				}
				thisHistorico.setHisIdIni(((Historico) thisAntigo).getHisIdIni());
				thisHistorico.setId(null);
			}
			this.save();
			//this.refresh();
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