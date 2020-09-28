package br.gov.jfrj.siga.vraptor;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Correio;
import br.gov.jfrj.siga.base.Data;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.cp.model.DpPessoaSelecao;
import br.gov.jfrj.siga.dp.CpPersonalizacao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.DpSubstituicao;
import br.gov.jfrj.siga.dp.dao.CpDao;

@Controller
public class SubstituicaoController extends SigaController {
	
	private Integer tipoTitular;
	private Integer tipoSubstituto;
	private String dtIniSubst;
	private String dtFimSubst;	
	private DpPessoaSelecao titularSel;
	private DpLotacaoSelecao lotaTitularSel;
	private DpPessoaSelecao substitutoSel;
	private DpLotacaoSelecao lotaSubstitutoSel;	
	
	private Map<Integer, String> getListaTipo() {
		final Map<Integer, String> map = new TreeMap<Integer, String>();
		map.put(1, "Matrícula");
		map.put(2, "Órgão Integrado");
		return map;
	}	
		


	/**
	 * @deprecated CDI eyes only
	 */
	public SubstituicaoController() {
		super();
	}

	@Inject
	public SubstituicaoController(HttpServletRequest request, Result result, SigaObjects so, EntityManager em) {
		super(request, result, CpDao.getInstance(), so, em);

		result.on(ServletException.class).forwardTo(this).appexception();
		result.on(Exception.class).forwardTo(this).exception();

		titularSel = new DpPessoaSelecao();	
		lotaTitularSel = new DpLotacaoSelecao();
		
		lotaSubstitutoSel = new DpLotacaoSelecao();
		substitutoSel = new DpPessoaSelecao();		
		
		tipoTitular = 1;
		tipoSubstituto = 1;		
	}
	
	private boolean podeCadastrarQualquerSubstituicao() throws Exception {
		return Cp.getInstance().getConf().podePorConfiguracao(
				getCadastrante(), getCadastrante().getLotacao(), 
				CpTipoConfiguracao.TIPO_CONFIG_CADASTRAR_QUALQUER_SUBST);
	}
	
	private List<DpSubstituicao> buscarSubstitutos(String substituicao, DpPessoa pessoa, DpLotacao lotacao) 
			throws SQLException, ServletException {
		
		Boolean isSubstLotacao = false;
		List<DpSubstituicao> todasSubst = new ArrayList<DpSubstituicao>();
		List<DpSubstituicao> substVigentes = new ArrayList<DpSubstituicao>();
		DpSubstituicao dpSubstituicao = new DpSubstituicao();
		dpSubstituicao.setTitular(pessoa);
		dpSubstituicao.setLotaTitular(lotacao);			
	    todasSubst = dao.consultarOrdemData(dpSubstituicao);
	    
	    if (getCadastrante().getId().equals(getTitular().getId())
				&& !getCadastrante().getLotacao().getId().equals(getLotaTitular().getId()))
	    	    	isSubstLotacao = true;	
	    
	    for (DpSubstituicao subst : todasSubst) {	
	    	if (substituicao == "true") {
	    		if (isSubstLotacao && subst.getTitular() != null)
	    			continue;	    		
	    	}
	    		
	    	if (subst.getLotaSubstituto() != null && subst.getLotaSubstituto().isFechada()
	    			&& substituicao == "false")	    		
	    		continue;
	    	if (subst.getSubstituto() != null && (subst.getSubstituto().isFechada() 
	    			|| subst.getSubstituto().isBloqueada()) && substituicao == "false")
	    		continue;
	    	if (subst.isEmVoga() || subst.isFutura()) {
	    		subst.setLotaSubstituto(subst.getLotaSubstituto() != null ? subst.getLotaSubstituto().getLotacaoAtual() : null);
	    		subst.setLotaTitular(subst.getLotaTitular() != null ? subst.getLotaTitular().getLotacaoAtual() : null);
	    		substVigentes.add(subst);	    		
	    	}
	    }
		return substVigentes;
	}
		
	@Get("/app/substituicao/listar")
	public void lista() throws Exception {
		String substituicao = "false";
		
		if (!getCadastrante().getId().equals(getTitular().getId())
				|| !getCadastrante().getLotacao().getId().equals(getLotaTitular().getId())) {
			if(podeCadastrarQualquerSubstituicao()){
				substituicao = "true";					
				result.include("itensTitular", buscarSubstitutos(substituicao, getTitular(), getLotaTitular()));
			}	
		}
		result.include("isSubstituicao", substituicao);
		result.include("itens", buscarSubstitutos(substituicao, getCadastrante(), getCadastrante().getLotacao()));
	}	
	
	@Get("/app/substituicao/editar")
	public void edita(Long id) throws Exception {
		//String buscarFechadas = "buscarFechadas="+podeCadastrarQualquerSubstituicao();
		String buscarFechadas = "buscarFechadas=true";
		result.include("strBuscarFechadas", buscarFechadas);
		
		if (id != null) {
			DpSubstituicao subst = dao().consultar(id, DpSubstituicao.class, false);

			if (subst.getTitular() == null) {
				tipoTitular = 2;
				lotaTitularSel.buscarPorObjeto(subst.getLotaTitular());
				
			} else {
				tipoTitular = 1;
				titularSel.buscarPorObjeto(subst.getTitular());
			}
			
			if (subst.getSubstituto() == null) {
				tipoSubstituto = 2;
				lotaSubstitutoSel.buscarPorObjeto(subst.getLotaSubstituto());
				
			} else {
				tipoSubstituto = 1;
				substitutoSel.buscarPorObjeto(subst.getSubstituto());
				
			}
			
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			try {
				dtFimSubst = df.format(subst.getDtFimSubst());
			} catch (final Exception e) {
			}

			try {
				dtIniSubst = df.format(subst.getDtIniSubst());
			} catch (final Exception e) {
			}
			result.include("substituicao", subst);
			result.include("dtIniSubst", dtIniSubst);
			result.include("dtFimSubst", dtFimSubst);				
		}
		
		//Dados do Titular
		result.include("listaTipoTitular", getListaTipo());
		result.include("tipoTitular", tipoTitular);
		result.include("titularSel", titularSel);//tipoTitular=1
		result.include("lotaTitularSel", lotaTitularSel);//tipoTitular=2
		
		//Dados do Substituto
		result.include("listaTipoSubstituto", getListaTipo());
		result.include("tipoSubstituto", tipoSubstituto);
		result.include("substitutoSel", substitutoSel);//tipoSubstituto=1
		result.include("lotaSubstitutoSel", lotaSubstitutoSel);//tipoSubstituto=2
	}
	
	@Post("/app/substituicao/gravar")
	public void gravar(DpSubstituicao substituicao
					  ,Integer tipoTitular
					  ,Integer tipoSubstituto			
			          ,DpPessoaSelecao titularSel
			          ,DpLotacaoSelecao lotaTitularSel
			          
			          ,DpPessoaSelecao substitutoSel
			          ,DpLotacaoSelecao lotaSubstitutoSel
				      ,String dtIniSubst
				      ,String dtFimSubst				          
					  ) throws Exception {
		
		
		Long lotacaoPai, lotacaoAvo = null, lotacaoIniPai, lotacaoIniAvo = null;	

		DpSubstituicao subst = new DpSubstituicao();
		
		this.tipoTitular = tipoTitular;
		this.titularSel = titularSel;//tipoTitular=1
		this.lotaTitularSel = lotaTitularSel;//tipoTitular=2
		
		this.tipoSubstituto = tipoSubstituto;
		this.substitutoSel = substitutoSel;//tipoSubstituto=1
		this.lotaSubstitutoSel = lotaSubstitutoSel;//tipoSubstituto=2
		
		try {
			dao().iniciarTransacao();
			if (tipoTitular == 1) {
				if (this.titularSel.getId() == null)
					throw new ServletException("Titular não informado");
				
				subst.setTitular(dao().consultar(this.titularSel.getId(),DpPessoa.class, false));
				
				if (!subst.getTitular().getIdPessoa().equals(getCadastrante().getIdPessoa())  
						&& !podeCadastrarQualquerSubstituicao())
					throw new ServletException("Titular não permitido. Apenas o próprio usuário pode se definir como titular.");
				
				subst.setLotaTitular(subst.getTitular().getLotacao());
			} else {
				subst.setTitular(null);
				if (this.lotaTitularSel.getId() == null)
					throw new ServletException("A lotação titular não foi informada");
				
				subst.setLotaTitular(dao().consultar(this.lotaTitularSel.getId(), DpLotacao.class, false));
				lotacaoIniPai = subst.getLotaTitular().getIdLotacaoIniPai();
				lotacaoPai = subst.getLotaTitular().getIdLotacaoPai();
				if(lotacaoPai != null)
					lotacaoAvo = subst.getLotaTitular().getLotacaoPai().getIdLotacaoPai();
				if(lotacaoIniPai != null)
					lotacaoIniAvo = subst.getLotaTitular().getLotacaoPai().getIdLotacaoIniPai();
				
				if (!subst.getLotaTitular().getIdLotacao().equals(getCadastrante().getIdLotacao()) 
						&& !podeCadastrarQualquerSubstituicao()) 
					if (!(!(lotacaoIniPai == null) && (lotacaoIniPai.equals(getCadastrante().getIdLotacaoIni())) ||
						  !(lotacaoIniAvo == null) && (lotacaoIniAvo.equals(getCadastrante().getIdLotacaoIni())) || 
						  !(lotacaoPai == null) && (lotacaoPai.equals(getCadastrante().getIdLotacao())) ||
						  !(lotacaoAvo == null) && (lotacaoAvo.equals(getCadastrante().getIdLotacao()))))
						throw new ServletException("Lotação titular não permitida. Apenas usuários da própria lotação ou lotados até 2 lotações superiores na hierarquia podem defini-la como titular.");
				
			}
			if (tipoSubstituto == 1) {
				if (this.substitutoSel.getId() == null)
					throw new ServletException("Substituto não informado"); 
				
				subst.setSubstituto(daoPes(this.substitutoSel.getId()));
				subst.setLotaSubstituto(subst.getSubstituto().getLotacao());
			} else {
				subst.setSubstituto(null);
				if (this.lotaSubstitutoSel.getId() == null)
					throw new ServletException("A lotação do substituto não foi informada");
				
				subst.setLotaSubstituto(daoLot(this.lotaSubstitutoSel.getId()));
			}
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			try {
				subst.setDtIniSubst(df.parse(dtIniSubst + " 00:00"));
			} catch (final ParseException e) {
				subst.setDtIniSubst(null);
			} catch (final NullPointerException e) {
				subst.setDtIniSubst(null);
			}
			try {
				subst.setDtFimSubst(df.parse(dtFimSubst + " 23:59"));
			} catch (final ParseException e) {
				subst.setDtFimSubst(null);
			} catch (final NullPointerException e) {
				subst.setDtFimSubst(null);
			}
			
			if (subst.getDtIniSubst() == null)
				subst.setDtIniSubst(new Date());
			else {
				if (!Data.dataDentroSeculo21(subst.getDtIniSubst()))
					throw new ServletException("Data inicial inválida, deve estar entre o ano 2000 e ano 2100");
			}
			
			if (subst.getDtFimSubst() == null) {
				throw new ServletException("Não é possível informar uma data final nula.");
				
			} else if (getIntervaloDeDiasEntreDatas(subst.getDtIniSubst(), subst.getDtFimSubst()) < 0) {
				throw new ServletException("Não é possível informar uma data final anterior a data inicial.");
			} else {
				Calendar c = Calendar.getInstance();
				c.setTime(subst.getDtIniSubst());
				c.add(Calendar.YEAR, 2);  
				
				if (c.getTime().compareTo(subst.getDtFimSubst()) < 0)  {
					throw new ServletException("Não é possível cadastrar período de substituição maior que 2 anos.");
			} else if (subst.getDtFimSubst() != null && !Data.dataDentroSeculo21(subst.getDtFimSubst()))
					throw new ServletException("Data final inválida, deve estar entre o ano 2000 e ano 2100");
			}				 
	

			subst.setDtIniRegistro(new Date());

			if (substituicao != null && substituicao.getIdSubstituicao() != null) {
				DpSubstituicao substAntiga = dao().consultar(substituicao.getIdSubstituicao(), DpSubstituicao.class, false);
				substAntiga.setDtFimRegistro(new Date());
				substAntiga = dao().gravar(substAntiga);
				subst.setIdRegistroInicial(substAntiga.getIdRegistroInicial());
			}

			subst = dao().gravar(subst);
			
			if (subst.getIdRegistroInicial() == null)
				subst.setIdRegistroInicial(subst.getIdSubstituicao());

			subst = dao().gravar(subst);
			
			enviarEmailSubstituicao(tipoTitular, tipoSubstituto, subst);
			
			result.redirectTo(this).lista();

			dao().commitTransacao();
		} catch (final ServletException e) {
			result.include("mensagemCabec", e.getMessage());
			result.include("msgCabecClass", "alert-warning alert-dismissible");		
			result.forwardTo(this).edita((substituicao != null && substituicao.getIdSubstituicao() != null ? substituicao.getIdSubstituicao() : null ));
		} catch (final Exception e) {
			dao().rollbackTransacao();
			result.include("exceptionStack", e);
			throw new AplicacaoException("Não foi possível Gravar", 0, e);			
		}

	}

	/**
	 * Monta a mensagem do Email de informação de substituição enviado de Pessoa para Pessoa
	 * 
	 * @param subst
	 * @return String com a mensagem do Email
	 */
	private String getMensagemPessoaParaPessoa(DpSubstituicao subst){
		
		String mensagem = "da matrícula: #matriculaSubstituto# - #nomeSubstituto# "
				+ "para matricula: #matriculaSubstituido# - #nomeSubstituido# com inicio em #inicioSubstituicao# e término em #terminoSubstituicao#. "
				+ "Neste interstício, #nomeSubstituto#, matrícula: #matriculaSubstituto#, poderá visualizar e assinar documentos destinados ao usuário #nomeSubstituido#, matrícula #matriculaSubstituido#.\n" + 
				"O sistema irá distinguir os atos assinados por cada agente, deixando claro que o subscritor é um substituto e não o destinatário original";
		
		mensagem = mensagem.replace("#matriculaSubstituto#", subst.getSubstituto().getSesbPessoa() + subst.getSubstituto().getMatricula());
		mensagem = mensagem.replace("#nomeSubstituto#", subst.getSubstituto().getNomePessoa());
		mensagem = mensagem.replace("#matriculaSubstituido#", subst.getTitular().getSesbPessoa() + subst.getTitular().getMatricula());
		mensagem = mensagem.replace("#nomeSubstituido#", subst.getTitular().getNomePessoa());
		mensagem = mensagem.replace("#inicioSubstituicao#", subst.getDtIniSubstDDMMYY().toString());
		mensagem = mensagem.replace("#terminoSubstituicao#", subst.getDtFimSubstDDMMYY().toString());

		return mensagem;
	}

	/**
	 * Monta a mensagem do Email de informação de substituição enviado de Pessoa para Pessoa
	 * 
	 * @param subst
	 * @return String com a mensagem do Email
	 */
	private String getMensagemPessoaParaLotacao(DpSubstituicao subst){
		
//		String mensagem = "da matrícula: #matriculaSubstituto# - #nomeSubstituto# "
//				+ "para matricula: #matriculaSubstituido# - #nomeSubstituido# com inicio em #inicioSubstituicao# e término em #terminoSubstituicao#. "
//				+ "Neste interstício, #nomeSubstituto#, matrícula: #matriculaSubstituto#, poderá visualizar e assinar documentos destinados ao usuário #nomeSubstituido#, matrícula #matriculaSubstituido#.\n" + 
//				"O sistema irá distinguir os atos assinados por cada agente, deixando claro que o subscritor é um substituto e não o destinatário original";
//		
//		mensagem = mensagem.replace("#matriculaSubstituto#", subst.getSubstituto().getSesbPessoa() + subst.getSubstituto().getMatricula());
//		mensagem = mensagem.replace("#nomeSubstituto#", subst.getSubstituto().getNomePessoa());
//		mensagem = mensagem.replace("#matriculaSubstituido#", subst.getTitular().getSesbPessoa() + subst.getTitular().getMatricula());
//		mensagem = mensagem.replace("#nomeSubstituido#", subst.getTitular().getNomePessoa());
//		mensagem = mensagem.replace("#inicioSubstituicao#", subst.getDtIniSubstDDMMYY().toString());
//		mensagem = mensagem.replace("#terminoSubstituicao#", subst.getDtFimSubstDDMMYY().toString());
//
//		return mensagem;
		
		// TODO Aguardar Jira
		return "";
	}
	
	/**
	 * Monta a mensagem do Email de informação de substituição enviado de Lotação para Pessoa
	 * 
	 * @param subst
	 * @return String com a mensagem do Email
	 */
	private String getMensagemLotacaoParaPessoa(DpSubstituicao subst){
		
		
		String mensagem = "da matrícula: #matriculaSubstituto# - #nomeSubstituto#, "
				+ "na lotação: #nomeLotacaoSubstituida# "
				+ "com inicio em #inicioSubstituicao# e término em #terminoSubstituicao#. "
				+ "Desta forma, o servidor pode ter acesso e assinar documentos na lotação #nomeLotacaoSubstituida#, "
				+ "como se tivesse uma lotação formal na mesma. Para isso basta acessar o menu substituir. ";
		
		mensagem = mensagem.replace("#matriculaSubstituto#", subst.getSubstituto().getSesbPessoa() + subst.getSubstituto().getMatricula());
		mensagem = mensagem.replace("#nomeSubstituto#", subst.getSubstituto().getNomePessoa());
		mensagem = mensagem.replace("#siglaLotacaoSubstituida#",  subst.getLotaTitular().getSigla());
		mensagem = mensagem.replace("#nomeLotacaoSubstituida#",subst.getLotaTitular().getNomeLotacao());
		mensagem = mensagem.replace("#inicioSubstituicao#", subst.getDtIniSubstDDMMYY().toString());
		mensagem = mensagem.replace("#terminoSubstituicao#", subst.getDtFimSubstDDMMYY().toString());

		return mensagem;
	}

	/**
	 * Monta a mensagem do Email de informação de substituição enviado de Lotacao para Lotacao
	 * 
	 * @param subst
	 * @return String com a mensagem do Email
	 */
	private String getMensagemLotacaoParaLotacao(DpSubstituicao subst){

		String mensagem = "da lotação: #siglaLotacaoSubstituta# - #nomeLotacaoSubstituta# "
				+ "para a lotação: #siglaLotacaoSubstituida# - #nomeLotacaoSubstituida# com inicio em #inicioSubstituicao# e término em #terminoSubstituicao#. "
				+ "Neste interstício, todos os colaboradores com lotação na unidade substituta #siglaLotacaoSubstituta# - #nomeLotacaoSubstituta#, "
				+ "poderão visualizar e assinar documentos destinados à unidade titular  #siglaLotacaoSubstituida# - #nomeLotacaoSubstituida#.\n";
		
		mensagem = mensagem.replace("#siglaLotacaoSubstituta#",subst.getLotaSubstituto().getSigla());
		mensagem = mensagem.replace("#nomeLotacaoSubstituta#", subst.getLotaSubstituto().getNomeLotacao());
		mensagem = mensagem.replace("#siglaLotacaoSubstituida#",  subst.getLotaTitular().getSigla());
		mensagem = mensagem.replace("#nomeLotacaoSubstituida#",subst.getLotaTitular().getNomeLotacao());
		mensagem = mensagem.replace("#inicioSubstituicao#", subst.getDtIniSubstDDMMYY().toString());
		mensagem = mensagem.replace("#terminoSubstituicao#", subst.getDtFimSubstDDMMYY().toString());

		return mensagem;
	}
	
	private String getEmailCabecalho(){
		
		String cabecalho = "Informamos que a matrícula: #matriculaCadastrante# - #nomeCadastrante# cadastrou uma substituição ";
		cabecalho = cabecalho.replace("#matriculaCadastrante#", getCadastrante().getSesbPessoa() + getCadastrante().getMatricula());
		cabecalho = cabecalho.replace("#nomeCadastrante#", getCadastrante().getNomePessoa());

		return cabecalho;
}

	private void enviarEmailSubstituicao(Integer tipoTitular, Integer tipoSubstituto, DpSubstituicao subst)
			throws Exception {
		
		String assunto = "Cadastro de Substituição";
		
		StringBuffer sbTextoEmail = new StringBuffer();

		sbTextoEmail.append(this.getEmailCabecalho());

		switch (tipoTitular) {
			case 1: // DE PESSOA 
				switch (tipoSubstituto) {
					case 1: // PARA PESSOA
						
						sbTextoEmail.append(getMensagemPessoaParaPessoa(subst));
					break;
						
					case 2: // PARA LOTACAO
						
						sbTextoEmail.append(getMensagemPessoaParaLotacao(subst));
					break;
				}
			break;
	
			case 2: // DE LOTACAO
				switch (tipoSubstituto) {
					case 1: // PARA PESSOA
						
						sbTextoEmail.append(getMensagemLotacaoParaPessoa(subst));
					break;
						
					case 2: // PARA LOTACAO
						
						sbTextoEmail.append(getMensagemLotacaoParaLotacao(subst));
					break;
				}
			break;
		}

		sbTextoEmail.append(this.getEmailRodape());

		List<String> listaDeEmails = getDestinatariosEmailSubstituicao(tipoTitular, tipoSubstituto, subst );
		
		Correio.enviar(listaDeEmails.toArray(new String[listaDeEmails.size()]),assunto, sbTextoEmail.toString());
	}


	private List<String> getDestinatariosEmailSubstituicao(Integer tipoTitular, Integer tipoSubstituto,
			DpSubstituicao subst) {
		
		Set<DpPessoa> pessoasParaEnviarEmail = new HashSet<DpPessoa>();
		
		if (tipoSubstituto == 1){
			pessoasParaEnviarEmail.add(subst.getSubstituto());
		}else{
			pessoasParaEnviarEmail.addAll(subst.getLotaSubstituto().getDpPessoaLotadosSet());
		}

		if (tipoTitular == 1) {
			pessoasParaEnviarEmail.add(subst.getTitular());
		} else {
			pessoasParaEnviarEmail.addAll(subst.getLotaTitular().getDpPessoaLotadosSet());
		}

		List<String> listaDeEmails = new ArrayList<>();
		for (DpPessoa pessoa : pessoasParaEnviarEmail){
			listaDeEmails.add(pessoa.getEmailPessoa());
		}
				
//				pessoasParaEnviarEmail.stream().map(DpPessoa :: getEmailPessoa).collect(Collectors.toList());
		listaDeEmails.add(getCadastrante().getEmailPessoa());
		
		return listaDeEmails;
	}

	
	private String getEmailRodape() {

		String rodape = "\n\n Atenção: Esta é uma mensagem automática. Por favor, não responda.";
		return rodape;
	}
	

	@Get("/app/substituicao/finalizar")
	public void finalizar() throws Exception {
		try {
			dao().iniciarTransacao();
			gravarFinalizar();
			dao().commitTransacao();
		} catch (Exception e) {
			dao().rollbackTransacao();
		}
		String referer = getRequest().getHeader("Referer");
		if (referer != null)
			result.redirectTo(referer);
		else
			result.redirectTo(PrincipalController.class).principal(false, false);
	}	
	
	private void gravarFinalizar() {
		CpPersonalizacao per = dao().consultarPersonalizacao(getCadastrante());

		if (per == null) {
			per = new CpPersonalizacao();
			per.setPessoa(getCadastrante());
		}

		per.setPesSubstituindo(null);
		per.setLotaSubstituindo(null);
		dao().gravar(per);
	}
	
	@Get("/app/substituicao/substituirGravar")
	public void substituirGravar(Long id) throws Exception {
		try {
			dao().iniciarTransacao();
			gravarFinalizar();
			
			if (id == null)
				throw new ServletException("Dados não informados");
			
			DpSubstituicao dpSub = dao().consultar(id, DpSubstituicao.class, false);
			
			if ((dpSub.getSubstituto() == null && !dpSub.getLotaSubstituto().equivale(getLotaCadastrante()) 
				|| (dpSub.getSubstituto() != null && !dpSub.getSubstituto().equivale(getCadastrante()))))
				throw new ServletException("Substituição não permitida");
			
			setLotaTitular(dpSub.getLotaTitular());
			setTitular(dpSub.getTitular());
	
			CpPersonalizacao per = dao().consultarPersonalizacao(getCadastrante());
			if (per == null) {
				per = new CpPersonalizacao();
			}
			per.setPessoa(getCadastrante());
			per.setPesSubstituindo(getTitular() != getCadastrante() ? getTitular(): null);
			per.setLotaSubstituindo(getLotaTitular() != getCadastrante().getLotacao() ? getLotaTitular() : null);
		
			dao().gravar(per);
			dao().commitTransacao();
//			result.use(Results.referer()).redirect();
		} catch (Exception e) {
			e.printStackTrace();
			dao().rollbackTransacao();
		}
		String referer = getRequest().getHeader("Referer");
		if (referer != null)
			result.redirectTo(referer);
		else
			result.redirectTo(PrincipalController.class).principal(false,false);
	}	
	
	public void exclui(Long id) throws Exception {
		
		try{
		
		if (id != null) {
			DpSubstituicao dpSub = dao().consultar(id, DpSubstituicao.class, false);
			
			if ((dpSub.getSubstituto() != null && dpSub.getSubstituto().equivale(getCadastrante()))					
				|| (dpSub.getSubstituto() == null && dpSub.getLotaSubstituto().equivale(getCadastrante().getLotacao()))
				||(dpSub.getTitular() != null && dpSub.getTitular().equivale(getCadastrante()))					
				|| (dpSub.getTitular() == null && dpSub.getLotaTitular().equivale(getCadastrante().getLotacao()))
				|| podeCadastrarQualquerSubstituicao()) {
				dao().iniciarTransacao();		
				dpSub.setDtFimRegistro(new Date());
				dpSub = dao().gravar(dpSub);
				dao().commitTransacao();
				String referer = getRequest().getHeader("Referer");
				if (referer != null)
					result.redirectTo(referer);
				else
					result.redirectTo(PrincipalController.class).principal(false,false);
			} else
				throw new ServletException("Usuário não tem permissão para excluir esta substituição");	
		} else
			throw new ServletException("Não foi informado id");
		
		} catch (final ServletException e) {
			result.include("mensagemCabec", e.getMessage());
			result.include("msgCabecClass", "alert-warning alert-dismissible");		
			result.forwardTo(this).lista();
		}catch (Exception e) {
			result.include("exceptionStack", e);
			throw new AplicacaoException("Não foi possível Excluir", 0, e);
		}
		
	}	
	
	private long getIntervaloDeDiasEntreDatas(Date firstDate, Date secondDate) {
	 
	    long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
	    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		return diff;
	}

}