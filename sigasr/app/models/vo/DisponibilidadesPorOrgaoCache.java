package models.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.SrDisponibilidade;
import models.SrItemConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;

public class DisponibilidadesPorOrgaoCache {

	private Map<DisponibilidadesPorOrgaoKey, SrDisponibilidade> disponibilidadesAgrupadas;

	public DisponibilidadesPorOrgaoCache(List<SrDisponibilidade> disponibilidades) {
		this.disponibilidadesAgrupadas = new HashMap<DisponibilidadesPorOrgaoCache.DisponibilidadesPorOrgaoKey, SrDisponibilidade>();
		for (SrDisponibilidade disponibilidade : disponibilidades) {
			disponibilidadesAgrupadas.put(new DisponibilidadesPorOrgaoKey(disponibilidade), disponibilidade);
		}
	}
	
	public DisponibilidadesPorOrgaoCache() {
		this.disponibilidadesAgrupadas = new HashMap<DisponibilidadesPorOrgaoCache.DisponibilidadesPorOrgaoKey, SrDisponibilidade>();
	}

	public SrDisponibilidade buscar(SrItemConfiguracao itemConfiguracao, CpOrgaoUsuario orgao) {
		return disponibilidadesAgrupadas.get(new DisponibilidadesPorOrgaoKey(itemConfiguracao, orgao));
	}
	
	public Map<String, SrDisponibilidade> buscarTodos(SrItemConfiguracao itemConfiguracao, List<CpOrgaoUsuario> orgaos) {
		Map<String, SrDisponibilidade> disponibilidades = new HashMap<String, SrDisponibilidade>();
		for (CpOrgaoUsuario orgao : orgaos) {
			SrDisponibilidade disponibilidade = buscar(itemConfiguracao, orgao);
			
			if (disponibilidade != null)
				disponibilidades.put(orgao.getSigla(), disponibilidade);
		}
		return disponibilidades;
	}

	/**
	 * Classe que representa os objetos que sao as chaves do cache.
	 */
	class DisponibilidadesPorOrgaoKey {

		private Long idItemConfiguracao;
		private Long idOrgao;
		
		public DisponibilidadesPorOrgaoKey(SrDisponibilidade disponibilidade) {
			this(disponibilidade.getItemConfiguracao(), disponibilidade.getOrgao());
		}

		public DisponibilidadesPorOrgaoKey(SrItemConfiguracao itemConfiguracao, CpOrgaoUsuario orgao) {
			this.idItemConfiguracao = itemConfiguracao.getHisIdIni();
			this.idOrgao = orgao.getIdOrgaoUsu();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime
					* result
					+ ((idItemConfiguracao == null) ? 0 : idItemConfiguracao
							.hashCode());
			result = prime * result
					+ ((idOrgao == null) ? 0 : idOrgao.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DisponibilidadesPorOrgaoKey other = (DisponibilidadesPorOrgaoKey) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (idItemConfiguracao == null) {
				if (other.idItemConfiguracao != null)
					return false;
			} else if (!idItemConfiguracao.equals(other.idItemConfiguracao))
				return false;
			if (idOrgao == null) {
				if (other.idOrgao != null)
					return false;
			} else if (!idOrgao.equals(other.idOrgao))
				return false;
			return true;
		}

		private DisponibilidadesPorOrgaoCache getOuterType() {
			return DisponibilidadesPorOrgaoCache.this;
		}
	}
}
