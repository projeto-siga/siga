package br.gov.jfrj.siga.pp.models;

import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.Objeto;
import br.gov.jfrj.siga.pp.dao.PpDao;

public abstract class PpModel extends Objeto {

    private static final long serialVersionUID = -3265658962532346932L;
    public static final Long ID_VAZIO = 0L;

    @Override
    public void save() {
        PpDao.getInstance().gravar(this);
    }

    public void refresh() {
        ContextoPersistencia
            .em()
            .refresh(this);
    }

    public boolean ehNovo() {
        return getId() == null || ID_VAZIO.equals(getId());
    }

    public static boolean existe(String id) {
        return id != null && !ID_VAZIO.equals(id);
    }

    public abstract String getId();
}
