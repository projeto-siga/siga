package models.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import models.SrFatorMultiplicacao;
import models.SrGestorItem;
import models.SrItemConfiguracao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

/**
 * Classe que representa um V.O. de {@link SrItemConfiguracao}.
 */
public class SrItemConfiguracaoVO implements ISelecionavel {
	
	private Long id;
	private String sigla;
	private String descricao;
	public String descrItemConfiguracao;
	public String tituloItemConfiguracao;
	public String siglaItemConfiguracao;
	public Long hisIdIni;
	public String descricaoSimilaridade;
	public int numFatorMultiplicacaoGeral;
	public boolean ativo;
	public int nivel;
	public List<SrGestorItemVO> gestorSetVO;
	public List<SrFatorMultiplicacaoVO> fatorMultiplicacaoSetVO;
	
	public SrItemConfiguracaoVO(Long id, String descricao, String titulo, String sigla, Long hisIdIni, String descricaoSimilaridade, 
			int numFatorMultiplicacaoGeral, boolean isAtivo, int nivel, Set<SrGestorItem> gestorSet, Set<SrFatorMultiplicacao> fatorMultiplicacaoSet) {
		this.id = id;
		this.descrItemConfiguracao = descricao;
		this.tituloItemConfiguracao = titulo;
		this.descricao = titulo;
		this.siglaItemConfiguracao = sigla;
		this.sigla = sigla;
		this.hisIdIni = hisIdIni;
		this.descricaoSimilaridade = descricaoSimilaridade;
		this.ativo = isAtivo;
		this.numFatorMultiplicacaoGeral = numFatorMultiplicacaoGeral;
		this.nivel = nivel;
		this.gestorSetVO = new ArrayList<SrGestorItemVO>();
		this.fatorMultiplicacaoSetVO = new ArrayList<SrFatorMultiplicacaoVO>();
		
		if(gestorSet != null)
			for (SrGestorItem item : gestorSet) {
				gestorSetVO.add(item.toVO());
			}
		
		if(fatorMultiplicacaoSet != null)
			for (SrFatorMultiplicacao item : fatorMultiplicacaoSet) {
				fatorMultiplicacaoSetVO.add(item.toVO());
			}
	}
	
	/**
	 * Converte o objeto para Json.
	 */
	public String toJson() {
		return toJsonObject().toString();
	}
	
	public JsonElement toJsonObject() {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting().serializeNulls();
		Gson gson = builder.create();

		return gson.toJsonTree(this);
	}
	
	public static SrItemConfiguracaoVO createFrom(SrItemConfiguracao item) {
		if (item != null)
			return new SrItemConfiguracaoVO(item.idItemConfiguracao, item.descrItemConfiguracao, item.tituloItemConfiguracao, item.siglaItemConfiguracao, item.getHisIdIni(), item.descricaoSimilaridade, item.numFatorMultiplicacaoGeral, item.isAtivo(), item.getNivel(), item.gestorSet, item.fatorMultiplicacaoSet);
		else
			return null;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public String getSigla() {
		return this.sigla;
	}

	@Override
	public String getDescricao() {
		return this.descricao;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@Override
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}