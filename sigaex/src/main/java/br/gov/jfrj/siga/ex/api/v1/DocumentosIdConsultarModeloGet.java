package br.gov.jfrj.siga.ex.api.v1;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.context.AcessoPublico;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.ex.model.enm.ExTipoDeMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.persistencia.ExMobilDaoFiltro;
import com.crivano.swaggerservlet.SwaggerServlet;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@AcessoPublico
public class DocumentosIdConsultarModeloGet implements IExApiV1.IDocumentosIdConsultarModeloGet {
	
	@Override
    public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
		String siglaDoDocumento = req.id;
        Long idDoDocumento = getIdDoDocumentoBySigla(compactarSigla(siglaDoDocumento));
        resp.idDoc = idDoDocumento.toString();
        resp.siglaDocumento = siglaDoDocumento;
        Long IdDoModeloDoDocumento = obterModeloDoDocumento(idDoDocumento);
		resp.idModelo = IdDoModeloDoDocumento.toString();
		resp.status = "Ok";
		resp.nomeDoModelo = obterNomeDoModeloDoDocumento(idDoDocumento);
    }
	
	private Long obterModeloDoDocumento(Long idDoDocumento) throws AplicacaoException {
        ExDocumento exDocumento = ExDao.getInstance().consultarExDocumentoPorId(idDoDocumento);
        if (exDocumento == null) {
            throw new AplicacaoException("Documento não encontrado com o ID: " + idDoDocumento);
        }

        Long idDoModeloDoDocumento = exDocumento.getExModelo().getIdMod();
        if (idDoModeloDoDocumento == null) {
            throw new AplicacaoException("Modelo do documento não encontrado para o ID: " + idDoDocumento);
        }
        return idDoModeloDoDocumento;
    }
	
	private String obterNomeDoModeloDoDocumento(Long idDoDocumento) throws AplicacaoException {
        ExDocumento exDocumento = ExDao.getInstance().consultarExDocumentoPorId(idDoDocumento);
        if (exDocumento == null) {
            throw new AplicacaoException("Documento não encontrado com o ID: " + idDoDocumento);
        }
        String nomeDoModelo = exDocumento.getExModelo().getNmMod();
        return nomeDoModelo;
    }

    public static String compactarSigla(String sigla) {
        if (sigla == null)
            return null;
        return sigla.replace("-", "").replace("/", "");
    }
    
	public Long getIdDoDocumentoBySigla(String sigla) throws AplicacaoException{
        Long idDocumento = ExDao.getInstance().consultarIdDocumentoPorSigla(descompactarSigla(sigla));
        return idDocumento; 
    }
	
	public static String descompactarSigla(String siglaCompactada) {
	    if (siglaCompactada == null) {
	        return null; // Ou lançar uma exceção, dependendo da lógica do seu negócio
	    }

	    // Verifica se a sigla tem o comprimento mínimo esperado (sem sufixo)
	    if (siglaCompactada.length() < 15) {
	        return null; // Ou lançar uma exceção
	    }

	    String prefixo = siglaCompactada.substring(0, 4);
	    String tipoDocumento = siglaCompactada.substring(4, 7);
	    String ano = siglaCompactada.substring(7, 11);
	    String numeroSequencial;
	    String sufixo = "";

	    // Verifica se existe um sufixo após o número sequencial
	    if (siglaCompactada.length() > 16) {
	        numeroSequencial = siglaCompactada.substring(11, 16);
	        sufixo = "-" + siglaCompactada.substring(16);
	    } else {
	        numeroSequencial = siglaCompactada.substring(11);
	    }

	    return prefixo + "-" + tipoDocumento + "-" + ano + "/" + numeroSequencial + sufixo;
	}

	
	@Override
    public String getContext() {
        return "verificar assinatura";
    }
}	