package br.gov.jfrj.siga.hibernate;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Classloader que inverte a lógica de carregamento das classes. 
 * No java, por padrão, quando um classloader tenta carregar uma classe, ele verifica se ela já foi carregada pelo classloader pai.
 * Este classloder primeiro busca a classe em sua lista de classes e, caso não encontre-a, verifica se existe no classloader pai.
 * Essa inversão de carregamento permite que ao reiniciar a aplicação as classes do plugin permaneçam compatíveis com a interface ao qual deve ser atachada.  
 * @author kpf
 *
 */
public class ClassLoaderRecarregavel extends URLClassLoader {

	private URL[] urls;

	/**
	 * Define como classloader pai o próprio classloader em execução e inicializa o classpath de forma identica ao pai. 
	 * @param urls
	 */
	public ClassLoaderRecarregavel(URL[] urls) {
		super(urls,Thread.currentThread().getContextClassLoader());
		this.urls = urls;
	}

	/**
	 * Carrega a classe de forma inversa da lógica padrão do Java
	 */
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
        // Verifica classe já está carregada. Se tiver, use-a
        Class loadedClass = findLoadedClass(name);
        
        if (loadedClass == null) {
            try {
                // Tenta carregar a classe por este classloader recarregável em vez de ler primeiro do classloader pai
            	if (!name.endsWith("IMontadorQuery") && !name.endsWith("IExMobilDaoFiltro")){ // Ignora se for a classe da interface (que deve ser carregada palo classloader pai)
            		loadedClass = findClass(name);
            	}
            } catch (ClassNotFoundException e) {
            	//Ignora a exceção porque simplesmente a classe não existe neste classlooader recarregável
            }
            
            // Se a classe não existe localmente, carrega do modo padrão do Java (busca primeiro as classes carregadas no classloader pai)
            if (loadedClass == null) {
                loadedClass = getParent().loadClass(name);
            }
        } 

        return loadedClass;
	}
	
}
