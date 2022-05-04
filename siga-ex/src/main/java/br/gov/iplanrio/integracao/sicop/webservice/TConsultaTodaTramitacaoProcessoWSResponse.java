
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
 *         &lt;element name="TConsultaTodaTramitacaoProcesso_WSResult" type="{http://pcrj/Sma/Sicop/Grava}Message" minOccurs="0"/>
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
    "tConsultaTodaTramitacaoProcessoWSResult"
})
@XmlRootElement(name = "TConsultaTodaTramitacaoProcesso_WSResponse")
public class TConsultaTodaTramitacaoProcessoWSResponse {

    @XmlElement(name = "TConsultaTodaTramitacaoProcesso_WSResult")
    protected Message tConsultaTodaTramitacaoProcessoWSResult;

    /**
     * Obt�m o valor da propriedade tConsultaTodaTramitacaoProcessoWSResult.
     * 
     * @return
     *     possible object is
     *     {@link Message }
     *     
     */
    public Message getTConsultaTodaTramitacaoProcessoWSResult() {
        return tConsultaTodaTramitacaoProcessoWSResult;
    }

    /**
     * Define o valor da propriedade tConsultaTodaTramitacaoProcessoWSResult.
     * 
     * @param value
     *     allowed object is
     *     {@link Message }
     *     
     */
    public void setTConsultaTodaTramitacaoProcessoWSResult(Message value) {
        this.tConsultaTodaTramitacaoProcessoWSResult = value;
    }

}
