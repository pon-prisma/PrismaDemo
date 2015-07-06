package it.prisma.businesslayer.utils;

import it.prisma.businesslayer.bizlib.JNDIHelper;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * This class helps looking up objects from JNDI context.
 * @author l.biava
 *
 */
public class BeanManagerHelper {

	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz) {
		BeanManager bm = getBeanManager();
		Bean<T> bean = (Bean<T>) bm.getBeans(clazz).iterator().next();
		CreationalContext<T> ctx = bm.createCreationalContext(bean);
		return (T) bm.getReference(bean, clazz, ctx);
	}

	private static BeanManager getBeanManager() {
		try {
			InitialContext initialContext = new InitialContext();
			return (BeanManager) initialContext.lookup("java:comp/BeanManager");
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	// private static BeanManager getBeanManager() {
	// ServletContext servletContext = (ServletContext) FacesContext
	// .getCurrentInstance().getExternalContext().getContext();
	// return (BeanManager) servletContext
	// .getAttribute("javax.enterprise.inject.spi.BeanManager");
	// }
	public static Object getFromJNDI(String path) {
		try {
			InitialContext initialContext = new InitialContext();
			return initialContext.lookup(path);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Returns an object with the name 'java:app/<this-app-name>/<b><code>path</code></b>'.
	 * @param path The path of the object to lookup.
	 * @throws RuntimeException if the object was not found.
	 * @return an object with the name 'java:app/<this-app-name>/<b><code>path</code></b>'
	 */
	public static Object getFromJNDIGlobalThisApp(String path) {
		try {			
			String appName=JNDIHelper.AppName;
			InitialContext initialContext = new InitialContext();
			//String appName=(String)initialContext.lookup("java:app/AppName");
			return initialContext.lookup("java:global/"+appName+"/"+path);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}
	
	
}