package br.gov.jfrj.siga.vraptor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpIdentidade;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;

@Controller
public class IdentidadeController extends GiControllerSupport {


	/**
	 * @deprecated CDI eyes only
	 */
	public IdentidadeController() {
		super();
	}

	@Inject
	public IdentidadeController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);
	}
	
	@Get("/app/gi/identidade/listar")
	public void lista(DpPessoaSelecao pessoaSel) throws Exception {
		assertAcesso("ID:Gerenciar identidades");
		DpPessoa pes = definirPessoa(pessoaSel);

		if (pes != null) {
			result.include("itens", dao().consultaIdentidades(pes));
		}
		result.include("pessoaSel", enviarPessoaSelecao(pessoaSel));
	}

	private DpPessoaSelecao enviarPessoaSelecao(DpPessoaSelecao pessoaSel) {
		return (pessoaSel == null) ? new DpPessoaSelecao() : pessoaSel;
	}
	
	@Transacional
	@Get("/app/gi/identidade/editar_gravar")
	public void aEditarGravar(DpPessoaSelecao pessoaSel, String dtExpiracao, Long id) throws Exception {
		assertAcesso("ID:Gerenciar identidades");
		if (id == null)
			throw new AplicacaoException("Não foi informada id");

		Date dataExpiracao = null;
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		try {
			dataExpiracao = df.parse(dtExpiracao + " 00:00");
		} catch (final ParseException e) {
		} catch (final NullPointerException e) {
		}

		CpIdentidade ident = daoId(id);
		Cp.getInstance().getBL().alterarIdentidade(ident, dataExpiracao, getIdentidadeCadastrante());
		
		result.forwardTo(this).lista(pessoaSel);
	}

	@Transacional
	@Get("/app/gi/identidade/cancelar")
	public void aCancelar(Long id, DpPessoaSelecao pessoaSel) throws Exception {
		assertAcesso("ID:Gerenciar identidades");
		if (id == null)
			throw new AplicacaoException("Não foi informada id");

		CpIdentidade ident = daoId(id);
		Cp.getInstance().getBL().cancelarIdentidade(ident, getIdentidadeCadastrante());
		
		result.forwardTo(this).lista(pessoaSel);
	}

	@Transacional
	@Get("/app/gi/identidade/bloquear")
	public void aBloquear(Long id, DpPessoaSelecao pessoaSel) throws Exception {
		assertAcesso("ID:Gerenciar identidades");
		if (id != null) {
			CpIdentidade ident = daoId(id);
			Cp.getInstance().getBL().bloquearIdentidade(ident, getIdentidadeCadastrante(), true);
			result.forwardTo(this).lista(pessoaSel);
		} else
			throw new AplicacaoException("Não foi informada id");
	}

	@Transacional
	@Get("/app/gi/identidade/desbloquear")
	public void aDesbloquear(Long id, DpPessoaSelecao pessoaSel) throws Exception {
		assertAcesso("ID:Gerenciar identidades");
		if (id != null) {
			CpIdentidade ident = daoId(id);
			Cp.getInstance().getBL().bloquearIdentidade(ident,getIdentidadeCadastrante(), false);
			result.forwardTo(this).lista(pessoaSel);
		} else
			throw new AplicacaoException("Não foi informada id");
	}

	@Transacional
	@Get("/app/gi/identidade/bloquear_pessoa")
	public void aBloquearPessoa(DpPessoaSelecao pessoaSel) throws Exception {
		assertAcesso("ID:Gerenciar identidades");
		DpPessoa pes = definirPessoa(pessoaSel);

		if (pes != null) {
			Cp.getInstance().getBL().bloquearPessoa(pes,getIdentidadeCadastrante(), true);
			result.forwardTo(this).lista(pessoaSel);
		} else
			throw new AplicacaoException("Não foi informada a pessoa");
	}

	@Transacional
	@Get("/app/gi/identidade/desbloquear_pessoa")
	public void aDesbloquearPessoa(DpPessoaSelecao pessoaSel) throws Exception {
		assertAcesso("ID:Gerenciar identidades");
		DpPessoa pes = definirPessoa(pessoaSel);

		if (pes != null) {
			Cp.getInstance().getBL().bloquearPessoa(pes,getIdentidadeCadastrante(), false);
			result.forwardTo(this).lista(pessoaSel);
		} else
			throw new AplicacaoException("Não foi informada a pessoa");
	}
	
	private CpIdentidade daoId(long id) {
		return dao().consultar(id, CpIdentidade.class, false);
	}
	
	private DpPessoa definirPessoa(DpPessoaSelecao pessoaSel) {
		DpPessoa pessoa = null;
		
		if (pessoaSel != null) {
			pessoa = pessoaSel.buscarObjeto();
		}
		return pessoa;
	}
	
}
