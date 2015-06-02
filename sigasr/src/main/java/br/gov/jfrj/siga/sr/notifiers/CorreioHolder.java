package br.gov.jfrj.siga.sr.notifiers;

public abstract class CorreioHolder {

    private CorreioHolder() {

    }

    private static final ThreadLocal<Correio> THREAD_LOCAL = new ThreadLocal<Correio>();

    public static void set(Correio correio) {
        THREAD_LOCAL.set(correio);
    }

    public static Correio get() {
        return THREAD_LOCAL.get();
    }

    public static void unset() {
        THREAD_LOCAL.remove();
    }
}
