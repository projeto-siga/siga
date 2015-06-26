package br.gov.jfrj.siga.uteis;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class StringEncoder {

    public static String encode(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "iso-8859-1");
    }

    public static String decode(String value) throws UnsupportedEncodingException {
        return URLDecoder.decode(value, "iso-8859-1");
    }
}
