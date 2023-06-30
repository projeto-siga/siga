package br.gov.jfrj.siga.cp.util;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

public class XjusUtils {
	public static final String ACESSO_PUBLICO = "PUBLICO";

	public static String formatId(Long id) {
		return String.format("%012d", id);
	}
	
	public static String getAcessosString(Set<Object> acessos, Date dt, Object incluirAcesso, Object excluirAcesso) {
		if (acessos.contains(ACESSO_PUBLICO))
			return ACESSO_PUBLICO;

		acessos.remove(null);

		// Otimizar a lista removendo todas as pessoas e lotações de um órgão,
		// quando este órgão todo pode acessar o documento
		Set<Object> toRemove = new HashSet<Object>();
		for (Object o : acessos) {
			if (o instanceof CpOrgaoUsuario) {
				CpOrgaoUsuario ou = (CpOrgaoUsuario) o;
				for (Object oo : acessos) {
					if (oo instanceof DpLotacao) {
						if (((DpLotacao) oo).getOrgaoUsuario().getId().equals(ou.getId()))
							toRemove.add(oo);
					} else if (oo instanceof DpPessoa) {
						if (((DpPessoa) oo).getOrgaoUsuario().getId().equals(ou.getId()))
							toRemove.add(oo);
					}
				}
			}
		}
		if (incluirAcesso != null)
			acessos.add(incluirAcesso);
		if (excluirAcesso != null)
			toRemove.add(excluirAcesso);

		acessos.removeAll(toRemove);

		SortedSet<String> result = new TreeSet<String>();
		for (Object o : acessos) {
			if (o instanceof String)
				result.add((String) o);
			else if (o instanceof CpOrgaoUsuario)
				result.add("O" + ((CpOrgaoUsuario) o).getId());
			else if (o instanceof DpLotacao)
				result.add("L" + ((DpLotacao) o).getIdInicial());
			else if (o instanceof DpPessoa)
				result.add("P" + ((DpPessoa) o).getIdInicial());
		}

		StringBuilder sb = new StringBuilder();

		for (String each : result) {
			if (sb.length() > 0)
				sb.append(",");
			sb.append(each);
		}

		return sb.toString();
	}

}
