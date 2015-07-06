package it.prisma.businesslayer.bizlib.iaas.cloudify;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Deque;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

/**
 * TODO: CLASSE DA SISTEMARE !!
 */
/**
 * <p>
 * <b>TO BE TESTED</b>
 * </p>
 * 
 * <p>
 * This class is used to customize cloudify recipes
 * </p>
 * 
 * <p>
 * It is assumed that the .zip file contains some files and a folder. In the
 * folder there must be only one properties file
 * </p>
 * 
 * Usage sample:
 * 
 * <pre>
 * {
 * 	&#064;code
 * 	HashMap&lt;String, String&gt; param = new HashMap&lt;String, String&gt;();
 * 	param.put(&quot;pentahoUser&quot;, &quot;\&quot;matteo\&quot;&quot;);
 * 	param.put(&quot;pentahoUserPass&quot;, &quot;\&quot;password\&quot;&quot;);
 * 	CustomRecipe b = new CustomRecipe(ORIGINAL_RECIPE_PATH, null, param);
 * 	try {
 * 		System.out.println(b.customize());
 * 	} catch (IOException e) {
 * 		e.printStackTrace();
 * 	}
 * }
 * </pre>
 * 
 * @author m.bassi
 * 
 */

public class CustomRecipe {

	private String originalRecipePath;
	private String customizedRecipePath;
	private Map<String, String> param;
	private String propertyFilePath;

	/**
	 * 
	 * @param originalRecipePath
	 *            path to the source .zip file
	 * @param customizedRecipePath
	 *            path to the customized .zip file. If null the path will be
	 *            originalRecipePath.replace(".zip",
	 *            "_<random_UUID>_custom.zip");
	 * @param param
	 *            a map of &lt;key,value&gt; mapped properties to replace. <br/>
	 *            Usage example, Properties file: <br/>
	 *            <code>
	 *            dbName="#{db.name}"<br/>
	 *            dbPassword="#{db.password}"
	 *            <br/></code> Properties to replace: <br/>
	 *            <code>
	 *            Map<String, String> props = new HashMap<String, String>();<br/>
	 *            props.put("db.name","myDBName");<br/>
	 *            props.put("db.password","myDBPassword");
	 *            </code> <br/>
	 *            <b>Note that you SHOULD supply a replace strategy to properly
	 *            format the key for the concrete file. A default one is
	 *            available for the format <code>#{key}</code></b>.
	 */
	public CustomRecipe(String originalRecipePath, String customizedRecipePath,
			Map<String, String> param) {

		this.originalRecipePath = originalRecipePath;
		if (customizedRecipePath == null) {
			this.customizedRecipePath = originalRecipePath.replace(".zip", "_"
					+ UUID.randomUUID() + "_custom.zip");
		} else {
			this.customizedRecipePath = customizedRecipePath;
		}
		this.param = param;
	}

	/**
	 * See {@link CustomRecipe#CustomRecipe(String, String, Map)}. This
	 * constructor enables the choice of the property file to use for the
	 * customization.
	 * 
	 * @param originalRecipePath
	 * @param customizedRecipePath
	 * @param param
	 * @param propertyFilePath
	 *            the path in the zip of the property file to edit.
	 */
	public CustomRecipe(String originalRecipePath, String customizedRecipePath,
			Map<String, String> param, String propertyFilePath) {
		this(originalRecipePath, customizedRecipePath, param);
		this.propertyFilePath = propertyFilePath;
	}

	/**
	 * 
	 * Given a .zip file within a recipe like this:
	 * 
	 * <pre>
	 * 	/****************************************************************
	 * 	* Copyright (c) 2014 Giuseppe Galeota. All rights reserved
	 * 	*
	 * 	*****************************************************************
	 *      
	 * 	serviceName = "pentaho"
	 * 	version = "5.1"
	 * 	name = "biserver-ce-5.1.0.0-752"
	 * 	zipName = "${name}.zip"
	 * 	downloadPath = "http://sourceforge.net/projects/pentaho/files/Business%20Intelligence%20Server/${version}/${zipName}"
	 * 
	 * 	BAserverPort = "8080"
	 * 	pentahoUser = "pentaho"
	 * 	pentahoUserPass = "Prisma2013"
	 * </pre>
	 * 
	 * <p>
	 * this method return the path of the .zip file with the recipe with
	 * customized
	 * </p>
	 * 
	 * 
	 * 
	 * @return path to the customized recipe
	 * @throws IOException
	 */
	public String customize() throws IOException {

		// Check file
		File zipSourceFile = new File(originalRecipePath);

		String filePathNoExtension = FilenameUtils
				.getFullPath(customizedRecipePath)
				+ FilenameUtils.getBaseName(customizedRecipePath);

		File outputTmpFolder = new File(filePathNoExtension);
		File zipCustomizedFile = new File(customizedRecipePath);

		if (zipSourceFile.equals(null)) {
			throw new IOException("source file null");
		}
		if (zipCustomizedFile.equals(null)) {
			throw new IOException("output file null");
		}

		unzip(zipSourceFile, outputTmpFolder);

		File propertiesFile;
		if(propertyFilePath!=null)
		{
			propertiesFile = new File(outputTmpFolder+File.separator+propertyFilePath);
			if(!propertiesFile.exists())
				throw new FileNotFoundException("The specified property file ("+propertyFilePath+") does not exit in the recipe.");
		}
		else
			propertiesFile = findPropertiesFile(outputTmpFolder);

		// Properties prop = new Properties();

		replaceProperties(param, propertiesFile, propertiesFile);
		
		// loadProp(propertiesFile, prop);
		//
		// insertProp(prop);
		//
		// saveProp(propertiesFile, prop);
		
		zip(outputTmpFolder, zipCustomizedFile);

		// make sure directory exists
		if (outputTmpFolder.exists()) {
			try {
				delete(outputTmpFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return this.customizedRecipePath;
	}

	/**
	 * Replaces each occurrence of the given properties (&lt;key,value&gt;
	 * mapped) in the given properties file.
	 * 
	 * @param properties
	 *            a map of &lt;key,value&gt; mapped properties to replace. <br/>
	 *            Usage example, Properties file: <code>
	 *            dbName="#{db.name}"
	 *            dbPassword="#{db.password}"
	 *            </code> Properties to replace: <code>
	 *            Map<String, String> props = new HashMap<String, String>();
	 *            props.put("db.name","myDBName");
	 *            props.put("db.password","myDBPassword");
	 *            </code>
	 * @param originalFile
	 *            the properties source file.
	 * @param customFile
	 *            the properties destination file.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void replaceProperties(Map<String, String> properties,
			File originalFile, File customFile) throws FileNotFoundException,
			IOException {

		FileInputStream inStream = null;
		FileOutputStream outStream = null;

		try {
			inStream = new FileInputStream(originalFile);

			String content = IOUtils.toString(inStream, "UTF-8");

			for (Map.Entry<String, String> item : param.entrySet()) {
				content = content.replace(replaceStrategy
						.getReplacePropertyKeyFormatted(item.getKey()), item
						.getValue());
			}

			outStream = new FileOutputStream(customFile);

			IOUtils.write(content, outStream, "UTF-8");
		} finally {
			if (inStream != null)
				inStream.close();
			if (outStream != null)
				outStream.close();
		}
	}

	/**
	 * The interface is used to specify the replacement strategy to convert a
	 * key for the property substitution to the format used in a concrete file. <br/>
	 * Example, Properties file: <br/>
	 * <code>
	 *            dbName="#{db.name}"<br/>
	 *            dbPassword="#{db.password}"
	 *            </code><br/>
	 * The format used is <code>#{key}</code>.
	 * 
	 * @author l.biava
	 *
	 */
	public interface ReplaceStrategy {
		/**
		 * @return return the format description. Example: <code>#{key}</code>.
		 */
		public String getReplaceFormat();

		/**
		 * @param key
		 *            the key to format.
		 * @return return the key formatted properly. Example: key=db.name
		 *         returns <code>#{db.name}</code>.
		 */
		public String getReplacePropertyKeyFormatted(String key);
	}

	/**
	 * Implements a default {@link ReplaceStrategy} using the format
	 * <code>#{key}</code> to replace the key.
	 * 
	 * @author l.biava
	 *
	 */
	public class DefaultReplaceStrategy implements ReplaceStrategy {

		protected static final String format = "#{key}";

		@Override
		public String getReplaceFormat() {
			return format;
		}

		@Override
		public String getReplacePropertyKeyFormatted(String key) {
			return format.replace("key", key);
		}

	}

	protected ReplaceStrategy replaceStrategy = new DefaultReplaceStrategy();

	/**
	 * Sets the replace strategy to format the key name of the property.</br>
	 * The default strategy uses the format <code>#{key}</code> to replace the
	 * key.
	 * 
	 * @param strategy
	 */
	public void setReplaceStrategy(ReplaceStrategy strategy) {
		replaceStrategy = strategy;
	}

	/**
	 * Return the replace strategy to format the key name of the property.
	 */
	public ReplaceStrategy getReplaceStrategy() {
		return replaceStrategy;
	}

	// private void replace(File originalFile, File customFile) {
	// try {
	// FileReader fileReader = new FileReader(originalFile);
	// BufferedReader bufferedReader = new BufferedReader(fileReader);
	// StringBuffer stringBuffer = new StringBuffer();
	// String line;
	// while ((line = bufferedReader.readLine()) != null) {
	// stringBuffer.append(line);
	// stringBuffer.append("\n");
	// }
	// String propString=stringBuffer.toString();
	// for(Map.Entry<String, String> item : param.entrySet()){
	// propString.replace(item.getKey(),item.getValue());
	// }
	// PrintWriter pw = new PrintWriter(customFile);
	// pw.println(propString);
	// pw.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// bufferedReader.
	// }
	// }

	// private void saveProp(File properties, Properties prop) {
	//
	// try {
	// Writer writer = new FileWriter(properties);
	// prop.store(writer, null);
	// writer.close();
	// // Properties class inserts a comment line on top.
	// // The comment must be removed to be compliant with cloudify
	// // standard
	// // In addition the values ​​http:// are read badly. http:// ->
	// // http\:// must be corrected
	// FileReader fileReader = new FileReader(properties);
	// BufferedReader bufferedReader = new BufferedReader(fileReader);
	// StringBuffer stringBuffer = new StringBuffer();
	// String line;
	// while ((line = bufferedReader.readLine()) != null) {
	// if (!line.startsWith("#")) {
	// if (line.contains("http\\://")) {
	// line = line.substring(0, line.indexOf("\\"))
	// + line.substring(line.indexOf("\\") + 1);
	// }
	// stringBuffer.append(line);
	// stringBuffer.append("\n");
	// }
	// }
	// bufferedReader.close();
	// PrintWriter pw = new PrintWriter(properties);
	// pw.println(stringBuffer.toString());
	// pw.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	public void zip(File directory, File zipfile) throws IOException {
		URI base = directory.toURI();
		Deque<File> queue = new LinkedList<File>();
		queue.push(directory);
		OutputStream out = new FileOutputStream(zipfile);
		Closeable res = null;
		try {
			ZipOutputStream zout = new ZipOutputStream(out);
			res = zout;
			while (!queue.isEmpty()) {
				directory = queue.pop();
				for (File kid : directory.listFiles()) {
					String name = base.relativize(kid.toURI()).getPath();
					if (kid.isDirectory()) {
						queue.push(kid);
						name = name.endsWith("/") ? name : name + "/";
						zout.putNextEntry(new ZipEntry(name));
					} else {
						zout.putNextEntry(new ZipEntry(name));
						copy(kid, zout);
						zout.closeEntry();
					}
				}
			}
		} finally {
			res.close();
			out.close();
		}
	}

	public void unzip(File zipfile, File directory) throws IOException {
		ZipFile zfile = new ZipFile(zipfile);
		Enumeration<? extends ZipEntry> entries = zfile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			File file = new File(directory, entry.getName());
			if (entry.isDirectory()) {
				file.mkdirs();
			} else {
				file.getParentFile().mkdirs();
				InputStream in = zfile.getInputStream(entry);
				try {
					copy(in, file);
				} finally {
					in.close();
				}
			}
		}
		zfile.close();
	}

	// private void loadProp(File propertiesFile, Properties prop) {
	//
	// try {
	// FileReader fileReader = new FileReader(propertiesFile);
	// BufferedReader bufferedReader = new BufferedReader(fileReader);
	// StringBuffer stringBuffer = new StringBuffer();
	// String line;
	// while ((line = bufferedReader.readLine()) != null) {
	// if (!(line.startsWith("*")) && !(line.startsWith("/"))) {
	// stringBuffer.append(line);
	// stringBuffer.append("\n");
	// }
	// }
	// StringReader reader = new StringReader(stringBuffer.toString());
	// fileReader.close();
	// prop.load(reader);
	// reader.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// private void insertProp(Properties prop) {
	// Iterator<String> iterator = this.param.keySet().iterator();
	// while (iterator.hasNext()) {
	// String key = iterator.next().toString();
	// String value = this.param.get(key).toString();
	// prop.put(key, value);
	// }
	// }

	private void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		while (true) {
			int readCount = in.read(buffer);
			if (readCount < 0) {
				break;
			}
			out.write(buffer, 0, readCount);
		}
	}

	private void copy(File file, OutputStream out) throws IOException {
		InputStream in = new FileInputStream(file);
		try {
			copy(in, out);
		} finally {
			in.close();
		}
	}

	private void copy(InputStream in, File file) throws IOException {
		OutputStream out = new FileOutputStream(file);
		try {
			copy(in, out);
		} finally {
			out.close();
		}
	}

	private File findPropertiesFile(File folder) {

		File infolder = null;
		for (File f : folder.listFiles()) {
			if (f.isDirectory()) {
				infolder = f;
				break;
			}
		}
		File properties = null;
		if (folder != null) {
			for (File f : infolder.listFiles()) {
				if (f.getName().endsWith(".properties")) {
					properties = f;
					break;
				}
			}
		} else {
			throw new NullPointerException("File .properties not found");
		}

		return properties;
	}

	private void delete(File file) throws IOException {

		if (file.isDirectory()) {
			// directory is empty, then delete it
			if (file.list().length == 0) {
				file.delete();
			} else {
				// list all the directory contents
				String files[] = file.list();
				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);
					// recursive delete
					delete(fileDelete);
				}
				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					// FileDeleteStrategy.FORCE.delete(file);
					file.delete();
				}
			}
		} else {
			// if file, then delete it
			file.delete();
			// FileDeleteStrategy.FORCE.delete(file);
		}
	}

}
