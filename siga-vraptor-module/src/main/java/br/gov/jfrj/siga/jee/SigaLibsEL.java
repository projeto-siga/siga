/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.siga.jee;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

import br.gov.jfrj.siga.base.Contexto;
import br.gov.jfrj.siga.base.ReaisPorExtenso;
import br.gov.jfrj.siga.base.SigaBaseProperties;
import br.gov.jfrj.siga.base.SigaCalendar;
import br.gov.jfrj.siga.base.Texto;
import br.gov.jfrj.siga.cp.CpServico;
import br.gov.jfrj.siga.cp.CpTipoConfiguracao;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.dp.dao.CpDao;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

public class SigaLibsEL {
	private static String month[] = new String[] { "Jan", "Fev", "Mar", "Abr",
			"Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez" };

	public static String concat(final String s, final String s2) {
		return s + s2;
	}

	public static Boolean has(final String[] as, final String s) {
		for (final String sAux : as)
			if (sAux.equals(s))
				return true;
		return false;
	}

	public static String reaisPorExtenso(final String s) {
		if (s == null)
			return null;
		String temp = s;
		temp = temp.replaceAll("\\.", "");
		temp = temp.replaceAll("\\,", ".");
		ReaisPorExtenso r = new ReaisPorExtenso(new BigDecimal(temp));
		return r.toString();
	}

	public static String removeAcento(final String s) {
		return Texto.removeAcento(s);
	}

	public static String maiusculasEMinusculas(String s) {
		return Texto.maiusculasEMinusculas(s);
	}

	public static Object resource(String name) {
		return Contexto.resource(name);
	}

	public static String espera(Date dt) {
		SigaCalendar c = new SigaCalendar();
		SigaCalendar lAnterior = new SigaCalendar(dt.getTime());
		// long l = -c.diffDayPeriods(lAnterior);
		long l = c.getUnixDay() - lAnterior.getUnixDay();
		if (l == 0) {
			if (lAnterior.getJulianDay() == c.getJulianDay())
				return (lAnterior.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "")
						+ lAnterior.get(Calendar.HOUR_OF_DAY) + ":"
						+ (lAnterior.get(Calendar.MINUTE) < 10 ? "0" : "")
						+ lAnterior.get(Calendar.MINUTE);
		}
		return lAnterior.get(Calendar.DAY_OF_MONTH) + "/"
				+ month[lAnterior.get(Calendar.MONTH)] + " ("
				+ Long.toString(l) + " dia" + (l == 1L ? "" : "s") + ")";
	}

	public static String esperaSimples(Date dt) {
		SigaCalendar c = new SigaCalendar();
		SigaCalendar lAnterior = new SigaCalendar(dt.getTime());
		// long l = -c.diffDayPeriods(lAnterior);
		long l = c.getUnixDay() - lAnterior.getUnixDay();
		if (l == 0) {
			if (lAnterior.getJulianDay() == c.getJulianDay())
				return lAnterior.get(Calendar.HOUR_OF_DAY) + ":"
						+ (lAnterior.get(Calendar.MINUTE) < 10 ? "0" : "")
						+ lAnterior.get(Calendar.MINUTE);
		}
		return Long.toString(l) + " dia" + (l == 1L ? "" : "s");
	}

	public static String intervalo(Date dtIni, Date dtFim) {
		SigaCalendar lFim = new SigaCalendar(dtIni.getTime());
		SigaCalendar lIni = new SigaCalendar(dtFim.getTime());

		long l = lFim.getUnixDay() - lIni.getUnixDay();
		if (l == 0) {
			return lIni.diffHHMMSS(lFim);
		}
		return Long.toString(l) + " dia" + (l == 1L ? "" : "s");
	}

	/*
	 * private static DaoFactory getFabrica() throws CsisException { return
	 * DaoFactory.getDAOFactory(); }
	 * 
	 * public static DpPessoa pessoa(Long id) throws CsisException { if (id ==
	 * null || id == 0) return null;
	 * 
	 * final DpPessoaDao dao = getFabrica().createDpPessoaDao(); return
	 * dao.consultar(id, false); }
	 * 
	 * public static List<DpPessoa> pessoasPorLotacao(Integer id, Boolean
	 * incluirSublotacoes) throws CsisException { if (id == null || id == 0)
	 * return null;
	 * 
	 * final DpLotacaoDao daoLotacao = getFabrica().createDpLotacaoDao();
	 * DpLotacao lotacao = daoLotacao.consultar(id, false);
	 * 
	 * List<DpLotacao> lotacoes = daoLotacao.listarTodos(); List<DpLotacao>
	 * sublotacoes = new ArrayList<DpLotacao>(); sublotacoes.add(lotacao);
	 * boolean continuar = true; while (continuar) { continuar = false; for
	 * (DpLotacao lot : lotacoes) { if (sublotacoes.contains(lot))
	 * 
	 * continue; if (sublotacoes.contains(lot.getLotacaoPai())) {
	 * sublotacoes.add(lot); continuar = true; } } }
	 * 
	 * List<DpPessoa> lstCompleta = new ArrayList<DpPessoa>(); final DpPessoaDao
	 * dao = getFabrica().createDpPessoaDao(); DpPessoaDaoFiltro flt = new
	 * DpPessoaDaoFiltro(); String estagiario = "ESTAGIARIO"; String juizFederal
	 * = "JUIZ FEDERAL"; String juizFederalSubstituto = "JUIZ
	 * SUBSTITUTO"; flt.setNome(""); for (DpLotacao lot : sublotacoes) {
	 * flt.setLotacao(lot); List<DpPessoa> lst = dao.consultarPorFiltro(flt); //
	 * final DpCargoDao daoCargo = getFabrica().createDpCargoDao(); // List
	 * <DpCargo> cargo= daoCargo.listarTodos();
	 * 
	 * for (DpPessoa pes : lst) { if (pes.getCargo() == null ||
	 * pes.getCargo().getNomeCargo() == null) { lstCompleta.add(pes); continue;
	 * } String cargo = pes.getCargo().getNomeCargo().toUpperCase(); if
	 * (!cargo.contains(juizFederal) && !cargo.contains(juizFederalSubstituto)
	 * && !(cargo.contains(estagiario))) lstCompleta.add(pes); } // for
	 * (DpPessoa pes : lst) { // for(DpCargo carg : cargo) //
	 * if(carg.getIdCargo().equals(pes.getIdCargo())&& //
	 * ((!(carg.getNomeCargo().contains(juizFederal))) && //
	 * (!(carg.getNomeCargo
	 * ().contains(juizFederalSubstituto)))&&(!(carg.getNomeCargo
	 * ().contains(estagiario))))) // lstCompleta.add(pes); // } }
	 * 
	 * return lstCompleta; }
	 */

	public static String fixFontSize(String html, String tamanho) {

		// Pattern p1 = Pattern.compile("^([0-9\\.\\-/]+) ([^\r\n]+)\r\n");
		Pattern p1 = Pattern.compile(
				"<((?:p|td|div|span)[^>]*) style=\"([^\"]*)\"([^>]*)>",
				Pattern.CASE_INSENSITIVE);
		Pattern p2 = Pattern.compile("(font-size:[^;]*)(;?)",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p1.matcher(html);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String all = m.group();
			String begin = m.group(1);
			String style = m.group(2);
			Matcher m2 = p2.matcher(style);
			if (m2.find()) {
				if (style.contains("font-size-no-fix: yes")) {
					style = style.replace("font-size-no-fix: yes;", "");
					style = style.replace("; font-size-no-fix: yes", "");
					style = style.replace("font-size-no-fix: yes", "");
				} else {
					style = style.replace(m2.group(), "font-size:" + tamanho
							+ m2.group(2));
				}
			} else {
				style = "font-size:" + tamanho + ";" + style;
			}
			String end = m.group(3);
			m.appendReplacement(sb, "<" + begin + " style=\"" + style + "\""
					+ end + ">");
		}
		m.appendTail(sb);

		Pattern p3 = Pattern.compile("<(p|td|div|span)([^>]*)>",
				Pattern.CASE_INSENSITIVE);
		Matcher m3 = p3.matcher(sb.toString());
		StringBuffer sb2 = new StringBuffer();
		String style = "font-size:" + tamanho + ";";
		while (m3.find()) {
			String all = m3.group();
			String begin = m3.group(1);
			String end = m3.group(2);
			if (!end.contains("style=")) {
				m3.appendReplacement(sb2, "<" + begin + " style=\"" + style
						+ "\"" + end + ">");
			} else {
				m3.appendReplacement(sb2, "<" + begin + end + ">");
			}
		}
		m3.appendTail(sb2);

		return sb2.toString();

	}

	/*
	 * public static ExTratamento tratamento(String autoridade, String genero) {
	 * return ExTratamento.tratamento(autoridade, genero); }
	 * 
	 * public static String verificaGenero(String autoridade) { return
	 * ExTratamento.genero(autoridade); }
	 * 
	 * public static Object resource(String name) { Context initContext = null;
	 * Context envContext = null;
	 * 
	 * try { initContext = new InitialContext(); envContext = (Context)
	 * initContext.lookup("java:/comp/env"); return envContext.lookup(name); }
	 * catch (final NamingException e) {
	 * System.err.print(Messages.getString("Oracle.LookUpErro") +
	 * e.getMessage()); } return null; } public static String cargoPessoa (Long
	 * idPessoa)throws CsisException{
	 * 
	 * final DpPessoaDao daoPessoa = getFabrica().createDpPessoaDao();
	 * List<DpPessoa> pessoa = daoPessoa.listarTodos(); for(DpPessoa pes :
	 * pessoa) if(pes.getIdPessoa().equals(idPessoa)) return
	 * pes.getCargo().getNomeCargo();
	 * 
	 * 
	 * return null; }
	 */

	public static CpDao dao() {
		return CpDao.getInstance();
	}

	public static Boolean podeUtilizarServicoPorConfiguracao(DpPessoa titular,
			DpLotacao lotaTitular, Integer idServico) throws Exception {
		Boolean b = Cp
				.getInstance()
				.getConf()
				.podePorConfiguracao(
						titular,
						lotaTitular,
						dao().consultar(idServico.longValue(), CpServico.class,
								false),
						CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO);
		return b;
	}

	public static Boolean podePorConfiguracao(DpPessoa titular,
			DpLotacao lotaTitular, Long idTpConf) throws Exception {
		return Cp.getInstance().getConf()
				.podePorConfiguracao(titular, lotaTitular, idTpConf);
	}

	// public static Boolean podeUtilizarServicoPorConfiguracao(DpPessoa
	// titular,
	// DpLotacao lotaTitular, String siglaServico) throws Exception {
	// CpServico srv = new CpServico();
	// srv.setSiglaServico(siglaServico);
	// return Cp.getInstance().getConf().podePorConfiguracao(titular,
	// lotaTitular, dao().consultarPorSigla(srv),
	// CpTipoConfiguracao.TIPO_CONFIG_UTILIZAR_SERVICO);
	// }

	public static Boolean podeUtilizarServicoPorConfiguracao(DpPessoa titular,
			DpLotacao lotaTitular, String servicoPath) throws Exception {
		return Cp
				.getInstance()
				.getConf()
				.podeUtilizarServicoPorConfiguracao(titular, lotaTitular,
						servicoPath);
	}

	public static Boolean podeGerirAlgumGrupo(DpPessoa titular,
			DpLotacao lotaTitular, Long idCpTipoGrupo) throws Exception {
		return Cp.getInstance().getConf()
				.podeGerirAlgumGrupo(titular, lotaTitular, idCpTipoGrupo);
	}

	public static String getURLSistema(String nome) {
		String ambiente = SigaBaseProperties.getString("ambiente");
		String url = System.getProperty(nome + "." + ambiente + ".url");
		if (url == null || url.length() == 0) {
			url = "#";
		}
		return url.trim();
	}

	public static String urlEncode(String value)
			throws UnsupportedEncodingException {
		return URLEncoder.encode(value, "UTF-8");
	}

	public static String getComplementoHead(CpOrgaoUsuario oragaoUsu) {
		final String CACHE_GENERIC_HOURS = "cache.hours";
		CacheManager manager = CacheManager.getInstance();
		Cache cache;

		if (!manager.cacheExists(CACHE_GENERIC_HOURS)) {
			manager.addCache(CACHE_GENERIC_HOURS);
			cache = manager.getCache(CACHE_GENERIC_HOURS);
			CacheConfiguration config;
			config = cache.getCacheConfiguration();
			config.setTimeToIdleSeconds(3600);
			config.setTimeToLiveSeconds(36000);
			config.setMaxElementsInMemory(10000);
			config.setMaxElementsOnDisk(1000000);
		} else
			cache = manager.getCache(CACHE_GENERIC_HOURS);

		Element element;
		String key = "complementoHEAD-" + oragaoUsu.getId();
		if ((element = cache.get(key)) != null) {
			return (String) element.getValue();
		}

		ProcessadorFreemarkerSimples p = new ProcessadorFreemarkerSimples();
		Map attrs = new HashMap();
		attrs.put("nmMod", "macro complementoHEAD");
		attrs.put("template", "[@complementoHEAD/]");
		try {
			String s = p.processarModelo(oragaoUsu, attrs, null).trim();
			cache.put(new Element(key, s));
			return s;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static Boolean podeCadastrarQqSubstituicaoPorConfiguracao(
			DpPessoa pessoa, DpLotacao lotacao) throws Exception {

		return Cp
				.getInstance()
				.getConf()
				.podePorConfiguracao(pessoa,
						CpTipoConfiguracao.TIPO_CONFIG_CADASTRAR_QUALQUER_SUBST)
				|| Cp.getInstance()
						.getConf()
						.podePorConfiguracao(
								lotacao,
								CpTipoConfiguracao.TIPO_CONFIG_CADASTRAR_QUALQUER_SUBST);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object evaluate(String expression, Object root) {
		expression = expression.replace(".", ".?");
		expression = expression.replaceFirst("^([a-zA-Z0-9]+)(?:$|(?:\\.|\\[)?.*$)", "isdef $1 ? $0 : null");
		CompiledTemplate template = TemplateCompiler
				.compileTemplate("${" + expression + "}");
		Map vars = new HashMap();
		vars.putAll((Map) root);

		Object output = TemplateRuntime.execute(template, vars);
		return output;
	}
	
	public static String pluralize(Integer count, String singular, String plural) {
		if(count <= 1){
			return singular;
		}
		else
			return plural;
	}

//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public static Object evaluate(String expression, Object root) throws Exception {
//		Object expr = Ognl.parseExpression(expression);
//
//	    OgnlContext ctx = new OgnlContext(); 
//	    
//	    Map vars = new HashMap();
//		vars.putAll((Map) root);
//		
//	    Object output = Ognl.getValue(expr, ctx, vars);
//		return output;
//	}
	
//	protected ELContext createContext(){
//		  ELResolver resolver=new CompositeELResolver(){
//		{
//		      add(new ArrayELResolver(false));
//		      add(new ListELResolver(false));
//		      add(new MapELResolver(false));
//		      add(new ResourceBundleELResolver());
//		      add(new BeanELResolver());
//		    }
//		  }
//		
//		  return new ELContext(resolver);
//		}
	
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public static Object evaluate(String expression, Object root) throws Exception {
//		Map vars = new HashMap();
//		vars.putAll((Map) root);
//		
//		ELResolver elResolver = new ELResolver(vars);
//	    final VariableMapper variableMapper = new DemoVariableMapper();
//	    final DemoFunctionMapper functionMapper = new DemoFunctionMapper();
//	    functionMapper.addFunction("myprefix", "hello", sayHello);
//	    final CompositeELResolver compositeELResolver = new CompositeELResolver();
//	    compositeELResolver.add(demoELResolver);
//	    compositeELResolver.add(new ArrayELResolver());
//	    compositeELResolver.add(new ListELResolver());
//	    compositeELResolver.add(new BeanELResolver());
//	    compositeELResolver.add(new MapELResolver());
//	    
//	    ELContext context = new ELContext() {
//	      @Override
//	      public ELResolver getELResolver() {
//	        return compositeELResolver;
//	      }
//	      @Override
//	      public FunctionMapper getFunctionMapper() {
//	        return functionMapper;
//	      }
//	      @Override
//	      public VariableMapper getVariableMapper() {
//	        return variableMapper;
//	      }
//	    };
//		
//		Object expr = Ognl.parseExpression(expression);
//
//	    OgnlContext ctx = new OgnlContext();
//	    
//	    Object output = Ognl.getValue(expr, ctx, vars);
//		return output;
//	}
	public static String maximoCaracteres(String s, Integer max) {
		return Texto.maximoCaracteres(s, max);
	}
	
	public static Boolean podeDelegarVisualizacao(DpPessoa cadastrante, DpLotacao lotacaoCadastrante) throws Exception {
		return Cp.getInstance().getConf()
				.podePorConfiguracao(cadastrante, lotacaoCadastrante, CpTipoConfiguracao.TIPO_CONFIG_DELEGAR_VISUALIZACAO);
	}

}
