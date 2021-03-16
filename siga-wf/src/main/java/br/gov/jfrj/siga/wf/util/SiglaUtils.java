package br.gov.jfrj.siga.wf.util;

import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.dao.CpDao;

public class SiglaUtils {

	public static class SiglaDecodificada {
		public Long id;
		public Integer ano;
		public Integer numero;
		public CpOrgaoUsuario orgaoUsuario;
	}

	public static SiglaDecodificada parse(String sigla, String tipo, CpOrgaoUsuario ouDefault) {
		SiglaDecodificada d = new SiglaDecodificada();

		sigla = sigla.trim().toUpperCase();

		Map<String, CpOrgaoUsuario> mapAcronimo = new TreeMap<String, CpOrgaoUsuario>();
		for (CpOrgaoUsuario ou : CpDao.getInstance().listarOrgaosUsuarios()) {
			mapAcronimo.put(ou.getAcronimoOrgaoUsu(), ou);
			mapAcronimo.put(ou.getSiglaOrgaoUsu(), ou);
		}
		StringBuilder acronimos = new StringBuilder();
		for (String s : mapAcronimo.keySet()) {
			if (acronimos.length() > 0)
				acronimos.append("|");
			acronimos.append(s);
		}

		final Pattern p2 = Pattern.compile("^TMP" + tipo + "-?([0-9]+)");
		final Pattern p1 = Pattern
				.compile("^(" + acronimos.toString() + ")?-?(" + tipo + ")?-?(?:(20[0-9]{2})/?)??([0-9]{1,5})$");
		final Matcher m2 = p2.matcher(sigla);
		final Matcher m1 = p1.matcher(sigla);

		if (m2.find()) {
			d.id = Long.parseLong(m2.group(1));
			return d;
		} else if (m1.find()) {
			if (m1.group(3) != null)
				d.ano = Integer.parseInt(m1.group(3));
			if (m1.group(4) != null)
			d.numero = Integer.parseInt(m1.group(4));
			if (m1.group(1) != null)
				d.orgaoUsuario = mapAcronimo.get(m1.group(1));
			if (d.orgaoUsuario == null && ouDefault != null)
				d.orgaoUsuario = ouDefault;
			if (d.ano == null)
				d.ano = (int) Calendar.getInstance().get(Calendar.YEAR);
			return d;
		} else
			throw new NumberFormatException("Formato de sigla inválido");
	}

}
