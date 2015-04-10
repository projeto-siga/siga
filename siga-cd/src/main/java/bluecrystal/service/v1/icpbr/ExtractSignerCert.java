
package bluecrystal.service.v1.icpbr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for extractSignerCert complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="extractSignerCert">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="signCmsb64" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "extractSignerCert", propOrder = {
    "signCmsb64"
})
public class ExtractSignerCert {

    protected String signCmsb64;

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

}
