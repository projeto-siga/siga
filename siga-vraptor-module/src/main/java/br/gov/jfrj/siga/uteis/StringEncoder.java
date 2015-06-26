package br.gov.jfrj.siga.uteis;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class StringEncoder {

    public static String encode(String value, String enc) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, enc);
    }

    public static String decode(String value, String enc) throws UnsupportedEncodingException {
        return URLDecoder.decode(value, enc);
    }
}
