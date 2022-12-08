
package br.gov.jfrj.siga.integracao.ws.pubnet.mapping;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import br.gov.jfrj.siga.integracao.ws.pubnet.xmlschema.Schema;


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
 *         &lt;element name="montaReciboPublicacaoCancelamentoResult" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element ref="{http://www.w3.org/2001/XMLSchema}schema"/&gt;
 *                   &lt;any/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
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
    "montaReciboPublicacaoCancelamentoResult"
})
@XmlRootElement(name = "montaReciboPublicacaoCancelamentoResponse")
public class MontaReciboPublicacaoCancelamentoResponse {

    protected MontaReciboPublicacaoCancelamentoResponse.MontaReciboPublicacaoCancelamentoResult montaReciboPublicacaoCancelamentoResult;

    /**
     * Obtém o valor da propriedade montaReciboPublicacaoCancelamentoResult.
     * 
     * @return
     *     possible object is
     *     {@link MontaReciboPublicacaoCancelamentoResponse.MontaReciboPublicacaoCancelamentoResult }
     *     
     */
    public MontaReciboPublicacaoCancelamentoResponse.MontaReciboPublicacaoCancelamentoResult getMontaReciboPublicacaoCancelamentoResult() {
        return montaReciboPublicacaoCancelamentoResult;
    }

    /**
     * Define o valor da propriedade montaReciboPublicacaoCancelamentoResult.
     * 
     * @param value
     *     allowed object is
     *     {@link MontaReciboPublicacaoCancelamentoResponse.MontaReciboPublicacaoCancelamentoResult }
     *     
     */
    public void setMontaReciboPublicacaoCancelamentoResult(MontaReciboPublicacaoCancelamentoResponse.MontaReciboPublicacaoCancelamentoResult value) {
        this.montaReciboPublicacaoCancelamentoResult = value;
    }


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
     *         &lt;element ref="{http://www.w3.org/2001/XMLSchema}schema"/&gt;
     *         &lt;any/&gt;
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
        "schema",
        "any"
    })
    public static class MontaReciboPublicacaoCancelamentoResult {

        @XmlElement(namespace = "http://www.w3.org/2001/XMLSchema", required = true)
        protected Schema schema;
        @XmlAnyElement(lax = true)
        protected Object any;

        /**
         * Obtém o valor da propriedade schema.
         * 
         * @return
         *     possible object is
         *     {@link Schema }
         *     
         */
        public Schema getSchema() {
            return schema;
        }

        /**
         * Define o valor da propriedade schema.
         * 
         * @param value
         *     allowed object is
         *     {@link Schema }
         *     
         */
        public void setSchema(Schema value) {
            this.schema = value;
        }

        /**
         * Obtém o valor da propriedade any.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getAny() {
            return any;
        }

        /**
         * Define o valor da propriedade any.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setAny(Object value) {
            this.any = value;
        }

    }

}
