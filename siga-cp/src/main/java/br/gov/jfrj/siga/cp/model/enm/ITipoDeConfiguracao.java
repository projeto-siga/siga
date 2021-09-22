package br.gov.jfrj.siga.cp.model.enm;

public interface ITipoDeConfiguracao {

	int getId();

	String getDescr();

	String getExplicacao();

	boolean isEditavel();
	
	CpSituacaoDeConfiguracaoEnum getSituacaoDefault();
	
	CpSituacaoDeConfiguracaoEnum[] getSituacoes();

	Enum[] getParams();

	Enum[] getObrigatorios();

	
	default public boolean ativo(String param) {
		if (obrigatorio(param))
			return true;
		if (param == null || getParams() == null)
			return false;
		String a[] = param.split(",");
		if (a.length > 1) {
			for (String s : a)
				if (ativo(s))
					return true;
		} else {
			for (Enum p : getParams()) {
				if (param.equals(p.name()))
					return true;
			}
		}
		return false;
	}

	default public boolean ativo(Enum param) {
		if (obrigatorio(param))
			return true;
		if (param == null || getParams() == null)
			return false;
		for (Enum p : getParams()) {
			if (param == p)
				return true;
		}
		return false;
	}

	default public boolean obrigatorio(String param) {
		if (param == null || getObrigatorios() == null)
			return false;
		for (Enum p : getObrigatorios()) {
			if (param.equals(p.name()))
				return true;
		}
		return false;
	}

	default public boolean obrigatorio(Enum param) {
		if (param == null || getObrigatorios() == null)
			return false;
		for (Enum p : getObrigatorios()) {
			if (param == p)
				return true;
		}
		return false;
	}

	default public String style(String param) {
		String a[] = param.split(",");
		for (String s : a)
			if (ativo(s))
				return "";
		return "display: none";
	}

}