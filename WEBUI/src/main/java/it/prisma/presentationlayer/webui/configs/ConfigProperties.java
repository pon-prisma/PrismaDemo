package it.prisma.presentationlayer.webui.configs;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({
	"classpath:application.properties",
	"classpath:version.properties"
})
public class ConfigProperties {

	@Value("${build.version}")
	private String projectBuildVersion;
	
	// Currently not supported
//	@Value("${build.date}")
//	private String projectBuildDate;
		
	public synchronized String getProjectVersion() {
	    String version = projectBuildVersion;
	    
	    if(version!=null)
	    	return version;

	    // try to load from maven properties first
	    try {
	        Properties p = new Properties();
	        InputStream is = getClass().getResourceAsStream("/META-INF/maven/it.prisma/prisma-webui/pom.properties");
	        if (is != null) {
	            p.load(is);
	            version = p.getProperty("version", "");
	        }
	    } catch (Exception e) {
	        // ignore
	    }

	    // fallback to using Java API
	    if (version == null) {
	        Package aPackage = getClass().getPackage();
	        if (aPackage != null) {
	            version = aPackage.getImplementationVersion();
	            if (version == null) {
	                version = aPackage.getSpecificationVersion();
	            }
	        }
	    }

	    if (version == null) {
	        // we could not compute the version so use a blank
	        version = "";
	    }

	    return version;
	} 
	
}
