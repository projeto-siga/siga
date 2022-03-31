
package br.gov.iplanrio.integracao.sicop.webservice;

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
 *         &lt;element name="ROTINA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OPCAO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMVOL1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLINI1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLFIN1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGSOLI1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATSOLIC1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATSOLIC1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMVOL2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLINI2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLFIN2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGSOLI2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATSOLIC2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATSOLIC2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMVOL3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLINI3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLFIN3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGSOLI3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATSOLIC3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATSOLIC3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMVOL4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLINI4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLFIN4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGSOLI4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATSOLIC4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATSOLIC4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMVOL5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLINI5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLFIN5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGSOLI5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATSOLIC5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATSOLIC5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMVOL6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLINI6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLFIN6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGSOLI6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATSOLIC6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATSOLIC6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMVOL7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLINI7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLFIN7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGSOLI7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATSOLIC7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATSOLIC7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMVOL8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLINI8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLFIN8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGSOLI8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATSOLIC8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATSOLIC8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMVOL9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLINI9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLFIN9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGSOLI9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATSOLIC9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATSOLIC9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMVOL10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLINI10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLFIN10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGSOLI10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATSOLIC10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATSOLIC10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMVOL11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLINI11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLFIN11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGSOLI11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATSOLIC11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATSOLIC11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMVOL12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLINI12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLFIN12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGSOLI12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATSOLIC12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATSOLIC12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMVOL13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLINI13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMFLFIN13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGSOLI13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATSOLIC13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATSOLIC13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SENHA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="INDCONFIRMA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "rotina",
    "opcao",
    "numproc1",
    "numvol1",
    "numflini1",
    "numflfin1",
    "orgsoli1",
    "datsolic1",
    "matsolic1",
    "numproc2",
    "numvol2",
    "numflini2",
    "numflfin2",
    "orgsoli2",
    "datsolic2",
    "matsolic2",
    "numproc3",
    "numvol3",
    "numflini3",
    "numflfin3",
    "orgsoli3",
    "datsolic3",
    "matsolic3",
    "numproc4",
    "numvol4",
    "numflini4",
    "numflfin4",
    "orgsoli4",
    "datsolic4",
    "matsolic4",
    "numproc5",
    "numvol5",
    "numflini5",
    "numflfin5",
    "orgsoli5",
    "datsolic5",
    "matsolic5",
    "numproc6",
    "numvol6",
    "numflini6",
    "numflfin6",
    "orgsoli6",
    "datsolic6",
    "matsolic6",
    "numproc7",
    "numvol7",
    "numflini7",
    "numflfin7",
    "orgsoli7",
    "datsolic7",
    "matsolic7",
    "numproc8",
    "numvol8",
    "numflini8",
    "numflfin8",
    "orgsoli8",
    "datsolic8",
    "matsolic8",
    "numproc9",
    "numvol9",
    "numflini9",
    "numflfin9",
    "orgsoli9",
    "datsolic9",
    "matsolic9",
    "numproc10",
    "numvol10",
    "numflini10",
    "numflfin10",
    "orgsoli10",
    "datsolic10",
    "matsolic10",
    "numproc11",
    "numvol11",
    "numflini11",
    "numflfin11",
    "orgsoli11",
    "datsolic11",
    "matsolic11",
    "numproc12",
    "numvol12",
    "numflini12",
    "numflfin12",
    "orgsoli12",
    "datsolic12",
    "matsolic12",
    "numproc13",
    "numvol13",
    "numflini13",
    "numflfin13",
    "orgsoli13",
    "datsolic13",
    "matsolic13",
    "senha",
    "indconfirma"
})
@XmlRootElement(name = "TCadastroDeVolumesdeProcesso_WS")
public class TCadastroDeVolumesdeProcessoWS {

    @XmlElement(name = "ROTINA")
    protected String rotina;
    @XmlElement(name = "OPCAO")
    protected String opcao;
    @XmlElement(name = "NUMPROC1")
    protected String numproc1;
    @XmlElement(name = "NUMVOL1")
    protected String numvol1;
    @XmlElement(name = "NUMFLINI1")
    protected String numflini1;
    @XmlElement(name = "NUMFLFIN1")
    protected String numflfin1;
    @XmlElement(name = "ORGSOLI1")
    protected String orgsoli1;
    @XmlElement(name = "DATSOLIC1")
    protected String datsolic1;
    @XmlElement(name = "MATSOLIC1")
    protected String matsolic1;
    @XmlElement(name = "NUMPROC2")
    protected String numproc2;
    @XmlElement(name = "NUMVOL2")
    protected String numvol2;
    @XmlElement(name = "NUMFLINI2")
    protected String numflini2;
    @XmlElement(name = "NUMFLFIN2")
    protected String numflfin2;
    @XmlElement(name = "ORGSOLI2")
    protected String orgsoli2;
    @XmlElement(name = "DATSOLIC2")
    protected String datsolic2;
    @XmlElement(name = "MATSOLIC2")
    protected String matsolic2;
    @XmlElement(name = "NUMPROC3")
    protected String numproc3;
    @XmlElement(name = "NUMVOL3")
    protected String numvol3;
    @XmlElement(name = "NUMFLINI3")
    protected String numflini3;
    @XmlElement(name = "NUMFLFIN3")
    protected String numflfin3;
    @XmlElement(name = "ORGSOLI3")
    protected String orgsoli3;
    @XmlElement(name = "DATSOLIC3")
    protected String datsolic3;
    @XmlElement(name = "MATSOLIC3")
    protected String matsolic3;
    @XmlElement(name = "NUMPROC4")
    protected String numproc4;
    @XmlElement(name = "NUMVOL4")
    protected String numvol4;
    @XmlElement(name = "NUMFLINI4")
    protected String numflini4;
    @XmlElement(name = "NUMFLFIN4")
    protected String numflfin4;
    @XmlElement(name = "ORGSOLI4")
    protected String orgsoli4;
    @XmlElement(name = "DATSOLIC4")
    protected String datsolic4;
    @XmlElement(name = "MATSOLIC4")
    protected String matsolic4;
    @XmlElement(name = "NUMPROC5")
    protected String numproc5;
    @XmlElement(name = "NUMVOL5")
    protected String numvol5;
    @XmlElement(name = "NUMFLINI5")
    protected String numflini5;
    @XmlElement(name = "NUMFLFIN5")
    protected String numflfin5;
    @XmlElement(name = "ORGSOLI5")
    protected String orgsoli5;
    @XmlElement(name = "DATSOLIC5")
    protected String datsolic5;
    @XmlElement(name = "MATSOLIC5")
    protected String matsolic5;
    @XmlElement(name = "NUMPROC6")
    protected String numproc6;
    @XmlElement(name = "NUMVOL6")
    protected String numvol6;
    @XmlElement(name = "NUMFLINI6")
    protected String numflini6;
    @XmlElement(name = "NUMFLFIN6")
    protected String numflfin6;
    @XmlElement(name = "ORGSOLI6")
    protected String orgsoli6;
    @XmlElement(name = "DATSOLIC6")
    protected String datsolic6;
    @XmlElement(name = "MATSOLIC6")
    protected String matsolic6;
    @XmlElement(name = "NUMPROC7")
    protected String numproc7;
    @XmlElement(name = "NUMVOL7")
    protected String numvol7;
    @XmlElement(name = "NUMFLINI7")
    protected String numflini7;
    @XmlElement(name = "NUMFLFIN7")
    protected String numflfin7;
    @XmlElement(name = "ORGSOLI7")
    protected String orgsoli7;
    @XmlElement(name = "DATSOLIC7")
    protected String datsolic7;
    @XmlElement(name = "MATSOLIC7")
    protected String matsolic7;
    @XmlElement(name = "NUMPROC8")
    protected String numproc8;
    @XmlElement(name = "NUMVOL8")
    protected String numvol8;
    @XmlElement(name = "NUMFLINI8")
    protected String numflini8;
    @XmlElement(name = "NUMFLFIN8")
    protected String numflfin8;
    @XmlElement(name = "ORGSOLI8")
    protected String orgsoli8;
    @XmlElement(name = "DATSOLIC8")
    protected String datsolic8;
    @XmlElement(name = "MATSOLIC8")
    protected String matsolic8;
    @XmlElement(name = "NUMPROC9")
    protected String numproc9;
    @XmlElement(name = "NUMVOL9")
    protected String numvol9;
    @XmlElement(name = "NUMFLINI9")
    protected String numflini9;
    @XmlElement(name = "NUMFLFIN9")
    protected String numflfin9;
    @XmlElement(name = "ORGSOLI9")
    protected String orgsoli9;
    @XmlElement(name = "DATSOLIC9")
    protected String datsolic9;
    @XmlElement(name = "MATSOLIC9")
    protected String matsolic9;
    @XmlElement(name = "NUMPROC10")
    protected String numproc10;
    @XmlElement(name = "NUMVOL10")
    protected String numvol10;
    @XmlElement(name = "NUMFLINI10")
    protected String numflini10;
    @XmlElement(name = "NUMFLFIN10")
    protected String numflfin10;
    @XmlElement(name = "ORGSOLI10")
    protected String orgsoli10;
    @XmlElement(name = "DATSOLIC10")
    protected String datsolic10;
    @XmlElement(name = "MATSOLIC10")
    protected String matsolic10;
    @XmlElement(name = "NUMPROC11")
    protected String numproc11;
    @XmlElement(name = "NUMVOL11")
    protected String numvol11;
    @XmlElement(name = "NUMFLINI11")
    protected String numflini11;
    @XmlElement(name = "NUMFLFIN11")
    protected String numflfin11;
    @XmlElement(name = "ORGSOLI11")
    protected String orgsoli11;
    @XmlElement(name = "DATSOLIC11")
    protected String datsolic11;
    @XmlElement(name = "MATSOLIC11")
    protected String matsolic11;
    @XmlElement(name = "NUMPROC12")
    protected String numproc12;
    @XmlElement(name = "NUMVOL12")
    protected String numvol12;
    @XmlElement(name = "NUMFLINI12")
    protected String numflini12;
    @XmlElement(name = "NUMFLFIN12")
    protected String numflfin12;
    @XmlElement(name = "ORGSOLI12")
    protected String orgsoli12;
    @XmlElement(name = "DATSOLIC12")
    protected String datsolic12;
    @XmlElement(name = "MATSOLIC12")
    protected String matsolic12;
    @XmlElement(name = "NUMPROC13")
    protected String numproc13;
    @XmlElement(name = "NUMVOL13")
    protected String numvol13;
    @XmlElement(name = "NUMFLINI13")
    protected String numflini13;
    @XmlElement(name = "NUMFLFIN13")
    protected String numflfin13;
    @XmlElement(name = "ORGSOLI13")
    protected String orgsoli13;
    @XmlElement(name = "DATSOLIC13")
    protected String datsolic13;
    @XmlElement(name = "MATSOLIC13")
    protected String matsolic13;
    @XmlElement(name = "SENHA")
    protected String senha;
    @XmlElement(name = "INDCONFIRMA")
    protected String indconfirma;

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
     * Obt�m o valor da propriedade numproc1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC1() {
        return numproc1;
    }

    /**
     * Define o valor da propriedade numproc1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC1(String value) {
        this.numproc1 = value;
    }

    /**
     * Obt�m o valor da propriedade numvol1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMVOL1() {
        return numvol1;
    }

    /**
     * Define o valor da propriedade numvol1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMVOL1(String value) {
        this.numvol1 = value;
    }

    /**
     * Obt�m o valor da propriedade numflini1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLINI1() {
        return numflini1;
    }

    /**
     * Define o valor da propriedade numflini1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLINI1(String value) {
        this.numflini1 = value;
    }

    /**
     * Obt�m o valor da propriedade numflfin1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLFIN1() {
        return numflfin1;
    }

    /**
     * Define o valor da propriedade numflfin1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLFIN1(String value) {
        this.numflfin1 = value;
    }

    /**
     * Obt�m o valor da propriedade orgsoli1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGSOLI1() {
        return orgsoli1;
    }

    /**
     * Define o valor da propriedade orgsoli1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGSOLI1(String value) {
        this.orgsoli1 = value;
    }

    /**
     * Obt�m o valor da propriedade datsolic1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATSOLIC1() {
        return datsolic1;
    }

    /**
     * Define o valor da propriedade datsolic1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATSOLIC1(String value) {
        this.datsolic1 = value;
    }

    /**
     * Obt�m o valor da propriedade matsolic1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATSOLIC1() {
        return matsolic1;
    }

    /**
     * Define o valor da propriedade matsolic1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATSOLIC1(String value) {
        this.matsolic1 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC2() {
        return numproc2;
    }

    /**
     * Define o valor da propriedade numproc2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC2(String value) {
        this.numproc2 = value;
    }

    /**
     * Obt�m o valor da propriedade numvol2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMVOL2() {
        return numvol2;
    }

    /**
     * Define o valor da propriedade numvol2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMVOL2(String value) {
        this.numvol2 = value;
    }

    /**
     * Obt�m o valor da propriedade numflini2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLINI2() {
        return numflini2;
    }

    /**
     * Define o valor da propriedade numflini2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLINI2(String value) {
        this.numflini2 = value;
    }

    /**
     * Obt�m o valor da propriedade numflfin2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLFIN2() {
        return numflfin2;
    }

    /**
     * Define o valor da propriedade numflfin2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLFIN2(String value) {
        this.numflfin2 = value;
    }

    /**
     * Obt�m o valor da propriedade orgsoli2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGSOLI2() {
        return orgsoli2;
    }

    /**
     * Define o valor da propriedade orgsoli2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGSOLI2(String value) {
        this.orgsoli2 = value;
    }

    /**
     * Obt�m o valor da propriedade datsolic2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATSOLIC2() {
        return datsolic2;
    }

    /**
     * Define o valor da propriedade datsolic2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATSOLIC2(String value) {
        this.datsolic2 = value;
    }

    /**
     * Obt�m o valor da propriedade matsolic2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATSOLIC2() {
        return matsolic2;
    }

    /**
     * Define o valor da propriedade matsolic2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATSOLIC2(String value) {
        this.matsolic2 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC3() {
        return numproc3;
    }

    /**
     * Define o valor da propriedade numproc3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC3(String value) {
        this.numproc3 = value;
    }

    /**
     * Obt�m o valor da propriedade numvol3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMVOL3() {
        return numvol3;
    }

    /**
     * Define o valor da propriedade numvol3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMVOL3(String value) {
        this.numvol3 = value;
    }

    /**
     * Obt�m o valor da propriedade numflini3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLINI3() {
        return numflini3;
    }

    /**
     * Define o valor da propriedade numflini3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLINI3(String value) {
        this.numflini3 = value;
    }

    /**
     * Obt�m o valor da propriedade numflfin3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLFIN3() {
        return numflfin3;
    }

    /**
     * Define o valor da propriedade numflfin3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLFIN3(String value) {
        this.numflfin3 = value;
    }

    /**
     * Obt�m o valor da propriedade orgsoli3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGSOLI3() {
        return orgsoli3;
    }

    /**
     * Define o valor da propriedade orgsoli3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGSOLI3(String value) {
        this.orgsoli3 = value;
    }

    /**
     * Obt�m o valor da propriedade datsolic3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATSOLIC3() {
        return datsolic3;
    }

    /**
     * Define o valor da propriedade datsolic3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATSOLIC3(String value) {
        this.datsolic3 = value;
    }

    /**
     * Obt�m o valor da propriedade matsolic3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATSOLIC3() {
        return matsolic3;
    }

    /**
     * Define o valor da propriedade matsolic3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATSOLIC3(String value) {
        this.matsolic3 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC4() {
        return numproc4;
    }

    /**
     * Define o valor da propriedade numproc4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC4(String value) {
        this.numproc4 = value;
    }

    /**
     * Obt�m o valor da propriedade numvol4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMVOL4() {
        return numvol4;
    }

    /**
     * Define o valor da propriedade numvol4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMVOL4(String value) {
        this.numvol4 = value;
    }

    /**
     * Obt�m o valor da propriedade numflini4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLINI4() {
        return numflini4;
    }

    /**
     * Define o valor da propriedade numflini4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLINI4(String value) {
        this.numflini4 = value;
    }

    /**
     * Obt�m o valor da propriedade numflfin4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLFIN4() {
        return numflfin4;
    }

    /**
     * Define o valor da propriedade numflfin4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLFIN4(String value) {
        this.numflfin4 = value;
    }

    /**
     * Obt�m o valor da propriedade orgsoli4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGSOLI4() {
        return orgsoli4;
    }

    /**
     * Define o valor da propriedade orgsoli4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGSOLI4(String value) {
        this.orgsoli4 = value;
    }

    /**
     * Obt�m o valor da propriedade datsolic4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATSOLIC4() {
        return datsolic4;
    }

    /**
     * Define o valor da propriedade datsolic4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATSOLIC4(String value) {
        this.datsolic4 = value;
    }

    /**
     * Obt�m o valor da propriedade matsolic4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATSOLIC4() {
        return matsolic4;
    }

    /**
     * Define o valor da propriedade matsolic4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATSOLIC4(String value) {
        this.matsolic4 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC5() {
        return numproc5;
    }

    /**
     * Define o valor da propriedade numproc5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC5(String value) {
        this.numproc5 = value;
    }

    /**
     * Obt�m o valor da propriedade numvol5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMVOL5() {
        return numvol5;
    }

    /**
     * Define o valor da propriedade numvol5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMVOL5(String value) {
        this.numvol5 = value;
    }

    /**
     * Obt�m o valor da propriedade numflini5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLINI5() {
        return numflini5;
    }

    /**
     * Define o valor da propriedade numflini5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLINI5(String value) {
        this.numflini5 = value;
    }

    /**
     * Obt�m o valor da propriedade numflfin5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLFIN5() {
        return numflfin5;
    }

    /**
     * Define o valor da propriedade numflfin5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLFIN5(String value) {
        this.numflfin5 = value;
    }

    /**
     * Obt�m o valor da propriedade orgsoli5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGSOLI5() {
        return orgsoli5;
    }

    /**
     * Define o valor da propriedade orgsoli5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGSOLI5(String value) {
        this.orgsoli5 = value;
    }

    /**
     * Obt�m o valor da propriedade datsolic5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATSOLIC5() {
        return datsolic5;
    }

    /**
     * Define o valor da propriedade datsolic5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATSOLIC5(String value) {
        this.datsolic5 = value;
    }

    /**
     * Obt�m o valor da propriedade matsolic5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATSOLIC5() {
        return matsolic5;
    }

    /**
     * Define o valor da propriedade matsolic5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATSOLIC5(String value) {
        this.matsolic5 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC6() {
        return numproc6;
    }

    /**
     * Define o valor da propriedade numproc6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC6(String value) {
        this.numproc6 = value;
    }

    /**
     * Obt�m o valor da propriedade numvol6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMVOL6() {
        return numvol6;
    }

    /**
     * Define o valor da propriedade numvol6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMVOL6(String value) {
        this.numvol6 = value;
    }

    /**
     * Obt�m o valor da propriedade numflini6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLINI6() {
        return numflini6;
    }

    /**
     * Define o valor da propriedade numflini6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLINI6(String value) {
        this.numflini6 = value;
    }

    /**
     * Obt�m o valor da propriedade numflfin6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLFIN6() {
        return numflfin6;
    }

    /**
     * Define o valor da propriedade numflfin6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLFIN6(String value) {
        this.numflfin6 = value;
    }

    /**
     * Obt�m o valor da propriedade orgsoli6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGSOLI6() {
        return orgsoli6;
    }

    /**
     * Define o valor da propriedade orgsoli6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGSOLI6(String value) {
        this.orgsoli6 = value;
    }

    /**
     * Obt�m o valor da propriedade datsolic6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATSOLIC6() {
        return datsolic6;
    }

    /**
     * Define o valor da propriedade datsolic6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATSOLIC6(String value) {
        this.datsolic6 = value;
    }

    /**
     * Obt�m o valor da propriedade matsolic6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATSOLIC6() {
        return matsolic6;
    }

    /**
     * Define o valor da propriedade matsolic6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATSOLIC6(String value) {
        this.matsolic6 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC7() {
        return numproc7;
    }

    /**
     * Define o valor da propriedade numproc7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC7(String value) {
        this.numproc7 = value;
    }

    /**
     * Obt�m o valor da propriedade numvol7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMVOL7() {
        return numvol7;
    }

    /**
     * Define o valor da propriedade numvol7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMVOL7(String value) {
        this.numvol7 = value;
    }

    /**
     * Obt�m o valor da propriedade numflini7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLINI7() {
        return numflini7;
    }

    /**
     * Define o valor da propriedade numflini7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLINI7(String value) {
        this.numflini7 = value;
    }

    /**
     * Obt�m o valor da propriedade numflfin7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLFIN7() {
        return numflfin7;
    }

    /**
     * Define o valor da propriedade numflfin7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLFIN7(String value) {
        this.numflfin7 = value;
    }

    /**
     * Obt�m o valor da propriedade orgsoli7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGSOLI7() {
        return orgsoli7;
    }

    /**
     * Define o valor da propriedade orgsoli7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGSOLI7(String value) {
        this.orgsoli7 = value;
    }

    /**
     * Obt�m o valor da propriedade datsolic7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATSOLIC7() {
        return datsolic7;
    }

    /**
     * Define o valor da propriedade datsolic7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATSOLIC7(String value) {
        this.datsolic7 = value;
    }

    /**
     * Obt�m o valor da propriedade matsolic7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATSOLIC7() {
        return matsolic7;
    }

    /**
     * Define o valor da propriedade matsolic7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATSOLIC7(String value) {
        this.matsolic7 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC8() {
        return numproc8;
    }

    /**
     * Define o valor da propriedade numproc8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC8(String value) {
        this.numproc8 = value;
    }

    /**
     * Obt�m o valor da propriedade numvol8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMVOL8() {
        return numvol8;
    }

    /**
     * Define o valor da propriedade numvol8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMVOL8(String value) {
        this.numvol8 = value;
    }

    /**
     * Obt�m o valor da propriedade numflini8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLINI8() {
        return numflini8;
    }

    /**
     * Define o valor da propriedade numflini8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLINI8(String value) {
        this.numflini8 = value;
    }

    /**
     * Obt�m o valor da propriedade numflfin8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLFIN8() {
        return numflfin8;
    }

    /**
     * Define o valor da propriedade numflfin8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLFIN8(String value) {
        this.numflfin8 = value;
    }

    /**
     * Obt�m o valor da propriedade orgsoli8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGSOLI8() {
        return orgsoli8;
    }

    /**
     * Define o valor da propriedade orgsoli8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGSOLI8(String value) {
        this.orgsoli8 = value;
    }

    /**
     * Obt�m o valor da propriedade datsolic8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATSOLIC8() {
        return datsolic8;
    }

    /**
     * Define o valor da propriedade datsolic8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATSOLIC8(String value) {
        this.datsolic8 = value;
    }

    /**
     * Obt�m o valor da propriedade matsolic8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATSOLIC8() {
        return matsolic8;
    }

    /**
     * Define o valor da propriedade matsolic8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATSOLIC8(String value) {
        this.matsolic8 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC9() {
        return numproc9;
    }

    /**
     * Define o valor da propriedade numproc9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC9(String value) {
        this.numproc9 = value;
    }

    /**
     * Obt�m o valor da propriedade numvol9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMVOL9() {
        return numvol9;
    }

    /**
     * Define o valor da propriedade numvol9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMVOL9(String value) {
        this.numvol9 = value;
    }

    /**
     * Obt�m o valor da propriedade numflini9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLINI9() {
        return numflini9;
    }

    /**
     * Define o valor da propriedade numflini9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLINI9(String value) {
        this.numflini9 = value;
    }

    /**
     * Obt�m o valor da propriedade numflfin9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLFIN9() {
        return numflfin9;
    }

    /**
     * Define o valor da propriedade numflfin9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLFIN9(String value) {
        this.numflfin9 = value;
    }

    /**
     * Obt�m o valor da propriedade orgsoli9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGSOLI9() {
        return orgsoli9;
    }

    /**
     * Define o valor da propriedade orgsoli9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGSOLI9(String value) {
        this.orgsoli9 = value;
    }

    /**
     * Obt�m o valor da propriedade datsolic9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATSOLIC9() {
        return datsolic9;
    }

    /**
     * Define o valor da propriedade datsolic9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATSOLIC9(String value) {
        this.datsolic9 = value;
    }

    /**
     * Obt�m o valor da propriedade matsolic9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATSOLIC9() {
        return matsolic9;
    }

    /**
     * Define o valor da propriedade matsolic9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATSOLIC9(String value) {
        this.matsolic9 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC10() {
        return numproc10;
    }

    /**
     * Define o valor da propriedade numproc10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC10(String value) {
        this.numproc10 = value;
    }

    /**
     * Obt�m o valor da propriedade numvol10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMVOL10() {
        return numvol10;
    }

    /**
     * Define o valor da propriedade numvol10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMVOL10(String value) {
        this.numvol10 = value;
    }

    /**
     * Obt�m o valor da propriedade numflini10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLINI10() {
        return numflini10;
    }

    /**
     * Define o valor da propriedade numflini10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLINI10(String value) {
        this.numflini10 = value;
    }

    /**
     * Obt�m o valor da propriedade numflfin10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLFIN10() {
        return numflfin10;
    }

    /**
     * Define o valor da propriedade numflfin10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLFIN10(String value) {
        this.numflfin10 = value;
    }

    /**
     * Obt�m o valor da propriedade orgsoli10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGSOLI10() {
        return orgsoli10;
    }

    /**
     * Define o valor da propriedade orgsoli10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGSOLI10(String value) {
        this.orgsoli10 = value;
    }

    /**
     * Obt�m o valor da propriedade datsolic10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATSOLIC10() {
        return datsolic10;
    }

    /**
     * Define o valor da propriedade datsolic10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATSOLIC10(String value) {
        this.datsolic10 = value;
    }

    /**
     * Obt�m o valor da propriedade matsolic10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATSOLIC10() {
        return matsolic10;
    }

    /**
     * Define o valor da propriedade matsolic10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATSOLIC10(String value) {
        this.matsolic10 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC11() {
        return numproc11;
    }

    /**
     * Define o valor da propriedade numproc11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC11(String value) {
        this.numproc11 = value;
    }

    /**
     * Obt�m o valor da propriedade numvol11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMVOL11() {
        return numvol11;
    }

    /**
     * Define o valor da propriedade numvol11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMVOL11(String value) {
        this.numvol11 = value;
    }

    /**
     * Obt�m o valor da propriedade numflini11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLINI11() {
        return numflini11;
    }

    /**
     * Define o valor da propriedade numflini11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLINI11(String value) {
        this.numflini11 = value;
    }

    /**
     * Obt�m o valor da propriedade numflfin11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLFIN11() {
        return numflfin11;
    }

    /**
     * Define o valor da propriedade numflfin11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLFIN11(String value) {
        this.numflfin11 = value;
    }

    /**
     * Obt�m o valor da propriedade orgsoli11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGSOLI11() {
        return orgsoli11;
    }

    /**
     * Define o valor da propriedade orgsoli11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGSOLI11(String value) {
        this.orgsoli11 = value;
    }

    /**
     * Obt�m o valor da propriedade datsolic11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATSOLIC11() {
        return datsolic11;
    }

    /**
     * Define o valor da propriedade datsolic11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATSOLIC11(String value) {
        this.datsolic11 = value;
    }

    /**
     * Obt�m o valor da propriedade matsolic11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATSOLIC11() {
        return matsolic11;
    }

    /**
     * Define o valor da propriedade matsolic11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATSOLIC11(String value) {
        this.matsolic11 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC12() {
        return numproc12;
    }

    /**
     * Define o valor da propriedade numproc12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC12(String value) {
        this.numproc12 = value;
    }

    /**
     * Obt�m o valor da propriedade numvol12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMVOL12() {
        return numvol12;
    }

    /**
     * Define o valor da propriedade numvol12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMVOL12(String value) {
        this.numvol12 = value;
    }

    /**
     * Obt�m o valor da propriedade numflini12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLINI12() {
        return numflini12;
    }

    /**
     * Define o valor da propriedade numflini12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLINI12(String value) {
        this.numflini12 = value;
    }

    /**
     * Obt�m o valor da propriedade numflfin12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLFIN12() {
        return numflfin12;
    }

    /**
     * Define o valor da propriedade numflfin12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLFIN12(String value) {
        this.numflfin12 = value;
    }

    /**
     * Obt�m o valor da propriedade orgsoli12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGSOLI12() {
        return orgsoli12;
    }

    /**
     * Define o valor da propriedade orgsoli12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGSOLI12(String value) {
        this.orgsoli12 = value;
    }

    /**
     * Obt�m o valor da propriedade datsolic12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATSOLIC12() {
        return datsolic12;
    }

    /**
     * Define o valor da propriedade datsolic12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATSOLIC12(String value) {
        this.datsolic12 = value;
    }

    /**
     * Obt�m o valor da propriedade matsolic12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATSOLIC12() {
        return matsolic12;
    }

    /**
     * Define o valor da propriedade matsolic12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATSOLIC12(String value) {
        this.matsolic12 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC13() {
        return numproc13;
    }

    /**
     * Define o valor da propriedade numproc13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC13(String value) {
        this.numproc13 = value;
    }

    /**
     * Obt�m o valor da propriedade numvol13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMVOL13() {
        return numvol13;
    }

    /**
     * Define o valor da propriedade numvol13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMVOL13(String value) {
        this.numvol13 = value;
    }

    /**
     * Obt�m o valor da propriedade numflini13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLINI13() {
        return numflini13;
    }

    /**
     * Define o valor da propriedade numflini13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLINI13(String value) {
        this.numflini13 = value;
    }

    /**
     * Obt�m o valor da propriedade numflfin13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMFLFIN13() {
        return numflfin13;
    }

    /**
     * Define o valor da propriedade numflfin13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMFLFIN13(String value) {
        this.numflfin13 = value;
    }

    /**
     * Obt�m o valor da propriedade orgsoli13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGSOLI13() {
        return orgsoli13;
    }

    /**
     * Define o valor da propriedade orgsoli13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGSOLI13(String value) {
        this.orgsoli13 = value;
    }

    /**
     * Obt�m o valor da propriedade datsolic13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATSOLIC13() {
        return datsolic13;
    }

    /**
     * Define o valor da propriedade datsolic13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATSOLIC13(String value) {
        this.datsolic13 = value;
    }

    /**
     * Obt�m o valor da propriedade matsolic13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATSOLIC13() {
        return matsolic13;
    }

    /**
     * Define o valor da propriedade matsolic13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATSOLIC13(String value) {
        this.matsolic13 = value;
    }

    /**
     * Obt�m o valor da propriedade senha.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSENHA() {
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
    public void setSENHA(String value) {
        this.senha = value;
    }

    /**
     * Obt�m o valor da propriedade indconfirma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINDCONFIRMA() {
        return indconfirma;
    }

    /**
     * Define o valor da propriedade indconfirma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINDCONFIRMA(String value) {
        this.indconfirma = value;
    }

}
