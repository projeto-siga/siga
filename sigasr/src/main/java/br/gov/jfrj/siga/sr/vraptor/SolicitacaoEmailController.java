package br.gov.jfrj.siga.sr.vraptor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.base.SigaBaseProperties;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrFormaAcompanhamento;
import br.gov.jfrj.siga.sr.model.SrGravidade;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;
import br.gov.jfrj.siga.sr.model.SrMeioComunicacao;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("/public/app/solicitacaoEmail")
public class SolicitacaoEmailController extends SrController {
	
	private static final String _NOME_PARAM_ACAO_SIGLA = "sigasr.solicitacaoPorEmail.acao.sigla";
	private static final String _NOME_PARAM_ITEM_SIGLA = "sigasr.solicitacaoPorEmail.item.sigla";
	private static final String _MSG_MIME_TYPE = "application/vnd.ms-outlook"; // antes message/rfc822

	class Retorno {
		public String codigo;
		public String mensagem;
		public Retorno(String codigo, String mensagem) {
			this.codigo = codigo;
			this.mensagem = mensagem;
		}
	}
	
	public SolicitacaoEmailController(HttpServletRequest request, Result result,  SigaObjects so, EntityManager em, SrValidator srValidator) {
		super(request, result, CpDao.getInstance(), so, em, srValidator);
	}
	
	private UploadedFile getArquivoMsg(final byte[] mensagem, final String assunto) {
		return new UploadedFile() {
			
			@Override
			public long getSize() {
				return mensagem.length;
			}
			
			@Override
			public String getFileName() {
				return limparParaNomeDeArquivo(assunto) + ".msg";
			}
			
			private String limparParaNomeDeArquivo(String texto) {
				String retorno = texto.replaceAll("\\s","_");
				if(retorno.length() > 30) {
					retorno = retorno.substring(retorno.length() - 30);
				}
				return retorno;
			}

			@Override
			public InputStream getFile() {
				try {
					return new ByteArrayInputStream(mensagem);
				} catch (Exception e) {
					return null;
				} 
			}
			
			@Override
			public String getContentType() {
				return _MSG_MIME_TYPE;
			}
		};
	}

	private SrSolicitacao criarSolicitacao(DpPessoa pessoa, String descricao, String textoMensagem, UploadedFile arquivoMensagem, Calendar dataEnvio) throws Exception {
		String siglaDoItem = SigaBaseProperties.getString(_NOME_PARAM_ITEM_SIGLA);
		String siglaDaAcao = SigaBaseProperties.getString(_NOME_PARAM_ACAO_SIGLA);
		
		SrItemConfiguracao itemConfiguracao = (SrItemConfiguracao) SrItemConfiguracao.AR.find("siglaItemConfiguracao = ?", siglaDoItem).first();
		SrAcao acao = (SrAcao) SrAcao.AR.find("siglaAcao = ?", siglaDaAcao).first();
		
		ArrayList<SrConfiguracao> desigs = new ArrayList<SrConfiguracao>(itemConfiguracao.getDesignacoesAtivas());
		
		SrConfiguracao designacao = desigs.get(0);
		
		SrSolicitacao srSolicitacao = new SrSolicitacao();
		srSolicitacao.setAcao(acao);
		srSolicitacao.setItemConfiguracao(itemConfiguracao);
		srSolicitacao.setDesignacao(designacao);
		
		srSolicitacao.setArquivo(arquivoMensagem);
		srSolicitacao.setDescricao(descricao);
		srSolicitacao.setDescrSolicitacao(textoMensagem);
		srSolicitacao.setDtOrigem(dataEnvio.getTime());
		srSolicitacao.setDtOrigemString(dataEnvio.toString());
		srSolicitacao.setDtReg(new Date());
		srSolicitacao.setFormaAcompanhamento(SrFormaAcompanhamento.ABERTURA_ANDAMENTO);
		srSolicitacao.setGravidade(SrGravidade.SEM_GRAVIDADE);
		
		srSolicitacao.setMeioComunicacao(SrMeioComunicacao.EMAIL);
		srSolicitacao.setRascunho(false);

		if (pessoa != null) {
			srSolicitacao.setLotaSolicitante(pessoa.getLotacao());
			srSolicitacao.setLotaTitular(pessoa.getLotacao());
			srSolicitacao.setSolicitante(pessoa);
			srSolicitacao.setTelPrincipal(pessoa.getTelefone() == null ? "" : pessoa.getTelefone());
			srSolicitacao.setTitular(pessoa);
			srSolicitacao.setOrgaoUsuario(pessoa.getOrgaoUsuario());
			srSolicitacao.deduzirLocalRamalEMeioContato();
		}
		
		srSolicitacao.salvar(pessoa, pessoa.getLotacao(), pessoa, pessoa.getLotacao());
		return srSolicitacao;
	}
	
	private static DpPessoa localizarPessoaPeloEmail(String emailPessoa) {
		DpPessoa pessoa = ((DpPessoa) DpPessoa.AR.find("emailPessoa = ?", emailPessoa).first());

		return pessoa;
	}
	
	@Path("/incluir")
	public void incluir(String emailRemetente, String assunto, String textoEmail, String msgBase64, Calendar dataEnvio) {
		Retorno retorno = null;
		if(emailRemetente == null || msgBase64 == null || assunto == null) {
			retorno = new Retorno("0", "dados incompletos");
		} else {
			byte[] arquivoMsg = DatatypeConverter.parseBase64Binary(msgBase64);
			UploadedFile anexo = getArquivoMsg(arquivoMsg, assunto);
			
			DpPessoa pessoa = localizarPessoaPeloEmail(emailRemetente);
			if(pessoa == null) {
				retorno = new Retorno("0", "nao ha usuario do siga cadastrado com o email informado");
			} else {
			
				try {
					SrSolicitacao srSolicitacao = criarSolicitacao(pessoa, assunto, textoEmail, anexo, dataEnvio);
					retorno = new Retorno("1", srSolicitacao.getCodigo());
				} catch (Exception e) {
					retorno = new Retorno("0", "erro generico ao salvar a solicitacao: " + e.getMessage());
				}
			
			}
		}
		result.use(Results.xml()).from(retorno).serialize();
	}
}