
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
 *         &lt;element name="TTramitacaoDeDocumentos_WSResult" type="{http://pcrj/Sma/Sicop/Grava}Message" minOccurs="0"/>
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
    "tTramitacaoDeDocumentosWSResult"
})
@XmlRootElement(name = "TTramitacaoDeDocumentos_WSResponse")
public class TTramitacaoDeDocumentosWSResponse {

    @XmlElement(name = "TTramitacaoDeDocumentos_WSResult")
    protected Message tTramitacaoDeDocumentosWSResult;

    /**
     * Obt�m o valor da propriedade tTramitacaoDeDocumentosWSResult.
     * 
     * @return
     *     possible object is
     *     {@link Message }
     *     
     */
    public Message getTTramitacaoDeDocumentosWSResult() {
        return tTramitacaoDeDocumentosWSResult;
    }

    /**
     * Define o valor da propriedade tTramitacaoDeDocumentosWSResult.
     * 
     * @param value
     *     allowed object is
     *     {@link Message }
     *     
     */
    public void setTTramitacaoDeDocumentosWSResult(Message value) {
        this.tTramitacaoDeDocumentosWSResult = value;
    }

}