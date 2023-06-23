package br.gov.jfrj.siga.ex.util;

import br.gov.jfrj.siga.cp.util.XjusUtils;

public enum ExXjusRecordServiceEnum {
    DOC, MOV;

    public static String formatIdAndService(Long id, ExXjusRecordServiceEnum service) {
        return XjusUtils.formatId(id) + "-" + service.ordinal();
    }

}
