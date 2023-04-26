
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
 *         &lt;element name="statuspublicacao_identificador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataenvioinicio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataenviofim" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="caderno_identificador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="anunciante_identificador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="retranca_codigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tipomateria_identificador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="modalidade_identificador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numeropublicacao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="processo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "statuspublicacaoIdentificador",
    "dataenvioinicio",
    "dataenviofim",
    "cadernoIdentificador",
    "anuncianteIdentificador",
    "retrancaCodigo",
    "tipomateriaIdentificador",
    "modalidadeIdentificador",
    "numeropublicacao",
    "processo"
})
@XmlRootElement(name = "consultaLicitacao")
public class ConsultaLicitacao {

    @XmlElement(name = "statuspublicacao_identificador")
    protected String statuspublicacaoIdentificador;
    protected String dataenvioinicio;
    protected String dataenviofim;
    @XmlElement(name = "caderno_identificador")
    protected String cadernoIdentificador;
    @XmlElement(name = "anunciante_identificador")
    protected String anuncianteIdentificador;
    @XmlElement(name = "retranca_codigo")
    protected String retrancaCodigo;
    @XmlElement(name = "tipomateria_identificador")
    protected String tipomateriaIdentificador;
    @XmlElement(name = "modalidade_identificador")
    protected String modalidadeIdentificador;
    protected String numeropublicacao;
    protected String processo;

    /**
     * Obtém o valor da propriedade statuspublicacaoIdentificador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatuspublicacaoIdentificador() {
        return statuspublicacaoIdentificador;
    }

    /**
     * Define o valor da propriedade statuspublicacaoIdentificador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatuspublicacaoIdentificador(String value) {
        this.statuspublicacaoIdentificador = value;
    }

    /**
     * Obtém o valor da propriedade dataenvioinicio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataenvioinicio() {
        return dataenvioinicio;
    }

    /**
     * Define o valor da propriedade dataenvioinicio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataenvioinicio(String value) {
        this.dataenvioinicio = value;
    }

    /**
     * Obtém o valor da propriedade dataenviofim.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataenviofim() {
        return dataenviofim;
    }

    /**
     * Define o valor da propriedade dataenviofim.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataenviofim(String value) {
        this.dataenviofim = value;
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
     * Obtém o valor da propriedade tipomateriaIdentificador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipomateriaIdentificador() {
        return tipomateriaIdentificador;
    }

    /**
     * Define o valor da propriedade tipomateriaIdentificador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipomateriaIdentificador(String value) {
        this.tipomateriaIdentificador = value;
    }

    /**
     * Obtém o valor da propriedade modalidadeIdentificador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModalidadeIdentificador() {
        return modalidadeIdentificador;
    }

    /**
     * Define o valor da propriedade modalidadeIdentificador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModalidadeIdentificador(String value) {
        this.modalidadeIdentificador = value;
    }

    /**
     * Obtém o valor da propriedade numeropublicacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeropublicacao() {
        return numeropublicacao;
    }

    /**
     * Define o valor da propriedade numeropublicacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeropublicacao(String value) {
        this.numeropublicacao = value;
    }

    /**
     * Obtém o valor da propriedade processo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcesso() {
        return processo;
    }

    /**
     * Define o valor da propriedade processo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcesso(String value) {
        this.processo = value;
    }

}
