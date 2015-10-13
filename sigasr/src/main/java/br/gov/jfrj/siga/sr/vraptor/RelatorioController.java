package br.gov.jfrj.siga.sr.vraptor;

import static br.gov.jfrj.siga.sr.util.SrSigaPermissaoPerfil.REL_RELATORIOS;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRParameter;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.gov.jfrj.siga.cp.model.DpLotacaoSelecao;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.sr.annotation.AssertAcesso;
import br.gov.jfrj.siga.sr.reports.SrRelAtendimento;
import br.gov.jfrj.siga.sr.validator.SrValidator;
import br.gov.jfrj.siga.vraptor.SigaObjects;

@Resource
@Path("app/relatorio")
public class RelatorioController extends SrController {

	private HttpServletResponse response;
	
	public RelatorioController(HttpServletRequest request, Result result, CpDao dao, SigaObjects so, EntityManager em, SrValidator srValidator,
			HttpServletResponse response) {
		super(request, result, dao, so, em, srValidator);
		this.response = response;
	}

	private static final String APPLICATION_EXCEL = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	@AssertAcesso(REL_RELATORIOS)
	@Path("/atendimentos")
	public void exibirRelAtendimentos() {
	}
	
	@AssertAcesso(REL_RELATORIOS)
	@Path("/atendimentos/gerar")
	public Download gerarRelAtendimentos(DpLotacao lotacao, String listaLotacoes, String siglaLotacao, 
			String dtIni, String dtFim, String downloadToken, String tipo) throws Exception {
		DpLotacao lotaAtendente = null;
		if (lotacao != null)
			lotaAtendente = DpLotacao.AR.findById(lotacao.getId());

		String nomeArquivoExportado = "relAtendimentos_" 
					+ new SimpleDateFormat("ddMMyy_HHmm").format(new Date()) + "_" 
					+ (lotaAtendente != null ? lotaAtendente.getSigla() : "lista-lotacoes") + ".xlsx";
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("dtIni", dtIni);
		parametros.put("dtFim", dtFim);
		parametros.put("idlotaAtendenteIni", lotaAtendente != null ? lotaAtendente.getIdLotacaoIni() : 0L);
		parametros.put("listaLotacoes", listaLotacoes);
		parametros.put("siglaLotacao", siglaLotacao);
		parametros.put("tipo", tipo);
		parametros.put("secaoUsuario", getTitular().getOrgaoUsuario().getDescricaoMaiusculas());
		parametros.put(JRParameter.IS_IGNORE_PAGINATION, true);
		
		SrRelAtendimento rel = new SrRelAtendimento(parametros);
		rel.gerar();

		response.addCookie(new Cookie("fileDownloadToken", downloadToken));
		return new InputStreamDownload(new ByteArrayInputStream(rel.getRelatorioExcel()), 
				APPLICATION_EXCEL, nomeArquivoExportado);
	}
}
