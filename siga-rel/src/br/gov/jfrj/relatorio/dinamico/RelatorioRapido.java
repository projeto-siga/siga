/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.relatorio.dinamico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

//import org.hibernate.Query;

import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import ar.com.fdvs.dj.domain.builders.GroupBuilder;
import ar.com.fdvs.dj.domain.constants.GroupLayout;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;
import ar.com.fdvs.dj.domain.entities.columns.SimpleColumn;

/**
 * Permite a geração rápida de relatórios. Basta o cliente desta classe seguir
 * os seguintes passos:<BR>
 * 1) Instanciar esta classe<BR>
 * 2) Adicionar colunas com o método .addColuna()<BR>
 * 3) Injetar os dados no relatório com o método .setDados()<BR>
 * 4) Executar o método .getRelatorio() e passar o seu retorno para um viewer
 * (Ex: JasperViewer.viewReport ou JasperExportManager.exportReportToPdfStream)
 * <BR>
 * <BR>
 * Os dados para este tipo de ralatório devem ser Set ou List.
 * 
 * Use a classe RelatorioTemple para agilizar/padronizar o procedimento acima.
 */
public class RelatorioRapido extends AbstractRelatorioBaseBuilder {

	public final static int ESQUERDA = 0;
	public final static int CENTRO = 1;
	public final static int DIREITA = 2;

	private List<Coluna> colunas;
	private int porcento;

	public RelatorioRapido(Map parametros) throws DJBuilderException {
		super(parametros);
		porcento = this.options.getPrintableWidth() / 100;
		this.setLeftMargin(10 * porcento);
		this.setRightMargin(0 * porcento);
		this.setBottomMargin(0 * porcento);
		this.setTopMargin(0 * porcento);
		colunas = new ArrayList<Coluna>();
	}

	/**
	 * Adiciona uma coluna ao relatório.
	 * 
	 * @param titulo
	 *            Título que aparece na coluna.
	 * @param tamanho
	 *            Tamanho da coluna na linha. Apesar de aceitar qualquer valor
	 *            inteiro, use entre 1 e 100 para se basear em porcentagem.<BR>
	 *            Se a coluna for agrupada o tamanho é sempre 100, não
	 *            importando o valor colocado.
	 * @param alinhamento
	 *            Alinhamento dos dados da coluna. Pode ser:<BR>
	 *            RelatorioRapido.ESQUERDA<BR>
	 *            RelatorioRapido.CENTRO<BR>
	 *            RelatorioRapido.DIREITO
	 */
	public void addColuna(String titulo, int tamanho, int alinhamento,
			boolean isAgrupado) {
		// ESTE MÉTODO DEVE SER APERFEIÇOADO. A PORCENTAGEM DA LARGURA NÃO ESTÁ
		// DE ACORDO COM O TAMANHO DA TELA
		Coluna c = criarColuna( titulo, tamanho, alinhamento, isAgrupado);
		colunas.add(c);
	}

	public void addColuna(String nome, String titulo, int tamanho, int alinhamento,
			boolean isAgrupado, boolean isHyperlink) {
		Coluna c = criarColuna( titulo, tamanho, alinhamento, isAgrupado);
		c.setHyperlink(isHyperlink);
		colunas.add(c);
	}

	private Coluna criarColuna( String titulo, int tamanho, int alinhamento,
			boolean isAgrupado) {
		Coluna c = new Coluna();
		c.setTitulo(titulo);
		c.setTamanho(tamanho);
		c.setAlinhamento(alinhamento);
		c.setAgrupado(isAgrupado);
		return c;
	}

	public void delColuna(String titulo, int tamanho, int alinhamento,
			boolean isAgrupado) {
		Coluna c = criarColuna( titulo, tamanho, alinhamento, isAgrupado);
		colunas.remove(c);
	}

	@Override
	public JasperPrint getRelatorioJasperPrint() throws JRException {
		processarColunas();
		return super.getRelatorioJasperPrint();
	}

	private void processarColunas() {

		int i = 0;

		for (Iterator iterator = colunas.iterator(); iterator.hasNext();) {
			Coluna c = (Coluna) iterator.next();

			try {
				Style estiloAlinhamento = copiarEstilo(estiloColuna);
				HorizontalAlign alinhamento = null;
				switch (c.getAlinhamento()) {
				case ESQUERDA:
					alinhamento = HorizontalAlign.LEFT;
					break;
				case CENTRO:
					alinhamento = HorizontalAlign.CENTER;
					break;
				case DIREITA:
					alinhamento = HorizontalAlign.RIGHT;
					break;
				}

				estiloAlinhamento.setHorizontalAlign(alinhamento);

				AbstractColumn coluna = ColumnBuilder.getInstance().setTitle(
						c.getTitulo()).setWidth(
						c.getTamanho()
								* (this.options.getPrintableWidth() / 100))
						.setColumnProperty(
								IConstantes.PREFIXO_COLUNA_PROPERTY + i,
								String.class.getName()).setStyle(
								estiloAlinhamento)
								.build();

				if (c.isHyperlink()) {
					this.addField(IConstantes.PREFIXO_HYPERLINK_PARAMETER + i,
							String.class.getName());
					((SimpleColumn) coluna)
							.getColumnProperty()
							.addFieldProperty(
									IConstantes.PREFIXO_HYPERLINK_PARAMETER + i,
									String.class.getName());
				}

				this.addColumn(coluna);

				if (c.isAgrupado()) {
					coluna.setStyle(estiloTituloGrupo);
					GroupBuilder grupoBuilder = new GroupBuilder();
					DJGroup grupo = grupoBuilder.setCriteriaColumn(
							(PropertyColumn) coluna).setGroupLayout(
							GroupLayout.VALUE_IN_HEADER).build();
					this.addGroup(grupo);
					this.setRightMargin(10 * porcento);

				}
				i++;
			} catch (ColumnBuilderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setDados(Collection dados) throws Exception {

		try {
			Object[] arrayDados = dados.toArray();
			dados = null;
			System.gc();
			Collection colecaoDados = new ArrayList<String>();
			Map<String, String> mapDados = new TreeMap<String, String>();
			int atual = 0;
			while (atual < arrayDados.length) {

				for (int j = 0; j < colunas.size(); j++) {
					mapDados.put(IConstantes.PREFIXO_COLUNA_PROPERTY + j,
							(String) arrayDados[atual]);

					if (colunas.get(j).isHyperlink()) {
						mapDados.put(IConstantes.PREFIXO_HYPERLINK_PARAMETER
								+ j, (String) arrayDados[atual + 1]);
						arrayDados[atual] = null;
						atual++;
					}

					arrayDados[atual] = null;
					atual++;
				}

				colecaoDados.add(mapDados);
				mapDados = new TreeMap<String, String>();
			}

			ds = new JRMapCollectionDataSource(colecaoDados);
		} catch (ClassCastException e) {
			throw new Exception(
					"****Os dados do relatório rápido devem ser Strings!");
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new Exception(
					"****O número de colunas nos dados não está de acordo com o layout definido!\n"
							+ "Se você está usando hyperlink no relatório, não se esqueça de colocar os dois dados: TÍTULO e ENDEREÇO do link!");
		}
	}

//	public void setDados(Query dados) throws Exception {
//		throw new Exception(
//				"Método desativado para relatório rápido! Use setDados(Set<String> dados)");
//	}

}

class Coluna {
	private String titulo;
	private String nome;
	private Integer tamanho;
	private Integer alinhamento;
	private boolean agrupado;
	private boolean hyperlink;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String titulo) {
		this.nome = nome;
	}
	
	public Integer getTamanho() {
		return tamanho;
	}

	public void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}

	public Integer getAlinhamento() {
		return alinhamento;
	}

	public void setAlinhamento(Integer alinhamento) {
		this.alinhamento = alinhamento;
	}

	public boolean isAgrupado() {
		return agrupado;
	}

	public void setAgrupado(boolean agrupado) {
		this.agrupado = agrupado;
	}

	public boolean isHyperlink() {
		return hyperlink;
	}

	public void setHyperlink(boolean hyperlink) {
		this.hyperlink = hyperlink;
	}

	public int compareTo(Object o) {
		return 0;
	}

}
