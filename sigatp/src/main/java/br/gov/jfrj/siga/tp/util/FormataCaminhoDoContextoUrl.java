package br.gov.jfrj.siga.tp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FormataCaminhoDoContextoUrl {
    public String retornarCaminhoContextoUrl(String url) {
        Matcher m;
        String pattern = "";

//        if (Http.Request.current() == null) {
//            // Para as urls iniciadas com application.base
//            pattern = "[\\w]*.[\\w]*";
//        }

//        else {
            // Para as urls iniciadas com http://
            pattern = ".*://([^:^/]*)(:\\d*)?(.*)?";
//        }

        Pattern r = Pattern.compile(pattern);
        m = r.matcher(url);

        if (m.find()) {
            return m.group(3).toString().substring(1);
        } else {
            return url;
        }
    }
}