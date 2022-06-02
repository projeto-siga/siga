package br.gov.jfrj.siga.ex.relatorio.dinamico.relatorios;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.Query;

import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import br.gov.jfrj.relatorio.dinamico.AbstractRelatorioBaseBuilder;
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.base.util.Texto;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import net.sf.jasperreports.engine.JRException;

public class RelDocsQuantidadeGerados extends RelatorioTemplate {

	private Map<String, Long> chaveGrupoCount = null;

	public RelDocsQuantidadeGerados(Map parametros) throws DJBuilderException {
		super(parametros);
	}

	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio() 	throws   JRException {

		this.setTitle("Relatório de Saída de Documentos por Setor");
		this.setSubtitle( "Perído: " + parametros.get("dataInicial").toString() + " a " + parametros.get("dataFinal").toString());

		this.addColuna("Assunto", 40, RelatorioRapido.ESQUERDA, true); 	// ORG.  DESTINO  + DESCR.  ORG.  DESTINO

		this.addColuna("Lotação ", 40, RelatorioRapido.ESQUERDA, true); 	// ORG.  DESTINO  + DESCR.  ORG.  DESTINO
		
		this.addColuna("Total ", 40, RelatorioRapido.ESQUERDA, true); // pai

		
		
		this.addColuna("Documento", 22, RelatorioRapido.ESQUERDA, false); 			// NUM. PROCESSO
		
		this.addColuna("Dt de Saída", 12, RelatorioRapido.ESQUERDA, false); 	// DATA DESPACHO
		
		this.addColuna("Órgão/Lotação Destino", 30, RelatorioRapido.ESQUERDA, false); 	// ORG.  DESTINO  + DESCR.  ORG.  DESTINO
		
		this.addColuna("Descrição", 70, RelatorioRapido.ESQUERDA, false); 		// DESCRICAO DOCUMENTO
		
		
		return this;

	}
	
	 
	private String montarConsulta() {

		StringBuilder sql = new StringBuilder("SELECT ");
		sql.append(" ec.CODIFICACAO CODIFICACAO,");
		sql.append(" ec.DESCR_CLASSIFICACAO  DESCR_ASSUNTO, ");
		sql.append(" lotaorigem.NOME_LOTACAO  LOTACAO_ORIGEM, ");
		sql.append(" u.acronimo_orgao_usu || '-' ||	f.sigla_forma_doc || '-' ||	ed.ano_emissao || '/' ||	lpad (ed.num_expediente, 5, '0') DOCUMENTO,");
		sql.append(" em.DT_TIMESTAMP  	DT_SAIDA ,");
		sql.append(" lotadestino.NOME_LOTACAO LOTACAO_DESTINO,");
		sql.append(" ed.DESCR_DOCUMENTO  DESCR_DOCUMENTO,");
		sql.append(" lotapai.NOME_LOTACAO PAI,");
		sql.append(" ec.CODIFICACAO || lotaorigem.SIGLA_LOTACAO || lotapai.NOME_LOTACAO CHAVE_GRUPO");
		
		sql.append(" FROM siga.ex_movimentacao em  ");
		sql.append(" inner join siga.ex_mobil m  ON m.ID_MOBIL = em.ID_MOBIL");
		sql.append(" inner join siga.ex_documento ed  ON ed.ID_DOC = m.ID_DOC ");
		sql.append(" inner join corporativo.cp_orgao_usuario u 	ON (ed.ID_ORGAO_USU  = u.id_orgao_usu)");
		sql.append(" inner join siga.ex_forma_documento f  		ON (f.id_forma_doc = ed.id_forma_doc)  ");
		sql.append(" inner join siga.ex_classificacao ec  ON ec.ID_CLASSIFICACAO = ed.ID_CLASSIFICACAO ");
		sql.append(" left  join corporativo.dp_lotacao lotadestino  ON lotadestino.ID_LOTACAO = em.ID_LOTA_RESP  ");
		sql.append(" left  join corporativo.dp_lotacao lotaorigem ON lotaorigem.ID_LOTACAO = em.ID_LOTA_CADASTRANTE");

		sql.append(" left join corporativo.dp_lotacao lotapai ON lotapai.SIGLA_LOTACAO = TO_CHAR( lotadestino.ID_ORGAO_USU )   ");
		
		
		sql.append(" WHERE  em.ID_TP_MOV = 3   and	em.ID_MOV_CANCELADORA  IS NULL");
		sql.append(" and	f.id_tipo_forma_doc= :idTipoFormaDoc");
		sql.append(" and	 ec.id_classificacao IN (:assuntos ) and em.ID_LOTA_CADASTRANTE IN (:setoresSubordinados)");
		sql.append(" and (em.DT_TIMESTAMP between  to_date(:dataInicial,'DD/MM/YYYY') and to_date(:dataFinal,'DD/MM/YYYY'))");
		
		sql.append(" GROUP BY ");
		sql.append(" ec.CODIFICACAO , ec.DESCR_CLASSIFICACAO ,	 lotaorigem.NOME_LOTACAO,  ");
		sql.append(" u.acronimo_orgao_usu || '-' ||	f.sigla_forma_doc || '-' ||	ed.ano_emissao || '/' ||	lpad (ed.num_expediente, 5, '0') ,");
		sql.append(" em.DT_TIMESTAMP  , lotadestino.NOME_LOTACAO , ed.DESCR_DOCUMENTO  , lotapai.NOME_LOTACAO,"); 
		sql.append(" ec.CODIFICACAO || lotaorigem.SIGLA_LOTACAO || lotapai.NOME_LOTACAO ");
		
		sql.append(" order by ec.CODIFICACAO , lotaorigem.NOME_LOTACAO, DT_TIMESTAMP ");
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
			sb.append(   String.valueOf( array[2]   ) ); //LOTACAO_ORIGEM
			sb.append(";");
			sb.append(String.valueOf( array[3] ));//DOCUMENTO
			sb.append(";");
			sb.append ( String.valueOf(formatter.format( array[4] )));//DT_SAIDA
			sb.append(";");
			sb.append( String.valueOf( array[5] )  ); //LOTACAO_DESTINO
			sb.append(";");
			sb.append(	 String.valueOf( array[6] )  	);//DESCR_DOCUMENTO
			sb.append(";");
			sb.append( String.valueOf( array[7])); //PAI
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
		sb.append("LOTACAO_ORIGEM") .append(";"); 
		sb.append("DOCUMENTO") .append(";");
		sb.append("DT_SAIDA") .append(";");
		sb.append("LOTACAO_DESTINO") .append(";"); 
		sb.append("DESCR_DOCUMENTO") .append(";");
		sb.append("PAI") .append(";");
		sb.append(System.lineSeparator());
		return sb.toString();
	}
	
	@Override
	public Collection processarDados() throws Exception {

		List<Object[]> lista = consultar();
		
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		List<String> listaFinal = new ArrayList<String>();
		
		chaveGrupoCount =  lista.stream().map(o -> String.valueOf(o[8]))
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		
	 
		for (Object[] array : lista) {
			
			 listaFinal.add(  String.valueOf( array[0])  +" "+ String.valueOf( array[1] ));//CODIFICACAO. ASSUNTO  + DESCR. ASSUNTO
			 listaFinal.add(	  String.valueOf( array[2] ) 	);//ORG. ORIGEM	 + DESCR. ORG. ORIGEM
			 
			 listaFinal.add( String.format("Total  %s:",String.valueOf( array[7] )) +  this.chaveGrupoCount.get(String.valueOf(array[8])));//pai

			 listaFinal.add( String.valueOf( array[3] ));//NUM. PROCESSO
			 listaFinal.add( String.valueOf(formatter.format( array[4] )));//DATA saida
			 listaFinal.add( String.valueOf( array[5] )  );//destino
			 listaFinal.add( array[6]  != null ?  String.valueOf( array[6] )  :"");//descricao
			
			// qtd de ocorrencias da mesma classificacao + mesma lotacao origem + mesma lotacao destino
			
		}
		
		return listaFinal;
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
