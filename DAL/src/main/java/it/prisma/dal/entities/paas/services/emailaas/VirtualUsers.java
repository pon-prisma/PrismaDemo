package it.prisma.dal.entities.paas.services.emailaas;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Configurations generated by hbm2java
 */
@Entity
@Table(name = "virtual_users") // schema="mailserverdb"
public class VirtualUsers implements java.io.Serializable {

	private static final long serialVersionUID = 5118051185605529700L;

	private Long id;
	private Long virtual_domain_id;
	private Long user_prisma_id;
	private String user;
	private String password;
	private String email;

	public VirtualUsers() {
	}

	public VirtualUsers(Long virtual_domain_id, Long user_prisma_id,
			String user, String password, String email) {
		super();
		this.virtual_domain_id = virtual_domain_id;
		this.user_prisma_id = user_prisma_id;
		this.user = user;
		this.password = password;
		this.email = email;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "virtual_domain_id")
	public Long getDomain_id() {
		return virtual_domain_id;
	}

	public void setDomain_id(Long virtual_domain_id) {
		this.virtual_domain_id = virtual_domain_id;
	}

	@Column(name = "user_prisma_id")
	public Long getUser_prisma_id() {
		return user_prisma_id;
	}

	public void setUser_prisma_id(Long user_prisma_id) {
		this.user_prisma_id = user_prisma_id;
	}

	@Column(name = "user")
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Column(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
