//TODO improve the validation

package it.prisma.presentationlayer.webui.datavalidation.forms.paas;

import it.prisma.utils.validation.RegularExpressionList;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


public class EmailForm implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Pattern(regexp = RegularExpressionList.ALPHANUMERIC_WHITESPACE_PATTERN)
	private String accountName;
	
	@NotNull
	private long domainId;

	@NotNull
	private String domain;

	@NotNull
	private String emailPassword;

	@NotNull
	private String emailConfirmPassword;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	public String getEmailConfirmPassword() {
		return emailConfirmPassword;
	}

	public void setEmailConfirmPassword(String emailConfirmPassword) {
		this.emailConfirmPassword = emailConfirmPassword;
	}
	
	public long getDomainId() {
		return domainId;
	}

	public void setDomainId(long domainId) {
		this.domainId = domainId;
	}

}