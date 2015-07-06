package it.prisma.businesslayer.orchestrator.dsl;

import java.io.Serializable;

public class Credentials implements Serializable {

	public enum CredentialsType {
		ANONYMOUS, USERNAME_PASSWORD, TOKEN, CERTIFICATE
	}

	private CredentialsType type;
	private String username;
	private String password;
	private String token;
	private Object customCredentialsData;

	public CredentialsType getType() {
		return type;
	}

	public void setType(CredentialsType type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Object getCustomCredentialsData() {
		return customCredentialsData;
	}

	public void setCustomCredentialsData(Object customCredentialsData) {
		this.customCredentialsData = customCredentialsData;
	}

}