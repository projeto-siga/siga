
package bluecrystal.service.v1.icpbr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for hashSignedAttribADRB21 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="hashSignedAttribADRB21">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="origHashb64" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signingTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="certb64" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "hashSignedAttribADRB21", propOrder = {
    "origHashb64",
    "signingTime",
    "certb64"
})
public class HashSignedAttribADRB21 {

    protected String origHashb64;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar signingTime;
    protected String certb64;

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
     * Gets the value of the certb64 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertb64() {
        return certb64;
    }

    /**
     * Sets the value of the certb64 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertb64(String value) {
        this.certb64 = value;
    }

}
