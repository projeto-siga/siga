
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
 *         &lt;element name="nome_arquivo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="justificativa_identificador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="recibo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="reciboHash" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "nomeArquivo",
    "justificativaIdentificador",
    "recibo",
    "reciboHash"
})
@XmlRootElement(name = "cancelarMaterial")
public class CancelarMaterial {

    @XmlElement(name = "nome_arquivo")
    protected String nomeArquivo;
    @XmlElement(name = "justificativa_identificador")
    protected String justificativaIdentificador;
    protected String recibo;
    protected String reciboHash;

    /**
     * Obtém o valor da propriedade nomeArquivo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeArquivo() {
        return nomeArquivo;
    }

    /**
     * Define o valor da propriedade nomeArquivo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeArquivo(String value) {
        this.nomeArquivo = value;
    }

    /**
     * Obtém o valor da propriedade justificativaIdentificador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJustificativaIdentificador() {
        return justificativaIdentificador;
    }

    /**
     * Define o valor da propriedade justificativaIdentificador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJustificativaIdentificador(String value) {
        this.justificativaIdentificador = value;
    }

    /**
     * Obtém o valor da propriedade recibo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecibo() {
        return recibo;
    }

    /**
     * Define o valor da propriedade recibo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecibo(String value) {
        this.recibo = value;
    }

    /**
     * Obtém o valor da propriedade reciboHash.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReciboHash() {
        return reciboHash;
    }

    /**
     * Define o valor da propriedade reciboHash.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReciboHash(String value) {
        this.reciboHash = value;
    }

}
