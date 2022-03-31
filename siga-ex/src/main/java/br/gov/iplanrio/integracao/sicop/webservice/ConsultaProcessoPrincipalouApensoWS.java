
package br.gov.iplanrio.integracao.sicop.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de ConsultaProcessoPrincipalouApenso_WS complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="ConsultaProcessoPrincipalouApenso_WS">
 *   &lt;complexContent>
 *     &lt;extension base="{http://pcrj/Sma/Sicop/Grava}Message">
 *       &lt;sequence>
 *         &lt;element name="ROTINA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OPCAO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PROCPRINCIPAL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PROCAPENSO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MAT1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORG1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIT1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATOPE1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOME1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MAT2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORG2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIT2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATOPE2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOME2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MAT3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORG3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIT3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATOPE3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOME3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MAT4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORG4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIT4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATOPE4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOME4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MAT5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORG5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIT5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATOPE5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOME5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MAT6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORG6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIT6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATOPE6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOME6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MAT7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORG7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIT7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATOPE7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOME7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MAT8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORG8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIT8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATOPE8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOME8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MAT9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORG9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIT9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATOPE9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOME9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MAT10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORG10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIT10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATOPE10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOME10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MAT11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORG11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIT11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATOPE11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOME11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MAT12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORG12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIT12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATOPE12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOME12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MAT13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORG13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIT13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATOPE13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOME13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MAT14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORG14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SIT14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATOPE14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NOME14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CONCAD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "ConsultaProcessoPrincipalouApenso_WS", propOrder = {
    "rotina",
    "opcao",
    "procprincipal",
    "procapenso",
    "item1",
    "numproc1",
    "mat1",
    "org1",
    "sit1",
    "datope1",
    "nome1",
    "item2",
    "numproc2",
    "mat2",
    "org2",
    "sit2",
    "datope2",
    "nome2",
    "item3",
    "numproc3",
    "mat3",
    "org3",
    "sit3",
    "datope3",
    "nome3",
    "item4",
    "numproc4",
    "mat4",
    "org4",
    "sit4",
    "datope4",
    "nome4",
    "item5",
    "numproc5",
    "mat5",
    "org5",
    "sit5",
    "datope5",
    "nome5",
    "item6",
    "numproc6",
    "mat6",
    "org6",
    "sit6",
    "datope6",
    "nome6",
    "item7",
    "numproc7",
    "mat7",
    "org7",
    "sit7",
    "datope7",
    "nome7",
    "item8",
    "numproc8",
    "mat8",
    "org8",
    "sit8",
    "datope8",
    "nome8",
    "item9",
    "numproc9",
    "mat9",
    "org9",
    "sit9",
    "datope9",
    "nome9",
    "item10",
    "numproc10",
    "mat10",
    "org10",
    "sit10",
    "datope10",
    "nome10",
    "item11",
    "numproc11",
    "mat11",
    "org11",
    "sit11",
    "datope11",
    "nome11",
    "item12",
    "numproc12",
    "mat12",
    "org12",
    "sit12",
    "datope12",
    "nome12",
    "item13",
    "numproc13",
    "mat13",
    "org13",
    "sit13",
    "datope13",
    "nome13",
    "item14",
    "numproc14",
    "mat14",
    "org14",
    "sit14",
    "datope14",
    "nome14",
    "concad",
    "statusLine"
})
public class ConsultaProcessoPrincipalouApensoWS
    extends Message
{

    @XmlElement(name = "ROTINA")
    protected String rotina;
    @XmlElement(name = "OPCAO")
    protected String opcao;
    @XmlElement(name = "PROCPRINCIPAL")
    protected String procprincipal;
    @XmlElement(name = "PROCAPENSO")
    protected String procapenso;
    @XmlElement(name = "ITEM1")
    protected String item1;
    @XmlElement(name = "NUMPROC1")
    protected String numproc1;
    @XmlElement(name = "MAT1")
    protected String mat1;
    @XmlElement(name = "ORG1")
    protected String org1;
    @XmlElement(name = "SIT1")
    protected String sit1;
    @XmlElement(name = "DATOPE1")
    protected String datope1;
    @XmlElement(name = "NOME1")
    protected String nome1;
    @XmlElement(name = "ITEM2")
    protected String item2;
    @XmlElement(name = "NUMPROC2")
    protected String numproc2;
    @XmlElement(name = "MAT2")
    protected String mat2;
    @XmlElement(name = "ORG2")
    protected String org2;
    @XmlElement(name = "SIT2")
    protected String sit2;
    @XmlElement(name = "DATOPE2")
    protected String datope2;
    @XmlElement(name = "NOME2")
    protected String nome2;
    @XmlElement(name = "ITEM3")
    protected String item3;
    @XmlElement(name = "NUMPROC3")
    protected String numproc3;
    @XmlElement(name = "MAT3")
    protected String mat3;
    @XmlElement(name = "ORG3")
    protected String org3;
    @XmlElement(name = "SIT3")
    protected String sit3;
    @XmlElement(name = "DATOPE3")
    protected String datope3;
    @XmlElement(name = "NOME3")
    protected String nome3;
    @XmlElement(name = "ITEM4")
    protected String item4;
    @XmlElement(name = "NUMPROC4")
    protected String numproc4;
    @XmlElement(name = "MAT4")
    protected String mat4;
    @XmlElement(name = "ORG4")
    protected String org4;
    @XmlElement(name = "SIT4")
    protected String sit4;
    @XmlElement(name = "DATOPE4")
    protected String datope4;
    @XmlElement(name = "NOME4")
    protected String nome4;
    @XmlElement(name = "ITEM5")
    protected String item5;
    @XmlElement(name = "NUMPROC5")
    protected String numproc5;
    @XmlElement(name = "MAT5")
    protected String mat5;
    @XmlElement(name = "ORG5")
    protected String org5;
    @XmlElement(name = "SIT5")
    protected String sit5;
    @XmlElement(name = "DATOPE5")
    protected String datope5;
    @XmlElement(name = "NOME5")
    protected String nome5;
    @XmlElement(name = "ITEM6")
    protected String item6;
    @XmlElement(name = "NUMPROC6")
    protected String numproc6;
    @XmlElement(name = "MAT6")
    protected String mat6;
    @XmlElement(name = "ORG6")
    protected String org6;
    @XmlElement(name = "SIT6")
    protected String sit6;
    @XmlElement(name = "DATOPE6")
    protected String datope6;
    @XmlElement(name = "NOME6")
    protected String nome6;
    @XmlElement(name = "ITEM7")
    protected String item7;
    @XmlElement(name = "NUMPROC7")
    protected String numproc7;
    @XmlElement(name = "MAT7")
    protected String mat7;
    @XmlElement(name = "ORG7")
    protected String org7;
    @XmlElement(name = "SIT7")
    protected String sit7;
    @XmlElement(name = "DATOPE7")
    protected String datope7;
    @XmlElement(name = "NOME7")
    protected String nome7;
    @XmlElement(name = "ITEM8")
    protected String item8;
    @XmlElement(name = "NUMPROC8")
    protected String numproc8;
    @XmlElement(name = "MAT8")
    protected String mat8;
    @XmlElement(name = "ORG8")
    protected String org8;
    @XmlElement(name = "SIT8")
    protected String sit8;
    @XmlElement(name = "DATOPE8")
    protected String datope8;
    @XmlElement(name = "NOME8")
    protected String nome8;
    @XmlElement(name = "ITEM9")
    protected String item9;
    @XmlElement(name = "NUMPROC9")
    protected String numproc9;
    @XmlElement(name = "MAT9")
    protected String mat9;
    @XmlElement(name = "ORG9")
    protected String org9;
    @XmlElement(name = "SIT9")
    protected String sit9;
    @XmlElement(name = "DATOPE9")
    protected String datope9;
    @XmlElement(name = "NOME9")
    protected String nome9;
    @XmlElement(name = "ITEM10")
    protected String item10;
    @XmlElement(name = "NUMPROC10")
    protected String numproc10;
    @XmlElement(name = "MAT10")
    protected String mat10;
    @XmlElement(name = "ORG10")
    protected String org10;
    @XmlElement(name = "SIT10")
    protected String sit10;
    @XmlElement(name = "DATOPE10")
    protected String datope10;
    @XmlElement(name = "NOME10")
    protected String nome10;
    @XmlElement(name = "ITEM11")
    protected String item11;
    @XmlElement(name = "NUMPROC11")
    protected String numproc11;
    @XmlElement(name = "MAT11")
    protected String mat11;
    @XmlElement(name = "ORG11")
    protected String org11;
    @XmlElement(name = "SIT11")
    protected String sit11;
    @XmlElement(name = "DATOPE11")
    protected String datope11;
    @XmlElement(name = "NOME11")
    protected String nome11;
    @XmlElement(name = "ITEM12")
    protected String item12;
    @XmlElement(name = "NUMPROC12")
    protected String numproc12;
    @XmlElement(name = "MAT12")
    protected String mat12;
    @XmlElement(name = "ORG12")
    protected String org12;
    @XmlElement(name = "SIT12")
    protected String sit12;
    @XmlElement(name = "DATOPE12")
    protected String datope12;
    @XmlElement(name = "NOME12")
    protected String nome12;
    @XmlElement(name = "ITEM13")
    protected String item13;
    @XmlElement(name = "NUMPROC13")
    protected String numproc13;
    @XmlElement(name = "MAT13")
    protected String mat13;
    @XmlElement(name = "ORG13")
    protected String org13;
    @XmlElement(name = "SIT13")
    protected String sit13;
    @XmlElement(name = "DATOPE13")
    protected String datope13;
    @XmlElement(name = "NOME13")
    protected String nome13;
    @XmlElement(name = "ITEM14")
    protected String item14;
    @XmlElement(name = "NUMPROC14")
    protected String numproc14;
    @XmlElement(name = "MAT14")
    protected String mat14;
    @XmlElement(name = "ORG14")
    protected String org14;
    @XmlElement(name = "SIT14")
    protected String sit14;
    @XmlElement(name = "DATOPE14")
    protected String datope14;
    @XmlElement(name = "NOME14")
    protected String nome14;
    @XmlElement(name = "CONCAD")
    protected String concad;
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
     * Obt�m o valor da propriedade procprincipal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPROCPRINCIPAL() {
        return procprincipal;
    }

    /**
     * Define o valor da propriedade procprincipal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPROCPRINCIPAL(String value) {
        this.procprincipal = value;
    }

    /**
     * Obt�m o valor da propriedade procapenso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPROCAPENSO() {
        return procapenso;
    }

    /**
     * Define o valor da propriedade procapenso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPROCAPENSO(String value) {
        this.procapenso = value;
    }

    /**
     * Obt�m o valor da propriedade item1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEM1() {
        return item1;
    }

    /**
     * Define o valor da propriedade item1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEM1(String value) {
        this.item1 = value;
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
     * Obt�m o valor da propriedade mat1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAT1() {
        return mat1;
    }

    /**
     * Define o valor da propriedade mat1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAT1(String value) {
        this.mat1 = value;
    }

    /**
     * Obt�m o valor da propriedade org1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORG1() {
        return org1;
    }

    /**
     * Define o valor da propriedade org1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORG1(String value) {
        this.org1 = value;
    }

    /**
     * Obt�m o valor da propriedade sit1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIT1() {
        return sit1;
    }

    /**
     * Define o valor da propriedade sit1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIT1(String value) {
        this.sit1 = value;
    }

    /**
     * Obt�m o valor da propriedade datope1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATOPE1() {
        return datope1;
    }

    /**
     * Define o valor da propriedade datope1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATOPE1(String value) {
        this.datope1 = value;
    }

    /**
     * Obt�m o valor da propriedade nome1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOME1() {
        return nome1;
    }

    /**
     * Define o valor da propriedade nome1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOME1(String value) {
        this.nome1 = value;
    }

    /**
     * Obt�m o valor da propriedade item2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEM2() {
        return item2;
    }

    /**
     * Define o valor da propriedade item2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEM2(String value) {
        this.item2 = value;
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
     * Obt�m o valor da propriedade mat2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAT2() {
        return mat2;
    }

    /**
     * Define o valor da propriedade mat2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAT2(String value) {
        this.mat2 = value;
    }

    /**
     * Obt�m o valor da propriedade org2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORG2() {
        return org2;
    }

    /**
     * Define o valor da propriedade org2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORG2(String value) {
        this.org2 = value;
    }

    /**
     * Obt�m o valor da propriedade sit2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIT2() {
        return sit2;
    }

    /**
     * Define o valor da propriedade sit2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIT2(String value) {
        this.sit2 = value;
    }

    /**
     * Obt�m o valor da propriedade datope2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATOPE2() {
        return datope2;
    }

    /**
     * Define o valor da propriedade datope2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATOPE2(String value) {
        this.datope2 = value;
    }

    /**
     * Obt�m o valor da propriedade nome2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOME2() {
        return nome2;
    }

    /**
     * Define o valor da propriedade nome2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOME2(String value) {
        this.nome2 = value;
    }

    /**
     * Obt�m o valor da propriedade item3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEM3() {
        return item3;
    }

    /**
     * Define o valor da propriedade item3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEM3(String value) {
        this.item3 = value;
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
     * Obt�m o valor da propriedade mat3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAT3() {
        return mat3;
    }

    /**
     * Define o valor da propriedade mat3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAT3(String value) {
        this.mat3 = value;
    }

    /**
     * Obt�m o valor da propriedade org3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORG3() {
        return org3;
    }

    /**
     * Define o valor da propriedade org3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORG3(String value) {
        this.org3 = value;
    }

    /**
     * Obt�m o valor da propriedade sit3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIT3() {
        return sit3;
    }

    /**
     * Define o valor da propriedade sit3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIT3(String value) {
        this.sit3 = value;
    }

    /**
     * Obt�m o valor da propriedade datope3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATOPE3() {
        return datope3;
    }

    /**
     * Define o valor da propriedade datope3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATOPE3(String value) {
        this.datope3 = value;
    }

    /**
     * Obt�m o valor da propriedade nome3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOME3() {
        return nome3;
    }

    /**
     * Define o valor da propriedade nome3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOME3(String value) {
        this.nome3 = value;
    }

    /**
     * Obt�m o valor da propriedade item4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEM4() {
        return item4;
    }

    /**
     * Define o valor da propriedade item4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEM4(String value) {
        this.item4 = value;
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
     * Obt�m o valor da propriedade mat4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAT4() {
        return mat4;
    }

    /**
     * Define o valor da propriedade mat4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAT4(String value) {
        this.mat4 = value;
    }

    /**
     * Obt�m o valor da propriedade org4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORG4() {
        return org4;
    }

    /**
     * Define o valor da propriedade org4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORG4(String value) {
        this.org4 = value;
    }

    /**
     * Obt�m o valor da propriedade sit4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIT4() {
        return sit4;
    }

    /**
     * Define o valor da propriedade sit4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIT4(String value) {
        this.sit4 = value;
    }

    /**
     * Obt�m o valor da propriedade datope4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATOPE4() {
        return datope4;
    }

    /**
     * Define o valor da propriedade datope4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATOPE4(String value) {
        this.datope4 = value;
    }

    /**
     * Obt�m o valor da propriedade nome4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOME4() {
        return nome4;
    }

    /**
     * Define o valor da propriedade nome4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOME4(String value) {
        this.nome4 = value;
    }

    /**
     * Obt�m o valor da propriedade item5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEM5() {
        return item5;
    }

    /**
     * Define o valor da propriedade item5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEM5(String value) {
        this.item5 = value;
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
     * Obt�m o valor da propriedade mat5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAT5() {
        return mat5;
    }

    /**
     * Define o valor da propriedade mat5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAT5(String value) {
        this.mat5 = value;
    }

    /**
     * Obt�m o valor da propriedade org5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORG5() {
        return org5;
    }

    /**
     * Define o valor da propriedade org5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORG5(String value) {
        this.org5 = value;
    }

    /**
     * Obt�m o valor da propriedade sit5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIT5() {
        return sit5;
    }

    /**
     * Define o valor da propriedade sit5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIT5(String value) {
        this.sit5 = value;
    }

    /**
     * Obt�m o valor da propriedade datope5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATOPE5() {
        return datope5;
    }

    /**
     * Define o valor da propriedade datope5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATOPE5(String value) {
        this.datope5 = value;
    }

    /**
     * Obt�m o valor da propriedade nome5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOME5() {
        return nome5;
    }

    /**
     * Define o valor da propriedade nome5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOME5(String value) {
        this.nome5 = value;
    }

    /**
     * Obt�m o valor da propriedade item6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEM6() {
        return item6;
    }

    /**
     * Define o valor da propriedade item6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEM6(String value) {
        this.item6 = value;
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
     * Obt�m o valor da propriedade mat6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAT6() {
        return mat6;
    }

    /**
     * Define o valor da propriedade mat6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAT6(String value) {
        this.mat6 = value;
    }

    /**
     * Obt�m o valor da propriedade org6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORG6() {
        return org6;
    }

    /**
     * Define o valor da propriedade org6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORG6(String value) {
        this.org6 = value;
    }

    /**
     * Obt�m o valor da propriedade sit6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIT6() {
        return sit6;
    }

    /**
     * Define o valor da propriedade sit6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIT6(String value) {
        this.sit6 = value;
    }

    /**
     * Obt�m o valor da propriedade datope6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATOPE6() {
        return datope6;
    }

    /**
     * Define o valor da propriedade datope6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATOPE6(String value) {
        this.datope6 = value;
    }

    /**
     * Obt�m o valor da propriedade nome6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOME6() {
        return nome6;
    }

    /**
     * Define o valor da propriedade nome6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOME6(String value) {
        this.nome6 = value;
    }

    /**
     * Obt�m o valor da propriedade item7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEM7() {
        return item7;
    }

    /**
     * Define o valor da propriedade item7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEM7(String value) {
        this.item7 = value;
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
     * Obt�m o valor da propriedade mat7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAT7() {
        return mat7;
    }

    /**
     * Define o valor da propriedade mat7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAT7(String value) {
        this.mat7 = value;
    }

    /**
     * Obt�m o valor da propriedade org7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORG7() {
        return org7;
    }

    /**
     * Define o valor da propriedade org7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORG7(String value) {
        this.org7 = value;
    }

    /**
     * Obt�m o valor da propriedade sit7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIT7() {
        return sit7;
    }

    /**
     * Define o valor da propriedade sit7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIT7(String value) {
        this.sit7 = value;
    }

    /**
     * Obt�m o valor da propriedade datope7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATOPE7() {
        return datope7;
    }

    /**
     * Define o valor da propriedade datope7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATOPE7(String value) {
        this.datope7 = value;
    }

    /**
     * Obt�m o valor da propriedade nome7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOME7() {
        return nome7;
    }

    /**
     * Define o valor da propriedade nome7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOME7(String value) {
        this.nome7 = value;
    }

    /**
     * Obt�m o valor da propriedade item8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEM8() {
        return item8;
    }

    /**
     * Define o valor da propriedade item8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEM8(String value) {
        this.item8 = value;
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
     * Obt�m o valor da propriedade mat8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAT8() {
        return mat8;
    }

    /**
     * Define o valor da propriedade mat8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAT8(String value) {
        this.mat8 = value;
    }

    /**
     * Obt�m o valor da propriedade org8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORG8() {
        return org8;
    }

    /**
     * Define o valor da propriedade org8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORG8(String value) {
        this.org8 = value;
    }

    /**
     * Obt�m o valor da propriedade sit8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIT8() {
        return sit8;
    }

    /**
     * Define o valor da propriedade sit8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIT8(String value) {
        this.sit8 = value;
    }

    /**
     * Obt�m o valor da propriedade datope8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATOPE8() {
        return datope8;
    }

    /**
     * Define o valor da propriedade datope8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATOPE8(String value) {
        this.datope8 = value;
    }

    /**
     * Obt�m o valor da propriedade nome8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOME8() {
        return nome8;
    }

    /**
     * Define o valor da propriedade nome8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOME8(String value) {
        this.nome8 = value;
    }

    /**
     * Obt�m o valor da propriedade item9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEM9() {
        return item9;
    }

    /**
     * Define o valor da propriedade item9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEM9(String value) {
        this.item9 = value;
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
     * Obt�m o valor da propriedade mat9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAT9() {
        return mat9;
    }

    /**
     * Define o valor da propriedade mat9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAT9(String value) {
        this.mat9 = value;
    }

    /**
     * Obt�m o valor da propriedade org9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORG9() {
        return org9;
    }

    /**
     * Define o valor da propriedade org9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORG9(String value) {
        this.org9 = value;
    }

    /**
     * Obt�m o valor da propriedade sit9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIT9() {
        return sit9;
    }

    /**
     * Define o valor da propriedade sit9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIT9(String value) {
        this.sit9 = value;
    }

    /**
     * Obt�m o valor da propriedade datope9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATOPE9() {
        return datope9;
    }

    /**
     * Define o valor da propriedade datope9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATOPE9(String value) {
        this.datope9 = value;
    }

    /**
     * Obt�m o valor da propriedade nome9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOME9() {
        return nome9;
    }

    /**
     * Define o valor da propriedade nome9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOME9(String value) {
        this.nome9 = value;
    }

    /**
     * Obt�m o valor da propriedade item10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEM10() {
        return item10;
    }

    /**
     * Define o valor da propriedade item10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEM10(String value) {
        this.item10 = value;
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
     * Obt�m o valor da propriedade mat10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAT10() {
        return mat10;
    }

    /**
     * Define o valor da propriedade mat10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAT10(String value) {
        this.mat10 = value;
    }

    /**
     * Obt�m o valor da propriedade org10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORG10() {
        return org10;
    }

    /**
     * Define o valor da propriedade org10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORG10(String value) {
        this.org10 = value;
    }

    /**
     * Obt�m o valor da propriedade sit10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIT10() {
        return sit10;
    }

    /**
     * Define o valor da propriedade sit10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIT10(String value) {
        this.sit10 = value;
    }

    /**
     * Obt�m o valor da propriedade datope10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATOPE10() {
        return datope10;
    }

    /**
     * Define o valor da propriedade datope10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATOPE10(String value) {
        this.datope10 = value;
    }

    /**
     * Obt�m o valor da propriedade nome10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOME10() {
        return nome10;
    }

    /**
     * Define o valor da propriedade nome10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOME10(String value) {
        this.nome10 = value;
    }

    /**
     * Obt�m o valor da propriedade item11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEM11() {
        return item11;
    }

    /**
     * Define o valor da propriedade item11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEM11(String value) {
        this.item11 = value;
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
     * Obt�m o valor da propriedade mat11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAT11() {
        return mat11;
    }

    /**
     * Define o valor da propriedade mat11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAT11(String value) {
        this.mat11 = value;
    }

    /**
     * Obt�m o valor da propriedade org11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORG11() {
        return org11;
    }

    /**
     * Define o valor da propriedade org11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORG11(String value) {
        this.org11 = value;
    }

    /**
     * Obt�m o valor da propriedade sit11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIT11() {
        return sit11;
    }

    /**
     * Define o valor da propriedade sit11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIT11(String value) {
        this.sit11 = value;
    }

    /**
     * Obt�m o valor da propriedade datope11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATOPE11() {
        return datope11;
    }

    /**
     * Define o valor da propriedade datope11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATOPE11(String value) {
        this.datope11 = value;
    }

    /**
     * Obt�m o valor da propriedade nome11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOME11() {
        return nome11;
    }

    /**
     * Define o valor da propriedade nome11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOME11(String value) {
        this.nome11 = value;
    }

    /**
     * Obt�m o valor da propriedade item12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEM12() {
        return item12;
    }

    /**
     * Define o valor da propriedade item12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEM12(String value) {
        this.item12 = value;
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
     * Obt�m o valor da propriedade mat12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAT12() {
        return mat12;
    }

    /**
     * Define o valor da propriedade mat12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAT12(String value) {
        this.mat12 = value;
    }

    /**
     * Obt�m o valor da propriedade org12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORG12() {
        return org12;
    }

    /**
     * Define o valor da propriedade org12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORG12(String value) {
        this.org12 = value;
    }

    /**
     * Obt�m o valor da propriedade sit12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIT12() {
        return sit12;
    }

    /**
     * Define o valor da propriedade sit12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIT12(String value) {
        this.sit12 = value;
    }

    /**
     * Obt�m o valor da propriedade datope12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATOPE12() {
        return datope12;
    }

    /**
     * Define o valor da propriedade datope12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATOPE12(String value) {
        this.datope12 = value;
    }

    /**
     * Obt�m o valor da propriedade nome12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOME12() {
        return nome12;
    }

    /**
     * Define o valor da propriedade nome12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOME12(String value) {
        this.nome12 = value;
    }

    /**
     * Obt�m o valor da propriedade item13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEM13() {
        return item13;
    }

    /**
     * Define o valor da propriedade item13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEM13(String value) {
        this.item13 = value;
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
     * Obt�m o valor da propriedade mat13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAT13() {
        return mat13;
    }

    /**
     * Define o valor da propriedade mat13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAT13(String value) {
        this.mat13 = value;
    }

    /**
     * Obt�m o valor da propriedade org13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORG13() {
        return org13;
    }

    /**
     * Define o valor da propriedade org13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORG13(String value) {
        this.org13 = value;
    }

    /**
     * Obt�m o valor da propriedade sit13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIT13() {
        return sit13;
    }

    /**
     * Define o valor da propriedade sit13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIT13(String value) {
        this.sit13 = value;
    }

    /**
     * Obt�m o valor da propriedade datope13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATOPE13() {
        return datope13;
    }

    /**
     * Define o valor da propriedade datope13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATOPE13(String value) {
        this.datope13 = value;
    }

    /**
     * Obt�m o valor da propriedade nome13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOME13() {
        return nome13;
    }

    /**
     * Define o valor da propriedade nome13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOME13(String value) {
        this.nome13 = value;
    }

    /**
     * Obt�m o valor da propriedade item14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEM14() {
        return item14;
    }

    /**
     * Define o valor da propriedade item14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEM14(String value) {
        this.item14 = value;
    }

    /**
     * Obt�m o valor da propriedade numproc14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC14() {
        return numproc14;
    }

    /**
     * Define o valor da propriedade numproc14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC14(String value) {
        this.numproc14 = value;
    }

    /**
     * Obt�m o valor da propriedade mat14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAT14() {
        return mat14;
    }

    /**
     * Define o valor da propriedade mat14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAT14(String value) {
        this.mat14 = value;
    }

    /**
     * Obt�m o valor da propriedade org14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORG14() {
        return org14;
    }

    /**
     * Define o valor da propriedade org14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORG14(String value) {
        this.org14 = value;
    }

    /**
     * Obt�m o valor da propriedade sit14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIT14() {
        return sit14;
    }

    /**
     * Define o valor da propriedade sit14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIT14(String value) {
        this.sit14 = value;
    }

    /**
     * Obt�m o valor da propriedade datope14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATOPE14() {
        return datope14;
    }

    /**
     * Define o valor da propriedade datope14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATOPE14(String value) {
        this.datope14 = value;
    }

    /**
     * Obt�m o valor da propriedade nome14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOME14() {
        return nome14;
    }

    /**
     * Define o valor da propriedade nome14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOME14(String value) {
        this.nome14 = value;
    }

    /**
     * Obt�m o valor da propriedade concad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCONCAD() {
        return concad;
    }

    /**
     * Define o valor da propriedade concad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCONCAD(String value) {
        this.concad = value;
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
