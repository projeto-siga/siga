package br.gov.jfrj.siga.vraptor.entity;

import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.Objeto;

/**
 * Classe base para os objetos
 * 
 * @author leonardo.lopes
 *
 */
public abstract class ObjetoVraptor extends Objeto {

	private static final long serialVersionUID = -2155359850671122370L;

	public static final Long ID_VAZIO = 0L;

	public void salvar() {
		save();
	}
	
	@Override
	public void save() {
		CpDao.getInstance().gravar(this);
	}

	public void refresh() {
		ContextoPersistencia.em().refresh(this);
	}

	public boolean ehNovo() {
		return getId() == null || ID_VAZIO.equals(getId());
	}

	protected abstract Long getId();
}