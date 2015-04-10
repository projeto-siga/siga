
package com.ittru.ccws.webservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for signCompare complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="signCompare">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="numCerts" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="psOid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="psUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signedAttribs" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="signingTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "signCompare", propOrder = {
    "numCerts",
    "psOid",
    "psUrl",
    "signedAttribs",
    "signingTime"
})
public class SignCompare {

    protected int numCerts;
    protected String psOid;
    protected String psUrl;
    @XmlElement(nillable = true)
    protected List<String> signedAttribs;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar signingTime;

    /**
     * Gets the value of the numCerts property.
     * 
     */
    public int getNumCerts() {
        return numCerts;
    }

    /**
     * Sets the value of the numCerts property.
     * 
     */
    public void setNumCerts(int value) {
        this.numCerts = value;
    }

    /**
     * Gets the value of the psOid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPsOid() {
        return psOid;
    }

    /**
     * Sets the value of the psOid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPsOid(String value) {
        this.psOid = value;
    }

    /**
     * Gets the value of the psUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPsUrl() {
        return psUrl;
    }

    /**
     * Sets the value of the psUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPsUrl(String value) {
        this.psUrl = value;
    }

    /**
     * Gets the value of the signedAttribs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signedAttribs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSignedAttribs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSignedAttribs() {
        if (signedAttribs == null) {
            signedAttribs = new ArrayList<String>();
        }
        return this.signedAttribs;
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

}
