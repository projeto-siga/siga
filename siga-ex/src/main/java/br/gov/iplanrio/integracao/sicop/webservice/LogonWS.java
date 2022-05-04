
package br.gov.iplanrio.integracao.sicop.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de Logon_WS complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="Logon_WS">
 *   &lt;complexContent>
 *     &lt;extension base="{http://pcrj/Sma/Sicop/Grava}Message">
 *       &lt;sequence>
 *         &lt;element name="USERCODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SENHA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MSG1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MSG2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "Logon_WS", propOrder = {
    "usercode",
    "senha",
    "msg1",
    "msg2",
    "statusLine"
})
public class LogonWS
    extends Message
{

    @XmlElement(name = "USERCODE")
    protected String usercode;
    @XmlElement(name = "SENHA")
    protected String senha;
    @XmlElement(name = "MSG1")
    protected String msg1;
    @XmlElement(name = "MSG2")
    protected String msg2;
    @XmlElement(name = "StatusLine")
    protected String statusLine;

    /**
     * Obt�m o valor da propriedade usercode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSERCODE() {
        return usercode;
    }

    /**
     * Define o valor da propriedade usercode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSERCODE(String value) {
        this.usercode = value;
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

    /**
     * Obt�m o valor da propriedade msg1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMSG1() {
        return msg1;
    }

    /**
     * Define o valor da propriedade msg1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMSG1(String value) {
        this.msg1 = value;
    }

    /**
     * Obt�m o valor da propriedade msg2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMSG2() {
        return msg2;
    }

    /**
     * Define o valor da propriedade msg2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMSG2(String value) {
        this.msg2 = value;
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
