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
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;

public class DpConfiguracaoNotificarPorEmail extends CpConfiguracao implements Serializable, 
Selecionavel, Historico, Sincronizavel, DpConvertableEntity {

	public DpConfiguracaoNotificarPorEmail() { }
	private static final String SIGA_CEMAIL = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email";
	private static final String SIGA_CEMAIL_ALTEMAIL = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;ALTEMAIL:Alteração de email";
	private static final String SIGA_CEMAIL_ALTSENHA = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;ALTSENHA:Alterar minha senha";
	private static final String SIGA_CEMAIL_CADUSU = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;CADUSU:Cadastro de novo usuário";
	private static final String SIGA_CEMAIL_CONSSIG = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;CONSSIG:Conssignatário";
	private static final String SIGA_CEMAIL_DOCMARC = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;DOCMARC:Documentos de marcadores";
	private static final String SIGA_CEMAIL_DOCTUN = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;DOCTUN:Documento tramitado para unidade";
	private static final String SIGA_CEMAIL_DOCTUSU = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;DOCTUSU:Documento tramitado para meu usuário";
	private static final String SIGA_CEMAIL_ESQSENHA = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;ESQSENHA:Esqueci minha senha";
	private static final String SIGA_CEMAIL_RESPASSI = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;RESPASSI:Responsável pela assinatura";
	private static final String SIGA_CEMAIL_SUB = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;SUB:Substituição";
	Integer receberEmail;
	
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
	
	public void verificandoAusenciaDeAcoesParaUsuario (DpPessoa pessoa, DpLotacao lotacao) {
		Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
				SIGA_CEMAIL );
		
		Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
				SIGA_CEMAIL_ALTSENHA );
		Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
				SIGA_CEMAIL_CADUSU );
		Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
				SIGA_CEMAIL_CONSSIG );
		Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
				SIGA_CEMAIL_DOCMARC );
		Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
				SIGA_CEMAIL_DOCTUN );
		Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
				SIGA_CEMAIL_DOCTUSU );
		Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
				SIGA_CEMAIL_ESQSENHA );
		Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
				SIGA_CEMAIL_RESPASSI );
		Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
				SIGA_CEMAIL_SUB );
		
		
		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
				SIGA_CEMAIL_ALTEMAIL )) {
			
			CpConfiguracao alterEmail = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.ALTERACAO_EMAIL.getIdLong(), pessoa.getIdPessoa());
		}
//		CpConfiguracao alterEmail = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.ALTERACAO_EMAIL.getIdLong(), pessoa.getIdPessoa());
//		if (alterEmail == null) {
//			receberEmail = 0;
//			Integer restringir = 1;
//			CpServico servico = new CpServico();
//			servico.setIdServico(CpAcoesDeNotificarPorEmail.ALTERACAO_EMAIL.getIdLong());
//			adicionarServico(servico, restringir, pessoa, receberEmail);
//		}
//		CpConfiguracao alterSenha = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.ALTERAR_MINHA_SENHA.getIdLong(), pessoa.getIdPessoa());
//		if (alterSenha == null) {
//			receberEmail = 1;
//			Integer restringir = 0; 
//			CpServico servico = new CpServico();
//			servico.setIdServico(CpAcoesDeNotificarPorEmail.ALTERAR_MINHA_SENHA.getIdLong());
//			adicionarServico(servico, restringir, pessoa, receberEmail);
//		}
//		CpConfiguracao cadUsu = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.CADASTRO_USUARIO.getIdLong(), pessoa.getIdPessoa());
//		if (cadUsu == null) {
//			receberEmail = 1;
//			Integer restringir = 0;
//			CpServico servico = new CpServico();
//			servico.setIdServico(CpAcoesDeNotificarPorEmail.CADASTRO_USUARIO.getIdLong());
//			adicionarServico(servico, restringir, pessoa, receberEmail);
//		}
//		CpConfiguracao conssi = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.CONSSIGNATARIO.getIdLong(), pessoa.getIdPessoa());
//		if (conssi == null) {
//			receberEmail = 1;
//			Integer restringir = 0;
//			CpServico servico = new CpServico();
//			servico.setIdServico(CpAcoesDeNotificarPorEmail.CONSSIGNATARIO.getIdLong());
//			adicionarServico(servico, restringir, pessoa, receberEmail);
//		}
//		CpConfiguracao docMarc = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.DOC_MARCADORES.getIdLong(), pessoa.getIdPessoa());
//		if (docMarc == null) {
//			receberEmail = 0;
//			Integer restringir = 1;
//			CpServico servico = new CpServico();
//			servico.setIdServico(CpAcoesDeNotificarPorEmail.DOC_MARCADORES.getIdLong());
//			adicionarServico(servico, restringir, pessoa, receberEmail);
//		}
//		CpConfiguracao docTramiUnidade = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.DOC_TRAMIT_PARA_M_UNIDADE.getIdLong(), pessoa.getIdPessoa());
//		if (docTramiUnidade == null) {
//			receberEmail = 0;
//			Integer restringir = 1;
//			CpServico servico = new CpServico();
//			servico.setIdServico(CpAcoesDeNotificarPorEmail.DOC_TRAMIT_PARA_M_UNIDADE.getIdLong());
//			adicionarServico(servico, restringir, pessoa, receberEmail);
//		}
//		CpConfiguracao docTramiUsu = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.DOC_TRAMIT_PARA_MEU_USU.getIdLong(), pessoa.getIdPessoa());
//		if (docTramiUsu == null) {
//			receberEmail = 0;
//			Integer restringir = 1;
//			CpServico servico = new CpServico();
//			servico.setIdServico(CpAcoesDeNotificarPorEmail.DOC_TRAMIT_PARA_MEU_USU.getIdLong());
//			adicionarServico(servico, restringir, pessoa, receberEmail);
//		}
//		CpConfiguracao esqSenha = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.ESQUECI_MINHA_SENHA.getIdLong(), pessoa.getIdPessoa());
//		if (esqSenha == null) {
//			receberEmail = 1;
//			Integer restringir = 0;
//			CpServico servico = new CpServico();
//			servico.setIdServico(CpAcoesDeNotificarPorEmail.ESQUECI_MINHA_SENHA.getIdLong());
//			adicionarServico(servico, restringir, pessoa, receberEmail);
//		}
//		CpConfiguracao respAssi = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.RESPONS_ASSINATURA.getIdLong(), pessoa.getIdPessoa());
//		if (respAssi == null) {
//			receberEmail = 1;
//			Integer restringir = 0;
//			CpServico servico = new CpServico();  
//			servico.setIdServico(CpAcoesDeNotificarPorEmail.RESPONS_ASSINATURA.getIdLong());
//			adicionarServico(servico, restringir, pessoa, receberEmail);
//		}
//		CpConfiguracao sub = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.SUBSTITUICAO.getIdLong(), pessoa.getIdPessoa());
//		if (sub == null) {
//			receberEmail = 1;
//			Integer restringir = 0;
//			CpServico servico = new CpServico();
//			servico.setIdServico(CpAcoesDeNotificarPorEmail.SUBSTITUICAO.getIdLong()); 
//			adicionarServico(servico, restringir, pessoa, receberEmail);  
//		}
	}
	
	@Transactional
	public void adicionarServico (CpServico servico, Integer restringir, DpPessoa pessoa, Integer receberEmail) {
		CpConfiguracao config = new CpConfiguracao(); 
		CpDao.getInstance().iniciarTransacao();
		config.setCpServico(servico);
		config.setHisDtIni(CpDao.getInstance().consultarDataEHoraDoServidor());
		config.setDpPessoa(pessoa);
		config.setReceberEmail(receberEmail); 
		config.setRestringir(restringir);
		CpDao.getInstance().gravar(config);
		CpDao.getInstance().commitTransacao();
	}
	
}
