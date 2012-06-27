package controllers;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.persistence.FlushModeType;

import models.SrConfiguracao;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.FlushMode;
import org.hibernate.Session;

import play.db.jpa.JPA;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.model.HistoricoAuditavel;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Historico;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.model.dao.ModeloDao;

public class SrDao extends CpDao {

	public static SrDao getInstance() {
		// Isso aqui está péssimo. Melhorar.

		Session playSession = (Session) JPA.em().getDelegate();
		playSession.setFlushMode(FlushMode.ALWAYS);
		SrDao dao = getInstance(SrDao.class, playSession);
		dao.sessao = (Session) JPA.em().getDelegate();

		return dao;

	}

	public void salvar(HistoricoAuditavel o, String principal)
			throws AplicacaoException {
		salvar(o, idAtiva(principal));
	}

	public void salvar(HistoricoAuditavel o, CpIdentidade id)
			throws AplicacaoException {
		try {
			HistoricoAuditavel oNovo;

			if (o.getId() == null) {
				oNovo = o;
			} else {
				HistoricoAuditavel oAnterior = JPA.em().find(o.getClass(),
						o.getId());
				oAnterior.setHisDtFim(consultarDataEHoraDoServidor());
				JPA.em().persist(oAnterior);

				oNovo = o.getClass().newInstance();
				PropertyUtils.copyProperties(oNovo, o);
				oNovo.setId(null);
				oNovo.setHisIdIni(oAnterior.getHisIdIni());
				oNovo.setHisIdcIni(oAnterior.getHisIdcIni());
			}

			oNovo.setHisDtIni(consultarDataEHoraDoServidor());
			JPA.em().persist(oNovo);
			JPA.em().getTransaction().commit();
			
			Long id2 = oNovo.getId();
			oNovo = o.getClass().newInstance();
			
			JPA.em().getTransaction().begin();
			oNovo = JPA.em().find(oNovo.getClass(), id2);
			oNovo.setHisIdIni(oNovo.getId());
			oNovo.setHisIdcIni(id);
			oNovo.setHisDtIni(new Date());
			JPA.em().getTransaction().commit();
			JPA.em().getTransaction().begin();
			int a = 0;

		} catch (IllegalAccessException iae) {
			// Erro no copyPoperties ou newInstance
			int a = 0;
		} catch (NoSuchMethodException nsme) {
			// Erro no copyPoperties
			int a = 0;
		} catch (InvocationTargetException ite) {
			// Erro no copyPoperties
			int a = 0;
		} catch (InstantiationException ie) {
			// Erro no newInstance
			int a = 0;
		}

	}

	public CpIdentidade idAtiva(String principal) throws AplicacaoException {
		return consultaIdentidadeCadastrante(principal, true);
	}

}
