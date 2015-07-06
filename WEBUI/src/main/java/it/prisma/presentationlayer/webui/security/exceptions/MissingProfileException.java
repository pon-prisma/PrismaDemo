package it.prisma.presentationlayer.webui.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class MissingProfileException extends AuthenticationException {

	private static final long serialVersionUID = 3489119547625754240L;
	private String identityProviderEntityLabel;
	private String identityProviderEntityId;
	private String nameId;
	private String email;

	public MissingProfileException(String msg) {
		super(msg);
	}
	
	public MissingProfileException(String identityProviderLabel, String identityProviderEntityId, String nameId, String email) {
		super("Missing profile");
		this.setIdentityProviderEntityId(identityProviderEntityId);
		this.setNameId(nameId);
		this.setEmail(email);
		this.setIdentityProviderEntityLabel(identityProviderLabel);
	}

	public String getIdentityProviderEntityId() {
		return identityProviderEntityId;
	}

	public void setIdentityProviderEntityId(String identityProviderEntityId) {
		this.identityProviderEntityId = identityProviderEntityId;
	}

	public String getNameId() {
		return nameId;
	}

	public void setNameId(String nameId) {
		this.nameId = nameId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdentityProviderEntityLabel() {
		return identityProviderEntityLabel;
	}

	public void setIdentityProviderEntityLabel(
			String identityProviderEntityLabel) {
		this.identityProviderEntityLabel = identityProviderEntityLabel;
	}

}
