
package br.gov.iplanrio.integracao.sicop.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de anonymous complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ROTINA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OPCAO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGAOORIGEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGAODESTINO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRRECEBEDOR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMGUIA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTDIGITACAO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTRECEBIMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRPROCESSO1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTDESPACHO1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRDESPACHO1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRPROCESSO2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTDESPACHO2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRDESPACHO2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRPROCESSO3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTDESPACHO3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRDESPACHO3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRPROCESSO4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTDESPACHO4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRDESPACHO4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRPROCESSO5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTDESPACHO5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRDESPACHO5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRPROCESSO6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTDESPACHO6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRDESPACHO6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRPROCESSO7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTDESPACHO7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRDESPACHO7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRPROCESSO8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTDESPACHO8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRDESPACHO8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRPROCESSO9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTDESPACHO9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRDESPACHO9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRPROCESSO10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTDESPACHO10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRDESPACHO10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRPROCESSO11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTDESPACHO11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRDESPACHO11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRPROCESSO12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTDESPACHO12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRDESPACHO12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRPROCESSO13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTDESPACHO13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRDESPACHO13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRPROCESSO14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTDESPACHO14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRDESPACHO14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRPROCESSO15" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP15" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTDESPACHO15" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRDESPACHO15" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRPROCESSO16" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP16" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTDESPACHO16" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRDESPACHO16" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRPROCESSO17" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP17" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTDESPACHO17" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRDESPACHO17" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRPROCESSO18" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP18" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTDESPACHO18" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRDESPACHO18" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "rotina",
    "opcao",
    "orgaoorigem",
    "orgaodestino",
    "matrrecebedor",
    "numguia",
    "dtdigitacao",
    "dtrecebimento",
    "nrprocesso1",
    "coddesp1",
    "dtdespacho1",
    "matrdespacho1",
    "nrprocesso2",
    "coddesp2",
    "dtdespacho2",
    "matrdespacho2",
    "nrprocesso3",
    "coddesp3",
    "dtdespacho3",
    "matrdespacho3",
    "nrprocesso4",
    "coddesp4",
    "dtdespacho4",
    "matrdespacho4",
    "nrprocesso5",
    "coddesp5",
    "dtdespacho5",
    "matrdespacho5",
    "nrprocesso6",
    "coddesp6",
    "dtdespacho6",
    "matrdespacho6",
    "nrprocesso7",
    "coddesp7",
    "dtdespacho7",
    "matrdespacho7",
    "nrprocesso8",
    "coddesp8",
    "dtdespacho8",
    "matrdespacho8",
    "nrprocesso9",
    "coddesp9",
    "dtdespacho9",
    "matrdespacho9",
    "nrprocesso10",
    "coddesp10",
    "dtdespacho10",
    "matrdespacho10",
    "nrprocesso11",
    "coddesp11",
    "dtdespacho11",
    "matrdespacho11",
    "nrprocesso12",
    "coddesp12",
    "dtdespacho12",
    "matrdespacho12",
    "nrprocesso13",
    "coddesp13",
    "dtdespacho13",
    "matrdespacho13",
    "nrprocesso14",
    "coddesp14",
    "dtdespacho14",
    "matrdespacho14",
    "nrprocesso15",
    "coddesp15",
    "dtdespacho15",
    "matrdespacho15",
    "nrprocesso16",
    "coddesp16",
    "dtdespacho16",
    "matrdespacho16",
    "nrprocesso17",
    "coddesp17",
    "dtdespacho17",
    "matrdespacho17",
    "nrprocesso18",
    "coddesp18",
    "dtdespacho18",
    "matrdespacho18"
})
@XmlRootElement(name = "TTramitaProcesso_WS")
public class TTramitaProcessoWS {

    @XmlElement(name = "ROTINA")
    protected String rotina;
    @XmlElement(name = "OPCAO")
    protected String opcao;
    @XmlElement(name = "ORGAOORIGEM")
    protected String orgaoorigem;
    @XmlElement(name = "ORGAODESTINO")
    protected String orgaodestino;
    @XmlElement(name = "MATRRECEBEDOR")
    protected String matrrecebedor;
    @XmlElement(name = "NUMGUIA")
    protected String numguia;
    @XmlElement(name = "DTDIGITACAO")
    protected String dtdigitacao;
    @XmlElement(name = "DTRECEBIMENTO")
    protected String dtrecebimento;
    @XmlElement(name = "NRPROCESSO1")
    protected String nrprocesso1;
    @XmlElement(name = "CODDESP1")
    protected String coddesp1;
    @XmlElement(name = "DTDESPACHO1")
    protected String dtdespacho1;
    @XmlElement(name = "MATRDESPACHO1")
    protected String matrdespacho1;
    @XmlElement(name = "NRPROCESSO2")
    protected String nrprocesso2;
    @XmlElement(name = "CODDESP2")
    protected String coddesp2;
    @XmlElement(name = "DTDESPACHO2")
    protected String dtdespacho2;
    @XmlElement(name = "MATRDESPACHO2")
    protected String matrdespacho2;
    @XmlElement(name = "NRPROCESSO3")
    protected String nrprocesso3;
    @XmlElement(name = "CODDESP3")
    protected String coddesp3;
    @XmlElement(name = "DTDESPACHO3")
    protected String dtdespacho3;
    @XmlElement(name = "MATRDESPACHO3")
    protected String matrdespacho3;
    @XmlElement(name = "NRPROCESSO4")
    protected String nrprocesso4;
    @XmlElement(name = "CODDESP4")
    protected String coddesp4;
    @XmlElement(name = "DTDESPACHO4")
    protected String dtdespacho4;
    @XmlElement(name = "MATRDESPACHO4")
    protected String matrdespacho4;
    @XmlElement(name = "NRPROCESSO5")
    protected String nrprocesso5;
    @XmlElement(name = "CODDESP5")
    protected String coddesp5;
    @XmlElement(name = "DTDESPACHO5")
    protected String dtdespacho5;
    @XmlElement(name = "MATRDESPACHO5")
    protected String matrdespacho5;
    @XmlElement(name = "NRPROCESSO6")
    protected String nrprocesso6;
    @XmlElement(name = "CODDESP6")
    protected String coddesp6;
    @XmlElement(name = "DTDESPACHO6")
    protected String dtdespacho6;
    @XmlElement(name = "MATRDESPACHO6")
    protected String matrdespacho6;
    @XmlElement(name = "NRPROCESSO7")
    protected String nrprocesso7;
    @XmlElement(name = "CODDESP7")
    protected String coddesp7;
    @XmlElement(name = "DTDESPACHO7")
    protected String dtdespacho7;
    @XmlElement(name = "MATRDESPACHO7")
    protected String matrdespacho7;
    @XmlElement(name = "NRPROCESSO8")
    protected String nrprocesso8;
    @XmlElement(name = "CODDESP8")
    protected String coddesp8;
    @XmlElement(name = "DTDESPACHO8")
    protected String dtdespacho8;
    @XmlElement(name = "MATRDESPACHO8")
    protected String matrdespacho8;
    @XmlElement(name = "NRPROCESSO9")
    protected String nrprocesso9;
    @XmlElement(name = "CODDESP9")
    protected String coddesp9;
    @XmlElement(name = "DTDESPACHO9")
    protected String dtdespacho9;
    @XmlElement(name = "MATRDESPACHO9")
    protected String matrdespacho9;
    @XmlElement(name = "NRPROCESSO10")
    protected String nrprocesso10;
    @XmlElement(name = "CODDESP10")
    protected String coddesp10;
    @XmlElement(name = "DTDESPACHO10")
    protected String dtdespacho10;
    @XmlElement(name = "MATRDESPACHO10")
    protected String matrdespacho10;
    @XmlElement(name = "NRPROCESSO11")
    protected String nrprocesso11;
    @XmlElement(name = "CODDESP11")
    protected String coddesp11;
    @XmlElement(name = "DTDESPACHO11")
    protected String dtdespacho11;
    @XmlElement(name = "MATRDESPACHO11")
    protected String matrdespacho11;
    @XmlElement(name = "NRPROCESSO12")
    protected String nrprocesso12;
    @XmlElement(name = "CODDESP12")
    protected String coddesp12;
    @XmlElement(name = "DTDESPACHO12")
    protected String dtdespacho12;
    @XmlElement(name = "MATRDESPACHO12")
    protected String matrdespacho12;
    @XmlElement(name = "NRPROCESSO13")
    protected String nrprocesso13;
    @XmlElement(name = "CODDESP13")
    protected String coddesp13;
    @XmlElement(name = "DTDESPACHO13")
    protected String dtdespacho13;
    @XmlElement(name = "MATRDESPACHO13")
    protected String matrdespacho13;
    @XmlElement(name = "NRPROCESSO14")
    protected String nrprocesso14;
    @XmlElement(name = "CODDESP14")
    protected String coddesp14;
    @XmlElement(name = "DTDESPACHO14")
    protected String dtdespacho14;
    @XmlElement(name = "MATRDESPACHO14")
    protected String matrdespacho14;
    @XmlElement(name = "NRPROCESSO15")
    protected String nrprocesso15;
    @XmlElement(name = "CODDESP15")
    protected String coddesp15;
    @XmlElement(name = "DTDESPACHO15")
    protected String dtdespacho15;
    @XmlElement(name = "MATRDESPACHO15")
    protected String matrdespacho15;
    @XmlElement(name = "NRPROCESSO16")
    protected String nrprocesso16;
    @XmlElement(name = "CODDESP16")
    protected String coddesp16;
    @XmlElement(name = "DTDESPACHO16")
    protected String dtdespacho16;
    @XmlElement(name = "MATRDESPACHO16")
    protected String matrdespacho16;
    @XmlElement(name = "NRPROCESSO17")
    protected String nrprocesso17;
    @XmlElement(name = "CODDESP17")
    protected String coddesp17;
    @XmlElement(name = "DTDESPACHO17")
    protected String dtdespacho17;
    @XmlElement(name = "MATRDESPACHO17")
    protected String matrdespacho17;
    @XmlElement(name = "NRPROCESSO18")
    protected String nrprocesso18;
    @XmlElement(name = "CODDESP18")
    protected String coddesp18;
    @XmlElement(name = "DTDESPACHO18")
    protected String dtdespacho18;
    @XmlElement(name = "MATRDESPACHO18")
    protected String matrdespacho18;

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
     * Obt�m o valor da propriedade orgaoorigem.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGAOORIGEM() {
        return orgaoorigem;
    }

    /**
     * Define o valor da propriedade orgaoorigem.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGAOORIGEM(String value) {
        this.orgaoorigem = value;
    }

    /**
     * Obt�m o valor da propriedade orgaodestino.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGAODESTINO() {
        return orgaodestino;
    }

    /**
     * Define o valor da propriedade orgaodestino.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGAODESTINO(String value) {
        this.orgaodestino = value;
    }

    /**
     * Obt�m o valor da propriedade matrrecebedor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRRECEBEDOR() {
        return matrrecebedor;
    }

    /**
     * Define o valor da propriedade matrrecebedor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRRECEBEDOR(String value) {
        this.matrrecebedor = value;
    }

    /**
     * Obt�m o valor da propriedade numguia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMGUIA() {
        return numguia;
    }

    /**
     * Define o valor da propriedade numguia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMGUIA(String value) {
        this.numguia = value;
    }

    /**
     * Obt�m o valor da propriedade dtdigitacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTDIGITACAO() {
        return dtdigitacao;
    }

    /**
     * Define o valor da propriedade dtdigitacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTDIGITACAO(String value) {
        this.dtdigitacao = value;
    }

    /**
     * Obt�m o valor da propriedade dtrecebimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTRECEBIMENTO() {
        return dtrecebimento;
    }

    /**
     * Define o valor da propriedade dtrecebimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTRECEBIMENTO(String value) {
        this.dtrecebimento = value;
    }

    /**
     * Obt�m o valor da propriedade nrprocesso1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRPROCESSO1() {
        return nrprocesso1;
    }

    /**
     * Define o valor da propriedade nrprocesso1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRPROCESSO1(String value) {
        this.nrprocesso1 = value;
    }

    /**
     * Obt�m o valor da propriedade coddesp1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDESP1() {
        return coddesp1;
    }

    /**
     * Define o valor da propriedade coddesp1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDESP1(String value) {
        this.coddesp1 = value;
    }

    /**
     * Obt�m o valor da propriedade dtdespacho1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTDESPACHO1() {
        return dtdespacho1;
    }

    /**
     * Define o valor da propriedade dtdespacho1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTDESPACHO1(String value) {
        this.dtdespacho1 = value;
    }

    /**
     * Obt�m o valor da propriedade matrdespacho1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRDESPACHO1() {
        return matrdespacho1;
    }

    /**
     * Define o valor da propriedade matrdespacho1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRDESPACHO1(String value) {
        this.matrdespacho1 = value;
    }

    /**
     * Obt�m o valor da propriedade nrprocesso2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRPROCESSO2() {
        return nrprocesso2;
    }

    /**
     * Define o valor da propriedade nrprocesso2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRPROCESSO2(String value) {
        this.nrprocesso2 = value;
    }

    /**
     * Obt�m o valor da propriedade coddesp2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDESP2() {
        return coddesp2;
    }

    /**
     * Define o valor da propriedade coddesp2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDESP2(String value) {
        this.coddesp2 = value;
    }

    /**
     * Obt�m o valor da propriedade dtdespacho2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTDESPACHO2() {
        return dtdespacho2;
    }

    /**
     * Define o valor da propriedade dtdespacho2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTDESPACHO2(String value) {
        this.dtdespacho2 = value;
    }

    /**
     * Obt�m o valor da propriedade matrdespacho2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRDESPACHO2() {
        return matrdespacho2;
    }

    /**
     * Define o valor da propriedade matrdespacho2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRDESPACHO2(String value) {
        this.matrdespacho2 = value;
    }

    /**
     * Obt�m o valor da propriedade nrprocesso3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRPROCESSO3() {
        return nrprocesso3;
    }

    /**
     * Define o valor da propriedade nrprocesso3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRPROCESSO3(String value) {
        this.nrprocesso3 = value;
    }

    /**
     * Obt�m o valor da propriedade coddesp3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDESP3() {
        return coddesp3;
    }

    /**
     * Define o valor da propriedade coddesp3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDESP3(String value) {
        this.coddesp3 = value;
    }

    /**
     * Obt�m o valor da propriedade dtdespacho3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTDESPACHO3() {
        return dtdespacho3;
    }

    /**
     * Define o valor da propriedade dtdespacho3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTDESPACHO3(String value) {
        this.dtdespacho3 = value;
    }

    /**
     * Obt�m o valor da propriedade matrdespacho3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRDESPACHO3() {
        return matrdespacho3;
    }

    /**
     * Define o valor da propriedade matrdespacho3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRDESPACHO3(String value) {
        this.matrdespacho3 = value;
    }

    /**
     * Obt�m o valor da propriedade nrprocesso4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRPROCESSO4() {
        return nrprocesso4;
    }

    /**
     * Define o valor da propriedade nrprocesso4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRPROCESSO4(String value) {
        this.nrprocesso4 = value;
    }

    /**
     * Obt�m o valor da propriedade coddesp4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDESP4() {
        return coddesp4;
    }

    /**
     * Define o valor da propriedade coddesp4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDESP4(String value) {
        this.coddesp4 = value;
    }

    /**
     * Obt�m o valor da propriedade dtdespacho4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTDESPACHO4() {
        return dtdespacho4;
    }

    /**
     * Define o valor da propriedade dtdespacho4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTDESPACHO4(String value) {
        this.dtdespacho4 = value;
    }

    /**
     * Obt�m o valor da propriedade matrdespacho4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRDESPACHO4() {
        return matrdespacho4;
    }

    /**
     * Define o valor da propriedade matrdespacho4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRDESPACHO4(String value) {
        this.matrdespacho4 = value;
    }

    /**
     * Obt�m o valor da propriedade nrprocesso5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRPROCESSO5() {
        return nrprocesso5;
    }

    /**
     * Define o valor da propriedade nrprocesso5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRPROCESSO5(String value) {
        this.nrprocesso5 = value;
    }

    /**
     * Obt�m o valor da propriedade coddesp5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDESP5() {
        return coddesp5;
    }

    /**
     * Define o valor da propriedade coddesp5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDESP5(String value) {
        this.coddesp5 = value;
    }

    /**
     * Obt�m o valor da propriedade dtdespacho5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTDESPACHO5() {
        return dtdespacho5;
    }

    /**
     * Define o valor da propriedade dtdespacho5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTDESPACHO5(String value) {
        this.dtdespacho5 = value;
    }

    /**
     * Obt�m o valor da propriedade matrdespacho5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRDESPACHO5() {
        return matrdespacho5;
    }

    /**
     * Define o valor da propriedade matrdespacho5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRDESPACHO5(String value) {
        this.matrdespacho5 = value;
    }

    /**
     * Obt�m o valor da propriedade nrprocesso6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRPROCESSO6() {
        return nrprocesso6;
    }

    /**
     * Define o valor da propriedade nrprocesso6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRPROCESSO6(String value) {
        this.nrprocesso6 = value;
    }

    /**
     * Obt�m o valor da propriedade coddesp6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDESP6() {
        return coddesp6;
    }

    /**
     * Define o valor da propriedade coddesp6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDESP6(String value) {
        this.coddesp6 = value;
    }

    /**
     * Obt�m o valor da propriedade dtdespacho6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTDESPACHO6() {
        return dtdespacho6;
    }

    /**
     * Define o valor da propriedade dtdespacho6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTDESPACHO6(String value) {
        this.dtdespacho6 = value;
    }

    /**
     * Obt�m o valor da propriedade matrdespacho6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRDESPACHO6() {
        return matrdespacho6;
    }

    /**
     * Define o valor da propriedade matrdespacho6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRDESPACHO6(String value) {
        this.matrdespacho6 = value;
    }

    /**
     * Obt�m o valor da propriedade nrprocesso7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRPROCESSO7() {
        return nrprocesso7;
    }

    /**
     * Define o valor da propriedade nrprocesso7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRPROCESSO7(String value) {
        this.nrprocesso7 = value;
    }

    /**
     * Obt�m o valor da propriedade coddesp7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDESP7() {
        return coddesp7;
    }

    /**
     * Define o valor da propriedade coddesp7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDESP7(String value) {
        this.coddesp7 = value;
    }

    /**
     * Obt�m o valor da propriedade dtdespacho7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTDESPACHO7() {
        return dtdespacho7;
    }

    /**
     * Define o valor da propriedade dtdespacho7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTDESPACHO7(String value) {
        this.dtdespacho7 = value;
    }

    /**
     * Obt�m o valor da propriedade matrdespacho7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRDESPACHO7() {
        return matrdespacho7;
    }

    /**
     * Define o valor da propriedade matrdespacho7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRDESPACHO7(String value) {
        this.matrdespacho7 = value;
    }

    /**
     * Obt�m o valor da propriedade nrprocesso8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRPROCESSO8() {
        return nrprocesso8;
    }

    /**
     * Define o valor da propriedade nrprocesso8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRPROCESSO8(String value) {
        this.nrprocesso8 = value;
    }

    /**
     * Obt�m o valor da propriedade coddesp8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDESP8() {
        return coddesp8;
    }

    /**
     * Define o valor da propriedade coddesp8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDESP8(String value) {
        this.coddesp8 = value;
    }

    /**
     * Obt�m o valor da propriedade dtdespacho8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTDESPACHO8() {
        return dtdespacho8;
    }

    /**
     * Define o valor da propriedade dtdespacho8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTDESPACHO8(String value) {
        this.dtdespacho8 = value;
    }

    /**
     * Obt�m o valor da propriedade matrdespacho8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRDESPACHO8() {
        return matrdespacho8;
    }

    /**
     * Define o valor da propriedade matrdespacho8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRDESPACHO8(String value) {
        this.matrdespacho8 = value;
    }

    /**
     * Obt�m o valor da propriedade nrprocesso9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRPROCESSO9() {
        return nrprocesso9;
    }

    /**
     * Define o valor da propriedade nrprocesso9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRPROCESSO9(String value) {
        this.nrprocesso9 = value;
    }

    /**
     * Obt�m o valor da propriedade coddesp9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDESP9() {
        return coddesp9;
    }

    /**
     * Define o valor da propriedade coddesp9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDESP9(String value) {
        this.coddesp9 = value;
    }

    /**
     * Obt�m o valor da propriedade dtdespacho9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTDESPACHO9() {
        return dtdespacho9;
    }

    /**
     * Define o valor da propriedade dtdespacho9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTDESPACHO9(String value) {
        this.dtdespacho9 = value;
    }

    /**
     * Obt�m o valor da propriedade matrdespacho9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRDESPACHO9() {
        return matrdespacho9;
    }

    /**
     * Define o valor da propriedade matrdespacho9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRDESPACHO9(String value) {
        this.matrdespacho9 = value;
    }

    /**
     * Obt�m o valor da propriedade nrprocesso10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRPROCESSO10() {
        return nrprocesso10;
    }

    /**
     * Define o valor da propriedade nrprocesso10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRPROCESSO10(String value) {
        this.nrprocesso10 = value;
    }

    /**
     * Obt�m o valor da propriedade coddesp10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDESP10() {
        return coddesp10;
    }

    /**
     * Define o valor da propriedade coddesp10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDESP10(String value) {
        this.coddesp10 = value;
    }

    /**
     * Obt�m o valor da propriedade dtdespacho10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTDESPACHO10() {
        return dtdespacho10;
    }

    /**
     * Define o valor da propriedade dtdespacho10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTDESPACHO10(String value) {
        this.dtdespacho10 = value;
    }

    /**
     * Obt�m o valor da propriedade matrdespacho10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRDESPACHO10() {
        return matrdespacho10;
    }

    /**
     * Define o valor da propriedade matrdespacho10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRDESPACHO10(String value) {
        this.matrdespacho10 = value;
    }

    /**
     * Obt�m o valor da propriedade nrprocesso11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRPROCESSO11() {
        return nrprocesso11;
    }

    /**
     * Define o valor da propriedade nrprocesso11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRPROCESSO11(String value) {
        this.nrprocesso11 = value;
    }

    /**
     * Obt�m o valor da propriedade coddesp11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDESP11() {
        return coddesp11;
    }

    /**
     * Define o valor da propriedade coddesp11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDESP11(String value) {
        this.coddesp11 = value;
    }

    /**
     * Obt�m o valor da propriedade dtdespacho11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTDESPACHO11() {
        return dtdespacho11;
    }

    /**
     * Define o valor da propriedade dtdespacho11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTDESPACHO11(String value) {
        this.dtdespacho11 = value;
    }

    /**
     * Obt�m o valor da propriedade matrdespacho11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRDESPACHO11() {
        return matrdespacho11;
    }

    /**
     * Define o valor da propriedade matrdespacho11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRDESPACHO11(String value) {
        this.matrdespacho11 = value;
    }

    /**
     * Obt�m o valor da propriedade nrprocesso12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRPROCESSO12() {
        return nrprocesso12;
    }

    /**
     * Define o valor da propriedade nrprocesso12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRPROCESSO12(String value) {
        this.nrprocesso12 = value;
    }

    /**
     * Obt�m o valor da propriedade coddesp12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDESP12() {
        return coddesp12;
    }

    /**
     * Define o valor da propriedade coddesp12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDESP12(String value) {
        this.coddesp12 = value;
    }

    /**
     * Obt�m o valor da propriedade dtdespacho12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTDESPACHO12() {
        return dtdespacho12;
    }

    /**
     * Define o valor da propriedade dtdespacho12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTDESPACHO12(String value) {
        this.dtdespacho12 = value;
    }

    /**
     * Obt�m o valor da propriedade matrdespacho12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRDESPACHO12() {
        return matrdespacho12;
    }

    /**
     * Define o valor da propriedade matrdespacho12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRDESPACHO12(String value) {
        this.matrdespacho12 = value;
    }

    /**
     * Obt�m o valor da propriedade nrprocesso13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRPROCESSO13() {
        return nrprocesso13;
    }

    /**
     * Define o valor da propriedade nrprocesso13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRPROCESSO13(String value) {
        this.nrprocesso13 = value;
    }

    /**
     * Obt�m o valor da propriedade coddesp13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDESP13() {
        return coddesp13;
    }

    /**
     * Define o valor da propriedade coddesp13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDESP13(String value) {
        this.coddesp13 = value;
    }

    /**
     * Obt�m o valor da propriedade dtdespacho13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTDESPACHO13() {
        return dtdespacho13;
    }

    /**
     * Define o valor da propriedade dtdespacho13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTDESPACHO13(String value) {
        this.dtdespacho13 = value;
    }

    /**
     * Obt�m o valor da propriedade matrdespacho13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRDESPACHO13() {
        return matrdespacho13;
    }

    /**
     * Define o valor da propriedade matrdespacho13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRDESPACHO13(String value) {
        this.matrdespacho13 = value;
    }

    /**
     * Obt�m o valor da propriedade nrprocesso14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRPROCESSO14() {
        return nrprocesso14;
    }

    /**
     * Define o valor da propriedade nrprocesso14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRPROCESSO14(String value) {
        this.nrprocesso14 = value;
    }

    /**
     * Obt�m o valor da propriedade coddesp14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDESP14() {
        return coddesp14;
    }

    /**
     * Define o valor da propriedade coddesp14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDESP14(String value) {
        this.coddesp14 = value;
    }

    /**
     * Obt�m o valor da propriedade dtdespacho14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTDESPACHO14() {
        return dtdespacho14;
    }

    /**
     * Define o valor da propriedade dtdespacho14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTDESPACHO14(String value) {
        this.dtdespacho14 = value;
    }

    /**
     * Obt�m o valor da propriedade matrdespacho14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRDESPACHO14() {
        return matrdespacho14;
    }

    /**
     * Define o valor da propriedade matrdespacho14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRDESPACHO14(String value) {
        this.matrdespacho14 = value;
    }

    /**
     * Obt�m o valor da propriedade nrprocesso15.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRPROCESSO15() {
        return nrprocesso15;
    }

    /**
     * Define o valor da propriedade nrprocesso15.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRPROCESSO15(String value) {
        this.nrprocesso15 = value;
    }

    /**
     * Obt�m o valor da propriedade coddesp15.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDESP15() {
        return coddesp15;
    }

    /**
     * Define o valor da propriedade coddesp15.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDESP15(String value) {
        this.coddesp15 = value;
    }

    /**
     * Obt�m o valor da propriedade dtdespacho15.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTDESPACHO15() {
        return dtdespacho15;
    }

    /**
     * Define o valor da propriedade dtdespacho15.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTDESPACHO15(String value) {
        this.dtdespacho15 = value;
    }

    /**
     * Obt�m o valor da propriedade matrdespacho15.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRDESPACHO15() {
        return matrdespacho15;
    }

    /**
     * Define o valor da propriedade matrdespacho15.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRDESPACHO15(String value) {
        this.matrdespacho15 = value;
    }

    /**
     * Obt�m o valor da propriedade nrprocesso16.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRPROCESSO16() {
        return nrprocesso16;
    }

    /**
     * Define o valor da propriedade nrprocesso16.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRPROCESSO16(String value) {
        this.nrprocesso16 = value;
    }

    /**
     * Obt�m o valor da propriedade coddesp16.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDESP16() {
        return coddesp16;
    }

    /**
     * Define o valor da propriedade coddesp16.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDESP16(String value) {
        this.coddesp16 = value;
    }

    /**
     * Obt�m o valor da propriedade dtdespacho16.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTDESPACHO16() {
        return dtdespacho16;
    }

    /**
     * Define o valor da propriedade dtdespacho16.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTDESPACHO16(String value) {
        this.dtdespacho16 = value;
    }

    /**
     * Obt�m o valor da propriedade matrdespacho16.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRDESPACHO16() {
        return matrdespacho16;
    }

    /**
     * Define o valor da propriedade matrdespacho16.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRDESPACHO16(String value) {
        this.matrdespacho16 = value;
    }

    /**
     * Obt�m o valor da propriedade nrprocesso17.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRPROCESSO17() {
        return nrprocesso17;
    }

    /**
     * Define o valor da propriedade nrprocesso17.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRPROCESSO17(String value) {
        this.nrprocesso17 = value;
    }

    /**
     * Obt�m o valor da propriedade coddesp17.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDESP17() {
        return coddesp17;
    }

    /**
     * Define o valor da propriedade coddesp17.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDESP17(String value) {
        this.coddesp17 = value;
    }

    /**
     * Obt�m o valor da propriedade dtdespacho17.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTDESPACHO17() {
        return dtdespacho17;
    }

    /**
     * Define o valor da propriedade dtdespacho17.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTDESPACHO17(String value) {
        this.dtdespacho17 = value;
    }

    /**
     * Obt�m o valor da propriedade matrdespacho17.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRDESPACHO17() {
        return matrdespacho17;
    }

    /**
     * Define o valor da propriedade matrdespacho17.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRDESPACHO17(String value) {
        this.matrdespacho17 = value;
    }

    /**
     * Obt�m o valor da propriedade nrprocesso18.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRPROCESSO18() {
        return nrprocesso18;
    }

    /**
     * Define o valor da propriedade nrprocesso18.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRPROCESSO18(String value) {
        this.nrprocesso18 = value;
    }

    /**
     * Obt�m o valor da propriedade coddesp18.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODDESP18() {
        return coddesp18;
    }

    /**
     * Define o valor da propriedade coddesp18.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODDESP18(String value) {
        this.coddesp18 = value;
    }

    /**
     * Obt�m o valor da propriedade dtdespacho18.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTDESPACHO18() {
        return dtdespacho18;
    }

    /**
     * Define o valor da propriedade dtdespacho18.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTDESPACHO18(String value) {
        this.dtdespacho18 = value;
    }

    /**
     * Obt�m o valor da propriedade matrdespacho18.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRDESPACHO18() {
        return matrdespacho18;
    }

    /**
     * Define o valor da propriedade matrdespacho18.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRDESPACHO18(String value) {
        this.matrdespacho18 = value;
    }

}
