package br.gov.jfrj.siga.dp;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Assemelhavel;
import br.gov.jfrj.siga.model.Historico;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;
import br.gov.jfrj.siga.sinc.lib.SincronizavelSuporte;

@SuppressWarnings("serial")
@Entity
@Table(name = "corporativo.dp_notificar_por_email")
@Cache(region = CpDao.CACHE_CORPORATIVO, usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class DpNotificarPorEmail extends AbstractDpNotificarPorEmail implements Serializable, 
	Selecionavel, Historico, Sincronizavel, DpConvertableEntity {

	public DpNotificarPorEmail() { }
	
	@Override
	public Long getIdInicial() {
		return getId();
	}

	@Override
	public boolean equivale(Object other) {
		if (other == null)
			return false;
		
		return this.getIdInicial().longValue() == ((DpNotificarPorEmail) other)
				.getIdInicial().longValue();
	}

	@Override
	public Long getHisIdIni() {
		return getIdNotificarPorEmailIni();
	}

	@Override
	public void setHisIdIni(Long hisIdIni) {
		setIdNotificarPorEmailIni(hisIdIni);
	}

	@Override
	public Date getHisDtIni() {
		return getDataInicioNotificarPorEmail();
	}

	@Override
	public void setHisDtIni(Date hisDtIni) {
		setDataInicioNotificarPorEmail(hisDtIni);
	}

	@Override
	public Date getHisDtFim() {
		return getDataFimNotificarPorEmail();
	}

	@Override
	public void setHisDtFim(Date hisDtFim) {
		setDataFimNotificarPorEmail(hisDtFim);
	}

	@Override
	public Integer getHisAtivo() {
		return getDataFimNotificarPorEmail() != null ? 1 : 0;
	}

	@Override
	public void setHisAtivo(Integer hisAtivo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean semelhante(Assemelhavel obj, int profundidade) {
		return SincronizavelSuporte.semelhante(this, obj, profundidade);
	}

	@Override
	public String getIdExterna() {
		return getIdeNotificarPorEmail();
	}

	@Override
	public void setIdExterna(String idExterna) {
		setIdeNotificarPorEmail(idExterna);
	}

	@Override
	public void setIdInicial(Long idInicial) {
		setIdNotificarPorEmailIni(idInicial);
	}

	@Override
	public Date getDataInicio() {
		return getDataInicioNotificarPorEmail();
	}

	@Override
	public void setDataInicio(Date dataInicio) {
		setDataInicioNotificarPorEmail(dataInicio);
	}

	@Override
	public Date getDataFim() {
		return getDataFimNotificarPorEmail();
	}

	@Override
	public void setDataFim(Date dataFim) {
		setDataFimNotificarPorEmail(dataFim);
	}

	@Override
	public String getLoteDeImportacao() {
		return null;
	}

	@Override
	public void setLoteDeImportacao(String loteDeImportacao) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNivelDeDependencia() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getDescricaoExterna() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSigla() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSigla(String sigla) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDescricao() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
