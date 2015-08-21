package br.gov.jfrj.siga.sr.model;


public class SrCadastro extends SrEtapaSolicitacao {

	public SrCadastro(SrSolicitacao sol) {
		super(sol);
		setDescricao("Cadastro");
		setInicio(sol.getDtInicioPrimeiraEdicao());
		if (sol.isFechado())
			setFimEfetivo(sol.getDtEfetivoFechamento());
		else if (sol.isCancelado())
			setFimEfetivo(sol.getDtCancelamento());
		else if (sol.jaFoiDesignada())
			setFimEfetivo(sol.getDtInicioAtendimento());
		setLotaResponsavel(sol.getLotaCadastrante());
		setResponsavel(sol.getCadastrante());
	}
}
