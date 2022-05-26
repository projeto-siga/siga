package br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.Coluna;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import net.sf.jasperreports.engine.JRException;

public class RelDocsQuantidadeGerados extends RelatorioTemplate {


	public RelDocsQuantidadeGerados(Map parametros) throws DJBuilderException {
		super(parametros);
	}

	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio() 	throws   JRException {

		this.setTitle("Relatório de Saída de Documentos por Setor");
		this.setSubtitle( "Perído: " + parametros.get("dataInicial").toString() + " a " + parametros.get("dataFinal").toString());

		this.addColuna("Assunto", 40, RelatorioRapido.ESQUERDA, true); 	// ORG.  DESTINO  + DESCR.  ORG.  DESTINO
		
		this.addColuna("Lotação Origem", 40, RelatorioRapido.ESQUERDA, true); 	// ORG.  DESTINO  + DESCR.  ORG.  DESTINO
		
		
		
		this.addColuna("Documento", 20, RelatorioRapido.ESQUERDA, false); 			// NUM. PROCESSO
		
		this.addColuna("Dt.Saída", 12, RelatorioRapido.ESQUERDA, false); 	// DATA DESPACHO
		
		this.addColuna("Lotação Destino", 40, RelatorioRapido.ESQUERDA, false); 	// ORG.  DESTINO  + DESCR.  ORG.  DESTINO
		
		this.addColuna("Descrição", 45, RelatorioRapido.ESQUERDA, false); 		// DESCRICAO DOCUMENTO
		
		
		this.addColuna("Total ", 40, RelatorioRapido.ESQUERDA, true); // pai
		
		return this;

	}
	
	 
	private String montarConsulta() {

		StringBuilder sql = new StringBuilder("SELECT ");
		sql.append(" ec.CODIFICACAO CODIFICACAO,ec.DESCR_CLASSIFICACAO  DESCR_ASSUNTO, ");
		sql.append(" udl2.SIGLA_ORGAO_USU  ||dl2.SIGLA_LOTACAO  SIGLA_LOTA_ORIGEM,    dl2.NOME_LOTACAO  NOME_LOTA_ORIGEM,");
		sql.append(" u.acronimo_orgao_usu || '-' ||	f.sigla_forma_doc || '-' ||	ed.ano_emissao || '/' ||	lpad (ed.num_expediente, 5, '0') DOCUMENTO,");
		sql.append(" em.DT_TIMESTAMP  	DT_SAIDA ,");
		sql.append(" udl.SIGLA_ORGAO_USU  ||dl.SIGLA_LOTACAO  SIGLA_LOTA_DESTINO,    dl.NOME_LOTACAO  NOME_LOTA_DESTINO,");
		sql.append(" ed.DESCR_DOCUMENTO  DESCR_DOCUMENTO, udl.NM_ORGAO_USU PAI,");
		sql.append(" ec.CODIFICACAO||dl2.SIGLA_LOTACAO|| udl.NM_ORGAO_USU CHAVE_GRUPO");
		
		sql.append(" FROM EX_MOVIMENTACAO em  ");
		sql.append(" INNER JOIN EX_MOBIL m  ON m.ID_MOBIL = em.ID_MOBIL");
		sql.append(" INNER JOIN EX_DOCUMENTO ed  ON ed.ID_DOC = m.ID_DOC ");
		sql.append(" INNER JOIN corporativo.cp_orgao_usuario u 	ON (ed.ID_ORGAO_USU  = u.id_orgao_usu)");
		sql.append(" INNER JOIN siga.ex_forma_documento f  		ON (f.id_forma_doc = ed.id_forma_doc)  ");
		sql.append(" INNER JOIN EX_CLASSIFICACAO ec  ON ec.ID_CLASSIFICACAO = ed.ID_CLASSIFICACAO ");
		sql.append(" LEFT JOIN CORPORATIVO.DP_LOTACAO dl  ON dl.ID_LOTACAO = em.ID_LOTA_RESP  ");
		sql.append(" LEFT JOIN  CORPORATIVO.DP_LOTACAO dl2 ON dl2.ID_LOTACAO = em.ID_LOTA_CADASTRANTE");
		sql.append(" INNER JOIN corporativo.cp_orgao_usuario udl2 ON udl2.ID_ORGAO_USU = dl2.ID_ORGAO_USU");
		sql.append(" INNER JOIN corporativo.cp_orgao_usuario udl ON udl.ID_ORGAO_USU = dl.ID_ORGAO_USU ");
		sql.append(" WHERE  em.ID_TP_MOV = 3   AND	em.ID_MOV_CANCELADORA  IS NULL");
		sql.append(" AND	f.id_tipo_forma_doc= :idTipoFormaDoc");
		sql.append(" AND	 ec.id_classificacao IN (:assuntos ) AND em.ID_LOTA_CADASTRANTE IN (:setoresSubordinados)");
		sql.append(" AND (em.DT_TIMESTAMP BETWEEN  to_date(:dataInicial,'DD/MM/YYYY') AND to_date(:dataFinal,'DD/MM/YYYY'))");
		sql.append(" ORDER BY ed.ID_CLASSIFICACAO , em.ID_LOTA_CADASTRANTE, DT_TIMESTAMP ");
		return sql.toString();
	}

	@Override
	public  String  processarDadosCSV(){
		
		List<Object[]> lista = consultar();
		
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		StringBuffer sb = new StringBuffer();
				
		sb.append(configurarRelatorioCSV());
		
		for (Object[] array : lista) { 
			
			sb.append( String.valueOf( array[0] ) );//CODIFICACAO
			sb.append(";");
			sb.append(  String.valueOf( array[1] ));//DESCR_ASSUNTO
			sb.append(";");
			sb.append(   String.valueOf( array[2]   ) ); //SIGLA_LOTACAO_ORIGEM
			sb.append(";");
			sb.append(  String.valueOf( array[3]  ) );//NOME_ORGAO_ORIGEM
			sb.append(";");
			sb.append(String.valueOf( array[4] ));//DOCUMENTO
			sb.append(";");
			sb.append ( String.valueOf(formatter.format( array[5] )));//DT_SAIDA
			sb.append(";");
			sb.append( String.valueOf( array[6] )  ); //SIGLA_LOTACAO_DESTINO
			sb.append(";");
			sb.append( String.valueOf( array[7] )); //NOME_ORGAO_DESTINO
			sb.append(";");
			sb.append(	 String.valueOf( array[8] )  	);//DESCR_MOVIMENTO
			sb.append(";");
			sb.append( String.valueOf( array[9])); //PAI
			sb.append(";");
			sb.append(System.lineSeparator());
		}
			
		return Texto.removeAcento( sb.toString() );
	}
	

	@Override
	public String configurarRelatorioCSV() {
		StringBuffer sb = new StringBuffer();
		sb.append("CODIFICACAO") .append(";");
		sb.append("DESCR_ASSUNTO") .append(";");
		sb.append("SIGLA_LOTACAO_ORIGEM") .append(";"); 
		sb.append("NOME_ORGAO_ORIGEM") .append(";"); 
		sb.append("DOCUMENTO") .append(";");
		sb.append("DT_SAIDA") .append(";");
		sb.append("SIGLA_LOTACAO_DESTINO") .append(";"); 
		sb.append("NOME_ORGAO_DESTINO") .append(";"); 
		sb.append("DESCR_MOVIMENTO") .append(";");
		sb.append("PAI") .append(";");
		sb.append(System.lineSeparator());
		return sb.toString();
	}
	
	@Override
	public Collection processarDados() throws Exception {

		List<Object[]> lista = consultar();
		
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		List<String> listaFinal = new ArrayList<String>();
		
	 
		for (Object[] array : lista) {

			 String codificacao =String.valueOf( array[0]);
			 String lotaOrigem = String.valueOf( array[2] );
			 String pai =  String.valueOf( array[9]);
			 String filtroTotal = String.valueOf(array[10]);
			 
			listaFinal.add(  codificacao  +" "+ String.valueOf( array[1] ));//CODIFICACAO. ASSUNTO  + DESCR. ASSUNTO
			listaFinal.add(	lotaOrigem +"-"+ String.valueOf( array[3] ) 	);//ORG. ORIGEM	 + DESCR. ORG. ORIGEM
			 
			listaFinal.add( String.valueOf( array[4] ));//NUM. PROCESSO
			listaFinal.add( String.valueOf(formatter.format( array[5] )));//DATA saida
			listaFinal.add( String.valueOf( array[6] ) +" "+ String.valueOf( array[7] ));//destino
			listaFinal.add( array[8]  != null ?  String.valueOf( array[8] )  :"");//descricao
			listaFinal.add( pai +" " + calcularTotal(lista, array[10]));//pai
			
			// qtd de ocorrencias da mesma classificacao + mesma lotacao origem + mesma lotacao destino
			
		}
		
		return listaFinal;
	}

	private String calcularTotal(List<Object[]> lista, Object filtroTotal) {
		 
		long total = lista.stream().
				 filter(f -> f[10] == filtroTotal  )
						 .count();
		return Long.toString(total);
	}

	private List<Object[]> consultar() {
		Query query = ContextoPersistencia.em().createNativeQuery( montarConsulta() );
		
		query.setParameter("assuntos", new ArrayList<String>(Arrays.asList(String.valueOf( parametros.get("listaAssunto") ).split(","))));
		
		query.setParameter("setoresSubordinados", new ArrayList<String>(Arrays.asList(String.valueOf( parametros.get("listaSetoresSubordinados") ).split(","))));
		
		query.setParameter("idTipoFormaDoc",Integer.valueOf(   String.valueOf(  parametros.get("idTipoFormaDoc")  ) ));
		
		query.setParameter("dataInicial", (String) parametros.get("dataInicial"));
		
		query.setParameter("dataFinal", (String) parametros.get("dataFinal"));

		
		List<Object[]> lista = query.getResultList();
		
		return lista;
	}
	 
	 
}
