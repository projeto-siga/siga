
package br.gov.iplanrio.integracao.sicop.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de ConsultaTodaTramitacaoProcesso_WS complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="ConsultaTodaTramitacaoProcesso_WS">
 *   &lt;complexContent>
 *     &lt;extension base="{http://pcrj/Sma/Sicop/Grava}Message">
 *       &lt;sequence>
 *         &lt;element name="ROTINA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NUMPROC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATDIG1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATREC1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDEST1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDESP1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GUIA1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATDIG2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATREC2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDEST2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDESP2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GUIA2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATDIG3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATREC3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDEST3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDESP3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GUIA3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATDIG4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATREC4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDEST4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDESP4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GUIA4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATDIG5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATREC5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDEST5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDESP5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GUIA5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATDIG6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATREC6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDEST6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDESP6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GUIA6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATDIG7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATREC7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDEST7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDESP7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GUIA7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATDIG8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATREC8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDEST8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDESP8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GUIA8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATDIG9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATREC9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDEST9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDESP9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GUIA9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATDIG10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATREC10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDEST10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDESP10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GUIA10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATDIG11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATREC11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDEST11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDESP11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GUIA11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATDIG12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATREC12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDEST12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDESP12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GUIA12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATDIG13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATREC13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDEST13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDESP13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GUIA13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATDIG14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MATREC14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORGDEST14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DATDESP14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SEQ14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GUIA14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESPACHO14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "ConsultaTodaTramitacaoProcesso_WS", propOrder = {
    "rotina",
    "numproc",
    "item1",
    "matdig1",
    "matrec1",
    "orgdest1",
    "datdesp1",
    "seq1",
    "guia1",
    "despacho1",
    "item2",
    "matdig2",
    "matrec2",
    "orgdest2",
    "datdesp2",
    "seq2",
    "guia2",
    "despacho2",
    "item3",
    "matdig3",
    "matrec3",
    "orgdest3",
    "datdesp3",
    "seq3",
    "guia3",
    "despacho3",
    "item4",
    "matdig4",
    "matrec4",
    "orgdest4",
    "datdesp4",
    "seq4",
    "guia4",
    "despacho4",
    "item5",
    "matdig5",
    "matrec5",
    "orgdest5",
    "datdesp5",
    "seq5",
    "guia5",
    "despacho5",
    "item6",
    "matdig6",
    "matrec6",
    "orgdest6",
    "datdesp6",
    "seq6",
    "guia6",
    "despacho6",
    "item7",
    "matdig7",
    "matrec7",
    "orgdest7",
    "datdesp7",
    "seq7",
    "guia7",
    "despacho7",
    "item8",
    "matdig8",
    "matrec8",
    "orgdest8",
    "datdesp8",
    "seq8",
    "guia8",
    "despacho8",
    "item9",
    "matdig9",
    "matrec9",
    "orgdest9",
    "datdesp9",
    "seq9",
    "guia9",
    "despacho9",
    "item10",
    "matdig10",
    "matrec10",
    "orgdest10",
    "datdesp10",
    "seq10",
    "guia10",
    "despacho10",
    "item11",
    "matdig11",
    "matrec11",
    "orgdest11",
    "datdesp11",
    "seq11",
    "guia11",
    "despacho11",
    "item12",
    "matdig12",
    "matrec12",
    "orgdest12",
    "datdesp12",
    "seq12",
    "guia12",
    "despacho12",
    "item13",
    "matdig13",
    "matrec13",
    "orgdest13",
    "datdesp13",
    "seq13",
    "guia13",
    "despacho13",
    "item14",
    "matdig14",
    "matrec14",
    "orgdest14",
    "datdesp14",
    "seq14",
    "guia14",
    "despacho14",
    "concad",
    "statusLine"
})
public class ConsultaTodaTramitacaoProcessoWS
    extends Message
{

    @XmlElement(name = "ROTINA")
    protected String rotina;
    @XmlElement(name = "NUMPROC")
    protected String numproc;
    @XmlElement(name = "ITEM1")
    protected String item1;
    @XmlElement(name = "MATDIG1")
    protected String matdig1;
    @XmlElement(name = "MATREC1")
    protected String matrec1;
    @XmlElement(name = "ORGDEST1")
    protected String orgdest1;
    @XmlElement(name = "DATDESP1")
    protected String datdesp1;
    @XmlElement(name = "SEQ1")
    protected String seq1;
    @XmlElement(name = "GUIA1")
    protected String guia1;
    @XmlElement(name = "DESPACHO1")
    protected String despacho1;
    @XmlElement(name = "ITEM2")
    protected String item2;
    @XmlElement(name = "MATDIG2")
    protected String matdig2;
    @XmlElement(name = "MATREC2")
    protected String matrec2;
    @XmlElement(name = "ORGDEST2")
    protected String orgdest2;
    @XmlElement(name = "DATDESP2")
    protected String datdesp2;
    @XmlElement(name = "SEQ2")
    protected String seq2;
    @XmlElement(name = "GUIA2")
    protected String guia2;
    @XmlElement(name = "DESPACHO2")
    protected String despacho2;
    @XmlElement(name = "ITEM3")
    protected String item3;
    @XmlElement(name = "MATDIG3")
    protected String matdig3;
    @XmlElement(name = "MATREC3")
    protected String matrec3;
    @XmlElement(name = "ORGDEST3")
    protected String orgdest3;
    @XmlElement(name = "DATDESP3")
    protected String datdesp3;
    @XmlElement(name = "SEQ3")
    protected String seq3;
    @XmlElement(name = "GUIA3")
    protected String guia3;
    @XmlElement(name = "DESPACHO3")
    protected String despacho3;
    @XmlElement(name = "ITEM4")
    protected String item4;
    @XmlElement(name = "MATDIG4")
    protected String matdig4;
    @XmlElement(name = "MATREC4")
    protected String matrec4;
    @XmlElement(name = "ORGDEST4")
    protected String orgdest4;
    @XmlElement(name = "DATDESP4")
    protected String datdesp4;
    @XmlElement(name = "SEQ4")
    protected String seq4;
    @XmlElement(name = "GUIA4")
    protected String guia4;
    @XmlElement(name = "DESPACHO4")
    protected String despacho4;
    @XmlElement(name = "ITEM5")
    protected String item5;
    @XmlElement(name = "MATDIG5")
    protected String matdig5;
    @XmlElement(name = "MATREC5")
    protected String matrec5;
    @XmlElement(name = "ORGDEST5")
    protected String orgdest5;
    @XmlElement(name = "DATDESP5")
    protected String datdesp5;
    @XmlElement(name = "SEQ5")
    protected String seq5;
    @XmlElement(name = "GUIA5")
    protected String guia5;
    @XmlElement(name = "DESPACHO5")
    protected String despacho5;
    @XmlElement(name = "ITEM6")
    protected String item6;
    @XmlElement(name = "MATDIG6")
    protected String matdig6;
    @XmlElement(name = "MATREC6")
    protected String matrec6;
    @XmlElement(name = "ORGDEST6")
    protected String orgdest6;
    @XmlElement(name = "DATDESP6")
    protected String datdesp6;
    @XmlElement(name = "SEQ6")
    protected String seq6;
    @XmlElement(name = "GUIA6")
    protected String guia6;
    @XmlElement(name = "DESPACHO6")
    protected String despacho6;
    @XmlElement(name = "ITEM7")
    protected String item7;
    @XmlElement(name = "MATDIG7")
    protected String matdig7;
    @XmlElement(name = "MATREC7")
    protected String matrec7;
    @XmlElement(name = "ORGDEST7")
    protected String orgdest7;
    @XmlElement(name = "DATDESP7")
    protected String datdesp7;
    @XmlElement(name = "SEQ7")
    protected String seq7;
    @XmlElement(name = "GUIA7")
    protected String guia7;
    @XmlElement(name = "DESPACHO7")
    protected String despacho7;
    @XmlElement(name = "ITEM8")
    protected String item8;
    @XmlElement(name = "MATDIG8")
    protected String matdig8;
    @XmlElement(name = "MATREC8")
    protected String matrec8;
    @XmlElement(name = "ORGDEST8")
    protected String orgdest8;
    @XmlElement(name = "DATDESP8")
    protected String datdesp8;
    @XmlElement(name = "SEQ8")
    protected String seq8;
    @XmlElement(name = "GUIA8")
    protected String guia8;
    @XmlElement(name = "DESPACHO8")
    protected String despacho8;
    @XmlElement(name = "ITEM9")
    protected String item9;
    @XmlElement(name = "MATDIG9")
    protected String matdig9;
    @XmlElement(name = "MATREC9")
    protected String matrec9;
    @XmlElement(name = "ORGDEST9")
    protected String orgdest9;
    @XmlElement(name = "DATDESP9")
    protected String datdesp9;
    @XmlElement(name = "SEQ9")
    protected String seq9;
    @XmlElement(name = "GUIA9")
    protected String guia9;
    @XmlElement(name = "DESPACHO9")
    protected String despacho9;
    @XmlElement(name = "ITEM10")
    protected String item10;
    @XmlElement(name = "MATDIG10")
    protected String matdig10;
    @XmlElement(name = "MATREC10")
    protected String matrec10;
    @XmlElement(name = "ORGDEST10")
    protected String orgdest10;
    @XmlElement(name = "DATDESP10")
    protected String datdesp10;
    @XmlElement(name = "SEQ10")
    protected String seq10;
    @XmlElement(name = "GUIA10")
    protected String guia10;
    @XmlElement(name = "DESPACHO10")
    protected String despacho10;
    @XmlElement(name = "ITEM11")
    protected String item11;
    @XmlElement(name = "MATDIG11")
    protected String matdig11;
    @XmlElement(name = "MATREC11")
    protected String matrec11;
    @XmlElement(name = "ORGDEST11")
    protected String orgdest11;
    @XmlElement(name = "DATDESP11")
    protected String datdesp11;
    @XmlElement(name = "SEQ11")
    protected String seq11;
    @XmlElement(name = "GUIA11")
    protected String guia11;
    @XmlElement(name = "DESPACHO11")
    protected String despacho11;
    @XmlElement(name = "ITEM12")
    protected String item12;
    @XmlElement(name = "MATDIG12")
    protected String matdig12;
    @XmlElement(name = "MATREC12")
    protected String matrec12;
    @XmlElement(name = "ORGDEST12")
    protected String orgdest12;
    @XmlElement(name = "DATDESP12")
    protected String datdesp12;
    @XmlElement(name = "SEQ12")
    protected String seq12;
    @XmlElement(name = "GUIA12")
    protected String guia12;
    @XmlElement(name = "DESPACHO12")
    protected String despacho12;
    @XmlElement(name = "ITEM13")
    protected String item13;
    @XmlElement(name = "MATDIG13")
    protected String matdig13;
    @XmlElement(name = "MATREC13")
    protected String matrec13;
    @XmlElement(name = "ORGDEST13")
    protected String orgdest13;
    @XmlElement(name = "DATDESP13")
    protected String datdesp13;
    @XmlElement(name = "SEQ13")
    protected String seq13;
    @XmlElement(name = "GUIA13")
    protected String guia13;
    @XmlElement(name = "DESPACHO13")
    protected String despacho13;
    @XmlElement(name = "ITEM14")
    protected String item14;
    @XmlElement(name = "MATDIG14")
    protected String matdig14;
    @XmlElement(name = "MATREC14")
    protected String matrec14;
    @XmlElement(name = "ORGDEST14")
    protected String orgdest14;
    @XmlElement(name = "DATDESP14")
    protected String datdesp14;
    @XmlElement(name = "SEQ14")
    protected String seq14;
    @XmlElement(name = "GUIA14")
    protected String guia14;
    @XmlElement(name = "DESPACHO14")
    protected String despacho14;
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
     * Obt�m o valor da propriedade numproc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMPROC() {
        return numproc;
    }

    /**
     * Define o valor da propriedade numproc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMPROC(String value) {
        this.numproc = value;
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
     * Obt�m o valor da propriedade matdig1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATDIG1() {
        return matdig1;
    }

    /**
     * Define o valor da propriedade matdig1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATDIG1(String value) {
        this.matdig1 = value;
    }

    /**
     * Obt�m o valor da propriedade matrec1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATREC1() {
        return matrec1;
    }

    /**
     * Define o valor da propriedade matrec1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATREC1(String value) {
        this.matrec1 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdest1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDEST1() {
        return orgdest1;
    }

    /**
     * Define o valor da propriedade orgdest1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDEST1(String value) {
        this.orgdest1 = value;
    }

    /**
     * Obt�m o valor da propriedade datdesp1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDESP1() {
        return datdesp1;
    }

    /**
     * Define o valor da propriedade datdesp1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDESP1(String value) {
        this.datdesp1 = value;
    }

    /**
     * Obt�m o valor da propriedade seq1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ1() {
        return seq1;
    }

    /**
     * Define o valor da propriedade seq1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ1(String value) {
        this.seq1 = value;
    }

    /**
     * Obt�m o valor da propriedade guia1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGUIA1() {
        return guia1;
    }

    /**
     * Define o valor da propriedade guia1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGUIA1(String value) {
        this.guia1 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO1() {
        return despacho1;
    }

    /**
     * Define o valor da propriedade despacho1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO1(String value) {
        this.despacho1 = value;
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
     * Obt�m o valor da propriedade matdig2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATDIG2() {
        return matdig2;
    }

    /**
     * Define o valor da propriedade matdig2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATDIG2(String value) {
        this.matdig2 = value;
    }

    /**
     * Obt�m o valor da propriedade matrec2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATREC2() {
        return matrec2;
    }

    /**
     * Define o valor da propriedade matrec2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATREC2(String value) {
        this.matrec2 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdest2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDEST2() {
        return orgdest2;
    }

    /**
     * Define o valor da propriedade orgdest2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDEST2(String value) {
        this.orgdest2 = value;
    }

    /**
     * Obt�m o valor da propriedade datdesp2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDESP2() {
        return datdesp2;
    }

    /**
     * Define o valor da propriedade datdesp2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDESP2(String value) {
        this.datdesp2 = value;
    }

    /**
     * Obt�m o valor da propriedade seq2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ2() {
        return seq2;
    }

    /**
     * Define o valor da propriedade seq2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ2(String value) {
        this.seq2 = value;
    }

    /**
     * Obt�m o valor da propriedade guia2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGUIA2() {
        return guia2;
    }

    /**
     * Define o valor da propriedade guia2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGUIA2(String value) {
        this.guia2 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO2() {
        return despacho2;
    }

    /**
     * Define o valor da propriedade despacho2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO2(String value) {
        this.despacho2 = value;
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
     * Obt�m o valor da propriedade matdig3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATDIG3() {
        return matdig3;
    }

    /**
     * Define o valor da propriedade matdig3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATDIG3(String value) {
        this.matdig3 = value;
    }

    /**
     * Obt�m o valor da propriedade matrec3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATREC3() {
        return matrec3;
    }

    /**
     * Define o valor da propriedade matrec3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATREC3(String value) {
        this.matrec3 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdest3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDEST3() {
        return orgdest3;
    }

    /**
     * Define o valor da propriedade orgdest3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDEST3(String value) {
        this.orgdest3 = value;
    }

    /**
     * Obt�m o valor da propriedade datdesp3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDESP3() {
        return datdesp3;
    }

    /**
     * Define o valor da propriedade datdesp3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDESP3(String value) {
        this.datdesp3 = value;
    }

    /**
     * Obt�m o valor da propriedade seq3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ3() {
        return seq3;
    }

    /**
     * Define o valor da propriedade seq3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ3(String value) {
        this.seq3 = value;
    }

    /**
     * Obt�m o valor da propriedade guia3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGUIA3() {
        return guia3;
    }

    /**
     * Define o valor da propriedade guia3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGUIA3(String value) {
        this.guia3 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO3() {
        return despacho3;
    }

    /**
     * Define o valor da propriedade despacho3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO3(String value) {
        this.despacho3 = value;
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
     * Obt�m o valor da propriedade matdig4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATDIG4() {
        return matdig4;
    }

    /**
     * Define o valor da propriedade matdig4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATDIG4(String value) {
        this.matdig4 = value;
    }

    /**
     * Obt�m o valor da propriedade matrec4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATREC4() {
        return matrec4;
    }

    /**
     * Define o valor da propriedade matrec4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATREC4(String value) {
        this.matrec4 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdest4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDEST4() {
        return orgdest4;
    }

    /**
     * Define o valor da propriedade orgdest4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDEST4(String value) {
        this.orgdest4 = value;
    }

    /**
     * Obt�m o valor da propriedade datdesp4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDESP4() {
        return datdesp4;
    }

    /**
     * Define o valor da propriedade datdesp4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDESP4(String value) {
        this.datdesp4 = value;
    }

    /**
     * Obt�m o valor da propriedade seq4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ4() {
        return seq4;
    }

    /**
     * Define o valor da propriedade seq4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ4(String value) {
        this.seq4 = value;
    }

    /**
     * Obt�m o valor da propriedade guia4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGUIA4() {
        return guia4;
    }

    /**
     * Define o valor da propriedade guia4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGUIA4(String value) {
        this.guia4 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO4() {
        return despacho4;
    }

    /**
     * Define o valor da propriedade despacho4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO4(String value) {
        this.despacho4 = value;
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
     * Obt�m o valor da propriedade matdig5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATDIG5() {
        return matdig5;
    }

    /**
     * Define o valor da propriedade matdig5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATDIG5(String value) {
        this.matdig5 = value;
    }

    /**
     * Obt�m o valor da propriedade matrec5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATREC5() {
        return matrec5;
    }

    /**
     * Define o valor da propriedade matrec5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATREC5(String value) {
        this.matrec5 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdest5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDEST5() {
        return orgdest5;
    }

    /**
     * Define o valor da propriedade orgdest5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDEST5(String value) {
        this.orgdest5 = value;
    }

    /**
     * Obt�m o valor da propriedade datdesp5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDESP5() {
        return datdesp5;
    }

    /**
     * Define o valor da propriedade datdesp5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDESP5(String value) {
        this.datdesp5 = value;
    }

    /**
     * Obt�m o valor da propriedade seq5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ5() {
        return seq5;
    }

    /**
     * Define o valor da propriedade seq5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ5(String value) {
        this.seq5 = value;
    }

    /**
     * Obt�m o valor da propriedade guia5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGUIA5() {
        return guia5;
    }

    /**
     * Define o valor da propriedade guia5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGUIA5(String value) {
        this.guia5 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO5() {
        return despacho5;
    }

    /**
     * Define o valor da propriedade despacho5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO5(String value) {
        this.despacho5 = value;
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
     * Obt�m o valor da propriedade matdig6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATDIG6() {
        return matdig6;
    }

    /**
     * Define o valor da propriedade matdig6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATDIG6(String value) {
        this.matdig6 = value;
    }

    /**
     * Obt�m o valor da propriedade matrec6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATREC6() {
        return matrec6;
    }

    /**
     * Define o valor da propriedade matrec6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATREC6(String value) {
        this.matrec6 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdest6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDEST6() {
        return orgdest6;
    }

    /**
     * Define o valor da propriedade orgdest6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDEST6(String value) {
        this.orgdest6 = value;
    }

    /**
     * Obt�m o valor da propriedade datdesp6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDESP6() {
        return datdesp6;
    }

    /**
     * Define o valor da propriedade datdesp6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDESP6(String value) {
        this.datdesp6 = value;
    }

    /**
     * Obt�m o valor da propriedade seq6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ6() {
        return seq6;
    }

    /**
     * Define o valor da propriedade seq6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ6(String value) {
        this.seq6 = value;
    }

    /**
     * Obt�m o valor da propriedade guia6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGUIA6() {
        return guia6;
    }

    /**
     * Define o valor da propriedade guia6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGUIA6(String value) {
        this.guia6 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO6() {
        return despacho6;
    }

    /**
     * Define o valor da propriedade despacho6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO6(String value) {
        this.despacho6 = value;
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
     * Obt�m o valor da propriedade matdig7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATDIG7() {
        return matdig7;
    }

    /**
     * Define o valor da propriedade matdig7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATDIG7(String value) {
        this.matdig7 = value;
    }

    /**
     * Obt�m o valor da propriedade matrec7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATREC7() {
        return matrec7;
    }

    /**
     * Define o valor da propriedade matrec7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATREC7(String value) {
        this.matrec7 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdest7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDEST7() {
        return orgdest7;
    }

    /**
     * Define o valor da propriedade orgdest7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDEST7(String value) {
        this.orgdest7 = value;
    }

    /**
     * Obt�m o valor da propriedade datdesp7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDESP7() {
        return datdesp7;
    }

    /**
     * Define o valor da propriedade datdesp7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDESP7(String value) {
        this.datdesp7 = value;
    }

    /**
     * Obt�m o valor da propriedade seq7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ7() {
        return seq7;
    }

    /**
     * Define o valor da propriedade seq7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ7(String value) {
        this.seq7 = value;
    }

    /**
     * Obt�m o valor da propriedade guia7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGUIA7() {
        return guia7;
    }

    /**
     * Define o valor da propriedade guia7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGUIA7(String value) {
        this.guia7 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO7() {
        return despacho7;
    }

    /**
     * Define o valor da propriedade despacho7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO7(String value) {
        this.despacho7 = value;
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
     * Obt�m o valor da propriedade matdig8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATDIG8() {
        return matdig8;
    }

    /**
     * Define o valor da propriedade matdig8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATDIG8(String value) {
        this.matdig8 = value;
    }

    /**
     * Obt�m o valor da propriedade matrec8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATREC8() {
        return matrec8;
    }

    /**
     * Define o valor da propriedade matrec8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATREC8(String value) {
        this.matrec8 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdest8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDEST8() {
        return orgdest8;
    }

    /**
     * Define o valor da propriedade orgdest8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDEST8(String value) {
        this.orgdest8 = value;
    }

    /**
     * Obt�m o valor da propriedade datdesp8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDESP8() {
        return datdesp8;
    }

    /**
     * Define o valor da propriedade datdesp8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDESP8(String value) {
        this.datdesp8 = value;
    }

    /**
     * Obt�m o valor da propriedade seq8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ8() {
        return seq8;
    }

    /**
     * Define o valor da propriedade seq8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ8(String value) {
        this.seq8 = value;
    }

    /**
     * Obt�m o valor da propriedade guia8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGUIA8() {
        return guia8;
    }

    /**
     * Define o valor da propriedade guia8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGUIA8(String value) {
        this.guia8 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO8() {
        return despacho8;
    }

    /**
     * Define o valor da propriedade despacho8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO8(String value) {
        this.despacho8 = value;
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
     * Obt�m o valor da propriedade matdig9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATDIG9() {
        return matdig9;
    }

    /**
     * Define o valor da propriedade matdig9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATDIG9(String value) {
        this.matdig9 = value;
    }

    /**
     * Obt�m o valor da propriedade matrec9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATREC9() {
        return matrec9;
    }

    /**
     * Define o valor da propriedade matrec9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATREC9(String value) {
        this.matrec9 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdest9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDEST9() {
        return orgdest9;
    }

    /**
     * Define o valor da propriedade orgdest9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDEST9(String value) {
        this.orgdest9 = value;
    }

    /**
     * Obt�m o valor da propriedade datdesp9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDESP9() {
        return datdesp9;
    }

    /**
     * Define o valor da propriedade datdesp9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDESP9(String value) {
        this.datdesp9 = value;
    }

    /**
     * Obt�m o valor da propriedade seq9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ9() {
        return seq9;
    }

    /**
     * Define o valor da propriedade seq9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ9(String value) {
        this.seq9 = value;
    }

    /**
     * Obt�m o valor da propriedade guia9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGUIA9() {
        return guia9;
    }

    /**
     * Define o valor da propriedade guia9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGUIA9(String value) {
        this.guia9 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO9() {
        return despacho9;
    }

    /**
     * Define o valor da propriedade despacho9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO9(String value) {
        this.despacho9 = value;
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
     * Obt�m o valor da propriedade matdig10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATDIG10() {
        return matdig10;
    }

    /**
     * Define o valor da propriedade matdig10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATDIG10(String value) {
        this.matdig10 = value;
    }

    /**
     * Obt�m o valor da propriedade matrec10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATREC10() {
        return matrec10;
    }

    /**
     * Define o valor da propriedade matrec10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATREC10(String value) {
        this.matrec10 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdest10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDEST10() {
        return orgdest10;
    }

    /**
     * Define o valor da propriedade orgdest10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDEST10(String value) {
        this.orgdest10 = value;
    }

    /**
     * Obt�m o valor da propriedade datdesp10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDESP10() {
        return datdesp10;
    }

    /**
     * Define o valor da propriedade datdesp10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDESP10(String value) {
        this.datdesp10 = value;
    }

    /**
     * Obt�m o valor da propriedade seq10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ10() {
        return seq10;
    }

    /**
     * Define o valor da propriedade seq10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ10(String value) {
        this.seq10 = value;
    }

    /**
     * Obt�m o valor da propriedade guia10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGUIA10() {
        return guia10;
    }

    /**
     * Define o valor da propriedade guia10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGUIA10(String value) {
        this.guia10 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO10() {
        return despacho10;
    }

    /**
     * Define o valor da propriedade despacho10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO10(String value) {
        this.despacho10 = value;
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
     * Obt�m o valor da propriedade matdig11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATDIG11() {
        return matdig11;
    }

    /**
     * Define o valor da propriedade matdig11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATDIG11(String value) {
        this.matdig11 = value;
    }

    /**
     * Obt�m o valor da propriedade matrec11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATREC11() {
        return matrec11;
    }

    /**
     * Define o valor da propriedade matrec11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATREC11(String value) {
        this.matrec11 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdest11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDEST11() {
        return orgdest11;
    }

    /**
     * Define o valor da propriedade orgdest11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDEST11(String value) {
        this.orgdest11 = value;
    }

    /**
     * Obt�m o valor da propriedade datdesp11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDESP11() {
        return datdesp11;
    }

    /**
     * Define o valor da propriedade datdesp11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDESP11(String value) {
        this.datdesp11 = value;
    }

    /**
     * Obt�m o valor da propriedade seq11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ11() {
        return seq11;
    }

    /**
     * Define o valor da propriedade seq11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ11(String value) {
        this.seq11 = value;
    }

    /**
     * Obt�m o valor da propriedade guia11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGUIA11() {
        return guia11;
    }

    /**
     * Define o valor da propriedade guia11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGUIA11(String value) {
        this.guia11 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO11() {
        return despacho11;
    }

    /**
     * Define o valor da propriedade despacho11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO11(String value) {
        this.despacho11 = value;
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
     * Obt�m o valor da propriedade matdig12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATDIG12() {
        return matdig12;
    }

    /**
     * Define o valor da propriedade matdig12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATDIG12(String value) {
        this.matdig12 = value;
    }

    /**
     * Obt�m o valor da propriedade matrec12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATREC12() {
        return matrec12;
    }

    /**
     * Define o valor da propriedade matrec12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATREC12(String value) {
        this.matrec12 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdest12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDEST12() {
        return orgdest12;
    }

    /**
     * Define o valor da propriedade orgdest12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDEST12(String value) {
        this.orgdest12 = value;
    }

    /**
     * Obt�m o valor da propriedade datdesp12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDESP12() {
        return datdesp12;
    }

    /**
     * Define o valor da propriedade datdesp12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDESP12(String value) {
        this.datdesp12 = value;
    }

    /**
     * Obt�m o valor da propriedade seq12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ12() {
        return seq12;
    }

    /**
     * Define o valor da propriedade seq12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ12(String value) {
        this.seq12 = value;
    }

    /**
     * Obt�m o valor da propriedade guia12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGUIA12() {
        return guia12;
    }

    /**
     * Define o valor da propriedade guia12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGUIA12(String value) {
        this.guia12 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO12() {
        return despacho12;
    }

    /**
     * Define o valor da propriedade despacho12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO12(String value) {
        this.despacho12 = value;
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
     * Obt�m o valor da propriedade matdig13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATDIG13() {
        return matdig13;
    }

    /**
     * Define o valor da propriedade matdig13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATDIG13(String value) {
        this.matdig13 = value;
    }

    /**
     * Obt�m o valor da propriedade matrec13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATREC13() {
        return matrec13;
    }

    /**
     * Define o valor da propriedade matrec13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATREC13(String value) {
        this.matrec13 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdest13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDEST13() {
        return orgdest13;
    }

    /**
     * Define o valor da propriedade orgdest13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDEST13(String value) {
        this.orgdest13 = value;
    }

    /**
     * Obt�m o valor da propriedade datdesp13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDESP13() {
        return datdesp13;
    }

    /**
     * Define o valor da propriedade datdesp13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDESP13(String value) {
        this.datdesp13 = value;
    }

    /**
     * Obt�m o valor da propriedade seq13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ13() {
        return seq13;
    }

    /**
     * Define o valor da propriedade seq13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ13(String value) {
        this.seq13 = value;
    }

    /**
     * Obt�m o valor da propriedade guia13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGUIA13() {
        return guia13;
    }

    /**
     * Define o valor da propriedade guia13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGUIA13(String value) {
        this.guia13 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO13() {
        return despacho13;
    }

    /**
     * Define o valor da propriedade despacho13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO13(String value) {
        this.despacho13 = value;
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
     * Obt�m o valor da propriedade matdig14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATDIG14() {
        return matdig14;
    }

    /**
     * Define o valor da propriedade matdig14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATDIG14(String value) {
        this.matdig14 = value;
    }

    /**
     * Obt�m o valor da propriedade matrec14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMATREC14() {
        return matrec14;
    }

    /**
     * Define o valor da propriedade matrec14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMATREC14(String value) {
        this.matrec14 = value;
    }

    /**
     * Obt�m o valor da propriedade orgdest14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGDEST14() {
        return orgdest14;
    }

    /**
     * Define o valor da propriedade orgdest14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGDEST14(String value) {
        this.orgdest14 = value;
    }

    /**
     * Obt�m o valor da propriedade datdesp14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATDESP14() {
        return datdesp14;
    }

    /**
     * Define o valor da propriedade datdesp14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATDESP14(String value) {
        this.datdesp14 = value;
    }

    /**
     * Obt�m o valor da propriedade seq14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEQ14() {
        return seq14;
    }

    /**
     * Define o valor da propriedade seq14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEQ14(String value) {
        this.seq14 = value;
    }

    /**
     * Obt�m o valor da propriedade guia14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGUIA14() {
        return guia14;
    }

    /**
     * Define o valor da propriedade guia14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGUIA14(String value) {
        this.guia14 = value;
    }

    /**
     * Obt�m o valor da propriedade despacho14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESPACHO14() {
        return despacho14;
    }

    /**
     * Define o valor da propriedade despacho14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESPACHO14(String value) {
        this.despacho14 = value;
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
