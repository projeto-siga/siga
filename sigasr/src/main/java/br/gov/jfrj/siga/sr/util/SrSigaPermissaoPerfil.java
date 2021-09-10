package br.gov.jfrj.siga.sr.util;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.vraptor.SigaObjects;

public class SrSigaPermissaoPerfil {

	public static final String ADM_ADMINISTRAR = "SR:Módulo de Serviços;ADM:Administrar";
	public static final String SALVAR_SOLICITACAO_AO_ABRIR = "SR:Módulo de Serviços;OPENSAVE:Salvar Solicitação Ao Abrir";
	public static final String EDTCONH_CRIAR_CONHECIMENTOS = "SR:Módulo de Serviços;EDTCONH:Criar Conhecimentos";
	public static final String REL_RELATORIOS = "SR:Módulo de Serviços;REL:Relatorios";
	public static final String PRIORIZAR_AO_ABRIR = "SR:Módulo de Serviços;OPENPRIOR:Priorizar ao Abrir";
	public static final String EXIBIR_MENU_RELATORIOS = "exibirMenuRelatorios";
	public static final String EXIBIR_MENU_ADMINISTRAR = "exibirMenuAdministrar";
	public static final String EXIBIR_MENU_CONHECIMENTOS = "exibirMenuConhecimentos";
	public static final String EXIBIR_CAMPO_PRIORIDADE = "exibirCampoPrioridade";
	
	public static final void setaPermissoes(SigaObjects so, Result result) {
		result.include(EXIBIR_MENU_ADMINISTRAR, permissao(ADM_ADMINISTRAR, so));
		result.include(EXIBIR_MENU_CONHECIMENTOS, permissao(EDTCONH_CRIAR_CONHECIMENTOS, so));
		result.include(EXIBIR_MENU_RELATORIOS, permissao(REL_RELATORIOS, so));
		result.include(EXIBIR_CAMPO_PRIORIDADE, permissao(PRIORIZAR_AO_ABRIR, so));
	}
	
	
	private static final boolean permissao(String codigo, SigaObjects so) {
		try {
			so.assertAcesso(codigo);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
}
