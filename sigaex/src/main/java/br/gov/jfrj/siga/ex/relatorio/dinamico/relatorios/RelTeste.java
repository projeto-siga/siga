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

import org.apache.commons.lang.StringUtils;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.dp.DpLotacao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMovimentacao;
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

		this.addColuna("Número", 18, RelatorioRapido.ESQUERDA, false); //NUM. PROCESSO
		this.addColuna("Dt.Despacho", 10, RelatorioRapido.ESQUERDA, false); //DATA DESPACHO
		this.addColuna("Dt.Recebimento", 10, RelatorioRapido.ESQUERDA, false);   //DATA RECEBIMENTO
		this.addColuna("Dt.Saída", 10, RelatorioRapido.ESQUERDA, false);   //,DATA SAÍDA,
		this.addColuna("Despacho", 30, RelatorioRapido.ESQUERDA, false);   //COD. DESPACHO +DESCR. DESPACHO 
		this.addColuna("Assunto", 30, RelatorioRapido.ESQUERDA, false);   //COD. ASSUNTO+ DESCR. ASSUNTO,
		this.addColuna("Órgão Origem", 30, RelatorioRapido.ESQUERDA, false);   //ORG. ORIGEM + DESCR. ORG. ORIGEM
		this.addColuna("Órgão Destino", 30, RelatorioRapido.ESQUERDA, false);   //ORG. DESTINO + DESCR. ORG. DESTINO
		this.addColuna("Matr.Digitador", 10, RelatorioRapido.ESQUERDA, false);   //MATR. DIGITADOR
		
		return this;
	}

	/*@Override
	public Collection processarDados() throws Exception {
 		
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		List<String> d = new ArrayList<String>();

		List<String> listaFinal = new ArrayList<String>();

		String sql =" select d from ExDocumento d join d.exMobilSet as mob join mob.exMovimentacaoSet as m";
		Query query = ContextoPersistencia.em().createQuery( sql );
		

		List<ExDocumento> lista = query.getResultList();
		
		
		for (ExDocumento doc : lista) {
			
			
			String numeroProcesso = doc.getCodigo();
			
			String codAssunto = doc.getExClassificacao().getCodificacao();
			String descAssunto = doc.getExClassificacao().getDescrClassificacao();
			
			String codOrgDestino ="";
			String descOrgaoDestino ="";
			
			if (doc.getLotaDestinatario() !=  null) {
				codOrgDestino =  String.valueOf( doc.getLotaDestinatario().getIdLotacao() );
				descOrgaoDestino = doc.getLotaDestinatario().getNomeLotacao();
			}
			
			for (ExMovimentacao m : doc.getExMovimentacaoSet()){
				
				String dataDespacho = formatter.format(m.getDtTimestamp());
				String dataRecebimento = formatter.format(m.getDtTimestamp());
				String dataSaida = formatter.format(m.getDtTimestamp());
				
				String codDespacho = String.valueOf( m.getExTipoMovimentacao().getIdTpMov());
				String descDespacho =m.getExTipoMovimentacao().getDescricao();
				
				
				String codOrgaoOrigem  ="";
				String descOrgaoOrigem ="";
				//TODO verificar com Ivan o Origem 
			if (m.getLotacao() != null ){
				 codOrgaoOrigem  = String.valueOf(m.getLotacao().getId() );
				 descOrgaoOrigem = m.getLotacao().getDescricao();
			}
				String digitador = m.getCadastrante()!= null ? String.valueOf( m.getCadastrante().getMatricula() ) :"";
			
				
				//NUM. PROCESSO,DATA DESPACHO,DATA RECEBIMENTO,DATA SAÍDA,COD. DESPACHO,DESCR. DESPACHO,
				//COD. ASSUNTO,DESCR. ASSUNTO,ORG. ORIGEM,DESCR. ORG. ORIGEM,ORG. DESTINO,DESCR. ORG. DESTINO,MATR. DIGITADOR
				
				listaFinal.add( numeroProcesso);
				listaFinal.add(dataDespacho);
				listaFinal.add(dataRecebimento);
				listaFinal.add(dataSaida);
				listaFinal.add(codDespacho);
				listaFinal.add(descDespacho);
				listaFinal.add(codAssunto);
				listaFinal.add(descAssunto);
				listaFinal.add(codOrgaoOrigem);
				listaFinal.add(descOrgaoOrigem);
				listaFinal.add(codOrgDestino);
				listaFinal.add(descOrgaoDestino);
				listaFinal.add(digitador);
			}
		}
			
		
		return listaFinal;
	}
*/
	
	public String montarQueryGeraldo(){
		
		String sqlDescricaoOrgao ="";
		
		String sqlOrgaoOrigem =	
				"SELECT  l.id_lotacao  "+
				"FROM corporativo.dp_lotacao l2 "+
				"WHERE l2.id_lotacao = ("+
				"	SELECT  m3.id_lota_resp "+
				"   FROM siga.ex_movimentacao m3  "+
				" 	WHERE m3.id_mov = ("+
				"		SELECT  max (m2.id_mov)   "+
				" 		FROM siga.ex_movimentacao m2   "+
				" 			INNER JOIN siga.ex_mobil mb2 on (mb2.id_mobil = m2.id_mobil)  "+
				" 		WHERE mb2.id_doc = d.id_doc  AND m2.id_mov < m.id_mov )	)	)";
		
		String sqlOrgaoOrigemDescricao =
				"SELECT  l.nome_lotacao "+
				"FROM corporativo.dp_lotacao l2  "+
				"WHERE l2.id_lotacao = ("+
				"	SELECT  m3.id_lota_resp "+
				"	FROM siga.ex_movimentacao m3   "+
				" 	WHERE m3.id_mov ("+
				"		SELECT  max (m2.id_mov) 	"+
				"		FROM siga.ex_movimentacao m2  "+
				" 			INNER JOIN siga.ex_mobil mb2 on (mb2.id_mobil = m2.id_mobil)  "+
				" 		WHERE mb2.id_doc = d.id_doc AND m2.id_mov < m.id_mov )	)	 ";
		
		String sqlColunas =" SELECT  u.acronimo_orgao_usu || '-' ||	f.sigla_forma_doc || '-' ||	d.ano_emissao || '/' ||	lpad (d.num_expediente, 5, '0') NUM_PROCESSO, "+
				"	m.dt_timestamp DATA_DESPACHO, "+
				"	m.dt_timestamp DATA_RECEBIMENTO, "+
				"	m.dt_timestamp DATA_SAÍDA, "+
				"	m.id_tp_mov COD_DESPACHO, "+
				"	tm.descr_tipo_movimentacao DESCR_DESPACHO, "+
				"	c.codificacao COD_ASSUNTO, "+
				"	c.descr_classificacao DESCR_ASSUNTO, ("+
				sqlOrgaoOrigem + 
				" ORG_ORIGEM, ("+
				sqlOrgaoOrigemDescricao +
				" ) DESCR_ORG_ORIGEM,  "+
				" 	l.id_lotacao ORG_DESTINO,  "+
				" 	l.nome_lotacao DESCR_ORG_DESTINO,  "+
				" 	m.id_cadastrante MATR_DIGITADOR";
				
		String sql =
				  " SELECT  X1.*   FROM (	"+
						  sqlColunas +
				"	FROM siga.ex_documento d  "+
				" 		INNER JOIN siga.ex_classificacao c on (c.id_classificacao = d.id_classificacao)  "+
				" 		INNER JOIN siga.ex_mobil mb on (mb.id_doc = d.id_doc)  "+
				" 		INNER JOIN siga.ex_movimentacao m on (m.id_mobil = mb.id_mobil) "+
				" 		INNER JOIN siga.ex_tipo_movimentacao tm on (tm.id_tp_mov = m.id_tp_mov) "+
				" 		INNER JOIN siga.ex_classificacao c on (c.id_classificacao = d.id_classificacao) "+
				" 		INNER JOIN corporativo.dp_lotacao l on (d.id_lota_cadastrante = l.id_lotacao) "+
				" 		INNER JOIN corporativo.cp_orgao_usuario u on (l.id_orgao_usu = u.id_orgao_usu) "+
				" 		INNER JOIN siga.ex_forma_documento f on (f.id_forma_doc = d.id_forma_doc) "+
				" 	ORDER BY NUM_PROCESSO , DATA_DESPACHO DESC) x1 "+
				" 		INNER JOIN  (  "+
				" 	SELECT  u.acronimo_orgao_usu || '-' ||"+
				"	 	f.sigla_forma_doc || '-' ||"+
				"	 	d.ano_emissao || '/' ||"+
				"	 	lpad (d.num_expediente, 5, '0') NUM_PROCESSO,"+
				" 		MAX(m.dt_timestamp) DATA_DESPACHO"+
				"	 FROM siga.ex_documento d"+
				"		INNER JOIN siga.ex_classificacao c on (c.id_classificacao = d.id_classificacao)"+
				"	 	INNER JOIN siga.ex_mobil mb on (mb.id_doc = d.id_doc)"+
				"	 	INNER JOIN siga.ex_movimentacao m on (m.id_mobil = mb.id_mobil)"+
				"	 	INNER JOIN siga.ex_tipo_movimentacao tm on (tm.id_tp_mov = m.id_tp_mov)"+
				"	 	INNER JOIN siga.ex_classificacao c on (c.id_classificacao = d.id_classificacao)"+
				"	 	INNER JOIN corporativo.dp_lotacao l on (d.id_lota_cadastrante = l.id_lotacao)"+
				"	 	INNER JOIN corporativo.cp_orgao_usuario u on (l.id_orgao_usu = u.id_orgao_usu)"+
				"	 	INNER JOIN siga.ex_forma_documento f on (f.id_forma_doc = d.id_forma_doc)"+
				"	 GROUP BY u.acronimo_orgao_usu,f.sigla_forma_doc,d.ano_emissao,d.num_expediente"+
				"	 ORDER BY NUM_PROCESSO desc  ) x2 on x1.NUM_PROCESSO = X2.NUM_PROCESSO AND X1.DATA_DESPACHO = X2.DATA_DESPACHO";
		
	
	return sql;
	}
	@Override
	public Collection processarDados() throws Exception {
 		
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		List<String> d = new ArrayList<String>();

//		String sql =
//				"SELECT	u.acronimo_orgao_usu || '-' ||	f.sigla_forma_doc || '-' || d.ano_emissao || '/' ||	lpad (d.num_expediente, 5, '0') ,	 m.dt_timestamp,	"
//				+ "	m.id_tp_mov ,		tm.descr_tipo_movimentacao,		c.codificacao ,		c.descr_classificacao ,		"
//				+ " ( SELECT	l.id_lotacao    FROM corporativo.dp_lotacao l2  WHERE l2.id_lotacao = "
//				+ " ( SELECT	m3.id_lota_resp FROM siga.ex_movimentacao m3    WHERE m3.id_mov ="
//				+ " ( SELECT max (m2.id_mov)	FROM siga.ex_movimentacao m2 	INNER JOIN siga.ex_mobil mb2 on (mb2.id_mobil = m2.id_mobil)"
//				+ "	WHERE mb2.id_doc = d.id_doc AND m2.id_mov < m.id_mov	)		)		), "
//				+ "( SELECT		l.nome_lotacao	FROM corporativo.dp_lotacao l2 	WHERE l2.id_lotacao = "
//				+ "( SELECT		m3.id_lota_resp	FROM siga.ex_movimentacao m3	WHERE m3.id_mov ="
//				+ "( SELECT 	max (m2.id_mov)	FROM siga.ex_movimentacao m2	INNER JOIN siga.ex_mobil mb2 on (mb2.id_mobil = m2.id_mobil)"
//				+ "	WHERE mb2.id_doc = d.id_doc	AND m2.id_mov < m.id_mov))), l.id_lotacao ,	l.nome_lotacao ,	m.id_cadastrante "
//				+ " FROM siga.ex_documento d 	INNER JOIN siga.ex_classificacao c on (c.id_classificacao = d.id_classificacao)	"
//				+ " INNER JOIN siga.ex_mobil mb on (mb.id_doc = d.id_doc)	"
//				+ " INNER JOIN siga.ex_movimentacao m on (m.id_mobil = mb.id_mobil)	"
//				+ " INNER JOIN siga.ex_tipo_movimentacao tm on (tm.id_tp_mov = m.id_tp_mov)	"
//				+ " INNER JOIN siga.ex_classificacao c on (c.id_classificacao = d.id_classificacao)	"
//				+ " INNER JOIN corporativo.dp_lotacao l on (d.id_lota_cadastrante = l.id_lotacao)	"
//				+ " INNER JOIN corporativo.cp_orgao_usuario u on (l.id_orgao_usu = u.id_orgao_usu)"
//				+ " INNER JOIN siga.ex_forma_documento f on (f.id_forma_doc = d.id_forma_doc) ";
//
		
		Query query = ContextoPersistencia.em().createNativeQuery( montarQueryGeraldo() );
		

		List<Object[]> lista = query.getResultList();
		
		List<String> listaFinal = new ArrayList<String>();
		
		for (Object[] array : lista) {
			
//			for (int i = 0; i < array.length; i++) {
//				listaFinal.add(String.valueOf( array[i] ));
//			}
			
			listaFinal.add( String.valueOf( array[0] ));//NUM. PROCESSO
			listaFinal.add( String.valueOf(formatter.format( array[1] )));//DATA DESPACHO
			listaFinal.add( String.valueOf(formatter.format( array[1] )));//DATA RECEBIMENTO
			listaFinal.add( String.valueOf(formatter.format( array[1] )));//,DATA SAÍDA
			listaFinal.add( String.valueOf( array[2] ) +" "+ String.valueOf( array[3] ));//COD. DESPACHO + DESCR. DESPACHO,
			listaFinal.add( String.valueOf( array[4] ) +" "+ String.valueOf( array[5] ));//COD. ASSUNTO  + DESCR. ASSUNTO
			listaFinal.add( String.valueOf( array[6] ) +" "+ String.valueOf( array[7] ));//ORG. ORIGEM	 + DESCR. ORG. ORIGEM
			listaFinal.add( String.valueOf( array[8] ) +" "+ String.valueOf( array[9] ));//ORG. DESTINO 	 + DESCR. ORG. DESTINO
			listaFinal.add( String.valueOf( array[10]));//MATR. DIGITADOR
			
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
