package br.gov.jfrj.siga.cp;

import java.util.ArrayList;
import java.util.List;

public enum CpMarcadorCoresEnum {
	COR_F1948A(0, "F1948A"), 
	COR_BB8FCE(1, "BB8FCE"), 
	COR_85C1E9(2, "85C1E9"), 
	COR_73C6B6(3, "73C6B6"), 
	COR_82E0AA(4, "82E0AA"), 
	COR_F8C471(5, "F8C471"), 
	COR_E59866(6, "E59866"),
	COR_D7DBDD(7, "D7DBDD"),
	COR_E74C3C(8, "E74C3C"), 
	COR_8E44AD(9, "8E44AD"), 
	COR_3498DB(10, "3498DB"), 
	COR_16A085(11, "16A085"), 
	COR_2ECC71(12, "2ECC71"), 
	COR_F39C12(13, "F39C12"), 
	COR_D35400(14, "D35400"), 
	COR_BDC3C7(15, "BDC3C7"),
	COR_CB4335(16, "CB4335"), 
	COR_6C3483(17, "6C3483"), 
	COR_2874A6(18, "2874A6"), 
	COR_117A65(19, "117A65"), 
	COR_239B56(20, "239B56"), 
	COR_B9770E(21, "B9770E"), 
	COR_A04000(22, "A04000"), 
	COR_909497(23, "909497");

	private final Integer id;
	private final String descricao;

	private CpMarcadorCoresEnum(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public static List<String> getList() {
		List<String> list = new ArrayList<String>();
		for (CpMarcadorCoresEnum item : values()) {
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
