package br.gov.jfrj.siga.sr.model;

public class SrAtributoSolicitacaoMap
{
	private Long idAtributo;
	private String valorAtributo;
	
	public SrAtributoSolicitacaoMap(){
		//this(0L,"");
	}
	
	public SrAtributoSolicitacaoMap(Long idAtributo,String valorAtributo){
		this.idAtributo = idAtributo;
		this.valorAtributo = valorAtributo;
	}

	public Long getIdAtributo() {
		return idAtributo;
	}

	public void setIdAtributo(Long idAtributo) {
		this.idAtributo = idAtributo;
	}

	public String getValorAtributo() {
		return valorAtributo;
	}

	public void setValorAtributo(String valorAtributo) {
		this.valorAtributo = valorAtributo;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SrAtributoSolicitacaoMap){
			return this.getIdAtributo().equals(((SrAtributoSolicitacaoMap) obj).getIdAtributo()) &&
					this.getValorAtributo().equals(((SrAtributoSolicitacaoMap) obj).getValorAtributo());
		}
		return false;
	}
}
