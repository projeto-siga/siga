package br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExVia;
import br.gov.jfrj.siga.ex.util.MascaraUtil;
import br.gov.jfrj.siga.hibernate.ExDao;

public class RelDocsClassificados extends RelatorioTemplate {

	String codificacao;
	DpLotacao lotacao;
	CpOrgaoUsuario cpOrgaoUsu;
	
	public RelDocsClassificados(Map parametros) throws DJBuilderException {
		super(parametros);
		
		this.codificacao = (String) parametros.get("codificacao");
		
		try{
			Long idLot = Long.valueOf((String) parametros.get("idLotacao"));
			this.lotacao = CpDao.getInstance().consultar(idLot, DpLotacao.class, false);
		}catch (Exception e) {
		}
		
		try{
			Long idOrgaoUsu = Long.valueOf((String)parametros.get("idOrgaoUsu"));
			this.cpOrgaoUsu = CpDao.getInstance().consultar(idOrgaoUsu, CpOrgaoUsuario.class, false);
		}catch (Exception e) {
		}

	}

	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException, JRException {
		//this.setTitle("Relação de Documentos Classificados");
		
		String titulo = "Relação de Documentos Classificados";
			
		if (this.codificacao!=null && this.codificacao.length()>0){
			titulo = titulo + " da: " + this.codificacao;
		}
		this.setTitle(titulo);
		if (this.lotacao!=null){
			String subtitulo = "Do(a) " + lotacao.getNomeLotacao();
			this.setSubtitle(subtitulo);
		}
		this.addColuna("Documento", 20, RelatorioRapido.CENTRO,
				false, false);
		this.addColuna("Classificação", 80, RelatorioRapido.ESQUERDA,
				false, false);

		return this;
	}

	@Override
	public Collection processarDados() throws Exception {
		MascaraUtil m = MascaraUtil.getInstance();
		List<String> dados = new ArrayList<String>();
		List<ExDocumento> listaDocs;
		
		if (this.codificacao!=null && this.codificacao.length()>0){
			listaDocs = ExDao.getInstance().consultarExDocumentoPorClassificacao(this.lotacao,m.getMscFilho(this.codificacao, true),this.cpOrgaoUsu);	
		}else{
			listaDocs = ExDao.getInstance().consultarExDocumentoPorClassificacao(this.lotacao,m.getMscTodosDoMaiorNivel(),this.cpOrgaoUsu);
		}
		
		
		for (ExDocumento d : listaDocs) {
			dados.add(d.getCodigo()); //num doc
			dados.add(d.getExClassificacaoAtual().getDescricaoCompleta()); //Descrição
			
		}
		
		return dados;
	}

}