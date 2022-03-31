
package br.gov.iplanrio.integracao.sicop.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de Message complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="Message">
 *   &lt;complexContent>
 *     &lt;extension base="{http://pcrj/Sma/Sicop/Grava}InnerMessage">
 *       &lt;sequence>
 *         &lt;element name="MessageClassName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Message", propOrder = {
    "messageClassName"
})
@XmlSeeAlso({
    CadastroDeFaturasWS.class,
    JuntadaDeDocumentoaProcessoWS.class,
    ConsultaTodaTramitacaoDocumentosWS.class,
    VinculacaoInscricaoImobPorProcessoWS.class,
    LogonWS.class,
    UnknownPage.class,
    ConsultaTodaTramitacaoProcessoWS.class,
    CadastroDeDocumentosWS.class,
    RuntimeError.class,
    CadastroDeVolumesdeProcessoWS.class,
    TramitaProcessoWS.class,
    ConsultaProcessoWS.class,
    TramitacaoDeDocumentosWS.class,
    LogonSicopWS.class,
    ConsultaProcessoPrincipalouApensoWS.class,
    ConsultaDocumentosWS.class,
    VinculacaoNumDeFolhasDoProcessoWS.class,
    TramitacaoInternaProcessoWS.class,
    ApensaProcessoWS.class,
    IncluiProcessoWS.class,
    DespachoComplementarWS.class,
    ConsultaTabsAssuntoDespachoOrgaoWS.class
})
public class Message
    extends InnerMessage
{

    @XmlElement(name = "MessageClassName")
    protected String messageClassName;

    /**
     * Obt�m o valor da propriedade messageClassName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageClassName() {
        return messageClassName;
    }

    /**
     * Define o valor da propriedade messageClassName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageClassName(String value) {
        this.messageClassName = value;
    }

}
