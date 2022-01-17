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
	private static final String SIGA_CEMAIL_CONSSIG = "Siga:Sistema Integrado de Gestão Administrativa;CEMAIL:Módulo de notificação por email;"+CpAcoesDeNotificarPorEmail.CONSSIGNATARIO.getSigla()+":"+CpAcoesDeNotificarPorEmail.CONSSIGNATARIO.getDescricao();
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
	
	public CpConfiguracao adicionarConfiguracao (DpPessoa pessoa, DpLotacao lotacao, String servicoSigla) {
		CpServico servico = new CpServico();
		CpConfiguracao config = new CpConfiguracao();
		String sigla = "";
		Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
				SIGA_CEMAIL );
		sigla = CpAcoesDeNotificarPorEmail.SIGA_CEMAIL.getSigla() + servicoSigla;
		servico = CpDao.getInstance().consultarServicoPorSigla(sigla);  
		config = CpDao.getInstance().consultarExistenciaServicoEmConfiguracao(servico.getIdServico(), pessoa.getIdPessoa());
		if (config == null) { 
			receberEmail = 1;  
			restringir = 1;
			//config = Cp.getInstance().getConf().adicionaNovaConfiguracao(servico, restringir, receberEmail, pessoa);
		}
		return config; 
		
//		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
//				SIGA_CEMAIL_ALTEMAIL )) {
//			sigla = CpAcoesDeNotificarPorEmail.SIGA_CEMAIL.getSigla() + CpAcoesDeNotificarPorEmail.ALTERACAO_EMAIL.getSigla();
//			servico = CpDao.getInstance().consultarServicoPorSigla(sigla);
//			if (!CpDao.getInstance().consultarExistenciaServicoEmConfiguracao2(servico.getIdServico(), pessoa.getIdPessoa())) { 
//				receberEmail = 1;
//				restringir = 1;
//				Cp.getInstance().getConf().adicionaNovaConfiguracao(servico, restringir, receberEmail, pessoa);
//			}
//		}
//		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
//				SIGA_CEMAIL_ALTSENHA )) {
//			servico = new CpServico();
//			sigla = CpAcoesDeNotificarPorEmail.SIGA_CEMAIL.getSigla() + CpAcoesDeNotificarPorEmail.ALTERAR_MINHA_SENHA.getSigla();
//			servico = CpDao.getInstance().consultarServicoPorSigla(sigla);
//			if (!CpDao.getInstance().consultarExistenciaServicoEmConfiguracao2(servico.getIdServico(), pessoa.getIdPessoa())) { 
//				receberEmail = 1;
//				restringir = 1;
//				Cp.getInstance().getConf().adicionaNovaConfiguracao(servico, restringir, receberEmail, pessoa);
//			}
//		}
//		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
//				SIGA_CEMAIL_CADUSU)) {
//			servico = new CpServico();
//			sigla = CpAcoesDeNotificarPorEmail.SIGA_CEMAIL.getSigla() + CpAcoesDeNotificarPorEmail.CADASTRO_USUARIO.getSigla();
//			servico = CpDao.getInstance().consultarServicoPorSigla(sigla);
//			if (!CpDao.getInstance().consultarExistenciaServicoEmConfiguracao2(servico.getIdServico(), pessoa.getIdPessoa())) { 
//				receberEmail = 1;
//				restringir = 1;
//				Cp.getInstance().getConf().adicionaNovaConfiguracao(servico, restringir, receberEmail, pessoa);
//			}
//		}
//		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
//				SIGA_CEMAIL_CONSSIG)) {
//			servico = new CpServico();
//			sigla = CpAcoesDeNotificarPorEmail.SIGA_CEMAIL.getSigla() + CpAcoesDeNotificarPorEmail.CONSSIGNATARIO.getSigla();
//			servico = CpDao.getInstance().consultarServicoPorSigla(sigla);
//			if (!CpDao.getInstance().consultarExistenciaServicoEmConfiguracao2(servico.getIdServico(), pessoa.getIdPessoa())) { 
//				receberEmail = 1;
//				restringir = 1;
//				Cp.getInstance().getConf().adicionaNovaConfiguracao(servico, restringir, receberEmail, pessoa);
//			}
//		}
//		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
//				SIGA_CEMAIL_DOCMARC)) {
//			servico = new CpServico();
//			sigla = CpAcoesDeNotificarPorEmail.SIGA_CEMAIL.getSigla() + CpAcoesDeNotificarPorEmail.DOC_MARCADORES.getSigla();
//			servico = CpDao.getInstance().consultarServicoPorSigla(sigla);
//			if (!CpDao.getInstance().consultarExistenciaServicoEmConfiguracao2(servico.getIdServico(), pessoa.getIdPessoa())) { 
//				receberEmail = 1;
//				restringir = 1;
//				Cp.getInstance().getConf().adicionaNovaConfiguracao(servico, restringir, receberEmail, pessoa);
//			}
//		}
//		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
//				SIGA_CEMAIL_DOCTUN)) {
//			servico = new CpServico();
//			sigla = CpAcoesDeNotificarPorEmail.SIGA_CEMAIL.getSigla() + CpAcoesDeNotificarPorEmail.DOC_TRAMIT_PARA_M_UNIDADE.getSigla();
//			servico = CpDao.getInstance().consultarServicoPorSigla(sigla);
//			if (!CpDao.getInstance().consultarExistenciaServicoEmConfiguracao2(servico.getIdServico(), pessoa.getIdPessoa())) { 
//				receberEmail = 1;
//				restringir = 1;
//				Cp.getInstance().getConf().adicionaNovaConfiguracao(servico, restringir, receberEmail, pessoa);
//			}
//		}
//		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
//				SIGA_CEMAIL_DOCTUSU)) {
//			servico = new CpServico();
//			sigla = CpAcoesDeNotificarPorEmail.SIGA_CEMAIL.getSigla() + CpAcoesDeNotificarPorEmail.DOC_TRAMIT_PARA_MEU_USU.getSigla();
//			servico = CpDao.getInstance().consultarServicoPorSigla(sigla);
//			if (!CpDao.getInstance().consultarExistenciaServicoEmConfiguracao2(servico.getIdServico(), pessoa.getIdPessoa())) { 
//				receberEmail = 1;
//				restringir = 1;
//				Cp.getInstance().getConf().adicionaNovaConfiguracao(servico, restringir, receberEmail, pessoa);
//			}
//		}
//		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
//				SIGA_CEMAIL_ESQSENHA)) {
//			servico = new CpServico();
//			sigla = CpAcoesDeNotificarPorEmail.SIGA_CEMAIL.getSigla() + CpAcoesDeNotificarPorEmail.ESQUECI_MINHA_SENHA.getSigla();
//			servico = CpDao.getInstance().consultarServicoPorSigla(sigla);
//			if (!CpDao.getInstance().consultarExistenciaServicoEmConfiguracao2(servico.getIdServico(), pessoa.getIdPessoa())) { 
//				receberEmail = 1;
//				restringir = 1;
//				Cp.getInstance().getConf().adicionaNovaConfiguracao(servico, restringir, receberEmail, pessoa);
//			}
//		}
//		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
//				SIGA_CEMAIL_RESPASSI)) {
//			servico = new CpServico();
//			sigla = CpAcoesDeNotificarPorEmail.SIGA_CEMAIL.getSigla() + CpAcoesDeNotificarPorEmail.RESPONS_ASSINATURA.getSigla();
//			servico = CpDao.getInstance().consultarServicoPorSigla(sigla);
//			if (!CpDao.getInstance().consultarExistenciaServicoEmConfiguracao2(servico.getIdServico(), pessoa.getIdPessoa())) { 
//				receberEmail = 1;
//				restringir = 1;
//				Cp.getInstance().getConf().adicionaNovaConfiguracao(servico, restringir, receberEmail, pessoa);
//			}
//		}
//		if (Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(pessoa, lotacao,
//				SIGA_CEMAIL_SUB)) {
//			servico = new CpServico();
//			sigla = CpAcoesDeNotificarPorEmail.SIGA_CEMAIL.getSigla() + CpAcoesDeNotificarPorEmail.SUBSTITUICAO.getSigla();
//			servico = CpDao.getInstance().consultarServicoPorSigla(sigla);
//			if (!CpDao.getInstance().consultarExistenciaServicoEmConfiguracao2(servico.getIdServico(), pessoa.getIdPessoa())) { 
//				receberEmail = 1;
//				restringir = 1;
//				Cp.getInstance().getConf().adicionaNovaConfiguracao(servico, restringir, receberEmail, pessoa);
//			}
//		}
		
	}
	
	public void verificandoAusenciaDeAcoesParaUsuario (DpPessoa pessoa) {
		
		
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
	
}
