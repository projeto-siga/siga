
package br.gov.jfrj.siga.integracao.ws.pubnet.mapping;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="datainicio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="datafim" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "datainicio",
    "datafim"
})
@XmlRootElement(name = "consultaMaterialEnviado")
public class ConsultaMaterialEnviado {

    protected String datainicio;
    protected String datafim;

    /**
     * Obtém o valor da propriedade datainicio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatainicio() {
        return datainicio;
    }

    /**
     * Define o valor da propriedade datainicio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatainicio(String value) {
        this.datainicio = value;
    }

    /**
     * Obtém o valor da propriedade datafim.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatafim() {
        return datafim;
    }

    /**
     * Define o valor da propriedade datafim.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatafim(String value) {
        this.datafim = value;
    }

}
