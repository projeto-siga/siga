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
import br.gov.jfrj.relatorio.dinamico.RelatorioRapido;
import br.gov.jfrj.relatorio.dinamico.RelatorioTemplate;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import net.sf.jasperreports.engine.JRException;

public class RelPermanenciaSetorAssunto extends RelatorioTemplate {

	List<String> listaAssunto = new ArrayList<>();
	
	List<String> listaSetoreSubordinado = new ArrayList<>();

	public RelPermanenciaSetorAssunto(Map parametros) throws DJBuilderException {
		super(parametros);
	
		if (parametros.get("listaAssunto") == null) {
			throw new DJBuilderException("Assunto deve ser escolhido!");
		}
		
		if (parametros.get("listaSetoresSubordinados") == null) {
			throw new DJBuilderException("Setor subordinado deve ser escolhido!");
		}
		

		 listaAssunto =  new ArrayList<String>(Arrays.asList(String.valueOf( parametros.get("listaAssunto") ).split(",")));
		
		 listaSetoreSubordinado =  new ArrayList<String>(Arrays.asList(String.valueOf( parametros.get("listaSetoresSubordinados") ).split(",")));

		if (parametros.get("secaoUsuario") == null) {
			throw new DJBuilderException(
					"Parâmetro secaoUsuario não informado!");
		}
		
		if (parametros.get("lotacaoTitular") == null) {
			throw new DJBuilderException("Parâmetro lotação não informado!");
		}
 
		if (parametros.get("link_siga") == null) {
			throw new DJBuilderException("Parâmetro link_siga não informado!");
		}
	}

	@Override
	public AbstractRelatorioBaseBuilder configurarRelatorio()
			throws DJBuilderException, JRException {

		this.setTitle("Relatório de Permanência por Setor e Assunto");

		this.addColuna("Número", 18, RelatorioRapido.ESQUERDA, false); 			// NUM. PROCESSO
		
		this.addColuna("Dt.Movimento", 15, RelatorioRapido.ESQUERDA, false); 	// DATA DESPACHO
		
		this.addColuna("Despacho", 25, RelatorioRapido.ESQUERDA, false); 		// COD. DESPACHO + DESCR.  DESPACHO
		
		this.addColuna("Assunto", 30, RelatorioRapido.ESQUERDA, true); 			// COD.  ASSUNTO +  DESCR.  ASSUNTO,
		
		this.addColuna("Órgão Origem", 40, RelatorioRapido.ESQUERDA, false); 	// ORG. ORIGEM  +  DESCR.  ORG.  ORIGEM
		
		this.addColuna("Órgão Destino", 40, RelatorioRapido.ESQUERDA, true); 	// ORG.  DESTINO  + DESCR.  ORG.  DESTINO
		
		this.addColuna("Digitador", 13, RelatorioRapido.ESQUERDA, false); 		// MATR.  DIGITADOR
		
		this.addColuna("Dias", 8, RelatorioRapido.ESQUERDA, false); 			// TEMPO (DIAS) PERMANENCIA

		return this;

	}

	private String montarConsulta() {

		String sql = "SELECT X1.NUM_PROCESSO, X1.DATA_DESPACHO, X1.COD_DESPACHO, X1.DESCR_DESPACHO, X1.ID_CLASSIFICACAO, "
						+ "		X1.COD_ASSUNTO, X1.DESCR_ASSUNTO, X1.ORG_ORIGEM,X1.DESCR_ORG_ORIGEM, X1.ORG_DESTINO, X1.DESCR_ORG_DESTINO, "
						+ "		X1.MATR_DIGITADOR, X1.TEMPO "
						+ "	FROM ( "
						+ "		SELECT u.acronimo_orgao_usu || '-' ||	f.sigla_forma_doc || '-' ||	d.ano_emissao || '/' ||	lpad (d.num_expediente, 5, '0') NUM_PROCESSO, 	m.dt_timestamp DATA_DESPACHO, "
						+ "			m.id_tp_mov COD_DESPACHO, 	tm.descr_tipo_movimentacao DESCR_DESPACHO, "
						+ "			c.codificacao COD_ASSUNTO, c.descr_classificacao DESCR_ASSUNTO, c.id_classificacao, ( "
						+ "				SELECT l.id_lotacao "
						+ "			  	FROM corporativo.dp_lotacao l2 "
						+ "			 	WHERE l2.id_lotacao = (	"
						+ "					SELECT m3.id_lota_resp "
						+ "					FROM siga.ex_movimentacao m3 "
						+ "					WHERE m3.id_mov = ( "
						+ "						SELECT max (m2.id_mov) "
						+ "						FROM siga.ex_movimentacao m2 "
						+ "						INNER JOIN siga.ex_mobil mb2 "
						+ "							ON (mb2.id_mobil = m2.id_mobil) "
						+ "						WHERE mb2.id_doc = d.id_doc AND m2.id_mov < m.id_mov ))) ORG_ORIGEM, ( "
						+ "				SELECT l.nome_lotacao "
						+ "				FROM corporativo.dp_lotacao l2 "
						+ "				WHERE l2.id_lotacao = ( "
						+ "					SELECT m3.id_lota_resp "
						+ "					FROM siga.ex_movimentacao m3 "
						+ "					WHERE m3.id_mov = ( "
						+ "						SELECT max (m2.id_mov) "
						+ "						FROM siga.ex_movimentacao m2 "
						+ "						INNER JOIN siga.ex_mobil mb2 "
						+ "							ON (mb2.id_mobil = m2.id_mobil) "
						+ "						WHERE mb2.id_doc = d.id_doc AND m2.id_mov < m.id_mov ))) DESCR_ORG_ORIGEM, "
						+ "			l.id_lotacao ORG_DESTINO, l.nome_lotacao DESCR_ORG_DESTINO, "
						+ "			p.matricula MATR_DIGITADOR, TO_NUMBER(SUBSTR( sysdate - m.dt_timestamp,2, instr(sysdate - m.dt_timestamp,' ')-2)) as TEMPO "
						+ "		FROM siga.ex_documento d "
						+ "		INNER JOIN siga.ex_classificacao c "
						+ "			ON (c.id_classificacao = d.id_classificacao) "
						+ "		INNER JOIN siga.ex_mobil mb"
						+ "			ON (mb.id_doc = d.id_doc) "
						+ "		INNER JOIN siga.ex_movimentacao m "
						+ "			ON (m.id_mobil = mb.id_mobil) "
						+ "		INNER JOIN siga.ex_tipo_movimentacao tm "
						+ "			ON (tm.id_tp_mov = m.id_tp_mov) "
						+ "		INNER JOIN siga.ex_classificacao c "
						+ "			ON (c.id_classificacao = d.id_classificacao) "
						+ "		INNER JOIN corporativo.dp_lotacao l "
						+ "			ON (d.id_lota_cadastrante = l.id_lotacao) "
						+ "		INNER JOIN corporativo.cp_orgao_usuario u"
						+ "			ON (l.id_orgao_usu = u.id_orgao_usu) "
						+ "		INNER JOIN siga.ex_forma_documento f"
						+ "			ON (f.id_forma_doc = d.id_forma_doc) "
						+ "		INNER JOIN corporativo.dp_pessoa p "
						+ "			ON m.id_cadastrante = p.id_pessoa "
						+ "	 	WHERE c.id_classificacao IN ( :assuntos ) AND l.id_lotacao IN ( :setoresSubordinados) " 
						+ "		ORDER BY NUM_PROCESSO , DATA_DESPACHO DESC ) x1 "
						+ "	INNER JOIN ( "
						+ "		SELECT u.acronimo_orgao_usu || '-' || f.sigla_forma_doc || '-' || 	d.ano_emissao || '/' || lpad (d.num_expediente, 5, '0') NUM_PROCESSO, "
						+ "			MAX(m.dt_timestamp) DATA_DESPACHO "
						+ "		FROM siga.ex_documento d "
						+ "		INNER JOIN siga.ex_classificacao c"
						+ "			ON (c.id_classificacao = d.id_classificacao) "
						+ "		INNER JOIN siga.ex_mobil mb "
						+ "			ON (mb.id_doc = d.id_doc) "
						+ "		INNER JOIN siga.ex_movimentacao m "
						+ "			ON (m.id_mobil = mb.id_mobil) "
						+ "		INNER JOIN siga.ex_tipo_movimentacao tm "
						+ "			ON (tm.id_tp_mov = m.id_tp_mov) "
						+ "		INNER JOIN siga.ex_classificacao c "
						+ "			ON (c.id_classificacao = d.id_classificacao) "
						+ "		INNER JOIN corporativo.dp_lotacao l "
						+ "			ON (d.id_lota_cadastrante = l.id_lotacao) "
						+ "		INNER JOIN corporativo.cp_orgao_usuario u "
						+ "			ON (l.id_orgao_usu = u.id_orgao_usu) "
						+ "		INNER JOIN siga.ex_forma_documento f "
						+ "			ON (f.id_forma_doc = d.id_forma_doc) "
						+ "		WHERE (tm.id_tp_mov >77) OR tm.id_tp_mov in(1,3,4,9,17,18,19,20,23,48,49,56) "
						+ "		GROUP BY u.acronimo_orgao_usu,f.sigla_forma_doc,d.ano_emissao,d.num_expediente "
						+ "		ORDER BY NUM_PROCESSO desc ) x2 "
						+ "		ON x1.NUM_PROCESSO = X2.NUM_PROCESSO AND X1.DATA_DESPACHO = X2.DATA_DESPACHO AND x1.COD_DESPACHO <> 48 "
						+ "	GROUP BY X1.NUM_PROCESSO,	X1.DATA_DESPACHO, X1.COD_DESPACHO, X1.DESCR_DESPACHO, X1.COD_ASSUNTO, X1.DESCR_ASSUNTO, X1.ID_CLASSIFICACAO, "
						+ "			 X1.ORG_ORIGEM, X1.DESCR_ORG_ORIGEM, X1.ORG_DESTINO, X1.DESCR_ORG_DESTINO, X1.MATR_DIGITADOR, X1.TEMPO "
						+ "	ORDER BY x1.ORG_DESTINO, x1.NUM_PROCESSO ";


		return sql;
	}

	
	@Override
	public Collection processarDados() throws Exception {
 		
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		List<String> d = new ArrayList<String>();

		Query query = ContextoPersistencia.em().createNativeQuery( montarConsulta() );
		
		query.setParameter("assuntos", listaAssunto);
		
		query.setParameter("setoresSubordinados",listaSetoreSubordinado);
		
		List<Object[]> lista = query.getResultList();
		
		List<String> listaFinal = new ArrayList<String>();

		for (Object[] array : lista) {

		//TODO validar assunto e destino

			String codAssunto =  array[4] != null ? String.valueOf( array[4] ) : null ;
			
			String codDestino =   array[9] != null ? String.valueOf( array[9] ) : null ;
			
				listaFinal.add( String.valueOf( array[0] ));//NUM. PROCESSO
				
				listaFinal.add( String.valueOf(formatter.format( array[1] )));//DATA DESPACHO
				
				listaFinal.add( String.valueOf( array[2] ) +" "+ String.valueOf( array[3] ));//COD. DESPACHO + DESCR. DESPACHO,
				
				listaFinal.add( String.valueOf( array[5] ) +" "+ String.valueOf( array[6] ));//COD. ASSUNTO  + DESCR. ASSUNTO
				
				listaFinal.add(	array[7] != null ? String.valueOf( array[7] ) +" "+ String.valueOf( array[8] ) : ""		);//ORG. ORIGEM	 + DESCR. ORG. ORIGEM
				
				listaFinal.add( array[9]  != null ?  String.valueOf( array[9] ) +" "+ String.valueOf( array[10]  ):"");//ORG. DESTINO 	 + DESCR. ORG. DESTINO
				
				listaFinal.add( String.valueOf( array[11]));//MATR. DIGITADOR
				
				listaFinal.add(String.valueOf( array[12]));//TEMPO
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
