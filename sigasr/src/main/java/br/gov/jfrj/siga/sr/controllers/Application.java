package br.gov.jfrj.siga.sr.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.joda.time.LocalDate;

import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.cp.CpComplexo;
import br.gov.jfrj.siga.cp.CpUnidadeMedida;
import br.gov.jfrj.siga.dp.CpMarcador;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.Objeto;
import br.gov.jfrj.siga.sr.model.SrAcao;
import br.gov.jfrj.siga.sr.model.SrAcordo;
import br.gov.jfrj.siga.sr.model.SrAtributo;
import br.gov.jfrj.siga.sr.model.SrAtributoSolicitacao;
import br.gov.jfrj.siga.sr.model.SrConfiguracao;
import br.gov.jfrj.siga.sr.model.SrConfiguracaoBL;
import br.gov.jfrj.siga.sr.model.SrDisponibilidade;
import br.gov.jfrj.siga.sr.model.SrEquipe;
import br.gov.jfrj.siga.sr.model.SrGravidade;
import br.gov.jfrj.siga.sr.model.SrItemConfiguracao;
import br.gov.jfrj.siga.sr.model.SrLista;
import br.gov.jfrj.siga.sr.model.SrMovimentacao;
import br.gov.jfrj.siga.sr.model.SrObjetivoAtributo;
import br.gov.jfrj.siga.sr.model.SrPergunta;
import br.gov.jfrj.siga.sr.model.SrPesquisa;
import br.gov.jfrj.siga.sr.model.SrPrioridade;
import br.gov.jfrj.siga.sr.model.SrSolicitacao;
import br.gov.jfrj.siga.sr.model.SrSolicitacao.SrTarefa;
import br.gov.jfrj.siga.sr.model.SrTipoAcao;
import br.gov.jfrj.siga.sr.model.SrTipoAtributo;
import br.gov.jfrj.siga.sr.model.SrTipoMotivoEscalonamento;
import br.gov.jfrj.siga.sr.model.SrTipoMotivoPendencia;
import br.gov.jfrj.siga.sr.model.SrTipoMovimentacao;
import br.gov.jfrj.siga.sr.model.SrTipoPergunta;
import br.gov.jfrj.siga.sr.model.SrTipoPermissaoLista;
import br.gov.jfrj.siga.sr.model.SrUrgencia;
import br.gov.jfrj.siga.sr.model.vo.PaginaItemConfiguracao;
import br.gov.jfrj.siga.sr.model.vo.SelecionavelVO;
import br.gov.jfrj.siga.sr.model.vo.SrSolicitacaoListaVO;
import br.gov.jfrj.siga.sr.notifiers.Correio;
import br.gov.jfrj.siga.sr.util.AtualizacaoLista;
import br.gov.jfrj.siga.sr.util.SrSolicitacaoAtendidos;
import br.gov.jfrj.siga.sr.util.SrSolicitacaoFiltro;
import br.gov.jfrj.siga.sr.util.SrSolicitacaoItem;
import br.gov.jfrj.siga.sr.validator.SrError;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.sr.vraptor.SrController;
import br.gov.jfrj.siga.vraptor.SigaObjects;

import com.google.gson.Gson;

public class Application extends SrController {

    // TODO: usado nas classes SrColicitacao e SrMovimentacao
    private Correio correio;

    public Application(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em, SrValidator srValidator, Correio correio) {
        super(request, result, dao, so, em, srValidator);
        this.correio = correio;
        // TODO Auto-generated constructor stub
    }

    // @Before(priority = 1)
    public void addDefaultsAlways() throws Exception {
        // prepararSessao();
        SrConfiguracaoBL.get().limparCacheSeNecessario();
    }

    // @Before(priority = 2, unless = { "exibirAtendente", "exibirLocalERamal","exibirItemConfiguracao" })
    public void addDefaults() throws Exception {

        try {
            // obterCabecalhoEUsuario("rgb(235, 235, 232)");
            // assertAcesso("");
        } catch (Exception e) {
            // tratarExcecoes(e);
        }

        try {
            // assertAcesso("ADM:Administrar");
            // renderArgs.put("exibirMenuAdministrar", true);
        } catch (Exception e) {
            // renderArgs.put("exibirMenuAdministrar", false);
        }

        try {
            // assertAcesso("EDTCONH:Criar Conhecimentos");
            // renderArgs.put("exibirMenuConhecimentos", true);
        } catch (Exception e) {
            // renderArgs.put("exibirMenuConhecimentos", false);
        }

        try {
            // assertAcesso("REL:Relatorios");
            // renderArgs.put("exibirMenuRelatorios", true);
        } catch (Exception e) {
            // renderArgs.put("exibirMenuRelatorios", false);
        }
    }

    public boolean ocultas() {
        return true;
        // return Boolean.parseBoolean(params.get("ocultas"));
    }

    public boolean todoOContexto() {
        return true;
        // return Boolean.parseBoolean(params.get("todoOContexto"));
    }

    // protected static void assertAcesso(String path) throws Exception {
    // SigaApplication.assertAcesso("SR:Módulo de Serviços;" + path);
    // }

    // @Catch()
    // public void tratarExcecoes(Exception e) {
    // SigaApplication.tratarExcecoes(e);
    // }

    public void index() throws Exception {
        editar(null);
    }

    @SuppressWarnings("rawtypes")
    public void gadget() {
        Query query = ContextoPersistencia.em().createNamedQuery("contarSrMarcas");
        query.setParameter("idPessoaIni", getTitular().getIdInicial());
        query.setParameter("idLotacaoIni", getLotaTitular().getIdInicial());
        List contagens = query.getResultList();
        // render(contagens);
    }

    public void corporativo() {
        try {
            Corporativo.dadosrh();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
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

    public void exibirLocalRamalEMeioContato(SrSolicitacao solicitacao) throws Exception {
        // render(solicitacao.deduzirLocalRamalEMeioContato());
    }

    public void listarAcoesSlugify() {
        String acoes = "";
        for (Object o : SrAcao.listar(false)) {
            SrAcao a = (SrAcao) o;
            acoes += a.getSiglaAcao() + "&nbsp;&nbsp;" + (a.getGcTags()) + "<br/>";
        }

        String itens = "";
        for (Object o : SrItemConfiguracao.listar(false)) {
            SrItemConfiguracao a = (SrItemConfiguracao) o;
            itens += a.getSiglaItemConfiguracao() + "&nbsp;&nbsp;" + (a.getGcTags()) + "<br/>";
        }
        // render(acoes, itens);
    }

    public void listarSolicitacoesRelacionadas(SrSolicitacaoFiltro solicitacao, HashMap<Long, String> atributoSolicitacaoMap) throws Exception {

        solicitacao.setAtributoSolicitacaoMap(atributoSolicitacaoMap);
        List<Object[]> solicitacoesRelacionadas = solicitacao.buscarSimplificado();
        // render(solicitacoesRelacionadas);
    }

    public void exibirAtributos(SrSolicitacao solicitacao) throws Exception {
        // render(solicitacao);
    }

    public void exibirAtributosConsulta(SrSolicitacaoFiltro filtro) throws Exception {
        List<SrAtributo> atributosDisponiveisAdicao = atributosDisponiveisAdicaoConsulta(filtro);
        // render(filtro, atributosDisponiveisAdicao);
    }

    public List<SrAtributo> atributosDisponiveisAdicaoConsulta(SrSolicitacaoFiltro filtro) throws Exception {
        List<SrAtributo> listaAtributosAdicao = new ArrayList<SrAtributo>();
        Map<Long, String> atributoMap = filtro.getAtributoSolicitacaoMap();

        for (SrAtributo srAtributo : SrAtributo.listarParaSolicitacao(Boolean.FALSE)) {
            if (!atributoMap.containsKey(srAtributo.getIdAtributo())) {
                listaAtributosAdicao.add(srAtributo);
            }
        }
        return listaAtributosAdicao;
    }

    public void exibirItemConfiguracao(SrSolicitacao solicitacao) throws Exception {
        if (solicitacao.getSolicitante() == null)
            // render(solicitacao);

            if (!solicitacao.getItensDisponiveis().contains(solicitacao.getItemConfiguracao()))
                solicitacao.setItemConfiguracao(null);

        DpPessoa titular = solicitacao.getTitular();
        DpLotacao lotaTitular = solicitacao.getLotaTitular();
        Map<SrAcao, List<SrTarefa>> acoesEAtendentes = solicitacao.getAcoesEAtendentes();
        // render(solicitacao, titular, lotaTitular, acoesEAtendentes);
    }

    public void exibirAcao(SrSolicitacao solicitacao) throws Exception {
        Map<SrAcao, List<SrTarefa>> acoesEAtendentes = solicitacao.getAcoesEAtendentes();
        // render(solicitacao, acoesEAtendentes);
    }

    public void exibirAcaoEscalonar(Long id, Long itemConfiguracao) throws Exception {
        SrSolicitacao solicitacao = SrSolicitacao.AR.findById(id);
        solicitacao.setTitular(getTitular());
        solicitacao.setLotaTitular(getLotaTitular());
        Map<SrAcao, List<SrTarefa>> acoesEAtendentes = new HashMap<SrAcao, List<SrTarefa>>();
        if (itemConfiguracao != null) {
            solicitacao.setItemConfiguracao(SrItemConfiguracao.AR.findById(itemConfiguracao));
            acoesEAtendentes = solicitacao.getAcoesEAtendentes();
        }
        // render(solicitacao, acoesEAtendentes);
    }

    public void exibirConhecimentosRelacionados(SrSolicitacao solicitacao) throws Exception {
        // render(solicitacao);
    }

    @SuppressWarnings("unchecked")
    private void formEditar(SrSolicitacao solicitacao) throws Exception {

        List<CpComplexo> locais = ContextoPersistencia.em().createQuery("from CpComplexo").getResultList();

        Map<SrAcao, List<SrTarefa>> acoesEAtendentes = solicitacao.getAcoesEAtendentes();
        // render("@editar", solicitacao, locais, acoesEAtendentes);
    }

    @SuppressWarnings("static-access")
    private void validarFormEditar(SrSolicitacao solicitacao) throws Exception {

        if (solicitacao.getSolicitante() == null) {
            srValidator.addError("solicitacao.solicitante", "Solicitante n&atilde;o informado");
        }
        if (solicitacao.getItemConfiguracao() == null) {
            srValidator.addError("solicitacao.itemConfiguracao", "Item n&atilde;o informado");
        }
        if (solicitacao.getAcao() == null) {
            srValidator.addError("solicitacao.acao", "A&ccedil&atilde;o n&atilde;o informada");
        }

        if (solicitacao.getDescrSolicitacao() == null || solicitacao.getDescrSolicitacao().trim().equals("")) {
            srValidator.addError("solicitacao.descrSolicitacao", "Descri&ccedil&atilde;o n&atilde;o informada");
        }

        Map<Long, Boolean> obrigatorio = solicitacao.getObrigatoriedadeTiposAtributoAssociados();
        for (SrAtributoSolicitacao att : solicitacao.getAtributoSolicitacaoSet()) {
            // Para evitar NullPointerExcetpion quando nao encontrar no Map
            if (Boolean.TRUE.equals(obrigatorio.get(att.getAtributo().getIdAtributo()))) {
                if ((att.getValorAtributoSolicitacao() == null || att.getValorAtributoSolicitacao().trim().equals("")))
                    srValidator.addError("solicitacao.atributoSolicitacaoMap[" + att.getAtributo().getIdAtributo() + "]", att.getAtributo().getNomeAtributo() + " n&atilde;o informado");
            }
        }

        if (srValidator.hasErrors()) {
            formEditar(solicitacao);
        }
    }

    @SuppressWarnings("static-access")
    private void validarFormEditarItem(SrItemConfiguracao itemConfiguracao) throws Exception {
        if (itemConfiguracao.getSiglaItemConfiguracao().equals("")) {
            srValidator.addError("siglaAcao", "Código não informado");
        }
        if (srValidator.hasErrors()) {
            enviarErroValidacao();
        }

    }

    @SuppressWarnings("static-access")
    private void validarFormEditarAcao(SrAcao acao) {
        if (acao.getSiglaAcao().equals("")) {
            srValidator.addError("siglaAcao", "Código não informado");
        }
        if (acao.getTituloAcao().equals("")) {
            srValidator.addError("tituloAcao", "Titulo não informado");
        }
        if (srValidator.hasErrors()) {
            enviarErroValidacao();
        }
    }

    private void validarFormEditarTipoAcao(SrTipoAcao acao) {
        if (acao.getSiglaTipoAcao().equals("")) {
            srValidator.addError("siglaAcao", "CÃ³digo nÃ£o informado");
        }
        if (acao.getTituloTipoAcao().equals("")) {
            srValidator.addError("tituloAcao", "Titulo nÃ£o informado");
        }
        if (srValidator.hasErrors()) {
            enviarErroValidacao();
        }
    }

    // private void enviarErroValidacao() {
    // JsonArray jsonArray = new JsonArray();
    //
    // List<Error> errors = Validation.errors();
    // for (Error error : errors) {
    // jsonArray.add(new Gson().toJsonTree(error));
    // }
    // error(Http.StatusCode.BAD_REQUEST, jsonArray.toString());
    // }

    @SuppressWarnings("static-access")
    private void validarFormEditarDesignacao(SrConfiguracao designacao) throws Exception {
        StringBuffer sb = new StringBuffer();

        if (designacao.getDescrConfiguracao() == null || designacao.getDescrConfiguracao().isEmpty())
            srValidator.addError("designacao.descrConfiguracao", "Descrição não informada");

        for (SrError error : srValidator.getErros()) {
            sb.append(error.getKey() + ";");
        }

        if (srValidator.hasErrors()) {
            throw new Exception(sb.toString());
        }
    }

    private void validarFormEditarPermissaoUsoLista(SrConfiguracao designacao) {
    }

    public void gravar(SrSolicitacao solicitacao) throws Exception {

        if (!solicitacao.isRascunho())
            validarFormEditar(solicitacao);

        solicitacao.salvar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
        Long id = solicitacao.getIdSolicitacao();
        exibir(id, todoOContexto(), ocultas());
    }

    public void juntar(Long idSolicitacaoAJuntar, Long idSolicitacaoRecebeJuntada, String justificativa) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(idSolicitacaoAJuntar);
        SrSolicitacao solRecebeJuntada = SrSolicitacao.AR.findById(idSolicitacaoRecebeJuntada);
        sol.juntar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(), solRecebeJuntada, justificativa);
        exibir(idSolicitacaoAJuntar, todoOContexto(), ocultas());
    }

    public void vincular(Long idSolicitacaoAVincular, Long idSolicitacaoRecebeVinculo, String justificativa) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(idSolicitacaoAVincular);
        SrSolicitacao solRecebeVinculo = SrSolicitacao.AR.findById(idSolicitacaoRecebeVinculo);
        sol.vincular(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(), solRecebeVinculo, justificativa);
        exibir(idSolicitacaoAVincular, todoOContexto(), ocultas());
    }

    public void desentranhar(Long id, String justificativa) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(id);
        sol.desentranhar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(), justificativa);
        exibir(id, todoOContexto(), ocultas());
    }

    public void estatistica() throws Exception {
        assertAcesso("REL:Relatorios");
        List<SrSolicitacao> lista = SrSolicitacao.AR.all().fetch();

        List<String[]> listaSols = SrSolicitacao.AR.find(
                "select sol.gravidade, count(distinct sol.idSolicitacao) " + "from SrSolicitacao sol, SrMovimentacao movimentacao "
                        + "where sol.idSolicitacao = movimentacao.solicitacao and movimentacao.tipoMov <> 7 " + "and movimentacao.lotaAtendente = " + getLotaTitular().getIdLotacao() + " "
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

        // String estat = sb.toString();

        // List<SrSolicitacao> listaEvol = SrSolicitacao.all().fetch();

        SrSolicitacaoAtendidos set = new SrSolicitacaoAtendidos();
        List<Object[]> listaEvolSols = SrSolicitacao.AR.find(
                "select extract (month from sol.dtReg), extract (year from sol.dtReg), count(distinct sol.idSolicitacao) " + "from SrSolicitacao sol, SrMovimentacao movimentacao "
                        + "where sol.idSolicitacao = movimentacao.solicitacao " + "and movimentacao.lotaAtendente = " + getLotaTitular().getIdLotacao() + " "
                        + "group by extract (month from sol.dtReg), extract (year from sol.dtReg)").fetch();

        for (Object[] sols : listaEvolSols) {
            set.add(new SrSolicitacaoItem((Integer) sols[0], (Integer) sols[1], (Long) sols[2], 0, 0));
        }

        List<Object[]> listaFechados = SrSolicitacao.AR.find(
                "select extract (month from sol.dtReg), extract (year from sol.dtReg), count(distinct sol.idSolicitacao) " + "from SrSolicitacao sol, SrMovimentacao movimentacao "
                        + "where sol.idSolicitacao = movimentacao.solicitacao " + "and movimentacao.tipoMov = 7 " + "and movimentacao.lotaAtendente = " + getLotaTitular().getId() + " "
                        + "group by extract (month from sol.dtReg), extract (year from sol.dtReg)").fetch();
        for (Object[] fechados : listaFechados) {
            set.add(new SrSolicitacaoItem((Integer) fechados[0], (Integer) fechados[1], 0, (Long) fechados[2], 0));
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
            SrSolicitacaoItem o = new SrSolicitacaoItem(ldl.getMonthOfYear(), ldl.getYear(), 0, 0, 0);
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

        // List<SrSolicitacao> top = SrSolicitacao.all().fetch();

        List<String[]> listaTop = SrSolicitacao.AR.find(
                "select sol.itemConfiguracao.tituloItemConfiguracao, count(distinct sol.idSolicitacao) " + "from SrSolicitacao sol, SrMovimentacao movimentacao "
                        + "where sol.idSolicitacao = movimentacao.solicitacao " + "and movimentacao.lotaAtendente = " + getLotaTitular().getIdLotacao() + " "
                        + "group by sol.itemConfiguracao.tituloItemConfiguracao").fetch();

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

        // List<SrSolicitacao> lstgut = SrSolicitacao.all().fetch();

        List<String[]> listaGUT = SrSolicitacao.AR.find(
                "select sol.gravidade, sol.urgencia, count(distinct sol.idSolicitacao) " + "from SrSolicitacao sol, SrMovimentacao movimentacao "
                        + "where sol.idSolicitacao = movimentacao.solicitacao " + "and movimentacao.lotaAtendente = " + getLotaTitular().getIdLotacao() + " " + "group by sol.gravidade, sol.urgencia")
                .fetch();

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

        // String gut = sbGUT.toString();
        // render(lista, evolucao, top10);
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
        // Edson: foi solicitado que funcionasse do modo abaixo. Eh o melhor modo??
        // todoOContexto = solicitacao.solicitacaoPai == null ? true : false;
        if (ocultas == null)
            ocultas = false;

        Set<SrMovimentacao> movs = solicitacao.getMovimentacaoSet(ocultas, null, false, todoOContexto, !ocultas, false);
        // render(solicitacao, movimentacao, todoOContexto, ocultas, movs);
    }

    @SuppressWarnings("unchecked")
    public void exibirLista(Long id) throws Exception {
        SrLista lista = SrLista.AR.findById(id);
        List<CpOrgaoUsuario> orgaos = ContextoPersistencia.em().createQuery("from CpOrgaoUsuario").getResultList();
        List<CpComplexo> locais = CpComplexo.AR.all().fetch();
        List<SrTipoPermissaoLista> tiposPermissao = SrTipoPermissaoLista.AR.all().fetch();
        SrSolicitacaoFiltro filtro = new SrSolicitacaoFiltro();
        SrSolicitacaoListaVO solicitacaoListaVO;
        String tiposPermissaoJson = new Gson().toJson(tiposPermissao);
        filtro.setIdListaPrioridade(id);
        lista = lista.getListaAtual();
        String jsonPrioridades = SrPrioridade.getJSON().toString();

        if (!lista.podeConsultar(getLotaTitular(), getCadastrante())) {
            throw new Exception("Exibição não permitida");
        }

        try {
            solicitacaoListaVO = SrSolicitacaoListaVO.fromFiltro(filtro, true, "", false, getLotaTitular(), getCadastrante());
        } catch (Exception e) {
            e.printStackTrace();
            solicitacaoListaVO = new SrSolicitacaoListaVO();
        }

        // render(lista, orgaos, locais, tiposPermissao, solicitacaoListaVO, tiposPermissaoJson, jsonPrioridades);
    }

    public void incluirEmLista(Long idSolicitacao) throws Exception {
        SrSolicitacao solicitacao = SrSolicitacao.AR.findById(idSolicitacao);
        solicitacao = solicitacao.getSolicitacaoAtual();
        List<SrPrioridade> prioridades = SrPrioridade.getValoresEmOrdem();
        // render(solicitacao, prioridades);
    }

    public void incluirEmListaGravar(Long idSolicitacao, Long idLista, SrPrioridade prioridade, Boolean naoReposicionarAutomatico) throws Exception {
        if (idLista == null) {
            throw new Exception("Selecione a lista para inclusÃ£o da solicitaÃ§Ã£o");
        }
        SrSolicitacao solicitacao = SrSolicitacao.AR.findById(idSolicitacao);
        SrLista lista = SrLista.AR.findById(idLista);
        solicitacao.incluirEmLista(lista, getCadastrante(), getLotaTitular(), prioridade, naoReposicionarAutomatico);
        exibir(idSolicitacao, todoOContexto(), ocultas());
    }

    public void retirarDeLista(Long idSolicitacao, Long idLista) throws Exception {
        SrSolicitacao solicitacao = SrSolicitacao.AR.findById(idSolicitacao);
        SrLista lista = SrLista.AR.findById(idLista);
        solicitacao.retirarDeLista(lista, getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
        exibirLista(idLista);
    }

    public void priorizarLista(List<AtualizacaoLista> listaPrioridadeSolicitacao, Long id) throws Exception {
        SrLista lista = SrLista.AR.findById(id);
        lista.priorizar(getCadastrante(), getLotaTitular(), listaPrioridadeSolicitacao);
        exibirLista(id);
    }

    public void selecionarSolicitacao(String sigla) throws Exception {
        SrSolicitacao sel = new SrSolicitacao();
        sel.setLotaTitular(getLotaTitular());
        sel = (SrSolicitacao) sel.selecionar(sigla);
        // render("@selecionar", sel);
    }

    // DB1: foi necessário receber e passar o parametro "nome"(igual ao buscarItem())
    // para chamar a function javascript correta,
    // e o parametro "popup" porque este metodo é usado também na lista,
    // e não foi possível deixar default no template(igual ao buscarItem.html)
    @SuppressWarnings("unchecked")
    public void buscarSolicitacao(SrSolicitacaoFiltro filtro, String nome, boolean popup) throws Exception {
        SrSolicitacaoListaVO solicitacaoListaVO;

        try {
            if (filtro.isPesquisar()) {
                solicitacaoListaVO = SrSolicitacaoListaVO.fromFiltro(filtro, false, nome, popup, getLotaTitular(), getCadastrante());
            } else {
                solicitacaoListaVO = new SrSolicitacaoListaVO();
            }
        } catch (Exception e) {
            e.printStackTrace();
            solicitacaoListaVO = new SrSolicitacaoListaVO();
        }

        // Montando o filtro...
        String[] tipos = new String[] { "Pessoa", "Lotação" };
        List<CpMarcador> marcadores = ContextoPersistencia.em().createQuery("select distinct cpMarcador from SrMarca").getResultList();

        List<SrAtributo> atributosDisponiveisAdicao = atributosDisponiveisAdicaoConsulta(filtro);
        List<SrLista> listasPrioridade = SrLista.listar(false);
        // render(solicitacaoListaVO, tipos, marcadores, filtro, nome, popup, atributosDisponiveisAdicao, listasPrioridade);
    }

    public void baixar(Long idArquivo) {
        // SrArquivo arq = SrArquivo.AR.findById(idArquivo);
        // if (arq != null)
        // renderBinary(new ByteArrayInputStream(arq.blob), arq.nomeArquivo);
    }

    public void darAndamento(SrMovimentacao movimentacao) throws Exception {
        movimentacao.setTipoMov(SrTipoMovimentacao.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ANDAMENTO));
        movimentacao.salvar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
        exibir(movimentacao.getSolicitacao().getIdSolicitacao(), todoOContexto(), ocultas());
    }

    public void anexarArquivo(SrMovimentacao movimentacao) throws Exception {
        movimentacao.salvar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
        exibir(movimentacao.getSolicitacao().getIdSolicitacao(), todoOContexto(), ocultas());
    }

    public void fechar(Long id, String motivo) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(id);
        sol.fechar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(), motivo);
        exibir(sol.getIdSolicitacao(), todoOContexto(), ocultas());
    }

    public void excluir(Long id) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(id);
        sol.excluir();
        editar(null);
    }

    public void responderPesquisa(Long id) throws Exception {
        /*
         * SrSolicitacao sol = SrSolicitacao.findById(id); SrPesquisa pesquisa = sol.getPesquisaDesignada(); if (pesquisa == null) throw new
         * Exception("NÃ£o foi encontrada nenhuma pesquisa designada para esta solicitaÃ§Ã£o."); pesquisa = SrPesquisa.findById(pesquisa.idPesquisa); pesquisa = pesquisa.getPesquisaAtual(); render(id,
         * pesquisa);
         */
    }

    public void responderPesquisaGravar(Long id, Map<Long, String> respostaMap) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(id);
        sol.responderPesquisa(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(), respostaMap);
        exibir(id, todoOContexto(), ocultas());
    }

    public void termoAtendimento(Long id) throws Exception {
        SrSolicitacao solicitacao = SrSolicitacao.AR.findById(id);
        // render(solicitacao);
    }

    public void cancelar(Long id) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(id);
        sol.cancelar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
        exibir(id, todoOContexto(), ocultas());
    }

    public void deixarPendente(Long id, SrTipoMotivoPendencia motivo, String calendario, String horario, String detalheMotivo) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(id);
        sol.deixarPendente(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(), motivo, calendario, horario, detalheMotivo);
        exibir(id, todoOContexto(), ocultas());
    }

    public void alterarPrazo(Long id, String motivo, String calendario, String horario) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(id);
        sol.alterarPrazo(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(), motivo, calendario, horario);
        exibir(id, todoOContexto(), ocultas());
    }

    public void terminarPendencia(Long id, String descricao, Long idMovimentacao) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(id);
        sol.terminarPendencia(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular(), descricao, idMovimentacao);
        exibir(id, todoOContexto(), ocultas());
    }

    public void reabrir(Long id) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(id);
        sol.reabrir(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
        exibir(id, todoOContexto(), ocultas());
    }

    public void desfazerUltimaMovimentacao(Long id) throws Exception {
        SrSolicitacao sol = SrSolicitacao.AR.findById(id);
        sol.desfazerUltimaMovimentacao(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
        exibir(id, todoOContexto(), ocultas());
    }

    public void escalonar(Long id) throws Exception {
        SrSolicitacao solicitacao = SrSolicitacao.AR.findById(id);
        solicitacao.setTitular(getTitular());
        solicitacao.setLotaTitular(getLotaTitular());
        solicitacao = solicitacao.getSolicitacaoAtual();
        Map<SrAcao, List<SrTarefa>> acoesEAtendentes = solicitacao.getAcoesEAtendentes();
        // render(solicitacao, acoesEAtendentes);
    }

    public void escalonarGravar(Long id, Long itemConfiguracao, SrAcao acao, Long idAtendente, Long idAtendenteNaoDesignado, Long idDesignacao, SrTipoMotivoEscalonamento motivo, String descricao,
            Boolean criaFilha, Boolean fechadoAuto) throws Exception {
        if (itemConfiguracao == null || acao == null || acao.getIdAcao() == null || acao.getIdAcao().equals(0L))
            throw new Exception("Operacao nao permitida. Necessario informar um item de configuracao " + "e uma acao.");
        SrSolicitacao solicitacao = SrSolicitacao.AR.findById(id);

        DpLotacao atendenteNaoDesignado = null;
        DpLotacao atendente = null;
        if (idAtendente != null)
            atendente = ContextoPersistencia.em().find(DpLotacao.class, idAtendente);
        if (idAtendenteNaoDesignado != null)
            atendenteNaoDesignado = ContextoPersistencia.em().find(DpLotacao.class, idAtendenteNaoDesignado);

        if (criaFilha) {
            if (fechadoAuto != null) {
                solicitacao.setFechadoAutomaticamente(fechadoAuto);
                solicitacao.save();
            }
            SrSolicitacao filha = null;
            if (solicitacao.isFilha())
                filha = solicitacao.getSolicitacaoPai().criarFilhaSemSalvar();
            else
                filha = solicitacao.criarFilhaSemSalvar();
            filha.setItemConfiguracao(SrItemConfiguracao.AR.findById(itemConfiguracao));
            filha.setAcao(SrAcao.AR.findById(acao.getIdAcao()));
            filha.setDesignacao(SrConfiguracao.AR.findById(idDesignacao));
            filha.setDescrSolicitacao(descricao);
            if (idAtendenteNaoDesignado != null)
                filha.setAtendenteNaoDesignado(atendenteNaoDesignado);
            filha.salvar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
            exibir(filha.getIdSolicitacao(), todoOContexto(), ocultas());
        } else {
            SrMovimentacao mov = new SrMovimentacao(solicitacao);
            mov.setTipoMov(SrTipoMovimentacao.AR.findById(SrTipoMovimentacao.TIPO_MOVIMENTACAO_ESCALONAMENTO));
            mov.setItemConfiguracao(SrItemConfiguracao.AR.findById(itemConfiguracao));
            mov.setAcao(SrAcao.AR.findById(acao.getIdAcao()));
            mov.setLotaAtendente(atendenteNaoDesignado != null ? atendenteNaoDesignado : atendente);
            if (solicitacao.getAtendente() != null && !mov.getLotaAtendente().equivale(solicitacao.getAtendente().getLotacao()))
                mov.setAtendente(null);
            mov.setMotivoEscalonamento(motivo);
            mov.setDesignacao(SrConfiguracao.AR.findById(idDesignacao));
            mov.setDescrMovimentacao("Motivo: " + mov.getMotivoEscalonamento() + "; Item: " + mov.getItemConfiguracao().getTituloItemConfiguracao() + "; Ação: " + mov.getAcao().getTituloAcao()
                    + "; Atendente: " + mov.getLotaAtendente().getSigla());
            mov.salvar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
            exibir(solicitacao.getIdSolicitacao(), todoOContexto(), ocultas());
        }

    }

    @SuppressWarnings("unchecked")
    public void listarDesignacao(boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        // List<SrConfiguracao> designacoes = new ArrayList<SrConfiguracao>();
        List<SrConfiguracao> designacoes = SrConfiguracao.listarDesignacoes(mostrarDesativados, null);
        List<CpOrgaoUsuario> orgaos = ContextoPersistencia.em().createQuery("from CpOrgaoUsuario").getResultList();
        List<CpComplexo> locais = CpComplexo.AR.all().fetch();

        List<SrPesquisa> pesquisaSatisfacao = SrPesquisa.AR.find("hisDtFim is null").fetch();

        // render(designacoes, orgaos, locais, pesquisaSatisfacao);
    }

    public void listarDesignacaoDesativados() throws Exception {
        listarDesignacao(Boolean.TRUE);
    }

    public String gravarDesignacao(SrConfiguracao designacao) throws Exception {
        assertAcesso("ADM:Administrar");
        validarFormEditarDesignacao(designacao);
        designacao.salvarComoDesignacao();
        // designacao.refresh();
        return designacao.getSrConfiguracaoJson();
    }

    public String gravarDesignacaoItem(SrConfiguracao designacao, Long idItemConfiguracao) throws Exception {
        assertAcesso("ADM:Administrar");
        designacao.salvarComoDesignacao();
        // designacao.refresh();

        SrItemConfiguracao itemConfiguracao = SrItemConfiguracao.AR.findById(idItemConfiguracao);
        itemConfiguracao.adicionarDesignacao(designacao);
        return designacao.getSrConfiguracaoJson(itemConfiguracao);
    }

    public String desativarDesignacao(Long id, boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        SrConfiguracao designacao = ContextoPersistencia.em().find(SrConfiguracao.class, id);
        designacao.finalizar();

        return designacao.getSrConfiguracaoJson();
    }

    public String buscarAbrangenciasAcordo(Long id) throws Exception {
        SrAcordo acordo = new SrAcordo();
        if (id != null)
            acordo = SrAcordo.AR.findById(id);
        List<SrConfiguracao> abrangencias = SrConfiguracao.listarAbrangenciasAcordo(Boolean.FALSE, acordo);
        return SrConfiguracao.convertToJSon(abrangencias);
    }

    public String buscarAbrangenciasAcordoDesativados(Long id) throws Exception {
        SrAcordo acordo = new SrAcordo();
        if (id != null)
            acordo = SrAcordo.AR.findById(id);
        List<SrConfiguracao> abrangencias = SrConfiguracao.listarAbrangenciasAcordo(Boolean.TRUE, acordo);
        return SrConfiguracao.convertToJSon(abrangencias);
    }

    public String gravarAcordo(SrAcordo acordo) throws Exception {
        assertAcesso("ADM:Administrar");
        acordo.salvar();

        return acordo.toJson();
    }

    public String desativarAcordo(Long id, boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        SrAcordo acordo = SrAcordo.AR.findById(id);
        acordo.finalizar();

        return acordo.toJson();
    }

    public String reativarAcordo(Long id, boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        SrAcordo acordo = SrAcordo.AR.findById(id);
        acordo.salvar();

        return acordo.toJson();
    }

    public String gravarAbrangencia(SrConfiguracao associacao) throws Exception {
        assertAcesso("ADM:Administrar");
        associacao.salvarComoAbrangenciaAcordo();
        // associacao.refresh();
        return associacao.getSrConfiguracaoJson();
    }

    public void desativarAbrangenciaEdicao(Long idAcordo, Long idAssociacao) throws Exception {
        assertAcesso("ADM:Administrar");
        SrConfiguracao abrangencia = ContextoPersistencia.em().find(SrConfiguracao.class, idAssociacao);
        abrangencia.finalizar();
    }

    public void reativarAbrangencia(Long id, boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        SrConfiguracao associacao = ContextoPersistencia.em().find(SrConfiguracao.class, id);
        associacao.salvar();
    }

    public String reativarDesignacao(Long id, boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        SrConfiguracao designacao = ContextoPersistencia.em().find(SrConfiguracao.class, id);
        designacao.salvar();

        return designacao.getSrConfiguracaoJson();
    }

    public void selecionarAcordo(String sigla) throws Exception {
        SrAcordo sel = new SrAcordo().selecionar(sigla);
        // render("@selecionar", sel);
    }

    @SuppressWarnings("unchecked")
    public void buscarAcordo(String nome, boolean popup, boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");

        List<SrAtributo> parametros = SrAtributo.listarParaAcordo(false);
        List<CpUnidadeMedida> unidadesMedida = CpDao.getInstance().listarUnidadesMedida();
        List<CpOrgaoUsuario> orgaos = ContextoPersistencia.em().createQuery("from CpOrgaoUsuario").getResultList();
        List<CpComplexo> locais = CpComplexo.AR.all().fetch();
        List<SrAcordo> acordos = SrAcordo.listar(mostrarDesativados);
        // render(acordos, nome, popup, mostrarDesativados, parametros, unidadesMedida, orgaos, locais);
    }

    public void buscarAcordoDesativadas() throws Exception {
        buscarAcordo(null, false, true);
    }

    @SuppressWarnings("unchecked")
    public void editarPermissaoUsoLista(Long id) throws Exception {
        assertAcesso("ADM:Administrar");
        List<CpOrgaoUsuario> orgaos = ContextoPersistencia.em().createQuery("from CpOrgaoUsuario").getResultList();
        List<CpComplexo> locais = CpComplexo.AR.all().fetch();
        List<SrLista> listasPrioridade = SrLista.getCriadasPelaLotacao(getLotaTitular());
        SrConfiguracao permissao = new SrConfiguracao();
        if (id != null)
            permissao = ContextoPersistencia.em().find(SrConfiguracao.class, id);
        // render(permissao, orgaos, locais, listasPrioridade);
    }

    public String listarPermissaoUsoListaDesativados(Long idLista) throws Exception {
        SrLista lista = new SrLista();
        if (idLista != null)
            lista = SrLista.AR.findById(idLista);
        List<SrConfiguracao> associacoes = SrConfiguracao.listarPermissoesUsoLista(lista, Boolean.TRUE);
        return SrConfiguracao.convertToJSon(associacoes);
    }

    public String listarPermissaoUsoLista(Long idLista) throws Exception {
        assertAcesso("ADM:Administrar");

        SrLista lista = new SrLista();
        if (idLista != null)
            lista = SrLista.AR.findById(idLista);
        List<SrConfiguracao> associacoes = SrConfiguracao.listarPermissoesUsoLista(lista, Boolean.FALSE);
        return SrConfiguracao.convertToJSon(associacoes);
    }

    public String gravarPermissaoUsoLista(SrConfiguracao permissao) throws Exception {
        assertAcesso("ADM:Administrar");
        validarFormEditarPermissaoUsoLista(permissao);
        permissao.salvarComoPermissaoUsoLista();
        // permissao.refresh();
        return permissao.toVO().toJson();
    }

    public void desativarPermissaoUsoLista(Long id) throws Exception {
        assertAcesso("ADM:Administrar");
        SrConfiguracao designacao = ContextoPersistencia.em().find(SrConfiguracao.class, id);
        designacao.finalizar();
    }

    public String desativarPermissaoUsoListaEdicao(Long idLista, Long idPermissao) throws Exception {
        assertAcesso("ADM:Administrar");
        SrConfiguracao configuracao = ContextoPersistencia.em().find(SrConfiguracao.class, idPermissao);
        configuracao.finalizar();

        return configuracao.getSrConfiguracaoJson();
    }

    public String gravarAssociacao(SrConfiguracao associacao) throws Exception {
        assertAcesso("ADM:Administrar");
        associacao.salvarComoAssociacaoAtributo();
        // associacao.refresh();
        return associacao.toVO().toJson();
    }

    public String gravarAssociacaoPesquisa(SrConfiguracao associacao) throws Exception {
        assertAcesso("ADM:Administrar");
        associacao.salvarComoAssociacaoPesquisa();
        // associacao.refresh();
        return associacao.toVO().toJson();
    }

    public void desativarAssociacaoEdicao(Long idAtributo, Long idAssociacao) throws Exception {
        assertAcesso("ADM:Administrar");
        SrConfiguracao associacao = ContextoPersistencia.em().find(SrConfiguracao.class, idAssociacao);
        associacao.finalizar();
    }

    public void desativarAssociacaoPesquisaEdicao(Long idPesquisa, Long idAssociacao) throws Exception {
        assertAcesso("ADM:Administrar");
        SrConfiguracao associacao = ContextoPersistencia.em().find(SrConfiguracao.class, idAssociacao);
        associacao.finalizar();
    }

    public void desativarAssociacao(Long id, boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        SrConfiguracao associacao = ContextoPersistencia.em().find(SrConfiguracao.class, id);
        associacao.finalizar();
    }

    public void reativarAssociacao(Long id, boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        SrConfiguracao associacao = ContextoPersistencia.em().find(SrConfiguracao.class, id);
        associacao.salvar();
    }

    @SuppressWarnings("unchecked")
    public void listarItem(boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        List<SrItemConfiguracao> itens = SrItemConfiguracao.listar(mostrarDesativados);

        List<CpOrgaoUsuario> orgaos = ContextoPersistencia.em().createQuery("from CpOrgaoUsuario").getResultList();
        List<CpComplexo> locais = CpComplexo.AR.all().fetch();
        List<CpUnidadeMedida> unidadesMedida = CpDao.getInstance().listarUnidadesMedida();
        List<SrPesquisa> pesquisaSatisfacao = SrPesquisa.AR.find("hisDtFim is null").fetch();

        // render(itens, mostrarDesativados, orgaos, locais, unidadesMedida, pesquisaSatisfacao);
    }

    public void listarItemDesativados() throws Exception {
        listarItem(Boolean.TRUE);
    }

    public String buscarDesignacoesItem(Long id) throws Exception {
        List<SrConfiguracao> designacoes;

        if (id != null) {
            SrItemConfiguracao itemConfiguracao = SrItemConfiguracao.AR.findById(id);
            designacoes = new ArrayList<SrConfiguracao>(itemConfiguracao.getDesignacoesAtivas());
            designacoes.addAll(itemConfiguracao.getDesignacoesPai());
        } else
            designacoes = new ArrayList<SrConfiguracao>();

        return SrConfiguracao.convertToJSon(designacoes);
    }

    public String buscarDesignacoesEquipe(Long id) throws Exception {
        List<SrConfiguracao> designacoes;

        if (id != null) {
            SrEquipe equipe = SrEquipe.AR.findById(id);
            designacoes = new ArrayList<SrConfiguracao>(equipe.getDesignacoes());
        } else
            designacoes = new ArrayList<SrConfiguracao>();

        return SrConfiguracao.convertToJSon(designacoes);
    }

    /**
     * Recupera as {@link SrConfiguracao permissoes} de uma {@link SrLista lista}.
     *
     * @param idObjetivo
     *            - ID da lista
     * @return - String contendo a lista no formato jSon
     */
    public String buscarPermissoesLista(Long idLista) throws Exception {
        List<SrConfiguracao> permissoes;

        if (idLista != null) {
            SrLista lista = SrLista.AR.findById(idLista);

            // permissoes = new ArrayList<SrConfiguracao>(lista.getPermissoes(lotaTitular(), cadastrante()));
            permissoes = SrConfiguracao.listarPermissoesUsoLista(lista, false);
        } else
            permissoes = new ArrayList<SrConfiguracao>();

        return SrConfiguracao.convertToJSon(permissoes);
    }

    public String gravarItem(SrItemConfiguracao itemConfiguracao) throws Exception {
        assertAcesso("ADM:Administrar");
        validarFormEditarItem(itemConfiguracao);
        itemConfiguracao.salvar();

        // Atualiza os conhecimentos relacionados
        // Edson: deveria ser feito por webservice. Nao estah sendo coberta
        // a atualizacao da classificacao quando ocorre mudanca de posicao na
        // hierarquia, pois isso eh mais complexo de acertar.
        // try {
        // HashMap<String, String> atributos = new HashMap<String, String>();
        // for (Http.Header h : request.headers.values())
        // if (!h.name.equals("content-type"))
        // atributos.put(h.name, h.value());
        //
        // SrItemConfiguracao anterior = null;
        // List<SrItemConfiguracao> itens = itemConfiguracao.getHistoricoItemConfiguracao();
        // if(itens != null)
        // anterior = itens.get(0);
        // if (anterior != null
        // && !itemConfiguracao.tituloItemConfiguracao
        // .equals(anterior.tituloItemConfiguracao))
        // ConexaoHTTP.get("http://"
        // + Play.configuration.getProperty("servidor.principal")
        // + ":8080/sigagc/app/updateTag?before="
        // + anterior.getTituloSlugify() + "&after="
        // + itemConfiguracao.getTituloSlugify(), atributos);
        // } catch (Exception e) {
        // Logger.error("Item " + itemConfiguracao.idItemConfiguracao
        // + " salvo, mas nao foi possivel atualizar conhecimento");
        // e.printStackTrace();
        // }
        //
        // itemConfiguracao.refresh();
        return itemConfiguracao.getSrItemConfiguracaoJson();
    }

    public String desativarItem(Long id, boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        SrItemConfiguracao item = SrItemConfiguracao.AR.findById(id);
        item.finalizar();

        return item.getSrItemConfiguracaoJson();
    }

    public String reativarItem(Long id, boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        SrItemConfiguracao item = SrItemConfiguracao.AR.findById(id);
        item.salvar();
        return item.getSrItemConfiguracaoJson();
    }

    public void selecionarItem(String sigla, SrSolicitacao sol) throws Exception {
        SrItemConfiguracao sel = new SrItemConfiguracao().selecionar(sigla, sol.getItensDisponiveis());
        // render("@selecionar", sel);
    }

    public void buscarItem(String sigla, String nome, SrItemConfiguracao filtro, SrSolicitacao sol) {

        List<SrItemConfiguracao> itens = null;

        try {
            if (filtro == null)
                filtro = new SrItemConfiguracao();
            if (sigla != null && !sigla.trim().equals(""))
                filtro.setSigla(sigla);
            itens = filtro.buscar(sol != null && (sol.getSolicitante() != null || sol.getLocal() != null) ? sol.getItensDisponiveis() : null);
        } catch (Exception e) {
            itens = new ArrayList<SrItemConfiguracao>();
        }

        // render(itens, filtro, nome, sol);
    }

    public String buscarAssociacaoPesquisa(Long idPesquisa) throws Exception {
        SrPesquisa pesq = SrPesquisa.AR.findById(idPesquisa);

        if (pesq != null)
            return pesq.toJson(true);
        else
            return "";
    }

    public String buscarAssociacaoAtributo(Long idAtributo) throws Exception {
        SrAtributo attr = SrAtributo.AR.findById(idAtributo);

        if (attr != null)
            return attr.toJson(true);
        else
            return "";
    }

    public void listarAtributoDesativados() throws Exception {
        listarAtributo(Boolean.TRUE);
    }

    @SuppressWarnings("unchecked")
    public void listarAtributo(boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        List<SrAtributo> atts = SrAtributo.listar(null, mostrarDesativados);
        List<SrObjetivoAtributo> objetivos = SrObjetivoAtributo.AR.all().fetch();
        List<CpOrgaoUsuario> orgaos = ContextoPersistencia.em().createQuery("from CpOrgaoUsuario").getResultList();
        List<CpComplexo> locais = CpComplexo.AR.all().fetch();
        // render(atts, objetivos, orgaos, locais);
    }

    public void editarAtributo(Long id) throws Exception {
        assertAcesso("ADM:Administrar");
        String tipoAtributoAnterior = null;
        SrAtributo att = new SrAtributo();
        if (id != null) {
            att = SrAtributo.AR.findById(id);
            if (att.getTipoAtributo() != null) {
                tipoAtributoAnterior = att.getTipoAtributo().name();
            }
        }
        if (att.getObjetivoAtributo() == null)
            att.setObjetivoAtributo(SrObjetivoAtributo.AR.findById(SrObjetivoAtributo.OBJETIVO_SOLICITACAO));
        List<SrConfiguracao> associacoes = SrConfiguracao.listarAssociacoesAtributo(att, Boolean.FALSE);
        List<SrObjetivoAtributo> objetivos = SrObjetivoAtributo.AR.all().fetch();
        // render(att, tipoAtributoAnterior, associacoes, objetivos);
    }

    public String listarAssociacaoAtributoDesativados(Long idAtributo) throws Exception {
        SrAtributo att = new SrAtributo();
        if (idAtributo != null)
            att = SrAtributo.AR.findById(idAtributo);
        List<SrConfiguracao> associacoes = SrConfiguracao.listarAssociacoesAtributo(att, Boolean.TRUE);
        return SrConfiguracao.convertToJSon(associacoes);
    }

    public String listarAssociacaoAtributo(Long idAtributo) throws Exception {
        assertAcesso("ADM:Administrar");

        SrAtributo att = new SrAtributo();
        if (idAtributo != null)
            att = SrAtributo.AR.findById(idAtributo);
        List<SrConfiguracao> associacoes = SrConfiguracao.listarAssociacoesAtributo(att, Boolean.FALSE);
        return SrConfiguracao.convertToJSon(associacoes);
    }

    public String gravarAtributo(SrAtributo atributo) throws Exception {
        assertAcesso("ADM:Administrar");
        validarFormEditarAtributo(atributo);
        atributo.salvar();
        return atributo.toVO(false).toJson();
    }

    @SuppressWarnings("static-access")
    private void validarFormEditarAtributo(SrAtributo atributo) {
        if (atributo.getTipoAtributo() == SrTipoAtributo.VL_PRE_DEFINIDO && atributo.getDescrPreDefinido().equals("")) {
            srValidator.addError("att.descrPreDefinido", "Valores Pré-definido não informados");
        }

        if (srValidator.hasErrors()) {
            enviarErroValidacao();
        }
    }

    public String desativarAtributo(Long id, boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        SrAtributo item = SrAtributo.AR.findById(id);
        item.finalizar();

        return item.toJson(false);
    }

    public String reativarAtributo(Long id, boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        SrAtributo item = SrAtributo.AR.findById(id);
        item.salvar();
        return item.toJson(false);
    }

    @SuppressWarnings("unchecked")
    public void listarPesquisa(boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        List<SrPesquisa> pesquisas = SrPesquisa.listar(mostrarDesativados);
        List<SrTipoPergunta> tipos = SrTipoPergunta.buscarTodos();
        List<CpOrgaoUsuario> orgaos = ContextoPersistencia.em().createQuery("from CpOrgaoUsuario").getResultList();
        List<CpComplexo> locais = CpComplexo.AR.all().fetch();
        // render(pesquisas, tipos, orgaos, locais);
    }

    public void listarPesquisaDesativadas() throws Exception {
        listarPesquisa(Boolean.TRUE);
    }

    public void editarPesquisa(Long id) throws Exception {
        assertAcesso("ADM:Administrar");
        SrPesquisa pesq = new SrPesquisa();
        if (id != null)
            pesq = SrPesquisa.AR.findById(id);
        List<SrTipoPergunta> tipos = SrTipoPergunta.AR.all().fetch();
        // render(pesq, tipos);
    }

    public String listarAssociacaoPesquisaDesativados(Long idPesquisa) throws Exception {
        SrPesquisa pesquisa = new SrPesquisa();
        if (idPesquisa != null)
            pesquisa = SrPesquisa.AR.findById(idPesquisa);
        List<SrConfiguracao> associacoes = SrConfiguracao.listarAssociacoesPesquisa(pesquisa, Boolean.TRUE);
        return SrConfiguracao.convertToJSon(associacoes);
    }

    public String listarAssociacaoPesquisa(Long idPesquisa) throws Exception {
        assertAcesso("ADM:Administrar");

        SrPesquisa pesquisa = new SrPesquisa();
        if (idPesquisa != null)
            pesquisa = SrPesquisa.AR.findById(idPesquisa);

        List<SrConfiguracao> associacoes = SrConfiguracao.listarAssociacoesPesquisa(pesquisa, Boolean.FALSE);
        return SrConfiguracao.convertToJSon(associacoes);
    }

    public String gravarPesquisa(SrPesquisa pesquisa, List<SrPergunta> perguntaSet) throws Exception {
        assertAcesso("ADM:Administrar");
        pesquisa = (SrPesquisa) Objeto.getImplementation(pesquisa);
        pesquisa.setPerguntaSet((pesquisa.getPerguntaSet() != null) ? pesquisa.getPerguntaSet() : new ArrayList<SrPergunta>());
        pesquisa.salvar();

        return pesquisa.atualizarTiposPerguntas().toJson();
    }

    public String desativarPesquisa(Long id, boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        SrPesquisa pesq = SrPesquisa.AR.findById(id);
        pesq.finalizar();

        return pesq.toJson();
    }

    public String reativarPesquisa(Long id, boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        SrPesquisa pesq = SrPesquisa.AR.findById(id);
        pesq.salvar();

        return pesq.toJson();
    }

    public void listarConhecimento(Long idItem, Long idAcao, boolean ajax) throws Exception {
        assertAcesso("EDTCONH:Criar Conhecimentos");
        SrItemConfiguracao item = idItem != null ? (SrItemConfiguracao) SrItemConfiguracao.AR.findById(idItem) : null;
        SrAcao acao = idAcao != null ? (SrAcao) SrAcao.AR.findById(idAcao) : null;
        // render("@listarConhecimento" + (ajax ? "Ajax" : ""), item, acao);
    }

    public void listarDisponibilidadeItens() {
        // render();
    }

    @SuppressWarnings("unchecked")
    public String listarPaginaDisponibilidade(PaginaItemConfiguracao pagina) throws Exception {
        List<CpOrgaoUsuario> orgaos = ContextoPersistencia.em().createQuery("from CpOrgaoUsuario").getResultList();

        return pagina.atualizar(orgaos).toJson();
    }

    public String gravarDisponibilidade(SrDisponibilidade disponibilidade, PaginaItemConfiguracao pagina) throws Exception {
        disponibilidade.salvar(pagina);
        return disponibilidade.toJsonObject().toString();
    }

    @SuppressWarnings("unchecked")
    public void listarEquipe(boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        List<SrEquipe> listaEquipe = SrEquipe.listar(mostrarDesativados);

        List<CpOrgaoUsuario> orgaos = ContextoPersistencia.em().createQuery("from CpOrgaoUsuario").getResultList();
        List<CpComplexo> locais = CpComplexo.AR.all().fetch();
        List<CpUnidadeMedida> unidadesMedida = CpDao.getInstance().listarUnidadesMedida();
        List<SrPesquisa> pesquisaSatisfacao = SrPesquisa.AR.find("hisDtFim is null").fetch();
        SelecionavelVO lotacaoUsuario = SelecionavelVO.createFrom(getLotaTitular());

        // render(listaEquipe, orgaos, locais, unidadesMedida, pesquisaSatisfacao, lotacaoUsuario);
    }

    public String gravarEquipe(SrEquipe equipe) throws Exception {
        assertAcesso("ADM:Administrar");
        equipe.salvar();
        return equipe.toJson();
    }

    public void listarAcao(boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        List<SrAcao> acoes = SrAcao.listar(mostrarDesativados);
        // render(acoes, mostrarDesativados);
    }

    public void listarAcaoDesativados() throws Exception {
        listarAcao(Boolean.TRUE);
    }

    public void editarAcao(Long id) throws Exception {
        assertAcesso("ADM:Administrar");
        SrAcao acao = new SrAcao();
        if (id != null)
            acao = SrAcao.AR.findById(id);
        // render(acao);
    }

    public String gravarAcao(SrAcao acao) throws Exception {
        assertAcesso("ADM:Administrar");
        validarFormEditarAcao(acao);
        acao.salvar();

        // Atualiza os conhecimentos relacionados.
        // Edson: deveria ser feito por webservice. Nao estah sendo coberta
        // a atualizacao da classificacao quando ocorre mudanca de posicao na
        // hierarquia, pois isso eh mais complexo de acertar.

        // Karina: Comentado pois precisa ser refatorado devido ao uso do ConexaoHTTP que está deprecated
        // try {
        // HashMap<String, String> atributos = new HashMap<String, String>();
        // for (Http.Header h : request.headers.values())
        // if (!h.name.equals("content-type"))
        // atributos.put(h.name, h.value());
        //
        // SrAcao anterior = acao
        // .getHistoricoAcao().get(0);
        // if (anterior != null
        // && !acao.tituloAcao
        // .equals(anterior.tituloAcao))
        // ConexaoHTTP.get("http://"
        // + Play.configuration.getProperty("servidor.principal")
        // + ":8080/sigagc/app/updateTag?before="
        // + anterior.getTituloSlugify() + "&after="
        // + acao.getTituloSlugify(), atributos);
        // } catch (Exception e) {
        // Logger.error("Acao " + acao.idAcao
        // + " salva, mas nao foi possivel atualizar conhecimento");
        // e.printStackTrace();
        // }
        return acao.toJson();
    }

    public String desativarAcao(Long id, boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        SrAcao acao = SrAcao.AR.findById(id);
        acao.finalizar();

        return acao.toJson();
    }

    public String reativarAcao(Long id, boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        SrAcao acao = SrAcao.AR.findById(id);
        acao.salvar();
        return acao.toJson();
    }

    public void selecionarAcao(String sigla, SrSolicitacao sol) throws Exception {

        SrAcao sel = new SrAcao().selecionar(sigla, sol.getAcoesDisponiveis());
        // render("@selecionar", sel);
    }

    public void buscarAcao(String sigla, String nome, SrAcao filtro, SrSolicitacao sol) {
        List<SrAcao> itens = null;

        try {
            if (filtro == null)
                filtro = new SrAcao();
            if (sigla != null && !sigla.trim().equals(""))
                filtro.setSigla(sigla);
            itens = filtro.buscar(sol != null && (sol.getSolicitante() != null || sol.getLocal() != null) ? sol.getAcoesDisponiveis() : null);
        } catch (Exception e) {
            itens = new ArrayList<SrAcao>();
        }

        // render(itens, filtro, nome, sol);
    }

    public void listarTipoAcao(boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        List<SrTipoAcao> tiposAcao = SrTipoAcao.listar(mostrarDesativados);
        // render(tiposAcao, mostrarDesativados);
    }

    public void listarTipoAcaoDesativados() throws Exception {
        listarTipoAcao(Boolean.TRUE);
    }

    public void editarTipoAcao(Long id) throws Exception {
        assertAcesso("ADM:Administrar");
        SrTipoAcao tipoAcao = new SrTipoAcao();
        if (id != null)
            tipoAcao = SrTipoAcao.AR.findById(id);
        // render(tipoAcao);
    }

    public String gravarTipoAcao(SrTipoAcao tipoAcao) throws Exception {
        assertAcesso("ADM:Administrar");
        validarFormEditarTipoAcao(tipoAcao);
        tipoAcao.salvar();
        return tipoAcao.toJson();
    }

    public void desativarTipoAcao(Long id, boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        SrTipoAcao tipoAcao = SrTipoAcao.AR.findById(id);
        tipoAcao.finalizar();
    }

    public Long reativarTipoAcao(Long id, boolean mostrarDesativados) throws Exception {
        assertAcesso("ADM:Administrar");
        SrTipoAcao tipoAcao = SrTipoAcao.AR.findById(id);
        tipoAcao.salvar();
        return tipoAcao.getId();
    }

    public void selecionarTipoAcao(String sigla, SrSolicitacao sol) throws Exception {

        SrTipoAcao sel = new SrTipoAcao().selecionar(sigla);
        // render("@selecionar", sel);
    }

    public void buscarTipoAcao(String sigla, String nome, SrTipoAcao filtro) {
        List<SrTipoAcao> itens = null;

        try {
            if (filtro == null)
                filtro = new SrTipoAcao();
            if (sigla != null && !sigla.trim().equals(""))
                filtro.setSigla(sigla);
            itens = filtro.buscar();
        } catch (Exception e) {
            itens = new ArrayList<SrTipoAcao>();
        }

        // render(itens, filtro, nome);
    }

    public void selecionarSiga(String sigla, String prefixo, String tipo, String nome) throws Exception {
        // redirect("/siga/" + (prefixo != null ? prefixo + "/" : "") + tipo + "/selecionar.action?" + "propriedade="
        // + tipo + nome + "&sigla=" + URLEncoder.encode(sigla, "UTF-8"));
    }

    public void buscarSiga(String sigla, String prefixo, String tipo, String nome) throws Exception {
        // redirect("/siga/" + (prefixo != null ? prefixo + "/" : "") + tipo + "/buscar.action?" + "propriedade=" + tipo
        // + nome + "&sigla=" + URLEncoder.encode(sigla, "UTF-8"));
    }

    @SuppressWarnings("unchecked")
    public void listarLista(boolean mostrarDesativados) throws Exception {
        List<CpOrgaoUsuario> orgaos = ContextoPersistencia.em().createQuery("from CpOrgaoUsuario").getResultList();
        List<CpComplexo> locais = CpComplexo.AR.all().fetch();
        List<SrTipoPermissaoLista> tiposPermissao = SrTipoPermissaoLista.AR.all().fetch();
        List<SrLista> listas = SrLista.listar(mostrarDesativados);
        String tiposPermissaoJson = new Gson().toJson(tiposPermissao);

        // render(listas, mostrarDesativados, orgaos, locais, tiposPermissao, tiposPermissaoJson);
    }

    public void listarListaDesativados() throws Exception {
        listarLista(Boolean.TRUE);
    }

    public String gravarLista(SrLista lista) throws Exception {
        lista.setLotaCadastrante(getLotaTitular());
        validarFormEditarLista(lista);
        lista.salvar();
        return lista.toJson();
    }

    private void validarFormEditarLista(SrLista lista) {
        if (lista.getNomeLista() == null || lista.getNomeLista().trim().equals("")) {
            srValidator.addError("lista.nomeLista", "Nome da Lista nÃ£o informados");
        }

        if (srValidator.hasErrors()) {
            enviarErroValidacao();
        }
    }

    public String desativarLista(Long id, boolean mostrarDesativados) throws Exception {
        SrLista lista = SrLista.AR.findById(id);
        lista.finalizar();

        return lista.toJson();
    }

    public String reativarLista(Long id, boolean mostrarDesativados) throws Exception {
        SrLista lista = SrLista.AR.findById(id);
        lista.salvar();
        return lista.toJson();
    }

    public void exibirPrioridade(SrSolicitacao solicitacao) {
        solicitacao.associarPrioridadePeloGUT();
        // render(solicitacao);
    }

    public void atualizarFechamentoAutomatico() throws Exception {
        List<SrSolicitacao> todasSolicitacoes = SrSolicitacao.AR.findAll();
        for (SrSolicitacao sol : todasSolicitacoes) {
            if (sol.isPai()) {
                sol.setFechadoAutomaticamente(false);
                sol.salvar(getCadastrante(), getCadastrante().getLotacao(), getTitular(), getLotaTitular());
            }
        }
        // renderText("AtualizaÃ§Ã£o realizada com sucesso");
    }

    public String configuracoesParaInclusaoAutomatica(Long idLista, boolean mostrarDesativados) throws Exception {
        SrLista lista = SrLista.AR.findById(idLista);
        return SrConfiguracao.buscaParaConfiguracaoInsercaoAutomaticaListaJSON(lista.getListaAtual(), mostrarDesativados);
    }

    public String configuracaoAutomaticaGravar(SrConfiguracao configuracaoInclusaoAutomatica) throws Exception {
        configuracaoInclusaoAutomatica.salvarComoInclusaoAutomaticaLista(configuracaoInclusaoAutomatica.getListaPrioridade());
        // configuracaoInclusaoAutomatica.refresh();
        return configuracaoInclusaoAutomatica.toVO().toJson();
    }

    public String desativarConfiguracaoAutomaticaGravar(Long id) throws Exception {
        SrConfiguracao configuracao = ContextoPersistencia.em().find(SrConfiguracao.class, id);
        configuracao.finalizar();
        return configuracao.toVO().toJson();
    }

    public String reativarConfiguracaoAutomaticaGravar(Long id) throws Exception {
        SrConfiguracao configuracao = ContextoPersistencia.em().find(SrConfiguracao.class, id);
        configuracao.salvar();
        return configuracao.toVO().toJson();
    }
}
