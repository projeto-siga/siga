package br.gov.jfrj.siga.ex.util.BIE;

import java.util.Arrays;
import java.util.List;



public enum TipoMateria {
	
	RESOLUCAO_PRESIDENCIA("Resolução da Presidência", "Resoluções da Presidência", new ConversorSimples("RSP")),
	ATO_PRESIDENCIA("Ato da Presidência", "Atos da Presidência", new ConversorSimples("ATP")),
	PORTARIA_PRESIDENCIA("Portaria da Presidência", "Portarias da Presidência", new ConversorSimples("PTP")),
	PORTARIA_PGD("Portaria PGD", "Portarias PGD", new ConversorSimples("PGD")), 
	PORTARIA_EMARF("Portaria da EMARF", "Portarias da EMARF", new ConversorSimples("PTE")),
	PORTARIA_SG("Portaria da SG", "Portarias da SG", new ConversorSimples("PSG")),
	PORTARIA_SGP("Portaria da SGP", "Portarias da SGP", new ConversorSimples("PRH"), new ConversorMateriaLivre()),
	PORTARIA("Portaria", "Portarias", new ConversorSimples("POR"), new ConversorMateriaLivre()),
	ORDEM_DE_SERVICO_PRESIDENCIA("Ordem de Serviço da Presidência", "Ordens de Serviço da Presidência", new ConversorSimples("OSP")),
	ORDEM_DE_SERVICO_DIRFO("Ordem de Serviço da DIRFO", "Ordens de Serviço da DIRFO", new ConversorSimples("ODF")),
	ORDEM_DE_SERVICO("Ordem de Serviço", "Ordens de Serviço", new ConversorSimples("ODS")),
	EDITAL_PRESIDENCIA("Edital da Presidência", "Editais da Presidência", new ConversorSimples("EDP")),
	DECISAO("Decisão CJEFs", "Decisões CJEFs", new ConversorSimples("DCS")),
	OFICIO_CIRCULAR_DIRFO ("Ofício Circular da DIRFO", "Ofícios Circulares da DIRFO", new ConversorSimples("OCD")), 
	OFICIO_CIRCULAR ("Ofício Circular", "Ofícios Circulares", new ConversorSimples("OCI")), 
	PROCESSO("Processo", "Processos", new ConversorMultiplo()), 
	CONCESSAO_DE_DIARIAS ("Concessão de Diárias", new ConversorMultiplo()), 
	TABELA_DE_CONCESSAO_DE_SUPRIMENTO_DE_FUNDOS ("Tabela de Concessão de Suprimento de Fundos", new ConversorMultiplo()), 
	ERRATA("Errata", "Erratas", new ConversorMateriaLivre()), 
	ANEXO("Anexo", "Anexos", new ConversorMateriaLivre()), 
	OUTROS("Outros", new ConversorMateriaLivre());
	
	private String descricao;
	private String descricaoPlural;
	private List<? extends ConversorDeExDocParaMateria> conversores;
	
	TipoMateria(String descricao, ConversorDeExDocParaMateria... conversores){
		this(descricao, null, conversores);
	}
	
	TipoMateria(String descricao, String plural, ConversorDeExDocParaMateria... conversores){
		this.descricao = descricao;
		this.descricaoPlural = plural;
		this.conversores = Arrays.asList(conversores);
		for (ConversorDeExDocParaMateria conversor : conversores)
			conversor.setTipoMateria(this);
	}
	
	public String getDescricao() {
		return descricao;
	}

	public String getDescricaoPlural() {
		if (descricaoPlural != null)
			return descricaoPlural;
		return descricao;
	}
	
	public String getDescricaoPluralMaiusculas(){
		return getDescricaoPlural().toUpperCase();
	}
	
	public List<? extends ConversorDeExDocParaMateria> getConversores(){
		return conversores;
	}
			
}
