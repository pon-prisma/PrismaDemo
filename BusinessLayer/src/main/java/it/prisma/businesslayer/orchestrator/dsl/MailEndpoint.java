package it.prisma.businesslayer.orchestrator.dsl;

public class MailEndpoint extends AbstractEndpoint {

	public static final String NAME = "Mail";

	private String emailDomain;
	private String infoUser;

	public String getEmailDomain() {
		return emailDomain;
	}

	public void setEmailDomain(String emailDomain) {
		this.emailDomain = emailDomain;
	}

	public String getInfoUser() {
		return infoUser;
	}

	public void setInfoUser(String infoUser) {
		this.infoUser = infoUser;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String toString() {
		return "MailEndpoint [emailDomain=" + emailDomain + ", infoUser="
				+ infoUser + "]";
	}

}
