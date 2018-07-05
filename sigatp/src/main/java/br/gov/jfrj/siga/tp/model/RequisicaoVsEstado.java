package br.gov.jfrj.siga.tp.model;

public class RequisicaoVsEstado  {

	private Long idRequisicaoTransporte;
	private EstadoRequisicao estado;

	public static EstadoRequisicao encontrarEstadoNaLista(RequisicaoVsEstado[] vetor, Long idRequisicao) {
		for (int i = 0; i < vetor.length; i++) {
			RequisicaoVsEstado re = vetor[i];
			if(re.getIdRequisicaoTransporte().equals(idRequisicao))
				return re.getEstado();
		}
		return null;
	}

    public Long getIdRequisicaoTransporte() {
        return idRequisicaoTransporte;
    }

    public void setIdRequisicaoTransporte(Long idRequisicaoTransporte) {
        this.idRequisicaoTransporte = idRequisicaoTransporte;
    }

    public EstadoRequisicao getEstado() {
        return estado;
    }

    public void setEstado(EstadoRequisicao estado) {
        this.estado = estado;
    }

}
