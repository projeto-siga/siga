
package bluecrystal.service.v1.icpbr;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the bluecrystal.service.v1.icpbr package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ValidateSignatureByPolicy_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "validateSignatureByPolicy");
    private final static QName _HashSignedAttribADRB10_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "hashSignedAttribADRB10");
    private final static QName _HashSignedAttribADRB10Response_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "hashSignedAttribADRB10Response");
    private final static QName _ExtractSignCompare_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "extractSignCompare");
    private final static QName _GetCertSubjectResponse_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "getCertSubjectResponse");
    private final static QName _HashSignedAttribADRB21_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "hashSignedAttribADRB21");
    private final static QName _ComposeEnvelopeADRB10Response_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "composeEnvelopeADRB10Response");
    private final static QName _GetCertSubject_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "getCertSubject");
    private final static QName _ExtractSignature_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "extractSignature");
    private final static QName _Exception_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "Exception");
    private final static QName _ValidateSignatureByPolicyResponse_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "validateSignatureByPolicyResponse");
    private final static QName _ExtractSignerCert_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "extractSignerCert");
    private final static QName _GetCertSubjectCn_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "getCertSubjectCn");
    private final static QName _ComposeEnvelopeADRB21_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "composeEnvelopeADRB21");
    private final static QName _ExtractSignatureResponse_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "extractSignatureResponse");
    private final static QName _ExtractSignCompareResponse_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "extractSignCompareResponse");
    private final static QName _ComposeEnvelopeADRB10_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "composeEnvelopeADRB10");
    private final static QName _HashSignedAttribADRB21Response_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "hashSignedAttribADRB21Response");
    private final static QName _GetCertSubjectCnResponse_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "getCertSubjectCnResponse");
    private final static QName _ParseCertificate_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "parseCertificate");
    private final static QName _ValidateSign_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "validateSign");
    private final static QName _ValidateSignResponse_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "validateSignResponse");
    private final static QName _ExtractSignerCertResponse_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "extractSignerCertResponse");
    private final static QName _ParseCertificateResponse_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "parseCertificateResponse");
    private final static QName _ComposeEnvelopeADRB21Response_QNAME = new QName("http://icpbr.v1.service.bluecrystal/", "composeEnvelopeADRB21Response");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: bluecrystal.service.v1.icpbr
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ExtractSignature }
     * 
     */
    public ExtractSignature createExtractSignature() {
        return new ExtractSignature();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link HashSignedAttribADRB10 }
     * 
     */
    public HashSignedAttribADRB10 createHashSignedAttribADRB10() {
        return new HashSignedAttribADRB10();
    }

    /**
     * Create an instance of {@link HashSignedAttribADRB10Response }
     * 
     */
    public HashSignedAttribADRB10Response createHashSignedAttribADRB10Response() {
        return new HashSignedAttribADRB10Response();
    }

    /**
     * Create an instance of {@link GetCertSubjectResponse }
     * 
     */
    public GetCertSubjectResponse createGetCertSubjectResponse() {
        return new GetCertSubjectResponse();
    }

    /**
     * Create an instance of {@link ValidateSignResponse }
     * 
     */
    public ValidateSignResponse createValidateSignResponse() {
        return new ValidateSignResponse();
    }

    /**
     * Create an instance of {@link ComposeEnvelopeADRB10 }
     * 
     */
    public ComposeEnvelopeADRB10 createComposeEnvelopeADRB10() {
        return new ComposeEnvelopeADRB10();
    }

    /**
     * Create an instance of {@link HashSignedAttribADRB21Response }
     * 
     */
    public HashSignedAttribADRB21Response createHashSignedAttribADRB21Response() {
        return new HashSignedAttribADRB21Response();
    }

    /**
     * Create an instance of {@link ValidateSignatureByPolicyResponse }
     * 
     */
    public ValidateSignatureByPolicyResponse createValidateSignatureByPolicyResponse() {
        return new ValidateSignatureByPolicyResponse();
    }

    /**
     * Create an instance of {@link ExtractSignerCert }
     * 
     */
    public ExtractSignerCert createExtractSignerCert() {
        return new ExtractSignerCert();
    }

    /**
     * Create an instance of {@link GetCertSubjectCn }
     * 
     */
    public GetCertSubjectCn createGetCertSubjectCn() {
        return new GetCertSubjectCn();
    }

    /**
     * Create an instance of {@link GetCertSubject }
     * 
     */
    public GetCertSubject createGetCertSubject() {
        return new GetCertSubject();
    }

    /**
     * Create an instance of {@link ExtractSignatureResponse }
     * 
     */
    public ExtractSignatureResponse createExtractSignatureResponse() {
        return new ExtractSignatureResponse();
    }

    /**
     * Create an instance of {@link ComposeEnvelopeADRB21 }
     * 
     */
    public ComposeEnvelopeADRB21 createComposeEnvelopeADRB21() {
        return new ComposeEnvelopeADRB21();
    }

    /**
     * Create an instance of {@link ExtractSignCompare }
     * 
     */
    public ExtractSignCompare createExtractSignCompare() {
        return new ExtractSignCompare();
    }

    /**
     * Create an instance of {@link ValidateSignatureByPolicy }
     * 
     */
    public ValidateSignatureByPolicy createValidateSignatureByPolicy() {
        return new ValidateSignatureByPolicy();
    }

    /**
     * Create an instance of {@link ComposeEnvelopeADRB10Response }
     * 
     */
    public ComposeEnvelopeADRB10Response createComposeEnvelopeADRB10Response() {
        return new ComposeEnvelopeADRB10Response();
    }

    /**
     * Create an instance of {@link HashSignedAttribADRB21 }
     * 
     */
    public HashSignedAttribADRB21 createHashSignedAttribADRB21() {
        return new HashSignedAttribADRB21();
    }

    /**
     * Create an instance of {@link ExtractSignerCertResponse }
     * 
     */
    public ExtractSignerCertResponse createExtractSignerCertResponse() {
        return new ExtractSignerCertResponse();
    }

    /**
     * Create an instance of {@link ComposeEnvelopeADRB21Response }
     * 
     */
    public ComposeEnvelopeADRB21Response createComposeEnvelopeADRB21Response() {
        return new ComposeEnvelopeADRB21Response();
    }

    /**
     * Create an instance of {@link ParseCertificateResponse }
     * 
     */
    public ParseCertificateResponse createParseCertificateResponse() {
        return new ParseCertificateResponse();
    }

    /**
     * Create an instance of {@link GetCertSubjectCnResponse }
     * 
     */
    public GetCertSubjectCnResponse createGetCertSubjectCnResponse() {
        return new GetCertSubjectCnResponse();
    }

    /**
     * Create an instance of {@link ExtractSignCompareResponse }
     * 
     */
    public ExtractSignCompareResponse createExtractSignCompareResponse() {
        return new ExtractSignCompareResponse();
    }

    /**
     * Create an instance of {@link ValidateSign }
     * 
     */
    public ValidateSign createValidateSign() {
        return new ValidateSign();
    }

    /**
     * Create an instance of {@link ParseCertificate }
     * 
     */
    public ParseCertificate createParseCertificate() {
        return new ParseCertificate();
    }

    /**
     * Create an instance of {@link NameValue }
     * 
     */
    public NameValue createNameValue() {
        return new NameValue();
    }

    /**
     * Create an instance of {@link SignCompare }
     * 
     */
    public SignCompare createSignCompare() {
        return new SignCompare();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateSignatureByPolicy }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "validateSignatureByPolicy")
    public JAXBElement<ValidateSignatureByPolicy> createValidateSignatureByPolicy(ValidateSignatureByPolicy value) {
        return new JAXBElement<ValidateSignatureByPolicy>(_ValidateSignatureByPolicy_QNAME, ValidateSignatureByPolicy.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HashSignedAttribADRB10 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "hashSignedAttribADRB10")
    public JAXBElement<HashSignedAttribADRB10> createHashSignedAttribADRB10(HashSignedAttribADRB10 value) {
        return new JAXBElement<HashSignedAttribADRB10>(_HashSignedAttribADRB10_QNAME, HashSignedAttribADRB10 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HashSignedAttribADRB10Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "hashSignedAttribADRB10Response")
    public JAXBElement<HashSignedAttribADRB10Response> createHashSignedAttribADRB10Response(HashSignedAttribADRB10Response value) {
        return new JAXBElement<HashSignedAttribADRB10Response>(_HashSignedAttribADRB10Response_QNAME, HashSignedAttribADRB10Response.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExtractSignCompare }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "extractSignCompare")
    public JAXBElement<ExtractSignCompare> createExtractSignCompare(ExtractSignCompare value) {
        return new JAXBElement<ExtractSignCompare>(_ExtractSignCompare_QNAME, ExtractSignCompare.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCertSubjectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "getCertSubjectResponse")
    public JAXBElement<GetCertSubjectResponse> createGetCertSubjectResponse(GetCertSubjectResponse value) {
        return new JAXBElement<GetCertSubjectResponse>(_GetCertSubjectResponse_QNAME, GetCertSubjectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HashSignedAttribADRB21 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "hashSignedAttribADRB21")
    public JAXBElement<HashSignedAttribADRB21> createHashSignedAttribADRB21(HashSignedAttribADRB21 value) {
        return new JAXBElement<HashSignedAttribADRB21>(_HashSignedAttribADRB21_QNAME, HashSignedAttribADRB21 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ComposeEnvelopeADRB10Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "composeEnvelopeADRB10Response")
    public JAXBElement<ComposeEnvelopeADRB10Response> createComposeEnvelopeADRB10Response(ComposeEnvelopeADRB10Response value) {
        return new JAXBElement<ComposeEnvelopeADRB10Response>(_ComposeEnvelopeADRB10Response_QNAME, ComposeEnvelopeADRB10Response.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCertSubject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "getCertSubject")
    public JAXBElement<GetCertSubject> createGetCertSubject(GetCertSubject value) {
        return new JAXBElement<GetCertSubject>(_GetCertSubject_QNAME, GetCertSubject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExtractSignature }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "extractSignature")
    public JAXBElement<ExtractSignature> createExtractSignature(ExtractSignature value) {
        return new JAXBElement<ExtractSignature>(_ExtractSignature_QNAME, ExtractSignature.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateSignatureByPolicyResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "validateSignatureByPolicyResponse")
    public JAXBElement<ValidateSignatureByPolicyResponse> createValidateSignatureByPolicyResponse(ValidateSignatureByPolicyResponse value) {
        return new JAXBElement<ValidateSignatureByPolicyResponse>(_ValidateSignatureByPolicyResponse_QNAME, ValidateSignatureByPolicyResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExtractSignerCert }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "extractSignerCert")
    public JAXBElement<ExtractSignerCert> createExtractSignerCert(ExtractSignerCert value) {
        return new JAXBElement<ExtractSignerCert>(_ExtractSignerCert_QNAME, ExtractSignerCert.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCertSubjectCn }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "getCertSubjectCn")
    public JAXBElement<GetCertSubjectCn> createGetCertSubjectCn(GetCertSubjectCn value) {
        return new JAXBElement<GetCertSubjectCn>(_GetCertSubjectCn_QNAME, GetCertSubjectCn.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ComposeEnvelopeADRB21 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "composeEnvelopeADRB21")
    public JAXBElement<ComposeEnvelopeADRB21> createComposeEnvelopeADRB21(ComposeEnvelopeADRB21 value) {
        return new JAXBElement<ComposeEnvelopeADRB21>(_ComposeEnvelopeADRB21_QNAME, ComposeEnvelopeADRB21 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExtractSignatureResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "extractSignatureResponse")
    public JAXBElement<ExtractSignatureResponse> createExtractSignatureResponse(ExtractSignatureResponse value) {
        return new JAXBElement<ExtractSignatureResponse>(_ExtractSignatureResponse_QNAME, ExtractSignatureResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExtractSignCompareResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "extractSignCompareResponse")
    public JAXBElement<ExtractSignCompareResponse> createExtractSignCompareResponse(ExtractSignCompareResponse value) {
        return new JAXBElement<ExtractSignCompareResponse>(_ExtractSignCompareResponse_QNAME, ExtractSignCompareResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ComposeEnvelopeADRB10 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "composeEnvelopeADRB10")
    public JAXBElement<ComposeEnvelopeADRB10> createComposeEnvelopeADRB10(ComposeEnvelopeADRB10 value) {
        return new JAXBElement<ComposeEnvelopeADRB10>(_ComposeEnvelopeADRB10_QNAME, ComposeEnvelopeADRB10 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HashSignedAttribADRB21Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "hashSignedAttribADRB21Response")
    public JAXBElement<HashSignedAttribADRB21Response> createHashSignedAttribADRB21Response(HashSignedAttribADRB21Response value) {
        return new JAXBElement<HashSignedAttribADRB21Response>(_HashSignedAttribADRB21Response_QNAME, HashSignedAttribADRB21Response.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCertSubjectCnResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "getCertSubjectCnResponse")
    public JAXBElement<GetCertSubjectCnResponse> createGetCertSubjectCnResponse(GetCertSubjectCnResponse value) {
        return new JAXBElement<GetCertSubjectCnResponse>(_GetCertSubjectCnResponse_QNAME, GetCertSubjectCnResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParseCertificate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "parseCertificate")
    public JAXBElement<ParseCertificate> createParseCertificate(ParseCertificate value) {
        return new JAXBElement<ParseCertificate>(_ParseCertificate_QNAME, ParseCertificate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateSign }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "validateSign")
    public JAXBElement<ValidateSign> createValidateSign(ValidateSign value) {
        return new JAXBElement<ValidateSign>(_ValidateSign_QNAME, ValidateSign.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateSignResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "validateSignResponse")
    public JAXBElement<ValidateSignResponse> createValidateSignResponse(ValidateSignResponse value) {
        return new JAXBElement<ValidateSignResponse>(_ValidateSignResponse_QNAME, ValidateSignResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExtractSignerCertResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "extractSignerCertResponse")
    public JAXBElement<ExtractSignerCertResponse> createExtractSignerCertResponse(ExtractSignerCertResponse value) {
        return new JAXBElement<ExtractSignerCertResponse>(_ExtractSignerCertResponse_QNAME, ExtractSignerCertResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParseCertificateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "parseCertificateResponse")
    public JAXBElement<ParseCertificateResponse> createParseCertificateResponse(ParseCertificateResponse value) {
        return new JAXBElement<ParseCertificateResponse>(_ParseCertificateResponse_QNAME, ParseCertificateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ComposeEnvelopeADRB21Response }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://icpbr.v1.service.bluecrystal/", name = "composeEnvelopeADRB21Response")
    public JAXBElement<ComposeEnvelopeADRB21Response> createComposeEnvelopeADRB21Response(ComposeEnvelopeADRB21Response value) {
        return new JAXBElement<ComposeEnvelopeADRB21Response>(_ComposeEnvelopeADRB21Response_QNAME, ComposeEnvelopeADRB21Response.class, null, value);
    }

}
