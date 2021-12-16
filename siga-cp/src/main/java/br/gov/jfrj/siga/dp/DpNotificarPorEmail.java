package br.gov.jfrj.siga.dp;

import java.io.Serializable;
import java.util.Date;

import javax.transaction.Transactional;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.model.enm.CpAcoesDeNotificarPorEmail;
import br.gov.jfrj.siga.cp.model.enm.CpSituacaoDeConfiguracaoEnum;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.Historico;
import br.gov.jfrj.siga.model.Selecionavel;
import br.gov.jfrj.siga.sinc.lib.Sincronizavel;

@Cache(region = CpDao.CACHE_CORPORATIVO, usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class DpNotificarPorEmail extends CpConfiguracao implements Serializable, 
	Selecionavel, Historico, Sincronizavel, DpConvertableEntity {

	public DpNotificarPorEmail() { }
	
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
	
//	public void verifica (Long usuarioId) {
//		CpConfiguracao notificarPorEmail = new CpConfiguracao();
//		notificarPorEmail = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(
//		CpAcoesDeNotificarPorEmail.CADASTRO_USUARIO.getIdLong(), usuarioId);
//	}
	
	public void verificandoAusenciaDeAcoesParaUsuario (DpPessoa pessoa) {
		CpConfiguracao alterEmail = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.ALTERACAO_EMAIL.getIdLong(), pessoa.getIdPessoa());
		if (alterEmail == null) {
			Integer restringir = 1;
			CpServico servico = new CpServico();
			servico.setIdServico(CpAcoesDeNotificarPorEmail.ALTERACAO_EMAIL.getIdLong());
			adicionarServico(servico, restringir, pessoa);
		}
		CpConfiguracao alterSenha = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.ALTERAR_MINHA_SENHA.getIdLong(), pessoa.getIdPessoa());
		if (alterSenha == null) {
			Integer restringir = 0; 
			CpServico servico = new CpServico();
			servico.setIdServico(CpAcoesDeNotificarPorEmail.ALTERAR_MINHA_SENHA.getIdLong());
			adicionarServico(servico, restringir, pessoa);
		}
		CpConfiguracao cadUsu = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.CADASTRO_USUARIO.getIdLong(), pessoa.getIdPessoa());
		if (cadUsu == null) {
			Integer restringir = 0;
			CpServico servico = new CpServico();
			servico.setIdServico(CpAcoesDeNotificarPorEmail.CADASTRO_USUARIO.getIdLong());
			adicionarServico(servico, restringir, pessoa);
		}
		CpConfiguracao conssi = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.CONSSIGNATARIO.getIdLong(), pessoa.getIdPessoa());
		if (conssi == null) {
			Integer restringir = 0;
			CpServico servico = new CpServico();
			servico.setIdServico(CpAcoesDeNotificarPorEmail.CONSSIGNATARIO.getIdLong());
			adicionarServico(servico, restringir, pessoa);
		}
		CpConfiguracao docMarc = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.DOC_MARCADORES.getIdLong(), pessoa.getIdPessoa());
		if (docMarc == null) {
			Integer restringir = 1;
			CpServico servico = new CpServico();
			servico.setIdServico(CpAcoesDeNotificarPorEmail.DOC_MARCADORES.getIdLong());
			adicionarServico(servico, restringir, pessoa);
		}
		CpConfiguracao docTramiUnidade = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.DOC_TRAMIT_PARA_M_UNIDADE.getIdLong(), pessoa.getIdPessoa());
		if (docTramiUnidade == null) {
			Integer restringir = 1;
			CpServico servico = new CpServico();
			servico.setIdServico(CpAcoesDeNotificarPorEmail.DOC_TRAMIT_PARA_M_UNIDADE.getIdLong());
			adicionarServico(servico, restringir, pessoa);
		}
		CpConfiguracao docTramiUsu = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.DOC_TRAMIT_PARA_MEU_USU.getIdLong(), pessoa.getIdPessoa());
		if (docTramiUsu == null) {
			Integer restringir = 1;
			CpServico servico = new CpServico();
			servico.setIdServico(CpAcoesDeNotificarPorEmail.DOC_TRAMIT_PARA_MEU_USU.getIdLong());
			adicionarServico(servico, restringir, pessoa);
		}
		CpConfiguracao esqSenha = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.ESQUECI_MINHA_SENHA.getIdLong(), pessoa.getIdPessoa());
		if (esqSenha == null) {
			Integer restringir = 0;
			CpServico servico = new CpServico();
			servico.setIdServico(CpAcoesDeNotificarPorEmail.ESQUECI_MINHA_SENHA.getIdLong());
			adicionarServico(servico, restringir, pessoa);
		}
		CpConfiguracao respAssi = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.RESPONS_ASSINATURA.getIdLong(), pessoa.getIdPessoa());
		if (respAssi == null) {
			Integer restringir = 0;
			CpServico servico = new CpServico();
			servico.setIdServico(CpAcoesDeNotificarPorEmail.RESPONS_ASSINATURA.getIdLong());
			adicionarServico(servico, restringir, pessoa);
		}
		CpConfiguracao sub = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.SUBSTITUICAO.getIdLong(), pessoa.getIdPessoa());
		if (sub == null) {
			Integer restringir = 0;
			CpServico servico = new CpServico();
			servico.setIdServico(CpAcoesDeNotificarPorEmail.SUBSTITUICAO.getIdLong()); 
			adicionarServico(servico, restringir, pessoa);
		}
		CpConfiguracao tramiDocMarca = CpDao.getInstance().consultarExistenciaDeServicosEmAcoesDeNotificacaoPorEmail(CpAcoesDeNotificarPorEmail.TRAMIT_DOC_MARCADOS.getIdLong(), pessoa.getIdPessoa());
		if (tramiDocMarca == null) {
			Integer restringir = 1;
			CpServico servico = new CpServico();
			servico.setIdServico(CpAcoesDeNotificarPorEmail.TRAMIT_DOC_MARCADOS.getIdLong());
			adicionarServico(servico, restringir, pessoa);
		}
	}
	
	@Transactional
	public void adicionarServico (CpServico servico, Integer restringir, DpPessoa pessoa) {
		CpConfiguracao config = new CpConfiguracao(); 
		CpDao.getInstance().iniciarTransacao();
		config.setCpServico(servico);
		config.setHisDtIni(CpDao.getInstance().consultarDataEHoraDoServidor());
		config.setDpPessoa(pessoa);
		config.setCpSituacaoConfiguracao(CpSituacaoDeConfiguracaoEnum.PODE); 
		config.setRestringir(restringir);
		CpDao.getInstance().gravar(config);
		CpDao.getInstance().commitTransacao();
	}
}
