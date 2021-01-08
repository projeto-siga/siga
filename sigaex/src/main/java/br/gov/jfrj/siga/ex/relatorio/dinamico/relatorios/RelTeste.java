package br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.Query;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import net.sf.jasperreports.engine.JRException;

public class RelTeste extends RelatorioTemplate {

	public RelTeste(Map parametros) throws DJBuilderException {
		super(parametros);
		if (parametros.get("secaoUsuario") == null) {
			throw new DJBuilderException(
					"Parâmetro secaoUsuario não informado!");
		}
		if (parametros.get("lotacaoTitular") == null) {
			throw new DJBuilderException("Parâmetro lotação não informado!");
		}
		/*
		 * if (parametros.get("orgao") == null) { throw new
		 * DJBuilderException("Parâmetro órgão não informado!"); }
		 */
		if (parametros.get("dataInicial") == null) {
			throw new DJBuilderException("Parâmetro dataInicial não informado!");
		}
		if (parametros.get("dataFinal") == null) {
			throw new DJBuilderException("Parâmetro dataFinal não informado!");
		}
		if (parametros.get("link_siga") == null) {
			throw new DJBuilderException("Parâmetro link_siga não informado!");
		}
	}

	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException, JRException {

		this.setTitle("Relação Modelos - Teste ");
		this.addColuna("descricao", 35, RelatorioRapido.ESQUERDA, false);
		this.addColuna("destinatario", 40, RelatorioRapido.ESQUERDA, false);
		this.addColuna("data", 40, RelatorioRapido.ESQUERDA, false);

		/*
		this.addColuna("Número", 35, RelatorioRapido.ESQUERDA, false); //NUM. PROCESSO
		this.addColuna("Dt.Despacho", 35, RelatorioRapido.ESQUERDA, false); //DATA DESPACHO
		this.addColuna("Dt.Recebimento", 35, RelatorioRapido.ESQUERDA, false);   //DATA RECEBIMENTO
		this.addColuna("Dt.Saída", 35, RelatorioRapido.ESQUERDA, false);   //,DATA SAÍDA,
		this.addColuna("Cod.Despacho", 35, RelatorioRapido.ESQUERDA, false);   //COD. DESPACHO
		this.addColuna("Descr.Despacho", 35, RelatorioRapido.ESQUERDA, false);   //DESCR. DESPACHO
		this.addColuna("Cod.Assunto", 35, RelatorioRapido.ESQUERDA, false);   //COD. ASSUNTO
		this.addColuna("Descr.Assunto", 35, RelatorioRapido.ESQUERDA, false);   //DESCR. ASSUNTO,
		this.addColuna("Órgão Origem", 35, RelatorioRapido.ESQUERDA, false);   //ORG. ORIGEM
		this.addColuna("Descr.Órgão Origem", 35, RelatorioRapido.ESQUERDA, false);   //DESCR. ORG. ORIGEM
		this.addColuna("Órgão Destino", 35, RelatorioRapido.ESQUERDA, false);   //ORG. DESTINO
		this.addColuna("Descr.Órgão Destino", 35, RelatorioRapido.ESQUERDA, false);   //DESCR. ORG. DESTINO
		this.addColuna("Matr.Digitador", 35, RelatorioRapido.ESQUERDA, false);   //MATR. DIGITADOR
		*/
		

		
		
		return this;

	}

	@Override
	public Collection processarDados() throws Exception {

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		List<String> d = new ArrayList<String>();
		
		Query query = ContextoPersistencia.em()
				.createQuery(
						"select   doc.descrDocumento, doc.nmDestinatario, doc.dtDoc"
						+ " from  ExDocumento doc " );


		List<Object[]> lista = query.getResultList();
		
		List<String> listaFinal = new ArrayList<String>();
		
		for (Object[] array : lista) {
			
			//NUM. PROCESSO,DATA DESPACHO,DATA RECEBIMENTO,DATA SAÍDA,COD. DESPACHO,DESCR. DESPACHO,
			//COD. ASSUNTO,DESCR. ASSUNTO,ORG. ORIGEM,DESCR. ORG. ORIGEM,ORG. DESTINO,DESCR. ORG. DESTINO,MATR. DIGITADOR

			for (int i = 0; i < array.length; i++) {
				
				listaFinal.add((String)array[i]);
			}
			
			
//			String descricao = (String)array[0];
//			listaFinal.add(descricao);
//			
//			String destinatario = (String)array[1];
//			listaFinal.add(destinatario);
			
		}
	 
		
		return listaFinal;
	}

	private void acrescentarColuna(List<String> d, Map<String, Long> map,
			String s, String lis) {
		Long l = 0L;
		String key = chave(s, lis);
		if (map.containsKey(key))
			l += map.get(key);
		if (l > 0)
			d.add(l.toString());
		else
			d.add("0");
	}

	private String chave(String tipodoc, String formadoc) {
		return tipodoc + formadoc;
	}
}
