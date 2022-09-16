
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
 *         &lt;element name="licitacao_identificador" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="modalidade" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="evento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numeropublicacao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="processo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="datapublicacao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "hashMD5Sintese",
    "licitacaoIdentificador",
    "modalidade",
    "evento",
    "numeropublicacao",
    "processo",
    "datapublicacao"
})
@XmlRootElement(name = "montaReciboNovoEvento")
public class MontaReciboNovoEvento {

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
    @XmlElement(name = "licitacao_identificador")
    protected int licitacaoIdentificador;
    protected String modalidade;
    protected String evento;
    protected String numeropublicacao;
    protected String processo;
    protected String datapublicacao;

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

    /**
     * Obtém o valor da propriedade licitacaoIdentificador.
     * 
     */
    public int getLicitacaoIdentificador() {
        return licitacaoIdentificador;
    }

    /**
     * Define o valor da propriedade licitacaoIdentificador.
     * 
     */
    public void setLicitacaoIdentificador(int value) {
        this.licitacaoIdentificador = value;
    }

    /**
     * Obtém o valor da propriedade modalidade.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModalidade() {
        return modalidade;
    }

    /**
     * Define o valor da propriedade modalidade.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModalidade(String value) {
        this.modalidade = value;
    }

    /**
     * Obtém o valor da propriedade evento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEvento() {
        return evento;
    }

    /**
     * Define o valor da propriedade evento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEvento(String value) {
        this.evento = value;
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

    /**
     * Obtém o valor da propriedade datapublicacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatapublicacao() {
        return datapublicacao;
    }

    /**
     * Define o valor da propriedade datapublicacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatapublicacao(String value) {
        this.datapublicacao = value;
    }

}
