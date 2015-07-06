package it.prisma.businesslayer.bizlib.paas.services.appaas.apprepo;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an object in OpenStack Object Storage, also with related metadata.
 * 
 * @author l.biava
 *
 */
public class StoredObject {
	private InputStream inputStream;
	private Map<String, Object> metadata = new HashMap<String, Object>();

	public StoredObject(InputStream inputStream, Map<String, Object> metadata) {
		super();
		this.inputStream = inputStream;
		this.metadata = metadata;
	}

	public StoredObject(InputStream inputStream) {
		super();
		this.inputStream = inputStream;
	}

	/**
	 * 
	 * @return the input stream to read the file from.
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/**
	 * 
	 * @return the metadata associated to the file. <b>Currently all the
	 *         response headers !</b>.
	 */
	public Map<String, Object> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, Object> metadata) {
		this.metadata = metadata;
	}
}
