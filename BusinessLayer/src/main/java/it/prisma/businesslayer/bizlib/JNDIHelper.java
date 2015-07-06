package it.prisma.businesslayer.bizlib;

/**
 * This class provides facilities for JNDI lookup.
 *
 */
public class JNDIHelper {

	/**
	 * The application name (actually EJB module name) as defined in ejb-jar.xml
	 * file. <br/>
	 * <b>WARNING: MUST BE THE SAME !</b>
	 */
	public static final String AppName = "MIUR_PRISMA-2.1-BusinessLayer";
	public static final String EJBModuleName = "";

	public static String getJNDIName(Class<?> clazz) {
		return "java:global/" + AppName + "/" + EJBModuleName + "/"
				+ clazz.getSimpleName() + "!" + clazz.getCanonicalName();
	}

	public static final String EJB_BasePath = "java:global/" + AppName + "/";// +EJBModuleName+"/";
}