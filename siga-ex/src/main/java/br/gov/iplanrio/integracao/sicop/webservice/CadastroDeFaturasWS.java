
package br.gov.iplanrio.integracao.sicop.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de CadastroDeFaturas_WS complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="CadastroDeFaturas_WS">
 *   &lt;complexContent>
 *     &lt;extension base="{http://pcrj/Sma/Sicop/Grava}Message">
 *       &lt;sequence>
 *         &lt;element name="ROTINA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OPCAO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRPROCESSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CNPJ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PROCINSTRUTIVO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRFATOUNF1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTEMISOUVENC1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VALOR1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPET1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTINICIO1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTFIM1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRFATOUNF2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTEMISOUVENC2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VALOR2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPET2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTINICIO2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTFIM2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRFATOUNF3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTEMISOUVENC3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VALOR3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPET3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTINICIO3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTFIM3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRFATOUNF4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTEMISOUVENC4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VALOR4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPET4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTINICIO4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTFIM4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRFATOUNF5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTEMISOUVENC5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VALOR5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPET5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTINICIO5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTFIM5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRFATOUNF6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTEMISOUVENC6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VALOR6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPET6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTINICIO6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTFIM6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRFATOUNF7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTEMISOUVENC7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VALOR7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPET7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTINICIO7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTFIM7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRFATOUNF8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTEMISOUVENC8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VALOR8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPET8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTINICIO8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTFIM8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRFATOUNF9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTEMISOUVENC9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VALOR9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPET9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTINICIO9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTFIM9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRFATOUNF10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTEMISOUVENC10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VALOR10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPET10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTINICIO10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTFIM10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRFATOUNF11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTEMISOUVENC11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VALOR11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPET11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTINICIO11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTFIM11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRFATOUNF12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTEMISOUVENC12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VALOR12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPET12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTINICIO12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTFIM12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRFATOUNF13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTEMISOUVENC13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VALOR13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPET13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTINICIO13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DTFIM13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "CadastroDeFaturas_WS", propOrder = {
    "rotina",
    "opcao",
    "nrprocesso",
    "cnpj",
    "procinstrutivo",
    "nrfatounf1",
    "dtemisouvenc1",
    "valor1",
    "compet1",
    "dtinicio1",
    "dtfim1",
    "nrfatounf2",
    "dtemisouvenc2",
    "valor2",
    "compet2",
    "dtinicio2",
    "dtfim2",
    "nrfatounf3",
    "dtemisouvenc3",
    "valor3",
    "compet3",
    "dtinicio3",
    "dtfim3",
    "nrfatounf4",
    "dtemisouvenc4",
    "valor4",
    "compet4",
    "dtinicio4",
    "dtfim4",
    "nrfatounf5",
    "dtemisouvenc5",
    "valor5",
    "compet5",
    "dtinicio5",
    "dtfim5",
    "nrfatounf6",
    "dtemisouvenc6",
    "valor6",
    "compet6",
    "dtinicio6",
    "dtfim6",
    "nrfatounf7",
    "dtemisouvenc7",
    "valor7",
    "compet7",
    "dtinicio7",
    "dtfim7",
    "nrfatounf8",
    "dtemisouvenc8",
    "valor8",
    "compet8",
    "dtinicio8",
    "dtfim8",
    "nrfatounf9",
    "dtemisouvenc9",
    "valor9",
    "compet9",
    "dtinicio9",
    "dtfim9",
    "nrfatounf10",
    "dtemisouvenc10",
    "valor10",
    "compet10",
    "dtinicio10",
    "dtfim10",
    "nrfatounf11",
    "dtemisouvenc11",
    "valor11",
    "compet11",
    "dtinicio11",
    "dtfim11",
    "nrfatounf12",
    "dtemisouvenc12",
    "valor12",
    "compet12",
    "dtinicio12",
    "dtfim12",
    "nrfatounf13",
    "dtemisouvenc13",
    "valor13",
    "compet13",
    "dtinicio13",
    "dtfim13",
    "lblconfirma",
    "indconfirma",
    "statusLine"
})
public class CadastroDeFaturasWS
    extends Message
{

    @XmlElement(name = "ROTINA")
    protected String rotina;
    @XmlElement(name = "OPCAO")
    protected String opcao;
    @XmlElement(name = "NRPROCESSO")
    protected String nrprocesso;
    @XmlElement(name = "CNPJ")
    protected String cnpj;
    @XmlElement(name = "PROCINSTRUTIVO")
    protected String procinstrutivo;
    @XmlElement(name = "NRFATOUNF1")
    protected String nrfatounf1;
    @XmlElement(name = "DTEMISOUVENC1")
    protected String dtemisouvenc1;
    @XmlElement(name = "VALOR1")
    protected String valor1;
    @XmlElement(name = "COMPET1")
    protected String compet1;
    @XmlElement(name = "DTINICIO1")
    protected String dtinicio1;
    @XmlElement(name = "DTFIM1")
    protected String dtfim1;
    @XmlElement(name = "NRFATOUNF2")
    protected String nrfatounf2;
    @XmlElement(name = "DTEMISOUVENC2")
    protected String dtemisouvenc2;
    @XmlElement(name = "VALOR2")
    protected String valor2;
    @XmlElement(name = "COMPET2")
    protected String compet2;
    @XmlElement(name = "DTINICIO2")
    protected String dtinicio2;
    @XmlElement(name = "DTFIM2")
    protected String dtfim2;
    @XmlElement(name = "NRFATOUNF3")
    protected String nrfatounf3;
    @XmlElement(name = "DTEMISOUVENC3")
    protected String dtemisouvenc3;
    @XmlElement(name = "VALOR3")
    protected String valor3;
    @XmlElement(name = "COMPET3")
    protected String compet3;
    @XmlElement(name = "DTINICIO3")
    protected String dtinicio3;
    @XmlElement(name = "DTFIM3")
    protected String dtfim3;
    @XmlElement(name = "NRFATOUNF4")
    protected String nrfatounf4;
    @XmlElement(name = "DTEMISOUVENC4")
    protected String dtemisouvenc4;
    @XmlElement(name = "VALOR4")
    protected String valor4;
    @XmlElement(name = "COMPET4")
    protected String compet4;
    @XmlElement(name = "DTINICIO4")
    protected String dtinicio4;
    @XmlElement(name = "DTFIM4")
    protected String dtfim4;
    @XmlElement(name = "NRFATOUNF5")
    protected String nrfatounf5;
    @XmlElement(name = "DTEMISOUVENC5")
    protected String dtemisouvenc5;
    @XmlElement(name = "VALOR5")
    protected String valor5;
    @XmlElement(name = "COMPET5")
    protected String compet5;
    @XmlElement(name = "DTINICIO5")
    protected String dtinicio5;
    @XmlElement(name = "DTFIM5")
    protected String dtfim5;
    @XmlElement(name = "NRFATOUNF6")
    protected String nrfatounf6;
    @XmlElement(name = "DTEMISOUVENC6")
    protected String dtemisouvenc6;
    @XmlElement(name = "VALOR6")
    protected String valor6;
    @XmlElement(name = "COMPET6")
    protected String compet6;
    @XmlElement(name = "DTINICIO6")
    protected String dtinicio6;
    @XmlElement(name = "DTFIM6")
    protected String dtfim6;
    @XmlElement(name = "NRFATOUNF7")
    protected String nrfatounf7;
    @XmlElement(name = "DTEMISOUVENC7")
    protected String dtemisouvenc7;
    @XmlElement(name = "VALOR7")
    protected String valor7;
    @XmlElement(name = "COMPET7")
    protected String compet7;
    @XmlElement(name = "DTINICIO7")
    protected String dtinicio7;
    @XmlElement(name = "DTFIM7")
    protected String dtfim7;
    @XmlElement(name = "NRFATOUNF8")
    protected String nrfatounf8;
    @XmlElement(name = "DTEMISOUVENC8")
    protected String dtemisouvenc8;
    @XmlElement(name = "VALOR8")
    protected String valor8;
    @XmlElement(name = "COMPET8")
    protected String compet8;
    @XmlElement(name = "DTINICIO8")
    protected String dtinicio8;
    @XmlElement(name = "DTFIM8")
    protected String dtfim8;
    @XmlElement(name = "NRFATOUNF9")
    protected String nrfatounf9;
    @XmlElement(name = "DTEMISOUVENC9")
    protected String dtemisouvenc9;
    @XmlElement(name = "VALOR9")
    protected String valor9;
    @XmlElement(name = "COMPET9")
    protected String compet9;
    @XmlElement(name = "DTINICIO9")
    protected String dtinicio9;
    @XmlElement(name = "DTFIM9")
    protected String dtfim9;
    @XmlElement(name = "NRFATOUNF10")
    protected String nrfatounf10;
    @XmlElement(name = "DTEMISOUVENC10")
    protected String dtemisouvenc10;
    @XmlElement(name = "VALOR10")
    protected String valor10;
    @XmlElement(name = "COMPET10")
    protected String compet10;
    @XmlElement(name = "DTINICIO10")
    protected String dtinicio10;
    @XmlElement(name = "DTFIM10")
    protected String dtfim10;
    @XmlElement(name = "NRFATOUNF11")
    protected String nrfatounf11;
    @XmlElement(name = "DTEMISOUVENC11")
    protected String dtemisouvenc11;
    @XmlElement(name = "VALOR11")
    protected String valor11;
    @XmlElement(name = "COMPET11")
    protected String compet11;
    @XmlElement(name = "DTINICIO11")
    protected String dtinicio11;
    @XmlElement(name = "DTFIM11")
    protected String dtfim11;
    @XmlElement(name = "NRFATOUNF12")
    protected String nrfatounf12;
    @XmlElement(name = "DTEMISOUVENC12")
    protected String dtemisouvenc12;
    @XmlElement(name = "VALOR12")
    protected String valor12;
    @XmlElement(name = "COMPET12")
    protected String compet12;
    @XmlElement(name = "DTINICIO12")
    protected String dtinicio12;
    @XmlElement(name = "DTFIM12")
    protected String dtfim12;
    @XmlElement(name = "NRFATOUNF13")
    protected String nrfatounf13;
    @XmlElement(name = "DTEMISOUVENC13")
    protected String dtemisouvenc13;
    @XmlElement(name = "VALOR13")
    protected String valor13;
    @XmlElement(name = "COMPET13")
    protected String compet13;
    @XmlElement(name = "DTINICIO13")
    protected String dtinicio13;
    @XmlElement(name = "DTFIM13")
    protected String dtfim13;
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
     * Obt�m o valor da propriedade nrprocesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRPROCESSO() {
        return nrprocesso;
    }

    /**
     * Define o valor da propriedade nrprocesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRPROCESSO(String value) {
        this.nrprocesso = value;
    }

    /**
     * Obt�m o valor da propriedade cnpj.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCNPJ() {
        return cnpj;
    }

    /**
     * Define o valor da propriedade cnpj.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCNPJ(String value) {
        this.cnpj = value;
    }

    /**
     * Obt�m o valor da propriedade procinstrutivo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPROCINSTRUTIVO() {
        return procinstrutivo;
    }

    /**
     * Define o valor da propriedade procinstrutivo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPROCINSTRUTIVO(String value) {
        this.procinstrutivo = value;
    }

    /**
     * Obt�m o valor da propriedade nrfatounf1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRFATOUNF1() {
        return nrfatounf1;
    }

    /**
     * Define o valor da propriedade nrfatounf1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRFATOUNF1(String value) {
        this.nrfatounf1 = value;
    }

    /**
     * Obt�m o valor da propriedade dtemisouvenc1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTEMISOUVENC1() {
        return dtemisouvenc1;
    }

    /**
     * Define o valor da propriedade dtemisouvenc1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTEMISOUVENC1(String value) {
        this.dtemisouvenc1 = value;
    }

    /**
     * Obt�m o valor da propriedade valor1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVALOR1() {
        return valor1;
    }

    /**
     * Define o valor da propriedade valor1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVALOR1(String value) {
        this.valor1 = value;
    }

    /**
     * Obt�m o valor da propriedade compet1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPET1() {
        return compet1;
    }

    /**
     * Define o valor da propriedade compet1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPET1(String value) {
        this.compet1 = value;
    }

    /**
     * Obt�m o valor da propriedade dtinicio1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTINICIO1() {
        return dtinicio1;
    }

    /**
     * Define o valor da propriedade dtinicio1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTINICIO1(String value) {
        this.dtinicio1 = value;
    }

    /**
     * Obt�m o valor da propriedade dtfim1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTFIM1() {
        return dtfim1;
    }

    /**
     * Define o valor da propriedade dtfim1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTFIM1(String value) {
        this.dtfim1 = value;
    }

    /**
     * Obt�m o valor da propriedade nrfatounf2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRFATOUNF2() {
        return nrfatounf2;
    }

    /**
     * Define o valor da propriedade nrfatounf2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRFATOUNF2(String value) {
        this.nrfatounf2 = value;
    }

    /**
     * Obt�m o valor da propriedade dtemisouvenc2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTEMISOUVENC2() {
        return dtemisouvenc2;
    }

    /**
     * Define o valor da propriedade dtemisouvenc2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTEMISOUVENC2(String value) {
        this.dtemisouvenc2 = value;
    }

    /**
     * Obt�m o valor da propriedade valor2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVALOR2() {
        return valor2;
    }

    /**
     * Define o valor da propriedade valor2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVALOR2(String value) {
        this.valor2 = value;
    }

    /**
     * Obt�m o valor da propriedade compet2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPET2() {
        return compet2;
    }

    /**
     * Define o valor da propriedade compet2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPET2(String value) {
        this.compet2 = value;
    }

    /**
     * Obt�m o valor da propriedade dtinicio2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTINICIO2() {
        return dtinicio2;
    }

    /**
     * Define o valor da propriedade dtinicio2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTINICIO2(String value) {
        this.dtinicio2 = value;
    }

    /**
     * Obt�m o valor da propriedade dtfim2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTFIM2() {
        return dtfim2;
    }

    /**
     * Define o valor da propriedade dtfim2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTFIM2(String value) {
        this.dtfim2 = value;
    }

    /**
     * Obt�m o valor da propriedade nrfatounf3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRFATOUNF3() {
        return nrfatounf3;
    }

    /**
     * Define o valor da propriedade nrfatounf3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRFATOUNF3(String value) {
        this.nrfatounf3 = value;
    }

    /**
     * Obt�m o valor da propriedade dtemisouvenc3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTEMISOUVENC3() {
        return dtemisouvenc3;
    }

    /**
     * Define o valor da propriedade dtemisouvenc3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTEMISOUVENC3(String value) {
        this.dtemisouvenc3 = value;
    }

    /**
     * Obt�m o valor da propriedade valor3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVALOR3() {
        return valor3;
    }

    /**
     * Define o valor da propriedade valor3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVALOR3(String value) {
        this.valor3 = value;
    }

    /**
     * Obt�m o valor da propriedade compet3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPET3() {
        return compet3;
    }

    /**
     * Define o valor da propriedade compet3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPET3(String value) {
        this.compet3 = value;
    }

    /**
     * Obt�m o valor da propriedade dtinicio3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTINICIO3() {
        return dtinicio3;
    }

    /**
     * Define o valor da propriedade dtinicio3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTINICIO3(String value) {
        this.dtinicio3 = value;
    }

    /**
     * Obt�m o valor da propriedade dtfim3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTFIM3() {
        return dtfim3;
    }

    /**
     * Define o valor da propriedade dtfim3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTFIM3(String value) {
        this.dtfim3 = value;
    }

    /**
     * Obt�m o valor da propriedade nrfatounf4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRFATOUNF4() {
        return nrfatounf4;
    }

    /**
     * Define o valor da propriedade nrfatounf4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRFATOUNF4(String value) {
        this.nrfatounf4 = value;
    }

    /**
     * Obt�m o valor da propriedade dtemisouvenc4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTEMISOUVENC4() {
        return dtemisouvenc4;
    }

    /**
     * Define o valor da propriedade dtemisouvenc4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTEMISOUVENC4(String value) {
        this.dtemisouvenc4 = value;
    }

    /**
     * Obt�m o valor da propriedade valor4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVALOR4() {
        return valor4;
    }

    /**
     * Define o valor da propriedade valor4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVALOR4(String value) {
        this.valor4 = value;
    }

    /**
     * Obt�m o valor da propriedade compet4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPET4() {
        return compet4;
    }

    /**
     * Define o valor da propriedade compet4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPET4(String value) {
        this.compet4 = value;
    }

    /**
     * Obt�m o valor da propriedade dtinicio4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTINICIO4() {
        return dtinicio4;
    }

    /**
     * Define o valor da propriedade dtinicio4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTINICIO4(String value) {
        this.dtinicio4 = value;
    }

    /**
     * Obt�m o valor da propriedade dtfim4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTFIM4() {
        return dtfim4;
    }

    /**
     * Define o valor da propriedade dtfim4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTFIM4(String value) {
        this.dtfim4 = value;
    }

    /**
     * Obt�m o valor da propriedade nrfatounf5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRFATOUNF5() {
        return nrfatounf5;
    }

    /**
     * Define o valor da propriedade nrfatounf5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRFATOUNF5(String value) {
        this.nrfatounf5 = value;
    }

    /**
     * Obt�m o valor da propriedade dtemisouvenc5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTEMISOUVENC5() {
        return dtemisouvenc5;
    }

    /**
     * Define o valor da propriedade dtemisouvenc5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTEMISOUVENC5(String value) {
        this.dtemisouvenc5 = value;
    }

    /**
     * Obt�m o valor da propriedade valor5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVALOR5() {
        return valor5;
    }

    /**
     * Define o valor da propriedade valor5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVALOR5(String value) {
        this.valor5 = value;
    }

    /**
     * Obt�m o valor da propriedade compet5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPET5() {
        return compet5;
    }

    /**
     * Define o valor da propriedade compet5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPET5(String value) {
        this.compet5 = value;
    }

    /**
     * Obt�m o valor da propriedade dtinicio5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTINICIO5() {
        return dtinicio5;
    }

    /**
     * Define o valor da propriedade dtinicio5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTINICIO5(String value) {
        this.dtinicio5 = value;
    }

    /**
     * Obt�m o valor da propriedade dtfim5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTFIM5() {
        return dtfim5;
    }

    /**
     * Define o valor da propriedade dtfim5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTFIM5(String value) {
        this.dtfim5 = value;
    }

    /**
     * Obt�m o valor da propriedade nrfatounf6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRFATOUNF6() {
        return nrfatounf6;
    }

    /**
     * Define o valor da propriedade nrfatounf6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRFATOUNF6(String value) {
        this.nrfatounf6 = value;
    }

    /**
     * Obt�m o valor da propriedade dtemisouvenc6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTEMISOUVENC6() {
        return dtemisouvenc6;
    }

    /**
     * Define o valor da propriedade dtemisouvenc6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTEMISOUVENC6(String value) {
        this.dtemisouvenc6 = value;
    }

    /**
     * Obt�m o valor da propriedade valor6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVALOR6() {
        return valor6;
    }

    /**
     * Define o valor da propriedade valor6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVALOR6(String value) {
        this.valor6 = value;
    }

    /**
     * Obt�m o valor da propriedade compet6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPET6() {
        return compet6;
    }

    /**
     * Define o valor da propriedade compet6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPET6(String value) {
        this.compet6 = value;
    }

    /**
     * Obt�m o valor da propriedade dtinicio6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTINICIO6() {
        return dtinicio6;
    }

    /**
     * Define o valor da propriedade dtinicio6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTINICIO6(String value) {
        this.dtinicio6 = value;
    }

    /**
     * Obt�m o valor da propriedade dtfim6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTFIM6() {
        return dtfim6;
    }

    /**
     * Define o valor da propriedade dtfim6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTFIM6(String value) {
        this.dtfim6 = value;
    }

    /**
     * Obt�m o valor da propriedade nrfatounf7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRFATOUNF7() {
        return nrfatounf7;
    }

    /**
     * Define o valor da propriedade nrfatounf7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRFATOUNF7(String value) {
        this.nrfatounf7 = value;
    }

    /**
     * Obt�m o valor da propriedade dtemisouvenc7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTEMISOUVENC7() {
        return dtemisouvenc7;
    }

    /**
     * Define o valor da propriedade dtemisouvenc7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTEMISOUVENC7(String value) {
        this.dtemisouvenc7 = value;
    }

    /**
     * Obt�m o valor da propriedade valor7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVALOR7() {
        return valor7;
    }

    /**
     * Define o valor da propriedade valor7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVALOR7(String value) {
        this.valor7 = value;
    }

    /**
     * Obt�m o valor da propriedade compet7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPET7() {
        return compet7;
    }

    /**
     * Define o valor da propriedade compet7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPET7(String value) {
        this.compet7 = value;
    }

    /**
     * Obt�m o valor da propriedade dtinicio7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTINICIO7() {
        return dtinicio7;
    }

    /**
     * Define o valor da propriedade dtinicio7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTINICIO7(String value) {
        this.dtinicio7 = value;
    }

    /**
     * Obt�m o valor da propriedade dtfim7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTFIM7() {
        return dtfim7;
    }

    /**
     * Define o valor da propriedade dtfim7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTFIM7(String value) {
        this.dtfim7 = value;
    }

    /**
     * Obt�m o valor da propriedade nrfatounf8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRFATOUNF8() {
        return nrfatounf8;
    }

    /**
     * Define o valor da propriedade nrfatounf8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRFATOUNF8(String value) {
        this.nrfatounf8 = value;
    }

    /**
     * Obt�m o valor da propriedade dtemisouvenc8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTEMISOUVENC8() {
        return dtemisouvenc8;
    }

    /**
     * Define o valor da propriedade dtemisouvenc8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTEMISOUVENC8(String value) {
        this.dtemisouvenc8 = value;
    }

    /**
     * Obt�m o valor da propriedade valor8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVALOR8() {
        return valor8;
    }

    /**
     * Define o valor da propriedade valor8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVALOR8(String value) {
        this.valor8 = value;
    }

    /**
     * Obt�m o valor da propriedade compet8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPET8() {
        return compet8;
    }

    /**
     * Define o valor da propriedade compet8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPET8(String value) {
        this.compet8 = value;
    }

    /**
     * Obt�m o valor da propriedade dtinicio8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTINICIO8() {
        return dtinicio8;
    }

    /**
     * Define o valor da propriedade dtinicio8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTINICIO8(String value) {
        this.dtinicio8 = value;
    }

    /**
     * Obt�m o valor da propriedade dtfim8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTFIM8() {
        return dtfim8;
    }

    /**
     * Define o valor da propriedade dtfim8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTFIM8(String value) {
        this.dtfim8 = value;
    }

    /**
     * Obt�m o valor da propriedade nrfatounf9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRFATOUNF9() {
        return nrfatounf9;
    }

    /**
     * Define o valor da propriedade nrfatounf9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRFATOUNF9(String value) {
        this.nrfatounf9 = value;
    }

    /**
     * Obt�m o valor da propriedade dtemisouvenc9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTEMISOUVENC9() {
        return dtemisouvenc9;
    }

    /**
     * Define o valor da propriedade dtemisouvenc9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTEMISOUVENC9(String value) {
        this.dtemisouvenc9 = value;
    }

    /**
     * Obt�m o valor da propriedade valor9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVALOR9() {
        return valor9;
    }

    /**
     * Define o valor da propriedade valor9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVALOR9(String value) {
        this.valor9 = value;
    }

    /**
     * Obt�m o valor da propriedade compet9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPET9() {
        return compet9;
    }

    /**
     * Define o valor da propriedade compet9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPET9(String value) {
        this.compet9 = value;
    }

    /**
     * Obt�m o valor da propriedade dtinicio9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTINICIO9() {
        return dtinicio9;
    }

    /**
     * Define o valor da propriedade dtinicio9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTINICIO9(String value) {
        this.dtinicio9 = value;
    }

    /**
     * Obt�m o valor da propriedade dtfim9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTFIM9() {
        return dtfim9;
    }

    /**
     * Define o valor da propriedade dtfim9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTFIM9(String value) {
        this.dtfim9 = value;
    }

    /**
     * Obt�m o valor da propriedade nrfatounf10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRFATOUNF10() {
        return nrfatounf10;
    }

    /**
     * Define o valor da propriedade nrfatounf10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRFATOUNF10(String value) {
        this.nrfatounf10 = value;
    }

    /**
     * Obt�m o valor da propriedade dtemisouvenc10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTEMISOUVENC10() {
        return dtemisouvenc10;
    }

    /**
     * Define o valor da propriedade dtemisouvenc10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTEMISOUVENC10(String value) {
        this.dtemisouvenc10 = value;
    }

    /**
     * Obt�m o valor da propriedade valor10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVALOR10() {
        return valor10;
    }

    /**
     * Define o valor da propriedade valor10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVALOR10(String value) {
        this.valor10 = value;
    }

    /**
     * Obt�m o valor da propriedade compet10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPET10() {
        return compet10;
    }

    /**
     * Define o valor da propriedade compet10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPET10(String value) {
        this.compet10 = value;
    }

    /**
     * Obt�m o valor da propriedade dtinicio10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTINICIO10() {
        return dtinicio10;
    }

    /**
     * Define o valor da propriedade dtinicio10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTINICIO10(String value) {
        this.dtinicio10 = value;
    }

    /**
     * Obt�m o valor da propriedade dtfim10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTFIM10() {
        return dtfim10;
    }

    /**
     * Define o valor da propriedade dtfim10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTFIM10(String value) {
        this.dtfim10 = value;
    }

    /**
     * Obt�m o valor da propriedade nrfatounf11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRFATOUNF11() {
        return nrfatounf11;
    }

    /**
     * Define o valor da propriedade nrfatounf11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRFATOUNF11(String value) {
        this.nrfatounf11 = value;
    }

    /**
     * Obt�m o valor da propriedade dtemisouvenc11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTEMISOUVENC11() {
        return dtemisouvenc11;
    }

    /**
     * Define o valor da propriedade dtemisouvenc11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTEMISOUVENC11(String value) {
        this.dtemisouvenc11 = value;
    }

    /**
     * Obt�m o valor da propriedade valor11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVALOR11() {
        return valor11;
    }

    /**
     * Define o valor da propriedade valor11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVALOR11(String value) {
        this.valor11 = value;
    }

    /**
     * Obt�m o valor da propriedade compet11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPET11() {
        return compet11;
    }

    /**
     * Define o valor da propriedade compet11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPET11(String value) {
        this.compet11 = value;
    }

    /**
     * Obt�m o valor da propriedade dtinicio11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTINICIO11() {
        return dtinicio11;
    }

    /**
     * Define o valor da propriedade dtinicio11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTINICIO11(String value) {
        this.dtinicio11 = value;
    }

    /**
     * Obt�m o valor da propriedade dtfim11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTFIM11() {
        return dtfim11;
    }

    /**
     * Define o valor da propriedade dtfim11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTFIM11(String value) {
        this.dtfim11 = value;
    }

    /**
     * Obt�m o valor da propriedade nrfatounf12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRFATOUNF12() {
        return nrfatounf12;
    }

    /**
     * Define o valor da propriedade nrfatounf12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRFATOUNF12(String value) {
        this.nrfatounf12 = value;
    }

    /**
     * Obt�m o valor da propriedade dtemisouvenc12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTEMISOUVENC12() {
        return dtemisouvenc12;
    }

    /**
     * Define o valor da propriedade dtemisouvenc12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTEMISOUVENC12(String value) {
        this.dtemisouvenc12 = value;
    }

    /**
     * Obt�m o valor da propriedade valor12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVALOR12() {
        return valor12;
    }

    /**
     * Define o valor da propriedade valor12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVALOR12(String value) {
        this.valor12 = value;
    }

    /**
     * Obt�m o valor da propriedade compet12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPET12() {
        return compet12;
    }

    /**
     * Define o valor da propriedade compet12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPET12(String value) {
        this.compet12 = value;
    }

    /**
     * Obt�m o valor da propriedade dtinicio12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTINICIO12() {
        return dtinicio12;
    }

    /**
     * Define o valor da propriedade dtinicio12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTINICIO12(String value) {
        this.dtinicio12 = value;
    }

    /**
     * Obt�m o valor da propriedade dtfim12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTFIM12() {
        return dtfim12;
    }

    /**
     * Define o valor da propriedade dtfim12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTFIM12(String value) {
        this.dtfim12 = value;
    }

    /**
     * Obt�m o valor da propriedade nrfatounf13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRFATOUNF13() {
        return nrfatounf13;
    }

    /**
     * Define o valor da propriedade nrfatounf13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRFATOUNF13(String value) {
        this.nrfatounf13 = value;
    }

    /**
     * Obt�m o valor da propriedade dtemisouvenc13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTEMISOUVENC13() {
        return dtemisouvenc13;
    }

    /**
     * Define o valor da propriedade dtemisouvenc13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTEMISOUVENC13(String value) {
        this.dtemisouvenc13 = value;
    }

    /**
     * Obt�m o valor da propriedade valor13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVALOR13() {
        return valor13;
    }

    /**
     * Define o valor da propriedade valor13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVALOR13(String value) {
        this.valor13 = value;
    }

    /**
     * Obt�m o valor da propriedade compet13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPET13() {
        return compet13;
    }

    /**
     * Define o valor da propriedade compet13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPET13(String value) {
        this.compet13 = value;
    }

    /**
     * Obt�m o valor da propriedade dtinicio13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTINICIO13() {
        return dtinicio13;
    }

    /**
     * Define o valor da propriedade dtinicio13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTINICIO13(String value) {
        this.dtinicio13 = value;
    }

    /**
     * Obt�m o valor da propriedade dtfim13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDTFIM13() {
        return dtfim13;
    }

    /**
     * Define o valor da propriedade dtfim13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDTFIM13(String value) {
        this.dtfim13 = value;
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
