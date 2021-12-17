
package br.gov.jfrj.siga.integracao.ws.siafem;

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
 *         &lt;element name="MensagemResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "mensagemResult"
})
@XmlRootElement(name = "MensagemResponse")
public class MensagemResponse {

    @XmlElement(name = "MensagemResult")
    protected String mensagemResult;

    /**
     * Obt�m o valor da propriedade mensagemResult.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMensagemResult() {
        return mensagemResult;
    }

    /**
     * Define o valor da propriedade mensagemResult.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMensagemResult(String value) {
        this.mensagemResult = value;
    }

}
