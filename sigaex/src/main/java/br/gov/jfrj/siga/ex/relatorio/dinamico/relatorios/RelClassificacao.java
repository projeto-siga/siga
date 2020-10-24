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
import br.gov.jfrj.siga.ex.ExClassificacao;
import br.gov.jfrj.siga.ex.ExVia;
import br.gov.jfrj.siga.ex.util.MascaraUtil;
import br.gov.jfrj.siga.hibernate.ExDao;

public class RelClassificacao extends RelatorioTemplate {

	String codificacao;
	
	public RelClassificacao(Map parametros) throws DJBuilderException {
		super(parametros);
		
		this.codificacao = (String) parametros.get("codificacao");

	}

	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException, JRException {
		this.setTitle("Relação de Classificação Documental");
		this.addColuna("Codificação", 12, RelatorioRapido.CENTRO,
				false, false);
		this.addColuna("Descrição", 50, RelatorioRapido.ESQUERDA,
				false, false);
		this.addColuna("Destino", 12, RelatorioRapido.ESQUERDA,
				false, false);
		this.addColuna("Arq. Cor.", 12, RelatorioRapido.ESQUERDA,
				false, false);
		this.addColuna("Arq. Int.", 12, RelatorioRapido.ESQUERDA,
				false, false);
		this.addColuna("Dest. Final", 12, RelatorioRapido.ESQUERDA,
				false, false);

		return this;
	}

	@Override
	public Collection processarDados() throws Exception {
		MascaraUtil m = MascaraUtil.getInstance();
		List<String> dados = new ArrayList<String>();
		List<ExClassificacao> listaClass;
		
		if (this.codificacao!=null && this.codificacao.length()>0){
			listaClass = ExDao.getInstance().consultarExClassificacao(m.getMscFilho(this.codificacao, true), "");
		}else{
			listaClass= ExDao.getInstance().consultarExClassificacaoVigente();
		}
		
		for (ExClassificacao c : listaClass) {
			dados.add(c.getCodificacao()); //Codificação

			StringBuffer descr = new StringBuffer(); 
			for (int i = 1; i < c.getNivel(); i++) {
				descr.append("  ");
			}
			descr.append(c.getDescrClassificacao());
			dados.add(descr.toString()); //Descrição
			
			if (c.getExViaSet() == null  || c.getExViaSet().size() == 0){
				dados.add("");	//Destino
				dados.add("");	//Arq. Cor.
				dados.add("");	//Arq. Int.
				dados.add("");	//Dest. Final
			}else{
				if ( c.getExViaSet() != null  &&  c.getExViaSet().size() >0){
					boolean primeiraVia = true;
					for (ExVia v : c.getExViaSet()) {
						if (!primeiraVia){
							StringBuffer codificacao = new StringBuffer();
							codificacao.append(c.getCodificacao());
							codificacao.append("-");
							codificacao.append(v.getLetraVia());
							
							dados.add(codificacao.toString());											//codificacao
							dados.add(descr.toString()); 										//descrição
						}
						dados.add(v.getExTipoDestinacao()==null?"":v.getExTipoDestinacao().getDescrTipoDestinacao());	//Destino
						dados.add(v.getTemporalidadeCorrente()==null?"":v.getTemporalidadeCorrente().getDescTemporalidade());	//Arq. Cor.
						dados.add(v.getTemporalidadeIntermediario()==null?"":v.getTemporalidadeIntermediario().getDescTemporalidade());	//Arq. Int.
						dados.add(v.getExDestinacaoFinal()==null?"":v.getExDestinacaoFinal().getDescrTipoDestinacao());	//Dest. Final
						primeiraVia=false;
					}
				}
			}
		}
		
		return dados;
	}

}