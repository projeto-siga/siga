package br.gov.jfrj.siga.picketlink;

import static org.picketlink.common.util.StringUtil.isNotNull;

import java.io.StringWriter;
import java.net.URI;
import java.security.Principal;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.xml.namespace.QName;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jboss.security.audit.AuditLevel;
import org.picketlink.common.PicketLinkLogger;
import org.picketlink.common.PicketLinkLoggerFactory;
import org.picketlink.common.constants.GeneralConstants;
import org.picketlink.common.constants.JBossSAMLConstants;
import org.picketlink.common.constants.JBossSAMLURIConstants;
import org.picketlink.common.constants.SAMLAuthenticationContextClass;
import org.picketlink.common.exceptions.ConfigurationException;
import org.picketlink.common.exceptions.ProcessingException;
import org.picketlink.common.exceptions.fed.AssertionExpiredException;
import org.picketlink.common.util.DocumentUtil;
import org.picketlink.common.util.StaxParserUtil;
import org.picketlink.common.util.StringUtil;
import org.picketlink.config.federation.SPType;
import org.picketlink.identity.federation.api.saml.v2.request.SAML2Request;
import org.picketlink.identity.federation.api.saml.v2.response.SAML2Response;
import org.picketlink.identity.federation.core.SerializablePrincipal;
import org.picketlink.identity.federation.core.audit.PicketLinkAuditEvent;
import org.picketlink.identity.federation.core.audit.PicketLinkAuditEventType;
import org.picketlink.identity.federation.core.audit.PicketLinkAuditHelper;
import org.picketlink.identity.federation.core.constants.AttributeConstants;
import org.picketlink.identity.federation.core.parsers.saml.SAMLParser;
import org.picketlink.identity.federation.core.saml.v2.common.IDGenerator;
import org.picketlink.identity.federation.core.saml.v2.holders.IDPInfoHolder;
import org.picketlink.identity.federation.core.saml.v2.holders.IssuerInfoHolder;
import org.picketlink.identity.federation.core.saml.v2.holders.SPInfoHolder;
import org.picketlink.identity.federation.core.saml.v2.interfaces.SAML2Handler;
import org.picketlink.identity.federation.core.saml.v2.interfaces.SAML2HandlerRequest;
import org.picketlink.identity.federation.core.saml.v2.interfaces.SAML2HandlerRequest.GENERATE_REQUEST_TYPE;
import org.picketlink.identity.federation.core.saml.v2.interfaces.SAML2HandlerResponse;
import org.picketlink.identity.federation.core.saml.v2.util.AssertionUtil;
import org.picketlink.identity.federation.core.saml.v2.util.StatementUtil;
import org.picketlink.identity.federation.core.saml.v2.util.XMLTimeUtil;
import org.picketlink.identity.federation.core.util.JAXPValidationUtil;
import org.picketlink.identity.federation.core.util.XMLEncryptionUtil;
import org.picketlink.identity.federation.saml.v2.assertion.AssertionType;
import org.picketlink.identity.federation.saml.v2.assertion.AttributeStatementType;
import org.picketlink.identity.federation.saml.v2.assertion.AttributeStatementType.ASTChoiceType;
import org.picketlink.identity.federation.saml.v2.assertion.AttributeType;
import org.picketlink.identity.federation.saml.v2.assertion.AuthnStatementType;
import org.picketlink.identity.federation.saml.v2.assertion.EncryptedAssertionType;
import org.picketlink.identity.federation.saml.v2.assertion.NameIDType;
import org.picketlink.identity.federation.saml.v2.assertion.StatementAbstractType;
import org.picketlink.identity.federation.saml.v2.assertion.SubjectType;
import org.picketlink.identity.federation.saml.v2.assertion.SubjectType.STSubType;
import org.picketlink.identity.federation.saml.v2.metadata.EndpointType;
import org.picketlink.identity.federation.saml.v2.metadata.SPSSODescriptorType;
import org.picketlink.identity.federation.saml.v2.protocol.AuthnContextComparisonType;
import org.picketlink.identity.federation.saml.v2.protocol.AuthnRequestType;
import org.picketlink.identity.federation.saml.v2.protocol.RequestedAuthnContextType;
import org.picketlink.identity.federation.saml.v2.protocol.ResponseType;
import org.picketlink.identity.federation.saml.v2.protocol.ResponseType.RTChoiceType;
import org.picketlink.identity.federation.saml.v2.protocol.StatusType;
import org.picketlink.identity.federation.web.core.HTTPContext;
import org.picketlink.identity.federation.web.core.IdentityServer;
import org.picketlink.identity.federation.web.handlers.saml2.BaseSAML2Handler;
import org.picketlink.identity.federation.web.handlers.saml2.SAML2AuthenticationHandler;
import org.picketlink.identity.federation.web.interfaces.IRoleValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Esta classe é uma cópia de SAML2AuthenticationHandler e foi criada apenas para incluir o atributo "member-of" na resposta do IDP. Quando fizermos a atualização do picketlink para versão superior a 2.7, deve ser possível eliminar essa classe e criar esse atributo atraves do SAML2AttributeManager.
 * 
 * @author nato
 *
 */
public class SigaAuthenticationHandler extends BaseSAML2Handler {
	
    protected static final PicketLinkLogger logger = PicketLinkLoggerFactory.getLogger();

    private final IDPAuthenticationHandler idp = new IDPAuthenticationHandler();

    private final SPAuthenticationHandler sp = new SPAuthenticationHandler();

    public void handleRequestType(SAML2HandlerRequest request, SAML2HandlerResponse response) throws ProcessingException {
        if (request.getSAML2Object() instanceof AuthnRequestType == false)
            return;

        if (getType() == HANDLER_TYPE.IDP) {
            idp.handleRequestType(request, response);
        } else {
            sp.handleRequestType(request, response);
        }
    }

    @Override
    public void handleStatusResponseType(SAML2HandlerRequest request, SAML2HandlerResponse response) throws ProcessingException {
        if (request.getSAML2Object() instanceof ResponseType == false)
            return;

        if (getType() == HANDLER_TYPE.IDP) {
            idp.handleStatusResponseType(request, response);
        } else {
            sp.handleStatusResponseType(request, response);
        }
    }

    @Override
    public void generateSAMLRequest(SAML2HandlerRequest request, SAML2HandlerResponse response) throws ProcessingException {
        if (GENERATE_REQUEST_TYPE.AUTH != request.getTypeOfRequestToBeGenerated())
            return;

        if (getType() == HANDLER_TYPE.IDP) {
            idp.generateSAMLRequest(request, response);
            response.setSendRequest(true);
        } else {
            sp.generateSAMLRequest(request, response);
            response.setSendRequest(true);
        }
    }

    private class IDPAuthenticationHandler {

        public void generateSAMLRequest(SAML2HandlerRequest request, SAML2HandlerResponse response) throws ProcessingException {
        }

        public void handleStatusResponseType(SAML2HandlerRequest request, SAML2HandlerResponse response)
                throws ProcessingException {
        }

        public void handleRequestType(SAML2HandlerRequest request, SAML2HandlerResponse response) throws ProcessingException {
            HTTPContext httpContext = (HTTPContext) request.getContext();
            ServletContext servletContext = httpContext.getServletContext();

            AuthnRequestType art = (AuthnRequestType) request.getSAML2Object();
            if (art == null)
                throw logger.samlHandlerAuthnRequestIsNull();

            String destination = art.getAssertionConsumerServiceURL().toASCIIString();

            logger.trace("Destination = " + destination);

            response.setDestination(destination);

            HttpSession session = BaseSAML2Handler.getHttpSession(request);
            Principal userPrincipal = (Principal) session.getAttribute(GeneralConstants.PRINCIPAL_ID);
            if (userPrincipal == null)
                userPrincipal = httpContext.getRequest().getUserPrincipal();
            /*
             * List<String> roles = (List<String>) session.getAttribute(GeneralConstants.ROLES_ID);
             */
            try {
                /*
                 * Map<String,Object> attribs = (Map<String, Object>) request.getOptions().get(GeneralConstants.ATTRIBUTES);
                 * long assertionValidity = (Long) request.getOptions().get(GeneralConstants.ASSERTIONS_VALIDITY); String
                 * destination = art.getAssertionConsumerServiceURL().toASCIIString(); Document samlResponse =
                 * this.getResponse(destination, userPrincipal, roles, request.getIssuer().getValue(), attribs,
                 * assertionValidity, art.getID());
                 */

                Document samlResponse = this.getResponse(request);

                // Update the Identity Server
                boolean isPost = httpContext.getRequest().getMethod().equalsIgnoreCase("POST");
                IdentityServer identityServer = (IdentityServer) servletContext.getAttribute(GeneralConstants.IDENTITY_SERVER);
                // We will try to find URL for global logout from SP metadata (if they are provided) and use SP logout URL
                // for registration to IdentityServer
                String participantLogoutURL = getParticipantURL(destination, request);

                logger.trace("Participant " + destination + " will be registered to IdentityServer with logout URL "
                        + participantLogoutURL);

                // If URL is null, participant doesn't support global logout
                if (participantLogoutURL != null) {
                    identityServer.stack().register(session.getId(), participantLogoutURL, isPost);
                }

                // Check whether we use POST binding for response
                boolean strictPostBinding = request.getOptions().get(GeneralConstants.SAML_IDP_STRICT_POST_BINDING) != null
                        && (Boolean) request.getOptions().get(GeneralConstants.SAML_IDP_STRICT_POST_BINDING);
                boolean postBindingForResponse = isPost || strictPostBinding;

                response.setResultingDocument(samlResponse);
                response.setRelayState(request.getRelayState());
                response.setPostBindingForResponse(postBindingForResponse);
            } catch (Exception e) {
                logger.samlHandlerAuthenticationError(e);
                throw logger.processingError(e);
            }
        }

        @SuppressWarnings("unchecked")
        public Document getResponse(SAML2HandlerRequest request) throws ConfigurationException, ProcessingException {
            HTTPContext httpContext = (HTTPContext) request.getContext();
            AuthnRequestType art = (AuthnRequestType) request.getSAML2Object();
            HttpSession session = BaseSAML2Handler.getHttpSession(request);
            Principal userPrincipal = (Principal) session.getAttribute(GeneralConstants.PRINCIPAL_ID);
            if (userPrincipal == null)
                userPrincipal = httpContext.getRequest().getUserPrincipal();

            String assertionConsumerURL = art.getAssertionConsumerServiceURL().toASCIIString();
            List<String> roles = (List<String>) session.getAttribute(GeneralConstants.ROLES_ID);
            String identityURL = request.getIssuer().getValue();
            Map<String, Object> attribs = (Map<String, Object>) request.getOptions().get(GeneralConstants.ATTRIBUTES);
            String requestID = art.getID();

            Document samlResponseDocument = null;

            String authMethod = (String) request.getOptions().get(GeneralConstants.LOGIN_TYPE);

            logger.trace("AssertionConsumerURL=" + assertionConsumerURL);

            ResponseType responseType = null;

            SAML2Response saml2Response = new SAML2Response();

            // Create a response type
            String id = IDGenerator.create("ID_");

            IssuerInfoHolder issuerHolder = new IssuerInfoHolder(identityURL);
            issuerHolder.setStatusCode(JBossSAMLURIConstants.STATUS_SUCCESS.get());

            IDPInfoHolder idp = new IDPInfoHolder();
            idp.setNameIDFormatValue(userPrincipal.getName());
            idp.setNameIDFormat(JBossSAMLURIConstants.NAMEID_FORMAT_PERSISTENT.get());

            String assertionID = (String) session.getAttribute(GeneralConstants.ASSERTION_ID);

            if (assertionID != null) {
                // Just renew the assertion
                AssertionType latestAssertion = (AssertionType) session.getAttribute(GeneralConstants.ASSERTION);
                if (latestAssertion != null)
                    idp.setAssertion(latestAssertion);
            }

            SPInfoHolder sp = new SPInfoHolder();
            sp.setResponseDestinationURI(assertionConsumerURL);
            sp.setRequestID(requestID);
            sp.setIssuer(art.getIssuer().getValue());
            responseType = saml2Response.createResponseType(id, sp, idp, issuerHolder);

            // Add information on the roles
            AssertionType assertion = responseType.getAssertions().get(0).getAssertion();

            // Create an AuthnStatementType
            if (handlerConfig.getParameter(DISABLE_AUTHN_STATEMENT) == null) {
                String authContextRef = JBossSAMLURIConstants.AC_PASSWORD.get();
                if (isNotNull(authMethod))
                    authContextRef = authMethod;

                AuthnStatementType authnStatement = StatementUtil.createAuthnStatement(XMLTimeUtil.getIssueInstant(),
                        authContextRef);

                authnStatement.setSessionIndex(assertion.getID());

                assertion.addStatement(authnStatement);
            }

            if (handlerConfig.getParameter(DISABLE_SENDING_ROLES) == null && (roles != null && !roles.isEmpty())) {
                AttributeStatementType attrStatement = null;
                if (handlerConfig.getParameter(USE_MULTI_VALUED_ROLES) != null) {
                    attrStatement = StatementUtil.createAttributeStatementForRoles(roles, true);
                } else {
                    attrStatement = StatementUtil.createAttributeStatement(roles);
                }
                if (attrStatement != null) {
                    assertion.addStatement(attrStatement);
                }
            }

            // Add in the attributes information
            if (attribs == null) //Nato: At least one attribute should exist to create the attStatement
            	attribs = new HashMap<String, Object>();
            if (attribs.size() == 0)
            	attribs.put("idp-name", "sigaidp");
            if (attribs != null && attribs.size() > 0) {
                AttributeStatementType attStatement = StatementUtil.createAttributeStatement(attribs);
                assertion.addStatement(attStatement);

                //Nato: Include a member-of multivalued attribute for each role. GSA requirement.
                AttributeType attr = new AttributeType("member-of");
                for (String role : roles) {
                	attr.addAttributeValue(role.toLowerCase());
                }
                attStatement.addAttribute(new ASTChoiceType(attr));
            }
            
            

            // Add assertion to the session
            session.setAttribute(GeneralConstants.ASSERTION, assertion);

            Map<String, Object> requestOptions = request.getOptions();
            PicketLinkAuditHelper auditHelper = (PicketLinkAuditHelper) requestOptions.get(GeneralConstants.AUDIT_HELPER);
            if (auditHelper != null) {
                PicketLinkAuditEvent auditEvent = new PicketLinkAuditEvent(AuditLevel.INFO);
                auditEvent.setWhoIsAuditing((String) requestOptions.get(GeneralConstants.CONTEXT_PATH));
                auditEvent.setType(PicketLinkAuditEventType.CREATED_ASSERTION);
                auditEvent.setAssertionID(id);
                auditHelper.audit(auditEvent);
            }

            try {
                samlResponseDocument = saml2Response.convert(responseType);

                if (logger.isTraceEnabled()) {
                    logger.trace("SAML Response Document: " + DocumentUtil.asString(samlResponseDocument));
                }
            } catch (Exception e) {
                throw logger.samlAssertionMarshallError(e);
            }

            return samlResponseDocument;
        }

        private String getParticipantURL(String destination, SAML2HandlerRequest request) {
            SPSSODescriptorType spMetadata = (SPSSODescriptorType) request.getOptions().get(
                    GeneralConstants.SP_SSO_METADATA_DESCRIPTOR);

            // Metadata not found. We will use destination for registration to IdentityServer
            if (spMetadata == null) {
                return destination;
            }

            List<EndpointType> logoutEndpoints = spMetadata.getSingleLogoutService();

            // If endpoint not found, we assume that SP doesn't support logout profile
            if (logoutEndpoints == null || logoutEndpoints.size() == 0) {
                return null;
            }

            // Use first endpoint for now (Maybe later we can find logoutType according to bindingType from SAMLRequest)
            EndpointType logoutEndpoint = logoutEndpoints.get(0);
            return logoutEndpoint.getLocation().toASCIIString();
        }
    }

    private class SPAuthenticationHandler {

        public void generateSAMLRequest(SAML2HandlerRequest request, SAML2HandlerResponse response) throws ProcessingException {
            String issuerValue = request.getIssuer().getValue();

            SAML2Request samlRequest = new SAML2Request();
            String id = IDGenerator.create("ID_");

            String assertionConsumerURL = (String) handlerConfig.getParameter(SAML2Handler.ASSERTION_CONSUMER_URL);
            if (StringUtil.isNullOrEmpty(assertionConsumerURL)) {
                assertionConsumerURL = issuerValue;
            }

            // Check if there is a nameid policy
            String nameIDFormat = (String) handlerConfig.getParameter(GeneralConstants.NAMEID_FORMAT);
            if (isNotNull(nameIDFormat)) {
                samlRequest.setNameIDFormat(nameIDFormat);
            }
            try {
                AuthnRequestType authn = samlRequest.createAuthnRequestType(id, assertionConsumerURL,
                        response.getDestination(), issuerValue);

                createRequestedAuthnContext(authn);

                String bindingType = getSPConfiguration().getBindingType();
                boolean isIdpUsesPostBinding = getSPConfiguration().isIdpUsesPostBinding();

                if (bindingType != null) {
                    if (bindingType.equals("POST") || isIdpUsesPostBinding) {
                        authn.setProtocolBinding(URI.create(JBossSAMLURIConstants.SAML_HTTP_POST_BINDING.get()));
                    } else if (bindingType.equals("REDIRECT")) {
                        authn.setProtocolBinding(URI.create(JBossSAMLURIConstants.SAML_HTTP_REDIRECT_BINDING.get()));
                    } else {
                        throw logger.samlInvalidProtocolBinding();
                    }
                }

                response.setResultingDocument(samlRequest.convert(authn));
                response.setSendRequest(true);

                Map<String, Object> requestOptions = request.getOptions();
                PicketLinkAuditHelper auditHelper = (PicketLinkAuditHelper) requestOptions.get(GeneralConstants.AUDIT_HELPER);
                if (auditHelper != null) {
                    PicketLinkAuditEvent auditEvent = new PicketLinkAuditEvent(AuditLevel.INFO);
                    auditEvent.setWhoIsAuditing((String) requestOptions.get(GeneralConstants.CONTEXT_PATH));
                    auditEvent.setType(PicketLinkAuditEventType.CREATED_ASSERTION);
                    auditEvent.setAssertionID(id);
                    auditHelper.audit(auditEvent);
                }

                // Save AuthnRequest ID into sharedState, so that we can later process it by another handler
                request.addOption(GeneralConstants.AUTH_REQUEST_ID, id);
            } catch (Exception e) {
                throw logger.processingError(e);
            }
        }

        public void handleStatusResponseType(SAML2HandlerRequest request, SAML2HandlerResponse response)
                throws ProcessingException {
            HTTPContext httpContext = (HTTPContext) request.getContext();
            ResponseType responseType = (ResponseType) request.getSAML2Object();
            List<RTChoiceType> assertions = responseType.getAssertions();
            if (assertions.size() == 0)
                throw logger.samlHandlerNoAssertionFromIDP();

            PrivateKey privateKey = (PrivateKey) request.getOptions().get(GeneralConstants.DECRYPTING_KEY);

            Object assertion = assertions.get(0).getEncryptedAssertion();
            if (assertion instanceof EncryptedAssertionType) {
                responseType = this.decryptAssertion(responseType, privateKey);
                assertion = responseType.getAssertions().get(0).getAssertion();
            }
            if (assertion == null) {
                assertion = assertions.get(0).getAssertion();
            }

            request.addOption(GeneralConstants.ASSERTION, assertion);

            Principal userPrincipal = handleSAMLResponse(responseType, response);
            if (userPrincipal == null) {
                response.setError(403, "User Principal not determined: Forbidden");
            } else {
                HttpSession session = httpContext.getRequest().getSession(false);

                // add the principal to the session
                session.setAttribute(GeneralConstants.PRINCIPAL_ID, userPrincipal);

                Document assertionDocument = AssertionUtil.asDocument((AssertionType) assertion);

                String assertionAttributeName = (String) handlerConfig
                        .getParameter(GeneralConstants.ASSERTION_SESSION_ATTRIBUTE_NAME);

                if (assertionAttributeName != null) {
                    session.setAttribute(assertionAttributeName, assertionDocument);
                }

                session.setAttribute(GeneralConstants.ASSERTION_SESSION_ATTRIBUTE_NAME, assertionDocument);
            }
        }

        public void handleRequestType(SAML2HandlerRequest request, SAML2HandlerResponse response) throws ProcessingException {
        }

        private ResponseType decryptAssertion(ResponseType responseType, PrivateKey privateKey) throws ProcessingException {
            if (privateKey == null)
                throw logger.nullArgumentError("privateKey");
            SAML2Response saml2Response = new SAML2Response();
            try {
                Document doc = saml2Response.convert(responseType);

                Element enc = DocumentUtil.getElement(doc, new QName(JBossSAMLConstants.ENCRYPTED_ASSERTION.get()));
                if (enc == null)
                    throw logger.samlHandlerNullEncryptedAssertion();
                String oldID = enc.getAttribute(JBossSAMLConstants.ID.get());
                Document newDoc = DocumentUtil.createDocument();
                Node importedNode = newDoc.importNode(enc, true);
                newDoc.appendChild(importedNode);

                Element decryptedDocumentElement = XMLEncryptionUtil.decryptElementInDocument(newDoc, privateKey);
                SAMLParser parser = new SAMLParser();

                JAXPValidationUtil.checkSchemaValidation(decryptedDocumentElement);
                AssertionType assertion = (AssertionType) parser.parse(StaxParserUtil.getXMLEventReader(DocumentUtil
                        .getNodeAsStream(decryptedDocumentElement)));

                responseType.replaceAssertion(oldID, new RTChoiceType(assertion));
                return responseType;
            } catch (Exception e) {
                throw logger.processingError(e);
            }
        }

        private Principal handleSAMLResponse(ResponseType responseType, SAML2HandlerResponse response)
                throws ProcessingException {
            if (responseType == null)
                throw logger.nullArgumentError("response type");

            StatusType statusType = responseType.getStatus();
            if (statusType == null)
                throw logger.nullArgumentError("Status Type from the IDP");

            String statusValue = statusType.getStatusCode().getValue().toASCIIString();
            if (JBossSAMLURIConstants.STATUS_SUCCESS.get().equals(statusValue) == false)
                throw logger.samlHandlerIDPAuthenticationFailedError();

            List<RTChoiceType> assertions = responseType.getAssertions();
            if (assertions.size() == 0)
                throw logger.samlHandlerNoAssertionFromIDP();

            AssertionType assertion = assertions.get(0).getAssertion();
            // Check for validity of assertion
            boolean expiredAssertion;
            try {
                String skew = (String) handlerConfig.getParameter(SAML2Handler.CLOCK_SKEW_MILIS);
                if (isNotNull(skew)) {
                    long skewMilis = Long.parseLong(skew);
                    expiredAssertion = AssertionUtil.hasExpired(assertion, skewMilis);
                } else
                    expiredAssertion = AssertionUtil.hasExpired(assertion);
            } catch (ConfigurationException e) {
                throw new ProcessingException(e);
            }
            if (expiredAssertion) {
                AssertionExpiredException aee = new AssertionExpiredException();
                aee.setId(assertion.getID());
                throw logger.assertionExpiredError(aee);
            }

            SubjectType subject = assertion.getSubject();
            /*
             * JAXBElement<NameIDType> jnameID = (JAXBElement<NameIDType>) subject.getContent().get(0); NameIDType nameID =
             * jnameID.getValue();
             */
            if (subject == null)
                throw logger.nullValueError("Subject in the assertion");

            STSubType subType = subject.getSubType();
            if (subType == null)
                throw logger.nullValueError("Unable to find subtype via subject");
            NameIDType nameID = (NameIDType) subType.getBaseID();

            if (nameID == null)
                throw logger.nullValueError("Unable to find username via subject");

            final String userName = nameID.getValue();
            List<String> roles = new ArrayList<String>();

            // Let us get the roles
            Set<StatementAbstractType> statements = assertion.getStatements();
            for (StatementAbstractType statement : statements) {
                if (statement instanceof AttributeStatementType) {
                    AttributeStatementType attributeStatement = (AttributeStatementType) statement;
                    roles.addAll(getRoles(attributeStatement));
                }
            }

            response.setRoles(roles);

            Principal principal = new SerializablePrincipal(userName);

            if (handlerChainConfig.getParameter(GeneralConstants.ROLE_VALIDATOR_IGNORE) == null) {
                // Validate the roles
                IRoleValidator roleValidator = (IRoleValidator) handlerChainConfig
                        .getParameter(GeneralConstants.ROLE_VALIDATOR);
                if (roleValidator == null)
                    throw logger.nullValueError("Role Validator");

                boolean validRole = roleValidator.userInRole(principal, roles);

                if (!validRole) {
                    logger.trace("Invalid role: " + roles);
                    principal = null;
                }
            }
            return principal;
        }

        /**
         * Get the roles from the attribute statement
         *
         * @param attributeStatement
         *
         * @return
         */
        private List<String> getRoles(AttributeStatementType attributeStatement) {
            List<String> roles = new ArrayList<String>();

            // PLFED-141: Disable role picking from IDP response
            if (handlerConfig.containsKey(DISABLE_ROLE_PICKING)) {
                String val = (String) handlerConfig.getParameter(DISABLE_ROLE_PICKING);
                if (isNotNull(val) && "true".equalsIgnoreCase(val))
                    return roles;
            }

            // PLFED-140: which of the attribute statements represent roles?
            List<String> roleKeys = new ArrayList<String>();

            if (handlerConfig.containsKey(ROLE_KEY)) {
                String roleKey = (String) handlerConfig.getParameter(ROLE_KEY);
                if (isNotNull(roleKey)) {
                    roleKeys.addAll(StringUtil.tokenize(roleKey));
                }
            }

            List<ASTChoiceType> attList = attributeStatement.getAttributes();
            for (ASTChoiceType obj : attList) {
                AttributeType attr = obj.getAttribute();
                if (roleKeys.size() > 0) {
                    if (!roleKeys.contains(attr.getName()))
                        continue;
                }
                List<Object> attributeValues = attr.getAttributeValue();
                if (attributeValues != null) {
                    for (Object attrValue : attributeValues) {
                        if (attrValue instanceof String) {
                            roles.add((String) attrValue);
                        } else if (attrValue instanceof Node) {
                            Node roleNode = (Node) attrValue;
                            roles.add(roleNode.getFirstChild().getNodeValue());
                        } else
                            throw logger.unsupportedRoleType(attrValue);
                    }
                }
            }
            return roles;
        }

        private SPType getSPConfiguration() {
            SPType spConfiguration = (SPType) handlerChainConfig.getParameter(GeneralConstants.CONFIGURATION);

            if (spConfiguration == null) {
                throw logger.samlHandlerServiceProviderConfigNotFound();
            }

            return spConfiguration;
        }
    }

    private void createRequestedAuthnContext(final AuthnRequestType authn) {
        String authnContextClasses = (String) handlerConfig.getParameter(GeneralConstants.AUTHN_CONTEXT_CLASSES);

        if (isNotNull(authnContextClasses)) {
            RequestedAuthnContextType requestAuthnContext = new RequestedAuthnContextType();

            for (String classFqn : authnContextClasses.split(",")) {
                SAMLAuthenticationContextClass standardClass = SAMLAuthenticationContextClass.forAlias(classFqn);

                if (standardClass != null) {
                    classFqn = standardClass.getFqn();
                }

                requestAuthnContext.addAuthnContextClassRef(classFqn);
            }

            if (!requestAuthnContext.getAuthnContextClassRef().isEmpty()) {
                String comparison = (String) handlerConfig.getParameter(GeneralConstants.REQUESTED_AUTHN_CONTEXT_COMPARISON);

                if (isNotNull(comparison)) {
                    requestAuthnContext.setComparison(AuthnContextComparisonType.fromValue(comparison));
                }

                authn.setRequestedAuthnContext(requestAuthnContext);
            } else {
                logger.debug("RequestedAuthnContext not set for AuthnRequest. No class was provided.");
            }
        }
    }

}
