package br.gov.jfrj.siga.cp;

import java.util.ArrayList;
import java.util.List;

public enum CpMarcadorCoresEnum {
	COR_F1948A, 
	COR_BB8FCE, 
	COR_85C1E9, 
	COR_73C6B6, 
	COR_82E0AA, 
	COR_F8C471, 
	COR_E59866,
	COR_D7DBDD,
	COR_E74C3C, 
	COR_8E44AD, 
	COR_3498DB, 
	COR_16A085, 
	COR_2ECC71, 
	COR_F39C12, 
	COR_D35400, 
	COR_BDC3C7,
	COR_CB4335, 
	COR_6C3483, 
	COR_2874A6, 
	COR_117A65, 
	COR_239B56, 
	COR_B9770E, 
	COR_A04000, 
	COR_909497;

	public static List<String> getList() {
		List<String> list = new ArrayList<String>();
		for (CpMarcadorCoresEnum item : values()){
		    list.add(item.name().replace("COR_", ""));
		}
	    return list;
	}
}	
