/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/*
 * Criado em  13/09/2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.gov.jfrj.siga.vraptor;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.cp.CpArquivoTipoArmazenamentoEnum;
import br.gov.jfrj.siga.cp.arquivo.ArmazenamentoHCP;
import br.gov.jfrj.siga.dp.dao.CpDao;

@Controller
public class ArmazenamentoController extends SigaController {

	private static final String ACESSO_ARMAZ = "FE: Ferramentas;ARMAZ: Armazenamento de Arquivos;";
	private static final String ACESSO_ESTATISTICA = "ARMAZ_ESTAT:Estatística de Armazenamento;";
	
	private Long totalCapacityBytes = 0L;
	private Long usedCapacityBytes  = 0L;
	private Integer softQuotaPercent  = 0;
	private Long objectCount  = 0L;
	private String namespaceName = "Não identificado";

	/**
	 * @deprecated CDI eyes only
	 */
	public ArmazenamentoController() {
		super();
	}

	@Inject
	public ArmazenamentoController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so,
			EntityManager em) {
		super(request, result, dao, so, em);
	}

	@Get({ "/app/armazenamento/estatistica" })
	public void estatistica() throws Exception {
		assertAcesso(ACESSO_ESTATISTICA);
		final Integer PERCENT_WARN = 70;
		final Integer PERCENT_DANGER = 95;
		JSONObject jsonEstatistica = new JSONObject();
		
		if (CpArquivoTipoArmazenamentoEnum.HCP.equals(CpArquivoTipoArmazenamentoEnum.valueOf(Prop.get("/siga.armazenamento.arquivo.tipo")))) {
			ArmazenamentoHCP a = ArmazenamentoHCP.getInstance();
			jsonEstatistica = a.estatistica().getJSONObject("statistics");
			convertJsonToResult(jsonEstatistica);
		}
		
		DecimalFormat formato = new DecimalFormat ("#,##0.00", new DecimalFormatSymbols (new Locale ("en", "US"))); 
		double percentualUsed = ((double) usedCapacityBytes/totalCapacityBytes) * 100;  
		
		result.include("totalCapacityBytes", totalCapacityBytes);
		result.include("usedCapacityBytes", usedCapacityBytes);
		result.include("softQuotaPercent", softQuotaPercent);
		result.include("objectCount", objectCount);
		result.include("bgClass", (percentualUsed < softQuotaPercent ? percentualUsed < PERCENT_WARN ? "bg-success" : "bg-warning" : "bg-danger"));
		result.include("percentualUsed", formato.format(percentualUsed));
		result.include("namespaceName", namespaceName);
		
		if (percentualUsed > PERCENT_DANGER) {
			result.include("msgCabecClass", "alert-danger");
			result.include("mensagemCabec", "O espaço para armazenamento de arquivos está se esgotando.");
		}
		
	}
	
	
	private void convertJsonToResult(JSONObject jsonEstatistica) throws JSONException {
		totalCapacityBytes = jsonEstatistica.getLong("totalCapacityBytes");
	    usedCapacityBytes = jsonEstatistica.getLong("usedCapacityBytes");
		softQuotaPercent = jsonEstatistica.getInt("softQuotaPercent");
		objectCount = jsonEstatistica.getLong("objectCount");
		namespaceName = jsonEstatistica.getString("namespaceName");
	}


	protected void assertAcesso(String pathServico) throws AplicacaoException {
		super.assertAcesso(ACESSO_ARMAZ + pathServico);
	}
}