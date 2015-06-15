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
package br.gov.jfrj.siga.gc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import br.gov.jfrj.siga.gc.model.GcInformacao;
import br.gov.jfrj.siga.gc.model.GcTag;
import br.gov.jfrj.siga.gc.service.GcService;
import br.gov.jfrj.siga.model.ContextoPersistencia;

/**
 * Classe que representa o webservice do workflow. O SIGA-DOC faz a chamada
 * remota dos métodos dessa classe para atualizar o workflow automaticamente,
 * baseando-se nas operações sobre documentos.
 * 
 * @author kpf
 * 
 */
@WebService(serviceName = "GcService", endpointInterface = "br.gov.jfrj.siga.gc.service.GcService", targetNamespace = "http://impl.service.gc.siga.jfrj.gov.br/")
public class GcServiceImpl implements GcService {
	private EntityManagerFactory factory;

	@PostConstruct
	public void init() {
		factory = Persistence.createEntityManagerFactory("default");
	}

	@PreDestroy
	public void destroy() {
		if (factory.isOpen()) {
			factory.close();
		}
	}

	@Override
	/**
	 * O padrão para tags atualizaveis segue o seguinte exemplo:
	 * @sr-item-1-123:sistemas
	 * @sr-item-2-213:siga
	 * @sr-item-3-241:siga-doc
	 * 
	 * categoria: sr-item
	 * id: 123
	 * titulos: sistemas,siga,siga-doc
	 */
	public Integer atualizarTag(String tag) throws Exception {
		Matcher matcher = GcTag.tagPattern.matcher(tag);
		if (!matcher.find()) {
			throw new Exception("Tag inválido. Deve seguir o padrão: "
					+ GcTag.tagPattern.toString());
		}
		String grupo = matcher.group(1);
		Integer indice = (matcher.group(2) != null) ? Integer.parseInt(matcher
				.group(2)) : null;
		String id = matcher.group(3);
		String titulo = matcher.group(4);

		EntityManager em = factory.createEntityManager();
		ContextoPersistencia.setEntityManager(em);

		EntityTransaction transaction = null;
		int count = 0;
		try {
			transaction = em.getTransaction();
			transaction.begin();

			TypedQuery<GcInformacao> q = em.createQuery(
					"select inf from GcInformacao as inf join inf.tags tag"
							+ " where tag.categoria like :categoria",
					GcInformacao.class);
			q.setParameter("categoria", grupo + "-1-" + id);
			List<GcInformacao> infs = q.getResultList();

			count = atualizarTagAlg(tag, infs);

			if (true)
				throw new Exception("Nao queremos gravar");

			if (transaction.isActive()) {
				transaction.commit();
			}

		} finally {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
		}

		if (em.isOpen()) {
			em.close();
		}

		return count;
	}

	protected int atualizarTagAlg(String tags, List<GcInformacao> infs)
			throws Exception {

		String atags[] = tags.split(",\\s*");

		Matcher matcher = GcTag.tagPattern.matcher(atags[atags.length - 1]);
		if (!matcher.find()) {
			throw new Exception("Tag inválido. Deve seguir o padrão: "
					+ GcTag.tagPattern.toString());
		}
		String grupo = matcher.group(1);
		Integer indice = (matcher.group(2) != null) ? Integer.parseInt(matcher
				.group(2)) : null;
		String id = matcher.group(3);
		String titulo = matcher.group(4);

		int count;
		count = infs.size();

		for (GcInformacao inf : infs) {
			GcTag tagAtualizar = null;
			for (GcTag t : inf.getTags()) {
				if (grupo.equals(t.getHierarquiaGrupo())
						&& id.equals(t.getHierarquiaId()))
					tagAtualizar = t;
			}
			if (tagAtualizar != null) {
				Integer indiceOriginal = tagAtualizar.getIndice();

				// Alteração de título
				if (!tagAtualizar.getTitulo().equals(titulo))
					tagAtualizar.setTitulo(titulo);

				// Alteração de índice
				if (!indiceOriginal.equals(indice)) {
					// Remover anteriores
					List<GcTag> remover = new ArrayList<>();
					for (GcTag t : inf.getTags()) {
						if (grupo.equals(t.getHierarquiaGrupo())
								&& indiceOriginal.compareTo(t.getIndice()) > 0)
							remover.add(t);
					}
					inf.getTags().removeAll(remover);

					// Reindexar posteriores

					// Atualizar o tag
					tagAtualizar.setIndice(indice);

					// Inserir tags anteriores informados

				}
			}
		}
		return count;
	}

}
