package models.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import models.SrFatorMultiplicacao;
import models.SrGestorItem;
import models.SrItemConfiguracao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Classe que representa um V.O. de {@link SrItemConfiguracao}.
 */
public class SrItemConfiguracaoVO {
	
	public Long id;
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
		this.siglaItemConfiguracao = sigla;
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
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting().serializeNulls();
		Gson gson = builder.create();

		return gson.toJson(this);
	}
	
	public static SrItemConfiguracaoVO createFrom(SrItemConfiguracao item) {
		if (item != null)
			return new SrItemConfiguracaoVO(item.idItemConfiguracao, item.descrItemConfiguracao, item.tituloItemConfiguracao, item.siglaItemConfiguracao, item.getHisIdIni(), item.descricaoSimilaridade, item.numFatorMultiplicacaoGeral, item.isAtivo(), item.getNivel(), item.gestorSet, item.fatorMultiplicacaoSet);
		else
			return null;
	}
}