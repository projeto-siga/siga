package br.gov.jfrj.siga.ex.bl;

import java.util.regex.Pattern;

public class AcessoConsulta {
	Pattern pattern;

	public AcessoConsulta(long idPessoaIni, long idLotacaoIni,
			long idOrgaoUsuPessoa, long idOrgaoUsuLotacao) {
		super();

		StringBuilder sb = new StringBuilder();
		sb.append("((^|,)PUBLICO($|,))");
		if (idPessoaIni != 0L)
			sb.append("|((^|,)P" + idPessoaIni + "($|,))");
		if (idLotacaoIni != 0L)
			sb.append("|((^|,)L" + idPessoaIni + "($|,))");
		if (idOrgaoUsuPessoa != 0L)
			sb.append("|((^|,)O" + idOrgaoUsuPessoa + "($|,))");
		if (idOrgaoUsuLotacao != idOrgaoUsuPessoa && idOrgaoUsuLotacao != 0L)
			sb.append("|((^|,)O" + idOrgaoUsuLotacao + "($|,))");

		this.pattern = Pattern.compile(sb.toString());
	}

	public boolean podeAcessar(String controleDeAcesso) {
		return this.pattern.matcher(controleDeAcesso).find();
	}

}
