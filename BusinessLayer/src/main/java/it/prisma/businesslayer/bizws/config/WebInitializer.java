package it.prisma.businesslayer.bizws.config;

import org.springframework.web.context.AbstractContextLoaderInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class WebInitializer extends AbstractContextLoaderInitializer {

	@Override
	protected WebApplicationContext createRootApplicationContext() {
		AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
		applicationContext.register(AppConfiguration.class);
		return applicationContext;
	}
}

/**
 * Configuration entry-point for Spring WebMVC configuration. <br/>
 * This class sets up the Spring MVC Dispatcher, with proper context-root
 * (mapping), other servlet filters (avoiding the use of web.xml file). It also
 * refers to a root context configuration class, for more general
 * configurations.
 * 
 * @author bln
 *
 */
// public class WebInitializer extends
// AbstractAnnotationConfigDispatcherServletInitializer {
//
// @Override
// protected Class<?>[] getRootConfigClasses() {
// AbstractContextLoaderInitializer.
// // Root context configuration class
// return new Class[] { AppConfiguration.class };
// }
//
// @Override
// protected String[] getServletMappings() {
// // Context-root URL
// return new String[] { AppConfiguration.WEB_APP_URL_PREFIX + "/*" };
// }
//
// @Override
// protected Class<?>[] getServletConfigClasses() {
// // Other configuration classes
// // MVCConfiguration contains all the MVC Config & Beans
// return new Class[] { MVCConfiguration.class };
// }
//
// @Override
// protected Filter[] getServletFilters() {
// Filter[] filters;
//
// CharacterEncodingFilter encFilter;
// HiddenHttpMethodFilter httpMethodFilter = new HiddenHttpMethodFilter();
//
// encFilter = new CharacterEncodingFilter();
//
// encFilter.setEncoding("UTF-8");
// encFilter.setForceEncoding(true);
//
// filters = new Filter[] { httpMethodFilter, encFilter };
// return filters;
// }
// }