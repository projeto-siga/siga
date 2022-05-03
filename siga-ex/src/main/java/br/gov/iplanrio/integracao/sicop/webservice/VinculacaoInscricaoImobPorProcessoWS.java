
package br.gov.iplanrio.integracao.sicop.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de VinculacaoInscricaoImobPorProcesso_WS complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="VinculacaoInscricaoImobPorProcesso_WS">
 *   &lt;complexContent>
 *     &lt;extension base="{http://pcrj/Sma/Sicop/Grava}Message">
 *       &lt;sequence>
 *         &lt;element name="ROTINA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OPCAO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROPROCESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI27" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM27" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ27" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI15" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM15" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ15" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI28" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM28" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ28" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI16" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM16" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ16" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI29" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM29" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ29" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI17" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM17" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ17" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI30" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM30" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ30" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI18" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM18" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ18" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI31" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM31" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ31" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI19" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM19" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ19" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI32" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM32" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ32" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI20" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM20" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ20" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI33" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM33" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ33" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI21" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM21" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ21" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI34" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM34" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ34" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI22" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM22" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ22" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI35" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM35" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ35" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI23" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM23" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ23" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI36" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM36" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ36" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI24" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM24" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ24" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI37" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM37" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ37" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI25" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM25" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ25" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI38" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM38" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ38" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI26" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM26" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ26" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRINI39" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INSCRFIM39" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ39" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LBLCONFIRMA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INDCONFIRMA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StatusLine" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VinculacaoInscricaoImobPorProcesso_WS", propOrder = {
    "rotina",
    "opcao",
    "nroprocesso",
    "inscrini1",
    "inscrfim1",
    "seq1",
    "inscrini14",
    "inscrfim14",
    "seq14",
    "inscrini27",
    "inscrfim27",
    "seq27",
    "inscrini2",
    "inscrfim2",
    "seq2",
    "inscrini15",
    "inscrfim15",
    "seq15",
    "inscrini28",
    "inscrfim28",
    "seq28",
    "inscrini3",
    "inscrfim3",
    "seq3",
    "inscrini16",
    "inscrfim16",
    "seq16",
    "inscrini29",
    "inscrfim29",
    "seq29",
    "inscrini4",
    "inscrfim4",
    "seq4",
    "inscrini17",
    "inscrfim17",
    "seq17",
    "inscrini30",
    "inscrfim30",
    "seq30",
    "inscrini5",
    "inscrfim5",
    "seq5",
    "inscrini18",
    "inscrfim18",
    "seq18",
    "inscrini31",
    "inscrfim31",
    "seq31",
    "inscrini6",
    "inscrfim6",
    "seq6",
    "inscrini19",
    "inscrfim19",
    "seq19",
    "inscrini32",
    "inscrfim32",
    "seq32",
    "inscrini7",
    "inscrfim7",
    "seq7",
    "inscrini20",
    "inscrfim20",
    "seq20",
    "inscrini33",
    "inscrfim33",
    "seq33",
    "inscrini8",
    "inscrfim8",
    "seq8",
    "inscrini21",
    "inscrfim21",
    "seq21",
    "inscrini34",
    "inscrfim34",
    "seq34",
    "inscrini9",
    "inscrfim9",
    "seq9",
    "inscrini22",
    "inscrfim22",
    "seq22",
    "inscrini35",
    "inscrfim35",
    "seq35",
    "inscrini10",
    "inscrfim10",
    "seq10",
    "inscrini23",
    "inscrfim23",
    "seq23",
    "inscrini36",
    "inscrfim36",
    "seq36",
    "inscrini11",
    "inscrfim11",
    "seq11",
    "inscrini24",
    "inscrfim24",
    "seq24",
    "inscrini37",
    "inscrfim37",
    "seq37",
    "inscrini12",
    "inscrfim12",
    "seq12",
    "inscrini25",
    "inscrfim25",
    "seq25",
    "inscrini38",
    "inscrfim38",
    "seq38",
    "inscrini13",
    "inscrfim13",
    "seq13",
    "inscrini26",
    "inscrfim26",
    "seq26",
    "inscrini39",
    "inscrfim39",
    "seq39",
    "lblconfirma",
    "indconfirma",
    "statusLine"
})
public class VinculacaoInscricaoImobPorProcessoWS
    extends Message
{

    @XmlElement(name = "ROTINA")
    protected String rotina;
    @XmlElement(name = "OPCAO")
    protected String opcao;
    @XmlElement(name = "NROPROCESSO")
    protected String nroprocesso;
    @XmlElement(name = "INSCRINI1")
    protected String inscrini1;
    @XmlElement(name = "INSCRFIM1")
    protected String inscrfim1;
    @XmlElement(name = "SEQ1")
    protected String seq1;
    @XmlElement(name = "INSCRINI14")
    protected String inscrini14;
    @XmlElement(name = "INSCRFIM14")
    protected String inscrfim14;
    @XmlElement(name = "SEQ14")
    protected String seq14;
    @XmlElement(name = "INSCRINI27")
    protected String inscrini27;
    @XmlElement(name = "INSCRFIM27")
    protected String inscrfim27;
    @XmlElement(name = "SEQ27")
    protected String seq27;
    @XmlElement(name = "INSCRINI2")
    protected String inscrini2;
    @XmlElement(name = "INSCRFIM2")
    protected String inscrfim2;
    @XmlElement(name = "SEQ2")
    protected String seq2;
    @XmlElement(name = "INSCRINI15")
    protected String inscrini15;
    @XmlElement(name = "INSCRFIM15")
    protected String inscrfim15;
    @XmlElement(name = "SEQ15")
    protected String seq15;
    @XmlElement(name = "INSCRINI28")
    protected String inscrini28;
    @XmlElement(name = "INSCRFIM28")
    protected String inscrfim28;
    @XmlElement(name = "SEQ28")
    protected String seq28;
    @XmlElement(name = "INSCRINI3")
    protected String inscrini3;
    @XmlElement(name = "INSCRFIM3")
    protected String inscrfim3;
    @XmlElement(name = "SEQ3")
    protected String seq3;
    @XmlElement(name = "INSCRINI16")
    protected String inscrini16;
    @XmlElement(name = "INSCRFIM16")
    protected String inscrfim16;
    @XmlElement(name = "SEQ16")
    protected String seq16;
    @XmlElement(name = "INSCRINI29")
    protected String inscrini29;
    @XmlElement(name = "INSCRFIM29")
    protected String inscrfim29;
    @XmlElement(name = "SEQ29")
    protected String seq29;
    @XmlElement(name = "INSCRINI4")
    protected String inscrini4;
    @XmlElement(name = "INSCRFIM4")
    protected String inscrfim4;
    @XmlElement(name = "SEQ4")
    protected String seq4;
    @XmlElement(name = "INSCRINI17")
    protected String inscrini17;
    @XmlElement(name = "INSCRFIM17")
    protected String inscrfim17;
    @XmlElement(name = "SEQ17")
    protected String seq17;
    @XmlElement(name = "INSCRINI30")
    protected String inscrini30;
    @XmlElement(name = "INSCRFIM30")
    protected String inscrfim30;
    @XmlElement(name = "SEQ30")
    protected String seq30;
    @XmlElement(name = "INSCRINI5")
    protected String inscrini5;
    @XmlElement(name = "INSCRFIM5")
    protected String inscrfim5;
    @XmlElement(name = "SEQ5")
    protected String seq5;
    @XmlElement(name = "INSCRINI18")
    protected String inscrini18;
    @XmlElement(name = "INSCRFIM18")
    protected String inscrfim18;
    @XmlElement(name = "SEQ18")
    protected String seq18;
    @XmlElement(name = "INSCRINI31")
    protected String inscrini31;
    @XmlElement(name = "INSCRFIM31")
    protected String inscrfim31;
    @XmlElement(name = "SEQ31")
    protected String seq31;
    @XmlElement(name = "INSCRINI6")
    protected String inscrini6;
    @XmlElement(name = "INSCRFIM6")
    protected String inscrfim6;
    @XmlElement(name = "SEQ6")
    protected String seq6;
    @XmlElement(name = "INSCRINI19")
    protected String inscrini19;
    @XmlElement(name = "INSCRFIM19")
    protected String inscrfim19;
    @XmlElement(name = "SEQ19")
    protected String seq19;
    @XmlElement(name = "INSCRINI32")
    protected String inscrini32;
    @XmlElement(name = "INSCRFIM32")
    protected String inscrfim32;
    @XmlElement(name = "SEQ32")
    protected String seq32;
    @XmlElement(name = "INSCRINI7")
    protected String inscrini7;
    @XmlElement(name = "INSCRFIM7")
    protected String inscrfim7;
    @XmlElement(name = "SEQ7")
    protected String seq7;
    @XmlElement(name = "INSCRINI20")
    protected String inscrini20;
    @XmlElement(name = "INSCRFIM20")
    protected String inscrfim20;
    @XmlElement(name = "SEQ20")
    protected String seq20;
    @XmlElement(name = "INSCRINI33")
    protected String inscrini33;
    @XmlElement(name = "INSCRFIM33")
    protected String inscrfim33;
    @XmlElement(name = "SEQ33")
    protected String seq33;
    @XmlElement(name = "INSCRINI8")
    protected String inscrini8;
    @XmlElement(name = "INSCRFIM8")
    protected String inscrfim8;
    @XmlElement(name = "SEQ8")
    protected String seq8;
    @XmlElement(name = "INSCRINI21")
    protected String inscrini21;
    @XmlElement(name = "INSCRFIM21")
    protected String inscrfim21;
    @XmlElement(name = "SEQ21")
    protected String seq21;
    @XmlElement(name = "INSCRINI34")
    protected String inscrini34;
    @XmlElement(name = "INSCRFIM34")
    protected String inscrfim34;
    @XmlElement(name = "SEQ34")
    protected String seq34;
    @XmlElement(name = "INSCRINI9")
    protected String inscrini9;
    @XmlElement(name = "INSCRFIM9")
    protected String inscrfim9;
    @XmlElement(name = "SEQ9")
    protected String seq9;
    @XmlElement(name = "INSCRINI22")
    protected String inscrini22;
    @XmlElement(name = "INSCRFIM22")
    protected String inscrfim22;
    @XmlElement(name = "SEQ22")
    protected String seq22;
    @XmlElement(name = "INSCRINI35")
    protected String inscrini35;
    @XmlElement(name = "INSCRFIM35")
    protected String inscrfim35;
    @XmlElement(name = "SEQ35")
    protected String seq35;
    @XmlElement(name = "INSCRINI10")
    protected String inscrini10;
    @XmlElement(name = "INSCRFIM10")
    protected String inscrfim10;
    @XmlElement(name = "SEQ10")
    protected String seq10;
    @XmlElement(name = "INSCRINI23")
    protected String inscrini23;
    @XmlElement(name = "INSCRFIM23")
    protected String inscrfim23;
    @XmlElement(name = "SEQ23")
    protected String seq23;
    @XmlElement(name = "INSCRINI36")
    protected String inscrini36;
    @XmlElement(name = "INSCRFIM36")
    protected String inscrfim36;
    @XmlElement(name = "SEQ36")
    protected String seq36;
    @XmlElement(name = "INSCRINI11")
    protected String inscrini11;
    @XmlElement(name = "INSCRFIM11")
    protected String inscrfim11;
    @XmlElement(name = "SEQ11")
    protected String seq11;
    @XmlElement(name = "INSCRINI24")
    protected String inscrini24;
    @XmlElement(name = "INSCRFIM24")
    protected String inscrfim24;
    @XmlElement(name = "SEQ24")
    protected String seq24;
    @XmlElement(name = "INSCRINI37")
    protected String inscrini37;
    @XmlElement(name = "INSCRFIM37")
    protected String inscrfim37;
    @XmlElement(name = "SEQ37")
    protected String seq37;
    @XmlElement(name = "INSCRINI12")
    protected String inscrini12;
    @XmlElement(name = "INSCRFIM12")
    protected String inscrfim12;
    @XmlElement(name = "SEQ12")
    protected String seq12;
    @XmlElement(name = "INSCRINI25")
    protected String inscrini25;
    @XmlElement(name = "INSCRFIM25")
    protected String inscrfim25;
    @XmlElement(name = "SEQ25")
    protected String seq25;
    @XmlElement(name = "INSCRINI38")
    protected String inscrini38;
    @XmlElement(name = "INSCRFIM38")
    protected String inscrfim38;
    @XmlElement(name = "SEQ38")
    protected String seq38;
    @XmlElement(name = "INSCRINI13")
    protected String inscrini13;
    @XmlElement(name = "INSCRFIM13")
    protected String inscrfim13;
    @XmlElement(name = "SEQ13")
    protected String seq13;
    @XmlElement(name = "INSCRINI26")
    protected String inscrini26;
    @XmlElement(name = "INSCRFIM26")
    protected String inscrfim26;
    @XmlElement(name = "SEQ26")
    protected String seq26;
    @XmlElement(name = "INSCRINI39")
    protected String inscrini39;
    @XmlElement(name = "INSCRFIM39")
    protected String inscrfim39;
    @XmlElement(name = "SEQ39")
    protected String seq39;
    @XmlElement(name = "LBLCONFIRMA")
    protected String lblconfirma;
    @XmlElement(name = "INDCONFIRMA")
    protected String indconfirma;
    @XmlElement(name = "StatusLine")
    protected String statusLine;

    /**
     * Obt�m o valor da propriedade rotina.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getROTINA() {
        return rotina;
    }

    /**
     * Define o valor da propriedade rotina.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setROTINA(String value) {
        this.rotina = value;
    }

    /**
     * Obt�m o valor da propriedade opcao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOPCAO() {
        return opcao;
    }

    /**
     * Define o valor da propriedade opcao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOPCAO(String value) {
        this.opcao = value;
    }

    /**
     * Obt�m o valor da propriedade nroprocesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROPROCESSO() {
        return nroprocesso;
    }

    /**
     * Define o valor da propriedade nroprocesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROPROCESSO(String value) {
        this.nroprocesso = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI1() {
        return inscrini1;
    }

    /**
     * Define o valor da propriedade inscrini1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI1(String value) {
        this.inscrini1 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM1() {
        return inscrfim1;
    }

    /**
     * Define o valor da propriedade inscrfim1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM1(String value) {
        this.inscrfim1 = value;
    }

    /**
     * Obt�m o valor da propriedade seq1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ1() {
        return seq1;
    }

    /**
     * Define o valor da propriedade seq1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ1(String value) {
        this.seq1 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI14() {
        return inscrini14;
    }

    /**
     * Define o valor da propriedade inscrini14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI14(String value) {
        this.inscrini14 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM14() {
        return inscrfim14;
    }

    /**
     * Define o valor da propriedade inscrfim14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM14(String value) {
        this.inscrfim14 = value;
    }

    /**
     * Obt�m o valor da propriedade seq14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ14() {
        return seq14;
    }

    /**
     * Define o valor da propriedade seq14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ14(String value) {
        this.seq14 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini27.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI27() {
        return inscrini27;
    }

    /**
     * Define o valor da propriedade inscrini27.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI27(String value) {
        this.inscrini27 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim27.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM27() {
        return inscrfim27;
    }

    /**
     * Define o valor da propriedade inscrfim27.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM27(String value) {
        this.inscrfim27 = value;
    }

    /**
     * Obt�m o valor da propriedade seq27.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ27() {
        return seq27;
    }

    /**
     * Define o valor da propriedade seq27.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ27(String value) {
        this.seq27 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI2() {
        return inscrini2;
    }

    /**
     * Define o valor da propriedade inscrini2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI2(String value) {
        this.inscrini2 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM2() {
        return inscrfim2;
    }

    /**
     * Define o valor da propriedade inscrfim2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM2(String value) {
        this.inscrfim2 = value;
    }

    /**
     * Obt�m o valor da propriedade seq2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ2() {
        return seq2;
    }

    /**
     * Define o valor da propriedade seq2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ2(String value) {
        this.seq2 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini15.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI15() {
        return inscrini15;
    }

    /**
     * Define o valor da propriedade inscrini15.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI15(String value) {
        this.inscrini15 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim15.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM15() {
        return inscrfim15;
    }

    /**
     * Define o valor da propriedade inscrfim15.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM15(String value) {
        this.inscrfim15 = value;
    }

    /**
     * Obt�m o valor da propriedade seq15.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ15() {
        return seq15;
    }

    /**
     * Define o valor da propriedade seq15.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ15(String value) {
        this.seq15 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini28.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI28() {
        return inscrini28;
    }

    /**
     * Define o valor da propriedade inscrini28.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI28(String value) {
        this.inscrini28 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim28.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM28() {
        return inscrfim28;
    }

    /**
     * Define o valor da propriedade inscrfim28.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM28(String value) {
        this.inscrfim28 = value;
    }

    /**
     * Obt�m o valor da propriedade seq28.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ28() {
        return seq28;
    }

    /**
     * Define o valor da propriedade seq28.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ28(String value) {
        this.seq28 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI3() {
        return inscrini3;
    }

    /**
     * Define o valor da propriedade inscrini3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI3(String value) {
        this.inscrini3 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM3() {
        return inscrfim3;
    }

    /**
     * Define o valor da propriedade inscrfim3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM3(String value) {
        this.inscrfim3 = value;
    }

    /**
     * Obt�m o valor da propriedade seq3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ3() {
        return seq3;
    }

    /**
     * Define o valor da propriedade seq3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ3(String value) {
        this.seq3 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini16.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI16() {
        return inscrini16;
    }

    /**
     * Define o valor da propriedade inscrini16.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI16(String value) {
        this.inscrini16 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim16.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM16() {
        return inscrfim16;
    }

    /**
     * Define o valor da propriedade inscrfim16.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM16(String value) {
        this.inscrfim16 = value;
    }

    /**
     * Obt�m o valor da propriedade seq16.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ16() {
        return seq16;
    }

    /**
     * Define o valor da propriedade seq16.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ16(String value) {
        this.seq16 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini29.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI29() {
        return inscrini29;
    }

    /**
     * Define o valor da propriedade inscrini29.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI29(String value) {
        this.inscrini29 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim29.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM29() {
        return inscrfim29;
    }

    /**
     * Define o valor da propriedade inscrfim29.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM29(String value) {
        this.inscrfim29 = value;
    }

    /**
     * Obt�m o valor da propriedade seq29.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ29() {
        return seq29;
    }

    /**
     * Define o valor da propriedade seq29.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ29(String value) {
        this.seq29 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI4() {
        return inscrini4;
    }

    /**
     * Define o valor da propriedade inscrini4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI4(String value) {
        this.inscrini4 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM4() {
        return inscrfim4;
    }

    /**
     * Define o valor da propriedade inscrfim4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM4(String value) {
        this.inscrfim4 = value;
    }

    /**
     * Obt�m o valor da propriedade seq4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ4() {
        return seq4;
    }

    /**
     * Define o valor da propriedade seq4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ4(String value) {
        this.seq4 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini17.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI17() {
        return inscrini17;
    }

    /**
     * Define o valor da propriedade inscrini17.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI17(String value) {
        this.inscrini17 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim17.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM17() {
        return inscrfim17;
    }

    /**
     * Define o valor da propriedade inscrfim17.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM17(String value) {
        this.inscrfim17 = value;
    }

    /**
     * Obt�m o valor da propriedade seq17.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ17() {
        return seq17;
    }

    /**
     * Define o valor da propriedade seq17.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ17(String value) {
        this.seq17 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini30.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI30() {
        return inscrini30;
    }

    /**
     * Define o valor da propriedade inscrini30.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI30(String value) {
        this.inscrini30 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim30.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM30() {
        return inscrfim30;
    }

    /**
     * Define o valor da propriedade inscrfim30.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM30(String value) {
        this.inscrfim30 = value;
    }

    /**
     * Obt�m o valor da propriedade seq30.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ30() {
        return seq30;
    }

    /**
     * Define o valor da propriedade seq30.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ30(String value) {
        this.seq30 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI5() {
        return inscrini5;
    }

    /**
     * Define o valor da propriedade inscrini5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI5(String value) {
        this.inscrini5 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM5() {
        return inscrfim5;
    }

    /**
     * Define o valor da propriedade inscrfim5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM5(String value) {
        this.inscrfim5 = value;
    }

    /**
     * Obt�m o valor da propriedade seq5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ5() {
        return seq5;
    }

    /**
     * Define o valor da propriedade seq5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ5(String value) {
        this.seq5 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini18.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI18() {
        return inscrini18;
    }

    /**
     * Define o valor da propriedade inscrini18.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI18(String value) {
        this.inscrini18 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim18.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM18() {
        return inscrfim18;
    }

    /**
     * Define o valor da propriedade inscrfim18.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM18(String value) {
        this.inscrfim18 = value;
    }

    /**
     * Obt�m o valor da propriedade seq18.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ18() {
        return seq18;
    }

    /**
     * Define o valor da propriedade seq18.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ18(String value) {
        this.seq18 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini31.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI31() {
        return inscrini31;
    }

    /**
     * Define o valor da propriedade inscrini31.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI31(String value) {
        this.inscrini31 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim31.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM31() {
        return inscrfim31;
    }

    /**
     * Define o valor da propriedade inscrfim31.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM31(String value) {
        this.inscrfim31 = value;
    }

    /**
     * Obt�m o valor da propriedade seq31.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ31() {
        return seq31;
    }

    /**
     * Define o valor da propriedade seq31.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ31(String value) {
        this.seq31 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI6() {
        return inscrini6;
    }

    /**
     * Define o valor da propriedade inscrini6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI6(String value) {
        this.inscrini6 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM6() {
        return inscrfim6;
    }

    /**
     * Define o valor da propriedade inscrfim6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM6(String value) {
        this.inscrfim6 = value;
    }

    /**
     * Obt�m o valor da propriedade seq6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ6() {
        return seq6;
    }

    /**
     * Define o valor da propriedade seq6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ6(String value) {
        this.seq6 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini19.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI19() {
        return inscrini19;
    }

    /**
     * Define o valor da propriedade inscrini19.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI19(String value) {
        this.inscrini19 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim19.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM19() {
        return inscrfim19;
    }

    /**
     * Define o valor da propriedade inscrfim19.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM19(String value) {
        this.inscrfim19 = value;
    }

    /**
     * Obt�m o valor da propriedade seq19.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ19() {
        return seq19;
    }

    /**
     * Define o valor da propriedade seq19.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ19(String value) {
        this.seq19 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini32.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI32() {
        return inscrini32;
    }

    /**
     * Define o valor da propriedade inscrini32.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI32(String value) {
        this.inscrini32 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim32.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM32() {
        return inscrfim32;
    }

    /**
     * Define o valor da propriedade inscrfim32.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM32(String value) {
        this.inscrfim32 = value;
    }

    /**
     * Obt�m o valor da propriedade seq32.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ32() {
        return seq32;
    }

    /**
     * Define o valor da propriedade seq32.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ32(String value) {
        this.seq32 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI7() {
        return inscrini7;
    }

    /**
     * Define o valor da propriedade inscrini7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI7(String value) {
        this.inscrini7 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM7() {
        return inscrfim7;
    }

    /**
     * Define o valor da propriedade inscrfim7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM7(String value) {
        this.inscrfim7 = value;
    }

    /**
     * Obt�m o valor da propriedade seq7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ7() {
        return seq7;
    }

    /**
     * Define o valor da propriedade seq7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ7(String value) {
        this.seq7 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini20.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI20() {
        return inscrini20;
    }

    /**
     * Define o valor da propriedade inscrini20.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI20(String value) {
        this.inscrini20 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim20.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM20() {
        return inscrfim20;
    }

    /**
     * Define o valor da propriedade inscrfim20.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM20(String value) {
        this.inscrfim20 = value;
    }

    /**
     * Obt�m o valor da propriedade seq20.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ20() {
        return seq20;
    }

    /**
     * Define o valor da propriedade seq20.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ20(String value) {
        this.seq20 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini33.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI33() {
        return inscrini33;
    }

    /**
     * Define o valor da propriedade inscrini33.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI33(String value) {
        this.inscrini33 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim33.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM33() {
        return inscrfim33;
    }

    /**
     * Define o valor da propriedade inscrfim33.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM33(String value) {
        this.inscrfim33 = value;
    }

    /**
     * Obt�m o valor da propriedade seq33.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ33() {
        return seq33;
    }

    /**
     * Define o valor da propriedade seq33.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ33(String value) {
        this.seq33 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI8() {
        return inscrini8;
    }

    /**
     * Define o valor da propriedade inscrini8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI8(String value) {
        this.inscrini8 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM8() {
        return inscrfim8;
    }

    /**
     * Define o valor da propriedade inscrfim8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM8(String value) {
        this.inscrfim8 = value;
    }

    /**
     * Obt�m o valor da propriedade seq8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ8() {
        return seq8;
    }

    /**
     * Define o valor da propriedade seq8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ8(String value) {
        this.seq8 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini21.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI21() {
        return inscrini21;
    }

    /**
     * Define o valor da propriedade inscrini21.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI21(String value) {
        this.inscrini21 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim21.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM21() {
        return inscrfim21;
    }

    /**
     * Define o valor da propriedade inscrfim21.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM21(String value) {
        this.inscrfim21 = value;
    }

    /**
     * Obt�m o valor da propriedade seq21.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ21() {
        return seq21;
    }

    /**
     * Define o valor da propriedade seq21.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ21(String value) {
        this.seq21 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini34.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI34() {
        return inscrini34;
    }

    /**
     * Define o valor da propriedade inscrini34.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI34(String value) {
        this.inscrini34 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim34.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM34() {
        return inscrfim34;
    }

    /**
     * Define o valor da propriedade inscrfim34.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM34(String value) {
        this.inscrfim34 = value;
    }

    /**
     * Obt�m o valor da propriedade seq34.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ34() {
        return seq34;
    }

    /**
     * Define o valor da propriedade seq34.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ34(String value) {
        this.seq34 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI9() {
        return inscrini9;
    }

    /**
     * Define o valor da propriedade inscrini9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI9(String value) {
        this.inscrini9 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM9() {
        return inscrfim9;
    }

    /**
     * Define o valor da propriedade inscrfim9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM9(String value) {
        this.inscrfim9 = value;
    }

    /**
     * Obt�m o valor da propriedade seq9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ9() {
        return seq9;
    }

    /**
     * Define o valor da propriedade seq9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ9(String value) {
        this.seq9 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini22.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI22() {
        return inscrini22;
    }

    /**
     * Define o valor da propriedade inscrini22.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI22(String value) {
        this.inscrini22 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim22.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM22() {
        return inscrfim22;
    }

    /**
     * Define o valor da propriedade inscrfim22.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM22(String value) {
        this.inscrfim22 = value;
    }

    /**
     * Obt�m o valor da propriedade seq22.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ22() {
        return seq22;
    }

    /**
     * Define o valor da propriedade seq22.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ22(String value) {
        this.seq22 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini35.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI35() {
        return inscrini35;
    }

    /**
     * Define o valor da propriedade inscrini35.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI35(String value) {
        this.inscrini35 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim35.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM35() {
        return inscrfim35;
    }

    /**
     * Define o valor da propriedade inscrfim35.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM35(String value) {
        this.inscrfim35 = value;
    }

    /**
     * Obt�m o valor da propriedade seq35.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ35() {
        return seq35;
    }

    /**
     * Define o valor da propriedade seq35.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ35(String value) {
        this.seq35 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI10() {
        return inscrini10;
    }

    /**
     * Define o valor da propriedade inscrini10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI10(String value) {
        this.inscrini10 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM10() {
        return inscrfim10;
    }

    /**
     * Define o valor da propriedade inscrfim10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM10(String value) {
        this.inscrfim10 = value;
    }

    /**
     * Obt�m o valor da propriedade seq10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ10() {
        return seq10;
    }

    /**
     * Define o valor da propriedade seq10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ10(String value) {
        this.seq10 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini23.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI23() {
        return inscrini23;
    }

    /**
     * Define o valor da propriedade inscrini23.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI23(String value) {
        this.inscrini23 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim23.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM23() {
        return inscrfim23;
    }

    /**
     * Define o valor da propriedade inscrfim23.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM23(String value) {
        this.inscrfim23 = value;
    }

    /**
     * Obt�m o valor da propriedade seq23.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ23() {
        return seq23;
    }

    /**
     * Define o valor da propriedade seq23.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ23(String value) {
        this.seq23 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini36.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI36() {
        return inscrini36;
    }

    /**
     * Define o valor da propriedade inscrini36.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI36(String value) {
        this.inscrini36 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim36.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM36() {
        return inscrfim36;
    }

    /**
     * Define o valor da propriedade inscrfim36.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM36(String value) {
        this.inscrfim36 = value;
    }

    /**
     * Obt�m o valor da propriedade seq36.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ36() {
        return seq36;
    }

    /**
     * Define o valor da propriedade seq36.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ36(String value) {
        this.seq36 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI11() {
        return inscrini11;
    }

    /**
     * Define o valor da propriedade inscrini11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI11(String value) {
        this.inscrini11 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM11() {
        return inscrfim11;
    }

    /**
     * Define o valor da propriedade inscrfim11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM11(String value) {
        this.inscrfim11 = value;
    }

    /**
     * Obt�m o valor da propriedade seq11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ11() {
        return seq11;
    }

    /**
     * Define o valor da propriedade seq11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ11(String value) {
        this.seq11 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini24.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI24() {
        return inscrini24;
    }

    /**
     * Define o valor da propriedade inscrini24.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI24(String value) {
        this.inscrini24 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim24.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM24() {
        return inscrfim24;
    }

    /**
     * Define o valor da propriedade inscrfim24.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM24(String value) {
        this.inscrfim24 = value;
    }

    /**
     * Obt�m o valor da propriedade seq24.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ24() {
        return seq24;
    }

    /**
     * Define o valor da propriedade seq24.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ24(String value) {
        this.seq24 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini37.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI37() {
        return inscrini37;
    }

    /**
     * Define o valor da propriedade inscrini37.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI37(String value) {
        this.inscrini37 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim37.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM37() {
        return inscrfim37;
    }

    /**
     * Define o valor da propriedade inscrfim37.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM37(String value) {
        this.inscrfim37 = value;
    }

    /**
     * Obt�m o valor da propriedade seq37.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ37() {
        return seq37;
    }

    /**
     * Define o valor da propriedade seq37.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ37(String value) {
        this.seq37 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI12() {
        return inscrini12;
    }

    /**
     * Define o valor da propriedade inscrini12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI12(String value) {
        this.inscrini12 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM12() {
        return inscrfim12;
    }

    /**
     * Define o valor da propriedade inscrfim12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM12(String value) {
        this.inscrfim12 = value;
    }

    /**
     * Obt�m o valor da propriedade seq12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ12() {
        return seq12;
    }

    /**
     * Define o valor da propriedade seq12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ12(String value) {
        this.seq12 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini25.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI25() {
        return inscrini25;
    }

    /**
     * Define o valor da propriedade inscrini25.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI25(String value) {
        this.inscrini25 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim25.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM25() {
        return inscrfim25;
    }

    /**
     * Define o valor da propriedade inscrfim25.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM25(String value) {
        this.inscrfim25 = value;
    }

    /**
     * Obt�m o valor da propriedade seq25.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ25() {
        return seq25;
    }

    /**
     * Define o valor da propriedade seq25.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ25(String value) {
        this.seq25 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini38.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI38() {
        return inscrini38;
    }

    /**
     * Define o valor da propriedade inscrini38.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI38(String value) {
        this.inscrini38 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim38.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM38() {
        return inscrfim38;
    }

    /**
     * Define o valor da propriedade inscrfim38.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM38(String value) {
        this.inscrfim38 = value;
    }

    /**
     * Obt�m o valor da propriedade seq38.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ38() {
        return seq38;
    }

    /**
     * Define o valor da propriedade seq38.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ38(String value) {
        this.seq38 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI13() {
        return inscrini13;
    }

    /**
     * Define o valor da propriedade inscrini13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI13(String value) {
        this.inscrini13 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM13() {
        return inscrfim13;
    }

    /**
     * Define o valor da propriedade inscrfim13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM13(String value) {
        this.inscrfim13 = value;
    }

    /**
     * Obt�m o valor da propriedade seq13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ13() {
        return seq13;
    }

    /**
     * Define o valor da propriedade seq13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ13(String value) {
        this.seq13 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini26.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI26() {
        return inscrini26;
    }

    /**
     * Define o valor da propriedade inscrini26.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI26(String value) {
        this.inscrini26 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim26.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM26() {
        return inscrfim26;
    }

    /**
     * Define o valor da propriedade inscrfim26.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM26(String value) {
        this.inscrfim26 = value;
    }

    /**
     * Obt�m o valor da propriedade seq26.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ26() {
        return seq26;
    }

    /**
     * Define o valor da propriedade seq26.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ26(String value) {
        this.seq26 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrini39.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRINI39() {
        return inscrini39;
    }

    /**
     * Define o valor da propriedade inscrini39.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRINI39(String value) {
        this.inscrini39 = value;
    }

    /**
     * Obt�m o valor da propriedade inscrfim39.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSCRFIM39() {
        return inscrfim39;
    }

    /**
     * Define o valor da propriedade inscrfim39.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSCRFIM39(String value) {
        this.inscrfim39 = value;
    }

    /**
     * Obt�m o valor da propriedade seq39.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ39() {
        return seq39;
    }

    /**
     * Define o valor da propriedade seq39.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ39(String value) {
        this.seq39 = value;
    }

    /**
     * Obt�m o valor da propriedade lblconfirma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLBLCONFIRMA() {
        return lblconfirma;
    }

    /**
     * Define o valor da propriedade lblconfirma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLBLCONFIRMA(String value) {
        this.lblconfirma = value;
    }

    /**
     * Obt�m o valor da propriedade indconfirma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINDCONFIRMA() {
        return indconfirma;
    }

    /**
     * Define o valor da propriedade indconfirma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINDCONFIRMA(String value) {
        this.indconfirma = value;
    }

    /**
     * Obt�m o valor da propriedade statusLine.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusLine() {
        return statusLine;
    }

    /**
     * Define o valor da propriedade statusLine.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusLine(String value) {
        this.statusLine = value;
    }

}
