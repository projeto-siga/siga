package br.gov.jfrj.siga.hibernate;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.ListIterator;
import java.util.logging.Logger;

import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.hibernate.ext.IMontadorQuery;
import br.gov.jfrj.siga.hibernate.ext.MontadorQuery;

/**
 * Classe que trata da lógica de carregamento da extensão de busca textual em outro classloader. 
 * Isso é necessário, pois durante o redeploy da aplicação é necessário um reinício das instâncias do servidor de aplicação.
 * @author kpf
 *
 */
public class CarregadorPlugin {

	Logger log = Logger.getLogger("siga.ex.ext");
	
	ClassLoaderRecarregavel classloader = null;
	
	/**
	 * Inicializa o carregador de plugins usando o mesmo classpath do classloader em execução
	 */
	public CarregadorPlugin() {

		Enumeration<URL> resources;
		try {
			resources = Thread.currentThread().getContextClassLoader().getResources("/br");
			ArrayList<URL> list = Collections.list(resources);

			ListIterator<URL> iterator = list.listIterator();
			while(iterator.hasNext()){
				URL url = iterator.next();
				if (!url.getPath().contains("siga-ext")){
					iterator.remove();
				}else{
					iterator.set(new URL(url.getPath().replaceAll("\\.jar.*", ".jar")));
				}
			}
			URL implDefault = Thread.currentThread().getContextClassLoader().getResource("br/gov/jfrj/siga/hibernate/ext/MontadorQuery.class");
			list.add(new URL(implDefault.toExternalForm().toString().replaceAll("\\.jar.*", ".jar").replace("vfs:", "file:")));
			
			URL[] classpath = new URL[list.size()];
			list.toArray(classpath);
			
			this.classloader = new ClassLoaderRecarregavel(classpath);
		} catch (IOException e) {
			log.severe("Não foi possível criar o classloader recarregável.");
		}
		
		
	}
	
	/**
	 * Retorna o componente padrão. Normalmente utilizada quando um plugin não está disponível
	 * @return - instância padrão
	 */
	public IMontadorQuery getMontadorQueryDefault() {
		try {
			return new MontadorQuery();
		} catch (Exception e) {
			log.severe("Não foi possível instanciar o MontadorQuery default! Será utilizado o MontadorQuery no mesmo classloader da aplicação e isso"
					+ " poderá exigir o reinicio da instância do servidor de aplicação durante o redeploy");
		}
		return null;
	}

	/**
	 * Retona a implementação disponibilizada pelo plugin
	 * @return - instância personalizada pelo plugin
	 */
	public IMontadorQuery getMontadorQueryImpl() {
		try {
			return (IMontadorQuery) Class.forName(Prop.get("montador.query"),true,this.classloader).newInstance();
		} catch (Exception e) {
			log.warning("Não foi possível instanciar o MontadorQuery do plugin!");
		}
		return getMontadorQueryDefault();
	}

}
