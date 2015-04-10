
package bluecrystal.service.v1.icpbr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for validateSignatureByPolicy complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="validateSignatureByPolicy">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="signCmsb64" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="psb64" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "validateSignatureByPolicy", propOrder = {
    "signCmsb64",
    "psb64"
})
public class ValidateSignatureByPolicy {

    protected String signCmsb64;
    protected String psb64;

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
     * Gets the value of the psb64 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPsb64() {
        return psb64;
    }

    /**
     * Sets the value of the psb64 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPsb64(String value) {
        this.psb64 = value;
    }

}
