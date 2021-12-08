package br.gov.jfrj.siga.vraptor;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.DpNotificarPorEmail;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.dp.dao.DpNotificarPorEmailDaoFiltro;

@Controller
public class NotificarPorEmailController extends SigaSelecionavelControllerSupport<DpNotificarPorEmail, DpNotificarPorEmailDaoFiltro>{

	/**
	 * @deprecated CDI eyes only
	 */
	public NotificarPorEmailController() {
		super();
	}
	
	@Inject
	public NotificarPorEmailController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em) {
		super(request, result, dao, so, em);
	}
	
	protected boolean temPermissaoParaExportarDados() {
		return Boolean.valueOf(Cp.getInstance().getConf().podeUtilizarServicoPorConfiguracao(getTitular(), getTitular().getLotacao(),"SIGA;GI;CAD_CARGO;EXP_DADOS"));
	}
	
	@Override
	protected DpNotificarPorEmailDaoFiltro createDaoFiltro() {
		return null; 
	}
	
	@Get({ "/app/notificarPorEmail/rec_notificacao_por_email", "/public/app/page/usuario/rec_notificacao_por_email" })
	public void lista(Integer paramoffset) throws Exception {	
		if(paramoffset == null) {
			paramoffset = 0;
		}
		//setItens(CpDao.getInstance().consultarNotificaocaoEmail(paramoffset, 15, getTitular().getIdPessoa()));
		setItens(CpDao.getInstance().consultarNotificaocaoEmail2(paramoffset, 15, getTitular().getIdPessoa()));
		result.include("itens", getItens());
		result.include("tamanho", dao().consultarQuantidadeNotificacaoPorEmail()); 
		setItemPagina(15); 
		result.include("currentPageNumber", calculaPaginaAtual(paramoffset));
		result.forwardTo("/WEB-INF/page/usuario/notificarPorEmail.jsp"); 
	}
	
//	@Transacional
//	@Get({ "/app/notificarPorEmail/rec_notificacao_por_email_gravar" })
//	public void gravar() throws Exception {
//		List<DpNotificarPorEmail> listaNotificarPorEmail = CpDao.getInstance().consultarNotificaocaoEmail(0, 15, getTitular().getIdPessoa());
//		if (listaNotificarPorEmail.isEmpty()) {
//			Date data = dao().consultarDataEHoraDoServidor();
//			DpPessoa dpPessoa = new DpPessoa();
//			dpPessoa.setIdPessoa(getTitular().getIdPessoa()); 
//				 
//			cadastroDeNovoUsuario(data, dpPessoa);
//			alterarMinhaSenha(data, dpPessoa);
//			esqueciMinhaSenha(data, dpPessoa);
//			responsavelPelaAssinatura(data, dpPessoa); 
//			conssignatario(data, dpPessoa);
//			substituicao(data, dpPessoa);
//			documentoTramitadoParaOMeuUsuario(data, dpPessoa);
//			documentoTramitadoParaUnidade(data, dpPessoa);
//			tramitacaoDeDocumentosMarcados(data, dpPessoa);
//			alteracaoDeEmail(data, dpPessoa);
//			documentoDeMarcadores(data, dpPessoa);
//		}
//		result.redirectTo(NotificarPorEmailController.class).lista(0);
//	} 
	
	@Transacional
	@Post({ "/app/notificarPorEmail/rec_notificacao_por_email2" })
	public void notificar(final int codigo) throws Exception {
		DpNotificarPorEmail emailUser = dao().consultarPeloCodigoNotificacaoPoremail(codigo, getTitular().getIdPessoa());
		
		emailUser.setId(emailUser.getId());
		emailUser.setDpPessoa(getTitular()); 
		
		int configuravel = emailUser.getConfiguravel();
		int naoConfiguravel = emailUser.getNaoConfiguravel();
		
		if (configuravel == 1 && naoConfiguravel == 0) {
			emailUser.setConfiguravel(0);
			emailUser.setNaoConfiguravel(1);
		} else { 
			emailUser.setConfiguravel(1);
			emailUser.setNaoConfiguravel(0); 
		}
		
		if (configuravel == 0 && naoConfiguravel == 1) {
			emailUser.setConfiguravel(1);
			emailUser.setNaoConfiguravel(0);
		} else {
			emailUser.setConfiguravel(0);
			emailUser.setNaoConfiguravel(1);
		}
		
		result.redirectTo(NotificarPorEmailController.class).lista(0);
	}

	private void documentoDeMarcadores(Date data, DpPessoa dpPessoa) {
		DpNotificarPorEmail documentoDeMarcadores = new DpNotificarPorEmail();
		documentoDeMarcadores.setCodigo(12);
		documentoDeMarcadores.setConfiguravel(1); 
		documentoDeMarcadores.setNaoConfiguravel(0);
		documentoDeMarcadores.setDpPessoa(dpPessoa);
		documentoDeMarcadores.setHisAtivo(1);
		documentoDeMarcadores.setRestringir(0);
		documentoDeMarcadores.setNomeDaAcao("Documentos de marcadores");
		documentoDeMarcadores.setDataFimNotificarPorEmail(data);
		documentoDeMarcadores.setDataInicioNotificarPorEmail(data);
		documentoDeMarcadores.setDataFim(data);
		documentoDeMarcadores.setDataInicio(data);
		documentoDeMarcadores.setHisDtFim(data);
		documentoDeMarcadores.setHisDtIni(data);
		documentoDeMarcadores.setCadastrado(1);
		
		dao.gravarComHistorico(documentoDeMarcadores, getIdentidadeCadastrante());
	}

	private void alteracaoDeEmail(Date data, DpPessoa dpPessoa) {
		DpNotificarPorEmail alteracaoDeEmail = new DpNotificarPorEmail();
		alteracaoDeEmail.setCodigo(11);
		alteracaoDeEmail.setConfiguravel(1); 
		alteracaoDeEmail.setNaoConfiguravel(0);
		alteracaoDeEmail.setDpPessoa(dpPessoa);
		alteracaoDeEmail.setHisAtivo(1);
		alteracaoDeEmail.setRestringir(0);
		alteracaoDeEmail.setNomeDaAcao("Alteração de email");
		alteracaoDeEmail.setDataFimNotificarPorEmail(data);
		alteracaoDeEmail.setDataInicioNotificarPorEmail(data);
		alteracaoDeEmail.setDataFim(data);
		alteracaoDeEmail.setDataInicio(data);
		alteracaoDeEmail.setHisDtFim(data);
		alteracaoDeEmail.setHisDtIni(data);
		alteracaoDeEmail.setCadastrado(1);
		dao.gravarComHistorico(alteracaoDeEmail, getIdentidadeCadastrante());
	}

	private void tramitacaoDeDocumentosMarcados(Date data, DpPessoa dpPessoa) {
		DpNotificarPorEmail tramitacaoDeDocumentosMarcados = new DpNotificarPorEmail();
		tramitacaoDeDocumentosMarcados.setCodigo(10);
		tramitacaoDeDocumentosMarcados.setConfiguravel(1); 
		tramitacaoDeDocumentosMarcados.setNaoConfiguravel(0);
		tramitacaoDeDocumentosMarcados.setDpPessoa(dpPessoa);
		tramitacaoDeDocumentosMarcados.setHisAtivo(1);
		tramitacaoDeDocumentosMarcados.setRestringir(0);
		tramitacaoDeDocumentosMarcados.setNomeDaAcao("Tramitação de documentos marcados");
		tramitacaoDeDocumentosMarcados.setDataFimNotificarPorEmail(data);
		tramitacaoDeDocumentosMarcados.setDataInicioNotificarPorEmail(data);
		tramitacaoDeDocumentosMarcados.setDataFim(data);
		tramitacaoDeDocumentosMarcados.setDataInicio(data);
		tramitacaoDeDocumentosMarcados.setHisDtFim(data);
		tramitacaoDeDocumentosMarcados.setHisDtIni(data);
		tramitacaoDeDocumentosMarcados.setCadastrado(1);
		dao.gravarComHistorico(tramitacaoDeDocumentosMarcados, getIdentidadeCadastrante());
	}

	private void documentoTramitadoParaUnidade(Date data, DpPessoa dpPessoa) {
		DpNotificarPorEmail documentoTramitadoParaUnidade = new DpNotificarPorEmail();
		documentoTramitadoParaUnidade.setCodigo(9);
		documentoTramitadoParaUnidade.setConfiguravel(1); 
		documentoTramitadoParaUnidade.setNaoConfiguravel(0);
		documentoTramitadoParaUnidade.setDpPessoa(dpPessoa);
		documentoTramitadoParaUnidade.setHisAtivo(1);
		documentoTramitadoParaUnidade.setRestringir(0);
		documentoTramitadoParaUnidade.setNomeDaAcao("Documento tramitado para unidade");
		documentoTramitadoParaUnidade.setDataFimNotificarPorEmail(data);
		documentoTramitadoParaUnidade.setDataInicioNotificarPorEmail(data);
		documentoTramitadoParaUnidade.setDataFim(data);
		documentoTramitadoParaUnidade.setDataInicio(data);
		documentoTramitadoParaUnidade.setHisDtFim(data);
		documentoTramitadoParaUnidade.setHisDtIni(data);
		documentoTramitadoParaUnidade.setCadastrado(1);
		dao.gravarComHistorico(documentoTramitadoParaUnidade, getIdentidadeCadastrante());
	}

	private void documentoTramitadoParaOMeuUsuario(Date data, DpPessoa dpPessoa) {
		DpNotificarPorEmail documentoTramitadoParaOMeuUsuario = new DpNotificarPorEmail();
		documentoTramitadoParaOMeuUsuario.setCodigo(8);
		documentoTramitadoParaOMeuUsuario.setConfiguravel(1); 
		documentoTramitadoParaOMeuUsuario.setNaoConfiguravel(0);
		documentoTramitadoParaOMeuUsuario.setDpPessoa(dpPessoa);
		documentoTramitadoParaOMeuUsuario.setHisAtivo(1);
		documentoTramitadoParaOMeuUsuario.setRestringir(0);
		documentoTramitadoParaOMeuUsuario.setNomeDaAcao("Documento tramitado para o meu usuário");
		documentoTramitadoParaOMeuUsuario.setDataFimNotificarPorEmail(data);
		documentoTramitadoParaOMeuUsuario.setDataInicioNotificarPorEmail(data);
		documentoTramitadoParaOMeuUsuario.setDataFim(data);
		documentoTramitadoParaOMeuUsuario.setDataInicio(data);
		documentoTramitadoParaOMeuUsuario.setHisDtFim(data);
		documentoTramitadoParaOMeuUsuario.setHisDtIni(data);
		documentoTramitadoParaOMeuUsuario.setCadastrado(1);
		dao.gravarComHistorico(documentoTramitadoParaOMeuUsuario, getIdentidadeCadastrante());
	}

	private void substituicao(Date data, DpPessoa dpPessoa) {
		DpNotificarPorEmail substituicao = new DpNotificarPorEmail();
		substituicao.setCodigo(7);
		substituicao.setConfiguravel(1); 
		substituicao.setNaoConfiguravel(0);
		substituicao.setDpPessoa(dpPessoa);
		substituicao.setHisAtivo(1);
		substituicao.setRestringir(1);
		substituicao.setNomeDaAcao("Substituição");
		substituicao.setDataFimNotificarPorEmail(data);
		substituicao.setDataInicioNotificarPorEmail(data);
		substituicao.setDataFim(data);
		substituicao.setDataInicio(data);
		substituicao.setHisDtFim(data);
		substituicao.setHisDtIni(data);
		substituicao.setCadastrado(1);
		dao.gravarComHistorico(substituicao, getIdentidadeCadastrante());
	}

	private void conssignatario(Date data, DpPessoa dpPessoa) {
		DpNotificarPorEmail conssignatario = new DpNotificarPorEmail();
		conssignatario.setCodigo(6);
		conssignatario.setConfiguravel(1); 
		conssignatario.setNaoConfiguravel(0);
		conssignatario.setDpPessoa(dpPessoa);
		conssignatario.setHisAtivo(1);
		conssignatario.setRestringir(1);
		conssignatario.setNomeDaAcao("Consignatário");
		conssignatario.setDataFimNotificarPorEmail(data);
		conssignatario.setDataInicioNotificarPorEmail(data);
		conssignatario.setDataFim(data);
		conssignatario.setDataInicio(data);
		conssignatario.setHisDtFim(data);
		conssignatario.setHisDtIni(data);
		conssignatario.setCadastrado(1);
		dao.gravarComHistorico(conssignatario, getIdentidadeCadastrante());
	}

	private void responsavelPelaAssinatura(Date data, DpPessoa dpPessoa) {
		DpNotificarPorEmail responsavelPelaAssinatura = new DpNotificarPorEmail();
		responsavelPelaAssinatura.setCodigo(5);
		responsavelPelaAssinatura.setConfiguravel(1); 
		responsavelPelaAssinatura.setNaoConfiguravel(0);
		responsavelPelaAssinatura.setDpPessoa(dpPessoa);
		responsavelPelaAssinatura.setHisAtivo(1);
		responsavelPelaAssinatura.setRestringir(1);
		responsavelPelaAssinatura.setNomeDaAcao("Responsável pela assinatura");
		responsavelPelaAssinatura.setDataFimNotificarPorEmail(data);
		responsavelPelaAssinatura.setDataInicioNotificarPorEmail(data);
		responsavelPelaAssinatura.setDataFim(data);
		responsavelPelaAssinatura.setDataInicio(data);
		responsavelPelaAssinatura.setHisDtFim(data);
		responsavelPelaAssinatura.setHisDtIni(data);
		responsavelPelaAssinatura.setCadastrado(1);
		dao.gravarComHistorico(responsavelPelaAssinatura, getIdentidadeCadastrante());
	}

	private void esqueciMinhaSenha(Date data, DpPessoa dpPessoa) {
		DpNotificarPorEmail esqueciMinhaSenha = new DpNotificarPorEmail();
		esqueciMinhaSenha.setCodigo(3);
		esqueciMinhaSenha.setConfiguravel(1); 
		esqueciMinhaSenha.setNaoConfiguravel(0);
		esqueciMinhaSenha.setDpPessoa(dpPessoa);
		esqueciMinhaSenha.setHisAtivo(1);
		esqueciMinhaSenha.setRestringir(1);
		esqueciMinhaSenha.setNomeDaAcao("Esqueci minha senha");
		esqueciMinhaSenha.setDataFimNotificarPorEmail(data);
		esqueciMinhaSenha.setDataInicioNotificarPorEmail(data);
		esqueciMinhaSenha.setDataFim(data);
		esqueciMinhaSenha.setDataInicio(data);
		esqueciMinhaSenha.setHisDtFim(data);
		esqueciMinhaSenha.setHisDtIni(data);
		esqueciMinhaSenha.setCadastrado(1);
		dao.gravarComHistorico(esqueciMinhaSenha, getIdentidadeCadastrante());
	}

	private void alterarMinhaSenha(Date data, DpPessoa dpPessoa) {
		DpNotificarPorEmail alterarMinhaSenha = new DpNotificarPorEmail();
		alterarMinhaSenha.setCodigo(2);
		alterarMinhaSenha.setConfiguravel(1); 
		alterarMinhaSenha.setNaoConfiguravel(0);
		alterarMinhaSenha.setDpPessoa(dpPessoa);
		alterarMinhaSenha.setHisAtivo(1);
		alterarMinhaSenha.setRestringir(1);
		alterarMinhaSenha.setNomeDaAcao("Alterar minha senha");
		alterarMinhaSenha.setDataFimNotificarPorEmail(data);
		alterarMinhaSenha.setDataInicioNotificarPorEmail(data);
		alterarMinhaSenha.setDataFim(data);
		alterarMinhaSenha.setDataInicio(data);
		alterarMinhaSenha.setHisDtFim(data);
		alterarMinhaSenha.setHisDtIni(data);
		alterarMinhaSenha.setCadastrado(1);
		dao.gravarComHistorico(alterarMinhaSenha, getIdentidadeCadastrante());
	}

	private void cadastroDeNovoUsuario(Date data, DpPessoa dpPessoa) {
		DpNotificarPorEmail cadastroDeNovoUsuario = new DpNotificarPorEmail();
		cadastroDeNovoUsuario.setCodigo(1);
		cadastroDeNovoUsuario.setConfiguravel(1); 
		cadastroDeNovoUsuario.setNaoConfiguravel(0);
		cadastroDeNovoUsuario.setDpPessoa(dpPessoa);
		cadastroDeNovoUsuario.setHisAtivo(1);
		cadastroDeNovoUsuario.setRestringir(1);
		cadastroDeNovoUsuario.setNomeDaAcao("Cadastro de novo usuário");
		cadastroDeNovoUsuario.setDataFimNotificarPorEmail(data);
		cadastroDeNovoUsuario.setDataInicioNotificarPorEmail(data);
		cadastroDeNovoUsuario.setDataFim(data);
		cadastroDeNovoUsuario.setDataInicio(data);
		cadastroDeNovoUsuario.setHisDtFim(data);
		cadastroDeNovoUsuario.setHisDtIni(data);
		cadastroDeNovoUsuario.setCadastrado(1);
		dao.gravarComHistorico(cadastroDeNovoUsuario, getIdentidadeCadastrante());
	}
	
}

