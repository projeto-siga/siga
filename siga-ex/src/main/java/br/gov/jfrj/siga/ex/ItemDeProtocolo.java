package br.gov.jfrj.siga.ex;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public class ItemDeProtocolo {
	
	private ExMobil mob;
	
	private String dtDDMMYY;
	
	private DpPessoa subscritor;
	
	private DpLotacao lotaSubscritor;
	
	private DpPessoa atendente;
	
	private DpLotacao lotaAtendente;
	
	public ItemDeProtocolo(){
		
	}
	
	public ItemDeProtocolo(ExMovimentacao mov){
		setMob(mov.getExMobil());
		setSubscritor(mov.getSubscritor());
		setLotaSubscritor(mov.getLotaSubscritor());
		setAtendente(mov.getResp());
		setLotaAtendente(mov.getLotaResp());
		setDtDDMMYY(mov.getDtMovDDMMYY());
		
		//Evitar lazy exception:
		getDoc().getCodigo();
	}

	public ExMobil getMob() {
		return mob;
	}

	public void setMob(ExMobil mob) {
		this.mob = mob;
	}

	public String getDtDDMMYY() {
		return dtDDMMYY;
	}

	public void setDtDDMMYY(String dtDDMMYY) {
		this.dtDDMMYY = dtDDMMYY;
	}

	public DpPessoa getSubscritor() {
		return subscritor;
	}

	public void setSubscritor(DpPessoa subscritor) {
		this.subscritor = subscritor;
	}

	public DpLotacao getLotaSubscritor() {
		return lotaSubscritor;
	}

	public void setLotaSubscritor(DpLotacao lotaSubscritor) {
		this.lotaSubscritor = lotaSubscritor;
	}

	public DpPessoa getAtendente() {
		return atendente;
	}

	public void setAtendente(DpPessoa atendente) {
		this.atendente = atendente;
	}

	public DpLotacao getLotaAtendente() {
		return lotaAtendente;
	}

	public void setLotaAtendente(DpLotacao lotaAtendente) {
		this.lotaAtendente = lotaAtendente;
	}
	
	public ExDocumento getDoc(){
		return mob.getDoc();
	}
	
}
