package br.gov.jfrj.siga.cp.util;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;

public abstract class MatriculaUtils {

	private static class Separado {
		String sigla;
		String complemento;
	}

	public static Separado separar(String junto, boolean complementoNumerico) {
		Separado separado = new Separado();

		Map<String, CpOrgaoUsuario> mapAcronimo = new TreeMap<String, CpOrgaoUsuario>();
		for (CpOrgaoUsuario ou : CpDao.getInstance().listarOrgaosUsuarios()) {
			mapAcronimo.put(ou.getAcronimoOrgaoUsu(), ou);
			mapAcronimo.put(ou.getSiglaOrgaoUsu(), ou);
		}

		SortedSet<String> set = new TreeSet<>(new Comparator<String>() {
			@Override
			public int compare(String arg0, String arg1) {
				int i = Integer.compare(arg1.length(), arg0.length());
				if (i != 0)
					return i;
				return arg0.compareTo(arg1);
			}
		});
		set.addAll(mapAcronimo.keySet());
		String acronimos = "";
		for (String s : set) {
			if (acronimos.length() > 0)
				acronimos += "|";
			acronimos += s;
		}

		final Pattern p1 = Pattern.compile("^(?<orgao>" + acronimos
				+ "){0,1}-?(?<complemento>[0-9"
				+ (!complementoNumerico ? "A-Za-z\\-\\/\\º\\ª\\_\\ " : "") + "]{1,20})$");
		final Matcher m1 = p1.matcher(junto);

		if (m1.find()) {
			String orgao = m1.group("orgao");
			String complemento = m1.group("complemento");

			if (orgao != null && orgao.length() > 0) {
				try {
					if (mapAcronimo.containsKey(orgao)) {
						separado.sigla = orgao;
					} else {
						CpOrgaoUsuario orgaoUsuario = new CpOrgaoUsuario();
						orgaoUsuario.setSiglaOrgaoUsu(orgao);

						orgaoUsuario = CpDao.getInstance().consultarPorSigla(
								orgaoUsuario);

						separado.sigla = orgao;
					}
				} catch (final Exception ce) {

				}
			}

			if (complemento != null) {
				separado.complemento = complemento;
			}
		}
		return separado;
	}

	/**
	 * 
	 * @param matricula
	 * @return a parte numérica da matrícula
	 * @throws AplicacaoException
	 */
	public static Long getParteNumericaDaMatricula(String matricula)
			throws AplicacaoException {
		validaPreenchimentoMatricula(matricula);
		Separado separado = separar(matricula, true);
		String strParteNumerica = separado.complemento;
		if (!StringUtils.isNumeric(strParteNumerica)) {
			throw new AplicacaoException(
					"A parte numérica da matrícula é inválida. Matrícula: "
							+ matricula + ". Parte Numérica: "
							+ strParteNumerica);
		}

		return Long.parseLong(strParteNumerica);
	}

	public static String getSiglaDoOrgaoDaMatricula(String matricula)
			throws AplicacaoException {
		validaPreenchimentoMatricula(matricula);
		Separado separado = separar(matricula, true);
		String sigla = separado.sigla;
		if (StringUtils.isNumeric(sigla)) {
			throw new AplicacaoException(
					"A sigla da matrícula é inválida. Matrícula: " + matricula
							+ ". Sigla: " + sigla);
		}

		return sigla;
	}

	public static String getSiglaDoOrgaoDaLotacao(String matricula)
			throws AplicacaoException {
		Separado separado = separar(matricula, false);
		return separado.sigla;
	}

	public static String getSiglaDaLotacao(String matricula)
			throws AplicacaoException {
		Separado separado = separar(matricula, false);
		return separado.complemento;
	}

	protected static void validaPreenchimentoMatricula(String matricula)
			throws AplicacaoException {
		if (StringUtils.isBlank(matricula) || matricula.length() <= 2) {
			throw new AplicacaoException(
					"A matrícula informada é nula ou inválida. Matrícula: "
							+ matricula);
		}
	}

}
