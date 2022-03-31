
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
 *         &lt;element name="NROPROCESSO1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHAS1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPREV1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDO1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROLIVRO1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHA1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROPROCESSO2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHAS2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPREV2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDO2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROLIVRO2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHA2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROPROCESSO3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHAS3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPREV3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDO3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROLIVRO3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHA3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROPROCESSO4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHAS4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPREV4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDO4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROLIVRO4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHA4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROPROCESSO5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHAS5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPREV5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDO5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROLIVRO5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHA5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROPROCESSO6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHAS6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPREV6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDO6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROLIVRO6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHA6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROPROCESSO7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHAS7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPREV7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDO7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROLIVRO7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHA7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROPROCESSO8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHAS8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPREV8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDO8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROLIVRO8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHA8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROPROCESSO9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHAS9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPREV9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDO9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROLIVRO9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHA9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROPROCESSO10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHAS10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPREV10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDO10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROLIVRO10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHA10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROPROCESSO11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHAS11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPREV11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDO11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROLIVRO11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHA11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROPROCESSO12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHAS12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPREV12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDO12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROLIVRO12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHA12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROPROCESSO13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHAS13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPREV13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDO13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROLIVRO13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHA13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROPROCESSO14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHAS14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPREV14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDO14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROLIVRO14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHA14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROPROCESSO15" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHAS15" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPREV15" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDO15" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROLIVRO15" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NROFOLHA15" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INDCONFIRMA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "nroprocesso1",
    "nrofolhas1",
    "comprev1",
    "datdo1",
    "nrolivro1",
    "nrofolha1",
    "nroprocesso2",
    "nrofolhas2",
    "comprev2",
    "datdo2",
    "nrolivro2",
    "nrofolha2",
    "nroprocesso3",
    "nrofolhas3",
    "comprev3",
    "datdo3",
    "nrolivro3",
    "nrofolha3",
    "nroprocesso4",
    "nrofolhas4",
    "comprev4",
    "datdo4",
    "nrolivro4",
    "nrofolha4",
    "nroprocesso5",
    "nrofolhas5",
    "comprev5",
    "datdo5",
    "nrolivro5",
    "nrofolha5",
    "nroprocesso6",
    "nrofolhas6",
    "comprev6",
    "datdo6",
    "nrolivro6",
    "nrofolha6",
    "nroprocesso7",
    "nrofolhas7",
    "comprev7",
    "datdo7",
    "nrolivro7",
    "nrofolha7",
    "nroprocesso8",
    "nrofolhas8",
    "comprev8",
    "datdo8",
    "nrolivro8",
    "nrofolha8",
    "nroprocesso9",
    "nrofolhas9",
    "comprev9",
    "datdo9",
    "nrolivro9",
    "nrofolha9",
    "nroprocesso10",
    "nrofolhas10",
    "comprev10",
    "datdo10",
    "nrolivro10",
    "nrofolha10",
    "nroprocesso11",
    "nrofolhas11",
    "comprev11",
    "datdo11",
    "nrolivro11",
    "nrofolha11",
    "nroprocesso12",
    "nrofolhas12",
    "comprev12",
    "datdo12",
    "nrolivro12",
    "nrofolha12",
    "nroprocesso13",
    "nrofolhas13",
    "comprev13",
    "datdo13",
    "nrolivro13",
    "nrofolha13",
    "nroprocesso14",
    "nrofolhas14",
    "comprev14",
    "datdo14",
    "nrolivro14",
    "nrofolha14",
    "nroprocesso15",
    "nrofolhas15",
    "comprev15",
    "datdo15",
    "nrolivro15",
    "nrofolha15",
    "indconfirma"
})
@XmlRootElement(name = "TVinculacaoNumDeFolhasDoProcesso_WS")
public class TVinculacaoNumDeFolhasDoProcessoWS {

    @XmlElement(name = "ROTINA")
    protected String rotina;
    @XmlElement(name = "OPCAO")
    protected String opcao;
    @XmlElement(name = "NROPROCESSO1")
    protected String nroprocesso1;
    @XmlElement(name = "NROFOLHAS1")
    protected String nrofolhas1;
    @XmlElement(name = "COMPREV1")
    protected String comprev1;
    @XmlElement(name = "DATDO1")
    protected String datdo1;
    @XmlElement(name = "NROLIVRO1")
    protected String nrolivro1;
    @XmlElement(name = "NROFOLHA1")
    protected String nrofolha1;
    @XmlElement(name = "NROPROCESSO2")
    protected String nroprocesso2;
    @XmlElement(name = "NROFOLHAS2")
    protected String nrofolhas2;
    @XmlElement(name = "COMPREV2")
    protected String comprev2;
    @XmlElement(name = "DATDO2")
    protected String datdo2;
    @XmlElement(name = "NROLIVRO2")
    protected String nrolivro2;
    @XmlElement(name = "NROFOLHA2")
    protected String nrofolha2;
    @XmlElement(name = "NROPROCESSO3")
    protected String nroprocesso3;
    @XmlElement(name = "NROFOLHAS3")
    protected String nrofolhas3;
    @XmlElement(name = "COMPREV3")
    protected String comprev3;
    @XmlElement(name = "DATDO3")
    protected String datdo3;
    @XmlElement(name = "NROLIVRO3")
    protected String nrolivro3;
    @XmlElement(name = "NROFOLHA3")
    protected String nrofolha3;
    @XmlElement(name = "NROPROCESSO4")
    protected String nroprocesso4;
    @XmlElement(name = "NROFOLHAS4")
    protected String nrofolhas4;
    @XmlElement(name = "COMPREV4")
    protected String comprev4;
    @XmlElement(name = "DATDO4")
    protected String datdo4;
    @XmlElement(name = "NROLIVRO4")
    protected String nrolivro4;
    @XmlElement(name = "NROFOLHA4")
    protected String nrofolha4;
    @XmlElement(name = "NROPROCESSO5")
    protected String nroprocesso5;
    @XmlElement(name = "NROFOLHAS5")
    protected String nrofolhas5;
    @XmlElement(name = "COMPREV5")
    protected String comprev5;
    @XmlElement(name = "DATDO5")
    protected String datdo5;
    @XmlElement(name = "NROLIVRO5")
    protected String nrolivro5;
    @XmlElement(name = "NROFOLHA5")
    protected String nrofolha5;
    @XmlElement(name = "NROPROCESSO6")
    protected String nroprocesso6;
    @XmlElement(name = "NROFOLHAS6")
    protected String nrofolhas6;
    @XmlElement(name = "COMPREV6")
    protected String comprev6;
    @XmlElement(name = "DATDO6")
    protected String datdo6;
    @XmlElement(name = "NROLIVRO6")
    protected String nrolivro6;
    @XmlElement(name = "NROFOLHA6")
    protected String nrofolha6;
    @XmlElement(name = "NROPROCESSO7")
    protected String nroprocesso7;
    @XmlElement(name = "NROFOLHAS7")
    protected String nrofolhas7;
    @XmlElement(name = "COMPREV7")
    protected String comprev7;
    @XmlElement(name = "DATDO7")
    protected String datdo7;
    @XmlElement(name = "NROLIVRO7")
    protected String nrolivro7;
    @XmlElement(name = "NROFOLHA7")
    protected String nrofolha7;
    @XmlElement(name = "NROPROCESSO8")
    protected String nroprocesso8;
    @XmlElement(name = "NROFOLHAS8")
    protected String nrofolhas8;
    @XmlElement(name = "COMPREV8")
    protected String comprev8;
    @XmlElement(name = "DATDO8")
    protected String datdo8;
    @XmlElement(name = "NROLIVRO8")
    protected String nrolivro8;
    @XmlElement(name = "NROFOLHA8")
    protected String nrofolha8;
    @XmlElement(name = "NROPROCESSO9")
    protected String nroprocesso9;
    @XmlElement(name = "NROFOLHAS9")
    protected String nrofolhas9;
    @XmlElement(name = "COMPREV9")
    protected String comprev9;
    @XmlElement(name = "DATDO9")
    protected String datdo9;
    @XmlElement(name = "NROLIVRO9")
    protected String nrolivro9;
    @XmlElement(name = "NROFOLHA9")
    protected String nrofolha9;
    @XmlElement(name = "NROPROCESSO10")
    protected String nroprocesso10;
    @XmlElement(name = "NROFOLHAS10")
    protected String nrofolhas10;
    @XmlElement(name = "COMPREV10")
    protected String comprev10;
    @XmlElement(name = "DATDO10")
    protected String datdo10;
    @XmlElement(name = "NROLIVRO10")
    protected String nrolivro10;
    @XmlElement(name = "NROFOLHA10")
    protected String nrofolha10;
    @XmlElement(name = "NROPROCESSO11")
    protected String nroprocesso11;
    @XmlElement(name = "NROFOLHAS11")
    protected String nrofolhas11;
    @XmlElement(name = "COMPREV11")
    protected String comprev11;
    @XmlElement(name = "DATDO11")
    protected String datdo11;
    @XmlElement(name = "NROLIVRO11")
    protected String nrolivro11;
    @XmlElement(name = "NROFOLHA11")
    protected String nrofolha11;
    @XmlElement(name = "NROPROCESSO12")
    protected String nroprocesso12;
    @XmlElement(name = "NROFOLHAS12")
    protected String nrofolhas12;
    @XmlElement(name = "COMPREV12")
    protected String comprev12;
    @XmlElement(name = "DATDO12")
    protected String datdo12;
    @XmlElement(name = "NROLIVRO12")
    protected String nrolivro12;
    @XmlElement(name = "NROFOLHA12")
    protected String nrofolha12;
    @XmlElement(name = "NROPROCESSO13")
    protected String nroprocesso13;
    @XmlElement(name = "NROFOLHAS13")
    protected String nrofolhas13;
    @XmlElement(name = "COMPREV13")
    protected String comprev13;
    @XmlElement(name = "DATDO13")
    protected String datdo13;
    @XmlElement(name = "NROLIVRO13")
    protected String nrolivro13;
    @XmlElement(name = "NROFOLHA13")
    protected String nrofolha13;
    @XmlElement(name = "NROPROCESSO14")
    protected String nroprocesso14;
    @XmlElement(name = "NROFOLHAS14")
    protected String nrofolhas14;
    @XmlElement(name = "COMPREV14")
    protected String comprev14;
    @XmlElement(name = "DATDO14")
    protected String datdo14;
    @XmlElement(name = "NROLIVRO14")
    protected String nrolivro14;
    @XmlElement(name = "NROFOLHA14")
    protected String nrofolha14;
    @XmlElement(name = "NROPROCESSO15")
    protected String nroprocesso15;
    @XmlElement(name = "NROFOLHAS15")
    protected String nrofolhas15;
    @XmlElement(name = "COMPREV15")
    protected String comprev15;
    @XmlElement(name = "DATDO15")
    protected String datdo15;
    @XmlElement(name = "NROLIVRO15")
    protected String nrolivro15;
    @XmlElement(name = "NROFOLHA15")
    protected String nrofolha15;
    @XmlElement(name = "INDCONFIRMA")
    protected String indconfirma;

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
     * Obt�m o valor da propriedade nroprocesso1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROPROCESSO1() {
        return nroprocesso1;
    }

    /**
     * Define o valor da propriedade nroprocesso1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROPROCESSO1(String value) {
        this.nroprocesso1 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolhas1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHAS1() {
        return nrofolhas1;
    }

    /**
     * Define o valor da propriedade nrofolhas1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHAS1(String value) {
        this.nrofolhas1 = value;
    }

    /**
     * Obt�m o valor da propriedade comprev1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPREV1() {
        return comprev1;
    }

    /**
     * Define o valor da propriedade comprev1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPREV1(String value) {
        this.comprev1 = value;
    }

    /**
     * Obt�m o valor da propriedade datdo1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDO1() {
        return datdo1;
    }

    /**
     * Define o valor da propriedade datdo1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDO1(String value) {
        this.datdo1 = value;
    }

    /**
     * Obt�m o valor da propriedade nrolivro1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROLIVRO1() {
        return nrolivro1;
    }

    /**
     * Define o valor da propriedade nrolivro1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROLIVRO1(String value) {
        this.nrolivro1 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolha1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHA1() {
        return nrofolha1;
    }

    /**
     * Define o valor da propriedade nrofolha1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHA1(String value) {
        this.nrofolha1 = value;
    }

    /**
     * Obt�m o valor da propriedade nroprocesso2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROPROCESSO2() {
        return nroprocesso2;
    }

    /**
     * Define o valor da propriedade nroprocesso2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROPROCESSO2(String value) {
        this.nroprocesso2 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolhas2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHAS2() {
        return nrofolhas2;
    }

    /**
     * Define o valor da propriedade nrofolhas2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHAS2(String value) {
        this.nrofolhas2 = value;
    }

    /**
     * Obt�m o valor da propriedade comprev2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPREV2() {
        return comprev2;
    }

    /**
     * Define o valor da propriedade comprev2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPREV2(String value) {
        this.comprev2 = value;
    }

    /**
     * Obt�m o valor da propriedade datdo2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDO2() {
        return datdo2;
    }

    /**
     * Define o valor da propriedade datdo2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDO2(String value) {
        this.datdo2 = value;
    }

    /**
     * Obt�m o valor da propriedade nrolivro2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROLIVRO2() {
        return nrolivro2;
    }

    /**
     * Define o valor da propriedade nrolivro2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROLIVRO2(String value) {
        this.nrolivro2 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolha2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHA2() {
        return nrofolha2;
    }

    /**
     * Define o valor da propriedade nrofolha2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHA2(String value) {
        this.nrofolha2 = value;
    }

    /**
     * Obt�m o valor da propriedade nroprocesso3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROPROCESSO3() {
        return nroprocesso3;
    }

    /**
     * Define o valor da propriedade nroprocesso3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROPROCESSO3(String value) {
        this.nroprocesso3 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolhas3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHAS3() {
        return nrofolhas3;
    }

    /**
     * Define o valor da propriedade nrofolhas3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHAS3(String value) {
        this.nrofolhas3 = value;
    }

    /**
     * Obt�m o valor da propriedade comprev3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPREV3() {
        return comprev3;
    }

    /**
     * Define o valor da propriedade comprev3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPREV3(String value) {
        this.comprev3 = value;
    }

    /**
     * Obt�m o valor da propriedade datdo3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDO3() {
        return datdo3;
    }

    /**
     * Define o valor da propriedade datdo3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDO3(String value) {
        this.datdo3 = value;
    }

    /**
     * Obt�m o valor da propriedade nrolivro3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROLIVRO3() {
        return nrolivro3;
    }

    /**
     * Define o valor da propriedade nrolivro3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROLIVRO3(String value) {
        this.nrolivro3 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolha3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHA3() {
        return nrofolha3;
    }

    /**
     * Define o valor da propriedade nrofolha3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHA3(String value) {
        this.nrofolha3 = value;
    }

    /**
     * Obt�m o valor da propriedade nroprocesso4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROPROCESSO4() {
        return nroprocesso4;
    }

    /**
     * Define o valor da propriedade nroprocesso4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROPROCESSO4(String value) {
        this.nroprocesso4 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolhas4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHAS4() {
        return nrofolhas4;
    }

    /**
     * Define o valor da propriedade nrofolhas4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHAS4(String value) {
        this.nrofolhas4 = value;
    }

    /**
     * Obt�m o valor da propriedade comprev4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPREV4() {
        return comprev4;
    }

    /**
     * Define o valor da propriedade comprev4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPREV4(String value) {
        this.comprev4 = value;
    }

    /**
     * Obt�m o valor da propriedade datdo4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDO4() {
        return datdo4;
    }

    /**
     * Define o valor da propriedade datdo4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDO4(String value) {
        this.datdo4 = value;
    }

    /**
     * Obt�m o valor da propriedade nrolivro4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROLIVRO4() {
        return nrolivro4;
    }

    /**
     * Define o valor da propriedade nrolivro4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROLIVRO4(String value) {
        this.nrolivro4 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolha4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHA4() {
        return nrofolha4;
    }

    /**
     * Define o valor da propriedade nrofolha4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHA4(String value) {
        this.nrofolha4 = value;
    }

    /**
     * Obt�m o valor da propriedade nroprocesso5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROPROCESSO5() {
        return nroprocesso5;
    }

    /**
     * Define o valor da propriedade nroprocesso5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROPROCESSO5(String value) {
        this.nroprocesso5 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolhas5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHAS5() {
        return nrofolhas5;
    }

    /**
     * Define o valor da propriedade nrofolhas5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHAS5(String value) {
        this.nrofolhas5 = value;
    }

    /**
     * Obt�m o valor da propriedade comprev5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPREV5() {
        return comprev5;
    }

    /**
     * Define o valor da propriedade comprev5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPREV5(String value) {
        this.comprev5 = value;
    }

    /**
     * Obt�m o valor da propriedade datdo5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDO5() {
        return datdo5;
    }

    /**
     * Define o valor da propriedade datdo5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDO5(String value) {
        this.datdo5 = value;
    }

    /**
     * Obt�m o valor da propriedade nrolivro5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROLIVRO5() {
        return nrolivro5;
    }

    /**
     * Define o valor da propriedade nrolivro5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROLIVRO5(String value) {
        this.nrolivro5 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolha5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHA5() {
        return nrofolha5;
    }

    /**
     * Define o valor da propriedade nrofolha5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHA5(String value) {
        this.nrofolha5 = value;
    }

    /**
     * Obt�m o valor da propriedade nroprocesso6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROPROCESSO6() {
        return nroprocesso6;
    }

    /**
     * Define o valor da propriedade nroprocesso6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROPROCESSO6(String value) {
        this.nroprocesso6 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolhas6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHAS6() {
        return nrofolhas6;
    }

    /**
     * Define o valor da propriedade nrofolhas6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHAS6(String value) {
        this.nrofolhas6 = value;
    }

    /**
     * Obt�m o valor da propriedade comprev6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPREV6() {
        return comprev6;
    }

    /**
     * Define o valor da propriedade comprev6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPREV6(String value) {
        this.comprev6 = value;
    }

    /**
     * Obt�m o valor da propriedade datdo6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDO6() {
        return datdo6;
    }

    /**
     * Define o valor da propriedade datdo6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDO6(String value) {
        this.datdo6 = value;
    }

    /**
     * Obt�m o valor da propriedade nrolivro6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROLIVRO6() {
        return nrolivro6;
    }

    /**
     * Define o valor da propriedade nrolivro6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROLIVRO6(String value) {
        this.nrolivro6 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolha6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHA6() {
        return nrofolha6;
    }

    /**
     * Define o valor da propriedade nrofolha6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHA6(String value) {
        this.nrofolha6 = value;
    }

    /**
     * Obt�m o valor da propriedade nroprocesso7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROPROCESSO7() {
        return nroprocesso7;
    }

    /**
     * Define o valor da propriedade nroprocesso7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROPROCESSO7(String value) {
        this.nroprocesso7 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolhas7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHAS7() {
        return nrofolhas7;
    }

    /**
     * Define o valor da propriedade nrofolhas7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHAS7(String value) {
        this.nrofolhas7 = value;
    }

    /**
     * Obt�m o valor da propriedade comprev7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPREV7() {
        return comprev7;
    }

    /**
     * Define o valor da propriedade comprev7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPREV7(String value) {
        this.comprev7 = value;
    }

    /**
     * Obt�m o valor da propriedade datdo7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDO7() {
        return datdo7;
    }

    /**
     * Define o valor da propriedade datdo7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDO7(String value) {
        this.datdo7 = value;
    }

    /**
     * Obt�m o valor da propriedade nrolivro7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROLIVRO7() {
        return nrolivro7;
    }

    /**
     * Define o valor da propriedade nrolivro7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROLIVRO7(String value) {
        this.nrolivro7 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolha7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHA7() {
        return nrofolha7;
    }

    /**
     * Define o valor da propriedade nrofolha7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHA7(String value) {
        this.nrofolha7 = value;
    }

    /**
     * Obt�m o valor da propriedade nroprocesso8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROPROCESSO8() {
        return nroprocesso8;
    }

    /**
     * Define o valor da propriedade nroprocesso8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROPROCESSO8(String value) {
        this.nroprocesso8 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolhas8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHAS8() {
        return nrofolhas8;
    }

    /**
     * Define o valor da propriedade nrofolhas8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHAS8(String value) {
        this.nrofolhas8 = value;
    }

    /**
     * Obt�m o valor da propriedade comprev8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPREV8() {
        return comprev8;
    }

    /**
     * Define o valor da propriedade comprev8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPREV8(String value) {
        this.comprev8 = value;
    }

    /**
     * Obt�m o valor da propriedade datdo8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDO8() {
        return datdo8;
    }

    /**
     * Define o valor da propriedade datdo8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDO8(String value) {
        this.datdo8 = value;
    }

    /**
     * Obt�m o valor da propriedade nrolivro8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROLIVRO8() {
        return nrolivro8;
    }

    /**
     * Define o valor da propriedade nrolivro8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROLIVRO8(String value) {
        this.nrolivro8 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolha8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHA8() {
        return nrofolha8;
    }

    /**
     * Define o valor da propriedade nrofolha8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHA8(String value) {
        this.nrofolha8 = value;
    }

    /**
     * Obt�m o valor da propriedade nroprocesso9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROPROCESSO9() {
        return nroprocesso9;
    }

    /**
     * Define o valor da propriedade nroprocesso9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROPROCESSO9(String value) {
        this.nroprocesso9 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolhas9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHAS9() {
        return nrofolhas9;
    }

    /**
     * Define o valor da propriedade nrofolhas9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHAS9(String value) {
        this.nrofolhas9 = value;
    }

    /**
     * Obt�m o valor da propriedade comprev9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPREV9() {
        return comprev9;
    }

    /**
     * Define o valor da propriedade comprev9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPREV9(String value) {
        this.comprev9 = value;
    }

    /**
     * Obt�m o valor da propriedade datdo9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDO9() {
        return datdo9;
    }

    /**
     * Define o valor da propriedade datdo9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDO9(String value) {
        this.datdo9 = value;
    }

    /**
     * Obt�m o valor da propriedade nrolivro9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROLIVRO9() {
        return nrolivro9;
    }

    /**
     * Define o valor da propriedade nrolivro9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROLIVRO9(String value) {
        this.nrolivro9 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolha9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHA9() {
        return nrofolha9;
    }

    /**
     * Define o valor da propriedade nrofolha9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHA9(String value) {
        this.nrofolha9 = value;
    }

    /**
     * Obt�m o valor da propriedade nroprocesso10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROPROCESSO10() {
        return nroprocesso10;
    }

    /**
     * Define o valor da propriedade nroprocesso10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROPROCESSO10(String value) {
        this.nroprocesso10 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolhas10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHAS10() {
        return nrofolhas10;
    }

    /**
     * Define o valor da propriedade nrofolhas10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHAS10(String value) {
        this.nrofolhas10 = value;
    }

    /**
     * Obt�m o valor da propriedade comprev10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPREV10() {
        return comprev10;
    }

    /**
     * Define o valor da propriedade comprev10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPREV10(String value) {
        this.comprev10 = value;
    }

    /**
     * Obt�m o valor da propriedade datdo10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDO10() {
        return datdo10;
    }

    /**
     * Define o valor da propriedade datdo10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDO10(String value) {
        this.datdo10 = value;
    }

    /**
     * Obt�m o valor da propriedade nrolivro10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROLIVRO10() {
        return nrolivro10;
    }

    /**
     * Define o valor da propriedade nrolivro10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROLIVRO10(String value) {
        this.nrolivro10 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolha10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHA10() {
        return nrofolha10;
    }

    /**
     * Define o valor da propriedade nrofolha10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHA10(String value) {
        this.nrofolha10 = value;
    }

    /**
     * Obt�m o valor da propriedade nroprocesso11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROPROCESSO11() {
        return nroprocesso11;
    }

    /**
     * Define o valor da propriedade nroprocesso11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROPROCESSO11(String value) {
        this.nroprocesso11 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolhas11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHAS11() {
        return nrofolhas11;
    }

    /**
     * Define o valor da propriedade nrofolhas11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHAS11(String value) {
        this.nrofolhas11 = value;
    }

    /**
     * Obt�m o valor da propriedade comprev11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPREV11() {
        return comprev11;
    }

    /**
     * Define o valor da propriedade comprev11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPREV11(String value) {
        this.comprev11 = value;
    }

    /**
     * Obt�m o valor da propriedade datdo11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDO11() {
        return datdo11;
    }

    /**
     * Define o valor da propriedade datdo11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDO11(String value) {
        this.datdo11 = value;
    }

    /**
     * Obt�m o valor da propriedade nrolivro11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROLIVRO11() {
        return nrolivro11;
    }

    /**
     * Define o valor da propriedade nrolivro11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROLIVRO11(String value) {
        this.nrolivro11 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolha11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHA11() {
        return nrofolha11;
    }

    /**
     * Define o valor da propriedade nrofolha11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHA11(String value) {
        this.nrofolha11 = value;
    }

    /**
     * Obt�m o valor da propriedade nroprocesso12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROPROCESSO12() {
        return nroprocesso12;
    }

    /**
     * Define o valor da propriedade nroprocesso12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROPROCESSO12(String value) {
        this.nroprocesso12 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolhas12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHAS12() {
        return nrofolhas12;
    }

    /**
     * Define o valor da propriedade nrofolhas12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHAS12(String value) {
        this.nrofolhas12 = value;
    }

    /**
     * Obt�m o valor da propriedade comprev12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPREV12() {
        return comprev12;
    }

    /**
     * Define o valor da propriedade comprev12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPREV12(String value) {
        this.comprev12 = value;
    }

    /**
     * Obt�m o valor da propriedade datdo12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDO12() {
        return datdo12;
    }

    /**
     * Define o valor da propriedade datdo12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDO12(String value) {
        this.datdo12 = value;
    }

    /**
     * Obt�m o valor da propriedade nrolivro12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROLIVRO12() {
        return nrolivro12;
    }

    /**
     * Define o valor da propriedade nrolivro12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROLIVRO12(String value) {
        this.nrolivro12 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolha12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHA12() {
        return nrofolha12;
    }

    /**
     * Define o valor da propriedade nrofolha12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHA12(String value) {
        this.nrofolha12 = value;
    }

    /**
     * Obt�m o valor da propriedade nroprocesso13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROPROCESSO13() {
        return nroprocesso13;
    }

    /**
     * Define o valor da propriedade nroprocesso13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROPROCESSO13(String value) {
        this.nroprocesso13 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolhas13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHAS13() {
        return nrofolhas13;
    }

    /**
     * Define o valor da propriedade nrofolhas13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHAS13(String value) {
        this.nrofolhas13 = value;
    }

    /**
     * Obt�m o valor da propriedade comprev13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPREV13() {
        return comprev13;
    }

    /**
     * Define o valor da propriedade comprev13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPREV13(String value) {
        this.comprev13 = value;
    }

    /**
     * Obt�m o valor da propriedade datdo13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDO13() {
        return datdo13;
    }

    /**
     * Define o valor da propriedade datdo13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDO13(String value) {
        this.datdo13 = value;
    }

    /**
     * Obt�m o valor da propriedade nrolivro13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROLIVRO13() {
        return nrolivro13;
    }

    /**
     * Define o valor da propriedade nrolivro13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROLIVRO13(String value) {
        this.nrolivro13 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolha13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHA13() {
        return nrofolha13;
    }

    /**
     * Define o valor da propriedade nrofolha13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHA13(String value) {
        this.nrofolha13 = value;
    }

    /**
     * Obt�m o valor da propriedade nroprocesso14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROPROCESSO14() {
        return nroprocesso14;
    }

    /**
     * Define o valor da propriedade nroprocesso14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROPROCESSO14(String value) {
        this.nroprocesso14 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolhas14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHAS14() {
        return nrofolhas14;
    }

    /**
     * Define o valor da propriedade nrofolhas14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHAS14(String value) {
        this.nrofolhas14 = value;
    }

    /**
     * Obt�m o valor da propriedade comprev14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPREV14() {
        return comprev14;
    }

    /**
     * Define o valor da propriedade comprev14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPREV14(String value) {
        this.comprev14 = value;
    }

    /**
     * Obt�m o valor da propriedade datdo14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDO14() {
        return datdo14;
    }

    /**
     * Define o valor da propriedade datdo14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDO14(String value) {
        this.datdo14 = value;
    }

    /**
     * Obt�m o valor da propriedade nrolivro14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROLIVRO14() {
        return nrolivro14;
    }

    /**
     * Define o valor da propriedade nrolivro14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROLIVRO14(String value) {
        this.nrolivro14 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolha14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHA14() {
        return nrofolha14;
    }

    /**
     * Define o valor da propriedade nrofolha14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHA14(String value) {
        this.nrofolha14 = value;
    }

    /**
     * Obt�m o valor da propriedade nroprocesso15.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROPROCESSO15() {
        return nroprocesso15;
    }

    /**
     * Define o valor da propriedade nroprocesso15.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROPROCESSO15(String value) {
        this.nroprocesso15 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolhas15.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHAS15() {
        return nrofolhas15;
    }

    /**
     * Define o valor da propriedade nrofolhas15.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHAS15(String value) {
        this.nrofolhas15 = value;
    }

    /**
     * Obt�m o valor da propriedade comprev15.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPREV15() {
        return comprev15;
    }

    /**
     * Define o valor da propriedade comprev15.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPREV15(String value) {
        this.comprev15 = value;
    }

    /**
     * Obt�m o valor da propriedade datdo15.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDO15() {
        return datdo15;
    }

    /**
     * Define o valor da propriedade datdo15.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDO15(String value) {
        this.datdo15 = value;
    }

    /**
     * Obt�m o valor da propriedade nrolivro15.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROLIVRO15() {
        return nrolivro15;
    }

    /**
     * Define o valor da propriedade nrolivro15.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROLIVRO15(String value) {
        this.nrolivro15 = value;
    }

    /**
     * Obt�m o valor da propriedade nrofolha15.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNROFOLHA15() {
        return nrofolha15;
    }

    /**
     * Define o valor da propriedade nrofolha15.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNROFOLHA15(String value) {
        this.nrofolha15 = value;
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

}
