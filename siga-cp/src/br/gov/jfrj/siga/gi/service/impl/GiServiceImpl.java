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
package br.gov.jfrj.siga.gi.service.impl;

import java.security.NoSuchAlgorithmException;

import javax.jws.WebService;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.GeraMessageDigest;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.dp.DpCargo;
import br.gov.jfrj.siga.dp.DpFuncaoConfianca;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import br.gov.jfrj.siga.gi.service.GiService;

/**
 * Esta classe implementa os métodos de gestão de identidade
 * O acesso à esta classe é realizado via web-services, com interfaces
 * definidas no módulo siga-ws, conforme o padrão adotados para o SIGA.
 * 
 * @author tah
 * 
 */
@WebService(endpointInterface = "br.gov.jfrj.siga.gi.service.GiService")
public class GiServiceImpl implements GiService {

	public String login(String matricula, String senha) {
		String resultado = "";
		try {
			final String hashAtual = GeraMessageDigest.executaHash(senha.getBytes(), "MD5");
			CpDao dao = CpDao.getInstance();
			
			DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
			flt.setSigla(matricula);
			
			DpPessoa p = (DpPessoa)dao.consultarPorSigla(flt);
			CpIdentidade id = null;
			id = dao.consultaIdentidadeCadastrante(matricula, true);
			if (id != null && id.getDscSenhaIdentidade().equals(hashAtual)){
				 resultado =  parseLoginResult(id);
			}
			
		} catch (AplicacaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultado;
	}

	private String parseLoginResult(CpIdentidade id) {
		JSONObject pessoa = new JSONObject();
		JSONObject lotacao = new JSONObject();
		JSONObject cargo = new JSONObject();
		JSONObject funcao = new JSONObject();
		
		try {
			DpPessoa p = id.getPessoaAtual();
			pessoa.put("idPessoa", p.getId());
			pessoa.put("matriculaPessoa", p.getMatricula());
			pessoa.put("siglaPessoa", p.getSiglaCompleta());
			pessoa.put("nomePessoa", p.getNomePessoa());
			pessoa.put("emailPessoa", p.getEmailPessoa());
			
			DpLotacao l = p.getLotacao();
			lotacao.put("idLotacao", l.getId());
			lotacao.put("nomeLotacao", l.getNomeLotacao());
			lotacao.put("siglaLotacao", l.getSigla());
			lotacao.put("idLotacaoPai", l.getIdLotacaoPai());
			lotacao.put("idTpLotacao", l.getCpTipoLotacao()!=null?l.getCpTipoLotacao().getIdTpLotacao():"null");
			lotacao.put("siglaTpLotacao", l.getCpTipoLotacao()!=null?l.getCpTipoLotacao().getSiglaTpLotacao():"null");
			
			DpCargo c = p.getCargo();
			cargo.put("idCargo", c.getId());
			cargo.put("nomeCargo", c.getNomeCargo());
			cargo.put("siglaCargo", c.getSigla());
			
			DpFuncaoConfianca f = p.getFuncaoConfianca();
			if (f !=null){
					funcao.put("idFuncaoConfianca", f.getId());
					funcao.put("nomeFuncaoConfianca", f.getNomeFuncao());
					funcao.put("siglaFuncaoConfianca", f.getSigla());
					funcao.put("idPaiFuncaoConfianca", f.getIdFuncaoPai());
			}
			
			
			pessoa.put("lotacao", lotacao);
			pessoa.put("cargo", cargo);
			pessoa.put("funcaoConfianca", funcao);
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return pessoa.toString();
	}



	
}
