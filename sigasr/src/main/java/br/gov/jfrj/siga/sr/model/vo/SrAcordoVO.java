package br.gov.jfrj.siga.sr.model.vo;

import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.sr.model.SrAcordo;
import br.gov.jfrj.siga.sr.model.SrAtributoAcordo;

public class SrAcordoVO extends AbstractSelecionavel {
	
	public boolean ativo;
	public Long hisIdIni;
	public String nomeAcordo;
	public String descrAcordo;
	public List<SrAtributoAcordoVO> atributoAcordoSet;

	public SrAcordoVO(Long id, String sigla, String descricao) {
		super(id, sigla, descricao);
	}
	
	public static SrAcordoVO createFrom(SrAcordo acordo) {
		SrAcordoVO acordoVO = new SrAcordoVO(acordo.getId(), acordo.getSigla(), acordo.getDescricao());
		acordoVO.ativo = acordo.isAtivo();
		acordoVO.hisIdIni = acordo.getHisIdIni();
		acordoVO.nomeAcordo = acordo.getNomeAcordo();
		acordoVO.descrAcordo = acordo.getDescrAcordo();
		acordoVO.atributoAcordoSet = new ArrayList<SrAtributoAcordoVO>();
		
		if (acordo.atributoAcordoSet != null)
			for (SrAtributoAcordo atributoAcordo : acordo.atributoAcordoSet)
				acordoVO.atributoAcordoSet.add(atributoAcordo.toVO());
		
		return acordoVO;
	}

}
