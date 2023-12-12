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
		//Código equivalente a select ID_MOD from ex_documento where ID_DOC = 2
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

        //Long idDoModeloDoDocumento = exDocumento.getExModelo().getIdMod();
        String nomeDoModelo = exDocumento.getExModelo().getNmMod();
        System.out.println(nomeDoModelo);
        //if (idDoModeloDoDocumento == null) {
        //    throw new AplicacaoException("Modelo do documento não encontrado para o ID: " + idDoDocumento);
        //}
        
        

        return nomeDoModelo;
    }
	/*
	private Long getIdDoDocumentoBySigla(String sigla) throws AplicacaoException {
		
		List<BigDecimal> idsDocumentos = ExDao.getInstance().consultarDocumentosPorSiglas(Collections.singletonList(siglaDoDocumento));
		//ExDocumento exDocumento = ExDao.getInstance().consultarDocumentosPorSiglas(null);
        //String idDocumento = ExDao.getInstance().consultarPorSigla(sigla);
        if (idDocumento == null) {
            throw new AplicacaoException("Documento não encontrado com a sigla: " + sigla);
        }
        return Long.valueOf(idDocumento);
    }
	*/
    public static String compactarSigla(String sigla) {
        if (sigla == null)
            return null;
        return sigla.replace("-", "").replace("/", "");
    }
    
	public Long getIdDoDocumentoBySigla(String sigla) throws AplicacaoException{
		
        //List<BigDecimal> ids = ExDao.getInstance().consultarDocumentosPorSiglas(Collections.singletonList(sigla));
        Long idDocumento = ExDao.getInstance().consultarIdDocumentoPorSigla(descompactarSigla(sigla));
        return idDocumento;
        
        /*
        if (!ids.isEmpty()) {
            // Converte o primeiro BigDecimal encontrado para long
            return ids.get(0).longValue();
        }
        */
        //throw new AplicacaoException("Documento não encontrado com a sigla: " + sigla);
        //return null; // Ou lança uma exceção, dependendo da lógica do seu negócio
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
	
    //@Override
    //public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
    	
        //TODO: Suportar sigla como entrada
        

    	/*
    	String siglaDoDocumento = "aaa";
    	
    	// Receber id do documento como entrada
    	Long idDoDocumento = Long.valueOf(req.id);
    	
    	//Consultar o documento
    	ExDocumento exDocumento = ExDao.getInstance().consultarExDocumentoPorId(idDoDocumento);
  
		if (exDocumento == null) {
            throw new AplicacaoException("Documento não encontrado com a sigla: " + siglaDoDocumento);
        }
    	
    	// Obter o ID do modelo do documento
        Long idDoModeloDoDocumento = exDocumento.getExModelo().getIdMod();
        if (idDoModeloDoDocumento == null) {
            throw new AplicacaoException("Modelo do documento não encontrado para a sigla: " + siglaDoDocumento);
        }
        
    	//prints para teste
        System.out.println(exDocumento);
    	System.out.println("######################## idDoDocumento ####################################");
    	System.out.println(idDoDocumento);
    	//ExMobil mob = ExDao.getInstance().consultarPorId(idDoDocumento);
    	System.out.println("######################## idDoModeloDoDocumento ####################################");
    	System.out.println(idDoModeloDoDocumento);
    	
    	//converter dados a serem retornados para string (montar saida)
    	// Configurar a resposta
    	//Retorna ModeloDoDocumento
    	resp.idDoc = Long.toString(idDoDocumento);
    	resp.idModelo = idDoModeloDoDocumento.toString();
    	resp.idMov = Long.toString(idDoModeloDoDocumento);
    	resp.status = "Ok";
    	
    	
        final ExMobilDaoFiltro filter = new ExMobilDaoFiltro();
        filter.setIdDoc(Long.valueOf(req.id));

        ExMobil mob = ExDao.getInstance().consultarPorSigla(filter);
        if (mob == null)
            throw new AplicacaoException(
                    "Não foi possível encontrar o documento " + req.id);
        
        ExTipoDeMovimentacao exTipoDeAssinatura = (req.assinaturaDigital != null && req.assinaturaDigital) ?
                ExTipoDeMovimentacao.ASSINATURA_DIGITAL_DOCUMENTO :
                ExTipoDeMovimentacao.ASSINATURA_COM_SENHA;

        Set<ExMovimentacao> movs = mob.getMovsNaoCanceladas(exTipoDeAssinatura);
        if(movs.isEmpty()){
            throw new AplicacaoException(
                    req.id + " não possui movimentação não cancelada de assinatura, " +
                            "então não é possível verificar assinatura");
        }

        resp.idDoc = req.id;

        try {
            for (ExMovimentacao mov : movs) {
                if (mov.assertAssinaturaValida() != null && mov.assertAssinaturaValida().contains("OK")) {
                    resp.idMov = mov.getIdMov().toString();
                    resp.status = "Ok";
                    return;
                }
            }
        } catch (final Exception e) {
            resp.status = "ERRO!";
        }
		*/
    //}

    /*
    private int obterIdDoModeloDoDocumento(String siglaDoDocumento) {
		// TODO Auto-generated method stub
    	    	
		return 0;
	}
	*/
