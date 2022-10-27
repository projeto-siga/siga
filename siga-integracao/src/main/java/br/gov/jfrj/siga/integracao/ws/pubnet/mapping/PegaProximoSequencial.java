
package br.gov.jfrj.siga.integracao.ws.pubnet.mapping;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de anonymous complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="retranca_codigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "retrancaCodigo"
})
@XmlRootElement(name = "pegaProximoSequencial")
public class PegaProximoSequencial {

    @XmlElement(name = "retranca_codigo")
    protected String retrancaCodigo;

    /**
     * Obtém o valor da propriedade retrancaCodigo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRetrancaCodigo() {
        return retrancaCodigo;
    }

    /**
     * Define o valor da propriedade retrancaCodigo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRetrancaCodigo(String value) {
        this.retrancaCodigo = value;
    }

}
