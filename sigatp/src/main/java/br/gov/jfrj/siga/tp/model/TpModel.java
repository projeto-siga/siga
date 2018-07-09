package br.gov.jfrj.siga.tp.model;

import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.Objeto;

/**
 * Classe base para as entidades do siga-tp
 *
 * @author db1
 *
 */
public abstract class TpModel extends Objeto {

	private static final long serialVersionUID = -3265658962532346951L;
	public static final Long ID_VAZIO = 0L;

	@Override
	public void save() {
		TpDao.getInstance().gravar(this);
	}

	public void refresh() {
		ContextoPersistencia
			.em()
			.refresh(this);
	}

	public boolean ehNovo() {
		return getId() == null || ID_VAZIO.equals(getId());
	}

	public static boolean existe(Long id) {
		return id != null && !ID_VAZIO.equals(id);
	}

	public abstract Long getId();
}
