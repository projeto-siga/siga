
package bluecrystal.service.v1.icpbr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for hashSignedAttribADRB10 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="hashSignedAttribADRB10">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="origHashb64" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signingTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="x509" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "hashSignedAttribADRB10", propOrder = {
    "origHashb64",
    "signingTime",
    "x509"
})
public class HashSignedAttribADRB10 {

    protected String origHashb64;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar signingTime;
    protected String x509;

    /**
     * Gets the value of the origHashb64 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigHashb64() {
        return origHashb64;
    }

    /**
     * Sets the value of the origHashb64 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigHashb64(String value) {
        this.origHashb64 = value;
    }

    /**
     * Gets the value of the signingTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSigningTime() {
        return signingTime;
    }

    /**
     * Sets the value of the signingTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSigningTime(XMLGregorianCalendar value) {
        this.signingTime = value;
    }

    /**
     * Gets the value of the x509 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getX509() {
        return x509;
    }

    /**
     * Sets the value of the x509 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setX509(String value) {
        this.x509 = value;
    }

}
