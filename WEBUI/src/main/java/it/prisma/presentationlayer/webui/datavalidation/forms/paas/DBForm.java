package it.prisma.presentationlayer.webui.datavalidation.forms.paas;

import it.prisma.presentationlayer.webui.datavalidation.forms.DeployForm;
import it.prisma.utils.validation.RegularExpressionList;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class DBForm extends DeployForm {

	private static final long serialVersionUID = 1L;

	@NotNull
	private String volume;

	@NotNull
	private String dbmsSelect;
	
	@Size(min = 4, max = 40)
	private String databaseName;

//	@NotNull
//	private String backup;

//	private String publicip;
	
//	private String encryptedDisk;
//
//	private String autoscaling;
//
//	private String maxFlavor;
//
//	private String maxVolume;

	@Size(min = 4, max = 40)
	@Pattern(regexp = RegularExpressionList.ALPHANUMERIC_PATTERN)
	private String rootPassword;

	@Size(min = 4, max = 40)
	@Pattern(regexp = RegularExpressionList.ALPHANUMERIC_PATTERN)
	private String repeatRootPassword;

	private String otherUser;

	private String username;

	private String usernamePassword;

	private String repeatUsernamePassword;

	private String permission;

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getDbmsSelect() {
		return dbmsSelect;
	}

	public void setDbmsSelect(String dbmsSelect) {
		this.dbmsSelect = dbmsSelect;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getRootPassword() {
		return rootPassword;
	}

	public void setRootPassword(String rootPassword) {
		this.rootPassword = rootPassword;
	}

	public String getRepeatRootPassword() {
		return repeatRootPassword;
	}

	public void setRepeatRootPassword(String repeatRootPassword) {
		this.repeatRootPassword = repeatRootPassword;
	}

	public String getOtherUser() {
		return otherUser;
	}

	public void setOtherUser(String otherUser) {
		this.otherUser = otherUser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsernamePassword() {
		return usernamePassword;
	}

	public void setUsernamePassword(String usernamePassword) {
		this.usernamePassword = usernamePassword;
	}

	public String getRepeatUsernamePassword() {
		return repeatUsernamePassword;
	}

	public void setRepeatUsernamePassword(String repeatUsernamePassword) {
		this.repeatUsernamePassword = repeatUsernamePassword;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	@Override
	public String toString() {
		return "DBForm [volume=" + volume + ", dbmsSelect=" + dbmsSelect
				+ ", rootPassword=" + rootPassword + ", repeatRootPassword="
				+ repeatRootPassword + ", otherUser=" + otherUser
				+ ", username=" + username + ", usernamePassword="
				+ usernamePassword + ", repeatUsernamePassword="
				+ repeatUsernamePassword + ", permission=" + permission + "]";
	}


}
