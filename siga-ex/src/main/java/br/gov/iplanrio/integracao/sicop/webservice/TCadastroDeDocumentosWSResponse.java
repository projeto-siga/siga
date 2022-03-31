
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
 *         &lt;element name="TCadastroDeDocumentos_WSResult" type="{http://pcrj/Sma/Sicop/Grava}Message" minOccurs="0"/>
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
    "tCadastroDeDocumentosWSResult"
})
@XmlRootElement(name = "TCadastroDeDocumentos_WSResponse")
public class TCadastroDeDocumentosWSResponse {

    @XmlElement(name = "TCadastroDeDocumentos_WSResult")
    protected Message tCadastroDeDocumentosWSResult;

    /**
     * Obt�m o valor da propriedade tCadastroDeDocumentosWSResult.
     * 
     * @return
     *     possible object is
     *     {@link Message }
     *     
     */
    public Message getTCadastroDeDocumentosWSResult() {
        return tCadastroDeDocumentosWSResult;
    }

    /**
     * Define o valor da propriedade tCadastroDeDocumentosWSResult.
     * 
     * @param value
     *     allowed object is
     *     {@link Message }
     *     
     */
    public void setTCadastroDeDocumentosWSResult(Message value) {
        this.tCadastroDeDocumentosWSResult = value;
    }

}
