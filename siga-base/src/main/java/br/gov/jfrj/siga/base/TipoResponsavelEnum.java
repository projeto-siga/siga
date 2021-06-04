package br.gov.jfrj.siga.base;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import br.gov.jfrj.siga.base.SigaMessages;

public enum TipoResponsavelEnum {
	
	MATRICULA(1, SigaMessages.getMessage("usuario.matricula")),
	LOTACAO(2, SigaMessages.getMessage("usuario.lotacao")),
	EXTERNO(3, SigaMessages.getMessage("responsavel.externo")),
	CAMPO_LIVRE(4, SigaMessages.getMessage("responsavel.campo.livre"));
	
	private Integer id;
	private String descricao;
	
	private TipoResponsavelEnum(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public Integer getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static Map<Integer, String> getLista(){
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(MATRICULA.getId(), MATRICULA.getDescricao());
		map.put(LOTACAO.getId(), LOTACAO.getDescricao());
		map.put(EXTERNO.getId(), EXTERNO.getDescricao());
		map.put(CAMPO_LIVRE.getId(), CAMPO_LIVRE.getDescricao());
		return map;
	}
	
	public static Map<Integer, String> getListaMatriculaLotacao(){
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(MATRICULA.getId(), MATRICULA.getDescricao());
		map.put(LOTACAO.getId(), LOTACAO.getDescricao());
		return map;
	}
	
	public static Map<Integer, String> getLista(List<String> chaves){
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		if(chaves!=null) {
			for(String chave : chaves) {
				TipoResponsavelEnum e = TipoResponsavelEnum.valueOf(chave);
				if(e != null) {
					map.put(e.getId(), e.getDescricao());
				}
			}
		}
		return map;
	}

}
