package br.gov.jfrj.siga.wf.vraptor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.SessionFactory;
import org.jbpm.graph.def.ProcessDefinition;

import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.cp.CpConfiguracao;
import br.gov.jfrj.siga.cp.CpSituacaoConfiguracao;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import br.gov.jfrj.siga.wf.Permissao;
import br.gov.jfrj.siga.wf.WfConfiguracao;
import br.gov.jfrj.siga.wf.bl.Wf;
import br.gov.jfrj.siga.wf.dao.WfDao;
import br.gov.jfrj.siga.wf.util.WfContextBuilder;
import br.gov.jfrj.siga.wf.util.WfTipoResponsavel;

@Resource
public class ConfiguracaoController extends WfController {

	private static int TIPO_RESP_INDEFINIDO = 0;
	private static int TIPO_RESP_MATRICULA = 1;
	private static int TIPO_RESP_LOTACAO = 2;

	private List<WfTipoResponsavel> listaTipoResponsavel = new ArrayList<WfTipoResponsavel>();

	/**
	 * Inicializa os tipos de responsáveis e suas respectivas expressões, quando
	 * houver.
	 */
	public ConfiguracaoController(HttpServletRequest request, Result result,
			WfDao dao, SigaObjects so, WfUtil util) {
		super(request, result, dao, so, util);

		WfTipoResponsavel tpIndefinido = new WfTipoResponsavel(
				TIPO_RESP_INDEFINIDO, "[Indefinido]", "");
		WfTipoResponsavel tpMatricula = new WfTipoResponsavel(
				TIPO_RESP_MATRICULA, "Matrícula", "matricula");
		WfTipoResponsavel tpLotacao = new WfTipoResponsavel(TIPO_RESP_LOTACAO,
				"Lotação", "lotacao");

		listaTipoResponsavel.add(tpIndefinido);
		listaTipoResponsavel.add(tpMatricula);
		listaTipoResponsavel.add(tpLotacao);
	}

	/**
	 * Grava a configuração da permissão. Primeiro, processa-se as alteraões nas
	 * permissões existentes e depois processa-se as novas permissões. As
	 * permissões indefinidas são excluídas da lista de permissões.
	 * 
	 * @return
	 * @throws Exception
	 */
	public void gravar(String orgao, String procedimento) throws Exception {
		ProcessDefinition pd = WfContextBuilder.getJbpmContext()
				.getJbpmContext().getGraphSession()
				.findLatestProcessDefinition(procedimento);

		CpOrgaoUsuario orgaoUsuario = daoOU(orgao);

		Date horaDoDB = dao().consultarDataEHoraDoServidor();
		if (pd != null) {

			// processa permissões existentes
			List<Permissao> listaPermissao = getPermissoes(orgaoUsuario,
					procedimento);
			for (Permissao perm : listaPermissao) {
				WfConfiguracao cfg = prepararConfiguracao(orgaoUsuario,
						procedimento);

				processarPermissao(orgaoUsuario, procedimento, perm, cfg,
						horaDoDB);

			}

			// processa nova permissão
			WfConfiguracao cfg = prepararConfiguracao(orgaoUsuario,
					procedimento);
			Permissao novaPermissao = new Permissao();
			novaPermissao.setId(this.getIdNovaPermissao());
			novaPermissao.setProcedimento(procedimento);

			processarPermissao(orgaoUsuario, procedimento, novaPermissao, cfg,
					horaDoDB);

			if (novaPermissao.getPessoa() != null
					|| novaPermissao.getLotacao() != null) {
				listaPermissao.add(novaPermissao);
			}

			// remove permissões inválidas
			ArrayList<Permissao> listaAux = new ArrayList<Permissao>();
			for (Permissao perm : listaPermissao) {
				if (perm.getPessoa() != null || perm.getLotacao() != null) {
					listaAux.add(perm);
				}
			}

			listaPermissao = listaAux;

			limparCache();
		}

		result.redirectTo(this).pesquisar(orgao, procedimento);
	}

	/**
	 * Seleciona um procedimento que terá suas permissões configuradas.
	 * 
	 * @return
	 * @throws Exception
	 */
	public void pesquisar(String orgao, String procedimento) throws Exception {
		assertAcesso(ACESSO_CONFIGURAR_INICIADORES);

		limparCache();
		ProcessDefinition pd = WfContextBuilder.getJbpmContext()
				.getJbpmContext().getGraphSession()
				.findLatestProcessDefinition(procedimento);
		CpOrgaoUsuario orgaoUsuario = daoOU(orgao);

		if (pd != null) {
			List<Permissao> listaPermissao = new ArrayList<Permissao>();
			listaPermissao.addAll(getPermissoes(orgaoUsuario, procedimento));
			result.include("orgao", orgao);
			result.include("procedimento", procedimento);
			result.include("listaPermissao", listaPermissao);
			result.include("listaOrgao", dao().listarOrgaosUsuarios());
			result.include("listaTipoResponsavel", listaTipoResponsavel);
			result.include("listaProcedimento", getListaProcedimento());
		}
	}

	/**
	 * Retorna a lista de configurações definidas para uma permissão.
	 * 
	 * @param perm
	 *            -
	 * @return Lista de permissões já gravadas no banco de dados.
	 * @throws Exception
	 */
	private List<WfConfiguracao> getConfiguracaoExistente(
			CpOrgaoUsuario orgaoUsuario, String procedimento, Permissao perm)
			throws Exception {
		WfConfiguracao fltConfigExistente = new WfConfiguracao();

		CpTipoConfiguracao tipoConfig = CpDao.getInstance().consultar(
				CpTipoConfiguracao.TIPO_CONFIG_INSTANCIAR_PROCEDIMENTO,
				CpTipoConfiguracao.class, false);

		fltConfigExistente.setCpTipoConfiguracao(tipoConfig);
		fltConfigExistente.setOrgaoUsuario(orgaoUsuario);
		fltConfigExistente.setProcedimento(procedimento);
		fltConfigExistente.setDpPessoa(perm.getPessoa());
		fltConfigExistente.setLotacao(perm.getLotacao());

		List<WfConfiguracao> cfgExistente = WfDao.getInstance().consultar(
				fltConfigExistente);
		List<WfConfiguracao> resultado = new ArrayList<WfConfiguracao>();
		// Melhorar isso: o filtro por pessoa ou lotação não está funcionando
		for (WfConfiguracao c : cfgExistente) {
			if ((c.getDpPessoa() != null || c.getLotacao() != null)
					&& c.getHisDtFim() == null
					&& c.getIdConfiguracao().equals(perm.getId())) {
				resultado.add(c);
			}
		}

		return resultado;
	}

	/**
	 * Retorna a lista de permissões definidas para uma definição de
	 * procedimento.
	 * 
	 * @param pd
	 *            - Definição de processo
	 * @return - Lista de permissões
	 * @throws Exception
	 */
	private List<Permissao> getPermissoes(CpOrgaoUsuario orgaoUsuario,
			String procedimento) throws Exception {

		List<Permissao> resultado = new ArrayList<Permissao>();

		TreeSet<CpConfiguracao> cfg = Wf
				.getInstance()
				.getConf()
				.getListaPorTipo(
						CpTipoConfiguracao.TIPO_CONFIG_INSTANCIAR_PROCEDIMENTO);
		for (CpConfiguracao c : cfg) {

			if (c instanceof WfConfiguracao) {
				WfConfiguracao wfC = (WfConfiguracao) c;
				if (wfC.getProcedimento() != null
						&& wfC.getProcedimento().equals(procedimento)
						&& wfC.getHisDtFim() == null) {

					Permissao perm = new Permissao();
					perm.setProcedimento(procedimento);
					perm.setId(c.getIdConfiguracao());

					perm.setPessoa(c.getDpPessoa());
					perm.setLotacao(c.getLotacao());

					if (perm.getPessoa() != null) {
						perm.setTipoResponsavel(TIPO_RESP_MATRICULA);
					}
					if (perm.getLotacao() != null) {
						perm.setTipoResponsavel(TIPO_RESP_LOTACAO);
					}

					resultado.add(perm);
				}
			}
		}

		return resultado;

	}

	/**
	 * Método utilizado para gravar uma nova configuração. ATENÇÃO: ESTE MÉTODO
	 * PROVAVELMENTE PODE SER ELIMINADO EM UM REFACTORING.
	 * 
	 * @param cfg
	 * @throws Exception
	 */
	private void gravarNovaConfig(WfConfiguracao cfg) throws Exception {
		WfDao.getInstance().gravarComHistorico(cfg, getIdentidadeCadastrante());
	}

	/**
	 * Torna uma configuração existente inválida. A invalidação da configuração
	 * normalmente ocorre ao se criar uma nova configuração. A configuração
	 * antiga torna-se inválida mas continua sendo mantida no banco de dados
	 * para fins de histórico.
	 * 
	 * @param cfgExistente
	 *            - Configuração a ser invalidada
	 * @param dataFim
	 *            - Data que define o fim da validade da configuração, ou seja,
	 *            data de invalidação.
	 * @throws AplicacaoException
	 */
	private void invalidarConfiguracao(WfConfiguracao cfgExistente, Date dataFim)
			throws AplicacaoException {
		if (cfgExistente != null && cfgExistente.getHisDtFim() == null) {
			cfgExistente.setHisDtFim(dataFim);
			WfDao.getInstance().gravarComHistorico(cfgExistente,
					getIdentidadeCadastrante());
		}
	}

	/**
	 * Limpa o cache do hibernate. Como as configurações são mantidas em cache
	 * por motivo de performance, as alterações precisam ser atualizadas para
	 * que possam valer imediatamente.
	 * 
	 * @throws Exception
	 */
	public void limparCache() throws Exception {

		SessionFactory sfWfDao = WfDao.getInstance().getSessao()
				.getSessionFactory();
		SessionFactory sfCpDao = CpDao.getInstance().getSessao()
				.getSessionFactory();

		CpTipoConfiguracao tipoConfig = CpDao.getInstance().consultar(
				CpTipoConfiguracao.TIPO_CONFIG_INSTANCIAR_PROCEDIMENTO,
				CpTipoConfiguracao.class, false);

		Wf.getInstance().getConf().limparCacheSeNecessario();

		sfWfDao.evict(CpConfiguracao.class);
		sfWfDao.evict(WfConfiguracao.class);
		sfCpDao.evict(DpLotacao.class);

		sfWfDao.evictQueries(CpDao.CACHE_QUERY_CONFIGURACAO);

		return;

	}

	/**
	 * Inicia um objeto WfConfiguracao de modo que possa receber as
	 * configurações definidas pelo usuário.
	 * 
	 * @return - Configuração pronta para receber as configurações.
	 */
	private WfConfiguracao prepararConfiguracao(CpOrgaoUsuario orgaoUsuario,
			String procedimento) {
		WfConfiguracao cfg = new WfConfiguracao();
		CpTipoConfiguracao tipoConfig = CpDao.getInstance().consultar(
				CpTipoConfiguracao.TIPO_CONFIG_INSTANCIAR_PROCEDIMENTO,
				CpTipoConfiguracao.class, false);
		cfg.setCpTipoConfiguracao(tipoConfig);
		cfg.setOrgaoUsuario(orgaoUsuario);
		cfg.setProcedimento(procedimento);
		return cfg;
	}

	/**
	 * Processa as permissões definidas pelo usuário. São extraídos os dados do
	 * request, definida a data de início da configuração, definidas as
	 * permissões, definida a invalidação da configuração antiga e atualizada a
	 * visão do usuário.
	 * 
	 * @param permissao
	 *            - Permissão a ser processada
	 * @param cfg
	 *            - Configuração preparada para receber as novas configurações
	 * @param horaDoBD
	 *            - Hora do banco de dados. A data/hora de início de vigência
	 *            deve ser a mesma da invalidação da configuração anterior.
	 * @throws Exception
	 */
	private void processarPermissao(CpOrgaoUsuario orgaoUsuario,
			String procedimento, Permissao permissao, WfConfiguracao cfg,
			Date horaDoBD) throws Exception {
		DpLotacao lotacao = null;
		DpPessoa pessoa = null;

		lotacao = extrairLotaAtor(permissao.getId());
		pessoa = extrairAtor(permissao.getId());

		if (pessoa != null || lotacao != null) {// se
			// configuração
			// definida
			cfg.setDpPessoa(pessoa);
			cfg.setLotacao(lotacao);

			cfg.setHisDtIni(horaDoBD);

			for (WfConfiguracao cfgExistente : getConfiguracaoExistente(
					orgaoUsuario, procedimento, permissao)) {
				invalidarConfiguracao(cfgExistente, horaDoBD);
			}

			permissao.setPessoa(pessoa);
			permissao.setLotacao(lotacao);

			CpSituacaoConfiguracao sit = new CpSituacaoConfiguracao();
			sit.setIdSitConfiguracao(CpSituacaoConfiguracao.SITUACAO_PODE);
			cfg.setCpSituacaoConfiguracao(sit);
			gravarNovaConfig(cfg);
			permissao.setId(cfg.getIdConfiguracao()); // deixa de usar a id
			// temporária

			// atualiza os dados da visão
			if (pessoa != null) {
				permissao.setPessoa(pessoa);
				permissao.setTipoResponsavel(TIPO_RESP_MATRICULA);
			}

			if (lotacao != null) {
				permissao.setLotacao(lotacao);
				permissao.setTipoResponsavel(TIPO_RESP_LOTACAO);
			}

		} else {
			for (WfConfiguracao cfgExistente : getConfiguracaoExistente(
					orgaoUsuario, procedimento, permissao)) {
				invalidarConfiguracao(cfgExistente, horaDoBD);
			}

			permissao.setPessoa(pessoa);
			permissao.setLotacao(lotacao);
		}

	}

	private Long getIdNovaPermissao() {
		return Long.MAX_VALUE;
	}

}
