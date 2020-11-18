package br.gov.jfrj.siga.cp;

import java.util.ArrayList;
import java.util.List;

public enum CpMarcadorTipoExibicaoEnum {
		IMEDIATA(0, "Imediata"),
		DATA_LIMITE(1, "Na data limite"),
		DATA_PLANEJADA(2, "Na data planejada");

		private final Integer id;
		private final String descricao;
		
		private CpMarcadorTipoExibicaoEnum(Integer id, String descricao) {
			this.id = id;
			this.descricao = descricao;
		}

		public static List<String> getList() { 
			List<String> list = new ArrayList<String>();
			for (CpMarcadorTipoExibicaoEnum item : values()){
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
