package br.gov.jfrj.siga.tp.model;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.dp.DpPessoa;

public enum TipoDePassageiro {

	MAGISTRADOS("MAGISTRADOS"),
	DIGNITARIOS("DIGNIT\u00C1RIO"), 
	SERVIDORES("SERVIDORES"),
	TERCEIRIZADOS("TERCEIRIZADOS"),
	CARGA("CARGA"),
	NENHUM("SEM PASSAGEIROS") ;

	private static final String _NOME_PARAMETRO_MOSTRAR_TIPO_PASSAGEIRO_CARGA = "mostrarTipoPassageiroCarga";
	private String descricao;

	private TipoDePassageiro(String descricao){
		this.setDescricao(descricao);
	}

	private void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return this.descricao;
	}

	@Override
	public String toString() {
		return this.name();
	}

	public boolean equals(TipoDePassageiro outroPassageiro) {
		return this.descricao.equals(outroPassageiro.getDescricao());
	}

	public static List<TipoDePassageiro> valuesParaComboRequisicao(DpPessoa usuario, CpComplexo complexo, boolean usuarioEhAdministrador) {
		List<TipoDePassageiro> retorno = new ArrayList<TipoDePassageiro>();

		retorno.add(TipoDePassageiro.MAGISTRADOS);
		retorno.add(TipoDePassageiro.DIGNITARIOS);
		retorno.add(TipoDePassageiro.SERVIDORES);
		retorno.add(TipoDePassageiro.TERCEIRIZADOS);
		
		boolean mostrarCarga = false;
		if(!usuarioEhAdministrador) {
			mostrarCarga = Boolean.parseBoolean(Parametro.buscarValorEmVigor(_NOME_PARAMETRO_MOSTRAR_TIPO_PASSAGEIRO_CARGA, usuario, complexo));
		}
		
		if(usuarioEhAdministrador || mostrarCarga) {
			retorno.add(TipoDePassageiro.CARGA);
		}
		
		return retorno;
	}

	/**
	 * Sobrecarga criada para auxiliar o desenvolvimento
	 * na camada de visao.
	 *
	 * @param outroPassageiro String de descricao do estado que se quer verificar.
	 * @return se a string passada identifica o estado
	 */
	public boolean equals(String outroPassageiro) {
		return this.descricao.equals(outroPassageiro);
	}

	public static String[] valuesString() {
		TipoDePassageiro[] vetor = TipoDePassageiro.values();
		List<String> retorno = new ArrayList<String>();
		for (int i = 0; i < vetor.length; i++) {
			retorno.add(vetor[i].name());
		}

		return retorno.toArray(new String[retorno.size()]);
	}
	
	public TipoDePassageiro[] getValues() {
		return TipoDePassageiro.values();
	}

}
