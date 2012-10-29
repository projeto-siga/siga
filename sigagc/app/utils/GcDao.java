package utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import models.HistoricoPersistivel;

import org.hibernate.Session;

import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.model.HistoricoAuditavel;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class GcDao extends CpDao {

	public static GcDao getInstance() {
		// Isso aqui está péssimo. Melhorar.

		Session playSession = (Session) JPA.em().getDelegate();
		GcDao dao = getInstance(GcDao.class, playSession);
		dao.sessao = playSession;

		return dao;

	}

	public <T extends HistoricoAuditavel> T salvar(T o, DpPessoa pessoa)
			throws AplicacaoException {
		return salvar(o, idAtiva(pessoa.getSigla()));
	}

	public <T extends HistoricoPersistivel> T salvar(T o, DpPessoa pessoa)
			throws AplicacaoException {
		return null;
	}

	public <T extends HistoricoPersistivel> T salvar(T o, String principal)
			throws AplicacaoException {
		return null;
	}

	public <T extends HistoricoAuditavel> T salvar(T o, String principal)
			throws AplicacaoException {
		return salvar(o, idAtiva(principal));
	}

	public <T extends HistoricoAuditavel> T salvar(T o, CpIdentidade id)
			throws AplicacaoException {
		try {
			T oNovo = (T) o.getClass().newInstance();

			// Entidades do Play não devem receber persist + flush, mas sim
			// save(), porque, ao executar
			// flush, o Hibernate vê se a entidade está suja e tem de ser
			// gravada, sendo que pra isso chama o PlayPlugin
			// (session.getInterceptor()). Se a
			// entidade
			// é do Play e não foi dado save(), o Plugin e engana o
			// Hibernate, fingindo que a entidade está limpa e que não deve ser
			// persistida, invalidando o flush.
			oNovo = persist(oNovo);
			Long idNovo = oNovo.getId();
			copiar(oNovo, o);
			oNovo.setId(idNovo);
			oNovo.setHisDtIni(consultarDataEHoraDoServidor());
			oNovo.setHisDtFim(null);

			if (o.getId() == null) {
				oNovo.setHisIdIni(oNovo.getId());
				oNovo.setHisIdcIni(id);
			} else {
				o = refresh(o);
				o = finalizar(o);
				oNovo.setHisIdIni(o.getHisIdIni());
				oNovo.setHisIdcIni(o.getHisIdcIni());
			}

			oNovo = persist(oNovo);
			flushIfNeeded(oNovo);

			return oNovo;

		} catch (IllegalAccessException iae) {
			// Erro no copyPoperties ou newInstance
			int a = 0;
		} catch (InstantiationException ie) {
			// Erro no newInstance
			int a = 0;
		}

		return null;

	}

	public <T> T salvar(T o) {
		JPA.em().persist(o);
		JPA.em().flush();

		return o;
	}

	public <T extends HistoricoAuditavel> T persist(T o) {
		if (o instanceof GenericModel) {
			((GenericModel) o).save();
		} else {
			JPA.em().persist(o);
		}
		return o;
	}

	public <T extends HistoricoAuditavel> T flushIfNeeded(T o) {
		if (!(o instanceof GenericModel))
			JPA.em().flush();
		return o;
	}

	public <T extends HistoricoAuditavel> T refresh(T o) {

		if (o instanceof GenericModel) {
			((GenericModel) o).refresh();
		} else {
			JPA.em().refresh(o);
		}
		return o;
	}

	public <T extends HistoricoAuditavel> T finalizar(T o)
			throws AplicacaoException {
		o.setHisDtFim(consultarDataEHoraDoServidor());
		o = persist(o);
		o = flushIfNeeded(o);
		return o;
	}

	public CpIdentidade idAtiva(String principal) throws AplicacaoException {
		return consultaIdentidadeCadastrante(principal, true);
	}
	
	public void copiar(HistoricoPersistivel dest, HistoricoPersistivel orig) {
		
	}

	public void copiar(HistoricoAuditavel dest, HistoricoAuditavel orig) {

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
