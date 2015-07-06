package it.prisma.businesslayer.orchestrator.dsl;

import java.io.Serializable;

public abstract class AbstractEndpoint implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String url;
	private Credentials credentials;

	public abstract String getName();

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	@Override
	public String toString() {
		return "AbstractEndpoint [url=" + url + ", credentials=" + credentials
				+ "]";
	}
	
}
