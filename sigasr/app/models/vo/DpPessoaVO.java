package models.vo;

import br.gov.jfrj.siga.dp.DpPessoa;

public class DpPessoaVO extends AbstractSelecionavel {
	
	public DpPessoaVO(Long id, String sigla, String descricao) {
		super(id, sigla, descricao);
	}

	public static DpPessoaVO createFrom(DpPessoa pessoa) {
		if (pessoa != null)
			return new DpPessoaVO(pessoa.getId(), pessoa.getSigla(), pessoa.getDescricao());
		else
			return null;
	}
}
