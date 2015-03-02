package org.jboss.as.jpa.hibernate4.infinispan;
 
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
 
import org.hibernate.cache.CacheException;
import org.hibernate.cache.infinispan.util.CacheCommandFactory;
import org.infinispan.AdvancedCache;
import org.infinispan.commands.module.ModuleCommandFactory;
import org.infinispan.factories.GlobalComponentRegistry;
import org.infinispan.manager.EmbeddedCacheManager;
 
/**
 * Put in modules/org/jboss/as/jpa/hibernate/4
 * Takes advantages of the classic {@link org.hibernate.cache.infinispan.JndiInfinispanRegionFactory, but adding the CacheCommandFactory to solve classloading issue HIBERNATE-134
 */
public class JndiInfinispanRegionFactory extends org.hibernate.cache.infinispan.JndiInfinispanRegionFactory {
 
    private static final long serialVersionUID = -3849525520794104001L;
 
    public JndiInfinispanRegionFactory() {
		super();
    }
 
    public JndiInfinispanRegionFactory(Properties props) {
		super(props);
    }
 
    @Override
    protected EmbeddedCacheManager createCacheManager(Properties properties) throws CacheException {
		EmbeddedCacheManager manager = super.createCacheManager(properties);
 
		CacheCommandFactory ccf = new CacheCommandFactory();
		// ccf.setRegionFactory(this);
		for (String cacheName : manager.getCacheNames()) {
			GlobalComponentRegistry globalCr = manager.getCache(cacheName).getAdvancedCache().getComponentRegistry().getGlobalComponentRegistry();
			Map<Byte, ModuleCommandFactory> factories = (Map<Byte, ModuleCommandFactory>) globalCr.getComponent("org.infinispan.modules.command.factories");
			if (factories == null || factories.size() == 0) {
				factories = new HashMap<Byte, ModuleCommandFactory>();
				factories.put((byte) 'a', ccf);
				globalCr.registerComponent(factories, "org.infinispan.modules.command.factories");
			}
		}
		return manager;
    }
 
    @SuppressWarnings("rawtypes")
    @Override
    protected AdvancedCache createCacheWrapper(AdvancedCache cache) {
		PrivilegedAction<ClassLoader> action = new PrivilegedAction<ClassLoader>() {
			public ClassLoader run() {
				return Thread.currentThread().getContextClassLoader();
			}
		};
		return cache.with(AccessController.doPrivileged(action));
    }
}
