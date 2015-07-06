package it.prisma.dal.entities.organization;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "OrganizationReferent")
public class OrganizationReferent implements java.io.Serializable {

	private static final long serialVersionUID = 3351424529279635160L;
	private long organizationReferentId;
	private Organization organization;
	private String firstName;
	private String middleName;
	private String lastName;
	private String email;
	private String phone;

	public OrganizationReferent() {
	}

	public OrganizationReferent(long organizationReferentId,
			Organization organization, String firstName, String lastName,
			String email) {
		this.organizationReferentId = organizationReferentId;
		this.organization = organization;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public OrganizationReferent(long organizationReferentId,
			Organization organization, String firstName, String middleName,
			String lastName, String email, String phone) {
		this.organizationReferentId = organizationReferentId;
		this.organization = organization;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "organizationReferentID", unique = true, nullable = false)
	public long getId() {
		return this.organizationReferentId;
	}

	public void setId(long organizationReferentId) {
		this.organizationReferentId = organizationReferentId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Organization_organizationID", nullable = false)
	public Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
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

	@Column(name = "email", nullable = false, length = 100)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "phone", length = 45)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
