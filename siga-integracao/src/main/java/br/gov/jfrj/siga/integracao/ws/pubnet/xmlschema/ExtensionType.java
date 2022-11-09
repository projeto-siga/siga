
package br.gov.jfrj.siga.integracao.ws.pubnet.xmlschema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * <p>Classe Java de extensionType complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="extensionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}annotated"&gt;
 *       &lt;sequence&gt;
 *         &lt;group ref="{http://www.w3.org/2001/XMLSchema}typeDefParticle" minOccurs="0"/&gt;
 *         &lt;group ref="{http://www.w3.org/2001/XMLSchema}attrDecls"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="base" use="required" type="{http://www.w3.org/2001/XMLSchema}QName" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "extensionType", propOrder = {
    "group",
    "all",
    "choice",
    "sequence",
    "attributeOrAttributeGroup",
    "anyAttribute"
})
@XmlSeeAlso({
    SimpleExtensionType.class
})
public class ExtensionType
    extends Annotated
{

    protected GroupRef group;
    protected All all;
    protected ExplicitGroup choice;
    protected ExplicitGroup sequence;
    @XmlElements({
        @XmlElement(name = "attribute", type = Attribute.class),
        @XmlElement(name = "attributeGroup", type = AttributeGroupRef.class)
    })
    protected List<Annotated> attributeOrAttributeGroup;
    protected Wildcard anyAttribute;
    @XmlAttribute(name = "base", required = true)
    protected QName base;

    /**
     * Obtém o valor da propriedade group.
     * 
     * @return
     *     possible object is
     *     {@link GroupRef }
     *     
     */
    public GroupRef getGroup() {
        return group;
    }

    /**
     * Define o valor da propriedade group.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupRef }
     *     
     */
    public void setGroup(GroupRef value) {
        this.group = value;
    }

    /**
     * Obtém o valor da propriedade all.
     * 
     * @return
     *     possible object is
     *     {@link All }
     *     
     */
    public All getAll() {
        return all;
    }

    /**
     * Define o valor da propriedade all.
     * 
     * @param value
     *     allowed object is
     *     {@link All }
     *     
     */
    public void setAll(All value) {
        this.all = value;
    }

    /**
     * Obtém o valor da propriedade choice.
     * 
     * @return
     *     possible object is
     *     {@link ExplicitGroup }
     *     
     */
    public ExplicitGroup getChoice() {
        return choice;
    }

    /**
     * Define o valor da propriedade choice.
     * 
     * @param value
     *     allowed object is
     *     {@link ExplicitGroup }
     *     
     */
    public void setChoice(ExplicitGroup value) {
        this.choice = value;
    }

    /**
     * Obtém o valor da propriedade sequence.
     * 
     * @return
     *     possible object is
     *     {@link ExplicitGroup }
     *     
     */
    public ExplicitGroup getSequence() {
        return sequence;
    }

    /**
     * Define o valor da propriedade sequence.
     * 
     * @param value
     *     allowed object is
     *     {@link ExplicitGroup }
     *     
     */
    public void setSequence(ExplicitGroup value) {
        this.sequence = value;
    }

    /**
     * Gets the value of the attributeOrAttributeGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attributeOrAttributeGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttributeOrAttributeGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Attribute }
     * {@link AttributeGroupRef }
     * 
     * 
     */
    public List<Annotated> getAttributeOrAttributeGroup() {
        if (attributeOrAttributeGroup == null) {
            attributeOrAttributeGroup = new ArrayList<Annotated>();
        }
        return this.attributeOrAttributeGroup;
    }

    /**
     * Obtém o valor da propriedade anyAttribute.
     * 
     * @return
     *     possible object is
     *     {@link Wildcard }
     *     
     */
    public Wildcard getAnyAttribute() {
        return anyAttribute;
    }

    /**
     * Define o valor da propriedade anyAttribute.
     * 
     * @param value
     *     allowed object is
     *     {@link Wildcard }
     *     
     */
    public void setAnyAttribute(Wildcard value) {
        this.anyAttribute = value;
    }

    /**
     * Obtém o valor da propriedade base.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getBase() {
        return base;
    }

    /**
     * Define o valor da propriedade base.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setBase(QName value) {
        this.base = value;
    }

}
