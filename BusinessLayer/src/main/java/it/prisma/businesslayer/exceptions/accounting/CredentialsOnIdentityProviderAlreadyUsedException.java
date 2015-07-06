package it.prisma.businesslayer.exceptions.accounting;

public class CredentialsOnIdentityProviderAlreadyUsedException extends AccountingException {

	private static final long serialVersionUID = 6960025962006096178L;
	
	public String getMessage(final Long identityProviderId, final String nameIdOnIdentityProvider) {
		return "NameId <" + nameIdOnIdentityProvider + "> already used on "
				+ "IdentityProvider with id <" + identityProviderId.toString() + ">";
	}

}
