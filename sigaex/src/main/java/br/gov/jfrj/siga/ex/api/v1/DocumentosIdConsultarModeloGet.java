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
    	
        //TODO: Suportar sigla como entrada
        //TODO: Deve retornar o id do documento pela sigla
        //TODO: Deve retornar o modelo corretp
    	resp.status = "Ok";
    	/*
    	 * Código equivalente a select ID_MOD from ex_documento where ID_DOC = 2
    	 */
    	//TODO: 2 receber a sigla do documento como entrada
    	//TODO: 3 Mudar nome do endpoint para o nome correto
    	
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
    }

    /*
    private int obterIdDoModeloDoDocumento(String siglaDoDocumento) {
		// TODO Auto-generated method stub
    	
    	System.out.println("######################## idDoDocumento ####################################");
    	System.out.println("######################## idDoDocumento ####################################");
    	System.out.println("######################## idDoDocumento ####################################");
    	System.out.println("######################## idDoDocumento ####################################");
    	System.out.println("######################## idDoDocumento ####################################");
    	System.out.println("######################## idDoDocumento ####################################");
    	System.out.println("######################## idDoDocumento ####################################");
    	System.out.println("######################## idDoDocumento ####################################");
    	System.out.println("######################## idDoDocumento ####################################");
    	System.out.println("######################## idDoDocumento ####################################");
    	System.out.println("######################## idDoDocumento ####################################");
    	System.out.println("######################## idDoDocumento ####################################");
    	
		return 0;
	}
	*/

	@Override
    public String getContext() {
        return "verificar assinatura";
    }
}
