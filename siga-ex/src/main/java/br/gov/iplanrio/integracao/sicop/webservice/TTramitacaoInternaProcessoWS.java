
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
 *         &lt;element name="NUMPROC1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CODDESP9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SENHA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "numproc1",
    "coddesp1",
    "despacho1",
    "numproc2",
    "coddesp2",
    "despacho2",
    "numproc3",
    "coddesp3",
    "despacho3",
    "numproc4",
    "coddesp4",
    "despacho4",
    "numproc5",
    "coddesp5",
    "despacho5",
    "numproc6",
    "coddesp6",
    "despacho6",
    "numproc7",
    "coddesp7",
    "despacho7",
    "numproc8",
    "coddesp8",
    "despacho8",
    "numproc9",
    "coddesp9",
    "despacho9",
    "senha"
})
@XmlRootElement(name = "TTramitacaoInternaProcesso_WS")
public class TTramitacaoInternaProcessoWS {

    @XmlElement(name = "ROTINA")
    protected String rotina;
    @XmlElement(name = "OPCAO")
    protected String opcao;
    @XmlElement(name = "NUMPROC1")
    protected String numproc1;
    @XmlElement(name = "CODDESP1")
    protected String coddesp1;
    @XmlElement(name = "DESPACHO1")
    protected String despacho1;
    @XmlElement(name = "NUMPROC2")
    protected String numproc2;
    @XmlElement(name = "CODDESP2")
    protected String coddesp2;
    @XmlElement(name = "DESPACHO2")
    protected String despacho2;
    @XmlElement(name = "NUMPROC3")
    protected String numproc3;
    @XmlElement(name = "CODDESP3")
    protected String coddesp3;
    @XmlElement(name = "DESPACHO3")
    protected String despacho3;
    @XmlElement(name = "NUMPROC4")
    protected String numproc4;
    @XmlElement(name = "CODDESP4")
    protected String coddesp4;
    @XmlElement(name = "DESPACHO4")
    protected String despacho4;
    @XmlElement(name = "NUMPROC5")
    protected String numproc5;
    @XmlElement(name = "CODDESP5")
    protected String coddesp5;
    @XmlElement(name = "DESPACHO5")
    protected String despacho5;
    @XmlElement(name = "NUMPROC6")
    protected String numproc6;
    @XmlElement(name = "CODDESP6")
    protected String coddesp6;
    @XmlElement(name = "DESPACHO6")
    protected String despacho6;
    @XmlElement(name = "NUMPROC7")
    protected String numproc7;
    @XmlElement(name = "CODDESP7")
    protected String coddesp7;
    @XmlElement(name = "DESPACHO7")
    protected String despacho7;
    @XmlElement(name = "NUMPROC8")
    protected String numproc8;
    @XmlElement(name = "CODDESP8")
    protected String coddesp8;
    @XmlElement(name = "DESPACHO8")
    protected String despacho8;
    @XmlElement(name = "NUMPROC9")
    protected String numproc9;
    @XmlElement(name = "CODDESP9")
    protected String coddesp9;
    @XmlElement(name = "DESPACHO9")
    protected String despacho9;
    @XmlElement(name = "SENHA")
    protected String senha;

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
     * Obt�m o valor da propriedade numproc1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC1() {
        return numproc1;
    }

    /**
     * Define o valor da propriedade numproc1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC1(String value) {
        this.numproc1 = value;
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
     * Obt�m o valor da propriedade despacho1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO1() {
        return despacho1;
    }

    /**
     * Define o valor da propriedade despacho1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO1(String value) {
        this.despacho1 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC2() {
        return numproc2;
    }

    /**
     * Define o valor da propriedade numproc2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC2(String value) {
        this.numproc2 = value;
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
     * Obt�m o valor da propriedade despacho2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO2() {
        return despacho2;
    }

    /**
     * Define o valor da propriedade despacho2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO2(String value) {
        this.despacho2 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC3() {
        return numproc3;
    }

    /**
     * Define o valor da propriedade numproc3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC3(String value) {
        this.numproc3 = value;
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
     * Obt�m o valor da propriedade despacho3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO3() {
        return despacho3;
    }

    /**
     * Define o valor da propriedade despacho3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO3(String value) {
        this.despacho3 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC4() {
        return numproc4;
    }

    /**
     * Define o valor da propriedade numproc4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC4(String value) {
        this.numproc4 = value;
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
     * Obt�m o valor da propriedade despacho4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO4() {
        return despacho4;
    }

    /**
     * Define o valor da propriedade despacho4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO4(String value) {
        this.despacho4 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC5() {
        return numproc5;
    }

    /**
     * Define o valor da propriedade numproc5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC5(String value) {
        this.numproc5 = value;
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
     * Obt�m o valor da propriedade despacho5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO5() {
        return despacho5;
    }

    /**
     * Define o valor da propriedade despacho5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO5(String value) {
        this.despacho5 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC6() {
        return numproc6;
    }

    /**
     * Define o valor da propriedade numproc6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC6(String value) {
        this.numproc6 = value;
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
     * Obt�m o valor da propriedade despacho6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO6() {
        return despacho6;
    }

    /**
     * Define o valor da propriedade despacho6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO6(String value) {
        this.despacho6 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC7() {
        return numproc7;
    }

    /**
     * Define o valor da propriedade numproc7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC7(String value) {
        this.numproc7 = value;
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
     * Obt�m o valor da propriedade despacho7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO7() {
        return despacho7;
    }

    /**
     * Define o valor da propriedade despacho7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO7(String value) {
        this.despacho7 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC8() {
        return numproc8;
    }

    /**
     * Define o valor da propriedade numproc8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC8(String value) {
        this.numproc8 = value;
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
     * Obt�m o valor da propriedade despacho8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO8() {
        return despacho8;
    }

    /**
     * Define o valor da propriedade despacho8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO8(String value) {
        this.despacho8 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC9() {
        return numproc9;
    }

    /**
     * Define o valor da propriedade numproc9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC9(String value) {
        this.numproc9 = value;
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
     * Obt�m o valor da propriedade despacho9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO9() {
        return despacho9;
    }

    /**
     * Define o valor da propriedade despacho9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO9(String value) {
        this.despacho9 = value;
    }

    /**
     * Obt�m o valor da propriedade senha.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSENHA() {
        return senha;
    }

    /**
     * Define o valor da propriedade senha.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSENHA(String value) {
        this.senha = value;
    }

}
