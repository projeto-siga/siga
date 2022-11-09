
package br.gov.jfrj.siga.integracao.ws.pubnet.xmlschema;

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
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}annotated"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="restriction" type="{http://www.w3.org/2001/XMLSchema}simpleRestrictionType"/&gt;
 *         &lt;element name="extension" type="{http://www.w3.org/2001/XMLSchema}simpleExtensionType"/&gt;
 *       &lt;/choice&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "restriction",
    "extension"
})
@XmlRootElement(name = "simpleContent")
public class SimpleContent
    extends Annotated
{

    protected SimpleRestrictionType restriction;
    protected SimpleExtensionType extension;

    /**
     * Obtém o valor da propriedade restriction.
     * 
     * @return
     *     possible object is
     *     {@link SimpleRestrictionType }
     *     
     */
    public SimpleRestrictionType getRestriction() {
        return restriction;
    }

    /**
     * Define o valor da propriedade restriction.
     * 
     * @param value
     *     allowed object is
     *     {@link SimpleRestrictionType }
     *     
     */
    public void setRestriction(SimpleRestrictionType value) {
        this.restriction = value;
    }

    /**
     * Obtém o valor da propriedade extension.
     * 
     * @return
     *     possible object is
     *     {@link SimpleExtensionType }
     *     
     */
    public SimpleExtensionType getExtension() {
        return extension;
    }

    /**
     * Define o valor da propriedade extension.
     * 
     * @param value
     *     allowed object is
     *     {@link SimpleExtensionType }
     *     
     */
    public void setExtension(SimpleExtensionType value) {
        this.extension = value;
    }

}
