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

import java.util.Set;

@AcessoPublico
public class DocumentosIdConsultarModeloGet implements IExApiV1.IDocumentosIdConsultarModeloGet {
	
	@Override
    public void run(Request req, Response resp, ExApiV1Context ctx) throws Exception {
        Long idDoDocumento = Long.valueOf(req.id);
        //TODO: Deverá ser possível passar a sigla do documento e retornar o modelo
        resp.idModelo = obterModeloDoDocumento(idDoDocumento);
        resp.status = "Ok";
    }
	
	private String obterModeloDoDocumento(Long idDoDocumento) throws AplicacaoException {
		//Código equivalente a select ID_MOD from ex_documento where ID_DOC = 2
        ExDocumento exDocumento = ExDao.getInstance().consultarExDocumentoPorId(idDoDocumento);
        if (exDocumento == null) {
            throw new AplicacaoException("Documento não encontrado com o ID: " + idDoDocumento);
        }

        Long idDoModeloDoDocumento = exDocumento.getExModelo().getIdMod();
        if (idDoModeloDoDocumento == null) {
            throw new AplicacaoException("Modelo do documento não encontrado para o ID: " + idDoDocumento);
        }

        return idDoModeloDoDocumento.toString();
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
