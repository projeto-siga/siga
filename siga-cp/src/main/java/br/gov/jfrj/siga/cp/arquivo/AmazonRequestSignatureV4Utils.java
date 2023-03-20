package br.gov.jfrj.siga.cp.arquivo;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

/**
 * Copyright 2020 Alex Vasiliev, licensed under the Apache 2.0 license:
 * https://opensource.org/licenses/Apache-2.0
 */
public class AmazonRequestSignatureV4Utils {

    /**
     * Generates signing headers for HTTP request in accordance with Amazon AWS API
     * Signature version 4 process.
     * <p>
     * Following steps outlined here:
     * <a href=
     * "https://docs.aws.amazon.com/general/latest/gr/signature-version-4.html">docs.aws.amazon.com</a>
     * <p>
     * The ISO8601 date parameter should look like '20150830T123600Z' and can be
     * created by making a call to:
     * 
     * <pre>
     * java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'").format(ZonedDateTime.now(ZoneOffset.UTC))}
     * </pre>
     * 
     * or, if you prefer joda:
     * 
     * <pre>
     * {@code
     * org.joda.time.format.ISODateTimeFormat.basicDateTimeNoMillis().print(DateTime.now().withZone(DateTimeZone.UTC))
     * }
     * </pre>
     *
     * @param method
     *                           HTTP request method, (GET|POST|DELETE|PUT|...),
     *                           e.g.,
     *                           {@link java.net.HttpURLConnection#getRequestMethod()}
     * @param host
     *                           URL host, e.g., {@link java.net.URL#getHost()}.
     * @param path
     *                           URL path, e.g., {@link java.net.URL#getPath()}.
     * @param query
     *                           URL query, (parameters in sorted order, see the AWS
     *                           spec) e.g., {@link java.net.URL#getQuery()}.
     * @param headerMap
     *                           HTTP request header map. The returned map should be
     *                           used in the request.
     * @param body
     *                           The binary request body, for requests like POST or
     *                           PUT. If null then new byte[0] will be used.
     * @param isoDateTime
     *                           The time and date of the request in ISO8601 basic
     *                           format like '20150830T123600Z', see comment above.
     * @param awsAccessKeyId
     *                           AWS Identity, e.g., "AKIAJTOUY..."
     * @param awsSecretAccessKey
     *                           AWS Secret Key, e.g., "I8Q2hY819e+7KzB..."
     * @param awsRegion
     *                           AWS Region, e.g., "us-east-1"
     * @param awsService
     *                           AWS Service, e.g., "route53"
     * @return Linked map of properties that should be set on the http request with
     *         no other headers.
     */
    public static Map<String, String> calculateAuthorizationHeaders(String method, String host, String path,
            String query, Map<String, String> headerMap, byte[] body, String isoDateTime, String awsAccessKeyId,
            String awsSecretAccessKey, String awsRegion, String awsService)
            throws GeneralSecurityException {

        if (body == null) {
            body = new byte[0];
        }
        String bodySha256 = hex(sha256(body));
        String isoJustDate = isoDateTime.substring(0, 8); // Cut the date portion of a string like '20150830T123600Z';

        Map<String, String> resultHeaderMap = new LinkedHashMap<>();

        // (1)
        // https://docs.aws.amazon.com/general/latest/gr/sigv4-create-canonical-request.html
        StringBuilder canonicalRequestSb = new StringBuilder();
        appendOptionalString(canonicalRequestSb, method);
        appendOptionalString(canonicalRequestSb, path);
        appendOptionalString(canonicalRequestSb, query);

        final String HOST_HEADER = "Host";
        final String AMAZON_DATE_HEADER = "X-Amz-Date";
        final String AMAZON_CONTENT_HASH_HEADER = "X-Amz-Content-Sha256";

        // need to order the headers to match the AWS docs but may not be necessary for
        // the request
        List<String> headerList = new ArrayList<>(headerMap.keySet());
        headerList.add(HOST_HEADER);
        headerList.add(AMAZON_CONTENT_HASH_HEADER);
        headerList.add(AMAZON_DATE_HEADER);
        Collections.sort(headerList, String.CASE_INSENSITIVE_ORDER);
        StringBuilder hashedHeadersSb = new StringBuilder();
        for (String header : headerList) {
            String value = headerMap.get(header);
            if (header.equals(HOST_HEADER)) {
                value = host;
            } else if (header.equals(AMAZON_CONTENT_HASH_HEADER)) {
                value = bodySha256;
            } else if (header.equals(AMAZON_DATE_HEADER)) {
                value = isoDateTime;
            }
            resultHeaderMap.put(header, value);
            String headerLower = header.toLowerCase();
            if (hashedHeadersSb.length() > 0) {
                hashedHeadersSb.append(';');
            }
            hashedHeadersSb.append(headerLower);
            String cannonicalValue = headerLower + ":" + normalizeSpaces(value);
            appendOptionalString(canonicalRequestSb, cannonicalValue);
        }
        canonicalRequestSb.append('\n'); // new line required after headers

        // names of the headers, lowercase, separated by ';'
        String signedHeaders = hashedHeadersSb.toString();
        appendOptionalString(canonicalRequestSb, signedHeaders);
        appendOptionalString(canonicalRequestSb, bodySha256);
        // trim off the last newline
        canonicalRequestSb.setLength(canonicalRequestSb.length() - 1);
        String canonicalRequestBody = canonicalRequestSb.toString();
        String canonicalRequestHash = hex(sha256(canonicalRequestBody.getBytes(StandardCharsets.UTF_8)));

        final String AWS_ENCRYPTION_ALGORITHM = "AWS4-HMAC-SHA256";

        // (2)
        // https://docs.aws.amazon.com/general/latest/gr/sigv4-create-string-to-sign.html
        StringBuilder stringToSignSb = new StringBuilder();
        stringToSignSb.append(AWS_ENCRYPTION_ALGORITHM).append('\n');
        stringToSignSb.append(isoDateTime).append('\n');
        String requestType = "aws4_request";
        String credentialScope = isoJustDate + "/" + awsRegion + "/" + awsService + "/" + requestType;
        stringToSignSb.append(credentialScope).append('\n');
        stringToSignSb.append(canonicalRequestHash);
        String stringToSign = stringToSignSb.toString();

        // (3)
        // https://docs.aws.amazon.com/general/latest/gr/sigv4-calculate-signature.html
        byte[] keySecret = ("AWS4" + awsSecretAccessKey).getBytes(StandardCharsets.UTF_8);
        byte[] keyDate = hmac(keySecret, isoJustDate);
        byte[] keyRegion = hmac(keyDate, awsRegion);
        byte[] keyService = hmac(keyRegion, awsService);
        byte[] keySigning = hmac(keyService, requestType);
        String signature = hex(hmac(keySigning, stringToSign));

        // finally we can build our Authorization header
        String authParameter = AWS_ENCRYPTION_ALGORITHM + " Credential=" + awsAccessKeyId + "/" + credentialScope
                + ",SignedHeaders=" + signedHeaders + ",Signature=" + signature;
        resultHeaderMap.put("Authorization", authParameter);
        return resultHeaderMap;
    }

    private static void appendOptionalString(StringBuilder sb, String str) {
        if (str != null) {
            sb.append(str);
        }
        sb.append('\n');
    }

    /**
     * replaces multiple whitespace character blocks into a single space, removes
     * whitespace from the front and end
     */
    private static String normalizeSpaces(String value) {
        StringBuilder sb = new StringBuilder(value.length());
        boolean prevSpace = false;
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (Character.isWhitespace(ch)) {
                if (sb.length() == 0) {
                    // skip spaces at the start
                } else {
                    // note that we saw a space which reduces multiples and ignores spaces
                    // this filters whitespace at the end of the string because they only emitted
                    // before the next char
                    prevSpace = true;
                }
                continue;
            }
            if (prevSpace) {
                // this changes all whitespace into a space
                sb.append(' ');
                prevSpace = false;
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    private static String hex(byte[] bytes) {
        final String HEX_CHARACTERS = "0123456789abcdef";
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            int val = (bytes[i] & 0xFF);
            sb.append(HEX_CHARACTERS.charAt(val >>> 4));
            sb.append(HEX_CHARACTERS.charAt(val & 0x0F));
        }
        return sb.toString();
    }

    private static byte[] sha256(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(bytes);
        return digest.digest();
    }

    private static byte[] hmac(byte[] key, String input) throws GeneralSecurityException {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key, "HmacSHA256"));
        return mac.doFinal(input.getBytes(StandardCharsets.UTF_8));
    }

    public static void main(String[] args) throws Exception {
        String method = "GET";
        String host = "s...2.jus.br";
        String path = "/mybucket/2023/3/17/15/54/67103b04-c020-48de-809d-81df32b7dd35.zip";
        String query = null;
        // probably should come from a file or something if doing a PUT or POST
        String body = null;
        String awsIdentity = "3e8...";
        String awsSecret = "qe...";
        String awsRegion = "us-west-2";
        String awsService = "s3";

        // optional headers
        Map<String, String> headers = new HashMap<>();

        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        String dateHeader = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'UTC'").format(zonedDateTime);
        headers.put("Date", dateHeader);
        String isoDateTime = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'").format(zonedDateTime);

        // this is a linked hashmap and the headers must be in a specific order
        Map<String, String> requestHeaders = AmazonRequestSignatureV4Utils.calculateAuthorizationHeaders(method, host,
                path, query, headers,
                body != null ? body.getBytes() : null, isoDateTime, awsIdentity, awsSecret, awsRegion, awsService);

        UnsafeSSLHelper unsafeSSLHelper = new UnsafeSSLHelper();
        ;

        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setSslcontext(unsafeSSLHelper.createUnsecureSSLContext())
                .setHostnameVerifier(unsafeSSLHelper.getPassiveX509HostnameVerifier()).build();) {
            HttpGet request = new HttpGet("https://" + host + path);
            List<Header> finalHeaders = new ArrayList<>(requestHeaders.size());
            // these must be in a specific order
            for (Entry<String, String> entry : requestHeaders.entrySet()) {
                finalHeaders.add(new BasicHeader(entry.getKey(), entry.getValue()));
            }
            // this overrides any previous headers specified
            request.setHeaders(finalHeaders.toArray(new Header[requestHeaders.size()]));
            try (CloseableHttpResponse response = httpClient.execute(request);) {
                System.out.println("response status: " + response.getStatusLine());
                System.out.println("response body:");
                System.out.println(IOUtils.toString(response.getEntity().getContent()));
            }
        }
    }
}