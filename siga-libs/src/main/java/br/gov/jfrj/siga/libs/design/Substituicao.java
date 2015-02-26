package br.gov.jfrj.siga.libs.design;

public class Substituicao {
	public final static int SUBSTITUICAO_PESSOA = 1;
	public final static int SUBSTITUICAO_LOTACAO = 2;

	Long id;
	int tipo;
	Long idTitular;
	String nomeTitular;
	Long idLotaTitular;
	String siglaLotaTitular;
	
	public Substituicao(Long id, int tipo, Long idTitular,
			String nomeTitular, Long idLotaTitular, String siglaLotaTitular) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.idTitular = idTitular;
		this.nomeTitular = nomeTitular;
		this.idLotaTitular = idLotaTitular;
		this.siglaLotaTitular = siglaLotaTitular;
	}
	
	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdTitular() {
		return idTitular;
	}

	public void setIdTitular(Long idTitular) {
		this.idTitular = idTitular;
	}

	public String getNomeTitular() {
		return nomeTitular;
	}

	public void setNomeTitular(String nomeTitular) {
		this.nomeTitular = nomeTitular;
	}

	public Long getIdLotaTitular() {
		return idLotaTitular;
	}

	public void setIdLotaTitular(Long idLotaTitular) {
		this.idLotaTitular = idLotaTitular;
	}

	public String getSiglaLotaTitular() {
		return siglaLotaTitular;
	}

	public void setSiglaLotaTitular(String siglaLotaTitular) {
		this.siglaLotaTitular = siglaLotaTitular;
	}

}
