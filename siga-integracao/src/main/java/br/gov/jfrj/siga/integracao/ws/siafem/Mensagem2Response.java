
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
 *         &lt;element name="Mensagem2Result" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "mensagem2Result"
})
@XmlRootElement(name = "Mensagem2Response")
public class Mensagem2Response {

    @XmlElement(name = "Mensagem2Result")
    protected String mensagem2Result;

    /**
     * Obt�m o valor da propriedade mensagem2Result.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMensagem2Result() {
        return mensagem2Result;
    }

    /**
     * Define o valor da propriedade mensagem2Result.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMensagem2Result(String value) {
        this.mensagem2Result = value;
    }

}
