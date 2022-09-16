
package br.gov.jfrj.siga.integracao.ws.pubnet.mapping;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="anunciante_identificador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="caderno_identificador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="retranca_codigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tipomaterial_identificador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sequencial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="hashMD5sintese" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "anuncianteIdentificador",
    "cadernoIdentificador",
    "retrancaCodigo",
    "tipomaterialIdentificador",
    "sequencial",
    "hashMD5Sintese"
})
@XmlRootElement(name = "montaReciboPublicacao")
public class MontaReciboPublicacao {

    @XmlElement(name = "anunciante_identificador")
    protected String anuncianteIdentificador;
    @XmlElement(name = "caderno_identificador")
    protected String cadernoIdentificador;
    @XmlElement(name = "retranca_codigo")
    protected String retrancaCodigo;
    @XmlElement(name = "tipomaterial_identificador")
    protected String tipomaterialIdentificador;
    protected String sequencial;
    @XmlElement(name = "hashMD5sintese")
    protected String hashMD5Sintese;

    /**
     * Obtém o valor da propriedade anuncianteIdentificador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnuncianteIdentificador() {
        return anuncianteIdentificador;
    }

    /**
     * Define o valor da propriedade anuncianteIdentificador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnuncianteIdentificador(String value) {
        this.anuncianteIdentificador = value;
    }

    /**
     * Obtém o valor da propriedade cadernoIdentificador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCadernoIdentificador() {
        return cadernoIdentificador;
    }

    /**
     * Define o valor da propriedade cadernoIdentificador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCadernoIdentificador(String value) {
        this.cadernoIdentificador = value;
    }

    /**
     * Obtém o valor da propriedade retrancaCodigo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRetrancaCodigo() {
        return retrancaCodigo;
    }

    /**
     * Define o valor da propriedade retrancaCodigo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRetrancaCodigo(String value) {
        this.retrancaCodigo = value;
    }

    /**
     * Obtém o valor da propriedade tipomaterialIdentificador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipomaterialIdentificador() {
        return tipomaterialIdentificador;
    }

    /**
     * Define o valor da propriedade tipomaterialIdentificador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipomaterialIdentificador(String value) {
        this.tipomaterialIdentificador = value;
    }

    /**
     * Obtém o valor da propriedade sequencial.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSequencial() {
        return sequencial;
    }

    /**
     * Define o valor da propriedade sequencial.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSequencial(String value) {
        this.sequencial = value;
    }

    /**
     * Obtém o valor da propriedade hashMD5Sintese.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHashMD5Sintese() {
        return hashMD5Sintese;
    }

    /**
     * Define o valor da propriedade hashMD5Sintese.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHashMD5Sintese(String value) {
        this.hashMD5Sintese = value;
    }

}
