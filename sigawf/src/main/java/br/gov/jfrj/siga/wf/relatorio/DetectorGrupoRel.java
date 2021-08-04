package br.gov.jfrj.siga.wf.relatorio;

/**
 * Classe responsável por separar em grupos as linhas especificadas
 * @author kpf
 *
 */
public class DetectorGrupoRel {

	public static enum GrupoEmProcessamento{
		INICIO, FIM, EM_PROGRESSO,NENHUM
	}
	
	private String grpInicio;
	private String grpFim;
	private GrupoEmProcessamento grupoEmProcessamento = GrupoEmProcessamento.NENHUM;
	
	public DetectorGrupoRel(String grpInicio, String grpFim) {
		this.grpInicio = grpInicio;
		this.grpFim = grpFim;
	}
	
	/**
	 * verifica se o nome da tarefa informada faz parte de um grupo
	 * @param nomeProcedimento
	 * @param nomeGrupo
	 * @return
	 */
	public boolean fazParteDoGrupo(String nomeGrupo){
		
		if (nomeGrupo.equalsIgnoreCase(grpInicio) && grupoEmProcessamento.equals(GrupoEmProcessamento.NENHUM)){
			setGrupoAtual(GrupoEmProcessamento.INICIO);
			return true;
		}
		
		if (nomeGrupo.equalsIgnoreCase(grpFim) &&( grupoEmProcessamento.equals(GrupoEmProcessamento.EM_PROGRESSO) ||  grupoEmProcessamento.equals(GrupoEmProcessamento.FIM))){
			setGrupoAtual(GrupoEmProcessamento.FIM);
			return true;
		}
		
		if (!(nomeGrupo.equalsIgnoreCase(grpInicio) || nomeGrupo.equalsIgnoreCase(grpFim)) &&
				grupoEmProcessamento.equals(GrupoEmProcessamento.INICIO)){
			return true;
		}
		
		if (grupoEmProcessamento.equals(GrupoEmProcessamento.EM_PROGRESSO)){
			return true;
		}

		
		
		
		return false;

	}

	private void setGrupoAtual(GrupoEmProcessamento grupoAtual) {
		this.grupoEmProcessamento = grupoAtual;
	}

	public GrupoEmProcessamento getGrupoEmProcessamento() {
		return grupoEmProcessamento;
	}
	
	public void reiniciarAvaliacao(){
		setGrupoAtual(GrupoEmProcessamento.NENHUM);
	}

	public void continuar() {
		setGrupoAtual(GrupoEmProcessamento.EM_PROGRESSO);
	}

	public boolean isInicio() {
		return getGrupoEmProcessamento().equals(DetectorGrupoRel.GrupoEmProcessamento.INICIO);
	}

	public boolean isFim() {
		return getGrupoEmProcessamento().equals(DetectorGrupoRel.GrupoEmProcessamento.FIM);
	}
	
}
