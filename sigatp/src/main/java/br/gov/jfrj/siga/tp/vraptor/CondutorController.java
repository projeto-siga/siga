package br.gov.jfrj.siga.tp.vraptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.ValidationMessage;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.codec.binary.Base64;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdmin;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissao;
import br.gov.jfrj.siga.tp.auth.annotation.RoleAdminMissaoComplexo;
import br.gov.jfrj.siga.tp.model.CategoriaCNH;
import br.gov.jfrj.siga.tp.model.Condutor;
import br.gov.jfrj.siga.tp.model.Imagem;
import br.gov.jfrj.siga.tp.model.ItemMenu;
import br.gov.jfrj.siga.tp.model.TpDao;
import br.gov.jfrj.siga.tp.util.ArquivoUploadUtil;
import br.gov.jfrj.siga.tp.util.FormatarTextoHtml;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Controller
@Path("/app/condutor")
public class CondutorController extends TpController {
	
	private static final String CONDUTOR = "condutor";

	/**
	 * @deprecated CDI eyes only
	 */
	public CondutorController() {
		super();
	}
	
	@Inject	
	public CondutorController(HttpServletRequest request, Result result,   Validator validator, SigaObjects so,  EntityManager em) {
		super(request, result, TpDao.getInstance(), validator, so, em);
	}

	@Path("/listar")
	public void listar() {
		result.include("condutores", getCondutores());
	}
	
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/editar/{id}")
	public void editar(Long id) throws Exception {
		Condutor condutor;
		if(id > 0)  {
			condutor = Condutor.AR.findById(id);
			if (condutor.getDpPessoa() != null) 
				condutor.setDpPessoa(recuperaPessoa(condutor.getDpPessoa()));
			else 
				condutor.setDpPessoa(new DpPessoa());
			
			result.include("categoriaCNH", condutor.getCategoriaCNH().getDescricao());
			
			if(condutor.getConteudoimagemblob() != null) 
				result.include("imgArquivo", "data:image/jpg;base64," + Base64.encodeBase64String(condutor.getConteudoimagemblob()));
			
		} else 
			condutor = new Condutor();

		MenuMontador.instance(result).recuperarMenuCondutores(id, ItemMenu.DADOSCADASTRAIS);
		result.include("listCategorias", CategoriaCNH.values());
		result.include(CONDUTOR, condutor);
	}

    @Transactional
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/salvar")
	public void salvar(@Valid Condutor condutor, final UploadedFile arquivo) throws Exception {
		if (arquivo != null) {
			byte[] arquivoConteudo = ArquivoUploadUtil.toByteArray(arquivo);
			error(!Imagem.tamanhoImagemAceito(arquivoConteudo.length), "imagem", "condutor.tamanhoImagemAceito.validation");
			
			error(!arquivo.getContentType().startsWith("image"), "imagem", "condutores.arquivoImagem.validation");
			condutor.setConteudoimagemblob(arquivoConteudo);
		} else 
			if (condutor.getSituacaoImagem() != null && "semimagem".equals(condutor.getSituacaoImagem())) 
				condutor.setConteudoimagemblob(null);
		
		error(condutor.getDpPessoa().getId() == null, "dpPessoa", "condutor.dppessoa.validation");

		if (validator.hasErrors()) {
			if (condutor.getDpPessoa().getId() != null) 
				condutor.setDpPessoa(recuperaPessoa(condutor.getDpPessoa()));
			else 
				condutor.setDpPessoa(new DpPessoa());
			
			result.include(CONDUTOR, condutor);
			result.include("listCategorias", CategoriaCNH.values());
			if(condutor.getId() > 0) 
				validator.onErrorUse(Results.page()).of(CondutorController.class).editar(condutor.getId());
			else
				validator.onErrorUse(Results.page()).of(CondutorController.class).editar(0L);
		}

		condutor.setCpOrgaoUsuario(getTitular().getOrgaoUsuario());
		condutor.save();
		result.redirectTo(CondutorController.class).listar();
	}

    @Transactional
    @RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/excluir/{id}")
	public void excluir(Long id) throws Exception {
		EntityTransaction tx = Condutor.AR.em().getTransaction();
		Condutor condutor = Condutor.AR.findById(id);

		if (!tx.isActive()) 
			tx.begin();

		try {
			condutor.delete();
			tx.commit();
			result.redirectTo(CondutorController.class).listar();
		} catch (PersistenceException ex) {
			tx.rollback();
			
			if (FormatarTextoHtml.removerAcentuacao(ex.getCause().getCause().getMessage()).contains("restricao de integridade")) 
				validator.add(new I18nMessage(CONDUTOR, "condutor.excluir.validation"));
			else 
				validator.add((Message) new ValidationMessage(ex.getMessage(), CONDUTOR));
			
			validator.onErrorForwardTo(CondutorController.class).listar();
		} catch (Exception ex) {
			tx.rollback();
			validator.add((Message) new ValidationMessage(ex.getMessage(), CONDUTOR));
			validator.onErrorForwardTo(CondutorController.class).listar();
		}
	}
	
	@RoleAdmin
	@RoleAdminMissao
	@RoleAdminMissaoComplexo
	@Path("/incluir")
	public void incluir() throws Exception {
		result.forwardTo(this).editar(0L);
	}
	
	@Path("/exibirDadosDpPessoa/{idPessoa}")
	public void exibirDadosDpPessoa(Long idPessoa) throws Exception {
		DpPessoa pessoa = DpPessoa.AR.findById(idPessoa);
		result.include("pessoa", pessoa);
	}
	
	@Path("/exibirImagem/{id}")
	public void exibirImagem(Long id) throws Exception {
		Condutor condutor = Condutor.AR.findById(id);
		result.include(CONDUTOR, condutor);
		result.include("imgArquivo", "data:image/jpg;base64," + Base64.encodeBase64String(condutor.getConteudoimagemblob()));
	}
	
	private DpPessoa recuperaPessoa(DpPessoa dpPessoa) throws Exception {
		Map<String, Object> parametros = new HashMap<String,Object>();
		parametros.put("idPessoaIni",dpPessoa.getIdInicial());
		return 	DpPessoa.AR.find("idPessoaIni = :idPessoaIni and dataFimPessoa = null", 
				parametros).first();
	}

	private List<Condutor> getCondutores() {
		try {
			return Condutor.listarTodos(getTitular().getOrgaoUsuario());
		} catch (Exception ignore) {
			return new ArrayList<Condutor>();
		}
	}
}