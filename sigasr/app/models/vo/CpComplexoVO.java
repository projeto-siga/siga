package models.vo;

import br.gov.jfrj.siga.cp.CpComplexo;

public class CpComplexoVO extends SelecionavelVO {

	public CpComplexoVO(Long id, String sigla, String descricao) {
		super(id, sigla, descricao);
	}
	
	public static SelecionavelVO createFrom(CpComplexo complexo) {
		if (complexo != null)
			return SelecionavelVO.createFrom(complexo.getIdComplexo(), complexo.getNomeComplexo());
		else
			return null;
	}

}
