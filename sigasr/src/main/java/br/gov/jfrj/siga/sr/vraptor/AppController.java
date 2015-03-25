package br.gov.jfrj.siga.sr.vraptor;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.joda.time.LocalDate;

import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.interceptor.download.ByteArrayDownload;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.SrCorreio;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrAcordo;
import br.gov.jfrj.siga.sr.model.SrArquivo;
import br.gov.jfrj.siga.sr.model.SrAtributo;
import br.gov.jfrj.siga.sr.model.SrAtributoSolicitacao;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrConfiguracaoBL;
import br.gov.jfrj.siga.sr.model.SrEquipe;
import br.gov.jfrj.siga.sr.model.SrGravidade;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;
import br.gov.jfrj.siga.sr.model.SrLista;
import br.gov.jfrj.siga.sr.model.SrMovimentacao;
import br.gov.jfrj.siga.sr.model.SrObjetivoAtributo;
import br.gov.jfrj.siga.sr.model.SrPesquisa;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.model.SrTipoAtributo;
import br.gov.jfrj.siga.sr.model.SrTipoMotivoEscalonamento;
import br.gov.jfrj.siga.sr.model.SrTipoMotivoPendencia;
import br.gov.jfrj.siga.sr.model.SrTipoMovimentacao;
import br.gov.jfrj.siga.sr.model.SrTipoPergunta;
import br.gov.jfrj.siga.sr.model.SrTipoPermissaoLista;
import br.gov.jfrj.siga.sr.model.SrUrgencia;
import br.gov.jfrj.siga.sr.util.SrSolicitacaoAtendidos;
import br.gov.jfrj.siga.sr.util.SrSolicitacaoFiltro;
import br.gov.jfrj.siga.sr.util.SrSolicitacaoItem;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
public class AppController extends SrController {

	private SrCorreio correio;
	private Validator validation;

	public AppController(HttpServletRequest request, Result result, 
			SigaObjects so, EntityManager em, SrCorreio correio, Validator validation) throws Exception{
		super(request, result, so, em);
		this.correio = correio;
		this.validation = validation;
		
		SrConfiguracaoBL.get().limparCacheSeNecessario();
		
		try {
			assertAcesso("ADM:Administrar");
			result.include("exibirMenuAdministrar", true);
		} catch (Exception e) {
			result.include("exibirMenuAdministrar", false);
		}
		
		try {
			assertAcesso("EDTCONH:Criar Conhecimentos");
			result.include("exibirMenuConhecimentos", true);
		} catch (Exception e) {
			result.include("exibirMenuConhecimentos", false);
		}

		try {
			assertAcesso("REL:Relatorios");
			result.include("exibirMenuRelatorios", true);
		} catch (Exception e) {
			result.include("exibirMenuRelatorios", false);
		}

	}

	private  final String HTTP_LOCALHOST_8080 = "http://localhost:8080";

	public boolean ocultas() {
		return Boolean.parseBoolean(request.getParameter("ocultas"));
	}
	
	public  boolean todoOContexto() {
		return Boolean.parseBoolean(request.getParameter("todoOContexto"));
	}
	
	public void index() throws Exception {
		result.redirectTo(this).editar(null);
	}

	public void gadget() {
		Query query = em().createNamedQuery("contarSrMarcas");
		query.setParameter("idPessoaIni", getTitular().getIdInicial());
		query.setParameter("idLotacaoIni", getLotaTitular().getIdInicial());
		List contagens = query.getResultList();
		result.include("contagens", contagens);
	}

	public void editar(Long id) throws Exception {
		
		SrSolicitacao solicitacao;
		if (id == null) {
			solicitacao = new SrSolicitacao();
			solicitacao.setSolicitante(getTitular());
		} else
			solicitacao = SrSolicitacao.AR.findById(id);
		
		if (solicitacao.getDtOrigem() == null)
			solicitacao.setDtOrigem(new Date());
		if (solicitacao.getDtIniEdicao() == null)
			solicitacao.setDtIniEdicao(new Date());
		solicitacao.atualizarAcordos();

		formEditar(solicitacao.deduzirLocalRamalEMeioContato());
	}

	@SuppressWarnings("unchecked")
	public void exibirLocalRamalEMeioContato(SrSolicitacao solicitacao)
			throws Exception {
		solicitacao.deduzirLocalRamalEMeioContato();
		result.include("solicitacao", solicitacao);
	}
	
	public void listarAcoesSlugify(){
		String acoes = "";
		for (Object o : SrAcao.listar(false)){
			SrAcao a = (SrAcao) o;
			acoes += a.getSiglaAcao() + "&nbsp;&nbsp;" + (a.getGcTags()) + "<br/>";
		}
		
		String itens = "";
		for (Object o : SrItemConfiguracao.listar(false)){
			SrItemConfiguracao a = (SrItemConfiguracao) o;
			itens += a.getSiglaItemConfiguracao() + "&nbsp;&nbsp;" + (a.getGcTags()) + "<br/>";
		}
		result.include("acoes", acoes);
		result.include("itens", itens);
	}
	
	public void listarSolicitacoesRelacionadas(SrSolicitacaoFiltro solicitacao, HashMap<Long, String> atributoSolicitacaoMap) 
			throws Exception{
		
		solicitacao.setAtributoSolicitacaoMap(atributoSolicitacaoMap);
		List<Object[]> solicitacoesRelacionadas = solicitacao.buscarSimplificado();
		result.include("solicitacoesRelacionadas", solicitacoesRelacionadas);
	}

	public void exibirAtributos(SrSolicitacao solicitacao) throws Exception {
		result.include("solicitacao", solicitacao);
	}

	public void exibirAtributosConsulta(SrSolicitacaoFiltro filtro) throws Exception {
		List<SrAtributo> atributosDisponiveisAdicao = atributosDisponiveisAdicaoConsulta(filtro);
		result.include("filtro", filtro);
		result.include("atributosDisponiveisAdicao", atributosDisponiveisAdicao);
	}

	public List<SrAtributo> atributosDisponiveisAdicaoConsulta(SrSolicitacaoFiltro filtro) throws Exception{
		List<SrAtributo> listaAtributosAdicao = new ArrayList<SrAtributo>();
		HashMap<Long, String> atributoMap = filtro.getAtributoSolicitacaoMap();
		
		for (SrAtributo srAtributo : SrAtributo.listarParaSolicitacao(Boolean.FALSE)) {
			if (!atributoMap.containsKey(srAtributo.getIdAtributo())) {
				listaAtributosAdicao.add(srAtributo);
			}
		}
		return listaAtributosAdicao;
	}
	
	public void exibirItemConfiguracao(SrSolicitacao solicitacao)
			throws Exception {
		if (solicitacao.getSolicitante() == null)
			result.include("solicitacao", solicitacao);
		
		if (solicitacao.getItensDisponiveis().contains(
				solicitacao.getItemConfiguracao()))
			solicitacao.setItemConfiguracao(null);

		DpPessoa cadastrante = solicitacao.getCadastrante();
		DpLotacao lotaTitular = solicitacao.getLotaCadastrante();
		Map<SrAcao, DpLotacao> acoesEAtendentes = solicitacao.getAcoesEAtendentes();
		result.include("solicitacao", solicitacao);
		result.include("acoesEAtendentes", acoesEAtendentes);
	}

	public void exibirAcao(SrSolicitacao solicitacao) throws Exception {
		Map<SrAcao, DpLotacao> acoesEAtendentes = solicitacao.getAcoesEAtendentes();
		result.include("solicitacao", solicitacao);
		result.include("acoesEAtendentes", acoesEAtendentes);
	}
	
	public void exibirAcaoEscalonar(Long id, Long itemConfiguracao) throws Exception {
		SrSolicitacao solicitacao = SrSolicitacao.AR.findById(id);
		solicitacao.setCadastrante( getCadastrante());
		solicitacao.setLotaCadastrante( getLotaTitular());
		Map<SrAcao, DpLotacao> acoesEAtendentes = new TreeMap<SrAcao, DpLotacao>();
		if (itemConfiguracao != null){
			solicitacao.setItemConfiguracao(SrItemConfiguracao.AR.findById(itemConfiguracao));
			acoesEAtendentes = solicitacao.getAcoesEAtendentes();
		}
		result.include("solicitacao", solicitacao);
		result.include("acoesEAtendentes", acoesEAtendentes);
	}

	public void exibirConhecimentosRelacionados(SrSolicitacao solicitacao)
			throws Exception {
		result.include("solicitacao", solicitacao);
	}

	@SuppressWarnings("unchecked")
	private void formEditar(SrSolicitacao solicitacao) throws Exception {
		
		List<CpComplexo> locais = em().createQuery("from CpComplexo")
				.getResultList();
		
		Map<SrAcao, DpLotacao> acoesEAtendentes = solicitacao.getAcoesEAtendentes();
		result.include("solicitacao", solicitacao);
		result.include("acoesEAtendentes", acoesEAtendentes);
		result.include("locais", locais);
	}

	@SuppressWarnings("-access")
	private void validarFormEditar(SrSolicitacao solicitacao)
			throws Exception {

		if (solicitacao.getSolicitante() == null) {
			validation.add(new ValidationMessage("solicitacao.solicitante",
					"Solicitante n&atilde;o informado"));
		}
		if (solicitacao.getItemConfiguracao() == null) {
			validation.add(new ValidationMessage("solicitacao.itemConfiguracao",
					"Item n&atilde;o informado"));
		}
		if (solicitacao.getAcao() == null) {
			validation.add(new ValidationMessage("solicitacao.acao", "A&ccedil&atilde;o n&atilde;o informada"));
		}

		if (solicitacao.getDescrSolicitacao() == null
				|| solicitacao.getDescrSolicitacao().trim().equals("")) {
			validation.add(new ValidationMessage("solicitacao.descrSolicitacao",
					"Descri&ccedil&atilde;o n&atilde;o informada"));
		}
		
		HashMap<Long, Boolean> obrigatorio = solicitacao.getObrigatoriedadeTiposAtributoAssociados();
		for (SrAtributoSolicitacao att : solicitacao.getAtributoSolicitacaoSet()) {
			// Para evitar NullPointerExcetpion quando nao encontrar no Map
			if(Boolean.TRUE.equals(obrigatorio.get(att.getAtributo().getIdAtributo()))) {
				if ((att.getValorAtributoSolicitacao() == null || att.getValorAtributoSolicitacao().trim().equals("")))
					validation.add(new ValidationMessage("solicitacao.atributoSolicitacaoMap["
							+ att.getAtributo().getIdAtributo() + "]",
							att.getAtributo().getNomeAtributo() + " n&atilde;o informado"));
			}
		}

		if (validation.hasErrors()) {
			formEditar(solicitacao);
		}
	}

	private  void validarFormEditarItem(
			SrItemConfiguracao itemConfiguracao) throws Exception {

		if (itemConfiguracao.getSiglaItemConfiguracao().equals("")) {
			validation.add(new ValidationMessage("itemConfiguracao.siglaItemConfiguracao",
					"C&oacute;digo n&atilde;o informado"));
		}

		if (itemConfiguracao.getTituloItemConfiguracao().equals("")) {
			validation.add(new ValidationMessage("itemConfiguracao.tituloItemConfiguracao",
					"T&iacute;tulo n&atilde;o informado"));
		}

		if (itemConfiguracao.getNumFatorMultiplicacaoGeral() < 1 ) {
			validation.add(new ValidationMessage("itemConfiguracao.numFatorMultiplicacaoGeral",
					"Fator de multiplica&ccedil;&atilde;o menor que 1"));
		}
		
		for (Message error : validation.getErrors()) {
			System.out.println(error.getMessage());
		}

		if (validation.hasErrors()) {
			//render("@editarItem", itemConfiguracao);
			result.include("itemConfiguracao", itemConfiguracao);
		}
	}

	private  void validarFormEditarAcao(SrAcao acao) {

		if (acao.getSiglaAcao().equals("")) {
			validation.add(new ValidationMessage("acao.siglaAcao", "Código não informado"));
		}

		if (acao.getTituloAcao().equals("")) {
			validation.add(new ValidationMessage("acao.tituloAcao", "Título não informado"));
		}

		if (validation.hasErrors()) {
			//render("@editarAcao", acao);
			result.include("acao", acao);
		}

	}

	@SuppressWarnings("-access")
	private  void validarFormEditarDesignacao(SrConfiguracao designacao) throws Exception {
		StringBuffer sb = new StringBuffer();
		
		if ((designacao.getAtendente() == null) && (designacao.getPreAtendente() == null)
				&& (designacao.getPosAtendente() == null)
				&& (designacao.getEquipeQualidade() == null)
				&& (designacao.getPesquisaSatisfacao() == null)) {
			validation.add(new ValidationMessage("designacao.atendente",
					"Atendente não informado."));
			validation.add(new ValidationMessage("designacao.preAtendente",
					"Pr&eacute;-atendente n&atilde;o informado."));
			validation.add(new ValidationMessage("designacao.posAtendente",
					"P&oacute;s-atendente n&atilde;o informado."));
			validation.add(new ValidationMessage("designacao.equipeQualidade",
					"Equipe de qualidade n&atilde;o informada."));
		}

		for (Message error : validation.getErrors()) {
			System.out.println(error.getMessage());
			sb.append(error.getCategory() + ";");
		}

		if (validation.hasErrors()) {
			throw new Exception(sb.toString());
		}
	}

	private  void validarFormEditarPermissaoUsoLista(
			SrConfiguracao designacao) {
	}

	public void gravar(SrSolicitacao solicitacao) throws Exception {

        if(!solicitacao.isRascunho())
        	validarFormEditar(solicitacao);
        
		solicitacao.salvar(getCadastrante(), getLotaTitular());
		Long id = solicitacao.getIdSolicitacao();
		result.redirectTo(this).exibir(id, todoOContexto(), ocultas());
	}
	
	public void juntar(Long idSolicitacaoAJuntar, Long idSolicitacaoRecebeJuntada, String justificativa) throws Exception {
		SrSolicitacao sol = SrSolicitacao.AR.findById(idSolicitacaoAJuntar);
		SrSolicitacao solRecebeJuntada = SrSolicitacao.AR.findById(idSolicitacaoRecebeJuntada);
		sol.juntar(getLotaTitular(), getCadastrante(), solRecebeJuntada, justificativa);
		result.redirectTo(this).exibir(idSolicitacaoAJuntar, todoOContexto(), ocultas());
	}
	
    public void vincular(Long idSolicitacaoAVincular, Long idSolicitacaoRecebeVinculo, String justificativa) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(idSolicitacaoAVincular);
        SrSolicitacao solRecebeVinculo = SrSolicitacao.AR.findById(idSolicitacaoRecebeVinculo);
        sol.vincular(getLotaTitular(), getCadastrante(), solRecebeVinculo, justificativa);
        result.redirectTo(this).exibir(idSolicitacaoAVincular, todoOContexto(), ocultas());
    }
    
    public void desentranhar(Long id, String justificativa) throws Exception {
		SrSolicitacao sol = SrSolicitacao.AR.findById(id);
		sol.desentranhar(getLotaTitular(), getCadastrante(), justificativa);
		result.redirectTo(this).exibir(id, todoOContexto(), ocultas());
	}
	
	public void estatistica() throws Exception {
		assertAcesso("REL:Relatorios");
		List<SrSolicitacao> lista = SrSolicitacao.AR.all().fetch();

		List<String[]> listaSols = SrSolicitacao
				.AR.find("select sol.gravidade, count(distinct sol.idSolicitacao) "
						+ "from SrSolicitacao sol, SrMovimentacao movimentacao "
						+ "where sol.idSolicitacao = movimentacao.solicitacao and movimentacao.tipoMov <> 7 "
						+ "and movimentacao.lotaAtendente = "
						+ getLotaTitular().getIdLotacao()
						+ " "
						+ "group by sol.gravidade").fetch();

		LocalDate ld = new LocalDate();
		ld = new LocalDate(ld.getYear(), ld.getMonthOfYear(), 1);

		// Header
		StringBuilder sb = new StringBuilder();
		sb.append("['Prioridade','Total'],");

		// Values
		for (Iterator<String[]> it = listaSols.iterator(); it.hasNext();) {
			Object[] obj = (Object[]) it.next();
			SrGravidade gravidade = (SrGravidade) obj[0];
			Long total = (Long) obj[1];
			sb.append("['");
			sb.append(gravidade.getDescrGravidade().toString());
			sb.append("',");
			sb.append(total.toString());
			sb.append(",");
			sb.append("],");
		}

		String estat = sb.toString();

		List<SrSolicitacao> listaEvol = SrSolicitacao.AR.all().fetch();

		SrSolicitacaoAtendidos set = new SrSolicitacaoAtendidos();
		List<Object[]> listaEvolSols = SrSolicitacao
				.AR.find("select extract (month from sol.dtReg), extract (year from sol.dtReg), count(distinct sol.idSolicitacao) "
						+ "from SrSolicitacao sol, SrMovimentacao movimentacao "
						+ "where sol.idSolicitacao = movimentacao.solicitacao "
						+ "and movimentacao.lotaAtendente = "
						+ getLotaTitular().getIdLotacao()
						+ " "
						+ "group by extract (month from sol.dtReg), extract (year from sol.dtReg)")
				.fetch();

		for (Object[] sols : listaEvolSols) {
			set.add(new SrSolicitacaoItem((Integer) sols[0], (Integer) sols[1],
					(Long) sols[2], 0, 0));
		}

		List<Object[]> listaFechados = SrSolicitacao
				.AR.find("select extract (month from sol.dtReg), extract (year from sol.dtReg), count(distinct sol.idSolicitacao) "
						+ "from SrSolicitacao sol, SrMovimentacao movimentacao "
						+ "where sol.idSolicitacao = movimentacao.solicitacao "
						+ "and movimentacao.tipoMov = 7 "
						+ "and movimentacao.lotaAtendente = "
						+ getLotaTitular().getId()
						+ " "
						+ "group by extract (month from sol.dtReg), extract (year from sol.dtReg)")
				.fetch();
		for (Object[] fechados : listaFechados) {
			set.add(new SrSolicitacaoItem((Integer) fechados[0],
					(Integer) fechados[1], 0, (Long) fechados[2], 0));
		}

		LocalDate ldt = new LocalDate();
		ldt = new LocalDate(ldt.getYear(), ldt.getMonthOfYear(), 1);

		// Header
		StringBuilder sbevol = new StringBuilder();
		sbevol.append("['Mês','Fechados','Abertos'],");

		// Values
		for (int i = -6; i <= 0; i++) {
			LocalDate ldl = ldt.plusMonths(i);
			sbevol.append("['");
			sbevol.append(ldl.toString("MMM/yy"));
			sbevol.append("',");
			long abertos = 0;
			long fechados = 0;
			SrSolicitacaoItem o = new SrSolicitacaoItem(ldl.getMonthOfYear(),
					ldl.getYear(), 0, 0, 0);
			if (set.contains(o)) {
				o = set.floor(o);
				abertos = o.abertos;
				fechados = o.fechados;
			}
			sbevol.append(fechados);
			sbevol.append(",");
			sbevol.append(abertos);
			sbevol.append(",");
			sbevol.append("],");
		}
		String evolucao = sbevol.toString();

		List<SrSolicitacao> top = SrSolicitacao.AR.all().fetch();

		List<String[]> listaTop = SrSolicitacao
				.AR.find("select sol.itemConfiguracao.tituloItemConfiguracao, count(distinct sol.idSolicitacao) "
						+ "from SrSolicitacao sol, SrMovimentacao movimentacao "
						+ "where sol.idSolicitacao = movimentacao.solicitacao "
						+ "and movimentacao.lotaAtendente = "
						+ getLotaTitular().getIdLotacao()
						+ " "
						+ "group by sol.itemConfiguracao.tituloItemConfiguracao")
				.fetch();

		// Header
		StringBuilder sbtop = new StringBuilder();
		sbtop.append("['Item de Configura&ccedil;&atilde;o','Total'],");

		// Values
		for (Iterator<String[]> itop = listaTop.iterator(); itop.hasNext();) {
			Object[] obj = (Object[]) itop.next();
			String itensconf = (String) obj[0];
			Long total = (Long) obj[1];
			sbtop.append("['");
			sbtop.append(itensconf);
			sbtop.append("',");
			sbtop.append(total.toString());
			sbtop.append(",");
			sbtop.append("],");
		}

		String top10 = sbtop.toString();

		List<SrSolicitacao> lstgut = SrSolicitacao.AR.all().fetch();

		List<String[]> listaGUT = SrSolicitacao
				.AR.find("select sol.gravidade, sol.urgencia, count(distinct sol.idSolicitacao) "
						+ "from SrSolicitacao sol, SrMovimentacao movimentacao "
						+ "where sol.idSolicitacao = movimentacao.solicitacao "
						+ "and movimentacao.lotaAtendente = "
						+ getLotaTitular().getIdLotacao()
						+ " "
						+ "group by sol.gravidade, sol.urgencia").fetch();

		// Header
		StringBuilder sbGUT = new StringBuilder();
		sbGUT.append("['Gravidade','Urgência','Total'],");

		// Values
		for (Iterator<String[]> itgut = listaGUT.iterator(); itgut.hasNext();) {
			Object[] obj = (Object[]) itgut.next();
			SrGravidade gravidade = (SrGravidade) obj[0];
			SrUrgencia urgencia = (SrUrgencia) obj[1];
			Long total = (Long) obj[2];
			sbGUT.append("['");
			sbGUT.append(gravidade.getDescrGravidade().toString());
			sbGUT.append("','");
			sbGUT.append(urgencia.getDescrUrgencia().toString());
			sbGUT.append("',");
			sbGUT.append(total.toString());
			sbGUT.append(",");
			sbGUT.append("],");
		}

		String gut = sbGUT.toString();
		
		result.include("lista", lista);
		result.include("evolucao", evolucao);
		result.include("top10", top10);
	}

	public void exibir(Long id, Boolean todoOContexto, Boolean ocultas) throws Exception {
		SrSolicitacao solicitacao = SrSolicitacao.AR.findById(id);
		if (solicitacao == null)
			throw new Exception("Solicitação não encontrada");
		else
			solicitacao = solicitacao.getSolicitacaoAtual();
		
		if (solicitacao == null)
			throw new Exception("Esta solicitação foi excluída");
		
		SrMovimentacao movimentacao = new SrMovimentacao(solicitacao);

		if (todoOContexto == null)
			todoOContexto = solicitacao.isParteDeArvore();
			//Edson: foi solicitado que funcionasse do modo abaixo. Eh o melhor modo??
			//todoOContexto = solicitacao.solicitacaoPai == null ? true : false;
		if (ocultas == null)
			ocultas = false;
	
		Set<SrMovimentacao> movs = solicitacao.getMovimentacaoSet(ocultas,
				null, false, todoOContexto, !ocultas, false);

		result.include("solicitacao", solicitacao);
		result.include("movimentacao", movimentacao);
		result.include("todoOContexto", todoOContexto);
		result.include("ocultas", ocultas);
		result.include("movs", movs);
	}

	public void exibirLista(Long id) throws Exception {
		SrLista lista = SrLista.AR.findById(id);
		lista.validarPodeExibirLista(getLotaTitular(), getCadastrante());
		result.include("lista", lista);
	}
	
	public void incluirEmLista(Long idSolicitacao) throws Exception {
		SrSolicitacao solicitacao = SrSolicitacao.AR.findById(idSolicitacao);
		solicitacao = solicitacao.getSolicitacaoAtual();
		result.include("solicitacao", solicitacao);
	}

	public void incluirEmListaGravar(Long idSolicitacao, Long idLista)
			throws Exception {
		SrSolicitacao solicitacao = SrSolicitacao.AR.findById(idSolicitacao);
		SrLista lista = SrLista.AR.findById(idLista);
		solicitacao.incluirEmLista(lista, getCadastrante(), getLotaTitular());
		result.redirectTo(this).exibir(idSolicitacao, todoOContexto(), ocultas());
	}

	public void retirarDeLista(Long idSolicitacao, Long idLista)
			throws Exception {
			SrSolicitacao solicitacao = SrSolicitacao.AR.findById(idSolicitacao);
			SrLista lista = SrLista.AR.findById(idLista);
			solicitacao.retirarDeLista(lista, getCadastrante(), getLotaTitular());
			result.redirectTo(this).exibirLista(idLista);
	}
	
	/*public void priorizarLista(@As(",") List<Long> ids, Long id)
			throws Exception {

		// Edson: as 3 linhas abaixo nao deveriam estar sendo necessarias, mas o
		// Play
		// nao estah fazendo o binding direito caso o parametro seja
		// List<SrSolicitacao>
		// em vez de List<Long>. Ver o que estah havendo.
		List<SrSolicitacao> sols = new ArrayList<SrSolicitacao>();
		for (Long l : ids)
			sols.add((SrSolicitacao) SrSolicitacao.AR.findById(l));

		SrLista lista = SrLista.AR.findById(id);
		lista.priorizar(getCadastrante(), getLotaTitular(), sols);
		exibirLista(id);
	}*/

	public void selecionarSolicitacao(String sigla) throws Exception {
		SrSolicitacao sel = new SrSolicitacao();
		sel.setCadastrante(getCadastrante());
		sel.setLotaCadastrante(getLotaTitular());
		sel = (SrSolicitacao) sel.selecionar(sigla);
		result.include("sel", sel);
		//render("@selecionar", sel);
	}
	
	//	DB1: foi necessário receber e passar o parametro "nome"(igual ao buscarItem())
	//	para chamar a function javascript correta,
	//	e o parametro "popup" porque este metodo é usado também na lista,
	//	e não foi possível deixar default no template(igual ao buscarItem.html) 
	@SuppressWarnings("unchecked")
	public void buscarSolicitacao(SrSolicitacaoFiltro filtro, String nome, boolean popup) throws Exception{

		List<SrSolicitacao> listaSolicitacao = new ArrayList<SrSolicitacao>();

		try {
			if (filtro.pesquisar) {
				listaSolicitacao = filtro.buscar();
			} else {
				listaSolicitacao = new ArrayList<SrSolicitacao>();
			}
		} catch (Exception e) {
			e.printStackTrace();
			listaSolicitacao = new ArrayList<SrSolicitacao>();
		}
		
		// Montando o filtro...
		String[] tipos = new String[] { "Pessoa", "Lotação" };
		List<CpMarcador> marcadores = em()
				.createQuery("select distinct cpMarcador from SrMarca")
				.getResultList();

		List<SrAtributo> atributosDisponiveisAdicao = atributosDisponiveisAdicaoConsulta(filtro);
		result.include("listaSolicitacao", listaSolicitacao);
		result.include("tipos", tipos);
		result.include("marcadores", marcadores);
		result.include("filtro", filtro);
		result.include("nome", nome);
		result.include("popup", popup);
		result.include("atributosDisponiveisAdicao", atributosDisponiveisAdicao);
	}

	public Download baixar(Long idArquivo) throws Exception{
		SrArquivo arq = SrArquivo.AR.findById(idArquivo);
		if (arq != null)
			return new ByteArrayDownload(arq.getBlob(), arq.getMime(),
					arq.getNomeArquivo(), true);
		throw new Exception("Arquivo não encontrado.");
	}

	public void darAndamento(SrMovimentacao movimentacao)
			throws Exception {
		movimentacao.setTipoMov(SrTipoMovimentacao
				.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO));
		movimentacao.salvar(getCadastrante(), getLotaTitular());
		result.redirectTo(this).exibir(movimentacao.getSolicitacao().getIdSolicitacao(), todoOContexto(), ocultas());
	}

	public void anexarArquivo(SrMovimentacao movimentacao)
			throws Exception {
		movimentacao.salvar(getCadastrante(), getLotaTitular());
		result.redirectTo(this).exibir(movimentacao.getSolicitacao().getIdSolicitacao(), todoOContexto(), ocultas());
	}

	public void fechar(Long id, String motivo) throws Exception {
		SrSolicitacao sol = SrSolicitacao.AR.findById(id);
		sol.fechar(getLotaTitular(), getCadastrante(), motivo);
		result.redirectTo(this).exibir(sol.getIdSolicitacao(), todoOContexto(), ocultas());
	}

	public void excluir(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.AR.findById(id);
		sol.excluir();
		result.redirectTo(this).editar(null);
	}
	
	public void responderPesquisa(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.AR.findById(id);
		SrPesquisa pesquisa = sol.getPesquisaDesignada();
		if (pesquisa == null)
			throw new Exception(
					"Nao foi encontrada nenhuma pesquisa designada para esta solicitacao.");
		pesquisa = SrPesquisa.AR.findById(pesquisa.getIdPesquisa());
		pesquisa = pesquisa.getPesquisaAtual();
		result.include("id", id);
		result.include("pesquisa", pesquisa);
	}
	
	public void responderPesquisaGravar(Long id,
			Map<Long, String> respostaMap) throws Exception {
		SrSolicitacao sol = SrSolicitacao.AR.findById(id);
		sol.responderPesquisa(getLotaTitular(), getCadastrante(), respostaMap);
		exibir(id, todoOContexto(), ocultas());
	}

	public void retornarAoAtendimento(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.AR.findById(id);
		sol.retornarAoAtendimento(getLotaTitular(), getCadastrante());
		result.redirectTo(this).exibir(id, todoOContexto(), ocultas());
	}

	public void termoAtendimento(Long id) throws Exception {
		SrSolicitacao solicitacao = SrSolicitacao.AR.findById(id);
		result.include("solicitacao", solicitacao);
	}

	public void cancelar(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.AR.findById(id);
		sol.cancelar(getLotaTitular(), getCadastrante());
		result.redirectTo(this).exibir(id, todoOContexto(), ocultas());
	}

	public void finalizarPreAtendimento(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.AR.findById(id);
		sol.finalizarPreAtendimento(getLotaTitular(), getCadastrante());
		result.redirectTo(this).exibir(sol.getIdSolicitacao(), todoOContexto(), ocultas());
	}

	public void retornarAoPreAtendimento(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.AR.findById(id);
		sol.retornarAoPreAtendimento(getLotaTitular(), getCadastrante());
		result.redirectTo(this).exibir(sol.getIdSolicitacao(), todoOContexto(), ocultas());
	}

	public void deixarPendente(Long id, SrTipoMotivoPendencia motivo,String calendario,
			String horario, String detalheMotivo) throws Exception {
		SrSolicitacao sol = SrSolicitacao.AR.findById(id);
		sol.deixarPendente(getLotaTitular(), getCadastrante(), motivo, calendario,
				horario, detalheMotivo);
		result.redirectTo(this).exibir(id, todoOContexto(), ocultas());
	}

	public void alterarPrazo(Long id, String motivo,
			String calendario, String horario) throws Exception {
			SrSolicitacao sol = SrSolicitacao.AR.findById(id);
			sol.alterarPrazo(getLotaTitular(), getCadastrante(), motivo, calendario,
					horario);
			result.redirectTo(this).exibir(id, todoOContexto(), ocultas());
	}

	public void terminarPendencia(Long id, String descricao, Long idMovimentacao) throws Exception {
		SrSolicitacao sol = SrSolicitacao.AR.findById(id);
		sol.terminarPendencia(getLotaTitular(), getCadastrante(), descricao, idMovimentacao);
		result.redirectTo(this).exibir(id, todoOContexto(), ocultas());
	}

	public void reabrir(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.AR.findById(id);
		sol.reabrir(getLotaTitular(), getCadastrante());
		result.redirectTo(this).exibir(id, todoOContexto(), ocultas());
	}

	public void desfazerUltimaMovimentacao(Long id) throws Exception {
		SrSolicitacao sol = SrSolicitacao.AR.findById(id);
		sol.desfazerUltimaMovimentacao(getCadastrante(), getLotaTitular());
		result.redirectTo(this).exibir(id, todoOContexto(), ocultas());
	}

	public void escalonar(Long id) throws Exception {
		SrSolicitacao solicitacao = SrSolicitacao.AR.findById(id);
		solicitacao.setCadastrante( getCadastrante());
		solicitacao.setLotaCadastrante( getLotaTitular());
		solicitacao = solicitacao.getSolicitacaoAtual();
		Map<SrAcao, DpLotacao> acoesEAtendentes = solicitacao.getAcoesEAtendentes();
		result.include("acoesEAtendentes", acoesEAtendentes);
		result.include("solicitacao", solicitacao);
	}
	
	public void escalonarGravar(Long id, Long itemConfiguracao,
				SrAcao acao, Long idAtendente, Long idAtendenteNaoDesignado, 
				SrTipoMotivoEscalonamento motivo, String descricao,
				Boolean criaFilha, Boolean fechadoAuto) throws Exception {
		if(itemConfiguracao == null || acao == null)
			throw new Exception("Operação não permitida. Necessário informar um item de configuração " + 
					"e uma ação.");
		SrSolicitacao solicitacao = SrSolicitacao.AR.findById(id);
		if (criaFilha) {
			if (fechadoAuto != null) {
				solicitacao.setFechadoAutomaticamente(fechadoAuto);
				solicitacao.save();
			}
			SrSolicitacao filha = null;
			if(solicitacao.isFilha())
				filha = solicitacao.getSolicitacaoPai().criarFilhaSemSalvar();
			else
				filha = solicitacao.criarFilhaSemSalvar();
			filha.setItemConfiguracao(SrItemConfiguracao.AR.findById(itemConfiguracao));
			filha.setAcao(SrAcao.AR.findById(acao.getIdAcao()));
			filha.setDescrSolicitacao(descricao);
			if (idAtendenteNaoDesignado != null)
				filha.setAtendenteNaoDesignado(em().find(DpLotacao.class, idAtendenteNaoDesignado));
			filha.salvar(getCadastrante(), getLotaTitular());
			result.redirectTo(this).exibir(filha.getIdSolicitacao(), todoOContexto(), ocultas());
		}
		else {
			SrMovimentacao mov = new SrMovimentacao(solicitacao);
			mov.setTipoMov(SrTipoMovimentacao
					.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ESCALONAMENTO));
			mov.setItemConfiguracao(SrItemConfiguracao.AR.findById(itemConfiguracao));
			mov.setAcao(SrAcao.AR.findById(acao.getIdAcao()));
			mov.setLotaAtendente(em().find(DpLotacao.class, idAtendente));
			mov.setDescrMovimentacao("Item: " + mov.getItemConfiguracao().getTituloItemConfiguracao() 
					+ "; Ação: " + mov.getAcao().getTituloAcao() + "; Atendente: " + mov.getLotaAtendente().getSigla());
			mov.setMotivoEscalonamento(motivo);
			mov.salvar(getCadastrante(), getLotaTitular());
			result.redirectTo(this).exibir(solicitacao.getIdSolicitacao(), todoOContexto(), ocultas());
		}
			
	}
	
	public void listarDesignacao(boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM:Administrar");
		List<SrConfiguracao> designacoes = SrConfiguracao.listarDesignacoes(mostrarDesativados, null);
		List<CpOrgaoUsuario> orgaos = em()
				.createQuery("from CpOrgaoUsuario").getResultList();
		List<CpComplexo> locais = CpComplexo.AR.all().fetch();

		List<SrPesquisa> pesquisaSatisfacao = SrPesquisa.AR.find(
				"hisDtFim is null").fetch();
		
		result.include("designacoes", designacoes);
		result.include("orgaos", orgaos);
		result.include("locais", locais);
		result.include("pesquisaSatisfacao", pesquisaSatisfacao);
	}
	
	public void listarDesignacaoDesativados() throws Exception {
		listarDesignacao(Boolean.TRUE);
	}

	public  Long gravarDesignacao(SrConfiguracao designacao)
			throws Exception {
		assertAcesso("ADM:Administrar");
		validarFormEditarDesignacao(designacao);
		designacao.salvarComoDesignacao();
		
		return designacao.getId();
	}

	public  Long desativarDesignacao(Long id, boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM:Administrar");
		SrConfiguracao designacao = em().find(SrConfiguracao.class, id);
		designacao.finalizar();
		
		return designacao.getId();
	}

	public void editarAcordo(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrAcordo acordo = new SrAcordo();
		if (id != null)
			acordo = SrAcordo.AR.findById(id);
		List<SrAtributo> parametros = SrAtributo.listarParaAcordo(false);
		List<CpUnidadeMedida> unidadesMedida = CpDao.getInstance().listarUnidadesMedida();
		List<SrConfiguracao> abrangencias = SrConfiguracao.listarAbrangenciasAcordo(false, acordo);
		List<CpOrgaoUsuario> orgaos = em()
				.createQuery("from CpOrgaoUsuario").getResultList();
		List<CpComplexo> locais = CpComplexo.AR.all().fetch();
		result.include("acordo", acordo);
		result.include("parametros", parametros);
		result.include("unidadesMedida", unidadesMedida);
		result.include("abrangencias", abrangencias);
		result.include("orgaos", orgaos);
		result.include("locais", locais);
	}

	public void gravarAcordo(SrAcordo acordo) throws Exception {
		assertAcesso("ADM:Administrar");
		acordo.salvarComHistorico();
		result.redirectTo(this).buscarAcordo(null, false, false);
	}

	public void desativarAcordo(Long id, boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM:Administrar");
		SrAcordo acordo = SrAcordo.AR.findById(id);
		acordo.finalizar();
		result.redirectTo(this).buscarAcordo(null, false, mostrarDesativados);
	}
	
	public void reativarAcordo(Long id, boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM:Administrar");
		SrAcordo acordo = SrAcordo.AR.findById(id);
		acordo.salvarComHistorico();
		result.redirectTo(this).buscarAcordo(null, false, mostrarDesativados);
	}
	
	public  Long gravarAbrangencia(SrConfiguracao associacao) throws Exception {
		assertAcesso("ADM:Administrar");
		associacao.salvarComoAbrangenciaAcordo();
		return associacao.getId();		
	}
	
	public void desativarAbrangenciaEdicao(Long idAcordo, Long idAssociacao) throws Exception {
		assertAcesso("ADM:Administrar");
		SrConfiguracao abrangencia = em().find(SrConfiguracao.class, idAssociacao);
		abrangencia.finalizar();
		result.redirectTo(this).editarAcordo(idAcordo);
	}

	public void reativarAbrangencia(Long id, boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM:Administrar");
		SrConfiguracao associacao = em().find(SrConfiguracao.class, id);
		associacao.salvarComHistorico();
		//listarAssociacao(mostrarDesativados);
	}

	public  Long reativarDesignacao(Long id, boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM:Administrar");
		SrConfiguracao designacao = em().find(SrConfiguracao.class, id);
		designacao.salvarComHistorico();
		
		return designacao.getId();
	}
	
	public void selecionarAcordo(String sigla)
			throws Exception {
		SrAcordo sel = new SrAcordo().selecionar(sigla);
		//render("@selecionar", sel);
		result.include("sel", sel);
	}

	public void buscarAcordo(String nome, boolean popup, boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM:Administrar");
		List<SrAcordo> acordos = SrAcordo.listar(mostrarDesativados);
		result.include("acordos", acordos);
		result.include("nome", nome);
		result.include("popup", popup);
		result.include("mostrarDesativados", mostrarDesativados);
	}
	
	public void buscarAcordoDesativadas() throws Exception {
		result.redirectTo(this).buscarAcordo(null, false, true);
	}
	
	public void editarPermissaoUsoLista(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		List<CpOrgaoUsuario> orgaos = em()
				.createQuery("from CpOrgaoUsuario").getResultList();
		List<CpComplexo> locais = CpComplexo.AR.all().fetch();
		List<SrLista> listasPrioridade = SrLista
				.getCriadasPelaLotacao(getLotaTitular());
		SrConfiguracao permissao = new SrConfiguracao();
		if (id != null)
			permissao = em().find(SrConfiguracao.class, id);
		result.include("permissao", permissao);
		result.include("orgaos", orgaos);
		result.include("locais", locais);
		result.include("listasPrioridade", listasPrioridade);
	}

	public  Long gravarPermissaoUsoLista(SrConfiguracao permissao) throws Exception {
		assertAcesso("ADM:Administrar");
		validarFormEditarPermissaoUsoLista(permissao);
		permissao.salvarComoPermissaoUsoLista();
		return permissao.getId();
	}

	public void desativarPermissaoUsoLista(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrConfiguracao designacao = em().find(SrConfiguracao.class, id);
		designacao.finalizar();
	}
	
	public void desativarPermissaoUsoListaEdicao(Long idLista, Long idPermissao) throws Exception {
		assertAcesso("ADM:Administrar");
		SrConfiguracao configuracao = em().find(SrConfiguracao.class, idPermissao);
		configuracao.finalizar();
		result.redirectTo(this).editarLista(idLista);
	}

	public void editarAssociacao(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrConfiguracao associacao = new SrConfiguracao();
		if (id != null)
			associacao = (SrConfiguracao) em()
					.find(SrConfiguracao.class, id);
		result.include("associacao", associacao);
	}

	public  Long gravarAssociacao(SrConfiguracao associacao) throws Exception {
		assertAcesso("ADM:Administrar");
		associacao.salvarComoAssociacaoAtributo();
		return associacao.getId();		
	}
	
	public void desativarAssociacaoEdicao(Long idAtributo, Long idAssociacao) throws Exception {
		assertAcesso("ADM:Administrar");
		SrConfiguracao associacao = em().find(SrConfiguracao.class, idAssociacao);
		associacao.finalizar();
		result.redirectTo(this).editarAtributo(idAtributo);
	}

	public void desativarAssociacao(Long id, boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM:Administrar");
		SrConfiguracao associacao = em().find(SrConfiguracao.class, id);
		associacao.finalizar();
		//listarAssociacao(mostrarDesativados);
	}

	public void reativarAssociacao(Long id, boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM:Administrar");
		SrConfiguracao associacao = em().find(SrConfiguracao.class, id);
		associacao.salvarComHistorico();
		//listarAssociacao(mostrarDesativados);
	}

	public void listarItem(boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM:Administrar");
		List<SrItemConfiguracao> itens = SrItemConfiguracao.listar(mostrarDesativados);
		result.include("itens", itens);
		result.include("mostrarDesativados", mostrarDesativados);
	}
	
	public void listarItemDesativados() throws Exception {
		result.redirectTo(this).listarItem(Boolean.TRUE);
	}

	@SuppressWarnings("unchecked")
	public void editarItem(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		List<SrConfiguracao> designacoes;
		List<CpOrgaoUsuario> orgaos = em()
				.createQuery("from CpOrgaoUsuario").getResultList();
		List<CpComplexo> locais = CpComplexo.AR.all().fetch();
		List<CpUnidadeMedida> unidadesMedida = CpDao.getInstance().listarUnidadesMedida();
		List<SrPesquisa> pesquisaSatisfacao = SrPesquisa.AR.find(
				"hisDtFim is null").fetch();
		List<SrLista> listasPrioridade = SrLista.listar(false);
		
		SrItemConfiguracao itemConfiguracao = new SrItemConfiguracao();
		if (id != null) {
			itemConfiguracao = SrItemConfiguracao.AR.findById(id);
		}
		
		result.include("itemConfiguracao", itemConfiguracao);
		result.include("orgaos", orgaos);
		result.include("locais", locais);
		result.include("unidadesMedida", unidadesMedida);
		result.include("pesquisaSatisfacao", pesquisaSatisfacao);
		result.include("listasPrioridade", listasPrioridade);
	}

	public void gravarItem(SrItemConfiguracao itemConfiguracao)
			throws Exception {
		assertAcesso("ADM:Administrar");
		validarFormEditarItem(itemConfiguracao);
		itemConfiguracao.salvarComHistorico();

		// Atualiza os conhecimentos relacionados
		// Edson: deveria ser feito por webservice. Nao estah sendo coberta
		// a atualizacao da classificacao quando ocorre mudanca de posicao na
		// hierarquia, pois isso eh mais complexo de acertar.
		/*try {
			HashMap<String, String> atributos = new HashMap<String, String>();
			for (Http.Header h : request.headers.values())
				if (!h.name.equals("content-type"))
					atributos.put(h.name, h.value());

			SrItemConfiguracao anterior = itemConfiguracao
					.getHistoricoItemConfiguracao().get(0);
			if (anterior != null && !itemConfiguracao.tituloItemConfiguracao.equals(anterior.tituloItemConfiguracao))	{
				String url = "http://"+ Play.configuration.getProperty("servidor.principal")+ ":8080/sigagc/app/updateTag?before="+ anterior.getTituloSlugify() + "&after="	+ itemConfiguracao.getTituloSlugify();
				
				
			
			}
		} catch (Exception e) {
			Logger.error("Item " + itemConfiguracao.idItemConfiguracao
					+ " salvo, mas nao foi possivel atualizar conhecimento");
			e.printStackTrace();
		}*/
		
		result.redirectTo(this).listarItem(false);
	}

	public void desativarItem(Long id, boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM:Administrar");
		SrItemConfiguracao item = SrItemConfiguracao.AR.findById(id);
		item.finalizar();
		result.redirectTo(this).listarItem(mostrarDesativados);
	}
	
	public void reativarItem(Long id, boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM:Administrar");
		SrItemConfiguracao item = SrItemConfiguracao.AR.findById(id);
		item.salvarComHistorico();
		result.redirectTo(this).listarItem(mostrarDesativados);
	}


	public void selecionarItem(String sigla, SrSolicitacao sol)
			throws Exception {
		SrItemConfiguracao sel = new SrItemConfiguracao().selecionar(sigla, sol.getItensDisponiveis());
		//render("@selecionar", sel);
		result.include("sel", sel);
	}

	public void buscarItem(String sigla, String nome,
			SrItemConfiguracao filtro, SrSolicitacao sol) {

		List<SrItemConfiguracao> itens = null;

		try {
			if (filtro == null)
				filtro = new SrItemConfiguracao();
			if (sigla != null && !sigla.trim().equals(""))
				filtro.setSigla(sigla);
			itens = filtro.buscar(sol != null
					&& (sol.getSolicitante() != null || sol.getLocal() != null) ? sol
					.getItensDisponiveis() : null);
		} catch (Exception e) {
			itens = new ArrayList<SrItemConfiguracao>();
		}

		result.include("itens", itens);
		result.include("filtro", filtro);
		result.include("nome", nome);
		result.include("sol", sol);
	}

	public void listarAtributoDesativados() throws Exception {
		result.redirectTo(this).listarAtributo(Boolean.TRUE);
	}
	
	public void listarAtributo(boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM:Administrar");
		List<SrAtributo> atts = SrAtributo.listar(null, mostrarDesativados);
		result.include("ats", atts);
	}

	public void editarAtributo(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		String tipoAtributoAnterior = null;
		SrAtributo att = new SrAtributo();
		if (id != null) {
			att = SrAtributo.AR.findById(id);
			if(att.getTipoAtributo() != null) {
				tipoAtributoAnterior = att.getTipoAtributo().name();
			}
		}
		if (att.getObjetivoAtributo() == null)
			att.setObjetivoAtributo(SrObjetivoAtributo.AR.findById(SrObjetivoAtributo.OBJETIVO_SOLICITACAO));
		List<SrConfiguracao> associacoes = SrConfiguracao.listarAssociacoesAtributo(att, Boolean.FALSE);
		List<SrObjetivoAtributo> objetivos = SrObjetivoAtributo.AR.all().fetch();
		result.include("att", att);
		result.include("tipoAtributoAnterior", tipoAtributoAnterior);
		result.include("associacoes", associacoes);
		result.include("objetivos", objetivos);
	}

	public void gravarAtributo(SrAtributo att) throws Exception {
		assertAcesso("ADM:Administrar");
		validarFormEditarAtributo(att);
		att.salvarComHistorico();
		result.redirectTo(this).listarAtributo(Boolean.FALSE);
	}

	private  void validarFormEditarAtributo(SrAtributo att) {
		if (att.getNomeAtributo().equals("")) {
			validation.add(new ValidationMessage("att.nomeAtributo",
					"Nome de atributo não informado"));
		}

		if (att.getTipoAtributo() == SrTipoAtributo.VL_PRE_DEFINIDO 
				&& att.getDescrPreDefinido().equals("")) {
			validation.add(new ValidationMessage("att.descrPreDefinido",
					"Valores Pré-definido não informados"));
		}
		
		for (Message error : validation.getErrors()) {
			System.out.println(error.getMessage());
		}

		if (validation.hasErrors()) {
			//render("@editarAtributo", att);
			result.include("att", att);
		}
	}

	public void desativarAtributo(Long id, boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM:Administrar");
		SrAtributo item = SrAtributo.AR.findById(id);
		item.finalizar();
		result.redirectTo(this).listarAtributo(mostrarDesativados);
	}

	public void reativarAtributo(Long id, boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM:Administrar");
		SrAtributo item = SrAtributo.AR.findById(id);
		item.salvarComHistorico();
		result.redirectTo(this).listarAtributo(mostrarDesativados);
	}

	public void listarPesquisa(boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM:Administrar");
		List<SrPesquisa> pesquisas = SrPesquisa.listar(mostrarDesativados);
		result.include("pesquisas", pesquisas);
	}
	
	public void listarPesquisaDesativadas() throws Exception {
		result.redirectTo(this).listarPesquisa(Boolean.TRUE);
	}

	public void editarPesquisa(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrPesquisa pesq = new SrPesquisa();
		if (id != null)
			pesq = SrPesquisa.AR.findById(id);
		List<SrTipoPergunta> tipos = SrTipoPergunta.AR.all().fetch();
		result.include("pesq", pesq);
		result.include("tipos", tipos);
	}

	public void gravarPesquisa(SrPesquisa pesq) throws Exception {
		assertAcesso("ADM:Administrar");
		pesq.salvarComHistorico();
		result.redirectTo(this).listarPesquisa(Boolean.FALSE);
	}

	public void desativarPesquisa(Long id, boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM:Administrar");
		SrPesquisa pesq = SrPesquisa.AR.findById(id);
		pesq.finalizar();
		result.redirectTo(this).listarPesquisa(mostrarDesativados);
	}
	
	public void reativarPesquisa(Long id, boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM:Administrar");
		SrPesquisa pesq = SrPesquisa.AR.findById(id);
		pesq.salvarComHistorico();
		result.redirectTo(this).listarPesquisa(mostrarDesativados);
	}
	
	public void listarConhecimento(Long idItem, Long idAcao, boolean ajax) throws Exception {
		assertAcesso("EDTCONH:Criar Conhecimentos");
		SrItemConfiguracao item = idItem != null ? (SrItemConfiguracao)SrItemConfiguracao.AR.findById(idItem) : null;
		SrAcao acao = idAcao != null ? (SrAcao)SrAcao.AR.findById(idAcao) : null;			
		//render("@listarConhecimento" + (ajax ? "Ajax" : ""), item, acao);
		result.include("item",item);
		result.include("acao", acao);
	}
	
	public void listarEquipe(boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM:Administrar");
		List<SrEquipe> listaEquipe = SrEquipe.listar(mostrarDesativados);
		result.include("listaEquipe", listaEquipe);
	}
	
	public void editarEquipe(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrEquipe equipe = null;
		List<CpOrgaoUsuario> orgaos = em()
				.createQuery("from CpOrgaoUsuario").getResultList();
		List<CpComplexo> locais = CpComplexo.AR.all().fetch();
		List<CpUnidadeMedida> unidadesMedida = CpDao.getInstance()
				.listarUnidadesMedida();
		List<SrPesquisa> pesquisaSatisfacao = SrPesquisa.AR.find(
				"hisDtFim is null").fetch();
		List<SrLista> listasPrioridade = SrLista.listar(false);

		if (id != null)
			equipe = SrEquipe.AR.findById(id);
		else {
			equipe = new SrEquipe();
			equipe.setLotacao(getLotaTitular());
		}

		List<SrConfiguracao> designacoesEquipe = equipe.getDesignacoes();

		result.include("equipe", equipe);
		result.include("designacoesEquipe", designacoesEquipe);
		result.include("orgaos", orgaos);
		result.include("locais", locais);
		result.include("unidadesMedida", unidadesMedida);
		result.include("pesquisaSatisfacao", pesquisaSatisfacao);
		result.include("listasPrioridade", listasPrioridade);
	}
	
	public void gravarEquipe(SrEquipe equipe) throws Exception {
		assertAcesso("ADM:Administrar");
		validarFormEditarEquipe(equipe);
		equipe.salvarComHistorico();
	}
	
	private  void validarFormEditarEquipe(SrEquipe equipe) throws Exception {
		StringBuffer sb = new StringBuffer();
		
		if (equipe.getLotacao() == null) {
			validation.add(new ValidationMessage("equipe.lotacao", "Lotação não informada"));
		}
		
		for (Message error : validation.getErrors()) {
			System.out.println(error.getCategory() + " :" + error.getMessage());
			sb.append(error.getCategory() + ";");
		}

		if (validation.hasErrors()) {
			throw new Exception(sb.toString());
		}
	}

	public void listarAcao(boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM:Administrar");
		List<SrAcao> acoes = SrAcao.listar(mostrarDesativados);
		result.include("acoes", acoes);
		result.include("mostrarDesativados", mostrarDesativados);
	}
	
	public void listarAcaoDesativados() throws Exception {
		result.redirectTo(this).listarAcao(Boolean.TRUE);
	}

	public void editarAcao(Long id) throws Exception {
		assertAcesso("ADM:Administrar");
		SrAcao acao = new SrAcao();
		if (id != null)
			acao = SrAcao.AR.findById(id);
		result.include("acao", acao);
	}

	public void gravarAcao(SrAcao acao) throws Exception {
		assertAcesso("ADM:Administrar");
		validarFormEditarAcao(acao);
		acao.salvarComHistorico();				
		result.redirectTo(this).listarAcao(false);
	}

	public void desativarAcao(Long id, boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM:Administrar");
		SrAcao acao = SrAcao.AR.findById(id);
		acao.finalizar();
		result.redirectTo(this).listarAcao(mostrarDesativados);
	}
	
	public void reativarAcao(Long id, boolean mostrarDesativados) throws Exception {
		assertAcesso("ADM:Administrar");
		SrAcao acao = SrAcao.AR.findById(id);
		acao.salvarComHistorico();
		result.redirectTo(this).listarAcao(mostrarDesativados);
	}

	public void selecionarAcao(String sigla, SrSolicitacao sol)
			throws Exception {

		SrAcao sel = new SrAcao().selecionar(sigla, sol.getAcoesDisponiveis());
		//render("@selecionar", sel);
		result.include("sel", sel);
	}

	public void buscarAcao(String sigla, String nome, SrAcao filtro,
			SrSolicitacao sol) {
		List<SrAcao> itens = null;

		try {
			if (filtro == null)
				filtro = new SrAcao();
			if (sigla != null && !sigla.trim().equals(""))
				filtro.setSigla(sigla);
			itens = filtro.buscar(sol != null
					&& (sol.getSolicitante() != null || sol.getLocal() != null) ? sol
					.getAcoesDisponiveis() : null);
		} catch (Exception e) {
			itens = new ArrayList<SrAcao>();
		}

		result.include("itens", itens);
		result.include("filtro", filtro);
		result.include("nome", nome);
		result.include("sol", sol);
	}

	public void selecionarSiga(String sigla, String prefixo, String tipo, String nome)
			throws Exception {
		result.redirectTo("/siga/" + (prefixo != null ? prefixo + "/" : "") + tipo + "/selecionar.action?" + "propriedade="
				+ tipo + nome + "&sigla=" + URLEncoder.encode(sigla, "UTF-8"));
	}

	public void buscarSiga(String sigla, String prefixo, String tipo, String nome)
			throws Exception {
		result.redirectTo("/siga/" + (prefixo != null ? prefixo + "/" : "") + tipo + "/buscar.action?" + "propriedade=" + tipo
				+ nome + "&sigla=" + URLEncoder.encode(sigla, "UTF-8"));
	}

	public void listarLista(boolean mostrarDesativados) throws Exception {
		List<SrLista> lista = SrLista.listar(mostrarDesativados);
		result.include("lista", lista);
		result.include("mostrarDesativados", mostrarDesativados);
		
	}
	
	public void listarListaDesativados() throws Exception {
		result.redirectTo(this).listarLista(Boolean.TRUE);
	}

	@SuppressWarnings("unchecked")
	public void editarLista(Long id) throws Exception {
		List<CpOrgaoUsuario> orgaos = em()
				.createQuery("from CpOrgaoUsuario").getResultList();
		List<CpComplexo> locais = CpComplexo.AR.all().fetch();
		
		SrLista lista = new SrLista();
		if (id != null)
			lista = SrLista.AR.findById(id);
		
		List<SrConfiguracao> permissoes = SrConfiguracao
				.listarPermissoesUsoLista(lista, false);
		List<SrTipoPermissaoLista> tiposPermissao = SrTipoPermissaoLista.AR.all().fetch();
		
		result.include("lista", lista);
		result.include("permissoes", permissoes);
		result.include("orgaos", orgaos);
		result.include("tiposPermissao", tiposPermissao);
	}

	public void gravarLista(SrLista lista) throws Exception {
		lista.salvarComHistorico();
		result.redirectTo(this).exibirLista(lista.getIdLista());
	}

	public void desativarLista(Long id, boolean mostrarDesativados) throws Exception {
		SrLista lista = SrLista.AR.findById(id);
		lista.finalizar();
		result.redirectTo(this).listarLista(mostrarDesativados);
	}
	
	public void reativarLista(Long id, boolean mostrarDesativados) throws Exception {
		SrLista lista = SrLista.AR.findById(id);
		lista.salvarComHistorico();
		result.redirectTo(this).listarLista(mostrarDesativados);
	}
	
	public void exibirPrioridade(SrSolicitacao solicitacao) {
		solicitacao.associarPrioridadePeloGUT();
		result.include("solicitacao", solicitacao);
	}

}