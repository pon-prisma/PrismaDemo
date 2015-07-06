package it.prisma.businesslayer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

	private PropertiesReader() {
		// Empty
	}

	/**
	 * Loads the given property file.
	 * 
	 * @param path
	 *            The path of the file (from resources folder).
	 * @return the properties contained in the file.
	 * @throws IOException
	 *             if cannot access the file.
	 */
	public static Properties read(String path) throws IOException {
		InputStream inputStream = PropertiesReader.class.getClassLoader()
				.getResourceAsStream(path);
		Properties properties = new Properties();
		properties.load(inputStream);
		return properties;
	}

}
