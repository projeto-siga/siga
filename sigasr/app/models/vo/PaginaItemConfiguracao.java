package models.vo;

import java.util.List;

import models.SrDisponibilidade;
import models.SrItemConfiguracao;
import util.JsonUtil;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class PaginaItemConfiguracao extends Pagina {

	private JsonArray orgaos;
	private String tituloOuCodigo;
	
	public PaginaItemConfiguracao() {
		this.orgaos = new JsonArray();
	}

	public JsonArray getOrgaos() {
		return orgaos;
	}

	public String toJson() {
		return JsonUtil
				.toJson(this)
				.toString();
	}

	public PaginaItemConfiguracao atualizar(List<CpOrgaoUsuario> orgaos) {
		return buscarItens(orgaos)
				.adicionarOrgaos(orgaos)
				.invalidarCache();
	}
	
	public PaginaItemConfiguracao invalidarCache() {
		DisponibilidadesPorOrgaoCacheHolder.remove();
		return this;
	}
	
	public PaginaItemConfiguracao adicionarOrgaos(List<CpOrgaoUsuario> orgaos) {
		for (CpOrgaoUsuario cpOrgaoUsuario : orgaos) {
			this.orgaos.add(JsonUtil.toJson(new OrgaoVO(cpOrgaoUsuario)));
		}
		return this;
	}
	
	public PaginaItemConfiguracao buscarItens(List<CpOrgaoUsuario> orgaos) {
		List<SrItemConfiguracao> itensConfiguracao = SrItemConfiguracao.listar(this);
		DisponibilidadesPorOrgaoCacheHolder.put(SrDisponibilidade.agruparDisponibilidades(itensConfiguracao, orgaos));
		
		for (SrItemConfiguracao itemConfiguracao : itensConfiguracao) {
			JsonObject jsonObject = (JsonObject) JsonUtil.toJson(itemConfiguracao.toVO());
			jsonObject.add("disponibilidades", itemConfiguracao.criarDisponibilidadesJSON(itemConfiguracao, orgaos));
			this.addRegistro(jsonObject);
		}
		return this;
	}

	public String getTituloOuCodigo() {
		return tituloOuCodigo;
	}

	public void setTituloOuCodigo(String tituloOuCodigo) {
		this.tituloOuCodigo = tituloOuCodigo;
	}
	
	public boolean possuiParametroConsulta() {
		return tituloOuCodigo != null && !tituloOuCodigo.isEmpty();
	}
	
	class OrgaoVO {
		private String sigla;
		private Long idOrgaoUsu;

		public OrgaoVO(CpOrgaoUsuario cpOrgaoUsuario) {
			this.sigla = cpOrgaoUsuario.getSigla();
			this.idOrgaoUsu = cpOrgaoUsuario.getIdOrgaoUsu();
		}

		public String getSigla() {
			return sigla;
		}

		public Long getIdOrgaoUsu() {
			return idOrgaoUsu;
		}
	}
}