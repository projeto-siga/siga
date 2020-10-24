package br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMobil;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.hibernate.ExDao;
import net.sf.jasperreports.engine.JRException;

public class RelMovProcesso extends RelatorioTemplate {

	public RelMovProcesso(Map<String, String> parametros) throws AplicacaoException {
		super(parametros);
		if (Utils.empty(parametros.get("secaoUsuario"))) {
			throw new AplicacaoException("Parâmetro secaoUsuario não informado!");
 		}
		if (Utils.empty(parametros.get("lotacao"))) {
			throw new AplicacaoException("Parâmetro lotação não informado!");
		}
		if (Utils.empty(parametros.get("processo"))) {
			throw new AplicacaoException("Parâmetro número do processo não informado!");
		}
		if (Utils.empty(parametros.get("dataInicial"))) {
			throw new AplicacaoException("Parâmetro dataInicial não informado!");
		}
		if (Utils.empty(parametros.get("dataFinal"))) {
			throw new AplicacaoException("Parâmetro dataFinal não informado!");
		}
		if (Utils.empty(parametros.get("link_siga"))) {
			throw new AplicacaoException("Parâmetro link_siga não informado!");
		}
	}

	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio() throws DJBuilderException, JRException {
		
		this.setTitle("Relatório de Movimentações de Processos");
		this.addColuna("Lotação", 10, RelatorioRapido.CENTRO, false);
		this.addColuna("Processo", 40, RelatorioRapido.CENTRO,
				false);
		this.addColuna("Movimentações", 50,
				RelatorioRapido.CENTRO, false);
			return this;
	}

	@Override
	public Collection processarDados() throws Exception {

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		List<String> d = new ArrayList<String>();
		
		String processo = (String) parametros.get("processo");
				
		Map<String, CpOrgaoUsuario> mapAcronimo = new TreeMap<String, CpOrgaoUsuario>();
		for (CpOrgaoUsuario ou : ExDao.getInstance().listarOrgaosUsuarios()) {
			mapAcronimo.put(ou.getAcronimoOrgaoUsu(), ou);
		}
		String acronimos = "";
		for (String s : mapAcronimo.keySet()) {
			acronimos += "|" + s;
		}

		final Pattern p2 = Pattern.compile("^TMP-?([0-9]{1,7})");
		final Pattern p1 = Pattern
				.compile("^("
						+ acronimos
						+ ")?-?([A-Za-z]{3})?-?(?:([0-9]{4})/?)??([0-9]{1,5})(\\.?[0-9]{1,3})?(?:((?:-?[a-zA-Z]{1})|(?:-[0-9]{1,2}))|((?:-?V[0-9]{1,2})))?$");
		final Matcher m2 = p2.matcher(processo.trim().toUpperCase());
		final Matcher m1 = p1.matcher(processo.trim().toUpperCase());
		
		TreeSet<ExMovimentacao> listaCompleta = new TreeSet<ExMovimentacao>();

		ExMovimentacao mov = null;
		ExMobil mob = new ExMobil();
		mob.getCodigo();
		for (ExMovimentacao movAux : mob.getExMovimentacaoSet()) {
			if (mov.getIdMov() == 1)
				listaCompleta.add(movAux);
		}

		
		Iterator it = listaCompleta.iterator();
		while (it.hasNext()) {
			Object[] obj = (Object[]) it.next();
			ExMovimentacao movim = (ExMovimentacao) obj[0];
			ExMobil mobile = (ExMobil) obj[1];
			ExDocumento doc = (ExDocumento) obj[2];
			if (mov.getExMobil().getSigla()  != null) {
				d.add(mov.getExMobil().getSigla());
			} else {
				d.add("");
			}
			if (mov.getDtRegMovDDMMYYHHMMSS().toString() != null) {
				d.add(mov.getDtRegMovDDMMYYHHMMSS().toString());
			} else {
				d.add("");
			}
			if (mov.getDescrTipoMovimentacao() != null) {
				d.add(mov.getDescrTipoMovimentacao());
			} else {
				d.add("");
			}
			if (mov.getLotaCadastrante().getSigla() != null) {
				d.add(mov.getLotaCadastrante().getSigla());
			} else {
				d.add("");
			}
			if (mov.getLotaResp().getSigla() != null) {
				d.add(mov.getLotaResp().getSigla());
			} else {
				d.add("");
			}
			if (mov.getLotaSubscritor().getSigla() != null) {
				d.add(mov.getLotaSubscritor().getSigla());
			} else {
				d.add("");
			}
		}
		return d;	}
}
