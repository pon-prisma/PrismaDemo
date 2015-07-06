package it.prisma.dal.entities.accounting;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "UserProfile")
public class UserProfile implements java.io.Serializable {

	private static final long serialVersionUID = 9037292725019582507L;
	private long userAccountId;
	private UserAccount userAccount;
	private String firstName;
	private String middleName;
	private String lastName;
	private String personalPhone;
	private String employer;
	private String emailRef;
	private String workPhone;
	private String nationality;
	private String taxcode;
	private String avatarUri;
	private Date createdAt;
	private Date modifiedAt;

	public UserProfile() {
	}

	public UserProfile(UserAccount userAccount, String firstName,
			String lastName, String employer, String emailRef,
			String nationality, String taxcode, Date createdAt, Date modifiedAt) {
		this.userAccount = userAccount;
		this.firstName = firstName;
		this.lastName = lastName;
		this.employer = employer;
		this.emailRef = emailRef;
		this.nationality = nationality;
		this.taxcode = taxcode;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	public UserProfile(UserAccount userAccount, String firstName,
			String middleName, String lastName, String personalPhone,
			String employer, String emailRef, String workPhone,
			String nationality, String taxcode, String avatarUri,
			Date createdAt, Date modifiedAt) {
		this.userAccount = userAccount;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.personalPhone = personalPhone;
		this.employer = employer;
		this.emailRef = emailRef;
		this.workPhone = workPhone;
		this.nationality = nationality;
		this.taxcode = taxcode;
		this.avatarUri = avatarUri;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}
	
	public UserProfile(UserAccount userAccount, String firstName,
			String middleName, String lastName, String personalPhone,
			String employer, String emailRef, String workPhone,
			String nationality, String taxcode) {
		this.userAccount = userAccount;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.personalPhone = personalPhone;
		this.employer = employer;
		this.emailRef = emailRef;
		this.workPhone = workPhone;
		this.nationality = nationality;
		this.taxcode = taxcode;
	}

	@GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "userAccount"))
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "userAccountID", unique = true, nullable = false)
	public long getUserAccountId() {
		return this.userAccountId;
	}

	public void setUserAccountId(long userAccountId) {
		this.userAccountId = userAccountId;
	}


	@OneToOne(fetch = FetchType.EAGER, mappedBy="userProfile")	
	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	@Column(name = "firstName", nullable = false, length = 45)
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "middleName", length = 45)
	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@Column(name = "lastName", nullable = false, length = 45)
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "personalPhone", length = 45)
	public String getPersonalPhone() {
		return this.personalPhone;
	}

	public void setPersonalPhone(String personalPhone) {
		this.personalPhone = personalPhone;
	}

	@Column(name = "employer", nullable = false, length = 100)
	public String getEmployer() {
		return this.employer;
	}

	public void setEmployer(String employer) {
		this.employer = employer;
	}

	@Column(name = "emailRef", nullable = false, length = 100)
	public String getEmailRef() {
		return this.emailRef;
	}

	public void setEmailRef(String emailRef) {
		this.emailRef = emailRef;
	}

	@Column(name = "workPhone", length = 45)
	public String getWorkPhone() {
		return this.workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	@Column(name = "nationality", nullable = false, length = 45)
	public String getNationality() {
		return this.nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality.toUpperCase();
	}

	@Column(name = "taxcode", nullable = false, length = 16)
	public String getTaxcode() {
		return this.taxcode;
	}

	public void setTaxcode(String taxcode) {
		this.taxcode = taxcode;
	}

	@Column(name = "avatarURI", length = 2083)
	public String getAvatarUri() {
		return this.avatarUri;
	}

	public void setAvatarUri(String avatarUri) {
		this.avatarUri = avatarUri;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdAt", nullable = false, length = 19, insertable = false, updatable = false)
	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modifiedAt", nullable = false, length = 19, insertable = false, updatable = false)
	public Date getModifiedAt() {
		return this.modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

}
