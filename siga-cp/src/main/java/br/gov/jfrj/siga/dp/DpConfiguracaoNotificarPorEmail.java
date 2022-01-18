package br.gov.jfrj.siga.dp;

import java.io.Serializable;
import java.util.Date;

import javax.transaction.Transactional;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.enm.CpAcoesDeNotificarPorEmail;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Historico;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.parser.SiglaParser;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;

public class DpConfiguracaoNotificarPorEmail extends CpConfiguracao implements Serializable, 
Selecionavel, Historico, Sincronizavel, DpConvertableEntity {

	public DpConfiguracaoNotificarPorEmail() { }
	private static final String SIGA_CEMAIL = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email";
	private static final String SIGA_CEMAIL_ALTEMAIL = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;" +CpAcoesDeNotificarPorEmail.ALTERACAO_EMAIL.getSigla()+ ":"+CpAcoesDeNotificarPorEmail.ALTERACAO_EMAIL.getDescricao();
	private static final String SIGA_CEMAIL_ALTSENHA = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;"+CpAcoesDeNotificarPorEmail.ALTERAR_MINHA_SENHA.getSigla()+":"+CpAcoesDeNotificarPorEmail.ALTERAR_MINHA_SENHA.getDescricao();
	private static final String SIGA_CEMAIL_CADUSU = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;"+CpAcoesDeNotificarPorEmail.CADASTRO_USUARIO.getSigla()+":"+CpAcoesDeNotificarPorEmail.CADASTRO_USUARIO.getDescricao();
	private static final String SIGA_CEMAIL_CONSSIG = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;"+CpAcoesDeNotificarPorEmail.CONSIGNATARIO.getSigla()+":"+CpAcoesDeNotificarPorEmail.CONSIGNATARIO.getDescricao();
	private static final String SIGA_CEMAIL_DOCMARC = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;"+CpAcoesDeNotificarPorEmail.DOC_MARCADORES.getSigla()+":"+CpAcoesDeNotificarPorEmail.DOC_MARCADORES.getDescricao();
	private static final String SIGA_CEMAIL_DOCTUN = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;"+CpAcoesDeNotificarPorEmail.DOC_TRAMIT_PARA_M_UNIDADE.getSigla()+":"+CpAcoesDeNotificarPorEmail.DOC_TRAMIT_PARA_M_UNIDADE.getDescricao();
	private static final String SIGA_CEMAIL_DOCTUSU = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;"+CpAcoesDeNotificarPorEmail.DOC_TRAMIT_PARA_MEU_USU.getSigla()+":"+CpAcoesDeNotificarPorEmail.DOC_TRAMIT_PARA_MEU_USU.getDescricao();
	private static final String SIGA_CEMAIL_ESQSENHA = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;"+CpAcoesDeNotificarPorEmail.ESQUECI_MINHA_SENHA.getSigla()+":"+CpAcoesDeNotificarPorEmail.ESQUECI_MINHA_SENHA.getDescricao();
	private static final String SIGA_CEMAIL_RESPASSI = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;"+CpAcoesDeNotificarPorEmail.RESPONS_ASSINATURA.getSigla()+":"+CpAcoesDeNotificarPorEmail.RESPONS_ASSINATURA.getDescricao();
	private static final String SIGA_CEMAIL_SUB = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;"+CpAcoesDeNotificarPorEmail.SUBSTITUICAO.getSigla()+":"+CpAcoesDeNotificarPorEmail.SUBSTITUICAO.getSigla();
	
	Integer receberEmail;
	Integer restringir;
	@Override
	public String getIdExterna() {
		return null;
	}

	@Override
	public void setIdExterna(String idExterna) { }

	@Override
	public void setIdInicial(Long idInicial) { }

	@Override
	public Date getDataInicio() {
		return null;
	}

	@Override
	public void setDataInicio(Date dataInicio) { }

	@Override
	public Date getDataFim() {
		return null;
	}

	@Override
	public void setDataFim(Date dataFim) { }

	@Override
	public String getLoteDeImportacao() {
		return null;
	}

	@Override
	public void setLoteDeImportacao(String loteDeImportacao) { }

	@Override
	public int getNivelDeDependencia() {
		return 0; 
	}

	@Override
	public String getDescricaoExterna() {
		return null;
	}

	@Override
	public String getSigla() {
		return null; 
	}

	@Override
	public void setSigla(String sigla) { }

	@Override
	public String getDescricao() {
		return null;
	}
	
	public void adicionarConfiguracao (DpPessoa pessoa, DpLotacao lotacao) {
		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
				SIGA_CEMAIL_ALTEMAIL )) {
			Cp.getInstance().getConf().podeUtilizarOuAdicionarServicoPorConfiguracao(pessoa, lotacao,
					SIGA_CEMAIL_ALTEMAIL, 0, 0 ); 
		} else {
			Cp.getInstance().getConf().podeUtilizarOuAdicionarServicoPorConfiguracao(pessoa, lotacao,
					SIGA_CEMAIL_ALTEMAIL, 0, 0 ); 
		}
		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
				SIGA_CEMAIL_ALTSENHA )) {
			Cp.getInstance().getConf().podeUtilizarOuAdicionarServicoPorConfiguracao(pessoa,lotacao,
					SIGA_CEMAIL_ALTSENHA, 1, 1 ); 
		} else {
			Cp.getInstance().getConf().podeUtilizarOuAdicionarServicoPorConfiguracao(pessoa, lotacao,
					SIGA_CEMAIL_ALTSENHA, 1, 1 ); 
		}
		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
				SIGA_CEMAIL_CADUSU )) {
			Cp.getInstance().getConf().podeUtilizarOuAdicionarServicoPorConfiguracao(pessoa, lotacao,
					SIGA_CEMAIL_CADUSU, 1, 1 );  
		} else {
			Cp.getInstance().getConf().podeUtilizarOuAdicionarServicoPorConfiguracao(pessoa, lotacao,
					SIGA_CEMAIL_CADUSU, 1, 1 );  
		}
		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
				SIGA_CEMAIL_CONSSIG )) {
			Cp.getInstance().getConf().podeUtilizarOuAdicionarServicoPorConfiguracao(pessoa, lotacao,
					SIGA_CEMAIL_CONSSIG, 1, 1 ); 
		} else {
			Cp.getInstance().getConf().podeUtilizarOuAdicionarServicoPorConfiguracao(pessoa, lotacao,
					SIGA_CEMAIL_CONSSIG, 1, 1 ); 
		}
		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
				SIGA_CEMAIL_DOCMARC )) {
			Cp.getInstance().getConf().podeUtilizarOuAdicionarServicoPorConfiguracao(pessoa, lotacao,
					SIGA_CEMAIL_DOCMARC, 0, 0 ); 
		} else {
			Cp.getInstance().getConf().podeUtilizarOuAdicionarServicoPorConfiguracao(pessoa, lotacao,
					SIGA_CEMAIL_DOCMARC, 0, 0 ); 
		}
		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
				SIGA_CEMAIL_DOCTUN )) {
			Cp.getInstance().getConf().podeUtilizarOuAdicionarServicoPorConfiguracao(pessoa, lotacao,
					SIGA_CEMAIL_DOCTUN, 0, 0 ); 
		} else {
			Cp.getInstance().getConf().podeUtilizarOuAdicionarServicoPorConfiguracao(pessoa, lotacao,
					SIGA_CEMAIL_DOCTUN, 0, 0 ); 
		}
		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
				SIGA_CEMAIL_DOCTUSU )) {
			Cp.getInstance().getConf().podeUtilizarOuAdicionarServicoPorConfiguracao(pessoa,lotacao,
					SIGA_CEMAIL_DOCTUSU, 0, 0 ); 
		} else {
			Cp.getInstance().getConf().podeUtilizarOuAdicionarServicoPorConfiguracao(pessoa, lotacao,
					SIGA_CEMAIL_DOCTUSU, 0, 0 ); 
		}
		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
				SIGA_CEMAIL_ESQSENHA )) {
			Cp.getInstance().getConf().podeUtilizarOuAdicionarServicoPorConfiguracao(pessoa, lotacao,
					SIGA_CEMAIL_ESQSENHA, 1, 1 ); 
		} else {
			Cp.getInstance().getConf().podeUtilizarOuAdicionarServicoPorConfiguracao(pessoa, lotacao,
					SIGA_CEMAIL_ESQSENHA, 1, 1 ); 
		}
		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
				SIGA_CEMAIL_RESPASSI )) {
			Cp.getInstance().getConf().podeUtilizarOuAdicionarServicoPorConfiguracao(pessoa, lotacao,
					SIGA_CEMAIL_RESPASSI, 1, 1 ); 
		} else {
			Cp.getInstance().getConf().podeUtilizarOuAdicionarServicoPorConfiguracao(pessoa, lotacao,
					SIGA_CEMAIL_RESPASSI, 1, 1 ); 
		}
		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
				SIGA_CEMAIL_SUB )) {
			Cp.getInstance().getConf().podeUtilizarOuAdicionarServicoPorConfiguracao(pessoa, lotacao,
					SIGA_CEMAIL_SUB, 1, 1 ); 
		} else {
			Cp.getInstance().getConf().podeUtilizarOuAdicionarServicoPorConfiguracao(pessoa, lotacao,
					SIGA_CEMAIL_SUB, 1, 1 );  
		}		
	}
	
}
