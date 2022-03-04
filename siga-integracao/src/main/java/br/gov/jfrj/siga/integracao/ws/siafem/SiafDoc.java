package br.gov.jfrj.siga.integracao.ws.siafem;

import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import br.gov.jfrj.siga.base.AplicacaoException;

public class SiafDoc {
	private String unidadeGestora;
	private String gestao;
	private String objetoProcesso;
	private String tipoLicitacao;
	private String id;
	private String ata;
	private String convenio;
	private String flagPresencial;
	private String flagEletronico;
	private String finalidade;
	private String processo;
	private String codUnico;
	private String codSemPapel;
	private String desdobramento;	
	private String cnpj;
	private String codMunicipio;
	private String signatarioCedente;
	private String signatarioConvenente;
	private String naturezaDespesa;
	private String dataVigenciaInicial;
	private String dataVigenciaFinal;
	private String dataCelebracao;
	private String dataPublicacao;
	private String valorContrapartida;
	private String valorTotal;
	private String situacao;
	private String objetoResumido1;
	private String objetoResumido2;
	private String objetoResumido3;
	
	SimpleDateFormat formatoSiafem = new SimpleDateFormat("ddMMMyyyy", new Locale("pt", "BR"));
	SimpleDateFormat formatoSiga = new SimpleDateFormat("ddMMyyyy", new Locale("pt", "BR"));
	
	private Map<String, String> formulario;
	
	public SiafDoc(Map<String, String> formulario) {
		this.formulario = formulario;
		try {					
			this.codUnico = get("codigoUnico") + get("digitoVerificadorCodigoUnico") ;
			this.unidadeGestora = "00000" + get("unidadeGestora").split(" ")[0];
			this.unidadeGestora = this.unidadeGestora.substring(this.unidadeGestora.length() - 6, this.unidadeGestora.length());
			this.gestao = get("compraGestao");
			this.objetoProcesso = URLDecoder.decode(get("objetoProcesso"), "UTF-8");
			this.tipoLicitacao = get("selecioneLicitacao").split("-")[0].trim();
			this.tipoLicitacao = this.tipoLicitacao.matches("\\d+") ? this.tipoLicitacao : "";
			this.id = get("idProcesso");
			this.ata = get("ataTeste").matches("[1Ss]") ? "S" : "N";
			this.convenio = get("especie").equals("Convênio Sim") ? "S" : "N";
			this.finalidade = URLDecoder.decode(get("finalidadeProcesso"), "UTF-8");
			this.processo = get("processoLegado");
			this.desdobramento = get("desdobramento");
			this.flagPresencial = (get("presencialConvite") + get("presencialLicitacao") + get("presencialPregao")).contains("Sim") ? "X" : "";
			this.flagEletronico = (get("eletronicoConvite") + get("eletronicoLicitacao") + get("eletronicoPregao")).contains("Sim") ? "X" : "";
			this.cnpj = get("interessado_cnpj").replaceAll("[^\\d]", "");
			this.naturezaDespesa = get("naturezaDespesa").split(" ")[0];
			this.codMunicipio = get("municipioSao").split(" ")[0];			
			this.signatarioCedente = get("signatarioCedente");
			this.signatarioConvenente = get("signatarioConvenente");
			this.dataCelebracao = formatarData(get("data_da_celebracao")); 
			this.dataPublicacao = formatarData(get("data_da_publicacao")); 
			this.dataVigenciaInicial = formatarData(get("data_da_inicio")); 
			this.dataVigenciaFinal = formatarData(get("data_da_fim")); 
			this.valorTotal = get("valorTotal").replaceAll("[^\\d]", "");
			this.valorContrapartida = get("valorContrapartida").replaceAll("[^\\d]", "");
			this.situacao = get("situacaoConvenio").split(" ")[0];
			this.objetoResumido1 = get("descricaoResumida");
			this.objetoResumido2 = substring(this.objetoResumido1, 77, 154);
			this.objetoResumido3 = substring(this.objetoResumido1, 154, 232);
			this.objetoResumido1 = substring(this.objetoResumido1, 0, 77);
			
//			Quando for Legado o usuário deve preencher SOMENTE os campos
//			- UG/Gestão
//			- número do processo legado (10 posições alfanumérico)
//			- desdobramento (1 posição)
			if (this.processo != null && !this.processo.isEmpty()) {
				this.objetoProcesso = this.tipoLicitacao = this.id = this.ata = this.convenio = this.finalidade = 
				this.flagPresencial = this.flagEletronico = this.cnpj = this.naturezaDespesa = this.codMunicipio = 
				this.signatarioCedente = this.signatarioConvenente = this.dataCelebracao = this.dataPublicacao = 
				this.dataVigenciaInicial = this.dataVigenciaFinal = this.valorTotal = this.valorContrapartida = 
				this.situacao = this.objetoResumido1 = this.objetoResumido2 = this.objetoResumido3 = "";
			}

		} catch (Exception e) {
			throw new AplicacaoException("Falha ao realizar a leitura da entrevista: " + e.getMessage());
		}
	}
	
	private String get(String key) {
		return this.formulario.get(key) == null ? "" : this.formulario.get(key).trim();
	}

	private String formatarData(String data) throws ParseException {
		String dataStr = data.replaceAll("[^\\d]", "");
		
		if(dataStr.isEmpty())
			return "";
				
		return formatoSiafem.format(formatoSiga.parse(dataStr));
	}

	private String substring(String str, int i, int j) {
		if(i >= str.length())
			return "";
				
		j = j >= str.length() ? str.length() : j;
		
		return str.substring(i, j);
	}

	public void setCodUnico(String codUnico) {
		this.codUnico = codUnico;
	}

	public void setCodSemPapel(String codSemPapel) {
		this.codSemPapel = codSemPapel;
	}

	public String getSiafDoc() {
		String[] parametros = new String[] { unidadeGestora, gestao, processo, desdobramento, codUnico, codSemPapel, objetoProcesso, 
				tipoLicitacao, id, ata, convenio, flagPresencial, flagEletronico, finalidade, cnpj, codMunicipio, signatarioCedente, 
				signatarioConvenente, naturezaDespesa, dataVigenciaInicial, dataVigenciaFinal, dataCelebracao, dataPublicacao, 
				valorContrapartida, valorTotal, situacao, objetoResumido1, objetoResumido2, objetoResumido3 };

		String xml = SIAFDOC;

		for (int i = 0; i < parametros.length; i++)
			xml = inserirValor(xml, i, parametros[i]);

		return xml.toString();
	}

	private String inserirValor(String xml, int i, String valor) {
		String tagVazia = "<" + TAGS[i] + "/>";
		String tagValor = "<" + TAGS[i] + ">" + valor + "</" + TAGS[i] + ">";

		if (valor != null && !valor.isEmpty())
			xml = xml.replace(tagVazia, tagValor);

		return xml;
	}

	private String[] TAGS = { "UG", "Gestao", "Processo", "Desdobramento", "CodUnico", "CodSemPapel", "Objeto", 
			"TipoLicitacao", "ID", "ATA", "Convenio", "FlagPresencial", "FlagEletronico", "Finalidade", "CNPJ",
			"CodMunicipio", "SignatarioCedente", "SignatarioConvenente", "NaturezaDespesa", "DataVigenciaInicial",
			"DataVigenciaFinal", "DataCelebracao", "DataPublicacao", "ValorContrapartida", "ValorTotal", "Situacao", 
			"ObjetoResumido1", "ObjetoResumido2", "ObjetoResumido3" };

	private String SIAFDOC = "<SIAFDOC>\n" +
			"   <cdMsg>SIAFPROCESSO001</cdMsg>\n" +
			"   <SiafemDocProcesso>\n" +
			"      <documento>\n" +
			"         <UG/>\n" +
			"         <Gestao/>\n" +
			"         <Processo/>\n" +
			"         <Desdobramento/>\n" +
			"         <CodUnico/>\n" +
			"         <CodSemPapel/>\n" +
			"         <Objeto/>\n" +
			"         <TipoLicitacao/>\n" +
			"         <ID/>\n" +
			"         <DigitoID/>\n" +
			"         <ATA/>\n" +
			"         <Convenio/>\n" +
			"         <FlagPresencial/>\n" +
			"         <FlagEletronico/>\n" +
			"         <ObjetoConvenio/>\n" +
			"         <CNPJ/>\n" +
			"         <CodMunicipio/>\n" +
			"         <SignatarioCedente/>\n" +
			"         <SignatarioConvenente/>\n" +
			"         <NaturezaDespesa/>\n" +
			"         <DataVigenciaInicial/>\n" +
			"         <DataVigenciaFinal/>\n" +
			"         <DataCelebracao/>\n" +
			"         <DataPublicacao/>\n" +
			"         <ValorContrapartida/>\n" +
			"         <ValorTotal/>\n" +
			"         <Situacao/>\n" +
			"         <ObjetoResumido1/>\n" +
			"         <ObjetoResumido2/>\n" +
			"         <ObjetoResumido3/>\n" +
			"      </documento>\n" +
			"      <finalidade>\n" +
			"         <Repeticao>\n" +
			"            <desc ID='1'>\n" +
			"               <Finalidade/>\n" +
			"            </desc>\n" +
			"         </Repeticao>\n" +
			"      </finalidade>\n" +
			"   </SiafemDocProcesso>\n" +
			"</SIAFDOC>";
}
