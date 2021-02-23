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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.SigaMessages;
import br.gov.jfrj.siga.cp.model.enm.CpMarcadorEnum;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExTipoDocumento;
import br.gov.jfrj.siga.hibernate.ExDao;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.model.dao.DaoFiltroSelecionavel;

public abstract class ExSelecionavelController<T extends Selecionavel, DaoFiltroT extends DaoFiltroSelecionavel> extends
		SigaSelecionavelControllerSupport<T, DaoFiltroT> {

	private static ResourceBundle bundle;

	/**
	 * @deprecated CDI eyes only
	 */
	public ExSelecionavelController() {
		super();
	}

	public ExSelecionavelController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}

	protected ExDao dao() {
		return ExDao.getInstance();
	}

	protected List<CpMarcador> getEstados(Long ultMovIdEstadoDoc) throws AplicacaoException {
		Long[] ids = {
				//Edson: favor manter ordem alfabética :-)
				CpMarcadorEnum.A_DEVOLVER.getId(),
				CpMarcadorEnum.A_DEVOLVER_FORA_DO_PRAZO.getId(),
				CpMarcadorEnum.A_RECEBER.getId(),
				CpMarcadorEnum.A_RECEBER.getId(), 
				CpMarcadorEnum.A_REMETER_MANUALMENTE.getId(),
				CpMarcadorEnum.A_REMETER_PARA_PUBLICACAO.getId(),
				CpMarcadorEnum.AGUARDANDO.getId(),
				CpMarcadorEnum.AGUARDANDO_DEVOLUCAO_FORA_DO_PRAZO.getId(),
				CpMarcadorEnum.ANEXO_PENDENTE_DE_ASSINATURA.getId(),
				CpMarcadorEnum.APENSADO.getId(), 
				CpMarcadorEnum.ARQUIVADO_CORRENTE.getId(),
				CpMarcadorEnum.CAIXA_DE_ENTRADA.getId(),
				CpMarcadorEnum.CANCELADO.getId(),
				CpMarcadorEnum.COMO_GESTOR.getId(),
				CpMarcadorEnum.COMO_INTERESSADO.getId(),
				CpMarcadorEnum.COMO_SUBSCRITOR.getId(),
				CpMarcadorEnum.DESPACHO_PENDENTE_DE_ASSINATURA.getId(),
				CpMarcadorEnum.DISPONIBILIZADO.getId(),
				CpMarcadorEnum.DOCUMENTO_ASSINADO_COM_SENHA.getId(),
				CpMarcadorEnum.EM_ANDAMENTO.getId(),
				CpMarcadorEnum.EM_ELABORACAO.getId(),
				CpMarcadorEnum.EM_TRANSITO.getId(),
				CpMarcadorEnum.EM_TRANSITO_ELETRONICO.getId(),
				CpMarcadorEnum.FINALIZAR_DOCUMENTO_COLABORATIVO.getId(),
				CpMarcadorEnum.JUNTADO.getId(), 
				CpMarcadorEnum.JUNTADO_A_DOCUMENTO_EXTERNO.getId(),
				CpMarcadorEnum.MOVIMENTACAO_ASSINADA_COM_SENHA.getId(),
				CpMarcadorEnum.MOVIMENTACAO_CONFERIDA_COM_SENHA.getId(),
				CpMarcadorEnum.PENDENTE_DE_ANEXACAO.getId(),
				CpMarcadorEnum.PENDENTE_DE_ASSINATURA.getId(),
				CpMarcadorEnum.PENDENTE_DE_ASSINATURA.getId(),
				CpMarcadorEnum.PENDENTE_DE_COLABORACAO.getId(),
				CpMarcadorEnum.PORTAL_TRANSPARENCIA.getId(),
				CpMarcadorEnum.PUBLICACAO_SOLICITADA.getId(),
				CpMarcadorEnum.PUBLICADO.getId(),
				CpMarcadorEnum.REMETIDO_PARA_PUBLICACAO.getId(),
				CpMarcadorEnum.REVISAR.getId(),
				CpMarcadorEnum.SEM_EFEITO.getId(),
				CpMarcadorEnum.SOBRESTADO.getId(), 
				CpMarcadorEnum.TRANSFERIDO_A_ORGAO_EXTERNO.getId()
		};
		if (ultMovIdEstadoDoc != null) {
			ArrayList<Long> a = new ArrayList<>();
			a.addAll(Arrays.asList(ids));
			if (!a.contains(ultMovIdEstadoDoc)) {
				a.add(ultMovIdEstadoDoc);
				ids = a.toArray(new Long[a.size()]);
			}
		}
		List<CpMarcador> l = dao().listarMarcadores(ids);
		l.addAll(dao().listarCpMarcadoresPorLotacaoEGeral(getLotaTitular(), true));
		l.sort(CpMarcador.GRUPO_COMPARATOR);
		return l;
	}

	protected Map<Integer, String> getListaTipoResp() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1, SigaMessages.getMessage("usuario.matricula"));
		map.put(2, "Lotação");
		return map;
	}

	protected List<ExTipoDocumento> getTiposDocumento() {
		return dao().listarExTiposDocumento();
	}

	protected void assertAcesso(final String pathServico) {
		super.assertAcesso("DOC:Módulo de Documentos;" + pathServico);
	}

}
