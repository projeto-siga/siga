
package br.gov.jfrj.siga.integracao.ws.pubnet.xmlschema;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * 
 *    group type for explicit groups, named top-level groups and
 *    group references
 * 
 * <p>Classe Java de group complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="group"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}annotated"&gt;
 *       &lt;group ref="{http://www.w3.org/2001/XMLSchema}particle" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;attGroup ref="{http://www.w3.org/2001/XMLSchema}occurs"/&gt;
 *       &lt;attGroup ref="{http://www.w3.org/2001/XMLSchema}defRef"/&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "group", propOrder = {
    "particle"
})
@XmlSeeAlso({
    RealGroup.class,
    ExplicitGroup.class
})
public abstract class Group
    extends Annotated
{

    @XmlElementRefs({
        @XmlElementRef(name = "group", namespace = "http://www.w3.org/2001/XMLSchema", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "any", namespace = "http://www.w3.org/2001/XMLSchema", type = Any.class, required = false),
        @XmlElementRef(name = "choice", namespace = "http://www.w3.org/2001/XMLSchema", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "sequence", namespace = "http://www.w3.org/2001/XMLSchema", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "all", namespace = "http://www.w3.org/2001/XMLSchema", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "element", namespace = "http://www.w3.org/2001/XMLSchema", type = JAXBElement.class, required = false)
    })
    protected List<Object> particle;
    @XmlAttribute(name = "minOccurs")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger minOccurs;
    @XmlAttribute(name = "maxOccurs")
    @XmlSchemaType(name = "allNNI")
    protected String maxOccurs;
    @XmlAttribute(name = "name")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String name;
    @XmlAttribute(name = "ref")
    protected QName ref;

    /**
     * Gets the value of the particle property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the particle property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParticle().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link GroupRef }{@code >}
     * {@link JAXBElement }{@code <}{@link ExplicitGroup }{@code >}
     * {@link JAXBElement }{@code <}{@link All }{@code >}
     * {@link JAXBElement }{@code <}{@link LocalElement }{@code >}
     * {@link Any }
     * {@link JAXBElement }{@code <}{@link ExplicitGroup }{@code >}
     * 
     * 
     */
    public List<Object> getParticle() {
        if (particle == null) {
            particle = new ArrayList<Object>();
        }
        return this.particle;
    }

    /**
     * Obtém o valor da propriedade minOccurs.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMinOccurs() {
        if (minOccurs == null) {
            return new BigInteger("1");
        } else {
            return minOccurs;
        }
    }

    /**
     * Define o valor da propriedade minOccurs.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMinOccurs(BigInteger value) {
        this.minOccurs = value;
    }

    /**
     * Obtém o valor da propriedade maxOccurs.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxOccurs() {
        if (maxOccurs == null) {
            return "1";
        } else {
            return maxOccurs;
        }
    }

    /**
     * Define o valor da propriedade maxOccurs.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxOccurs(String value) {
        this.maxOccurs = value;
    }

    /**
     * Obtém o valor da propriedade name.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Define o valor da propriedade name.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Obtém o valor da propriedade ref.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getRef() {
        return ref;
    }

    /**
     * Define o valor da propriedade ref.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setRef(QName value) {
        this.ref = value;
    }

}
