package br.gov.jfrj.siga.cp.bl;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.CpPersonalizacao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpSubstituicao;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class CpSubstituicaoBL {
	private static CpDao dao() {
		return CpDao.getInstance();
	}

	public static void finalizarSubstituicao(DpPessoa cadastrante) {
		CpPersonalizacao per = dao().consultarPersonalizacao(cadastrante);

		if (per == null) {
			per = new CpPersonalizacao();
			per.setPessoa(cadastrante);
		}

		per.setPesSubstituindo(null);
		per.setLotaSubstituindo(null);
		dao().gravar(per);
	}

	public static void substituir(DpPessoa cadastrante, Long id) {
		finalizarSubstituicao(cadastrante);

		if (id == null)
			throw new AplicacaoException("Dados não informados");

		DpSubstituicao dpSub = dao().consultar(id, DpSubstituicao.class, false);

		if ((dpSub.getSubstituto() == null && !dpSub.getLotaSubstituto().equivale(cadastrante.getLotacao())
				|| (dpSub.getSubstituto() != null && !dpSub.getSubstituto().equivale(cadastrante))))
			throw new AplicacaoException("Substituição não permitida");

		DpLotacao lotaTitular = dpSub.getLotaTitular();
		DpPessoa titular = dpSub.getTitular();

		CpPersonalizacao per = dao().consultarPersonalizacao(cadastrante);
		if (per == null) {
			per = new CpPersonalizacao();
		}
		per.setPessoa(cadastrante);
		per.setPesSubstituindo(titular != cadastrante ? titular : null);
		per.setLotaSubstituindo(lotaTitular != cadastrante.getLotacao() ? lotaTitular : null);

		dao().gravar(per);
	}

}
