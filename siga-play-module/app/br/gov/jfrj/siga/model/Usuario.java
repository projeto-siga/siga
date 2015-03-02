package br.gov.jfrj.siga.model;

import br.gov.jfrj.siga.acesso.ConheceUsuario;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public class Usuario implements ConheceUsuario{
	
	private DpPessoa cadastrante;
	private DpPessoa titular;
	private DpLotacao lotaTitular;
	private CpIdentidade identidadeCadastrante;
	
	@Override
	public CpIdentidade getIdentidadeCadastrante() {
		return identidadeCadastrante;
	}
	@Override
	public void setIdentidadeCadastrante(CpIdentidade idc) {
		identidadeCadastrante = idc;
	}

	@Override
	public DpPessoa getCadastrante() {
		return cadastrante;
	}
	@Override
	public void setCadastrante(DpPessoa pessoa) {
		cadastrante = pessoa;
	}

	@Override
	public DpPessoa getTitular() {
		return titular;
	}
	@Override
	public void setTitular(DpPessoa pesSubstituindo) {
		titular = pesSubstituindo;
	}

	@Override
	public DpLotacao getLotaTitular() {
		return lotaTitular;
	}
	@Override
	public void setLotaTitular(DpLotacao lotaSubstituindo) {
		lotaTitular = lotaSubstituindo;
	}

}
