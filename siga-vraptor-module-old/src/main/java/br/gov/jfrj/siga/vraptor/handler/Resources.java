package br.gov.jfrj.siga.vraptor.handler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import br.com.caelum.vraptor.http.route.Router;


import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

@ApplicationScoped
public final class Resources {

    private HashMap<Class<?>, List<Method>> classAndYourMethods = new HashMap<>();
    private static Resources instance;
    private Router router;

    public void setRouter(Router router) {
        this.router = router;
    }

    public Resources() {
    }

    public static synchronized Resources getInstance() {
        if (null == instance)
            instance = new Resources();

        return instance;
    }

    protected void setClassAndMethods(Class<?> classe, List<Method> methods) {
        if (!classAndYourMethods.containsKey(classe)) {
            classAndYourMethods.put(classe, methods);
        }
    }

    public Method getMethod(Class<?> classe, final String nomeDoMetodo) {

        List<Method> metodos = classAndYourMethods.get(classe);

        Method encontrado = Iterables.find(metodos, new Predicate<Method>() {

            @Override
            public boolean apply(Method metodoDaVez) {
                return metodoDaVez.getName().equals(nomeDoMetodo);
            }
        });

        return encontrado;
    }

    public String urlFor(Class<?> classe, String metodo, Object... params) {
        return router.urlFor(classe, getMethod(classe, metodo), params);
    }
}
