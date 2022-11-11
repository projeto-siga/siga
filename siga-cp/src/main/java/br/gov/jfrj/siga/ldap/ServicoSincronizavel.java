package br.gov.jfrj.siga.ldap;

import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.dp.dao.CpDao;


public class ServicoSincronizavel {
	
	String definicao;
	CpServico servico;
	
	public ServicoSincronizavel(String definicao) {
		this.definicao = definicao;
	}
	
	public String get(String param){
		String[] params = definicao.split(",");
		for (String p : params) {
			String prop = p.split(":")[0].trim();
			String valor = null;
			try{
				 valor = p.split(":")[1].trim();	
			}catch (Exception e) {
				valor = "";
			}
			
			if (prop.equalsIgnoreCase(param.trim())){
				return valor;
			}
		}
		System.out.println("x");
		return null;
	}
	
	public CpServico getCpServico(){
		if(servico == null){
			servico = CpDao.getInstance().consultarPorSiglaCpServico(get("svc"));
		}
		
		return servico;
	}

}
