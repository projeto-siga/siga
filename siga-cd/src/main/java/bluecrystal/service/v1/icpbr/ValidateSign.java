
package bluecrystal.service.v1.icpbr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for validateSign complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="validateSign">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="signCmsb64" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="origHashb64" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signingTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="verifyCrlOcsp" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "validateSign", propOrder = {
    "signCmsb64",
    "origHashb64",
    "signingTime",
    "verifyCrlOcsp"
})
public class ValidateSign {

    protected String signCmsb64;
    protected String origHashb64;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar signingTime;
    protected boolean verifyCrlOcsp;

    /**
     * Gets the value of the signCmsb64 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignCmsb64() {
        return signCmsb64;
    }

    /**
     * Sets the value of the signCmsb64 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignCmsb64(String value) {
        this.signCmsb64 = value;
    }

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
     * Gets the value of the verifyCrlOcsp property.
     * 
     */
    public boolean isVerifyCrlOcsp() {
        return verifyCrlOcsp;
    }

    /**
     * Sets the value of the verifyCrlOcsp property.
     * 
     */
    public void setVerifyCrlOcsp(boolean value) {
        this.verifyCrlOcsp = value;
    }

}
