package it.prisma.businesslayer.utils;

import java.io.File;

/**
 * This classes is an helper to get the paths to the folder of various kind of
 * resources.
 * 
 * @author l.biava
 *
 */
public class PathHelper {

	public enum ResourceType {
		CLOUDIFY_RECIPES("cloudify-recipes/"), CONFIG_PROPERTIES(""), CONFIG_ENV_VARIABLE_PROPERTIES(
				""), CONFIG_ENV_VARIABLE_PROPERTIES_PROFILES("var-configs-profiles/");

		private final String subPath;

		private ResourceType(String subPath) {
			this.subPath = subPath;
		}

		public String getSubPath() {
			return subPath;
		}

		@Override
		public String toString() {
			return subPath;
		}
	}

	private PathHelper() {
	}

	/**
	 * 
	 * @return the path of the folder containing all the resources (Unix
	 *         encoded).
	 */
	public static String getBaseResourcesPath() {
		String resourcePath = PathHelper.class.getProtectionDomain()
				.getCodeSource().getLocation().getFile()
				+ "/";

		// Fix for windows filesystem
		if (File.separatorChar == '\\')
			resourcePath = resourcePath.substring(1);

		return resourcePath;
	}

	/**
	 * 
	 * @param type
	 *            the resource type to get the path for.
	 * @return the path of the folder containing the given resource type.
	 */
	public static String getResourcesPath(ResourceType type) {
		return getBaseResourcesPath() + type.getSubPath();
	}

}
