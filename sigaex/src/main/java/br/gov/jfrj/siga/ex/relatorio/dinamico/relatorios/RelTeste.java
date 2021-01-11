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

//		this.setTitle("Relação Modelos - Teste ");
//		this.addColuna("descricao", 35, RelatorioRapido.ESQUERDA, false);
//		this.addColuna("destinatario", 40, RelatorioRapido.ESQUERDA, false);
//		this.addColuna("data", 40, RelatorioRapido.ESQUERDA, false);

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
		

		
		
		return this;

	}

	@Override
	public Collection processarDados() throws Exception {
/*
		String sql = " SELECT	u.acronimo_orgao_usu || '-' ||	f.sigla_forma_doc || '-' || d.ano_emissao || '/' ||	lpad (d.num_expediente, 5, '0') ,"
				+ "		m.dt_timestamp ,"
				+ "		m.dt_timestamp ,"
				+ "		m.dt_timestamp ,"
				+ "		m.id_tp_mov ,"
				+ "		tm.descr_tipo_movimentacao,"
				+ "		c.codificacao ,"
				+ "		c.descr_classificacao ,"
				+ "		( 	SELECT	l.id_lotacao	FROM corporativo.dp_lotacao l2"
				+ "  		WHERE l2.id_lotacao = ( SELECT"
				+ "											m3.id_lota_resp"
				+ "									FROM siga.ex_movimentacao m3 "
				+ "									WHERE m3.id_mov =(	SELECT max (m2.id_mov)"
				+ "														FROM siga.ex_movimentacao m2 "
				+ "															INNER JOIN siga.ex_mobil mb2 on (mb2.id_mobil = m2.id_mobil)"
				+ "														WHERE mb2.id_doc = d.id_doc AND m2.id_mov < m.id_mov	)		)"
				+ "		), ( SELECT"
				+ "				l.nome_lotacao"
				+ "		  FROM corporativo.dp_lotacao l2"
				+ " 	  WHERE l2.id_lotacao = (	SELECT		m3.id_lota_resp"
				+ "									FROM siga.ex_movimentacao m3 "
				+ "									WHERE m3.id_mov =(	SELECT 	max (m2.id_mov)"
				+ "														FROM siga.ex_movimentacao m2 "
				+ "															INNER JOIN siga.ex_mobil mb2 on (mb2.id_mobil = m2.id_mobil)"
				+ "														WHERE mb2.id_doc = d.id_doc "
				+ "															AND m2.id_mov < m.id_mov "
				+ "													)								)		),"
				+ "		l.id_lotacao ,		l.nome_lotacao ,		m.id_cadastrante"
				+ " FROM siga.ex_documento d"
				+ " 	INNER JOIN siga.ex_classificacao c on (c.id_classificacao = d.id_classificacao)"
				+ "	INNER JOIN siga.ex_mobil mb on (mb.id_doc = d.id_doc)"
				+ "	INNER JOIN siga.ex_movimentacao m on (m.id_mobil = mb.id_mobil)"
				+ "	INNER JOIN siga.ex_tipo_movimentacao tm on (tm.id_tp_mov = m.id_tp_mov)"
				+ "	INNER JOIN siga.ex_classificacao c on (c.id_classificacao = d.id_classificacao)"
				+ "	INNER JOIN corporativo.dp_lotacao l on (d.id_lota_cadastrante = l.id_lotacao)"
				+ "	INNER JOIN corporativo.cp_orgao_usuario u on (l.id_orgao_usu = u.id_orgao_usu)"
				+ "	INNER JOIN siga.ex_forma_documento f on (f.id_forma_doc = d.id_forma_doc)"
				+ " ORDER BY 'NUM. PROCESSO' desc;";
	 		*/
		
		String sql =
				"SELECT	u.acronimo_orgao_usu || '-' ||	f.sigla_forma_doc || '-' || d.ano_emissao || '/' ||	lpad (d.num_expediente, 5, '0') ,	 m.dt_timestamp ,		m.dt_timestamp ,		m.dt_timestamp ,		m.id_tp_mov ,		tm.descr_tipo_movimentacao,		c.codificacao ,		c.descr_classificacao ,		( 	SELECT	l.id_lotacao	FROM corporativo.dp_lotacao l2  		WHERE l2.id_lotacao = ( SELECT											m3.id_lota_resp									FROM siga.ex_movimentacao m3 									WHERE m3.id_mov =(	SELECT max (m2.id_mov)														FROM siga.ex_movimentacao m2 															INNER JOIN siga.ex_mobil mb2 on (mb2.id_mobil = m2.id_mobil)														WHERE mb2.id_doc = d.id_doc AND m2.id_mov < m.id_mov	)		)		), ( SELECT				l.nome_lotacao		  FROM corporativo.dp_lotacao l2 	  WHERE l2.id_lotacao = (	SELECT		m3.id_lota_resp									FROM siga.ex_movimentacao m3 									WHERE m3.id_mov =(	SELECT 	max (m2.id_mov)														FROM siga.ex_movimentacao m2 															INNER JOIN siga.ex_mobil mb2 on (mb2.id_mobil = m2.id_mobil)														WHERE mb2.id_doc = d.id_doc 															AND m2.id_mov < m.id_mov 													)								)		),		l.id_lotacao ,		l.nome_lotacao ,		m.id_cadastrante FROM siga.ex_documento d 	INNER JOIN siga.ex_classificacao c on (c.id_classificacao = d.id_classificacao)	INNER JOIN siga.ex_mobil mb on (mb.id_doc = d.id_doc)	INNER JOIN siga.ex_movimentacao m on (m.id_mobil = mb.id_mobil)	INNER JOIN siga.ex_tipo_movimentacao tm on (tm.id_tp_mov = m.id_tp_mov)	INNER JOIN siga.ex_classificacao c on (c.id_classificacao = d.id_classificacao)	INNER JOIN corporativo.dp_lotacao l on (d.id_lota_cadastrante = l.id_lotacao)	INNER JOIN corporativo.cp_orgao_usuario u on (l.id_orgao_usu = u.id_orgao_usu)	INNER JOIN siga.ex_forma_documento f on (f.id_forma_doc = d.id_forma_doc) ORDER BY 'NUM. PROCESSO' desc;";
		
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		List<String> d = new ArrayList<String>();
		
//		Query query = ContextoPersistencia.em()
//				.createQuery(
//						"select   doc.descrDocumento, doc.nmDestinatario, doc.dtDoc"
//						+ " from  ExDocumento doc " );
		
		 Query query = ContextoPersistencia.em().createNativeQuery( sql );
		
		

		List<Object[]> lista = query.getResultList();
		
		List<String> listaFinal = new ArrayList<String>();
		
		for (Object[] array : lista) {
			
			//NUM. PROCESSO,DATA DESPACHO,DATA RECEBIMENTO,DATA SAÍDA,COD. DESPACHO,DESCR. DESPACHO,
			//COD. ASSUNTO,DESCR. ASSUNTO,ORG. ORIGEM,DESCR. ORG. ORIGEM,ORG. DESTINO,DESCR. ORG. DESTINO,MATR. DIGITADOR

			for (int i = 0; i < array.length; i++) {
				
				listaFinal.add(String.valueOf( array[i] ));
				
			         
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
