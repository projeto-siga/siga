package utils;

public class RiBL {
	public static String atualizarClassificacao(String classificacao, String hashTag) {		
		if(classificacao.isEmpty() && hashTag.isEmpty())
			return null;
		else if (classificacao.isEmpty() && !hashTag.isEmpty())
			return hashTag.trim();
		else if(!classificacao.isEmpty() && hashTag.isEmpty())
			return classificacao;
		else
			return classificacao.concat(", ").concat(hashTag).trim();
	}

}
