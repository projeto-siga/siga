package br.gov.iplanrio.integracao.sicop;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import br.gov.iplanrio.integracao.sicop.webservice.ConsultaProcessoWS;
import br.gov.jfrj.siga.Service;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.ex.ExSequencia.ExSequenciaEnum;
import br.gov.jfrj.siga.ex.service.ExService;
import br.gov.jfrj.siga.hibernate.ExDao;

public class SicopService {
	
	public String incluirProcesso(String sigla, String codassunto){
		
		try{			
			
			SicopAPI sicopApi = new SicopAPI( Prop.get("sicop.url") , Prop.get("sicop.token") );
			
			sicopApi.acessarRotina("2010");

			String numeroProcessoSicop =   gerarNumeroSicop();
			
			ProcessoSicop processo = new ProcessoSicop(numeroProcessoSicop,  sigla, sicopApi.getLoginSicop(), codassunto);
	
			sicopApi.adicionar(processo); 
	
			return numeroProcessoSicop.replaceFirst ("^0*", "") ;
		
	} catch (Exception e) {
			throw new AplicacaoException("Não foi possível criar processo no SICOP - serviço indisponível. ");
		}
		
	}

	private String gerarNumeroSicop() {
		
		try {
		
			Date dt =ExDao.getInstance().dt();
			Calendar c = Calendar.getInstance();
			c.setTime(dt);
			Long anoAtual =Long.valueOf(c.get(Calendar.YEAR));
			
			ExService exService = Service.getExService();
			String sequencia =  exService.obterSequencia(ExSequenciaEnum.SICOP.getValor(), anoAtual,  "1");
			Long nroSequencial = Long.parseLong(sequencia);
			
			//99 é o órgao do processorio no sicop
			return "99"+ StringUtils.leftPad(String.valueOf(nroSequencial), 6, "0")+ anoAtual;
			
		} catch (Exception e) {
			throw new AplicacaoException("Não foi possível gerar número do processo SICOP. ");
		}
		 
	}
	
	
	  public String consultarProcessoSicop(String numeroOriginal, SicopAPI sicopApi) throws SicopException {
			
			ConsultaProcessoWS processoSicop = (ConsultaProcessoWS) sicopApi.obterPorNumero( numeroOriginal);
			
			if (processoSicop != null ){
				
				return processoSicop.getNRPROCESSO();
			}
			
				throw new AplicacaoException("Processo não cadastrado!");
		}
}
