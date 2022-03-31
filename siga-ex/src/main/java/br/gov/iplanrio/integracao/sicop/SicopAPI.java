package br.gov.iplanrio.integracao.sicop;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.jws.WebParam;
import javax.xml.ws.BindingProvider;

import org.apache.commons.lang.StringUtils;

import br.gov.iplanrio.integracao.sicop.webservice.ConsultaProcessoWS;
import br.gov.iplanrio.integracao.sicop.webservice.IncluiProcessoWS;
import br.gov.iplanrio.integracao.sicop.webservice.LogonSicopWS;
import br.gov.iplanrio.integracao.sicop.webservice.Message;
import br.gov.iplanrio.integracao.sicop.webservice.RuntimeError;
import br.gov.iplanrio.integracao.sicop.webservice.WSSicopGrava;
import br.gov.iplanrio.integracao.sicop.webservice.WSSicopGravaSoap;

 
public class SicopAPI {
    
    private WSSicopGravaSoap wsSoap;
    
    private BindingProvider provider;
    
    private Message msg;
    
    private String loginSicop;
    
    private String senhaSicop;
    
    private String urlSICOP;
    
    @Deprecated
    public SicopAPI(){
    	
    }
    
    public SicopAPI( String url,String token){
    	
    	this.urlSICOP = url;
    	
    	verificarToken(token);
    }
    
    public void verificarToken(String token){
    	
    	byte[] credDecoded = Base64.getDecoder().decode(token);
		
    	String credentials = new String(credDecoded, StandardCharsets.UTF_8);
		
    	final String[] values = credentials.split(":", 2);

		this.loginSicop = values[0];
		
		this.senhaSicop = values[1];
		
    }
    
    public void adicionar( ProcessoSicop processo) throws SicopException{
        
//        try{
//        	acessarRotina("2010" );
//        	
//        }
//        catch (Exception e) {
//            throw new SicopException(" O serviço SICOP está temporariamente indisponível.");
//        }
        
        try{
            
			if ("LogonSicop_WS".equalsIgnoreCase(msg.getMessageClassName())) {
				LogonSicopWS logonSicopWS;
				logonSicopWS = (LogonSicopWS) msg;
				String retornoProcessamento = logonSicopWS.getStatusLine().trim();

				throw new SicopException(retornoProcessamento);
			}
			
            if("IncluiProcesso_WS".equalsIgnoreCase(msg.getMessageClassName())){
            	
                msg = wsSoap.tIncluiProcessoWS( 
                		processo.getRotina(),
                		processo.getOpcao(),
                		processo.getNumprocesso(),
                		processo.getDataproc(),
                		processo.getOrgaoorigem(),
                		processo.getTipodoc(),
                		processo.getNumdoc(),
                		processo.getOrgaodoc(),
                		processo.getTipoident(),
                		processo.getNrdocidentificador(),
                		processo.getCodassunto(),
                		processo.getNomrequerente(),
                		processo.getAutoinfracao(),
                		processo.getPlacaveiculo(),
                		processo.getTppessoa(),
                		processo.getNumcpfcgc(),
                		processo.getCodlogradouro(),
                		processo.getEndereco(),
                		processo.getNum(),
                		processo.getComplemento(),
                		processo.getCep(),
                		processo.getCodcep(),
                		processo.getBairro(),
                		processo.getDdi(),
                		processo.getDdd(),
                		processo.getTelefone(),
                		processo.getRamal(),
                		processo.getEmail(),
                		processo.getInfcompl1(),
                		processo.getInfcompl2(),
                		processo.getInfcompl3(),
                		processo.getNomnomeparte(),
                		processo.getEnderecoparte(),
                		processo.getNumparte(),
                		processo.getComplementoparte(),
                		processo.getBairroparte(),
                		processo.getCepparte(),
                		processo.getCodlograparte(),
                		processo.getTelefoneparte(),
                		processo.getQlfparte(),
                        processo.getDataprocuracaoparte(),
                        processo.getTipoidentparte(),
                        processo.getNrdocidentificadorparte(),
                       
                        this.loginSicop, //processo.getMatrrecebdor(),
                        
                        processo.getImpressora(),
                        processo.getIndconfirma()
                        );

                IncluiProcessoWS incluiProcessoWS;
                RuntimeError runtimeError;
                
                if(msg instanceof IncluiProcessoWS){
                    
                    incluiProcessoWS = (IncluiProcessoWS) msg;
                    String retornoProcessamento = incluiProcessoWS.getStatusLine().trim();
                    
                    if(!isProcessoIncluidoSicop(retornoProcessamento)){
                        throw new SicopException(retornoProcessamento);                    }
                    
                }
                
                if(msg instanceof RuntimeError){
                    runtimeError = (RuntimeError) msg;
                    throw new SicopException(runtimeError.getErrorCode() + " - " + runtimeError.getErrorMessage());
                }
                
            }  
            

        }
        catch (Exception e) {
            throw new SicopException(e.getMessage(), e.getCause());
        }
        finally{
            //producao
        	//msg = wsSoap.tIncluiProcessoWS("FIM", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "","", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
        	msg = wsSoap.tIncluiProcessoWS("FIM", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "","", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "","", "", "","");

            desconectar();
        }
    }
    
    private   boolean isProcessoIncluidoSicop(String msgRetornoProcessamento){
    	
    	 if ( (msgRetornoProcessamento.toUpperCase()).indexOf("REGISTRO INCLUIDO") >=0){
    		 return true;
    		 
    	 }
    	
    	return false;
    }
    
    public ConsultaProcessoWS obterPorNumero(  String numero) throws SicopException{
        
     
        
        try{
        	acessarRotina("3010");
        	
        }
        catch (Exception e) {
            throw new SicopException(" O serviço está temporariamente indisponível.");
        }
        
        try{
            
        	if ("LogonSicop_WS".equalsIgnoreCase(msg.getMessageClassName())) {
				LogonSicopWS logonSicopWS;
				logonSicopWS = (LogonSicopWS) msg;
				String retornoProcessamento = logonSicopWS.getStatusLine().trim();

				throw new SicopException(retornoProcessamento);
			}
            
            if("ConsultaProcesso_WS".equalsIgnoreCase(msg.getMessageClassName())){
            	 
            	msg = wsSoap.tConsultaProcessoWS("",numero);
            	
            	ConsultaProcessoWS processo;
                RuntimeError runtimeError;
                
               	if(msg instanceof RuntimeError){
             	   runtimeError = (RuntimeError) msg;
             		throw new SicopException(runtimeError.getErrorCode() + " - " + runtimeError.getErrorMessage());
             	}
                
                if(msg instanceof ConsultaProcessoWS){
                	 processo = (ConsultaProcessoWS) msg;
                    	
	            	if(processo.getDESCRTPDOC().equals("              ")){
	            		return null;
	            	}
            	
	            	return processo;
                }
            }
            
            
           throw new SicopException(" O serviço está temporariamente indisponível.");
            
        }
        catch (SicopException e) {
            throw new SicopException(e.getMessage(), e.getCause());
        }
        catch (Exception e) {
            throw new SicopException(e.getMessage(), e.getCause());
        }
        finally{
            msg = wsSoap.tConsultaProcessoWS("FIM", "");

            desconectar();
        }
        
    }
    
    private void iniciarComunicacao(String urlSICOP){
        
		wsSoap = new WSSicopGrava(urlSICOP).getWSSicopGravaSoap();
		
        provider = (BindingProvider) wsSoap;
        
        provider.getRequestContext().put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);
        
    }
    
    private void conectar()  {

    	msg = wsSoap.connect();

    }
    
    public void acessarRotina(String rotina ) throws SicopException  {
    	
    	
   	  try{
           
	    	verificarCredenciais();
	    	
	        iniciarComunicacao(urlSICOP);
	        
	        conectar();
	        
	        if("LogonSicop_WS".equalsIgnoreCase(msg.getMessageClassName())){
	            msg = wsSoap.tLogonSicopWS(rotina, loginSicop, senhaSicop, "");
	        }
 
    	  }
          catch (Exception e) {
              throw new SicopException(" O serviço SICOP está temporariamente indisponível.");
          }
    	  
        
    }
    
    private void desconectar(){
        wsSoap.disconnect();
    }
    
    private void verificarCredenciais() throws SicopException{
    	
        if(StringUtils.isBlank(loginSicop) || StringUtils.isBlank(senhaSicop)){
            throw new SicopException("Nao foi possível acessar o Sicop. Credenciais do Sicop devem ser definidas para esse usuário! Operaçao nao foi concluída.");
        }
    }

	public String getLoginSicop() {
		return loginSicop;
	}

	public void setLoginSicop(String loginSicop) {
		this.loginSicop = loginSicop;
	}
    
    
}
