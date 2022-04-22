package br.gov.jfrj.siga.hibernate;

import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.ModeloDao;
import org.jboss.logging.Logger;

public class IntegracaoDao extends CpDao {
    public static final String CACHE_EX = "ex";

    private static final Logger log = Logger.getLogger(IntegracaoDao.class);

    public static IntegracaoDao getInstance() {
        return ModeloDao.getInstance(IntegracaoDao.class);
    }
}
