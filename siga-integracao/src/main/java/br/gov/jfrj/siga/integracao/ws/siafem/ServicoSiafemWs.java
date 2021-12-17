package br.gov.jfrj.siga.integracao.ws.siafem;

import java.util.Calendar;
import br.gov.jfrj.siga.base.AplicacaoException;

public class ServicoSiafemWs {
	public static boolean enviarDocumento(String usuario, String senha, SiafDoc doc) {
		RecebeMSG service = new RecebeMSG();

		try {
			String ano = Calendar.getInstance().get(Calendar.YEAR) + "";

			String ret = service.getRecebeMSGSoap().mensagem(usuario, senha, ano, "", doc.getSiafDoc());

			return verificarRetorno(ret);
		} catch (Exception e) {
			throw new AplicacaoException("Erro na integração com SIAFEM: " + e.getMessage(), 0, e);
		}

	}

	private static boolean verificarRetorno(String ret) throws Exception {
		if(ret == null || ret.isEmpty())
			throw new AplicacaoException("não retornou resposta.");

		String statusOperacao = extrairValor(ret, "StatusOperacao");
		String msgErro = extrairValor(ret, "MsgErro");
		String msgRetorno = extrairValor(ret, "MsgRetorno");
		
		if(statusOperacao == null || statusOperacao.isEmpty())
			if(msgErro == null || msgErro.isEmpty())
				msgErro = "Retorno vazio";
		
		if((statusOperacao != null && statusOperacao.equals("false")) || !msgErro.isEmpty())
			throw new AplicacaoException(msgErro + msgRetorno);
		
		return true;
	}
	
	private static String extrairValor(String xml, String atributo) {
		int inicio = xml.indexOf(atributo) + atributo.length() + 1;
		int fim = xml.indexOf("</" + atributo);
		
		if(inicio != -1 && fim > inicio) 
			return xml.substring(inicio, fim);
		
		return "";
	}

}
