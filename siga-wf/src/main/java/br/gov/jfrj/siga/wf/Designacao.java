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
package br.gov.jfrj.siga.wf;

import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;

/**
 * Esta classe representa uma designação de responsabilidade e é utilizada na view pesquisarDesignação.jsp <br/> 
 * 
 * Designação de Tarefas (Cadastro de Responsabilidades)

1.CRIAÇÃO
2.OBJETIVO
3.UTILIZAÇÃO
4.COMPONENTES
5.BANCO DE DADOS
6.FUNCIONAMENTO BÁSICO
7.OBSERVAÇÕES

1.CRIAÇÃO
	Funcionalidade iniciada na iteração 05/10/2009 a 16/10/2009 do SIGAWF.
	
2.OBJETIVO
	Configurar quem são os responsáveis por cada tarefa do processo. 

3.UTILIZAÇÃO
	Acesse a opção "Designar Tarefas" e o processo desejado no quadro de tarefas 
	localizado na página inicial do SIGA-DOC. 

4.COMPONENTES
	4.1 pesquisarDesignacao.jsp (/sigawf/WebContent/WEB-INF/page/workflow/)
		Interface do usuário de configuração e pesquisa de desgnações
		
	4.2 WfDesignacaoAction.java (/sigawf/src/br/gov/jfrj/siga/wf/webwork/action/)
		Executa a lógica da funcionalidade de designação de resposabilidades. 
		Possui uma classe interna que define o tipo de responsável.
		
	4.3 Designacao.java (/siga-wf/src/br/gov/jfrj/siga/wf/)
		Representa uma designação de responsabilidade
		
	4.4 xwork.xml (/sigawf/src/)
		Configuração do webwork que realiza a conexão entre a interface do usuário e 
		lógica da designação de tarefas.
		
	4.5 inbox.jsp (/sigawf/WebContent/WEB-INF/page/workflow/)
		Interface de acesso à funcionalidade

5.BANCO DE DADOS
	
	5.1 CORPORATIVO.CP_CONFIGURACAO
		Registra as informações comuns entre configurações. 
		Determina se uma configuração está ativa ou não dentre outras informações.

	5.2 SIGAWF.WF_CONFIGURACAO
		Registra a configuração da designação da tarefa

6.FUNCIONAMENTO BÁSICO
       
	6.1 PESQUISA
       Quando o usuário acessa a página inicial, seleciona a opção "Designar Tarefas" 
       e o processo desejado, o inbox.jsp chama a action pesquisarDesignação configurada 
       no xwork.xml. Nesse momento o método WfDesignacaoAction.aPesquisarDesignacao() é 
       chamado e retorna a página pesquisarDesignacao.jsp. A página pesquisarDesignacao.jsp 
       lê as listas de designação (com raias e sem raias) e a página é desenhada na tela do 
       usuário. 
       
	6.2 GRAVACAO
       O usuário define as responsabilidades através dos componentes de seleção (seleção.tag) 
       e clica no botão gravar que ativa a action gravarDesignacao que, por sua vez, chama o 
       método WfDesignacaoAction.aGravarDesignacao(). Este método grava as designações e retorna 
       a página pesquisarDesignacao.jsp com as informações gravadas.

7.OBSERVAÇÕES       
       Maiores detalhes são encontrados no próprio código dos componentes responsáveis 
       pela funcionalidade de desiganção de tarefas.  
 * 
 * @author kpf
 * 
 */
public class Designacao {
	private String procedimento;
	private String raia;
	private Long Id;
	private String tarefa;
	private String nomeDoNo;
	private String expressao;
	private DpPessoa ator;
	private DpLotacao lotaAtor;
	private Integer tipoResponsavel;

	/**
	 * Retorna o tipo de responsável desigando para a tarefa.
	 * 
	 * @return retorna 0 para INDEFINIDO (Sem designação), retorna 1 para
	 *         MATRICULA (Matrícula de uma pessoa [DpPessoa]), retorna 2 para
	 *         LOTACAO (Sigla de uma lotação) retorna 3 para LOTA_SUP (Lotação
	 *         hierarquicamente superior da pessoa designada na tarefa anterior)
	 *         retorna 4 para SUP_HIER (Superior hierárquico da pessoa designada
	 *         na tarefa anterior)
	 */
	public Integer getTipoResponsavel() {
		return tipoResponsavel;
	}

	/**
	 * Informa o tipo de responsável desigando para a tarefa.
	 * 
	 * @return retorna 0 para INDEFINIDO (Sem designação), retorna 1 para
	 *         MATRICULA (Matrícula de uma pessoa [DpPessoa]), retorna 2 para
	 *         LOTACAO (Sigla de uma lotação) retorna 3 para LOTA_SUP (Lotação
	 *         hierarquicamente superior da pessoa designada na tarefa anterior)
	 *         retorna 4 para SUP_HIER (Superior hierárquico da pessoa designada
	 *         na tarefa anterior)
	 */
	public void setTipoResponsavel(Integer tipoResponsavel) {
		this.tipoResponsavel = tipoResponsavel;
	}

	/**
	 * Retorna o procedimento referente à designação.
	 * 
	 * @return
	 */
	public String getProcedimento() {
		return procedimento;
	}

	/**
	 * Informa o procedimento referente à designação.
	 * 
	 * @param procedimento
	 */
	public void setProcedimento(String procedimento) {
		this.procedimento = procedimento;
	}

	/**
	 * Retorna a raia referente à designação.
	 * 
	 * @return
	 */
	public String getRaia() {
		return raia;
	}

	/**
	 * Informa a raia referente à designação.
	 * 
	 * @param raia
	 */
	public void setRaia(String raia) {
		this.raia = raia;
	}

	/**
	 * Retorna a tarefa referente à designação.
	 * 
	 * @return
	 */
	public String getTarefa() {
		return tarefa;
	}

	/**
	 * Informa a tarefa referente à designação.
	 * 
	 * @param tarefa
	 */
	public void setTarefa(String tarefa) {
		this.tarefa = tarefa;
	}

	/**
	 * Retorna a expressão de uma designação. As expressões existentes são :
	 * "previous --> group() --> superior_group" para Lotação superior e
	 * "previous --> chief" para superior hierárquico.
	 * 
	 * @return
	 */
	public String getExpressao() {
		return expressao;
	}

	/**
	 * Informa a expressão de uma designação. As expressões existentes são :
	 * "previous --> group() --> superior_group" para Lotação superior e
	 * "previous --> chief" para superior hierárquico.
	 * @param expressao
	 */
	public void setExpressao(String expressao) {
		this.expressao = expressao;
	}

	/**
	 * Retorna o ator referente à designação.
	 * @return
	 */
	public DpPessoa getAtor() {
		return ator;
	}

	/**
	 * Informa o ator referente à designação. 
	 * @param ator
	 */
	public void setAtor(DpPessoa ator) {
		this.ator = ator;
	}

	/**
	 * Retorna a lotação referente à designação.
	 * @return
	 */
	public DpLotacao getLotaAtor() {
		return lotaAtor;
	}

	/**
	 * Informa a lotação referente à designação.
	 * @param lotaAtor
	 */
	public void setLotaAtor(DpLotacao lotaAtor) {
		this.lotaAtor = lotaAtor;
	}

	/**
	 * Retorna o ID da designação.
	 * @return
	 */
	public Long getId() {
		return Id;
	}

	/**
	 * Informa o ID da designação.
	 * @param id
	 */
	public void setId(Long id) {
		Id = id;
	}

	public void setNomeDoNo(String nomeDoNo) {
		this.nomeDoNo = nomeDoNo;
	}

	public String getNomeDoNo() {
		return nomeDoNo;
	}

}
