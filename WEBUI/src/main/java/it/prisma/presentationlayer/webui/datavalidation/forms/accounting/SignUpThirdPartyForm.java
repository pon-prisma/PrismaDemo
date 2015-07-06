package it.prisma.presentationlayer.webui.datavalidation.forms.accounting;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Defines the representation of a PRISMA user at the time of registration on
 * the portal.
 * 
 * @author <a href="mailto:v.denotaris@reply.it">Vincenzo De Notaris</a>
 * @version 0.1.0
 * @since 0.1.0
 * @see <a
 *      href="http://www.istc.cnr.it/project/prisma-piattaforme-cloud-interoperabili-smart-government">Progetto
 *      PRISMA</a>
 */
public class SignUpThirdPartyForm implements
		Serializable {

	private static final long serialVersionUID = 6506123736572669029L;

	// First Name. Must comply with the following rules:
	// - Must contain only alphabetic characters;
	// - Can include spaces and hyphens;
	// - Minimum length of 2 characters, maximum length 20.
	@Size(min = 2, max = 20)
	@Pattern(regexp = "^[\\p{L}\\s'.-]+$")
	private String firstName;
	
	// Last Name. Must comply with the following rules:
	// - Must contain only alphabetic characters;
	// - Can include spaces and hyphens;
	// - Minimum length of 2 characters, maximum length 20.
	@Size(min = 2, max = 20)
	@Pattern(regexp = "^[\\p{L}\\s'.-]+$")
	private String lastName;

	// Company Name or organization Name. Its length
	// can not exceed 40 characters.
	@Size(min = 3, max = 40)
	private String employer;

	// Email to a referent. Must comply with the rules on permissible formalism
	// to an email address.
	@Size(min = 5, max = 40)
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
	private String emailRef;

	// Optional
	private String workTelephone;

	// Optional
	private String personalTelephone;

	@NotNull
	@Size(min = 16, max = 16)
	@Pattern(regexp = "^([A-Za-z]{6}[0-9lmnpqrstuvLMNPQRSTUV]{2}[abcdehlmprstABCDEHLMPRST]{1}[0-9lmnpqrstuvLMNPQRSTUV]{2}[A-Za-z]{1}[0-9lmnpqrstuvLMNPQRSTUV]{3}[A-Za-z]{1})|([0-9]{11})$")
	private String fiscalCode;

	@NotNull
	@Size(min = 2, max = 2)
	private String nationality;

	/**
	 * Returns first name and last name associated with the current user in
	 * PRISMA.
	 * 
	 * @return fullname First name and last name associated with the current
	 *         user.
	 */
	public String getFullName() {
		return firstName + " " + lastName;
	}

	/**
	 * Returns the first name associated with the current user in PRISMA.
	 * 
	 * @return first name associated with the current user.
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Sets the first name of the current user in PRISMA.
	 * 
	 * @param firstName
	 *            First name of the current user.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns the last name of the current user in PRISMA.
	 * 
	 * @return last Name associated with the current user.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the name associated with the current user in PRISMA
	 * 
	 * @param lastname
	 *            Last name associated with the current user.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns the email address of professional reference of the current user
	 * in PRISMA.
	 * 
	 * @return Email address of professional reference of the current user.
	 */
	public String getEmailRef() {
		return emailRef;
	}

	/**
	 * Sets the email address of professional reference of the current user in
	 * PRISMA.
	 * 
	 * @param email
	 *            Email address of professional reference of the current user.
	 */
	public void setEmailRef(String emailRef) {
		this.emailRef = emailRef;
	}

	/**
	 * Returns the name of the company or organization of the current user in
	 * PRISMA.
	 * 
	 * @return Name of the company or name of organization of the current user.
	 */
	public String getEmployer() {
		return employer;
	}

	/**
	 * Sets the name of the company or organization of the current user in
	 * PRISMA.
	 * 
	 * @param employer
	 *            Name of the company or name of organization of the current
	 *            user.
	 */
	public void setEmployer(String employer) {
		this.employer = employer;
	}

	public String getPersonalTelephone() {
		return personalTelephone;
	}

	public void setPersonalTelephone(String personalTelephone) {
		this.personalTelephone = personalTelephone;
	}

	public String getWorkTelephone() {
		return workTelephone;
	}

	public void setWorkTelephone(String workTelephone) {
		this.workTelephone = workTelephone;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getFiscalCode() {
		return fiscalCode;
	}

	public void setFiscalCode(String fiscalCode) {
		this.fiscalCode = fiscalCode;
	}

}