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
package br.gov.jfrj.siga.wf.util;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;
import org.jbpm.identity.assignment.ExpressionAssignmentException;
import org.jbpm.identity.assignment.ExpressionAssignmentHandler;
import org.jbpm.identity.assignment.TermTokenizer;
import org.jbpm.jpdl.el.ExpressionEvaluator;
import org.jbpm.jpdl.el.FunctionMapper;
import org.jbpm.jpdl.el.VariableResolver;
import org.jbpm.jpdl.el.impl.ExpressionEvaluatorImpl;
import org.jbpm.security.SecurityHelper;
import org.jbpm.taskmgmt.exe.Assignable;
import org.jbpm.taskmgmt.exe.SwimlaneInstance;

import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpResponsavel;
import br.gov.jfrj.siga.dp.dao.DpLotacaoDaoFiltro;
import br.gov.jfrj.siga.dp.dao.DpPessoaDaoFiltro;
import br.gov.jfrj.siga.wf.dao.WfDao;

/**
 * Implementa a linguagem de designação de atores para realizar as tarefas,
 * baseado no componente de controle de identidade presente no siga-cp.
 * 
 * <pre>
 * syntax : first-term --&gt; next-term --&gt; next-term --&gt; ... --&gt; next-term
 * 
 * first-term ::= previous |
 *                swimlane(swimlane-name) |
 *                variable(variable-name) |
 *                user(user-name) |
 *                group(group-name)
 * 
 * next-term ::= group(group-type) |
 *               member(role-name)
 * </pre>
 */

public class WfExpressionAssignmentHandler {
	private static final long serialVersionUID = 1L;

	protected String expression;
	protected ExecutionContext executionContext = null;
	protected TermTokenizer tokenizer;
	protected DpResponsavel entity = null;

	/**
	 * Designa uma pessoa ou lotação a uma tarefa ou raia (assignable). A pessoa
	 * ou lotação é o resultado de uma expressão.
	 * 
	 * @param expression
	 * @param assignable
	 * @param executionContext
	 */
	public void assign(String expression, Assignable assignable,
			ExecutionContext executionContext) {

		try {
			this.tokenizer = new TermTokenizer(expression);
			this.executionContext = executionContext;
			entity = resolveFirstTerm(tokenizer.nextTerm());
			while (tokenizer.hasMoreTerms() && (entity != null)) {
				entity = resolveNextTerm(tokenizer.nextTerm());
			}
			// if the expression did not resolve to an actor
			if (entity == null) {
				// throw an exception
				throw new RuntimeException(
						"couldn't resolve assignment expression '" + expression
								+ "'");

				// else if the expression evaluated to a user
			} else if (entity instanceof DpPessoa) {
				// do direct assignment
				assignable.setActorId(entity.getSiglaCompleta());

				// else if the expression evaluated to a group
			} else if (entity instanceof DpLotacao) {
				// put the group in the pool
				assignable.setPooledActors(new String[] { entity
						.getSiglaCompleta() });
			}

		} catch (RuntimeException e) {
			throw new ExpressionAssignmentException(
					"couldn't resolve assignment expression '" + expression
							+ "'", e);
		}
	}

	/**
	 * Resolve o primeiro termo da expressão.
	 * 
	 * @param term
	 * @return
	 */
	protected DpResponsavel resolveFirstTerm(String term) {
		DpResponsavel entity = null;

		log.debug("resolving first term '" + term + "'");

		if (term.equalsIgnoreCase("previous")) {
			String userName = SecurityHelper.getAuthenticatedActorId();
			entity = getUserByName(userName);

		} else if ((term.startsWith("swimlane(")) && (term.endsWith(")"))) {
			String swimlaneName = term.substring(9, term.length() - 1).trim();
			String userName = getSwimlaneActorId(swimlaneName);
			entity = getUserByName(userName);

		} else if ((term.startsWith("variable(")) && (term.endsWith(")"))) {
			String variableName = term.substring(9, term.length() - 1).trim();
			Object value = getVariable(variableName);

			if (value == null) {
				throw new ExpressionAssignmentException("variable '"
						+ variableName + "' is null");

			} else if (value instanceof String) {
				try {
					entity = getUserByName((String) value);
				} catch (Exception ex) {
					entity = getGroupByName((String) value);
				}
			} else if (value instanceof DpResponsavel) {
				entity = (DpResponsavel) value;
			}

		} else if ((term.startsWith("expression(")) && (term.endsWith(")"))) {
			String expressionName = term.substring(11, term.length() - 1).trim();
			Object value = getExpression(expressionName);

			if (value == null) {
				throw new ExpressionAssignmentException("expression '"
						+ expressionName + "' is null");

			} else if (value instanceof String) {
				try {
					entity = getUserByName((String) value);
				} catch (Exception ex) {
					entity = getGroupByName((String) value);
				}
			} else if (value instanceof DpResponsavel) {
				entity = (DpResponsavel) value;
			}

		} else if ((term.startsWith("user(")) && (term.endsWith(")"))) {
			String userName = term.substring(5, term.length() - 1).trim();
			entity = getUserByName(userName);

		} else if ((term.startsWith("group(")) && (term.endsWith(")"))) {
			String groupName = term.substring(6, term.length() - 1).trim();
			entity = getGroupByName(groupName);

		} else if ((term.startsWith("function(")) && (term.endsWith(")"))) {
			String functionName = term.substring(9, term.length() - 1).trim();
			entity = getUserByFunctionName(functionName);

		} else if ((term.startsWith("category(")) && (term.endsWith(")"))) {
			String categoryName = term.substring(9, term.length() - 1).trim();
			entity = getUserByCategoryName(categoryName);

		} else {
			throw new ExpressionAssignmentException(
					"couldn't interpret first term in expression '"
							+ expression + "'");
		}

		return entity;
	}

	/**
	 * Verifica se uma pessoa é chefe.
	 * 
	 * @param pes
	 * @return
	 */
	protected boolean isChief(DpPessoa pes) {
		final String[] chiefs = new String[] { "diretor", "chefe",
				"coordenador", "oficial de gabinete", "supervisor",
				"secretario geral da presidencia" };

		for (String role : chiefs) {
			if ((pes.getCargo() != null && Texto.removeAcento(
					pes.getCargo().getDescricao().trim().replace("  ", " "))
					.toLowerCase().contains(role))
					|| (pes.getFuncaoConfianca() != null && Texto.removeAcento(
							pes.getFuncaoConfianca().getDescricao().trim()
									.replace("  ", " ")).toLowerCase()
							.contains(role))) {
				entity = pes;
				return true;
			}
		}
		return false;
	}

	/**
	 * Resolve o próximo termo da expressão.
	 * 
	 * @param term
	 * @return
	 */
	protected DpResponsavel resolveNextTerm(String term) {

		log.debug("resolving next term '" + term + "'");

		if (term.equalsIgnoreCase("chief")) {
			DpPessoa user = (DpPessoa) entity;
			DpLotacao group = user.getLotacao();
			if (isChief(user)) {
				group = group.getLotacaoPai();
			}
			while (group != null) {
				try {
					for (DpPessoa pes : WfDao.getInstance().pessoasPorLotacao(
							group.getIdLotacao(), false,true)) {
						if (isChief(pes)) {
							entity = pes;
							return entity;
						}
					}
				} catch (AplicacaoException e) {
					throw new ExpressionAssignmentException(
							"couldn't interpret term '" + term
									+ "' in expression '" + expression + "'", e);
				}
				group = group.getLotacaoPai();
			}
			throw new ExpressionAssignmentException("Can't find a chief for '"
					+ user.getSigla() + "'");
		} else if (term.equalsIgnoreCase("superior_group")) {
			DpLotacao group = (DpLotacao) entity;
			entity = group.getLotacaoPai();
			return entity;
		} else if ((term.startsWith("group(")) && (term.endsWith(")"))) {
			String groupType = term.substring(6, term.length() - 1).trim();
			DpPessoa user = (DpPessoa) entity;
			entity = user.getLotacao();

			// Set groups = user.getGroupsForGroupType(groupType);
			// if (groups.size() == 0) {
			// throw new ExpressionAssignmentException(
			// "no groups for group-type '" + groupType + "'");
			// }
			// entity = (DpResponsavel) groups.iterator().next();

		} else if ((term.startsWith("member(")) && (term.endsWith(")"))) {
			String role = term.substring(7, term.length() - 1).trim();
			DpLotacao group = (DpLotacao) entity;

			try {
				for (DpPessoa pes : WfDao.getInstance().pessoasPorLotacao(
						group.getIdLotacao(), false,true)) {
					if ((pes.getCargo() != null && Texto.removeAcento(
							pes.getCargo().getDescricao().trim())
							.contains(role))
							|| (pes.getFuncaoConfianca() != null && Texto
									.removeAcento(
											pes.getFuncaoConfianca()
													.getDescricao().trim())
									.contains(role))) {
						entity = pes;
						break;
					}
				}
			} catch (AplicacaoException e) {
				throw new ExpressionAssignmentException(
						"couldn't interpret term '" + term
								+ "' in expression '" + expression + "'", e);
			}

			// entity = expressionSession.getUserByGroupAndRole(group.getName(),
			// role);

			if (entity == null) {
				throw new ExpressionAssignmentException("no users in role '"
						+ role + "'");
			}

		} else {
			throw new ExpressionAssignmentException("couldn't interpret term '"
					+ term + "' in expression '" + expression + "'");
		}

		return entity;
	}

	/**
	 * Retorna a variável do contexto de execução, caso exista.
	 * 
	 * @param variableName
	 * @return
	 */
	protected Object getVariable(String variableName) {
		Token token = executionContext.getToken();
		return executionContext.getContextInstance().getVariable(variableName,
				token);
	}

	/**
	 * Retorna a variável do contexto de execução, caso exista.
	 * 
	 * @param variableName
	 * @return
	 */
	protected Object getExpression(String expression) {
		FunctionMapper fm = new WfFunctionMapper();
		VariableResolver vr = new WfVariableResolver(executionContext);
		ExpressionEvaluator ee = new ExpressionEvaluatorImpl();
		Object result = ee.evaluate("${" + expression + "}", String.class, vr, fm);
		return result;
	}

	/**
	 * Retorna a lotação referente ao nome passado como parâmetro.
	 * 
	 * @param groupName
	 *            - nome da lotação
	 * @return
	 */
	protected DpResponsavel getGroupByName(String groupName) {
		DpLotacao group = null;

		DpLotacaoDaoFiltro fltLota = new DpLotacaoDaoFiltro();
		fltLota.setSiglaCompleta(groupName);
		group = (DpLotacao) WfDao.getInstance().consultarPorSigla(fltLota);

		if (group == null) {
			throw new ExpressionAssignmentException("group '" + groupName
					+ "' couldn't be fetched from the user db");
		}
		return group;
	}

	/**
	 * Retorna a pessoa referente ao nome passado como parâmetro.
	 * 
	 * @param userName
	 *            - Nome do usuário
	 * @return
	 */
	protected DpResponsavel getUserByName(String userName) {
		DpPessoa user = null;

		DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
		flt.setSigla(userName);
		user = (DpPessoa) WfDao.getInstance().consultarPorSigla(flt);

		if (user == null) {
			throw new ExpressionAssignmentException("user '" + userName
					+ "' couldn't be fetched from the user db");
		}
		return user;
	}

	/**
	 * Retorna a pessoa referente a função passada como parâmetro, se o usuário
	 * não for encontrado na lotação, escala para a lotação superior e assim por
	 * diante até encontrar um usuário que possua a função especificada. Utiliza
	 * uma expressão regular para fazer o match do nome da função.
	 * 
	 * @param userName
	 *            - Nome do usuário
	 * @return
	 */
	protected DpResponsavel getUserByFunctionName(String regex) {
		DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
		DpLotacao lot = null;
		lot = entity instanceof DpPessoa ? ((DpPessoa) entity).getLotacao()
				: ((DpLotacao) entity);
		while (lot != null) {
			flt.setLotacao(lot);
			flt.setNome("");
			List<DpPessoa> users = WfDao.getInstance().consultarPorFiltro(flt);
			for (DpPessoa user : users) {
				if (user.getFuncaoConfianca() != null
						&& user.getFuncaoConfianca().getNomeFuncao() != null
						&& user.getFuncaoConfianca().getNomeFuncao()
								.toLowerCase().matches(regex))
					return user;
			}
			lot = lot.getLotacaoPai();
		}

		throw new ExpressionAssignmentException(
				"user with a function name that matches the regular expression = '"
						+ regex
						+ "' couldn't be fetched from the user db following the chain of '"
						+ entity.getSiglaCompleta() + "'");
	}

	/**
	 * Retorna a pessoa referente a função passada como parâmetro, se o usuário
	 * não for encontrado na lotação, escala para a lotação superior e assim por
	 * diante até encontrar um usuário que possua a função especificada. Utiliza
	 * uma expressão regular para fazer o match do nome da função.
	 * 
	 * @param userName
	 *            - Nome do usuário
	 * @return
	 */
	protected DpResponsavel getUserByCategoryName(String regex) {
		DpPessoaDaoFiltro flt = new DpPessoaDaoFiltro();
		DpLotacao lot = null;
		lot = entity instanceof DpPessoa ? ((DpPessoa) entity).getLotacao()
				: ((DpLotacao) entity);
		while (lot != null) {
			flt.setLotacao(lot);
			flt.setNome("");
			List<DpPessoa> users = WfDao.getInstance().consultarPorFiltro(flt);
			for (DpPessoa user : users) {
				if (user.getCargo() != null
						&& user.getCargo().getNomeCargo() != null
						&& user.getCargo().getNomeCargo().toLowerCase()
								.matches(regex))
					return user;
			}
			lot = lot.getLotacaoPai();
		}

		throw new ExpressionAssignmentException(
				"user with a category name that matches the regular expression = '"
						+ regex
						+ "' couldn't be fetched from the user db following the chain of '"
						+ entity.getSiglaCompleta() + "'");
	}

	/**
	 * Retorna o id do ator designado para a raia (swimlane).
	 * 
	 * @param swimlaneName
	 * @return
	 */
	protected String getSwimlaneActorId(String swimlaneName) {
		SwimlaneInstance swimlaneInstance = executionContext
				.getTaskMgmtInstance().getSwimlaneInstance(swimlaneName);
		if (swimlaneInstance == null) {
			throw new ExpressionAssignmentException("no swimlane instance '"
					+ swimlaneName + "'");
		}
		return swimlaneInstance.getActorId();
	}

	private static final Log log = LogFactory
			.getLog(ExpressionAssignmentHandler.class);

	// Just for the unit tests
	public void setEntity(DpResponsavel entity) {
		this.entity = entity;
	}
}
