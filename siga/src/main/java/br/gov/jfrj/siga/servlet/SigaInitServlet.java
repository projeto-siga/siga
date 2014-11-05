package br.gov.jfrj.siga.servlet;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;

import org.hibernate.cfg.AnnotationConfiguration;

import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.dao.HibernateUtil;

/**
 * Inicializa ambiente do siga
 */
public class SigaInitServlet extends HttpServlet implements Servlet {

       private static final long serialVersionUID = -2468366133341470474L;

    public SigaInitServlet() {
       try {
                       AnnotationConfiguration cfg = CpDao
                                       .criarHibernateCfg("java:/SigaCpDS");

                       HibernateUtil.configurarHibernate(cfg, "");
                       Cp.getInstance().getConf().inicializarCache();
               } catch (Exception e) {
                       // TODO Auto-generated catch block
                       e.printStackTrace();
               }
    }


}



