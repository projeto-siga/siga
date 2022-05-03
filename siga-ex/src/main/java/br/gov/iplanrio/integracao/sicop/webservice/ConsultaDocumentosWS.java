
package br.gov.iplanrio.integracao.sicop.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de ConsultaDocumentos_WS complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="ConsultaDocumentos_WS">
 *   &lt;complexContent>
 *     &lt;extension base="{http://pcrj/Sma/Sicop/Grava}Message">
 *       &lt;sequence>
 *         &lt;element name="ROTINA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OPCAO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIPDOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMDOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PRTESOURO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMEXT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FLAGVOLTAR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DOCUMENTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TIT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGRESPONSAVEL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDOCDESC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOMREQUERENTE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ENDERECO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ASSUNTO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPLEMENTO1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPLEMENTO2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPLEMENTO3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATCAD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HORCAD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PRAZO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATDIG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TPDOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NRDOC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDOCD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORIGEM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESTINO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ULTENDERECO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATTRAMITACAO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TEMPO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMGUIA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDIG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HORDIG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ULTMATDIG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRECEB" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATRECEBDESCR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATREC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RESREC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGREC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATATUREC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HORATUREC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StatusLine" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConsultaDocumentos_WS", propOrder = {
    "rotina",
    "opcao",
    "tipdoc",
    "numdoc",
    "prtesouro",
    "numext",
    "orgdoc",
    "flagvoltar",
    "documento",
    "tit",
    "desc",
    "orgresponsavel",
    "datdoc",
    "datdocdesc",
    "nomrequerente",
    "endereco",
    "assunto",
    "complemento1",
    "complemento2",
    "complemento3",
    "datcad",
    "horcad",
    "prazo",
    "matdig",
    "tpdoc",
    "nrdoc",
    "orgdocd",
    "origem",
    "destino",
    "ultendereco",
    "dattramitacao",
    "tempo",
    "numguia",
    "seq",
    "despacho",
    "datdig",
    "hordig",
    "ultmatdig",
    "matreceb",
    "matrecebdescr",
    "datrec",
    "resrec",
    "orgrec",
    "dataturec",
    "horaturec",
    "statusLine"
})
public class ConsultaDocumentosWS
    extends Message
{

    @XmlElement(name = "ROTINA")
    protected String rotina;
    @XmlElement(name = "OPCAO")
    protected String opcao;
    @XmlElement(name = "TIPDOC")
    protected String tipdoc;
    @XmlElement(name = "NUMDOC")
    protected String numdoc;
    @XmlElement(name = "PRTESOURO")
    protected String prtesouro;
    @XmlElement(name = "NUMEXT")
    protected String numext;
    @XmlElement(name = "ORGDOC")
    protected String orgdoc;
    @XmlElement(name = "FLAGVOLTAR")
    protected String flagvoltar;
    @XmlElement(name = "DOCUMENTO")
    protected String documento;
    @XmlElement(name = "TIT")
    protected String tit;
    @XmlElement(name = "DESC")
    protected String desc;
    @XmlElement(name = "ORGRESPONSAVEL")
    protected String orgresponsavel;
    @XmlElement(name = "DATDOC")
    protected String datdoc;
    @XmlElement(name = "DATDOCDESC")
    protected String datdocdesc;
    @XmlElement(name = "NOMREQUERENTE")
    protected String nomrequerente;
    @XmlElement(name = "ENDERECO")
    protected String endereco;
    @XmlElement(name = "ASSUNTO")
    protected String assunto;
    @XmlElement(name = "COMPLEMENTO1")
    protected String complemento1;
    @XmlElement(name = "COMPLEMENTO2")
    protected String complemento2;
    @XmlElement(name = "COMPLEMENTO3")
    protected String complemento3;
    @XmlElement(name = "DATCAD")
    protected String datcad;
    @XmlElement(name = "HORCAD")
    protected String horcad;
    @XmlElement(name = "PRAZO")
    protected String prazo;
    @XmlElement(name = "MATDIG")
    protected String matdig;
    @XmlElement(name = "TPDOC")
    protected String tpdoc;
    @XmlElement(name = "NRDOC")
    protected String nrdoc;
    @XmlElement(name = "ORGDOCD")
    protected String orgdocd;
    @XmlElement(name = "ORIGEM")
    protected String origem;
    @XmlElement(name = "DESTINO")
    protected String destino;
    @XmlElement(name = "ULTENDERECO")
    protected String ultendereco;
    @XmlElement(name = "DATTRAMITACAO")
    protected String dattramitacao;
    @XmlElement(name = "TEMPO")
    protected String tempo;
    @XmlElement(name = "NUMGUIA")
    protected String numguia;
    @XmlElement(name = "SEQ")
    protected String seq;
    @XmlElement(name = "DESPACHO")
    protected String despacho;
    @XmlElement(name = "DATDIG")
    protected String datdig;
    @XmlElement(name = "HORDIG")
    protected String hordig;
    @XmlElement(name = "ULTMATDIG")
    protected String ultmatdig;
    @XmlElement(name = "MATRECEB")
    protected String matreceb;
    @XmlElement(name = "MATRECEBDESCR")
    protected String matrecebdescr;
    @XmlElement(name = "DATREC")
    protected String datrec;
    @XmlElement(name = "RESREC")
    protected String resrec;
    @XmlElement(name = "ORGREC")
    protected String orgrec;
    @XmlElement(name = "DATATUREC")
    protected String dataturec;
    @XmlElement(name = "HORATUREC")
    protected String horaturec;
    @XmlElement(name = "StatusLine")
    protected String statusLine;

    /**
     * Obt�m o valor da propriedade rotina.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getROTINA() {
        return rotina;
    }

    /**
     * Define o valor da propriedade rotina.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setROTINA(String value) {
        this.rotina = value;
    }

    /**
     * Obt�m o valor da propriedade opcao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOPCAO() {
        return opcao;
    }

    /**
     * Define o valor da propriedade opcao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOPCAO(String value) {
        this.opcao = value;
    }

    /**
     * Obt�m o valor da propriedade tipdoc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPDOC() {
        return tipdoc;
    }

    /**
     * Define o valor da propriedade tipdoc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPDOC(String value) {
        this.tipdoc = value;
    }

    /**
     * Obt�m o valor da propriedade numdoc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMDOC() {
        return numdoc;
    }

    /**
     * Define o valor da propriedade numdoc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMDOC(String value) {
        this.numdoc = value;
    }

    /**
     * Obt�m o valor da propriedade prtesouro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRTESOURO() {
        return prtesouro;
    }

    /**
     * Define o valor da propriedade prtesouro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRTESOURO(String value) {
        this.prtesouro = value;
    }

    /**
     * Obt�m o valor da propriedade numext.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMEXT() {
        return numext;
    }

    /**
     * Define o valor da propriedade numext.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMEXT(String value) {
        this.numext = value;
    }

    /**
     * Obt�m o valor da propriedade orgdoc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDOC() {
        return orgdoc;
    }

    /**
     * Define o valor da propriedade orgdoc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDOC(String value) {
        this.orgdoc = value;
    }

    /**
     * Obt�m o valor da propriedade flagvoltar.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFLAGVOLTAR() {
        return flagvoltar;
    }

    /**
     * Define o valor da propriedade flagvoltar.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFLAGVOLTAR(String value) {
        this.flagvoltar = value;
    }

    /**
     * Obt�m o valor da propriedade documento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDOCUMENTO() {
        return documento;
    }

    /**
     * Define o valor da propriedade documento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDOCUMENTO(String value) {
        this.documento = value;
    }

    /**
     * Obt�m o valor da propriedade tit.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIT() {
        return tit;
    }

    /**
     * Define o valor da propriedade tit.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIT(String value) {
        this.tit = value;
    }

    /**
     * Obt�m o valor da propriedade desc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESC() {
        return desc;
    }

    /**
     * Define o valor da propriedade desc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESC(String value) {
        this.desc = value;
    }

    /**
     * Obt�m o valor da propriedade orgresponsavel.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGRESPONSAVEL() {
        return orgresponsavel;
    }

    /**
     * Define o valor da propriedade orgresponsavel.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGRESPONSAVEL(String value) {
        this.orgresponsavel = value;
    }

    /**
     * Obt�m o valor da propriedade datdoc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDOC() {
        return datdoc;
    }

    /**
     * Define o valor da propriedade datdoc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDOC(String value) {
        this.datdoc = value;
    }

    /**
     * Obt�m o valor da propriedade datdocdesc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDOCDESC() {
        return datdocdesc;
    }

    /**
     * Define o valor da propriedade datdocdesc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDOCDESC(String value) {
        this.datdocdesc = value;
    }

    /**
     * Obt�m o valor da propriedade nomrequerente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOMREQUERENTE() {
        return nomrequerente;
    }

    /**
     * Define o valor da propriedade nomrequerente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOMREQUERENTE(String value) {
        this.nomrequerente = value;
    }

    /**
     * Obt�m o valor da propriedade endereco.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getENDERECO() {
        return endereco;
    }

    /**
     * Define o valor da propriedade endereco.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setENDERECO(String value) {
        this.endereco = value;
    }

    /**
     * Obt�m o valor da propriedade assunto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getASSUNTO() {
        return assunto;
    }

    /**
     * Define o valor da propriedade assunto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setASSUNTO(String value) {
        this.assunto = value;
    }

    /**
     * Obt�m o valor da propriedade complemento1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPLEMENTO1() {
        return complemento1;
    }

    /**
     * Define o valor da propriedade complemento1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPLEMENTO1(String value) {
        this.complemento1 = value;
    }

    /**
     * Obt�m o valor da propriedade complemento2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPLEMENTO2() {
        return complemento2;
    }

    /**
     * Define o valor da propriedade complemento2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPLEMENTO2(String value) {
        this.complemento2 = value;
    }

    /**
     * Obt�m o valor da propriedade complemento3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMPLEMENTO3() {
        return complemento3;
    }

    /**
     * Define o valor da propriedade complemento3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMPLEMENTO3(String value) {
        this.complemento3 = value;
    }

    /**
     * Obt�m o valor da propriedade datcad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATCAD() {
        return datcad;
    }

    /**
     * Define o valor da propriedade datcad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATCAD(String value) {
        this.datcad = value;
    }

    /**
     * Obt�m o valor da propriedade horcad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHORCAD() {
        return horcad;
    }

    /**
     * Define o valor da propriedade horcad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHORCAD(String value) {
        this.horcad = value;
    }

    /**
     * Obt�m o valor da propriedade prazo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPRAZO() {
        return prazo;
    }

    /**
     * Define o valor da propriedade prazo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPRAZO(String value) {
        this.prazo = value;
    }

    /**
     * Obt�m o valor da propriedade matdig.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATDIG() {
        return matdig;
    }

    /**
     * Define o valor da propriedade matdig.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATDIG(String value) {
        this.matdig = value;
    }

    /**
     * Obt�m o valor da propriedade tpdoc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTPDOC() {
        return tpdoc;
    }

    /**
     * Define o valor da propriedade tpdoc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTPDOC(String value) {
        this.tpdoc = value;
    }

    /**
     * Obt�m o valor da propriedade nrdoc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNRDOC() {
        return nrdoc;
    }

    /**
     * Define o valor da propriedade nrdoc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNRDOC(String value) {
        this.nrdoc = value;
    }

    /**
     * Obt�m o valor da propriedade orgdocd.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDOCD() {
        return orgdocd;
    }

    /**
     * Define o valor da propriedade orgdocd.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDOCD(String value) {
        this.orgdocd = value;
    }

    /**
     * Obt�m o valor da propriedade origem.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORIGEM() {
        return origem;
    }

    /**
     * Define o valor da propriedade origem.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORIGEM(String value) {
        this.origem = value;
    }

    /**
     * Obt�m o valor da propriedade destino.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESTINO() {
        return destino;
    }

    /**
     * Define o valor da propriedade destino.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESTINO(String value) {
        this.destino = value;
    }

    /**
     * Obt�m o valor da propriedade ultendereco.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getULTENDERECO() {
        return ultendereco;
    }

    /**
     * Define o valor da propriedade ultendereco.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setULTENDERECO(String value) {
        this.ultendereco = value;
    }

    /**
     * Obt�m o valor da propriedade dattramitacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATTRAMITACAO() {
        return dattramitacao;
    }

    /**
     * Define o valor da propriedade dattramitacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATTRAMITACAO(String value) {
        this.dattramitacao = value;
    }

    /**
     * Obt�m o valor da propriedade tempo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTEMPO() {
        return tempo;
    }

    /**
     * Define o valor da propriedade tempo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTEMPO(String value) {
        this.tempo = value;
    }

    /**
     * Obt�m o valor da propriedade numguia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMGUIA() {
        return numguia;
    }

    /**
     * Define o valor da propriedade numguia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMGUIA(String value) {
        this.numguia = value;
    }

    /**
     * Obt�m o valor da propriedade seq.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ() {
        return seq;
    }

    /**
     * Define o valor da propriedade seq.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ(String value) {
        this.seq = value;
    }

    /**
     * Obt�m o valor da propriedade despacho.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO() {
        return despacho;
    }

    /**
     * Define o valor da propriedade despacho.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO(String value) {
        this.despacho = value;
    }

    /**
     * Obt�m o valor da propriedade datdig.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDIG() {
        return datdig;
    }

    /**
     * Define o valor da propriedade datdig.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDIG(String value) {
        this.datdig = value;
    }

    /**
     * Obt�m o valor da propriedade hordig.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHORDIG() {
        return hordig;
    }

    /**
     * Define o valor da propriedade hordig.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHORDIG(String value) {
        this.hordig = value;
    }

    /**
     * Obt�m o valor da propriedade ultmatdig.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getULTMATDIG() {
        return ultmatdig;
    }

    /**
     * Define o valor da propriedade ultmatdig.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setULTMATDIG(String value) {
        this.ultmatdig = value;
    }

    /**
     * Obt�m o valor da propriedade matreceb.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRECEB() {
        return matreceb;
    }

    /**
     * Define o valor da propriedade matreceb.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRECEB(String value) {
        this.matreceb = value;
    }

    /**
     * Obt�m o valor da propriedade matrecebdescr.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATRECEBDESCR() {
        return matrecebdescr;
    }

    /**
     * Define o valor da propriedade matrecebdescr.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATRECEBDESCR(String value) {
        this.matrecebdescr = value;
    }

    /**
     * Obt�m o valor da propriedade datrec.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATREC() {
        return datrec;
    }

    /**
     * Define o valor da propriedade datrec.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATREC(String value) {
        this.datrec = value;
    }

    /**
     * Obt�m o valor da propriedade resrec.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRESREC() {
        return resrec;
    }

    /**
     * Define o valor da propriedade resrec.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRESREC(String value) {
        this.resrec = value;
    }

    /**
     * Obt�m o valor da propriedade orgrec.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGREC() {
        return orgrec;
    }

    /**
     * Define o valor da propriedade orgrec.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGREC(String value) {
        this.orgrec = value;
    }

    /**
     * Obt�m o valor da propriedade dataturec.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATATUREC() {
        return dataturec;
    }

    /**
     * Define o valor da propriedade dataturec.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATATUREC(String value) {
        this.dataturec = value;
    }

    /**
     * Obt�m o valor da propriedade horaturec.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHORATUREC() {
        return horaturec;
    }

    /**
     * Define o valor da propriedade horaturec.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHORATUREC(String value) {
        this.horaturec = value;
    }

    /**
     * Obt�m o valor da propriedade statusLine.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusLine() {
        return statusLine;
    }

    /**
     * Define o valor da propriedade statusLine.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusLine(String value) {
        this.statusLine = value;
    }

}
