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
package br.gov.jfrj.siga.vraptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.SigaBaseProperties;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.ex.SigaExProperties;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

public abstract class ExSelecionavelController<T extends Selecionavel, DaoFiltroT extends DaoFiltroSelecionavel> extends
		SigaSelecionavelControllerSupport<T, DaoFiltroT> {

	private static ResourceBundle bundle;

	public ExSelecionavelController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}

	protected ExDao dao() {
		return ExDao.getInstance();
	}

	protected List<CpMarcador> getEstados() throws AplicacaoException {
		Long[] ids = {
				//Edson: favor manter ordem alfabética :-)
				CpMarcador.MARCADOR_A_DEVOLVER,
				CpMarcador.MARCADOR_A_DEVOLVER_FORA_DO_PRAZO,
				CpMarcador.MARCADOR_A_RECEBER,
				CpMarcador.MARCADOR_A_RECEBER, 
				CpMarcador.MARCADOR_A_REMETER_MANUALMENTE,
				CpMarcador.MARCADOR_A_REMETER_PARA_PUBLICACAO,
				CpMarcador.MARCADOR_AGUARDANDO,
				CpMarcador.MARCADOR_AGUARDANDO_DEVOLUCAO_FORA_DO_PRAZO,
				CpMarcador.MARCADOR_ANEXO_PENDENTE_DE_ASSINATURA,
				CpMarcador.MARCADOR_APENSADO, 
				CpMarcador.MARCADOR_ARQUIVADO_CORRENTE,
				CpMarcador.MARCADOR_CAIXA_DE_ENTRADA,
				CpMarcador.MARCADOR_CANCELADO,
				CpMarcador.MARCADOR_COMO_GESTOR,
				CpMarcador.MARCADOR_COMO_INTERESSADO,
				CpMarcador.MARCADOR_COMO_SUBSCRITOR,
				CpMarcador.MARCADOR_DESPACHO_PENDENTE_DE_ASSINATURA,
				CpMarcador.MARCADOR_DISPONIBILIZADO,
				CpMarcador.MARCADOR_DOCUMENTO_ASSINADO_COM_SENHA,
				CpMarcador.MARCADOR_EM_ANDAMENTO,
				CpMarcador.MARCADOR_EM_ELABORACAO,
				CpMarcador.MARCADOR_EM_TRANSITO,
				CpMarcador.MARCADOR_EM_TRANSITO_ELETRONICO,
				CpMarcador.MARCADOR_FINALIZAR_DOCUMENTO_COLABORATIVO,
				CpMarcador.MARCADOR_JUNTADO, 
				CpMarcador.MARCADOR_JUNTADO_A_DOCUMENTO_EXTERNO,
				CpMarcador.MARCADOR_MOVIMENTACAO_ASSINADA_COM_SENHA,
				CpMarcador.MARCADOR_MOVIMENTACAO_CONFERIDA_COM_SENHA,
				CpMarcador.MARCADOR_PENDENTE_DE_ANEXACAO,
				CpMarcador.MARCADOR_PENDENTE_DE_ASSINATURA,
				CpMarcador.MARCADOR_PENDENTE_DE_ASSINATURA,
				CpMarcador.MARCADOR_PENDENTE_DE_COLABORACAO,
				CpMarcador.MARCADOR_PUBLICACAO_SOLICITADA,
				CpMarcador.MARCADOR_PUBLICADO,
				CpMarcador.MARCADOR_REMETIDO_PARA_PUBLICACAO,
				CpMarcador.MARCADOR_REVISAR,
				CpMarcador.MARCADOR_SEM_EFEITO,
				CpMarcador.MARCADOR_SOBRESTADO, 
				CpMarcador.MARCADOR_TRANSFERIDO_A_ORGAO_EXTERNO
		};
		return dao().listarMarcadores(ids);
	}

	protected Map<Integer, String> getListaTipoResp() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1, getBundle().getString("usuario.matricula"));
		map.put(2, "Órgão Integrado");
		return map;
	}

	protected List<ExTipoDocumento> getTiposDocumento() {
		return dao().listarExTiposDocumento();
	}

	protected void assertAcesso(final String pathServico) {
		super.assertAcesso("DOC:Módulo de Documentos;" + pathServico);
	}

	private static ResourceBundle getBundle() {
        if (bundle == null) {
        	bundle = ResourceBundle.getBundle("messages_" + SigaBaseProperties.getString("siga.local"));
        }
        return bundle;
    }
}
