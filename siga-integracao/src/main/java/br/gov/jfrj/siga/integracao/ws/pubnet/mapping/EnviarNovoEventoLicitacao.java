
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
 *         &lt;element name="anunciante_identificador" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="caderno_identificador" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="retranca_codigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tipomaterial_identificador" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="licitacao_identificador" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="modalidade" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="evento_identificador" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="sintese" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="datapublicacao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "anuncianteIdentificador",
    "cadernoIdentificador",
    "retrancaCodigo",
    "tipomaterialIdentificador",
    "licitacaoIdentificador",
    "modalidade",
    "eventoIdentificador",
    "sintese",
    "datapublicacao",
    "recibo",
    "reciboHash"
})
@XmlRootElement(name = "enviarNovoEventoLicitacao")
public class EnviarNovoEventoLicitacao {

    @XmlElement(name = "anunciante_identificador")
    protected int anuncianteIdentificador;
    @XmlElement(name = "caderno_identificador")
    protected int cadernoIdentificador;
    @XmlElement(name = "retranca_codigo")
    protected String retrancaCodigo;
    @XmlElement(name = "tipomaterial_identificador")
    protected int tipomaterialIdentificador;
    @XmlElement(name = "licitacao_identificador")
    protected int licitacaoIdentificador;
    protected int modalidade;
    @XmlElement(name = "evento_identificador")
    protected int eventoIdentificador;
    protected String sintese;
    protected String datapublicacao;
    protected String recibo;
    protected String reciboHash;

    /**
     * Obtém o valor da propriedade anuncianteIdentificador.
     * 
     */
    public int getAnuncianteIdentificador() {
        return anuncianteIdentificador;
    }

    /**
     * Define o valor da propriedade anuncianteIdentificador.
     * 
     */
    public void setAnuncianteIdentificador(int value) {
        this.anuncianteIdentificador = value;
    }

    /**
     * Obtém o valor da propriedade cadernoIdentificador.
     * 
     */
    public int getCadernoIdentificador() {
        return cadernoIdentificador;
    }

    /**
     * Define o valor da propriedade cadernoIdentificador.
     * 
     */
    public void setCadernoIdentificador(int value) {
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
     */
    public int getTipomaterialIdentificador() {
        return tipomaterialIdentificador;
    }

    /**
     * Define o valor da propriedade tipomaterialIdentificador.
     * 
     */
    public void setTipomaterialIdentificador(int value) {
        this.tipomaterialIdentificador = value;
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
     */
    public int getModalidade() {
        return modalidade;
    }

    /**
     * Define o valor da propriedade modalidade.
     * 
     */
    public void setModalidade(int value) {
        this.modalidade = value;
    }

    /**
     * Obtém o valor da propriedade eventoIdentificador.
     * 
     */
    public int getEventoIdentificador() {
        return eventoIdentificador;
    }

    /**
     * Define o valor da propriedade eventoIdentificador.
     * 
     */
    public void setEventoIdentificador(int value) {
        this.eventoIdentificador = value;
    }

    /**
     * Obtém o valor da propriedade sintese.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSintese() {
        return sintese;
    }

    /**
     * Define o valor da propriedade sintese.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSintese(String value) {
        this.sintese = value;
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
