package br.gov.jfrj.siga.dp;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Historico;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;

@Cache(region = CpDao.CACHE_CORPORATIVO, usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class DpNotificarPorEmail extends CpConfiguracao implements Serializable, 
	Selecionavel, Historico, Sincronizavel, DpConvertableEntity {

	@Override
	public String getIdExterna() {
		return null;
	}

	@Override
	public void setIdExterna(String idExterna) { }

	@Override
	public void setIdInicial(Long idInicial) { }

	@Override
	public Date getDataInicio() {
		return null;
	}

	@Override
	public void setDataInicio(Date dataInicio) { }

	@Override
	public Date getDataFim() {
		return null;
	}

	@Override
	public void setDataFim(Date dataFim) { }

	@Override
	public String getLoteDeImportacao() {
		return null;
	}

	@Override
	public void setLoteDeImportacao(String loteDeImportacao) { }

	@Override
	public int getNivelDeDependencia() {
		return 0;
	}

	@Override
	public String getDescricaoExterna() {
		return null;
	}

	@Override
	public String getSigla() {
		return null;
	}

	@Override
	public void setSigla(String sigla) { }

	@Override
	public String getDescricao() {
		return null;
	}
	
}
