package models.siga;

import java.util.Comparator;

import br.gov.jfrj.siga.cp.CpConfiguracao;

public class PlayConfiguracaoComparator implements Comparator<PlayConfiguracao> {

	public int compareSelectedFields(PlayConfiguracao c1, PlayConfiguracao c2) {

		if (c1.getCpServico() == null && c2.getCpServico() != null)
			return 1;
		else if (c1.getCpServico() != null && c2.getCpServico() == null)
			return -1;

		if (c1.getCpIdentidade() == null && c2.getCpIdentidade() != null)
			return 1;
		else if (c1.getCpIdentidade() != null && c2.getCpIdentidade() == null)
			return -1;

		if (c1.getDpPessoa() == null && c2.getDpPessoa() != null)
			return 1;
		else if (c1.getDpPessoa() != null && c2.getDpPessoa() == null)
			return -1;

		if (c1.getCpGrupo() == null && c2.getCpGrupo() != null)
			return 1;
		else if (c1.getCpGrupo() != null && c2.getCpGrupo() == null)
			return -1;

		if (c1.getLotacao() == null && c2.getLotacao() != null)
			return 1;
		else if (c1.getLotacao() != null && c2.getLotacao() == null)
			return -1;

		if (c1.getFuncaoConfianca() == null && c2.getFuncaoConfianca() != null)
			return 1;
		else if (c1.getFuncaoConfianca() != null
				&& c2.getFuncaoConfianca() == null)
			return -1;

		if (c1.getOrgaoUsuario() == null && c2.getOrgaoUsuario() != null)
			return 1;
		else if (c1.getOrgaoUsuario() != null && c2.getOrgaoUsuario() == null)
			return -1;

		if (c1.getCargo() == null && c2.getCargo() != null)
			return 1;
		else if (c1.getCargo() != null && c2.getCargo() == null)
			return -1;

		if (c1.getCpGrupo() != null && c2.getCpGrupo() != null) {
			int i1 = c1.getCpGrupo().getNivel();
			int i2 = c2.getCpGrupo().getNivel();
			if (i1 > i2)
				return -1;
			if (i1 < i2)
				return 1;
		}

		return 0;
	}

	// Critérios de desempate
	//
	public int untieSelectedFields(PlayConfiguracao c1, PlayConfiguracao c2) {

		// A configuração mais restritiva deve ser priorizada.
		//
		if (c1.getCpSituacaoConfiguracao() != null
				&& c2.getCpSituacaoConfiguracao() != null) {
			long i1 = c1.getCpSituacaoConfiguracao()
					.getRestritividadeSitConfiguracao();
			long i2 = c2.getCpSituacaoConfiguracao()
					.getRestritividadeSitConfiguracao();
			if (i1 > i2)
				return -1;
			if (i1 < i2)
				return 1;
		}

		// Se não houver critério melhor, priorizar em função da id do grupo
		// para que não mude cada vez que a lista é recarregada ou que uma nova
		// configuração for inserida na lista, pois nesse caso ela ganharia uma
		// nova id e alteraria a ordenação.
		//
		if (c1.getCpGrupo() != null && c2.getCpGrupo() != null)
			return c1.getCpGrupo().getId().compareTo(c2.getCpGrupo().getId());

		return 0;
	}

	public int compare(PlayConfiguracao c1, PlayConfiguracao c2) {
		int i = compareSelectedFields(c1, c2);
		if (i != 0)
			return i;

		i = untieSelectedFields(c1, c2);
		if (i != 0)
			return i;

		return c1.getIdConfiguracao().compareTo(c2.getIdConfiguracao());
	}
}