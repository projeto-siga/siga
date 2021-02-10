package br.gov.jfrj.siga.cp.model.enm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.gov.jfrj.siga.cp.converter.IEnumWithId;

public enum CpMarcadorCorEnum implements IEnumWithId, Serializable {
	COR_666666(1, "666666"), 
	COR_85C1E9(2, "85C1E9"), 
	COR_73C6B6(3, "73C6B6"), 
	COR_F8C471(4, "F8C471"), 
	COR_E59866(5, "E59866"),
	COR_E74C3C(6, "E74C3C"), 
	COR_3498DB(7, "3498DB"), 
	COR_16A085(8, "16A085"), 
	COR_2ECC71(9, "2ECC71"), 
	COR_F39C12(10, "F39C12"), 
	COR_D35400(11, "D35400"), 
	COR_BB8FCE(12, "BB8FCE"),
	COR_CB4335(13, "CB4335"), 
	COR_6C3483(14, "6C3483"), 
	COR_2874A6(15, "2874A6"), 
	COR_117A65(16, "117A65"), 
	COR_239B56(17, "239B56"), 
	COR_B9770E(18, "B9770E"), 
	COR_A04000(19, "A04000"), 
	COR_909497(20, "909497"),
	COR_F1948A(21, "F1948A"); 

	private final Integer id;
	private final String descricao;

	private CpMarcadorCorEnum(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public static List<String> getList() {
		List<String> list = new ArrayList<String>();
		for (CpMarcadorCorEnum item : values()) {
			list.add(item.descricao);
		}
		return list;
	}

	public Integer getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}
}	
