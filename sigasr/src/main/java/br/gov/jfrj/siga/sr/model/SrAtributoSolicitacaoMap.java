package br.gov.jfrj.siga.sr.model;

public class SrAtributoSolicitacaoMap
{
	private Long idAtributoSolicitacao;
	private Long idAtributo;
	private String valorAtributo;
	
	public SrAtributoSolicitacaoMap(){
		//this(0L,"");
	}
	
	public SrAtributoSolicitacaoMap(Long idAtributoSolicitacao, Long idAtributo,String valorAtributo){
		this.idAtributoSolicitacao = idAtributoSolicitacao;
		this.idAtributo = idAtributo;
		this.valorAtributo = valorAtributo;
	}
	
	public Long getIdAtributoSolicitacao() {
		return idAtributoSolicitacao;
	}

	public void setIdAtributoSolicitacao(Long idAtributoSolicitacao) {
		this.idAtributoSolicitacao = idAtributoSolicitacao;
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
	
    public boolean isValorPreenchido() {
    	return getValorAtributo() != null; 
    }
	
	public boolean isAtributoSolicitacaoInicial() {
		return getIdAtributoSolicitacao() == null;
	}

	public void definirAtributoSolicitacaoComoInicial() {
		setIdAtributoSolicitacao(null);
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
