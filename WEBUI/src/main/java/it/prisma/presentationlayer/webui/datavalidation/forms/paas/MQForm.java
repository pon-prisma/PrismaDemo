//TODO improve the validation

package it.prisma.presentationlayer.webui.datavalidation.forms.paas;

import it.prisma.presentationlayer.webui.datavalidation.forms.DeployForm;
import it.prisma.utils.validation.RegularExpressionList;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class MQForm extends DeployForm {

	private static final long serialVersionUID = 1L;

	@Size(min = 4, max = 40)
	@Pattern(regexp = RegularExpressionList.ALPHANUMERIC_PATTERN)
	private String username;

	@Size(min = 4, max = 40)
	@Pattern(regexp = RegularExpressionList.ALPHANUMERIC_PATTERN)
	private String userPassword;

	@Size(min = 4, max = 40)
	@Pattern(regexp = RegularExpressionList.ALPHANUMERIC_PATTERN)
	private String repeatUserPassword;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getRepeatUserPassword() {
		return repeatUserPassword;
	}

	public void setRepeatUserPassword(String repeatUserPassword) {
		this.repeatUserPassword = repeatUserPassword;
	}


}
