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
package br.gov.jfrj.siga.ex.bl.BIE;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import br.gov.jfrj.siga.dp.CpOrgaoUsuario;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.hibernate.ExDao;

public class HierarquizadorBoletimInternoCJF {

	private class DescricaoNodoComparator implements Comparator<Nodo> {

		public int compare(Nodo o1, Nodo o2) {
			return o1.getDescr().compareTo(o2.getDescr());
		}
	}

	private class CidadeComparator implements Comparator<ExDocumento> {

		public int compare(ExDocumento o1, ExDocumento o2) {
			return o1.getLocalidadeString().compareTo(o2.getLocalidadeString());
		}

	}

	private class TipoDataComparator implements Comparator<ExDocumento> {

		public int compare(ExDocumento o1, ExDocumento o2) {
			if (o1.getExFormaDocumento().getIdFormaDoc() > o2
					.getExFormaDocumento().getIdFormaDoc())
				return 1;
			else if (o1.getExFormaDocumento().getIdFormaDoc() < o2
					.getExFormaDocumento().getIdFormaDoc())
				return -1;
			else if (o1.getDtFinalizacao().after(o2.getDtFinalizacao()))
				return 1;
			else if (o1.getDtFinalizacao().before(o2.getDtFinalizacao()))
				return -1;
			else
				return 0;
		}
	}

	private class DataComparator implements Comparator<ExDocumento> {

		public int compare(ExDocumento o1, ExDocumento o2) {
			if (o1.getDtFinalizacao().after(o2.getDtFinalizacao()))
				return 1;
			else if (o1.getDtFinalizacao().before(o2.getDtFinalizacao()))
				return -1;
			else
				return 0;
		}
	}

	
	

	

	private List<ExDocumento> docsAPublicar;
	private List<NodoMaior> nodosPrincipais;
	private HashMap<Long, Integer> ordemPorTipo;

	public HierarquizadorBoletimInternoCJF(CpOrgaoUsuario orgao) {
		this(orgao, ExDao.getInstance().consultarPorModeloParaPublicar(orgao));
	}

	public HierarquizadorBoletimInternoCJF(CpOrgaoUsuario orgao,
			List<ExDocumento> docsAPublicar) {

		setDocsAPublicar(docsAPublicar);

		criaEstruturaNodos();

		for (ExDocumento doc : getDocsAPublicar()) 
			alocaDocumento(doc);
		
		
		for (NodoMaior n : nodosPrincipais) {
			for (NodoMenor n2 : n.nodos) {
				n2.ordena();
				List<ExDocumento> novaLista = new ArrayList<ExDocumento>();
				for (ExDocumento docLista : n2.getExDocumentoSet()) {
					novaLista.add(docLista);
					if(docLista.getExDocumentoFilhoSet() != null) {
						for (ExDocumento docFilho : docLista.getExDocumentoFilhoSet()) {
							if(docFilho.getExFormaDocumento().getIdFormaDoc() == 60 && docFilho.isAssinado())
								novaLista.add(docFilho);
						}
					}
				}
				n2.setExDocumentoSet(novaLista);
			}
			n.ordena();
		}
	}
	
	public static String enxugaHtml(String html){
		int aPartirDe = 0;
		int posIni = html.indexOf("<p");
		int posFim = html.indexOf(">", posIni);
		
		while(posIni!=-1){
			String sub = html.substring(posIni, posFim);
			if (sub.indexOf("style=\"")!=-1)
				sub = sub.replace("style=\"", "style=\"margin-bottom:7px; margin-top:7px;");
			else 
				sub = sub + " style=\"margin-bottom:7px; margin-top:7px;\" ";
			
			String[] atributos = sub.split(";");
			
			for (String atributo : atributos) {
				if(atributo.contains("text-indent:")) {
					sub = sub.replace(atributo + ";", "");
				}
				if(atributo.contains("text-align:left")) {
					sub = sub.replace(atributo, "text-align:justify");
				}
			}
			
			html = html.substring(0, posIni) + sub + html.substring(posFim, html.length());
			aPartirDe = posFim + 1;
			posIni = html.indexOf("<p", aPartirDe);
			posFim = html.indexOf(">", posIni);
		}
		html = html.replace("&nbsp;", " ");
		return html;
		
	}

	private void criaEstruturaNodos() {
		nodosPrincipais = new ArrayList<NodoMaior>();

		ordemPorTipo = new HashMap<Long, Integer>();
		
		//Documentos da Presidência
		NodoMaior nodoPresidencia = new NodoMaior("Presidência");
		
		adicionaTipoDeDocumentoANodo(nodoPresidencia);

		//Documentos da Vice-Presidência
		NodoMaior nodoVicePresidencia = new NodoMaior("Vice-Presidência");
		
		adicionaTipoDeDocumentoANodo(nodoVicePresidencia);

		//Documentos da EMARF
		NodoMaior nodoEmarf = new NodoMaior("EMARF");
		
		adicionaTipoDeDocumentoANodo(nodoEmarf);

		//Documentos da Secretaria Geral
		NodoMaior nodoSecretariaGeral = new NodoMaior("Secretaria Geral");

		adicionaTipoDeDocumentoANodo(nodoSecretariaGeral);
		
		//Documentos da Secretaria de Recursos Humanos
		NodoMaior nodoSecretariaDeRecursosHumanos = new NodoMaior("Secretaria de Recursos Humanos");

		adicionaTipoDeDocumentoANodo(nodoSecretariaDeRecursosHumanos);
		
		//Documentos da CCJF
		NodoMaior nodoCcjf = new NodoMaior("CCJF");
		
		adicionaTipoDeDocumentoANodo(nodoCcjf);

		//Documentos da SPO
		NodoMaior nodoSpo = new NodoMaior("SPO");
		
		adicionaTipoDeDocumentoANodo(nodoSpo);
		
		//Documentos Outros
		NodoMaior nodoOutros = new NodoMaior("OUTROS");
		
		adicionaTipoDeDocumentoANodo(nodoOutros);
		
		nodosPrincipais.add(nodoPresidencia);
		nodosPrincipais.add(nodoVicePresidencia);
		nodosPrincipais.add(nodoEmarf);
		nodosPrincipais.add(nodoSecretariaGeral);
		nodosPrincipais.add(nodoSecretariaDeRecursosHumanos);
		nodosPrincipais.add(nodoCcjf);
		nodosPrincipais.add(nodoSpo);
		nodosPrincipais.add(nodoOutros);

		//TRF-PRES Resolução da Presidência
		ordemPorTipo.put(84L, 0);

		//Resolução
		ordemPorTipo.put(92L, 0);
		
		//TRF-PRES Ato da Presidência
		ordemPorTipo.put(86L, 1);
		
		//TRF-PRES Portaria da Presidência
		ordemPorTipo.put(85L, 2);
		
		//Portarias da EMARF
		ordemPorTipo.put(88L, 2);
		
		//Portaria (SG)
		ordemPorTipo.put(46L,2);
		
		//Portaria (SRH)
		ordemPorTipo.put(41L, 2);
		
		//Portaria
		ordemPorTipo.put(6L, 2);
		
		//Intrução Normativa não encontrado
		
		
		//TRF-PRES Ordem de Serviço da Presidência
		ordemPorTipo.put(83L, 4);
		
		//Ordem de Serviço
		ordemPorTipo.put(7L, 4);
		
		//TRF-PRES Edital da Presidência
		ordemPorTipo.put(82L, 5);
		
		//Despacho
		ordemPorTipo.put(8L, 6);
		
		//TRF - CJEFs Decisão
		ordemPorTipo.put(99L, 6);
		
		//Circulares não encontada
		
		//Ofício Circular
		ordemPorTipo.put(42L, 8);
		
		//Concessão de Diárias - Ainda não desenvolvido
		
		//Escala de Férias - Ainda não desenvolvido
		
		//Pagamento de Diárias  - Ainda não desenvolvido
		
		//Concessão de Suprimento de Fundos
	}
	
	private void adicionaTipoDeDocumentoANodo(NodoMaior nodo){
		
		nodo.getNodos().add(	new NodoMenor("Resoluções", new DataComparator()));
		
		nodo.getNodos().add(	new NodoMenor("Atos", new DataComparator()));

		nodo.getNodos().add(	new NodoMenor("Portarias", new DataComparator()));

		nodo.getNodos().add(	new NodoMenor("Instruções Normativas", new DataComparator()));
		
		nodo.getNodos().add(	new NodoMenor("Ordens de Serviço", new DataComparator()));
		
		nodo.getNodos().add(	new NodoMenor("Editais", new DataComparator()));

		nodo.getNodos().add(	new NodoMenor("Despachos/Decisões", new DataComparator()));

		nodo.getNodos().add(	new NodoMenor("Circulares", new DataComparator()));

		nodo.getNodos().add(	new NodoMenor("Ofícios Circulares", new DataComparator()));
		
		nodo.getNodos().add(	new NodoMenor("Concessão de Diárias", new DataComparator()));//Modelo Ainda não foi desenvolvido

		nodo.getNodos().add(	new NodoMenor("Escala de Férias", new DataComparator()));//Modelo Ainda não foi desenvolvido

		nodo.getNodos().add(	new NodoMenor("Pagamentos de Diárias", new DataComparator()));//Modelo Ainda não foi desenvolvido

		nodo.getNodos().add(	new NodoMenor("Concessão de Suprimentos e Fundos", new DataComparator()));//Modelo Ainda não foi desenvolvido
	}

	private void alocaDocumento(ExDocumento doc) {
		
		if(doc.getLotaSubscritor().getSigla().equals("PRES")) {
			
			nodosPrincipais.get(0).getNodos().get(
					ordemPorTipo.get(doc.getExModelo().getExFormaDocumento().getId()))
					.getExDocumentoSet().add(doc);
			
		} else if(doc.getLotaSubscritor().getSigla().equals("VPC")) {
			
			nodosPrincipais.get(1).getNodos().get(
					ordemPorTipo.get(doc.getExModelo().getExFormaDocumento().getId()))
					.getExDocumentoSet().add(doc);
			
		} else if(doc.getLotaSubscritor().getSigla().equals("EMARF")) {
			
			nodosPrincipais.get(2).getNodos().get(
					ordemPorTipo.get(doc.getExModelo().getExFormaDocumento().getId()))
					.getExDocumentoSet().add(doc);
			
		} else if(doc.getLotaSubscritor().getSigla().equals("SG")) {
			
			nodosPrincipais.get(3).getNodos().get(
					ordemPorTipo.get(doc.getExModelo().getExFormaDocumento().getId()))
					.getExDocumentoSet().add(doc);
			
		} else if(doc.getLotaSubscritor().getSigla().equals("SRH")) {
			
			nodosPrincipais.get(4).getNodos().get(
					ordemPorTipo.get(doc.getExModelo().getExFormaDocumento().getId()))
					.getExDocumentoSet().add(doc);
			
		} else if(doc.getLotaSubscritor().getSigla().equals("CCJF")) {
			
			nodosPrincipais.get(5).getNodos().get(
					ordemPorTipo.get(doc.getExModelo().getExFormaDocumento().getId()))
					.getExDocumentoSet().add(doc);
			
		} else if(doc.getLotaSubscritor().getSigla().equals("SPO")) {
			
			nodosPrincipais.get(6).getNodos().get(
					ordemPorTipo.get(doc.getExModelo().getExFormaDocumento().getId()))
					.getExDocumentoSet().add(doc);
			
		} else {
			nodosPrincipais.get(7).getNodos().get(
					ordemPorTipo.get(doc.getExModelo().getExFormaDocumento().getId()))
					.getExDocumentoSet().add(doc);
		}
		
	}

	public static int obterIndiceMateriaLivreDoc(NodoMenor nodoMenor, ExDocumento doc,
			Integer i) {
		String tipoMateria = "";
		int contador = 1;
		if (nodoMenor.getDescr().equals("ANEXOS"))
			tipoMateria = "Anexo";
		else if (nodoMenor.getDescr().equals("PORTARIAS"))
			tipoMateria = "Portaria";
		else if (nodoMenor.getDescr().equals("PORTARIAS DA SGP"))
			tipoMateria = "Portaria SGP";
		else if (nodoMenor.getDescr().equals("ERRATAS"))
			tipoMateria = "Errata";
		else if (nodoMenor.getDescr().equals("OUTROS"))
			tipoMateria = "Campo Livre";

		for (String chave : doc.getForm().keySet()) {
			String chave2[] = chave.split("tipoMateria");
			if (chave2.length > 1)
				if (doc.getForm().get(chave).equals(tipoMateria))
					if (contador == i)
						return Integer.parseInt(chave2[1]);
					else
						contador++;
		}
		return 0;
	}

	public static String obterTituloMateriaLivre(NodoMenor nodoMenor,
			ExDocumento doc, Integer i) {
		return doc.getForm().get(
				"tituloMateria" + obterIndiceMateriaLivreDoc(nodoMenor, doc, i));
	}

	public static String obterBlobMateriaLivre(NodoMenor nodoMenor,
			ExDocumento doc, Integer i) {
		return doc.getForm().get(
				"texto_publicacao" + obterIndiceMateriaLivreDoc(nodoMenor, doc, i));
	}

	public List<ExDocumento> getDocsAPublicar() {
		return docsAPublicar;
	}

	public void setDocsAPublicar(List<ExDocumento> docsAPublicar) {
		this.docsAPublicar = docsAPublicar;
	}

	public List<NodoMaior> getNodosPrincipais() {
		return nodosPrincipais;
	}

	public void setNodosPrincipais(List<NodoMaior> nodosPrincipais) {
		this.nodosPrincipais = nodosPrincipais;
	}
}