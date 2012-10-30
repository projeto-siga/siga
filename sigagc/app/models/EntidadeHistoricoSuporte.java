package models;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;

import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.model.HistoricoAuditavel;
import br.gov.jfrj.siga.dp.DpPessoa;

public class EntidadeHistoricoSuporte {

	private HistoricoPersistivel o;

	public EntidadeHistoricoSuporte(HistoricoPersistivel o) {
		this.o = o;
	}

	@SuppressWarnings("unused")
	private EntidadeHistoricoSuporte() {

	}

	public void salvar() throws Exception {
		try {
			o.setHisDtIni(new Date());
			o.setHisDtFim(null);
			if (o.getId() == null) {
				o.salvarSimples();
				o.setHisIdIni(o.getId());
			} else {
				o.destacar();
				HistoricoPersistivel oAntigo = (HistoricoPersistivel) o
						.buscarPorId(o.getId());
				oAntigo.finalizar();
				o.setHisIdIni(oAntigo.getHisIdIni());
			}

			o.salvarSimples();
			o.flushSeNecessario();

		} catch (IllegalAccessException iae) {
			// Erro no copyPoperties ou newInstance
			int a = 0;
		} catch (InstantiationException ie) {
			// Erro no newInstance
			int a = 0;
		}

	}

	public void finalizar() throws Exception {
		o.setHisDtFim(new Date());
		o.salvarSimples();
		o.flushSeNecessario();
	}

	public void copiar(HistoricoPersistivel dest, HistoricoPersistivel orig) {

		for (Method getter : orig.getClass().getDeclaredMethods()) {
			try {
				String getterName = getter.getName();
				if (!getterName.startsWith("get"))
					continue;
				if (Collection.class.isAssignableFrom(getter.getReturnType()))
					continue;
				String setterName = getterName.replace("get", "set");
				Object origValue = getter.invoke(orig);
				dest.getClass().getMethod(setterName, getter.getReturnType())
						.invoke(dest, origValue);
			} catch (NoSuchMethodException nSME) {
				int a = 0;
			} catch (IllegalAccessException iae) {
				int a = 0;
			} catch (IllegalArgumentException iae) {
				int a = 0;
			} catch (InvocationTargetException nfe) {
				int a = 0;
			}

		}

	}

}
