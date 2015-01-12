package models.vo;

import br.gov.jfrj.siga.cp.CpComplexo;

public class CpComplexoVO extends AbstractSelecionavel {
	
	public CpComplexoVO(Long id, String nome) {
		super(id, nome);
	}

	public static CpComplexoVO createFrom(CpComplexo complexo) {
		if (complexo != null)
			return new CpComplexoVO(complexo.getIdComplexo(), complexo.getNomeComplexo());
		else
			return null;
	}
}
