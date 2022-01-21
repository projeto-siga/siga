
package br.gov.jfrj.siga.integracao.ws.siafem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de anonymous complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Usuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Senha" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AnoBase" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UnidadeGestora" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DocumentoXML" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sIP_Estimulo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "usuario",
    "senha",
    "anoBase",
    "unidadeGestora",
    "documentoXML",
    "sipEstimulo"
})
@XmlRootElement(name = "Mensagem2")
public class Mensagem2 {

    @XmlElement(name = "Usuario")
    protected String usuario;
    @XmlElement(name = "Senha")
    protected String senha;
    @XmlElement(name = "AnoBase")
    protected String anoBase;
    @XmlElement(name = "UnidadeGestora")
    protected String unidadeGestora;
    @XmlElement(name = "DocumentoXML")
    protected String documentoXML;
    @XmlElement(name = "sIP_Estimulo")
    protected String sipEstimulo;

    /**
     * Obt�m o valor da propriedade usuario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Define o valor da propriedade usuario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsuario(String value) {
        this.usuario = value;
    }

    /**
     * Obt�m o valor da propriedade senha.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Define o valor da propriedade senha.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenha(String value) {
        this.senha = value;
    }

    /**
     * Obt�m o valor da propriedade anoBase.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnoBase() {
        return anoBase;
    }

    /**
     * Define o valor da propriedade anoBase.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnoBase(String value) {
        this.anoBase = value;
    }

    /**
     * Obt�m o valor da propriedade unidadeGestora.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnidadeGestora() {
        return unidadeGestora;
    }

    /**
     * Define o valor da propriedade unidadeGestora.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnidadeGestora(String value) {
        this.unidadeGestora = value;
    }

    /**
     * Obt�m o valor da propriedade documentoXML.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentoXML() {
        return documentoXML;
    }

    /**
     * Define o valor da propriedade documentoXML.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentoXML(String value) {
        this.documentoXML = value;
    }

    /**
     * Obt�m o valor da propriedade sipEstimulo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIPEstimulo() {
        return sipEstimulo;
    }

    /**
     * Define o valor da propriedade sipEstimulo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIPEstimulo(String value) {
        this.sipEstimulo = value;
    }

}
